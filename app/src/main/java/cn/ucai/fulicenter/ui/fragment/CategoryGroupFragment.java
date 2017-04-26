package cn.ucai.fulicenter.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import cn.ucai.fulicenter.model.bean.CategoryGroupBean;
import cn.ucai.fulicenter.model.net.CategoryModel;
import cn.ucai.fulicenter.model.net.ICategoryModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ConvertUtils;

/**
 * Created by w on 2017/4/26.
 */

public class CategoryGroupFragment extends ListFragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        new WorkThread().start();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateGroupList(ArrayList<CategoryGroupBean> list) {
        if (list != null && list.size() > 0) {
            setListAdapter(new ArrayAdapter<CategoryGroupBean>(
                    getContext(),
                    android.R.layout.simple_expandable_list_item_1,
                    android.R.id.text1,
                    list
            ));
        }
    }

    class WorkThread extends Thread {
        ICategoryModel model = new CategoryModel();
        @Override
        public void run() {
            super.run();
            model.loadGroupData(getContext(), new OnCompleteListener<CategoryGroupBean[]>() {
                @Override
                public void onSuccess(CategoryGroupBean[] result) {
                    ArrayList<CategoryGroupBean> list = ConvertUtils.array2List(result);
                    EventBus.getDefault().post(list);
                }

                @Override
                public void onError(String error) {

                }
            });
        }
    }
}
