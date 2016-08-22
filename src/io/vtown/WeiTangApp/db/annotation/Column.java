package io.vtown.WeiTangApp.db.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体中字段与表中列的对应关系
 * 
 * @author Liu
 * 
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

	/**
	 * 表名
	 * 
	 * @return
	 */
	String value();

}
