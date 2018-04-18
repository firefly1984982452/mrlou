package com.mrlou.mine;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrlou.activity.MainFragmentActivity;
import com.mrlou.addservices.AddServices_HistoryAct;
import com.mrlou.addservices.Main_addServices;
import com.mrlou.dialog.AlertDialogs;
import com.mrlou.favorites.Collect_MainAct;
import com.mrlou.listener.HttpListener;
import com.mrlou.message.Message_MainAct;
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpGetThread;
import com.mrlou.util.Request;
import com.mrlou.util.User_info;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class Mine_Activity extends Fragment implements HttpListener {

	private LinearLayout lay_setting, lay_myhourse, lay_mycustomer,
			lay_mymessage, lay_history_complete, lay_mypost,
			lay_mine_customer_service, lay_added_service,
			lay_membertype, lay_mycollect;
	private View view;
	private ImageView mine_act_head;
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	// private ImageLoader imageLoader;
	private TextView tv_mine_act_name, tv_mine_act_identy, tv_company_phone,
			tv_membertype;
	private SharedPreferences sharedPreferences;
	private String phone_number, email, en_username, cn_username, company,
			building_a, building_b, avatar, avatar_id, type, intro, work_start,
			region_id, user_id, type_id, region_id_real, wechat_qr_id,
			post_card_id, post_card, wechat_qr, wechat, add_v, session_id,
			member_type,service_type,company_id,office_building,office_building_id,good_type;
	private SweetAlertDialog pDialog;
	private Progress_Dialog dialog;
	private boolean flag = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub4
		view = inflater.inflate(R.layout.mine_activity, container, false);
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(getActivity()));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.logo) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.logo) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.logo) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build();
		getDate();
		findView();
		return view;
	}

	private void getDate() {
		// TODO Auto-generated method stub
		Bundle args = getArguments();
		phone_number = args.getString("phone_number");
		email = args.getString("email");
		en_username = args.getString("en_username");
		cn_username = args.getString("cn_username");
		company = args.getString("company");
		building_a = args.getString("building_a");
		building_b = args.getString("building_b");
		avatar = args.getString("avatar");
		avatar_id = args.getString("avatar_id");
		type = args.getString("type");
		intro = args.getString("intro");
		work_start = args.getString("work_start");
		post_card_id = args.getString("post_card_id");
		wechat_qr_id = args.getString("wechat_qr_id");
		post_card = args.getString("post_card");
		wechat_qr = args.getString("wechat_qr");
		wechat = args.getString("wechat");
		member_type = args.getString("member_type");
		region_id = args.getString("region_id");
		region_id_real = args.getString("region_id_real");
		add_v = args.getString("v");
		service_type=args.getString("service_type");
		company_id=args.getString("company_id");
		office_building=args.getString("office_building");
		office_building_id=args.getString("office_building_id");
		good_type=args.getString("good_type");
		sharedPreferences = getActivity().getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		// type_id = sharedPreferences.getString("type_id", "");
		user_id = sharedPreferences.getString("user_id", "");
		session_id = sharedPreferences.getString("session_id", "");
		pDialog = new SweetAlertDialog(getActivity(),
				SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
		dialog = new Progress_Dialog(getActivity(), pDialog);
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_mymessage=(LinearLayout) view.findViewById(R.id.lay_mymessage);
		lay_mymessage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent;
				intent = new Intent(getActivity(), Message_MainAct.class);
				startActivity(intent);
			}
		});
		lay_mycollect = (LinearLayout) view.findViewById(R.id.lay_mycollect);
		lay_mycollect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent;
				intent = new Intent(getActivity(), Collect_MainAct.class);
				startActivity(intent);
			}
		});
		tv_company_phone = (TextView) view.findViewById(R.id.tv_company_phone);
		tv_company_phone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Intent intent;
				// intent = new Intent(getActivity(),
				// AddServices_HistoryAct.class);
				// startActivity(intent);
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ "400-968-7099"));
				getActivity().startActivity(intent);
			}
		});
		lay_mine_customer_service = (LinearLayout) view
				.findViewById(R.id.lay_mine_customer_service);
		lay_mine_customer_service.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), Add_FriendAct.class);
				getActivity().startActivity(intent);
			}
		});
		tv_membertype = (TextView) view.findViewById(R.id.tv_membertype);
		if (member_type.equals("1")) {
			tv_membertype.setText("您好，你不是认证会员，点击认证！");
		} else if (member_type.equals("2")) {
			tv_membertype.setText("您好，你是普通会员，感谢支持！");
		} else if (member_type.equals("3")) {
			tv_membertype.setText("您好，您是白银会员，感谢支持！");
		} else if (member_type.equals("4")) {
			tv_membertype.setText("您好，您是白金会员，感谢支持！");
		} else {
			tv_membertype.setText("您好，你的会员已到期，点击查看！");
		}
		tv_mine_act_name = (TextView) view.findViewById(R.id.tv_mine_act_name);
		tv_mine_act_name.setText(cn_username + en_username);
		tv_mine_act_identy = (TextView) view
				.findViewById(R.id.tv_mine_act_identy);
		if (type.equals("1")) {
			tv_mine_act_identy.setText("开发商   " + company);
		} else if (type.equals("2")) {
			tv_mine_act_identy.setText("代理商   " + company);
		} else if (type.equals("3")) {
			tv_mine_act_identy.setText("运营商   " + company);
		} else if (type.equals("102")) {
			tv_mine_act_identy.setText("经纪人   " + company);
		} else if (type.equals("103")) {
			tv_mine_act_identy.setText("物业公司   " + company);
		} else if (type.equals("104")){
			tv_mine_act_identy.setText("企业服务商   " + company);
		}else {
			tv_mine_act_identy.setText("其他   " + company);
		}
		// 设置头像
		mine_act_head = (ImageView) view.findViewById(R.id.mine_act_head);
		mine_act_head.setImageResource(R.drawable.logo);
		ImageLoader.getInstance().displayImage(avatar, mine_act_head, options);
		// imageLoader.loadImage(avatar, 100, 100, mine_act_head);
		lay_myhourse = (LinearLayout) view.findViewById(R.id.lay_myhourse);
		lay_setting = (LinearLayout) view.findViewById(R.id.lay_setting);
		lay_mycustomer = (LinearLayout) view.findViewById(R.id.lay_mycustomer);
		// lay_help_center = (LinearLayout) view
		// .findViewById(R.id.lay_help_center);
		// lay_about_mrlou = (LinearLayout) view
		// .findViewById(R.id.lay_about_mrlou);
		lay_history_complete = (LinearLayout) view
				.findViewById(R.id.lay_history_complete);
		lay_mypost = (LinearLayout) view.findViewById(R.id.lay_mypost);
		// lay_feedback = (LinearLayout) view.findViewById(R.id.lay_feedback);
		lay_myhourse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = false;
				Intent intent = new Intent(getActivity(), My_HourseAct.class);
				startActivity(intent);
			}
		});
		lay_added_service = (LinearLayout) view
				.findViewById(R.id.lay_added_service);
		lay_added_service.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						Main_addServices.class);
				startActivity(intent);
			}
		});
		lay_membertype = (LinearLayout) view.findViewById(R.id.lay_membertype);
		lay_membertype.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						Elite_meetingAct.class);
				startActivity(intent);
			}
		});
		lay_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = true;
				Intent intent;
				if (type.equals("299")) {
					intent = new Intent(getActivity(), Setting_personal01.class);
					intent.putExtra("cn_username", cn_username);
					intent.putExtra("en_username", en_username);
					intent.putExtra("email", email);
					intent.putExtra("phone_number", phone_number);
					intent.putExtra("type", type);
					intent.putExtra("user_id", user_id);
					intent.putExtra("avatar", avatar);
					intent.putExtra("avatar_id", avatar_id);
					intent.putExtra("company", company);
					intent.putExtra("wechat", wechat);
					intent.putExtra("wechat_qr", wechat_qr);
					intent.putExtra("post_card", post_card);
					intent.putExtra("wechat_qr_id", wechat_qr_id);
					intent.putExtra("post_card_id", post_card_id);
					intent.putExtra("company_id", company_id);
					intent.putExtra("v", add_v);
				} else if (type.equals("102")) {
					intent = new Intent(getActivity(), Setting_personal02.class);
					intent.putExtra("cn_username", cn_username);
					intent.putExtra("en_username", en_username);
					intent.putExtra("email", email);
					intent.putExtra("phone_number", phone_number);
					intent.putExtra("type", type);
					intent.putExtra("avatar", avatar);
					intent.putExtra("company", company);
					intent.putExtra("avatar_id", avatar_id);
					intent.putExtra("user_id", user_id);
					intent.putExtra("intro", intro);
					intent.putExtra("wechat_qr_id", wechat_qr_id);
					intent.putExtra("post_card_id", post_card_id);
					intent.putExtra("work_start", work_start);
					intent.putExtra("region_id", region_id);
					intent.putExtra("region_id_real", region_id_real);
					intent.putExtra("wechat", wechat);
					intent.putExtra("wechat_qr", wechat_qr);
					intent.putExtra("post_card", post_card);
					intent.putExtra("v", add_v);
					intent.putExtra("company_id", company_id);
					intent.putExtra("office_building", office_building);
					intent.putExtra("office_building_id", office_building_id);
					intent.putExtra("good_type", good_type);
				} else if (type.equals("104")) {
					intent = new Intent(getActivity(), Setting_personal04.class);
					intent.putExtra("cn_username", cn_username);
					intent.putExtra("en_username", en_username);
					intent.putExtra("email", email);
					intent.putExtra("phone_number", phone_number);
					intent.putExtra("company", company);
					intent.putExtra("type", type);
					intent.putExtra("intro", intro);
					intent.putExtra("user_id", user_id);
					intent.putExtra("work_start", work_start);
					intent.putExtra("avatar", avatar);
					intent.putExtra("service_type", service_type);
					intent.putExtra("wechat_qr_id", wechat_qr_id);
					intent.putExtra("post_card_id", post_card_id);
					intent.putExtra("avatar_id", avatar_id);
					intent.putExtra("region_id", region_id);
					intent.putExtra("region_id_real", region_id_real);
					intent.putExtra("wechat", wechat);
					intent.putExtra("wechat_qr", wechat_qr);
					intent.putExtra("post_card", post_card);
					intent.putExtra("v", add_v);
					intent.putExtra("company_id", company_id);
					intent.putExtra("office_building", office_building);
					intent.putExtra("office_building_id", office_building_id);
				} else {
					intent = new Intent(getActivity(), Setting_personal03.class);
					intent.putExtra("cn_username", cn_username);
					intent.putExtra("en_username", en_username);
					intent.putExtra("email", email);
					intent.putExtra("phone_number", phone_number);
					intent.putExtra("company", company);
					intent.putExtra("type", type);
					intent.putExtra("user_id", user_id);
					intent.putExtra("avatar", avatar);
					intent.putExtra("wechat_qr_id", wechat_qr_id);
					intent.putExtra("post_card_id", post_card_id);
					intent.putExtra("avatar_id", avatar_id);
					intent.putExtra("building_a", building_a);
					intent.putExtra("building_b", building_b);
					intent.putExtra("wechat", wechat);
					intent.putExtra("wechat_qr", wechat_qr);
					intent.putExtra("post_card", post_card);
					intent.putExtra("v", add_v);
					intent.putExtra("company_id", company_id);
				}
				startActivity(intent);
			}
		});
		lay_mycustomer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = false;
				Intent intent = new Intent(getActivity(), Customer_ManAct.class);
				startActivity(intent);
			}
		});
		// lay_feedback.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// flag = false;
		// Intent intent = new Intent(getActivity(), FeedBack_Act.class);
		// startActivity(intent);
		// }
		// });
		lay_mypost.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = false;
				Intent intent = new Intent(getActivity(), MyPost_Act.class);
				startActivity(intent);
			}
		});

		// lay_help_center.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// flag = false;
		// Intent intent = new Intent(getActivity(), Help_CenterAct.class);
		// startActivity(intent);
		// }
		// });
		// lay_about_mrlou.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// flag = false;
		// Intent intent = new Intent(getActivity(), About_MrlouAct.class);
		// startActivity(intent);
		// }
		// });
		lay_history_complete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flag = false;
				if (type.equals("102")) {
					Intent intent = new Intent(getActivity(),
							History_CompleteAct.class);
					intent.putExtra("add_v", add_v);
					startActivity(intent);
				} else {
					Intent intent = new Intent(getActivity(),
							AddServices_HistoryAct.class);
					intent.putExtra("add_v", add_v);
					startActivity(intent);
				}

			}
		});
		if (type.equals("299")) {
			lay_history_complete.setVisibility(View.GONE);
			lay_mypost.setVisibility(View.GONE);
			lay_mycustomer.setVisibility(View.GONE);
		} else if (type.equals("102")) {
			lay_history_complete.setVisibility(View.VISIBLE);
			lay_mypost.setVisibility(View.VISIBLE);
			lay_mycustomer.setVisibility(View.VISIBLE);
		} else if (type.equals("104")) {
			lay_mypost.setVisibility(View.GONE);
			lay_mycustomer.setVisibility(View.GONE);
		} else {
			lay_history_complete.setVisibility(View.GONE);
		}
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
				Toast.makeText(getActivity(), "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				dialog.dismiss();
				Toast.makeText(getActivity(), "网络异常，请重新尝试连接", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC_COLLECT) {
			dialog.dismiss();
			JSONObject json, json2;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				String msg = BaseHelper.getString(json, "msg");
				if (errorid.equals("0")) {
					json2 = json.getJSONObject("user");
					phone_number = json2.getString("phone_number");
					email = json2.getString("email");
					en_username = json2.getString("en_username");
					cn_username = json2.getString("cn_username");
					company = json2.getString("company");
					avatar = json2.getString("avatar");
					avatar_id = json2.getString("avatar_id");
					type = json2.getString("type");
					wechat_qr_id = json2.getString("wechat_qr_id");
					post_card_id = json2.getString("post_card_id");
					post_card = json2.getString("post_card");
					wechat_qr = json2.getString("wechat_qr");
					wechat = json2.getString("wechat");
					if (type.equals("102")) {
						intro = json2.getString("intro");
						work_start = json2.getString("start_work");
						region_id = json2.getString("region_id");
						region_id_real = json2.getString("region_id_real");
						add_v = json2.getString("v");
					} else if (!type.equals("299")) {
						building_a = json2.getString("building_a");
						building_b = json2.getString("building_b");
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
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (flag) {
			dialog.dialog();
			setrefresh(User_info.getSession_id(), User_info.getUser_id());
		}
		if (User_info.getAdd_v()!=null) {
			if (User_info.getAdd_v().equals("0")) {
				AlertDialogs dialogs=new AlertDialogs();
				dialogs.alertDialogs(getActivity(),"您尚未认证会员,进入易楼精英汇认证","现在认证");
			}
		}
		super.onResume();
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	public void setrefresh(String session_id, String user_id) {
		select_perinfo(session_id, user_id);
	}

	public void select_perinfo(String session_id, String user_id) {
		Request request = Request.requestgetperinfo(session_id, user_id);
		new HttpGetThread(request, this);
	}

}
