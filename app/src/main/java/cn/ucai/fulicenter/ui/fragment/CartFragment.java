package cn.ucai.fulicenter.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.model.bean.CartBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.CartModel;
import cn.ucai.fulicenter.model.net.ICartModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.ResultUtils;
import cn.ucai.fulicenter.ui.adapter.CartAdapter;
import cn.ucai.fulicenter.ui.view.SpaceItemDecoration;

/**
 * Created by Administrator on 2017/3/24.
 */

public class CartFragment extends Fragment {
    private static final String TAG = "CartFragment";
    ICartModel model;
    @BindView(R.id.tv_cart_sum_price)
    TextView tvCartSumPrice;
    @BindView(R.id.tv_cart_save_price)
    TextView tvCartSavePrice;
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.nothing)
    TextView nothing;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    User user;
    ArrayList<CartBean> cartList = new ArrayList<>();
    LinearLayoutManager gm;
    CartAdapter adapter;
    @BindView(R.id.layout_cart)
    RelativeLayout layoutCart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = new CartModel();
        initView();
        initData();
        setListener();
    }

    private void setListener() {
        setPullDownListener();
    }

    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRefresh(true);
                initData();
            }
        });
    }

    private void setCartListLayout(boolean isShow) {
        nothing.setVisibility(isShow ? View.GONE : View.VISIBLE);
        layoutCart.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void setRefresh(boolean refresh) {
        srl.setRefreshing(refresh);
        tvRefresh.setVisibility(refresh ? View.VISIBLE : View.GONE);
    }

    private void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow));
        gm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(gm);
        rv.setHasFixedSize(true);
        adapter = new CartAdapter(getContext(), cartList);
        rv.setAdapter(adapter);
        rv.addItemDecoration(new SpaceItemDecoration(12));
        setCartListLayout(false);
    }

    private void initData() {
        user = FuLiCenterApplication.getCurrentUser();
        if (user != null) {
            showCartList();
        }
    }

    private void showCartList() {
        model.loadData(getContext(), user.getMuserName(), new OnCompleteListener<CartBean[]>() {
            @Override
            public void onSuccess(CartBean[] result) {
                setRefresh(false);
                setCartListLayout(true);
                if (result != null) {
                    cartList.clear();
                    if (result.length > 0) {
                        ArrayList<CartBean> list = ResultUtils.array2List(result);
                        cartList.addAll(list);
                        adapter.notifyDataSetChanged();
                    } else {
                        setCartListLayout(false);
                    }
                }
            }

            @Override
            public void onError(String error) {
                setRefresh(false);
                Log.e(TAG, "onError,error=" + error);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
