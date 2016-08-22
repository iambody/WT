package io.vtown.WeiTangApp.bean.bcomment.easy.shopbus;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-27 下午4:57:39
 * 
 */
public class BLShopBusOut extends BBase {
	private String is_brand;// =1
	private List<BLShopBusIn> list = new ArrayList<BLShopBusIn>();
	private String seller_id;// =619
	private String seller_name;// =ccx测试品牌商店铺
	private boolean IsCanSelct = false;
	public String getIs_brand() {
		return is_brand;
	}
	public void setIs_brand(String is_brand) {
		this.is_brand = is_brand;
	}
	public List<BLShopBusIn> getList() {
		return list;
	}
	public void setList(List<BLShopBusIn> list) {
		this.list = list;
	}
	public String getSeller_id() {
		return seller_id;
	}
	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}
	public String getSeller_name() {
		return seller_name;
	}
	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}
	public boolean isIsCanSelct() {
		return IsCanSelct;
	}
	public void setIsCanSelct(boolean isCanSelct) {
		IsCanSelct = isCanSelct;
	}

	
}
