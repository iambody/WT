package io.vtown.WeiTangApp.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.RecyclerCommentItemDecoration;
import io.vtown.WeiTangApp.comment.view.custom.EndlessRecyclerOnScrollListener;
import io.vtown.WeiTangApp.comment.view.custom.HeaderViewRecyclerAdapter;
import io.vtown.WeiTangApp.comment.view.custom.horizontalscroll.HBaseAdapter;
import io.vtown.WeiTangApp.comment.view.custom.horizontalscroll.HorizontalScrollMenu;
import io.vtown.WeiTangApp.comment.view.pop.PMainTabSort;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.ui.comment.AMianSort;

/**
 * Created by datutu on 2016/10/28.
 * 首页分类
 */

public class FMainSort extends FBase {
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
//    @BindView(R.id.fragment_goodsort_recyclerview)
    RecyclerView fragmentGoodsortRecyclerview;

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
    //一个标记去记录是否已经筛选过
    private boolean IsHaveSort = false;
    //需要保存我筛选过的记录条件仅仅是供给一级分类刚筛选过又要筛选的 如果以及分类变了需要进行重置
    private String SecondSortId;
    private BSortRang PriceSort;
    private BSortRang ScoreSort;
    private String BrandName;
    //记录位置
    private int SecondSortId_Postion;
    private int PriceSort_Postion;
    private int ScoreSort_Postion;
    private int BrandName_Postion;

    //TODO在进这个fragment时候需要偷偷加载/刷新已经缓存过的筛选的价格区间积分去接纳品牌名称列表


