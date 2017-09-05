package com.zhuoxin.zhang.geocaching.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
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

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.zhuoxin.zhang.geocaching.R;
import com.zhuoxin.zhang.geocaching.commons.ActivityUtils;
import com.zhuoxin.zhang.geocaching.custom.TreasureView;
import com.zhuoxin.zhang.geocaching.treasure.Area;
import com.zhuoxin.zhang.geocaching.treasure.Treasure;
import com.zhuoxin.zhang.geocaching.treasure.TreasureRepo;
import com.zhuoxin.zhang.geocaching.treasure.detail.TreasureDetailActivity;
import com.zhuoxin.zhang.geocaching.treasure.hide.HideTreasureActivity;
import com.zhuoxin.zhang.geocaching.user.login.LoginView;


import java.util.IllegalFormatCodePointException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/8/30.
 */

public class MapFragment extends Fragment implements MapFragmentView {

    private static final int REQUEST_CODE = 100;
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
    @BindView(R.id.treasureView)
    TreasureView mTreasureView;
    @BindView(R.id.hide_treasure)
    RelativeLayout mHide_treasure;

    private View mView;
    private BaiduMap mBaiduMap;
    private static LatLng mCurrentLocation;
    private boolean isFirst = true;
    private LocationClient mLocationClient;
    private ActivityUtils mActivityUtils;
    private BitmapDescriptor mDot;
    private BitmapDescriptor mWindow;
    private Marker mCurrentMaker;
    public static final int TREASURE_MADE_NORMAL = 0;
    public static final int TREASURE_MADE_SELECT = 1;
    public static final int TREASURE_MADE_BURY = 2;
    private int currentMode = TREASURE_MADE_NORMAL;
    private InfoWindow mInfoWindow;
    private static String mAddrStr;
    private GeoCoder mGeoCoder;
    private LatLng mCurrentStatus;
    private String mGeoAddress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_map, null);
        unbinder = ButterKnife.bind(this, mView);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE);
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivityUtils = new ActivityUtils(this);
        //初始化
        initMapView();
        //初始化位置
        intiLocation();
        //初始化地理编码
        initGeoCorder();
    }

    /**
     * 初始化地理编码
     */
    private void initGeoCorder() {

        mGeoCoder = GeoCoder.newInstance();
        Log.e("TAG", "==========1");
        mGeoCoder.setOnGetGeoCodeResultListener(mOnGetGeoCoderResultListener);
    }

    private OnGetGeoCoderResultListener mOnGetGeoCoderResultListener = new OnGetGeoCoderResultListener() {


        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                mGeoAddress = "未知地址";
                Log.e("TAG", reverseGeoCodeResult.getAddress());
                mTvCurrentLocation.setText(mGeoAddress);
                return;
            }
            Log.e("TAG", reverseGeoCodeResult.getAddress());
            mGeoAddress = reverseGeoCodeResult.getAddress();
            mTvCurrentLocation.setText(mGeoAddress);

        }
    };

    /**
     * 更改模式
     *
     * @param mode
     */
    public void changeUIMode(int mode) {
        if (mode == currentMode) {
            return;
        }
        currentMode = mode;


        switch (mode) {
            case TREASURE_MADE_NORMAL:
                if (mCurrentMaker != null) {
                    mCurrentMaker.setVisible(true);
                }
                mLayoutBottom.setVisibility(View.GONE);
                mBaiduMap.hideInfoWindow();
                mCenterLayout.setVisibility(View.GONE);

                break;
            case TREASURE_MADE_SELECT:
                //mCurrentMaker.setVisible(false);
                //mBaiduMap.showInfoWindow(mInfoWindow);
                //mLayoutBottom.setVisibility(View.VISIBLE);
                mTreasureView.setVisibility(View.VISIBLE);
                mHide_treasure.setVisibility(View.GONE);
                mCenterLayout.setVisibility(View.GONE);
                break;
            case TREASURE_MADE_BURY:
                if (mCurrentMaker != null) {
                    mCurrentMaker.setVisible(true);
                }
                mCenterLayout.setVisibility(View.VISIBLE);
                mLayoutBottom.setVisibility(View.GONE);
                mBaiduMap.hideInfoWindow();
                mTreasureView.setVisibility(View.GONE);
                mBtnHideHere.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mLayoutBottom.setVisibility(View.VISIBLE);
                        mTreasureView.setVisibility(View.GONE);
                        mHide_treasure.setVisibility(View.VISIBLE);
                    }
                });

                break;

        }


    }

    /**
     * 显示宝藏详情页面
     */
    @OnClick(R.id.treasureView)
    public void navigateToDetail() {
        int mTreasure_id = mCurrentMaker.getExtraInfo().getInt("treasure_id");
        Treasure mTreasure = TreasureRepo.getInstance().getTreasure(mTreasure_id);
        TreasureDetailActivity.open(getContext(), mTreasure);

    }

    /**
     * 埋藏宝藏
     */
    @OnClick(R.id.hide_treasure)
    public void hideTreasure() {

        String mTitle = mEtTreasureTitle.getText().toString().trim();
        if (TextUtils.isEmpty(mTitle)) {
            mActivityUtils.showToast("宝藏标题不能为空！");
            return;
        }
        HideTreasureActivity.open(getContext(), mTitle, mCurrentStatus, mGeoAddress);

    }

    /**
     * 初始化位置
     */
    private void intiLocation() {

        mBaiduMap.setMyLocationEnabled(true);
        //第一步,声明LocationClient类
        mLocationClient = new LocationClient(getContext().getApplicationContext());
        //第二步，配置定位SDK参数
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        //option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        //option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        mLocationClient.setLocOption(option);
        //第三步，实现BDAbstractLocationListener接口
        mLocationClient.registerLocationListener(mBDLocationListener);
        //第四步，开始定位
        mLocationClient.start();

    }

    //百度地图状态改变监听
    private BaiduMap.OnMapStatusChangeListener mOnMapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        @Override
        //地图状态改变开始
        public void onMapStatusChangeStart(MapStatus mapStatus) {

        }

        @Override
        //地图状态改变中
        public void onMapStatusChange(MapStatus mapStatus) {

        }

        @Override
        // 地图状态改变结束
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            LatLng mTarget = mapStatus.target;
            if (mCurrentStatus != mTarget) {
                //更新地图
                updateView(mTarget);
            }
            if (currentMode == TREASURE_MADE_BURY) {
                //Log.e("TAG","==========2");
                mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(mTarget));
            }
            // Log.e("TAG","==========3");
            mCurrentStatus = mTarget;
        }
    };

    /**
     * 更新地图
     *
     * @param target
     */

    private void updateView(LatLng target) {
        double mLatitude = target.latitude;
        double mLongitude = target.longitude;
        Area mArea = new Area();
        mArea.setMaxLat(Math.ceil(mLatitude));
        mArea.setMaxLng(Math.ceil(mLongitude));
        mArea.setMinLat(Math.floor(mLatitude));
        mArea.setMinLng(Math.floor(mLongitude));
        new MapFragmentPresenter(this).getTreasures(mArea);
    }

    /**
     * 位置改变监听
     */
    private BDLocationListener mBDLocationListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {

            //获取定位结果
            String locationTime = location.getTime();//获取定位时间

            //location.getLocType();    //获取定位类型
            double mLatitude = location.getLatitude();//获取纬度信息
            double mLongitude = location.getLongitude();//获取经度信息
            mCurrentLocation = new LatLng(mLatitude, mLongitude);
            MyLocationData mMyLocationData = new MyLocationData.Builder()
                    .accuracy(100)
                    .latitude(mLatitude)
                    .longitude(mLongitude)
                    .build();
            mBaiduMap.setMyLocationData(mMyLocationData);
            float mRadius = location.getRadius();//获取定位精准度


            //获取地址信息
            mAddrStr = location.getAddrStr();
            String mCounttry = location.getCountry();//获取国家信息
            location.getCountryCode();    //获取国家码
            String mCity = location.getCity();//获取城市信息
            location.getCityCode();    //获取城市码
            String mDistrict = location.getDistrict();//获取区县信息
            String mStreet = location.getStreet();//获取街道信息
            location.getStreetNumber();    //获取街道码
            String mLocationDescribe = location.getLocationDescribe();//获取当前位置描述信息
            //location.getPoiList();    //获取当前位置周边POI信息
            //location.getBuildingID();    //室内精准定位下，获取楼宇ID
            //location.getBuildingName();    //室内精准定位下，获取楼宇名称
            //location.getFloor();    //室内精准定位下，获取当前位置所处的楼层信息
            Log.e("================", "纬度：" + mLatitude + "经度：" + mLongitude + "地址：" + mAddrStr + "   详细描述：" + mLocationDescribe
                    + "定位精准度：" + mRadius + " 定位时间：" + locationTime);

            //if (mAddrStr!=null)
            if (isFirst) {
                moveToLocation();
                isFirst = false;
            }


        }
    };

    /**
     * 定位功能
     */
    private void moveToLocation() {

        mLocationClient.requestLocation();
        MapStatus mMapStatus = new MapStatus.Builder()
                .zoom(19)
                .rotate(0f)
                .overlook(0f)
                .target(mCurrentLocation)
                .build();

        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mMapStatus));

    }

    /**
     * 初始化地图
     */
    private void initMapView() {
        //mDot = BitmapDescriptorFactory.fromResource(R.mipmap.plant_xiaojianguoqiang_11);
        mDot = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_dot);
        mWindow = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_expanded);

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
        //地图状态改变监听
        mBaiduMap.setOnMapStatusChangeListener(mOnMapStatusChangeListener);
        //地图覆盖物点击监听
        mBaiduMap.setOnMarkerClickListener(mOnMarkerClickListener);
    }

    /**
     * 覆盖物点击事件
     */
    private BaiduMap.OnMarkerClickListener mOnMarkerClickListener = new BaiduMap.OnMarkerClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public boolean onMarkerClick(Marker marker) {
            if (mCurrentMaker != null) {
                //当前被点击的标记物不为空，说明，肯定有覆盖物被点击了
                mCurrentMaker.setVisible(true);

            }
            mCurrentMaker = marker;
            mCurrentMaker.setVisible(false);

            LatLng mPosition = mCurrentMaker.getPosition();
            mInfoWindow = new InfoWindow(mWindow, mPosition, 0, new InfoWindow.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick() {
                  /*  mBaiduMap.hideInfoWindow();
                    mCurrentMaker.setVisible(true);
                    mLayoutBottom.setVisibility(View.GONE);*/
                    changeUIMode(TREASURE_MADE_NORMAL);
                }
            });
            int mTreasure_id = marker.getExtraInfo().getInt("treasure_id");
            Treasure mTreasure = TreasureRepo.getInstance().getTreasure(mTreasure_id);

            mTreasureView.bindView(mTreasure);
            mLayoutBottom.setVisibility(View.VISIBLE);
            // mTreasureView.setVisibility(View.VISIBLE);
            mBaiduMap.showInfoWindow(mInfoWindow);


            changeUIMode(TREASURE_MADE_SELECT);
            return false;
        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 点击事件
     *
     * @param view
     */
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

                moveToLocation();

                break;
            case R.id.tv_satellite:
                //卫星
                int mMapType = mBaiduMap.getMapType();
                mMapType = mMapType == BaiduMap.MAP_TYPE_NORMAL ? BaiduMap.MAP_TYPE_SATELLITE : BaiduMap.MAP_TYPE_NORMAL;
                mBaiduMap.setMapType(mMapType);
                mTvSatellite.setText(mMapType == BaiduMap.MAP_TYPE_NORMAL ? "卫星" : "普通");

                break;
            case R.id.tv_compass:
                //指南针
                boolean mCompassEnabled = mBaiduMap.getUiSettings().isCompassEnabled();
                mBaiduMap.getUiSettings().setCompassEnabled(!mCompassEnabled);


                break;
        }

    }

    /**
     * 获取权限
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationClient.requestLocation();

                } else {
                    mActivityUtils.showToast("权限不足，请去应用权限授权！！");
                }
        }
    }
    //--------------------------实现自视图接口的方法-------------------------

    /**
     * 展示宝藏
     *
     * @param treasureList
     */
    @Override
    public void showTreasure(List<Treasure> treasureList) {
        mBaiduMap.clear();
        if (currentMode != TREASURE_MADE_BURY) {
            mLayoutBottom.setVisibility(View.GONE);
            changeUIMode(TREASURE_MADE_NORMAL);
        }


        Log.e("=========", treasureList.size() + "");
        for (Treasure treasure : treasureList) {
            Bundle mBundle = new Bundle();
            mBundle.putInt("treasure_id", treasure.getId());
            MarkerOptions mOptions = new MarkerOptions();
            //设置 marker 覆盖物的锚点比例，默认（0.5f, 1.0f）水平居中，垂直下对齐
            mOptions.anchor(0.5f, 0.5f);
            //设置 marker 覆盖物的额外信息
            mOptions.extraInfo(mBundle);
            //设置 Marker 覆盖物的图标，相同图案的 icon 的 marker 最好使用同一个 BitmapDescriptor 对象以节省内存空间。
            mOptions.icon(mDot);
            //设置 marker 覆盖物的位置坐标
            LatLng mLatLng = new LatLng(treasure.getLatitude(), treasure.getLongitude());
            mOptions.position(mLatLng);
            /*DotOptions mDotOptions = new DotOptions();
            mDotOptions.extraInfo(mBundle);
            mDotOptions.center(mLatLng);
            mDotOptions.color(Color.BLUE);*/

            mBaiduMap.addOverlay(mOptions);

        }


    }

    /**
     * 展示信息
     *
     * @param msg
     */
    @Override
    public void showMessage(String msg) {

        mActivityUtils.showToast(msg);

    }


    public static LatLng getmCurrentLocation() {
        return mCurrentLocation;
    }

    public static String getCurrentAddress() {
        return mAddrStr;
    }

    public Boolean isBuryMode() {
        if (currentMode == TREASURE_MADE_BURY) {
            return true;
        } else {

            return false;
        }
    }


}
