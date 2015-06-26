package com.miaotu.activity;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.adapter.SearchResultCustomTourAdapter;
import com.miaotu.adapter.SearchResultTogetherAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.CustomTour;
import com.miaotu.model.Together;
import com.miaotu.result.BaseResult;
import com.miaotu.result.MyCustomTourResult;
import com.miaotu.result.MyTogetherResult;
import com.miaotu.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchResultCustomTourActicity extends BaseActivity implements View.OnClickListener{

    private TextView tvLeft,tvTitle;
    private ListView listView;
    private static int PAGECOUNT=1000;
    private List<CustomTour> customTours;
    private SearchResultCustomTourAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_custom_tour_acticity);

        intView();
        bindView();
        initData();
    }

    private void intView(){
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        listView = (ListView) findViewById(R.id.lv_content);
    }

    private void bindView(){
        tvLeft.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchResultCustomTourActicity.this, CustomTourDetailActivity.class);
                intent.putExtra("id", customTours.get(i).getId());
                startActivity(intent);
            }
        });
    }

    private void initData(){
        tvTitle.setText("妙旅团搜索结果");
        customTours = new ArrayList<>();
        adapter = new SearchResultCustomTourAdapter(this, customTours);
        listView.setAdapter(adapter);
        getSearchResult(getIntent().getStringExtra("key"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_left:
                finish();
                break;
        }
    }

    /**
     * 获取一起去的查询结果
     * @param key
     */
    private void getSearchResult(final String key){
        new BaseHttpAsyncTask<Void, Void, MyCustomTourResult>(this, true){

            @Override
            protected void onCompleteTask(MyCustomTourResult result) {
                if (result.getCode() == BaseResult.SUCCESS){
                    customTours.clear();
                    if (result.getCustomTourInfolist() == null){
                        adapter.notifyDataSetChanged();
                        return;
                    }
                    customTours.addAll(result.getCustomTourInfolist());
                    adapter.notifyDataSetChanged();
                }else {
                    if (StringUtil.isBlank(result.getMsg())){
                        showToastMsg("搜索失败");
                    }else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected MyCustomTourResult run(Void... params) {
                return HttpRequestUtil.getInstance().getSearchResultCustomTour(
                        readPreference("token"), key, "1", PAGECOUNT + "");
            }
        }.execute();
    }
}
