package io.vtown.WeiTangApp.bean.bcomment;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-18 下午3:15:41 存放内层List列表的字段
 */
@SuppressWarnings("serial")
public class BLComment extends BBase {
	// 最新的首页的广告列表里面的数据
	// private String id;//:"4",
	// private String title;//:"广告4",
	private String pic_path;// :"我的://fs.v-town.cc/advert1468404720",
	private String url;// "http://dev.vt.m.v-town.cn/goods/index/index?goods_id=706",
	private int advert_type;// :"2",advert_type 类型 1H5首页，2商品详情页，3店铺详情页,4活动详情页
	private String category_id;// "0",
	private String price;// :"0",
	private String recommend_position;// :"1",
	// private String status;//:"1",
	// private String update_time;//:"0",
	// private String create_time;//:"1468404720",
	private String source_id;// :"706",
	private String client;// :"10",
	// private String is_delete;//:"0",
	private String sort;// :"5",
	private String advert_type_str;// :"商品详情页"

	// 搜索界面的热门商品&&&搜索商品获取的搜索结果 TODO此处需要进行搜索历史的缓存**********************
	private String id;// ": "3",
	private String title;// ": "iPhone SE"
	private String orig_price;
	// 首页的品牌图片跳转到的品牌页的列表的bean**********************

	// private String id;//": "2",
	// private String title;//": "面膜
	private String is_agent;// ": "0",
	private String sell_price;// ": "12000",
	private String cover;// ": "http://ww2.sinaimg.cn/mw690/717a1ca7jw1f2uaz63y3fj21hc1z44qp.jpg"
	// home页POp跳转到品牌列表页的品牌列表item数据**********************
	// private String id;// ":"1",
	private String seller_name;// ":"asdfasdf",
	// private String cover;//":"http://ww2.sinaimg.cn/j30k00euqa9.jpg"
	// 产品的分类页面中的gradview的==>bean**********************
	// private String id;//": "2",
	private String cate_name;// ": "粉底液"

	// 商铺里面订单管理在外层==>bean**********************
	private List<BLComment> a = new ArrayList<BLComment>();
	// 商铺里面订单管理在内层==>bean**********************

	// 商品show列表的item的 数据n**********我的个人Show需要****************************
	private String seller_id;// ": "3",
	private String goods_id;// ": "1",
	private String is_type;// ": "0",发show形式，0图片 1 视频
	private String vid;// ": "",
	private String intro;// ": "我在测试",
	private String create_time="0";// ": "1462784207",
	private String goodurl;// ": "http://dev.vt.api.v-town.cn:8500/v1/goods/goods/detail?goods_id=1",
	private String qrcode;// ": "",
	private String sendnumber="0";// ": "1"
	private String teamCounter;
	private BLDComment sellerinfo = new BLDComment();
	private BLDComment goodinfo = new BLDComment();
	private String pre_url;// 视频时候的视频的第一帧的封面图
	private List<String> imgarr = new ArrayList<String>();
	private List<BLComment> attr = new ArrayList<BLComment>();
	// 商品show的BLDComment里面的一个数组使用到的key（规格）*****************************************
	// private String id;//":"1",
	private String goods_sid;// ":"1",
	private String attr_id;// ":"1x2",
	private String attr_name;// ":"蓝色XXL",
	// private String sell_price;//":"10000",
	private String agent_price;// ":"0",
	private String store;// ":"9999999",
	private String vstore;// ":"99999999",
	private String commission;// ":"10",
	private String starting;// ":"0",
	private String is_whole;// ":"0"

	// 购物车列表中需要的bean ***********
	// private String seller_id;// ":"10",
	// private String seller_name;// ":"100",
	// private String is_brand;//
	private List<BLDComment> list = new ArrayList<BLDComment>();
	private boolean IsCanSelct = false;
	// 图片在信息 TODO在图片列表上传时候需要用到在***********
	private Bitmap picBitmap;
	private File picFile;
	private String picFileStr;
	private byte[] FileByte;

	// 查询资金明细***********
	// private List<BLDComment> capitalDetail = new ArrayList<BLDComment>();
	private String month;
	// ****************start*********订单管理*********start*****************

