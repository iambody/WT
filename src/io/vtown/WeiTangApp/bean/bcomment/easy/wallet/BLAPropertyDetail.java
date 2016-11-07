package io.vtown.WeiTangApp.bean.bcomment.easy.wallet;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-7-27 下午3:09:19
 *  资金明细
 */
public class BLAPropertyDetail extends BBase {
	private String capital_no;// capital_no=Z2016072756101574786
	private String create_time;// create_time=1469597512
	private String date;// date=13:31
	private String dateStr;// dateStr=今天
	private String id;// id=2521
	private String member_id;// member_id=699
	private String merchant_order_no;// merchant_order_no=S2016072710257100401
	private String pay_no;// pay_no=
	private String price;// price=4
	private String sale_type;// sale_type=1
	private String seller_id;// seller_id=576
	private String status;// status=1
	private String title;// title=订单S2016072710257100401
	private String type;// type=4
	private String update_time;// update_time=0
	private String  goods_name;//"goods_name": "返佣商品3（10/15）",

	private String bank_name;//"bank_name": "中国建设银行",
	private String fetch_money;//"fetch_money": 9900,
	private String fetch_type;//"fetch_type": 1,
	private String counter_fee;//"counter_fee": 100,

	private String direction;//"direction": 1,

	private String alipay;//"alipay": "",
	private String bank_card;//"bank_card": "1000089563333396",

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getCapital_no() {
		return capital_no;
	}
	public void setCapital_no(String capital_no) {
		this.capital_no = capital_no;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
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
	public String getMerchant_order_no() {
		return merchant_order_no;
	}
	public void setMerchant_order_no(String merchant_order_no) {
		this.merchant_order_no = merchant_order_no;
	}
	public String getPay_no() {
		return pay_no;
	}
	public void setPay_no(String pay_no) {
		this.pay_no = pay_no;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getSale_type() {
		return sale_type;
	}
	public void setSale_type(String sale_type) {
		this.sale_type = sale_type;
	}
	public String getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}


	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getFetch_money() {
		return fetch_money;
	}

	public void setFetch_money(String fetch_money) {
		this.fetch_money = fetch_money;
	}

	public String getFetch_type() {
		return fetch_type;
	}

	public void setFetch_type(String fetch_type) {
		this.fetch_type = fetch_type;
	}

	public String getCounter_fee() {
		return counter_fee;
	}

	public void setCounter_fee(String counter_fee) {
		this.counter_fee = counter_fee;
	}

	public String getAlipay() {
		return alipay;
	}

	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}

	public String getBank_card() {
		return bank_card;
	}

	public void setBank_card(String bank_card) {
		this.bank_card = bank_card;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
}
