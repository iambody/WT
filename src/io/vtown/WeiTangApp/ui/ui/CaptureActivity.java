package io.vtown.WeiTangApp.ui.ui;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.bean.bcomment.news.BPay;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.NHttpBaseStr;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.zxing.ZXConfig;
import io.vtown.WeiTangApp.comment.zxing.camera.CameraManager;
import io.vtown.WeiTangApp.comment.zxing.decode.BeepManager;
import io.vtown.WeiTangApp.comment.zxing.decode.CaptureActivityHandler;
import io.vtown.WeiTangApp.comment.zxing.decode.InactivityTimer;
import io.vtown.WeiTangApp.comment.zxing.decode.ViewfinderView;
import io.vtown.WeiTangApp.comment.zxing.result.ResultHandler;
import io.vtown.WeiTangApp.comment.zxing.result.ResultHandlerFactory;
import io.vtown.WeiTangApp.event.interf.IHttpResult;
import io.vtown.WeiTangApp.ui.ABase;
import io.vtown.WeiTangApp.ui.comment.AWeb;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.center.myinvitecode.ABindCode;
import io.vtown.WeiTangApp.ui.title.mynew.ANew;
import io.vtown.WeiTangApp.ui.title.shop.channel.ABeJunior;
import io.vtown.WeiTangApp.wxapi.WXPayEntryActivity;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;

import de.greenrobot.event.EventBus;

