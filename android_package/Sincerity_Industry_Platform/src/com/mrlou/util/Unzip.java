package com.mrlou.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import android.util.Log;

public class Unzip {
	
	public int  Unzipmethod(String compath, String folderPath)throws ZipException,IOException{
		 File file = new File(compath);
		 File file2 = new File(folderPath);
		 if (!file2.exists()) {
			file2.mkdirs();
		}
		 ZipFile zfile=new ZipFile(file);
			Enumeration<?> zList=zfile.entries();
			ZipEntry ze=null;
			byte[] buf=new byte[1024];
			while(zList.hasMoreElements()){
				ze=(ZipEntry)zList.nextElement();    
				if(ze.isDirectory()){
					Log.d("upZipFile", "ze.getName() = "+ze.getName());
					String dirstr = folderPath + ze.getName();
					dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
					Log.d("upZipFile", "str = "+dirstr);
					File f=new File(dirstr);
					f.mkdir();
					continue;
				}
				Log.d("upZipFile", "ze.getName() = "+ze.getName());
				OutputStream os=new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
				InputStream is=new BufferedInputStream(zfile.getInputStream(ze));
				int readLen=0;
				while ((readLen=is.read(buf, 0, 1024))!=-1) {
					os.write(buf, 0, readLen);
				}
				is.close();
				os.close();    
			}
			zfile.close();
			Log.d("upZipFile", "finish");
			return 7;
	 }

	public static File getRealFileName(String baseDir, String absFileName){
		String[] dirs=absFileName.split("/");
		File ret=new File(baseDir);
		String substr = null;
		if(dirs.length>1){
			for (int i = 0; i < dirs.length-1;i++) {
				substr = dirs[i];
				try {
					substr = new String(substr.getBytes("8859_1"), "GB2312");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				ret=new File(ret, substr);

			}
			Log.d("upZipFile", "1ret = "+ret);
			if(!ret.exists())
				ret.mkdirs();
			substr = dirs[dirs.length-1];
			try {
				//substr.trim();
				substr = new String(substr.getBytes("8859_1"), "GB2312");
				Log.d("upZipFile", "substr = "+substr);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ret=new File(ret, substr);
			Log.d("upZipFile", "2ret = "+ret);
			return ret;
		}else {
			return new File(baseDir,absFileName);	
		}
		
		
	}
	
}
