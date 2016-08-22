package io.vtown.WeiTangApp.bean.bcomment.easy.shoporder;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-7-27 上午9:54:38
 *  
 */
public class BLShopOrderManage extends BBase{
	 private String id;//"id": 1547,
	 private String member_id;//"member_id": 721,
	 private String seller_id;//"seller_id": 576,
	 private String send_seller_id;//"send_seller_id": 576,
	 private String is_show;//"is_show": 1,
	 private String is_send;//"is_send": 1,
	 private String is_refund;//"is_refund": 0,
	 private String is_edit;//"is_edit": 0,
	 private String send_from;//"send_from": 1,
	 private String create_time;//"create_time": 1469436680,
	 private String update_time;//"update_time": 1469436695,
	 private String order_status;//"order_status": 70,
	 private String goods_id;//"goods_id": 872,
	 private String goods_attr_id;//"goods_attr_id": 3446,
	 private String sell_price;//"sell_price": 1,
	 private String UUID;//"UUID": "",
	 private String source;//"source": 0,
	 private String channel;//"channel": "PT",
	 private String seller_order_sn;//"seller_order_sn": "S2016072556535657843",
	 private String goods_price;//"goods_price": 1,
	 private String money_paid;//"money_paid": 1,
	 private String express_id;//"express_id": 0,
	 private String express_name;//"express_name": "",
	 private String express_number;//"express_number": "",
	 private String express_status;//"express_status": 0,
	 private String express_key;//"express_key": "",
	 private String postage;//"postage": 1,
	 private String info;//"info": "",
	 private String send_time;//"send_time": 0,
	 private String confirm_time;//"confirm_time": 0,
	 private String cancel_time;//"cancel_time": 0,
	 private String end_time;//"end_time": 0,
	 private String return_reason;//"return_reason": "",
	 private String cancel_reason;//"cancel_reason": "",
	 private String remind_time;//"remind_time": 0,
	 private String apply_time;//"apply_time": 1469437416,
	 private String pay_time;//"pay_time": 1469436695,
	 private List<BLDComment> goods = new ArrayList<BLDComment>();//"goods"
	 private String number;//"number": 1
	public String getId() {
		return id;
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
	public String getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
	public String getSend_seller_id() {
		return send_seller_id;
	}
	public void setSend_seller_id(String send_seller_id) {
		this.send_seller_id = send_seller_id;
	}
	public String getIs_show() {
		return is_show;
	}
	public void setIs_show(String is_show) {
		this.is_show = is_show;
	}
	public String getIs_send() {
		return is_send;
	}
	public void setIs_send(String is_send) {
		this.is_send = is_send;
	}
	public String getIs_refund() {
		return is_refund;
	}
	public void setIs_refund(String is_refund) {
		this.is_refund = is_refund;
	}
	public String getIs_edit() {
		return is_edit;
	}
	public void setIs_edit(String is_edit) {
		this.is_edit = is_edit;
	}
	public String getSend_from() {
		return send_from;
	}
	public void setSend_from(String send_from) {
		this.send_from = send_from;
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
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getGoods_attr_id() {
		return goods_attr_id;
	}
	public void setGoods_attr_id(String goods_attr_id) {
		this.goods_attr_id = goods_attr_id;
	}
	public String getSell_price() {
		return sell_price;
	}
	public void setSell_price(String sell_price) {
		this.sell_price = sell_price;
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
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
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
	public String getExpress_key() {
		return express_key;
	}
	public void setExpress_key(String express_key) {
		this.express_key = express_key;
	}
	public String getPostage() {
		return postage;
	}
	public void setPostage(String postage) {
		this.postage = postage;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
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
	public String getPay_time() {
		return pay_time;
	}
	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}
	public List<BLDComment> getGoods() {
		return goods;
	}
	public void setGoods(List<BLDComment> goods) {
		this.goods = goods;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	 
	 
}
