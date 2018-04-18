package com.mrlou.addservices;

import java.util.ArrayList;

import com.mrlou.mrlou.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> str_count, str_dw, str_type;
	private TextView tv_count, tv_dw, tv_type;

	public GridViewAdapter(Context context, ArrayList<String> count,
			ArrayList<String> dw, ArrayList<String> type) {

		this.mContext = context;
		this.str_count = count;
		this.str_dw = dw;
		this.str_type = type;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return str_count.size();
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
			view = (View) inflate.inflate(R.layout.economic_personal_list_item,
					null);
		}
		tv_count = (TextView) view.findViewById(R.id.tv_eco_person_count);
		tv_dw = (TextView) view.findViewById(R.id.tv_eco_person_dw);
		tv_type = (TextView) view.findViewById(R.id.tv_eco_person_type);
		
//		if (position==1) {
//			tv_count.setText(str_count.get(position)+"㎡");
//			tv_type.setText(str_type.get(position));
//		}else {
			tv_count.setText(str_count.get(position));
			tv_dw.setText(str_dw.get(position));
			tv_type.setText(str_type.get(position));
//		}
		
		
		return view;
	}

}
