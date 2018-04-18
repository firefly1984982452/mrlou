package com.mrlou.favorites;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mrlou.adapter.Hourse_DetailAdapter;
import com.mrlou.h5.RoomInfo_Act;
import com.mrlou.h5.YlbMessage_Act;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.mrlou.R.string;
import com.mrlou.ttf.FontManager;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class Hourse_DetailAct extends Activity implements HttpListener {

	private LinearLayout lay_hourse_detail_back, lay_hourse_detail_none,
			lay_hourse_detail_main,lay_mine_hourse_add;
	private TextView tv_myhourse_buildingname;
	private ArrayList<String> str_id, str_category, str_building_id,
			str_building_name, str_avatar, str_transaction_type, str_unit_no,
			str_status, str_verified, str_image_total, str_rent_price,
			str_sell_price, str_unique, str_time;
	private ListView lv;
	private Hou_DetailAdapter adapter;
	private String id, buildingname;
	private SharedPreferences sharedPreferences;
	private String mrString,url,url2;
	private String session_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_hourse_detail);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		buildingname = intent.getStringExtra("buildingname");
		sharedPreferences = Hourse_DetailAct.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		prapare();
		select_area(session_id, id);
		// findView();
	}

	private void prapare() {
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
		str_unique = new ArrayList<String>();
		str_time = new ArrayList<String>();
	}

	private void findView() {
		// TODO Auto-generated method stub
		tv_myhourse_buildingname = (TextView) findViewById(R.id.tv_myhourse_buildingname);
		tv_myhourse_buildingname.setText(buildingname);
		lay_hourse_detail_none = (LinearLayout) findViewById(R.id.lay_hourse_detail_none);
		lay_hourse_detail_back = (LinearLayout) findViewById(R.id.lay_hourse_detail_back);
		lay_hourse_detail_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		AnimationSet set = new AnimationSet(false);
		Animation animation = new AlphaAnimation(0, 1); // AlphaAnimation
														// 控制渐变透明的动画效果
		animation.setDuration(200); // 动画时间毫秒数
		set.addAnimation(animation); // 加入动画集合
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 1);
		lv = (ListView) findViewById(R.id.lv_hourse_detail);
		adapter = new Hou_DetailAdapter(Hourse_DetailAct.this, str_id,
				str_category, str_building_id, str_building_name, str_avatar,
				str_transaction_type, str_unit_no, str_status, str_verified,
				str_image_total, str_rent_price, str_sell_price, str_unique,session_id);
		lv.setAdapter(adapter);
		lv.setLayoutAnimation(controller);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mrString = Hourse_DetailAct.this.getExternalFilesDir(null).getPath();
				url = "file://"+mrString+"/bulidersir/lxs_index.html";
				url2 = "file://"+mrString+"/bulidersir/";
				Intent intent = new Intent(Hourse_DetailAct.this,
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
					Hourse_DetailAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	public void select_area(String session_id, String id) {
		Request request = Request.requestcollect_hourse(session_id, id);
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
				Toast.makeText(Hourse_DetailAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(Hourse_DetailAct.this, "网络异常，请尝试下拉刷新", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC) {
			JSONObject json;
			JSONArray joArray;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					joArray = json.getJSONArray("list");
					int length = joArray.length();
					for (int i = 0; i < length; i++) {
						json = joArray.getJSONObject(i);
						str_id.add(json.getString("id"));
						str_category.add(json.getString("category"));
						str_building_id.add(json.getString("building_id"));
						str_building_name.add(json.getString("building_name"));
						str_avatar.add(json.getString("avatar"));
						str_transaction_type.add(json
								.getString("transaction_type"));
						str_unit_no.add(json.getString("unit_no"));
						str_status.add(json.getString("status"));
						str_verified.add(json.getString("verified"));
						str_image_total.add(json.getString("image_total"));
						str_rent_price.add(json.getString("rent_price"));
						str_sell_price.add(json.getString("sell_price"));
						str_unique.add(json.getString("unique"));
						str_time.add(json.getString("time"));
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
	
}