    //开始进行
    private MySortAdapter mySortAdapter;
    private HeaderViewRecyclerAdapter MyOutSortAdapter;
    //recyleview 的布局管理器
    LinearLayoutManager SortLayoutManager;
    //是否正在加载更多
    private boolean IsLoadingMore;
    // 条数小于20时候不可以加载更多
    private boolean IsCanLoadMore;

    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_main_sort, null);
        ButterKnife.bind(this, BaseView);
        EventBus.getDefault().register(this, "MyReCeverMsg", BMessage.class);
        SetTitleHttpDataLisenter(this);
        //必需先确保一级分类存在******不存在就立即进行获取
        ICacheCategory();


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

    }

    private void IBase() {
        fragmentGoodsortRecyclerview= (RecyclerView) BaseView.findViewById(R.id.fragment_goodsort_recyclerview);
        fSortGoofdContainer.setSwiped(false);
        fSortGoofdContainer.SetLayoutColor(getResources().getColor(R.color.app_fen));
        fSortGoofdContainer.setMenuItemPaddingLeft(50);
        fSortGoofdContainer.setMenuItemPaddingRight(50);
        fSortGoofdContainer.SetBgColo(getResources().getColor(R.color.app_fen));
        fSortGoofdContainer.setColorList(R.drawable.selector_menu_item_text1);
        fSortGoofdContainer.setCheckedBackground(R.color.app_fen);
//        fSortGoofdContainer.SetCheckedTxtColor(getResources().getColor(R.color.gold));
        fSortGoofdContainer.setAdapter(new SortMenuAdapter());


        //上边选择的textview的设置

//        ChangGrad(false);


        SortLayoutManager = new LinearLayoutManager(BaseContext);
        fragmentGoodsortRecyclerview.setLayoutManager(SortLayoutManager);
        fragmentGoodsortRecyclerview.addItemDecoration(new RecyclerCommentItemDecoration(BaseContext, RecyclerCommentItemDecoration.VERTICAL_LIST, R.drawable.shape_show_divider_line));


        mySortAdapter = new MySortAdapter();
        MyOutSortAdapter = new HeaderViewRecyclerAdapter(mySortAdapter);
        fragmentGoodsortRecyclerview.setAdapter(MyOutSortAdapter);
        fragmentGoodsortRecyclerview.addOnScrollListener(new EndlessRecyclerOnScrollListener(SortLayoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                PromptManager.ShowCustomToast(BaseContext, "开始sss");
                createLoadMoreView9();
//                if (!IsLoadingMore) {
//                    if (true) {
//                        PromptManager.ShowCustomToast(BaseContext, "开始sss");
//                        createLoadMoreView9();
//                    }
//                }
            }
        });
    }

    /**
     * 开始显示
     */
    private void createLoadMoreView9() {
        View loadMoreView = LayoutInflater
                .from(BaseActivity)
                .inflate(R.layout.swiperefresh_footer, fragmentGoodsortRecyclerview, false);
        MyOutSortAdapter.addFooterView(loadMoreView);
//        IsLoadingMore = true;
//        IData(lastid, LOAD_LOADMOREING);

    }

    private void HindLoadMore() {
        MyOutSortAdapter.RemoveFooterView();
        IsLoadingMore = false;
    }

    private void ChangGrad(boolean IsGrad) {
        if (IsGrad) {
            SortLayoutManager = new GridLayoutManager(BaseContext, 2);
        } else {
            SortLayoutManager = new LinearLayoutManager(BaseContext);
            SortLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        }
        fragmentGoodsortRecyclerview.setLayoutManager(SortLayoutManager);
        fragmentGoodsortRecyclerview.addItemDecoration(new RecyclerCommentItemDecoration(BaseContext, RecyclerCommentItemDecoration.VERTICAL_LIST, R.drawable.shape_show_divider_line));

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
                break;
            case 2:
                sortGoodPrice.setTextColor(getResources().getColor(R.color.app_fen));
                sortGoodPriceIv.setImageResource(R.drawable.sort_price_down);
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
                if (!SortZongHe)
                    ResetSort();
                //  //开始请求数据！！！！！！！！！！！

                break;
            case R.id.sort_good_price_lay://点击价格
                SortZongHe = false;
                sortGoodZonghe.setTextColor(getResources().getColor(R.color.gray));

                PriceColorControl(false);
                break;
            case R.id.sort_good_jifen://点击积分
                SortZongHe = false;
                sortGoodZonghe.setTextColor(getResources().getColor(R.color.gray));
                if (!SortJiFenClick) {
                    SortJiFenClick = true;
                    sortGoodJifen.setTextColor(getResources().getColor(R.color.app_fen));
                    //开始请求数据！！！！！！！！！！！
                }
                break;
            case R.id.sort_good_xiaoliang://点击销量
                SortZongHe = false;
                sortGoodZonghe.setTextColor(getResources().getColor(R.color.gray));
                if (!SortSellNumberClick) {
                    SortSellNumberClick = true;
                    sortGoodXiaoliang.setTextColor(getResources().getColor(R.color.app_fen));
                    //开始请求数据！！！！！！！！！！！
                }
                break;
            case R.id.sort_good_shaixuan_lay://点击筛选
                SortSortClick = !SortSortClick;
                SortShaiXuan();
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
                break;
        }
    }

    private void ResetSort() {
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
            UpSortPostion = position;
//            PromptManager.ShowCustomToast(BaseContext, "位置==>" + MySortCategory.get(position).getCate_name());
        }

    }

    @Override
    public void InitCreate(Bundle d) {

    }

    @Override
    public void getResult(int Code, String Msg, BComment Data) {

    }

    @Override
    public void onError(String error, int LoadType) {

    }

    /**
     * 商品列表的AP
     */

   public class MySortAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<BSortGood> MyDatas = new ArrayList<>();

        public MySortAdapter() {
        }

        //刷新
        public void FrashAp(List<BSortGood> daaa) {
            this.MyDatas = daaa;
            this.notifyDataSetChanged();
        }

        //添加刷新
        public void AddFrashAp(List<BSortGood> daaa) {
            this.MyDatas.addAll(daaa);
            this.notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_item_goodsort, null);
            SortHolder holder = new SortHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            SortHolder MyHolder = (SortHolder) holder;

        }

        @Override
        public int getItemCount() {
            return 15;
        }

        class SortHolder extends RecyclerView.ViewHolder {
            private TextView ddddddd;

            public SortHolder(View itemView) {
                super(itemView);
                ddddddd= (TextView) itemView.findViewById(R.id.ll);
            }
        }
    }


    public void MyReCeverMsg(BMessage msg) {
        switch (msg.getMessageType()) {
            case 2111:
                SortSortClick = false;
                SortShaiXuan();
                break;
            case 9901://筛选条件完成开始请求数据
                IsHaveSort = true;
                String SecondSortId = msg.getSecondSortId();
                BSortRang PriceSort = msg.getPriceSort();
                BSortRang ScoreSort = msg.getScoreSort();
                String BrandName = msg.getBrandSort();
//获取位置
                SecondSortId_Postion = msg.getSecondSortId_Postion();
                PriceSort_Postion = msg.getPriceSort_Postion();
                ScoreSort_Postion = msg.getScoreSort_Postion();
                BrandName_Postion = msg.getBrandSort_Postion();


                PromptManager.ShowCustomToast(BaseContext, String.format("我的二级分类:%s，我的最大价格:%s,我的最大积分:%s,我的品牌名字:%s", SecondSortId, PriceSort.getMax(), ScoreSort.getMax(), BrandName));

                break;
        }
    }
}
