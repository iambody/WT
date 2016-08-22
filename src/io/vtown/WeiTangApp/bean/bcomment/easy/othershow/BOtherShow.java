package io.vtown.WeiTangApp.bean.bcomment.easy.othershow;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcomment.easy.BShopSousou;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-27 下午2:39:52
 * 
 */
public class BOtherShow extends BBase {
	// 我的show和其他人的show
	private List<BLOtherShowOut> showinfo = new ArrayList<BLOtherShowOut>();
	private BShopSousou sellerinfo = new BShopSousou();
	public List<BLOtherShowOut> getShowinfo() {
		return showinfo;
	}
	public void setShowinfo(List<BLOtherShowOut> showinfo) {
		this.showinfo = showinfo;
	}
	public BShopSousou getSellerinfo() {
		return sellerinfo;
	}
	public void setSellerinfo(BShopSousou sellerinfo) {
		this.sellerinfo = sellerinfo;
	}

}
