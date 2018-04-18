package com.mrlou.adapter;

import java.util.ArrayList;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrlou.mine.My_HourseDetailAct;
import com.mrlou.mrlou.R;

public class ChooseArea_Adapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ViewHolder viewHolder;
	private ArrayList<String> list_name, list_flag;
	private Context mContext;
	private ArrayList<String> list_content;
	private int flag_length = 0;

	public ChooseArea_Adapter(Context context, ArrayList<String> name,
			ArrayList<String> flag) {
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.list_name = name;
		this.list_flag = flag;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_flag.size();
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
			view = inflater.inflate(R.layout.mine_list_area_choose, null);
			viewHolder = new ViewHolder();
			viewHolder.position = position;
			viewHolder.tv_name = (TextView) view
					.findViewById(R.id.tv_area_choose_content);
			viewHolder.iv_icon = (ImageView) view
					.findViewById(R.id.iv_area_choose_icon);
			viewHolder.lay_icon = (LinearLayout) view
					.findViewById(R.id.lay_area_choose_icon);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.tv_name.setText(list_name.get(position));
		
		set_flag(position, viewHolder.iv_icon);
		
		viewHolder.lay_icon.setOnClickListener(new BtnClicklistener(position,
				viewHolder.iv_icon));
		
		return view;
	}

	public class ViewHolder {
		int position;
		private TextView tv_name;
		private ImageView iv_icon;
		private LinearLayout lay_icon;
	}

	public void set_flag(int position,View view){
		
		if (list_flag.get(position) == "1") {
			view.setBackgroundDrawable(mContext.getResources()
					.getDrawable(R.drawable.resourse_images_checked));
			flag_length++;
		}
	}
	
	class BtnClicklistener implements OnClickListener {

		private int position;
		private View view;

		public BtnClicklistener(int pos, View view) {
			this.position = pos;
			this.view = view;
		}

		@Override
		public void onClick(View v) {

			if (list_flag.get(position).equals("0")) {
				if (flag_length < 3) {
					view.setBackgroundDrawable(mContext.getResources()
							.getDrawable(R.drawable.resourse_images_checked));
					list_flag.set(position, "1");
					flag_length++;
				} else {
					Toast.makeText(mContext, "只能选择三个", 1000).show();
				}
			} else {
				view.setBackgroundDrawable(mContext.getResources().getDrawable(
						R.drawable.resourse_images_uncheck));
				list_flag.set(position, "0");
				flag_length--;
			}

		}

	}
}
