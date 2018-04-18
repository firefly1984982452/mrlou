package com.mrlou.addservices;

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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mrlou.economic.Economic_CompanyAct;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 导航页面
 * 
 */
public class GuidePage extends Activity implements HttpListener{

	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private ViewGroup main, group;
	private View main_view;
	private ImageView imageView, iv_guidepage, iv_guidepage2, iv_guidepage3,
			iv_guidepage4, iv_guidepage5, iv_guidepage6, iv_guidepage7,
			iv_guidepage8, iv_guidepage9;
	private ImageView[] imageViews;
	private View view, view2, view3, view4, view5, view6, view7, view8, view9;
	private DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private ArrayList<String> str_case_image = new ArrayList<String>();
	private LinearLayout lay_guidepage_back;
	private SharedPreferences sharedPreferences;
	private String session_id, id;;
	private boolean flags=false;
	private long startTime = 0l;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		sharedPreferences = GuidePage.this.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		select_cjinfo(session_id, id);
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(GuidePage.this));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.imgbg) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.imgbg) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.imgbg) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build();

	}

	private void findView() {
		LayoutInflater inflater = getLayoutInflater();
		pageViews = new ArrayList<View>();
		view = inflater.inflate(R.layout.advertiment_item01, null);
		view2 = inflater.inflate(R.layout.advertiment_item01, null);
		view3 = inflater.inflate(R.layout.advertiment_item01, null);
		view4 = inflater.inflate(R.layout.advertiment_item01, null);
		view5 = inflater.inflate(R.layout.advertiment_item01, null);
		view6 = inflater.inflate(R.layout.advertiment_item01, null);
		view7 = inflater.inflate(R.layout.advertiment_item01, null);
		view8 = inflater.inflate(R.layout.advertiment_item01, null);
		view9 = inflater.inflate(R.layout.advertiment_item01, null);
		select_pic(str_case_image.size());
		imageViews = new ImageView[pageViews.size()];
		main_view = inflater.inflate(R.layout.guide_page, null);
		group = (ViewGroup) main_view.findViewById(R.id.viewGroup);
		viewPager = (ViewPager) main_view.findViewById(R.id.guidePages);
		setContentView(main_view);
		lay_guidepage_back = (LinearLayout) main_view
				.findViewById(R.id.lay_guidepage_back);
//		lay_guidepage_back.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				System.out.println("--测试6--");
//				simulateKey(KeyEvent.KEYCODE_BACK);
//			}
//		});
		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		  boolean handle = false;
		    if(onTouchEvent(ev)){
		    	flags=true;
		    	simulateKey(KeyEvent.KEYCODE_BACK);
//		        handle = lay_guidepage_back.dispatchTouchEvent(ev);
		    }else{
		        handle = viewPager.dispatchTouchEvent(ev);
		    }
		    return handle;
	}
	
	public boolean onInterceptTouchEvent(MotionEvent ev){
		boolean flag=true;
		if (lay_guidepage_back.dispatchTouchEvent(ev)) {
			flag=true;
		}else {
			flag=false;
		}
		return flag;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		boolean flag=true;
		if (event.getX()<200) {
			flag=true;
		}else {
			flag=false;
		}
		 return flag;
	}
	
	private void select_pic(int image) {
		// TODO Auto-generated method stub
		switch (image) {
		case 1:
			iv_guidepage = (ImageView) view
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(0),
					iv_guidepage, options);
			pageViews.add(view);
			break;
		case 2:
			iv_guidepage = (ImageView) view
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(0),
					iv_guidepage, options);
			pageViews.add(view);
			iv_guidepage2 = (ImageView) view2
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(1),
					iv_guidepage2, options);
			pageViews.add(view2);
			break;
		case 3:
			iv_guidepage = (ImageView) view
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(0),
					iv_guidepage, options);
			pageViews.add(view);
			iv_guidepage2 = (ImageView) view2
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(1),
					iv_guidepage2, options);
			pageViews.add(view2);
			iv_guidepage3 = (ImageView) view3
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(2),
					iv_guidepage3, options);
			pageViews.add(view3);
			break;
		case 4:
			iv_guidepage = (ImageView) view
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(0),
					iv_guidepage, options);
			pageViews.add(view);
			iv_guidepage2 = (ImageView) view2
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(1),
					iv_guidepage2, options);
			pageViews.add(view2);
			iv_guidepage3 = (ImageView) view3
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(2),
					iv_guidepage3, options);
			pageViews.add(view3);
			iv_guidepage4 = (ImageView) view4
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(3),
					iv_guidepage4, options);
			pageViews.add(view4);
			break;
		case 5:
			iv_guidepage = (ImageView) view
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(0),
					iv_guidepage, options);
			pageViews.add(view);
			iv_guidepage2 = (ImageView) view2
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(1),
					iv_guidepage2, options);
			pageViews.add(view2);
			iv_guidepage3 = (ImageView) view3
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(2),
					iv_guidepage3, options);
			pageViews.add(view3);
			iv_guidepage4 = (ImageView) view4
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(3),
					iv_guidepage4, options);
			pageViews.add(view4);
			iv_guidepage5 = (ImageView) view5
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(4),
					iv_guidepage5, options);
			pageViews.add(view5);
			break;
		case 6:
			iv_guidepage = (ImageView) view
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(0),
					iv_guidepage, options);
			pageViews.add(view);
			iv_guidepage2 = (ImageView) view2
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(1),
					iv_guidepage2, options);
			pageViews.add(view2);
			iv_guidepage3 = (ImageView) view3
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(2),
					iv_guidepage3, options);
			pageViews.add(view3);
			iv_guidepage4 = (ImageView) view4
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(3),
					iv_guidepage4, options);
			pageViews.add(view4);
			iv_guidepage5 = (ImageView) view5
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(4),
					iv_guidepage5, options);
			pageViews.add(view5);
			iv_guidepage6 = (ImageView) view6
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(5),
					iv_guidepage6, options);
			pageViews.add(view6);
			break;
		case 7:
			iv_guidepage = (ImageView) view
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(0),
					iv_guidepage, options);
			pageViews.add(view);
			iv_guidepage2 = (ImageView) view2
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(1),
					iv_guidepage2, options);
			pageViews.add(view2);
			iv_guidepage3 = (ImageView) view3
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(2),
					iv_guidepage3, options);
			pageViews.add(view3);
			iv_guidepage4 = (ImageView) view4
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(3),
					iv_guidepage4, options);
			pageViews.add(view4);
			iv_guidepage5 = (ImageView) view5
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(4),
					iv_guidepage5, options);
			pageViews.add(view5);
			iv_guidepage6 = (ImageView) view6
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(5),
					iv_guidepage6, options);
			pageViews.add(view6);
			iv_guidepage7 = (ImageView) view7
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(6),
					iv_guidepage7, options);
			pageViews.add(view7);
			break;
		case 8:
			iv_guidepage = (ImageView) view
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(0),
					iv_guidepage, options);
			pageViews.add(view);
			iv_guidepage2 = (ImageView) view2
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(1),
					iv_guidepage2, options);
			pageViews.add(view2);
			iv_guidepage3 = (ImageView) view3
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(2),
					iv_guidepage3, options);
			pageViews.add(view3);
			iv_guidepage4 = (ImageView) view4
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(3),
					iv_guidepage4, options);
			pageViews.add(view4);
			iv_guidepage5 = (ImageView) view5
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(4),
					iv_guidepage5, options);
			pageViews.add(view5);
			iv_guidepage6 = (ImageView) view6
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(5),
					iv_guidepage6, options);
			pageViews.add(view6);
			iv_guidepage7 = (ImageView) view7
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(6),
					iv_guidepage7, options);
			pageViews.add(view7);
			iv_guidepage8 = (ImageView) view8
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(7),
					iv_guidepage8, options);
			pageViews.add(view8);
			break;
		case 9:
			iv_guidepage = (ImageView) view
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(0),
					iv_guidepage, options);
			pageViews.add(view);
			iv_guidepage2 = (ImageView) view2
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(1),
					iv_guidepage2, options);
			pageViews.add(view2);
			iv_guidepage3 = (ImageView) view3
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(2),
					iv_guidepage3, options);
			pageViews.add(view3);
			iv_guidepage4 = (ImageView) view4
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(3),
					iv_guidepage4, options);
			pageViews.add(view4);
			iv_guidepage5 = (ImageView) view5
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(4),
					iv_guidepage5, options);
			pageViews.add(view5);
			iv_guidepage6 = (ImageView) view6
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(5),
					iv_guidepage6, options);
			pageViews.add(view6);
			iv_guidepage7 = (ImageView) view7
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(6),
					iv_guidepage7, options);
			pageViews.add(view7);
			iv_guidepage8 = (ImageView) view8
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(7),
					iv_guidepage8, options);
			pageViews.add(view8);
			iv_guidepage9 = (ImageView) view9
					.findViewById(R.id.iv_guidepage_image);
			ImageLoader.getInstance().displayImage(str_case_image.get(8),
					iv_guidepage9, options);
			pageViews.add(view9);
			break;
		}
	}

	class GuidePageAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return pageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(pageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(pageViews.get(arg1));
			return pageViews.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public void finishUpdate(View arg0) {

		}
	}

	class GuidePageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
