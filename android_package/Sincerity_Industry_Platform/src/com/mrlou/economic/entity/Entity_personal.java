package com.mrlou.economic.entity;

import java.io.Serializable;


public class Entity_personal implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String total;
	private String region_name;
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getRegion_name() {
		return region_name;
	}
	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	
}
