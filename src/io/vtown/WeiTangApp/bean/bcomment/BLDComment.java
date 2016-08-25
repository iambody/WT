package io.vtown.WeiTangApp.bean.bcomment;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcache.BShop;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-12 下午2:21:55
 * @version 这个公用的bean是列表bean里面可能包含的bean不用做 列表
 */
public class BLDComment extends BBase {
	// show列表里面的 listitem里面 包含的一个对象sellerinfo*****************************

	private String id;// ": "3",
	private String is_brand;// ": "0",
	private String seller_name;// ": "sfasfasdf",
	private String cover;// ": "http://ww1.sinaimg.cn/mw1024/005HVYLcgw1f2iyqy4r0sj30qo0zk4ol.jpg",
	private String attention;// ": "0",
	private String avatar="";// ": "http://ww1.sinaimg.cn/mw1024/005HVYLcgw1f2iyqy4r0sj30qo0zk4ol.jpg"
	// show列表里面的 listitem里面 包含的一个对象goodinfo*****************************

	// private String id;// ": "1",
	private String seller_id;// ": "1",
	private String goods_sid;// ": "0",
	private String title;// ": "面膜商品标题",
	private String sell_price;// ": "10000",
	private String postage;// ": "0",
	private String is_agent;// ": "0",//是自营的还是品牌的
	private String sale_status;// 是否商家
	private List<BLComment> attr = new ArrayList<BLComment>();// ": [],
	// private String seller_name;// ": "好铺2016"

	// 购物车列表中需要的二级列表的列表bean******************************
	// private String id;//":"3",
	private String goods_price;// ":"2",
	private String attr_id;// ":"1x2",
	private String attr_name;// ":"蓝色XXL",
	private String goods_id;// ":"13"
	private String goods_name;//
	private String goods_num;//
	private boolean IsSelcet = false;// 商品是否被选择状态
	private String cid;// 规格id
	private String is_sales;// 购物车商品是否下架  0标识下架状态
	// 资金明细的列表的bean***********************
	// private String id;
	// private String seller_id;
	private String member_id;
	private String merchant_order_no;
	private String type;
	private String price;
	private String status;
	private String update_time;
	private String create_time;
	private String sale_type;
	private String date;
	private String dateStr;

	// 订单管理中里面那个ListView的bean******************************

	// private String goods_price;//"goods_price": "300",
	private String goods_attr_id;// "goods_attr_id": "5",
	private String goods_number;// "goods_number": "8",
	// private String goods_id;//"goods_id": "2",
	private String goods_type;// "goods_type": "0",
	// private String goods_name;//"goods_name": "面膜2",
	// private String channel;//"channel": "PT",
	private String goods_standard;// "goods_standard": "红色100ML",
	private String goods_cover;// "goods_cover":
								// "http://ww3.sinaimg.cn/mw690/6b2107bfjw1f3bjrigv0sj20w016ox6p.jpg"
	// private String goods_money;//goods_money=255500
	// 商品详情里面的=》goods_info***********************************
	private String rtype;// ": "0",
	private List<String> roll;
	private String intro;// ": "asdfasdfasf",
	private String deliver;// ": "21",
	private String is_revise;// ": "1",
	private String itype;// ": "0",
	// private String create_time;//": "0"
	private String rate;
	private String number;

	// 代理商店铺的数据=》base里面的bean************************************
	// private String id;// ": "2",
	// private String is_brand;// ": "0",
	// private String seller_name;// ": "zzzzzzzzzzzzzzz",
	// private String member_id;// ": "1",
	// private String cover;// ": "http://fs.v-town.cc/test20150516.jpg",
	// private String status;// ": "20",
	// private String attention;// ": "5",
	// private String avatar;// ": "http://fs.v-town.cc/head01.gif",
	// private String intro;// ": "zzzzzzzzzzzzzzzz",
	private String goods_count;
	private String credential;//
	private String todayVisitor;// ": 0
	private String is_collect;
	// // ***************************渠道管理=》扫描二维码成为下级的=》priv品牌的列表**********
	private String agency;// ": 10,
	private String level;// ": "4",
	private String description;// ": "成为ADIDAS品牌店的四级代理商"

