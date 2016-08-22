package io.vtown.WeiTangApp.bean.bcomment.easy.goodsort;

import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcache.BHome;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.shopbrand.BLBrandGood;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-27 下午7:56:56
 * 
 */
public class BGoodSort extends BBase {
	private List<BLGoodSort> banner;
	private List<BLBrandGood> goods;

	private List<BLGoodSort> subcategory;

	public List<BLGoodSort> getBanner() {
		return banner;
	}

	public void setBanner(List<BLGoodSort> banner) {
		this.banner = banner;
	}

	public List<BLBrandGood> getGoods() {
		return goods;
	}

	public void setGoods(List<BLBrandGood> goods) {
		this.goods = goods;
	}

	public List<BLGoodSort> getSubcategory() {
		return subcategory;
	}

	public void setSubcategory(List<BLGoodSort> subcategory) {
		this.subcategory = subcategory;
	}

}
