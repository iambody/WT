package io.vtown.WeiTangApp.fragment.main;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.chanven.lib.cptr.loadmore.SwipeRefreshHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.adapter.MyIvdapter;
import io.vtown.WeiTangApp.adapter.ShowRecyclerAdapter;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BLShow;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BShow;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DateUtils;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.RecyclerCommentItemDecoration;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.custom.EndlessRecyclerOnScrollListener;
import io.vtown.WeiTangApp.comment.view.custom.HeaderViewRecyclerAdapter;
import io.vtown.WeiTangApp.comment.view.custom.RefreshLayout;
import io.vtown.WeiTangApp.comment.view.custom.recycle.RefreshRecyclerView;
import io.vtown.WeiTangApp.fragment.FBase;

/**
 * Created by datutu on 2016/9/26.
 */

public class FMainNewShow extends FBase implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {

//    private RefreshLayout fragment_newshow_refrash;
    private RefreshRecyclerView maintab_newshow_recyclerview;
    private View maintab_newshow_nodata_lay;
    //    private TextView maintab_newshow_uptxt;
    //AP
    private ShowRecyclerAdapter myNewShowAdapter;
    //布局管理器
    private LinearLayoutManager linearLayoutManager;

    //数据

    private BUser MBUser;
    private String LastId = "";
    private boolean IsCache;//是否处于缓存显示数据中
    private List<BShow> ShowData = new ArrayList<BShow>();
    private HeaderViewRecyclerAdapter MyHeadAdapter;

    //记录是否可以加载更多
    private boolean IsCanLoad=true;
    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_newshow, null);
        MBUser = Spuit.User_Get(BaseContext);
        SetTitleHttpDataLisenter(this);
        IView();
        ICacheShow();
        IData(LastId, INITIALIZE);
    }

    private void ICacheShow() {
        // 缓存中有show的数据进行处理
        String CachData = Spuit.Show_GetStr(BaseContext);
        if (!StrUtils.isEmpty(CachData)) {
            // 开始解析*************************
            try {
                ShowData = JSON.parseArray(CachData, BShow.class);// ();
            } catch (Exception e) {
                PromptManager.ShowCustomToast(BaseContext, "缓存时候解析出错");
                return;
            }
//            if(ShowData.size()==20)IsCanLoad=true;
            myNewShowAdapter.FrashData(ShowData);
            IsCache = true;
        } else {// 没有数据就直接显示空白
            IsCache = false;
            IDataView(maintab_newshow_recyclerview, maintab_newshow_nodata_lay, NOVIEW_INITIALIZE);
        }
    }

    private void IView() {
//        fragment_newshow_refrash= (RefreshLayout) BaseView.findViewById(R.id.fragment_newshow_refrash);
//        maintab_newshow_uptxt = (TextView) BaseView.findViewById(R.id.maintab_newshow_uptxt);
        maintab_newshow_nodata_lay = BaseView.findViewById(R.id.maintab_newshow_nodata_lay);

        maintab_newshow_recyclerview = (RefreshRecyclerView) BaseView.findViewById(R.id.maintab_newshow_recyclerview);
        //绑定布局set
        linearLayoutManager = new LinearLayoutManager(BaseContext);
        maintab_newshow_recyclerview.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        //
        myNewShowAdapter = new ShowRecyclerAdapter(BaseContext, screenWidth, BaseView, false, BaseActivity);
//        MyHeadAdapter = new HeaderViewRecyclerAdapter(myNewShowAdapter);
        maintab_newshow_recyclerview.setAdapter(myNewShowAdapter);//(myNewShowAdapter);MyHeadAdapter
        maintab_newshow_recyclerview.addItemDecoration(new RecyclerCommentItemDecoration(BaseContext, RecyclerCommentItemDecoration.VERTICAL_LIST, R.drawable.shape_show_divider_line));
        maintab_newshow_recyclerview.setItemAnimator(new DefaultItemAnimator());
        maintab_newshow_recyclerview.setLoadMoreEnable(true);//允许加
        maintab_newshow_recyclerview.setFooterResource(R.layout.swiperefresh_footer);
        maintab_newshow_recyclerview.setOnLoadMoreListener(new RefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void loadMoreListener() {
                PromptManager.ShowCustomToast(BaseContext,"开始是是是");
            }
        });
        //初始化分页frash
