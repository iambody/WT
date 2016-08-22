package io.vtown.WeiTangApp.bean.bcomment.easy.wallet;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-7-27 下午3:36:12
 *  
 */
public class BLAPropertyList extends BBase {
	private List<BLAPropertyDetail> list = new ArrayList<BLAPropertyDetail>();
	private String month;//month=2016-07
	
	
	public List<BLAPropertyDetail> getList() {
		return list;
	}
	public void setList(List<BLAPropertyDetail> list) {
		this.list = list;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	
	
}
