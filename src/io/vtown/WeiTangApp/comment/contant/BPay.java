package io.vtown.WeiTangApp.comment.contant;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-10 下午4:32:10
 *  
 */
public class BPay extends BBase {
	private String appid="wxf104565ec7418036";
	private String partner_id="wx201605101501378d726a41590954769379";
	private String appkey="ILzIIN9pBibe2H5XvYpVxWD6GjMpHlwc";
	private String appsecret="7396a8106b5e27e02d462fc1d0283e94";
	private String partner_key;
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getPartner_id() {
		return partner_id;
	}
	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}
	public String getAppkey() {
		return appkey;
	}
	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}
	public String getAppsecret() {
		return appsecret;
	}
	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}
	public String getPartner_key() {
		return partner_key;
	}
	public void setPartner_key(String partner_key) {
		this.partner_key = partner_key;
	}
	
	
}
