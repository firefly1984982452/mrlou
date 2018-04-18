package com.mrlou.economic.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class XCCircleRectImageView_head extends ImageView {

	private Paint paint;
	private int mSize,mRadius;
	public XCCircleRectImageView_head(Context context) {
		this(context, null);
	}

	public XCCircleRectImageView_head(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public XCCircleRectImageView_head(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		paint = new Paint();
	}

	@Override
	public void setMaxWidth(int maxWidth) {
		// TODO Auto-generated method stub
		
		super.setMaxWidth(maxWidth);
	}

	@Override
	public void setMaxHeight(int maxHeight) {
		// TODO Auto-generated method stub
		super.setMaxHeight(maxHeight);
	}

	/**
	 * 绘制圆角矩形图片
	 * 
	 * @author caizhiming
	 */
//	@Override
//	protected void onDraw(Canvas canvas) {
//
//		Drawable drawable = getDrawable();
//		if (null != drawable) {
//			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//			int width = bitmap.getWidth();
//			int height = bitmap.getHeight();
//			Bitmap b;
//			if (width < height) {
//				b = createCircleImage(bitmap, width, height, width);
//			} else {
//				b = createCircleImage(bitmap, width, height, height);
//			}
//			
//			canvas.drawBitmap(b, 0, 0, paint);
//		} else {
//			super.onDraw(canvas);
//		}
//	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    mSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
	    mRadius = mSize / 2;
	    setMeasuredDimension(mSize, mSize);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
	    //得到原bitmap
	    Bitmap src = ((BitmapDrawable) drawable).getBitmap();
	    if (src == null) {
	        super.onDraw(canvas);
	        return;
	    }
	    //把bitmap缩小为和View大小一致
	    Bitmap newBitmp = Bitmap.createScaledBitmap(src, mSize, mSize, false);
	    if (newBitmp == null) {
	        return;
	    }
	    //将缩小后的bitmap设置为画笔的shader
	    BitmapShader mBitmapShader = new BitmapShader(newBitmp, TileMode.REPEAT,
	            TileMode.REPEAT);
	    //生成用来绘图的bitmap，并在其上用画笔绘图
	    Bitmap dest = Bitmap.createBitmap(mSize, mSize, Bitmap.Config.ARGB_8888);
	    if (dest == null) {
	        return;
	    }
	    Canvas c = new Canvas(dest);
	    Paint paint = new Paint();
	    paint.setAntiAlias(true);
	    paint.setShader(mBitmapShader);
	    c.drawCircle(mRadius, mRadius, mRadius, paint);
	    //将最后生成的bitmap绘制到View的canvas上
	    canvas.drawBitmap(dest, 0, 0, paint);
	}
	/**
	 * 根据原图和变长绘制圆形图片
	 * 
	 * @param source
	 * @param min
	 * @return
	 */
	private Bitmap createCircleImage(Bitmap source, int width, int height,
			int min) {
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
