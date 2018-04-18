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
import android.content.SharedPreferences.Editor;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.mrlou.util.User_info;

public class Message_Main extends Fragment implements HttpListener {

	private View view;
	private ListView lv;
	private Message_MainAdapter adapter;
	private ArrayList<String> str_title, str_content, str_flag, str_time,
			str_createtime, str_times;
	private SharedPreferences sharedPreferences;
	private Editor editor;
	private SweetAlertDialog pDialog;
	private Progress_Dialog dialog;
	private boolean flag = true;
	private String type_id;
	private LinearLayout message_mianback;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.message_main, container, false);
		sharedPreferences = getActivity().getSharedPreferences("message",
				Activity.MODE_PRIVATE);
		prapare();
		dialog.dialog();
		select_area(User_info.getSession_id());
		// findView();
		return view;
	}

	private void prapare() {
		// Bundle args = getArguments();
		str_content = new ArrayList<>();
		str_createtime = new ArrayList<>();
		str_time = new ArrayList<>();
		str_times = new ArrayList<String>();
		// if (User_info.getType_id().equals("299")
		// || User_info.getType_id().equals("104")) {
		// str_title = new ArrayList<String>();
		// str_title.add("楼先生活动");
		// str_title.add("最新认证房源");
		// str_title.add("楼先生独家房源");
		// str_title.add("整层独栋写字楼");
		// str_title.add("联合办公室出租");
		// str_title.add("通知消息");
		// str_title.add("房源工作日志提醒");
		// } else {
		str_title = new ArrayList<String>();
		// str_title.add("楼先生活动");
		// str_title.add("最新认证房源");
		// str_title.add("楼先生独家房源");
		// str_title.add("整层独栋写字楼");
		// str_title.add("联合办公室出租");
		// str_title.add("新盘推广");
		// str_title.add("通知消息");
		// str_title.add("评论提醒");
		// str_title.add("会员代理房源合作");
		// str_title.add("会员客户合作推荐");
		// str_title.add("房源工作日志提醒");
		// str_title.add("客户工作日志提醒");
		//
		// }
		str_flag = new ArrayList<String>();
		pDialog = new SweetAlertDialog(getActivity(),
				SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
		dialog = new Progress_Dialog(getActivity(), pDialog);
		message_mianback = (LinearLayout) view
				.findViewById(R.id.message_mianback);
		message_mianback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
	}

	private void refresh_data() {
		// TODO Auto-generated method stub
		str_content.clear();
		str_createtime.clear();
		str_time.clear();
		str_times.clear();
		str_title.clear();
		str_flag.clear();
		if (User_info.getType_id().equals("299")
				|| User_info.getType_id().equals("104")) {
			str_title.add("楼先生活动");
			str_title.add("最新认证房源");
			str_title.add("楼先生独家房源");
			str_title.add("整层独栋写字楼");
			str_title.add("联合办公室出租");
			str_title.add("新盘推广");
			str_title.add("通知消息");
			str_title.add("评论提醒");
			str_title.add("房源工作日志提醒");
		} else {
			str_title.add("楼先生活动");
			str_title.add("最新认证房源");
			str_title.add("楼先生独家房源");
			str_title.add("整层独栋写字楼");
			str_title.add("联合办公室出租");
			str_title.add("新盘推广");
			str_title.add("通知消息");
			str_title.add("评论提醒");
			str_title.add("会员代理房源合作");
			str_title.add("会员客户合作推荐");
			str_title.add("房源工作日志提醒");
			str_title.add("客户工作日志提醒");
		}
	}

	private void findView() {
		// TODO Auto-generated method stub
		editor = sharedPreferences.edit();
		for (int i = 0; i < str_title.size(); i++) {
			str_times.add(sharedPreferences.getString(str_title.get(i), ""));
			if (str_times.size() != 0) {
				if (str_times.size() != str_time.size()) {
					str_times.add("");
				}
			}
			if (str_times.get(i).equals("") && !str_time.get(i).equals("")) {
				str_flag.add("1");
				editor.putString(str_title.get(i), str_time.get(i));
			} else if (str_time.get(i).equals("")) {
				str_flag.add("0");
			} else {
				try {
					if (!str_time.get(i).equals(str_times.get(i))) {
						str_flag.add("1");
						editor.putString(str_title.get(i), str_time.get(i));
					} else {
						str_flag.add("0");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		lv = (ListView) view.findViewById(R.id.lv_message_main);
		adapter = new Message_MainAdapter(getActivity(), str_title,
				str_content, str_time, str_flag);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				str_flag.set(position, "0");
				adapter.notifyDataSetChanged();
				if (str_title.size() < 11) {
					selectact_six(position);
				} else {
					selectact(position);
				}
			}
		});
	}

	public void selectact_six(int position) {
		Intent intent;
		switch (position) {
		case 0:
			flag = false;
			editor.putString(str_title.get(0), str_time.get(0));
			editor.commit();
			intent = new Intent(getActivity(), Message_LxsAct.class);
			startActivity(intent);
			break;
		case 1:
			flag = false;
			editor.putString(str_title.get(1), str_time.get(1));
			editor.commit();
			intent = new Intent(getActivity(), Message_NewHourseAct.class);
			intent.putExtra("type", "0");
			startActivity(intent);
			break;
		case 2:
			flag = false;
			editor.putString(str_title.get(2), str_time.get(2));
			editor.commit();
			intent = new Intent(getActivity(), Message_ValuesHourseAct.class);
			startActivity(intent);
			break;
		case 3:
			flag = false;
			editor.putString(str_title.get(3), str_time.get(3));
			editor.commit();
			intent = new Intent(getActivity(), Message_SingleBuilderAct.class);
			startActivity(intent);
			break;
		case 4:
			flag = false;
			editor.putString(str_title.get(4), str_time.get(4));
			editor.commit();
			intent = new Intent(getActivity(), Message_ShortHourseAct.class);
			startActivity(intent);
			break;
		case 5:
			flag = false;
			editor.putString(str_title.get(5), str_time.get(5));
			editor.commit();
			intent = new Intent(getActivity(), Message_PropertiesAct.class);
			startActivity(intent);
			break;
		case 6:
			flag = false;
			editor.putString(str_title.get(6), str_time.get(6));
			editor.commit();
			intent = new Intent(getActivity(), Message_NoticeAct.class);
			startActivity(intent);
			break;
		case 7:
			flag = false;
			editor.putString(str_title.get(7), str_time.get(7));
			editor.commit();
			intent = new Intent(getActivity(), Message_PostcommentAct.class);
			startActivity(intent);
			break;
		case 8:
			flag = false;
			editor.putString(str_title.get(8), str_time.get(8));
			editor.commit();
			intent = new Intent(getActivity(), Message_HourseAct.class);
			startActivity(intent);
			break;

		}
	}

	public void selectact(int position) {
		Intent intent;
		switch (position) {
		case 0:
			flag = false;
			editor.putString(str_title.get(0), str_time.get(0));
			editor.commit();
			intent = new Intent(getActivity(), Message_LxsAct.class);
			startActivity(intent);
			break;
		case 1:
			flag = false;
			editor.putString(str_title.get(1), str_time.get(1));
			editor.commit();
			intent = new Intent(getActivity(), Message_NewHourseAct.class);
			intent.putExtra("type", "0");
			startActivity(intent);
			break;
		case 2:
			flag = false;
			editor.putString(str_title.get(2), str_time.get(2));
			editor.commit();
			intent = new Intent(getActivity(), Message_ValuesHourseAct.class);
			startActivity(intent);
			break;
		case 3:
			flag = false;
			editor.putString(str_title.get(3), str_time.get(3));
			editor.commit();
			intent = new Intent(getActivity(), Message_SingleBuilderAct.class);
			startActivity(intent);
			break;
		case 4:
			flag = false;
			editor.putString(str_title.get(4), str_time.get(4));
			editor.commit();
			intent = new Intent(getActivity(), Message_ShortHourseAct.class);
			startActivity(intent);
			break;
		case 5:
			flag = false;
			editor.putString(str_title.get(5), str_time.get(5));
			editor.commit();
			intent = new Intent(getActivity(), Message_PropertiesAct.class);
			startActivity(intent);
			break;
		case 6:
			flag = false;
			editor.putString(str_title.get(6), str_time.get(6));
			editor.commit();
			intent = new Intent(getActivity(), Message_NoticeAct.class);
			startActivity(intent);
			break;
		case 7:
			flag = false;
			editor.putString(str_title.get(7), str_time.get(7));
			editor.commit();
			intent = new Intent(getActivity(), Message_PostcommentAct.class);
			startActivity(intent);
			break;
		case 8:
			flag = false;
			editor.putString(str_title.get(8), str_time.get(8));
			editor.commit();
			intent = new Intent(getActivity(),
					Message_ExclusivelistingsAct.class);
			startActivity(intent);
			break;
		case 9:
			flag = false;
			editor.putString(str_title.get(9), str_time.get(9));
			editor.commit();
			intent = new Intent(getActivity(), Message_ExclusiveclientAct.class);
			startActivity(intent);
			break;

		case 10:
			flag = false;
			editor.putString(str_title.get(10), str_time.get(10));
			editor.commit();
			intent = new Intent(getActivity(), Message_HourseAct.class);
			startActivity(intent);
			break;
		case 11:
			flag = false;
			editor.putString(str_title.get(11), str_time.get(11));
			editor.commit();
			intent = new Intent(getActivity(), Message_CustomerLogAct.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// prapare();
				findView();
				break;
			case 2:
				Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT)
						.show();
				break;
			case 99:
				Toast.makeText(getActivity(), "网络异常，请尝试重新连接",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	public void select_area(String session_id) {
		Request request = Request.requestmessage_main(session_id);
		new HttpThread(request, this);
	}

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC_COMPANY) {
			dialog.dismiss();
			JSONObject json;
			JSONArray joArray;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					refresh_data();
					joArray = json.getJSONArray("list");
					int length = joArray.length();
					for (int i = 0; i < length; i++) {
						json = joArray.getJSONObject(i);
						str_content.add(json.getString("content"));
						str_createtime.add(json.getString("createtime"));
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

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		System.out.println("---onStart---");
		if (User_info.isFlag()) {
			select_area(User_info.getSession_id());
		}
		super.onStart();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		User_info.setFlag(false);
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		System.out.println("---onresume---");
		super.onResume();
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
}
