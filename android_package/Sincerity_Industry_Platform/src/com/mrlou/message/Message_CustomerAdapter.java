package com.mrlou.message;

import java.util.ArrayList;

import com.mrlou.mrlou.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Message_CustomerAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> str_name, str_content, str_time;

	public Message_CustomerAdapter(Context context, ArrayList<String> name,
			ArrayList<String> content, ArrayList<String> time) {
		this.mContext = context;
		this.str_name = name;
		this.str_content = content;
		this.str_time = time;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return str_name.size();
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
		ViewHolder viewHolder;
		if (view == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflate = ((Activity) mContext).getLayoutInflater();
			view = (View) inflate.inflate(R.layout.message_customerlog_list,
					null);
			viewHolder.tv_content = (TextView) view
					.findViewById(R.id.tv_message_customer_content);
			viewHolder.tv_name = (TextView) view
					.findViewById(R.id.tv_message_customer_name);
			viewHolder.tv_time = (TextView) view
					.findViewById(R.id.tv_message_customer_time);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tv_content.setText(str_content.get(position));
		viewHolder.tv_name.setText(str_name.get(position));
		viewHolder.tv_time.setText(str_time.get(position));
		return view;
	}

	class ViewHolder {
		private TextView tv_name, tv_content, tv_time;
	}
}
