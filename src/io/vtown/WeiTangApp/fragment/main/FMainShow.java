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
public class FMainShow extends FBase{
    @Override
    public void InItView() {

        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_main_show,null);
        Log.i("mainhome","FMainShow===>onCreate");
    }

    @Override
    public void InitCreate(Bundle d) {
        Log.i("mainhome","FMainShow===>onCreateView");
    }

    @Override
    public void getResult(int Code, String Msg, BComment Data) {

    }

    @Override
    public void onError(String error, int LoadType) {

    }
}
