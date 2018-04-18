package com.mrlou.mine;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.mrlou.adapter.Hourse_FollowAdapter;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class Customer_DetailAct extends Activity implements HttpListener {

	private LinearLayout lay_customer_logadd, lay_customer_edit,
			lay_customer_detail_back, lay_customer_detail_main,lay_customer_phone_contact;
	private LinearLayout lay_icon01, lay_icon02, lay_icon03, lay_icon04;
	private ImageView iv_icon01, iv_icon02, iv_icon03, iv_icon04;
	private ListView lv;
	private TextView tv_customer_name, tv_customer_follow_name,
			tv_customer_follow_time, tv_customer_follow_union,
			tv_customer_follow_job;
	private Hourse_FollowAdapter adapter;
	private ArrayList<String> str_date, str_content, str_id, str_title,
			str_status, str_is_read, str_notify_time, str_event_date,
			str_picture, str_picture_id;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private ScrollView mScrollView;
	private SharedPreferences sharedPreferences;
	private int num = 1;
	private String session_id, customer_id, id, name, transaction_type,
			first_access_time, sector, pre_square_meter, pre_room, pre_price_1,
			intro, process, phone_number, company, job_title, use_for,
			company_property, employee_total, current_building, expire_time,
			type, images_id, create_time, update_time;
	private ImageView iv_customer_follow_pic;
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_customer_detail);
		prepare();
		Intent intent = getIntent();
		customer_id = intent.getStringExtra("id");
		sharedPreferences = Customer_DetailAct.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		select_standoffice(customer_id, "1", session_id);
		ImageLoader.getInstance()
				.init(ImageLoaderConfiguration
						.createDefault(Customer_DetailAct.this));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.logo) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.logo) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.logo) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
				.build();
		// findView();
	}

	private void prepare() {
		// TODO Auto-generated method stub
		str_date = new ArrayList<String>();
		str_content = new ArrayList<String>();
		str_id = new ArrayList<String>();
		str_title = new ArrayList<String>();
		str_status = new ArrayList<String>();
		str_is_read = new ArrayList<String>();
		str_notify_time = new ArrayList<String>();
		str_event_date = new ArrayList<String>();
		str_picture = new ArrayList<String>();
		str_picture_id = new ArrayList<String>();
	}

	private void refresh() {
		// TODO Auto-generated method stub
		// 这几个刷新Label的设置
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_customer_info);
		mPullRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(
				"上次刷新为1分钟前");
		mPullRefreshScrollView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
		mPullRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel(
				"正在刷新...");
		mPullRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel("手松开刷新");

		// 上拉、下拉设定
		mPullRefreshScrollView.setMode(Mode.PULL_FROM_END);

		// 上拉监听函数
		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						num = num + 1;
						select_standoffice(customer_id, "" + num, session_id);
						// Toast.makeText(Customer_DetailAct.this, "测试下拉", Toast.LENGTH_SHORT)
						// .show();
						// select_standoffice("1");
						// 执行刷新函数
						new GetDataTask().execute();
					}
				});

		// 获取ScrollView布局
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

	private void findView() {
		// TODO Auto-generated method stub
		if (str_picture_id.size() == 1) {
			images_id = str_picture_id.get(0);
		} else if (str_picture_id.size() > 1) {
			for (int i = 0; i < str_picture_id.size(); i++) {
				if (i == 0) {
					images_id = str_picture_id.get(0);
				} else {
					images_id = images_id + "," + str_picture_id.get(i);
				}
			}
		}
		if (str_date.size() >= 20) {
			refresh();
		}
		iv_customer_follow_pic = (ImageView) findViewById(R.id.iv_customer_follow_pic);

		tv_customer_name = (TextView) findViewById(R.id.tv_customer_name);
		tv_customer_name.setText("客户跟踪");
		tv_customer_follow_name = (TextView) findViewById(R.id.tv_customer_follow_name);
		tv_customer_follow_name.setText(name);
		tv_customer_follow_time = (TextView) findViewById(R.id.tv_customer_follow_time);
		tv_customer_follow_time.setText("录入:" + create_time + "  更新:"
				+ update_time);
		tv_customer_follow_job = (TextView) findViewById(R.id.tv_customer_follow_job);
		tv_customer_follow_job.setText(sector);
		lay_customer_phone_contact= (LinearLayout) findViewById(R.id.lay_customer_phone_contact);
		lay_customer_phone_contact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (phone_number.equals("")) {
					Toast.makeText(Customer_DetailAct.this, "您没有给该客户添加联系电话", Toast.LENGTH_SHORT).show();
				}else {
					Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
							+ phone_number));
					startActivity(intent);
				}
			}
		});
		tv_customer_follow_union = (TextView) findViewById(R.id.tv_customer_follow_union);
		if (transaction_type.equals("0")) {
			tv_customer_follow_union.setText("需求:" + pre_square_meter + "   ¥"
					+ pre_price_1 + "元/㎡/天");
		} else {
			tv_customer_follow_union.setText("需求:" + pre_square_meter + "   ¥"
					+ pre_price_1 + "元/㎡");
		}
		iv_customer_follow_pic = (ImageView) findViewById(R.id.iv_customer_follow_pic);
		if (str_picture.size() > 0) {
			iv_customer_follow_pic.setImageResource(R.drawable.logo);
			ImageLoader.getInstance().displayImage(str_picture.get(0),
					iv_customer_follow_pic, options);
		}
		lay_icon01 = (LinearLayout) findViewById(R.id.lay_customer_detail_icon01);
		lay_icon01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				process = "1";
				selecticon(1);
				select_customeredit(session_id, id, name, transaction_type,
						first_access_time, job_title, pre_square_meter,
						pre_room, pre_price_1, "㎡", intro, process,
						phone_number, company, job_title, use_for,
						company_property, employee_total, current_building,
						expire_time, images_id);
			}
		});
		lay_icon02 = (LinearLayout) findViewById(R.id.lay_customer_detail_icon02);
		lay_icon02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				process = "2";
				selecticon(2);
				select_customeredit(session_id, id, name, transaction_type,
						first_access_time, job_title, pre_square_meter,
						pre_room, pre_price_1, "㎡", intro, process,
						phone_number, company, job_title, use_for,
						company_property, employee_total, current_building,
						expire_time, images_id);
			}
		});
		lay_icon03 = (LinearLayout) findViewById(R.id.lay_customer_detail_icon03);
		lay_icon03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				process = "3";
				selecticon(3);
				select_customeredit(session_id, id, name, transaction_type,
						first_access_time, job_title, pre_square_meter,
						pre_room, pre_price_1, "㎡", intro, process,
						phone_number, company, job_title, use_for,
						company_property, employee_total, current_building,
						expire_time, images_id);
			}
		});
		lay_icon04 = (LinearLayout) findViewById(R.id.lay_customer_detail_icon04);
		lay_icon04.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				process = "4";
				selecticon(4);
				select_customeredit(session_id, id, name, transaction_type,
						first_access_time, job_title, pre_square_meter,
						pre_room, pre_price_1, "㎡", intro, process,
						phone_number, company, job_title, use_for,
						company_property, employee_total, current_building,
						expire_time, images_id);
			}
		});
		iv_icon01 = (ImageView) findViewById(R.id.customer_detail_icon01);
		iv_icon02 = (ImageView) findViewById(R.id.customer_detail_icon02);
		iv_icon03 = (ImageView) findViewById(R.id.customer_detail_icon03);
		iv_icon04 = (ImageView) findViewById(R.id.customer_detail_icon04);
		if (process.equals("1")) {
			selecticon(1);
		} else if (process.equals("2")) {
			selecticon(2);
		} else if (process.equals("3")) {
			selecticon(3);
		} else {
			selecticon(4);
		}
		lay_customer_detail_back = (LinearLayout) findViewById(R.id.lay_customer_detail_back);
		lay_customer_detail_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		lay_customer_edit = (LinearLayout) findViewById(R.id.lay_customer_edit);
		lay_customer_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Customer_DetailAct.this,
						Customer_InfoeditAct.class);
				intent.putExtra("session_id", session_id);
				intent.putExtra("name", name);
				intent.putExtra("id", id);
				intent.putExtra("transaction_type", transaction_type);
				intent.putExtra("first_access_time", first_access_time);
				intent.putExtra("sector", sector);
				intent.putExtra("pre_square_meter", pre_square_meter);
				intent.putExtra("pre_room", pre_room);
				intent.putExtra("pre_price_1", pre_price_1);
				intent.putExtra("intro", intro);
				intent.putExtra("process", process);
				intent.putExtra("phone_number", phone_number);
				intent.putExtra("company", company);
				intent.putExtra("job_title", job_title);
				intent.putExtra("use_for", use_for);
				intent.putExtra("company_property", company_property);
				intent.putExtra("employee_total", employee_total);
				intent.putExtra("current_building", current_building);
				intent.putExtra("expire_time", expire_time);
				intent.putExtra("customer_id", customer_id);
				intent.putExtra("type", type);
				intent.putStringArrayListExtra("images_id", str_picture_id);
				intent.putStringArrayListExtra("images_url", str_picture);
				startActivity(intent);
				Customer_DetailAct.this.finish();
			}
		});
		lay_customer_logadd = (LinearLayout) findViewById(R.id.lay_customer_logadd);
		lay_customer_logadd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Customer_DetailAct.this,
						Job_AddAct.class);
				intent.putExtra("target_type", "5");
				intent.putExtra("id", id);
				startActivity(intent);
			}
		});
		lv = (ListView) findViewById(R.id.lv_customer_detail);
		adapter = new Hourse_FollowAdapter(this, str_date, str_content);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Customer_DetailAct.this,
						Job_AddEditAct.class);
				intent.putExtra("target_type", "5");
				intent.putExtra("id", str_id.get(position));
				intent.putExtra("target_id", customer_id);
				intent.putExtra("time", str_event_date.get(position));
				intent.putExtra("notify_time", str_notify_time.get(position));
				intent.putExtra("content", str_content.get(position));
				startActivity(intent);
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
			iv_icon03.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon04.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			break;
		case 2:
			iv_icon01.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon02.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_checked));
			iv_icon03.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon04.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			break;
		case 3:
			iv_icon01.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon02.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon03.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_checked));
			iv_icon04.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			break;
		case 4:
			iv_icon01.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon02.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon03.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon04.setBackgroundDrawable(getResources().getDrawable(
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
					Customer_DetailAct.this.finish();
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
				// lv_search.setVisibility(View.VISIBLE);
				findView();
				adapter.notifyDataSetChanged();
				break;
			case 2:
				Toast.makeText(Customer_DetailAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(Customer_DetailAct.this, "修改成功", Toast.LENGTH_SHORT).show();
				break;
			case 4028:
				Toast.makeText(Customer_DetailAct.this, "客户不存在", Toast.LENGTH_SHORT).show();
				break;
			case 4029:
				Toast.makeText(Customer_DetailAct.this, "注释", Toast.LENGTH_SHORT).show();
				break;
			case 4031:
				Toast.makeText(Customer_DetailAct.this, "交易类型有误", Toast.LENGTH_SHORT).show();
				break;
			case 4032:
				Toast.makeText(Customer_DetailAct.this, "首次到访日期有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4033:
				Toast.makeText(Customer_DetailAct.this, "客户名称有误", Toast.LENGTH_SHORT).show();
				break;
			case 4034:
				Toast.makeText(Customer_DetailAct.this, "行业类别有误", Toast.LENGTH_SHORT).show();
				break;
			case 4035:
				Toast.makeText(Customer_DetailAct.this, "意向面积有误", Toast.LENGTH_SHORT).show();
				break;
			case 4036:
				Toast.makeText(Customer_DetailAct.this, "意向房源有误", Toast.LENGTH_SHORT).show();
				break;
			case 4037:
				Toast.makeText(Customer_DetailAct.this, "价格预算有误", Toast.LENGTH_SHORT).show();
				break;
			case 4038:
				Toast.makeText(Customer_DetailAct.this, "价格预算单位有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4039:
				Toast.makeText(Customer_DetailAct.this, "房源简介有误", Toast.LENGTH_SHORT).show();
				break;
			case 4040:
				Toast.makeText(Customer_DetailAct.this, "联系电话有误", Toast.LENGTH_SHORT).show();
				break;
			case 4041:
				Toast.makeText(Customer_DetailAct.this, "公司名称有误", Toast.LENGTH_SHORT).show();
				break;
			case 4042:
				Toast.makeText(Customer_DetailAct.this, "公司职位有误", Toast.LENGTH_SHORT).show();
				break;
			case 4043:
				Toast.makeText(Customer_DetailAct.this, "租售动机有误", Toast.LENGTH_SHORT).show();
				break;
			case 4044:
				Toast.makeText(Customer_DetailAct.this, "单位性质有误", Toast.LENGTH_SHORT).show();
				break;
			case 4045:
				Toast.makeText(Customer_DetailAct.this, "员工数量有误", Toast.LENGTH_SHORT).show();
				break;
			case 4046:
				Toast.makeText(Customer_DetailAct.this, "目前所在写字楼有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4047:
				Toast.makeText(Customer_DetailAct.this, "租约到期日有误", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(Customer_DetailAct.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public void select_standoffice(String id, String page, String session_id) {
		Request request = Request.requestcustomerinfo(id, page, session_id);
		new HttpThread(request, this);
	}

	public void select_customeredit(String session_id, String id, String name,
			String transaction_type, String first_access_time, String sector,
			String pre_square_meter, String pre_room, String pre_price_1,
			String pre_price_unit_1, String intro, String process,
			String phone_number, String company, String job_title,
			String use_for, String company_property, String employee_total,
			String current_building, String expire_time, String images_id) {
		Request request = Request.requestcustomerinfoedit(session_id, id, name,
				transaction_type, first_access_time, sector, pre_square_meter,
				pre_room, pre_price_1, pre_price_unit_1, intro, process,
				phone_number, company, job_title, use_for, company_property,
				employee_total, current_building, expire_time, images_id);
		new HttpThread(request, this);
	}

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC) {
			JSONObject json, json2;
			JSONArray joArray, joArray2, joArray3;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					json2 = json.getJSONObject("data");
					name = json2.getString("name");
					id = json2.getString("id");
					transaction_type = json2.getString("transaction_type");
					first_access_time = json2.getString("first_access_time");
					sector = json2.getString("sector");
					pre_square_meter = json2.getString("pre_square_meter");
					pre_room = json2.getString("pre_room");
					pre_price_1 = json2.getString("pre_price_1");
					intro = json2.getString("intro");
					process = json2.getString("process");
					phone_number = json2.getString("phone_number");
					company = json2.getString("company");
					job_title = json2.getString("job_title");
					use_for = json2.getString("use_for");
					company_property = json2.getString("company_property");
					employee_total = json2.getString("employee_total");
					current_building = json2.getString("current_building");
					expire_time = json2.getString("expire_time");
					create_time = json2.getString("create_time");
					update_time = json2.getString("update_time");
					// type=json2.getString("type");
					joArray = json2.getJSONArray("work_log");
					int len = joArray.length();
					for (int i = 0; i < len; i++) {
						json = joArray.getJSONObject(i);
						str_id.add(json.getString("id"));
						str_title.add(json.getString("title"));
						str_content.add(json.getString("content"));
						str_event_date.add(json.getString("event_date"));
						str_status.add(json.getString("status"));
						str_is_read.add(json.getString("is_read"));
						str_notify_time.add(json.getString("notify_time"));
						str_date.add(json.getString("event_date") + "  "
								+ json.getString("week_no"));
					}
					joArray2 = json2.getJSONArray("pdata");
					for (int i = 0; i < joArray2.length(); i++) {
						json = joArray2.getJSONObject(i);
						str_picture.add(json.getString("picture"));
						str_picture_id.add(json.getString("picture_id"));
					}
					newHandler.sendEmptyMessage(1);
				} else if (errorid.equals("1")) {
					newHandler.sendEmptyMessage(2);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (reqID == Request.REQUEST_ECONOMIC_COLLECT) {
			JSONObject json;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					newHandler.sendEmptyMessage(3);
				} else if (errorid.equals("1")) {
					newHandler.sendEmptyMessage(2);
				} else if (errorid.equals("4028")) {
					newHandler.sendEmptyMessage(4028);

				} else if (errorid.equals("4029")) {
					newHandler.sendEmptyMessage(4029);

				} else if (errorid.equals("4031")) {
					newHandler.sendEmptyMessage(4031);

				} else if (errorid.equals("4032")) {
					newHandler.sendEmptyMessage(4032);

				} else if (errorid.equals("4033")) {
					newHandler.sendEmptyMessage(4033);

				} else if (errorid.equals("4034")) {
					newHandler.sendEmptyMessage(4034);

				} else if (errorid.equals("4035")) {
					newHandler.sendEmptyMessage(4035);

				} else if (errorid.equals("4036")) {
					newHandler.sendEmptyMessage(4036);

				} else if (errorid.equals("4037")) {
					newHandler.sendEmptyMessage(4037);

				} else if (errorid.equals("4038")) {
					newHandler.sendEmptyMessage(4038);

				} else if (errorid.equals("4039")) {
					newHandler.sendEmptyMessage(4039);

				} else if (errorid.equals("4040")) {
					newHandler.sendEmptyMessage(4040);

				} else if (errorid.equals("4041")) {
					newHandler.sendEmptyMessage(4041);

				} else if (errorid.equals("4042")) {
					newHandler.sendEmptyMessage(4042);

				} else if (errorid.equals("4043")) {
					newHandler.sendEmptyMessage(4043);

				} else if (errorid.equals("4044")) {
					newHandler.sendEmptyMessage(4044);

				} else if (errorid.equals("4045")) {
					newHandler.sendEmptyMessage(4045);

				} else if (errorid.equals("4046")) {
					newHandler.sendEmptyMessage(4046);

				} else {
					newHandler.sendEmptyMessage(4047);

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
