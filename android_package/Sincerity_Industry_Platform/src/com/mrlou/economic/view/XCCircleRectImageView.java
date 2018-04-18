package com.mrlou.economic.view;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class XCCircleRectImageView extends ImageView {

	private Paint paint;

	public XCCircleRectImageView(Context context) {
		this(context, null);
	}

	public XCCircleRectImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public XCCircleRectImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		paint = new Paint();
	}

	/**
	 * 绘制圆角矩形图片
	 * 
	 * @author caizhiming
	 */
	@Override
	protected void onDraw(Canvas canvas) {

		Drawable drawable = getDrawable();
		if (null != drawable) {
			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			int  width =bitmap.getWidth();
			int height=bitmap.getHeight();
			Bitmap b;
			if (width<height) {
				b=createCircleImage(bitmap, width,height,width);
			}else {
				b=createCircleImage(bitmap, width,height,height);
			}
			canvas.drawBitmap(b, 0, 0, paint);
		} else {
			super.onDraw(canvas);
		}
	}

    /** 
     * 根据原图和变长绘制圆形图片 
     *  
     * @param source 
     * @param min 
     * @return 
     */  
    private Bitmap createCircleImage(Bitmap source, int width,int height,int min)  
    {  
        final Paint paint = new Paint();  
        paint.setAntiAlias(true);  
        Bitmap target = Bitmap.createBitmap(width, height, Config.ARGB_8888);  
        /** 
         * 产生一个同样大小的画布 
         */  
        Canvas canvas = new Canvas(target);  
        /** 
         * 首先绘制圆形 
         */  
        canvas.drawCircle(width / 2, height / 2, min / 2, paint);  
        /** 
         * 使用SRC_IN，参考上面的说明 
         */  
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));  
        /** 
         * 绘制图片 
         */  
        canvas.drawBitmap(source, 0, 0, paint);  
        return target;  
    }  
}
