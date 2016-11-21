package io.vtown.WeiTangApp.test;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.easy.mainsort.BSortGood;
import io.vtown.WeiTangApp.ui.ABase;

/**
 * Created by datutu on 2016/11/21.
 */

public class ASwipLoadTest extends ABase {
    private RecyclerView swip_recyclerview;
    private SwipeToLoadLayout swipelayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_swipload);
        swip_recyclerview= (RecyclerView) findViewById(R.id.swipe_target);

        LinearLayoutManager     layoutManager = new LinearLayoutManager(BaseContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        swip_recyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        swipelayout= (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
//        SwipHead swipe_refresh_header = (SwipHead) findViewById(R.id.head);
//        SwipFoot swipe_load_more_footer = (SwipFoot) findViewById(R.id.foots);
//
//        swipelayout.setRefreshHeaderView(swipe_refresh_header);
//        swipelayout.setLoadMoreFooterView(swipe_load_more_footer);

        swip_recyclerview.setAdapter(new SortRecycleApa());
        swipelayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipelayout.setRefreshing(false);
            }
        });
        swipelayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                swipelayout.setLoadingMore(false);
            }
        });
    }

    class SortRecycleApa extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//        private List<BSortGood> SortDatas = new ArrayList<>();
//
//        public void FrashData(List<BSortGood> ds) {
//            this.SortDatas = ds;
//            this.notifyDataSetChanged();
//        }
//
//        public void AddFrashData(List<BSortGood> dd) {
//            this.SortDatas.addAll(dd);
//            this.notifyDataSetChanged();
//
//        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item_swipe, parent, false);

            return new SortHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            SortHolder holder1= (SortHolder) holder;
        }

        @Override
        public int getItemCount() {
            return 31;
        }

        //注解Holder
        class SortHolder extends RecyclerView.ViewHolder {

            SortHolder(View view) {
                super(view);
            }
        }
    }
}
