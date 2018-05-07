package com.andy.myproject_007.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;


import com.andy.myproject_007.common.MainApplication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


/**
 * @Describe 
 * @Author guoenbo
 * @Date 2016-9-5 下午4:53:45
 */
public class NetUtil1 {

	/**
	 * 编码格式
	 * */
	private static final String UTF_8 = "utf-8";

	/**
	 * 创建固定数量的线程池来管理线程
	 */
	private static ExecutorService executorService = Executors.newFixedThreadPool(5);

	/**
	 * 设置忽略SSL证书检查
	 */
	private static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	private static SSLContext SSLcontext;

	/**
	 * 向UI线程发送消息
	 * */
	private static Handler handler = new Handler();

	/**
	 * 请求回调接口
	 * */
	public interface RequestCallBack {

		/**
		 * 请求成功
		 * @param statusCode 状态码
		 * @param json 返回JSON数据
		 * */
		public void onSuccess(int statusCode, String json);

		/**
		 * 接口异常
		 * @param statusCode 状态码
		 * @param errorMsg 异常提示信息
		 * */
		public void onFailure(int statusCode, String errorMsg);

		/**
		 * 请求失败
		 * @param e 异常
		 * @param errorMsg 异常提示信息
		 * */
		public void onFailure(Exception e, String errorMsg);

	}

	private static String getBaseUrl(){
		String baseUrl = MainApplication.getURL();
		return baseUrl;
	}
	private static String getliveUrl(){
		String baseUrl = MainApplication.getLiveURL();
		return baseUrl;
	}
	private static String getAppUrl() {
		String baseUrl = MainApplication.getAppURL();
		return baseUrl;
	}

