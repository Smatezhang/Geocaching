package com.zhuoxin.zhang.geocaching.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.zhuoxin.zhang.geocaching.R;

/**
 * Created by Administrator on 2017/8/25.
 */

public class AlertDialogFragment extends DialogFragment {
    private static final String KEY_TITLE ="key_title" ;
    private static final String KEY_MESSAGE ="key_message" ;

    public static AlertDialogFragment getInstance(String title, String message){
        AlertDialogFragment mAlertDialogFragment = new AlertDialogFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(KEY_TITLE,title);
        mBundle.putString(KEY_MESSAGE,message);
        mAlertDialogFragment.setArguments(mBundle);
        return mAlertDialogFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle mBundle = getArguments();
        String title = mBundle.getString(KEY_TITLE);
        String message = mBundle.getString(KEY_MESSAGE);
        return new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface mDialogInterface, int mI) {
                        mDialogInterface.dismiss();
                    }
                })
                .create();
    }
}
