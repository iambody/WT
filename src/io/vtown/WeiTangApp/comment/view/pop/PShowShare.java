package io.vtown.WeiTangApp.comment.view.pop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BLShow;
import io.vtown.WeiTangApp.bean.bcomment.new_three.BActive;
import io.vtown.WeiTangApp.bean.bcomment.new_three.BNewHome;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.ViewUtils;
import io.vtown.WeiTangApp.comment.view.ShowSelectPic;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.comment.AWeb;
import io.vtown.WeiTangApp.ui.title.loginregist.bindcode_three.ANewBindCode;
import io.vtown.WeiTangApp.ui.title.zhuanqu.AZhuanQu;

/**
 * Created by Yihuihua on 2016/9/7.
 */
public class PShowShare extends PopupWindow implements View.OnClickListener {

    private Activity activity;
    private Context mContext;

    private View mRootView;

    private BNew mShareBeanNew;
    protected String BaseKey_Bean = "abasebeankey";
    private RelativeLayout show_share_to_friends, show_share_to_weixin, show_share_to_show;
    private TextView show_share_cancel;
    public boolean IsErWeiMaShare = false;


    private ShowShareInterListener MShowShareInterListener;

    public void SetShareListener(ShowShareInterListener result) {
        this.MShowShareInterListener = result;
    }

    public interface ShowShareInterListener {
        public void GetResultType(int ResultType);//1代表 好友；；2代表朋友圈  ；；3代表show分享  4代表取消
    }

    public PShowShare(Context context, Activity mactivity, BNew sharebeanNew) {
        this.mContext = context;
        this.activity = mactivity;
        if (null == sharebeanNew) {
            this.dismiss();
            return;
        }
        this.mShareBeanNew = sharebeanNew;
        mRootView = LayoutInflater.from(context).inflate(R.layout.pop_show_share, null);

        IPop();
        IView();

    }

