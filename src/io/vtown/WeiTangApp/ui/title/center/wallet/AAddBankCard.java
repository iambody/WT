package io.vtown.WeiTangApp.ui.title.center.wallet;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-20 上午9:46:03 银行卡管理之添加银行卡页面
 */
public class AAddBankCard extends ATitleBase {

	/**
	 * 输入银行卡卡号
	 */
	private EditText et_bank_card_numb;
	/**
	 * 选择银行
	 */
	private View select_bank;
	/**
	 * 我同意选择按钮
	 */
	// private CheckBox cb_i_agree;
	/**
	 * 银行授权协议
	 */
	private TextView tv_bank_transfer_agreement;
	/**
	 * 提交按钮
	 */
	private TextView tv_btn_submit_bank_card;
	private BLComment mBlComment;
	/**
	 * 用户信息
	 */
	private BUser user_Get;
	/**
	 * 完成之后要跳到哪里
	 */
	private int togo;
	/**
	 * 显示银行卡名称
	 */
	private TextView comment_txtarrow_content;
	/**
	 * 真实姓名
	 */
	private TextView tv_bind_bank_card_real_name;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_center_wallet_bankcard_manager_add_bankcard);
		user_Get = Spuit.User_Get(BaseContext);
		togo = getIntent().getIntExtra("togo", 0);
		IView();
	}

	private void IView() {
		et_bank_card_numb = (EditText) findViewById(R.id.et_bank_card_numb);
		select_bank = findViewById(R.id.select_bank);
		tv_bank_transfer_agreement = (TextView) findViewById(R.id.tv_bank_transfer_agreement);
		tv_btn_submit_bank_card = (TextView) findViewById(R.id.tv_btn_submit_bank_card);
		tv_bind_bank_card_real_name = (TextView) findViewById(R.id.tv_bind_bank_card_real_name);
		StrUtils.SetColorsTxt(BaseContext, tv_bind_bank_card_real_name,
				R.color.app_gray, "姓名：", Spuit.User_Get(BaseContext).getName());
		SetItemContent(select_bank, R.string.select_bank, "");
		tv_bank_transfer_agreement.setOnClickListener(this);
		tv_btn_submit_bank_card.setOnClickListener(this);
	}

	private void IData(String cardNo) {

		BUser user_Get = Spuit.User_Get(BaseContext);
		String name = user_Get.getName();

		SetTitleHttpDataLisenter(this);
		PromptManager.showLoading(BaseContext);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("member_id", user_Get.getId());
		map.put("seller_id", user_Get.getSeller_id());
		map.put("name", name);
		map.put("bank_name", mBlComment.getBank_name());
		map.put("card_number", cardNo);
		FBGetHttpData(map, Constants.Bank_Manage_Add_Card, Method.POST, 0,
				LOAD_INITIALIZE);

	}

	private void SetItemContent(View VV, int ResourceTitle, String str) {
		((TextView) VV.findViewById(R.id.comment_txtarrow_title))
				.setText(getResources().getString(ResourceTitle));
		comment_txtarrow_content = ((TextView) VV
				.findViewById(R.id.comment_txtarrow_content));
		comment_txtarrow_content.setText(str);

		VV.setOnClickListener(this);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.add_bank_card));
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {

		if (Code == 200) {
			PromptManager.ShowMyToast(BaseContext, "银行卡添加成功");
			Intent intent = null;
			if (1 == togo) {
				intent = new Intent(BaseContext, ATiXian.class);
			} else {
				intent = new Intent(BaseContext, ABankCardManager.class);
				intent.putExtra("isFinish", false);
			}
			EventBus.getDefault().post(new BMessage(BMessage.Tage_Updata_Tixian_Message));
			startActivity(intent);
			EventBus.getDefault().post(
					new BMessage(BMessage.Tage_Updata_BankCard_List));
			finish();
		} else {
			DataError("银行卡添加失败", 1);
		}

	}

	@Override
	protected void DataError(String error, int LoadTyp) {

		PromptManager.ShowMyToast(BaseContext, error);
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
		case R.id.select_bank:
			PromptManager.SkipResultActivity(BaseActivity, new Intent(
					BaseActivity, ASelectBank.class), 0);
			break;

		case R.id.tv_bank_transfer_agreement:

			break;

		case R.id.tv_btn_submit_bank_card:
			String cardNo = et_bank_card_numb.getText().toString().trim();

			if (!StrUtils.checkBankCard(BaseContext, cardNo)) {
				return;
			}

			String bank_name = comment_txtarrow_content.getText().toString()
					.trim();
			if (StrUtils.isEmpty(bank_name)) {
				PromptManager.ShowMyToast(BaseContext, "选择您要绑定的银行");
				return;
			}

			// if (!cb_i_agree.isChecked()) {
			// PromptManager.ShowMyToast(BaseContext, "请阅读并同意《银行转账授权协议》");
			// return;
			// }
			if (CheckNet(BaseContext))
				return;
			IData(cardNo);
			break;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (0 == requestCode && resultCode == RESULT_OK) {
			mBlComment = (BLComment) data.getSerializableExtra("bank_info");
			SetItemContent(select_bank, R.string.select_bank,
					mBlComment.getBank_name());
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

}
