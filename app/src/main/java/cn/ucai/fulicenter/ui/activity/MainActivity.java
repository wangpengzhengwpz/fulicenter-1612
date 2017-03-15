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
import cn.ucai.fulicenter.ui.fragment.BoutiqueFragment;
import cn.ucai.fulicenter.ui.fragment.NewGoodsFragment;

public class MainActivity extends AppCompatActivity {

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
    Fragment[] fragments;
    NewGoodsFragment newGoodsFragment;
    BoutiqueFragment boutiqueFragment;
    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bind = ButterKnife.bind(this);
        initFragment();
        getSupportFragmentManager().beginTransaction()
                .add(new NewGoodsFragment(), "newGoods")
                .add(R.id.fragment_container, newGoodsFragment)
                .add(R.id.fragment_container, boutiqueFragment)
                .hide(boutiqueFragment)
                .show(newGoodsFragment)
                .commit();
    }

    private void initFragment() {
        fragments = new Fragment[2];
        newGoodsFragment = new NewGoodsFragment();
        boutiqueFragment = new BoutiqueFragment();
        fragments[0] = newGoodsFragment;
        fragments[1] = boutiqueFragment;
    }

    public void onCheckedChange(View view) {
        switch (view.getId()) {
            case R.id.layout_new_good:
                index = 0;
                break;
            case R.id.layout_boutique:
                index = 1;
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
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }
}
