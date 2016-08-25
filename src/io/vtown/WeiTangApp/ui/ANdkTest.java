package io.vtown.WeiTangApp.ui;

import android.os.Bundle;
import android.app.Activity;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.ndk.NdkUtils;

public class ANdkTest extends ABase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_andk_test);
        NdkUtils.getTestStringFormC();
    }

}
