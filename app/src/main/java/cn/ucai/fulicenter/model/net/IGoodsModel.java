package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/3/15.
 */

public interface IGoodsModel {
    void loadData(Context context, int goodsId,
                  OkHttpUtils.OnCompleteListener<GoodsDetailsBean> listener);
    void loadCollectStatus(Context context, int goodsId, String username,
                           OnCompleteListener<MessageBean> listener);
}
