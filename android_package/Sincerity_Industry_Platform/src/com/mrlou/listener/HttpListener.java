package com.mrlou.listener;

public interface HttpListener {
	public void doResponse(int reqID, String b);
	public void doError(String s);

}
