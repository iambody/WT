package io.vtown.WeiTangApp.ui.comment.order;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.shoporder.BLShopOrderManage;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.comment.view.custom.RefreshLayout;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.shop.odermanger.ADaifaOrderModify;
import io.vtown.WeiTangApp.ui.title.shop.odermanger.ADaifuOrderModify;
import io.vtown.WeiTangApp.ui.title.shop.odermanger.AOderDetail;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-8-18 下午7:09:03
 */
public class AShopOrderManager extends ATitleBase implements OnItemClickListener, RefreshLayout.OnLoadListener {

    /**
     * 标记 分别是 全部，代付款，已付款，待收货，退货,关闭 订单状态 10:代付款 20:已付款 待发货 30:已发货 待收货 40:退款中
     * 50:仲裁处理中 100:已完成 110:已取消 60拒绝退款，70同意退款
     */
    public static final int PAll = 0;
    public static final int PDaiFu = 10;
    public static final int PYiFu = 20;
    public static final int PDaiShou = 30;
    public static final int PTuiKuan = 40;
    public static final int PZhongCai = 60;
    public static final int PAgreeTuiKuan = 70;
    public static final int PClose = 100;
    public static final int PCancel = 110;

    /**
     * 传递tage的key
     */
    public static final String Key_TageStr = "FShopOderMangerkey";
    /**
     * 获取到的key
     */
    private static int Ket_Tage = PAll;

    /**
     * 共同的ListView
     */
    private ListView lv_fall_daifu_common;
    // 获取失败
    private View fragent_oder_nodata_lay;

    /**
     * AP
     */
    private myAdapter myAdapter;

    /**
     * 当前订单状态
     */
    private String order_status1 = "";

    /**
     * last_id
     */
    private String last_id = "";
    /**
     * 用户信息
     */
    private BUser user_Get;

    private int mPosition = -1;
    /**
     * 列表数据
     */
    private List<BLShopOrderManage> dattaa = null;

    // 订单状态 10:代付款 20:已付款 待发货 30:已发货 待收货 40:退款中 50:仲裁处理中 100:已完成 110:已取消
    // 60拒绝退款，70同意退款
    /**
     * 弹出popup的按钮
     */
    private PopupWindow mPopupWindow;
    private TextView tv_center_order_all;
    private TextView tv_center_order_no_pay;
    private TextView tv_center_order_paid;
    private TextView tv_center_order_no_take;
    private TextView tv_center_order_refund_and_arbitrament;
    private TextView tv_center_order_over;

