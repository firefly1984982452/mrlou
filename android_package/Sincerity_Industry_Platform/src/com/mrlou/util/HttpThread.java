package com.mrlou.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.widget.Toast;

import com.mrlou.activity.MainFragmentActivity;
import com.mrlou.listener.HttpListener;

public class HttpThread extends Thread {

	private Request request;
	private HttpListener listener;

	public HttpThread(Request request, HttpListener listener) {
		this.request = request;
		this.listener = listener;
		DownLoadTask task = new DownLoadTask();
		task.execute();
	}

	
	public void run() {
		try {
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
			HttpConnectionParams.setSoTimeout(httpParams, 10000);
			HttpClient httpclient = new DefaultHttpClient(httpParams);
			HttpPost httppost = new HttpPost(request.url);
			if (request.nameValuePairs != null) {
				httppost.setEntity(new UrlEncodedFormEntity(
						request.nameValuePairs, HTTP.UTF_8));
			}

			HttpResponse response = httpclient.execute(httppost);
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
	
	private void asyncTask(){
		try {
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 10000);// 连接超时
			HttpConnectionParams.setSoTimeout(httpParams, 10000);// socket超时
			HttpClient httpclient = new DefaultHttpClient(httpParams);
			HttpPost httppost = new HttpPost(request.url);

			if (request.nameValuePairs != null) {
				httppost.setEntity(new UrlEncodedFormEntity(
						request.nameValuePairs, HTTP.UTF_8));
			}
			HttpResponse response = httpclient.execute(httppost);
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
