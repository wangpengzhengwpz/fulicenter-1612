package cn.ucai.fulicenter.application;

import android.app.Application;
import android.content.Context;

import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.dao.UserDao;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.SharePrefrenceUtils;

/**
 * Created by Administrator on 2017/3/14.
 */

public class FuLiCenterApplication extends Application {
    static FuLiCenterApplication instance;
    static User currentUser;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getInstance() {
        return instance;
    }

    public static User getCurrentUser() {
        if (currentUser == null) {
            final String username = SharePrefrenceUtils.getInstance().getUserName();
            L.e("application", "username=" + username);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    currentUser = UserDao.getInstance(instance).getUserInfo(username);
                }
            }).start();
        }
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        FuLiCenterApplication.currentUser = currentUser;
    }
}
