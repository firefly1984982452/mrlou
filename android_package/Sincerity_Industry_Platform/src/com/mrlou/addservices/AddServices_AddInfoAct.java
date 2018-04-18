package com.mrlou.addservices;

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

import com.mrlou.economic.view.BadgeView;
import com.mrlou.listener.HttpListener;
import com.mrlou.mine.Hourse_InfoeditAct;
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

public class AddServices_AddInfoAct extends Activity implements HttpListener {

	private LinearLayout lay_newadd_info, lay_newadd_info_back, lay_icon01,
			lay_icon02, lay_newadd_time, lay_service_add_second,
			lay_service_add_third;
	private TextView tv_newadd_time, tv_add_services;
	private ImageView iv_newadd_info01, iv_icon01, iv_icon02, iv_newadd_info02,
			iv_newadd_info03;
	private LinearLayout lay_newadd_info01, lay_newadd_info02,
			lay_newadd_info03, lay_add_services;
	private View view_pop;
	private Bitmap photo;
	private Button bt_pic, bt_camera, bt_cancel;
	private PopupWindow pop_image;
	private int CAMERA_RESULT = 100;
	private int RESULT_LOAD_IMAGE = 200;
	private int year, month, day;
	private String saveDir = Environment.getExternalStorageDirectory()
			.getPath() + "/temp_image";
	private File mPhotoFile;
	private Uri imageuri;
	private LinearLayout lay_newadd_commit;
	private EditText et_newadd_buildingname,
			et_newadd_industry, et_newadd_proof, et_newadd_customer_name;
	private String session_id,  photo_stream,
			avatars_id = "", add_v, avatarts_case_id = "",service_type;
	private SharedPreferences sharedPreferences;
	private SweetAlertDialog pDialog;
	private Progress_Dialog dialog;
	private int image;
	private ArrayList<String> str_images = new ArrayList<>();
	private ArrayList<String> str_case_id = new ArrayList<>();
	private BadgeView badgeView1, badgeView2, badgeView3, badgeView4,
			badgeView5, badgeView6, badgeView7, badgeView8, badgeView9,
			badgeView10, badgeView11, badgeView12;
	private PopupWindow popwindow;
	private View view;
	private LinearLayout lay_01, lay_02, lay_03, lay_04, lay_05, lay_06,
			lay_07, lay_08, lay_09, lay_10;
	private LinearLayout lay_case_01, lay_case_02, lay_case_03, lay_case_04,
			lay_case_05, lay_case_06, lay_case_07, lay_case_08, lay_case_09;
	private ImageView iv_case_01, iv_case_02, iv_case_03, iv_case_04,
			iv_case_05, iv_case_06, iv_case_07, iv_case_08, iv_case_09;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addservices_add_info);
		sharedPreferences = AddServices_AddInfoAct.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		Intent intent = getIntent();
		add_v = intent.getStringExtra("add_v");
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		// 弹出请求对话框
		pDialog = new SweetAlertDialog(AddServices_AddInfoAct.this,
				SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
		dialog = new Progress_Dialog(AddServices_AddInfoAct.this, pDialog);
		lay_newadd_commit = (LinearLayout) findViewById(R.id.lay_addservices_add_commit);
		lay_newadd_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 证明文件的Id
				for (int i = str_images.size() - 1; i >= 0; i--) {
					if (str_images.get(i).equals("1")) {
						str_images.remove(i);
					}
				}
				if (str_images.size() == 1) {
					avatars_id = str_images.get(0);
				} else if (str_images.size() > 1) {
					for (int i = 0; i < str_images.size(); i++) {
						if (i == 0) {
							avatars_id = str_images.get(0);
						} else {
							avatars_id = avatars_id + "," + str_images.get(i);
						}
					}
				} else {
					avatars_id = "";
				}

				// 案例图片的id
				for (int i = str_case_id.size() - 1; i >= 0; i--) {
					if (str_case_id.get(i).equals("1")) {
						str_case_id.remove(i);
					}
				}
				if (str_case_id.size() == 1) {
					avatarts_case_id = str_case_id.get(0);
				} else if (str_case_id.size() > 1) {
					for (int i = 0; i < str_case_id.size(); i++) {
						if (i == 0) {
							avatarts_case_id = str_case_id.get(0);
						} else {
							avatarts_case_id = avatarts_case_id + ","
									+ str_case_id.get(i);
						}
					}
				} else {
					avatarts_case_id = "";
				}

//				if (add_v.equals("1")) {
					if (!et_newadd_customer_name.getText().toString()
							.equals("")
							&& !et_newadd_buildingname.getText().toString()
									.equals("")
							&& !service_type
									.equals("")
							&& !et_newadd_industry.getText().toString()
									.equals("")
							&& !tv_newadd_time.getText().toString().equals("")) {
						if (avatars_id != ""
								|| !et_newadd_proof.getText().toString()
										.equals("")) {
							dialog.dialog();
							select_cjinfoadd(
									session_id,
									et_newadd_customer_name.getText()
											.toString(),
									et_newadd_buildingname.getText().toString(),
									service_type,
									tv_newadd_time.getText().toString(),
									et_newadd_industry.getText().toString(),
									et_newadd_proof.getText().toString(),
									avatars_id, avatarts_case_id);
						} else {
							Toast.makeText(AddServices_AddInfoAct.this,
									"证明人名字，电话未填写或者未上传图片", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(AddServices_AddInfoAct.this,
								"资料未填全，请填写完全再提交", Toast.LENGTH_SHORT).show();
					}
//				} else {
//					Toast.makeText(AddServices_AddInfoAct.this, "非认证经纪人无提交权限",
//							Toast.LENGTH_SHORT).show();
//				}
			}
		});
		et_newadd_buildingname = (EditText) findViewById(R.id.et_addservices_add_buildingname);
		view = LayoutInflater.from(AddServices_AddInfoAct.this).inflate(
				R.layout.idenfity_addedservices, null);
		tv_add_services = (TextView) findViewById(R.id.tv_addservices_add_services);
		lay_add_services = (LinearLayout) findViewById(R.id.lay_addservices_add_services);
		lay_add_services.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.setOutsideTouchable(true);
				popwindow.showAtLocation(lay_newadd_info_back, Gravity.RIGHT
						| Gravity.BOTTOM, 0, 0);
				popwindow.showAsDropDown(view);
				popwindow.update();
				WindowManager.LayoutParams params = AddServices_AddInfoAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				AddServices_AddInfoAct.this.getWindow().setAttributes(params);
			}
		});

		lay_01 = (LinearLayout) view.findViewById(R.id.lay_services01);
		lay_01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_add_services.setText("装修设计");
				service_type="1";
			}
		});
		lay_02 = (LinearLayout) view.findViewById(R.id.lay_services02);
		lay_02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_add_services.setText("办公家具");
				service_type="2";
			}
		});
		lay_03 = (LinearLayout) view.findViewById(R.id.lay_services03);
		lay_03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_add_services.setText("办公设备");
				service_type="3";
			}
		});
		lay_04 = (LinearLayout) view.findViewById(R.id.lay_services04);
		lay_04.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_add_services.setText("办公用品");
				service_type="4";
			}
		});
		lay_05 = (LinearLayout) view.findViewById(R.id.lay_services05);
		lay_05.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_add_services.setText("工商财税");
				service_type="5";
			}
		});
		lay_06 = (LinearLayout) view.findViewById(R.id.lay_services06);
		lay_06.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_add_services.setText("金融服务");
				service_type="6";
			}
		});
		lay_07 = (LinearLayout) view.findViewById(R.id.lay_services07);
		lay_07.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_add_services.setText("人力资源");
				service_type="7";
			}
		});
		lay_08 = (LinearLayout) view.findViewById(R.id.lay_services08);
		lay_08.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_add_services.setText("营销建站");
				service_type="8";
			}
		});
		lay_09 = (LinearLayout) view.findViewById(R.id.lay_services09);
		lay_09.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_add_services.setText("商务会展");
				service_type="9";
			}
		});
		lay_10 = (LinearLayout) view.findViewById(R.id.lay_services10);
		lay_10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popwindow.dismiss();
				tv_add_services.setText("电信网络");
				service_type="10";
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
		// pop_addmenu.setBackgroundDrawable(new BitmapDrawable());
		popwindow.setFocusable(true);

		et_newadd_industry = (EditText) findViewById(R.id.et_addservices_add_industry);
		et_newadd_proof = (EditText) findViewById(R.id.et_addservices_add_proof);
		et_newadd_customer_name = (EditText) findViewById(R.id.et_addservices_add_customername);
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		tv_newadd_time = (TextView) findViewById(R.id.tv_addservices_add_time);
		lay_newadd_time = (LinearLayout) findViewById(R.id.lay_addservices_add_time);
		lay_newadd_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatePickerDialog datePicker = new DatePickerDialog(
						AddServices_AddInfoAct.this, new OnDateSetListener() {
							@Override
							public void onDateSet(DatePicker view, int years,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								tv_newadd_time.setText(year + "-"
										+ (monthOfYear + 1) + "-" + dayOfMonth);
								year = years;
								month = monthOfYear;
								day = dayOfMonth;
							}
						}, year, month, day);
				datePicker.show();
			}
		});
		imageuri = Uri.fromFile(new File(saveDir + "temp4.png"));
		lay_newadd_info_back = (LinearLayout) findViewById(R.id.lay_addservices_add_info_back);
		lay_newadd_info_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AddServices_AddInfoAct.this,
						AddServices_HistoryAct.class);
				intent.putExtra("add_v", add_v);
				startActivity(intent);
				finish();
			}
		});
		lay_service_add_second = (LinearLayout) findViewById(R.id.lay_service_add_second);
		lay_service_add_third = (LinearLayout) findViewById(R.id.lay_service_add_third);
		lay_newadd_info = (LinearLayout) findViewById(R.id.lay_addservices_add_info);
		iv_newadd_info01 = (ImageView) findViewById(R.id.iv_addservices_add_info01);
		lay_newadd_info01 = (LinearLayout) findViewById(R.id.lay_addservices_add_info01);
		iv_newadd_info02 = (ImageView) findViewById(R.id.iv_addservices_add_info02);
		lay_newadd_info02 = (LinearLayout) findViewById(R.id.lay_addservices_add_info02);
		iv_newadd_info03 = (ImageView) findViewById(R.id.iv_addservices_add_info03);
		lay_newadd_info03 = (LinearLayout) findViewById(R.id.lay_addservices_add_info03);
		badgeView1 = new BadgeView(AddServices_AddInfoAct.this,
				lay_newadd_info01);
		badgeView1.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView1.setBackgroundResource(R.drawable.fangyuan_images_del_img);
		badgeView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_newadd_info01
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images.set(0, "1");
				badgeView1.hide();
			}
		});
		badgeView2 = new BadgeView(AddServices_AddInfoAct.this,
				lay_newadd_info02);
		badgeView2.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView2.setBackgroundResource(R.drawable.fangyuan_images_del_img);
		badgeView2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_newadd_info02
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images.set(1, "1");
				badgeView2.hide();
			}
		});
		badgeView3 = new BadgeView(AddServices_AddInfoAct.this,
				lay_newadd_info03);
		badgeView3.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView3.setBackgroundResource(R.drawable.fangyuan_images_del_img);
		badgeView3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_newadd_info03
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images.set(2, "1");
				badgeView3.hide();
			}
		});

		lay_case_01 = (LinearLayout) findViewById(R.id.lay_addservices_add_example01);
		iv_case_01 = (ImageView) findViewById(R.id.iv_addservices_add_example01);
		badgeView4 = new BadgeView(AddServices_AddInfoAct.this, lay_case_01);
		badgeView4.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView4.setBackgroundResource(R.drawable.fangyuan_images_del_img);

		badgeView4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_case_01
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_case_id.set(0, "1");
				badgeView4.hide();
			}
		});
		lay_case_02 = (LinearLayout) findViewById(R.id.lay_addservices_add_example02);
		iv_case_02 = (ImageView) findViewById(R.id.iv_addservices_add_example02);
		badgeView5 = new BadgeView(AddServices_AddInfoAct.this, lay_case_02);
		badgeView5.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView5.setBackgroundResource(R.drawable.fangyuan_images_del_img);

		badgeView5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_case_02
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_case_id.set(1, "1");
				badgeView5.hide();
			}
		});
		lay_case_03 = (LinearLayout) findViewById(R.id.lay_addservices_add_example03);
		iv_case_03 = (ImageView) findViewById(R.id.iv_addservices_add_example03);
		badgeView6 = new BadgeView(AddServices_AddInfoAct.this, lay_case_03);
		badgeView6.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView6.setBackgroundResource(R.drawable.fangyuan_images_del_img);

		badgeView6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_case_03
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_case_id.set(2, "1");
				badgeView6.hide();
			}
		});
		lay_case_04 = (LinearLayout) findViewById(R.id.lay_addservices_add_example04);
		iv_case_04 = (ImageView) findViewById(R.id.iv_addservices_add_example04);
		badgeView7 = new BadgeView(AddServices_AddInfoAct.this, lay_case_04);
		badgeView7.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView7.setBackgroundResource(R.drawable.fangyuan_images_del_img);

		badgeView7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_case_04
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_case_id.set(3, "1");
				badgeView6.hide();
			}
		});
		lay_case_05 = (LinearLayout) findViewById(R.id.lay_addservices_add_example05);
		iv_case_05 = (ImageView) findViewById(R.id.iv_addservices_add_example05);
		badgeView8 = new BadgeView(AddServices_AddInfoAct.this, lay_case_05);
		badgeView8.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView8.setBackgroundResource(R.drawable.fangyuan_images_del_img);

		badgeView8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_case_05
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_case_id.set(4, "1");
				badgeView8.hide();
			}
		});
		lay_case_06 = (LinearLayout) findViewById(R.id.lay_addservices_add_example06);
		iv_case_06 = (ImageView) findViewById(R.id.iv_addservices_add_example06);
		badgeView9 = new BadgeView(AddServices_AddInfoAct.this, lay_case_06);
		badgeView9.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView9.setBackgroundResource(R.drawable.fangyuan_images_del_img);

		badgeView9.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_case_06
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_case_id.set(5, "1");
				badgeView9.hide();
			}
		});
		lay_case_07 = (LinearLayout) findViewById(R.id.lay_addservices_add_example07);
		iv_case_07 = (ImageView) findViewById(R.id.iv_addservices_add_example07);
		badgeView10 = new BadgeView(AddServices_AddInfoAct.this, lay_case_07);
		badgeView10.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView10.setBackgroundResource(R.drawable.fangyuan_images_del_img);

		badgeView10.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_case_07
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_case_id.set(6, "1");
				badgeView10.hide();
			}
		});
		lay_case_08 = (LinearLayout) findViewById(R.id.lay_addservices_add_example08);
		iv_case_08 = (ImageView) findViewById(R.id.iv_addservices_add_example08);
		badgeView11 = new BadgeView(AddServices_AddInfoAct.this, lay_case_08);
		badgeView11.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView11.setBackgroundResource(R.drawable.fangyuan_images_del_img);

		badgeView11.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_case_08
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_case_id.set(7, "1");
				badgeView11.hide();
			}
		});
		lay_case_09 = (LinearLayout) findViewById(R.id.lay_addservices_add_example09);
		iv_case_09 = (ImageView) findViewById(R.id.iv_addservices_add_example09);
		badgeView12 = new BadgeView(AddServices_AddInfoAct.this, lay_case_09);
		badgeView12.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView12.setBackgroundResource(R.drawable.fangyuan_images_del_img);

		badgeView12.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_case_09
						.setImageResource(R.drawable.fangyuan_images_no_content);
				str_case_id.set(8, "1");
				badgeView12.hide();
			}
		});

		iv_newadd_info01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 1;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_newadd_info, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = AddServices_AddInfoAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				AddServices_AddInfoAct.this.getWindow().setAttributes(params);
			}
		});
		iv_newadd_info02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 2;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_newadd_info, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = AddServices_AddInfoAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				AddServices_AddInfoAct.this.getWindow().setAttributes(params);
			}
		});
		iv_newadd_info03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 3;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_newadd_info, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = AddServices_AddInfoAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				AddServices_AddInfoAct.this.getWindow().setAttributes(params);
			}
		});
		iv_case_01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 4;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_newadd_info, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = AddServices_AddInfoAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				AddServices_AddInfoAct.this.getWindow().setAttributes(params);
			}
		});
		iv_case_02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 5;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_newadd_info, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = AddServices_AddInfoAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				AddServices_AddInfoAct.this.getWindow().setAttributes(params);
			}
		});
		iv_case_03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 6;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_newadd_info, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = AddServices_AddInfoAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				AddServices_AddInfoAct.this.getWindow().setAttributes(params);
			}
		});
		iv_case_04.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 7;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_newadd_info, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = AddServices_AddInfoAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				AddServices_AddInfoAct.this.getWindow().setAttributes(params);
			}
		});
		iv_case_05.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 8;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_newadd_info, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = AddServices_AddInfoAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				AddServices_AddInfoAct.this.getWindow().setAttributes(params);
			}
		});
		iv_case_06.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 9;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_newadd_info, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = AddServices_AddInfoAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				AddServices_AddInfoAct.this.getWindow().setAttributes(params);
			}
		});
		iv_case_07.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 10;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_newadd_info, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = AddServices_AddInfoAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				AddServices_AddInfoAct.this.getWindow().setAttributes(params);
			}
		});
		iv_case_08.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 11;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_newadd_info, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = AddServices_AddInfoAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				AddServices_AddInfoAct.this.getWindow().setAttributes(params);
			}
		});
		iv_case_09.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 12;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_newadd_info, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = AddServices_AddInfoAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				AddServices_AddInfoAct.this.getWindow().setAttributes(params);
			}
		});
		view_pop = LayoutInflater.from(AddServices_AddInfoAct.this).inflate(
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bitmap bitmap = null;
		if (requestCode == CAMERA_RESULT && resultCode == RESULT_OK) {
			if (mPhotoFile != null && mPhotoFile.exists()) {
				bitmap = decodeUriAsBitmap(Uri.fromFile(mPhotoFile));
				switch (image) {
				case 1:
					iv_newadd_info01.setImageBitmap(bitmap);
					iv_newadd_info02.setVisibility(View.VISIBLE);
					badgeView1.show();
					break;
				case 2:
					iv_newadd_info02.setImageBitmap(bitmap);
					iv_newadd_info03.setVisibility(View.VISIBLE);
					badgeView2.show();
					break;
				case 3:
					iv_newadd_info03.setImageBitmap(bitmap);
					badgeView3.show();
					break;
				case 4:
					iv_case_01.setImageBitmap(bitmap);
					iv_case_02.setVisibility(View.VISIBLE);
					badgeView4.show();
					break;
				case 5:
					iv_case_02.setImageBitmap(bitmap);
					iv_case_03.setVisibility(View.VISIBLE);
					badgeView5.show();
					break;
				case 6:
					lay_service_add_second.setVisibility(View.VISIBLE);
					iv_case_03.setImageBitmap(bitmap);
					iv_case_04.setVisibility(View.VISIBLE);
					badgeView6.show();
					break;
				case 7:
					iv_case_04.setImageBitmap(bitmap);
					iv_case_05.setVisibility(View.VISIBLE);
					badgeView7.show();
					break;
				case 8:
					iv_case_05.setImageBitmap(bitmap);
					iv_case_06.setVisibility(View.VISIBLE);
					badgeView8.show();
					break;
				case 9:
					lay_service_add_third.setVisibility(View.VISIBLE);
					iv_case_06.setImageBitmap(bitmap);
					iv_case_07.setVisibility(View.VISIBLE);
					badgeView9.show();
					break;
				case 10:
					iv_case_07.setImageBitmap(bitmap);
					iv_case_08.setVisibility(View.VISIBLE);
					badgeView10.show();
					break;
				case 11:
					iv_case_08.setImageBitmap(bitmap);
					iv_case_09.setVisibility(View.VISIBLE);
					badgeView11.show();
					break;
				case 12:
					iv_case_09.setImageBitmap(bitmap);
					badgeView12.show();
					break;
				}
			}
			upload_image(bitmap);
		}
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			bitmap = decodeUriAsBitmap(data.getData());
			switch (image) {
			case 1:
				iv_newadd_info01.setImageBitmap(bitmap);
				iv_newadd_info02.setVisibility(View.VISIBLE);
				badgeView1.show();
				break;

			case 2:
				iv_newadd_info02.setImageBitmap(bitmap);
				iv_newadd_info03.setVisibility(View.VISIBLE);
				badgeView2.show();
				break;

			case 3:
				iv_newadd_info03.setImageBitmap(bitmap);
				badgeView3.show();
				break;
			case 4:
				iv_case_01.setImageBitmap(bitmap);
				iv_case_02.setVisibility(View.VISIBLE);
				badgeView4.show();
				break;
			case 5:
				iv_case_02.setImageBitmap(bitmap);
				iv_case_03.setVisibility(View.VISIBLE);
				badgeView5.show();
				break;
			case 6:
				lay_service_add_second.setVisibility(View.VISIBLE);
				iv_case_03.setImageBitmap(bitmap);
				iv_case_04.setVisibility(View.VISIBLE);
				badgeView6.show();
				break;
			case 7:
				iv_case_04.setImageBitmap(bitmap);
				iv_case_05.setVisibility(View.VISIBLE);
				badgeView7.show();
				break;
			case 8:
				iv_case_05.setImageBitmap(bitmap);
				iv_case_06.setVisibility(View.VISIBLE);
				badgeView8.show();
				break;
			case 9:
				lay_service_add_third.setVisibility(View.VISIBLE);
				iv_case_06.setImageBitmap(bitmap);
				iv_case_07.setVisibility(View.VISIBLE);
				badgeView9.show();
				break;
			case 10:
				iv_case_07.setImageBitmap(bitmap);
				iv_case_08.setVisibility(View.VISIBLE);
				badgeView10.show();
				break;
			case 11:
				iv_case_08.setImageBitmap(bitmap);
				iv_case_09.setVisibility(View.VISIBLE);
				badgeView11.show();
				break;
			case 12:
				iv_case_09.setImageBitmap(bitmap);
				badgeView12.show();
				break;
			}
			upload_image(bitmap);
		}

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
		Request request;
		if (image < 4) {
			request = Request.requestImg(photo_stream, "9", "1", "2", "2");
		} else {
			request = Request.requestImg(photo_stream, "10", "1", "2", "2");
		}
		new HttpThread(request, AddServices_AddInfoAct.this);
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
					AddServices_AddInfoAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	public void clearWindow() {
		WindowManager.LayoutParams params = AddServices_AddInfoAct.this
				.getWindow().getAttributes();
		params.alpha = 1f;
		AddServices_AddInfoAct.this.getWindow().setAttributes(params);
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
				Toast.makeText(AddServices_AddInfoAct.this, "添加成功", Toast.LENGTH_SHORT)
						.show();
				Intent intent = new Intent(AddServices_AddInfoAct.this,
						AddServices_HistoryAct.class);
				intent.putExtra("add_v", add_v);
				startActivity(intent);
				finish();
				break;
			case 2:
				Toast.makeText(AddServices_AddInfoAct.this, "暂无数据", Toast.LENGTH_SHORT)
						.show();
				break;
			case 44:
				Toast.makeText(AddServices_AddInfoAct.this, "图片上传失败", Toast.LENGTH_SHORT)
						.show();
				if (image == 1) {
					iv_newadd_info01
							.setImageResource(R.drawable.fangyuan_images_no_content);
				} else if (image == 2) {
					iv_newadd_info02
							.setImageResource(R.drawable.fangyuan_images_no_content);
				} else if (image == 3) {
					iv_newadd_info03
							.setImageResource(R.drawable.fangyuan_images_no_content);
				} else if (image == 4) {
					iv_case_01
							.setImageResource(R.drawable.fangyuan_images_no_content);
				} else if (image == 5) {
					iv_case_02
							.setImageResource(R.drawable.fangyuan_images_no_content);
				} else if (image == 6) {
					iv_case_03
							.setImageResource(R.drawable.fangyuan_images_no_content);
				} else if (image == 7) {
					iv_case_04
							.setImageResource(R.drawable.fangyuan_images_no_content);
				} else if (image == 8) {
					iv_case_05
							.setImageResource(R.drawable.fangyuan_images_no_content);
				} else if (image == 9) {
					iv_case_06
							.setImageResource(R.drawable.fangyuan_images_no_content);
				} else if (image == 10) {
					iv_case_07
							.setImageResource(R.drawable.fangyuan_images_no_content);
				} else if (image == 11) {
					iv_case_08
							.setImageResource(R.drawable.fangyuan_images_no_content);
				} else if (image == 12) {
					iv_case_09
							.setImageResource(R.drawable.fangyuan_images_no_content);
				}

				break;
			case 55:
				Toast.makeText(AddServices_AddInfoAct.this, "图片上传成功", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4001:
				Toast.makeText(AddServices_AddInfoAct.this, "参数不可为空", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4014:
				Toast.makeText(AddServices_AddInfoAct.this, "请选填姓名电话或者证明文件",
						Toast.LENGTH_SHORT).show();
				break;
			case 4015:
				Toast.makeText(AddServices_AddInfoAct.this, "姓名电话或者证明文件未填写",
						Toast.LENGTH_SHORT).show();
				break;
			case 4012:
				Toast.makeText(AddServices_AddInfoAct.this, "没有找到成交记录", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4004:
				Toast.makeText(AddServices_AddInfoAct.this, "图片类型错误", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4005:
				Toast.makeText(AddServices_AddInfoAct.this, "图片非法", Toast.LENGTH_SHORT)
						.show();
				break;
			case 99:
				dialog.dismiss();
				Toast.makeText(AddServices_AddInfoAct.this, "网络异常，请重新尝试连接",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	public void select_cjinfoadd(String session_id, String customer_name,
			String building_name, String service_type, String cj_time,
			String industry, String proof, String proof_id, String case_id) {
		Request request = Request.request_addservice_cjinfo(session_id,
				customer_name, building_name, service_type, cj_time, industry,
				proof, proof_id, case_id);
		new HttpThread(request, this);
	}

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC) {
			dialog.dismiss();
			JSONObject json;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					newHandler.sendEmptyMessage(1);
				} else if (errorid.equals("1")) {
					newHandler.sendEmptyMessage(2);
				} else if (errorid.equals("4001")) {
					newHandler.sendEmptyMessage(4001);
				} else if (errorid.equals("4014")) {
					newHandler.sendEmptyMessage(4014);
				} else if (errorid.equals("4015")) {
					newHandler.sendEmptyMessage(4015);
				} else if (errorid.equals("4012")) {
					newHandler.sendEmptyMessage(4012);
				} else if (errorid.equals("4004")) {
					newHandler.sendEmptyMessage(4015);
				} else if (errorid.equals("4005")) {
					newHandler.sendEmptyMessage(4012);
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
					if (image < 4) {
						str_images.add(json.getString("id"));
					} else {
						str_case_id.add(json.getString("id"));
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
			Intent intent = new Intent(AddServices_AddInfoAct.this,
					AddServices_HistoryAct.class);
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
