package io.vtown.WeiTangApp.bean.bcache;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.db.DBHelper;
import io.vtown.WeiTangApp.db.annotation.Column;
import io.vtown.WeiTangApp.db.annotation.TableName;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-26 上午11:02:34
 * @author 搜索历史需要用到的缓存bean
 */
@TableName(DBHelper.SouRecordTab)
public class BSouRecord extends BBase {
	@Column(DBHelper.SouRecordTab_Id)
	private String id;// ": "3",
	@Column(DBHelper.SouRecordTab_Title)
	private String title;// ": "iPhone SE"
	@Column(DBHelper.SouRecordTab_Type)
	private String Type;//ToDo=>type 1标识商品的缓存  2标识店铺的缓存（目前只需缓存商品 不许缓存店铺）  预留字段

	public BSouRecord() {
		super();
	}

	public BSouRecord(String id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public BSouRecord(String id, String title, String type) {
		super();
		this.id = id;
		this.title = title;
		Type = type;
	}

}
