package com.zhuoxin.zhang.geocaching.treasure.list;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuoxin.zhang.geocaching.R;
import com.zhuoxin.zhang.geocaching.custom.TreasureView;
import com.zhuoxin.zhang.geocaching.treasure.Treasure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/5.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {

    private List<Treasure> mTreasureList = new ArrayList<>();

    public MyRecyclerViewAdapter(List<Treasure> treasureList) {
        mTreasureList = treasureList;
    }

    @Override
    public MyRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder mViewHolder = new MyViewHolder(new TreasureView(parent.getContext()));

        return mViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Treasure mTreasure = mTreasureList.get(position);
        holder.mTreasureView.bindView(mTreasure);
        holder.mTreasureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.itemClickListener(mTreasure);
            }
        });
    }



    @Override
    public int getItemCount() {
        return mTreasureList.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder{
        TreasureView mTreasureView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTreasureView =(TreasureView)itemView;
        }
    }

    interface OnItemClickListener {
        void itemClickListener(Treasure treasure);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private OnItemClickListener mOnItemClickListener ;

}
