package net.fofi.app.api;

import net.fofi.app.api.remote.FOFIApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ZYY on 2018/2/27.
 */

public class RetrofitHelper {
    // 服务器 Ip 地址
//    private final static String serverIp= "http://169.254.42.232:9999/";
    private final static String serverIp= "http://192.168.1.103:9999/";

    private Retrofit retrofit;
    private FOFIApi fofiApi;
    public FOFIApi getFofiApi( ){
        //加上之前 @POST（）里的地址 组成完整的接口url
        retrofit=new Retrofit.Builder()
                .baseUrl(serverIp)
                .addConverterFactory(GsonConverterFactory.create())
                .build();//增加返回值为实体类的支持
        //创建service
        return retrofit.create(FOFIApi.class);
    }
}
