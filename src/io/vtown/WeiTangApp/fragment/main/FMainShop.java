package io.vtown.WeiTangApp.fragment.main;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.io.File;
import java.util.HashMap;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcache.BShop;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.shop.BMyShop;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.ImagePathConfig;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.SdCardUtils;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.PullScrollView;
import io.vtown.WeiTangApp.comment.view.RoundAngleImageView;
import io.vtown.WeiTangApp.comment.view.custom.RefreshLayout;
import io.vtown.WeiTangApp.comment.view.listview.SecondStepView;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.ui.afragment.ASellStatistics;
import io.vtown.WeiTangApp.ui.afragment.AShopGoodManger;
import io.vtown.WeiTangApp.ui.comment.order.AShopOrderManager;
import io.vtown.WeiTangApp.ui.comment.order.AShopPurchaseOrder;
import io.vtown.WeiTangApp.ui.title.center.wallet.ACenterWallet;
import io.vtown.WeiTangApp.ui.title.loginregist.ARealIdauth;
import io.vtown.WeiTangApp.ui.title.shop.ABrandCheck;
import io.vtown.WeiTangApp.ui.title.shop.ABrandDaiLi;
import io.vtown.WeiTangApp.ui.title.shop.addgood.ANewAddGood;
import io.vtown.WeiTangApp.ui.title.shop.center.AShopData;
import io.vtown.WeiTangApp.ui.title.shop.channel.AChannel;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;

/**
 * Created by datutu on 2016/9/18.
 */
public class FMainShop extends FBase implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private RefreshLayout fragment_shop_refrash;

    //    private SecondStepView fragment_main_shop_load_head_iv;
//    private AnimationDrawable secondAnimation;
    //背景
//    private ImageView fragment_main_iv_shop_cover;
    //头像
//    private RoundAngleImageView fragment_main_tab_shop_iv;
    //店铺名字
    private TextView fragment_main_tab_shop_name;
    //店铺描述
