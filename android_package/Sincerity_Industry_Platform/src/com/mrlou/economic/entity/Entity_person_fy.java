package com.mrlou.economic.entity;

import java.io.Serializable;

public class Entity_person_fy implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String href;
	private String category;
	private String building_id;
	private String building_name;
	private String avatar;
	private String transaction_type;
	private String unit_no;
	private String position;
	private String square_meter;
	private String status;
	private String area_str;
	private String plate_str;
	private String price;
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
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getTransaction_type() {
		return transaction_type;
	}
	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	public String getUnit_no() {
		return unit_no;
	}
	public void setUnit_no(String unit_no) {
		this.unit_no = unit_no;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getSquare_meter() {
		return square_meter;
	}
	public void setSquare_meter(String square_meter) {
		this.square_meter = square_meter;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
}
