package com.mrlou.message;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrlou.mine.Select_CompanyAct;
import com.mrlou.mrlou.R;

/**
 * @author jx_chen
 * @date 2016-11-21---下午4:02:32
 * @problem
 */
public class Message_SelectAreaAdapter extends BaseAdapter {

	private ArrayList<String> str_name,list_flag;
	private Context mContext;
	private ViewHolder viewHolder;
	private LayoutInflater inflate;
	public Message_SelectAreaAdapter(Context context, ArrayList<String> name, ArrayList<String> flag) {
		this.mContext = context;
		this.inflate = LayoutInflater.from(context);
		this.str_name = name;
		this.list_flag=flag;
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
		if (view == null) {
			view = inflate.inflate(R.layout.message_select_areaitem, null);
			viewHolder = new ViewHolder();
			viewHolder.position = position;
			viewHolder.tv_name = (TextView) view
					.findViewById(R.id.tv_select_areaitem);
			viewHolder.iv_icon = (ImageView) view
					.findViewById(R.id.iv_select_area_icon);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tv_name.setText(str_name.get(position));
		set_flag(position,viewHolder.iv_icon);
		return view;
	}

	public class ViewHolder {
		int position;
		private TextView tv_name;
		private ImageView iv_icon;
	}

	public void set_flag(int position,View view){
		
		if (list_flag.get(position).equals("1")) {
			view.setBackgroundDrawable(mContext.getResources()
					.getDrawable(R.drawable.resourse_images_checked));
		}else {
			view.setBackgroundDrawable(mContext.getResources()
					.getDrawable(R.drawable.resourse_images_uncheck));
		}
	}
	
	
}
