package com.mrlou.economic;

import org.json.JSONException;
import org.json.JSONObject;

import com.mrlou.identify.Identify_AgentaAct;
import com.mrlou.listener.HttpListener;
import com.mrlou.mine.History_CompleteAct;
import com.mrlou.mine.New_AddInfoAct;
import com.mrlou.mrlou.R;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author jx_chen
 * @date 2016-11-29---上午10:47:50
 * @problem
 */
public class Economic_error extends Activity implements HttpListener {

	private TextView tv_name;
	private EditText et_content;
	private LinearLayout lay_commit,lay_back;
	private String name, id, session_id;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.economic_error);
		sharedPreferences = Economic_error.this.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		Intent intent = getIntent();
		name = intent.getStringExtra("name");
		id = intent.getStringExtra("id");
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		tv_name = (TextView) findViewById(R.id.tv_economic_error_name);
		tv_name.setText(name);
		et_content = (EditText) findViewById(R.id.et_economic_error_content);
		lay_commit = (LinearLayout) findViewById(R.id.lay_economic_error_commit);
		lay_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!et_content.getText().toString().equals("")) {
					select_error(session_id, name, et_content.getText()
							.toString(), "2", "3", id);
				} else {
					Toast.makeText(Economic_error.this, "请输入纠错内容",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		lay_back=(LinearLayout) findViewById(R.id.lay_economic_error_back);
		lay_back.setOnClickListener(new OnClickListener() {
			
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
					Economic_error.this.finish();
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
				Toast.makeText(Economic_error.this, "感谢您的宝贵意见，楼先生将尽快核实并修正信息",
						Toast.LENGTH_SHORT).show();
				simulateKey(KeyEvent.KEYCODE_BACK);
				break;
			case 2:
				Toast.makeText(Economic_error.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(Economic_error.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public void select_error(String session_id, String title, String content,
			String type, String report_type, String building_id) {
		Request request = Request.request_error(session_id, title, content,
				type, report_type, building_id);
		new HttpThread(request, this);
	}

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC) {
			JSONObject json;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					newHandler.sendEmptyMessage(1);
				} else {
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
