package com.mrlou.message;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mrlou.economic.view.BadgeView;
import com.mrlou.mrlou.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class Message_NewHourseAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<String>  str;
	private ArrayList<String> str_id, str_category, str_building_id,
			str_building_name, str_avatar, str_transaction_type, str_unit_no,
			str_status, str_verified, str_image_total, str_rent_price,
			str_sell_price, str_release_time, str_datetime, str_user_type,
			str_fee_rate, str_unique;
	private int[] drawable = { R.drawable.developers, R.drawable.agent02,
			R.drawable.operator, R.drawable.agent, R.drawable.property,
			R.drawable.other };
	private String type_id;
	private ViewHoler viewHoler;
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

	// private ImageLoader imageLoader;

	@SuppressWarnings("deprecation")
	public Message_NewHourseAdapter(Context context, ArrayList<String> id,
			ArrayList<String> category, ArrayList<String> building_id,
			ArrayList<String> building_name, ArrayList<String> avatar,
			ArrayList<String> transaction_type, ArrayList<String> unit_no,
			ArrayList<String> status, ArrayList<String> verified,
			ArrayList<String> image_total, ArrayList<String> rent_price,
			ArrayList<String> sell_price, ArrayList<String> release_time,
			ArrayList<String> user_type,
			ArrayList<String> fee_rate, ArrayList<String> unique, String type_id) {
		this.mContext = context;
		this.str_id = id;
		this.str_category = category;
		this.str_building_id = building_id;
		this.str_building_name = building_name;
		this.str_avatar = avatar;
		this.str_transaction_type = transaction_type;
		this.str_unit_no = unit_no;
		this.str_status = status;
		this.str_verified = verified;
		this.str_image_total = image_total;
		this.str_rent_price = rent_price;
		this.str_sell_price = sell_price;
		this.str_release_time = release_time;
		this.str_user_type = user_type;
		this.str_fee_rate = fee_rate;
		this.str_unique = unique;
		this.type_id=type_id;
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(mContext));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
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
		return str_avatar.size();
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
			view = (View) inflate
					.inflate(R.layout.message_newhourse_list, null);
//			viewHoler.tv_time = (TextView) view
//					.findViewById(R.id.tv_message_newhourse_time);
			viewHoler.lv_mxs_item = (ListView) view
					.findViewById(R.id.lv_message_newhourse_item);
			viewHoler.tv_name = (TextView) view
					.findViewById(R.id.tv_message_newhourse_name);
			viewHoler.tv_area = (TextView) view
					.findViewById(R.id.tv_message_newhourse_area);
			viewHoler.iv_builder = (ImageView) view
					.findViewById(R.id.iv_message_newhourse);
			viewHoler.iv_builder.setScaleType(ScaleType.FIT_XY);
			viewHoler.tv_values = (TextView) view
					.findViewById(R.id.tv_message_newhourse_values);
			viewHoler.tv_times = (TextView) view
					.findViewById(R.id.tv_message_newhourse_times);
			// viewHoler.iv_flag = (ImageView) view
			// .findViewById(R.id.iv_message_newhourse_flag);
			viewHoler.iv_kehu_status = (ImageView) view
					.findViewById(R.id.iv_message_newhourse_flag);
			viewHoler.lay_message_newhourse = (LinearLayout) view
					.findViewById(R.id.lay_message_newhourse);
//			viewHoler.lay_message_newhourse_time = (LinearLayout) view
//					.findViewById(R.id.lay_message_newhourse_time);
			viewHoler.lay_message_newhourse_show = (LinearLayout) view
					.findViewById(R.id.lay_message_newhourse_show);
			viewHoler.badgeView1 = new BadgeView(mContext, viewHoler.iv_builder);
			viewHoler.iv_image_flag = (ImageView) view
					.findViewById(R.id.iv_list_newhourse_detail);
			// viewHoler.badgeView1.setBadgePosition(BadgeView.POSITION_BOTTOM);

			// int w =
			// View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
			// int h =
			// View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
			// viewHoler.iv_builder.measure(w, h);
			// int height = viewHoler.iv_builder.getMaxHeight();
			// System.out.println("height:----"+height);

			LayoutParams laParams = (LayoutParams) viewHoler.iv_builder
					.getLayoutParams();
			int height = laParams.height;
			viewHoler.badgeView1.setGravity(Gravity.BOTTOM);
			viewHoler.badgeView1.setBadgeMargin(0, height - 20);
			viewHoler.badgeView1
					.setBackgroundResource(R.drawable.authentication);
			viewHoler.badgeView2 = new BadgeView(mContext,
					viewHoler.lay_message_newhourse);
			viewHoler.badgeView2.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
			viewHoler.badgeView2.setBackgroundResource(R.drawable.sole);

			view.setTag(viewHoler);
		} else {
			viewHoler = (ViewHoler) view.getTag();
		}
