package com.mrlou.economic_service;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mrlou.economic.Economic_mainAdapter;
import com.mrlou.economic.Economic_personalAct;
import com.mrlou.h5.Map_ClickAct;
import com.mrlou.listener.HttpListener;
import com.mrlou.mine.Elite_meetingAct;
import com.mrlou.mine.My_HourseAct;
import com.mrlou.mine.New_AddInfoAct;
import com.mrlou.mine.Other_SettingAct;
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpGetThread;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.mrlou.yi.Yi_Activity;

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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author jx_chen
 * @date 2016-11-18---下午2:04:59
 * @problem
 */
public class Economic_ServiceAct extends Activity implements HttpListener {

	private LinearLayout lay_economic_service_back;
	private ListView lv_economic_service;
	private TextView tv_economic_service;
	private Economic_mainAdapter madapter;
	private ArrayList<String> str_avatar, str_name, str_phone, str_company,
			str_work_age, str_all_total, str_region_total, str_company_status,
			str_rname, str_id;
	private SweetAlertDialog pDialog;
	private Progress_Dialog dialog;
	private ImageView iv_economic_add;
	private String total, add_v, session_id, user_type;
	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ecomomic_service);
		sharedPreferences = Economic_ServiceAct.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		user_type = sharedPreferences.getString("type_id", "");
		prepare();
		dialog.dialog();
		select_economicservice(session_id);
		// findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_economic_service_back = (LinearLayout) findViewById(R.id.lay_economic_service_back);
		lay_economic_service_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		tv_economic_service = (TextView) findViewById(R.id.tv_economic_service);
		tv_economic_service.setText("楼先生注册经纪人" + total
				+ "人，按区域查找资深经纪服务请选择5年+经纪！");
		lv_economic_service = (ListView) findViewById(R.id.lv_economic_service);
		madapter = new Economic_mainAdapter(Economic_ServiceAct.this,
				str_avatar, str_name, str_phone, str_company, str_work_age,
				str_all_total, str_region_total, str_company_status, str_rname,
				"2");
		lv_economic_service.setAdapter(madapter);
		lv_economic_service.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Economic_ServiceAct.this,
						Economic_personalAct.class);
				intent.putExtra("id", str_id.get(position));
				startActivity(intent);
			}
		});
		iv_economic_add = (ImageView) findViewById(R.id.iv_economic_service_add);
		iv_economic_add.setVisibility(View.VISIBLE);
		iv_economic_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Economic_ServiceAct.this,
						Map_ClickAct.class);
				intent.putExtra("type", "2");
				intent.putExtra("url_map",
						"https://appapi.imrlou.com/map/index.html?client=android&search=jjgs");
				startActivity(intent);
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
					Economic_ServiceAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	private void prepare() {
		// 弹出请求对话框
		pDialog = new SweetAlertDialog(Economic_ServiceAct.this,
				SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
		dialog = new Progress_Dialog(Economic_ServiceAct.this, pDialog);
		str_avatar = new ArrayList<String>();
		str_name = new ArrayList<String>();
		str_phone = new ArrayList<String>();
		str_company = new ArrayList<String>();
		str_work_age = new ArrayList<String>();
		str_all_total = new ArrayList<String>();
		str_region_total = new ArrayList<String>();
		str_company_status = new ArrayList<String>();
		str_rname = new ArrayList<String>();
		str_id = new ArrayList<String>();
	}

	public void select_economicservice(String session_id) {
		Request request = Request.request_economic_service(session_id);
		new HttpThread(request, this);
	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				findView();
				break;
			case 2:
				Toast.makeText(Economic_ServiceAct.this, "暂无数据",
						Toast.LENGTH_SHORT).show();
				break;
			case 99:
				dialog.dismiss();
				Toast.makeText(Economic_ServiceAct.this, "网络异常，请重新尝试连接",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_NEW) {
			dialog.dismiss();
			JSONObject json;
			JSONArray joArray;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					joArray = json.getJSONArray("list");
					total = json.getString("total");
					add_v = json.getString("v");
					int lenth = joArray.length();
					for (int i = 0; i < lenth; i++) {
						json = joArray.getJSONObject(i);
						str_id.add(json.getString("id"));
						str_name.add(json.getString("cn_username")
								+ json.getString("en_username"));
						str_avatar.add(json.getString("avatar"));
						str_company.add(json.getString("company"));
						str_phone.add(json.getString("phone"));
						str_region_total.add("0");
						str_all_total.add(json.getString("all_total"));
						str_rname.add(json.getString("cj_time"));
						str_work_age.add(json.getString("work_age"));
						str_company_status
								.add(json.getString("company_status"));
					}
					newHandler.sendEmptyMessage(1);
				} else {
					dialog.dismiss();
					// Toast.makeText(MainFragmentActivity.this, msg,
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
