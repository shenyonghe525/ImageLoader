package com.syh.imageloader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

@SuppressLint("HandlerLeak")
public class ImageLoader {

	private ImageView mImageView;

	private String mUrl;

	private LruCache<String, Bitmap> mLruCache;

	private ListView mListView;

	private Set<ImageLoadAsyncTask> mTasks;

	public ImageLoader(ListView listView) {
		mListView = listView;
		mTasks = new HashSet<ImageLoader.ImageLoadAsyncTask>();
		// 获取最大可用内存
		long maxMemory = Runtime.getRuntime().maxMemory();
		int cacheSize = (int) (maxMemory / 4);
		mLruCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				// 在每次存入缓存的时候调用
				return value.getByteCount();
			}
		};
	}

	/**
	 * 将图片增加到缓存 Tile:addBitmapToCache
	 * 
	 * @param url
	 * @param bitmap void
	 */
	public void addBitmapToCache(String url, Bitmap bitmap) {
		if (getBitmapFromCache(url) == null) {
			mLruCache.put(url, bitmap);
		}
	}

	/**
	 * 从缓存中获取图片 Tile:getBitmapFromCache
	 * 
	 * @param url
	 * @return Bitmap
	 */
	public Bitmap getBitmapFromCache(String url) {
		return mLruCache.get(url);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// 避免缓存的图片乱位
			if (mImageView.getTag().equals(mUrl))
				mImageView.setImageBitmap((Bitmap) msg.obj);
		};
	};

	/**
	 * 
	 * Tile:showImgByThread
	 * 
	 * @param imageView
	 * @param imgUrl void
	 */
	public void showImgByThread(ImageView imageView, final String imgUrl) {

		mImageView = (ImageView) mListView.findViewWithTag(imgUrl);
		mUrl = imgUrl;

		new Thread() {
			public void run() {
				Bitmap bitmap = getBitmapFromUrl(imgUrl);
				Message message = Message.obtain();
				message.obj = bitmap;
				mHandler.sendMessage(message);
			}
		}.start();
	}

	/**
	 * 
	 * Tile:showImgByAsyncTask
	 * 
	 * @param imageView
	 * @param imgUrl void
	 */
	public void showImgByAsyncTask(ImageView imageView, String imgUrl) {

		// 从缓存中取出对应的图片
		Bitmap bitmap = getBitmapFromCache(imgUrl);
		if (bitmap == null) {
			imageView.setImageResource(R.drawable.ic_launcher);
		} else {
			imageView.setImageBitmap(bitmap);
		}

	}

	private class ImageLoadAsyncTask extends AsyncTask<String, Void, Bitmap> {

		private String url;

		public ImageLoadAsyncTask(String urlString) {
			url = urlString;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			// 从网络获取图片,将不在缓存的图片加入缓存
			Bitmap bitmap = getBitmapFromUrl(params[0]);
			if (bitmap != null) {
				addBitmapToCache(params[0], bitmap);
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			ImageView imageView = (ImageView) mListView.findViewWithTag(url);
			if (imageView != null && result != null) {
				imageView.setImageBitmap(result);
			}
			mTasks.remove(this);
		}
	}

	/**
	 * 通过图片的url获取图片文件 Tile:getBitmapFromUrl
	 * 
	 * @param urlString
	 * @return Bitmap
	 */
	private Bitmap getBitmapFromUrl(String urlString) {

		Bitmap bitmap;
		InputStream is = null;

		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			is = new BufferedInputStream(connection.getInputStream());
			bitmap = BitmapFactory.decodeStream(is);
			connection.disconnect();
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 加载指定位置的图片 Tile:loadImages
	 * 
	 * @param start
	 * @param end void
	 */
	public void loadImages(int start, int end) {
		for (int i = start; i < end; i++) {
			String urlString = MyAdapter.Urls[i];
			// 从缓存中取出对应的图片
			Bitmap bitmap = getBitmapFromCache(urlString);
			if (bitmap == null) {
				ImageLoadAsyncTask asyncTask = new ImageLoadAsyncTask(urlString);
				asyncTask.execute(urlString);
				mTasks.add(asyncTask);
			} else {
				ImageView imageView = (ImageView) mListView
						.findViewWithTag(urlString);
				imageView.setImageBitmap(bitmap);
			}
		}
	}

	/**
	 * 滚动时停止所有的异步任务 Tile:cancelAllTasks void
	 */
	public void cancelAllTasks() {
		if (mTasks != null) {
			for (ImageLoadAsyncTask task : mTasks) {
				task.cancel(false);
			}
		}
	}
}
