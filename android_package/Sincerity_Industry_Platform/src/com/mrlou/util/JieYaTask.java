package com.mrlou.util;

import com.mrlou.activity.MainFragmentActivity;

import android.content.Context;
import android.os.AsyncTask;


public class JieYaTask extends AsyncTask<String , Integer,Integer>{

	private String from,traget;
	private int result;
	private Context context;
	
	public JieYaTask(String from,String traget,Context context){
		this.from =from;
		this.traget = traget;
		this.context = context;
		
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	
	
	
	
	@Override
	protected Integer doInBackground(String... params) {
	
		Unzip unzip = new Unzip();
		try {
			result = unzip.Unzipmethod(from,traget);
		} catch (Exception e) {
			
		}
		
		return result;
	}

	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Integer result) {
	
		 ((MainFragmentActivity)context).dozip(this.result); 
	}
	
}
