package com.zhuoxin.zhang.geocaching.map;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.zhuoxin.zhang.geocaching.MainActivity;
import com.zhuoxin.zhang.geocaching.R;
import com.zhuoxin.zhang.geocaching.commons.ActivityUtils;
import com.zhuoxin.zhang.geocaching.entity.UserPrefs;
import com.zhuoxin.zhang.geocaching.net.NetClient;
import com.zhuoxin.zhang.geocaching.treasure.list.TreasureListFragment;
import com.zhuoxin.zhang.geocaching.user.account.UserCenterActivity;

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
    private FragmentManager mSupportFragmentManager;
    private TreasureListFragment mTreasureListFragment;

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

        mSupportFragmentManager = getSupportFragmentManager();
        mMapFragment = (MapFragment) mSupportFragmentManager.findFragmentById(R.id.mapFragment);
        mTreasureListFragment = new TreasureListFragment();
        // 用户头像点击事件
        mUserIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,UserCenterActivity.class));
            }
        });

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
               if (mTreasureListFragment.isAdded()){
                   mSupportFragmentManager.popBackStack();
                   //mSupportFragmentManager.beginTransaction().remove(mTreasureListFragment).commit();
               }
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


    /**
     * 当你执行invalidateOptionsMenu()就会执行一次
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem mItem = menu.getItem(0);
        if (mItem.getItemId() == R.id.action_toggle){
            if (mTreasureListFragment!= null&&mTreasureListFragment.isAdded()){
                mItem.setIcon(R.drawable.ic_map);
            } else {
                mItem.setIcon(R.drawable.ic_view_list);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_toggle){
            if (mTreasureListFragment!= null&&mTreasureListFragment.isAdded()){
                Log.e("TAG","++++++++++++++++");
                mSupportFragmentManager.popBackStack();
                mSupportFragmentManager.beginTransaction().remove(mTreasureListFragment).commit();
            }else {
                Log.e("TAG","===================");
                mSupportFragmentManager.beginTransaction().replace(R.id.fragment_container,mTreasureListFragment)
                        .addToBackStack(null).commit();
            }
            invalidateOptionsMenu();
        }

        return super.onOptionsItemSelected(item);
    }



    /**
     * 当activity创建的时候执行，在activity的每一次生命周期中只执行一次
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 重写返回键
     */
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START)){
            mDrawerLayout.closeDrawer(Gravity.START);
            return;
        }

        if (mMapFragment.isBuryMode()){
            mMapFragment.changeUIMode(MapFragment.TREASURE_MADE_NORMAL);
            return;
        }

        super.onBackPressed();
    }
}