//		if (str_total.get(position).equals("0")) {
//			viewHoler.lay_message_newhourse_time.setVisibility(View.VISIBLE);
//			viewHoler.tv_time.setText(str_date.get(position) + "  " + "发布认证房源("
//					+ str_total.get(position) + ")");
//			viewHoler.lay_message_newhourse_show.setVisibility(View.GONE);
//		} else {
//			viewHoler.lay_message_newhourse_show.setVisibility(View.VISIBLE);
//			if (!str_date.get(position).equals("")) {
//				viewHoler.lay_message_newhourse_time
//						.setVisibility(View.VISIBLE);
//				viewHoler.tv_time.setText(str_date.get(position) + "  "
//						+ "发布认证房源(" + str_total.get(position) + ")");
//			} else {
//				viewHoler.lay_message_newhourse_time.setVisibility(View.GONE);
//			}
			if (str_unique.get(position).equals("1")) {
				viewHoler.badgeView2.show();
			} else {
				viewHoler.badgeView2.hide();
			}
			viewHoler.badgeView1.show();
			viewHoler.iv_builder.setImageResource(R.drawable.ic_launcher);
			ImageLoader.getInstance().displayImage(str_avatar.get(position),
					viewHoler.iv_builder, options);
			// imageLoader.loadImage(str_avatar.get(position), 70, 70,
			// viewHoler.iv_builder);

			if (str_user_type.get(position).equals("1")) {
				viewHoler.iv_kehu_status.setBackgroundResource(drawable[0]);
			} else if (str_user_type.get(position).equals("2")) {
				viewHoler.iv_kehu_status.setBackgroundResource(drawable[1]);
			} else if (str_user_type.get(position).equals("3")) {
				viewHoler.iv_kehu_status.setBackgroundResource(drawable[2]);
			} else if (str_user_type.get(position).equals("102")) {
				viewHoler.iv_kehu_status.setBackgroundResource(drawable[3]);
			} else if (str_user_type.get(position).equals("103")) {
				viewHoler.iv_kehu_status.setBackgroundResource(drawable[4]);
			} else {
				viewHoler.iv_kehu_status.setBackgroundResource(drawable[5]);
			}
			viewHoler.tv_name.setText(str_building_name.get(position));
			viewHoler.tv_area.setText(str_unit_no.get(position));
			if (type_id.equals("299")) {
				viewHoler.tv_times.setText("");
			}else {
				viewHoler.tv_times.setText(str_fee_rate.get(position));
			}
			
			if (str_transaction_type.get(position).equals("1")) {
				viewHoler.tv_values.setText("租金报价: "
						+ str_rent_price.get(position));
				viewHoler.iv_image_flag
				.setImageResource(R.drawable.lxs_images_rent);
			} else {
				viewHoler.tv_values.setText("出售报价: "
						+ str_sell_price.get(position));
				viewHoler.iv_image_flag
				.setImageResource(R.drawable.lxs_images_sale);
			}
		return view;
	}

	class ViewHoler {
//		private TextView tv_time;
		private ListView lv_mxs_item;
		private int num = 0;
		private LinearLayout lay_message_newhourse,
				lay_message_newhourse_show;
		private TextView tv_name, tv_area, tv_values, tv_times;
		private ImageView iv_builder, iv_flag, iv_kehu_status,iv_image_flag;
		private BadgeView badgeView1, badgeView2;
	}

}
