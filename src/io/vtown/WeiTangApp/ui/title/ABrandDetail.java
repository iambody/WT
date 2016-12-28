package io.vtown.WeiTangApp.ui.title;

import io.vtown.WeiTangApp.BaseApplication;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.shop.BShopBase;
import io.vtown.WeiTangApp.bean.bcomment.easy.shop.BShopCatory;
import io.vtown.WeiTangApp.bean.bcomment.easy.shop.BShopGoods;
import io.vtown.WeiTangApp.bean.bcomment.easy.shopbrand.BLBrandGood;
import io.vtown.WeiTangApp.bean.bcomment.easy.shopbrand.BShopBrand;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.ImageCycleView;
import io.vtown.WeiTangApp.comment.view.ImageCycleView.ImageCycleViewListener;
import io.vtown.WeiTangApp.comment.view.ScrollBottomScrollView;
import io.vtown.WeiTangApp.comment.view.ScrollBottomScrollView.ScrollBottomListener;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.custom.PullView;
import io.vtown.WeiTangApp.comment.view.listview.HorizontalListView;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.AShopGoodSou;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;
import io.vtown.WeiTangApp.ui.comment.im.AChatLoad;
import io.vtown.WeiTangApp.ui.title.center.myshow.AOtherShow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.ui.title.center.myshow.ARecyclerOtherShow;
import io.vtown.WeiTangApp.ui.title.zhuanqu.ABrandShopShare;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @author home页面点击跳转品牌详情页
 * @version 创建时间：2016-4-27 下午3:23:22
 */
public class ABrandDetail extends ATitleBase implements PullView.OnFooterRefreshListener {
    private PullView brandscroll;
    /**
     * 正常时候需要显示
     */
    private LinearLayout brand_detail_out_lay;

    /**
     * error的view
     */
    private View brand_detail_nodata_lay;
    /**
     * 头像
     */
    private CircleImageView brand_detail_brand_iv;

    /**
     * 名字
     */
    private TextView brand_detail_brand_name;
    /**
     * 查看show
     */
    private ImageView brand_detail_brand_lookshow_iv;
    /**
     * 查看品牌详情
     */
    private ImageView brand_detail_brand_brandinf_iv;
    /*
    搜搜
     */
//    private ImageView brand_detail_brand_sou_iv;
    private ImageView brand_detail_brand_collect_iv;

    /**
     * 横向分类的Ls
     */
    private HorizontalListView brand_detail_horizontal_ls;
    /**
     * 横线滑动Ls的Ap
     */
    private BrandLsAp brandLsAp;
//    private ImageView right_right_iv;
    /**
     * 推荐品牌
     */
    private CompleteGridView brand_detail_grid;
    /**
     * 推荐品牌AP
     */
    private RecommendAp recommendAp;
    /**
     * 申请代理
     */
//    private TextView brand_detail_apply;
    /**
     * pager
     */
//    private ImageCycleView branddetail_banner;

    /**
     * 分页加载时候的当前页
     */
    private int CurrentPage = 1;
    /**
     * 全部当前店铺的信息
     */
    private BShopBrand mBComment;

    /**
     * 用户信息
     */
    private BUser user_Get;
    /**
     * 是否收藏
     */
    private boolean IsCollect = false;

    // 当前分类的id号
    private String Current_Category_id;

