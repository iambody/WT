package io.vtown.WeiTangApp.ui.title.center.set;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.https.client.HttpsPost;
import io.vtown.WeiTangApp.comment.util.NetUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.pop.PPassWord;
import io.vtown.WeiTangApp.event.interf.OnPasswordInputFinish;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jpush.a.a.u;

import com.android.volley.Request.Method;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-22 下午9:20:00 重置交易密码步骤二页面
 */
public class AResetPswStep2 extends ATitleBase {

	/**
	 * 手机号输入框
	 */
	private TextView tv_enter_phone_number;

	/**
	 * 下一步按钮
	 */
	private Button btn_next_step2;

	/**
	 * 真实姓名输入框
	 */
	private TextView tv_reset_psw_real_name;
	/**
	 * 身份证号输入框
	 */
	private EditText et_enter_id_numb;

	/**
	 * 验证码
	 */
	private EditText safe_verification_code;
	/**
	 * 发送验证码按钮
	 */
	private TextView safe_verification_getcode_bt;

	/**
	 * 获取验证码
	 */
	private boolean sendedAuthCode = false; // 是否已经 获取验证了

	/**
	 * 记录下发送验证码的手机号
	 */
	private String MyCommintPhone;

	/**
	 * 输入姓名和身份证号
	 */
	private LinearLayout ll_enter_name_and_numb_id;

	/**
	 * 输入手机号和验证码
	 */
	private LinearLayout ll_enter_phone_and_code;

	/**
	 * 获取验证码时间 间隔
	 */
	private int times = 60;

	/**
	 * SP中保存的用户绑定的手机号
	 */
	private String phone;

	/**
	 * 按钮操作标志
	 */
	private boolean showButton = false;

	/**
	 * 密码控件
	 */
	static String FristPas = null;
	private View view;

	/**
	 * 用户信息
	 */
	private BUser user_Get;

	/**
	 * 身份证号
	 */
	private String id_numb;

	/**
	 * 是否点击了获取验证码
	 */
	private boolean isClickGetCode = false;

	@Override
	protected void InItBaseView() {
		SetTitleHttpDataLisenter(this);

		user_Get = Spuit.User_Get(getApplicationContext());
		phone = user_Get.getPhone();

		setContentView(R.layout.activity_center_set_personal_data_account_safe_resetpsw_step2);
		view = LayoutInflater
				.from(BaseContext)
				.inflate(
						R.layout.activity_center_set_personal_data_account_safe_resetpsw_step2,
						null);
		IView();

	}

