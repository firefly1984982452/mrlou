package com.mrlou.mine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Instrumentation;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mrlou.activity.MainFragmentActivity;
import com.mrlou.listener.HttpListener;
import com.mrlou.mine.Setting_personal02.poponDismissListener;
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpGetThread;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.mrlou.util.User_info;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author jx_chen
 * @date 2016-10-20---下午5:36:39
 * @problem
 */
public class Elite_meetingAct02 extends Activity implements HttpListener {

	private EditText et_c_name, et_e_name, et_mail, et_company;
	private LinearLayout lay_member_click, lay_elite_member, lay_member01,
			lay_member02, lay_member03, lay_time_click, lay_time_begin,
			lay_time_end, lay_elite_time, lay_elite02_back;
	private TextView tv_member, tv_time_begin, tv_time_end, tv_elite02_name,
			tv_elite_time_type01, tv_elite_time_type02, tv_elite_time_type03,
			tv_elite_identify;
	private int year, month, day;
	private String email, en_username, cn_username, company, session_id,
			user_id, member_type = "", type, user_type, post_card;
	private SharedPreferences sharedPreferences;
	private Button bt_elite_apply;
	private boolean member_flag = true, time_flag = true, time_begins = false,
			time_ends = false;
	private LinearLayout lay_elite_time_type01, lay_elite_time_type02,
			lay_elite_time_type03;
	private int data = 3;
	private ImageView iv_elite_time_type, iv_elite_member_type;
	private Calendar c;
	private DateFormat df;
	private Date date = null;
	private ImageView iv_elite_post;
	DisplayImageOptions options;
	private PopupWindow pop_image;
	private View view_pop;
	private Button bt_pic, bt_camera, bt_cancel;
	private Uri imageuri;
	private String saveDir = Environment.getExternalStorageDirectory()
			.getPath() + "/temp_image";
	private int CAMERA_RESULT = 100;
	private int RESULT_LOAD_IMAGE = 200;
	private File mPhotoFile;
	private Bitmap photo;
	private int image;
	private String photo_stream, post_card_id;
	private SweetAlertDialog pDialog;
	private Progress_Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_elite_meeting02);
		sharedPreferences = Elite_meetingAct02.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		user_id = sharedPreferences.getString("user_id", "");
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.fangyuan_images_no_content) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.fangyuan_images_no_content) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.fangyuan_images_no_content) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build();
		pDialog = new SweetAlertDialog(Elite_meetingAct02.this,
				SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
		dialog = new Progress_Dialog(Elite_meetingAct02.this, pDialog);
		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		select_perinfo(session_id, user_id);
	}

	private void findView() {
		// TODO Auto-generated method stub
		df = new SimpleDateFormat("yyyy-MM-dd");
		iv_elite_time_type = (ImageView) findViewById(R.id.iv_elite_time_type);
		iv_elite_member_type = (ImageView) findViewById(R.id.iv_elite_member_type);
		tv_elite_time_type01 = (TextView) findViewById(R.id.tv_elite_time_type01);
		tv_elite_time_type02 = (TextView) findViewById(R.id.tv_elite_time_type02);
		tv_elite_time_type03 = (TextView) findViewById(R.id.tv_elite_time_type03);
		lay_elite_time_type01 = (LinearLayout) findViewById(R.id.lay_elite_time_type01);
		lay_elite_time_type01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				data = 3;
				lay_elite_time_type01
						.setBackgroundResource(R.drawable.corner_view_red_2dp);
				lay_elite_time_type02
						.setBackgroundResource(R.drawable.corner_lightgray_2dp);
				lay_elite_time_type03
						.setBackgroundResource(R.drawable.corner_lightgray_2dp);
				tv_elite_time_type01.setTextColor(Elite_meetingAct02.this
						.getResources().getColor(R.color.white));
				tv_elite_time_type02.setTextColor(Elite_meetingAct02.this
						.getResources().getColor(R.color.black));
				tv_elite_time_type03.setTextColor(Elite_meetingAct02.this
						.getResources().getColor(R.color.black));
				if (time_begins) {
					c.setTime(date);
					c.add(Calendar.MONTH, 3);
					c.add(Calendar.DAY_OF_MONTH, -1);
					tv_time_end.setText(df.format(c.getTime()));
				}
			}
		});
		lay_elite_time_type02 = (LinearLayout) findViewById(R.id.lay_elite_time_type02);
		lay_elite_time_type02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				data = 6;
				lay_elite_time_type01
						.setBackgroundResource(R.drawable.corner_lightgray_2dp);
				lay_elite_time_type02
						.setBackgroundResource(R.drawable.corner_view_red_2dp);
				lay_elite_time_type03
						.setBackgroundResource(R.drawable.corner_lightgray_2dp);
				tv_elite_time_type01.setTextColor(Elite_meetingAct02.this
						.getResources().getColor(R.color.black));
				tv_elite_time_type02.setTextColor(Elite_meetingAct02.this
						.getResources().getColor(R.color.white));
				tv_elite_time_type03.setTextColor(Elite_meetingAct02.this
						.getResources().getColor(R.color.black));
				if (time_begins) {
					c.setTime(date);
					c.add(Calendar.MONTH, 6);
					c.add(Calendar.DAY_OF_MONTH, -1);
					tv_time_end.setText(df.format(c.getTime()));
				}
			}
		});
		lay_elite_time_type03 = (LinearLayout) findViewById(R.id.lay_elite_time_type03);
		lay_elite_time_type03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				data = 12;
				lay_elite_time_type01
						.setBackgroundResource(R.drawable.corner_lightgray_2dp);
				lay_elite_time_type02
						.setBackgroundResource(R.drawable.corner_lightgray_2dp);
				lay_elite_time_type03
						.setBackgroundResource(R.drawable.corner_view_red_2dp);
				tv_elite_time_type01.setTextColor(Elite_meetingAct02.this
						.getResources().getColor(R.color.black));
				tv_elite_time_type02.setTextColor(Elite_meetingAct02.this
						.getResources().getColor(R.color.black));
				tv_elite_time_type03.setTextColor(Elite_meetingAct02.this
						.getResources().getColor(R.color.white));
				if (time_begins) {
					c.setTime(date);
					c.add(Calendar.YEAR, 1);
					c.add(Calendar.DAY_OF_YEAR, -1);
					tv_time_end.setText(df.format(c.getTime()));
				}
			}
		});
		lay_elite_time_type01
				.setBackgroundResource(R.drawable.corner_view_red_2dp);
		lay_elite_time_type02
				.setBackgroundResource(R.drawable.corner_lightgray_2dp);
		lay_elite_time_type03
				.setBackgroundResource(R.drawable.corner_lightgray_2dp);
		tv_elite_time_type01.setTextColor(Elite_meetingAct02.this
				.getResources().getColor(R.color.white));
		tv_elite_time_type02.setTextColor(Elite_meetingAct02.this
				.getResources().getColor(R.color.black));
		tv_elite_time_type03.setTextColor(Elite_meetingAct02.this
				.getResources().getColor(R.color.black));
		lay_elite02_back = (LinearLayout) findViewById(R.id.lay_elite02_back);
		lay_elite02_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		tv_elite02_name = (TextView) findViewById(R.id.tv_elite02_name);
		tv_elite02_name.setText(type);
		et_c_name = (EditText) findViewById(R.id.et_elite_c_name);
		et_c_name.setText(cn_username);
		et_c_name.setFocusable(false);
		et_mail = (EditText) findViewById(R.id.et_elite_email);
		et_mail.setText(email);
		et_mail.setFocusable(false);
		et_e_name = (EditText) findViewById(R.id.et_elite_e_name);
		et_e_name.setText(en_username);
		et_e_name.setFocusable(false);
		et_company = (EditText) findViewById(R.id.et_elite_company);
		et_company.setText(company);
		et_company.setFocusable(false);
		tv_elite_identify = (TextView) findViewById(R.id.tv_elite_identifys);
		if (user_type.equals("299")) {
			tv_elite_identify.setText("其他");
		} else if (user_type.equals("1")) {
			tv_elite_identify.setText("开发商");
		} else if (user_type.equals("2")) {
			tv_elite_identify.setText("代理商");
		} else if (user_type.equals("3")) {
			tv_elite_identify.setText("运营商");
		} else if (user_type.equals("102")) {
			tv_elite_identify.setText("经纪人");
		} else if (user_type.equals("103")) {
			tv_elite_identify.setText("物业公司");
		} else if (user_type.equals("201")) {
			tv_elite_identify.setText("小业主");
		} else if (user_type.equals("202")) {
			tv_elite_identify.setText("代理人");
		} else if (user_type.equals("104")) {
			tv_elite_identify.setText("企业服务商");
		} else {
			tv_elite_identify.setText("转租人");
		}
		iv_elite_post = (ImageView) findViewById(R.id.iv_elite_post);
		if (!post_card.equals("")) {
			ImageLoader.getInstance().displayImage(post_card, iv_elite_post,
					options);
		}else {
			iv_elite_post.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pop_image.setOutsideTouchable(true);
					pop_image.showAtLocation(lay_elite_member, Gravity.BOTTOM
							| Gravity.CENTER, 0, 0);
					pop_image.showAsDropDown(view_pop);
					pop_image.update();
					WindowManager.LayoutParams params = Elite_meetingAct02.this
							.getWindow().getAttributes();
					params.alpha = 0.7f;
					Elite_meetingAct02.this.getWindow().setAttributes(params);
				}
			});
		}
		
		

		view_pop = LayoutInflater.from(Elite_meetingAct02.this).inflate(
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
		imageuri = Uri.fromFile(new File(saveDir + "temp2.png"));
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

		lay_member_click = (LinearLayout) findViewById(R.id.lay_elite_member_click);
		lay_member_click.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (member_flag) {
					lay_elite_member.setVisibility(View.VISIBLE);
					iv_elite_member_type
							.setBackgroundResource(R.drawable.btn_list_arrow_right_down);
					member_flag = false;
				} else {
					lay_elite_member.setVisibility(View.GONE);
					iv_elite_member_type
							.setBackgroundResource(R.drawable.btn_list_arrow_right_up);
					member_flag = true;
				}

			}
		});
		lay_elite_member = (LinearLayout) findViewById(R.id.lay_elite_member);
		lay_elite_member.setVisibility(View.GONE);
		lay_member01 = (LinearLayout) findViewById(R.id.lay_elite_member01);
		lay_member01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				member_flag = true;
				lay_elite_member.setVisibility(View.GONE);
				tv_member.setText("会员类型： 普通会员");
				iv_elite_member_type
						.setBackgroundResource(R.drawable.btn_list_arrow_right_up);
				member_type = "1";
			}
		});
		lay_member02 = (LinearLayout) findViewById(R.id.lay_elite_member02);
		lay_member02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				member_flag = true;
				lay_elite_member.setVisibility(View.GONE);
				tv_member.setText("会员类型： 白银会员");
				iv_elite_member_type
						.setBackgroundResource(R.drawable.btn_list_arrow_right_up);
				member_type = "2";
			}
		});
		lay_member03 = (LinearLayout) findViewById(R.id.lay_elite_member03);
		lay_member03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				member_flag = true;
				lay_elite_member.setVisibility(View.GONE);
				iv_elite_member_type
						.setBackgroundResource(R.drawable.btn_list_arrow_right_up);
				tv_member.setText("会员类型： 白金会员");
				member_type = "3";
			}
		});
		lay_time_click = (LinearLayout) findViewById(R.id.lay_elite_time_click);
		lay_time_click.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (time_flag) {
					lay_elite_time.setVisibility(View.VISIBLE);
					iv_elite_time_type
							.setBackgroundResource(R.drawable.btn_list_arrow_right_down);
					time_flag = false;
				} else {
					lay_elite_time.setVisibility(View.GONE);
					iv_elite_time_type
							.setBackgroundResource(R.drawable.btn_list_arrow_right_up);
					time_flag = true;
				}

			}
		});
		c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		lay_elite_time = (LinearLayout) findViewById(R.id.lay_elite_time);
		lay_elite_time.setVisibility(View.GONE);
		lay_time_begin = (LinearLayout) findViewById(R.id.lay_elite_time_begin);
		lay_time_begin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatePickerDialog datePicker = new DatePickerDialog(
						Elite_meetingAct02.this, new OnDateSetListener() {

							@Override
							public void onDateSet(DatePicker view, int years,
									int monthOfYear, int dayOfMonth) {
								// TODO Auto-generated method stub
								tv_time_begin.setText(year + "-" + (month + 1)
										+ "-" + day);
								year = years;
								month = monthOfYear;
								day = dayOfMonth;
								// Date date=new Date(year, month, day);
								try {
									date = df.parse(year + "-" + (month + 1)
											+ "-" + day);
								} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								c.setTime(date);
								time_begins = true;
								if (data == 3) {
									c.add(Calendar.MONTH, 3);
									c.add(Calendar.DAY_OF_MONTH, -1);
									tv_time_end.setText(df.format(c.getTime()));
								} else if (data == 6) {
									c.add(Calendar.MONTH, 6);
									c.add(Calendar.DAY_OF_MONTH, -1);
									tv_time_end.setText(df.format(c.getTime()));
								} else {
									c.add(Calendar.YEAR, 1);
									c.add(Calendar.DAY_OF_YEAR, -1);
									tv_time_end.setText(df.format(c.getTime()));
								}
							}
						}, year, month, day);
				datePicker.show();
			}
		});
		lay_time_end = (LinearLayout) findViewById(R.id.lay_elite_time_end);
		tv_member = (TextView) findViewById(R.id.tv_elite_member_type);
		tv_time_begin = (TextView) findViewById(R.id.tv_elite_time_begin);
		tv_time_end = (TextView) findViewById(R.id.tv_elite_time_end);
		bt_elite_apply = (Button) findViewById(R.id.bt_elite_apply);
		bt_elite_apply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!member_type.equals("") && time_begins&&!post_card_id.equals("")) {
					select_cancelcolect(session_id, et_c_name.getText()
							.toString(), et_e_name.getText().toString(),
							et_mail.getText().toString(), et_company.getText()
									.toString(), member_type, tv_time_begin
									.getText().toString(), tv_time_end
									.getText().toString());
				} else {
					Toast.makeText(Elite_meetingAct02.this, "信息未填全", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	public void select_cancelcolect(String session_id, String cn_name,
			String en_name, String email, String company, String type,
			String start_time, String end_time) {
		Request request = Request.request_insertapply(session_id, cn_name,
				en_name, email, company, type, start_time, end_time,post_card_id);
		new HttpThread(request, this);
	}

	public void select_perinfo(String session_id, String user_id) {
		Request request = Request.requestgetperinfo(session_id, user_id);
		new HttpGetThread(request, this);
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
				Toast.makeText(Elite_meetingAct02.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(Elite_meetingAct02.this, "提交成功，稍后会有客服人员与您联系",
						Toast.LENGTH_SHORT).show();
				simulateKey(KeyEvent.KEYCODE_BACK);
				break;
			case 44:
				Toast.makeText(Elite_meetingAct02.this, "图片上传失败", Toast.LENGTH_SHORT).show();
				iv_elite_post
						.setImageResource(R.drawable.fangyuan_images_no_content);
				break;
			case 55:
				Toast.makeText(Elite_meetingAct02.this, "图片上传成功", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(Elite_meetingAct02.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_NEW) {
			JSONObject json;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					newHandler.sendEmptyMessage(3);
				} else if (errorid.equals("1")) {
					newHandler.sendEmptyMessage(2);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (reqID == Request.REQUEST_ECONOMIC_COLLECT) {
			JSONObject json, json2;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					json2 = json.getJSONObject("user");
					email = json2.getString("email");
					en_username = json2.getString("en_username");
					cn_username = json2.getString("cn_username");
					user_type = json2.getString("type");
					company = json2.getString("company");
					post_card = json2.getString("post_card");
					post_card_id = json2.getString("post_card_id");
					newHandler.sendEmptyMessage(1);
				} else {
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
				String msg = BaseHelper.getString(json, "msg");
				if (errorid.equals("0")) {
					if (image == 1) {
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

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					Elite_meetingAct02.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bitmap bitmap = null;
		if (requestCode == CAMERA_RESULT && resultCode == RESULT_OK) {
			if (mPhotoFile != null && mPhotoFile.exists()) {
				bitmap = decodeUriAsBitmap(Uri.fromFile(mPhotoFile));
				dialog.dialog();
				iv_elite_post.setImageBitmap(bitmap);
				upload_image(bitmap);
			}

		}
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			bitmap = decodeUriAsBitmap(data.getData());
			dialog.dialog();
			iv_elite_post.setImageBitmap(bitmap);
			upload_image(bitmap);
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

	public void upload_image(Bitmap bitmap) {
		// dialog.dialog();
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
		Request request = Request.requestImg(photo_stream, "6", "1",
				"2", "2");
		new HttpThread(request, Elite_meetingAct02.this);
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

	/**
	 * 获取指定日后 后 dayAddNum 天的 日期
	 * 
	 * @param day
	 *            日期，格式为String："2013-9-3";
	 * @param dayAddNum
	 *            增加天数 格式为int;
	 * @return
	 */
	public static String getDateStr(String day, int dayAddNum) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date nowDate = null;
		try {
			nowDate = df.parse(day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date newDate2 = new Date(nowDate.getTime() + dayAddNum * 24 * 60 * 60
				* Toast.LENGTH_SHORT);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateOk = simpleDateFormat.format(newDate2);
		return dateOk;
	}

	public void clearWindow() {
		WindowManager.LayoutParams params = Elite_meetingAct02.this.getWindow()
				.getAttributes();
		params.alpha = 1f;
		Elite_meetingAct02.this.getWindow().setAttributes(params);
	}

	class poponDismissListener implements PopupWindow.OnDismissListener {

		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub
			// Log.v("List_noteTypeActivity:", "我是关闭事件");
			clearWindow();
		}

	}

	private void destoryImage() {
		if (photo != null) {
			photo.recycle();
			photo = null;
		}
	}

}
