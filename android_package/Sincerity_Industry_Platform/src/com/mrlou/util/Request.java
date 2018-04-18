package com.mrlou.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.baidu.integrationsdk.lib.R.string;

import android.os.Handler;
import android.renderscript.Type;
import android.webkit.WebView;

public class Request {
	public int reqID = -1;// 发送请求的id
	public String url = "";// 发送请求的地址
	public JSONObject jObject;
	public List<NameValuePair> nameValuePairs;// 发送请求的参数集合
	final static String ACT_UPLOAD_IMG = "upload_img";
	final static String ACT_PAY = "pay";
	final static String ACT_PAY_PARAMS = "pay_info";
	public static final int REQUEST_IMG_STREAM = 1;
	public static final int REQUEST_LOGINBYTOKEN = 2;
	public static final int REQUEST_SHARECREATE = 3;
	public static final int REQUEST_SHARELOG = 4;
	public static final int REQUEST_NEW = 5;
	public static final int REQUEST_ECONOMIC = 6;
	public static final int REQUEST_ECONOMIC_COMPANY = 7;
	public static final int REQUEST_ECONOMIC_COLLECT = 8;
	static String data = null;
	static Handler handle = new Handler();

	public Request(int reqID, String url, List<NameValuePair> nameValuePairs) {
		this.reqID = reqID;
		this.url = url;
		this.nameValuePairs = nameValuePairs;
	}

