package io.vtown.WeiTangApp.ui.title.center.wallet;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BCBankCardAndAlipayInfo;
import io.vtown.WeiTangApp.bean.bcomment.easy.BLBankCardAndAlipayList;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.pop.PPassWord;
import io.vtown.WeiTangApp.event.interf.OnPasswordInputFinish;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.io.Serializable;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-19 下午2:45:52
 * 
 */
public class ATiXian extends ATitleBase {
	/**
	 * 支付宝和银行卡
	 */
	private TextView tixian_zhifubao, tixian_yinhangka;
	/**
	 * 支付宝布局
	 */
	private LinearLayout ll_alipay_content;
	/**
	 * 银行卡布局
	 */
	private LinearLayout ll_bank_content;
	/**
	 * 支付宝账户
	 */
	private View alipay_account;
	/**
	 * 最大可转金额
	 */
	private EditText et_this_time_allow_transfer_money_alipay;
	/**
	 * 银行卡账户
	 */
	private View bank_card_account;
	/**
	 * 最大可转金额
	 */
	private EditText et_this_time_allow_transfer_money_bank;
	/**
	 * 箭头图标
	 */
	private ImageView iv_arrow_right;
	/**
	 * 账户图标
	 */
	private ImageView iv_my_account_icon;
	/**
	 * 账户姓名
	 */
	private TextView tv_my_account_name;
	/**
	 * 账号
	 */
	private TextView tv_my_account_number;
	/**
	 * 申请
	 */
	private TextView tv_apply_withdraw_deposit;
	/**
	 * 提现规则
	 */
	private TextView tixianguize_txt;

	/**
	 * 1:银行卡，2:支付宝
	 */
	private int fetch_type = 2;
	private BLBankCardAndAlipayList bank_list;
	private BLBankCardAndAlipayList alipay_list;
	/**
	 * 没有银行卡时
	 */
	private View no_bank_card_account;

	/**
	 * 没有添加支付宝时
	 */
	private View no_alipay_account;
	/**
	 * 用户信息
	 */
	private BUser user_Get;
	/**
	 * 获取数据成功时显示的布局
	 */
	private LinearLayout center_wallet_tixian_outlay;

	/**
	 * 提示
	 */
	private TextView tv_bank_card_limit;
	private View view;
	private View tixian_nodata_lay;


	@Override
	protected void InItBaseView() {

		setContentView(R.layout.activity_center_wallet_tixian);
		view = LayoutInflater.from(BaseContext).inflate(R.layout.activity_center_wallet_tixian, null);
		user_Get = Spuit.User_Get(getApplicationContext());
		SetTitleHttpDataLisenter(this);
		IBase();


		IData();

	}


	/**
	 * 获取支付宝
	 */
	private void IData() {

		BCBankCardAndAlipayInfo alipaybankinfo = (BCBankCardAndAlipayInfo) getIntent().getSerializableExtra("alipaybankinfo");
		if(null == alipaybankinfo){
			GetData();
		}else{
			RefreshAlipayView(alipaybankinfo);
		}

	}

	private void GetData(){
				PromptManager.showtextLoading(BaseContext,
				getResources()
						.getString(R.string.xlistview_header_hint_loading));
		HashMap<String, String> map = new HashMap<String, String>();
		//map.put("api_version", "2.0.1");//API版本上线2.0.1
		map.put("member_id", user_Get.getId());
		FBGetHttpData(map, Constants.Get_Tixian_Message, Method.GET, 0,
				LOAD_INITIALIZE);

	}

	/**
	 * 
	 * 提现
	 * 
	 * @param fetch_type
	 * @param BL
	 * @param money
	 */
	private void ApplyWithdraw(int fetch_type, BLBankCardAndAlipayList BL, String money,
			String pay_password) {
		PromptManager.showLoading(BaseContext);
		HashMap<String, String> map = new HashMap<String, String>();
		switch (fetch_type) {
		case 2:
			map.put("member_id", user_Get.getId());
			map.put("name", BL.getName());
			map.put("fetch_money", money);
			map.put("fetch_type", fetch_type + "");
			map.put("alipay", BL.getAlipay());
			break;

		case 1:
			map.put("bank_id", BL.getBank_id());
			map.put("bank_name", BL.getBank_name());
			map.put("bank_card", BL.getCard_number());
			map.put("member_id", user_Get.getId());
			map.put("name", user_Get.getName());
			map.put("fetch_money", money);
			map.put("fetch_type", fetch_type + "");
			break;

		}
		String rsa_psw = Constants.RSA(pay_password, BaseContext);
		map.put("pay_password", rsa_psw);

		FBGetHttpData(map, Constants.Apply_Withdraw, Method.POST, 2,
				LOAD_INITIALIZE);
	}

