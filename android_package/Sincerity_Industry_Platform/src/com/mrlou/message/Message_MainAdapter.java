package com.mrlou.message;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrlou.economic.view.BadgeView;
import com.mrlou.image.ImageLoader;
import com.mrlou.mrlou.R;

public class Message_MainAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> str_title, str_content, str_time, str_flag;
	private ViewHolder viewHolder;
	private int[] img = { R.drawable.lxs_hd, R.drawable.new_hourse,
			R.drawable.logo, R.drawable.values_hourse,R.drawable.single_hourse,
			R.drawable.properties, R.drawable.notice,R.drawable.my_post,R.drawable.lightings, R.drawable.client,
			 R.drawable.hourse_log, R.drawable.customer_log
			 };
	private int[] imgs = { R.drawable.lxs_hd, R.drawable.new_hourse,
			R.drawable.values_hourse, R.drawable.single_hourse,R.drawable.single_hourse,
			R.drawable.properties,R.drawable.notice, R.drawable.my_post,R.drawable.hourse_log };

	public Message_MainAdapter(Context context, ArrayList<String> title,
			ArrayList<String> content, ArrayList<String> time,
			ArrayList<String> flag) {
		this.mContext = context;
		this.str_title = title;
		this.str_content = content;
		this.str_time = time;
		this.str_flag = flag;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return str_title.size();
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
			LayoutInflater inflate = ((Activity) mContext).getLayoutInflater();
			view = (View) inflate.inflate(R.layout.message_list_main, null);
			viewHolder.iv_head = (ImageView) view
					.findViewById(R.id.iv_message_main);
			viewHolder.tv_title = (TextView) view
					.findViewById(R.id.tv_message_title);
			viewHolder.tv_time = (TextView) view
					.findViewById(R.id.tv_message_time);
			viewHolder.tv_content = (TextView) view
					.findViewById(R.id.tv_message_content);
			viewHolder.lay_message_main_pic = (LinearLayout) view
					.findViewById(R.id.lay_message_main_pic);
			
			viewHolder.badgeView = new BadgeView(mContext,
					viewHolder.lay_message_main_pic);
			viewHolder.badgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
			viewHolder.badgeView.setBadgeBackgroundColor(mContext.getResources().getColor(R.color.red));
			viewHolder.badgeView.setText("1");
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tv_title.setText(str_title.get(position));
		viewHolder.tv_time.setText(str_time.get(position));
		viewHolder.tv_content.setText(str_content.get(position));
		if (str_flag.size()<12) {
			viewHolder.iv_head.setImageResource(imgs[position]);
		}else {
			viewHolder.iv_head.setImageResource(img[position]);
		}
		viewHolder.iv_head.setImageResource(img[position]);
		
		if (str_flag.get(position).equals("0")) {
			viewHolder.badgeView.hide();
		} else {
			viewHolder.badgeView.show();
		}
		return view;
	}

	public class ViewHolder {
		private ImageView iv_head;
		private TextView tv_title, tv_content, tv_time;
		private BadgeView badgeView;
		private LinearLayout lay_message_main_pic;
	}
}
