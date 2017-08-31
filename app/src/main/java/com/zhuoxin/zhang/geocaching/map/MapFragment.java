package com.zhuoxin.zhang.geocaching.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.zhuoxin.zhang.geocaching.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/8/30.
 */

public class MapFragment extends Fragment {

    @BindView(R.id.center)
    Space mCenter;
    @BindView(R.id.iv_located)
    ImageView mIvLocated;
    @BindView(R.id.btn_HideHere)
    Button mBtnHideHere;
    @BindView(R.id.centerLayout)
    RelativeLayout mCenterLayout;
    @BindView(R.id.iv_scaleUp)
    ImageView mIvScaleUp;
    @BindView(R.id.iv_scaleDown)
    ImageView mIvScaleDown;
    @BindView(R.id.tv_located)
    TextView mTvLocated;
    @BindView(R.id.tv_satellite)
    TextView mTvSatellite;
    @BindView(R.id.tv_compass)
    TextView mTvCompass;
    @BindView(R.id.ll_locationBar)
    LinearLayout mLlLocationBar;
    @BindView(R.id.tv_currentLocation)
    TextView mTvCurrentLocation;
    @BindView(R.id.iv_toTreasureInfo)
    ImageView mIvToTreasureInfo;
    @BindView(R.id.et_treasureTitle)
    EditText mEtTreasureTitle;
    @BindView(R.id.cardView)
    CardView mCardView;
    @BindView(R.id.layout_bottom)
    FrameLayout mLayoutBottom;
    @BindView(R.id.map_frame)
    FrameLayout mMapFrame;
    Unbinder unbinder;
    private View mView;
    private BaiduMap mBaiduMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, null);
        unbinder = ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化
        initMapView();
        //

    }

    private void initMapView() {
        //定义地图状态
        MapStatus mapStatus = new MapStatus.Builder()
                .overlook(0f)//地图俯仰角度。
                .rotate(0f)//地图旋转角度。
                .zoom(16)//地图缩放级别 3~21
                .build();

        BaiduMapOptions mBaiduMapOptions = new BaiduMapOptions()
                .mapStatus(mapStatus)//设置地图初始化时的地图状态， 默认地图中心点为北京天安门，缩放级别为 12.0f
                .scaleControlEnabled(false)//设置是否显示比例尺控件
                .zoomControlsEnabled(false)//设置是否显示缩放控件
                .zoomGesturesEnabled(true);//设置是否允许缩放手势

        MapView mMapView = new MapView(getContext(), mBaiduMapOptions);
        mBaiduMap = mMapView.getMap();
        //将MapView加入到Fragment中
        mMapFrame.addView(mMapView, 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_scaleUp, R.id.iv_scaleDown, R.id.tv_located, R.id.tv_satellite, R.id.tv_compass})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_scaleUp:
                //放大
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());

                break;
            case R.id.iv_scaleDown:
                //缩小
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());

                break;
            case R.id.tv_located:
                //定位



                break;
            case R.id.tv_satellite:
                //卫星
                int mMapType = mBaiduMap.getMapType();
                mMapType = mMapType==BaiduMap.MAP_TYPE_NORMAL?BaiduMap.MAP_TYPE_SATELLITE:BaiduMap.MAP_TYPE_NORMAL;
                mBaiduMap.setMapType(mMapType);
                mTvSatellite.setText(mMapType==BaiduMap.MAP_TYPE_NORMAL?"卫星":"普通");

                break;
            case R.id.tv_compass:
                //指南针
                boolean mCompassEnabled = mBaiduMap.getUiSettings().isCompassEnabled();
                mBaiduMap.getUiSettings().setCompassEnabled(!mCompassEnabled);


                break;
        }
    }
}
