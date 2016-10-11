package io.vtown.WeiTangApp.ui.title;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BDGoodDetailInstantInfo;
import io.vtown.WeiTangApp.bean.bcomment.easy.BGoodDetail;
import io.vtown.WeiTangApp.bean.bcomment.easy.gooddetail.BDataGood;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DateUtils;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.ImageCycleView;
import io.vtown.WeiTangApp.comment.view.ImageCycleView.ImageCycleViewListener;
import io.vtown.WeiTangApp.comment.view.ScrollDistanceScrollView;
import io.vtown.WeiTangApp.comment.view.ScrollDistanceScrollView.OnGetDistanceListener;
import io.vtown.WeiTangApp.comment.view.pop.PPurchase;
import io.vtown.WeiTangApp.comment.view.pop.PPurchase.OnPopupStutaChangerListener;
import io.vtown.WeiTangApp.comment.view.pop.PReturnRule;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.AGoodShow;
import io.vtown.WeiTangApp.ui.comment.AVidemplay;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;
import io.vtown.WeiTangApp.ui.comment.im.AChatLoad;
import io.vtown.WeiTangApp.ui.ui.AMainTab;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-20 下午4:15:31
 * @author商品详情
 */
public class AGoodDetail extends ATitleBase {


    ImageView gooddetail_up_title_back;
    TextView gooddetail_up_title;
    /**
     * 购物车的log
     */
    private ImageView right_left_iv;
    /**
     * 外层需要显示的
     */
    private RelativeLayout gooddetails_outlay;

    // 视频的布局
    private RelativeLayout goodsdetail_vido_lay;
    private ImageView goodsdetail_vido_lay_cover_iv;
    private ImageView goodsdetail_vido_lay_controler_iv;
    // banner的布局
    // private RelativeLayout gooddetail_page_lay;
    /**
     * Error的
     */
    private View gooddetail_nodata_lay;
    /**
     * 商品标题
     */
    private TextView tv_good_title;
    /**
     * 查看show
     */
    private RelativeLayout rl_look_show;// 查看show
    private RelativeLayout rl_look_share;// 分享
    /**
     * show头像
     */
    private ImageView iv_show_icon;
    /**
     * show条目数
     */
    private TextView tv_show_count;
    /**
     * 建议零售价
     */
    private TextView tv_suggest_retail_price;
    /**
     * 发货地址
     */
    private TextView tv_send_address;
    /**
     * 邮费
     */
    private TextView tv_freight;
    /**
     * 店铺头像
     */
    private CircleImageView iv_seller_icon;
    /**
     * 店铺名称
     */
    private TextView tv_seller_shop_name;

    /**
     * Banner ViewPage
     */
    // private CustomViewPager MyPager;
    /**
     * Banner ViewPage 中的点
     */
    private ViewGroup DotGroup;

    /**
     * 查看店铺
     */
    private LinearLayout rl_seller;

    private LinearLayout gooddetail_picview;

    /**
     * 买
     */
    private TextView tv_buy;
    /**
     * 一键代卖
     */

    private TextView tv_replace_sell;
    /**
     * banner
     */
    private ImageCycleView gooddetail_banner;

    private List<BDataGood> goods_attr;

    /**
     * 传递的String
     */
    private String GoodsId;
    /**
     * 获取到的整个商品的信息
     */
    private BGoodDetail datas;// BDComment

    /**
     * 是否关注
     */
    private boolean isAttention = false;

    /**
     * 关注
     */
    private ImageView right_iv;
    private ImageView right_right_iv;
    /**
     * 用户信息
     */
    private BUser user_Get;

    /**
     * 图文详情列表
     */
    // private CompleteListView lv_pic_text_detail;
    // private PicTextDetailAdapter picTextDetailAdapter;

    private TextView gooddetail_random_message;
    /**
     * 基础view
     */
    private View mView;
    /**
     * 三个标识
     */
    // 判断是否品牌商品 1=》品牌商品进行判断=》标识是否可以代理
    // 判断是否是品牌商品0=》自营商品进行判断=》是否你可以代卖
    private boolean IsAgen;
    // 如果是品牌商品=》是否可以代理
    private boolean IsDaiLi;
    // 如果是自营商品=》是否可以代卖上架
    private boolean IsShangJia;
    // 是从show页面跳转进来的并且需要显示Pop
    private boolean IsShowPop;

