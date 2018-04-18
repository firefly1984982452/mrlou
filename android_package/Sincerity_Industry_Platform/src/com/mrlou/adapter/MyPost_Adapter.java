package com.mrlou.adapter;

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

public class MyPost_Adapter extends BaseAdapter implements HttpListener {

	private ArrayList<String> list_url, list_time, list_status, list_title,
			list_subject, list_cn_name, list_en_name, list_expire_time,
			list_id, list_flag;
	private String flag, session_id;
	private ViewHolder viewHolder;
	private Context mContext;
	private int[] drawable = { R.drawable.ylb_hourse, R.drawable.ylb_customer,
			R.drawable.ylb_other };
	private int select_num = 0;
	DisplayImageOptions options; // DisplayImageOptions是用于设置图片显示的类

	public MyPost_Adapter(Context context, ArrayList<String> id,
			ArrayList<String> url, ArrayList<String> flag,
			ArrayList<String> time, ArrayList<String> status,
			ArrayList<String> title, ArrayList<String> subject,
			ArrayList<String> cn_name, ArrayList<String> en_name,
			ArrayList<String> expire_time, String session_id) {
		this.mContext = context;
		this.list_id = id;
		this.list_url = url;
		this.list_flag = flag;
		this.list_time = time;
		this.list_status = status;
		this.list_title = title;
		this.list_subject = subject;
		this.list_cn_name = cn_name;
		this.list_en_name = en_name;
		this.list_expire_time = expire_time;
		this.session_id = session_id;
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(mContext));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.logo) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.logo) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.logo) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.build();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_url.size();
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
			viewHolder = new ViewHolder();
			LayoutInflater inflate = ((Activity) mContext).getLayoutInflater();
			view = (View) inflate.inflate(R.layout.mine_list_mypost, null);
			viewHolder.iv_head = (ImageView) view
					.findViewById(R.id.iv_list_mypost_head);
			viewHolder.iv_delete = (ImageView) view
					.findViewById(R.id.iv_mypost_flag);
			viewHolder.iv_identity = (ImageView) view
					.findViewById(R.id.iv_list_mypost_identity);
			viewHolder.tv_time = (TextView) view
					.findViewById(R.id.tv_mypost_time);
			viewHolder.tv_content = (TextView) view
					.findViewById(R.id.tv_mypost_content);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		viewHolder.iv_head.setImageResource(R.drawable.ic_launcher);
		ImageLoader.getInstance().displayImage(list_url.get(position), viewHolder.iv_head, options);
		// imageLoader.loadImage(list_url.get(position), 50, 50,
		// viewHolder.iv_head);
		if (list_subject.get(position).equals("1")) {
			viewHolder.iv_identity.setBackgroundResource(drawable[0]);
		} else if (list_subject.get(position).equals("2")) {
			viewHolder.iv_identity.setBackgroundResource(drawable[1]);
		} else {
			viewHolder.iv_identity.setBackgroundResource(drawable[2]);
		}
		viewHolder.tv_content.setText(list_title.get(position));

		if (list_flag.get(position) == "0") {
			viewHolder.iv_delete.setBackgroundDrawable(mContext.getResources()
					.getDrawable(R.drawable.mine_images_del));
			viewHolder.iv_delete.setOnClickListener(new BtnClicklistener(
					position, viewHolder.iv_delete));
		} else if (list_flag.get(position) == "1"
				&& list_status.get(position).equals("2")) {
			viewHolder.iv_delete.setBackgroundDrawable(null);
			viewHolder.iv_delete.setOnClickListener(null);
			viewHolder.tv_time.setText(list_expire_time.get(position) + "  失效");
		} else {
			viewHolder.tv_time.setText(list_time.get(position) + "  发布");
			viewHolder.iv_delete.setBackgroundDrawable(mContext.getResources()
					.getDrawable(R.drawable.mine_images_del));
			viewHolder.iv_delete.setOnClickListener(new BtnClicklistener(
					position, viewHolder.iv_delete));
		}

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
			select_num = position;
			if (list_flag.get(position).equals("0")) {
				select_cancelpost(session_id, "4", list_id.get(position));
			} else {
				select_modifypost(session_id, "2", list_id.get(position));
			}
		}
	}

	public class ViewHolder {
		private ImageView iv_head, iv_identity, iv_delete;
		private TextView tv_time, tv_content;
	}

	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Toast.makeText(mContext, "删除成功", 1000).show();
				list_id.remove(select_num);
				list_url.remove(select_num);
				list_time.remove(select_num);
				list_status.remove(select_num);
				list_title.remove(select_num);
				list_subject.remove(select_num);
				list_cn_name.remove(select_num);
				list_en_name.remove(select_num);
				list_expire_time.remove(select_num);
				notifyDataSetChanged();
				break;
			case 2:
				Toast.makeText(mContext, "暂无数据", 1000).show();
				break;
			case 3:
				list_status.set(select_num, "2");
				notifyDataSetChanged();
				break;
			case 99:
				Toast.makeText(mContext, "网络异常，请重新尝试连接", 1000).show();
				break;
			}
		}
	};

	public void select_cancelpost(String session_id, String type, String id) {
		Request request = Request.requestcancelpost(session_id, type, id);
		new HttpThread(request, this);
	}

	public void select_modifypost(String session_id, String status, String id) {
		Request request = Request.requestmodifypost(session_id, status, id);
		new HttpThread(request, this);
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
		if (reqID == Request.REQUEST_ECONOMIC_COMPANY) {
			JSONObject json;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					newHandler.sendEmptyMessage(3);
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
