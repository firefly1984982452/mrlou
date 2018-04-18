package com.mrlou.favorites;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author jx_chen
 * @date 2016-10-17---下午4:02:19
 * @problem
 */
public class Collect_Adapter extends BaseAdapter {
	
	private Context mcontext;
	private ArrayList<String> str_url,str_building,str_area,str_price;
	public Collect_Adapter(Context convert, ArrayList<String> str_url,
			ArrayList<String> str_building, ArrayList<String> str_area,
			ArrayList<String> str_price) {
		// TODO Auto-generated constructor stub
		this.mcontext=convert;
		this.str_url=str_url;
		this.str_building=str_building;
		this.str_area=str_area;
		this.str_price=str_price;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return str_url.size();
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
		
		return view;
	}

}
