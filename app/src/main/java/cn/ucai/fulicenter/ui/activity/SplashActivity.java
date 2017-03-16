package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.view.MFGT;

public class SplashActivity extends AppCompatActivity {
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
                MFGT.gotoMain(SplashActivity.this);
                SplashActivity.this.finish();
            }
        }, time);
    }
}
