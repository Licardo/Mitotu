package com.miaotu.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.util.LogUtil;

public class AboutActivity extends BaseFragmentActivity implements OnClickListener {

	private TextView tvLeft;
	private TextView tvTitle;
    private RelativeLayout layoutDial,layoutAddQQGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		findView();
		bindView();
		initView();
	}


    @Override
	protected void onDestroy() {
		super.onDestroy();
		tvLeft = null;
		tvTitle = null;
	}

	private void findView() {
		tvLeft = (TextView) findViewById(R.id.tv_left);
		tvTitle = (TextView) findViewById(R.id.tv_title);
        layoutDial = (RelativeLayout) findViewById(R.id.layout_dial);
        layoutAddQQGroup = (RelativeLayout) findViewById(R.id.layout_add_qq_group);

	}

	private void bindView() {
		tvLeft.setVisibility(View.VISIBLE);
		tvTitle.setVisibility(View.VISIBLE);
		tvLeft.setOnClickListener(this);
        layoutDial.setOnClickListener(this);
        layoutAddQQGroup.setOnClickListener(this);
	}

	private void initView() {
//		tvLeft.setBackgroundResource(R.drawable.icon_back);

		tvTitle.setText("关于我们");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		    case R.id.tv_left:
			    AboutActivity.this.finish();
			    break;
            case R.id.layout_dial:
                Intent intent1 = new Intent();
                intent1.setAction("android.intent.action.DIAL");
                intent1.setData(Uri.parse("tel:4008-030-085"));
                startActivity(intent1);
                break;
            case R.id.layout_add_qq_group:
                joinQQGroup();
                break;
		}
	}
    /****************
     *
     * 发起添加群流程。群号：杭州单身交友旅游(68481005) 的 key 为： mt69Ie5m5LHWcYzx-M1dQcXIpw9ZlFa9
     * 调用 joinQQGroup(mt69Ie5m5LHWcYzx-M1dQcXIpw9ZlFa9) 即可发起手Q客户端申请加群 杭州单身交友旅游(68481005)
     *
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup() {
        String key = "mt69Ie5m5LHWcYzx-M1dQcXIpw9ZlFa9";
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

}
