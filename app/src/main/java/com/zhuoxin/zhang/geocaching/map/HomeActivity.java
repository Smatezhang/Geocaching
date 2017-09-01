package com.zhuoxin.zhang.geocaching.map;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zhuoxin.zhang.geocaching.MainActivity;
import com.zhuoxin.zhang.geocaching.R;
import com.zhuoxin.zhang.geocaching.commons.ActivityUtils;
import com.zhuoxin.zhang.geocaching.entity.UserPrefs;
import com.zhuoxin.zhang.geocaching.net.NetClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    @BindView(R.id.navigation)
    NavigationView mNavigation;
    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    protected ActivityUtils mActivityUtils;
    protected ImageView mUserIcon;
    private MapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
        init();
    }

    private void init() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mUserIcon = mNavigation.getHeaderView(0).findViewById(R.id.iv_usericon);
        ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
       //同步状态
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);
        mNavigation.setNavigationItemSelectedListener(this);

        mMapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);


    }

    @Override
    protected void onStart() {
        super.onStart();
        String mPhoto = UserPrefs.getInstance().getPhoto();
        if (mPhoto != null){
            Picasso.with(this).load(NetClient.BASE_URL+mPhoto).into(mUserIcon);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_hide:
               /* // TODO: 2017/8/30
                mActivityUtils.showToast("埋藏宝藏！！");*/
               mMapFragment.changeUIMode(MapFragment.TREASURE_MADE_BURY);
                break;
            case R.id.menu_my_list:
                // TODO: 2017/8/30
                mActivityUtils.showToast("埋藏宝藏列表！！");
                break;
            case R.id.menu_help:
                // TODO: 2017/8/30
                mActivityUtils.showToast("帮助");
                break;
            case R.id.menu_logout:
                // TODO: 2017/8/30
                UserPrefs.getInstance().clearUser();
                mActivityUtils.startActivity(MainActivity.class);
                finish();
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}
