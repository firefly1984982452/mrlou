package com.mrlou.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrlou.mrlou.R;

public class MyHourse_Item_Adapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ViewHolder viewHolder;
	private ArrayList<String> list_hourse;
	private ArrayList<String> list_hourse_length;
	private Context mContext;

	public MyHourse_Item_Adapter(Context context, ArrayList<String> hourse) {
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.list_hourse = hourse;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_hourse.size();
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
			view = inflater.inflate(R.layout.mine_myhourse_list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.lay_myhourse_list_item = (LinearLayout) view
					.findViewById(R.id.lay_myhourse_list_item);
			viewHolder.tv_myhourse_list_item = (TextView) view
					.findViewById(R.id.tv_myhourse_list_item);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tv_myhourse_list_item.setText(list_hourse.get(position));
		return view;
	}

	public class ViewHolder {
		private LinearLayout lay_myhourse_list_item;
		private TextView tv_myhourse_list_item;
	}
}
