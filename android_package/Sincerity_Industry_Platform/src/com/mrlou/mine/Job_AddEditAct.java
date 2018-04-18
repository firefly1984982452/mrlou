package com.mrlou.mine;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Instrumentation;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.ETC1;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.ttf.FontManager;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

public class Job_AddEditAct extends Activity implements HttpListener {

	private LinearLayout lay_job_add_edit_back, lay_job_add_edit_time,
			lay_job_add_edittime, lay_job_addedit_update;
	private TextView tv_job_add_edit_time, tv_job_add_edittime;
	private int year, month, day, hour, min;
	private Button bt_jobadd_edit;
	private ScrollView scr_job_add_edit_main;
	private String target_type, session_id, id, target_id, time, notify_time,
			content;
	private SharedPreferences sharedPreferences;
	private EditText et_jobadd_eidt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_job_add_edit);
		Intent intent = getIntent();
		target_type = intent.getStringExtra("target_type");
		id = intent.getStringExtra("id");
		target_id = intent.getStringExtra("target_id");
		time = intent.getStringExtra("time");
		notify_time = intent.getStringExtra("notify_time");
		content = intent.getStringExtra("content");
		sharedPreferences = Job_AddEditAct.this.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		// 遍历所有子控件 添加字体
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		hour = c.get(Calendar.HOUR_OF_DAY);
		min = c.get(Calendar.MINUTE);
		et_jobadd_eidt = (EditText) findViewById(R.id.et_jobadd_edit_content);
		et_jobadd_eidt.setText(content);
		tv_job_add_edittime = (TextView) findViewById(R.id.tv_job_addedit_time);
		tv_job_add_edittime.setText(time);
		lay_job_add_edittime = (LinearLayout) findViewById(R.id.lay_job_addedit_time);
		lay_job_add_edittime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatePickerDialog datePicker = new DatePickerDialog(
						Job_AddEditAct.this, new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int years,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								tv_job_add_edittime.setText(year + "-"
										+ (month + 1) + "-" + day);
								year = years;
								month = monthOfYear;
								day = dayOfMonth;
							}
						}, year, month, day);
				datePicker.show();
			}
		});
		tv_job_add_edit_time = (TextView) findViewById(R.id.tv_job_add_edit_time);
		tv_job_add_edit_time.setText(notify_time);
		lay_job_add_edit_time = (LinearLayout) findViewById(R.id.lay_job_add_edit_time);
		lay_job_add_edit_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				TimePickerDialog time = new TimePickerDialog(
						Job_AddEditAct.this, new OnTimeSetListener() {

							@Override
							public void onTimeSet(TimePicker view,
									int hourOfDay, int minute) {
								// TODO Auto-generated method stub
								tv_job_add_edit_time.setText(year + "-"
										+ (month + 1) + "-" + day + " "
										+ hourOfDay + ":" + minute);
								hour = hourOfDay;
								min = minute;
							}
						}, hour, min, true);

				time.show();
				DatePickerDialog datePicker = new DatePickerDialog(
						Job_AddEditAct.this, new OnDateSetListener() {

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
		lay_job_add_edit_back = (LinearLayout) findViewById(R.id.lay_job_add_edit_back);
		lay_job_add_edit_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		bt_jobadd_edit = (Button) findViewById(R.id.bt_jobadd_edit);
		bt_jobadd_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				select_jobdelete(session_id, id);
				System.out.println("---"+id);
			}
		});
		lay_job_addedit_update = (LinearLayout) findViewById(R.id.lay_job_addedit_update);
		lay_job_addedit_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!et_jobadd_eidt.getText().toString().equals("")
						&& !tv_job_add_edit_time.getText().toString()
								.equals("")) {
					select_jobadd(session_id, id, target_type, target_id,
							tv_job_add_edittime.getText().toString(),
							et_jobadd_eidt.getText().toString(),
							tv_job_add_edit_time.getText().toString());
				} else {
					Toast.makeText(Job_AddEditAct.this, "修改的内容跟时间不能为空", Toast.LENGTH_SHORT)
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
					Job_AddEditAct.this.finish();
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
				Toast.makeText(Job_AddEditAct.this, "修改成功", Toast.LENGTH_SHORT).show();
				if (target_type.equals("5")) {
					Intent intent = new Intent(Job_AddEditAct.this,
							Customer_DetailAct.class);
					intent.putExtra("id", target_id);
					startActivity(intent);
					Job_AddEditAct.this.finish();
				} else {
					Intent intent = new Intent(Job_AddEditAct.this,
							Hourse_FollowAct.class);
					intent.putExtra("id", target_id);
					startActivity(intent);
					Job_AddEditAct.this.finish();
				}
				break;
			case 2:
				Toast.makeText(Job_AddEditAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(Job_AddEditAct.this, "删除成功", Toast.LENGTH_SHORT).show();
				if (target_type.equals("5")) {
					Intent intent = new Intent(Job_AddEditAct.this,
							Customer_DetailAct.class);
					intent.putExtra("id", target_id);
					startActivity(intent);
					Job_AddEditAct.this.finish();
				} else {
					Intent intent = new Intent(Job_AddEditAct.this,
							Hourse_FollowAct.class);
					intent.putExtra("id", target_id);
					startActivity(intent);
					Job_AddEditAct.this.finish();
				}
				break;
			case 99:
				Toast.makeText(Job_AddEditAct.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public void select_jobadd(String session_id, String ids, String type,
			String id, String event_date, String content, String nofity_time) {
		Request request = Request.requestjoblogedit(session_id, ids, type, id,
				event_date, content, nofity_time);
		new HttpThread(request, this);
	}

	public void select_jobdelete(String session_id, String id) {
		Request request = Request.requestjobdelete(session_id, id);
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
				} else if (errorid.equals("4087")) {
					Toast.makeText(Job_AddEditAct.this, "工作日志未找到", Toast.LENGTH_SHORT).show();
				} else if (errorid.equals("4089")) {
					Toast.makeText(Job_AddEditAct.this, "工作日志未找到", Toast.LENGTH_SHORT).show();
				} else if (errorid.equals("4090")) {
					Toast.makeText(Job_AddEditAct.this, "输入内容有误", Toast.LENGTH_SHORT).show();
				} else if (errorid.equals("4091")) {
					Toast.makeText(Job_AddEditAct.this, "输入内容有误", Toast.LENGTH_SHORT).show();
				} else if (errorid.equals("4092")) {
					Toast.makeText(Job_AddEditAct.this, "输入内容有误", Toast.LENGTH_SHORT).show();
				} else if (errorid.equals("4093")) {
					Toast.makeText(Job_AddEditAct.this, "输入内容有误", Toast.LENGTH_SHORT).show();
				} else if (errorid.equals("4094")) {
					Toast.makeText(Job_AddEditAct.this, "输入内容有误", Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (reqID == Request.REQUEST_ECONOMIC_COMPANY) {
			JSONObject json;
			String errorid;
			try {
				json = new JSONObject(b);
				errorid = BaseHelper.getString(json, "errorid");
				String msg = BaseHelper.getString(json, "msg");
				if (errorid.equals("0")) {
					newHandler.sendEmptyMessage(3);
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
}
