package cn.ucai.fulicenter.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.FuLiCenterApplication;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.model.bean.AlbumsBean;
import cn.ucai.fulicenter.model.bean.GoodsDetailsBean;
import cn.ucai.fulicenter.model.bean.MessageBean;
import cn.ucai.fulicenter.model.bean.User;
import cn.ucai.fulicenter.model.net.CartModel;
import cn.ucai.fulicenter.model.net.GoodsModel;
import cn.ucai.fulicenter.model.net.ICartModel;
import cn.ucai.fulicenter.model.net.IGoodsModel;
import cn.ucai.fulicenter.model.net.OnCompleteListener;
import cn.ucai.fulicenter.model.utils.AntiShake;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.model.utils.L;
import cn.ucai.fulicenter.model.utils.OkHttpUtils;
import cn.ucai.fulicenter.ui.view.FlowIndicator;
import cn.ucai.fulicenter.ui.view.MFGT;
import cn.ucai.fulicenter.ui.view.SlideAutoLoopView;

/**
 * Created by Administrator on 2017/3/17.
 */
public class GoodsDetailsActivity extends AppCompatActivity {
    private static final String TAG = GoodsDetailsActivity.class.getSimpleName();
    IGoodsModel model;
    ICartModel cartModel;
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
    boolean isCollects = false;
    AntiShake util = new AntiShake();

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
        cartModel = new CartModel();
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
            collectAction(I.ACTION_IS_COLLECT, user);
        }
    }

    private void collectAction(final int action, User user) {
        model.collectAction(GoodsDetailsActivity.this, action, goodsId, user.getMuserName(),
                new OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean msg) {
                        if (msg != null && msg.isSuccess()) {
                            isCollects = action == I.ACTION_DELETE_COLLECT ? false : true;
                            if (action!=I.ACTION_IS_COLLECT) {
                                CommonUtils.showShortToast(msg.getMsg());
                            }
                        } else {
                            isCollects = action == I.ACTION_DELETE_COLLECT ? true : false;
                        }
                        setCollectStatus();
                    }

                    @Override
                    public void onError(String error) {
                        L.e(TAG, "error=" + error);
                        if (action==I.ACTION_IS_COLLECT) {
                            isCollects = false;
                            setCollectStatus();
                        }
                    }
                });
    }

    private void setCollectStatus() {
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

    @OnClick(R.id.iv_good_collect)
    public void collectGoods() {
        if (util.check()) return;
        User user = FuLiCenterApplication.getCurrentUser();
        if (user == null) {
            MFGT.gotoLogin(GoodsDetailsActivity.this, 0);
        } else {
            if (isCollects) {
                //取消收藏
                collectAction(I.ACTION_DELETE_COLLECT, user);
            } else {
                //添加收藏
                collectAction(I.ACTION_ADD_COLLECT, user);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.e(TAG, "onDestroy,isCollects=" + isCollects);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backClick();
    }

    @OnClick(R.id.backClickArea)
    public void backClick() {
        MFGT.finish(GoodsDetailsActivity.this);
        setResult(RESULT_OK, new Intent()
                .putExtra(I.GoodsDetails.KEY_IS_COLLECTED, isCollects)
                .putExtra(I.GoodsDetails.KEY_GOODS_ID, goodsId));
    }

    @OnClick(R.id.iv_good_cart)
    public void addCart() {
        if (util.check())return;
        User user = FuLiCenterApplication.getCurrentUser();
        if (user == null) {
            MFGT.gotoLogin(GoodsDetailsActivity.this, 0);
        } else {
            addGoodsToCart(user);
        }
    }

    private void addGoodsToCart(User user) {
        cartModel.cartAction(GoodsDetailsActivity.this, I.ACTION_CART_ADD, null,
                String.valueOf(goodsId), user.getMuserName(), 1,
                new OnCompleteListener<MessageBean>() {
                    @Override
                    public void onSuccess(MessageBean result) {
                        if (result != null && result.isSuccess()) {
                            CommonUtils.showShortToast(R.string.add_goods_success);
                            sendBroadcast(new Intent(I.BROADCAST_UPDATA_CART));
                        } else {
                            CommonUtils.showShortToast(R.string.add_goods_fail);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        CommonUtils.showShortToast(R.string.add_goods_fail);
                        L.e(TAG, "addGoodsToCart,error=" + error);
                    }
                });
    }

    @OnClick(R.id.iv_good_share)
    public void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }
}
