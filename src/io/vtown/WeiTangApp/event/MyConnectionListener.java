package io.vtown.WeiTangApp.event;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-8-2 下午5:10:11
 * 
 */
public class MyConnectionListener implements EMConnectionListener {
	private Activity pContext;

	public MyConnectionListener(Activity pContext) {
		super();
		this.pContext = pContext;
	}

	@Override
	public void onConnected() {
	}

	@Override
	public void onDisconnected(final int error) {
//		System.out.println("这里有没有走啊，连接状态监听");
		if (error == EMError.USER_REMOVED) {
			// 显示帐号已经被移除
		} else if (error == EMError.CONNECTION_CONFLICT) {
			// 显示帐号在其他设备登陆
			Message mes = new Message();
			mes.what = 1;
			hand.sendMessage(mes);
		} else {
//			if (NetUtils.hasNetwork(BaseActivity)) {
//				// 连接不到聊天服务器
//			} else {
//				// 当前网络不可用，请检查网络设置
//			}
		}
	}

	Handler hand = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				// 显示帐号在其他设备登陆
				// Toast.makeText(pContext, "您的账号在别处登录了",
				// Toast.LENGTH_SHORT).show();
			}
		}
	};
}