//    private TextView fragment_main_tab_shop_sign;
    //累计收入的view
    private View fragment_main_tab_shop_all_income_lay;
    private TextView All_InCome;
    //今日收入 ，今日销量，今日访客
    private TextView fragment_main_tab_shop_today_income, fragment_main_tab_shop_today_volume, fragment_main_tab_shop_today_visitor;
    //团队总人数，直属下级
    private TextView fragment_main_tab_shop_team_number, fragment_main_tab_shop_zhijixiashu_number;
    //商品发布，订单管理，渠道管理，采购订单，品牌代理，商品管理，销售统计，店铺预览，品牌入驻
    private View fragment_main_tab_shop_good_fabu, fragment_main_tab_shop_good_oder_guanli, fragment_main_tab_shop_good_qudao_guanli, fragment_main_tab_shop_caigoudan,
            fragment_main_tab_shop_pinpaidaili, fragment_main_tab_shop_good_good_guanli, fragment_main_tab_shop_xiaoshoutongji, fragment_main_tab_shop_lookshop, fragment_main_tab_shop_ruzhu;

    /**
     * 相关联的数据
     */
    //Sp中店铺相关数据
    private BShop myBShop = null;

    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_main_shop, null);
        // 注册事件
        EventBus.getDefault().register(this, "OnMainTabShop", BMessage.class);
        SetTitleHttpDataLisenter(this);
        IBaseView();
        IData(REFRESHING);

    }


    private void IBaseView() {
        fragment_shop_refrash = (RefreshLayout) BaseView.findViewById(R.id.fragment_shop_refrash);
        fragment_shop_refrash.setCanLoadMore(false);
        fragment_shop_refrash.setOnRefreshListener(this);
        fragment_shop_refrash.setColorSchemeResources(R.color.app_fen, R.color.app_fen1, R.color.app_fen2, R.color.app_fen3);
//        fragment_main_tab_shop_iv = (RoundAngleImageView) BaseView.findViewById(R.id.fragment_main_tab_shop_iv);
        fragment_main_tab_shop_name = ViewHolder.get(BaseView, R.id.fragment_main_tab_shop_name);
//        fragment_main_tab_shop_sign = ViewHolder.get(BaseView, R.id.fragment_main_tab_shop_sign);

//设置头像的border
//        fragment_main_tab_shop_iv.setBorderWidth(10);
//        fragment_main_tab_shop_iv.setBorderColor(getResources().getColor(R.color.transparent6));
//        fragment_main_tab_shop_iv.setOnClickListener(this);
        fragment_main_tab_shop_all_income_lay = ViewHolder.get(BaseView, R.id.fragment_main_tab_shop_all_income_lay);
        //总收入
        All_InCome = (TextView) fragment_main_tab_shop_all_income_lay.findViewById(R.id.comment_txtarrow_content);
        ((TextView) fragment_main_tab_shop_all_income_lay.findViewById(R.id.comment_txtarrow_title)).setText(getResources().getString(R.string.shop_all_income));
        fragment_main_tab_shop_all_income_lay.setOnClickListener(this);
        //日收入 ，今日销量，今日访客
        fragment_main_tab_shop_today_income = ViewHolder.get(BaseView, R.id.fragment_main_tab_shop_today_income);
        fragment_main_tab_shop_today_volume = ViewHolder.get(BaseView, R.id.fragment_main_tab_shop_today_volume);
        fragment_main_tab_shop_today_visitor = ViewHolder.get(BaseView, R.id.fragment_main_tab_shop_today_visitor);
        //直接下属

        fragment_main_tab_shop_team_number = ViewHolder.get(BaseView, R.id.fragment_main_tab_shop_team_number);
        fragment_main_tab_shop_zhijixiashu_number = ViewHolder.get(BaseView, R.id.fragment_main_tab_shop_zhijixiashu_number);


        //商品发布，订单管理，渠道管理，
        fragment_main_tab_shop_good_fabu = ViewHolder.get(BaseView, R.id.fragment_main_tab_shop_good_fabu);
        fragment_main_tab_shop_good_oder_guanli = ViewHolder.get(BaseView, R.id.fragment_main_tab_shop_good_oder_guanli);
        fragment_main_tab_shop_good_qudao_guanli = ViewHolder.get(BaseView, R.id.fragment_main_tab_shop_good_qudao_guanli);
        fragment_main_tab_shop_caigoudan = ViewHolder.get(BaseView, R.id.fragment_main_tab_shop_caigoudan);
        fragment_main_tab_shop_pinpaidaili = ViewHolder.get(BaseView, R.id.fragment_main_tab_shop_pinpaidaili);
        fragment_main_tab_shop_good_good_guanli = ViewHolder.get(BaseView, R.id.fragment_main_tab_shop_good_good_guanli);
        fragment_main_tab_shop_xiaoshoutongji = ViewHolder.get(BaseView, R.id.fragment_main_tab_shop_xiaoshoutongji);

        fragment_main_tab_shop_lookshop = ViewHolder.get(BaseView, R.id.fragment_main_tab_shop_lookshop);
        fragment_main_tab_shop_ruzhu = ViewHolder.get(BaseView, R.id.fragment_main_tab_shop_ruzhu);
        //开始给九宫格进行赋值
        SetCommentIV(getResources().getString(R.string.grad1), R.drawable.shop_grad1, fragment_main_tab_shop_good_fabu);
        SetCommentIV(getResources().getString(R.string.grad2), R.drawable.shop_grad2, fragment_main_tab_shop_good_oder_guanli);
        SetCommentIV(getResources().getString(R.string.grad3), R.drawable.shop_grad3, fragment_main_tab_shop_good_qudao_guanli);
        SetCommentIV(getResources().getString(R.string.grad4), R.drawable.shop_grad4, fragment_main_tab_shop_caigoudan);
        SetCommentIV(getResources().getString(R.string.grad5), R.drawable.shop_grad5, fragment_main_tab_shop_pinpaidaili);
        SetCommentIV(getResources().getString(R.string.grad6), R.drawable.shop_grad6, fragment_main_tab_shop_good_good_guanli);
        SetCommentIV(getResources().getString(R.string.grad7), R.drawable.shop_grad7, fragment_main_tab_shop_xiaoshoutongji);
        SetCommentIV(getResources().getString(R.string.grad8), R.drawable.shop_grad8, fragment_main_tab_shop_lookshop);
        SetCommentIV(getResources().getString(R.string.grad9), R.drawable.shop_grad9, fragment_main_tab_shop_ruzhu);


        ShowView(Spuit.Shop_Get(BaseContext));
    }

    private void IData(int Type) {
        fragment_shop_refrash.setRefreshing(true);
        SetTitleHttpDataLisenter(this);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("seller_id", Spuit.User_Get(BaseContext).getSeller_id());
        FBGetHttpData(map, Constants.MyShop, Request.Method.GET, 0, Type);
    }

    /**
     * 上边是IV下边是文字的布局
     */
    public void SetCommentIV(String title, int IvRource, View V) {
        ImageView viessw;
        ((ImageView) V.findViewById(R.id.comment_ivtxt_iv))
                .setBackgroundResource(IvRource);
        ((TextView) V.findViewById(R.id.comment_ivtxt_txt)).setText(title);
        V.setOnClickListener(this);

    }

    //刷新UI
    private void ShowView(BShop myBShop) {
        if (StrUtils.isEmpty(myBShop.getSeller_name()))// 没有缓存数据
            return;
//        ImageLoaderUtil.Load2(StrUtils.NullToStr(myBShop.getAvatar()),
//                fragment_main_tab_shop_iv, R.drawable.testiv);


        StrUtils.SetTxt(fragment_main_tab_shop_today_income,
                StrUtils.SetTextForMony(myBShop.getTodayIncome()));
        StrUtils.SetTxt(fragment_main_tab_shop_today_volume, myBShop.getTodaySales());
        StrUtils.SetTxt(fragment_main_tab_shop_today_visitor, myBShop.getTodayVisitor());

        StrUtils.SetTxt(fragment_main_tab_shop_team_number, myBShop.getTeamCounter());
        StrUtils.SetTxt(fragment_main_tab_shop_zhijixiashu_number, myBShop.getSubCounter());
        // tab_shop_name = (TextView) findViewById(R.id.tab_shop_name);
        // tab_shop_sign
        StrUtils.SetTxt(fragment_main_tab_shop_name, myBShop.getSeller_name());
//        StrUtils.SetTxt(
//                fragment_main_tab_shop_sign,
//                StrUtils.isEmpty(myBShop.getIntro()) ? "您还未描述店铺" : myBShop
//                        .getIntro());
        StrUtils.SetTxt(All_InCome,
                StrUtils.SetTextForMony(myBShop.getTotalIncome()) + "元");
    }

    public void OnMainTabShop(BMessage myMessage) {
        int messageType = myMessage.getMessageType();
        switch (messageType) {
            case BMessage.Tage_Shop_data_shopname_change:
                StrUtils.SetTxt(fragment_main_tab_shop_name, Spuit.Shop_Get(BaseContext)
                        .getSeller_name());
                break;

            default:
                break;
        }

    }

    @Override
    public void InitCreate(Bundle d) {

    }

    @Override
    public void getResult(int Code, String Msg, BComment Data) {
        {
            if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                onError(Msg, Data.getHttpLoadType());
                return;
            }

            if (REFRESHING == Data.getHttpLoadType()) {
                fragment_shop_refrash.setRefreshing(false);
            }
            BMyShop mBShop = new BMyShop();
            mBShop = JSON.parseObject(Data.getHttpResultStr(), BMyShop.class);

            myBShop = mBShop.getBase();
            myBShop.setSubCounter(mBShop.getSubCounter());
            myBShop.setTeamCounter(mBShop.getTeamCounter());
            myBShop.setTodayVisitor(mBShop.getTodayVisitor());
            myBShop.setTodayIncome(mBShop.getTodayIncome());
            myBShop.setTodaySales(mBShop.getTodaySales());
            myBShop.setTotalIncome(mBShop.getTotalIncome());

            ShowView(myBShop);

            Spuit.Shop_Save(BaseContext, myBShop);
        }
    }

    @Override
    public void onError(String error, int LoadType) {
        if (LoadType == REFRESHING) {
            fragment_shop_refrash.setRefreshing(false);
            return;
        }
        IData(INITIALIZE);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 注销事件
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.fragment_main_tab_shop_iv://跳转到我的店铺板块
////            case R.id.fragment_main_iv_shop_cover:
//                if (!StrUtils.isEmpty(Spuit.Shop_Get(BaseContext).getSeller_name())) {// 已经获取数据
//                    PromptManager.SkipActivity(BaseActivity, new Intent(
//                            BaseContext, AShopData.class));
//                } else {// 未获取数据 开始请求
//                    IData(INITIALIZE);
//                }
//                break;
            case R.id.fragment_main_tab_shop_all_income_lay://累计收入
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
            case R.id.fragment_main_tab_shop_good_fabu://发布商品
                if (CheckNet(BaseContext))
                    return;
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                        ANewAddGood.class));

                break;
            case R.id.fragment_main_tab_shop_good_oder_guanli://订单管理
                if (CheckNet(BaseContext))
                    return;
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                        AShopOrderManager.class));

                break;
            case R.id.fragment_main_tab_shop_good_qudao_guanli://渠道管理
                if (CheckNet(BaseContext))
                    return;
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        AChannel.class));
                break;
            case R.id.fragment_main_tab_shop_caigoudan://采购
                if (CheckNet(BaseContext))
                    return;
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                        AShopPurchaseOrder.class));

                break;
            case R.id.fragment_main_tab_shop_pinpaidaili://品牌代理
                if (CheckNet(BaseContext))
                    return;
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                        ABrandDaiLi.class));

                break;
            case R.id.fragment_main_tab_shop_good_good_guanli://商品管理
                if (CheckNet(BaseContext))
                    return;
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                        AShopGoodManger.class));
                break;
            case R.id.fragment_main_tab_shop_xiaoshoutongji://销售统计
                if (CheckNet(BaseContext))
                    return;
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                        ASellStatistics.class));

                break;
            case R.id.fragment_main_tab_shop_lookshop://查看店铺
                if (CheckNet(BaseContext))
                    return;
                BComment mBComment = new BComment(Spuit.Shop_Get(BaseContext)
                        .getId(), Spuit.Shop_Get(BaseContext).getSeller_name());
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        AShopDetail.class).putExtra(BaseKey_Bean, mBComment));

                break;
            case R.id.fragment_main_tab_shop_ruzhu://品牌入驻
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ABrandCheck.class));

                break;

        }

    }

    @Override
    public void onRefresh() {
        IData(REFRESHING);
    }
}
