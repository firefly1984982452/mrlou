package com.mrlou.addservices;

import java.io.Serializable;

public class AddServices_SearchUtil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String compangy;
	private String agent;
	private String phone;
	private String agent_id;
	private String flag;

	public AddServices_SearchUtil(String compangy, String agent, String phone,
			String agent_id, String flag) {
		this.compangy = compangy;
		this.agent = agent;
		this.phone = phone;
		this.agent_id = agent_id;
		this.flag = flag;
	}

	public String getCompangy() {
		return compangy;
	}

	public void setCompangy(String compangy) {
		this.compangy = compangy;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(String agent_id) {
		this.agent_id = agent_id;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

}
