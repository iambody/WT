package io.vtown.WeiTangApp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BShow;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.CopyTextView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;

/**
 * Created by Yihuihua on 2016/9/27.
 */

public class ShowRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int Show_Grid = 111;
    private static final int Show_Video = 112;
    private static final int Show_Pic = 113;
   // private int Show_Txt = 114;

    private List<BShow> datas = new ArrayList<BShow>();

    private LayoutInflater inflater;

    private int mScreenWidth;

    private Context mContext;

    public ShowRecyclerAdapter(Context context,int screenWidth) {
        super();
        this.mContext = context;
        this.mScreenWidth = screenWidth;
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

    private boolean isMyShow(String seller_id){
        BUser bUser = Spuit.User_Get(mContext);
        String _seller_id = bUser.getSeller_id();
        if(seller_id.equals(_seller_id)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int getItemViewType(int position) {

        BShow bShow = datas.get(position);
        String is_type = bShow.getIs_type();
        if ("0".equals(is_type)) {
            int size = bShow.getImgarr().size();
            if (size > 1) {
                return Show_Grid;
            } else if (size == 1) {
                return Show_Pic;
            }
        } else {
            return Show_Video;
        }

        return super.getItemViewType(position);
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
                StrUtils.SetTxt(grid_item.my_show_item_time_grid, StrUtils.longtostr(bShow.getCreate_time()));
                if(isMyShow(bShow.getSeller_id())){
                    grid_item.my_show_item_delete_grid.setVisibility(View.VISIBLE);
                }else{
                    grid_item.my_show_item_delete_grid.setVisibility(View.GONE);
                }
                StrUtils.SetTxt(grid_item.my_show_content_title, datas.get(position).getIntro());
                StrUtils.SetTxt(grid_item.comment_show_transmit_numb_grid,"有"+bShow.getSendnumber()+"人转发");
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

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        (mScreenWidth - DimensionPixelUtil.dip2px(
                                mContext, 80)),
                        LinearLayout.LayoutParams.WRAP_CONTENT);

                grid_item.item_recycler_my_show_gridview.setLayoutParams(layoutParams);
                if (Urls.size() == 4) {
                    grid_item.item_recycler_my_show_gridview.setNumColumns(2);

                }
                if (Urls.size() > 4) {
                    grid_item.item_recycler_my_show_gridview.setNumColumns(3);

                }
                //StrUtils.SetTxt(grid_item.my_show_create_time, StrUtils.longtostr(bShow.getCreate_time()));
                break;

            case Show_Video:
                MyShowVideoItem video_item = (MyShowVideoItem) holder;
                ImageLoaderUtil.Load2(bShow.getSellerinfo().getAvatar(), video_item.my_show_item_icon_video, R.drawable.error_iv2);
                StrUtils.SetTxt(video_item.my_show_item_name_video, bShow.getSellerinfo().getSeller_name());
                StrUtils.SetTxt(video_item.my_show_item_time_video, StrUtils.longtostr(bShow.getCreate_time()));
                if(isMyShow(bShow.getSeller_id())){
                    video_item.my_show_item_delete_video.setVisibility(View.VISIBLE);
                }else{
                    video_item.my_show_item_delete_video.setVisibility(View.GONE);
                }
                StrUtils.SetTxt(video_item.my_show_content_video_title, datas.get(position).getIntro());
                StrUtils.SetTxt(video_item.comment_show_transmit_numb_video,"有"+bShow.getSendnumber()+"人转发");
                try {
                    ImageLoaderUtil.Load(bShow.getPre_url(),
                            video_item.item_recycler_my_show_vido_image, R.drawable.error_iv1);
                } catch (Exception e) {
                }
                //StrUtils.SetTxt(video_item.my_show_video_create_time, StrUtils.longtostr(bShow.getCreate_time()));
                break;

            case Show_Pic:
                MyShowSinglePicItem pic_item = (MyShowSinglePicItem) holder;
                ImageLoaderUtil.Load2(bShow.getSellerinfo().getAvatar(), pic_item.my_show_item_icon_pic, R.drawable.error_iv2);
                StrUtils.SetTxt(pic_item.my_show_item_name_pic, bShow.getSellerinfo().getSeller_name());
                StrUtils.SetTxt(pic_item.my_show_item_time_pic, StrUtils.longtostr(bShow.getCreate_time()));
                if(isMyShow(bShow.getSeller_id())){
                    pic_item.my_show_item_delete_pic.setVisibility(View.VISIBLE);
                }else{
                    pic_item.my_show_item_delete_pic.setVisibility(View.GONE);
                }
                StrUtils.SetTxt(pic_item.my_show_content_single_pic_title, datas.get(position).getIntro());
                StrUtils.SetTxt(pic_item.comment_show_transmit_numb_pic,"有"+bShow.getSendnumber()+"人转发");
                try {
                    ImageLoaderUtil.Load(bShow.getImgarr().get(0),
                            pic_item.item_recycler_my_show_single_pic, R.drawable.error_iv1);
                } catch (Exception e) {
                }
                //StrUtils.SetTxt(pic_item.my_show_single_pic_create_time, StrUtils.longtostr(bShow.getCreate_time()));
                break;

        }


    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public void FrashData(List<BShow> datas) {
        this.datas = datas;
        this.notifyDataSetChanged();
    }

    public void FrashAllData(List<BShow> datas){
        this.datas.addAll(datas);
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
            //my_show_single_pic_create_time = (TextView) itemView.findViewById(R.id.my_show_single_pic_create_time);
        }
    }

}



