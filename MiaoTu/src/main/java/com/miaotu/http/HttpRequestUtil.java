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
import com.miaotu.model.PersonInfoResult;
import com.miaotu.form.PublishTogether;
import com.miaotu.model.RegisterInfo;
import com.miaotu.model.Together;
import com.miaotu.result.BaseResult;
import com.miaotu.result.LoginResult;
import com.miaotu.result.PhotoUploadResult;
import com.miaotu.util.StringUtil;

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
	 * @param registerInfo
	 * @return
	 */
    public LoginResult login (RegisterInfo registerInfo){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
		if(!StringUtil.isEmpty(registerInfo.getPhone())){
			params.add(new BasicNameValuePair("phone", registerInfo.getPhone()));
		}
		if(!StringUtil.isEmpty(registerInfo.getPassword())){
			params.add(new BasicNameValuePair("password", registerInfo.getPassword()));
		}
		if(!StringUtil.isEmpty(registerInfo.getUsid())){
			params.add(new BasicNameValuePair("usid", registerInfo.getUsid()));
		}
		if(!StringUtil.isEmpty(registerInfo.getUnionid())){
			params.add(new BasicNameValuePair("unionid", registerInfo.getUnionid()));
		}
		if(!StringUtil.isEmpty(registerInfo.getOpenid())){
			params.add(new BasicNameValuePair("openid", registerInfo.getOpenid()));
		}
        return HttpDecoder.getForObject(
				getUrl("base/user"), LoginResult.class,
				params);

    }
	/**
	 * 注册
	 * @param registerInfo
	 * @return
	 */
    public BaseResult register (RegisterInfo registerInfo){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
		if(!StringUtil.isEmpty(registerInfo.getPhone())){
			params.add(new BasicNameValuePair("phone", registerInfo.getPhone()));
		}
		if(!StringUtil.isEmpty(registerInfo.getPassword())){
			params.add(new BasicNameValuePair("password", registerInfo.getPassword()));
		}
		if(!StringUtil.isEmpty(registerInfo.getUsid())){
			params.add(new BasicNameValuePair("usid", registerInfo.getUsid()));
		}
		if(!StringUtil.isEmpty(registerInfo.getUnionid())){
			params.add(new BasicNameValuePair("unionid", registerInfo.getUnionid()));
		}
		if(!StringUtil.isEmpty(registerInfo.getOpenid())){
			params.add(new BasicNameValuePair("openid", registerInfo.getOpenid()));
		}
		if(!StringUtil.isEmpty(registerInfo.getCode())){
			params.add(new BasicNameValuePair("code", registerInfo.getCode()));
		}
		if(!StringUtil.isEmpty(registerInfo.getGender())){
			params.add(new BasicNameValuePair("gender", registerInfo.getGender()));
		}
		if(!StringUtil.isEmpty(registerInfo.getNickname())){
			params.add(new BasicNameValuePair("nickname", registerInfo.getNickname()));
		}
		if(!StringUtil.isEmpty(registerInfo.getProvince())){
			params.add(new BasicNameValuePair("province", registerInfo.getProvince()));
		}
		if(!StringUtil.isEmpty(registerInfo.getCity())){
			params.add(new BasicNameValuePair("city", registerInfo.getCity()));
		}
		if(!StringUtil.isEmpty(registerInfo.getBirthday())){
			params.add(new BasicNameValuePair("birthday", registerInfo.getBirthday()));
		}
		if(!StringUtil.isEmpty(registerInfo.getAge())){
			params.add(new BasicNameValuePair("age", registerInfo.getAge()));
		}
		if(!StringUtil.isEmpty(registerInfo.getHeadurl())){
			params.add(new BasicNameValuePair("headurl", registerInfo.getHeadurl()));
		}
        return HttpDecoder.postForObject(
				getUrl("base/user"), BaseResult.class,
				params);

	}

	/**
	 * 获取验证码
	 * @param tel
	 * @return
	 */
    public BaseResult getSms (String tel){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("phone", tel));
        return HttpDecoder.getForObject(
				getUrl("sms/register"), BaseResult.class,
				params);

	}

	/**
	 * 获取约游列表
	 * @param token
	 * @param page
	 * @param num
	 * @return
	 */
    public BaseResult getTogetherList (String token,String page,String num){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("token", token));
		params.add(new BasicNameValuePair("page", page));
		params.add(new BasicNameValuePair("num", num));
        return HttpDecoder.getForObject(
				getUrl("yueyou/list"), BaseResult.class,
				params);

	}
	/**
	 * 发布一起去
	 * @param together
	 * @return
	 */
    public BaseResult publishTogether (PublishTogether together){
        return HttpDecoder.postForObject(
				getUrl("yueyou/"), BaseResult.class,
				transformObject2Map(together));

	}
	/**
	 * 上传照片
//	 * @param token
	 * @param img
	 * @return
	 */
	public PhotoUploadResult uploadPhoto (List<File> img){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
//		params.add(new BasicNameValuePair("token",token));
//		String ext = Util.getExtName(img);
//		Log.d("图片格式", ext);
//		params.add(new BasicNameValuePair("ext",ext));
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
	 * @param token
	 * @param uid
	 * @return
	 */
	public PersonInfoResult getPersonInfo(String token, String uid){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("token", token));
		params.add(new BasicNameValuePair("uid", uid));
		return HttpDecoder.getForObject(getUrl("user"), PersonInfoResult.class, params);
	}

}
