package com.mrlou.economic;

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

public class Eco_perListAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> str_name, str_detail, str_job, str_status,
			str_url, str_other;
	private TextView tv_name, tv_detail, tv_job;
//	private ImageLoader imageLoader;
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	private ImageView iv_economic_hour;

	public Eco_perListAdapter(Context context, ArrayList<String> name,
			ArrayList<String> detail, ArrayList<String> job,
			ArrayList<String> status, ArrayList<String> url,
			ArrayList<String> other) {

		this.mContext = context;
		this.str_name = name;
		this.str_detail = detail;
		this.str_job = job;
		this.str_status = status;
		this.str_url = url;
		this.str_other = other;
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));
//		imageLoader = new ImageLoader(new ImageDownloader() {
//			@Override
//			public Bitmap download(String path, int width, int height) {
//				return HttpUtil.download(path);
//			}
//		});
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
//		imageLoader
//				.loadImage(str_url.get(position), 140, 140, iv_economic_hour);
		tv_name = (TextView) view.findViewById(R.id.tv_economic_builder_name);
		tv_detail = (TextView) view.findViewById(R.id.tv_economic_detail);
		tv_job = (TextView) view.findViewById(R.id.tv_economic_job);
		tv_name.setText(str_name.get(position));
		if (str_status.get(position).equals("2")
				&& str_other.get(0).equals("1")) {
			tv_job.setText("客户行业：" + str_job.get(position));
			tv_detail.setText("出售    " + str_detail.get(position)+"成交");
		} else if (str_status.get(position).equals("1")
				&& str_other.get(0).equals("1")) {
			tv_job.setText("客户行业：" + str_job.get(position));
			tv_detail.setText("租赁    " + str_detail.get(position)+"成交");
		} else if (str_status.get(position).equals("2")
				&& str_other.get(0).equals("2")) {
			tv_job.setText("出售报价：" + str_job.get(position));
			tv_detail.setText(str_detail.get(position));
		} else {
			tv_job.setText("租金报价：" + str_job.get(position));
			tv_detail.setText( str_detail.get(position));
		}

		return view;
	}

}
