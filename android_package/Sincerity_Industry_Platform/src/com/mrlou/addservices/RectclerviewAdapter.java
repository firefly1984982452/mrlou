package com.mrlou.addservices;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrlou.mrlou.R;

public class RectclerviewAdapter extends RecyclerView.Adapter<RectclerviewAdapter.ViewHolder> {

	private Context mContext;
	private ArrayList<String> str_count, str_dw, str_type;


	public RectclerviewAdapter(Context context, ArrayList<String> count,
			ArrayList<String> dw, ArrayList<String> type) {
		this.mContext = context;
		this.str_count = count;
		this.str_dw = dw;
		this.str_type = type;
	}

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		return str_count.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		// TODO Auto-generated method stub
		viewHolder.tv_count.setText( str_count.get(position));
		viewHolder.tv_dw.setText(str_dw.get(position));
		viewHolder.tv_type.setText( str_type.get(position));
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup view, int position) {
		// TODO Auto-generated method stub
		LayoutInflater inflate = ((Activity) mContext).getLayoutInflater();
		view = (ViewGroup) inflate.inflate(R.layout.economic_personal_list_item, null);
		return new ViewHolder(view);
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		private TextView tv_count, tv_dw, tv_type;
		public ViewHolder(View itemView) {
			super(itemView);
			tv_count = (TextView) itemView
					.findViewById(R.id.tv_eco_person_count);
			tv_dw = (TextView) itemView.findViewById(R.id.tv_eco_person_dw);
			tv_type = (TextView) itemView.findViewById(R.id.tv_eco_person_type);
		}
	}
}