	// ****************start*********店铺品牌代理列表*********start**************
	// private String id;
	// private String is_brand;
	// private String seller_name;
	// private String cover;
	// private String attention;
	// private String avatar;
	private String agency_time;
	// private String level;
	// ****************end*********店铺品牌代理列表*********end******************
	// 生成订单页面**********************oderbeing接口二级列表***********************
	// private String cid;// ":3,
	// private String member_id;// ":67,
	// private String goods_id;// ":7,
	// private String goods_num;// ":3,
	private String goods_attr;// ":94,
	private String goods_attr_name;// ":"蓝色100ML",
	private String creat_time;// ":1464865519,
	private String order_id;// ":2,
	// private String update_time;// ":1465185511,
	private String channel;// ":"CG",
	private String inventory_from;// ":0,
	private String is_delete;// ":1,
	// private String cover;// ":"http://fs.v-town.cc/cover6.jpg",
	private String goods_attr_price;// ":"890",
	// private String goods_price;// ":2670
	// private String title;
	// 我的店铺*******************************************
//	private BShop base = new BShop();
//	private String subCounter;// ": 2,
//	private String teamCounter;// ": 14,
//	// private String todayVisitor;// ": 0,
//	private String todayIncome;// ": 0,
//	private String todaySales;// ": 3,
//	private String totalIncome;// ": 0

	// **********************OderBeing界面******************************
	// *****coupons*******
	private String coupons_id;// ":"5",
	private String coupons_name;// ":"测试卡券3",
	private String coupons_money;// ":"3000"

	// *****coupons*******
	// private String member_id;//":"67",
	private String name = "";// ":"李四",
	private String mobile = "";// ":"12312312323",
	private String tel = "";// ":"",
	private String zipcode = "";// ":"",
	private String province = "";// ":"河北",
	private String city = "";// ":"河北",
	private String county = "";// ":"北京",
	private String address = "";// ":"河北"
	private String street_address = "";//
	// *****************start*************个人Show*************start********************
	// private String id;
	// private String is_brand;
	// private String seller_name;
	// private String cover;
	// private String attention;
	// private String avatar;
	// private String seller_id;
	// private String goods_sid;
	// private String sell_price;
	// private String postage;
	// private String is_agent;
	// private String cover;
	// private String sale_status;
	private String vid;
	// private List<String> attr;
	// private String seller_name;
	// private String is_brand;
	private String is_sale;

	// private String id;
	// private String seller_id;
	private String is_type;
	// private String goods_id;
	// private String goods_sid;
	private String pre_url;
	// private String vid;
	// private String intro;
	private String ratio;
	// private String create_time;
	private BLDComment sellerinfo;
	private List<String> imgarr = new ArrayList<String>();
	private String goodurl;
	private String qrcode;
	// private String date;
	private BLDComment goodinfo;
	private String sendnumber;

	// *****************end*************个人Show*************end********************

	// *****************Start*************商品详情规格*************Start********************

	private String p1;// "p1": "1304",
	private String v1;// "v1": "100ML",
	private String c1;// "c1": "容量",
	private String p2;// "p2": "1305",
	private String v2;// "v2": "蓝色",
	private String c2;// "c2": "颜色"

	// *****************end*************商品详情规格*************end********************

	// *****************Start*************添加商品之分类下面二级分类*************Start********************
	// private String id;//"id": "5",
	// private String name;// "name": "尺码"
	// *****************end*************添加商品之分类下面二级分类*************end********************

	// 搜索店铺获取的结果*****************************************************
	// private String id;// ": "2",
	// private String is_brand;// ": "0",
	// private String seller_name;// ": "Shanghai ERKE",
	// private String member_id;// ": "1",
	// private String cover;// ": "http://fs.v-town.cc/zzzzzzzzzzzzzzzz",
	// private String status;// ": "20",
	// private String attention;// ": "5",
	// private String avatar;// ": "http://fs.v-town.cc/head01.gif",
	// private String intro;// ": "wwwwww"

	// *****************Start*************添加商品之分类下面二级分类*************Start********************
	private String seller_order_sn;// "seller_order_sn": "S2016063010053102379",
	// private String goods_id;//"goods_id": "1",
	// private String goods_name;//"goods_name": "面膜1",
	private String goods_money;// "goods_money": "900",

	// private String goods_price;//"goods_price": "300",
	// private String goods_number;//"goods_number": "3",
	// private String goods_cover;//"goods_cover":
	// "http://img11.360buyimg.com/n7/jfs/t2794/118/556412839/103412/9e3234b5/5719f6d8Nc2c0b395.jpg"
	// *****************end*************添加商品之分类下面二级分类*************end********************

	// center====>未付款===》son_order的bean********************************
	// private String member_id;//": "410",
	// private String seller_id;//": "2",
	private String order_sn;// ": "A2016070457509799720",
	// private String seller_order_sn;//": "S2016070457515599729",
	private String source;// ": "0",
	private List<BDComment> goods = new ArrayList<BDComment>();

	// private String seller_name;// "seller_name": "国际知名品牌的代理",
	// private String number;// "number": 1

