package io.vtown.WeiTangApp.ui.title.shop.purchase;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.purchase.BDShopPurchaseDetail;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.fragment.FShopPurchase;
import io.vtown.WeiTangApp.ui.ANull;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.im.AChat;
import io.vtown.WeiTangApp.ui.title.ABrandDetail;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.account.ACashierDesk;
import io.vtown.WeiTangApp.ui.title.center.myorder.AApplyTuikuan;
import io.vtown.WeiTangApp.ui.title.center.set.AAddressManage;
import io.vtown.WeiTangApp.ui.title.mynew.ANew;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-6-28 上午10:22:52 采购单详情页面
 */
public class APurchaseDetail extends ATitleBase {

	/**
	 * 地址信息
	 */
	private View purchase_address_message;
	/**
	 * 订单信息
	 */
	private View purchase_order_message;
	/**
	 * 卖家名字
	 */
	private TextView tv_purchase_detail_shop_name;
	/**
	 * 联系卖家
	 */
	private LinearLayout ll_purchase_contact_seller;
	/**
	 * 商品列表
	 */
	private CompleteListView lv_purchase_common_goods;
	/**
	 * 商品个数
	 */
	private TextView tv_purchase_good_count;
	/**
	 * 总价
	 */
	private TextView tv_purchase_total_price;
	/**
	 * 运费
	 */
	private TextView tv_purchase_post_price;
	/**
	 * 地址图标
	 */
	private ImageView commentview_add_iv;
	/**
	 * 收货人姓名
	 */
	private TextView commentview_add_name;
	/**
	 * 收货人电话
	 */
	private TextView commentview_add_phone;
	/**
	 * 地址
	 */
	private TextView commentview_add_address;
	/**
	 * 地址右箭头
	 */
	private ImageView iv_right_arrow;
	/**
	 * 订单号
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
	 * 取消订单和去支付布局
	 */
	private LinearLayout ll_purchase_cancel_and_to_pay;
	/**
	 * 取消订单
	 */
	private TextView tv_purchase_cancel_order;
	/**
	 * 去付款
	 */
	private TextView tv_purchase_to_pay;
	/**
	 * member_id
	 */
	private String member_id;
	/**
	 * 订单号
	 */
	private String seller_order_sn;

	/**
	 * 修改地址的请求码
	 */
	private static final int REQUEST_CODE_ADDRESS = 211;
	/**
	 * 取消订单Dialog
	 */
	private CustomDialog dialog;
	/**
	 * 从网络获取到的数据
	 */
	private BDShopPurchaseDetail data;
	/**
	 * 订单状态
	 */
	private int ket_Tage;
	/**
	 * 申请退款和提醒发货
	 */
	private LinearLayout ll_purchase_tuikuan_and_fahuo;
	/**
	 * 申请退款
	 */
	private TextView tv_purchase_apply_tuikuan;
	/**
	 * 提醒发货
	 */
	private TextView tv_purchase_remind_send_out;
	/**
	 * 查看物流信息布局
	 */
	private LinearLayout ll_purchase_look_express_message;
	/**
	 * 查看物流
	 */
	private View look_express_speed;
	/**
	 * 物流信息列表及快递信息布局
	 */
	private LinearLayout ll_purchase_good_express_speed;
	/**
	 * 物流信息列表
	 */
	private CompleteListView lv_purchase_good_express_speed;
	/**
	 * 快递单号
	 */
	private TextView tv_purchase_express_numb;
	/**
	 * 快递名字
	 */
	private TextView tv_purchase_express_name;
	/**
	 * 延期收货与确认收货布局
	 */
	private LinearLayout ll_purchase_delay_and_confirm;
	/**
	 * 延期收货
	 */
	private TextView tv_purchase_delay_shouhuo;
	/**
	 * 确认收货
	 */
	private TextView tv_purchase_confirm_shouhuo;
	/**
	 * 物流信息AP
	 */
	private ExpressMessageAdapter expressMessageAdapter;
	/**
	 * 仲裁
	 */
	private TextView tv_purchase_arbitration;
	/**
	 * 再次购买
	 */
	private TextView tv_purchase_buy_again;
	/**
	 * 查看物流
	 */
	private TextView comment_txtarrow_title;
	/**
	 * 查看物流右箭头
	 */
	private ImageView iv_comment_right_arrow;

