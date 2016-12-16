package io.vtown.WeiTangApp.comment.view.pop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
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

import static io.vtown.WeiTangApp.comment.view.pop.PShowShare.SHARE_GOODS_ERROR;

/**
 * Created by Yihuihua on 2016/9/7.
 */
public class PShowShare extends PopupWindow implements View.OnClickListener {

    private Activity activity;
    private Context mContext;

    private View mRootView;

    private BNew mShareBeanNew;
    protected String BaseKey_Bean = "abasebeankey";
    // 图片(九宫格)和视频分享 ，，商品分享，，Show分享
    private RelativeLayout show_share_to_pic_vedio, show_share_to_weixin, show_share_to_show;
    private TextView show_share_cancel;
    public boolean IsErWeiMaShare = false;

    public static final int SHARE_TO_SHOW = 110;//分享Show标识
    public static final int SHARE_TO_WXCHAT = 111;//分享微信好友
    public static final int SHARE_TO_FRIENDS = 112;//分享朋友圈
    public static final int SHARE_TO_QQCHAT = 113;//分享QQ好友
    public static final int SHARE_TO_QZONE = 114;//分享空间
    public static final int SHARE_TO_SINAWB = 115;//分享微薄
    public static final int SHARE_PIC_VEDIO = 116;//分享图片/视屏


    //三方分享时候
    public static final int SHARE_GOODS_OK = 119;//分享商品成功
    public static final int SHARE_GOODS_ERROR = 120;//分享商品成功
    private ShowShareInterListener MShowShareInterListener;
    private Dialog dialog;
    private ImageView iv_pic_vedio_share_icon;
    private TextView tv_pic_vedio_share_title;
    private View show_share_line;
    private boolean isPic;
    private boolean isUrl;

    public void SetShareListener(ShowShareInterListener result) {
        this.MShowShareInterListener = result;
    }

    public interface ShowShareInterListener {
        public void GetResultType(int ResultType);//1代表 好友；；2代表朋友圈  ；；3代表show分享  4代表取消
    }

    /**
     * @param sharebeanNew 分享的实体
     * @param isPic        分享是否是图片
     * @param isUrl        分享是否是带商品
     */
    public PShowShare(Context context, Activity mactivity, BNew sharebeanNew, boolean isPic, boolean isUrl) {
        this.mContext = context;
        this.activity = mactivity;
        if (null == sharebeanNew) {
            this.dismiss();
            return;
        }
        this.mShareBeanNew = sharebeanNew;
        mRootView = LayoutInflater.from(context).inflate(R.layout.pop_show_share, null);
        this.isPic = isPic;
        this.isUrl = isUrl;
        IPop();
        IView();

    }

