package com.zhuoxin.zhang.geocaching.map;

import com.zhuoxin.zhang.geocaching.net.NetClient;
import com.zhuoxin.zhang.geocaching.treasure.Area;
import com.zhuoxin.zhang.geocaching.treasure.Treasure;
import com.zhuoxin.zhang.geocaching.treasure.TreasureRepo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/8/31.
 */

public class MapFragmentPresenter {
private MapFragmentView mMapFragmentView;

    public MapFragmentPresenter(MapFragmentView mapFragmentView) {
        mMapFragmentView = mapFragmentView;
    }

    public void getTreasures(final Area area){

        if (TreasureRepo.getInstance().isCached(area)){
            //展示宝藏 如果已经缓存过了
            mMapFragmentView.showTreasure(TreasureRepo.getInstance().getTreasure());
            return;
        }
        NetClient.getInstance().getNetApi().getTreasure(area).enqueue(new Callback<List<Treasure>>() {
            @Override
            public void onResponse(Call<List<Treasure>> call, Response<List<Treasure>> response) {
                if (response.isSuccessful()){
                    List<Treasure> mTreasureList = response.body();
                    if (mTreasureList== null){
                        mMapFragmentView.showMessage("未知错误!!");
                        return;
                    }
                    //缓存区域
                    TreasureRepo.getInstance().cache(area);
                    //添加宝藏
                    TreasureRepo.getInstance().addTreasure(mTreasureList);
                    mMapFragmentView.showTreasure(mTreasureList);

                }
            }

            @Override
            public void onFailure(Call<List<Treasure>> call, Throwable t) {
                mMapFragmentView.showMessage("网络请求失败!!");
            }
        });

    }

}
