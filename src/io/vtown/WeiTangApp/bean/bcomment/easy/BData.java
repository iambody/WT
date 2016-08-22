package io.vtown.WeiTangApp.bean.bcomment.easy;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-25 下午5:18:43
 *  较少部分
 */
public class BData extends BBase {

	//进行扫码(邀请下级)后的数据
	private String invite_id;
	private List<BLDComment> qrtext=new ArrayList<BLDComment>();
	public String getInvite_id() {
		return invite_id;
	}
	public void setInvite_id(String invite_id) {
		this.invite_id = invite_id;
	}
	public List<BLDComment> getQrtext() {
		return qrtext;
	}
	public void setQrtext(List<BLDComment> qrtext) {
		this.qrtext = qrtext;
	}
	 
	
	
	
	
	
}
