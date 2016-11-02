package io.vtown.WeiTangApp.fragment.main;

import android.content.Intent;
import android.os.Bundle;
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
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
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
                if (SortSortClick) {
                    Intent MyIntent = new Intent(BaseActivity, AMianSort.class);
                    MyIntent.putExtra("catoryid", MySortCategory.get(UpSortPostion).getId());
                    PromptManager.SkipActivity(BaseActivity, MyIntent);
                }
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

    public void MyReCeverMsg(BMessage msg) {
        switch (msg.getMessageType()) {
            case 2111:
                SortSortClick = false;
                SortShaiXuan();
                break;
        }
    }
}
