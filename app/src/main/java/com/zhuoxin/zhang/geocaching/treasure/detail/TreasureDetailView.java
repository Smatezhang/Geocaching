package com.zhuoxin.zhang.geocaching.treasure.detail;

import java.util.List;

/**
 * Created by Administrator on 2017/9/1.
 */

public interface TreasureDetailView {
    void showTreasureDetail(List<TreasureDetailResult> treasureDetailResultList);
    void showMessage(String msg);
}
