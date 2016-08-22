package io.vtown.WeiTangApp.bean.bcomment.easy.shopbus;

import io.vtown.WeiTangApp.bean.BBase;

import java.util.List;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-27 下午4:56:56
 * 
 */
public class BShopBus extends BBase {
	// 购物车
	private List<BLShopBusOut> PT;
	private List<BLShopBusOut> CG;
	public List<BLShopBusOut> getPT() {
		return PT;
	}
	public void setPT(List<BLShopBusOut> pT) {
		PT = pT;
	}
	public List<BLShopBusOut> getCG() {
		return CG;
	}
	public void setCG(List<BLShopBusOut> cG) {
		CG = cG;
	}

	 

}
