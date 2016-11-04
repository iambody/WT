package io.vtown.WeiTangApp.bean.bcomment.easy.centerorder;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-7-26 下午9:03:42
 *  
 */
public class BLCenterOder extends BBase {
	 private String id;//"id": 1340,
	 private String member_id;//"member_id": 699,
	 private String seller_id;//"seller_id": 480,
	 private String order_sn;//"order_sn": "A2016072648564810972",
	 private String order_status;//"order_status": 110,
	 private String seller_order_sn;//"seller_order_sn": "S2016072648565648275",
	 private String goods_price;//"goods_price": 10000,
	 private String postage;//"postage": 10,
	 private String create_time;//"create_time": 1469527552,
	 private String refund;//"refund": 0,
	 private String money_paid;//"money_paid": 10010,
	 private String seller_name;//"seller_name": "雨一直下",
	 private List<BLDComment> goods = new ArrayList<BLDComment>();// "detail":"goods"
	 private String number;//"number": 1,
	 private String blance;//"blance": 126273,
	 private String order_total_price;//"order_total_price": 10010
	 private List<BLDComment> son_order = new ArrayList<BLDComment>();
	 private String postage_money;//"postage_money": "0",
	 private String delaynumber;//delaynumber=0
	 private String remind_time;//remind_time=0
	private int advance_point;// "advance_point": 0,
	private String point_time;//"point_time": 0,

	public int getAdvance_point() {
		return advance_point;
	}

	public void setAdvance_point(int advance_point) {
		this.advance_point = advance_point;
	}

	public String getPoint_time() {
		return point_time;
	}

	public void setPoint_time(String point_time) {
		this.point_time = point_time;
	}

	public String getDelaynumber() {
		return delaynumber;
	}
	public void setDelaynumber(String delaynumber) {
		this.delaynumber = delaynumber;
	}
	public String getRemind_time() {
		return remind_time;
	}
	public void setRemind_time(String remind_time) {
		this.remind_time = remind_time;
	}
	public String getPostage_money() {
		return postage_money;
	}
	public void setPostage_money(String postage_money) {
		this.postage_money = postage_money;
	}
	public List<BLDComment> getSon_order() {
		return son_order;
	}
	public void setSon_order(List<BLDComment> son_order) {
		this.son_order = son_order;
	}
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
	public String getPostage() {
		return postage;
	}
	public void setPostage(String postage) {
		this.postage = postage;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getRefund() {
		return refund;
	}
	public void setRefund(String refund) {
		this.refund = refund;
	}
	public String getMoney_paid() {
		return money_paid;
	}
	public void setMoney_paid(String money_paid) {
		this.money_paid = money_paid;
	}
	public String getSeller_name() {
		return seller_name;
	}
	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
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
	public String getBlance() {
		return blance;
	}
	public void setBlance(String blance) {
		this.blance = blance;
	}
	public String getOrder_total_price() {
		return order_total_price;
	}
	public void setOrder_total_price(String order_total_price) {
		this.order_total_price = order_total_price;
	}
	 
	 
}
