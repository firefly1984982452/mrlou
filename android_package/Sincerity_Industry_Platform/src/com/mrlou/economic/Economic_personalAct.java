package com.mrlou.economic;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.mrlou.economic.view.BadgeView;
import com.mrlou.economic.view.XCCircleRectImageView_head;
import com.mrlou.h5.Building_Act;
import com.mrlou.h5.RoomInfo_Act;
import com.mrlou.image.HttpUtil;
import com.mrlou.image.ImageLoader;
import com.mrlou.image.ImageLoader.ImageDownloader;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

public class Economic_personalAct extends Activity implements HttpListener {

	private LinearLayout lay_economic_personal_back, lay_economic_list_company,
			lay_economic_personal_collect,lay_economic_personal_error;
	private ArrayList<String> str_dw, str_name, str_status, str_url;
	private ArrayList<String> str_detail, str_job, str_details, str_other;
	private GridView gridView;
	private RectclerviewAdapter adapter;
	private Button bt_eco_personal_lishi, bt_eco_personal_fabu;
	private ListView lv_economic_personal;
	private Eco_perListAdapter adapter2;
	private LinearLayout lay_economic_personal_search, lay_economic_per_phone;
	private XCCircleRectImageView_head imageView;
	private ImageLoader imageLoader;
	private ImageView iv_economic_personal_collect;
	private BadgeView badgeView;
	private TextView tv_economic_list_company, tv_economic_per_age,
			tv_economic_intro, tv_economic_per_name, tv_economic_per_phone;
	private String user_intro, url, url2;
	private ArrayList<String> str_cjinfo_total, str_cjinfo_region_name, str_id;
	private ArrayList<String> str_fyinfo_id, str_fyinfo_building_id,
			str_fyinfo_building_name, str_fyinfo_unit_no, str_fyinfo_price,
			str_fyinfo_transaction_type, str_fyinfo_avatar;
	private ArrayList<String> str_cjinfo_id, str_cjinfo_total_num,
			str_cjinfo_avatar_url, str_cjinfo_sell_total,
			str_cjinfo_building_id, str_cjinfo_building_name,
			str_cjinfo_square_meter, str_cjinfo_cj_time, str_cjinfo_industry,
			str_cjinfo_transaction_type;
	private String fy_total_num, fy_rent_total, fy_sell_total, cj_total_num,
			cj_rent_total, cj_sell_total, id, mrString;
	private String name, phone, company, work_age, avatar, status;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private SharedPreferences sharedPreferences;
	private String session_id, iscollect;
	private ScrollView mScrollView;
	private SweetAlertDialog pDialog;
	private Progress_Dialog dialog;
	RecyclerView recyclerView;
	FullyLinearLayoutManager layoutManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.economic_personal);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		pDialog = new SweetAlertDialog(Economic_personalAct.this,
				SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
		dialog = new Progress_Dialog(Economic_personalAct.this, pDialog);
		dialog.dialog();
		sharedPreferences = Economic_personalAct.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		init();
		getDate();
		refresh();
		select_agent(id);
		// prepare();
		// findView();
	}

	private void init() {
		recyclerView = (RecyclerView) findViewById(R.id.recycler_view_per);
		recyclerView.setHasFixedSize(true);
		int spanCount = 1; // 只显示一行
		layoutManager = new FullyLinearLayoutManager(spanCount,
				StaggeredGridLayoutManager.HORIZONTAL);
		recyclerView.setLayoutManager(layoutManager);
	}

	private void refresh() {
		// TODO Auto-generated method stub
		imageView = (XCCircleRectImageView_head) findViewById(R.id.iv_economic_personal);
		imageView.setImageResource(R.drawable.logo);
		// 这几个刷新Label的设置
		// mPullRefreshScrollView = (PullToRefreshScrollView)
		// findViewById(R.id.pull_refresh_scrollview);
		// mPullRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(
		// "上次刷新为1分钟前");
		// mPullRefreshScrollView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
		// mPullRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel(
		// "正在刷新...");
		// mPullRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel("手松开刷新");

		// 上拉、下拉设定
		// mPullRefreshScrollView.setMode(Mode.PULL_FROM_START);

		// 上拉监听函数
		// mPullRefreshScrollView
		// .setOnRefreshListener(new OnRefreshListener<ScrollView>() {

		// @Override
		// public void onRefresh(
		// PullToRefreshBase<ScrollView> refreshView) {
		// select_agent(id);
		// 执行刷新函数
		// new GetDataTask().execute();
		// }
		// });

		// 获取ScrollView布局
		// mScrollView = mPullRefreshScrollView.getRefreshableView();
	}

	private class GetDataTask extends AsyncTask<Void, Void, LinearLayout> {

		@Override
		protected LinearLayout doInBackground(Void... params) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				Log.e("msg", "GetDataTask:" + e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(LinearLayout result) {
			// Do some stuff here

			// LinearLayout sub_root_lin=(LinearLayout)
			// findViewById(R.id.sub_root_lin);
			// LinearLayout.LayoutParams LP_FW = new LinearLayout.LayoutParams(
			// LinearLayout.LayoutParams.FILL_PARENT,
			// LinearLayout.LayoutParams.WRAP_CONTENT);
			// sub_root_lin.addView(result, 0, LP_FW);

			// 控制是否继续下拉刷新
			// mPullRefreshScrollView.setMode(Mode.BOTH);

			// 在更新UI后，无需其它Refresh操作，系统会自己加载新的listView
			mPullRefreshScrollView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

	private void prepare() {
		// TODO Auto-generated method stub

		str_dw = new ArrayList<String>();
		str_dw.add("套");
		str_dw.add("㎡");
		int length = str_cjinfo_total.size() - 2;
		for (int i = 0; i < length; i++) {
			if (i == length - 1) {
				str_dw.add("");
			} else {
				str_dw.add("套");
			}
		}

		str_details = new ArrayList<String>();
		for (int i = 0; i < str_cjinfo_square_meter.size(); i++) {
			str_details.add(str_cjinfo_square_meter.get(i) + "    "
					+ str_cjinfo_cj_time.get(i));
		}
		str_detail = new ArrayList<String>();
		str_detail.addAll(str_details);
		str_name = new ArrayList<String>();
		str_name.addAll(str_cjinfo_building_name);
		str_job = new ArrayList<String>();
		str_job.addAll(str_cjinfo_industry);
		str_status = new ArrayList<String>();
		str_status.addAll(str_cjinfo_transaction_type);
		str_url = new ArrayList<String>();
		str_url.addAll(str_cjinfo_avatar_url);
		str_id = new ArrayList<String>();
		str_id.addAll(str_cjinfo_building_id);
		if (str_cjinfo_total.size() == 0) {
			str_cjinfo_total.add("0");
			str_cjinfo_total.add("0");
			str_cjinfo_region_name.add("最新成交");
			str_cjinfo_region_name.add("最新成交");
		}
		str_other = new ArrayList<String>();
		str_other.add("1");
	}

	public void clean_list() {
		str_id.clear();
		str_detail.clear();
		str_name.clear();
		str_status.clear();
		str_job.clear();
		str_url.clear();
		str_other.clear();
	}

	// 初始化控件
	private void findView() {
		// TODO Auto-generated method stub
		mrString = Economic_personalAct.this.getExternalFilesDir(null)
				.getPath();
		url = "file://" + mrString + "/bulidersir/lxs_index.html";
		url2 = "file://" + mrString + "/bulidersir/";
		lay_economic_personal_error=(LinearLayout) findViewById(R.id.lay_economic_personal_error);
		lay_economic_personal_error.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Economic_personalAct.this,Economic_error.class);
				intent.putExtra("name", name);
				intent.putExtra("id", id);
				startActivity(intent);
			}
		});
		iv_economic_personal_collect = (ImageView) findViewById(R.id.iv_economic_personal_collect);
		lay_economic_personal_collect = (LinearLayout) findViewById(R.id.lay_economic_personal_collect);
		lay_economic_personal_collect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (iscollect.equals("0")) {
					cancel_collect(session_id, "5", id);
				} else {
					select_collect(session_id, "5", id);
				}
			}
		});
		// mPullRefreshScrollView.scrollTo(0, 0);
		lay_economic_list_company = (LinearLayout) findViewById(R.id.lay_economic_list_company);
		tv_economic_list_company = (TextView) findViewById(R.id.tv_economic_list_company);
		tv_economic_list_company.setText(company);
		if (status.equals("0")) {
			lay_economic_list_company.setBackgroundDrawable(null);
			tv_economic_list_company.setTextColor(Economic_personalAct.this
					.getResources().getColor(R.color.light_gray));
		} else {
			lay_economic_list_company
					.setBackgroundDrawable(Economic_personalAct.this
							.getResources().getDrawable(
									R.drawable.corner_view_red_2dp));
			tv_economic_list_company.setTextColor(Economic_personalAct.this
					.getResources().getColor(R.color.white));
		}

		lay_economic_list_company.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (status.equals("1")) {
					Intent intent = new Intent(Economic_personalAct.this,
							Economic_CompanyAct.class);
					intent.putExtra("company", company);
					startActivity(intent);
				}
			}
		});

		tv_economic_per_age = (TextView) findViewById(R.id.tv_economic_per_age);
		tv_economic_per_age.setText("从业" + work_age + "年");
		tv_economic_intro = (TextView) findViewById(R.id.tv_economic_intro);
		tv_economic_intro.setText(user_intro);
		tv_economic_per_name = (TextView) findViewById(R.id.tv_economic_per_name);
		tv_economic_per_name.setText(name);
		tv_economic_per_phone = (TextView) findViewById(R.id.tv_economic_per_phone);
		tv_economic_per_phone.setText(phone);
		lay_economic_per_phone = (LinearLayout) findViewById(R.id.lay_economic_per_phone);
		lay_economic_per_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ phone));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		imageLoader = new ImageLoader(new ImageDownloader() {
			@Override
			public Bitmap download(String path, int width, int height) {
				return HttpUtil.download(path);
			}
		});
		lay_economic_personal_back = (LinearLayout) findViewById(R.id.lay_economic_personal_back);
		lay_economic_personal_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		lay_economic_personal_search = (LinearLayout) findViewById(R.id.lay_economic_personal_search);
		lay_economic_personal_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Economic_personalAct.this,
						Economic_SearchAct.class);
				startActivity(intent);
			}
		});
		adapter = new RectclerviewAdapter(Economic_personalAct.this,
				str_cjinfo_total, str_dw, str_cjinfo_region_name);
		recyclerView.setLayoutManager(layoutManager);
		recyclerView.setAdapter(adapter);
		imageView = (XCCircleRectImageView_head) findViewById(R.id.iv_economic_personal);
		imageView.setImageResource(R.drawable.logo);
		badgeView = new BadgeView(Economic_personalAct.this, imageView);
		badgeView.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
		badgeView.setBackgroundResource(R.drawable.oval_v);
		badgeView.show();
		imageLoader.loadImage(avatar, 100, 100, imageView);
		lv_economic_personal = (ListView) findViewById(R.id.lv_economic_personal);
		adapter2 = new Eco_perListAdapter(Economic_personalAct.this, str_name,
				str_detail, str_job, str_status, str_url, str_other);
		lv_economic_personal.setAdapter(adapter2);
		lv_economic_personal.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent;
				if (str_other.get(0).equals("1")) {
					intent = new Intent(Economic_personalAct.this,
							Building_Act.class);
				} else {
					intent = new Intent(Economic_personalAct.this,
							RoomInfo_Act.class);
				}
				intent.putExtra("url", url);
				intent.putExtra("url2", url2);
				intent.putExtra("frsource", "gg");
				intent.putExtra("id", str_id.get(position));
				startActivity(intent);
			}
		});

		bt_eco_personal_lishi = (Button) findViewById(R.id.bt_eco_personal_lishi);
		bt_eco_personal_lishi.setText("历史成交(租" + cj_rent_total + "售"
				+ cj_sell_total + ")");
		bt_eco_personal_lishi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clean_list();
				bt_eco_personal_fabu
						.setBackgroundResource(R.drawable.corner_right_gray);
				bt_eco_personal_lishi
						.setBackgroundResource(R.drawable.corner_left_black);
				str_id.addAll(str_cjinfo_building_id);
				str_detail.addAll(str_details);
				str_name.addAll(str_cjinfo_building_name);
				str_job.addAll(str_cjinfo_industry);
				str_status.addAll(str_cjinfo_transaction_type);
				str_url.addAll(str_cjinfo_avatar_url);
				str_other.add("1");
				adapter2.notifyDataSetChanged();
			}
		});
		bt_eco_personal_fabu = (Button) findViewById(R.id.bt_eco_personal_fabu);
		bt_eco_personal_fabu.setText("发布房源(租" + fy_rent_total + "售"
				+ fy_sell_total + ")");
		bt_eco_personal_fabu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clean_list();
				bt_eco_personal_lishi
						.setBackgroundResource(R.drawable.corner_left_gray);
				bt_eco_personal_fabu
						.setBackgroundResource(R.drawable.corner_right_black);
				str_id.addAll(str_fyinfo_building_id);
				str_detail.addAll(str_fyinfo_unit_no);
				str_name.addAll(str_fyinfo_building_name);
				str_job.addAll(str_fyinfo_price);
				str_status.addAll(str_fyinfo_transaction_type);
				str_url.addAll(str_fyinfo_avatar);
				str_other.add("2");
				adapter2.notifyDataSetChanged();
			}
		});
	}

	public void select_agent(String id) {
		Request request = Request.requesteconomicpersonal(id);
		new HttpThread(request, this);
	}

	public void is_collect(String session_id, String type, String id) {
		Request request = Request.requestis_collect(session_id, id, type);
		new HttpThread(request, this);
	}

	public void select_collect(String session_id, String type, String id) {
		Request request = Request.requesteconomiccollect(session_id, type, id);
		new HttpThread(request, this);
	}

	public void cancel_collect(String session_id, String type, String id) {
		Request request = Request.requestcancelpost(session_id, type, id);
		new HttpThread(request, this);
	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					Economic_personalAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	// 接收请求返回的数据
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
					name = json3.getString("cn_username")
							+ json3.getString("en_username");
					phone = json3.getString("phone");
					company = json3.getString("company");
					work_age = json3.getString("work_age");
					avatar = json3.getString("avatar");
					status = json3.getString("company_status");
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
							str_fyinfo_building_id.add(json.getString("id"));
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
		if (reqID == Request.REQUEST_ECONOMIC_COLLECT) {
			JSONObject json;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					newHandler.sendEmptyMessage(3);
				} else {
					newHandler.sendEmptyMessage(4);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (reqID == Request.REQUEST_SHARELOG) {
			JSONObject json;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				iscollect = json.getString("iscollect");
				if (iscollect.equals("0")) {
					newHandler.sendEmptyMessage(5);
				} else {
					newHandler.sendEmptyMessage(6);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (reqID == Request.REQUEST_NEW) {
			JSONObject json;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					newHandler.sendEmptyMessage(7);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				prepare();
				findView();
				is_collect(session_id, "5", id);
				break;
			case 2:
				dialog.dismiss();
				Toast.makeText(Economic_personalAct.this, "暂无数据", 500).show();
				break;
			case 3:
				iv_economic_personal_collect
						.setImageResource(R.drawable.collect_check);
				Toast.makeText(Economic_personalAct.this, "收藏成功", 500).show();
				break;
			case 4:
				iv_economic_personal_collect
						.setImageResource(R.drawable.collect_uncheck);
				Toast.makeText(Economic_personalAct.this, "收藏失败或者已收藏过", 500)
						.show();
				break;
			case 5:
				iv_economic_personal_collect
						.setImageResource(R.drawable.collect_check);
				break;
			case 6:
				iv_economic_personal_collect
						.setImageResource(R.drawable.collect_uncheck);
				break;
			case 7:
				iv_economic_personal_collect
						.setImageResource(R.drawable.collect_uncheck);
				break;
			case 99:
				dialog.dismiss();
				Toast.makeText(Economic_personalAct.this, "网络异常，请尝试下拉刷新", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	@Override
	public void doError(String s) {
		// TODO Auto-generated method stub
		if (s.equals("请求异常")) {
			newHandler.sendEmptyMessage(99);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// clean_list();
		clear_data();
		super.onDestroy();
	}

	// 初始化参数
	private void getDate() {
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

	// 清理arraylist,防止参数混肴
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Economic_personalAct.this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
