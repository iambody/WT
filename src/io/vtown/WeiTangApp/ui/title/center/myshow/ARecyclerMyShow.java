package io.vtown.WeiTangApp.ui.title.center.myshow;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.adapter.MyIvdapter;

import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BShowShare;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BLBShow;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BLShow;
import io.vtown.WeiTangApp.bean.bcomment.new_three.BNewHome;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.download.DownFileUtils;
import io.vtown.WeiTangApp.comment.util.DateUtils;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.CopyTextView;
import io.vtown.WeiTangApp.comment.view.RecyclerCommentItemDecoration;
import io.vtown.WeiTangApp.comment.view.ShowSelectPic;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.custom.EndlessRecyclerOnScrollListener;
import io.vtown.WeiTangApp.comment.view.custom.HeaderViewRecyclerAdapter;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.pop.PShare;
import io.vtown.WeiTangApp.comment.view.pop.PShowShare;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.AGoodVidoShare;
import io.vtown.WeiTangApp.ui.comment.AVidemplay;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.ui.AAddNewShow;

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
    private View mRootView;
    private View comment_myshow_no__lay;
    private int DeletePostion;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_recycler_my_show);
        this.mRootView = LayoutInflater.from(BaseContext).inflate(R.layout.activity_recycler_my_show, null);
        SetTitleHttpDataLisenter(this);
        String seller_id = getIntent().getStringExtra("seller_id");
        if (StrUtils.isEmpty(seller_id)) {
            return;
        } else {
            _seller_id = seller_id;
        }
        user_get = Spuit.User_Get(BaseContext);
