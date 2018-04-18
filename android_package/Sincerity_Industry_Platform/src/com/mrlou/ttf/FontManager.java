package com.mrlou.ttf;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FontManager {
	
	private static Typeface tf;
	public static void changeFonts(ViewGroup root, Activity act) {
		if (tf!=null) {
			
		}else {
			tf = Typeface.createFromAsset(act.getAssets(),
					"SourceHanSansCN-Normal.ttf");
		}
		
		for (int i = 0; i < root.getChildCount(); i++) {
			View v = root.getChildAt(i);
			if (v instanceof TextView) {
				((TextView) v).setTypeface(tf);
			} else 
//				if (v instanceof Button) {
//				((Button) v).setTypeface(tf);
//			} else 
				if (v instanceof EditText) {
				((EditText) v).setTypeface(tf);
			} else if (v instanceof ViewGroup) {
				changeFonts((ViewGroup) v, act);
			}
		}
	}
}
