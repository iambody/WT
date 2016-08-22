package io.vtown.WeiTangApp.db.dao;

import io.vtown.WeiTangApp.bean.bcache.BSouRecord;
import io.vtown.WeiTangApp.db.base.DAOSupport;
import android.content.Context;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-26 上午11:04:32
 * 
 */
public class DaoSouRecord extends DAOSupport<BSouRecord> {
	private static DaoSouRecord myDao = null;

	public DaoSouRecord(Context context) {
		super(context);
	}

	public static DaoSouRecord getInstance(Context context) {
		if (myDao == null) {
			myDao = new DaoSouRecord(context);
		}
		return myDao;

	}
}
