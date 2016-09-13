package io.vtown.WeiTangApp.bean.bcomment;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcache.BShop;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-29 上午11:00:05 这个javabean用于非缓存的json二级对象bean
 */
@SuppressWarnings("serial")
public class BDComment extends BBase {
	// 首页的品牌图片跳转到的品牌页的二级data的bean
	private String is_brand;// ": "1",
	private String seller_name;// ": "asdfasdf",
	private String member_id;// ": "2",
	private String cover;// ": "http://ww2.sinaimg.cn/mw1024/005HVYLcjw1f2zjcmnxmyj30k00euqa9.jpg",
	private String status;// ": "20",
	private String attention;// ": "12",
	private String avatar;// ": "http://ww2.sinaimg.cn/mw1024/005HVYLcjw1f2zjcmnxmyj30k00euqa9.jpg",
	private String intro;// ": "11123123"
	// 商铺详情（SHow头像进入）
	// private String is_brand;////":"1",
	// private String seller_name;//":"asdfasdf",
	// private String member_id;//":"2",
	// private String cover;//":"sdfasdfasdfadf",
	// private String status;//":"20",
	// private String attention;//":"12",
	// private String avatar;//":"sdfasdfasdf",
	// private String intro;//":"11123123"

	// ****************start*********订单管理*********start*****************
	// 订单管理列表item的数据
	private String id;

	// private String member_id;
	private String order_sn;
	private String order_status;
	private String seller_order_sn;
	private String goods_price;
	private String money_paid;
	private String express_id;
	private String express_name;
	private String express_number;
	private String express_status;
	private String postage;
	private String info;
	private String create_time;
	private String update_time;
	private String send_time;
	private String confirm_time;
	private String cancel_time;
	private String end_time;
	private String return_reason;
	private String cancel_reason;
	private String express_key;
	private String remind_time;
	private String apply_time;
	private List<BLComment> goods = new ArrayList<BLComment>();

	// *****************start*********商品详情*********start******************

	// private String id;
	private String seller_id;
	private String goods_info_id;
	private String goods_sid;
	private String goods_pid;
	private String title;
	// private String postage;
	private String sell_price;
	private String sale_status;
	private String is_agent;
	private String vid;
	// private String cover;
	private String is_delete;
	private String category_id;
	private String virtual_store;
	private String real_store;
	private String sup_virtual;
	// private String create_time;
	// private String update_time;
	// private String seller_name;//、、": "avatar":
	// private String avatar;//
	private BLDComment goods_info = new BLDComment();
	private List<BLComment> goods_attr = new ArrayList<BLComment>();
	// private String is_desell;//
	private String is_dasell;// 品牌商品是否可以代理 0标识可以代理1标识不可以代理
	private String rtype;// ;
	private String deliver;
	private String is_revise;
	// private String create_time;
	// private String update_time;
	// private String is_agent;
	private String is_collect;
	private String is_desell;// 自营商品是否可以代卖 0标识可以代卖 1标识不可以代卖
	private String slevel;// "slevel": "0",
	private String max_price;// "max_price": 3,
	// private String ladder_price;// "ladder_price":
	private BLDComment ladder_price = new BLDComment();
	private String goods_url;// "goods_url":
								// "http://dev.vt.m.v-town.cn/goods/index/index?goods_id=2"
	// shop=》商品管理==》编辑=》商品编辑页面
	private int is_edit;
	// *****************end*********商品详情*********end******************

	// *****************start*********订单详情*********start******************
	// 订单详情bean数据

	// private String id;//"id": "68",
	// private String member_id;//"member_id": "2",
	// private String seller_id;//"seller_id": "2",
	// private String seller_name;//"seller_name": "发多没1234561111",
	// private String order_sn;//"order_sn": "A2016062157555248826",
	// private String order_status;//"order_status": "10",
	// private String seller_order_sn;//"seller_order_sn":
	// "S2016062157565255339",
	// private String goods_price;//"goods_price": "5100",
	// private String money_paid;//"money_paid": "5100",
	// private String express_id;//"express_id": "0",
	// private String express_name;//"express_name": "",
	// private String express_number;//"express_number": "",
	// private String express_status;//"express_status": "0",
	// private String postage;//"postage": "1",
	private String coupons_id;// "coupons_id": "0",
	private String coupons_price;// "coupons_price": "0",
	private String order_total_price;// order_total_price=210
	// private String info;//"info": "",
	// private String create_time;//"create_time": "1466488089",
	// private String update_time;//"update_time": "1466488089",
	// private String send_time;//"send_time": "0",
	// private String confirm_time;//"confirm_time": "0",
	// private String cancel_time;//"cancel_time": "0",
	// private String end_time;//"end_time": "0",
	private String pay_time;// "pay_time": "0",
	private String extend_confirm;// "extend_confirm": "0",
	// private String return_reason;//"return_reason": "",
	// private String cancel_reason;//"cancel_reason": "",
	// private String express_key;//"express_key": "",
	// private String remind_time;//"remind_time": "0",
	// private String apply_time;//"apply_time": "0",
	private String delivery_type;// "delivery_type": "0",
	private String is_send;//is_send=1
	private String delaynumber;// "delaynumber": "0",
	private String UUID;// "UUID": "868754024111604",
	private String source;// "source": "0",
	private String is_refund;// "is_refund": "0",
	private String channel;// "channel": "PT",
	private String mobile;// "mobile": "12345678901",
	private String username;// "username": "FF好几年",
	private String province;// "province": "安徽省",
	private String city;// "city": "合肥市",
	private String area;// "area": "枞阳县",
	private String address;// "address": "比你",
	// private List<BLComment> goods = new ArrayList<BLComment>();

