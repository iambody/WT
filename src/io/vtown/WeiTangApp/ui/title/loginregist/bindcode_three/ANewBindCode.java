package io.vtown.WeiTangApp.ui.title.loginregist.bindcode_three;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.im.AChatInf;

/**
 * Created by datutu on 2016/10/17.
 */

public class ANewBindCode extends ATitleBase {
    @BindView(R.id.new_bindcode_ed)
    EditText newBindcodeEd;
    @BindView(R.id.new_bindcode_submint_bt)
    TextView newBindcodeSubmintBt;
    @BindView(R.id.new_bindcode_connect)
    TextView newBindcodeConnect;
    private BUser MyUser;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_bindcode_three);
        MyUser = Spuit.User_Get(this);
        ButterKnife.bind(this);
        SetTitleHttpDataLisenter(this);
//
    }

    @Override
    protected void InitTile() {
        SetTitleTxt(getResources().getString(R.string.Bindcode));
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        //通知首页修改状态
        EventBus.getDefault().post(new BMessage(BMessage.Fragment_Home_Bind));
        Spuit.IsHaveBind_Set(BaseContext, true);
        PromptManager.ShowCustomToast(BaseContext, "已绑定");
        //通知个人中心刷新数据
        EventBus.getDefault().post(new BMessage(BMessage.Fragment_Center_ChangStatus));
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
    }

    @Override
    protected void InItBundle(Bundle bundle) {
    }

    @Override
    protected void SaveBundle(Bundle bundle) {
    }

    private void BindCode(String invite_code) {
        if (!StrUtils.isEmpty(MyUser.getInvite_code())
                && MyUser.getInvite_code().equals(invite_code)) {
            PromptManager.ShowCustomToast(BaseContext, "不能绑定自己邀请码");
            return;
        }
        PromptManager.showtextLoading(BaseContext,
                getResources().getString(R.string.loading));
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", Spuit.User_Get(BaseActivity).getMember_id());
        map.put("invite_code", invite_code);
        FBGetHttpData(map, Constants.Login_Bind_Invite_Code, Request.Method.POST, 0,
                LOAD_INITIALIZE);
    }

    @OnClick({R.id.new_bindcode_submint_bt, R.id.new_bindcode_connect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.new_bindcode_submint_bt:
                BindCode(newBindcodeEd.getText().toString().trim());
                break;
            case R.id.new_bindcode_connect:
                Intent intent = new Intent(BaseActivity, AChatInf.class);
                // holder.name.setText(ReciverName);
                intent.putExtra("tagname", Constants.WtHelper);
                String ReciverName = Constants.WtHelper;

                intent.putExtra("title","微糖客服" );
                intent.putExtra("iv","" );
                intent.putExtra("ishepler", true);
                startActivity(intent);
                break;
        }

    }


}
