package io.vtown.WeiTangApp.ui.title.center.myshow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.adapter.ShowRecyclerAdapter;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BShow;
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
import io.vtown.WeiTangApp.ui.comment.ACommentList;
import io.vtown.WeiTangApp.ui.title.ABrandDetail;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;

/**
 * Created by Yihuihua on 2016/9/29.
 */

public class ARecyclerOtherShow extends ATitleBase {

    private RecyclerView recyclerview_other_show;
    private View HeadView;
    private CircleImageView center_show_head;
    private ImageView center_show_bg;
    private LinearLayoutManager mLinearLayoutManager;
    private ShowRecyclerAdapter mShowAdapter;
    private HeaderViewRecyclerAdapter mHeadAdapter;
    private boolean IsCanLoadMore = false;
    private boolean IsLoadingMore = false;
    private String lastid = "";
    private View mRootView;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.acvitty_recycler_other_show);
        this.mRootView = LayoutInflater.from(BaseContext).inflate(R.layout.acvitty_recycler_other_show, null);
        SetTitleHttpDataLisenter(this);
        IBundlle();
        IHeaderView();
        IView();
        IData(lastid, LOAD_INITIALIZE);

    }

    private void IBundlle() {
        if (getIntent().getExtras() == null
                && !getIntent().getExtras().containsKey(BaseKey_Bean))
            BaseActivity.finish();
    }

    private void IHeaderView() {
        HeadView = LayoutInflater.from(BaseContext).inflate(R.layout.view_othershow, null);
        center_show_head = (CircleImageView) HeadView.findViewById(R.id.center_show_head);
        center_show_bg = (ImageView) HeadView.findViewById(R.id.comment_othershow_bg);
        center_show_head.setBorderWidth(10);
        center_show_head.setBorderColor(getResources().getColor(R.color.transparent7));
        ImageLoaderUtil.Load2(baseBcBComment.getCover(),
                center_show_bg, R.drawable.error_iv1);
        ImageLoaderUtil.Load2(baseBcBComment.getAvatar(),
                center_show_head, R.drawable.error_iv2);
        center_show_head.setOnClickListener(this);

    }

    private void IView() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerview_other_show = (RecyclerView) findViewById(R.id.recyclerview_other_show);
        recyclerview_other_show.setLayoutManager(mLinearLayoutManager);
        recyclerview_other_show.addItemDecoration(new RecyclerCommentItemDecoration(BaseContext, RecyclerCommentItemDecoration.VERTICAL_LIST, R.drawable.shape_show_divider_line));
        mShowAdapter = new ShowRecyclerAdapter(BaseContext, screenWidth, mRootView, this, false);
        mHeadAdapter = new HeaderViewRecyclerAdapter(mShowAdapter);
        mHeadAdapter.addHeaderView(HeadView);
        recyclerview_other_show.setAdapter(mHeadAdapter);//myShowAdapter
        recyclerview_other_show.addOnScrollListener(new EndlessRecyclerOnScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {

                if (!IsLoadingMore) {
                    if (IsCanLoadMore) {
//                        PromptManager.ShowCustomToast(BaseContext, "开始sss");
                        createLoadMoreView();
                    }
                }

            }
        });
    }

    private void IData(String Lastid, int LoadType) {
        if (LoadType == LOAD_INITIALIZE) {
            PromptManager.showtextLoading(BaseContext, getResources()
                    .getString(R.string.loading));
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("seller_id", baseBcBComment.getId());
        map.put("lastid", Lastid);
        map.put("pagesize", Constants.PageSize + "");
        FBGetHttpData(map, Constants.Get_My_Show, Request.Method.GET, 0,
                LoadType);
    }

    /**
     * 删除我自己的show
     */

    public void DeletMyShow(String ShowId, String seller_id) {
        HashMap<String, String> mHashMap = new HashMap<String, String>();

        mHashMap.put("id", ShowId);
        mHashMap.put("seller_id", seller_id);

        FBGetHttpData(mHashMap, Constants.MyShowDelete, Request.Method.DELETE, 91,
                LOAD_INITIALIZE);

    }

    /**
     * 开始显示
     */
    private void createLoadMoreView() {
        View loadMoreView = LayoutInflater
                .from(BaseContext)
                .inflate(R.layout.swiperefresh_footer, recyclerview_other_show, false);
        mHeadAdapter.addFooterView(loadMoreView);
        IsLoadingMore = true;
        IData(lastid, LOAD_LOADMOREING);
    }

    private void HindLoadMore() {
        mHeadAdapter.RemoveFooterView();
        IsLoadingMore = false;

    }


    @Override
    protected void InitTile() {
        SetTitleTxt(baseBcBComment.getSeller_name());
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {


        switch (Data.getHttpLoadType()) {
            case LOAD_INITIALIZE:
                if (91 == Data.getHttpResultTage()) {// 删除
                    PromptManager.ShowCustomToast(BaseContext, "删除成功");
                    lastid = "";
                    IData(lastid, LOAD_INITIALIZE);
                }
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    PromptManager.ShowCustomToast(BaseContext, getResources().getString(R.string.noshow));
                    return;
                }
                List<BShow> datas = new ArrayList<BShow>();
                datas = JSON.parseArray(Data.getHttpResultStr(), BShow.class);
                lastid = datas.get(datas.size() - 1).getId();
                mShowAdapter.FrashData(datas);
                IsCanLoadMore = datas.size() == Constants.PageSize ? true : false;

                break;

            case LOAD_LOADMOREING:

                HindLoadMore();

                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    IsCanLoadMore = false;
                    return;
                }
                List<BShow> datass = new ArrayList<BShow>();
                datass = JSON.parseArray(Data.getHttpResultStr(), BShow.class);
                lastid = datass.get(datass.size() - 1).getId();
                mShowAdapter.FrashAllData(datass);
                IsCanLoadMore = datass.size() == Constants.PageSize ? true : false;

                break;
        }
    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.ShowCustomToast(BaseContext, error);
        if (LoadType == LOAD_LOADMOREING) {
            HindLoadMore();
        }
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
        switch (V.getId()) {
            case R.id.center_show_head:
                PromptManager.SkipActivity(
                        BaseActivity,
                        new Intent(BaseActivity, baseBcBComment.getIs_brand()
                                .equals("1") ? ABrandDetail.class
                                : AShopDetail.class).putExtra(
                                ACommentList.Tage_ResultKey,
                                ACommentList.Tage_HomePopBrand).putExtra(
                                BaseKey_Bean,
                                new BComment(baseBcBComment.getId(), baseBcBComment
                                        .getSeller_name())));
//                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
//                        AShopDetail.class).putExtra(BaseKey_Bean, baseBcBComment));


                break;
        }
    }

    @Override
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }
}
