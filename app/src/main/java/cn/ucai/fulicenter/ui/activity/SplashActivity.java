package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.dao.UserDao;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.SharePrefrenceUtils;
import cn.ucai.fulicenter.view.MFGT;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    int time = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String username = SharePrefrenceUtils.getInstance().getUserName();
                if (username != null) {
                    User user = UserDao.getInstance(SplashActivity.this).getUserInfo(username);
                    L.e(TAG, "user=" + user);
                    FuLiCenterApplication.setCurrentUser(user);
                }
                MFGT.gotoMain(SplashActivity.this);
                SplashActivity.this.finish();
            }
        }, time);
    }
}
