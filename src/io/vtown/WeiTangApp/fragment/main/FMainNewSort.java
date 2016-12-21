package io.vtown.WeiTangApp.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

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
import io.vtown.WeiTangApp.comment.view.custom.horizontalscroll.HBaseAdapter;
import io.vtown.WeiTangApp.comment.view.custom.horizontalscroll.HorizontalScrollMenu;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.ui.AGoodSort;

/**
 * Created by datutu on 2016/12/19.
 */

public class FMainNewSort extends FBase {

    @BindView(R.id.f_main_new_new_sort_sou)
    ImageView fMainNewNewSortSou;
    @BindView(R.id.f_sort_new_container)
    HorizontalScrollMenu fSortNewContainer;
    @BindView(R.id.f_sort_new_horizontalscrollview)
    HorizontalScrollView fSortNewHorizontalscrollview;
    @BindView(R.id.f_sort_new_horizontalscrollview_lay)
    LinearLayout fSortNewHorizontalscrollviewLay;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeToLoadLayout swipeToLoadLayout;


    private List<BSortCategory> MainSortCategory = new ArrayList<>();
    private List<String> MainSortCateorys;

    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_main_new_sort, null);
        ButterKnife.bind(this, BaseView);
        IView();
    }

    private void IView() {
        IBaseSort();//wewew一级分类

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

        for (int i = 0; i < 10; i++) {

            LinearLayout.LayoutParams psa = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            psa.setMargins(20, 5, 20, 5);
            TextView txt = new TextView(BaseContext);
            txt.setText("测试案例");
            txt.setLayoutParams(psa);
            fSortNewHorizontalscrollviewLay.addView(txt);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
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
            PromptManager.ShowCustomToast(BaseContext, String.format("第%s页", position + ""));
        }
    }

    @OnClick({R.id.f_main_new_new_sort_sou})
    public void onClick(View V) {
        switch (V.getId()) {
            case R.id.f_main_new_new_sort_sou:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, AGoodSort.class));
                break;
        }
    }
}
