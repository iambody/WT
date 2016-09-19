package io.vtown.WeiTangApp.fragment.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.PullScrollView;
import io.vtown.WeiTangApp.comment.view.listview.SecondStepView;
import io.vtown.WeiTangApp.fragment.FBase;

/**
 * Created by datutu on 2016/9/18.
 */
public class FMainShop extends FBase {

    private PullScrollView fragment_main_shop_out_scrollview;
    private SecondStepView fragment_main_shop_load_head_iv;
    //背景
    private ImageView fragment_main_iv_shop_cover;
    //头像
    private CircleImageView fragment_main_tab_shop_iv;
    //店铺名字
    private TextView fragment_main_tab_shop_name;
    //店铺描述
    private TextView fragment_main_tab_shop_sign;


    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_main_shop, null);

    }

    @Override
    public void InitCreate(Bundle d) {

    }

    @Override
    public void getResult(int Code, String Msg, BComment Data) {

    }

    @Override
    public void onError(String error, int LoadType) {

    }
}
