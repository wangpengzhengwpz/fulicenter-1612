package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/3/15.
 */
public class CartAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<CartBean> mList;
    CompoundButton.OnCheckedChangeListener listener;
    View.OnClickListener updateListener;
    View.OnClickListener delListener;

    public CartAdapter(Context context, List<CartBean> list) {
        mContext = context;
        mList = list;
    }

    public void setListener(CompoundButton.OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    public void setUpdateListener(View.OnClickListener updateListener) {
        this.updateListener = updateListener;
    }

    public void setDelListener(View.OnClickListener delListener) {
        this.delListener = delListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = new CartViewHolder(View.inflate(mContext,
                R.layout.item_cart, null));
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CartViewHolder vh = (CartViewHolder) holder;
        final CartBean bean = mList.get(position);
        vh.bind(position);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    class CartViewHolder extends ViewHolder {
        @BindView(R.id.cb_cart_selected)
        CheckBox cbCartSelected;
        @BindView(R.id.iv_cart_thumb)
        ImageView ivCartThumb;
        @BindView(R.id.tv_cart_good_name)
        TextView tvCartGoodName;
        @BindView(R.id.iv_cart_add)
        ImageView ivCartAdd;
        @BindView(R.id.tv_cart_count)
        TextView tvCartCount;
        @BindView(R.id.iv_cart_del)
        ImageView ivCartDel;
        @BindView(R.id.tv_cart_price)
        TextView tvCartPrice;

        CartViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            CartBean bean = mList.get(position);
            tvCartCount.setText("(" + bean.getCount() + ")");
            cbCartSelected.setChecked(bean.isChecked());
            GoodsDetailsBean goods = bean.getGoods();
            if (goods != null) {
                ImageLoader.downloadImg(mContext, ivCartThumb, goods.getGoodsThumb());
                tvCartGoodName.setText(goods.getGoodsName());
                tvCartPrice.setText(goods.getCurrencyPrice());
            }
            cbCartSelected.setTag(position);
            cbCartSelected.setOnCheckedChangeListener(listener);
            ivCartAdd.setTag(position);
            ivCartAdd.setOnClickListener(updateListener);
            ivCartDel.setTag(position);
            ivCartDel.setOnClickListener(delListener);
        }
    }
}
