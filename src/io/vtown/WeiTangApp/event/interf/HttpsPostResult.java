package io.vtown.WeiTangApp.event.interf;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-8-4 下午2:33:07
 * 
 */
public interface HttpsPostResult {
	/**
	 * 请求成功返回的内容
	 * 
	 * @param status
	 * @param datas
	 */
	public void getResult(String Msg);

	/**
	 * 请求失败返回的错误信息
	 */
	public void onError();
}