	// *****************代理商店铺*****************
	private BLDComment base = new BLDComment();
	private List<BLComment> agents = new ArrayList<BLComment>();
	private List<BLComment> categorys = new ArrayList<BLComment>();
	private List<BLComment> agent = new ArrayList<BLComment>();
	// *****************start*********渠道管理页面*********start******************
	private String weekSales;
	private String weekstock;
	private String superior;
	private String junior;
	// *****************end*********渠道管理页面*********end******************
	// ***************************渠道管理=》扫描二维码成为下级**********
	// private String seller_id;// ": 2,
	private String phone;// ": 4,
	private List<BLDComment> priv = new ArrayList<BLDComment>();

	// *****************start*********地址管理页面*********start******************
	private int default_id;// "default": null,
	private List<BLComment> list = new ArrayList<BLComment>();

	// *****************end*********地址管理页面*********end******************

	// *****************start*********我的钱包页面*********start******************

	// private String member_id;//"member_id": "1",
	// private String seller_id;//"seller_id": "1",
	private String name;// "name": "1",
	private String identity_card;// "identity_card": "1",
	private String blance;// "blance": "799946259",
	private String assets;// "assets": "100",
	private String income;// "income": "21374",
	private String reflect;// "reflect": "0",
	private String freeze;// "freeze": "2052745",
	private String bank_id;// "bank_id": "2",
	private String alipay;// "alipay": "1",
	private String bank_name;// "bank_name": "中国工商银行"

	// *****************end*********我的钱包页面*********end******************

	// *****************start*********购物车--确认订单*********start******************
	// private String blance;//"blance": 0,
	private String creat_time;// "creat_time": 1466663228,
	// private String money_paid;// "money_paid": 300,
	// private String order_sn;// "order_sn": "A2016062399971001607"
	// *****************end*********购物车--确认订单*********end******************

	// *****************start*********我的采购单-采购单详情*********start******************
	// private String id;//"id": "172",
	// private String member_id;//"member_id": "345",
	// private String seller_id;//"seller_id": "1",
	// private String seller_name;//"seller_name": "kobe",
	// private String order_sn;//"order_sn": "A2016062810110199137",
	// private String order_status;//"order_status": "110",
	// private String seller_order_sn;//"seller_order_sn":
	// "S2016062810248494697",
	// private String goods_price;//"goods_price": "300",
	// private String money_paid;//"money_paid": "300",
	// private String express_id;//"express_id": "0",
	// private String express_name;//"express_name": "",
	// private String express_number;//"express_number": "",
	// private String express_status;//"express_status": "0",
	// private String postage;//"postage": "10",
	// private String coupons_id;//"coupons_id": "0",
	// private String coupons_price;//"coupons_price": "0",
	// private String info;//"info": "",
	// private String create_time;//"create_time": "1467083790",
	// private String update_time;//"update_time": "1467083790",
	// private String send_time;//"send_time": "0",
	// private String confirm_time;//"confirm_time": "0",
	// private String cancel_time;//"cancel_time": "1467084961",
	// private String end_time;//"end_time": "0",
	// private String pay_time;//"pay_time": "0",
	// private String extend_confirm;//"extend_confirm": "0",
	// private String return_reason;//"return_reason": "",
	// private String cancel_reason;//"cancel_reason": "超时取消",
	// private String express_key;//"express_key": "",
	// private String remind_time;//"remind_time": "0",
	// private String apply_time;//"apply_time": "0",
	// private String delivery_type;//"delivery_type": "0",
	// private String delaynumber;//"delaynumber": "0",
	// private String UUID;//"UUID": "",
	// private String source;//"source": "0",
	// private String is_refund;//"is_refund": "0",
	// private String channel;//"channel": "CG",
	// private String mobile;//"mobile": "18310998310",
	// private String username;//"username": "大兔兔",
	// private String province;//"province": "海南省",
	// private String city;//"city": "海口市",
	// private String area;//"area": "滨海区",
	// private String address;//"address": "完整地址",
	// private List<BLComment> goods = new ArrayList<BLComment>();
	// *****************end*********我的采购单-采购单详情*********end******************

