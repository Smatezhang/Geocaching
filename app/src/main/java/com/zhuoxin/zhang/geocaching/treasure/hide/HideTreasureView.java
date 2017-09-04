package com.zhuoxin.zhang.geocaching.treasure.hide;

/**
 * Created by Administrator on 2017/9/4.
 */

public interface HideTreasureView {
    void showMessage(String msg);
    void showProgress();
    void hideProgress();
    void backHome();
    void clearET();
}
