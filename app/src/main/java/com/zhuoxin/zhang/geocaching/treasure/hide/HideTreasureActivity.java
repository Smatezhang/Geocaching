package com.zhuoxin.zhang.geocaching.treasure.hide;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.baidu.mapapi.model.LatLng;
import com.zhuoxin.zhang.geocaching.R;
import com.zhuoxin.zhang.geocaching.commons.ActivityUtils;
import com.zhuoxin.zhang.geocaching.entity.UserPrefs;
import com.zhuoxin.zhang.geocaching.treasure.Treasure;
import com.zhuoxin.zhang.geocaching.treasure.TreasureRepo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HideTreasureActivity extends AppCompatActivity implements HideTreasureView {


    private static final String TREASURE_TITLE = "treasure_title";
    private static final String TREASURRE_LOCATION = "treasure_location";
    private static final String TREASURE_ADDRESS = "treasure_address";
    @BindView(R.id.hide_send)
    ImageView mHideSend;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_description)
    EditText mEtDescription;
    private ActivityUtils mActivityUtils;
    private ProgressDialog mProgressDialog;
    private String mMtitle;
    private String mAddress;
    private LatLng mLocation;

    public static void open(Context context, String title, LatLng location,String address) {
        Intent mIntent = new Intent(context, HideTreasureActivity.class);
        mIntent.putExtra(TREASURE_TITLE, title);
        mIntent.putExtra(TREASURRE_LOCATION,location);
        mIntent.putExtra(TREASURE_ADDRESS,address);
        context.startActivity(mIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hide_treasure);
        ButterKnife.bind(this);

        mActivityUtils = new ActivityUtils(this);
        Intent mIntent = getIntent();
        mMtitle = mIntent.getStringExtra(TREASURE_TITLE);
        mAddress = mIntent.getStringExtra(TREASURE_ADDRESS);
        mLocation = mIntent.getParcelableExtra(TREASURRE_LOCATION);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mMtitle);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick(R.id.hide_send)
    public void onViewClicked() {
        HideTreasure mHideTreasure = new HideTreasure();
        mHideTreasure.setTitle(mMtitle);
        mHideTreasure.setDescription(mEtDescription.getText().toString().trim());
        mHideTreasure.setTokenId(UserPrefs.getInstance().getTokenid());
        mHideTreasure.setAltitude(0.0);
        mHideTreasure.setLocation(mAddress);
        mHideTreasure.setLatitude(mLocation.latitude);
        mHideTreasure.setLongitude(mLocation.longitude);
        new HideTreasurePresenter(this).hideTreasure(mHideTreasure);
        //清除缓存
        TreasureRepo.getInstance().clear();

    }

    //===================================================================
    @Override
    public void showMessage(String msg) {
        mActivityUtils.showToast(msg);
    }

    @Override
    public void showProgress() {
        mProgressDialog = ProgressDialog.show(this, "宝藏上传", "玩命上传中......");
    }

    @Override
    public void hideProgress() {
        mProgressDialog.dismiss();
    }

    @Override
    public void backHome() {
        finish();
    }

    @Override
    public void clearET() {
        mEtDescription.setText("");
    }
}