//			for (int i = 0; i < imageViews.length; i++) {
//				imageViews[arg0]
//						.setBackgroundResource(R.drawable.page_indicator_focused);
//				if (arg0 != i) {
//					imageViews[i]
//							.setBackgroundResource(R.drawable.page_indicator);
//				}
//			}
		}
	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		long tempTime = System.currentTimeMillis();
		if (tempTime - startTime <= 4000) {
			new Thread() {
				public void run() {
					try {
						System.out.println("--测试返回1--");
						Instrumentation inst = new Instrumentation();
						inst.sendKeyDownUpSync(KeyCode);
						GuidePage.this.finish();
					} catch (Exception e) {
						Log.e("Exception when sendKeyDownUpSync", e.toString());
					}
				}
			}.start();
		} else {
			startTime = tempTime;
		}
	}

	public void select_cjinfo(String session_id, String id) {
		Request request = Request.request_addservice_cjinfo(session_id, id);
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
				Toast.makeText(GuidePage.this, "暂无数据", 1000).show();
				break;
			case 99:
				Toast.makeText(GuidePage.this, "网络异常，请重新尝试连接", 1000).show();
				break;
			}
		}
	};

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_NEW) {
			JSONObject json, json2;
			JSONArray joArray2;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					json2 = json.getJSONObject("list");
					joArray2 = json2.getJSONArray("case_list");
					for (int i = 0; i < joArray2.length(); i++) {
						json = joArray2.getJSONObject(i);
						str_case_image.add(json.getString("url"));
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