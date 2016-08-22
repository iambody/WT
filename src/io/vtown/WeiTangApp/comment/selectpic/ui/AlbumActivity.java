package io.vtown.WeiTangApp.comment.selectpic.ui;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.selectpic.adapter.AlbumGridViewAdapter;
import io.vtown.WeiTangApp.comment.selectpic.util.AlbumHelper;
import io.vtown.WeiTangApp.comment.selectpic.util.Bimp;
import io.vtown.WeiTangApp.comment.selectpic.util.ImageBucket;
import io.vtown.WeiTangApp.comment.selectpic.util.ImageItem;
import io.vtown.WeiTangApp.comment.selectpic.util.PublicWay;
import io.vtown.WeiTangApp.comment.selectpic.util.Res;
import io.vtown.WeiTangApp.ui.comment.AGoodShare;
import io.vtown.WeiTangApp.ui.comment.ASelectPic;

import java.util.ArrayList;
import java.util.List;

import org.ice4j.message.Message;

import de.greenrobot.event.EventBus;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * 这个是进入相册显示所有图片的界面
 * 
 */
public class AlbumActivity extends Activity {
	// 显示手机里的所有图片的列表控件
	private GridView gridView;
	// 当手机里没有图片时，提示用户没有图片的控件
	private TextView tv;
	// gridView的adapter
	private AlbumGridViewAdapter gridImageAdapter;
	// 完成按钮
	private Button okButton;
	// 返回按钮
	private Button back;
	// 取消按钮
	private Button cancel;
	private Intent intent;
	// 预览按钮
	private Button preview;
	private Context mContext;
	private ArrayList<ImageItem> dataList;
	private AlbumHelper helper;
	public static List<ImageBucket> contentList;
	public static Bitmap bitmap;
	// 在addgoods里面传进来一个表示 是宝贝的图片还是描述的图片
	private int AddGoodType = -1;

