package com.zhuoxin.zhang.geocaching.user.register;

/**
 * Created by Administrator on 2017/8/29.
 */

public interface RegisterView {
    void showMessage(String msg);
    void showProgress();
    void hideProgress();
    void clearEditText();
    void navigateTOHome();
}
