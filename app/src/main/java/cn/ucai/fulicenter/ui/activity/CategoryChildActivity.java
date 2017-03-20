package cn.ucai.fulicenter.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.view.CatFilterCategoryButton;
import cn.ucai.fulicenter.view.MFGT;

/**
 * Created by Administrator on 2017/3/17.
 */
public class CategoryChildActivity extends AppCompatActivity {
    boolean sortPrice;
    boolean sortAddTime;
    int sortBy = I.SORT_BY_ADDTIME_DESC;
    NewGoodsFragment newGoodsFragment;
    @BindView(R.id.btn_sort_price)
    Button btnSortPrice;
    @BindView(R.id.btn_sort_addtime)
    Button btnSortAddtime;
    String groupName;
    @BindView(R.id.cfcd_filter)
    CatFilterCategoryButton cfcdFilter;
    List<CategoryChildBean> mList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_child);
        ButterKnife.bind(this);
        newGoodsFragment = new NewGoodsFragment();
        //接受小类id
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, newGoodsFragment)
                .commit();
        //接受大类名称，当前大类的小类集合
        groupName = getIntent().getStringExtra(I.CategoryGroup.NAME);
        mList = (List<CategoryChildBean>) getIntent().getSerializableExtra(I.CategoryChild.DATA);
        initView();
    }

    private void initView() {
        cfcdFilter.initView(groupName, mList);
    }

    @OnClick({R.id.btn_sort_price, R.id.btn_sort_addtime})
    public void sortList(View view) {
        Drawable end = null;
        switch (view.getId()) {
            case R.id.btn_sort_price:
                sortBy = sortPrice ? I.SORT_BY_ADDTIME_ASC : I.SORT_BY_ADDTIME_DESC;
                sortPrice = !sortPrice;
                end = getResources().getDrawable(sortPrice ?
                        R.drawable.arrow_order_up : R.drawable.arrow_order_down);
                btnSortPrice.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
                break;
            case R.id.btn_sort_addtime:
                sortBy = sortAddTime ? I.SORT_BY_ADDTIME_ASC : I.SORT_BY_ADDTIME_DESC;
                sortAddTime = !sortAddTime;
                end = getResources().getDrawable(sortAddTime ?
                        R.drawable.arrow_order_up : R.drawable.arrow_order_down);
                btnSortAddtime.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
                break;
        }
        newGoodsFragment.sortBy(sortBy);
    }

    @OnClick(R.id.backClickArea)
    public void backonClick() {
        MFGT.finish(CategoryChildActivity.this);
    }
}
