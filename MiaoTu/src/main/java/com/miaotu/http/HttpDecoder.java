package com.miaotu.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLHandshakeException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.annotation.SuppressLint;
import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miaotu.util.EasySSLSocketFactory;
import com.miaotu.util.LogUtil;

/**
 * 发送http请求并将响应信息解析为字符串或对象
 *
 * @author sunfuyong
 *
 */
public class HttpDecoder {

	private static final int READ_TIMEOUT = 10000;
	private static final int CONNECTION_TIMEOUT = 30000;
	private static DefaultHttpClient mClient;
	private static BasicHttpContext localcontext;

	/**
	 * 将get请求url中参数特殊值进行编码
	 *
	 * @param uri
	 * @param params
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private static String getEncodedGetUri(String uri,
			List<NameValuePair> params) throws UnsupportedEncodingException {
		if (params == null || uri == null) {
			return uri;
		}
		StringBuilder builder = new StringBuilder();
		builder.append(uri);
		builder.append(uri.indexOf("?") > 0 ? '&' : '?');
		for (NameValuePair pair : params) {
			builder.append(pair.getName()).append("=")
					.append(URLEncoder.encode(pair.getValue(), HTTP.UTF_8))
					.append("&");
		}
		builder.deleteCharAt(builder.length() - 1);

		return builder.toString();

	}

	/**
	 * 以post方式发送请求，访问web
	 *
	 * @param uri
	 *            web地址
	 * @return 响应数据
	 */
	public static String postForString(String uri, List<NameValuePair> params) {
        		String ss = "";
		for(NameValuePair param:params){
			ss += param.getName()+"="+param.getValue()+"&";
		}
		LogUtil.d("url:",uri+"?"+ss);
		BufferedReader reader = null;
		StringBuffer sb = null;
		String result = "";
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(uri);
		try {
			// 设置字符集
			HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			// 请求对象
			request.setEntity(entity);
			// 发送请求
			HttpResponse response = client.execute(request);

			// 请求成功
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				reader = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				sb = new StringBuffer();
				String line = "";
				// String NL = System.getProperty("line.separator");
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				// 关闭流
				if (null != reader) {
					reader.close();
					reader = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (null != sb) {
			result = sb.toString();
		}
		LogUtil.d(result);
		return result;
	}

	/**
	 * post文件
	 *
	 * @param url
	 * @param clz
	 * @param params
	 * @return
	 */
	public static <T> T postForObject(String url, Class<T> clz,
			List<NameValuePair> params,List<File> files) {
		//打印上传的参数
		String param = url;
		for(int i=0;i<params.size();i++){
			NameValuePair pair = params.get(i);
		    param+="&"+pair.getName()+"="+pair.getValue();
		}
		Log.d("post参数", param);
		Log.d("", "Sending  request to " + url);


		HttpParams ps = new BasicHttpParams();
		ConnManagerParams.setTimeout(ps, 5000);
		ConnManagerParams.setMaxTotalConnections(ps, 10);

		HttpProtocolParams.setVersion(ps, HttpVersion.HTTP_1_1);

		HttpProtocolParams.setUserAgent(ps,"miaotu");

		// Create and initialize scheme registry
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
			.getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(),
			443));
		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(
				ps, schemeRegistry);
		mClient = new DefaultHttpClient(cm, ps);
		ObjectMapper mapper = getObjectMapper();
		T result = null;
		URI uri = createURI(url);
		HttpUriRequest method = null;
		method = createMethod(HttpPost.METHOD_NAME, uri, files, params);
		SetupHTTPConnectionParams(method);
		try {
			HttpResponse response = mClient.execute(method, localcontext);
			// 请求成功
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = null;
				StringBuffer sb = null;
				reader = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				sb = new StringBuffer();
				String line = "";
				// String NL = System.getProperty("line.separator");
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
//				result = mapper.readValue(response
//						.getEntity().getContent(), clz);
				LogUtil.d(sb.toString());
				if (null != sb) {
					result = mapper.readValue(sb.toString(), clz);
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;

	}
	/**
     * CreateURI from URL string
     *
     * @param url
     * @return request URI
     * @throws org.apache.http.HttpException
     *             Cause by URISyntaxException
     */
    private static URI createURI(String url) {
		URI uri=null;
		try {
			uri = new URI(url);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return uri;
    }
    /**
     * Setup HTTPConncetionParams
     *
     * @param method
     */
    private static void SetupHTTPConnectionParams(HttpUriRequest method) {
	HttpConnectionParams.setConnectionTimeout(method.getParams(),
			40 * 1000);
	HttpConnectionParams
		.setSoTimeout(method.getParams(), 40000 );

	mClient.setHttpRequestRetryHandler(requestRetryHandler);
	HttpConnectionParams.setSocketBufferSize(method.getParams(), 8192);
	method.addHeader("Accept-Encoding", "gzip, deflate");
    }
    /**
     * 异常自动恢复处理, 使用HttpRequestRetryHandler接口实现请求的异常恢复
     */
    private static HttpRequestRetryHandler requestRetryHandler = new HttpRequestRetryHandler() {
	// 自定义的恢复策略
	@Override
	public boolean retryRequest(IOException exception, int executionCount,
		HttpContext context) {
	    // 设置恢复策略，在发生异常时候将自动重试N次
	    if (executionCount >= 3) {
		// Do not retry if over max retry count
		return false;
	    }
	    if (exception instanceof NoHttpResponseException) {
		// Retry if the server dropped connection on us
		return true;
	    }
	    if (exception instanceof SSLHandshakeException) {
		// Do not retry on SSL handshake exception
		return false;
	    }
	    HttpRequest request = (HttpRequest) context
		    .getAttribute(ExecutionContext.HTTP_REQUEST);
	    boolean idempotent = (request instanceof HttpEntityEnclosingRequest);
	    if (!idempotent) {
		// Retry if the request is considered idempotent
		return true;
	    }
	    return false;
	}
    };
	private static HttpUriRequest createMethod(String httpMethod, URI uri,
    	    List<File> files, List<NameValuePair> postParams)
    	    {

    	HttpUriRequest method;

    	if (httpMethod.equalsIgnoreCase(HttpPost.METHOD_NAME)) {
    	    // POST METHOD

    	    HttpPost post = new HttpPost(uri);

    	    // See this:
    	    // http://groups.google.com/group/twitter-development-talk/browse_thread/thread/e178b1d3d63d8e3b
    	    post.getParams().setBooleanParameter(
					"http.protocol.expect-continue", false);
    		HttpEntity entity = null;
    		if (null != files) {
    		    List<String> filenames = new ArrayList<String>(files.size());
    		    filenames.add("picture");
                LogUtil.d("文件个数1："+files.size());
    		    try {
					entity = createMultipartEntity(filenames, files, postParams);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

    		} else if (null != postParams) {
    		    try {
					entity = new UrlEncodedFormEntity(postParams, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

    		}

    		post.setEntity(entity);


    	    method = post;
    	} else if (httpMethod.equalsIgnoreCase(HttpDelete.METHOD_NAME)) {
    	    method = new HttpDelete(uri);
    	} else {
    	    method = new HttpGet(uri);
    	}

    	return method;
        }
	/**
     * 创建可带多个File的MultipartEntity
     *
     * @param filenames
     *            文件名
     * @param files
     *            文件
     * @param postParams
     *            其他POST参数
     * @return 带文件和其他参数的Entity
     * @throws UnsupportedEncodingException
     *
     */
    @SuppressLint("NewApi")
	private static MultipartEntity createMultipartEntity(List<String> filenames,
	    List<File> files, List<NameValuePair> postParams)
	    throws UnsupportedEncodingException {
	MultipartEntity entity = new MultipartEntity();
	// Don't try this. Server does not appear to support chunking.
	// entity.addPart("media", new InputStreamBody(imageStream, "media"));
	// String fileType =
	// file.getName().substring(file.getName().lastIndexOf('.') + 1);
	for (int i = 0; files != null && i < files.size(); i++) {
	    entity.addPart("picture"+i, new FileBody(files.get(i)));
	    Log.d("上传文件", files.get(i).getTotalSpace()+"b");
	}

	for (NameValuePair param : postParams) {
	    entity.addPart(param.getName(), new StringBody(param.getValue(), Charset.forName("UTF-8")));
	}

	return entity;
    }
	/**
	 * 将返回结果峰值成指定类型的对象后返回
	 *
	 * @param url
	 * @param clz
	 * @param params
	 * @return
	 */
	public static <T> T postForObject(String url, Class<T> clz,
			List<NameValuePair> params) {
		ObjectMapper mapper = getObjectMapper();
		T result = null;
		if (LogUtil.isDebug) {
			String str = postForString(url, params);
			try {
				result = mapper.readValue(str, clz);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}

		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(url);

		try {
			// 设置字符集
			HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
			// 请求对象
			request.setEntity(entity);
			// 发送请求
			HttpResponse response = client.execute(request);

			// 请求成功
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				result = mapper.readValue(response.getEntity().getContent(),
						clz);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public static <T> T getForObject(String url, Class<T> clz) {
		return getForObject(url, clz, null);
	}

	/**
	 * 将返回结果峰值成指定类型的对象后返回
	 *
	 * @param url
	 * @param clz
	 * @param params
	 * @return
	 */
	public static <T> T getForObject(String url, Class<T> clz,
			List<NameValuePair> params) {
		ObjectMapper mapper = getObjectMapper();
		T result = null;
		if (LogUtil.isDebug) {
			String str = getForString(url, params);
			try {
				result = mapper.readValue(str, clz);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}
		InputStream in = null;
		HttpURLConnection conn = null;
		try {
			conn = getConnection(url, params);
			in = conn.getInputStream();
			result = mapper.readValue(in, clz);
			// String sessionID = CookieManage.gethttpurlconnection(conn);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		Log.i("lgs", result.toString());
		return result;
	}
	/**
	 * 将返回结果峰值成指定类型的对象后返回
	 * 根据对象的类解析
	 * @param url
	 * @param
	 * @param params
	 * @return
	 */


	public static String getForString(String url, List<NameValuePair> params) {
		// InputStream in = null;
		String result = null;
		BufferedReader reader = null;
		HttpURLConnection conn = null;
        String ss="";
        for(NameValuePair param:params){
            ss += param.getName()+"="+param.getValue()+"&";
        }
        LogUtil.d("url:",url+"?"+ss);
		try {
			conn = getConnection(url, params);
			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			StringBuilder builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append("\n");
			}
			result = builder.toString();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		LogUtil.d(result);
		return result;
	}

	private static HttpURLConnection getConnection(String url,
			List<NameValuePair> params) throws MalformedURLException,
			UnsupportedEncodingException, IOException {
		LogUtil.d(getEncodedGetUri(url, params));
		String temp = getEncodedGetUri(url, params);
		HttpURLConnection conn = (HttpURLConnection) new URL(temp)
				.openConnection();
		conn.setConnectTimeout(CONNECTION_TIMEOUT);
		conn.setReadTimeout(READ_TIMEOUT);
		conn.setRequestMethod("GET");
		return conn;
	}

	public static ObjectMapper getObjectMapper() {
		ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		return mapper;
	}

	public static String getForString(String string) {
		return getForString(string, null);
	}

	public static String f_imageToString(File imgFile) {
		// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 返回Base64编码过的字节数组字符串
		Log.d("Size", data.length / 1024 + "");
		String str = new String(Base64.encode(data, Base64.DEFAULT));
		return str;
	}
}
