package com.mrlou.message;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrlou.mrlou.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class Message_LxsAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> str_id, str_title, str_imgurl, str_content,
			str_news_type, str_link, str_createtime;
	private ViewHoler viewHoler;
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类
	
//	private ImageLoader imageLoader;

	public Message_LxsAdapter(Context context, ArrayList<String> id,
			ArrayList<String> title, ArrayList<String> imgurl,
			ArrayList<String> content, ArrayList<String> news_type,
			ArrayList<String> link, ArrayList<String> createtime) {
		this.mContext = context;
		this.str_id = id;
		this.str_title = title;
		this.str_imgurl = imgurl;
		this.str_content = content;
		this.str_news_type = news_type;
		this.str_link = link;
		this.str_createtime = createtime;
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));
		// ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mContext));
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
//		.displayer(new RoundedBitmapDisplayer(5)) // 设置成圆角图片
		.build();

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return str_id.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (view == null) {
			viewHoler = new ViewHoler();
			LayoutInflater inflate = ((Activity) mContext).getLayoutInflater();
			view = (View) inflate.inflate(R.layout.message_lxs_list, null);
			viewHoler.tv_time = (TextView) view
					.findViewById(R.id.tv_message_list_time);
			viewHoler.tv_bulider = (TextView) view
					.findViewById(R.id.tv_message_item_builder);
			viewHoler.tv_content = (TextView) view
					.findViewById(R.id.tv_message_item_content);
			viewHoler.iv_builder = (ImageView) view
					.findViewById(R.id.iv_message_item_bulider);
			viewHoler.lay_message_lxs_list_item = (LinearLayout) view
					.findViewById(R.id.lay_message_lxs_list_item);
			viewHoler.lay_message_lsit_time = (LinearLayout) view
					.findViewById(R.id.lay_message_lsit_time);
			view.setTag(viewHoler);
		} else {
			viewHoler = (ViewHoler) view.getTag();
		}
		if (position >= 1) {
			if (str_createtime.get(position).equals(
					str_createtime.get(position - 1))) {
				viewHoler.lay_message_lsit_time.setVisibility(View.GONE);
			} else {
				viewHoler.lay_message_lsit_time.setVisibility(View.VISIBLE);
			}
		}
		ImageLoader.getInstance().displayImage(str_imgurl.get(position), viewHoler.iv_builder, options);
		// ImageLoader.getInstance().displayImage(str_imgurl.get(position),
		// viewHoler.iv_builder);
//		imageLoader.loadImage(str_imgurl.get(position), 70, 70,
//				viewHoler.iv_builder);
		viewHoler.tv_bulider.setText(str_title.get(position));
		viewHoler.tv_content.setText(str_content.get(position));
		viewHoler.tv_time.setText(str_createtime.get(position));

		return view;
	}

	class ViewHoler {
		private TextView tv_time;
		private TextView tv_bulider, tv_content;
		private ImageView iv_builder;
		private LinearLayout lay_message_lxs_list_item, lay_message_lsit_time;
	}
}
