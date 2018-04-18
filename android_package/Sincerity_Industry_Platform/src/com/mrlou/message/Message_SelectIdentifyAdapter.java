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

/**
 *@author jx_chen
 *@date 2016-11-21---下午4:02:32
 *@problem
 */
public class Message_SelectIdentifyAdapter extends BaseAdapter{

	private ArrayList<String> str_name;
	private Context mContext;
	private TextView tv_name;
	public Message_SelectIdentifyAdapter(Context context,ArrayList<String> name){
		
		this.mContext=context;
		this.str_name=name;
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
		LayoutInflater inflate = ((Activity) mContext).getLayoutInflater();
		view = (View) inflate.inflate(R.layout.message_select_areaitem, null);
		tv_name=(TextView) view.findViewById(R.id.tv_select_areaitem);
		tv_name.setText(str_name.get(position));
		return view;
	}

}
