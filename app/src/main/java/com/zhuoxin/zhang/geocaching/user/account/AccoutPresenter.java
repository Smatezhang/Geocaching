package com.zhuoxin.zhang.geocaching.user.account;

import com.zhuoxin.zhang.geocaching.entity.UserPrefs;
import com.zhuoxin.zhang.geocaching.net.NetClient;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

/**
 * Created by Administrator on 2017/9/5.
 */

public class AccoutPresenter {

    private AccountView mAccountView;

    public AccoutPresenter(AccountView accountView) {
        mAccountView = accountView;
    }

    public void upload(File file){
        //显示进度条
        mAccountView.showProgress();
        RequestBody mRequestBody = RequestBody.create(null, file);
        MultipartBody.Part mPart = MultipartBody.Part.createFormData("头像上传", "image.img", mRequestBody);
        NetClient.getInstance().getNetApi().upLoad(mPart).enqueue(new Callback<UploadResult>() {
            @Override
            public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {
                //隐藏进度条
                if (response.isSuccessful()){
                    UploadResult mUploadResult = response.body();
                    if (mUploadResult== null){
                        // msg
                        mAccountView.showMessage("未知错误");
                        return;
                    }

                    if (mUploadResult.getCount()!=1){
                        //msg
                        mAccountView.showMessage(mUploadResult.getMsg());
                        return;
                    }

                    //缓存头像
                    String mUrl = mUploadResult.getUrl();
                    String mPhotoUrl = mUrl.substring(mUrl.lastIndexOf("/") + 1);
                    UserPrefs.getInstance().setPhoto(mUrl);
                    //更新头像

                    update(mPhotoUrl);

                }
            }

            @Override
            public void onFailure(Call<UploadResult> call, Throwable t) {
                //隐藏进度条
                mAccountView.hideProgress();
                mAccountView.showMessage("上传失败");
            }
        });
    }

    private void update(final String photoUrl) {

        Update mUpdate = new Update(UserPrefs.getInstance().getTokenid(), photoUrl);
        NetClient.getInstance().getNetApi().update(mUpdate).enqueue(new Callback<UpdateResult>() {
            @Override
            public void onResponse(Call<UpdateResult> call, Response<UpdateResult> response) {
                    if (response.isSuccessful()){
                        UpdateResult mUpdateResult = response.body();
                        if (mUpdateResult== null){
                            //msg
                            mAccountView.showMessage("未知错误1");
                            return;
                        }
                        if (mUpdateResult.getCode()!=1){
                            //msg
                            mAccountView.showMessage(mUpdateResult.getMsg());

                            return;
                        }
                        //显示头像
                        mAccountView.displayPhoto();

                    }
            }

            @Override
            public void onFailure(Call<UpdateResult> call, Throwable t) {
                //msg
                mAccountView.showMessage("更新失败！");
            }
        });

    }


}
