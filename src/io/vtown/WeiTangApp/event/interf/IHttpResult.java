/**
 * 
 */
package io.vtown.WeiTangApp.event.interf;

/**
 * @author 王永奎 E-mail:wangyk@nsecurities.cn
 * @department 互联网金融部
 * @version 创建时间：2015-11-6 下午2:04:21 用于HTTP请求网络数据时候的回掉
 */

public interface IHttpResult<T> {
	/**
	 * 请求成功返回的内容
	 * 
	 * @param status
	 * @param datas
	 */
	void getResult( int Code,String Msg,  T Data);

	/**
	 * 请求失败返回的错误信息
	 */
	void onError(String error,int LoadType);

}