    private void IView() {
        show_share_to_friends = (RelativeLayout) mRootView.findViewById(R.id.show_share_to_friends);
        show_share_to_weixin = (RelativeLayout) mRootView.findViewById(R.id.show_share_to_weixin);
        show_share_to_show = (RelativeLayout) mRootView.findViewById(R.id.show_share_to_show);
        show_share_cancel = (TextView) mRootView.findViewById(R.id.show_share_cancel);
        show_share_to_friends.setOnClickListener(this);
        show_share_to_weixin.setOnClickListener(this);
        show_share_to_show.setOnClickListener(this);
        show_share_cancel.setOnClickListener(this);

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_share_to_friends://分享好友
                //权限888888888888888888888
                //如果未绑定或者已绑定未激活的用户分享权限的判断***************************
                if (!Spuit.IsHaveBind_Get(activity) && !Spuit.IsHaveBind_JiQi_Get(activity)) {//未绑定邀请码
                    ShowCustomDialog(activity.getResources().getString(R.string.no_bind_code),
                            activity.getResources().getString(R.string.quxiao), activity.getResources().getString(R.string.bind_code),
                            new IDialogResult() {
                                @Override
                                public void RightResult() {
                                    PShowShare.this.dismiss();
                                    PromptManager.SkipActivity(activity, new Intent(activity,
                                            ANewBindCode.class));
                                    //
                                }

                                @Override
                                public void LeftResult() {

                                }
                            });
                    return;
                }
                if (!Spuit.IsHaveActive_Get(activity)) {//绑定邀请码未激活
                    ShowCustomDialog(JSON.parseObject(CacheUtil.NewHome_Get(activity), BNewHome.class).getIntegral() < 10000 ? activity.getResources().getString(R.string.to_Jihuo_toqiandao1) : activity.getResources().getString(R.string.to_Jihuo_toqiandao2),
                            activity.getResources().getString(R.string.look_guize), activity.getResources().getString(R.string.to_jihuo1),
                            new IDialogResult() {
                                @Override
                                public void RightResult() {

                                    BActive maxtive = Spuit.Jihuo_get(activity);
                                    BComment mBCommentss = new BComment(maxtive.getActivityid(),
                                            maxtive.getActivitytitle());
                                    PromptManager.SkipActivity(activity, new Intent(
                                            activity, AZhuanQu.class).putExtra(BaseKey_Bean,
                                            mBCommentss));
                                    PShowShare.this.dismiss();

                                }

                                @Override
                                public void LeftResult() {
                                    PShowShare.this.dismiss();
                                    PromptManager.SkipActivity(activity, new Intent(
                                            activity, AWeb.class).putExtra(
                                            AWeb.Key_Bean,
                                            new BComment(Constants.Homew_JiFen, activity.getResources().getString(R.string.jifenguize))));

                                }
                            });

                    return;
                }
//权限888888888888888888888
                //判断是否安装微信
                if(!ViewUtils.isWeixinAvilible(activity)){
                    PromptManager.ShowCustomToast(activity,"请先安装手机微信");
                    return;
                }
                Share(1);
                MShowShareInterListener.GetResultType(1);
                this.dismiss();
                break;
            case R.id.show_share_to_weixin://分享朋友圈
                //权限888888888888888888888
                //如果未绑定或者已绑定未激活的用户分享权限的判断***************************
                if (!Spuit.IsHaveBind_Get(activity) && !Spuit.IsHaveBind_JiQi_Get(activity)) {//未绑定邀请码
                    ShowCustomDialog(activity.getResources().getString(R.string.no_bind_code),
                            activity.getResources().getString(R.string.quxiao), activity.getResources().getString(R.string.bind_code),
                            new IDialogResult() {
                                @Override
                                public void RightResult() {
                                    PShowShare.this.dismiss();
                                    PromptManager.SkipActivity(activity, new Intent(activity,
                                            ANewBindCode.class));
                                    //
                                }

                                @Override
                                public void LeftResult() {

                                }
                            });
                    return;
                }
                if (!Spuit.IsHaveActive_Get(activity)) {//绑定邀请码未激活
                    ShowCustomDialog(JSON.parseObject(CacheUtil.NewHome_Get(activity), BNewHome.class).getIntegral() < 10000 ? activity.getResources().getString(R.string.to_Jihuo_toqiandao1) : activity.getResources().getString(R.string.to_Jihuo_toqiandao2),
                            activity.getResources().getString(R.string.look_guize), activity.getResources().getString(R.string.to_jihuo1),
                            new IDialogResult() {
                                @Override
                                public void RightResult() {

                                    BActive maxtive = Spuit.Jihuo_get(activity);
                                    BComment mBCommentss = new BComment(maxtive.getActivityid(),
                                            maxtive.getActivitytitle());
                                    PromptManager.SkipActivity(activity, new Intent(
                                            activity, AZhuanQu.class).putExtra(BaseKey_Bean,
                                            mBCommentss));
                                    PShowShare.this.dismiss();

                                }

                                @Override
                                public void LeftResult() {
                                    PShowShare.this.dismiss();
                                    PromptManager.SkipActivity(activity, new Intent(
                                            activity, AWeb.class).putExtra(
                                            AWeb.Key_Bean,
                                            new BComment(Constants.Homew_JiFen, activity.getResources().getString(R.string.jifenguize))));

                                }
                            });

                    return;
                }
//权限888888888888888888888
                //判断是否安装微信
                if(!ViewUtils.isWeixinAvilible(activity)){
                    PromptManager.ShowCustomToast(activity,"请先安装手机微信");
                    return;
                }             Share(2);
                MShowShareInterListener.GetResultType(2);
                this.dismiss();
                break;
            case R.id.show_share_to_show://show分享
                MShowShareInterListener.GetResultType(3);
                this.dismiss();
                break;
            case R.id.show_share_cancel://取消
                MShowShareInterListener.GetResultType(4);
                this.dismiss();
                break;
        }
    }


    private void Share(int Type) {
        ShareSDK.initSDK(mContext);
        Platform platform = null;
        Platform.ShareParams sp = new Platform.ShareParams();

        switch (Type) {
            case 1:// 好友分享
                platform = ShareSDK.getPlatform(mContext, Wechat.NAME);
                sp.setShareType(Platform.SHARE_WEBPAGE);// S
                sp.setText(mShareBeanNew.getShare_content());
                sp.setImageUrl(mShareBeanNew.getShare_log());
                sp.setTitle(mShareBeanNew.getShare_title());//
                sp.setUrl(mShareBeanNew.getShare_url());
                break;
            case 2:// 朋友圈分享
                platform = ShareSDK.getPlatform(mContext, WechatMoments.NAME);
                sp.setShareType(Platform.SHARE_WEBPAGE);
                sp.setText(mShareBeanNew.getShare_content());
                sp.setImageUrl(mShareBeanNew.getShare_log());
                sp.setTitle(mShareBeanNew.getShare_title());//
                sp.setUrl(mShareBeanNew.getShare_url());
                break;
            default:
                break;
        }

        platform.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                PromptManager.ShowCustomToast(mContext, "分享取消");
                PShowShare.this.dismiss();
            }

            @Override
            public void onComplete(Platform arg0, int arg1,
                                   HashMap<String, Object> arg2) {
                PromptManager.ShowCustomToast(mContext, "分享完成");
                PShowShare.this.dismiss();
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                PromptManager.ShowCustomToast(mContext, "分享取消");
                PShowShare.this.dismiss();
            }
        });
        platform.share(sp);
    }

    public void ShowCustomDialog(String title, String Left, String Right,
                                 final IDialogResult mDialogResult) {
        final CustomDialog dialog = new CustomDialog(activity,
                R.style.mystyle, R.layout.dialog_purchase_cancel, 1, Left,
                Right);
        dialog.show();
        dialog.setTitleText(title);
        dialog.HindTitle2();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setcancelListener(new CustomDialog.oncancelClick() {

            @Override
            public void oncancelClick(View v) {
                dialog.dismiss();
                mDialogResult.LeftResult();
            }
        });

        dialog.setConfirmListener(new CustomDialog.onConfirmClick() {
            @Override
            public void onConfirmCLick(View v) {
                dialog.dismiss();
                mDialogResult.RightResult();
            }
        });
    }

    public boolean isIsErWeiMaShare() {
        return IsErWeiMaShare;
    }

    public void setIsErWeiMaShare(boolean isErWeiMaShare) {
        IsErWeiMaShare = isErWeiMaShare;
    }
}
