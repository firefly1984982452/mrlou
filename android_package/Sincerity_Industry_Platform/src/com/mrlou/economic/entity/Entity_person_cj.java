package com.mrlou.economic.entity;

import java.io.Serializable;

public class Entity_person_cj implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String 	id;
	private String 	href;
	private String 	transaction_type;
	private String 	building_id;
	private String 	building_name;
	private String 	square_meter;
	private String 	cj_time;
	private String 	industry;
	private String 	avatar_url;
	private String 	area_str;
	private String 	plate_str;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getTransaction_type() {
		return transaction_type;
	}
	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	public String getBuilding_id() {
		return building_id;
	}
	public void setBuilding_id(String building_id) {
		this.building_id = building_id;
	}
	public String getBuilding_name() {
		return building_name;
	}
	public void setBuilding_name(String building_name) {
		this.building_name = building_name;
	}
	public String getSquare_meter() {
		return square_meter;
	}
	public void setSquare_meter(String square_meter) {
		this.square_meter = square_meter;
	}
	public String getCj_time() {
		return cj_time;
	}
	public void setCj_time(String cj_time) {
		this.cj_time = cj_time;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getAvatar_url() {
		return avatar_url;
	}
	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}
	public String getArea_str() {
		return area_str;
	}
	public void setArea_str(String area_str) {
		this.area_str = area_str;
	}
	public String getPlate_str() {
		return plate_str;
	}
	public void setPlate_str(String plate_str) {
		this.plate_str = plate_str;
	}
	
}
