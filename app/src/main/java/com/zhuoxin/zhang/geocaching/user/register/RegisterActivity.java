package com.zhuoxin.zhang.geocaching.user.register;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.zhuoxin.zhang.geocaching.R;
import com.zhuoxin.zhang.geocaching.commons.ActivityUtils;
import com.zhuoxin.zhang.geocaching.commons.RegexUtils;
import com.zhuoxin.zhang.geocaching.custom.AlertDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_Username)
    EditText mEtUsername;
    @BindView(R.id.et_Password)
    EditText mEtPassword;
    @BindView(R.id.et_Confirm)
    EditText mEtConfirm;
    @BindView(R.id.btn_Register)
    Button mBtnRegister;
    protected ActivityUtils mActivityUtils;
    protected String confirm;
    protected String password;
    protected String mUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.register);
        mEtUsername.addTextChangedListener(mTextWatcher);
        mEtPassword.addTextChangedListener(mTextWatcher);
        mEtConfirm.addTextChangedListener(mTextWatcher);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private TextWatcher mTextWatcher = new TextWatcher() {



        @Override
        public void beforeTextChanged(CharSequence mCharSequence, int mI, int mI1, int mI2) {

        }

        @Override
        public void onTextChanged(CharSequence mCharSequence, int mI, int mI1, int mI2) {

        }

        @Override
        public void afterTextChanged(Editable mEditable) {
            mUsername = mEtUsername.getText().toString().trim();
            password = mEtPassword.getText().toString().trim();
            confirm = mEtConfirm.getText().toString().trim();
            boolean canClick = !TextUtils.isEmpty(mUsername)&&!TextUtils.isEmpty(password)
                    &&!TextUtils.isEmpty(confirm);
            mBtnRegister.setEnabled(canClick);
        }
    };

    @OnClick(R.id.btn_Register)
    public void onViewClicked() {
        if (RegexUtils.verifyUsername(mUsername)!=RegexUtils.VERIFY_SUCCESS){
            AlertDialogFragment.getInstance(getString(R.string.username_error)
                    ,getString(R.string.username_rules)).show(getSupportFragmentManager(),
                    "mUsername");
            return;
        }
        if (RegexUtils.verifyUsername(password)!=RegexUtils.VERIFY_SUCCESS){
            AlertDialogFragment.getInstance(getString(R.string.password_error)
                    ,getString(R.string.password_rules)).show(getSupportFragmentManager(),"password");
            return;
        }
        if (!TextUtils.equals(password,confirm)){
            AlertDialogFragment.getInstance("注册失败","两次输入的密码不一致！")
                    .show(getSupportFragmentManager(),"useName");
            return;
        }
        // TODO: 2017/8/25
        mActivityUtils.showToast("注册成功！");

    }
}
