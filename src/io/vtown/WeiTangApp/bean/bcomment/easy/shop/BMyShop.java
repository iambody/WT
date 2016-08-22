package io.vtown.WeiTangApp.bean.bcomment.easy.shop;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcache.BShop;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-8-9 下午5:44:07
 * 
 */
public class BMyShop extends BBase {
	private BShop base = new BShop();

	private String todayVisitor;// ": 20,
	private String todayIncome;// ": 0,
	private String teamCounter;// ": 0,
	private String todaySales;// ": 0,
	private String totalIncome;// ": 0,
	private String subCounter;// ": 0
	public BShop getBase() {
		return base;
	}
	public void setBase(BShop base) {
		this.base = base;
	}
	public String getTodayVisitor() {
		return todayVisitor;
	}
	public void setTodayVisitor(String todayVisitor) {
		this.todayVisitor = todayVisitor;
	}
	public String getTodayIncome() {
		return todayIncome;
	}
	public void setTodayIncome(String todayIncome) {
		this.todayIncome = todayIncome;
	}
	public String getTeamCounter() {
		return teamCounter;
	}
	public void setTeamCounter(String teamCounter) {
		this.teamCounter = teamCounter;
	}
	public String getTodaySales() {
		return todaySales;
	}
	public void setTodaySales(String todaySales) {
		this.todaySales = todaySales;
	}
	public String getTotalIncome() {
		return totalIncome;
	}
	public void setTotalIncome(String totalIncome) {
		this.totalIncome = totalIncome;
	}
	public String getSubCounter() {
		return subCounter;
	}
	public void setSubCounter(String subCounter) {
		this.subCounter = subCounter;
	}
	
	
	
	
	
	
}
