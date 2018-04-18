package com.mrlou.mine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager.LayoutParams;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mrlou.economic.Economic_personalAct;
import com.mrlou.h5.Exit_Act;
import com.mrlou.identify.Identify_Act;
import com.mrlou.identify.Identity_choose_AreaAct;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.mrlou.util.User_info;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * @author jx_chen
 * @date 2016-10-31---上午11:21:57
 * @problem
 */
public class Setting_personal04 extends Activity implements HttpListener {

	private LinearLayout lay_area, lay_head, lay_post, lay_wechat,
			lay_setting04_back, lay_services, lay_setting_modify_04,
			lay_setting04_type_more, 
			lay_setting04_type_renzheng,
			lay_setting04_c_name_renzheng, lay_setting04_company_renzheng,lay_setting04_company_more,
			lay_setting04_intro, lay_setting04_company, lay_setting04_building;
	private EditText et_c_name, et_e_name, et_email, et_wechat;
	private TextView  tv_worktime, tv_services, tv_setting04_phone,
			tv_setting04_intro, tv_setting04_company,tv_setting04_building;
	private ImageView iv_head, iv_post, iv_wechat;
	private Button bt_setting04_comfirm, bt_setting04_exit;
	private PopupWindow popwindow;
	private View view;
	private LinearLayout lay_01, lay_02, lay_03, lay_04, lay_05, lay_06,
			lay_07, lay_08, lay_09, lay_10;
	private SweetAlertDialog pDialog;
	private Progress_Dialog dialog;
	private SharedPreferences sharedPreferences;
	private File mPhotoFile;
	private Uri imageuri;
	private int CAMERA_RESULT = 100;
	private int RESULT_LOAD_IMAGE = 200;
	private int PHOTORESOULT = 300;
	private int image;
	private String photo_stream;
	private PopupWindow pop_image;
	private View view_pop;
	private Button bt_pic, bt_camera, bt_cancel;
	private String saveDir = Environment.getExternalStorageDirectory()
			.getPath() + "/temps_image";
	private DisplayImageOptions options;
	private String avatar, session_id, cn_username, en_username, email,
			avatars_id, company, wechat_qr_id, post_card_id, work_start,
			region_id, region_id_real, post_card, wechat_qr, wechat, add_v,
			phone_number, service_type, user_id, type, intro, company_id,
			office_building, office_building_id;
	private int year, month, day;
	private String url, url2, mrString;
	private String[] str = new String[] { "装修设计", "办公家具", "办公设备", "办公用品",
			"工商财税", "金融服务", "人力资源", "营销建站", "商务会展", "电信网络" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_setting_personal4);
		getDate();
		findView();
	}

	private void getDate() {
		ImageLoader.getInstance()
				.init(ImageLoaderConfiguration
						.createDefault(Setting_personal04.this));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.logo) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.logo) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.logo) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build();
		Intent intent = getIntent();
		cn_username = intent.getStringExtra("cn_username");
		en_username = intent.getStringExtra("en_username");
		email = intent.getStringExtra("email");
		avatar = intent.getStringExtra("avatar");
		avatars_id = intent.getStringExtra("avatar_id");
		company = intent.getStringExtra("company");
		wechat_qr_id = intent.getStringExtra("wechat_qr_id");
		post_card_id = intent.getStringExtra("post_card_id");
		work_start = intent.getStringExtra("work_start");
		region_id = intent.getStringExtra("region_id");
		region_id_real = intent.getStringExtra("region_id_real");
		post_card = intent.getStringExtra("post_card");
		wechat_qr = intent.getStringExtra("wechat_qr");
		service_type = intent.getStringExtra("service_type");
		phone_number = intent.getStringExtra("phone_number");
		wechat = intent.getStringExtra("wechat");
		add_v = intent.getStringExtra("v");
		type = intent.getStringExtra("type");
		user_id = intent.getStringExtra("user_id");
		intro = intent.getStringExtra("intro");
		company_id = intent.getStringExtra("company_id");
		office_building = intent.getStringExtra("office_building");
		office_building_id = intent.getStringExtra("office_building_id");
		sharedPreferences = Setting_personal04.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
	}

	private void findView() {
		// TODO Auto-generated method stub
		pDialog = new SweetAlertDialog(Setting_personal04.this,
				SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
		dialog = new Progress_Dialog(Setting_personal04.this, pDialog);
		tv_setting04_building=(TextView) findViewById(R.id.tv_setting04_building);
		tv_setting04_building.setText(office_building);
		lay_setting04_building = (LinearLayout) findViewById(R.id.lay_setting04_building);
		lay_setting04_building.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Setting_personal04.this,
						Add_AgentBuildingAct.class);
				intent.putExtra("cn_username", et_c_name.getText().toString());
				intent.putExtra("en_username", et_e_name.getText().toString());
				intent.putExtra("email", et_email.getText().toString());
				intent.putExtra("phone_number", phone_number);
				intent.putExtra("type", type);
				intent.putExtra("user_id", user_id);
				intent.putExtra("avatar", avatar);
				intent.putExtra("company", tv_setting04_company.getText()
						.toString());
				intent.putExtra("company_id", company_id);
				intent.putExtra("avatar_id", avatars_id);
				intent.putExtra("wechat_qr_id", wechat_qr_id);
				intent.putExtra("post_card_id", post_card_id);
				intent.putExtra("intro", tv_setting04_intro.getText()
						.toString());
				intent.putExtra("work_start", tv_worktime.getText().toString());
				intent.putExtra("region_id", "");
				intent.putExtra("service_type", service_type);
				intent.putExtra("region_id_real", region_id_real);
				intent.putExtra("wechat", et_wechat.getText().toString());
				intent.putExtra("wechat_qr", wechat_qr);
				intent.putExtra("v", add_v);
				intent.putExtra("post_card", post_card);
				intent.putExtra("office_building", office_building);
				intent.putExtra("office_building_id", office_building_id);
				intent.putExtra("act", "2");
				startActivity(intent);
				Setting_personal04.this.finish();
			}
		});
		tv_setting04_intro = (TextView) findViewById(R.id.tv_setting04_intro);
		tv_setting04_intro.setText(intro);
		lay_setting04_intro = (LinearLayout) findViewById(R.id.lay_setting04_intro);
		lay_setting04_intro.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Setting_personal04.this,
						Introduction_Act02.class);
				intent.putExtra("cn_username", et_c_name.getText().toString());
				intent.putExtra("en_username", et_e_name.getText().toString());
				intent.putExtra("email", et_email.getText().toString());
				intent.putExtra("phone_number", phone_number);
				intent.putExtra("type", type);
				intent.putExtra("user_id", user_id);
				intent.putExtra("avatar", avatar);
				intent.putExtra("company", tv_setting04_company.getText()
						.toString());
				intent.putExtra("company_id", company_id);
				intent.putExtra("avatar_id", avatars_id);
				intent.putExtra("wechat_qr_id", wechat_qr_id);
				intent.putExtra("post_card_id", post_card_id);
				intent.putExtra("intro", tv_setting04_intro.getText()
						.toString());
				intent.putExtra("work_start", tv_worktime.getText().toString());
				intent.putExtra("region_id", "");
				intent.putExtra("service_type", service_type);
				intent.putExtra("region_id_real", region_id_real);
				intent.putExtra("wechat", et_wechat.getText().toString());
				intent.putExtra("wechat_qr", wechat_qr);
				intent.putExtra("v", add_v);
				intent.putExtra("post_card", post_card);
				intent.putExtra("office_building", office_building);
				intent.putExtra("office_building_id", office_building_id);
				startActivity(intent);
				Setting_personal04.this.finish();
			}
		});
		lay_setting04_type_more = (LinearLayout) findViewById(R.id.lay_setting04_type_more);
		lay_setting04_type_renzheng = (LinearLayout) findViewById(R.id.lay_setting04_type_renzheng);
		lay_setting04_c_name_renzheng = (LinearLayout) findViewById(R.id.lay_setting04_c_name_renzheng);
		lay_setting04_company_renzheng = (LinearLayout) findViewById(R.id.lay_setting04_company_renzheng);
		lay_setting04_company_more=(LinearLayout) findViewById(R.id.lay_setting04_company_more);
		lay_setting_modify_04 = (LinearLayout) findViewById(R.id.lay_setting_modify_04);
		lay_setting_modify_04.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Setting_personal04.this,
						Identify_Act.class);
				intent.putExtra("cn_username", et_c_name.getText().toString());
				intent.putExtra("en_username", et_e_name.getText().toString());
				intent.putExtra("email", et_email.getText().toString());
				intent.putExtra("company", tv_setting04_company.getText()
						.toString());
				intent.putExtra("company_id", company_id);
				intent.putExtra("avatar", avatar);
				intent.putExtra("wechat_qr_id", wechat_qr_id);
				intent.putExtra("post_card_id", post_card_id);
				intent.putExtra("avatar_id", avatars_id);
				intent.putExtra("building_a", "");
				intent.putExtra("building_b", "");
				intent.putExtra("wechat", et_wechat.getText().toString());
				intent.putExtra("wechat_qr", wechat_qr);
				intent.putExtra("post_card", post_card);
				intent.putExtra("region_id", "");
				intent.putExtra("region_id_real", region_id_real);
				intent.putExtra("v", add_v);
				startActivity(intent);

			}
		});
		et_c_name = (EditText) findViewById(R.id.et_setting04_c_name);
		et_e_name = (EditText) findViewById(R.id.et_setting04_e_name);
		lay_setting04_company = (LinearLayout) findViewById(R.id.lay_setting04_company);
		lay_setting04_company.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!add_v.equals("1")) {
					Intent intent = new Intent(Setting_personal04.this,
							Select_CompanyAct.class);
					intent.putExtra("cn_username", et_c_name.getText()
							.toString());
					intent.putExtra("en_username", et_e_name.getText()
							.toString());
					intent.putExtra("email", et_email.getText().toString());
					intent.putExtra("phone_number", phone_number);
					intent.putExtra("type", type);
					intent.putExtra("user_id", user_id);
					intent.putExtra("avatar", avatar);
					intent.putExtra("company", tv_setting04_company.getText()
							.toString());
					intent.putExtra("company_id", company_id);
					intent.putExtra("avatar_id", avatars_id);
					intent.putExtra("wechat_qr_id", wechat_qr_id);
					intent.putExtra("post_card_id", post_card_id);
					intent.putExtra("intro", tv_setting04_intro.getText()
							.toString());
					intent.putExtra("work_start", tv_worktime.getText()
							.toString());
					intent.putExtra("region_id", "");
					intent.putExtra("service_type", service_type);
					intent.putExtra("region_id_real", region_id_real);
					intent.putExtra("wechat", et_wechat.getText().toString());
					intent.putExtra("wechat_qr", wechat_qr);
					intent.putExtra("v", add_v);
					intent.putExtra("post_card", post_card);
					intent.putExtra("act", "4");
					intent.putExtra("office_building", office_building);
					intent.putExtra("office_building_id", office_building_id);
					startActivity(intent);
					Setting_personal04.this.finish();
				}
			}
		});
		tv_setting04_company = (TextView) findViewById(R.id.tv_setting04_company);
		et_email = (EditText) findViewById(R.id.et_setting04_email);
		et_wechat = (EditText) findViewById(R.id.et_setting04_wechat);
		tv_setting04_phone = (TextView) findViewById(R.id.tv_setting04_phone);
		tv_setting04_phone.setText(phone_number);
		lay_services = (LinearLayout) findViewById(R.id.lay_setting04_services);
		lay_services.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.setOutsideTouchable(true);
				popwindow.showAtLocation(lay_setting04_back, Gravity.RIGHT
						| Gravity.BOTTOM, 0, 0);
				popwindow.showAsDropDown(view);
				popwindow.update();
				WindowManager.LayoutParams params = Setting_personal04.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				Setting_personal04.this.getWindow().setAttributes(params);
			}
		});
		bt_setting04_exit = (Button) findViewById(R.id.bt_setting04_exit);
		bt_setting04_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				User_info.setFlag(true);
				mrString = Setting_personal04.this.getExternalFilesDir(null)
						.getPath();
				url = "file://" + mrString + "/bulidersir/register_step1.html";
				url2 = "file://" + mrString + "/bulidersir/";
				Intent intent = new Intent(Setting_personal04.this,
						Exit_Act.class);
				intent.putExtra("url", url);
				intent.putExtra("url2", url2);
				startActivity(intent);
				Setting_personal04.this.finish();
			}
		});
		bt_setting04_comfirm = (Button) findViewById(R.id.bt_setting04_comfirm);
		bt_setting04_comfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!et_c_name.getText().toString().equals("")
						&& !et_email.getText().toString().equals("")
						&& !tv_setting04_company.getText().toString()
								.equals("")
						&& !tv_services.getText().toString().equals("")
						&& !tv_worktime.getText().toString().equals("")) {
					select_setting(session_id, user_id, type, et_c_name
							.getText().toString(), et_e_name.getText()
							.toString(), et_email.getText().toString(),
							service_type, tv_setting04_company.getText()
									.toString(), tv_setting04_intro.getText()
									.toString(), tv_worktime.getText()
									.toString(), et_wechat
									.getText().toString(), avatars_id,
							wechat_qr_id, post_card_id);
				} else {
					Toast.makeText(Setting_personal04.this, "资料未填全,请填写完后提交",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		view = LayoutInflater.from(Setting_personal04.this).inflate(
				R.layout.idenfity_addedservices, null);
		tv_services = (TextView) findViewById(R.id.tv_setting04_services);
		if (!service_type.equals("0")) {
			int flag = Integer.valueOf(service_type);
			tv_services.setText(str[flag - 1]);
		}
		lay_01 = (LinearLayout) view.findViewById(R.id.lay_services01);
		lay_01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_services.setText("装修设计");
				service_type = "1";
			}
		});
		lay_02 = (LinearLayout) view.findViewById(R.id.lay_services02);
		lay_02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_services.setText("办公家具");
				service_type = "2";
			}
		});
		lay_03 = (LinearLayout) view.findViewById(R.id.lay_services03);
		lay_03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_services.setText("办公设备");
				service_type = "3";
			}
		});
		lay_04 = (LinearLayout) view.findViewById(R.id.lay_services04);
		lay_04.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_services.setText("办公用品");
				service_type = "4";
			}
		});
		lay_05 = (LinearLayout) view.findViewById(R.id.lay_services05);
		lay_05.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_services.setText("工商财税");
				service_type = "5";
			}
		});
		lay_06 = (LinearLayout) view.findViewById(R.id.lay_services06);
		lay_06.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_services.setText("金融服务");
				service_type = "6";
			}
		});
		lay_07 = (LinearLayout) view.findViewById(R.id.lay_services07);
		lay_07.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_services.setText("人力资源");
				service_type = "7";
			}
		});
		lay_08 = (LinearLayout) view.findViewById(R.id.lay_services08);
		lay_08.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_services.setText("营销建站");
				service_type = "8";
			}
		});
		lay_09 = (LinearLayout) view.findViewById(R.id.lay_services09);
		lay_09.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_services.setText("商务会展");
				service_type = "9";
			}
		});
		lay_10 = (LinearLayout) view.findViewById(R.id.lay_services10);
		lay_10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_services.setText("电信网络");
				service_type = "10";
			}
		});

		// 设置popwindow
		WindowManager wm = this.getWindowManager();
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
		lay_wechat = (LinearLayout) findViewById(R.id.lay_setting04_wechat_pic);
		lay_wechat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 7;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_area, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = Setting_personal04.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				Setting_personal04.this.getWindow().setAttributes(params);
			}
		});
		lay_head = (LinearLayout) findViewById(R.id.lay_setting04_head);
		lay_head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 1;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_area, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = Setting_personal04.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				Setting_personal04.this.getWindow().setAttributes(params);
			}
		});
		lay_post = (LinearLayout) findViewById(R.id.lay_setting04_post);
		lay_post.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 6;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_area, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = Setting_personal04.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				Setting_personal04.this.getWindow().setAttributes(params);
			}
		});
		view_pop = LayoutInflater.from(Setting_personal04.this).inflate(
				R.layout.popwindow_image, null);
		bt_pic = (Button) view_pop.findViewById(R.id.bt_picture);
		bt_cancel = (Button) view_pop.findViewById(R.id.bt_cancel);
		bt_camera = (Button) view_pop.findViewById(R.id.bt_camera);

		pop_image = new PopupWindow(view_pop, LayoutParams.FILL_PARENT, 600,
				true);
		pop_image.setBackgroundDrawable(dw);
		pop_image.setBackgroundDrawable(new BitmapDrawable());
		pop_image.setFocusable(true);
		pop_image.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					clearWindow();
					pop_image.dismiss();
					return true;
				}
				return false;
			}
		});
		imageuri = Uri.fromFile(new File(saveDir + "temp3.png"));
		pop_image.setOnDismissListener(new poponDismissListener());
		bt_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop_image.dismiss();
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
		bt_camera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop_image.dismiss();
				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED)) {
					mPhotoFile = new File(saveDir, "temp4.jpg");
					mPhotoFile.delete();
					if (!mPhotoFile.exists()) {
						try {
							mPhotoFile.createNewFile();
						} catch (IOException e) {
							e.printStackTrace();
							Toast.makeText(getApplication(), "照片创建失败!",
									Toast.LENGTH_LONG).show();
							return;
						}
					}
					Intent intent = new Intent(
							"android.media.action.IMAGE_CAPTURE");
					intent.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(mPhotoFile));
					startActivityForResult(intent, CAMERA_RESULT);
				} else {
					Toast.makeText(getApplication(), "sdcard无效或没有插入!",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pop_image.dismiss();
			}
		});
		File savePath = new File(saveDir);
		if (!savePath.exists()) {
			savePath.mkdirs();
		}
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		tv_worktime = (TextView) findViewById(R.id.tv_setting04_worktime);
		tv_worktime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatePickerDialog datePicker = new DatePickerDialog(
						Setting_personal04.this, new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int years,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								tv_worktime.setText(year + "-" + (month + 1)
										+ "-" + day);
								year = years;
								month = monthOfYear;
								day = dayOfMonth;
							}
						}, year, month, day);
				datePicker.show();
			}
		});
		iv_head = (ImageView) findViewById(R.id.iv_setting04_head);
		ImageLoader.getInstance().displayImage(avatar, iv_head, options);
		iv_post = (ImageView) findViewById(R.id.iv_setting04_post);
		iv_wechat = (ImageView) findViewById(R.id.iv_setting04_wechat);
		lay_setting04_back = (LinearLayout) findViewById(R.id.lay_setting04_back);
		lay_setting04_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		tv_setting04_company.setText(company);
		if (add_v.equals("1")) {
			et_c_name.setText(cn_username);
			et_c_name.setFocusable(false);
			lay_setting04_type_more.setVisibility(View.GONE);
			lay_setting04_type_renzheng.setVisibility(View.VISIBLE);
			lay_setting04_c_name_renzheng.setVisibility(View.VISIBLE);
			lay_setting04_company_renzheng.setVisibility(View.VISIBLE);
			lay_setting04_company_more.setVisibility(View.GONE);
		} else {
			lay_setting04_type_more.setVisibility(View.VISIBLE);
			lay_setting04_type_renzheng.setVisibility(View.GONE);
			lay_setting04_c_name_renzheng.setVisibility(View.GONE);
			lay_setting04_company_renzheng.setVisibility(View.GONE);
			lay_setting04_company_more.setVisibility(View.VISIBLE);
			et_c_name.setText(cn_username);
		}
		et_c_name.setText(cn_username);
		et_e_name.setText(en_username);
		et_email.setText(email);
		if (!work_start.equals("")) {
			tv_worktime.setText(work_start);
		}
		if (!wechat.equals("")) {
			et_wechat.setText(wechat);
		}
		if (!wechat_qr.equals("")) {
			ImageLoader.getInstance().displayImage(wechat_qr, iv_wechat,
					options);
		}
		if (!post_card.equals("")) {
			ImageLoader.getInstance().displayImage(post_card, iv_post, options);
		}
	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					Setting_personal04.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	public void select_setting(String session_id, String user_id, String type,
			String cn_name, String en_name, String email, String service_type,
			String company, String intro, String time,
			String wx_num, String avatar_id, String wechat_qr_id,
			String post_card_id) {
		Request request = Request.requestsettingper04(session_id, user_id,
				type, cn_name, en_name, email, service_type, company, intro,
				time, wx_num, avatar_id, wechat_qr_id, post_card_id);
		new HttpThread(request, this);
	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				simulateKey(KeyEvent.KEYCODE_BACK);
				Toast.makeText(Setting_personal04.this, "修改成功", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(Setting_personal04.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 44:
				Toast.makeText(Setting_personal04.this, "图片上传失败", Toast.LENGTH_SHORT).show();
				if (image == 1) {
					iv_head.setImageResource(R.drawable.fangyuan_images_no_content);
				} else if (image == 6) {
					iv_post.setImageResource(R.drawable.fangyuan_images_no_content);
				} else {
					iv_wechat
							.setImageResource(R.drawable.fangyuan_images_no_content);
				}
				break;
			case 55:
				Toast.makeText(Setting_personal04.this, "图片上传成功", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(Setting_personal04.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT)
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
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					newHandler.sendEmptyMessage(1);
				} else if (errorid.equals("1")) {
					newHandler.sendEmptyMessage(2);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (reqID == Request.REQUEST_IMG_STREAM) {
			dialog.dismiss();
			JSONObject json;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					if (image == 1) {
						avatars_id = json.getString("id");
						avatar = json.getString("url");
					} else if (image == 7) {
						wechat_qr_id = json.getString("id");
						wechat_qr = json.getString("url");
					} else {
						post_card_id = json.getString("id");
						post_card = json.getString("url");
					}
					newHandler.sendEmptyMessage(55);
				} else {
					newHandler.sendEmptyMessage(44);
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

	public void clearWindow() {
		WindowManager.LayoutParams params = Setting_personal04.this.getWindow()
				.getAttributes();
		params.alpha = 1f;
		Setting_personal04.this.getWindow().setAttributes(params);
	}

	class poponDismissListener implements PopupWindow.OnDismissListener {

		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub
			clearWindow();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bitmap bitmap = null;
		if (requestCode == CAMERA_RESULT && resultCode == RESULT_OK) {
			if (mPhotoFile != null && mPhotoFile.exists()) {
				bitmap = decodeUriAsBitmap(Uri.fromFile(mPhotoFile));
				if (image == 1) {
					startPhotoZoom(Uri.fromFile(mPhotoFile), PHOTORESOULT);
				} else if (image == 7) {
					iv_wechat.setImageBitmap(bitmap);
					upload_image(bitmap);
				} else {
					iv_post.setImageBitmap(bitmap);
					upload_image(bitmap);
				}
			}
		}
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			bitmap = decodeUriAsBitmap(data.getData());
			if (image == 1) {
				startPhotoZoom(data.getData(), PHOTORESOULT);
			} else if (image == 7) {
				iv_wechat.setImageBitmap(bitmap);
				upload_image(bitmap);
			} else {
				iv_post.setImageBitmap(bitmap);
				upload_image(bitmap);
			}
		}
		if (requestCode == PHOTORESOULT && resultCode == RESULT_OK) {

			if (imageuri != null) {
				Bitmap bitmaps = decodeUriAsBitmap(imageuri);
				dialog.dialog();
				iv_head.setImageBitmap(bitmaps);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				out = yasuo(bitmaps);
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				photo_stream = new String(Base64.encode(out.toByteArray(),
						Base64.DEFAULT));
				Request request = Request.requestImg(photo_stream, "1", "1",
						"2", "2");
				new HttpThread(request, Setting_personal04.this);
			}
		}
	}

	public void upload_image(Bitmap bitmap) {
		dialog.dialog();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		out = yasuo(bitmap);
		try {
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		photo_stream = new String(Base64.encode(out.toByteArray(),
				Base64.DEFAULT));
		Request request = Request.requestImg(photo_stream, "" + image, "1",
				"2", "2");
		new HttpThread(request, Setting_personal04.this);
	}

	public ByteArrayOutputStream yasuo(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 90;
		bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		while (baos.toByteArray().length / 1024 > 500) {
			baos.reset();
			options -= 10;
			bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		return baos;
	}

	public void startPhotoZoom(Uri uri, int i) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX, outputY 是裁剪图片宽高
		intent.putExtra("outputX", 400);
		intent.putExtra("outputY", 400);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, i);
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
}
