package io.vtown.WeiTangApp.ui.title.center.wallet;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BCBankCardAndAlipayInfo;
import io.vtown.WeiTangApp.bean.bcomment.easy.wallet.BDCenterWallet;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.PullScrollView;
import io.vtown.WeiTangApp.comment.view.PullScrollView.onRefreshListener;
import io.vtown.WeiTangApp.comment.view.custom.RefreshLayout;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.onConfirmClick;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.oncancelClick;
import io.vtown.WeiTangApp.comment.view.listview.SecondStepView;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.util.HashMap;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-19 下午1:56:46 个人中心的我的钱包
 * 
 */
public class ACenterWallet extends ATitleBase implements RefreshLayout.OnLoadListener{
private RefreshLayout center_wallter_refrash;
	/**
	 * 我的资产
	 */
	private TextView tv_my_property;
	/**
	 * 可提现余额
	 */
	private TextView tv_submit_balance;
	/**
	 * 交易中金额
	 */
	private TextView tv_trade_money;
	/**
	 * 累计收益
	 */
	private TextView tv_add_up_income;
	/**
	 * 提交按钮
	 */
	private View tv_btn_submit;
	/**
	 * 银行卡管理
	 */
	private View bank_card_manage;
	/**
	 * 资金明细
	 */
	private View property_detail;
	/**
	 * 支付宝管理
	 */
	private View alipay_manage;
	/**
	 * 用户相关信息
	 */
	private BUser user_Get;
	private LinearLayout center_wallet_outlay;
	private View center_wallet_nodata_lay;

	// 刷新的相关view
//	private PullScrollView wallet_out_scrollview;
//	private SecondStepView wallet_load_head_iv;
//	private AnimationDrawable secondAnimation;

	/**
	 * 获取到的数据
	 */
	private BDCenterWallet data = null;
	private BCBankCardAndAlipayInfo data1;

	/**
	 * 是否是获取页面数据
	 */
	private boolean isGetData = false;

	/**
	 * Dialog
	 */
	private CustomDialog dialog;

