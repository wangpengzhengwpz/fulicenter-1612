package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.CollectBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.IUserModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.net.UserModel;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.ConvertUtils;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.ui.adapter.CollectsAdapter;
import cn.ucai.fulicenter.ui.view.SpaceItemDecoration;

/**
 * Created by Administrator on 2017/3/23.
 */
public class CollectsActivity extends AppCompatActivity {
    private static final String TAG = CollectsActivity.class.getSimpleName();
    @BindView(R.id.tv_refresh)
    TextView tvRefresh;
    @BindView(R.id.rv_goods)
    RecyclerView rvGoods;
    @BindView(R.id.srl)
    SwipeRefreshLayout srl;
    @BindView(R.id.tv_nomore)
    TextView tvNomore;
    IUserModel model;
    int pageId = 1;
    GridLayoutManager gm;
    List<CollectBean> mList = new ArrayList<>();
    Unbinder bind;
    CollectsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_new_goods);
        bind = ButterKnife.bind(this);
        model = new UserModel();
        initView();
        initData(I.ACTION_DOWNLOAD);
        setListener();
    }

    private void initView() {
        srl.setColorSchemeColors(
                getResources().getColor(R.color.google_green),
                getResources().getColor(R.color.google_blue),
                getResources().getColor(R.color.google_red),
                getResources().getColor(R.color.google_yellow));
        gm = new GridLayoutManager(CollectsActivity.this, I.COLUM_NUM);
        gm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == mList.size()) {
                    return I.COLUM_NUM;
                }
                return 1;
            }
        });
        rvGoods.setLayoutManager(gm);
        rvGoods.setHasFixedSize(true);
        adapter = new CollectsAdapter(CollectsActivity.this, mList);
        rvGoods.setAdapter(adapter);
        rvGoods.addItemDecoration(new SpaceItemDecoration(12));

    }

    private void initData(final int action) {
        User user = FuLiCenterApplication.getCurrentUser();
        if (user == null) {
            finish();
            return;
        }
        model.loadCollects(CollectsActivity.this, user.getMuserName(), pageId, I.PAGE_SIZE_DEFAULT,
                new OnCompleteListener<CollectBean[]>() {
                    @Override
                    public void onSuccess(CollectBean[] result) {
                        setRefresh(false);
                        adapter.setMore(true);
                        L.e(TAG, "initData,result = " + result);
                        if (result != null && result.length > 0) {
                            ArrayList<CollectBean> list = ConvertUtils.array2List(result);
                            if (action == I.ACTION_DOWNLOAD || action == I.ACTION_PULL_DOWN) {
                                mList.clear();
                            }
                            mList.addAll(list);
                            if (list.size() < I.PAGE_SIZE_DEFAULT) {
                                adapter.setMore(false);
                            }
                            adapter.notifyDataSetChanged();
                        } else if (pageId == 1 && result != null && result.length == 0) {
                            mList.clear();
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(String error) {
                        L.e(TAG, "initData,error = " + error);
                        CommonUtils.showShortToast(error);
                        setRefresh(false);
                    }
                });
    }

    private void setListener() {
        setPullDownListener();
        setPullUpListener();
    }

    private void setRefresh(boolean refresh) {
        if (srl != null) {
            srl.setRefreshing(refresh);
        }
        if (tvRefresh != null) {
            tvRefresh.setVisibility(refresh ? View.VISIBLE : View.GONE);
        }
    }

    private void setPullDownListener() {
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setRefresh(true);
                pageId = 1;
                initData(I.ACTION_PULL_DOWN);
            }
        });
    }

    private void setPullUpListener() {
        rvGoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastPosition = gm.findLastVisibleItemPosition();
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastPosition == adapter.getItemCount() - 1
                        && adapter.isMore()) {
                    pageId++;
                    initData(I.ACTION_PULL_UP);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstPosition = gm.findFirstVisibleItemPosition();
                srl.setEnabled(firstPosition == 0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.e(TAG, "onActivityResult,requestCode" + requestCode + ",resultCode" + resultCode +
                ",data" + data);
        if (resultCode == RESULT_OK && requestCode == I.REQUEST_CODE_COLLECT) {
            boolean isCollected = data.getBooleanExtra(I.GoodsDetails.KEY_IS_COLLECTED, true);
            int goodsId = data.getIntExtra(I.GoodsDetails.KEY_GOODS_ID, 0);
            L.e(TAG, "onActivityResult,isCollected=" + isCollected + ",goodsId=" + goodsId);
            if (!isCollected) {
                mList.remove(new CollectBean(goodsId));
                adapter.notifyDataSetChanged();
            }
        }
    }
}
