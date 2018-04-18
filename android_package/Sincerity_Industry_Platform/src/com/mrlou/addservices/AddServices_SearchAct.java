package com.mrlou.addservices;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mrlou.listener.HttpListener;
import com.mrlou.mine.Choose_StutaAct;
import com.mrlou.mrlou.R;
import com.mrlou.util.BaseHelper;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.mrlou.write.sdcard.InputUtil;
import com.mrlou.write.sdcard.OutputUtil;

public class AddServices_SearchAct extends Activity implements HttpListener {

	private LinearLayout lay_search_history, lay_economic_search_back;
	private ListView lv_search, lv_history;
	private EditText et_search;
	private ArrayList<String> str_search, str_id, str_history, str_name,
			str_names, str_phone, str_phones, str_flag, str_ids, str_company;
	private AddServices_searchAdapter adapter, adapter2;
	private String str, str_02;
	private String search, search2, id, company;
	private AddServices_SearchUtil search_util;
	private List<AddServices_SearchUtil> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.economic_search);
		// 生成Economic_SearchUtil表
		prepare();
		readListFromSDcard();
		findView();

	}

	private void prepare() {
		// TODO Auto-generated method stub
		str_id = new ArrayList<String>();
		str_search = new ArrayList<String>();
		str_history = new ArrayList<String>();
		list = new ArrayList<AddServices_SearchUtil>();
		str_name = new ArrayList<String>();
		str_phone = new ArrayList<String>();
		str_flag = new ArrayList<String>();
		str_names = new ArrayList<String>();
		str_phones = new ArrayList<String>();
		str_ids = new ArrayList<String>();
		str_company = new ArrayList<String>();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay_economic_search_back = (LinearLayout) findViewById(R.id.lay_economic_search_back);
		lay_economic_search_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		lay_search_history = (LinearLayout) findViewById(R.id.lay_search_history);
		lv_search = (ListView) findViewById(R.id.lv_economic_search);
		et_search = (EditText) findViewById(R.id.et_economic_search);
		et_search.addTextChangedListener(mTextWatcher);
		adapter = new AddServices_searchAdapter(AddServices_SearchAct.this,
				str_search);
		lv_search.setAdapter(adapter);
		lv_search.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (search.equals("")) {
					write("0", position);
					Intent intent = new Intent(AddServices_SearchAct.this,
							AddServices_personalAct.class);
					intent.putExtra("id", str_id.get(position));
					startActivity(intent);
				} else if (position + 1 > str_id.size()) {
					write("1", position);
					Intent intent = new Intent(AddServices_SearchAct.this,
							AddServices_CompanyAct.class);
					intent.putExtra("company", str_search.get(position));
					startActivity(intent);
				} else {
					write("0", position);
					Intent intent = new Intent(AddServices_SearchAct.this,
							AddServices_personalAct.class);
					intent.putExtra("id", str_id.get(position));
					startActivity(intent);
				}

			}
		});
		adapter2 = new AddServices_searchAdapter(AddServices_SearchAct.this,
				str_history);
		lv_history = (ListView) findViewById(R.id.lv_history_search);
		lv_history.setAdapter(adapter2);
		lv_history.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (str_flag.get(position).equals("0")) {
					Intent intent = new Intent(AddServices_SearchAct.this,
							AddServices_personalAct.class);
					intent.putExtra("id", str_ids.get(position));
					startActivity(intent);
				} else {
					Intent intent = new Intent(AddServices_SearchAct.this,
							AddServices_CompanyAct.class);
					intent.putExtra("company", str_history.get(position));
					startActivity(intent);
				}
			}
		});
		lv_search.setVisibility(View.GONE);
	}

	public void write(String flag, int potision) {
		if (flag.equals("0")) {
			list.add(new AddServices_SearchUtil("2", str_name.get(potision),
					str_phone.get(potision), str_id.get(potision), "0"));
		} else {
			list.add(new AddServices_SearchUtil(str_search.get(potision), "2", "2",
					"2", "1"));
		}
		for (int i = 0; i < str_names.size(); i++) {
			list.add(new AddServices_SearchUtil(str_company.get(i), str_names.get(i), str_phones
					.get(i), str_ids.get(i), str_flag.get(i)));
		}
//		for (int i = 0; i < str_company.size(); i++) {
//			list.add(new Economic_SearchUtil(str_company.get(i), "", "", "",
//					"1"));
//		}
		writeListIntoSDcard(list);
	}

	/**
	 * write into sdcard (object)
	 * 
	 * @param list
	 */
	private void writeListIntoSDcard(List<AddServices_SearchUtil> list) {
		if (new OutputUtil<AddServices_SearchUtil>().writeListIntoSDcard(
				"mrlou.txt", list)) {
			
		} else {
			Toast.makeText(AddServices_SearchAct.this, "写入SD卡失败", 2000).show();
		}
	}

	/**
	 * read from sdcard
	 */
	private void readListFromSDcard() {
		List<AddServices_SearchUtil> list = new InputUtil<AddServices_SearchUtil>()
				.readListFromSdCard("mrlou.txt");
		if (list == null) {
			Toast.makeText(AddServices_SearchAct.this, "SD卡读取失败", 2000).show();
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (list.size() > 5) {
					if (i < 5) {
//						if (list.get(i).getFlag().equals("0")) {
//							str_names.add(list.get(i).getAgent());
//							str_phones.add(list.get(i).getPhone());
//							str_ids.add(list.get(i).getAgent_id());
//							str_flag.add(list.get(i).getFlag());
//							str_history.add(list.get(i).getAgent() + "   "
//									+ list.get(i).getPhone());
//						} else {
//							str_flag.add(list.get(i).getFlag());
//							str_company.add(list.get(i).getCompangy());
//							str_history.add(list.get(i).getCompangy());
//						}
						str_names.add(list.get(i).getAgent());
						str_phones.add(list.get(i).getPhone());
						str_ids.add(list.get(i).getAgent_id());
						str_flag.add(list.get(i).getFlag());
						str_company.add(list.get(i).getCompangy());
						if (str_names.get(i).equals("2")) {
							str_history.add(list.get(i).getCompangy());
						}else {
							str_history.add(list.get(i).getAgent() + "   "
									+ list.get(i).getPhone());
						}
					}
				} else {
//					if (list.get(i).getFlag().equals("0")) {
//						str_names.add(list.get(i).getAgent());
//						str_phones.add(list.get(i).getPhone());
//						str_ids.add(list.get(i).getAgent_id());
//						str_flag.add(list.get(i).getFlag());
//						str_history.add(list.get(i).getAgent() + "   "
//								+ list.get(i).getPhone());
//					} else {
//						str_flag.add(list.get(i).getFlag());
//						str_company.add(list.get(i).getCompangy());
//						str_history.add(list.get(i).getCompangy());
//					}
					str_names.add(list.get(i).getAgent());
					str_phones.add(list.get(i).getPhone());
					str_ids.add(list.get(i).getAgent_id());
					str_flag.add(list.get(i).getFlag());
					str_company.add(list.get(i).getCompangy());
					if (str_names.get(i).equals("2")) {
						str_history.add(list.get(i).getCompangy());
					}else {
						str_history.add(list.get(i).getAgent() + "   "
								+ list.get(i).getPhone());
					}
				}
			}
		}
	}

	TextWatcher mTextWatcher = new TextWatcher() {
		private CharSequence temp;

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			temp = s;

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			lay_search_history.setVisibility(View.GONE);
			lv_history.setVisibility(View.GONE);
			str_search.clear();
			adapter.notifyDataSetChanged();
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			if (!et_search.getText().toString().equals("")) {
				select_agent(et_search.getText().toString());
			}
			if (s.length() == 0) {
				lay_search_history.setVisibility(View.GONE);
				lv_history.setVisibility(View.GONE);
			}
		}
	};
	@SuppressLint("HandlerLeak")
	private Handler newHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				lv_search.setVisibility(View.VISIBLE);
				adapter.notifyDataSetChanged();
				break;
			case 2:
				Toast.makeText(AddServices_SearchAct.this, "暂无数据", 1000).show();
				break;
			case 99:
				Toast.makeText(AddServices_SearchAct.this, "网络异常，请尝试继续搜索", 1000)
						.show();
				break;
			}
		}
	};

	public void select_agent(String content) {
		Request request = Request.requesteconomicsearch(content);
		new HttpThread(request, this);
	}

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		if (reqID == Request.REQUEST_ECONOMIC) {
			str_search.clear();
			str_name.clear();
			str_phone.clear();
			str_id.clear();
			JSONObject json, json2;
			JSONArray joArray, joArray2;
			try {
				json = new JSONObject(b);
				String errorid = BaseHelper.getString(json, "errorid");
				if (errorid.equals("0")) {
					clean();
					json2 = json.getJSONObject("list");
					search = json2.getString("company");
					if (search.equals("")) {
						joArray = json2.getJSONArray("user");
						int len = joArray.length();
						for (int i = 0; i < len; i++) {
							json = joArray.getJSONObject(i);
							str_name.add(json.getString("cn_username")
									+ json.getString("en_username"));
							str_phone.add(json.getString("phone"));
							str_search.add(str_name.get(i) + str_phone.get(i));
							str_id.add(json.getString("id"));
						}
					} else {
						joArray = json2.getJSONArray("user");
						int len = joArray.length();
						for (int i = 0; i < len; i++) {
							json = joArray.getJSONObject(i);
							search2 = json.getString("cn_username");
							if (!search2.equals("")) {
								str_name.add(json.getString("cn_username")
										+ json.getString("en_username"));
								str_phone.add(json.getString("phone"));
								str_search.add(json.getString("cn_username")
										+ json.getString("en_username")
										+ json.getString("phone"));
								str_id.add(json.getString("id"));
							}
						}
						joArray2 = json2.getJSONArray("company");
						for (int i = 0; i < joArray2.length(); i++) {
							json = joArray2.getJSONObject(i);
							str_search.add(json.getString("name"));
						}
					}
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

	// 调用返回键的功能
	public void simulateKey(final int KeyCode) {
		new Thread() {
			public void run() {
				try {
					Instrumentation inst = new Instrumentation();
					inst.sendKeyDownUpSync(KeyCode);
					AddServices_SearchAct.this.finish();
				} catch (Exception e) {
					Log.e("Exception when sendKeyDownUpSync", e.toString());
				}
			}
		}.start();
	}

	public void clean() {
		str_search.clear();
		str_id.clear();
	}
}
