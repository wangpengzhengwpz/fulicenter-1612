package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.BoutiqueBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by Administrator on 2017/3/15.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<BoutiqueBean> mList;
    boolean isMore;

    public BoutiqueAdapter(Context context, List<BoutiqueBean> list) {
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
            vh = new FooterViewHolder(View.inflate(mContext, R.layout.item_footer, null));
        } else {
            vh = new BoutiqueViewHolder(View.inflate(mContext, R.layout.item_boutique, null));
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
        BoutiqueViewHolder vh = (BoutiqueViewHolder) holder;
        BoutiqueBean bean = mList.get(position);
        ImageLoader.downloadImg(mContext, vh.ivBoutiqueImg, bean.getImageurl());
        vh.tvBoutiqueTitle.setText(bean.getTitle());
        vh.tvBoutiqueName.setText(bean.getName());
        vh.tvBoutiqueDescription.setText(bean.getDescription());
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

    class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView tvFooter;

        FooterViewHolder(View view) {
            super(view);
            tvFooter = (TextView) view.findViewById(R.id.tvFooter);
            ButterKnife.bind(this, view);
        }
    }

    class BoutiqueViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivBoutiqueImg)
        ImageView ivBoutiqueImg;
        @BindView(R.id.tvBoutiqueTitle)
        TextView tvBoutiqueTitle;
        @BindView(R.id.tvBoutiqueName)
        TextView tvBoutiqueName;
        @BindView(R.id.tvBoutiqueDescription)
        TextView tvBoutiqueDescription;
        @BindView(R.id.layout_boutique_item)
        RelativeLayout layoutBoutiqueItem;

        BoutiqueViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
