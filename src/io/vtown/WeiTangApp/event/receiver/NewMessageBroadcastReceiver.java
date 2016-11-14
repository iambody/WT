package io.vtown.WeiTangApp.event.receiver;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;

import de.greenrobot.event.EventBus;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.comment.im.AChatInf;
import io.vtown.WeiTangApp.ui.title.mynew.ANew;
import io.vtown.WeiTangApp.ui.ui.AMainTab;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-12 下午4:37:53 IM 换信进行聊的通知
 */
@SuppressLint("NewApi")
public class NewMessageBroadcastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// 消息id
		String msgId = intent.getStringExtra("msgid");
		// 发消息的人的username(userid)
		String msgFrom = intent.getStringExtra("from");
		// 消息类型，文本，图片，语音消息等,这里返回的值为msg.type.ordinal()。
		// 所以消息type实际为是enum类型
		int msgType = intent.getIntExtra("type", 0);
		Log.d("main", "new message id:" + msgId + " from:" + msgFrom + " type:"
				+ msgType);

		/*
		 * // 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象 EMMessage
		 * EMMessagemessage = EMChatManager.getInstance().getMessage( msgId);
		 * EMConversation conversation = EMChatManager.getInstance()
		 * .getConversation(msgFrom);
		 * 
		 * // 更方便的方法是通过msgId直接获取整个message // EMMessage message = //
		 * EMChatManager.getInstance().getMessage(msgId);
		 * 
		 * // if (msgFrom.equals(chatname) && adapter != null) {//
		 * 如果发消息的人就是当前聊天的人 // conversation =
		 * EMChatManager.getInstance().getConversation( // msgFrom); //
		 * adapter.notifyDataSetChanged();// 刷新适配器 //
		 * chat_listview.setSelection(chat_listview.getBottom());// 显示到最后一条 // }
		 * BMessage daBMessage = new BMessage(BMessage.Tage_IM_Notic);
		 * daBMessage.setFromImId(msgFrom);
		 * EventBus.getDefault().post(daBMessage);
		 */
		// PromptManager.ShowCustomToast(context, "消息！！！！！！！！！！！！！");
		// showNotify(context);
		EMMessage message = EMChatManager.getInstance().getMessage(msgId);

		String SendName = "";

		try {
			SendName = message.getStringAttribute("extSendNickname");
		} catch (Exception e) {
			// TODO: handle exception
		}

		ShowMyNotifi(context, "您有一条新消息", StrUtils.isEmpty(SendName) ? "小糖果"
				: SendName + "的消息", "请及时查看");
		EventBus.getDefault().post(new BMessage(BMessage.IM_Have_MSG));
		Spuit.IMMessage_Set(context, true);

	}

	Intent[] makeIntentStack(Context context) {
		Intent[] intents = new Intent[2];

		return intents;
	}

	public static void showNotify(Context context) {

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification.Builder builder = new Notification.Builder(context);
		Intent intent = new Intent(context, AChatInf.class); // 需要跳转指定的页面
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pendingIntent);
		builder.setSmallIcon(R.drawable.ic_launcher);// 设置图标
		builder.setContentTitle("标题");// 设置通知的标题
		builder.setContentText("内容");// 设置通知的内容
		builder.setWhen(System.currentTimeMillis());// 设置通知来到的时间
		builder.setAutoCancel(true); // 自己维护通知的消失
		builder.setTicker("new message");// 第一次提示消失的时候显示在通知栏上的
		builder.setOngoing(true);
		builder.setNumber(20);

		Notification notification = builder.build();
		notification.flags = Notification.FLAG_NO_CLEAR; // 只有全部清除时，Notification才会清除
		notificationManager.notify(0, notification);
	}

	/**
	 * API=》18after 使用com
	 */
	private void ShowMyNotifi(Context context, String tickerText, String title,
			String content) {
		// 获取server
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// 实例化budil
		NotificationCompat.Builder mBuilder = new Builder(context);
		mBuilder.setTicker(tickerText);
		mBuilder.setContentTitle(title);
		mBuilder.setContentText(content);// 设置通知下拉后的内容
		mBuilder.setWhen(System.currentTimeMillis());// 设置通知的时间
		mBuilder.setPriority(Notification.PRIORITY_DEFAULT);// 设置通知优先级
		mBuilder.setOngoing(false);// //ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
		mBuilder.setDefaults(Notification.DEFAULT_ALL);
		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setContentIntent(GetPend(context));
		mBuilder.setAutoCancel(true);
		// 设置notification
		Notification mNotification = mBuilder.build();

		mNotificationManager.notify(1, mNotification);
	}

	private PendingIntent GetPend(Context context) {
		// Intent[] intents = new Intent[2];
		// // intents[0] = Intent.makeRestartActivityTask(new
		// ComponentName(context,
		// // io.vtown.WeiTangApp.ui.ui.AMain.class));
		// intents[0] = new Intent(context,
		// io.vtown.WeiTangApp.ui.ui.AMain.class);
		// intents[1] = new Intent(io.vtown.WeiTangApp.ui.ui.AMain.this,
		// io.vtown.WeiTangApp.ui.title.mynew.ANew.class);
		//
		// PendingIntent pendingIntent = PendingIntent.getActivities(context, 0,
		// intents, 0);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				new Intent(context, AMainTab.class).putExtra("isNewNotify",true)
						.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
				PendingIntent.FLAG_CANCEL_CURRENT);
		return pendingIntent;

	}
}
