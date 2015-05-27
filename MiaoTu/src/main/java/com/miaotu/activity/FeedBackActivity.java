package com.miaotu.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.miaotu.R;

public class FeedBackActivity extends BaseActivity implements View.OnClickListener{

    private TextView tvCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        initView();
        bindVew();
    }

    private void initView(){
        tvCall = (TextView) this.findViewById(R.id.tv_call);
        /*String html = "<a href=\"\">联系我们</a>";
        CharSequence charSequence = Html.fromHtml(html);
        tvCall.setText(charSequence);*/
        SpannableString sp = new SpannableString("联系妙途");
        sp.setSpan(new URLSpan(""), 0, sp.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.text_href)),
                0, sp.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvCall.setText(sp);
//        tvCall.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void bindVew(){
        tvCall.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_call:
                Intent aboutIntent = new Intent(FeedBackActivity.this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
            default:
                break;
        }
    }
}