	private void IBase() {
		tixianguize_txt = (TextView) findViewById(R.id.tixianguize_txt);
		tv_bank_card_limit = (TextView) findViewById(R.id.tv_bank_card_limit);
		center_wallet_tixian_outlay = (LinearLayout) findViewById(R.id.center_wallet_tixian_outlay);
		center_wallet_tixian_outlay.setVisibility(View.GONE);
		tixian_nodata_lay = findViewById(R.id.tixian_nodata_lay);

		tixian_zhifubao = (TextView) findViewById(R.id.tixian_zhifubao);
		tixian_yinhangka = (TextView) findViewById(R.id.tixian_yinhangka);
		ll_alipay_content = (LinearLayout) findViewById(R.id.ll_alipay_content);
		ll_bank_content = (LinearLayout) findViewById(R.id.ll_bank_content);
		alipay_account = findViewById(R.id.alipay_account);
		et_this_time_allow_transfer_money_alipay = (EditText) findViewById(R.id.et_this_time_allow_transfer_money_alipay);
		bank_card_account = findViewById(R.id.bank_card_account);
		et_this_time_allow_transfer_money_bank = (EditText) findViewById(R.id.et_this_time_allow_transfer_money_bank);
		iv_arrow_right = (ImageView) bank_card_account
				.findViewById(R.id.iv_arrow_right);
		ImageView iv_my_account_icon = (ImageView) alipay_account
				.findViewById(R.id.iv_my_account_icon);
		iv_my_account_icon.setImageResource(R.drawable.alipay_log);
		tv_apply_withdraw_deposit = (TextView) findViewById(R.id.tv_apply_withdraw_deposit);

		no_bank_card_account = findViewById(R.id.no_bank_card_account);
		no_alipay_account = findViewById(R.id.no_alipay_account);

		((TextView) no_alipay_account
				.findViewById(R.id.comment_txtarrow_content))
				.setText("请点击添加支付宝");
		((TextView) no_bank_card_account
				.findViewById(R.id.comment_txtarrow_content))
				.setText("请点击添加银行卡");

		tixian_zhifubao.setOnClickListener(this);
		tixian_yinhangka.setOnClickListener(this);
		bank_card_account.setOnClickListener(this);
		tv_apply_withdraw_deposit.setOnClickListener(this);
		no_alipay_account.setOnClickListener(this);
		no_bank_card_account.setOnClickListener(this);

	}

	@Override
	protected void InitTile() {
		SetTitleTxt("提现");
	}

