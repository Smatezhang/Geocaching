package com.zhuoxin.zhang.geocaching.treasure.detail;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
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
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.zhuoxin.zhang.geocaching.R;
import com.zhuoxin.zhang.geocaching.commons.ActivityUtils;
import com.zhuoxin.zhang.geocaching.custom.TreasureView;
import com.zhuoxin.zhang.geocaching.map.MapFragment;
import com.zhuoxin.zhang.geocaching.treasure.Treasure;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
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

    @OnClick(R.id.iv_navigation)
    public void navigation(){
        final String mCurrentAddress = MapFragment.getCurrentAddress();
        final LatLng mCurrentLocation = MapFragment.getmCurrentLocation();
        final String mEndAddress = mTreasure.getLocation();
        final LatLng mEndLocation = new LatLng(mTreasure.getLatitude(), mTreasure.getLongitude());
        PopupMenu mPopupMenu = new PopupMenu(TreasureDetailActivity.this, mIvNavigation);
        mPopupMenu.inflate(R.menu.menu_navigation);
        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.walking_navi:
                        // TODO: 2017/9/4  walking Navigation
                        openNavigation(mCurrentLocation,mCurrentAddress,mEndAddress,mEndLocation,1);
                        break;
                    case R.id.biking_navi:
                        // TODO: 2017/9/4 biking navigation
                        openNavigation(mCurrentLocation, mCurrentAddress, mEndAddress, mEndLocation, 2);
                        break;
                }
                return false;
            }
        });
        mPopupMenu.show();

    }

    private void openNavigation(LatLng currentLocation, String currentAddress, String endAddress, LatLng endLocation, int i) {
        NaviParaOption mNaviParaOption = new NaviParaOption()
                .startName(currentAddress)
                .startPoint(currentLocation)
                .endName(endAddress)
                .endPoint(endLocation);
        int type = i;
        if (type == 1){
            boolean mB = BaiduMapNavigation.openBaiduMapWalkNavi(mNaviParaOption, TreasureDetailActivity.this);
            if (!mB){
                BaiduMapNavigation.openWebBaiduMapNavi(mNaviParaOption,TreasureDetailActivity.this);
            }
        }else {
            boolean mB = BaiduMapNavigation.openBaiduMapBikeNavi(mNaviParaOption, TreasureDetailActivity.this);
            if (!mB){
                 new AlertDialog.Builder(TreasureDetailActivity.this)
                        .setTitle("骑行导航")
                        .setMessage("检测到系统未下载百度地图app或者地图版本太低，建议下载安装最新版的百度地图")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                OpenClientUtil.getLatestBaiduMapApp(TreasureDetailActivity.this);
                            }
                        }).create()
                        .show();




            }
        }


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
