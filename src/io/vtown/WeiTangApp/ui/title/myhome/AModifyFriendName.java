package io.vtown.WeiTangApp.ui.title.myhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by Yihuihua on 2016/12/19.
 */
public class AModifyFriendName extends ATitleBase {
    @BindView(R.id.et_modify_friend_name)
    EditText etModifyFriendName;
    private Unbinder mBinder;
    private String mFriendName = "";
    private String mMember_id = "";
    public static final String FRIEND_NAME_KEY = "friend_name_key";
    public static final String MEMBER_ID_KEY = "member_id_key";


    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_modify_friend_name);
        mBinder = ButterKnife.bind(this);
        IBundle();
        IView();
    }

    private void IBundle() {
        Intent intent = getIntent();
        mFriendName = intent.getStringExtra(FRIEND_NAME_KEY);
        mMember_id = intent.getStringExtra(MEMBER_ID_KEY);
    }

    private void IView() {
        if(!StrUtils.isEmpty(mFriendName)){
            etModifyFriendName.setText(mFriendName);
            etModifyFriendName.setSelection(mFriendName.length());
        }
    }

    @Override
    protected void InitTile() {
        SetTitleTxt("修改备注");
        SetRightText("完成");
        right_txt.setOnClickListener(this);
    }

    private void modifyName(String name) {
        SetTitleHttpDataLisenter(this);
        PromptManager.showLoading(BaseContext);
        HashMap<String, String> map = new HashMap<>();
        map.put("member_id",mMember_id);
        map.put("remark",name);
        FBGetHttpData(map, Constants.Friends_Remark, Request.Method.PUT,0,LOAD_INITIALIZE);
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        AModifyFriendName.this.finish();
        BMessage msg = new BMessage(BMessage.REMARK_FRIENDS);
        msg.setRemark_name(etModifyFriendName.getText().toString().trim());
        EventBus.getDefault().post(msg);
    }

    @Override
    protected void DataError(String error, int LoadType) {
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
            case R.id.right_txt:
                if (CheckNet(BaseContext)) return;
                String edit_name = etModifyFriendName.getText().toString().trim();
                if (mFriendName.equals(edit_name)) {
                    AModifyFriendName.this.finish();
                } else {
                    modifyName(edit_name);
                }

                break;
        }

    }

    @Override
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinder.unbind();
    }
}
