package com.andy.myproject_007.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;


import com.andy.myproject_007.common.MainApplication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
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
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


/**
 * @Describe
 * @Author leeandy007
 * @Date 2016-9-5 下午4:53:45
 */
public class NetUtil {

	/**
	 * 编码格式
	 * */
	private static final String UTF_8 = "utf-8";

	/**
	 * 创建固定数量的线程池来管理线程
	 */
	private static ExecutorService executorService = Executors.newFixedThreadPool(5);

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

	public interface DownloadCallBack{

		/**
		 * 请求成功
		 *
		 * @param bitmap 返回JSON数据
		 * */
		public void onSuccess(int statusCode, Bitmap bitmap);

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

	public enum RequestMethod{
		GET,
		POST
	}

	/**
	 * Request
	 * @param mRequestMethod 请求方法 GET或POST
	 * @param action 接口名
	 * @param params 请求参数
	 * @param mRequestCallBack 实现回调得到服务器返回数据
	 */
	public static void Request(RequestMethod mRequestMethod, String action, Map<String, Object> params,
							   final RequestCallBack mRequestCallBack){
		switch (mRequestMethod) {
			case GET:
				get(action, params, mRequestCallBack);
				break;
			case POST:
				post(action, params, mRequestCallBack);
				break;
		}
	}