	// ***************start*********我的--我的订单--订单详情*********start******************
	// private String id;//"id": "187",
	// private String member_id;//"member_id": "355",
	// private String seller_id;//"seller_id": "241",
	// private String seller_name;//"seller_name": "大兔兔的自营铺子2016-总店",
	// private String order_sn;//"order_sn": "A2016062897985498880",
	// private String order_status;//"order_status": "110",
	// private String seller_order_sn;//"seller_order_sn":
	// "S2016062897994851486",
	// private String goods_price;//"goods_price": "900",
	// private String money_paid;//"money_paid": "900",
	// private String express_id;//"express_id": "0",
	// private String express_name;//"express_name": "",
	// private String express_number;//"express_number": "",
	// private String express_status;//"express_status": "0",
	// private String postage;//"postage": "10",
	// private String coupons_id;//"coupons_id": "0",
	// private String coupons_price;//"coupons_price": "0",
	// private String info;//"info": "",
	// private String create_time;//"create_time": "1467101050",
	// private String update_time;//"update_time": "1467101050",
	// private String send_time;//"send_time": "0",
	// private String confirm_time;//"confirm_time": "0",
	// private String cancel_time;//"cancel_time": "1467168781",
	// private String end_time;//"end_time": "0",
	// private String pay_time;//"pay_time": "0",
	// private String extend_confirm;//"extend_confirm": "0",
	// private String return_reason;//"return_reason": "",
	// private String cancel_reason;//"cancel_reason": "超时取消",
	// private String express_key;//"express_key": "",
	// private String remind_time;//"remind_time": "1467106177",
	// private String apply_time;//"apply_time": "0",
	// private String delivery_type;//"delivery_type": "0",
	// private String delaynumber;//"delaynumber": "0",
	// private String UUID;//"UUID": "",
	// private String source;//"source": "0",
	// private String is_refund;//"is_refund": "0",
	private String refund;//refund=0
	// private String channel;//"channel": "PT",
	// private String province;//"province": "海南省",
	// private String city;//"city": "海口市",
	// private String area;//"area": "滨海区",
	// private String address;//"address": "门牌号",
	// private String username;//"username": "大兔兔买买",
	// private String mobile;//"mobile": "18310998315",
	// private List<BLComment> goods = new ArrayList<BLComment>();// "detail":

	private String logisticinfo;// "logisticinfo": "",
	private String number;// "number": 1


	// *****************end*********我的--我的订单--订单详情*********end******************

	// center====>未付款===》son_order的bean********************************
	// private String seller_order_sn;// ": "S2016070457515599729",
	private String goods_id;// ": "2",
	private String goods_name;// ": "面膜2",
	private String goods_money;// ": "3",
	// private String money_paid;// ": "3",
	private String goods_number;// ": "1",
	private String goods_cover;// ": "http://ww3.sinaimg.cn/mw690/6b2107bfjw1f3bjrigv0sj20w016ox6p.jpg",
	private String purchase_price;// ": "0",

	// private String source;// ": "20"
	// ***********************************活动专区的数据************************************************
	// private String id;// ": "1",
	// private String title;// ": "1234",
	private String pic_path;// ": "http://fs.v-town.cc/activity1468050744",
	// private String status;// ": "1",
	// private String create_time;// ": "1468034859",
	// private String update_time;// ": "1468050744",
	// private String is_delete;// ": "0",
	private String content;// ": "12334",
	private String url;// ": "123334",
	private List<BLComment> category = new ArrayList<BLComment>();

	// *****************start*********去付款*********start******************
	//private String blance;// blance=7534
	//private String create_time;// create_time=1469168407
	//private String money_paid;// money_paid=210
	//private String order_sn;// order_sn=A2016072255101574680
	// *****************end*********去付款*********end******************

	// *****************start*********提现*********start******************
	private List<BLComment> bank_list = new ArrayList<BLComment>();
	private BLComment alipay_list;
	private String tixinarule;//提现的规则
	// *****************end*********提现*********end******************
	
	
	

