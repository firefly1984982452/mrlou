package com.mrlou.mine;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.ttf.FontManager;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class Job_AddAct extends Activity implements HttpListener {

	private LinearLayout lay_job_add_back, lay_job_add_time, lay_job_addtime,
			lay_job_add_update;
	private TextView tv_job_add_time, tv_job_addtime;
	private EditText et_addjob_content;
	private int year, month, day, hour, min;
	private ScrollView src_job_add_main;
	private FontManager fm = new FontManager();
	private String target_type, session_id, id;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_job_add);
		Intent intent = getIntent();
		target_type = intent.getStringExtra("target_type");
		id = intent.getStringExtra("id");
		sharedPreferences = Job_AddAct.this.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		hour = c.get(Calendar.HOUR_OF_DAY);
		min = c.get(Calendar.MINUTE);
		et_addjob_content = (EditText) findViewById(R.id.et_jobadd_content);
		tv_job_addtime = (TextView) findViewById(R.id.tv_job_addtime);
		tv_job_addtime.setText(year + "-" + (month + 1) + "-" + day);
		lay_job_addtime = (LinearLayout) findViewById(R.id.lay_job_addtime);
		lay_job_addtime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatePickerDialog datePicker = new DatePickerDialog(
						Job_AddAct.this, new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int years,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								tv_job_addtime.setText(year + "-" + (month + 1)
										+ "-" + day);
								year = years;
								month = monthOfYear;
								day = dayOfMonth;
							}
						}, year, month, day);
				datePicker.show();
			}
		});
		tv_job_add_time = (TextView) findViewById(R.id.tv_job_add_time);
		lay_job_add_time = (LinearLayout) findViewById(R.id.lay_job_add_time);
		lay_job_add_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimePickerDialog time = new TimePickerDialog(Job_AddAct.this,
						new OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								// TODO Auto-generated method stub
								tv_job_add_time.setText(year + "-"
										+ (month + 1) + "-" + day + " "
										+ hourOfDay + ":" + minute);
								hour = hourOfDay;
								min = minute;
							}
						}, hour, min, true);

				time.show();
				DatePickerDialog datePicker = new DatePickerDialog(
						Job_AddAct.this, new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int years,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								year = years;
								month = monthOfYear;
								day = dayOfMonth;
							}
						}, year, month, day);
				datePicker.show();
			}
		});
		lay_job_add_back = (LinearLayout) findViewById(R.id.lay_job_add_back);
		lay_job_add_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		lay_job_add_update = (LinearLayout) findViewById(R.id.lay_job_add_update);
		lay_job_add_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!tv_job_addtime.getText().toString().equals("")
						&& !tv_job_add_time.getText().toString().equals("")
						&& !et_addjob_content.getText().toString().equals("")) {
					select_jobadd(session_id, target_type, id, tv_job_addtime
							.getText().toString(), et_addjob_content.getText()
							.toString(), tv_job_add_time.getText().toString());
				} else {
					Toast.makeText(Job_AddAct.this, "时间给内容均不能为空，请填写好！", Toast.LENGTH_SHORT)
							.show();
				}

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
					Job_AddAct.this.finish();
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
				Toast.makeText(Job_AddAct.this, "添加成功", Toast.LENGTH_SHORT).show();
				if (target_type.equals("5")) {
					Intent intent = new Intent(Job_AddAct.this,
							Customer_DetailAct.class);
					intent.putExtra("id", id);
					startActivity(intent);
					Job_AddAct.this.finish();
				} else {
					Intent intent = new Intent(Job_AddAct.this,
							Hourse_FollowAct.class);
					intent.putExtra("id", id);
					startActivity(intent);
					Job_AddAct.this.finish();
				}
				break;
			case 2:
				Toast.makeText(Job_AddAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 33:

				break;
			case 99:
				Toast.makeText(Job_AddAct.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	public void select_jobadd(String session_id, String type, String id,
			String event_date, String content, String nofity_time) {
		Request request = Request.requestjoblogadd(session_id, type, id,
				event_date, content, nofity_time);
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
				String msg = BaseHelper.getString(json, "msg");
				if (errorid.equals("0")) {
					newHandler.sendEmptyMessage(1);
				} else if (errorid.equals("1")) {
					newHandler.sendEmptyMessage(2);
				} else if (errorid.equals("4080")) {
					Toast.makeText(Job_AddAct.this, "请选择给哪个房源添加工作日志", Toast.LENGTH_SHORT)
							.show();
				} else if (errorid.equals("4081")) {
					Toast.makeText(Job_AddAct.this, "请选择给工作日志类型", Toast.LENGTH_SHORT)
							.show();
				} else if (errorid.equals("4082")) {
					Toast.makeText(Job_AddAct.this, "工作日志内容有误", Toast.LENGTH_SHORT)
							.show();
				} else if (errorid.equals("4083")) {
					Toast.makeText(Job_AddAct.this, "工作日期格式错误", Toast.LENGTH_SHORT)
							.show();
				} else if (errorid.equals("4084")) {
					Toast.makeText(Job_AddAct.this, "工作日期不可设定为过去", Toast.LENGTH_SHORT)
							.show();
				} else if (errorid.equals("4085")) {
					Toast.makeText(Job_AddAct.this, "通知时间格式错误", Toast.LENGTH_SHORT)
							.show();
				} else if (errorid.equals("4086")) {
					Toast.makeText(Job_AddAct.this, "通知时间不可设定为过去", Toast.LENGTH_SHORT)
							.show();
				} else if (errorid.equals("4055")) {
					Toast.makeText(Job_AddAct.this, "没有找到房源或客户", Toast.LENGTH_SHORT)
							.show();
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
