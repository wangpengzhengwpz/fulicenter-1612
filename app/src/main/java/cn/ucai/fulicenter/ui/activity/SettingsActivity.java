package cn.ucai.fulicenter.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.Result;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.dao.UserDao;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.OnSetAvatarListener;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/21.
 */
public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = SettingsActivity.class.getSimpleName();
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.iv_user_profile_avatar)
    ImageView ivUserProfileAvatar;
    @BindView(R.id.tv_user_profile_name)
    TextView tvUserProfileName;
    @BindView(R.id.tv_user_profile_nick)
    TextView tvUserProfileNick;
    OnSetAvatarListener onSetAvatarListener;
    String avatarName;
    User user;
    Bundle bundle;
    ProgressDialog pd;
    IUserModel model;
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        initData();
        bundle = savedInstanceState;
    }

    private void initData() {
        tvCommonTitle.setText(getString(R.string.user_profile));
        user = FuLiCenterApplication.getCurrentUser();
        if (user != null) {
            showUserInfo(user);
        } else {
            backArea();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void showUserInfo(User user) {
        tvUserProfileName.setText(user.getMuserName());
        tvUserProfileNick.setText(user.getMuserNick());
        ImageLoader.setAvatar(ImageLoader.getAvatarUrl(user), SettingsActivity.this, ivUserProfileAvatar);
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

    @OnClick(R.id.layout_user_profile_username)
    public void usernameOnClick() {
        CommonUtils.showShortToast(R.string.username_connot_be_modify);
    }

    @OnClick(R.id.layout_user_profile_nickname)
    public void updateNick() {
        MFGT.gotoUpdateNick(SettingsActivity.this);
    }

    @OnClick(R.id.layout_user_profile_avatar)
    public void avatarOnClick() {
        onSetAvatarListener = new OnSetAvatarListener(SettingsActivity.this,
                R.id.layout_user_profile_avatar, getAvatarName(), I.AVATAR_TYPE_USER_PATH);
    }

    private String getAvatarName() {
        avatarName = user.getMuserName() + System.currentTimeMillis();
        L.e(TAG, "avatarname=" + avatarName);
        return avatarName;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            L.e(TAG, "requestCode=" + requestCode + ",data=" + data);
            onSetAvatarListener.setAvatar(requestCode, data, ivUserProfileAvatar);
            if (requestCode == OnSetAvatarListener.REQUEST_CROP_PHOTO) {
                uploadAvatar();
            }
        }
    }

    private void showDialog() {
        pd = new ProgressDialog(SettingsActivity.this);
        pd.setMessage(getString(R.string.update_user_avatar));
        pd.show();
    }

    private void updateSuccess(final User u) {
        L.e(TAG, "updateSuccess,user=" + user);
        CommonUtils.showShortToast(R.string.update_user_avatar_success);
        FuLiCenterApplication.setCurrentUser(u);
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean b = UserDao.getInstance(SettingsActivity.this).saveUserInfo(u);
                L.e(TAG, "updateSuccess,b=" + b);
            }
        }).start();
        initData();
    }

    private void uploadAvatar() {
        File file = new File(OnSetAvatarListener.getAvatarPath(SettingsActivity.this, I.AVATAR_TYPE
                + "/" + user.getMuserName() + I.AVATAR_SUFFIX_JPG));
        L.e("file="+file.exists());
        L.e("file="+file.getAbsolutePath());
        showDialog();
        model = new UserModel();
        model.uploadAvatar(SettingsActivity.this, user.getMuserName(), file,
                new OnCompleteListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Result result = ResultUtils.getResultFromJson(s, User.class);
                        if (result != null) {
                            if (result.isRetMsg()) {
                                User u = (User) result.getRetData();
                                updateSuccess(u);
                            } else {
                                if (result.isRetMsg()) {
                                    User u = (User) result.getRetData();
                                    updateSuccess(u);
                                } else {
                                    if (result.getRetCode() == I.MSG_UPLOAD_AVATAR_FAIL) {
                                        CommonUtils.showShortToast(R.string.update_user_avatar_fail);
                                    }
                                }
                            }
                        }
                        pd.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        pd.dismiss();
                        CommonUtils.showShortToast(R.string.update_user_avatar_fail);
                    }
                });
    }
}
