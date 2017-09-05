package com.zhuoxin.zhang.geocaching.user.account;

/**
 * Created by Administrator on 2017/9/5.
 */

public interface AccountView {
    void showMessage(String msg);
    void showProgress();
    void hideProgress();
    void displayPhoto();
}
