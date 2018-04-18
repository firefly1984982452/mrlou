package com.mrlou.update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mrlou.mrlou.R;

public class UpdateManager {
	/* 下载�? */
	private static final int DOWNLOAD = 1;
	/* 下载结束 */
	private static final int DOWNLOAD_FINISH = 2;

	private static final int UPDATE_VERSION = 03;
	/* 保存解析的XML信息 */
	HashMap<String, String> mHashMap;
	/* 下载保存路径 */
	private String mSavePath, ver_name;
	/* 记录进度条数�? */
	private int progress;
	/* 是否取消更新 */
	private boolean cancelUpdate = false, isNew = true;
	private Button bt_update, bt_updateCancel, bt_load;
	private TextView tv, tv_pb, tv_pb1;
	public AlertDialog aDialog_update, alertDialog_load;
	private Context mContext;
	/* 更新进度�? */
	private ProgressBar mProgress;
	private Dialog mDownloadDialog;
	private SharedPreferences msp;
	private Editor editor;
	private String errorCode, version_code, version_name, version_url;
	private Handler mHandler;
	private ProgressDialog progressDialog;
	private boolean flags;
	public UpdateManager(Context context,boolean flag) {
		this.mContext = context;
		this.flags=flag;
	}

	/**
	 * 检测软件更新
	 */
	public void checkUpdate(boolean flag) {
		this.isNew = flag;
		// preferenceHelper = PreferenceHelper.getInstance(mContext);
		// showProgressDialog("正在连接...");
		// initHandler();
		// netmanages();
	}

