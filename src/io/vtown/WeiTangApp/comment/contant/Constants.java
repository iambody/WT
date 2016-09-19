package io.vtown.WeiTangApp.comment.contant;

import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.UpComparator;
import io.vtown.WeiTangApp.comment.util.encrypt.RSAUtils;
import io.vtown.WeiTangApp.comment.util.encrypt.StringEncrypt;
import io.vtown.WeiTangApp.comment.zxing.decode.Intents.Encode;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author Yihuihua
 *
 */
/**
 * @author Yihuihua
 * 
 */
public class Constants {
	/**
	 * 是否开启debug模式
	 */
	public final static boolean Debug = true;
	/**
	 * 在tab主界面需要跳转到的时候 界面时候需要跳转到注册或者登录界面时候@author datutu
	 */
	public static final String TabMainKill = "tabkill";
	public static final String TableMenu1 = "0";
	public static final String TableMenu2 = "1";
	public static final String TableMenu3 = "4";
	public static final String TableMenu4 = "2";
	public static final String TableMenu5 = "3";

	/**
	 * 推送类型
	 */

	public static final int SOURCE_TYPE_MESSAGE = 1; // 源类型：消息
	public static final int SOURCE_TYPE_PAY = 2; // 源类型：支付
	public static final int SOURCE_TYPE_ORDER = 3; // 源类型：订单
	public static final int ACTION_PT_ORDER = 1;// 操作类型：普通下单
	public static final int ACTION_CG_ORDER = 2;// 操作类型：采购下单
	public static final int ACTION_TO_PAY = 3;// 操作类型：付款
	public static final int ACTION_CG_SEND = 4;// 操作类型：采购订单发货
	public static final int ACTION_PT_SEND = 5;// 操作类型：普通订单发货
	public static final int ACTION_CG_REFUND = 6;// 操作类型：采购订单同意退款
	public static final int ACTION_PT_REFUND = 7;// 操作类型：普通订单同意退款
	public static final int ACTION_CG_UNREFUND = 8;// 操作类型：采购订单拒绝退款
	public static final int ACTION_PT_UNREFUND = 9;// 操作类型：采购订单拒绝退款
	public static final int ACTION_PT_MODIFY_PRICE = 16;// 操作类型：修改价格

	public static final String Api_Version = "2.0.0";

	/**
	 * 下边微测试的test的id 上线需删除
	 */

	public static final String invite_code = "";

	public static final String extend = "1";
	/**
	 * IM时候需要进行的拼接
	 */
	public static final String ImHost = "v-town";

	public static final String SucessToError = "successtoerror";

	/**
	 * 所有上传图片拍照图片必须要放在本文件夹里面
	 */
	public static String PicHost = Environment.getExternalStorageDirectory()
			+ "/WtAndroid/";
	/**
	 * 所有网络图片的缓存
	 */
	public static String PicCach = Environment.getExternalStorageDirectory()
			+ "/WtAndroid/cach/";
	/**
	 * 所有生成图片的缓存
	 */
	public static String PicQrCach = Environment.getExternalStorageDirectory()
			+ "/WtAndroid/cach/qr/";

	/**
	 * 用户协议
	 */
	public static String AppDeal_Url = "http://www.v-town.cc/xieyi.html"; //

	/**
	 * 功能介绍
	 */
	public static String AppGongnneg_Url ="http://www.v-town.cc/guide.html";//"http://dev.vt.static.v-town.cn/v2/front/m/guide.html"; //

	/**
	 * 开发环境 的host
	 */
	  public static String Host = "http://dev.vt.api.v-town.cn";
	/**
	 * 测试环境的host 测试环境 需要切换key 需要同时切换SignKey这个字段!!!!!!!!!!!!!!!!!!!!
	 */
	// public static String Host = "https://testvtapi.v-town.cc";
	public static String WxHost="https://static.v-town.cc/";

	/**
	 * 生产环境
	 */
 	//public static String Host = "https://api.v-town.cc";

	// 接口配置参数*******************************************************************
	/**
	 * 首页的URL
	 */
	public static String HomeUrl = Host + "/v1/seller/index/index";

	/**
	 * app升级
	 */
	public static String UpData = Host + "/v1/api/api/get-version";

