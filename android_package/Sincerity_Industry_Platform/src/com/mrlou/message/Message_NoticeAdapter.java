package com.mrlou.message;

import java.util.ArrayList;

import com.mrlou.mrlou.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Message_NoticeAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> str_id, str_content,
			str_createtime,str_title,str_data;
	private ViewHoler viewHoler;

	public Message_NoticeAdapter(Context context, ArrayList<String> data,ArrayList<String> id,
			ArrayList<String> title,
			ArrayList<String> content, ArrayList<String> createtime) {
		this.mContext = context;
		this.str_data=data;
		this.str_id = id;
		this.str_title=title;
		this.str_content = content;
		this.str_createtime = createtime;
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
			view = (View) inflate.inflate(R.layout.message_notice_list, null);
			viewHoler.tv_time = (TextView) view
					.findViewById(R.id.tv_message_notice_time);
			viewHoler.tv_title = (TextView) view
					.findViewById(R.id.tv_message_notice_title);
			viewHoler.tv_content = (TextView) view
					.findViewById(R.id.tv_message_notice_content);
			viewHoler.lin_time = (LinearLayout) view
					.findViewById(R.id.lay_message_notice_time);
			view.setTag(viewHoler);
		} else {
			viewHoler = (ViewHoler) view.getTag();
		}
			if (str_data.get(position).equals("")) {
				viewHoler.lin_time.setVisibility(View.GONE);
			} else {
				viewHoler.lin_time.setVisibility(View.VISIBLE);
			}
		viewHoler.tv_time.setText(str_createtime.get(position));
		viewHoler.tv_title.setText(str_title.get(position));
		viewHoler.tv_content.setText(str_content.get(position));
		return view;
	}

	class ViewHoler {
		private TextView tv_time, tv_title, tv_content;
		private LinearLayout lin_time;
	}
}