	/**
	 * 请求版本以及下载地址
	 */
	public static Request requestNews(String version) {
		StringBuffer sb = new StringBuffer(Constant.load_version);
		ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("v", version));
		return new Request(REQUEST_NEW, sb.toString(), nameValuePair);
	}

	/**
	 * 图片上传 http://appapi.imrlou.com/api/upload？file1=&object_type=&object_id=&
	 * is_avater=&mark=;
	 * 
	 */
	public static Request requestImg(String file1, String object_type,
			String object_id, String is_avatar, String mark) {
		StringBuffer sb = new StringBuffer(Constant.upload);
		ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
		nameValuePair.add(new BasicNameValuePair("file1", file1));
		nameValuePair.add(new BasicNameValuePair("object_type", object_type));
		nameValuePair.add(new BasicNameValuePair("object_id", object_id));
		nameValuePair.add(new BasicNameValuePair("is_avater", is_avatar));
		nameValuePair.add(new BasicNameValuePair("mark", mark));
		System.out.println(sb.toString() + "-----" + object_type);
		return new Request(REQUEST_IMG_STREAM, sb.toString(), nameValuePair);
	}

	// 登陆的接口 loginbytoken
	public static Request requestloginbytoken(String token,
			String device_serial, String sys_agent, String channid) {
		StringBuffer sb = new StringBuffer(Constant.loginbutoken);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("token", token));
		nameValuePairs.add(new BasicNameValuePair("device_serial",
				device_serial));
		nameValuePairs.add(new BasicNameValuePair("channel_id", channid));
		nameValuePairs.add(new BasicNameValuePair("os", "android"));
		nameValuePairs.add(new BasicNameValuePair("sys_agent", sys_agent));
		return new Request(REQUEST_LOGINBYTOKEN, sb.toString(), nameValuePairs);
	}

	// 建立分享访问的接口
	// public static Request requestShareCreate(String type,String id){
	// StringBuffer sb = new StringBuffer(Constant.sharecreate);
	// ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	// nameValuePairs.add(new BasicNameValuePair("type", type));
	// nameValuePairs.add(new BasicNameValuePair("id", id));
	// return new Request(REQUEST_SHARECREATE, sb.toString(), nameValuePairs);
	// }

	// 版本升级
	public static Request requestgetversion(String version) {
		StringBuffer sb = new StringBuffer(Constant.update);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("version", version));
		System.out.println(sb.toString() + "--" + version);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 五年加经纪人公司接口
	public static Request requesteconomicompany(String name) {
		StringBuffer sb = new StringBuffer(Constant.economic_company);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("pp_name", name));
		System.out.println(sb.toString() + "--" + name);
		return new Request(REQUEST_ECONOMIC_COMPANY, sb.toString(),
				nameValuePairs);
	}

	// 五年加经纪人首页接口
	public static Request requesteconomicmain(String area_id) {
		StringBuffer sb = new StringBuffer(Constant.economic_main);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("rid", area_id));
		System.out.println(sb.toString() + "--" + area_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 五年加经纪人个人品牌页
	public static Request requesteconomicpersonal(String id) {
		StringBuffer sb = new StringBuffer(Constant.economic_personal);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", id));
		System.out.println(sb.toString() + "--" + id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 五年加经纪人搜索
	public static Request requesteconomicsearch(String content) {
		StringBuffer sb = new StringBuffer(Constant.economic_search);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("key", content));
		System.out.println(sb.toString() + "--" + content);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 五年加经纪人收藏
	public static Request requesteconomiccollect(String session_id,
			String type, String id) {
		StringBuffer sb = new StringBuffer(Constant.economic_collect);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		nameValuePairs.add(new BasicNameValuePair("id", id));
		System.out.println(sb.toString() + "--" + session_id + "--" + id + "--"
				+ type);
		return new Request(REQUEST_ECONOMIC_COLLECT, sb.toString(),
				nameValuePairs);
	}

	// 最新认证房源
	public static Request requestmessaagenewhourse(String id) {
		StringBuffer sb = new StringBuffer(Constant.message_newhourse);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("", ""));
		System.out.println(sb.toString() + "--" + id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 独栋写字楼
	public static Request requeststandoffice() {
		StringBuffer sb = new StringBuffer(Constant.message_standingoffice);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		// nameValuePairs.add(new BasicNameValuePair("page", id));
		// System.out.println(sb.toString() + "--" + id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 独栋写字楼
	public static Request requestshorthourse() {
		StringBuffer sb = new StringBuffer(Constant.message_shorthourse);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		// nameValuePairs.add(new BasicNameValuePair("page", id));
		// System.out.println(sb.toString() + "--" + id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 分类列表信息
	public static Request requestemessagetype(String session_id, String type) {
		StringBuffer sb = new StringBuffer(Constant.message_labelmessage);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		System.out.println(sb.toString() + "--" + type);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 通知消息首页
	public static Request requesteconomicnoticemessage(String type,
			String page, String session_id) {
		StringBuffer sb = new StringBuffer(Constant.message_message);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("type", type));
		nameValuePairs.add(new BasicNameValuePair("page", page));
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		System.out.println(sb.toString() + "--" + session_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 通知消息
	public static Request requestnotice(String session_id) {
		StringBuffer sb = new StringBuffer(Constant.message_notice);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		System.out.println(sb.toString() + "--" + session_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 客户管理
	public static Request requestcustomermanage(String page, String session_id) {
		StringBuffer sb = new StringBuffer(Constant.customer_manager);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("page", page));
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		System.out.println(sb.toString() + "--" + session_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 客户信息
	public static Request requestcustomerinfo(String id, String page,
			String session_id) {
		StringBuffer sb = new StringBuffer(Constant.customer_info);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("page", page));
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		System.out.println(sb.toString() + "--" + session_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 客户信息修改
	public static Request requestcustomerinfoedit(String session_id, String id,
			String name, String transaction_type, String first_access_time,
			String sector, String pre_square_meter, String pre_room,
			String pre_price_1, String pre_price_unit_1, String intro,
			String process, String phone_number, String company,
			String job_title, String use_for, String company_property,
			String employee_total, String current_building, String expire_time,
			String picture_id) {
		StringBuffer sb = new StringBuffer(Constant.customer_infoeidt);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("name", name));
		nameValuePairs.add(new BasicNameValuePair("transaction_type",
				transaction_type));
		nameValuePairs.add(new BasicNameValuePair("first_access_time",
				first_access_time));
		nameValuePairs.add(new BasicNameValuePair("sector", sector));
		nameValuePairs.add(new BasicNameValuePair("pre_square_meter",
				pre_square_meter));
		nameValuePairs.add(new BasicNameValuePair("pre_room", pre_room));
		nameValuePairs.add(new BasicNameValuePair("pre_price_1", pre_price_1));
		nameValuePairs.add(new BasicNameValuePair("pre_price_unit_1",
				pre_price_unit_1));
		nameValuePairs.add(new BasicNameValuePair("intro", intro));
		nameValuePairs.add(new BasicNameValuePair("process", process));
		nameValuePairs
				.add(new BasicNameValuePair("phone_number", phone_number));
		nameValuePairs.add(new BasicNameValuePair("company", company));
		nameValuePairs.add(new BasicNameValuePair("job_title", job_title));
		nameValuePairs.add(new BasicNameValuePair("use_for", use_for));
		nameValuePairs.add(new BasicNameValuePair("company_property",
				company_property));
		nameValuePairs.add(new BasicNameValuePair("employee_total",
				employee_total));
		nameValuePairs.add(new BasicNameValuePair("current_building",
				current_building));
		nameValuePairs.add(new BasicNameValuePair("expire_time", expire_time));
		nameValuePairs.add(new BasicNameValuePair("picture_id", picture_id));
		System.out.println(sb.toString() + "-----" + picture_id);
		return new Request(REQUEST_ECONOMIC_COLLECT, sb.toString(),
				nameValuePairs);
	}

	// 客户删除
	public static Request requestcustomerdelete(String id, String session_id) {
		StringBuffer sb = new StringBuffer(Constant.customer_delete);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		System.out.println(sb.toString() + "--" + session_id);
		return new Request(REQUEST_ECONOMIC_COMPANY, sb.toString(),
				nameValuePairs);
	}

	// 客户添加
	public static Request requestcustomeradd(String session_id, String name,
			String transaction_type, String first_access_time, String sector,
			String pre_square_meter, String pre_room, String pre_price_1,
			String pre_price_unit_1, String intro) {
		StringBuffer sb = new StringBuffer(Constant.customer_add);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("name", name));
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("transaction_type",
				transaction_type));
		nameValuePairs.add(new BasicNameValuePair("first_access_time",
				first_access_time));
		nameValuePairs.add(new BasicNameValuePair("sector", sector));
		nameValuePairs.add(new BasicNameValuePair("pre_square_meter",
				pre_square_meter));
		nameValuePairs.add(new BasicNameValuePair("pre_room", pre_room));
		nameValuePairs.add(new BasicNameValuePair("pre_price_1", pre_price_1));
		nameValuePairs.add(new BasicNameValuePair("pre_price_unit_1",
				pre_price_unit_1));
		nameValuePairs.add(new BasicNameValuePair("intro", intro));
		System.out.println(sb.toString() + "--" + session_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 工作日志添加
	public static Request requestjoblogadd(String session_id,
			String target_type, String target_id, String event_date,
			String content, String notify_time) {
		StringBuffer sb = new StringBuffer(Constant.joblogadd);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("target_type", target_type));
		nameValuePairs.add(new BasicNameValuePair("target_id", target_id));
		nameValuePairs.add(new BasicNameValuePair("event_date", event_date));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		nameValuePairs.add(new BasicNameValuePair("notify_time", notify_time));
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		System.out.println(sb.toString() + "--" + session_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 工作日志修改
	public static Request requestjoblogedit(String session_id, String id,
			String target_type, String target_id, String event_date,
			String content, String notify_time) {
		StringBuffer sb = new StringBuffer(Constant.joblogedit);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("target_type", target_type));
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("target_id", target_id));
		nameValuePairs.add(new BasicNameValuePair("event_date", event_date));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		nameValuePairs.add(new BasicNameValuePair("notify_time", notify_time));
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		System.out.println(sb.toString() + "--" + session_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 工作日志修改
	public static Request requestjobdelete(String session_id, String id) {
		StringBuffer sb = new StringBuffer(Constant.joblogdelete);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		System.out.println(sb.toString() + "--" + session_id);
		return new Request(REQUEST_ECONOMIC_COMPANY, sb.toString(),
				nameValuePairs);
	}

	// 广告接口
	public static Request requestadvertise(String session_id) {
		StringBuffer sb = new StringBuffer(Constant.advertise);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("type", "3"));
		System.out.println(sb.toString() + "--" + session_id);
		return new Request(REQUEST_ECONOMIC_COMPANY, sb.toString(),
				nameValuePairs);
	}

	// 我的房源接口
	public static Request requestmyhourse(String session_id) {
		StringBuffer sb = new StringBuffer(Constant.myhourse);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		System.out.println(sb.toString() + "--" + session_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 单个房源接口
	public static Request requestmyhourse_detail(String session_id, String id) {
		StringBuffer sb = new StringBuffer(Constant.myhourse_detail);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("building_id", id));
		System.out.println(sb.toString() + "--" + session_id + "id" + id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 消息首页
	public static Request requestmessage_main(String session_id) {
		StringBuffer sb = new StringBuffer(Constant.message_main);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		System.out.println(sb.toString() + "--" + session_id);
		return new Request(REQUEST_ECONOMIC_COMPANY, sb.toString(),
				nameValuePairs);
	}

	// 获取个人信息
	public static Request requestgetperinfo(String session_id, String user_id) {
		StringBuffer sb = new StringBuffer(Constant.per_info);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("user_id", user_id));
		System.out.println(sb.toString() + "--" + session_id + "##" + user_id);
		return new Request(REQUEST_ECONOMIC_COLLECT, sb.toString(),
				nameValuePairs);
	}

	// 个人设置修改01
	public static Request requestsettingper01(String session_id,
			String user_id, String type, String cn_name, String en_name,
			String email, String avatar_id, String company, String wx_num,
			String wechat_qr_id, String post_card_id) {
		StringBuffer sb = new StringBuffer(Constant.per_setting);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("is_chk", "0"));
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("id", user_id));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		nameValuePairs.add(new BasicNameValuePair("cn_username", cn_name));
		nameValuePairs.add(new BasicNameValuePair("en_username", en_name));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("company", company));
		nameValuePairs.add(new BasicNameValuePair("wechat", wx_num));
		nameValuePairs.add(new BasicNameValuePair("avatar", avatar_id));
		nameValuePairs
				.add(new BasicNameValuePair("wechat_qr_id", wechat_qr_id));
		nameValuePairs
				.add(new BasicNameValuePair("post_card_id", post_card_id));
		System.out.println(sb.toString() + "--" + session_id + "!!!!!"
				+ cn_name + "####" + avatar_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 个人设置修改02
	public static Request requestsettingper02(String session_id,
			String user_id, String type, String cn_name, String en_name,
			String email, String company, String intro, String time,
			String area, String wx_num, String avatar_id, String wechat_qr_id,
			String post_card_id,String office_building,String office_building_id,String good_type) {
		StringBuffer sb = new StringBuffer(Constant.per_setting);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("is_chk", "0"));
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("id", user_id));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		nameValuePairs.add(new BasicNameValuePair("cn_username", cn_name));
		nameValuePairs.add(new BasicNameValuePair("en_username", en_name));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("company", company));
		nameValuePairs.add(new BasicNameValuePair("intro", intro));
		nameValuePairs.add(new BasicNameValuePair("work_start", time));
		nameValuePairs.add(new BasicNameValuePair("region_id", area));
		nameValuePairs.add(new BasicNameValuePair("wechat", wx_num));
		nameValuePairs.add(new BasicNameValuePair("avatar", avatar_id));
		nameValuePairs.add(new BasicNameValuePair("office_building", office_building));
		nameValuePairs.add(new BasicNameValuePair("office_building_id", office_building_id));
		nameValuePairs.add(new BasicNameValuePair("good_type", good_type));
		nameValuePairs
				.add(new BasicNameValuePair("wechat_qr_id", wechat_qr_id));
		nameValuePairs
				.add(new BasicNameValuePair("post_card_id", post_card_id));
		System.out.println(sb.toString() + "-is_chk-" + "0" + "-session_id-"
				+ session_id + "-id-" + user_id + "-type-" + type
				+ "-cn_username-" + cn_name + "-en_username-" + en_name
				+ "-email-" + email + "-company-" + company + "-intro-" + intro
				+ "-work_start-" + time + "-region_id-" + area + "-wechat-"
				+ wx_num + "-post_card_id-" + post_card_id + "-avatar-"
				+ avatar_id + "---wechat_qr_id---" + wechat_qr_id+"--good_type--"+good_type);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 个人设置修改03
	public static Request requestsettingper03(String session_id,
			String user_id, String type, String cn_name, String en_name,
			String email, String company, String building_a, String building_b,
			String wx_num, String avatars_id, String wechat_qr_id,
			String post_card_id) {
		StringBuffer sb = new StringBuffer(Constant.per_setting);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("is_chk", "0"));
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("id", user_id));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		nameValuePairs.add(new BasicNameValuePair("cn_username", cn_name));
		nameValuePairs.add(new BasicNameValuePair("en_username", en_name));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("company", company));
		nameValuePairs.add(new BasicNameValuePair("building_a", building_a));
		nameValuePairs.add(new BasicNameValuePair("building_b", building_b));
		nameValuePairs.add(new BasicNameValuePair("wechat", wx_num));
		nameValuePairs.add(new BasicNameValuePair("avatar", avatars_id));
		nameValuePairs
				.add(new BasicNameValuePair("wechat_qr_id", wechat_qr_id));
		nameValuePairs
				.add(new BasicNameValuePair("post_card_id", post_card_id));
		System.out.println(sb.toString() + "--" + session_id
				+ "--post_card_id--" + post_card_id + "wechat_qr_id:"
				+ wechat_qr_id + "avatar:" + avatars_id + "wechat" + wx_num);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 个人设置修改04
	public static Request requestsettingper04(String session_id,
			String user_id, String type, String cn_name, String en_name,
			String email, String service_type, String company, String intro,
			String time, String wx_num, String avatar_id,
			String wechat_qr_id, String post_card_id) {
		StringBuffer sb = new StringBuffer(Constant.per_setting);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("is_chk", "0"));
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("id", user_id));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		nameValuePairs.add(new BasicNameValuePair("cn_username", cn_name));
		nameValuePairs.add(new BasicNameValuePair("en_username", en_name));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs
				.add(new BasicNameValuePair("service_type", service_type));
		nameValuePairs.add(new BasicNameValuePair("company", company));
		nameValuePairs.add(new BasicNameValuePair("intro", intro));
		nameValuePairs.add(new BasicNameValuePair("work_start", time));
		nameValuePairs.add(new BasicNameValuePair("wechat", wx_num));
		nameValuePairs.add(new BasicNameValuePair("avatar", avatar_id));
		nameValuePairs
				.add(new BasicNameValuePair("wechat_qr_id", wechat_qr_id));
		nameValuePairs
				.add(new BasicNameValuePair("post_card_id", post_card_id));
		System.out.println(sb.toString() + "-is_chk-" + "0" + "-session_id-"
				+ session_id + "-id-" + user_id + "-type-" + type
				+ "-cn_username-" + cn_name + "-en_username-" + en_name
				+ "-email-" + email + "-company-" + company + "-intro-" + intro
				+ "-work_start-" + time + "-region_id-"  + "-wechat-"
				+ wx_num + "-post_card_id-" + post_card_id + "-avatar-"
				+ avatar_id + "---wechat_qr_id---" + wechat_qr_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 获取新增楼盘
	public static Request requestaddbuild(String key) {
		StringBuffer sb = new StringBuffer(Constant.per_addbuild);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("key", key));
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 查询公司名称
	public static Request requestselectcompany(String key) {
		StringBuffer sb = new StringBuffer(Constant.company_select);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("company", key));
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 添加公司名称
	public static Request requestaddcompany(String key) {
		StringBuffer sb = new StringBuffer(Constant.per_addbuild);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("key", key));
		return new Request(REQUEST_NEW, sb.toString(), nameValuePairs);
	}

	// 成交信息新增
	public static Request requestaddcjinfo(String session_id,
			String transaction_type, String building_name, String square_meter,
			String cj_time, String industry, String proof, String proof_id) {
		StringBuffer sb = new StringBuffer(Constant.cjinfo_add);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("transaction_type",
				transaction_type));
		nameValuePairs.add(new BasicNameValuePair("type", "1"));
		nameValuePairs.add(new BasicNameValuePair("building_name",
				building_name));
		nameValuePairs
				.add(new BasicNameValuePair("square_meter", square_meter));
		nameValuePairs.add(new BasicNameValuePair("cj_time", cj_time));
		nameValuePairs.add(new BasicNameValuePair("industry", industry));
		nameValuePairs.add(new BasicNameValuePair("contact", proof));
		nameValuePairs.add(new BasicNameValuePair("proof_id", proof_id));
		System.out
				.println(sb.toString() + "--" + session_id + "---" + proof_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 成交信息编辑
	public static Request requesteditcjinfo(String session_id, String id,
			String transaction_type, String building_name, String square_meter,
			String cj_time, String industry, String proof, String proof_id) {
		StringBuffer sb = new StringBuffer(Constant.cjinfo_edit);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("transaction_type",
				transaction_type));
		nameValuePairs.add(new BasicNameValuePair("type", "1"));
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("building_name",
				building_name));
		nameValuePairs
				.add(new BasicNameValuePair("square_meter", square_meter));
		nameValuePairs.add(new BasicNameValuePair("cj_time", cj_time));
		nameValuePairs.add(new BasicNameValuePair("industry", industry));
		nameValuePairs.add(new BasicNameValuePair("contact", proof));
		nameValuePairs.add(new BasicNameValuePair("proof_id", proof_id));
		System.out.println(sb.toString() + "--" + session_id + "---"
				+ "--proof_id--" + proof_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 成交首页
	public static Request requestcjinfomain(String session_id) {
		StringBuffer sb = new StringBuffer(Constant.cjinfo_main);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("type", "1"));
		System.out.println(sb.toString() + "--" + session_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 房源信息接口
	public static Request requestroominfo(String session_id, String id,
			String page) {
		StringBuffer sb = new StringBuffer(Constant.roominfo);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("page", page));
		System.out.println(sb.toString() + "--" + session_id + "---" + id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 房源信息修改
	public static Request requesthourseinfoedit(String session_id, String id,
			String category, String unit_no, String square_meter,
			String transaction_type, String room_price, String room_price_unit,
			String position, String head_to, String delivery_status,
			String delivery_date, String fee_rate, String min_rent_period,
			String contact_phone, String chair_total, String intro,
			String publish_status, String status, String avatar, String images,
			String land_id) {
		StringBuffer sb = new StringBuffer(Constant.roominfo_eidt);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("category", category));
		nameValuePairs.add(new BasicNameValuePair("unit_no", unit_no));
		nameValuePairs
				.add(new BasicNameValuePair("square_meter", square_meter));
		nameValuePairs.add(new BasicNameValuePair("transaction_type",
				transaction_type));
		nameValuePairs.add(new BasicNameValuePair("room_price", room_price));
		nameValuePairs.add(new BasicNameValuePair("room_price_unit",
				room_price_unit));
		nameValuePairs.add(new BasicNameValuePair("position", position));
		nameValuePairs.add(new BasicNameValuePair("head_to", head_to));
		nameValuePairs.add(new BasicNameValuePair("delivery_status",
				delivery_status));
		nameValuePairs.add(new BasicNameValuePair("delivery_date",
				delivery_date));
		nameValuePairs.add(new BasicNameValuePair("fee_rate", fee_rate));
		nameValuePairs.add(new BasicNameValuePair("min_rent_period",
				min_rent_period));
		nameValuePairs.add(new BasicNameValuePair("contact_phone",
				contact_phone));
		nameValuePairs.add(new BasicNameValuePair("chair_total", chair_total));
		nameValuePairs.add(new BasicNameValuePair("intro", intro));
		nameValuePairs.add(new BasicNameValuePair("publish_status",
				publish_status));
		nameValuePairs.add(new BasicNameValuePair("status", status));
		nameValuePairs.add(new BasicNameValuePair("avatar", avatar));
		nameValuePairs.add(new BasicNameValuePair("images", images));
		nameValuePairs.add(new BasicNameValuePair("land_id", land_id));
		System.out.println(sb.toString() + "-----" + session_id + "==="
				+ "room_price_unit----" + land_id);
		return new Request(REQUEST_ECONOMIC_COMPANY, sb.toString(),
				nameValuePairs);
	}

	// 我的帖子首页接口
	public static Request requestmypostmain(String session_id) {
		StringBuffer sb = new StringBuffer(Constant.per_mypostmain);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		System.out.println(sb.toString() + "--" + session_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 取消收藏帖子4,收藏楼盘2,收藏经纪人5,收藏房源3
	public static Request requestcancelpost(String session_id, String type,
			String id) {
		StringBuffer sb = new StringBuffer(Constant.per_cancelpost);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		nameValuePairs.add(new BasicNameValuePair("id", id));
		System.out.println(sb.toString() + "--" + session_id + "---00" + id);
		return new Request(REQUEST_NEW, sb.toString(), nameValuePairs);
	}

	// 我的帖子修改收藏接口
	public static Request requestmodifypost(String session_id, String status,
			String id) {
		StringBuffer sb = new StringBuffer(Constant.per_modifypost);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("status", status));
		nameValuePairs.add(new BasicNameValuePair("id", id));
		System.out.println(sb.toString() + "--" + session_id + "---00" + id);
		return new Request(REQUEST_ECONOMIC_COMPANY, sb.toString(),
				nameValuePairs);
	}

	// 帮助中心跟楼先生
	public static Request requestabout(String session_id, String type) {
		StringBuffer sb = new StringBuffer(Constant.per_about);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 易楼精英会
	public static Request requestelite(String session_id, String type) {
		StringBuffer sb = new StringBuffer(Constant.per_elite);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		System.out.println(sb.toString() + "--" + session_id + "--type--"
				+ type);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 收藏夹首页
	public static Request requestcollect(String session_id) {
		StringBuffer sb = new StringBuffer(Constant.collect_main);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		System.out.println(sb.toString() + "--" + session_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 单个房源收藏接口
	public static Request requestcollect_hourse(String session_id, String id) {
		StringBuffer sb = new StringBuffer(Constant.collect_hourse);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("building_id", id));
		System.out.println(sb.toString() + "--" + session_id + "--" + id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 判断是否已经收藏
	public static Request requestis_collect(String session_id, String id,
			String type) {
		StringBuffer sb = new StringBuffer(Constant.is_collect);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		System.out.println(sb.toString() + "-session_id-" + session_id + "-id-"
				+ id + "-type-" + type);
		return new Request(REQUEST_SHARELOG, sb.toString(), nameValuePairs);
	}

	// 刪除失效房源
	public static Request requestdel_hourse(String session_id, String id) {
		StringBuffer sb = new StringBuffer(Constant.del_hourse);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("id", id));
		System.out.println(sb.toString() + "-session_id-" + session_id + "-id-"
				+ id);
		return new Request(REQUEST_NEW, sb.toString(), nameValuePairs);
	}

	// 会员提交申请
	public static Request request_insertapply(String session_id,
			String cn_name, String en_name, String email, String company,
			String type, String start_time, String end_time,String post_card_id) {
		StringBuffer sb = new StringBuffer(Constant.insertapply);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("cn_name", cn_name));
		nameValuePairs.add(new BasicNameValuePair("en_name", en_name));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("company", company));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		nameValuePairs.add(new BasicNameValuePair("start_time", start_time));
		nameValuePairs.add(new BasicNameValuePair("end_time", end_time));
		nameValuePairs.add(new BasicNameValuePair("post_card_id", post_card_id));
		System.out.println(sb.toString() + "-session_id-" + session_id);
		return new Request(REQUEST_NEW, sb.toString(), nameValuePairs);
	}

	// 身份修改(开、代、运、物、经纪人、服务商)
	public static Request request_idenfify(String session_id, String type,
			String cn_name, String en_name, String email, String company,
			String company_id, String building_a, String building_b,
			String worktime, String region_id, String wechat,
			String wechat_qr_id, String avatar_id, String post_card_id,
			String service_type,String good_type) {
		StringBuffer sb = new StringBuffer(Constant.identify_modify);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		nameValuePairs.add(new BasicNameValuePair("cn_username", cn_name));
		nameValuePairs.add(new BasicNameValuePair("en_username", en_name));
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("company", company));
		nameValuePairs.add(new BasicNameValuePair("building_a", building_a));
		nameValuePairs.add(new BasicNameValuePair("building_b", building_b));
		nameValuePairs.add(new BasicNameValuePair("start_work", worktime));
		nameValuePairs.add(new BasicNameValuePair("region_id", region_id));
		nameValuePairs.add(new BasicNameValuePair("good_type", good_type));
		nameValuePairs.add(new BasicNameValuePair("wechat", wechat));
		nameValuePairs
				.add(new BasicNameValuePair("wechat_qr_id", wechat_qr_id));
		nameValuePairs.add(new BasicNameValuePair("avatar_id", avatar_id));
		nameValuePairs
				.add(new BasicNameValuePair("post_card_id", post_card_id));
		nameValuePairs
				.add(new BasicNameValuePair("service_type", service_type));
		System.out.println(sb.toString() + "-session_id-" + session_id
				+ "-type-" + type + "-cn_username-" + cn_name + "-en_username-"
				+ en_name + "-email-" + email + "-company-" + company
				+ "-building_a-" + building_a + "-building_b-" + building_b
				+ "-start_work-" + worktime + "-region_id-" + region_id
				+ "-wechat-" + wechat + "-wechat_qr_id-" + wechat_qr_id
				+ "-post_card_id-" + post_card_id + "-service_type-" + service_type
				+ "-avatar_id-" + avatar_id);
		return new Request(REQUEST_NEW, sb.toString(), nameValuePairs);
	}

	// 易楼邦
	public static Request request_yi(String session_id, String page,
			String subject) {
		StringBuffer sb = new StringBuffer(Constant.yi);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("page", page));
		nameValuePairs.add(new BasicNameValuePair("subject", subject));
		System.out.println(sb.toString() + "-session_id-" + session_id
				+ "-page-" + page + "-subject-" + subject);
		return new Request(REQUEST_NEW, sb.toString(), nameValuePairs);
	}

	// 反馈
	public static Request request_feedback(String session_id, String content) {
		StringBuffer sb = new StringBuffer(Constant.feedback);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		System.out.println(sb.toString() + "-session_id-" + session_id);
		return new Request(REQUEST_NEW, sb.toString(), nameValuePairs);
	}

	// 委托
	public static Request request_entrust(String session_id, String type,
			String content, String phone, String name) {
		StringBuffer sb = new StringBuffer(Constant.entrust);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		nameValuePairs.add(new BasicNameValuePair("phone_number", phone));
		nameValuePairs.add(new BasicNameValuePair("name", name));
		System.out.println(sb.toString() + "-session_id-" + session_id);
		return new Request(REQUEST_NEW, sb.toString(), nameValuePairs);
	}

	// 增值服务商首页
	public static Request request_addservices(String session_id,String id) {
		StringBuffer sb = new StringBuffer(Constant.addservices);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("service_type", id));
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		System.out.println(sb.toString() + "-service_type-" + id);
		return new Request(REQUEST_NEW, sb.toString(), nameValuePairs);
	}

	// 增值服务商个人
	public static Request request_addservices_per(String id) {
		StringBuffer sb = new StringBuffer(Constant.addservices_per);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", id));
		System.out.println(sb.toString() + "-id-" + id);
		return new Request(REQUEST_NEW, sb.toString(), nameValuePairs);
	}

	// 增值服务商公司
	public static Request request_addservices_company(String company) {
		StringBuffer sb = new StringBuffer(Constant.addservices_company);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("pp_name", company));
		System.out.println(sb.toString() + "-company-" + company);
		return new Request(REQUEST_NEW, sb.toString(), nameValuePairs);
	}

	// 增值服务商成交信息新增
	public static Request request_addservice_cjinfo(String session_id,
			String customer_name, String building_name, String service_type,
			String cj_time, String industry, String proof, String proof_id,
			String case_id) {
		StringBuffer sb = new StringBuffer(Constant.addservices_cjinfoadd);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("customer_name",
				customer_name));
		nameValuePairs.add(new BasicNameValuePair("type", "2"));
		nameValuePairs.add(new BasicNameValuePair("building_name",
				building_name));
		nameValuePairs
				.add(new BasicNameValuePair("service_type", service_type));
		nameValuePairs.add(new BasicNameValuePair("cj_time", cj_time));
		nameValuePairs.add(new BasicNameValuePair("industry", industry));
		nameValuePairs.add(new BasicNameValuePair("contact", proof));
		nameValuePairs.add(new BasicNameValuePair("proof_id", proof_id));
		nameValuePairs.add(new BasicNameValuePair("case_id", case_id));
		System.out
				.println(sb.toString() + "--" + session_id + "---" + proof_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 增值服务商成交信息编辑
	public static Request request_editservice_cjinfo(String session_id,
			String id, String customer_name, String building_name,
			String service_type, String cj_time, String industry,
			String contact, String proof_id, String case_id) {
		StringBuffer sb = new StringBuffer(Constant.addservices_cjinfoedit);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("customer_name",
				customer_name));
		nameValuePairs.add(new BasicNameValuePair("type", "2"));
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("building_name",
				building_name));
		nameValuePairs
				.add(new BasicNameValuePair("service_type", service_type));
		nameValuePairs.add(new BasicNameValuePair("cj_time", cj_time));
		nameValuePairs.add(new BasicNameValuePair("industry", industry));
		nameValuePairs.add(new BasicNameValuePair("contact", contact));
		nameValuePairs.add(new BasicNameValuePair("proof_id", proof_id));
		nameValuePairs.add(new BasicNameValuePair("case_id", case_id));
		System.out.println(sb.toString() + "--" + session_id + "---"
				+ "--proof_id--" + proof_id + "--customer_name--"
				+ customer_name + "--case_id--" + case_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 增值服务商成交首页
	public static Request request_addservice_cjinfomain(String session_id) {
		StringBuffer sb = new StringBuffer(Constant.addservices_cjinfomain);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("type", "2"));
		System.out.println(sb.toString() + "--" + session_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 增值服务商成交单个信息
	public static Request request_addservice_cjinfo(String session_id, String id) {
		StringBuffer sb = new StringBuffer(Constant.addservices_cjinfo);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("id", id));
		System.out.println(sb.toString() + "--" + session_id);
		return new Request(REQUEST_NEW, sb.toString(), nameValuePairs);
	}

	// 经济服务首页
	public static Request request_economic_service(String session_id) {
		StringBuffer sb = new StringBuffer(Constant.economic_service);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		System.out.println(sb.toString());
		return new Request(REQUEST_NEW, sb.toString(), nameValuePairs);
	}

	// 帖子筛选
	public static Request request_searchthread(String session_id, String page,
			String subject, String area_code) {
		StringBuffer sb = new StringBuffer(Constant.searchthread);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("page", page));
		nameValuePairs.add(new BasicNameValuePair("subject", subject));
		nameValuePairs.add(new BasicNameValuePair("area_code", area_code));
		System.out.println(sb.toString() + "--" + page + "==" + subject + "!!"
				+ area_code);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 帖子筛选
	public static Request request_hoursesearch(String trs_type, String meter,
			String price, String unit, String regions, String type) {
		StringBuffer sb = new StringBuffer(Constant.hoursesearch);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("trs_type", trs_type));
		nameValuePairs.add(new BasicNameValuePair("meter", meter));
		nameValuePairs.add(new BasicNameValuePair("price", price));
		nameValuePairs.add(new BasicNameValuePair("unit", unit));
		nameValuePairs.add(new BasicNameValuePair("regions", regions));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		System.out.println(sb.toString() + "--" + trs_type + "==" + meter
				+ "!!" + price + "--" + unit + "==" + regions + "!!" + type);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 纠错举报
	public static Request request_error(String session_id, String title,
			String content, String type, String report_type, String building_id) {
		StringBuffer sb = new StringBuffer(Constant.error);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("session_id", session_id));
		nameValuePairs.add(new BasicNameValuePair("title", title));
		nameValuePairs.add(new BasicNameValuePair("content", content));
		nameValuePairs.add(new BasicNameValuePair("type", type));
		nameValuePairs.add(new BasicNameValuePair("report_type", report_type));
		nameValuePairs.add(new BasicNameValuePair("building_id", building_id));
		System.out.println(sb.toString() + "--" + session_id);
		return new Request(REQUEST_ECONOMIC, sb.toString(), nameValuePairs);
	}

	// 分享统计的接口
	public static Request requestsharelog(String id, String shareto) {
		StringBuffer sb = new StringBuffer(Constant.sharelog);
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", id));
		nameValuePairs.add(new BasicNameValuePair("shareto", shareto));
		return new Request(REQUEST_SHARELOG, sb.toString(), nameValuePairs);
	}

	/**
	 * 
	 * @param webView
	 * @param url
	 * @param dishOrderList
	 */
	public static void requestPost(final WebView webView, final String url,
			String cli_id) {
		JSONObject json = new JSONObject();
		try {
			json.put("cli_id", cli_id);
			final ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs
					.add(new BasicNameValuePair("jsons", json.toString()));

			new Thread(new Runnable() {

				@Override
				public void run() {
					HttpResponse response = null;
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(url);
					try {
						httppost.setEntity(new UrlEncodedFormEntity(
								nameValuePairs, "UTF-8"));
						response = httpclient.execute(httppost);
						data = EntityUtils.toString(response.getEntity(),
								"UTF-8");
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						webView.loadDataWithBaseURL(url, data, "text/html",
								"UTF-8", null);
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
