package io.vtown.WeiTangApp.ui.title.shop.odermanger;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import io.vtown.WeiTangApp.bean.bcomment.easy.BAddress;
import io.vtown.WeiTangApp.bean.bcomment.easy.LogisticsData;
import io.vtown.WeiTangApp.bean.bcomment.easy.shoporder.BDShopOrderDetail;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CopyTextView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.account.AOderBeing;
import io.vtown.WeiTangApp.ui.title.mynew.ANew;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-27 上午11:02:15 订单管理-->待发货-->订单修改页面
 */
public class ADaifaOrderModify extends ATitleBase {

    /**
     * 收货人姓名
     */
    private TextView commentview_add_name;
    /**
     * 收货人手机号
     */
    private TextView commentview_add_phone;
    /**
     * 收货人地址
     */
    private TextView commentview_add_address;
    /**
     * 收货人地址信息
     */
    private View daifa_address_message;
    /**
     * 收货人订单信息
     */
    private View daifa_order_message;
    /**
     * 订单号
     */
    private TextView tv_order_id;
    /**
     * 下单时间
     */
    private TextView tv_ordering_time;
    /**
     * 付款时间
     */
    private TextView tv_pay_time;
    /**
     * 商品列表
     */
    private CompleteListView lv_daifa_common_goods;
    /**
     * 商品个数
     */
    private TextView tv_daifa_good_count;
    /**
     * 商品总价
     */
    private TextView tv_daifa_total_price;
    /**
     * 运费
     */
    private TextView tv_daifa_post_price;
    /**
     * 确认订单
     */
    private Button btn_daifa_confirm_order;

    /**
     * 修改地址的请求码
     */
    private static final int REQUEST_CODE_ADDRESS = 111;
    /**
     * 选择物流的请求码
     */
    private static final int REQUEST_CODE_LOGISTICS = 112;

    /**
     * 发货页面
     */
    public static final int Tage_From_Send = 100;
    /**
     * 修改订单页面
     */
    public static final int Tage_From_Modify = 101;

    /**
     * 查看物流页面之查看物流页面
     */
    public static final int Tage_From_Look_Express = 102;

    /**
     * 查看物流页面之补录物流页面
     */
    public static final int Tage_From_Repair_Express = 103;

    /**
     * 查看订单详情页面
     */
    public static final int Tage_From_Look_Detail = 104;

    /**
     * 已关闭--查看订单详情页面
     */
    public static final int Tage_From_Look_Detail_Close = 105;

    public static final String Tag = "ADaifaOrderModify";

    /**
     * 获取到的网络数据
     */
    private BDShopOrderDetail data = null;
    private int type;
    /**
     * 修改订单和发货所在布局
     */
    private LinearLayout ll_modify_and_send_out;
    /**
     * 物流发货
     */
    private TextView tv_order_manage_logistics_send_out;

    /**
     * 地址图标
     */
    private ImageView commentview_add_iv;
    /**
     * 物流相关的布局
     */
    private LinearLayout ll_send_out_logistics_message;
    /**
     * 买家留言
     */
    private EditText et_buyer_message;
    /**
     * 选择物流公司
     */
    private View select_logistics_company;
    /**
     * 选择物流公司title
     */
    private TextView comment_txtarrow_title;
    /**
     * 物流公司名称
     */
    private TextView comment_txtarrow_content;
    /**
     * 快递单号输入
     */
    private EditText et_express_number;
    /**
     * 右箭头显示
     */
    private ImageView iv_right_arrow;
    /**
     * 快递公司对应的信息对象
     */
    private BLComment bl;
    /**
     * 商家ID
     */
    private String seller_id;
    /**
     * 订单号
     */
    private String seller_order_sn;
    /**
     * 物流信息
     */
    private LinearLayout ll_express_message;
    /**
     * 商品总的信息
     */
    private LinearLayout rl_order_goods_message;
    /**
     * 物流进度
     */
    private CompleteListView lv_order_good_express_speed;
    /**
     * 快递的AP
     */
    private ExpressAdapter expressAdapter;
    /**
     * 买家留言布局
     */
    private LinearLayout ll_buyer_message;
    /**
     * 联系买家布局
     */
    private LinearLayout ll_contact_buyer;
    /**
     * 同不同意退款布局
     */
    private LinearLayout ll_agree_and_no_agree;
    /**
     * 不同意退款
     */
    private TextView tv_order_manage_i_not_agree_refund;
    /**
     * 同意退款
     */
    private TextView tv_order_manage_i_agree_refund;

    /**
     * 是否从地址管理里获取地址
     */
    private boolean isSelectAddress = false;
    /**
     * 地址信息对象
     */
    private BLComment bl2;
    /**
     * 获取到数据需要展示的布局
     */
    private LinearLayout daifa_order_modify_outlay;
    /**
     * 获取数据失败情况显示的布局
     */
    private View daifa_order_modify_nodata_lay;

