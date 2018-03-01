package net.fofi.app.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import net.fofi.app.base.BaseApplication;

/**
 * 工具类 TDevice
 * Created by ZYY on 2018/3/1.
 */

public class TDevice {

    public static boolean hasInternet() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.context()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info != null && info.isAvailable() && info.isConnected();
    }
}
