package io.vtown.WeiTangApp.fragment.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.android.volley.Request;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BShowShare;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BLShow;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DateUtils;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.ShowSelectPic;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.custom.RefreshLayout;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.comment.view.pop.PShowShare;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.ui.comment.AGoodVidoShare;
import io.vtown.WeiTangApp.ui.comment.AVidemplay;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.center.myshow.AOtherShow;
import io.vtown.WeiTangApp.ui.title.center.myshow.ARecyclerOtherShow;

/**
 * Created by datutu on 2016/9/18.
 */
public class FMainShow extends FBase implements RefreshLayout.OnLoadListener, View.OnClickListener {

    private RefreshLayout fragment_show_refrash;
    private ListView maintab_show_show_ls;
    private TextView maintab_show_uptxt;
    private View maintab_show_nodata_lay;
    //数据
    private BUser MyUser;
    /**
     * AP
     */
    private MainShowAp showAp;


    /**
     * 当前的最后item的lastid
     */
    private String LastId = "";
    /**
     * 是否存在缓存
     */
    private boolean IsCache;
    /**
     * 当前的listview可见的第一个位置
     */
    private int startPos = 0;
    /**
     * 当前listview可见的最后一个位置
     */
    private int endPos;

    private boolean needToTop;


    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_main_show, null);
        MyUser = Spuit.User_Get(BaseContext);
        SetTitleHttpDataLisenter(this);
        EventBus.getDefault().register(this, "MainShowReciverMessage", BMessage.class);
        IBaseView();
        ICacheLs();
        fragment_show_refrash.setRefreshing(true);
        IData(LastId, REFRESHING);
    }

    private void IData(String lastId, int initialize) {
        if (initialize == INITIALIZE && !IsCache)
            PromptManager.showtextLoading(BaseContext, getResources()
                    .getString(R.string.loading));

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("seller_id", MyUser.getSeller_id());
        map.put("lastid", LastId);
        // map.put("pagesize", "10");
        FBGetHttpData(map, Constants.Show_ls, Request.Method.GET, 0, initialize);

    }

    /**
     * 缓存数据的处理
     */
    private void ICacheLs() {
        // 缓存中有show的数据进行处理
        String CachData = Spuit.Show_GetStr(BaseContext);
        if (!StrUtils.isEmpty(CachData)) {
            List<BLShow> data = new ArrayList<BLShow>();
            // 开始解析*************************
            try {
                data = JSON.parseArray(CachData, BLShow.class);// ();
            } catch (Exception e) {
                IDataView(maintab_show_show_ls, maintab_show_nodata_lay, NOVIEW_INITIALIZE);
                return;
            }

            showAp.FrashData(data);
            IsCache = true;
        } else {// 没有数据就直接显示空白
            IsCache = false;
            // IDataView(ShowLs, show_nodata_lay, NOVIEW_INITIALIZE);
        }
    }


    /**
     * 初始化各种view
     */
    private void IBaseView() {
        neterrorview = io.vtown.WeiTangApp.comment.util.ViewHolder.get(BaseView, R.id.fragment_main_show_neterrorview);
        neterrorview.setOnClickListener(this);
        CheckNet();
        fragment_show_refrash = (RefreshLayout) BaseView.findViewById(R.id.fragment_show_refrash);
        fragment_show_refrash.setOnLoadListener(this);
        fragment_show_refrash.setColorSchemeResources(R.color.app_fen, R.color.app_fen1, R.color.app_fen2, R.color.app_fen3);
        //只能设置不可以下拉刷新的方法
//        fragment_show_refrash.setEnabled(false);

        maintab_show_show_ls = (ListView) BaseView.findViewById(R.id.maintab_show_show_ls);
        maintab_show_uptxt = (TextView) BaseView.findViewById(R.id.maintab_show_uptxt);
        maintab_show_nodata_lay = BaseView.findViewById(R.id.maintab_show_nodata_lay);


        maintab_show_uptxt.setOnClickListener(this);
        maintab_show_nodata_lay.setOnClickListener(this);
        // ls滑动时候不要加载图片
        maintab_show_show_ls.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {


                if (firstVisibleItem != 0) {
                    needToTop = true;
                    maintab_show_uptxt.setVisibility(View.VISIBLE);
                } else if (firstVisibleItem == 0) {
                    needToTop = false;
                    maintab_show_uptxt.setVisibility(View.GONE);
                }

            }
        });

        showAp = new MainShowAp(R.layout.item_show);
        maintab_show_show_ls.setAdapter(showAp);
        maintab_show_nodata_lay.setOnClickListener(this);
    }

    public void MainShowReciverMessage(BMessage myMessage) {
        if (myMessage.getMessageType() == BMessage.Tage_Main_To_ShowData) {// 第一次进来
            // 后台通知
            // 进行偷偷加载数据
            IData(LastId, LOADHind);
        }
        if (myMessage.getMessageType() == BMessage.Tage_Show_Hind_Load) {
            IData(LastId, LOADHind);
        }
    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_main_show_neterrorview:
                PromptManager.GoToNetSeting(BaseActivity);
                break;
            case R.id.maintab_show_uptxt:
                maintab_show_show_ls.smoothScrollToPosition(0);
                break;
            case R.id.maintab_show_nodata_lay:
                IData(LastId, INITIALIZE);
                break;
        }
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


    /**
     * Show的adapter
     *
     * @author datutu
     */
    private class MainShowAp extends BaseAdapter {

        /**
         * 填充器
         */
        private LayoutInflater inflater;
        /**
         * 资源id
         */
        private int ResourceId;
        /**
         * 数据
         */
        private List<BLShow> datas = new ArrayList<BLShow>();

        public MainShowAp(int resourceId) {
            super();
            this.inflater = LayoutInflater.from(BaseContext);
            ResourceId = resourceId;
        }

        public void DeletPostion(int Postioo) {
            this.datas.remove(Postioo);
            this.notifyDataSetChanged();
        }

        public void FrashData(List<BLShow> dass) {

            this.datas = dass;
            // this.datas.addAll(dass);
            this.notifyDataSetChanged();
        }

        public void AddFrashData(List<BLShow> dasss) {
            datas.addAll(dasss);
            this.notifyDataSetChanged();
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
        public View getView(int arg0, View convertView, ViewGroup arg2) {
            ShowItem myItem = null;
            if (convertView == null) {
                myItem = new ShowItem();
                convertView = inflater.inflate(ResourceId, null);
                // 公用
                myItem.item_show_iv = (CircleImageView) convertView
                        .findViewById(R.id.item_show_iv);

//                myItem.item_show_gooddetail = ViewHolder.get(convertView,
//                        R.id.item_show_gooddetail);
                myItem.item_show_name = ViewHolder.get(convertView,
                        R.id.item_show_name);
                myItem.item_show_txt_inf = ViewHolder.get(convertView,
                        R.id.item_show_txt_inf);
                myItem.item_show_time = ViewHolder.get(convertView,
                        R.id.item_show_time);
                myItem.item_show_share_iv = ViewHolder.get(convertView,
                        R.id.item_show_share_iv);
                myItem.item_show_share_number = ViewHolder.get(convertView,
                        R.id.item_show_share_number);
                myItem.item_show_gooddetail_iv = ViewHolder.get(convertView,
                        R.id.item_show_gooddetail_iv);

                // 视频播放
                myItem.item_show_vido_lay = (RelativeLayout) convertView
                        .findViewById(R.id.item_show_vido_lay);
                myItem.item_show_vido_image = ViewHolder.get(convertView,
                        R.id.item_show_vido_image);
                myItem.item_show_vido_control_image = ViewHolder.get(
                        convertView, R.id.item_show_vido_control_image);
                myItem.item_show_delete_txt = ViewHolder.get(convertView,
                        R.id.item_show_delete_txt);
                myItem.item_show_gooddetail_lay = (LinearLayout) convertView
                        .findViewById(R.id.item_show_gooddetail_lay);
                // 一张图片
                myItem.item_show_oneiv = ViewHolder.get(convertView,
                        R.id.item_show_oneiv);
                // 多张图片
                myItem.item_show_gridview = (CompleteGridView) convertView
                        .findViewById(R.id.item_show_gridview);

                // IvSet(datas.get(arg0), myItem);

                convertView.setTag(myItem);
            } else {
                myItem = (ShowItem) convertView.getTag();
            }
            ImageLoaderUtil.Load2(datas.get(arg0).getSellerinfo().getAvatar(),
                    myItem.item_show_iv, R.drawable.testiv);
            IvSet(datas.get(arg0), myItem);
            final BLShow ItemData = datas.get(arg0);
            final int MyPostion = arg0;
            // 是我发的show就需要显示删除
            myItem.item_show_delete_txt
                    .setVisibility(MyUser.getSeller_id().equals(
                            ItemData.getSeller_id()) ? View.VISIBLE : View.GONE);

            // IvSet(ItemData, myItem);
            // IViewData(myItem, datas.get(arg0), BaseView, arg0);

            myItem.item_show_share_iv.setOnClickListener(new ShareClick(
                    ItemData, myItem, BaseView));
            StrUtils.SetTxt(myItem.item_show_share_number,
                    ItemData.getSendnumber() + "人转发");
            StrUtils.SetTxt(myItem.item_show_name, ItemData.getSellerinfo()
                    .getSeller_name());
            StrUtils.SetTxt(myItem.item_show_txt_inf, ItemData.getIntro());
            StrUtils.SetTxt(myItem.item_show_time, DateUtils
                    .convertTimeToFormat(Long.parseLong(ItemData
                            .getCreate_time())));
            myItem.item_show_delete_txt
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (CheckNet(BaseContext))
                                return;
                            SelectToDo(1, ItemData.getId(), MyPostion);

                        }
                    });
            myItem.item_show_gooddetail_lay
                    .setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {


                            if (CheckNet(BaseContext))
                                return;
                            Intent intent = new Intent(BaseActivity,
                                    AOtherShow.class);
                            intent.putExtra(
                                    BaseKey_Bean,
                                    new BComment(
                                            ItemData.getSeller_id(),
                                            ItemData.getSellerinfo().getCover(),
                                            ItemData.getSellerinfo()
                                                    .getAvatar(), ItemData
                                            .getSellerinfo()
                                            .getSeller_name(), ItemData
                                            .getSellerinfo()
                                            .getIs_brand()));
                            PromptManager.SkipActivity(BaseActivity, intent);
                        }
                    });
            myItem.item_show_gooddetail_iv
                    .setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (CheckNet(BaseContext))
                                return;
                            PromptManager.SkipActivity(BaseActivity,
                                    new Intent(BaseActivity, AGoodDetail.class)
                                            .putExtra("goodid",
                                                    ItemData.getGoods_id()));
                        }
                    });
            myItem.item_show_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (CheckNet(BaseContext))
                        return;
                    Intent intent = new Intent(BaseActivity, ARecyclerOtherShow.class);
                    intent.putExtra(BaseKey_Bean,
                            new BComment(ItemData.getSeller_id(), ItemData
                                    .getSellerinfo().getCover(), ItemData
                                    .getSellerinfo().getAvatar(), ItemData
                                    .getSellerinfo().getSeller_name(), ItemData
                                    .getSellerinfo().getIs_brand()));
                    PromptManager.SkipActivity(BaseActivity, intent);

                }
            });

            return convertView;
        }

        class ShowItem {
            // 基本信息
            CircleImageView item_show_iv;
            LinearLayout item_show_gooddetail_lay;// 查看show
            TextView item_show_name;// 名字
//            TextView item_show_gooddetail;// 查看详情//废弃
            ImageView item_show_gooddetail_iv;// 查看详情
            TextView item_show_txt_inf;// 信息
            TextView item_show_time;// 时间
            ImageView item_show_delete_txt;// 删除
            ImageView item_show_share_iv;// 分享
            TextView item_show_share_number;// 转发数量
            // 视频
            RelativeLayout item_show_vido_lay;// 视频播放的父布局
            ImageView item_show_vido_image;// vido第一帧的图片
            ImageView item_show_vido_control_image;// 控制播放的view
            // 一张图片
            ImageView item_show_oneiv;
            // 多张图片时候需要的九宫格
            CompleteGridView item_show_gridview;
        }

        private void IvSet(BLShow blComment, ShowItem myItem) {
            boolean IsPic = blComment.getIs_type().equals("0");
            // 下边是图片和视频
            if (IsPic) {// 图片显示gradview
                final List<String> Urls = blComment.getImgarr();
                if (Urls == null || Urls.size() == 0)
                    return;
                if (1 == Urls.size()) {// 一张图片
                    myItem.item_show_gridview.setVisibility(View.GONE);
                    myItem.item_show_vido_lay.setVisibility(View.GONE);
                    myItem.item_show_oneiv.setVisibility(View.VISIBLE);
                    try {
                        ImageLoaderUtil.Load(Urls.get(0),
                                myItem.item_show_oneiv, R.drawable.error_iv1);
                    } catch (Exception e) {
                    }
                    myItem.item_show_oneiv
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
                    myItem.item_show_gridview.setVisibility(View.VISIBLE);
                    myItem.item_show_vido_lay.setVisibility(View.GONE);
                    myItem.item_show_oneiv.setVisibility(View.GONE);
                    // 赋数据
                    myItem.item_show_gridview.setAdapter(new Ivdapter(Urls));
                    myItem.item_show_gridview
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
                    myItem.item_show_gridview.setLayoutParams(layoutParams);
                    if (Urls.size() == 4) {
                        myItem.item_show_gridview.setNumColumns(2);

                    }
                    if (Urls.size() > 4) {
                        myItem.item_show_gridview.setNumColumns(3);

                    }
                }
            } else {// 视频只有一个relativity

                // String VidoPicUrl = blComment.getPre_url();// 视频的第一帧的图片
                myItem.item_show_gridview.setVisibility(View.GONE);
                myItem.item_show_vido_lay.setVisibility(View.VISIBLE);
                myItem.item_show_oneiv.setVisibility(View.GONE);
                // myItem.item_show_vido_control_image
                try {
                    ImageLoaderUtil.Load2(blComment.getPre_url(),
                            myItem.item_show_vido_image, R.drawable.error_iv1);
                } catch (Exception e) {
                }

                final String VidoPath = blComment.getVid();
                myItem.item_show_vido_control_image
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

        class ShareClick implements View.OnClickListener {
            private BLShow datBlComment = null;
            private ShowItem showItem;
            private View view;

            public ShareClick() {
                super();
            }

            public ShareClick(BLShow datBlComment, ShowItem showItem, View views) {
                super();
                this.datBlComment = datBlComment;
                this.showItem = showItem;
                this.view = views;
            }

            @Override
            public void onClick(View arg0) {
//                BNew bnew = new BNew();
//                bnew.setShare_title( datBlComment.getGoodinfo().getTitle());
//                bnew.setShare_content( datBlComment.getGoodinfo().getTitle());
//
//                bnew.setShare_url(datBlComment.getGoodurl());
//                if (datBlComment.getIs_type().equals("0")) {//照片直接取出第一张进行分享
//                    bnew.setShare_log(datBlComment.getImgarr().get(0));
//                } else {//视频  直接取出视频封面分享
//                    bnew.setShare_log(datBlComment.getPre_url());
//                }
//                PShowShare showShare = new PShowShare(BaseContext,BaseActivity, bnew,false,false);
//                showShare.SetShareListener(new PShowShare.ShowShareInterListener() {
//                    @Override
//                    public void GetResultType(int ResultType) {
//                        switch (ResultType) {
//
//                            case 3:
//                                if (datBlComment.getIs_type().equals("0")) {// 照片
//                                    //如果是照片  只需要把照片数组和商品id 传到show分享即可
//                                    BShowShare MyBShowShare = new BShowShare();
//                                    MyBShowShare.setImgarr(datBlComment.getImgarr());
//                                    MyBShowShare.setGoods_id(datBlComment.getGoods_id());
//                                    MyBShowShare.setIntro(StrUtils.NullToStr3(datBlComment.getIntro()));
//                                    PromptManager
//                                            .SkipActivity(
//                                                    BaseActivity,
//                                                    new Intent(BaseActivity, ShowSelectPic.class).putExtra(
//                                                            ShowSelectPic.Key_Data,
//                                                            MyBShowShare));
//
//                                } else {// 视频
//                                    BShowShare MyVidoBShowShare = new BShowShare();
//                                    MyVidoBShowShare.setVido_pre_url(datBlComment.getPre_url());
//                                    MyVidoBShowShare.setVido_Vid(datBlComment.getVid());
//                                    MyVidoBShowShare.setIntro(StrUtils.NullToStr3(datBlComment.getIntro()));
//                                    MyVidoBShowShare.setGoods_id(datBlComment.getGoods_id());
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
//
//
//                        }
//                    }
//                });
//                showShare.showAtLocation(BaseView, Gravity.BOTTOM, 0, 0);
//
//
            }
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

    /**
     * 选择的操作 Type==1标识的是删除show
     */
    private void SelectToDo(int Type, final String ShowId, final int Postion) {
        ShowCustomDialog("是否删除该show", "取消", "确定", new IDialogResult() {

            @Override
            public void RightResult() {
                PromptManager.showLoading(BaseContext);
                DeletMyShow(ShowId, Postion);
            }

            @Override
            public void LeftResult() {
            }
        });
    }

    /**
     * 删除我自己的show
     */

    private void DeletMyShow(String ShowId, int postion) {
        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put("id", ShowId);
        mHashMap.put("seller_id", MyUser.getSeller_id());
        FBGetHttpData(mHashMap, Constants.MyShowDelete, Request.Method.DELETE, 11,
                postion + 11);
    }

    @Override
    public void InitCreate(Bundle d) {

    }

    @Override
    public void getResult(int Code, String Msg, BComment Data) {
        switch (Data.getHttpResultTage()) {
            case 0:// 获取商品的列表
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
//                    if (INITIALIZE == Data.getHttpLoadType()) {
//                        Spuit.Show_SaveStr(BaseContext, Data.getHttpResultStr());
//                        showAp.FrashData(new ArrayList<BLShow>());
//                    }
                    switch (Data.getHttpLoadType()) {
                        case INITIALIZE:
                            Spuit.Show_SaveStr(BaseContext, Data.getHttpResultStr());
                            showAp.FrashData(new ArrayList<BLShow>());
                            onError(Constants.SucessToError, Data.getHttpLoadType());
                            break;
                        case REFRESHING:

                            fragment_show_refrash.setRefreshing(false);
                            onError(Constants.SucessToError, Data.getHttpLoadType());
                            break;
                        case LOADMOREING:
//                            IsStopFresh(LOADMOREING);
                            fragment_show_refrash.setLoading(false);
                            break;
                    }
                    return;
                }

                List<BLShow> data = new ArrayList<BLShow>();
                try {
                    data = JSON.parseArray(Data.getHttpResultStr(), BLShow.class);// ();
                } catch (Exception e) {
                    onError("解析错误", 1);
                    return;
                }
                IDataView(maintab_show_show_ls, maintab_show_nodata_lay, NOVIEW_RIGHT);
                if (data.size() < 10)
//                    maintab_show_show_ls.hidefoot();
                    fragment_show_refrash.setCanLoadMore(false);
                else
//                    maintab_show_show_ls.ShowFoot();
                    fragment_show_refrash.setCanLoadMore(true);
                switch (Data.getHttpLoadType()) {
                    case INITIALIZE:
                        showAp.FrashData(data);
                        Spuit.Show_SaveStr(BaseContext, Data.getHttpResultStr());
                        break;
                    case REFRESHING:
                        showAp.FrashData(data);
//                        IsStopFresh(REFRESHING);
                        fragment_show_refrash.setRefreshing(false);
                        break;
                    case LOADMOREING:
                        showAp.AddFrashData(data);
//                        IsStopFresh(LOADMOREING);
                        fragment_show_refrash.setLoading(false);
                        break;
                    default:
                        break;
                }
                LastId = data.get(data.size() - 1).getId();

                break;
            case 11:// 删除我的show
                PromptManager.ShowCustomToast(BaseContext, "删除成功");
                showAp.DeletPostion(Data.getHttpLoadType() - 11);
                break;

            default:
                break;
        }


    }

    @Override
    public void onError(String error, int LoadType) {


        if (LoadType == REFRESHING) {
//            maintab_show_show_ls.stopRefresh();
            fragment_show_refrash.setRefreshing(false);
        }
        if (LoadType == LOADMOREING) {
//            maintab_show_show_ls.stopLoadMore();
            fragment_show_refrash.setLoading(false);
        }
        if (LoadType == INITIALIZE) {
            if (showAp.getCount() == 0)
                IDataView(maintab_show_show_ls, maintab_show_nodata_lay, NOVIEW_ERROR);

            return;
        }

        PromptManager.ShowCustomToast(BaseContext, error);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
        }
    }
}
