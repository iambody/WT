package io.vtown.WeiTangApp.ui.title.center.wallet;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.event.receiver.NewMessageBroadcastReceiver;
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
 * @version 创建时间：2016-5-20 下午12:58:31 添加支付宝页面
 */
public class AAddAliPay extends ATitleBase {

	/**
	 * 支付宝账号输入框
	 */
	private EditText et_alipay_numb;
	/**
	 * 支付宝真实姓名输入框
	 */
	private TextView tv_add_alipay_real_name;
	/**
	 * 银行授权协议
	 */
	private TextView tv_alipay_transfer_agreement;
	/**
	 * 提交按钮
	 */
	private TextView tv_btn_submit_alipay;
	/**
	 * 用户信息
	 */
	private BUser user_Get;
	/**
	 * 操作完成之后要跳转到哪里
	 */
	private int togo;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_center_wallet_bankcard_manager_add_alipay);
		user_Get = Spuit.User_Get(BaseContext);
		togo = getIntent().getIntExtra("togo", 0);
		IView();
		IData();
	}

	

	/**
	 * 控件初始化
	 */
	private void IView() {
		et_alipay_numb = (EditText) findViewById(R.id.et_alipay_numb);
		tv_add_alipay_real_name = (TextView) findViewById(R.id.tv_add_alipay_real_name);
		
		tv_alipay_transfer_agreement = (TextView) findViewById(R.id.tv_alipay_transfer_agreement);
		tv_btn_submit_alipay = (TextView) findViewById(R.id.tv_btn_submit_alipay);
		tv_alipay_transfer_agreement.setOnClickListener(this);
		tv_btn_submit_alipay.setOnClickListener(this);
	}
	
	/**
	 * 数据初始化
	 */
	private void IData() {
		BUser user_Get = Spuit.User_Get(BaseContext);
		String name = user_Get.getName();
		StrUtils.SetTxt(tv_add_alipay_real_name, name);
	}
	
	/**
	 * 
	 * 添加支付宝账号网络请求
	 * @param memberId
	 * @param alipay_id
	 */
	private void AddAlipay(String memberId, String alipay_id) {
		
		SetTitleHttpDataLisenter(this);
		PromptManager.showLoading(BaseContext);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("member_id", memberId);
		map.put("alipay", alipay_id);
		FBGetHttpData(map, Constants.Modify_Alipay, Method.GET, 0, LOAD_INITIALIZE);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getString(R.string.lable_add_alipay));
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		if(200 == Code){
			PromptManager.ShowMyToast(BaseContext, "支付宝添加成功");
			Intent intent = null;
			if(1 == togo){
				intent = new Intent(BaseContext, ATiXian.class);
				
			}else{
				intent = new Intent(BaseContext,AAlipayManager.class);
				intent.putExtra("isFinish", true);
			}
			EventBus.getDefault().post(new BMessage(BMessage.Tage_Updata_Tixian_Message));
			startActivity(intent);
			this.finish();
		}else{
			PromptManager.ShowMyToast(BaseContext, "支付宝添加失败");
		}
	}

	@Override
	protected void DataError(String error,int LoadTyp) {
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
		case R.id.tv_alipay_transfer_agreement:

			break;
		case R.id.tv_btn_submit_alipay:
			getAlipayInfo();
			break;

		}
	}

	/**
	 * 获取支付宝信息
	 */
	private void getAlipayInfo() {
		String alipay = et_alipay_numb.getText().toString().trim();
		
		if(!StrUtils.CheckAlipay(BaseContext,alipay)){
			return;
		}
		
		if(CheckNet(BaseContext))return;
		AddAlipay(user_Get.getId(),alipay);
		
	}



	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}
	
	

}
