package io.vtown.WeiTangApp.bean.bcomment.easy;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-27 上午11:35:22
 * @author 商品管理
 */
public class BLGoodManger extends BBase {
	private String cover;// =http://fs.v-town.cc/pic_993111468994306823.jpg
	private String goods_sid;// =868
	private String goods_url;// =http://dev.vt.m.v-town.cn/goods/index/index?goods_id=948&type=goods
	private String id;// =948
	private String is_agent;// =0
	private String is_edit;// =0
	private String max_price;// =0
	private String sale_status;// =20
	private String sales;// =0
	private String sell_price;// =1500
	private String seller_id;// =487
	private String store;// =0
	private String title;//
	private String vstore;// =0;//
	
	//品牌商  ==>列表  
	private int status; //0表示未上架可以上架；；1
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getGoods_sid() {
		return goods_sid;
	}
	public void setGoods_sid(String goods_sid) {
		this.goods_sid = goods_sid;
	}
	public String getGoods_url() {
		return goods_url;
	}
	public void setGoods_url(String goods_url) {
		this.goods_url = goods_url;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIs_agent() {
		return is_agent;
	}
	public void setIs_agent(String is_agent) {
		this.is_agent = is_agent;
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
	public String getSale_status() {
		return sale_status;
	}
	public void setSale_status(String sale_status) {
		this.sale_status = sale_status;
	}
	public String getSales() {
		return sales;
	}
	public void setSales(String sales) {
		this.sales = sales;
	}
	public String getSell_price() {
		return sell_price;
	}
	public void setSell_price(String sell_price) {
		this.sell_price = sell_price;
	}
	public String getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
	public String getStore() {
		return store;
	}
	public void setStore(String store) {
		this.store = store;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getVstore() {
		return vstore;
	}
	public void setVstore(String vstore) {
		this.vstore = vstore;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	
	
}
