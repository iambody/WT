package io.vtown.WeiTangApp.fragment.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.new_three.BActive;
import io.vtown.WeiTangApp.bean.bcomment.new_three.BLBanner;
import io.vtown.WeiTangApp.bean.bcomment.new_three.BNewHome;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.util.ui.UiHelper;
import io.vtown.WeiTangApp.comment.util.ui.WaveHelper;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.ImageCycleView;
import io.vtown.WeiTangApp.comment.view.WaveView;
import io.vtown.WeiTangApp.comment.view.custom.HomeScrollView;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.ui.comment.AWeb;
import io.vtown.WeiTangApp.ui.title.ABrandDetail;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.AIntegralDetail;
import io.vtown.WeiTangApp.ui.title.AInviteFriendRecord;
import io.vtown.WeiTangApp.ui.title.AReturnDetail;
import io.vtown.WeiTangApp.ui.title.center.myinvitecode.AMyInviteCode;
import io.vtown.WeiTangApp.ui.title.loginregist.AInviteAndApprove;
import io.vtown.WeiTangApp.ui.title.loginregist.bindcode_three.ANewBindCode;
import io.vtown.WeiTangApp.ui.title.mynew.ANew;
import io.vtown.WeiTangApp.ui.title.shop.center.AShopData;
import io.vtown.WeiTangApp.ui.title.zhuanqu.AZhuanQu;
import io.vtown.WeiTangApp.ui.ui.ANewHome;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;
import io.vtown.WeiTangApp.ui.ui.ASouSouGood;
import io.vtown.WeiTangApp.ui.ui.CaptureActivity;

/**
 * Created by datutu on 2016/10/9.
 */

public class FMainNewHome extends FBase implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, HomeScrollView.OnScrollListener {
    @BindView(R.id.fragment_newhome_iv_sao)
    ImageView fragmentNewhomeIvSao; //扫一扫
    @BindView(R.id.fragment_newhome_iv_sou)
    ImageView fragmentNewhomeIvSou; // 搜索
    @BindView(R.id.fragment_newhome_iv_new)
    ImageView fragmentNewhomeIvNew; // 搜索
    @BindView(R.id.fragment_newhome_bg)
    ImageView fragmentNewhomeBg; //我的背景图片
    @BindView(R.id.fragment_newhome_waveview)
    WaveView fragmentNewhomeWaveview; //波浪view
    @BindView(R.id.fragment_newhome_head_iv)
    CircleImageView fragmentNewhomeHeadIv;//头像
    @BindView(R.id.fragment_newhome_banner)
    ImageCycleView fragmentNewhomeBanner;//Banner的句柄
    @BindView(R.id.fragment_newhome_srollviw)
    SwipeRefreshLayout fragmentNewhomeSrollviw;//外层刷新句柄
    @BindView(R.id.fragment_newhome_insrollviw)
    HomeScrollView fragmentNewhomeInsrollviw;//内层监控滑动
    //    @BindView(R.id.fragment_newhome_head_iv_level)
//    ImageView fragmentNewhomeHeadIvLevel;//等级的图片
//    @BindView(R.id.fragment_newhome_banner_title)
//    TextView fragmentNewhomeBannerTitle;//Bannerd的title
    @BindView(R.id.fragment_newhome_banner_lay)
    RelativeLayout fragmentNewhomeBannerLay;//Bannerd的布局
    @BindView(R.id.fragment_newhome_bt_jifem)
    Button fragmentNewhomeBtJifem;//积分规则
    @BindView(R.id.fragment_newhome_bt_fanyong)
    Button fragmentNewhomeBtFanyong;//粉红规则
    @BindView(R.id.fragment_newhome_username)
    TextView fragmentNewhomeUsername;//用户名
    @BindView(R.id.fragment_newhome_usertag)
    TextView fragmentNewhomeUsertag;//用户标签


