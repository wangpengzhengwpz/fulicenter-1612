package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/3/15.
 */

public interface ICategoryModel {
    void loadGroupData(Context context, OkHttpUtils
            .OnCompleteListener<CategoryGroupBean[]> listener);
    void loadChildData(Context context, int parentId, OkHttpUtils
            .OnCompleteListener<CategoryChildBean[]> listener);
}