    private void IView() {
        show_share_to_pic_vedio = (RelativeLayout) mRootView.findViewById(R.id.show_share_to_pic_vedio);
        show_share_line = mRootView.findViewById(R.id.show_share_line);
        show_share_to_weixin = (RelativeLayout) mRootView.findViewById(R.id.show_share_to_weixin);
        show_share_to_show = (RelativeLayout) mRootView.findViewById(R.id.show_share_to_show);
        iv_pic_vedio_share_icon = (ImageView) mRootView.findViewById(R.id.iv_pic_vedio_share_icon);
        tv_pic_vedio_share_title = (TextView) mRootView.findViewById(R.id.tv_pic_vedio_share_title);
        show_share_cancel = (TextView) mRootView.findViewById(R.id.show_share_cancel);
        show_share_to_pic_vedio.setOnClickListener(this);
        show_share_to_weixin.setOnClickListener(this);
        show_share_to_show.setOnClickListener(this);
        show_share_cancel.setOnClickListener(this);

        if (isPic) {
            tv_pic_vedio_share_title.setText("图片分享");
            iv_pic_vedio_share_icon.setImageResource(R.drawable.ic_jiugonggefenxiang_nor);
        } else {
            tv_pic_vedio_share_title.setText("视频分享");
            iv_pic_vedio_share_icon.setImageResource(R.drawable.ic_shipinfenxiang_nor);
        }

        if (isUrl) {
            show_share_line.setVisibility(View.VISIBLE);
            show_share_to_weixin.setVisibility(View.VISIBLE);
        } else {
            show_share_line.setVisibility(View.GONE);
            show_share_to_weixin.setVisibility(View.GONE);
        }

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
            case R.id.show_share_to_pic_vedio://最下边！！！！！ 图片(九宫格)和视频分享 ===》需要权限判断
                String aa = mShareBeanNew.getShare_url();

                if (!isPic) {
//                    controlType(SHARE_TO_FRIENDS);
                    mShareBeanNew.setShare_url(mShareBeanNew.getShare_vido_url());
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

//                Share(2);
                    this.dismiss();
                    showShareDialog();
                    return;
                }
                //是图片直接回调通知九宫格
                MShowShareInterListener.GetResultType(SHARE_PIC_VEDIO);

                this.dismiss();
                break;
            case R.id.show_share_to_weixin://中间！！！！！商品分享=====》需要权限
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

//                Share(2);
                this.dismiss();
                showShareDialog();


                break;
            case R.id.show_share_to_show://最上边！！！！！show分享====》不需要权限
                MShowShareInterListener.GetResultType(SHARE_TO_SHOW);
                this.dismiss();
                break;
            case R.id.show_share_cancel://取消
                //MShowShareInterListener.GetResultType(4);
                this.dismiss();
                break;

            case R.id.ll_share_2_wxchat://分享微信
//                PromptManager.ShowCustomToast(mContext, "分享微信");
                controlType(SHARE_TO_WXCHAT);
                break;

            case R.id.ll_share_2_wxfriends://分享朋友圈
//                PromptManager.ShowCustomToast(mContext, "分享朋友圈");
                controlType(SHARE_TO_FRIENDS);
                break;

            case R.id.ll_share_2_qq://分享QQ
//                PromptManager.ShowCustomToast(mContext, "分享QQ");
                controlType(SHARE_TO_QQCHAT);
                break;

            case R.id.ll_share_2_qzone://分享空间
//                PromptManager.ShowCustomToast(mContext, "分享空间");
                controlType(SHARE_TO_QZONE);
                break;
            case R.id.ll_share_2_sinawb://分享微博
//                PromptManager.ShowCustomToast(mContext, "分享微博");ss
                controlType(SHARE_TO_SINAWB);
                break;

            case R.id.dialog_show_share_cancel:
                if (null != dialog) {
                    dialog.dismiss();
                }
                break;
        }
    }

    private void controlType(int resultType) {

        if (resultType == SHARE_TO_WXCHAT || resultType == SHARE_TO_FRIENDS) {
            //判断是否安装微信
            if (!ViewUtils.isWeixinAvilible(activity)) {
                PromptManager.ShowCustomToast(activity, "请先安装手机微信");
                return;
            }
        }
        if (resultType == SHARE_TO_QQCHAT || resultType == SHARE_TO_QZONE) {
            //判断是否安装微信
            if (!ViewUtils.isQQClientAvailable(activity)) {
                PromptManager.ShowCustomToast(activity, "请先安装手机QQ");
                return;
            }
        }
        Share(resultType);
        PromptManager.showLoading(mContext);
        if (dialog != null)
            dialog.dismiss();
    }

    private void showShareDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity,R.style.mystyle_dialog);
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_show_share, null);
//        builder.setView(view);
//        dialog = builder.create();
        if (dialog == null) {
            dialog = new Dialog(activity, R.style.mystyle_dialog);
            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(true);
        }
        view.findViewById(R.id.ll_share_2_wxchat).setOnClickListener(this);
        view.findViewById(R.id.ll_share_2_wxfriends).setOnClickListener(this);
        view.findViewById(R.id.ll_share_2_qq).setOnClickListener(this);
        view.findViewById(R.id.ll_share_2_qzone).setOnClickListener(this);
        view.findViewById(R.id.ll_share_2_sinawb).setOnClickListener(this);
        view.findViewById(R.id.dialog_show_share_cancel).setOnClickListener(this);
        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
        attributes.gravity = Gravity.BOTTOM;
        dialog.show();
    }


    private void Share(final int Type) {
        ShareSDK.initSDK(mContext);
        Platform platform = null;
        Platform.ShareParams sp = new Platform.ShareParams();

        switch (Type) {
            case SHARE_TO_WXCHAT:// 好友分享
                platform = ShareSDK.getPlatform(mContext, Wechat.NAME);
                sp.setShareType(Platform.SHARE_WEBPAGE);// S
                sp.setText(mShareBeanNew.getShare_content());
                sp.setImageUrl(mShareBeanNew.getShare_log());
                sp.setTitle(mShareBeanNew.getShare_title());//
                sp.setUrl(mShareBeanNew.getShare_url());
                break;
            case SHARE_TO_FRIENDS:// 朋友圈分享
                platform = ShareSDK.getPlatform(mContext, WechatMoments.NAME);
                sp.setShareType(Platform.SHARE_WEBPAGE);
                sp.setText(mShareBeanNew.getShare_content());
                sp.setImageUrl(mShareBeanNew.getShare_log());
                sp.setTitle(mShareBeanNew.getShare_title());//
                sp.setUrl(mShareBeanNew.getShare_url());
                break;
            case SHARE_TO_QQCHAT://QQ好友
                platform = ShareSDK.getPlatform(mContext, QQ.NAME);
                sp.setTitle(mShareBeanNew.getShare_title());
                sp.setTitleUrl(mShareBeanNew.getShare_url()); // 标题的超链接
                sp.setText(mShareBeanNew.getShare_content());
                sp.setImageUrl(mShareBeanNew.getShare_log());
                break;
            case SHARE_TO_QZONE://QQ空间
                platform = ShareSDK.getPlatform(mContext, QZone.NAME);
                sp.setTitle(mShareBeanNew.getShare_title());
                sp.setTitleUrl(mShareBeanNew.getShare_url()); // 标题的超链接
                sp.setText(mShareBeanNew.getShare_content());
                sp.setImageUrl(mShareBeanNew.getShare_log());
                break;
            case SHARE_TO_SINAWB://微博

                break;
            default:
                break;
        }

        platform.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                PromptManager.ShowCustomToast(mContext, "分享取消");
                MShowShareInterListener.GetResultType(SHARE_GOODS_ERROR);
                PromptManager.closeLoading();
                PShowShare.this.dismiss();
            }

            @Override
            public void onComplete(Platform arg0, int arg1,
                                   HashMap<String, Object> arg2) {
                PromptManager.ShowCustomToast(mContext, "分享完成");
                if(SHARE_TO_FRIENDS==Type||SHARE_TO_FRIENDS==Type)
                MShowShareInterListener.GetResultType(SHARE_GOODS_OK);
                PromptManager.closeLoading();
                PShowShare.this.dismiss();
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                PromptManager.ShowCustomToast(mContext, "分享取消");
                MShowShareInterListener.GetResultType(SHARE_GOODS_ERROR);
                PromptManager.closeLoading();
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
