package io.vtown.WeiTangApp.ui.title;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;

import java.util.HashMap;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by datutu on 2017/1/3.
 */

public class ANewSingn extends ATitleBase {

    private BUser MyUser;
    private TextView jj;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_newsingn);
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
        jj = (TextView) findViewById(R.id.jj);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt(getResources().getString(R.string.signe));
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        StrUtils.SetTxt(jj, Data.getHttpResultStr());
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
    protected void MyClick(View V) {

    }

    @Override
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }
}
