package com.mrlou.application;

import java.util.ArrayList;
import android.app.Activity;
import android.app.Application;

public class MyApplication extends Application {
	private ArrayList<Activity> app = new ArrayList<Activity>();
	private static MyApplication instance;
	
	public void addActivity(Activity act) {
		app.add(act);
	}
	
	public static MyApplication getInstance() {
		if (instance == null) {
			instance = new MyApplication();
		}
		return instance;
	}

	public void exit() {
		for (Activity ac : app) {
			ac.finish();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
