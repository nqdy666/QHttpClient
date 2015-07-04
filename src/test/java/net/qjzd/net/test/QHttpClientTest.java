package net.qjzd.net.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.qjzd.httpclient.FileResponseHandler;
import net.qjzd.httpclient.QHttpClient;
import net.qjzd.httpclient.JsonResponseHandler;
import net.qjzd.httpclient.ServerErrorException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class QHttpClientTest {
	
	//http地址, 测试时, 请修改
	public static final String HTTP_URL = "http://127.0.0.1:8080/";
	
	/**
	 * 请求json数据
	 * @return
	 */
	public JSONObject post() {
		String url = HTTP_URL;
		Map<String, String> params = new HashMap<String, String>();
		params.put("param1_key", "param1"); //请求参数, 测试时, 请修改
		params.put("param2_key", "param2");
		JSONObject httpRst = null;
		try {
			httpRst = (JSONObject)QHttpClient.post(url, params, new JsonResponseHandler());
		} catch (ServerErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(JSON.toJSON(httpRst));
		return httpRst;
	}
	
	/**
	 * 下载文件
	 */
	public void downloadFile() {
		String url = HTTP_URL;
		Map<String, String> params = new HashMap<String, String>();
		params.put("param1_key", "param1"); //请求参数, 测试时, 请修改
		params.put("param2_key", "param2");
		
		String destFilePath = ""; //下载文件存放的位置, 测试时, 请修改
		File destfile = new File(destFilePath);
		try {
			QHttpClient.post(url, params, new FileResponseHandler(destfile));
		} catch (ServerErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JSONObject postFile() {
		String url = HTTP_URL;
		Map<String, String> params = new HashMap<String, String>();
		params.put("param1_key", "param1"); //请求参数, 测试时, 请修改
		params.put("param2_key", "param2");
		
		Map<String, File> fileParams = new HashMap<String, File>();
		
		String uploadFilePath = ""; //待上传文件的路径, 测试时, 请修改
		File uploadFile = new File(uploadFilePath);
		fileParams.put("file", uploadFile);
		
		JSONObject rstJson = null;
		try {
			rstJson = (JSONObject) QHttpClient.post(url, params, fileParams, new JsonResponseHandler());
		} catch (ServerErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(JSON.toJSONString(rstJson));
		return rstJson;
	}
	
	
	public static void main(String[] args) {
		QHttpClientTest client = new QHttpClientTest();
		client.post();
		client.downloadFile();
		client.postFile();
	}
}
