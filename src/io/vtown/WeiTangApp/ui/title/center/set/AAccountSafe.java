package io.vtown.WeiTangApp.ui.title.center.set;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.onConfirmClick;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.oncancelClick;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.loginregist.ARealIdauth;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-22 下午5:55:50 账户安全页面
 */
public class AAccountSafe extends ATitleBase {

	/**
	 * 是否已认证
	 */
	private boolean isLogin_RenZheng_Set;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_center_set_personal_data_account_safe);
		isLogin_RenZheng_Set = Spuit.IsLogin_RenZheng_Set(BaseContext);
		IView();
	}

	@Override
	protected void InitTile() {

		SetTitleTxt(getString(R.string.account_safe));

	}

	private void IView() {
		String content = "";
		if (isLogin_RenZheng_Set) {
			content = getResources().getString(R.string.reset_pwd);
		} else {
			content = getResources().getString(R.string.set_pwd);
		}
		View reset_pwd = findViewById(R.id.reset_pwd);
		((TextView) reset_pwd.findViewById(R.id.comment_txtarrow_title))
				.setText(content);
		reset_pwd.setOnClickListener(this);
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
		switch (V.getId()) {
		case R.id.reset_pwd:
			boolean isLogin_RenZheng_Set = Spuit
					.IsLogin_RenZheng_Set(BaseContext);
			if (isLogin_RenZheng_Set) {
				PromptManager.SkipActivity(BaseActivity, new Intent(
						BaseActivity, AResetPswStep2.class));
			} else {
				ShowRealAuthDialog();
			}

			break;
		}
	}

	/**
	 * 实名认证对话框
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
						PromptManager.SkipActivity(BaseActivity, new Intent(
								BaseContext, ARealIdauth.class).putExtra(
								ARealIdauth.FROM_WHERE_KEY, 3));
					}

					@Override
					public void LeftResult() {
					}
				});

		// final CustomDialog dialog = new CustomDialog(BaseContext,
		// R.style.mystyle, R.layout.customdialog, 1, "取消", "去认证");
		// dialog.show();
		// dialog.setTitleText("您还没有进行实名认证");
		// dialog.setConfirmListener(new onConfirmClick() {
		// @Override
		// public void onConfirmCLick(View v) {
		//
		// PromptManager.SkipActivity(BaseActivity, new Intent(
		// BaseContext, ARealIdauth.class).putExtra(
		// ARealIdauth.FROM_WHERE_KEY, 3));
		// dialog.dismiss();
		// }
		// });
		// dialog.setcancelListener(new oncancelClick() {
		// @Override
		// public void oncancelClick(View v) {
		//
		// dialog.dismiss();
		// }
		// });
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

}
