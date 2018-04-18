package com.mrlou.view;

import android.content.Context;

import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.mrlou.mrlou.R;

public class Xtextview extends TextView{

	private String customFont;
	public Xtextview(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public Xtextview(Context context, AttributeSet attrs){
		super(context, attrs);
        setCustomFont(context, attrs);
	}
	public Xtextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }
	private void setCustomFont(Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Xtextview);
        customFont = a.getString(R.styleable.Xtextview_customFont);
        setCustomFont(context, customFont);
        a.recycle();
	}
	 public boolean setCustomFont(Context ctx, String asset) {
	        Typeface tf = null;
	        try {
	        tf = Typeface.createFromAsset(ctx.getAssets(), asset);  
	        } catch (Exception e) {
	           // Log.e(TAG, "Could not get typeface: "+e.getMessage());
	            return false;
	        }
	        setTypeface(tf);  
	        return true;
	    }
}
