package io.vtown.WeiTangApp.ui.title.shop.addgood;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.util.LogUtils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.multiphoto.model.ImageItem1;
import io.vtown.WeiTangApp.comment.multiphoto.util.CustomConstants;
import io.vtown.WeiTangApp.comment.net.qiniu.NUPLoadUtil;
import io.vtown.WeiTangApp.comment.net.qiniu.NUPLoadUtil.UpResult1;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils.UpResult;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.ProcessImageView;
import io.vtown.WeiTangApp.event.interf.IBottomDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.multiphotopicker.APublicGood.PopupWindows;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-6-14 下午4:36:37 商品描述页面
 * 
 */
public class AEditGoodDesc extends ATitleBase {
	public final static int Tag_GoodDesc = 231;

	private View BaseView;
	private ListView addgood_miaoshu_ls;
	private MyDescAp myDescAp;
	private LinearLayout ll_brand_list_foot;
	/**
	 * 添加 按钮
	 */
	private ImageView iv_add_brand_item;
	private TextView tv_add_brand_item;
	/* 选择图片的路径 */
	private File tempFiles;
	/* 保存拍照图片的路径 */
	private String path = "";
	public static List<ImageItem1> mDataList = new ArrayList<ImageItem1>();
	// 上传图片
	private final int SUCCESS = 0;
	// 当前的进度
	private int CurrentProgress = 0;

	// 提交数据按钮
	private TextView add_good_desc_commint;

	@Override
	protected void InItBaseView() {
		BaseView = LayoutInflater.from(BaseContext).inflate(
				R.layout.activity_add_good_desc_edit, null);
		setContentView(BaseView);
		IView();
	}

	/**
	 * 初始化控件
	 */
	private void IView() {
		add_good_desc_commint = (TextView) findViewById(R.id.add_good_desc_commint);
		addgood_miaoshu_ls = (ListView) findViewById(R.id.addgood_miaoshu_ls);
		IFootView();
		myDescAp = new MyDescAp();
		addgood_miaoshu_ls.setAdapter(myDescAp);
		add_good_desc_commint.setOnClickListener(this);
	}

	private void SetImageView(ImageView iv, Bitmap b) {

		iv.setImageBitmap(b);
	}

	private void IFootView() {
		View view = LayoutInflater.from(BaseContext).inflate(
				R.layout.item_branddaili_foot, null);
		addgood_miaoshu_ls.addFooterView(view);
		iv_add_brand_item = (ImageView) view
				.findViewById(R.id.iv_add_brand_item);
		tv_add_brand_item = (TextView) view
				.findViewById(R.id.tv_add_brand_item);
		ll_brand_list_foot = (LinearLayout) view
				.findViewById(R.id.ll_brand_list_foot);
		ll_brand_list_foot.setOnClickListener(this);

	}

