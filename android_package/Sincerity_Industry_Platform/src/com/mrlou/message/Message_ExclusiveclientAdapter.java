package com.mrlou.message;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mrlou.economic.Economic_CompanyAct;
import com.mrlou.economic.Economic_personalAct;
import com.mrlou.economic.view.BadgeView;
import com.mrlou.mrlou.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class Message_ExclusiveclientAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String> str_id, str_title, str_imgurl, str_content,
			str_createtime, str_company, str_name, str_phone, str_work_age,
			str_company_status,str_v;
	private ViewHoler viewHoler;
	private BadgeView badgeView;
	// private ImageLoader imageLoader;
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

	public Message_ExclusiveclientAdapter(Context context,
			ArrayList<String> id, ArrayList<String> title,
			ArrayList<String> imgurl, ArrayList<String> content,
			ArrayList<String> name, ArrayList<String> company,
			ArrayList<String> createtime, ArrayList<String> phone,
			ArrayList<String> work_age, ArrayList<String> company_status,ArrayList<String> v) {
		this.mContext = context;
		this.str_id = id;
		this.str_title = title;
		this.str_imgurl = imgurl;
		this.str_content = content;
		this.str_name = name;
		this.str_company = company;
		this.str_phone = phone;
		this.str_work_age = work_age;
		this.str_createtime = createtime;
		this.str_company_status = company_status;
		this.str_v=v;
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(mContext));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.logo) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.logo) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.logo) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
				.build();
		// imageLoader = new ImageLoader(new ImageDownloader() {
		// @Override
		// public Bitmap download(String path, int width, int height) {
		// return HttpUtil.download(path);
		// }
		// });
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return str_id.size();
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
			viewHoler = new ViewHoler();
			LayoutInflater inflate = ((Activity) mContext).getLayoutInflater();
			view = (View) inflate.inflate(
					R.layout.message_exclusiveclient_list, null);
			viewHoler.tv_time = (TextView) view
					.findViewById(R.id.tv_message_exclusiveclient_time);
			viewHoler.tv_bulider = (TextView) view
					.findViewById(R.id.tv_exclusiveclient_builder);
			viewHoler.tv_content = (TextView) view
					.findViewById(R.id.tv_exclusiveclient_content);
			viewHoler.iv_builder = (ImageView) view
					.findViewById(R.id.iv_exclusiveclient_head);
			viewHoler.tv_name = (TextView) view
					.findViewById(R.id.tv_exclusiveclient_name);
			viewHoler.lay_exclusiveclient = (LinearLayout) view
					.findViewById(R.id.lay_exclusiveclient);
			viewHoler.tv_exclusiveclient_company = (TextView) view
					.findViewById(R.id.tv_exclusiveclient_company);
			viewHoler.tv_exclusiveclient_age = (TextView) view
					.findViewById(R.id.tv_exclusiveclient_age);
			viewHoler.tv_exclusiveclient_phone = (TextView) view
					.findViewById(R.id.tv_exclusiveclient_phone);
			viewHoler.lay_exclusiveclient_phone = (LinearLayout) view
					.findViewById(R.id.lay_exclusiveclient_phone);
			viewHoler.lay_exclusive_client = (LinearLayout) view
					.findViewById(R.id.lay_exclusive_client);
			viewHoler.lay_exclusiveclient_time = (LinearLayout) view
					.findViewById(R.id.lay_exclusiveclient_time);
			view.setTag(viewHoler);
		} else {
			viewHoler = (ViewHoler) view.getTag();
		}
		if (position >= 1) {
			if (str_createtime.get(position).equals(
					str_createtime.get(position - 1))) {
				viewHoler.lay_exclusiveclient_time.setVisibility(View.GONE);
			} else {
				viewHoler.lay_exclusiveclient_time.setVisibility(View.VISIBLE);
			}
		}
		badgeView = new BadgeView(mContext, viewHoler.iv_builder);
		badgeView.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
		badgeView.setBackgroundResource(R.drawable.oval_v);
		if (str_v.get(position).equals("1")) {
			badgeView.show();
		}else {
			badgeView.hide();
		}
		if (str_company_status.get(position).equals("0")) {
			viewHoler.lay_exclusiveclient.setBackgroundDrawable(null);
			viewHoler.tv_exclusiveclient_company.setTextColor(mContext
					.getResources().getColor(R.color.light_gray));
		} else {
			viewHoler.lay_exclusiveclient
					.setBackgroundDrawable(mContext.getResources().getDrawable(
							R.drawable.corner_view_red_2dp));
			viewHoler.tv_exclusiveclient_company.setTextColor(mContext
					.getResources().getColor(R.color.white));
			viewHoler.tv_exclusiveclient_company.setLeft(10);
		}
		viewHoler.tv_exclusiveclient_phone.setText(str_phone.get(position));
		viewHoler.tv_exclusiveclient_company.setText(str_company.get(position));
		viewHoler.tv_name.setText(str_name.get(position));
		viewHoler.tv_exclusiveclient_age.setText("从业"
				+ str_work_age.get(position) + "年");
		ImageLoader.getInstance().displayImage(str_imgurl.get(position),
				viewHoler.iv_builder, options);
		// imageLoader.loadImage(str_imgurl.get(position), 70, 70,
		// viewHoler.iv_builder);
		viewHoler.tv_bulider.setText(str_title.get(position));
		viewHoler.tv_content.setText(str_content.get(position));
		viewHoler.tv_time.setText(str_createtime.get(position));
		// 跳转经纪人
		viewHoler.lay_exclusive_client.setOnClickListener(new bt_onclick(
				position, viewHoler.lay_exclusive_client));
		// 跳转公司
		viewHoler.lay_exclusiveclient.setOnClickListener(new bt_onclick(
				position, viewHoler.lay_exclusiveclient));
		//拨打电话
		viewHoler.lay_exclusiveclient_phone.setOnClickListener(new bt_onclick(
				position, viewHoler.lay_exclusiveclient_phone));
		return view;
	}

	class ViewHoler {
		private TextView tv_time, tv_name, tv_exclusiveclient_company;
		private TextView tv_bulider, tv_content, tv_exclusiveclient_age,
				tv_exclusiveclient_phone;
		private ImageView iv_builder;
		private LinearLayout lay_exclusiveclient_time, lay_exclusiveclient,
				lay_exclusiveclient_phone, lay_exclusive_client;
	}

	class bt_onclick implements OnClickListener {

		private int position;
		private View view;

		public bt_onclick(int pos, View view) {
			this.position = pos;
			this.view = view;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.lay_exclusiveclient:
				if (str_company_status.get(position).equals("1")) {
					Intent intent = new Intent(mContext,
							Economic_CompanyAct.class);
					intent.putExtra("company", str_company.get(position));
					mContext.startActivity(intent);
				}
				break;

			case R.id.lay_exclusive_client:
				if (str_v.get(position).equals("1")) {
					Intent intents = new Intent(mContext,
							Economic_personalAct.class);
					intents.putExtra("id", str_id.get(position));
					mContext.startActivity(intents);
				}
				break;
			case R.id.lay_exclusiveclient_phone:
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ str_phone.get(position)));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
				break;
			}
		}

	}
}
