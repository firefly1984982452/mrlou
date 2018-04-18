package com.mrlou.economic;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.mapcore.util.bt;
import com.mrlou.activity.MainFragmentActivity;
import com.mrlou.addservices.Main_addServices;
import com.mrlou.economic.view.BaseTools;
import com.mrlou.economic.view.ColumnHorizontalScrollView;
import com.mrlou.economic_service.Economic_ServiceAct;
import com.mrlou.h5.Map_ClickAct;
import com.mrlou.h5.Ylb_act;
import com.mrlou.listener.HttpListener;
import com.mrlou.message.Message_NewHourseAct;
import com.mrlou.mine.Elite_meetingAct;
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.mrlou.util.User_info;
import com.mrlou.yi.Yi_Activity;
import com.umeng_social_sdk_res_lib.R.string;

public class Economic_mianAct extends Fragment implements HttpListener {
	/** 自定义HorizontalScrollView */
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	LinearLayout mRadioGroup_content;
	LinearLayout ll_more_columns;
	RelativeLayout rl_column;
	private ViewPager mViewPager;
	private ImageView button_more_columns;
	private Economic_mainFragment eco_fragment;
	private String[] area_id = { "310115", "310101", "310106", "310104",
			"310105", "310107", "310108", "310109", "310110", "310113",
			"310112", "310114", "310118", "310117", "310120", "310116",
			"310230" };
	private String[] str_area = { "浦东新区", "黄浦区", "静安区", "徐汇区", "长宁区", "普陀区",
			"闸北区", "虹口区", "杨浦区", "宝山区", "闵行区", "嘉定区", "青浦区", "松江区", "奉贤区",
			"金山区", "崇明区" };
	private int[] iv_drawble = { R.drawable.pudong_dark,
			R.drawable.huangpu_dark, R.drawable.jingan_dark,
			R.drawable.xuhui_dark, R.drawable.changning_dark,
			R.drawable.putuo_dark, R.drawable.zhebei_dark,
			R.drawable.hongkou_dark, R.drawable.yangpu_dark,
			R.drawable.baoshan_dark, R.drawable.minhang_dark,
			R.drawable.jiading_dark, R.drawable.qingpu_dark,
			R.drawable.songjiang_dark, R.drawable.fengxian_dark,
			R.drawable.jinshan_dark, R.drawable.congming_dark };
	private int[] iv_drawble2 = { R.drawable.pudong_light,
			R.drawable.huangpu_light, R.drawable.jingan_light,
			R.drawable.xuhui_light, R.drawable.changning_light,
			R.drawable.putuo_light, R.drawable.zhebei_light,
			R.drawable.hongkou_light, R.drawable.yangpu_light,
			R.drawable.baoshan_light, R.drawable.minhang_light,
			R.drawable.jiading_light, R.drawable.qingpu_light,
			R.drawable.songjiang_light, R.drawable.fengxian_light,
			R.drawable.jinshan_light, R.drawable.congming_light };
	/** 当前选中的栏目 */
	private int columnSelectIndex = 0;
	/** 左阴影部分 */
	public ImageView shade_left;
	/** 右阴影部分 */
	public ImageView shade_right;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	/** Item宽度 */
	private int mItemWidth = 0;
	/** fragment 集合列表 */
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

