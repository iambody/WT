package io.vtown.WeiTangApp.ui.title.multiphotopicker;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.multiphoto.adapter.ImagePublishAdapter;
import io.vtown.WeiTangApp.comment.multiphoto.model.ImageItem1;
import io.vtown.WeiTangApp.comment.multiphoto.util.CustomConstants;
import io.vtown.WeiTangApp.comment.multiphoto.util.IntentConstants;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-26 下午3:38:04
 * 
 */
public class APublicGood extends ATitleBase {
	private GridView mGridView;
	private ImagePublishAdapter mAdapter;
	// private TextView sendTv;
	public static List<ImageItem1> mDataList = new ArrayList<ImageItem1>();
	private File tempFiles;
//	private static final int TAKE_PICTURE = 0x000000;
	private String path = "";

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.multiphoto_act_publish);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		notifyDataChanged(); // 当在ImageZoomActivity中删除图片时，返回这里需要刷新
	}

	public void initView() {
		TextView titleTv = (TextView) findViewById(R.id.title);
		titleTv.setText("");
		mGridView = (GridView) findViewById(R.id.gridview);
		mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mAdapter = new ImagePublishAdapter(this, mDataList);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == getDataSize()) {
					new PopupWindows(BaseActivity, mGridView);
				} else {
					Intent intent = new Intent(BaseActivity, AImageZoom.class);
//					intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST,
//							(Serializable) mDataList);
//					baseApplication.setAddPicLs(mDataList);
					intent.putExtra(IntentConstants.EXTRA_CURRENT_IMG_POSITION,
							position);
					startActivity(intent);
				}
			}
		});
	}

	private int getDataSize() {
		return mDataList == null ? 0 : mDataList.size();
	}

	private int getAvailableSize() {
		int availSize = CustomConstants.MAX_IMAGE_SIZE - mDataList.size();
		if (availSize >= 0) {
			return availSize;
		}
		return 0;
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	public class PopupWindows extends PopupWindow {

		public PopupWindows(Context mContext, View parent) {

			View view = View.inflate(mContext,
					R.layout.multiphoto_item_popupwindow, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.MATCH_PARENT);
			setHeight(LayoutParams.MATCH_PARENT);
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					takePhoto();
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					// Intent intent = new Intent(PublishActivity.this,
					// ImageBucketChooseActivity.class);
					// intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
					// getAvailableSize());
					// startActivity(intent);
					gallery();
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	/**
	 * 从相册获取
	 * 
	 * @param view
	 */
	public void gallery() {

		tempFiles = new File(Constants.PicHost, String.valueOf(System
				.currentTimeMillis()) + ".jpg"); 
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		BaseActivity.startActivityForResult(intent, TAKE_GALLY);

	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 * @return
	 */
//	private static String getPath1(String path) {
//		File f = new File(path);
//		if (!f.exists()) {
//			f.mkdirs();
//		}
//		return f.getAbsolutePath();
//	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 * @return
	 */
//	private File getFile1(String path) {
//		File f = new File(path);
//		if (!f.exists()) {
//			try {
//				f.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return f;
//	}

	public void takePhoto() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		File vFile = new File(Constants.PicHost, String.valueOf(System
				.currentTimeMillis()) + ".jpg");
		if (!vFile.exists()) {
			File vDirPath = vFile.getParentFile();
			vDirPath.mkdirs();
		} else {
			if (vFile.exists()) {
				vFile.delete();
			}
		}
		path = vFile.getPath();
		Uri cameraUri = Uri.fromFile(vFile);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
		BaseActivity.startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (mDataList.size() < CustomConstants.MAX_IMAGE_SIZE
					&& resultCode == -1 && !TextUtils.isEmpty(path)) {
				ImageItem1 item = new ImageItem1();
				item.sourcePath = path;
				Toast.makeText(getApplicationContext(), "file路径=>" + path,
						10 * 1000).show();
				mDataList.add(item);

			}
			break;
		case TAKE_GALLY:
			Uri uri = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(uri, filePathColumn,
					null, null, null);// 从系统表中查询指定Uri对应的照片
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex); // 获取照片路径
			cursor.close();
			// Bitmap bitmap= BitmapFactory.decodeFile(picturePath);

			ImageItem1 iteme = new ImageItem1();
			iteme.sourcePath = picturePath;
			mDataList.add(iteme);
			Toast.makeText(getApplicationContext(),
					"file路径=>" + tempFiles.getPath(), 10 * 1000).show();

			break;
		}
	}

	private void notifyDataChanged() {
		mAdapter.notifyDataSetChanged();
	}

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
