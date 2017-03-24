package cn.ucai.fulicenter.model.net;

import android.content.Context;

import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;

/**
 * Created by Administrator on 2017/3/24.
 */

public class CartModel implements ICartModel {

    @Override
    public void loadData(Context context, String username, OnCompleteListener<CartBean[]> listener) {
        OkHttpUtils<CartBean[]> utils = new OkHttpUtils<>(context);
        utils.setRequestUrl(I.REQUEST_FIND_CARTS)
                .addParam(I.Cart.USER_NAME,username)
                .targetClass(CartBean[].class)
                .execute(listener);
    }

    @Override
    public void cartAction(Context context, int action, String cartId, String goodsId, String username, int count, OnCompleteListener<MessageBean> listener) {
        OkHttpUtils<MessageBean> utils = new OkHttpUtils<>(context);
        if (action==I.ACTION_CART_ADD){
            addCart(utils,username,goodsId,listener);
        }
        if (action==I.ACTION_CART_DEL){
            delCart(utils,cartId,listener);
        }
        if (action==I.ACTION_CART_UPDATA){
            updateCart(utils,cartId,count,listener);
        }
    }

    private void updateCart(OkHttpUtils<MessageBean> utils, String cartId, int count,
                            OnCompleteListener<MessageBean> listener) {
        utils.setRequestUrl(I.REQUEST_UPDATE_CART)
                .addParam(I.Cart.ID,cartId)
                .addParam(I.Cart.COUNT,String.valueOf(count))
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    private void delCart(OkHttpUtils<MessageBean> utils, String cartId,
                         OnCompleteListener<MessageBean> listener) {
        utils.setRequestUrl(I.REQUEST_DELETE_CART)
                .addParam(I.Cart.ID,cartId)
                .targetClass(MessageBean.class)
                .execute(listener);
    }

    private void addCart(OkHttpUtils<MessageBean> utils, String username, String goodsId,
                         OnCompleteListener<MessageBean> listener) {
        utils.setRequestUrl(I.REQUEST_ADD_CART)
                .addParam(I.Cart.USER_NAME,username)
                .addParam(I.Cart.GOODS_ID,goodsId)
                .addParam(I.Cart.COUNT,String.valueOf(1))
                .addParam(I.Cart.IS_CHECKED,String.valueOf(0))
                .targetClass(MessageBean.class)
                .execute(listener);
    }
}
