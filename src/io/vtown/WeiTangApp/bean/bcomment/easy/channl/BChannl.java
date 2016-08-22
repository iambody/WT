package io.vtown.WeiTangApp.bean.bcomment.easy.channl;

import io.vtown.WeiTangApp.R.string;
import io.vtown.WeiTangApp.bean.BBase;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-28 上午11:21:31
 * 
 */
public class BChannl extends BBase {
	private String junior;// =6
	private String superior;// =2
	private String weekSales;// =1

	public String invite_code_img = "http://www.lagou.com/i/image/M00/23/47/CgqKkVcXIKSADKFqAAAkAWuqPx8396.jpg";// 分享邀请二维码文案
	public String invite_code_title = "微糖邀请码";// 分享邀请二维码的文案
	public String invite_code_content = "微糖通过其专业的技术团队，力求为每一个商家打造强大公信力，为每个消费者创造购物的便捷和良好的用户体验";

	public String getJunior() {
		return junior;
	}

	public void setJunior(String junior) {
		this.junior = junior;
	}

	public String getSuperior() {
		return superior;
	}

	public void setSuperior(String superior) {
		this.superior = superior;
	}

	public String getWeekSales() {
		return weekSales;
	}

	public void setWeekSales(String weekSales) {
		this.weekSales = weekSales;
	}

	public String getInvite_code_img() {
		return invite_code_img;
	}

	public void setInvite_code_img(String invite_code_img) {
		this.invite_code_img = invite_code_img;
	}

	public String getInvite_code_title() {
		return invite_code_title;
	}

	public void setInvite_code_title(String invite_code_title) {
		this.invite_code_title = invite_code_title;
	}

	public String getInvite_code_content() {
		return invite_code_content;
	}

	public void setInvite_code_content(String invite_code_content) {
		this.invite_code_content = invite_code_content;
	}

}