	/**
	 * 刷新支付宝View
	 * 
	 * @param datas
	 */
	private void RefreshAlipayView(BCBankCardAndAlipayInfo datas) {
		center_wallet_tixian_outlay.setVisibility(View.VISIBLE);
		tixian_nodata_lay.setVisibility(View.GONE);
		if (datas.getBank_list() != null) {
			bank_list = datas.getBank_list().get(0);
		}
		alipay_list = datas.getAlipay_list();
		StrUtils.SetColorsTxt_lettersize(BaseContext, tixianguize_txt, R.color.app_fen1,
				"提现规则:", datas.getTixinarule());
		StrUtils.SetColorsTxt_lettersize(BaseContext, tv_bank_card_limit, R.color.app_fen1,
				"提现规则:", datas.getTixinarule());

		if (alipay_list != null && !StrUtils.isEmpty(alipay_list.getAlipay())) {
			tixian_zhifubao.setVisibility(View.VISIBLE);
			tixian_yinhangka.setVisibility(View.VISIBLE);
			if (datas.getBank_list() == null) {
				tixian_yinhangka.setVisibility(View.GONE);
				tixian_zhifubao.setVisibility(View.GONE);
//				tixian_zhifubao.setBackground(getResources().getDrawable(
//						R.drawable.shape_fen_circular_bg));
//				tixian_zhifubao.setTextColor(getResources().getColor(
//						R.color.white));
//				tixian_zhifubao.setEnabled(false);
				ll_bank_content.setVisibility(View.GONE);
				ll_alipay_content.setVisibility(View.VISIBLE);
				fetch_type = 2;
			}



			TextView tv_my_account_name = (TextView) alipay_account
					.findViewById(R.id.tv_my_account_name);
			TextView tv_my_account_number = (TextView) alipay_account
					.findViewById(R.id.tv_my_account_number);
			String real_name = getResources().getString(
					R.string.alipay_real_name_format);
			String alipay_numb = getResources().getString(
					R.string.lable_alipay_numb_format);
			StrUtils.SetTxt(
					tv_my_account_name,
					String.format(real_name,
							StrUtils.NullToStr(alipay_list.getName())));
			StrUtils.SetTxt(tv_my_account_number,
					String.format(alipay_numb, alipay_list.getAlipay()));
			no_alipay_account.setVisibility(View.GONE);
		}

		if (datas.getBank_list() != null) {
			if (StrUtils.isEmpty(alipay_list.getAlipay())) {
				tixian_yinhangka.setVisibility(View.GONE);
				tixian_zhifubao.setVisibility(View.GONE);
//				tixian_yinhangka.setBackground(getResources().getDrawable(
//						R.drawable.shape_fen_circular_bg));
//				tixian_yinhangka.setTextColor(getResources().getColor(
//						R.color.white));
//				tixian_yinhangka.setEnabled(false);
				ll_alipay_content.setVisibility(View.GONE);
				ll_bank_content.setVisibility(View.VISIBLE);
				fetch_type = 1;
				iv_arrow_right.setVisibility(View.VISIBLE);
				iv_arrow_right.setImageResource(R.drawable.arrow_right);
			}

			iv_my_account_icon = (ImageView) bank_card_account
					.findViewById(R.id.iv_my_account_icon);
			tv_my_account_name = (TextView) bank_card_account
					.findViewById(R.id.tv_my_account_name);
			tv_my_account_number = (TextView) bank_card_account
					.findViewById(R.id.tv_my_account_number);
			ImageLoaderUtil.Load2(bank_list.getIcon(), iv_my_account_icon,
					R.drawable.error_iv2);
			StrUtils.SetTxt(tv_my_account_name, bank_list.getBank_name());
			String bank_numb = getResources().getString(
					R.string.lable_bank_numb_format);
			String card_number = bank_list.getCard_number();
			String card_number_sub = card_number.substring(
					card_number.length() - 4, card_number.length());
			StrUtils.SetTxt(tv_my_account_number,
					String.format(bank_numb, card_number_sub));
			no_bank_card_account.setVisibility(View.GONE);

		}
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		tv_apply_withdraw_deposit.setEnabled(true);
		switch (Data.getHttpResultTage()) {
		case 0:

			// BLComment datas = new BLComment();
			BCBankCardAndAlipayInfo datas = new BCBankCardAndAlipayInfo();
			try {

				datas = JSON.parseObject(Data.getHttpResultStr(),
						BCBankCardAndAlipayInfo.class);

			} catch (Exception e) {
				DataError("解析失败", 1);
			}

			RefreshAlipayView(datas);

			break;

		case 2:
			PromptManager.ShowMyToast(BaseContext, "提现成功");
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void DataError(String error, int LoadTyp) {
		tv_apply_withdraw_deposit.setEnabled(true);
		PromptManager.ShowMyToast(BaseContext, error);
		 if(LOAD_INITIALIZE == LoadTyp){
			 center_wallet_tixian_outlay.setVisibility(View.VISIBLE);
			 tixian_nodata_lay.setVisibility(View.GONE);

		 }
		switch (LoadTyp) {
		case LOAD_REFRESHING:// 获取支付宝失败===》默认未绑定支付宝

			break;
		case LOAD_LOADMOREING:// 获取银行卡失败===》未绑定银行卡
			break;
		default:
			break;
		}
	}

	@Override
	protected void NetConnect() {
		NetError.setVisibility(View.GONE);
		tv_apply_withdraw_deposit.setEnabled(true);
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
		boolean isLogin_RenZheng_Set = Spuit.IsLogin_RenZheng_Set(getApplicationContext());
		switch (V.getId()) {
		case R.id.tixian_zhifubao:// 支付宝

			ControlClick(R.id.tixian_zhifubao);
			ll_bank_content.setVisibility(View.GONE);
			ll_alipay_content.setVisibility(View.VISIBLE);
			fetch_type = 2;
			break;
		case R.id.tixian_yinhangka:// 银行卡

			ControlClick(R.id.tixian_yinhangka);
			ll_alipay_content.setVisibility(View.GONE);
			ll_bank_content.setVisibility(View.VISIBLE);
			fetch_type = 1;
			iv_arrow_right.setVisibility(View.VISIBLE);
			iv_arrow_right.setImageResource(R.drawable.arrow_right);

			break;

			case R.id.tv_apply_withdraw_deposit:
				tv_apply_withdraw_deposit.setEnabled(false);
				switch (fetch_type){
					case  1:
						String transferMoney1 = et_this_time_allow_transfer_money_bank
								.getText().toString().trim();
						if (!StrUtils.checkMoney(BaseContext, transferMoney1, 100, 50000)) {
							tv_apply_withdraw_deposit.setEnabled(true);
							return;
						}
						if (bank_list != null) {
							if (CheckNet(BaseContext))
								return;
							PassView(fetch_type, transferMoney1);
						}
						break;

					case 2:
						String transferMoney = et_this_time_allow_transfer_money_alipay
								.getText().toString().trim();

						if (!StrUtils.checkMoney(BaseContext, transferMoney, 100, 50000)) {
							tv_apply_withdraw_deposit.setEnabled(true);
							return;
						}
						if (CheckNet(BaseContext))
							return;
						PassView(fetch_type,transferMoney);
						break;
				}



				break;

		case R.id.bank_card_account:
			if (CheckNet(BaseContext))
				return;
			PromptManager.SkipResultActivity(BaseActivity, new Intent(
					BaseActivity, ABankCardManager.class).putExtra("isFinish",
					true), 1);

			break;


		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (1 == requestCode && resultCode == RESULT_OK) {
			bank_list = (BLBankCardAndAlipayList) data.getSerializableExtra("bankinfo");
			ImageLoaderUtil.Load2(bank_list.getIcon(), iv_my_account_icon,
					R.drawable.error_iv2);
			StrUtils.SetTxt(tv_my_account_name, bank_list.getBank_name());
			String bank_numb = getResources().getString(
					R.string.lable_bank_numb_format);
			String card_number = bank_list.getCard_number();
			String card_number_sub = card_number.substring(
					card_number.length() - 4, card_number.length());
			StrUtils.SetTxt(tv_my_account_number,
					String.format(bank_numb, card_number_sub));
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	private void ControlClick(int ClickId) {
		tixian_zhifubao
				.setBackground(R.id.tixian_zhifubao == ClickId ? getResources()
						.getDrawable(R.drawable.shape_left_pre)
						: getResources().getDrawable(R.drawable.shape_left_nor));
		tixian_zhifubao
				.setTextColor(R.id.tixian_zhifubao == ClickId ? getResources()
						.getColor(R.color.TextColorWhite) : getResources()
						.getColor(R.color.app_fen));

		tixian_yinhangka
				.setBackground(R.id.tixian_yinhangka == ClickId ? getResources()
						.getDrawable(R.drawable.shape_right_pre)
						: getResources()
								.getDrawable(R.drawable.shape_right_nor));
		tixian_yinhangka
				.setTextColor(R.id.tixian_yinhangka == ClickId ? getResources()
						.getColor(R.color.TextColorWhite) : getResources()
						.getColor(R.color.app_fen));

	}

	/**
	 * 密码输入
	 * @param type
	 * @param name
	 * @param id_numb
	 */
	private void PassView(final int fetch_type,final String money) {
		final PPassWord p = new PPassWord(BaseContext, screenWidth,
				getString(R.string.please_input_6_bit_reset_psd));

		p.setOnPassWordListener(new OnPasswordInputFinish() {

			@Override
			public void inputFinish(String getStrPassword) {

				p.dismiss();
				switch (fetch_type) {
					case 2:
					int money_int = (int) (Float.parseFloat(money) * 100);
					ApplyWithdraw(2, alipay_list, money_int + "",getStrPassword);
					break;

				case 1:


						int money1_int = (int) (Float.parseFloat(money) * 100);
						ApplyWithdraw(1, bank_list, money1_int + "",getStrPassword);

					break;

				default:
					break;
				}
				
			}

			@Override
			public void LostPassWord() {
				PromptManager.ShowCustomToast(BaseContext, "忘记密码怎么办");
			}

			@Override
			public void Cancle() {
				p.dismiss();
				tv_apply_withdraw_deposit.setEnabled(true);
			}
		});
		p.showAtLocation(view, Gravity.CENTER, 0, 0);
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

}
