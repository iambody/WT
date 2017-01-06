package io.vtown.WeiTangApp.ui.title;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by datutu on 2017/1/3.
 */

public class ANewSingn extends ATitleBase {

    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.tv_sign_days)
    TextView tvSignDays;
    @BindView(R.id.rl_sign_btn_layout)
    RelativeLayout rlSignBtnLayout;
    @BindView(R.id.iv_sign_piont_1)
    ImageView ivSignPiont1;
    @BindView(R.id.iv_sign_piont_2)
    ImageView ivSignPiont2;
    @BindView(R.id.iv_sign_piont_3)
    ImageView ivSignPiont3;
    @BindView(R.id.iv_sign_piont_4)
    ImageView ivSignPiont4;
    @BindView(R.id.iv_sign_piont_5)
    ImageView ivSignPiont5;
    private BUser MyUser;
    private Unbinder mBinder;


    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_newsingn);
        mBinder = ButterKnife.bind(this);
        MyUser = Spuit.User_Get(BaseContext);
        SetTitleHttpDataLisenter(this);
        IBaseView();
        IBaseNet();
    }

    private void IBaseNet() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", MyUser.getMember_id());
        FBGetHttpData(map, Constants.NewHomeSign, Request.Method.POST, 10, LOAD_INITIALIZE);
    }

    private void IBaseView() {
        View layout_sign_1 = findViewById(R.id.layout_sign_1);
        View layout_sign_2 = findViewById(R.id.layout_sign_2);
        View layout_sign_3 = findViewById(R.id.layout_sign_3);
        View layout_sign_4 = findViewById(R.id.layout_sign_4);
        View layout_sign_5 = findViewById(R.id.layout_sign_5);
        View layout_sign_6 = findViewById(R.id.layout_sign_6);

    }

    @Override
    protected void InitTile() {
        SetTitleTxt(getResources().getString(R.string.signe));
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        BComment bComment = JSON.parseObject(Data.getHttpResultStr(), BComment.class);
        int sign_number = bComment.getSign_number();

    }

    @Override
    protected void DataError(String error, int LoadType) {

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
    @OnClick(R.id.rl_sign_btn_layout)
    protected void MyClick(View V) {
        switch (V.getId()) {
            case R.id.rl_sign_btn_layout:
                if (CheckNet(BaseContext)) return;
                IBaseNet();
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
