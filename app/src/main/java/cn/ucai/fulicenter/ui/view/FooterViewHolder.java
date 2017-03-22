package cn.ucai.fulicenter.ui.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.ucai.fulicenter.R;

/**
 * Created by Administrator on 2017/3/16.
 */

public class FooterViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tvFooter)
    TextView tvFooter;

    public FooterViewHolder(View view) {
        super(view);
        tvFooter = (TextView) view.findViewById(R.id.tvFooter);
        ButterKnife.bind(this, view);
    }

    public void setFooter(String footerString) {
        tvFooter.setText(footerString);
    }

    public void setFooter(int footerString) {
        tvFooter.setText(footerString);
    }
}