	/**
	 * 商品搜索
	 */
	public static String SouGood = Host + "/v1/search/search/tag";
	// public static String SouGood1 = Host + "/v1/search/search/goodinfo";
	/**
	 * 商品搜索结果item点击进入的列表
	 */
	public static String SouGoodinf = Host + "/v1/search/search/goodinfo";
	/**
	 * 商品分页加载 分页时候的每页的size
	 */
	public static String SouGoodinf_size = "10";
	/**
	 * 默认每页的个数
	 */
	public static int PageSize = 10;
	public static int PageSize1 = 100;
	public static int PageSize3 = 2000;
	/**
	 * 店铺搜索接口 Get
	 */
	public static String SouShop = Host + "/v1/search/search/shop";
	/**
	 * 热门接口 (搜索界面)GET @author datutu
	 */
	public static String SouHotGood = Host + "/v1/search/search/hot";
	/**
	 * 获取热门搜索条目
	 */
	public static String SouGoodSize = "6";
	/**
	 * 首页点击品牌进去的品牌详情页 Get
	 */
	public static String Brand_Inf = Host + "/v1/seller/seller/agent";
	// 自营店铺 &&品牌店铺合并******************
	public static String Shop_Inf = Host + "/v1/seller/seller/detail";
	/**
	 * 品牌页筛选商品的列表(所有分类列表用同一个)
	 */
	public static String Select_Ls = Host + "/v1/goods/goods/list";
	/**
	 * 首页POP点击品牌进去的品牌列表
	 */
	public static String Brand_List = Host + "/v1/seller/index/agents";
	/**
	 * 产品分类的url
	 */
	public static String Good_Sort = Host + "/v1/seller/index/subcategory";

	/**
	 * 获取一级Show列表
	 */
	public static String Show_ls = Host + "/v1/show/show/list";
	/**
	 * 店铺详情（Show点击头像进去）
	 */
	public static String ShopDetail_inf = Host
			+ "/v1/seller/seller/agent-index";// "/v1/seller/seller/detail";
	/**
	 * 店铺首页 Tab页面
	 */
	// public static String ShopMainHome = Host + "/v1/seller/seller/index";

	/**
	 * 购物车列表 Tab页面
	 */
	public static String ShopBus_Ls = Host + "/v1/shopcart/cart/list";
	/**
	 * 购物车删除商品
	 */
	public static String ShopBus_Delete = Host + "/v1/shopcart/cart/remove";
	/**
	 * 购物车修改商品的个数
	 */
	public static String ShopBus_GoodAlter = Host + "/v1/shopcart/cart/set-num";
	/**
	 * 商品详情的数据
	 */
	public static String GoodDetail = Host + "/v1/goods/goods/detail";

	/**
	 * 修改商品规格
	 */
	public static String Modify_Content = Host + "/v1/goods/goods/gattrs";

	/**
	 * 订单生成实时查询
	 */
	public static String Order_Instant_Info_Select = Host
			+ "/v1/order/order/realtime-order";

	/**
	 * 商品收藏
	 */
	public static String Good_Attention = Host + "/v1/member/collect/goods";

	/**
	 * 取消商品收藏
	 */
	public static String Good_Attention_Delete = Host
			+ "/v1/member/collect/delete-goods";

	/**
	 * 用户的资金记录
	 */
	public static String ZiJinJiLu = Host + "/v1/pay/capital/list";

	/**
	 * 卖家订单和管理列表
	 */
	public static String SELLER_ORDER_MANAGER = Host + "/v1/order/seller/list";

	/**
	 * 同意退款
	 */
	public static String Agree_TuiKuan = Host + "/v1/order/seller/agree-refund";

	/**
	 * 拒绝退款
	 */
	public static String UnAgree_TuiKuan = Host
			+ "/v1/order/seller/reject-refund";

	/**
	 * 订单详情
	 */
	public static String Order_Detail = Host + "/v1/order/seller/detail";

	/**
	 * 发货
	 */
	public static String Send_Out = Host + "/v1/order/seller/send-goods";

	/**
	 * 查看物流
	 */
	public static String Look_Express_Message = Host
			+ "/v1/order/seller/express";

