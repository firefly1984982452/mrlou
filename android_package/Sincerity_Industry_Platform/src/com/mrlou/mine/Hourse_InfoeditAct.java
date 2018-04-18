package com.mrlou.mine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Instrumentation;
import android.content.Intent;
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
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrlou.economic.view.BadgeView;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.ttf.FontManager;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class Hourse_InfoeditAct extends Activity implements HttpListener {

	private LinearLayout lay_hourseinfo_phone, lay_hourse_infoedit_back,
			lay_icon01, lay_icon02, lay_icon03, lay_icon04, lay_deal_date,
			lay_hourse_infoedit_status, lay_hourse_image,
			lay_hourseinfo_image_yezhu, lay_hourse_infoedit02,
			lay_hourse_infoedit;
	private LinearLayout lay_second, lay_third, lay_hourseinfo_yezhu,
			lay_hourseinfo_yezhu02, lay_hourseinfo_yezhu03;
	private ImageView iv_icon01, iv_icon02, iv_icon03, iv_icon04;
	private ImageView iv_other, iv_yezhu, iv_yezhu02, iv_yezhu03, iv_hourse,
			iv_other2, iv_other3, iv_other4, iv_other5, iv_other6, iv_other7,
			iv_other8, iv_other9;
	private TextView tv_icon03, tv_hourse_infoedit_status;
	private EditText et_icon04;
	private View view_pop;
	private Bitmap photo;
	private Button bt_pic, bt_camera, bt_cancel;
	private PopupWindow pop_image;
	private int CAMERA_RESULT = 100;
	private int RESULT_LOAD_IMAGE = 200;
	private int year, month, day, image;
	private String status;
	private String saveDir = Environment.getExternalStorageDirectory()
			.getPath() + "/temp_image";
	private File mPhotoFile;
	private Uri imageuri;
	private boolean is_flag03 = true, is_flag04 = true;
	private WindowManager.LayoutParams params;
	private String id, category, building_name, avatar, avatar_id,
			transaction_type, unit_no, session_id, rent_price, sell_price,
			head_to, delivery_date, delivery_status, intro, square_meter,
			position, min_rent_period, contact_phone, hourse_id,
			images_id = "", avatars, sell_price_unit = "",
			rent_price_unit = "", room_price_unit;
	private TextView tv_hourse_infoeidt_name, tv_hourse_infoeidt_area,
			tv_hourse_infoeidt_identify, tv_hourse_infoeidt_unit,
			tv_hourse_infoeidt_unit02;
	private EditText et_hourse_infoeidt_location, et_hourse_infoeidt_area,
			et_hourse_infoeidt_price, et_hourse_infoeidt_direction,
			et_hourse_infoeidt_phone, et_hourse_infoeidt_intro;
	private Button bt_hourse_infoedit;
	private String photo_stream, avatar_ids, land, land_id, user_type,
			fee_rate;
	private ArrayList<String> str_images_url, str_images_id,str_land_id,str_land;
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private SweetAlertDialog pDialog;
	private Progress_Dialog dialog;
	private BadgeView badgeView, badgeView2, badgeView3, badgeView4,
			badgeView5, badgeView6, badgeView7, badgeView8, badgeView9,
			badgeView10, badgeView11,badgeView12,badgeView13;
	private LinearLayout lay_hourseinfo_other1, lay_hourseinfo_other2,
			lay_hourseinfo_other3, lay_hourseinfo_other4,
			lay_hourseinfo_other5, lay_hourseinfo_other6,
			lay_hourseinfo_other7, lay_hourseinfo_other8,
			lay_hourseinfo_other9;
	private ImageView iv_hourse_infoedit, iv_hourse_infoedit02;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_hourse_infoedit);
		getDate();
		ImageLoader.getInstance()
				.init(ImageLoaderConfiguration
						.createDefault(Hourse_InfoeditAct.this));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.logo) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.logo) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.logo) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
				.build();
		findView();
	}

	private void getDate() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		session_id = intent.getStringExtra("session_id");
		building_name = intent.getStringExtra("building_name");
		id = intent.getStringExtra("id");
		category = intent.getStringExtra("category");
		transaction_type = intent.getStringExtra("transaction_type");
		position = intent.getStringExtra("position");
		square_meter = intent.getStringExtra("square_meter");
		rent_price = intent.getStringExtra("rent_price");
		sell_price = intent.getStringExtra("sell_price");
		rent_price_unit = intent.getStringExtra("rent_price_unit");
		sell_price_unit = intent.getStringExtra("sell_price_unit");
		// System.out.println("----"+rent_price_unit+sell_price_unit);
		head_to = intent.getStringExtra("head_to");
		fee_rate = intent.getStringExtra("fee_rate");
		delivery_status = intent.getStringExtra("delivery_status");
		delivery_date = intent.getStringExtra("delivery_date");
		intro = intent.getStringExtra("intro");
		contact_phone = intent.getStringExtra("contact_phone");
		unit_no = intent.getStringExtra("unit_no");
		status = intent.getStringExtra("status");
		hourse_id = intent.getStringExtra("hourse_id");
		str_images_url = intent.getStringArrayListExtra("images_url");
		str_images_id = intent.getStringArrayListExtra("images_id");
		min_rent_period = intent.getStringExtra("min_rent_period");
		str_land=intent.getStringArrayListExtra("land");
		str_land_id=intent.getStringArrayListExtra("land_id");
		
