package com.mrlou.message;

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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.iflytek.msc.MSC;
import com.mrlou.h5.RoomInfo_Act;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

public class Message_SingleBuilderAct extends Activity implements HttpListener {

	private LinearLayout lay_message_singlebuilder_back;
	private ListView lv_message_singlebuilder;
	private Message_SingleBuilderAdapter adapter;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private ArrayList<String> str_id, str_category, str_building_id,
			str_building_name, str_avatar, str_transaction_type, str_unit_no,
			str_status, str_verified, str_image_total, str_rent_price,
			str_sell_price, str_release_time, str_datetime, str_user_type,
			str_fee_rate, str_unique;
	private int num = 1;
	private String url, url2, type_id,mrString;
	private SharedPreferences sharedPreferences;
	private TextView tv_name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_singlebuilder);
		sharedPreferences = Message_SingleBuilderAct.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		type_id = sharedPreferences.getString("type_id", "");
		prepare();
		select_standoffice();
		// findView();
	}

	private void refresh() {
		// TODO Auto-generated method stub
		// 这几个刷新Label的设置
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_message);
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
						select_standoffice();
						// select_standoffice("1");
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

	public void select_standoffice() {
		Request request = Request.requeststandoffice();
		new HttpThread(request, this);
	}

	private void prepare() {
		// TODO Auto-generated method stub
		str_id = new ArrayList<String>();
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
		str_datetime = new ArrayList<String>();
		str_user_type = new ArrayList<String>();
		str_unique = new ArrayList<String>();
		str_fee_rate = new ArrayList<String>();
		str_release_time = new ArrayList<String>();
		tv_name=(TextView) findViewById(R.id.tv_shorthourse_name);
		tv_name.setText("整层独栋写字楼");
	}

	private void findView() {
		// TODO Auto-generated method stub

//		if (str_avatar.size() >= 20) {
//			refresh();
//		}
		lay_message_singlebuilder_back = (LinearLayout) findViewById(R.id.lay_message_singlebuilder_back);
		lay_message_singlebuilder_back
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						simulateKey(KeyEvent.KEYCODE_BACK);
					}
				});
		lv_message_singlebuilder = (ListView) findViewById(R.id.lv_message_singlebuilder);
		adapter = new Message_SingleBuilderAdapter(
				Message_SingleBuilderAct.this, str_id, str_category,
				str_building_id, str_building_name, str_avatar,
				str_transaction_type, str_unit_no, str_status, str_verified,
				str_image_total, str_rent_price, str_sell_price,
				str_release_time, str_datetime, str_user_type, str_fee_rate,
				str_unique,type_id);
		lv_message_singlebuilder.setAdapter(adapter);
		lv_message_singlebuilder.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mrString = Message_SingleBuilderAct.this.getExternalFilesDir(null).getPath();
				url = "file://"+mrString+"/bulidersir/lxs_index.html";
				url2 = "file://"+mrString+"/bulidersir/";
				Intent intent = new Intent(
						Message_SingleBuilderAct.this,
						RoomInfo_Act.class);
				intent.putExtra("url", url);
				intent.putExtra("url2", url2);
				intent.putExtra("frsource", "gg");
				intent.putExtra("id", str_id.get(position));
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
					Message_SingleBuilderAct.this.finish();
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
				findView();
				break;
			case 2:
				// Toast.makeText(Economic_SearchAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 33:

				break;
			case 99:
				// Toast.makeText(Economic_SearchAct.this, "网络异常，请尝试下拉刷新", Toast.LENGTH_SHORT)
				// .show();
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
					joArray = json.getJSONArray("list");
					int length = joArray.length();
					for (int i = 0; i < length; i++) {
						json2 = joArray.getJSONObject(i);
						str_id.add(json2.getString("id"));
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
						//str_datetime.add(json2.getString("datetime"));
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

	@Override
	public void doError(String s) {
		// TODO Auto-generated method stub
		if (s.equals("请求异常")) {
			newHandler.sendEmptyMessage(99);
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
			Message_SingleBuilderAct.this.finish();
            return true;
        }
		return super.onKeyDown(keyCode, event);
	}
//	
//	class Message_SingleBuilderAdapter extends BaseAdapter {
//
//		DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
//
//		private Context mContext;
//		private ArrayList<String> str_id, str_category, str_building_id,
//				str_building_name, str_avatar, str_transaction_type, str_unit_no,
//				str_status, str_verified, str_image_total, str_rent_price,
//				str_sell_price, str_release_time, str_datetime, str_user_type,
//				str_fee_rate, str_unique;
//		private int[] drawable = { R.drawable.developers, R.drawable.agent02,
//				R.drawable.operator, R.drawable.agent, R.drawable.property,
//				R.drawable.other };
////		private ImageLoader imageLoader;
//
//		public Message_SingleBuilderAdapter(Context context, ArrayList<String> id,
//				ArrayList<String> category, ArrayList<String> building_id,
//				ArrayList<String> building_name, ArrayList<String> avatar,
//				ArrayList<String> transaction_type, ArrayList<String> unit_no,
//				ArrayList<String> status, ArrayList<String> verified,
//				ArrayList<String> image_total, ArrayList<String> rent_price,
//				ArrayList<String> sell_price, ArrayList<String> release_time,
//				ArrayList<String> datetime, ArrayList<String> user_type,
//				ArrayList<String> fee_rate, ArrayList<String> unique) {
//			this.mContext = context;
//			this.str_id = id;
//			this.str_category = category;
//			this.str_building_id = building_id;
//			this.str_building_name = building_name;
//			this.str_avatar = avatar;
//			this.str_transaction_type = transaction_type;
//			this.str_unit_no = unit_no;
//			this.str_status = status;
//			this.str_verified = verified;
//			this.str_image_total = image_total;
//			this.str_rent_price = rent_price;
//			this.str_sell_price = sell_price;
//			this.str_release_time = release_time;
//			this.str_datetime = datetime;
//			this.str_user_type = user_type;
//			this.str_fee_rate = fee_rate;
//			this.str_unique = unique;
////			imageLoader = new ImageLoader(new ImageDownloader() {
////				@Override
////				public Bitmap download(String path, int width, int height) {
////					return HttpUtil.download(path);
////				}
////			});
//			options = new DisplayImageOptions.Builder()
//					.showStubImage(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
//					.showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
//					.showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
//					.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
//					.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
//					.displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
//					.build();
//		}
//
//		@Override
//		public int getCount() {
//			// TODO Auto-generated method stub
//			return str_avatar.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		@Override
//		public long getItemId(int position) {
//			// TODO Auto-generated method stub
//			return 0;
//		}
//
//		public View getView(int position, View view, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			ViewHoler viewHoler;
//			if (view == null) {
//				viewHoler = new ViewHoler();
//				LayoutInflater inflate = ((Activity) mContext).getLayoutInflater();
//				view = (View) inflate.inflate(R.layout.message_singlebuilder_list,
//						null);
//				viewHoler.tv_area = (TextView) view
//						.findViewById(R.id.tv_message_singlebuilder_area);
//				viewHoler.tv_name = (TextView) view
//						.findViewById(R.id.tv_message_singlebuilder_name);
//				viewHoler.tv_time = (TextView) view
//						.findViewById(R.id.tv_message_singlebuilder_time);
//				viewHoler.tv_values = (TextView) view
//						.findViewById(R.id.tv_message_singlebuilder_values);
//				viewHoler.iv_pic01 = (ImageView) view
//						.findViewById(R.id.iv_message_singlebuilder);
//				viewHoler.iv_pic02 = (ImageView) view
//						.findViewById(R.id.iv_message_singlebuilder_flag);
//				viewHoler.lay_pic = (LinearLayout) view
//						.findViewById(R.id.lay_message_singlebuilder);
//				viewHoler.badgeView = new BadgeView(mContext, viewHoler.iv_pic01);
//				// viewHoler.badgeView.setBadgePosition(BadgeView.POSITION_BOTTOM);
//				LayoutParams laParams = (LayoutParams) viewHoler.iv_pic01
//						.getLayoutParams();
//				int height = laParams.height;
//				viewHoler.badgeView.setGravity(Gravity.BOTTOM);
//				viewHoler.badgeView.setBadgeMargin(0, height - 20);
//				viewHoler.badgeView
//						.setBackgroundResource(R.drawable.authentication);
//				viewHoler.badgeView.show();
//
//				viewHoler.badgeView2 = new BadgeView(mContext, viewHoler.lay_pic);
//				viewHoler.badgeView2.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
//				viewHoler.badgeView2.setBackgroundResource(R.drawable.sole);
//				view.setTag(viewHoler);
//			} else {
//				viewHoler = (ViewHoler) view.getTag();
//			}
//			viewHoler.iv_pic01.setImageResource(R.drawable.ic_launcher);
//
//			imageLoader.displayImage(str_avatar.get(position), viewHoler.iv_pic01, options);
////			imageLoader.loadImage(str_avatar.get(position), 70, 70,
////					viewHoler.iv_pic01);
//
//			if (str_unique.get(position).equals("1")) {
//				viewHoler.badgeView2.show();
//			} else {
//				viewHoler.badgeView2.hide();
//			}
//
//			if (str_user_type.get(position).equals("1")) {
//				viewHoler.iv_pic02.setBackgroundResource(drawable[0]);
//			} else if (str_user_type.get(position).equals("2")) {
//				viewHoler.iv_pic02.setBackgroundResource(drawable[1]);
//			} else if (str_user_type.get(position).equals("3")) {
//				viewHoler.iv_pic02.setBackgroundResource(drawable[2]);
//			} else if (str_user_type.get(position).equals("102")) {
//				viewHoler.iv_pic02.setBackgroundResource(drawable[3]);
//			} else if (str_user_type.get(position).equals("103")) {
//				viewHoler.iv_pic02.setBackgroundResource(drawable[4]);
//			} else {
//				viewHoler.iv_pic02.setBackgroundResource(drawable[5]);
//			}
//			viewHoler.tv_name.setText(str_building_name.get(position));
//			viewHoler.tv_area.setText(str_unit_no.get(position));
//			viewHoler.tv_time.setText(str_fee_rate.get(position));
//			if (str_transaction_type.get(position).equals("1")) {
//				viewHoler.tv_values
//						.setText("租金报价: " + str_rent_price.get(position));
//			} else {
//				viewHoler.tv_values
//						.setText("出售报价: " + str_sell_price.get(position));
//			}
//			return view;
//		}
//
//		class ViewHoler {
//			private TextView tv_time, tv_name, tv_area, tv_values;
//			private ImageView iv_pic01, iv_pic02;
//			private LinearLayout lay_pic;
//			private BadgeView badgeView, badgeView2;
//		}
//	}
	
}
