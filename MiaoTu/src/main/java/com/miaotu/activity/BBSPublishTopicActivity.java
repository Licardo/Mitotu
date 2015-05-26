package com.miaotu.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miaotu.R;
import com.miaotu.async.BaseHttpAsyncTask;
import com.miaotu.http.HttpRequestUtil;
import com.miaotu.model.Movement;
import com.miaotu.result.BaseResult;
import com.miaotu.result.MovementListResult;
import com.miaotu.result.PhotoUploadResult;
import com.miaotu.util.LogUtil;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;
import com.miaotu.view.ChoseTopicMovementPopupWindow;
import com.miaotu.view.DraggableGridView;
import com.miaotu.view.OnRearrangeListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.photoselector.model.PhotoModel;
import com.photoselector.ui.PhotoPreviewActivity;
import com.photoselector.ui.PhotoSelectorActivity;
import com.photoselector.util.CommonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ying on 2015/3/6.
 */
public class BBSPublishTopicActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private TextView tvTitle, tvMovementName;
    private TextView tvLeft, tvRight;
    private EditText etTitle, etContent;
    //    private Topic topic;
    DraggableGridView dgv;
    private ArrayList<PhotoModel> photoList;
    private List<Movement> movementList;
    private RelativeLayout layoutMovement;
    private List<File> files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bbs_publish_topic);
        findView();
        bindView();
        init();
    }

    private void findView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvLeft.setVisibility(View.VISIBLE);
        tvRight.setVisibility(View.VISIBLE);
        tvLeft.setBackgroundResource(R.drawable.arrow_left_grey);
        tvRight.setText("发布");
        ViewGroup.LayoutParams params = tvRight.getLayoutParams();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        tvRight.setLayoutParams(params);
        etContent = (EditText) findViewById(R.id.et_content);
        etTitle = (EditText) findViewById(R.id.et_title);
        tvMovementName = (TextView) findViewById(R.id.tv_movement_name);
        dgv = ((DraggableGridView) findViewById(R.id.dgv));
        layoutMovement = (RelativeLayout) findViewById(R.id.layout_movement);
    }

    private void bindView() {
        dgv.setOnRearrangeListener(new OnRearrangeListener() {
            public void onRearrange(int oldIndex, int newIndex) {
                photoList.add(newIndex, photoList.remove(oldIndex));
//                String word = poem.remove(oldIndex);
//                if (oldIndex < newIndex)
//                    poem.add(newIndex, word);
//                else
//                    poem.add(newIndex, word);
            }
        });
        dgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg2 == dgv.getChildCount() - 1) {
//                    showToastMsg("添加照片");
                    //添加照片
                    Intent intent = new Intent(BBSPublishTopicActivity.this, PhotoSelectorActivity.class);
                    intent.putExtra("key_max", 9 - photoList.size());
                    startActivityForResult(intent, 1);
                } else {
                    //点击照片
                    /** 预览照片 */
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("photos", photoList);
                    bundle.putSerializable("position", arg2);
                    CommonUtils.launchActivity(BBSPublishTopicActivity.this, PhotoPreviewActivity.class, bundle);
                }
