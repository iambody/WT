package io.vtown.WeiTangApp.ui.comment;

import java.io.File;
import java.util.HashMap;

import com.android.volley.Request.Method;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BLShow;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.qiniu.NUPLoadUtil;
import io.vtown.WeiTangApp.comment.net.qiniu.NUPLoadUtil.UpResult1;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils.UpResult;
import io.vtown.WeiTangApp.comment.selectpic.util.Bimp;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.switchButtonView.EaseSwitchButton;
import io.vtown.WeiTangApp.event.interf.IBottomDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.recordervido.ARecoderVido;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import de.greenrobot.event.EventBus;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-1 下午12:02:01
 * @商品的转发 只能视频
 */
public class AGoodVidoShare extends ATitleBase implements
		OnCheckedChangeListener {
	private View mView;
	public final static String Key_VidoFromShow = "vidosharefromshow";
	private RelativeLayout good_vido_lay;
	private ImageView good_vido_lay_bg_iv;
	private ImageView good_vido_lay_control_iv;

	private EditText good_vido_share_ed;

	private TextView good_vido_share_bt;

	private View good_vido_switchbt_lay;

	private EaseSwitchButton good_vido_switchbt;

	private boolean IsVidoOnLyShareWx;

	// 是否从Show里面获取

	private boolean IsVidoShow;
	/**
	 * 获取到视频录制path后进行刷新页面
	 */
	private String NewVidoPath;

	private String NewVidoPathCover = "";

	// 从Show列表进入时候传递进来的数据
	private BLShow ShowDatas = new BLShow();
	// show；列表传递进来的数据****************
	public final static String Key_VidoData = "vidoshowdata";

	public BUser mBUser;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_goodvido_share);
		mView = LayoutInflater.from(BaseContext).inflate(
				R.layout.activity_goodvido_share, null);
		EventBus.getDefault().register(this, "VidoSharEven", BMessage.class);
		mBUser = Spuit.User_Get(BaseContext);
		IBundl();
		hintKbTwo();
		IBaseV();
	}

	private void IBundl() {

		IsVidoShow = getIntent().getBooleanExtra(Key_VidoFromShow, false);
		if (IsVidoShow) {// 从show进来的
			ShowDatas = (BLShow) getIntent().getSerializableExtra(Key_VidoData);
		} else {// c从商品详情进来的s

		}

	}

	private void IBaseV() {
		good_vido_share_ed = (EditText) findViewById(R.id.good_vido_share_ed);
		good_vido_share_bt = (TextView) findViewById(R.id.good_vido_share_bt);
		good_vido_share_bt.setOnClickListener(this);
		good_vido_switchbt_lay = findViewById(R.id.good_vido_switchbt_lay);

		good_vido_lay = (RelativeLayout) findViewById(R.id.good_vido_lay);
		good_vido_lay_bg_iv = (ImageView) findViewById(R.id.good_vido_lay_bg_iv);
		good_vido_lay_control_iv = (ImageView) findViewById(R.id.good_vido_lay_control_iv);
		good_vido_lay_control_iv.setOnClickListener(this);
		ImageLoaderUtil.Load2(ShowDatas.getPre_url(), good_vido_lay_bg_iv,
				R.drawable.ic_launcher);

		good_vido_switchbt = (EaseSwitchButton) good_vido_switchbt_lay
				.findViewById(R.id.switch_select);
		((TextView) good_vido_switchbt_lay
				.findViewById(R.id.tv_switch_button_lable)).setText("只分享商品链接");

		good_vido_switchbt.setChecked(false);
		good_vido_switchbt.setOnCheckedChangeListener(this);

	}

	private void ShowVidoView(boolean IsVido) {
		good_vido_lay_bg_iv.setImageBitmap(createVideoThumbnail(NewVidoPath));
	}

	@Override
	protected void InitTile() {
		SetRightText("录视频");
		right_txt.setOnClickListener(this);
		SetTitleTxt(getResources().getString(R.string.share));
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {

		switch (Data.getHttpResultTage()) {
		case 5:
			PromptManager.ShowCustomToast(BaseContext, "转发成功");
			BaseActivity.finish();

			break;

		default:
			break;
		}
	}

	@Override
	protected void DataError(String error, int LoadType) {
	}

	@Override
	protected void NetConnect() {
		NetError.setVisibility(View.GONE);

	}

	@Override
	protected void NetDisConnect() {
		NetError.setVisibility(View.VISIBLE);

	}

	@Override
	protected void SetNetView() {
		SetNetStatuse(NetError);

	}

	@Override
	protected void MyClick(View V) {
		switch (V.getId()) {
		case R.id.right_txt:
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					ARecoderVido.class));
			break;
		case R.id.good_vido_lay_control_iv://

			if (StrUtils.isEmpty(NewVidoPath)) {
				if (!StrUtils.isEmpty(ShowDatas.getVid()))
					PromptManager.SkipActivity(BaseActivity, new Intent(
							BaseActivity, AVidemplay.class).putExtra(
							AVidemplay.VidoKey, ShowDatas.getVid()));
			} else {
				PromptManager.SkipActivity(
						BaseActivity,
						new Intent(BaseActivity, AVidemplay.class).putExtra(
								AVidemplay.VidoKey, NewVidoPath).putExtra(
								"issd", true));
			}
			break;

		case R.id.good_vido_share_bt:
			hintKbTwo();
			// new ShareVidoPop(BaseContext, mView, IsVidoShow);
			// UpVido(NewVidoPath);
			SharePop();

			break;
		default:

			break;
		}
	}

	private void SharePop() {
		ShowBottomPop(BaseContext, mView, "朋友圈分享", IsVidoOnLyShareWx ? "微信分享"
				: "Show分享", new IBottomDialogResult() {

			@Override
			public void SecondResult() {
				if (!IsVidoOnLyShareWx) {// 发Show****************
					// ShowZhuanNet();
					UpVido(NewVidoPath);
				} else {// 发朋友 **************
					if (IsVidoShow) {// show的数据源转发微信好友
						Share(1, ShowDatas.getGoodinfo().getTitle(), ShowDatas
								.getSellerinfo().getSeller_name(), ShowDatas
								.getPre_url(), ShowDatas.getGoodurl());
					} else {// 商品详情的数据源专访微信好友
						// Share(2);
					}

				}
			}

			@Override
			public void FristResult() {
				if (IsVidoShow) {// show的数据源转发朋友圈
					//
					Share(2, ShowDatas.getGoodinfo().getTitle(), ShowDatas
							.getSellerinfo().getSeller_name(), ShowDatas
							.getPre_url(), ShowDatas.getGoodurl());
				} else {// 商品详情的数据源专访朋友圈
					// Share(2);
				}
			}

			@Override
			public void CancleResult() {
			}
		});

	}

	class ShareVidoPop extends PopupWindow {

		public ShareVidoPop(Context mContext, View parent,
				final Boolean IsmyShow) {

			View view = View.inflate(mContext,
					R.layout.multiphoto_item_popupwindow, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			final LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.MATCH_PARENT);
			setHeight(LayoutParams.MATCH_PARENT);

			ColorDrawable dw = new ColorDrawable(0x00000000);
			setBackgroundDrawable(dw);
			this.setOutsideTouchable(true);

			setFocusable(true);

			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			view.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent event) {
					int Bottom = ll_popup.getTop();

					int y = (int) event.getY();

					if (event.getAction() == MotionEvent.ACTION_UP) {
						if (y < Bottom) {
							dismiss();
						}
					}
					return true;
				}
			});

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);

			bt1.setText("朋友圈分享");
			bt2.setText(IsVidoOnLyShareWx ? "微信分享" : "Show转发");

			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

					dismiss();
					if (IsmyShow) {// show的数据源转发朋友圈
						//
						Share(2, ShowDatas.getGoodinfo().getTitle(), ShowDatas
								.getSellerinfo().getSeller_name(), ShowDatas
								.getPre_url(), ShowDatas.getGoodurl());
					} else {// 商品详情的数据源专访朋友圈
						// Share(2);
					}

				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dismiss();
					if (!IsVidoOnLyShareWx) {// 发Show****************
						// ShowZhuanNet();
						UpVido(NewVidoPath);
					} else {// 发朋友 **************
						if (IsmyShow) {// show的数据源转发微信好友
							Share(1, ShowDatas.getGoodinfo().getTitle(),
									ShowDatas.getSellerinfo().getSeller_name(),
									ShowDatas.getPre_url(),
									ShowDatas.getGoodurl());
						} else {// 商品详情的数据源专访微信好友
							// Share(2);
						}

					}

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
	 * 发Show操作
	 */
	private void UpShowData(String VidoPath) {
		SetTitleHttpDataLisenter(this);
		HashMap<String, String> hashMap = new HashMap<String, String>();

		if (IsVidoShow) {// Show的数据
			hashMap.put("seller_id", mBUser.getSeller_id());
			hashMap.put("goods_id", ShowDatas.getGoods_id());
			hashMap.put("vid",
					StrUtils.isEmpty(NewVidoPath) ? ShowDatas.getVid()
							: VidoPath);
			hashMap.put("intro", good_vido_share_ed.getText().toString().trim());
			hashMap.put("is_type", "1");
			hashMap.put("is_add_url", "0");
			hashMap.put("pre_url",
					StrUtils.isEmpty(NewVidoPath) ? ShowDatas.getPre_url()
							: NewVidoPathCover);
			hashMap.put("ratio", "1");

		} else {
		}
		FBGetHttpData(hashMap, Constants.GoodsShow_ZhuanFa, Method.POST, 5,
				LOAD_LOADMOREING);

	}

	/**
	 * 微信分享的操作代码
	 */

	private void Share(int Type, String Title, String Content, String IvUrl,
			String UrL) {
		ShareSDK.initSDK(BaseContext);
		Platform platform = null;
		ShareParams sp = new ShareParams();
		switch (Type) {
		case 1:// 好友分享
			platform = ShareSDK.getPlatform(BaseContext, Wechat.NAME);
			sp.setShareType(Platform.SHARE_WEBPAGE);

			sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);

			sp.setText(Content);
			sp.setImageUrl(IvUrl);
			sp.setTitle(getResources().getString(R.string.share_app) + Title);//
			sp.setUrl(UrL );

			// sp.setText("大兔兔的测试数据");
			// sp.setImageUrl("http://static.freepik.com/free-photo/letter-a-underlined_318-8682.jpg");
			// sp.setTitle("大兔兔的title");//
			// sp.setUrl("www.baidu.com");
			break;
		case 2:// 朋友圈分享
			platform = ShareSDK.getPlatform(BaseContext, WechatMoments.NAME);
			sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);

			sp.setText(Content);
			sp.setImageUrl(IvUrl);
			sp.setTitle(getResources().getString(R.string.share_app) + Title);//
			sp.setUrl(UrL );

			// sp.setText("大兔兔的测试数据");
			// sp.setImageUrl("http://static.freepik.com/free-photo/letter-a-underlined_318-8682.jpg");
			// sp.setTitle("大兔兔的测试数据");//
			// sp.setUrl("www.baidu.com");
			break;
		default:
			break;
		}
		platform.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				PromptManager.ShowCustomToast(BaseContext, "分享失败");
			}

			@Override
			public void onComplete(Platform arg0, int arg1,
					HashMap<String, Object> arg2) {
				PromptManager.ShowCustomToast(BaseContext, "分享完成");
				BaseActivity.finish();
			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
				PromptManager.ShowCustomToast(BaseContext, "分享取消");
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
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		hintKbTwo();
		IsVidoOnLyShareWx = isChecked;

		good_vido_share_ed.setVisibility(IsVidoOnLyShareWx ? View.GONE
				: View.VISIBLE);

		good_vido_lay.setVisibility(IsVidoOnLyShareWx ? View.GONE
				: View.VISIBLE);

	}

	/**
	 * 获取信息
	 */
	public void VidoSharEven(BMessage bMessage) {
		if (bMessage.getMessageType() == 290) {
			NewVidoPath = bMessage.getReCordVidoPath();
			ShowVidoView(true);
			// NewVidoPathCover
			UpVidoPathCover(createVideoThumbnail(NewVidoPath));
		}
	}

	/**
	 * 上传封面
	 */
	private void UpVidoPathCover(Bitmap bitmap) {

		NUpLoadUtils dLoadUtils = new NUpLoadUtils(BaseContext,
				StrUtils.Bitmap2Bytes(bitmap), StrUtils.UploadQNName("photo"));

		dLoadUtils.SetUpResult(new UpResult() {

			@Override
			public void Progress(String arg0, double arg1) {

			}

			@Override
			public void Onerror() {

			}

			@Override
			public void Complete(String HostUrl, String Url) {
				NewVidoPathCover = HostUrl;

			}
		});
		dLoadUtils.UpLoad();

	}

	/**
	 * 上传视频
	 * 
	 * @param VidoPath
	 */
	private void UpVido(String VidoPath) {

		if (StrUtils.isEmpty(VidoPath)) {
			UpShowData(null);
			return;
		}
		if (StrUtils.isEmpty(NewVidoPathCover)) {
			UpVidoPathCover(createVideoThumbnail(NewVidoPath));
		}
		NUPLoadUtil dLoadUtils = new NUPLoadUtil(BaseContext,
				new File(VidoPath), StrUtils.UploadVido("photo"));
		PromptManager.showtextLoading3(BaseContext,
				getResources().getString(R.string.loading));
		dLoadUtils.SetUpResult1(new UpResult1() {

			@Override
			public void Progress(String arg0, double arg1) {
			}

			@Override
			public void Onerror() {
				PromptManager.closeTextLoading3();
			}

			@Override
			public void Complete(String HostUrl, String Url) {
				PromptManager.closeTextLoading3();
				UpShowData(HostUrl);

			}
		});
		dLoadUtils.UpLoad();

	}

}
