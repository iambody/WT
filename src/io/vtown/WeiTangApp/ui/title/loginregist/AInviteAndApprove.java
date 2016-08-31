package io.vtown.WeiTangApp.ui.title.loginregist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.UpComparator;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.https.client.HttpsPost;
import io.vtown.WeiTangApp.comment.util.NetUtil;
import io.vtown.WeiTangApp.comment.util.SdCardUtils;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.event.interf.HttpsPostResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.AWeb;
import io.vtown.WeiTangApp.ui.ui.AMain;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-27 上午10:12:43 填写邀请码 和实名认证公用界面
 */
public class AInviteAndApprove extends ATitleBase {

    /**
     * 电话号码输入框
     */
    private EditText verification_phone;
    /**
     * 验证码获取按钮
     */
    private EditText verification_code;
    /**
     * 验证码输入框框
     */
    private TextView verification_submint_bt;
    /**
     * 提交按钮
     */
    private TextView verification_getcode_bt;

    /**
     * 用户协议
     */
    private TextView verification_deal_txt;
    /**
     * 如果是微信绑定登录的需要进行隐藏的下边的view
     */
    // private LinearLayout verification_code_down_lay;

    /**
     * 其他登录方式 微信登陆
     */
    private TextView verification_wx_login;
    /**
     * 记录下发送验证码的手机号
     */
    private String MyCommintPhone;
    private ImageView login_back_iv;
    /**
     * 获取验证码
     */
    private boolean sendedAuthCode = false; // 是否已经 获取验证了
    /**
     * 获取验证码时间 间隔
     */
    private int times = 30;