//        bShop = Spuit.Shop_Get(BaseContext);
        MBNewHome = JSON.parseObject(CacheUtil.NewHome_Get(BaseContext), BNewHome.class);
        IView();
        ICache();
        IData(lastid, LOAD_INITIALIZE);
    }

    private void IView() {
        comment_myshow_no__lay = findViewById(R.id.comment_myshow_no__lay);
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
        myShowAdapter = new ShowRecyclerAdapter(BaseContext, screenWidth, mRootView, this, false);

        MyHeadAdapter = new HeaderViewRecyclerAdapter(myShowAdapter);
        MyHeadAdapter.addHeaderView(HeadView);
        recyclerview_my_show.setAdapter(MyHeadAdapter);//myShowAdapter
        recyclerview_my_show.addOnScrollListener(new EndlessRecyclerOnScrollListener(MLinearLayoutManager) {
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
        recyclerview_my_show.smoothScrollToPosition(0);
    }


    private void ICache() {
        String CacheStr = CacheUtil.MyShow_Get(BaseContext);
        if (StrUtils.isEmpty(CacheStr)) {
            PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.xlistview_header_hint_loading));
            return;
        }

        // 开始显示缓存数据

        List<BLShow> datas = new ArrayList<BLShow>();

        datas = JSON.parseArray(CacheStr, BLShow.class);

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

        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", _seller_id);
        map.put("lastid", lastid);
        map.put("pagesize", Constants.PageSize + "");
        FBGetHttpData(map, Constants.Get_My_Show, Request.Method.GET, 0, loadtype);
    }

    /**
     * 删除我自己的show
     */

    public void DeletMyShow(String ShowId, int Postion) {
        PromptManager.showLoading(BaseContext);
        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put("id", ShowId);
        mHashMap.put("seller_id", Spuit.User_Get(BaseContext).getSeller_id());
        FBGetHttpData(mHashMap, Constants.MyShowDelete, Request.Method.DELETE, 91,
                LOAD_INITIALIZE);

    }

    @Override
    protected void onResume() {
        PromptManager.closeLoading();
        super.onResume();
    }

    @Override
    protected void InitTile() {
        SetTitleTxt("我的SHOW");
        SetRightIv(R.drawable.ic_jiahao_add);
        right_iv.setOnClickListener(this);
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        switch (Data.getHttpLoadType()) {
            case LOAD_INITIALIZE:
                if (91 == Data.getHttpResultTage()) {// 删除
                    PromptManager.ShowCustomToast(BaseContext, "删除成功");
                    if (myShowAdapter.GetDatas().size() == 1) {
                        IDataView(recyclerview_my_show, comment_myshow_no__lay, NOVIEW_ERROR);
                        ShowErrorIv(R.drawable.error_show);
                        ShowErrorCanLoad(getResources().getString(R.string.younoshowss));
                        return;
                    }
//                    IData(lastid, LOAD_INITIALIZE);
                    myShowAdapter.GetDatas().remove(DeletePostion);
                    myShowAdapter.notifyItemRemoved(DeletePostion);
                    myShowAdapter.notifyItemRangeRemoved(0, myShowAdapter.GetDatas().size() - DeletePostion);
                    lastid = myShowAdapter.GetDatas().get(myShowAdapter.GetDatas().size() - 1).getId();
                    return;
                }

                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    CacheUtil.MyShow_Save(BaseContext, "");
                    myShowAdapter.FrashData(new ArrayList<BLShow>());
                    IDataView(recyclerview_my_show, comment_myshow_no__lay, NOVIEW_ERROR);
                    ShowErrorIv(R.drawable.error_show);
                    ShowErrorCanLoad(getResources().getString(R.string.younoshow));
                    return;
                }
                CacheUtil.MyShow_Save(BaseContext, Data.getHttpResultStr());
                List<BLShow> datas = new ArrayList<BLShow>();
                datas = JSON.parseArray(Data.getHttpResultStr(), BLShow.class);
                lastid = datas.get(datas.size() - 1).getId();
                myShowAdapter.FrashData(datas);
                IsCanLoadMore = datas.size() == Constants.PageSize ? true : false;

                break;

            case LOAD_LOADMOREING:
                HindLoadMore();
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    IsCanLoadMore = false;
                    return;
                }
                List<BLShow> datass = new ArrayList<BLShow>();
                datass = JSON.parseArray(Data.getHttpResultStr(), BLShow.class);
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
        if (V.getId() == R.id.right_iv) {
            //权限判定！！！！！！！！！！！！！！
            if (CheckLimite()) return;
            //权限判定！！！！！！！！！！！！！！！！！！！
            PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, AAddNewShow.class));
        }
    }

    @Override
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }

    //Adapter***************************************************************
    public class ShowRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int Show_Grid = 111;
        private static final int Show_Video = 112;
        private static final int Show_Pic = 113;
        private static final int Show_Null = 1141;

        private static final int HeadView = 101;//增加投
        private static final int FootView = 102;//增加底部加载更多
        // private int Show_Txt = 114;

        private List<BLShow> datas = new ArrayList<BLShow>();

        private LayoutInflater inflater;

//    private int mScreenWidth;

        private Context mContext;

        private boolean IsShowDetaiDate;//是否显示详细的日期

        private View BaseView;//转发时候需要show的位置的baseview

        private Activity SActivity;
        //头像是否可点击
        private boolean mClickHeaderIv;
        //对外暴露加载更多接口

        //是否可以加载更多
