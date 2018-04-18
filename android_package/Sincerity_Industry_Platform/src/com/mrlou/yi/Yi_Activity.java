package com.mrlou.yi;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.mrlou.favorites.IntenrProvider;
import com.mrlou.h5.YlbMessage_Act;
import com.mrlou.listener.HttpListener;
import com.mrlou.message.Message_SelectAreaAdapter;
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.mrlou.util.User_info;

/**
 * @author jx_chen
 * @date 2016-10-17---下午2:30:03
 * @problem
 */
public class Yi_Activity extends Fragment  implements HttpListener{
	private TabHost tabHost;
	private TextView tv_hourse;// 我有房源
	private TextView tv_customer;// 我有客户
	private TextView tv_other;// 其他主题
	private LinearLayout lay_hourse;
	private LinearLayout lay_customer;
	private LinearLayout lay_other;
	private View currentTab;
	private String mrString,url,url2;
	private SharedPreferences sharedPreferences;
	private ImageView iv_yi_add,iv_yi_select;
	private PopupWindow popwindow;
	private View view,view_main;
	private ListView lv_area;
	private Message_SelectAreaAdapter mAdapter;
	private ArrayList<String> str_area,str_area_id;
	private String type="1",session_id;
	private ArrayList<String> str_id, str_user_id, str_avatar, str_username,
	str_title, str_subject,str_region,str_reply_total,str_like_total;
	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.mrlou_yi);
//		sharedPreferences = getActivity().getSharedPreferences("user",
//				Activity.MODE_PRIVATE);
//		session_id = sharedPreferences.getString("session_id", "");
//		prepare();
//		findView();
//		LocalActivityManager groupActivityManager = new LocalActivityManager(
//				getActivity(), false);
//		groupActivityManager.dispatchCreate(savedInstanceState);
//		tabHost.setup(groupActivityManager);
//		init();
//		initTabhost();
//	}
//	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view_main = inflater.inflate(R.layout.mrlou_yi, container, false);
		sharedPreferences = getActivity().getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		prepare();
		findView();
		LocalActivityManager groupActivityManager = new LocalActivityManager(
				getActivity(), false);
		groupActivityManager.dispatchCreate(savedInstanceState);
		tabHost.setup(groupActivityManager);
		init();
		initTabhost();
		return view_main;
	}
	

	private void init() {
		// TODO Auto-generated method stub
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String arg0) {
				if (currentTab == null) {
					currentTab = tabHost.getCurrentView();
					return;
				}
				Animation animation = AnimationUtils.loadAnimation(
						getActivity(), R.anim.push_left_out);
				currentTab.setAnimation(animation);
				animation = AnimationUtils.loadAnimation(getActivity(),
						R.anim.push_left_in);
				tabHost.getCurrentView().setAnimation(animation);
				currentTab = tabHost.getCurrentView();
			}
		});
	}

	private void initTabhost() {
		// TODO Auto-generated method stub
		tabHost.addTab(tabHost
				.newTabSpec(IntenrProvider.ACTION_COLLECTION_YI_HOURSE)
				.setIndicator(IntenrProvider.ACTION_COLLECTION_YI_HOURSE)
				.setContent(new Intent(getActivity(), YI_HourseAct.class)));
		tabHost.addTab(tabHost
				.newTabSpec(IntenrProvider.ACTION_COLLECTION_YI_CUSTOMER)
				.setIndicator(IntenrProvider.ACTION_COLLECTION_YI_CUSTOMER)
				.setContent(new Intent(getActivity(), Yi_CustomerAct.class)));
		tabHost.addTab(tabHost
				.newTabSpec(IntenrProvider.ACTION_COLLECTION_YI_OTHER)
				.setIndicator(IntenrProvider.ACTION_COLLECTION_YI_OTHER)
				.setContent(new Intent(getActivity(), Yi_OtherAct.class)));

	}

	private void findView() {
		// TODO Auto-generated method stub
		iv_yi_select=(ImageView) view_main.findViewById(R.id.iv_yi_select);
		iv_yi_select.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),Yi_SelectAct.class);
				intent.putExtra("type", type);
				startActivity(intent);
				
