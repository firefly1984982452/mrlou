package com.mrlou.economic;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
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
import com.mrlou.h5.Building_Act;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class Economic_CompanyAct extends Activity implements HttpListener {

	private ArrayList<String> str_count, str_dw, str_type;
	private ArrayList<String> str_name, str_phone, str_year, str_company,
			str_deal, str_url, str_flag, str_status;
	private ArrayList<String> str_year1, str_deal2;
	private GridView gridView;
	private RectclerviewAdapter adapter;
	private Button bt_eco_compangy_lishi, bt_eco_compangy_agent;
	private ListView lv_economic_company;
	private Eco_CompanyListAdapter adapter2;
	private String name, note, phone, url, address, total_num, rent_num,
			sell_num, agent_totals, region_name, total, company,url1,url2,mrString ;
	private ArrayList<String> str_building_id, str_avatar_url,
			str_building_name, str_square_meter, str_cj_time, str_industry,
			str_transaction_type;
	private ArrayList<String> str_id, str_names, str_phones, str_companys,
			str_region_total, str_total, str_region_name, str_avatar,
			str_work_age;
	private ArrayList<String> str_cj_region_name, str_cj_total;
	private TextView tv_economic_company_content, tv_economic_company_address,
			tv_economic_company_phone, tv_economic_company_netaddress,
			tv_economic_company_title;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private ScrollView mScrollView;
	private SweetAlertDialog pDialog;
	private Progress_Dialog dialog;
	RecyclerView recyclerView;
	FullyLinearLayoutManager layoutManager;
	private boolean flags = false;
	private LinearLayout lay_economic_company_back;
	private ImageView iv_economiccompany_logo;
	private DisplayImageOptions options;
	private String avatar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.economic_company);
		Intent intent = getIntent();
		company = intent.getStringExtra("company");
		pDialog = new SweetAlertDialog(Economic_CompanyAct.this,
				SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
		dialog = new Progress_Dialog(Economic_CompanyAct.this, pDialog);
		dialog.dialog();
		init();
		//refresh();
		getDate();
		select_area(company);
		// prepare();
		// findView();

	}

	private void init() {
		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		recyclerView.setHasFixedSize(true);

		int spanCount = 1; // 只显示一行
		layoutManager = new FullyLinearLayoutManager(spanCount,
				StaggeredGridLayoutManager.HORIZONTAL);
		recyclerView.setLayoutManager(layoutManager);
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.logo) // 设置图片下载期间显示的图片
		.showImageForEmptyUri(R.drawable.logo) // 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.logo) // 设置图片加载或解码过程中发生错误显示的图片
		.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
