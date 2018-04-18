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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
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
import com.mrlou.economic.view.BadgeView;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class Hourse_FollowAct extends Activity implements HttpListener {

	private LinearLayout lay_hourse_follow, lay_hourse_logadd,
			lay_hourse_follow_back, lay_mine_hourse_edit,
			lay_hourse_follow_pic;
	private LinearLayout lay_icon01, lay_icon02, lay_icon03, lay_icon04;
	private ImageView iv_icon01, iv_icon02, iv_icon03, iv_icon04,
			iv_hourse_follow_pic;
	private ListView lv;
	private TextView tv_hourse_follow_name, tv_hourse_follow_union,
			tv_hourse_follow_time;
	private Hourse_FollowAdapter adapter;
	private SharedPreferences sharedPreferences;
	private String id, category, building_id, building_name, avatar, avatar_id,
			transaction_type, unit_no, status, verified, image_total,
			rent_price, rent_price_unit="", sell_price, sell_price_unit="", room_price_unit,head_to,
			delivery_date, delivery_status, intro, square_meter, update_time,
			position, min_rent_period, contact_phone, user_type;
	private ArrayList<String> str_id, str_event_date, str_week_no, str_title,
			str_content, str_status, str_notify_time, str_images_id, str_date,
			str_images_url,str_land_id,str_land;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private String hourse_id, session_id, hourse_status, unique = "0", land,
			land_id, fee_rate,images_id,price,release_time,is_click="0",val_total,click_status;
	private int num = 0;
	private BadgeView badgeView, badgeView2;
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_hourse_follow);
		Intent intent = getIntent();
		hourse_id = intent.getStringExtra("id");
		sharedPreferences = Hourse_FollowAct.this.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		prepare();
		select_standoffice(session_id, hourse_id, "1");
		// findView();
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
						select_standoffice(hourse_id, "" + num, session_id);
						// 执行刷新函数
						new GetDataTask().execute();
					}
				});

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
		str_date = new ArrayList<String>();
		str_id = new ArrayList<String>();
		str_event_date = new ArrayList<String>();
		str_week_no = new ArrayList<String>();
		str_title = new ArrayList<String>();
		str_content = new ArrayList<String>();
		str_status = new ArrayList<String>();
		str_notify_time = new ArrayList<String>();
		str_images_id = new ArrayList<String>();
		str_images_url = new ArrayList<String>();
		str_land_id=new ArrayList<String>();
		str_land=new ArrayList<String>();
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(Hourse_FollowAct.this));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.logo) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.logo) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.logo) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
				.build();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_mine_hourse_edit=(LinearLayout) findViewById(R.id.lay_mine_hourse_edit);
		lay_mine_hourse_edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if (status.equals("1") || status.equals("2")) {
					Intent intent = new Intent(Hourse_FollowAct.this,
							Hourse_InfoeditAct.class);
					intent.putExtra("session_id", session_id);
					intent.putExtra("building_name", building_name);
					intent.putExtra("hourse_id", hourse_id);
					intent.putExtra("category", category);
					intent.putExtra("id", id);
					intent.putExtra("transaction_type", transaction_type);
					intent.putExtra("position", position);
					intent.putExtra("square_meter", square_meter);
					intent.putExtra("rent_price", rent_price);
					intent.putExtra("sell_price", sell_price);
					intent.putExtra("rent_price_unit", rent_price_unit);
					intent.putExtra("sell_price_unit", sell_price_unit);
					intent.putExtra("head_to", head_to);
					intent.putExtra("delivery_status", delivery_status);
					intent.putExtra("delivery_date", delivery_date);
					intent.putExtra("fee_rate", fee_rate);
					intent.putExtra("intro", intro);
					intent.putExtra("contact_phone", contact_phone);
					intent.putExtra("unit_no", unit_no);
					intent.putExtra("position", position);
					intent.putExtra("status", status);
					intent.putExtra("avatar", avatar);
					intent.putExtra("avatar_id", avatar_id);
//					intent.putExtra("land", land);
					intent.putExtra("min_rent_period", min_rent_period);
//					intent.putExtra("land_id", land_id);
					intent.putExtra("user_type", user_type);
					intent.putStringArrayListExtra("images_url", str_images_url);
					intent.putStringArrayListExtra("images_id", str_images_id);
					intent.putStringArrayListExtra("land", str_land);
					intent.putStringArrayListExtra("land_id", str_land_id);
					startActivity(intent);
					Hourse_FollowAct.this.finish();
//				}else {
//					Toast.makeText(Hourse_FollowAct.this, "成交或者失效状态不能编辑", Toast.LENGTH_SHORT).show();
//				}
			}
		});
		tv_hourse_follow_name = (TextView) findViewById(R.id.tv_hourse_follow_name);
		tv_hourse_follow_name.setText(building_name);
		tv_hourse_follow_union = (TextView) findViewById(R.id.tv_hourse_follow_union);
		
		if (transaction_type.equals("1")) {
			if (category.equals("4")||category.equals("5")) {
				tv_hourse_follow_union.setText(unit_no + "," +"￥" + rent_price
						+ rent_price_unit);
			}else {
				tv_hourse_follow_union.setText(unit_no + ","+square_meter+"㎡ ￥" + rent_price
						+ rent_price_unit);
			}
			
		} else {
			if (category.equals("4")||category.equals("5")) {
				tv_hourse_follow_union.setText(unit_no + "," + " ￥"  + sell_price
						+ sell_price_unit);
			}else {
				tv_hourse_follow_union.setText(unit_no + ","+square_meter+"㎡ ￥"  + sell_price
						+ sell_price_unit);
			}
			
		}
		tv_hourse_follow_time = (TextView) findViewById(R.id.tv_hourse_follow_time);
		tv_hourse_follow_time.setText("发布:  " + release_time + "  "
				+ "  更新:  " + update_time);
		lay_hourse_follow_pic = (LinearLayout) findViewById(R.id.lay_hourse_follow_pic);
		badgeView = new BadgeView(Hourse_FollowAct.this, lay_hourse_follow_pic);
		badgeView.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
		badgeView.setBackgroundResource(R.drawable.sole);
		if (unique.equals("1")) {
			badgeView.show();
		}
		iv_hourse_follow_pic = (ImageView) findViewById(R.id.iv_hourse_follow_pic);
		iv_hourse_follow_pic.setImageResource(R.drawable.ic_launcher);
		// imageLoader.loadImage(avatar, 120, 120, iv_hourse_follow_pic);
		ImageLoader.getInstance().displayImage(avatar, iv_hourse_follow_pic,
				options);
		badgeView2 = new BadgeView(Hourse_FollowAct.this, iv_hourse_follow_pic);
		// badgeView2.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
		LayoutParams laParams = (LayoutParams) iv_hourse_follow_pic
				.getLayoutParams();
		int height = laParams.height;
		badgeView2.setGravity(Gravity.BOTTOM);
		badgeView2.setBadgeMargin(0, height - 20);
		badgeView2.setBackgroundResource(R.drawable.authentication);
		if (verified.equals("1")) {
			badgeView2.show();
		}
		if (str_content.size() >= 20) {
			refresh();
		}
		// 遍历所有子控件 添加字体
		lay_icon01 = (LinearLayout) findViewById(R.id.lay_hourse_follow_icon01);
		lay_icon01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (is_click.equals("1")) {
					click_status="1";
//					status = "1";
//					selecticon(1);
					select_customeradd(session_id, id, category, unit_no, square_meter,
							transaction_type, price, position, head_to, delivery_status,
							delivery_date, fee_rate, min_rent_period, contact_phone, "",
							intro, "1", status,
							avatar_id, images_id, land_id);
				}else {
					Toast.makeText(Hourse_FollowAct.this, "您发布的房源已超过三个月，系统已将该房源自动下架，请重新添加发布", Toast.LENGTH_SHORT).show();
				}
			}
		});
		lay_icon02 = (LinearLayout) findViewById(R.id.lay_hourse_follow_icon02);
		lay_icon02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (is_click.equals("1")) {
					click_status="2";
//					status = "2";
//					selecticon(2);
					select_customeradd(session_id, id, category, unit_no, square_meter,
							transaction_type, price, position, head_to, delivery_status,
							delivery_date, fee_rate, min_rent_period, contact_phone, "",
							intro, "1", status,
							avatar_id, images_id, land_id);
				}else {
					Toast.makeText(Hourse_FollowAct.this, "您发布的房源已超过三个月，系统已将该房源自动下架，请重新添加发布", Toast.LENGTH_SHORT).show();
				}
			}
		});
		lay_icon03 = (LinearLayout) findViewById(R.id.lay_hourse_follow_icon03);
		lay_icon03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (is_click.equals("1")||is_click.equals("2")) {
					click_status="3";
//					status = "3";
//					selecticon(3);
					select_customeradd(session_id, id, category, unit_no, square_meter,
							transaction_type, price, position, head_to, delivery_status,
							delivery_date, fee_rate, min_rent_period, contact_phone, "",
							intro, "1", status,
							avatar_id, images_id, land_id);
				}else {
					Toast.makeText(Hourse_FollowAct.this, "您发布的房源已超过三个月，系统已将该房源自动下架，请重新添加发布", Toast.LENGTH_SHORT).show();
				}
			}
		});
		lay_icon04 = (LinearLayout) findViewById(R.id.lay_hourse_follow_icon04);
		lay_icon04.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					click_status="4";
