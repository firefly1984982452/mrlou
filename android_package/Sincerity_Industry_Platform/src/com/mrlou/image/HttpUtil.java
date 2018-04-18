/**
 * 
 */
package com.mrlou.image;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.util.Log;

/**
 * @author jiashuai.xujs@alibaba-inc.com 2014-5-7 上午10:15:57
 *
 */
public class HttpUtil {

	public static Bitmap download(String path){
		try{
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			InputStream in = conn.getInputStream();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int len = 0;
			byte buff[] = new byte[1024];
			while((len = in.read(buff)) > 0){
				out.write(buff, 0 ,len);
			}
			in.close();
			out.close();
			Log.e("test", "download:"+path);
			byte image[] = out.toByteArray();
			return ImageUtil.bytes2Bimap(image);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
