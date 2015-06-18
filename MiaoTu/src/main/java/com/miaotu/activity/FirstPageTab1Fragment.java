package com.miaotu.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miaotu.R;
import com.miaotu.adapter.FirstPageImageAdapter;
import com.miaotu.adapter.TogetherlistAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.Banner;
import com.miaotu.model.RegisterInfo;
import com.miaotu.model.Together;
import com.miaotu.result.BaseResult;
import com.miaotu.result.LoginResult;
import com.miaotu.result.TogetherResult;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.GuideGallery;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FirstPageTab1Fragment extends BaseFragment implements View.OnClickListener {
private View root;
    private PullToRefreshListView lvPull;
    private View head;
    private TogetherlistAdapter adapter;
    private List<Together> mList;
    private int page=1;
    private final int PAGECOUNT = 12;
    private boolean isLoadMore = false;
    private View layoutMore;
    private GuideGallery gallery; // 自定义轮播控件
    private boolean run = false;
    private LinearLayout layoutContainer;
    protected Handler imageChangeHandler = new MyHandler();
    private static final int CHANGE_IMG = 1;
    private Timer timer;
    Handler handler ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_first_page_tab1, container, false);
        head = inflater.inflate(R.layout.together_head, null);
        layoutMore = inflater.inflate(R.layout.pull_to_refresh_more, null);
        gallery = (GuideGallery) head.findViewById(R.id.default_gallery);
        layoutContainer = (LinearLayout) head.findViewById(R.id.layout_container);
        findView();
        bindView();
        init();
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void findView() {
        lvPull = (PullToRefreshListView) root.findViewById(R.id.lv_pull);
    }
    private void bindView() {
        lvPull.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),
                        TogetherDetailActivity.class);
                intent.putExtra("id", mList.get(position - 2).getId());
                getParentFragment().startActivityForResult(intent, position - 2);
            }
        });
        lvPull.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(
                        getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                getTogether(false);

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
//                loadMore();
            }

        });
        lvPull.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
//                showToastMsg("滑动到底了");
                if (!isLoadMore&&mList.size()==page*PAGECOUNT) {
                    loadMore(false);
                }
            }

        });
            }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void init() {
        lvPull.getRefreshableView().addHeaderView(head,null,false);
        mList = new ArrayList<>();
        adapter = new TogetherlistAdapter(getActivity(),mList,false, false);
        lvPull.setAdapter(adapter);
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Point size = new Point();
        wm.getDefaultDisplay().getSize(size);
        int width = size.x;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                width, width * 202 / 339);
        gallery.setLayoutParams(params);
        getTogether(true);
        handler = new Handler( ) {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        head.findViewById(R.id.tv_tip).setVisibility(View.GONE);
                        break;
                }
                super.handleMessage(msg);
            }
        };


    }
//获取一起去
    private void getTogether(final boolean isShow) {
        new BaseHttpAsyncTask<Void, Void, TogetherResult>(getActivity(), isShow) {
            @Override
            protected void onCompleteTask(TogetherResult result) {
                if(root==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    mList.clear();
                    if(result.getTogetherList()!=null){
                        mList.addAll(result.getTogetherList());
                    }
                    adapter.notifyDataSetChanged();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        }
                    }, 5000);
                    if(lvPull.getRefreshableView().getFooterViewsCount()==1&&mList.size()==PAGECOUNT*page){
                        lvPull.getRefreshableView().addFooterView(layoutMore);
                    }
                    if(result.getBannerList()!=null){
                        try {
//                            List<String> imagePathes = new ArrayList<String>();
                            List<Banner> banners = new ArrayList<Banner>();
                            for (Banner banner : result.getBannerList()) {
                                if (banner.getBid() != null) {
//                                    imagePathes.add(banner.getPicUrl());
                                    banners.add(banner);
                                } else {
//                                    imagePathes.add("");
                                }

                            }

                            gallery.bottomFrame = layoutContainer;
                            gallery.setAutoPlay(false);// 设置自动播放
//                            gallery.setImageSize(imagePathes.size());
                            gallery.setImageSize(banners.size());
                            gallery.initPoints();
                            if(!run) {
                                startPlayPic();
                            }
                            gallery.setPageMargin(0);
                            FirstPageImageAdapter adapter = new FirstPageImageAdapter(
                                    getActivity(), banners,
                                    true,R.layout.gallery_item_first_page,
                                    R.id.iv_gallery);// 最后一个参数true表示设置可以点击
                            gallery.setAdapter(adapter);
//                            gallery.setCurrentItem(imagePathes.size() * 5000);
                            gallery.setCurrentItem(banners.size() * 5000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if(StringUtil.isEmpty(result.getMsg())){
                        showToastMsg("获取约游列表失败！");
                    }else{
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected TogetherResult run(Void... params) {
                page=1;
                return HttpRequestUtil.getInstance().getTogetherList(
                        readPreference("token"),page+"",PAGECOUNT+"",
                        readPreference("latitude"),readPreference("longitude"));
            }

            @Override
            protected void finallyRun() {
                super.finallyRun();
                lvPull.onRefreshComplete();
            }
        }.execute();
    }
    private void startPlayPic() {
        //判断是否开启自动播放
        new Thread() {
            @Override
            public void run() {
                Log.d("Thread run:", "" + run);
                run = true;
                while (run) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Thread.interrupted();
                    }
                    Message msg = imageChangeHandler.obtainMessage();
                    msg.what = CHANGE_IMG;
                    imageChangeHandler.sendMessage(msg);
                }
            }
        }.start();
    }
    class MyHandler extends Handler {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == CHANGE_IMG) {
                if (gallery != null) {
                    gallery.setCurrentItem(gallery.getCurrentItem() + 1);
                }
            }

        }
    }
    //获取一起去
    private void loadMore(final boolean isShow) {
        new BaseHttpAsyncTask<Void, Void, TogetherResult>(getActivity(), isShow) {
            @Override
            protected void onCompleteTask(TogetherResult result) {
                if(root==null){
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    if(result.getTogetherList()==null){
                        return;
                    }
                    mList.addAll(result.getTogetherList());
                    adapter.notifyDataSetChanged();

                } else {
                    if(StringUtil.isEmpty(result.getMsg())){
                        showToastMsg("获取约游列表失败！");
                    }else{
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected TogetherResult run(Void... params) {
                isLoadMore = true;
                page+=1;
                return HttpRequestUtil.getInstance().getTogetherList(readPreference("token"),page+"",PAGECOUNT+"",readPreference("latitude"),readPreference("longitude"));
            }

            @Override
            protected void finallyRun() {
                isLoadMore=false;
                if(mList.size()!=PAGECOUNT*page){
                    lvPull.getRefreshableView().removeFooterView(layoutMore);
                }
                super.finallyRun();
            }
        }.execute();
    }
    @Override
    public void onClick(View view) {
        if(!Util.isNetworkConnected(getActivity())) {
            showToastMsg("当前未联网，请检查网络设置");
            return;
        }
        switch (view.getId()) {

        }
    }

    /**
     * 设置喜欢控件显示
     * @param position
     */
    public void modifyLikeView(int position, boolean flag){
        mList.get(position).setIsLike(flag);
        adapter.notifyDataSetChanged();
    }
}