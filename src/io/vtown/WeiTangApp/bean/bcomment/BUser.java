package io.vtown.WeiTangApp.bean.bcomment;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-11 下午2:41:27
 * 
 */
public class BUser extends BBase {
	public String id;// ": "229",
	public String phone;// ": "13466581919",
	public String head_img;// ": "",
	public String nickname;// ": "",
	public String pw_type;// ": "0",
	public String weixin_open_id;// ": "ofF0Ut8IJYW4NBAWPl83Vekg8Rjs",
	public String seller_name;// ": "",
	public String token;// ": "574fc3cd6588a",
	public String name;// ":
	public String identity_card;// ": "123",
	public String seller_id;// ": "209",
	public String is_new;// 当不为空切是0时候标识是第一次登陆
	public String member_id;//
	public String invite_code;
	public String parent_id;
	public String invite_url;// 邀请码分享地址
	public String seller_no;// 店铺id



	public String getId() {
		return member_id;
	}

	public String getSeller_no() {
		return seller_no;
	}

	public void setSeller_no(String seller_no) {
		this.seller_no = seller_no;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getHead_img() {
		return head_img;
	}

	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getWeixin_open_id() {
		return weixin_open_id;
	}

	public void setWeixin_open_id(String weixin_open_id) {
		this.weixin_open_id = weixin_open_id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentity_card() {
		return identity_card;
	}

	public void setIdentity_card(String identity_card) {
		this.identity_card = identity_card;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getIs_new() {
		return is_new;
	}

	public void setIs_new(String is_new) {
		this.is_new = is_new;
	}

	public BUser() {
		super();
	}

	public String getPw_type() {
		return pw_type;
	}

	public void setPw_type(String pw_type) {
		this.pw_type = pw_type;
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

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	public String getInvite_url() {
		return invite_url;
	}

	public void setInvite_url(String invite_url) {
		this.invite_url = invite_url;
	}

//	public String getInvite_code_img() {
//		return invite_code_img;
//	}
//
//	public void setInvite_code_img(String invite_code_img) {
//		this.invite_code_img = invite_code_img;
//	}
//
//	public String getInvite_code_title() {
//		return invite_code_title;
//	}
//
//	public void setInvite_code_title(String invite_code_title) {
//		this.invite_code_title = invite_code_title;
//	}

	public BUser(String id, String phone, String head_img, String nickname,
			String pw_type, String weixin_open_id, String seller_name,
			String token, String name, String identity_card, String seller_id,
			String is_new) {
		super();
		this.id = id;
		this.phone = phone;
		this.head_img = head_img;
		this.nickname = nickname;
		this.pw_type = pw_type;
		this.weixin_open_id = weixin_open_id;
		this.seller_name = seller_name;
		this.token = token;
		this.name = name;
		this.identity_card = identity_card;
		this.seller_id = seller_id;
		this.is_new = is_new;
	}

	public String getInvite_code() {
		return invite_code;
	}

	public void setInvite_code(String invite_code) {
		this.invite_code = invite_code;
	}

	@Override
	public String toString() {
		return "BUser [id=" + id + "\n, phone=" + phone + "\n, head_img="
				+ head_img + "\n, nickname=" + nickname + "\n, pw_type="
				+ pw_type + "\n, weixin_open_id=" + weixin_open_id
				+ "\n, seller_name=" + seller_name + "\n, token=" + token
				+ "\n, name=" + name + "\n, identity_card=" + identity_card
				+ "\n, seller_id=" + seller_id + "\n, is_new=" + is_new
				+ "\n, member_id=" + member_id + "\n, invite_code="
				+ invite_code + "\n, parent_id=" + parent_id + "]";
	}

}