	/**
	 * 获取软件版本�?
	 * 
	 * @param context
	 * @return
	 */
	private double getVersionCode(Context context) {
		double versionCode = 0;
		try {
			// 获取软件版本号，对应AndroidManifest.xml下android:versionName
			versionCode = Double.parseDouble(context.getPackageManager()
					.getPackageInfo("com.example.app_pic", 0).versionName);

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	/**
	 * 显示软件更新对话框?
	 */
	public void showNoticeDialog() {
		// 构�?对话�?
		final View view;
		view = LayoutInflater.from(mContext).inflate(R.layout.update_dialog,
				null);
		bt_update = (Button) view.findViewById(R.id.bt_update);
		// bt_updateCancel = (Button) view.findViewById(R.id.bt2_update);
		// bt_update.setText("立即更新");
		// bt_updateCancel.setText("稍后更新");
		// tv = (TextView) view.findViewById(R.id.up_title);
		// tv.setText("检测到新版本，需要更新吗");

		aDialog_update = new AlertDialog.Builder(mContext).create();
		aDialog_update.show();
		aDialog_update.setCanceledOnTouchOutside(false);
		WindowManager.LayoutParams params = aDialog_update.getWindow()
				.getAttributes();
		params.gravity = Gravity.CENTER;
		aDialog_update.getWindow().setAttributes(params);
		aDialog_update.getWindow().setContentView(view);
		aDialog_update.setCancelable(false);
		aDialog_update.setOnKeyListener(keylistener);  
		bt_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				flags=false;
				aDialog_update.dismiss();
				Intent intent = new Intent(mContext, DownAPKService.class);
				intent.putExtra("apk_url",
						"http://appapi.imrlou.com/release/Sincerity_Industry_Platform.apk");
				mContext.startService(intent);
				Toast.makeText(mContext, "正在后台进行下载，稍后会自动安装", Toast.LENGTH_LONG)
						.show();
			}
		});
		// bt_updateCancel.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// aDialog_update.dismiss();
		// }
		// });
	}

	private OnKeyListener keylistener = new DialogInterface.OnKeyListener() {

		@Override
		public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			// TODO Auto-generated method stub
			if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
				return true;
			} else {
				return false;
			}
		}
	};

	/**
	 * 显示软件下载对话?
	 */
	private void showDownloadDialog() {
		final View view;
		view = LayoutInflater.from(mContext).inflate(R.layout.dialog_bg, null);
		// bt_load = (Button) view.findViewById(R.id.bt_load);
		// bt_load.setText("取消");
		// tv = (TextView) view.findViewById(R.id.load_title);
		// tv.setText("下载进度");
		// tv_pb = (TextView) view.findViewById(R.id.tv_pb);
		// tv_pb1 = (TextView) view.findViewById(R.id.tv_pb1);
		// alertDialog_load = new AlertDialog.Builder(mContext).create();
		// // 给下载对话框增加进度
		// mProgress = (ProgressBar) view.findViewById(R.id.update_progress);
		alertDialog_load.show();
		alertDialog_load.setCanceledOnTouchOutside(false);
		WindowManager.LayoutParams params = aDialog_update.getWindow()
				.getAttributes();
		params.gravity = Gravity.CENTER;
		alertDialog_load.getWindow().setAttributes(params);
		alertDialog_load.getWindow().setContentView(view);
		bt_load.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDialog_load.dismiss();
				File file = new File(mSavePath, mHashMap.get("version_name"));
				file.delete();
				// 设置取消状�?
				cancelUpdate = true;
			}
		});
		downloadApk();
	}

	/**
	 * 下载apk文件
	 */
	private void downloadApk() {
		// 启动新线程下载软�?
		new downloadApkThread().start();
	}

	/**
	 * 下载文件线程
	 * 
	 * @author coolszy
	 * @date 2012-4-26
	 * @blog http://blog.92coding.com
	 */
	private class downloadApkThread extends Thread {
		@Override
		public void run() {
			Looper.prepare();
			try {
				// 判断SD卡是否存在，并且是否具有读写权限
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// 获得存储卡的路径
					String sdpath = Environment.getExternalStorageDirectory()
							+ "/";
					mSavePath = sdpath + "GMACSAIC_App";
					// URL url = new
					// URL("https://www.esibas.com.cn/sibas"+mHashMap.get("version_url"));

					// URL url = new
					// URL(mContext.getResources().getString(R.string.url_total)
					// + mHashMap.get("version_url"));
					URL url = new URL("");
					// new URL(mContext.getResources().getString(
					// R.string.url_total_load)
					// + mHashMap.get("version_url")
					// + "&memberMobile="
					// + Member.getMembermobile()
					// + "&passWord="
					// + Member.getPswd() + "&appIMIE=" + Member.getIEME());
					ver_name = mHashMap.get("version_url")
							.substring(
									mHashMap.get("version_url").indexOf(
											"FILE_NAME=") + 10,
									mHashMap.get("version_url").indexOf(
											"&apkSize"));
					// 创建连接
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestProperty("Accept-Encoding", "identity");
					// conn.setRequestProperty("cookie", Member.getCookie());
					// conn.setRequestProperty("loginKeepFlag",
					// Member.getLoginKeepFlag());
					conn.connect();

					// 获取文件大小
					String len = conn.getHeaderField("Content-Length");
					int length = conn.getContentLength();
					// int length=Integer.valueOf(len);
					System.out.println("文件大小----->" + length);
					// 创建输入流
					InputStream is = conn.getInputStream();
					File file = new File(mSavePath);
					// 判断文件目录是否存在
					if (!file.exists()) {
						file.mkdir();
					}
					File apkFile = new File(mSavePath, ver_name);
					FileOutputStream fos = new FileOutputStream(apkFile);
					int count = 0;
					// 缓存
					byte buf[] = new byte[1024];
					// 写入到文件中
					do {
						int numread = is.read(buf);
						count += numread;
						// 计算进度条位数
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOAD);
						// 写入文件
						if (numread <= 0) {
							// 下载完成
							mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
							break;
						}
						fos.write(buf, 0, numread);
						fos.flush();
					} while (!cancelUpdate);// 点击取消就停止下�?
					fos.close();
					is.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				alertDialog_load.dismiss();
				Toast.makeText(mContext, "网络不给力,请重新再试", Toast.LENGTH_LONG)
						.show();
				File file = new File(mSavePath, version_name);
				if (file.exists()) {
					file.delete();
				}
				e.printStackTrace();
			}
			Looper.loop();
			// 取消下载对话框显�?
			alertDialog_load.dismiss();
		}
	};

	/**
	 * 安装APK文件
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, ver_name);
		if (!apkfile.exists()) {
			return;
		}
		// 设置出现导航界面
		msp = mContext.getSharedPreferences("USERCONFIG", Context.MODE_PRIVATE);
		editor = msp.edit();
		editor.putBoolean("firstUse", true);
		editor.commit();

		// 通过Intent安装APK文件
		Intent i = new Intent();
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setAction(android.content.Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);

	}

}