	/**
	 * 延期收货
	 */
	public static String Delay_Shou_Huo = Host
			+ "/v1/order/member/delayreceive";

	/**
	 * 确认收货
	 */
	public static String Confirm_Order = Host + "/v1/order/order/confirm-order";

	/**
	 * 同意退款
	 */
	public static String Agree_Refund = Host + "/v1/order/seller/agree-refund";

	/**
	 * 拒绝退款
	 */
	public static String Reject_Refund = Host
			+ "/v1/order/seller/reject-refund";

	/**
	 * 修改地址
	 */
	public static String Order_Manage_Modify_Address = Host
			+ "/v1/order/seller/edit-address";

	/**
	 * 修改总价
	 */
	public static String Modify_Total_Price = Host
			+ "/v1/order/seller/update-total";

	/**
	 * 快递列表
	 */
	public static String Logistics_List = Host + "/v1/api/api/express";

	/**
	 * 采购单列表
	 */
	public static String Purchase_List = Host + "/v1/order/order/list";

	/**
	 * 采购单列表--未付款
	 */
	public static String Purchase_No_Pay_List = Host
			+ "/v1/order/order/nopay-list";

	/**
	 * 采购单列表--未付款详情
	 */
	public static String Purchase_No_Pay_Detail = Host
			+ "/v1/order/order/nopayment-detail";

	/**
	 * 采购单详情
	 */
	public static String Purchase_Detail = Host
			+ "/v1/order/order/order-detail";

	/**
	 * 采购单详情--取消订单
	 */
	public static String Purchase_Cancel_Order = Host
			+ "/v1/order/order/cancel";

	/**
	 * 订单支付
	 */
	public static String Purchase_Order_Topay = Host
			+ "/v1/order/order/pay-order";

	/**
	 * 采购单详情--提醒发货
	 */
	public static String Purchase_Remind_Send_Out = Host
			+ "/v1/order/order/warn-send";

	/**
	 * 商品管理
	 */
	public static String GOODS_MANAGE_LIST = Host + "/v1/goods/goods/list";
	/**
	 * 商品管理中=> 点击编辑后需要的权限判断
	 */
	public static String GOODS_Manger_EditClick = "/v1/goods/goods/check-modify";
	/**
	 * 修改店铺头像
	 */
	public static String MODIFI_SHOP_ICON = Host + "/v1/seller/seller/avatar";

	/**
	 * 修改店铺封面
	 */
	public static String MODIFI_SHOP_COVER = Host + "/v1/seller/seller/cover";

	/**
	 * 修改店铺昵称
	 */
	public static String MODIFI_SHOP_NICKNAME = Host
			+ "/v1/seller/seller/nickname";

	/**
	 * 修改店铺简介
	 */
	public static String MODIFI_SHOP_INTRODUCE = Host
			+ "/v1/seller/seller/intro";

	/**
	 * 修改店铺简介
	 */
	public static String ORDER_DETAIL_MESSAGE = Host
			+ "/v1/order/seller/detail";

	/**
	 * 商品关注
	 */
	public static String CENTER_GOOD_COLLECT = Host
			+ "/v1/member/collect/goods-list";

	/**
	 * 我的---我的订单
	 */
	public static String Center_My_Order = Host + "/v1/order/member/list";

	/**
	 * 我的-->我的订单-->订单详情
	 */
	public static String Center_My_Order_Detail = Host
			+ "/v1/order/member/detail";

	/**
	 * 我的-->我的订单-->订单详情--取消订单
	 */
	public static String Center_My_Order_Cancel = Host
			+ "/v1/order/member/cancel-order";

	/**
	 * 我的-->我的订单-->未付款订单详情--去付款
	 */
	public static String Center_My_Order_Go_Pay = Host
			+ "/v1/order/member/topay";
	/**
	 * 我的-->我的订单-->订单详情--提醒发货
	 */
	public static String Center_My_Order_Remind_Send_Out = Host
			+ "/v1/order/member/remind";

	/**
	 * 我的-->我的订单-->订单详情--确认收货
	 */
	public static String Center_My_Order_Confirm_Order = Host
			+ "/v1/order/member/confirm";