	private String UUID;// "UUID": "",
	private String express_number;// "express_number": "",
	private String express_id;// "express_id": 0,
	private String info;// "info": "",
	private String postage;// "postage": 1,
	private String is_send;// "is_send": "1",
	private String remind_time;// "remind_time": 0,
	// private String id;// "id": "123",
	private String send_time;// "send_time": 0,
	// private String sell_price;// "sell_price": "300",
	private String goods_price;// "goods_price": 5100,
	private String goods_attr_id;// "goods_attr_id": "5",
	private String express_name;// "express_name": "",
	private String money_paid;// "money_paid": 5100,
	private String cancel_reason;// "cancel_reason": "",
	private String apply_time;// "apply_time": 0,
	private String return_reason;// "return_reason": "",
	private String send_from;// "send_from": "1",
	private String number;// "number": 2,
	private String send_seller_id;// "send_seller_id": "2",
	private String order_status;// "order_status": "10",
	private String member_id;// "member_id": "2",
	private String cancel_time;// "cancel_time": 0,
	private String is_refund;// "is_refund": "1",
	private String is_show;// "is_show": "1",
	private String express_status;// "express_status": 0,
	private String source;// "source": "0",
	private String update_time;// "update_time": "0",
	private String refund;// refund=0;
	private String end_time;// "end_time": 0,
	// private String goods_id;// "goods_id": "2",
	// private String create_time;// "create_time": "1466488089",
	private String express_key;// "express_key": "",
	// private String seller_id;//"seller_id": "2",
	private String seller_order_sn;// "order_sn": "S2016062157565255339",
	// private String channel;// "channel": "PT",
	private String confirm_time;// "confirm_time": 0,
	// private String is_edit;// "is_edit": "1"

	private List<BLDComment> goods = new ArrayList<BLDComment>();
	// ****************end*********订单管理*********end*****************

	// ****************start*********订单详情*********start*****************
	// 订单详情list里bean
	// private String goods_name;//"goods_name": "面膜2",
	// private String goods_id;//"goods_id": "2",
	// private String goods_price;//"goods_price": "300",
	// private String goods_number;//"goods_number": "8",
	// private String
	// goods_cover;//"goods_cover":"http://ww3.sinaimg.cn/mw690/6b2107bfjw1f3bjrigv0sj20w016ox6p.jpg",
	// private String goods_standard;//"goods_standard": "红色100ML",
	// private String goods_type;//"goods_type": "0",
	// private String goods_attr_id;//"goods_attr_id": "5",
	// private String channel;//"channel": "PT"
	private String goods_money;// goods_money=255500
	private String purchase_price;
	// ***************end*********订单详情*********end*****************

	private String goods_name;
	// 商品详情=》里面的规格列表
	// private String id;//": "3",
	// private String goods_sid;//": "2",
	// private String attr_id;//": "1x2",
	// private String attr_name;//": "蓝色XXL",
	// private String sell_price;//": "500",
	// private String store;//": "34",
	// private String vstore;//": "0"
	// private String goods_id;
	// private String goods_price;
	private String goods_number;
	private String goods_cover;
	private String goods_standard;
	private String goods_type;
	private BLDComment attr_map = new BLDComment();
	// private String goods_attr_id;
	private String channel;

	// ****************start*********商品管理*********start*****************
	// 商品管理列表信息
	// private String id;
	// private String seller_id;
	// private String title;
	// private String is_agent;
	// private String sell_price;
	// private String cover;
	private String sales;
	private String is_edit;
	private String max_price;
	private String goods_url;

	// 品牌的分类的列表**************************
	// private String id;// ": "1",
	// private String seller_id;// ": "1",
	// private String title;// ": "面膜1",
	// private String is_agent;// ": "0",
	// private String sell_price;// ": "10000",
	// private String cover;//
	private boolean IsBrandDetaiLsSelect = false;

	// ": "http://img11.360buyimg.com/n7/jfs/t2794/118/556412839/103412/9e3234b5/5719f6d8Nc2c0b395.jpg",
	// private String sale_status;// ": "100",
	// private String sales;// ": 0,
	// private String is_edit;// ": 1
	/**
	 * 
	 * @param picBitmap
	 *            图片的Bitmap
	 * @param picFile
	 *            图片视频在路径
	 * @param picFileStr
	 *            图片视频在路径地址
	 * @param fileByte
	 *            图片视频转换成在 IO流
	 */
	public BLComment(Bitmap picBitmap, File picFile, String picFileStr,
			byte[] fileByte) {
		super();
		this.picBitmap = picBitmap;
		this.picFile = picFile;
		this.picFileStr = picFileStr;
		FileByte = fileByte;
	}

	// *****************start*********商品详情*********start******************
	// private String attr_id;
	// private String sell_price;
	// private String agent_price;
	// private String store;
	// private String commission;
	// private String starting;
	// private String is_whole;
	// ****************start*********商品关注*********start**************
	// private String id;
	// private String seller_id;
	// private String goods_sid;
	// private String title;
	// private String sell_price;
	// private String postage;
	// private String is_agent;
	// private String cover;
	private String sale_status;
	// ****************start*********店铺关注*********start**************
	// private String id;
	private String is_brand;
	// private String seller_name;
	// private String cover;
	private String attention;
	private String avatar;