//        fragment_newshow_refrash.setOnRefreshListener(this);
//        maintab_newshow_recyclerview.setColorSchemeResources(R.color.app_fen,R.color.app_fen1,R.color.app_fen2,R.color.app_fen3);
//        maintab_newshow_recyclerview.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int currentPage) {
////                IData(LastId, LOADMOREING);
//
//                if (IsCanLoad) {
//                    IsCanLoad=!IsCanLoad;
//                    PromptManager.ShowCustomToast(BaseContext, "开始第" + currentPage + "页");
//                    createLoadMoreView();
//                    IData(LastId, LOADMOREING);
//                }
//            }
//        });

    }

    /**
     * 开始显示
     */
    private void createLoadMoreView() {
        View loadMoreView = LayoutInflater
                .from(BaseContext)
                .inflate(R.layout.swiperefresh_footer, maintab_newshow_recyclerview, false);
        MyHeadAdapter.addFooterView(loadMoreView);
    }

    private void HindLoadMore() {
        MyHeadAdapter.RemoveFooterView();
    }

    /**
     * 获取数据后就开始消失
     *
     * @param LastId
     * @param LoadType
     */

    private void IData(String LastId, int LoadType) {
        if (LoadType == INITIALIZE && !IsCache)
            PromptManager.showtextLoading(BaseContext, getResources()
                    .getString(R.string.loading));

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("seller_id", MBUser.getSeller_id());
        map.put("lastid", LastId);
        map.put("pagesize", Constants.PageSize + "");
        FBGetHttpData(map, Constants.Show_ls, Request.Method.GET, 0, LoadType);
    }

    @Override
    public void InitCreate(Bundle d) {

    }

    @Override
    public void getResult(int Code, String Msg, BComment Data) {
        switch (Data.getHttpResultTage()) {
            case 0:// 获取商品的列表
                //只要是返回200数据就进行保存
                Spuit.Show_SaveStr(BaseContext, Data.getHttpResultStr());
                switch (Data.getHttpLoadType()) {
                    case INITIALIZE://初始化
                        if (StrUtils.isEmpty(Data.getHttpResultStr())) {//空的

                            return;
                        }
                        ShowData = JSON.parseArray(Data.getHttpResultStr(), BShow.class);
                        myNewShowAdapter.FrashData(ShowData);
                        LastId = ShowData.get(ShowData.size() - 1).getId();

                        if (ShowData.size() < Constants.PageSize2)
//                            fragment_newshow_refrash.setCanLoadMore(false);
                            IsCanLoad = false;
                        else
//                            fragment_newshow_refrash.setCanLoadMore(true);
                            IsCanLoad = true;
                        break;
                    case REFRESHING:
//                        fragment_newshow_refrash.setRefreshing(false);
                        if (StrUtils.isEmpty(Data.getHttpResultStr())) {//空的
                            return;
                        }
                        ShowData = JSON.parseArray(Data.getHttpResultStr(), BShow.class);
                        myNewShowAdapter.FrashData(ShowData);
                        LastId = ShowData.get(ShowData.size() - 1).getId();
                        if (ShowData.size() < Constants.PageSize2)
//                            fragment_newshow_refrash.setCanLoadMore(false);
                            IsCanLoad = false;
                        else
//                            fragment_newshow_refrash.setCanLoadMore(true);
                            IsCanLoad = true;
                        break;
                    case LOADMOREING:
//                        fragment_newshow_refrash.setLoading(false);
                        HindLoadMore();
                        if (StrUtils.isEmpty(Data.getHttpResultStr())) {//空的
//                            fragment_newshow_refrash.setCanLoadMore(false);
                            return;
                        }
//                        MyHeadAdapter.RemoveFooterView();
                        List<BShow> GetData = JSON.parseArray(Data.getHttpResultStr(), BShow.class);

                        ShowData.addAll(GetData);
                        myNewShowAdapter.FrashAllData(ShowData);
                        LastId = GetData.get(GetData.size() - 1).getId();
                        if (GetData.size() < Constants.PageSize2)
//                            fragment_newshow_refrash.setCanLoadMore(false);
                            IsCanLoad = false;
                        else
//                            fragment_newshow_refrash.setCanLoadMore(true);
                            IsCanLoad = true;
                        break;
                }
                break;
            case 11:// 删除我的show
                PromptManager.ShowCustomToast(BaseContext, "删除成功");
//                myNewShowAdapter.DeleteFrashAp(Data.getHttpLoadType() - 11);
                break;

            default:
                break;
        }


    }

    @Override
    public void onError(String error, int LoadType) {
        switch (LoadType) {
            case INITIALIZE:
                break;
            case LOADMOREING:
//                fragment_newshow_refrash.setLoading(false);
//                HindLoadMore();
                break;
            case REFRESHING:
//                fragment_newshow_refrash.setRefreshing(false);
                break;
            case LOADHind:
                break;
            default:
                break;
        }
        PromptManager.ShowCustomToast(BaseContext, error);
    }

    @Override
    public void onClick(View v) {

    }



    @Override
    public void onRefresh() {
        LastId = "";
        IData(LastId, REFRESHING);
    }

//    @Override
//    public void OnLoadMore() {
//        PromptManager.ShowCustomToast(BaseContext, "滑动底部");
////         IData(LastId, LOADMOREING);
//    }
//
//    @Override
//    public void OnFrash() {
//        LastId = "";
//        IData(LastId, REFRESHING);
//    }


}
