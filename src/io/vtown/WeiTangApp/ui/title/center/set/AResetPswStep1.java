package io.vtown.WeiTangApp.ui.title.center.set;

import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.http.util.EncodingUtils;

import com.android.volley.Request.Method;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-22 下午8:36:19
 *  重置交易密码步骤一页面
 *  
 */
public class AResetPswStep1 extends ATitleBase {

	/**
	 * 真实姓名输入框
	 */
	private EditText et_enter_name;
	/**
	 * 身份证号输入框
	 */
	private EditText et_enter_id_numb;
	/**
	 * 下一步按钮
	 */
	private Button btn_next_step;

	@Override
	protected void InItBaseView() {
		
		setContentView(R.layout.activity_center_set_personal_data_account_safe_resetpsw_step1);
		IView();
		
	}
	
	/**
	 * 验证身份信息
	 */
	private void CheckUsr(String identity_card,String name){
		SetTitleHttpDataLisenter(this);
		PromptManager.showLoading(BaseContext);
		HashMap<String, String> map = new HashMap<String, String>();
		BUser user_Get = Spuit.User_Get(BaseContext);
		map.put("member_id", user_Get.getMember_id());
		map.put("identity_card", identity_card);
		map.put("name",URLEncoder.encode(name));
		FBGetHttpData(map, Constants.Check_User, Method.GET, 0, LOAD_INITIALIZE);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		
		super.onNewIntent(intent);
		setIntent(intent);
		isFinish();
	}
	
	private void isFinish(){
		boolean isFinish = getIntent().getBooleanExtra("isFinish", false);
		if(isFinish){
			this.finish();
		}
	}

	private void IView() {
		et_enter_name = (EditText) findViewById(R.id.et_enter_name);
		et_enter_id_numb = (EditText) findViewById(R.id.et_enter_id_numb);
		btn_next_step = (Button) findViewById(R.id.btn_next_step);
		btn_next_step.setOnClickListener(this);
	}

	@Override
	protected void InitTile() {
		
		SetTitleTxt(getString(R.string.reset_pwd));
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		
		PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,AResetPswStep2.class));
	}

	@Override
	protected void DataError(String error,int LoadTyp) {
		PromptManager.ShowMyToast(BaseContext, error);
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
		case R.id.btn_next_step:
			
			String name = et_enter_name.getText().toString().trim();
			if(StrUtils.isEmpty(name)){
				PromptManager.ShowMyToast(BaseContext, "请输入姓名");
				return ;
			}
			String id_numb = et_enter_id_numb.getText().toString().trim();
			if(!StrUtils.checkIdNo(BaseContext,id_numb)){
				return;
			}
			CheckUsr(id_numb, name);
			break;
		}
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

}