	// private String intro;
	// ****************start*********浏览记录*********start**************
	// private String id;
	// private String seller_id;
	// private String goods_sid;
	// private String title;
	// private String sell_price;
	// private String postage;
	// private String is_agent;
	// private String cover;
	// private String sale_status;
	// 代理商店铺的分类列表和品牌的列表***************************************
	// private String id;// ": "1",
	// private String is_brand;// ": "1",
	// private String seller_name;// ": "NIKE品牌店",
	// private String cover;//
	// ": "http://ww1.sinaimg.cn/orj480/6b2107bfjw1f4chandkkvj20yi1a0u0x.jpg",
	// private String attention;// ": "0",
	// private String avatar;//
	// ": "http://tva4.sinaimg.cn/crop.0.0.512.512.180/6b2107bfjw8evuyhyfgz9j20e80e8q3h.jpg",
	private String agency_time;// ": "2016-05-06"
	// *****分类列表****
	// private String id;// ": "2",
	// private String cate_name;// ": "粉底液"
	// ****************start*********商品分类列表*********start**************
	// private String id;
	// private String seller_id;
	// private String title;
	// private String is_agent;
	// private String sell_price;
	// private String cover;
	// private String sale_status;
	// ****************end*********商品分类列表*********end******************

	// ****************start*********我的卡卷列表*********start**************
	private String coupons_id;
	private String taken_time;
	private String coupons_name;
	private String coupons_money;
	private String used_endtime;
	private String used_starttime;
	// private String info;
	private String used_day;
	private String status;// 0:可有，1:可用
	// ****************end*********我的卡卷列表*********end******************

	// ****************start*********渠道管理我的下级列表*********start**************
	// private String id;
	// private String is_brand;
	// private String seller_name;
	// private String cover;
	// private String attention;
	// private String avatar;
	// ****************end*********渠道管理我的下级列表*********end******************

	// ****************start*********渠道管理我的上级列表*********start**************
	// private String id;
	// private String is_brand;
	// private String seller_name;
	// private String cover;
	// private String attention;
	// private String avatar;
	// ****************end*********渠道管理我的上级列表*********end******************

	// ****************start*********地址管理列表*********start**************

	private String address;// "address": "梵蒂冈法国",
	private String county;// "county": "风光风光",
	private String address_id;// "address_id": "4",
	private String zipcode;// "zipcode": "",
	private String tel;// "tel": "123456",
	private String name;// "name": "lin",
	private String province;// "province": "北京",
	private String city;// "city": "北京",
	private String mobile;// "mobile": "1234567",
	// private String member_id;//"member_id": "2"

	// ****************end*********地址管理列表*********end******************

	// ****************start*********我的银行卡列表*********start**************

	// private String icon;
	// private String bank_name;
	// private String bank_code;
	private String card_number;
	private String bank_id;
	// ****************end*********我的银行卡列表*********end******************

	// ****************start*********银行卡列表*********start**************
	private String icon;
	private String bank_name;
	private String bank_code;

	// ****************end*********银行卡列表*********end******************
	// ****************end*********地址管理列表*********end******************

	// *****************start*********支付宝管理页面*********start******************
	// private String name;
	private String alipay;
	// *****************end*********支付宝管理页面*********end******************

	// 渠道管理邀请下级-代理的品牌列表*************************************
	// private Stringid;//":"1",
	// private Stringseller_name;//":"asdfasdf",
	// private Stringcover;//":"sfasdfasdfasdf",
	// private Stringintro;//":"11123123",
	// private Stringavatar;//":"sdfasdfasdf",
	private List<String> levels = new ArrayList<String>();
	private String level;
	// 生成订单页面**********************oderbeing接口**list列表************************************************
	// private String postage;//":"10",
	private String all_money;// ":"2670",
	// private String seller_name;//":"Jack Jones",
	private String all_goods_num;// ":3,
	private List<BLDComment> store_list = new ArrayList<BLDComment>();// ":[

	// *****************start*********我的Show*********start******************
	private String date;
	// private List<BLDComment> listss=new ArrayList<BLDComment>();
	// *****************end*********我的Show*********end******************

	// *****************start*********添加商品之分类*********start******************
	// private String id;//"id":"1",
	// private String cate_name;//"cate_name":"化妆",
	private String pid;// "pid":"0"
	private List<BLDComment> attrs = new ArrayList<BLDComment>();

	// *****************end*********添加商品之分类*********end******************