//		land = intent.getStringExtra("land");
//		land_id = intent.getStringExtra("land_id");
		
		avatar = intent.getStringExtra("avatar");
		avatar_id = intent.getStringExtra("avatar_id");
		user_type = intent.getStringExtra("user_type");
		// room_price_unit=sell_price_unit+rent_price_unit;
	}

	private void findView() {
		// TODO Auto-generated method stub
		// 弹出请求对话框
		pDialog = new SweetAlertDialog(Hourse_InfoeditAct.this,
				SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
		dialog = new Progress_Dialog(Hourse_InfoeditAct.this, pDialog);
		bt_hourse_infoedit = (Button) findViewById(R.id.bt_hourse_infoedit);
		bt_hourse_infoedit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = str_images_id.size() - 1; i >= 0; i--) {
					if (str_images_id.get(i).equals("1")) {
						str_images_id.remove(i);
					}
				}
				if (str_images_id.size() == 1) {
					images_id = str_images_id.get(0);
				} else if (str_images_id.size() > 1) {
					for (int i = 0; i < str_images_id.size(); i++) {
						if (i == 0) {
							images_id = str_images_id.get(0);
						} else {
							images_id = images_id + "," + str_images_id.get(i);
						}
					}
				} else {
					images_id = "";
				}
				
				for (int i = str_land_id.size() - 1; i >= 0; i--) {
					if (str_land_id.get(i).equals("1")) {
						str_land_id.remove(i);
					}
				}
				if (str_land_id.size() == 1) {
					land_id = str_land_id.get(0);
				} else if (str_land_id.size() > 1) {
					for (int i = 0; i < str_land_id.size(); i++) {
						if (i == 0) {
							land_id = str_land_id.get(0);
						} else {
							land_id = land_id + "," + str_land_id.get(i);
						}
					}
				} else {
					land_id = "";
				}
				
				dialog.dialog();
				select_customeradd(session_id, id, category,
						et_hourse_infoeidt_location.getText().toString(),
						et_hourse_infoeidt_area.getText().toString(),
						transaction_type, et_hourse_infoeidt_price.getText()
								.toString(), position,
						et_hourse_infoeidt_direction.getText().toString(),
						delivery_status, tv_icon03.getText().toString(),
						et_icon04.getText().toString(), min_rent_period,
						et_hourse_infoeidt_phone.getText().toString(), "",
						et_hourse_infoeidt_intro.getText().toString(), "1",
						status, avatar_id, images_id, land_id);
			}
		});
		tv_hourse_infoeidt_name = (TextView) findViewById(R.id.tv_hourse_infoeidt_name);
		tv_hourse_infoeidt_name.setText(building_name);
		tv_hourse_infoeidt_area = (TextView) findViewById(R.id.tv_hourse_infoeidt_area);
		tv_hourse_infoeidt_area.setText(unit_no + " " + square_meter + "㎡");
		tv_hourse_infoeidt_identify = (TextView) findViewById(R.id.tv_hourse_infoeidt_identify);
		lay_hourseinfo_yezhu = (LinearLayout) findViewById(R.id.lay_hourseinfo_yezhu);
		lay_hourseinfo_yezhu.setVisibility(View.GONE);
		if (user_type.equals("299")) {
			tv_hourse_infoeidt_identify.setText("其他");
		} else if (user_type.equals("1")) {
			tv_hourse_infoeidt_identify.setText("开发商");
		} else if (user_type.equals("2")) {
			tv_hourse_infoeidt_identify.setText("代理商");
		} else if (user_type.equals("3")) {
			tv_hourse_infoeidt_identify.setText("运营商");
		} else if (user_type.equals("102")) {
			tv_hourse_infoeidt_identify.setText("经纪人");
		} else if (user_type.equals("103")) {
			tv_hourse_infoeidt_identify.setText("物业公司");
		} else if (user_type.equals("201")) {
			tv_hourse_infoeidt_identify.setText("小业主");
			lay_hourseinfo_yezhu.setVisibility(View.VISIBLE);
		} else if (user_type.equals("202")) {
			tv_hourse_infoeidt_identify.setText("代理人");
		} else {
			tv_hourse_infoeidt_identify.setText("转租人");
		}
		et_hourse_infoeidt_location = (EditText) findViewById(R.id.et_hourse_infoeidt_location);
		et_hourse_infoeidt_location.setText(unit_no);
		et_hourse_infoeidt_area = (EditText) findViewById(R.id.et_hourse_infoeidt_area);
		et_hourse_infoeidt_area.setText(square_meter);
		et_hourse_infoeidt_price = (EditText) findViewById(R.id.et_hourse_infoeidt_price);
		tv_hourse_infoeidt_unit = (TextView) findViewById(R.id.tv_hourse_infoeidt_unit);
		tv_hourse_infoeidt_unit02 = (TextView) findViewById(R.id.tv_hourse_infoeidt_unit02);
		iv_hourse_infoedit = (ImageView) findViewById(R.id.iv_hourse_infoedit);
		iv_hourse_infoedit02 = (ImageView) findViewById(R.id.iv_hourse_infoedit02);

		et_hourse_infoeidt_direction = (EditText) findViewById(R.id.et_hourse_infoeidt_direction);
		et_hourse_infoeidt_direction.setText(head_to);
		et_hourse_infoeidt_phone = (EditText) findViewById(R.id.et_hourse_infoeidt_phone);
		et_hourse_infoeidt_phone.setText(contact_phone);
		et_hourse_infoeidt_intro = (EditText) findViewById(R.id.et_hourse_infoeidt_intro);
		et_hourse_infoeidt_intro.setText(intro);
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		tv_hourse_infoedit_status = (TextView) findViewById(R.id.tv_infoedit_status);
		if (delivery_status.equals("1")) {
			tv_hourse_infoedit_status.setText("毛坯交付");
		} else if (delivery_status.equals("2")) {
			tv_hourse_infoedit_status.setText("标准交付");
		} else if (delivery_status.equals("3")) {
			tv_hourse_infoedit_status.setText("现状装修隔断");
		} else if (delivery_status.equals("4")) {
			tv_hourse_infoedit_status.setText("现状装修家具");
		} else if (delivery_status.equals("5")) {
			tv_hourse_infoedit_status.setText("拎包办公");
		} else {
			tv_hourse_infoedit_status.setText("协商交付条件");
		}
		lay_hourse_infoedit_status = (LinearLayout) findViewById(R.id.lay_infoedit_status);
		lay_hourse_infoedit_status.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Hourse_InfoeditAct.this,
						Choose_StutaAct.class);
				intent.putExtra("session_id", session_id);
				intent.putExtra("building_name", building_name);
				intent.putExtra("category", category);
				intent.putExtra("id", id);
				intent.putExtra("transaction_type", transaction_type);
				intent.putExtra("position", position);
				intent.putExtra("square_meter", et_hourse_infoeidt_area
						.getText().toString());
				intent.putExtra("rent_price", et_hourse_infoeidt_price
						.getText().toString());
				intent.putExtra("sell_price", et_hourse_infoeidt_price
						.getText().toString());
				intent.putExtra("rent_price_unit", rent_price_unit);
				intent.putExtra("sell_price_unit", sell_price_unit);
				intent.putExtra("head_to", et_hourse_infoeidt_direction
						.getText().toString());
				intent.putExtra("fee_rate", et_icon04.getText().toString());
				intent.putExtra("delivery_status", delivery_status);
				intent.putExtra("delivery_date", tv_icon03.getText().toString());
				intent.putExtra("intro", et_hourse_infoeidt_intro.getText()
						.toString());
				intent.putExtra("contact_phone", et_hourse_infoeidt_phone
						.getText().toString());
				intent.putExtra("unit_no", et_hourse_infoeidt_location
						.getText().toString());
				intent.putExtra("position", position);
				intent.putExtra("status", status);
				intent.putExtra("hourse_id", hourse_id);
				intent.putExtra("min_rent_period", min_rent_period);
