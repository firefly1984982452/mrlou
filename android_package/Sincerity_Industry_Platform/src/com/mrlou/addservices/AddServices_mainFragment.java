package com.mrlou.addservices;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

public class AddServices_mainFragment extends Fragment implements HttpListener {
	private ArrayList<String> str_avatar, str_name, str_company, str_work_age,
			str_all_total, str_phone, str_id, str_rname,
			str_company_status, str_rid,str_service_type;
	Activity activity;
	private LinearLayout lay_economic_area_list, lay_economic_area;
	private ListView mListView;
	private AddServices_mainAdapter mAdapter;
	private String text;
	int channel_id;
	private ImageView detail_loading;
	public final static int SET_NEWSLIST = 0;
	private View view;
	private SweetAlertDialog pDialog;
	private Progress_Dialog dialog;
	private String user_intro;
	private ArrayList<String> str_cjinfo_total, str_cjinfo_region_name;
	private ArrayList<String> str_fyinfo_id, str_fyinfo_building_id,
			str_fyinfo_building_name, str_fyinfo_unit_no, str_fyinfo_price,
			str_fyinfo_transaction_type, str_fyinfo_avatar;
	private ArrayList<String> str_cjinfo_id, str_cjinfo_avatar_url,
			str_cjinfo_transaction_type, str_cjinfo_building_id,
			str_cjinfo_building_name, str_cjinfo_square_meter,
			str_cjinfo_cj_time, str_cjinfo_industry;
	private String fy_total_num, fy_rent_total, fy_sell_total, cj_total_num,
			cj_rent_total, cj_sell_total;
	private String name, phone, company, work_age, avatar, status;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.activity = activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (view == null) {
			view = inflater.inflate(R.layout.addservices_fragment, container,
					false);
		}
		init_date();
		getDate();
		findView();
		return view;
	}

	private void getDate() {
		// TODO Auto-generated method stub
		Bundle args = getArguments();
		str_avatar = args.getStringArrayList("avatar");
		str_company = args.getStringArrayList("company");
		str_work_age = args.getStringArrayList("work_age");
		str_name = args.getStringArrayList("name");
		str_all_total = args.getStringArrayList("all_total");
		str_phone = args.getStringArrayList("phone");
		str_id = args.getStringArrayList("id");
		str_rname = args.getStringArrayList("rname");
		str_service_type=args.getStringArrayList("service_type");
		str_company_status = args.getStringArrayList("company_status");
		str_rid = args.getStringArrayList("rid");
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_economic_area = (LinearLayout) view
				.findViewById(R.id.lay_addservices_area);
		lay_economic_area_list = (LinearLayout) view
				.findViewById(R.id.lay_addservices_area_list);
		mListView = (ListView) view.findViewById(R.id.addservices_mListView);
		mAdapter = new AddServices_mainAdapter(activity, str_avatar, str_name,
				str_phone, str_company, str_work_age, str_all_total,
				 str_company_status, str_rname,str_service_type);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						AddServices_CompanyAct.class);
				intent.putExtra("company", str_company.get(position));
				startActivity(intent);
			}
		});

	}

	public void select_agent(String id) {
		Request request = Request.requesteconomicpersonal(id);
		new HttpThread(request, this);
	}

	public void setVis(boolean flag) {
		if (flag) {
			lay_economic_area.setVisibility(View.GONE);
			lay_economic_area_list.setVisibility(View.VISIBLE);
		} else {
			lay_economic_area.setVisibility(View.VISIBLE);
			lay_economic_area_list.setVisibility(View.GONE);
		}
	}

	public void setAdapter(ArrayList<String> avatar, ArrayList<String> name,
			ArrayList<String> phone, ArrayList<String> company,
			ArrayList<String> work_age, ArrayList<String> all_total,
			ArrayList<String> company_status,
			ArrayList<String> rname, ArrayList<String> id, ArrayList<String> rid) {
		str_avatar = avatar;
		str_company = company;
		str_work_age = work_age;
		str_name = name;
		str_all_total = all_total;
		str_phone = phone;
		str_id = id;
		str_rname = rname;
		str_company_status = company_status;
		str_rid = rid;
		mAdapter.notifyDataSetChanged();
	}

	/* 摧毁视图 */
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.d("onDestroyView", "channel_id = " + channel_id);
		mAdapter = null;
	}

	/* 摧毁该Fragment，一般是FragmentActivity 被摧毁的时候伴随着摧毁 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Intent intent = new Intent(getActivity(),
						AddServices_personalAct.class);
				intent.putStringArrayListExtra("cjinfo_total", str_cjinfo_total);
				intent.putStringArrayListExtra("cjinfo_region_name",
						str_cjinfo_region_name);
				intent.putStringArrayListExtra("fyinfo_id", str_fyinfo_id);
				intent.putStringArrayListExtra("fyinfo_building_id",
						str_fyinfo_building_id);
				intent.putStringArrayListExtra("fyinfo_building_name",
						str_fyinfo_building_name);
				intent.putStringArrayListExtra("fyinfo_unit_no",
						str_fyinfo_unit_no);
				intent.putStringArrayListExtra("fyinfo_price", str_fyinfo_price);
				intent.putStringArrayListExtra("fyinfo_transaction_type",
						str_fyinfo_transaction_type);
				intent.putStringArrayListExtra("fyinfo_avatar",
						str_fyinfo_avatar);
				intent.putStringArrayListExtra("cjinfo_id", str_cjinfo_id);
				intent.putStringArrayListExtra("cjinfo_building_id",
						str_cjinfo_building_id);
				intent.putStringArrayListExtra("cjinfo_building_name",
						str_cjinfo_building_name);
				intent.putStringArrayListExtra("cjinfo_square_meter",
						str_cjinfo_square_meter);
				intent.putStringArrayListExtra("cjinfo_cj_time",
						str_cjinfo_cj_time);
				intent.putStringArrayListExtra("cjinfo_industry",
						str_cjinfo_industry);
				intent.putStringArrayListExtra("cjinfo_avatar_url",
						str_cjinfo_avatar_url);
				intent.putStringArrayListExtra("cjinfo_transaction_type",
						str_cjinfo_transaction_type);
				intent.putExtra("user_intro", user_intro);
				intent.putExtra("fy_total_num", fy_total_num);
				intent.putExtra("fy_sell_total", fy_sell_total);
				intent.putExtra("cj_total_num", cj_total_num);
				intent.putExtra("fy_rent_total", fy_rent_total);
				intent.putExtra("cj_rent_total", cj_rent_total);
				intent.putExtra("cj_sell_total", cj_sell_total);
				intent.putExtra("name", name);
				intent.putExtra("phone", phone);
				intent.putExtra("company", company);
				intent.putExtra("work_age", work_age);
				intent.putExtra("avatar", avatar);
				intent.putExtra("status", status);
				dialog.dismiss();
				startActivity(intent);
				break;
			case 2:
				dialog.dismiss();
				Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				dialog.dismiss();
				Toast.makeText(getActivity(), "网络异常，请重新尝试连接", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC) {
			JSONObject json, json2, json3, json4, json5;
			JSONArray joArray, joArray2, joArray3;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					clear_data();
					json2 = json.getJSONObject("list");
					json3 = json2.getJSONObject("user_info");
					user_intro = json3.getString("intro");
					joArray = json2.getJSONArray("cj_info");
					int lenth = joArray.length();
					for (int i = 0; i < lenth; i++) {
						json = joArray.getJSONObject(i);
						str_cjinfo_total.add(json.getString("total"));
						str_cjinfo_region_name.add(json
								.getString("region_name"));
					}
					json4 = json2.getJSONObject("fy_list");
					fy_rent_total = json4.getString("rent_total");
					fy_sell_total = json4.getString("sell_total");
					fy_total_num = json4.getString("total_num");
					if (!fy_total_num.equals("0")) {
						joArray2 = json4.getJSONArray("data");
						int lenth2 = joArray2.length();
						for (int i = 0; i < lenth2; i++) {
							json = joArray2.getJSONObject(i);
							str_fyinfo_avatar.add(json.getString("avatar"));
							str_fyinfo_building_id.add(json
									.getString("building_id"));
							str_fyinfo_building_name.add(json
									.getString("building_name"));
							str_fyinfo_id.add(json.getString("id"));
							str_fyinfo_price.add(json.getString("price"));
							str_fyinfo_transaction_type.add(json
									.getString("transaction_type"));
							str_fyinfo_unit_no.add(json.getString("unit_no"));
						}
					}

					json5 = json2.getJSONObject("cj_list");
					cj_rent_total = json5.getString("rent_total");
					cj_sell_total = json5.getString("sell_total");
					cj_total_num = json5.getString("total_num");
					if (!cj_total_num.equals("0")) {
						joArray3 = json5.getJSONArray("data");
						int lenth3 = joArray3.length();
						for (int i = 0; i < lenth3; i++) {
							json = joArray3.getJSONObject(i);
							str_cjinfo_avatar_url.add(json
									.getString("avatar_url"));
							str_cjinfo_building_id.add(json
									.getString("building_id"));
							str_cjinfo_building_name.add(json
									.getString("building_name"));
							str_cjinfo_cj_time.add(json.getString("cj_time"));
							str_cjinfo_id.add(json.getString("id"));
							str_cjinfo_industry.add(json.getString("industry"));
							str_cjinfo_square_meter.add(json
									.getString("square_meter"));
							str_cjinfo_transaction_type.add(json
									.getString("transaction_type"));
						}
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

	@Override
	public void doError(String s) {
		// TODO Auto-generated method stub
		if (s.equals("请求异常")) {
			newHandler.sendEmptyMessage(99);
		}
	}

	private void init_date() {
		// TODO Auto-generated method stub
		// 成交信息
		str_cjinfo_total = new ArrayList<String>();
		str_cjinfo_region_name = new ArrayList<String>();
		// 发布房源
		str_fyinfo_id = new ArrayList<String>();
		str_fyinfo_building_id = new ArrayList<String>();
		str_fyinfo_building_name = new ArrayList<String>();
		str_fyinfo_unit_no = new ArrayList<String>();
		str_fyinfo_price = new ArrayList<String>();
		str_fyinfo_transaction_type = new ArrayList<String>();
		str_fyinfo_avatar = new ArrayList<String>();
		// 成交历史
		str_cjinfo_id = new ArrayList<String>();
		str_cjinfo_building_id = new ArrayList<String>();
		str_cjinfo_building_name = new ArrayList<String>();
		str_cjinfo_square_meter = new ArrayList<String>();
		str_cjinfo_cj_time = new ArrayList<String>();
		str_cjinfo_industry = new ArrayList<String>();
		str_cjinfo_avatar_url = new ArrayList<String>();
		str_cjinfo_transaction_type = new ArrayList<String>();
	}

	private void clear_data() {
		str_cjinfo_total.clear();
		str_cjinfo_region_name.clear();
		str_fyinfo_id.clear();
		str_fyinfo_building_id.clear();
		str_fyinfo_building_name.clear();
		str_fyinfo_unit_no.clear();
		str_fyinfo_price.clear();
		str_fyinfo_transaction_type.clear();
		str_fyinfo_avatar.clear();
		str_cjinfo_id.clear();
		str_cjinfo_building_id.clear();
		str_cjinfo_building_name.clear();
		str_cjinfo_square_meter.clear();
		str_cjinfo_cj_time.clear();
		str_cjinfo_industry.clear();
		str_cjinfo_avatar_url.clear();
		str_cjinfo_transaction_type.clear();
	}
}