    // 图片还是视频 的标识

    private boolean IsPicDetail = true;

    private boolean fristGetRandomMessage = false;

    public final static String Tage_CaiGou = "iscaigou";
    /**
     * 是否是采购
     */
    private Boolean IsCaiGou = false;

    /**
     * 是否需要随机消息
     */
    private boolean runRandomMessage = false;

    /**
     * 随机消息布局
     */
    private LinearLayout ll_gooddetail_random_message;

    /**
     * 实时订单信息
     */
    private BDGoodDetailInstantInfo info = null;

    private String Order_Create_time = "";

    private String usrname = "";

    /**
     * 回到顶部按钮
     */
    private RelativeLayout rl_to_top;

    /**
     * 联系客服
     */
    private RelativeLayout rl_good_detail_lianxikefu_log;
    /**
     * 收藏
     */
    private RelativeLayout rl_good_detail_shoucang_log;

    /**
     * scrollview
     */
    private ScrollDistanceScrollView good_detail_scrollview;


    private LinearLayout good_title_up;
    private ImageView good_detail_fanyong_log;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_gooddetail);
        mView = LayoutInflater.from(BaseContext).inflate(
                R.layout.activity_gooddetail, null);


        user_Get = Spuit.User_Get(BaseContext);
        IsCaiGou = getIntent().getBooleanExtra(Tage_CaiGou, false);
        EventBus.getDefault().register(this, "getReciverMsg", BMessage.class);
        SetTitleHttpDataLisenter(this);
        IBanner();
        IIBundle();
        IBase();

        IData(GoodsId);
    }


    private void IIBundle() {
        if (!getIntent().getExtras().containsKey("goodid"))
            BaseActivity.finish();
        GoodsId = getIntent().getStringExtra("goodid");

        IsShowPop = getIntent().getBooleanExtra("needshowpop", false);
    }

    private void IData(String GoodId) {
        PromptManager.showtextLoading(BaseContext,
                getResources()
                        .getString(R.string.xlistview_header_hint_loading));
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("goods_id", GoodsId);
        map.put("extend", "1");
        map.put("member_id", user_Get.getId());
        map.put("buy_type", IsCaiGou ? "1" : "0");
        map.put("seller_id", user_Get.getSeller_id());
        FBGetHttpData(map, Constants.GoodDetail, Method.GET, 0, LOAD_INITIALIZE);
    }

    /**
     * 获取实时的用户信息
     */
    private void GetInstantUsrInfo() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("goods_id", GoodsId);
        FBGetHttpData(map, Constants.Order_Instant_Info_Select, Method.GET, 2,
                250);
    }

    private void AttentionGood(boolean isAttention) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", user_Get.getId());
        map.put("goods_id", GoodsId);
        int method_Type = isAttention ? Method.POST : Method.DELETE;
        String Host_Str = isAttention ? Constants.Good_Attention
                : Constants.Good_Attention_Delete;

        FBGetHttpData(map, Host_Str, method_Type, 1, LOAD_REFRESHING);
    }

    private void IBase() {
        gooddetail_up_title_back = (ImageView) findViewById(R.id.gooddetail_up_title_back);
        gooddetail_up_title = (TextView) findViewById(R.id.gooddetail_up_title);
        gooddetail_up_title_back.setOnClickListener(this);

        good_title_up = (LinearLayout) findViewById(R.id.good_title_up);
        gooddetail_picview = (LinearLayout) findViewById(R.id.gooddetail_picview);
        right_right_iv = (ImageView) findViewById(R.id.good_detail_lianxikefu_log);
        right_iv = (ImageView) findViewById(R.id.good_detail_shoucang_log);
        right_left_iv = (ImageView) findViewById(R.id.good_detail_shopbus_log);

        rl_good_detail_lianxikefu_log = (RelativeLayout) findViewById(R.id.rl_good_detail_lianxikefu_log);
        rl_good_detail_shoucang_log = (RelativeLayout) findViewById(R.id.rl_good_detail_shoucang_log);

        gooddetail_random_message = (TextView) findViewById(R.id.gooddetail_random_message);
        ll_gooddetail_random_message = (LinearLayout) findViewById(R.id.ll_gooddetail_random_message);
        good_detail_fanyong_log = (ImageView) findViewById(R.id.good_detail_fanyong_log);
        tv_good_title = (TextView) findViewById(R.id.tv_good_title);
        rl_look_show = (RelativeLayout) findViewById(R.id.rl_look_show);
        rl_look_share = (RelativeLayout) findViewById(R.id.rl_look_share);
        iv_show_icon = (ImageView) findViewById(R.id.iv_show_icon);
        rl_look_share.setOnClickListener(this);
        // 回到顶部按钮
        rl_to_top = (RelativeLayout) findViewById(R.id.rl_to_top);
        good_detail_scrollview = (ScrollDistanceScrollView) findViewById(R.id.good_detail_scrollview);
        rl_to_top.setOnClickListener(this);
        ShowErrorCanLoad("网络调皮,点我重试哦");
        good_detail_scrollview
                .SetOnGetDistanceListener(new OnGetDistanceListener() {

                    @Override
                    public void getDistance(int distance) {
                        if (distance < screenHeight) {
                            rl_to_top.setVisibility(View.GONE);
                            good_title_up.setVisibility(View.GONE);
                        } else {
                            rl_to_top.setVisibility(View.VISIBLE);
                            good_title_up.setVisibility(View.VISIBLE);
                        }
                    }
                });

        // tv_show_count = (TextView) findViewById(R.id.tv_show_count);
        tv_suggest_retail_price = (TextView) findViewById(R.id.tv_suggest_retail_price);
        tv_send_address = (TextView) findViewById(R.id.tv_send_address);
        tv_freight = (TextView) findViewById(R.id.tv_freight);
        iv_seller_icon = (CircleImageView) findViewById(R.id.iv_seller_icon);
        tv_seller_shop_name = (TextView) findViewById(R.id.tv_seller_shop_name);
        // lv_pic_text_detail = (CompleteListView)
        // findViewById(R.id.lv_pic_text_detail);
        // lv_pic_text_detail.setFocusable(false);
        rl_seller = (LinearLayout) findViewById(R.id.rl_seller);

        tv_buy = (TextView) findViewById(R.id.tv_buy);
        tv_replace_sell = (TextView) findViewById(R.id.tv_replace_sell);

        rl_look_show.setOnClickListener(this);
        rl_seller.setOnClickListener(this);
        tv_buy.setOnClickListener(this);
        tv_replace_sell.setOnClickListener(this);
        right_iv.setOnClickListener(this);
        right_right_iv.setOnClickListener(this);
        // 初始化页面时关注设为不可用
        right_iv.setEnabled(false);
        right_left_iv.setOnClickListener(this);
        good_detail_fanyong_log.setOnClickListener(this);



        ShowErrorCanLoad(getResources().getString(R.string.error_null_noda));

    }

    @Override
    protected void InitTile() {
        SetTitleTxt("商品详情");

    }

    private void InItitle() {


    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

        switch (Data.getHttpResultTage()) {
            case 0:// 商品信息
                // TODO可能有提交操作 所以是否判断值为空
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    // DataError(Msg, Data.getHttpLoadType());
//                    BaseActivity.finish();
//                    PromptManager.ShowCustomToast(BaseContext, Msg);

                    DataError("网络异常", LOAD_INITIALIZE);
                    return;
                }

                datas = new BGoodDetail();
                try {
                    datas = JSON.parseObject(Data.getHttpResultStr(),
                            BGoodDetail.class);
                    goods_attr = datas.getGoods_attr();

                } catch (Exception e) {
                    DataError("解析错误", 1);
                    return;

                }

                IDataView(gooddetails_outlay, gooddetail_nodata_lay, NOVIEW_RIGHT);
                RefreshView(datas);
                runRandomMessage = true;
                mHandler.postDelayed(mRandomMessageTimerTask, 3000);
                break;

            case 1:// 关注商品
                right_iv.setImageResource(isAttention ? R.drawable.ic_shoucang_press_good_detail
                        : R.drawable.ic_shoucang_nor_good_detail);
                PromptManager.ShowMyToast(BaseContext, isAttention ? "关注商品成功"
                        : "取消关注商品成功");
                break;

            case 2:// 订单生成实时查询
                // if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                // //info = null;
                // return;
                // }
                info = new BDGoodDetailInstantInfo();
                try {
                    info = JSON.parseObject(Data.getHttpResultStr(),
                            BDGoodDetailInstantInfo.class);
                } catch (Exception e) {

                }

                SetMessage();

                break;
            case 4:// 品牌商=》代理品牌
                PromptManager.ShowCustomToast(BaseContext, Msg);

            default:
                break;
        }

    }

    @Override
    protected void DataError(String error, int LoadTyp) {
        PromptManager.ShowCustomToast(BaseContext, error);

        if (LOAD_INITIALIZE == LoadTyp) {// 刚进来获取数据时候异常就不显示数据
            // 数据初异常时不可用
            rl_good_detail_lianxikefu_log.setVisibility(View.GONE);
            right_right_iv.setVisibility(View.GONE);
            rl_good_detail_shoucang_log.setVisibility(View.GONE);
            right_iv.setVisibility(View.GONE);
            right_iv.setEnabled(false);
            // BaseActivity.finish();
            IDataView(gooddetails_outlay, gooddetail_nodata_lay, NOVIEW_ERROR);
        }

        if (250 == LoadTyp) {
            info = null;
            runRandomMessage = false;
            mHandler.removeCallbacks(mRandomMessageTimerTask);
        }
    }

    /**
     * 刷新页面控件数据
     */
    private void RefreshView(BGoodDetail datas) {
//        StrUtils.SetTxt(gooddetailUpTitle,datas.getTitle());
        StrUtils.SetTxt(gooddetail_up_title, datas.getTitle());
        // InItitle();
        // 判断是否是图片还是视频
        IsPicDetail = datas.getGoods_info().getRtype().equals("0");

        // 运费显示

        // tv_freight
        StrUtils.SetTxt(tv_freight, StrUtils.SetTextForMony(datas.getPostage())
                + "元");
        if (StrUtils.toFloat(datas.getPostage()) == 0f) {
            tv_freight.setText("包邮");
            tv_freight.setTextColor(getResources().getColor(R.color.app_red));
        }
        // 判断是否品牌商品 1=》品牌商品进行判断=》标识是否可以代理
        // 判断是否是品牌商品0=》自营商品进行判断=》是否你可以代卖
        IsAgen = datas.getIs_agent().equals("1");
        // 如果是品牌商品=》是否可以代理
        IsDaiLi = !StrUtils.isEmpty(datas.getIs_dasell())
                && datas.getIs_dasell().equals("0") ? true : false;
        // 如果是自营商品=》是否可以代卖上架
        IsShangJia = !StrUtils.isEmpty(datas.getIs_desell())
                && datas.getIs_desell().equals("0") ? true : false;

        // 判断完毕商品的数据需要显示下边相应的的两个按钮的显示
        if (IsAgen) {// 品牌商品
            tv_replace_sell.setVisibility(View.GONE);
            if (IsDaiLi) {// 品牌商品可以代理
                // tv_replace_sell.setText("代理");
                // rl_look_show.setVisibility(View.GONE);
            } else {// 品牌商品不可以代理
                // tv_replace_sell.setVisibility(View.GONE);
                rl_look_show.setVisibility(View.VISIBLE);
            }

        } else {// 自营品牌商品
            if (IsShangJia) {// 可以上架
                tv_replace_sell.setText("一键代卖");
                // rl_look_show.setVisibility(View.GONE);
            } else {// 已经上架过不可以上架了
                tv_replace_sell.setVisibility(View.GONE);
                rl_look_show.setVisibility(View.VISIBLE);
            }

        }
        if (datas.getSeller_id() != null
                && datas.getSeller_id().equals(user_Get.getSeller_id())) {// 我自己的商品
            tv_buy.setVisibility(View.GONE);
            rl_good_detail_shoucang_log.setVisibility(View.GONE);
            right_iv.setVisibility(View.GONE);
            rl_good_detail_lianxikefu_log.setVisibility(View.GONE);
            right_right_iv.setVisibility(View.GONE);
        }

        // 数据初始化完成时设置为可用
        right_iv.setEnabled(true);
        StrUtils.SetTxt(tv_good_title, datas.getTitle());
        StrUtils.SetTxt(tv_suggest_retail_price,
                StrUtils.SetTextForMony(datas.getSell_price()) + "元");
        String is_collect = datas.getIs_collect();
        if (!StrUtils.isEmpty(is_collect)) {

            isAttention = ("1".equals(is_collect)) ? true : false;
            right_iv.setImageResource(isAttention ? R.drawable.ic_shoucang_press_good_detail
                    : R.drawable.ic_shoucang_nor_good_detail);

        }
        ImageLoaderUtil.Load2(datas.getAvatar(), iv_seller_icon,
                R.drawable.error_iv2);
        StrUtils.SetTxt(tv_seller_shop_name, datas.getSeller_name());
        // String format = String.format("%1$s条", datas.getPostage());
        // StrUtils.SetTxt(tv_show_count, format);

        StrUtils.SetTxt(tv_send_address, datas.getGoods_info().getDeliver());

        if (IsPicDetail) {// 图片轮播的视图
            InItPaGeView(datas.getGoods_info().getRoll());

            goodsdetail_vido_lay.setVisibility(View.GONE);
            gooddetail_banner.setVisibility(View.VISIBLE);
        } else {// 视频展示的view
            goodsdetail_vido_lay.setVisibility(View.VISIBLE);
            gooddetail_banner.setVisibility(View.GONE);
            ImageLoaderUtil.Load2(datas.getCover(),
                    goodsdetail_vido_lay_cover_iv, R.drawable.error_iv1);

        }

        if (null != datas.getGoods_info().getIntro() && datas.getGoods_info().getIntro().size() > 0) {

            final List<String> PicLs = datas.getGoods_info().getIntro();
            // picTextDetailAdapter = new PicTextDetailAdapter(BaseContext,
            // R.layout.item_good_detail_pic_text_list, PicLs);
            // lv_pic_text_detail.setAdapter(picTextDetailAdapter);
            for (int i = 0; i < PicLs.size(); i++) {
                final int Postion = i;
                final ImageView myimage = new ImageView(BaseContext);
                myimage.setClickable(true);
                myimage.setScaleType(ScaleType.FIT_XY);
                myimage.setAdjustViewBounds(true);
                LayoutParams mLayoutParams = new LayoutParams(
                        screenWidth, LayoutParams.WRAP_CONTENT);
                myimage.setLayoutParams(mLayoutParams);
                ImageLoaderUtil.Load2(PicLs.get(i), myimage,
                        R.drawable.error_iv1);
                gooddetail_picview.addView(myimage);

                myimage.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent mIntent = new Intent(BaseContext,
                                AphotoPager.class);
                        mIntent.putExtra("position", Postion);
                        mIntent.putExtra("urls", StrUtils.LsToArray(PicLs));
                        PromptManager.SkipActivity(BaseActivity, mIntent);
                    }
                });
            }

        } else {

        }
        // 是从show跳转进来的
        if (IsShowPop) {
            if (IsAgen) {// 品牌商品

                // if (IsDaiLi) {// 品牌商品可以代理
                // DaiLiGoods(GoodsId);
                // } else {// 品牌商品不可以代理
                // // tv_replace_sell.setVisibility(View.GONE);
                // }

            } else {// 自营品牌商品
                if (IsShangJia) {// 可以上架
                    PPurchase pShowVirtualLibGood2 = new PPurchase(
                            BaseActivity, BaseContext, 200,
                            PPurchase.TYPE_GOOD_DETAIL_REPLACE_SELL, datas,
                            GoodsId, IsCaiGou);
                    pShowVirtualLibGood2.showAtLocation(mView, Gravity.CENTER,
                            0, 0);
                    pShowVirtualLibGood2
                            .setOnPopupStutaChangerListener(new OnPopupStutaChangerListener() {

                                @Override
                                public void getPopupStuta(int stuta) {
                                    switch (stuta) {
                                        case PPurchase.TYPE_ADD_SHOPBUS:// 加入购物车成功

                                            break;
                                        case PPurchase.TYPE_ADD_ONLINE:// 上架成功

                                            break;

                                        default:
                                            break;
                                    }
                                }
                            });

                } else {// 已经上架过不可以上架了
                    // tv_replace_sell.setVisibility(View.GONE);
                }

            }
        }

        if (IsCaiGou) {
            PPurchase pShowVirtualLibGood1 = new PPurchase(BaseActivity,
                    BaseContext, 200, PPurchase.TYPE_GOOD_DETAIL_BUY, datas,
                    GoodsId, IsCaiGou);
            pShowVirtualLibGood1.showAtLocation(mView, Gravity.CENTER, 0, 0);
            pShowVirtualLibGood1
                    .setOnPopupStutaChangerListener(new OnPopupStutaChangerListener() {
                        @Override
                        public void getPopupStuta(int stuta) {
                            switch (stuta) {
                                case PPurchase.TYPE_ADD_SHOPBUS:// 加入购物车成功

                                    break;
                                case PPurchase.TYPE_ADD_ONLINE:// 上架成功

                                    break;

                                default:
                                    break;
                            }
                        }
                    });
        }
        // 判断是否在售中************************************
        if (!StrUtils.isEmpty(datas.getSale_status())
                && datas.getSale_status().equals("100")) {
        } else {// 非在售状态
            tv_buy.setClickable(false);
            tv_buy.setText("非在售中");
            tv_buy.setBackgroundColor(getResources().getColor(R.color.app_gray));
            tv_replace_sell.setClickable(false);
            tv_replace_sell.setVisibility(View.GONE);
        }

    }

    @Override
    protected void NetConnect() {
        NetError.setVisibility(View.GONE);
        if (datas == null)
            IData(GoodsId);

//        mHandler.postDelayed(mRandomMessageTimerTask, 1000);

    }

    @Override
    protected void NetDisConnect() {
        NetError.setVisibility(View.VISIBLE);

    }

    @Override
    protected void SetNetView() {
        SetNetStatuse(NetError);

    }

    @Override
    protected void MyClick(View V) {
        switch (V.getId()) {
            case R.id.gooddetail_up_title_back:
                BaseActivity.finish();
                overridePendingTransition(R.anim.push_rigth_in, R.anim.push_rigth_out);
                break;
            case R.id.tv_buy:
                if (CheckNet(BaseContext))
                    return;
                goPopActivity(AGoodPop.TYPE_GOOD_DETAIL_BUY);
                break;
            case R.id.tv_replace_sell:
                if (CheckNet(BaseContext))
                    return;
                if (IsAgen) {// 品牌商品

                    if (IsDaiLi) {// 品牌商品可以代理
                        DaiLiGoods(GoodsId);
                    } else {// 品牌商品不可以代理
                        // tv_replace_sell.setVisibility(View.GONE);
                    }

                } else {// 自营品牌商品
                    if (IsShangJia) {// 可以上架
                        goPopActivity(AGoodPop.TYPE_GOOD_DETAIL_REPLACE_SELL);
                    }
                }


                break;
            case R.id.rl_look_share:// 分享
                if (CheckNet(BaseContext))
                    return;
                // PromptManager.ShowCustomToast(BaseContext, "分享");
                BNew mBNew = new BNew();
                mBNew.setShare_url(datas.getGoods_url());
                mBNew.setShare_content(datas.getTitle());
                mBNew.setShare_title(datas.getTitle());
                mBNew.setShare_log(datas.getCover());
                ShowP(mView, mBNew);
                break;
            case R.id.rl_look_show:
                if (CheckNet(BaseContext))
                    return;
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        AGoodShow.class).putExtra(
                        AGoodShow.Tage_GoodSid,
                        datas.getGoods_sid().equals("0") ? datas.getId() : datas
                                .getGoods_sid()));
                break;
            case R.id.rl_seller:
                if (CheckNet(BaseContext))
                    return;
                if (null == datas)
                    BaseActivity.finish();
                boolean isbrand = datas.getGoods_sid().equals("0")
                        && datas.getIs_agent().equals("1");
                PromptManager
                        .SkipActivity(BaseActivity, new Intent(BaseActivity,
                                isbrand ? ABrandDetail.class : AShopDetail.class)
                                .putExtra(
                                        BaseKey_Bean,
                                        new BComment(datas.getSeller_id(), datas
                                                .getSeller_name())));
                break;
            case R.id.gooddetail_nodata_lay:// 获取数据失败时候重新获取
                if (CheckNet(BaseContext))
                    return;
                IData(GoodsId);
                break;

            case R.id.good_detail_shoucang_log:
                if (CheckNet(BaseContext))
                    return;
                isAttention = !isAttention;
                AttentionGood(isAttention);
                break;

            case R.id.good_detail_lianxikefu_log:
                if (CheckNet(BaseContext))
                    return;
                // PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                // AChat.class));
                if (!StrUtils.isEmpty(datas.getSeller_id()))
                    PromptManager
                            .SkipActivity(
                                    BaseActivity,
                                    new Intent(BaseActivity, AChatLoad.class)
                                            .putExtra(AChatLoad.Tage_TageId,
                                                    datas.getSeller_id())
                                            .putExtra(AChatLoad.Tage_Name,
                                                    datas.getSeller_name())
                                            .putExtra(AChatLoad.Tage_Iv,
                                                    datas.getAvatar()));
                break;
            case R.id.goodsdetail_vido_lay_controler_iv:// 控制播放
                if (CheckNet(BaseContext))
                    return;
                // datas
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        AVidemplay.class).putExtra(AVidemplay.VidoKey, datas
                        .getGoods_info().getVid()));
                break;
            // 跳转购物车
            case R.id.good_detail_shopbus_log:
                EventBus.getDefault().post(new BMessage(BMessage.Tage_Tab_four));
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                        AMainTab.class).putExtra("a", "1"));

                break;

            case R.id.rl_to_top:
                good_detail_scrollview.fullScroll(View.FOCUS_UP);
                break;

            case R.id.good_detail_fanyong_log://返佣
                showReturnPop();
                break;
            default:
                break;
        }

    }

    private void showReturnPop(){
        PReturnRule pReturnRule = new PReturnRule(BaseContext);
        pReturnRule.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
    }

    private void DaiLiGoods(String goods_id) {
        // GoodsDaiLi
        HashMap<String, String> map = new HashMap<String, String>();

        map.put("goods_id", goods_id);
        map.put("seller_id", user_Get.getId());

        FBGetHttpData(map, Constants.GoodsDaiLi, Method.POST, 3,
                LOAD_LOADMOREING);

    }

    @Override
    protected void InItBundle(Bundle bundle) {
    }

    @Override
    protected void SaveBundle(Bundle bundle) {
    }


    private void goPopActivity(int type) {
        Intent intent = new Intent(BaseContext, AGoodPop.class);
        intent.putExtra("good_info", datas);
        intent.putExtra("Show_type", type);
        intent.putExtra("GoodsId", GoodsId);
        intent.putExtra("IsCaiGou", IsCaiGou);
        startActivity(intent);
    }

    // ******************start*****************Banner*****************start********************
    private void IBanner() {
        // 视频
        goodsdetail_vido_lay = (RelativeLayout) findViewById(R.id.goodsdetail_vido_lay);
        goodsdetail_vido_lay_cover_iv = (ImageView) findViewById(R.id.goodsdetail_vido_lay_cover_iv);
        goodsdetail_vido_lay_controler_iv = (ImageView) findViewById(R.id.goodsdetail_vido_lay_controler_iv);
        goodsdetail_vido_lay_controler_iv.setOnClickListener(this);

        gooddetails_outlay = (RelativeLayout) findViewById(R.id.gooddetails_outlay);
        gooddetail_nodata_lay = findViewById(R.id.gooddetail_nodata_lay);
        gooddetail_nodata_lay.setOnClickListener(this);
        IDataView(gooddetails_outlay, gooddetail_nodata_lay, NOVIEW_INITIALIZE);

        gooddetail_banner = (ImageCycleView) findViewById(R.id.gooddetail_banner);

    }

