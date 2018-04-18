package com.mrlou.activity;

import java.io.File;

import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.cloud.model.AMapCloudException;
import com.amap.api.cloud.model.CloudItem;
import com.amap.api.cloud.model.CloudItemDetail;
import com.amap.api.cloud.search.CloudResult;
import com.amap.api.cloud.search.CloudSearch;
import com.amap.api.cloud.search.CloudSearch.OnCloudSearchListener;
import com.amap.api.cloud.search.CloudSearch.SearchBound;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.NaviPara;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.mrlou.mrlou.R;
import com.mrlou.poilay.PoiOverlay;
import com.mrlou.util.CloundUtil;
import com.mrlou.util.SystemBarTintManager;
import com.mrlou.util.SystemBarTintManager.SystemBarConfig;
import com.mrlou.view.ActionSheetDialog;
import com.mrlou.view.ActionSheetDialog.OnSheetItemClickListener;
import com.mrlou.view.ActionSheetDialog.SheetItemColor;

public class BasicMapActivity extends Activity implements
		AMap.OnMapLoadedListener, PoiSearch.OnPoiSearchListener,
		AMap.OnMarkerClickListener, CloudSearch.OnCloudSearchListener,
		AMapLocationListener, AMap.InfoWindowAdapter,
		AMap.OnInfoWindowClickListener {
	private MapView mapView;
	private Context context;
	private RelativeLayout mainLayout;
	private AMap aMap;
	private LinearLayout dt_ll, gj_ll, sc_ll, cy_ll, jd_ll, yh_ll, jj_ll,
			bottom;
	private TextView dt_tv, gj_tv, sc_tv, cy_tv, jd_tv, yh_tv, jj_tv, name, fj,
			daohang, titletext;
	private ImageView dt, gj, sc, cy, jd, yh, jj, back;
	private String notchoosecolor = "#000000", wuxiaoString = "#bab4bf",
			choosedcolor = "#ff0000";
	// <---------周边控件
	private MarkerOptions markerOption;
	private LatLng latlng;
	private com.amap.api.services.core.LatLonPoint lp;
	private PoiSearch.Query query;
	private PoiSearch poiSearch;
	private Marker detailMarker;
	private PoiResult poiResult;
	private List<PoiItem> poiItems = new ArrayList<PoiItem>();
	private com.amap.api.maps.overlay.PoiOverlay poiOverlay;
	// <---------云图
	private ArrayList<CloudItem> items = new ArrayList<CloudItem>();
	private CloudSearch mCloudSearch;
	private String mTableID = "560c835ce4b0c3f1af9a5c93";
	private String mKeyWord = "";
	private String mLocalCityName = "";
	private CloudSearch.Query mQuery;
	private String TAG = "AMapYunTuDemo";
	private Marker mCloudIDMarer;
	private PoiOverlay mPoiCloudOverlay;
	private List<CloudItem> mCloudItems = new ArrayList<CloudItem>();
	private String buliding_id;
	private String buliding_name;
	private String buliding_address;
	// 调用百度之前的定位
	private LocationManagerProxy mLocationManagerProxy;
	private String dwlon = "";
	private String dwlat = "";
	private com.amap.api.cloud.model.LatLonPoint lp2;
	Typeface customFont;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basicmap_activity);
		context = this;
		customFont = Typeface.createFromAsset(this.getAssets(),
				"SourceHanSansCN-Normal.ttf");
		Bundle data = getIntent().getExtras();
		latlng = new LatLng(Double.parseDouble(data.getString("lat")),
				Double.parseDouble(data.getString("lon")));
		lp = new com.amap.api.services.core.LatLonPoint(Double.parseDouble(data
				.getString("lat")), Double.parseDouble(data.getString("lon")));
		lp2 = new com.amap.api.cloud.model.LatLonPoint(Double.parseDouble(data
				.getString("lat")), Double.parseDouble(data.getString("lon")));
		buliding_id = data.getString("buliding_id");
		buliding_name = data.getString("title").trim();
		buliding_address = data.getString("address");
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);
		init();
		initbottom();
		initBenMarker();
		Titlebarimmersion();
		setUpMap();
		System.out.println("===="+sHA1(BasicMapActivity.this));
	}

	public static String sHA1(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), PackageManager.GET_SIGNATURES);
			byte[] cert = info.signatures[0].toByteArray();
			MessageDigest md = MessageDigest.getInstance("SHA1");
			byte[] publicKey = md.digest(cert);
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < publicKey.length; i++) {
				String appendString = Integer.toHexString(0xFF & publicKey[i])
						.toUpperCase(Locale.US);
				if (appendString.length() == 1)
					hexString.append("0");
				hexString.append(appendString);
				hexString.append(":");
			}
			return hexString.toString();
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void Titlebarimmersion() {
		if (Build.VERSION.SDK_INT >= 19) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setTintColor(Color.parseColor("#ffffff"));
		SystemBarConfig config = tintManager.getConfig();
		mainLayout = (RelativeLayout) findViewById(R.id.mainlayout);
		int sdkcode = android.os.Build.VERSION.SDK_INT;
		if (sdkcode > 18) {
			mainLayout.setPadding(0, getStatusHeight(context), 0,
					config.getPixelInsetBottom());
		}

	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	public static int getStatusHeight(Context context) {
		int statusHeight = -1;
		try {
			Class<?> clazz = Class.forName("com.android.internal.R$dimen");
			Object object = clazz.newInstance();
			int height = Integer.parseInt(clazz.getField("status_bar_height")
					.get(object).toString());
			statusHeight = context.getResources().getDimensionPixelSize(height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusHeight;
	}

	// <--------云图
	@Override
	public void onCloudItemDetailSearched(CloudItemDetail item, int rCode) {
		if (rCode == 0 && item != null) {
			if (mCloudIDMarer != null) {
				mCloudIDMarer.destroy();
			}
			aMap.clear();
			LatLng position = CloundUtil.convertToLatLng(item.getLatLonPoint());
			aMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(new CameraPosition(position, 18, 0, 30)));
			mCloudIDMarer = aMap.addMarker(new MarkerOptions()
					.position(position).title(item.getTitle())
					.icon(BitmapDescriptorFactory.defaultMarker(210.0F)));
			if (!item.getTitle().equals(buliding_name)) {
				items.add(item);
			}
			Log.d(TAG, "_id" + item.getID());
			Log.d(TAG, "_location" + item.getLatLonPoint().toString());
			Log.d(TAG, "_name" + item.getTitle());
			Log.d(TAG, "_address" + item.getSnippet());
			Log.d(TAG, "_caretetime" + item.getCreatetime());
			Log.d(TAG, "_updatetime" + item.getUpdatetime());
			Log.d(TAG, "_distance" + item.getDistance());
			Iterator<?> iter = item.getCustomfield().entrySet().iterator();
			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
				Log.d(TAG, key + "   " + val);
			}
		}
	}

	@Override
	public void onCloudSearched(CloudResult result, int rCode) {
		System.out.println("-----" + "recode" + rCode);
		// Log.e("+++++++++++++++mapresult",
		// result.getClouds()+""+"recode"+rCode);
		Log.e("name=============", buliding_name);
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {
				if (result.getQuery().equals(mQuery)) {
					mCloudItems = result.getClouds();
					if (mCloudItems != null && mCloudItems.size() > 0) {
						for (int i = 0; i < mCloudItems.size(); i++) {
							if (mCloudItems.get(i).getTitle()
									.equals(buliding_name)) {
								Log.e("99999999999999999", "zhixingle" + i);
								mCloudItems.remove(i);
								continue;
							}
						}
						aMap.clear();
						mPoiCloudOverlay = new PoiOverlay(aMap, mCloudItems);
						mPoiCloudOverlay.removeFromMap();
						mPoiCloudOverlay.addToMap();
						for (CloudItem item : mCloudItems) {
							Log.e("+++++++++++itemtitle", item.getTitle());
							if (!item.getTitle().equals(buliding_name)) {
								Log.e("zhixingle00000000000", "1111111111");
								items.add(item);
								continue;
							}
							Log.d(TAG, "_id " + item.getID());
							Log.d(TAG, "_location "
									+ item.getLatLonPoint().toString());
							Log.d(TAG, "_name " + item.getTitle());
							Log.d(TAG, "_address " + item.getSnippet());
							Log.d(TAG, "_caretetime " + item.getCreatetime());
							Log.d(TAG, "_updatetime " + item.getUpdatetime());
							Log.d(TAG, "_distance " + item.getDistance());
							Iterator<?> iter = item.getCustomfield().entrySet()
									.iterator();
							while (iter.hasNext()) {
								@SuppressWarnings("rawtypes")
								Map.Entry entry = (Map.Entry) iter.next();
								Object key = entry.getKey();
								Object val = entry.getValue();
								Log.d(TAG, key + "   " + val);
							}
						}

						if (mQuery.getBound().getShape()
								.equals(SearchBound.BOUND_SHAPE)) {// 圆形

							aMap.moveCamera(CameraUpdateFactory
									.changeLatLng(new LatLng(lp2.getLatitude(),
											lp2.getLongitude())));
							aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
							addMarkersToMap();
						}

					} else {
						Toast.makeText(BasicMapActivity.this, "无返回结果1",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(BasicMapActivity.this, "无返回结果2",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(BasicMapActivity.this, "网络连接错误",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	public void searchByLocal() {
		items.clear();
		SearchBound bound = new SearchBound(lp2, Toast.LENGTH_SHORT);// 设置检索范围 ;
		try {
			mQuery = new CloudSearch.Query(mTableID, mKeyWord, bound);
			mQuery.setPageSize(30);// 设置每页最多返回多少条poiitem
			mQuery.setPageNum(0);// 设置查第一页
			mCloudSearch.searchCloudAsyn(mQuery);
		} catch (AMapCloudException e) {
			e.printStackTrace();
		}
	}

	// <-----------marker图标
	@SuppressLint("InflateParams")
	@Override
	public View getInfoContents(Marker arg0) {
		String idString = arg0.getTitle();
		View view = null;
		if (arg0.getTitle().equals(buliding_name)) {
			LayoutInflater inflater = LayoutInflater
					.from(BasicMapActivity.this);
			view = inflater.inflate(R.layout.infowindow, null);
			TextView titleTextView = (TextView) view
					.findViewById(R.id.infotitle);
			titleTextView.setText(arg0.getTitle());
			titleTextView.setTypeface(customFont);
			titleTextView.setIncludeFontPadding(false);
			titleTextView.setPadding(0, 10, 0, 10);
			TextView contentTextView = (TextView) view
					.findViewById(R.id.infocontent);
			contentTextView.setText(arg0.getSnippet());
			contentTextView.setTypeface(customFont);
			contentTextView.setIncludeFontPadding(false);
			contentTextView.setPadding(0, 10, 0, 10);
		}
		for (CloudItem item : items) {
			if (idString.equals(item.getTitle())) {
				LayoutInflater inflater = LayoutInflater
						.from(BasicMapActivity.this);
				view = inflater.inflate(R.layout.infowindow, null);
				TextView titleTextView = (TextView) view
						.findViewById(R.id.infotitle);
				titleTextView.setText(arg0.getTitle());
				titleTextView.setTypeface(customFont);
				titleTextView.setIncludeFontPadding(false);
				titleTextView.setPadding(0, 10, 0, 10);
				TextView contentTextView = (TextView) view
						.findViewById(R.id.infocontent);
				contentTextView.setText(arg0.getSnippet());
				contentTextView.setTypeface(customFont);
				contentTextView.setIncludeFontPadding(false);
				contentTextView.setPadding(0, 10, 0, 10);
			}
		}

		for (PoiItem poiItem : poiItems) {
			if (idString.equals(poiItem.getTitle())) {
				LayoutInflater inflater = LayoutInflater
						.from(BasicMapActivity.this);
				view = inflater.inflate(R.layout.infowindow, null);
				TextView titleTextView = (TextView) view
						.findViewById(R.id.infotitle);
				titleTextView.setText(arg0.getTitle());
				titleTextView.setTypeface(customFont);
				titleTextView.setIncludeFontPadding(false);
				titleTextView.setPadding(0, 10, 0, 10);
				TextView contentTextView = (TextView) view
						.findViewById(R.id.infocontent);
				contentTextView.setText(arg0.getSnippet());
				contentTextView.setTypeface(customFont);
				contentTextView.setIncludeFontPadding(false);
				contentTextView.setPadding(0, 10, 0, 10);
				ImageView imageView = (ImageView) view
						.findViewById(R.id.infoimg);
				imageView.setVisibility(View.GONE);
			}
		}
		return view;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		return null;
	}

	// <----------infowindow点击事件
	@Override
	public void onInfoWindowClick(Marker arg0) {
		String idString = arg0.getTitle();
		if (arg0.getTitle().equals(buliding_name)) {
			Intent intent = new Intent();
			intent.putExtra("lasturl", "lxs_buliding_detail.html");
			intent.putExtra("bulidid", buliding_id);
			setResult(7, intent);
			finish();
		}
		for (CloudItem item : items) {
			if (idString.equals(item.getTitle())) {
				Intent intent = new Intent();
				intent.putExtra("lasturl", "lxs_buliding_detail.html");
				intent.putExtra("bulidid",
						item.getCustomfield().get("building_id"));
				setResult(7, intent);
				finish();
				break;
			}
		}
	}

	// <------------周边完成
	@Override
	public boolean onMarkerClick(Marker marker) {
		if (poiOverlay != null && poiItems != null && poiItems.size() > 0) {
			detailMarker = marker;
		}
		return false;
	}

	private void dosearchquery(String keyword, String type, String city) {
		aMap.setOnMapClickListener(null);// 进行poi搜索时清除掉地图点击事件
		query = new PoiSearch.Query(keyword, type, city);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		query.setLimitDiscount(false);
		query.setLimitGroupbuy(false);
		query.setPageSize(Toast.LENGTH_SHORT);
		query.setPageNum(0);
		poiSearch = new PoiSearch(this, query);
		poiSearch
				.setBound(new com.amap.api.services.poisearch.PoiSearch.SearchBound(
						lp, Toast.LENGTH_SHORT));
		poiSearch.setOnPoiSearchListener((OnPoiSearchListener) this);// 设置数据返回的监听器
		poiSearch.searchPOIAsyn();// 开始搜索
	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail result, int rCode) {
		if (rCode == 0) {
			if (result != null) {// 搜索poi的结果
				if (detailMarker != null) {
					StringBuffer sb = new StringBuffer(result.getSnippet());
					if (result.getDeepType() != null) {
						detailMarker.setSnippet(sb.toString());
						detailMarker.showInfoWindow();
					} else {
						Toast.makeText(BasicMapActivity.this, "无返回数据",
								Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				Toast.makeText(BasicMapActivity.this, "无返回数据",
						Toast.LENGTH_SHORT).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(BasicMapActivity.this, "无返回数据", Toast.LENGTH_SHORT)
					.show();
		} else if (rCode == 32) {
			Toast.makeText(BasicMapActivity.this, "无返回数据", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(BasicMapActivity.this, "其他错误" + rCode,
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {
				if (result.getQuery().equals(query)) {
					poiResult = result;
					poiItems = poiResult.getPois();
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();
					if (poiItems != null && poiItems.size() > 0) {
						aMap.clear();// 清理之前的图标
						poiOverlay = new com.amap.api.maps.overlay.PoiOverlay(
								aMap, poiItems);
						poiOverlay.removeFromMap();
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();
						setUpMap();
					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
						Toast.makeText(BasicMapActivity.this,
								"其他类型" + suggestionCities, Toast.LENGTH_SHORT)
								.show();
					} else {
						Toast.makeText(BasicMapActivity.this, "无返回数据",
								Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				Toast.makeText(BasicMapActivity.this, "无返回数据",
						Toast.LENGTH_SHORT).show();
			}
		} else if (rCode == 27) {
			Toast.makeText(BasicMapActivity.this, "网络异常", Toast.LENGTH_SHORT)
					.show();
		} else if (rCode == 32) {
			Toast.makeText(BasicMapActivity.this, "配置错误", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(BasicMapActivity.this, "其他错误", Toast.LENGTH_SHORT)
					.show();
		}
	}

	// -------------------增加marker
	private void setUpMap() {
		aMap.setOnMapLoadedListener(this);
		addMarkersToMap();
	}

	private void addMarkersToMap() {
		// markerOption = new MarkerOptions();
		// markerOption.position(latlng);
		// markerOption.title(buliding_name).snippet(buliding_address);
		// markerOption.draggable(true);
		// markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
		// .decodeResource(getResources(), R.drawable.p)));
		// aMap.addMarker(markerOption);

		this.markerOption = new MarkerOptions();
		this.markerOption.position(this.latlng);
		this.markerOption.title(this.buliding_name).snippet(
				this.buliding_address);
		this.markerOption.draggable(true);
		this.markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
				.decodeResource(getResources(), R.drawable.p)));
		this.aMap.addMarker(this.markerOption);
	}

	@Override
	public void onMapLoaded() {
		LatLngBounds bounds = new LatLngBounds.Builder().include(latlng)
				.build();
		aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
	}

	// ---------------<初始化布局以及点击事件
	private void initbottom() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int phonewith = metrics.widthPixels;
		back = (ImageView) findViewById(R.id.back);
		titletext = (TextView) findViewById(R.id.titletext);
		daohang = (TextView) findViewById(R.id.daohang);
		bottom = (LinearLayout) findViewById(R.id.bottom);
		dt_ll = (LinearLayout) findViewById(R.id.dt_ll);
		gj_ll = (LinearLayout) findViewById(R.id.gj_ll);
		sc_ll = (LinearLayout) findViewById(R.id.sc_ll);
		cy_ll = (LinearLayout) findViewById(R.id.cy_ll);
		jd_ll = (LinearLayout) findViewById(R.id.jd_ll);
		yh_ll = (LinearLayout) findViewById(R.id.yh_ll);
		jj_ll = (LinearLayout) findViewById(R.id.jj_ll);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				phonewith * 20 / 320, phonewith * 20 / 320);
		dt = (ImageView) findViewById(R.id.dt);
		dt.setLayoutParams(params);
		gj = (ImageView) findViewById(R.id.gj);
		gj.setLayoutParams(params);
		sc = (ImageView) findViewById(R.id.sc);
		sc.setLayoutParams(params);
		cy = (ImageView) findViewById(R.id.cy);
		cy.setLayoutParams(params);
		jd = (ImageView) findViewById(R.id.jd);
		jd.setLayoutParams(params);
		yh = (ImageView) findViewById(R.id.yh);
		yh.setLayoutParams(params);
		jj = (ImageView) findViewById(R.id.jj);
		jj.setLayoutParams(params);
		dt_tv = (TextView) findViewById(R.id.dt_tv);
		gj_tv = (TextView) findViewById(R.id.gj_tv);
		sc_tv = (TextView) findViewById(R.id.sc_tv);
		cy_tv = (TextView) findViewById(R.id.cy_tv);
		jd_tv = (TextView) findViewById(R.id.jd_tv);
		yh_tv = (TextView) findViewById(R.id.yh_tv);
		jj_tv = (TextView) findViewById(R.id.jj_tv);
		titletext.setTypeface(customFont);
		titletext.setIncludeFontPadding(false);
		titletext.setPadding(0, 10, 0, 10);
		daohang.setTypeface(customFont);
		daohang.setIncludeFontPadding(false);
		daohang.setPadding(0, 10, 0, 10);
		dt_tv.setTypeface(customFont);
		dt_tv.setIncludeFontPadding(false);
		dt_tv.setPadding(0, 10, 0, 10);
		gj_tv.setTypeface(customFont);
		gj_tv.setIncludeFontPadding(false);
		gj_tv.setPadding(0, 10, 0, 10);
		sc_tv.setTypeface(customFont);
		sc_tv.setPadding(0, 10, 0, 10);
		sc_tv.setIncludeFontPadding(false);
		cy_tv.setTypeface(customFont);
		cy_tv.setIncludeFontPadding(false);
		cy_tv.setPadding(0, 10, 0, 10);
		jd_tv.setTypeface(customFont);
		jd_tv.setIncludeFontPadding(false);
		jd_tv.setPadding(0, 10, 0, 10);
		yh_tv.setTypeface(customFont);
		yh_tv.setIncludeFontPadding(false);
		yh_tv.setPadding(0, 10, 0, 10);
		jj_tv.setTypeface(customFont);
		jj_tv.setIncludeFontPadding(false);
		jj_tv.setPadding(0, 10, 0, 10);
		dt_ll.setOnClickListener(new MyClickListener());
		gj_ll.setOnClickListener(new MyClickListener());
		sc_ll.setOnClickListener(new MyClickListener());
		cy_ll.setOnClickListener(new MyClickListener());
		jd_ll.setOnClickListener(new MyClickListener());
		yh_ll.setOnClickListener(new MyClickListener());
		jj_ll.setOnClickListener(new MyClickListener());
		back.setOnClickListener(new MyClickListener());
		daohang.setOnClickListener(new MyClickListener());

	}

	private void initBenMarker() {
		name = (TextView) findViewById(R.id.name);
		fj = (TextView) findViewById(R.id.fj);
		name.setTypeface(customFont);
		fj.setIncludeFontPadding(false);
		name.setIncludeFontPadding(false);
		fj.setTypeface(customFont);
		name.setText(buliding_name);
		fj.setText("附近楼盘");
		name.setOnClickListener(new MyClickListener());
		fj.setOnClickListener(new MyClickListener());

	}

	private class MyClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			int t = v.getId();
			switch (t) {
			case R.id.dt_ll:
				changeImage(0);
				dosearchquery("", "地铁", mLocalCityName);
				setUpMap();
				aMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(new CameraPosition(latlng, 18, 0, 30)));
				break;
			case R.id.gj_ll:
				changeImage(1);
				dosearchquery("", "公交", mLocalCityName);
				aMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(new CameraPosition(latlng, 18, 0, 30)));
				break;
			case R.id.sc_ll:
				changeImage(2);
				dosearchquery("", "商场", mLocalCityName);
				aMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(new CameraPosition(latlng, 18, 0, 30)));
				break;
			case R.id.cy_ll:
				changeImage(3);
				dosearchquery("", "餐饮", mLocalCityName);
				aMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(new CameraPosition(latlng, 18, 0, 30)));
				break;
			case R.id.jd_ll:
				changeImage(4);
				dosearchquery("", "酒店", mLocalCityName);
				aMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(new CameraPosition(latlng, 18, 0, 30)));
				break;
			case R.id.yh_ll:
				changeImage(5);
				dosearchquery("", "银行", mLocalCityName);
				aMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(new CameraPosition(latlng, 18, 0, 30)));
				break;
			case R.id.jj_ll:
//				changeImage(6);
//				dosearchquery("", "时景", mLocalCityName);
//				aMap.animateCamera(CameraUpdateFactory
//						.newCameraPosition(new CameraPosition(latlng, 18, 0, 30)));
				break;
			case R.id.name:
				huanyuan();
				if (aMap != null) {
					aMap.clear();
					addMarkersToMap();
				}
				aMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(new CameraPosition(latlng, 18, 0, 30)));
				break;
			case R.id.fj:
				huanyuan();
				searchByLocal();
				aMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(new CameraPosition(latlng, 18, 0, 30)));
				break;
			case R.id.back:
				huanyuan();
				Intent intent = new Intent();
				intent.putExtra("bulidid", "ssss");
				setResult(9, intent);
				finish();
				break;
			case R.id.daohang:
				huanyuan();
				showpopwindow();
				break;
			default:
				break;
			}
		}
	}

	private void showpopwindow() {
		new ActionSheetDialog(BasicMapActivity.this)
				.builder()
				.setCancelable(false)
				.setCanceledOnTouchOutside(false)
				.setTitle("打开方式")
				.addSheetItem("高德地图", SheetItemColor.Blue,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								NaviPara naviPara = new NaviPara();
								naviPara.setTargetPoint(latlng);
								naviPara.setNaviStyle(AMapUtils.DRIVING_AVOID_CONGESTION);
								try {
									AMapUtils.openAMapNavi(naviPara,
											getApplicationContext());
								} catch (com.amap.api.maps.AMapException e) {
									Toast.makeText(BasicMapActivity.this,
											"没有安装高德地图，请安装高德地图!",
											Toast.LENGTH_SHORT).show();
								}
							}
						})
				.addSheetItem("百度地图", SheetItemColor.Blue,
						new OnSheetItemClickListener() {
							@Override
							public void onClick(int which) {
								if (isInstallByread("com.baidu.BaiduMap")) {
									dingwei();
								} else {
									Toast.makeText(context,
											"没有安装百度地图，请安装百度地图!",
											Toast.LENGTH_SHORT).show();
									return;
								}
							}
						}).show();

	}

	@SuppressLint("SdCardPath")
	private boolean isInstallByread(String packageName) {
		return new File("/data/data/" + packageName).exists();
	}

	private void dingwei() {
		// mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		// mLocationManagerProxy.requestLocationData(
		// LocationProviderProxy.AMapNetwork, -1, 15, this);
		// mLocationManagerProxy.setGpsEnable(false);
		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.requestLocationData("lbs", -1L, 15.0F, this);
		mLocationManagerProxy.setGpsEnable(false);
	}

	private void changeImage(int t) {
		huanyuan();
		LinearLayout layout = (LinearLayout) bottom.getChildAt(t);
		ImageView imageView = (ImageView) layout.getChildAt(0);
		TextView textView = (TextView) layout.getChildAt(1);
		textView.setTextColor(Color.parseColor(choosedcolor));
		switch (t) {
		case 0:
			imageView.setImageResource(R.drawable.dt_selected);
			break;
		case 1:
			imageView.setImageResource(R.drawable.gj_selected);
			break;
		case 2:
			imageView.setImageResource(R.drawable.sc_selected);
			break;
		case 3:
			imageView.setImageResource(R.drawable.cy_selected);
			break;
		case 4:
			imageView.setImageResource(R.drawable.jd_selected);
			break;
		case 5:
			imageView.setImageResource(R.drawable.yh_selected);
			break;
		case 6:
			imageView.setImageResource(R.drawable.jj_selected);
			break;
		default:
			break;
		}
	}

	private void huanyuan() {
		dt.setImageResource(R.drawable.dt_unselect);
		gj.setImageResource(R.drawable.gj_unselect);
		sc.setImageResource(R.drawable.sc_unselect);
		cy.setImageResource(R.drawable.cy_unselect);
		jd.setImageResource(R.drawable.jd_unselect);
		yh.setImageResource(R.drawable.yh_unselect);
		jj.setImageResource(R.drawable.jj_unselect);
		dt_tv.setTextColor(Color.parseColor(notchoosecolor));
		gj_tv.setTextColor(Color.parseColor(notchoosecolor));
		sc_tv.setTextColor(Color.parseColor(notchoosecolor));
		cy_tv.setTextColor(Color.parseColor(notchoosecolor));
		jd_tv.setTextColor(Color.parseColor(notchoosecolor));
		yh_tv.setTextColor(Color.parseColor(notchoosecolor));
		jj_tv.setTextColor(Color.parseColor(notchoosecolor));
	}

	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			aMap.setOnMarkerClickListener(BasicMapActivity.this);
			aMap.setInfoWindowAdapter(this);
			aMap.setOnInfoWindowClickListener(this);
			mCloudSearch = new CloudSearch(this.getApplicationContext());
			mCloudSearch.setOnCloudSearchListener(this);
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@SuppressWarnings("deprecation")
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null
				&& amapLocation.getAMapException().getErrorCode() == 0) {
			dwlat = String.valueOf(amapLocation.getLatitude());
			dwlon = String.valueOf(amapLocation.getLongitude());
			Intent intent = null;
			if (!TextUtils.isEmpty(dwlat) && !TextUtils.isEmpty(dwlon)) {
				try {
					intent = Intent
							.getIntent("intent://map/direction?origin=latlng:"
									+ dwlat
									+ ","
									+ dwlon
									+ "|name: 起点&destination="
									+ buliding_address
									+ "&mode=driving&src="
									+ getPackageName()
									+ "|"
									+ BasicMapActivity.this
											.getString(R.string.app_name)
									+ "#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
					startActivity(intent);

				} catch (URISyntaxException e) {
					e.printStackTrace();
					Toast.makeText(context, "导航失败，请重试！", Toast.LENGTH_SHORT)
							.show();
					return;
				}
			} else {
				Toast.makeText(context, "获取位置失败！", Toast.LENGTH_SHORT).show();
				return;
			}
		}
	}

}
