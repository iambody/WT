package io.vtown.WeiTangApp.ui.title.shop.addgood;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.multiphoto.adapter.ImagePublishAdapter;
import io.vtown.WeiTangApp.comment.multiphoto.model.ImageItem1;
import io.vtown.WeiTangApp.comment.multiphoto.util.CustomConstants;
import io.vtown.WeiTangApp.comment.multiphoto.util.ImageDisplayer;
import io.vtown.WeiTangApp.comment.multiphoto.util.IntentConstants;
import io.vtown.WeiTangApp.comment.net.NHttpBaseStr;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils.UpResult;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.event.interf.IHttpResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.multiphotopicker.AImageZoom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.lidroid.xutils.util.LogUtils;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-17 上午10:12:37
 * @see 上传宝贝时候需要选择图片
 */
public class AAddpic extends ATitleBase {

	// 保存的gradview
	private GridView addgoodpic_gridview;
	// 提交按钮
	private TextView addgoodpic_submint;
	// 适配器
	private PubAdapter mAdapter;

	public List<ImageItem1> mDataList = new ArrayList<ImageItem1>();
	// 相册图片临时保存
	private File tempFiles;
	// 拍照图片临时保存
	private String path = "";
	/**
	 * statactivityforresult的key 请求和返回用同一个int数值
	 */
	public final static int AddPicKey = 401;

