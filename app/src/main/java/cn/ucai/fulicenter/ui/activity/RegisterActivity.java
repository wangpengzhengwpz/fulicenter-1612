package cn.ucai.fulicenter.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.MD5;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by Administrator on 2017/3/20.
 */
public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.tv_username)
    EditText tvUsername;
    @BindView(R.id.tv_nick)
    EditText tvNick;
    @BindView(R.id.tv_password)
    EditText tvPassword;
    @BindView(R.id.confirm_password)
    EditText confirmPassword;
    IUserModel model;
    String username;
    String nickname;
    String password;
    ProgressDialog pd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
        model = new UserModel();
    }

    private void initView() {
        tvCommonTitle.setText(R.string.register_title);
    }

    @OnClick({R.id.backClickArea, R.id.btn_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backClickArea:
                MFGT.finish(RegisterActivity.this);
                break;
            case R.id.btn_register:
                register();
                break;
        }
    }

    private void register() {
        if (checkInput()) {
            showDialog();
            model.register(RegisterActivity.this, username, nickname,
                    MD5.getMessageDigest(password), new OnCompleteListener<String>() {
                @Override
                public void onSuccess(String s) {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    if (result != null) {
                        if (result.isRetMsg()) {
                            registerSuccess();
                        } else {
                            if (result.getRetCode() == I.MSG_REGISTER_USERNAME_EXISTS) {
                                CommonUtils.showShortToast(R.string.register_fail_exists);
                            } else {
                                CommonUtils.showShortToast(R.string.register_fail);
                            }
                        }
                    }
                    pd.dismiss();
                }

                @Override
                public void onError(String error) {
                    pd.dismiss();
                    CommonUtils.showShortToast(R.string.register_fail);
                }
            });
        }
    }

    private void showDialog() {
        pd = new ProgressDialog(RegisterActivity.this);
        pd.setMessage(getString(R.string.registering));
        pd.show();
    }

    private void registerSuccess() {
        setResult(RESULT_OK, new Intent().putExtra(I.User.USER_NAME, username));
        CommonUtils.showShortToast(R.string.register_success);
        MFGT.finish(RegisterActivity.this);
    }

    private boolean checkInput() {
        username = tvUsername.getText().toString().trim();
        nickname = tvNick.getText().toString().trim();
        password = tvPassword.getText().toString().trim();
        String confirm = confirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            tvUsername.requestFocus();
            tvUsername.setError(getString(R.string.user_name_connot_be_empty));
            return false;
        }
        if (!username.matches("[a-zA-Z]\\w{5,15}")) {
            tvUsername.requestFocus();
            tvUsername.setError(getString(R.string.illegal_user_name));
        }
        if (TextUtils.isEmpty(nickname)) {
            tvNick.requestFocus();
            tvNick.setError(getString(R.string.nick_name_connot_be_empty));
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            tvPassword.requestFocus();
            tvPassword.setError(getString(R.string.password_connot_be_empty));
            return false;
        }
        if (TextUtils.isEmpty(confirm)) {
            confirmPassword.requestFocus();
            confirmPassword.setError(getString(R.string.confirm_password_connot_be_empty));
            return false;
        }
        if (!password.equals(confirm)) {
            confirmPassword.requestFocus();
            confirmPassword.setError(getString(R.string.two_input_password));
            return false;
        }
        return true;
    }
}