//    private boolean IsCanLoadMore = false;

        public ShowRecyclerAdapter(Context context, int screenWidth, View Mybaseview, Activity activity, boolean clickHeaderIv) {
            super();
            this.mContext = context;
//        this.mScreenWidth = screenWidth;
            this.BaseView = Mybaseview;
            this.SActivity = activity;
            this.mClickHeaderIv = clickHeaderIv;
            this.inflater = LayoutInflater.from(context);
        }

        public ShowRecyclerAdapter(Context context, int screenWidth, View Mybaseview, boolean isShowDetaiDate, Activity mActivity) {
            super();
            this.mContext = context;
//        this.mScreenWidth = screenWidth;
            this.BaseView = Mybaseview;
            this.IsShowDetaiDate = isShowDetaiDate;

            this.SActivity = mActivity;
            this.inflater = LayoutInflater.from(context);

        }

        public List<BLShow> GetDatas() {
            return datas;
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
            boolean IsHaveGoodInf = !bShow.getGoods_id().equals("0");
            switch (getItemViewType(position)) {
                case Show_Grid:
                    final List<String> Urls = bShow.getImgarr();
                    MyShowItem grid_item = (MyShowItem) holder;
                    ImageLoaderUtil.Load2(bShow.getSellerinfo().getAvatar(), grid_item.my_show_item_icon_grid, R.drawable.error_iv2);
                    StrUtils.SetTxt(grid_item.my_show_item_name_grid, bShow.getSellerinfo().getSeller_name());
                    StrUtils.SetTxt(grid_item.my_show_item_time_grid, DateUtils.convertTimeToFormat(StrUtils.toLong(bShow.getCreate_time())));
                    grid_item.comment_show_share_iv_grid.setVisibility(View.VISIBLE);
                    grid_item.comment_show_gooddetail_iv_grid.setVisibility(IsHaveGoodInf ? View.VISIBLE : View.GONE);
                    grid_item.comment_show_share_iv_grid.setOnClickListener(new ShareShowClick(datas.get(position)));
                    grid_item.comment_show_gooddetail_iv_grid.setOnClickListener(new LookDetailClick(datas.get(position)));
//                if(mClickHeaderIv){
//                    grid_item.my_show_item_icon_grid.setClickable(true);
//                    grid_item.my_show_item_icon_grid.setOnClickListener(new LookShowClick(datas.get(position)));
//                }else{
                    grid_item.my_show_item_icon_grid.setClickable(false);
//                }

                    if (isMyShow(bShow.getSeller_id())) {
                        grid_item.my_show_item_delete_grid.setVisibility(View.VISIBLE);
                        grid_item.my_show_item_delete_grid.setOnClickListener(new DeleteShowClick(datas.get(position), position));
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

                case Show_Video:
                    MyShowVideoItem video_item = (MyShowVideoItem) holder;
                    ImageLoaderUtil.Load2(bShow.getSellerinfo().getAvatar(), video_item.my_show_item_icon_video, R.drawable.error_iv2);
                    StrUtils.SetTxt(video_item.my_show_item_name_video, bShow.getSellerinfo().getSeller_name());

                    StrUtils.SetTxt(video_item.my_show_item_time_video, DateUtils.convertTimeToFormat(StrUtils.toLong(bShow.getCreate_time())));
                    video_item.comment_show_share_iv_video.setVisibility(View.VISIBLE);
                    video_item.comment_show_gooddetail_iv_video.setVisibility(IsHaveGoodInf ? View.VISIBLE : View.GONE);
                    video_item.comment_show_share_iv_video.setOnClickListener(new ShareShowClick(datas.get(position)));
                    video_item.comment_show_gooddetail_iv_video.setOnClickListener(new LookDetailClick(datas.get(position)));

//                if(mClickHeaderIv){
//                    video_item.my_show_item_icon_video.setClickable(true);
//                    video_item.my_show_item_icon_video.setOnClickListener(new LookShowClick(datas.get(position)));
//                }else{
                    video_item.my_show_item_icon_video.setClickable(false);
//                }

                    if (isMyShow(bShow.getSeller_id())) {
                        video_item.my_show_item_delete_video.setVisibility(View.VISIBLE);
                        video_item.my_show_item_delete_video.setOnClickListener(new DeleteShowClick(datas.get(position), position));
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

                            PromptManager.SkipActivity(SActivity,
                                    new Intent(mContext,
                                            AVidemplay.class).putExtra(
                                            AVidemplay.VidoKey, vid));
                        }
                    });
                    break;

                case Show_Pic:
                    MyShowSinglePicItem pic_item = (MyShowSinglePicItem) holder;
                    ImageLoaderUtil.Load2(bShow.getSellerinfo().getAvatar(), pic_item.my_show_item_icon_pic, R.drawable.error_iv2);
                    StrUtils.SetTxt(pic_item.my_show_item_name_pic, bShow.getSellerinfo().getSeller_name());

                    StrUtils.SetTxt(pic_item.my_show_item_time_pic, DateUtils.convertTimeToFormat(StrUtils.toLong(bShow.getCreate_time())));
                    pic_item.comment_show_share_iv_pic.setVisibility(View.VISIBLE);
                    pic_item.comment_show_gooddetail_iv_pic.setVisibility(IsHaveGoodInf ? View.VISIBLE : View.GONE);
                    pic_item.comment_show_share_iv_pic.setOnClickListener(new ShareShowClick(datas.get(position)));
                    pic_item.comment_show_gooddetail_iv_pic.setOnClickListener(new LookDetailClick(datas.get(position)));

//                if(mClickHeaderIv){
//                    pic_item.my_show_item_icon_pic.setClickable(true);
//                    pic_item.my_show_item_icon_pic.setOnClickListener(new LookShowClick(datas.get(position)));
//                }else{
                    pic_item.my_show_item_icon_pic.setClickable(false);
//                }

                    if (isMyShow(bShow.getSeller_id())) {
                        pic_item.my_show_item_delete_pic.setVisibility(View.VISIBLE);
                        pic_item.my_show_item_delete_pic.setOnClickListener(new DeleteShowClick(datas.get(position), position));
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
                            PromptManager.SkipActivity(SActivity,
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

            public LookShowClick(BLShow myShareShow) {
                MyShareShow = myShareShow;
            }

            @Override
            public void onClick(View v) {
                {
                    Intent intent = new Intent(SActivity,
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
                    PromptManager.SkipActivity(SActivity, intent);
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
                PromptManager.SkipActivity(SActivity,
                        new Intent(SActivity, AGoodDetail.class)
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
                //判断发show的权限！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
                //权限888888888888888888888
                //如果未绑定或者已绑定未激活的用户分享权限的判断***************************

                //判断发show的权限！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
                PShowShare myshare = null;//new PShowShare(BaseContext, BaseActivity, new BNew(), true, true);

                if (IsHaveGoods_Share) {//带商品连接********************
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
                        bnew.setShare_vido_url(Constants.VidoShareHtml + MyShareShow.getVid());
                        myshare = new PShowShare(BaseContext, BaseActivity, bnew, false, true);
                    }
                    myshare.SetShareListener(new PShowShare.ShowShareInterListener() {
                        @Override
                        public void GetResultType(int ResultType) {
                            switch (ResultType) {
                                case PShowShare.SHARE_TO_SHOW://Show分享
//                                    PromptManager.ShowCustomToast(BaseContext, "SHOW分享");
                                    //   Show分享********************************************************************
//                                    PromptManager.ShowCustomToast(BaseContext, "SHOW分享");
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
                                    Show_Award();
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
                        bnew.setShare_vido_url(Constants.VidoShareHtml + MyShareShow.getVid());
                        myshare = new PShowShare(BaseContext, BaseActivity, bnew, false, false);
                    }
                    myshare.SetShareListener(new PShowShare.ShowShareInterListener() {
                        @Override
                        public void GetResultType(int ResultType) {
                            switch (ResultType) {
                                case PShowShare.SHARE_TO_SHOW://Show分享
                                    //   Show分享********************************************************************
//                                    PromptManager.ShowCustomToast(BaseContext, "SHOW分享");
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
                                    Show_Award();
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

        int CountNumber;

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
                dd.SetResult(new DownFileUtils.DownLoadListener() {
                    @Override
                    public void DownLoadOk(String path) {
                        Log.i("filetest", "成功" + postion);
                        CountNumber = CountNumber + 1;
                        if (CountNumber == imgarr.size()) {
                            sharemuil(Content, test);
                        }

                    }

                    @Override
                    public void DownLoadError(String msg) {
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

        private void sharemuil(String content, File... files) {
            Show_Award();
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

        class DeleteShowClick implements View.OnClickListener {
            BLShow DeleteBShow;
            int MYDELETpOSTION;

            public DeleteShowClick(BLShow myShareShow, int Postion) {
                DeleteBShow = myShareShow;
                MYDELETpOSTION = Postion;
            }

            @Override
            public void onClick(View v) {
                ShowCustomDialog(DeleteBShow.getId(), MYDELETpOSTION);
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
                    DeletePostion = Postion;
                    DeletMyShow(ShowId, Postion);


                }
            });
        }


    }


    //adapter
}
