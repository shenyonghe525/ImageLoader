package com.syh.imageloader;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyAdapter extends CommonAdapter<NewsDto> implements
		OnScrollListener {

	private ImageLoader mImageLoader;

	private int mStart, mEnd;

	public static String[] Urls;

	private boolean mFirst;

	public MyAdapter(Context context, List<NewsDto> datas, int id,
			ListView listView) {
		super(context, datas, id);
		mImageLoader = new ImageLoader(listView);
		Urls = new String[datas.size()];
		for (int i = 0; i < datas.size(); i++) {
			Urls[i] = datas.get(i).getImageUrl();
		}
		// 注册滚动停止加载接口
		listView.setOnScrollListener(this);
		mFirst = true;
	}

	public void convert(View convertView, ViewGroup parent,
			final NewsDto newsDto) {

		ImageView imageView = ViewHolder.get(convertView, R.id.iv_newicon);
		TextView title = ViewHolder.get(convertView, R.id.tv_title);
		TextView content = ViewHolder.get(convertView, R.id.tv_content);
		title.setText(newsDto.getTitle());
		content.setText(newsDto.getContent());
		imageView.setImageResource(R.drawable.ic_launcher);
		imageView.setTag(newsDto.getImageUrl());
		// mImageLoader.showImgByAsyncTask(imageView, newsDto.getImageUrl());
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (scrollState == SCROLL_STATE_IDLE) {
			// 加载可见项
			mImageLoader.loadImages(mStart, mEnd);
		} else {
			// 停止下载任务
			mImageLoader.cancelAllTasks();
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		mStart = firstVisibleItem;
		mEnd = firstVisibleItem + visibleItemCount;
		// 第一次显示的时候调用
		if (mFirst && visibleItemCount > 0) {
			mImageLoader.loadImages(mStart, mEnd);
			mFirst = false;
		}
	}

}
