package io.vtown.WeiTangApp.bean.bcomment.easy.purchase;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-7-27 下午1:49:42
 *  
 */
public class BDShopPurchaseDetail extends BBase {
	private String address;//address=小小店铺购买使用账户余额支付
	private String apply_time;//apply_time=0
	private String area;//area=鄂托克前旗
	private String cancel_reason;//cancel_reason=
	private String cancel_time;//cancel_time=0
	private String channel;//channel=CG
	private String city;//city=鄂尔多斯市
	private String confirm_time;//confirm_time=0
	private String coupons_id;//coupons_id=0
	private String coupons_price;//coupons_price=0
	private String create_time;// create_time=1469595920
	private String delaynumber;// delaynumber=0
	private String delivery_type;// delivery_type=0
	private String end_time;// end_time=0
	private String express_id;// express_id=0
	private String express_key;// express_key=
	private String express_name;// express_name=
	private String express_number;// express_number=
	private String express_status;// express_status=0
	private String extend_confirm;// extend_confirm=1469682330
	private String goods_price;// goods_price=19
	private String id;// id=1358
	private String info;// info=
	private String logisticinfo;// logisticinfo=
	private String member_id;// member_id=699
	private String member_seller_id;// member_seller_id=576
	private String mobile;// mobile=17612345678
	private String money_paid;// money_paid=0
	private String old_order_id;// old_order_id=0
	private String order_sn;// order_sn=A2016072749509710317
	private String order_status;// order_status=20
	private String pay_time;// pay_time=1469595929
	private String postage;// postage=0
	private String province;// province=内蒙古自治区
	private String refund;// refund=0
	private String remind_time;// remind_time=0
	private String return_reason;// return_reason=
	private String return_reason_id;// return_reason_id=0
	private String seller_id;// seller_id=623
	private String seller_name;// seller_name=lyn的测试品牌店铺
	private String seller_order_sn;// seller_order_sn=S2016072749515355969
	private String send_time;// send_time=0
	private String source;// source=0
	private String update_time;// update_time=1469595920
	private String username;// username=余额支付-小小店铺
	private String UUID;// UUID=1234567890
	private List<BLComment> goods = new ArrayList<BLComment>();// "detail":"goods""goods":
	 private String creat_time;//"creat_time": 1469702504,
	 private String postage_money;
	 private String order_total_price;//"order_total_price": 2300,
	private String is_delete;// "is_delete": 0,
	private String pay_sn;//"pay_sn": "",
	private String used_balance;// "used_balance": 0,
	private String pay_status;// "pay_status": 0,
	private String cancel_type;// "cancel_type": "",
	private String pay_type;//  "pay_type": 0,
	private String refund_status;//  "refund_status": 0,
	private String order_note;//  "order_note": "",
	private String used_coupons_name;// "used_coupons_name": "",
	private String number;// "number": 2,
	private String postcode;//  "postcode": "",
	private String trade_type;//  "trade_type": "",
	private String used_coupons;//  "used_coupons": 0,
	private String payment_type;// "payment_type": 0,
	private String balance_price;//"balance_price": 0,
	
	 
	public String getBalance_price() {
		return balance_price;
	}
	public void setBalance_price(String balance_price) {
		this.balance_price = balance_price;
	}
	public String getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}
	public String getPay_sn() {
		return pay_sn;
	}
	public void setPay_sn(String pay_sn) {
		this.pay_sn = pay_sn;
	}
	public String getUsed_balance() {
		return used_balance;
	}
	public void setUsed_balance(String used_balance) {
		this.used_balance = used_balance;
	}
	public String getPay_status() {
		return pay_status;
	}
	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}
	public String getCancel_type() {
		return cancel_type;
	}
	public void setCancel_type(String cancel_type) {
		this.cancel_type = cancel_type;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public String getRefund_status() {
		return refund_status;
	}
	public void setRefund_status(String refund_status) {
		this.refund_status = refund_status;
	}
	public String getOrder_note() {
		return order_note;
	}
	public void setOrder_note(String order_note) {
		this.order_note = order_note;
	}
	public String getUsed_coupons_name() {
		return used_coupons_name;
	}
	public void setUsed_coupons_name(String used_coupons_name) {
		this.used_coupons_name = used_coupons_name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getUsed_coupons() {
		return used_coupons;
	}
	public void setUsed_coupons(String used_coupons) {
		this.used_coupons = used_coupons;
	}
	public String getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}
	public String getOrder_total_price() {
		return order_total_price;
	}
	public void setOrder_total_price(String order_total_price) {
		this.order_total_price = order_total_price;
	}
	public String getPostage_money() {
		return postage_money;
	}
	public void setPostage_money(String postage_money) {
		this.postage_money = postage_money;
	}
	public String getCreat_time() {
		return creat_time;
	}
	public void setCreat_time(String creat_time) {
		this.creat_time = creat_time;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getApply_time() {
		return apply_time;
	}
	public void setApply_time(String apply_time) {
		this.apply_time = apply_time;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getCancel_reason() {
		return cancel_reason;
	}
	public void setCancel_reason(String cancel_reason) {
		this.cancel_reason = cancel_reason;
	}
	public String getCancel_time() {
		return cancel_time;
	}
	public void setCancel_time(String cancel_time) {
		this.cancel_time = cancel_time;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getConfirm_time() {
		return confirm_time;
	}
	public void setConfirm_time(String confirm_time) {
		this.confirm_time = confirm_time;
	}
	public String getCoupons_id() {
		return coupons_id;
	}
	public void setCoupons_id(String coupons_id) {
		this.coupons_id = coupons_id;
	}
	public String getCoupons_price() {
		return coupons_price;
	}
	public void setCoupons_price(String coupons_price) {
		this.coupons_price = coupons_price;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getDelaynumber() {
		return delaynumber;
	}
	public void setDelaynumber(String delaynumber) {
		this.delaynumber = delaynumber;
	}
	public String getDelivery_type() {
		return delivery_type;
	}
	public void setDelivery_type(String delivery_type) {
		this.delivery_type = delivery_type;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getExpress_id() {
		return express_id;
	}
	public void setExpress_id(String express_id) {
		this.express_id = express_id;
	}
	public String getExpress_key() {
		return express_key;
	}
	public void setExpress_key(String express_key) {
		this.express_key = express_key;
	}
	public String getExpress_name() {
		return express_name;
	}
	public void setExpress_name(String express_name) {
		this.express_name = express_name;
	}
	public String getExpress_number() {
		return express_number;
	}
	public void setExpress_number(String express_number) {
		this.express_number = express_number;
	}
	public String getExpress_status() {
		return express_status;
	}
	public void setExpress_status(String express_status) {
		this.express_status = express_status;
	}
	public String getExtend_confirm() {
		return extend_confirm;
	}
	public void setExtend_confirm(String extend_confirm) {
		this.extend_confirm = extend_confirm;
	}
	public String getGoods_price() {
		return goods_price;
	}
	public void setGoods_price(String goods_price) {
		this.goods_price = goods_price;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getLogisticinfo() {
		return logisticinfo;
	}
	public void setLogisticinfo(String logisticinfo) {
		this.logisticinfo = logisticinfo;
	}
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getMember_seller_id() {
		return member_seller_id;
	}
	public void setMember_seller_id(String member_seller_id) {
		this.member_seller_id = member_seller_id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMoney_paid() {
		return money_paid;
	}
	public void setMoney_paid(String money_paid) {
		this.money_paid = money_paid;
	}
	public String getOld_order_id() {
		return old_order_id;
	}
	public void setOld_order_id(String old_order_id) {
		this.old_order_id = old_order_id;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getPay_time() {
		return pay_time;
	}
	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}
	public String getPostage() {
		return postage;
	}
	public void setPostage(String postage) {
		this.postage = postage;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getRefund() {
		return refund;
	}
	public void setRefund(String refund) {
		this.refund = refund;
	}
	public String getRemind_time() {
		return remind_time;
	}
	public void setRemind_time(String remind_time) {
		this.remind_time = remind_time;
	}
	public String getReturn_reason() {
		return return_reason;
	}
	public void setReturn_reason(String return_reason) {
		this.return_reason = return_reason;
	}
	public String getReturn_reason_id() {
		return return_reason_id;
	}
	public void setReturn_reason_id(String return_reason_id) {
		this.return_reason_id = return_reason_id;
	}
	public String getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
	public String getSeller_name() {
		return seller_name;
	}
	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}
	public String getSeller_order_sn() {
		return seller_order_sn;
	}
	public void setSeller_order_sn(String seller_order_sn) {
		this.seller_order_sn = seller_order_sn;
	}
	public String getSend_time() {
		return send_time;
	}
	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public List<BLComment> getGoods() {
		return goods;
	}
	public void setGoods(List<BLComment> goods) {
		this.goods = goods;
	}
	
	
}
