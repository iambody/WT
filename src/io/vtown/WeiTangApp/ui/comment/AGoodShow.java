package io.vtown.WeiTangApp.ui.comment;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.adapter.BasAdapter;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DateUtils;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.comment.view.listview.LListView.IXListViewListener;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.center.myshow.AOtherShow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-1 下午12:45:06
 * @see 商品的show
 */
public class AGoodShow extends ATitleBase implements IXListViewListener {
    // 基view
    private View BaseView;
    // 记录最后一条show的id提供分页加载
    private String LastId = "";

    private BUser mBUser;
    // 无数据时候的view
    private View NoDataView;
    // 分页的listview
    private LListView activity_goodshow_ls;
    /**
     * ls的Ap
     */
    private GoodsShowAp goodsShowAp;

    // 详情页带过来的数据
    private String Goods_Sid;
    public final static String Tage_GoodSid = "gooddetaisid";


    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_goodshow);
        mBUser = Spuit.User_Get(BaseActivity);


        IBund();
        IBasVV();
        IData(Goods_Sid, LOAD_INITIALIZE);
    }

    private void IBund() {
        if (null == getIntent().getExtras()
                || !getIntent().getExtras().containsKey(Tage_GoodSid))
            BaseActivity.finish();

        Goods_Sid = getIntent().getStringExtra(Tage_GoodSid);
        SetTitleHttpDataLisenter(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//		CacheUtil.BitMapRecycle(bimap);
    }

    private void IBasVV() {
        NoDataView = findViewById(R.id.goodshow_nodata_lay);
        BaseView = LayoutInflater.from(BaseContext).inflate(
                R.layout.activity_goodshow, null);
        activity_goodshow_ls = (LListView) findViewById(R.id.activity_goodshow_ls);

        activity_goodshow_ls.setPullLoadEnable(true);
        activity_goodshow_ls.setPullRefreshEnable(true);
        activity_goodshow_ls.setXListViewListener(this);
        activity_goodshow_ls.hidefoot();

        goodsShowAp = new GoodsShowAp(R.layout.item_show);
        activity_goodshow_ls.setAdapter(goodsShowAp);
        IDataView(activity_goodshow_ls, NoDataView, NOVIEW_INITIALIZE);
    }

    private void IData(String goods_sid, int loadtype) {

        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("goods_sid", goods_sid);
        hashMap.put("lastid", LastId);
        hashMap.put("pagesize", Constants.PageSize + "");
        hashMap.put("seller_id", mBUser.getSeller_id());
        FBGetHttpData(hashMap, Constants.GoodsShow, Method.GET, 0, loadtype);

    }

    @Override
    protected void InitTile() {
        SetTitleTxt("商品Show");
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        switch (Data.getHttpResultTage()) {
            case 0:// 获取列表数据*************************

                switch (Data.getHttpLoadType()) {// 获取show列表的
                    case LOAD_INITIALIZE:
                    case LOAD_REFRESHING:

                        if (StrUtils.isEmpty(Data.getHttpResultStr())) {// 如果是空的
                            // TODO没数据需要怎么处理？

                            if (LOAD_REFRESHING == LOAD_INITIALIZE
                                    || LOAD_REFRESHING == LOAD_REFRESHING) {// 非加载更多
                                IDataView(activity_goodshow_ls, NoDataView,
                                        NOVIEW_ERROR);
                                ShowErrorCanLoad("暂无相关Show");
                                ShowErrorIv(R.drawable.error_show);
                            }

                            break;
                        }
                        IDataView(activity_goodshow_ls, NoDataView, NOVIEW_RIGHT);
                        // 解析数据**********************
                        List<BLComment> blComments = JSON.parseArray(
                                Data.getHttpResultStr(), BLComment.class);

                        if (blComments.size() == 0) {
                            DataError(Msg, Data.getHttpLoadType());
                        }
                        LastId = blComments.get(blComments.size() - 1).getId();

                        if (blComments.size() < 10) {
                            activity_goodshow_ls.hidefoot();
                        } else {
                            activity_goodshow_ls.ShowFoot();
                        }

                        if (Data.getHttpLoadType() == LOAD_REFRESHING) {
                            activity_goodshow_ls.stopRefresh();
                        }
                        goodsShowAp.FrashData(blComments);
                        break;
                    case LOAD_LOADMOREING:
                        activity_goodshow_ls.stopLoadMore();
                        if (StrUtils.isEmpty(Data.getHttpResultStr())){PromptManager.ShowCustomToast(BaseContext,"暂无更多show"); return;}
                        List<BLComment> blCommentsss = JSON.parseArray(
                                Data.getHttpResultStr(), BLComment.class);

                        if (blCommentsss.size() == 0) {
                            DataError(Msg, Data.getHttpLoadType());
                        }
                        LastId = blCommentsss.get(blCommentsss.size() - 1).getId();

                        if (blCommentsss.size() < 10) {
                            activity_goodshow_ls.hidefoot();
                        } else {
                            activity_goodshow_ls.ShowFoot();
                        }
                        goodsShowAp.AddFrashData(blCommentsss);
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
                IDataView(activity_goodshow_ls, NoDataView, NOVIEW_ERROR);
                ShowErrorCanLoad("暂无相关Show");
                ShowErrorIv(R.drawable.error_show);
                break;
            case LOAD_REFRESHING:
                activity_goodshow_ls.stopRefresh();
                IDataView(activity_goodshow_ls, NoDataView, NOVIEW_ERROR);
                break;
            case LOAD_LOADMOREING:
                activity_goodshow_ls.stopLoadMore();
                break;
            default:
                break;
        }
    }

    class GoodsShowAp extends BasAdapter {

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
        private List<BLComment> datas = new ArrayList<BLComment>();

        public GoodsShowAp(int resourceId) {
            super();

            this.inflater = LayoutInflater.from(BaseContext);
            ResourceId = resourceId;
        }

        public void FrashData(List<BLComment> dass) {

            this.datas = dass;
            // this.datas.addAll(dass);
            this.notifyDataSetChanged();
        }

        public void AddFrashData(List<BLComment> dasss) {
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

//				myItem.item_show_gooddetail = ViewHolder.get(convertView,
//						R.id.item_show_gooddetail);
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
            IvSet(datas.get(arg0), myItem);
            final BLComment ItemData = datas.get(arg0);
            myItem.item_show_gooddetail_iv.setVisibility(View.GONE);
            // IvSet(ItemData, myItem);
            // IViewData(myItem, datas.get(arg0), BaseView, arg0);
            ImageLoaderUtil.Load2(ItemData.getSellerinfo().getAvatar(),
                    myItem.item_show_iv, R.drawable.error_iv2);
            myItem.item_show_share_iv.setVisibility(View.INVISIBLE);

            // myItem.item_show_share_iv.setOnClickListener(new ShareClick(
            // ItemData, myItem, BaseView));
            StrUtils.SetTxt(myItem.item_show_share_number,
                    ItemData.getSendnumber() + "人转发");
            StrUtils.SetTxt(myItem.item_show_name, ItemData.getSellerinfo()
                    .getSeller_name());
            StrUtils.SetTxt(myItem.item_show_txt_inf, ItemData.getIntro());
            StrUtils.SetTxt(myItem.item_show_time, DateUtils
                    .convertTimeToFormat(Long.parseLong(ItemData
                            .getCreate_time())));

            return convertView;
        }

        class ShowItem {
            // 基本信息
            CircleImageView item_show_iv;
            TextView item_show_name;// 名字
            TextView item_show_gooddetail;// 查看详情
            ImageView item_show_gooddetail_iv;// 查看详情
            TextView item_show_txt_inf;// 信息
            TextView item_show_time;// 时间
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

        private void IvSet(BLComment blComment, ShowItem myItem) {
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
                        ImageLoaderUtil.Load2(Urls.get(0),
                                myItem.item_show_oneiv, R.drawable.ic_launcher);
                    } catch (Exception e) {
                    }
                    myItem.item_show_oneiv
                            .setOnClickListener(new OnClickListener() {
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
                    myItem.item_show_gridview
                            .setAdapter(new GoodsDetailVpAdapter(Urls,
                                    BaseContext));
                    myItem.item_show_gridview
                            .setOnItemClickListener(new OnItemClickListener() {
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

                    LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            (screenWidth - DimensionPixelUtil.dip2px(
                                    BaseContext, 80)),
                            LayoutParams.WRAP_CONTENT);
                    myItem.item_show_gridview.setLayoutParams(layoutParams);
                }
            } else {// 视频只有一个relativity

                // String VidoPicUrl = blComment.getPre_url();// 视频的第一帧的图片
                myItem.item_show_gridview.setVisibility(View.GONE);
                myItem.item_show_vido_lay.setVisibility(View.VISIBLE);
                myItem.item_show_oneiv.setVisibility(View.GONE);
                // myItem.item_show_vido_control_image
                try {
                    ImageLoaderUtil.Load2(blComment.getPre_url(),
                            myItem.item_show_vido_image, R.drawable.testiv);
                } catch (Exception e) {
                }

                final String VidoPath = blComment.getVid();
                myItem.item_show_vido_control_image
                        .setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                PromptManager.SkipActivity(BaseActivity,
                                        new Intent(BaseActivity,
                                                AVidemplay.class).putExtra(
                                                AVidemplay.VidoKey, VidoPath));
                            }
                        });

            }

        }

        class ShareClick implements OnClickListener {
            private BLComment datBlComment = null;
            private ShowItem showItem;
            private View view;

            public ShareClick() {
                super();
            }

            public ShareClick(BLComment datBlComment, ShowItem showItem,
                              View views) {
                super();
                this.datBlComment = datBlComment;
                this.showItem = showItem;
                this.view = views;
            }

            @Override
            public void onClick(View arg0) {
                // SharePop(datBlComment, view);
                GoddsShowClick(datBlComment);
            }

        }

    }

    /**
     * 点击分享
     *
     * @param datBlComment2
     */
    private void GoddsShowClick(BLComment datBlComment2) {

    }

    class GoodsDetailVpAdapter extends BaseAdapter {

        private List<String> datas;
        private Context mContext;
        private LayoutInflater iLayoutInflater;

        public GoodsDetailVpAdapter(List<String> datas, Context mContext) {
            super();
            this.datas = datas;
            this.mContext = mContext;
            this.iLayoutInflater = LayoutInflater.from(mContext);
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
                ImageLoaderUtil.Load(datas.get(arg0),
                        imageItem.item_show_in_imagview, R.drawable.testiv);

                arg1.setTag(imageItem);
            } else {
                imageItem = (InImageItem) arg1.getTag();
            }

            return arg1;
        }

        class InImageItem {
            ImageView item_show_in_imagview;
        }

    }

    @Override
    protected void NetConnect() {
        NetError.setVisibility(View.GONE);

    }

    @Override
    protected void NetDisConnect() {
        NetError.setVisibility(View.VISIBLE);

    }

    @Override
    protected void SetNetView() {
        SetNetStatuse(NetError);

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
    public void onRefresh() {
        LastId = "";
        IData(Goods_Sid, LOAD_REFRESHING);
    }

    @Override
    public void onLoadMore() {
        IData(Goods_Sid, LOAD_LOADMOREING);
    }

}