	private boolean isRotate = false;
	/**
	 * 获取到数据之后显示的布局
	 */
	private LinearLayout shop_my_purchase_outlay;
	/**
	 * 获取数据失败之后显示的布局
	 */
	private View shop_purchase_nodata_lay;
	/**
	 * 下单时间布局
	 */
	private LinearLayout ll_pay_time;
	/**
	 * 退款申请中
	 */
	private TextView tv_purchase_order_refund_applying;
	/**
	 * 订单已完成
	 */
	private TextView tv_purchase_order_is_over;

	/**
	 * 标志是否是获取订单详情
	 */
	private boolean isGetDetail = false;

	private String balance;
	/**
	 * 已延期收货
	 */
	private TextView tv_purchase_order_refund_is_delay;
	/**
	 * 物流公司和快递单号
	 */
	private LinearLayout ll_purchase_express_message;
	/**
	 * 订单状态Lable
	 */
	private TextView tv_purchase_order_good_express_title;
	/**
	 * 使用余额和卡券
	 */
	private LinearLayout ll_shop_purchase_used_balance_and_coupons;
	/**
	 * 使用余额
	 */
	private TextView tv_shop_purchase_used_balance;
	/**
	 * 使用卡券
	 */
	private TextView tv_shop_purchase_used_coupons;
	/**
	 * 使用余额和卡券分割线
	 */
	private View line_left_shop_purchase;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_shop_my_purchase_detail);
		// 注册事件
		EventBus.getDefault().register(this, "OnGetMessage", BMessage.class);
		SetTitleHttpDataLisenter(this);
		Intent intent = getIntent();
		member_id = intent.getStringExtra("member_id");
		seller_order_sn = intent.getStringExtra("seller_order_sn");
		// order_sn = intent.getStringExtra("order_sn");

		ket_Tage = intent.getIntExtra("Key_TageStr", 0);
		IView();
		IData(member_id, seller_order_sn);
	}

	/**
	 * 初始化控件
	 */
	private void IView() {

		shop_my_purchase_outlay = (LinearLayout) findViewById(R.id.shop_my_purchase_outlay);
		shop_purchase_nodata_lay = findViewById(R.id.shop_purchase_nodata_lay);
		IDataView(shop_my_purchase_outlay, shop_purchase_nodata_lay,
				NOVIEW_INITIALIZE);

		// 地址相关的控件
		purchase_address_message = findViewById(R.id.purchase_address_message);
		commentview_add_iv = (ImageView) purchase_address_message
				.findViewById(R.id.commentview_add_iv);
		commentview_add_name = (TextView) purchase_address_message
				.findViewById(R.id.commentview_add_name);
		commentview_add_phone = (TextView) purchase_address_message
				.findViewById(R.id.commentview_add_phone);
		commentview_add_address = (TextView) purchase_address_message
				.findViewById(R.id.commentview_add_address);
		iv_right_arrow = (ImageView) purchase_address_message
				.findViewById(R.id.iv_right_arrow);
		commentview_add_iv.setVisibility(View.VISIBLE);
		//余额和卡券
		ll_shop_purchase_used_balance_and_coupons = (LinearLayout) findViewById(R.id.ll_shop_purchase_used_balance_and_coupons);
		tv_shop_purchase_used_balance = (TextView) findViewById(R.id.tv_shop_purchase_used_balance);
		tv_shop_purchase_used_coupons = (TextView) findViewById(R.id.tv_shop_purchase_used_coupons);
		line_left_shop_purchase = findViewById(R.id.line_left_shop_purchase);

		// 订单相关控件
		purchase_order_message = findViewById(R.id.purchase_order_message);
		tv_order_id = (TextView) purchase_order_message
				.findViewById(R.id.tv_order_id);
		tv_ordering_time = (TextView) purchase_order_message
				.findViewById(R.id.tv_ordering_time);
		tv_pay_time = (TextView) purchase_order_message
				.findViewById(R.id.tv_pay_time);
		ll_pay_time = (LinearLayout) purchase_order_message
				.findViewById(R.id.ll_pay_time);
		
		// 让订单编号文本有可复制功能
		StrUtils.SetTextViewCopy(tv_order_id);

		tv_purchase_order_refund_applying = (TextView) findViewById(R.id.tv_purchase_order_refund_applying);
		tv_purchase_order_is_over = (TextView) findViewById(R.id.tv_purchase_order_is_over);

		tv_purchase_order_refund_is_delay = (TextView) findViewById(R.id.tv_purchase_order_refund_is_delay);

		tv_purchase_detail_shop_name = (TextView) findViewById(R.id.tv_purchase_detail_shop_name);
		ll_purchase_contact_seller = (LinearLayout) findViewById(R.id.ll_purchase_contact_seller);
		lv_purchase_common_goods = (CompleteListView) findViewById(R.id.lv_purchase_common_goods);
		tv_purchase_good_count = (TextView) findViewById(R.id.tv_purchase_good_count);
		tv_purchase_total_price = (TextView) findViewById(R.id.tv_purchase_total_price);
		tv_purchase_post_price = (TextView) findViewById(R.id.tv_purchase_post_price);

		// 取消订单和去付款
		ll_purchase_cancel_and_to_pay = (LinearLayout) findViewById(R.id.ll_purchase_cancel_and_to_pay);
		tv_purchase_cancel_order = (TextView) findViewById(R.id.tv_purchase_cancel_order);
		tv_purchase_to_pay = (TextView) findViewById(R.id.tv_purchase_to_pay);

		// 申请退款和提醒发货

		ll_purchase_tuikuan_and_fahuo = (LinearLayout) findViewById(R.id.ll_purchase_tuikuan_and_fahuo);
		tv_purchase_apply_tuikuan = (TextView) findViewById(R.id.tv_purchase_apply_tuikuan);
		tv_purchase_remind_send_out = (TextView) findViewById(R.id.tv_purchase_remind_send_out);

		// 查看物流
		// ll_purchase_look_express_message = (LinearLayout)
		// findViewById(R.id.ll_purchase_look_express_message);
		ll_purchase_express_message = (LinearLayout) findViewById(R.id.ll_purchase_express_message);
		tv_purchase_order_good_express_title = (TextView) findViewById(R.id.tv_purchase_order_good_express_title);
		lv_purchase_good_express_speed = (CompleteListView) findViewById(R.id.lv_purchase_good_express_speed);
		tv_purchase_express_numb = (TextView) findViewById(R.id.tv_purchase_express_numb);
		tv_purchase_express_name = (TextView) findViewById(R.id.tv_purchase_express_name);

		// 延期收货与确认收货
		ll_purchase_delay_and_confirm = (LinearLayout) findViewById(R.id.ll_purchase_delay_and_confirm);
		tv_purchase_delay_shouhuo = (TextView) findViewById(R.id.tv_purchase_delay_shouhuo);
		tv_purchase_confirm_shouhuo = (TextView) findViewById(R.id.tv_purchase_confirm_shouhuo);

		// 仲裁
		tv_purchase_arbitration = (TextView) findViewById(R.id.tv_purchase_arbitration);

		// 再次购买
		tv_purchase_buy_again = (TextView) findViewById(R.id.tv_purchase_buy_again);

		switch (ket_Tage) {
		case FShopPurchase.PDaiFu:
			purchase_address_message.setEnabled(false);
			commentview_add_iv.setVisibility(View.GONE);
			iv_right_arrow.setVisibility(View.GONE);
			ll_pay_time.setVisibility(View.GONE);
			ll_purchase_cancel_and_to_pay.setVisibility(View.VISIBLE);
			break;

		case FShopPurchase.PYiFu:
			purchase_address_message.setEnabled(false);
			commentview_add_iv.setVisibility(View.GONE);
			iv_right_arrow.setVisibility(View.GONE);
			ll_purchase_tuikuan_and_fahuo.setVisibility(View.VISIBLE);
			break;

		case FShopPurchase.PDaiShou:
			purchase_address_message.setEnabled(false);
			commentview_add_iv.setVisibility(View.GONE);
			iv_right_arrow.setVisibility(View.GONE);
			// ll_purchase_look_express_message.setVisibility(View.GONE);
			ll_purchase_express_message.setVisibility(View.GONE);
			ll_purchase_delay_and_confirm.setVisibility(View.VISIBLE);
			break;
		case FShopPurchase.PTuiKuan:// 退款中

			purchase_address_message.setEnabled(false);
			commentview_add_iv.setVisibility(View.GONE);
			iv_right_arrow.setVisibility(View.GONE);
			tv_purchase_order_refund_applying.setVisibility(View.VISIBLE);

			break;

		case FShopPurchase.PZhongCai://
			purchase_address_message.setEnabled(false);
			commentview_add_iv.setVisibility(View.GONE);
			iv_right_arrow.setVisibility(View.GONE);
			tv_purchase_arbitration.setVisibility(View.VISIBLE);
			break;

		case FShopPurchase.PAgreeRefund:// 卖家已同意退款
			purchase_address_message.setEnabled(false);
			commentview_add_iv.setVisibility(View.GONE);
			iv_right_arrow.setVisibility(View.GONE);

			break;

		case FShopPurchase.PClose:// 订单已完成
			purchase_address_message.setEnabled(false);
			commentview_add_iv.setVisibility(View.GONE);
			iv_right_arrow.setVisibility(View.GONE);
			// ll_purchase_look_express_message.setVisibility(View.VISIBLE);
			tv_purchase_order_is_over.setVisibility(View.VISIBLE);
			break;

		case FShopPurchase.PIsCancel:// 订单已取消
			purchase_address_message.setEnabled(false);
			commentview_add_iv.setVisibility(View.GONE);
			iv_right_arrow.setVisibility(View.GONE);
			ll_pay_time.setVisibility(View.GONE);
			break;

		default:
			break;
		}

		purchase_address_message.setOnClickListener(this);
		tv_purchase_cancel_order.setOnClickListener(this);
		tv_purchase_to_pay.setOnClickListener(this);
		tv_purchase_apply_tuikuan.setOnClickListener(this);
		tv_purchase_remind_send_out.setOnClickListener(this);
		tv_purchase_delay_shouhuo.setOnClickListener(this);
		tv_purchase_confirm_shouhuo.setOnClickListener(this);
		// look_express_speed.setOnClickListener(this);
		shop_purchase_nodata_lay.setOnClickListener(this);
		ll_purchase_contact_seller.setOnClickListener(this);
	}

	/**
	 * @param member_id
	 * @param seller_order_sn
	 *            初始化页面数据
	 */
	private void IData(String member_id, String seller_order_sn) {
		PromptManager.showtextLoading(BaseContext,
				getResources()
						.getString(R.string.xlistview_header_hint_loading));
		HashMap<String, String> map = new HashMap<String, String>();
		isGetDetail = true;
		map.put("member_id", member_id);
		map.put("seller_order_sn", seller_order_sn);
		FBGetHttpData(map, Constants.Purchase_Detail, Method.GET, 0,
				LOAD_INITIALIZE);

	}

	/**
	 * 取消订单
	 */
	private void CancelOrder() {
		HashMap<String, String> map = new HashMap<String, String>();
		isGetDetail = false;
		map.put("member_id", member_id);
		map.put("seller_order_sn", seller_order_sn);
		map.put("cancel_reason", data.getCancel_reason());
		map.put("cancel_type", "2");
		FBGetHttpData(map, Constants.Purchase_Cancel_Order, Method.PUT, 1,
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
		FBGetHttpData(map, Constants.Purchase_Remind_Send_Out, Method.POST, 2,
				LOAD_INITIALIZE);
	}

	/**
	 * 查看物流请求
	 */
	private void LookExpress() {
		HashMap<String, String> map = new HashMap<String, String>();
		isGetDetail = false;
		map.put("seller_order_sn", seller_order_sn);
		FBGetHttpData(map, Constants.Look_Express_Message, Method.GET, 3,
				LOAD_INITIALIZE);
	}

	/**
	 * 延期收货
	 */
	private void DelayShouHuo() {
		HashMap<String, String> map = new HashMap<String, String>();
		isGetDetail = false;
		map.put("seller_order_sn", seller_order_sn);
		map.put("member_id", member_id);
		FBGetHttpData(map, Constants.Delay_Shou_Huo, Method.PUT, 4,
				LOAD_INITIALIZE);
	}

	/**
	 * 确认收货
	 */
	private void ConfirmShouhuo() {
		HashMap<String, String> map = new HashMap<String, String>();
		isGetDetail = false;
		map.put("seller_order_sn", seller_order_sn);
		map.put("member_id", member_id);
		FBGetHttpData(map, Constants.Confirm_Order, Method.POST, 5,
				LOAD_INITIALIZE);
	}

	/**
	 * 去付款
	 */
	private void GoPay(String member_id, String order_sn) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("member_id", member_id);
		map.put("order_sn", order_sn);
		FBGetHttpData(map, Constants.Center_My_Order_Go_Pay, Method.PUT, 6,
				LOAD_INITIALIZE);
	}

	/**
	 * @param data2
	 *            给控件刷新数据
	 */
	private void RefreshView(final BDShopPurchaseDetail data2) {

		StrUtils.SetTxt(tv_purchase_detail_shop_name, data2.getSeller_name());

		String name = getResources().getString(R.string.consignee_name_order);

		StrUtils.SetTxt(commentview_add_name,
				String.format(name, data2.getUsername()));
		StrUtils.SetTxt(commentview_add_phone, data2.getMobile());
		String address = getResources().getString(
				R.string.consignee_detail_address);
		StrUtils.SetTxt(
				commentview_add_address,
				data2.getProvince() + data2.getCity() + data2.getArea()
						+ data2.getAddress());
		
		StrUtils.SetColorsTxt(BaseContext, tv_shop_purchase_used_balance, R.color.app_gray, "使用余额：", String.format("%1$s元", StrUtils.SetTextForMony(data2.getBalance_price())));
		StrUtils.SetColorsTxt(BaseContext, tv_shop_purchase_used_coupons, R.color.app_gray, "使用卡券：", String.format("%1$s元",StrUtils.SetTextForMony(data2.getCoupons_price())));
		//显示使用余额和卡券，只有金额不为0的时候才显示
		if(0!=Integer.parseInt(data2.getBalance_price())||0 != Integer.parseInt(data2.getCoupons_price())){
			ll_shop_purchase_used_balance_and_coupons.setVisibility(View.VISIBLE);
			if(0!=Integer.parseInt(data2.getBalance_price())){
				tv_shop_purchase_used_balance.setVisibility(View.VISIBLE);
			}else{
				tv_shop_purchase_used_balance.setVisibility(View.GONE);
				line_left_shop_purchase.setVisibility(View.GONE);
			}
			if(0 != Integer.parseInt(data2.getCoupons_price())){
				tv_shop_purchase_used_coupons.setVisibility(View.VISIBLE);
			}else{
				tv_shop_purchase_used_coupons.setVisibility(View.GONE);
				line_left_shop_purchase.setVisibility(View.GONE);
			}
		}else{
			ll_shop_purchase_used_balance_and_coupons.setVisibility(View.GONE);
		}
		
		int Order_status = Integer.parseInt(data2.getOrder_status());
		// 如果是待付款订单和已取消订单显示为大单号，已付款显示小单号
		if (FShopPurchase.PDaiFu == Order_status
				|| FShopPurchase.PIsCancel == Order_status) {
			StrUtils.SetTxt(tv_order_id, data2.getOrder_sn());
		} else {
			StrUtils.SetTxt(tv_order_id, data2.getSeller_order_sn());
		}

		StrUtils.SetTxt(tv_pay_time,
				StrUtils.longtostr(Long.parseLong(data2.getPay_time())));
		String count = getResources().getString(R.string.goods_count);
		StrUtils.SetTxt(tv_purchase_good_count,
				String.format(count, data2.getGoods().size()));

		// float total_price =
		// Float.parseFloat(data2.getGoods_price())+Float.parseFloat(data2.getPostage());

		if (FShopPurchase.PDaiFu == Order_status) {
			StrUtils.SetTxt(tv_purchase_total_price, String.format("%1$s元",
					StrUtils.SetTextForMony(data2.getOrder_total_price())));
			// StrUtils.SetTxt(tv_ordering_time,
			// StrUtils.longtostr(Long.parseLong(data2.getCreate_time())));
			float postage_moneyF = Float.parseFloat(data2.getPostage_money());
			String postage = String.format("(含运费%1$s元)",
					StrUtils.SetTextForMony(postage_moneyF + ""));
			StrUtils.SetTxt(tv_purchase_post_price,
					postage_moneyF == 0.0f ? "(免邮费)" : postage);
		} else {
			StrUtils.SetTxt(
					tv_purchase_total_price,
					String.format("%1$s元",
							StrUtils.SetTextForMony(data2.getGoods_price())));
			float postage_moneyF = Float.parseFloat(data2.getPostage());
			String postage = String.format("(含运费%1$s元)",
					StrUtils.SetTextForMony(postage_moneyF + ""));
			StrUtils.SetTxt(tv_purchase_post_price,
					postage_moneyF == 0.0f ? "(免邮费)" : postage);
		}

		// 如果当前订单是有权限申请退款时，申请退款才显示

		if (FShopPurchase.PYiFu == Order_status) {
			if ("0".equals(data2.getRefund())) {
				tv_purchase_apply_tuikuan.setVisibility(View.VISIBLE);
				if ("0".equals(data2.getRemind_time())) {
					tv_purchase_remind_send_out.setVisibility(View.VISIBLE);
				} else {
					tv_purchase_remind_send_out.setVisibility(View.GONE);
				}
			} else {
				tv_purchase_apply_tuikuan.setVisibility(View.GONE);
				tv_purchase_remind_send_out.setVisibility(View.GONE);
			}
		}

		if (FShopPurchase.PDaiShou == Order_status) {

			// 延迟时间10天，只有10天后才显示延迟收货
			long delaytime = Long.parseLong(data.getCreate_time())
					+ (10 * 24 * 60 * 60);
			// 当前时间
			long nowtime = System.currentTimeMillis() / 1000;
			if (nowtime < delaytime) {
				ll_purchase_delay_and_confirm.setVisibility(View.VISIBLE);
				tv_purchase_delay_shouhuo.setVisibility(View.GONE);
			} else {
				if ("0".equals(data2.getDelaynumber())) {
					ll_purchase_delay_and_confirm.setVisibility(View.VISIBLE);
				} else {
					ll_purchase_delay_and_confirm.setVisibility(View.GONE);
					tv_purchase_order_refund_is_delay
							.setVisibility(View.VISIBLE);
				}
			}

		}
		// 显示快递单号和物流公司
		if (!StrUtils.isEmpty(data2.getExpress_name())
				&& !StrUtils.isEmpty(data2.getExpress_number())) {
			ll_purchase_express_message.setVisibility(View.VISIBLE);
			StrUtils.SetColorsTxt(BaseContext, tv_purchase_express_numb,
					R.color.app_gray, "快递单号：", data2.getExpress_number());
			StrUtils.SetColorsTxt(BaseContext, tv_purchase_express_name,
					R.color.app_gray, "物流公司：", data2.getExpress_name());
		} else {
			ll_purchase_express_message.setVisibility(View.GONE);
		}

		List<BLComment> express_data = new ArrayList<BLComment>();
		try {
			express_data = JSON.parseArray(data2.getLogisticinfo(),
					BLComment.class);
		} catch (Exception e) {

		}
		if (FShopPurchase.PDaiShou == Order_status
				|| FShopPurchase.PClose == Order_status) {
			tv_purchase_order_good_express_title.setVisibility(View.VISIBLE);

			if (express_data.size() == 0) {
				StrUtils.SetColorsTxt(BaseContext,
						tv_purchase_order_good_express_title, R.color.app_gray,
						"物流状态：", data2.getLogisticinfo());
			} else {
				StrUtils.SetTxt(tv_purchase_order_good_express_title, "物流状态：");
				lv_purchase_good_express_speed.setVisibility(View.VISIBLE);
				expressMessageAdapter = new ExpressMessageAdapter(
						R.layout.item_purchase_express_message);
				lv_purchase_good_express_speed
						.setAdapter(expressMessageAdapter);
				expressMessageAdapter.RefreshData(express_data);
			}
		}
		// //根据物流状态是否需要显示物流进度。express_status：1已发货 2未收货 3已收货',
		// if("0".equals(data2.getExpress_status())){
		// ll_purchase_look_express_message.setVisibility(View.GONE);
		// }else{
		// ll_purchase_look_express_message.setVisibility(View.VISIBLE);
		// }

		StrUtils.SetTxt(tv_ordering_time,
				StrUtils.longtostr(Long.parseLong(data2.getCreate_time())));

		PurchaseAdater purchaseAdater = new PurchaseAdater(
				R.layout.item_purchese_detail_goods_list, data2.getGoods());
		lv_purchase_common_goods.setAdapter(purchaseAdater);
		// 点击商品Item跳转商品详情页面
		lv_purchase_common_goods
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(BaseContext,
								AGoodDetail.class);
						intent.putExtra("goodid", data2.getGoods()
								.get(position).getGoods_id());
						PromptManager.SkipActivity(BaseActivity, intent);
					}
				});

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
			if (CheckNet(BaseContext))
				return;
			LookExpress();
			rotate = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		} else {
			rotate = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			ll_purchase_good_express_speed.setVisibility(View.GONE);
		}

		rotate.setFillAfter(true);
		v.setAnimation(rotate);
	}

	@Override
	protected void InitTile() {
		String title = "";
		if (FShopPurchase.PDaiFu == ket_Tage) {
			title = "确认订单";
		} else {
			title = "订单详情";
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
		case 0:// 详情
			if (StrUtils.isEmpty(Data.getHttpResultStr())) {
				return;
			}
			data = new BDShopPurchaseDetail();
			try {
				data = JSON.parseObject(Data.getHttpResultStr(),
						BDShopPurchaseDetail.class);
			} catch (Exception e) {
				DataError("解析失败", 1);
			}
			IDataView(shop_my_purchase_outlay, shop_purchase_nodata_lay,
					NOVIEW_RIGHT);
			RefreshView(data);
			break;

		case 1:// 取消订单
			PromptManager.ShowMyToast(BaseContext, "订单取消成功！！！");
			EventBus.getDefault().post(new BMessage(BMessage.Tage_My_Purchase));
			this.finish();
			break;

		case 2:// 提醒发货
			PromptManager.ShowMyToast(BaseContext, "提醒发货成功");
			EventBus.getDefault().post(new BMessage(BMessage.Tage_My_Purchase));
			this.finish();
			break;

		// case 3:// 查看物流信息
		// ll_purchase_good_express_speed.setVisibility(View.VISIBLE);
		// List<BLComment> express_datas = new ArrayList<BLComment>();
		// try {
		// express_datas = JSON.parseArray(Data.getHttpResultStr(),
		// BLComment.class);
		// } catch (Exception e) {
		// DataError("解析失败", 1);
		// }
		// expressMessageAdapter.RefreshData(express_datas);
		// break;

		case 4:// 延期收货
			PromptManager.ShowMyToast(BaseContext, "收货延期成功");
			EventBus.getDefault().post(new BMessage(BMessage.Tage_My_Purchase));
			this.finish();
			break;

		case 5:// 确认收货
			PromptManager.ShowMyToast(BaseContext, "确认收货成功");
			EventBus.getDefault().post(new BMessage(BMessage.Tage_My_Purchase));
			this.finish();
			break;

		case 6:// 去付款
			BDComment data = new BDComment();
			try {
				data = JSON.parseObject(Data.getHttpResultStr(),
						BDComment.class);
			} catch (Exception e) {

			}
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					ACashierDesk.class).putExtra("addOrderInfo", data));
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
			IDataView(shop_my_purchase_outlay, shop_purchase_nodata_lay,
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
		case R.id.purchase_address_message:// 地址
			Intent intent = new Intent(BaseActivity, AAddressManage.class);
			intent.putExtra("NeedFinish", true);
			PromptManager.SkipResultActivity(BaseActivity, intent,
					REQUEST_CODE_ADDRESS);
			break;

		case R.id.tv_purchase_cancel_order:// 取消订单
			ShowCustomDialog("确认取消订单吗？", "取消", "确认", new IDialogResult() {
				@Override
				public void RightResult() {
					if (CheckNet(BaseContext))
						return;
					CancelOrder();

				}

				@Override
				public void LeftResult() {
				}
			});
			break;

		case R.id.tv_purchase_to_pay:// 去付款
			ShowCustomDialog("确认去付款？", "取消", "确认", new IDialogResult() {
				@Override
				public void RightResult() {
					if (CheckNet(BaseContext))
						return;
					// GoPay(member_id, order_sn);
				}

				@Override
				public void LeftResult() {
				}
			});

			break;

		case R.id.tv_purchase_apply_tuikuan:// 申请退款
			Intent intent2 = new Intent(BaseContext, AApplyTuikuan.class);
			intent2.putExtra("seller_order_sn", seller_order_sn);
			intent2.putExtra("FromTag", AApplyTuikuan.Tag_From_Purchase);
			PromptManager.SkipActivity(BaseActivity, intent2);
			break;

		case R.id.tv_purchase_remind_send_out:// 提醒发货
			if (CheckNet(BaseContext))
				return;
			RemindSendOut();
			break;

		case R.id.tv_purchase_delay_shouhuo:// 延期收货

			ShowCustomDialog("每个商品只允许延期一次，延期为3天，确认延期收货吗？", "取消", "确认",
					new IDialogResult() {
						@Override
						public void RightResult() {
							if (CheckNet(BaseContext))
								return;
							DelayShouHuo();
						}

						@Override
						public void LeftResult() {
						}
					});
			break;

		case R.id.tv_purchase_confirm_shouhuo:// 确认收货
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

		// case R.id.look_express_speed:// 查看快递
		// isRotate = !isRotate;
		// SetAnimRotate(iv_comment_right_arrow, isRotate);
		//
		// break;

		case R.id.shop_purchase_nodata_lay:// 重新获取数据
			if (CheckNet(BaseContext))
				return;
			IData(member_id, seller_order_sn);
			break;

		case R.id.ll_purchase_contact_seller:// 联系卖家
			if (CheckNet(BaseContext))
				return;
			//PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
			//		AChat.class));
			BComment mBComment = new BComment(data.getSeller_id(),
					data.getSeller_name());
			PromptManager.SkipActivity(BaseActivity,
					new Intent(BaseActivity, ANull.class)
							.putExtra(BaseKey_Bean, mBComment));
			break;

		case R.id.right_right_iv:// 消息按钮
			if (CheckNet(BaseContext))
				return;
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					ANew.class));
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (REQUEST_CODE_ADDRESS == requestCode && RESULT_OK == resultCode) {
			BLComment bl = (BLComment) data.getSerializableExtra("AddressInfo");
			if (bl != null) {
				StrUtils.SetTxt(
						commentview_add_name,
						String.format(getString(R.string.tv_commentview_name)
								+ "%1$s", bl.getName()));
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
	class PurchaseAdater extends BaseAdapter {

		/**
		 * 填充器
		 */
		private LayoutInflater inflater;
		/**
		 * 资源id
		 */
		private int ResourceId;

		private List<BLComment> data = new ArrayList<BLComment>();

		public PurchaseAdater(int ResouceId, List<BLComment> data) {
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
				item.iv_purchese_detail_good_icon = (ImageView) arg1
						.findViewById(R.id.iv_purchese_detail_good_icon);
				item.iv_purchese_detail_goods_type = (ImageView) arg1
						.findViewById(R.id.iv_purchese_detail_goods_type);
				item.tv_purchese_detail_good_title = (TextView) arg1
						.findViewById(R.id.tv_purchese_detail_good_title);
				item.tv_purchese_good_content = (TextView) arg1
						.findViewById(R.id.tv_purchese_good_content);
				item.tv_purchese_content_value = (TextView) arg1
						.findViewById(R.id.tv_purchese_content_value);
				item.tv_purchese_detail_good_price = (TextView) arg1
						.findViewById(R.id.tv_purchese_detail_good_price);
				item.tv_purchese_detail_good_count = (TextView) arg1
						.findViewById(R.id.tv_purchese_detail_good_count);

				ImageLoaderUtil
						.Load2(data.get(arg0).getGoods_cover(),
								item.iv_purchese_detail_good_icon,
								R.drawable.error_iv2);

				arg1.setTag(item);
			} else {
				item = (PurcheseItem) arg1.getTag();
			}

			String goods_type = data.get(arg0).getGoods_type();

			if ("0".equals(goods_type)) {
				item.iv_purchese_detail_goods_type.setVisibility(View.GONE);
			} else {
				item.iv_purchese_detail_goods_type.setVisibility(View.VISIBLE);
			}

			StrUtils.SetTxt(item.tv_purchese_detail_good_title, data.get(arg0)
					.getGoods_name());
			StrUtils.SetTxt(item.tv_purchese_content_value, data.get(arg0)
					.getGoods_standard());
			String goods_price = String.format("￥%1$s",
					StrUtils.SetTextForMony(data.get(arg0).getGoods_price()));
			StrUtils.SetTxt(item.tv_purchese_detail_good_price, goods_price);
			String goods_number = String.format("x%1$s", data.get(arg0)
					.getGoods_number());
			StrUtils.SetTxt(item.tv_purchese_detail_good_count, goods_number);

			return arg1;
		}

		class PurcheseItem {
			ImageView iv_purchese_detail_good_icon,
					iv_purchese_detail_goods_type;
			TextView tv_purchese_detail_good_title, tv_purchese_good_content,
					tv_purchese_content_value, tv_purchese_detail_good_price,
					tv_purchese_detail_good_count;

		}

	}

	class ExpressMessageAdapter extends BaseAdapter {
		private int ResourseId;
		private LayoutInflater inflater;
		private List<BLComment> datas = new ArrayList<BLComment>();

		public ExpressMessageAdapter(int ResourseId) {
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
			ExpressMessageItem express = null;

			if (convertView == null) {
				express = new ExpressMessageItem();
				convertView = inflater.inflate(ResourseId, null);
				express.top_line = ViewHolder.get(convertView, R.id.top_line);
				express.bottom_line = ViewHolder.get(convertView,
						R.id.bottom_line);
				express.tv_purchase_express_state = ViewHolder.get(convertView,
						R.id.tv_purchase_express_state);
				express.tv_purchase_express_time = ViewHolder.get(convertView,
						R.id.tv_purchase_express_time);
				convertView.setTag(express);
			} else {
				express = (ExpressMessageItem) convertView.getTag();
			}

			if (0 == position) {
				express.top_line.setVisibility(View.INVISIBLE);
			}

			StrUtils.SetTxt(express.tv_purchase_express_state,
					datas.get(position).getContext());
			StrUtils.SetTxt(express.tv_purchase_express_time,
					datas.get(position).getTime());

			return convertView;
		}
	}

	/**
	 * @author Yihuihua 快递的Holder
	 */
	class ExpressMessageItem {
		public View top_line, bottom_line;
		public TextView tv_purchase_express_state, tv_purchase_express_time;
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