    private LinearLayout fragmentNewhomeIvLaya;
    //上边的title
    private View fragment_newhome_putitle_lay;
    //邀请人数，我的积分，返佣金额
    private View fragment_newhome_renshu_lay, fragment_newhome_jifen_lay, fragment_newhome_yongjin_lay;
    private TextView Text_fragment_newhome_RenShu, Text_fragment_newhome_JiFen, Text_fragment_newhome_YongJin;
    //签到,邀请，礼包,特卖
    private View fragment_newhome_qian_lay, fragment_newhome_yaoqing_lay, fragment_newhome_libao_lay, fragment_newhome_temai_lay;
    //ButterKnife句柄
    private Unbinder unbinder;
    //WaveHelper句柄
    private WaveHelper mWaveHelper;
    //是否已经签到
    private boolean IsHomeSign;

    //用户信息
    private BUser MyUser;
    //获取到的首页数据
    private BNewHome MBNewHome;
    //判断是否邀请过好友 没有邀请过就直接跳转到邀请界面
    private boolean IsHaveXiaji;

    private boolean IShow = true;

    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_newmainhome, null);
        EventBus.getDefault().register(this, "ReceverBrod", BMessage.class);
        unbinder = ButterKnife.bind(this, BaseView);
        MyUser = Spuit.User_Get(BaseContext);
        SetTitleHttpDataLisenter(this);
        IBase();
    }

    /**
     * 开始处理缓存数据//if存在先显示数据后偷偷加载数据else直接显示数据
     */
    private void ICacheData() {//TODO添加缓存操作
        String CacheData = CacheUtil.NewHome_Get(BaseContext);
        if (StrUtils.isEmpty(CacheData)) {//没有缓存
            INetData(INITIALIZE);
        } else {//有缓存
            MBNewHome = JSON.parseObject(CacheData, BNewHome.class);
            BindHomeData(MBNewHome);
            INetData(REFRESHING);
        }

    }

    /**
     * 请求数据
     */
    private void INetData(int Type) {
        if (Type == INITIALIZE)
            PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.loading));
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", MyUser.getMember_id());
        map.put("seller_id", MyUser.getSeller_id());
        FBGetHttpData(map, Constants.NewHome, Request.Method.GET, 0, Type);
    }

    /**
     * 开始签到
     */
    private void BeginSign() {
        PromptManager.showLoading(BaseContext);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", MyUser.getMember_id());
        FBGetHttpData(map, Constants.NewHomeSign, Request.Method.POST, 10, INITIALIZE);

    }

    /**
     * 填充数据
     */
    private void BindHomeData(BNewHome Data) {
        //上边赋值
        ImageLoaderUtil.Load2(Data.getSellerinfo().getCover(), fragmentNewhomeBg, R.drawable.error_iv1);
        ImageLoaderUtil.Load2(Data.getSellerinfo().getAvatar(), fragmentNewhomeHeadIv, R.drawable.error_iv2);
        StrUtils.SetTxt(Text_fragment_newhome_RenShu, Data.getMySub());//邀请人数
        StrUtils.SetTxt(Text_fragment_newhome_JiFen, Data.getIntegral() + "");//我的积分
        StrUtils.SetTxt(Text_fragment_newhome_YongJin, StrUtils.SetTextForMony(Data.getRebate()));//我的f返佣
        IsHaveXiaji = Data.getMySub().equals("0") ? false : true;
        IPage(Data.getBanner());
//        StrUtils.SetTxt(fragmentNewhomeBannerTitle, Data.getBanner().get(0).getTitle());
        StrUtils.SetTxt(fragmentNewhomeUsername, Data.getSellerinfo().getSeller_name());
        //开始进行level的判定和设置
        LevelSet(Data);
        //开始缓存活动数据
        BActive mActive = new BActive(Data.getActivityid(), Data.getActivitytitle());
        Spuit.Jihuo_set(BaseContext, mActive);

    }

    /**
     * 开始初始化banner
     *
     * @param banner
     */
    private void IPage(final List<BLBanner> banner) {

        //Banner赋值
        fragmentNewhomeBanner.setImageResources(ChangeLs(banner), ChangeLs(banner), new ImageCycleView.ImageCycleViewListener() {
                    @Override
                    public void displayImage(String imageURL, ImageView imageView, int postion) {
                        ImageLoaderUtil.Load2(imageURL, imageView, R.drawable.error_iv1);
//                        StrUtils.SetTxt(fragmentNewhomeBannerTitle, Data.getBanner().get(postion).getTitle());
                    }

                    @Override
                    public void onImageClick(int position, View imageView) {

                        switch (MBNewHome.getBanner().get(position).getAdvert_type()) {

                            case 1:// HT跳转

                                PromptManager.SkipActivity(BaseActivity, new Intent(
                                        BaseActivity, AWeb.class).putExtra(
                                        AWeb.Key_Bean,
                                        new BComment(MBNewHome.getBanner().get(position).getUrl(), StrUtils.NullToStr(MBNewHome.getBanner().get(position)
                                                .getAdvert_type_str()))));
                                break;
                            case 2:// 商品详情页
                                PromptManager.SkipActivity(BaseActivity, new Intent(
                                        BaseContext, AGoodDetail.class).putExtra("goodid",
                                        MBNewHome.getBanner().get(position).getSource_id()));
                                // PromptManager.SkipActivity(BaseActivity, new
                                // Intent(BaseContext, APlayer.class));
                                break;
                            case 3:// 店铺详情页!!!!!!!!!!!!!!!!!!!!!!!!需要修改
                                BComment mBComment = new BComment(MBNewHome.getBanner().get(position).getSource_id(), MBNewHome.getBanner().get(position)
                                        .getTitle());
                                if (MBNewHome.getBanner().get(position).getIs_brand() == 1) {// 品牌店铺
                                    PromptManager.SkipActivity(BaseActivity, new Intent(
                                            BaseActivity, ABrandDetail.class).putExtra(
                                            BaseKey_Bean, mBComment));
                                } else {// 自营店铺
                                    PromptManager.SkipActivity(BaseActivity, new Intent(
                                            BaseActivity, AShopDetail.class).putExtra(
                                            BaseKey_Bean, mBComment));
                                }
                                break;
                            case 4:// 活动详情页
                                BComment mBCommentss = new BComment(MBNewHome.getBanner().get(position).getSource_id(),
                                        MBNewHome.getBanner().get(position).getTitle());
                                PromptManager.SkipActivity(BaseActivity, new Intent(
                                        BaseContext, AZhuanQu.class).putExtra(BaseKey_Bean,
                                        mBCommentss));
                                break;
                            default:
                                // default时候直接展示大图
                                break;
                        }
//                        fragmentNewhomeIvLaya.getBackground().setAlpha(255);
                    }
                },
                screenWidth * 3 / 5);
        //防止冲突
        fragmentNewhomeBanner.GetPage().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        fragmentNewhomeSrollviw.setEnabled(false);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        fragmentNewhomeSrollviw.setEnabled(true);
                        break;
                }
                return false;
            }
        });
        fragmentNewhomeBanner.SetPageChangeListener(new ImageCycleView.onPageChange() {
            @Override
            public void onPagerPostion(int Postion) {
//                StrUtils.SetTxt(fragmentNewhomeBannerTitle, banner.get(Postion).getTitle());
            }
        });
        if (fragmentNewhomeBannerLay.getVisibility() != View.VISIBLE)
            fragmentNewhomeBannerLay.setVisibility(View.VISIBLE);
    }


    private void IBase() {
        fragmentNewhomeSrollviw.setOnRefreshListener(this);
        fragmentNewhomeInsrollviw.setScrolListener(this);
        //上边布局
        fragment_newhome_putitle_lay = ViewHolder.get(BaseView, R.id.fragment_newhome_renshu_lay);
        fragment_newhome_renshu_lay = ViewHolder.get(BaseView, R.id.fragment_newhome_renshu_lay);
        fragment_newhome_jifen_lay = ViewHolder.get(BaseView, R.id.fragment_newhome_jifen_lay);
        fragment_newhome_yongjin_lay = ViewHolder.get(BaseView, R.id.fragment_newhome_yongjin_lay);
        SetDownTitle(fragment_newhome_renshu_lay, getResources().getString(R.string.newhome_renshu));
        SetDownTitle(fragment_newhome_jifen_lay, getResources().getString(R.string.newhome_myjifen));
        SetDownTitle(fragment_newhome_yongjin_lay, getResources().getString(R.string.newhome_fanyong));
        //下边布局
        fragment_newhome_qian_lay = ViewHolder.get(BaseView, R.id.fragment_newhome_qian_lay);
        fragment_newhome_yaoqing_lay = ViewHolder.get(BaseView, R.id.fragment_newhome_yaoqing_lay);
        fragment_newhome_libao_lay = ViewHolder.get(BaseView, R.id.fragment_newhome_libao_lay);
        fragment_newhome_temai_lay = ViewHolder.get(BaseView, R.id.fragment_newhome_temai_lay);
        SetDownLay(fragment_newhome_qian_lay, getResources().getString(R.string.newhome_qiandao), R.drawable.newhome_down_qian);
        SetDownLay(fragment_newhome_yaoqing_lay, getResources().getString(R.string.newhome_yaoqing), R.drawable.newhome_down_freads);
        SetDownLay(fragment_newhome_libao_lay, getResources().getString(R.string.newhome_libao), R.drawable.newhome_down_libao);
        SetDownLay(fragment_newhome_temai_lay, getResources().getString(R.string.newhome_temai), R.drawable.newhome_down_temai);

        fragmentNewhomeHeadIv.setBorderWidth(5);
        fragmentNewhomeHeadIv.setBorderColor(getResources().getColor(R.color.transparent6));
        //开始赋值

        fragmentNewhomeIvLaya = (LinearLayout) (ViewHolder.get(BaseView, R.id.fragment_newhome_putitle_lay)).findViewById(R.id.fragment_newhome_iv_layaa);//ViewHolder.get(BaseView, R.id.fragment_newhome_iv_lay);
        fragmentNewhomeIvLaya.getBackground().mutate().setAlpha(0);
        IWave();
        //解决冲突
        fragmentNewhomeSrollviw.setColorSchemeResources(R.color.app_fen, R.color.app_fen1, R.color.app_fen2, R.color.app_fen3);
        fragmentNewhomeInsrollviw.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                fragmentNewhomeSrollviw.setEnabled(fragmentNewhomeInsrollviw.getScrollY() == 0);
            }
        });
        ICacheData();
    }

    /**
     * 进行level设置
     * BindStatus==>1标识未绑定状态，2标识绑定未激活状态，3标识绑定已激活状态
     * IsStar===>1标识普通店铺，2标识明细店铺
     */
    private void LevelSet(BNewHome Data) {
        //是否激活状态
        if (Data.getIs_activate() == 1) {//已经激活
//            StrUtils.SetTxt(fragmentNewhomeUsertag, getResources().getString(R.string.yijihuo));
            Spuit.IsHaveActive_Set(BaseContext, true);
            StrUtils.SetTxt(fragmentNewhomeUsertag, Data.getMember_level_name());
            //设置shape的背景色和字体颜色
            UiHelper.SetShapeColor(fragmentNewhomeUsertag, getResources().getColor(R.color.app_fen1));
            fragmentNewhomeUsertag.setTextColor(getResources().getColor(R.color.white));
//激活才能邀请好友未激活不能邀请好友
            ((ImageView) fragment_newhome_qian_lay.findViewById(R.id.comment_fragment_newhome_downlay_iv)).setImageResource(R.drawable.newhome_down_qian);
            ((TextView) fragment_newhome_qian_lay.findViewById(R.id.comment_fragment_newhome_downlay_txt)).setTextColor(getResources().getColor(R.color.gray));
        } else {//未激活
            //设置shape的背景色和字体颜色
            UiHelper.SetShapeColor(fragmentNewhomeUsertag, getResources().getColor(R.color.app_line));
            fragmentNewhomeUsertag.setTextColor(getResources().getColor(R.color.gray));


            StrUtils.SetTxt(fragmentNewhomeUsertag, getResources().getString(R.string.weijihuo));
            Spuit.IsHaveActive_Set(BaseContext, false);
            //激活才能邀请好友未激活不能邀请好友
            ((ImageView) fragment_newhome_qian_lay.findViewById(R.id.comment_fragment_newhome_downlay_iv)).setImageResource(R.drawable.newhome_down_qian_pre);
            ((TextView) fragment_newhome_qian_lay.findViewById(R.id.comment_fragment_newhome_downlay_txt)).setTextColor(getResources().getColor(R.color.app_gray));


        }

        //判断绑定 状态
        if (Data.getBindstatus() == 1) {//绑定
            Spuit.IsHaveBind_Set(BaseContext, true);
        } else {//未绑定
            Spuit.IsHaveBind_Set(BaseContext, false);
            StrUtils.SetTxt(fragmentNewhomeUsertag, getResources().getString(R.string.nobind));
        }
//        UiHelper.SetShapeColor(fragmentNewhomeUsertag, getResources().getColor(R.color.app_fen));
//判断是否明细店铺状态
//        if (Data.getIsstar() == 1) {//明星店铺
//            fragmentNewhomeHeadIvLevel.setImageResource(R.drawable.newhome_level2);
//        } else {//普通店铺
//            fragmentNewhomeHeadIvLevel.setImageResource(R.drawable.newhome_level1);
//        }

        //是否已经签到
        if (Data.getIs_attendance() == 1) {// 已经签到
            IsHomeSign = true;
            SetDownLay(fragment_newhome_qian_lay, getResources().getString(R.string.newhome_qiandao1), R.drawable.newhome_down_qian_pre);
            ((TextView) fragment_newhome_qian_lay.findViewById(R.id.comment_fragment_newhome_downlay_txt)).setTextColor(getResources().getColor(R.color.app_line));
        } else {
            IsHomeSign = false;
            SetDownLay(fragment_newhome_qian_lay, getResources().getString(R.string.newhome_qiandao), R.drawable.newhome_down_qian);
            ((TextView) fragment_newhome_qian_lay.findViewById(R.id.comment_fragment_newhome_downlay_txt)).setTextColor(getResources().getColor(R.color.gray));
        }

    }

    /**
     * 设置上边下边的标题
     */
    private void SetDownTitle(View V, String Downtitle) {
        V.setOnClickListener(this);
        ((TextView) V.findViewById(R.id.comment_fragment_newhome_updatalay_downtxt)).setText(Downtitle);
        switch (V.getId()) {
            case R.id.fragment_newhome_renshu_lay:
                Text_fragment_newhome_RenShu = (TextView) V.findViewById(R.id.comment_fragment_newhome_updatalay_uptxt);
                break;
            case R.id.fragment_newhome_jifen_lay:
                Text_fragment_newhome_JiFen = (TextView) V.findViewById(R.id.comment_fragment_newhome_updatalay_uptxt);
                break;
            case R.id.fragment_newhome_yongjin_lay:
                Text_fragment_newhome_YongJin = (TextView) V.findViewById(R.id.comment_fragment_newhome_updatalay_uptxt);
                break;
        }
    }

    //设置下边
    private void SetDownLay(View V, String Downtitle, int ResouceId) {//

        V.setOnClickListener(this);
        ((TextView) V.findViewById(R.id.comment_fragment_newhome_downlay_txt)).setText(Downtitle);
        ((ImageView) V.findViewById(R.id.comment_fragment_newhome_downlay_iv)).setImageResource(ResouceId);
    }


    private void IWave() {
        fragmentNewhomeWaveview.setBorder(0, getResources().getColor(R.color.transparent));
        mWaveHelper = new WaveHelper(fragmentNewhomeWaveview);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (IShow) {
            Log.i("homewave", "显示");
            mWaveHelper.start();
            fragmentNewhomeBanner.startImageCycle();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (IShow) {
            Log.i("homewave", "隐藏");
            mWaveHelper.cancel();
            fragmentNewhomeBanner.pushImageCycle();
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.i("homewave", "隐藏");
            mWaveHelper.cancel();
            fragmentNewhomeBanner.pushImageCycle();
            IShow = false;
        } else {
            Log.i("homewave", "显示");
            mWaveHelper.start();
            fragmentNewhomeBanner.startImageCycle();
            IShow = true;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mWaveHelper.cancel();
//        unbinder.unbind();
        fragmentNewhomeBanner.pushImageCycle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Override
    public void InitCreate(Bundle d) {

    }

    @Override
    public void getResult(int Code, String Msg, BComment Data) {
        switch (Data.getHttpResultTage()) {
            case 0://获取首页数据
                if (Data.getHttpLoadType() == REFRESHING) {
                    fragmentNewhomeSrollviw.setRefreshing(false);
                }
                MBNewHome = JSON.parseObject(Data.getHttpResultStr(), BNewHome.class);
                BindHomeData(MBNewHome);
                CacheUtil.NewHome_Save(BaseContext, Data.getHttpResultStr());
                break;
            case 10://开始签到
                IsHomeSign = true;
                SetDownLay(fragment_newhome_qian_lay, getResources().getString(R.string.newhome_qiandao1), R.drawable.newhome_down_qian_pre);
                //需要进行缓存本地
                INetData(LOADHind);
                break;
        }


    }

    @Override
    public void onError(String error, int LoadType) {
        PromptManager.ShowCustomToast(BaseContext, error);
        if (LoadType == REFRESHING) {
            fragmentNewhomeSrollviw.setRefreshing(false);
        }
    }

//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if (hidden) {
//            fragmentNewhomeIvLaya.getBackground().setAlpha(255);
//        } else {
//            fragmentNewhomeIvLaya.getBackground().setAlpha(0);
//        }
//
//    }

    @OnClick({R.id.fragment_newhome_iv_sao, R.id.fragment_newhome_iv_sou, R.id.fragment_newhome_iv_new, R.id.fragment_newhome_head_iv, R.id.fragment_newhome_bt_jifem, R.id.fragment_newhome_bt_fanyong, R.id.fragment_newhome_usertag})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fragment_newhome_iv_sao:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                        CaptureActivity.class));
                break;
            case R.id.fragment_newhome_iv_sou:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, ASouSouGood.class));
                break;
            case R.id.fragment_newhome_iv_new:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ANew.class));
                break;
            case R.id.fragment_newhome_head_iv:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, AShopData.class));
                break;
            case R.id.fragment_newhome_renshu_lay:

