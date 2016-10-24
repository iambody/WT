package io.vtown.WeiTangApp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.ui.title.loginregist.ALogin;

/**
 * Created by datutu on 2016/10/13.
 */

public class AExitNull extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_null_for_shopdetailskip);
//        AppManager.getAppManager().finishAllActivity();
//        PromptManager.SkipActivity(this,new Intent(this, ALogin.class));
        this.finish();
    }
}
