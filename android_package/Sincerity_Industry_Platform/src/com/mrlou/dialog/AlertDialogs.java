package com.mrlou.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.mrlou.mine.Elite_meetingAct;
import com.mrlou.mrlou.R;
import com.mrlou.util.User_info;

/**
 * @author jx_chen
 * @date 2016-12-29---下午4:53:51
 * @problem
 */
public class AlertDialogs {

	private Button rightButton, leftbButton;
	private TextView textView;
	public AlertDialog aDialog;
	private Context mcontext;

	public void alertDialogs(Context context, String title, String btString2) {
		final View view;
		view = LayoutInflater.from(context)
				.inflate(R.layout.dialog_login, null);
		mcontext = context;
		leftbButton = (Button) view.findViewById(R.id.bt_cancel);
		leftbButton.setText("取消");
		leftbButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				User_info.setAdd_v("1");
				aDialog.dismiss();
			}
		});
		rightButton = (Button) view.findViewById(R.id.bt_confirm);
		rightButton.setText(btString2);
		textView = (TextView) view.findViewById(R.id.d_title);
		textView.setText(title);
		if (aDialog != null && aDialog.isShowing()) {
			aDialog.dismiss();
		}
		aDialog = new AlertDialog.Builder(context).create();
		aDialog.setCanceledOnTouchOutside(false);
		WindowManager.LayoutParams params = aDialog.getWindow().getAttributes();
		params.gravity = Gravity.CENTER;
		
		params.width = 200;
		params.height = 200 ;
		aDialog.getWindow().setAttributes(params);
		
//		WindowManager m = ((Activity) mcontext).getWindowManager();
//		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//		WindowManager.LayoutParams p = aDialog.getWindow().getAttributes();
//		// 获取对话框当前的参数值
//		p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
//		aDialog.getWindow().setAttributes(p);

		//aDialog.getWindow().setAttributes(params);
		// Window dialogWindow = aDialog.getWindow();
		// WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		// lp.width = 290;
		// dialogWindow.setAttributes(lp);
		try {
			aDialog.show();
		} catch (Exception e) {
		}

		aDialog.getWindow().setContentView(view);
		rightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mcontext, Elite_meetingAct.class);
				mcontext.startActivity(intent);
				User_info.setAdd_v("1");
				aDialog.dismiss();
			}
		});
		aDialog.setCancelable(true);
	}

}
