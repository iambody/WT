package io.vtown.WeiTangApp.ui.title.center.myinvitecode;

import java.util.HashMap;

import com.android.volley.Request.Method;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-8-9 下午6:21:12
 * 
 */
public class ABindCode extends ATitleBase {

	private TextView bindcode_txt;
	private TextView bindcode_submint_bt;
	private String MyBindCode;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_bind_code);
		try {
			MyBindCode = getIntent().getStringExtra("code");
		} catch (Exception e) {
			BaseActivity.finish();
		}
		bindcode_txt = (TextView) findViewById(R.id.bindcode_txt);
		bindcode_submint_bt = (TextView) findViewById(R.id.bindcode_submint_bt);
		StrUtils.SetTxt(bindcode_txt, MyBindCode);
		bindcode_submint_bt.setOnClickListener(this);
	}

	@Override
	protected void InitTile() {
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		Spuit.InvitationCode_Set(BaseActivity, true);
		PromptManager.ShowCustomToast(BaseContext, "绑定成功");
		BaseActivity.finish();
	}

	@Override
	protected void DataError(String error, int LoadType) {
		PromptManager.ShowCustomToast(BaseContext, error);
		BaseActivity.finish();
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
		case R.id.bindcode_submint_bt:
			BindCode(MyBindCode);
			break;

		default:
			break;
		}
	}

	private void BindCode(String invite_code) {
		SetTitleHttpDataLisenter(this);
		PromptManager.showtextLoading(BaseContext,
				getResources().getString(R.string.loading));
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("member_id", Spuit.User_Get(BaseActivity).getMember_id());
		map.put("invite_code", invite_code);
		FBGetHttpData(map, Constants.Login_Bind_Invite_Code, Method.POST, 0,
				LOAD_INITIALIZE);
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

}
