package io.vtown.WeiTangApp.bean.bcache;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.db.DBHelper;
import io.vtown.WeiTangApp.db.annotation.Column;
import io.vtown.WeiTangApp.db.annotation.TableName;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-26 下午2:49:33 TODO品牌ID需要添加数据库 目前测试数据阶段
 */
@TableName(DBHelper.HomeTab)
public class BHome extends BBase {
	@Column(DBHelper.HomeTab_Title)
	private String title = "";
	@Column(DBHelper.HomeTab_Pic_path)
	private String pic_path = "";
	@Column(DBHelper.HomeTab_Url)
	private String url = ""; //
	@Column(DBHelper.HomeTab_Category_id)
	private String category_id = ""; // 是banner单独有的
	@Column(DBHelper.HomeTab_Recommend_position)
	private String recommend_position = ""; //
	@Column(DBHelper.HomeTab_Advert_type)
	private String advert_type = "0";// 3=>BANNER;;2=>goods;;1=>agency;;默认是0（弹出框的列表）
	@Column(DBHelper.HomeTab_Source_id)
	private String source_id;//
	@Column(DBHelper.HomeTab_Price)
	private String price;
	// 弹出框需要的
	@Column(DBHelper.HomeTab_Id)
	private String id;// ": "1",
	@Column(DBHelper.HomeTab_Cate_name)
	private String cate_name;// ": "化妆"
	private String is_brand;// 1是品牌店铺，0是普通
	private String cover;// =http://fs.v-town.cc//storage/emulated/0/WtAndroid/1466155115974.jpg
	// 最新的首页的广告列表里面的数据
	// private String id;// :"4",
	// private String title;// :"广告4",
	// private String pic_path;// :"http://fs.v-town.cc/advert1468404720",
	// private String url;//
	// "http://dev.vt.m.v-town.cn/goods/index/index?goods_id=706",
	// private int advert_type;// :"2",advert_type 类型 1H5首页，2商品详情页，3店铺详情页,4活动详情页
	// private String category_id;// "0",
	// private String price;// :"0",
	// private String recommend_position;// :"1",
	private String status;// :"1",
	private String update_time;// :"0",
	private String create_time;// :"1468404720",
	// private String source_id;// :"706",
	private String client;// :"10",
	// private String is_delete;//:"0",
	private String sort;// :"5",
	private String advert_type_str;// :"商品详情页"

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public BHome() {
		super();
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	/**
	 * @param id
	 * @param cate_name
	 */
	public BHome(String id, String cate_name) {
		super();
		this.id = id;
		this.cate_name = cate_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	public String getRecommend_position() {
		return recommend_position;
	}

	public void setRecommend_position(String recommend_position) {
		this.recommend_position = recommend_position;
	}

	public String getAdvert_type() {
		return advert_type;
	}

	public void setAdvert_type(String advert_type) {
		this.advert_type = advert_type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCate_name() {
		return cate_name;
	}

	public void setCate_name(String cate_name) {
		this.cate_name = cate_name;
	}

	public String getSource_id() {
		return source_id;
	}

	public void setSource_id(String source_id) {
		this.source_id = source_id;
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

	public String getIs_brand() {
		return is_brand;
	}

	public void setIs_brand(String is_brand) {
		this.is_brand = is_brand;
	}

}