	@Override
	protected void InitTile() {
		SetTitleTxt("商品描述");
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (mDataList.size() < CustomConstants.MAX_IMAGE_SIZE
					&& resultCode == -1 && !TextUtils.isEmpty(path)) {
				ImageItem1 item = new ImageItem1();
				item.sourcePath = path;
				// Toast.makeText(getApplicationContext(), "file路径=>" + path,
				// 10 * 1000).show();
				mDataList.add(item);
				// item_addgood_desc_iv1.setVisibility(View.VISIBLE);
				// item_addgood_desc_iv1.setImageBitmap(BitmapFactory
				// .decodeFile(path));
				CurrentProgress = 0;
				// GetPic(BitmapFactory.decodeFile(path), path);
				GetPic(StrUtils.GetBitMapFromPath(path), path);
			}
			break;
		case TAKE_GALLY:
			Uri uri = null;
			try {
				uri = data.getData();
			} catch (Exception e) {
				LogUtils.i("sss");
				return;
			}

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
			// Toast.makeText(getApplicationContext(),
			// "file路径=>" + tempFiles.getPath(), 10 * 1000).show();
			// item_addgood_desc_iv1.setVisibility(View.VISIBLE);
			// item_addgood_desc_iv1.setImageBitmap(decodeUriAsBitmap(uri));
			CurrentProgress = 0;
			// GetPic(decodeUriAsBitmap(uri), tempFiles.getPath());
			GetPic(StrUtils.GetBitMapFromPath(picturePath), picturePath);
			break;
		}
	}

	// 返回一个bitmap后需要刷新Ap
	private void GetPic(Bitmap mBitmap, String Path) {
		myDescAp.AddPic(mBitmap, Path);
	}

	/**
	 * uri转换为bitmap
	 */
	public Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(this.getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
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
		case R.id.ll_brand_list_foot:
			if (myDescAp.getCount() == 10) {// 最多上传10张
				PromptManager.ShowCustomToast(BaseContext, "最多上传10张");
				return;
			}
			SelectPic();
			break;
		case R.id.add_good_desc_commint:// 提交数据
			// baseApplication.setAddGoosDescrtion(myDescAp.GetResource());
			setResult(Tag_GoodDesc, new Intent());
			BaseActivity.finish();

			break;
		default:
			break;
		}
	}

	private void SelectPic() {
		// new SelectPopupWindows(BaseActivity, BaseView);

		ShowBottomPop(BaseContext, BaseView, "相机", "相册",
				new IBottomDialogResult() {

					@Override
					public void SecondResult() {
						gallery();
					}

					@Override
					public void FristResult() {
						takePhoto();
					}

					@Override
					public void CancleResult() {
					}
				});

	}

	private class MyDescAp extends BaseAdapter {// item_addgood_desc
		public List<ImageItem1> mImageItems = new ArrayList<ImageItem1>();

		public void AddPic(Bitmap mBitmap, String uri) {
			mImageItems.add(new ImageItem1(uri, mBitmap));
			this.notifyDataSetChanged();
		}

		public void ReMove() {
			// mImageItems.add(new ImageItem(uri, mBitmap));
			List<ImageItem1> mImage = new ArrayList<ImageItem1>();
			for (int i = 0; i < mImageItems.size() - 1; i++) {
				mImage.add(mImageItems.get(i));
			}
			mImageItems = mImage;
			this.notifyDataSetChanged();

		}

		public List<ImageItem1> GetResource() {
			return mImageItems;
		}

		@Override
		public int getCount() {
			return mImageItems.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mImageItems.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			AddGoodDescItem mAddGoodDescItem = null;
			if (null == arg1) {
				mAddGoodDescItem = new AddGoodDescItem();
				arg1 = LayoutInflater.from(BaseContext).inflate(
						R.layout.item_addgood_desc, null);

				mAddGoodDescItem.item_addgood_desc_iv = (ProcessImageView) arg1
						.findViewById(R.id.item_addgood_desc_iv);
				arg1.setTag(mAddGoodDescItem);

			} else {
				mAddGoodDescItem = (AddGoodDescItem) arg1.getTag();
			}
			if (arg0 == mImageItems.size() - 1) {
				mAddGoodDescItem.item_addgood_desc_iv
						.setImageBitmap(mImageItems.get(arg0).getmBitmap());
				// StarrtBitMap(mAddGoodDescItem.item_addgood_desc_iv);
				UpPhone(mImageItems.get(arg0).getmBitmap(),
						mAddGoodDescItem.item_addgood_desc_iv,
						mImageItems.get(arg0).getSourcePath());
			} else {
				mAddGoodDescItem.item_addgood_desc_iv
						.setImageBitmap(mImageItems.get(arg0).getmBitmap());
			}

			return arg1;
		}

		class AddGoodDescItem {
			ProcessImageView item_addgood_desc_iv;
		}
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SUCCESS:
				PromptManager.ShowCustomToast(BaseContext, "成功");
				// item_addgood_desc_iv1.setVisibility(View.GONE);
				break;

			}
		}
	};

	private void UpPhone(Bitmap Map, final ProcessImageView VV, String NAme) {

		StarrtBitMap(VV);
		NUpLoadUtils dLoadUtils = new NUpLoadUtils(BaseContext,
				Bitmap2Bytes(Map), NAme);
		dLoadUtils.SetUpResult(new UpResult() {

			@Override
			public void Progress(String arg0, double arg1) {
				// VV.setProgress((int) arg1);
				PromptManager.ShowCustomToast(BaseContext, "当前进度" + arg1);
				ll_brand_list_foot.setClickable(false);
			}

			@Override
			public void Onerror() {
				PromptManager.ShowCustomToast(BaseContext, "上传错误");
				myDescAp.ReMove();
				ll_brand_list_foot.setClickable(true);
			}

			@Override
			public void Complete(String HostUrl, String Url) {
				ll_brand_list_foot.setClickable(true);
				CurrentProgress = 100;
				LogUtils.i(Url);
				VV.setProgress(100);
				myDescAp.GetResource().get(myDescAp.GetResource().size() - 1)
						.setThumbnailPath(HostUrl);
				PromptManager.ShowCustomToast(BaseContext, "上传完毕 ");
				// 图片上传成功需要和后台进行交互

			}
		});

		dLoadUtils.UpLoad();

	}

	/**
	 * TODO目前获取不到七牛的进度 假的进度展示用户
	 */
	private void StarrtBitMap(final ProcessImageView VV) {
		// 模拟图片上传进度
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean istrue = true;
				while (true) {
					if (CurrentProgress == 100) {
						istrue = false;
						break;
					}
					if (CurrentProgress == 90) {// 图片上传完成
						CurrentProgress = 89;
						handler.sendEmptyMessage(CurrentProgress);
						// return;
					}

					CurrentProgress++;
					VV.setProgress(CurrentProgress);
					if (CurrentProgress == 100) {
						istrue = false;
						break;
					}
					try {
						Thread.sleep(400); // 暂停0.2秒
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
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
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
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
		startActivityForResult(intent, TAKE_GALLY);

	}

	public class SelectPopupWindows extends PopupWindow {

		// public SelectPopupWindows(Context mContext, View parent) {
		//
		// View view = View.inflate(mContext,
		// R.layout.multiphoto_item_popupwindow, null);
		// view.startAnimation(AnimationUtils.loadAnimation(mContext,
		// R.anim.fade_ins));
		// LinearLayout ll_popup = (LinearLayout) view
		// .findViewById(R.id.ll_popup);
		// ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
		// R.anim.push_bottom_in_2));
		//
		// setWidth(LayoutParams.MATCH_PARENT);
		// setHeight(LayoutParams.MATCH_PARENT);
		// setFocusable(true);
		// setOutsideTouchable(true);
		// setContentView(view);
		// showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		// update();
		//
		// Button bt1 = (Button) view
		// .findViewById(R.id.item_popupwindows_camera);
		// Button bt2 = (Button) view
		// .findViewById(R.id.item_popupwindows_Photo);
		// Button bt3 = (Button) view
		// .findViewById(R.id.item_popupwindows_cancel);
		// bt1.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// takePhoto();
		// dismiss();
		// }
		// });
		// bt2.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// // Intent intent = new Intent(PublishActivity.this,
		// // ImageBucketChooseActivity.class);
		// // intent.putExtra(IntentConstants.EXTRA_CAN_ADD_IMAGE_SIZE,
		// // getAvailableSize());
		// // startActivity(intent);
		// gallery();
		// dismiss();
		// }
		// });
		// bt3.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// dismiss();
		// }
		// });
		//
		// }
	}

	private void UpIvServer() {

	}
}
