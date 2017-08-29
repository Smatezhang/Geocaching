package com.zhuoxin.zhang.geocaching.user.register;

import com.zhuoxin.zhang.geocaching.entity.RegisterResult;
import com.zhuoxin.zhang.geocaching.entity.User;
import com.zhuoxin.zhang.geocaching.entity.UserPrefs;
import com.zhuoxin.zhang.geocaching.net.NetClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/29.
 */

public class RegisterPresenter {

    private RegisterView mRegisterView;

    public RegisterPresenter(RegisterView mRegisterView) {
        this.mRegisterView = mRegisterView;
    }

    public void register(User mUser){
        //显示进度条
        mRegisterView.showProgress();
        NetClient.getInstance().getNetApi().register(mUser).enqueue(new Callback<RegisterResult>() {
            @Override
            public void onResponse(Call<RegisterResult> call, Response<RegisterResult> response) {
                //隐藏进度条
                mRegisterView.hideProgress();
                if (response.isSuccessful()){
                    RegisterResult mRegisterResult = response.body();
                    if(mRegisterResult.getErrcode() == 1){
                        //注册成功！
                        mRegisterView.showMessage(mRegisterResult.getErrmsg());

                        //缓存数据
                        UserPrefs.getInstance().setTokenid(mRegisterResult.getTokenid());
                       //页面跳转
                        mRegisterView.navigateTOHome();
                       return;
                    }else {
                        //吐丝
                        mRegisterView.showMessage(mRegisterResult.getErrmsg());
                        return;
                    }
                }else {
                    //吐丝
                    mRegisterView.showMessage("未知错误！！");
                }

            }

            @Override
            public void onFailure(Call<RegisterResult> call, Throwable t) {
                //隐藏进度条
                mRegisterView.hideProgress();
                //吐丝
                mRegisterView.showMessage("注册失败！！");
                //清理
                mRegisterView.clearEditText();
            }
        });
    }
}