    //返回按钮
    private ImageView brandshop_back_iv;
    //搜索布局按钮
    private RelativeLayout brandshop_sou_lay;
    //联系店铺
    private ImageView brandshop_connect_iv;

//	private int LateItem;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_brand_detail);
        user_Get = Spuit.User_Get(BaseContext);
        EventBus.getDefault().register(this, "ReciverMessage", BMessage.class);
        IView();
        IData();
    }

    /**
     * view的初始化
     */
    private void IView() {

        brand_detail_brand_collect_iv = (ImageView) findViewById(R.id.brand_detail_brand_collect_iv);
        brandshop_back_iv = (ImageView) findViewById(R.id.brandshop_back_iv);
        brandshop_sou_lay = (RelativeLayout) findViewById(R.id.brandshop_sou_lay);
        brandshop_connect_iv = (ImageView) findViewById(R.id.brandshop_connect_iv);
//        brand_detail_brand_sou_iv = (ImageView) findViewById(R.id.brand_detail_brand_sou_iv);
        brandscroll = (PullView) findViewById(R.id.brandscroll);
        brandscroll.setOnFooterRefreshListener(this);
//        branddetail_banner = (ImageCycleView) findViewById(R.id.branddetail_banner);
        brand_detail_out_lay = (LinearLayout) findViewById(R.id.brand_detail_out_lay);
        brand_detail_nodata_lay = findViewById(R.id.brand_detail_nodata_lay);
        brand_detail_nodata_lay.setOnClickListener(this);
        IDataView(brand_detail_out_lay, brand_detail_nodata_lay,
                NOVIEW_INITIALIZE);

        brand_detail_brand_iv = (CircleImageView) findViewById(R.id.brand_detail_brand_iv);
        brand_detail_brand_name = (TextView) findViewById(R.id.brand_detail_brand_name);
        brand_detail_brand_lookshow_iv = (ImageView) findViewById(R.id.brand_detail_brand_lookshow_iv);
        brand_detail_brand_brandinf_iv = (ImageView) findViewById(R.id.brand_detail_brand_brandinf_iv);
        brand_detail_horizontal_ls = (HorizontalListView) findViewById(R.id.brand_detail_horizontal_ls);
        brand_detail_grid = (CompleteGridView) findViewById(R.id.brand_detail_grid);

        brand_detail_brand_brandinf_iv.setOnClickListener(this);
        brand_detail_brand_lookshow_iv.setOnClickListener(this);
        brand_detail_brand_iv.setOnClickListener(this);

//        brand_detail_apply = (TextView) findViewById(R.id.brand_detail_apply);
//        brand_detail_apply.setOnClickListener(this);

        recommendAp = new RecommendAp(BaseContext);
        brand_detail_grid.setAdapter(recommendAp);

        brandLsAp = new BrandLsAp(BaseContext,
                R.layout.item_fragment_shop_good_manger_brand);
        brand_detail_horizontal_ls.setAdapter(brandLsAp);
        //
        brand_detail_grid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                BShopGoods mBlComment = (BShopGoods) arg0
                        .getItemAtPosition(arg2);

                PromptManager.SkipActivity(BaseActivity, new Intent(
                        BaseContext, AGoodDetail.class).putExtra("goodid",
                        mBlComment.getId()));

            }
        });

        brand_detail_brand_collect_iv.setOnClickListener(this);
        brandshop_back_iv.setOnClickListener(this);
        brandshop_sou_lay.setOnClickListener(this);
        brandshop_connect_iv.setOnClickListener(this);

    }

    /**
     * 数据的处理
     */
    private void IData() {
        SetTitleHttpDataLisenter(this);
        PromptManager.showtextLoading(BaseContext,
                getResources().getString(R.string.loading));
        // 获取基本信息
        HashMap<String, String> map = new HashMap<String, String>();
        // map.put("agency_id", "1");
        // map.put("agency_id", baseBcBComment.getId());
        map.put("seller_id", baseBcBComment.getId());
        map.put("_member_id", user_Get.getId());
        FBGetHttpData(map, Constants.Shop_Inf, Method.GET, 0, LOAD_INITIALIZE);
        // GetList(CurrentPage, baseBcBComment.getId(), "");
    }

    /**
     * 获取筛选列表信息 Sell_Id是品牌商的ID！！！！！ category_id 是分类的ID
     */
    private void GetList(int Page, int LoadtYPE) {
        // 获取全部信息
        HashMap<String, String> SelectMap = new HashMap<String, String>();
        SelectMap.put("category_id", Current_Category_id);// 空代表全部，
        SelectMap.put("seller_id", mBComment.getBase().getId());//
        SelectMap.put("page", Page + "");
        SelectMap.put("pagesize", Constants.PageSize + "");
        // recommendAp.Clearn();
        FBGetHttpData(SelectMap, Constants.Select_Ls, Method.GET, 1, LoadtYPE);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt(baseBcBComment.getTitle());
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

        switch (Data.getHttpResultTage()) {
            case 0:// 获取全部的信息
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    DataError(Constants.SucessToError, Data.getHttpLoadType());
                    return;
                }
                mBComment = new BShopBrand();
                // 解析***********************************************************************************
                JSONObject mJsonObject = null;
                try {
                    mJsonObject = new JSONObject(Data.getHttpResultStr());

                } catch (JSONException e1) {
                    e1.printStackTrace();
                    DataError(Msg, Data.getHttpLoadType());
                    return;
                }
                if (StrUtils.JsonContainKey(mJsonObject, "agent")) {
                    try {
                        if (!StrUtils.isEmpty(mJsonObject.getString("agent")))
                            mBComment.setAgent(JSON.parseArray(
                                    mJsonObject.getString("agent"),
                                    BShopGoods.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (StrUtils.JsonContainKey(mJsonObject, "base")) {
                    try {
                        mBComment.setBase(JSON.parseObject(
                                mJsonObject.getString("base"), BShopBase.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if (StrUtils.JsonContainKey(mJsonObject, "categorys")) {

                    try {
                        if (!StrUtils.isEmpty(mJsonObject.getString("categorys")))
                            mBComment.setCategorys(JSON.parseArray(
                                    mJsonObject.getString("categorys"),
                                    BShopCatory.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if (StrUtils.JsonContainKey(mJsonObject, "roll")) {

                    try {
                        if (!StrUtils.isEmpty(mJsonObject.getString("roll")))
                            mBComment.setRoll(JSON.parseArray(
                                    mJsonObject.getString("roll"), String.class));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (StrUtils.JsonContainKey(mJsonObject, "is_agented")) {
                    try {
                        mBComment
                                .setIs_agented(mJsonObject.getString("is_agented"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // 解析****************************************************************************************

                ResultData(mBComment);
                IDataView(brand_detail_out_lay, brand_detail_nodata_lay,
                        NOVIEW_RIGHT);
                break;
            case 1:// 获取分类列表数据
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    // DataError(Msg, Data.getHttpLoadType());
                    if (LOAD_LOADMOREING == Data.getHttpLoadType()) {
                        brandscroll.onFooterRefreshComplete();
                        PromptManager.ShowCustomToast(BaseContext, getResources().getString(R.string.nomoregood));
                    }

                    return;
                }
                List<BShopGoods> mBlComments = new ArrayList<BShopGoods>();
                try {
                    mBlComments = JSON.parseArray(Data.getHttpResultStr(),
                            BShopGoods.class);
                } catch (Exception e) {
                    // DataError("", Data.getHttpLoadType());
                    return;
                }
                switch (Data.getHttpLoadType()) {
                    case LOAD_INITIALIZE:
                        recommendAp.FrashData(mBlComments);
//				LateItem = mBlComments.size() - 1;
                        if (mBlComments.size() == 10) {
                            brandscroll.ShowFoot();
                        } else {
                            brandscroll.HindFoot();
                        }
                        break;
                    case LOAD_LOADMOREING:
//				brand_detail_grid.setSelection(LateItem);
                        recommendAp.AddFrashData(mBlComments);
//				LateItem = LateItem + mBlComments.size();ssssssssssssss
                        brandscroll.onFooterRefreshComplete();
                        if (mBlComments.size() == 10) {
                            brandscroll.ShowFoot();
                        } else {
                            brandscroll.HindFoot();

                        }
                        break;
                    default:
                        break;
                }

                IDataView(brand_detail_out_lay, brand_detail_nodata_lay,
                        NOVIEW_RIGHT);
                break;
            case 111://

                // IsCollect
                PromptManager.ShowCustomToast(BaseContext, IsCollect ? "已取消收藏"
                        : "已收藏");
                IsCollect = !IsCollect;
                CollectIV(IsCollect);
                break;
            default:
                break;
        }

    }

    public void ReciverMessage(BMessage message) {
        if (message.getMessageType() == BMessage.Tage_Brand_Apply_Statue) {
//            brand_detail_apply.setClickable(false);
//            brand_detail_apply.setFocusable(false);
//            brand_detail_apply.setBackgroundColor(getResources().getColor(
//                    R.color.app_gray));
//            brand_detail_apply.setText("审核中");
//            brand_detail_apply.setTextColor(getResources().getColor(
//                    R.color.white));
        }


    }

    /**
     * @param ；；1标识获取
     */
    private void ResultData(BShopBrand data) {
        brandshop_connect_iv.setVisibility(View.VISIBLE);
//        if (data.getIs_agented().equals("1")) {// 已经代理过
//            brand_detail_apply.setClickable(false);
//            brand_detail_apply.setFocusable(false);
//            brand_detail_apply.setBackgroundColor(getResources().getColor(
//                    R.color.app_gray));
//            brand_detail_apply.setText("您已代理");
//            brand_detail_apply.setTextColor(getResources().getColor(
//                    R.color.white));
//        } else if (data.getIs_agented().equals("2")) {// 审核中
//            brand_detail_apply.setClickable(false);
//            brand_detail_apply.setFocusable(false);
//            brand_detail_apply.setBackgroundColor(getResources().getColor(
//                    R.color.app_gray));
//            brand_detail_apply.setText("审核中");
//            brand_detail_apply.setTextColor(getResources().getColor(
//                    R.color.white));
//        } else if (data.getIs_agented().equals("0")) {// 未代理
//
//        } else {// 未代理过
//
//        }

        // 获取品牌信息
//        InItPaGeView(data.getRoll());
        BShopBase bdComment = data.getBase();
        ImageLoaderUtil.Load(bdComment.getAvatar(), brand_detail_brand_iv,
                R.drawable.error_iv2);
        recommendAp.FrashData(data.getAgent());
        StrUtils.SetTxt(brand_detail_brand_name, bdComment.getSeller_name());
        brandLsAp.Refrsh(data.getCategorys());
        IsCollect = data.getBase().getIs_collect() != null
                && data.getBase().getIs_collect().equals("1");
        CollectIV(IsCollect);
    }

    private void CollectIV(boolean IsCollect) {
        brand_detail_brand_collect_iv.setImageResource(IsCollect ? R.drawable.brand_collct_pre
                : R.drawable.brand_collct_nor);
    }

    @Override
    protected void DataError(String error, int LoadTyp) {
        PromptManager.ShowCustomToast(BaseContext, error);
        IDataView(brand_detail_out_lay, brand_detail_nodata_lay, NOVIEW_ERROR);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        branddetail_banner.startImageCycle();
    }

    ;

    @Override
    protected void onPause() {
        super.onPause();
//        branddetail_banner.pushImageCycle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        branddetail_banner.pushImageCycle();
    }

    @Override
    protected void NetConnect() {
        NetError.setVisibility(View.GONE);
        IData();
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
        switch (V.getId()) {
            case R.id.brandshop_back_iv:
                BaseActivity.finish();
                break;
            case R.id.brand_detail_brand_lookshow_iv:// 品牌商铺的Show
                if (null == mBComment)
                    return;
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ARecyclerOtherShow.class).putExtra(BaseKey_Bean, new BComment(
                        mBComment.getBase().getId(),
                        mBComment.getBase().getCover(), mBComment.getBase()
                        .getAvatar(), mBComment.getBase().getSeller_name(),
                        "1")));

                break;
            case R.id.brand_detail_brand_brandinf_iv:// 品牌详情
                if (null == mBComment)
                    return;
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ABrandShopInf.class).putExtra(ABrandShopInf.Tage_Key,
                        mBComment.getBase().getIntro()));

                break;
            case R.id.brand_detail_apply:// 申请
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                        AApplyProxy.class).putExtra("brandid", mBComment.getBase()
                        .getId()));
                break;
            case R.id.brand_detail_nodata_lay:
                IData();
                break;
            case R.id.brand_detail_brand_collect_iv:

                CollecNetDo(!IsCollect);
                break;
            case R.id.brandshop_sou_lay://搜搜
                if( CheckNet(BaseContext))return;
                if (mBComment.getAgent() == null) {
                    PromptManager.ShowCustomToast(BaseContext, "店铺没有商品，无法搜索");
                    return;
                }
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, AShopGoodSou.class).putExtra("Sellid", mBComment.getBase().getId()).putExtra("Sellname", mBComment.getBase().getSeller_name()));
                BaseApplication.GetInstance().setShopSouRecommend(mBComment.getAgent());
                break;
            case R.id.brandshop_connect_iv:// 联系客服
               if( CheckNet(BaseContext))return;
                if (!StrUtils.isEmpty(mBComment.getBase().getId()))
                    PromptManager.SkipActivity(
                            BaseActivity,
                            new Intent(BaseActivity, AChatLoad.class)
                                    .putExtra(AChatLoad.Tage_TageId,
                                            mBComment.getBase().getId())
                                    .putExtra(AChatLoad.Tage_Name,
                                            mBComment.getBase().getSeller_name())
                                    .putExtra(AChatLoad.Tage_Iv,
                                            mBComment.getBase().getAvatar()));
                break;

            case R.id.brand_detail_brand_iv://头像
                if (null == mBComment)
                    return;
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ABrandShopShare.class).putExtra(ABrandShopShare.Tage_Key,
                        mBComment.getBase().getSeller_url()).putExtra(ABrandShopShare.Tage_Avatar_Key, mBComment.getBase().getAvatar()).putExtra(ABrandShopShare.Tage_Seller_Id, mBComment.getBase().getSeller_no()).putExtra(ABrandShopShare.Tage_Title, mBComment.getBase().getSeller_name()));
                break;
            default:
                break;
        }

    }

    /**
     * 进行收藏取消收藏
     */
    private void CollecNetDo(boolean IsToCollect) {
        // Constants.GuanZhuShop====>关注店铺////member_id///////seller_id
        // Constants.CancleGuanZhuShop===》取消关注店铺////member_id///seller_id
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("member_id", user_Get.getMember_id());
        hashMap.put("seller_id", mBComment.getBase().getId());

        FBGetHttpData(hashMap, IsToCollect ? Constants.GuanZhuShop
                : Constants.CancleGuanZhuShop, IsToCollect ? Method.POST
                : Method.DELETE, 111, LOAD_INITIALIZE);
    }

    // ******************start*****************Banner*****************start********************

//    private void InItPaGeView(List<String> data) {
//        if (null == data || data.size() == 0) {
//            branddetail_banner.setVisibility(View.GONE);
//            return;
//        }
//        ArrayList<String> ddas = (ArrayList<String>) data;
//        branddetail_banner.setImageResources(ddas, ddas, mAdCycleViewListener,
//                screenWidth / 2);
//    }

    private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
        @Override
        public void displayImage(String imageURL, ImageView imageView, int postion) {
            ImageLoaderUtil.Load2(imageURL, imageView, R.drawable.error_iv1);
        }

        @Override
        public void onImageClick(int position, View imageView) {
            if (mBComment.getRoll() != null && mBComment.getRoll().size() > 0) {
                List<String> daa = mBComment.getRoll();

                Intent mIntent = new Intent(BaseContext, AphotoPager.class);
                mIntent.putExtra("position", position);
                mIntent.putExtra("urls", StrUtils.LsToArray(daa));
                PromptManager.SkipActivity(BaseActivity, mIntent);
            }
        }


    };

    @Override
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }


    /**
     * 只有品牌商品中使用到的横向滑动的listview
     */

    private class BrandLsAp extends BaseAdapter {
        private Context mycContext;
        private LayoutInflater inflater;
        private int ResourceId;
        private List<BShopCatory> datas = new ArrayList<BShopCatory>();

        // private List<MyBrandItem> Views = new ArrayList<MyBrandItem>();

        public BrandLsAp(Context mycContext, int resourceId) {
            super();
            this.mycContext = mycContext;
            this.inflater = LayoutInflater.from(mycContext);
            this.ResourceId = resourceId;
        }

        /**
         * 刷新需要在头部添加一个全部的item
         *
         * @param da
         */
        public void Refrsh(List<BShopCatory> da) {
            if (null == da || da.size() == 0) {
                return;
            }
            BShopCatory dad = new BShopCatory();
            dad.setId("");
            dad.setCate_name("全部");
            dad.setBrandDetaiLsSelect(false);
            this.datas = new ArrayList<BShopCatory>();
            this.datas.add(dad);
            this.datas.addAll(da);

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
        public View getView(int arg0, View convertView, ViewGroup arg2) {
            MyBrandItem myItem = null;
            if (convertView == null) {
                myItem = new MyBrandItem();
                convertView = inflater.inflate(ResourceId, null);
                myItem.item_fragment_shop_good_manger_brand_name = (TextView) convertView
                        .findViewById(R.id.item_fragment_shop_good_manger_brand_name);
                // Views.add(myItem);
                convertView.setTag(myItem);

            } else {
                myItem = (MyBrandItem) convertView.getTag();
            }
            myItem.item_fragment_shop_good_manger_brand_name
                    .setBackgroundResource(datas.get(arg0)
                            .isBrandDetaiLsSelect() ? R.drawable.shape_comment_oval_pre
                            : R.drawable.shape_comment_oval);
            myItem.item_fragment_shop_good_manger_brand_name
                    .setTextColor(getResources()
                            .getColor(
                                    datas.get(arg0).isBrandDetaiLsSelect() ? R.color.app_fen
                                            : R.color.grey));

            StrUtils.SetTxt(myItem.item_fragment_shop_good_manger_brand_name,
                    datas.get(arg0).getCate_name());
            myItem.item_fragment_shop_good_manger_brand_name
                    .setOnClickListener(new HorizontalItemClikListener(arg0,
                            datas, myItem, this));// TODO后期需要传入正确的BLComment数据bean
            // 目前为null
            return convertView;
        }

        class MyBrandItem {
            TextView item_fragment_shop_good_manger_brand_name;
        }

        class HorizontalItemClikListener implements OnClickListener {

            private int Postion;// 记录点击的位置
            private List<BShopCatory> blCommentss;// 记录品牌的数据bean
            private MyBrandItem myBrandItem;
            BrandLsAp ap;

            public HorizontalItemClikListener(int postion,
                                              List<BShopCatory> data, MyBrandItem item, BrandLsAp aBrandLsAp) {
                super();
                myBrandItem = item;
                ap = aBrandLsAp;
                Postion = postion;
                blCommentss = data;
            }

            @Override
            public void onClick(View arg0) {
                if (blCommentss.get(Postion).isBrandDetaiLsSelect())
                    return;

                for (int i = 0; i < blCommentss.size(); i++) {

                    blCommentss.get(i).setBrandDetaiLsSelect(i == Postion);

                    myBrandItem.item_fragment_shop_good_manger_brand_name
                            .setBackgroundResource(blCommentss.get(Postion)
                                    .isBrandDetaiLsSelect() ? R.drawable.shape_comment_oval_pre
                                    : R.drawable.shape_comment_oval);
                    myBrandItem.item_fragment_shop_good_manger_brand_name
                            .setTextColor(getResources()
                                    .getColor(
                                            blCommentss.get(Postion)
                                                    .isBrandDetaiLsSelect() ? R.color.app_fen
                                                    : R.color.grey));
                }
                ap.notifyDataSetChanged();
                CurrentPage = 1;

                ChangSort(blCommentss.get(Postion));
            }
        }
    }

    // 点击横向的分类的item进行数据变换s
    private void ChangSort(BShopCatory d) {
        // PromptManager.ShowCustomToast(BaseContext, "Seller_id" + d.getId());
        PromptManager.showtextLoading(BaseContext,
                getResources().getString(R.string.loading));
        Current_Category_id = d.getId();
        GetList(CurrentPage, LOAD_INITIALIZE);

    }

    /**
     * GradViewd的adapter
     *
     * @author datutu
     */
    class RecommendAp extends BaseAdapter {
        private List<BShopGoods> datas = new ArrayList<BShopGoods>();
        private Context myContext;

        public RecommendAp(Context myContext) {
            super();
            this.myContext = myContext;
        }

        /**
         * 初次刷新
         *
         * @param da
         */
        public void FrashData(List<BShopGoods> da) {
            if (da == null)
                return;
            this.datas = da;
            notifyDataSetChanged();
        }

        /**
         * 添加刷新
         *
         * @param da
         */
        public void AddFrashData(List<BShopGoods> da) {
            if (da == null)
                return;
            this.datas.addAll(da);
            notifyDataSetChanged();
        }

        public void Clearn() {
            datas = new ArrayList<BShopGoods>();
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
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            Myitem myItem = null;
            if (arg1 == null) {
                myItem = new Myitem();
                arg1 = LayoutInflater.from(myContext).inflate(
                        R.layout.item_branddetail_recommend, null);
                myItem.item_branddetail_iv = ViewHolder.get(arg1,
                        R.id.item_branddetail_iv);
                myItem.item_branddetail_name = ViewHolder.get(arg1,
                        R.id.item_branddetail_name);
                myItem.item_branddetail_price = ViewHolder.get(arg1,
                        R.id.item_branddetail_price);
                myItem.item_branddetail_orig_price = ViewHolder.get(arg1,
                        R.id.item_branddetail_orig_price);
                myItem.item_branddetail_sales = ViewHolder.get(arg1,
                        R.id.item_branddetail_sales);
                myItem.item_branddetail_score = ViewHolder.get(arg1,
                        R.id.item_branddetail_score);
                arg1.setTag(myItem);
            } else {
                myItem = (Myitem) arg1.getTag();
            }
            BShopGoods data = datas.get(arg0);
            ImageLoaderUtil.Load2(data.getCover(), myItem.item_branddetail_iv,
                    R.drawable.error_iv2);
            StrUtils.SetTxt(myItem.item_branddetail_name, data.getTitle());
            StrUtils.SetTxt(myItem.item_branddetail_price,
                    StrUtils.SetTextForMony(data.getSell_price()) + "元");
            if (!StrUtils.isEmpty(data.getOrig_price()) && !data.getOrig_price().equals("0")) {
                StrUtils.SetTxt(myItem.item_branddetail_orig_price,
                        "原价" + StrUtils.SetTextForMony(data.getOrig_price()));
                myItem.item_branddetail_orig_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            //判断销量
            if (!StrUtils.isEmpty(data.getSales())) {
                myItem.item_branddetail_sales.setText(String.format("销量:%s", data.getSales()));
            }
            //判断积分
            if (!StrUtils.isEmpty(data.getScore())) {
                myItem.item_branddetail_score.setText(String.format("积分:%s", data.getScore()));
            }
            return arg1;
        }

        class Myitem {
            ImageView item_branddetail_iv;
            TextView item_branddetail_name;
            TextView item_branddetail_price;
            TextView item_branddetail_orig_price, item_branddetail_sales, item_branddetail_score;
        }
    }

//	private final class ScrollListener implements OnScrollListener {
//
//		// scrollState=0,1,2三种状态
//		public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//		}
//
//		// totalItemCount 表示ListView中已装载的数据
//		public void onScroll(AbsListView view, int firstVisibleItem,
//				int visibleItemCount, int totalItemCount) {
//
//			// final int loadtotal = totalItemCount;
//			// int lastItemid = newhome_ls.getLastVisiblePosition();//
//			// 获取当前屏幕最后Item的ID
//			// if ((lastItemid + 1) == totalItemCount) {// 达到数据的最后一条记录
//			// if (totalItemCount > 0) {
//			// // // 当前页
//			// int currentpage = totalItemCount % number == 0 ? totalItemCount
//			// / number
//			// : totalItemCount / number + 1;
//			// int nextpage = currentpage + 1;// 下一页
//			//
//			// new Thread(new Runnable() {
//			// public void run() {
//			// try {
//			// Thread.sleep(3000);
//			// } catch (InterruptedException e) {
//			// e.printStackTrace();
//			// }
//			//
//			// // handler.sendMessage(handler.obtainMessage(100,
//			// // result));
//			// }
//			// }).start();
//			// // }
//			// }
//			//
//			// }
//		}
//	}

    //	@Override
//	public void scrollBottom() {
//		PromptManager.showLoading(BaseContext);
//		CurrentPage = CurrentPage + 1;
//		GetList(CurrentPage, LOAD_LOADMOREING);
//	}
//
//	@Override
//	public void scrollUp() {
//
//	}
    @Override
    public void onFooterRefresh(PullView view) {
//		PromptManager.showLoading(BaseContext);
        CurrentPage = CurrentPage + 1;
        GetList(CurrentPage, LOAD_LOADMOREING);
    }

}
