package com.mrlou.addservices;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.cloud.a.l;
import com.mrlou.economic.view.BadgeView;
import com.mrlou.image.HttpUtil;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.progressbar.Progress_Dialog;
import com.mrlou.progressbar.SweetAlertDialog;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class AddServices_mainAdapter extends BaseAdapter {

	private ArrayList<String> list_url, list_name, list_phone, list_company,
			list_job, list_newcount, list_stutas, list_rname,
			list_service_type;
	private Context mContext;
	private ViewHolder viewHolder;
	private String[] str = new String[] { "装修设计", "办公家具", "办公设备", "办公用品",
			"工商财税", "金融服务 ", "人力资源", "营销建站", "商务会展", "电信网路" };
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

	// private ImageLoader imageLoader;

	public AddServices_mainAdapter(Context context, ArrayList<String> url,
			ArrayList<String> name, ArrayList<String> phone,
			ArrayList<String> company, ArrayList<String> job,
			ArrayList<String> newcount, ArrayList<String> status,
			ArrayList<String> rname, ArrayList<String> service_type) {
		this.mContext = context;
		this.list_company = company;
		this.list_job = job;
		this.list_name = name;
		this.list_newcount = newcount;
		this.list_phone = phone;
		this.list_url = url;
		this.list_stutas = status;
		this.list_rname = rname;
		this.list_service_type = service_type;
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(mContext));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.logo) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.logo) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.logo) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_name.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list_name.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (view == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflate = ((Activity) mContext).getLayoutInflater();
			view = (View) inflate.inflate(R.layout.addservices_list_area, null);
			viewHolder.iv_head = (ImageView) view
					.findViewById(R.id.iv_addservices_list_head);
			viewHolder.tv_name = (TextView) view
					.findViewById(R.id.tv_addservices_list_name);
			viewHolder.tv_company = (TextView) view
					.findViewById(R.id.tv_addservices_list_company);
			viewHolder.lay_econimic_phone = (LinearLayout) view
					.findViewById(R.id.lay_addservices_phone);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.iv_head.setImageResource(R.drawable.logo);
		ImageLoader.getInstance().displayImage(list_url.get(position),
				viewHolder.iv_head, options);
		viewHolder.tv_name.setText(list_name.get(position));
		viewHolder.tv_company.setText(list_company.get(position));
		viewHolder.lay_econimic_phone.setOnClickListener(new BtnClicklistener(
				position, viewHolder.lay_econimic_phone));
		return view;
	}

	class ViewHolder {
		private ImageView iv_head;
		private TextView tv_name, tv_company;
		private LinearLayout  lay_econimic_phone;
	}

	class BtnClicklistener implements OnClickListener {

		private int position;
		private View view;

		public BtnClicklistener(int pos, View view) {
			this.position = pos;
			this.view = view;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.lay_addservices_phone:
				System.out.println("--ceshi--");
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ list_phone.get(position)));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
				break;
			default:
				break;
			}
		}
	}
}
