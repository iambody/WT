package io.vtown.WeiTangApp.db.dao;

import android.content.Context;
import io.vtown.WeiTangApp.bean.bcache.BHome;
import io.vtown.WeiTangApp.db.base.DAOSupport;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-26 下午3:22:37
 * 
 */
public class DaoHome extends DAOSupport<BHome> {
	private static DaoHome myDao = null;

	public DaoHome(Context context) {
		super(context);
	}

	public static DaoHome getInstance(Context context) {
		if (myDao == null) {
			myDao = new DaoHome(context);
		}
		return myDao;

	}
}
