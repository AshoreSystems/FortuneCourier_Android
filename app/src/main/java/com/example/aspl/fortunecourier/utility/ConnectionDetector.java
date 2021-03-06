package com.example.aspl.fortunecourier.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

	private Context _context;

	public ConnectionDetector(Context context) {
		this._context = context;
	}

	public boolean isConnectingToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo info = connectivity.getActiveNetworkInfo();

			if (info != null) { // connected to the internet
				if (info.getType() == ConnectivityManager.TYPE_WIFI ){
					return true;
				}else if(info.getType() == ConnectivityManager.TYPE_MOBILE){
					return true;
				}
			}
		}
		return false;
	}

}
