package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.ui.activity.CategoryChildActivity;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by Administrator on 2017/3/20.
 */

public class CatFilterAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<CategoryChildBean> mList;
    String groupName;

    public CatFilterAdapter(Context context, ArrayList<CategoryChildBean> list, String name) {
        this.mContext = context;
        this.mList = list;
        groupName = name;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public CategoryChildBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        CatFilterViewHolder vh = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_cat_filter, null);
            vh = new CatFilterViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (CatFilterViewHolder) view.getTag();
        }
        vh.bind(position);
        return view;
    }

    class CatFilterViewHolder {
        @BindView(R.id.ivCategoryChildThumb)
        ImageView ivCategoryChildThumb;
        @BindView(R.id.tvCategoryChildName)
        TextView tvCategoryChildName;
        @BindView(R.id.layout_category_child)
        RelativeLayout layoutCategoryChild;

        CatFilterViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(int position) {
            final CategoryChildBean bean = mList.get(position);
            tvCategoryChildName.setText(bean.getName());
            ImageLoader.downloadImg(mContext, ivCategoryChildThumb, bean.getImageUrl());
            layoutCategoryChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MFGT.gotoCategoryChild(mContext, bean.getId(), groupName, mList);
                    ((CategoryChildActivity)mContext).finish();
                }
            });
        }
    }
}
