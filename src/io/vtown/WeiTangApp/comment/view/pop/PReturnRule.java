package io.vtown.WeiTangApp.comment.view.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import io.vtown.WeiTangApp.R;


/**
 * Created by Yihuihua on 2016/10/11.
 */

public class PReturnRule extends PopupWindow {

    private Context mContext;
    private View mRootView;

    public PReturnRule(Context context){
       this.mContext = context;
        initPop();
        initView();
    }

    private void initPop() {
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.pop_return_rule, null);
        setContentView(mRootView);
        mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setTouchable(true);
        this.update();
        this.setOutsideTouchable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        setBackgroundDrawable(dw);

    }

    private void initView() {

    }
}
