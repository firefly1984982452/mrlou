package com.mrlou.yi;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
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
import com.mrlou.h5.Ylb_act;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

/**
 *@author jx_chen
 *@date 2016-10-31---上午9:56:41
 *@problem
 */
public class Yi_OtherAct extends Activity implements HttpListener{
	

	private ListView lv_hourse;
	private ArrayList<String> str_id, str_user_id, str_avatar, str_username,
			str_title, str_subject,str_region, str_reply_total,
			str_like_total;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private ScrollView mScrollView;
	private Yi_Adapter adapter;
	private String mrString,url,url2,session_id;
	private SharedPreferences sharedPreferences;
	private int num=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yi_other);
		sharedPreferences = Yi_OtherAct.this.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		init();
		select_yi_hourse(session_id, "1", "3");
		refresh();
	}

	private void refresh() {
		// TODO Auto-generated method stub
		// 这几个刷新Label的设置
		mPullRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
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
						// select_agent(id);
						// 执行刷新函数
						select_yi_hourse(session_id, ""+num, "3");
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
			// mPullRefreshScrollView.setMode(Mode.BOTH);

			// 在更新UI后，无需其它Refresh操作，系统会自己加载新的listView
			mPullRefreshScrollView.onRefreshComplete();
			super.onPostExecute(result);
		}
	}

	private void findView() {
		// TODO Auto-generated method stub
		lv_hourse = (ListView) findViewById(R.id.lv_yi_other);
		adapter = new Yi_Adapter(Yi_OtherAct.this, str_id, str_user_id,
				str_avatar, str_username, str_title, str_subject,str_region,str_reply_total,str_like_total);
		lv_hourse.setAdapter(adapter);
		lv_hourse.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mrString = Yi_OtherAct.this.getExternalFilesDir(null).getPath();
				url = "file://"+mrString+"/bulidersir/ylb_thread.html";
				url2 = "file://"+mrString+"/bulidersir/";
				Intent intent = new Intent(Yi_OtherAct.this, Ylb_act.class);
				intent.putExtra("url", url);
				intent.putExtra("url2", url2);
				intent.putExtra("num", "1");
				intent.putExtra("id", str_id.get(position));
				startActivity(intent);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				num=num+1;
				findView();
				break;
			case 2:
				Toast.makeText(Yi_OtherAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(Yi_OtherAct.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	public void select_yi_hourse(String session_id, String page, String subject) {
		Request request = Request.request_yi(session_id, page, subject);
		new HttpThread(request, this);
	}

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_NEW) {
			JSONObject json;
			JSONArray jsonArray;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					jsonArray = json.getJSONArray("list");
					int len = jsonArray.length();
					for (int i = 0; i < len; i++) {
						json = jsonArray.getJSONObject(i);
						str_avatar.add(json.getString("user_avatar"));
						str_id.add(json.getString("id"));
						str_title.add(json.getString("title"));
						str_user_id.add(json.getString("user_id"));
						str_username.add(json.getString("user_username"));
						str_subject.add(json.getString("subject"));
						str_region.add("");
						str_reply_total.add(json.getString("reply_total"));
						str_like_total.add(json.getString("like_total"));
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

	private void init() {
		str_avatar = new ArrayList<>();
		str_id = new ArrayList<>();
		str_title = new ArrayList<>();
		str_user_id = new ArrayList<>();
		str_username = new ArrayList<>();
		str_subject = new ArrayList<>();
		str_region=new ArrayList<>();
		str_reply_total=new ArrayList<>();
		str_like_total=new ArrayList<>();
	}
}