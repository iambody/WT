package io.vtown.WeiTangApp.fragment.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.fragment.FBase;

/**
 * Created by datutu on 2016/9/18.
 */
public class FMainCenter extends FBase {
    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_main_center, null);
        Log.i("mainhome","FMainCenter===>onCreate ");
    }

    @Override
    public void InitCreate(Bundle d) {
        Log.i("mainhome","FMainCenter===>onCreateView");
    }

    @Override
    public void getResult(int Code, String Msg, BComment Data) {

    }

    @Override
    public void onError(String error, int LoadType) {

    }
}
