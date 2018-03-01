package net.fofi.app.base;

import android.app.Application;
import android.content.Context;

/**
 * Created by ZYY on 2018/3/1.
 */

public class BaseApplication extends Application {

    static Context _context;

    @Override
    public void onCreate() {
        super.onCreate();
        _context = getApplicationContext();
        //LeakCanary.install(this);
    }

    public static synchronized BaseApplication context() {
        return (BaseApplication) _context;
    }
}
