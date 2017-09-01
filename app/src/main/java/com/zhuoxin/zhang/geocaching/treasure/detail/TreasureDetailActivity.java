package com.zhuoxin.zhang.geocaching.treasure.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.zhuoxin.zhang.geocaching.R;
import com.zhuoxin.zhang.geocaching.commons.ActivityUtils;
import com.zhuoxin.zhang.geocaching.custom.TreasureView;
import com.zhuoxin.zhang.geocaching.treasure.Treasure;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TreasureDetailActivity extends AppCompatActivity implements TreasureDetailView {
    private static final String TREASURE = "treasure";
    @BindView(R.id.iv_navigation)
    ImageView mIvNavigation;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.frameLayout)
    FrameLayout mFrameLayout;
   @BindView(R.id.detail_treasure)
    TreasureView mTreasureView;
    @BindView(R.id.tv_detail_description)
    TextView mTvDetailDescription;
    private Treasure mTreasure;
    private BaiduMap mBaiduMap;
    private ActivityUtils mActivityUtils;

    public static void open(Context context, Treasure treasure) {
        Intent mIntent = new Intent(context,TreasureDetailActivity.class);
        mIntent.putExtra(TREASURE, treasure);
        context.startActivity(mIntent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_detail);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
        mTreasure = (Treasure) getIntent().getSerializableExtra(TREASURE);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(mTreasure.getTitle());
        }

        init();

        new TreasureDetailPresenter(this).getTreasureDetail(new TreasureDetail(mTreasure.getId()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {

        LatLng mLatLng = new LatLng(mTreasure.getLatitude(), mTreasure.getLongitude());
        MapStatus mapStatus = new MapStatus.Builder()
                .overlook(-20f)//地图俯仰角度。
                .rotate(0f)//地图旋转角度。
                .zoom(19)//地图缩放级别 3~21
                .target(mLatLng)
                .build();
        BaiduMapOptions mOptions = new BaiduMapOptions()
                .scaleControlEnabled(false)
                .mapStatus(mapStatus)
                .compassEnabled(false)
                .overlookingGesturesEnabled(false)
                .rotateGesturesEnabled(false)
                .scrollGesturesEnabled(false)
                .zoomControlsEnabled(false)
                .zoomGesturesEnabled(false);



        MapView mMapView = new MapView(this,mOptions);

        mBaiduMap = mMapView.getMap();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(mLatLng);

        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.treasure_expanded));
        mBaiduMap.addOverlay(markerOptions);//添加覆盖物
        mFrameLayout.addView(mMapView);
        //显示宝藏信息
        mTreasureView.bindView(mTreasure);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);


    }

    //=============================实现自接口的方法========================
    @Override
    public void showTreasureDetail(List<TreasureDetailResult> treasureDetailResultList) {
        if (treasureDetailResultList !=null){
            if (treasureDetailResultList.size()>= 1){
                TreasureDetailResult mTreasureDetailResult = treasureDetailResultList.get(0);
                mTvDetailDescription.setText(mTreasureDetailResult.description);
                return;
            }
            mTvDetailDescription.setText("没有描述！！！");
        }
    }

    @Override
    public void showMessage(String msg) {
    mActivityUtils.showToast(msg);
    }
}
