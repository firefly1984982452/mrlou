package com.mrlou.mine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrlou.h5.Exit_Act;
import com.mrlou.h5.YlbMessage_Act;
import com.mrlou.identify.Identify_Act;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.ttf.FontManager;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.mrlou.util.User_info;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class Setting_personal03 extends Activity implements HttpListener {

	private LinearLayout lay_setting_personal03_back, lay_setting03_get_loupan,
			lay_setting_modify_03, lay_setting03_c_name, lay_setting03_company,
			lay_setting03_company_content,lay_setting03_company_more;
	private ImageView iv_setting_personal_icon01, iv_setting_personal_icon02,
			iv_setting_personal_head;
	private EditText et_setting03_c_name, et_setting03_e_name,
			et_setting03_email, et_setting03_wx_num;
	private Button bt_setting03_exit, bt_setting03_modify;
	private TextView tv_setting03_phone, tv_setting03_identy,
			tv_setting03_building, tv_setting03_company;
	private int image = 1;
	private View view_pop;
	private Bitmap photo;
	private Button bt_pic, bt_camera, bt_cancel;
	private PopupWindow pop_image;
	private String saveDir = Environment.getExternalStorageDirectory()
			.getPath() + "/temp_image";
	private File mPhotoFile;
	private Uri imageuri;
	private int CAMERA_RESULT = 100;
	private int RESULT_LOAD_IMAGE = 200;
	private int PHOTORESOULT = 300;
	private String session_id;
	private SharedPreferences sharedPreferences;
	private String phone_number, email, en_username, cn_username, company,
			building_a, building_b, avatar, status, type, user_id;
	private String avatars_id, wechat_qr_id, post_card_id, photo_stream,
			post_card, wechat, wechat_qr, url, url2, add_v, mrString,
			company_id;
	// private ImageLoader imageLoader;
	DisplayImageOptions options, options2; // DisplayImageOptions是用于设置图片显示的类
	private SweetAlertDialog pDialog;
	private Progress_Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_setting_personal3);
		sharedPreferences = Setting_personal03.this.getSharedPreferences(
				"user", Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		ImageLoader.getInstance()
				.init(ImageLoaderConfiguration
						.createDefault(Setting_personal03.this));
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
	}

	private void getDate() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		phone_number = intent.getStringExtra("phone_number");
		email = intent.getStringExtra("email");
		en_username = intent.getStringExtra("en_username");
		cn_username = intent.getStringExtra("cn_username");
		company = intent.getStringExtra("company");
		building_a = intent.getStringExtra("building_a");
		building_b = intent.getStringExtra("building_b");
		avatar = intent.getStringExtra("avatar");
		avatars_id = intent.getStringExtra("avatar_id");
		status = intent.getStringExtra("status");
		type = intent.getStringExtra("type");
		user_id = intent.getStringExtra("user_id");
		post_card = intent.getStringExtra("post_card");
		post_card_id = intent.getStringExtra("post_card_id");
		wechat_qr_id = intent.getStringExtra("wechat_qr_id");
		wechat_qr = intent.getStringExtra("wechat_qr");
		wechat = intent.getStringExtra("wechat");
		add_v = intent.getStringExtra("v");
		company_id = intent.getStringExtra("company_id");
	}

	private void findView() {
		// TODO Auto-generated method stub
		pDialog = new SweetAlertDialog(Setting_personal03.this,
				SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
		dialog = new Progress_Dialog(Setting_personal03.this, pDialog);
		lay_setting03_c_name = (LinearLayout) findViewById(R.id.lay_setting03_c_name);
		lay_setting03_company = (LinearLayout) findViewById(R.id.lay_setting03_company);
		lay_setting03_company_more=(LinearLayout) findViewById(R.id.lay_setting03_company_more);
		tv_setting03_phone = (TextView) findViewById(R.id.tv_setting03_phone);
		tv_setting03_phone.setText(phone_number);
		tv_setting03_identy = (TextView) findViewById(R.id.tv_setting03_identy);
		if (type.equals("1")) {
			tv_setting03_identy.setText("开发商");
		} else if (type.equals("2")) {
			tv_setting03_identy.setText("代理商");
		} else if (type.equals("3")) {
			tv_setting03_identy.setText("运营商");
		} else {
			tv_setting03_identy.setText("物业公司");
		}
		tv_setting03_building = (TextView) findViewById(R.id.tv_setting03_building);
		if (!building_a.equals("") || !building_b.equals("")) {
			if (building_a.equals("")) {
				tv_setting03_building.setText(building_b);
			} else if (building_b.equals("")) {
				tv_setting03_building.setText(building_a);
			} else {
				tv_setting03_building.setText(building_a + "," + building_b);
			}
		}
		et_setting03_c_name = (EditText) findViewById(R.id.et_setting03_c_name);
		if (add_v.equals("1")) {
			et_setting03_c_name.setText(cn_username);
			et_setting03_c_name.setFocusable(false);
			lay_setting03_c_name.setVisibility(View.VISIBLE);
		} else {
			et_setting03_c_name.setText(cn_username);
			lay_setting03_c_name.setVisibility(View.GONE);
		}
		et_setting03_e_name = (EditText) findViewById(R.id.et_setting03_e_name);
		et_setting03_e_name.setText(en_username);
		et_setting03_email = (EditText) findViewById(R.id.et_setting03_email);
		et_setting03_email.setText(email);
		tv_setting03_company = (TextView) findViewById(R.id.tv_setting03_company);
		tv_setting03_company.setText(company);
		if (add_v.equals("1")) {
			lay_setting03_company_more.setVisibility(View.GONE);
			lay_setting03_company.setVisibility(View.VISIBLE);
		} else {
			lay_setting03_company_more.setVisibility(View.VISIBLE);
			lay_setting03_company.setVisibility(View.GONE);
		}
		et_setting03_wx_num = (EditText) findViewById(R.id.et_setting03_wx_num);
		et_setting03_wx_num.setText(wechat);
		bt_setting03_exit = (Button) findViewById(R.id.bt_setting03_exit);
		bt_setting03_exit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				User_info.setFlag(true);
				mrString = Setting_personal03.this.getExternalFilesDir(null)
						.getPath();
				url = "file://" + mrString + "/bulidersir/register_step1.html";
				url2 = "file://" + mrString + "/bulidersir/";
				Intent intent = new Intent(Setting_personal03.this,
						Exit_Act.class);
				intent.putExtra("url", url);
				intent.putExtra("url2", url2);
				startActivity(intent);
				Setting_personal03.this.finish();
			}
		});
		bt_setting03_modify = (Button) findViewById(R.id.bt_setting03_modify);
		bt_setting03_modify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!et_setting03_c_name.getText().toString().equals("")
						&& !et_setting03_email.getText().toString().equals("")
						&& !et_setting03_e_name.getText().toString().equals("")
						&& !tv_setting03_company.getText().toString()
								.equals("")
						&& !tv_setting03_building.getText().toString()
								.equals("")) {
					select_setting(session_id, user_id, type,
							et_setting03_c_name.getText().toString(),
							et_setting03_e_name.getText().toString(),
							et_setting03_email.getText().toString(),
							tv_setting03_company.getText().toString(),
							building_a, building_b, et_setting03_wx_num
									.getText().toString(), avatars_id,
							wechat_qr_id, post_card_id);
				} else {
					Toast.makeText(Setting_personal03.this, "填写内容不全，请补全后重新提交",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		lay_setting03_company_content = (LinearLayout) findViewById(R.id.lay_setting03_company_content);
		lay_setting03_company_content.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!add_v.equals("1")) {
					Intent intent = new Intent(Setting_personal03.this,
							Select_CompanyAct.class);
					intent.putExtra("v", add_v);
					intent.putExtra("company_id", company_id);
					intent.putExtra("cn_username", et_setting03_c_name
							.getText().toString());
					intent.putExtra("en_username", et_setting03_e_name
							.getText().toString());
					intent.putExtra("email", et_setting03_email.getText()
							.toString());
					intent.putExtra("phone_number", phone_number);
					intent.putExtra("company", tv_setting03_company.getText()
							.toString());
					intent.putExtra("type", type);
					intent.putExtra("status", status);
					intent.putExtra("user_id", user_id);
					intent.putExtra("avatar", avatar);
					intent.putExtra("wechat_qr_id", wechat_qr_id);
					intent.putExtra("post_card_id", post_card_id);
					intent.putExtra("avatar_id", avatars_id);
					intent.putExtra("building_a", building_a);
					intent.putExtra("building_b", building_b);
					intent.putExtra("wechat", et_setting03_wx_num.getText()
							.toString());
					intent.putExtra("wechat_qr", wechat_qr);
					intent.putExtra("post_card", post_card);
					intent.putExtra("act", "3");
					startActivity(intent);
					Setting_personal03.this.finish();
				}
			}
		});
		lay_setting_modify_03 = (LinearLayout) findViewById(R.id.lay_setting_modify_03);
		lay_setting_modify_03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Setting_personal03.this,
						Identify_Act.class);
				intent.putExtra("cn_username", et_setting03_c_name.getText()
						.toString());
				intent.putExtra("en_username", et_setting03_e_name.getText()
						.toString());
				intent.putExtra("email", et_setting03_email.getText()
						.toString());
				intent.putExtra("company", tv_setting03_company.getText()
						.toString());
				intent.putExtra("avatar", avatar);
				intent.putExtra("wechat_qr_id", wechat_qr_id);
				intent.putExtra("post_card_id", post_card_id);
				intent.putExtra("avatar_id", avatars_id);
				intent.putExtra("building_a", building_a);
				intent.putExtra("building_b", building_b);
				intent.putExtra("wechat", et_setting03_wx_num.getText()
						.toString());
				intent.putExtra("wechat_qr", wechat_qr);
				intent.putExtra("post_card", post_card);
				intent.putExtra("region_id", "");
				intent.putExtra("region_id_real", "");
				intent.putExtra("company_id", company_id);
				startActivity(intent);

			}
		});
		lay_setting03_get_loupan = (LinearLayout) findViewById(R.id.lay_setting03_get_loupan);
		lay_setting03_get_loupan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Setting_personal03.this,
						Add_BuildingAct.class);
				intent.putExtra("cn_username", et_setting03_c_name.getText()
						.toString());
				intent.putExtra("en_username", et_setting03_e_name.getText()
						.toString());
				intent.putExtra("email", et_setting03_email.getText()
						.toString());
				intent.putExtra("phone_number", phone_number);
				intent.putExtra("company", tv_setting03_company.getText()
						.toString());
				intent.putExtra("type", type);
				intent.putExtra("status", status);
				intent.putExtra("user_id", user_id);
				intent.putExtra("avatar", avatar);
				intent.putExtra("wechat_qr_id", wechat_qr_id);
				intent.putExtra("post_card_id", post_card_id);
				intent.putExtra("avatar_id", avatars_id);
				intent.putExtra("building_a", building_a);
				intent.putExtra("building_b", building_b);
				intent.putExtra("wechat", et_setting03_wx_num.getText()
						.toString());
				intent.putExtra("wechat_qr", wechat_qr);
				intent.putExtra("post_card", post_card);
				intent.putExtra("v", add_v);
				intent.putExtra("company_id", company_id);
				startActivity(intent);
				Setting_personal03.this.finish();
			}
		});
		// 更改或上传微信二维码
		iv_setting_personal_icon01 = (ImageView) findViewById(R.id.iv_setting_personal3_icon01);
		iv_setting_personal_icon01
				.setImageResource(R.drawable.fangyuan_images_no_content);
		if (wechat_qr != "") {
			ImageLoader.getInstance().displayImage(wechat_qr,
					iv_setting_personal_icon01, options2);
		}
		iv_setting_personal_icon01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 7;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_setting03_get_loupan,
						Gravity.BOTTOM | Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = Setting_personal03.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				Setting_personal03.this.getWindow().setAttributes(params);
			}
		});
		// 更改或上传名片
		iv_setting_personal_icon02 = (ImageView) findViewById(R.id.iv_setting_personal3_icon02);
		iv_setting_personal_icon02
				.setImageResource(R.drawable.fangyuan_images_no_content);
		if (post_card != "") {
			ImageLoader.getInstance().displayImage(post_card,
					iv_setting_personal_icon02, options2);
		}
		iv_setting_personal_icon02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 6;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_setting03_get_loupan,
						Gravity.BOTTOM | Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = Setting_personal03.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				Setting_personal03.this.getWindow().setAttributes(params);
			}
		});
		// 更改或上传头像
		iv_setting_personal_head = (ImageView) findViewById(R.id.iv_setting_personal3_head);
		ImageLoader.getInstance().displayImage(avatar,
				iv_setting_personal_head, options);
		iv_setting_personal_head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 1;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_setting03_get_loupan,
						Gravity.BOTTOM | Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = Setting_personal03.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				Setting_personal03.this.getWindow().setAttributes(params);
			}
		});
		lay_setting_personal03_back = (LinearLayout) findViewById(R.id.lay_setting_personal03_back);
		lay_setting_personal03_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		view_pop = LayoutInflater.from(Setting_personal03.this).inflate(
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
					mPhotoFile = new File(saveDir, "temps.jpg");
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
				if (image == 1) {
					startPhotoZoom(Uri.fromFile(mPhotoFile), PHOTORESOULT);
				} else if (image == 7) {
					startPhotoZoom(Uri.fromFile(mPhotoFile), PHOTORESOULT);
				} else {
					iv_setting_personal_icon02.setImageBitmap(bitmap);
					upload_image(bitmap);
				}

			}
		}
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			bitmap = decodeUriAsBitmap(data.getData());
			dialog.dialog();
			if (image == 1) {
				startPhotoZoom(data.getData(), PHOTORESOULT);
			} else if (image == 7) {
				startPhotoZoom(data.getData(), PHOTORESOULT);
			} else {
				iv_setting_personal_icon02.setImageBitmap(bitmap);
				upload_image(bitmap);
			}

		}

		if (requestCode == PHOTORESOULT && resultCode == RESULT_OK) {

			if (imageuri != null) {
				Bitmap bitmaps = decodeUriAsBitmap(imageuri);
				dialog.dialog();
				if (image == 1) {
					iv_setting_personal_head.setImageBitmap(bitmaps);
				} else if (image == 7) {
					iv_setting_personal_icon01.setImageBitmap(bitmaps);
				}
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
				Request request = Request.requestImg(photo_stream, "" + image,
						"1", "2", "2");
				new HttpThread(request, Setting_personal03.this);
			}
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
		Request request = Request.requestImg(photo_stream, "" + image, "1",
				"2", "2");
		new HttpThread(request, Setting_personal03.this);
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

	public void clearWindow() {
		WindowManager.LayoutParams params = Setting_personal03.this.getWindow()
				.getAttributes();
		params.alpha = 1f;
		Setting_personal03.this.getWindow().setAttributes(params);
	}

	class poponDismissListener implements PopupWindow.OnDismissListener {

		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub
			// Log.v("List_noteTypeActivity:", "我是关闭事件");
			clearWindow();
		}

	}

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					Setting_personal03.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	public void select_setting(String session_id, String user_id, String type,
			String cn_name, String en_name, String email, String company,
			String building_a, String building_b, String wx_num,
			String avatars_id, String wechat_qr_id, String post_card_id) {
		Request request = Request.requestsettingper03(session_id, user_id,
				type, cn_name, en_name, email, company,building_a, building_b,
				wx_num, avatars_id, wechat_qr_id, post_card_id);
		new HttpThread(request, this);
	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Toast.makeText(Setting_personal03.this, "修改成功", Toast.LENGTH_SHORT).show();
				simulateKey(KeyEvent.KEYCODE_BACK);
				break;
			case 2:
				Toast.makeText(Setting_personal03.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 44:
				Toast.makeText(Setting_personal03.this, "图片上传失败", Toast.LENGTH_SHORT).show();
				if (image == 1) {
					iv_setting_personal_head
							.setImageResource(R.drawable.fangyuan_images_no_content);
				} else if (image == 6) {
					iv_setting_personal_icon02
							.setImageResource(R.drawable.fangyuan_images_no_content);
				} else {
					iv_setting_personal_icon01
							.setImageResource(R.drawable.fangyuan_images_no_content);
				}
				break;
			case 55:
				Toast.makeText(Setting_personal03.this, "图片上传成功", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(Setting_personal03.this, "网络异常，请尝试下拉刷新", Toast.LENGTH_SHORT)
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
}
