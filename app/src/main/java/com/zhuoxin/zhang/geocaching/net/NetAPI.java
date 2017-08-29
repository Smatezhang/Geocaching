package com.zhuoxin.zhang.geocaching.net;

import com.zhuoxin.zhang.geocaching.entity.RegisterResult;
import com.zhuoxin.zhang.geocaching.entity.User;
import com.zhuoxin.zhang.geocaching.entity.LoginResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2017/8/29.
 */

public interface NetAPI {
    /**
     * 登录
     * @param mUser
     * @return
     */
    @POST("/Handler/UserHandler.ashx?action=login")
    Call<LoginResult> login(@Body User mUser);

    /**
     * 注册
     * @param mUser
     * @return
     */
    @POST("/Handler/UserHandler.ashx?action=register")
    Call<RegisterResult> register(@Body User mUser);



}
