package io.vtown.WeiTangApp.ui.title.center.myshow;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.adapter.ShowRecyclerAdapter;
import io.vtown.WeiTangApp.bean.bcache.BShop;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BShow;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
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
    // 需要他的封面和头像
    private BShop bShop;

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
        bShop = Spuit.Shop_Get(BaseContext);
        IView();
        IData(lastid, LOAD_INITIALIZE);
    }

    private void IView() {
        RelativeLayout myshow_head_lay = (RelativeLayout) findViewById(R.id.myshow_head_lay);
        ImageView center_show_bg_iv = (ImageView) findViewById(R.id.center_show_bg_iv);
        TextView center_show_head_myname = (TextView) findViewById(R.id.center_show_head_myname);
        CircleImageView center_show_head = (CircleImageView) findViewById(R.id.center_show_head);
        center_show_head.setBorderWidth(10);
        center_show_head.setBorderColor(getResources().getColor(R.color.transparent7));
        ImageLoaderUtil.Load2(bShop.getCover(), center_show_bg_iv,
                R.drawable.error_iv2);
        ImageLoaderUtil.Load2(bShop.getAvatar(), center_show_head,
                R.drawable.error_iv1);


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                screenWidth, screenWidth / 2);// new lParams(screenWidth,
        // screenWidth/2);
        center_show_bg_iv.setLayoutParams(layoutParams);

        // 设置头像
        LinearLayout.LayoutParams pasLayoutParams = new LinearLayout.LayoutParams(
                screenWidth / 4, screenWidth / 4);
        pasLayoutParams.setMargins(screenWidth * 11 / 16, -(screenWidth / 8),
                0, 0);
        center_show_head.setLayoutParams(pasLayoutParams);
        StrUtils.SetTxt(center_show_head_myname, bShop.getSeller_name());

       recyclerview_my_show = (RecyclerView) findViewById(R.id.recyclerview_my_show);
        recyclerview_my_show.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_my_show.addItemDecoration(new RecyclerCommentItemDecoration(BaseContext, RecyclerCommentItemDecoration.VERTICAL_LIST, R.drawable.shape_show_divider_line));
        myShowAdapter = new ShowRecyclerAdapter(BaseContext, screenWidth);
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


}
