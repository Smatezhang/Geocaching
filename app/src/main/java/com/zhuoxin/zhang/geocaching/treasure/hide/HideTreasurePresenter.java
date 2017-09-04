package com.zhuoxin.zhang.geocaching.treasure.hide;

import com.zhuoxin.zhang.geocaching.net.NetClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2017/9/4.
 */

public class HideTreasurePresenter {
    private HideTreasureView mHideTreasureView;

    public HideTreasurePresenter(HideTreasureView hideTreasureView) {
        mHideTreasureView = hideTreasureView;
    }

    public void hideTreasure(HideTreasure hideTreasure){
        //显示进度条
        mHideTreasureView.showProgress();
        NetClient.getInstance().getNetApi().hideTreasure(hideTreasure).enqueue(new Callback<HideTreasureResult>() {
            @Override
            public void onResponse(Call<HideTreasureResult> call, Response<HideTreasureResult> response) {
                //隐藏进度条
                mHideTreasureView.hideProgress();
                mHideTreasureView.clearET();
                if (response.isSuccessful()){
                    HideTreasureResult mHideTreasureResult = response.body();
                    if (mHideTreasureResult== null){
                        mHideTreasureView.showMessage("未知错误");
                        return;
                    }
                    if (mHideTreasureResult.getErrcode()!=1){
                        mHideTreasureView.showMessage(mHideTreasureResult.getErrmsg());
                        return;
                    }
                    mHideTreasureView.backHome();

                }
            }

            @Override
            public void onFailure(Call<HideTreasureResult> call, Throwable t) {
                //隐藏进度条
                mHideTreasureView.hideProgress();

                mHideTreasureView.showMessage("上传失败！！");
            }
        });
    }

}
