package io.vtown.WeiTangApp.db.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实体与表的对应关系
 * 
 * @author Liu
 * 
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableName {

	/**
	 * 表名
	 * 
	 * @return
	 */
	String value();

}