	/**
	 * 我的-->我的订单-->订单详情--延期收货
	 */
	public static String Center_My_Order_Delayreceive = Host
			+ "/v1/order/member/delayreceive";

	/**
	 * 我的-->我的订单-->订单详情--退款申请
	 */
	public static String Center_My_Order_Apply_Refund = Host
			+ "/v1/order/member/apply-refund";

	/**
	 * 我的采购单-->订单详情--退款申请
	 */
	public static String Purchase_Apply_Refund = Host
			+ "/v1/order/order/apply-refund";

	/**
	 * 退款申请 -- 退款原因
	 */
	public static String Refund_Reason = Host + "/v1/order/member/refundreason";

	/**
	 * 店铺关注
	 */
	public static String CENTER_SHOP_COLLECT = Host
			+ "/v1/member/collect/seller-list";

	/**
	 * 浏览记录
	 */
	public static String CENTER_BROWSE_RECORD = Host
			+ "/v1/member/look/look-list";

	/**
	 * 我的卡卷列表
	 */
	public static String CENTER_MY_COUPONS = Host + "/v1/coupons/coupons/list";

	/**
	 * 渠道管理页面
	 */
	public static String SHOP_CHANNEL_MANAGE = Host + "/v1/seller/seller/sales";

	/**
	 * 渠道管理-->我的下级页面
	 */
	public static String SHOP_CHANNEL_MY_JUNIOR = Host
			+ "/v1/seller/seller/lower";

	/**
	 * 渠道管理-->我的上级页面
	 */
	public static String SHOP_CHANNEL_MY_SUPERIOR = Host
			+ "/v1/seller/seller/upper";

	/**
	 * 渠道管理-->邀请记录
	 */
	public static String SHOP_CHANNEL_INVITE_RECORD = Host
			+ "/v1/seller/agent/invitations";

	/**
	 * 渠道发展下级的品牌列表
	 */
	public static String SHOP_CHANNEL_XiaJi_BrandLs = Host
			+ "/v1/seller/agent/agents";

	/**
	 * 渠道管理下=》发展下级=》提交信息生成二维码
	 */
	public static String SHOP_CHANNEL_XiaJi_CreaterCode = Host
			+ "/v1/seller/agent/qrcode";
	/**
	 * 渠道管理=》扫码=》获取信息=》选择信息后提交成为下级按钮
	 */
	public static String SHOP_CHANNEL_XiaJi_commint = Host
			+ "/v1/seller/agent/invite";
	/**
	 * 渠道管理=》 邀请上级
	 */
	// public static String SHOP_CHANNEL_yaoqing_shangji = Host
	// + "/v1/seller/agent/invite";
	/**
	 * 渠道管理=》邀请上级
	 */
	public static String SHOP_CHANNEL_yaoqing_shangjiSup = Host
			+ "/v1/seller/agent/superior";

	/**
	 * 个人设置--地址管理列表
	 */
	public static String SET_ADDRESS_MANAGE = Host + "/v1/member/address/list";

	/**
	 * 店铺品牌代理列表
	 */
	public static String SHOP_BRAND_AGENT = Host
			+ "/v1/seller/agent/agent-list";

	/**
	 * 店铺品牌代理列表-->查看证书
	 */
	public static String SHOP_BRAND_AGENT_LOOK_CERTIFICATE = Host
			+ "/v1/seller/agent/credential";

	/**
	 * 店铺品牌代理列表-->申请升级
	 */
	public static String SHOP_BRAND_AGENT_APPLY_LEVEL = Host
			+ "/v1/seller/agent/upgrade";

	/**
	 * 查询帐户信息
	 */
	public static String Select_Account_Message = Host + "/v1/pay/account/get";

	/**
	 * 银行卡列表
	 */
	public static String BANK_LIST = Host + "/v1/api/api/bank";

	/**
	 * 我的银行卡列表
	 */
	public static String MY_BANK_LIST = Host + "/v1/pay/bank/list";

	/**
	 * 修改银行卡
	 */
	public static String Modify_Bank_Card = Host + "/v1/pay/bank/update";

	/**
	 * 删除银行卡
	 */
	public static String Remove_Bank_Card = Host + "/v1/pay/bank/delete";