	private void IView() {

		ll_enter_phone_and_code = (LinearLayout) findViewById(R.id.ll_enter_phone_and_code);
		tv_enter_phone_number = (TextView) findViewById(R.id.tv_enter_phone_number);
		safe_verification_code = (EditText) findViewById(R.id.safe_verification_code);
		safe_verification_getcode_bt = (TextView) findViewById(R.id.safe_verification_getcode_bt);
		btn_next_step2 = (Button) findViewById(R.id.btn_next_step2);
		if (!showButton) {
			btn_next_step2.setText("校验验证码");
		}
		btn_next_step2.setOnClickListener(this);

		safe_verification_getcode_bt.setOnClickListener(this);

		ll_enter_name_and_numb_id = (LinearLayout) findViewById(R.id.ll_enter_name_and_numb_id);
		tv_reset_psw_real_name = (TextView) findViewById(R.id.tv_reset_psw_real_name);
		et_enter_id_numb = (EditText) findViewById(R.id.et_enter_id_numb);

		StrUtils.SetColorsTxt(BaseContext, tv_enter_phone_number,
				R.color.app_gray, "您绑定的手机号：", phone.substring(0, 3) + "****"
						+ phone.substring(7));
		StrUtils.SetTxt(tv_reset_psw_real_name,user_Get.getName());
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getString(R.string.reset_pwd));
	}

	/**
	 * 修改交易密码请求
	 * 
	 * @param id_numb
	 * @param name
	 */
	private void ResetPsw(String psw1, String psw2, String name, String id_numb) {
		PromptManager.showLoading(BaseContext);
		HashMap<String, String> map = new HashMap<String, String>();

		map.put("member_id", user_Get.getMember_id());
		String rsa_psw1 = Constants.RSA(psw1, BaseContext);
		String identity_card_rsa = Constants.RSA(id_numb, BaseContext);
		map.put("password", rsa_psw1);
		map.put("password2", rsa_psw1);
		map.put("identity_card", identity_card_rsa);
		map.put("name", name);// URLEncoder.encode(name));
		FBGetHttpData(map, Constants.Reset_Psw, Method.PUT, 0, LOAD_INITIALIZE);
	}

	/**
	 * 校验验证码
	 */
	private void CheckCode(String phone, String code) {
		PromptManager.showLoading(BaseContext);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("phone", phone);
		map.put("code", code);
		FBGetHttpData(map, Constants.Check_Code, Method.POST, 1,
				LOAD_INITIALIZE);
	}

	/**
	 * 验证身份信息
	 */
	private void CheckUsr(String identity_card, String name) {
		SetTitleHttpDataLisenter(this);
		PromptManager.showLoading(BaseContext);
		HashMap<String, String> map = new HashMap<String, String>();

		map.put("member_id", user_Get.getMember_id());
		map.put("identity_card", identity_card);
		map.put("name", name);
		FBGetHttpData(map, Constants.Check_User, Method.GET, 2, LOAD_INITIALIZE);
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		switch (Data.getHttpResultTage()) {
		case 0:// 修改交易密码
			PromptManager.ShowCustomToast(BaseContext,
					getString(R.string.the_reset_psd_modify_finish));
			AResetPswStep2.this.finish();
			break;

		case 1:// 校验验证码
			ll_enter_phone_and_code.setVisibility(View.GONE);
			ll_enter_name_and_numb_id.setVisibility(View.VISIBLE);
			showButton = true;
			btn_next_step2.setText("下一步");
			break;

		default:
			break;
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
		case R.id.safe_verification_getcode_bt:
			if (CheckNet(BaseContext))
				return;
			safe_verification_code.requestFocus();
			isClickGetCode = true;
			if (!sendedAuthCode) {
				getAuthCode();
			} else {
				Animation shake = AnimationUtils.loadAnimation(BaseContext,
						R.anim.animlayout);
				safe_verification_getcode_bt.startAnimation(shake);
			}
			break;

		case R.id.btn_next_step2:
			if (CheckNet(BaseContext))
				return;
			if (showButton) {

				id_numb = et_enter_id_numb.getText().toString().trim();
				if (!StrUtils.checkIdNo(BaseContext, id_numb)) {
					return;
				}
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(
						safe_verification_code.getWindowToken(), 0);
				if (!id_numb.equals(user_Get.getIdentity_card())) {
					PromptManager.ShowMyToast(BaseContext, "您输入的身份号不正确");
				} else {
					PassView(0, user_Get.getName(), id_numb);
				}

			} else {
				if (!CheCheckCode(2))
					return;

				String code = safe_verification_code.getText().toString()
						.trim();
				if (!StrUtils.isEmpty(code) && !isClickGetCode) {
					PromptManager.ShowMyToast(BaseContext, "请先获取验证码");
					return;
				}
				CheckCode(phone, code);
			}

			break;

		}
	}

	private void getAuthCode() {

		if (NetUtil.isConnected(BaseContext)) {
			try {
				new CodeTask(phone, 1).execute();// 1注册
													// 2
			} catch (Exception e) {
			}

		} else
			PromptManager.ShowCustomToast(BaseContext, getResources()
					.getString(R.string.net_error_tip));

	}

	// 获取验证码
	class CodeTask extends AsyncTask<String, Integer, String> {

		String phone;
		int Type;// 1注册，2找回密码，3其它

		public CodeTask(String phone, int MyType) {
			PromptManager.showLoading(BaseContext);
			this.phone = phone;
			this.Type = MyType;
			MyCommintPhone = phone;
		}

		@Override
		protected void onPostExecute(String retSrc) {
			PromptManager.closeLoading();
			/**
			 * 获取到返回的成功数据
			 */
			int Status = 0;
			int Code = 0;
			String Msg = null;
			try {
				JSONObject obj = new JSONObject(retSrc);
				Code = obj.getInt("code");
				Msg = obj.getString("msg");
			} catch (Exception e) {
			}
			if (200 == Code) {// 提交成功
				sendedAuthCode = true;
				safe_verification_getcode_bt.post(authTimeRunnable);
				PromptManager.ShowCustomToast(BaseContext, "发送成功");
			} else {// 提交失败
				PromptManager.ShowCustomToast(BaseContext, "发送失败");
			}
		}

		Runnable authTimeRunnable = new Runnable() {
			@Override
			public void run() {
				safe_verification_getcode_bt
						.setBackgroundResource(R.drawable.regist_code_shape_pre);
				safe_verification_getcode_bt
						.setText(String.format(
								"  %d%s  ",
								times,
								getResources().getString(
										R.string.txtresuorce_seconde)));
				times--;
				if (times <= 0) {
					safe_verification_getcode_bt
							.setBackgroundResource(R.drawable.select_fen_to_gray);
					safe_verification_getcode_bt.setText(getResources()
							.getString(R.string.txtresuorce_getcode));
					times = 60;
					sendedAuthCode = false;
					return;
				}
				safe_verification_getcode_bt.postDelayed(this, 1000);
			}
		};

		@Override
		protected String doInBackground(String... params) {
			// String retSrc = null;
			// HttpPost request = new HttpPost(Constants.SMS);
			// List<NameValuePair> nameValuePairs = new
			// ArrayList<NameValuePair>();
			// nameValuePairs.add(new BasicNameValuePair("phone", phone));
			// // nameValuePairs.add(new BasicNameValuePair("type_id", Type +
			// ""));
			// // 发送请求
			// HttpResponse httpResponse = null;
			// try {
			// request.setEntity(new UrlEncodedFormEntity(nameValuePairs,
			// HTTP.UTF_8));
			// httpResponse = new DefaultHttpClient().execute(request);
			// // retSrc = EntityUtils.toString(httpResponse.getEntity());
			// retSrc = Change(httpResponse);
			// if (httpResponse.getStatusLine().getStatusCode() == 200) {
			// return retSrc;
			// // retSrc=EntityUtils.toString(httpResponse.getEntity());
			// } else {
			// return null;
			// }
			// } catch (ClientProtocolException e) {
			// PromptManager.closeLoading();
			// e.printStackTrace();
			// } catch (IOException e) {
			// PromptManager.closeLoading();
			// e.printStackTrace();
			// }
			// return null;
			// https*********************************************************************************
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("phone", phone));
			nameValuePairs.add(new BasicNameValuePair("type_id", Type + ""));
			nameValuePairs.add(new BasicNameValuePair("UUID", Constants
					.GetPhoneId(BaseContext)));
			String Timsp = Constants.TimeStamp();
			nameValuePairs.add(new BasicNameValuePair("timestamp", Timsp));
			nameValuePairs.add(new BasicNameValuePair("source", "20"));
			// 加言****************************************************
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("phone", phone);
			hashMap.put("type_id", Type + "");
			hashMap.put("UUID", Constants.GetPhoneId(BaseContext));
			hashMap.put("timestamp", Timsp);
			hashMap.put("source", "20");

			JSONObject obj = new JSONObject();
			try {
				obj.put("phone", phone);
				obj.put("type_id", Type + "");
				obj.put("UUID", Constants.GetPhoneId(BaseContext));
				obj.put("timestamp", Timsp);
				obj.put("source", "20");
				obj.put("sign", Sign(hashMap));
			} catch (Exception e) {

			}

			nameValuePairs.add(new BasicNameValuePair("sign", Sign(hashMap)));

			// 参数
			HttpParams httpParameters = new BasicHttpParams();
			// 设置连接超时
			HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
			// 设置socket超时
			HttpConnectionParams.setSoTimeout(httpParameters, 3000);
			// 获取HttpClient对象 （认证）
			HttpClient hc = HttpsPost.initHttpClient(httpParameters);
			HttpPost post = new HttpPost(Constants.SMS);
			// 发送数据类型
			// post.addHeader("Content-Type", "application/json;charset=utf-8");
			// // 接受数据类型
			// post.addHeader("Accept", "application/json");
			// 请求报文
			StringEntity entity = null;

			// try {
			// entity = new StringEntity(obj.toString(), "UTF-8");
			// } catch (UnsupportedEncodingException e1) {
			// e1.printStackTrace();
			// }

			// post.setEntity(entity);

			try {
				post.setEntity(new UrlEncodedFormEntity(nameValuePairs,
						HTTP.UTF_8));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			post.setParams(httpParameters);
			HttpResponse response = null;

			try {
				response = hc.execute(post);

				int sCode = response.getStatusLine().getStatusCode();
				if (sCode == HttpStatus.SC_OK) {
					return EntityUtils.toString(response.getEntity());
				} else {
					return "";
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return "";
			} catch (IOException e) {
				e.printStackTrace();
				return "";
			}
			// https*********************************************************************************
		}

	}

	private String Change(HttpResponse response) {
		StringBuffer sb = new StringBuffer();
		HttpEntity entity = response.getEntity();
		InputStream is;
		BufferedReader br = null;
		try {
			is = entity.getContent();
			br = new BufferedReader(new InputStreamReader(is, HTTP.UTF_8));
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String data = "";
		try {
			while ((data = br.readLine()) != null) {
				sb.append(data);
			}
		} catch (Exception e) {
		}
		return sb.toString();
	}

	/**
	 * 1标识只检验手机号是不是为空 2标识检验手机号和验证码是不是为空
	 * 
	 * @param type
	 * @return
	 */
	private boolean CheCheckCode(int type) {
		if (1 == type)
			return true;

		if (StrUtils
				.isEmpty(safe_verification_code.getText().toString().trim())) {
			PromptManager.ShowCustomToast(BaseContext, getResources()
					.getString(R.string.code_is_null));
			return false;
		}
		return true;
	}

	private void PassView(final int Type, final String name,
			final String id_numb) {
		final PPassWord p = new PPassWord(
				BaseContext,
				screenWidth,
				Type == 0 ? getString(R.string.please_input_6_bit_reset_psd)
						: getString(R.string.please_input_again_6_bit_reset_psd));

		p.setOnPassWordListener(new OnPasswordInputFinish() {

			@Override
			public void inputFinish(String getStrPassword) {
				if (0 == Type) {
					FristPas = getStrPassword;
					p.dismiss();
					PassView(1, name, id_numb);

					return;
				}
				if (1 == Type) {
					if (getStrPassword.equals(FristPas)) {
						p.dismiss();

						FristPas = null;

						ResetPsw(getStrPassword, getStrPassword, name, id_numb);
						return;
					} else {
						PromptManager.ShowCustomToast(BaseContext,
								getString(R.string.two_reset_psd_not_equals));
						p.dismiss();
						PassView(0, name, id_numb);
						return;
					}
					// TODO 确认完成 需要下一步操作

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
		p.showAtLocation(view, Gravity.CENTER, 0, 0);
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

}
