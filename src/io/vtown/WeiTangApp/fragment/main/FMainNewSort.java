package io.vtown.WeiTangApp.fragment.main;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import butterknife.OnClick;
import butterknife.Unbinder;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.main_sort.BCMainSort;
import io.vtown.WeiTangApp.bean.bcomment.easy.main_sort.BLBannerListData;
import io.vtown.WeiTangApp.bean.bcomment.easy.main_sort.BLGoods;
import io.vtown.WeiTangApp.bean.bcomment.easy.main_sort.BLGoodsListData;
import io.vtown.WeiTangApp.bean.bcomment.easy.mainsort.BSortCategory;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.RoundAngleImageView;
import io.vtown.WeiTangApp.comment.view.ScrollDistanceScrollView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.comment.view.custom.horizontalscroll.CustHScrollView;
import io.vtown.WeiTangApp.comment.view.custom.horizontalscroll.HBaseAdapter;
import io.vtown.WeiTangApp.comment.view.custom.horizontalscroll.HorizontalScrollMenu;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.ui.AGoodSort;
import io.vtown.WeiTangApp.ui.comment.AWeb;
import io.vtown.WeiTangApp.ui.title.ABrandDetail;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.zhuanqu.AZhuanQu;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;
import io.vtown.WeiTangApp.ui.ui.ASouSouGood;

/**
 * Created by datutu on 2016/12/19.
 */

public class FMainNewSort extends FBase implements OnLoadMoreListener, OnRefreshListener, AdapterView.OnItemClickListener {

    @BindView(R.id.f_main_new_new_sort_sou)
    ImageView fMainNewNewSortSou;
    @BindView(R.id.f_sort_new_container)
    HorizontalScrollMenu fSortNewContainer;
    @BindView(R.id.f_sort_new_horizontalscrollview)
    HorizontalScrollView fSortNewHorizontalscrollview;
    @BindView(R.id.f_sort_new_horizontalscrollview_lay)
    LinearLayout fSortNewHorizontalscrollviewLay;
    @BindView(R.id.fragment_main_new_sort_data_layout)
    RelativeLayout fragment_main_new_sort_data_layout;
    @BindView(R.id.rl_to_sort_top)
    RelativeLayout rl_to_sort_top;
    @BindView(R.id.swipe_target)
    ScrollDistanceScrollView swipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;
    //banner ListView
    @BindView(R.id.fragment_main_new_sort_bannerlist)
    CompleteListView fragment_main_new_sort_bannerlist;
    //商品GridView
    @BindView(R.id.fragment_main_new_sort_goodslist)
    CompleteGridView fragment_main_new_sort_goodslist;
    private static final int TYPE_ALL = 0x11;
    private static final int TYPE_OTHER = 0x12;
    private int current_type = TYPE_ALL;
    private String current_category_id = "0";

    private int mPage = 1;