	// *****************start*********添加商品之规格*********start******************
	private String add_good_id;// 分类Id
	private String add_good_cate_name;// 分类名称
	private String add_good_attrs_id_1;// 分类参数Id
	private String add_good_attrs_id_2;// 分类参数Id
	private String add_good_attrs_name_1;// 分类参数名称
	private String add_good_attrs_name_2;// 分类参数名称

	private String add_good_attrs_value_1;// 输入的参数值
	private String add_good_attrs_value_2;// 输入的参数值
	private String add_good_attrs_value_3;// 输入的参数值
	private String add_good_attrs_value_4;// 输入的参数值

	// *****************end*********添加商品之规格*********end******************

	// ***********************代理店铺下边的商品列表*********************************
	// private String id;// ": "11",
	// private String seller_id;// ": "2",
	// private String title;// ": "面膜11",
	// private String is_agent;// ": "1",
	// private String sell_price;// ": 111,
	// private String cover;//
	// ": "http://ww2.sinaimg.cn/mw690/717a1ca7jw1f2uaz63y3fj21hc1z44qp.jpg",
	// private String sale_status;// ": "100",
	// private String sales;// ": "",
	// private String is_edit;// ": 0,
	// private String max_price;// ": 110
	// ***********************消息首页的列表***************************
	private String source_type;
	private BNew message_info = new BNew();
	// 折线图*********************************
	// private String date;
	private String value;

	// *****************start*********快递列表*********start******************
	private String key;// "key": "shunfeng",
	// private String name;//"name": "顺丰",
	private String acronym;// "acronym": "S"

	// private String id;
	// *****************end*********快递列表*********end******************

	// *****************start*********查看物流*********start******************
	private String time;// "time":"2016-04-21 11:01:09",
	private String ftime;// "ftime":"2016-04-21 11:01:09",
	private String context;// "context":"已签收,感谢使用顺丰,期待再次为您服务"

	// *****************end*********查看物流*********end******************

	// *****************start*********我的采购单列表*********start******************
	// private String id;//"id": "155",
	// private String member_id;//"member_id": "345",
	// private String seller_id;//"seller_id": "1",
	// private String seller_name;//"seller_name": "kobe",
	// private String order_sn;//"order_sn": "A2016062710049524467",
	// private String order_status;//"order_status": "40",
	// private String seller_order_sn;// "seller_order_sn":
	// "S2016062710049100187",
	// private String goods_price;//"goods_price": "300",
	// private String money_paid;//"money_paid": "0",
	// private String express_id;//"express_id": "0",
	// private String express_name;//"express_name": "",
	// private String express_number;//"express_number": "",
	// private String express_status;// "express_status": "0",
	// private String postage;//"postage": "1",
	// private String coupons_id;//"coupons_id": "393",
	private String coupons_price;// "coupons_price": "5000",
	// private String info;//"info": "",
	// private String create_time;//"create_time": "1467020412",
	// private String update_time;//"update_time": "1467020412",
	// private String send_time;//"send_time": "0",
	// private String confirm_time;//"confirm_time": "0",
	// private String cancel_time;//"cancel_time": "0",
	// private String end_time;//"end_time": "0",
	private String pay_time;// "pay_time": "1467020412",
	private String extend_confirm;// "extend_confirm": "0",
	// private String return_reason;//"return_reason": "就是要退款",
	// private String cancel_reason;//"cancel_reason": "",
	// private String express_key;//"express_key": "",
	// private String remind_time;//"remind_time": "0",
	// private String apply_time;//"apply_time": "1467021216",
	private String delivery_type;// "delivery_type": "0",
	private String delaynumber;// "delaynumber": "0",

	// private String UUID;//"UUID": "",
	// private String source;//"source": "0",
	// private String is_refund;//"is_refund": "1",
	// private String channel;//"channel": "CG",
	// private String number;//"number": 1,
	// List<BLDComment> goods = new ArrayList<BLDComment>();
	// *****************end*********我的采购单列表*********end******************

