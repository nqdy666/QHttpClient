package net.qjzd.httpclient;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;


import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.alibaba.fastjson.JSONObject;

/**
 * 秦晋之巅HttpClient
 * http客户端
 * @author qjzd
 */
public class QHttpClient {

	/**
	 * post发送请求
	 * 请求参数使用map包装
	 * @param url 请求地址
	 * @param params 请求参数列表, 每一个请求参数为String类型
	 * @param handler 请求返回数据处理类
	 * @return 返回对象handler处理后的数据
	 * @throws ServerErrorException
	 */
	public static Object post(String url, Map<String, String> params, ResponseHandler<?> handler) throws ServerErrorException {
		CloseableHttpClient client = HttpClients.createMinimal();
		RequestBuilder rb = RequestBuilder.post();
		for (Entry<String, String> e : params.entrySet()) {
			rb.addParameter(e.getKey(), e.getValue());
		}
		rb.setUri(url);
		try {
			return client.execute(rb.build(), handler);
		} catch (ClientProtocolException e) {
			throw new ServerErrorException("服务器[" + url + "]连接不通", e);
		} catch (IOException e) {
			throw new ServerErrorException("服务器[" + url + "]访问异常", e);
		}
	}

	/**
	 * post发送请求
	 * 请求参数使用json包装
	 * @param url 请求地址
	 * @param params  请求参数列表, 每一个请求参数为String类型
	 * @param handler 请求返回数据处理类
	 * @param encoder 编码, 入参会使用该编码转换
	 * @return 返回对象handler处理后的数据
	 * @throws ServerErrorException
	 */
	public static Object postJson(String url, JSONObject params, ResponseHandler<?> handler, String encoder) throws ServerErrorException {
		if (encoder == null || "".equals(encoder.trim())) {
			encoder = "UTF-8";
		}
		CloseableHttpClient client = HttpClients.createMinimal();
		RequestBuilder rb = RequestBuilder.post();

		StringEntity entity = new StringEntity(params.toString(), encoder);// 解决中文乱码问题
		entity.setContentEncoding(encoder);
		entity.setContentType("application/json");
		rb.setUri(url);
		rb.setEntity(entity);

		try {
			return client.execute(rb.build(), handler);
		} catch (ClientProtocolException e) {
			throw new ServerErrorException("服务器[" + url + "]连接不通", e);
		} catch (IOException e) {
			throw new ServerErrorException("服务器[" + url + "]访问异常", e);
		}
	}

	/**
	 * 上传一个纯文件
	 * 如果要请求需要其他参数, 需要在url自动拼接
	 * @param url 请求地址
	 * @param file 带上传文件 
	 * @param handler 请求返回数据处理类
	 * @return 返回对象handler处理后的数据
	 * @throws ServerErrorException
	 */
	public static Object postFile(String url, File file, ResponseHandler<?> handler) throws ServerErrorException {
		CloseableHttpClient client = HttpClients.createMinimal();
		HttpPost hp = new HttpPost(url);
		hp.setEntity(new FileEntity(file));
		try {
			return client.execute(hp, handler);
		} catch (ClientProtocolException e) {
			throw new ServerErrorException("服务器[" + url + "]连接不通", e);
		} catch (IOException e) {
			throw new ServerErrorException("服务器[" + url + "]访问异常", e);
		}
	}
	
	/**
	 * 与浏览器兼容的方式上传文件
	 * @param url 请求地址
	 * @param params 请求参数列表
	 * @param fileParams 需要上传的文件列表
	 * @param handler 返回对象handler处理后的数据
	 * @return 返回对象handler处理后的数据
	 * @throws ServerErrorException
	 */
	public static Object post(String url, Map<String, String> params, Map<String, File> fileParams, ResponseHandler<?> handler) throws ServerErrorException {
		CloseableHttpClient client = HttpClients.createMinimal();
		MultipartEntityBuilder meb = MultipartEntityBuilder.create();
		meb.setCharset(Charset.forName("UTF-8"));
		meb.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		if (params != null) {
			for (Entry<String, String> e : params.entrySet()) {
				meb.addTextBody(e.getKey(), e.getValue(), ContentType.create("text/plain", "UTF-8"));
			}
		}
		if (fileParams != null) {
			for (Entry<String, File> e : fileParams.entrySet()) {
				meb.addBinaryBody(e.getKey(), e.getValue());
			}
		}

		HttpPost hp = new HttpPost(url);
		HttpEntity entity = meb.build();
		hp.setEntity(entity);
		try {
			return client.execute(hp, handler);
		} catch (ClientProtocolException e) {
			throw new ServerErrorException("服务器[" + url + "]连接不通", e);
		} catch (IOException e) {
			throw new ServerErrorException("服务器[" + url + "]访问异常", e);
		}
	}
}
