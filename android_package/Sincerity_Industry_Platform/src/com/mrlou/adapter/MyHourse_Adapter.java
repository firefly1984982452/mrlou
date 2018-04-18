package com.mrlou.adapter;

import java.util.ArrayList;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mrlou.mine.My_HourseDetailAct;
import com.mrlou.mrlou.R;

public class MyHourse_Adapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ViewHolder viewHolder;
	private ArrayList<String> list_area, list_id, list_total, list_totals,
			list_code, list_area_name;
	private Context mContext;

	public MyHourse_Adapter(Context context, ArrayList<String> area_name,
			ArrayList<String> total, ArrayList<String> id,
			ArrayList<String> name, ArrayList<String> code,
			ArrayList<String> totals) {
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.list_area_name = area_name;
		this.list_total = total;
		this.list_id = id;
		this.list_area = name;
		this.list_code = code;
		this.list_totals = totals;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_id.size();
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
			view = inflater.inflate(R.layout.mine_myhourse_list, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_company = (TextView) view
					.findViewById(R.id.tv_myhourse);
			viewHolder.lv_hourse = (ListView) view
					.findViewById(R.id.lv_myhourse_item);
			viewHolder.lay_myhourse_list_item = (LinearLayout) view
					.findViewById(R.id.lay_myhourse_list_item);
			viewHolder.tv_myhourse_list_item = (TextView) view
					.findViewById(R.id.tv_myhourse_list_item);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		if (position >= 1) {
			if (list_code.get(position).equals(list_code.get(position - 1))) {
				viewHolder.lay_myhourse_list_item.setVisibility(View.GONE);
			} else {
				viewHolder.lay_myhourse_list_item.setVisibility(View.VISIBLE);
				viewHolder.tv_company.setText(list_area_name.get(position)
						+ "(" + list_total.get(position) + ")");
			}
		} else {
			viewHolder.lay_myhourse_list_item.setVisibility(View.VISIBLE);
			viewHolder.tv_company.setText(list_area_name.get(position) + "("
					+ list_total.get(position) + ")");
		}
		viewHolder.tv_myhourse_list_item.setText(list_area.get(position) + "("
				+ list_totals.get(position) + ")");
		return view;
	}

	public class ViewHolder {
		private TextView tv_company;
		private ListView lv_hourse;
		private LinearLayout lay_myhourse_list_item;
		private TextView tv_myhourse_list_item;
	}
}