    private Drawable mDrawable;
    private RefreshLayout fragment_shop_order_refrash;


    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_shop_order_manager);
        SetTitleHttpDataLisenter(this);
        user_Get = Spuit.User_Get(getApplicationContext());
        IView();

        ICache();
        IData(LOAD_INITIALIZE, Ket_Tage + "");
        // 注册事件
        EventBus.getDefault().register(this, "OnGetMessage", BMessage.class);
    }

    /**
     * 关闭窗口
     */
    private void closePopupWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * 显示PopupWindow
     *
     * @param view
     */
    private void showPopupWindow(View view) {

        if (mPopupWindow == null) {
            View contentView = View.inflate(AShopOrderManager.this,
                    R.layout.pop_center_order, null);
            LinearLayout ll_center_order_all = (LinearLayout) contentView
                    .findViewById(R.id.ll_center_order_all);
            LinearLayout ll_center_order_no_pay = (LinearLayout) contentView
                    .findViewById(R.id.ll_center_order_no_pay);
            LinearLayout ll_center_order_paid = (LinearLayout) contentView
                    .findViewById(R.id.ll_center_order_paid);

            LinearLayout ll_center_order_no_take = (LinearLayout) contentView
                    .findViewById(R.id.ll_center_order_no_take);
            LinearLayout ll_center_order_refund_and_arbitrament = (LinearLayout) contentView
                    .findViewById(R.id.ll_center_order_refund_and_arbitrament);
            LinearLayout ll_center_order_over = (LinearLayout) contentView
                    .findViewById(R.id.ll_center_order_over);

            tv_center_order_all = (TextView) contentView
                    .findViewById(R.id.tv_center_order_all);
            tv_center_order_no_pay = (TextView) contentView
                    .findViewById(R.id.tv_center_order_no_pay);
            tv_center_order_paid = (TextView) contentView
                    .findViewById(R.id.tv_center_order_paid);
            tv_center_order_no_take = (TextView) contentView
                    .findViewById(R.id.tv_center_order_no_take);
            tv_center_order_refund_and_arbitrament = (TextView) contentView
                    .findViewById(R.id.tv_center_order_refund_and_arbitrament);
            tv_center_order_over = (TextView) contentView
                    .findViewById(R.id.tv_center_order_over);


            View gray_view = contentView.findViewById(R.id.gray_view);
            changeTextColor(tv_center_order_all);
            ll_center_order_all.setOnClickListener(this);
            ll_center_order_no_pay.setOnClickListener(this);
            ll_center_order_paid.setOnClickListener(this);
            ll_center_order_no_take.setOnClickListener(this);
            ll_center_order_refund_and_arbitrament.setOnClickListener(this);
            ll_center_order_over.setOnClickListener(this);

            gray_view.setOnClickListener(this);

            mPopupWindow = new PopupWindow(contentView,
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
            mPopupWindow.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss() {
                    mDrawable = getResources().getDrawable(
                            R.drawable.arrow_down);
                    mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
                            mDrawable.getMinimumHeight());
                    title.setCompoundDrawables(null, null, mDrawable, null);
                }
            });

            mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x0ffffff));
        }

        mDrawable = getResources().getDrawable(R.drawable.arrow_up);
        mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
                mDrawable.getMinimumHeight());
        title.setCompoundDrawables(null, null, mDrawable, null);

        // 设置好参数之后再show
        mPopupWindow.showAsDropDown(view, 0, 29);

    }

    private void changeTextColor(TextView tv) {
        tv_center_order_all.setTextColor(getResources().getColor(R.color.grey));
        tv_center_order_no_pay.setTextColor(getResources().getColor(
                R.color.grey));
        tv_center_order_paid
                .setTextColor(getResources().getColor(R.color.grey));
        tv_center_order_no_take.setTextColor(getResources().getColor(
                R.color.grey));
        tv_center_order_refund_and_arbitrament.setTextColor(getResources()
                .getColor(R.color.grey));
        tv_center_order_over
                .setTextColor(getResources().getColor(R.color.grey));
        tv.setTextColor(getResources().getColor(R.color.app_fen));
    }

    @Override
    protected void InitTile() {


        SetTitleTxt("全部");
        mDrawable = getResources().getDrawable(R.drawable.arrow_down);
        mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
                mDrawable.getMinimumHeight());
        title.setCompoundDrawables(null, null, mDrawable, null);
        title.setOnClickListener(this);
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        switch (Data.getHttpResultTage()) {
            case 0:// 列表数据
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    if (LOAD_INITIALIZE == Data.getHttpLoadType()) {
                        //PromptManager.ShowCustomToast(BaseContext, "暂无订单");
                        switch (Ket_Tage) {


                            case PAll:
                                ShowErrorCanLoad(getResources().getString(R.string.error_null_my_order_all));
                                break;
                            case PDaiFu:
                                ShowErrorCanLoad(getResources().getString(R.string.error_null_my_order_no_pay));
                                break;
                            case PYiFu:
                                ShowErrorCanLoad(getResources().getString(R.string.error_null_my_order_daifa));
                                break;
                            case PDaiShou:
                                ShowErrorCanLoad(getResources().getString(R.string.error_null_my_order_daishou));
                                break;
                            case PTuiKuan:
                                ShowErrorCanLoad(getResources().getString(R.string.error_null_my_order_tuikuan));
                                break;
                            case PClose:
                                ShowErrorCanLoad(getResources().getString(R.string.error_null_my_order_over));
                                break;

                        }
                        ShowErrorIv(R.drawable.pic_maidedingdankongbaiyemian);
                        fragent_oder_nodata_lay.setVisibility(View.VISIBLE);
                        fragent_oder_nodata_lay.setClickable(false);
                        lv_fall_daifu_common.setVisibility(View.GONE);
                        myAdapter.FrashData(new ArrayList<BLShopOrderManage>());
                        if (0 == Ket_Tage) {
                            CacheUtil.Shop_Order_List_Save(getApplicationContext(),
                                    Data.getHttpResultStr());
                        }
                    }
                    if (LOAD_LOADMOREING == Data.getHttpLoadType()) {
                        //lv_fall_daifu_common.stopLoadMore();
                        fragment_shop_order_refrash.setLoading(false);
                        PromptManager.ShowCustomToast(BaseContext, "没有更多订单哦");

                    }
                    if (LOAD_REFRESHING == Data.getHttpLoadType()) {
                        //lv_fall_daifu_common.stopRefresh();
                        fragment_shop_order_refrash.setRefreshing(false);
                        PromptManager.ShowCustomToast(BaseContext, "暂无订单");
                        myAdapter.FrashData(new ArrayList<BLShopOrderManage>());
                    }
                    // lv_fall_daifu_common.stopRefresh();
                    // lv_fall_daifu_common.stopLoadMore();
                    return;
                }
                lv_fall_daifu_common.setVisibility(View.VISIBLE);
                fragent_oder_nodata_lay.setVisibility(View.GONE);
                // PromptManager.showtextLoading(BaseContext, getResources()
                // .getString(R.string.loading));
                dattaa = new ArrayList<BLShopOrderManage>();

                try {
                    dattaa = JSON.parseArray(Data.getHttpResultStr(),
                            BLShopOrderManage.class);
                } catch (Exception e) {
                    //onError("解析错误", 1);
                    return;
                }
                if (dattaa.size() == 0)
                    return;

                last_id = dattaa.get(dattaa.size() - 1).getId();
                if (0 == Ket_Tage) {
                    CacheUtil.Shop_Order_List_Save(getApplicationContext(),
                            Data.getHttpResultStr());
                }
                //PromptManager.closetextLoading();
                switch (Data.getHttpLoadType()) {
                    case LOAD_INITIALIZE:
                        myAdapter.FrashData(dattaa);

                        fragment_shop_order_refrash.setRefreshing(false);

                        if (dattaa.size() == Constants.PageSize2)
                            //lv_fall_daifu_common.ShowFoot();
                            fragment_shop_order_refrash.setCanLoadMore(true);
                        if (dattaa.size() < Constants.PageSize2)
                            //lv_fall_daifu_common.hidefoot();
                            fragment_shop_order_refrash.setCanLoadMore(false);

                        break;
                    case LOAD_REFRESHING:// 刷新数据
                        //lv_fall_daifu_common.stopRefresh();
                        fragment_shop_order_refrash.setRefreshing(false);
                        myAdapter.FrashData(dattaa);
                        if (dattaa.size() == Constants.PageSize2)
                            //lv_fall_daifu_common.ShowFoot();
                            fragment_shop_order_refrash.setCanLoadMore(true);
                        if (dattaa.size() < Constants.PageSize2)
                            //lv_fall_daifu_common.hidefoot();
                            fragment_shop_order_refrash.setCanLoadMore(false);
                        break;
                    case LOAD_LOADMOREING:// 加载更多
                        //lv_fall_daifu_common.stopLoadMore();
                        fragment_shop_order_refrash.setLoading(false);
                        myAdapter.AddFrashData(dattaa);
                        if (dattaa.size() == Constants.PageSize2)
                            //lv_fall_daifu_common.ShowFoot();
                            fragment_shop_order_refrash.setCanLoadMore(true);
                        if (dattaa.size() < Constants.PageSize2)
                            //lv_fall_daifu_common.hidefoot();
                            fragment_shop_order_refrash.setCanLoadMore(false);
                        break;
                }

                break;
            case 1:// 同意退款
                PromptManager.ShowMyToast(BaseContext, "订单进入退款中……");
                last_id = "";
                IData(LOAD_INITIALIZE, Ket_Tage + "");

                break;

            case 2:// 不同意退款
                PromptManager.ShowMyToast(BaseContext, "订单进入仲裁中……");
                last_id = "";
                IData(LOAD_INITIALIZE, Ket_Tage + "");
                break;
        }
    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.ShowCustomToast(BaseContext, error);
        switch (LoadType) {
            case LOAD_INITIALIZE:
                lv_fall_daifu_common.setVisibility(View.GONE);
                fragent_oder_nodata_lay.setVisibility(View.VISIBLE);
                fragent_oder_nodata_lay.setClickable(true);
                ShowErrorCanLoad(getResources().getString(R.string.error_null_noda));
                break;
            case LOAD_REFRESHING:// 刷新数据
                //lv_fall_daifu_common.stopRefresh();
                fragment_shop_order_refrash.setRefreshing(false);

                break;
            case LOAD_LOADMOREING:// 加载更多
                fragment_shop_order_refrash.setLoading(false);
                //lv_fall_daifu_common.stopLoadMore();

                break;

            case 1117:
                lv_fall_daifu_common.setVisibility(View.VISIBLE);
                fragent_oder_nodata_lay.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void NetConnect() {
        NetError.setVisibility(View.GONE);
        last_id = "";
        IData(LOAD_REFRESHING, Ket_Tage + "");
    }

    @Override
    protected void NetDisConnect() {
        NetError.setVisibility(View.VISIBLE);

        closePopupWindow();


    }

    @Override
    protected void SetNetView() {
        SetNetStatuse(NetError);
    }

    @Override
    protected void MyClick(View V) {
        switch (V.getId()) {
            case R.id.title:
                if (CheckNet(BaseContext)) return;
                showPopupWindow(V);
                break;
            case R.id.ll_center_order_all:
                categoryOperate("全部", tv_center_order_all, PAll);
                break;

            case R.id.ll_center_order_no_pay:

                categoryOperate("待付款", tv_center_order_no_pay, PDaiFu);
                break;

            case R.id.ll_center_order_paid:
                categoryOperate("待发货", tv_center_order_paid, PYiFu);
                break;

            case R.id.ll_center_order_no_take:
                categoryOperate("待收货", tv_center_order_no_take, PDaiShou);
                break;

            case R.id.ll_center_order_refund_and_arbitrament:
                categoryOperate("退款/仲裁", tv_center_order_refund_and_arbitrament,
                        PTuiKuan);
                break;

            case R.id.ll_center_order_over:
                categoryOperate("已完成", tv_center_order_over, PClose);
                break;

            case R.id.gray_view:
                closePopupWindow();
                break;

            case R.id.shop_order_manager_nodata_lay:
                last_id = "";
                IData(LOAD_INITIALIZE, Ket_Tage + "");
                break;
            default:
                break;
        }
    }

    @Override
    protected void InItBundle(Bundle bundle) {
    }

    @Override
    protected void SaveBundle(Bundle bundle) {
    }

    private void ICache() {
        String shop_Order_List = CacheUtil.Shop_Order_List_Get(getApplicationContext());
        if (StrUtils.isEmpty(shop_Order_List)) {
            return;
        }
        try {
            dattaa = JSON.parseArray(shop_Order_List, BLShopOrderManage.class);
        } catch (Exception e) {
            return;
        }
        if (PAll == Ket_Tage) {
            myAdapter.FrashData(dattaa);
        }
    }

    private void IData(int loadType, String order_status
    ) {

        if (loadType == LOAD_INITIALIZE) {
            PromptManager.showtextLoading(AShopOrderManager.this, getResources()
                    .getString(R.string.loading));
        }

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("seller_id", user_Get.getSeller_id());
        map.put("order_status", order_status);
        map.put("last_id", last_id);
        FBGetHttpData(map, Constants.SELLER_ORDER_MANAGER, Method.GET, 0,
                loadType);
    }

    /**
     * 同意退款
     *
     * @param seller_order_sn
     * @param type
     */
    private void AgreeTuiKun(String seller_id, String seller_order_sn) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("seller_id", seller_id);
        map.put("seller_order_sn", seller_order_sn);
        FBGetHttpData(map, Constants.Agree_TuiKuan, Method.PUT, 1,
                1117);

    }

    /**
     * 拒绝退款
     *
     * @param seller_order_sn
     * @param type
     */
    private void UnAgreeTuiKun(String seller_id, String seller_order_sn) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("seller_id", seller_id);
        map.put("seller_order_sn", seller_order_sn);
        FBGetHttpData(map, Constants.UnAgree_TuiKuan, Method.PUT, 2,
                1117);

    }

    /**
     * 接收事件
     *
     * @param event
     */

    public void OnGetMessage(BMessage event) {
        int messageType = event.getMessageType();
        if (messageType == BMessage.getTageOrderManageUpdata()) {
            last_id = "";
            IData(LOAD_INITIALIZE, Ket_Tage + "");
        }
    }

    private void IView() {

        fragent_oder_nodata_lay = findViewById(R.id.shop_order_manager_nodata_lay);

        fragment_shop_order_refrash = (RefreshLayout) findViewById(R.id.fragment_shop_order_refrash);
        fragment_shop_order_refrash.setOnLoadListener(this);
        fragment_shop_order_refrash.setColorSchemeResources(R.color.app_fen, R.color.app_fen1, R.color.app_fen2, R.color.app_fen3);
        fragment_shop_order_refrash.setCanLoadMore(false);

        lv_fall_daifu_common = (ListView) findViewById(R.id.lv_shop_order_manager);


        // 设置滚动时不从网上加载图片
        lv_fall_daifu_common
                .setOnScrollListener(getPauseOnScrollListener(new OnScrollListener() {

                    @Override
                    public void onScrollStateChanged(AbsListView view,
                                                     int scrollState) {
                    }

                    @Override
                    public void onScroll(AbsListView view,
                                         int firstVisibleItem, int visibleItemCount,
                                         int totalItemCount) {
                    }
                }));

        myAdapter = new myAdapter(Ket_Tage,
                R.layout.item_fragment_good_manager_outside);
        lv_fall_daifu_common.setAdapter(myAdapter);

        lv_fall_daifu_common.setOnItemClickListener(this);
        fragent_oder_nodata_lay.setOnClickListener(this);

    }

    @Override
    public void OnLoadMore() {
        IData(LOAD_LOADMOREING, Ket_Tage + "");
    }

    @Override
    public void OnFrash() {
        last_id = "";
        IData(LOAD_REFRESHING, Ket_Tage + "");
    }


    // /////////////////////////////////////////////////////////////

    class myAdapter extends BaseAdapter {

        int key;

        /**
         * 填充器
         */
        private LayoutInflater inflater;
        /**
         * 资源id
         */
        private int ResourceId;
        /**
         * 数据
         */
        private List<BLShopOrderManage> datas = new ArrayList<BLShopOrderManage>();

        public myAdapter(int key, int resourceId) {
            super();
            this.key = key;
            this.inflater = LayoutInflater.from(BaseContext);
            ResourceId = resourceId;
        }

        /**
         * 刷新数据
         *
         * @param dass
         */
        public void FrashData(List<BLShopOrderManage> dass) {
            if (datas.size() > 0) {
                datas.clear();
            }
            this.datas = dass;
            this.notifyDataSetChanged();
        }

        /**
         * 加载更多
         */
        public void AddFrashData(List<BLShopOrderManage> dass) {
            this.datas.addAll(dass);
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {

            return datas.size();
        }

        @Override
        public Object getItem(int arg0) {

            return datas.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {

            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {

            OutsideItem outside = null;

            if (arg1 == null) {
                outside = new OutsideItem();
                arg1 = inflater.inflate(ResourceId, null);
                outside.tv_order_numb_tag = ViewHolder.get(arg1,
                        R.id.tv_order_numb_tag);
                outside.btn_modification = ViewHolder.get(arg1,
                        R.id.btn_modification);
                outside.btn_deliver_goods = ViewHolder.get(arg1,
                        R.id.btn_deliver_goods);
                outside.btn_modification_order = ViewHolder.get(arg1,
                        R.id.btn_modification_order);
                outside.tv_order_numb = ViewHolder
                        .get(arg1, R.id.tv_order_numb);
                outside.tv_goods_count_desc = ViewHolder.get(arg1,
                        R.id.tv_goods_count_desc);
                outside.tv_good_price = ViewHolder
                        .get(arg1, R.id.tv_good_price);
                outside.tv_post_price = ViewHolder
                        .get(arg1, R.id.tv_post_price);
                outside.lv_daifu_daifa_common_inside = (CompleteListView) arg1
                        .findViewById(R.id.lv_daifu_daifa_common_inside);
                outside.tv_shop_order_manage_is_cancel = (TextView) arg1
                        .findViewById(R.id.tv_shop_order_manage_is_cancel);
                outside.tv_shop_order_manage_is_over = (TextView) arg1
                        .findViewById(R.id.tv_shop_order_manage_is_over);
                outside.tv_shop_order_nanage_order_sn_numb = (TextView) arg1
                        .findViewById(R.id.tv_shop_order_nanage_order_sn_numb);
                // ll_shop_center_order_manage_order_sn1,ll_shop_center_order_manage_order_sn2
                outside.ll_shop_center_order_manage_order_sn1 = (LinearLayout) arg1
                        .findViewById(R.id.ll_shop_center_order_manage_order_sn1);
                outside.ll_shop_center_order_manage_order_sn2 = (LinearLayout) arg1
                        .findViewById(R.id.ll_shop_center_order_manage_order_sn2);
                outside.tv_shop_order_manage_is_zhongcai = (TextView) arg1
                        .findViewById(R.id.tv_shop_order_manage_is_zhongcai);
                outside.btn_order_optioning = (TextView) arg1
                        .findViewById(R.id.btn_order_optioning);
                arg1.setTag(outside);
            } else {
                outside = (OutsideItem) arg1.getTag();
            }

            BLShopOrderManage blComment = datas.get(arg0);
            int order_status = Integer.parseInt(blComment.getOrder_status());
            String is_send = blComment.getIs_send();//is_send为1的时候是发货商
            String is_refund = blComment.getIs_refund();
            String is_edit = blComment.getIs_edit();
            String is_show = blComment.getIs_show();
            switch (order_status) {
                case 10:
                    outside.btn_modification_order.setText("修改订单");
                    if ("0".equals(is_edit)) {
                        outside.btn_modification_order.setVisibility(View.GONE);
                        outside.btn_order_optioning.setVisibility(View.VISIBLE);
                    } else {
                        outside.btn_modification_order.setVisibility(View.VISIBLE);
                        outside.btn_order_optioning.setVisibility(View.GONE);
                    }
                    outside.btn_modification.setVisibility(View.GONE);
                    outside.btn_deliver_goods.setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_zhongcai
                            .setVisibility(View.GONE);

                    outside.tv_shop_order_manage_is_cancel.setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_over.setVisibility(View.GONE);
                    outside.ll_shop_center_order_manage_order_sn1
                            .setVisibility(View.VISIBLE);
                    outside.ll_shop_center_order_manage_order_sn2
                            .setVisibility(View.GONE);
                    break;
                case 20:// is_edit为0表示不可对订单进行修改操作。为1表示可对订单进行修改地址操作
                    if ("0".equals(is_edit)) {
                        if ("0".equals(is_send)) {
                            outside.btn_order_optioning.setVisibility(View.VISIBLE);
                            outside.btn_deliver_goods.setVisibility(View.GONE);
                            outside.btn_modification.setVisibility(View.GONE);

                        } else {
                            outside.btn_deliver_goods.setVisibility(View.VISIBLE);
                            outside.btn_order_optioning.setVisibility(View.GONE);
                            outside.btn_modification.setVisibility(View.GONE);
                        }

                    } else {
                        if ("0".equals(is_send)) {
                            outside.btn_modification.setVisibility(View.VISIBLE);
                            outside.btn_deliver_goods.setVisibility(View.GONE);
                            outside.btn_order_optioning.setVisibility(View.GONE);
                        } else {
                            outside.btn_modification.setVisibility(View.VISIBLE);
                            outside.btn_deliver_goods.setVisibility(View.VISIBLE);
                            outside.btn_order_optioning.setVisibility(View.GONE);
                        }

                    }
                    outside.btn_order_optioning.setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_zhongcai
                            .setVisibility(View.GONE);
                    outside.btn_modification.setText("修改订单");
                    outside.btn_deliver_goods.setText("发货");
                    outside.btn_modification_order.setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_cancel.setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_over.setVisibility(View.GONE);
                    outside.ll_shop_center_order_manage_order_sn1
                            .setVisibility(View.VISIBLE);
                    outside.ll_shop_center_order_manage_order_sn2
                            .setVisibility(View.GONE);
                    break;

                case 30:
                    outside.btn_order_optioning.setVisibility(View.GONE);
                    outside.btn_modification.setVisibility(View.GONE);
                    outside.btn_deliver_goods.setVisibility(View.GONE);
                    outside.btn_modification_order.setVisibility(View.VISIBLE);
                    outside.tv_shop_order_manage_is_cancel.setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_zhongcai
                            .setVisibility(View.GONE);
                    outside.btn_modification_order.setText("查看物流");
                    outside.tv_shop_order_manage_is_over.setVisibility(View.GONE);
                    outside.ll_shop_center_order_manage_order_sn1
                            .setVisibility(View.VISIBLE);
                    outside.ll_shop_center_order_manage_order_sn2
                            .setVisibility(View.GONE);

                    break;

                case 40:// 买家已申请退款，卖家可同意退款和拒绝退款

                    if ("1".equals(is_edit)) {
                        if ("0".equals(is_refund)) {
                            outside.ll_shop_center_order_manage_order_sn2
                                    .setVisibility(View.GONE);
                            outside.btn_modification.setVisibility(View.GONE);
                            outside.btn_deliver_goods.setVisibility(View.GONE);
                            outside.ll_shop_center_order_manage_order_sn1
                                    .setVisibility(View.VISIBLE);
                            outside.tv_shop_order_manage_is_zhongcai
                                    .setVisibility(View.VISIBLE);
                        } else {
                            outside.ll_shop_center_order_manage_order_sn2
                                    .setVisibility(View.VISIBLE);
                            outside.ll_shop_center_order_manage_order_sn1
                                    .setVisibility(View.GONE);
                            outside.btn_modification.setVisibility(View.VISIBLE);
                            outside.btn_deliver_goods.setVisibility(View.VISIBLE);

                            outside.tv_shop_order_manage_is_zhongcai.setVisibility(View.GONE);
                        }
                    } else {
                        outside.ll_shop_center_order_manage_order_sn2
                                .setVisibility(View.GONE);
                        outside.ll_shop_center_order_manage_order_sn1
                                .setVisibility(View.VISIBLE);
                        outside.btn_modification.setVisibility(View.GONE);
                        outside.btn_deliver_goods.setVisibility(View.GONE);

                        outside.tv_shop_order_manage_is_zhongcai.setVisibility(View.GONE);
                    }



                    outside.btn_order_optioning.setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_cancel.setVisibility(View.GONE);

                    outside.tv_shop_order_manage_is_zhongcai
                            .setVisibility(View.GONE);
                    outside.btn_modification_order.setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_over.setVisibility(View.GONE);
                    outside.btn_modification.setText("同意退款");
                    outside.btn_deliver_goods.setText("拒绝退款");
//                    outside.ll_shop_center_order_manage_order_sn2
//                            .setVisibility(View.VISIBLE);
//                    outside.ll_shop_center_order_manage_order_sn1
//                            .setVisibility(View.GONE);

                    break;

                case 60:// 买家申请退款，卖家拒绝退款，仲裁中状态
                    outside.btn_order_optioning.setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_cancel.setVisibility(View.GONE);
                    outside.btn_modification.setVisibility(View.GONE);
                    outside.btn_deliver_goods.setVisibility(View.GONE);
                    outside.btn_modification_order.setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_zhongcai
                            .setVisibility(View.VISIBLE);
                    outside.tv_shop_order_manage_is_over.setVisibility(View.GONE);
                    outside.ll_shop_center_order_manage_order_sn2
                            .setVisibility(View.GONE);
                    outside.ll_shop_center_order_manage_order_sn1
                            .setVisibility(View.VISIBLE);

                    break;

                case 70:// 卖家同意退款后的订单状态--已完成状态
                    outside.btn_order_optioning.setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_cancel.setVisibility(View.GONE);
                    outside.btn_modification.setVisibility(View.GONE);
                    outside.btn_deliver_goods.setVisibility(View.GONE);
                    outside.btn_modification_order.setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_zhongcai
                            .setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_over
                            .setVisibility(View.VISIBLE);
                    StrUtils.SetTxt(outside.tv_shop_order_manage_is_over, "退款已完成");
                    outside.ll_shop_center_order_manage_order_sn2
                            .setVisibility(View.GONE);
                    outside.ll_shop_center_order_manage_order_sn1
                            .setVisibility(View.VISIBLE);
                    break;

                case 100:// 订单已关闭
                    outside.btn_order_optioning.setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_cancel.setVisibility(View.GONE);
                    outside.btn_modification.setVisibility(View.GONE);
                    outside.btn_deliver_goods.setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_zhongcai
                            .setVisibility(View.GONE);
                    outside.btn_modification_order.setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_over
                            .setVisibility(View.VISIBLE);
                    StrUtils.SetTxt(outside.tv_shop_order_manage_is_over, "订单已完成");
                    outside.ll_shop_center_order_manage_order_sn1
                            .setVisibility(View.VISIBLE);
                    outside.ll_shop_center_order_manage_order_sn2
                            .setVisibility(View.GONE);
                    break;

                case 110:// 订单取消
                    outside.btn_order_optioning.setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_cancel
                            .setVisibility(View.VISIBLE);
                    outside.tv_shop_order_manage_is_zhongcai
                            .setVisibility(View.GONE);
                    outside.btn_modification.setVisibility(View.GONE);
                    outside.btn_deliver_goods.setVisibility(View.GONE);
                    outside.btn_modification_order.setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_over.setVisibility(View.GONE);
                    outside.ll_shop_center_order_manage_order_sn1
                            .setVisibility(View.VISIBLE);
                    outside.ll_shop_center_order_manage_order_sn2
                            .setVisibility(View.GONE);
                    break;

                default:
                    outside.btn_order_optioning.setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_zhongcai
                            .setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_cancel.setVisibility(View.GONE);
                    outside.btn_modification.setVisibility(View.GONE);
                    outside.btn_deliver_goods.setVisibility(View.GONE);
                    outside.btn_modification_order.setVisibility(View.GONE);
                    outside.tv_shop_order_manage_is_over.setVisibility(View.GONE);
                    outside.ll_shop_center_order_manage_order_sn1
                            .setVisibility(View.VISIBLE);
                    outside.ll_shop_center_order_manage_order_sn2
                            .setVisibility(View.GONE);
                    break;
            }

            onClickEvent(outside, blComment);

            if (!StrUtils.isEmpty(datas.get(arg0).getCancel_reason())) {
                StrUtils.SetTxt(outside.tv_shop_order_manage_is_cancel, datas
                        .get(arg0).getCancel_reason());
            } else {
                StrUtils.SetTxt(outside.tv_shop_order_manage_is_cancel, "订单已取消");
            }
            String seller_order_sn = "";
            seller_order_sn = datas.get(arg0).getSeller_order_sn();
            StrUtils.SetTxt(outside.tv_shop_order_nanage_order_sn_numb,
                    seller_order_sn);
            StrUtils.SetTxt(outside.tv_order_numb, seller_order_sn);
//            float total_price = Float.parseFloat(datas.get(arg0)
//                    .getGoods_price())
//                    + Float.parseFloat(datas.get(arg0).getPostage());
//            String price = String.format("%1$s元",
//                    StrUtils.SetTextForMony(total_price + ""));
//            StrUtils.SetTxt(outside.tv_good_price, price);
            int total_price = Integer.parseInt(datas.get(arg0).getGoods_price()) + Integer.parseInt(datas.get(arg0).getPostage());
            StrUtils.SetMoneyFormat(BaseContext, outside.tv_good_price, total_price + "", 17);
            String number = String.format("共%1$s件商品", datas.get(arg0)
                    .getNumber());
            StrUtils.SetTxt(outside.tv_goods_count_desc, number);
            String postage = String.format("(含运费%1$s元)",
                    StrUtils.SetTextForMony(datas.get(arg0).getPostage()));
            float postageF = Float.parseFloat(datas.get(arg0).getPostage());
            StrUtils.SetTxt(outside.tv_post_price, postageF == 0.0f ? "(免邮费)"
                    : postage);

            final List<BLDComment> goods = datas.get(arg0).getGoods();
            mmyAdapter mmyAdapter = new mmyAdapter(
                    R.layout.item_fragment_good_manager_inside, goods);
            outside.lv_daifu_daifa_common_inside.setAdapter(mmyAdapter);
            // 商品Item的点击事件，跳转商品详情页面
            outside.lv_daifu_daifa_common_inside
                    .setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            Intent intent = new Intent(BaseContext,
                                    AGoodDetail.class);
                            intent.putExtra("goodid", goods.get(position)
                                    .getGoods_id());
                            PromptManager.SkipActivity(AShopOrderManager.this, intent);
                        }
                    });

            // // outside.tv_order_numb_tag.setText(getItem(arg0));
            return arg1;
        }

        /**
         * item按钮的点击事件
         *
         * @param outside
         * @param blComment
         * @param type
         */
        private void onClickEvent(OutsideItem outside,
                                  final BLShopOrderManage blComment) {

            final int order_status = Integer.parseInt(blComment
                    .getOrder_status());

            // 修改订单
            outside.btn_modification_order
                    .setOnClickListener(new OnClickListener() {

                        Intent intent = null;

                        @Override
                        public void onClick(View arg0) {
                            switch (order_status) {
                                case 10:// 待付款
                                    if (CheckNet(BaseContext))
                                        return;
                                    intent = new Intent(AShopOrderManager.this,
                                            ADaifuOrderModify.class);

                                    break;

                                case 30:// 已发货
                                    if (CheckNet(AShopOrderManager.this))
                                        return;
                                    intent = new Intent(AShopOrderManager.this,
                                            ADaifaOrderModify.class);

                                    intent.putExtra(
                                            ADaifaOrderModify.Tag,
                                            ADaifaOrderModify.Tage_From_Look_Express);
                                    break;

                                default:
                                    break;
                            }
                            intent.putExtra("seller_id",
                                    blComment.getSeller_id());
                            intent.putExtra("seller_order_sn",
                                    blComment.getSeller_order_sn());
                            PromptManager.SkipActivity((Activity) BaseContext,
                                    intent);

                        }
                    });
            // 发货
            outside.btn_deliver_goods.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    switch (order_status) {
                        case 20:// 发货
                            if (CheckNet(BaseContext))
                                return;
                            Intent intent = new Intent(BaseContext,
                                    ADaifaOrderModify.class);

                            intent.putExtra("seller_id", blComment.getSeller_id());
                            intent.putExtra("seller_order_sn",
                                    blComment.getSeller_order_sn());
                            intent.putExtra(ADaifaOrderModify.Tag,
                                    ADaifaOrderModify.Tage_From_Send);
                            PromptManager.SkipActivity(AShopOrderManager.this,
                                    intent);
                            break;
                        case 40:// 不同意退款
                            if (CheckNet(BaseContext))
                                return;
                            UnAgreeTuiKun(blComment.getSeller_id(),
                                    blComment.getSeller_order_sn());

                        default:
                            break;
                    }

                }
            });
            // 修改按钮
            outside.btn_modification.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    switch (order_status) {
                        case 20:// 修改
                            if (CheckNet(BaseContext))
                                return;
                            Intent intent = new Intent(BaseContext,
                                    ADaifaOrderModify.class);
                            intent.putExtra("seller_id", blComment.getSeller_id());
                            intent.putExtra("seller_order_sn",
                                    blComment.getSeller_order_sn());
                            intent.putExtra(ADaifaOrderModify.Tag,
                                    ADaifaOrderModify.Tage_From_Modify);
                            PromptManager.SkipActivity(AShopOrderManager.this,
                                    intent);
                            break;

                        case 40:// 同意退款
                            if (CheckNet(BaseContext))
                                return;
                            AgreeTuiKun(blComment.getSeller_id(),
                                    blComment.getSeller_order_sn());
                            break;

                        default:
                            break;
                    }

                }
            });

        }

        class mmyAdapter extends BaseAdapter {

            /**
             * 填充器
             */
            private LayoutInflater inflater;
            /**
             * 资源id
             */
            private int ResourceId;
            /**
             * 数据
             */
            private List<BLDComment> data = new ArrayList<BLDComment>();

            public mmyAdapter(int ResourceId, List<BLDComment> goods) {
                super();
                this.ResourceId = ResourceId;
                this.inflater = LayoutInflater.from(BaseContext);
                this.data = goods;
            }

            public void setPos(int pos) {

            }

            @Override
            public int getCount() {

                return data.size();
            }

            @Override
            public Object getItem(int arg0) {

                return data.get(arg0);
            }

            @Override
            public long getItemId(int arg0) {

                return arg0;
            }

            @Override
            public View getView(int arg0, View convertView, ViewGroup arg2) {

                InsideItem inside = null;

                if (convertView == null) {
                    inside = new InsideItem();
                    convertView = inflater.inflate(ResourceId, null);

                    inside.tv_order_manage_good_title = ViewHolder.get(
                            convertView, R.id.tv_order_manage_good_title);
                    inside.tv_order_manage_good_price = ViewHolder.get(
                            convertView, R.id.tv_order_manage_good_price);
                    inside.tv_order_manage_good_count = ViewHolder.get(
                            convertView, R.id.tv_order_manage_good_count);

                    inside.tv_order_manage_good_content = ViewHolder.get(
                            convertView, R.id.tv_order_manage_good_content);

                    inside.tv_order_manage_content_value = ViewHolder.get(
                            convertView, R.id.tv_order_manage_content_value);

                    inside.tv_order_namage_good_total_money = ViewHolder.get(
                            convertView, R.id.tv_order_namage_good_total_money);

                    inside.iv_order_manage_good_icon = ViewHolder.get(
                            convertView, R.id.iv_order_manage_good_icon);

                    inside.iv_order_manage_goods_type = ViewHolder.get(
                            convertView, R.id.iv_order_manage_goods_type);

                    convertView.setTag(inside);

                } else {
                    inside = (InsideItem) convertView.getTag();
                }
                ImageLoaderUtil.Load2(data.get(arg0).getGoods_cover(),
                        inside.iv_order_manage_good_icon, R.drawable.error_iv2);

                String goods_type = data.get(arg0).getGoods_type();
                if (!StrUtils.isEmpty(goods_type)) {
                    if ("0".equals(goods_type)) {
                        inside.iv_order_manage_goods_type
                                .setVisibility(View.GONE);
                    } else {
                        inside.iv_order_manage_goods_type
                                .setVisibility(View.VISIBLE);
                    }
                }

                StrUtils.SetTxt(inside.tv_order_manage_good_title,
                        data.get(arg0).getGoods_name());
                String goods_price = String.format("￥%1$s", StrUtils
                        .SetTextForMony(data.get(arg0).getGoods_price()));
                StrUtils.SetTxt(inside.tv_order_manage_good_price, goods_price);

                String goods_number = String.format("x%1$s", data.get(arg0)
                        .getGoods_number());
                StrUtils.SetTxt(inside.tv_order_manage_good_count, goods_number);
                StrUtils.SetTxt(inside.tv_order_manage_content_value,
                        data.get(arg0).getGoods_standard());
                StrUtils.SetTxt(inside.tv_order_namage_good_total_money, String
                        .format("总价:%1$s元", StrUtils.SetTextForMony(data.get(
                                arg0).getGoods_money())));

                return convertView;
            }
        }

        /**
         * 待发货，待付款外面item的holder
         */
        class OutsideItem {
            public TextView tv_order_numb_tag, tv_order_numb,
                    tv_goods_count_desc, tv_good_price, tv_post_price;
            public TextView btn_modification, btn_deliver_goods,
                    btn_modification_order, tv_shop_order_manage_is_cancel,
                    tv_shop_order_manage_is_over,
                    tv_shop_order_nanage_order_sn_numb,
                    tv_shop_order_manage_is_zhongcai, btn_order_optioning;
            public CompleteListView lv_daifu_daifa_common_inside;
            public LinearLayout ll_shop_center_order_manage_order_sn1,
                    ll_shop_center_order_manage_order_sn2;

        }

        /**
         * 待发货，待付款里面item的holder
         */
        class InsideItem {
            public TextView tv_order_manage_good_title,
                    tv_order_manage_good_content,
                    tv_order_manage_content_value, tv_order_manage_good_price,
                    tv_order_namage_good_total_money,
                    tv_order_manage_good_count;
            public ImageView iv_order_manage_good_icon,
                    iv_order_manage_goods_type;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (CheckNet(BaseContext))
            return;
        int count = myAdapter.getCount();
        if (count > 0) {
            BLShopOrderManage blComment = (BLShopOrderManage) myAdapter
                    .getItem(arg2);
            int order_status = Integer.parseInt(blComment.getOrder_status());
            Intent intent = null;
            if (100 == order_status) {
                intent = new Intent(AShopOrderManager.this, ADaifaOrderModify.class);
                intent.putExtra(ADaifaOrderModify.Tag,
                        ADaifaOrderModify.Tage_From_Look_Detail_Close);

            } else {
                intent = new Intent(AShopOrderManager.this, AOderDetail.class);
                intent.putExtra("order_status", order_status);
                intent.putExtra("is_send", blComment.getIs_send());
                intent.putExtra("is_edit", blComment.getIs_edit());
                intent.putExtra("is_refund",blComment.getIs_refund());

            }

            intent.putExtra("seller_id", blComment.getSeller_id());
            intent.putExtra("seller_order_sn", blComment.getSeller_order_sn());

            PromptManager.SkipActivity(AShopOrderManager.this, intent);
        }

    }


    private void categoryOperate(String title, TextView view, int oprateType) {


        changeTextColor(view);
        SetTitleTxt(title);
        closePopupWindow();

        if (Ket_Tage == oprateType) {
            closePopupWindow();
        } else {
            if (dattaa != null && dattaa.size() > 0) {
                dattaa.clear();
            }
            last_id = "";
            Ket_Tage = oprateType;
            IData(LOAD_INITIALIZE, Ket_Tage + "");
        }

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        last_id = "";
        Ket_Tage = PAll;
        mPopupWindow = null;
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {

        }
    }

}
