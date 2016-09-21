package io.vtown.WeiTangApp.ui.title;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcache.BShop;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.shop.BMyShop;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.ImagePathConfig;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.ACommentList;
import io.vtown.WeiTangApp.ui.comment.order.ACenterMyOrder;
import io.vtown.WeiTangApp.ui.title.center.mycoupons.AMyCoupons;
import io.vtown.WeiTangApp.ui.title.center.myinvitecode.AMyInviteCode;
import io.vtown.WeiTangApp.ui.title.center.myshow.ACenterShow;
import io.vtown.WeiTangApp.ui.title.center.set.APersonalData;
import io.vtown.WeiTangApp.ui.title.center.wallet.ACenterWallet;
import io.vtown.WeiTangApp.ui.title.loginregist.ARealIdauth;

import java.io.File;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-12 下午4:16:24
 * @author 一级页面 我的
 */
public class ACenter extends ATitleBase {
	/**
	 * 上边 Show圈,订单，邀请码，我的钱包，我的卡劵
	 */
	private View center_show, center_oder, center_invite_code, center_wallet,
			center_card;

	/**
	 * 下边商品关注 店铺收藏 浏览记录
	 */
	private View center_oder_guanzhu, center_shop_collect,
			center_liulan_history;

	private CircleImageView center_myiv;
	private TextView center_myname;

