package io.vtown.WeiTangApp.ui.comment.im;

import com.easemob.chat.EMChat;

import android.os.Bundle;
import android.view.View;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-18 上午10:36:22
 * 
 */
public class AChatLs extends ATitleBase {
	 
	@Override
	protected void InItBaseView() {
 
	}

	/**
	 * 获取联系人列表，并过滤掉黑名单和排序
	 */
//	private void getContactList() {
//		contactList.clear();
//		// 获取本地好友列表
//		Map<String, User> users = DemoApplication.getInstance()
//				.getContactList();
//		Iterator<Entry<String, User>> iterator = users.entrySet().iterator();
//		while (iterator.hasNext()) {
//			Entry<String, User> entry = iterator.next();
//			if (!entry.getKey().equals(Constant.NEW_FRIENDS_USERNAME)
//					&& !entry.getKey().equals(Constant.GROUP_USERNAME)
//					&& !entry.getKey().equals(Constant.CHAT_ROOM)
//					&& !blackList.contains(entry.getKey())) {
//				EMLog.i(TAG, "获取联系人=" + entry.getValue());
//				contactList.add(entry.getValue());
//			}
//		}
//	}

	@Override
	protected void InitTile() {
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
	}

	@Override
	protected void DataError(String error, int LoadType) {
	}

	@Override
	protected void NetConnect() {
	}

	@Override
	protected void NetDisConnect() {
	}

	@Override
	protected void SetNetView() {
	}

	@Override
	protected void MyClick(View V) {
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

}
