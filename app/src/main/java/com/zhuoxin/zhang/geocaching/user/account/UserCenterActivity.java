package com.zhuoxin.zhang.geocaching.user.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.math.MathUtils;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;
import com.zhuoxin.zhang.geocaching.R;
import com.zhuoxin.zhang.geocaching.base.BaseActivity;
import com.zhuoxin.zhang.geocaching.commons.ActivityUtils;
import com.zhuoxin.zhang.geocaching.custom.IconSelectPopupWindow;
import com.zhuoxin.zhang.geocaching.entity.UserPrefs;

import org.hybridsquad.android.library.CropHandler;
import org.hybridsquad.android.library.CropHelper;
import org.hybridsquad.android.library.CropParams;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserCenterActivity extends BaseActivity implements AccountView{

    @BindView(R.id.account_toolbar)
    Toolbar mAccountToolbar;
    @BindView(R.id.iv_usericon)
    CircularImageView mIvUsericon;
    @BindView(R.id.user_name)
    TextView mUserName;
    @BindView(R.id.linearLayout)
    RelativeLayout mLinearLayout;
    private ActivityUtils mActivityUtils;
    private IconSelectPopupWindow mIconSelectPopupWindow;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        mActivityUtils = new ActivityUtils(this);
        setSupportActionBar(mAccountToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("个人信息");
        }

    }


    @OnClick(R.id.iv_usericon)
    public void onViewClicked() {

        if (mIconSelectPopupWindow == null) {

            mIconSelectPopupWindow = new IconSelectPopupWindow(this, new IconSelectPopupWindow.OnItemClickListener() {
                @Override
                public void toGallery() {
                    Intent intent = CropHelper.buildCropFromGalleryIntent(mCropHandler.getCropParams());
                    startActivityForResult(intent, CropHelper.REQUEST_CROP);
                   // mActivityUtils.showToast("相册");
                }

                @Override
                public void toCamera() {
                    Intent intent = CropHelper.buildCaptureIntent(mCropHandler.getCropParams().uri);
                    startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
                    //mActivityUtils.showToast("相机");
                }

                @Override
                public void cancel() {
                    mIconSelectPopupWindow.dismiss();
                    //mActivityUtils.showToast("取消");
                }
            });
        }

        mIconSelectPopupWindow.show();

    }

    /**
     * First you need a CropHandler to handle the activity results of cropping photos.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CropHelper.handleResult(mCropHandler, requestCode, resultCode, data);
    }

    /**
     * Make sure you implemented these methods:
     */
    private CropHandler mCropHandler = new CropHandler() {
        @Override
        public void onPhotoCropped(Uri uri) {
            // TODO: 2017/9/5
            new AccoutPresenter(UserCenterActivity.this).upload(new File(uri.getPath()));
        }

        @Override
        public void onCropCancel() {
            mActivityUtils.showToast("剪切取消");
        }

        @Override
        public void onCropFailed(String message) {
            mActivityUtils.showToast("剪切失败！！");
        }

        @Override
        public CropParams getCropParams() {
            CropParams mCropParams = new CropParams();
            return mCropParams;
        }

        @Override
        public Activity getContext() {
            return UserCenterActivity.this;
        }
    };

//=======================================================
    @Override
    public void showMessage(String msg) {
        mActivityUtils.showToast(msg);
    }

    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(UserCenterActivity.this, "上传头像", "玩命上传中.....");

    }

    @Override
    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    public void displayPhoto() {

        String mPhoto = UserPrefs.getInstance().getPhoto();
        if (mPhoto != null){
            Picasso.with(UserCenterActivity.this).load(mPhoto)
                    .error(R.mipmap.user_icon)// 加载错误显示的视图
                    .placeholder(R.mipmap.user_icon)// 占位视图
                    .into(mIvUsericon);
        }
    }
}
