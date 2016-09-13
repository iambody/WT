package io.vtown.WeiTangApp.ui.title.loginregist;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.pop.PPassWord;
import io.vtown.WeiTangApp.event.interf.OnPasswordInputFinish;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.center.set.ARealIdauthSucceed;
import io.vtown.WeiTangApp.ui.title.center.set.AResetPswStep2;
import io.vtown.WeiTangApp.ui.title.center.wallet.AAddAliPay;
import io.vtown.WeiTangApp.ui.title.center.wallet.AAddBankCard;
import io.vtown.WeiTangApp.ui.title.center.wallet.ACenterWallet;
import io.vtown.WeiTangApp.ui.ui.AMain;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request.Method;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @author 首次登陆后未做实名认证的需要进入=》 实名认证界面
 * @version 创建时间：2016-5-31 下午1:40:31
 */
public class ARealIdauth extends ATitleBase {

    /**
     * 输入姓名
     */
    private EditText realid_name_ed;
    /**
     * 输入身份证
     */
    private EditText realid_identityid_ed;
    /**
     * 完成
     */
    private TextView realid_submint_bt;
    /**
     * view
     */

    private int from_where_value;

    public static final String FROM_WHERE_KEY = "ARealIdauth";
    private View BVivew;
    private static String Psd;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_realid_auth);
        BVivew = LayoutInflater.from(this).inflate(
                R.layout.activity_realid_auth, null);
        IBase();
    }

    private void IBase() {
        realid_name_ed = (EditText) findViewById(R.id.realid_name_ed);
        realid_identityid_ed = (EditText) findViewById(R.id.realid_identityid_ed);
        realid_submint_bt = (TextView) findViewById(R.id.realid_submint_bt);
        realid_submint_bt.setOnClickListener(this);

        from_where_value = getIntent().getIntExtra(FROM_WHERE_KEY, 0);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt(getString(R.string.Real_auth_title));
        HindBackIv();
        if (1 == from_where_value) {
            SetRightText("跳过");
            right_txt.setOnClickListener(this);
        }

    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        Spuit.IsLogin_RenZheng_Save(BaseContext, realid_name_ed.getText()
                .toString().trim(), realid_identityid_ed.getText().toString()
                .trim());

        switch (from_where_value) {
            case 1:// 从登录进来
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        AMain.class));
                break;
            case 2:// 我的-实名认证进来
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                        ARealIdauthSucceed.class));
                break;
            case 3:// 账户安全进来
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                        AResetPswStep2.class));
                break;

            case 8:// 钱包
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                        ACenterWallet.class));
                break;

            default:
                break;
        }


        BaseActivity.finish();
    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.ShowCustomToast(BaseContext, error);
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
            case R.id.right_txt:// 跳过
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        AMain.class));
                BaseActivity.finish();
                break;
            case R.id.realid_submint_bt:// 点击完成需要弹出密码框输入密码
                if (IsCheckName()) {
                    // 键盘关闭
                    hintKbTwo();
                    ToSetPassd(0);
                }
                break;
            default:
                break;
        }
    }

    private boolean IsCheckName() {
        if (StrUtils.isEmpty(realid_name_ed.getText().toString().trim())) {
            PromptManager.ShowCustomToast(BaseContext, "请填写姓名");
            return false;
        }
        String id_numb = realid_identityid_ed.getText().toString().trim();
        if (!StrUtils.checkIdNo(BaseContext, id_numb)) {
            return false;
        }

//		if (StrUtils.isEmpty(realid_identityid_ed.getText().toString().trim())) {
//			PromptManager.ShowCustomToast(BaseContext, "请填写身份证号");
//			return false;
//		}

        if (!Spuit.IsLogin_Get(BaseContext)) {
            PromptManager.ShowCustomToast(BaseContext, "请先登录");
            PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                    ALogin.class));
            BaseActivity.finish();
            return false;
        }

        return true;
    }

    /**
     * 0标识第一次输入；；1标识第二次输入
     */
    private void ToSetPassd(final int Type) {
        final PPassWord pp = new PPassWord(BaseContext, screenWidth,
                0 == Type ? "输入交易密码" : "再次输入密码");
        pp.setOnPassWordListener(new OnPasswordInputFinish() {
            @Override
            public void inputFinish(String getStrPassword) {
                // PromptManager.ShowCustomToast(BaseContext, getStrPassword);
                if (0 == Type) {
                    Psd = getStrPassword;
                    pp.dismiss();
                    PromptManager.ShowCustomToast(BaseContext, "再次输入密码");
                    ToSetPassd(2);
                } else {
                    if (!Psd.equals(getStrPassword)) {
                        PromptManager.ShowCustomToast(BaseContext, "密码不一致重新输入");
                        ToSetPassd(0);
                        return;
                    } else {// 第二次 输入成功可以进行提交

                        BUser mBUser = Spuit.User_Get(BaseContext);

                        ApproveSubmit(mBUser.getId(), realid_identityid_ed
                                        .getText().toString().trim(),
                                mBUser.getSeller_id(), realid_name_ed.getText()
                                        .toString().trim(), Psd, Psd);
                        pp.dismiss();
                        Psd = null;
                    }
                }

            }

            @Override
            public void LostPassWord() {
                PromptManager.ShowCustomToast(BaseContext, "忘记密码怎么办");
            }

            @Override
            public void Cancle() {
                pp.dismiss();
            }
        });
        pp.showAtLocation(BVivew, Gravity.CENTER, 0, 0);
    }

    /**
     * @param member_id
     * @param identity_card身份证
     * @param seller_id
     * @param name真实姓名
     * @param password
     * @param password2
     */
    private void ApproveSubmit(String member_id, String identity_card,
                               String seller_id, String name, String password, String password2) {
        SetTitleHttpDataLisenter(this);
        PromptManager.showLoading(BaseContext);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", member_id);
        String identity_card_rsa = Constants.RSA(identity_card, BaseContext);
        map.put("identity_card", identity_card_rsa);
        map.put("seller_id", seller_id);
        map.put("name", name);
        String pasdd = Constants.RSA(password, BaseContext);
        map.put("password", pasdd);
        map.put("password2", pasdd);

        FBGetHttpData(map, Constants.Login_Real_RenZheng, Method.POST, 0,
                LOAD_INITIALIZE);
    }

    @Override
    protected void InItBundle(Bundle bundle) {
    }

    @Override
    protected void SaveBundle(Bundle bundle) {
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, AMain.class));
            BaseActivity.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
