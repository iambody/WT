package io.vtown.WeiTangApp.ui.title.center.myshow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcache.BShop;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BShow;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.CopyTextView;
import io.vtown.WeiTangApp.comment.view.RecyclerCommentItemDecoration;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;
import io.vtown.WeiTangApp.ui.ui.ARecyclerShow;

/**
 * Created by Yihuihua on 2016/9/23.
 */

public class ARecyclerMyShow extends ATitleBase {
    private RecyclerView recyclerview_my_show;
    private BUser user_get;
    private MyShowAdapter myShowAdapter;
    private String lastid = "";

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_recycler_my_show);
        user_get = Spuit.User_Get(BaseContext);
        IView();
        IData(lastid, LOAD_INITIALIZE);
    }

    private void IView() {
        recyclerview_my_show = (RecyclerView) findViewById(R.id.recyclerview_my_show);
        recyclerview_my_show.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_my_show.addItemDecoration(new RecyclerCommentItemDecoration(BaseContext, RecyclerCommentItemDecoration.VERTICAL_LIST, R.drawable.shape_show_divider_line));
        myShowAdapter = new MyShowAdapter();
        recyclerview_my_show.setAdapter(myShowAdapter);
    }

    private void IData(String lastid, int loadtype) {
        if (LOAD_INITIALIZE == loadtype) {
            PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.xlistview_header_hint_loading));
        }
        SetTitleHttpDataLisenter(this);
        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", user_get.getSeller_id());
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

    class MyShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int Show_Grid = 111;
        private static final int Show_Video = 112;
        private static final int Show_Pic = 113;
        private int Show_Txt = 114;

        private List<BShow> datas = new ArrayList<BShow>();

        private LayoutInflater inflater;

        public MyShowAdapter() {
            super();

            this.inflater = LayoutInflater.from(BaseContext);

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
                    ImageLoaderUtil.Load2(bShow.getSellerinfo().getAvatar(),grid_item.my_show_item_icon_grid,R.drawable.error_iv2);
                    StrUtils.SetTxt(grid_item.my_show_item_name_grid,bShow.getSellerinfo().getSeller_name());
                    StrUtils.SetTxt(grid_item.my_show_item_time_grid,StrUtils.longtostr(bShow.getCreate_time()));
                    StrUtils.SetTxt(grid_item.my_show_content_title, datas.get(position).getIntro());

                    // 赋数据
                    grid_item.item_recycler_my_show_gridview.setAdapter(new Ivdapter(Urls));
                    grid_item.item_recycler_my_show_gridview
                            .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0,
                                                        View arg1, int arg2, long arg3) {
                                    Intent mIntent = new Intent(BaseContext,
                                            AphotoPager.class);
                                    mIntent.putExtra("position", arg2);
                                    mIntent.putExtra("urls",
                                            StrUtils.LsToArray(Urls));
                                    PromptManager.SkipActivity(BaseActivity,
                                            mIntent);
                                }

                            });

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            (screenWidth - DimensionPixelUtil.dip2px(
                                    BaseContext, 80)),
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
                    ImageLoaderUtil.Load2(bShow.getSellerinfo().getAvatar(),video_item.my_show_item_icon_video,R.drawable.error_iv2);
                    StrUtils.SetTxt(video_item.my_show_item_name_video,bShow.getSellerinfo().getSeller_name());
                    StrUtils.SetTxt(video_item.my_show_item_time_video,StrUtils.longtostr(bShow.getCreate_time()));
                    StrUtils.SetTxt(video_item.my_show_content_video_title, datas.get(position).getIntro());
                    try {
                        ImageLoaderUtil.Load(bShow.getPre_url(),
                                video_item.item_recycler_my_show_vido_image, R.drawable.error_iv1);
                    } catch (Exception e) {
                    }
                    //StrUtils.SetTxt(video_item.my_show_video_create_time, StrUtils.longtostr(bShow.getCreate_time()));
                    break;

                case Show_Pic:
                    MyShowSinglePicItem pic_item = (MyShowSinglePicItem) holder;
                    ImageLoaderUtil.Load2(bShow.getSellerinfo().getAvatar(),pic_item.my_show_item_icon_pic,R.drawable.error_iv2);
                    StrUtils.SetTxt(pic_item.my_show_item_name_pic,bShow.getSellerinfo().getSeller_name());
                    StrUtils.SetTxt(pic_item.my_show_item_time_pic,StrUtils.longtostr(bShow.getCreate_time()));
                    StrUtils.SetTxt(pic_item.my_show_content_single_pic_title, datas.get(position).getIntro());
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
    }


    class MyShowItem extends RecyclerView.ViewHolder {

        private CopyTextView my_show_content_title;
        private CompleteGridView item_recycler_my_show_gridview;
        private View my_show_grid_head;
        private CircleImageView my_show_item_icon_grid;
        private TextView my_show_item_name_grid;
        private TextView my_show_item_time_grid;
        //private TextView my_show_create_time;


        public MyShowItem(View itemView) {
            super(itemView);
            my_show_grid_head = itemView.findViewById(R.id.my_show_grid_head);
            my_show_item_icon_grid = (CircleImageView) my_show_grid_head.findViewById(R.id.my_show_item_icon);
            my_show_item_name_grid = (TextView) my_show_grid_head.findViewById(R.id.my_show_item_name);
            my_show_item_time_grid = (TextView) my_show_grid_head.findViewById(R.id.my_show_item_time);
            my_show_content_title = (CopyTextView) itemView.findViewById(R.id.my_show_content_title);
            item_recycler_my_show_gridview = (CompleteGridView) itemView.findViewById(R.id.item_recycler_my_show_gridview);
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

        public MyShowVideoItem(View itemView) {
            super(itemView);
            my_show_video_head = itemView.findViewById(R.id.my_show_video_head);
            my_show_item_icon_video = (CircleImageView) my_show_video_head.findViewById(R.id.my_show_item_icon);
            my_show_item_name_video = (TextView) my_show_video_head.findViewById(R.id.my_show_item_name);
            my_show_item_time_video = (TextView) my_show_video_head.findViewById(R.id.my_show_item_time);
            my_show_content_video_title = (CopyTextView) itemView.findViewById(R.id.my_show_content_video_title);
            item_recycler_my_show_vido_image = (ImageView) itemView.findViewById(R.id.item_recycler_my_show_vido_image);
            item_recycler_my_show_vido_control_image = (ImageView) itemView.findViewById(R.id.item_recycler_my_show_vido_control_image);
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
        // private TextView my_show_single_pic_create_time;

        public MyShowSinglePicItem(View itemView) {
            super(itemView);
            my_show_single_pic_head = itemView.findViewById(R.id.my_show_single_pic_head);
            my_show_item_icon_pic = (CircleImageView) my_show_single_pic_head.findViewById(R.id.my_show_item_icon);
            my_show_item_name_pic = (TextView) my_show_single_pic_head.findViewById(R.id.my_show_item_name);
            my_show_item_time_pic = (TextView) my_show_single_pic_head.findViewById(R.id.my_show_item_time);
            my_show_content_single_pic_title = (CopyTextView) itemView.findViewById(R.id.my_show_content_single_pic_title);
            item_recycler_my_show_single_pic = (ImageView) itemView.findViewById(R.id.item_recycler_my_show_single_pic);
            //my_show_single_pic_create_time = (TextView) itemView.findViewById(R.id.my_show_single_pic_create_time);
        }
    }




    /**
     * 九宫格图片的Ap
     */
    class Ivdapter extends BaseAdapter {

        private List<String> datas;

        private LayoutInflater iLayoutInflater;

        public Ivdapter(List<String> datas) {
            super();
            this.datas = datas;

            this.iLayoutInflater = LayoutInflater.from(BaseContext);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int arg0) {
            return datas.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(final int arg0, View arg1, ViewGroup arg2) {
            InImageItem imageItem = null;
            if (null == arg1) {
                arg1 = iLayoutInflater.inflate(R.layout.item_show_in_imagview,
                        null);
                imageItem = new InImageItem();
                imageItem.item_show_in_imagview = ViewHolder.get(arg1,
                        R.id.item_show_in_imagview);
                // LayoutParams layoutParams = new LinearLayout.LayoutParams(
                // (screenWidth - DimensionPixelUtil.dip2px(baseApplication,
                // 1020)) / 3,
                // (screenWidth - DimensionPixelUtil.dip2px(baseApplication,
                // 100)) / 3);
                // imageItem.item_show_in_imagview.setLayoutParams(layoutParams);
                //


                arg1.setTag(imageItem);
            } else {
                imageItem = (InImageItem) arg1.getTag();
            }
//			ImageLoaderUtil.Load22(datas.get(arg0),
//					imageItem.item_show_in_imagview, R.drawable.error_iv2);


            String tag = (String) imageItem.item_show_in_imagview.getTag();
            if (tag == null || !tag.equals(datas.get(arg0))) {
                ImageLoader.getInstance().displayImage(
                        datas.get(arg0),
                        new ImageViewAware(imageItem.item_show_in_imagview, false), ImageLoaderUtil.GetDisplayOptions(R.drawable.error_iv2),
                        new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view,
                                                        FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String s, View view,
                                                          Bitmap bitmap) {
                                view.setTag(datas.get(arg0));// 确保下载完成再打tag.
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }

                        });
            }


            return arg1;
        }

        class InImageItem {
            ImageView item_show_in_imagview;
        }
    }
}
