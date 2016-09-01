package io.vtown.WeiTangApp.ui.ui;

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
import io.vtown.WeiTangApp.comment.view.PullScrollView;
import io.vtown.WeiTangApp.comment.view.PullScrollView.onRefreshListener;
import io.vtown.WeiTangApp.comment.view.listview.SecondStepView;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitileNoBase;
import io.vtown.WeiTangApp.ui.afragment.ASellStatistics;
import io.vtown.WeiTangApp.ui.afragment.AShopPurchase;
import io.vtown.WeiTangApp.ui.comment.order.AShopOrderManager;
import io.vtown.WeiTangApp.ui.comment.order.AShopPurchaseOrder;
import io.vtown.WeiTangApp.ui.derls.AGoodManger;
import io.vtown.WeiTangApp.ui.title.center.wallet.ACenterWallet;
import io.vtown.WeiTangApp.ui.title.loginregist.ARealIdauth;
import io.vtown.WeiTangApp.ui.title.shop.ABrandCheck;
import io.vtown.WeiTangApp.ui.title.shop.ABrandDaiLi;
import io.vtown.WeiTangApp.ui.title.shop.addgood.AAddGood;
import io.vtown.WeiTangApp.ui.title.shop.center.AShopData;
import io.vtown.WeiTangApp.ui.title.shop.channel.AChannel;

import java.io.File;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-12 下午4:14:32
 * @author 一级页面 商铺
 */
public class AShop extends ATitileNoBase {
	/**
	 * 头像
	 */
	private CircleImageView tab_shop_iv;
	/**
	 * 小店名字
	 */
	private TextView tab_shop_name;
	/**
	 * 小店说明
	 */
	private TextView tab_shop_sign;

	/**
	 * 累计收入
	 */
	private TextView All_Income;
	/**
	 * 累计收入的布局
	 */
	private View tab_shop_all_income_lay;

	/**
	 * 今日收入
	 */
	private TextView tab_shop_today_income;
	/**
	 * 今日销量
	 */
	private TextView tab_shop_today_volume;
	/**
	 * 当日访客
	 */
	private TextView tab_shop_today_visitor;
	/**
	 * 团队总人数
	 */
	private TextView tab_shop_team_number;
	/**
	 * 直级下属
	 */
	private TextView tab_shop_zhijixiashu_number;
	/**
	 * 产品发布,订单管理，渠道管理，我的采购单，平拍代理，商品管理，销售统计
	 */
	private View tab_shop_good_fabu, tab_shop_good_oder_guanli,
			tab_shop_good_qudao_guanli, tab_shop_caigoudan,
			tab_shop_pinpaidaili, tab_shop_good_good_guanli,
			tab_shop_xiaoshoutongji, tab_shop_lookshop, tab_shop_ruzhu;
	/**
	 * 封面
	 */
	private ImageView iv_shop_cover;

