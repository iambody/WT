package io.vtown.WeiTangApp.ui.title.center.myshow;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.adapter.ShowRecyclerAdapter;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BShow;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.RecyclerCommentItemDecoration;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by Yihuihua on 2016/9/23.
 */

public class ARecyclerMyShow extends ATitleBase {

    private RecyclerView recyclerview_my_show;
    private BUser user_get;
    private ShowRecyclerAdapter myShowAdapter;
    private String lastid = "";
    private String _seller_id;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_recycler_my_show);
        String seller_id = getIntent().getStringExtra("seller_id");
        if (StrUtils.isEmpty(seller_id)) {
            return;
        } else {
            _seller_id = seller_id;
        }
        user_get = Spuit.User_Get(BaseContext);
        IView();
        IData(lastid, LOAD_INITIALIZE);
    }

    private void IView() {
        recyclerview_my_show = (RecyclerView) findViewById(R.id.recyclerview_my_show);
        recyclerview_my_show.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_my_show.addItemDecoration(new RecyclerCommentItemDecoration(BaseContext, RecyclerCommentItemDecoration.VERTICAL_LIST, R.drawable.shape_show_divider_line));
        myShowAdapter = new ShowRecyclerAdapter(BaseContext, screenWidth, _seller_id.equals(user_get.getSeller_id()));
        recyclerview_my_show.setAdapter(myShowAdapter);
    }

    private void IData(String lastid, int loadtype) {
        if (LOAD_INITIALIZE == loadtype) {
            PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.xlistview_header_hint_loading));
        }
        SetTitleHttpDataLisenter(this);
        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", _seller_id);
        map.put("lastid", lastid);
        map.put("pagesize", Constants.PageSize + "");
        FBGetHttpData(map, Constants.Get_My_Show, Request.Method.GET, 0, loadtype);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt("我的SHOW");
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        if (StrUtils.isEmpty(Data.getHttpResultStr())) {
            return;
        }
        List<BShow> datas = new ArrayList<BShow>();
        try {
            datas = JSON.parseArray(Data.getHttpResultStr(), BShow.class);
        } catch (Exception e) {
            return;
        }
        myShowAdapter.FrashData(datas);

    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.ShowCustomToast(BaseContext, error);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
