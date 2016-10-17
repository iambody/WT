package io.vtown.WeiTangApp.fragment.main;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.io.File;
import java.util.HashMap;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcache.BShop;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.shop.BMyShop;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
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
import io.vtown.WeiTangApp.ui.title.center.set.APersonalData;
import io.vtown.WeiTangApp.ui.title.center.wallet.ACenterWallet;
import io.vtown.WeiTangApp.ui.title.loginregist.ARealIdauth;
import io.vtown.WeiTangApp.ui.title.loginregist.bindcode_three.ANewBindCode;
import io.vtown.WeiTangApp.ui.title.shop.channel.AInviteRecord;
import io.vtown.WeiTangApp.ui.ui.ARecyclerShow;

/**
 * Created by datutu on 2016/9/18.
 */
public class FMainCenter extends FBase implements View.OnClickListener ,SwipeRefreshLayout.OnRefreshListener{
    private SwipeRefreshLayout fragment_center_out;
    private RelativeLayout maintab_center_myiv_lay;
    private ImageView maintab_center_cover;
    private CircleImageView maintab_center_myiv;
    private TextView maintab_center_myname;
    //我的订单，，我的钱包
    private View maintab_tab_center_oder, maintab_tab_center_walle;
    //我的show,邀请码，卡券
    private View maintab_center_show, maintab_center_invite_code, maintab_center_card;
    //商品关注，店铺收藏，浏览记录
    private View maintab_center_oder_guanzhu, maintab_center_shop_collect, maintab_center_liulan_history;
    //高斯图片的路径文件
    private File CenterCoverFile;

    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_main_center, null);
        CenterCoverFile = new File(ImagePathConfig.CenterCoverPath(BaseContext));
        EventBus.getDefault().register(this, "OnMainCenterGetMessage", BMessage.class);
        IBaseView();

    }

    private void IBaseView() {
        fragment_center_out= (SwipeRefreshLayout) BaseView.findViewById(R.id.fragment_center_out);
        fragment_center_out.setOnRefreshListener(this);
        fragment_center_out .setColorSchemeResources(R.color.app_fen, R.color.app_fen1, R.color.app_fen2, R.color.app_fen3);
        //
        maintab_center_myiv_lay = (RelativeLayout) BaseView.findViewById(R.id.maintab_center_myiv_lay);
        maintab_center_cover = (ImageView) BaseView.findViewById(R.id.maintab_center_cover);
        maintab_center_myiv = (CircleImageView) BaseView.findViewById(R.id.maintab_center_myiv);
        maintab_center_myname = (TextView) BaseView.findViewById(R.id.maintab_center_myname);
        //我的订单，，我的钱包
        maintab_tab_center_oder = BaseView.findViewById(R.id.maintab_tab_center_oder);
        maintab_tab_center_walle = BaseView.findViewById(R.id.maintab_tab_center_walle);
        //我的show,邀请码，卡券
        maintab_center_show = BaseView.findViewById(R.id.maintab_center_show);
        maintab_center_invite_code = BaseView.findViewById(R.id.maintab_center_invite_code);
        maintab_center_card = BaseView.findViewById(R.id.maintab_center_card);

        //商品关注，店铺收藏，浏览记录
        maintab_center_oder_guanzhu = BaseView.findViewById(R.id.maintab_center_oder_guanzhu);
        maintab_center_shop_collect = BaseView.findViewById(R.id.maintab_center_shop_collect);
        maintab_center_liulan_history = BaseView.findViewById(R.id.maintab_center_liulan_history);
        //设置属性
        maintab_center_myiv.setBorderWidth(10);
        maintab_center_myiv.setBorderColor(getResources().getColor(R.color.transparent6));
//        maintab_center_myiv.setOnClickListener(this);
        maintab_center_myiv_lay.setOnClickListener(this);

//        //图片处理
//        if (CenterCoverFile.exists()) {
//            maintab_center_cover.setImageBitmap(BitmapFactory
//                    .decodeFile(ImagePathConfig.CenterCoverPath(BaseContext)));
//        } else {
//            ImageLoaderUtil.LoadGaosi(BaseContext, Spuit.Shop_Get(BaseContext)
//                    .getAvatar(), maintab_center_cover, R.drawable.item_shangji_iv, 2);
//        }
//        ImageLoaderUtil.Load2(Spuit.Shop_Get(BaseContext).getAvatar(),
//                maintab_center_myiv, R.drawable.error_iv2);

        ImageLoaderUtil.Load2(Spuit.Shop_Get(BaseContext).getAvatar(),
                maintab_center_myiv, R.drawable.error_iv2);

        StrUtils.SetTxt(maintab_center_myname, Spuit.Shop_Get(BaseContext)
                .getSeller_name());
        //Native处理
        SetItemContent(maintab_center_show, R.string.center_show, R.drawable.center_iv1);


        SetItemContent(maintab_center_card, R.string.center_kaquan,
                R.drawable.center_iv5);
        //Native下边
        SetItemContent(maintab_center_oder_guanzhu, R.string.center_good_guanzhu,
                R.drawable.center_iv6);
        SetItemContent(maintab_center_shop_collect, R.string.center_shop_collect,
                R.drawable.center_iv7);
        SetItemContent(maintab_center_liulan_history, R.string.center_jilu,
                R.drawable.center_iv8);
        // 上边两个
        SetCommentIV("我的订单", R.drawable.shop_grad2, maintab_tab_center_oder);
        SetCommentIV("我的钱包", R.drawable.center_wallet, maintab_tab_center_walle);
        MyResume();
    }

    @Override
    public void InitCreate(Bundle d) {

    }

    public void OnMainCenterGetMessage(BMessage mymessage) {
        {

            int messageType = mymessage.getMessageType();
            BShop myBShop = Spuit.Shop_Get(BaseContext);
            switch (messageType) {
                case BMessage.Tage_Main_To_ShowGaoSi:
                    if (!StrUtils.isEmpty(myBShop.getCover()))
                        ImageLoaderUtil.LoadGaosi(BaseContext,
                                Spuit.Shop_Get(BaseContext).getAvatar(), maintab_center_cover,
                                R.drawable.item_shangji_iv, 2);


                case BMessage.Tage_Shop_data_cover_change:
                    if (!StrUtils.isEmpty(myBShop.getAvatar())) {
                        ImageLoaderUtil.Load2(Spuit.Shop_Get(BaseContext).getAvatar(),
                                maintab_center_myiv, R.drawable.testiv);
                        ImageLoaderUtil.LoadGaosi(BaseContext,
                                Spuit.Shop_Get(BaseContext).getAvatar(), maintab_center_cover,
                                R.drawable.error_iv1, 2);
                    }
                    break;
                case BMessage.Tage_Shop_data_shopname_change:
                    StrUtils.SetTxt(maintab_center_myname, Spuit.Shop_Get(BaseContext)
                            .getSeller_name());

                    break;

                default:
                    break;
            }
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
        ((TextView) V.findViewById(R.id.comment_ivtxt_txt)).setTextAppearance(
                BaseContext, R.style.AudioFileInfoOverlayText1);

        // style="@style/AudioFileInfoOverlayText"
        V.setOnClickListener(this);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
        } else {
            MyResume();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            MyResume();
//        } else {
//            //相当于Fragment的onPause
//        }
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
        if (StrUtils.isEmpty(Spuit.Shop_Get(BaseContext).getAvatar())
                && StrUtils.isEmpty(Spuit.Shop_Get(BaseContext).getId())) {
            IData(INITIALIZE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 获取商铺的信息
     */
    private void IData(int Type) {
        SetTitleHttpDataLisenter(this);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("seller_id", Spuit.User_Get(BaseContext).getSeller_id());
        FBGetHttpData(map, Constants.MyShop, Request.Method.GET, 0, Type);
    }

    @Override
    public void getResult(int Code, String Msg, BComment Data) {
        {
            if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                onError(Msg, Data.getHttpLoadType());
                return;
            }

            BMyShop mBShop = new BMyShop();
            mBShop = JSON.parseObject(Data.getHttpResultStr(), BMyShop.class);
            BShop MyBShop = mBShop.getBase();
            MyBShop.setSubCounter(mBShop.getSubCounter());
            MyBShop.setTeamCounter(mBShop.getTeamCounter());
            MyBShop.setTodayVisitor(mBShop.getTodayVisitor());
            MyBShop.setTodayIncome(mBShop.getTodayIncome());
            MyBShop.setTodaySales(mBShop.getTodaySales());
            MyBShop.setTotalIncome(mBShop.getTotalIncome());
            Spuit.Shop_Save(BaseContext, MyBShop);
            ImageLoaderUtil.Load2(Spuit.Shop_Get(BaseContext).getAvatar(),
                    maintab_center_myiv, R.drawable.testiv);

            // File CenterCoverFile = new
            // File(ImagePathConfig.CenterCoverPath(BaseContext));
            if (CenterCoverFile.exists()) {
                maintab_center_cover.setImageBitmap(BitmapFactory
                        .decodeFile(ImagePathConfig.CenterCoverPath(BaseContext)));
            } else {
                ImageLoaderUtil.LoadGaosi(BaseContext, Spuit.Shop_Get(BaseContext)
                        .getAvatar(), maintab_center_cover, R.drawable.item_shangji_iv, 2);
            }

            StrUtils.SetTxt(maintab_center_myname, Spuit.Shop_Get(BaseContext)
                    .getSeller_name());
        }
    }

    @Override
    public void onError(String error, int LoadType) {

    }

    @Override
    public void onClick(View v) {
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
                    PromptManager.SkipActivity(BaseActivity, new Intent(
                            BaseActivity, AWeb.class).putExtra(
                            AWeb.Key_Bean,
                            new BComment(Constants.Homew_JiFen, getResources().getString(R.string.jifenguize))));
                } else {//已激活==》跳转邀请界面
                    PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                            AMyInviteCode.class));
                }

                break;
            case R.id.maintab_center_card://卡券

                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        AMyCoupons.class));
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
    @Override
    public void onRefresh() {
        fragment_center_out.setRefreshing(false);
    }
}