	/** 请求CODE */
	public final static int CHANNELREQUEST = 1;
	/** 调整返回的RESULTCODE */
	public final static int CHANNELRESULT = 10;
	private ArrayList<String> str_avatar, str_name, str_company, str_work_age,
			str_all_total, str_region_total, str_phone, str_id, str_rname,
			str_company_status, str_rid;
	private View view;
	private LinearLayout lay_search;
	private SweetAlertDialog pDialog;
	private Progress_Dialog dialog;
	private SharedPreferences sharedPreferences;
	private ImageView  iv_add;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.economic_main, container, false);
		mScreenWidth = BaseTools.getWindowsWidth(getActivity());
		mItemWidth = mScreenWidth / 5;// 一个Item宽度为屏幕的1/5
		prepare();
		initView();
		return view;
	}

	private void prepare() {
		// TODO Auto-generated method stub
		sharedPreferences = getActivity().getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		Bundle args = getArguments();
		str_avatar = args.getStringArrayList("avatar");
		str_region_total = args.getStringArrayList("region_total");
		str_company = args.getStringArrayList("company");
		str_work_age = args.getStringArrayList("work_age");
		str_name = args.getStringArrayList("name");
		str_all_total = args.getStringArrayList("all_total");
		str_phone = args.getStringArrayList("phone");
		str_id = args.getStringArrayList("id");
		str_rname = args.getStringArrayList("rname");
		str_company_status = args.getStringArrayList("company_status");
		str_rid = args.getStringArrayList("rid");
	}

	/** 初始化layout控件 */
	private void initView() {

		// 弹出请求对话框
		pDialog = new SweetAlertDialog(getActivity(),
				SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
		dialog = new Progress_Dialog(getActivity(), pDialog);

		mColumnHorizontalScrollView = (ColumnHorizontalScrollView) view
				.findViewById(R.id.mColumnHorizontalScrollView);
		// 水平滚动条 标题处
		mRadioGroup_content = (LinearLayout) view
				.findViewById(R.id.mRadioGroup_content);
		// 水平滚动条的布局 Relativelayout
		rl_column = (RelativeLayout) view.findViewById(R.id.rl_column);
		// 主页面的fragment 添加处
		mViewPager = (ViewPager) view.findViewById(R.id.mViewPager);
		shade_left = (ImageView) view.findViewById(R.id.shade_left);
		shade_right = (ImageView) view.findViewById(R.id.shade_right);
		setChangelView();
	}

	/**
	 * 当栏目项发生变化时候调用
	 * */
	private void setChangelView() {
		// initColumnData();
		initTabColumn();
		initFragment();
	}

	/**
	 * 初始化Column栏目项
	 * */
	private void initTabColumn() {
		mRadioGroup_content.removeAllViews();
		// int count = userChannelList.size();
		mColumnHorizontalScrollView.setParam(getActivity(), mScreenWidth,
				mRadioGroup_content, shade_left, shade_right, ll_more_columns,
				rl_column);
		for (int i = 0; i < iv_drawble.length; i++) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					mItemWidth, LayoutParams.FILL_PARENT);
			params.leftMargin = 10;
			params.rightMargin = 10;
			params.topMargin = 25;
			params.bottomMargin = 25;
			Button bt_area = new Button(getActivity());
			bt_area.setBackgroundDrawable(getActivity().getResources()
					.getDrawable(iv_drawble[i]));
			bt_area.setText(str_area[i]);
			bt_area.setTextSize(13);
			bt_area.setTextColor(getActivity().getResources()
					.getColorStateList(R.color.white));
			bt_area.setGravity(Gravity.CENTER);
			bt_area.setPadding(10, 10, 10, 10);
			bt_area.setId(i);
			if (columnSelectIndex == i) {
				bt_area.setSelected(true);
			}

			bt_area.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
						View localView = mRadioGroup_content.getChildAt(i);
						if (localView != v)
							localView.setSelected(false);
						else {
							dialog.dialog();
							select_area(area_id[i]);
							v.setBackgroundDrawable(getActivity()
									.getResources().getDrawable(iv_drawble2[i]));
							localView.setSelected(true);
							mViewPager.setCurrentItem(i);
							selectTab(i);
						}
					}
				}
			});
			mRadioGroup_content.addView(bt_area, i, params);
		}
		iv_add = (ImageView) view.findViewById(R.id.iv_economic_add);
		iv_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						Map_ClickAct.class);
				intent.putExtra("type", "2");
				intent.putExtra("url_map", "https://appapi.imrlou.com/map/index.html?client=android&search=jjgs");
				startActivity(intent);
			}
		});
		lay_search = (LinearLayout) view
				.findViewById(R.id.lay_economic_main_search);
		lay_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						Economic_SearchAct.class);
				startActivity(intent);
			}
		});
		init_view(0);
	}

	private void init_view(int tab) {
		// TODO Auto-generated method stub
		// 判断是否选中
		for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
			View checkView = mRadioGroup_content.getChildAt(j);
			if (j == tab) {
				checkView.setBackgroundDrawable(getActivity().getResources()
						.getDrawable(iv_drawble2[j]));
			}
			checkView.setSelected(true);
		}
	}

	/**
	 * 选择的Column里面的Tab
	 * */
	private void selectTab(int tab_postion) {
		columnSelectIndex = tab_postion;
		for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
			View checkView = mRadioGroup_content.getChildAt(tab_postion);
			int k = checkView.getMeasuredWidth();
			int l = checkView.getLeft();
			int i2 = l + k / 2 - mScreenWidth / 2;
			// rg_nav_content.getParent()).smoothScrollTo(i2, 0);
			// mColumnHorizontalScrollView.setAnimation(animation);
			mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
			// mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
			// mItemWidth , 0);
		}
		// 判断是否选中
		for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
			View checkView = mRadioGroup_content.getChildAt(j);
			boolean ischeck;
			if (j == tab_postion) {
				ischeck = true;
			} else {
				checkView.setBackgroundDrawable(getActivity().getResources()
						.getDrawable(iv_drawble[j]));
				ischeck = false;
			}
			checkView.setSelected(ischeck);
		}
	}

	/**
	 * 初始化Fragment,并添加不同的fragment界面
	 * */
	private void initFragment() {
		fragments.clear();// 清空
		// int count = area_id.length;
		// for (int i = 0; i < count; i++) {
		Bundle data = new Bundle();
		data.putStringArrayList("id", str_id);
		data.putStringArrayList("name", str_name);
		data.putStringArrayList("phone", str_phone);
		data.putStringArrayList("company", str_company);
		data.putStringArrayList("region_total", str_region_total);
		data.putStringArrayList("all_total", str_all_total);
		data.putStringArrayList("rname", str_rname);
		data.putStringArrayList("avatar", str_avatar);
		data.putStringArrayList("company_status", str_company_status);
		data.putStringArrayList("rid", str_rid);
		data.putStringArrayList("work_age", str_work_age);
		eco_fragment = new Economic_mainFragment();
		eco_fragment.setArguments(data);
		fragments.add(eco_fragment);
		// }
		EconomicFragmentPagerAdapter mAdapetr = new EconomicFragmentPagerAdapter(
				getActivity().getSupportFragmentManager(), fragments);
		// mViewPager.setOffscreenPageLimit(0);
		mViewPager.setAdapter(mAdapetr);
		mViewPager.setOnPageChangeListener(pageListener);

	}

	/**
	 * ViewPager切换监听方法
	 * */
	public OnPageChangeListener pageListener = new OnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			mViewPager.setCurrentItem(position);
			selectTab(position);
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
					clean_list();
					joArray = json.getJSONArray("list");
					int lenth = joArray.length();
					for (int i = 0; i < lenth; i++) {
						json = joArray.getJSONObject(i);
						str_id.add(json.getString("id"));
						str_name.add(json.getString("cn_username")
								+ json.getString("en_username"));
						str_avatar.add(json.getString("avatar"));
						str_company.add(json.getString("company"));
						str_phone.add(json.getString("phone"));
						str_region_total.add(json.getString("region_total"));
						str_all_total.add(json.getString("all_total"));
						str_rid.add(json.getString("rid"));
						str_rname.add(json.getString("rname"));
						str_work_age.add(json.getString("work_age"));
						str_company_status
								.add(json.getString("company_status"));
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

	public void select_area(String id) {
		Request request = Request.requesteconomicmain(id);
		new HttpThread(request, this);
	}

	@Override
	public void doError(String s) {
		// TODO Auto-generated method stub
		if (s.equals("请求异常")) {
			newHandler.sendEmptyMessage(99);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				dialog.dismiss();
				// eco_fragment.onDestroyView();
				// setChangelView();
				// initFragment();
				eco_fragment.setVis(true);
				eco_fragment.setAdapter(str_avatar, str_name, str_phone,
						str_company, str_work_age, str_all_total,
						str_region_total, str_company_status, str_rname,
						str_id, str_rid);
				break;
			case 2:
				dialog.dismiss();
				eco_fragment.setVis(false);
				break;
			case 99:
				dialog.dismiss();
				Toast.makeText(getActivity(), "网络连接失败，请重新尝试连接", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	public void clean_list() {
		str_all_total.clear();
		str_avatar.clear();
		str_company.clear();
		str_company_status.clear();
		str_id.clear();
		str_name.clear();
		str_phone.clear();
		str_region_total.clear();
		str_rid.clear();
		str_rname.clear();
		str_work_age.clear();
	}
}