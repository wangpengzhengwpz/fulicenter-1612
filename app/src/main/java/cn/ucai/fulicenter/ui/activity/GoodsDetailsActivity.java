package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.AlbumsBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.GoodsModel;
import cn.ucai.fulicenter.model.net.IGoodsModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;
import cn.ucai.fulicenter.ui.view.FlowIndicator;
import cn.ucai.fulicenter.ui.view.MFGT;
import cn.ucai.fulicenter.ui.view.SlideAutoLoopView;

/**
 * Created by Administrator on 2017/3/17.
 */
public class GoodsDetailsActivity extends AppCompatActivity {
    IGoodsModel model;
    int goodsId = 0;
    @BindView(R.id.tv_good_name_english)
    TextView tvGoodNameEnglish;
    @BindView(R.id.tv_good_name)
    TextView tvGoodName;
    @BindView(R.id.tv_good_price_shop)
    TextView tvGoodPriceShop;
    @BindView(R.id.tv_good_price_current)
    TextView tvGoodPriceCurrent;
    @BindView(R.id.salv)
    SlideAutoLoopView salv;
    @BindView(R.id.indicator)
    FlowIndicator indicator;
    @BindView(R.id.wv_good_brief)
    WebView wvGoodBrief;
    GoodsDetailsBean bean;
    @BindView(R.id.iv_good_collect)
    ImageView ivGoodCollect;

    @Override
    protected void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_datails);
        ButterKnife.bind(this);
        goodsId = getIntent().getIntExtra(I.Goods.KEY_GOODS_ID, 0);
        if (goodsId == 0) {
            MFGT.finish(GoodsDetailsActivity.this);
            return;
        }
        model = new GoodsModel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        if (bean == null) {
            model.loadData(GoodsDetailsActivity.this, goodsId, new OkHttpUtils
                    .OnCompleteListener<GoodsDetailsBean>() {
                @Override
                public void onSuccess(GoodsDetailsBean result) {
                    if (result != null) {
                        bean = result;
                        showDetails();
                    }
                }

                @Override
                public void onError(String error) {
                    CommonUtils.showShortToast(error);
                }
            });
        }
        loadCollectStatus();
    }

    private void loadCollectStatus() {
        User user = FuLiCenterApplication.getCurrentUser();
        if (user != null) {
            model.loadCollectStatus(GoodsDetailsActivity.this, goodsId, user.getMuserName(),
                    new OnCompleteListener<MessageBean>() {
                        @Override
                        public void onSuccess(MessageBean msg) {
                            if (msg != null && msg.isSuccess()) {
                                setCollectStatus(true);
                            } else {
                                setCollectStatus(false);
                            }
                        }

                        @Override
                        public void onError(String error) {
                            setCollectStatus(false);
                        }
                    });
        }
    }

    private void setCollectStatus(boolean isCollects) {
        ivGoodCollect.setImageResource(isCollects ?
                R.mipmap.bg_collect_out : R.mipmap.bg_collect_in);
    }

    private void showDetails() {
        tvGoodNameEnglish.setText(bean.getGoodsEnglishName());
        tvGoodName.setText(bean.getGoodsName());
        tvGoodPriceShop.setText(bean.getShopPrice());
        tvGoodPriceCurrent.setText(bean.getCurrencyPrice());
        salv.startPlayLoop(indicator, getAblumUrl(), getAblumCount());
        wvGoodBrief.loadDataWithBaseURL(null, bean.getGoodsBrief(), I.TEXT_HTML, I.UTF_8, null);

    }

    private int getAblumCount() {
        if (bean.getProperties() != null && bean.getProperties().length > 0) {
            return bean.getProperties()[0].getAlbums().length;
        }
        return 0;
    }

    private String[] getAblumUrl() {
        if (bean.getProperties() != null && bean.getProperties().length > 0) {
            AlbumsBean[] albums = bean.getProperties()[0].getAlbums();
            if (albums != null && albums.length > 0) {
                String[] urls = new String[albums.length];
                for (int i = 0; i < albums.length; i++) {
                    urls[i] = albums[0].getImgUrl();
                }
                return urls;
            }
        }
        return null;
    }

    @OnClick(R.id.backClickArea)
    public void onClick() {
        MFGT.finish(GoodsDetailsActivity.this);
    }
}
