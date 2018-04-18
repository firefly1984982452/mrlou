package com.mrlou.adapter;

import java.util.ArrayList;
import u.aly.dr;

import com.mrlou.mrlou.R;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Customer_ManageAdapter extends BaseAdapter {

	private ArrayList<String> str_name, str_status,str_intro,str_time;
	private Context mContext;
	private int[] drawalbe = { R.drawable.kehu_images_looking,
			R.drawable.kehu_images_want, R.drawable.kehu_images_dealed,
			R.drawable.kehu_images_disabled };
	private TextView tv_customer_manager,tv_customer_content,tv_customer_time;
	private ImageView iv_kehu;
	public Customer_ManageAdapter(Context context, ArrayList<String> name,
			ArrayList<String> status,ArrayList<String> intro,ArrayList<String> time) {
		this.mContext = context;
		this.str_name = name;
		this.str_intro=intro;
		this.str_status = status;
		this.str_time=time;
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
		view = (View) inflate.inflate(R.layout.mine_list_customer_manage, null);
		tv_customer_manager = (TextView) view
				.findViewById(R.id.tv_customer_manage);
		tv_customer_manager.setText(str_name.get(position));
		iv_kehu=(ImageView) view.findViewById(R.id.iv_kehu_status);
		tv_customer_content=(TextView) view.findViewById(R.id.tv_customer_content);
		tv_customer_content.setText(str_intro.get(position));
		tv_customer_time=(TextView) view.findViewById(R.id.tv_customer_time);
		tv_customer_time.setText(str_time.get(position));
		if (str_status.get(position).equals("1")) {
			iv_kehu.setBackgroundResource(drawalbe[0]);
		}else if (str_status.get(position).equals("2")) {
			iv_kehu.setBackgroundResource(drawalbe[1]);
		}else if (str_status.get(position).equals("3")) {
			iv_kehu.setBackgroundResource(drawalbe[2]);
		}else {
			iv_kehu.setBackgroundResource(drawalbe[3]);
		}
		return view;
	}

}