	// 是否从转发界面进来的
	private boolean isshare;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(Res.getLayoutID("plugin_camera_album"));
		EventBus.getDefault().register(this, "PicSelctFinsh", BMessage.class);
		PublicWay.activityList.add(this);
		isshare = getIntent().getBooleanExtra("isshare", false);
		mContext = this;
		// 注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
		IntentFilter filter = new IntentFilter("data.broadcast.action");
		registerReceiver(broadcastReceiver, filter);
		bitmap = BitmapFactory.decodeResource(getResources(),
				Res.getDrawableID("plugin_camera_no_pictures"));
		AddGoodType = getIntent().getIntExtra("pictype", -1);
		init();
		initListener();
		// 这个函数主要用来控制预览和完成按钮的状态
		isShowOkBt();
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// mContext.unregisterReceiver(this);
			// TODO Auto-generated method stub
			gridImageAdapter.notifyDataSetChanged();
		}
	};

	// 预览按钮的监听
	private class PreviewListener implements OnClickListener {
		public void onClick(View v) {
			if (Bimp.tempSelectBitmap.size() > 0) {
				intent.putExtra("position", "1");
				intent.setClass(AlbumActivity.this,
						isshare ? AShareGaller.class : GalleryActivity.class);
				startActivity(intent);
			}
		}

	}

	// 完成按钮的监听
	private class AlbumSendListener implements OnClickListener {
		public void onClick(View v) {
			overridePendingTransition(R.anim.activity_translate_in,
					R.anim.activity_translate_out);
			intent.setClass(mContext,
					isshare ? AGoodShare.class : ASelectPic.class).putExtra(
					"pictype", AddGoodType);
			startActivity(intent);
			finish();
		}

	}

	// 返回按钮监听
	private class BackListener implements OnClickListener {
		public void onClick(View v) {
			// intent.setClass(AlbumActivity.this,
			// ImageFile.class).putExtra("pictype", AddGoodType);
			if (isshare) {// 是从转发商品进来的
				intent.setClass(AlbumActivity.this, AGoodShare.class);
				startActivity(intent);
				return;
			}

			intent.setClass(AlbumActivity.this, ASelectPic.class).putExtra(
					"pictype", AddGoodType);
			startActivity(intent);
		}
	}

	// 取消按钮的监听
	private class CancelListener implements OnClickListener {
		public void onClick(View v) {
			Bimp.tempSelectBitmap.clear();

			if (isshare) {
				intent.setClass(mContext, ASelectPic.class);
				startActivity(intent);
				
				return;
			}
			intent.setClass(mContext, ASelectPic.class).putExtra("pictype",
					AddGoodType);
			startActivity(intent);
		}
	}

	// 初始化，给一些对象赋值
	private void init() {
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		contentList = helper.getImagesBucketList(false);
		dataList = new ArrayList<ImageItem>();
		for (int i = 0; i < contentList.size(); i++) {
			dataList.addAll(contentList.get(i).imageList);
		}

		back = (Button) findViewById(Res.getWidgetID("back"));
		back.setVisibility(View.INVISIBLE);
		cancel = (Button) findViewById(Res.getWidgetID("cancel"));
		cancel.setOnClickListener(new CancelListener());
		cancel.setVisibility(View.INVISIBLE);
		back.setOnClickListener(new BackListener());
		preview = (Button) findViewById(Res.getWidgetID("preview"));
		preview.setOnClickListener(new PreviewListener());
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		gridView = (GridView) findViewById(Res.getWidgetID("myGrid"));
		gridImageAdapter = new AlbumGridViewAdapter(this, dataList,
				Bimp.tempSelectBitmap);
		gridView.setAdapter(gridImageAdapter);
		tv = (TextView) findViewById(Res.getWidgetID("myText"));
		gridView.setEmptyView(tv);
		okButton = (Button) findViewById(Res.getWidgetID("ok_button"));
		okButton.setText(Res.getString("finish") + "("
				+ Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
	}

	private void initListener() {

		gridImageAdapter
				.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(final ToggleButton toggleButton,
							int position, boolean isChecked, Button chooseBt) {
						if (Bimp.tempSelectBitmap.size() >= PublicWay.num) {
							toggleButton.setChecked(false);
							chooseBt.setVisibility(View.GONE);
							if (!removeOneData(dataList.get(position))) {
								Toast.makeText(AlbumActivity.this,
										Res.getString("only_choose_num"), 200)
										.show();
							}
							return;
						}
						if (isChecked) {
							chooseBt.setVisibility(View.VISIBLE);
							Bimp.tempSelectBitmap.add(dataList.get(position));
							okButton.setText(Res.getString("finish") + "("
									+ Bimp.tempSelectBitmap.size() + "/"
									+ PublicWay.num + ")");
						} else {
							Bimp.tempSelectBitmap.remove(dataList.get(position));
							chooseBt.setVisibility(View.GONE);
							okButton.setText(Res.getString("finish") + "("
									+ Bimp.tempSelectBitmap.size() + "/"
									+ PublicWay.num + ")");
						}
						isShowOkBt();
					}
				});

		okButton.setOnClickListener(new AlbumSendListener());

	}

	private boolean removeOneData(ImageItem imageItem) {
		if (Bimp.tempSelectBitmap.contains(imageItem)) {
			Bimp.tempSelectBitmap.remove(imageItem);
			okButton.setText(Res.getString("finish") + "("
					+ Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
			return true;
		}
		return false;
	}

	public void isShowOkBt() {
		if (Bimp.tempSelectBitmap.size() > 0) {
			okButton.setText(Res.getString("finish") + "("
					+ Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
			preview.setPressed(true);
			okButton.setPressed(true);
			preview.setClickable(true);
			okButton.setClickable(true);
			okButton.setTextColor(Color.WHITE);
			preview.setTextColor(Color.WHITE);
		} else {
			okButton.setText(Res.getString("finish") + "("
					+ Bimp.tempSelectBitmap.size() + "/" + PublicWay.num + ")");
			preview.setPressed(false);
			preview.setClickable(false);
			okButton.setPressed(false);
			okButton.setClickable(false);
			okButton.setTextColor(Color.parseColor("#E1E0DE"));
			preview.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			intent.setClass(AlbumActivity.this, ImageFile.class);
			startActivity(intent);
		}
		return false;

	}

	@Override
	protected void onRestart() {
		isShowOkBt();
		super.onRestart();
	}

	/**
	 * 注册事件监听
	 */
	public void PicSelctFinsh(BMessage dBMessage) {
		this.finish();
	}
}
