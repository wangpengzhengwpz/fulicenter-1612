package cn.ucai.fulicenter.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.net.CategoryModel;
import cn.ucai.fulicenter.model.net.ICategoryModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.ImageLoader;

/**
 * Created by w on 2017/4/26.
 */

public class CategoryChildFragment extends Fragment {
    int parentId = 344;
    ArrayList<CategoryChildBean> childList;
    @BindView(R.id.rv_category_child)
    RecyclerView rvCategoryChild;
    CategoryChildAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category_child, container, false);
        ButterKnife.bind(this, view);
        GridLayoutManager gm = new GridLayoutManager(getContext(), I.COLUM_NUM);
        rvCategoryChild.setLayoutManager(gm);
        rvCategoryChild.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new WorkThread().start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateGroupList(ArrayList<CategoryChildBean> list) {
        if (list != null && list.size() > 0) {
            childList = list;
            if (adapter == null) {
                adapter = new CategoryChildAdapter();
                rvCategoryChild.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    class WorkThread extends Thread {
        ICategoryModel model = new CategoryModel();

        @Override
        public void run() {
            super.run();
            model.loadChildData(getContext(), parentId, new OnCompleteListener<CategoryChildBean[]>() {
                @Override
                public void onSuccess(CategoryChildBean[] result) {
                    ArrayList<CategoryChildBean> list = ConvertUtils.array2List(result);
                    EventBus.getDefault().post(list);
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }

    class CategoryChildAdapter extends RecyclerView.Adapter<CategoryChildAdapter.ChildViewHolder> {

        @Override
        public CategoryChildAdapter.ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View.inflate(getContext(), R.layout.adapter_child, null);
            return null;
        }

        @Override
        public void onBindViewHolder(CategoryChildAdapter.ChildViewHolder holder, int position) {
            holder.tvChild.setText(childList.get(position).getName());
            ImageLoader.downloadImg(getContext(), holder.ivChild, childList.get(position).getImageUrl());
        }

        @Override
        public int getItemCount() {
            return childList != null ? childList.size() : 0;
        }

        class ChildViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_child)
            ImageView ivChild;
            @BindView(R.id.tv_child)
            TextView tvChild;

            ChildViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }
    }
}
