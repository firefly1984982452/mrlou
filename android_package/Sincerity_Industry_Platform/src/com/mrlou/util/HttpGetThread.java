package com.mrlou.util;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

import com.lidroid.xutils.http.client.util.URLEncodedUtils;
import com.mrlou.listener.HttpListener;

public class HttpGetThread extends Thread {

	private Request request;
	private HttpListener listener;

	public HttpGetThread(Request request, HttpListener listener) {
		this.request = request;
		this.listener = listener;
		DownLoadTask task = new DownLoadTask();
		task.execute();
	}

	public void run() {
		try {
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
			HttpConnectionParams.setSoTimeout(httpParams, 30000);
			HttpClient httpclient = new DefaultHttpClient(httpParams);
			
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
			params.add(new BasicNameValuePair("session_id", request.nameValuePairs.get(0).getValue()));  
			params.add(new BasicNameValuePair("user_id", request.nameValuePairs.get(1).getValue()));
			String param = URLEncodedUtils.format(params, "UTF-8"); 

			HttpGet httpGet = new HttpGet(request.url+"?"+param);
			
			
//			httpGet.setHeader("session_id", request.nameValuePairs.get(0).getValue());
//			httpGet.setHeader("user_id", request.nameValuePairs.get(1).getValue());
//			System.out.println(request.nameValuePairs.get(0).getValue()
//					+ "==httpclient1=="
//					+ request.nameValuePairs.get(1).getValue());
			
			
			// URL realUrl = new URL(request.url);
			// // 打开和URL之间的连接
			// URLConnection connection = realUrl.openConnection();
			// // 设置通用的请求属性
			// connection.setRequestProperty("session_id",
			// request.nameValuePairs.get(0).toString());
			// connection.setRequestProperty("user_id",
			// request.nameValuePairs.get(1).toString());
			// // 建立实际的连接
			// connection.connect();

			// HttpPost httppost = new HttpPost(request.url);
			// if (request.nameValuePairs != null) {

			// httpGet.s
			// httppost.setEntity(new UrlEncodedFormEntity(
			// request.nameValuePairs, HTTP.UTF_8));
			// }

			HttpResponse response = httpclient.execute(httpGet);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				String rev = EntityUtils.toString(response.getEntity());
				listener.doResponse(request.reqID, rev);
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	class DownLoadTask extends AsyncTask<String, Void, String> {

		public DownLoadTask() {

		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... url) {
			asyncTask();
			return "1";
		}

		@Override
		protected void onPostExecute(String result) {

		}

		@Override
		protected void onCancelled() {

		}
	}

	private void asyncTask() {
		try {
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 10000);// 连接超时
			HttpConnectionParams.setSoTimeout(httpParams, 30000);// socket超时
			HttpClient httpclient = new DefaultHttpClient(httpParams);
			
			List<BasicNameValuePair> params = new LinkedList<BasicNameValuePair>();  
			params.add(new BasicNameValuePair("session_id", request.nameValuePairs.get(0).getValue()));  
			params.add(new BasicNameValuePair("user_id", request.nameValuePairs.get(1).getValue()));
			String param = URLEncodedUtils.format(params, "UTF-8"); 

			HttpGet httpGet = new HttpGet(request.url+"?"+param);
//			HttpGet httpGet = new HttpGet(request.url);
//
//			httpGet.setHeader("session_id", request.nameValuePairs.get(0).getValue());
//			httpGet.setHeader("user_id", request.nameValuePairs.get(1).getValue());
//			
//			System.out.println(request.nameValuePairs.get(0).getValue()
//					+ "==httpclient2=="
//					+ request.nameValuePairs.get(1).getValue());
			HttpResponse response = httpclient.execute(httpGet);
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {// 请求响应成功
				String rev = EntityUtils.toString(response.getEntity());
				listener.doResponse(request.reqID, rev);
				System.out.println(rev);
			} else {
				listener.doError("网络连接故障!" + code);
			}
		} catch (Exception e) {
			e.printStackTrace();
			listener.doError("请求异常");
		}
	}
}
