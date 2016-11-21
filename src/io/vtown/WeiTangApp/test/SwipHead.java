package io.vtown.WeiTangApp.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;

import io.vtown.WeiTangApp.R;

/**
 * Created by datutu on 2016/11/21.
 */

public class SwipHead extends LinearLayout implements SwipeRefreshTrigger, SwipeTrigger {
    private TextView tvStatus;

    public SwipHead(Context context) {
        this(context, null, 0);
    }


    public SwipHead(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipHead(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        //这个view随意定义
        //这里的原理就是简单的动态布局添加
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = View.inflate(getContext(), R.layout.test_swipload_head, null);
        addView(view, lp);
        tvStatus = (TextView) view.findViewById(R.id.swip_head_txt);
    }

    @Override
    public void onRefresh() {
        tvStatus.setText("刷新");
    }

    @Override
    public void onPrepare() {
        tvStatus.setText("准备");
    }

    @Override
    public void onMove(int i, boolean b, boolean b1) {

    }

    @Override
    public void onRelease() {
        tvStatus.setText("松开");
    }

    @Override
    public void onComplete() {
        tvStatus.setText("完成");
    }

    @Override
    public void onReset() {
        tvStatus.setText("onReset");
    }
}