	/**
	 * Get
	 * @param action 接口名
	 * @param params 请求参数
	 * @param mRequestCallBack 实现回调得到服务器返回数据
	 */
	private static void get(String action, Map<String, Object> params, final RequestCallBack mRequestCallBack) {
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
	private static void post(final String action, final Map<String, Object> params, final RequestCallBack mRequestCallBack) {
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

	/**
	 * Get请求
	 * */
	private static boolean RequestGetUrl(String requestUrl, final RequestCallBack mRequestCallBack) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(requestUrl);
			if (url.getProtocol().toLowerCase().equals("https")) {
				//信任所有主机（如果需要证书验证请在此处调用AuthenticationHosts()方法，并与下文https.setSSLSocketFactory(SSLcontext.getSocketFactory());联合使用）
				trustAllHosts();
				//设置SSL证书的相关处理
//				SSLContext mSSLContext = AuthenticationHosts(MainApplication.getContext());
				//创建https链接
				HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
				//设置证书认证
//				https.setSSLSocketFactory(mSSLContext.getSocketFactory());
				//设置ip授权认证：如果已经安装该证书，可以不设置，否则需要设置
				https.setHostnameVerifier(DO_NOT_VERIFY);
				conn = https;
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(30 * 1000);// 设置连接主机超时（单位：毫秒）
			conn.setReadTimeout(30 * 1000);// 设置从主机读取数据超时（单位：毫秒）
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
						if(responseCode >= HttpURLConnection.HTTP_BAD_REQUEST && responseCode < HttpURLConnection.HTTP_INTERNAL_ERROR){
							// 4XX 状态码表示请求可能出错
							mRequestCallBack.onFailure(responseCode, "请求异常");
						} else if(responseCode >= HttpURLConnection.HTTP_INTERNAL_ERROR){
							// 5XX 状态码表示服务器可能出错
							mRequestCallBack.onFailure(responseCode, "服务器异常");
						}
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
					mRequestCallBack.onFailure(e, "请检查您的网络");
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
	 * Post请求上传文件
	 */
	private static boolean RequestPostUpLoadFile(String RequestURL, Map<String, Object> params, Map<String, File> files, final RequestCallBack mRequestCallBack){
		HttpURLConnection conn = null;
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data"; // 内容类型
		try {
			URL url = new URL(RequestURL);
			if (url.getProtocol().toLowerCase().equals("https")) {
				//信任所有主机（如果需要证书验证请在此处调用AuthenticationHosts()方法，并与下文https.setSSLSocketFactory(SSLcontext.getSocketFactory());联合使用）
				trustAllHosts();
				//设置SSL证书的相关处理
//				SSLContext mSSLContext = AuthenticationHosts(MainApplication.getContext());
				//创建https链接
				HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
				//设置证书认证
//				https.setSSLSocketFactory(mSSLContext.getSocketFactory());
				//设置ip授权认证：如果已经安装该证书，可以不设置，否则需要设置
				https.setHostnameVerifier(DO_NOT_VERIFY);
				conn = https;
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			conn.setReadTimeout(100 * 1000);
			conn.setConnectTimeout(30 * 1000);
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
			if (files != null && files.size() != 0){
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
						if(responseCode >= HttpURLConnection.HTTP_BAD_REQUEST && responseCode < HttpURLConnection.HTTP_INTERNAL_ERROR){
							// 4XX 状态码表示请求可能出错
							mRequestCallBack.onFailure(responseCode, "请求异常");
						} else if(responseCode >= HttpURLConnection.HTTP_INTERNAL_ERROR){
							// 5XX 状态码表示服务器可能出错
							mRequestCallBack.onFailure(responseCode, "服务器异常");
						}
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
	public static String getParamsUrl(Map<String, Object> params) {
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
			return stringBuffer.toString();
		}
		return "";
	}

	public static void download(final String url, final DownloadCallBack mCallBack){
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				getImage(url, mCallBack);
			}
		});
	}

	/**
	 * 获取网络图片
	 *
	 * @param imageurl 图片网络地址
	 * @return Bitmap 返回位图
	 */
	private static void getImage(String imageurl, final DownloadCallBack mCallBack) {
		HttpURLConnection conn = null;
		try {
			URL url = new URL(imageurl);//创建URL对象
			if (url.getProtocol().toLowerCase().equals("https")) {
				//信任所有主机（如果需要证书验证请在此处调用AuthenticationHosts()方法，并与下文https.setSSLSocketFactory(SSLcontext.getSocketFactory());联合使用）
				trustAllHosts();
				//设置SSL证书的相关处理
//				SSLContext mSSLContext = AuthenticationHosts(MainApplication.getContext());
				//创建https链接
				HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
				//设置证书认证
//				https.setSSLSocketFactory(mSSLContext.getSocketFactory());
				//设置ip授权认证：如果已经安装该证书，可以不设置，否则需要设置
				https.setHostnameVerifier(DO_NOT_VERIFY);
				conn = https;
			} else {
				conn = (HttpURLConnection) url.openConnection();
			}
			conn.setConnectTimeout(5 * 1000); //超时设置
			conn.setDoInput(true);//设置输入为真
			conn.setUseCaches(false); //设置不使用缓存
			/**
			 * 获取响应码 200=成功 当响应成功，获取响应的流
			 */
			final int responseCode = conn.getResponseCode();
			if(responseCode == HttpURLConnection.HTTP_OK) {
				InputStream is = conn.getInputStream();//得到InputStream流对象
				/**
				 * android 2.3以下的版本由于系统的原因会加载不到图片，要用字节流处理
				 * */
				byte[] buffer = new byte[1024];//创建内存输出流对象
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					bos.write(buffer, 0, len);
					bos.flush();
				}
				bos.close();
				is.close();
				byte[] data = bos.toByteArray();
				final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
				handler.post(new Runnable() {
					@Override
					public void run() {
						mCallBack.onSuccess(responseCode, bitmap);
					}
				});
			} else {
				handler.post(new Runnable() {
					@Override
					public void run() {
						if(responseCode >= HttpURLConnection.HTTP_BAD_REQUEST && responseCode < HttpURLConnection.HTTP_INTERNAL_ERROR){
							// 4XX 状态码表示请求可能出错
							mCallBack.onFailure(responseCode, "请求异常");
						} else if(responseCode >= HttpURLConnection.HTTP_INTERNAL_ERROR){
							// 5XX 状态码表示服务器可能出错
							mCallBack.onFailure(responseCode, "服务器异常");
						}
					}
				});
			}
		} catch (final SocketTimeoutException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					mCallBack.onFailure(e, "网络超时");
				}
			});
		} catch (final ConnectException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					mCallBack.onFailure(e, "连接服务异常");
				}
			});
		} catch (final IOException e) {
			handler.post(new Runnable() {
				@Override
				public void run() {
					mCallBack.onFailure(e, "解析数据异常");
				}
			});
		} finally {
			conn.disconnect();
		}

	}


	/**
	 * 信任所有主机-对于任何证书都不做检查 (https协议)
	 */
	private static void trustAllHosts() {
		SSLContext mSSLContext = null;
		// 创建一个信任管理器不验证SSL证书检查链
		TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[]{};
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}
		}};

		// 设置信任所有主机
		try {
			mSSLContext = SSLContext.getInstance("TLS");
			mSSLContext.init(null, trustAllCerts, null);
			HttpsURLConnection.setDefaultSSLSocketFactory(mSSLContext.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * https证书单向认证
	 * @param context
	 */
	private static SSLContext AuthenticationHosts(Context context){
		SSLContext mSSLContext = null;
		try {
			InputStream in = new BufferedInputStream(context.getAssets().open("client.crt"));
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			Certificate ca = cf.generateCertificate(in);
			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			keystore.load(null, null);
			keystore.setCertificateEntry("ca", ca);
			String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
			tmf.init(keystore);
			mSSLContext = SSLContext.getInstance("TLS");
			mSSLContext.init(null, tmf.getTrustManagers(), null);
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
		return mSSLContext;
	}

    /**
     * https证书单向认证
     * @param context
     */
    private static SSLContext AuthenticationHosts1(final Context context){
        SSLContext mSSLContext = null;
        try {
            TrustManager[] trustManagers = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                            if(chain == null || chain.length < 0){
                                throw new IllegalArgumentException("请检查服务端证书是否为空");
                            }
                            for (X509Certificate cert : chain){
                                cert.checkValidity();
                                try {
                                    InputStream in = new BufferedInputStream(context.getAssets().open("client.crt"));
                                    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                                    X509Certificate serverCert = (X509Certificate)certificateFactory.generateCertificate(in);
                                    cert.verify(serverCert.getPublicKey());
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                } catch (InvalidKeyException e) {
                                    e.printStackTrace();
                                } catch (NoSuchProviderException e) {
                                    e.printStackTrace();
                                } catch (SignatureException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };
            mSSLContext = SSLContext.getInstance("TLS");
            mSSLContext.init(null, trustManagers, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(mSSLContext.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return mSSLContext;
    }

    /**
     * 设置忽略SSL证书验证(https协议)
     */
    private static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * 设置SSL证书验证(https协议)
     */
    private static HostnameVerifier VERIFY = new HostnameVerifier() {

        public boolean verify(String hostname, SSLSession session) {
            HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
            Boolean result = hostnameVerifier.verify(MainApplication.getURL(), session);
            return result;
        }
    };

}
