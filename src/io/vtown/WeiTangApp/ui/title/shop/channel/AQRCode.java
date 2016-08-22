package io.vtown.WeiTangApp.ui.title.shop.channel;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils.UpResult;
import io.vtown.WeiTangApp.comment.selectpic.util.Bimp;
import io.vtown.WeiTangApp.comment.util.QRCodeUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.pop.PShare;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.alibaba.fastjson.JSON;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-16 下午1:07:09 点击成生二维码后的展示二维码页面
 */
public class AQRCode extends ATitleBase {

	/**
	 * 二维码
	 */
	private ImageView iv_two_dimension_code;
	/**
	 * 分享按钮
	 */
	private Button btn_share_qrcode;
	/**
	 * 保存相册按钮
	 */
	private Button btn_save_gallery;

	private String codeBean;

	// 保存生成二维码的路径
	private String CreateQCodePath;

	private BNew codeQBNew;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_build_two_dimension_code);
		IBund();
		IView();
		// BeQRCode(); //之前需求 需要通过UEL下载图片并展示该二维码图片

		CreateQCodePath = getFileRoot(BaseContext) + File.separator + "qr_"
				+ System.currentTimeMillis() + ".jpg";

		CreatQ(codeQBNew.getQrcode(), CreateQCodePath);
		// 偷偷上传七牛

	}

	private void UpPic(final String createQCodePath2) {
		NUpLoadUtils dLoadUtils = new NUpLoadUtils(BaseContext,
				Bitmap2Bytes(StrUtils.GetBitMapFromPath(createQCodePath2)),
				StrUtils.UploadQNName("photo"));
		dLoadUtils.SetUpResult(new UpResult() {

			@Override
			public void Progress(String arg0, double arg1) {
			}

			@Override
			public void Onerror() {
				UpPic(createQCodePath2);
			}

			@Override
			public void Complete(String HostUrl, String Url) {
				codeQBNew.setShare_log(HostUrl);
				ShowPsssss(
						LayoutInflater.from(BaseContext).inflate(
								R.layout.activity_build_two_dimension_code,
								null), codeQBNew);
			}
		});
		dLoadUtils.UpLoad();
	}

	private void IBund() {
		if (getIntent().getExtras() == null
				|| !getIntent().getExtras().containsKey("codeBean"))
			BaseActivity.finish();
		codeBean = getIntent().getStringExtra("codeBean");

		if (codeBean == null)
			return;

		codeQBNew = JSON.parseObject(codeBean, BNew.class);

	}

	/**
	 * 微信分享弹出框 BVivew 代表目前activity的view
	 */
	protected void ShowPsssss(View BVivew, BNew mBNew) {
		PShare da = new PShare(BaseContext, mBNew);
		da.setIsErWeiMaShare(true);
		da.showAtLocation(BVivew, Gravity.BOTTOM, 0, 0);
	}

//	private void BeQRCode() {
//		final String filePath = getFileRoot(BaseContext) + File.separator
//				+ "qr_" + System.currentTimeMillis() + ".jpg";
//		new Thread(new Runnable() {
//			@Override
//			public void run() {
//				boolean success = QRCodeUtil.createQRImage(codeBean, 800, 800,
//						null, filePath);
//
//				if (success) {
//					runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							iv_two_dimension_code.setImageBitmap(BitmapFactory
//									.decodeFile(filePath));
//
//						}
//					});
//				}
//			}
//		}).start();
//	}

//	// 文件存储根目录
//	private String getFileRoot(Context context) {
//		if (Environment.getExternalStorageState().equals(
//				Environment.MEDIA_MOUNTED)) {
//			File external = context.getExternalFilesDir(null);
//			if (external != null) {
//				return external.getAbsolutePath();
//			}
//		}
//
//		return context.getFilesDir().getAbsolutePath() + File.separator
//				+ "good" + File.separator + "savePic";
//	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.develop_junior));
	}

	private void IView() {
		iv_two_dimension_code = (ImageView) findViewById(R.id.iv_two_dimension_code);
		btn_share_qrcode = (Button) findViewById(R.id.btn_share_qrcode);
		btn_save_gallery = (Button) findViewById(R.id.btn_save_gallery);

		btn_share_qrcode.setOnClickListener(this);
		btn_save_gallery.setOnClickListener(this);
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
	}

	@Override
	protected void DataError(String error, int LoadTyp) {
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

	private void CreatQ(final String Str, final String Pathe) {
		BUser user_Get = Spuit.User_Get(BaseActivity);
//		final String filePath = getFileRoot(BaseContext) + File.separator
//				+ "qr_mycode" + user_Get.getSeller_id() + ".jpg";
		File mFile = new File(Pathe);
		if (mFile.exists()) {
			// 如果存在就直接显示
			iv_two_dimension_code.setImageBitmap(BitmapFactory.decodeFile(Pathe));
			return;
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				String avatar = Spuit.Shop_Get(BaseContext).getAvatar();
 				Bitmap logoBm = com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImageSync(avatar);
				boolean success = QRCodeUtil.createQRImage(Str, 800, 800, logoBm,
						Pathe);

				if (success) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							iv_two_dimension_code.setImageBitmap(BitmapFactory
									.decodeFile(Pathe));
							btn_share_qrcode.setVisibility(View.VISIBLE);
							btn_save_gallery.setVisibility(View.VISIBLE);
						}
					});
				}
			}
		}).start();

	}

	@Override
	protected void MyClick(View V) {
		switch (V.getId()) {
		case R.id.btn_share_qrcode:
			if (!Constants.isWeixinAvilible(BaseContext)) {
				PromptManager.ShowCustomToast(BaseContext, "未安装微信");
				return;
			}
			// 分享前线传给七牛
			UpPic(CreateQCodePath);

			break;

		case R.id.btn_save_gallery:
			saveToGallery("QRcode.png");
			break;

		}
	}

	/**
	 * 将二维码保存到本地相册
	 * 
	 * @param fileName
	 */
	private void saveToGallery(String fileName) {
		Drawable drawable = iv_two_dimension_code.getDrawable();
		if (drawable != null) {
			BitmapDrawable bd = (BitmapDrawable) drawable;
			Bitmap bm = bd.getBitmap();
			String subForder = getFileRoot(BaseContext);
			File foder = new File(subForder);
			if (!foder.exists()) {
				foder.mkdirs();
			}
			File myCaptureFile = new File(subForder, fileName);
			if (!myCaptureFile.exists()) {
				BufferedOutputStream bos = null;
				try {
					myCaptureFile.createNewFile();
					bos = new BufferedOutputStream(new FileOutputStream(
							myCaptureFile));
					bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
					bos.flush();
					Toast.makeText(BaseContext, "保存成功!", Toast.LENGTH_SHORT)
							.show();
					// 发广播通知更新图库
					Intent intent = new Intent(
							Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
					Uri uri = Uri.fromFile(myCaptureFile);
					intent.setData(uri);
					sendBroadcast(intent);
				} catch (Exception e) {
					Toast.makeText(BaseContext, "保存失败!", Toast.LENGTH_SHORT)
							.show();
					e.printStackTrace();
				} finally {
					try {
						if (bos != null) {
							bos.close();
						}

					} catch (Exception e) {

						e.printStackTrace();
					}
				}

			}
		}
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

}
