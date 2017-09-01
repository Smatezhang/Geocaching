package com.zhuoxin.zhang.geocaching.treasure.detail;

import com.zhuoxin.zhang.geocaching.net.NetClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/9/1.
 */

public class TreasureDetailPresenter {
    public TreasureDetailPresenter(TreasureDetailView treasureDetailView) {
        mTreasureDetailView = treasureDetailView;
    }

    private TreasureDetailView mTreasureDetailView;

    public void getTreasureDetail(TreasureDetail treasureDetail){
        NetClient.getInstance().getNetApi().getTreasureDetail(treasureDetail).enqueue(new Callback<List<TreasureDetailResult>>() {
            @Override
            public void onResponse(Call<List<TreasureDetailResult>> call, Response<List<TreasureDetailResult>> response) {
                if (response.isSuccessful()){
                    List<TreasureDetailResult> mTreasureDetailResultList = response.body();
                    if (mTreasureDetailResultList ==null){
                        mTreasureDetailView.showMessage("未知错误！！");
                        return;
                    }
                    mTreasureDetailView.showTreasureDetail(mTreasureDetailResultList);

                }
            }

            @Override
            public void onFailure(Call<List<TreasureDetailResult>> call, Throwable t) {
                mTreasureDetailView.showMessage("请求失败！！");
            }
        });
    }
}
