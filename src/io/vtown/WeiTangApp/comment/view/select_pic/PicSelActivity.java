package io.vtown.WeiTangApp.comment.view.select_pic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.util.StrUtils;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.view.select_pic.utils.ImageLoader;
import io.vtown.WeiTangApp.comment.view.select_pic.utils.bean.ImageFloder;


public class PicSelActivity extends Activity implements ListImageDirPopupWindow.OnImageDirSelected {
	protected static  int MAX_SEL_ENABLE = 0;
	public static final int REQUEST_PICS_CODE = 201;
	public static final int RESULT_PICS_OK_CODE = 202;
	public static final String RESULT_PICS_ARRAY = "RESULT_PICS_ARRAY";
	private ProgressDialog mProgressDialog;

	/**
	 * 存储文件夹中的图片数量
	 */
	private int mPicsSize;
	/**
	 * 图片数量最多的文件夹
	 */
	private File mImgDir;
	/**
	 * 所有的图片
	 */
	private List<String> mImgs;

	private GridView mGirdView;
	private MyAdapter mAdapter;
	/**
	 * 临时的辅助类，用于防止同一个文件夹的多次扫描
	 */
	private HashSet<String> mDirPaths = new HashSet<String>();

	/**
	 * 扫描拿到所有的图片文件夹
	 */
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

	// private RelativeLayout mBottomLy;

	// private TextView mChooseDir;
	// private TextView mImageCount;
	int totalCount = 0;
	private int mScreenHeight;
	private ListImageDirPopupWindow mListImageDirPopupWindow;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mProgressDialog.dismiss();
			// 为View绑定数据
			data2View();
			// 初始化展示文件夹的popupWindw
			initListDirPopupWindw();
		}
	};

	// 图片选择回调
	private MyAdapter.OnPicSelCallBack onPicSelCallBack = new MyAdapter.OnPicSelCallBack() {

		@Override
		public void onSel(final Set<String> mSelectedImage) {
			PicSelActivity.this.onSel(mSelectedImage);
		}
	};

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	protected void onSel(final Set<String> mSelectedImage) {
		if (mSelectedImage == null) {
			return;
		}
		ly_bt_content.removeAllViews();

		for (final String uri:mSelectedImage) {
			View child = getLayoutInflater().inflate(
					R.layout.com_mark_utils_picsel_bottom_item, null);
			ImageView img = (ImageView) child.findViewById(R.id.id_item_image);
			ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(uri, img);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					dip2px(PicSelActivity.this, 77), dip2px(
					PicSelActivity.this, 77));
			child.setLayoutParams(params);

			RelativeLayout.LayoutParams paramsr = new RelativeLayout.LayoutParams(
					dip2px(PicSelActivity.this, 57), dip2px(
					PicSelActivity.this, 57));
			paramsr.topMargin = dip2px(PicSelActivity.this, 10);
			paramsr.leftMargin = dip2px(PicSelActivity.this, 10);
			img.setLayoutParams(paramsr);
			child.findViewById(R.id.id_item_close).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View view) {
							ly_bt_content.removeView((View) view.getParent());
							mSelectedImage.remove(uri);
							mAdapter.notifyDataSetChanged();
							id_sel_count.setText(mSelectedImage.size() + "/"
									+ MAX_SEL_ENABLE);
						}
					});
			ly_bt_content.addView(child);
		}

