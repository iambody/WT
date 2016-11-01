package io.vtown.WeiTangApp.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.mainsort.BSortCategory;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
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

    //筛选的pop
    private PMainTabSort pMainTabSort;
    //配置

    private List<BSortCategory> MySortCategory;

    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_main_sort, null);
        ButterKnife.bind(this, BaseView);
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
    }


    @OnClick({R.id.fragment_main_sort_sou_lay, R.id.sort_good_zonghe, R.id.sort_good_price, R.id.sort_good_jifen, R.id.sort_good_xiaoliang, R.id.sort_good_shaixuan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sort_good_zonghe://点击综合&&点击综合价格//积分//销量//全部清空
                break;
            case R.id.sort_good_price://点击价格
                break;
            case R.id.sort_good_jifen://点击积分
                break;
            case R.id.sort_good_xiaoliang://点击销量

                break;
            case R.id.sort_good_shaixuan://点击筛选
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, AMianSort.class));

                break;
            case R.id.fragment_main_sort_sou_lay://点击搜索
                break;
        }
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
            PromptManager.ShowCustomToast(BaseContext, "位置" + position);
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


}
