package com.mrlou.addservices;

import com.mrlou.mrlou.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

/**
 * @author jx_chen
 * @date 2016-11-9---下午7:01:59
 * @problem
 */
public class Main_addServices extends FragmentActivity {

	@Override
	protected void onCreate(@Nullable Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.add_services_main);
		// 必需继承FragmentActivity,嵌套fragment只需要这行代码
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, new AddServices_mianAct())
				.commitAllowingStateLoss();
	}
}
