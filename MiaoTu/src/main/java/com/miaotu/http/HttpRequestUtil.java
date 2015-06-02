package com.miaotu.http;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;

import com.miaotu.annotation.FormProperty;
import com.miaotu.annotation.Ignore;
import com.miaotu.form.MFriendsInfo;
import com.miaotu.model.ModifyPersonInfo;
import com.miaotu.result.BlackResult;
import com.miaotu.result.CustomTourResult;
import com.miaotu.result.MyTogetherResult;
import com.miaotu.result.LikeResult;
import com.miaotu.result.LuckyResult;
import com.miaotu.result.MessageResult;
import com.miaotu.result.MoneyResult;
import com.miaotu.result.MovementListResult;
import com.miaotu.result.MyCustomTourResult;
import com.miaotu.result.PersonInfoResult;
import com.miaotu.form.PublishTogether;
import com.miaotu.model.RegisterInfo;
import com.miaotu.result.BaseResult;
import com.miaotu.result.LoginResult;
import com.miaotu.result.PhotoUploadResult;
import com.miaotu.result.RedPackageListResult;
import com.miaotu.result.SearchTourResult;
import com.miaotu.result.SearchUserResult;
import com.miaotu.result.SymbolResult;
import com.miaotu.result.TogetherDetailResult;
import com.miaotu.result.TogetherResult;
import com.miaotu.result.TopicCommentsListResult;
import com.miaotu.result.TopicListResult;
import com.miaotu.result.TopicMessageListResult;
import com.miaotu.result.TopicResult;
import com.miaotu.util.LogUtil;
import com.miaotu.util.StringUtil;
import com.miaotu.util.Util;

@SuppressLint("SimpleDateFormat")
public class HttpRequestUtil {
    //    正式环境
//	private static final String SYM_HOST = "http://112.124.11.134/";
//	private static final String IMG_SYM_HOST = "http://112.124.11.134/";
    //测试环境
    private static final String SYM_HOST = "http://121.41.105.30:8011/v1/";
    private static final String IMG_SYM_HOST = "http://img1.miaotu.com/";

    public static String getServer() {
        return SYM_HOST;
    }

    public static String getImgServer() {
        return IMG_SYM_HOST;
    }

    public static final int CONNECTION_TIME_OUT = 20000;

    // private static HttpRequestUtil instance = new HttpRequestUtil();

    // private RestTemplate restTemplate;

