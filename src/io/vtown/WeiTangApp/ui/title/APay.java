package io.vtown.WeiTangApp.ui.title;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.contant.PayResult;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.AddAndSubView;
import io.vtown.WeiTangApp.comment.view.AddAndSubView.OnNumChangeListener;
import io.vtown.WeiTangApp.comment.view.pop.PAddSelect;
import io.vtown.WeiTangApp.comment.view.pop.PAddSelect.AddSelectInterface;
import io.vtown.WeiTangApp.comment.view.pop.PPassWord;
import io.vtown.WeiTangApp.event.interf.OnPasswordInputFinish;
import io.vtown.WeiTangApp.ui.ALoading;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.afragment.ASellStatistics;
import io.vtown.WeiTangApp.ui.comment.AVidemplay;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;
import io.vtown.WeiTangApp.ui.comment.recordervido.ARecoderVido;
import io.vtown.WeiTangApp.ui.title.loginregist.ALogin;
import io.vtown.WeiTangApp.ui.ui.CaptureActivity;
import io.vtown.WeiTangApp.wxapi.AH5Pay;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-10 下午4:30:54
 * 
 */
public class APay extends ATitleBase implements PlatformActionListener {
	private Button ddddwwww, ttt, ssduotu, loadinggg, asw, address, teststring,
			bbaaaddd, bbaaa, bbaab, bbaa, bba, bba2, bba3, bba4, bba5, bba6,
			bba7, bba8, bba9, bbaa1, bbaa2, bbaa3, be_qrcode;

