package io.vtown.WeiTangApp.ui.comment.order;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
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
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.adapter.BasAdapter;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.centerorder.BLCenterOder;
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
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.account.ACashierDesk;
import io.vtown.WeiTangApp.ui.title.center.myorder.AApplyTuikuan;
import io.vtown.WeiTangApp.ui.title.center.myorder.ACenterMyOrderDetail;
import io.vtown.WeiTangApp.ui.title.center.myorder.ACenterMyOrderNoPayDetail;
import io.vtown.WeiTangApp.ui.ui.AMainTab;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-8-18 下午1:37:57
 */
public class ACenterMyOrder extends ATitleBase implements
        OnItemClickListener, RefreshLayout.OnLoadListener {
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
    public static final String Key_TageStr = "FCenterssOderkey";
    /**
     * 获取到的key
     */
    private static int Ket_Tage = PAll;
    /**
     * 我的订单列表
     */
    private ListView fragment_center_order_ls;
    private View fragent_centeroder_nodata_lay;
    /**
     * 用户信息
     */
    private BUser user_Get;

    /**
     * 当前订单状态
     */
    private String order_status = "0";

    /**
     * last_id
     */
    private String last_id = "";
    /**
     * AP
     */
    private CenterOrderOutsideAdapter centerOrderOutsideAdapter;
    /**
     * 待付款AP
     */
    private CenterOrderNoPayAdapter centerOrderNoPayAdapter;

    /**
     * 是否需要在onResumeg再刷新数据标志
     */
    private boolean NeedRefrashData = false;
    /**
     * 列表数据
     */
    private List<BLCenterOder> order_list;

    /**
     * 弹出popup的按钮
     */
    private PopupWindow popupWindow;
    private TextView tv_center_order_all;
    private TextView tv_center_order_no_pay;
    private TextView tv_center_order_paid;
    private TextView tv_center_order_no_take;
    private TextView tv_center_order_refund_and_arbitrament;
    private TextView tv_center_order_over;

    private Drawable mDrawable;
    private TextView mTitle;
    private RefreshLayout fragment_center_order_refrash;
    private int SelectPosition = -1;

    // @Override
    // public void InItView() {
    //
    // BaseView = LayoutInflater.from(BaseContext).inflate(
    // R.layout.fragment_center_oder, null);
    // SetTitleHttpDataLisenter(this);
    // user_Get = Spuit.User_Get(BaseContext);
    // if (-1 == Ket_Tage)
    // return;
    // IView();
    // ICache();
    // IData(INITIALIZE, order_status);
    //
    // // 注册事件
    // EventBus.getDefault().register(this, "OnGetMessage", BMessage.class);
    //
    // }
    @Override
    protected void InItBaseView() {

        setContentView(R.layout.activity_center_fragment);
        SetTitleHttpDataLisenter(this);
        user_Get = Spuit.User_Get(getApplicationContext());
        IView();

        ICache();

        IData(LOAD_INITIALIZE, Ket_Tage + "");

        // 注册事件
        EventBus.getDefault().register(this, "OnGetMessage", BMessage.class);
    }

    private void ICache() {
        String center_Order_List = CacheUtil.Center_Order_List_Get(getApplicationContext());
        if (StrUtils.isEmpty(center_Order_List)) {
            return;
        }
        try {
            order_list = JSON.parseArray(center_Order_List, BLCenterOder.class);
        } catch (Exception e) {
            return;
        }
        if (PAll == Ket_Tage) {
            centerOrderOutsideAdapter.RefreshData(order_list);
        }
    }

    private void IView() {
        fragent_centeroder_nodata_lay = findViewById(R.id.fragent_centeroder_nodata_lay1);


        fragment_center_order_refrash = (RefreshLayout) findViewById(R.id.fragment_center_order_refrash);
        fragment_center_order_refrash.setOnLoadListener(this);
        fragment_center_order_refrash.setColorSchemeResources(R.color.app_fen, R.color.app_fen1, R.color.app_fen2, R.color.app_fen3);
        fragment_center_order_refrash.setCanLoadMore(false);
        fragment_center_order_ls = (ListView) findViewById(R.id.fragment_center_order_ls1);
        centerOrderOutsideAdapter = new CenterOrderOutsideAdapter(
                R.layout.item_fragment_center_order_outside);
        centerOrderNoPayAdapter = new CenterOrderNoPayAdapter(
                R.layout.item_center_order_no_pay_outside);
        fragment_center_order_ls.setOnItemClickListener(this);

        fragment_center_order_ls.setAdapter(centerOrderOutsideAdapter);

        // 设置滚动时不从网上加载图片
        fragment_center_order_ls
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
        fragment_center_order_ls.setOnItemClickListener(this);
        fragent_centeroder_nodata_lay.setOnClickListener(this);


    }

    /**
     * 关闭窗口
     */
    private void closePopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    /**
     * 显示PopupWindow
     *
     * @param view
     */
    private void showPopupWindow(View view) {

        if (popupWindow == null) {
            View contentView = View.inflate(ACenterMyOrder.this,
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

            popupWindow = new PopupWindow(contentView,
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
            popupWindow.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss() {
                    mDrawable = getResources().getDrawable(
                            R.drawable.arrow_down);
                    mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
                            mDrawable.getMinimumHeight());
                    mTitle.setCompoundDrawables(null, null, mDrawable, null);
                }
            });

            popupWindow.setBackgroundDrawable(new ColorDrawable(0x0ffffff));
        }

        mDrawable = getResources().getDrawable(R.drawable.arrow_up);
        mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
                mDrawable.getMinimumHeight());
        mTitle.setCompoundDrawables(null, null, mDrawable, null);

        // 设置好参数之后再show
        popupWindow.showAsDropDown(view, 0, 29);

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

    /**
     * 获取列表数据
     *
     * @param string
     * @param order_status
     * @param last_id
     * @param initialize
     */
    private void IData(int loadType, String order_status) {
        if (loadType == LOAD_INITIALIZE) {
            PromptManager.showtextLoading(BaseContext, getResources()
                    .getString(R.string.loading));
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", user_Get.getId()); // );

        map.put("lastid", last_id);

        map.put("pagesize", Constants.PageSize + "");

        String host = "";

        if (PDaiFu == Ket_Tage) {
            map.put("type", "PT");
            host = Constants.CenterOderWeiFu;
        } else {
            map.put("order_status", order_status + "");
            host = Constants.Center_My_Order;
        }
        FBGetHttpData(map, host, Method.GET, 0, loadType);

    }

    /**
     * 提醒发货请求
     *
     * @param seller_order_sn
     * @param member_id
     */
    private void RemindSendOut(String seller_order_sn, String member_id) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("seller_order_sn", seller_order_sn);
        map.put("member_id", member_id);
        FBGetHttpData(map, Constants.Center_My_Order_Remind_Send_Out,
                Method.PUT, 1, 1116);
    }

    /**
     * 确认收货请求
     *
     * @param seller_order_sn
     * @param member_id
     */
    private void ConfirmShouhuo(String seller_order_sn, String member_id) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("seller_order_sn", seller_order_sn);
        map.put("member_id", member_id);
        FBGetHttpData(map, Constants.Center_My_Order_Confirm_Order, Method.PUT,
                2, 1116);
    }

    /**
     * 取消订单
     */
    private void CancelMyOrder(String seller_order_sn, String member_id) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", member_id);
        // @王永奎=======》7.14修改取消订单的key值！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
        map.put("order_sn", seller_order_sn);
        FBGetHttpData(map, Constants.Center_My_Order_Cancel, Method.PUT, 3,
                1116);
    }

    /**
     * 去付款
     */
    private void GoPay(String member_id, String order_sn) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", member_id);
        map.put("order_sn", order_sn);
        FBGetHttpData(map, Constants.Center_My_Order_Go_Pay, Method.PUT, 4,
                1116);
    }

    /**
     * 延迟收货
     *
     * @param seller_order_sn
     * @param member_id
     */
    private void DelayReceive(String seller_order_sn, String member_id) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", member_id);
        map.put("seller_order_sn", seller_order_sn);
        FBGetHttpData(map, Constants.Center_My_Order_Delayreceive, Method.PUT,
                5, 1116);
    }

    /**
     * 快速获取积分
     *
     * @param seller_order_sn
     * @param member_id
     */
    private void getIntegral(String seller_order_sn, String member_id) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", member_id);
        map.put("seller_order_sn", seller_order_sn);

        FBGetHttpData(map, Constants.Center_My_Order_Integral, Method.PUT,
                6, 1116);
    }

    /**
     * 暴露当是哪个订单状态的页面
     *
     * @return
     */
    public static int GetKetTage() {
        return Ket_Tage;
    }

    /**
     * 重新刷新数据
     */
    public void RegetData() {
        last_id = "";
        IData(LOAD_INITIALIZE, order_status);
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

    /**
     * 外层list的Ap
     *
     * @author datutu
     */
    private class CenterOrderOutsideAdapter extends BasAdapter {
        private LayoutInflater inflater;
        private int ResourceId;
        private List<BLCenterOder> datas = new ArrayList<BLCenterOder>();

        public CenterOrderOutsideAdapter(int resourceId) {
            super();

            this.inflater = LayoutInflater.from(BaseContext);
            this.ResourceId = resourceId;

        }

        /**
         * 刷新列表数据
         *
         * @param order_list
         */
        public void RefreshData(List<BLCenterOder> order_list) {
            this.datas = order_list;
            this.notifyDataSetChanged();
        }

        /**
         * 加载更多
         */
        public void AddFrashData(List<BLCenterOder> order_list) {
            this.datas.addAll(order_list);
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

        private void RefreshPosition(int type, int option, int position) {
//            switch (type) {
//                case PAll:
//
//                    BLCenterOder data = datas.get(position);// 非未付订单列表
//                    int visiblePosition = fragment_center_order_ls.getFirstVisiblePosition();
//                    if (position - visiblePosition >= 0) {
//                        View view = fragment_center_order_ls.getChildAt(position - visiblePosition + 1);
//                        CenterOrderOutsideItem holder1 = (CenterOrderOutsideItem) view.getTag();
//
//                        switch (Integer.parseInt(data.getOrder_status())) {
//                            case PDaiFu:
//                                if (0 == option) {//去付款
//                                    ControlView(holder1,data,PYiFu);
//                                } else {//取消订单
//                                    ControlView(holder1,data,PCancel);
//                                }
//                                break;
//
//                            case PYiFu:
//                               switch (option){
//                                   case 0://立即获取积分
//                                       ControlView(holder1,data,PYiFu);
//                                       if(1 ==data.getIs_have_point()){
//                                           holder1.fragment_center_order_is_get_integral.setVisibility(View.VISIBLE);
//                                           holder1.fragment_center_order_get_integral.setVisibility(View.GONE);
//                                       }else{
//                                           holder1.fragment_center_order_is_get_integral.setVisibility(View.GONE);
//                                           holder1.fragment_center_order_get_integral.setVisibility(View.GONE);
//                                       }
//
//
//                                       break;
//
//                                   case 1://申请退款
//                                       ControlView(holder1,data,PTuiKuan);
//                                       break;
//
//                                   case 2://提醒发货
//
//                                       holder1.fragment_center_order_remind_fahuo.setVisibility(View.GONE);
//
//                                       break;
//                               }
//                                break;
//
//                            case PDaiShou:
//                                switch (option){
//                                    case 0://确认收货
//                                        ControlView(holder1,data,PClose);
//                                        break;
//
//                                    case 1://延迟收货
//                                        holder1.fragment_center_order_shouhuo_commiont
//                                                .setVisibility(View.VISIBLE);
//                                        holder1.fragment_center_order_delayreceive
//                                                .setVisibility(View.GONE);
//                                        holder1.fragment_center_order_is_delaytime
//                                                .setVisibility(View.GONE);
//                                        break;
//                                }
//                                break;
//                        }
//                    }
//
//
//
//                    break;
//
//                case 2:
//                    datas.remove(position);
//                    notifyDataSetChanged();
//                    break;
//
//
//
//
//            }
            if (PAll == type) {
                BLCenterOder data = datas.get(position);// 非未付订单列表
                int visiblePosition = fragment_center_order_ls.getFirstVisiblePosition();
                if (position - visiblePosition >= 0) {
                    View view = fragment_center_order_ls.getChildAt(position - visiblePosition + 1);
                    CenterOrderOutsideItem holder1 = (CenterOrderOutsideItem) view.getTag();

                    switch (Integer.parseInt(data.getOrder_status())) {
                        case PDaiFu:
                            ControlView(holder1, data, PCancel);
                            break;

                        case PYiFu:
                            switch (option) {
                                case 0://立即获取积分
                                    ControlView(holder1, data, PYiFu);
                                    if (1 == data.getIs_have_point()) {
                                        holder1.fragment_center_order_is_get_integral.setVisibility(View.VISIBLE);
                                        holder1.fragment_center_order_get_integral.setVisibility(View.GONE);
                                    } else {
                                        holder1.fragment_center_order_is_get_integral.setVisibility(View.GONE);
                                        holder1.fragment_center_order_get_integral.setVisibility(View.GONE);
                                    }


                                    break;

                                case 1://申请退款
                                    ControlView(holder1, data, PTuiKuan);
                                    break;

                                case 2://提醒发货

                                    holder1.fragment_center_order_remind_fahuo.setVisibility(View.GONE);

                                    break;
                            }
                            break;

                        case PDaiShou:
                            switch (option) {
                                case 0://确认收货
                                    ControlView(holder1, data, PClose);
                                    break;

                                case 1://延迟收货
                                    holder1.fragment_center_order_shouhuo_commiont
                                            .setVisibility(View.VISIBLE);
                                    holder1.fragment_center_order_delayreceive
                                            .setVisibility(View.GONE);
                                    holder1.fragment_center_order_is_delaytime
                                            .setVisibility(View.GONE);
                                    break;
                            }
                            break;
                    }
                }
            } else {
                datas.remove(position);
                notifyDataSetChanged();
            }
        }

        private void ControlView(CenterOrderOutsideItem myItem, BLCenterOder data, int showType) {
            //int showType = Integer.parseInt(data.getOrder_status());
            switch (showType) {
                case PDaiFu:
                    myItem.fragment_center_order_pay_to.setVisibility(View.VISIBLE);
                    myItem.fragment_center_order_cancel_order
                            .setVisibility(View.VISIBLE);
                    myItem.fragment_center_order_pay_again.setVisibility(View.GONE);
                    myItem.fragment_center_order_shouhuo_commiont
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_apply_for_tuikuan_commiont
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_remind_fahuo
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_arbitration
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_is_cencal.setVisibility(View.GONE);
                    myItem.fragment_center_order_is_over.setVisibility(View.GONE);
                    myItem.fragment_center_order_apply_refunding
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_delayreceive
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_is_delaytime
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_get_integral.setVisibility(View.GONE);
                    myItem.fragment_center_order_is_get_integral.setVisibility(View.GONE);

                    break;

                case PYiFu:

                    if ("0".equals(data.getRefund())) {
                        myItem.fragment_center_order_apply_for_tuikuan_commiont
                                .setVisibility(View.VISIBLE);

                        if (0 == data.getIs_have_point()) {
                            myItem.fragment_center_order_is_get_integral.setVisibility(View.GONE);
                            myItem.fragment_center_order_get_integral.setVisibility(View.GONE);
                        } else {
                            if (0 == data.getAdvance_point()) {
                                myItem.fragment_center_order_is_get_integral.setVisibility(View.GONE);
                                myItem.fragment_center_order_get_integral.setVisibility(View.VISIBLE);
                            } else {
                                myItem.fragment_center_order_apply_for_tuikuan_commiont.setVisibility(View.GONE);
                                myItem.fragment_center_order_get_integral.setVisibility(View.GONE);
                                myItem.fragment_center_order_is_get_integral.setVisibility(View.VISIBLE);

                            }
                        }


                        if ("0".equals(data.getRemind_time())) {
                            myItem.fragment_center_order_remind_fahuo
                                    .setVisibility(View.VISIBLE);
                        } else {
                            myItem.fragment_center_order_remind_fahuo
                                    .setVisibility(View.GONE);
                        }


                    } else {
                        myItem.fragment_center_order_apply_for_tuikuan_commiont
                                .setVisibility(View.GONE);
                        myItem.fragment_center_order_remind_fahuo
                                .setVisibility(View.GONE);
                    }

                    myItem.fragment_center_order_cancel_order
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_pay_to.setVisibility(View.GONE);
                    myItem.fragment_center_order_pay_again.setVisibility(View.GONE);
                    myItem.fragment_center_order_shouhuo_commiont
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_arbitration
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_is_cencal.setVisibility(View.GONE);
                    myItem.fragment_center_order_is_over.setVisibility(View.GONE);
                    myItem.fragment_center_order_apply_refunding
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_delayreceive
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_is_delaytime
                            .setVisibility(View.GONE);

                    break;

                case PDaiShou:
                    // 延迟时间10天，只有10天后才显示延迟收货
                    long delaytime = Long.parseLong(data.getCreate_time())
                            + (10 * 24 * 60 * 60);
                    // 当前时间
                    long nowtime = System.currentTimeMillis() / 1000;
                    if (nowtime < delaytime) {

                        myItem.fragment_center_order_shouhuo_commiont
                                .setVisibility(View.VISIBLE);
                        myItem.fragment_center_order_delayreceive
                                .setVisibility(View.GONE);
                        myItem.fragment_center_order_is_delaytime
                                .setVisibility(View.GONE);

                    } else {
                        if ("0".equals(data.getDelaynumber())) {
                            myItem.fragment_center_order_shouhuo_commiont
                                    .setVisibility(View.VISIBLE);
                            myItem.fragment_center_order_delayreceive
                                    .setVisibility(View.VISIBLE);
                            myItem.fragment_center_order_is_delaytime
                                    .setVisibility(View.GONE);
                        } else {
                            myItem.fragment_center_order_shouhuo_commiont
                                    .setVisibility(View.VISIBLE);
                            myItem.fragment_center_order_delayreceive
                                    .setVisibility(View.GONE);
                            myItem.fragment_center_order_is_delaytime
                                    .setVisibility(View.GONE);
                        }
                    }
                    myItem.fragment_center_order_get_integral.setVisibility(View.GONE);
                    myItem.fragment_center_order_remind_fahuo
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_cancel_order
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_pay_again.setVisibility(View.GONE);

                    myItem.fragment_center_order_pay_to.setVisibility(View.GONE);
                    myItem.fragment_center_order_apply_for_tuikuan_commiont
                            .setVisibility(View.GONE);

                    myItem.fragment_center_order_arbitration
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_is_cencal.setVisibility(View.GONE);
                    myItem.fragment_center_order_is_over.setVisibility(View.GONE);
                    myItem.fragment_center_order_apply_refunding
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_is_get_integral.setVisibility(View.GONE);
                    break;

                case PTuiKuan:
                    myItem.fragment_center_order_is_get_integral.setVisibility(View.GONE);
                    myItem.fragment_center_order_get_integral.setVisibility(View.GONE);
                    myItem.fragment_center_order_arbitration
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_cancel_order
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_pay_to.setVisibility(View.GONE);
                    myItem.fragment_center_order_pay_again.setVisibility(View.GONE);
                    myItem.fragment_center_order_shouhuo_commiont
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_apply_for_tuikuan_commiont
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_remind_fahuo
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_delayreceive
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_is_cencal.setVisibility(View.GONE);
                    myItem.fragment_center_order_is_over.setVisibility(View.GONE);
                    myItem.fragment_center_order_apply_refunding
                            .setVisibility(View.VISIBLE);
                    myItem.fragment_center_order_is_delaytime
                            .setVisibility(View.GONE);
                    break;

                case PZhongCai:
                    myItem.fragment_center_order_is_get_integral.setVisibility(View.GONE);
                    myItem.fragment_center_order_get_integral.setVisibility(View.GONE);
                    myItem.fragment_center_order_arbitration
                            .setVisibility(View.VISIBLE);
                    myItem.fragment_center_order_cancel_order
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_pay_to.setVisibility(View.GONE);
                    myItem.fragment_center_order_pay_again.setVisibility(View.GONE);
                    myItem.fragment_center_order_shouhuo_commiont
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_apply_for_tuikuan_commiont
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_remind_fahuo
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_is_cencal.setVisibility(View.GONE);
                    myItem.fragment_center_order_is_over.setVisibility(View.GONE);
                    myItem.fragment_center_order_apply_refunding
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_delayreceive
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_is_delaytime
                            .setVisibility(View.GONE);
                    break;

                case PAgreeTuiKuan:
                    myItem.fragment_center_order_is_get_integral.setVisibility(View.GONE);
                    myItem.fragment_center_order_get_integral.setVisibility(View.GONE);
                    myItem.fragment_center_order_cancel_order
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_pay_again.setVisibility(View.GONE);
                    myItem.fragment_center_order_pay_to.setVisibility(View.GONE);
                    myItem.fragment_center_order_shouhuo_commiont
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_apply_for_tuikuan_commiont
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_remind_fahuo
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_arbitration
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_is_cencal.setVisibility(View.GONE);
                    myItem.fragment_center_order_is_over
                            .setVisibility(View.VISIBLE);
                    StrUtils.SetTxt(myItem.fragment_center_order_is_over, "退款已完成");
                    myItem.fragment_center_order_apply_refunding
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_delayreceive
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_is_delaytime
                            .setVisibility(View.GONE);
                    break;

                case PClose:
                    myItem.fragment_center_order_is_get_integral.setVisibility(View.GONE);
                    myItem.fragment_center_order_get_integral.setVisibility(View.GONE);
                    myItem.fragment_center_order_cancel_order
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_pay_again.setVisibility(View.GONE);
                    myItem.fragment_center_order_pay_to.setVisibility(View.GONE);

                    myItem.fragment_center_order_shouhuo_commiont
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_apply_for_tuikuan_commiont
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_remind_fahuo
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_arbitration
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_is_cencal.setVisibility(View.GONE);
                    myItem.fragment_center_order_is_over
                            .setVisibility(View.VISIBLE);
                    StrUtils.SetTxt(myItem.fragment_center_order_is_over, "订单已完成");
                    myItem.fragment_center_order_apply_refunding
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_delayreceive
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_is_delaytime
                            .setVisibility(View.GONE);
                    break;

                case PCancel:// 订单已取消
                    myItem.fragment_center_order_is_get_integral.setVisibility(View.GONE);
                    myItem.fragment_center_order_get_integral.setVisibility(View.GONE);
                    myItem.fragment_center_order_cancel_order
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_pay_again.setVisibility(View.GONE);
                    myItem.fragment_center_order_pay_to.setVisibility(View.GONE);

                    myItem.fragment_center_order_shouhuo_commiont
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_apply_for_tuikuan_commiont
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_remind_fahuo
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_arbitration
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_is_cencal
                            .setVisibility(View.VISIBLE);
                    myItem.fragment_center_order_is_over.setVisibility(View.GONE);
                    myItem.fragment_center_order_apply_refunding
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_delayreceive
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_is_delaytime
                            .setVisibility(View.GONE);
                    break;

                default:
                    myItem.fragment_center_order_is_get_integral.setVisibility(View.GONE);
                    myItem.fragment_center_order_get_integral.setVisibility(View.GONE);
                    myItem.fragment_center_order_pay_again.setVisibility(View.GONE);
                    myItem.fragment_center_order_pay_to.setVisibility(View.GONE);
                    myItem.fragment_center_order_cancel_order
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_apply_for_tuikuan_commiont
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_remind_fahuo
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_arbitration
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_shouhuo_commiont
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_is_cencal.setVisibility(View.GONE);
                    myItem.fragment_center_order_is_over.setVisibility(View.GONE);
                    myItem.fragment_center_order_apply_refunding
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_delayreceive
                            .setVisibility(View.GONE);
                    myItem.fragment_center_order_is_delaytime
                            .setVisibility(View.GONE);
                    break;
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see io.vtown.WeiTangApp.adapter.BasAdapter#getView(int,
         * android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int position, View convertView, ViewGroup arg2) {
            CenterOrderOutsideItem myItem = null;
            if (convertView == null) {

                convertView = inflater.inflate(ResourceId, null);
                myItem = new CenterOrderOutsideItem();
                myItem.fragment_center_order_shopname = (TextView) convertView
                        .findViewById(R.id.fragment_center_order_shopname);
                myItem.fragment_center_order_cancel_order = (TextView) convertView
                        .findViewById(R.id.fragment_center_order_cancel_order);
                myItem.fragment_center_order_pay_to = (TextView) convertView
                        .findViewById(R.id.fragment_center_order_pay_to);
                myItem.fragment_center_order_pay_again = (TextView) convertView
                        .findViewById(R.id.fragment_center_order_pay_again);
                myItem.fragment_center_order_shouhuo_commiont = (TextView) convertView
                        .findViewById(R.id.fragment_center_order_shouhuo_commiont);
                myItem.fragment_center_order_apply_for_tuikuan_commiont = (TextView) convertView
                        .findViewById(R.id.fragment_center_order_apply_for_tuikuan_commiont);
                myItem.fragment_center_order_remind_fahuo = (TextView) convertView
                        .findViewById(R.id.fragment_center_order_remind_fahuo);
                myItem.fragment_center_order_get_integral = (TextView) convertView.findViewById(R.id.fragment_center_order_get_integral);
                myItem.fragment_center_order_is_get_integral = (TextView) convertView.findViewById(R.id.fragment_center_order_is_get_integral);
                myItem.fragment_center_order_arbitration = (TextView) convertView
                        .findViewById(R.id.fragment_center_order_arbitration);
                myItem.fragment_center_order_is_delaytime = (TextView) convertView
                        .findViewById(R.id.fragment_center_order_is_delaytime);
                myItem.fragment_center_order_delayreceive = (TextView) convertView
                        .findViewById(R.id.fragment_center_order_delayreceive);
                myItem.item_fragment_center_order_allnum = (TextView) convertView
                        .findViewById(R.id.item_fragment_center_order_allnum);
                myItem.item_fragment_center_order_allmoney = (TextView) convertView
                        .findViewById(R.id.item_fragment_center_order_allmoney);
                myItem.item_fragment_center_order_ls = (CompleteListView) convertView
                        .findViewById(R.id.item_fragment_center_order_ls);
                myItem.item_fragment_center_order_postage = (TextView) convertView
                        .findViewById(R.id.item_fragment_center_order_postage);
                myItem.fragment_center_order_is_cencal = (TextView) convertView
                        .findViewById(R.id.fragment_center_order_is_cencal);
                myItem.fragment_center_order_is_over = (TextView) convertView
                        .findViewById(R.id.fragment_center_order_is_over);
                myItem.fragment_center_order_apply_refunding = (TextView) convertView
                        .findViewById(R.id.fragment_center_order_apply_refunding);
                myItem.tv_center_my_order_seller_order_sn = (TextView) convertView
                        .findViewById(R.id.tv_center_my_order_seller_order_sn);
                convertView.setTag(myItem);
            } else {
                myItem = (CenterOrderOutsideItem) convertView.getTag();
            }

            BLCenterOder data = datas.get(position);// 非未付订单列表

            LogUtils.i("**************good--id****************" + data.getId());
            ControlView(myItem, data, Integer.parseInt(data.getOrder_status()));

            StrUtils.SetTxt(myItem.tv_center_my_order_seller_order_sn,
                    data.getSeller_order_sn());
            StrUtils.SetTxt(myItem.fragment_center_order_shopname, data.getSeller_name());

            StrUtils.SetTxt(myItem.item_fragment_center_order_allnum,
                    String.format("共%1$s件商品", data.getNumber()));
            // float total_price = Float.parseFloat(data.getGoods_price())
            // + Float.parseFloat(data.getPostage());
//            StrUtils.SetTxt(myItem.item_fragment_center_order_allmoney, String
//                    .format("%1$s元", StrUtils.SetTextForMony(data
//                            .getOrder_total_price())));

            StrUtils.SetMoneyFormat(BaseContext, myItem.item_fragment_center_order_allmoney, data.getOrder_total_price(), 17);

            float postageF = Float.parseFloat(data.getPostage());
            StrUtils.SetTxt(
                    myItem.item_fragment_center_order_postage,
                    postageF == 0.0f ? "(免邮费)" : String.format("(含运费%1$s元)",
                            StrUtils.SetTextForMony(postageF + "")));
            String str = StrUtils.SetTextForMony(data
                    .getOrder_total_price());


            myItem.item_fragment_center_order_ls
                    .setAdapter(new CenterOrderInsideAdapter(
                            R.layout.item_fragment_center_order_inside, datas
                            .get(position).getGoods(), false));
            final int myItemPosition = position;
            // 商品Item的点击事件，跳转商品详情页面
            myItem.item_fragment_center_order_ls
                    .setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            Intent intent = new Intent(BaseContext,
                                    AGoodDetail.class);
                            intent.putExtra("goodid", datas.get(myItemPosition)
                                    .getGoods().get(position).getGoods_id());
                            PromptManager.SkipActivity(BaseActivity, intent);
                        }
                    });

            onClickEvent(position, myItem, data);

            return convertView;

        }

        /**
         * 点击事件的处理
         *
         * @param myItem
         * @param blComment
         */
        private void onClickEvent(final int position, CenterOrderOutsideItem myItem,
                                  final BLCenterOder blComment) {

            // 取消订单
            myItem.fragment_center_order_cancel_order
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ShowCustomDialog("确认取消订单吗？", "取消", "确认",
                                    new IDialogResult() {
                                        @Override
                                        public void RightResult() {
                                            if (CheckNet(BaseContext))
                                                return;
                                            CancelMyOrder(
                                                    blComment.getOrder_sn(),
                                                    blComment.getMember_id());
                                            SelectPosition = position;
                                        }

                                        @Override
                                        public void LeftResult() {
                                        }
                                    });

                        }
                    });

            // 再次购买
            myItem.fragment_center_order_pay_again
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
            // 去付款
            myItem.fragment_center_order_pay_to
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            ShowCustomDialog("确认要去付款吗？", "取消", "确认",
                                    new IDialogResult() {
                                        @Override
                                        public void RightResult() {
                                            if (CheckNet(BaseContext))
                                                return;
                                            GoPay(blComment.getMember_id(),
                                                    blComment.getOrder_sn());
                                            SelectPosition = position;
                                        }

                                        @Override
                                        public void LeftResult() {
                                        }
                                    });

                        }
                    });
            // 延迟收货
            myItem.fragment_center_order_delayreceive
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            ShowCustomDialog("每个商品只允许延期一次，延期为3天，确认延期收货吗？",
                                    "取消", "确认", new IDialogResult() {
                                        @Override
                                        public void RightResult() {
                                            if (CheckNet(BaseContext))
                                                return;
                                            DelayReceive(blComment
                                                            .getSeller_order_sn(),
                                                    blComment.getMember_id());
                                            SelectPosition = position;
                                        }

                                        @Override
                                        public void LeftResult() {
                                        }
                                    });
                        }
                    });

            myItem.fragment_center_order_get_integral.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CheckNet(BaseContext))
                        return;

                    ShowCustomDialog("确认立即获取积分吗？\n获取积分后不能再申请退款", "取消", "确认",
                            new IDialogResult() {
                                @Override
                                public void RightResult() {
                                    getIntegral(blComment.getSeller_order_sn(), blComment.getMember_id());
                                    SelectPosition = position;
                                }

                                @Override
                                public void LeftResult() {
                                }
                            });
                }
            });

            // 确认收货
            myItem.fragment_center_order_shouhuo_commiont
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (CheckNet(BaseContext))
                                return;

                            ShowCustomDialog("确认收货？", "取消", "确认",
                                    new IDialogResult() {
                                        @Override
                                        public void RightResult() {
                                            ConfirmShouhuo(blComment
                                                            .getSeller_order_sn(),
                                                    blComment.getMember_id());
                                            SelectPosition = position;
                                        }

                                        @Override
                                        public void LeftResult() {
                                        }
                                    });

                        }
                    });
            // 申请退款
            myItem.fragment_center_order_apply_for_tuikuan_commiont
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SelectPosition = position;
                            Intent intent = new Intent(BaseContext,
                                    AApplyTuikuan.class);
                            intent.putExtra("FromTag",
                                    AApplyTuikuan.Tag_From_Center_My_Order);
                            intent.putExtra("seller_order_sn",
                                    blComment.getSeller_order_sn());
                            PromptManager.SkipActivity((Activity) BaseContext,
                                    intent);

                        }
                    });
            // 提醒发货
            myItem.fragment_center_order_remind_fahuo
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (CheckNet(BaseContext))
                                return;
                            RemindSendOut(blComment.getSeller_order_sn(),
                                    blComment.getMember_id());
                            SelectPosition = position;
                        }
                    });

            // 仲裁
            myItem.fragment_center_order_arbitration
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                        }
                    });

        }

        /**
         * 外层的
         */
        class CenterOrderOutsideItem {
            TextView tv_center_my_order_seller_order_sn;// 订单编号
            TextView fragment_center_order_shopname;// 店名
            TextView fragment_center_order_cancel_order;// 取消订单
            TextView fragment_center_order_pay_to;// 去付款
            TextView fragment_center_order_pay_again;// 再次购买
            TextView fragment_center_order_shouhuo_commiont;// 确认收货
            TextView fragment_center_order_apply_for_tuikuan_commiont;// 申请退款
            TextView fragment_center_order_remind_fahuo;// 提醒发货
            TextView fragment_center_order_arbitration;// 仲裁
            TextView fragment_center_order_delayreceive;// 延迟收货
            TextView fragment_center_order_is_cencal;// 订单已取消
            TextView fragment_center_order_is_over;// 订单已完成
            TextView fragment_center_order_is_delaytime;// 已延期收货
            TextView fragment_center_order_apply_refunding;// 申请退款中
            TextView fragment_center_order_get_integral;// 获取积分
            TextView fragment_center_order_is_get_integral;//已获取积分
            CompleteListView item_fragment_center_order_ls;

            TextView item_fragment_center_order_allnum;// 多少件商品
            TextView item_fragment_center_order_allmoney;// 所有费用
            TextView item_fragment_center_order_postage;// 运费

        }

    }

    /**
     * 内层的ls的adapter
     *
     * @author datutu
     */
    class CenterOrderInsideAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private int ResourceId;
        private boolean IsDaiFu;
        private List<BLDComment> datas = new ArrayList<BLDComment>();

        public CenterOrderInsideAdapter(int resourceId, List<BLDComment> list,
                                        boolean IsDaiFus) {
            super();

            this.inflater = LayoutInflater.from(BaseContext);
            this.ResourceId = resourceId;
            this.datas = list;
            this.IsDaiFu = IsDaiFus;
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
        public View getView(int position, View convertView, ViewGroup arg2) {
            CenterOrderInsideItem myItem = null;
            if (convertView == null) {
                myItem = new CenterOrderInsideItem();
                convertView = inflater.inflate(ResourceId, null);
                myItem.item_fragment_center_order_in_iv = (ImageView) convertView
                        .findViewById(R.id.item_fragment_center_order_in_iv);
                myItem.item_fragment_center_order_in_name = (TextView) convertView
                        .findViewById(R.id.item_fragment_center_order_in_name);
                myItem.item_fragment_center_order_in_price = (TextView) convertView
                        .findViewById(R.id.item_fragment_center_order_in_price);
                myItem.item_fragment_center_order_in_number = (TextView) convertView
                        .findViewById(R.id.item_fragment_center_order_in_number);

                convertView.setTag(myItem);
            } else {
                myItem = (CenterOrderInsideItem) convertView.getTag();
            }
            ImageLoaderUtil.Load2(datas.get(position).getGoods_cover(),
                    myItem.item_fragment_center_order_in_iv,
                    R.drawable.error_iv2);
            StrUtils.SetTxt(myItem.item_fragment_center_order_in_name, datas
                    .get(position).getGoods_name());
            StrUtils.SetTxt(
                    myItem.item_fragment_center_order_in_price,
                    String.format("￥%1$s", StrUtils.SetTextForMony(datas.get(
                            position).getGoods_price())));
            StrUtils.SetTxt(myItem.item_fragment_center_order_in_number, String
                    .format("x%1$s", datas.get(position).getGoods_number()));

            return convertView;

        }

        class CenterOrderInsideItem {
            ImageView item_fragment_center_order_in_iv;
            TextView item_fragment_center_order_in_name;
            TextView item_fragment_center_order_in_price;
            TextView item_fragment_center_order_in_number;
        }
    }

    // *************************************************************************

    /**
     * @author Yihuihua 最外层的AP
     */
    class CenterOrderNoPayAdapter extends BaseAdapter {

        private int ResourseId;
        private LayoutInflater inflater;
        List<BLCenterOder> datas = new ArrayList<BLCenterOder>();

        public CenterOrderNoPayAdapter(int ResourseId) {
            super();
            this.ResourseId = ResourseId;
            this.inflater = LayoutInflater.from(BaseContext);
        }

        @Override
        public int getCount() {

            return datas.size();
        }

        /**
         * 刷新列表数据
         *
         * @param order_list
         */
        public void RefreshData(List<BLCenterOder> order_list) {
            this.datas = order_list;
            this.notifyDataSetChanged();
        }

        /**
         * 加载更多
         */
        public void AddFrashData(List<BLCenterOder> order_list) {
            this.datas.addAll(order_list);
            this.notifyDataSetChanged();
        }

        @Override
        public Object getItem(int position) {

            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CenterOrderNoPayItem centerOrderNoPay = null;
            if (convertView == null) {
                centerOrderNoPay = new CenterOrderNoPayItem();
                convertView = inflater.inflate(ResourseId, null);
                centerOrderNoPay.tv_center_order_no_pay_order_sn = (TextView) convertView
                        .findViewById(R.id.tv_center_order_no_pay_order_sn);
                centerOrderNoPay.fragment_center_order_no_pay_pay_to = (TextView) convertView
                        .findViewById(R.id.fragment_center_order_no_pay_pay_to);
                centerOrderNoPay.fragment_center_order_no_pay_cancel_order = (TextView) convertView
                        .findViewById(R.id.fragment_center_order_no_pay_cancel_order);
                centerOrderNoPay.item_fragment_center_order_no_pay_outside = (CompleteListView) convertView
                        .findViewById(R.id.item_fragment_center_order_no_pay_outside);
                centerOrderNoPay.item_fragment_center_order_no_pay_allnum = (TextView) convertView
                        .findViewById(R.id.item_fragment_center_order_no_pay_allnum);
                centerOrderNoPay.item_fragment_center_order_no_pay_allmoney = (TextView) convertView
                        .findViewById(R.id.item_fragment_center_order_no_pay_allmoney);
                centerOrderNoPay.item_fragment_center_order_no_pay_postage = (TextView) convertView
                        .findViewById(R.id.item_fragment_center_order_no_pay_postage);

                convertView.setTag(centerOrderNoPay);
            } else {
                centerOrderNoPay = (CenterOrderNoPayItem) convertView.getTag();
            }
            CenterOrderNoPayInsideAdapter centerOrderNoPayInside = new CenterOrderNoPayInsideAdapter(
                    R.layout.item_center_order_no_pay_inside, datas.get(
                    position).getSon_order());
            centerOrderNoPay.item_fragment_center_order_no_pay_outside
                    .setAdapter(centerOrderNoPayInside);
            StrUtils.SetTxt(centerOrderNoPay.tv_center_order_no_pay_order_sn,
                    datas.get(position).getOrder_sn());

            // float total_price =
            // Float.parseFloat(datas.get(position).getOrder_total_price())
            // + Float.parseFloat(datas.get(position).getPostage_money());
//            StrUtils.SetTxt(
//                    centerOrderNoPay.item_fragment_center_order_no_pay_allmoney,
//                    String.format("%1$s元", StrUtils.SetTextForMony(datas.get(
//                            position).getOrder_total_price())));

            StrUtils.SetMoneyFormat(BaseContext, centerOrderNoPay.item_fragment_center_order_no_pay_allmoney, datas.get(
                    position).getOrder_total_price(), 17);
            float postage_moneyF = Float.parseFloat(datas.get(position)
                    .getPostage_money());
            StrUtils.SetTxt(
                    centerOrderNoPay.item_fragment_center_order_no_pay_postage,
                    postage_moneyF == 0.0f ? "(免邮费)" : String.format(
                            "(含运费%1$s元)",
                            StrUtils.SetTextForMony(postage_moneyF + "")));

            final BLCenterOder blComment = datas.get(position);
            // 去付款
            centerOrderNoPay.fragment_center_order_no_pay_pay_to
                    .setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            ShowCustomDialog("确认要去付款吗？", "取消", "确认",
                                    new IDialogResult() {
                                        @Override
                                        public void RightResult() {
                                            if (CheckNet(BaseContext))
                                                return;
                                            GoPay(blComment.getMember_id(),
                                                    blComment.getOrder_sn());
                                        }

                                        @Override
                                        public void LeftResult() {
                                        }
                                    });

                        }
                    });
            // 取消订单
            centerOrderNoPay.fragment_center_order_no_pay_cancel_order
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ShowCustomDialog("确认取消订单吗？", "取消", "确认",
                                    new IDialogResult() {
                                        @Override
                                        public void RightResult() {
                                            if (CheckNet(BaseContext))
                                                return;
                                            CancelMyOrder(
                                                    blComment.getOrder_sn(),
                                                    blComment.getMember_id());

                                        }

                                        @Override
                                        public void LeftResult() {
                                        }
                                    });

                        }
                    });
            return convertView;
        }

    }

    class CenterOrderNoPayInsideAdapter extends BaseAdapter {

        private int ResourseId;
        private LayoutInflater inflater;
        private List<BLDComment> secoud_datas = new ArrayList<BLDComment>();

        public CenterOrderNoPayInsideAdapter(int ResourseId,
                                             List<BLDComment> datas) {
            super();

            this.ResourseId = ResourseId;
            this.inflater = LayoutInflater.from(BaseContext);
            this.secoud_datas = datas;
        }

        @Override
        public int getCount() {

            return secoud_datas.size();
        }

        @Override
        public Object getItem(int position) {

            return secoud_datas.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CenterOrderNoPayInsideItem centerOrderNoPayInside = null;
            if (convertView == null) {
                centerOrderNoPayInside = new CenterOrderNoPayInsideItem();
                convertView = inflater.inflate(ResourseId, null);
                centerOrderNoPayInside.tv_center_order_no_pay_seller_name = (TextView) convertView
                        .findViewById(R.id.tv_center_order_no_pay_seller_name);
                centerOrderNoPayInside.item_fragment_center_order_no_pay_inside = (CompleteListView) convertView
                        .findViewById(R.id.item_fragment_center_order_no_pay_inside);
                convertView.setTag(centerOrderNoPayInside);
            } else {
                centerOrderNoPayInside = (CenterOrderNoPayInsideItem) convertView
                        .getTag();
            }
            StrUtils.SetTxt(
                    centerOrderNoPayInside.tv_center_order_no_pay_seller_name,
                    String.format("卖家：%1$s", secoud_datas.get(position)
                            .getSeller_name()));
            CenterOrderNoPayInnerMostAdapter centerOrderNoPayInnerMost = new CenterOrderNoPayInnerMostAdapter(
                    R.layout.item_center_order_no_pay_innermost, secoud_datas
                    .get(position).getGoods());
            centerOrderNoPayInside.item_fragment_center_order_no_pay_inside
                    .setAdapter(centerOrderNoPayInnerMost);
            final int myItemPosition = position;
            centerOrderNoPayInside.item_fragment_center_order_no_pay_inside
                    .setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            Intent intent = new Intent(BaseContext,
                                    AGoodDetail.class);
                            intent.putExtra("goodid",
                                    secoud_datas.get(myItemPosition).getGoods()
                                            .get(position).getGoods_id());
                            PromptManager.SkipActivity(BaseActivity, intent);
                        }
                    });

            return convertView;
        }

    }

    /**
     * @author Yihuihua 最里层的AP
     */
    class CenterOrderNoPayInnerMostAdapter extends BaseAdapter {

        private int ResourseId;
        private LayoutInflater inflater;
        private List<BDComment> innerMost_data = new ArrayList<BDComment>();

        public CenterOrderNoPayInnerMostAdapter(int ResourseId,
                                                List<BDComment> datas) {
            super();

            this.ResourseId = ResourseId;
            this.inflater = LayoutInflater.from(BaseContext);
            this.innerMost_data = datas;
        }

        @Override
        public int getCount() {

            return innerMost_data.size();
        }

        @Override
        public Object getItem(int position) {

            return innerMost_data.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CenterOrderNoPayInnerMostItem centerOrderNoPayInnerMost = null;
            if (convertView == null) {
                centerOrderNoPayInnerMost = new CenterOrderNoPayInnerMostItem();
                convertView = inflater.inflate(ResourseId, null);
                centerOrderNoPayInnerMost.item_fragment_center_order_no_pay_in_iv = ViewHolder
                        .get(convertView,
                                R.id.item_fragment_center_order_no_pay_in_iv);
                centerOrderNoPayInnerMost.item_fragment_center_order_no_pay_in_name = ViewHolder
                        .get(convertView,
                                R.id.item_fragment_center_order_no_pay_in_name);
                centerOrderNoPayInnerMost.item_fragment_center_order_no_pay_in_price = ViewHolder
                        .get(convertView,
                                R.id.item_fragment_center_order_no_pay_in_price);
                centerOrderNoPayInnerMost.item_fragment_center_order_no_pay_in_number = ViewHolder
                        .get(convertView,
                                R.id.item_fragment_center_order_no_pay_in_number);
                convertView.setTag(centerOrderNoPayInnerMost);
                ImageLoaderUtil
                        .Load2(innerMost_data.get(position).getGoods_cover(),
                                centerOrderNoPayInnerMost.item_fragment_center_order_no_pay_in_iv,
                                R.drawable.error_iv2);
            } else {
                centerOrderNoPayInnerMost = (CenterOrderNoPayInnerMostItem) convertView
                        .getTag();
            }

            StrUtils.SetTxt(
                    centerOrderNoPayInnerMost.item_fragment_center_order_no_pay_in_name,
                    innerMost_data.get(position).getGoods_name());
            StrUtils.SetTxt(
                    centerOrderNoPayInnerMost.item_fragment_center_order_no_pay_in_price,
                    String.format("￥%1$s", StrUtils
                            .SetTextForMony(innerMost_data.get(position)
                                    .getGoods_money())));
            StrUtils.SetTxt(
                    centerOrderNoPayInnerMost.item_fragment_center_order_no_pay_in_number,
                    String.format("x%1$s", innerMost_data.get(position)
                            .getGoods_number()));
            return convertView;
        }

    }

    /**
     * @author Yihuihua 最外层列表的Holder
     */
    class CenterOrderNoPayItem {

        public TextView tv_center_order_no_pay_order_sn;// 订单号
        public TextView fragment_center_order_no_pay_pay_to;// 去付款
        public TextView fragment_center_order_no_pay_cancel_order;// 取消订单
        public CompleteListView item_fragment_center_order_no_pay_outside;// 第一层的订单列表
        public TextView tv_center_my_order_no_pay_seller_order_sn;// 订单编号
        public TextView item_fragment_center_order_no_pay_allnum;// 有多少件商品
        public TextView item_fragment_center_order_no_pay_allmoney;// 总价
        public TextView item_fragment_center_order_no_pay_postage;// 运费

    }

    /**
     * @author Yihuihua 第二层列表Holder
     */
    class CenterOrderNoPayInsideItem {
        public TextView tv_center_order_no_pay_seller_name;// 买家名称
        public CompleteListView item_fragment_center_order_no_pay_inside;// 第二层的订单列表
    }

    /**
     * @author Yihuihua 最里层的Holder
     */
    class CenterOrderNoPayInnerMostItem {
        public ImageView item_fragment_center_order_no_pay_in_iv;// 商品图标
        public TextView item_fragment_center_order_no_pay_in_name;// 商品title
        public TextView item_fragment_center_order_no_pay_in_price;// 商品价格
        public TextView item_fragment_center_order_no_pay_in_number;// 商品个数
    }

    // *************************************************************************

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (CheckNet(BaseContext))
            return;
        BLCenterOder bl_data = null;

        if (PDaiFu != Ket_Tage) {
            int count = centerOrderOutsideAdapter.getCount();
            // if(count > 0){
            bl_data = (BLCenterOder) centerOrderOutsideAdapter
                    .getItem(position);

            int order_status = Integer.parseInt(bl_data.getOrder_status());
            Intent intent = null;
            if (PDaiFu == order_status) {
                intent = new Intent(BaseContext,
                        ACenterMyOrderNoPayDetail.class);
                intent.putExtra("order_sn", bl_data.getOrder_sn());

            } else {
                intent = new Intent(BaseContext, ACenterMyOrderDetail.class);

                intent.putExtra("Key_TageStr", order_status);
            }

            intent.putExtra("member_id", bl_data.getMember_id());
            intent.putExtra("seller_order_sn", bl_data.getSeller_order_sn());
            PromptManager.SkipActivity((Activity) BaseContext, intent);
            // }

        } else {
            int count = centerOrderNoPayAdapter.getCount();
            // if(count > 0){
            bl_data = (BLCenterOder) centerOrderNoPayAdapter
                    .getItem(position);
            Intent intent = new Intent(BaseContext,
                    ACenterMyOrderNoPayDetail.class);
            intent.putExtra("order_sn", bl_data.getOrder_sn());
            intent.putExtra("member_id", bl_data.getMember_id());
            intent.putExtra("seller_order_sn", bl_data.getSeller_order_sn());
            PromptManager.SkipActivity((Activity) BaseContext, intent);
            // }

        }

    }


    /**
     * 接收事件
     *
     * @param event
     */

    public void OnGetMessage(BMessage event) {
        int messageType = event.getMessageType();
        if (messageType == event.Tage_Center_Order_Updata) {
            last_id = "";
            IData(LOAD_INITIALIZE, Ket_Tage + "");
        }
        if (messageType == event.Tage_To_Pay_Updata) {
            last_id = "";
            IData(LOAD_INITIALIZE, Ket_Tage + "");
        }

    }

    @Override
    protected void InitTile() {
        mTitle = (TextView) findViewById(R.id.title);
        SetTitleTxt("全部");
        mDrawable = getResources().getDrawable(R.drawable.arrow_down);
        mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
                mDrawable.getMinimumHeight());
        mTitle.setCompoundDrawables(null, null, mDrawable, null);
        mTitle.setOnClickListener(this);
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        switch (Data.getHttpResultTage()) {
            case 0:// 获取列表的返回结果

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
                        ShowErrorIv(R.drawable.pic_maidongxikongbaiyemian);
                        fragent_centeroder_nodata_lay.setVisibility(View.VISIBLE);
                        fragent_centeroder_nodata_lay.setClickable(false);
                        fragment_center_order_ls.setVisibility(View.GONE);
                        if (PDaiFu == Ket_Tage) {
                            centerOrderNoPayAdapter
                                    .RefreshData(new ArrayList<BLCenterOder>());
                        } else {
                            centerOrderOutsideAdapter
                                    .RefreshData(new ArrayList<BLCenterOder>());

                            if (PAll == Ket_Tage) {
                                CacheUtil.Center_Order_List_Save(getApplicationContext(),
                                        Data.getHttpResultStr());
                            }
                        }
                    }
                    if (LOAD_LOADMOREING == Data.getHttpLoadType()) {
                        //fragment_center_order_ls.stopLoadMore();

                        PromptManager.ShowCustomToast(BaseContext, "没有更多订单哦");

                        fragment_center_order_refrash.setLoading(false);
                    }
                    if (LOAD_REFRESHING == Data.getHttpLoadType()) {
                        //fragment_center_order_ls.stopRefresh();
                        PromptManager.ShowCustomToast(BaseContext, "暂无订单");
                        if (PDaiFu == Ket_Tage) {
                            centerOrderNoPayAdapter
                                    .RefreshData(new ArrayList<BLCenterOder>());
                        } else {
                            centerOrderOutsideAdapter
                                    .RefreshData(new ArrayList<BLCenterOder>());

                        }
                    }
                    return;
                }
                fragment_center_order_ls.setVisibility(View.VISIBLE);
                fragent_centeroder_nodata_lay.setVisibility(View.GONE);
                order_list = new ArrayList<BLCenterOder>();

                try {

                    order_list = JSON.parseArray(Data.getHttpResultStr(),
                            BLCenterOder.class);

                } catch (Exception e) {
                    onError("解析失败", 0);
                }
                if (PAll == Ket_Tage) {
                    CacheUtil.Center_Order_List_Save(getApplicationContext(),
                            Data.getHttpResultStr());
                }
                switch (Data.getHttpLoadType()) {
                    case LOAD_INITIALIZE:

                        if (PDaiFu == Ket_Tage) {
                            centerOrderNoPayAdapter.RefreshData(order_list);
                        } else {
                            centerOrderOutsideAdapter.RefreshData(order_list);
                        }
                        fragment_center_order_refrash.setRefreshing(false);
                        if (order_list.size() == Constants.PageSize)
                            //fragment_center_order_ls.ShowFoot();
                            fragment_center_order_refrash.setCanLoadMore(true);
                        if (order_list.size() < Constants.PageSize)
                            // fragment_center_order_ls.hidefoot();
                            fragment_center_order_refrash.setCanLoadMore(false);
                        break;

                    case LOAD_REFRESHING:// 刷新数据
                        fragment_center_order_refrash.setRefreshing(false);
                        //fragment_center_order_ls.stopRefresh();
                        if (PDaiFu == Ket_Tage) {
                            centerOrderNoPayAdapter.RefreshData(order_list);
                        } else {
                            centerOrderOutsideAdapter.RefreshData(order_list);
                        }

                        if (order_list.size() == Constants.PageSize)
                            //fragment_center_order_ls.ShowFoot();
                            fragment_center_order_refrash.setCanLoadMore(true);
                        if (order_list.size() < Constants.PageSize)
                            // fragment_center_order_ls.hidefoot();
                            fragment_center_order_refrash.setCanLoadMore(false);
                        break;

                    case LOAD_LOADMOREING:// 加载更多
                        fragment_center_order_refrash.setLoading(false);
                        // fragment_center_order_ls.stopLoadMore();
                        if (PDaiFu == Ket_Tage) {
                            centerOrderNoPayAdapter.AddFrashData(order_list);
                        } else {
                            centerOrderOutsideAdapter.AddFrashData(order_list);
                        }

                        if (order_list.size() == Constants.PageSize)
                            //fragment_center_order_ls.ShowFoot();
                            fragment_center_order_refrash.setCanLoadMore(true);
                        if (order_list.size() < Constants.PageSize) {
                            //fragment_center_order_ls.hidefoot();
                            fragment_center_order_refrash.setCanLoadMore(false);

                        }

                        break;
                }

                last_id = order_list.get(order_list.size() - 1).getId();

                break;
            case 1:// 提醒发货
                PromptManager.ShowMyToast(BaseContext, "已提醒卖家发货");
                last_id = "";
                IData(LOAD_INITIALIZE, Ket_Tage + "");
                break;

            case 2:// 确认收货
                PromptManager.ShowMyToast(BaseContext, "确认收货");
                last_id = "";
                IData(LOAD_INITIALIZE, Ket_Tage + "");
                break;

            case 3:// 取消订单
                PromptManager.ShowMyToast(BaseContext, "订单取消成功");
                last_id = "";
                IData(LOAD_INITIALIZE, Ket_Tage + "");
                //centerOrderOutsideAdapter.RefreshPosition(Ket_Tage, 1, SelectPosition);
                break;

            case 4:// 去付款
                BDComment data = new BDComment();
                try {
                    data = JSON.parseObject(Data.getHttpResultStr(),
                            BDComment.class);
                } catch (Exception e) {

                }
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                        ACashierDesk.class).putExtra("addOrderInfo", data));
                break;

            case 5:// 延迟收货
                PromptManager.ShowMyToast(BaseContext, "订单已延期");
                last_id = "";
                IData(LOAD_INITIALIZE, Ket_Tage + "");
                break;

            case 6:// 积分获取
                PromptManager.ShowMyToast(BaseContext, "积分获取成功");
                last_id = "";
                IData(LOAD_INITIALIZE, Ket_Tage + "");
              // centerOrderOutsideAdapter.RefreshPosition(Ket_Tage, 0, SelectPosition);
                break;

            default:
                break;
        }
    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.ShowMyToast(BaseContext, error);
        switch (LoadType) {
            case LOAD_INITIALIZE:
                fragment_center_order_ls.setVisibility(View.GONE);
                fragent_centeroder_nodata_lay.setVisibility(View.VISIBLE);
                fragent_centeroder_nodata_lay.setClickable(true);
                ShowErrorCanLoad(getResources().getString(R.string.error_null_noda));
                break;
            case LOAD_REFRESHING:// 刷新数据
                // fragment_center_order_ls.stopRefresh();

                break;
            case LOAD_LOADMOREING:// 加载更多
                //fragment_center_order_ls.stopLoadMore();

                break;

            case 1116:
                fragment_center_order_ls.setVisibility(View.VISIBLE);
                fragent_centeroder_nodata_lay.setVisibility(View.GONE);
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

            case R.id.fragent_centeroder_nodata_lay1:
                last_id = "";
                IData(LOAD_INITIALIZE, Ket_Tage + "");
                break;
            default:
                break;
        }
    }

    private void categoryOperate(String title, TextView view, int oprateType) {
        if (PDaiFu == oprateType) {
            fragment_center_order_ls.setAdapter(centerOrderNoPayAdapter);
        } else {
            fragment_center_order_ls.setAdapter(centerOrderOutsideAdapter);
        }

        changeTextColor(view);
        SetTitleTxt(title);
        closePopupWindow();


        if (Ket_Tage == oprateType) {
            closePopupWindow();
        } else {
            last_id = "";
            if (order_list != null && order_list.size() > 0) {
                order_list.clear();
            }
            Ket_Tage = oprateType;
            IData(LOAD_INITIALIZE, Ket_Tage + "");
        }
    }

    @Override
    protected void InItBundle(Bundle bundle) {
    }

    @Override
    protected void SaveBundle(Bundle bundle) {
    }

    /**
     * 点击左侧按钮的监听事件
     */
    public void title_left_bt(View v) {
        finish();

        overridePendingTransition(R.anim.push_rigth_in, R.anim.push_rigth_out);
        PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext, AMainTab.class).putExtra("a", "1"));
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext, AMainTab.class));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        Ket_Tage = PAll;
        last_id = "";
        popupWindow = null;
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

}
