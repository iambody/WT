package io.vtown.WeiTangApp.comment.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import io.vtown.WeiTangApp.R;

/**
 * Created by Yihuihua on 2017/1/3.
 */

public class OrderShareDialog extends Dialog {

    private View mRootView;
    private Context mContext;

    public OrderShareDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public OrderShareDialog(Context context, int theme) {
        super(context, theme);
    }

    protected OrderShareDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_order_share, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(mRootView);
    }
}
