package com.mrlou.mine;

import org.json.JSONException;
import org.json.JSONObject;

import com.mrlou.identify.Idenfity_registerAct;
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
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author jx_chen
 * @date 2016-11-3---下午2:32:05
 * @problem
 */
public class FeedBack_Act extends Activity implements HttpListener {

	private EditText et_content;
	private LinearLayout lay_back;
	private Button bt_comfirm;
	private SharedPreferences sharedPreferences;
	private String session_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_feedback);
		sharedPreferences = FeedBack_Act.this.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		et_content = (EditText) findViewById(R.id.et_feedback_content);
		lay_back = (LinearLayout) findViewById(R.id.lay_feedback_back);
		lay_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		bt_comfirm = (Button) findViewById(R.id.bt_feedback_comfirm);
		bt_comfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!et_content.getText().toString().equals("")) {
					select_feedback(session_id, et_content.getText().toString());
				} else {
					Toast.makeText(FeedBack_Act.this, "未填写反馈内容，请填写后再提交",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	public void select_feedback(String session_id, String content) {
		Request request = Request.request_feedback(session_id, content);
		new HttpThread(request, this);
	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				simulateKey(KeyEvent.KEYCODE_BACK);
				Toast.makeText(FeedBack_Act.this, "提交成功", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(FeedBack_Act.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(FeedBack_Act.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT).show();
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
				} else if (errorid.equals("1")) {
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

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					FeedBack_Act.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

}
