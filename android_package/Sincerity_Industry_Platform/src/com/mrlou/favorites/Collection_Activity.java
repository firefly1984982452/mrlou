package com.mrlou.favorites;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.mrlou.addservices.AddServices_personalAct;
import com.mrlou.listener.HttpListener;
import com.mrlou.message.Message_NoticeAct;
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.mrlou.yi.Yi_Activity;

/**
 * @author jx_chen
 * @date 2016-10-17---下午2:30:03
 * @problem
 */
public class Collection_Activity extends Fragment implements HttpListener {
	private View view;
	private TabHost tabHost;
	private TextView tv_builder;// 楼盘
	private TextView tv_house;// 房源
	private TextView tv_agent;// 经纪人
	private LinearLayout lay_builder;
	private LinearLayout lay_hourse;
	private LinearLayout lay_agent;
	private SharedPreferences sharedPreferences;
	private View currentTab;
	private String total, valtotal, donetotal, losetotal, building_total,
			agent_total,session_id,type_id;
	private ArrayList<String> str_building_url, str_building_name,
			str_building_area, str_building_price, str_building_id;
	private ArrayList<String> str_area_name, str_total, str_id, str_name,
			str_area_code, str_totals;
	private ArrayList<String> str_agent_url, str_agent_name, str_agent_company,
			str_agent_age, str_agent_deal, str_agent_phone, str_agent_status,
			str_agent_id;
	private SweetAlertDialog pDialog;
	private Progress_Dialog dialog;
	private LinearLayout lay_collect_ylb,lay_collectmain_back;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.mrlou_favorites, container, false);
		sharedPreferences = getActivity().getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		type_id = sharedPreferences.getString("type_id", "");
		findView();
		LocalActivityManager groupActivityManager = new LocalActivityManager(
				getActivity(), false);
		groupActivityManager.dispatchCreate(savedInstanceState);
		tabHost.setup(groupActivityManager);
		init();
		initDate();
		dialog.dialog();
		select_requestcollect(session_id);
		return view;
	}

	private void init() {
		// TODO Auto-generated method stub
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String arg0) {
				if (currentTab == null) {
					currentTab = tabHost.getCurrentView();
					return;
				}
				Animation animation = AnimationUtils.loadAnimation(
						getActivity(), R.anim.push_left_out);
				currentTab.setAnimation(animation);
				animation = AnimationUtils.loadAnimation(getActivity(),
						R.anim.push_left_in);
				tabHost.getCurrentView().setAnimation(animation);
				currentTab = tabHost.getCurrentView();
			}
		});
	}

	private void initTabhost() {
		// TODO Auto-generated method stub
		tv_builder.setText("收藏楼盘("+building_total+")");
		tv_house.setText("收藏房源("+total+")");
		tv_agent.setText("收藏经纪人("+agent_total+")");
		Intent intent = new Intent(getActivity(), Builder_act.class);
		intent.putStringArrayListExtra("id", str_building_id);
		intent.putStringArrayListExtra("name", str_building_name);
		intent.putStringArrayListExtra("price", str_building_price);
		intent.putStringArrayListExtra("avatar_url", str_building_url);
		intent.putStringArrayListExtra("address", str_building_area);
		
		Intent intent02=new Intent(getActivity(), Hourse_act.class);
		intent02.putStringArrayListExtra("area_name", str_area_name);
		intent02.putStringArrayListExtra("total", str_total);
		intent02.putStringArrayListExtra("id", str_id);
		intent02.putStringArrayListExtra("name", str_name);
		intent02.putStringArrayListExtra("area_code", str_area_code);
		intent02.putStringArrayListExtra("totals", str_totals);
		
		Intent intent03=new Intent(getActivity(), Agent_act.class);
		intent03.putStringArrayListExtra("id", str_agent_id);
		intent03.putStringArrayListExtra("name", str_agent_name);
		intent03.putStringArrayListExtra("phone", str_agent_phone);
		intent03.putStringArrayListExtra("company", str_agent_company);
		intent03.putStringArrayListExtra("company_status", str_agent_status);
		intent03.putStringArrayListExtra("work_age", str_agent_age);
		intent03.putStringArrayListExtra("avatar", str_agent_url);
		intent03.putStringArrayListExtra("deal", str_agent_deal);
		
		tabHost.addTab(tabHost
				.newTabSpec(IntenrProvider.ACTION_COLLECTION_BULIDER)
				.setIndicator(IntenrProvider.ACTION_COLLECTION_BULIDER)
				.setContent(new Intent(intent)));
		tabHost.addTab(tabHost
				.newTabSpec(IntenrProvider.ACTION_COLLECTION_HOURSE)
				.setIndicator(IntenrProvider.ACTION_COLLECTION_HOURSE)
				.setContent(new Intent(intent02)));
		tabHost.addTab(tabHost
				.newTabSpec(IntenrProvider.ACTION_COLLECTION_AGENT)
				.setIndicator(IntenrProvider.ACTION_COLLECTION_AGENT)
				.setContent(new Intent(intent03)));

	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_collectmain_back=(LinearLayout) view.findViewById(R.id.lay_collectmain_back);
		lay_collectmain_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
//		lay_collect_ylb=(LinearLayout) view.findViewById(R.id.lay_collect_ylb);
//		if (type_id.equals("299")) {
//			lay_collect_ylb.setVisibility(View.INVISIBLE);
//		}else {
//			lay_collect_ylb.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Intent intent=new Intent(getActivity(),Yi_Activity.class);
//					startActivity(intent);
//				}
//			});
//		}
		tabHost = (TabHost) view.findViewById(R.id.tab_host);
		// 楼盘
		tv_builder = (TextView) view.findViewById(R.id.tv_building);
		lay_builder = (LinearLayout) view.findViewById(R.id.lay_main_building);
		lay_builder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				changeTagBg(1);
			}
		});
		// 房源
		tv_house = (TextView) view.findViewById(R.id.tv_hourse);
		lay_hourse = (LinearLayout) view.findViewById(R.id.lay_main_hourse);
		lay_hourse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				changeTagBg(2);
			}
		});
		// 经纪人
		tv_agent = (TextView) view.findViewById(R.id.tv_agent);
		lay_agent = (LinearLayout) view.findViewById(R.id.lay_main_agent);
		lay_agent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				changeTagBg(3);
			}
		});
	}

	private void changeTagBg(int flag) {
		// TODO Auto-generated method stub
		if (flag == 1) {
			lay_builder.setBackgroundResource(R.drawable.corner_left_black);
			tabHost.setCurrentTabByTag(IntenrProvider.ACTION_COLLECTION_BULIDER);
			tv_builder.setTextColor(getActivity().getResources().getColor(
					R.color.white));
		} else {
			lay_builder
					.setBackgroundResource(R.drawable.corner_left_black_and_white);
			tv_builder.setTextColor(getActivity().getResources().getColor(
					R.color.light_gray));
		}
		if (flag == 2) {
			lay_hourse.setBackgroundResource(R.color.light_dark);
			tabHost.setCurrentTabByTag(IntenrProvider.ACTION_COLLECTION_HOURSE);
			tv_house.setTextColor(getActivity().getResources().getColor(
					R.color.white));
		} else {
			lay_hourse.setBackgroundResource(R.drawable.border_center_black);
			tv_house.setTextColor(getActivity().getResources().getColor(
					R.color.light_gray));
		}
		if (flag == 3) {
			lay_agent.setBackgroundResource(R.drawable.corner_right_black);
			tabHost.setCurrentTabByTag(IntenrProvider.ACTION_COLLECTION_AGENT);
			tv_agent.setTextColor(getActivity().getResources().getColor(
					R.color.white));
		} else {
			lay_agent
					.setBackgroundResource(R.drawable.corner_right_black_and_white);
			tv_agent.setTextColor(getActivity().getResources().getColor(
					R.color.light_gray));
		}
	}

	public void select_requestcollect(String session_id) {
		Request request = Request.requestcollect(session_id);
		new HttpThread(request, this);
	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				initTabhost();
				changeTagBg(1);
				break;
			case 2:
				Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(getActivity(), "网络异常，请重新尝试连接", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC) {
			dialog.dismiss();
			JSONObject json, json2, json3, json4, json5, json6;
			JSONArray joArray, joArray2, joArray3;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					clearDate();
					json2 = json.getJSONObject("data1");
					building_total = json2.getString("total");
					joArray = json2.getJSONArray("list");
					int len = joArray.length();
					for (int i = 0; i < len; i++) {
						json3 = joArray.getJSONObject(i);
						str_building_id.add(json3.getString("id"));
						str_building_url.add(json3.getString("avatar_url"));
						str_building_name.add(json3.getString("name"));
						str_building_area.add(json3.getString("address"));
						str_building_price.add(json3.getString("rent_price")
								+ json3.getString("sold_price"));
					}
					
					json4 = json.getJSONObject("data2");
					total = json4.getString("total");
					joArray2 = json4.getJSONArray("list");
					int length = joArray2.length();
					for (int j = 0; j < length; j++) {
						json3 = joArray2.getJSONObject(j);
						str_area_name.add(json3.getString("area_name"));
						str_total.add(json3.getString("total"));
						joArray3 = json3.getJSONArray("cate");
						for (int m = 0; m < joArray3.length(); m++) {
							json2 = joArray3.getJSONObject(m);
							str_id.add(json2.getString("id"));
							str_name.add(json2.getString("name"));
							str_area_code.add(json2.getString("area_code"));
							str_totals.add(json2.getString("total"));
							if (m >= 1) {
								str_total.add("");
								str_area_name.add("");
							}
						}
					}
					json5 = json.getJSONObject("data3");
					agent_total = json5.getString("total");
					joArray3 = json5.getJSONArray("list");
					for (int i = 0; i < joArray3.length(); i++) {
						json = joArray3.getJSONObject(i);
						str_agent_id.add(json.getString("id"));
						str_agent_name.add(json.getString("cn_username")+json.getString("en_username"));
						str_agent_company.add(json.getString("company"));
						str_agent_phone.add(json.getString("phone"));
						str_agent_status.add(json.getString("company_status"));
						str_agent_age.add(json.getString("work_age"));
						str_agent_deal.add("最新成交"+json.getString("all_total")+"  "+json.getString("rname")+json.getString("region_total"));
						str_agent_url.add(json.getString("avatar"));
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

	// 调用返回键的功能
		public void simulateKey(final int KeyCode) {
			new Thread() {
				public void run() {
					try {
						Instrumentation inst = new Instrumentation();
						inst.sendKeyDownUpSync(KeyCode);
						getActivity().finish();
					} catch (Exception e) {
						Log.e("Exception when sendKeyDownUpSync", e.toString());
					}
				}
			}.start();
		}
		
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		dialog.dialog();
		select_requestcollect(session_id);
	}
	@Override
	public void doError(String s) {
		// TODO Auto-generated method stub
		if (s.equals("请求异常")) {
			dialog.dismiss();
			newHandler.sendEmptyMessage(99);
		}
	}
	
	public void initDate(){
		str_agent_age=new ArrayList<>();
		str_agent_company=new ArrayList<>();
		str_agent_deal=new ArrayList<>();
		str_agent_id=new ArrayList<>();
		str_agent_name=new ArrayList<>();
		str_agent_phone=new ArrayList<>();
		str_agent_status=new ArrayList<>();
		str_agent_url=new ArrayList<>();
		
		str_area_code=new ArrayList<>();
		str_area_name=new ArrayList<>();
		str_building_area=new ArrayList<>();
		str_building_id=new ArrayList<>();
		str_building_name=new ArrayList<>();
		str_building_price=new ArrayList<>();
		str_building_url=new ArrayList<>();
		str_id=new ArrayList<>();
		str_name=new ArrayList<>();
		str_total=new ArrayList<>();
		str_totals=new ArrayList<>();
	
		pDialog = new SweetAlertDialog(getActivity(),
				SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
		dialog = new Progress_Dialog(getActivity(), pDialog);
	}
	
	public void clearDate(){
		str_agent_age.clear();
		str_agent_company.clear();
		str_agent_deal.clear();
		str_agent_id.clear();
		str_agent_name.clear();
		str_agent_phone.clear();
		str_agent_status.clear();
		str_agent_url.clear();
		
		str_area_code.clear();
		str_area_name.clear();
		str_building_area.clear();
		str_building_id.clear();
		str_building_name.clear();
		str_building_price.clear();
		str_building_url.clear();
		str_id.clear();
		str_name.clear();
		str_total.clear();
		str_totals.clear();
	}
}