	/**
	 * 我的支付宝列表
	 */
	public static String MY_ALIPAY_LIST = Host + "/v1/pay/account/alipay-list";

	/**
	 * 获取提现信息
	 */
	public static String Get_Tixian_Message = Host
			+ "/v1/pay/account/alipay-bank-list";

	/**
	 * 更新支付宝
	 */
	public static String Modify_Alipay = Host + "/v1/pay/account/update-pay";

	/**
	 * 获取生成订单的列表接口（结算页面的数据）
	 */
	public static String JieSuan_Ui_Url = Host + "/v1/shopcart/cart/affirm";

	/**
	 * 设置默认地址
	 */
	public static String set_Default_Address = Host
			+ "/v1/member/member/default-address";

	/**
	 * 删除收货人地址
	 */
	public static String set_Delete_Address = Host
			+ "/v1/member/address/delete";

	/**
	 * 默认地址详情
	 */
	public static String Default_Address_Detail = Host
			+ "/v1/member/member/default-list";

	/**
	 * 修改收货地址
	 */
	public static String Modify_Address = Host + "/v1/member/address/update";

	/**
	 * 添加收货地址
	 */
	public static String Add_New_Address = Host + "/v1/member/address/insert";

	/**
	 * 校验身份信息
	 */
	public static String Check_User = Host + "/v1/pay/account/check-user";

	/**
	 * 重置交易密码
	 */
	public static String Reset_Psw = Host + "/v1/pay/account/set-password";

	/**
	 * 校验验证码
	 */
	public static String Check_Code = Host + "/v1/message/member/check";

	/**
	 * 获取验证码
	 */
	public static String SMS = Host + "/v1/message/member/send";

	/**
	 * 支付接口
	 */
	public static String AliPay = Host + "/v1/pay/pay/add";

	/**
	 * 获取七牛Token的接口
	 */
	public static String QiNiuToken = Host + "/v1/api/api/qiniu-token";

	// **************************登录相关的url**start********************
	/**
	 * 微信登录
	 */
	public static String Login_Wx_Login = Host
			+ "/v1/member/passport/wechat-login";
	/**
	 * 校验验证码
	 */
	public static String SmS_Code_Check = Host + "/v1/message/member/check";
	/**
	 * 微信绑定手机号
	 */
	public static String Login_Wx_Phone_Bind = Host
			+ "/v1/member/passport/wechat-bind";
	/**
	 * 手机号登录
	 */
	public static String Login_Phone = Host + "/v1/member/passport/login";
	/**
	 * 添加绑定邀请码邀请码
	 */
	public static String Login_Bind_Invite_Code = Host
			+ "/v1/member/member/parent-bind";
	/**
	 * 申请提现
	 */
	public static String Apply_Withdraw = Host + "/v1/pay/fetchmoney/add";

	/**
	 * 银行卡管理之添加银行卡
	 */
	public static String Bank_Manage_Add_Card = Host + "/v1/pay/bank/add";

	/**
	 * 获取我的个人show
	 */
	public static String Center_My_Show_Data = Host + "/v1/show/show/myshow";

	/**
	 * 添加购物车
	 */
	public static String Add_to_Good_Bus = Host + "/v1/shopcart/cart/add";

	/**
	 * 商品上架
	 */
	public static String Add_to_online = Host + "/v1/goods/goods/online";

	/**
	 * 自营商品代卖
	 */
	public static String Goods_Desell = Host + "/v1/goods/goods/desell";

	/**
	 * 添加商品之所有分类列表
	 */
	public static String Add_Good_Categoty = Host
			+ "/v1/category/category/list";

	/**
	 * 实名认证
	 */
	public static String Login_Real_RenZheng = Host + "/v1/pay/account/add";

	// **************************登录相关的url***end*******************

	/**
	 * 我的店铺
	 */
	public static String MyShop = Host + "/v1/seller/seller/myshop";

