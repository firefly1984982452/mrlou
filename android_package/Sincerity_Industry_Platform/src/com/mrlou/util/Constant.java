package com.mrlou.util;

import java.io.File;
import java.io.IOException;

import android.os.Environment;

public class Constant {
	public static boolean isInitEnter;
	public final static String load_version = "http://appapi.imrlou.com/api/version";
	// public final static String upload =
	// "http://appapi.imrlou.com/api/upload";
	public final static String upload = "http://appapi.imrlou.com/api/upload";
	public final static String api_key = "jqRPGQeH04UeIT2n1AzbmVqT";
	public final static String appapi_id = "7124101";
	public final static String loginbutoken = "http://appapi.imrlou.com/api/loginbytoken";
	public final static String sharecreate = "http://appapi.imrlou.com/api/sharecreate";
	public final static String sharelog = "http://appapi.imrlou.com/api/sharelog";
	// 五年+经纪人首公司品牌接口
	public final static String economic_company = "http://appapi.imrlou.com/api/getcompanybyname";
	// 五年+经济人个人品牌接口
	public final static String economic_personal = "http://appapi.imrlou.com/api/getagentinfo";
	// 五年+经纪人首页区域接口
	public final static String economic_main = "http://appapi.imrlou.com/api/getagentlist";
	// 五年+经纪人搜索接口
	public final static String economic_search = "http://appapi.imrlou.com/api/agenttip";
	// 经纪人收藏接口
	public final static String economic_collect = "http://appapi.imrlou.com/api/collect";
	// 版本升级
	public final static String update = "http://appapi.imrlou.com/api/getversion";
	// 最新认证房源
	public final static String message_newhourse = "http://appapi.imrlou.com/api/getverifiedroom";
	// 独栋
	public final static String message_standingoffice = "http://appapi.imrlou.com/api/standingoffice";
	// 短租
	public final static String message_shorthourse = "http://appapi.imrlou.com/api/unionoffice";
	// 分类列表信息
	public final static String message_labelmessage = "http://appapi.imrlou.com/api/newslist";
	// 消息模块房源工作日志 客户工作日志 楼先生 帖子提醒
	public final static String message_message = "http://appapi.imrlou.com/api/eventlist";
	// 通知消息页
	public final static String message_notice = "http://appapi.imrlou.com/api/getusernotice";
	// 客户首页
	public final static String customer_manager = "http://appapi.imrlou.com/api/customerindex";
	// 客户信息
	public final static String customer_info = "http://appapi.imrlou.com/api/customer";
	// 客户信息修改
	public final static String customer_infoeidt = "http://appapi.imrlou.com/api/customeredit";
	// 客户删除
	public final static String customer_delete = "http://appapi.imrlou.com/api/customerdelete";
	// 客户新增
	public final static String customer_add = "http://appapi.imrlou.com/api/customercreate";
	// 工作日志添加
	public final static String joblogadd = "http://appapi.imrlou.com/api/worklogcreate";
	// 工作日志修改
	public final static String joblogedit = "http://appapi.imrlou.com/api/worklogcreate";
	// 工作日志删除
	public final static String joblogdelete = "http://appapi.imrlou.com/api/worklogremove";
//	// 广告接口
//	public final static String advertise = "http://appapi.imrlou.com/api/getadvlist";
	// 广告接口
	public final static String advertise = "http://appapi.imrlou.com/api/newgetadvlist";
	// 我的房源接口
	public final static String myhourse = "http://appapi.imrlou.com/api/myroomlist";
	// 我的房源详情接口
	public final static String myhourse_detail = "http://appapi.imrlou.com/api/getmanageroom";
	// 消息首页接口(线上)
	public final static String message_main = "http://appapi.imrlou.com/api/messageindexs";
//	// 消息首页接口(测试)
//	public final static String message_main = "http://appapi.imrlou.com/api/messageindex";
	// 历史成交新增
	public final static String cjinfo_add = "http://appapi.imrlou.com/api/cjinfo";
	// 历史成交编辑
	public final static String cjinfo_edit = "http://appapi.imrlou.com/api/cjinfo";
	// 历史成交首页
	public final static String cjinfo_main = "http://appapi.imrlou.com/api/myhistory";
	// 房源信息接口
	public final static String roominfo = "http://appapi.imrlou.com/api/roominfos";
	// 房源信息编辑接口
	public final static String roominfo_eidt = "http://appapi.imrlou.com/api/roomedit";
	// 个人设置修改
	public final static String per_setting = "http://appapi.imrlou.com/api/users";
	// 个人设置添加楼盘
	public final static String per_addbuild = "http://appapi.imrlou.com/api/buildingtipaddr";
	// 获取个人信息
	public final static String per_info = "http://appapi.imrlou.com/api/users";
	// 获取帖子首页
	public final static String per_mypostmain = "http://appapi.imrlou.com/api/mybbs";
	// 取消收藏帖子,收藏楼盘,收藏经纪人,收藏房源
	public final static String per_cancelpost = "http://appapi.imrlou.com/api/uncollect";
	// 修改收藏帖子
	public final static String per_modifypost = "http://appapi.imrlou.com/api/threadstatus";
	// 关于楼先生、帮助中心
	public final static String per_about = "http://appapi.imrlou.com/api/introductions";
	//易楼精英会
	public final static String per_elite = "http://appapi.imrlou.com/api/elite";
	// 收藏夹首页
	public final static String collect_main = "http://appapi.imrlou.com/api/collectionindex";
	// 单个楼盘收藏接口
	public final static String collect_hourse = "http://appapi.imrlou.com/api/getroombyid";
	// 判断是否已收藏
	public final static String is_collect = "http://appapi.imrlou.com/api/iscollect";
	// 刪除失效房源
	public final static String del_hourse = "http://appapi.imrlou.com/api/deletemyroombyid";
	// 会员提交申请
	public final static String insertapply = "http://appapi.imrlou.com/api/insertapplys";
	// 身份修改
	public final static String identify_modify = "http://appapi.imrlou.com/api/changetype";
	// 易楼帮
	public final static String yi = "http://appapi.imrlou.com/api/threadindexs";
	// 反馈
	public final static String feedback = "http://appapi.imrlou.com/api/feedback";
	// 委托
	public final static String entrust = "http://appapi.imrlou.com/api/roomagent";
	// 增值服务商
	public final static String addservices = "http://appapi.imrlou.com/api/getservicelist";
	// 增值服务商个人品牌页
	public final static String addservices_per = "http://appapi.imrlou.com/api/getserviceinfo";
	// 增值服务商公司品牌页
	public final static String addservices_company = "http://appapi.imrlou.com/api/getservicecompany";
	// 增值服务商历史成交首页
	public final static String addservices_cjinfomain = "http://appapi.imrlou.com/api/myhistory";
	// 增值服务商历史成交编辑
	public final static String addservices_cjinfoedit= "http://appapi.imrlou.com/api/cjinfo";
	// 增值服务商历史成交新增
	public final static String addservices_cjinfoadd = "http://appapi.imrlou.com/api/cjinfo";
	// 增值服务商单个历史成交信息
	public final static String addservices_cjinfo = "http://appapi.imrlou.com/api/getcjinfo";
	// 经济服务首页
	public final static String economic_service = "http://appapi.imrlou.com/api/manageserver";
	// 帖子筛选
	public final static String searchthread = "http://appapi.imrlou.com/api/searchthread";
	// 认证房源筛选
	public final static String hoursesearch = "http://appapi.imrlou.com/api/searchverifiedroom";
	// 纠错举报
	public final static String error = "http://appapi.imrlou.com/api/insertreport";
	// 公司名称查询
	public final static String company_select = "http://appapi.imrlou.com/api/newcompanytip";
	// public final static String
	// economic_main="http://192.168.5.111/www_mrlou_com/frontend/web/index.php/api/userbymobilephone"
	public final static String FILE_DIR = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ "louxiansheng"
			+ File.separator + "temp" + File.separator + "image";
	public final static String CACHE_STRING = "headicon_temp.jpg";

	public static File createFile(String fileDirs, String fileName) {
		File file = new File(fileDirs);
		if (!file.exists()) {
			file.mkdirs();
		}
		File nfile = new File(fileDirs + File.separator + fileName);
		if (nfile.exists()) {
			nfile.delete();
		}
		try {
			nfile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return nfile;
	}

}
