package cn.ucai.fulicenter.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.utils.ImageLoader;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.ui.fragment.CategoryFragment;

/**
 * Created by Administrator on 2017/3/16.
 */

public class CategoryAdapter extends BaseExpandableListAdapter {
    private static final String TAG = CategoryFragment.class.getSimpleName();
    Context mContext;
    List<CategoryGroupBean> groupList;
    List<List<CategoryChildBean>> childList;

    public CategoryAdapter(Context mContext) {
        this.mContext = mContext;
        this.groupList = new ArrayList<>();
        this.childList = new ArrayList<>();
    }

    @Override
    public int getGroupCount() {
        return groupList != null ? groupList.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList != null && childList.get(groupPosition) != null ?
                childList.get(groupPosition).size() : 0;
    }

    @Override
    public CategoryGroupBean getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public CategoryChildBean getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        CategoryGroupViewHolder vh = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_category_group, null);
            vh = new CategoryGroupViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (CategoryGroupViewHolder) view.getTag();
        }
        CategoryGroupBean group = getGroup(groupPosition);
        L.e(TAG, "group=" + group);
        vh.tvGroupName.setText(group.getName());
        ImageLoader.downloadImg(mContext, vh.ivGroupThumb, group.getImageUrl());
        vh.ivIndicator.setImageResource(isExpanded ? R.mipmap.expand_off : R.mipmap.expand_on);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view,
                             ViewGroup parent) {
        CategoryChildViewHolder vh = null;
        if (view == null) {
            view = View.inflate(mContext, R.layout.item_category_child, null);
            vh = new CategoryChildViewHolder(view);
            view.setTag(vh);
        } else {
            vh = (CategoryChildViewHolder) view.getTag();
        }
        CategoryChildBean child = getChild(groupPosition, childPosition);
        L.e(TAG, "child=" + child);
        if (child != null) {
            vh.tvGroupChildName.setText(child.getName());
            ImageLoader.downloadImg(mContext, vh.ivCategoryChildThumb, child.getImageUrl());
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void initData(List<CategoryGroupBean> groupList,
                         List<List<CategoryChildBean>> childList) {
        L.e(TAG, "initData,groupList=" + groupList.size() + ",childList=" + childList.size());
        this.groupList.addAll(groupList);
        this.childList.addAll(childList);
        L.e(TAG, "initData,groupList=" + this.groupList.size() + ",childList=" +
                this.childList.size());
        notifyDataSetChanged();
    }

    class CategoryGroupViewHolder {
        @BindView(R.id.iv_group_thumb)
        ImageView ivGroupThumb;
        @BindView(R.id.tv_group_name)
        TextView tvGroupName;
        @BindView(R.id.iv_indicator)
        ImageView ivIndicator;

        CategoryGroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class CategoryChildViewHolder {
        @BindView(R.id.iv_category_child_thumb)
        ImageView ivCategoryChildThumb;
        @BindView(R.id.tv_group_child_name)
        TextView tvGroupChildName;
        @BindView(R.id.layout_category_child)
        RelativeLayout layoutCategoryChild;

        CategoryChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
