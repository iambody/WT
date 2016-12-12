package io.vtown.WeiTangApp.comment.view.pop;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import io.vtown.WeiTangApp.R;


/**
 * Created by Yihuihua on 2016/12/12.
 */

public class PPicOrVedioShare extends PopupWindow implements View.OnClickListener {
    private Context mContext;
    private View mRootView;
    private boolean isPic  ;
    private boolean IsUrl;
    private RelativeLayout show_pic_or_vedio_share_to_show;
    private RelativeLayout show_pic_or_vedio_share_to_pic_vedio;
    private ImageView iv_pic_or_vedio_pic_vedio_share_icon;
    private TextView tv_pic_or_vedio_pic_vedio_share_title;

    private OnPicOrVedioShareListener mListener;
    public static final int SHOW_SHARE = 1144;
    public static final int PIC_SHARE = 3344;
    public static final int VEDIO_SHARE = 1314;
    public static final int GOOD_SHARE = 4411;//商品分享





    private RelativeLayout show_pic_or_vedio_share_to_weixin;

    protected String BaseKey_Bean = "xxasebeankey";
    private AlertDialog dialog;


    public interface OnPicOrVedioShareListener{
       void showStatus(int type);
   }

    public void setOnPicOrVedioShareListener(OnPicOrVedioShareListener listener){
        this.mListener = listener;
    }


    public PPicOrVedioShare(Context context,boolean isPic,boolean ISUrl) {
        this.mContext = context;

        mRootView = LayoutInflater.from(context).inflate(R.layout.pop_pic_or_vedio_show_share, null);
        this.isPic = isPic;
        this.IsUrl=ISUrl;
        IPop();
        IView();

    }

    private void IPop() {
        setContentView(mRootView);
        mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
//
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);
    }

    private void IView() {
        show_pic_or_vedio_share_to_show = (RelativeLayout) mRootView.findViewById(R.id.show_pic_or_vedio_share_to_show);
        show_pic_or_vedio_share_to_weixin = (RelativeLayout) mRootView.findViewById(R.id.show_pic_or_vedio_share_to_weixin);
        show_pic_or_vedio_share_to_pic_vedio = (RelativeLayout) mRootView.findViewById(R.id.show_pic_or_vedio_share_to_pic_vedio);
        iv_pic_or_vedio_pic_vedio_share_icon = (ImageView) mRootView.findViewById(R.id.iv_pic_or_vedio_pic_vedio_share_icon);
        tv_pic_or_vedio_pic_vedio_share_title = (TextView) mRootView.findViewById(R.id.tv_pic_or_vedio_pic_vedio_share_title);
        TextView show_pic_or_vedio_share_cancel = (TextView) mRootView.findViewById(R.id.show_pic_or_vedio_share_cancel);
        show_pic_or_vedio_share_to_show.setOnClickListener(this);
        show_pic_or_vedio_share_to_weixin.setOnClickListener(this);
        show_pic_or_vedio_share_to_pic_vedio.setOnClickListener(this);
        show_pic_or_vedio_share_cancel.setOnClickListener(this);

        if(IsUrl){
            show_pic_or_vedio_share_to_weixin.setVisibility(View.VISIBLE);
        }else{
            show_pic_or_vedio_share_to_weixin.setVisibility(View.GONE);
        }

        if (isPic) {
            tv_pic_or_vedio_pic_vedio_share_title.setText("图片分享");
            iv_pic_or_vedio_pic_vedio_share_icon.setImageResource(R.drawable.ic_jiugonggefenxiang_nor);
        } else {
            tv_pic_or_vedio_pic_vedio_share_title.setText("视频分享");
            iv_pic_or_vedio_pic_vedio_share_icon.setImageResource(R.drawable.ic_shipinfenxiang_nor);
        }




        //如果椒Urld
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.show_pic_or_vedio_share_to_show://Show分享
                mListener.showStatus(SHOW_SHARE);
                break;

            case R.id.show_pic_or_vedio_share_to_weixin://商品分享
                mListener.showStatus(GOOD_SHARE);
                break;

            case R.id.show_pic_or_vedio_share_to_pic_vedio://图片或视频分享
                mListener.showStatus(isPic?PIC_SHARE:VEDIO_SHARE);
                break;

            case R.id.show_pic_or_vedio_share_cancel:
                PPicOrVedioShare.this.dismiss();
                break;


        }
    }




}
