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
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.mrlou.adapter.Customer_ManageAdapter;
import com.mrlou.listener.HttpListener;
import com.mrlou.message.Message_CustomerLogAct;
import com.mrlou.message.Message_PostcommentAct;
import com.mrlou.mrlou.R;
import com.mrlou.ttf.FontManager;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

public class Customer_ManAct extends Activity implements HttpListener{

	private ArrayList<String> str_name,str_status,str_id,str_intro,str_time;
	private ListView lv_customer_manage;
	private Customer_ManageAdapter adapter;
	private LinearLayout lay_customer_manager_add, lay_customer_manage_back,
			lay_customer_manage_main;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private ScrollView mScrollView;
	private SharedPreferences sharedPreferences;
	private int num=1;
	private String session_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_customer_manage);
		prepare();
		sharedPreferences = Customer_ManAct.this.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		select_standoffice("1", session_id);
//		refresh();
//		findView();
	}
	private void prepare() {
		// TODO Auto-generated method stub
		str_name = new ArrayList<String>();
		str_id=new ArrayList<String>();
		str_status=new ArrayList<String>();
		str_intro=new ArrayList<String>();
		str_time=new ArrayList<String>();
	}
	private void refresh() {
		// TODO Auto-generated method stub
		// 这几个刷新Label的设置
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_customer);
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
						num=num+1;
						select_standoffice(""+num, session_id);
						Toast.makeText(Customer_ManAct.this, "测试下拉", Toast.LENGTH_SHORT).show();
						//select_standoffice("1");
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
		if (str_name.size()>=20) {
			refresh();
		}
		lay_customer_manage_back = (LinearLayout) findViewById(R.id.lay_customer_manage_back);
		lay_customer_manage_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		
		lv_customer_manage = (ListView) findViewById(R.id.lv_customer_manage);
		adapter = new Customer_ManageAdapter(Customer_ManAct.this, str_name,str_status,str_intro,str_time);
		lv_customer_manage.setAdapter(adapter);

		lv_customer_manage.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Customer_ManAct.this,
						Customer_DetailAct.class);
				intent.putExtra("id", str_id.get(position));
				startActivity(intent);
			}
		});

		lay_customer_manager_add = (LinearLayout) findViewById(R.id.lay_customer_manager_add);
		lay_customer_manager_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Customer_ManAct.this,
						Customer_managerAddAct.class);
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
					Customer_ManAct.this.finish();
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
				//lv_search.setVisibility(View.VISIBLE);
				findView();
				//adapter.notifyDataSetChanged();
				break;
			case 2:
				Toast.makeText(Customer_ManAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 33:

				break;
			case 99:
				Toast.makeText(Customer_ManAct.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT)
				.show();
				break;
			}
		}
	};

	public void select_standoffice(String page,String session_id) {
		Request request = Request.requestcustomermanage(page, session_id);
		new HttpThread(request, this);
	}
	
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
						int len = joArray.length();
						for (int i = 0; i < len; i++) {
							json = joArray.getJSONObject(i);
							str_id.add(json.getString("id"));
							str_name.add(json.getString("name"));
							str_status.add(json.getString("process"));
							str_intro.add(json.getString("intro"));
							str_time.add(json.getString("update_time"));
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
