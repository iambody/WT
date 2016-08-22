package io.vtown.WeiTangApp.ui.title.shop.channel;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.util.HashMap;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request.Method;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-16 上午11:22:50 邀请上级页面
 */
public class AInviteSuperior extends ATitleBase {

	/**
	 * 上级姓名输入框
	 */
	private EditText et_superior_name;
	/**
	 * 上级手机号输入框
	 */
	private EditText et_superior_phone;
	/**
	 * 上级代理品牌输入框
	 */
	private EditText et_superior_agent_brand;
	/**
	 * 团队人数
	 */
	private EditText et_superior_agent_teamnumber;
	/**
	 * 团队描述
	 */
	private EditText et_superior_agent_team_inf;
	/**
	 * 三个输入框后面的三个星星
	 */
	private TextView tv_star1, tv_star2, tv_star3, tv_star4;

	/**
	 * 邀请按钮
	 */
	private Button btn_invite;
	/**
	 * 用户信息
	 */
	private BUser user_Get;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_invite_superior);
		user_Get = Spuit.User_Get(BaseContext);
		IView();
	}

	private void IView() {
		et_superior_name = (EditText) findViewById(R.id.et_superior_name);
		et_superior_phone = (EditText) findViewById(R.id.et_superior_phone);
		et_superior_agent_brand = (EditText) findViewById(R.id.et_superior_agent_brand);
		et_superior_agent_teamnumber = (EditText) findViewById(R.id.et_superior_agent_teamnumber);
		et_superior_agent_team_inf = (EditText) findViewById(R.id.et_superior_agent_team_inf);
		tv_star1 = (TextView) findViewById(R.id.tv_star1);
		tv_star2 = (TextView) findViewById(R.id.tv_star2);
		tv_star3 = (TextView) findViewById(R.id.tv_star3);
		btn_invite = (Button) findViewById(R.id.btn_invite);
		btn_invite.setOnClickListener(this);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.invite_superior));
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {

		PromptManager.showtextLoading(BaseContext,
				getResources().getString(R.string.tijiaochengg));
		BaseActivity.finish();
	}

	@Override
	protected void DataError(String error, int LoadTyp) {
		PromptManager.ShowCustomToast(BaseContext, error);
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
		case R.id.btn_invite:
			if(CheckNet(BaseContext))return;
			if (IsReday())
				Commint();
			break;

		}
	}

	/**
	 * 是否满足必填条件
	 * 
	 * @return
	 */
	private boolean IsReday() {
		if (StrUtils.isEmpty(GetEdStr(et_superior_name))) {
			PromptManager.ShowCustomToast(BaseContext, getResources()
					.getString(R.string.chanle_t_name));
			return false;
		}
		if (StrUtils.isEmpty(GetEdStr(et_superior_phone))) {
			PromptManager.ShowCustomToast(BaseContext, getResources()
					.getString(R.string.chanle_t_phone));
			return false;
		}
		if (!StrUtils.isMobileNO(GetEdStr(et_superior_phone))) {
			PromptManager.ShowCustomToast(BaseContext, getResources()
					.getString(R.string.phone_right));
			return false;
		}

		if (StrUtils.isEmpty(GetEdStr(et_superior_agent_brand))) {
			PromptManager.ShowCustomToast(BaseContext, getResources()
					.getString(R.string.chanle_t_brand));
			return false;
		}
		if (StrUtils.isEmpty(GetEdStr(et_superior_agent_teamnumber))) {
			PromptManager.ShowCustomToast(BaseContext, getResources()
					.getString(R.string.chanle_t_teamnumber));
			return false;
		}
		if (StrUtils.isEmpty(GetEdStr(et_superior_agent_team_inf))) {
			PromptManager.ShowCustomToast(BaseContext, getResources()
					.getString(R.string.chanle_t_teamnuminf));
			return false;
		}

		return true;
	}

	private String GetEdStr(EditText editText) {
		return editText.getText().toString().trim();
	}

	private void Commint() {

		SetTitleHttpDataLisenter(this);
		PromptManager.showLoading(BaseContext);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", user_Get.getSeller_id());
		map.put("name", GetEdStr(et_superior_name));
		map.put("phone", GetEdStr(et_superior_phone));
		map.put("brand", GetEdStr(et_superior_agent_brand));
		map.put("team", GetEdStr(et_superior_agent_teamnumber));
		map.put("intro", GetEdStr(et_superior_agent_team_inf));
		FBGetHttpData(map, Constants.SHOP_CHANNEL_yaoqing_shangjiSup,
				Method.POST, 0, LOAD_INITIALIZE);

	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复
			if (!StrUtils.EditTextIsEmPty(et_superior_name)
					|| !StrUtils.EditTextIsEmPty(et_superior_phone)
					|| !StrUtils.EditTextIsEmPty(et_superior_agent_brand)
					|| !StrUtils.EditTextIsEmPty(et_superior_agent_teamnumber)
					|| !StrUtils.EditTextIsEmPty(et_superior_agent_team_inf)) {

				ShowCustomDialog("是否退出?",
						getResources().getString(R.string.cancle), "退出",
						new IDialogResult() {

							@Override
							public void RightResult() {
								onBackPressed();
							}

							@Override
							public void LeftResult() {
							}
						});
			} else {
				onBackPressed();
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}
}
