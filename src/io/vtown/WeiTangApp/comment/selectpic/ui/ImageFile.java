package io.vtown.WeiTangApp.comment.selectpic.ui;


import org.ice4j.message.Message;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.selectpic.adapter.FolderAdapter;
import io.vtown.WeiTangApp.comment.selectpic.util.Bimp;
import io.vtown.WeiTangApp.comment.selectpic.util.PublicWay;
import io.vtown.WeiTangApp.comment.selectpic.util.Res;
import io.vtown.WeiTangApp.ui.comment.ASelectPic;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

/**
 * 这个类主要是用来进行显示包含图片的文件夹
 *
 */
public class ImageFile extends Activity {

	private FolderAdapter folderAdapter;
	private Button bt_cancel;
	private Context mContext;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(Res.getLayoutID("plugin_camera_image_file"));
		EventBus.getDefault().register(this, "PicSelctFinsh", BMessage.class);
		PublicWay.activityList.add(this);
		mContext = this;
		bt_cancel = (Button) findViewById(Res.getWidgetID("cancel"));
		bt_cancel.setOnClickListener(new CancelListener());
		GridView gridView = (GridView) findViewById(Res.getWidgetID("fileGridView"));
		TextView textView = (TextView) findViewById(Res.getWidgetID("headerTitle"));
		textView.setText(Res.getString("photo"));
		folderAdapter = new FolderAdapter(this);
		gridView.setAdapter(folderAdapter);
	}

	private class CancelListener implements OnClickListener {// 取消按钮的监听
		public void onClick(View v) {
			//清空选择的图片
			Bimp.tempSelectBitmap.clear();
			Intent intent = new Intent();
			intent.setClass(mContext, ASelectPic.class);
			startActivity(intent);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(mContext, ASelectPic.class);
			startActivity(intent);
		}
		
		return true;
	}
	/**
	   * 注册事件监听
	   */
		public void PicSelctFinsh(BMessage d) {
			this.finish();
		}
}
