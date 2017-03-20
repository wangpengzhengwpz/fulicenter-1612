package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.ui.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.ui.fragment.CategoryFragment;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;
import cn.ucai.fulicenter.view.MFGT;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.layout_new_good)
    RadioButton layoutNewGood;
    @BindView(R.id.layout_boutique)
    RadioButton layoutBoutique;
    @BindView(R.id.layout_category)
    RadioButton layoutCategory;
    @BindView(R.id.layout_cart)
    RadioButton layoutCart;
    @BindView(R.id.layout_personal_center)
    RadioButton layoutPersonalCenter;
    @BindView(R.id.main_bottom)
    RadioGroup mainBottom;
    @BindView(R.id.fragment_container)
    RelativeLayout fragmentContainer;
    Unbinder bind;
    int index = 0;
    int currentIndex = 0;
    Fragment[] fragments;
    NewGoodsFragment newGoodsFragment;
    BoutiqueFragment boutiqueFragment;
    CategoryFragment categoryFragment;
    RadioButton[] radioButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        initFragment();
        initRadioButton();
        getSupportFragmentManager().beginTransaction()
                .add(new NewGoodsFragment(), "newGoods")
                .add(R.id.fragment_container, newGoodsFragment)
                .add(R.id.fragment_container, boutiqueFragment)
                .add(R.id.fragment_container, categoryFragment)
                .hide(boutiqueFragment)
                .hide(categoryFragment)
                .show(newGoodsFragment)
                .commit();
    }

    private void initRadioButton() {
        radioButtons = new RadioButton[5];
        radioButtons[0] = layoutNewGood;
        radioButtons[1] = layoutBoutique;
        radioButtons[2] = layoutCategory;
        radioButtons[3] = layoutCart;
        radioButtons[4] = layoutPersonalCenter;
    }

    private void initFragment() {
        fragments = new Fragment[3];
        newGoodsFragment = new NewGoodsFragment();
        boutiqueFragment = new BoutiqueFragment();
        categoryFragment = new CategoryFragment();
        fragments[0] = newGoodsFragment;
        fragments[1] = boutiqueFragment;
        fragments[2] = categoryFragment;
    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.layout_new_good:
                index = 0;
                break;
            case R.id.layout_boutique:
                index = 1;
                break;
            case R.id.layout_category:
                index = 2;
                break;
            case R.id.layout_cart:
                if (FuLiCenterApplication.getCurrentUser() == null) {
                    MFGT.gotoLogin(MainActivity.this);
                } else {
                    index = 3;
                }
                break;
            case R.id.layout_personal_center:
                if (FuLiCenterApplication.getCurrentUser() == null) {
                    MFGT.gotoLogin(MainActivity.this);
                } else {
                    index = 4;
                }
                break;
        }
        setFragment();
    }

    private void setFragment() {
        if (currentIndex != index) {
            getSupportFragmentManager().beginTransaction()
                    .hide(fragments[currentIndex])
                    .show(fragments[index])
                    .commit();
            currentIndex = index;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        L.e(TAG, "index=" + index + ",currentIndex=" + currentIndex);
        setRadioButton();
    }

    private void setRadioButton() {
        for (int i=0;i<radioButtons.length;i++) {
            if (i == currentIndex) {
                radioButtons[i].setChecked(true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }
}
