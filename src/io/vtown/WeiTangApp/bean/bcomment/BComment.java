package io.vtown.WeiTangApp.bean.bcomment;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcache.BHome;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-12 下午5:14:47 注意 这是一个公用javabean 考虑到后续会迭代的因素
 *          每增加一个bean需要操作人进行详细的标注（包括那个界面需要时间等等）
 */
public class BComment extends BBase {
	/**
	 * 在ABase中定义一个http请求数据的标记
	 */
	protected int HttpResultTage;
	/**
	 * 在ABase中定义一个http请求数据的返回string
	 */
	protected String HttpResultStr;
	/**
	 * 加载模式INITIALIZE = 0;// 初次进入时候 REFRESHING = 1;// 刷新 LOADMOREING = 2;// 加载更多
	 */
	protected int HttpLoadType;

	/**
	 * 所有inten传递数据时需要进行一个参数的传递的string 单独一个参数的构造函数
	 */
	private String CommentKey;
	// TODO 以后所有的Extras()传递所需封装的javabean都在这里面抽离出来单独组建新的可复用的bean

	// 商品搜索店铺搜索intent到commentlist需要的公用bean************************************
	private String id;
	private String title;

	// 首页的四个列表************************************
	private List<BHome> banner;
	private List<BHome> goods;
	private List<BHome> agency;// 品牌列表
	// 新的Home的两个Key*****************************************************
	private List<BHome> cate;// 分类
	private List<BHome> advert;// 广告列表

	// 界面WEBview使用的两个参数bean
	private String Url;
	// private String title;

	// 首页的品牌图片跳转到的品牌页的二级data的bean***************************************
	private String is_agented;
	private BDComment base;
	private List<String> roll = new ArrayList<String>();
	private List<BLComment> categorys = new ArrayList<BLComment>();
	private List<BLComment> agent = new ArrayList<BLComment>();
	// 商品分类界面的三个列表（上边的banner中间的gradview
	// 下边的listview）************************************
	// private List<BHome> banner = new ArrayList<BHome>();
	private List<BLComment> subcategory = new ArrayList<BLComment>();// 分类的gradview数据
	// private List<BHome> goods = new ArrayList<BHome>();
	// 商品show列表的item的 数据
	// *********************生成订单界面的获取订单接口OderBean界面******************
	private String order_total_price;// ":"3768",

	public String getIs_used_coupons() {
		return is_used_coupons;
	}

	public void setIs_used_coupons(String is_used_coupons) {
		this.is_used_coupons = is_used_coupons;
	}

	private String money_paid;// ":"768",
	private String is_used_coupons;// Y=>可选//N不可选
	private BLDComment coupons;
	private BLDComment address = new BLDComment();
	private List<BLComment> list = new ArrayList<BLComment>();
	// 跳转到OtherShow界面里面需要带的数据*************************
	private String cover;//
	private String avatar;//
	// private String id;//
	private String seller_name;//
	private String is_brand;

	// 我的show和其他人的show
	private List<BLComment> showinfo = new ArrayList<BLComment>();
	private BDComment sellerinfo = new BDComment();
	private int cart_num;
	// 购物车
	private List<BLComment> PT;
	private List<BLComment> CG;

	// 个人中心 我的show里面
	public BComment() {
		super();
	}

	// /**
	// * ABase中封装返回http请求的封装 bean@author datutu
	// */
	// public BComment(String httpResultStr, int httpResultTage) {
	// super();
	// HttpResultTage = httpResultTage;
	// HttpResultStr = httpResultStr;
	//
	// }

	public int getCart_num() {
		return cart_num;
	}

	public void setCart_num(int cart_num) {
		this.cart_num = cart_num;
	}

	public int getHttpResultTage() {
		return HttpResultTage;
	}

	public String getIs_brand() {
		return is_brand;
	}

	public void setIs_brand(String is_brand) {
		this.is_brand = is_brand;
	}

