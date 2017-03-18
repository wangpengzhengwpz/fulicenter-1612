package cn.ucai.fulicenter.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.ucai.fulicenter.R;
import cn.ucai.fulicenter.model.utils.L;

/**
 * Created by Administrator on 2017/3/18.
 */

public class CatFilterCategoryButton extends Button {
    private static final String TAG = "CatFilterCategoryButton";
    Context mContext;
    boolean isExpan = false;
    PopupWindow popupWindow;
    public CatFilterCategoryButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        showArrow();
    }

    private void initPop() {
        popupWindow = new PopupWindow(mContext);
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xbb000000));
        TextView tv = new TextView(mContext);
        tv.setTextColor(getResources().getColor(R.color.red));
        tv.setTextSize(30);
        tv.setText("CatFilterCategoryButton");
        popupWindow.setContentView(tv);
        popupWindow.showAsDropDown(this);
    }

    private void showArrow() {
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
                L.e(TAG, "setOnClickListener" + isExpan);
                Drawable end = getResources().getDrawable(isExpan ?
                        R.drawable.arrow2_up : R.drawable.arrow2_down);
                setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, end, null);
                isExpan = !isExpan;
            }
        });
    }
}