	public static void getApp(final Context context,String action, Map<String, Object> params, final RequestCallBack mRequestCallBack) {
		final String requestUrl = getAppUrl() + action + "?" + getParamsUrl(params);
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				RequestGetUrl(requestUrl, mRequestCallBack);
			}
		});
	}

	/**
	 * Post 可多文件上传
	 *
	 * @param action           接口名
	 * @param params           请求参数，上传文件格式params.put(file.getName(), file);
	 * @param mRequestCallBack 实现回调得到服务器返回数据
	 */
	public static void postApp(final String action, final Map<String, Object> params, final RequestCallBack mRequestCallBack) {
		final Map<String, Object> map = new HashMap<String, Object>();
		final Map<String, File> files = new HashMap<String, File>();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			if (entry.getValue() instanceof File) {
				files.put(entry.getKey(), (File) entry.getValue());
			} else {
				map.put(entry.getKey(), entry.getValue());
			}
		}
		executorService.submit(new Runnable() {
			@Override
			public void run() {

				RequestPostUpLoadFile(getAppUrl() + action, map, files, mRequestCallBack);
			}
		});

	}

	/**
	 * Get
	 * @param action 接口名
	 * @param params 请求参数
	 * @param mRequestCallBack 实现回调得到服务器返回数据
	 */
	public static void get(String action, Map<String, Object> params, final RequestCallBack mRequestCallBack) {
		final String requestUrl = getBaseUrl() + action + "?" + getParamsUrl(params);
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				RequestGetUrl(requestUrl, mRequestCallBack);
			}
		});
	}

	/**
	 * Post 可多文件上传
	 * @param action 接口名
	 * @param params 请求参数，上传文件格式params.put(file.getName(), file);
	 * @param mRequestCallBack 实现回调得到服务器返回数据
	 */
	public static void post(final String action, final Map<String, Object> params, final RequestCallBack mRequestCallBack) {
		final Map<String, Object> map = new HashMap<String, Object>();
		final Map<String, File> files = new HashMap<String, File>();
		for(Map.Entry<String, Object> entry : params.entrySet()){
			if(entry.getValue() instanceof File){
				files.put(entry.getKey(), (File)entry.getValue());
			} else {
				map.put(entry.getKey(), entry.getValue());
			}
		}
		executorService.submit(new Runnable() {
			@Override
			public void run() {

				RequestPostUpLoadFile(getBaseUrl() + action, map, files, mRequestCallBack);
			}
		});

	}
	public static void getLive(String action, Map<String, Object> params, final RequestCallBack mRequestCallBack) {
		final String requestUrl = getliveUrl() + action + "?" + getParamsUrl(params);
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				RequestGetUrl(requestUrl, mRequestCallBack);
			}
		});
	}

	/**
	 * Post 可多文件上传
	 * @param action 接口名
	 * @param params 请求参数，上传文件格式params.put(file.getName(), file);
	 * @param mRequestCallBack 实现回调得到服务器返回数据
	 */
	public static void postLive(final String action, final Map<String, Object> params, final RequestCallBack mRequestCallBack) {
		final Map<String, Object> map = new HashMap<String, Object>();
		final Map<String, File> files = new HashMap<String, File>();
		for(Map.Entry<String, Object> entry : params.entrySet()){
			if(entry.getValue() instanceof File){
				files.put(entry.getKey(), (File)entry.getValue());
			} else {
				map.put(entry.getKey(), entry.getValue());
			}
		}
		executorService.submit(new Runnable() {
			@Override
			public void run() {

				RequestPostUpLoadFile(getliveUrl() + action, map, files, mRequestCallBack);
			}
		});

	}

	/**
	 * Get请求， https的注释主要写在该方法里
	 * */
	private static boolean RequestGetUrl(String requestUrl, final RequestCallBack mRequestCallBack) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(requestUrl);
			if (url.getProtocol().toLowerCase().equals("https")) {
				//信任所有主机
				trustAllHosts();
				//设置SSL证书的相关处理,当这个方法没有注释掉时trustAllHosts()需要注释掉
				//AuthenticationHosts(MainApplication.getContext());
				//创建https链接
				HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
				//设置证书认证，当这个方法没有注释掉时trustAllHosts()需要注释掉
				//https.setSSLSocketFactory(SSLcontext.getSocketFactory());
				//setSSLSocketFactory + AuthenticationHosts()设置证书单向验证
				//通过所有证书，不验证安全性(不验证是否CA颁发的证书)
				https.setHostnameVerifier(DO_NOT_VERIFY);//trustAllHosts（）+setHostnameVerifier()设置信任所有证书
				conn = https;
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(30000);// 设置连接主机超时（单位：毫秒）
			conn.setReadTimeout(30000);// 设置从主机读取数据超时（单位：毫秒）
			final int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				// 发送请求，获取服务器数据
				InputStream is = conn.getInputStream();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				StringBuffer buffer = new StringBuffer();
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				final String json = buffer.toString();
				//在子线程中更新主线程（UI线程）
				handler.post(new Runnable() {
					@Override
					public void run() {
						mRequestCallBack.onSuccess(responseCode, json);
					}
				});
				return true;
			} else {
				handler.post(new Runnable() {
					@Override
					public void run() {
						// 请求服务器出错
						mRequestCallBack.onFailure(responseCode, "连接出错");
					}
				});
				return false;
			}
		} catch (final SocketTimeoutException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					mRequestCallBack.onFailure(e, "网络超时");
				}
			});
			return false;
		} catch (final ConnectException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					mRequestCallBack.onFailure(e, "连接服务异常");
				}
			});
			return false;
		} catch (final IOException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					mRequestCallBack.onFailure(e, "解析数据异常");
				}
			});
			return false;
		} finally {
			conn.disconnect();
		}
	}

	/**
	 * Post请求，支持Https请求，支持上传文件
	 */
	private static boolean RequestPostUpLoadFile(String RequestURL, Map<String, Object> params, Map<String, File> files, final RequestCallBack mRequestCallBack){
		HttpURLConnection conn = null;
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data"; // 内容类型
		try {
			URL url = new URL(RequestURL);
			//获取协议类型并转成小写，即http或https
			if (url.getProtocol().toLowerCase().equals("https")) {
				//信任所有主机
				trustAllHosts();
				//创建https链接
				HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
				//通过所有证书，不验证安全性(不验证是否CA颁发的证书)
				https.setHostnameVerifier(DO_NOT_VERIFY);
				conn = https;
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			conn.setReadTimeout(100 * 1000);
			conn.setConnectTimeout(100 * 1000);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", UTF_8); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			// 首先组拼文本类型的参数
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END);
				sb.append("Content-Type: text/plain; charset=" + UTF_8 + LINE_END);
				sb.append("Content-Transfer-Encoding: 8bit" + LINE_END);
				sb.append(LINE_END);
				sb.append(StringUtil.isEmptyToString(entry.getValue()));
				sb.append(LINE_END);
			}
			dos.write(sb.toString().getBytes());
			// 发送文件数据
			if (files != null){
				for (Map.Entry<String, File> file : files.entrySet()) {
					StringBuilder sb1 = new StringBuilder();
					sb1.append(PREFIX);
					sb1.append(BOUNDARY);
					sb1.append(LINE_END);
					sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getKey() + "\"" + LINE_END);
					sb1.append("Content-Type: application/octet-stream; charset=" + UTF_8 + LINE_END);
					sb1.append(LINE_END);
					dos.write(sb1.toString().getBytes());
					InputStream is = new FileInputStream(file.getValue());
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						dos.write(buffer, 0, len);
					}
					is.close();
					dos.write(LINE_END.getBytes());
				}
			}
			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
			dos.write(end_data);
			dos.flush();
			/**
			 * 获取响应码 200=成功 当响应成功，获取响应的流
			 */
			final int responseCode = conn.getResponseCode();
			if(responseCode == HttpURLConnection.HTTP_OK){
				InputStream input = conn.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				StringBuffer buffer = new StringBuffer();
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				final String json = buffer.toString();
				handler.post(new Runnable() {
					@Override
					public void run() {
						mRequestCallBack.onSuccess(responseCode, json);
					}
				});
				return true;
			}else {
				handler.post(new Runnable() {
					@Override
					public void run() {
						// 请求服务器出错
						mRequestCallBack.onFailure(responseCode, "连接出错");
					}
				});
				return false;
			}
		} catch (final SocketTimeoutException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					mRequestCallBack.onFailure(e, "网络超时");
				}
			});
			return false;
		} catch (final ConnectException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					mRequestCallBack.onFailure(e, "连接服务异常");
				}
			});
			return false;
		} catch (final IOException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					mRequestCallBack.onFailure(e, "解析数据异常");
				}
			});
			return false;
		} finally {
			conn.disconnect();
		}
	}

	/**
	 * 自动组装请求参数
	 * */
	private static String getParamsUrl(Map<String, Object> params) {
		StringBuffer stringBuffer = new StringBuffer(); // 存储封装好的请求体信息
		if(!StringUtil.isEmpty(params)){
			try {
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					stringBuffer.append(entry.getKey()).append("=").append(URLEncoder.encode(StringUtil.isEmptyToString(entry.getValue()),UTF_8)).append("&");
				}
				stringBuffer.deleteCharAt(stringBuffer.length() - 1); // 删除最后的一个"&"
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return stringBuffer.toString();
	}
	public static boolean checkNet(Context context) {// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 信任所有主机-对于任何证书都不做检查 (https协议)
	 */
	private static void trustAllHosts() {
		// 创建一个信任管理器不验证SSL证书检查链
		TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[]{};
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
		}};

		// 设置信任所有主机
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * https证书单向认证
	 * @param context
	 */
	private static void AuthenticationHosts(Context context){
		CertificateFactory cf = null;
		try {
			//读取证书
			cf = CertificateFactory.getInstance("X.509");
			InputStream in = context.getAssets().open("client.crt");
			Certificate ca = cf.generateCertificate(in);
			// 初始化keyStore，用来导入证书
			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			//参数null 表示使用系统默认keystore，也可使用其他keystore（需事先将srca.cer 证书导入keystore 里
			keystore.load(null, null);
			// 把client.crt 这个证书导入到KeyStore 里，别名叫做ca
			keystore.setCertificateEntry("ca", ca);
			//把该证书库作为信任证书库
			String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
			//用我们设定好的TrustManager 去做ssl 通信协议校验，即证书校验
			tmf.init(keystore);

			// Create an SSLContext that uses our TrustManager
			SSLcontext = SSLContext.getInstance("TLS");
			SSLcontext.init(null, tmf.getTrustManagers(), null);
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
