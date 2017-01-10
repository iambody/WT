package io.vtown.WeiTangApp.bean.bcomment.easy.gooddetail;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-8-16 上午11:33:44
 * 
 */
public class BDataGood extends BBase {
	private String attr_id;// ": "17759398959",
	private String id;// ": 677,
	private String vstore;// ": 0,
	private String sell_price;// ": 3990,
	private String agent_price;// ": 3990,
	private String store;// ": 999,
	private String Odlestore;
	private String goods_sid;// ": 515,
	private String attr_name;
	private String fee;
	private String score;
	private String icon;
	private BLDataGood attr_map=new BLDataGood();

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getAttr_id() {
		return attr_id;
	}
	public void setAttr_id(String attr_id) {
		this.attr_id = attr_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVstore() {
		return vstore;
	}
	public void setVstore(String vstore) {
		this.vstore = vstore;
	}
	public String getSell_price() {
		return sell_price;
	}
	public void setSell_price(String sell_price) {
		this.sell_price = sell_price;
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
	public String getGoods_sid() {
		return goods_sid;
	}
	public void setGoods_sid(String goods_sid) {
		this.goods_sid = goods_sid;
	}
	public String getAttr_name() {
		return attr_name;
	}
	public void setAttr_name(String attr_name) {
		this.attr_name = attr_name;
	}
	public BLDataGood getAttr_map() {
		return attr_map;
	}
	public void setAttr_map(BLDataGood attr_map) {
		this.attr_map = attr_map;
	}
	public String getOdlestore() {
		return Odlestore;
	}
	public void setOdlestore(String odlestore) {
		Odlestore = odlestore;
	}


	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
}
