package com.mrlou.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.mrlou.activity.MainFragmentActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class CopyTask extends AsyncTask<String, Integer, String> {

	private String mname,mtraget,copyresult;
	private Context mcontext;
	
	
	public 	CopyTask (Context context,String name,String traget ){
		mcontext = context;
		mname = name;
		mtraget = traget;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		copyresult = copyFileFromAssets(mcontext, mname, mtraget);
		
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
//		super.onPostExecute(result);
		((MainFragmentActivity)mcontext).docopy(copyresult,mname); 
	}
	
	
	//移动字体文件
		public  String  copyFileFromAssets(Context context, String assetsFilePath, String targetFileFullPath) {  
			String result="";
			
			InputStream assestsFileImputStream;  
			OutputStream out = null;
			try {  
				assestsFileImputStream = context.getAssets().open(assetsFilePath); 
				try {
					File outFile = new File(targetFileFullPath, assetsFilePath);
					if (outFile.exists()) {
						outFile.delete();
					}
					outFile.createNewFile();
					out = new FileOutputStream(outFile);
					byte[] buffer = new byte[1024];
					int read;
					while((read = assestsFileImputStream.read(buffer)) != -1){
						out.write(buffer, 0, read);
					}
					assestsFileImputStream.close();
					out.flush();
					out.close();
					result="success";
				} catch(IOException e) {
					Log.e("tag", "Failed to copy asset file: " +assetsFilePath , e);
					result="failed";
				}       
			} catch (IOException e) {  
				Log.d("tag", "copyFileFromAssets " + "IOException-" + e.getMessage());  
				e.printStackTrace();  
				result="failed";
			}  
			
			return result;
		}
	
}
