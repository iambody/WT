package io.vtown.WeiTangApp.ui.title.center.myorder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.centerorder.BDCenterOrderDetail;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.DateUtils;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.ViewUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CopyTextView;
import io.vtown.WeiTangApp.comment.view.DotView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.APaySucceed;
import io.vtown.WeiTangApp.ui.comment.AWeb;
import io.vtown.WeiTangApp.ui.comment.im.AChatLoad;
import io.vtown.WeiTangApp.ui.comment.order.ACenterMyOrder;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.account.ACashierDesk;
import io.vtown.WeiTangApp.ui.title.center.set.AAddressManage;
import io.vtown.WeiTangApp.ui.title.mynew.ANew;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-6-30 下午5:36:55 我的-->我的订单-->订单详情
 */
public class ACenterMyOrderDetail extends ATitleBase {

    /**
     * member_id
     */
    private String member_id;
    /**
     * 订单号
     */
    private String seller_order_sn;

    /**
     * 订单状态
     */
    private int ket_Tage;
    /**
     * 地址信息
     */
    private View center_my_order_address;
    /**
     * 订单信息
     */
    private View center_my_order_order_message;
    /**
     * 卖家名称
     */
    private TextView tv_center_my_order_shop_name;
    /**
     * 联系卖家
     */
    private LinearLayout ll_center_my_order_contact_seller;
    /**
     * 商品列表
     */
    private CompleteListView lv_center_my_order_goods;
    /**
     * 商品个数
     */
    private TextView tv_center_my_order_good_count;
    /**
     * 总价格
     */
    private TextView tv_center_my_order_total_price;
    /**
     * 运费
     */
    private TextView tv_center_my_order_post_price;
    /**
     * 取消订单和去付款布局
     */
    private LinearLayout ll_center_my_order_cancel_and_to_pay;
    /**
     * 取消订单
     */
    private TextView tv_center_my_order_cancel_order;
    /**
     * 去付款
     */
    private TextView tv_center_my_order_to_pay;
    /**
     * 地址图标
     */
    private ImageView commentview_add_iv;
    /**
     * 收货人
     */
    private TextView commentview_add_name;
    /**
     * 联系电话
     */
    private TextView commentview_add_phone;
    /**
     * 收货地址
     */
    private TextView commentview_add_address;
    /**
     * 右箭头
     */
    private ImageView iv_right_arrow;
    /**
     * 订单编号
     */
    private TextView tv_order_id;
    /**
     * 下单时间
     */
    private TextView tv_ordering_time;
    /**
     * 支付时间
     */
    private TextView tv_pay_time;

    /**
     * 取消订单Dialog
     */
    private CustomDialog dialog;

    /**
     * 获取到的数据
     */
    private BDCenterOrderDetail order_detail = null;

    /**
     * 修改地址的请求码
     */
    private static final int REQUEST_CODE_ADDRESS = 212;
    /**
     * 付款时间布局
     */
    private LinearLayout ll_pay_time;
    /**
     * 申请退款和提醒发货
     */
    private LinearLayout ll_center_my_order_apply_refund_and_remind_send_out;
    /**
     * 申请退款
     */
    private TextView tv_center_my_order_apply_refund;
    /**
     * 提醒发货
     */
    private TextView tv_center_my_order_remind_send_out;
    /**
     * 再次购买和确认订单
     */
    private LinearLayout ll_center_my_order_buy_agian_and_confirm;
    /**
     * 延期收货
     */
    private TextView tv_center_my_order_delayreceive;
    /**
     * 确认订单
     */
    private TextView tv_center_my_order_confirm;
    /**
     * 查看物流信息布局
     */
    private LinearLayout ll_center_my_order_look_express_message;
    /**
     * 查看物流
     */
    private View look_center_my_order_express_speed;
    /**
     * 物流进度布局
     */
    private LinearLayout ll_center_my_order_good_express_speed;
    /**
     * 物流进度列表
     */
    private CompleteListView lv_center_my_order_good_express_speed;
    /**
     * 快递单号和快递名称
     */
    private LinearLayout ll_center_my_order_express_message;
    /**
     * 快递单号
     */
    private CopyTextView tv_center_my_order_express_numb;
    /**
     * 快递名称
     */
    private TextView tv_center_my_order_express_name;
    /**
     * 查看物流条目
     */
    private TextView comment_txtarrow_title;
    /**
     * 查看物流条目箭头
     */
    private ImageView iv_comment_right_arrow;

    private boolean isRotate = false;
    /**
     * 物流信息AP
     */
    private CenterExpressMessageAdapter centerExpressMessageAdapter;
    /**
     * 仲裁状态
     */
    private TextView tv_center_my_order_arbitration;
    /**
     * 再次购买
     */
    private TextView tv_center_my_order_close_buy_agian;
    /**
     * 获取到数据时显示的布局
     */
    private RelativeLayout center_my_order_detail_outlay;
    /**
     * 获取数据失败时显示的布局
     */
    private View center_my_order_detail_nodata_lay;

    /**
     * 当此标志是获取详情数据时，失败时需要显示失败布局
     */
    private boolean isGetDetail = false;