    private List<BSortCategory> MainSortCategory = new ArrayList<>();
    private List<String> MainSortCateorys;
    private List<BSortCategory> cate_data = new ArrayList<BSortCategory>();
    private Unbinder mBinder;
    private GoodsListAdapter goodsListAdapter;
    private BannerListAdapter bannerListAdapter;
    private BCMainSort sort_data;
    private View fragment_main_new_sort_nodata_layout;
    private boolean IShow = true;

    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_main_new_sort, null);
        SetTitleHttpDataLisenter(this);
        mBinder = ButterKnife.bind(this, BaseView);
        IView();
        ICache();

    }

    private void ICache() {
        String mainsort = CacheUtil.Main_Sort_Get(BaseContext);
        if (StrUtils.isEmpty(mainsort)) {
            PromptManager.showtextLoading(BaseContext, getString(R.string.xlistview_header_hint_loading));
            return;
        } else {
            sort_data = JSON.parseObject(mainsort, BCMainSort.class);
            setData(sort_data);
            if (StrUtils.isEmpty(sort_data.getGoodslist())) {
                fragment_main_new_sort_goodslist.setVisibility(View.GONE);
                return;
            } else {
                fragment_main_new_sort_goodslist.setVisibility(View.VISIBLE);
            }
            List<BLGoodsListData> good_list = JSON.parseArray(sort_data.getGoodslist(), BLGoodsListData.class);
            setGoodsListData(good_list);
        }
    }

    private void IView() {

        fragment_main_new_sort_nodata_layout = BaseView.findViewById(R.id.fragment_main_new_sort_nodata_layout);
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        IBaseSort();//一级分类
        bannerListAdapter = new BannerListAdapter();
        fragment_main_new_sort_bannerlist.setAdapter(bannerListAdapter);
        goodsListAdapter = new GoodsListAdapter();
        fragment_main_new_sort_goodslist.setAdapter(goodsListAdapter);
        fragment_main_new_sort_goodslist.setOnItemClickListener(this);
        fragment_main_new_sort_nodata_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IData(INITIALIZE);
            }
        });

        swipeTarget
                .SetOnGetDistanceListener(new ScrollDistanceScrollView.OnGetDistanceListener() {

                    @Override
                    public void getDistance(int distance) {
                        if (distance < screenHeight) {
                            rl_to_sort_top.setVisibility(View.GONE);

                        } else {
                            rl_to_sort_top.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

    private void IData(int loadtype) {
        HashMap<String, String> map = new HashMap<>();
        map.put("category_id", current_category_id);
        map.put("page", mPage + "");
        map.put("pagesize", Constants.PageSize2 + "");
        map.put("member_id", Spuit.User_Get(BaseContext).getMember_id());
        FBGetHttpData(map, Constants.Main_New_Sort, Request.Method.GET, 0, loadtype);
    }

    private void IBaseSort() {
        MainSortCategory = JSON.parseArray(CacheUtil.HomeSort_Get(BaseContext), BSortCategory.class);
        MainSortCateorys = new ArrayList<>();
        MainSortCateorys.add("全部");
        for (int i = 0; i < MainSortCategory.size(); i++) {
            MainSortCateorys.add(MainSortCategory.get(i).getCate_name());
        }

        fSortNewContainer.setMenuItemPaddingLeft(50);
        fSortNewContainer.setMenuItemPaddingRight(50);
        fSortNewContainer.setAdapter(new MainSortMenuAdapter());

        //二级分类测试
    }


    /*
    * 设置二级分类数据
    * */
    private void setCategotyData(List<BSortCategory> data) {
        fSortNewHorizontalscrollviewLay.removeAllViews();
        int view_width = (screenWidth) / 4;
        for (int i = 0; i < data.size(); i++) {
            LinearLayout.LayoutParams txtparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(view_width, LinearLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams imgparams = new LinearLayout.LayoutParams(DimensionPixelUtil.dip2px(BaseContext, 70), DimensionPixelUtil.dip2px(BaseContext, 70));
            int dip2px_5 = DimensionPixelUtil.dip2px(BaseContext, 5);
            //imgparams.setMargins(dip2px_5, dip2px_5, dip2px_5, dip2px_5);
            //txtparams.setMargins(0, 8, 0, 0);
            int dip2px_10 = DimensionPixelUtil.dip2px(BaseContext, 10);
            int dip2px_16 = DimensionPixelUtil.dip2px(BaseContext, 18);
            LinearLayout layout = new LinearLayout(BaseContext);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(dip2px_10, 0, dip2px_10, dip2px_10);
            layout.setGravity(Gravity.CENTER);
            txtparams.gravity = Gravity.CENTER;
            imgparams.gravity = Gravity.CENTER;
            ImageView img = new ImageView(BaseContext);
            img.setScaleType(ImageView.ScaleType.FIT_CENTER);
            img.setPadding(dip2px_16, dip2px_16, dip2px_16, 0);
            ImageLoaderUtil.Load2(data.get(i).getCate_icon(), img, R.drawable.error_iv2);
            TextView txt = new TextView(BaseContext);
            txt.setText(data.get(i).getCate_name());
            txt.setTextColor(getResources().getColor(R.color.app_gray));
            layout.setLayoutParams(layoutparams);
            img.setLayoutParams(imgparams);
            txt.setLayoutParams(txtparams);
            layout.addView(img);
            layout.addView(txt);
            fSortNewHorizontalscrollviewLay.addView(layout);
            //fSortNewHorizontalscrollviewLay.addView(txt);
            setCategoryClick(data, i, layout);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (IShow) {
            Log.i("homewave", "显示");
            if (swipeToLoadLayout.isRefreshing()) swipeToLoadLayout.setRefreshing(false);
            PromptManager.closeLoading();
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.i("homewave", "隐藏");
            if (swipeToLoadLayout.isRefreshing()) swipeToLoadLayout.setRefreshing(false);
            IShow = false;

        } else {
            Log.i("homewave", "显示");
            if (swipeToLoadLayout.isRefreshing()) swipeToLoadLayout.setRefreshing(false);
            PromptManager.closeLoading();
            IShow = true;
        }

    }

    /*
    * 设置BannerList数据
    * */
    private void setBannerListData(List<BLBannerListData> banner_data) {
        bannerListAdapter.setData(banner_data);
    }

    /*
    * 设置GoodsList数据
    * */
    private void setGoodsListData(List<BLGoodsListData> good_list) {
        goodsListAdapter.setData(good_list);
    }

    private void setGoodsListMoreData(List<BLGoodsListData> good_list) {
        goodsListAdapter.addMore(good_list);
    }

    /*
    * GoodsList加载更多
    * */
    private void setData(BCMainSort sort_data) {
        if (TYPE_OTHER == current_type) {
            if (!StrUtils.isEmpty(sort_data.getCategory())) {
                cate_data = JSON.parseArray(sort_data.getCategory(), BSortCategory.class);
                if (cate_data.size() > 0) {
                    fSortNewHorizontalscrollviewLay.setVisibility(View.VISIBLE);
                    setCategotyData(cate_data);
                }
            } else {
                fSortNewHorizontalscrollviewLay.setVisibility(View.GONE);
            }
        }
        if (!StrUtils.isEmpty(sort_data.getBannerlist())) {
            List<BLBannerListData> banner_data = JSON.parseArray(sort_data.getBannerlist(), BLBannerListData.class);
            fragment_main_new_sort_bannerlist.setVisibility(View.VISIBLE);
            setBannerListData(banner_data);
        } else {
            fragment_main_new_sort_bannerlist.setVisibility(View.GONE);
        }

    }

    @Override
    public void InitCreate(Bundle d) {

    }

    @Override
    public void getResult(int Code, String Msg, BComment Data) {


        switch (Data.getHttpLoadType()) {
            case INITIALIZE:
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    fragment_main_new_sort_data_layout.setVisibility(View.GONE);
                    fragment_main_new_sort_nodata_layout.setVisibility(View.VISIBLE);
                    fragment_main_new_sort_nodata_layout.setClickable(false);
                    return;
                }
                fragment_main_new_sort_nodata_layout.setVisibility(View.GONE);
                fragment_main_new_sort_data_layout.setVisibility(View.VISIBLE);

                sort_data = JSON.parseObject(Data.getHttpResultStr(), BCMainSort.class);
                if (TYPE_ALL == current_type) {
                    CacheUtil.Main_Sort_Save(BaseContext, Data.getHttpResultStr());
                }
                setData(sort_data);
                if (StrUtils.isEmpty(sort_data.getGoodslist())) {
                    fragment_main_new_sort_goodslist.setVisibility(View.GONE);
                    return;
                } else {
                    fragment_main_new_sort_goodslist.setVisibility(View.VISIBLE);
                }
                List<BLGoodsListData> good_list = JSON.parseArray(sort_data.getGoodslist(), BLGoodsListData.class);
                setGoodsListData(good_list);
                if (good_list.size() == Constants.PageSize2) {
                    swipeToLoadLayout.setLoadMoreEnabled(true);
                } else {
                    swipeToLoadLayout.setLoadMoreEnabled(false);
                }
                break;

            case REFRESHING:
                swipeToLoadLayout.setRefreshing(false);
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    PromptManager.ShowCustomToast(BaseContext, "没有最新的了");
                    return;
                }
                sort_data = JSON.parseObject(Data.getHttpResultStr(), BCMainSort.class);
                setData(sort_data);
                if (StrUtils.isEmpty(sort_data.getGoodslist())) {
                    fragment_main_new_sort_goodslist.setVisibility(View.GONE);
                    return;
                } else {
                    fragment_main_new_sort_goodslist.setVisibility(View.VISIBLE);
                }
                List<BLGoodsListData> good_list1 = JSON.parseArray(sort_data.getGoodslist(), BLGoodsListData.class);
                setGoodsListData(good_list1);
                if (good_list1.size() == Constants.PageSize2) {
                    swipeToLoadLayout.setLoadMoreEnabled(true);
                } else {
                    swipeToLoadLayout.setLoadMoreEnabled(false);
                }
                break;

            case LOADMOREING:
                swipeToLoadLayout.setLoadingMore(false);
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    PromptManager.ShowCustomToast(BaseContext, "没有更多了");
                    return;
                }
                sort_data = JSON.parseObject(Data.getHttpResultStr(), BCMainSort.class);
                if (mPage != 1) {
                    if (StrUtils.isEmpty(sort_data.getGoodslist())) {
                        fragment_main_new_sort_goodslist.setVisibility(View.GONE);
                        return;
                    } else {
                        fragment_main_new_sort_goodslist.setVisibility(View.VISIBLE);
                    }
                    List<BLGoodsListData> good_list2 = JSON.parseArray(sort_data.getGoodslist(), BLGoodsListData.class);
                    setGoodsListMoreData(good_list2);
                    if (good_list2.size() == Constants.PageSize2) {
                        swipeToLoadLayout.setLoadMoreEnabled(true);
                    } else {
                        swipeToLoadLayout.setLoadMoreEnabled(false);
                    }
                }
                break;
        }
    }

    @Override
    public void onError(String error, int LoadType) {
        PromptManager.ShowCustomToast(BaseContext, error);
        switch (LoadType) {
            case INITIALIZE:
                fragment_main_new_sort_data_layout.setVisibility(View.GONE);
                fragment_main_new_sort_nodata_layout.setVisibility(View.VISIBLE);
                fragment_main_new_sort_nodata_layout.setClickable(true);
                ShowErrorCanLoad(getString(R.string.error_null_noda));
                break;

            case REFRESHING:
                swipeToLoadLayout.setLoadingMore(false);
                break;

            case LOADMOREING:
                swipeToLoadLayout.setLoadingMore(false);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinder.unbind();

    }

    @Override
    public void onLoadMore() {
        mPage++;
        IData(LOADMOREING);
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        IData(REFRESHING);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BLGoodsListData good_data = (BLGoodsListData) goodsListAdapter.getItem(position);
        PromptManager.SkipActivity(BaseActivity, new Intent(
                BaseContext, AGoodDetail.class).putExtra("goodid",
                good_data.getId()));
    }


    class MainSortMenuAdapter extends HBaseAdapter {
        @Override
        public List<String> getMenuItems() {
            return MainSortCateorys;
        }

        @Override
        public List<View> getContentViews() {
            List<View> views = new ArrayList<View>();
            for (String str : getMenuItems()) {
                View v = LayoutInflater.from(BaseContext).inflate(
                        R.layout.content_view, null);
                TextView tv = (TextView) v.findViewById(R.id.tv_content);
                tv.setText(str);
                LinearLayout.LayoutParams ps = new LinearLayout.LayoutParams(
                        120, DimensionPixelUtil.dip2px(BaseContext, 50));
                ps.setMargins(16, 10, 16, 10);
                v.setLayoutParams(ps);
                views.add(v);
            }
            return views;
        }

        @Override
        public void onPageChanged(int position, boolean visitStatus) {
            // PromptManager.ShowCustomToast(BaseContext, String.format("第%s页", position + ""));
            if (swipeToLoadLayout.isRefreshing()) swipeToLoadLayout.setRefreshing(false);
            if (0 == position) {
                fSortNewHorizontalscrollviewLay.setVisibility(View.GONE);//全部就不显示二级分类
                swipeTarget.smoothScrollTo(0, 0);
                current_category_id = "0";
                mPage = 1;
                IData(INITIALIZE);
                current_type = TYPE_ALL;
            } else {
                current_category_id = MainSortCategory.get(position - 1).getId();
                swipeTarget.smoothScrollTo(0, 0);
                mPage = 1;
                IData(INITIALIZE);
                fSortNewHorizontalscrollviewLay.setVisibility(View.VISIBLE);

                fSortNewHorizontalscrollview.smoothScrollTo(0, 0);


                current_type = TYPE_OTHER;
            }

        }
    }

    @OnClick({R.id.f_main_new_new_sort_sou, R.id.rl_to_sort_top})
    public void onClick(View V) {
        switch (V.getId()) {
            case R.id.f_main_new_new_sort_sou:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, ASouSouGood.class));
                break;

            case R.id.rl_to_sort_top:
                swipeTarget.fullScroll(View.FOCUS_UP);
                break;


        }
    }


    class BannerListAdapter extends BaseAdapter {
        private List<BLBannerListData> banner_data = new ArrayList<BLBannerListData>();
        private LayoutInflater inflater;

        public BannerListAdapter() {
            super();
            this.inflater = LayoutInflater.from(BaseContext);
        }

        @Override
        public int getCount() {
            return banner_data.size();
        }

        @Override
        public Object getItem(int position) {
            return banner_data.get(position);
        }

        public void setData(List<BLBannerListData> banner_data) {
            this.banner_data = banner_data;
            this.notifyDataSetChanged();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BannerListHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_main_sort_banner_list, null);
                holder = new BannerListHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (BannerListHolder) convertView.getTag();
            }
            BLBannerListData blBannerListData = banner_data.get(position);
            ImageLoaderUtil.Load2(blBannerListData.getPic_path(), holder.itemMainSortBannerListPic, R.drawable.error_iv1);
            StrUtils.SetTxt(holder.itemMainSortBannerListTitle, blBannerListData.getTitle());
            List<BLGoods> goods = blBannerListData.getGoods();
            if (goods != null && goods.size() > 0) {
                holder.layout_line.setVisibility(View.VISIBLE);
                setGoosLayout(blBannerListData, goods, holder.itemMainSortBannerListGoodsLayout);

            } else {
                holder.layout_line.setVisibility(View.GONE);
                holder.itemMainSortBannerListGoodsLayout.removeAllViews();

            }

            setBannerClick(blBannerListData, holder.itemMainSortBannerListPic);
            return convertView;
        }

        private void setBannerClick(final BLBannerListData data, ImageView itemMainSortBannerListPic) {
            itemMainSortBannerListPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CheckNet(BaseContext))
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
                                    BaseContext, AGoodDetail.class).putExtra("goodid",
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
        }

        private void setGoosLayout(BLBannerListData data, List<BLGoods> goods, LinearLayout layout) {
            layout.removeAllViews();
            int view_width = (screenWidth - DimensionPixelUtil.dip2px(BaseContext, 0)) / 4;
            int good_size = 0;
            if (goods.size() > 2) {
                good_size = goods.size() + 1;
            } else {
                good_size = goods.size();
            }
            LinearLayout view = null;
            LinearLayout.LayoutParams viewparams = new LinearLayout.LayoutParams(view_width, LinearLayout.LayoutParams.WRAP_CONTENT);// DimensionPixelUtil.dip2px(BaseContext, 120)
            LinearLayout.LayoutParams more_params = new LinearLayout.LayoutParams(view_width, view_width);
            LinearLayout.LayoutParams img_params = new LinearLayout.LayoutParams(view_width, view_width);
            viewparams.setMargins(DimensionPixelUtil.dip2px(BaseContext, 3), DimensionPixelUtil.dip2px(BaseContext, 5), DimensionPixelUtil.dip2px(BaseContext, 3), DimensionPixelUtil.dip2px(BaseContext, 5));
            more_params.setMargins(DimensionPixelUtil.dip2px(BaseContext, 3), DimensionPixelUtil.dip2px(BaseContext, 5), DimensionPixelUtil.dip2px(BaseContext, 9), DimensionPixelUtil.dip2px(BaseContext, 5));
            for (int i = 0; i < good_size; i++) {
                if (i != goods.size()) {
                    BLGoods blGoods = goods.get(i);
                    view = (LinearLayout) inflater.inflate(R.layout.item_main_sort_banner_goods, null);
                    // view.setPadding(DimensionPixelUtil.dip2px(BaseContext, 5), DimensionPixelUtil.dip2px(BaseContext, 5), DimensionPixelUtil.dip2px(BaseContext, 5), DimensionPixelUtil.dip2px(BaseContext, 5));
                    //view.setLayoutParams(viewparams);
                    ImageView item_main_sort_banner_goods_img = (ImageView) view.findViewById(R.id.item_main_sort_banner_goods_img);
                    TextView item_main_sort_banner_goods_name = (TextView) view.findViewById(R.id.item_main_sort_banner_goods_name);
                    TextView item_main_sort_banner_goods_price = (TextView) view.findViewById(R.id.item_main_sort_banner_goods_price);
                    item_main_sort_banner_goods_img.setLayoutParams(img_params);
                    ImageLoaderUtil.Load2(blGoods.getCover(), item_main_sort_banner_goods_img, R.drawable.error_iv2);
                    StrUtils.SetTxt(item_main_sort_banner_goods_name, StrUtils.isEmpty(blGoods.getShorttitle()) ? blGoods.getTitle() : blGoods.getShorttitle());
                    StrUtils.SetTxt(item_main_sort_banner_goods_price, StrUtils.SetTextForMony(blGoods.getSell_price()) + "元");
                    view.setLayoutParams(viewparams);
                } else {//添加查看更布局
                    view = (LinearLayout) inflater.inflate(R.layout.item_main_sort_banner_more_goods, null);
                    view.setLayoutParams(more_params);
                }
                setBannerGoodsClick(data, goods, view, i);
                layout.addView(view);

            }
        }

        private void setBannerGoodsClick(final BLBannerListData data, final List<BLGoods> goods, LinearLayout view, final int posotion) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (posotion == goods.size() && posotion >= 3) {//查看更多 第三个以及第三个以后的权威查看更多 1或者2个商品没有查看更多
//                        BComment mBCommentss = new BComment(data.getId(),
//                                data.getTitle());
//                        PromptManager.SkipActivity(BaseActivity, new Intent(
//                                BaseContext, AZhuanQu.class).putExtra(BaseKey_Bean,
//                                mBCommentss));
                        //分类******点击******
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
                                        BaseContext, AGoodDetail.class).putExtra("goodid",
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


                        //分类********点击***********
                    } else {//点击商品跳转
                        PromptManager.SkipActivity(BaseActivity, new Intent(
                                BaseContext, AGoodDetail.class).putExtra("goodid",
                                goods.get(posotion).getGoods_id()));
                    }
                }
            });
        }


    }

    class BannerListHolder {
        @BindView(R.id.item_main_sort_banner_list_pic)
        ImageView itemMainSortBannerListPic;
        @BindView(R.id.item_main_sort_banner_list_title)
        TextView itemMainSortBannerListTitle;
        @BindView(R.id.item_main_sort_banner_list_goods_layout)
        LinearLayout itemMainSortBannerListGoodsLayout;
        @BindView(R.id.layout_line)
        LinearLayout layout_line;
        @BindView(R.id.item_main_sort_banner_list_goods_horizontalscrollview)
        CustHScrollView item_main_sort_banner_list_goods_horizontalscrollview;


        BannerListHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    class GoodsListAdapter extends BaseAdapter {

        private List<BLGoodsListData> good_data = new ArrayList<BLGoodsListData>();

        private LayoutInflater inflater;

        public GoodsListAdapter() {
            super();

            this.inflater = LayoutInflater.from(BaseContext);
        }

        public void setData(List<BLGoodsListData> good_data) {
            this.good_data = good_data;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return good_data.size();
        }

        public void addMore(List<BLGoodsListData> more_data) {
            this.good_data.addAll(more_data);
            this.notifyDataSetChanged();
        }

        @Override
        public Object getItem(int position) {
            return good_data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GoodsListHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_main_sort_goods_list, null);
                holder = new GoodsListHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (GoodsListHolder) convertView.getTag();
            }
            BLGoodsListData blGoodsListData = good_data.get(position);
            ImageLoaderUtil.Load2(blGoodsListData.getCover(), holder.itemMainSortGoodsImg, R.drawable.error_iv2);
            StrUtils.SetTxt(holder.itemMainSortGoodsTitle, blGoodsListData.getTitle());
            if (Integer.parseInt(blGoodsListData.getSales()) > 0) {
                holder.itemMainSortGoodsSale.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(holder.itemMainSortGoodsSale, "销量：" + blGoodsListData.getSales());
            } else {
                holder.itemMainSortGoodsSale.setVisibility(View.GONE);
            }

            if (Integer.parseInt(blGoodsListData.getScore()) > 0) {
                holder.itemMainSortGoodsScore.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(holder.itemMainSortGoodsScore, "积分：" + blGoodsListData.getScore());
            } else {
                holder.itemMainSortGoodsScore.setVisibility(View.VISIBLE);
            }

            StrUtils.SetTxt(holder.itemMainSortGoodsImgPrice, StrUtils.SetTextForMony(blGoodsListData.getSell_price()) + "元");

            if (Integer.parseInt(blGoodsListData.getOrig_price()) > 0) {
                holder.itemMainSortGoodsImgOldPrice.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(holder.itemMainSortGoodsImgOldPrice, "原价" + StrUtils.SetTextForMony(blGoodsListData.getOrig_price()));
                holder.itemMainSortGoodsImgOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                holder.itemMainSortGoodsImgOldPrice.setVisibility(View.VISIBLE);
            }
            return convertView;
        }


    }

    class GoodsListHolder {
        @BindView(R.id.item_main_sort_goods_img)
        ImageView itemMainSortGoodsImg;
        @BindView(R.id.item_main_sort_goods_title)
        TextView itemMainSortGoodsTitle;
        @BindView(R.id.item_main_sort_goods_sale)
        TextView itemMainSortGoodsSale;
        @BindView(R.id.item_main_sort_goods_score)
        TextView itemMainSortGoodsScore;
        @BindView(R.id.item_main_sort_goods_img_price)
        TextView itemMainSortGoodsImgPrice;
        @BindView(R.id.item_main_sort_goods_img_old_price)
        TextView itemMainSortGoodsImgOldPrice;

        GoodsListHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    /*
  * 分类的点击事件
  * */
    private void setCategoryClick(final List<BSortCategory> data, final int position, LinearLayout layout) {
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AGoodSort.Tag_CurrentOutSortId //一级分类
//                AGoodSort.Tag_SecondSortId //二级分类
                BSortCategory bSortCategory = data.get(position);
                Intent intent = new Intent(BaseContext, AGoodSort.class);
                intent.putExtra(AGoodSort.Tag_CurrentOutSortId, current_category_id);
                intent.putExtra(AGoodSort.Tag_SecondSortId, bSortCategory.getId());
                PromptManager.SkipActivity(BaseActivity, intent);
            }
        });
    }

}
