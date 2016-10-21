package io.vtown.WeiTangApp.fragment.main;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcache.BShop;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.shop.BMyShop;
import io.vtown.WeiTangApp.bean.bcomment.new_three.BActive;
import io.vtown.WeiTangApp.bean.bcomment.new_three.BNewHome;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.ImagePathConfig;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.ui.comment.ACommentList;
import io.vtown.WeiTangApp.ui.comment.AWeb;
import io.vtown.WeiTangApp.ui.comment.order.ACenterMyOrder;
import io.vtown.WeiTangApp.ui.title.AIntegralDetail;
import io.vtown.WeiTangApp.ui.title.AInviteFriendRecord;
import io.vtown.WeiTangApp.ui.title.center.mycoupons.AMyCoupons;
import io.vtown.WeiTangApp.ui.title.center.myinvitecode.ABindCode;
import io.vtown.WeiTangApp.ui.title.center.myinvitecode.AMyInviteCode;
import io.vtown.WeiTangApp.ui.title.center.myshow.ACenterShow;
import io.vtown.WeiTangApp.ui.title.center.myshow.ARecyclerMyShow;
import io.vtown.WeiTangApp.ui.title.center.set.AAboutWt;
import io.vtown.WeiTangApp.ui.title.center.set.AAddressManage;
import io.vtown.WeiTangApp.ui.title.center.set.APersonalData;
import io.vtown.WeiTangApp.ui.title.center.wallet.ACenterWallet;
import io.vtown.WeiTangApp.ui.title.loginregist.ARealIdauth;
import io.vtown.WeiTangApp.ui.title.loginregist.bindcode_three.ANewBindCode;
import io.vtown.WeiTangApp.ui.title.shop.channel.AInviteRecord;
import io.vtown.WeiTangApp.ui.title.zhuanqu.AZhuanQu;
import io.vtown.WeiTangApp.ui.ui.ARecyclerShow;

/**
 * Created by datutu on 2016/9/18.
 */
public class FMainCenter extends FBase implements View.OnClickListener {

    private RelativeLayout maintab_center_myiv_lay;
    private ImageView maintab_center_cover;
    private CircleImageView maintab_center_myiv;
    private TextView maintab_center_myname;
    //我的订单，，我的钱包
    private View maintab_tab_center_oder, maintab_tab_center_walle;
    //我的show,邀请码，卡券,我的地址
    private View maintab_center_show, maintab_center_invite_code, maintab_center_card, maintab_center_address;
    //商品关注，店铺收藏，浏览记录
    private View maintab_center_oder_guanzhu, maintab_center_shop_collect, maintab_center_liulan_history;
    //关于我们
    private View maintab_center_oder_about;
    //高斯图片的路径文件
    private File CenterCoverFile;

