package io.vtown.WeiTangApp.fragment.main;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.adapter.MyIvdapter;
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
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.custom.RefreshLayout;
import io.vtown.WeiTangApp.fragment.FBase;

/**
 * Created by datutu on 2016/9/26.
 */

public class FMainNewShow extends FBase implements RefreshLayout.OnLoadListener, View.OnClickListener {

    private RefreshLayout fragment_newshow_refrash;
    private RecyclerView maintab_newshow_recyclerview;
    private View maintab_newshow_nodata_lay;
    private TextView maintab_newshow_uptxt;
    //AP
    private MyNewShowAdapter myNewShowAdapter;
    //布局管理器
    private LinearLayoutManager linearLayoutManager;

    //数据

    private BUser MBUser;
    private String LastId = "";
    private boolean IsCache;//是否处于缓存显示数据中
    private List<BShow> ShowData = new ArrayList<BShow>();

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
            myNewShowAdapter.FrashAp();
            IsCache = true;
        } else {// 没有数据就直接显示空白
            IsCache = false;
            IDataView(maintab_newshow_recyclerview, maintab_newshow_nodata_lay, NOVIEW_INITIALIZE);
        }
    }

    private void IView() {
        maintab_newshow_uptxt = (TextView) BaseView.findViewById(R.id.maintab_newshow_uptxt);
        maintab_newshow_nodata_lay = BaseView.findViewById(R.id.maintab_newshow_nodata_lay);

        maintab_newshow_recyclerview = (RecyclerView) BaseView.findViewById(R.id.maintab_newshow_recyclerview);
        //绑定布局set
        linearLayoutManager = new LinearLayoutManager(BaseContext);
        maintab_newshow_recyclerview.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        //
        myNewShowAdapter = new MyNewShowAdapter();
        maintab_newshow_recyclerview.setAdapter(myNewShowAdapter);
//        maintab_newshow_recyclerview.addItemDecoration(new RecyclerCommentItemDecoration(BaseContext));
        maintab_newshow_recyclerview.setItemAnimator(new DefaultItemAnimator());
        //初始化分页frash
        fragment_newshow_refrash = (RefreshLayout) BaseView.findViewById(R.id.fragment_newshow_refrash);
        fragment_newshow_refrash.setOnLoadListener(this);
        fragment_newshow_refrash.setColorSchemeResources(R.color.app_fen, R.color.app_fen1, R.color.app_fen2, R.color.app_fen3);
    }

    private void IData(String LastId, int LoadType) {
        if (LoadType == INITIALIZE && !IsCache)
            PromptManager.showtextLoading(BaseContext, getResources()
                    .getString(R.string.loading));

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("seller_id", MBUser.getSeller_id());
        map.put("lastid", LastId);
        map.put("pagesize", Constants.PageSize2 + "");
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
                        myNewShowAdapter.FrashAp();
                        LastId = ShowData.get(ShowData.size() - 1).getId();


                        if (ShowData.size() < Constants.PageSize2)
                            fragment_newshow_refrash.setCanLoadMore(false);
                        else
                            fragment_newshow_refrash.setCanLoadMore(true);
                        break;
                    case REFRESHING:
                        fragment_newshow_refrash.setRefreshing(false);
                        if (StrUtils.isEmpty(Data.getHttpResultStr())) {//空的
                            return;
                        }
                        ShowData = JSON.parseArray(Data.getHttpResultStr(), BShow.class);
                        myNewShowAdapter.FrashAp();
                        LastId = ShowData.get(ShowData.size() - 1).getId();
                        if (ShowData.size() < Constants.PageSize2)
                            fragment_newshow_refrash.setCanLoadMore(false);
                        else
                            fragment_newshow_refrash.setCanLoadMore(true);
                        break;
                    case LOADMOREING:
                        fragment_newshow_refrash.setLoading(false);
                        if (StrUtils.isEmpty(Data.getHttpResultStr())) {//空的
                            fragment_newshow_refrash.setCanLoadMore(false);
                            return;
                        }

                        List<BShow> GetData = JSON.parseArray(Data.getHttpResultStr(), BShow.class);

                        ShowData.addAll(GetData);
                        myNewShowAdapter.AddFrashAp(GetData.size());
                        LastId = GetData.get(GetData.size() - 1).getId();
                        if (ShowData.size() < Constants.PageSize2)
                            fragment_newshow_refrash.setCanLoadMore(false);
                        else
                            fragment_newshow_refrash.setCanLoadMore(true);
                        break;
                }
                break;
            case 11:// 删除我的show
                PromptManager.ShowCustomToast(BaseContext, "删除成功");
                myNewShowAdapter.DeleteFrashAp(Data.getHttpLoadType() - 11);
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
                fragment_newshow_refrash.setLoading(false);
                break;
            case REFRESHING:
                fragment_newshow_refrash.setRefreshing(false);
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
    public void OnLoadMore() {
        IData(LastId, LOADMOREING);
    }

    @Override
    public void OnFrash() {
        LastId = "";
        IData(LastId, REFRESHING);
    }


    private class MyNewShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private LayoutInflater layoutInflater;

        public MyNewShowAdapter() {
            this.layoutInflater = LayoutInflater.from(BaseContext);
        }

        /**
         * 刷新数据
         */
        public void FrashAp() {
            this.notifyDataSetChanged();
//            this.notifyItemRangeInserted(0, ShowData.size() - 1);
        }

        //需要在添加给数据源数据后再调用  先刷新在赋值数据源
        public void AddFrashAp(int size) {
            this.notifyItemRangeInserted(getItemCount(), getItemCount() + size);
        }

        //删除
        public void DeleteFrashAp(int postion) {
            ShowData.remove(postion);
            this.notifyItemRemoved(postion);
        }

        @Override
        public int getItemViewType(int position) {
            // //1标识一张图片;;2标识多张图片;;3标识视频
            if (ShowData.get(position).equals("1")) {//视频
                return 3;
            } else if (ShowData.get(position).getImgarr().size() == 1) {//一张图片
                return 1;
            } else {//多张图片
                return 2;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            BaseHodler holder = null;
            switch (viewType) {
                case 1://单张图片
                    view = layoutInflater.inflate(R.layout.item_newshow_onepic, null);
                    holder = new MyOnePicHodler(view);

                    break;
                case 2://多张图片
                    view = layoutInflater.inflate(R.layout.item_newshow_morepic, null);
                    holder = new MyMorePicHodler(view);

                    break;
                case 3://视频
                    view = layoutInflater.inflate(R.layout.item_newshow_vid, null);
                    holder = new MyVidoHodler(view);

                    break;
            }

            return holder;


        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            BaseHodler baseHodler = (BaseHodler) holder;
            StrUtils.SetTxt(baseHodler.comment_newshow_item_date, DateUtils
                    .convertTimeToFormat(ShowData.get(position)
                            .getCreate_time()));
            StrUtils.SetTxt(baseHodler.comment_newshow_item_name, ShowData.get(position).getSellerinfo().getSeller_name());
            StrUtils.SetTxt(baseHodler.comment_newshow_item_inf, ShowData.get(position).getIntro());
            ImageLoaderUtil.Load2(ShowData.get(position).getSellerinfo().getAvatar(), baseHodler.comment_newshow_item_head, R.drawable.error_iv2);
            StrUtils.SetTxt(baseHodler.comment_newshow_item_number,
                    ShowData.get(position).getSendnumber() + "人转发");
            baseHodler.comment_newshow_item_delete
                    .setVisibility(MBUser.getSeller_id().equals(
                            ShowData.get(position).getSeller_id()) ? View.VISIBLE : View.GONE);


            switch (getItemViewType(position)) {
                case 1://一张图
                    MyOnePicHodler OnePicHodler = (MyOnePicHodler) holder;
                    ImageLoaderUtil.Load2(ShowData.get(position).getImgarr().get(0), OnePicHodler.item_newshow_onepic_image, R.drawable.error_iv2);


                    break;
                case 2://多张图
                    MyMorePicHodler MorePicHodler = (MyMorePicHodler) holder;
                    MorePicHodler.item_newshow_gridview.setAdapter(new MyIvdapter(BaseContext,ShowData.get(position).getImgarr()));
                    break;
                case 3://视频播放
                    MyVidoHodler vidoPicHodler = (MyVidoHodler) holder;
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return ShowData.size();
        }


        //一张图片的句柄
        class MyOnePicHodler extends BaseHodler {
            ImageView item_newshow_onepic_image;

            public MyOnePicHodler(View itemView) {
                super(itemView);
                item_newshow_onepic_image = ViewHolder.get(itemView, R.id.item_newshow_onepic_image);
            }
        }

        //多张图片的句柄
        class MyMorePicHodler extends BaseHodler {
            CompleteGridView item_newshow_gridview;

            public MyMorePicHodler(View itemView) {
                super(itemView);
                item_newshow_gridview = (CompleteGridView) itemView.findViewById(R.id.item_newshow_gridview);

            }
        }

        //视频的句柄
        class MyVidoHodler extends BaseHodler {
            ImageView item_newshow_vido_image;
            ImageView item_newshow_vido_control_image;

            public MyVidoHodler(View itemView) {
                super(itemView);
                item_newshow_vido_image = ViewHolder.get(itemView, R.id.item_newshow_vido_image);


            }
        }

        //公用的
        class BaseHodler extends RecyclerView.ViewHolder {
            //公用的上边
            ImageView comment_newshow_item_head;
            TextView comment_newshow_item_name;
            TextView comment_newshow_item_inf;
            //公用的下边
            TextView comment_newshow_item_date;//日期
            TextView comment_newshow_item_delete;//删除
            ImageView comment_newshow_item_gooddetail;//详情
            ImageView comment_newshow_item_share;//分享
            TextView comment_newshow_item_number;//分享数量

            public BaseHodler(View itemView) {
                super(itemView);
                comment_newshow_item_head = ViewHolder.get(itemView, R.id.comment_newshow_item_head);
                comment_newshow_item_name = ViewHolder.get(itemView, R.id.comment_newshow_item_name);
                comment_newshow_item_inf = ViewHolder.get(itemView, R.id.comment_newshow_item_inf);

                comment_newshow_item_date = ViewHolder.get(itemView, R.id.comment_newshow_item_date);
                comment_newshow_item_delete = ViewHolder.get(itemView, R.id.comment_newshow_item_delete);
                comment_newshow_item_gooddetail = ViewHolder.get(itemView, R.id.comment_newshow_item_gooddetail);
                comment_newshow_item_share = ViewHolder.get(itemView, R.id.comment_newshow_item_share);
                comment_newshow_item_number = ViewHolder.get(itemView, R.id.comment_newshow_item_number);

            }
        }


    }


}
