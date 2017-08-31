package com.zhuoxin.zhang.geocaching.map;

import com.zhuoxin.zhang.geocaching.treasure.Treasure;

import java.util.List;

/**
 * Created by Administrator on 2017/8/31.
 */

public interface MapFragmentView {
    void showTreasure(List<Treasure> treasureList);
    void  showMessage(String msg);
}
