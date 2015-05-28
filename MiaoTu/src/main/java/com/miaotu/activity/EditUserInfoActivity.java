package com.miaotu.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.miaotu.R;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.ModifyPersonInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.result.PhotoUploadResult;
import com.miaotu.util.LogUtil;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.CircleImageView;
import com.miaotu.view.FlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EditUserInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_left, tv_title, tv_right;
    private Button btn_add;
    private EditText et_tag;
    private FlowLayout fl_tags;
    private ModifyPersonInfo userinfo;
    private EditText et_nickname, et_gender, et_age, et_address, et_emotion, et_job, et_wantgo;
    private RelativeLayout rl_changephoto;
    private List<String> alltags;
    private CircleImageView iv_head_photo;
    private static final String IMAGE_FILE_LOCATION = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/miaotu/temp.jpg";
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);//
    private String photourl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        initView();
        initData();
    }

    private void initView() {
        rl_changephoto = (RelativeLayout) this.findViewById(R.id.rl_changephoto);
        iv_head_photo = (CircleImageView) this.findViewById(R.id.iv_head_photo);
        et_wantgo = (EditText) this.findViewById(R.id.et_wantgo);
        et_job = (EditText) this.findViewById(R.id.et_job);
        et_address = (EditText) this.findViewById(R.id.et_address);
        et_emotion = (EditText) this.findViewById(R.id.et_emotion);
        et_age = (EditText) this.findViewById(R.id.et_age);
        et_gender = (EditText) this.findViewById(R.id.et_gender);
        et_nickname = (EditText) this.findViewById(R.id.et_nickname);
        tv_title = (TextView) this.findViewById(R.id.tv_title);
        tv_left = (TextView) this.findViewById(R.id.tv_left);
        tv_right = (TextView) this.findViewById(R.id.tv_right);
        btn_add = (Button) this.findViewById(R.id.btn_add);
        et_tag = (EditText) this.findViewById(R.id.et_tag);
        fl_tags = (FlowLayout) this.findViewById(R.id.fl_tags);
        tv_right.setText("完成");
        tv_title.setText("编辑个人资料");
        tv_left.setOnClickListener(this);
        tv_right.setOnClickListener(this);
        btn_add.setOnClickListener(this);
        rl_changephoto.setOnClickListener(this);
    }

    private void clearEditText() {
        et_wantgo.setText("");
        et_job.setText("");
        et_address.setText("");
        et_emotion.setText("");
        et_age.setText("");
        et_gender.setText("");
        et_nickname.setText("");
        et_tag.setText("");
    }

    private void initData() {
        File myDir = new File(Environment
                .getExternalStorageDirectory().getAbsolutePath() + "/miaotu");
        myDir.mkdirs();
        userinfo = new ModifyPersonInfo();
        alltags = new ArrayList<String>();
        String headimg = readPreference("headphoto");
        UrlImageViewHelper.setUrlDrawable(iv_head_photo, headimg, R.drawable.icon_default_head_photo);
    }

    private int position;

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_left:
                finish();
                break;
            case R.id.btn_add:
                String content = et_tag.getText().toString().trim();
                if (!StringUtil.isBlank(content)) {
                    final View tagview = LayoutInflater.from(EditUserInfoActivity.this).inflate(
                            R.layout.item_tag, null);
                    TextView tv_tag = (TextView) tagview.findViewById(R.id.tv_tag);
                    final ImageView iv_del = (ImageView) tagview.findViewById(R.id.iv_tag);
                    tv_tag.setText(content);
                    tagview.setTag(position);
                    alltags.add(content);
                    tagview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            fl_tags.removeView(tagview);
                            fl_tags.requestLayout();
                            int pos = (int) tagview.getTag();
                            if (pos < alltags.size()) {
                                alltags.remove(pos);
                            }
                        }
                    });
                    FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                            FlowLayout.LayoutParams.WRAP_CONTENT);
                    params.bottomMargin = Util.dip2px(EditUserInfoActivity.this, 10);
                    params.rightMargin = Util.dip2px(EditUserInfoActivity.this, 10);
                    tagview.setLayoutParams(params);
                    fl_tags.addView(tagview);
                    fl_tags.requestLayout();
                    et_tag.setText("");
                    position++;
                }
                break;
            case R.id.tv_right:
                String token = readPreference("token");
                userinfo.setToken(token);
                userinfo.setNickname(et_nickname.getText().toString().trim());
                userinfo.setGender(et_gender.getText().toString().trim());
                userinfo.setAge(et_age.getText().toString().trim());
                userinfo.setAddress(et_address.getText().toString().trim());
                userinfo.setMarital_status(et_emotion.getText().toString().trim());
                userinfo.setWork(et_job.getText().toString().trim());
                userinfo.setWant_go(et_wantgo.getText().toString().trim());
                userinfo.setHear_url("");
                LogUtil.e("修改头像", "路径// " + photourl);
                String contenttag = "";
                for (String tag : alltags) {
                    contenttag += tag + ",";
                }
                if (!StringUtil.isBlank(contenttag)) {
                    userinfo.setTags(contenttag.substring(0, contenttag.length() - 1));
                }
                modifyUserInfo(userinfo);
                clearEditText();
                break;
            case R.id.rl_changephoto:
                chosePhoto(2);
                break;
            default:
                break;
        }
    }

    /**
     * 修改用户信息
     */
    private void modifyUserInfo(final ModifyPersonInfo info) {
        new BaseHttpAsyncTask<Void, Void, BaseResult>(this, true) {

            @Override
            protected void onCompleteTask(BaseResult baseResult) {
                if (baseResult.getCode() == BaseResult.SUCCESS) {
                    showToastMsg("修改成功");
                    if (!StringUtil.isBlank(info.getHear_url())) {
                        writePreference("headphoto", info.getHear_url());
                    }
                    if (!StringUtil.isBlank(info.getWork())) {
                        writePreference("job", info.getWork());
                    }
                    if (!StringUtil.isBlank(info.getWork())) {
                        writePreference("age", info.getAge());
                    }
                    if (!StringUtil.isBlank(info.getWork())) {
                        writePreference("name", info.getNickname());
                    }
                    if (!StringUtil.isBlank(info.getWork())) {
                        writePreference("gender", info.getGender());
                    }
//                    UrlImageViewHelper.setUrlDrawable(iv_head_photo, info.getHear_url(),
//                            R.drawable.icon_default_head_photo);
                } else {
                    if (StringUtil.isBlank(baseResult.getMsg())) {
                        showToastMsg("修改信息失败");
                    } else {
                        showToastMsg(baseResult.getMsg());
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                return HttpRequestUtil.getInstance().modifyPersonInfo(info);
            }
        }.execute();
    }

    public void chosePhoto(int index) {
        File fos = null;
        try {
            fos = new File(IMAGE_FILE_LOCATION);
            fos.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(fos);
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        if (index == 1) {
            startActivityForResult(intent, 3); // 如果requestCode=3，是修改头像
        } else if (index == 2) {
            startActivityForResult(intent, 22); // requestCode=22,是相册添加照片
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 22:
                    if (imageUri != null) {
                        File file = new File(imageUri.getPath());
                        List<File> imgs = new ArrayList<File>();
                        imgs.add(file);
                        addPhoto(imgs);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 向服务器添加照片
     *
     * @param imgs
     */
    private void addPhoto(final List<File> imgs) {
        new BaseHttpAsyncTask<Void, Void, PhotoUploadResult>(this) {

            @Override
            protected void onCompleteTask(final PhotoUploadResult result) {
                if (result.getCode() == BaseResult.SUCCESS) {
                    photourl = result.getPhotoList().get(0);
                    UrlImageViewHelper.setUrlDrawable(iv_head_photo, photourl,
                            R.drawable.icon_default_head_photo);
                    ModifyPersonInfo info = new ModifyPersonInfo();
                    info.setHear_url(photourl);
                    modifyUserInfo(info);
                } else {
                    if (StringUtil.isBlank(result.getMsg())) {
                        showToastMsg("操作失败");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected PhotoUploadResult run(Void... params) {
                return HttpRequestUtil.getInstance().uploadPhoto(imgs);
            }

        }.execute();

    }
}
