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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mrlou.economic.Economic_CompanyAct;
import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class Agent_Adapter extends BaseAdapter implements HttpListener{

	private ArrayList<String> str_agent_url, str_agent_name, str_agent_company,
			str_agent_age, str_agent_deal, str_agent_phone, str_agent_status,
			str_agent_id;
	private Context mContext;
	private ViewHolder viewHolder;
	private int select_num;
	private String session_id;
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

	public Agent_Adapter(Context context, ArrayList<String> url,
			ArrayList<String> name, ArrayList<String> company,
			ArrayList<String> age, ArrayList<String> deal,
			ArrayList<String> phone, ArrayList<String> status,
			ArrayList<String> id,String session_id) {
		this.mContext = context;
		this.str_agent_url = url;
		this.str_agent_name = name;
		this.str_agent_company = company;
		this.str_agent_age = age;
		this.str_agent_deal = deal;
		this.str_agent_phone = phone;
		this.str_agent_status = status;
		this.str_agent_id = id;
		this.session_id=session_id;
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(mContext));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				// .displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
				.build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return str_agent_url.size();
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
			view = (View) inflate.inflate(R.layout.list_agent, null);
			viewHolder.iv_collect_head = (ImageView) view
					.findViewById(R.id.iv_collect_head);
			viewHolder.tv_name = (TextView) view
					.findViewById(R.id.tv_agent_name);
			viewHolder.tv_company = (TextView) view
					.findViewById(R.id.tv_collect_agent_company);
			viewHolder.tv_age = (TextView) view
					.findViewById(R.id.tv_collect_agent_workage);
			viewHolder.lay_collect_agent_company = (LinearLayout) view
					.findViewById(R.id.lay_collect_agent_company);
			viewHolder.lay_collect_phone = (LinearLayout) view
					.findViewById(R.id.lay_collect_phone);
			viewHolder.tv_deal = (TextView) view
					.findViewById(R.id.tv_collect_agent_deal);
			viewHolder.lay_collect_agent_del = (LinearLayout) view
					.findViewById(R.id.lay_collect_agent_del);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
//		if (!str_agent_url.get(position).equals("1")) {
			System.out.println("-----"+position);
			viewHolder.tv_name.setText(str_agent_name.get(position));
			viewHolder.tv_company.setText(str_agent_company.get(position));
			viewHolder.tv_age.setText("从业" + str_agent_age.get(position) + "年");
			viewHolder.tv_deal.setText(str_agent_deal.get(position));
			viewHolder.lay_collect_phone.setOnClickListener(new BtnClicklistener(
					position, viewHolder.lay_collect_phone));
			viewHolder.lay_collect_agent_company
					.setOnClickListener(new BtnClicklistener(position,
							viewHolder.lay_collect_agent_company));
			viewHolder.lay_collect_agent_del
					.setOnClickListener(new BtnClicklistener(position,
							viewHolder.lay_collect_agent_del));
			if (str_agent_status.get(position).equals("1")) {
				viewHolder.lay_collect_agent_company
						.setBackgroundDrawable(mContext.getResources().getDrawable(
								R.drawable.corner_view_red_2dp));
				viewHolder.tv_company.setTextColor(mContext.getResources()
						.getColor(R.color.white));
				viewHolder.tv_company.setLeft(10);
			} else {
				viewHolder.lay_collect_agent_company.setBackgroundDrawable(null);
				viewHolder.tv_company.setTextColor(mContext.getResources()
						.getColor(R.color.light_gray));
			}
			viewHolder.iv_collect_head.setImageResource(R.drawable.logo);
			ImageLoader.getInstance().displayImage(str_agent_url.get(position),
					viewHolder.iv_collect_head, options);
//		}
		
		return view;
	}

	public class ViewHolder {
		private TextView tv_company, tv_name, tv_age, tv_deal;
		private ImageView iv_collect_head;
		private LinearLayout lay_collect_phone, lay_collect_agent_company,
				lay_collect_agent_del;
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
			case R.id.lay_collect_agent_company:
				if (str_agent_status.get(position).equals("1")) {
					Intent i = new Intent(mContext, Economic_CompanyAct.class);
					i.putExtra("company", str_agent_company.get(position));
					mContext.startActivity(i);
				}
				break;

			case R.id.lay_collect_phone:
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ str_agent_phone.get(position)));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.startActivity(intent);
				break;
			case R.id.lay_collect_agent_del:
				select_num=position;
				select_cancelcolect(session_id, "5", str_agent_id.get(position));
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
				Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
				str_agent_age.remove(select_num);
				str_agent_company.remove(select_num);
				str_agent_deal.remove(select_num);
				str_agent_id.remove(select_num);
				str_agent_name.remove(select_num);
				str_agent_phone.remove(select_num);
				str_agent_status.remove(select_num);
				str_agent_url.remove(select_num);
//				str_agent_url.set(select_num, "1");
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