	private boolean OnresumeFromPage = false;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_addpic);
		IBaseVVV();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (OnresumeFromPage) {
			OnresumeFromPage = !OnresumeFromPage;
			return;
		}
		Mynotify(); // 当在ImageZoomActivity中删除图片时，返回这里需要刷新

	}

	private void IBaseVVV() {
		addgoodpic_submint = (TextView) findViewById(R.id.addgoodpic_submint);
		addgoodpic_submint.setOnClickListener(this);
		addgoodpic_gridview = (GridView) findViewById(R.id.addgoodpic_gridview);
		addgoodpic_gridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		mAdapter = new PubAdapter(this, mDataList);
		addgoodpic_gridview.setAdapter(mAdapter);
		addgoodpic_gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == getDataSize()) {
					new PopupWindows(BaseActivity, addgoodpic_gridview);
				} else {
					try {
//
//						baseApplication.setAddPicLs(mDataList);
//						Intent intent = new Intent(BaseActivity,
//								AImageZoom.class);
//						// intent.putExtra(IntentConstants.EXTRA_IMAGE_LIST,
//						// (Serializable) mDataList);
//						intent.putExtra(
//								IntentConstants.EXTRA_CURRENT_IMG_POSITION,
//								position);
//						BaseActivity.startActivity(intent);
//						OnresumeFromPage = true;
					} catch (Exception e) {
						LogUtils.i("ssss");
					}
				}
			}
		});
	}

	@Override
	protected void InitTile() {
		SetTitleTxt("选择宝贝图片");
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
		switch (V.getId()) {
		case R.id.addgoodpic_submint:// 点击提交
//			baseApplication.setAddGoodsPics(mDataList);
			setResult(AddPicKey, new Intent());
			BaseActivity.finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
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
					// Intent intent = new Intent(BaseActivity,
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

				InputStream is;
				try {
					is = new FileInputStream(new File(path));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					break;
				}
				// BitmapFactory.decodeStream(is);

				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inTempStorage = new byte[100 * 1024];
				opts.inPurgeable = true;
				// 5.设置位图缩放比例
				// width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
				opts.inSampleSize = 4;
				Bitmap mBitmap = BitmapFactory.decodeStream(is, null, opts);

				item.setmBitmap(mBitmap);
				// item.setmBitmap(BitmapFactory.decodeFile(path));
				Toast.makeText(getApplicationContext(), "file路径=>" + path,
						10 * 1000).show();
				mDataList.add(item);

			}
			break;
		case TAKE_GALLY:
			Uri uri;
			try {
				uri = data.getData();
			} catch (Exception e) {
				LogUtils.i("ss");
				break;
			}
			String picturePath = null;
			Bitmap bitmap = null;
			try {

				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(uri, filePathColumn,
						null, null, null);// 从系统表中查询指定Uri对应的照片
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				picturePath = cursor.getString(columnIndex); // 获取照片路径
				cursor.close();
				// bitmap = BitmapFactory.decodeFile(picturePath);
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inTempStorage = new byte[100 * 1024];
				opts.inPurgeable = true;
				// 5.设置位图缩放比例
				// width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
				opts.inSampleSize = 4;

				InputStream is = new FileInputStream(picturePath);
				bitmap = BitmapFactory.decodeStream(is, null, opts);

			} catch (Exception e) {
				break;
			}
			ImageItem1 iteme = new ImageItem1();
			iteme.setmBitmap(bitmap);
			iteme.sourcePath = picturePath;
			mDataList.add(iteme);
			// Toast.makeText(getApplicationContext(),
			// "file路径=>" + tempFiles.getPath(), 10 * 1000).show();

			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			BaseActivity.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void Mynotify() {
		mAdapter.notifyDataSetChanged();
		if (mDataList.size() == 0)
			return;
		// mAdapter.AddFrash(mDataList.get(mDataList.size() - 1));
		ImageItem1 mImageItem = mDataList.get(mDataList.size() - 1);
		//
		UpdaIvTest(StrUtils.Bitmap2Bytes(StrUtils.GetBitMapFromPath(mImageItem
				.getSourcePath())), StrUtils.UploadQNName("photo"));
	}

	public class PubAdapter extends BaseAdapter {
		private List<ImageItem1> Datas = new ArrayList<ImageItem1>();
		private Boolean IsShoProgres = true;
		private Context mContext;

		public PubAdapter(Context context, List<ImageItem1> dataList) {
			this.mContext = context;
			this.Datas = dataList;

		}

		/**
		 * 上传成功后刷新
		 */
		public void UpLoadDone() {
			IsShoProgres = false;
			this.notifyDataSetChanged();
			new java.util.Timer().schedule(new TimerTask() {

				@Override
				public void run() {
					IsShoProgres = true;
					this.cancel();
				}
			}, 2000);

		}

		/**
		 * shangc
		 */
		public void UpLoaderror() {

		}

		public int getCount() {
			// 多返回一个用于展示添加图标
			if (Datas == null) {
				return 1;
			} else if (Datas.size() == CustomConstants.MAX_IMAGE_SIZE) {
				return CustomConstants.MAX_IMAGE_SIZE;
			} else {
				return Datas.size() + 1;
			}
		}

		public Object getItem(int position) {
			if (Datas != null && Datas.size() == CustomConstants.MAX_IMAGE_SIZE) {
				return Datas.get(position);
			}

			else if (Datas == null || position - 1 < 0
					|| position > Datas.size()) {
				return null;
			} else {
				return Datas.get(position - 1);
			}
		}

		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("ViewHolder")
		public View getView(int position, View convertView, ViewGroup parent) {
			// 所有Item展示不满一页，就不进行ViewHolder重用了，避免了一个拍照以后添加图片按钮被覆盖的奇怪问题
			convertView = View.inflate(mContext,
					R.layout.multiphoto_item_publish, null);
			ImageView imageIv = (ImageView) convertView
					.findViewById(R.id.item_grid_image);
			ProgressBar item_grid_image_progress = (ProgressBar) convertView
					.findViewById(R.id.item_grid_image_progress);
			if (isShowAddItem(position)) {
				imageIv.setImageResource(R.drawable.multiphoto_btn_add_pic);
				imageIv.setBackgroundResource(R.color.bg_gray);
				item_grid_image_progress.setVisibility(View.GONE);
			} else {
				final ImageItem1 item = Datas.get(position);
				ImageDisplayer.getInstance(mContext).displayBmp(imageIv,
						item.thumbnailPath, item.sourcePath);
				if (position == Datas.size() - 1) {
					item_grid_image_progress
							.setVisibility(IsShoProgres ? View.VISIBLE
									: View.GONE);
				} else {
					item_grid_image_progress.setVisibility(View.GONE);
				}
				// item_grid_image_progress
				// .setVisibility(booleans.get(position) ? View.VISIBLE
				// : View.GONE);
			}

			return convertView;
		}

		private boolean isShowAddItem(int position) {
			int size = Datas == null ? 0 : Datas.size();
			return position == size;
		}
	}

	// *********************************

	public void UpdaIvTest(byte[] bytes, final String picname) {
		NUpLoadUtils dLoadUtils = new NUpLoadUtils(BaseContext, bytes, picname);
		dLoadUtils.SetUpResult(new UpResult() {

			@Override
			public void Progress(String arg0, double arg1) {
			}

			@Override
			public void Onerror() {
				PromptManager.ShowCustomToast(BaseContext, "上传Onerror");

			}

			@Override
			public void Complete(String HostUrl, String Url) {
				mAdapter.UpLoadDone();
				mDataList.get(mDataList.size() - 1).setThumbnailPath(HostUrl);
				PromptManager.ShowCustomToast(BaseContext, "上传成功名字：" + picname);
			}
		});

		dLoadUtils.UpLoad();
	}

}
