package io.vtown.WeiTangApp.bean.bcomment.easy.centerorder;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-7-27 上午11:35:30
 *  
 */
public class BDCenterOrderDetail extends BBase {
	private String id;//"id": 1340,
	private String member_id;//"member_id": 699,
	private String member_seller_id;//"member_seller_id": 576,
	private String seller_id;//"seller_id": 480,
	private String seller_name;//"seller_name": "雨一直下",
	private String order_sn;//"order_sn": "A2016072648564810972",
	private String order_status;//"order_status": 110,
	private String seller_order_sn;//"seller_order_sn": "S2016072648565648275",
	private String goods_price;//"goods_price": 10000,
	private String money_paid;//"money_paid": 10010,
	private String express_id;//"express_id": 0,
	private String express_name;//"express_name": "",
	private String express_number;//"express_number": "",
	private String express_status;//"express_status": 0,
	private String postage;//"postage": 10,
	private String coupons_id;//"coupons_id": 0,
	private String coupons_price;//"coupons_price": 0,
	private String info;//"info": "",
	private String create_time;//"create_time": 1469527552,
	private String update_time;//"update_time": 1469527552,
	private String send_time;//"send_time": 0,
	private String confirm_time;//"confirm_time": 0,
	private String cancel_time;//"cancel_time": 1469527656,
	private String end_time;//"end_time": 0,
	private String pay_time;//"pay_time": 0,
	private String extend_confirm;//"extend_confirm": 0,
	private String return_reason;//"return_reason": "",
	private String cancel_reason;//"cancel_reason": "",
	private String express_key;//"express_key": "",
	private String remind_time;//"remind_time": 0,
	private String apply_time;//"apply_time": 0,
	private String delivery_type;//"delivery_type": 0,
	private String delaynumber;//"delaynumber": 0,
	private String UUID;//"UUID": "1234567890",
	private String source;//"source": 0,
	private String refund;//"refund": 0,
	private String channel;//"channel": "PT",
	private String return_reason_id;//"return_reason_id": 0,
	private String old_order_id;//"old_order_id": 0,
	private String province;//"province": "海南省",
	private String city;//"city": "三亚市",
	private String area;//"area": "白沙黎族自治县",
	private String address;//"address": "点点滴滴",
	private String username;//"username": "点点滴滴对对对发查查",
	private String mobile;//"mobile": "13611383242",
	private String order_total_price;//"order_total_price": 10010,
	private List<BLComment> goods = new ArrayList<BLComment>();//"goods":
    private String logisticinfo;//	 "logisticinfo": "",
	private String number;//  "number": 1
	private String avatar="";
	private String balance_price;//"balance_price": 0,
	
	public String getBalance_price() {
		return balance_price;
	}

	public void setBalance_price(String balance_price) {
		this.balance_price = balance_price;
	}

	public String getId() {
		return id;
	}
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public void setId(String id) {
		this.id = id;
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
	public String getSeller_order_sn() {
		return seller_order_sn;
	}
	public void setSeller_order_sn(String seller_order_sn) {
		this.seller_order_sn = seller_order_sn;
	}
	public String getGoods_price() {
		return goods_price;
	}
	public void setGoods_price(String goods_price) {
		this.goods_price = goods_price;
	}
	public String getMoney_paid() {
		return money_paid;
	}
	public void setMoney_paid(String money_paid) {
		this.money_paid = money_paid;
	}
	public String getExpress_id() {
		return express_id;
	}
	public void setExpress_id(String express_id) {
		this.express_id = express_id;
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
	public String getPostage() {
		return postage;
	}
	public void setPostage(String postage) {
		this.postage = postage;
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
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getSend_time() {
		return send_time;
	}
	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}
	public String getConfirm_time() {
		return confirm_time;
	}
	public void setConfirm_time(String confirm_time) {
		this.confirm_time = confirm_time;
	}
	public String getCancel_time() {
		return cancel_time;
	}
	public void setCancel_time(String cancel_time) {
		this.cancel_time = cancel_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getPay_time() {
		return pay_time;
	}
	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}
	public String getExtend_confirm() {
		return extend_confirm;
	}
	public void setExtend_confirm(String extend_confirm) {
		this.extend_confirm = extend_confirm;
	}
	public String getReturn_reason() {
		return return_reason;
	}
	public void setReturn_reason(String return_reason) {
		this.return_reason = return_reason;
	}
	public String getCancel_reason() {
		return cancel_reason;
	}
	public void setCancel_reason(String cancel_reason) {
		this.cancel_reason = cancel_reason;
	}
	public String getExpress_key() {
		return express_key;
	}
	public void setExpress_key(String express_key) {
		this.express_key = express_key;
	}
	public String getRemind_time() {
		return remind_time;
	}
	public void setRemind_time(String remind_time) {
		this.remind_time = remind_time;
	}
	public String getApply_time() {
		return apply_time;
	}
	public void setApply_time(String apply_time) {
		this.apply_time = apply_time;
	}
	public String getDelivery_type() {
		return delivery_type;
	}
	public void setDelivery_type(String delivery_type) {
		this.delivery_type = delivery_type;
	}
	public String getDelaynumber() {
		return delaynumber;
	}
	public void setDelaynumber(String delaynumber) {
		this.delaynumber = delaynumber;
	}
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getRefund() {
		return refund;
	}
	public void setRefund(String refund) {
		this.refund = refund;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getReturn_reason_id() {
		return return_reason_id;
	}
	public void setReturn_reason_id(String return_reason_id) {
		this.return_reason_id = return_reason_id;
	}
	public String getOld_order_id() {
		return old_order_id;
	}
	public void setOld_order_id(String old_order_id) {
		this.old_order_id = old_order_id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getOrder_total_price() {
		return order_total_price;
	}
	public void setOrder_total_price(String order_total_price) {
		this.order_total_price = order_total_price;
	}
	public List<BLComment> getGoods() {
		return goods;
	}
	public void setGoods(List<BLComment> goods) {
		this.goods = goods;
	}
	public String getLogisticinfo() {
		return logisticinfo;
	}
	public void setLogisticinfo(String logisticinfo) {
		this.logisticinfo = logisticinfo;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	
}
