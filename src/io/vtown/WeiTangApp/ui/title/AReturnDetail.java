package io.vtown.WeiTangApp.ui.title;

import android.os.Bundle;
import android.view.View;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by Yihuihua on 2016/10/12.
 * 返佣明细页面
 */

public class AReturnDetail extends ATitleBase {
    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_return_detail);
    }

    @Override
    protected void InitTile() {

    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

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
