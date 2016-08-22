package io.vtown.WeiTangApp.event.interf;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-23 下午12:36:05
 * 
 */
public interface IBottomDialogResult {
	/**
	 * 上边的
	 * 
	 * @param status
	 * @param datas
	 */
	void FristResult();

	/**
	 * 下边的
	 */
	void SecondResult();

	/**
	 * 取消按钮
	 */
	void CancleResult();
}