	// *****************Start*************采购单二级bean*************Start********************
	// private String goods_name;//"goods_name": "自营转代理书",
	// private String goods_id;//"goods_id": "673",
	// private String goods_price;//"goods_price": "111",
	// private String goods_number;// "goods_number": "2",
	// private String goods_cover;//"goods_cover":
	// "http://fs.v-town.cc/photo_14678629240000000000001471027178.jpg",
	// private String goods_standard;// "goods_standard": "100100",
	// private String goods_type;// "goods_type": "0",
	// private String goods_attr_id;//"goods_attr_id": "3062",
	// private String channel;//"channel": "CG",
	// private String seller_id;//"seller_id": "484"
	// *****************end*************采购单二级bean*************end********************

	// **************************************活动专区三级数据**************************************************
	// private String id;// ": "5",
	private String activity_category_id;// ": "1",
	// private String goods_id;// ": "636",
	private String sort;// ": "43",

	// private String create_time;// ": "1468290166",
	// private String update_time;// ": "1468309579",
	// private String is_delete;// ": "0",
	// private String goods_name;// ": "自营转代理书",
	// private String cover;//
	// ": "http://fs.v-town.cc/photo_14678629240000000000001471027178.jpg",
	// private String sell_price;// ": 110

	// //扫码后进行
	// private String agency;//":1,
	// private String level;//":2,
	// private String description;//":"成为kobe的二级代理商"


	public String getGoods_count() {
		return goods_count;
	}

	public void setGoods_count(String goods_count) {
		this.goods_count = goods_count;
	}

	public String getAgency_time() {
		return agency_time;
	}

	public String getSeller_order_sn() {
		return seller_order_sn;
	}

	public void setSeller_order_sn(String seller_order_sn) {
		this.seller_order_sn = seller_order_sn;
	}

	public String getGoods_money() {
		return goods_money;
	}

	public void setGoods_money(String goods_money) {
		this.goods_money = goods_money;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getGoods_attr_id() {
		return goods_attr_id;
	}

	public void setGoods_attr_id(String goods_attr_id) {
		this.goods_attr_id = goods_attr_id;
	}

	public String getGoods_type() {
		return goods_type;
	}

	public void setGoods_type(String goods_type) {
		this.goods_type = goods_type;
	}

	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		this.p1 = p1;
	}

	public String getV1() {
		return v1;
	}

	public void setV1(String v1) {
		this.v1 = v1;
	}

	public String getC1() {
		return c1;
	}

	public void setC1(String c1) {
		this.c1 = c1;
	}

	public String getP2() {
		return p2;
	}

	public void setP2(String p2) {
		this.p2 = p2;
	}

	public String getV2() {
		return v2;
	}

	public void setV2(String v2) {
		this.v2 = v2;
	}

	public String getC2() {
		return c2;
	}

	public void setC2(String c2) {
		this.c2 = c2;
	}

	public BLDComment() {
		super();

	}

