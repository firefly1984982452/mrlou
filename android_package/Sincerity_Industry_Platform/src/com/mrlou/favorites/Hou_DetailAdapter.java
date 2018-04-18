package com.mrlou.favorites;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrlou.economic.Economic_CompanyAct;
import com.mrlou.economic.view.BadgeView;
import com.mrlou.listener.HttpListener;
import com.mrlou.mine.New_EditInfoAct;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class Hou_DetailAdapter extends BaseAdapter implements HttpListener {

	private ArrayList<String> str_id, str_category, str_building_id,
			str_building_name, str_avatar, str_transaction_type, str_unit_no,
			str_status, str_verified, str_image_total, str_rent_price,
			str_sell_price, str_unique;
	private java.net.URL url;
	private Context mContext;
	private int select_num;
	private String session_id;
	// private ImageLoader imageLoader;
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

	public Hou_DetailAdapter(Context context, ArrayList<String> id,
			ArrayList<String> category, ArrayList<String> building_id,
			ArrayList<String> building_name, ArrayList<String> avatar,
			ArrayList<String> transaction_type, ArrayList<String> unit_no,
			ArrayList<String> status, ArrayList<String> verified,
			ArrayList<String> image_total, ArrayList<String> rent_price,
			ArrayList<String> sell_price, ArrayList<String> unique,
			String session_id) {

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
		this.str_unique = unique;
		this.session_id = session_id;
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
		ViewHoler viewHoler;
		if (view == null) {
			viewHoler = new ViewHoler();
			LayoutInflater inflate = ((Activity) mContext).getLayoutInflater();
			view = (View) inflate.inflate(R.layout.list_collect_hourse_detail,
					null);
			viewHoler.lay_hourse_detail_pic = (LinearLayout) view
					.findViewById(R.id.lay_collecthourse_detail_pic);
			viewHoler.tv_hourse_detail_content = (TextView) view
					.findViewById(R.id.tv_collecthourse_detail_content);
			viewHoler.tv_hourse_detail_price = (TextView) view
					.findViewById(R.id.tv_collecthourse_detail_price);
			// viewHoler.tv_hourse_detail_time = (TextView) view
			// .findViewById(R.id.tv_collecthourse_detail_time);
			viewHoler.lay_collecthourse_detail_del = (LinearLayout) view
					.findViewById(R.id.lay_collecthourse_detail_del);
			viewHoler.iv_hourse_detail_pic = (ImageView) view
					.findViewById(R.id.iv_collecthourse_detail_pic);
			viewHoler.iv_list_hourse_detail = (ImageView) view
					.findViewById(R.id.iv_collectlist_hourse_detail);
			viewHoler.badgeView = new BadgeView(mContext,
					viewHoler.iv_hourse_detail_pic);
			// viewHoler.badgeView.setBadgePosition(BadgeView.POSITION_BOTTOM);
			LayoutParams laParams = (LayoutParams) viewHoler.iv_hourse_detail_pic
					.getLayoutParams();
			int height = laParams.height;
			viewHoler.badgeView.setGravity(Gravity.BOTTOM);
			viewHoler.badgeView.setBadgeMargin(0, height - 15);
			// viewHoler.badgeView.setBadgeMargin(0, 80);
			viewHoler.badgeView
					.setBackgroundResource(R.drawable.authentication);
			viewHoler.badgeView2 = new BadgeView(mContext,
					viewHoler.lay_hourse_detail_pic);
			viewHoler.badgeView2.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
			viewHoler.badgeView2.setBackgroundResource(R.drawable.sole);
			view.setTag(viewHoler);
		} else {
			viewHoler = (ViewHoler) view.getTag();
		}
		viewHoler.lay_collecthourse_detail_del
				.setOnClickListener(new BtnClicklistener(position,
						viewHoler.lay_collecthourse_detail_del));
		// if (!str_avatar.get(position).equals("1")) {
		if (str_verified.get(position).equals("1")) {
			viewHoler.badgeView.show();
		} else {
			viewHoler.badgeView.hide();
		}
		if (str_unique.get(position).equals("1")) {
			viewHoler.badgeView2.show();
		} else {
			viewHoler.badgeView2.hide();
		}
		if (str_transaction_type.get(position).equals("1")) {
			viewHoler.tv_hourse_detail_price.setText("租金报价:" + "   "
					+ str_rent_price.get(position));
			viewHoler.iv_list_hourse_detail
					.setImageResource(R.drawable.lxs_images_rent);
		} else {
			viewHoler.tv_hourse_detail_price.setText("出售报价:" + "   "
					+ str_sell_price.get(position));
			viewHoler.iv_list_hourse_detail
					.setImageResource(R.drawable.lxs_images_sale);
		}
		viewHoler.tv_hourse_detail_content.setText(str_unit_no.get(position));
		viewHoler.iv_hourse_detail_pic.setImageResource(R.drawable.ic_launcher);
		ImageLoader.getInstance().displayImage(str_avatar.get(position),
				viewHoler.iv_hourse_detail_pic, options);
		// viewHoler.tv_hourse_detail_time.setText(str_time.get(position));
		// }

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
			switch (v.getId()) {
			case R.id.lay_collecthourse_detail_del:
				select_num = position;
				select_cancelcolect(session_id, "3",
						str_id.get(position));
				break;
			}

		}
	}

	public void select_cancelcolect(String session_id, String type, String id) {
		Request request = Request.requestcancelpost(session_id, type, id);
		new HttpThread(request, this);
	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
//				Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
				str_avatar.remove(select_num);
				str_building_id.remove(select_num);
				str_building_name.remove(select_num);
				str_category.remove(select_num);
				str_id.remove(select_num);
				str_image_total.remove(select_num);
				str_rent_price.remove(select_num);
				str_sell_price.remove(select_num);
				str_status.remove(select_num);
				str_transaction_type.remove(select_num);
				str_unique.remove(select_num);
				str_unit_no.remove(select_num);
				str_verified.remove(select_num);
				// str_avatar.set(select_num, "1");
				notifyDataSetChanged();
				break;
			case 2:
				Toast.makeText(mContext, "暂无数据", Toast.LENGTH_SHORT).show();
				break;
			case 99:
				Toast.makeText(mContext, "网络异常，请重新尝试连接", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	class ViewHoler {
		private TextView tv_hourse_detail_content, tv_hourse_detail_price;
		private ImageView iv_hourse_detail_pic, iv_list_hourse_detail;
		private LinearLayout lay_hourse_detail_pic,
				lay_collecthourse_detail_del;
		private BadgeView badgeView, badgeView2;
	}

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_NEW) {
			JSONObject json;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					newHandler.sendEmptyMessage(1);
				} else if (errorid.equals("1")) {
					newHandler.sendEmptyMessage(2);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void doError(String s) {
		// TODO Auto-generated method stub
		if (s.equals("请求异常")) {
			newHandler.sendEmptyMessage(99);
		}
	}
}
