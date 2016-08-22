package io.vtown.WeiTangApp.comment.upgrade;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-8-2 上午10:34:42
 * 
 */
public class UpdateManager {
	private Context mContext; // 上下文

	private String apkUrl; // apk下载地址
	private static final String savePath = "/sdcard/updateAPK/"; // apk保存到SD卡的路径
	private static final String saveFileName = savePath + "weitang.apk"; // 完整路径名

	private ProgressBar mProgress; // 下载进度条控件
	private static final int DOWNLOADING = 1; // 表示正在下载
	private static final int DOWNLOADED = 2; // 下载完毕
	private static final int DOWNLOAD_FAILED = 3; // 下载失败
	private int progress; // 下载进度
	private boolean cancelFlag = false; // 取消下载标志位

	private String updateDescription = " "; // 更新内容描述信息
	private String updateVersion = " "; // 更新d

	private AlertDialog alertDialog2; // 表示提示对话框、进度条对话框

	private TextView update_progress_txt;
	private TextView update_progress_version;// 版本描述
	private TextView update_progress_desc;

	/** 构造函数 */
	public UpdateManager(Context context, String Url, String Desc,
			String Version) {
		this.mContext = context;
		this.apkUrl = Url;
		this.updateDescription = Desc;
		this.updateVersion = Version;
	}

	public void UpDown() {
		showDownloadDialog();
	}

	/** 显示进度条对话框 */
	public void showDownloadDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle("正在更新");
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.softupdate_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.update_progress);
		update_progress_desc = (TextView) v
				.findViewById(R.id.update_progress_desc);
		update_progress_version = (TextView) v
				.findViewById(R.id.update_progress_version);
		update_progress_desc.setText(updateDescription);
		StrUtils.SetTxt(update_progress_version, "新版本:" + updateVersion);

		update_progress_txt = (TextView) v
				.findViewById(R.id.update_progress_txt);

		update_progress_txt.setVisibility(View.GONE);
		dialog.setView(v);

		alertDialog2 = dialog.create();
		alertDialog2.setCancelable(false);
		alertDialog2.show();

		// 下载apk
		downloadAPK();
	}

	/** 下载apk的线程 */
	public void downloadAPK() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					URL url = new URL(apkUrl);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.connect();

					int length = conn.getContentLength();
					InputStream is = conn.getInputStream();

					File file = new File(savePath);
					if (!file.exists()) {
						file.mkdir();
					}
					String apkFile = saveFileName;
					File ApkFile = new File(apkFile);
					FileOutputStream fos = new FileOutputStream(ApkFile);

					int count = 0;
					byte buf[] = new byte[1024];

					do {
						int numread = is.read(buf);
						count += numread;
						progress = (int) (((float) count / length) * 100);
						// 更新进度
						mHandler.sendEmptyMessage(DOWNLOADING);
						if (numread <= 0) {
							// 下载完成通知安装
							mHandler.sendEmptyMessage(DOWNLOADED);
							break;
						}
						fos.write(buf, 0, numread);
					} while (!cancelFlag); // 点击取消就停止下载.

					fos.close();
					is.close();
				} catch (Exception e) {
					mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
					e.printStackTrace();
				}
			}
		}).start();
	}

	/** 更新UI的handler */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case DOWNLOADING:
				update_progress_txt.setVisibility(View.VISIBLE);
				update_progress_txt.setText("下载进度" + progress + "%");
				mProgress.setProgress(progress);
				break;
			case DOWNLOADED:
				if (alertDialog2 != null)
					alertDialog2.dismiss();
				installAPK();
				break;
			case DOWNLOAD_FAILED:
				// Toast.makeText(mContext, "网络断开，请稍候再试", Toast.LENGTH_LONG)
				// .show();
				PromptManager.ShowCustomToast(mContext, "网络断开，请稍候再试");
				break;
			default:
				break;
			}
		}
	};

	/** 下载完成后自动安装apk */
	public void installAPK() {
		File apkFile = new File(saveFileName);
		if (!apkFile.exists()) {
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + apkFile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(intent);
	}
}
