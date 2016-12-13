package io.vtown.WeiTangApp.ui.title.center.myshow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.adapter.MyIvdapter;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BShowShare;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BLBShow;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BLShow;
import io.vtown.WeiTangApp.bean.bcomment.new_three.BActive;
import io.vtown.WeiTangApp.bean.bcomment.new_three.BNewHome;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DateUtils;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.CopyTextView;
import io.vtown.WeiTangApp.comment.view.DividerItemDecoration;
import io.vtown.WeiTangApp.comment.view.ShowSelectPic;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.pop.PShowShare;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.AGoodVidoShare;
import io.vtown.WeiTangApp.ui.comment.AVidemplay;
import io.vtown.WeiTangApp.ui.comment.AWeb;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;
import io.vtown.WeiTangApp.ui.title.ABrandDetail;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.loginregist.bindcode_three.ANewBindCode;
import io.vtown.WeiTangApp.ui.title.zhuanqu.AZhuanQu;

/**
 * Created by datutu on 2016/12/13.
 */

public class ANewGoodShow extends ATitleBase implements OnLoadMoreListener, OnRefreshListener {
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    // 基view
    private View BaseView;
    // 记录最后一条show的id提供分页加载
    private String LastId = "";

    private BUser mBUser;
    // 无数据时候的view
    private View NoDataView;

    // 详情页带过来的数据
    private String Goods_Sid;
    public final static String Tage_GoodSid = "gooddetaisid";

    private GoodsShowAdapter MyGoodsShowAdapter;
    private LinearLayoutManager LinearShowLayoutManager;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_newgoods_show);
        BaseView=LayoutInflater.from(this).inflate(R.layout.activity_newgoods_show,null);
        ButterKnife.bind(this);
        mBUser = Spuit.User_Get(BaseActivity);
        IBund();
        IBase();
        IData(Goods_Sid, LOAD_INITIALIZE);
    }

    private void IBase() {
        LinearShowLayoutManager = new LinearLayoutManager(BaseContext);
        swipeTarget.setLayoutManager(LinearShowLayoutManager);
        swipeTarget.addItemDecoration(new DividerItemDecoration(BaseContext, LinearLayoutManager.VERTICAL, Color.TRANSPARENT, 1));
        MyGoodsShowAdapter = new GoodsShowAdapter(BaseContext);
        NoDataView = findViewById(R.id.goodsnewhow_nodata_lay);
        swipeTarget.setAdapter(MyGoodsShowAdapter);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);

    }

    private void IBund() {
        if (null == getIntent().getExtras()
                || !getIntent().getExtras().containsKey(Tage_GoodSid))
            BaseActivity.finish();

        Goods_Sid = getIntent().getStringExtra(Tage_GoodSid);
        SetTitleHttpDataLisenter(this);
    }



    @Override
    protected void InitTile() {
        SetTitleTxt("Show");
    }

    private void IData(String goods_sid, int loadtype) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("goods_sid", goods_sid);
        hashMap.put("lastid", LastId);
        hashMap.put("pagesize", Constants.PageSize + "");
        hashMap.put("seller_id", mBUser.getSeller_id());
        FBGetHttpData(hashMap, Constants.GoodsShow, Request.Method.GET, 0, loadtype);

    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        switch (Data.getHttpResultTage()) {
            case 0:// 获取列表数据*************************
                switch (Data.getHttpLoadType()) {// 获取show列表的
                    case LOAD_INITIALIZE:
                    case LOAD_REFRESHING:
                        if(LOAD_REFRESHING==Data.getHttpLoadType())swipeToLoadLayout.setRefreshing(false);
                        if (StrUtils.isEmpty(Data.getHttpResultStr())) {// 如果是空的
                            // TODO没数据需要怎么处理？
                            if (LOAD_REFRESHING == LOAD_INITIALIZE
                                    || LOAD_REFRESHING == LOAD_REFRESHING) {// 非加载更多
                                IDataView(swipeToLoadLayout, NoDataView,
                                        NOVIEW_ERROR);
                                ShowErrorCanLoad("暂无相关Show");
                                ShowErrorIv(R.drawable.error_show);
                            }
                            break;
                        }
                        IDataView(swipeToLoadLayout, NoDataView, NOVIEW_RIGHT);
                        // 解析数据**********************
                        List<BLShow> blComments = JSON.parseArray(
                                Data.getHttpResultStr(), BLShow.class);

                        if (blComments.size() == 0) {
                            DataError(Msg, Data.getHttpLoadType());
                        }
                        LastId = blComments.get(blComments.size() - 1).getId();

                        if (blComments.size() < 10) {
                            swipeToLoadLayout.setLoadMoreEnabled(false);
                        } else {
                            swipeToLoadLayout.setLoadMoreEnabled(true);
                        }

                        if (Data.getHttpLoadType() == LOAD_REFRESHING) {
                            swipeToLoadLayout.setRefreshing(false);
                        }
                        MyGoodsShowAdapter.FrashData(blComments);
                        break;
                    case LOAD_LOADMOREING:
                        swipeToLoadLayout.setLoadingMore(false);
                        if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                            PromptManager.ShowCustomToast(BaseContext, "暂无更多show");
                            return;
                        }
                        List<BLShow> blCommentsss = JSON.parseArray(
                                Data.getHttpResultStr(), BLShow.class);

                        if (blCommentsss.size() == 0) {
                            DataError(Msg, Data.getHttpLoadType());
                        }
                        LastId = blCommentsss.get(blCommentsss.size() - 1).getId();

                        if (blCommentsss.size() < 10) {
                            swipeToLoadLayout.setLoadMoreEnabled(false);
                        } else {
                            swipeToLoadLayout.setLoadMoreEnabled(true);
                        }
                        MyGoodsShowAdapter.AddFrashData(blCommentsss);
                        break;
                    default:
                        break;
                }

                break;

            default:
                break;
        }


    }

    @Override
    protected void DataError(String error, int LoadType) {
        switch (LoadType) {
            case LOAD_INITIALIZE:
                IDataView(swipeToLoadLayout, NoDataView, NOVIEW_ERROR);
                ShowErrorCanLoad("暂无相关Show");
                ShowErrorIv(R.drawable.error_show);
                break;
            case LOAD_REFRESHING:
                swipeToLoadLayout.setRefreshing(false);
                IDataView(swipeToLoadLayout, NoDataView, NOVIEW_ERROR);
                break;
            case LOAD_LOADMOREING:
                swipeToLoadLayout.setLoadingMore(false);
                break;
            default:
                break;
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

    }

    @Override
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }

    @Override
    public void onLoadMore() {
        IData(Goods_Sid, LOAD_LOADMOREING);
    }

    @Override
    public void onRefresh() {
        LastId = "";
        IData(Goods_Sid, LOAD_REFRESHING);
    }