//				popwindow.setOutsideTouchable(true);
//				popwindow.showAtLocation(lay_yi_back, Gravity.RIGHT
//						| Gravity.BOTTOM, 0, 0);
//				popwindow.showAsDropDown(view);
//				popwindow.update();
//				WindowManager.LayoutParams params = getActivity()
//						.getWindow().getAttributes();
//				params.alpha = 0.7f;
//				getActivity().getWindow().setAttributes(params);
			}
		});
		view = LayoutInflater.from(getActivity()).inflate(
				R.layout.message_select_arealist, null);
		// 设置popwindow
		WindowManager wm = getActivity().getWindowManager();
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		popwindow = new PopupWindow(view, width - 200, height - 50, true);
		popwindow.setAnimationStyle(R.style.popwin_anim_style);
		popwindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					popwindow.dismiss();
					return true;
				}
				return false;
			}
		});
		ColorDrawable dw = new ColorDrawable(0x90000000);
		popwindow.setOnDismissListener(new poponDismissListener());
		popwindow.setBackgroundDrawable(dw);
		popwindow.setFocusable(true);
//		lv_area=(ListView) view.view_main.findViewById(R.id.lv_select_area);
//		mAdapter=new Message_SelectAreaAdapter(getActivity(), str_area);
//		lv_area.setAdapter(mAdapter);
//		lv_area.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				popwindow.dismiss();
//				select_yi_hourse_search(session_id, "1", type, str_area_id.get(position));
//			}
//		});
		
		iv_yi_add=(ImageView) view_main.findViewById(R.id.iv_yi_add);
		iv_yi_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mrString = getActivity().getExternalFilesDir(null).getPath();
				url = "file://"+mrString+"/bulidersir/ylb_add_thread.html";
				url2 = "file://"+mrString+"/bulidersir/";
				Intent intent = new Intent(getActivity(), YlbMessage_Act.class);
				intent.putExtra("url", url);
				intent.putExtra("url2", url2);
				startActivity(intent);
			}
		});
