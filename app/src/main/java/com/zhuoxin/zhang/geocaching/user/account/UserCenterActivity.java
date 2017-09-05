package com.zhuoxin.zhang.geocaching.user.account;

import android.os.Bundle;
import android.support.v4.math.MathUtils;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.zhuoxin.zhang.geocaching.R;
import com.zhuoxin.zhang.geocaching.base.BaseActivity;
import com.zhuoxin.zhang.geocaching.commons.ActivityUtils;
import com.zhuoxin.zhang.geocaching.custom.IconSelectPopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserCenterActivity extends BaseActivity {

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
                    mActivityUtils.showToast("相册");
                }

                @Override
                public void toCamera() {
                    mActivityUtils.showToast("相机");
                }

                @Override
                public void cancel() {
                    mActivityUtils.showToast("取消");
                }
            });
        }

        mIconSelectPopupWindow.show();

    }
}
