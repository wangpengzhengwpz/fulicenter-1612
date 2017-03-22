package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.dao.UserDao;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by Administrator on 2017/3/21.
 */
public class SettingsActivity extends AppCompatActivity {
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.iv_user_profile_avatar)
    ImageView ivUserProfileAvatar;
    @BindView(R.id.tv_user_profile_name)
    TextView tvUserProfileName;
    @BindView(R.id.tv_user_profile_nick)
    TextView tvUserProfileNick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        tvCommonTitle.setText(getString(R.string.user_profile));
        User user = FuLiCenterApplication.getCurrentUser();
        if (user != null) {
            showUserInfo(user);
        } else {
            backArea();
        }
    }

    private void showUserInfo(User user) {
        tvUserProfileName.setText(user.getMuserName());
        tvUserProfileNick.setText(user.getMuserNick());
        ImageLoader.downloadImg(SettingsActivity.this, ivUserProfileAvatar, user.getAvatar());
    }

    @OnClick(R.id.backClickArea)
    public void backArea() {
        MFGT.finish(SettingsActivity.this);
    }

    @OnClick(R.id.btn_logout)
    public void logout() {
        UserDao.getInstance(SettingsActivity.this).logout();
        finish();
        MFGT.gotoLogin(SettingsActivity.this, I.REQUEST_CODE_LOGIN);
    }
}