	/**
	 * Sp中店铺相关数据
	 */
	private BShop myBShop = null;
	// 刷新加载
	private PullScrollView shop_out_scrollview;
	private SecondStepView shop_load_head_iv;
	private AnimationDrawable secondAnimation;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_shop);
		// 注册事件
		EventBus.getDefault().register(this, "OnGetMessage", BMessage.class);
		IView();
		IData(LOAD_INITIALIZE);
	}

	private void IData(int Type) {
		SetTitleHttpDataLisenter(this);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", Spuit.User_Get(BaseContext).getSeller_id());
		FBGetHttpData(map, Constants.MyShop, Method.GET, 0, Type);
	}

	private void IView() {
		shop_out_scrollview = (PullScrollView) findViewById(R.id.shop_out_scrollview);
		shop_load_head_iv = (SecondStepView) findViewById(R.id.shop_load_head_iv);
		shop_load_head_iv
				.setBackgroundResource(R.drawable.second_step_animation);
		secondAnimation = (AnimationDrawable) shop_load_head_iv.getBackground();
		shop_out_scrollview.setOnRefreshListener(new onRefreshListener() {

			@Override
			public void refresh() {

				// LoadFrashComplet();
				secondAnimation.start();
				IData(LOAD_REFRESHING);
			}

		});

		tab_shop_iv = (CircleImageView) findViewById(R.id.tab_shop_iv);

		tab_shop_iv.setBorderWidth(10);
		tab_shop_iv.setBorderColor(getResources().getColor(R.color.transparent6));
		tab_shop_name = (TextView) findViewById(R.id.tab_shop_name);
		tab_shop_name.setOnClickListener(this);
		iv_shop_cover = (ImageView) findViewById(R.id.iv_shop_cover);
		iv_shop_cover.setOnClickListener(this);
		tab_shop_sign = (TextView) findViewById(R.id.tab_shop_sign);
		tab_shop_sign.setOnClickListener(this);
		tab_shop_all_income_lay = (View) findViewById(R.id.tab_shop_all_income_lay);
		All_Income = (TextView) tab_shop_all_income_lay
				.findViewById(R.id.comment_txtarrow_content);

		((TextView) tab_shop_all_income_lay
				.findViewById(R.id.comment_txtarrow_title))
				.setText(getResources().getString(R.string.shop_all_income));
		tab_shop_good_fabu = (View) findViewById(R.id.tab_shop_good_fabu);

		tab_shop_good_oder_guanli = findViewById(R.id.tab_shop_good_oder_guanli);
		tab_shop_good_qudao_guanli = findViewById(R.id.tab_shop_good_qudao_guanli);
		tab_shop_caigoudan = findViewById(R.id.tab_shop_caigoudan);
		tab_shop_pinpaidaili = findViewById(R.id.tab_shop_pinpaidaili);
		tab_shop_good_good_guanli = findViewById(R.id.tab_shop_good_good_guanli);
		tab_shop_xiaoshoutongji = findViewById(R.id.tab_shop_xiaoshoutongji);
		tab_shop_lookshop = findViewById(R.id.tab_shop_lookshop);
		tab_shop_ruzhu = findViewById(R.id.tab_shop_ruzhu);

		SetCommentIV(getResources().getString(R.string.grad1),
				R.drawable.shop_grad1, tab_shop_good_fabu);
		SetCommentIV(getResources().getString(R.string.grad2),
				R.drawable.shop_grad2, tab_shop_good_oder_guanli);
		SetCommentIV(getResources().getString(R.string.grad3),
				R.drawable.shop_grad3, tab_shop_good_qudao_guanli);
		SetCommentIV(getResources().getString(R.string.grad4),
				R.drawable.shop_grad4, tab_shop_caigoudan);
		SetCommentIV(getResources().getString(R.string.grad5),
				R.drawable.shop_grad5, tab_shop_pinpaidaili);
		SetCommentIV(getResources().getString(R.string.grad6),
				R.drawable.shop_grad6, tab_shop_good_good_guanli);
		SetCommentIV(getResources().getString(R.string.grad7),
				R.drawable.shop_grad7, tab_shop_xiaoshoutongji);
		SetCommentIV(getResources().getString(R.string.grad8),
				R.drawable.shop_grad8, tab_shop_lookshop);
		SetCommentIV(getResources().getString(R.string.grad9),
				R.drawable.shop_grad9, tab_shop_ruzhu);

		// 需要展示的txt
		tab_shop_today_income = (TextView) findViewById(R.id.tab_shop_today_income);
		tab_shop_today_volume = (TextView) findViewById(R.id.tab_shop_today_volume);
		tab_shop_today_visitor = (TextView) findViewById(R.id.tab_shop_today_visitor);
		tab_shop_team_number = (TextView) findViewById(R.id.tab_shop_team_number);
		tab_shop_zhijixiashu_number = (TextView) findViewById(R.id.tab_shop_zhijixiashu_number);

		ShowView(Spuit.Shop_Get(BaseContext));

		tab_shop_iv.setOnClickListener(this);
		tab_shop_all_income_lay.setOnClickListener(this);
	}

	@Override
	protected void onResume() {

		super.onResume();

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
		V.setOnClickListener(this);

	}

	@Override
	protected void InitTile() {

	}

	@Override
	protected void InItBundle(Bundle bundle) {

	}

	@Override
	protected void SaveBundle(Bundle bundle) {

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
	protected void DataResult(int Code, String Msg, BComment Data) {
		if (StrUtils.isEmpty(Data.getHttpResultStr())) {
			DataError(Msg, Data.getHttpLoadType());
			return;
		}

		if (LOAD_REFRESHING == Data.getHttpLoadType()) {
			LoadFrashComplet();
		}
		BMyShop mBShop = new BMyShop();
		mBShop = JSON.parseObject(Data.getHttpResultStr(), BMyShop.class);

		myBShop = mBShop.getBase();
		myBShop.setSubCounter(mBShop.getSubCounter());
		myBShop.setTeamCounter(mBShop.getTeamCounter());
		myBShop.setTodayVisitor(mBShop.getTodayVisitor());
		myBShop.setTodayIncome(mBShop.getTodayIncome());
		myBShop.setTodaySales(mBShop.getTodaySales());
		myBShop.setTotalIncome(mBShop.getTotalIncome());

		ShowView(myBShop);

		Spuit.Shop_Save(BaseContext, myBShop);
	}

	private void ShowView(BShop myBShop) {
		if (StrUtils.isEmpty(myBShop.getSeller_name()))// 没有缓存数据
			return;
		ImageLoaderUtil.Load2(StrUtils.NullToStr(myBShop.getAvatar()),
				tab_shop_iv, R.drawable.testiv);
		File CoverFile = new File(ImagePathConfig.ShopCoverPath(BaseContext));
		if (CoverFile.exists()) {// 已经存在了
			iv_shop_cover.setImageBitmap(BitmapFactory
					.decodeFile(ImagePathConfig.ShopCoverPath(BaseContext)));
		} else {
			ImageLoaderUtil.LoadGaosi(BaseContext,
					StrUtils.NullToStr(myBShop.getCover()), iv_shop_cover,
					R.color.app_fen, 1);

		}

		StrUtils.SetTxt(tab_shop_today_income,
				StrUtils.SetTextForMony(myBShop.getTodayIncome()));
		StrUtils.SetTxt(tab_shop_today_volume, myBShop.getTodaySales());
		StrUtils.SetTxt(tab_shop_today_visitor, myBShop.getTodayVisitor());

		StrUtils.SetTxt(tab_shop_team_number, myBShop.getTeamCounter());
		StrUtils.SetTxt(tab_shop_zhijixiashu_number, myBShop.getSubCounter());
		// tab_shop_name = (TextView) findViewById(R.id.tab_shop_name);
		// tab_shop_sign
		StrUtils.SetTxt(tab_shop_name, myBShop.getSeller_name());
		StrUtils.SetTxt(
				tab_shop_sign,
				StrUtils.isEmpty(myBShop.getIntro()) ? "您还未描述店铺" : myBShop
						.getIntro());
		StrUtils.SetTxt(All_Income,
				StrUtils.SetTextForMony(myBShop.getTotalIncome()));
	}

	@Override
	protected void DataError(String error, int LoadTyp) {
		if (LoadTyp == LOAD_REFRESHING) {
			LoadFrashComplet();
			return;
		}
		IData(LOAD_INITIALIZE);
		// PromptManager.ShowCustomToast(BaseContext, error);
	}

	@Override
	protected void MyClick(View V) {
		switch (V.getId()) {
		case R.id.tab_shop_good_fabu:// 产品发布
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					AAddGood.class));
			break;
		case R.id.tab_shop_good_oder_guanli:// 订单管理
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					AShopOrderManager.class));

			break;
		case R.id.tab_shop_good_qudao_guanli:// 渠道管理
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					AChannel.class));
			break;
		case R.id.tab_shop_caigoudan:// 我的采购单
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					AShopPurchaseOrder.class));
			break;
		case R.id.tab_shop_pinpaidaili:// 平拍代理APay
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					ABrandDaiLi.class));
			break;
		case R.id.tab_shop_good_good_guanli:// 商品管理AGoodManger.java
			// PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
			// AShopGoodManger.class));//AGoodManger
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					AGoodManger.class));
			break;
		case R.id.tab_shop_xiaoshoutongji:// 销售统计
			// PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
			// APay.class));
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					ASellStatistics.class));
			break;

		case R.id.tab_shop_iv:// 头像
		case R.id.tab_shop_name:
		case R.id.tab_shop_sign:
		case R.id.iv_shop_cover:
			// 判断是否shop的数据已经存在sp中
			if (!StrUtils.isEmpty(Spuit.Shop_Get(BaseContext).getSeller_name())) {// 已经获取数据
				PromptManager.SkipActivity(BaseActivity, new Intent(
						BaseContext, AShopData.class));
			} else {// 未获取数据 开始请求
				IData(LOAD_INITIALIZE);
			}
			//
			break;

		case R.id.tab_shop_all_income_lay:// 累计收益

			boolean isLogin_RenZheng_Set = Spuit
					.IsLogin_RenZheng_Set(BaseContext);
			if (isLogin_RenZheng_Set) {
				PromptManager.SkipActivity(BaseActivity, new Intent(
						BaseActivity, ACenterWallet.class));
			} else {
				ShowRealAuthDialog();
			}

			// PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
			// ACenterWallet.class));
			break;
		case R.id.tab_shop_lookshop:// 店铺预览
			BComment mBComment = new BComment(Spuit.Shop_Get(BaseContext)
					.getId(), Spuit.Shop_Get(BaseContext).getSeller_name());
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					AShopDetail.class).putExtra(BaseKey_Bean, mBComment));
			break;
		case R.id.tab_shop_ruzhu:// 店入驻
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					ABrandCheck.class));
			break;
		default:
			break;
		}
	}

	/**
	 * 实名认证对话框
	 * 
	 * @param fetch_type
	 * 
	 * @param datBlComment
	 * 
	 * @param aa
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
		/*
		 * final CustomDialog dialog = new CustomDialog(BaseContext,
		 * R.style.mystyle, R.layout.customdialog, 1, "取消", "去认证");
		 * dialog.show(); dialog.setTitleText("您还没有进行实名认证");
		 * dialog.setConfirmListener(new onConfirmClick() {
		 * 
		 * @Override public void onConfirmCLick(View v) { int from_where = 10;
		 * 
		 * PromptManager.SkipActivity(BaseActivity, new Intent( BaseContext,
		 * ARealIdauth.class).putExtra( ARealIdauth.FROM_WHERE_KEY,
		 * from_where));
		 * 
		 * dialog.dismiss(); } }); dialog.setcancelListener(new oncancelClick()
		 * {
		 * 
		 * @Override public void oncancelClick(View v) {
		 * 
		 * dialog.dismiss(); } });
		 */
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		// 注销事件
		try {
			EventBus.getDefault().unregister(this);
		} catch (Exception e) {
		}

	}

	/**
	 * 接收事件
	 * 
	 * @param event
	 */

	public void OnGetMessage(BMessage event) {
		// myBShop = ;
		int messageType = event.getMessageType();
		// if (messageType == BMessage.getTageShopData()) {
		// ShowView(myBShop);
		//
		// }
		switch (messageType) {
		case BMessage.Tage_Main_To_ShowGaoSi:
			if (!StrUtils.isEmpty(myBShop.getCover()))
				ImageLoaderUtil.LoadGaosi(BaseContext, myBShop.getCover(),
						iv_shop_cover, R.color.app_fen, 1);
			break;
		case BMessage.Tage_Shop_data_shopname_change:

			StrUtils.SetTxt(tab_shop_name, Spuit.Shop_Get(BaseContext)
					.getSeller_name());

			break;
		case BMessage.Tage_Shop_data_desc_change:

			StrUtils.SetTxt(tab_shop_sign, Spuit.Shop_Get(BaseContext)
					.getIntro());

			break;
		case BMessage.Tage_Shop_data_cover_change:

			ImageLoaderUtil.Load(Spuit.Shop_Get(BaseContext).getAvatar(),
					tab_shop_iv, R.drawable.testiv);

			break;
		case BMessage.Tage_Shop_data_background_change:
			ImageLoaderUtil.LoadGaosi(BaseContext, Spuit.Shop_Get(BaseContext)
					.getCover(), iv_shop_cover, R.drawable.item_shangji_iv, 0);
			break;

		default:
			break;
		}
	}

	private void LoadFrashComplet() {
		Message m = new Message();
		m.what = 101;
		mHandler.sendMessage(m);
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 101) {
				if (secondAnimation != null)
					secondAnimation.stop();
				shop_out_scrollview.stopRefresh();
			}

		}
	};

}
