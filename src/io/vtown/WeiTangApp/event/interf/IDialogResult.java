package io.vtown.WeiTangApp.event.interf;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-22 下午6:05:46
 * 左右选择的弹出框回掉
 */
public interface IDialogResult {
	/**
	 * 请求成功返回的内容
	 * 
	 * @param status
	 * @param datas
	 */
	void LeftResult();

	/**
	 * 请求失败返回的错误信息
	 */
	void RightResult();
}
