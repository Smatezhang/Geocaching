package com.zhuoxin.zhang.geocaching.custom;

import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.zhuoxin.zhang.geocaching.commons.ActivityUtils;

import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by Administrator on 2017/8/25.
 */

public class MainFragmentMp4 extends Fragment implements TextureView.SurfaceTextureListener {

    protected TextureView mTextureView;
    protected MediaPlayer mMediaPlayer;
    protected ActivityUtils mActivityUtils;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //创建TextureView
        mTextureView = new TextureView(getContext());

        return mTextureView;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //设置监听
        mTextureView.setSurfaceTextureListener(this);
        mActivityUtils = new ActivityUtils(this);

    }
    //----------------------------监听事件--------------------------------
    @Override
    public void onSurfaceTextureAvailable(final SurfaceTexture mSurfaceTexture, int mI, int mI1) {
        try {
            //获取资源文件
            AssetFileDescriptor mOpenFd = getContext().getAssets().openFd("welcome.mp4");
            //
            FileDescriptor mDescriptor = mOpenFd.getFileDescriptor();
            //创建 mediaplayer
            mMediaPlayer = new MediaPlayer();
            //给mediaplayer 设置资源
            mMediaPlayer.setDataSource(mDescriptor,mOpenFd.getStartOffset(),mOpenFd.getLength());
           //设置循环次数

            mMediaPlayer.setLooping(true);
            mMediaPlayer.prepareAsync();
            //mediaplayer 的准备监听
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mMediaPlayer) {

                    mMediaPlayer.setSurface(new Surface(mSurfaceTexture));
                    mMediaPlayer.start();

                }
            });
        } catch (IOException mE) {
            mActivityUtils.showToast("播放失败！！"+mE.getMessage());

        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture mSurfaceTexture, int mI, int mI1) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture mSurfaceTexture) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture mSurfaceTexture) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null){
            mMediaPlayer.release();
            mMediaPlayer =null;
        }
    }
}
