package io.vtown.WeiTangApp.comment.view.pop;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

import cn.sharesdk.wechat.moments.WechatMoments;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcache.BHome;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.ViewUtils;
import io.vtown.WeiTangApp.comment.view.pop.PHomeSelect.SeleckClickListener;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-12 下午5:27:35 分享时候调用的代码
 */
public class PShare extends PopupWindow implements OnClickListener {
    /**
     * 上下文
     */
    private Context pContext;

    /**
     * 基view
     */
    private View BaseView;
    /**
     * view
     */
    private RelativeLayout pop_share_haoyou, pop_share_pyq, pop_share_qq, pop_share_qqkj, pop_share_show, pop_share_weibo;
    private TextView pop_share_cancle;
    /**
     * 回掉接口
     *
     * @param pContext
     */
    private SeleckClickListener mClickListener;

    /**
     * 保存分享信息的实体类
     *
     * @param pContext
     */
    private BNew ShareBeanNew;

    public boolean IsErWeiMaShare = false;
    /**
     * 商品PoP点击Show分享 通知商品详情页面进行跳转到发show页面
     */
    public static final int TYPE_GOODDETAIL_TO_SHOW = 1144;
    //是否需要显示show转发
    private boolean IsNoShowShare;


    //成功失败的暴露接口
    private ShareResultIntface MShareResultIntface;

    /**
     * 暴露成功失败的结果
     */
    public void GetShareResult(ShareResultIntface result) {
        this.MShareResultIntface = result;
    }

    public PShare(Context pContext, BNew sharebeanNew, boolean IsHaveShowShare) {
        super();
        this.pContext = pContext;
        this.IsNoShowShare = IsHaveShowShare;
        BaseView = LayoutInflater.from(pContext).inflate(R.layout.pop_share,
                null);
        this.ShareBeanNew = sharebeanNew;
        if (null == ShareBeanNew) {
            this.dismiss();
//			PromptManager.ShowCustomToast(pContext, "请传入BNew实体类(后台是否存在?)");
            return;
        }
        IPop();
        IView();
        ShareSDK.initSDK(pContext);
    }

    private void IView() {
        pop_share_show = ViewHolder.get(BaseView, R.id.pop_share_show);
        pop_share_haoyou = ViewHolder.get(BaseView, R.id.pop_share_haoyou);
        pop_share_pyq = ViewHolder.get(BaseView, R.id.pop_share_pyq);
        pop_share_cancle = ViewHolder.get(BaseView, R.id.pop_share_cancle);
        pop_share_qq = ViewHolder.get(BaseView, R.id.pop_share_qq);
        pop_share_qqkj = ViewHolder.get(BaseView, R.id.pop_share_qqkj);
        pop_share_weibo = ViewHolder.get(BaseView, R.id.pop_share_weibo);
        pop_share_cancle.setOnClickListener(this);
        pop_share_haoyou.setOnClickListener(this);
        pop_share_pyq.setOnClickListener(this);
        pop_share_qq.setOnClickListener(this);
        pop_share_qqkj.setOnClickListener(this);
        pop_share_show.setOnClickListener(this);
        pop_share_weibo.setOnClickListener(this);
        pop_share_show.setVisibility(IsNoShowShare ? View.GONE : View.VISIBLE);

    }


    private void IPop() {
        BaseView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        setContentView(BaseView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.pop_share_weibo://新浪微博
                ShareSina();
                break;
            case R.id.pop_share_qq://QQ好友
                ShareQQ(1);
                PShare.this.dismiss();
                break;
            case R.id.pop_share_qqkj://QQ朋友圈
                ShareQQ(2);
                PShare.this.dismiss();
                break;
            case R.id.pop_share_haoyou:
                Share(1);
                PShare.this.dismiss();
                break;
            case R.id.pop_share_pyq:
                Share(2);
                PShare.this.dismiss();
                break;
            case R.id.pop_share_cancle:
                PShare.this.dismiss();
                break;

            case R.id.pop_share_show://发show
                PShare.this.dismiss();
                EventBus.getDefault().post(new BMessage(TYPE_GOODDETAIL_TO_SHOW));

                break;
            default:
                break;
        }
    }

    private void ShareSina() {

        if (!ViewUtils.isWeiboInstalled(pContext)) {
            PromptManager.ShowCustomToast(pContext, pContext.getResources().getString(R.string.weibonoanzhuang));
            return;
        }


        Platform platform = null;
        ShareParams sp = new ShareParams();
        platform = ShareSDK.getPlatform(pContext, SinaWeibo.NAME);
//        platform.SSOSetting(true);
//        disableSSOWhenAuthorize
        platform.SSOSetting(false);

        sp.setTitle(ShareBeanNew.getShare_title());
        sp.setTitleUrl(ShareBeanNew.getShare_url()); // 标题的超链接
        sp.setText(ShareBeanNew.getShare_content());
        sp.setImageUrl(ShareBeanNew.getShare_log());
        platform.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                PromptManager.ShowCustomToast(pContext, "分享取消");
//                PromptManager.ShowCustomToast(pContext, arg1 + "=====>" + arg2.toString());

            }

