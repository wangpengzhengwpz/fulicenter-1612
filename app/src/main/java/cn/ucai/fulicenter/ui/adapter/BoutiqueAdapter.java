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
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by Administrator on 2017/3/15.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter {
    Context mContext;
    List<BoutiqueBean> mList;

    public BoutiqueAdapter(Context context, List<BoutiqueBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh = new BoutiqueViewHolder(View.inflate(mContext, R.layout.item_boutique, null));
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BoutiqueViewHolder vh = (BoutiqueViewHolder) holder;
        final BoutiqueBean bean = mList.get(position);
        ImageLoader.downloadImg(mContext, vh.ivBoutiqueImg, bean.getImageurl());
        vh.tvBoutiqueTitle.setText(bean.getTitle());
        vh.tvBoutiqueName.setText(bean.getName());
        vh.tvBoutiqueDescription.setText(bean.getDescription());
        bean.getId();
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MFGT.gotoBoutiqueChild(mContext, bean);
            }
        });
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
