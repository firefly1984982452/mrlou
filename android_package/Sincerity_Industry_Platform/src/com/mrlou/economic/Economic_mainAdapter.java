package com.mrlou.economic;

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

public class Economic_mainAdapter extends BaseAdapter {

	private ArrayList<String> list_url, list_name, list_phone, list_company,
			list_job, list_newcount, list_areacount, list_stutas, list_rname;;
	private Context mContext;
	private ViewHolder viewHolder;
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	// private ImageLoader imageLoader;
	private String flags;

	public Economic_mainAdapter(Context context, ArrayList<String> url,
			ArrayList<String> name, ArrayList<String> phone,
			ArrayList<String> company, ArrayList<String> job,
			ArrayList<String> newcount, ArrayList<String> areacount,
			ArrayList<String> status, ArrayList<String> rname,String flag) {
		this.mContext = context;
		this.list_areacount = areacount;
		this.list_company = company;
		this.list_job = job;
		this.list_name = name;
		this.list_newcount = newcount;
		this.list_phone = phone;
		this.list_url = url;
		this.list_stutas = status;
		this.list_rname = rname;
		this.flags=flag;
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(mContext));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.logo) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.logo) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.logo) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
//				.displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
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
			view = (View) inflate.inflate(R.layout.economic_list_area, null);
			viewHolder.iv_head = (ImageView) view
					.findViewById(R.id.iv_economic_list_head);
			viewHolder.tv_name = (TextView) view
					.findViewById(R.id.tv_economic_list_name);
			viewHolder.tv_company = (TextView) view
					.findViewById(R.id.tv_economic_list_company);
			viewHolder.tv_job = (TextView) view
					.findViewById(R.id.tv_economic_list_job_year);
			viewHolder.tv_newcount = (TextView) view
					.findViewById(R.id.tv_economic_list_new_count);
			viewHolder.tv_areacount = (TextView) view
					.findViewById(R.id.tv_economic_list_area_count);
			viewHolder.lay_company = (LinearLayout) view
					.findViewById(R.id.lay_economic_list_company);
			viewHolder.lay_econimic_phone = (LinearLayout) view
					.findViewById(R.id.lay_econimic_phone);
			viewHolder.tv_area_name = (TextView) view
					.findViewById(R.id.tv_economic_list_area_name);
			viewHolder.lay_economic_head = (LinearLayout) view
					.findViewById(R.id.lay_economic_head);
			viewHolder.badgeView = new BadgeView(mContext, viewHolder.iv_head);
			viewHolder.badgeView
					.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
			viewHolder.badgeView.setBackgroundResource(R.drawable.oval_v);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.iv_head.setImageResource(R.drawable.logo);
		ImageLoader.getInstance().displayImage(list_url.get(position), viewHolder.iv_head, options);
		if (list_stutas.get(position).equals("0")) {
			viewHolder.lay_company.setBackgroundDrawable(null);
			viewHolder.tv_company.setTextColor(mContext.getResources()
					.getColor(R.color.light_gray));
		} else {
			viewHolder.lay_company
					.setBackgroundDrawable(mContext.getResources().getDrawable(
							R.drawable.corner_view_red_2dp));
			viewHolder.tv_company.setTextColor(mContext.getResources()
					.getColor(R.color.white));
			viewHolder.tv_company.setLeft(10);
		}
		viewHolder.badgeView.show();
		viewHolder.tv_name.setText(list_name.get(position));
		viewHolder.tv_company.setText(list_company.get(position));
		viewHolder.tv_job.setText("从业" + list_job.get(position) + "年");
		viewHolder.tv_newcount.setText(list_newcount.get(position));
		if (flags.equals("1")) {
			viewHolder.tv_area_name.setText(list_rname.get(position));
			viewHolder.tv_areacount.setText(list_areacount.get(position));
		}else {
			viewHolder.tv_area_name.setText("最新  ");
			viewHolder.tv_areacount.setText(list_rname.get(position));
		}
		viewHolder.lay_company.setOnClickListener(new BtnClicklistener(
				position, viewHolder.lay_company));
		viewHolder.lay_econimic_phone.setOnClickListener(new BtnClicklistener(
				position, viewHolder.lay_econimic_phone));
		return view;
	}

	class ViewHolder {
		private ImageView iv_head, iv_phone;
		private TextView tv_name, tv_company, tv_job, tv_newcount,
				tv_areacount, tv_area_name;
		private LinearLayout lay_company, lay_economic_head,
				lay_econimic_phone;
		private BadgeView badgeView;
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
			case R.id.lay_econimic_phone:
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ list_phone.get(position)));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
				mContext.startActivity(intent);
				break;
			case R.id.lay_economic_list_company:
				if (list_stutas.get(position).equals("1")) {
					Intent i = new Intent(mContext, Economic_CompanyAct.class);
					i.putExtra("company", list_company.get(position));
					mContext.startActivity(i);
				}
				break;
			default:
				break;
			}
		}
	}
}
