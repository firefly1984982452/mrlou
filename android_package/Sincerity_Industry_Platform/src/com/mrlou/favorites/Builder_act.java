package com.mrlou.favorites;

import java.util.ArrayList;

import com.amap.api.services.core.ar;
import com.mrlou.economic.Economic_personalAct;
import com.mrlou.h5.Building_Act;
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

public class Builder_act extends Activity {

	private LinearLayout lay;
	private ListView list;
	private Builder_Adapter adapter;
	private ArrayList<String> str_building_url, str_building_name,
			str_building_area, str_building_price, str_building_id;
	private SharedPreferences sharedPreferences;
	private String session_id;
	private String mrString,url,url2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collection_builder);
		sharedPreferences = Builder_act.this.getSharedPreferences("user",
				Activity.MODE_PRIVATE);
		session_id = sharedPreferences.getString("session_id", "");
		Intent intent = getIntent();
		str_building_url = intent.getStringArrayListExtra("avatar_url");
		str_building_name = intent.getStringArrayListExtra("name");
		str_building_price = intent.getStringArrayListExtra("price");
		str_building_id = intent.getStringArrayListExtra("id");
		str_building_area = intent.getStringArrayListExtra("address");
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		lay = (LinearLayout) findViewById(R.id.lay_builder);
		list = (ListView) findViewById(R.id.lv_builder);
		if (str_building_url.size()>0) {
			lay.setVisibility(View.GONE);
			list.setVisibility(View.VISIBLE);
			adapter = new Builder_Adapter(Builder_act.this, str_building_url,
					str_building_name, str_building_area, str_building_price,
					str_building_id,session_id);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Builder_act.this,
							Building_Act.class);
					mrString = Builder_act.this.getExternalFilesDir(null).getPath();
					url = "file://"+mrString+"/bulidersir/lxs_index.html";
					url2 = "file://"+mrString+"/bulidersir/";
					intent.putExtra("url", url);
					intent.putExtra("url2", url2);
					intent.putExtra("frsource", "gg");
					intent.putExtra("id", str_building_id.get(position));
					startActivity(intent);
				}
			});
		}else {
			lay.setVisibility(View.VISIBLE);
			list.setVisibility(View.GONE);
		}
	}
}