    //定时任务进行背景设置********************
    private int BgAlpha = 0;
    private boolean IsUpAlpha = true;
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            myhandler.sendMessage(message);
        }
    };
    Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (BgAlpha >= 255) {
                IsUpAlpha = false;
            }
            if (BgAlpha <= 0) {
                IsUpAlpha = true;
            }
            if (IsUpAlpha) {
                BgAlpha = BgAlpha + 5;
                if (BgAlpha > 255) BgAlpha = 255;
            } else {
                BgAlpha = BgAlpha - 5;
                if (BgAlpha < 0) BgAlpha = 0;
            }
            Log.i("center", "色值==》" + BgAlpha);
            maintab_center_cover.getBackground().mutate().setAlpha(BgAlpha);
            super.handleMessage(msg);
        }
    };

    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_main_center, null);
        CenterCoverFile = new File(ImagePathConfig.CenterCoverPath(BaseContext));
        EventBus.getDefault().register(this, "OnMainCenterGetMessage", BMessage.class);
        IBaseView();
        timer.schedule(task, 120, 120);
    }

    private void IBaseView() {
        //
        maintab_center_myiv_lay = (RelativeLayout) BaseView.findViewById(R.id.maintab_center_myiv_lay);
        maintab_center_cover = (ImageView) BaseView.findViewById(R.id.maintab_center_cover);
        maintab_center_myiv = (CircleImageView) BaseView.findViewById(R.id.maintab_center_myiv);


        maintab_center_myname = (TextView) BaseView.findViewById(R.id.maintab_center_myname);
        //我的订单，，我的钱包
        maintab_tab_center_oder = BaseView.findViewById(R.id.maintab_tab_center_oder);
        maintab_tab_center_walle = BaseView.findViewById(R.id.maintab_tab_center_walle);
        //我的show,邀请码，卡券地址
        maintab_center_show = BaseView.findViewById(R.id.maintab_center_show);
        maintab_center_invite_code = BaseView.findViewById(R.id.maintab_center_invite_code);
        maintab_center_card = BaseView.findViewById(R.id.maintab_center_card);
        maintab_center_address = BaseView.findViewById(R.id.maintab_center_address);
        //商品关注，店铺收藏，浏览记录
        maintab_center_oder_guanzhu = BaseView.findViewById(R.id.maintab_center_oder_guanzhu);
        maintab_center_shop_collect = BaseView.findViewById(R.id.maintab_center_shop_collect);
        maintab_center_liulan_history = BaseView.findViewById(R.id.maintab_center_liulan_history);
        //关于我们
        maintab_center_oder_about = BaseView.findViewById(R.id.maintab_center_oder_about);
        //设置属性
        maintab_center_myiv.setBorderWidth(5);
        maintab_center_myiv.setBorderColor(getResources().getColor(R.color.transparent6));
//        maintab_center_myiv.setOnClickListener(this);
        maintab_center_myiv_lay.setOnClickListener(this);

        if(!StrUtils.isEmpty( CacheUtil.NewHome_Get(BaseContext)))
        ImageLoaderUtil.Load2(JSON.parseObject( CacheUtil.NewHome_Get(BaseContext), BNewHome.class).getSellerinfo().getAvatar(),
                maintab_center_myiv, R.drawable.error_iv2);

        StrUtils.SetTxt(maintab_center_myname, Spuit.Shop_Get(BaseContext)
                .getSeller_name());
        //Native处理
        SetItemContent(maintab_center_show, R.string.center_show, R.drawable.center_iv1);


        SetItemContent(maintab_center_card, R.string.center_kaquan,
                R.drawable.center_iv5);
        SetItemContent(maintab_center_address, R.string.myaddress,
                R.drawable.address_iv);

        //Native下边
        SetItemContent(maintab_center_oder_guanzhu, R.string.center_good_guanzhu,
                R.drawable.center_iv6);
        SetItemContent(maintab_center_shop_collect, R.string.center_shop_collect,
                R.drawable.center_iv7);
        SetItemContent(maintab_center_liulan_history, R.string.center_jilu,
                R.drawable.center_iv8);
        //底部
        SetItemContent(maintab_center_oder_about, R.string.about_w_town,
                R.drawable.tab1_nor);//login_my_log

        // 上边两个
        SetCommentIV("我的订单", R.drawable.shop_grad2, maintab_tab_center_oder);
        SetCommentIV("我的钱包", R.drawable.center_wallet, maintab_tab_center_walle);
        MyResume();
    }

    @Override
    public void InitCreate(Bundle d) {

    }

    public void OnMainCenterGetMessage(BMessage mymessage) {

        int messageType = mymessage.getMessageType();
        BShop myBShop = Spuit.Shop_Get(BaseContext);
        switch (messageType) {


            case BMessage.Tage_Shop_data_cover_change:
                if (!StrUtils.isEmpty(myBShop.getAvatar())) {
                    ImageLoaderUtil.Load2(Spuit.Shop_Get(BaseContext).getAvatar(),
                            maintab_center_myiv, R.drawable.testiv);
                }
                break;
            case BMessage.Tage_Shop_data_shopname_change:
                StrUtils.SetTxt(maintab_center_myname, Spuit.Shop_Get(BaseContext)
                        .getSeller_name());

                break;
            case BMessage.Fragment_Center_ChangStatus:

                MyResume();
                break;
            default:
                break;
        }

    }

    private void SetItemContent(View VV, int ResourceTitle, int ResourceIvId) {
        ((TextView) VV.findViewById(R.id.commentview_center_txt))
                .setText(getResources().getString(ResourceTitle));
        ((ImageView) VV.findViewById(R.id.commentview_center_iv))
                .setImageResource(ResourceIvId);
        VV.setOnClickListener(this);
    }

    /**
     * 上边是IV下边是文字的布局
     *
     * @param title
     * @param IvRource
     * @param V
     */
    public void SetCommentIV(String title, int IvRource, View V) {
        ImageView viessw;
        ((ImageView) V.findViewById(R.id.comment_ivtxt_iv))
                .setBackgroundResource(IvRource);
        ((TextView) V.findViewById(R.id.comment_ivtxt_txt)).setText(title);
//        ((TextView) V.findViewById(R.id.comment_ivtxt_txt)).setTextAppearance(
//                BaseContext, R.style.AudioFileInfoOverlayText1);

        // style="@style/AudioFileInfoOverlayText"
        V.setOnClickListener(this);

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (timer != null) {// 停止timer
                timer.cancel();
                timer = null;
            }
            BgAlpha = 254;
            maintab_center_cover.getBackground().mutate().setAlpha(BgAlpha);
        } else {
//            if (timer == null) {
            timer = new Timer();
//            }
            task = new TimerTask() {
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    myhandler.sendMessage(message);
                }
            };
            timer.schedule(task, 120, 120);
            MyResume();
        }
    }

   

    public void MyResume() {
        int ShowBindTitle;
        if (!Spuit.IsHaveBind_Get(BaseActivity)) {//未绑定
            ShowBindTitle = R.string.bind_yaoqing;//"绑定邀请码";
        } else if (Spuit.IsHaveBind_Get(BaseActivity) && !Spuit.IsHaveActive_Get(BaseContext)) {//已绑定未激活==》跳转激活界面
            ShowBindTitle = R.string.bind_jihuo;//"请激活";
        } else {//已激活==》跳转邀请界面
            ShowBindTitle = R.string.bind_yaoqinghaoyou;//"邀请好友";
        }
        SetItemContent(maintab_center_invite_code, ShowBindTitle,
                R.drawable.center_iv3);
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void getResult(int Code, String Msg, BComment Data) {
    }

    @Override
    public void onError(String error, int LoadType) {

    }

    @Override
    public void onClick(View v) {

        if (timer != null) {// 停止timer
            timer.cancel();
            timer = null;
        }
        switch (v.getId()) {
            case R.id.maintab_center_myiv_lay:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, APersonalData.class));
                break;
            case R.id.maintab_tab_center_oder://我的订单
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ACenterMyOrder.class));
                break;
            case R.id.maintab_tab_center_walle://我的钱包
                boolean isLogin_RenZheng_Set = Spuit
                        .IsLogin_RenZheng_Set(BaseContext);
                if (isLogin_RenZheng_Set) {
                    PromptManager.SkipActivity(BaseActivity, new Intent(
                            BaseActivity, ACenterWallet.class));
                } else {

                    ShowCustomDialog(getResources().getString(R.string.noshimingrenz),
                            getResources().getString(R.string.cancle), "去认证",
                            new IDialogResult() {

                                @Override
                                public void RightResult() {
                                    int from_where = 10;

                                    PromptManager.SkipActivity(BaseActivity, new Intent(
                                            BaseContext, ARealIdauth.class).putExtra(
                                            ARealIdauth.FROM_WHERE_KEY, from_where));
                                }

                                @Override
                                public void LeftResult() {
                                }
                            });

                }
                break;
            case R.id.maintab_center_show://我的show
