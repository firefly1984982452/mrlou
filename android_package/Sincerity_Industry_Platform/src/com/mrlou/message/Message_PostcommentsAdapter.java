package com.mrlou.message;

import java.util.ArrayList;

import com.mrlou.mrlou.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Message_PostcommentsAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> str_content, str_values, str_name, str_time;

	public Message_PostcommentsAdapter(Context context,
			ArrayList<String> content, ArrayList<String> values,
			ArrayList<String> name, ArrayList<String> time) {
		this.mContext = context;
		this.str_content = content;
		this.str_values = values;
		this.str_name = name;
		this.str_time = time;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return str_content.size();
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
		ViewHoler viewHoler;
		if (view == null) {
			viewHoler = new ViewHoler();
			LayoutInflater inflate = ((Activity) mContext).getLayoutInflater();
			view = (View) inflate.inflate(R.layout.message_postcomments_list,
					null);
			viewHoler.tv_content = (TextView) view
					.findViewById(R.id.tv_message_postcomments_content);
			viewHoler.tv_values = (TextView) view
					.findViewById(R.id.tv_message_postcomments_values);
			viewHoler.tv_name = (TextView) view
					.findViewById(R.id.tv_message_postcomments_name);
			viewHoler.tv_time = (TextView) view
					.findViewById(R.id.tv_message_postcomments_time);
			viewHoler.lay_values = (LinearLayout) view
					.findViewById(R.id.lay_message_postcomments_values);
			view.setTag(viewHoler);
		} else {
			viewHoler = (ViewHoler) view.getTag();
		}
		viewHoler.tv_content.setText(str_content.get(position));
		viewHoler.tv_name.setText(str_name.get(position));
		viewHoler.tv_time.setText(str_time.get(position));
		if (str_values.get(position).equals("0")) {
			viewHoler.lay_values.setVisibility(View.GONE);
		} else {
			viewHoler.lay_values.setVisibility(View.VISIBLE);
			viewHoler.tv_values.setText(str_values.get(position));
		}

		return view;
	}

	class ViewHoler {
		private LinearLayout lay_values;
		private TextView tv_content, tv_values, tv_name, tv_time;
	}
}