//                dgv.removeViewAt(arg2);
//                poem.remove(arg2);
            }
        });
        layoutMovement.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvLeft.setOnClickListener(this);
        etContent.setOnFocusChangeListener(this);
        etTitle.setOnFocusChangeListener(this);
    }

    private void init() {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText("发表话题");

        ImageView imageView = new ImageView(this);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(Util.dip2px(this, 70), Util.dip2px(this, 70));
        imageView.setLayoutParams(params);
        imageView.setBackgroundResource(R.drawable.icon_pic_add);

        dgv.addView(imageView);
        photoList = new ArrayList<>();
        movementList = new ArrayList<>();
        files = new ArrayList<>();
        getJoined();
    }

    public static DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(com.photoselector.R.drawable.ic_picture_loading)
            .showImageOnFail(com.photoselector.R.drawable.ic_picture_loadfailed)
            .cacheInMemory(false).cacheOnDisk(true).considerExifParams(true)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY).build();

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_movement:
                //选择活动
                ChoseTopicMovementPopupWindow popWindow = new ChoseTopicMovementPopupWindow(
                        BBSPublishTopicActivity.this, view.getWidth(), view, movementList);
                popWindow.showPopupWindow();
                break;
            case R.id.tv_right:
                if (validate()) {
//                    publishTopic();
                    getPhotoUrl();
                }
                break;
            case R.id.tv_left:
                finish();
                break;
        }
    }

    private boolean validate() {
        if (StringUtil.isBlank(etTitle.getText().toString())) {
            showToastMsg("请输入标题");
            return false;
        }
        if (StringUtil.isBlank(etContent.getText().toString())) {
            showToastMsg("请输入内容");
            return false;
        }
        return true;
    }

    private void publishTopic(final List<String> imgs) {

        //发表话题
        /*if (!readPreference("login_state").equals("in")) {
            Intent intent = new Intent(BBSPublishTopicActivity.this, LoginActivity.class);
            startActivity(intent);
            return;
        } else {
            files.clear();
            for (PhotoModel photo : photoList) {
                File file = new File(photo.getOriginalPath());
                files.add(file);
            }
            LogUtil.d("图片个数：" + photoList.size());
            boolean gifFlg = false;
            for (File file : files) {
                String ext = Util.getExtName(file);
                LogUtil.d("后缀名" + ext);
                if (ext.equals("gif") || ext.equals("GIF")) {
                    showToastMsg("暂时不支持上传gif动图(⊙o⊙)");
                    gifFlg = true;
                    break;
                }
            }
            if (gifFlg) {
                return;
            }*/
        new BaseHttpAsyncTask<Void, Void, BaseResult>(BBSPublishTopicActivity.this, true) {
            @Override
            protected void onCompleteTask(BaseResult result) {
                if (movementList == null) {
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    showToastMsg("发表成功！");
                    setResult(1);
                    finish();
                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                        showToastMsg("发表话题失败！");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected BaseResult run(Void... params) {
                String images = "";
                for (String img:imgs){
                    images += img+",";
                }
                return HttpRequestUtil.getInstance().publishTopic((String) layoutMovement.getTag(), readPreference("token"),
                        etContent.getText().toString(), images.substring(0, images.length() - 1));
            }

//                @Override
//                protected void onError() {
//                    // TODO Auto-generated method stub
//
//                }
        }.execute();
//        }
    }

    private void getJoined() {
        new BaseHttpAsyncTask<Void, Void, MovementListResult>(this, true) {
            @Override
            protected void onCompleteTask(MovementListResult result) {
                if (movementList == null) {
                    return;
                }
                if (result.getCode() == BaseResult.SUCCESS) {
                    if (result.getResults().size() == 0) {
                        layoutMovement.setVisibility(View.GONE);
                    } else {
                        layoutMovement.setVisibility(View.VISIBLE);
                        movementList.addAll(result.getResults());
                        Movement frakeMovement = new Movement();
                        frakeMovement.setName("不选择");
                        movementList.add(frakeMovement);
                    }
                } else {
                    if (StringUtil.isEmpty(result.getMsg())) {
                        showToastMsg("获取用户参加活动列表失败！");
                    } else {
                        showToastMsg(result.getMsg());
                    }
                }
            }

            @Override
            protected MovementListResult run(Void... params) {
                return HttpRequestUtil.getInstance().getUserJoin(readPreference("id"), "");
            }
//        @Override
//        protected void onError() {
//            // TODO Auto-generated method stub
//
//        }
        }.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //选择照片返回
            List<PhotoModel> selected = (ArrayList<PhotoModel>) data.getSerializableExtra("photos");
            for (int i = 0; i < selected.size(); i++) {
                for (int j = 0; j < photoList.size(); j++) {
                    if (selected.get(i).getOriginalPath().equals(photoList.get(j).getOriginalPath())) {
                        //已经添加过的去除掉
                        selected.remove(i);
                        break;
                    }
                }
            }
            photoList.addAll(selected);
            dgv.removeAllViews();
            for (PhotoModel photo : photoList) {
                ImageView imageView = new ImageView(this);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(params);
                ImageLoader.getInstance().displayImage(
                        "file://" + photo.getOriginalPath(), imageView, imageOptions);
                LogUtil.d("图片路径：" + photo.getOriginalPath());
                ImageView ivDel = new ImageView(this);
                RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(Util.dip2px(this, 25), Util.dip2px(this, 25));
                params1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                ivDel.setLayoutParams(params1);
                ivDel.setTag(photo);
                ivDel.setBackgroundResource(R.drawable.icon_pic_del);
                ivDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dgv.removeView((View) view.getParent());
                        photoList.remove(view.getTag());
                        if (photoList.size() == 8) {
//                            dgv.getChildAt(dgv.getChildCount()-1).setVisibility(View.VISIBLE);
                            //添加图片的item
                            ImageView imageView = new ImageView(BBSPublishTopicActivity.this);
                            AbsListView.LayoutParams params = new AbsListView.LayoutParams(Util.dip2px(BBSPublishTopicActivity.this, 70), Util.dip2px(BBSPublishTopicActivity.this, 70));
                            imageView.setLayoutParams(params);
                            imageView.setBackgroundResource(R.drawable.icon_pic_add);
                            dgv.addView(imageView);
                        }
                    }
                });

                RelativeLayout relativeLayout = new RelativeLayout(this);
                relativeLayout.addView(imageView);
                relativeLayout.addView(ivDel);

                dgv.addView(relativeLayout);
            }
            //添加图片的item
            ImageView imageView = new ImageView(this);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(Util.dip2px(this, 70), Util.dip2px(this, 70));
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(R.drawable.icon_pic_add);
            if (photoList.size() > 8) {
//                dgv.getChildAt(dgv.getChildCount()-1).setVisibility(View.GONE);
            } else {
                dgv.addView(imageView);
//                dgv.getChildAt(dgv.getChildCount()-1).setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (!hasFocus) {
            System.out.println("失去焦点");
            // 失去焦点
            view.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    private void getPhotoUrl() {
        if (!readPreference("login_state").equals("in")) {
            Intent intent = new Intent(BBSPublishTopicActivity.this, LoginActivity.class);
            startActivity(intent);
            return;
        } else {
            files.clear();
            for (PhotoModel photo : photoList) {
                File file = new File(photo.getOriginalPath());
                files.add(file);
            }
            LogUtil.d("图片个数：" + photoList.size());
            boolean gifFlg = false;
            for (File file : files) {
                String ext = Util.getExtName(file);
                LogUtil.d("后缀名" + ext);
                if (ext.equals("gif") || ext.equals("GIF")) {
                    showToastMsg("暂时不支持上传gif动图(⊙o⊙)");
                    gifFlg = true;
                    break;
                }
            }
            if (gifFlg) {
                return;
            }
            new BaseHttpAsyncTask<Void, Void, PhotoUploadResult>(this, false) {

                @Override
                protected void onCompleteTask(PhotoUploadResult result) {
                    if (result.getCode() == BaseResult.SUCCESS) {
//                        showToastMsg("上传照片成功");
                        publishTopic(result.getPhotoList());
                    } else {
                        if (StringUtil.isBlank(result.getMsg())) {
                            showToastMsg("上传照片失败");
                        } else {
                            showToastMsg(result.getMsg());
                        }
                    }
                }

                @Override
                protected PhotoUploadResult run(Void... params) {
                    return HttpRequestUtil.getInstance().uploadPhoto(files);
                }
            }.execute();
        }
    }
}
