package io.vtown.WeiTangApp.bean.bcomment.easy;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.shop.BShopBase;
import io.vtown.WeiTangApp.bean.bcomment.easy.shop.BShopCatory;
import io.vtown.WeiTangApp.bean.bcomment.easy.shop.BShopGoods;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-27 上午10:23:01
 * @author 店铺的数据
 */
public class BShop extends BBase {
	//自营店铺
	private BShopBase base = new BShopBase();
	private List<BShopGoods> agents = new ArrayList<BShopGoods>();
	private List<BShopCatory> categorys = new ArrayList<BShopCatory>();
	private List<BShopGoods> agent = new ArrayList<BShopGoods>();
	private List<BShopGoods> diy = new ArrayList<BShopGoods>();
	//品牌店铺

	public BShopBase getBase() {
		return base;
	}

	public void setBase(BShopBase base) {
		this.base = base;
	}

	public List<BShopGoods> getAgents() {
		return agents;
	}

	public void setAgents(List<BShopGoods> agents) {
		this.agents = agents;
	}

	public List<BShopCatory> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<BShopCatory> categorys) {
		this.categorys = categorys;
	}

	public List<BShopGoods> getAgent() {
		return agent;
	}

	public void setAgent(List<BShopGoods> agent) {
		this.agent = agent;
	}

	public List<BShopGoods> getDiy() {
		return diy;
	}

	public void setDiy(List<BShopGoods> diy) {
		this.diy = diy;
	}
}
