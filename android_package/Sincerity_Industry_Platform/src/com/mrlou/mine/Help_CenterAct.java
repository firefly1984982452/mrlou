package com.mrlou.mine;

import org.json.JSONException;
import org.json.JSONObject;

import com.mrlou.h5.Other_H5Act;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.ttf.FontManager;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Help_CenterAct extends Activity implements HttpListener {

	private LinearLayout lay_help_center_back,lay_mine_help_new,lay_help_function,lay_help_idenfity;
	private String content,session_id,user_type;
	private SharedPreferences sharedPreferences;
	private TextView tv_type;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_help_center);
		sharedPreferences = Help_CenterAct.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		user_type = sharedPreferences.getString("type_id", "");
//		select_jobadd("1");
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_mine_help_new=(LinearLayout) findViewById(R.id.lay_mine_help_new);
		lay_mine_help_new.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Help_CenterAct.this,Other_H5Act.class);
				intent.putExtra("url", "mine_help_reg.html");
				startActivity(intent);
			}
		});
		lay_help_function=(LinearLayout) findViewById(R.id.lay_help_function);
		lay_help_function.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Help_CenterAct.this,Other_H5Act.class);
				intent.putExtra("url", "mine_help_func.html");
				startActivity(intent);
			}
		});
		tv_type=(TextView) findViewById(R.id.tv_help_center_usertype);
		if (user_type.equals("299")) {
			tv_type.setText("客户使用攻略");
		} else if (user_type.equals("102")) {
			tv_type.setText("经纪人攻略");
		} else if (user_type.equals("104")) {
			tv_type.setText("企业服务商攻略");
		} else {
			tv_type.setText("业主方攻略");
		}
		lay_help_idenfity=(LinearLayout) findViewById(R.id.lay_help_idenfity);
		lay_help_idenfity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Help_CenterAct.this,Other_H5Act.class);
				if (user_type.equals("102")) {
					intent.putExtra("url", "mine_help_agent.html");
				}else if (user_type.equals("104")) {
					intent.putExtra("url", "mine_help_server.html");
				}else if (user_type.equals("299")) {
					intent.putExtra("url", "mine_help_other.html");
				}else {
					intent.putExtra("url", "mine_help_owner.html");
				}
				startActivity(intent);
			}
		});
		lay_help_center_back = (LinearLayout) findViewById(R.id.lay_help_center_back);
		lay_help_center_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					Help_CenterAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
//				findView();
//				tv_help_center.setText(content);
				break;
			case 2:
				Toast.makeText(Help_CenterAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 33:

				break;
			case 99:
				Toast.makeText(Help_CenterAct.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public void select_jobadd( String type) {
		Request request = Request.requestabout(session_id,type);
		new HttpThread(request, this);
	}

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC) {
			JSONObject json,json2;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				String msg = BaseHelper.getString(json, "msg");
				if (errorid.equals("0")) {
					json2=json.getJSONObject("list");
					content=json2.getString("content");
					newHandler.sendEmptyMessage(1);
				} else if (errorid.equals("1")) {
					newHandler.sendEmptyMessage(2);
				} else {
					System.out.println("" + msg);
					// Toast.makeText(Help_CenterAct.this, "修改错误，请认真对过之后再重新提交",
					// Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void doError(String s) {
		// TODO Auto-generated method stub
		if (s.equals("请求异常")) {
			newHandler.sendEmptyMessage(99);
		}
	}
}
