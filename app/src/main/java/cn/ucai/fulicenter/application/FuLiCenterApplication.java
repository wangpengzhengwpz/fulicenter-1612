package cn.ucai.fulicenter.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/3/14.
 */

public class FuLiCenterApplication extends Application {
    static FuLiCenterApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getInstance() {
        return instance;
    }
}