	/**
	 * 提交订单 OderBeingjiemian
	 */
	public static String OderBeing = Host + "/v1/order/order/add-order";
	// 订单支付
	public static String Oder_Pay = Host + "/v1/order/order/pay-order";
	/**
	 * 用户消息列表
	 */
	public static String My_New_ls = Host + "/v1/member/message/get-list";
	/**
	 * 用户消息Item列表
	 */
	public static String My_Item_New_ls = Host
			+ "/v1/member/message/get-list-by-type";
	/**
	 * 清空消息
	 */
	public static String My_Item_New_Delet = Host + "/v1/member/message/clear";

	// 商品管理中各种request操作************************
	// 在售中=》删除
	public static String Good_Manger_Delete = Host + "/v1/goods/goods/remove";
	// 在售中=》下架
	public static String Good_Manger_XiaJia = Host + "/v1/goods/goods/offline";
	// 已经垃圾箱=》恢复
	public static String Good_Manger_HuiFu = Host + "/v1/goods/goods/recover";
	// 已经垃圾箱=》彻底删除
	public static String Good_Manger_Delete_Chedi = Host
			+ "/v1/goods/goods/clear";
	// 已经下架=》彻底删除
	public static String Good_Manger_ShangJia = Host + "/v1/goods/goods/online";
	/**
	 * 添加宝贝**
	 */
	public static String Shop_AddGoods = Host + "/v1/goods/goods/publish";

	/**
	 * 根据商品的id获取商品的规格
	 */
	public static String Shop_Attrs = Host + "/v1/goods/goods/attrs";

	/**
	 * 自营商品判断是否可以代买
	 */
	public static String IsDaiMai_ZiYing = Host + "/v1/goods/goods/decheck";

	/**
	 * 品牌商品
	 */
	public static String IsDaiMai_Brand = Host + "/v1/goods/goods/dacheck";
	/**
	 * 商品一键代理
	 */
	public static String GoodsDaiLi = Host + "/v1/goods/goods/dasell";
	/**
	 * 银联支付订单号绑定支付token/v1/webpay/pay/bind
	 */
	public static String YinLianPay = Host + "/v1/webpay/pay/bind";
	/**
	 * 品牌店铺里面进行 品牌申请
	 */
	public static String BrandApplay = Host + "/v1/seller/agent/apply";
	/**
	 * 转发分享Show
	 */
	public static String GoodsShow_ZhuanFa = Host + "/v1/show/show/append";
	/**
	 * 商品的Show
	 */
	public static String GoodsShow = Host + "/v1/show/show/goodshow";
	/**
	 * 删除（隐藏我的show）
	 */
	public static String MyShowDelete = Host + "/v1/show/show/hideshow";

	/**
	 * 关注店铺
	 */
	public static String GuanZhuShop = Host + "/v1/member/collect/seller";
	/**
	 * 取消店铺关注
	 */
	public static String CancleGuanZhuShop = Host
			+ "/v1/member/collect/delete-seller";
	/**
	 * 发展下级生成二维码
	 */
	public static String CreateQRCodeInf = Host + "/v1/seller/agent/qrcode";
	/**
	 * center=》未付款
	 */
	public static String CenterOderWeiFu = Host + "/v1/order/member/nopayment";

	/**
	 * center=》未付款--订单详情
	 */
	public static String Center_Order_No_Pay_Detail = Host
			+ "/v1/order/member/nopaydetail";

	/**
	 * 销售统计
	 */
	public static String SellStaatistic_Line = Host
			+ "/v1/seller/count/countinfo";
	/**
	 * 删除浏览商品
	 */
	public static String Delete_LiuLan_goods = Host
			+ "/v1/member/look/delete-look";
	/**
	 * 活动专区的数据
	 */
	public static String HuoDongZhuanQu = Host + "/v1/advert/advert/activity";
	/**
	 * 编辑商品
	 */
	public static String GoodAlter = Host + "/v1/goods/goods/modify";
	/**
	 * 通过扫描发展下级的二维码进行的code进行获取列表
	 */
	public static String SaoMiaoCodeToList = Host + "/v1/seller/agent/query";

	/**
	 * 商品管理的品牌商的可售商品列表
	 */

	public static String GoodCanSellList = Host + "/v1/goods/goods/sales-list";
	/**
	 * 获取分享邀请码的文案
	 */
	public static String ShareInItViter = Host + "/v1/api/api/invite";
	/**
	 * 根据消息类型进行删除========》消息
	 */
	public static String NewDeletByType = Host
			+ "/v1/member/message/delete-by-type";
	/**
	 * 删除消息
	 */
	public static String NewItemDeletByType = Host
			+ "/v1/member/message/delete";

