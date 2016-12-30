package io.vtown.WeiTangApp.ui.title.center.wallet;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BLBankCardAndAlipayList;
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

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-6-3 下午1:08:35
 *  修改支付宝页面
 */
public class AModifyAlipay extends ATitleBase {

	/**
	 * 修改之前的支付宝用户名称
	 */
	private TextView tv_modify_real_alipay_name_before;
	/**
	 * 修改之前的支付宝账号
	 */
	private TextView tv_modify_alipay_id_before;
	/**
	 * 真实姓名
	 */
	private TextView tv_modify_alipay_real_name;
	/**
	 * 输入的支付宝账号
	 */
	private EditText et_modify_alipay_id;
	/**
	 * 保存修改按钮
	 */
	private TextView tv_modify_alipay_save;
	/**
	 * 用户信息
	 */
	private BUser user_Get;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_center_wallet_alipay_manage_modify);
		user_Get = Spuit.User_Get(getApplicationContext());
		IView();
		IData();
	}

	

	/**
	 * 初始化控件
	 */
	private void IView() {
		tv_modify_real_alipay_name_before = (TextView) findViewById(R.id.tv_modify_real_alipay_name_before);
		tv_modify_alipay_id_before = (TextView) findViewById(R.id.tv_modify_alipay_id_before);
		tv_modify_alipay_real_name = (TextView) findViewById(R.id.tv_modify_alipay_real_name);
		et_modify_alipay_id = (EditText) findViewById(R.id.et_modify_alipay_id);
		tv_modify_alipay_save = (TextView) findViewById(R.id.tv_modify_alipay_save);
		tv_modify_alipay_save.setOnClickListener(this);
		
	}
	
	/**
	 * 数据初始化
	 */
	private void IData() {
		Intent intent = getIntent();
		BLBankCardAndAlipayList alipay_info = (BLBankCardAndAlipayList) intent.getSerializableExtra("alipay_info");
		StrUtils.SetTxt(tv_modify_real_alipay_name_before, alipay_info.getName());
		StrUtils.SetTxt(tv_modify_alipay_id_before, alipay_info.getAlipay());
		StrUtils.SetTxt(tv_modify_alipay_real_name, alipay_info.getName());
	}
	

	/**
	 * 
	 * 修改支付宝账号网络请求
	 * @param memberId
	 * @param alipay_id
	 */
	private void ModifyAlipay(String memberId, String alipay_id) {
		
		SetTitleHttpDataLisenter(this);
		PromptManager.showLoading(BaseContext);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("member_id", memberId);
		map.put("alipay", alipay_id);
		FBGetHttpData(map, Constants.Modify_Alipay, Method.GET, 0, LOAD_INITIALIZE);
	}


	@Override
	protected void InitTile() {
		SetTitleTxt("修改支付宝");
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		//tv_modify_alipay_save.setEnabled(true);
		if(200 == Code){

			PromptManager.ShowMyToast(BaseContext, "支付宝修改成功");
			Intent intent = new Intent(BaseContext,AAlipayManager.class);
			intent.putExtra("isFinish", true);
			startActivity(intent);
			
			finish();
		}else{
			PromptManager.ShowMyToast(BaseContext, "支付宝修改失败");

		}
	}

	@Override
	protected void DataError(String error, int LoadType) {
		PromptManager.ShowMyToast(BaseContext, error);
		//tv_modify_alipay_save.setEnabled(true);
	}

	@Override
	protected void NetConnect() {
		NetError.setVisibility(View.GONE);
		//tv_modify_alipay_save.setEnabled(true);
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
		case R.id.tv_modify_alipay_save:
			//tv_modify_alipay_save.setEnabled(false);
			getAlipayInfo();
			break;

		default:
			break;
		}
	}

	/**
	 * 获取支付宝信息
	 */
	private void getAlipayInfo() {
		String alipay_id = et_modify_alipay_id.getText().toString().trim();
		if(!StrUtils.CheckAlipay(BaseContext,alipay_id)){
			//tv_modify_alipay_save.setEnabled(true);
			return;
		}
		if(CheckNet(BaseContext))return;
		ModifyAlipay(user_Get.getId(),alipay_id);
	}





	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

}
