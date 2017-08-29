package com.zhuoxin.zhang.geocaching.user.login;

import com.zhuoxin.zhang.geocaching.entity.User;
import com.zhuoxin.zhang.geocaching.entity.UserPrefs;
import com.zhuoxin.zhang.geocaching.entity.LoginResult;
import com.zhuoxin.zhang.geocaching.net.NetClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/29.
 */

public class LoginPresenter {

    private LoginView mLoginView;

    public LoginPresenter(LoginView mLoginView) {
        this.mLoginView = mLoginView;
    }

    public void login(final User mUser){
        //显示进度条
        mLoginView.showProgress();
        NetClient.getInstance().getNetApi().login(mUser).enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
               //隐藏进度条
                mLoginView.hideProgress();
                if (response.isSuccessful()){
                    LoginResult mUserResult = response.body();
                    if (mUserResult != null){
                        if (mUserResult.getErrcode() == 1){
                            //弹吐司
                            mLoginView.showMessage(mUserResult.getErrmsg());
                            UserPrefs.getInstance().setPhoto(mUserResult.getHeadpic());
                            UserPrefs.getInstance().setTokenid(mUserResult.getTokenid());

                            mLoginView.NavigateToHome();
                            return;
                        }else {
                            //弹吐司
                            mLoginView.showMessage(mUserResult.getErrmsg());
                            return;
                        }

                    }
                    //弹吐司
                    mLoginView.showMessage("未知错误！！"+"mUserResult = null");
                    return;
                }else {
                    //弹吐司
                    mLoginView.showMessage("未知错误！！");
                }

            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                //弹吐司
                //隐藏进度条
                mLoginView.hideProgress();
                mLoginView.showMessage("连接失败！！");
                mLoginView.clearEditText();

            }
        });
    }
}
