package com.mrlou.addservices;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrlou.economic.view.BadgeView;
import com.mrlou.mrlou.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class AddServices_CompanyListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> str_name, str_phone, str_year, str_company,
			str_deal, str_url, str_url_flag, str_customer_name,str_type,str_service_type,str_img_all;
	private ViewHolder viewHolder;
	DisplayImageOptions options, options2; // DisplayImageOptions是用于设置图片显示的类
			// private ImageLoader imageLoader;
	private String[] str=new String[]{"装修设计","办公家具","办公设备","办公用品","工商财税","金融服务 ","人力资源","营销建站","商务会展","电信网路"};
	
	public AddServices_CompanyListAdapter(Context context, ArrayList<String> url,
			ArrayList<String> flag, ArrayList<String> name,
			ArrayList<String> phone, ArrayList<String> year,
			ArrayList<String> company, ArrayList<String> deal,
			ArrayList<String> customer_name,ArrayList<String> type,ArrayList<String> service_type,ArrayList<String> img_all) {
		this.mContext = context;
		this.str_url = url;
		this.str_url_flag = flag;
		this.str_name = name;
		this.str_phone = phone;
		this.str_year = year;
		this.str_company = company;
		this.str_deal = deal;
		this.str_customer_name = customer_name;
		this.str_type=type;
		this.str_service_type=service_type;
		this.str_img_all=img_all;
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(mContext));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
				.build();
		options2 = new DisplayImageOptions.Builder()
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
		return str_name.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return str_name.get(position);
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
			view = (View) inflate.inflate(R.layout.addservices_list_company_agent,
					null);
			viewHolder.tv_name = (TextView) view
					.findViewById(R.id.tv_addservices_agent_name);
			viewHolder.iv_phone = (ImageView) view
					.findViewById(R.id.iv_addservices_agent_phone);
			viewHolder.lay_company = (LinearLayout) view
					.findViewById(R.id.lay_addservices_company_name);
			viewHolder.tv_company = (TextView) view
					.findViewById(R.id.tv_addservices_compangy_name);
			viewHolder.tv_year = (TextView) view
					.findViewById(R.id.tv_addservices_agent_year);
			viewHolder.tv_deal = (TextView) view
					.findViewById(R.id.tv_addservices_deal);
			viewHolder.iv_pic = (ImageView) view
					.findViewById(R.id.iv_addservices_company_pic);
			viewHolder.iv_head = (ImageView) view
					.findViewById(R.id.iv_addservices_company_head);
			viewHolder.lay_economic_company_head = (LinearLayout) view
					.findViewById(R.id.lay_addservices_company_head);
			viewHolder.lay_economic_company_pic = (LinearLayout) view
					.findViewById(R.id.lay_addservices_company_pic);
			viewHolder.tv_addservices_pic_flag=(TextView) view.findViewById(R.id.tv_addservices_pic_flag);
			viewHolder.lay_addservices_type=(LinearLayout) view.findViewById(R.id.lay_addservices_type);
			viewHolder.tv_addservices_type=(TextView) view.findViewById(R.id.tv_addservices_type);
			viewHolder.badgeView = new BadgeView(mContext, viewHolder.iv_head);
			viewHolder.badgeView
					.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
			viewHolder.badgeView.setBackgroundResource(R.drawable.oval_v);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		if (str_url_flag.get(0).equals("0")) {
			viewHolder.iv_pic.setImageResource(R.drawable.logo);
			viewHolder.lay_economic_company_pic.setVisibility(View.VISIBLE);
			viewHolder.lay_economic_company_head.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(str_url.get(position), viewHolder.iv_pic, options);
			viewHolder.tv_year.setText(str_customer_name.get(position)+"     " + str[Integer.valueOf(str_type.get(position))-1]);
			viewHolder.tv_deal.setText("客户行业:" + str_deal.get(position)+"   "+str_year.get(position));
			viewHolder.lay_company.setVisibility(View.GONE);
			viewHolder.iv_phone.setVisibility(View.GONE);
			viewHolder.lay_addservices_type.setVisibility(View.GONE);
			if (!str_img_all.get(position).equals("0")) {
				viewHolder.tv_addservices_pic_flag.setVisibility(View.VISIBLE);
			}else {
				viewHolder.tv_addservices_pic_flag.setVisibility(View.GONE);
			}
			
		} else {
			viewHolder.badgeView.show();
			viewHolder.iv_head.setImageResource(R.drawable.logo);
			viewHolder.lay_economic_company_pic.setVisibility(View.GONE);
			viewHolder.lay_economic_company_head.setVisibility(View.VISIBLE);
			viewHolder.lay_addservices_type.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(str_url.get(position), viewHolder.iv_head, options2);
			viewHolder.tv_year.setText("从业" + str_year.get(position) + "年");
			viewHolder.tv_deal.setText(str_deal.get(position));
			viewHolder.lay_company.setVisibility(View.VISIBLE);
			viewHolder.iv_phone.setVisibility(View.VISIBLE);
			viewHolder.tv_company.setText(str_company.get(position));
			viewHolder.iv_phone.setOnClickListener(new BtnClicklistener(
					position, viewHolder.iv_phone));
			viewHolder.tv_addservices_type.setText(str[Integer.valueOf(str_service_type.get(position))-1]);
			viewHolder.tv_addservices_pic_flag.setVisibility(View.GONE);
		}
		viewHolder.tv_name.setText(str_name.get(position));
		return view;
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
			// TODO Auto-generated method stub
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ str_phone.get(position)));
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			mContext.startActivity(intent);
		}
	}

	public class ViewHolder {
		private LinearLayout lay_company, lay_economic_company_head,
				lay_economic_company_pic,lay_addservices_type;
		private ImageView iv_phone, iv_pic, iv_head;
		private TextView tv_name, tv_company, tv_year, tv_deal,tv_addservices_type,tv_addservices_pic_flag;
		private BadgeView badgeView;
	}
}
