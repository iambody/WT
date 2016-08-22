package io.vtown.WeiTangApp.ui.title.center.set;

import android.R.integer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.pop.PPassWord;
import io.vtown.WeiTangApp.event.interf.OnPasswordInputFinish;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-22 下午4:26:35 实名认证页面
 * 
 */
public class AAuthentication extends ATitleBase {

	/**
	 * 请输入真实姓名
	 */
	private EditText et_authentication_input_real_name;
	/**
	 * 请输入身体证号
	 */
	private EditText et_authentication_input_number_id;
	/**
	 * 下一步按钮
	 */
	private Button btn_authentication_next_step;
	private View bVivew;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_center_set_personal_data_authentication);
		bVivew = LayoutInflater
				.from(this)
				.inflate(
						R.layout.activity_center_set_personal_data_authentication,
						null);
		IView();
	}

	private void IView() {
		et_authentication_input_real_name = (EditText) findViewById(R.id.et_authentication_input_real_name);
		et_authentication_input_number_id = (EditText) findViewById(R.id.et_authentication_input_number_id);
		btn_authentication_next_step = (Button) findViewById(R.id.btn_authentication_next_step);
		btn_authentication_next_step.setOnClickListener(this);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getString(R.string.authentication));
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
	}

	@Override
	protected void DataError(String error, int LoadTyp) {
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
		String real_name = et_authentication_input_real_name.getText()
				.toString().trim();
		String number_id = et_authentication_input_number_id.getText()
				.toString().trim();

		switch (V.getId()) {
		case R.id.btn_authentication_next_step:

			if (StrUtils.isEmpty(real_name)) {
				PromptManager.ShowMyToast(BaseContext, getString(R.string.please_input_your_real_name));
				return;
			}
			if (StrUtils.isEmpty(number_id)) {
				PromptManager.ShowMyToast(BaseContext, getString(R.string.please_input_your_number_id));
				return;
			}
			if (!isIDNO(number_id)) {
				PromptManager.ShowMyToast(BaseContext, getString(R.string.the_number_id_format_error_input_restart));
				return;
			}

			PassView(0);

			break;

		default:
			break;
		}
	}

	private boolean isIDNO(String id) {
		String telRegex = "[\\d]{14,17}[0-9a-zA-Z]";
		return id.matches(telRegex);
	}

	/**
	 * 密码控件
	 */
	static String FristPas = null;

	private void PassView(final int Type) {
		final PPassWord p = new PPassWord(BaseContext, screenWidth,
				Type == 0 ? getString(R.string.please_input_6_bit_reset_psd) : getString(R.string.please_input_again_6_bit_reset_psd));

		p.setOnPassWordListener(new OnPasswordInputFinish() {

			@Override
			public void inputFinish(String getStrPassword) {
				PromptManager.ShowCustomToast(BaseContext, getStrPassword);
				if (0 == Type) {
					FristPas = getStrPassword;
					p.dismiss();
					PassView(1);

					return;
				}
				if (1 == Type) {
					if (getStrPassword.equals(FristPas)) {
						p.dismiss();
						PromptManager.ShowCustomToast(BaseContext, getString(R.string.anthentication_finish));
						FristPas = null;
						return;
					} else {
						PromptManager.ShowCustomToast(BaseContext,
								getString(R.string.two_reset_psd_not_equals));
						p.dismiss();
						PassView(0);
						return;
					}
					// TODO确认完成 需要下一步操作

				}

				p.dismiss();
			}

			@Override
			public void LostPassWord() {
				PromptManager.ShowCustomToast(BaseContext, "忘记密码怎么办");
			}

			@Override
			public void Cancle() {
				p.dismiss();
			}
		});
		p.showAtLocation(bVivew, Gravity.CENTER, 0, 0);
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

}
