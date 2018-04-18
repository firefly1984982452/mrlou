package com.mrlou.favorites;

import java.util.ArrayList;

import com.mrlou.economic.Economic_personalAct;
import com.mrlou.mrlou.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class Agent_act extends Activity {

	private LinearLayout lay;
	private ListView list;
	private ArrayList<String> str_agent_url, str_agent_name, str_agent_company,
			str_agent_age, str_agent_deal, str_agent_phone, str_agent_status,
			str_agent_id;
	private Agent_Adapter adapter;
	private SharedPreferences sharedPreferences;
	private String session_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collection_agent);
		sharedPreferences = Agent_act.this.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		Intent intent = getIntent();
		str_agent_url = intent.getStringArrayListExtra("avatar");
		str_agent_name = intent.getStringArrayListExtra("name");
		str_agent_company = intent.getStringArrayListExtra("company");
		str_agent_age = intent.getStringArrayListExtra("work_age");
		str_agent_deal = intent.getStringArrayListExtra("deal");
		str_agent_phone = intent.getStringArrayListExtra("phone");
		str_agent_status = intent.getStringArrayListExtra("company_status");
		str_agent_id = intent.getStringArrayListExtra("id");
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay = (LinearLayout) findViewById(R.id.lay_agent);
		list = (ListView) findViewById(R.id.lv_agent);
		if (str_agent_url.size() > 0) {
			lay.setVisibility(View.GONE);
			list.setVisibility(View.VISIBLE);
			adapter = new Agent_Adapter(Agent_act.this, str_agent_url,
					str_agent_name, str_agent_company, str_agent_age,
					str_agent_deal, str_agent_phone, str_agent_status,
					str_agent_id,session_id);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(Agent_act.this,Economic_personalAct.class);
					intent.putExtra("id", str_agent_id.get(position));
					startActivity(intent);
				}
			});
		} else {
			lay.setVisibility(View.VISIBLE);
			list.setVisibility(View.GONE);
		}

	}
}
