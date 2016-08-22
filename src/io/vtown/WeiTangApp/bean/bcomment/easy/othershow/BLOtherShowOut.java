package io.vtown.WeiTangApp.bean.bcomment.easy.othershow;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-27 下午2:41:20
 * 
 */
public class BLOtherShowOut extends BBase {
	private String date;// =2016-07-27
	private List<BLOtherShowIn> list = new ArrayList<BLOtherShowIn>();

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<BLOtherShowIn> getList() {
		return list;
	}

	public void setList(List<BLOtherShowIn> list) {
		this.list = list;
	}

}
