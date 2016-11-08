package io.vtown.WeiTangApp.wxapi;

import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.ui.ABase;
import io.vtown.WeiTangApp.ui.afragment.ACenterOder;
import io.vtown.WeiTangApp.ui.afragment.AShopOderManage;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;

public class WXPayEntryActivity extends ABase implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.pay_result);
		//
		// api = WXAPIFactory.createWXAPI(this, "wxf104565ec7418036");

		api = WXAPIFactory.createWXAPI(this, Constants.WxPayKey);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
		case 0:// 0 成功 展示成功页面
			PromptManager.ShowCustomToast(BaseContext, "微信支付成功");
//			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
//					ACenterOder.class)); 
			BaseActivity.finish();
			// 通知刷新订单数据
			EventBus.getDefault().post(
					new BMessage(BMessage.Tage_To_Pay_Updata));
			EventBus.getDefault().post(new BMessage(BMessage.Tage_Kill_Self2));
			WXPayEntryActivity.this.finish();
			break;
		case -1:// -1 错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
			PromptManager
					.ShowCustomToast(BaseContext,
							" 错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。。");
			WXPayEntryActivity.this.finish();
			break;
		case -2:// -2 用户取消 无需处理。发生场景：用户不支付了，点击取消，返回APP。
			PromptManager.ShowCustomToast(BaseContext,
					"用户取消 无需处理。发生场景：用户不支付了，点击取消，返回APP。");
			WXPayEntryActivity.this.finish();
			break;
		default:
			break;
		}

		// if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setTitle(R.string.app_tip);
		// builder.setMessage(getString(R.string.pay_result_callback_msg,
		// String.valueOf(resp.errCode)));
		// builder.show();
		// }
	}
}