    @SuppressWarnings("deprecation")
    private HttpRequestUtil() {
        // Work around pre-Froyo bugs in HTTP connection reuse.
        if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");

        }
    }

    public static HttpRequestUtil getInstance() {
        return new HttpRequestUtil();
        // return instance;
    }

    /**
     * 登陆
     *
     * @param registerInfo
     * @return
     */
    public LoginResult login(RegisterInfo registerInfo) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (!StringUtil.isEmpty(registerInfo.getPhone())) {
            params.add(new BasicNameValuePair("phone", registerInfo.getPhone()));
        }
        if (!StringUtil.isEmpty(registerInfo.getPassword())) {
            params.add(new BasicNameValuePair("password", registerInfo.getPassword()));
        }
        if (!StringUtil.isEmpty(registerInfo.getUsid())) {
            params.add(new BasicNameValuePair("usid", registerInfo.getUsid()));
        }
        if (!StringUtil.isEmpty(registerInfo.getUnionid())) {
            params.add(new BasicNameValuePair("unionid", registerInfo.getUnionid()));
        }
        if (!StringUtil.isEmpty(registerInfo.getOpenid())) {
            params.add(new BasicNameValuePair("openid", registerInfo.getOpenid()));
        }
        return HttpDecoder.getForObject(
                getUrl("base/user"), LoginResult.class,
                params);

    }

    /**
     * 注册
     *
     * @param registerInfo
     * @return
     */
    public BaseResult register(RegisterInfo registerInfo) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        if (!StringUtil.isEmpty(registerInfo.getPhone())) {
            params.add(new BasicNameValuePair("phone", registerInfo.getPhone()));
        }
        if (!StringUtil.isEmpty(registerInfo.getPassword())) {
            params.add(new BasicNameValuePair("password", registerInfo.getPassword()));
        }
        if (!StringUtil.isEmpty(registerInfo.getUsid())) {
            params.add(new BasicNameValuePair("usid", registerInfo.getUsid()));
        }
        if (!StringUtil.isEmpty(registerInfo.getUnionid())) {
            params.add(new BasicNameValuePair("unionid", registerInfo.getUnionid()));
        }
        if (!StringUtil.isEmpty(registerInfo.getOpenid())) {
            params.add(new BasicNameValuePair("openid", registerInfo.getOpenid()));
        }
        if (!StringUtil.isEmpty(registerInfo.getCode())) {
            params.add(new BasicNameValuePair("code", registerInfo.getCode()));
        }
        if (!StringUtil.isEmpty(registerInfo.getGender())) {
            params.add(new BasicNameValuePair("gender", registerInfo.getGender()));
        }
        if (!StringUtil.isEmpty(registerInfo.getNickname())) {
            params.add(new BasicNameValuePair("nickname", registerInfo.getNickname()));
        }
        if (!StringUtil.isEmpty(registerInfo.getProvince())) {
            params.add(new BasicNameValuePair("province", registerInfo.getProvince()));
        }
        if (!StringUtil.isEmpty(registerInfo.getCity())) {
            params.add(new BasicNameValuePair("city", registerInfo.getCity()));
        }
        if (!StringUtil.isEmpty(registerInfo.getBirthday())) {
            params.add(new BasicNameValuePair("birthday", registerInfo.getBirthday()));
        }
        if (!StringUtil.isEmpty(registerInfo.getAge())) {
            params.add(new BasicNameValuePair("age", registerInfo.getAge()));
        }
        if (!StringUtil.isEmpty(registerInfo.getHeadurl())) {
            params.add(new BasicNameValuePair("headurl", registerInfo.getHeadurl()));
        }
        return HttpDecoder.postForObject(
                getUrl("base/user"), BaseResult.class,
                params);

    }

    /**
     * 获取验证码
     *
     * @param tel
     * @return
     */
    public BaseResult getSms(String tel) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("phone", tel));
        return HttpDecoder.getForObject(
                getUrl("sms/register"), BaseResult.class,
                params);

    }

    /**
     * 喜欢约游
     *
     * @param token
     * @param yid
     * @return
     */
    public BaseResult likeTogether(String token, String yid) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("yid", yid));
        return HttpDecoder.postForObject(
                getUrl("yueyou/like"), BaseResult.class,
                params);

    }

    /**
     * 对约游进行评论
     *
     * @param token
     * @param yid
     * @param content
     * @return
     */
    public BaseResult publishTogetherComment(String token, String yid, String content) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("yid", yid));
        params.add(new BasicNameValuePair("content", content));
        return HttpDecoder.postForObject(
                getUrl("yueyou/reply"), BaseResult.class,
                params);

    }

    /**
     * 获取约游列表
     *
     * @param token
     * @param page
     * @param num
     * @return
     */
    public TogetherResult getTogetherList(String token, String page, String num, String latitude, String longitude) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("page", page));
        params.add(new BasicNameValuePair("num", num));
        params.add(new BasicNameValuePair("latitude", latitude));
        params.add(new BasicNameValuePair("longitude", longitude));
        return HttpDecoder.getForObject(
                getUrl("yueyou/list"), TogetherResult.class,
                params);

    }

    /**
     * 搜索线路
     *
     * @param token
     * @param key
     * @param latitude
     * @param longitude
     * @return
     */
    public SearchTourResult searchTour(String token, String key, String latitude, String longitude) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("keywords", key));
        params.add(new BasicNameValuePair("latitude", latitude));
        params.add(new BasicNameValuePair("longitude", longitude));
        return HttpDecoder.getForObject(
                getUrl("base/search"), SearchTourResult.class,
                params);

    }


	/**
	 * 搜索用户
	 * @param token
	 * @param key
	 * @param page
	 * @param num
	 * @return
	 */
	public SearchUserResult searchUser (String token,String key,String page,String num){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("keywords", key));
        params.add(new BasicNameValuePair("page", page));
        params.add(new BasicNameValuePair("num", num));
        return HttpDecoder.getForObject(
                getUrl("users/search"), SearchUserResult.class,
                params);
    }

    /**
     * 获取定制游列表
     *
     * @param token
     * @param page
     * @param num
     * @return
     */
    public CustomTourResult getCustomTourList(String token, String page, String num) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("page", page));
        params.add(new BasicNameValuePair("num", num));
        return HttpDecoder.getForObject(
                getUrl("activity/list"), CustomTourResult.class,
                params);

    }

    /**
     * 发布一起去
     *
     * @param together
     * @return
     */
    public BaseResult publishTogether(PublishTogether together) {
        return HttpDecoder.postForObject(
                getUrl("yueyou/"), BaseResult.class,
                transformObject2Map(together));

    }

    /**
     * 获取一起去详情
     *
     * @param token
     * @param id
     * @return
     */
    public TogetherDetailResult getTogetherDetail(String token, String id) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("yid", id));
        return HttpDecoder.getForObject(
                getUrl("yueyou/"), TogetherDetailResult.class, params);

    }

    /**
     * 参加约游
     *
     * @param token
     * @param id
     * @param name
     * @param phone
     * @return
     */
    public BaseResult joinTogether(String token, String id, String name, String phone) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("yid", id));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("phone", phone));
        return HttpDecoder.postForObject(
                getUrl("yueyou/join"), BaseResult.class, params);

    }

    /**
     * 上传照片
     * //	 * @param token
     *
     * @param img
     * @return
     */
    public PhotoUploadResult uploadPhoto(List<File> img) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        return HttpDecoder.postForObject(
                getImgUrl("upload/picture"), PhotoUploadResult.class,
                params, img);

    }

    /**
     * 获取实际接口地址
     *
     * @param url
     * @return
     */
    private static String getUrl(String url) {
        return SYM_HOST + url;
    }

    /**
     * 获取图片接口地址
     *
     * @param url
     * @return
     */
    private static String getImgUrl(String url) {
        return IMG_SYM_HOST + url;
    }

    /**
     * 将对象属性值转换为MultiValueMap
     *
     * @param o
     * @return
     */
    public List<NameValuePair> transformObject2Map(Object o) {
        return transformObject2Map(o, o.getClass());
    }

    private List<NameValuePair> transformObject2Map(Object o, Class<?> cl) {
        List<NameValuePair> data = new ArrayList<NameValuePair>();
        for (Class<?> superCl = cl.getSuperclass(); superCl != null
                && !superCl.equals(Object.class); superCl = superCl
                .getSuperclass()) {
            data.addAll(transformObject2Map(o, superCl));
        }
        for (Field f : cl.getDeclaredFields()) {
            FormProperty fp = f.getAnnotation(FormProperty.class);
            Ignore ig = f.getAnnotation(Ignore.class);
            boolean accessFlag = f.isAccessible();
            f.setAccessible(true);
            try {
                if (ig != null) {
                    if (ig.byValue()) {
                        if (f.getType().equals(int.class)) {
                            if (f.getInt(o) != ig.intValue()) {
                                data.add(new BasicNameValuePair(fp == null ? f
                                        .getName() : fp.value(), f.get(o)
                                        .toString()));
                            }
                        } else if (f.getType().equals(String.class)) {
                            if (f.get(o) != null
                                    && !f.get(o).equals(ig.stringValue())) {
                                data.add(new BasicNameValuePair(fp == null ? f
                                        .getName() : fp.value(), f.get(o)
                                        .toString()));
                            }
                        }
                    }
                    continue;
                }
                if (fp == null && f.get(o) != null) {
                    data.add(new BasicNameValuePair(f.getName(), f.get(o)
                            .toString()));
                } else if (f.get(o) != null) {
                    data.add(new BasicNameValuePair(fp.value(), f.get(o)
                            .toString()));
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            f.setAccessible(accessFlag);
        }
        return data;
    }

    /**
     * 获取用户个人信息
     *
     * @param token
     * @param uid
     * @return
     */
    public PersonInfoResult getPersonInfo(String token, String uid) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("uid", uid));
        return HttpDecoder.getForObject(getUrl("user"), PersonInfoResult.class, params);
    }

    /**
     * 修改用户信息
     *
     * @param info
     * @return
     */
    public BaseResult modifyPersonInfo(ModifyPersonInfo info) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", info.getToken()));
        params.add(new BasicNameValuePair("nickname", info.getNickname()));
        params.add(new BasicNameValuePair("age", info.getAge()));
        params.add(new BasicNameValuePair("gender", info.getGender()));
        params.add(new BasicNameValuePair("country", info.getCountry()));
        params.add(new BasicNameValuePair("province", info.getProvince()));
        params.add(new BasicNameValuePair("city", info.getCity()));
        params.add(new BasicNameValuePair("email", info.getEmail()));
        params.add(new BasicNameValuePair("head_url", info.getHear_url()));
        params.add(new BasicNameValuePair("about_me", info.getAbout_me()));
        params.add(new BasicNameValuePair("high", info.getHigh()));
        params.add(new BasicNameValuePair("education", info.getEducation()));
        params.add(new BasicNameValuePair("marital_status", info.getMarital_status()));
        params.add(new BasicNameValuePair("address", info.getAddress()));
        params.add(new BasicNameValuePair("graduate_school", info.getGraduate_school()));
        params.add(new BasicNameValuePair("work", info.getWork()));
        params.add(new BasicNameValuePair("tags", info.getTags()));
        params.add(new BasicNameValuePair("body_type", info.getBody_type()));
        params.add(new BasicNameValuePair("want_go", info.getWant_go()));
        params.add(new BasicNameValuePair("been_go", info.getBeen_go()));
        params.add(new BasicNameValuePair("hobbies", info.getHobbies()));
        params.add(new BasicNameValuePair("music", info.getMusic()));
        params.add(new BasicNameValuePair("film", info.getFilm()));
        params.add(new BasicNameValuePair("book", info.getBook()));
        params.add(new BasicNameValuePair("food", info.getFood()));

        return HttpDecoder.postForObject(getUrl("user"), BaseResult.class, params);
    }

    /**
     * 获取话题列表
     *
     * @param info info.type hot(热门)/nearby(身旁)
     * @return
     */
    public TopicListResult getTopicList(MFriendsInfo info) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", info.getToken()));
        params.add(new BasicNameValuePair("page", info.getPage()));
        params.add(new BasicNameValuePair("latitude", info.getLatitude()));
        params.add(new BasicNameValuePair("longitude", info.getLongitude()));
        params.add(new BasicNameValuePair("num", info.getNum()));
        params.add(new BasicNameValuePair("type", info.getType()));
