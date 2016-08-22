package io.vtown.WeiTangApp.event.interf;

/**
 * 自定义接口，用于给密码输入完成添加回掉事件
 */
public interface OnPasswordInputFinish {
	/**
	 * 输入密码结束
	 */
	void inputFinish(String getStrPassword);
	/**
	 * 忘记密码
	 */
	void LostPassWord();
	/**
	 * 忘记密码
	 */
	void Cancle();
}
