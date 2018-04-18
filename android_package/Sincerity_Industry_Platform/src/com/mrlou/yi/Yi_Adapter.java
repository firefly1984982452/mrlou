package com.mrlou.yi;

import java.util.ArrayList;

import com.mrlou.adapter.MyPost_Adapter.ViewHolder;
import com.mrlou.mrlou.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author jx_chen
 * @date 2016-10-17---下午4:02:19
 * @problem
 */
public class Yi_Adapter extends BaseAdapter {

	private Context mcontext;
	private ArrayList<String> str_id, str_user_id, str_avatar, str_username,
			str_title, str_subject, str_region,str_like_total,str_reply_total;
	private ViewHolder viewHolder;
	private int[] drawable = { R.drawable.ylb_hourse, R.drawable.ylb_customer,
			R.drawable.ylb_other };

	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

	public Yi_Adapter(Context convert, ArrayList<String> str_id,
			ArrayList<String> str_user_id, ArrayList<String> str_avatar,
			ArrayList<String> str_username, ArrayList<String> str_title,
			ArrayList<String> str_subject, ArrayList<String> str_region,ArrayList<String> reply_total,ArrayList<String> like_total) {
		// TODO Auto-generated constructor stub
		this.mcontext = convert;
		this.str_id = str_id;
		this.str_user_id = str_user_id;
		this.str_avatar = str_avatar;
		this.str_username = str_username;
		this.str_title = str_title;
		this.str_subject = str_subject;
		this.str_region = str_region;
		this.str_reply_total=reply_total;
		this.str_like_total=like_total;
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(mcontext));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.logo) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.logo) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.logo) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
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
			viewHolder = new ViewHolder();
			LayoutInflater inflate = ((Activity) mcontext).getLayoutInflater();
			view = (View) inflate.inflate(R.layout.yi_list_hourse, null);
			viewHolder.tv_message_count = (TextView) view
					.findViewById(R.id.tv_hourse_message_count);
			viewHolder.tv_price_count = (TextView) view
					.findViewById(R.id.tv_hourse_price_count);
			viewHolder.tv_tile = (TextView) view
					.findViewById(R.id.tv_hourse_content);
			viewHolder.tv_hourse_area = (TextView) view
					.findViewById(R.id.tv_hourse_area);
			viewHolder.iv_head = (ImageView) view
					.findViewById(R.id.iv_list_hourse_head);
			viewHolder.iv_flag = (ImageView) view
					.findViewById(R.id.iv_list_hourse_identity);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.iv_head.setImageResource(R.drawable.logo);
		ImageLoader.getInstance().displayImage(str_avatar.get(position),
				viewHolder.iv_head, options);
		viewHolder.tv_price_count.setText(str_like_total.get(position));
		viewHolder.tv_message_count.setText(str_reply_total.get(position));
		viewHolder.tv_tile.setText(str_title.get(position));
		viewHolder.tv_hourse_area.setText(str_region.get(position));
		if (str_subject.get(position).equals("1")) {
			viewHolder.iv_flag.setBackgroundResource(drawable[0]);
		} else if (str_subject.get(position).equals("2")) {
			viewHolder.iv_flag.setBackgroundResource(drawable[1]);
		} else {
			viewHolder.iv_flag.setBackgroundResource(drawable[2]);
		}
		return view;
	}

	public class ViewHolder {
		private TextView tv_tile, tv_hourse_area, tv_message_count,
				tv_price_count;
		private ImageView iv_head, iv_flag;
	}
}
