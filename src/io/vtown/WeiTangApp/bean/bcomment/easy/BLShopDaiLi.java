package io.vtown.WeiTangApp.bean.bcomment.easy;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-27 下午2:20:16
 * 
 */
public class BLShopDaiLi extends BBase {
	private String agency_time;// =2016-07-14
	private String attention;// =2
	private String avatar;// =http://fs.v-town.cc/avatar_702471467868678849.jpg
	private String cover;// =http://fs.v-town.cc/avatar_14678674600000000000000368511559.jpg
	private String credential;// =http://fs.v-town.cc/TW201.jpg
	private String id;// =484
	private String is_brand;// =1
	private String level;// =1
	private String seller_name;// =卡卡西品牌旗舰店
	//横向滑动 是否已经选择
	private boolean IsSelectBrand=false;


	public boolean isSelectBrand() {
		return IsSelectBrand;
	}

	public void setSelectBrand(boolean selectBrand) {
		IsSelectBrand = selectBrand;
	}

	public String getAgency_time() {
		return agency_time;
	}
	public void setAgency_time(String agency_time) {
		this.agency_time = agency_time;
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
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getCredential() {
		return credential;
	}
	public void setCredential(String credential) {
		this.credential = credential;
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
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getSeller_name() {
		return seller_name;
	}
	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}
	public BLShopDaiLi() {
		super();
		
	}
	public BLShopDaiLi(String id, String seller_name) {
		super();
		this.id = id;
		this.seller_name = seller_name;
	}

	public BLShopDaiLi(String id, String seller_name, boolean isSelectBrand) {
		this.id = id;
		this.seller_name = seller_name;
		IsSelectBrand = isSelectBrand;
	}
}