//                if (IsHaveXiaji) {
//                    PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, AInviteFriendRecord.class));
//                    return;
//                }
                if (!Spuit.IsHaveBind_Get(BaseActivity)) {
                    PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                            ANewBindCode.class));
                    return;
                }
                if (Spuit.IsHaveActive_Get(BaseActivity)) {
                    PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, AInviteFriendRecord.class));
                }

                if (Spuit.IsHaveBind_Get(BaseActivity) && !Spuit.IsHaveActive_Get(BaseContext)) {
                    ShowCustomDialog(getResources().getString(R.string.to_Jihuo),
                            getResources().getString(R.string.look_guize), getResources().getString(R.string.to_jihuo),
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
                    return;
                }


                break;
            case R.id.fragment_newhome_jifen_lay:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, AIntegralDetail.class));
                break;
            case R.id.fragment_newhome_yongjin_lay:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, AReturnDetail.class));
                break;
            case R.id.fragment_newhome_temai_lay://特卖专区
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, ANewHome.class));
                break;
//            fragment_newhome_qian_lay, fragment_newhome_yaoqing_lay, fragment_newhome_libao_lay
            case R.id.fragment_newhome_qian_lay://签到
                if (!Spuit.IsHaveBind_Get(BaseContext)) {//未绑定邀请码
                    ShowCustomDialog(getResources().getString(R.string.no_bind_code),
                            getResources().getString(R.string.quxiao), getResources().getString(R.string.bind_code),
                            new IDialogResult() {
                                @Override
                                public void RightResult() {
                                    PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                                            ANewBindCode.class));
                                }

                                @Override
                                public void LeftResult() {
                                }
                            });
                    return;
                }
                if (Spuit.IsHaveBind_Get(BaseContext) && !Spuit.IsHaveActive_Get(BaseContext)) {//绑定邀请码未激活
                    ShowCustomDialog(getResources().getString(R.string.to_Jihuo_toqiandao),
                            getResources().getString(R.string.look_guize), getResources().getString(R.string.to_jihuo),
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

                    return;
                }

                if (Spuit.IsHaveActive_Get(BaseContext)) {//已经激活
                    BeginSign();
                    return;
                }

                break;
            case R.id.fragment_newhome_yaoqing_lay://邀请好友
                if (!Spuit.IsHaveBind_Get(BaseContext)) {//未绑定邀请码
                    ShowCustomDialog(getResources().getString(R.string.no_bind_code),
                            getResources().getString(R.string.quxiao), getResources().getString(R.string.bind_code),
                            new IDialogResult() {
                                @Override
                                public void RightResult() {
                                    PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                                            ANewBindCode.class));
                                }

                                @Override
                                public void LeftResult() {

                                }
                            });

                    return;
                }
                if (Spuit.IsHaveBind_Get(BaseContext) && !Spuit.IsHaveActive_Get(BaseContext)) {//绑定邀请码未激活
                    ShowCustomDialog(getResources().getString(R.string.jihuo_canto_ibit),
                            getResources().getString(R.string.look_guize), getResources().getString(R.string.to_jihuo),
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

                    return;
                }


                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, AMyInviteCode.class));
                break;
            case R.id.fragment_newhome_libao_lay://激活礼包
                BComment mBCommentss = new BComment(MBNewHome.getActivityid(),
                        MBNewHome.getActivitytitle());
                PromptManager.SkipActivity(BaseActivity, new Intent(
                        BaseContext, AZhuanQu.class).putExtra(BaseKey_Bean,
                        mBCommentss));

                break;
            case R.id.fragment_newhome_bt_jifem://积分
                PromptManager.SkipActivity(BaseActivity, new Intent(
                        BaseActivity, AWeb.class).putExtra(
                        AWeb.Key_Bean,
                        new BComment(Constants.Homew_JiFen, getResources().getString(R.string.jifenguize))));
                break;
            case R.id.fragment_newhome_bt_fanyong://返佣
                PromptManager.SkipActivity(BaseActivity, new Intent(
                        BaseActivity, AWeb.class).putExtra(
                        AWeb.Key_Bean,
                        new BComment(Constants.Homew_FanYong, getResources().getString(R.string.fanyongguize))));
                break;
            case R.id.fragment_newhome_usertag://标签
                PromptManager.SkipActivity(BaseActivity, new Intent(
                        BaseActivity, AWeb.class).putExtra(
                        AWeb.Key_Bean,
                        new BComment(Constants.Home_Level, getResources().getString(R.string.dengjiguize))));
                break;
        }