//    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
//        @Override
//        public void onImageClick(int position, View imageView) {
//            List<String> mStrings = gooddetail_banner.getMyImageUrlList();
//            Intent mIntent = new Intent(BaseContext, AphotoPager.class);
//            mIntent.putExtra("position", position);
//            mIntent.putExtra("urls", StrUtils.LsToArray(mStrings));
//            PromptManager.SkipActivity(BaseActivity, mIntent);
//        }
//
//        @Override
//        public void displayImage(String imageURL, ImageView imageView) {
//            ImageLoaderUtil.Load2(imageURL, imageView, R.drawable.error_iv1);
//        }
//    };

    private void InItPaGeView(List<String> data) {
        ArrayList<String> ssss = (ArrayList<String>) data;
        gooddetail_banner.setImageResources(ssss, ssss, new ImageCycleViewListener() {
                    @Override
                    public void displayImage(String imageURL, ImageView imageView) {
                        ImageLoaderUtil.Load2(imageURL, imageView, R.drawable.error_iv1);
                    }

                    @Override
                    public void onImageClick(int position, View imageView) {
                        List<String> mStrings = gooddetail_banner.getMyImageUrlList();
                        Intent mIntent = new Intent(BaseContext, AphotoPager.class);
                        mIntent.putExtra("position", position);
                        mIntent.putExtra("urls", StrUtils.LsToArray(mStrings));
                        PromptManager.SkipActivity(BaseActivity, mIntent);
                    }
                },
                screenWidth);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gooddetail_banner.startImageCycle();
//        runRandomMessage = true;
//        mHandler.postDelayed(mRandomMessageTimerTask, 3000);
    }

    ;

    @Override
    protected void onRestart() {
        super.onRestart();
//        runRandomMessage = true;
//        mHandler.postDelayed(mRandomMessageTimerTask, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gooddetail_banner.pushImageCycle();
        runRandomMessage = false;
        mHandler.removeCallbacks(mRandomMessageTimerTask);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gooddetail_banner.pushImageCycle();
        runRandomMessage = false;
        mHandler.removeCallbacks(mRandomMessageTimerTask);
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            return;
        }


