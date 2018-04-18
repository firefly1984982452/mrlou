package com.mrlou.message;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.array;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.mrlou.yi.YI_HourseAct;
import com.mrlou.yi.Yi_Activity;
import com.mrlou.yi.Yi_CustomerAct;

/**
 * @author jx_chen
 * @date 2016-11-21---上午10:25:54
 * @problem
 */
public class Message_SelectAct extends Activity implements HttpListener {

	private LinearLayout lay_select_icon01, lay_select_icon02,
			lay_select_infoedit, lay_select_infoedit02, lay_select_area,
			lay_select_identify, lay_select_back;
	private ImageView iv_select_icon01, iv_select_icon02, iv_select_infoedit,
			iv_select_infoedit02;
	private EditText et_select_meter, et_select_price, et_select_meter02,
			et_select_price02;
	private TextView tv_select_unit, tv_select_unit02, tv_select_area,
			tv_select_identify;
	private Button bt_select_comfirm;
	private String transaction_type, room_price_unit, area_code, type_code;
	private PopupWindow popwindow, pop_identify;
	private View view, view_identify;
	private Message_SelectAreaAdapter mAdapter;
	private Message_SelectIdentifyAdapter mAdapter2;
	private ListView lv_area, lv_identify;
	private ArrayList<String> str_area, str_id, str_type, str_type_id,str_flag;
	private ArrayList<String> str_ids, str_category, str_building_id,
			str_building_name, str_avatar, str_transaction_type, str_unit_no,
			str_status, str_verified, str_image_total, str_rent_price,
			str_sell_price, str_release_time, str_user_type, str_fee_rate,
			str_unique;
	private SweetAlertDialog pDialog;
	private Progress_Dialog dialog;
	private Button bt_select_yi_all,bt_select_yi_modify;
	private int flags=99;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_select);
		pDialog = new SweetAlertDialog(Message_SelectAct.this,
				SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
		dialog = new Progress_Dialog(Message_SelectAct.this, pDialog);
		prepare();
		init();
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_select_back = (LinearLayout) findViewById(R.id.lay_select_back);
		lay_select_icon01 = (LinearLayout) findViewById(R.id.lay_select_icon01);
		lay_select_icon02 = (LinearLayout) findViewById(R.id.lay_select_icon02);
		lay_select_infoedit = (LinearLayout) findViewById(R.id.lay_select_infoedit);
		lay_select_infoedit02 = (LinearLayout) findViewById(R.id.lay_select_infoedit02);
		lay_select_area = (LinearLayout) findViewById(R.id.lay_select_area);
		lay_select_identify = (LinearLayout) findViewById(R.id.lay_select_identify);

		iv_select_icon01 = (ImageView) findViewById(R.id.iv_select_icon01);
		iv_select_icon02 = (ImageView) findViewById(R.id.iv_select_icon02);
		iv_select_infoedit = (ImageView) findViewById(R.id.iv_select_infoedit);
		iv_select_infoedit02 = (ImageView) findViewById(R.id.iv_select_infoedit02);

		et_select_meter = (EditText) findViewById(R.id.et_select_meter);
		et_select_meter02 = (EditText) findViewById(R.id.et_select_meter02);
		et_select_price = (EditText) findViewById(R.id.et_select_price);
		et_select_price02 = (EditText) findViewById(R.id.et_select_price02);

		tv_select_unit = (TextView) findViewById(R.id.tv_select_unit);
		tv_select_unit02 = (TextView) findViewById(R.id.tv_select_unit02);
		tv_select_area = (TextView) findViewById(R.id.tv_select_area);
		tv_select_identify = (TextView) findViewById(R.id.tv_select_identify);

		bt_select_comfirm = (Button) findViewById(R.id.bt_select_comfirm);
		lay_select_area.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.setOutsideTouchable(true);
				popwindow.showAtLocation(lay_select_back, Gravity.RIGHT
						| Gravity.BOTTOM, 0, 0);
				popwindow.showAsDropDown(view);
				popwindow.update();
				WindowManager.LayoutParams params = Message_SelectAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				Message_SelectAct.this.getWindow().setAttributes(params);
			}
		});
		view = LayoutInflater.from(Message_SelectAct.this).inflate(
				R.layout.message_select_arealist, null);
		// 设置popwindow
		WindowManager wm = this.getWindowManager();
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		popwindow = new PopupWindow(view, width - 200, height - 50, true);
		popwindow.setAnimationStyle(R.style.popwin_anim_style);
		popwindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					popwindow.dismiss();
					return true;
				}
				return false;
			}
		});
		ColorDrawable dw = new ColorDrawable(0x90000000);
		popwindow.setOnDismissListener(new poponDismissListener());
		popwindow.setBackgroundDrawable(dw);
		// pop_addmenu.setBackgroundDrawable(new BitmapDrawable());
		popwindow.setFocusable(true);
		
		bt_select_yi_all=(Button) view.findViewById(R.id.bt_select_yi_all);
		bt_select_yi_all.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int j = 0; j < str_flag.size(); j++) {
					str_flag.set(j, "0");
				}
				mAdapter.notifyDataSetChanged();
				flags=99;
			}
		});
		bt_select_yi_modify=(Button) view.findViewById(R.id.bt_select_yi_modify);
		bt_select_yi_modify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (flags!=99) {
					for (int i = 0; i < str_flag.size(); i++) {
						if (flags==i) {
							str_flag.set(i, "1");
						}else {
							str_flag.set(i, "0");
						}
					}
					mAdapter.notifyDataSetChanged();
					tv_select_area.setText(str_area.get(flags));
					area_code = str_id.get(flags);
				}
				popwindow.dismiss();
			}
		});
		lv_area = (ListView) view.findViewById(R.id.lv_select_area);
		mAdapter = new Message_SelectAreaAdapter(Message_SelectAct.this,
				str_area,str_flag);
		lv_area.setAdapter(mAdapter);
		lv_area.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				flags=position;
				for (int i = 0; i < str_flag.size(); i++) {
					if (position==i) {
						str_flag.set(i, "1");
					}else {
						str_flag.set(i, "0");
					}
				}
				mAdapter.notifyDataSetChanged();
			}
		});

		lay_select_identify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop_identify.setOutsideTouchable(true);
				pop_identify.showAtLocation(lay_select_back, Gravity.RIGHT
						| Gravity.BOTTOM, 0, 0);
				pop_identify.showAsDropDown(view_identify);
				pop_identify.update();
				WindowManager.LayoutParams params = Message_SelectAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				Message_SelectAct.this.getWindow().setAttributes(params);
			}
		});
		view_identify = LayoutInflater.from(Message_SelectAct.this).inflate(
				R.layout.message_select_identifylist, null);
		pop_identify = new PopupWindow(view_identify, width - 200, height - 50,
				true);
		pop_identify.setAnimationStyle(R.style.popwin_anim_style);
		pop_identify.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					pop_identify.dismiss();
					return true;
				}
				return false;
			}
		});
		pop_identify.setOnDismissListener(new poponDismissListener());
		pop_identify.setBackgroundDrawable(dw);
		pop_identify.setFocusable(true);
		lv_identify = (ListView) view_identify
				.findViewById(R.id.lv_select_identify);
		mAdapter2 = new Message_SelectIdentifyAdapter(Message_SelectAct.this,
				str_type);
		lv_identify.setAdapter(mAdapter2);
		lv_identify.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				pop_identify.dismiss();
				tv_select_identify.setText(str_type.get(position));
				type_code = str_type_id.get(position);
			}
		});
		lay_select_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		lay_select_icon01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				transaction_type = "1";
				tv_select_unit.setText("元/㎡/天");
				tv_select_unit02.setText("元/月");
				selecticon(1);
			}
		});
		lay_select_icon02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				transaction_type = "2";
				tv_select_unit.setText("元/㎡");
				tv_select_unit02.setText("万元");
				selecticon(2);
			}
		});
		lay_select_infoedit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selecticon02(1);
				if (transaction_type.equals("1")) {
					room_price_unit = "1";
				} else {
					room_price_unit = "3";
				}
			}
		});
		lay_select_infoedit02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selecticon02(2);
				if (transaction_type.equals("1")) {
					room_price_unit = "2";
				} else {
					room_price_unit = "4";
				}
			}
		});
		transaction_type = "1";
		room_price_unit = "1";
		tv_select_unit.setText("元/㎡/天");
		tv_select_unit02.setText("元/月");
		selecticon(1);
		selecticon02(1);
		tv_select_area.setText("全部");
		tv_select_identify.setText("全部");

		bt_select_comfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dialog();
				if (tv_select_area.getText().toString().equals("全部")) {
					area_code = "";
				} 
				if (tv_select_identify.getText().toString().equals("全部")) {
					type_code="";
				} 

				select_newhourse(transaction_type, et_select_meter.getText()
						.toString()
						+ ","
						+ et_select_meter02.getText().toString(),
						et_select_price.getText().toString() + ","
								+ et_select_price02.getText().toString(),
						room_price_unit, area_code, type_code);
			}
		});
	}

	public void selecticon(int flag) {
		switch (flag) {
		case 1:
			iv_select_icon01.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_checked));
			iv_select_icon02.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			break;
		case 2:
			iv_select_icon01.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_select_icon02.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_checked));
			break;
		default:
			break;
		}
	}

	public void selecticon02(int flag) {
		switch (flag) {
		case 1:
			iv_select_infoedit.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.resourse_images_checked));
			iv_select_infoedit02.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.resourse_images_uncheck));
			break;
		case 2:
			iv_select_infoedit.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.resourse_images_uncheck));
			iv_select_infoedit02.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.resourse_images_checked));
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
//					Instrumentation inst = new Instrumentation();
//					inst.sendKeyDownUpSync(KeyCode);
					Intent intent = new Intent(Message_SelectAct.this, Message_NewHourseAct.class);
					intent.putExtra("type", "0");
					startActivity(intent);
					Message_SelectAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	class poponDismissListener implements PopupWindow.OnDismissListener {

		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub
			clearWindow();
		}
	}

	public void clearWindow() {
		WindowManager.LayoutParams params = Message_SelectAct.this.getWindow()
				.getAttributes();
		params.alpha = 1f;
		Message_SelectAct.this.getWindow().setAttributes(params);
	}

	private void prepare() {
		str_area = new ArrayList<String>();
		str_area.add("浦东新区");
		str_area.add("黄浦区");
		str_area.add("静安区");
		str_area.add("徐汇区");
		str_area.add("长宁区");
		str_area.add("普陀区");
		str_area.add("闸北区");
		str_area.add("虹口区");
		str_area.add("杨浦区");
		str_area.add("宝山区");
		str_area.add("闵行区");
		str_area.add("嘉定区");
		str_area.add("青浦区");
		str_area.add("松江区");
		str_area.add("奉贤区");
		str_area.add("金山区");
		str_area.add("崇明区");
		str_id = new ArrayList<String>();
		str_id.add("310115");
		str_id.add("310101");
		str_id.add("310106");
		str_id.add("310104");
		str_id.add("310105");
		str_id.add("310107");
		str_id.add("310108");
		str_id.add("310109");
		str_id.add("310110");
		str_id.add("310113");
		str_id.add("310112");
		str_id.add("310114");
		str_id.add("310118");
		str_id.add("310117");
		str_id.add("310120");
		str_id.add("310116");
		str_id.add("310230");
		str_flag=new ArrayList<>();
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_flag.add("0");
		str_type = new ArrayList<String>();
		str_type.add("开发商");
		str_type.add("代理商");
		str_type.add("运营商");
		str_type.add("物业公司");
		str_type.add("经纪人");
		str_type.add("小业主");
		str_type.add("委托人");
		str_type_id = new ArrayList<String>();
		str_type_id.add("1");
		str_type_id.add("2");
		str_type_id.add("3");
		str_type_id.add("103");
		str_type_id.add("102");
		str_type_id.add("201");
		str_type_id.add("202");
	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Intent intent = new Intent(Message_SelectAct.this,
						Message_NewHourseAct.class);
				intent.putStringArrayListExtra("id", str_ids);
				intent.putStringArrayListExtra("category", str_category);
				intent.putStringArrayListExtra("building_id", str_building_id);
				intent.putStringArrayListExtra("building_name",
						str_building_name);
				intent.putStringArrayListExtra("avatar", str_avatar);
				intent.putStringArrayListExtra("sell_price", str_sell_price);
				intent.putStringArrayListExtra("transaction_type",
						str_transaction_type);
				intent.putStringArrayListExtra("release_time", str_release_time);
				intent.putStringArrayListExtra("unit_no", str_unit_no);
				intent.putStringArrayListExtra("status", str_status);
				intent.putStringArrayListExtra("verified", str_verified);
				intent.putStringArrayListExtra("user_type", str_user_type);
				intent.putStringArrayListExtra("image_total", str_image_total);
				intent.putStringArrayListExtra("rent_price", str_rent_price);
				intent.putStringArrayListExtra("fee_rate", str_fee_rate);
				intent.putStringArrayListExtra("unique", str_unique);
				intent.putExtra("type", "1");
				startActivity(intent);
				Message_SelectAct.this.finish();
				break;
			case 2:
				Toast.makeText(Message_SelectAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				dialog.dismiss();
				Toast.makeText(Message_SelectAct.this, "网络异常，请重新发起请求", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public void select_newhourse(String trs_type, String meter, String price,
			String unit, String regions, String type) {
		Request request = Request.request_hoursesearch(trs_type, meter, price,
				unit, regions, type);
		new HttpThread(request, this);
	}

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC) {
			dialog.dismiss();
			JSONObject json, json2;
			JSONArray joArray;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					joArray = json.getJSONArray("list");
					int length = joArray.length();
					for (int i = 0; i < length; i++) {
						json2 = joArray.getJSONObject(i);
						str_ids.add(json2.getString("id"));
						str_category.add(json2.getString("category"));
						str_building_id.add(json2.getString("building_id"));
						str_building_name.add(json2.getString("building_name"));
						str_avatar.add(json2.getString("avatar"));
						str_transaction_type.add(json2
								.getString("transaction_type"));
						str_unit_no.add(json2.getString("unit_no"));
						str_status.add(json2.getString("status"));
						str_verified.add(json2.getString("verified"));
						str_image_total.add(json2.getString("image_total"));
						str_rent_price.add(json2.getString("rent_price"));
						str_sell_price.add(json2.getString("sell_price"));
						str_release_time.add(json2.getString("release_time"));
						str_user_type.add(json2.getString("user_type"));
						str_fee_rate.add(json2.getString("fee_rate"));
						str_unique.add(json2.getString("unique"));
					}
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

	private void init() {
		// TODO Auto-generated method stub
		str_ids = new ArrayList<String>();
		str_category = new ArrayList<String>();
		str_building_id = new ArrayList<String>();
		str_building_name = new ArrayList<String>();
		str_avatar = new ArrayList<String>();
		str_transaction_type = new ArrayList<String>();
		str_unit_no = new ArrayList<String>();
		str_status = new ArrayList<String>();
		str_verified = new ArrayList<String>();
		str_image_total = new ArrayList<String>();
		str_rent_price = new ArrayList<String>();
		str_sell_price = new ArrayList<String>();
		str_user_type = new ArrayList<String>();
		str_unique = new ArrayList<String>();
		str_fee_rate = new ArrayList<String>();
		str_release_time = new ArrayList<String>();
	}

	@Override
	public void doError(String s) {
		// TODO Auto-generated method stub
		if (s.equals("请求异常")) {
			newHandler.sendEmptyMessage(99);
		}
	}
}
