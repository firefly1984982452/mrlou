package com.mrlou.mine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Instrumentation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
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
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.ttf.FontManager;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;

public class New_AddInfoAct extends Activity implements HttpListener {

	private LinearLayout lay_newadd_info, lay_newadd_info_back, lay_icon01,
			lay_icon02, lay_newadd_time;
	private TextView tv_newadd_time;
	private ImageView iv_newadd_info01, iv_icon01, iv_icon02,iv_newadd_info02,iv_newadd_info03;
	private LinearLayout lay_newadd_info01,lay_newadd_info02,lay_newadd_info03;
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
	private EditText et_newadd_buildingname, et_newadd_square_meter,
			et_newadd_industry, et_newadd_proof;
	private String session_id, transaction_type = "1", photo_stream,
			avatars_id = "", add_v;
	private SharedPreferences sharedPreferences;
	private SweetAlertDialog pDialog;
	private Progress_Dialog dialog;
	private int image;
	private ArrayList<String> str_images=new ArrayList<>();
	private BadgeView badgeView1, badgeView2, badgeView3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newadd_info);
		sharedPreferences = New_AddInfoAct.this.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		Intent intent = getIntent();
		add_v = intent.getStringExtra("add_v");
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		// 弹出请求对话框
		pDialog = new SweetAlertDialog(New_AddInfoAct.this,
				SweetAlertDialog.PROGRESS_TYPE).setTitleText("Loading...");
		dialog = new Progress_Dialog(New_AddInfoAct.this, pDialog);
		if (add_v.equals("0")) {
			Toast.makeText(New_AddInfoAct.this, "非认证经纪人无提交权限", Toast.LENGTH_SHORT).show();
		}
		lay_newadd_commit = (LinearLayout) findViewById(R.id.lay_newadd_commit);
		lay_newadd_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = str_images.size()-1; i >=0; i--) {
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
				}else {
					avatars_id="";
				}
				
				if (add_v.equals("1")) {
					if (!et_newadd_buildingname.getText().toString().equals("")
							&& !et_newadd_square_meter.getText().toString()
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
									transaction_type,
									et_newadd_buildingname.getText().toString(),
									et_newadd_square_meter.getText().toString(),
									tv_newadd_time.getText().toString(),
									et_newadd_industry.getText().toString(),
									et_newadd_proof.getText().toString(),
									avatars_id);
						} else {
							Toast.makeText(New_AddInfoAct.this,
									"证明人名字，电话未填写或者未上传证明文件", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(New_AddInfoAct.this, "资料未填全，请填写完全再提交",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(New_AddInfoAct.this, "非认证经纪人无提交权限", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		et_newadd_buildingname = (EditText) findViewById(R.id.et_newadd_buildingname);
		et_newadd_square_meter = (EditText) findViewById(R.id.et_newadd_square_meter);
		et_newadd_industry = (EditText) findViewById(R.id.et_newadd_industry);
		et_newadd_proof = (EditText) findViewById(R.id.et_newadd_proof);
		Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		tv_newadd_time = (TextView) findViewById(R.id.tv_newadd_time);
		lay_newadd_time = (LinearLayout) findViewById(R.id.lay_newadd_time);
		lay_newadd_time.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatePickerDialog datePicker = new DatePickerDialog(
						New_AddInfoAct.this, new OnDateSetListener() {
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
		lay_icon01 = (LinearLayout) findViewById(R.id.lay_newadd_info_icon01);
		lay_icon01.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				transaction_type = "1";
				selecticon(1);
			}
		});
		lay_icon02 = (LinearLayout) findViewById(R.id.lay_newadd_info_icon02);
		lay_icon02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				transaction_type = "2";
				selecticon(2);
			}
		});
		iv_icon01 = (ImageView) findViewById(R.id.iv_newadd_info_icon01);
		iv_icon02 = (ImageView) findViewById(R.id.iv_newadd_info_icon02);
		selecticon(1);
		imageuri = Uri.fromFile(new File(saveDir + "temp4.png"));
		lay_newadd_info_back = (LinearLayout) findViewById(R.id.lay_newadd_info_back);
		lay_newadd_info_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(New_AddInfoAct.this,
						History_CompleteAct.class);
				intent.putExtra("add_v", add_v);
				startActivity(intent);
				finish();
			}
		});
		lay_newadd_info = (LinearLayout) findViewById(R.id.lay_newadd_info);
		iv_newadd_info01 = (ImageView) findViewById(R.id.iv_newadd_info01);
		lay_newadd_info01=(LinearLayout) findViewById(R.id.lay_newadd_info01);
		iv_newadd_info02=(ImageView) findViewById(R.id.iv_newadd_info02);
		lay_newadd_info02=(LinearLayout) findViewById(R.id.lay_newadd_info02);
		iv_newadd_info03=(ImageView) findViewById(R.id.iv_newadd_info03);
		lay_newadd_info03=(LinearLayout) findViewById(R.id.lay_newadd_info03);
		badgeView1 = new BadgeView(New_AddInfoAct.this,
				lay_newadd_info01);
		badgeView1.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView1.setBackgroundResource(R.drawable.fangyuan_images_del_img);
		badgeView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_newadd_info01.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images.set(0, "1");
				badgeView1.hide();
			}
		});
		badgeView2 = new BadgeView(New_AddInfoAct.this,
				lay_newadd_info02);
		badgeView2.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView2.setBackgroundResource(R.drawable.fangyuan_images_del_img);
		badgeView2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_newadd_info02.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images.set(1, "1");
				badgeView2.hide();
			}
		});
		badgeView3 = new BadgeView(New_AddInfoAct.this,
				lay_newadd_info03);
		badgeView3.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
		badgeView3.setBackgroundResource(R.drawable.fangyuan_images_del_img);
		badgeView3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				iv_newadd_info03.setImageResource(R.drawable.fangyuan_images_no_content);
				str_images.set(2, "1");
				badgeView3.hide();
			}
		});
		
		iv_newadd_info01.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image=1;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_newadd_info, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = New_AddInfoAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				New_AddInfoAct.this.getWindow().setAttributes(params);
			}
		});
		iv_newadd_info02.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image=2;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_newadd_info, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = New_AddInfoAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				New_AddInfoAct.this.getWindow().setAttributes(params);
			}
		});
		iv_newadd_info03.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				image=3;
				pop_image.setOutsideTouchable(true);
				pop_image.showAtLocation(lay_newadd_info, Gravity.BOTTOM
						| Gravity.CENTER, 0, 0);
				pop_image.showAsDropDown(view_pop);
				pop_image.update();
				WindowManager.LayoutParams params = New_AddInfoAct.this
						.getWindow().getAttributes();
				params.alpha = 0.7f;
				New_AddInfoAct.this.getWindow().setAttributes(params);
			}
		});
		view_pop = LayoutInflater.from(New_AddInfoAct.this).inflate(
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
		Request request = Request.requestImg(photo_stream, "9", "1", "2", "2");
		new HttpThread(request, New_AddInfoAct.this);
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

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					New_AddInfoAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	public void clearWindow() {
		WindowManager.LayoutParams params = New_AddInfoAct.this.getWindow()
				.getAttributes();
		params.alpha = 1f;
		New_AddInfoAct.this.getWindow().setAttributes(params);
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
				Toast.makeText(New_AddInfoAct.this, "添加成功", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(New_AddInfoAct.this,
						History_CompleteAct.class);
				intent.putExtra("add_v", add_v);
				startActivity(intent);
				finish();
				break;
			case 2:
				Toast.makeText(New_AddInfoAct.this, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 44:
				Toast.makeText(New_AddInfoAct.this, "图片上传失败", Toast.LENGTH_SHORT).show();
				if (image==1) {
					iv_newadd_info01
					.setImageResource(R.drawable.fangyuan_images_no_content);
				}else if (image==2) {
					iv_newadd_info02
					.setImageResource(R.drawable.fangyuan_images_no_content);
				}else {
					iv_newadd_info03
					.setImageResource(R.drawable.fangyuan_images_no_content);
				}
				
				break;
			case 55:
				Toast.makeText(New_AddInfoAct.this, "图片上传成功", Toast.LENGTH_SHORT).show();
				break;
			case 4001:
				Toast.makeText(New_AddInfoAct.this, "参数不可为空", Toast.LENGTH_SHORT).show();
				break;
			case 4014:
				Toast.makeText(New_AddInfoAct.this, "请选填姓名电话或者证明文件", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4015:
				Toast.makeText(New_AddInfoAct.this, "姓名电话或者证明文件未填写", Toast.LENGTH_SHORT)
						.show();
				break;
			case 4012:
				Toast.makeText(New_AddInfoAct.this, "没有找到成交记录", Toast.LENGTH_SHORT).show();
				break;
			case 4004:
				Toast.makeText(New_AddInfoAct.this, "图片类型错误", Toast.LENGTH_SHORT).show();
				break;
			case 4005:
				Toast.makeText(New_AddInfoAct.this, "图片非法", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				dialog.dismiss();
				Toast.makeText(New_AddInfoAct.this, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT)
						.show();
				break;
			}
		}
	};

	public void select_cjinfoadd(String session_id, String transaction_type,
			String building_name, String square_meter, String cj_time,
			String industry, String proof, String proof_id) {
		Request request = Request.requestaddcjinfo(session_id,
				transaction_type, building_name, square_meter, cj_time,
				industry, proof, proof_id);
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
					str_images.add(json.getString("id"));
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
			Intent intent = new Intent(New_AddInfoAct.this,
					History_CompleteAct.class);
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
