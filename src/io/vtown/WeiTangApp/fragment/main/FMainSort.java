package io.vtown.WeiTangApp.fragment.main;

import android.content.Intent;
import android.graphics.Paint;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.mainsort.BSortCategory;
import io.vtown.WeiTangApp.bean.bcomment.easy.mainsort.BSortGood;
import io.vtown.WeiTangApp.bean.bcomment.easy.mainsort.BSortRang;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.NHttpBaseStr;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.RefreshLayout;
import io.vtown.WeiTangApp.comment.view.custom.horizontalscroll.HBaseAdapter;
import io.vtown.WeiTangApp.comment.view.custom.horizontalscroll.HorizontalScrollMenu;
import io.vtown.WeiTangApp.comment.view.pop.PMainTabSort;
import io.vtown.WeiTangApp.event.interf.IHttpResult;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.ui.comment.AMianSort;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.ui.ASouSouGood;

/**
 * Created by datutu on 2016/10/28.
 * 首页分类
 */

public class FMainSort extends FBase implements RefreshLayout.OnLoadListener {
    @BindView(R.id.fragment_main_sort_sou_lay)
    RelativeLayout fragmentMainSortSouLay;
    @BindView(R.id.f_sort_goofd_container)
    HorizontalScrollMenu fSortGoofdContainer;
    @BindView(R.id.sort_good_zonghe)
    TextView sortGoodZonghe;
    @BindView(R.id.sort_good_price)
    TextView sortGoodPrice;
    @BindView(R.id.sort_good_jifen)
    TextView sortGoodJifen;
    @BindView(R.id.sort_good_xiaoliang)
    TextView sortGoodXiaoliang;
    @BindView(R.id.sort_good_shaixuan)
    TextView sortGoodShaixuan;
    @BindView(R.id.sort_good_price_iv)
    ImageView sortGoodPriceIv;//价格的右边图片
    @BindView(R.id.sort_good_price_lay)
    RelativeLayout sortGoodPriceLay;//价格的点击布局
    @BindView(R.id.sort_good_shaixuan_iv)
    ImageView sortGoodShaixuanIv;//筛选的右边图片
    @BindView(R.id.sort_good_shaixuan_lay)
    RelativeLayout sortGoodShaixuanLay;//筛选的点击布局布局
    @BindView(R.id.fragment_sort_ls)
    ListView fragmentSortLs;
    @BindView(R.id.fragment_sort_refrash)
    RefreshLayout fragmentSortRefrash;

    private View fragment_sort_nodata_lay;
    private boolean IsUpSortZoreClick = false;
    //一级分类的选择位置
    private int UpSortPostion = 0;
    //是否综合被点击 默认情况是被点击的
    private boolean SortZongHe = true;
    //是否价格升序 /0=>标识刚进来没有选择（重置状态）；；1==>标识点击后进行价格由低到高，；2==>标识点击后价格由高到低
    private int SortPriceUp = 0;
    //是否筛选被点击状态（默认没有被点击)
    private boolean SortSortClick = false;
    //是否积分被筛选
    private boolean SortJiFenClick = false;
    //是否销量被筛选
    private boolean SortSellNumberClick = false;
    //筛选的pop
    private PMainTabSort pMainTabSort;
    //配置
    private List<BSortCategory> MySortCategory;

    //一个标识去记录点击筛选时候的一级分类的位置
    private int SortPostion = 0;
    private String CurrentOutSortId = "0";
    //一个标记去记录是否已经筛选过
    private boolean IsHaveSort = false;
    //需要保存我筛选过的记录条件仅仅是供给一级分类刚筛选过又要筛选的 如果以及分类变了需要进行重置
    private String SecondSortId = "";
    private BSortRang PriceSort;
    private BSortRang ScoreSort;
    private String BrandName = "";
    //记录位置
    private int SecondSortId_Postion;
    private int PriceSort_Postion;
    private int ScoreSort_Postion;
    private int BrandName_Postion;
    //记录是否已经自定义区间
    private boolean IsSort_Rang_Price_ZiDingYi;//是否自定义价格区间
    private boolean IsSort_Rang_Score_ZiDingYi;//是否自定义积分区间

