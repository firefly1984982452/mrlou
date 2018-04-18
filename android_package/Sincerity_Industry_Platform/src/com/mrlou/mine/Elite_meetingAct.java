package com.mrlou.mine;


import java.io.FileNotFoundException;

import org.json.JSONException;
import org.json.JSONObject;

import com.mrlou.activity.InitActivity;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.mrlou.view.ZoomImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 *@author jx_chen
 *@date 2016-10-20---下午3:39:57
 *@problem
 */
public class Elite_meetingAct extends Activity implements HttpListener{

	private TextView tv_member_introduce,tv_elite_member_notify;
	private String content,avatar,verify_time,status,session_id,user_type;
	private LinearLayout lay_elite_back,lay_member_add,lay_member_renew,lay_elite_verfity;
	private ImageView iv_elite;
	private ImageView imageView;
	private SharedPreferences sharedPreferences;
	private DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_elite_meeting);
		sharedPreferences = Elite_meetingAct.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		user_type =sharedPreferences.getString("type_id", "");
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(Elite_meetingAct.this));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.imgbg) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.imgbg) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.imgbg) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
//				.displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build();
		if (user_type.equals("102")) {
			select_Elite("2");
		}else if (user_type.equals("299")) {
			select_Elite("3");
		}else if(user_type.equals("104")){
			select_Elite("4");
		}else {
			select_Elite("1");
		}
		
		findView();
//		Init();
	}

	private void Init() {
		// TODO Auto-generated method stub
		tv_member_introduce.setText(content);
		tv_elite_member_notify=(TextView) findViewById(R.id.tv_elite_member_notify);
		lay_elite_verfity=(LinearLayout) findViewById(R.id.lay_elite_verfity);
		iv_elite=(ImageView) findViewById(R.id.iv_elite);
		ImageLoader.getInstance().displayImage(avatar, iv_elite, options);
		lay_member_renew=(LinearLayout) findViewById(R.id.lay_member_renew);
		lay_member_renew.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Elite_meetingAct.this,Elite_meetingAct02.class);
				intent.putExtra("type", "会员续费");
				startActivity(intent);
			}
		});
		if (status.equals("0")) {
			lay_elite_verfity.setVisibility(View.GONE);
		}else {
			tv_elite_member_notify.setText("您将于"+verify_time+"会员到期，请提前续期");
			lay_member_add.setVisibility(View.INVISIBLE);
		}
	}

	private void findView() {
		// TODO Auto-generated method stub
		tv_member_introduce=(TextView) findViewById(R.id.tv_member_introduce);
		lay_elite_back=(LinearLayout) findViewById(R.id.lay_elite_back);
		lay_elite_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		lay_member_add=(LinearLayout) findViewById(R.id.lay_member_add);
		lay_member_add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Elite_meetingAct.this,Elite_meetingAct02.class);
				intent.putExtra("type", "加入会员");
				startActivity(intent);
			}
		});
	}

	public void select_Elite(String type) {
		Request request = Request.requestelite(session_id,type);
		new HttpThread(request, this);
	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Init();
				break;
			case 2:
				Toast.makeText(Elite_meetingAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(Elite_meetingAct.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};
	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC) {
			JSONObject json, json2;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					json2 = json.getJSONObject("list");
					content = json2.getString("content");
					avatar=json2.getString("avatar");
					verify_time=json2.getString("verify_time");
					status=json2.getString("status");
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
	private Bitmap decodeUriAsBitmap(Uri uri) {

		Bitmap bitmap = null;

		try {

			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri));

		} catch (FileNotFoundException e) {

			e.printStackTrace();

			return null;

		}

		return bitmap;

	}
	@Override
	public void doError(String s) {
		// TODO Auto-generated method stub
		if (s.equals("请求异常")) {
			newHandler.sendEmptyMessage(99);
		}
	}
	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					Elite_meetingAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}
}
