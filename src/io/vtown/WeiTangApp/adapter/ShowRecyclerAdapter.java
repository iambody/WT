package io.vtown.WeiTangApp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BShowShare;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BShow;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DateUtils;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.CopyTextView;
import io.vtown.WeiTangApp.comment.view.ShowSelectPic;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.pop.PShowShare;
import io.vtown.WeiTangApp.ui.comment.AGoodVidoShare;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.center.myshow.AOtherShow;
import io.vtown.WeiTangApp.ui.title.center.myshow.ARecyclerMyShow;
import io.vtown.WeiTangApp.ui.title.center.myshow.ARecyclerOtherShow;

/**
 * Created by Yihuihua on 2016/9/27.
 */

public class ShowRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int Show_Grid = 111;
    private static final int Show_Video = 112;
    private static final int Show_Pic = 113;

    private static final int HeadView = 101;//增加投
    private static final int FootView = 102;//增加底部加载更多
    // private int Show_Txt = 114;

    private List<BShow> datas = new ArrayList<BShow>();

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

    public ShowRecyclerAdapter(Context context, int screenWidth,View Mybaseview,Activity activity,boolean clickHeaderIv) {
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

        if (datas.get(position).getIs_type().equals("1")) {//视频
            return Show_Video;
        } else if (datas.get(position).getImgarr().size() == 1) {//一张图片
            return Show_Pic;
        } else {//多张图片
            return Show_Grid;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        BShow bShow = datas.get(position);
        switch (getItemViewType(position)) {
            case Show_Grid:
                final List<String> Urls = bShow.getImgarr();
                MyShowItem grid_item = (MyShowItem) holder;
                ImageLoaderUtil.Load2(bShow.getSellerinfo().getAvatar(), grid_item.my_show_item_icon_grid, R.drawable.error_iv2);
                StrUtils.SetTxt(grid_item.my_show_item_name_grid, bShow.getSellerinfo().getSeller_name());
                StrUtils.SetTxt(grid_item.my_show_item_time_grid, IsShowDetaiDate ? StrUtils.longtostr(bShow.getCreate_time()) : DateUtils.convertTimeToFormat(bShow.getCreate_time()));

                grid_item.comment_show_share_iv_grid.setOnClickListener(new ShareShowClick(datas.get(position)));
                grid_item.comment_show_gooddetail_iv_grid.setOnClickListener(new LookDetailClick(datas.get(position)));
                if(mClickHeaderIv){
                    grid_item.my_show_item_icon_grid.setClickable(true);
                    grid_item.my_show_item_icon_grid.setOnClickListener(new LookShowClick(datas.get(position)));
                }else{
                    grid_item.my_show_item_icon_grid.setClickable(false);
                }

                if (isMyShow(bShow.getSeller_id())) {
                    grid_item.my_show_item_delete_grid.setVisibility(View.VISIBLE);
                    grid_item.my_show_item_delete_grid.setOnClickListener(new DeleteShowClick(datas.get(position)));
                } else {
                    grid_item.my_show_item_delete_grid.setVisibility(View.GONE);
                }
                //StrUtils.SetTxt(grid_item.my_show_content_title, datas.get(position).getIntro());
                SetIntro(grid_item.my_show_content_title, datas.get(position).getIntro());
                StrUtils.SetTxt(grid_item.comment_show_transmit_numb_grid, "有" + (StrUtils.isEmpty(bShow.getSendnumber())?"0": bShow.getSendnumber())+ "人转发");
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

                StrUtils.SetTxt(video_item.my_show_item_time_video, IsShowDetaiDate ? StrUtils.longtostr(bShow.getCreate_time()) : DateUtils.convertTimeToFormat(bShow.getCreate_time()));
                video_item.comment_show_share_iv_video.setOnClickListener(new ShareShowClick(datas.get(position)));
                video_item.comment_show_gooddetail_iv_video.setOnClickListener(new LookDetailClick(datas.get(position)));

                if(mClickHeaderIv){
                    video_item.my_show_item_icon_video.setClickable(true);
                    video_item.my_show_item_icon_video.setOnClickListener(new LookShowClick(datas.get(position)));
                }else{
                    video_item.my_show_item_icon_video.setClickable(false);
                }

                if (isMyShow(bShow.getSeller_id())) {
                    video_item.my_show_item_delete_video.setVisibility(View.VISIBLE);
                    video_item.my_show_item_delete_video.setOnClickListener(new DeleteShowClick(datas.get(position)));
                } else {
                    video_item.my_show_item_delete_video.setVisibility(View.GONE);
                }

               // StrUtils.SetTxt(video_item.my_show_content_video_title, datas.get(position).getIntro());

                SetIntro(video_item.my_show_content_video_title, datas.get(position).getIntro());

                video_item.my_show_content_video_title.setVisibility(StrUtils.isEmpty(datas.get(position).getIntro()) ? View.GONE : View.VISIBLE);


                StrUtils.SetTxt(video_item.comment_show_transmit_numb_video, "有" + (StrUtils.isEmpty(bShow.getSendnumber())?"0": bShow.getSendnumber())+ "人转发");
                try {
                    ImageLoaderUtil.Load2(bShow.getPre_url(),
                            video_item.item_recycler_my_show_vido_image, R.drawable.error_iv1);
                } catch (Exception e) {
                }
                break;

            case Show_Pic:
                MyShowSinglePicItem pic_item = (MyShowSinglePicItem) holder;
                ImageLoaderUtil.Load2(bShow.getSellerinfo().getAvatar(), pic_item.my_show_item_icon_pic, R.drawable.error_iv2);
                StrUtils.SetTxt(pic_item.my_show_item_name_pic, bShow.getSellerinfo().getSeller_name());

                StrUtils.SetTxt(pic_item.my_show_item_time_pic, IsShowDetaiDate ? StrUtils.longtostr(bShow.getCreate_time()) : DateUtils.convertTimeToFormat(bShow.getCreate_time()));
                pic_item.comment_show_share_iv_pic.setOnClickListener(new ShareShowClick(datas.get(position)));
                pic_item.comment_show_gooddetail_iv_pic.setOnClickListener(new LookDetailClick(datas.get(position)));

                if(mClickHeaderIv){
                    pic_item.my_show_item_icon_pic.setClickable(true);
                    pic_item.my_show_item_icon_pic.setOnClickListener(new LookShowClick(datas.get(position)));
                }else{
                    pic_item.my_show_item_icon_pic.setClickable(false);
                }

                if (isMyShow(bShow.getSeller_id())) {
                    pic_item.my_show_item_delete_pic.setVisibility(View.VISIBLE);
                    pic_item.my_show_item_delete_pic.setOnClickListener(new DeleteShowClick(datas.get(position)));
                } else {
                    pic_item.my_show_item_delete_pic.setVisibility(View.GONE);
                }
                SetIntro(pic_item.my_show_content_single_pic_title, datas.get(position).getIntro());

                StrUtils.SetTxt(pic_item.comment_show_transmit_numb_pic, "有" + (StrUtils.isEmpty(bShow.getSendnumber())?"0": bShow.getSendnumber())+ "人转发");
                try {
                    ImageLoaderUtil.Load2(bShow.getImgarr().get(0),
                            pic_item.item_recycler_my_show_single_pic, R.drawable.error_iv1);
                } catch (Exception e) {
                }
                break;

        }

    }

    private void SetIntro(TextView view,String intro){
        if(StrUtils.isEmpty(intro)){
            view.setVisibility(View.GONE);
        }else{
            view.setVisibility(View.VISIBLE);
            StrUtils.SetTxt(view,intro);
        }
    }

    @Override
    public int getItemCount() {
        return  datas.size();
    }


    public void FrashData(List<BShow> datas1) {
        this.datas = datas1;
        this.notifyDataSetChanged();
    }

    public void FrashAllData(List<BShow> datas2) {
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

    /**
     * 查看show
     */
    class LookShowClick implements View.OnClickListener {
        BShow MyShareShow;

        public LookShowClick(BShow myShareShow) {
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
        BShow MyShareShow;

        public LookDetailClick(BShow myShareShow) {
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

        BShow MyShareShow;

        public ShareShowClick(BShow myShareShow) {
            MyShareShow = myShareShow;
        }

        @Override
        public void onClick(View v) {

            BNew bnew = new BNew();
            bnew.setShare_title(mContext.getResources().getString(R.string.share_app) + "  " + MyShareShow.getGoodinfo().getTitle());
            bnew.setShare_content(mContext.getResources().getString(R.string.share_app) + "  " + MyShareShow.getGoodinfo().getTitle());

            bnew.setShare_url(MyShareShow.getGoodurl());
            if (MyShareShow.getIs_type().equals("0")) {//照片直接取出第一张进行分享
                bnew.setShare_log(MyShareShow.getImgarr().get(0));
            } else {//视频  直接取出视频封面分享
                bnew.setShare_log(MyShareShow.getPre_url());
            }
            PShowShare showShare = new PShowShare(mContext, bnew);
            showShare.SetShareListener(new PShowShare.ShowShareInterListener() {
                @Override
                public void GetResultType(int ResultType) {
                    switch (ResultType) {
                        case 3:
                            if (MyShareShow.getIs_type().equals("0")) {// 照片
                                //如果是照片  只需要把照片数组和商品id 传到show分享即可
                                BShowShare MyBShowShare = new BShowShare();
                                MyBShowShare.setImgarr(MyShareShow.getImgarr());
                                MyBShowShare.setGoods_id(MyShareShow.getGoods_id());
                                MyBShowShare.setIntro(StrUtils.NullToStr3(MyShareShow.getIntro()));
                                PromptManager
                                        .SkipActivity(
                                                SActivity,
                                                new Intent(SActivity, ShowSelectPic.class).putExtra(
                                                        ShowSelectPic.Key_Data,
                                                        MyBShowShare));

                            } else {// 视频
                                BShowShare MyVidoBShowShare = new BShowShare();
                                MyVidoBShowShare.setVido_pre_url(MyShareShow.getPre_url());
                                MyVidoBShowShare.setVido_Vid(MyShareShow.getVid());
                                MyVidoBShowShare.setIntro(StrUtils.NullToStr3(MyShareShow.getIntro()));
                                MyVidoBShowShare.setGoods_id(MyShareShow.getGoods_id());
                                PromptManager.SkipActivity(
                                        SActivity,
                                        new Intent(SActivity, AGoodVidoShare.class)
                                                .putExtra(AGoodVidoShare.Key_VidoFromShow,
                                                        true).putExtra(
                                                AGoodVidoShare.Key_VidoData,
                                                MyVidoBShowShare));

                            }
                            break;
                    }
                }
            });
            showShare.showAtLocation(BaseView, Gravity.BOTTOM, 0, 0);


        }
    }

    class DeleteShowClick implements View.OnClickListener{
        BShow DeleteBShow;
        public DeleteShowClick(BShow myShareShow){
            DeleteBShow = myShareShow;
        }

        @Override
        public void onClick(View v) {
            ShowCustomDialog(DeleteBShow.getId(),DeleteBShow.getSeller_id());
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
    private void ShowCustomDialog(final String ShowId,final String seller_id) {
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
                if(SActivity instanceof ARecyclerMyShow){
                    ((ARecyclerMyShow)SActivity).DeletMyShow(ShowId,seller_id);

                }else if(SActivity instanceof ARecyclerOtherShow){
                    ((ARecyclerOtherShow)SActivity).DeletMyShow(ShowId,seller_id);
                }


            }
        });
    }


}