//		for (int i = 0; i < mSelectedImage.size(); i++) {
//			final String uri = mSelectedImage.get(i);
//			View child = getLayoutInflater().inflate(
//					R.layout.com_mark_utils_picsel_bottom_item, null);
//			ImageView img = (ImageView) child.findViewById(R.id.id_item_image);
//			ImageLoader.getInstance(3, Type.LIFO).loadImage(uri, img);
//			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//					dip2px(PicSelActivity.this, 77), dip2px(
//							PicSelActivity.this, 77));
//			child.setLayoutParams(params);
//
//			RelativeLayout.LayoutParams paramsr = new RelativeLayout.LayoutParams(
//					dip2px(PicSelActivity.this, 57), dip2px(
//							PicSelActivity.this, 57));
//			paramsr.topMargin = dip2px(PicSelActivity.this, 10);
//			paramsr.leftMargin = dip2px(PicSelActivity.this, 10);
//			img.setLayoutParams(paramsr);
//			child.findViewById(R.id.id_item_close).setOnClickListener(
//					new OnClickListener() {
//						@Override
//						public void onClick(View view) {
//							ly_bt_content.removeView((View) view.getParent());
//							mSelectedImage.remove(uri);
//							mAdapter.notifyDataSetChanged();
//							id_sel_count.setText(mSelectedImage.size() + "/"
//									+ MAX_SEL_ENABLE);
//						}
//					});
//			ly_bt_content.addView(child);
//		}
		id_sel_count.setText(mSelectedImage.size() + "/" + MAX_SEL_ENABLE);
	}

	public static int dip2px(Context context, int dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	private LinearLayout ly_bt_content;

	private TextView id_sel_count;

	private View ly_top;
	private File tmpFile;
	private String tmpPath;
	private static PicSelActivity instance;

	/**
	 * 为View绑定数据
	 */
	private void data2View() {
		if (mImgDir == null) {
			Toast.makeText(getApplicationContext(), "擦，一张图片没扫描到",
					Toast.LENGTH_SHORT).show();
			return;
		}

		mImgs = Arrays.asList(mImgDir.list());
		/**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		 */
		mAdapter = new MyAdapter(getApplicationContext(), mImgs,
				R.layout.com_mark_utils_picsel_grid_item,
				mImgDir.getAbsolutePath());
		mAdapter.setOnPicSelCallBack(onPicSelCallBack);
		mGirdView.setAdapter(mAdapter);
		// mImageCount.setText(totalCount + "张");
	};

	/**
	 * 初始化展示文件夹的popupWindw
	 */
	private void initListDirPopupWindw() {
		View view = LayoutInflater.from(getApplicationContext()).inflate(
				R.layout.com_mark_utils_picsel_list_dir, null);
		mListImageDirPopupWindow = new ListImageDirPopupWindow(
				LayoutParams.MATCH_PARENT, (int) (mScreenHeight - dip2px(this,
				110)), mImageFloders, view);

		mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1.0f;
				getWindow().setAttributes(lp);
			}
		});
		// 设置选择文件夹的回调
		mListImageDirPopupWindow.setOnImageDirSelected(this);
		view.findViewById(R.id.id_cancel).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						mListImageDirPopupWindow.dismiss();
					}
				});
	}

	public static File getSDCardDir(Context context, String dirName) {
		MyAdapter.FileUtils fileUtils = new MyAdapter.FileUtils();
		if (!fileUtils.isSDCardMounted()) {
			return context.getFilesDir();
		}
		if (!fileUtils.isDirectory(fileUtils.getSDPATH() + dirName)) {
			fileUtils.createSDDir(dirName);
		}
		return new File(fileUtils.getSDPATH() + dirName);
	}

	public static PicSelActivity getInstance() {
		return instance;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		instance = null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		instance = this;
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		int Select_Img_Size = getIntent().getIntExtra("Select_Img_Size",0);
		MAX_SEL_ENABLE = Select_Img_Size;
		setContentView(R.layout.com_mark_utils_picsel);

		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		mScreenHeight = outMetrics.heightPixels;
		int mScreenWidth = outMetrics.widthPixels;

		Window window = getWindow();
		WindowManager.LayoutParams layoutParams = window.getAttributes();
		//设置窗口的大小及透明度

		layoutParams.width = mScreenWidth-50;
		layoutParams.height = mScreenHeight-50;

		window.setAttributes(layoutParams);

		ArrayList<String> pics = getIntent().getStringArrayListExtra(
				RESULT_PICS_ARRAY);
		if (pics != null) {
			if (MyAdapter.mSelectedImage ==null) {
				MyAdapter.mSelectedImage = new HashSet<String>();
			}
			MyAdapter.mSelectedImage.clear();
			MyAdapter.mSelectedImage.addAll(pics);

		} else {
			if (MyAdapter.mSelectedImage != null) {
				MyAdapter.mSelectedImage.clear();
			}
		}



		initView();
		getImages();
		initEvent();

		onSel(MyAdapter.mSelectedImage);
	}

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
	 */
	private void getImages() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		// 显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

		new Thread(new Runnable() {
			@Override
			public void run() {

				String firstImage = null;

				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = PicSelActivity.this
						.getContentResolver();

				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);

				Log.e("TAG", mCursor.getCount() + "");
				while (mCursor.moveToNext()) {
					// 获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));

					Log.e("TAG", path);
					// 拿到第一张图片的路径
					if (firstImage == null)
						firstImage = path;
					// 获取该图片的父路径名
					File parentFile = new File(path).getParentFile();
					if (parentFile == null)
						continue;
					String dirPath = parentFile.getAbsolutePath();
					ImageFloder imageFloder = null;
					// 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
					if (mDirPaths.contains(dirPath)) {
						continue;
					} else {
						mDirPaths.add(dirPath);
						// 初始化imageFloder
						imageFloder = new ImageFloder();
						imageFloder.setDir(dirPath);
						imageFloder.setFirstImagePath(path);
					}

					int picSize = parentFile.list(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String filename) {
							if (filename.endsWith(".jpg")
									|| filename.endsWith(".png")
									|| filename.endsWith(".jpeg"))
								return true;
							return false;
						}
					}).length;
					totalCount += picSize;

					imageFloder.setCount(picSize);
					mImageFloders.add(imageFloder);

					if (picSize > mPicsSize) {
						mPicsSize = picSize;
						mImgDir = parentFile;
					}
				}
				mCursor.close();

				// 扫描完成，辅助的HashSet也就可以释放内存了
				mDirPaths = null;

				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(0x110);

			}
		}).start();

	}

	/**
	 * 初始化View
	 */
	private void initView() {
		LinearLayout ll_rootview = (LinearLayout) findViewById(R.id.ll_rootview);
		Window window = getWindow();

		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyless);
		mGirdView = (GridView) findViewById(R.id.id_gridView);

		ly_bt_content = (LinearLayout) findViewById(R.id.ly_bt_content);
		id_sel_count = (TextView) findViewById(R.id.id_sel_count);
		id_sel_count.setText("0/" + MAX_SEL_ENABLE);
		findViewById(R.id.id_cancel).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						finish();
					}
				});
		findViewById(R.id.id_ok).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent data = new Intent();
				ArrayList<String> tmpArrayList=new ArrayList<String>();
				if (MyAdapter.mSelectedImage!=null) {
					tmpArrayList.addAll(MyAdapter.mSelectedImage);
				}
				if (MyAdapter.mSelectedImage.size() > 0) {
					data.putExtra(RESULT_PICS_ARRAY, tmpArrayList);
				}
				setResult(RESULT_PICS_OK_CODE, data);
				BMessage msg = new BMessage(BMessage.Tage_Select_Pic);
				msg.setTmpArrayList(tmpArrayList);
				EventBus.getDefault().post(msg);
				finish();
			}
		});

	}


	private void initEvent() {
		ly_top = findViewById(R.id.ly_top);
		/**
		 * 为底部的布局设置点击事件，弹出popupWindow
		 */

		findViewById(R.id.iv_camera).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (MyAdapter.mSelectedImage.size()>=PicSelActivity.MAX_SEL_ENABLE) {
					Toast.makeText(getApplicationContext(), "图片已经超过"+PicSelActivity.MAX_SEL_ENABLE+"张", Toast.LENGTH_SHORT).show();
					return;
				}

				Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
				File tempFile=PicSelActivity.getInstance().getTmpFile();
				if (tempFile!=null) {
					getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(tempFile));
				}


				PicSelActivity.getInstance().startActivityForResult(getImageByCamera, PicSelActivity.REQUEST_PICS_CODE);
			}

		});
