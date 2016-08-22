package io.vtown.WeiTangApp.db.base;


import java.io.Serializable;
import java.util.List;

/**
 * 实体操作的公共接口
 * 
 * @author Liu
 * 
 * @param <T>
 */
public interface DAO<T> {
	/**
	 * 添加
	 * 
	 * @param t
	 * @return
	 */
	long insert(T t);

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	int delete(Serializable id);

	/**
	 * 更新
	 * 
	 * @param t
	 * @return
	 */
	int update(T t);

	/**
	 * 查询所有
	 * 
	 * @return
	 */
	List<T> findAll();

	/**
	 * 按照条件查询
	 * 
	 * @param selection
	 * @param selectionArgs
	 * @param orderBy
	 * @return
	 */
	List<T> findByCondition(String selection, String[] selectionArgs,
			String orderBy);

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
	List<T> findByCondition(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy);
	
	long clearn();
}