    /**
     * 是否修改了地址
     */
    private boolean isModify = false;

    /**
     * 地址内容
     */
    private Bundle bundle;

    /**
     * 当此标志是获取详情数据时，失败时需要显示失败布局
     */
    private boolean isGetDetail = false;
    /**
     * 物流状态
     */
    private TextView tv_order_good_express_title;
    /**
     * 快递单号
     */
    private CopyTextView tv_order_express_numb;
    /**
     * 物流公司
     */
    private TextView tv_order_express_name;
    /**
     * 使用余额
     */
    private TextView tv_order_manager_used_balance;
    /**
     * 使用卡券
     */
    private TextView tv_order_manager_used_coupons;
    /**
     * 使用卡券和余额
     */
    private LinearLayout ll_order_manager_used_balance_and_coupons;
    /**
     * 使用卡券和余额分割线
     */
    private View line_left;
    private BAddress address_info;


    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_daifa_order_modify);
        Intent intent = getIntent();
        seller_id = intent.getStringExtra("seller_id");
        seller_order_sn = intent.getStringExtra("seller_order_sn");
        type = intent.getIntExtra(Tag, 0);
        IView();
        IData(seller_id, seller_order_sn);
        // 只有当页面是查看物流和已关闭查看订单详情时获取物流动态

    }

    /**
     * 订单详情请求
     *
     * @param seller_id
     * @param seller_order_sn
     */
    private void IData(String seller_id, String seller_order_sn) {
        PromptManager.showtextLoading(BaseContext,
                getResources()
                        .getString(R.string.xlistview_header_hint_loading));
        SetTitleHttpDataLisenter(this);
        isGetDetail = true;
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("seller_id", seller_id);
        map.put("seller_order_sn", seller_order_sn);
        FBGetHttpData(map, Constants.Order_Detail, Method.GET, 0,
                LOAD_INITIALIZE);

    }

    /**
     * 发货请求
     *
     * @param express_number
     * @param bl2
     */
    private void SendOut(String express_number, BLComment bl2) {
        HashMap<String, String> map = new HashMap<String, String>();
        isGetDetail = false;
        map.put("seller_order_sn", seller_order_sn);
        map.put("express_number", express_number);
        map.put("seller_id", seller_id);
        String express_id = "";
        String express_key = "";
        String express_name = "";
        if (bl2 != null) {
            express_id = bl2.getId();
            express_key = bl2.getKey();
            express_name = bl2.getName();
        }
        map.put("express_id", express_id);
        map.put("express_key", express_key);
        map.put("express_name", express_name);

        FBGetHttpData(map, Constants.Send_Out, Method.PUT, 1, LOAD_INITIALIZE);

    }

    /**
     * 查看物流请求
     */
    private void LookExpress() {
        HashMap<String, String> map = new HashMap<String, String>();
        isGetDetail = false;
        map.put("seller_order_sn", seller_order_sn);
        FBGetHttpData(map, Constants.Look_Express_Message, Method.GET, 2,
                LOAD_INITIALIZE);
    }

    /**
     * 退款请求
     */
    private void Refund(int type) {
        String host = "";
        HashMap<String, String> map = new HashMap<String, String>();
        isGetDetail = false;
        map.put("seller_id", seller_id);
        map.put("seller_order_sn", seller_order_sn);
        switch (type) {
            case 0:// 同意退款
                host = Constants.Agree_Refund;
                break;

            case 1:// 不同意退款
                host = Constants.Reject_Refund;
                break;
        }
        FBGetHttpData(map, host, Method.PUT, 3, LOAD_INITIALIZE);
    }

    /**
     * 修改地址
     */
    private void ModifyAddress() {
        HashMap<String, String> map = new HashMap<String, String>();
        isGetDetail = false;
        if (isModify) {
            map.put("username", bundle.getString("name"));
            map.put("mobile", bundle.getString("mobile"));
            map.put("province", bundle.getString("province"));
            map.put("city", bundle.getString("city"));
            map.put("area", bundle.getString("county"));
            map.put("street_address", bundle.getString("address"));
            map.put("address", bundle.getString("address"));
            map.put("postcode", bundle.getString("postcode"));
        } else {
            if (!StrUtils.isEmpty(data.getUsername())
                    && !StrUtils.isEmpty(data.getMobile())
                    && !StrUtils.isEmpty(data.getProvince())
                    && !StrUtils.isEmpty(data.getCity())
                    && !StrUtils.isEmpty(data.getArea())
                    && !StrUtils.isEmpty(data.getAddress())) {
                map.put("username", data.getUsername());
                map.put("mobile", data.getMobile());
                map.put("province", data.getProvince());
                map.put("city", data.getCity());
                map.put("area", data.getArea());
                map.put("street_address", data.getAddress());
                map.put("address", data.getAddress());
                map.put("postcode", "");
            }
        }
        map.put("seller_order_sn", seller_order_sn);
        map.put("seller_id", seller_id);
        FBGetHttpData(map, Constants.Order_Manage_Modify_Address, Method.PUT,
                4, LOAD_INITIALIZE);

    }

    private void IView() {
        daifa_order_modify_outlay = (LinearLayout) findViewById(R.id.daifa_order_modify_outlay);
        daifa_order_modify_nodata_lay = findViewById(R.id.daifa_order_modify_nodata_lay);
        IDataView(daifa_order_modify_outlay, daifa_order_modify_nodata_lay,
                NOVIEW_INITIALIZE);

        daifa_address_message = findViewById(R.id.daifa_address_message);
        commentview_add_name = (TextView) daifa_address_message
                .findViewById(R.id.commentview_add_name);
        commentview_add_phone = (TextView) daifa_address_message
                .findViewById(R.id.commentview_add_phone);
        commentview_add_address = (TextView) daifa_address_message
                .findViewById(R.id.commentview_add_address);
        commentview_add_iv = (ImageView) daifa_address_message
                .findViewById(R.id.commentview_add_iv);

        iv_right_arrow = (ImageView) daifa_address_message
                .findViewById(R.id.iv_right_arrow);

        daifa_order_message = findViewById(R.id.daifa_order_message);

        tv_order_id = (TextView) daifa_order_message
                .findViewById(R.id.tv_order_id);
        // 让订单编号文本有可复制功能
        StrUtils.SetTextViewCopy(tv_order_id);
        tv_ordering_time = (TextView) daifa_order_message
                .findViewById(R.id.tv_ordering_time);
        tv_pay_time = (TextView) daifa_order_message
                .findViewById(R.id.tv_pay_time);

        ll_order_manager_used_balance_and_coupons = (LinearLayout) findViewById(R.id.ll_order_manager_used_balance_and_coupons);
        line_left = findViewById(R.id.line_left);
        tv_order_manager_used_balance = (TextView) findViewById(R.id.tv_order_manager_used_balance);
        tv_order_manager_used_coupons = (TextView) findViewById(R.id.tv_order_manager_used_coupons);

        tv_order_good_express_title = (TextView) findViewById(R.id.tv_order_good_express_title);

        ll_send_out_logistics_message = (LinearLayout) findViewById(R.id.ll_send_out_logistics_message);
        ll_buyer_message = (LinearLayout) findViewById(R.id.ll_buyer_message);
        et_buyer_message = (EditText) findViewById(R.id.et_buyer_message);
        select_logistics_company = findViewById(R.id.select_logistics_company);
        comment_txtarrow_title = (TextView) select_logistics_company
                .findViewById(R.id.comment_txtarrow_title);
        comment_txtarrow_title
                .setText(getString(R.string.select_logistics_company_title));
        comment_txtarrow_content = (TextView) select_logistics_company
                .findViewById(R.id.comment_txtarrow_content);
        et_express_number = (EditText) findViewById(R.id.et_express_number);

        lv_daifa_common_goods = (CompleteListView) findViewById(R.id.lv_daifa_common_goods);
        tv_daifa_good_count = (TextView) findViewById(R.id.tv_daifa_good_count);
        tv_daifa_total_price = (TextView) findViewById(R.id.tv_daifa_total_price);
        tv_daifa_post_price = (TextView) findViewById(R.id.tv_daifa_post_price);
        btn_daifa_confirm_order = (Button) findViewById(R.id.btn_daifa_confirm_order);

        ll_modify_and_send_out = (LinearLayout) findViewById(R.id.ll_modify_and_send_out);

        tv_order_manage_logistics_send_out = (TextView) findViewById(R.id.tv_order_manage_logistics_send_out);

        ll_express_message = (LinearLayout) findViewById(R.id.ll_express_message);
        tv_order_express_numb = (CopyTextView) findViewById(R.id.tv_order_express_numb);
        tv_order_express_name = (TextView) findViewById(R.id.tv_order_express_name);
        rl_order_goods_message = (LinearLayout) findViewById(R.id.rl_order_goods_message);
        lv_order_good_express_speed = (CompleteListView) findViewById(R.id.lv_order_good_express_speed);


        ll_contact_buyer = (LinearLayout) findViewById(R.id.ll_contact_buyer);

        ll_agree_and_no_agree = (LinearLayout) findViewById(R.id.ll_agree_and_no_agree);
        tv_order_manage_i_not_agree_refund = (TextView) findViewById(R.id.tv_order_manage_i_not_agree_refund);
        tv_order_manage_i_agree_refund = (TextView) findViewById(R.id.tv_order_manage_i_agree_refund);

        // 初始化快递列表
        IExpressList();

        // 区分点击来显示不同的布局
        switch (type) {
            case Tage_From_Modify:// 修改订单页面
                ll_send_out_logistics_message.setVisibility(View.GONE);
                ll_modify_and_send_out.setVisibility(View.GONE);
                btn_daifa_confirm_order.setVisibility(View.VISIBLE);
                commentview_add_iv.setVisibility(View.VISIBLE);
                iv_right_arrow.setVisibility(View.VISIBLE);
                ll_express_message.setVisibility(View.GONE);
                lv_order_good_express_speed.setVisibility(View.GONE);

                ll_contact_buyer.setVisibility(View.VISIBLE);
                ll_agree_and_no_agree.setVisibility(View.GONE);
                daifa_address_message.setEnabled(true);
                ll_buyer_message.setVisibility(View.GONE);
                break;

            case Tage_From_Send:// 发货页面
                ll_send_out_logistics_message.setVisibility(View.VISIBLE);
                ll_modify_and_send_out.setVisibility(View.VISIBLE);
                btn_daifa_confirm_order.setVisibility(View.GONE);
                commentview_add_iv.setVisibility(View.GONE);
                iv_right_arrow.setVisibility(View.GONE);
                ll_express_message.setVisibility(View.GONE);
                ll_buyer_message.setVisibility(View.GONE);
                ll_contact_buyer.setVisibility(View.VISIBLE);
                ll_agree_and_no_agree.setVisibility(View.GONE);
                lv_order_good_express_speed.setVisibility(View.GONE);

                // 地址不可点击
                daifa_address_message.setEnabled(false);
                break;

            case Tage_From_Look_Express:// 查看物流页面之查看物流
                ll_send_out_logistics_message.setVisibility(View.GONE);
                ll_modify_and_send_out.setVisibility(View.GONE);
                btn_daifa_confirm_order.setVisibility(View.GONE);
                commentview_add_iv.setVisibility(View.GONE);
                iv_right_arrow.setVisibility(View.GONE);
                ll_buyer_message.setVisibility(View.GONE);
                daifa_address_message.setEnabled(false);
                daifa_order_message.setVisibility(View.GONE);
                ll_express_message.setVisibility(View.GONE);
                rl_order_goods_message.setVisibility(View.GONE);
                daifa_order_message.setVisibility(View.GONE);
                ll_agree_and_no_agree.setVisibility(View.GONE);
                ll_contact_buyer.setVisibility(View.VISIBLE);
                lv_order_good_express_speed.setVisibility(View.VISIBLE);


                break;
            case Tage_From_Repair_Express:// 查看物流页面之补录物流
                ll_send_out_logistics_message.setVisibility(View.VISIBLE);
                daifa_order_message.setVisibility(View.GONE);
                ll_buyer_message.setVisibility(View.GONE);
                ll_modify_and_send_out.setVisibility(View.GONE);
                btn_daifa_confirm_order.setVisibility(View.GONE);
                commentview_add_iv.setVisibility(View.GONE);
                iv_right_arrow.setVisibility(View.GONE);
                ll_express_message.setVisibility(View.GONE);
                rl_order_goods_message.setVisibility(View.GONE);
                ll_agree_and_no_agree.setVisibility(View.GONE);
                lv_order_good_express_speed.setVisibility(View.GONE);

                ll_contact_buyer.setVisibility(View.VISIBLE);
                // 地址不可点击
                daifa_address_message.setEnabled(false);
                break;

            case Tage_From_Look_Detail:// 查看订单详情
                ll_send_out_logistics_message.setVisibility(View.GONE);
                ll_buyer_message.setVisibility(View.GONE);
                ll_modify_and_send_out.setVisibility(View.GONE);
                btn_daifa_confirm_order.setVisibility(View.GONE);
                commentview_add_iv.setVisibility(View.GONE);
                iv_right_arrow.setVisibility(View.GONE);
                ll_express_message.setVisibility(View.GONE);
                lv_order_good_express_speed.setVisibility(View.GONE);

                ll_contact_buyer.setVisibility(View.VISIBLE);
                ll_agree_and_no_agree.setVisibility(View.VISIBLE);
                // 地址不可点击
                daifa_address_message.setEnabled(false);
                break;

            case Tage_From_Look_Detail_Close:// 已关闭之查看订单详情
                ll_contact_buyer.setVisibility(View.GONE);

                lv_order_good_express_speed.setVisibility(View.VISIBLE);

                ll_express_message.setVisibility(View.GONE);
                commentview_add_iv.setVisibility(View.GONE);
                iv_right_arrow.setVisibility(View.GONE);
                ll_modify_and_send_out.setVisibility(View.GONE);
                btn_daifa_confirm_order.setVisibility(View.GONE);
                ll_send_out_logistics_message.setVisibility(View.GONE);
                ll_agree_and_no_agree.setVisibility(View.GONE);
                // 地址不可点击
                daifa_address_message.setEnabled(false);
                break;
        }
        select_logistics_company.setOnClickListener(this);
        tv_order_manage_logistics_send_out.setOnClickListener(this);
        daifa_address_message.setOnClickListener(this);
        btn_daifa_confirm_order.setOnClickListener(this);
        tv_order_manage_i_not_agree_refund.setOnClickListener(this);
        tv_order_manage_i_agree_refund.setOnClickListener(this);
        daifa_order_modify_nodata_lay.setOnClickListener(this);
        ll_contact_buyer.setOnClickListener(this);

    }

    private void IExpressList() {
        expressAdapter = new ExpressAdapter(R.layout.item_express_message);
        lv_order_good_express_speed.setAdapter(expressAdapter);


    }


    @Override
    protected void InitTile() {
        String title = "";
        switch (type) {
            case Tage_From_Send:// 发货
                title = getString(R.string.send_out_good);
                break;

            case Tage_From_Modify:// 修改订单
                title = getString(R.string.modify_order);
                break;

            case Tage_From_Look_Express:// 查看物流
            case Tage_From_Repair_Express:// 查看物流
                title = getString(R.string.look_express);
                break;

            case Tage_From_Look_Detail:// 订单详情
            case Tage_From_Look_Detail_Close:// 已关闭之订单详情
                title = getString(R.string.order_detail);
                break;
        }
        SetTitleTxt(title);
        ImageView right_right_iv = (ImageView) findViewById(R.id.right_right_iv);
        right_right_iv.setVisibility(View.VISIBLE);
        right_right_iv.setImageDrawable(getResources().getDrawable(
                R.drawable.new1));
        right_right_iv.setOnClickListener(this);
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

        switch (Data.getHttpResultTage()) {
            case 0:
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    return;
                }
                data = new BDShopOrderDetail();
                try {
                    data = JSON.parseObject(Data.getHttpResultStr(),
                            BDShopOrderDetail.class);
                } catch (Exception e) {
                    DataError("解析失败", 1);
                }

                IDataView(daifa_order_modify_outlay, daifa_order_modify_nodata_lay,
                        NOVIEW_RIGHT);
                RefreshView(data);
                break;

            case 1:
                PromptManager.ShowMyToast(BaseContext, "发货成功");
                // 发送事件
                EventBus.getDefault().post(
                        new BMessage(BMessage.Tage_Order_Manage_Updata));
                this.finish();
                break;

            case 2:
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    return;
                }
                List<BLComment> express_data = new ArrayList<BLComment>();
                try {
                    express_data = JSON.parseArray(Data.getHttpResultStr(),
                            BLComment.class);
                } catch (Exception e) {
                    DataError("解析失败", 1);
                }
                if (express_data.size() == 0) {
                    DataError(Data.getHttpResultStr(), 1);
                    return;
                }

                //expressAdapter.RefreshData(express_data);

                break;

            case 3:
                // TODO //是否同意退款
                break;

            case 4:
                PromptManager.ShowMyToast(BaseContext, "修改地址成功");
                this.finish();
                break;
        }
    }

    /**
     * 刷新控件
     *
     * @param data2
     */
    private void RefreshView(final BDShopOrderDetail data2) {
        if (data2 != null) {
            StrUtils.SetTxt(commentview_add_name, data2.getUsername());


            StrUtils.SetTxt(commentview_add_phone, data2.getMobile());
            StrUtils.SetTxt(commentview_add_address, data2.getProvince()
                    + data2.getCity() + data2.getArea() + data2.getAddress());

            if (Tage_From_Modify == type) {
                address_info = new BAddress(data2.getUsername(), data2.getMobile(), data2.getProvince(), data2.getCity(), data2.getArea(), data2.getAddress());
            }


            StrUtils.SetTxt(tv_order_id, data2.getSeller_order_sn());
            StrUtils.SetTxt(tv_ordering_time,
                    StrUtils.longtostr(Long.parseLong(data2.getCreate_time())));
            StrUtils.SetTxt(tv_pay_time,
                    StrUtils.longtostr(Long.parseLong(data2.getPay_time())));

            StrUtils.SetColorsTxt(BaseContext, tv_order_manager_used_balance, R.color.app_gray, "使用余额：", String.format("%1$s元", StrUtils.SetTextForMony(data2.getBalance_price())));
            StrUtils.SetColorsTxt(BaseContext, tv_order_manager_used_coupons, R.color.app_gray, "使用卡券：", String.format("%1$s元", StrUtils.SetTextForMony(data2.getCoupons_price())));
            //显示使用余额和卡券，只有金额不为0的时候才显示
            if (0 != Integer.parseInt(data2.getBalance_price()) || 0 != Integer.parseInt(data2.getCoupons_price())) {
                ll_order_manager_used_balance_and_coupons.setVisibility(View.VISIBLE);
                if (0 != Integer.parseInt(data2.getBalance_price())) {
                    tv_order_manager_used_balance.setVisibility(View.VISIBLE);
                } else {
                    tv_order_manager_used_balance.setVisibility(View.GONE);
                    line_left.setVisibility(View.GONE);
                }
                if (0 != Integer.parseInt(data2.getCoupons_price())) {
                    tv_order_manager_used_coupons.setVisibility(View.VISIBLE);
                } else {
                    tv_order_manager_used_coupons.setVisibility(View.GONE);
                    line_left.setVisibility(View.GONE);
                }
            } else {
                ll_order_manager_used_balance_and_coupons.setVisibility(View.GONE);
            }

            String number = String.format("共%1$s件商品", data2.getGoods().size()
                    + "");
            StrUtils.SetTxt(tv_daifa_good_count, number);
            int price = Integer.parseInt(data2.getGoods_price())
                    + Integer.parseInt(data2.getPostage());
//            String total_price = String.format("%1$s元",
//                    StrUtils.SetTextForMony(price + ""));
//            StrUtils.SetTxt(tv_daifa_total_price, total_price);

            StrUtils.SetMoneyFormat(BaseContext,tv_daifa_total_price,price+"",17);
            float postageF = Float.parseFloat(data2.getPostage());
            String postage = String.format("(含运费%1$s元)",
                    StrUtils.SetTextForMony(postageF + ""));
            StrUtils.SetTxt(tv_daifa_post_price, postageF == 0.0f ? "(免邮费)"
                    : postage);

            OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(
                    R.layout.item_oreder_detail_good_list, data2.getGoods());
            lv_daifa_common_goods.setAdapter(orderDetailAdapter);
            // 点击商品Item跳转商品详情页面
            lv_daifa_common_goods
                    .setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            Intent intent = new Intent(BaseContext,
                                    AGoodDetail.class);
                            intent.putExtra("goodid",
                                    data2.getGoods().get(position)
                                            .getGoods_id());
                            PromptManager.SkipActivity(BaseActivity, intent);
                        }
                    });

            if (!StrUtils.isEmpty(data2.getExpress_name())
                    && !StrUtils.isEmpty(data2.getExpress_number())) {
                ll_express_message.setVisibility(View.VISIBLE);
                StrUtils.SetColorsTxt(BaseContext, tv_order_express_numb,
                        R.color.app_gray, "快递单号：", data2.getExpress_number());
                StrUtils.SetColorsTxt(BaseContext, tv_order_express_name,
                        R.color.app_gray, "物流公司：", data2.getExpress_name());
            } else {
                ll_express_message.setVisibility(View.GONE);
            }
            if (Tage_From_Look_Express == type
                    || Tage_From_Look_Detail_Close == type) {
                tv_order_good_express_title.setVisibility(View.VISIBLE);
                List<LogisticsData> express_data = new ArrayList<LogisticsData>();
                try {
                    express_data = JSON.parseArray(data2.getLogisticinfo(),
                            LogisticsData.class);
                } catch (Exception e) {

                }
                // StrUtils.isEmpty(data2.getExpress_key()) &&
                // StrUtils.isEmpty(data2.getExpress_name()) &&
                // StrUtils.isEmpty(data2.getExpress_number())
                if (express_data.size() == 0) {
                    StrUtils.SetColorsTxt(BaseContext,
                            tv_order_good_express_title, R.color.app_gray,
                            "物流状态：", data2.getLogisticinfo());
                } else {
                    StrUtils.SetTxt(tv_order_good_express_title, "物流状态：");

                    expressAdapter.RefreshData(express_data);


                    // View layout = lv_order_good_express_speed.getChildAt(0);
                    // View top_line = layout.findViewById(R.id.top_line);
                    // top_line.setVisibility(View.INVISIBLE);
                }
            }

        }
    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.ShowMyToast(BaseContext, error);
        if (LOAD_INITIALIZE == LoadType && isGetDetail) {// 刚进来获取数据时候异常就不显示数据
            // 数据初异常时不可用
            IDataView(daifa_order_modify_outlay, daifa_order_modify_nodata_lay,
                    NOVIEW_ERROR);
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
            case R.id.daifa_address_message:// 地址

                Intent intent = new Intent(BaseActivity,
                        AModifyDeliveryAddress.class);
                if (address_info != null) {
                    intent.putExtra("address_info", address_info);
                    PromptManager.SkipResultActivity(BaseActivity, intent,
                            REQUEST_CODE_ADDRESS);
                }


                break;

            case R.id.btn_daifa_confirm_order:// 确认订单
                if (CheckNet(BaseContext))
                    return;
                ModifyAddress();
                break;

            case R.id.tv_order_manage_logistics_send_out:// 物流发货
                String logistics = comment_txtarrow_content.getText().toString()
                        .trim();
                if (StrUtils.isEmpty(logistics)) {
                    PromptManager.ShowMyToast(BaseContext, "请选择物流");
                    return;
                }
                String express_number = et_express_number.getText().toString()
                        .trim();
                if (StrUtils.isEmpty(express_number)) {
                    PromptManager.ShowMyToast(BaseContext, "请输入快递单号");
                    return;
                }
                if (CheckNet(BaseContext))
                    return;
                SendOut(express_number, bl);
                break;

            case R.id.select_logistics_company:// 选择物流
                if (CheckNet(BaseContext))
                    return;
                Intent intent2 = new Intent(BaseContext, ASeleteLogistics.class);
                PromptManager.SkipResultActivity(BaseActivity, intent2,
                        REQUEST_CODE_LOGISTICS);
                break;
            case R.id.tv_order_manage_i_not_agree_refund:// 不同意退款
                if (CheckNet(BaseContext))
                    return;
                Refund(1);
                break;

            case R.id.tv_order_manage_i_agree_refund:// 同意退款
                if (CheckNet(BaseContext))
                    return;
                Refund(0);
                break;

            case R.id.daifa_order_modify_nodata_lay:// 重新加载数据
                if (CheckNet(BaseContext))
                    return;
                IData(seller_id, seller_order_sn);
                break;

            case R.id.ll_contact_buyer:// 联系买家
                if (CheckNet(BaseContext))
                    return;
//			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
//					AChat.class));

                BComment mBComment = new BComment(data.getMember_seller_id(),
                        "");
                PromptManager.SkipActivity(BaseActivity,
                        new Intent(BaseActivity, AShopDetail.class)
                                .putExtra(BaseKey_Bean, mBComment));
                break;

            case R.id.right_right_iv:// 标题栏消息按钮
                if (CheckNet(BaseContext))
                    return;
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ANew.class));
                break;
        }

    }

    /**
     * 获取确认订单所需要的参数
     */
    private void getAccount() {
        String AccountStr = "";
        if (data != null) {
            String cid = data.getId();
            for (BLComment bldc : data.getGoods()) {
                AccountStr = AccountStr + cid + ":" + bldc.getGoods_number()
                        + ",";
            }
        }

        if (!StrUtils.isEmpty(AccountStr)) {// 已经选择了
            AccountStr = AccountStr.substring(0, AccountStr.length() - 1);
        }
        PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                AOderBeing.class).putExtra("accountstr", AccountStr));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (REQUEST_CODE_ADDRESS == requestCode && RESULT_OK == resultCode) {

            bundle = data.getBundleExtra("AddressInfo");
            if (bundle != null) {
                isModify = true;

                StrUtils.SetTxt( commentview_add_name,bundle.getString("name"));

                StrUtils.SetTxt(commentview_add_phone,
                        bundle.getString("mobile"));
                StrUtils.SetTxt(
                        commentview_add_address,
                        bundle.getString("province") + bundle.getString("city")
                                + bundle.getString("county")
                                + bundle.getString("address"));

            }
        }

        if (REQUEST_CODE_LOGISTICS == requestCode && RESULT_OK == resultCode) {
            bl = (BLComment) data.getSerializableExtra("logisticsinfo");
            if (bl != null) {
                StrUtils.SetTxt(comment_txtarrow_content, bl.getName());
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
     * @author Yihuihua 订单详情AP
     */
    class OrderDetailAdapter extends BaseAdapter {

        private int ResoureId;
        private LayoutInflater inflater;

        private List<BLComment> datas = new ArrayList<BLComment>();

        public OrderDetailAdapter(int ResoureId, List<BLComment> datas) {
            super();

            this.ResoureId = ResoureId;
            this.inflater = LayoutInflater.from(BaseContext);
            this.datas = datas;
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

            OrderDetailItem orderDetail = null;
            if (arg1 == null) {
                orderDetail = new OrderDetailItem();
                arg1 = inflater.inflate(ResoureId, null);
                orderDetail.iv_order_manage_order_detail_good_icon = ViewHolder
                        .get(arg1, R.id.iv_order_manage_order_detail_good_icon);
                orderDetail.iv_order_manage_order_detail_goods_type = ViewHolder
                        .get(arg1, R.id.iv_order_manage_order_detail_goods_type);
                orderDetail.tv_order_manage_order_detail_good_title = ViewHolder
                        .get(arg1, R.id.tv_order_manage_order_detail_good_title);
                orderDetail.tv_order_manage_good_order_detail_content = ViewHolder
                        .get(arg1,
                                R.id.tv_order_manage_good_order_detail_content);
                orderDetail.tv_order_manage_order_detail_content_value = ViewHolder
                        .get(arg1,
                                R.id.tv_order_manage_order_detail_content_value);
                orderDetail.tv_order_manage_order_detail_good_price = ViewHolder
                        .get(arg1, R.id.tv_order_manage_order_detail_good_price);
                orderDetail.tv_order_manage_order_detail_good_count = ViewHolder
                        .get(arg1, R.id.tv_order_manage_order_detail_good_count);

                ImageLoaderUtil.Load2(datas.get(arg0).getGoods_cover(),
                        orderDetail.iv_order_manage_order_detail_good_icon,
                        R.drawable.error_iv2);

                arg1.setTag(orderDetail);

            } else {
                orderDetail = (OrderDetailItem) arg1.getTag();
            }

            String goods_type = datas.get(arg0).getGoods_type();
            if ("0".equals(goods_type)) {
                StrUtils.SetTxt(
                        orderDetail.tv_order_manage_order_detail_good_title, datas
                                .get(arg0).getGoods_name());
            } else {
                StrUtils.SetTxt(
                        orderDetail.tv_order_manage_order_detail_good_title, datas
                                .get(arg0).getGoods_name());
                StrUtils.setTxtLeftDrawable(BaseContext,orderDetail.tv_order_manage_order_detail_good_title);
            }


            StrUtils.SetTxt(
                    orderDetail.tv_order_manage_order_detail_content_value,
                    datas.get(arg0).getGoods_standard());
            String goods_price = String.format("￥%1$s",
                    StrUtils.SetTextForMony(datas.get(arg0).getGoods_price()));
            StrUtils.SetTxt(
                    orderDetail.tv_order_manage_order_detail_good_price,
                    goods_price);
            String goods_number = String.format("x%1$s", datas.get(arg0)
                    .getGoods_number());
            StrUtils.SetTxt(
                    orderDetail.tv_order_manage_order_detail_good_count,
                    goods_number);

            return arg1;
        }

    }

    /**
     * @author Yihuihua 快递AP
     */
    class ExpressAdapter extends BaseAdapter {

        private int ResourseId;
        private LayoutInflater inflater;
        private List<LogisticsData> datas = new ArrayList<LogisticsData>();

        public ExpressAdapter(int ResourseId) {
            super();

            this.ResourseId = ResourseId;
            this.inflater = LayoutInflater.from(BaseContext);
        }

        public void RefreshData(List<LogisticsData> dass) {
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
            ExpressItem express = null;

            if (convertView == null) {
                express = new ExpressItem();
                convertView = inflater.inflate(ResourseId, null);
                express.top_line = ViewHolder.get(convertView, R.id.top_line);
                express.bottom_line = ViewHolder.get(convertView,
                        R.id.bottom_line);
                express.tv_express_state = ViewHolder.get(convertView,
                        R.id.tv_express_state);
                express.tv_express_time = ViewHolder.get(convertView,
                        R.id.tv_express_time);
                convertView.setTag(express);
            } else {
                express = (ExpressItem) convertView.getTag();
            }

            if (0 == position) {
                express.top_line.setVisibility(View.INVISIBLE);
                express.bottom_line.setVisibility(View.VISIBLE);
            }

             if (datas.size() - 1 == position) {
                 express.top_line.setVisibility(View.VISIBLE);
                 express.bottom_line.setVisibility(View.INVISIBLE);
             }

            StrUtils.SetTxt(express.tv_express_state, datas.get(position)
                    .getContext());
            StrUtils.SetTxt(express.tv_express_time, datas.get(position)
                    .getTime());

            return convertView;
        }

    }

    /**
     * @author Yihuihua 快递的Holder
     */
    class ExpressItem {
        public View top_line, bottom_line;
        public TextView tv_express_state, tv_express_time;
    }

    /**
     * @author Yihuihua 订单详情的Holder
     */
    class OrderDetailItem {
        public TextView tv_order_manage_order_detail_good_title,
                tv_order_manage_good_order_detail_content,
                tv_order_manage_order_detail_content_value,
                tv_order_manage_order_detail_good_price,
                tv_order_manage_order_detail_good_count;
        public ImageView iv_order_manage_order_detail_good_icon,
                iv_order_manage_order_detail_goods_type;
    }

}
