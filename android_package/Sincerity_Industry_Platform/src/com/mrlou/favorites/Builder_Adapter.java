package com.mrlou.favorites;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class Builder_Adapter extends BaseAdapter implements HttpListener {

	private ArrayList<String> str_name;
	private ArrayList<String> str_address;
	private ArrayList<String> str_amount, str_id;
	private Context mContext;
	private ViewHolder viewHolder;
	private ArrayList<String> str_url;
	private String session_id;
	private int select_num = 0;
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

	public Builder_Adapter(Context context, ArrayList<String> url,
			ArrayList<String> name, ArrayList<String> address,
			ArrayList<String> amount, ArrayList<String> id,String session_id) {
		this.mContext = context;
		this.str_url = url;
		this.str_name = name;
		this.str_address = address;
		this.str_amount = amount;
		this.str_id = id;
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
		return str_url.size();
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
			viewHolder = new ViewHolder();
			view = (View) inflate.inflate(R.layout.list_builder, null);
			viewHolder.iv_collect_building = (ImageView) view
					.findViewById(R.id.iv_collect_building);
			viewHolder.tv_company = (TextView) view
					.findViewById(R.id.tv_collect_building);
			viewHolder.tv_area = (TextView) view
					.findViewById(R.id.tv_collect_area);
			viewHolder.tv_price = (TextView) view
					.findViewById(R.id.tv_collect_price);
			viewHolder.lay_collect_del=(LinearLayout) view.findViewById(R.id.lay_collect_del);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
//		if (!str_url.get(position).equals("1")) {
			viewHolder.tv_company.setText(str_name.get(position));
			viewHolder.tv_area.setText(str_address.get(position));
			viewHolder.tv_price.setText(str_amount.get(position));
			viewHolder.lay_collect_del.setOnClickListener(new BtnClicklistener(position, viewHolder.lay_collect_del));
			viewHolder.iv_collect_building.setImageResource(R.drawable.ic_launcher);
			ImageLoader.getInstance().displayImage(str_url.get(position),
					viewHolder.iv_collect_building, options);
//		}
		return view;
	}

	public class ViewHolder {
		private TextView tv_company, tv_area, tv_price;
		private ImageView  iv_collect_building;
		private LinearLayout lay_collect_del;
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
			select_num = position;
			select_cancelcolect(session_id, "2", str_id.get(position));
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
				str_address.remove(select_num);
				str_amount.remove(select_num);
				str_id.remove(select_num);
				str_name.remove(select_num);
				str_url.remove(select_num);
//				str_url.set(select_num, "1");
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
