package io.vtown.WeiTangApp.fragment.main;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.adapter.MyIvdapter;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BShowShare;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BLBShow;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BLShow;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BShow;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.download.DownFileUtils;
import io.vtown.WeiTangApp.comment.download.DownFileUtils.DownLoadListener;
import io.vtown.WeiTangApp.comment.util.DateUtils;
import io.vtown.WeiTangApp.comment.util.SdCardUtils;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.CopyTextView;
import io.vtown.WeiTangApp.comment.view.DividerItemDecoration;
import io.vtown.WeiTangApp.comment.view.ShowSelectPic;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.pop.PShowShare;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.ui.comment.AGoodVidoShare;
import io.vtown.WeiTangApp.ui.comment.AVidemplay;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;
import io.vtown.WeiTangApp.ui.title.ABrandDetail;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.center.myshow.ARecyclerOtherShow;
import io.vtown.WeiTangApp.ui.ui.AAddNewShow;

/**
 * Created by datutu on 2016/12/5.
 */

public class FMainNewShow extends FBase implements OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    @BindView(R.id.maintab_new_show_uptxt)
    TextView maintabNewShowUptxt;
    @BindView(R.id.fragment_main_show_add)
    ImageView fragmentMainShowAdd;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    // recycleview***************************
    private LinearLayoutManager LinearShowLayoutManager;
    private StaggeredGridLayoutManager StaggeredGridLayoutManager;
    private MyShowAdapter myShowAdapter;
    // recycleview***************************

    private boolean IShow = true;
    private BUser MyUser;
    /**
     * 当前的最后item的lastid
     */
    private String LastId = "";
    /**
     * 是否存在缓存
     */
    private boolean IsCache;

    /**
     * 保存需要删除的recycleview的位置 当后台交互成功后直接移除
     */
    private int Postion;
    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_main_new_show, null);
        ButterKnife.bind(this, BaseView);
        SetTitleHttpDataLisenter(this);
        MyUser = Spuit.User_Get(BaseContext);
        IBaseView();
        ICacheData();
        IData(LastId, INITIALIZE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (IShow) {
            Log.i("homewave", "显示");
            PromptManager.closeLoading();
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.i("homewave", "隐藏");

            IShow = false;

        } else {
            Log.i("homewave", "显示");
            PromptManager.closeLoading();
            IShow = true;
        }

    }

    /**
     * 基础UI
     */
    private void IBaseView() {
        LinearShowLayoutManager = new LinearLayoutManager(BaseContext);
        swipeTarget.setLayoutManager(LinearShowLayoutManager);
        swipeTarget.addItemDecoration(new DividerItemDecoration(BaseContext, LinearLayoutManager.VERTICAL, Color.TRANSPARENT, 1));
        myShowAdapter = new MyShowAdapter(BaseContext);
        swipeTarget.setAdapter(myShowAdapter);

        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
//        IData(LastId, INITIALIZE);
        swipeTarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.canScrollVertically(-1)) {//顶部
                    maintabNewShowUptxt.setVisibility(View.VISIBLE);
                } else {
                    maintabNewShowUptxt.setVisibility(View.GONE);
                }
            }
        });
    }

    //基础的缓存数据
    private void ICacheData() {
        // 缓存中有show的数据进行处理
        String CachData = Spuit.Show_GetStr(BaseContext);
        if (!StrUtils.isEmpty(CachData)) {
            List<BLShow> data = new ArrayList<BLShow>();
            // 开始解析*************************
            data = JSON.parseArray(CachData, BLShow.class);// ();
            myShowAdapter.FrashData(data);
            IsCache = true;
        } else {// 没有数据就直接显示空白
            IsCache = false;

        }
    }

    /**
     * 的数据请求
     */
    private void IData(String lastId, int initialize) {
        switch (initialize) {
            case INITIALIZE:
                if (!IsCache) {
                    PromptManager.showtextLoading(BaseContext, getResources()
                            .getString(R.string.loading));
                } else {
//                    swipeToLoadLayout.setSwipingToRefreshToDefaultScrollingDuration(1000);
                }
                break;
            case REFRESHING:

                break;
            case LOADMOREING:
                break;
            case LOADHind:
                break;
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("seller_id", MyUser.getSeller_id());
        map.put("lastid", lastId);
        map.put("pagesize", "" + Constants.PageSize2);
        FBGetHttpData(map, Constants.Show_ls, Request.Method.GET, 0, initialize);

    }

    @Override
    public void InitCreate(Bundle d) {

    }

    @Override
    public void getResult(int Code, String Msg, BComment Data) {
        switch (Data.getHttpResultTage()) {
            case 0://获取列表
                switch (Data.getHttpLoadType()) {
                    case INITIALIZE:

                        if (IsCache) {//有缓存时候会进行下拉效果的动画操作 所以获取时候要停止
                            swipeToLoadLayout.setRefreshing(false);
                        }
                        String NetStr = Data.getHttpResultStr();
                        Spuit.Show_SaveStr(BaseContext, NetStr);
                        if (StrUtils.isEmpty(NetStr)) {//如果是空的

                            return;
                        }
                        //开始解析数据********************************************

                        List<BLShow> ShowDatas = JSON.parseArray(NetStr, BLShow.class);
                        myShowAdapter.FrashData(ShowDatas);
                        if (ShowDatas.size() < Constants.PageSize2) {
                            swipeToLoadLayout.setLoadMoreEnabled(false);
                        }
                        if (ShowDatas.size() == Constants.PageSize2) {
                            swipeToLoadLayout.setLoadMoreEnabled(true);
                        }
                        //标记最后位置
                        LastId = ShowDatas.get(ShowDatas.size() - 1).getId();
                        break;
                    case LOADHind://偷偷刷新的 没有任何动画效果 无需处理
                    case REFRESHING://正常刷新有效果
                        if (Data.getHttpLoadType() == REFRESHING)
                            swipeToLoadLayout.setRefreshing(false);
                        String NetStr_Frash = Data.getHttpResultStr();
                        Spuit.Show_SaveStr(BaseContext, NetStr_Frash);
                        if (StrUtils.isEmpty(NetStr_Frash)) {//如果是空的
                            //不需要做处理（）！！！！！！！
                            return;
                        }
                        //开始解析数据********************************************
                        List<BLShow> ShowFrashDatas = JSON.parseArray(NetStr_Frash, BLShow.class);
                        myShowAdapter.FrashData(ShowFrashDatas);
                        if (ShowFrashDatas.size() < Constants.PageSize2) {
                            swipeToLoadLayout.setLoadMoreEnabled(false);
                        }
                        if (ShowFrashDatas.size() == Constants.PageSize2) {
                            swipeToLoadLayout.setLoadMoreEnabled(true);
                        }
                        //标记最后位置
                        LastId = ShowFrashDatas.get(ShowFrashDatas.size() - 1).getId();
                        break;
                    case LOADMOREING:
                        swipeToLoadLayout.setLoadingMore(false);
                        //开始解析数据********************************************
                        String NetStr_LoadMore = Data.getHttpResultStr();
                        if (StrUtils.isEmpty(NetStr_LoadMore)) {//如果是空的
                            //不需要做处理（）！！！！！！！
                            return;
                        }
                        //开始解析数据********************************************
                        List<BLShow> ShowLoadDatas = JSON.parseArray(NetStr_LoadMore, BLShow.class);
                        myShowAdapter.FrashAllData(ShowLoadDatas);
                        if (ShowLoadDatas.size() < Constants.PageSize2) {
                            swipeToLoadLayout.setLoadMoreEnabled(false);
                        }
                        if (ShowLoadDatas.size() == Constants.PageSize2) {
                            swipeToLoadLayout.setLoadMoreEnabled(true);
                        }
                        //标记最后位置
                        LastId = ShowLoadDatas.get(ShowLoadDatas.size() - 1).getId();
                        break;

                }
                break;
            case 11://删除我的Show成功
                myShowAdapter.GetAllData().remove(Postion);
                myShowAdapter.notifyItemRemoved(Postion);
                myShowAdapter.notifyItemRangeChanged(0,myShowAdapter.GetAllData().size()-Postion);
                break;
        }
    }

    @Override
    public void onError(String error, int LoadType) {
        switch (LoadType) {
            case INITIALIZE:
                if (IsCache) {//有缓存时候会进行下拉效果的动画操作 所以获取时候要停止
                    swipeToLoadLayout.setRefreshing(false);
                }
                break;
            case REFRESHING:
                swipeToLoadLayout.setRefreshing(false);
                break;
            case LOADMOREING:
                swipeToLoadLayout.setLoadingMore(false);
                break;
            case LOADHind:
                break;
        }
    }


    @OnClick({R.id.fragment_main_show_add,R.id.maintab_new_show_uptxt})
    public void onClick(View V) {
        switch (V.getId()) {
            case R.id.fragment_main_show_add://添加show
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, AAddNewShow.class));
                break;
            case R.id.maintab_new_show_uptxt:
                swipeTarget.smoothScrollToPosition(0);
                break;
        }
    }

    @Override
    public void onLoadMore() {
        IData(LastId, LOADMOREING);
    }

    @Override
    public void onRefresh() {
        LastId = "";
        IData(LastId, REFRESHING);
    }


    //外层的ap***********************************************************************************************************
    public class MyShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int Show_Grid = 111;
        private static final int Show_Video = 112;
        private static final int Show_Pic = 113;
        private static final int Show_Null = 1141;

        // private int Show_Txt = 114;

        private List<BLShow> datas = new ArrayList<BLShow>();

        private LayoutInflater inflater;
        private Context mContext;

        public MyShowAdapter(Context context) {
            super();
            this.mContext = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = null;
            switch (viewType) {
                case Show_Grid:
                    holder = new MyShowItem(inflater.inflate(R.layout.item_recycler_my_show, parent, false));
                    break;
                case Show_Video:
                    holder = new MyShowVideoItem(inflater.inflate(R.layout.item_recycler_my_show_video, parent, false));
                    break;
                case Show_Pic:
                    holder = new MyShowSinglePicItem(inflater.inflate(R.layout.item_recycle_my_show_single_pic, parent, false));
                    break;
                case Show_Null:
                    holder = new NullItem(inflater.inflate(R.layout.item_show_null, parent, false));
                    break;
            }
            return holder;
        }

        private boolean isMyShow(String seller_id) {
            BUser bUser = Spuit.User_Get(mContext);
            String _seller_id = bUser.getSeller_id();
            if (seller_id.equals(_seller_id)) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (datas.size() == 0) {
                return Show_Null;
            } else if (datas.get(position).getIs_type().equals("1")) {//视频
                return Show_Video;
            } else if (datas.get(position).getImgarr().size() == 1) {//一张图片
                return Show_Pic;
            } else {//多张图片
                return Show_Grid;
            }


        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            BLShow bShow = datas.get(position);
            boolean IsHaveGoodInf = !bShow.getGoods_id().equals("0");//是否包含商品
            boolean IsMyShow = bShow.getSellerinfo().getId().equals(MyUser.getSeller_id());
            BLBShow GoodsData = null;
            if (IsHaveGoodInf) {
                GoodsData = JSON.parseObject(bShow.getGoodinfo(), BLBShow.class);
            }
            switch (getItemViewType(position)) {
                case Show_Grid://多张图片******************************
                    final List<String> Urls = bShow.getImgarr();
                    MyShowItem grid_item = (MyShowItem) holder;
                    ImageLoaderUtil.Load2(bShow.getSellerinfo().getAvatar(), grid_item.my_show_item_icon_grid, R.drawable.error_iv2);
                    StrUtils.SetTxt(grid_item.my_show_item_name_grid, bShow.getSellerinfo().getSeller_name());
                    StrUtils.SetTxt(grid_item.my_show_item_time_grid, DateUtils.convertTimeToFormat(StrUtils.toLong(bShow.getCreate_time())));

                    grid_item.comment_show_share_iv_grid.setOnClickListener(new ShareShowClick(datas.get(position)));
                    grid_item.comment_show_gooddetail_iv_grid.setVisibility(IsHaveGoodInf ? View.VISIBLE : View.GONE);
                    grid_item.comment_show_gooddetail_iv_grid.setOnClickListener(new LookDetailClick(datas.get(position)));
                    //点击头像********
                    grid_item.my_show_item_icon_grid.setOnClickListener(new LookShowClick(datas.get(position)));

                    if (isMyShow(bShow.getSeller_id())) {
                        grid_item.my_show_item_delete_grid.setVisibility(View.VISIBLE);
                        grid_item.my_show_item_delete_grid.setOnClickListener(new DeleteShowClick(datas.get(position),position));
                    } else {
                        grid_item.my_show_item_delete_grid.setVisibility(View.GONE);
                    }
                    //StrUtils.SetTxt(grid_item.my_show_content_title, datas.get(position).getIntro());
                    SetIntro(grid_item.my_show_content_title, datas.get(position).getIntro());
                    StrUtils.SetTxt(grid_item.comment_show_transmit_numb_grid, "有" + (StrUtils.isEmpty(bShow.getSendnumber()) ? "0" : bShow.getSendnumber()) + "人转发");
                    // 赋数据
                    grid_item.item_recycler_my_show_gridview.setAdapter(new MyIvdapter(mContext, Urls));
                    grid_item.item_recycler_my_show_gridview
                            .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0,
                                                        View arg1, int arg2, long arg3) {
                                    Intent mIntent = new Intent(mContext,
                                            AphotoPager.class);
                                    mIntent.putExtra("position", arg2);
                                    mIntent.putExtra("urls",
                                            StrUtils.LsToArray(Urls));
                                    PromptManager.SkipActivity((Activity) mContext,
                                            mIntent);
                                }

                            });

                    if (Urls.size() == 4) {
                        grid_item.item_recycler_my_show_gridview.setNumColumns(2);

                    }
                    if (Urls.size() > 4) {
                        grid_item.item_recycler_my_show_gridview.setNumColumns(3);

                    }
                    break;

                case Show_Video://小视频******************************
                    MyShowVideoItem video_item = (MyShowVideoItem) holder;
                    ImageLoaderUtil.Load2(bShow.getSellerinfo().getAvatar(), video_item.my_show_item_icon_video, R.drawable.error_iv2);
                    StrUtils.SetTxt(video_item.my_show_item_name_video, bShow.getSellerinfo().getSeller_name());

                    StrUtils.SetTxt(video_item.my_show_item_time_video, DateUtils.convertTimeToFormat(StrUtils.toLong(bShow.getCreate_time())));
                    video_item.comment_show_share_iv_video.setOnClickListener(new ShareShowClick(datas.get(position)));
                    video_item.comment_show_gooddetail_iv_video.setVisibility(IsHaveGoodInf ? View.VISIBLE : View.GONE);
                    video_item.comment_show_gooddetail_iv_video.setOnClickListener(new LookDetailClick(datas.get(position)));
                    //点击头像********
                    video_item.my_show_item_icon_video.setOnClickListener(new LookShowClick(datas.get(position)));

                    if (isMyShow(bShow.getSeller_id())) {
                        video_item.my_show_item_delete_video.setVisibility(View.VISIBLE);
                        video_item.my_show_item_delete_video.setOnClickListener(new DeleteShowClick(datas.get(position),position));
                    } else {
                        video_item.my_show_item_delete_video.setVisibility(View.GONE);
                    }

                    // StrUtils.SetTxt(video_item.my_show_content_video_title, datas.get(position).getIntro());

                    SetIntro(video_item.my_show_content_video_title, datas.get(position).getIntro());

                    video_item.my_show_content_video_title.setVisibility(StrUtils.isEmpty(datas.get(position).getIntro()) ? View.GONE : View.VISIBLE);


                    StrUtils.SetTxt(video_item.comment_show_transmit_numb_video, "有" + (StrUtils.isEmpty(bShow.getSendnumber()) ? "0" : bShow.getSendnumber()) + "人转发");
                    try {
                        ImageLoaderUtil.Load2(bShow.getPre_url(),
                                video_item.item_recycler_my_show_vido_image, R.drawable.error_iv1);
                    } catch (Exception e) {
                    }
                    final String vid = bShow.getVid();
                    video_item.item_recycler_my_show_vido_control_image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            PromptManager.SkipActivity(BaseActivity,
                                    new Intent(mContext,
                                            AVidemplay.class).putExtra(
                                            AVidemplay.VidoKey, vid));
                        }
                    });
                    break;

                case Show_Pic://一张图片******************************
                    MyShowSinglePicItem pic_item = (MyShowSinglePicItem) holder;
                    ImageLoaderUtil.Load2(bShow.getSellerinfo().getAvatar(), pic_item.my_show_item_icon_pic, R.drawable.error_iv2);
                    StrUtils.SetTxt(pic_item.my_show_item_name_pic, bShow.getSellerinfo().getSeller_name());

                    StrUtils.SetTxt(pic_item.my_show_item_time_pic, DateUtils.convertTimeToFormat(StrUtils.toLong(bShow.getCreate_time())));
                    pic_item.comment_show_share_iv_pic.setOnClickListener(new ShareShowClick(datas.get(position)));
                    pic_item.comment_show_gooddetail_iv_pic.setVisibility(IsHaveGoodInf ? View.VISIBLE : View.GONE);
                    pic_item.comment_show_gooddetail_iv_pic.setOnClickListener(new LookDetailClick(datas.get(position)));
                    //点击头像********
                    pic_item.my_show_item_icon_pic.setOnClickListener(new LookShowClick(datas.get(position)));

                    if (isMyShow(bShow.getSeller_id())) {
                        pic_item.my_show_item_delete_pic.setVisibility(View.VISIBLE);
                        pic_item.my_show_item_delete_pic.setOnClickListener(new DeleteShowClick(datas.get(position),position));
                    } else {
                        pic_item.my_show_item_delete_pic.setVisibility(View.GONE);
                    }
                    SetIntro(pic_item.my_show_content_single_pic_title, datas.get(position).getIntro());

                    StrUtils.SetTxt(pic_item.comment_show_transmit_numb_pic, "有" + (StrUtils.isEmpty(bShow.getSendnumber()) ? "0" : bShow.getSendnumber()) + "人转发");
                    final List<String> urls = bShow.getImgarr();
                    try {
                        ImageLoaderUtil.Load2(urls.get(0),
                                pic_item.item_recycler_my_show_single_pic, R.drawable.error_iv1);
                    } catch (Exception e) {
                    }
                    pic_item.item_recycler_my_show_single_pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mIntent = new Intent(mContext,
                                    AphotoPager.class);
                            mIntent.putExtra("position", 0);
                            mIntent.putExtra("urls",
                                    StrUtils.LsToArray(urls));
                            PromptManager.SkipActivity(BaseActivity,
                                    mIntent);
                        }
                    });
                    break;


                case Show_Null:


                    break;

            }

        }

        private void SetIntro(TextView view, String intro) {
            if (StrUtils.isEmpty(intro)) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(view, intro);
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }


        public void FrashData(List<BLShow> datas1) {
            this.datas = datas1;
            this.notifyDataSetChanged();
        }

        public void FrashAllData(List<BLShow> datas2) {
            this.datas.addAll(datas2);
            this.notifyDataSetChanged();
        }
        public List<BLShow> GetAllData(  ) {
            return datas;
        }
        //多张图片holder***************************************************
        class MyShowItem extends RecyclerView.ViewHolder {
            private CopyTextView my_show_content_title;
            private CompleteGridView item_recycler_my_show_gridview;
            private View my_show_grid_head;
            private CircleImageView my_show_item_icon_grid;
            private TextView my_show_item_name_grid;
            private TextView my_show_item_time_grid;
            private TextView comment_show_transmit_numb_grid;
            private ImageView comment_show_gooddetail_iv_grid;
            private ImageView comment_show_share_iv_grid;
            private ImageView my_show_item_delete_grid;
            //private TextView my_show_create_time;


            public MyShowItem(View itemView) {
                super(itemView);
                my_show_grid_head = itemView.findViewById(R.id.my_show_grid_head);
                my_show_item_icon_grid = (CircleImageView) my_show_grid_head.findViewById(R.id.my_show_item_icon);
                my_show_item_name_grid = (TextView) my_show_grid_head.findViewById(R.id.my_show_item_name);
                my_show_item_time_grid = (TextView) my_show_grid_head.findViewById(R.id.my_show_item_time);
                my_show_item_delete_grid = (ImageView) my_show_grid_head.findViewById(R.id.my_show_item_delete);
                my_show_content_title = (CopyTextView) itemView.findViewById(R.id.my_show_content_title);
                item_recycler_my_show_gridview = (CompleteGridView) itemView.findViewById(R.id.item_recycler_my_show_gridview);
                View comment_show_operation_grid = itemView.findViewById(R.id.comment_show_operation_grid);
                comment_show_transmit_numb_grid = (TextView) comment_show_operation_grid.findViewById(R.id.comment_show_transmit_numb);
                comment_show_gooddetail_iv_grid = (ImageView) comment_show_operation_grid.findViewById(R.id.comment_show_gooddetail_iv);
                comment_show_share_iv_grid = (ImageView) comment_show_operation_grid.findViewById(R.id.comment_show_share_iv);
                // my_show_create_time = (TextView) itemView.findViewById(R.id.my_show_create_time);

            }


        }

        //一个小视频holder***************************************************
        class MyShowVideoItem extends RecyclerView.ViewHolder {
            private CopyTextView my_show_content_video_title;
            private ImageView item_recycler_my_show_vido_image;
            private ImageView item_recycler_my_show_vido_control_image;
            // private TextView my_show_video_create_time;
            private View my_show_video_head;
            private CircleImageView my_show_item_icon_video;
            private TextView my_show_item_name_video;
            private TextView my_show_item_time_video;
            private TextView comment_show_transmit_numb_video;
            private ImageView comment_show_gooddetail_iv_video;
            private ImageView comment_show_share_iv_video;
            private ImageView my_show_item_delete_video;

            public MyShowVideoItem(View itemView) {
                super(itemView);
                my_show_video_head = itemView.findViewById(R.id.my_show_video_head);
                my_show_item_icon_video = (CircleImageView) my_show_video_head.findViewById(R.id.my_show_item_icon);
                my_show_item_name_video = (TextView) my_show_video_head.findViewById(R.id.my_show_item_name);
                my_show_item_time_video = (TextView) my_show_video_head.findViewById(R.id.my_show_item_time);
                my_show_item_delete_video = (ImageView) my_show_video_head.findViewById(R.id.my_show_item_delete);
                my_show_content_video_title = (CopyTextView) itemView.findViewById(R.id.my_show_content_video_title);
                item_recycler_my_show_vido_image = (ImageView) itemView.findViewById(R.id.item_recycler_my_show_vido_image);
                item_recycler_my_show_vido_control_image = (ImageView) itemView.findViewById(R.id.item_recycler_my_show_vido_control_image);
                View comment_show_operation_video = itemView.findViewById(R.id.comment_show_operation_video);
                comment_show_transmit_numb_video = (TextView) comment_show_operation_video.findViewById(R.id.comment_show_transmit_numb);
                comment_show_gooddetail_iv_video = (ImageView) comment_show_operation_video.findViewById(R.id.comment_show_gooddetail_iv);
                comment_show_share_iv_video = (ImageView) comment_show_operation_video.findViewById(R.id.comment_show_share_iv);
                comment_show_gooddetail_iv_video = (ImageView) comment_show_operation_video.findViewById(R.id.comment_show_gooddetail_iv);
                // my_show_video_create_time = (TextView) itemView.findViewById(R.id.my_show_video_create_time);
            }
        }

        //一张图片holder***************************************************
        class MyShowSinglePicItem extends RecyclerView.ViewHolder {
            private CopyTextView my_show_content_single_pic_title;
            private ImageView item_recycler_my_show_single_pic;
            private View my_show_single_pic_head;
            private CircleImageView my_show_item_icon_pic;
            private TextView my_show_item_name_pic;
            private TextView my_show_item_time_pic;
            private TextView comment_show_transmit_numb_pic;
            private ImageView comment_show_gooddetail_iv_pic;
            private ImageView comment_show_share_iv_pic;
            private ImageView my_show_item_delete_pic;
            // private TextView my_show_single_pic_create_time;

            public MyShowSinglePicItem(View itemView) {
                super(itemView);
                my_show_single_pic_head = itemView.findViewById(R.id.my_show_single_pic_head);
                my_show_item_icon_pic = (CircleImageView) my_show_single_pic_head.findViewById(R.id.my_show_item_icon);
                my_show_item_name_pic = (TextView) my_show_single_pic_head.findViewById(R.id.my_show_item_name);
                my_show_item_time_pic = (TextView) my_show_single_pic_head.findViewById(R.id.my_show_item_time);
                my_show_item_delete_pic = (ImageView) my_show_single_pic_head.findViewById(R.id.my_show_item_delete);
                my_show_content_single_pic_title = (CopyTextView) itemView.findViewById(R.id.my_show_content_single_pic_title);
                item_recycler_my_show_single_pic = (ImageView) itemView.findViewById(R.id.item_recycler_my_show_single_pic);
                View comment_show_operation_pic = itemView.findViewById(R.id.comment_show_operation_pic);
                comment_show_transmit_numb_pic = (TextView) comment_show_operation_pic.findViewById(R.id.comment_show_transmit_numb);
                comment_show_gooddetail_iv_pic = (ImageView) comment_show_operation_pic.findViewById(R.id.comment_show_gooddetail_iv);
                comment_show_share_iv_pic = (ImageView) comment_show_operation_pic.findViewById(R.id.comment_show_share_iv);
                comment_show_gooddetail_iv_pic = (ImageView) comment_show_operation_pic.findViewById(R.id.comment_show_gooddetail_iv);
                //my_show_single_pic_create_time = (TextView) itemView.findViewById(R.id.my_show_single_pic_create_time);
            }
        }

        class NullItem extends RecyclerView.ViewHolder {

            private ImageView iv_no_data;
            private TextView tv_no_show_data;

            public NullItem(View itemView) {
                super(itemView);
                iv_no_data = (ImageView) itemView.findViewById(R.id.iv_no_data);
                tv_no_show_data = (TextView) itemView.findViewById(R.id.tv_no_show_data);
            }
        }

        /**
         * 查看show
         */
        class LookShowClick implements View.OnClickListener {
            BLShow MyShareShow;
            boolean IsBrand;

            public LookShowClick(BLShow myShareShow) {
                MyShareShow = myShareShow;
                IsBrand = myShareShow.getSellerinfo().getIs_brand().equals("1");
            }

            //品牌商点击进入品牌店铺&&普通app用户点击进入其他的Show页面（老的错误逻辑）
            @Override
            public void onClick(View v) {
                if (IsBrand) {   // 品牌店铺发布的show===>跳转到品牌商详情页面
                    BComment mBComment = new BComment(MyShareShow.getSellerinfo().getId(), MyShareShow.getSellerinfo().getSeller_name());
                    PromptManager.SkipActivity(BaseActivity, new Intent(
                            BaseActivity, ABrandDetail.class).putExtra(
                            BaseKey_Bean, mBComment));

                } else { //自营店铺(普通用户发布的show)==》跳转到其他show页面
                    Intent intent = new Intent(BaseActivity,
                            ARecyclerOtherShow.class);
                    intent.putExtra(
                            "abasebeankey",
                            new BComment(
                                    MyShareShow.getSeller_id(),
                                    MyShareShow.getSellerinfo().getCover(),
                                    MyShareShow.getSellerinfo()
                                            .getAvatar(), MyShareShow
                                    .getSellerinfo()
                                    .getSeller_name(), MyShareShow
                                    .getSellerinfo()
                                    .getIs_brand()));
                    PromptManager.SkipActivity(BaseActivity, intent);
                }
            }

        }

        /**
         * 查看详情
         */
        class LookDetailClick implements View.OnClickListener {
            BLShow MyShareShow;

            public LookDetailClick(BLShow myShareShow) {
                MyShareShow = myShareShow;
            }

            @Override
            public void onClick(View v) {
                PromptManager.SkipActivity(BaseActivity,
                        new Intent(BaseActivity, AGoodDetail.class)
                                .putExtra("goodid",
                                        MyShareShow.getGoods_id()));
            }
        }

        /**
         * 分享
         */
        class ShareShowClick implements View.OnClickListener {

            BLShow MyShareShow;
            boolean IsHaveGoods_Share;
            boolean IsVido;

            public ShareShowClick(BLShow myShareShow) {
                MyShareShow = myShareShow;
                IsHaveGoods_Share = !myShareShow.getGoods_id().equals("0");
                IsVido = myShareShow.getIs_type().equals("1");
            }

            @Override
            public void onClick(View v) {
                PShowShare myshare = null;//new PShowShare(BaseContext, BaseActivity, new BNew(), true, true);

                if (IsHaveGoods_Share) { //带商品连接********************
                    BNew bnew = new BNew();
                    BLBShow GoodInf = JSON.parseObject(MyShareShow.getGoodinfo(), BLBShow.class);
                    bnew.setShare_title(GoodInf.getTitle());
                    bnew.setShare_content(GoodInf.getTitle());
                    bnew.setShare_url(MyShareShow.getGoodurl());
                    if (MyShareShow.getIs_type().equals("0")) {//照片直接取出第一张进行分享
                        bnew.setShare_log(MyShareShow.getImgarr().get(0));
                        myshare = new PShowShare(BaseContext, BaseActivity, bnew, true, true);
                    } else {//视频  直接取出视频封面分享
                        bnew.setShare_log(MyShareShow.getPre_url());
                        myshare = new PShowShare(BaseContext, BaseActivity, bnew, false, true);
                    }
                    myshare.SetShareListener(new PShowShare.ShowShareInterListener() {
                        @Override
                        public void GetResultType(int ResultType) {
                            switch (ResultType) {
                                case PShowShare.SHARE_TO_SHOW://Show分享
                                    PromptManager.ShowCustomToast(BaseContext, "SHOW分享");
                                    //   Show分享********************************************************************
                                    PromptManager.ShowCustomToast(BaseContext, "SHOW分享");
                                    if (!IsVido) {// 照片
//                                    //如果是照片  只需要把照片数组和商品id 传到show分享即可
                                        BShowShare MyBShowShare = new BShowShare();
                                        MyBShowShare.setImgarr(MyShareShow.getImgarr());
                                        MyBShowShare.setGoods_id(MyShareShow.getGoods_id());
                                        MyBShowShare.setIntro(StrUtils.NullToStr3(MyShareShow.getIntro()));
                                        PromptManager
                                                .SkipActivity(
                                                        BaseActivity,
                                                        new Intent(BaseActivity, ShowSelectPic.class).putExtra(
                                                                ShowSelectPic.Key_Data,
                                                                MyBShowShare));

                                    } else {// 视频
                                        BShowShare MyVidoBShowShare = new BShowShare();
                                        MyVidoBShowShare.setVido_pre_url(MyShareShow.getPre_url());
                                        MyVidoBShowShare.setVido_Vid(MyShareShow.getVid());
                                        MyVidoBShowShare.setIntro(StrUtils.NullToStr3(MyShareShow.getIntro()));
                                        MyVidoBShowShare.setGoods_id(MyShareShow.getGoods_id());
                                        PromptManager.SkipActivity(
                                                BaseActivity,
                                                new Intent(BaseActivity, AGoodVidoShare.class)
                                                        .putExtra(AGoodVidoShare.Key_VidoFromShow,
                                                                true).putExtra(
                                                        AGoodVidoShare.Key_VidoData,
                                                        MyVidoBShowShare));

                                    }
                                    //   Show分享********************************************************************
                                    break;
                                case PShowShare.SHARE_GOODS_OK://三方分享成功
                                    break;
                                case PShowShare.SHARE_GOODS_ERROR://三方分享失败
                                    break;
                                case PShowShare.SHARE_PIC_VEDIO://九宫格或者视频分享
                                    if (!IsVido)
                                        try {
                                            Pic9ShowBegin(MyShareShow.getImgarr(), StrUtils.isEmpty(MyShareShow.getIntro()) ? "微糖商城#" : MyShareShow.getIntro());
                                        } catch (Exception e) {
                                            PromptManager.closeLoading();
                                            PromptManager.ShowCustomToast(BaseContext, getResources().getString(R.string.jiugongge_error));
                                        }

//                                    PromptManager.ShowCustomToast(BaseContext, "九宫格分享");
                                    break;
                            }
                        }
                    });

//
                    //带商品连接********************
                } else {//不带商品连接*********************
                    BNew bnew = new BNew();
                    BLBShow SellInf = MyShareShow.getSellerinfo();
                    bnew.setShare_title(SellInf.getSeller_name());
                    bnew.setShare_content(StrUtils.isEmpty(MyShareShow.getIntro()) ? SellInf.getSeller_name() : MyShareShow.getIntro());
                    bnew.setShare_log(SellInf.getAvatar());

                    if (MyShareShow.getIs_type().equals("0")) {//照片直接取出第一张进行分享
                        myshare = new PShowShare(BaseContext, BaseActivity, bnew, true, false);
                    } else {//视频  直接取出视频封面分享
                        bnew.setShare_url(Constants.VidoShareHtml + MyShareShow.getVid());
                        myshare = new PShowShare(BaseContext, BaseActivity, bnew, false, false);
                    }
                    myshare.SetShareListener(new PShowShare.ShowShareInterListener() {
                        @Override
                        public void GetResultType(int ResultType) {
                            switch (ResultType) {
                                case PShowShare.SHARE_TO_SHOW://Show分享
                                    //   Show分享********************************************************************
                                    PromptManager.ShowCustomToast(BaseContext, "SHOW分享");
                                    if (!IsVido) {// 照片
//                                    //如果是照片  只需要把照片数组和商品id 传到show分享即可
                                        BShowShare MyBShowShare = new BShowShare();
                                        MyBShowShare.setImgarr(MyShareShow.getImgarr());
                                        MyBShowShare.setGoods_id(MyShareShow.getGoods_id());
                                        MyBShowShare.setIntro(StrUtils.NullToStr3(MyShareShow.getIntro()));
                                        PromptManager
                                                .SkipActivity(
                                                        BaseActivity,
                                                        new Intent(BaseActivity, ShowSelectPic.class).putExtra(
                                                                ShowSelectPic.Key_Data,
                                                                MyBShowShare));

                                    } else {// 视频
                                        BShowShare MyVidoBShowShare = new BShowShare();
                                        MyVidoBShowShare.setVido_pre_url(MyShareShow.getPre_url());
                                        MyVidoBShowShare.setVido_Vid(MyShareShow.getVid());
                                        MyVidoBShowShare.setIntro(StrUtils.NullToStr3(MyShareShow.getIntro()));
                                        MyVidoBShowShare.setGoods_id(MyShareShow.getGoods_id());
                                        PromptManager.SkipActivity(
                                                BaseActivity,
                                                new Intent(BaseActivity, AGoodVidoShare.class)
                                                        .putExtra(AGoodVidoShare.Key_VidoFromShow,
                                                                true).putExtra(
                                                        AGoodVidoShare.Key_VidoData,
                                                        MyVidoBShowShare));

                                    }
                                    //   Show分享********************************************************************
                                    break;
                                case PShowShare.SHARE_GOODS_OK://三方分享成功
                                    break;
                                case PShowShare.SHARE_GOODS_ERROR://三方分享失败
                                    break;
                                case PShowShare.SHARE_PIC_VEDIO://九宫格或者视频分享
                                    //开始九宫格分享！！！！！！！！！！！！
                                    //开始九宫格分享！！！！！！！！！！！！
                                    //开始九宫格分享！！！！！！！！！！！！
//                                    PromptManager.ShowCustomToast(BaseContext, "九宫格分享");
                                    if (!IsVido) {
                                        try {
                                            Pic9ShowBegin(MyShareShow.getImgarr(), StrUtils.isEmpty(MyShareShow.getIntro()) ? "微糖商城#" : MyShareShow.getIntro());
                                        } catch (Exception e) {
                                            PromptManager.closeLoading();
                                            PromptManager.ShowCustomToast(BaseContext, getResources().getString(R.string.jiugongge_error));
                                        }
                                    }
                                    break;
                            }
                        }
                    });

                    //不带商品的连接结束******************
                }

                myshare.showAtLocation(BaseView, Gravity.BOTTOM, 0, 0);
            }
//                BNew bnew = new BNew();
//                bnew.setShare_title(mContext.getResources().getString(R.string.share_app) + "  " + MyShareShow.getGoodinfo().getTitle());
//                bnew.setShare_content(mContext.getResources().getString(R.string.share_app) + "  " + MyShareShow.getGoodinfo().getTitle());
//
//                bnew.setShare_url(MyShareShow.getGoodurl());
//                if (MyShareShow.getIs_type().equals("0")) {//照片直接取出第一张进行分享
//                    bnew.setShare_log(MyShareShow.getImgarr().get(0));
//                } else {//视频  直接取出视频封面分享
//                    bnew.setShare_log(MyShareShow.getPre_url());
//                }
//                PShowShare showShare = null;
//                PShowShare showShare = new PShowShare(mContext, BaseActivity, bnew);
//                showShare.SetShareListener(new PShowShare.ShowShareInterListener() {
//                    @Override
//                    public void GetResultType(int ResultType) {
//                        switch (ResultType) {
//                            case 3:
//                                if (MyShareShow.getIs_type().equals("0")) {// 照片
//                                    //如果是照片  只需要把照片数组和商品id 传到show分享即可
//                                    BShowShare MyBShowShare = new BShowShare();
//                                    MyBShowShare.setImgarr(MyShareShow.getImgarr());
//                                    MyBShowShare.setGoods_id(MyShareShow.getGoods_id());
//                                    MyBShowShare.setIntro(StrUtils.NullToStr3(MyShareShow.getIntro()));
//                                    PromptManager
//                                            .SkipActivity(
//                                                    BaseActivity,
//                                                    new Intent(BaseActivity, ShowSelectPic.class).putExtra(
//                                                            ShowSelectPic.Key_Data,
//                                                            MyBShowShare));
//
//                                } else {// 视频
//                                    BShowShare MyVidoBShowShare = new BShowShare();
//                                    MyVidoBShowShare.setVido_pre_url(MyShareShow.getPre_url());
//                                    MyVidoBShowShare.setVido_Vid(MyShareShow.getVid());
//                                    MyVidoBShowShare.setIntro(StrUtils.NullToStr3(MyShareShow.getIntro()));
//                                    MyVidoBShowShare.setGoods_id(MyShareShow.getGoods_id());
//                                    PromptManager.SkipActivity(
//                                            BaseActivity,
//                                            new Intent(BaseActivity, AGoodVidoShare.class)
//                                                    .putExtra(AGoodVidoShare.Key_VidoFromShow,
//                                                            true).putExtra(
//                                                    AGoodVidoShare.Key_VidoData,
//                                                    MyVidoBShowShare));
//
//                                }
//                                break;
//                        }
//                    }
//                });
//                showShare.showAtLocation(BaseView, Gravity.BOTTOM, 0, 0);
//
//
//            }
        }

        /**
         * 删除show
         */
        class DeleteShowClick implements View.OnClickListener {
            BLShow DeleteBShow;
            int Mypostion;

            public DeleteShowClick(BLShow myShareShow,int Postion) {
                DeleteBShow = myShareShow;
                Mypostion=Postion;
            }

            @Override
            public void onClick(View v) {
                ShowCustomDialog(DeleteBShow.getId(), Mypostion);
            }
        }

        /**
         * 删除show
         *
         * @param
         * @param
         * @param
         * @param
         */
        private void ShowCustomDialog(final String ShowId, final int Postion) {
            final CustomDialog dialog = new CustomDialog(mContext,
                    R.style.mystyle, R.layout.dialog_purchase_cancel, 1, "取消", "删除");
            dialog.show();
            dialog.setTitleText("是否删除该show?");
            dialog.HindTitle2();
            dialog.setCanceledOnTouchOutside(true);
            dialog.setcancelListener(new CustomDialog.oncancelClick() {

                @Override
                public void oncancelClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.setConfirmListener(new CustomDialog.onConfirmClick() {
                @Override
                public void onConfirmCLick(View v) {
                    dialog.dismiss();
                       DeletMyShow(ShowId, Postion);


                }
            });
        }


    }

    int CountNumber = 0;

    public void RecursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

    private void Pic9ShowBegin(final List<String> imgarr, final String Content) {
        CountNumber = 0;
        PromptManager.showLoading(BaseContext);
        File sdCards = Environment.getExternalStorageDirectory();
        final File test = new File(sdCards + "/wtshowpic");
        if (test.exists()) {
            RecursionDeleteFile(test);
        }

        for (int i = 0; i < imgarr.size(); i++) {
            final File downshow = new File(sdCards, "/wtshowpic/" + StrUtils.GetPic(imgarr.get(i), 8));
            final int postion = i;
            DownFileUtils dd = new DownFileUtils();
            dd.SetResult(new DownLoadListener() {
                @Override
                public void DownLoadOk() {
                    Log.i("filetest", "成功" + postion);
                    CountNumber = CountNumber + 1;
                    if (CountNumber == imgarr.size()) {
                        sharemuil(Content, test);
                    }

                }

                @Override
                public void DownLoadError() {
                    Log.i("filetest", "失败" + postion);
                    CountNumber = CountNumber + 1;
                    if (CountNumber == imgarr.size()) {
                        sharemuil(Content, test);
                    }


                }
            });
            dd.xUtilsHttpUtilDonLoadFile(imgarr.get(i), downshow.getPath(), downshow, StrUtils.GetPic(imgarr.get(i), 8));
        }


    }

    private void sharemuil(String content, File... files) {
        PromptManager.closeLoading();
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm",
                "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("image/*");

        ArrayList<Uri> imageUris = new ArrayList<Uri>();

        for (File f : files) {
            File[] filels = f.listFiles();
            for (int i = 0; i < filels.length; i++) {
                File file = filels[i];
                if (checkIsImageFile(file.getPath())) {
                    imageUris.add(Uri.fromFile(file));
                }
            }

        }
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        intent.putExtra("Kdescription", content);
        startActivity(intent);
    }

    private boolean checkIsImageFile(String fName) {
        boolean isImageFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("jpg") || FileEnd.equals("png") || FileEnd.equals("gif")
                || FileEnd.equals("jpeg") || FileEnd.equals("bmp")) {
            isImageFile = true;
        } else {
            isImageFile = false;
        }
        return isImageFile;
    }

    /**
     * 删除我自己的show
     */

    private void DeletMyShow(String ShowId, int postion) {
        PromptManager.showLoading(BaseContext);
        Postion=postion;
        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put("id", ShowId);
        mHashMap.put("seller_id", MyUser.getSeller_id());
        FBGetHttpData(mHashMap, Constants.MyShowDelete, Request.Method.DELETE, 11,
                postion + 11);
    }
}
