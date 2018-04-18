package com.mrlou.message;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrlou.activity.AdActivity;
import com.mrlou.economic.view.BadgeView;
import com.mrlou.image.HttpUtil;
import com.mrlou.mrlou.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class Message_SingleBuilderAdapter extends BaseAdapter {

	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

	private Context mContext;
	private ArrayList<String> str_id, str_category, str_building_id,
			str_building_name, str_avatar, str_transaction_type, str_unit_no,
			str_status, str_verified, str_image_total, str_rent_price,
			str_sell_price, str_release_time, str_datetime, str_user_type,
			str_fee_rate, str_unique;
	private int[] drawable = { R.drawable.developers, R.drawable.agent02,
			R.drawable.operator, R.drawable.agent, R.drawable.property,
			R.drawable.other };
	private String type_id;

	public Message_SingleBuilderAdapter(Context context, ArrayList<String> id,
			ArrayList<String> category, ArrayList<String> building_id,
			ArrayList<String> building_name, ArrayList<String> avatar,
			ArrayList<String> transaction_type, ArrayList<String> unit_no,
			ArrayList<String> status, ArrayList<String> verified,
			ArrayList<String> image_total, ArrayList<String> rent_price,
			ArrayList<String> sell_price, ArrayList<String> release_time,
			ArrayList<String> datetime, ArrayList<String> user_type,
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
		this.str_datetime = datetime;
		this.str_user_type = user_type;
		this.str_fee_rate = fee_rate;
		this.str_unique = unique;
		this.type_id = type_id;
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(mContext));
		// imageLoader = new ImageLoader(new ImageDownloader() {
		// @Override
		// public Bitmap download(String path, int width, int height) {
		// return HttpUtil.download(path);
		// }
		// });
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
				.build();
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

	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHoler viewHoler;
		if (view == null) {
			viewHoler = new ViewHoler();
			LayoutInflater inflate = ((Activity) mContext).getLayoutInflater();
			view = (View) inflate.inflate(R.layout.message_singlebuilder_list,
					null);
			viewHoler.tv_area = (TextView) view
					.findViewById(R.id.tv_message_singlebuilder_area);
			viewHoler.tv_name = (TextView) view
					.findViewById(R.id.tv_message_singlebuilder_name);
			viewHoler.tv_time = (TextView) view
					.findViewById(R.id.tv_message_singlebuilder_time);
			viewHoler.tv_values = (TextView) view
					.findViewById(R.id.tv_message_singlebuilder_values);
			viewHoler.iv_pic01 = (ImageView) view
					.findViewById(R.id.iv_message_singlebuilder);
			viewHoler.iv_pic02 = (ImageView) view
					.findViewById(R.id.iv_message_singlebuilder_flag);
			viewHoler.lay_pic = (LinearLayout) view
					.findViewById(R.id.lay_message_singlebuilder);
			viewHoler.iv_image_flag = (ImageView) view
					.findViewById(R.id.iv_list_singlehourse_detail);
			viewHoler.badgeView = new BadgeView(mContext, viewHoler.iv_pic01);
			// viewHoler.badgeView.setBadgePosition(BadgeView.POSITION_BOTTOM);
			LayoutParams laParams = (LayoutParams) viewHoler.iv_pic01
					.getLayoutParams();
			int height = laParams.height;
			viewHoler.badgeView.setGravity(Gravity.BOTTOM);
			viewHoler.badgeView.setBadgeMargin(0, height - 20);
			viewHoler.badgeView
					.setBackgroundResource(R.drawable.authentication);
			viewHoler.badgeView.show();

			viewHoler.badgeView2 = new BadgeView(mContext, viewHoler.lay_pic);
			viewHoler.badgeView2.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
			viewHoler.badgeView2.setBackgroundResource(R.drawable.sole);
			view.setTag(viewHoler);
		} else {
			viewHoler = (ViewHoler) view.getTag();
		}
		viewHoler.iv_pic01.setImageResource(R.drawable.ic_launcher);

		ImageLoader.getInstance().displayImage(str_avatar.get(position),
				viewHoler.iv_pic01, options);
		// imageLoader.loadImage(str_avatar.get(position), 70, 70,
		// viewHoler.iv_pic01);

		if (str_unique.get(position).equals("1")) {
			viewHoler.badgeView2.show();
		} else {
			viewHoler.badgeView2.hide();
		}

		if (str_user_type.get(position).equals("1")) {
			viewHoler.iv_pic02.setBackgroundResource(drawable[0]);
		} else if (str_user_type.get(position).equals("2")) {
			viewHoler.iv_pic02.setBackgroundResource(drawable[1]);
		} else if (str_user_type.get(position).equals("3")) {
			viewHoler.iv_pic02.setBackgroundResource(drawable[2]);
		} else if (str_user_type.get(position).equals("102")) {
			viewHoler.iv_pic02.setBackgroundResource(drawable[3]);
		} else if (str_user_type.get(position).equals("103")) {
			viewHoler.iv_pic02.setBackgroundResource(drawable[4]);
		} else {
			viewHoler.iv_pic02.setBackgroundResource(drawable[5]);
		}
		viewHoler.tv_name.setText(str_building_name.get(position));
		viewHoler.tv_area.setText(str_unit_no.get(position));
		if (type_id.equals("299")) {
			viewHoler.tv_time.setText("");
		} else {
			viewHoler.tv_time.setText(str_fee_rate.get(position));
		}
		// viewHoler.tv_time.setText(str_fee_rate.get(position));
		if (str_transaction_type.get(position).equals("1")) {
			viewHoler.tv_values
					.setText("租金报价: " + str_rent_price.get(position));
			viewHoler.iv_image_flag
					.setImageResource(R.drawable.lxs_images_rent);
		} else {
			viewHoler.tv_values
					.setText("出售报价: " + str_sell_price.get(position));
			viewHoler.iv_image_flag
					.setImageResource(R.drawable.lxs_images_sale);
		}
		return view;
	}

	class ViewHoler {
		private TextView tv_time, tv_name, tv_area, tv_values;
		private ImageView iv_pic01, iv_pic02, iv_image_flag;
		private LinearLayout lay_pic;
		private BadgeView badgeView, badgeView2;
	}
}