//		.displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
		.build();
	}

	private void refresh() {
		// TODO Auto-generated method stub
		// 这几个刷新Label的设置
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollviews);
		mPullRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(
				"上次刷新为1分钟前");
		mPullRefreshScrollView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
		mPullRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel(
				"正在刷新...");
		mPullRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel("手松开刷新");

		// 上拉、下拉设定
		mPullRefreshScrollView.setMode(Mode.PULL_FROM_START);

		// 上拉监听函数
		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						select_area(company);
						// 执行刷新函数
						new GetDataTask().execute();
					}
				});

		// 获取ScrollView布局，此文中用不到
		mScrollView = mPullRefreshScrollView.getRefreshableView();
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
			// mPullRefreshScrollView.setMode(Mode.DISABLED);

			// 在更新UI后，无需其它Refresh操作，系统会自己加载新的listView
			mPullRefreshScrollView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

	private void prepare() {
		// TODO Auto-generated method stub
		str_count = new ArrayList<String>();
		str_count.addAll(str_cj_total);
		str_dw = new ArrayList<String>();
		str_dw.add("套");
		str_dw.add("㎡");
		int length = str_cj_total.size();
		if (length - 2 > 0) {
			for (int i = 0; i < length - 2; i++) {
				if(i==length-3){
			str_dw.add("");	
				}else {
				str_dw.add("套");
			}
			}
		}
		str_type = new ArrayList<String>();
		str_type.addAll(str_cj_region_name);
		str_year1 = new ArrayList<String>();
		for (int i = 0; i < str_square_meter.size(); i++) {
			str_year1.add(str_square_meter.get(i) + "    " + str_cj_time.get(i));
		}
		str_company = new ArrayList<String>();
		str_company.add("0");
		str_deal = new ArrayList<String>();
		str_deal.addAll(str_industry);
		str_flag = new ArrayList<String>();
		str_flag.add("0");
		str_name = new ArrayList<String>();
		str_name.addAll(str_building_name);
		str_phone = new ArrayList<String>();
		str_phone.addAll(str_phones);
		str_url = new ArrayList<String>();
		str_url.addAll(str_avatar_url);
		str_year = new ArrayList<String>();
		str_year.addAll(str_year1);
		str_status = new ArrayList<String>();
		str_status.addAll(str_transaction_type);
		str_deal2 = new ArrayList<String>();
		for (int i = 0; i < str_total.size(); i++) {
			str_deal2.add("最新成交" + str_total.get(i));
		}
	}

	private void findView() {
		// TODO Auto-generated method stub
		iv_economiccompany_logo=(ImageView) findViewById(R.id.iv_economiccompany_logo);
		iv_economiccompany_logo.setImageResource(R.drawable.logo);
		ImageLoader.getInstance().displayImage(avatar, iv_economiccompany_logo, options);
		lay_economic_company_back = (LinearLayout) findViewById(R.id.lay_economic_company_back);
		lay_economic_company_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		tv_economic_company_title = (TextView) findViewById(R.id.tv_economic_company_title);
		tv_economic_company_title.setText(name);
		tv_economic_company_content = (TextView) findViewById(R.id.tv_economic_company_content);
		tv_economic_company_content
				.setText("公司简介:"
						+ note);
		tv_economic_company_address = (TextView) findViewById(R.id.tv_economic_company_address);
		if(address.equals("")){
			tv_economic_company_address.setVisibility(View.GONE);
			}else{
				tv_economic_company_address.setText("公司地址:" + address);
				}
		
		tv_economic_company_phone = (TextView) findViewById(R.id.tv_economic_company_phone);
		if(phone.equals("")){
			tv_economic_company_phone.setVisibility(View.GONE);
			}else{
				tv_economic_company_phone.setText("服务电话:" + phone);
				}
		tv_economic_company_netaddress = (TextView) findViewById(R.id.tv_economic_company_netaddress);
		if(url.equals("")){
			tv_economic_company_netaddress.setVisibility(View.GONE);
			}else{
				tv_economic_company_netaddress.setText("公司网址:" + url);
				}
		// gridView = (GridView) findViewById(R.id.grid_company);
		// int size = str_count.size();
		// int length = 140;
		// DisplayMetrics dm = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(dm);
		// float density = dm.density;
		// int gridviewWidth = (int) (size * (length + 4) * density);
		// int itemWidth = (int) (length * density);
		// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		// LinearLayout.LayoutParams.WRAP_CONTENT,
		// LinearLayout.LayoutParams.FILL_PARENT);
		// gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
		// gridView.setColumnWidth(itemWidth); // 设置列表项宽
		// gridView.setHorizontalSpacing(5); // 设置列表项水平间距
		// gridView.setVerticalSpacing(2);
		// gridView.setGravity(Gravity.RIGHT);
		// gridView.setStretchMode(GridView.NO_STRETCH);// 设置缩放
		// gridView.setNumColumns(size);
		adapter = new RectclerviewAdapter(Economic_CompanyAct.this, str_count,
				str_dw, str_type);
		// gridView.setAdapter(adapter);

		recyclerView.setAdapter(adapter);

		lv_economic_company = (ListView) findViewById(R.id.lv_economic_company);
		adapter2 = new Eco_CompanyListAdapter(Economic_CompanyAct.this,
				str_url, str_flag, str_name, str_phone, str_year, str_company,
				str_deal, str_status);
		lv_economic_company.setAdapter(adapter2);

		bt_eco_compangy_lishi = (Button) findViewById(R.id.bt_eco_company_lishi);
		bt_eco_compangy_lishi.setText("历史成交(租" + rent_num + "售" + sell_num
				+ ")");
		bt_eco_compangy_lishi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clear();
				flags = false;
				bt_eco_compangy_agent
						.setBackgroundResource(R.drawable.corner_right_gray);
				bt_eco_compangy_lishi
						.setBackgroundResource(R.drawable.corner_left_black);
				
				str_flag.add("0");
				str_company.add("0");
				str_deal.addAll(str_industry);
				str_name.addAll(str_building_name);
				str_phone.addAll(str_phones);
				str_url.addAll(str_avatar_url);
				str_year.addAll(str_year1);
				str_status.addAll(str_transaction_type);
				adapter2.notifyDataSetChanged();
			}
		});
		bt_eco_compangy_agent = (Button) findViewById(R.id.bt_eco_company_agent);
		bt_eco_compangy_agent.setText("认证经纪人(" + total + ")");
		bt_eco_compangy_agent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				clear();
				flags = true;
				bt_eco_compangy_lishi
						.setBackgroundResource(R.drawable.corner_left_gray);
				bt_eco_compangy_agent
						.setBackgroundResource(R.drawable.corner_right_black);
				str_flag.add("1");
				str_company.addAll(str_companys);
				str_deal.addAll(str_deal2);
				str_url.addAll(str_avatar);
				str_name.addAll(str_names);
				str_phone.addAll(str_phones);
				str_year.addAll(str_work_age);
				str_status.add("99");
				adapter2.notifyDataSetChanged();
			}
		});

		lv_economic_company.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (flags) {
					Intent intent = new Intent(Economic_CompanyAct.this,
							Economic_personalAct.class);
					intent.putExtra("id", str_id.get(position));
					startActivity(intent);
				}else{
					mrString = Economic_CompanyAct.this.getExternalFilesDir(null).getPath();
					url = "file://"+mrString+"/bulidersir/lxs_index.html";
					url2 = "file://"+mrString+"/bulidersir/";
					Intent intent = new Intent(Economic_CompanyAct.this, Building_Act.class);
					intent.putExtra("url", url1);
					intent.putExtra("url2", url2);
					intent.putExtra("frsource", "gg");
					intent.putExtra("id", str_building_id.get(position));
					startActivity(intent);
					
					}
			}
		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// clear();
		clean_list();
		super.onDestroy();
	}

	public void clear() {
		str_name.clear();
		str_phone.clear();
		str_year.clear();
		str_company.clear();
		str_deal.clear();
		str_url.clear();
		str_flag.clear();
		str_status.clear();
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
				break;
			case 2:
				dialog.dismiss();
				break;
			case 99:
				dialog.dismiss();
				Toast.makeText(Economic_CompanyAct.this, "网络连接失败，请尝试下拉刷新", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC_COMPANY) {
			JSONObject json, json2, json3, json4, json5;
			JSONArray joArray, joArray2, joArray3;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					clean_list();
					json2 = json.getJSONObject("list");
					json3 = json2.getJSONObject("company_info");
					name = json3.getString("name");
					note = json3.getString("note");
					phone = json3.getString("phone");
					url = json3.getString("url");
					address = json3.getString("address");
					avatar=json3.getString("avatar");
					joArray = json2.getJSONArray("user_cj");
					int lenth = joArray.length();
					for (int i = 0; i < lenth; i++) {
						json = joArray.getJSONObject(i);
						str_cj_total.add(json.getString("total"));
						str_cj_region_name.add(json.getString("region_name"));
					}
					json4 = json2.getJSONObject("user_info");
					total = json4.getString("totals");
					if (!total.equals("0")) {
						joArray2 = json4.getJSONArray("list");
						int lenth2 = joArray2.length();
						for (int i = 0; i < lenth2; i++) {
							json = joArray2.getJSONObject(i);
							str_id.add(json.getString("id"));
							str_names.add(json.getString("cn_username")
									+ json.getString("en_username"));
							str_phones.add(json.getString("phone"));
							str_companys.add(json.getString("company"));
							str_region_total
									.add(json.getString("region_total"));
							str_total.add(json.getString("total"));
							str_region_name.add(json.getString("region_name"));
							str_avatar.add(json.getString("avatar"));
							str_work_age.add(json.getString("work_age"));
						}
					}

					json5 = json2.getJSONObject("cj_info");
					total_num = json5.getString("total_num");
					sell_num = json5.getString("sell_total");
					rent_num = json5.getString("rent_total");
					if (!total_num.equals("0")) {
						joArray3 = json5.getJSONArray("data");
						int lenth3 = joArray3.length();
						for (int i = 0; i < lenth3; i++) {
							json = joArray3.getJSONObject(i);
							str_avatar_url.add(json.getString("avatar_url"));
							str_building_id.add(json.getString("building_id"));
							str_building_name.add(json
									.getString("building_name"));
							str_cj_time.add(json.getString("cj_time"));
							str_industry.add(json.getString("industry"));
							str_square_meter
									.add(json.getString("square_meter"));
							str_transaction_type.add(json
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

	public void select_area(String name) {
		Request request = Request.requesteconomicompany(name);
		new HttpThread(request, this);
	}

	private void getDate() {

		str_avatar = new ArrayList<String>();
		str_avatar_url = new ArrayList<String>();
		str_building_id = new ArrayList<String>();
		str_building_name = new ArrayList<String>();
		str_cj_region_name = new ArrayList<String>();
		str_cj_time = new ArrayList<String>();
		str_cj_total = new ArrayList<String>();
		str_company = new ArrayList<String>();
		str_companys = new ArrayList<String>();
		str_id = new ArrayList<String>();
		str_industry = new ArrayList<String>();
		str_name = new ArrayList<String>();
		str_names = new ArrayList<String>();
		str_phone = new ArrayList<String>();
		str_phones = new ArrayList<String>();
		str_region_name = new ArrayList<String>();
		str_region_total = new ArrayList<String>();
		str_square_meter = new ArrayList<String>();
		str_total = new ArrayList<String>();
		str_transaction_type = new ArrayList<String>();
		str_work_age = new ArrayList<String>();
	}

	private void clean_list() {
		// TODO Auto-generated method stub
		str_avatar.clear();
		str_avatar_url.clear();
		str_building_id.clear();
		str_building_name.clear();
		str_cj_region_name.clear();
		str_cj_time.clear();
		str_cj_total.clear();
		str_company.clear();
		str_id.clear();
		str_industry.clear();
		str_name.clear();
		str_phone.clear();
		str_region_name.clear();
		str_region_total.clear();
		str_square_meter.clear();
		str_total.clear();
		str_transaction_type.clear();
		str_work_age.clear();
	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
			inst.sendKeyDownUpSync(KeyCode);
			Economic_CompanyAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
			Economic_CompanyAct.this.finish();
        }
		return super.onKeyDown(keyCode, event);
	}
}
