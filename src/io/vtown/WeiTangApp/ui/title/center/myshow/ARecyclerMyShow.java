package io.vtown.WeiTangApp.ui.title.center.myshow;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.adapter.ShowRecyclerAdapter;
import io.vtown.WeiTangApp.bean.bcache.BShop;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BShow;
import io.vtown.WeiTangApp.bean.bcomment.new_three.BNewHome;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.RecyclerCommentItemDecoration;
import io.vtown.WeiTangApp.comment.view.custom.EndlessRecyclerOnScrollListener;
import io.vtown.WeiTangApp.comment.view.custom.HeaderViewRecyclerAdapter;
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

    private HeaderViewRecyclerAdapter MyHeadAdapter;
    // 需要他的封面和头像
//    private BShop bShop;
    private BNewHome MBNewHome;
    private View HeadView;
    private CircleImageView center_show_head;
    private ImageView center_show_bg;

    private LinearLayoutManager MLinearLayoutManager;
    private boolean IsCanLoadMore = false;
    private boolean IsLoadingMore = false;
    private View mRootView ;
    private  View comment_myshow_no__lay;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_recycler_my_show);
        this.mRootView = LayoutInflater.from(BaseContext).inflate(R.layout.activity_recycler_my_show,null);
        SetTitleHttpDataLisenter(this);
        String seller_id = getIntent().getStringExtra("seller_id");
        if (StrUtils.isEmpty(seller_id)) {
            return;
        } else {
            _seller_id = seller_id;
        }
        user_get = Spuit.User_Get(BaseContext);
//        bShop = Spuit.Shop_Get(BaseContext);
        MBNewHome= JSON.parseObject( CacheUtil.NewHome_Get(BaseContext), BNewHome.class);
        IView();
        ICache();
        IData(lastid, LOAD_INITIALIZE);
    }

    private void IView() {
        comment_myshow_no__lay=findViewById(R.id.comment_myshow_no__lay);
        HeadView = LayoutInflater.from(BaseContext).inflate(R.layout.view_othershow, null);

        center_show_head = (CircleImageView) HeadView.findViewById(R.id.center_show_head);
        center_show_bg = (ImageView) HeadView.findViewById(R.id.comment_othershow_bg);
        center_show_head.setBorderWidth(10);
        center_show_head.setBorderColor(getResources().getColor(R.color.transparent7));

        ImageLoaderUtil.Load2(MBNewHome.getSellerinfo().getCover(),
                center_show_bg, R.drawable.error_iv1);
        ImageLoaderUtil.Load2(MBNewHome.getSellerinfo().getAvatar(),
                center_show_head, R.drawable.error_iv2);

        MLinearLayoutManager = new LinearLayoutManager(this);
        recyclerview_my_show = (RecyclerView) findViewById(R.id.recyclerview_my_show);
        recyclerview_my_show.setLayoutManager(MLinearLayoutManager);
        recyclerview_my_show.addItemDecoration(new RecyclerCommentItemDecoration(BaseContext, RecyclerCommentItemDecoration.VERTICAL_LIST, R.drawable.shape_show_divider_line));
        myShowAdapter = new ShowRecyclerAdapter(BaseContext, screenWidth,mRootView,this,false);

        MyHeadAdapter = new HeaderViewRecyclerAdapter(myShowAdapter);
        MyHeadAdapter.addHeaderView(HeadView);
        recyclerview_my_show.setAdapter(MyHeadAdapter);//myShowAdapter
        recyclerview_my_show.addOnScrollListener(new EndlessRecyclerOnScrollListener(MLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {

                if(!IsLoadingMore){
                    if (IsCanLoadMore) {
                        PromptManager.ShowCustomToast(BaseContext, "开始sss");
                        createLoadMoreView();
                    }
                }

            }
        });

    }


    private void ICache() {
        String CacheStr = CacheUtil.MyShow_Get(BaseContext);
        if (StrUtils.isEmpty(CacheStr)){
            PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.xlistview_header_hint_loading));
            return;
        }

        // 开始显示缓存数据

        List<BShow> datas = new ArrayList<BShow>();
        try {
            datas = JSON.parseArray(CacheStr, BShow.class);

        } catch (Exception e) {

            return;
        }
        myShowAdapter.FrashData(datas);
    }

    /**
     * 开始显示
     */
    private void createLoadMoreView() {
        View loadMoreView = LayoutInflater
                .from(BaseContext)
                .inflate(R.layout.swiperefresh_footer, recyclerview_my_show, false);
        MyHeadAdapter.addFooterView(loadMoreView);
        IsLoadingMore = true;
        IData(lastid, LOAD_LOADMOREING);

    }

    private void HindLoadMore() {
        MyHeadAdapter.RemoveFooterView();
        IsLoadingMore = false;
    }

    private void IData(String lastid, int loadtype) {
//        if (LOAD_INITIALIZE == loadtype) {
//            PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.xlistview_header_hint_loading));
//        }

        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", _seller_id);
        map.put("lastid", lastid);
        map.put("pagesize", Constants.PageSize + "");
        FBGetHttpData(map, Constants.Get_My_Show, Request.Method.GET, 0, loadtype);
    }

    /**
     * 删除我自己的show
     */

    public void DeletMyShow(String ShowId,String seller_id) {
        HashMap<String, String> mHashMap = new HashMap<String, String>();

        mHashMap.put("id", ShowId);
        mHashMap.put("seller_id", seller_id);

        FBGetHttpData(mHashMap, Constants.MyShowDelete, Request.Method.DELETE, 91,
                LOAD_INITIALIZE);

    }

    @Override
    protected void InitTile() {
        SetTitleTxt("我的SHOW");
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {


        switch (Data.getHttpLoadType()) {
            case LOAD_INITIALIZE:
                if (91 == Data.getHttpResultTage()) {// 删除
                    PromptManager.ShowCustomToast(BaseContext, "删除成功");
                    lastid = "";
                    IData(lastid, LOAD_INITIALIZE);
                    //return;
                }

                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    CacheUtil.MyShow_Save(BaseContext,"");
                    myShowAdapter.FrashData(new ArrayList<BShow>());
                    IDataView(recyclerview_my_show, comment_myshow_no__lay, NOVIEW_ERROR);
                    ShowErrorIv(R.drawable.error_show);
                    ShowErrorCanLoad(getResources().getString(R.string.younoshow));
                    return;
                }
                CacheUtil.MyShow_Save(BaseContext,Data.getHttpResultStr());
                List<BShow> datas = new ArrayList<BShow>();
                datas = JSON.parseArray(Data.getHttpResultStr(), BShow.class);
                lastid = datas.get(datas.size() - 1).getId();
                myShowAdapter.FrashData(datas);
                IsCanLoadMore = datas.size() == Constants.PageSize ? true : false;

                break;

            case LOAD_LOADMOREING:

                HindLoadMore();

                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    IsCanLoadMore=false;
                    return;
                }
                List<BShow> datass = new ArrayList<BShow>();
                datass = JSON.parseArray(Data.getHttpResultStr(), BShow.class);
                lastid = datass.get(datass.size() - 1).getId();
                myShowAdapter.FrashAllData(datass);
                IsCanLoadMore = datass.size() == Constants.PageSize ? true : false;

                break;
        }

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
