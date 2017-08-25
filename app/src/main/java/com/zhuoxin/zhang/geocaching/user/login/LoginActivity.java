package com.zhuoxin.zhang.geocaching.user.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhuoxin.zhang.geocaching.R;
import com.zhuoxin.zhang.geocaching.commons.ActivityUtils;
import com.zhuoxin.zhang.geocaching.commons.RegexUtils;
import com.zhuoxin.zhang.geocaching.custom.AlertDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_Username)
    EditText mEtUsername;
    @BindView(R.id.et_Password)
    EditText mEtPassword;
    @BindView(R.id.tv_forgetPassword)
    TextView mTvForgetPassword;
    @BindView(R.id.btn_Login)
    Button mBtnLogin;
    protected ActivityUtils mActivityUtils;
    protected String mpassword;
    protected String musername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mActivityUtils = new ActivityUtils(this);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.login);
        mEtUsername.addTextChangedListener(mTextWatcher);
        mEtPassword.addTextChangedListener(mTextWatcher);

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
            musername = mEtUsername.getText().toString().trim();
            mpassword = mEtPassword.getText().toString().trim();
            boolean canClick = !TextUtils.isEmpty(musername)&&!TextUtils.isEmpty(mpassword);
            mBtnLogin.setEnabled(canClick);
        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()){
                case android.R.id.home:
                    finish();
                    break;
            }
            return super.onOptionsItemSelected(item);

    }

    @OnClick(R.id.btn_Login)
    public void onViewClicked() {
        if (RegexUtils.verifyUsername(musername)!=RegexUtils.VERIFY_SUCCESS){
            AlertDialogFragment.getInstance(getString(R.string.username_error)
                    ,getString(R.string.username_rules)).show(getSupportFragmentManager(),
                    "mUsername");
            return;
        }
        if (RegexUtils.verifyUsername(mpassword)!=RegexUtils.VERIFY_SUCCESS){
            AlertDialogFragment.getInstance(getString(R.string.password_error)
                    ,getString(R.string.password_rules)).show(getSupportFragmentManager(),"password");
            return;
        }
        // TODO: 2017/8/25
        mActivityUtils.showToast("登录成功！！");
    }
}