	public String getId() {
		return id;
	}

	public String getIs_send() {
		return is_send;
	}

	public void setIs_send(String is_send) {
		this.is_send = is_send;
	}

	public String getOrder_total_price() {
		return order_total_price;
	}

	public void setOrder_total_price(String order_total_price) {
		this.order_total_price = order_total_price;
	}

	public String getRefund() {
		return refund;
	}

	public void setRefund(String refund) {
		this.refund = refund;
	}

	public BLComment getAlipay_list() {
		return alipay_list;
	}

	public void setAlipay_list(BLComment alipay_list) {
		this.alipay_list = alipay_list;
	}

	public List<BLComment> getBank_list() {
		return bank_list;
	}

	public void setBank_list(List<BLComment> bank_list) {
		this.bank_list = bank_list;
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

	public BLDComment getLadder_price() {
		return ladder_price;
	}

	public void setLadder_price(BLDComment ladder_price) {
		this.ladder_price = ladder_price;
	}

	public String getSlevel() {
		return slevel;
	}

	public void setSlevel(String slevel) {
		this.slevel = slevel;
	}

	public String getMax_price() {
		return max_price;
	}

	public void setMax_price(String max_price) {
		this.max_price = max_price;
	}

	public String getGoods_url() {
		return goods_url;
	}

	public void setGoods_url(String goods_url) {
		this.goods_url = goods_url;
	}

	public String getCreat_time() {
		return creat_time;
	}

	public void setCreat_time(String creat_time) {
		this.creat_time = creat_time;
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

	public String getExtend_confirm() {
		return extend_confirm;
	}

	public void setExtend_confirm(String extend_confirm) {
		this.extend_confirm = extend_confirm;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getIs_refund() {
		return is_refund;
	}

	public void setIs_refund(String is_refund) {
		this.is_refund = is_refund;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getIs_collect() {
		return is_collect;
	}

	public void setIs_collect(String is_collect) {
		this.is_collect = is_collect;
	}

	public String getIs_desell() {
		return is_desell;
	}

	public void setIs_desell(String is_desell) {
		this.is_desell = is_desell;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentity_card() {
		return identity_card;
	}

	public void setIdentity_card(String identity_card) {
		this.identity_card = identity_card;
	}

	public String getBlance() {
		return blance;
	}

	public void setBlance(String blance) {
		this.blance = blance;
	}

	public String getAssets() {
		return assets;
	}

	public void setAssets(String assets) {
		this.assets = assets;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getReflect() {
		return reflect;
	}

	public void setReflect(String reflect) {
		this.reflect = reflect;
	}

	public String getFreeze() {
		return freeze;
	}

	public void setFreeze(String freeze) {
		this.freeze = freeze;
	}

	public String getBank_id() {
		return bank_id;
	}

	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}

	public String getAlipay() {
		return alipay;
	}

	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public int getDefault_id() {
		return default_id;
	}

	public void setDefault_id(int default_id) {
		this.default_id = default_id;
	}

	public List<BLComment> getList() {
		return list;
	}

	public void setList(List<BLComment> list) {
		this.list = list;
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

	public List<BLComment> getGoods() {
		return goods;
	}

	public void setGoods(List<BLComment> goods) {
		this.goods = goods;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getGoods_info_id() {
		return goods_info_id;
	}

	public void setGoods_info_id(String goods_info_id) {
		this.goods_info_id = goods_info_id;
	}

	public String getGoods_sid() {
		return goods_sid;
	}

	public void setGoods_sid(String goods_sid) {
		this.goods_sid = goods_sid;
	}

	public String getGoods_pid() {
		return goods_pid;
	}

	public void setGoods_pid(String goods_pid) {
		this.goods_pid = goods_pid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSell_price() {
		return sell_price;
	}

	public void setSell_price(String sell_price) {
		this.sell_price = sell_price;
	}

	public String getSale_status() {
		return sale_status;
	}

	public void setSale_status(String sale_status) {
		this.sale_status = sale_status;
	}

	public String getIs_agent() {
		return is_agent;
	}

	public void setIs_agent(String is_agent) {
		this.is_agent = is_agent;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getVirtual_store() {
		return virtual_store;
	}

	public void setVirtual_store(String virtual_store) {
		this.virtual_store = virtual_store;
	}

	public String getReal_store() {
		return real_store;
	}

	public void setReal_store(String real_store) {
		this.real_store = real_store;
	}

	public String getSup_virtual() {
		return sup_virtual;
	}

	public void setSup_virtual(String sup_virtual) {
		this.sup_virtual = sup_virtual;
	}

	public String getRtype() {
		return rtype;
	}

	public void setRtype(String rtype) {
		this.rtype = rtype;
	}

	public String getDeliver() {
		return deliver;
	}

	public void setDeliver(String deliver) {
		this.deliver = deliver;
	}

	public String getIs_revise() {
		return is_revise;
	}

	public void setIs_revise(String is_revise) {
		this.is_revise = is_revise;
	}

	public String getIs_brand() {
		return is_brand;
	}

	public void setIs_brand(String is_brand) {
		this.is_brand = is_brand;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAttention() {
		return attention;
	}

	public void setAttention(String attention) {
		this.attention = attention;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public BDComment() {
		super();

	}

	private String makeMoney;
	private int makeMoneyType;

	public BDComment(String makeMoney, int makeMoneyType) {
		this.makeMoney = makeMoney;
		this.makeMoneyType = makeMoneyType;
	}

	public String getMakeMoney() {
		return makeMoney;
	}

	public void setMakeMoney(String makeMoney) {
		this.makeMoney = makeMoney;
	}

	public int getMakeMoneyType() {
		return makeMoneyType;
	}

	public void setMakeMoneyType(int makeMoneyType) {
		this.makeMoneyType = makeMoneyType;
	}

	public List<BLComment> getGoods_attr() {
		return goods_attr;
	}

	public void setGoods_attr(List<BLComment> goods_attr) {
		this.goods_attr = goods_attr;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getPay_time() {
		return pay_time;
	}

	public void setPay_time(String pay_time) {
		this.pay_time = pay_time;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public BLDComment getBase() {
		return base;
	}

	public void setBase(BLDComment base) {
		this.base = base;
	}

	public List<BLComment> getAgents() {
		return agents;
	}

	public void setAgents(List<BLComment> agents) {
		this.agents = agents;
	}

	public List<BLComment> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<BLComment> categorys) {
		this.categorys = categorys;
	}

	public String getWeekSales() {
		return weekSales;
	}

	public void setWeekSales(String weekSales) {
		this.weekSales = weekSales;
	}

	public String getWeekstock() {
		return weekstock;
	}

	public void setWeekstock(String weekstock) {
		this.weekstock = weekstock;
	}

	public String getSuperior() {
		return superior;
	}

	public void setSuperior(String superior) {
		this.superior = superior;
	}

	public String getJunior() {
		return junior;
	}

	public void setJunior(String junior) {
		this.junior = junior;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<BLDComment> getPriv() {
		return priv;
	}

	public void setPriv(List<BLDComment> priv) {
		this.priv = priv;
	}

	public BLDComment getGoods_info() {
		return goods_info;
	}

	public void setGoods_info(BLDComment goods_info) {
		this.goods_info = goods_info;
	}

	public String getIs_dasell() {
		return is_dasell;
	}

	public void setIs_dasell(String is_dasell) {
		this.is_dasell = is_dasell;
	}

	/**
	 * 我的订单跳转到支付界面时候需要进行
	 * 
	 * @param order_sn
	 * @param money_paid
	 * @param blance
	 * @param creat_time
	 */
	public BDComment(String order_sn, String money_paid, String blance,
			String create_time) {
		super();
		this.order_sn = order_sn;
		this.money_paid = money_paid;
		this.blance = blance;
		this.create_time = create_time;
	}

	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getGoods_money() {
		return goods_money;
	}

	public void setGoods_money(String goods_money) {
		this.goods_money = goods_money;
	}

	public String getGoods_number() {
		return goods_number;
	}

	public void setGoods_number(String goods_number) {
		this.goods_number = goods_number;
	}

	public String getGoods_cover() {
		return goods_cover;
	}

	public void setGoods_cover(String goods_cover) {
		this.goods_cover = goods_cover;
	}

	public String getPurchase_price() {
		return purchase_price;
	}

	public void setPurchase_price(String purchase_price) {
		this.purchase_price = purchase_price;
	}

	public List<BLComment> getAgent() {
		return agent;
	}

	public void setAgent(List<BLComment> agent) {
		this.agent = agent;
	}

	public int getIs_edit() {
		return is_edit;
	}

	public void setIs_edit(int is_edit) {
		this.is_edit = is_edit;
	}

	public String getPic_path() {
		return pic_path;
	}

	public void setPic_path(String pic_path) {
		this.pic_path = pic_path;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<BLComment> getCategory() {
		return category;
	}

	public void setCategory(List<BLComment> category) {
		this.category = category;
	}

	public String getTixinarule() {
		return tixinarule;
	}

	public void setTixinarule(String tixinarule) {
		this.tixinarule = tixinarule;
	}
}
