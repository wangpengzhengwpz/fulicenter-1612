package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.view.FooterViewHolder;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/15.
 */
public class CollectsAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<CollectBean> mList;
    boolean isMore;

    public CollectsAdapter(Context context, List<CollectBean> list) {
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = null;
        if (viewType == I.TYPE_FOOTER) {
            vh = new FooterViewHolder(View.inflate(mContext, R.layout.item_footer, null));
        } else {
            vh = new GoodsViewHolder(View.inflate(mContext, R.layout.item_colects, null));
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == I.TYPE_FOOTER) {
            FooterViewHolder vh = (FooterViewHolder) holder;
            vh.setFooter(getFooterString());
            return;
        }
        GoodsViewHolder vh = (GoodsViewHolder) holder;
        vh.bind(position);
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

    @OnClick({R.id.iv_collect_del, R.id.layout_goods})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_collect_del:
                break;
            case R.id.layout_goods:
                break;
        }
    }

    class GoodsViewHolder extends ViewHolder {
        @BindView(R.id.ivGoodsThumb)
        ImageView ivGoodsThumb;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.iv_collect_del)
        ImageView ivCollectDel;
        @BindView(R.id.layout_goods)
        RelativeLayout layoutGoods;

        GoodsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            final CollectBean bean = mList.get(position);
            tvGoodsName.setText(bean.getGoodsName());
            ImageLoader.downloadImg(mContext, ivGoodsThumb, bean.getGoodsThumb());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoDetails(mContext, bean.getGoodsId());
                }
            });
        }
    }
}
