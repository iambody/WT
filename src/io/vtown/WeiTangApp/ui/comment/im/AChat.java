package io.vtown.WeiTangApp.ui.comment.im;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.event.receiver.NewMessageBroadcastReceiver;
import io.vtown.WeiTangApp.ui.ABase;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMGroupManager;
import com.easemob.util.NetUtils;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-8 下午3:05:31
 * 
 */
public class AChat extends ABase {
	private ProgressDialog pd; // 加载提示框
	private EditText tag_edt, username_edt, password_edt;
	private Button login_btn;
	Handler hand1 = new Handler();

	public final static String Tage_TageId = "TargetImId";
	// sellId
	private String TageId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		// IBasebun();
		InItBa();
	}

	private void IBasebun() {
		if (getIntent().getExtras() != null
				&& getIntent().getExtras().containsKey(Tage_TageId)) {
			TageId = Constants.ImHost + getIntent().getStringExtra(Tage_TageId);
			return;
		}
		PromptManager.ShowCustomToast(BaseContext, "selleid没有进入 For  Test");
		BaseActivity.finish();

	}

	private void InItBa() {

		// 初始化Hx(环信)
		onInit(this);
		// 注册一个监听连接状态的listener(环信)
		EMChatManager.getInstance().addConnectionListener(
				new MyConnectionListener());
		pd = new ProgressDialog(this);
		pd.setMessage("努力加载中...");
		pd.setCanceledOnTouchOutside(false);
		tag_edt = (EditText) findViewById(R.id.tag_name);// 找谁聊天
		username_edt = (EditText) findViewById(R.id.username);// 账号
		password_edt = (EditText) findViewById(R.id.password);// 密码
		login_btn = (Button) findViewById(R.id.login);// 登录
		login_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
			 
				if (!pd.isShowing())
					pd.show();
				login(tag_edt.getText().toString(), username_edt.getText()
						.toString(), password_edt.getText().toString());
			}
		});
	}

	public void login(final String tagname, final String username,
			final String password) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				EMChatManager.getInstance().login(username, password,
						new EMCallBack() {// 回调
							@Override
							public void onSuccess() {
								runOnUiThread(new Runnable() {
									public void run() {
										EMGroupManager.getInstance()
												.loadAllGroups();
										EMChatManager.getInstance()
												.loadAllConversations();
										Log.d("main", "登陆聊天服务器成功！");
										Toast.makeText(getApplicationContext(),
												"登陆聊天服务器成功", Toast.LENGTH_SHORT)
												.show();
										if (pd.isShowing())
											pd.dismiss();
										Intent intent = new Intent(
												BaseActivity, AChatInf.class);
										intent.putExtra("tagname", tagname);
										
//										startActivity(intent);
//										finish();
									}
								});
							}

							@Override
							public void onProgress(int progress, String status) {

							}

							@Override
							public void onError(int code, String message) {
								Log.d("main", "登陆聊天服务器失败！");

								runOnUiThread(new Runnable() {
									public void run() {
										Toast.makeText(getApplicationContext(),
												"登录失败，请重新登录",
												Toast.LENGTH_SHORT).show();
										pd.dismiss();
									}
								});
							}
						});

			}
		};

		hand1.postDelayed(r, 2000);
	}

	public synchronized boolean onInit(Context context) {

		Context appContext = getApplicationContext();
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		// 如果app启用了远程的service，此application:onCreate会被调用2次
		// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
		// 默认的app会在以包名为默认的process name下运行，如果查到的process name不是app的process
		// name就立即返回

		if (processAppName == null
				|| !processAppName.equalsIgnoreCase("io.vtown.WeiTangApp")) {
			// Log.e(TAG, "enter the service process!");
			// "com.easemob.chatuidemo"为demo的包名，换到自己项目中要改成自己包名

			// 则此application::onCreate 是被service 调用的，直接返回
			System.out
					.println("~~~~~~~~~~~~~~~~~~~~~~未初始化环信~~~~~~~~~~~~~~~~~~");
			return false;
		}
		// 初始化环信SDK
		Log.d("DemoApplication", "Initialize EMChat SDK");
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~初始化环信~~~~~~~~~~~~~~~~~~");
		EMChat.getInstance().init(appContext);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~已初始化环信~~~~~~~~~~~~~~~~~~");
		// 获取到EMChatOptions对象
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		// 默认添加好友时，是不需要验证的，改成需要验证
		options.setAcceptInvitationAlways(false);
		// 设置收到消息是否有新消息通知，默认为true
		options.setNotificationEnable(false);
		// 设置收到消息是否有声音提示，默认为true
		 options.setNoticeBySound(false);
		// 设置收到消息是否震动 默认为true
		options.setNoticedByVibrate(false);
		// 设置语音消息播放是否设置为扬声器播放 默认为true
		// options.setUseSpeaker(false);

		return true;
	}

	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this
				.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm
							.getApplicationInfo(info.processName,
									PackageManager.GET_META_DATA));
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
			}
		}
		return processName;
	}

	// 实现ConnectionListener接口
	private class MyConnectionListener implements EMConnectionListener {
		@Override
		public void onConnected() {
		}

		@Override
		public void onDisconnected(final int error) {
			System.out.println("这里有没有走啊，连接状态监听");
			if (error == EMError.USER_REMOVED) {
				// 显示帐号已经被移除
			} else if (error == EMError.CONNECTION_CONFLICT) {
				// 显示帐号在其他设备登陆
				Message mes = new Message();
				mes.what = 1;
				hand.sendMessage(mes);
			} else {
				if (NetUtils.hasNetwork(BaseActivity)) {
					// 连接不到聊天服务器
				} else {
					// 当前网络不可用，请检查网络设置
				}
			}
		}
	}

	Handler hand = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				// 显示帐号在其他设备登陆
				Toast.makeText(getApplicationContext(), "您的账号在别处登录了",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

}
