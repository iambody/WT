package io.vtown.WeiTangApp.test;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.easy.mainsort.BSortGood;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.view.custom.recycle.LoadMoreRecyclerView;
import io.vtown.WeiTangApp.ui.ABase;

/**
 * Created by datutu on 2016/11/16.
 */

public class ALoadmor extends ABase {
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private LoadMoreRecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button qiehuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_loadmore);
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter();
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        recyclerView = (LoadMoreRecyclerView) findViewById(R.id.list);
        qiehuan= (Button) findViewById(R.id.qiehuan);
        qiehuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //切换网格模式
                myItemRecyclerViewAdapter.switchMode(true);
                recyclerView.switchLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                //切换正常模式
//                myItemRecyclerViewAdapter.switchMode(false);
//                recyclerView.switchLayoutManager(new LinearLayoutManager(ALoadmor.this));
            }
        });
        recyclerView.setLoadingMore(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myItemRecyclerViewAdapter);
        recyclerView.setAutoLoadMoreEnable(true);
//        recyclerView.notifyMoreFinish(false);
//        myItemRecyclerViewAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);

            }
        });
        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PromptManager.ShowCustomToast(BaseContext, "加载更多");
//                        swipeRefreshLayout.setRefreshing(false);
                         recyclerView.notifyMoreFinish(false);
                    }
                }, 1000);
            }
        });

//        recyclerView.setVisibility(View.GONE);
//        recyclerView.getAdapter().notifyItemRemoved(recyclerView.getmLoadMorePosition());

//        recyclerView.setAutoLoadMoreEnable(true);

    }


    public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<BSortGood> mValues = new ArrayList<>();
        private boolean mIsStagger;

//        public MyItemRecyclerViewAdapter(List<BSortGood> items) {
//            mValues = items;
//        }

        public MyItemRecyclerViewAdapter() {
        }

        public void switchMode(boolean mIsStagger) {
            this.mIsStagger = mIsStagger;
        }

        public void setData(List<BSortGood> datas) {
            mValues = datas;
        }

        public void addDatas(List<BSortGood> datas) {
            mValues.addAll(datas);
        }



        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == LoadMoreRecyclerView.TYPE_STAGGER) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_item_sort1_stagger, parent, false);
                return new StaggerViewHolder(view);
            } else {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_item_sort1_nor, parent, false);
                return new ViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            BSortGood StaggerData = mValues.get(position);
            if (mIsStagger) {
                StaggerViewHolder staggerViewHolder = (StaggerViewHolder) holder;


            } else {
                ViewHolder mHolder = (ViewHolder) holder;

            }
        }

        @Override
        public int getItemCount() {
            return 25;
//            return mValues.size();
        }

        //九宫格的Holder
        public class StaggerViewHolder extends RecyclerView.ViewHolder {
            public View mView;
            public TextView fragment_item_sort_stagger_left_txt;
            public TextView fragment_item_sort_stagger_right_txt;

            public StaggerViewHolder(View itemView) {
                super(itemView);
                mView = itemView;
                fragment_item_sort_stagger_left_txt = (TextView) itemView.findViewById(R.id.fragment_item_sort_stagger_left_txt);
                fragment_item_sort_stagger_right_txt = (TextView) itemView.findViewById(R.id.fragment_item_sort_stagger_right_txt);
            }
        }

        //正常的Holder
        public class ViewHolder extends RecyclerView.ViewHolder {
            public View mView;

            public TextView fragment_item_sort_up_txt;
            public TextView fragment_item_sort_down_txt;

            public BSortGood mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                fragment_item_sort_up_txt = (TextView) view.findViewById(R.id.fragment_item_sort_up_txt);
                fragment_item_sort_down_txt = (TextView) view.findViewById(R.id.fragment_item_sort_down_txt);
            }

//            @Override
//            public String toString() {
//                return super.toString() + " '" + mContentView.getText() + "'";
//            }
        }


    }

}