//		findViewById(R.id.sel_pic).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				mListImageDirPopupWindow
//						.setAnimationStyle(R.style.anim_popup_dir);
//				mListImageDirPopupWindow.showAsDropDown(ly_top, 0, 0);
//
//				// 设置背景颜色变暗
//				WindowManager.LayoutParams lp = getWindow().getAttributes();
//				lp.alpha = .3f;
//				getWindow().setAttributes(lp);
//			}
//		});
	}

	@Override
	public void selected(ImageFloder floder) {

		mImgDir = new File(floder.getDir());
		mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if (filename.endsWith(".jpg") || filename.endsWith(".png")
						|| filename.endsWith(".jpeg"))
					return true;
				return false;
			}
		}));
		/**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		 */
		mAdapter = new MyAdapter(getApplicationContext(), mImgs,
				R.layout.com_mark_utils_picsel_grid_item,
				mImgDir.getAbsolutePath());
		mAdapter.setOnPicSelCallBack(onPicSelCallBack);
		mGirdView.setAdapter(mAdapter);
		// mAdapter.notifyDataSetChanged();
		// mImageCount.setText(floder.getCount() + "张");
		// mChooseDir.setText(floder.getName());
		mListImageDirPopupWindow.dismiss();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK
				&& requestCode == PicSelActivity.REQUEST_PICS_CODE) {
			if (tmpPath!=null) {
				Toast.makeText(this,tmpPath, Toast.LENGTH_SHORT).show();
				MyAdapter.mSelectedImage.add(tmpPath);
				onSel(MyAdapter.mSelectedImage);
			}
		}
	}

	int count;
	public File getTmpFile() {
		File dir = getSDCardDir(this, "WtApp");
		if (dir != null) {
			tmpFile = new File(dir, StrUtils.UploadQNName("selpic"));//"tmp.jpg"+count
			if (tmpFile != null) {
				tmpPath = tmpFile.getAbsolutePath();
				count++;
				if (count>MAX_SEL_ENABLE) {
					count=0;
				}
				return tmpFile;
			}
		}
		return null;
	}

	public static Bitmap comPressBitmap(String path, int w, int h) {
		Bitmap tmpBitmap = null;
		try {
			tmpBitmap = createNewBitmapAndCompressByFile(path,
					new int[] { w, h });
		} catch (RuntimeException e) {
		} catch (Exception e) {
		}
		return tmpBitmap;
	}

	public static Bitmap createNewBitmapAndCompressByFile(String filePath,
														  int wh[]) {
		int offset = 100;
		File file = new File(filePath);
		int raye = readPictureDegree(filePath);

		long fileSize = file.length();
		if (200 * 1024 < fileSize && fileSize <= 1024 * 1024)
			offset = 90;
		else if (1024 * 1024 < fileSize)
			offset = 85;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 为true里只读图片的信息，如果长宽，返回的bitmap为null
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inDither = false;
		/**
		 * 计算图片尺寸 //TODO 按比例缩放尺寸
		 */
		BitmapFactory.decodeFile(filePath, options);

		int bmpheight = options.outHeight;
		int bmpWidth = options.outWidth;
		int inSampleSize = bmpheight / wh[1] > bmpWidth / wh[0] ? bmpheight
				/ wh[1] : bmpWidth / wh[0];
		// if(bmpheight / wh[1] < bmpWidth / wh[0]) inSampleSize = inSampleSize
		// * 2 / 3;//TODO 如果图片太宽而高度太小，则压缩比例太大。所以乘以2/3
		if (inSampleSize > 1)
			options.inSampleSize = inSampleSize;// 设置缩放比例
		options.inJustDecodeBounds = false;

		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			return null;
		}
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(is, null, options);
		} catch (OutOfMemoryError e) {
			System.gc();
			bitmap = null;
		}

		if (offset == 100)
			return bitmap;// 缩小质量
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		if (bitmap != null) {
			bitmap.compress(Bitmap.CompressFormat.JPEG, offset, baos);
		}
		byte[] buffer = baos.toByteArray();
		options = null;
		if (buffer.length >= fileSize)
			return bitmap;
		try {
			return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (raye != 0) {
			bitmap = rotaingImageView(-raye, bitmap);
		}
		return bitmap;
	}

	/**
	 * @desc <pre>
	 * 旋转图片
	 * </pre>
	 * @author Weiliang Hu
	 * @date 2013-9-18
	 * @param angle
	 * @param bitmap
	 * @return
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		bitmap.recycle();
		return resizedBitmap;
	}

	/**
	 * @desc <pre>
	 * 获取图片旋转的角度
	 * </pre>
	 * @author Weiliang Hu
	 * @date 2013-9-18
	 * @param path
	 * @return
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

}
