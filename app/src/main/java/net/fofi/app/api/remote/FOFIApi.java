package net.fofi.app.api.remote;


import net.fofi.app.improve.account.User;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * FOFIApi 网络API 类
 * Created by ZYY on 2018/2/27.
 */

public interface FOFIApi {

    /**
     * 用户注册接口，返回一个 User
     * @param telephone 手机号
     * @param password 密码
     * @param username 用户名
     * @param gender 性别 男-male 女-female
     * */
    @POST("action/register")
    Call<User> register(@Query("telephone") String telephone,
                        @Query("password") String password,
                        @Query("username") String username,
                        @Query("gender") String gender);

    /**
     * 用户登录接口，返回一个 User
     * @param telephone 手机号
     * @param password 密码
     * */
    @POST("action/login")
    Call<User> login(@Query("telephone") String telephone,
                     @Query("password") String password);

    /**
     * 判断手机号是否已经注册，已注册返回手机号，未注册则返回 null 值
     * @param telephone 手机号
     * */
    @POST("action/isregister")
    Call<String> isRegister(@Query("telephone") String telephone);

    /**
     * 重置密码，返回一个 User
     * @param telephone 手机号
     * @param password 密码
     * */
    @POST("action/resetpwd")
    Call<User> resetPwd(@Query("telephone") String telephone,
                        @Query("password") String password);
}
