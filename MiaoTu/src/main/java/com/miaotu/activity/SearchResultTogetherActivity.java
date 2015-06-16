package com.miaotu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.adapter.SearchResultTogetherAdapter;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.Together;
import com.miaotu.result.BaseResult;
import com.miaotu.result.MyTogetherResult;
import com.miaotu.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchResultTogetherActivity extends BaseActivity implements OnClickListener{

    private TextView tvLeft,tvTitle;
    private ListView listView;
    private static int PAGECOUNT=1000;
    private List<Together> togetherList;
    private SearchResultTogetherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_together);

        initView();
        bindView();
        initData();
    }

    private void initView(){
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        listView = (ListView) findViewById(R.id.lv_content);
    }

    private void bindView(){
        tvLeft.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchResultTogetherActivity.this, TogetherDetailActivity.class);
                intent.putExtra("id", togetherList.get(i).getId());
                startActivity(intent);
            }
        });
    }

    private void initData(){
        togetherList = new ArrayList<>();
        adapter = new SearchResultTogetherAdapter(this, togetherList);
        listView.setAdapter(adapter);
        getSearchResult(getIntent().getStringExtra("key"));
    }

    /**
     * 获取一起去的查询结果
     * @param key
     */
    private void getSearchResult(final String key){
        new BaseHttpAsyncTask<Void, Void, MyTogetherResult>(this, true){

            @Override
            protected void onCompleteTask(MyTogetherResult result) {
                if (result.getCode() == BaseResult.SUCCESS){
                    togetherList.clear();
                    if (result.getDateTourInfoList() == null){
                        adapter.notifyDataSetChanged();
                        return;
                    }
                    togetherList.addAll(result.getDateTourInfoList());
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
            protected MyTogetherResult run(Void... params) {
                return HttpRequestUtil.getInstance().getSearchResultTogether(
                        readPreference("token"), key, readPreference("latitude"),
                        readPreference("longitude"), "1", PAGECOUNT+"");
            }
        }.execute();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_left:
                finish();
                break;
        }
    }
}
