package io.vtown.WeiTangApp.db.base;

import io.vtown.WeiTangApp.db.DBHelper;
import io.vtown.WeiTangApp.db.annotation.Column;
import io.vtown.WeiTangApp.db.annotation.ID;
import io.vtown.WeiTangApp.db.annotation.TableName;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
 

/**
 * 实现类的公共部分
 * 
 * @author Liu
 * 
 * @param <T>
 */
public abstract class DAOSupport<T> implements DAO<T> {
	/**
	 * 通用化处理 问题一: 表名获取 问题二: 将实体中的字段数据设置给数据表中对应的列 问题三: 设置数据到实体的对应字段中 问题四:
	 * 获取到实体中主键的数据 问题五: 实体对应的实例创建
	 */

	protected Context context;
	protected DBHelper helper;
	protected SQLiteDatabase db;

	public DAOSupport(Context context) {
		super();
		this.context = context;
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	@Override
	public long insert(T t) {

		ContentValues values = new ContentValues();
		fillContentValues(t, values);
		return db.insert(getTableName(), null, values);
	}

	@Override
	public int delete(Serializable id) {
		return db.delete(getTableName(), getTableId() + " = ?",
				new String[] { id.toString() });
	}

	/**
	 * 删除指定的一行
	 * 
	 * @param colum
	 * @param Valuse
	 * @return
	 */
	public int DeletItem(String colum, String Valuse) {
		return db.delete(getTableName(), colum + " = ?",
				new String[] { Valuse });
	}

	@Override
	public int update(T t) {
		ContentValues values = new ContentValues();
		fillContentValues(t, values);
		return db.update(getTableName(), values, getTableId() + " = ?",
				new String[] { getId(t) });
	}

	/**
	 * 清除所有项目
	 */
	@Override
	public long clearn() {
		String whereClause = null;
		String[] whereArgs = null;

		return db.delete(getTableName(), whereClause, whereArgs);

	}

	@Override
	public List<T> findAll() {
		return findByCondition(null, null, null);
	}

	public List<T> findByCondition(String selection, String[] selectionArgs,
			String orderBy) {
		return findByCondition(null, selection, selectionArgs, null, null,
				orderBy);
	}

	/**
	 * 按照条件查询
	 * 
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 */
	public List<T> findByCondition(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		List<T> result = null;

		Cursor cursor = null;
		try {
			cursor = db.query(getTableName(), columns, selection,
					selectionArgs, groupBy, having, orderBy);
			if (cursor != null) {
				result = new ArrayList<T>();
				while (cursor.moveToNext()) {

					T t = getInstance();
					// 设置数据
					fillField(cursor, t);// 数据源，设置的目标
					result.add(t);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null)
				cursor.close();
		}

		return result;
	}

	/**
	 * 设置数据到实体的对应字段中
	 * 
	 * @param t
	 * @param cursor
	 */
	private void fillField(Cursor cursor, T t) {
		Field[] fields = t.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				int columnIndex = cursor.getColumnIndex(column.value());
				Object value;
				if (field.getType() == int.class) {
					value = cursor.getInt(columnIndex);
				} else if (field.getType() == long.class) {
					value = cursor.getLong(columnIndex);
				} else if (field.getType() == float.class) {
					value = cursor.getFloat(columnIndex);
				} else if (field.getType() == double.class) {
					value = cursor.getDouble(columnIndex);
				} else {
					value = cursor.getString(columnIndex);
				}
				try {
					field.set(t, value);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String getTableId() {
		return "id";

		// T t = getInstance();
		// Field[] fields = t.getClass().getDeclaredFields();
		// for (Field field : fields) {
		// field.setAccessible(true);
		// ID id = field.getAnnotation(ID.class);
		// if (id != null) {
		// Column column = field.getAnnotation(Column.class);
		// if (column != null)
		// return column.value();
		// }
		//
		// }
		// return null;
	}

	/**
	 * 获取实体对应主键数据
	 * 
	 * @param t
	 * @return
	 */
	private String getId(T t) {
		Field[] fields = t.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			ID id = field.getAnnotation(ID.class);
			if (id != null) {
				try {
					return field.get(t).toString();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		}
		return null;
	}

	/**
	 * 将实体中的字段数据设置给数据表中对应的列
	 * 
	 * @param t
	 * @param values
	 */
	private void fillContentValues(T t, ContentValues values) {
		Field[] fields = t.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Column column = field.getAnnotation(Column.class);

			if (column != null) {
				String key = column.value();
				try {
					if (field.get(t) != null) {
						String value = field.get(t).toString();
						ID id = field.getAnnotation(ID.class);
						if (id != null && id.autoincrement())
							continue;
						values.put(key, value);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取表名
	 * 
	 * @return
	 */
	private String getTableName() {
		T t = getInstance();
		TableName tableName = t.getClass().getAnnotation(TableName.class);
		if (tableName != null) {
			return tableName.value();
		}
		return null;
	}

	/**
	 * 获取实体实例
	 * 
	 * @return
	 */
	private T getInstance() {
		// getClass()获取运行时类
		// getGenericSuperclass()获取支持泛型的父类
		Type genericSuperclass = getClass().getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			Type type = ((ParameterizedType) genericSuperclass)
					.getActualTypeArguments()[0];
			try {
				return (T) ((Class) type).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return null;

	}
}
