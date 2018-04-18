package com.mrlou.image;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.WeakHashMap;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

/**
 * 本地图片加载器 如果ImageView需要在图片从磁盘载入并应用到imageView上之前，取消设置图片的动作，设置tag为false(例如，
 * 添加图片到imageView上，还没显示出来，马上删除)
 */
public class ImageLoader {

	private static final String TAG = "ImageLoader";

	private ImageCache cache;

	private HashSet<String> cacheKeys = new HashSet<String>();
	
	private ImageDownloader downloader;
	
	// 保存filepath和ImageView的关系，因为ImageView会复用，所以只有这个关系才是正确的关系
	// 一个imageView只能对应一个filepath，一个filepath对应一个物理文件
	private WeakHashMap<ImageView, String> imageView2FileMap = new WeakHashMap<ImageView, String>();
	// 一个filepath可能对应多个imageView,因为有可能会有多个imageView显示同一张图片
	private HashMap<String, HashSet<ImageViewReference>> file2ImageViewMap = new HashMap<String, HashSet<ImageViewReference>>();
	// 正在读的或者已经在列队里的filepath,读完删除
	private HashSet<String> fileInLoadSet = new HashSet<String>();

	public ImageLoader(ImageDownloader downloader) {
		if(downloader == null){
			throw new RuntimeException("ImageDownloader can not be null");
		}
		this.cache = ImageCache.getInstance();
		this.downloader = downloader;
	}

	/**
	 * 给imageView设置图片
	 * 
	 * @param filePath
	 *            图片路径
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 * @param imageView
	 * @return 缓存中有，直接设置，并返回true,没有异步读取，读完再设置，返回false
	 */
	public boolean loadImage(String filePath, int width, int height, ImageView imageView) {
		String filePathKey = getKeyForFilePath(filePath, width, height);
		Bitmap bmp = cache.get(filePathKey);
		if (bmp == null) {
			ImageViewReference imageViewRef = new ImageViewReference(imageView);
			// 更新imageView和filepath的最新的关系
			imageView2FileMap.put(imageView, filePathKey);
			HashSet<ImageViewReference> imageViewSet = file2ImageViewMap.get(filePathKey);
			if (imageViewSet == null) {
				imageViewSet = new HashSet<ImageViewReference>();
				file2ImageViewMap.put(filePathKey, imageViewSet);
			}
			imageViewSet.add(imageViewRef);
			// 不会重复下载
			if (fileInLoadSet.contains(filePathKey)) {
				return false;
			} else {
				fileInLoadSet.add(filePathKey);
			}
			Holder holder = new Holder();
			holder.width = width;
			holder.height = height;
			holder.filePath = filePath;
			holder.filePathKey = filePathKey;
			holder.imageViewRef = imageViewRef;
			new ImageLoadTask().execute(holder);
			return false;
		} else {
			imageView.setImageBitmap(bmp);
			return true;
		}

	}

	private class ImageLoadTask extends AsyncTask<Holder, Void, Holder> {

