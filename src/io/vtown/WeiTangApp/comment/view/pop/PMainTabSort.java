package io.vtown.WeiTangApp.comment.view.pop;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import io.vtown.WeiTangApp.R;

/**
 * Created by datutu on 2016/10/31.
 */

public class PMainTabSort extends PopupWindow {
    /**
     * 上下文
     */
    private Context pContext;

    /**
     * 基view
     */
    private View BaseView;

    public PMainTabSort(Context pContext) {
        super(pContext);
        this.pContext = pContext;
        BaseView = LayoutInflater.from(pContext).inflate(
                R.layout.pop_maintab_sort, null);
        IPop();
    }

    private void IPop() {
        BaseView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        setContentView(BaseView);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        setBackgroundDrawable(dw);
        // setBackgroundDrawable(pContext.getResources().getDrawable(
        // R.drawable.home_select_bg));

        this.setOutsideTouchable(true);
    }
}
