package com.mrlou.addservices;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrlou.mine.New_EditInfoAct;
import com.mrlou.mrlou.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class AddServices_History_Adapter extends BaseAdapter {

	private ArrayList<String> list_name, list_type, list_square_meter,
			list_time, list_industry, list_status, list_url, list_proof,
			list_proof_id, list_contact, list_flag, list_id;
	private Context mContext;
	private int[] drawable = { R.drawable.kehu_images_looking,
			R.drawable.kehu_images_want, R.drawable.kehu_images_dealed,
			R.drawable.kehu_images_disabled };
	private ViewHolder viewHolder;
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

	public AddServices_History_Adapter(Context context, ArrayList<String> id,
			ArrayList<String> name, ArrayList<String> job_type,
			ArrayList<String> square_meter, ArrayList<String> time,
			ArrayList<String> industry, ArrayList<String> status,
			ArrayList<String> flag, ArrayList<String> url,
			ArrayList<String> proof, ArrayList<String> proof_id,
			ArrayList<String> contact) {
		this.mContext = context;
		this.list_id = id;
		this.list_name = name;
		this.list_type = job_type;
		this.list_square_meter = square_meter;
		this.list_time = time;
		this.list_industry = industry;
		this.list_status = status;
		this.list_flag = flag;
		this.list_url = url;
		this.list_proof = proof;
		this.list_proof_id = proof_id;
		this.list_contact = contact;
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(mContext));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.logo) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.logo) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.logo) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
				.build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_name.size();
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
			view = (View) inflate.inflate(R.layout.addservices_list_history,
					null);
			viewHolder = new ViewHolder();
			viewHolder.tv_history_flag = (LinearLayout) view
					.findViewById(R.id.tv_addservices_histroy_edit);
			viewHolder.tv_history_content = (TextView) view
					.findViewById(R.id.tv_addservices_history_content);
			viewHolder.tv_history_name = (TextView) view
					.findViewById(R.id.tv_addservices_history_name);
			viewHolder.tv_history_jobtype = (TextView) view
					.findViewById(R.id.tv_addservices_history_jobtype);
			viewHolder.iv_history = (ImageView) view
					.findViewById(R.id.iv_addservices_history);
			viewHolder.iv_history_label = (ImageView) view
					.findViewById(R.id.iv_addservices_history_label);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		if (list_flag.get(position).equals("1")) {
			viewHolder.tv_history_flag.setVisibility(View.VISIBLE);
		} else {
			viewHolder.tv_history_flag.setVisibility(View.GONE);
		}
		if (list_status.get(position).equals("1")) {
			viewHolder.iv_history_label.setBackgroundResource(drawable[0]);
		} else if (list_status.get(position).equals("2")) {
			viewHolder.iv_history_label.setBackgroundResource(drawable[1]);
		} else if (list_status.get(position).equals("3")) {
			viewHolder.iv_history_label.setBackgroundResource(drawable[2]);
		} else {
			viewHolder.iv_history_label.setBackgroundResource(drawable[3]);
		}
		viewHolder.iv_history.setImageResource(R.drawable.ic_launcher);
		ImageLoader.getInstance().displayImage(list_url.get(position),
				viewHolder.iv_history, options);
		// imageLoader.loadImage(list_url.get(position), 120, 120,
		// viewHolder.iv_history);
		// viewHolder.tv_history_flag.setOnClickListener(new BtnClicklistener(
		// position, viewHolder.tv_history_flag));
		if (list_type.get(position).equals("1")) {
			viewHolder.tv_history_content.setText("租赁     "
					+ list_square_meter.get(position) + "㎡   "
					+ list_time.get(position));
		} else {
			viewHolder.tv_history_content.setText("出售     "
					+ list_square_meter.get(position) + "㎡   "
					+ list_time.get(position));
		}
		viewHolder.tv_history_jobtype.setText("客户行业:   "
				+ list_industry.get(position));
		viewHolder.tv_history_name.setText(list_name.get(position));
		return view;
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
			// TODO Auto-generated method stub
			Intent intent = new Intent(mContext, New_EditInfoAct.class);
			intent.putExtra("id", list_id.get(position));
			intent.putExtra("trans_type", list_type.get(position));
			intent.putExtra("building_name", list_name.get(position));
			intent.putExtra("squre_meter", list_square_meter.get(position));
			intent.putExtra("cj_time", list_time.get(position));
			intent.putExtra("industry", list_industry.get(position));
			intent.putExtra("contact", list_contact.get(position));
			intent.putExtra("avatars", list_proof.get(position));
			intent.putExtra("avatars_id", list_proof_id.get(position));
			mContext.startActivity(intent);
		}
	}

	public class ViewHolder {
		private LinearLayout tv_history_flag;
		private TextView tv_history_name, tv_history_jobtype,
				tv_history_content;
		private ImageView iv_history, iv_history_label;
	}
}