public final class CaptureActivity extends ABase implements
		SurfaceHolder.Callback {

	private static final String TAG = CaptureActivity.class.getSimpleName();

	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private Result savedResultToShow;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Collection<BarcodeFormat> decodeFormats;
	private InactivityTimer inactivityTimer;
	private String characterSet;
	private BeepManager beepManager;

	final static String profix1 = "?appid=";
	final static String profix2 = "-title=";
	final static String action = "muzhiwan.action.detail";
	final static String bundle_key = "detail";

	private ImageView opreateView;

	private boolean IsYinLianPay;
	private String payoder;

	ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.capture);
		IsYinLianPay = getIntent().getBooleanExtra("ispay", false);
		if (IsYinLianPay) {
			payoder = getIntent().getStringExtra("payoder");
		}
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);
		opreateView = (ImageView) findViewById(R.id.button_openorcloseClick);

		opreateView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cameraManager != null) {
					ZXConfig.KEY_FRONT_LIGHT = !ZXConfig.KEY_FRONT_LIGHT;
					if (ZXConfig.KEY_FRONT_LIGHT == true) {
						opreateView
								.setImageResource(R.drawable.mzw_camera_close);
					} else {
						opreateView
								.setImageResource(R.drawable.mzw_camera_open);
					}
					cameraManager.getConfigManager().initializeTorch(
							cameraManager.getCamera().getParameters(), false);
					onPause();
					onResume();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		cameraManager = new CameraManager(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		viewfinderView.setCameraManager(cameraManager);

		handler = null;
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(surfaceHolder);
		} else {
			// Install the callback and wait for surfaceCreated() to init the
			// camera.
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		beepManager.updatePrefs();

		inactivityTimer.onResume();
	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		cameraManager.closeDriver();
		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			// restartPreviewAfterDelay(0L);
			return super.onKeyDown(keyCode, event);
		case KeyEvent.KEYCODE_FOCUS:
		case KeyEvent.KEYCODE_CAMERA:
			// Handle these events so they don't launch the Camera app
			return true;
			// Use volume up/down to turn on light
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			cameraManager.setTorch(false);
			return true;
		case KeyEvent.KEYCODE_VOLUME_UP:
			cameraManager.setTorch(true);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
		// Bitmap isn't used yet -- will be used soon
		if (handler == null) {
			savedResultToShow = result;
		} else {
			if (result != null) {
				savedResultToShow = result;
			}
			if (savedResultToShow != null) {
				Message message = Message.obtain(handler,
						R.id.decode_succeeded, savedResultToShow);
				handler.sendMessage(message);
			}
			savedResultToShow = null;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG,
					"*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	/**
	 * A valid barcode has been found, so give an indication of success and show
	 * the results.
	 */
	public void handleDecode(Result rawResult, Bitmap barcode) {
		inactivityTimer.onActivity();
		ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(
				this, rawResult);

		boolean fromLiveScan = barcode != null;
		if (fromLiveScan) {

			beepManager.playBeepSoundAndVibrate();
			// drawResultPoints(barcode, rawResult);
			viewfinderView.drawResultBitmap(barcode);
		}

		// 注意 需要跳转时候要判断下是哪种类型的二维码=》 "type": "invite"标识渠道管理成为下级
		// 其他的标识需要在确定！！！！！！！！！！！！！！！！！！！！
		String text = rawResult.getText();
		if (IsYinLianPay) {// 是银联支付
			YinLinPay(StrUtils.StrUrlToBean(text).getToken(), payoder);
			return;
		} else {
			BPay MyBean = StrUtils.StrUrlToBean(text);
			if (StrUtils.isEmpty(MyBean.getType())) {
				// 直接跳转到H5页面
				PromptManager.SkipActivity(BaseActivity, new Intent(
						BaseActivity, AWeb.class).putExtra(AWeb.Key_Bean,
						new BComment(text, "微糖商城")));
				BaseActivity.finish();
				return;
			} else {
				// 进行区分
				if (MyBean.getType().equals("invite")) {// 发展下级的扫码操作
					PromptManager.SkipActivity(BaseActivity, new Intent(
							BaseContext, ABeJunior.class).putExtra("url",
							MyBean.getQrcode()));
					BaseActivity.finish();
					return;
				}
				if (MyBean.getType().equals("goods")) {// 商品详情
					if (!StrUtils.isEmpty(MyBean.getGoods_id())) {
						PromptManager.SkipActivity(BaseActivity, new Intent(
								BaseContext, AGoodDetail.class).putExtra(
								"goodid", MyBean.getGoods_id()));
						BaseActivity.finish();
						return;
					} else {
						PromptManager.SkipActivity(BaseActivity, new Intent(
								BaseActivity, AWeb.class).putExtra(
								AWeb.Key_Bean, new BComment(text, "微糖商城")));
						BaseActivity.finish();
						return;
					}

				}
				if (MyBean.getType().equals("seller")) {// 店铺
					if (!StrUtils.isEmpty(MyBean.getSeller_id()))
						PromptManager.SkipActivity(BaseActivity, new Intent(
								BaseActivity, AShopDetail.class).putExtra(
								BaseKey_Bean,
								new BComment(MyBean.getSeller_id(), "微糖商城")));
					BaseActivity.finish();
					return;

				}

				if (MyBean.getType().equals("inviteCode")) {// 邀请码
					if (!StrUtils.isEmpty(MyBean.getInviteCode()))
						PromptManager.SkipActivity(BaseActivity, new Intent(
								BaseActivity, ABindCode.class).putExtra("code",
								MyBean.getInviteCode()));
					BaseActivity.finish();
					return;

				}

				PromptManager.SkipActivity(BaseActivity, new Intent(
						BaseActivity, AWeb.class).putExtra(AWeb.Key_Bean,
						new BComment(text, "微糖商城")));
				BaseActivity.finish();
				return;
			}

		}
		// 需要调整为解析URL的解析操作********************************************************************
		// 非银联支付的扫码操作
		// try {
		// JSONObject obj = new JSONObject(text);
		// if (obj.getString("type").equals("invite")) {// 渠道管理的
		// PromptManager
		// .SkipActivity(BaseActivity, new Intent(BaseContext,
		// ABeJunior.class).putExtra("url", text));
		// } else {// 非渠道管理成为下级的 暂时跳转new
		// PromptManager.SkipActivity(BaseActivity, new Intent(
		// BaseContext, ANew.class));
		// }
		// } catch (Exception e) {
		// PromptManager.ShowCustomToast(BaseContext, "无法识别二维码来源");
		// BaseActivity.finish();
		//
		// }

		// Toast.makeText(this, "扫描结果:" + text, Toast.LENGTH_LONG).show();

	}

	/**
	 * Superimpose a line for 1D or dots for 2D to highlight the key features of
	 * the barcode.
	 */
	private void drawResultPoints(Bitmap barcode, Result rawResult) {
		ResultPoint[] points = rawResult.getResultPoints();
		if (points != null && points.length > 0) {
			Canvas canvas = new Canvas(barcode);
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(R.color.result_points));
			if (points.length == 2) {
				paint.setStrokeWidth(4.0f);
				drawLine(canvas, paint, points[0], points[1]);
			} else if (points.length == 4
					&& (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A || rawResult
							.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
				// Hacky special case -- draw two lines, for the barcode and
				// metadata
				drawLine(canvas, paint, points[0], points[1]);
				drawLine(canvas, paint, points[2], points[3]);
			} else {
				paint.setStrokeWidth(10.0f);
				for (ResultPoint point : points) {
					canvas.drawPoint(point.getX(), point.getY(), paint);
				}
			}
		}
	}

	private static void drawLine(Canvas canvas, Paint paint, ResultPoint a,
			ResultPoint b) {
		canvas.drawLine(a.getX(), a.getY(), b.getX(), b.getY(), paint);
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG,
					"initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, viewfinderView,
						decodeFormats, characterSet, cameraManager);
			}
			decodeOrStoreSavedBitmap(null, null);
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			Toast.makeText(this, R.string.camera_problem, Toast.LENGTH_SHORT)
					.show();
			finish();
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializing camera", e);
			Toast.makeText(this, R.string.framwork_problem, Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	/**
	 * @author 提醒用户打开本链接=》http://dev.vt.www.v-town.cn/pay/pay/index
	 * @author 银联支付时候需要扫码支付 扫码后获取的token需要绑定订单号=》然后提示框就ok不用管是否PC端是否已经支付完成
	 * @author 需要在扫二维码界面进行调用
	 */
	private void YinLinPay(String token, String order_sn) {
		NHttpBaseStr baseStr = new NHttpBaseStr(BaseContext);

		baseStr.setPostResult(new IHttpResult<String>() {
			@Override
			public void onError(String error, int LoadType) {
				PromptManager.ShowCustomToast(BaseContext, "操作失败,请重新操作");
			}

			@Override
			public void getResult(int Code, String Msg, String Data) {
				if (200 == Code) {// 已经通知PC端进行支付
					ShowReminder(BaseContext, "请在网页进行安全支付");
				} else {// PC端绑定失败
					PromptManager.ShowCustomToast(BaseContext, "操作失败,请重新操作");

				}
			}
		});

		HashMap<String, String> map = new HashMap<String, String>();
		BUser bUser = Spuit.User_Get(BaseContext);
//		map.put("token", token);
		map.put("trade_token", token);
		map.put("member_id", bUser.getId());
		map.put("order_sn", order_sn);
		baseStr.getData(Constants.YinLianPay, map, Method.PUT);
	}

	/**
	 * 选择框
	 */
	private void ShowReminder(Context X, String Title) {

		Builder dialog = new AlertDialog.Builder(X);
		LayoutInflater inflater = (LayoutInflater) X
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.dialog_reminder, null);
		dialog.setView(layout);
		TextView dialog_txt_title = (TextView) layout
				.findViewById(R.id.dialog_txt_title);

		dialog_txt_title.setText(Title);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				EventBus.getDefault().post(
						new BMessage(BMessage.Tage_Kill_Self));
				BaseActivity.finish();

			}
		});

		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				EventBus.getDefault().post(
						new BMessage(BMessage.Tage_Kill_Self));
				BaseActivity.finish();
			}
		});
		dialog.show();
	}
}