	/**
	 * 获取收货地址(省)
	 */
	public static String Get_Province = Host
			+ "/v1/api/api/get-province";

	/**
	 * 获取收货地址（市）
	 */
	public static String Get_City = Host
			+ "/v1/api/api/get-city";

	/**
	 * 获取地址（区）
	 */
	public static String Get_Area = Host
			+ "/v1/api/api/get-area";
/**
 * 店铺内进行搜索
 */
	public static String ShopGoodSou=Host+"/v1/goods/goods/search";
	// 其他参数*******************************************************************
	// 其他参数*******************************************************************
	// 其他参数*******************************************************************
	// 其他参数*******************************************************************
	/**
	 * 签名时候的key  开发环境
	 */
	  public static String SignKey = "GriE93gIGp$5bDjQ4rc20FzxWGghTIau";
	/**
	 * 测试环境
	 */
	// public static String SignKey = "3vh4xN3@G02ixajB*^@PHkxfz88AKk%O";
	/**
	 * 正式环境的key
	 */
 //	public static String SignKey = "hkf%t5SMv1HtrVS!Y%B!NPNS!!0cWgy";

	// 微糖小助手sheng'chuan'j生产
 // public static String WtHelper = "v-town000111222";
 //开发环境
	 public static String WtHelper = "v-town000111222";
	/**
	 * 微信支付的key
	 */
	public static String WxPayKey = "wxd5b2375c327ca5f7";

	/**
	 * 七牛上传时候的key
	 */
	public static String QiNiuHost = "http://fs.v-town.cc";

	/**
	 * IM的密码
	 */
	public static String ImPasd   ="123456";

	/**
	 * 网络交互特殊code码//////被挤下来
	 */
	public static int NetCodeExit = 900;

	/**
	 * 判断HTTP返回的code值 TODO目前只有200和非200两种 后续会陆续添加其他code标识值
	 */
	public static boolean StatuseOk(int code) {
		if (200 == code)
			return true;
		return false;
	}

	/**
	 * SHA256方法
	 */
	public static String SHA(String key) {
		return StringEncrypt.Encrypt(key, "");
	}

	/**
	 * 获取时间戳
	 */
	public static String TimeStamp() {
		return System.currentTimeMillis() + "";
	}

	/**
	 * 入参签名
	 */
	public static HashMap<String, String> Sign(HashMap<String, String> map) {
		HashMap<String, String> da = new HashMap<String, String>();
		map.put("timestamp", Constants.TimeStamp());
		da = map;
		List<BComment> mBlComments = new ArrayList<BComment>();
		for (Iterator iter = da.entrySet().iterator(); iter.hasNext();) {
			Map.Entry element = (Map.Entry) iter.next();
			String strKey = String.valueOf(element.getKey());
			String strObj = String.valueOf(element.getValue());
			mBlComments.add(new BComment(strKey, strObj));
		}
		UpComparator m = new UpComparator();
		Collections.sort(mBlComments, m);
		String K = "";
		for (BComment h : mBlComments) {
			K = K + h.getId() + h.getTitle();
		}

		K = K + SignKey;

		// for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
		// Map.Entry element = (Map.Entry) iter.next();
		// String strKey = String.valueOf(element.getKey());
		// String strObj = String.valueOf(element.getValue());
		// map.put(strKey, URLEncoder.encode(strObj));
		// }

		if (map.containsKey("keyword")) {
			map.put("keyword", URLEncoder.encode(map.get("keyword")));
		}
		if (map.containsKey("title")) {
			map.put("title", URLEncoder.encode(map.get("title")));
		}
		map.put("sign", Constants.SHA(K));
		return map;

	}

