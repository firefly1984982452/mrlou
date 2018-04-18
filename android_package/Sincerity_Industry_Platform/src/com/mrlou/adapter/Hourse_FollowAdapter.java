package com.mrlou.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mrlou.mrlou.R;

public class Hourse_FollowAdapter extends BaseAdapter {

	private ArrayList<String> str_date;
	private ArrayList<String> str_content;
	private Context mContext;
	private TextView tv_date, tv_content;

	public Hourse_FollowAdapter(Context context, ArrayList<String> date,
			ArrayList<String> content) {
		this.mContext = context;
		this.str_content = content;
		this.str_date = date;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return str_date.size();
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
		LayoutInflater inflate = ((Activity) mContext).getLayoutInflater();
		view = (View) inflate.inflate(R.layout.mine_list_hourse_follow, null);
		tv_date = (TextView) view.findViewById(R.id.tv_date);
		tv_content = (TextView) view.findViewById(R.id.tv_content);
		tv_date.setText(str_date.get(position));
		tv_content.setText(str_content.get(position));
		return view;
	}

}