	/**
	 * ABase中封装返回http请求的封装 bean@author datutu
	 * 
	 * @param httpResultStr
	 *            Data里面的数据
	 * @param httpResultTage
	 *            请求http请求的标识
	 * @param httpLoadType
	 *            请求时候的加载方式 INITIALIZE = 0初次进入时候 ;;REFRESHING=> 1刷新;;
	 *            LOADMOREING => 2加载更多
	 */
	public BComment(String httpResultStr, int httpResultTage, int httpLoadType) {
		super();
		HttpResultTage = httpResultTage;
		HttpResultStr = httpResultStr;
		HttpLoadType = httpLoadType;
	}

	public void setHttpResultTage(int httpResultTage) {
		HttpResultTage = httpResultTage;
	}

	public String getHttpResultStr() {
		return HttpResultStr;
	}

	public void setHttpResultStr(String httpResultStr) {
		HttpResultStr = httpResultStr;
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

	// 商品搜索店铺搜索intent到commentlist需要的公用bean&&跳转到webview时候
	// id当成url属性&&入参签名时候&&首页跳转到品牌详情需要的bean
	// key=>valuse会用到
	public BComment(String id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	public List<BHome> getBanner() {
		return banner;
	}

	public void setBanner(List<BHome> banner) {
		this.banner = banner;
	}

	public List<BHome> getGoods() {
		return goods;
	}

	public void setGoods(List<BHome> goods) {
		this.goods = goods;
	}

	public List<BHome> getAgency() {
		return agency;
	}

	public void setAgency(List<BHome> agency) {
		this.agency = agency;
	}

	public List<BHome> getCate() {
		return cate;
	}

	public void setCate(List<BHome> cate) {
		this.cate = cate;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public BDComment getBase() {
		return base;
	}

	public void setBase(BDComment base) {
		this.base = base;
	}

	public String getIs_agented() {
		return is_agented;
	}

	public void setIs_agented(String is_agented) {
		this.is_agented = is_agented;
	}

	public List<String> getRoll() {
		return roll;
	}

	public void setRoll(List<String> roll) {
		this.roll = roll;
	}

	public List<BLComment> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<BLComment> categorys) {
		this.categorys = categorys;
	}

	public List<BLComment> getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(List<BLComment> subcategory) {
		this.subcategory = subcategory;
	}

	public int getHttpLoadType() {
		return HttpLoadType;
	}

	public void setHttpLoadType(int httpLoadType) {
		HttpLoadType = httpLoadType;
	}

	/**
	 * 传递一个数据时候的构造函数
	 * 
	 * @param commentKey
	 */
	public BComment(String commentKey) {
		super();
		CommentKey = commentKey;
	}

	public String getCommentKey() {
		return CommentKey;
	}

	public void setCommentKey(String commentKey) {
		CommentKey = commentKey;
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

	public BLDComment getCoupons() {
		return coupons;
	}

	public void setCoupons(BLDComment coupons) {
		this.coupons = coupons;
	}

	public BLDComment getAddress() {
		return address;
	}

	public void setAddress(BLDComment address) {
		this.address = address;
	}

	public List<BLComment> getList() {
		return list;
	}

	public void setList(List<BLComment> list) {
		this.list = list;
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

	public BComment(String id, String cover, String avatar, String seller_name,
			String is_brand) {
		super();
		this.id = id;
		this.cover = cover;
		this.avatar = avatar;
		this.seller_name = seller_name;
		this.is_brand = is_brand;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public List<BLComment> getShowinfo() {
		return showinfo;
	}

	public void setShowinfo(List<BLComment> showinfo) {
		this.showinfo = showinfo;
	}

	public BDComment getSellerinfo() {
		return sellerinfo;
	}

	public void setSellerinfo(BDComment sellerinfo) {
		this.sellerinfo = sellerinfo;
	}

	public List<BLComment> getPT() {
		return PT;
	}

	public void setPT(List<BLComment> pT) {
		PT = pT;
	}

	public List<BLComment> getCG() {
		return CG;
	}

	public void setCG(List<BLComment> cG) {
		CG = cG;
	}

	public List<BLComment> getAgent() {
		return agent;
	}

	public void setAgent(List<BLComment> agent) {
		this.agent = agent;
	}

	public List<BHome> getAdvert() {
		return advert;
	}

	public void setAdvert(List<BHome> advert) {
		this.advert = advert;
	}

}