//AP*****************


    public class GoodsShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int Show_Grid = 111;
        private static final int Show_Video = 112;
        private static final int Show_Pic = 113;
        private static final int Show_Null = 1141;

        // private int Show_Txt = 114;

        private List<BLShow> datas = new ArrayList<BLShow>();

        private LayoutInflater inflater;
        private Context mContext;

        public GoodsShowAdapter(Context context) {
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
            boolean IsMyShow = bShow.getSellerinfo().getId().equals(mBUser.getSeller_id());
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
                    grid_item.comment_show_gooddetail_iv_grid.setVisibility(  View.GONE);
                    grid_item.comment_show_share_iv_grid.setVisibility(  View.GONE);
                    grid_item.comment_show_gooddetail_iv_grid.setOnClickListener(new LookDetailClick(datas.get(position)));
                    //点击头像********
                    grid_item.my_show_item_icon_grid.setClickable(true);
                    grid_item.my_show_item_icon_grid.setOnClickListener(new LookShowClick(datas.get(position)));

//                    if (isMyShow(bShow.getSeller_id())) {
//                        grid_item.my_show_item_delete_grid.setVisibility(View.VISIBLE);
//                        grid_item.my_show_item_delete_grid.setOnClickListener(new  DeleteShowClick(datas.get(position)));
//                    } else {
                    grid_item.my_show_item_delete_grid.setVisibility(View.GONE);
//                    }
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
                    video_item.comment_show_gooddetail_iv_video.setVisibility(  View.GONE);
                    video_item.comment_show_share_iv_video.setVisibility(  View.GONE);
                    video_item.comment_show_gooddetail_iv_video.setOnClickListener(new LookDetailClick(datas.get(position)));
                    //点击头像********
                    video_item.my_show_item_icon_video.setClickable(true);
                    video_item.my_show_item_icon_video.setOnClickListener(new LookShowClick(datas.get(position)));

//                    if (isMyShow(bShow.getSeller_id())) {
//                        video_item.my_show_item_delete_video.setVisibility(View.VISIBLE);
//                        video_item.my_show_item_delete_video.setOnClickListener(new  DeleteShowClick(datas.get(position)));
//                    } else {
                    video_item.my_show_item_delete_video.setVisibility(View.GONE);
//                    }

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
                    pic_item.comment_show_gooddetail_iv_pic.setVisibility(  View.GONE);
                    pic_item.comment_show_share_iv_pic.setVisibility(  View.GONE);
                    pic_item.comment_show_gooddetail_iv_pic.setOnClickListener(new LookDetailClick(datas.get(position)));
                    //点击头像********
                    pic_item.my_show_item_icon_pic.setClickable(true);
                    pic_item.my_show_item_icon_pic.setOnClickListener(new LookShowClick(datas.get(position)));

//                    if (isMyShow(bShow.getSeller_id())) {
//                        pic_item.my_show_item_delete_pic.setVisibility(View.VISIBLE);
//                        pic_item.my_show_item_delete_pic.setOnClickListener(new  DeleteShowClick(datas.get(position)));
//                    } else {
                    pic_item.my_show_item_delete_pic.setVisibility(View.GONE);
//                    }
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

        public void AddFrashData(List<BLShow> datas1) {
            this.datas.addAll(datas1);
            this.notifyDataSetChanged();
        }

        public void FrashAllData(List<BLShow> datas2) {
            this.datas.addAll(datas2);
            this.notifyDataSetChanged();
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
//                    Intent intent = new Intent(BaseActivity,
//                            ARecyclerOtherShow.class);
//                    intent.putExtra(
//                            "abasebeankey",
//                            new BComment(
//                                    MyShareShow.getSeller_id(),
//                                    MyShareShow.getSellerinfo().getCover(),
//                                    MyShareShow.getSellerinfo()
//                                            .getAvatar(), MyShareShow
//                                    .getSellerinfo()
//                                    .getSeller_name(), MyShareShow
//                                    .getSellerinfo()
//                                    .getIs_brand()));
//                    PromptManager.SkipActivity(BaseActivity, intent);
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
                //权限

                //判断是否符合分享条件*************


                if (!Spuit.IsHaveBind_Get(BaseContext) && !Spuit.IsHaveBind_JiQi_Get(BaseContext)) {//未绑定邀请码
                    ShowCustomDialog(getResources().getString(R.string.no_bind_code),
                            getResources().getString(R.string.quxiao), getResources().getString(R.string.bind_code),
                            new IDialogResult() {
                                @Override
                                public void RightResult() {
                                    PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                                            ANewBindCode.class));
                                }

                                @Override
                                public void LeftResult() {
                                }
                            });
                    return;
                }
                if (!Spuit.IsHaveActive_Get(BaseContext)) {//绑定邀请码未激活
                    ShowCustomDialog(JSON.parseObject(CacheUtil.NewHome_Get(BaseContext), BNewHome.class).getIntegral() < 10000 ? getResources().getString(R.string.to_Jihuo_toqiandao1) : getResources().getString(R.string.to_Jihuo_toqiandao2),
                            getResources().getString(R.string.look_guize), getResources().getString(R.string.to_jihuo1),
                            new IDialogResult() {
                                @Override
                                public void RightResult() {
                                    BActive maxtive = Spuit.Jihuo_get(BaseContext);
                                    BComment mBCommentss = new BComment(maxtive.getActivityid(),
                                            maxtive.getActivitytitle());
                                    PromptManager.SkipActivity(BaseActivity, new Intent(
                                            BaseContext, AZhuanQu.class).putExtra(BaseKey_Bean,
                                            mBCommentss));
                                }

                                @Override
                                public void LeftResult() {
                                    PromptManager.SkipActivity(BaseActivity, new Intent(
                                            BaseActivity, AWeb.class).putExtra(
                                            AWeb.Key_Bean,
                                            new BComment(Constants.Homew_JiFen, getResources().getString(R.string.jifenguize))));

                                }
                            });

                    return;
                }


                //判断是否符合分享条件*************

                //权限


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
//                                            Pic9ShowBegin(MyShareShow.getImgarr(), StrUtils.isEmpty(MyShareShow.getIntro()) ? "微糖商城#" : MyShareShow.getIntro());
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
//                                            Pic9ShowBegin(MyShareShow.getImgarr(), StrUtils.isEmpty(MyShareShow.getIntro()) ? "微糖商城#" : MyShareShow.getIntro());
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

            public DeleteShowClick(BLShow myShareShow) {
                DeleteBShow = myShareShow;
            }

            @Override
            public void onClick(View v) {
                ShowDDDCustomDialog(DeleteBShow.getId(), DeleteBShow.getSellerinfo().getId());
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
        private void ShowDDDCustomDialog(final String ShowId, final String seller_id) {
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
//                       DeletMyShow(ShowId, seller_id);


                }
            });
        }


    }


}
