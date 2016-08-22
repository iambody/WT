package io.vtown.WeiTangApp.service;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.contant.DownConfig;
import io.vtown.WeiTangApp.ui.ui.AMain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-8-1 下午5:56:57
 * 
 */
public class DownloadService extends Service {

	// notification 名字
	private String notify_name = "正在下载...";
	public static final String INTENT_URL = "myurl";
	public static final String Desc = "mydesc";
	private Context mContext = this;
	Notification mNotification;
	private static final int NOTIFY_ID = 0;
	private NotificationManager mNotificationManager;
	/* 下载包安装路径 */
	private static final String savePath = DownConfig.getUpgradePath();
	private static final String saveFileName  = savePath + "weitang.apk";
	private String apkUrl; // 下载地址
	private String ApkDesc;// 升级描述
	private int progress;
	boolean canceled;
	private Thread downLoadThread;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("DownloadService", "intent=" + intent.toString()
				+ " ;           flags= " + flags + " ;    startId" + startId);
		if (intent.hasExtra(DownloadService.INTENT_URL)) {
			apkUrl = (String) intent.getExtras()
					.get(DownloadService.INTENT_URL);
		}
		if (intent.hasExtra(DownloadService.Desc)) {
			ApkDesc = (String) intent.getExtras().get(DownloadService.Desc);
		}
		progress = 0;
		setUpNotification();
		new Thread() {
			public void run() {
				// 开始下载
				startDownload();
			};
		}.start();

		return startId;

	};

	private void startDownload() {
		canceled = false;
		downloadApk();
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				// 下载完毕
				// 取消通知
				mNotificationManager.cancel(NOTIFY_ID);
				installApk();
				break;
			case 2:
				// 这里是用户界面手动取消，所以会经过activity的onDestroy();方法
				// 取消通知
				mNotificationManager.cancel(NOTIFY_ID);
				break;
			case 1:
				int rate = msg.arg1;
				if (rate < 100) {
					RemoteViews contentview = mNotification.contentView;
					contentview.setTextViewText(R.id.tv_progress, rate + "%");
					contentview.setProgressBar(R.id.progressbar, 100, rate,
							false);
				} else {
					// 下载完毕后变换通知形式
					mNotification.flags = Notification.FLAG_AUTO_CANCEL;
					mNotification.contentView = null;
					mNotification.setLatestEventInfo(mContext, "下载完成",
							"文件已下载完毕", null);
					stopSelf();// 停掉服务自身
				}
				PendingIntent contentIntent2 = PendingIntent.getActivity(
						getApplicationContext(), 0, new Intent(),
						PendingIntent.FLAG_UPDATE_CURRENT);
				mNotification.contentIntent = contentIntent2;
				mNotificationManager.notify(NOTIFY_ID, mNotification);
				break;
			case 3:
				mNotification.flags = Notification.FLAG_AUTO_CANCEL;
				RemoteViews contentView = new RemoteViews(getPackageName(),
						R.layout.update_download_notification_layout);
				contentView.setTextViewText(R.id.name, "下载失败");
				// 指定个性化视图
				mNotification.contentView = contentView;
				Intent intent = new Intent(getApplicationContext(), AMain.class);
				PendingIntent contentIntent = PendingIntent.getActivity(
						getApplicationContext(), 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);

				// 指定内容意图
				mNotification.contentIntent = contentIntent;
				mNotificationManager.notify(NOTIFY_ID, mNotification);

				stopSelf();// 停掉服务自身
				break;

			}
		}
	};

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);
	}

	private int lastRate = 0;
	private InputStream is = null;
	private FileOutputStream fos = null;

	/**
	 * 下载apk
	 * 
	 * @param url
	 */
	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	private Runnable mdownApkRunnable = new Runnable() {

		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				is = conn.getInputStream();

				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					Message msg = mHandler.obtainMessage();
					msg.what = 1;
					msg.arg1 = progress;
					if (progress >= lastRate + 1) {
						mHandler.sendMessage(msg);
						lastRate = progress;
					}
					if (numread <= 0) {
						mHandler.sendEmptyMessage(0);// 下载完成通知安装
						// 下载完了，cancelled也要设置
						canceled = true;
						break;
					}
					fos.write(buf, 0, numread);
				} while (!canceled);// 点击取消就停止下载.
				 
				fos.close();
				is.close();
			} catch (Exception e) {

				Message msg = mHandler.obtainMessage();
				msg.what = 3;
				mHandler.sendMessage(msg);
				e.printStackTrace();
			} finally {
				try {
					if (fos != null) {
						fos.close();
					}
					is.close();
					if (is != null) {
						is.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	};

	/**
	 * 创建通知
	 */
	private void setUpNotification() {
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = "开始下载";
		long when = System.currentTimeMillis();
		mNotification = new Notification(icon, tickerText, when);
		;
		// 放置在"正在运行"栏目中
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;

		RemoteViews contentView = new RemoteViews(getPackageName(),
				R.layout.update_download_notification_layout);
		contentView.setTextViewText(R.id.name, notify_name);
		// 指定个性化视图
		mNotification.contentView = contentView;

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);

		// 指定内容意图
		mNotification.contentIntent = contentIntent;
		mNotificationManager.notify(NOTIFY_ID, mNotification);
	}

}
