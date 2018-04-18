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

public class AddServices_searchAdapter extends BaseAdapter {

	private TextView tv_econimic_search_content;
	private Context context;
	private ArrayList<String> str_search;

	public AddServices_searchAdapter(Context context, ArrayList<String> search) {

		this.context = context;
		this.str_search = search;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return str_search.size();
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
			LayoutInflater inflate = ((Activity) context).getLayoutInflater();
			view = (View) inflate.inflate(R.layout.economic_search_list,
					null);
		}
		tv_econimic_search_content=(TextView) view.findViewById(R.id.tv_econimic_search_content);
		tv_econimic_search_content.setText(str_search.get(position));
		return view;
	}

}