	public void setAgency_time(String agency_time) {
		this.agency_time = agency_time;
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

	public String getGoods_standard() {
		return goods_standard;
	}

	public void setGoods_standard(String goods_standard) {
		this.goods_standard = goods_standard;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getSale_type() {
		return sale_type;
	}

	public void setSale_type(String sale_type) {
		this.sale_type = sale_type;
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

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
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

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getGoods_sid() {
		return goods_sid;
	}

	public void setGoods_sid(String goods_sid) {
		this.goods_sid = goods_sid;
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

	public String getPostage() {
		return postage;
	}

	public void setPostage(String postage) {
		this.postage = postage;
	}

	public String getIs_agent() {
		return is_agent;
	}

	public void setIs_agent(String is_agent) {
		this.is_agent = is_agent;
	}

	public List<BLComment> getAttr() {
		return attr;
	}

	public void setAttr(List<BLComment> attr) {
		this.attr = attr;
	}

	public String getSale_status() {
		return sale_status;
	}

	public void setSale_status(String sale_status) {
		this.sale_status = sale_status;
	}

	public String getAttr_id() {
		return attr_id;
	}

	public void setAttr_id(String attr_id) {
		this.attr_id = attr_id;
	}

	public String getAttr_name() {
		return attr_name;
	}

	public void setAttr_name(String attr_name) {
		this.attr_name = attr_name;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getGoods_num() {
		return goods_num;
	}

	public void setGoods_num(String goods_num) {
		this.goods_num = goods_num;
	}

	public boolean isIsSelcet() {
		return IsSelcet;
	}

	public void setIsSelcet(boolean isSelcet) {
		IsSelcet = isSelcet;
	}

	public String getGoods_price() {
		return goods_price;
	}

	public void setGoods_price(String goods_price) {
		this.goods_price = goods_price;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}

	public String getRtype() {
		return rtype;
	}

	public void setRtype(String rtype) {
		this.rtype = rtype;
	}

	public List<String> getRoll() {
		return roll;
	}

	public void setRoll(List<String> roll) {
		this.roll = roll;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
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

	public String getItype() {
		return itype;
	}

	public void setItype(String itype) {
		this.itype = itype;
	}

	public String getTodayVisitor() {
		return todayVisitor;
	}

	public void setTodayVisitor(String todayVisitor) {
		this.todayVisitor = todayVisitor;
	}

	public String getAgency() {
		return agency;
	}

	public void setAgency(String agency) {
		this.agency = agency;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGoods_attr() {
		return goods_attr;
	}

	public void setGoods_attr(String goods_attr) {
		this.goods_attr = goods_attr;
	}

	public String getGoods_attr_name() {
		return goods_attr_name;
	}

	public void setGoods_attr_name(String goods_attr_name) {
		this.goods_attr_name = goods_attr_name;
	}

	public String getCreat_time() {
		return creat_time;
	}

	public void setCreat_time(String creat_time) {
		this.creat_time = creat_time;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getInventory_from() {
		return inventory_from;
	}

	public void setInventory_from(String inventory_from) {
		this.inventory_from = inventory_from;
	}

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public String getGoods_attr_price() {
		return goods_attr_price;
	}

	public void setGoods_attr_price(String goods_attr_price) {
		this.goods_attr_price = goods_attr_price;
	}

//	public BShop getBase() {
//		return base;
//	}
//
//	public void setBase(BShop base) {
//		this.base = base;
//	}
//
//	public String getSubCounter() {
//		return subCounter;
//	}
//
//	public void setSubCounter(String subCounter) {
//		this.subCounter = subCounter;
//	}
//
//	public String getTeamCounter() {
//		return teamCounter;
//	}
//
//	public void setTeamCounter(String teamCounter) {
//		this.teamCounter = teamCounter;
//	}
//
//	public String getTodayIncome() {
//		return todayIncome;
//	}
//
//	public void setTodayIncome(String todayIncome) {
//		this.todayIncome = todayIncome;
//	}
//
//	public String getTodaySales() {
//		return todaySales;
//	}
//
//	public void setTodaySales(String todaySales) {
//		this.todaySales = todaySales;
//	}
//
//	public String getTotalIncome() {
//		return totalIncome;
//	}

//	public void setTotalIncome(String totalIncome) {
//		this.totalIncome = totalIncome;
//	}

	public String getCoupons_name() {
		return coupons_name;
	}

	public void setCoupons_name(String coupons_name) {
		this.coupons_name = coupons_name;
	}

	public String getCoupons_money() {
		return coupons_money;
	}

	public void setCoupons_money(String coupons_money) {
		this.coupons_money = coupons_money;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getIs_sale() {
		return is_sale;
	}

	public void setIs_sale(String is_sale) {
		this.is_sale = is_sale;
	}

	public String getIs_type() {
		return is_type;
	}

	public void setIs_type(String is_type) {
		this.is_type = is_type;
	}

	public String getPre_url() {
		return pre_url;
	}

	public void setPre_url(String pre_url) {
		this.pre_url = pre_url;
	}

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public BLDComment getSellerinfo() {
		return sellerinfo;
	}

	public void setSellerinfo(BLDComment sellerinfo) {
		this.sellerinfo = sellerinfo;
	}

	public List<String> getImgarr() {
		return imgarr;
	}

	public void setImgarr(List<String> imgarr) {
		this.imgarr = imgarr;
	}

	public String getGoodurl() {
		return goodurl;
	}

	public void setGoodurl(String goodurl) {
		this.goodurl = goodurl;
	}

	public String getQrcode() {
		return qrcode;
	}

	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}

	public BLDComment getGoodinfo() {
		return goodinfo;
	}

	public void setGoodinfo(BLDComment goodinfo) {
		this.goodinfo = goodinfo;
	}

	public String getSendnumber() {
		return sendnumber;
	}

	public void setSendnumber(String sendnumber) {
		this.sendnumber = sendnumber;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStreet_address() {
		return street_address;
	}

	public void setStreet_address(String street_address) {
		this.street_address = street_address;
	}

	public String getCoupons_id() {
		return coupons_id;
	}

	public void setCoupons_id(String coupons_id) {
		this.coupons_id = coupons_id;
	}

	public BLDComment(String coupons_id, String coupons_name,
			String coupons_money) {
		super();
		this.coupons_id = coupons_id;
		this.coupons_name = coupons_name;
		this.coupons_money = coupons_money;
	}

	public String getIs_collect() {
		return is_collect;
	}

	public void setIs_collect(String is_collect) {
		this.is_collect = is_collect;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getIs_sales() {
		return is_sales;
	}

	public void setIs_sales(String is_sales) {
		this.is_sales = is_sales;
	}

	public List<BDComment> getGoods() {
		return goods;
	}

	public void setGoods(List<BDComment> goods) {
		this.goods = goods;
	}

	public String getActivity_category_id() {
		return activity_category_id;
	}

	public void setActivity_category_id(String activity_category_id) {
		this.activity_category_id = activity_category_id;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

}
