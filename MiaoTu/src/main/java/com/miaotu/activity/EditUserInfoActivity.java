package com.miaotu.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
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
import com.miaotu.adapter.DateArrayAdapter;
import com.miaotu.adapter.DateNumericAdapter;
import com.miaotu.adapter.ProvinceAdapter;
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
import com.miaotu.view.WheelTwoColumnDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

public class EditUserInfoActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_left, tv_title, tv_right;
    private Button btn_add;
    private EditText et_tag;
    private FlowLayout fl_tags;
    private ModifyPersonInfo userinfo;
    private EditText et_nickname, et_emotion, et_job, et_wantgo;
    private TextView tv_address, tv_age, tv_gender;
    private RelativeLayout rl_changephoto;
    private List<String> alltags;
    private CircleImageView iv_head_photo;
    private static final String IMAGE_FILE_LOCATION = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/miaotu/temp.jpg";
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);//
    private String photourl;
    private boolean scrolling = false;
    private WheelTwoColumnDialog dialog;

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
        tv_address = (TextView) this.findViewById(R.id.tv_address);
        et_emotion = (EditText) this.findViewById(R.id.et_emotion);
        tv_age = (TextView) this.findViewById(R.id.tv_age);
        tv_gender = (TextView) this.findViewById(R.id.tv_gender);
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
        tv_address.setOnClickListener(this);
        tv_age.setOnClickListener(this);
        tv_gender.setOnClickListener(this);
    }

    private void clearEditText() {
        et_wantgo.setText("");
        et_job.setText("");
        tv_address.setText("");
        et_emotion.setText("");
        tv_age.setText("");
        tv_gender.setText("");
        et_nickname.setText("");
        et_tag.setText("");
        fl_tags.removeAllViews();
    }

    /**
     * 判断edittext是否全为空
     *
     * @return
     */
    private boolean isEmpty() {
        boolean empty = false;
        if (StringUtil.isBlank(et_wantgo.getText().toString()) &&
                StringUtil.isBlank(et_job.getText().toString()) &&
                StringUtil.isBlank(tv_address.getText().toString()) &&
                StringUtil.isBlank(et_emotion.getText().toString()) &&
                StringUtil.isBlank(tv_age.getText().toString()) &&
                StringUtil.isBlank(tv_gender.getText().toString()) &&
                StringUtil.isBlank(et_nickname.getText().toString()) &&
                StringUtil.isBlank(et_tag.getText().toString()) &&
                fl_tags.getChildCount() < 1) {
            empty = true;
        }
        return empty;
    }

    private void initData() {
        File myDir = new File(Environment
                .getExternalStorageDirectory().getAbsolutePath() + "/miaotu");
        myDir.mkdirs();
        userinfo = new ModifyPersonInfo();
        alltags = new ArrayList<String>();
        String headimg = readPreference("headphoto");
        UrlImageViewHelper.setUrlDrawable(iv_head_photo, headimg, R.drawable.default_avatar);
        et_nickname.setText(readPreference("name"));
        tv_gender.setText(readPreference("gender"));
        tv_age.setText(readPreference("age")+"岁");
        tv_address.setText(readPreference("address"));
        et_emotion.setText(readPreference("emotion"));
        et_job.setText(readPreference("job"));
        et_wantgo.setText(readPreference("wantgo"));
        String tags = readPreference("tags");
        for (String content:tags.split(",")){
            addTag(content);
        }
    }

    private int position;

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_left:
                if (isEmpty()) {
                    finish();
                } else {
                    final Dialog dialog = new AlertDialog.Builder(EditUserInfoActivity.this).create();
                    dialog.setCancelable(true);
                    dialog.show();
                    View v = LayoutInflater.from(EditUserInfoActivity.this).inflate(
                            R.layout.dialog_message_empty, null);
                    v.setBackgroundResource(R.drawable.bg_dialog_choose_register);
                    dialog.setContentView(v);
                    Button btnCancle = (Button) v.findViewById(R.id.btn_cancel);
                    Button btnConfirm = (Button) v.findViewById(R.id.btn_confirm);
                    TextView tvContent = (TextView) v.findViewById(R.id.tv_content);
                    btnCancle.setText("否");
                    btnConfirm.setText("是");
                    tvContent.setText("是否保存已填信息？");
                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            EditUserInfoActivity.this.onClick(tv_right);
                            dialog.dismiss();
                        }
                    });
                    btnCancle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            dialog.dismiss();
                        }
                    });
                }
                break;
            case R.id.btn_add:
                String content = et_tag.getText().toString().trim();
                addTag(content);
                break;
            case R.id.tv_right:
                if (isEmpty()) {
                    return;
                }
                String token = readPreference("token");
                userinfo.setToken(token);
                userinfo.setNickname(et_nickname.getText().toString().trim());
                userinfo.setGender(tv_gender.getText().toString().trim());
                if (StringUtil.isBlank(tv_age.getText().toString().trim())){
                    userinfo.setAge("");
                }else {
                    userinfo.setAge(tv_age.getText().toString().trim().substring(0,
                            tv_age.getText().toString().length() - 1));
                }
                userinfo.setAddress(tv_address.getText().toString().trim());
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
            case R.id.tv_address:
                getCityDialog();
                break;
            case R.id.tv_age:
                getAgeDialog();
                break;
            case R.id.tv_gender:
                getGenderDialog();
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

                    if (!StringUtil.isBlank(info.getWork())) {
                        writePreference("job", info.getWork());
                    }
                    if (!StringUtil.isBlank(info.getAge())) {
                        writePreference("age", info.getAge());
                    }
                    if (!StringUtil.isBlank(info.getNickname())) {
                        writePreference("name", info.getNickname());
                    }
                    if (!StringUtil.isBlank(info.getGender())) {
                        writePreference("gender", info.getGender());
                    }
                    if (!StringUtil.isBlank(info.getAddress())) {
                        writePreference("address", info.getAddress());
                    }
                    if (!StringUtil.isBlank(info.getMarital_status())) {
                        writePreference("emotion", info.getMarital_status());
                    }
                    if (!StringUtil.isBlank(info.getWant_go())) {
                        writePreference("wantgo", info.getWant_go());
                    }
                    if (!StringUtil.isBlank(info.getTags())) {
                        writePreference("tags", info.getTags());
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
                            R.drawable.default_avatar);
                    ModifyPersonInfo info = new ModifyPersonInfo();
                    info.setToken(readPreference("token"));
                    info.setHear_url(photourl);
                    if (!StringUtil.isBlank(info.getHear_url())) {
                        writePreference("headphoto", info.getHear_url());
                    }
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

    // 获取城市dialog
    private void getCityDialog() {
        // 为dialog的listview赋值
        LayoutInflater lay = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = lay.inflate(R.layout.dialog_city_layout, null);
        final WheelView province = (WheelView) v.findViewById(R.id.province);
        province.setVisibleItems(5);
        province.setViewAdapter(new ProvinceAdapter(this));

        final String cities[][] = new String[][]{
                new String[]{"安庆", "蚌埠", "巢湖", "池州", "滁州", "阜阳", "合肥", "淮北",
                        "淮南", "黄山", "六安", "马鞍山", "宿州", "铜陵", "芜湖", "宣城", "亳州"},
                new String[]{"北京市", "密云县", "延庆县"},
                new String[]{"福州", "龙岩", "南平", "宁德", "莆田", "泉州", "三明", "厦门",
                        "漳州"},
                new String[]{"白银", "定西", "甘南藏族自治州", "嘉峪关", "金昌", "酒泉", "兰州",
                        "临夏回族自治州", "陇南", "平凉", "庆阳", "天水", "武威", "张掖"},
                new String[]{"潮州", "东莞", "佛山", "广州", "河源", "惠州", "江门", "揭阳",
                        "茂名", "梅州", "清远", "汕头", "汕尾", "韶关", "深圳", "阳江", "云浮",
                        "湛江", "肇庆", "中山", "珠海"},
                new String[]{"百色", "北海", "崇左", "防城港", "桂林", "贵港", "河池", "贺州",
                        "来宾", "柳州", "南宁", "钦州", "梧州", "玉林"},
                new String[]{"安顺", "毕节", "贵阳", "六盘水", "黔东南苗族侗族自治州",
                        "黔南布依族苗族自治州", "黔西南布依族苗族自治州", "铜仁", "遵义"},
                new String[]{"白沙黎族自治县", "保亭黎族苗族自治县", "昌江黎族自治县", "澄迈县", "定安县",
                        "东方", "海口", "乐东黎族自治县", "临高县", "陵水黎族自治县", "琼海",
                        "琼中黎族苗族自治县", "三亚", "屯昌县", "万宁", "文昌", "五指山", "儋州"},
                new String[]{"保定", "沧州", "承德", "邯郸", "衡水", "廊坊", "秦皇岛",
                        "石家庄", "唐山", "邢台", "张家口"},
                new String[]{"安阳", "鹤壁", "济源", "焦作", "开封", "洛阳", "南阳", "平顶山",
                        "三门峡", "商丘", "新乡", "信阳", "许昌", "郑州", "周口", "驻马店", "漯河",
                        "濮阳"},
                new String[]{"大庆", "大兴安岭", "哈尔滨", "鹤岗", "黑河", "鸡西", "佳木斯",
                        "牡丹江", "七台河", "齐齐哈尔", "双鸭山", "绥化", "伊春"},
                new String[]{"鄂州", "恩施土家族苗族自治州", "黄冈", "黄石", "荆门", "荆州",
                        "潜江", "神农架林区", "十堰", "随州", "天门", "武汉", "仙桃", "咸宁",
                        "襄樊", "孝感", "宜昌"},
                new String[]{"常德", "长沙", "郴州", "衡阳", "怀化", "娄底", "邵阳", "湘潭",
                        "湘西土家族苗族自治州", "益阳", "永州", "岳阳", "张家界", "株洲"},
                new String[]{"白城", "白山", "长春", "吉林", "辽源", "四平", "松原", "通化",
                        "延边朝鲜族自治州"},
                new String[]{"常州", "淮安", "连云港", "南京", "南通", "苏州", "宿迁", "泰州",
                        "无锡", "徐州", "盐城", "扬州", "镇江"},
                new String[]{"抚州", "赣州", "吉安", "景德镇", "九江", "南昌", "萍乡", "上饶",
                        "新余", "宜春", "鹰潭"},
                new String[]{"鞍山", "本溪", "朝阳", "大连", "丹东", "抚顺", "阜新", "葫芦岛",
                        "锦州", "辽阳", "盘锦", "沈阳", "铁岭", "营口"},
                new String[]{"阿拉善盟", "巴彦淖尔盟", "包头", "赤峰", "鄂尔多斯", "呼和浩特",
                        "呼伦贝尔", "通辽", "乌海", "乌兰察布盟", "锡林郭勒盟", "兴安盟"},
                new String[]{"固原", "石嘴山", "吴忠", "银川"},
                new String[]{"果洛藏族自治州", "海北藏族自治州", "海东", "海南藏族自治州",
                        "海西蒙古族藏族自治州", "黄南藏族自治州", "西宁", "玉树藏族自治州"},
                new String[]{"滨州", "德州", "东营", "菏泽", "济南", "济宁", "莱芜", "聊城",
                        "临沂", "青岛", "日照", "泰安", "威海", "潍坊", "烟台", "枣庄", "淄博"},
                new String[]{"长治", "大同", "晋城", "晋中", "临汾", "吕梁", "朔州", "太原",
                        "忻州", "阳泉", "运城"},
                new String[]{"安康", "宝鸡", "汉中", "商洛", "铜川", "渭南", "西安", "咸阳",
                        "延安", "榆林"},
                new String[]{"崇明县", "上海市"},
                new String[]{"阿坝藏族羌族自治州", "巴中", "成都", "达州", "德阳", "甘孜藏族自治州",
                        "广安", "广元", "乐山", "凉山彝族自 治州", "眉山", "绵阳", "南充", "内江",
                        "攀枝花", "遂宁", "雅安", "宜宾", "资阳", "自贡", "泸州"},
                new String[]{"蓟县", "静海县", "宁河县", "天津市"},
                new String[]{"阿里", "昌都", "拉萨", "林芝", "那曲", "日喀则", "山南"},
                new String[]{"阿克苏", "阿拉尔", "巴音郭楞蒙古自治州", "博尔塔拉蒙古自治州",
                        "昌吉回族自治州", "哈密", "和田", "喀什", "克 拉玛依", "克孜勒苏柯尔克孜自治州",
                        "石河子", "图木舒克", "吐鲁番", "乌鲁木齐", "五家渠", "伊犁哈萨克自治州"},
                new String[]{" 保山", "楚雄彝族自治州", "大理白族自治州", "德宏傣族景颇族自治州",
                        "迪庆藏族自治州", "红河哈尼族彝族自治州", "昆明", "丽江", "临 沧", "怒江傈傈族自治州",
                        "曲靖", "思茅", "文山壮族苗族自治州", "西双版纳傣族自治州", "玉溪", "昭通"},
                new String[]{"杭州", "湖州", "嘉兴", "金华", "丽水", "宁波", "绍兴", "台州",
                        "温州", "舟山", "衢州"},
                new String[]{"重庆市", "沙坪坝", "渝中", "江北", "南岸", "九龙坡", "大渡口",
                        "渝北", "北碚", "万盛", "双桥", "巴南", "万州", "涪陵", "黔江", "长寿",
                        "城口县", "大足县", "垫江县", "丰都县", "奉节县", "合川市", "江津市", "开县",
                        "梁平县", "南川市", "彭 水苗族土家族自治县", "荣昌县", "石柱土家族自治县", "铜梁县",
                        "巫山县", "巫溪县", "武隆县", "秀山土家族苗族自治县", "永川市",
                        " 酉阳土家族苗族自治县", "云阳县", "忠县", "潼南县", "璧山县", "綦江县"},};

        final WheelView city = (WheelView) v.findViewById(R.id.city);
        city.setVisibleItems(5);

        province.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (!scrolling) {
                    updateCities(city, cities, newValue);
                }
            }
        });

        province.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
                scrolling = true;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                scrolling = false;
                updateCities(city, cities, province.getCurrentItem());
            }
        });

        province.setCurrentItem(29);
        // 创建dialog
        dialog = new WheelTwoColumnDialog(this, R.style.Dialog_Fullscreen, v);
        dialog.setOnConfirmListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String curCity = cities[province.getCurrentItem()][city
                        .getCurrentItem()]; // 获取当前选中的城市
                tv_address.setText(curCity);