	// *****************start*********我的采购单列表--未付款*********start******************
	// private String id;//"id": "624",
	private String order_sn;// "order_sn": "A2016070810151515623",
	// private String order_note;//"order_note": "我要测试",
	// private String order_status;//"order_status": "10",
	// private String member_id;//"member_id": "607",
	// private String seller_id;//"seller_id": "484",
	// private String mobile;//"mobile": "18621573373",
	// private String username;//"username": "测试",
	// private String province;//"province": "北京市",
	// private String city;//"city": "北京市",
	// private String area;//"area": "朝阳区",
	// private String address;//"address": "吉洛优品",
	// private String order_total_price;//"order_total_price": "451",
	// private String money_paid;//"money_paid": "451",
	private String used_balance;// "used_balance": "0",
	private String pay_sn;// "pay_sn": "",
	private String pay_type;// "pay_type": "0",
	// private String pay_time;//"pay_time": "0",
	// private String end_time;//"end_time": "0",
	private String pay_status;// "pay_status": "0",
	private String used_coupons;// "used_coupons": "0",
	private String used_coupons_name;// "used_coupons_name": "",
	private String used_coupons_money;// "used_coupons_money": "0",
	// private String creat_time;//"creat_time": "1467963453",
	// private String postage_money;//"postage_money": "0",
	private String is_delete;// "is_delete": "0",
	// private String remind_time;//"remind_time": "0",
	// private String UUID;//"UUID": "12345678",
	// private String update_time;//"update_time": "0",
	private String postcode;// "postcode": "",
	private String payment_type;// "payment_type": "0",
	private String trade_type;// "trade_type": "",
	private String cancel_type;// "cancel_type": "",
	// private String source;//"source": "10",
	// private String channel;//"channel": "CG",
	// private String number;//"number": 2,
	// List<BLDComment> goods = new ArrayList<BLDComment>();
	// *****************end*********我的采购单列表--未付款*********end******************

	// *****************start*********我的采购单-订单详情*********start******************
	// private String goods_name;//"goods_name": "面膜1",
	// private String goods_id;//"goods_id": "1",
	// private String goods_price;//"goods_price": "300",
	// private String goods_number;//"goods_number": "1",
	// private String goods_cover;//"goods_cover":
	// "http://img11.360buyimg.com/n7/jfs/t2794/118/556412839/103412/9e3234b5/5719f6d8Nc2c0b395.jpg",
	// private String goods_standard;//"goods_standard": "蓝色100ML",
	// private String goods_type;//"goods_type": "0",
	// private String goods_attr_id;//"goods_attr_id": "1",
	// private String channel;//"channel": "CG"
	// *****************end*********我的采购单-订单详情*********end******************

	// *****************start*********我的--我的订单*********start******************
	// private String id;// "id": "312",
	// private String member_id;// "member_id": "345",
	// private String seller_id;//"seller_id": "1",
	// private String order_sn;//"order_sn": "A2016063010053525961",
	// private String order_status;//"order_status": "110",
	// private String seller_order_sn;// "seller_order_sn":
	// "S2016063010053102379",
	// private String goods_price;//"goods_price": "900",
	// private String postage;// "postage": "10",
	// private String seller_name;// "seller_name": "kobe",
	List<BLDComment> detail = new ArrayList<BLDComment>();// "detail":

	// private String number;//"number": 1
	// *****************end*********我的--我的订单*********end******************

	// *****************start*********我的--我的订单--订单详情*********start**************
	// private String goods_name;//"goods_name": "大兔兔的自营商品2016-1",
	// private String goods_id;//"goods_id": "408",
	// private String goods_price;//"goods_price": "300",
	// private String goods_number;//"goods_number": "3",
	// private String goods_cover;//"goods_cover":
	// "http://fs.v-town.cc/test0516.jpg",
	// private String goods_standard;//"goods_standard": "蓝色100ML",
	// private String goods_type;//"goods_type": "0",
	// private String goods_attr_id;//"goods_attr_id": "2088",
	// private String channel;//"channel": "PT"

	// **************** ******我的--我的订单--未付款------订单详情******* ****************
	 
	// private String order_sn;//": "A2016070457509799720",
	// private String order_status;//": "10",
	private String order_total_price;// ": "3",
	private String postage_money;// ": "0",
	private String creat_time;// ": "1467618120",
	// private String member_id;//": "410",
	private String blance;
	private String username;// "username": "大兔兔零购",
	// private String mobile;//"mobile": "18310998310",
	// private String province;//"province": "海南省",
	// private String city;// "city": "海口市",
	private String area;// "area": "滨海区",
	private List<BLDComment> son_order = new ArrayList<BLDComment>();
	// private String address;//"address": "门牌号",
	// private String address;//"order_sn": "A2016070697971015236",
	// private String creat_time;//"creat_time": "1467770522",
	// private String postage_money;//"postage_money": "0",
	// private String source;//"source": "20",
	// private String order_total_price;//"order_total_price": "156",
	// private String money_paid;//"money_paid": "156",

	// *****************start*********退款原因*********start**************
	// private String id;// "id": 2,
	private String text;// "text": "购买数量错误"

	// ****************end ******退款原因******* end****************

	// **********************我的活动数据二级的*******************************************
	private String category_name;

