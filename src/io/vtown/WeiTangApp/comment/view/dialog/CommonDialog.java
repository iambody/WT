package io.vtown.WeiTangApp.comment.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;

/**
 * Created by Yihuihua on 2016/12/14.
 */

public class CommonDialog extends Dialog {

    private Context mContext;
    private List<String> mDatas;
    private LinearLayout dialog_common_root_layout;
    private View mRootView;

    public CommonDialog(Context context, List<String> data) {
        super(context);
        this.mContext = context;
        this.mDatas = data;

    }

    public CommonDialog(Context context, int theme) {
        super(context, theme);
    }

    protected CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView = LayoutInflater.from(mContext).inflate(R.layout.dialog_common, null);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(mRootView);
        IView();
    }

    private void IView() {
        dialog_common_root_layout = (LinearLayout) mRootView.findViewById(R.id.dialog_common_root_layout);
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //contentParams.setMargins(DimensionPixelUtil.dip2px(mContext, 16), 0, 0, 0);
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        if (mDatas != null && mDatas.size() > 0) {
            for (int i = 0; i < mDatas.size(); i++) {
                View line = new View(mContext);
                line.setBackgroundResource(R.color.app_gray);
                line.setLayoutParams(lineParams);
                TextView textView = new TextView(mContext);
                textView.setClickable(true);
                textView.setLayoutParams(contentParams);
                textView.setPadding(DimensionPixelUtil.dip2px(mContext, 11), DimensionPixelUtil.dip2px(mContext, 11), 10, DimensionPixelUtil.dip2px(mContext, 11));
                textView.setGravity(Gravity.LEFT);
                textView.setTextSize(16);
                textView.setTextColor(mContext.getResources().getColor(R.color.black));
                textView.setText(mDatas.get(i));
                dialog_common_root_layout.addView(textView);
                if (i != mDatas.size() - 1) {
                    dialog_common_root_layout.addView(line);

                }
                if(0 == i){
                    textView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.selector_common_dialog_top_bg));
                }else if(mDatas.size() -1 == i){
                    textView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.selector_common_dialog_bottom_bg));
                }else{
                    textView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.selector_common_dialog_mid_bg));
                }

                textView.setOnClickListener(new myOnclickListener(i));
            }
        }
    }

    class myOnclickListener implements View.OnClickListener {

        private int clickposition;

        public myOnclickListener(int position) {
            clickposition = position;
        }

        @Override
        public void onClick(View v) {
            mListener.clickPosition(clickposition);
            dismiss();
        }
    }

    public interface OnCommonDialogClickListener {
        void clickPosition(int position);
    }

    private OnCommonDialogClickListener mListener;

    public void setOnCommonDialogClickListener(OnCommonDialogClickListener listener) {
        mListener = listener;
    }
}
