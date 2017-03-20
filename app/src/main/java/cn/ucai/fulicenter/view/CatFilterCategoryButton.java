package cn.ucai.fulicenter.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.bean.CategoryChildBean;
import cn.ucai.fulicenter.model.utils.CommonUtils;
import cn.ucai.fulicenter.ui.adapter.CatFilterAdapter;

/**
 * Created by Administrator on 2017/3/18.
 */

public class CatFilterCategoryButton extends Button {
    private static final String TAG = "CatFilterCategoryButton";
    Context mContext;
    boolean isExpan = false;
    PopupWindow popupWindow;
    GridView gv;
    CatFilterAdapter adapter;
    ArrayList<CategoryChildBean> list = new ArrayList<>();

    //butterknife实例化
    public CatFilterCategoryButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setCatFilterOnClickListener();
    }

    private void setCatFilterOnClickListener() {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpan) {
                    initPop();
                } else {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    }
                }
                showArrow();
            }
        });
    }

    private void initPop() {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(mContext);
            popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new ColorDrawable(0xbb000000));
            popupWindow.setContentView(gv);
        }

        popupWindow.showAsDropDown(this);
    }

    private void showArrow() {
        Drawable end = getResources().getDrawable(isExpan ?
                R.drawable.arrow2_down : R.drawable.arrow2_up);
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
        isExpan = !isExpan;
    }

    public void initView(String groupName, ArrayList<CategoryChildBean> l) {
        if (groupName == null || l == null) {
            CommonUtils.showShortToast("小类数据获取异常");
            return;
        }
        this.setText(groupName);
        list = l;
        //实例化列表控件
        gv = new GridView(mContext);
        gv.setHorizontalSpacing(10);
        gv.setVerticalSpacing(10);
        gv.setNumColumns(GridView.AUTO_FIT);
        //列表里面显示数据的adapter适配器
        adapter = new CatFilterAdapter(mContext, list, groupName);
        gv.setAdapter(adapter);
    }
}
