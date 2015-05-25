package com.miaotu.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.miaotu.R;

public class MyDatingTourActivity extends BaseActivity {

    private PullToRefreshListView pullToRefreshListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dating_tour);

        initView();
    }

    private void initView(){
        pullToRefreshListView = (PullToRefreshListView) this.findViewById(R.id.pulltorefresh_content);
    }
}