//				intent.putExtra("land", land);
//				intent.putExtra("land_id", land_id);
				intent.putExtra("avatar", avatar);
				intent.putExtra("avatar_id", avatar_id);
				intent.putStringArrayListExtra("images_url", str_images_url);
				intent.putStringArrayListExtra("images_id", str_images_id);
				intent.putStringArrayListExtra("land", str_land);
				intent.putStringArrayListExtra("land_id", str_land_id);
				intent.putExtra("user_type", user_type);
				startActivity(intent);
				Hourse_InfoeditAct.this.finish();
			}
		});

		lay_deal_date = (LinearLayout) findViewById(R.id.lay_deal_date);
		lay_deal_date.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (tv_icon03.getText().equals("现房")) {

				} else {
					DatePickerDialog datePicker = new DatePickerDialog(
							Hourse_InfoeditAct.this, new OnDateSetListener() {

								@Override
								public void onDateSet(DatePicker view,
										int years, int monthOfYear,
										int dayOfMonth) {
									// TODO Auto-generated method stub
									tv_icon03.setText(year + "-"
											+ (monthOfYear + 1) + "-"
											+ dayOfMonth);
									year = years;
									month = monthOfYear;
									day = dayOfMonth;
								}
							}, year, month, day);
					datePicker.show();
				}
			}
		});
		lay_icon01 = (LinearLayout) findViewById(R.id.lay_hourse_infoedit_icon01);
		lay_icon01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				transaction_type = "1";
				et_hourse_infoeidt_price.setText(rent_price);
				tv_hourse_infoeidt_unit.setText("元/㎡/天");
				tv_hourse_infoeidt_unit02.setText("元/月");
				selecticon(1);
			}
		});
		lay_icon02 = (LinearLayout) findViewById(R.id.lay_hourse_infoedit_icon02);
		lay_icon02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!category.equals("4")&&!category.equals("5")) {
					transaction_type = "2";
					et_hourse_infoeidt_price.setText(sell_price);
					tv_hourse_infoeidt_unit.setText("元/㎡");
					tv_hourse_infoeidt_unit02.setText("万元");
					selecticon(2);
				}else {
					Toast.makeText(Hourse_InfoeditAct.this, "商务中心和创客空间目前只支持出租", 1000).show();
				}
			}
		});
		lay_hourse_infoedit = (LinearLayout) findViewById(R.id.lay_hourse_infoedit);
		lay_hourse_infoedit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selecticon02(1);
				if (transaction_type.equals("1")) {
					room_price_unit = "元/㎡/天";
				} else {
					room_price_unit = "元/㎡";
				}
			}
		});
		lay_hourse_infoedit02 = (LinearLayout) findViewById(R.id.lay_hourse_infoedit02);
		lay_hourse_infoedit02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selecticon02(2);
				if (transaction_type.equals("1")) {
					room_price_unit = "元/月";
				} else {
					room_price_unit = "万元";
				}
			}
		});
		iv_icon01 = (ImageView) findViewById(R.id.iv_hourse_infoedit_icon01);
		iv_icon02 = (ImageView) findViewById(R.id.iv_hourse_infoedit_icon02);
		if (transaction_type.equals("1")) {
			selecticon(1);
			et_hourse_infoeidt_price.setText(rent_price);
			tv_hourse_infoeidt_unit.setText("元/㎡/天");
			tv_hourse_infoeidt_unit02.setText("元/月");
			if (rent_price_unit.equals("元/㎡/天")) {
				selecticon02(1);
				rent_price_unit = "元/㎡/天";
				room_price_unit = rent_price_unit;
			} else {
				selecticon02(2);
				rent_price_unit = "元/月";
				room_price_unit = rent_price_unit;
			}

		} else {
			selecticon(2);
			et_hourse_infoeidt_price.setText(sell_price);
			tv_hourse_infoeidt_unit.setText("元/㎡");
			tv_hourse_infoeidt_unit02.setText("万元");
			if (rent_price_unit.equals("元/m²")) {
				selecticon02(1);
				sell_price_unit = "元/m²";
				room_price_unit = sell_price_unit;
			} else {
				selecticon02(2);
				sell_price_unit = "万元";
				room_price_unit = sell_price_unit;
			}
		}
		lay_icon03 = (LinearLayout) findViewById(R.id.lay_hourse_infoedit_icon03);
		tv_icon03 = (TextView) findViewById(R.id.tv_hourse_infoedit_icon03);
		tv_icon03.setText(delivery_date);
		lay_icon03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (is_flag03) {
					iv_icon03.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.resourse_images_checked));
					is_flag03 = false;
					tv_icon03.setText("现房");
				} else {
					iv_icon03.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.resourse_images_uncheck));
					is_flag03 = true;
					tv_icon03.setText("");
				}
			}
		});
		lay_icon04 = (LinearLayout) findViewById(R.id.lay_hourse_infoedit_icon04);
		et_icon04 = (EditText) findViewById(R.id.et_hourse_infoedit_icon04);
		et_icon04.setText(fee_rate);
		lay_icon04.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (is_flag04) {
					iv_icon04.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.resourse_images_checked));
					is_flag04 = false;
					et_icon04.setText("不合作");
					et_icon04.setEnabled(false);
				} else {
					iv_icon04.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.resourse_images_uncheck));
					is_flag04 = true;
					et_icon04.setText("");
					et_icon04.setEnabled(true);
				}
			}
		});
		iv_icon03 = (ImageView) findViewById(R.id.iv_hourse_infoeidt_icon03);
		iv_icon04 = (ImageView) findViewById(R.id.iv_hourse_infoeidt_icon04);
		lay_hourse_infoedit_back = (LinearLayout) findViewById(R.id.lay_hourse_infoedit_back);
		lay_hourse_infoedit_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Hourse_InfoeditAct.this,
						Hourse_FollowAct.class);
				intent.putExtra("id", hourse_id);
				startActivity(intent);
				finish();
				// simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		imageuri = Uri.fromFile(new File(saveDir + "temp2.png"));
		lay_hourseinfo_phone = (LinearLayout) findViewById(R.id.lay_hourseinfo_phone);
		lay_second = (LinearLayout) findViewById(R.id.lay_hourseinfo_second);
		lay_third = (LinearLayout) findViewById(R.id.lay_hourseinfo_third);

		iv_other = (ImageView) findViewById(R.id.iv_hourseinfo_other1);
		iv_other.setOnClickListener(new Bt_imageview());
		iv_other2 = (ImageView) findViewById(R.id.iv_hourseinfo_other2);
		iv_other2.setOnClickListener(new Bt_imageview());
		iv_other3 = (ImageView) findViewById(R.id.iv_hourseinfo_other3);
		iv_other3.setOnClickListener(new Bt_imageview());
		iv_other4 = (ImageView) findViewById(R.id.iv_hourseinfo_other4);
		iv_other4.setOnClickListener(new Bt_imageview());
		iv_other5 = (ImageView) findViewById(R.id.iv_hourseinfo_other5);
		iv_other5.setOnClickListener(new Bt_imageview());
		iv_other6 = (ImageView) findViewById(R.id.iv_hourseinfo_other6);
		iv_other6.setOnClickListener(new Bt_imageview());
		iv_other7 = (ImageView) findViewById(R.id.iv_hourseinfo_other7);
		iv_other7.setOnClickListener(new Bt_imageview());
		iv_other8 = (ImageView) findViewById(R.id.iv_hourseinfo_other8);
		iv_other8.setOnClickListener(new Bt_imageview());
		iv_other9 = (ImageView) findViewById(R.id.iv_hourseinfo_other9);
		iv_other9.setOnClickListener(new Bt_imageview());

		iv_hourse = (ImageView) findViewById(R.id.iv_hourseinfo_hourse);
		iv_hourse.setOnClickListener(new Bt_imageview());
		if (!avatar.equals("")) {
			iv_hourse.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(avatar, iv_hourse, options);
			// imageLoader.loadImage(avatar, 100, 100, iv_hourse);
		}
		iv_yezhu = (ImageView) findViewById(R.id.iv_hourseinfo_yezhu);
		iv_yezhu02 = (ImageView) findViewById(R.id.iv_hourseinfo_yezhu02);
		iv_yezhu03 = (ImageView) findViewById(R.id.iv_hourseinfo_yezhu03);
		iv_yezhu.setOnClickListener(new Bt_imageview());
		iv_yezhu02.setOnClickListener(new Bt_imageview());
		iv_yezhu03.setOnClickListener(new Bt_imageview());