	// private List<BLDComment>goods;
	// *****************start*********提现支付宝账户*********start**************
	// private String name;//"name": "崔臣霞",
	// private String alipay;//"alipay": "wang@124.com"
	// ****************end ******提现支付宝账户******* end****************

	public String getUUID() {
		return UUID;
	}

	public String getRefund() {
		return refund;
	}

	public void setRefund(String refund) {
		this.refund = refund;
	}

	public String getGoods_money() {
		return goods_money;
	}

	public void setGoods_money(String goods_money) {
		this.goods_money = goods_money;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getUsed_balance() {
		return used_balance;
	}

	public void setUsed_balance(String used_balance) {
		this.used_balance = used_balance;
	}

	public String getPay_sn() {
		return pay_sn;
	}

	public void setPay_sn(String pay_sn) {
		this.pay_sn = pay_sn;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getPay_status() {
		return pay_status;
	}

	public void setPay_status(String pay_status) {
		this.pay_status = pay_status;
	}

	public String getUsed_coupons() {
		return used_coupons;
	}

	public void setUsed_coupons(String used_coupons) {
		this.used_coupons = used_coupons;
	}

	public String getUsed_coupons_name() {
		return used_coupons_name;
	}

	public void setUsed_coupons_name(String used_coupons_name) {
		this.used_coupons_name = used_coupons_name;
	}

	public String getUsed_coupons_money() {
		return used_coupons_money;
	}

	public void setUsed_coupons_money(String used_coupons_money) {
		this.used_coupons_money = used_coupons_money;
	}

	public String getIs_delete() {
		return is_delete;
	}

	public void setIs_delete(String is_delete) {
		this.is_delete = is_delete;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getCancel_type() {
		return cancel_type;
	}

	public void setCancel_type(String cancel_type) {
		this.cancel_type = cancel_type;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public List<BLDComment> getDetail() {
		return detail;
	}

	public void setDetail(List<BLDComment> detail) {
		this.detail = detail;
	}

	public String getSeller_order_sn() {
		return seller_order_sn;
	}

	public void setSeller_order_sn(String seller_order_sn) {
		this.seller_order_sn = seller_order_sn;
	}

	public String getCoupons_price() {
		return coupons_price;
	}

	public void setCoupons_price(String coupons_price) {
		this.coupons_price = coupons_price;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getFtime() {
		return ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	public String getSend_from() {
		return send_from;
	}

	public void setSend_from(String send_from) {
		this.send_from = send_from;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getStatus() {
		return status;
	}

	public String getAdd_good_attrs_value_1() {
		return add_good_attrs_value_1;
	}

	public void setAdd_good_attrs_value_1(String add_good_attrs_value_1) {
		this.add_good_attrs_value_1 = add_good_attrs_value_1;
	}

	public String getAdd_good_attrs_value_2() {
		return add_good_attrs_value_2;
	}

	public void setAdd_good_attrs_value_2(String add_good_attrs_value_2) {
		this.add_good_attrs_value_2 = add_good_attrs_value_2;
	}

	public String getAdd_good_attrs_value_3() {
		return add_good_attrs_value_3;
	}

	public void setAdd_good_attrs_value_3(String add_good_attrs_value_3) {
		this.add_good_attrs_value_3 = add_good_attrs_value_3;
	}

	public String getAdd_good_attrs_value_4() {
		return add_good_attrs_value_4;
	}

	public void setAdd_good_attrs_value_4(String add_good_attrs_value_4) {
		this.add_good_attrs_value_4 = add_good_attrs_value_4;
	}

	public String getAdd_good_id() {
		return add_good_id;
	}

	public void setAdd_good_id(String add_good_id) {
		this.add_good_id = add_good_id;
	}

	public String getAdd_good_cate_name() {
		return add_good_cate_name;
	}

	public void setAdd_good_cate_name(String add_good_cate_name) {
		this.add_good_cate_name = add_good_cate_name;
	}

	public String getAdd_good_attrs_id_1() {
		return add_good_attrs_id_1;
	}

	public void setAdd_good_attrs_id_1(String add_good_attrs_id_1) {
		this.add_good_attrs_id_1 = add_good_attrs_id_1;
	}

	public String getAdd_good_attrs_id_2() {
		return add_good_attrs_id_2;
	}

	public void setAdd_good_attrs_id_2(String add_good_attrs_id_2) {
		this.add_good_attrs_id_2 = add_good_attrs_id_2;
	}

	public String getAdd_good_attrs_name_1() {
		return add_good_attrs_name_1;
	}

	public void setAdd_good_attrs_name_1(String add_good_attrs_name_1) {
		this.add_good_attrs_name_1 = add_good_attrs_name_1;
	}

	public String getAdd_good_attrs_name_2() {
		return add_good_attrs_name_2;
	}

	public void setAdd_good_attrs_name_2(String add_good_attrs_name_2) {
		this.add_good_attrs_name_2 = add_good_attrs_name_2;
	}

	public List<BLDComment> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<BLDComment> attrs) {
		this.attrs = attrs;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public BLDComment getAttr_map() {
		return attr_map;
	}

	public void setAttr_map(BLDComment attr_map) {
		this.attr_map = attr_map;
	}

	public String getAddress_id() {
		return address_id;
	}

	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAlipay() {
		return alipay;
	}

	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}

	public String getCard_number() {
		return card_number;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

	public String getBank_id() {
		return bank_id;
	}

	public void setBank_id(String bank_id) {
		this.bank_id = bank_id;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
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

	public String getGoods_attr_id() {
		return goods_attr_id;
	}

	public void setGoods_attr_id(String goods_attr_id) {
		this.goods_attr_id = goods_attr_id;
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

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getCancel_time() {
		return cancel_time;
	}

	public void setCancel_time(String cancel_time) {
		this.cancel_time = cancel_time;
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

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIs_agent() {
		return is_agent;
	}

	public void setIs_agent(String is_agent) {
		this.is_agent = is_agent;
	}

	public String getSell_price() {
		return sell_price;
	}

	public void setSell_price(String sell_price) {
		this.sell_price = sell_price;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public String getCate_name() {
		return cate_name;
	}

	public void setCate_name(String cate_name) {
		this.cate_name = cate_name;
	}

	public List<BLComment> getA() {
		return a;
	}

	public void setA(List<BLComment> a) {
		this.a = a;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}

	public String getIs_type() {
		return is_type;
	}

	public void setIs_type(String is_type) {
		this.is_type = is_type;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
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

	public String getSendnumber() {
		return sendnumber;
	}

	public void setSendnumber(String sendnumber) {
		this.sendnumber = sendnumber;
	}

	public BLDComment getSellerinfo() {
		return sellerinfo;
	}

	public void setSellerinfo(BLDComment sellerinfo) {
		this.sellerinfo = sellerinfo;
	}

	public BLDComment getGoodinfo() {
		return goodinfo;
	}

	public void setGoodinfo(BLDComment goodinfo) {
		this.goodinfo = goodinfo;
	}

	public List<String> getImgarr() {
		return imgarr;
	}

	public void setImgarr(List<String> imgarr) {
		this.imgarr = imgarr;
	}

	public String getGoods_sid() {
		return goods_sid;
	}

	public void setGoods_sid(String goods_sid) {
		this.goods_sid = goods_sid;
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

	public String getAgent_price() {
		return agent_price;
	}

	public void setAgent_price(String agent_price) {
		this.agent_price = agent_price;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getVstore() {
		return vstore;
	}

	public void setVstore(String vstore) {
		this.vstore = vstore;
	}

	public String getCommission() {
		return commission;
	}

	public void setCommission(String commission) {
		this.commission = commission;
	}

	public String getStarting() {
		return starting;
	}

	public void setStarting(String starting) {
		this.starting = starting;
	}

	public String getIs_whole() {
		return is_whole;
	}

	public void setIs_whole(String is_whole) {
		this.is_whole = is_whole;
	}

	public Bitmap getPicBitmap() {
		return picBitmap;
	}

	public void setPicBitmap(Bitmap picBitmap) {
		this.picBitmap = picBitmap;
	}

	public File getPicFile() {
		return picFile;
	}

	public void setPicFile(File picFile) {
		this.picFile = picFile;
	}

	public String getPicFileStr() {
		return picFileStr;
	}

	public void setPicFileStr(String picFileStr) {
		this.picFileStr = picFileStr;
	}

	public byte[] getFileByte() {
		return FileByte;
	}

	public void setFileByte(byte[] fileByte) {
		FileByte = fileByte;
	}

	public List<BLDComment> getList() {
		return list;
	}

	public void setList(List<BLDComment> list) {
		this.list = list;
	}

	public String getPre_url() {
		return pre_url;
	}

	public void setPre_url(String pre_url) {
		this.pre_url = pre_url;
	}

	public String getGoods_price() {
		return goods_price;
	}

	public void setGoods_price(String goods_price) {
		this.goods_price = goods_price;
	}

	public List<BLDComment> getGoods() {
		return goods;
	}

	public void setGoods(List<BLDComment> goods) {
		this.goods = goods;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
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

	public String getGoods_type() {
		return goods_type;
	}

	public void setGoods_type(String goods_type) {
		this.goods_type = goods_type;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getSale_status() {
		return sale_status;
	}

	public void setSale_status(String sale_status) {
		this.sale_status = sale_status;
	}

	public BLComment() {
		super();
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public String getIs_edit() {
		return is_edit;
	}

	public void setIs_edit(String is_edit) {
		this.is_edit = is_edit;
	}

	public String getMax_price() {
		return max_price;
	}

	public void setMax_price(String max_price) {
		this.max_price = max_price;
	}

	public boolean isIsCanSelct() {
		return IsCanSelct;
	}

	public void setIsCanSelct(boolean isCanSelct) {
		IsCanSelct = isCanSelct;
	}

	public boolean isIsBrandDetaiLsSelect() {
		return IsBrandDetaiLsSelect;
	}

	public void setIsBrandDetaiLsSelect(boolean isBrandDetaiLsSelect) {
		IsBrandDetaiLsSelect = isBrandDetaiLsSelect;
	}

	public String getIs_brand() {
		return is_brand;
	}

	public void setIs_brand(String is_brand) {
		this.is_brand = is_brand;
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

	public String getAgency_time() {
		return agency_time;
	}

	public void setAgency_time(String agency_time) {
		this.agency_time = agency_time;
	}

	public String getCoupons_id() {
		return coupons_id;
	}

	public void setCoupons_id(String coupons_id) {
		this.coupons_id = coupons_id;
	}

	public String getTaken_time() {
		return taken_time;
	}

	public void setTaken_time(String taken_time) {
		this.taken_time = taken_time;
	}

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

	public String getUsed_endtime() {
		return used_endtime;
	}

	public void setUsed_endtime(String used_endtime) {
		this.used_endtime = used_endtime;
	}

	public String getUsed_starttime() {
		return used_starttime;
	}

	public void setUsed_starttime(String used_starttime) {
		this.used_starttime = used_starttime;
	}

	public String getUsed_day() {
		return used_day;
	}

	public void setUsed_day(String used_day) {
		this.used_day = used_day;
	}

	public String getName() {
		return name;
	}
	

	public String getPurchase_price() {
		return purchase_price;
	}

	public void setPurchase_price(String purchase_price) {
		this.purchase_price = purchase_price;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<String> getLevels() {
		return levels;
	}

	public void setLevels(List<String> levels) {
		this.levels = levels;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getAll_money() {
		return all_money;
	}

	public void setAll_money(String all_money) {
		this.all_money = all_money;
	}

	public String getAll_goods_num() {
		return all_goods_num;
	}

	public void setAll_goods_num(String all_goods_num) {
		this.all_goods_num = all_goods_num;
	}

	public List<BLDComment> getStore_list() {
		return store_list;
	}

	public void setStore_list(List<BLDComment> store_list) {
		this.store_list = store_list;
	}

	public List<BLComment> getAttr() {
		return attr;
	}

	public void setAttr(List<BLComment> attr) {
		this.attr = attr;
	}

	public String getSource_type() {
		return source_type;
	}

	public void setSource_type(String source_type) {
		this.source_type = source_type;
	}

	public BNew getMessage_info() {
		return message_info;
	}

	public void setMessage_info(BNew message_info) {
		this.message_info = message_info;
	}

	public List<BLDComment> getSon_order() {
		return son_order;
	}

	public void setSon_order(List<BLDComment> son_order) {
		this.son_order = son_order;
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

	public String getBlance() {
		return blance;
	}

	public void setBlance(String blance) {
		this.blance = blance;
	}

	public String getPic_path() {
		return pic_path;
	}

	public void setPic_path(String pic_path) {
		this.pic_path = pic_path;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getAdvert_type() {
		return advert_type;
	}

	public void setAdvert_type(int advert_type) {
		this.advert_type = advert_type;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getRecommend_position() {
		return recommend_position;
	}

	public void setRecommend_position(String recommend_position) {
		this.recommend_position = recommend_position;
	}

	public String getSource_id() {
		return source_id;
	}

	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getAdvert_type_str() {
		return advert_type_str;
	}

	public void setAdvert_type_str(String advert_type_str) {
		this.advert_type_str = advert_type_str;
	}

	public String getGoods_url() {
		return goods_url;
	}

	public void setGoods_url(String goods_url) {
		this.goods_url = goods_url;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getTeamCounter() {
		return teamCounter;
	}

	public void setTeamCounter(String teamCounter) {
		this.teamCounter = teamCounter;
	}

	// public List<BLDComment> getListss() {
	// return listss;
	// }
	//
	// public void setListss(List<BLDComment> listss) {
	// this.listss = listss;
	// }

	public String getOrig_price() {
		return orig_price;
	}

	public void setOrig_price(String orig_price) {
		this.orig_price = orig_price;
	}
}