    //TODO在进这个fragment时候需要偷偷加载/刷新已经缓存过的筛选的价格区间积分去接纳品牌名称列表
    //开始进行
    private SortAp mySortAdapter;


    //当前列表是第几页的
    private int CurrentPage = 1;

    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_main_sort, null);
        ButterKnife.bind(this, BaseView);
        EventBus.getDefault().register(this, "MyReCeverMsg", BMessage.class);
        SetTitleHttpDataLisenter(this);
        //必需先确保一级分类存在******不存在就立即进行获取
        ICacheCategory();
        //第一次进来需要偷偷的加载==>价格/品牌/积分三个标识
        ISaveSortTiaoJian();
    }


    private void ICacheCategory() {
        String Mycache = CacheUtil.HomeSort_Get(BaseContext);
        if (StrUtils.isEmpty(Mycache)) {//不存在缓存***
            IGetCategoryData();
        } else {//存在缓存****
            MySortCategory = JSON.parseArray(Mycache, BSortCategory.class);
            MySortCategory.add(0, new BSortCategory("0", "全部"));
            IBase();
        }

    }

    /**
     * 分类列表转换成名称的列表
     */
    private List<String> ChangSt(List<BSortCategory> dass) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < dass.size(); i++) {
            data.add(dass.get(i).getCate_name());
        }
        return data;
    }

    //开始获取一级分类的数据
    private void IGetCategoryData() {
        NHttpBaseStr Bastr = new NHttpBaseStr(BaseContext);
        Bastr.setPostResult(new IHttpResult<String>() {
            @Override
            public void getResult(int Code, String Msg, String Data) {
                CacheUtil.HomeSort_Save(BaseContext, Data);

                MySortCategory = JSON.parseArray(Data, BSortCategory.class);
                MySortCategory.add(0, new BSortCategory("0", "全部"));
                IBase();
            }

            @Override
            public void onError(String error, int LoadType) {

            }
        });

        HashMap<String, String> Hsmap = new HashMap<>();
        Hsmap.put("pid", "0");
        Bastr.getData(Constants.Add_Good_Categoty, Hsmap, Request.Method.GET);

    }

    //开始获取要添加标识进行处理
    //UpType 1标识综合 2标识价格升序 21标识价格降序 3标识积分 4标识销量
    // 只有当排序类别价格时候才会显示升序降序 积分和销量全部是降序
    private void GetGoodsLs(int Page, String UpType, boolean IsUp, int LoadType) {
        if (LoadType == INITIALIZE) {
            PromptManager.showtextLoading(BaseContext, "加载中....");
        }
        HashMap<String, String> GoodsMap = new HashMap<>();
//        GoodsMap.put("seller_id", Spuit.User_Get(BaseContext).getSeller_id());
        GoodsMap.put("sale_status", "100");//正常售卖的
        GoodsMap.put("is_delete", "0");//0标识正常
        GoodsMap.put("pagesize", "20");//当前的页数
        GoodsMap.put("is_agent", "1");// 不传标识获取品牌和自营的
        GoodsMap.put("page", Page + "");//当前的页数
        GoodsMap.put("cate_id", CurrentOutSortId);//外层分类分类id
        if (!StrUtils.isEmpty(SecondSortId) && !SecondSortId.contains(","))
            GoodsMap.put("category_id", SecondSortId);//内层分类分类id

        GoodsMap.put("orderby", UpType);//排序字段
        GoodsMap.put("sort", !IsUp ? "ASC" : "DESC");//排序顺序ASC升序   DESC降序


        GoodsMap.put("min", ScoreSort.getMin());//积分的最小
        GoodsMap.put("max", ScoreSort.getMax());//积分的最大
        GoodsMap.put("price_max", StrUtils.toInt_price(PriceSort.getMax()));//价格的最大值
        GoodsMap.put("price_min", StrUtils.toInt_price(PriceSort.getMin()));//价格的最小值
        GoodsMap.put("keyword", BrandName);
        FBGetHttpData(GoodsMap, Constants.Select_Ls, Request.Method.GET, 0, LoadType);
    }

    private void IBase() {
        fragment_sort_nodata_lay = BaseView.findViewById(R.id.fragment_sort_nodata_lay);

        PriceSort = new BSortRang("0", Constants.SortMax);
        ScoreSort = new BSortRang("0", Constants.SortMax);
        fSortGoofdContainer.setSwiped(false);
//        fSortGoofdContainer.SetLayoutColor(getResources().getColor(R.color.app_fen));
        fSortGoofdContainer.setMenuItemPaddingLeft(50);
        fSortGoofdContainer.setMenuItemPaddingRight(50);
//        fSortGoofdContainer.SetBgColo(getResources().getColor(R.color.app_fen));

//        fSortGoofdContainer.setColorList(R.drawable.selector_menu_item_text1); //正常selector_menu_item_text // 正常selector_menu_item_text//非正常R.drawable.select_sort_up1
//        fSortGoofdContainer.setCheckedBackground(R.color.app_fen);
//        fSortGoofdContainer.SetCheckedTxtColor(getResources().getColor(R.color.gold));
        fSortGoofdContainer.setAdapter(new SortMenuAdapter());
        fragmentSortRefrash.setColorSchemeResources(R.color.app_fen, R.color.app_fen1, R.color.app_fen2, R.color.app_fen3);
        fragmentSortRefrash.setOnLoadListener(this);
//        fragmentSortRefrash.setCanLoadMore(false);
//        fragmentSortRefrash
        //上边选择的textview的设置
        GetGoodsLs(CurrentPage, "weight", true, INITIALIZE);

        mySortAdapter = new SortAp();
        fragmentSortLs.setAdapter(mySortAdapter);
        fragmentSortLs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BSortGood data = (BSortGood) parent.getItemAtPosition(position);
                PromptManager.SkipActivity(BaseActivity,
                        new Intent(BaseActivity, AGoodDetail.class)
                                .putExtra("goodid", data.getId()));
            }
        });


    }


    /**
     * 对于价格的txt右边的图片和文字进行颜色处理啊
     */
    private void PriceColorControl(boolean reset) {
        if (reset) SortPriceUp = 0;//重置时候直接复原即可
        else if (SortPriceUp == 0) {//非重置时候需要进行转换
            SortPriceUp = 1;
        } else if (SortPriceUp == 1) {
            SortPriceUp = 2;
        } else if (SortPriceUp == 2) {
            SortPriceUp = 1;
        } else {
        }

        switch (SortPriceUp) {
            case 0:
                sortGoodPrice.setTextColor(getResources().getColor(R.color.gray));
                sortGoodPriceIv.setImageResource(R.drawable.sort_price_nor);
                break;
            case 1:
                sortGoodPrice.setTextColor(getResources().getColor(R.color.app_fen));
                sortGoodPriceIv.setImageResource(R.drawable.sort_price_up);
                GetGoodsLs(CurrentPage, "sell_price", false, INITIALIZE);
                break;
            case 2:
                sortGoodPrice.setTextColor(getResources().getColor(R.color.app_fen));
                sortGoodPriceIv.setImageResource(R.drawable.sort_price_down);
                GetGoodsLs(CurrentPage, "sell_price", true, INITIALIZE);
                break;
        }
    }

    /**
     * 进行筛选文字的颜色
     */
    private void SortShaiXuan() {
        sortGoodShaixuan.setTextColor(getResources().getColor(SortSortClick ? R.color.app_fen : R.color.gray));
        sortGoodShaixuanIv.setImageResource(SortSortClick ? R.drawable.sort_arrow_dow_pre : R.drawable.sort_arrow_dow_nor);
    }

    /**
     * 开始获取商品列表
     */
    @OnClick({R.id.fragment_main_sort_sou_lay, R.id.sort_good_zonghe, R.id.sort_good_jifen, R.id.sort_good_xiaoliang, R.id.sort_good_price_lay, R.id.sort_good_shaixuan_lay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sort_good_zonghe://点击综合&&点击综合价格//积分//销量//全部清空
                CurrentPage = 1;
                if (!SortZongHe)
                    ResetSort();
                //  //开始请求数据！！！！！！！！！！！
                break;
            case R.id.sort_good_price_lay://点击价格
                CurrentPage = 1;
                //综合重置
                SortZongHe = false;
                sortGoodZonghe.setTextColor(getResources().getColor(R.color.gray));
                //积分重置
                SortJiFenClick = false;
                sortGoodJifen.setTextColor(getResources().getColor(R.color.gray));
                //销量重置
                SortSellNumberClick = false;
                sortGoodXiaoliang.setTextColor(getResources().getColor(R.color.gray));

                PriceColorControl(false);
                break;
            case R.id.sort_good_jifen://点击积分
                CurrentPage = 1;
                //综合重置
                SortZongHe = false;
                sortGoodZonghe.setTextColor(getResources().getColor(R.color.gray));
                //价格重置
                PriceColorControl(true);
                //销量重置
                SortSellNumberClick = false;
                sortGoodXiaoliang.setTextColor(getResources().getColor(R.color.gray));


                if (!SortJiFenClick) {
                    SortJiFenClick = true;
                    sortGoodJifen.setTextColor(getResources().getColor(R.color.app_fen));
                    //开始请求数据！！！！！！！！！！！
                    GetGoodsLs(CurrentPage, "score", true, INITIALIZE);
                }
                break;
            case R.id.sort_good_xiaoliang://点击销量
                CurrentPage = 1;
                //综合重置
                SortZongHe = false;
                sortGoodZonghe.setTextColor(getResources().getColor(R.color.gray));
                //价格重置
                PriceColorControl(true);
                //积分重置
                SortJiFenClick = false;
                sortGoodJifen.setTextColor(getResources().getColor(R.color.gray));


                if (!SortSellNumberClick) {
                    SortSellNumberClick = true;
                    sortGoodXiaoliang.setTextColor(getResources().getColor(R.color.app_fen));
                    //开始请求数据！！！！！！！！！！！
                    GetGoodsLs(CurrentPage, "sales", true, INITIALIZE);
                }
                break;
            case R.id.sort_good_shaixuan_lay://点击筛选
                SortSortClick = !SortSortClick;
                SortShaiXuan();
//                fSortGoofdContainer.setColorList(R.drawable.select_sort_up1); //正常selector_menu_item_text // 正常selector_menu_item_text//非正常R.drawable.select_sort_up1
                if (SortPostion == UpSortPostion && IsHaveSort) {//需要把原来筛选获取的数据带过去
                    PromptManager.ShowCustomToast(BaseContext, "我需要把数据带过去");
                    Intent MyIntent = new Intent(BaseActivity, AMianSort.class);
                    //我需要带参数过去初始化之前数据
                    MyIntent.putExtra("IsInItView", true);
                    MyIntent.putExtra("SecondSortId", SecondSortId);
                    MyIntent.putExtra("PriceSort", PriceSort);
                    MyIntent.putExtra("ScoreSort", ScoreSort);
                    MyIntent.putExtra("BrandName", BrandName);
                    //传递位置
                    MyIntent.putExtra("SecondSortId_Postion", SecondSortId_Postion);
                    MyIntent.putExtra("PriceSort_Postion", PriceSort_Postion);
                    MyIntent.putExtra("ScoreSort_Postion", ScoreSort_Postion);
                    MyIntent.putExtra("BrandName_Postion", BrandName_Postion);
//传递是否已经自定义区间
                    MyIntent.putExtra("IsSort_Rang_Price_ZiDingYi", IsSort_Rang_Price_ZiDingYi);
                    MyIntent.putExtra("IsSort_Rang_Score_ZiDingYi", IsSort_Rang_Score_ZiDingYi);

                    MyIntent.putExtra("catoryid", MySortCategory.get(UpSortPostion).getId());
                    PromptManager.SkipActivity(BaseActivity, MyIntent);
                } else {
                    SortPostion = UpSortPostion;
                    PromptManager.ShowCustomToast(BaseContext, "我需要初始化进去");
                    Intent MyIntent = new Intent(BaseActivity, AMianSort.class);
                    MyIntent.putExtra("catoryid", MySortCategory.get(UpSortPostion).getId());
                    PromptManager.SkipActivity(BaseActivity, MyIntent);
                }


//                if (SortSortClick) {
//                    Intent MyIntent = new Intent(BaseActivity, AMianSort.class);
//                    MyIntent.putExtra("catoryid", MySortCategory.get(UpSortPostion).getId());
//                    PromptManager.SkipActivity(BaseActivity, MyIntent);
//                }
                break;
            case R.id.fragment_main_sort_sou_lay://点击搜索
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, ASouSouGood.class));
                break;
        }
    }

    private void ResetSort() {
        SecondSortId = "";
        PriceSort = new BSortRang("0", Constants.SortMax);
        ScoreSort = new BSortRang("0", Constants.SortMax);
        BrandName = "";

        SecondSortId_Postion = -1;
        PriceSort_Postion = -1;
        ScoreSort_Postion = -1;
        BrandName_Postion = -1;
        //改变是否自定义区间
        IsSort_Rang_Price_ZiDingYi = false;
        IsSort_Rang_Score_ZiDingYi = false;
        //改变综合的标志
        SortZongHe = true;
        sortGoodZonghe.setTextColor(getResources().getColor(R.color.app_fen));
        //改变价格的标志
        PriceColorControl(true);
        //该表筛选的标志
        SortSortClick = false;
        SortShaiXuan();
        //改变积分的标志
        SortJiFenClick = false;
        sortGoodJifen.setTextColor(getResources().getColor(R.color.gray));
        //改变销量的标志
        SortSellNumberClick = false;
        sortGoodXiaoliang.setTextColor(getResources().getColor(R.color.gray));
        //开始请求数据！！！！！！！！！！！！！！！！！！！！！！！！！！！！
        GetGoodsLs(CurrentPage, "weight", true, INITIALIZE);
    }

    /**
     * 获取分类（在本fragment中只用获取到外层的一级分类就可以，，二级分类需要在弹出框中获取）
     *
     * @param Sorttype
     */
    private void NetSort(String Sorttype) {

        HashMap<String, String> map = new HashMap<>();
        map.put("pid", Sorttype);
        FBGetHttpData(map, Constants.Add_Good_Categoty, Request.Method.GET, 0, 11);

    }

    @Override
    public void OnLoadMore() {
        CurrentPage = CurrentPage + 1;
        if (SortZongHe) //综合被点击
            GetGoodsLs(CurrentPage, "weight", true, LOADMOREING);
       else if (SortPriceUp == 1) //价格升序
            GetGoodsLs(CurrentPage, "sell_price", false, LOADMOREING);
      else  if (SortPriceUp == 2) //价格降序
            GetGoodsLs(CurrentPage, "sell_price", true, LOADMOREING);
        else if (SortJiFenClick) //积分升序
            GetGoodsLs(CurrentPage, "score", true, LOADMOREING);
       else  if (SortSellNumberClick)
            GetGoodsLs(CurrentPage, "sales", true, LOADMOREING);else ;
    }

    @Override
    public void OnFrash() {
//
        CurrentPage = 1;

        if (SortZongHe) //综合被点击
            GetGoodsLs(CurrentPage, "weight", true, REFRESHING);
        else if (SortPriceUp == 1) //价格升序
            GetGoodsLs(CurrentPage, "sell_price", false, REFRESHING);
        else if (SortPriceUp == 2) //价格降序
            GetGoodsLs(CurrentPage, "sell_price", true, REFRESHING);
       else if (SortJiFenClick) //积分升序
            GetGoodsLs(CurrentPage, "score", true, REFRESHING);
       else if (SortSellNumberClick)
            GetGoodsLs(CurrentPage, "sales", true, REFRESHING);else;
    }


    class SortMenuAdapter extends HBaseAdapter {
        @Override
        public List<String> getMenuItems() {
            return ChangSt(MySortCategory);
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
            if (!IsUpSortZoreClick && position == 0) {
                IsUpSortZoreClick = true;

                return;
            }
            if (position != UpSortPostion) {//点击的是其他的非原来的一级分类 就默认认为还没进行筛选 同时还要把筛选条件重置
                IsHaveSort = true;

            }
            CurrentPage = 1;
            UpSortPostion = position;
            CurrentOutSortId = MySortCategory.get(UpSortPostion).getId();
            //重置
            ResetSort();
            GetGoodsLs(CurrentPage, "weight", false, INITIALIZE);


            if (fragmentSortRefrash.isRefreshing()) fragmentSortRefrash.setRefreshing(false);
//            PromptManager.ShowCustomToast(BaseContext, "位置==>" + MySortCategory.get(position).getCate_name());
        }

    }

    @Override
    public void InitCreate(Bundle d) {

    }

    @Override
    public void onPause() {
        super.onPause();
//sss
        if (fragmentSortRefrash.isRefreshing()) fragmentSortRefrash.setRefreshing(false);
    }

    @Override
    public void getResult(int Code, String Msg, BComment Data) {

        String ResultStr = Data.getHttpResultStr();
        IDataView(fragmentSortRefrash, fragment_sort_nodata_lay, NOVIEW_RIGHT);
        switch (Data.getHttpResultTage()) {
            case 0://获取列表
                switch (Data.getHttpLoadType()) {
                    case INITIALIZE:

                        if (StrUtils.isEmpty(ResultStr)) {
                            PromptManager.ShowCustomToast(BaseContext, "暂无数据");
                            mySortAdapter.FrashData(new ArrayList<BSortGood>());
                            IDataView(fragmentSortRefrash, fragment_sort_nodata_lay, NOVIEW_ERROR);
                            ShowErrorCanLoad(getResources().getString(R.string.error_null_good));
                            ShowErrorIv(R.drawable.error_sou);
                            return;
                        }
                        fragmentSortLs.smoothScrollToPosition(-20);
                        List<BSortGood> ListGoods = JSON.parseArray(ResultStr, BSortGood.class);

                        mySortAdapter.FrashData(ListGoods);
                        if (ListGoods.size() < 20) {
                            fragmentSortRefrash.setCanLoadMore(false);
                        }
                        if (ListGoods.size() == 20) {
                            fragmentSortRefrash.setCanLoadMore(true);
                        }
                        break;
                    case LOADMOREING:
                        fragmentSortRefrash.setLoading(false);
                        //
                        if (StrUtils.isEmpty(ResultStr)) {
                            PromptManager.ShowCustomToast(BaseContext, "没更多商品咯");
                            return;
                        }
                        List<BSortGood> ListGoodsmore = JSON.parseArray(ResultStr, BSortGood.class);
                        mySortAdapter.AddFrashData(ListGoodsmore);
                        if (ListGoodsmore.size() < 20) {
                            fragmentSortRefrash.setCanLoadMore(false);
                        }
                        if (ListGoodsmore.size() == 20) {
                            fragmentSortRefrash.setCanLoadMore(true);
                        }
                        break;
                    case REFRESHING:
                        fragmentSortLs.smoothScrollToPosition(-20);
                        fragmentSortRefrash.setRefreshing(false);

                        if (StrUtils.isEmpty(ResultStr)) {
                            PromptManager.ShowCustomToast(BaseContext, "暂无数据");
                            mySortAdapter.FrashData(new ArrayList<BSortGood>());
                            IDataView(fragmentSortRefrash, fragment_sort_nodata_lay, NOVIEW_ERROR);
                            ShowErrorCanLoad(getResources().getString(R.string.error_null_good));
                            ShowErrorIv(R.drawable.error_sou);
                            return;
                        }
                        List<BSortGood> ListGoodss = JSON.parseArray(ResultStr, BSortGood.class);

                        mySortAdapter.FrashData(ListGoodss);
                        if (ListGoodss.size() < 20) {
                            fragmentSortRefrash.setCanLoadMore(false);
                        }
                        if (ListGoodss.size() == 20) {
                            fragmentSortRefrash.setCanLoadMore(true);
                        }

                        break;
                    case LOADHind:
                        break;

                }

                break;
        }
    }

    @Override
    public void onError(String error, int LoadType) {
        switch (LoadType) {
            case REFRESHING:
                fragmentSortRefrash.setRefreshing(false);
                IDataView(fragmentSortRefrash, fragment_sort_nodata_lay, NOVIEW_ERROR);
                break;
            case LOADMOREING:
                fragmentSortRefrash.setLoading(false);

                break;
        }
    }

    /**
     * 商品列表的AP
     */


    class SortAp extends BaseAdapter {
        private List<BSortGood> SortDatas = new ArrayList<>();

        public void FrashData(List<BSortGood> ds) {
            this.SortDatas = ds;
            this.notifyDataSetChanged();
        }

        public void AddFrashData(List<BSortGood> dd) {
            this.SortDatas.addAll(dd);
            this.notifyDataSetChanged();

        }

        @Override
        public int getCount() {
            return SortDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return SortDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MySortItem mySortItem = null;
            if (null == convertView) {
                mySortItem = new MySortItem();
                convertView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_item_goodsort, null);
                mySortItem.item_main_sort_iv = ViewHolder.get(convertView, R.id.item_main_sort_iv);
                mySortItem.item_main_sort_name = ViewHolder.get(convertView, R.id.item_main_sort_name);
                mySortItem.item_main_sort_jifen = ViewHolder.get(convertView, R.id.item_main_sort_jifen);
                mySortItem.item_main_sort_xiaoliang = ViewHolder.get(convertView, R.id.item_main_sort_xiaoliang);
                mySortItem.item_main_sort_price = ViewHolder.get(convertView, R.id.item_main_sort_price);
                mySortItem.item_main_sort_price_yuan = ViewHolder.get(convertView, R.id.item_main_sort_price_yuan);
                convertView.setTag(mySortItem);
            } else {
                mySortItem = (MySortItem) convertView.getTag();
            }
            BSortGood bSortGood = SortDatas.get(position);
            ImageLoaderUtil.Load2(bSortGood.getCover(), mySortItem.item_main_sort_iv, R.drawable.error_iv2);
            StrUtils.SetTxt(mySortItem.item_main_sort_name, bSortGood.getTitle());
            StrUtils.SetTxt(mySortItem.item_main_sort_jifen, String.format("积分：%s", bSortGood.getScore()));
            StrUtils.SetTxt(mySortItem.item_main_sort_xiaoliang, String.format("销量：%s", bSortGood.getSales()));
            StrUtils.SetTxt(mySortItem.item_main_sort_price, String.format("￥%s", StrUtils.SetTextForMony(bSortGood.getSell_price())));
            if (!StrUtils.isEmpty(bSortGood.getOrig_price()) && !bSortGood.getOrig_price().equals("0")) {
                StrUtils.SetTxt(mySortItem.item_main_sort_price_yuan, String.format("￥%s", StrUtils.SetTextForMony(bSortGood.getOrig_price())));
                mySortItem.item_main_sort_price_yuan.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            return convertView;
        }

        class MySortItem {
            ImageView item_main_sort_iv;
            TextView item_main_sort_name;
            TextView item_main_sort_jifen, item_main_sort_xiaoliang;
            TextView item_main_sort_price, item_main_sort_price_yuan;
        }
    }


    public void MyReCeverMsg(BMessage msg) {
//        fSortGoofdContainer.setColorList(R.drawable.selector_menu_item_text); //正常selector_menu_item_text // 正常selector_menu_item_text//非正常R.drawable.select_sort_up1
        switch (msg.getMessageType()) {

            case 2111:
                SortSortClick = false;
                SortShaiXuan();
                break;
            case 9901://筛选条件完成开始请求数据
                CurrentPage = 1;
                IsHaveSort = true;
                SecondSortId = msg.getSecondSortId();
                PriceSort = msg.getPriceSort();
                ScoreSort = msg.getScoreSort();
                BrandName = msg.getBrandSort();
                //获取位置
                SecondSortId_Postion = msg.getSecondSortId_Postion();
                PriceSort_Postion = msg.getPriceSort_Postion();
                ScoreSort_Postion = msg.getScoreSort_Postion();
                BrandName_Postion = msg.getBrandSort_Postion();

                //获取是否自定义
                IsSort_Rang_Price_ZiDingYi = msg.isSort_Price_ZiDingYi();
                IsSort_Rang_Score_ZiDingYi = msg.isSort_Score_ZiDingYi();

//if(SecondSortId_Postion==-1&&PriceSort_Postion==-1&&ScoreSort_Postion==-1&&BrandName_Postion==-1){return ;}
                if (SortZongHe) //综合被点击{
                    GetGoodsLs(CurrentPage, "weight", true, INITIALIZE);
               else if (SortPriceUp == 1) //价格升序
                    GetGoodsLs(CurrentPage, "sell_price", true, INITIALIZE);
               else if (SortPriceUp == 2) //价格降序
                    GetGoodsLs(CurrentPage, "sell_price", false, INITIALIZE);
              else  if (SortJiFenClick) //积分升序
                    GetGoodsLs(CurrentPage, "score", true, INITIALIZE);
              else  if (SortSellNumberClick)
                    GetGoodsLs(CurrentPage, "sales", true, INITIALIZE);else;
                PromptManager.ShowCustomToast(BaseContext, String.format("我的二级分类:%s，我的最大价格:%s,我的最大积分:%s,我的品牌名字:%s", SecondSortId, PriceSort.getMax(), ScoreSort.getMax(), BrandName));

                break;
        }
    }

    /**
     * 要确定是放在fragment里面还是放在外边tabactivity里面
     */
    private void ISaveSortTiaoJian() {
        Net_Hind_Rang_Price();
        Net_Hind_Rang_Scro();
        Net_Hind_HindBrandLs();
    }

    /**
     * 获取品牌的数据
     */
    private void Net_Hind_HindBrandLs() {
        //先判断是否存在品牌的缓存数据
        NHttpBaseStr mbrandNHttpBaseStr = new NHttpBaseStr(BaseContext);
        mbrandNHttpBaseStr.setPostResult(new IHttpResult<String>() {
            @Override
            public void getResult(int Code, String Msg, String Data) {
                CacheUtil.HomeSort_Brand_Save(BaseContext, Data);
            }

            @Override
            public void onError(String error, int LoadType) {
            }
        });
        HashMap<String, String> map = new HashMap<>();
        mbrandNHttpBaseStr.getData(Constants.BrandsLs, map, Request.Method.GET);


    }

    /**
     * 偷偷获取 价格的 区间列表
     */
    private void Net_Hind_Rang_Price() {
        //先判断是否存在 的缓存数据

        NHttpBaseStr mbrandNHttpBaseStr = new NHttpBaseStr(BaseContext);
        mbrandNHttpBaseStr.setPostResult(new IHttpResult<String>() {
            @Override
            public void getResult(int Code, String Msg, String Data) {
                CacheUtil.HomeSort_Price_Range_Save(BaseContext, Data);
            }

            @Override
            public void onError(String error, int LoadType) {

            }
        });
        HashMap<String, String> map = new HashMap<>();
        mbrandNHttpBaseStr.getData(Constants.MainSort_Price_Rang, map, Request.Method.GET);
    }

    /**
     * 偷偷获取积分的区间
     */
    private void Net_Hind_Rang_Scro() {
        NHttpBaseStr mbrandNHttpBaseStr = new NHttpBaseStr(BaseContext);
        mbrandNHttpBaseStr.setPostResult(new IHttpResult<String>() {
            @Override
            public void getResult(int Code, String Msg, String Data) {
                CacheUtil.HomeSort_Scroe_Range_Save(BaseContext, Data);
            }

            @Override
            public void onError(String error, int LoadType) {

            }
        });
        HashMap<String, String> map = new HashMap<>();
        mbrandNHttpBaseStr.getData(Constants.MainSort_Score_Rang, map, Request.Method.GET);
    }

}
