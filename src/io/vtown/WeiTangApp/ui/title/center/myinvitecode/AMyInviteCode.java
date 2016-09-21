package io.vtown.WeiTangApp.ui.title.center.myinvitecode;

import java.io.File;
import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcache.BShop;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.channl.BChannl;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.QRCodeUtil;
import io.vtown.WeiTangApp.comment.util.SdCardUtils;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoader;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.dialog.ImagViewDialog;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.loginregist.AInviteCode;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-6-26 下午4:31:10 我的邀请码页面
 */
public class AMyInviteCode extends ATitleBase {

	/**
	 * 邀请码图标
	 */
	private CircleImageView iv_my_invite_code_icon;
	/**
	 * 二维码
	 */
	private ImageView my_invite_code_iv;
	/**
	 * 邀请码
	 */
	private TextView tv_my_invite_code;
	/**
	 * 复制按钮
	 */
//	private TextView tv_my_invite_code_copy;
	private ImageView tv_my_invite_code_copy_iv;
	/**
	 * 邀请码描述
	 */
	private TextView tv_my_invite_code_desc;
	/**
	 * 分享微信
	 */
	private LinearLayout ll_my_invite_code_share_to_wx;
	/**
	 * 分享到朋友圈
	 */
	private LinearLayout ll_my_invite_code_share_to_frends;
	private ClipData myClip;
	/**
	 * 文本操作管理
	 */
	private ClipboardManager myClipboard;
	private BUser bUser;

	private TextView right_txt;

	// 分享的地址*****************************http://dev.vt.m.v-town.cn/member/invite/index*************************后边get请求+我的邀请码
	// ******************http://dev.vt.m.v-town.cn/member/invite/index?inviteCode=L38NDK****************************************************
	private String InItCode;
	private BShop mBShop;

	// 分享的数据
	private BChannl myBChannl;

	private int ShareType;

