package com.mrlou.favorites;

import java.util.ArrayList;

import com.mrlou.h5.RoomInfo_Act;
import com.mrlou.message.Message_NewHourseAct;
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

public class Hourse_act extends Activity {

	private LinearLayout lay;
	private ListView list;
	private Hourse_Adapter adapter;
	private ArrayList<String> str_area_name, str_total, str_id, str_name,
			str_area_code, str_totals;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collection_house);
		Intent intent = getIntent();
		str_area_name = intent.getStringArrayListExtra("area_name");
		str_total = intent.getStringArrayListExtra("total");
		str_id = intent.getStringArrayListExtra("id");
		str_name = intent.getStringArrayListExtra("name");
		str_area_code = intent.getStringArrayListExtra("area_code");
		str_totals = intent.getStringArrayListExtra("totals");
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay = (LinearLayout) findViewById(R.id.lay_house);
		list = (ListView) findViewById(R.id.lv_house);
		if (str_name.size()>0) {
			lay.setVisibility(View.GONE);
			list.setVisibility(View.VISIBLE);
			adapter = new Hourse_Adapter(Hourse_act.this, str_area_name, str_total,
					str_id, str_name, str_area_code, str_totals);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(Hourse_act.this,Hourse_DetailAct.class);
					intent.putExtra("id", str_id.get(position));
					intent.putExtra("buildingname", str_name.get(position));
					startActivity(intent);
				}
			});
		}else {
			lay.setVisibility(View.VISIBLE);
			list.setVisibility(View.GONE);
		}
	}
}
