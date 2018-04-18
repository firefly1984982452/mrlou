package com.mrlou.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ImageVIew_Text {

	ImageView img;  
    private Bitmap imgMarker;  
    private int width,height;   //图片的高度和宽带  
    private Bitmap imgTemp;  //临时标记图  
	private Context mContext;
    
    public ImageVIew_Text(Context context,int w,int h){
    	this.mContext=context;
    	this.width=w;
    	this.height=h;
    }
    
	 // 穿件带字母的标记图片  
    private Drawable createDrawable(String letter) {  
        imgTemp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);  
        Canvas canvas = new Canvas(imgTemp);  
        Paint paint = new Paint(); // 建立画笔  
        paint.setDither(true);  
        paint.setFilterBitmap(true);  
        Rect src = new Rect(0, 0, width, height);  
        Rect dst = new Rect(0, 0, width, height);  
        canvas.drawBitmap(imgMarker, src, dst, paint);  
  
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG  
                | Paint.DEV_KERN_TEXT_FLAG);  
        textPaint.setTextSize(20.0f);  
        textPaint.setTypeface(Typeface.DEFAULT_BOLD); // 采用默认的宽度  
        textPaint.setColor(Color.WHITE);  
  
        canvas.drawText(String.valueOf(letter), width /2-5, height/2+5,  
                textPaint);  
        canvas.save(Canvas.ALL_SAVE_FLAG);  
        canvas.restore();  
        return (Drawable) new BitmapDrawable(mContext.getResources(), imgTemp);  
    }  
	
}
