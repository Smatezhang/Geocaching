package com.zhuoxin.zhang.geocaching.custom;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.DrawableContainer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.zhuoxin.zhang.geocaching.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/9/5.
 */

public class IconSelectPopupWindow extends PopupWindow {

    private Activity mActivity;
    private OnItemClickListener mOnItemClickListener;

    public IconSelectPopupWindow(Activity activity, OnItemClickListener mOnItemClickListener) {
        super(activity.getLayoutInflater().inflate(R.layout.window_select_icon, null), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mActivity = activity;
        ButterKnife.bind(this, getContentView());
        setBackgroundDrawable(new BitmapDrawable());
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public void show() {
        showAtLocation(mActivity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);


    }

    @OnClick({R.id.btn_gallery, R.id.btn_camera, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_gallery:
                mOnItemClickListener.toGallery();
                break;
            case R.id.btn_camera:
                mOnItemClickListener.toCamera();
                break;
            case R.id.btn_cancel:
                mOnItemClickListener.cancel();
                break;
        }
    }


    public interface OnItemClickListener {

        void toGallery();

        void toCamera();

        void cancel();
    }

}
