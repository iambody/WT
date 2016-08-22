package io.vtown.WeiTangApp.bean.bcache;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-7 下午2:53:53 店铺界面需要进行的Sp保存
 */
public class BShop extends BBase {
	public String subCounter;// ":"1",
	public String teamCounter;// ":0,
	public String todayVisitor;// ":0,
	public String todayIncome;// ":0,
	public String todaySales;// ":0,
	public String totalIncome;// ":0

	public String id;// ": "2",
	public String is_brand;// ": "0",
	public String seller_name;// ": "Shanghai ERKE",
	public String member_id;// ": "1",
	public String cover;// ": "http://fs.v-town.cc/zzzzzzzzzzzzzzzz",
	public String status;// ": "20",
	public String attention;// ": "5",
	public String avatar;// ": "http://fs.v-town.cc/head01.gif",
	public String intro;// ": "wwwwww"
	public String seller_no;
	public String seller_url;

	public String getSubCounter() {
		return subCounter;
	}

	public void setSubCounter(String subCounter) {
		this.subCounter = subCounter;
	}

	public String getTeamCounter() {
		return teamCounter;
	}

	public void setTeamCounter(String teamCounter) {
		this.teamCounter = teamCounter;
	}

	public String getTodayVisitor() {
		return todayVisitor;
	}

	public String getSeller_url() {
		return seller_url;
	}

	public void setSeller_url(String seller_url) {
		this.seller_url = seller_url;
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIs_brand() {
		return is_brand;
	}

	public void setIs_brand(String is_brand) {
		this.is_brand = is_brand;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public String getAttention() {
		return attention;
	}

	public void setAttention(String attention) {
		this.attention = attention;
	}

	public String getSeller_no() {
		return seller_no;
	}

	public void setSeller_no(String seller_no) {
		this.seller_no = seller_no;
	}

}