    /**
     * 申请退款中
     */
    private TextView tv_center_my_order_refund_applying;
    /**
     * 已延期收货
     */
    private TextView tv_center_my_order_is_delay;
    /**
     * 订单状态Lable
     */
    private TextView tv_center_order_good_express_title;
    /**
     * 使用余额和卡券
     */
    private LinearLayout ll_center_order_used_balance_and_coupons;
    /**
     * 使用余额
     */
    private TextView tv_center_order_used_balance;
    /**
     * 使用卡券
     */
    private TextView tv_center_order_used_coupons;
    /**
     * 使用余额和卡券分割线
     */
    private View line_left_center_order;
    private TextView tv_center_my_order_get_integral;
    private LinearLayout ll_center_my_order_get_integral_tips;
    private TextView tv_center_my_order_get_integral_tips;
    private RelativeLayout rl_relation_seller;
    private LinearLayout ll_center_my_order_order_note_insurance;
    private LinearLayout ll_center_my_order_order_insurance;
    private LinearLayout ll_center_my_order_order_note;
    private TextView tv_center_my_order_order_insurance;
    private TextView tv_center_my_order_order_note;
    private View line_insurance_note;
    private ImageView iv_center_order_what_is_insurance;
    private ImageView iv_detail_share_red_packets;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_center_my_order_detail);
        // 注册事件
        EventBus.getDefault().register(this, "OnGetMessage", BMessage.class);
        SetTitleHttpDataLisenter(this);
        Intent intent = getIntent();
        member_id = intent.getStringExtra("member_id");
        seller_order_sn = intent.getStringExtra("seller_order_sn");
        ket_Tage = intent.getIntExtra("Key_TageStr", 0);
        IView();
        IData();
    }

    /**
     * 获取订单详情
     *
     * @param member_id
     * @param seller_order_sn
     */
    private void IData() {
        PromptManager.showtextLoading(BaseContext,
                getResources()
                        .getString(R.string.xlistview_header_hint_loading));
        HashMap<String, String> map = new HashMap<String, String>();
        isGetDetail = true;
        map.put("member_id", member_id);
        map.put("seller_order_sn", seller_order_sn);
        FBGetHttpData(map, Constants.Center_My_Order_Detail, Method.GET, 0,
                LOAD_INITIALIZE);
    }

    /**
     * 取消订单
     */
    private void CancelMyOrder() {
        HashMap<String, String> map = new HashMap<String, String>();
        isGetDetail = false;
        map.put("member_id", member_id);
        map.put("seller_order_sn", seller_order_sn);
        FBGetHttpData(map, Constants.Center_My_Order_Cancel, Method.PUT, 1,
                LOAD_INITIALIZE);
    }

    /**
     * 提醒发货
     */
    private void RemindSendOut() {
        HashMap<String, String> map = new HashMap<String, String>();
        isGetDetail = false;
        map.put("member_id", member_id);
        map.put("seller_order_sn", seller_order_sn);
        FBGetHttpData(map, Constants.Center_My_Order_Remind_Send_Out,
                Method.PUT, 2, LOAD_INITIALIZE);
    }

    /**
     * 查看物流信息
     */
    private void LookCenterOrderExpress() {
        HashMap<String, String> map = new HashMap<String, String>();
        isGetDetail = false;
        map.put("seller_order_sn", seller_order_sn);
        FBGetHttpData(map, Constants.Look_Express_Message, Method.GET, 3,
                LOAD_INITIALIZE);
    }

    /**
     * 确认收货
     */
    private void ConfirmShouhuo() {
        HashMap<String, String> map = new HashMap<String, String>();
        isGetDetail = false;
        map.put("member_id", member_id);
        map.put("seller_order_sn", seller_order_sn);
        FBGetHttpData(map, Constants.Center_My_Order_Confirm_Order, Method.PUT,
                4, LOAD_INITIALIZE);
    }

    /**
     * 延迟收货
     *
     * @param seller_order_sn
     * @param member_id
     */
    private void DelayReceive() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", member_id);
        map.put("seller_order_sn", seller_order_sn);
        FBGetHttpData(map, Constants.Center_My_Order_Delayreceive, Method.PUT,
                5, LOAD_INITIALIZE);
    }


    /**
     * 快速获取积分
     *
     * @param seller_order_sn
     * @param member_id
     */
    private void getIntegral() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", member_id);
        map.put("seller_order_sn", seller_order_sn);

        FBGetHttpData(map, Constants.Center_My_Order_Integral, Method.PUT,
                6, LOAD_INITIALIZE);
    }

    /**
     * 初始化控件
     */
    private void IView() {

        center_my_order_detail_outlay = (RelativeLayout) findViewById(R.id.center_my_order_detail_outlay);
        center_my_order_detail_nodata_lay = findViewById(R.id.center_my_order_detail_nodata_lay);
        IDataView(center_my_order_detail_outlay,
                center_my_order_detail_nodata_lay, NOVIEW_INITIALIZE);

        // 地址信息
        center_my_order_address = findViewById(R.id.center_my_order_address);
        commentview_add_iv = (ImageView) center_my_order_address
                .findViewById(R.id.commentview_add_iv);

        commentview_add_name = (TextView) center_my_order_address
                .findViewById(R.id.commentview_add_name);
        commentview_add_phone = (TextView) center_my_order_address
                .findViewById(R.id.commentview_add_phone);
        commentview_add_address = (TextView) center_my_order_address
                .findViewById(R.id.commentview_add_address);
        iv_right_arrow = (ImageView) center_my_order_address
                .findViewById(R.id.iv_right_arrow);

        // 订单信息
        center_my_order_order_message = findViewById(R.id.center_my_order_order_message);
        tv_order_id = (TextView) center_my_order_order_message
                .findViewById(R.id.tv_order_id);
        tv_ordering_time = (TextView) center_my_order_order_message
                .findViewById(R.id.tv_ordering_time);
        tv_pay_time = (TextView) center_my_order_order_message
                .findViewById(R.id.tv_pay_time);
        ll_pay_time = (LinearLayout) center_my_order_order_message
                .findViewById(R.id.ll_pay_time);
        //让订单编号文本有可复制功能
        StrUtils.SetTextViewCopy(tv_order_id);

        //余额和卡券

        ll_center_order_used_balance_and_coupons = (LinearLayout) findViewById(R.id.ll_center_order_used_balance_and_coupons);
        tv_center_order_used_balance = (TextView) findViewById(R.id.tv_center_order_used_balance);
        tv_center_order_used_coupons = (TextView) findViewById(R.id.tv_center_order_used_coupons);
        line_left_center_order = findViewById(R.id.line_left_center_order);

        //正品险和备注留言
        ll_center_my_order_order_note_insurance = (LinearLayout) findViewById(R.id.ll_center_my_order_order_note_insurance);
        ll_center_my_order_order_insurance = (LinearLayout) findViewById(R.id.ll_center_my_order_order_insurance);
        ll_center_my_order_order_note = (LinearLayout) findViewById(R.id.ll_center_my_order_order_note);
        tv_center_my_order_order_insurance = (TextView) findViewById(R.id.tv_center_my_order_order_insurance);
        tv_center_my_order_order_note = (TextView) findViewById(R.id.tv_center_my_order_order_note);
        line_insurance_note = findViewById(R.id.line_insurance_note);
        iv_center_order_what_is_insurance = (ImageView) findViewById(R.id.iv_center_order_what_is_insurance);
        tv_center_my_order_order_insurance.setOnClickListener(this);
        iv_center_order_what_is_insurance.setOnClickListener(this);


        tv_center_my_order_is_delay = (TextView) findViewById(R.id.tv_center_my_order_is_delay);
        rl_relation_seller = (RelativeLayout) findViewById(R.id.rl_relation_seller);
        tv_center_my_order_shop_name = (TextView) findViewById(R.id.tv_center_my_order_shop_name);
        ll_center_my_order_contact_seller = (LinearLayout) findViewById(R.id.ll_center_my_order_contact_seller);
        lv_center_my_order_goods = (CompleteListView) findViewById(R.id.lv_center_my_order_goods);
        tv_center_my_order_good_count = (TextView) findViewById(R.id.tv_center_my_order_good_count);
        tv_center_my_order_total_price = (TextView) findViewById(R.id.tv_center_my_order_total_price);
        tv_center_my_order_post_price = (TextView) findViewById(R.id.tv_center_my_order_post_price);

        ll_center_my_order_get_integral_tips = (LinearLayout) findViewById(R.id.ll_center_my_order_get_integral_tips);
        tv_center_my_order_get_integral_tips = (TextView) findViewById(R.id.tv_center_my_order_get_integral_tips);

        tv_center_order_good_express_title = (TextView) findViewById(R.id.tv_center_order_good_express_title);
        lv_center_my_order_good_express_speed = (CompleteListView) findViewById(R.id.lv_center_my_order_good_express_speed);
        ll_center_my_order_express_message = (LinearLayout) findViewById(R.id.ll_center_my_order_express_message);
        tv_center_my_order_express_numb = (CopyTextView) findViewById(R.id.tv_center_my_order_express_numb);

        tv_center_my_order_express_name = (TextView) findViewById(R.id.tv_center_my_order_express_name);

        // 取消订单和去付款
        ll_center_my_order_cancel_and_to_pay = (LinearLayout) findViewById(R.id.ll_center_my_order_cancel_and_to_pay);
        tv_center_my_order_cancel_order = (TextView) findViewById(R.id.tv_center_my_order_cancel_order);
        tv_center_my_order_to_pay = (TextView) findViewById(R.id.tv_center_my_order_to_pay);

        // 申请退款和提醒发货
        ll_center_my_order_apply_refund_and_remind_send_out = (LinearLayout) findViewById(R.id.ll_center_my_order_apply_refund_and_remind_send_out);
        tv_center_my_order_get_integral = (TextView) findViewById(R.id.tv_center_my_order_get_integral);
        tv_center_my_order_apply_refund = (TextView) findViewById(R.id.tv_center_my_order_apply_refund);
        tv_center_my_order_remind_send_out = (TextView) findViewById(R.id.tv_center_my_order_remind_send_out);


        // 延迟发货和确认订单
        ll_center_my_order_buy_agian_and_confirm = (LinearLayout) findViewById(R.id.ll_center_my_order_buy_agian_and_confirm);
        tv_center_my_order_delayreceive = (TextView) findViewById(R.id.tv_center_my_order_delayreceive);
        tv_center_my_order_confirm = (TextView) findViewById(R.id.tv_center_my_order_confirm);

        // 退货/仲裁--仲裁状态
        tv_center_my_order_arbitration = (TextView) findViewById(R.id.tv_center_my_order_arbitration);

        iv_detail_share_red_packets = (ImageView) findViewById(R.id.iv_detail_share_red_packets);
        iv_detail_share_red_packets.setOnClickListener(this);
        ViewUtils.setAnimator(iv_detail_share_red_packets);

        // 已关闭--再次购买

        tv_center_my_order_close_buy_agian = (TextView) findViewById(R.id.tv_center_my_order_close_buy_agian);
        // 申请退款中
        tv_center_my_order_refund_applying = (TextView) findViewById(R.id.tv_center_my_order_refund_applying);
        switch (ket_Tage) {
            case ACenterMyOrder.PYiFu:// 已付款
                commentview_add_iv.setVisibility(View.GONE);
                iv_right_arrow.setVisibility(View.GONE);
                center_my_order_address.setEnabled(false);
                ll_center_my_order_apply_refund_and_remind_send_out
                        .setVisibility(View.VISIBLE);
                iv_detail_share_red_packets.setVisibility(View.VISIBLE);

                break;
            case ACenterMyOrder.PDaiShou:// 待收货
                commentview_add_iv.setVisibility(View.GONE);
                iv_right_arrow.setVisibility(View.GONE);
                center_my_order_address.setEnabled(false);
                ll_center_my_order_buy_agian_and_confirm.setVisibility(View.GONE);
                iv_detail_share_red_packets.setVisibility(View.VISIBLE);
                // ll_center_my_order_look_express_message.setVisibility(View.GONE);
                break;

            case ACenterMyOrder.PTuiKuan:// 申请退款中。。。买家已申请退款
                center_my_order_address.setEnabled(false);
                commentview_add_iv.setVisibility(View.GONE);
                iv_right_arrow.setVisibility(View.GONE);
                tv_center_my_order_refund_applying.setVisibility(View.VISIBLE);
                iv_detail_share_red_packets.setVisibility(View.GONE);
            break;

            case ACenterMyOrder.PZhongCai:// 仲裁中。。。买家申请退款，卖家拒绝退款
                center_my_order_address.setEnabled(false);
                commentview_add_iv.setVisibility(View.GONE);
                iv_right_arrow.setVisibility(View.GONE);
                tv_center_my_order_arbitration.setVisibility(View.VISIBLE);
                iv_detail_share_red_packets.setVisibility(View.GONE);
                break;
            case ACenterMyOrder.PTuikuanSuccess1:
            case ACenterMyOrder.PTuikuanSuccess2:
            case ACenterMyOrder.PAgreeTuiKuan:// 卖家已同意退款
                commentview_add_iv.setVisibility(View.GONE);
                iv_right_arrow.setVisibility(View.GONE);
                center_my_order_address.setEnabled(false);
                // ll_center_my_order_look_express_message.setVisibility(View.GONE);
                tv_center_my_order_close_buy_agian.setVisibility(View.GONE);
                iv_detail_share_red_packets.setVisibility(View.GONE);
                break;

            case ACenterMyOrder.PCancel:// 已取消
                commentview_add_iv.setVisibility(View.GONE);
                iv_right_arrow.setVisibility(View.GONE);
                center_my_order_address.setEnabled(false);
                tv_center_my_order_close_buy_agian.setVisibility(View.GONE);
                ll_pay_time.setVisibility(View.GONE);
                iv_detail_share_red_packets.setVisibility(View.GONE);
                break;
            case ACenterMyOrder.PClose:// 已关闭
                commentview_add_iv.setVisibility(View.GONE);
                iv_right_arrow.setVisibility(View.GONE);
                center_my_order_address.setEnabled(false);
                // ll_center_my_order_look_express_message.setVisibility(View.GONE);
                tv_center_my_order_close_buy_agian.setVisibility(View.GONE);
                iv_detail_share_red_packets.setVisibility(View.VISIBLE);
                break;

        }
        center_my_order_address.setOnClickListener(this);
        tv_center_my_order_cancel_order.setOnClickListener(this);
        tv_center_my_order_to_pay.setOnClickListener(this);
        tv_center_my_order_apply_refund.setOnClickListener(this);
        tv_center_my_order_remind_send_out.setOnClickListener(this);
        tv_center_my_order_delayreceive.setOnClickListener(this);
        tv_center_my_order_confirm.setOnClickListener(this);
        // look_center_my_order_express_speed.setOnClickListener(this);
        tv_center_my_order_close_buy_agian.setOnClickListener(this);
        center_my_order_detail_nodata_lay.setOnClickListener(this);
        rl_relation_seller.setOnClickListener(this);
        ll_center_my_order_contact_seller.setOnClickListener(this);
        tv_center_my_order_get_integral.setOnClickListener(this);

    }

    /**
     * 刷新控件数据
     *
     * @param order_detail2
     */
    private void RefreshView(final BDCenterOrderDetail order_detail2) {

        StrUtils.SetTxt(tv_center_my_order_shop_name,
                order_detail2.getSeller_name());

        String name = getResources().getString(R.string.consignee_name_order);


        StrUtils.SetColorsTxt(BaseContext, tv_center_order_used_balance, R.color.app_gray, "使用余额：", String.format("%1$s元", StrUtils.SetTextForMony(order_detail2.getBalance_price())));
        StrUtils.SetColorsTxt(BaseContext, tv_center_order_used_coupons, R.color.app_gray, "使用卡券：", String.format("%1$s元", StrUtils.SetTextForMony(order_detail2.getCoupons_price())));

        if(!StrUtils.isEmpty(order_detail2.getOrder_note()) || !order_detail2.getInsurance().equals("0")){
            ll_center_my_order_order_note_insurance.setVisibility(View.VISIBLE);
            if(!StrUtils.isEmpty(order_detail2.getOrder_note())){
                ll_center_my_order_order_note.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(tv_center_my_order_order_note,order_detail2.getOrder_note());
            }else{
                ll_center_my_order_order_note.setVisibility(View.GONE);
                line_insurance_note.setVisibility(View.GONE);
            }

            if(!order_detail2.getInsurance().equals("0")){
                ll_center_my_order_order_insurance.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(tv_center_my_order_order_insurance,"已购买正品保险");
            }else{
                ll_center_my_order_order_insurance.setVisibility(View.GONE);
                line_insurance_note.setVisibility(View.GONE);
            }
        }else{
            ll_center_my_order_order_note_insurance.setVisibility(View.GONE);
        }
        //显示使用余额和卡券，只有金额不为0的时候才显示
        if (0 != Integer.parseInt(order_detail2.getBalance_price()) || 0 != Integer.parseInt(order_detail2.getCoupons_price())) {
            ll_center_order_used_balance_and_coupons.setVisibility(View.VISIBLE);
            if (0 != Integer.parseInt(order_detail2.getBalance_price())) {
                tv_center_order_used_balance.setVisibility(View.VISIBLE);
            } else {
                tv_center_order_used_balance.setVisibility(View.GONE);
                line_left_center_order.setVisibility(View.GONE);
            }
            if (0 != Integer.parseInt(order_detail2.getCoupons_price())) {
                tv_center_order_used_coupons.setVisibility(View.VISIBLE);
            } else {
                tv_center_order_used_coupons.setVisibility(View.GONE);
                line_left_center_order.setVisibility(View.GONE);
            }
        } else {
            ll_center_order_used_balance_and_coupons.setVisibility(View.GONE);
        }

        int Order_status = Integer.parseInt(order_detail2.getOrder_status());
        if (ACenterMyOrder.PDaiFu == Order_status
                || ACenterMyOrder.PCancel == Order_status) {
            StrUtils.SetTxt(tv_order_id, order_detail2.getOrder_sn());
        } else {
            StrUtils.SetTxt(tv_order_id, order_detail2.getSeller_order_sn());
        }


        if(ACenterMyOrder.PDaiShou != Order_status){
            tv_center_my_order_confirm.setVisibility(View.GONE);
            tv_center_my_order_confirm.setEnabled(false);
        }else{
            tv_center_my_order_confirm.setVisibility(View.VISIBLE);
            tv_center_my_order_confirm.setEnabled(true);
        }

        // 如果没有延期收货，则显示确认收货和延期收货
        if (ACenterMyOrder.PDaiShou == Order_status) {
            // 延迟时间10天，只有10天后才显示延迟收货
            long delaytime = Long.parseLong(order_detail2.getCreate_time())
                    + (10 * 24 * 60 * 60);
            // 当前时间
            long nowtime = System.currentTimeMillis() / 1000;
            if (nowtime < delaytime) {
                ll_center_my_order_buy_agian_and_confirm
                        .setVisibility(View.VISIBLE);
                tv_center_my_order_delayreceive.setVisibility(View.GONE);
            } else {
                if ("0".equals(order_detail2.getDelaynumber())) {
                    ll_center_my_order_buy_agian_and_confirm
                            .setVisibility(View.VISIBLE);
                } else {
                    ll_center_my_order_buy_agian_and_confirm
                            .setVisibility(View.GONE);
                    tv_center_my_order_is_delay.setVisibility(View.VISIBLE);
                }
            }

        }

        // 只有未申请过退款才能申请退款,如果已申请退款就不能提醒发货了
        if (ACenterMyOrder.PYiFu == Order_status) {
            if ("0".equals(order_detail2.getRefund())) {
                tv_center_my_order_apply_refund.setVisibility(View.VISIBLE);




                if ("0".equals(order_detail2.getRemind_time())) {
                    tv_center_my_order_remind_send_out
                            .setVisibility(View.VISIBLE);
                } else {
                    tv_center_my_order_remind_send_out.setVisibility(View.GONE);
                }
            } else {
                tv_center_my_order_apply_refund.setVisibility(View.GONE);
                tv_center_my_order_remind_send_out.setVisibility(View.GONE);
            }

            if (0 == order_detail2.getIs_have_point()) {
                ll_center_my_order_get_integral_tips.setVisibility(View.GONE);
                tv_center_my_order_get_integral.setVisibility(View.GONE);
            } else {
                if (0 == order_detail2.getAdvance_point()) {
                    // myItem.fragment_center_order_is_get_integral.setVisibility(View.GONE);
                    ll_center_my_order_get_integral_tips.setVisibility(View.GONE);
                    tv_center_my_order_get_integral.setVisibility(View.VISIBLE);
                } else {
                    tv_center_my_order_apply_refund.setVisibility(View.GONE);
                    tv_center_my_order_get_integral.setVisibility(View.GONE);
                    ll_center_my_order_get_integral_tips.setVisibility(View.VISIBLE);
                    StrUtils.SetTxt(tv_center_my_order_get_integral_tips, "温馨提示：您已在" + DateUtils.timeStampToStr(Long.parseLong(order_detail2.getPoint_time())) + "获取积分,获取积分后不能再进行申请退款操作");
                    /// myItem.fragment_center_order_is_get_integral.setVisibility(View.VISIBLE);

                }
            }
        }

//		StrUtils.SetColorsTxt(BaseContext, commentview_add_name,
//				R.color.app_gray, getString(R.string.tv_commentview_name),
//				order_detail2.getUsername());
        StrUtils.SetTxt(commentview_add_name, order_detail2.getUsername());

        StrUtils.SetTxt(commentview_add_phone, order_detail2.getMobile());
        StrUtils.SetTxt(commentview_add_address, order_detail2.getProvince()
                + order_detail2.getCity() + order_detail2.getArea()
                + order_detail2.getAddress());

        StrUtils.SetTxt(tv_ordering_time, StrUtils.longtostr(Long
                .parseLong(order_detail2.getCreate_time())));
        StrUtils.SetTxt(tv_pay_time,
                StrUtils.longtostr(Long.parseLong(order_detail2.getPay_time())));
        StrUtils.SetTxt(tv_center_my_order_good_count,
                String.format("共%1$s件商品", order_detail2.getNumber()));

        // float price =
        // Float.parseFloat(order_detail.getGoods_price())+Float.parseFloat(order_detail.getPostage());
//		StrUtils.SetTxt(tv_center_my_order_total_price, String.format("%1$s元",
//				StrUtils.SetTextForMony(order_detail2.getOrder_total_price())));
        float postageF = Float.parseFloat(order_detail2.getPostage());
        float good_priceF = Float.parseFloat(order_detail2.getGoods_price());
        StrUtils.SetMoneyFormat(BaseContext, tv_center_my_order_total_price, (postageF+good_priceF)+"", 17);

        String postage = String.format("(含运费%1$s元)",
                StrUtils.SetTextForMony(postageF + ""));
        StrUtils.SetTxt(tv_center_my_order_post_price,
                postageF == 0.0f ? "(免邮费)" : postage);

        CenterMyOrderAdater myOrderAdater = new CenterMyOrderAdater(
                R.layout.item_center_my_order_lv, order_detail2.getGoods());
        lv_center_my_order_goods.setAdapter(myOrderAdater);
        // 点击商品Item跳转商品详情页面
        lv_center_my_order_goods
                .setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        String goods_id = order_detail2.getGoods()
                                .get(position).getGoods_id();
                        Intent intent = new Intent(BaseContext,
                                AGoodDetail.class);
                        intent.putExtra("goodid", goods_id);
                        PromptManager.SkipActivity(BaseActivity, intent);
                    }
                });
        // 显示快递单号和物流公司
        if (!StrUtils.isEmpty(order_detail2.getExpress_number())
                && !StrUtils.isEmpty(order_detail2.getExpress_name())) {
            ll_center_my_order_express_message.setVisibility(View.VISIBLE);
            StrUtils.SetColorsTxt(BaseContext, tv_center_my_order_express_numb,
                    R.color.app_gray, "快递单号：",
                    order_detail2.getExpress_number());
            StrUtils.SetColorsTxt(BaseContext, tv_center_my_order_express_name,
                    R.color.app_gray, "物流公司：", order_detail2.getExpress_name());
        } else {
            ll_center_my_order_express_message.setVisibility(View.GONE);
        }

        List<BLComment> express_data = new ArrayList<BLComment>();
        try {
            express_data = JSON.parseArray(order_detail2.getLogisticinfo(),
                    BLComment.class);
        } catch (Exception e) {

        }
        if (ACenterMyOrder.PDaiShou == Order_status
                || ACenterMyOrder.PClose == Order_status) {
            tv_center_order_good_express_title.setVisibility(View.VISIBLE);

            if (express_data.size() == 0) {
                tv_center_order_good_express_title.setVisibility(View.GONE);
            } else {

                StrUtils.SetTxt(tv_center_order_good_express_title, "物流状态：");
                lv_center_my_order_good_express_speed
                        .setVisibility(View.VISIBLE);
                centerExpressMessageAdapter = new CenterExpressMessageAdapter(
                        R.layout.item_center_my_order_express_message);
                lv_center_my_order_good_express_speed
                        .setAdapter(centerExpressMessageAdapter);
                centerExpressMessageAdapter.RefreshData(express_data);

                lv_center_my_order_good_express_speed.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        BLComment item = (BLComment) centerExpressMessageAdapter.getItem(position);
                        if (!StrUtils.isEmpty(item.getContext())) {
                            final String telnum = StrUtils.getTelnum(item.getContext());
                            if(!StrUtils.isEmpty(telnum)){
                                ShowCustomDialog("联系：" + telnum, "拨号", "取消", new IDialogResult() {
                                    @Override
                                    public void LeftResult() {
                                        Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri
                                                .parse("tel:" + telnum));
                                        startActivity(intentPhone);
                                    }

                                    @Override
                                    public void RightResult() {

                                    }
                                });
                            }

                        }
                    }
                });

            }
        }
    }


    @Override
    protected void InitTile() {

        SetTitleTxt("订单详情");

    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

        switch (Data.getHttpResultTage()) {
            case 0:// 获取详情数据
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    return;
                }
                order_detail = new BDCenterOrderDetail();
                try {
                    order_detail = JSON.parseObject(Data.getHttpResultStr(),
                            BDCenterOrderDetail.class);
                } catch (Exception e) {
                    DataError("解析失败", 1);
                }
                IDataView(center_my_order_detail_outlay,
                        center_my_order_detail_nodata_lay, NOVIEW_RIGHT);
                RefreshView(order_detail);
                break;

            case 1:// 取消订单

                EventBus.getDefault().post(
                        new BMessage(BMessage.Tage_Center_Order_Updata));
                IData();
                break;

            case 2:// 提醒发货
                PromptManager.ShowMyToast(BaseContext, "提醒发货成功");
                EventBus.getDefault().post(
                        new BMessage(BMessage.Tage_Center_Order_Updata));
                IData();
                break;

            case 3:// 查看物流
            /*
             * //ll_center_my_order_good_express_speed.setVisibility(View.VISIBLE
			 * ); /List<BLComment> express_datas = new ArrayList<BLComment>();
			 * try { express_datas = JSON.parseArray(Data.getHttpResultStr(),
			 * BLComment.class); } catch (Exception e) { DataError("解析失败", 1); }
			 * centerExpressMessageAdapter.RefreshData(express_datas);
			 */
                break;

            case 4:// 确认收货
                PromptManager.ShowMyToast(BaseContext, "确认收货成功");
                EventBus.getDefault().post(
                        new BMessage(BMessage.Tage_Center_Order_Updata));
                IData();

                break;

            case 5:// 延期收货
                PromptManager.ShowMyToast(BaseContext, "订单已延期");
                EventBus.getDefault().post(
                        new BMessage(BMessage.Tage_Center_Order_Updata));
                IData();
                break;

            case 6:
                PromptManager.ShowMyToast(BaseContext, "积分获取成功");
                EventBus.getDefault().post(
                        new BMessage(BMessage.Tage_Center_Order_Updata));
                IData();
                break;
        }

    }

    @Override
    protected void DataError(String error, int LoadType) {
        if (dialog != null) {
            dialog.dismiss();
        }
        PromptManager.ShowMyToast(BaseContext, error);

        if (LOAD_INITIALIZE == LoadType && isGetDetail) {
            IDataView(center_my_order_detail_outlay,
                    center_my_order_detail_nodata_lay, NOVIEW_ERROR);
            ShowErrorCanLoad(getResources().getString(R.string.error_null_noda));
        }
    }

    @Override
    protected void NetConnect() {
        NetError.setVisibility(View.GONE);
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

            case R.id.center_my_order_address:// 地址条目
                Intent intent = new Intent(BaseActivity, AAddressManage.class);
                intent.putExtra("NeedFinish", true);
                PromptManager.SkipResultActivity(BaseActivity, intent,
                        REQUEST_CODE_ADDRESS);
                break;
            case R.id.tv_center_my_order_cancel_order:// 取消订单
                if (CheckNet(BaseContext))
                    return;
                ShowCustomDialog("确认取消订单吗？", "取消", "确认", new IDialogResult() {
                    @Override
                    public void RightResult() {
                        CancelMyOrder();
                    }

                    @Override
                    public void LeftResult() {
                    }
                });
                break;

            case R.id.tv_center_my_order_to_pay:// 去付款----->收银台页面
                if (CheckNet(BaseContext))
                    return;

                ShowCustomDialog("确认去付款？", "取消", "确认", new IDialogResult() {
                    @Override
                    public void RightResult() {
                        if (order_detail != null) {
                            PromptManager.SkipActivity(BaseActivity, new Intent(
                                    BaseContext, ACashierDesk.class).putExtra(
                                    "addOrderInfo", order_detail));
                        }
                    }

                    @Override
                    public void LeftResult() {
                    }
                });

                break;

            case R.id.tv_center_my_order_get_integral://获取积分


                if (CheckNet(BaseContext))
                    return;

                ShowCustomDialog("确认立即获取积分吗？\n获取积分后不能再申请退款", "取消", "确认", new IDialogResult() {
                    @Override
                    public void RightResult() {
                        getIntegral();
                    }

                    @Override
                    public void LeftResult() {
                    }
                });
                break;

            case R.id.tv_center_my_order_apply_refund:// 申请退款
                if (CheckNet(BaseContext))
                    return;
                Intent intent2 = new Intent(BaseContext, AApplyTuikuan.class);
                intent2.putExtra("seller_order_sn", seller_order_sn);
                intent2.putExtra("FromTag", AApplyTuikuan.Tag_From_Center_My_Order);
                PromptManager.SkipActivity(BaseActivity, intent2);
                break;

            case R.id.tv_center_my_order_remind_send_out:// 提醒发货
                if (CheckNet(BaseContext))
                    return;
                RemindSendOut();
                break;

            // case R.id.look_center_my_order_express_speed:// 查看物流
            // if (CheckNet(BaseContext))
            // return;
            // dd
            // //isRotate = !isRotate;
            // //SetAnimRotate(iv_comment_right_arrow, isRotate);
            // break;

            case R.id.tv_center_my_order_confirm:// 确认收货
                if (CheckNet(BaseContext))
                    return;
                ShowCustomDialog("确认收货？", "取消", "确认", new IDialogResult() {
                    @Override
                    public void RightResult() {
                        ConfirmShouhuo();
                    }

                    @Override
                    public void LeftResult() {
                    }
                });

                break;

            case R.id.tv_center_my_order_delayreceive:// 延期收货
                ShowCustomDialog("每个商品只允许延期一次，延期为3天，确认延期收货吗？", "取消", "确认",
                        new IDialogResult() {
                            @Override
                            public void RightResult() {
                                if (CheckNet(BaseContext))
                                    return;
                                DelayReceive();
                            }

                            @Override
                            public void LeftResult() {
                            }
                        });
                break;

            case R.id.center_my_order_detail_nodata_lay:// 重新加载数据
                IData();
                break;

            case R.id.ll_center_my_order_contact_seller:// 联系卖家
                if (CheckNet(BaseContext))
                    return;
                if (!StrUtils.isEmpty(order_detail.getSeller_id()))
                    PromptManager.SkipActivity(
                            BaseActivity,
                            new Intent(BaseActivity, AChatLoad.class)
                                    .putExtra(AChatLoad.Tage_TageId,
                                            order_detail.getSeller_id())
                                    .putExtra(AChatLoad.Tage_Name,
                                            order_detail.getSeller_name())
                                    .putExtra(AChatLoad.Tage_Iv,
                                            order_detail.getAvatar()));
                break;

            case R.id.tv_center_my_order_order_insurance:
            case R.id.iv_center_order_what_is_insurance:
                PromptManager.SkipActivity(BaseActivity, new Intent(
                        BaseActivity, AWeb.class).putExtra(
                        AWeb.Key_Bean,
                        new BComment(Constants.GoodsInsurance_Url, getResources().getString(R.string.zhengpin_insurance))));
                break;


//            case R.id.rl_relation_seller://查看店铺
//                BComment bComment = new BComment(order_detail.getSeller_id(), order_detail.getSeller_name());
//               String  is_brand = order_detail.getIs_brand();
//                if ("0".equals(is_brand)) {
//                    intent = new Intent(BaseContext, AShopDetail.class);
//                } else {
//                    intent = new Intent(BaseContext,
//                            ABrandDetail.class);
//                }
//
//                intent.putExtra(BaseKey_Bean, bComment);
//                PromptManager.SkipActivity(BaseActivity, intent);
//
//                break;

            case R.id.iv_detail_share_red_packets://红包
                Intent intent11 = new Intent(BaseContext, APaySucceed.class);
                intent11.putExtra(APaySucceed.Key_Oder,order_detail.getOrder_sn());
                PromptManager.SkipActivity(BaseActivity,intent11);
                break;
        }
    }

    /**
     * 箭头执行旋转动画
     *
     * @param v
     * @param flag
     */
    private void SetAnimRotate(View v, boolean flag) {
        RotateAnimation rotate = null;
        if (flag) {
            // LookCenterOrderExpress();
            rotate = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            rotate = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF,
                    0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            ll_center_my_order_good_express_speed.setVisibility(View.GONE);
        }

        rotate.setFillAfter(true);
        v.setAnimation(rotate);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (REQUEST_CODE_ADDRESS == requestCode && RESULT_OK == resultCode) {
            BLComment bl = (BLComment) data.getSerializableExtra("AddressInfo");
            if (bl != null) {
//				StrUtils.SetColorsTxt(BaseContext, commentview_add_name,
//						R.color.app_gray,
//						getString(R.string.tv_commentview_name), bl.getName());
                StrUtils.SetTxt(commentview_add_name, bl.getName());

                StrUtils.SetTxt(commentview_add_phone, bl.getMobile());
                StrUtils.SetTxt(
                        commentview_add_address,
                        bl.getProvince() + bl.getCity() + bl.getCounty()
                                + bl.getAddress());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void InItBundle(Bundle bundle) {
    }

    @Override
    protected void SaveBundle(Bundle bundle) {
    }

    /**
     * @author Yihuihua 商品列表
     */
    class CenterMyOrderAdater extends BaseAdapter {

        /**
         * 填充器
         */
        private LayoutInflater inflater;
        /**
         * 资源id
         */
        private int ResourceId;

        private List<BLComment> data = new ArrayList<BLComment>();

        public CenterMyOrderAdater(int ResouceId, List<BLComment> data) {
            super();
            this.inflater = LayoutInflater.from(BaseContext);
            ResourceId = ResouceId;
            this.data = data;
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
        public View getView(int arg0, View arg1, ViewGroup arg2) {

            PurcheseItem item = null;
            if (arg1 == null) {
                arg1 = inflater.inflate(ResourceId, null);
                item = new PurcheseItem();
                item.iv_center_my_order_detail_good_icon = (ImageView) arg1
                        .findViewById(R.id.iv_center_my_order_detail_good_icon);
                item.tv_center_my_order_detail_good_title = (TextView) arg1
                        .findViewById(R.id.tv_center_my_order_detail_good_title);
                item.tv_center_my_order_good_content = (TextView) arg1
                        .findViewById(R.id.tv_center_my_order_good_content);
                item.tv_center_my_order_content_value = (TextView) arg1
                        .findViewById(R.id.tv_center_my_order_content_value);
                item.tv_center_my_order_detail_good_price = (TextView) arg1
                        .findViewById(R.id.tv_center_my_order_detail_good_price);
                item.tv_center_my_order_detail_good_count = (TextView) arg1
                        .findViewById(R.id.tv_center_my_order_detail_good_count);

                ImageLoaderUtil.Load2(data.get(arg0).getGoods_cover(),
                        item.iv_center_my_order_detail_good_icon,
                        R.drawable.error_iv2);

                arg1.setTag(item);
            } else {
                item = (PurcheseItem) arg1.getTag();
            }
            String goods_type = data.get(arg0).getGoods_type();
            StrUtils.SetTxt(item.tv_center_my_order_detail_good_title, data
                    .get(arg0).getGoods_name());
            StrUtils.SetTxt(item.tv_center_my_order_content_value,
                    data.get(arg0).getGoods_standard());
            String goods_price = String.format("￥%1$s",
                    StrUtils.SetTextForMony(data.get(arg0).getGoods_price()));
            StrUtils.SetTxt(item.tv_center_my_order_detail_good_price,
                    goods_price);
            String goods_number = String.format("x%1$s", data.get(arg0)
                    .getGoods_number());
            StrUtils.SetTxt(item.tv_center_my_order_detail_good_count,
                    goods_number);

            return arg1;
        }

        class PurcheseItem {
            ImageView iv_center_my_order_detail_good_icon;
            TextView tv_center_my_order_detail_good_title,
                    tv_center_my_order_good_content,
                    tv_center_my_order_content_value,
                    tv_center_my_order_detail_good_price,
                    tv_center_my_order_detail_good_count;

        }

    }

    class CenterExpressMessageAdapter extends BaseAdapter {

        private int ResourseId;
        private LayoutInflater inflater;
        private List<BLComment> datas = new ArrayList<BLComment>();

        public CenterExpressMessageAdapter(int ResourseId) {
            super();

            this.ResourseId = ResourseId;
            this.inflater = LayoutInflater.from(BaseContext);
        }

        public void RefreshData(List<BLComment> dass) {
            this.datas = dass;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {

            return datas.size();
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
            CenterExpressMessageItem express = null;

            if (convertView == null) {
                express = new CenterExpressMessageItem();
                convertView = inflater.inflate(ResourseId, null);
                express.top_line = ViewHolder.get(convertView, R.id.top_line);
                express.bottom_line = ViewHolder.get(convertView,
                        R.id.bottom_line);
                express.tv_center_my_order_express_state = ViewHolder.get(
                        convertView, R.id.tv_center_my_order_express_state);
                express.tv_center_my_order_express_time = ViewHolder.get(
                        convertView, R.id.tv_center_my_order_express_time);
                express.dot_view_center_order = (DotView) convertView.findViewById(R.id.dot_view_center_order);
                convertView.setTag(express);
            } else {
                express = (CenterExpressMessageItem) convertView.getTag();
            }
            LayoutParams params = express.dot_view_center_order.getLayoutParams();
            if (0 == position) {
                express.top_line.setVisibility(View.INVISIBLE);
                express.bottom_line.setVisibility(View.VISIBLE);
            }
            if (datas.size() - 1 == position) {
                express.top_line.setVisibility(View.VISIBLE);
                express.bottom_line.setVisibility(View.INVISIBLE);

            }
            express.dot_view_center_order.setLayoutParams(params);
            StrUtils.SetTxt(express.tv_center_my_order_express_state, datas
                    .get(position).getContext());
            StrUtils.SetTxt(express.tv_center_my_order_express_time,
                    datas.get(position).getTime());

            return convertView;
        }
    }

    /**
     * @author Yihuihua 快递的Holder
     */
    class CenterExpressMessageItem {
        public View top_line, bottom_line;
        public TextView tv_center_my_order_express_state,
                tv_center_my_order_express_time;
        public DotView dot_view_center_order;
    }

    /**
     * 接收事件
     *
     * @param event
     */

    public void OnGetMessage(BMessage event) {
        int messageType = event.getMessageType();
        if (messageType == BMessage.Tage_Apply_Refund) {
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        // 注销事件
        EventBus.getDefault().unregister(this, BMessage.class);
    }

}