	private Bitmap MyBitMap;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_center_my_invite_code);
		myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		bUser = Spuit.User_Get(getApplicationContext());
		mBShop = Spuit.Shop_Get(getApplicationContext());

		// 缓存处理
		ICache();
		// 获取邀请码数据
		IData(LOAD_INITIALIZE, 0);
		// 开始展示view
		IView();

	}

	/**
	 * 缓存处理
	 */
	private void ICache() {
		if (StrUtils.isEmpty(CacheUtil.Center_Set_Initve_Get(BaseContext))) {// 没有缓存
			return;
		} else {// 有缓存
			try {
				myBChannl = JSON.parseObject(
						CacheUtil.Center_Set_Initve_Get(BaseContext),
						BChannl.class);
			} catch (Exception e) {
				return;
			}
		}

	}

	/**
	 * 初始化控件
	 */
	private void IView() {

		tv_my_invite_code_copy_iv= (ImageView) findViewById(R.id.tv_my_invite_code_copy_iv);
		my_invite_code_iv = (ImageView) findViewById(R.id.my_invite_code_iv);
		my_invite_code_iv.setOnClickListener(this);
		iv_my_invite_code_icon = (CircleImageView) findViewById(R.id.iv_my_invite_code_icon);
		ImageLoaderUtil.Load2(mBShop.getAvatar(), iv_my_invite_code_icon,
				R.drawable.error_iv2);
		tv_my_invite_code = (TextView) findViewById(R.id.tv_my_invite_code);
//		tv_my_invite_code_copy = (TextView) findViewById(R.id.tv_my_invite_code_copy);
		tv_my_invite_code_desc = (TextView) findViewById(R.id.tv_my_invite_code_desc);
		ll_my_invite_code_share_to_wx = (LinearLayout) findViewById(R.id.ll_my_invite_code_share_to_wx);
		ll_my_invite_code_share_to_frends = (LinearLayout) findViewById(R.id.ll_my_invite_code_share_to_frends);
//		tv_my_invite_code_copy.setOnClickListener(this);
		tv_my_invite_code_copy_iv.setOnClickListener(this);
		ll_my_invite_code_share_to_wx.setOnClickListener(this);
		ll_my_invite_code_share_to_frends.setOnClickListener(this);
		tv_my_invite_code_desc.setText(getResources().getString(
				R.string.share_quan_str));
		//!!!!!!!!!!!!!!!!!!!!!显示后台数据
		 StrUtils.SetColorsTxt(BaseContext,tv_my_invite_code_desc,R.color.red,"邀请好友注册各得","20元",",快快扫我吧") ;
		// tv_my_invite_code_desc.setVisibility(View.GONE);
		tv_my_invite_code.measure(0,0);
		int height = tv_my_invite_code.getMeasuredHeight();
		int width = tv_my_invite_code.getMeasuredWidth();
		Shader shader =new LinearGradient(0, 0, width/2, height/2, getResources().getColor(R.color.app_fen), getResources().getColor(R.color.green), Shader.TileMode.CLAMP);

		tv_my_invite_code.getPaint().setShader(shader);
		StrUtils.SetTxt(tv_my_invite_code, bUser.getInvite_code());
		// InItCode = getFileRoot(BaseContext) + File.separator + "qr_" +
		// "mycode"
		// + ".jpg";

		String InItCode = SdCardUtils.CodePath(BaseContext) + "mycode.jpg";

		ISizeCodeIv();
		CreatQ(bUser.getInvite_url(), InItCode);
	}

	private void ISizeCodeIv() {
		LinearLayout.LayoutParams mLayoutParams = new LayoutParams(
				screenWidth / 2, screenWidth / 2);
		my_invite_code_iv.setLayoutParams(mLayoutParams);

	}

	private void CreatQ(final String Str, final String Pathe) {
		// final String filePath = getFileRoot(BaseContext) + File.separator
		// + "qr_mycode" + bUser.getSeller_id() + ".jpg";
//		File mFile = new File(Pathe);
//		if (mFile.exists()) {
//			// 如果存在就直接显示
//			MyBitMap = BitmapFactory.decodeFile(Pathe);
//			my_invite_code_iv.setImageBitmap(MyBitMap);
//			my_invite_code_iv.setVisibility(View.VISIBLE);
//
//			return;
//		}
		// 如果不存在就进行吹

		new Thread(new Runnable() {
			@Override
			public void run() {

				String avatar = Spuit.Shop_Get(getApplicationContext()).getAvatar();
				Bitmap logoBm = com.nostra13.universalimageloader.core.ImageLoader
						.getInstance().loadImageSync(avatar);
				boolean success = QRCodeUtil.createQRImage(Str, 800, 800,
						logoBm, Pathe);

				if (success) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							MyBitMap = BitmapFactory.decodeFile(Pathe);
							my_invite_code_iv.setImageBitmap(MyBitMap);
							my_invite_code_iv.setVisibility(View.VISIBLE);
//							new ImagViewDialog(BaseContext, MyBitMap, screenWidth, 2)
//									.show();
						}
					});
				}
			}
		}).start();

	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getString(R.string.center_yaoqingma));
		right_txt = (TextView) findViewById(R.id.right_txt);
		right_txt.setOnClickListener(this);

		SetRightText("邀请码");

	}

	@Override
	protected void onResume() {
		super.onResume();
		right_txt
				.setVisibility(Spuit.InvitationCode_Get(getApplicationContext()) ? View.GONE
						: View.VISIBLE);

	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		if (StrUtils.isEmpty(Data.getHttpResultStr())) {
			return;
		}

		try {
			myBChannl = JSON
					.parseObject(Data.getHttpResultStr(), BChannl.class);
		} catch (Exception e) {
			return;
		}
		CacheUtil.Center_Set_Initve_Save(BaseContext, Data.getHttpResultStr());
		if (Data.getHttpLoadType() == LOAD_LOADMOREING) {// 是二次验证是否存在时候的验证
			Share(Data.getHttpResultTage());
		}
	}

	@Override
	protected void DataError(String error, int LoadType) {
		if (LoadType == LOAD_LOADMOREING) {// 分享时候
			Share(ShareType);
		}
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
		case R.id.my_invite_code_iv://
			if (MyBitMap != null)
				new ImagViewDialog(BaseContext, MyBitMap, screenWidth, 2)
						.show();
			break;
		case R.id.tv_my_invite_code_copy_iv:// 复制邀请码
			copy();
			break;
		case R.id.ll_my_invite_code_share_to_wx:// 分享好友

			// Share(1);
			ShareType = 1;
			ShowBegin(1);
			break;

		case R.id.ll_my_invite_code_share_to_frends:// 分享朋友圈
			// Share(2);
			ShareType = 2;
			ShowBegin(2);
			break;
		case R.id.right_txt:// 点击绑定 跳转到绑定邀请码界面********
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					AInviteCode.class).putExtra("isfromcenter", true));
			break;
		}
	}

	/**
	 * 获取分享的文案
	 */
	private void IData(int Loadtype, int ShareMethod) {
		SetTitleHttpDataLisenter(this);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", Spuit.User_Get(getApplicationContext()).getSeller_id());
		FBGetHttpData(map, Constants.ShareInItViter, Method.GET, ShareMethod,
				Loadtype);
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	/**
	 * 复制
	 */
	@SuppressLint("NewApi")
	private void copy() {
		String text = tv_my_invite_code.getText().toString().trim();
		myClip = ClipData.newPlainText("text", text);
		myClipboard.setPrimaryClip(myClip);
		PromptManager.ShowCustomUpToast(BaseContext, "邀请码已经复制,快快邀请好友吧");
	}

	/**
	 * 开始分享
	 */
	private void ShowBegin(int Type) {
		if (myBChannl == null) {
			// 要获取数据
			IData(LOAD_LOADMOREING, Type);
		} else {
			Share(Type);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		CacheUtil.BitMapRecycle(MyBitMap);
	}

	/**
	 * 分享
	 * 
	 * @param Type
	 */
	private void Share(int Type) {
		if (!Constants.isWeixinAvilible(BaseContext)) {
			PromptManager.ShowCustomToast(BaseContext, "请安装微信");
			return;
		}
		if (myBChannl == null) {
			myBChannl = new BChannl();
		}
		ShareSDK.initSDK(BaseContext);
		Platform platform = null;
		ShareParams sp = new ShareParams();
		switch (Type) {
		case 1:// 好友分享
			platform = ShareSDK.getPlatform(BaseContext, Wechat.NAME);
			sp.setShareType(Platform.SHARE_WEBPAGE);

			sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);

			sp.setImageUrl(myBChannl.getInvite_code_img());
			sp.setTitle(myBChannl.getInvite_code_title());//
			sp.setText(StrUtils.isEmpty(myBChannl.getInvite_code_content()) ? getResources()
					.getString(R.string.invter_share_conten) : myBChannl
					.getInvite_code_content());
			sp.setUrl(bUser.getInvite_url());
			break;
		case 2:// 朋友圈分享
			platform = ShareSDK.getPlatform(BaseContext, WechatMoments.NAME);
			sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);

			sp.setText(StrUtils.isEmpty(myBChannl.getInvite_code_content()) ? getResources()
					.getString(R.string.invter_share_conten) : myBChannl
					.getInvite_code_content());
			sp.setImageUrl(myBChannl.getInvite_code_img());
			sp.setTitle(myBChannl.getInvite_code_title());//
			sp.setUrl(bUser.getInvite_url());
			break;
		default:
			break;
		}
		platform.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				PromptManager.ShowCustomToast(BaseContext, "分享取消");
			}

			@Override
			public void onComplete(Platform arg0, int arg1,
					HashMap<String, Object> arg2) {
				PromptManager.ShowCustomToast(BaseContext, "分享完成");
			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
			}
		});
		platform.share(sp);
	}

}