    private boolean WxIsBind = false;
    private String WXId = "";

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_verification_code);
        ImyBund();
        IBase();

    }

    private void ImyBund() {
        if (null == getIntent().getExtras()
                || !getIntent().getExtras().containsKey("iswx"))
            BaseActivity.finish();
        WxIsBind = getIntent().getBooleanExtra("iswx", false);
        WXId = getIntent().getStringExtra("wxid");
    }



    private void IBase() {
        login_back_iv= (ImageView) findViewById(R.id.login_back_iv);
        login_back_iv.setOnClickListener(this);
        findViewById(R.id.login_title_lay).setVisibility(View.GONE);

        verification_deal_txt = (TextView) findViewById(R.id.verification_deal_txt);
        verification_deal_txt.setOnClickListener(this);

        verification_phone = (EditText) findViewById(R.id.verification_phone);
        verification_code = (EditText) findViewById(R.id.verification_code);
        verification_getcode_bt = (TextView) findViewById(R.id.verification_getcode_bt);
        verification_submint_bt = (TextView) findViewById(R.id.verification_submint_bt);
        verification_wx_login = (TextView) findViewById(R.id.verification_wx_login);
        verification_getcode_bt.setOnClickListener(this);
        verification_submint_bt.setOnClickListener(this);
        verification_wx_login.setOnClickListener(this);
        verification_code.setHint("请输入验证码");
        // select_fen_to_gray.setb
        verification_submint_bt.setBackground(getResources().getDrawable(   R.drawable.shap_txtbt_pre));
        verification_submint_bt.setClickable(false);

        // verification_code_down_lay = (LinearLayout)
        // findViewById(R.id.verification_code_down_lay);
        // verification_code_down_lay.setVisibility(WxIsBind ? View.GONE
        // : View.VISIBLE);
        // verification_phone.setText(WxIsBind ? "" : "18310998310");
        // StrUtils.SetTxt(verification_submint_bt, WxIsBind?"登录":"");
        // if(WxIsBind){//如果是微信登录需要进行view的隐藏 （非绑定则为全部显示）
        // verification_code_down_lay.setVisibility(View.GONE);
        // }

    }

    @Override
    protected void InitTile() {
        SetTitleTxt(getString(R.string.login_title));
        HindBackIv();

    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

        switch (Data.getHttpResultTage()) {
            case 0:// 校验过验证码的

                if (WxIsBind) {
                    BindWxPhone(WXId, MyCommintPhone);
                } else {
                    PhoneLogin(MyCommintPhone, verification_code.getText()
                            .toString().trim());
                }

                break;
            case 1:// 校验完毕验证码发送 手机号绑定接口的接口返回的数据 ==》需要跳转到添加邀请码界面
            case 2:// 手机号登录成功==》需要跳转到添加邀请码界面
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    PromptManager.ShowCustomToast(BaseContext, getResources()
                            .getString(R.string.dataerror));
                    return;
                }

                BUser mBUser = JSON.parseObject(Data.getHttpResultStr(),
                        BUser.class);
                Spuit.User_Save(BaseContext, mBUser);
                Spuit.IsLogin_Set(BaseContext, true);
                //开始注册极光推送
                InitJPush();

                SdCardUtils.delFile(SdCardUtils.CodePath(BaseContext) + "mycode.jpg");
                SdCardUtils.delFile(SdCardUtils.CodePath(BaseContext) + "shopcode.jpg");
                // if(mBUser.get){}
                // 如果是第一次注册就进行下边逻辑
                if (!StrUtils.isEmpty(mBUser.getIs_new())
                        && mBUser.getIs_new().equals("0")) {// 注册进行第一次注册 需要进行流程跳转
                    if (StrUtils.isEmpty(mBUser.getParent_id())
                            || mBUser.getParent_id().equals("0")) {// 暂未进行绑定邀请码
                        PromptManager.SkipActivity(BaseActivity, new Intent(
                                BaseActivity, AInviteCode.class));
                    } else {// 已经绑定了邀请码
                        if (!Spuit.IsLogin_RenZheng_Set(BaseActivity)) {// 绑定邀请码但是未进行认证=》需要跳转到认证界面
                            PromptManager.SkipActivity(BaseActivity, new Intent(
                                    BaseActivity, ARealIdauth.class));
                        } else {// 绑定过邀请码 也认证过了===》跳转到主页面
                            PromptManager.SkipActivity(BaseActivity, new Intent(
                                    BaseActivity, AMain.class));
                        }
                    }

                } else {// 登录直接跳转
                    PromptManager.SkipActivity(BaseActivity, new Intent(
                            BaseActivity, AMain.class));


                }

                BaseActivity.finish();
                EventBus.getDefault().post(
                        new BMessage(BMessage.Tage_Login_Kill_Other));

                break;
            default:
                break;
        }

    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.ShowCustomToast(BaseContext, error);
    }

    private void getAuthCode() {
        String phoneNum = verification_phone.getText().toString();
        if (StrUtils.isMobileNO(phoneNum.trim())) {
            if (NetUtil.isConnected(BaseContext)) {
                try {
                    new CodeTask(phoneNum, 1).execute();// 1注册
                    // 2
                } catch (Exception e) {
                }

            } else
                PromptManager.ShowCustomToast(BaseContext, getResources()
                        .getString(R.string.net_error_tip));
        } else {
            PromptManager.ShowCustomToast(BaseContext, getResources()
                    .getString(R.string.txtresuorce_phoneerror));
            Animation shake = AnimationUtils.loadAnimation(this,
                    R.anim.animlayout);
            verification_getcode_bt.startAnimation(shake);

        }
    }

    Runnable authTimeRunnable = new Runnable() {
        @Override
        public void run() {
            verification_getcode_bt
                    .setBackgroundResource(R.drawable.regist_code_shape_pre);
            verification_getcode_bt.setText(String.format("    %d%s    ", times,
                    getResources().getString(R.string.txtresuorce_seconde)));
            times--;
            if (times <= 0) {
                verification_getcode_bt
                        .setBackgroundResource(R.drawable.select_fen_to_gray);
                verification_getcode_bt.setText(getResources().getString(
                        R.string.txtresuorce_getcode));
                times = 60;
                sendedAuthCode = false;
                return;
            }
            verification_getcode_bt.postDelayed(this, 1000);
        }
    };

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
                verification_getcode_bt.post(authTimeRunnable);
                PromptManager.ShowCustomToast(BaseContext, "发送成功");
                verification_submint_bt.setBackground(getResources()
                        .getDrawable(R.drawable.select_fen_to_gray));

                verification_submint_bt.setClickable(true);
            } else {// 提交失败
                PromptManager.ShowCustomToast(BaseContext,
                        StrUtils.isEmpty(Msg) ? "发送失败" : Msg);
            }
        }

        @Override
        protected String doInBackground(String... params) {
            // String retSrc = null;
            // HttpPost request = new HttpPost(Constants.SMS);
            // List<NameValuePair> nameValuePairs = new
            // ArrayList<NameValuePair>();
            // nameValuePairs.add(new BasicNameValuePair("phone", phone));
            // nameValuePairs.add(new BasicNameValuePair("type_id", Type + ""));
            // nameValuePairs.add(new BasicNameValuePair("UUID", Constants
            // .GetPhoneId(BaseContext)));
            // String Timsp = "1470289975366";//Constants.TimeStamp();
            // nameValuePairs.add(new BasicNameValuePair("timestamp", Timsp));
            // nameValuePairs.add(new BasicNameValuePair("source", "20"));
            // // 加言
            // HashMap<String, String> hashMap = new HashMap<String, String>();
            // hashMap.put("phone", phone);
            // hashMap.put("type_id", Type + "");
            // hashMap.put("UUID", Constants.GetPhoneId(BaseContext));
            // hashMap.put("timestamp", Timsp);
            // hashMap.put("source", "20");
            //
            // nameValuePairs.add(new BasicNameValuePair("sign",
            // Sign(hashMap)));
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
//			post.addHeader("Content-Type", "application/json;charset=utf-8");
//			// 接受数据类型
//			post.addHeader("Accept", "application/json");
            // 请求报文
            StringEntity entity = null;

//			try {
//				entity = new StringEntity(obj.toString(), "UTF-8");
//			} catch (UnsupportedEncodingException e1) {
//				e1.printStackTrace();
//			}

//			post.setEntity(entity);

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

    // public String Sign(HashMap<String, String> map) {
    // HashMap<String, String> da = new HashMap<String, String>();
    //
    // da = map;
    // List<BComment> mBlComments = new ArrayList<BComment>();
    // for (Iterator iter = da.entrySet().iterator(); iter.hasNext();) {
    // Map.Entry element = (Map.Entry) iter.next();
    // String strKey = String.valueOf(element.getKey());
    // String strObj = String.valueOf(element.getValue());
    // mBlComments.add(new BComment(strKey, strObj));
    // }
    // UpComparator m = new UpComparator();
    // Collections.sort(mBlComments, m);
    // String K = "";
    // for (BComment h : mBlComments) {
    // K = K + h.getId() + h.getTitle();
    // }
    //
    // K = K + Constants.SignKey;
    //
    // return Constants.SHA(K);
    //
    // }

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

    @Override
    protected void NetConnect() {
    }

    @Override
    protected void NetDisConnect() {

    }

    @Override
    protected void SetNetView() {

    }

    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.push_topin, R.anim.push_bttomout);
    }
    @Override
    protected void MyClick(View V) {
        switch (V.getId()) {
            case R.id.login_back_iv:
                this.finish();
                overridePendingTransition(R.anim.push_topin, R.anim.push_bttomout);
                break;
            case R.id.verification_getcode_bt:
                verification_code.requestFocus();
                if (!sendedAuthCode) {
                    getAuthCode();
                } else {
                    Animation shake = AnimationUtils.loadAnimation(BaseContext,
                            R.anim.animlayout);
                    verification_getcode_bt.startAnimation(shake);
                }
                break;
            case R.id.verification_submint_bt:// 提交按钮
                // PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                // AInviteCode.class));

                // 微信登录和手机号绑定都需要先校验验证码
                if (!CheCheckCode(2))
                    return;

                if (!WxIsBind) {// 手机号登录
                    PhoneLogin(MyCommintPhone, verification_code.getText()
                            .toString().trim());
                } else {// 微信绑定手机号
                    CheckCode(MyCommintPhone, verification_code.getText()
                            .toString().trim());
                }

                break;
            case R.id.verification_wx_login:// 微信登陆
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ALogin.class));
                BaseActivity.finish();
                break;
            case R.id.verification_deal_txt:// 用户协议
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        AWeb.class).putExtra(AWeb.Key_Bean,
                        new BComment(Constants.AppDeal_Url, getResources()
                                .getString(R.string.app_xieyi1))));
                break;
            default:
                break;
        }
    }

    /**
     * 1标识只检验手机号是不是为空 2标识检验手机号和验证码是不是为空
     *
     * @param type
     * @return
     */
    private boolean CheCheckCode(int type) {
        if (StrUtils.isEmpty(verification_phone.getText().toString().trim())) {
            PromptManager.ShowCustomToast(BaseContext, getResources()
                    .getString(R.string.phone_is_null));
            return false;
        }
        if (verification_phone.getText().toString().trim().length() != 11) {
            PromptManager.ShowCustomToast(BaseContext, getResources()
                    .getString(R.string.phone_is_long));
            return false;
        }
        if (1 == type)
            return true;

        if (StrUtils.isEmpty(verification_code.getText().toString().trim())) {
            PromptManager.ShowCustomToast(BaseContext, getResources()
                    .getString(R.string.code_is_null));
            return false;
        }
        return true;
    }

    /**
     * 手机号登录
     */
    private void PhoneLogin(String Phone, String Code) {
        SetTitleHttpDataLisenter(this);
        PromptManager.showLoading(BaseContext);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("phone", Phone);
        map.put("code", Code);
        FBGetHttpData(map, Constants.Login_Phone, Method.POST, 2,
                LOAD_INITIALIZE);
    }

    /**
     * 手机号和微信进行绑定
     */
    private void BindWxPhone(String weixin_open_id, String Phone) {
        PromptManager.showLoading(BaseContext);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("phone", Phone);
        map.put("weixin_open_id", weixin_open_id);
        FBGetHttpData(map, Constants.Login_Wx_Phone_Bind, Method.POST, 1,
                LOAD_INITIALIZE);
    }

    /**
     * 校验验证码
     */
    private void CheckCode(String Phone, String Code) {
        // SmS_Code_Check
        SetTitleHttpDataLisenter(this);
        PromptManager.showLoading(BaseContext);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("phone", Phone);
        map.put("code", Code);
        FBGetHttpData(map, Constants.SmS_Code_Check, Method.POST, 0,
                LOAD_INITIALIZE);

    }

    @Override
    protected void InItBundle(Bundle bundle) {
    }

    @Override
    protected void SaveBundle(Bundle bundle) {
    }

}
