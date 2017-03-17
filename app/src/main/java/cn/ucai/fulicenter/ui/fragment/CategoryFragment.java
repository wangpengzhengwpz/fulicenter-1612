package cn.ucai.fulicenter.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.CategoryModel;
import cn.ucai.fulicenter.model.net.ICategoryModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.adapter.CategoryAdapter;

/**
 * Created by Administrator on 2017/3/16.
 */

public class CategoryFragment extends Fragment {
    private static final String TAG = CategoryFragment.class.getSimpleName();
    ICategoryModel model;
    @BindView(R.id.elv_category)
    ExpandableListView elvCategory;
    List<CategoryGroupBean> groupList = new ArrayList<>();
    List<List<CategoryChildBean>> childList = new ArrayList<>();
    CategoryAdapter adapter;
    int loadIndex = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    model = new CategoryModel();
    adapter = new CategoryAdapter(getContext());
    elvCategory.setAdapter(adapter);
    loadGroupData();
        elvCategory.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return false;
            }
        });
}

    private void loadGroupData() {
        model.loadGroupData(getContext(), new OnCompleteListener<CategoryGroupBean[]>() {
            @Override
            public void onSuccess(CategoryGroupBean[] result) {
                if (result != null) {
                    ArrayList<CategoryGroupBean> list = ResultUtils.array2List(result);
                    groupList.clear();
                    groupList.addAll(list);
                    for (int i=0;i<list.size();i++) {
                        laodChildData(list.get(i).getId());

                    }
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void laodChildData(int parentId) {
        model.loadChildData(getContext(), parentId, new OnCompleteListener<CategoryChildBean[]>() {
            @Override
            public void onSuccess(CategoryChildBean[] result) {
                loadIndex++;
                if (result != null) {
                    ArrayList<CategoryChildBean> list = ResultUtils.array2List(result);
                    childList.add(list);
                }
                if (loadIndex == groupList.size()) {
                    adapter.initData(groupList, childList);
                    L.e(TAG, "load add data...");
                }
            }

            @Override
            public void onError(String error) {
                loadIndex++;
                L.e(TAG, "onError,laodChildData" + error);
                if (loadIndex == groupList.size()) {
                    adapter.initData(groupList, childList);
                    L.e(TAG, "onError,load add data...");
                }
            }
        });
    }
}
