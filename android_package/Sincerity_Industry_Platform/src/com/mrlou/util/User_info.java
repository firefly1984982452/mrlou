package com.mrlou.util;

import java.io.Serializable;

public class User_info implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String session_id;
	private static String user_id;
	private static String type_id;
	private static String add_v;
	private static boolean flag;
	
	public static String getAdd_v() {
		return add_v;
	}

	public static void setAdd_v(String add_v) {
		User_info.add_v = add_v;
	}

	public static String getSession_id() {
		return session_id;
	}

	public static void setSession_id(String session_id) {
		User_info.session_id = session_id;
	}

	public static boolean isFlag() {
		return flag;
	}

	public static void setFlag(boolean flag) {
		User_info.flag = flag;
	}

	public static String getUser_id() {
		return user_id;
	}

	public static void setUser_id(String user_id) {
		User_info.user_id = user_id;
	}

	public static String getType_id() {
		return type_id;
	}

	public static void setType_id(String type_id) {
		User_info.type_id = type_id;
	}

}
