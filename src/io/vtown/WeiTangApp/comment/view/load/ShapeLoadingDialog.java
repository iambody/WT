package io.vtown.WeiTangApp.comment.view.load;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;

import io.vtown.WeiTangApp.R;


/**
 */
public class ShapeLoadingDialog {


    private Context mContext;
    private Dialog mDialog;
    private LoadingView mLoadingView;
    private View mDialogContentView;


    public ShapeLoadingDialog(Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        mDialog = new Dialog(mContext, R.style.custom_load_dialog);
        mDialogContentView = LayoutInflater.from(mContext).inflate(R.layout.layout_load_dialog, null);


        mLoadingView = (LoadingView) mDialogContentView.findViewById(R.id.loadView);
        mDialog.setContentView(mDialogContentView);
        mDialog.setCanceledOnTouchOutside(false);
//        setBackground(mContext.getResources().getColor(R.color.white));
    }

    public void setBackground(int color) {
        GradientDrawable gradientDrawable = (GradientDrawable) mDialogContentView.getBackground();
        gradientDrawable.setColor(color);
    }

    public void setLoadingText(CharSequence charSequence) {
        mLoadingView.setLoadingText(charSequence);
    }

    public void show() {
        mDialog.show();

    }

    public boolean IsShow() {
        return mDialog.isShowing();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public Dialog getDialog() {
        return mDialog;
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        mDialog.setCanceledOnTouchOutside(cancel);
    }
}
