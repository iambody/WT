package io.vtown.WeiTangApp.fragment.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.adapter.BasAdapter;
import io.vtown.WeiTangApp.bean.bcache.BHome;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.RefreshLayout;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.comment.view.select_pic.utils.ViewHolder;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.ui.comment.AWeb;
import io.vtown.WeiTangApp.ui.title.ABrandDetail;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.mynew.ANew;
import io.vtown.WeiTangApp.ui.title.zhuanqu.AZhuanQu;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;
import io.vtown.WeiTangApp.ui.ui.ASouSouGood;
import io.vtown.WeiTangApp.ui.ui.CaptureActivity;

/**
 * Created by datutu on 2016/9/18.
 */
public class FMainHome extends FBase implements RefreshLayout.OnLoadListener, View.OnClickListener {
    //扫描和消息
    private ImageView fragment_maintab_sao_iv, fragment_maintab_new_iv;
    //搜索按钮
    private RelativeLayout fragment_maintab_sou_lay;
    //ls
    private ListView fragment_maintab_ls;

    private RefreshLayout fragment_home_refrash;
    /**
     * adapter
     */
    private NewHoemAp newHoemAp;
    //用户
    private BUser mBUser;
    // 保存全部的View数据
    private BComment mBComment;
    /**
     * 当前是第几页
     */
    private int CurrNumber = 1;

    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_main_home, null);
        mBUser = Spuit.User_Get(BaseContext);
        SetTitleHttpDataLisenter(this);
        IBaseView();

    }


    private void IBaseView() {
        neterrorview = io.vtown.WeiTangApp.comment.util.ViewHolder.get(BaseView, R.id.fragment_main_home_neterrorview);
        neterrorview.setOnClickListener(this);
        CheckNet();

        fragment_home_refrash = (RefreshLayout) BaseView.findViewById(R.id.fragment_home_refrash);
        fragment_maintab_sao_iv = io.vtown.WeiTangApp.comment.util.ViewHolder.get(BaseView, R.id.fragment_maintab_sao_iv);
        fragment_maintab_new_iv = io.vtown.WeiTangApp.comment.util.ViewHolder.get(BaseView, R.id.fragment_maintab_new_iv);
        fragment_maintab_sou_lay = io.vtown.WeiTangApp.comment.util.ViewHolder.get(BaseView, R.id.fragment_maintab_sou_lay);
        fragment_maintab_ls = (ListView) BaseView.findViewById(R.id.fragment_maintab_ls);
        newHoemAp = new NewHoemAp();
        fragment_home_refrash.setOnLoadListener(this);

        fragment_maintab_ls.setAdapter(newHoemAp);


        fragment_maintab_sao_iv.setOnClickListener(this);
        fragment_maintab_new_iv.setOnClickListener(this);
        fragment_maintab_sou_lay.setOnClickListener(this);

        fragment_home_refrash.setColorSchemeResources(R.color.app_fen, R.color.app_fen1, R.color.app_fen2, R.color.app_fen3);
        fragment_maintab_ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                BHome data = (BHome) arg0.getItemAtPosition(arg2);

                if (CheckNet(BaseContext))
                    return;
                // advert_type 类型 1H5首页，2商品详情页，3店铺详情页,4活动详情页
                if (StrUtils.isEmpty(data.getAdvert_type()))
                    return;

                int Type = StrUtils.toInt(data.getAdvert_type());

                switch (Type) {
                    case 1:// HT跳转
                        PromptManager.SkipActivity(BaseActivity, new Intent(
                                BaseActivity, AWeb.class).putExtra(
                                AWeb.Key_Bean,
                                new BComment(data.getUrl(), StrUtils.NullToStr(data
                                        .getAdvert_type_str()))));
                        break;
                    case 2:// 商品详情页
                        PromptManager.SkipActivity(BaseActivity, new Intent(
                                BaseActivity, AGoodDetail.class).putExtra("goodid",
                                data.getSource_id()));
                        // PromptManager.SkipActivity(BaseActivity, new
                        // Intent(BaseContext, APlayer.class));
                        break;
                    case 3:// 店铺详情页!!!!!!!!!!!!!!!!!!!!!!!!需要修改
                        BComment mBComment = new BComment(data.getSource_id(), data
                                .getTitle());
                        if (data.getIs_brand().equals("1")) {// 品牌店铺
                            PromptManager.SkipActivity(BaseActivity, new Intent(
                                    BaseActivity, ABrandDetail.class).putExtra(
                                    BaseKey_Bean, mBComment));
                        } else {// 自营店铺
                            PromptManager.SkipActivity(BaseActivity, new Intent(
                                    BaseActivity, AShopDetail.class).putExtra(
                                    BaseKey_Bean, mBComment));
                        }
                        break;
                    case 4:// 活动详情页
                        BComment mBCommentss = new BComment(data.getSource_id(),
                                data.getTitle());
                        PromptManager.SkipActivity(BaseActivity, new Intent(
                                BaseContext, AZhuanQu.class).putExtra(BaseKey_Bean,
                                mBCommentss));
                        break;
                    default:
                        // default时候直接展示大图
                        break;
                }

            }
        });

        ICacheData();

    }

    /**
     * 开启缓存模式
     */
    private void ICacheData() {
        if (StrUtils.isEmpty(CacheUtil.Home_Get(BaseContext))) {
            PromptManager.showtextLoading(BaseContext, getResources()
                    .getString(R.string.loading));
            GetHomeData(CurrNumber, INITIALIZE);
            return;
        }
        // 开始解析数据
        try {
            mBComment = JSON.parseObject(CacheUtil.Home_Get(BaseContext),
                    BComment.class);
            newHoemAp.FrashData(mBComment.getAdvert());
            if (mBComment.getAdvert() == null
                    || mBComment.getAdvert().size() < 10) {
//                fragment_maintab_ls.hidefoot();
            } else {// 没有更多
//                fragment_maintab_ls.ShowFoot();
            }
        } catch (Exception e) {

            return;
        }
        GetHomeData(CurrNumber, INITIALIZE);
    }


    /**
     * 获取数据
     */
    private void GetHomeData(int Page, int LoadType) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("page", Page + "");
        map.put("pageNum", Constants.PageSize + "");
        map.put("member_id", mBUser.getMember_id());
        FBGetHttpData(map, Constants.HomeUrl, Request.Method.GET, 0, LoadType);
    }

