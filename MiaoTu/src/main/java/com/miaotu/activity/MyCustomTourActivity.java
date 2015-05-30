package com.miaotu.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miaotu.R;
import com.miaotu.adapter.MyCustomTourAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.CustomTourInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.result.MyCustomTourResult;
import com.miaotu.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class MyCustomTourActivity extends BaseActivity {

    private TextView tvTitle, tvLeft;
    private PullToRefreshListView lvCustomTour;
    private static int PAGECOUNT=10;
    private int curPageCount = 0;
    private boolean isLoadMore = false;
    private List<CustomTourInfo> customTourInfoList;
    private MyCustomTourAdapter adapter;

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
    }

    private void initData(){
        customTourInfoList = new ArrayList<CustomTourInfo>();
        adapter = new MyCustomTourAdapter(this, customTourInfoList);
        lvCustomTour.setAdapter(adapter);

        String token = readPreference("token");
        String uid = readPreference("uid");
        getOwnerCustomerTour(token, uid, "owner", PAGECOUNT+"");
    }

    /**
     * 获取我发起的秒旅团
     * @param token
     * @param uid
     * @param type
     * @param num
     */
    private void getOwnerCustomerTour(final String token, final String uid,
                                      final String type, final String num){
        new BaseHttpAsyncTask<Void, Void, MyCustomTourResult>(this, true){

            @Override
            protected void onCompleteTask(MyCustomTourResult myCustomTourResult) {
                if (myCustomTourResult.getCode() == BaseResult.SUCCESS){
                    if(customTourInfoList == null){
                        return;
                    }
                    if (myCustomTourResult.getCustomTourInfolist() == null){
                        return;
                    }

                    //测试
                    CustomTourInfo info = new CustomTourInfo();
                    info.setHeadurl("");
                    info.setEnddate("5月30号");
                    info.setStartdate("4月30号");
                    info.setNickname("四小美");
                    info.setMtprice("199元");
                    info.setTitle("中华人民共和国");
                    myCustomTourResult.getCustomTourInfolist().add(info);
                    CustomTourInfo info1 = new CustomTourInfo();
                    info1.setHeadurl("");
                    info1.setEnddate("5月30号");
                    info1.setStartdate("4月30号");
                    info1.setNickname("四小美");
                    info1.setMtprice("199元");
                    info.setTitle("中华人民共和国");
                    myCustomTourResult.getCustomTourInfolist().add(info1);

                    customTourInfoList.clear();
                    customTourInfoList.addAll(myCustomTourResult.getCustomTourInfolist());
                    adapter.notifyDataSetChanged();

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
                return HttpRequestUtil.getInstance().getOwnerCustomerTour(token, uid, type, num);
            }
        }.execute();
    }
}
