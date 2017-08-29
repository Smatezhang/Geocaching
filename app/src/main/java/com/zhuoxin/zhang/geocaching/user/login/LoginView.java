package com.zhuoxin.zhang.geocaching.user.login;

/**
 * Created by Administrator on 2017/8/29.
 */

public interface LoginView {
    void showMessage(String msg);
    void showProgress();
    void hideProgress();
    void NavigateToHome();
    void clearEditText();
}
