package com.zhuoxin.zhang.geocaching.custom;

import android.content.Context;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.zhuoxin.zhang.geocaching.R;
import com.zhuoxin.zhang.geocaching.map.MapFragment;
import com.zhuoxin.zhang.geocaching.treasure.Treasure;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/9/1.
 */

public class TreasureView extends RelativeLayout {
    @BindView(R.id.treasure_icon)
    ImageView mTreasureIcon;
    @BindView(R.id.tv_treasureTitle)
    TextView mTvTreasureTitle;
    @BindView(R.id.tv_distance)
    TextView mTvDistance;
    @BindView(R.id.linear_title)
    LinearLayout mLinearTitle;
    @BindView(R.id.tv_treasureLocation)
    TextView mTvTreasureLocation;
    @BindView(R.id.iv_arrow)
    ImageView mIvArrow;
    @BindView(R.id.cardView)
    CardView mCardView;
    private Unbinder mUnbinder;

    public TreasureView(Context context) {
        super(context);
        initView();
    }

    public TreasureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TreasureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View mView = LayoutInflater.from(getContext()).inflate(R.layout.view_treasure, this, true);
        mUnbinder = ButterKnife.bind(mView);

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void bindView(Treasure treasure){
        String mTitle = treasure.getTitle();
        //获取位置信息
        String mLocation = treasure.getLocation();
        //获取经度
        double mLongitude = treasure.getLongitude();
        //获取纬度
        double mAltitude = treasure.getLatitude();
        LatLng mTreasurePosition = new LatLng(mAltitude, mLongitude);
        LatLng myselfLocation = MapFragment.getmCurrentLocation();
        double mDistance = DistanceUtil.getDistance(mTreasurePosition, myselfLocation);
        DecimalFormat mFormat = new DecimalFormat("#.00");
        String mFormatDistance = mFormat.format(mDistance)+"km";

        mTvTreasureTitle.setText(mTitle);
        mTvDistance.setText(mFormatDistance);
        mTvTreasureLocation.setText(mLocation);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mUnbinder.unbind();
    }


}