		@Override
		protected Holder doInBackground(Holder... params) {
			Holder holder = params[0];
			int width = holder.width;
			int height = holder.height;
			String filePath = holder.filePath;
			String filePathKey = holder.filePathKey;
			// 找到key对应的所有imageView,如果imageView的数量是0说明不用下载了
			int count = getCountOfImageViewForKey(filePathKey);
			if (count <= 0) {
				return null;
			}
			try {
				Random rnd = new Random();
				Thread.sleep((int) (1000 * rnd.nextDouble()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 开始读取，放入cache
			if(downloader != null){
				//Bitmap bmp = ImageUtil.compressBitmap(filePath, width, height);
				Bitmap bmp = downloader.download(filePath, width, height);
				if(bmp != null){
					cache.put(filePathKey, bmp);
					cacheKeys.add(filePath);
					holder.imageData = bmp;
				}
			}
			return holder;
		}

		@Override
		protected void onPostExecute(Holder holder) {
			super.onPostExecute(holder);
			// 读完图片，把key移除
			String filePathKey = holder.filePathKey;
			fileInLoadSet.remove(filePathKey);
			
			Bitmap data = holder.imageData;
			if(data == null){
				return;
			}
			
			ArrayList<ImageView> imageViewArrayList = getImageViewListForKey(filePathKey);
			if (imageViewArrayList.size() == 0) {
				return;
			}
			// 遍历imageview列表,通过imageView2FileMap查找该imageView对应的最新的latestFilePathKey是不是刚刚下载好的这个filePathKey
			// 只有一直才需要显示，如果不一致，说明该imageView已经被复用，对应到了新的key
			for (ImageView imageView : imageViewArrayList) {
				String latestFilePathKey = imageView2FileMap.get(imageView);
				if (latestFilePathKey != null && latestFilePathKey.equals(filePathKey)) {
					if (imageView != null) {
						imageView.setImageBitmap(data);
					//	Log.e(TAG, "设置图片 ");
						/*
						 * boolean isSet; 
						 * try{ 
						 * 		isSet=(Boolean)
						 * 		imageView.getTag(); 
						 * }catch(Exception e) {
						 * 		isSet=true; 
						 * } 
						 * if(isSet) {
						 * 		imageView.setImageBitmap(result); 
						 * 		Log.e(TAG,"设置图片 "); 
						 * }
						 */
					}
					// 即使不remove，也会自动回收
					imageView2FileMap.remove(imageView);
				} else {
					
				}
			}
			file2ImageViewMap.remove(filePathKey);
		}
	}

	class Holder {
		int width,height;
		String filePath, filePathKey;
		Bitmap imageData;
		ImageViewReference imageViewRef;
	}

	private String getKeyForFilePath(String imagePath, int width, int height) {
		return imagePath + "_" + width + "_" + height;
	}

	/**
	 * 销毁ImageLoader
	 * 
	 * */
	public void clear(){
		imageView2FileMap.clear();
		file2ImageViewMap.clear();
		fileInLoadSet.clear();
		for(String cacheKey : cacheKeys){
			cache.remove(cacheKey);
		}
		cacheKeys.clear();
		imageView2FileMap = null;
		file2ImageViewMap = null;
		fileInLoadSet = null;
		cacheKeys = null;
		downloader = null;
		cache = null;
	}
	
	/**
	 * 销毁ImageLoader， 应用退出的时候调用
	 * 
	 * */
	public void destory() {
		clear();
		ImageCache.destroy();
	}
	
	
	public interface ImageDownloader{
		public Bitmap download(String path,int width, int height);
	}

	/**
	 * 通过file2ImageViewMap获取filePath对应的所有imageView列表 同时删除被回收的imageView,
	 * 
	 * @param filePathKey
	 * @return
	 */
	private ArrayList<ImageView> getImageViewListForKey(String filePathKey) {
		ArrayList<ImageView> imageViewArrayList = new ArrayList<ImageView>();
		HashSet<ImageViewReference> imageViewReferences = file2ImageViewMap.get(filePathKey);
		if(imageViewReferences == null){
			return null;
		}
		Iterator<ImageViewReference> it = imageViewReferences.iterator();
		while (it.hasNext()) {
			ImageViewReference reference = it.next();
			if (reference.get() != null) {
				imageViewArrayList.add(reference.get());
			} else {
				it.remove();
			}
		}
		return imageViewArrayList;
	}

	/**
	 * 获取指定的filePath对应的有效imageView的数量
	 * 
	 * @param filePathKey
	 * @return
	 */
	private int getCountOfImageViewForKey(String filePathKey) {
		ArrayList<ImageView> imageViewArrayList = getImageViewListForKey(filePathKey);
		if(imageViewArrayList == null){
			return 0;
		}else{
			return imageViewArrayList.size();
		}
	}
	
	private static class ImageCache extends LruCache<String, Bitmap> {
		private static final int cacheSize = 10 * 1024 * 1024;
		private static ImageCache instance = new ImageCache(cacheSize);
		public static ImageCache getInstance(){
			return instance;
		}
		private ImageCache(int maxSize) {
			super(maxSize);
		}
		@Override
		protected int sizeOf(String key, Bitmap value) {
			return value.getByteCount();
		}
		public static void destroy(){
			if(instance == null){
				return;
			}
			instance.evictAll();
			instance = null;
		}
	}
	
	private static class ImageViewReference extends WeakReference<ImageView> {
		public ImageViewReference(ImageView r) {
			super(r);
		}
		@Override
		public boolean equals(Object o) {
			ImageViewReference other=(ImageViewReference)o;
			return this.get()==other.get();
		}
		@Override
		public int hashCode() {
			ImageView imageView = this.get();
			if(imageView != null){
				return imageView.hashCode();
			}
			return 0;
		}
	}
	
}
