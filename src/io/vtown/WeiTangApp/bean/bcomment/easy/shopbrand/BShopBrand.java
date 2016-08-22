package io.vtown.WeiTangApp.bean.bcomment.easy.shopbrand;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-27 下午6:44:07
 * 
 */
public class BShopBrand extends BBase {

	private List<String> roll  ;
	private List<BLComment> categorys  ;
	private List<BLBrandGood> agent  ;
	private String is_agented;
	private BDComment base;

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

	 

	public String getIs_agented() {
		return is_agented;
	}

	public void setIs_agented(String is_agented) {
		this.is_agented = is_agented;
	}

	public BDComment getBase() {
		return base;
	}

	public void setBase(BDComment base) {
		this.base = base;
	}

	public List<BLBrandGood> getAgent() {
		return agent;
	}

	public void setAgent(List<BLBrandGood> agent) {
		this.agent = agent;
	}

}
