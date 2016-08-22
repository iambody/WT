package io.vtown.WeiTangApp.bean.bcomment.easy.centerorder;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-7-27 上午11:47:50
 *  
 */
public class BDCenterOrderNoPayDetail extends BBase {
	private String username;//"username": "大兔兔零购",
	private String mobile;//"mobile": "18310998310",
	private String province;//"province": "海南省",
	private String city;//"city": "海口市",
	private String area;//"area": "滨海区",
	private String address;//"address": "门牌号",
	private String order_sn;//"order_sn": "A2016070697971015236",
	private String create_time;//"creat_time": "1467770522",
	private String postage_money;//"postage_money": "0",
	private String source;//"source": "20",
	private String order_total_price;//"order_total_price": "156",
	private String money_paid;//"money_paid": "156",
	private List<BLDComment> son_order = new ArrayList<BLDComment>();//"son_order":
	private String used_coupons_money;//"used_coupons_money": 0,
	
	
	
	
	public String getUsed_coupons_money() {
		return used_coupons_money;
	}
	public void setUsed_coupons_money(String used_coupons_money) {
		this.used_coupons_money = used_coupons_money;
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
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getPostage_money() {
		return postage_money;
	}
	public void setPostage_money(String postage_money) {
		this.postage_money = postage_money;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getOrder_total_price() {
		return order_total_price;
	}
	public void setOrder_total_price(String order_total_price) {
		this.order_total_price = order_total_price;
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
	
	
}