//		lay_yi_back=(LinearLayout) view_main.findViewById(R.id.lay_yi_back);
//		lay_yi_back.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				simulateKey(KeyEvent.KEYCODE_BACK);
//			}
//		});
		tabHost = (TabHost) view_main.findViewById(R.id.tab_host);
		// 我有房源
		tv_hourse = (TextView) view_main.findViewById(R.id.tv_yi_hourse);
		lay_hourse = (LinearLayout) view_main.findViewById(R.id.lay_yi_hourse);
		lay_hourse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				type="1";
				iv_yi_select.setVisibility(View.VISIBLE);
				changeTagBg(1);
			}
		});
		// 我有客户
		tv_customer = (TextView) view_main.findViewById(R.id.tv_yi_customer);
		lay_customer = (LinearLayout) view_main.findViewById(R.id.lay_yi_customer);
		lay_customer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				type="2";
				iv_yi_select.setVisibility(View.VISIBLE);
				changeTagBg(2);
			}
		});
		// 其他主题
		tv_other = (TextView) view_main.findViewById(R.id.tv_yi_other);
		lay_other = (LinearLayout) view_main.findViewById(R.id.lay_yi_other);
		lay_other.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				iv_yi_select.setVisibility(View.GONE);
				changeTagBg(3);
			}
		});
		changeTagBg(1);
	}

	private void changeTagBg(int flag) {
		// TODO Auto-generated method stub
		if (flag == 1) {
			lay_hourse.setBackgroundResource(R.drawable.corner_left_black);
			tabHost.setCurrentTabByTag(IntenrProvider.ACTION_COLLECTION_YI_HOURSE);
			tv_hourse.setTextColor(getActivity().getResources().getColor(
					R.color.white));
		} else {
			lay_hourse
					.setBackgroundResource(R.drawable.corner_left_black_and_white);
			tv_hourse.setTextColor(getActivity().getResources().getColor(
					R.color.light_gray));
		}
		if (flag == 2) {
			lay_customer.setBackgroundResource(R.color.light_dark);
			tabHost.setCurrentTabByTag(IntenrProvider.ACTION_COLLECTION_YI_CUSTOMER);
			tv_customer.setTextColor(getActivity().getResources().getColor(
					R.color.white));
		} else {
			lay_customer.setBackgroundResource(R.drawable.border_center_black);
			tv_customer.setTextColor(getActivity().getResources().getColor(
					R.color.light_gray));
		}
		if (flag == 3) {
			lay_other.setBackgroundResource(R.drawable.corner_right_black);
			tabHost.setCurrentTabByTag(IntenrProvider.ACTION_COLLECTION_YI_OTHER);
			tv_other.setTextColor(getActivity().getResources().getColor(
					R.color.white));
		} else {
			lay_other
					.setBackgroundResource(R.drawable.corner_right_black_and_white);
			tv_other.setTextColor(getActivity().getResources().getColor(
					R.color.light_gray));
		}
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
		class poponDismissListener implements PopupWindow.OnDismissListener {

			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				// Log.v("List_noteTypeActivity:", "我是关闭事件");
				clearWindow();
			}
		}
		public void clearWindow() {
			WindowManager.LayoutParams params = getActivity()
					.getWindow().getAttributes();
			params.alpha = 1f;
			getActivity().getWindow().setAttributes(params);
		}
		private void prepare(){
			str_area=new ArrayList<String>();
			str_area.add("浦东新区");
			str_area.add("黄浦区");
			str_area.add("静安区");
			str_area.add("徐汇区");
			str_area.add("长宁区");
			str_area.add("普陀区");
			str_area.add("闸北区");
			str_area.add("虹口区");
			str_area.add("杨浦区");
			str_area.add("宝山区");
			str_area.add("闵行区");
			str_area.add("嘉定区");
			str_area.add("青浦区");
			str_area.add("松江区");
			str_area.add("奉贤区");
			str_area.add("金山区");
			str_area.add("崇明区");
			str_area_id=new ArrayList<String>();
			str_area_id.add("310115");
			str_area_id.add("310101");
			str_area_id.add("310106");
			str_area_id.add("310104");
			str_area_id.add("310105");
			str_area_id.add("310107");
			str_area_id.add("310108");
			str_area_id.add("310109");
			str_area_id.add("310110");
			str_area_id.add("310113");
			str_area_id.add("310112");
			str_area_id.add("310114");
			str_area_id.add("310118");
			str_area_id.add("310117");
			str_area_id.add("310120");
			str_area_id.add("310116");
			str_area_id.add("310230");
		}
		@SuppressLint("HandlerLeak")
		private Handler newHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					if (type.equals("1")) {
						YI_HourseAct.setSelect(str_id, str_user_id, str_avatar, str_username, str_title, str_subject,str_region,str_reply_total,str_like_total);
					}else {
						Yi_CustomerAct.setSelect(str_id, str_user_id, str_avatar, str_username, str_title, str_subject,str_region,str_reply_total,str_like_total);
					}
					break;
				case 2:
					Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
					break;
				case 99:
					Toast.makeText(getActivity(), "网络异常，请重新尝试连接", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		};
		public void select_yi_hourse_search(String session_id, String page, String subject,String area_code) {
			Request request = Request.request_searchthread(session_id, page, subject, area_code);
			new HttpThread(request, this);
		}
		
		@Override
		public void doResponse(int reqID, String b) {
			// TODO Auto-generated method stub
			 if(reqID == Request.REQUEST_ECONOMIC) {
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
								str_region.add(json.getString("region"));
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
}