            @Override
            public void onComplete(Platform arg0, int arg1,
                                   HashMap<String, Object> arg2) {
                PromptManager.ShowCustomToast(pContext, "分享完成");

            }

            @Override
            public void onCancel(Platform arg0, int arg1) {

            }
        });
        platform.share(sp);

    }

    private void ShareQQ(int type) {
        //判断是否安装微信
        if (!ViewUtils.isQQClientAvailable(pContext)) {
            PromptManager.ShowCustomToast(pContext, "请先安装手机QQ");
            return;
        }
        Platform platform = null;
        ShareParams sp = new ShareParams();
        switch (type) {
            case 1://QQ好友
                platform = ShareSDK.getPlatform(pContext, QQ.NAME);
                sp.setTitle(ShareBeanNew.getShare_title());
                sp.setTitleUrl(ShareBeanNew.getShare_url()); // 标题的超链接
                sp.setText(ShareBeanNew.getShare_content());
                sp.setImageUrl(ShareBeanNew.getShare_log());
//                sp.setSite("发布分享的网站名称");
//                sp.setSiteUrl("发布分享网站的地址");


                break;
            case 2://QQ朋友圈
                platform = ShareSDK.getPlatform(pContext, QZone.NAME);
                sp.setTitle(ShareBeanNew.getShare_title());
                sp.setTitleUrl(ShareBeanNew.getShare_url()); // 标题的超链接
                sp.setText(ShareBeanNew.getShare_content());
                sp.setImageUrl(ShareBeanNew.getShare_log());
                break;
            case 3://新浪微博
//                platform = ShareSDK.getPlatform(pContext, QZone.NAME);
//                sp.setTitle(ShareBeanNew.getShare_title());
//                sp.setTitleUrl(ShareBeanNew.getShare_url()); // 标题的超链接
//                sp.setText(ShareBeanNew.getShare_content());
//                sp.setImageUrl(ShareBeanNew.getShare_log());
                break;

        }
        platform.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                PromptManager.ShowCustomToast(pContext, "分享取消");
                if (null != MShareResultIntface) {
                    MShareResultIntface.ShareResult(0);
                }
            }

            @Override
            public void onComplete(Platform arg0, int arg1,
                                   HashMap<String, Object> arg2) {
                PromptManager.ShowCustomToast(pContext, "分享完成");
                if (null != MShareResultIntface) {

//                    MShareResultIntface.ShareResult(1);
                }
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                if (null != MShareResultIntface) {
                    MShareResultIntface.ShareResult(0);
                }
            }
        });
        platform.share(sp);
    }

    private void Share(int Type) {
        if (!ViewUtils.isWeixinAvilible(pContext)) {
            PromptManager.ShowCustomToast(pContext, "请先安装手机微信");
            return;
        }
        Platform platform = null;
        ShareParams sp = new ShareParams();
        switch (Type) {
            case 1:// 好友分享
                platform = ShareSDK.getPlatform(pContext, Wechat.NAME);
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

                sp.setText(ShareBeanNew.getShare_content());
                sp.setImageUrl(ShareBeanNew.getShare_log());
                sp.setTitle(ShareBeanNew.getShare_title());//
                sp.setUrl(ShareBeanNew.getShare_url());
                break;
            case 2:// 朋友圈分享
                platform = ShareSDK.getPlatform(pContext, WechatMoments.NAME);
                if (IsErWeiMaShare) {//二维码=》图片
                    sp.setShareType(Platform.SHARE_IMAGE);
                } else {//飞二维码=》网页
                    sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);}
                }
                // sp.setText("大兔兔的测试数据");
                // sp.setImageUrl("http://static.freepik.com/free-photo/letter-a-underlined_318-8682.jpg");
                // sp.setTitle("大兔兔的测试数据");//
                // sp.setUrl("www.baidu.com");
                sp.setText(ShareBeanNew.getShare_content());
                sp.setImageUrl(ShareBeanNew.getShare_log());
                sp.setTitle(ShareBeanNew.getShare_title());//
                sp.setUrl(ShareBeanNew.getShare_url());
                break;
            default:
                break;
        }
        platform.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                PromptManager.ShowCustomToast(pContext, "分享取消");
                if (null != MShareResultIntface) {
                    MShareResultIntface.ShareResult(0);
                }
            }

            @Override
            public void onComplete(Platform arg0, int arg1,
                                   HashMap<String, Object> arg2) {
                PromptManager.ShowCustomToast(pContext, "分享完成");
                if (null != MShareResultIntface) {
                    MShareResultIntface.ShareResult(1);
                }
            }

            @Override
            public void onCancel(Platform arg0, int arg1) {
                if (null != MShareResultIntface) {
                    MShareResultIntface.ShareResult(0);
                }
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


    public interface ShareResultIntface {
        //0代表失败 1代表成功
        public void ShareResult(int ResultType);


    }
}
