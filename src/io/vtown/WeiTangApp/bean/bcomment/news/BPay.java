package io.vtown.WeiTangApp.bean.bcomment.news;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-28 下午3:46:10
 * 
 */
public class BPay extends BBase {

	private String appid;// ": "wxf104565ec7418036",
	private String noncestr;// ": "93bh1olg0adf8gnb387o1nc0t8pvglz9",
	// private String package": "Sign=WXPay",
	private String partnerid;// ": "1236552602",
	private String prepayid;// ": "wx2016062815393987f420f4430931110715",
	private String timestamp;// ": "1467099579",
	private String sign;// ": "A642362A9B9D4BE48B853AD0687C7B81"
	private String pay;
	
	private String  inviteCode;
//	private String seller_id;
	//支付时候需要获取的数据
	private String pay_url;//银联扫码支付

	
	
	
	// 扫码时候需要解析的
	private String type;// pay=>标识银联扫码支付；；goods==>商品详情；；invite==》生成下级二维码;;seller=》 店铺id
	private String token;// 获取的银联扫码支付的token
	private String qrcode;//发展下级的二维码时候的值和invite并存！！
	private String goods_id;
	private String seller_id; 
	
	private String is_brand;

	public String getPay_url() {
		return pay_url;
	}

	public void setPay_url(String pay_url) {
		this.pay_url = pay_url;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getNoncestr() {
		return noncestr;
	}

	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}

	public String getPartnerid() {
		return partnerid;
	}

	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}

	public String getPrepayid() {
		return prepayid;
	}

	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public BPay() {
		super();

	}

	public String getIs_brand() {
		return is_brand;
	}

	public void setIs_brand(String is_brand) {
		this.is_brand = is_brand;
	}

	public String getPay() {
		return pay;
	}

	public void setPay(String pay) {
		this.pay = pay;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

}