//		return HttpDecoder.postForObject(
//				getUrl("topic"), TopicListResult.class,
//				params);
        return HttpDecoder.getForObject(
                getUrl("users"), TopicListResult.class,
                params);
    }

    /**
     * 获取发布的动态
     *
     * @return
     */
    public TopicListResult getPublishStates(String token, String uid, String num) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("page", "1"));
        params.add(new BasicNameValuePair("num", num));
        return HttpDecoder.getForObject(
                getUrl("user/state/list"), TopicListResult.class,
                params);
    }

    /**
     * 获取话题评论列表
     *
     * @param sid
     * @param token
     * @return
     */
    public TopicCommentsListResult getTopicComments(String sid, String token, String num) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("sid", sid));
        params.add(new BasicNameValuePair("page", "1"));
        params.add(new BasicNameValuePair("num", num));
        return HttpDecoder.getForObject(
                getUrl("user/state/reply"), TopicCommentsListResult.class,
                params);
    }

    /**
     * 获取话题详情
     *
     * @return
     */
    public TopicResult getTopicDetail(String sid, String token) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id", sid));
        params.add(new BasicNameValuePair("token", token));
        return HttpDecoder.getForObject(
                getUrl("user/state"), TopicResult.class,
                params);
    }

    //对帖子发表评论
    public BaseResult publishComment(String token, String content, String sid) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("pid", sid));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("sid", sid));
        params.add(new BasicNameValuePair("content", content));
        return HttpDecoder.postForObject(
                getUrl("user/state/reply"), BaseResult.class,
                params);
    }

    /**
     * 发表话题
     *
     * @param aid
     * @param token
     * @param content
     * @param img
     * @return
     */
    public BaseResult publishTopic(String aid, String token, String content, String img) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("aid", aid));
        params.add(new BasicNameValuePair("content", content));
        params.add(new BasicNameValuePair("img", img));
        LogUtil.e("上传的图片", "img: " + img);
        return HttpDecoder.postForObject(
                getUrl("user/state"), BaseResult.class,
                params);
    }

    /**
     * 获取话题消息列表
     *
     * @param count
     * @return
     */
    public TopicMessageListResult getTopicMessage(String token, String count, String type) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("num", count));
        params.add(new BasicNameValuePair("page", "1"));
        return HttpDecoder.getForObject(
                getUrl("user/msg/"+type), TopicMessageListResult.class,
                params);

    }

    /**
     * 标记消息为已读状态
     *
     * @param token
     * @param smid
     * @return
     */
    public MessageResult readTopicMessage(String token, String smid) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("id", smid));
        return HttpDecoder.postForObject(
                getUrl("user/msg"), MessageResult.class,
                params);
    }

    /**
     * 删除消息
     *
     * @param token
     * @param smid
     * @return
     */
    public MessageResult deleteTopicMessage(String token, String smid) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("id", smid));
        return HttpDecoder.postForObject(
                getUrl("user/msg/delete"), MessageResult.class,
                params);
    }

    /**
     * 清空所有消息
     *
     * @param token
     * @return
     */
    public BaseResult emptyTopicMessage(String token, String type) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        return HttpDecoder.postForObject(
                getUrl("user/msg/delete/"+type), BaseResult.class,
                params);
    }

    /**
     * 喜欢/取消喜欢用户
     *
     * @param token
     * @param toUser
     * @return
     */
    public BaseResult like(String token, String toUser) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("to_uid", toUser));
        return HttpDecoder.postForObject(
                getUrl("user/like"), BaseResult.class,
                params);

    }

    /**
     * 移除照片
     *
     * @param token
     * @param photoId
     * @return
     */
    public PhotoUploadResult removePhoto(String token, String photoId) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("id", photoId));
        return HttpDecoder.getForObject(
                getUrl("user/photo_del"), PhotoUploadResult.class,
                params);

    }

    /**
     * 修改用户头像
     *
     * @param
     * @param
     * @return
     */
    public PhotoUploadResult modifyUserPhoto(String token, File img) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        String ext = Util.getExtName(img);
        Log.d("图片格式", ext);
        params.add(new BasicNameValuePair("ext", ext));
        List<File> files = new ArrayList<File>();
        files.add(img);
        return HttpDecoder.postForObject(
                getUrl("user/avatar"), PhotoUploadResult.class,
                params, files);

    }

    /**
     * 获取红包记录
     *
     * @return
     */
    public RedPackageListResult getLuckyList (String token, String num){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("page","1"));
        params.add(new BasicNameValuePair("num",num));
        return HttpDecoder.getForObject(
                getUrl("user/lucky"), RedPackageListResult.class,
                params);
    }

    /**
     * 每日一卦
     *
     * @return
     */
    public SymbolResult getSymbol (String token){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token",token));
        return HttpDecoder.postForObject(
                getUrl("user/day"), SymbolResult.class,
                params);
    }

    /**
     * 获取红包金额(没用)
     *
     * @return
     */
    public MoneyResult getLuckyMoney (String token){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token",token));
        return HttpDecoder.getForObject(
                getUrl("user/lucky_money"), MoneyResult.class,
                params);
    }


    /**
     * 优惠码兑换红包
     * @param token
     * @param code
     * @return
     */
    public LuckyResult exchangeCouponCode(String token, String code){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token",token));
        params.add(new BasicNameValuePair("code",code));
        return HttpDecoder.postForObject(
                getUrl("user/lucky"), LuckyResult.class,
                params);
    }

    /**
     * 获取黑名单列表
     * @param token
     * @param uid
     * @return
     */
    public BlackResult getBlackList(String token, String uid){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token",token));
        params.add(new BasicNameValuePair("uid",uid));
        params.add(new BasicNameValuePair("page","1"));
        params.add(new BasicNameValuePair("num","10"));
        return HttpDecoder.getForObject(getUrl("user/blocks"),
                BlackResult.class, params);
    }
