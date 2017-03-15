package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.NewGoodsBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/3/15.
 */
public class GoodsAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<NewGoodsBean> mList;
    boolean isMore;

    public GoodsAdapter(Context context, List<NewGoodsBean> list) {
        mContext = context;
        mList = list;
        isMore = true;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = null;
        if (viewType == I.TYPE_FOOTER) {
            vh = new FooterViewHolder(View.inflate(mContext, R.layout.item_footer , null));
        } else {
            vh = new GoodsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_goods, parent, false));
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder vh = (FooterViewHolder) holder;
            vh.tvFooter.setText(getFooterString());
            return;
        }
        GoodsViewHolder vh = (GoodsViewHolder) holder;
        NewGoodsBean bean = mList.get(position);
        vh.tvGoodsName.setText(bean.getGoodsName());
        vh.tvGoodsPrice.setText(bean.getCurrencyPrice());
        ImageLoader.downloadImg(mContext, vh.ivGoodsThumb, bean.getGoodsThumb());
    }

    public int getFooterString() {
        return isMore ? R.string.load_more : R.string.no_more;
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() + 1 : 1;
    }

    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return I.TYPE_FOOTER;
        }
        return I.TYPE_ITEM;
    }

    class GoodsViewHolder extends RecyclerView.ViewHolder {
        ImageView ivGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;
        @BindView(R.id.layout_goods)
        LinearLayout layoutGoods;

        GoodsViewHolder(View view) {
            super(view);
            ivGoodsThumb = (ImageView) view.findViewById(R.id.ivGoodsThumb);
            ButterKnife.bind(this, view);
        }
    }

    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView tvFooter;

        FooterViewHolder(View view) {
            super(view);
            tvFooter = (TextView) view.findViewById(R.id.tvFooter);
            ButterKnife.bind(this, view);
        }
    }
}
