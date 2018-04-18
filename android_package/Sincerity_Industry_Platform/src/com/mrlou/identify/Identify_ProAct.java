package com.mrlou.identify;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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
import android.widget.TextView;
import android.widget.Toast;

import com.mrlou.listener.HttpListener;
import com.mrlou.mine.Add_BuildingAct;
import com.mrlou.mine.Select_CompanyAct;
import com.mrlou.mine.Setting_personal03;
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpGetThread;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * @author jx_chen
 * @date 2016-10-31---上午11:22:32
 * @problem
 */
public class Identify_ProAct extends Activity implements HttpListener {

	private TextView tv_02, tv_03, tv_04, tv_identify_title;
	private LinearLayout lay_proreigs_back,lay_proreigs_company;
	private String type, session_id;
	private EditText et_c_name, et_e_name, et_email, et_wechat;
	private TextView tv_building,tv_proreigs_company;
	private Button bt_proregis_comfirm;
	private LinearLayout lay_building, lay_head, lay_post;
	private ImageView iv_head, iv_post;
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
	private String avatar, cn_username, en_username, email, avatars_id,
			company, wechat_qr_id, post_card_id,
			region_id_real, post_card, wechat_qr, wechat, building_a,
			building_b,type_id,company_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.identify_pro_register);
		getDate();
		findView();
	}

	private void getDate() {
		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		type_id = intent.getStringExtra("type_id");
		cn_username = intent.getStringExtra("cn_username");
		en_username = intent.getStringExtra("en_username");
		email = intent.getStringExtra("email");
		avatar = intent.getStringExtra("avatar");
		avatars_id = intent.getStringExtra("avatar_id");
		company = intent.getStringExtra("company");
		company_id=intent.getStringExtra("company_id");
		wechat_qr_id = intent.getStringExtra("wechat_qr_id");
		post_card_id = intent.getStringExtra("post_card_id");
		region_id_real = intent.getStringExtra("region_id_real");
		post_card = intent.getStringExtra("post_card");
		wechat_qr = intent.getStringExtra("wechat_qr");
		wechat = intent.getStringExtra("wechat");
		building_a = intent.getStringExtra("building_a");
		building_b = intent.getStringExtra("building_b");
		sharedPreferences = Identify_ProAct.this.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(Identify_ProAct.this));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.fangyuan_images_no_content) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.fangyuan_images_no_content) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.fangyuan_images_no_content) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build();
	}

	private void findView() {
		// TODO Auto-generated method stub
		pDialog = new SweetAlertDialog(Identify_ProAct.this,
				SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
		dialog = new Progress_Dialog(Identify_ProAct.this, pDialog);

		bt_proregis_comfirm = (Button) findViewById(R.id.bt_proregis_comfirm);
		bt_proregis_comfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!et_c_name.getText().toString().equals("")
						&& !et_email.getText().toString().equals("")
						&& !tv_proreigs_company.getText().toString().equals("")
						&& !tv_building.getText().toString().equals("")) {
					select_modifytype(session_id, et_c_name.getText()
							.toString(), et_e_name.getText().toString(),
							et_email.getText().toString(), tv_proreigs_company.getText()
									.toString(), building_a + building_b, "",
							region_id_real, et_wechat.getText().toString(),
							wechat_qr_id, avatars_id, post_card_id, "");
				} else {
					Toast.makeText(Identify_ProAct.this, "资料未填全,请填写完必填项后提交",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		lay_proreigs_company=(LinearLayout) findViewById(R.id.lay_proreigs_company);
		lay_proreigs_company.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Identify_ProAct.this,Select_CompanyAct.class);
				intent.putExtra("cn_username", et_c_name.getText().toString());
				intent.putExtra("en_username", et_e_name.getText().toString());
				intent.putExtra("email", et_email.getText().toString());
				intent.putExtra("company", company);
				intent.putExtra("company_id", company_id);
				intent.putExtra("type", type);
				intent.putExtra("type_id", type_id);
				intent.putExtra("avatar", avatar);
				intent.putExtra("wechat_qr_id", wechat_qr_id);
				intent.putExtra("post_card_id", post_card_id);
				intent.putExtra("avatar_id", avatars_id);
				intent.putExtra("building_a", building_a);
				intent.putExtra("building_b", building_b);
				intent.putExtra("wechat", et_wechat.getText().toString());
				intent.putExtra("wechat_qr", wechat_qr);
				intent.putExtra("post_card", post_card);
				intent.putExtra("act", "6");
				startActivity(intent);
				Identify_ProAct.this.finish();
			}
		});
		et_c_name = (EditText) findViewById(R.id.et_proreigs_c_name);
		et_e_name = (EditText) findViewById(R.id.et_proreigs_e_name);
		et_email = (EditText) findViewById(R.id.et_proreigs_email);
		tv_proreigs_company = (TextView) findViewById(R.id.tv_proreigs_company);
		et_wechat = (EditText) findViewById(R.id.et_proregis_wechat);
		tv_building = (TextView) findViewById(R.id.tv_proregis_building);
		lay_building = (LinearLayout) findViewById(R.id.lay_proregis_building);
		lay_building.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Identify_ProAct.this,
						Identify_addBuildingAct.class);
				intent.putExtra("cn_username", et_c_name.getText().toString());
				intent.putExtra("en_username", et_e_name.getText().toString());
				intent.putExtra("email", et_email.getText().toString());
				intent.putExtra("company", tv_proreigs_company.getText().toString());
				intent.putExtra("company_id", company_id);
				intent.putExtra("type", type);
				intent.putExtra("type_id", type_id);
				intent.putExtra("avatar", avatar);
				intent.putExtra("wechat_qr_id", wechat_qr_id);
				intent.putExtra("post_card_id", post_card_id);
				intent.putExtra("avatar_id", avatars_id);
				intent.putExtra("building_a", building_a);
				intent.putExtra("building_b", building_b);
				intent.putExtra("wechat", et_wechat.getText().toString());
				intent.putExtra("wechat_qr", wechat_qr);
				intent.putExtra("post_card", post_card);
				startActivity(intent);
				Identify_ProAct.this.finish();
			}
		});
		lay_head = (LinearLayout) findViewById(R.id.lay_proregis_head);
		lay_head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 1;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_building, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = Identify_ProAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				Identify_ProAct.this.getWindow().setAttributes(params);
			}
		});
		lay_post = (LinearLayout) findViewById(R.id.lay_proregis_post);
		lay_post.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image = 6;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_building, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = Identify_ProAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				Identify_ProAct.this.getWindow().setAttributes(params);
			}
		});
		view_pop = LayoutInflater.from(Identify_ProAct.this).inflate(
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
		iv_head = (ImageView) findViewById(R.id.iv_proregis_head);
		ImageLoader.getInstance().displayImage(avatar, iv_head, options);
		iv_post = (ImageView) findViewById(R.id.iv_proregis_post);

		tv_identify_title = (TextView) findViewById(R.id.tv_identify_title);
		tv_identify_title.setText(type);
		tv_02 = (TextView) findViewById(R.id.tv_pro_02);
		tv_02.setText("同意\"");
		tv_03 = (TextView) findViewById(R.id.tv_pro_03);
		tv_03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Identify_ProAct.this,
						Identify_MrlouAct.class);
				startActivity(intent);
			}
		});
		tv_04 = (TextView) findViewById(R.id.tv_pro_04);
		tv_04.setText("\"");
		lay_proreigs_back = (LinearLayout) findViewById(R.id.lay_proreigs_back);
		lay_proreigs_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		et_c_name.setText(cn_username);
		et_e_name.setText(en_username);
		et_email.setText(email);
		if (!company.equals("")) {
			tv_proreigs_company.setText(company);
		}
		if (!building_a.equals("") || !building_b.equals("")) {
			tv_building.setText(building_a + building_b);
		}
		if (!wechat.equals("")) {
			et_wechat.setText(wechat);
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
					Identify_ProAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	public void select_modifytype(String session_id, String cn_name,
			String en_name, String email, String company, String building,
			String worktime, String region_id, String wechat,
			String wechat_qr_id, String avatar_id, String post_card_id,
			String service_type) {
		Request request = Request.request_idenfify(session_id, type_id,cn_name,
				en_name, email, company,company_id, building_a,building_b, worktime, region_id, wechat,
				wechat_qr_id, avatar_id, post_card_id, service_type,"");
		new HttpThread(request, this);
	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				simulateKey(KeyEvent.KEYCODE_BACK);
				Toast.makeText(Identify_ProAct.this, "修改身份提交成功", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Toast.makeText(Identify_ProAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 44:
				Toast.makeText(Identify_ProAct.this, "图片上传失败", Toast.LENGTH_SHORT).show();
				if (image == 1) {
					iv_head
							.setImageResource(R.drawable.fangyuan_images_no_content);
				} else if (image == 6) {
					iv_post
							.setImageResource(R.drawable.fangyuan_images_no_content);
				} 
				break;
			case 55:
				Toast.makeText(Identify_ProAct.this, "图片上传成功", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(Identify_ProAct.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT)
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
					newHandler.sendEmptyMessage(1);
				} else if (errorid.equals("1")) {
					newHandler.sendEmptyMessage(2);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if (reqID == Request.REQUEST_IMG_STREAM) {
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Bitmap bitmap = null;
		if (requestCode == CAMERA_RESULT && resultCode == RESULT_OK) {
			if (mPhotoFile != null && mPhotoFile.exists()) {
				bitmap = decodeUriAsBitmap(Uri.fromFile(mPhotoFile));
				if (image == 1) {
					startPhotoZoom(Uri.fromFile(mPhotoFile), PHOTORESOULT);
				}
				else {
					iv_post.setImageBitmap(bitmap);
					upload_image(bitmap);
				}
			}
		}
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
				&& null != data) {
			bitmap = decodeUriAsBitmap(data.getData());
			// dialog.dialog();
			if (image == 1) {
				startPhotoZoom(data.getData(), PHOTORESOULT);
			} 
			else {
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
				new HttpThread(request, Identify_ProAct.this);
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
		 new HttpThread(request, Identify_ProAct.this);
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

	public void clearWindow() {
		WindowManager.LayoutParams params = Identify_ProAct.this.getWindow()
				.getAttributes();
		params.alpha = 1f;
		Identify_ProAct.this.getWindow().setAttributes(params);
	}

	class poponDismissListener implements PopupWindow.OnDismissListener {

		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub
			// Log.v("List_noteTypeActivity:", "我是关闭事件");
			clearWindow();
		}
	}

}