/**
     * 加入/解除黑名单
     * @param token
     * @param to_uid
     * @return
     */
    public BaseResult setBlackList(String token, String to_uid){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token",token));
        params.add(new BasicNameValuePair("uid",to_uid));
        return HttpDecoder.postForObject(getUrl("user/Blocks"),
                BaseResult.class, params);
    }

    /**
     * 获取我的秒旅团
     * @param token
     * @param uid
     * @param type  owner(我发起的)/join(我参加的)/like(我喜欢的)
     * @param num
     * @return
     */
    public MyCustomTourResult getOwnerCustomerTour(String token, String uid, String type, String num){

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("page", "1"));
        params.add(new BasicNameValuePair("num", num));
        return HttpDecoder.getForObject(getUrl("user/activity/" + type),
                MyCustomTourResult.class, params);
    }

/**
     * 获取我的约游
     * @param token
     * @param uid
     * @param type  owner(我发起的)/join(我参加的)/like(我喜欢的)
     * @param num
     * @return
     */
    public MyTogetherResult getMyTogetherList(String token, String uid, String type, String num){

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("uid", uid));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("page", "1"));
        params.add(new BasicNameValuePair("num", num));
        return HttpDecoder.getForObject(getUrl("user/yueyou/"+type),
                MyTogetherResult.class, params);
    }

}