//        fragmentNewhomeIvLaya.setVisibility(View.GONE);
//        fragmentNewhomeIvLaya.getBackground().setAlpha(255);
    }


    @Override
    public void onScroll(int scrollY) {
        if (scrollY < 20) {
            fragmentNewhomeIvLaya.getBackground().mutate().setAlpha(0);
//            PromptManager.ShowCustomToast(BaseContext, "数据" + scrollY);
//            back.getBackground().setAlpha(255);
//            shopping_cart.getBackground().setAlpha(255);
//        } else if (scrollY >= 100 && scrollY < 860) {//860
            Log.i("homejuli", "小于100==>" + scrollY);
        } else if (scrollY >= 20 && scrollY < 160) {//860
            Log.i("homejuli", "大于100小于160==>" + scrollY);
            fragmentNewhomeIvLaya.getBackground().mutate().setAlpha((scrollY + 50));//scrollY-100
//            PromptManager.ShowCustomToast(BaseContext, "数据" + scrollY);
//            back.getBackground().setAlpha(255 - (scrollY-100)/3);
//            shopping_cart.getBackground().setAlpha(255 - (scrollY-100)/3);
        } else {
            Log.i("homejuli", "大于160==>" + scrollY);
            fragmentNewhomeIvLaya.getBackground().mutate().setAlpha(255);
//            back.getBackground().setAlpha(0);
//            shopping_cart.getBackground().setAlpha(0);
        }
    }
//    Scrollview和swip滑动冲突问题

    @Override
    public void onRefresh() {
        INetData(REFRESHING);
//        fragmentNewhomeSrollviw.setRefreshing(false);
    }


    /***
     * banner开始转化
     *
     * @param data
     */
    private ArrayList<String> ChangeLs(List<BLBanner> data) {
        ArrayList<String> Myls = new ArrayList<String>();
        for (int i = 0; i < data.size(); i++) {
            Myls.add(data.get(i).getPic_path());
        }
        return Myls;
    }

    /**
     * 接受广播通知
     */

    public void ReceverBrod(BMessage Bmesage) {

        switch (Bmesage.getMessageType()) {
            case BMessage.Fragment_Home_Bind://绑定状态

                INetData(LOADHind);
                break;
            case BMessage.Fragment_home_pause:
                PromptManager.ShowCustomToast(BaseContext, "消失");
//                fragmentNewhomeIvLaya.getBackground().setAlpha(255);
                break;


        }


    }
}
