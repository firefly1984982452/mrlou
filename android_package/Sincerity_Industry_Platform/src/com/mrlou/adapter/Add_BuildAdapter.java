package com.mrlou.adapter;

import java.util.ArrayList;

import com.mrlou.mrlou.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Add_BuildAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> str_content;
	private TextView tv_content;
	public Add_BuildAdapter(Context context, ArrayList<String> str_content) {

		this.mContext = context;
		this.str_content = str_content;
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
		if (view == null) {
			LayoutInflater inflate = ((Activity) mContext).getLayoutInflater();
			view = (View) inflate.inflate(R.layout.mine_addbuilding_list, null);
			tv_content=(TextView) view.findViewById(R.id.tv_addbuilding_content);
		}
		tv_content.setText(str_content.get(position));
		return view;
	}

}
