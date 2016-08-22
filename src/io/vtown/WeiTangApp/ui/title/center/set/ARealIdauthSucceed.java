package io.vtown.WeiTangApp.ui.title.center.set;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-6-12 下午9:20:21
 *  实名认证成功页面
 */
public class ARealIdauthSucceed extends ATitleBase {

	/**
	 * 姓名
	 */
	private TextView tv_real_auth_name;
	/**
	 * 身份证号
	 */
	private TextView tv_real_auth_id;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_center_set_real_auth_succeed);
		IView();
		IData();
	}

	

	private void IView() {
		tv_real_auth_name = (TextView) findViewById(R.id.tv_real_auth_name);
		tv_real_auth_id = (TextView) findViewById(R.id.tv_real_auth_id);
	}
	
	private void IData() {
		BUser user_Get = Spuit.User_Get(BaseContext);
		String name = user_Get.getName();
		String identity_card = user_Get.getIdentity_card();
		StrUtils.SetColorsTxt(BaseContext, tv_real_auth_name, R.color.app_gray, "姓名：", name);
		StrUtils.SetColorsTxt(BaseContext, tv_real_auth_id, R.color.app_gray, "身份证号：", StrUtils.getIdNoFormatForUser(identity_card));
	}

	@Override
	protected void InitTile() {
		SetTitleTxt("实名认证");
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
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
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

}
