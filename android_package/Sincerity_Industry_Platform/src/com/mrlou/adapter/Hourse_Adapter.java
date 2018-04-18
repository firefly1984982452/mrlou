package com.mrlou.adapter;

import java.util.ArrayList;

import com.mrlou.mrlou.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class Hourse_Adapter extends BaseAdapter {

	private ArrayList<String> list_area;
	private ArrayList<String> list_hourse;
	private ArrayList<String> list_hourse_length;
	private Context mContext;

	public Hourse_Adapter(Context context, ArrayList<String> area,
			ArrayList<String> hourse,ArrayList<String> hourse_length) {
		this.mContext=context;
		this.list_area=area;
		this.list_hourse=hourse;
		this.list_hourse_length=hourse_length;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_area.size();
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
		view = (View) inflate.inflate(R.layout.mine_list_customer_manage, null);
		return view;
	}

}
