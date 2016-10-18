package io.vtown.WeiTangApp.ui.title.loginregist;

import java.util.HashMap;

import com.android.volley.Request.Method;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.ui.AMain;
import io.vtown.WeiTangApp.ui.ui.AMainTab;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @author datutu 新登陆Or新注册用户填写邀请码信息
 * @version 创建时间：2016-5-31 下午1:06:04
 */
public class AInviteCode extends ATitleBase {
    // 输入验证码
    private EditText invitecode_ed;

    // 下一步
    private TextView invitecode_submint_bt;

    /**
     * 是否从center=>我的邀请码右上角=》跳转来绑定 //不是从登录进入的！！！！！！！！
     */
    private boolean IsFromCenter;

    private BUser mBUser;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_invitecode);
        IsFromCenter = getIntent().getBooleanExtra("isfromcenter", false);
        mBUser = Spuit.User_Get(BaseContext);
        IBase();
    }
    private void IBase() {
        invitecode_ed = (EditText) findViewById(R.id.invitecode_ed);
        invitecode_submint_bt = (TextView) findViewById(R.id.invitecode_submint_bt);
        invitecode_submint_bt.setOnClickListener(this);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt("邀请码");
        SetRightText("跳过");
        HindBackIv();
        right_txt.setOnClickListener(this);
        if (IsFromCenter) {
            right_txt.setVisibility(View.GONE);
        }
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        Spuit.InvitationCode_Set(BaseActivity, true);
        if (IsFromCenter) {
            BaseActivity.finish();
            return;
        }
        if (Spuit.IsLogin_RenZheng_Set(BaseContext)) {// 认证过了
            PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
//                    AMain.class));
                    AMainTab.class));

            BaseActivity.finish();
        } else {// 没认证过
            PromptManager.ShowCustomToast(BaseContext, "绑定成功");
            PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                    ARealIdauth.class).putExtra(ARealIdauth.FROM_WHERE_KEY, 1));
            BaseActivity.finish();
        }

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
                if (Spuit.IsLogin_RenZheng_Set(BaseContext)) {// 认证过了
                    PromptManager.SkipActivity(BaseActivity, new Intent(
                            BaseActivity, AMain.class));
                    BaseActivity.finish();
                } else {// 没认证过
                    PromptManager.SkipActivity(BaseActivity, new Intent(
                            BaseActivity, ARealIdauth.class).putExtra(
                            ARealIdauth.FROM_WHERE_KEY, 1));
                    BaseActivity.finish();
                }

                break;
            case R.id.invitecode_submint_bt:// 下一步
                if (StrUtils.EditTextIsEmPty(invitecode_ed)) {
                    PromptManager.ShowCustomToast(BaseContext, "请填写邀请码");
                    return;
                }
                BindCode(invitecode_ed.getText().toString().trim());
                break;

            default:
                break;
        }
    }

    private void BindCode(String invite_code) {
        if (!StrUtils.isEmpty(mBUser.getInvite_code())
                && mBUser.getInvite_code().equals(invite_code)) {
            PromptManager.ShowCustomToast(BaseContext, "不能绑定自己邀请码");
            return;
        }

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, AMainTab.class));
            BaseActivity.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void InItBundle(Bundle bundle) {
    }

    @Override
    protected void SaveBundle(Bundle bundle) {
    }

}