	/**
	 * 只有第一次进入的时候才会获取数据
	 */
	private boolean isFristInto = false;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_center_wallet);
		SetTitleHttpDataLisenter(this);
		// 注册事件
		EventBus.getDefault().register(this, "OnGetMessage", BMessage.class);
		user_Get = Spuit.User_Get(getApplicationContext());
		IBase();
		ICache();
		isFristInto = true;
	}

	/**
	 * 缓存处理
	 */
	private void ICache() {
		String center_Wallet_Get = CacheUtil.Center_Wallet_Get(BaseContext);
		if (!StrUtils.isEmpty(center_Wallet_Get)) {
			IDataView(center_wallet_outlay, center_wallet_nodata_lay,
					NOVIEW_RIGHT);
			try {
				data = JSON
						.parseObject(center_Wallet_Get, BDCenterWallet.class);
				RefreshView(data);
			} catch (Exception e) {
				IDataView(center_wallet_outlay, center_wallet_nodata_lay,
						NOVIEW_INITIALIZE);

				PromptManager.ShowCustomToast(BaseContext, "缓存数据解析失败");
			}

		} else {
			PromptManager.showtextLoading(BaseContext, getResources()
					.getString(R.string.loading));
		}

	}

	@Override
	protected void onResume() {

		super.onResume();
		IData(LOAD_INITIALIZE);
		ITiXianData();
		
	}

	/**
	 * 获取账户信息
	 */
	private void IData(int Type) {

		HashMap<String, String> map = new HashMap<String, String>();
		isGetData = true;
		map.put("member_id", user_Get.getId());
		FBGetHttpData(map, Constants.Select_Account_Message, Method.GET, 0,
				Type);

	}

	/**
	 * 获取提现
	 */
	private void ITiXianData() {
		HashMap<String, String> map = new HashMap<String, String>();
		isGetData = false;
		map.put("member_id", user_Get.getId());
		FBGetHttpData(map, Constants.Get_Tixian_Message, Method.GET, 1,
				LOAD_INITIALIZE);
	}

	private void IBase() {
		center_wallter_refrash= (RefreshLayout) findViewById(R.id.center_wallter_refrash);
		center_wallter_refrash.setOnLoadListener(this);
		center_wallter_refrash.setColorSchemeResources(R.color.app_fen, R.color.app_fen1, R.color.app_fen2, R.color.app_fen3);
		center_wallter_refrash.setCanLoadMore(false);


		center_wallet_outlay = (LinearLayout) findViewById(R.id.center_wallet_outlay);
		center_wallet_nodata_lay = findViewById(R.id.center_wallet_nodata_lay);
		IDataView(center_wallet_outlay, center_wallet_nodata_lay,
				NOVIEW_INITIALIZE);

		tv_my_property = (TextView) findViewById(R.id.tv_my_property);
		tv_submit_balance = (TextView) findViewById(R.id.tv_submit_balance);
		tv_trade_money = (TextView) findViewById(R.id.tv_trade_money);
		tv_add_up_income = (TextView) findViewById(R.id.tv_add_up_income);
		tv_btn_submit = findViewById(R.id.tv_btn_submit);


		bank_card_manage = findViewById(R.id.bank_card_manage);
		property_detail = findViewById(R.id.property_detail);
		alipay_manage = findViewById(R.id.alipay_manage);

		SetItemContent(bank_card_manage, R.string.bank_card_manage, "");
		SetItemContent(alipay_manage, R.string.alipay_manage, "");
		SetItemContent(property_detail, R.string.property_detail, "");
		SetItemContent(tv_btn_submit, R.string.sbumit_tixian, "");

		tv_btn_submit.setOnClickListener(this);
		center_wallet_nodata_lay.setOnClickListener(this);
		// 刷新
//		wallet_out_scrollview = (PullScrollView) findViewById(R.id.wallet_out_scrollview);
//		wallet_load_head_iv = (SecondStepView) findViewById(R.id.wallet_load_head_iv);
//		wallet_load_head_iv
//				.setBackgroundResource(R.drawable.second_step_animation);
//		secondAnimation = (AnimationDrawable) wallet_load_head_iv
//				.getBackground();
//		wallet_out_scrollview.setOnRefreshListener(new onRefreshListener() {
//
//			@Override
//			public void refresh() {
//
//				// LoadFrashComplet();
//				secondAnimation.start();
//				IData(LOAD_REFRESHING);
//			}
//
//		});

	}

	private void SetItemContent(View VV, int ResourceTitle, String ResourceRight) {
		((TextView) VV.findViewById(R.id.comment_txtarrow_title))
				.setText(getResources().getString(ResourceTitle));
		if (!StrUtils.isEmpty(ResourceRight)) {
			((TextView) VV.findViewById(R.id.comment_txtarrow_content))
					.setText(ResourceRight);
		}
		VV.setOnClickListener(this);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt("我的钱包");
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {

		if (LOAD_REFRESHING == Data.getHttpLoadType()) {
			LoadFrashComplet();
		}

		if (StrUtils.isEmpty(Data.getHttpResultStr())) {
			return;
		}

		switch (Data.getHttpResultTage()) {
		case 0:
			data = new BDCenterWallet();
			try {
				data = JSON.parseObject(Data.getHttpResultStr(),
						BDCenterWallet.class);

			} catch (Exception e) {
				// DataError("解析失败", 1);
			}
			IDataView(center_wallet_outlay, center_wallet_nodata_lay,
					NOVIEW_RIGHT);
			CacheUtil.Center_Wallet_Save(BaseContext, Data.getHttpResultStr());
			RefreshView(data);
			isFristInto = false;
			break;

		case 1:
			data1 = new BCBankCardAndAlipayInfo();
			try {
				data1 = JSON.parseObject(Data.getHttpResultStr(),
						BCBankCardAndAlipayInfo.class);


			} catch (Exception e) {
				// DataError("解析失败", 1);

			}
			break;

		default:
			break;
		}

	}

	/**
	 * 刷新数据
	 * 
	 * @param data2
	 */
	private void RefreshView(BDCenterWallet data2) {
		StrUtils.SetTxt(tv_my_property,
				StrUtils.SetTextForMony(data2.getAssets())+"元");
		StrUtils.SetTxt(tv_submit_balance,
				StrUtils.SetTextForMony(data2.getBlance())+"元");
		StrUtils.SetTxt(tv_trade_money,
				StrUtils.SetTextForMony(data2.getFreeze())+"元");
		StrUtils.SetTxt(tv_add_up_income,
				StrUtils.SetTextForMony(data2.getIncome())+"元");
	}

	@Override
	protected void DataError(String error, int LoadTyp) {
		PromptManager.ShowMyToast(BaseContext, error);

		if (LOAD_INITIALIZE == LoadTyp && isGetData) {

			IDataView(center_wallet_outlay, center_wallet_nodata_lay,
					data == null ? NOVIEW_ERROR : NOVIEW_RIGHT);

		}

		if (LOAD_REFRESHING == LoadTyp) {
			LoadFrashComplet();
			IDataView(center_wallet_outlay, center_wallet_nodata_lay,
					NOVIEW_RIGHT);
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
		case R.id.tv_btn_submit:// 提现
			if (CheckNet(BaseContext))
				return;
			if(data1 != null){

				if (data1.getBank_list() == null
						&& StrUtils.isEmpty(data1.getAlipay_list().getAlipay())) {
					ShowCustomDialog(2, "请先绑定银行卡或支付宝", "");
				} else {
					PromptManager.SkipActivity(BaseActivity, new Intent(
							BaseActivity, ATiXian.class).putExtra("alipaybankinfo",data1));
				}
			}else{
				PromptManager.ShowCustomToast(BaseContext,"正在努力获取数据，请稍候……");
				return;
			}


			break;
		case R.id.bank_card_manage:
			if (CheckNet(BaseContext))
				return;
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					ABankCardManager.class).putExtra("isFinish", false));

			break;
		case R.id.property_detail:
			if (CheckNet(BaseContext))
				return;
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					APropertyDetail.class));
			break;

		case R.id.alipay_manage:
			if (CheckNet(BaseContext))
				return;
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					AAlipayManager.class));
			break;

		case R.id.center_wallet_nodata_lay:// 重新加载数据
			if (CheckNet(BaseContext))
				return;
			IData(LOAD_INITIALIZE);
			break;

		}
	}

	/**
	 * 
	 * 弹出对话框
	 * 
	 * @param type
	 * @param title1
	 * @param title2
	 * @param
	 */
	private void ShowCustomDialog(int type, String title1, String title2) {
		dialog = new CustomDialog(BaseContext, R.style.mystyle,
				R.layout.dialog_purchase_cancel, 1, "绑定支付宝", "绑定银行卡");
		dialog.show();
		dialog.setTitleText(title1);
		if (2 == type) {
			dialog.setTitleText2(title2);
		}
		dialog.setCanceledOnTouchOutside(false);
		dialog.setcancelListener(new oncancelClick() {

			@Override
			public void oncancelClick(View v) {
				// 绑定支付宝
				Intent intent = new Intent(BaseContext, AAddAliPay.class);
				intent.putExtra("togo", 1);
				PromptManager.SkipActivity(BaseActivity, intent);
				dialog.dismiss();
			}
		});

		dialog.setConfirmListener(new onConfirmClick() {
			@Override
			public void onConfirmCLick(View v) {

				// 绑定银行卡
				Intent intent2 = new Intent(BaseContext, AAddBankCard.class);
				intent2.putExtra("togo", 1);
				PromptManager.SkipActivity(BaseActivity, intent2);
				dialog.dismiss();

			}
		});
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 101) {
				center_wallter_refrash.setRefreshing(false);
			}

		}
	};

	private void LoadFrashComplet() {
		Message m = new Message();
		m.what = 101;
		mHandler.sendMessage(m);
	}
	
	/**
	 * 接收事件
	 * 
	 * @param event
	 */

	public void OnGetMessage(BMessage event) {
		int messageType = event.getMessageType();
		if (messageType == event.Tage_Updata_Tixian_Message) {
			ITiXianData();;
		}

		
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		try {
			EventBus.getDefault().unregister(new BMessage());
		} catch (Exception e) {
			
		}
	}

	@Override
	public void OnLoadMore() {

	}

	@Override
	public void OnFrash() {
		IData(LOAD_REFRESHING);
	}
}
