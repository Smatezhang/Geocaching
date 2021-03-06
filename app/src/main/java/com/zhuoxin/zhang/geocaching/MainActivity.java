package com.zhuoxin.zhang.geocaching;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhuoxin.zhang.geocaching.commons.ActivityUtils;
import com.zhuoxin.zhang.geocaching.entity.UserPrefs;
import com.zhuoxin.zhang.geocaching.map.HomeActivity;
import com.zhuoxin.zhang.geocaching.user.login.LoginActivity;
import com.zhuoxin.zhang.geocaching.user.register.RegisterActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_MAIN = "main_home";
    protected ActivityUtils mActivityUtils;
    protected BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context mContext, Intent mIntent) {
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
        SharedPreferences mUser_info = getSharedPreferences("user_info", MODE_PRIVATE);

        if (mUser_info!= null){
            int mKey_tokenid = mUser_info.getInt("key_tokenid", 0);
            if (mKey_tokenid == UserPrefs.getInstance().getTokenid()){
                mActivityUtils.startActivity(HomeActivity.class);
                finish();
            }
        }


        IntentFilter mIntentFilter = new IntentFilter(ACTION_MAIN);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,mIntentFilter);

    }

    @OnClick({R.id.btn_Register, R.id.btn_Login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_Register:
                mActivityUtils.startActivity(RegisterActivity.class);
                break;
            case R.id.btn_Login:
                mActivityUtils.startActivity(LoginActivity.class);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }
}
