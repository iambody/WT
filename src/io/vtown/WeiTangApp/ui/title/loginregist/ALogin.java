package io.vtown.WeiTangApp.ui.title.loginregist;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.NHttpBaseStr;
import io.vtown.WeiTangApp.comment.util.SdCardUtils;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.event.interf.IHttpResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.ui.AMain;

import java.util.HashMap;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-27 上午9:52:44 登录界面
 */
public class ALogin extends ATitleBase implements PlatformActionListener {
	/**
	 * logo
	 */
	private ImageView login_logo;

	/**
	 * 微信一键登录
	 */
	private TextView login_wx_login;
	/**
	 * 手机号登录
	 */
	private TextView login_phone_login;

	@Override
	protected void InItBaseView() {

		setContentView(R.layout.activity_login);
		if (Spuit.IsLogin_Get(BaseContext)) {
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					AMain.class));
			BaseActivity.finish();
			return;
		}
		ShareSDK.initSDK(BaseContext);
		EventBus.getDefault().register(this, "LoginSuccesKill", BMessage.class);
		Ibase();
	}

	private void Ibase() {
		login_logo = (ImageView) findViewById(R.id.login_logo);
		login_wx_login = (TextView) findViewById(R.id.login_wx_login);
		login_phone_login = (TextView) findViewById(R.id.login_phone_login);
		login_wx_login.setOnClickListener(this);
		login_phone_login.setOnClickListener(this);

	}

	// 获取到微信的id后进行后台连接
	private void WXLogin(final String WXid) {//
		PromptManager.showLoading(BaseContext);
		NHttpBaseStr mBaseStr = new NHttpBaseStr(BaseContext);
		mBaseStr.setPostResult(new IHttpResult<String>() {

			@Override
			public void onError(String error, int LoadType) {
				PromptManager.ShowCustomToast(BaseContext, error);
			}

			@Override
			public void getResult(int Code, String Msg, String Data) {
				if (200 == Code) {// 微信意见绑定直接登录
					// 绑定过手机的
					BUser mBUser = JSON.parseObject(Data, BUser.class);
					Spuit.User_Save(BaseContext, mBUser);
					Spuit.IsLogin_Set(BaseActivity, true);
					// 微信登陆成功**********************************************
					// if (StrUtils.isEmpty(Muser.getParent_id())
					// || Muser.getParent_id().equals("0")) {// 暂未进行绑定邀请码
					// PromptManager.SkipActivity(BaseActivity, new Intent(
					// BaseActivity, AInviteCode.class));
					// } else {// 已经绑定了邀请码
					// if (!Spuit.IsLogin_RenZheng_Set(BaseActivity)) {//
					// 绑定邀请码但是未进行认证=》需要跳转到认证界面
					// PromptManager
					// .SkipActivity(BaseActivity, new Intent(
					// BaseActivity, ARealIdauth.class));
					// } else {// 绑定过邀请码 也认证过了===》跳转到主页面
					// PromptManager.SkipActivity(BaseActivity,
					// new Intent(BaseActivity, AMain.class));
					// }
					// }
					//开始注册极光推送
					InitJPush();
					SdCardUtils.delFile(SdCardUtils.CodePath(BaseContext)
							+ "mycode.jpg");
					SdCardUtils.delFile(SdCardUtils.CodePath(BaseContext)
							+ "shopcode.jpg");
					// 如果是第一次注册就进行下边逻辑
					if (!StrUtils.isEmpty(mBUser.getIs_new())
							&& mBUser.getIs_new().equals("0")) {// 注册进行第一次注册
																// 需要进行流程跳转
						if (StrUtils.isEmpty(mBUser.getParent_id())
								|| mBUser.getParent_id().equals("0")) {// 暂未进行绑定邀请码
							PromptManager
									.SkipActivity(BaseActivity, new Intent(
											BaseActivity, AInviteCode.class));
						} else {// 已经绑定了邀请码
							if (!Spuit.IsLogin_RenZheng_Set(BaseActivity)) {// 绑定邀请码但是未进行认证=》需要跳转到认证界面
								PromptManager.SkipActivity(BaseActivity,
										new Intent(BaseActivity,
												ARealIdauth.class));
							} else {// 绑定过邀请码 也认证过了===》跳转到主页面
								PromptManager.SkipActivity(BaseActivity,
										new Intent(BaseActivity, AMain.class));
							}
						}

					} else {// 登录直接跳转
						PromptManager.SkipActivity(BaseActivity, new Intent(
								BaseActivity, AMain.class));

					}

					// PromptManager.SkipActivity(BaseActivity, new Intent(
					// BaseActivity, AMain.class));
					BaseActivity.finish();

				} else if (2017 == Code) {// 微信没有绑定
					PromptManager.ShowCustomToast(BaseContext, "绑定手机号");
					PromptManager.SkipActivity(
							BaseActivity,
							new Intent(BaseActivity, AInviteAndApprove.class)
									.putExtra("iswx", true).putExtra("wxid",
											WXid));
				} else {// 错误
					PromptManager.ShowCustomToast(BaseContext, Msg);
				}
			}
		});
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("weixin_open_id", WXid);
		mBaseStr.getData(Constants.Login_Wx_Login, map, Method.POST);

	}

	@Override
	protected void InitTile() {
		SetTitleTxt("");
	}

	 

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		// if (StrUtils.isEmpty(Data.getHttpResultStr())) {
		// PromptManager.ShowCustomToast(BaseContext, Msg);
		// return;
		// }
		//
		// if (Muser.getIs_new().equals("1")) {// 绑定过手机号的直接跳转到主界面
		// PromptManager.ShowCustomToast(BaseContext, "登陆成功");
		//
		// } else {
		//
		// }
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
		case R.id.login_wx_login:
			if (!Constants.isWeixinAvilible(BaseContext)) {
				PromptManager.ShowCustomToast(BaseContext, "请安装微信客户端");
				return;
			}
			WxAuth();

			break;
		case R.id.login_phone_login:
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					AInviteAndApprove.class).putExtra("iswx", false));
			break;
		default:
			break;
		}
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int auth_statu = msg.what;
			Platform platform = (Platform) msg.obj;
			switch (auth_statu) {
			case 1:

				final String nickname = platform.getDb().getUserName();
				final String userid = platform.getDb().getUserId();
				String usericon = platform.getDb().getUserIcon();
				String platformName = platform.getName();
				// Toast.makeText(
				// BaseContext,
				// String.format("昵称%s;   id是%s;   名字是%s", nickname,
				// userid, platformName), 10 * 1000).show();
				WXLogin(userid);
				break;
			case 0:
				PromptManager.ShowCustomToastLong(BaseContext, "微信验证失败");

				break;
			case 3:
				PromptManager.ShowCustomToast(BaseContext, "微信验证取消");
			default:
				break;
			}
			return;

		}
	};

	private void WxAuth() {

		Platform w22eibo = ShareSDK.getPlatform(BaseContext, Wechat.NAME);// Wechat.NAME);
		w22eibo.removeAccount(true);
		w22eibo.setPlatformActionListener(this);
		w22eibo.showUser(null);// 获得用户数据

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
		message.obj = arg2.toString();

		// message.obj = arg0;
		message.what = 0;
		mHandler.sendMessage(message);
	}

	/**
	 * 登录成功后需要进行进行finish本activity
	 */

	public void LoginSuccesKill(BMessage bMessage) {
		if (bMessage.getMessageType() == BMessage.Tage_Login_Kill_Other) {
			BaseActivity.finish();
		}
	}

}
