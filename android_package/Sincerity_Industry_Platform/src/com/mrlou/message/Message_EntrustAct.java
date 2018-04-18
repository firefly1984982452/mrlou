package com.mrlou.message;

import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.mapcore.util.bt;
import com.mrlou.addservices.AddServices_HistoryAct;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author jx_chen
 * @date 2016-11-11---下午4:32:34
 * @problem
 */
public class Message_EntrustAct extends Activity implements HttpListener {

	private LinearLayout lay_back, lay_icon01, lay_icon02, lay_icon03;
	private ImageView iv_icon01, iv_icon02, iv_icon03;
	private EditText et_phone, et_content,et_name;
	private Button bt_comfirm;
	private String session_id, type;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_entrust);
		sharedPreferences = Message_EntrustAct.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_back = (LinearLayout) findViewById(R.id.lay_entrust_back);
		lay_icon01 = (LinearLayout) findViewById(R.id.lay_entrust_icon01);
		lay_icon01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				type = "1";
				iv_icon01.setBackgroundDrawable(Message_EntrustAct.this
						.getResources().getDrawable(
								R.drawable.resourse_images_checked));
				iv_icon02.setBackgroundDrawable(Message_EntrustAct.this
						.getResources().getDrawable(
								R.drawable.resourse_images_uncheck));
				iv_icon03.setBackgroundDrawable(Message_EntrustAct.this
						.getResources().getDrawable(
								R.drawable.resourse_images_uncheck));
			}
		});
		lay_icon02 = (LinearLayout) findViewById(R.id.lay_entrust_icon02);
		lay_icon02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				type = "2";
				iv_icon01.setBackgroundDrawable(Message_EntrustAct.this
						.getResources().getDrawable(
								R.drawable.resourse_images_uncheck));
				iv_icon02.setBackgroundDrawable(Message_EntrustAct.this
						.getResources().getDrawable(
								R.drawable.resourse_images_checked));
				iv_icon03.setBackgroundDrawable(Message_EntrustAct.this
						.getResources().getDrawable(
								R.drawable.resourse_images_uncheck));
			}
		});
		lay_icon03 = (LinearLayout) findViewById(R.id.lay_entrust_icon03);
		lay_icon03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				type = "3";
				iv_icon01.setBackgroundDrawable(Message_EntrustAct.this
						.getResources().getDrawable(
								R.drawable.resourse_images_uncheck));
				iv_icon02.setBackgroundDrawable(Message_EntrustAct.this
						.getResources().getDrawable(
								R.drawable.resourse_images_uncheck));
				iv_icon03.setBackgroundDrawable(Message_EntrustAct.this
						.getResources().getDrawable(
								R.drawable.resourse_images_checked));
			}
		});
		iv_icon01 = (ImageView) findViewById(R.id.iv_entrust_icon01);
		iv_icon02 = (ImageView) findViewById(R.id.iv_entrust_icon02);
		iv_icon03 = (ImageView) findViewById(R.id.iv_entrust_icon03);
		lay_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		et_phone = (EditText) findViewById(R.id.et_entrust_phone);
		et_content = (EditText) findViewById(R.id.et_entrust_content);
		et_name=(EditText) findViewById(R.id.et_entrust_name);
		bt_comfirm = (Button) findViewById(R.id.bt_entrust_comfirm);
		bt_comfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!et_content.getText().toString().equals("")) {
					if (!et_phone.getText().toString().equals("")) {
						if (!et_name.getText().toString().equals("")) {
							select_entrust(session_id, type, et_content.getText()
									.toString(), et_phone.getText().toString(),et_name.getText().toString());
						}else {
							Toast.makeText(Message_EntrustAct.this, "委托人名字未填写", Toast.LENGTH_SHORT).show();
						}
					}else {
						Toast.makeText(Message_EntrustAct.this, "电话号码未填写", Toast.LENGTH_SHORT).show();
					}
				}else {
					Toast.makeText(Message_EntrustAct.this, "房源详情未填写", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void select_entrust(String session_id, String type, String content,
			String phone,String name) {
		Request request = Request.request_entrust(session_id, type, content,
				phone,name);
		new HttpThread(request, this);
	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					Message_EntrustAct.this.finish();
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
				Toast.makeText(Message_EntrustAct.this, "提交成功", Toast.LENGTH_SHORT).show();
				simulateKey(KeyEvent.KEYCODE_BACK);
				break;
			case 2:
				Toast.makeText(Message_EntrustAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(Message_EntrustAct.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};
	
	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_NEW) {
			JSONObject json;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					newHandler.sendEmptyMessage(1);
				} else{
					newHandler.sendEmptyMessage(2);
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
