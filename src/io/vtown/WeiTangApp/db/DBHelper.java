package io.vtown.WeiTangApp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 负责数据缓存 模块
 * 
 * @author 大兔兔的账户
 * 
 */
public class DBHelper extends SQLiteOpenHelper {
	/**
	 * 数据库名字
	 */
	public final static String DB_NAME = "weitang.db";
	/**
	 * 数据库版本 TODO 版本1为1.0老版本数据库，
	 */
	public static int VERSION = 1;

	/**
	 * TODO当我们升级版本2时候需要对数据库进行重新定义（根据项目需求）
	 */
	public static int NEWVERSION = 2;
	/**
	 * test表主键
	 */
	public final static String TABLE_ID = "_id";

	// 存放各个表的表名******************************************************************
	/** 商品历史搜索的表名字 */
	public final static String SouRecordTab = "tabsourecord";
	/** 商品历史搜索的表名字&&&&&&& */
	/** Home的表名字 */
	public final static String HomeTab = "tabhome";
	/** Home的表名字&&&&&&&&& */
	// 存放各个表的colum******************************************************************
	/** 商品历史搜索 */
	public final static String SouRecordTab_Id = "id";
	public final static String SouRecordTab_Title = "title";
	public final static String SouRecordTab_Type = "type";
	/** 商品历史搜索&&&&&&& */
	/** Home */
	public final static String HomeTab_Advert_type = "advert_type";// 3=>BANNER;;2=>goods;;1=>agency;;0=>cate
	public final static String HomeTab_Title = "title";
	public final static String HomeTab_Pic_path = "pic_path";
	public final static String HomeTab_Url = "url";
	public final static String HomeTab_Recommend_position = "recommend_position";
	public final static String HomeTab_Category_id = "category_id";// banner单独有的
	public final static String HomeTab_Source_id = "source_id";
	public final static String HomeTab_Price = "price";
	// 下边是home弹出框出来的列表 默认advert_type 类型是0
	public final static String HomeTab_Id = "id";
	public final static String HomeTab_Cate_name = "cate_name";

	/** Home&&&&&&&& */
	public DBHelper(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		/** 商品历史搜索 **/
		String SouRecord_Sql = "CREATE TABLE " + SouRecordTab + " ( "
				+ TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ SouRecordTab_Type + " TEXT," + SouRecordTab_Id + " TEXT,"
				+ SouRecordTab_Title + " TEXT)";
		/** 商品历史搜索 **/
		db.execSQL(SouRecord_Sql);
		/** Home **/
		String HomeTab_Sql = "CREATE TABLE " + HomeTab + " ( " + TABLE_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + HomeTab_Advert_type
				+ " TEXT," + HomeTab_Title + " TEXT," + HomeTab_Pic_path
				+ " TEXT," + HomeTab_Url + " TEXT," + HomeTab_Source_id
				+ " TEXT," + HomeTab_Price + " TEXT,"
				+ HomeTab_Recommend_position + " TEXT," + HomeTab_Category_id
				+ " TEXT," + HomeTab_Id + " TEXT," + HomeTab_Cate_name
				+ " TEXT)";
		/** Home **/
		db.execSQL(HomeTab_Sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
