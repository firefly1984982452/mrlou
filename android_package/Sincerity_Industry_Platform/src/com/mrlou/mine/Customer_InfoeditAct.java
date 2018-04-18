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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrlou.economic.view.BadgeView;
import com.mrlou.listener.HttpListener;
import com.mrlou.message.Message_SingleBuilderAct;
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
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class Customer_InfoeditAct extends Activity implements HttpListener {

	private LinearLayout lay_customerinfo_phone, lay_customer_infoedit_back,
			lay_icon01, lay_icon02, lay_customer_infoedit_time,
			lay_iv_customerinfo_other, lay_customer_infoedit_eidt;
	private LinearLayout lay_second, lay_third, lay_date_end;
	private TextView tv_customer_infoedit_time, tv_date_end;
	private ImageView iv_other, iv_other2, iv_other3, iv_other4, iv_other5,
			iv_other6, iv_other7, iv_other8, iv_other9, iv_icon01, iv_icon02;
	private View view_pop;
	private Bitmap photo;
	private Button bt_pic, bt_camera, bt_cancel;
	private PopupWindow pop_image;
	private int CAMERA_RESULT = 100;
	private int RESULT_LOAD_IMAGE = 200;
	private int PHOTORESOULT = 300;
	private int year, month, day, image;
	private String saveDir = Environment.getExternalStorageDirectory()
			.getPath() + "/temp_image";
	private File mPhotoFile;
	private Uri imageuri;
	private WindowManager.LayoutParams params;
	private String session_id, id, customer_id, name, transaction_type,
			first_access_time, sector, pre_square_meter, pre_room, pre_price_1,
			intro, process, phone_number, company, job_title, use_for, type,
			company_property, employee_total, current_building, expire_time,
			avatars_id, avatars, photo_stream, images_id = "";
	private EditText et_info_name, et_info_job, et_info_area, et_info_price,
			et_info_hourse, et_info_intro, et_info_phone, et_info_company,
			et_info_jobtype, et_info_user_for, et_info_property, et_info_total,
			et_info_building;
	private Button bt_customer_delete;
	private ArrayList<String> str_images_url, str_images_id;
	// private ImageLoader imageLoader;
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private SweetAlertDialog pDialog;
	private Progress_Dialog dialog;
	private BadgeView badgeView1, badgeView2, badgeView3, badgeView4,
			badgeView5, badgeView6, badgeView7, badgeView8, badgeView9;
	private LinearLayout lay_customerinfo_other1, lay_customerinfo_other2,
			lay_customerinfo_other3, lay_customerinfo_other4,
			lay_customerinfo_other5, lay_customerinfo_other6,
			lay_customerinfo_other7, lay_customerinfo_other8,
			lay_customerinfo_other9;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_customer_infoedit);
		getDate();
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration
						.createDefault(Customer_InfoeditAct.this));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
				.build();
		// imageLoader = new ImageLoader(new ImageDownloader() {
		// @Override
		// public Bitmap download(String path, int width, int height) {
		// return HttpUtil.download(path);
		// }
		// });
		findView();
	}

	private void getDate() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		session_id = intent.getStringExtra("session_id");
		name = intent.getStringExtra("name");
		id = intent.getStringExtra("id");
		transaction_type = intent.getStringExtra("transaction_type");
		first_access_time = intent.getStringExtra("first_access_time");
		sector = intent.getStringExtra("sector");
		pre_square_meter = intent.getStringExtra("pre_square_meter");
		pre_room = intent.getStringExtra("pre_room");
		pre_price_1 = intent.getStringExtra("pre_price_1");
		intro = intent.getStringExtra("intro");
		phone_number = intent.getStringExtra("phone_number");
		process = intent.getStringExtra("process");
		company = intent.getStringExtra("company");
		job_title = intent.getStringExtra("job_title");
		use_for = intent.getStringExtra("use_for");
		company_property = intent.getStringExtra("company_property");
		employee_total = intent.getStringExtra("employee_total");
		current_building = intent.getStringExtra("current_building");
		expire_time = intent.getStringExtra("expire_time");
		customer_id = intent.getStringExtra("customer_id");
		type = intent.getStringExtra("type");
		str_images_url = intent.getStringArrayListExtra("images_url");
		str_images_id = intent.getStringArrayListExtra("images_id");
	}

	private void findView() {
		// TODO Auto-generated method stub
		// 弹出请求对话框
		pDialog = new SweetAlertDialog(Customer_InfoeditAct.this,
				SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
		dialog = new Progress_Dialog(Customer_InfoeditAct.this, pDialog);
		// 添加日期
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		tv_customer_infoedit_time = (TextView) findViewById(R.id.tv_customer_infoedit_time);
		tv_customer_infoedit_time.setText(first_access_time);
		lay_customer_infoedit_time = (LinearLayout) findViewById(R.id.lay_customer_infoedit_time);
		lay_customer_infoedit_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatePickerDialog datePicker = new DatePickerDialog(
						Customer_InfoeditAct.this, new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int years,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								tv_customer_infoedit_time.setText(year + "-"
										+ (monthOfYear + 1) + "-" + dayOfMonth);
								year = years;
								month = monthOfYear;
								day = dayOfMonth;
							}
						}, year, month, day);
				datePicker.show();
			}
		});
		tv_date_end = (TextView) findViewById(R.id.tv_date_end);
		tv_date_end.setText(expire_time);
		lay_date_end = (LinearLayout) findViewById(R.id.lay_date_end);
		lay_date_end.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatePickerDialog datePicker = new DatePickerDialog(
						Customer_InfoeditAct.this, new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int years,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								tv_date_end.setText(year + "-"
										+ (monthOfYear + 1) + "-" + dayOfMonth);
								year = years;
								month = monthOfYear;
								day = dayOfMonth;
							}
						}, year, month - 1, day);
				datePicker.show();
			}
		});

		bt_customer_delete = (Button) findViewById(R.id.bt_customer_delete);
		bt_customer_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dialog();
				select_customerdelete(id, session_id);
			}
		});

		lay_icon01 = (LinearLayout) findViewById(R.id.lay_customer_infoedit_icon01);
		lay_icon01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				transaction_type = "1";
				selecticon(1);
			}
		});
		lay_icon02 = (LinearLayout) findViewById(R.id.lay_customer_infoedit_icon02);
		lay_icon02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				transaction_type = "2";
				selecticon(2);
			}
		});
		iv_icon01 = (ImageView) findViewById(R.id.iv_customer_infoedit_icon01);
		iv_icon02 = (ImageView) findViewById(R.id.iv_customer_infoedit_icon02);
		if (transaction_type.equals("1")) {
			selecticon(1);
		} else {
			selecticon(2);
		}
		et_info_name = (EditText) findViewById(R.id.et_info_name);
		et_info_name.setText(name);
		et_info_job = (EditText) findViewById(R.id.et_info_job);
		et_info_job.setText(sector);
		et_info_area = (EditText) findViewById(R.id.et_info_area);
		et_info_area.setText(pre_square_meter);
		et_info_price = (EditText) findViewById(R.id.et_info_price);
		et_info_price.setText(pre_price_1);
		et_info_hourse = (EditText) findViewById(R.id.et_info_hourse);
		et_info_hourse.setText(pre_room);
		et_info_intro = (EditText) findViewById(R.id.et_info_intro);
		et_info_intro.setText(intro);
		et_info_phone = (EditText) findViewById(R.id.et_info_phone);
		et_info_phone.setText(phone_number);
		et_info_company = (EditText) findViewById(R.id.et_info_company);
		et_info_company.setText(company);
		et_info_jobtype = (EditText) findViewById(R.id.et_info_jobtype);
		et_info_jobtype.setText(job_title);
		et_info_user_for = (EditText) findViewById(R.id.et_info_user_for);
		et_info_user_for.setText(use_for);
		et_info_property = (EditText) findViewById(R.id.et_info_property);
		et_info_property.setText(company_property);
		et_info_total = (EditText) findViewById(R.id.et_info_total);
		et_info_total.setText(employee_total);
		et_info_building = (EditText) findViewById(R.id.et_info_building);
		et_info_building.setText(current_building);
		lay_customer_infoedit_back = (LinearLayout) findViewById(R.id.lay_customer_infoedit_back);
		lay_customer_infoedit_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Customer_InfoeditAct.this,
						Customer_DetailAct.class);
				intent.putExtra("id", customer_id);
				startActivity(intent);
				finish();
				// simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		imageuri = Uri.fromFile(new File(saveDir + "temp2.png"));
		lay_customerinfo_phone = (LinearLayout) findViewById(R.id.lay_customerinfo_phone);
		lay_second = (LinearLayout) findViewById(R.id.lay_customer_second);
		lay_third = (LinearLayout) findViewById(R.id.lay_customer_third);
		iv_other = (ImageView) findViewById(R.id.iv_customerinfo_other1);
		iv_other.setOnClickListener(new Bt_imageview());
		iv_other2 = (ImageView) findViewById(R.id.iv_customerinfo_other2);
		iv_other2.setOnClickListener(new Bt_imageview());
		iv_other3 = (ImageView) findViewById(R.id.iv_customerinfo_other3);
		iv_other3.setOnClickListener(new Bt_imageview());
		iv_other4 = (ImageView) findViewById(R.id.iv_customerinfo_other4);
		iv_other4.setOnClickListener(new Bt_imageview());
		iv_other5 = (ImageView) findViewById(R.id.iv_customerinfo_other5);
		iv_other5.setOnClickListener(new Bt_imageview());
		iv_other6 = (ImageView) findViewById(R.id.iv_customerinfo_other6);
		iv_other6.setOnClickListener(new Bt_imageview());
		iv_other7 = (ImageView) findViewById(R.id.iv_customerinfo_other7);
		iv_other7.setOnClickListener(new Bt_imageview());
		iv_other8 = (ImageView) findViewById(R.id.iv_customerinfo_other8);
		iv_other8.setOnClickListener(new Bt_imageview());
		iv_other9 = (ImageView) findViewById(R.id.iv_customerinfo_other9);
		iv_other9.setOnClickListener(new Bt_imageview());
		
		params = Customer_InfoeditAct.this.getWindow().getAttributes();
		view_pop = LayoutInflater.from(Customer_InfoeditAct.this).inflate(
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
					mPhotoFile = new File(saveDir, "temp" + image + ".png");
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
		lay_customer_infoedit_eidt = (LinearLayout) findViewById(R.id.lay_customer_infoedit_eidt);
		lay_customer_infoedit_eidt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dialog();
				for (int i = str_images_id.size()-1; i >=0; i--) {
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
				}else {
					images_id="";
				}
				select_customeredit(session_id, id, et_info_name.getText()
						.toString(), transaction_type,
						tv_customer_infoedit_time.getText().toString(),
						et_info_job.getText().toString(), et_info_area
								.getText().toString(), et_info_hourse.getText()
								.toString(),
						et_info_price.getText().toString(), "㎡", et_info_intro
								.getText().toString(), process, et_info_phone
								.getText().toString(), et_info_company
								.getText().toString(), et_info_jobtype
								.getText().toString(), et_info_user_for
								.getText().toString(), et_info_property
								.getText().toString(), et_info_total.getText()
								.toString(), et_info_building.getText()
								.toString(), tv_date_end.getText().toString(),
						images_id);
			}
		});
		
		lay_customerinfo_other1=(LinearLayout) findViewById(R.id.lay_customerinfo_other1);
		badgeView1 = new BadgeView(Customer_InfoeditAct.this,
				lay_customerinfo_other1);
		badgeView1.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView1.setBackgroundResource(R.drawable.fangyuan_images_del_img);
		badgeView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_other.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images_id.set(0, "1");
				badgeView1.hide();
			}
		});
		
		lay_customerinfo_other2=(LinearLayout) findViewById(R.id.lay_customerinfo_other2);
		badgeView2 = new BadgeView(Customer_InfoeditAct.this,
				lay_customerinfo_other2);
		badgeView2.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView2.setBackgroundResource(R.drawable.fangyuan_images_del_img);
		badgeView2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_other2.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images_id.set(1, "1");
				badgeView2.hide();
			}
		});
		
		lay_customerinfo_other3=(LinearLayout) findViewById(R.id.lay_customerinfo_other3);
		badgeView3 = new BadgeView(Customer_InfoeditAct.this,
				lay_customerinfo_other3);
		badgeView3.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView3.setBackgroundResource(R.drawable.fangyuan_images_del_img);
		badgeView3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_other3.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images_id.set(2, "1");
				badgeView3.hide();
			}
		});
		
		lay_customerinfo_other4=(LinearLayout) findViewById(R.id.lay_customerinfo_other4);
		badgeView4 = new BadgeView(Customer_InfoeditAct.this,
				lay_customerinfo_other4);
		badgeView4.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView4.setBackgroundResource(R.drawable.fangyuan_images_del_img);
		badgeView4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_other4.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images_id.set(3, "1");
				badgeView4.hide();
			}
		});
		
		lay_customerinfo_other5=(LinearLayout) findViewById(R.id.lay_customerinfo_other5);
		badgeView5 = new BadgeView(Customer_InfoeditAct.this,
				lay_customerinfo_other5);
		badgeView5.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView5.setBackgroundResource(R.drawable.fangyuan_images_del_img);
		badgeView5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_other5.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images_id.set(4, "1");
				badgeView5.hide();
			}
		});
		
		lay_customerinfo_other6=(LinearLayout) findViewById(R.id.lay_customerinfo_other6);
		badgeView6 = new BadgeView(Customer_InfoeditAct.this,
				lay_customerinfo_other6);
		badgeView6.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView6.setBackgroundResource(R.drawable.fangyuan_images_del_img);
		badgeView6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_other6.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images_id.set(5, "1");
				badgeView6.hide();
			}
		});
		
		lay_customerinfo_other7=(LinearLayout) findViewById(R.id.lay_customerinfo_other7);
		badgeView7 = new BadgeView(Customer_InfoeditAct.this,
				lay_customerinfo_other7);
		badgeView7.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView7.setBackgroundResource(R.drawable.fangyuan_images_del_img);
		badgeView7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_other7.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images_id.set(6, "1");
				badgeView7.hide();
			}
		});
		
		lay_customerinfo_other8=(LinearLayout) findViewById(R.id.lay_customerinfo_other8);
		badgeView8 = new BadgeView(Customer_InfoeditAct.this,
				lay_customerinfo_other8);
		badgeView8.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView8.setBackgroundResource(R.drawable.fangyuan_images_del_img);
		badgeView8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_other8.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images_id.set(7, "1");
				badgeView8.hide();
			}
		});
		
		lay_customerinfo_other9=(LinearLayout) findViewById(R.id.lay_customerinfo_other2);
		badgeView9 = new BadgeView(Customer_InfoeditAct.this,
				lay_customerinfo_other9);
		badgeView9.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView9.setBackgroundResource(R.drawable.fangyuan_images_del_img);
		badgeView9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_other9.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images_id.set(8, "1");
				badgeView9.hide();
			}
		});
		
		if (str_images_id.size() > 0) {
			select_pic(str_images_url.size());
		}
	}

	private void select_pic(int image) {
		switch (image) {
		case 1:
			iv_other2.setVisibility(View.VISIBLE);
			badgeView1.show();
			iv_other.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(0),
					iv_other, options);
			// imageLoader.loadImage(str_images_url.get(0), 100, 100, iv_other);
			break;
		case 2:
			iv_other2.setVisibility(View.VISIBLE);
			iv_other3.setVisibility(View.VISIBLE);
			badgeView1.show();
			badgeView2.show();
			iv_other.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(0),
					iv_other, options);
			// imageLoader.loadImage(str_images_url.get(0), 100, 100, iv_other);
			iv_other2.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(1),
					iv_other2, options);
			// imageLoader.loadImage(str_images_url.get(1), 100, 100,
			// iv_other2);
			break;
		case 3:
			iv_other2.setVisibility(View.VISIBLE);
			iv_other3.setVisibility(View.VISIBLE);
			iv_other4.setVisibility(View.VISIBLE);
			badgeView1.show();
			badgeView2.show();
			badgeView3.show();
			lay_second.setVisibility(View.VISIBLE);
			iv_other.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(0),
					iv_other, options);
			// imageLoader.loadImage(str_images_url.get(0), 100, 100, iv_other);
			iv_other2.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(1),
					iv_other2, options);
			// imageLoader.loadImage(str_images_url.get(1), 100, 100,
			// iv_other2);
			iv_other3.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(2),
					iv_other3, options);
			// imageLoader.loadImage(str_images_url.get(2), 100, 100,
			// iv_other3);
			break;
		case 4:
			iv_other2.setVisibility(View.VISIBLE);
			iv_other3.setVisibility(View.VISIBLE);
			iv_other4.setVisibility(View.VISIBLE);
			iv_other5.setVisibility(View.VISIBLE);
			badgeView1.show();
			badgeView2.show();
			badgeView3.show();
			badgeView4.show();
			lay_second.setVisibility(View.VISIBLE);
			iv_other.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(0),
					iv_other, options);
			// imageLoader.loadImage(str_images_url.get(0), 100, 100, iv_other);
			iv_other2.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(1),
					iv_other2, options);
			// imageLoader.loadImage(str_images_url.get(1), 100, 100,
			// iv_other2);
			iv_other3.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(2),
					iv_other3, options);
			// imageLoader.loadImage(str_images_url.get(2), 100, 100,
			// iv_other3);
			iv_other4.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(3),
					iv_other4, options);
			// imageLoader.loadImage(str_images_url.get(3), 100, 100,
			// iv_other4);
			break;
		case 5:
			iv_other2.setVisibility(View.VISIBLE);
			iv_other3.setVisibility(View.VISIBLE);
			iv_other4.setVisibility(View.VISIBLE);
			iv_other5.setVisibility(View.VISIBLE);
			iv_other6.setVisibility(View.VISIBLE);
			badgeView1.show();
			badgeView2.show();
			badgeView3.show();
			badgeView4.show();
			badgeView5.show();
			lay_second.setVisibility(View.VISIBLE);
			iv_other.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(0),
					iv_other, options);
			// imageLoader.loadImage(str_images_url.get(0), 100, 100, iv_other);
			iv_other2.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(1),
					iv_other2, options);
			// imageLoader.loadImage(str_images_url.get(1), 100, 100,
			// iv_other2);
			iv_other3.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(2),
					iv_other3, options);
			// imageLoader.loadImage(str_images_url.get(2), 100, 100,
			// iv_other3);
			iv_other4.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(3),
					iv_other4, options);
			// imageLoader.loadImage(str_images_url.get(3), 100, 100,
			// iv_other4);
			iv_other5.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(4),
					iv_other5, options);
			// imageLoader.loadImage(str_images_url.get(4), 100, 100,
			// iv_other5);
			break;
		case 6:
			iv_other2.setVisibility(View.VISIBLE);
			iv_other3.setVisibility(View.VISIBLE);
			iv_other4.setVisibility(View.VISIBLE);
			iv_other5.setVisibility(View.VISIBLE);
			iv_other6.setVisibility(View.VISIBLE);
			iv_other7.setVisibility(View.VISIBLE);
			badgeView1.show();
			badgeView2.show();
			badgeView3.show();
			badgeView4.show();
			badgeView5.show();
			badgeView6.show();
			lay_second.setVisibility(View.VISIBLE);
			lay_third.setVisibility(View.VISIBLE);
			iv_other.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(0),
					iv_other, options);
			// imageLoader.loadImage(str_images_url.get(0), 100, 100, iv_other);
			iv_other2.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(1),
					iv_other2, options);
			// imageLoader.loadImage(str_images_url.get(1), 100, 100,
			// iv_other2);
			iv_other3.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(2),
					iv_other3, options);
			// imageLoader.loadImage(str_images_url.get(2), 100, 100,
			// iv_other3);
			iv_other4.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(3),
					iv_other4, options);
			// imageLoader.loadImage(str_images_url.get(3), 100, 100,
			// iv_other4);
			iv_other5.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(4),
					iv_other5, options);
			// imageLoader.loadImage(str_images_url.get(4), 100, 100,
			// iv_other5);
			iv_other6.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(5),
					iv_other6, options);
			// imageLoader.loadImage(str_images_url.get(5), 100, 100,
			// iv_other6);
			break;
		case 7:
			iv_other2.setVisibility(View.VISIBLE);
			iv_other3.setVisibility(View.VISIBLE);
			iv_other4.setVisibility(View.VISIBLE);
			iv_other5.setVisibility(View.VISIBLE);
			iv_other6.setVisibility(View.VISIBLE);
			iv_other7.setVisibility(View.VISIBLE);
			iv_other8.setVisibility(View.VISIBLE);
			badgeView1.show();
			badgeView2.show();
			badgeView3.show();
			badgeView4.show();
			badgeView5.show();
			badgeView6.show();
			badgeView7.show();
			lay_second.setVisibility(View.VISIBLE);
			lay_third.setVisibility(View.VISIBLE);
			iv_other.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(0),
					iv_other, options);
			// imageLoader.loadImage(str_images_url.get(0), 100, 100, iv_other);
			iv_other2.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(1),
					iv_other2, options);
			// imageLoader.loadImage(str_images_url.get(1), 100, 100,
			// iv_other2);
			iv_other3.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(2),
					iv_other3, options);
			// imageLoader.loadImage(str_images_url.get(2), 100, 100,
			// iv_other3);
			iv_other4.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(3),
					iv_other4, options);
			// imageLoader.loadImage(str_images_url.get(3), 100, 100,
			// iv_other4);
			iv_other5.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(4),
					iv_other5, options);
			// imageLoader.loadImage(str_images_url.get(4), 100, 100,
			// iv_other5);
			iv_other6.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(5),
					iv_other6, options);
			// imageLoader.loadImage(str_images_url.get(5), 100, 100,
			// iv_other6);
			iv_other7.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(6),
					iv_other7, options);
			// imageLoader.loadImage(str_images_url.get(6), 100, 100,
			// iv_other7);
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
			badgeView1.show();
			badgeView2.show();
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
			// imageLoader.loadImage(str_images_url.get(0), 100, 100, iv_other);
			iv_other2.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(1),
					iv_other2, options);
			// imageLoader.loadImage(str_images_url.get(1), 100, 100,
			// iv_other2);
			iv_other3.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(2),
					iv_other3, options);
			// imageLoader.loadImage(str_images_url.get(2), 100, 100,
			// iv_other3);
			iv_other4.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(3),
					iv_other4, options);
			// imageLoader.loadImage(str_images_url.get(3), 100, 100,
			// iv_other4);
			iv_other5.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(4),
					iv_other5, options);
			// imageLoader.loadImage(str_images_url.get(4), 100, 100,
			// iv_other5);
			iv_other6.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(5),
					iv_other6, options);
			// imageLoader.loadImage(str_images_url.get(5), 100, 100,
			// iv_other6);
			iv_other7.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(6),
					iv_other7, options);
			// imageLoader.loadImage(str_images_url.get(6), 100, 100,
			// iv_other7);
			iv_other8.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(7),
					iv_other8, options);
			// imageLoader.loadImage(str_images_url.get(7), 100, 100,
			// iv_other8);
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
			badgeView1.show();
			badgeView2.show();
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
			// imageLoader.loadImage(str_images_url.get(0), 100, 100, iv_other);
			iv_other2.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(1),
					iv_other2, options);
			// imageLoader.loadImage(str_images_url.get(1), 100, 100,
			// iv_other2);
			iv_other3.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(2),
					iv_other3, options);
			// imageLoader.loadImage(str_images_url.get(2), 100, 100,
			// iv_other3);
			iv_other4.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(3),
					iv_other4, options);
			// imageLoader.loadImage(str_images_url.get(3), 100, 100,
			// iv_other4);
			iv_other5.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(4),
					iv_other5, options);
			// imageLoader.loadImage(str_images_url.get(4), 100, 100,
			// iv_other5);
			iv_other6.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(5),
					iv_other6, options);
			// imageLoader.loadImage(str_images_url.get(5), 100, 100,
			// iv_other6);
			iv_other7.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(6),
					iv_other7, options);
			// imageLoader.loadImage(str_images_url.get(6), 100, 100,
			// iv_other7);
			iv_other8.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(7),
					iv_other8, options);
			// imageLoader.loadImage(str_images_url.get(7), 100, 100,
			// iv_other8);
			iv_other9.setImageResource(R.drawable.fangyuan_images_no_content);
			ImageLoader.getInstance().displayImage(str_images_url.get(8),
					iv_other9, options);
			// imageLoader.loadImage(str_images_url.get(8), 100, 100,
			// iv_other9);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bitmap bitmap=null;
		if (requestCode == CAMERA_RESULT && resultCode == RESULT_OK) {
			if (mPhotoFile != null && mPhotoFile.exists()) {
//				startPhotoZoom(Uri.fromFile(mPhotoFile), PHOTORESOULT);
				bitmap = decodeUriAsBitmap(Uri.fromFile(mPhotoFile));
				switch (image) {
				case 1:
					iv_other.setImageBitmap(bitmap);
					iv_other2.setVisibility(View.VISIBLE);
					badgeView1.show();
					break;
				case 2:
					iv_other2.setImageBitmap(bitmap);
					iv_other3.setVisibility(View.VISIBLE);
					badgeView2.show();
					break;
				case 3:
					lay_second.setVisibility(View.VISIBLE);
					iv_other3.setImageBitmap(bitmap);
					iv_other4.setVisibility(View.VISIBLE);
					badgeView3.show();
					break;
				case 4:
					iv_other4.setImageBitmap(bitmap);
					iv_other5.setVisibility(View.VISIBLE);
					badgeView4.show();
					break;
				case 5:
					iv_other5.setImageBitmap(bitmap);
					iv_other6.setVisibility(View.VISIBLE);
					badgeView5.show();
					break;
				case 6:
					lay_third.setVisibility(View.VISIBLE);
					iv_other6.setImageBitmap(bitmap);
					iv_other7.setVisibility(View.VISIBLE);
					badgeView6.show();
					break;
				case 7:
					iv_other7.setImageBitmap(bitmap);
					iv_other8.setVisibility(View.VISIBLE);
					badgeView7.show();
					break;
				case 8:
					iv_other8.setImageBitmap(bitmap);
					iv_other9.setVisibility(View.VISIBLE);
					badgeView8.show();
					break;
				case 9:
					iv_other9.setImageBitmap(bitmap);
					badgeView9.show();
					break;
				default:
					break;
				}
			}
		}
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
//			startPhotoZoom(data.getData(), PHOTORESOULT);
			bitmap = decodeUriAsBitmap(data.getData());
			switch (image) {
			case 1:
				iv_other.setImageBitmap(bitmap);
				iv_other2.setVisibility(View.VISIBLE);
				badgeView1.show();
				break;
			case 2:
				iv_other2.setImageBitmap(bitmap);
				iv_other3.setVisibility(View.VISIBLE);
				badgeView2.show();
				break;
			case 3:
				lay_second.setVisibility(View.VISIBLE);
				iv_other3.setImageBitmap(bitmap);
				iv_other4.setVisibility(View.VISIBLE);
				badgeView3.show();
				break;
			case 4:
				iv_other4.setImageBitmap(bitmap);
				iv_other5.setVisibility(View.VISIBLE);
				badgeView4.show();
				break;
			case 5:
				iv_other5.setImageBitmap(bitmap);
				iv_other6.setVisibility(View.VISIBLE);
				badgeView5.show();
				break;
			case 6:
				lay_third.setVisibility(View.VISIBLE);
				iv_other6.setImageBitmap(bitmap);
				iv_other7.setVisibility(View.VISIBLE);
				badgeView6.show();
				break;
			case 7:
				iv_other7.setImageBitmap(bitmap);
				iv_other8.setVisibility(View.VISIBLE);
				badgeView7.show();
				break;
			case 8:
				iv_other8.setImageBitmap(bitmap);
				iv_other9.setVisibility(View.VISIBLE);
				badgeView8.show();
				break;
			case 9:
				iv_other9.setImageBitmap(bitmap);
				badgeView9.show();
				break;
			default:
				break;
			}
		}
		upload_image(bitmap);
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

	public void upload_image(Bitmap bitmap){
		
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
		dialog.dialog();
		request = Request.requestImg(photo_stream, "5", "1", "2", "2");
		new HttpThread(request, Customer_InfoeditAct.this);
	}
	
	public void startPhotoZoom(Uri uri, int i) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		// outputX, outputY 是裁剪图片宽高
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 400);
		intent.putExtra("outputY", 400);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
		intent.putExtra("noFaceDetection", true);
		startActivityForResult(intent, i);
	}

	@Override
	protected void onDestroy() {
		destoryImage();
		super.onDestroy();
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

	class Bt_imageview implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.iv_customerinfo_other1:
				image = 1;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_customerinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Customer_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_customerinfo_other2:
				image = 2;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_customerinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();

				params.alpha = 0.7f;
				Customer_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_customerinfo_other3:
				image = 3;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_customerinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Customer_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_customerinfo_other4:
				image = 4;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_customerinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Customer_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_customerinfo_other5:
				image = 5;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_customerinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Customer_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_customerinfo_other6:
				image = 6;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_customerinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Customer_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_customerinfo_other7:
				image = 7;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_customerinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Customer_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_customerinfo_other8:
				image = 8;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_customerinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Customer_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			case R.id.iv_customerinfo_other9:
				image = 9;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_customerinfo_phone, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				params.alpha = 0.7f;
				Customer_InfoeditAct.this.getWindow().setAttributes(params);
				break;
			default:
				break;
			}
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

	private void destoryImage() {
		if (photo != null) {
			photo.recycle();
			photo = null;
		}
	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					Customer_InfoeditAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	public void clearWindow() {
		WindowManager.LayoutParams params = Customer_InfoeditAct.this
				.getWindow().getAttributes();
		params.alpha = 1f;
		Customer_InfoeditAct.this.getWindow().setAttributes(params);
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
				Toast.makeText(Customer_InfoeditAct.this, "修改成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(Customer_InfoeditAct.this,
						Customer_DetailAct.class);
				intent.putExtra("id", customer_id);
				startActivity(intent);
				finish();
				break;
			case 2:
				Toast.makeText(Customer_InfoeditAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(Customer_InfoeditAct.this, "删除成功", Toast.LENGTH_SHORT).show();
				Intent intents = new Intent(Customer_InfoeditAct.this,
						Customer_ManAct.class);
				startActivity(intents);
				finish();
				break;
			case 4:
				Toast.makeText(Customer_InfoeditAct.this, "删除失败", Toast.LENGTH_SHORT).show();
				break;
			case 44:
				Toast.makeText(Customer_InfoeditAct.this, "图片上传失败", Toast.LENGTH_SHORT)
						.show();
				break;
			case 55:
				Toast.makeText(Customer_InfoeditAct.this, "图片上传成功", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4004:
				Toast.makeText(Customer_InfoeditAct.this, "图片类型错误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4005:
				Toast.makeText(Customer_InfoeditAct.this, "图片非法", Toast.LENGTH_SHORT).show();
				break;
			case 4028:
				Toast.makeText(Customer_InfoeditAct.this, "客户不存在", Toast.LENGTH_SHORT).show();
				break;
			case 4029:
				Toast.makeText(Customer_InfoeditAct.this, "注释", Toast.LENGTH_SHORT).show();
				break;
			case 4031:
				Toast.makeText(Customer_InfoeditAct.this, "交易类型有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4032:
				Toast.makeText(Customer_InfoeditAct.this, "首次到访日期有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4033:
				Toast.makeText(Customer_InfoeditAct.this, "客户名称有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4034:
				Toast.makeText(Customer_InfoeditAct.this, "行业类别有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4035:
				Toast.makeText(Customer_InfoeditAct.this, "意向面积有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4036:
				Toast.makeText(Customer_InfoeditAct.this, "意向房源有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4037:
				Toast.makeText(Customer_InfoeditAct.this, "价格预算有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4038:
				Toast.makeText(Customer_InfoeditAct.this, "价格预算单位有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4039:
				Toast.makeText(Customer_InfoeditAct.this, "房源简介有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4040:
				Toast.makeText(Customer_InfoeditAct.this, "联系电话有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4041:
				Toast.makeText(Customer_InfoeditAct.this, "公司名称有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4042:
				Toast.makeText(Customer_InfoeditAct.this, "公司职位有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4043:
				Toast.makeText(Customer_InfoeditAct.this, "租售动机有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4044:
				Toast.makeText(Customer_InfoeditAct.this, "单位性质有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4045:
				Toast.makeText(Customer_InfoeditAct.this, "员工数量有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4046:
				Toast.makeText(Customer_InfoeditAct.this, "目前所在写字楼有误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4047:
				Toast.makeText(Customer_InfoeditAct.this, "租约到期日有误", Toast.LENGTH_SHORT)
						.show();
				break;

			case 99:
				dialog.dismiss();
				Toast.makeText(Customer_InfoeditAct.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public void select_customeredit(String session_id, String id, String name,
			String transaction_type, String first_access_time, String sector,
			String pre_square_meter, String pre_room, String pre_price_1,
			String pre_price_unit_1, String intro, String process,
			String phone_number, String company, String job_title,
			String use_for, String company_property, String employee_total,
			String current_building, String expire_time, String images_id) {
		Request request = Request.requestcustomerinfoedit(session_id, id, name,
				transaction_type, first_access_time, sector, pre_square_meter,
				pre_room, pre_price_1, pre_price_unit_1, intro, process,
				phone_number, company, job_title, use_for, company_property,
				employee_total, current_building, expire_time, images_id);
		new HttpThread(request, this);
	}

	public void select_customerdelete(String id, String session_id) {
		Request request = Request.requestcustomerdelete(id, session_id);
		new HttpThread(request, this);
	}

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC_COLLECT) {
			dialog.dismiss();
			JSONObject json;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					newHandler.sendEmptyMessage(1);
				} else if (errorid.equals("1")) {
					newHandler.sendEmptyMessage(2);
				} else if (errorid.equals("4028")) {
					newHandler.sendEmptyMessage(4028);

				} else if (errorid.equals("4029")) {
					newHandler.sendEmptyMessage(4029);

				} else if (errorid.equals("4031")) {
					newHandler.sendEmptyMessage(4031);

				} else if (errorid.equals("4032")) {
					newHandler.sendEmptyMessage(4032);

				} else if (errorid.equals("4033")) {
					newHandler.sendEmptyMessage(4033);

				} else if (errorid.equals("4034")) {
					newHandler.sendEmptyMessage(4034);

				} else if (errorid.equals("4035")) {
					newHandler.sendEmptyMessage(4035);

				} else if (errorid.equals("4036")) {
					newHandler.sendEmptyMessage(4036);

				} else if (errorid.equals("4037")) {
					newHandler.sendEmptyMessage(4037);

				} else if (errorid.equals("4038")) {
					newHandler.sendEmptyMessage(4038);

				} else if (errorid.equals("4039")) {
					newHandler.sendEmptyMessage(4039);

				} else if (errorid.equals("4040")) {
					newHandler.sendEmptyMessage(4040);

				} else if (errorid.equals("4041")) {
					newHandler.sendEmptyMessage(4041);

				} else if (errorid.equals("4042")) {
					newHandler.sendEmptyMessage(4042);

				} else if (errorid.equals("4043")) {
					newHandler.sendEmptyMessage(4043);

				} else if (errorid.equals("4044")) {
					newHandler.sendEmptyMessage(4044);

				} else if (errorid.equals("4045")) {
					newHandler.sendEmptyMessage(4045);

				} else if (errorid.equals("4046")) {
					newHandler.sendEmptyMessage(4046);

				} else {
					newHandler.sendEmptyMessage(4047);

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (reqID == Request.REQUEST_ECONOMIC_COMPANY) {
			dialog.dismiss();
			JSONObject json;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					newHandler.sendEmptyMessage(3);
				} else if (errorid.equals("1")) {
					newHandler.sendEmptyMessage(4);
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
				String msg = BaseHelper.getString(json, "msg");
				if (errorid.equals("0")) {
					avatars_id = json.getString("id");
					avatars = json.getString("url");
					if (image == 1) {
						if (str_images_id.size() >= 1) {
							str_images_id.set(0, avatars_id);
						} else {
							str_images_id.add(avatars_id);
						}
					} else if (image == 2) {
						if (str_images_id.size() >= 2) {
							str_images_id.set(1, avatars_id);
						} else {
							str_images_id.add(avatars_id);
						}
					} else if (image == 3) {
						if (str_images_id.size() >= 3) {
							str_images_id.set(2, avatars_id);
						} else {
							str_images_id.add(avatars_id);
						}
					} else if (image == 4) {
						if (str_images_id.size() >= 4) {
							str_images_id.set(3, avatars_id);
						} else {
							str_images_id.add(avatars_id);
						}
					} else if (image == 5) {
						if (str_images_id.size() >= 5) {
							str_images_id.set(4, avatars_id);
						} else {
							str_images_id.add(avatars_id);
						}
					} else if (image == 6) {
						if (str_images_id.size() >= 6) {
							str_images_id.set(5, avatars_id);
						} else {
							str_images_id.add(avatars_id);
						}
					} else if (image == 7) {
						if (str_images_id.size() >= 7) {
							str_images_id.set(6, avatars_id);
						} else {
							str_images_id.add(avatars_id);
						}
					} else if (image == 8) {
						if (str_images_id.size() >= 8) {
							str_images_id.set(7, avatars_id);
						} else {
							str_images_id.add(avatars_id);
						}
					} else if (image == 9) {
						if (str_images_id.size() >= 9) {
							str_images_id.set(8, avatars_id);
						} else {
							str_images_id.add(avatars_id);
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
	public void doError(String s) {
		// TODO Auto-generated method stub
		if (s.equals("请求异常")) {
			newHandler.sendEmptyMessage(99);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Intent intent = new Intent(Customer_InfoeditAct.this,
					Customer_DetailAct.class);
			intent.putExtra("id", customer_id);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
