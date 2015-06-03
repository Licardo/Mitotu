package com.miaotu.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miaotu.R;
import com.miaotu.adapter.MyCustomTourAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.CustomTour;
import com.miaotu.model.CustomTourInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.result.MyCustomTourResult;
import com.miaotu.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class MyCustomTourActivity extends BaseActivity implements View.OnClickListener{

    private TextView tvTitle, tvLeft;
    private PullToRefreshListView lvCustomTour;
    private static int PAGECOUNT=10;
    private int curPageCount = 0;
    private boolean isLoadMore = false;
    private List<CustomTour> customTourInfoList;
    private MyCustomTourAdapter adapter;
    private View layoutMore;
    private String token,uid,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_custom_tour);

        initView();
        initData();
    }

    private void initView() {
        tvLeft = (TextView) this.findViewById(R.id.tv_left);
        tvTitle = (TextView) this.findViewById(R.id.tv_title);
        lvCustomTour = (PullToRefreshListView) this.findViewById(R.id.lv_customtour);
        lvCustomTour.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(
                        MyCustomTourActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                getOwnerCustomerTour(token,uid,type);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadMore();
            }
        });
        tvLeft.setOnClickListener(this);
        tvTitle.setText("妙旅团");
    }

    private void initData(){
        customTourInfoList = new ArrayList<CustomTour>();
        adapter = new MyCustomTourAdapter(this, customTourInfoList);
        lvCustomTour.setAdapter(adapter);

        token = readPreference("token");
        uid = readPreference("uid");
        type = "owner";
        getOwnerCustomerTour(token, uid, type);
    }

    /**
     * 获取我发起的秒旅团
     * @param token
     * @param uid
     * @param type
     */
    private void getOwnerCustomerTour(final String token, final String uid,
                                      final String type){
        new BaseHttpAsyncTask<Void, Void, MyCustomTourResult>(this, true){

            @Override
            protected void onCompleteTask(MyCustomTourResult myCustomTourResult) {
                if (myCustomTourResult.getCode() == BaseResult.SUCCESS){
                    if(customTourInfoList == null){
                        return;
                    }
                    //测试
//                    CustomTour info = new CustomTour();
//                    info.setEndDate("5月30号");
//                    info.setStartDate("4月30号");
//                    info.setNickname("四小美");
//                    info.setMtPrice("199元");
//                    info.setTitle("中华人民共和国");
//                    myCustomTourResult.getCustomTourInfolist().add(info);
//                    CustomTour info1 = new CustomTour();
//                    info1.setEndDate("5月30号");
//                    info1.setStartDate("4月30号");
//                    info1.setNickname("四小美");
//                    info1.setMtPrice("199元");
//                    info1.setTags("贝克汉姆,卡卡,罗纳尔迪尼奥");
//                    info1.setTitle("中华人民共和国");
//                    myCustomTourResult.getCustomTourInfolist().add(info1);

                    customTourInfoList.clear();
                    if (myCustomTourResult.getCustomTourInfolist() == null){
                        adapter.notifyDataSetChanged();
                        return;
                    }
                    customTourInfoList.addAll(myCustomTourResult.getCustomTourInfolist());
                    adapter.notifyDataSetChanged();

                    if(lvCustomTour.getRefreshableView().getFooterViewsCount()==1&&customTourInfoList.size()==PAGECOUNT){
                        lvCustomTour.getRefreshableView().addFooterView(layoutMore);
                    }

                }else {
                    if (StringUtil.isBlank(myCustomTourResult.getMsg())){
                        showToastMsg("获取发起的秒旅团失败");
                    }else {
                        showToastMsg(myCustomTourResult.getMsg());
                    }
                }
            }

            @Override
            protected MyCustomTourResult run(Void... params) {
                curPageCount=PAGECOUNT;
                return HttpRequestUtil.getInstance().getOwnerCustomerTour(token, uid, type, curPageCount+"");
            }

            @Override
            protected void finallyRun() {
                if(customTourInfoList==null){
                    return;
                }
                lvCustomTour.onRefreshComplete();
            }
        }.execute();
    }

    private void loadMore(){
        new BaseHttpAsyncTask<Void, Void, MyCustomTourResult>(this, false){

            @Override
            protected void onCompleteTask(MyCustomTourResult myCustomTourResult) {
                if (myCustomTourResult.getCode() == BaseResult.SUCCESS){
                    if(customTourInfoList == null){
                        return;
                    }
                    if (myCustomTourResult.getCustomTourInfolist() == null){
                        return;
                    }

                    customTourInfoList.clear();
                    customTourInfoList.addAll(myCustomTourResult.getCustomTourInfolist());
                    adapter.notifyDataSetChanged();

                    if(customTourInfoList.size()!=curPageCount){
                        lvCustomTour.getRefreshableView().removeFooterView(layoutMore);
                    }

                }else {
                    if (StringUtil.isBlank(myCustomTourResult.getMsg())){
                        showToastMsg("获取发起的秒旅团失败");
                    }else {
                        showToastMsg(myCustomTourResult.getMsg());
                    }
                }
            }

            @Override
            protected MyCustomTourResult run(Void... params) {
                isLoadMore = true;
                curPageCount+=PAGECOUNT;
                return HttpRequestUtil.getInstance().getOwnerCustomerTour(token, uid, type, curPageCount+"");
            }

            @Override
            protected void finallyRun() {
                if(customTourInfoList==null){
                    return;
                }
                lvCustomTour.onRefreshComplete();
                isLoadMore = false;
            }
        }.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_left:
                finish();
                break;
            default:
                break;
        }
    }
}
