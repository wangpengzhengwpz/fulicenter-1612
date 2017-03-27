package cn.ucai.fulicenter.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.application.I;
import cn.ucai.fulicenter.ui.view.MFGT;

/**
 * Created by Administrator on 2017/3/27.
 */

public class OrderActivity extends AppCompatActivity {
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.ed_order_name)
    EditText edOrderName;
    @BindView(R.id.ed_order_phone)
    EditText edOrderPhone;
    @BindView(R.id.spin_order_province)
    Spinner spinOrderProvince;
    @BindView(R.id.ed_order_street)
    EditText edOrderStreet;
    @BindView(R.id.tv_order_price)
    TextView tvOrderPrice;
    int orderPrice = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        orderPrice = getIntent().getIntExtra(I.ORDER_BUY_PRICE, 0);
        initView();
    }

    private void initView() {
        tvCommonTitle.setText("填写收货人地址");
        tvOrderPrice.setText(String.valueOf(orderPrice));
    }


    @OnClick(R.id.backClickArea)
    public void backArea() {
        MFGT.finish(OrderActivity.this);
    }

    @OnClick(R.id.tv_order_buy)
    public void commitOrder() {
        String receiveName = edOrderName.getText().toString();
        if (TextUtils.isEmpty(receiveName)) {
            edOrderName.setError("收货人姓名不能为空");
            edOrderName.requestFocus();
            return;
        }
        String mobile = edOrderPhone.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            edOrderPhone.setError("手机号码不能为空");
            edOrderPhone.requestFocus();
            return;
        }
        if (!mobile.matches("[\\d]{11}")) {
            edOrderPhone.setError("手机号码格式错误");
            edOrderPhone.requestFocus();
            return;
        }
        String area = spinOrderProvince.getSelectedItem().toString();
        if (TextUtils.isEmpty(area)) {
            Toast.makeText(OrderActivity.this, "收货地区不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        String address = edOrderStreet.getText().toString();
        if (TextUtils.isEmpty(address)) {
            edOrderStreet.setError("街道地址不能为空");
           edOrderStreet.requestFocus();
            return;
        }
    }
}
