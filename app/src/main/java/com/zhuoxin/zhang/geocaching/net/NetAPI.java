package com.zhuoxin.zhang.geocaching.net;

import com.zhuoxin.zhang.geocaching.entity.RegisterResult;
import com.zhuoxin.zhang.geocaching.entity.User;
import com.zhuoxin.zhang.geocaching.entity.LoginResult;
import com.zhuoxin.zhang.geocaching.treasure.Area;
import com.zhuoxin.zhang.geocaching.treasure.Treasure;
import com.zhuoxin.zhang.geocaching.treasure.detail.TreasureDetail;
import com.zhuoxin.zhang.geocaching.treasure.detail.TreasureDetailResult;
import com.zhuoxin.zhang.geocaching.treasure.hide.HideTreasure;
import com.zhuoxin.zhang.geocaching.treasure.hide.HideTreasureResult;
import com.zhuoxin.zhang.geocaching.user.account.Update;
import com.zhuoxin.zhang.geocaching.user.account.UpdateResult;
import com.zhuoxin.zhang.geocaching.user.account.UploadResult;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Administrator on 2017/8/29.
 */

public interface NetAPI {
    /**
     * 登录
     *
     * @param mUser
     * @return
     */
    @POST("/Handler/UserHandler.ashx?action=login")
    Call<LoginResult> login(@Body User mUser);

    /**
     * 注册
     *
     * @param mUser
     * @return
     */
    @POST("/Handler/UserHandler.ashx?action=register")
    Call<RegisterResult> register(@Body User mUser);

    /**
     * 根据区域 获取宝藏
     *
     * @param area
     * @return
     */
    @POST("/Handler/TreasureHandler.ashx?action=show")
    Call<List<Treasure>> getTreasure(@Body Area area);

    /**
     * 获取宝藏详情
     *
     * @param treasureDetail
     * @return
     */
    @POST("/Handler/TreasureHandler.ashx?action=tdetails")
    Call<List<TreasureDetailResult>> getTreasureDetail(@Body TreasureDetail treasureDetail);

    /**
     * 上传宝藏
     * @param hideTreasure
     * @return
     */

    @POST("/Handler/TreasureHandler.ashx?action=hide")
    Call<HideTreasureResult> hideTreasure(@Body HideTreasure hideTreasure);

    /**
     * 更新头像
     * @param update
     * @return
     */
    @POST("/Handler/UserHandler.ashx?action=update")
    Call<UpdateResult> update(@Body Update update);

    /**
     * 上传头像
     * @param part
     * @return
     */
    @Multipart
    @POST("/Handler/UserLoadPicHandler1.ashx")
    Call<UploadResult> upLoad(@Part MultipartBody.Part part);

}
