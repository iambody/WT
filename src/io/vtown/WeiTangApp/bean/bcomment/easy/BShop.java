package io.vtown.WeiTangApp.bean.bcomment.easy;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-27 上午10:23:01
 * @author 店铺的数据
 */
public class BShop extends BBase {
	//自营店铺
	private BLDComment base = new BLDComment();
	private List<BLComment> agents = new ArrayList<BLComment>();
	private List<BLComment> categorys = new ArrayList<BLComment>();
	private List<BLComment> agent = new ArrayList<BLComment>();
	private List<BLComment> diy = new ArrayList<BLComment>();
	//品牌店铺
	
	
	public BLDComment getBase() {
		return base;
	}

	public void setBase(BLDComment base) {
		this.base = base;
	}

	public List<BLComment> getAgents() {
		return agents;
	}

	public void setAgents(List<BLComment> agents) {
		this.agents = agents;
	}

	public List<BLComment> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<BLComment> categorys) {
		this.categorys = categorys;
	}

	public List<BLComment> getAgent() {
		return agent;
	}

	public void setAgent(List<BLComment> agent) {
		this.agent = agent;
	}

	public List<BLComment> getDiy() {
		return diy;
	}

	public void setDiy(List<BLComment> diy) {
		this.diy = diy;
	}

}