//		if (!land.equals("")) {
//			iv_yezhu.setImageResource(R.drawable.fangyuan_images_no_content);
//			ImageLoader.getInstance().displayImage(land, iv_yezhu, options);
//		}
		
		lay_hourse_image = (LinearLayout) findViewById(R.id.lay_hourse_image);
		badgeView = new BadgeView(Hourse_InfoeditAct.this, lay_hourse_image);
		badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView.setBackgroundResource(R.drawable.fangyuan_images_del_img);
		if (avatar != null) {
			badgeView.show();
		}
		badgeView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_hourse
						.setImageResource(R.drawable.fangyuan_images_no_content);
				avatar_id = "";
				badgeView.hide();
			}
		});
		lay_hourseinfo_image_yezhu = (LinearLayout) findViewById(R.id.lay_hourseinfo_image_yezhu);
		badgeView2 = new BadgeView(Hourse_InfoeditAct.this,
				lay_hourseinfo_image_yezhu);
		badgeView2.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView2.setBackgroundResource(R.drawable.fangyuan_images_del_img);
		badgeView2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_yezhu.setImageResource(R.drawable.fangyuan_images_no_content);
				str_land_id.set(0, "1");
				badgeView2.hide();
			}
		});
		
		lay_hourseinfo_yezhu02 = (LinearLayout) findViewById(R.id.lay_hourseinfo_image_yezhu02);
		badgeView12 = new BadgeView(Hourse_InfoeditAct.this,
				lay_hourseinfo_yezhu02);
		badgeView12.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView12.setBackgroundResource(R.drawable.fangyuan_images_del_img);
		badgeView12.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_yezhu02.setImageResource(R.drawable.fangyuan_images_no_content);
				str_land_id.set(1, "1");
				badgeView12.hide();
			}
		});
		
		lay_hourseinfo_yezhu03 = (LinearLayout) findViewById(R.id.lay_hourseinfo_image_yezhu03);
		badgeView13 = new BadgeView(Hourse_InfoeditAct.this,
				lay_hourseinfo_yezhu03);
		badgeView13.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView13.setBackgroundResource(R.drawable.fangyuan_images_del_img);
		badgeView13.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_yezhu03.setImageResource(R.drawable.fangyuan_images_no_content);
				str_land_id.set(2, "1");
				badgeView13.hide();
			}
		});
		lay_hourseinfo_other1 = (LinearLayout) findViewById(R.id.lay_hourseinfo_other1);
		badgeView3 = new BadgeView(Hourse_InfoeditAct.this,
				lay_hourseinfo_other1);
		badgeView3.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView3.setBackgroundResource(R.drawable.fangyuan_images_del_img);

		badgeView3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_other.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images_id.set(0, "1");
				badgeView3.hide();
			}
		});

		lay_hourseinfo_other2 = (LinearLayout) findViewById(R.id.lay_hourseinfo_other2);
		badgeView4 = new BadgeView(Hourse_InfoeditAct.this,
				lay_hourseinfo_other2);
		badgeView4.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView4.setBackgroundResource(R.drawable.fangyuan_images_del_img);

		badgeView4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_other2
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images_id.set(1, "1");
				badgeView4.hide();
			}
		});

		lay_hourseinfo_other3 = (LinearLayout) findViewById(R.id.lay_hourseinfo_other3);
		badgeView5 = new BadgeView(Hourse_InfoeditAct.this,
				lay_hourseinfo_other3);
		badgeView5.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView5.setBackgroundResource(R.drawable.fangyuan_images_del_img);

		badgeView5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_other3
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images_id.set(2, "1");
				badgeView5.hide();
			}
		});

		lay_hourseinfo_other4 = (LinearLayout) findViewById(R.id.lay_hourseinfo_other4);
		badgeView6 = new BadgeView(Hourse_InfoeditAct.this,
				lay_hourseinfo_other4);
		badgeView6.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView6.setBackgroundResource(R.drawable.fangyuan_images_del_img);

		badgeView6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_other4
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images_id.set(3, "1");
				badgeView6.hide();
			}
		});

		lay_hourseinfo_other5 = (LinearLayout) findViewById(R.id.lay_hourseinfo_other5);
		badgeView7 = new BadgeView(Hourse_InfoeditAct.this,
				lay_hourseinfo_other5);
		badgeView7.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView7.setBackgroundResource(R.drawable.fangyuan_images_del_img);

		badgeView7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_other5
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images_id.set(4, "1");
				badgeView7.hide();
			}
		});

		lay_hourseinfo_other6 = (LinearLayout) findViewById(R.id.lay_hourseinfo_other6);
		badgeView8 = new BadgeView(Hourse_InfoeditAct.this,
				lay_hourseinfo_other6);
		badgeView8.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView8.setBackgroundResource(R.drawable.fangyuan_images_del_img);

		badgeView8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_other6
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images_id.set(5, "1");
				badgeView8.hide();
			}
		});

		lay_hourseinfo_other7 = (LinearLayout) findViewById(R.id.lay_hourseinfo_other7);
		badgeView9 = new BadgeView(Hourse_InfoeditAct.this,
				lay_hourseinfo_other7);
		badgeView9.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView9.setBackgroundResource(R.drawable.fangyuan_images_del_img);

		badgeView9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_other7
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images_id.set(6, "1");
				badgeView9.hide();
			}
		});

		lay_hourseinfo_other8 = (LinearLayout) findViewById(R.id.lay_hourseinfo_other8);
		badgeView10 = new BadgeView(Hourse_InfoeditAct.this,
				lay_hourseinfo_other8);
		badgeView10.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView10.setBackgroundResource(R.drawable.fangyuan_images_del_img);

		badgeView10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_other8
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images_id.set(7, "1");
				badgeView10.hide();
			}
		});

		lay_hourseinfo_other9 = (LinearLayout) findViewById(R.id.lay_hourseinfo_other9);
		badgeView11 = new BadgeView(Hourse_InfoeditAct.this,
				lay_hourseinfo_other9);
		badgeView11.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView11.setBackgroundResource(R.drawable.fangyuan_images_del_img);

		badgeView11.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_other9
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images_id.set(8, "1");
				badgeView11.hide();
			}
		});
		if (str_images_id.size() > 0) {
			select_pic(str_images_url.size());
		}
		if (str_land_id.size()>0) {
			select_land(str_land.size());
		}
		params = Hourse_InfoeditAct.this.getWindow().getAttributes();
		view_pop = LayoutInflater.from(Hourse_InfoeditAct.this).inflate(
				R.layout.popwindow_image, null);
		bt_pic = (Button) view_pop.findViewById(R.id.bt_picture);
		bt_cancel = (Button) view_pop.findViewById(R.id.bt_cancel);
		bt_camera = (Button) view_pop.findViewById(R.id.bt_camera);

		pop_image = new PopupWindow(view_pop, LayoutParams.FILL_PARENT, 600,
				true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
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
				destoryImage();
				String state = Environment.getExternalStorageState();
				if (state.equals(Environment.MEDIA_MOUNTED)) {
					mPhotoFile = new File(saveDir, "temp.jpg");
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
	}
	
	private void select_land(int image){
		switch (image) {
		case 1:
			iv_yezhu.setVisibility(View.VISIBLE);
			badgeView2.show();
			iv_yezhu.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_land.get(0),
					iv_yezhu, options);
			break;
		case 2:
			iv_yezhu.setVisibility(View.VISIBLE);
			iv_yezhu02.setVisibility(View.VISIBLE);
			badgeView2.show();
			badgeView12.show();
			iv_yezhu.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_land.get(0),
					iv_yezhu, options);
			iv_yezhu02.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_land.get(1),
					iv_yezhu02, options);
			break;
		case 3:
			iv_yezhu.setVisibility(View.VISIBLE);
			iv_yezhu02.setVisibility(View.VISIBLE);
			iv_yezhu03.setVisibility(View.VISIBLE);
			badgeView2.show();
			badgeView12.show();
			badgeView13.show();
			iv_yezhu.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_land.get(0),
					iv_yezhu, options);
			iv_yezhu02.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_land.get(1),
					iv_yezhu02, options);
			iv_yezhu03.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_land.get(2),
					iv_yezhu03, options);
			break;
		}
	}
	
	private void select_pic(int image) {
		switch (image) {
		case 1:
			iv_other2.setVisibility(View.VISIBLE);
			badgeView3.show();
			iv_other.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(0),
					iv_other, options);
			break;
		case 2:
			iv_other2.setVisibility(View.VISIBLE);
			iv_other3.setVisibility(View.VISIBLE);
			badgeView3.show();
			badgeView4.show();
			iv_other.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(0),
					iv_other, options);
			iv_other2.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(1),
					iv_other2, options);
			break;
		case 3:
			iv_other2.setVisibility(View.VISIBLE);
			iv_other3.setVisibility(View.VISIBLE);
			iv_other4.setVisibility(View.VISIBLE);
			badgeView3.show();
			badgeView4.show();
			badgeView5.show();
			lay_second.setVisibility(View.VISIBLE);
			iv_other.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(0),
					iv_other, options);
			iv_other2.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(1),
					iv_other2, options);
			iv_other3.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(2),
					iv_other3, options);
			break;
		case 4:
			iv_other2.setVisibility(View.VISIBLE);
			iv_other3.setVisibility(View.VISIBLE);
			iv_other4.setVisibility(View.VISIBLE);
			iv_other5.setVisibility(View.VISIBLE);
			badgeView3.show();
			badgeView4.show();
			badgeView5.show();
			badgeView6.show();
			lay_second.setVisibility(View.VISIBLE);
			iv_other.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(0),
					iv_other, options);
			iv_other2.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(1),
					iv_other2, options);
			iv_other3.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(2),
					iv_other3, options);
			iv_other4.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(3),
					iv_other4, options);
			break;
		case 5:
			iv_other2.setVisibility(View.VISIBLE);
			iv_other3.setVisibility(View.VISIBLE);
			iv_other4.setVisibility(View.VISIBLE);
			iv_other5.setVisibility(View.VISIBLE);
			iv_other6.setVisibility(View.VISIBLE);
			badgeView3.show();
			badgeView4.show();
			badgeView5.show();
			badgeView6.show();
			badgeView7.show();
			lay_second.setVisibility(View.VISIBLE);
			iv_other.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(0),
					iv_other, options);
			iv_other2.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(1),
					iv_other2, options);
			iv_other3.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(2),
					iv_other3, options);
			iv_other4.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(3),
					iv_other4, options);
			iv_other5.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(4),
					iv_other5, options);
			break;
		case 6:
			iv_other2.setVisibility(View.VISIBLE);
			iv_other3.setVisibility(View.VISIBLE);
			iv_other4.setVisibility(View.VISIBLE);
			iv_other5.setVisibility(View.VISIBLE);
			iv_other6.setVisibility(View.VISIBLE);
			iv_other7.setVisibility(View.VISIBLE);
			badgeView3.show();
			badgeView4.show();
			badgeView5.show();
			badgeView6.show();
			badgeView7.show();
			badgeView8.show();
			lay_second.setVisibility(View.VISIBLE);
			lay_third.setVisibility(View.VISIBLE);
			iv_other.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(0),
					iv_other, options);
			iv_other2.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(1),
					iv_other2, options);
			iv_other3.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(2),
					iv_other3, options);
			iv_other4.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(3),
					iv_other4, options);
			iv_other5.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(4),
					iv_other5, options);
			iv_other6.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(5),
					iv_other6, options);
			break;
		case 7:
			iv_other2.setVisibility(View.VISIBLE);
			iv_other3.setVisibility(View.VISIBLE);
			iv_other4.setVisibility(View.VISIBLE);
			iv_other5.setVisibility(View.VISIBLE);
			iv_other6.setVisibility(View.VISIBLE);
			iv_other7.setVisibility(View.VISIBLE);
			iv_other8.setVisibility(View.VISIBLE);
			badgeView3.show();
			badgeView4.show();
			badgeView5.show();
			badgeView6.show();
			badgeView7.show();
			badgeView8.show();
			badgeView9.show();
			lay_second.setVisibility(View.VISIBLE);
			lay_third.setVisibility(View.VISIBLE);
			iv_other.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(0),
					iv_other, options);
			iv_other2.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(1),
					iv_other2, options);
			iv_other3.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(2),
					iv_other3, options);
			iv_other4.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(3),
					iv_other4, options);
			iv_other5.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(4),
					iv_other5, options);
			iv_other6.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(5),
					iv_other6, options);
			iv_other7.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(6),
					iv_other7, options);
			break;
		case 8:
			iv_other2.setVisibility(View.VISIBLE);
			iv_other3.setVisibility(View.VISIBLE);
			iv_other4.setVisibility(View.VISIBLE);
			iv_other5.setVisibility(View.VISIBLE);
			iv_other6.setVisibility(View.VISIBLE);
			iv_other7.setVisibility(View.VISIBLE);
			iv_other8.setVisibility(View.VISIBLE);
			iv_other9.setVisibility(View.VISIBLE);
			badgeView3.show();
			badgeView4.show();
			badgeView5.show();
			badgeView6.show();
			badgeView7.show();
			badgeView8.show();
			badgeView9.show();
			badgeView10.show();
			lay_second.setVisibility(View.VISIBLE);
			lay_third.setVisibility(View.VISIBLE);
			iv_other.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(0),
					iv_other, options);
			iv_other2.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(1),
					iv_other2, options);
			iv_other3.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(2),
					iv_other3, options);
			iv_other4.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(3),
					iv_other4, options);
			iv_other5.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(4),
					iv_other5, options);
			iv_other6.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(5),
					iv_other6, options);
			iv_other7.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(6),
					iv_other7, options);
			iv_other8.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(7),
					iv_other8, options);
			break;
		case 9:
			iv_other2.setVisibility(View.VISIBLE);
			iv_other3.setVisibility(View.VISIBLE);
			iv_other4.setVisibility(View.VISIBLE);
			iv_other5.setVisibility(View.VISIBLE);
			iv_other6.setVisibility(View.VISIBLE);
			iv_other7.setVisibility(View.VISIBLE);
			iv_other8.setVisibility(View.VISIBLE);
			iv_other9.setVisibility(View.VISIBLE);
			badgeView3.show();
			badgeView4.show();
			badgeView5.show();
			badgeView6.show();
			badgeView7.show();
			badgeView8.show();
			badgeView9.show();
			badgeView10.show();
			badgeView11.show();
			lay_second.setVisibility(View.VISIBLE);
			lay_third.setVisibility(View.VISIBLE);
			iv_other.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(0),
					iv_other, options);
			iv_other2.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(1),
					iv_other2, options);
			iv_other3.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(2),
					iv_other3, options);
			iv_other4.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(3),
					iv_other4, options);
			iv_other5.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(4),
					iv_other5, options);
			iv_other6.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(5),
					iv_other6, options);
			iv_other7.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(6),
					iv_other7, options);
			iv_other8.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(7),
					iv_other8, options);
			iv_other9.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(8),
					iv_other9, options);
			break;
		}
	}

	class Bt_imageview implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.iv_hourseinfo_other1:
				image = 10;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_hourseinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Hourse_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_hourseinfo_other2:
				image = 11;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_hourseinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();

				params.alpha = 0.7f;
				Hourse_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_hourseinfo_other3:
				image = 12;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_hourseinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Hourse_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_hourseinfo_other4:
				image = 13;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_hourseinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Hourse_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_hourseinfo_other5:
				image = 14;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_hourseinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Hourse_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_hourseinfo_other6:
				image = 15;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_hourseinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Hourse_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_hourseinfo_other7:
				image = 16;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_hourseinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Hourse_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_hourseinfo_other8:
				image = 17;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_hourseinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Hourse_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_hourseinfo_other9:
				image = 18;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_hourseinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Hourse_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_hourseinfo_yezhu:
				image = 8;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_hourseinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Hourse_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_hourseinfo_yezhu02:
				image = 7;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_hourseinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Hourse_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_hourseinfo_yezhu03:
				image = 6;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_hourseinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Hourse_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_hourseinfo_hourse:
				image = 3;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_hourseinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Hourse_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			default:
				break;
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bitmap bitmap = null;
		if (requestCode == CAMERA_RESULT && resultCode == RESULT_OK) {
			if (mPhotoFile != null && mPhotoFile.exists()) {
				// startPhotoZoom(Uri.fromFile(mPhotoFile), PHOTORESOULT);
				bitmap = decodeUriAsBitmap(Uri.fromFile(mPhotoFile));
				switch (image) {
				case 10:
					badgeView3.show();
					iv_other.setImageBitmap(bitmap);
					iv_other2.setVisibility(View.VISIBLE);
					break;
				case 11:
					badgeView4.show();
					iv_other2.setImageBitmap(bitmap);
					iv_other3.setVisibility(View.VISIBLE);
					break;
				case 12:
					badgeView5.show();
					lay_second.setVisibility(View.VISIBLE);
					iv_other3.setImageBitmap(bitmap);
					iv_other4.setVisibility(View.VISIBLE);
					break;
				case 13:
					badgeView6.show();
					iv_other4.setImageBitmap(bitmap);
					iv_other5.setVisibility(View.VISIBLE);
					break;
				case 14:
					badgeView7.show();
					iv_other5.setImageBitmap(bitmap);
					iv_other6.setVisibility(View.VISIBLE);
					break;
				case 15:
					badgeView8.show();
					lay_third.setVisibility(View.VISIBLE);
					iv_other6.setImageBitmap(bitmap);
					iv_other7.setVisibility(View.VISIBLE);
					break;
				case 16:
					badgeView9.show();
					iv_other7.setImageBitmap(bitmap);
					iv_other8.setVisibility(View.VISIBLE);
					break;
				case 17:
					badgeView10.show();
					iv_other8.setImageBitmap(bitmap);
					iv_other9.setVisibility(View.VISIBLE);
					break;
				case 18:
					badgeView11.show();
					iv_other9.setImageBitmap(bitmap);
					break;
				case 8:
					badgeView2.show();
					iv_yezhu.setImageBitmap(bitmap);
					break;
				case 7:
					badgeView12.show();
					iv_yezhu02.setImageBitmap(bitmap);
					break;
				case 6:
					badgeView13.show();
					iv_yezhu03.setImageBitmap(bitmap);
					break;
				case 3:
					badgeView.show();
					iv_hourse.setImageBitmap(bitmap);
					break;
				}
			}
		}
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			// startPhotoZoom(data.getData(), PHOTORESOULT);
			bitmap = decodeUriAsBitmap(data.getData());
			switch (image) {
			case 10:
				badgeView3.show();
				iv_other.setImageBitmap(bitmap);
				iv_other2.setVisibility(View.VISIBLE);
				break;
			case 11:
				badgeView4.show();
				iv_other2.setImageBitmap(bitmap);
				iv_other3.setVisibility(View.VISIBLE);
				break;
			case 12:
				badgeView5.show();
				lay_second.setVisibility(View.VISIBLE);
				iv_other3.setImageBitmap(bitmap);
				iv_other4.setVisibility(View.VISIBLE);
				break;
			case 13:
				badgeView6.show();
				iv_other4.setImageBitmap(bitmap);
				iv_other5.setVisibility(View.VISIBLE);
				break;
			case 14:
				badgeView7.show();
				iv_other5.setImageBitmap(bitmap);
				iv_other6.setVisibility(View.VISIBLE);
				break;
			case 15:
				badgeView8.show();
				lay_third.setVisibility(View.VISIBLE);
				iv_other6.setImageBitmap(bitmap);
				iv_other7.setVisibility(View.VISIBLE);
				break;
			case 16:
				badgeView9.show();
				iv_other7.setImageBitmap(bitmap);
				iv_other8.setVisibility(View.VISIBLE);
				break;
			case 17:
				badgeView10.show();
				iv_other8.setImageBitmap(bitmap);
				iv_other9.setVisibility(View.VISIBLE);
				break;
			case 18:
				badgeView11.show();
				iv_other9.setImageBitmap(bitmap);
				break;
			case 8:
				badgeView2.show();
				iv_yezhu.setImageBitmap(bitmap);
				iv_yezhu02.setVisibility(View.VISIBLE);
				break;
			case 7:
				badgeView12.show();
				iv_yezhu02.setImageBitmap(bitmap);
				iv_yezhu03.setVisibility(View.VISIBLE);
				break;
			case 6:
				badgeView13.show();
				iv_yezhu03.setImageBitmap(bitmap);
				break;
			case 3:
				badgeView.show();
				iv_hourse.setImageBitmap(bitmap);
				break;
			}

		}

		upload_image(bitmap);
		// if (requestCode == PHOTORESOULT && resultCode == RESULT_OK) {
		//
		// if (imageuri != null) {
		// }
		// }
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

	public void upload_image(Bitmap bitmap) {
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
		Request request = null;
		if (image == 3) {
			request = Request.requestImg(photo_stream, "3", "1", "1", "2");
		} else if (image == 8 || image == 7 || image == 6) {
			request = Request.requestImg(photo_stream, "8", "1", "2", "2");
		} else {
			request = Request.requestImg(photo_stream, "3", "1", "2", "2");
		}
		dialog.dialog();
		new HttpThread(request, Hourse_InfoeditAct.this);
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

	@Override
	protected void onDestroy() {
		destoryImage();
		super.onDestroy();
	}

	private void destoryImage() {
		if (photo != null) {
			photo.recycle();
			photo = null;
		}
	}

	public void selecticon(int flag) {
		switch (flag) {
		case 1:
			iv_icon01.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_checked));
			iv_icon02.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			break;
		case 2:
			iv_icon01.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_uncheck));
			iv_icon02.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.resourse_images_checked));
			break;
		default:
			break;
		}
	}

	public void selecticon02(int flag) {
		switch (flag) {
		case 1:
			iv_hourse_infoedit.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.resourse_images_checked));
			iv_hourse_infoedit02.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.resourse_images_uncheck));
			break;
		case 2:
			iv_hourse_infoedit.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.resourse_images_uncheck));
			iv_hourse_infoedit02.setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.resourse_images_checked));
			break;
		default:
			break;
		}
	}

	public void clearWindow() {
		WindowManager.LayoutParams params = Hourse_InfoeditAct.this.getWindow()
				.getAttributes();
		params.alpha = 1f;
		Hourse_InfoeditAct.this.getWindow().setAttributes(params);
	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					clearWindow();
					Hourse_InfoeditAct.this.finish();
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

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Toast.makeText(Hourse_InfoeditAct.this, "更新成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(Hourse_InfoeditAct.this,
						Hourse_FollowAct.class);
				intent.putExtra("id", hourse_id);
				startActivity(intent);
				finish();
				break;
			case 2:
				Toast.makeText(Hourse_InfoeditAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 44:
				Toast.makeText(Hourse_InfoeditAct.this, "图片上传失败", Toast.LENGTH_SHORT).show();
				break;
			case 55:
				Toast.makeText(Hourse_InfoeditAct.this, "图片上传成功", Toast.LENGTH_SHORT).show();
				break;
			case 4004:
				Toast.makeText(Hourse_InfoeditAct.this, "图片类型错误", Toast.LENGTH_SHORT).show();
				break;
			case 4005:
				Toast.makeText(Hourse_InfoeditAct.this, "图片非法", Toast.LENGTH_SHORT).show();
				break;
			case 4067:
				Toast.makeText(Hourse_InfoeditAct.this, "房源不存在", Toast.LENGTH_SHORT).show();
				break;
			case 4088:
				Toast.makeText(Hourse_InfoeditAct.this, "发布人信息有误", Toast.LENGTH_SHORT).show();
				break;
			case 4068:
				Toast.makeText(Hourse_InfoeditAct.this, "房源不存在", Toast.LENGTH_SHORT).show();
				break;
			case 4058:
				Toast.makeText(Hourse_InfoeditAct.this, "房源单元有误", Toast.LENGTH_SHORT).show();
				break;
			case 4059:
				Toast.makeText(Hourse_InfoeditAct.this, "交易类型有误", Toast.LENGTH_SHORT).show();
				break;
			case 4061:
				Toast.makeText(Hourse_InfoeditAct.this, "房源位置有误", Toast.LENGTH_SHORT).show();
				break;
			case 4074:
				Toast.makeText(Hourse_InfoeditAct.this, "房源朝向有误", Toast.LENGTH_SHORT).show();
				break;
			case 4075:
				Toast.makeText(Hourse_InfoeditAct.this, "交付状态有误", Toast.LENGTH_SHORT).show();
				break;
			case 4030:
				Toast.makeText(Hourse_InfoeditAct.this, "最短租约有误", Toast.LENGTH_SHORT).show();
				break;
			case 4076:
				Toast.makeText(Hourse_InfoeditAct.this, "交付日期有误或者中介佣金未输入", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4077:
				Toast.makeText(Hourse_InfoeditAct.this, "联系电话有误", Toast.LENGTH_SHORT).show();
				break;
			case 4069:
				Toast.makeText(Hourse_InfoeditAct.this, "房源简介有误", Toast.LENGTH_SHORT).show();
				break;
			case 4071:
				Toast.makeText(Hourse_InfoeditAct.this, "房源状态有误", Toast.LENGTH_SHORT).show();
				break;
			case 4078:
				Toast.makeText(Hourse_InfoeditAct.this, "房源封面图有误", Toast.LENGTH_SHORT).show();
				break;
			case 4079:
				Toast.makeText(Hourse_InfoeditAct.this, "房源图片有误", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				dialog.dismiss();
				Toast.makeText(Hourse_InfoeditAct.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public void select_customeradd(String session_id, String id,
			String category, String unit_no, String square_meter,
			String transaction_type, String room_price, String position,
			String head_to, String delivery_status, String delivery_date,
			String fee_rate, String min_rent_period, String contact_phone,
			String chair_total, String intro, String publish_status,
			String status, String avatar, String images, String land_id) {
		Request request = Request.requesthourseinfoedit(session_id, id,
				category, unit_no, square_meter, transaction_type, room_price,
				room_price_unit, position, head_to, delivery_status,
				delivery_date, fee_rate, min_rent_period, contact_phone,
				chair_total, intro, publish_status, status, avatar, images,
				land_id);
		new HttpThread(request, this);
	}

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC_COMPANY) {
			dialog.dismiss();
			JSONObject json;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					newHandler.sendEmptyMessage(1);
				} else if (errorid.equals("1")) {
					newHandler.sendEmptyMessage(2);
				} else if (errorid.equals("4067")) {
					newHandler.sendEmptyMessage(4067);
				} else if (errorid.equals("4088")) {
					newHandler.sendEmptyMessage(4088);
				} else if (errorid.equals("4068")) {
					newHandler.sendEmptyMessage(4068);
				} else if (errorid.equals("4058")) {
					newHandler.sendEmptyMessage(4058);
				} else if (errorid.equals("4059")) {
					newHandler.sendEmptyMessage(4059);
				} else if (errorid.equals("4061")) {
					newHandler.sendEmptyMessage(4061);
				} else if (errorid.equals("4074")) {
					newHandler.sendEmptyMessage(4074);
				} else if (errorid.equals("4075")) {
					newHandler.sendEmptyMessage(4075);
				} else if (errorid.equals("4076")) {
					newHandler.sendEmptyMessage(4076);
				} else if (errorid.equals("4030")) {
					newHandler.sendEmptyMessage(4030);
				} else if (errorid.equals("4077")) {
					newHandler.sendEmptyMessage(4077);
				} else if (errorid.equals("4069")) {
					newHandler.sendEmptyMessage(4069);
				} else if (errorid.equals("4071")) {
					newHandler.sendEmptyMessage(4071);
				} else if (errorid.equals("4078")) {
					newHandler.sendEmptyMessage(4078);
				} else if (errorid.equals("4079")) {
					newHandler.sendEmptyMessage(4079);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (reqID == Request.REQUEST_IMG_STREAM) {
			dialog.dismiss();
			JSONObject json, json2;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					avatar_ids = json.getString("id");
					avatars = json.getString("url");
					if (image == 3) {
						avatar_id = avatar_ids;
					} else if (image == 8) {
						if (str_land_id.size()==1) {
							str_land_id.set(0, avatar_ids);
						}else {
							str_land_id.add(avatar_ids);
						}
					}else if (image==7) {
						if (str_land_id.size()==2) {
							str_land_id.set(1, avatar_ids);
						}else {
							str_land_id.add(avatar_ids);
						}
					}else if (image==6) {
						if (str_land_id.size()==3) {
							str_land_id.set(2, avatar_ids);
						}else {
							str_land_id.add(avatar_ids);
						}
					}
					if (image == 10) {
						if (str_images_id.size() == 1) {
							str_images_id.set(0, avatar_ids);
						} else {
							str_images_id.add(avatar_ids);
						}
					} else if (image == 11) {
						if (str_images_id.size() == 2) {
							str_images_id.set(1, avatar_ids);
						} else {
							str_images_id.add(avatar_ids);
						}
					} else if (image == 12) {
						if (str_images_id.size() == 3) {
							str_images_id.set(2, avatar_ids);
						} else {
							str_images_id.add(avatar_ids);
						}
					} else if (image == 13) {
						if (str_images_id.size() == 4) {
							str_images_id.set(3, avatar_ids);
						} else {
							str_images_id.add(avatar_ids);
						}
					} else if (image == 14) {
						if (str_images_id.size() == 5) {
							str_images_id.set(4, avatar_ids);
						} else {
							str_images_id.add(avatar_ids);
						}
					} else if (image == 15) {
						if (str_images_id.size() == 6) {
							str_images_id.set(5, avatar_ids);
						} else {
							str_images_id.add(avatar_ids);
						}
					} else if (image == 16) {
						if (str_images_id.size() == 7) {
							str_images_id.set(6, avatar_ids);
						} else {
							str_images_id.add(avatar_ids);
						}
					} else if (image == 17) {
						if (str_images_id.size() == 8) {
							str_images_id.set(7, avatar_ids);
						} else {
							str_images_id.add(avatar_ids);
						}
					} else if (image == 18) {
						if (str_images_id.size() == 9) {
							str_images_id.set(8, avatar_ids);
						} else {
							str_images_id.add(avatar_ids);
						}
					}

					newHandler.sendEmptyMessage(55);
				} else if (errorid.equals("4004")) {
					newHandler.sendEmptyMessage(4004);
				} else if (errorid.equals("4005")) {
					newHandler.sendEmptyMessage(4005);
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent(Hourse_InfoeditAct.this,
					Hourse_FollowAct.class);
			intent.putExtra("id", hourse_id);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void doError(String s) {
		// TODO Auto-generated method stub
		if (s.equals("请求异常")) {
			newHandler.sendEmptyMessage(99);
		}
	}
}