	/**
	 * 上传图片时候不需要 encode
	 *
	 * @param map
	 * @return
	 */
	public static HashMap<String, String> Sign2(HashMap<String, String> map) {
		HashMap<String, String> da = new HashMap<String, String>();
		map.put("timestamp", Constants.TimeStamp());
		da = map;
		List<BComment> mBlComments = new ArrayList<BComment>();
		for (Iterator iter = da.entrySet().iterator(); iter.hasNext();) {
			Map.Entry element = (Map.Entry) iter.next();
			String strKey = String.valueOf(element.getKey());
			String strObj = String.valueOf(element.getValue());
			mBlComments.add(new BComment(strKey, strObj));
		}
		UpComparator m = new UpComparator();
		Collections.sort(mBlComments, m);
		String K = "";
		for (BComment h : mBlComments) {
			K = K + h.getId() + h.getTitle();
		}

		K = K + SignKey;

		// for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
		// Map.Entry element = (Map.Entry) iter.next();
		// String strKey = String.valueOf(element.getKey());
		// String strObj = String.valueOf(element.getValue());
		// map.put(strKey, URLEncoder.encode(strObj));
		// }

		map.put("sign", Constants.SHA(K));
		return map;

	}

	/**
	 * RSA加密的方法
	 */
	public static String RSA(String str, Context CT) {
		try {
			return RSAUtils.GetRsA(CT, str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}

	/**
	 * HashMap转换成JsonObject
	 */
	public static JSONObject SignToJson(HashMap<String, String> map) {

		JSONObject object = new JSONObject();

		for (Iterator iter = map.entrySet().iterator(); iter.hasNext();) {
			Map.Entry element = (Map.Entry) iter.next();
			String strKey = String.valueOf(element.getKey());
			String strObj = String.valueOf(element.getValue());
			try {
				object.put(strKey, strObj);
			} catch (Exception e) {

			}
		}

		return object;

	}

	/**
	 * 判断 用户是否安装微信客户端
	 */
	public static boolean isWeixinAvilible(Context context) {
		final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equals("com.tencent.mm")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取设备唯一号
	 */
	public static String GetPhoneId(Context context) {
		TelephonyManager TelephonyMgr = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		String szImei = TelephonyMgr.getDeviceId();
		return szImei;
	}

	/**
	 * deviceID的组成为：渠道标志+识别符来源标志+hash后的终端识别符 渠道标志为： 1，andriod（a） 识别符来源标志： 1，
	 * wifi mac地址（wifi）； 2， IMEI（imei）； 3， 序列号（sn）； 4，
	 * id：随机码。若前面的都取不到时，则随机生成一个随机码，需要缓存。
	 *
	 * @param context
	 * @return
	 */
	// public static String getDeviceId(Context context) {
	// TelephonyManager TelephonyMgr = (TelephonyManager) context
	// .getSystemService(context.TELEPHONY_SERVICE);
	// String szImei = TelephonyMgr.getDeviceId();
	//
	// //
	// // //序列号（sn）
	// // String sn = tm.getSimSerialNumber();
	// // if(!StrUtils.isEmpty(sn)){
	// // deviceId.append("sn");
	// // deviceId.append(sn);
	// // return deviceId.toString();
	// // }
	//
	// // //如果上面都没有， 则生成一个id：随机码
	// // String uuid = getUUID(context);
	// // if(!StrUtils.isEmpty(uuid)){
	// // deviceId.append("id");
	// // deviceId.append(uuid);
	// // return deviceId.toString();
	// // }
	// // } catch (Exception e) {
	// // e.printStackTrace();
	// // deviceId.append("id").append(getUUID(context));
	// // }
	//
	// // return deviceId.toString();
	//
	// }
	public static void applyFont(final Context context, final View root,
			final String fontName) {
		try {
			if (root instanceof ViewGroup) {
				ViewGroup viewGroup = (ViewGroup) root;
				for (int i = 0; i < viewGroup.getChildCount(); i++)
					applyFont(context, viewGroup.getChildAt(i), fontName);
			} else if (root instanceof TextView)
				((TextView) root).setTypeface(Typeface.createFromAsset(
						context.getAssets(), fontName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 版本名
	public static String getVersionName(Context context) {
		return getPackageInfo(context).versionName;
	}

	// 版本号
	public static int getVersionCode(Context context) {
		return getPackageInfo(context).versionCode;
	}

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo pi = null;

		try {
			PackageManager pm = context.getPackageManager();
			pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);

			return pi;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pi;
	}
}
