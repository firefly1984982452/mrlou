package com.mrlou.image;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class ImageUtil {
	
	static final String TAG="ImageUtil";
	
	public static Bitmap compressPic2Bitmap(String picfullname) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(picfullname, options); // 此时返回bm为空
		options.inJustDecodeBounds = false;
		int be = (int) (options.outHeight / (float) 200);
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(picfullname, options);
		return bitmap;
	}

	public static Drawable bitmap2Drawable(Bitmap bm) {
		BitmapDrawable bd = new BitmapDrawable(bm);
		return bd;
	}

	public static Drawable compressPic2Drawable(String picfullname) {
		return bitmap2Drawable(compressPic2Bitmap(picfullname));
	}

	public static Bitmap compressPic2Bitmap(Bitmap bitmap, int width, int height, boolean isAdjust) {
		if (bitmap.getWidth() < width && bitmap.getHeight() < height) {
			return bitmap;
		}
		float sx = new BigDecimal(width).divide(new BigDecimal(bitmap.getWidth()), 4, BigDecimal.ROUND_DOWN)
				.floatValue();
		float sy = new BigDecimal(height).divide(new BigDecimal(bitmap.getHeight()), 4, BigDecimal.ROUND_DOWN)
				.floatValue();
		if (isAdjust) {
			sx = (sx < sy ? sx : sy);
			sy = sx;
		}
		Matrix matrix = new Matrix();
		matrix.postScale(sx, sy);
		return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
	}

	public static Drawable compressPic2Drawable(String picfullname, int width, int height) {
		// Bitmap bitmap = compressPic2Bitmap(picfullname);
		// bitmap = compressPic2Bitmap(bitmap, width, height, true);

		Bitmap bitmap = compressBitmap(picfullname, width, height);
		return new BitmapDrawable(bitmap);
	}

	public static Bitmap compressBitmap(String path, int sdwidth, int sdheight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeFile(path, options);
		options.inSampleSize = calculateSampleSize(options, sdwidth, sdheight);
		options.inJustDecodeBounds = false;
		options.inDither = false;
		options.inPreferredConfig = Bitmap.Config.RGB_565;
		return BitmapFactory.decodeFile(path, options);
	}

	public static int calculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
	/**
	 * 有损压缩图片
	 * @param filePath
	 * @return
	 */
	public static byte[] compressBitmap(String filePath) {
		Bitmap sourceBmp=BitmapFactory.decodeFile(filePath);
		if(sourceBmp!=null)
		{
			Log.e(TAG, "原大小"+sourceBmp.getByteCount());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			sourceBmp.compress(Bitmap.CompressFormat.JPEG, 60, baos);
			sourceBmp.recycle();
			sourceBmp=null;
			//如果压缩后还大于10M,再压一次,这种情况一般不会出现
			if(baos.size()>10*1024*1024)
			{
				byte[] bytes=baos.toByteArray();
				Bitmap tempBmp=BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
				baos.reset();
				tempBmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
				tempBmp.recycle();
				tempBmp=null;
			}
			byte[] bytes=baos.toByteArray();
			Log.e(TAG, "压缩后大小"+bytes.length);
			return bytes;
		}else{
			return null;
		}
	}
	
	public static Bitmap bytes2Bimap(byte[] b) {
         if (b.length != 0) {
             return BitmapFactory.decodeByteArray(b, 0, b.length);
         } else {
             return null;
         }
     }
}
