package io.vtown.WeiTangApp.ui.comment;

import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;

import java.util.HashMap;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by datutu on 2016/9/10.
 * 本搜索是可以分页加载的注意！！！！！！！！！！
 */
public class AShopGoodSou extends ATitleBase {

    /**
     * 记录当前的列表页数
     */
    private int CurrentPage = 1;
    /**
     * 记录当前搜索哦的搜索词语
     */
    private String CurrentTitle;


    private String SellId;
    private String Title;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_shopgoodsou);
        SellId = getIntent().getStringExtra("Sellid");
        Title = getIntent().getStringExtra("Sellname");
    }

    @Override
    protected void InitTile() {
        SetTitleTxt(StrUtils.NullToStr(Title));
    }

    /**
     * 搜索
     */
    private void ISouConnect(int LoadType) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("title", CurrentTitle);
        hashMap.put("seller_id", SellId);

        hashMap.put("page", CurrentPage + "");
        hashMap.put("pagesize", Constants.PageSize + "");
        FBGetHttpData(hashMap, Constants.ShopGoodSou, Request.Method.GET, 1, LoadType);

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
