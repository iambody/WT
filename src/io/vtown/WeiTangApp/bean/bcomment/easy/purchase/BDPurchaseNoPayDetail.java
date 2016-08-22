package io.vtown.WeiTangApp.bean.bcomment.easy.purchase;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-7-28 下午4:55:54
 *  
 */
public class BDPurchaseNoPayDetail extends BBase {
	private String id;// "id": 1891,
	private String order_sn;// "order_sn": "A2016072810052529153",
	private String order_status;// "order_status": 10,
	private String order_total_price;// "order_total_price": 1,
	private String postage_money;// "postage_money": 0,
	private String creat_time;// "creat_time": 1469692685,
	private String member_id;// "member_id": 699,
	private String money_paid;// "money_paid": 1,
	private List<BLDComment> son_order = new ArrayList<BLDComment>();// "son_order":
	private String blance;// "blance": 147239
	private String address;//address=小小店铺购买使用账户余额支付
	private String area;// area=鄂托克前旗
	private String cancel_type;// cancel_type=
	private String channel;// channel=CG
	private String city;//city=鄂尔多斯市
	private String create_time;//create_time=1469776197
	private String end_time;//end_time=0
	private String is_delete;// is_delete=0
	private String mobile;// mobile=17612345678
	private String old_order_id;//old_order_id=0
	private String order_note;//order_note=
	private String pay_sn;// pay_sn=
	private String pay_status;// pay_status=0
	private String pay_time;//pay_time=0
	private String pay_type;// pay_type=0
	private String payment_type;// payment_type=0
	private String postcode;// postcode=
	private String province;// province=内蒙古自治区
	private String refund_status;// refund_status=0
	private String remind_time;// remind_time=0
	private String seller_id;// seller_id=576
	private String trade_type;// trade_type=
	private String update_time;//update_time=0
	private String used_balance;// used_balance=0
	private String used_coupons;//used_coupons=0
	private String used_coupons_money;// used_coupons_money=0
	private String used_coupons_name;// used_coupons_name=
	private String username;// username=余额支付-小小店铺
	private String UUID;// UUID=868754024111604
	
	
	
	
	
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getCancel_type() {
		return cancel_type;
	}
	public void setCancel_type(String cancel_type) {
		this.cancel_type = cancel_type;
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
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getIs_delete() {
		return is_delete;
	}
	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getOld_order_id() {
		return old_order_id;
	}
	public void setOld_order_id(String old_order_id) {
		this.old_order_id = old_order_id;
	}
	public String getOrder_note() {
		return order_note;
	}
	public void setOrder_note(String order_note) {
		this.order_note = order_note;
	}
	public String getPay_sn() {
		return pay_sn;
	}
	public void setPay_sn(String pay_sn) {
		this.pay_sn = pay_sn;
	}
	public String getPay_status() {
		return pay_status;
	}
	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}
	public String getPay_time() {
		return pay_time;
	}
	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}
	public String getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getRefund_status() {
		return refund_status;
	}
	public void setRefund_status(String refund_status) {
		this.refund_status = refund_status;
	}
	public String getRemind_time() {
		return remind_time;
	}
	public void setRemind_time(String remind_time) {
		this.remind_time = remind_time;
	}
	public String getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getUsed_balance() {
		return used_balance;
	}
	public void setUsed_balance(String used_balance) {
		this.used_balance = used_balance;
	}
	public String getUsed_coupons() {
		return used_coupons;
	}
	public void setUsed_coupons(String used_coupons) {
		this.used_coupons = used_coupons;
	}
	public String getUsed_coupons_money() {
		return used_coupons_money;
	}
	public void setUsed_coupons_money(String used_coupons_money) {
		this.used_coupons_money = used_coupons_money;
	}
	public String getUsed_coupons_name() {
		return used_coupons_name;
	}
	public void setUsed_coupons_name(String used_coupons_name) {
		this.used_coupons_name = used_coupons_name;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getMember_id() {
		return member_id;
	}
	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}
	public String getMoney_paid() {
		return money_paid;
	}
	public void setMoney_paid(String money_paid) {
		this.money_paid = money_paid;
	}
	public List<BLDComment> getSon_order() {
		return son_order;
	}
	public void setSon_order(List<BLDComment> son_order) {
		this.son_order = son_order;
	}
	public String getBlance() {
		return blance;
	}
	public void setBlance(String blance) {
		this.blance = blance;
	}
	
}