//                myUserInfo.setCity(curCity); // 重新修改所在城市
                // updateUserInfo(myUserInfo); // 更新用户信息
                dialog.dismiss();
            }
        });
    }

    /**
     * Updates the city wheel
     */
    private void updateCities(WheelView city, String cities[][], int index) {
        ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
                cities[index]);
        adapter.setTextSize(18);
        city.setViewAdapter(adapter);
        city.setCurrentItem(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.cancel();
            dialog = null;
        }
    }

    // 获取性别dialog
    private void getGenderDialog() {
        // 为dialog的listview赋值
        LayoutInflater lay = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = lay.inflate(R.layout.dialog_age_layout, null);
        final WheelView wvDay = (WheelView) v.findViewById(R.id.wv_day);

        final String months[] = new String[]{"男", "女"};
        wvDay.setViewAdapter(new DateArrayAdapter(this, months, 0));
        // 创建dialog
        dialog = new WheelTwoColumnDialog(this, R.style.Dialog_Fullscreen, v);
        dialog.setOnConfirmListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                int monthIndex = wvMonth.getCurrentItem();
                int dayIndex = wvDay.getCurrentItem();
                String gender = "男";
                if (dayIndex == 1) {
                    gender = "女";
                }
                tv_gender.setText(gender);

                dialog.dismiss();
            }
        });
    }

    // 获取生日dialog
    private void getAgeDialog() {
        // 为dialog的listview赋值
        LayoutInflater lay = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = lay.inflate(R.layout.dialog_age_layout, null);
        final WheelView wvDay = (WheelView) v.findViewById(R.id.wv_day);

        final String months[] = new String[100];
        for (int i = 1; i <= 100; i++) {
            months[i - 1] = i + "";
        }
        wvDay.setViewAdapter(new DateArrayAdapter(this, months, 17));
        // 创建dialog
        dialog = new WheelTwoColumnDialog(this, R.style.Dialog_Fullscreen, v);
        dialog.setOnConfirmListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                int monthIndex = wvMonth.getCurrentItem();
                int dayIndex = wvDay.getCurrentItem();
                String currentDay = (dayIndex + 1) + ""; // 获取年龄
//                if (currentDay.length() == 1) {
//                    currentDay = "0" + currentDay;
//                }
                tv_age.setText(currentDay + "岁");

                dialog.dismiss();
            }
        });
    }

    /**
     * 添加标签
     */
    private void addTag(String content){
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
    }
}
