package com.mrlou.mine;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.mrlou.listener.HttpListener;
import com.mrlou.mrlou.R;
import com.mrlou.util.HttpThread;
import com.mrlou.util.Request;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 *@author jx_chen
 *@date 2016-11-14---下午7:12:50
 *@problem
 */
public class Add_FriendAct extends Activity implements HttpListener{

	private LinearLayout lay_back,lay_addfriend_share;
	private String[] names = { "微信好友", "朋友圈", "QQ", "QQ空间", "短信", "复制链接", "新浪",
	"邮件" };
	private PopupWindow popupWindow;
	private int[] images = { R.drawable.wechat, R.drawable.wxcicle, R.drawable.qq,
			R.drawable.qzone, R.drawable.sms, R.drawable.copy, R.drawable.sina,
			R.drawable.gmail };
	private String  errorString,shareidString,baseurlString,fenxiangall
			,imgurl,imgid,lasturlString, bulididString,bdobject_type, bdobject_id, bdis_avatar,
			bdmark,bdcut,cur="sss",prams="sss",detail="sss",mapdetail="sss",firsttwo="no",
			shareimg="http://appapi.imrlou.com/lxs/images/lxs_images_code.jpg",sharecontent="",shareurl="",sharetitle="",sharecopy="";
	UMSocialService mController;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mine_add_friend);
		findView();
	}

	private void findView() {
		// TODO Auto-generated method stub
		View contentView = Add_FriendAct.this.getLayoutInflater()
				.inflate(R.layout.popwindow, null);
		GridView gridView = (GridView) contentView.findViewById(R.id.gridView);
		gridView.setAdapter(getAdapter());
		gridView.setOnItemClickListener(new ItemClickListener());
		popupWindow = new PopupWindow(contentView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);// 取得焦点
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.animation);
		lay_back=(LinearLayout) findViewById(R.id.lay_addfriend_back);
		lay_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				simulateKey(KeyEvent.KEYCODE_BACK);
			}
		});
		lay_addfriend_share=(LinearLayout) findViewById(R.id.lay_addfriend_share);
		lay_addfriend_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.showAtLocation(lay_back, Gravity.BOTTOM, 0, 0);
