package com.mrlou.message;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.mrlou.mrlou.R;

/**
 *@author jx_chen
 *@date 2016-11-18---下午2:07:02
 *@problem
 */
public class Message_MainAct extends FragmentActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collect_main);
		// 必需继承FragmentActivity,嵌套fragment只需要这行代码
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.container_collect, new Message_Main())
				.commitAllowingStateLoss();
	}
}
