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
import android.widget.TextView;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BLShow;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.view.ShowSelectPic;

/**
 * Created by Yihuihua on 2016/9/7.
 */
public class PShowShare extends PopupWindow implements View.OnClickListener {

    private Context mContext;

    private View mRootView;

    private BNew mShareBeanNew;

    private LinearLayout show_share_to_friends,show_share_to_weixin,show_share_to_show;
    private TextView show_share_cancel;
    public boolean IsErWeiMaShare = false;
    private BLShow mBLComment;

    public PShowShare(Context context,BNew sharebeanNew,BLShow datBlComment){
        this.mContext = context;
        if (null == sharebeanNew && null == datBlComment) {
            this.dismiss();
            return;
        }
        this.mBLComment = datBlComment;
        this.mShareBeanNew = sharebeanNew;
        mRootView = LayoutInflater.from(context).inflate(R.layout.pop_show_share,null);
        ShareSDK.initSDK(context);
        IPop();
        IView();

    }

    private void IView() {
        show_share_to_friends = (LinearLayout) mRootView.findViewById(R.id.show_share_to_friends);
        show_share_to_weixin = (LinearLayout) mRootView.findViewById(R.id.show_share_to_weixin);
        show_share_to_show = (LinearLayout) mRootView.findViewById(R.id.show_share_to_show);
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
       setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        //ColorDrawable dw = new ColorDrawable(0xb0000000);
        setBackgroundDrawable(null);
        this.setOutsideTouchable(true);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.show_share_to_friends://分享好友
                Share(1);
                this.dismiss();
                break;
            case R.id.show_share_to_weixin://分享朋友圈
                Share(2);
                this.dismiss();
                break;
            case R.id.show_share_to_show://show分享
                toShow();
                break;
            case R.id.show_share_cancel://取消
                this.dismiss();
                break;
        }
    }

    private void toShow(){
        PromptManager
                .SkipActivity(
                        (Activity) mContext,
                        new Intent(mContext, ShowSelectPic.class).putExtra(
                                ShowSelectPic.Key_Data,
                                mBLComment));
        this.dismiss();
    }

    private void Share(int Type) {

        Platform platform = null;
        Platform.ShareParams sp = new Platform.ShareParams();
        switch (Type) {
            case 1:// 好友分享
                platform = ShareSDK.getPlatform(mContext, Wechat.NAME);
//			sp.setShareType(Platform.SHARE_WEBPAGE);
                if (IsErWeiMaShare) {//二维码=》图片
                    sp.setShareType(Platform.SHARE_IMAGE);
                } else {//飞二维码=》网页
                    sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);}
                }
                // sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);
                // sp.setText("大兔兔的测试数据");
                // sp.setImageUrl("http://static.freepik.com/free-photo/letter-a-underlined_318-8682.jpg");
                // sp.setTitle("大兔兔的title");//
                // sp.setUrl("www.baidu.com");

                sp.setText(mShareBeanNew.getShare_content());
                sp.setImageUrl(mShareBeanNew.getShare_log());
                sp.setTitle(mShareBeanNew.getShare_title());//
                sp.setUrl(mShareBeanNew.getShare_url());
                break;
            case 2:// 朋友圈分享
                platform = ShareSDK.getPlatform(mContext, WechatMoments.NAME);
                if (IsErWeiMaShare) {//二维码=》图片
                    sp.setShareType(Platform.SHARE_IMAGE);
                } else {//飞二维码=》网页
                    sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);}
                }
                // sp.setText("大兔兔的测试数据");
                // sp.setImageUrl("http://static.freepik.com/free-photo/letter-a-underlined_318-8682.jpg");
                // sp.setTitle("大兔兔的测试数据");//
                // sp.setUrl("www.baidu.com");
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
            }
        });
        platform.share(sp);
    }

    public boolean isIsErWeiMaShare() {
        return IsErWeiMaShare;
    }

    public void setIsErWeiMaShare(boolean isErWeiMaShare) {
        IsErWeiMaShare = isErWeiMaShare;
    }
}
