package com.mrlou.addservices;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrlou.economic.view.XCRoundRectImageView;
import com.mrlou.image.HttpUtil;
import com.mrlou.mrlou.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class AddServices_perListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> str_name, str_detail, str_job,
			str_url,str_customer_name,str_type;
	private TextView tv_name, tv_detail, tv_job;
//	private ImageLoader imageLoader;
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private ImageView iv_economic_hour;
	private String[] str=new String[]{"装修设计","办公家具","办公用品","代理注册","金融服务","广告营销 ","法律咨询","猎头招聘","活动拓展","礼品福利","技术服务 "};
	

	public AddServices_perListAdapter(Context context, ArrayList<String> name,
			ArrayList<String> detail, ArrayList<String> job,
			 ArrayList<String> url,
			ArrayList<String> customer_name,ArrayList<String> type) {
		this.mContext = context;
		this.str_name = name;
		this.str_detail = detail;
		this.str_job = job;
		this.str_url = url;
		this.str_customer_name=customer_name;
		this.str_type=type;
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
		.showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
		.showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
		.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
		.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
		.displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
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
			LayoutInflater inflate = ((Activity) mContext).getLayoutInflater();
			view = (View) inflate.inflate(R.layout.economic_list_hour, null);
		}
		iv_economic_hour = (ImageView) view
				.findViewById(R.id.iv_economic_hour);
		ImageLoader.getInstance().displayImage(str_url.get(position), iv_economic_hour, options);
		tv_name = (TextView) view.findViewById(R.id.tv_economic_builder_name);
		tv_detail = (TextView) view.findViewById(R.id.tv_economic_detail);
		tv_job = (TextView) view.findViewById(R.id.tv_economic_job);
		tv_name.setText(str_name.get(position));
		tv_job.setText("客户行业：" + str_job.get(position)+"  "+str_detail.get(position)+"成交");
		if (!str_type.get(position).equals("0")) {
			tv_detail.setText(str_customer_name.get(position) +"    "+ str[Integer.valueOf(str_type.get(position))-1]);
		}else {
			tv_detail.setText(str_customer_name.get(position));
		}
		return view;
	}

}