	private View tab_center_oder, tab_center_walle;
	private ImageView center_cover;
	private File CenterCoverFile;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_center);
		CenterCoverFile = new File(ImagePathConfig.CenterCoverPath(BaseContext));
		EventBus.getDefault().register(this, "OnGetMessage", BMessage.class);
		IView();
	}

	private void IView() {
		IUp();
		IDown();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (StrUtils.isEmpty(Spuit.Shop_Get(BaseContext).getAvatar())
				&& StrUtils.isEmpty(Spuit.Shop_Get(BaseContext).getId())) {
			IData(LOAD_INITIALIZE);
		}
	}

	/**
	 * 获取商铺的信息
	 */
	private void IData(int Type) {
		SetTitleHttpDataLisenter(this);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", Spuit.User_Get(BaseContext).getSeller_id());
		FBGetHttpData(map, Constants.MyShop, Method.GET, 0, Type);
	}

	private void IDown() {
	}

	private void IUp() {
		center_cover = (ImageView) findViewById(R.id.center_cover);
		tab_center_oder = findViewById(R.id.tab_center_oder);
		tab_center_walle = findViewById(R.id.tab_center_walle);

		center_myiv = (CircleImageView) findViewById(R.id.center_myiv);
		center_myname = (TextView) findViewById(R.id.center_myname);
		center_myiv.setBorderWidth(10);
		center_myiv.setBorderColor(getResources().getColor(R.color.transparent6));
		center_myiv.setOnClickListener(this);

		if (CenterCoverFile.exists()) {
			center_cover.setImageBitmap(BitmapFactory
					.decodeFile(ImagePathConfig.CenterCoverPath(BaseContext)));
		} else {
			ImageLoaderUtil.LoadGaosi(BaseContext, Spuit.Shop_Get(BaseContext)
					.getAvatar(), center_cover, R.drawable.item_shangji_iv, 2);
		}
		ImageLoaderUtil.Load2(Spuit.Shop_Get(BaseContext).getAvatar(),
				center_myiv, R.drawable.error_iv2);

		StrUtils.SetTxt(center_myname, Spuit.Shop_Get(BaseContext)
				.getSeller_name());

		center_show = findViewById(R.id.center_show);
		center_oder = findViewById(R.id.center_oder);
		center_invite_code = findViewById(R.id.center_invite_code);
		center_wallet = findViewById(R.id.center_wallet);
		center_card = findViewById(R.id.center_card);

		// 下边
		center_oder_guanzhu = findViewById(R.id.center_oder_guanzhu);
		center_shop_collect = findViewById(R.id.center_shop_collect);
		center_liulan_history = findViewById(R.id.center_liulan_history);

		SetItemContent(center_show, R.string.center_show, R.drawable.center_iv1);
		SetItemContent(center_oder, R.string.center_oder, R.drawable.center_iv2);
		SetItemContent(center_invite_code, R.string.center_yaoqingma,
				R.drawable.center_iv3);
		SetItemContent(center_wallet, R.string.center_wallet,
				R.drawable.center_iv4);
		SetItemContent(center_card, R.string.center_kaquan,
				R.drawable.center_iv5);

		// 下
		SetItemContent(center_oder_guanzhu, R.string.center_good_guanzhu,
				R.drawable.center_iv6);
		SetItemContent(center_shop_collect, R.string.center_shop_collect,
				R.drawable.center_iv7);
		SetItemContent(center_liulan_history, R.string.center_jilu,
				R.drawable.center_iv8);
		// 上边两个

		SetCommentIV("我的订单", R.drawable.shop_grad2, tab_center_oder);
		SetCommentIV("我的钱包", R.drawable.center_wallet, tab_center_walle);

	}

	/**
	 * 上边是IV下边是文字的布局
	 * 
	 * @param title
	 * @param IvRource
	 * @param V
	 */
	public void SetCommentIV(String title, int IvRource, View V) {
		ImageView viessw;
		((ImageView) V.findViewById(R.id.comment_ivtxt_iv))
				.setBackgroundResource(IvRource);
		((TextView) V.findViewById(R.id.comment_ivtxt_txt)).setText(title);
		((TextView) V.findViewById(R.id.comment_ivtxt_txt)).setTextAppearance(
				BaseContext, R.style.AudioFileInfoOverlayText1);

		// style="@style/AudioFileInfoOverlayText"
		V.setOnClickListener(this);

	}

	private void SetItemContent(View VV, int ResourceTitle, int ResourceIvId) {
		((TextView) VV.findViewById(R.id.commentview_center_txt))
				.setText(getResources().getString(ResourceTitle));
		((ImageView) VV.findViewById(R.id.commentview_center_iv))
				.setImageResource(ResourceIvId);
		VV.setOnClickListener(this);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt("我的");
		findViewById(R.id.lback).setVisibility(View.GONE);
		// SetRightIv(R.drawable.center_iv_setting);
		// right_iv.setOnClickListener(this);
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		if (StrUtils.isEmpty(Data.getHttpResultStr())) {
			DataError(Msg, Data.getHttpLoadType());
			return;
		}

		BMyShop mBShop = new BMyShop();
		mBShop = JSON.parseObject(Data.getHttpResultStr(), BMyShop.class);
		BShop MyBShop = mBShop.getBase();
		MyBShop.setSubCounter(mBShop.getSubCounter());
		MyBShop.setTeamCounter(mBShop.getTeamCounter());
		MyBShop.setTodayVisitor(mBShop.getTodayVisitor());
		MyBShop.setTodayIncome(mBShop.getTodayIncome());
		MyBShop.setTodaySales(mBShop.getTodaySales());
		MyBShop.setTotalIncome(mBShop.getTotalIncome());
		Spuit.Shop_Save(BaseContext, MyBShop);
		ImageLoaderUtil.Load2(Spuit.Shop_Get(BaseContext).getAvatar(),
				center_myiv, R.drawable.testiv);

		// File CenterCoverFile = new
		// File(ImagePathConfig.CenterCoverPath(BaseContext));
		if (CenterCoverFile.exists()) {
			center_cover.setImageBitmap(BitmapFactory
					.decodeFile(ImagePathConfig.CenterCoverPath(BaseContext)));
		} else {
			ImageLoaderUtil.LoadGaosi(BaseContext, Spuit.Shop_Get(BaseContext)
					.getAvatar(), center_cover, R.drawable.item_shangji_iv, 2);
		}
		// ImageLoaderUtil.LoadGaosi(BaseContext, Spuit.Shop_Get(BaseContext)
		// .getAvatar(), center_cover, R.drawable.error_iv1,2);
		StrUtils.SetTxt(center_myname, Spuit.Shop_Get(BaseContext)
				.getSeller_name());
	}

	@Override
	protected void DataError(String error, int LoadTyp) {

	}

	public void OnGetMessage(BMessage event) {

		int messageType = event.getMessageType();
		BShop myBShop = Spuit.Shop_Get(BaseContext);
		switch (messageType) {
		case BMessage.Tage_Main_To_ShowGaoSi:
			if (!StrUtils.isEmpty(myBShop.getCover()))
				ImageLoaderUtil.LoadGaosi(BaseContext,
						Spuit.Shop_Get(BaseContext).getAvatar(), center_cover,
						R.drawable.item_shangji_iv, 2);


		case BMessage.Tage_Shop_data_cover_change:
			if (!StrUtils.isEmpty(myBShop.getAvatar())) {
				ImageLoaderUtil.Load2(Spuit.Shop_Get(BaseContext).getAvatar(),
						center_myiv, R.drawable.testiv);
				ImageLoaderUtil.LoadGaosi(BaseContext,
						Spuit.Shop_Get(BaseContext).getAvatar(), center_cover,
						R.drawable.error_iv1, 2);
			}
			break;
		case BMessage.Tage_Shop_data_shopname_change:
			StrUtils.SetTxt(center_myname, Spuit.Shop_Get(BaseContext)
					.getSeller_name());

			break;
		// case BMessage.Tage_Shop_data_background_change:
		// if (!StrUtils.isEmpty(myBShop.getCover()))
		// ImageLoaderUtil.LoadGaosi(BaseContext,
		// Spuit.Shop_Get(BaseContext).getAvatar(), center_cover,
		// R.drawable.item_shangji_iv,2);
		// break;

		default:
			break;
		}
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
		case R.id.center_myiv:
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					APersonalData.class));
			break;
		case R.id.center_show:// Show圈

			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					ACenterShow.class));
			break;
		case R.id.center_oder:// 我的订单
			// PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
			// ACenterOder.class));
			break;
		case R.id.center_invite_code:// 邀请码
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					AMyInviteCode.class));
			break;
		case R.id.center_wallet:// 钱包
			// boolean isLogin_RenZheng_Set = Spuit
			// .IsLogin_RenZheng_Set(BaseContext);
			// if (isLogin_RenZheng_Set) {
			// PromptManager.SkipActivity(BaseActivity, new Intent(
			// BaseActivity, ACenterWallet.class));
			// } else {
			// ShowRealAuthDialog();
			// }

			break;
		case R.id.center_card:// 卡劵

			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					AMyCoupons.class));
			break;

		case R.id.center_oder_guanzhu:// 商品关注

			Intent intent = new Intent(BaseActivity, ACommentList.class);
			intent.putExtra(ACommentList.Tage_ResultKey,
					ACommentList.Tage_ACenterOderGuanzhu);

			PromptManager.SkipActivity(BaseActivity, intent);

			break;
		case R.id.center_shop_collect:// 店铺收藏

			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					ACommentList.class).putExtra(ACommentList.Tage_ResultKey,
					ACommentList.Tage_ACenterShopCollect));

			break;
		case R.id.center_liulan_history:// 浏览记录

			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
				ACommentList.class).putExtra(ACommentList.Tage_ResultKey,
				ACommentList.Tage_ACenterGoodBrowseRecord));
//			 PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
//			 AShowIDPic.class));
			break;
		case R.id.tab_center_oder:
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					ACenterMyOrder.class));

			break;
		case R.id.tab_center_walle:
			boolean isLogin_RenZheng_Set = Spuit
					.IsLogin_RenZheng_Set(BaseContext);
			if (isLogin_RenZheng_Set) {
				PromptManager.SkipActivity(BaseActivity, new Intent(
						BaseActivity, ACenterWallet.class));
			} else {
				ShowRealAuthDialog();
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 实名认证对话框
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @param
	 */
	private void ShowRealAuthDialog() {

		ShowCustomDialog(getResources().getString(R.string.noshimingrenz),
				getResources().getString(R.string.cancle), "去认证",
				new IDialogResult() {

					@Override
					public void RightResult() {
						int from_where = 10;

						PromptManager.SkipActivity(BaseActivity, new Intent(
								BaseContext, ARealIdauth.class).putExtra(
								ARealIdauth.FROM_WHERE_KEY, from_where));
					}

					@Override
					public void LeftResult() {
					}
				});


	}

	@Override
	protected void InItBundle(Bundle bundle) {

	}

	@Override
	protected void SaveBundle(Bundle bundle) {

	}//

}
