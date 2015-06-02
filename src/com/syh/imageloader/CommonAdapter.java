package com.syh.imageloader;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonAdapter<T> extends BaseAdapter {

	protected Context mContext;
	//数据源
	protected List<T> mDatas;
	protected LayoutInflater mLayoutInflater;
	// listview item的布局文件
	protected int mItemId;

	public CommonAdapter(Context context, List<T> datas, int itemId) {
		this.mContext = context;
		this.mDatas = datas;
		this.mItemId = itemId;
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 当列表里的每一项显示到界面时，都会调用这个方法一次
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(mItemId, parent, false);
		}
		convert(convertView, parent, getItem(position));
		return convertView;
	}

	/**
	 * 查询item对应子项并进行处理。 Tile:convert
	 * 
	 * @param convertView 当前item
	 * @param parent
	 * @param t 对应数据类型 void
	 */
	public abstract void convert(View convertView, ViewGroup parent, T t);

}
