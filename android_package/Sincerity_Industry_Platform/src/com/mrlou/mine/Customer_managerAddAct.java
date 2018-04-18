package com.mrlou.mine;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mrlou.activity.MainFragmentActivity;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.ttf.FontManager;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class Customer_managerAddAct extends Activity implements HttpListener {

	private LinearLayout lay_customer_add_back, lay_icon01, lay_icon02,
			lay_customer_add_time, lay_customer_add;
	private TextView tv_customer_add_time, tv_customer_add_unit;
	private ImageView iv_icon01, iv_icon02;
	private int year, month, day;
	private SharedPreferences sharedPreferences;
	private String session_id, transaction_type = "1";
	private EditText et_name, et_sector, et_pre_square_meter, et_pre_room,
			et_pre_price_1, et_intro;
	private SweetAlertDialog pDialog;
	private Progress_Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_customer_add);
		sharedPreferences = Customer_managerAddAct.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		// 弹出请求对话框
		pDialog = new SweetAlertDialog(Customer_managerAddAct.this,
				SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
		dialog = new Progress_Dialog(Customer_managerAddAct.this, pDialog);
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		tv_customer_add_unit = (TextView) findViewById(R.id.tv_customer_add_unit);

		tv_customer_add_time = (TextView) findViewById(R.id.tv_customer_add_time);
		lay_customer_add_time = (LinearLayout) findViewById(R.id.lay_customer_add_time);
		lay_customer_add_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatePickerDialog datePicker = new DatePickerDialog(
						Customer_managerAddAct.this, new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int years,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								tv_customer_add_time.setText(year + "-"
										+ (monthOfYear + 1) + "-" + dayOfMonth);
								year = years;
								month = monthOfYear;
								day = dayOfMonth;
							}
						}, year, month, day);
				datePicker.show();
			}
		});
		lay_customer_add = (LinearLayout) findViewById(R.id.lay_customer_add);
		lay_customer_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dialog();
				select_customeradd(session_id, et_name.getText().toString(),
						transaction_type, tv_customer_add_time.getText()
								.toString(), et_sector.getText().toString(),
						et_pre_square_meter.getText().toString(), et_pre_room
								.getText().toString(), et_pre_price_1.getText()
								.toString(), tv_customer_add_unit.getText()
								.toString(), et_intro.getText().toString());
			}
		});
		et_name = (EditText) findViewById(R.id.et_add_name);
		et_sector = (EditText) findViewById(R.id.et_add_job_title);
		et_pre_square_meter = (EditText) findViewById(R.id.et_add_area);
		et_pre_price_1 = (EditText) findViewById(R.id.et_add_price);
		et_pre_room = (EditText) findViewById(R.id.et_add_hourse);
		et_intro = (EditText) findViewById(R.id.et_add_intro);
		lay_icon01 = (LinearLayout) findViewById(R.id.lay_customer_add_icon01);
		lay_icon01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				transaction_type = "1";
				selecticon(1);
				tv_customer_add_unit.setText("元/㎡/天");
			}
		});
		lay_icon02 = (LinearLayout) findViewById(R.id.lay_customer_add_icon02);
		lay_icon02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				transaction_type = "2";
				selecticon(2);
				tv_customer_add_unit.setText("元/㎡");
			}
		});
		iv_icon01 = (ImageView) findViewById(R.id.iv_customer_add_icon01);
		iv_icon02 = (ImageView) findViewById(R.id.iv_customer_add_icon02);
		selecticon(1);
		lay_customer_add_back = (LinearLayout) findViewById(R.id.lay_customer_add_back);
		lay_customer_add_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
	}

	public void selecticon(int flag) {
		switch (flag) {
		case 1:
			iv_icon01.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_checked));
			iv_icon02.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			break;
		case 2:
			iv_icon01.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon02.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_checked));
			break;
		default:
			break;
		}
	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					Customer_managerAddAct.this.finish();
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
				dialog.dismiss();
				Toast.makeText(Customer_managerAddAct.this, "添加成功", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent(Customer_managerAddAct.this,
						Customer_ManAct.class);
				startActivity(intent);
				finish();
				break;
			case 2:
				dialog.dismiss();
				Toast.makeText(Customer_managerAddAct.this, "暂无数据", Toast.LENGTH_SHORT)
						.show();
				break;
			case 99:
				dialog.dismiss();
				Toast.makeText(Customer_managerAddAct.this, "网络异常，请重新尝试连接",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	public void select_customeradd(String session_id, String name,
			String transaction_type, String first_access_time, String sector,
			String pre_square_meter, String pre_room, String pre_price_1,
			String pre_price_unit_1, String intro) {
		Request request = Request.requestcustomeradd(session_id, name,
				transaction_type, first_access_time, sector, pre_square_meter,
				pre_room, pre_price_1, pre_price_unit_1, intro);
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
				} else if (errorid.equals("4018")) {
					Toast.makeText(Customer_managerAddAct.this, "交易类型有误", Toast.LENGTH_SHORT)
							.show();
				} else if (errorid.equals("4019")) {
					Toast.makeText(Customer_managerAddAct.this, "首次到访日期有误",
							Toast.LENGTH_SHORT).show();
				} else if (errorid.equals("4020")) {
					Toast.makeText(Customer_managerAddAct.this, "客户名称有误", Toast.LENGTH_SHORT)
							.show();
				} else if (errorid.equals("4021")) {
					Toast.makeText(Customer_managerAddAct.this, "行业类别有误", Toast.LENGTH_SHORT)
							.show();
				} else if (errorid.equals("4022")) {
					Toast.makeText(Customer_managerAddAct.this, "意向面积有误", Toast.LENGTH_SHORT)
							.show();
				} else if (errorid.equals("4023")) {
					Toast.makeText(Customer_managerAddAct.this, "意向房源有误", Toast.LENGTH_SHORT)
							.show();
				} else if (errorid.equals("4024")) {
					Toast.makeText(Customer_managerAddAct.this, "价格预算有误", Toast.LENGTH_SHORT)
							.show();
				} else if (errorid.equals("4025")) {
					Toast.makeText(Customer_managerAddAct.this, "价格预算单位有误",
							Toast.LENGTH_SHORT).show();
				} else if (errorid.equals("4026")) {
					Toast.makeText(Customer_managerAddAct.this, "客户简介有误", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(Customer_managerAddAct.this, "联系电话有误", Toast.LENGTH_SHORT)
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
