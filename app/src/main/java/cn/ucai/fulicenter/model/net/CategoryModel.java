package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/3/16.
 */

public class CategoryModel implements ICategoryModel {
    @Override
    public void loadGroupData(Context context,
                              OkHttpUtils.OnCompleteListener<CategoryGroupBean[]> listener) {
        OkHttpUtils<CategoryGroupBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_GROUP)
                .targetClass(CategoryGroupBean[].class)
                .execute(listener);
    }

    @Override
    public void loadChildData(Context context, int parentId,
                              OkHttpUtils.OnCompleteListener<CategoryChildBean[]> listener) {
        OkHttpUtils<CategoryChildBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CATEGORY_CHILDREN)
                .addParam(I.CategoryChild.PARENT_ID,String.valueOf(parentId))
                .targetClass(CategoryChildBean[].class)
                .execute(listener);
    }
}
