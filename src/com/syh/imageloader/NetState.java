package com.syh.imageloader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetState {

	public static boolean isNetAvailable(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				Toast.makeText(context, "网络可用", Toast.LENGTH_SHORT).show();
				return mNetworkInfo.isAvailable();
			}
		}
		Toast.makeText(context, "网络不可用", Toast.LENGTH_SHORT).show();
		return false;
	}
}