	private IWXAPI msgApi;
	private View BVivew;
	private AddAndSubView testadd;
	private String PayStr = "body=商品名称&input_charset=UTF-8&it_b_pay=1h&notify_url=https://devpay.v-town.cc/v1/pay/order/alipay-callback&out_trade_no=P201605235410250560&partner=2088021707391422&payment_type=1&seller_id=zhifu@v-town.cc&service=mobile.securitypay.pay&subject=商品名称&total_fee=0.1&sign_type=RSA&sign=BxBOQ75b4TMuinzjfzJeb9iLP5CNXScclKVgXQpDUTNjwNwxl2KJAj2U1Yy%2BLVRpRZMwoosilvV8G2MDii%2Bu6DNBI0edwzXCSNPCEFiNp6pQQAwLYiCb3%2BLva3BXyx9TUUlK1mmv9aeiiNQ65DJ9WCoJ09WfOUYiS5wDbTKv1lQ%3D";
	private TextView assss, vvvvs;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_pay);
		BVivew = LayoutInflater.from(this).inflate(R.layout.activity_pay, null);
		ShareSDK.initSDK(BaseContext);
		msgApi = WXAPIFactory.createWXAPI(BaseContext, null);

		// 将该app注册到微信
		msgApi.registerApp("wxd930ea5d5a258f4f");
		bbaaa = (Button) findViewById(R.id.bbaaa);
		bbaaa.setOnClickListener(this);

		ssduotu = (Button) findViewById(R.id.ssduotu);
		ssduotu.setOnClickListener(this);

		bbaab = (Button) findViewById(R.id.bbaab);
		bbaab.setOnClickListener(this);
		bbaa = (Button) findViewById(R.id.bbaa);
		bbaa.setOnClickListener(this);
		bba = (Button) findViewById(R.id.bba);
		bba.setOnClickListener(this);
		bba2 = (Button) findViewById(R.id.bba2);
		bba2.setOnClickListener(this);
		bba3 = (Button) findViewById(R.id.bba3);
		bba3.setOnClickListener(this);
		bba4 = (Button) findViewById(R.id.bba4);
		bba4.setOnClickListener(this);
		bba5 = (Button) findViewById(R.id.bba5);
		bba5.setOnClickListener(this);

		bba6 = (Button) findViewById(R.id.bba6);
		bba6.setOnClickListener(this);
		bba7 = (Button) findViewById(R.id.bba7);
		bba7.setOnClickListener(this);
		bba8 = (Button) findViewById(R.id.bba8);
		bba8.setOnClickListener(this);
		bba9 = (Button) findViewById(R.id.bba9);
		bba9.setOnClickListener(this);
		bbaa1 = (Button) findViewById(R.id.bbaa1);
		bbaa1.setOnClickListener(this);

		bbaa2 = (Button) findViewById(R.id.bbaa2);
		bbaa2.setOnClickListener(this);
		bbaa3 = (Button) findViewById(R.id.bbaa3);
		bbaa3.setOnClickListener(this);

		ttt = (Button) findViewById(R.id.ttt);
		ttt.setOnClickListener(this);

		ddddwwww = (Button) findViewById(R.id.ddddwwww);
		ddddwwww.setOnClickListener(this);

		bbaaaddd = (Button) findViewById(R.id.bbaaaddd);
		bbaaaddd.setOnClickListener(this);

		address = (Button) findViewById(R.id.address);
		address.setOnClickListener(this);
		// 销售统计
		asw = (Button) findViewById(R.id.asw);
		asw.setOnClickListener(this);

		loadinggg = (Button) findViewById(R.id.loadinggg);
		loadinggg.setOnClickListener(this);

		teststring = (Button) findViewById(R.id.teststring);
		teststring.setOnClickListener(this);
		assss = (TextView) findViewById(R.id.assss);

		be_qrcode = (Button) findViewById(R.id.be_qrcode);
		be_qrcode.setOnClickListener(this);

		vvvvs = (TextView) findViewById(R.id.vvvvs);
		StrUtils.SetTxt(vvvvs, "用户信息=>"
				+ Spuit.User_Get(BaseContext).toString());

		ITestAdd();
	}

	private void ITestAdd() {
		testadd = (AddAndSubView) findViewById(R.id.testadd);

		testadd.setOnNumChangeListener(new OnNumChangeListener() {
			@Override
			public void onNumChange(View view, int num) {
				PromptManager.ShowCustomToast(BaseContext, "当前数值" + num);

				// testadd.setNum(2);//设置数据 TODO如果大雨最大值MAX 则手动设置成Max
			}
		});
	}

	private void shareMultiplePictureToTimeLine(File... files) {
		Intent intent = new Intent();
		ComponentName comp = new ComponentName("com.tencent.mm",
				"com.tencent.mm.ui.tools.ShareToTimeLineUI");
		intent.setComponent(comp);
		intent.setAction(Intent.ACTION_SEND_MULTIPLE);
		intent.setType("image/*");
		intent.putExtra("Kdescription", "sss");
		ArrayList<Uri> imageUris = new ArrayList<Uri>();
		for (File f : files) {
			imageUris.add(Uri.fromFile(f));
		}
		intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);

		startActivity(intent);
	}

	private void Share9() {

		// 遍历 SD 卡下 .png 文件通过微信分享
		File root = Environment.getExternalStorageDirectory();
		File[] files = root.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().endsWith(".png")
						|| pathname.getName().endsWith(".JPG"))
					return true;
				return false;
			}
		});
		shareMultiplePictureToTimeLine(files);
	}

	// private void Share99() {
	// Intent intent = new Intent();
	// ComponentName comp = new ComponentName("com.tencent.mm",
	// "com.tencent.mm.ui.tools.ShareToTimeLineUI");
	// intent.setComponent(comp);
	// intent.setAction(Intent.ACTION_SEND_MULTIPLE);
	// intent.setType("image/*");
	// intent.putExtra("Kdescription", title);
	// ArrayList<Uri> imageUris = new ArrayList<Uri>();
	// for (File f : files) {
	// imageUris.add(Uri.fromFile(f));
	// }
	// intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
	// startActivity(intent);
	// }

	private void Pay() {
		PayReq request = new PayReq();

		request.appId = "wxf104565ec7418036";

		request.partnerId = "1236552602";

		request.prepayId = "wx20160510164516355b8aeeff0147105749";

		request.packageValue = "Sign=WXPay";

		request.nonceStr = "61186458c9f9137";

		request.timeStamp = "1462869916";

		request.sign = "FDDF17B159F3BF2CEDD07320E7ABD336";

		msgApi.sendReq(request);

	}

	@Override
	protected void InitTile() {
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

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int auth_statu = msg.what;

			Platform platform = (Platform) msg.obj;
			final String nickname = platform.getDb().getUserName();
			final String userid = platform.getDb().getUserId();
			String usericon = platform.getDb().getUserIcon();
			String platformName = platform.getName();
//			Toast.makeText(
//					BaseContext,
//					String.format("昵称%s;   id是%s;   名字是%s", nickname, userid,
//							platformName), 10*1000).show();

		}
	};

	@Override
	protected void MyClick(View V) {
		switch (V.getId()) {
		case R.id.ddddwwww:

			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					ARecoderVido.class));

			break;
		case R.id.ttt:
			Share9();
			break;
		case R.id.ssduotu:


			break;
		case R.id.loadinggg:// 引导页
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					ALoading.class));
			break;
		case R.id.asw:// 销售统计
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					ASellStatistics.class));
			break;
		case R.id.teststring://

			StrUtils.SetColorsTxt(BaseContext, assss, R.color.app_fen, "运费:",
					"  40元");
			break;
		case R.id.bbaaaddd:// 登陆注销
			Spuit.Login_Out(BaseContext);
			if (Spuit.IsLogin_RenZheng_Set(BaseContext)) {
				Spuit.IsLogin_RenZheng_Save(BaseContext, "", "");
			}
			PromptManager.ShowCustomToast(BaseContext, "注销成功");
			break;
		case R.id.bbaaa:
			ToLogin();
			break;

		case R.id.bbaab:
			ToImagPage();
			break;
		case R.id.bbaa:
			break;
		case R.id.bba:// 支付
			Pay();
			break;
		case R.id.bba2:// 好友
			Share(1);
			break;
		case R.id.bba3:// 朋友圈
			Share(2);
			break;
		case R.id.bba4:// 三方
			Login(1);
			break;
		case R.id.bba5:// 三方
			ShowP(BVivew,null);
			break;

		case R.id.bba6:// 三方
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					CaptureActivity.class));

			// 启动拍摄的Activity
			// Intent intent = new Intent(this,AVideoPlayerView.class);
			// PromptManager.SkipResultActivity(BaseActivity,intent,200);
			break;
		case R.id.bba7:
			UpdaIvTest();
			break;
		case R.id.bba8:// 三方
			VideoPlay();
			break;
		case R.id.bba9:// 三方
			ShowDialog();
			break;
		case R.id.bbaa1:// 支付宝客户端
			AliResultPay(PayStr);
			break;
		case R.id.bbaa2:// 支付宝网页
			AliPayH5();
			break;
		case R.id.bbaa3:// 支付密码
			PassView();
			break;
		case R.id.address:// 收货地址
			Address();
			break;

		case R.id.be_qrcode:// 生成二维码
			BeQRCode();
		default:
			break;
		}
	}

	/**
	 * 生成二维码
	 */
	private void BeQRCode() {
		PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
				ABeQRCode.class));
	}

	private void Address() {
		final PAddSelect m = new PAddSelect(BaseContext,false);

		m.GetAddressResult(new AddSelectInterface() {

			@Override
			public void GetAddResult(String ProviceName, String CityName,
					String DistrictName, String ZipCode) {
				PromptManager.ShowCustomToast(BaseContext, "当前选中:"
						+ ProviceName + "," + CityName + "," + DistrictName
						+ ", 邮编" + ZipCode);
				m.dismiss();
			}
		});
		m.showAtLocation(BVivew, Gravity.BOTTOM, 0, 0);
	}

	/**
	 * 跳转登陆界面
	 */
	private void ToLogin() {
		if (Spuit.IsLogin_Get(BaseContext)) {
			PromptManager.ShowCustomToast(BaseContext, "登录过");
		} else
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					ALogin.class));

	}

	private void ToImagPage() {
		String[] s = {
				"http://img2.imgtn.bdimg.com/it/u=1402463900,4094276086&fm=21&gp=0.jpg",
				"http://epaper.syd.com.cn/sywb/res/1/20070627/66211182882864515.jpg",
				"http://img5.imgtn.bdimg.com/it/u=3906640903,4114141898&fm=21&gp=0.jpg" };

		Intent mIntent = new Intent(BaseContext, AphotoPager.class);
		mIntent.putExtra("position", 2);
		mIntent.putExtra("urls", s);

		PromptManager.SkipActivity(BaseActivity, mIntent);
	}

	/**
	 * 密码控件
	 */
	private void PassView() {
		final PPassWord p = new PPassWord(BaseContext, screenWidth, "test");
		p.setOnPassWordListener(new OnPasswordInputFinish() {

			@Override
			public void inputFinish(String getStrPassword) {
				PromptManager.ShowCustomToast(BaseContext, getStrPassword);
			}

			@Override
			public void LostPassWord() {
				PromptManager.ShowCustomToast(BaseContext, "忘记密码怎么办");
			}

			@Override
			public void Cancle() {
				p.dismiss();
			}
		});
		p.showAtLocation(BVivew, Gravity.CENTER, 0, 0);
	}

	/**
	 * 支付宝H5支付
	 */
	private void AliPayH5() {

		String url = "https://devpay.v-town.cc/v1/pay/order/alipay-callback";

		Intent intent = new Intent(this, AH5Pay.class);
		Bundle extras = new Bundle();
		/**
		 * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
		 * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
		 * 商户可以根据自己的需求来实现
		 */
		// url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
		extras.putString("url", url);
		intent.putExtras(extras);
		startActivity(intent);
	}

	private Handler PayHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: {
				PayResult payResult = new PayResult((String) msg.obj);
				/**
				 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
				 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
				 * docType=1) 建议商户依赖异步通知
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息

				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(BaseContext, "支付成功", Toast.LENGTH_SHORT)
							.show();
				} else {
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(BaseContext, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(BaseContext, "支付失败", Toast.LENGTH_SHORT)
								.show();

					}
				}
				break;
			}
			default:
				break;
			}
		};
	};

	/**
	 * 支付宝获取到数据后进行的操作
	 */
	private void AliResultPay(final String Singe) {

		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(BaseActivity);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(Singe, true);
				Message msg = new Message();
				msg.what = 1;
				msg.obj = result;
				PayHandler.sendMessage(msg);
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * 弹出自定义dialog
	 */
	private void ShowDialog() {

	}

	/**
	 * 视频播放
	 */
	private void VideoPlay() {
		PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
				AVidemplay.class));
	}

	private void Login(int aa) {
		if (1 == aa) {

			Platform w22eibo = ShareSDK.getPlatform(BaseContext, Wechat.NAME);// Wechat.NAME);
			// weibo.SSOSetting(false);//
			// 此处设置为false，则在优先采用客户端授权的方法，设置true会采用网页方式
			w22eibo.showUser(null);// 获得用户数据
			w22eibo.setPlatformActionListener(this);
			w22eibo.authorize();
		}
		// if (2 == aa) {
		// ShareSDK.initSDK(BaseContext);
		// Platform weibo = ShareSDK.getPlatform(BaseContext, Q);
		// // weibo.SSOSetting(false);//
		// // 此处设置为false，则在优先采用客户端授权的方法，设置true会采用网页方式
		// // weibo.showUser(null);// 获得用户数据
		// weibo.setPlatformActionListener(this);
		// weibo.authorize();
		// }

	}

	private void Share(int Type) {
		ShareSDK.initSDK(BaseContext);
		Platform platform = null;
		ShareParams sp = new ShareParams();
		switch (Type) {
		case 1:// 好友分享
			platform = ShareSDK.getPlatform(BaseContext, Wechat.NAME);
			sp.setShareType(Platform.SHARE_WEBPAGE);

			sp.setText("大兔兔的测试数据");

			sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);
			// String[] da = {
			// "http://blog.china.com/u/060919/10980/pic/11650709521171.JPG",
			// "http://blog.china.com/u/060919/10980/pic/11650709521171.JPG",
			// "http://blog.china.com/u/060919/10980/pic/11650709521171.JPG",
			// "http://p0.so.qhimg.com/t01bc712920228b4daf.jpg",
			// "http://p0.so.qhimg.com/t01bc712920228b4daf.jpg",
			// "http://blog.china.com/u/060919/10980/pic/11650709521171.JPG",
			// "http://blog.china.com/u/060919/10980/pic/11650709521171.JPG",
			// "http://blog.china.com/u/060919/10980/pic/11650709521171.JPG",
			// "http://img9.3lian.com/c1/vector/10/05/093.jpg" };
			// sp.setImageArray(da);
			sp.setImageUrl("http://static.freepik.com/free-photo/letter-a-underlined_318-8682.jpg");
			sp.setTitle("大兔兔的title");//
			sp.setUrl("www.baidu.com");
			break;
		case 2:// 朋友圈分享
			platform = ShareSDK.getPlatform(BaseContext, WechatMoments.NAME);
			sp.setShareType(Platform.SHARE_IMAGE);// SHARE_WEBPAGE);
			// String[] das = {
			// "http://static.freepik.com/free-photo/letter-a-underlined_318-8682.jpg",
			// "http://p0.so.qhimg.com/t01bc712920228b4daf.jpg",
			// "http://p0.so.qhimg.com/t01bc712920228b4daf.jpg",
			// "http://p0.so.qhimg.com/t01bc712920228b4daf.jpg",
			// "http://p0.so.qhimg.com/t01bc712920228b4daf.jpg",
			// "http://img.juimg.com/tuku/yulantu/140121/328274-140121150F098.jpg",
			// "http://pic18.nipic.com/20111207/6608733_144956759000_2.jpg",
			// "http://img.sootuu.com/vector/200801/072/0556.jpg",
			// "http://img9.3lian.com/c1/vector/10/05/093.jpg" };
			// sp.setImageArray(das);
			sp.setText("大兔兔的测试数据");
			sp.setImageUrl("http://static.freepik.com/free-photo/letter-a-underlined_318-8682.jpg");
			sp.setTitle("大兔兔的测试数据");//
			sp.setUrl("www.baidu.com");
			break;
		default:
			break;
		}
		platform.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				PromptManager.ShowCustomToast(BaseContext, "onError错误");
			}

			@Override
			public void onComplete(Platform arg0, int arg1,
					HashMap<String, Object> arg2) {
				PromptManager.ShowCustomToast(BaseContext, "onComplete完成");
			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
			}
		});
		platform.share(sp);
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		Message message = new Message();
		message.obj = arg0;
		message.what = 3;
		mHandler.sendMessage(message);
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		Message message = new Message();
		message.obj = arg0;
		message.what = 1;
		mHandler.sendMessage(message);
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		Message message = new Message();
		message.obj = arg0;
		message.what = 0;
		mHandler.sendMessage(message);
	}

	// 图片上传

	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 进行图片上传操作
	 */
	public void UpdaIvTest() {

		// File aa = null;
		//
		// NUpLoadUtils dLoadUtils = new NUpLoadUtils(BaseContext,aa,
		// "aa.jpg");
		// dLoadUtils.SetUpResult(new UpResult() {
		//
		// @Override
		// public void Progress(String arg0, double arg1) {
		// }
		//
		// @Override
		// public void Onerror() {
		// PromptManager.closeLoading();
		// }
		//
		// @Override
		// public void Complete(String Url) {
		// PromptManager.closeLoading();
		// LogUtils.i(Url);
		// PromptManager
		// .ShowCustomToast(BaseContext, "图片上传成功图片URL：" + Url);
		//
		// }
		// });
		// PromptManager.showLoading(BaseContext);
		// dLoadUtils.UpLoad();
	}
}