//        GoodsPollUtils.stopPollingService(BaseContext);
    }


    public void getReciverMsg(BMessage event) {
        int msg_type = event.getMessageType();
        switch (msg_type) {
            case AGoodPop.TYPE_ADD_SHOPBUS:

                break;
            case AGoodPop.TYPE_ADD_ONLINE:

                break;
        }
    }


    private Runnable mRandomMessageTimerTask = new Runnable() {
        @Override
        public void run() {
            GetInstantUsrInfo();


            if (runRandomMessage) {
                if (CheckNet(BaseContext))
                    return;
                mHandler.postDelayed(mRandomMessageTimerTask, 10000);
            }

        }

    };

    private void SetMessage() {
        if (info != null) {
            if (StrUtils.isEmpty(info.getCreat_time())
                    && StrUtils.isEmpty(info.getUsername())) {
                ll_gooddetail_random_message.setVisibility(View.GONE);
            } else {
                ll_gooddetail_random_message.setVisibility(View.VISIBLE);
                if (!fristGetRandomMessage) {
                    setScaleAnima(ll_gooddetail_random_message);
                }
                fristGetRandomMessage = true;

                Order_Create_time = info.getCreat_time();

                usrname = info.getUsername();
                gooddetail_random_message.setText(String.format(
                        "%1$s%2$s的%3$s购买", DateUtils
                                .convertTimeToFormat(StrUtils
                                        .toLong(Order_Create_time)),
                        info.getProvince() + info.getCity(), StrUtils
                                .getRealNameFormatString(info.getUsername())));
            }

        }
    }

    private void setScaleAnima(ViewGroup view) {
        ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setDuration(800);

        animation.setFillAfter(true);// 动画执行完后是否停留在执行完的状态
        view.setAnimation(animation);
    }

    private Handler mHandler = new Handler();



}
