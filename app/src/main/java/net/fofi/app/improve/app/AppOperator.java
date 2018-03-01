package net.fofi.app.improve.app;

import android.os.Handler;
import android.os.Looper;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ZYY on 2018/2/26.
 */

public final class AppOperator {
    private static Handler mHandler;
    private static ExecutorService EXECUTORS_INSTANCE;
    private static Gson GSON_INSTANCE;

    public static Executor getExecutor() {
        if (EXECUTORS_INSTANCE == null) {
            synchronized (AppOperator.class) {
                if (EXECUTORS_INSTANCE == null) {
                    EXECUTORS_INSTANCE = Executors.newFixedThreadPool(6);
                }
            }
        }
        return EXECUTORS_INSTANCE;
    }


    public static void runOnMainThread(Runnable runnable) {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        mHandler.post(runnable);
    }

    public static void runOnThread(Runnable runnable) {
        getExecutor().execute(runnable);
    }

//    public static GlideUrl getGlideUrlByUser(String url) {
//        if (AccountHelper.isLogin()) {
//            return new GlideUrl(url,
//                    new LazyHeaders
//                            .Builder()
//                            .addHeader("Cookie", AccountHelper.getCookie())
//                            .build());
//        } else {
//            return new GlideUrl(url);
//        }
//    }
//
//    public static Gson createGson() {
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        //gsonBuilder.setExclusionStrategies(new SpecificClassExclusionStrategy(null, Model.class));
//        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        JsonDeserializer deserializer = new IntegerJsonDeserializer();
//        gsonBuilder.registerTypeAdapter(int.class, deserializer);
//        gsonBuilder.registerTypeAdapter(Integer.class, deserializer);
//
//        deserializer = new FloatJsonDeserializer();
//        gsonBuilder.registerTypeAdapter(float.class, deserializer);
//        gsonBuilder.registerTypeAdapter(Float.class, deserializer);
//
//        deserializer = new DoubleJsonDeserializer();
//        gsonBuilder.registerTypeAdapter(double.class, deserializer);
//        gsonBuilder.registerTypeAdapter(Double.class, deserializer);
//
//        deserializer = new StringJsonDeserializer();
//        gsonBuilder.registerTypeAdapter(String.class, deserializer);
//
//        gsonBuilder.registerTypeAdapter(Tweet.Image.class, new ImageJsonDeserializer());
//
//        return gsonBuilder.create();
//    }

//    public synchronized static Gson getGson() {
//        if (GSON_INSTANCE == null)
//            GSON_INSTANCE = createGson();
//        return GSON_INSTANCE;
//    }
}
