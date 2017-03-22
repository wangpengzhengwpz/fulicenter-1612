package cn.ucai.fulicenter.model.net;

import android.content.Context;

import java.io.File;

import cn.ucai.fulicenter.model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/3/15.
 */

public interface IUserModel {
    void login(Context context, String username, String password,
               OkHttpUtils.OnCompleteListener<String> listener);
    void register(Context context, String username, String nickname, String password,
                  OkHttpUtils.OnCompleteListener<String> listener);
    void updateNick(Context context, String username, String newnick,
                    OnCompleteListener<String> listener);
    void uploadAvatar(Context context, String username, File file, OnCompleteListener<String> listener);
}