//    @Override
//    public void onRefresh() {
//        CurrNumber = 1;
//        GetHomeData(CurrNumber, REFRESHING);
//    }
//
//    @Override
//    public void onLoadMore() {
//        CurrNumber = CurrNumber + 1;
//        GetHomeData(CurrNumber, LOADMOREING);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_main_home_neterrorview:
                PromptManager.GoToNetSeting(BaseActivity);
                break;
            case R.id.fragment_maintab_sao_iv://扫码
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                        CaptureActivity.class));
                break;
            case R.id.fragment_maintab_new_iv://新消息
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ANew.class));
                break;
            case R.id.fragment_maintab_sou_lay:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                        ASouSouGood.class));
                break;
        }
    }

    @Override
    public void OnLoadMore() {
        CurrNumber = CurrNumber + 1;
        GetHomeData(CurrNumber, LOADMOREING);
    }

    @Override
    public void OnFrash() {
        CurrNumber = 1;
        GetHomeData(CurrNumber, REFRESHING);
    }

    /**
     * 列表的Adapater
     */
    private class NewHoemAp extends BasAdapter {
        private List<BHome> datas = new ArrayList<BHome>();

        public NewHoemAp() {
            super();

        }

        /**
         * 刷新
         */
        public void FrashData(List<BHome> da) {
            this.datas = da;
            notifyDataSetChanged();
        }

        /**
         * 添加刷新
         */
        public void AddFrash(List<BHome> daas) {
            if (null == daas)
                return;
            this.datas.addAll(daas);
            notifyDataSetChanged();
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
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            Myitem myItem = null;
            if (arg1 == null) {
                myItem = new Myitem();
                arg1 = LayoutInflater.from(BaseContext).inflate(
                        R.layout.item_home_brand, null);
                myItem.item_home_brand_iv = (ImageView) arg1
                        .findViewById(R.id.item_home_brand_iv);

                ViewGroup.LayoutParams lp = myItem.item_home_brand_iv
                        .getLayoutParams();
                lp.width = screenWidth;
                lp.height = screenWidth / 2;
                myItem.item_home_brand_iv.setScaleType(ImageView.ScaleType.FIT_XY);
                myItem.item_home_brand_iv.setLayoutParams(lp);

                arg1.setTag(myItem);
            } else {
                myItem = (Myitem) arg1.getTag();
            }

            final Myitem myitem2 = myItem;
            final int Postion = arg0;
            final BHome data = datas.get(arg0);

//			 ImageLoaderUtil.Load22(datas.get(arg0).getPic_path(),
//			 myItem.item_home_brand_iv, R.drawable.error_iv1);


            String tag = (String) myitem2.item_home_brand_iv.getTag();
            if (tag == null || !tag.equals(datas.get(Postion).getPic_path())) {
                ImageLoader.getInstance().displayImage(
                        datas.get(Postion).getPic_path(),
                        new ImageViewAware(myitem2.item_home_brand_iv, false), ImageLoaderUtil.GetDisplayOptions(R.drawable.error_iv1),
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
                                view.setTag(datas.get(
                                        Postion).getPic_path());// 确保下载完成再打tag.
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }

                        });
            }


            return arg1;
        }

        class Myitem {
            ImageView item_home_brand_iv;
        }

        List<String> GetLs(List<BHome> daass) {
            List<String> mStrings = new ArrayList<String>();

            for (int i = 0; i < daass.size(); i++) {
                mStrings.add(daass.get(i).getPic_path());
            }
            return mStrings;

        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
        }
    }


    @Override
    public void InitCreate(Bundle d) {
        Log.i("mainhome", "FMainHome===>onCreate ");
    }

    @Override
    public void getResult(int Code, String Msg, BComment Data) {
        {
            switch (Data.getHttpLoadType()) {
                case INITIALIZE:
                    CacheUtil.Home_Save(BaseContext, Data.getHttpResultStr());
                    if (StrUtils.isEmpty(Data.getHttpResultStr())) {// 数据不存在
                        return;
                    }
                    mBComment = JSON.parseObject(Data.getHttpResultStr(),
                            BComment.class);
                    Spuit.ShopBusNumber_Save(BaseContext, mBComment.getCart_num());
                    Send(mBComment.getCart_num());
                    newHoemAp.FrashData(mBComment.getAdvert());

                    if (mBComment.getAdvert() == null
                            || mBComment.getAdvert().size() < 10) {
//                        fragment_maintab_ls.hidefoot();
                        fragment_home_refrash.setCanLoadMore(false);
                    } else {// 没有更多
//                        fragment_maintab_ls.ShowFoot();
                        fragment_home_refrash.setCanLoadMore(true);
                    }
                    break;
                case REFRESHING:
//                    fragment_maintab_ls.stopRefresh();
                    fragment_home_refrash.setRefreshing(false);
                    if (StrUtils.isEmpty(Data.getHttpResultStr())) {// 数据不存在
                        return;
                    }
                    mBComment = JSON.parseObject(Data.getHttpResultStr(),
                            BComment.class);
                    if (mBComment.getAdvert() == null
                            || mBComment.getAdvert().size() < 10) {
//                        fragment_maintab_ls.hidefoot();
                        fragment_home_refrash.setCanLoadMore(false);
                    } else {// 没有更多
//                        fragment_maintab_ls.ShowFoot();
                        fragment_home_refrash.setCanLoadMore(true);
                    }

                    newHoemAp.FrashData(mBComment.getAdvert());
                    break;
                case LOADMOREING:
//                    fragment_maintab_ls.stopLoadMore();
                    fragment_home_refrash.setLoading(false);
                    JSONObject mJsonObject = null;
                    try {
                        mJsonObject = new JSONObject(Data.getHttpResultStr());
                        if (StrUtils.isEmpty(mJsonObject.getString("advert")))
                            return;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (StrUtils.isEmpty(Data.getHttpResultStr())) {// 数据不存在
                        return;
                    }
                    mBComment = JSON.parseObject(Data.getHttpResultStr(),
                            BComment.class);
                    if (mBComment.getAdvert() == null
                            || mBComment.getAdvert().size() < 10) {
//                        fragment_maintab_ls.hidefoot();
                        fragment_home_refrash.setCanLoadMore(false);
                    } else {// 没有更多
//                        fragment_maintab_ls.ShowFoot();
                        fragment_home_refrash.setCanLoadMore(true);
                    }
                    newHoemAp.AddFrash(mBComment.getAdvert());
                    break;
                default:
                    break;
            }

        }
    }

    /**
     * 购物车的提示
     */
    public void Send(int Number) {
        BMessage bMessage = new BMessage(BMessage.Tage_MainTab_ShopBus);
        bMessage.setTabShopBusNumber(Number);
        EventBus.getDefault().post(bMessage);
    }

    @Override
    public void onError(String error, int LoadType) {
        switch (LoadType) {
            case INITIALIZE:
                break;
            case LOADMOREING:
                fragment_home_refrash.setLoading(false);
                break;
            case REFRESHING:
                fragment_home_refrash.setRefreshing(false);
                break;

        }
    }
}