//					status = "4";
//					selecticon(4);
					select_customeradd(session_id, id, category, unit_no, square_meter,
							transaction_type, price, position, head_to, delivery_status,
							delivery_date, fee_rate, min_rent_period, contact_phone, "",
							intro, "1", status,
							avatar_id, images_id, land_id);
			}
		});
		
		if (str_images_id.size() == 1) {
			images_id = str_images_id.get(0);
		} else if (str_images_id.size() > 1) {
			for (int i = 0; i < str_images_id.size(); i++) {
				if (i == 0) {
					images_id = str_images_id.get(0);
				} else {
					images_id = images_id + "," + str_images_id.get(i);
				}
			}
		}
		
		if (str_land_id.size()==1) {
			land_id = str_land_id.get(0);
		} else if (str_land_id.size() > 1) {
			for (int i = 0; i < str_land_id.size(); i++) {
				if (i == 0) {
					land_id = str_land_id.get(0);
				} else {
					land_id = land_id + "," + str_land_id.get(i);
				}
			}
		}
		
		iv_icon01 = (ImageView) findViewById(R.id.iv_hourse_follow_icon01);
		iv_icon02 = (ImageView) findViewById(R.id.iv_hourse_follow_icon02);
		iv_icon03 = (ImageView) findViewById(R.id.iv_hourse_follow_icon03);
		iv_icon04 = (ImageView) findViewById(R.id.iv_hourse_follow_icon04);
		if (status.equals("1")) {
			click_status="1";
			selecticon(1);
		} else if (status.equals("2")) {
			click_status="2";
			selecticon(2);
		} else if (status.equals("3")) {
			click_status="3";
			selecticon(3);
		} else {
			click_status="4";
			selecticon(4);
		}
		if (transaction_type.equals("1")) {
			price=rent_price;
		}else {
			price=sell_price;
		}
		lay_hourse_follow_back = (LinearLayout) findViewById(R.id.lay_hourse_follow_back);
		lay_hourse_follow_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		lay_hourse_logadd = (LinearLayout) findViewById(R.id.lay_hourse_logadd);
		lay_hourse_logadd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Hourse_FollowAct.this,
						Job_AddAct.class);
				intent.putExtra("target_type", "3");
				intent.putExtra("id", hourse_id);
				startActivity(intent);
			}
		});
		lay_hourse_follow = (LinearLayout) findViewById(R.id.lay_hourse_follow);
		lay_hourse_follow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				select_customeradd(session_id, id, category, unit_no, square_meter,
						transaction_type, price, position, head_to, delivery_status,
						delivery_date, fee_rate, min_rent_period, contact_phone, "",
						intro, "1", status,
						avatar_id, images_id, land_id);
			}
		});

		AnimationSet set = new AnimationSet(false);
		Animation animation = new AlphaAnimation(0, 1); // AlphaAnimation
														// 控制渐变透明的动画效果
		animation.setDuration(200); // 动画时间毫秒数
		set.addAnimation(animation); // 加入动画集合
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 1);

		lv = (ListView) findViewById(R.id.lv_hourse_follow);
		adapter = new Hourse_FollowAdapter(this, str_date, str_content);
		lv.setAdapter(adapter);
		lv.setLayoutAnimation(controller);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Hourse_FollowAct.this,
						Job_AddEditAct.class);
				intent.putExtra("target_type", "3");
				intent.putExtra("id", str_id.get(position));
				intent.putExtra("target_id", hourse_id);
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
					Hourse_FollowAct.this.finish();
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
				Toast.makeText(Hourse_FollowAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				if (click_status.equals("1")) {
					status = "1";
					selecticon(1);
				}else if (click_status.equals("2")) {
					status = "2";
					selecticon(2);
				}else if (click_status.equals("3")) {
					status = "3";
					selecticon(3);
				}else {
					status = "4";
					selecticon(4);
				}
				Toast.makeText(Hourse_FollowAct.this, "修改成功", Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(Hourse_FollowAct.this, "您好，你已发布最多"+val_total+"条房源数，如需发布更多房源，请联系客服咨询", Toast.LENGTH_SHORT).show();
				break;
			case 4067:
				Toast.makeText(Hourse_FollowAct.this, "房源不存在", Toast.LENGTH_SHORT).show();
				break;
			case 4088:
				Toast.makeText(Hourse_FollowAct.this, "发布人信息有误", Toast.LENGTH_SHORT).show();
				break;
			case 4068:
				Toast.makeText(Hourse_FollowAct.this, "房源不存在", Toast.LENGTH_SHORT).show();
				break;
			case 4058:
				Toast.makeText(Hourse_FollowAct.this, "房源单元有误", Toast.LENGTH_SHORT).show();
				break;
			case 4059:
				Toast.makeText(Hourse_FollowAct.this, "交易类型有误", Toast.LENGTH_SHORT).show();
				break;
			case 4061:
				Toast.makeText(Hourse_FollowAct.this, "房源位置有误", Toast.LENGTH_SHORT).show();
				break;
			case 4074:
				Toast.makeText(Hourse_FollowAct.this, "房源朝向有误", Toast.LENGTH_SHORT).show();
				break;
			case 4075:
				Toast.makeText(Hourse_FollowAct.this, "交付状态有误", Toast.LENGTH_SHORT).show();
				break;
			case 4030:
				Toast.makeText(Hourse_FollowAct.this, "最短租约有误", Toast.LENGTH_SHORT).show();
				break;
			case 4076:
				Toast.makeText(Hourse_FollowAct.this, "交付日期有误或者中介佣金未输入", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4077:
				Toast.makeText(Hourse_FollowAct.this, "联系电话有误", Toast.LENGTH_SHORT).show();
				break;
			case 4069:
				Toast.makeText(Hourse_FollowAct.this, "房源简介有误", Toast.LENGTH_SHORT).show();
				break;
			case 4071:
				Toast.makeText(Hourse_FollowAct.this, "房源状态有误", Toast.LENGTH_SHORT).show();
				break;
			case 4078:
				Toast.makeText(Hourse_FollowAct.this, "房源封面图有误", Toast.LENGTH_SHORT).show();
				break;
			case 4079:
				Toast.makeText(Hourse_FollowAct.this, "房源图片有误", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(Hourse_FollowAct.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public void select_customeradd(String session_id, String id,
			String category, String unit_no, String square_meter,
			String transaction_type, String room_price, String position,
			String head_to, String delivery_status, String delivery_date,
			String fee_rate, String min_rent_period, String contact_phone,
			String chair_total, String intro, String publish_status,
			String status, String avatar, String images, String land_id) {
		Request request = Request.requesthourseinfoedit(session_id, id,
				category, unit_no, square_meter, transaction_type, room_price,room_price_unit,
				position, head_to, delivery_status, delivery_date, fee_rate,
				min_rent_period, contact_phone, chair_total, intro,
				publish_status, status, avatar, images, land_id);
		new HttpThread(request, this);
	}

	public void select_standoffice(String session_id, String id, String page) {
		Request request = Request.requestroominfo(session_id, id, page);
		new HttpThread(request, this);
	}

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC) {
			JSONObject json, json2;
			JSONArray joArray, joArray2,joArray3;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					json2 = json.getJSONObject("data");
					id = json2.getString("id");
					is_click=json2.getString("is_click");
					transaction_type = json2.getString("transaction_type");
					building_id = json2.getString("building_id");
					building_name = json2.getString("building_name");
					avatar = json2.getString("avatar");
					avatar_id = json2.getString("avatar_id");
					category = json2.getString("category");
					unit_no = json2.getString("unit_no");
					val_total=json2.getString("val_total");
					status = json2.getString("status");
					fee_rate = json2.getString("fee_rate");
					user_type = json2.getString("publish_user_type");
					unique=json2.getString("unique");
					release_time=json2.getString("release_time");
					verified = json2.getString("verified");
					image_total = json2.getString("image_total");
					if (transaction_type.equals("1")) {
						rent_price = json2.getString("rent_price");	
						rent_price_unit = json2.getString("rent_price_unit");
						room_price_unit=rent_price_unit;
					} else {
						sell_price = json2.getString("sell_price");
						sell_price_unit = json2.getString("sell_price_unit");
						room_price_unit=sell_price_unit;
					}
					head_to = json2.getString("head_to");
					delivery_date = json2.getString("delivery_date");
					delivery_status = json2.getString("delivery_status");
					intro = json2.getString("intro");
					square_meter = json2.getString("square_meter");
					update_time = json2.getString("update_time");
					position = json2.getString("position");
					contact_phone = json2.getString("contact_phone");
					min_rent_period = json2.getString("min_rent_period");
					joArray = json2.getJSONArray("work_log");
					int len = joArray.length();
					for (int i = 0; i < len; i++) {
						json = joArray.getJSONObject(i);
						str_id.add(json.getString("id"));
						str_event_date.add(json.getString("event_date"));
						str_content.add(json.getString("content"));
						str_status.add(json.getString("status"));
						str_notify_time.add(json.getString("notify_time"));
						str_date.add(json.getString("event_date") + "  "
								+ json.getString("week_no"));
					}
					joArray2 = json2.getJSONArray("images");
					for (int i = 0; i < joArray2.length(); i++) {
						json = joArray2.getJSONObject(i);
						str_images_id.add(json.getString("id"));
						str_images_url.add(json.getString("url"));
					}
					joArray3=json2.getJSONArray("land");
					for (int i = 0; i < joArray3.length(); i++) {
						json=joArray2.getJSONObject(i);
						str_land.add(json.getString("url"));
						str_land_id.add(json.getString("id"));
					}
					newHandler.sendEmptyMessage(1);
				} else if (errorid.equals("1")) {
					newHandler.sendEmptyMessage(2);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if (reqID==Request.REQUEST_ECONOMIC_COMPANY) {
			JSONObject json;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					newHandler.sendEmptyMessage(3);
				}else if (errorid.equals("4067")) {
					newHandler.sendEmptyMessage(4067);

				} else if (errorid.equals("4088")) {
					newHandler.sendEmptyMessage(4088);

				} else if (errorid.equals("4068")) {
					newHandler.sendEmptyMessage(4068);

				} else if (errorid.equals("4058")) {
					newHandler.sendEmptyMessage(4058);

				} else if (errorid.equals("4059")) {
					newHandler.sendEmptyMessage(4059);

				} else if (errorid.equals("4061")) {
					newHandler.sendEmptyMessage(4061);

				} else if (errorid.equals("4074")) {
					newHandler.sendEmptyMessage(4074);

				} else if (errorid.equals("4075")) {
					newHandler.sendEmptyMessage(4075);
				} else if (errorid.equals("4076")) {
					newHandler.sendEmptyMessage(4076);

				} else if (errorid.equals("4030")) {
					newHandler.sendEmptyMessage(4030);

				} else if (errorid.equals("4077")) {
					newHandler.sendEmptyMessage(4077);

				} else if (errorid.equals("4069")) {
					newHandler.sendEmptyMessage(4069);

				} else if (errorid.equals("4071")) {
					newHandler.sendEmptyMessage(4071);

				} else if (errorid.equals("4078")) {
					newHandler.sendEmptyMessage(4078);

				} else if (errorid.equals("4079")) {
					newHandler.sendEmptyMessage(4079);
				}else if (errorid.equals("1")) {
					newHandler.sendEmptyMessage(4);
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