//                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
//                        ACenterShow.class));
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ARecyclerMyShow.class).putExtra("seller_id", Spuit.User_Get(BaseContext).getSeller_id()));
                break;
            case R.id.maintab_center_invite_code://我的邀请码
                //买东西前提是先绑定，绑定后需要先激活在进行邀请

                if (!Spuit.IsHaveBind_Get(BaseActivity)) {//未绑定
                    PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                            ANewBindCode.class));
                } else if (Spuit.IsHaveBind_Get(BaseActivity) && !Spuit.IsHaveActive_Get(BaseContext)) {//已绑定未激活==》跳转激活界面

                    ShowCustomDialog("请先激活账户",
                            "查看规则", "去激活",
                            new IDialogResult() {
                                @Override
                                public void RightResult() {
                                    BActive maxtive = Spuit.Jihuo_get(BaseContext);
                                    BComment mBCommentss = new BComment(maxtive.getActivityid(),
                                            maxtive.getActivitytitle());
                                    PromptManager.SkipActivity(BaseActivity, new Intent(
                                            BaseContext, AZhuanQu.class).putExtra(BaseKey_Bean,
                                            mBCommentss));
                                }

                                @Override
                                public void LeftResult() {
                                    PromptManager.SkipActivity(BaseActivity, new Intent(
                                            BaseActivity, AWeb.class).putExtra(
                                            AWeb.Key_Bean,
                                            new BComment(Constants.Homew_JiFen, getResources().getString(R.string.jifenguize))));


                                }
                            });


                } else {//已激活==》跳转邀请界面
                    PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                            AMyInviteCode.class));
                }

                break;
            case R.id.maintab_center_card://卡券

                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        AMyCoupons.class));
                break;
            case R.id.maintab_center_address://我的地址
                if (CheckNet(BaseContext))
                    return;
                Intent intentss = new Intent(BaseActivity, AAddressManage.class);
                intentss.putExtra("NeedFinish", false);
                PromptManager.SkipActivity(BaseActivity, intentss);
                break;
            case R.id.maintab_center_oder_guanzhu://商品关注
                Intent intent = new Intent(BaseActivity, ACommentList.class);
                intent.putExtra(ACommentList.Tage_ResultKey,
                        ACommentList.Tage_ACenterOderGuanzhu);

                PromptManager.SkipActivity(BaseActivity, intent);
                break;
            case R.id.maintab_center_shop_collect://店铺收藏
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ACommentList.class).putExtra(ACommentList.Tage_ResultKey,
                        ACommentList.Tage_ACenterShopCollect));
                break;
            case R.id.maintab_center_liulan_history://浏览记录
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ACommentList.class).putExtra(ACommentList.Tage_ResultKey,
                        ACommentList.Tage_ACenterGoodBrowseRecord));

                break;
            case R.id.maintab_center_oder_about:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        AAboutWt.class));
                break;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
        }

    }


}