//				Shareto(URLDecoder.decode(content,"utf-8"), URLDecoder.decode(url,"utf-8"), URLDecoder.decode(shareimg,"utf-8"), URLDecoder.decode(copy,"utf-8"), URLDecoder.decode(shareid,"utf-8"));
				mController = UMServiceFactory.getUMSocialService("com.umeng.share");
			}
		});
	}


	SnsPostListener mShareListener = new SnsPostListener() {
		@Override
		public void onStart() {
		}
		@Override
		public void onComplete(SHARE_MEDIA platform, int stCode,
				SocializeEntity entity) {
			if (stCode == 200) {
				sharecallback("success");
			} else {
				sharecallback("failed");
			}
		}
	};

	//分享回调方法
	private void sharecallback(String result){
		if (result.equals("success")) {

			Toast.makeText(Add_FriendAct.this, "分享成功", Toast.LENGTH_SHORT).show();
		}
		if (result.equals("failed")) {
			Toast.makeText(Add_FriendAct.this, "分享失敗", Toast.LENGTH_SHORT).show();
//			if (curwebview.equals("1")) {
//				firstwebView.loadUrl("javascript:shareSucccessCallBack('分享失败')");
//			}else if (curwebview.equals("2")){
//				secondWebView.loadUrl("javascript:shareSucccessCallBack('分享失败')");
//			}
		}
	}
	private final class ItemClickListener implements OnItemClickListener{
		@SuppressWarnings("deprecation")
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0://微信好友
				Fenxiangweixin("找办公楼认准楼先生 ","楼先生致力于服务办公楼租售行业的开放平台", "http://appapi.imrlou.com/lxs/lxs_invite_share.html");
				mController.postShare(Add_FriendAct.this,SHARE_MEDIA.WEIXIN ,
						mShareListener);
				tosharelog(shareidString,SHARE_MEDIA.WEIXIN.toString());
				popupWindow.dismiss();
				break;
			case 1://微信朋友圈
				Fengwxcicle("找办公楼认准楼先生 ","楼先生致力于服务办公楼租售行业的开放平台", "http://appapi.imrlou.com/lxs/lxs_invite_share.html");
				mController.postShare(Add_FriendAct.this,SHARE_MEDIA.WEIXIN_CIRCLE,
						mShareListener);
				tosharelog(shareidString,SHARE_MEDIA.WEIXIN_CIRCLE.toString());
				popupWindow.dismiss();
				break;
			case 2://qq
				Fenxiangqq("找办公楼认准楼先生 ","楼先生致力于服务办公楼租售行业的开放平台", "http://appapi.imrlou.com/lxs/lxs_invite_share.html");
				mController.postShare(Add_FriendAct.this,SHARE_MEDIA.QQ,
						mShareListener);
				tosharelog(shareidString,SHARE_MEDIA.QQ.toString());
				popupWindow.dismiss();
				break;
			case 3://qq空间
				FenxiangqqQz("找办公楼认准楼先生 ","楼先生致力于服务办公楼租售行业的开放平台", "http://appapi.imrlou.com/lxs/lxs_invite_share.html");
				mController.postShare(Add_FriendAct.this,SHARE_MEDIA.QZONE,
						mShareListener);
				tosharelog(shareidString,SHARE_MEDIA.QZONE.toString());
				popupWindow.dismiss();
				break;
			case 4://短信
				FenxiangEmailSms("楼先生致力于服务办公楼租售行业的开放平台", "http://appapi.imrlou.com/lxs/lxs_invite_share.html");
				mController.postShare(Add_FriendAct.this,SHARE_MEDIA.SMS,
						mShareListener);
				tosharelog(shareidString,SHARE_MEDIA.SMS.toString());
				popupWindow.dismiss();
				break;
			case 5://复制
				ClipboardManager cmb = (ClipboardManager)Add_FriendAct.this.getSystemService(Context.CLIPBOARD_SERVICE);  
				cmb.setText(fenxiangall.trim());  
				Toast.makeText(Add_FriendAct.this,"复制成功", Toast.LENGTH_SHORT).show();
				popupWindow.dismiss();
				break;
			case 6://新浪
				FenxiangSina("找办公楼认准楼先生 ","楼先生致力于服务办公楼租售行业的开放平台", "http://appapi.imrlou.com/lxs/lxs_invite_share.html");
				mController.postShare(Add_FriendAct.this,SHARE_MEDIA.SINA,
						mShareListener);
				tosharelog(shareidString,SHARE_MEDIA.SINA.toString());
				popupWindow.dismiss();
				break;
			case 7://邮箱
				FenxiangEmailSms("楼先生致力于服务办公楼租售行业的开放平台", "http://appapi.imrlou.com/lxs/lxs_invite_share.html");
				mController.postShare(Add_FriendAct.this,SHARE_MEDIA.EMAIL,
						mShareListener);
				tosharelog(shareidString,SHARE_MEDIA.EMAIL.toString());
				popupWindow.dismiss();
				break;
			default:
				break;
			}
		}
	}
	private void tosharelog(String id ,String shareto){
		Request request = Request.requestsharelog(id,shareto);
		new HttpThread(request, Add_FriendAct.this);
	}
	public void openPopWindow() {
//		initpopwindow();
//		popupWindow.showAtLocation(myView, Gravity.BOTTOM, 0, 0);
	}

	private void Shareto(String content,String url,String img,String copy,String shareid){
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
//		shareimg = img;
		try {
			sharecontent = URLDecoder.decode(content,"utf-8")+url+URLDecoder.decode(copy,"utf-8");
			sharetitle= URLDecoder.decode(content,"utf-8");
			sharecopy= URLDecoder.decode(copy,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		shareurl = url;

		openPopWindow();
	}

	private void FenxiangEmailSms(String content,String url){
		mController.setShareContent(content);
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
		smsHandler.setTargetUrl(url);
		EmailHandler emailHandler = new EmailHandler();
		emailHandler.addToSocialSDK();
		emailHandler.setTargetUrl(url);
	}

	private void Fenxiangqq(String title,String content,String url){
		QQShareContent qqShareContent = new QQShareContent(new UMImage(Add_FriendAct.this, shareimg));
		qqShareContent.setTitle(title);
		qqShareContent.setShareContent(content);
		qqShareContent.setTargetUrl(url);
		mController.setShareMedia(qqShareContent);
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(Add_FriendAct.this, "1104815437",
				"9sS47vI7nCTIhXvR");
		qqSsoHandler.setTargetUrl(url);
		qqSsoHandler.addToSocialSDK();  

	}

	private void FenxiangqqQz(String title,String content,String url){
		QZoneShareContent qZoneShareContent = new QZoneShareContent(new UMImage(Add_FriendAct.this, shareimg));
		qZoneShareContent.setTitle(title);

		qZoneShareContent.setShareContent(content);
		mController.setShareMedia(qZoneShareContent);
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(Add_FriendAct.this, "1104815437",
				"9sS47vI7nCTIhXvR");
		qZoneSsoHandler.setTargetUrl(url);
		qZoneSsoHandler.addToSocialSDK();

	}

	private void FenxiangSina(String title,String content,String url){
		SinaShareContent sinaShareContent = new SinaShareContent(new UMImage(Add_FriendAct.this, shareimg));
		sinaShareContent.setShareContent(content);
		sinaShareContent.setTitle(title);
		sinaShareContent.setTargetUrl(url);
		mController.setShareMedia(sinaShareContent);
		SinaSsoHandler sinaSsoHandler = new SinaSsoHandler();
		sinaSsoHandler.setTargetUrl(url);

	}


	private void Fenxiangweixin(String title,String content,String url){
		WeiXinShareContent weiXinShareContent = new WeiXinShareContent(new UMImage(Add_FriendAct.this, shareimg));
		weiXinShareContent.setShareContent(content);
		weiXinShareContent.setTargetUrl(url);
		weiXinShareContent.setTitle(title);
		mController.setShareMedia(weiXinShareContent);
		String appID = "wx5f8d6e9fa8ae4fa7";
		String appSecret = "ffc05368ec2a0093d6a3181602beaf25";
		UMWXHandler wxHandler = new UMWXHandler(Add_FriendAct.this,appID,appSecret);
		wxHandler.addToSocialSDK();
	}


	private void Fengwxcicle(String title,String content,String url) {
		String appID = "wx5f8d6e9fa8ae4fa7";
//		String appSecret ="ffc05368ec2a0093d6a3181602beaf25";
		String appSecret = "ffc05368ec2a0093d6a3181602beaf25";
		UMWXHandler wxCircleHandler = new UMWXHandler(Add_FriendAct.this,appID,appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.setTitle(title);
		wxCircleHandler.setTargetUrl(url);
		wxCircleHandler.addToSocialSDK();
		CircleShareContent circleShareContent  = new CircleShareContent(new UMImage(Add_FriendAct.this, shareimg));
		circleShareContent.setTitle(title);
		circleShareContent.setShareContent(content);
		circleShareContent.setTargetUrl(url);
		mController.setShareMedia(circleShareContent);

	}
	
	private ListAdapter getAdapter() {
		List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < images.length; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("image", images[i]);
			item.put("name", names[i]);
			data.add(item);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(Add_FriendAct.this, data,
				R.layout.grid_item, new String[] { "image", "name" },
				new int[] { R.id.imageView, R.id.textView });
		return simpleAdapter;
	}
	
	// 调用返回键的功能
		public void simulateKey(final int KeyCode) {
			new Thread() {
				public void run() {
					try {
						Instrumentation inst = new Instrumentation();
						inst.sendKeyDownUpSync(KeyCode);
						Add_FriendAct.this.finish();
					} catch (Exception e) {
						Log.e("Exception when sendKeyDownUpSync", e.toString());
					}
				}
			}.start();
		}

	@Override
	public void doResponse(int reqID, String b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doError(String s) {
		// TODO Auto-generated method stub
		
	}
}
