package io.vtown.WeiTangApp.ui.ui;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BShow;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DateUtils;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.CopyTextView;
import io.vtown.WeiTangApp.comment.view.RecyclerCommentItemDecoration;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.AVidemplay;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;

/**
 * Created by Yihuihua on 2016/9/23.
 */

public class ARecyclerShow extends ATitleBase {

    private RecyclerView recyclerview_show;
    private BUser user_Get;
    /**
     * 当前的最后item的lastid
     */
    private String LastId = "";
    private ShowListAdapter mShowListAdapter;
    private View mRootView;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_recycler_show);
        mRootView = LayoutInflater.from(BaseContext).inflate(R.layout.activity_recycler_show,null);
        user_Get = Spuit.User_Get(BaseContext);
        IView();
        IData(LastId, LOAD_INITIALIZE);
    }

    private void IView() {
        recyclerview_show = (RecyclerView) findViewById(R.id.recyclerview_show);
        recyclerview_show.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_show.addItemDecoration(new RecyclerCommentItemDecoration(BaseContext,RecyclerCommentItemDecoration.VERTICAL_LIST,R.drawable.shape_show_divider_line));
        mShowListAdapter = new ShowListAdapter(R.layout.item_recycler_show);
        recyclerview_show.setAdapter(mShowListAdapter);
    }

    private void IData(String LastId, int LoadType) {
        SetTitleHttpDataLisenter(this);
        if (LoadType == LOAD_INITIALIZE)
            PromptManager.showtextLoading(BaseContext, getResources()
                    .getString(R.string.loading));
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("seller_id", user_Get.getSeller_id());
        map.put("lastid", LastId);
        // map.put("pagesize", "10");s
        FBGetHttpData(map, Constants.Show_ls, Request.Method.GET, 0, LoadType);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt("SHOW");
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
        mShowListAdapter.FreshData(datas);
    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.ShowCustomToast(BaseContext,error);
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

    class ShowListAdapter extends RecyclerView.Adapter<ShowItem> {

        private int ResouseId;
        private LayoutInflater inflater;
        private List<BShow> datas = new ArrayList<BShow>();

        public ShowListAdapter(int ResouseId) {
            super();
            this.ResouseId = ResouseId;
            this.inflater = LayoutInflater.from(BaseContext);
        }

        @Override
        public ShowItem onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = inflater.inflate(ResouseId, parent, false);
            ShowItem showItem = new ShowItem(inflate);
            return showItem;
        }

        @Override
        public void onBindViewHolder(ShowItem holder, final int position) {
           final BShow bshow = datas.get(position);

            ImageLoaderUtil.Load2(bshow.getSellerinfo().getAvatar(),
                    holder.item_recycler_show_iv, R.drawable.testiv);

            IvSet(bshow, holder);
            StrUtils.SetTxt(holder.item_recycler_show_name,datas.get(position).getSellerinfo().getSeller_name());
            if(StrUtils.isEmpty(bshow.getIntro())){
                holder.item_recycler_show_txt_inf.setVisibility(View.GONE);
            }else{
                StrUtils.SetTxt(holder.item_recycler_show_txt_inf,bshow.getIntro());
            }

            StrUtils.SetTxt(holder.item_recycler_show_share_number,
                    bshow.getSendnumber() + "人转发");
            long create_time = bshow.getCreate_time();
            StrUtils.SetTxt(holder.item_recycler_show_time, DateUtils.convertTimeToFormat(create_time));




        }

        private void IvSet(BShow bShow, ShowItem holder) {
            boolean IsPic = bShow.getIs_type().equals("0");
            // 下边是图片和视频
            if (IsPic) {// 图片显示gradview
                final List<String> Urls = bShow.getImgarr();
                if (Urls == null || Urls.size() == 0)
                    return;
                if (1 == Urls.size()) {// 一张图片
                    holder.item_recycler_show_gridview.setVisibility(View.GONE);
                    holder.item_recycler_show_vido_lay.setVisibility(View.GONE);
                    holder.item_recycler_show_oneiv.setVisibility(View.VISIBLE);
                    try {
                        ImageLoaderUtil.Load(Urls.get(0),
                                holder.item_recycler_show_oneiv, R.drawable.error_iv1);
                    } catch (Exception e) {
                    }
                    holder.item_recycler_show_oneiv
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View arg0) {
                                    Intent mIntent = new Intent(BaseContext,
                                            AphotoPager.class);
                                    mIntent.putExtra("position", 0);
                                    mIntent.putExtra("urls",
                                            StrUtils.LsToArray(Urls));
                                    PromptManager.SkipActivity(BaseActivity,
                                            mIntent);
                                }
                            });
                }
                if (Urls.size() > 1) {// 两张图片
                    holder.item_recycler_show_gridview.setVisibility(View.VISIBLE);
                    holder.item_recycler_show_vido_lay.setVisibility(View.GONE);
                    holder.item_recycler_show_oneiv.setVisibility(View.GONE);
                    // 赋数据
                    holder.item_recycler_show_gridview.setAdapter(new Ivdapter(Urls));
                    holder.item_recycler_show_gridview
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
                    holder.item_recycler_show_gridview.setLayoutParams(layoutParams);
                    if (Urls.size() == 4) {
                        holder.item_recycler_show_gridview.setNumColumns(2);

                    }
                    if (Urls.size() > 4) {
                        holder.item_recycler_show_gridview.setNumColumns(3);

                    }
                }
            } else {// 视频只有一个relativity

                // String VidoPicUrl = blComment.getPre_url();// 视频的第一帧的图片
                holder.item_recycler_show_gridview.setVisibility(View.GONE);
                holder.item_recycler_show_vido_lay.setVisibility(View.VISIBLE);
                holder.item_recycler_show_oneiv.setVisibility(View.GONE);
                // myItem.item_show_vido_control_image
                try {
                    ImageLoaderUtil.Load2(bShow.getPre_url(),
                            holder.item_recycler_show_vido_image, R.drawable.error_iv1);
                } catch (Exception e) {
                }

                final String VidoPath = bShow.getVid();
                holder.item_recycler_show_vido_control_image
                        .setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                if (CheckNet(BaseContext))
                                    return;
                                PromptManager.SkipActivity(BaseActivity,
                                        new Intent(BaseActivity,
                                                AVidemplay.class).putExtra(
                                                AVidemplay.VidoKey, VidoPath));
                            }
                        });
            }

        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public void FreshData(List<BShow> datas){
            this.datas = datas;
            this.notifyDataSetChanged();
        }


    }

    class ShowItem extends RecyclerView.ViewHolder {

        private CircleImageView item_recycler_show_iv;
        private LinearLayout item_recycler_show_gooddetail_lay;
        private TextView item_recycler_show_name;
        private TextView item_recycler_show_gooddetail;
        private CopyTextView item_recycler_show_txt_inf;
        private RelativeLayout item_recycler_show_vido_lay;
        private ImageView item_recycler_show_vido_image;
        private ImageView item_recycler_show_vido_control_image;
        private ImageView item_recycler_show_oneiv;
        private CompleteGridView item_recycler_show_gridview;
        private TextView item_recycler_show_time;
        private TextView item_recycler_show_delete_txt;
        private ImageView item_recycler_show_gooddetail_iv;
        private ImageView item_recycler_show_share_iv;
        private TextView item_recycler_show_share_number;

        public ShowItem(View itemView) {
            super(itemView);
            item_recycler_show_iv = (CircleImageView) itemView.findViewById(R.id.item_recycler_show_iv);
            item_recycler_show_gooddetail_lay = (LinearLayout) itemView.findViewById(R.id.item_recycler_show_gooddetail_lay);
            item_recycler_show_name = (TextView) itemView.findViewById(R.id.item_recycler_show_name);
            item_recycler_show_gooddetail = (TextView) itemView.findViewById(R.id.item_recycler_show_gooddetail);
            item_recycler_show_txt_inf = (CopyTextView) itemView.findViewById(R.id.item_recycler_show_txt_inf);
            item_recycler_show_vido_lay = (RelativeLayout) itemView.findViewById(R.id.item_recycler_show_vido_lay);
            item_recycler_show_vido_image = (ImageView) itemView.findViewById(R.id.item_recycler_show_vido_image);
            item_recycler_show_vido_control_image = (ImageView) itemView.findViewById(R.id.item_recycler_show_vido_control_image);
            item_recycler_show_oneiv = (ImageView) itemView.findViewById(R.id.item_recycler_show_oneiv);
            item_recycler_show_gridview = (CompleteGridView) itemView.findViewById(R.id.item_recycler_show_gridview);
            item_recycler_show_time = (TextView) itemView.findViewById(R.id.item_recycler_show_time);
            item_recycler_show_delete_txt = (TextView) itemView.findViewById(R.id.item_recycler_show_delete_txt);
            item_recycler_show_gooddetail_iv = (ImageView) itemView.findViewById(R.id.item_recycler_show_gooddetail_iv);
            item_recycler_show_share_iv = (ImageView) itemView.findViewById(R.id.item_recycler_show_share_iv);
            item_recycler_show_share_number = (TextView) itemView.findViewById(R.id.item_recycler_show_share_number);
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
