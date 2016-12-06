package io.vtown.WeiTangApp.ui.ui;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.bean.bcomment.three_one.search.BLSearchShopAndGood;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by Yihuihua on 2016/12/6.
 */
public class AAddShowGoodLs extends ATitleBase implements OnLoadMoreListener, OnRefreshListener, AdapterView.OnItemClickListener {
    @BindView(R.id.swipe_target)
    ListView swipeTarget;
    @BindView(R.id.swipeToLoadLayout)
   SwipeToLoadLayout swipeToLoadLayout;
    private Unbinder mBinder;
    private int mPage = 1;
    private String mTitle;
    private View search_result_lv_nodata_lay;
    private List<BLSearchShopAndGood> datas = new ArrayList<BLSearchShopAndGood>();
    private ShowSearchResultAdapter mSearchResultAdapter;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_add_show_ls);
        mBinder = ButterKnife.bind(this);
        IBundle();
        IBase();
        IData(LOAD_INITIALIZE);
    }

    private void IBundle() {
        mTitle =  getIntent().getStringExtra("search_key");
    }

    private void IBase() {
        swipeToLoadLayout.setOnLoadMoreListener(this);
        swipeToLoadLayout.setOnRefreshListener(this);
        search_result_lv_nodata_lay = findViewById(R.id.search_result_lv_nodata_lay);
        mSearchResultAdapter = new ShowSearchResultAdapter();
        swipeTarget.setAdapter(mSearchResultAdapter);
        swipeTarget.setOnItemClickListener(this);
    }

    private void IData(int loadtype) {
        SetTitleHttpDataLisenter(this);
        if(loadtype == LOAD_INITIALIZE){
            PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.xlistview_header_hint_loading));
        }
        HashMap<String, String> map = new HashMap<>();
        map.put("keyword", mTitle);
        map.put("page", mPage + "");
        map.put("pagesize", Constants.PageSize + "");
        FBGetHttpData(map, Constants.Search_Goodinfo, Request.Method.GET, 0, loadtype);
    }


    @Override
    protected void InitTile() {
        SetTitleTxt(mTitle);
    }

    @Override
    public void onLoadMore() {
        mPage++;
        IData(LOAD_LOADMOREING);
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        IData(LOAD_REFRESHING);
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

        switch (Data.getHttpLoadType()) {
            case LOAD_INITIALIZE:
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    search_result_lv_nodata_lay.setVisibility(View.VISIBLE);
                    swipeToLoadLayout.setVisibility(View.GONE);
                    ShowErrorCanLoad(getResources().getString(R.string.search_result_good_null));
                    ShowErrorIv(R.drawable.error_sou);
                    search_result_lv_nodata_lay.setClickable(false);
                    return;
                }
                search_result_lv_nodata_lay.setVisibility(View.GONE);
                swipeToLoadLayout.setVisibility(View.VISIBLE);
                datas = JSON.parseArray(Data.getHttpResultStr(), BLSearchShopAndGood.class);
                if (datas.size() == Constants.PageSize) {
                    swipeToLoadLayout.setLoadMoreEnabled(true);
                } else {
                    swipeToLoadLayout.setLoadMoreEnabled(false);
                }

               mSearchResultAdapter.notifyDataSetChanged();

                break;

            case LOAD_REFRESHING:
                swipeToLoadLayout.setRefreshing(false);
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    PromptManager.ShowCustomToast(BaseContext,"没有最新的了");
                    return;
                }
                datas = JSON.parseArray(Data.getHttpResultStr(), BLSearchShopAndGood.class);
                if (datas.size() == Constants.PageSize) {
                    swipeToLoadLayout.setLoadMoreEnabled(false);
                } else {
                    swipeToLoadLayout.setLoadMoreEnabled(false);
                }
                mSearchResultAdapter.notifyDataSetChanged();
                break;

            case LOAD_LOADMOREING:
                swipeToLoadLayout.setLoadingMore(false);
                if(StrUtils.isEmpty(Data.getHttpResultStr())){
                    PromptManager.ShowCustomToast(BaseContext,"没有更多了");
                    return;
                }
                List<BLSearchShopAndGood> data_more = new ArrayList<BLSearchShopAndGood>();
                data_more = JSON.parseArray(Data.getHttpResultStr(), BLSearchShopAndGood.class);

                if (data_more.size() == Constants.PageSize) {
                    swipeToLoadLayout.setLoadMoreEnabled(true);
                } else {

                    swipeToLoadLayout.setLoadMoreEnabled(false);
                }
                datas.addAll(data_more);
                mSearchResultAdapter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.ShowCustomToast(BaseContext,error);
        if(LoadType == LOAD_INITIALIZE){
            swipeToLoadLayout.setVisibility(View.GONE);
            search_result_lv_nodata_lay.setVisibility(View.VISIBLE);
            ShowErrorCanLoad(getResources().getString(R.string.error_null_noda));
            search_result_lv_nodata_lay.setClickable(true);

        }

    }

    @Override
    protected void NetConnect() {
        NetError.setVisibility(View.GONE);

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

    }

    @Override
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BLSearchShopAndGood item = (BLSearchShopAndGood) mSearchResultAdapter.getItem(position);
        BMessage bMessage = new BMessage(BMessage.From_Search_Lv_Finish);
        bMessage.setmSearchGood(item);
        AAddShowGoodLs.this.finish();
        EventBus.getDefault().post(bMessage);

    }


    class ShowSearchResultAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder goods = null;
            if (convertView == null) {


                convertView = LayoutInflater.from(BaseContext).inflate(R.layout.item_search_result_show_lv_goods, null);
                goods = new ViewHolder(convertView);
                convertView.setTag(goods);


            } else {

                goods = (ViewHolder) convertView.getTag();

            }


            BLSearchShopAndGood blSearchShopAndGood = datas.get(position);

            ImageLoaderUtil.Load2(blSearchShopAndGood.getAvatar(), goods.ivSearchResultShowAllGoodIcon, R.drawable.error_iv2);
            StrUtils.SetTxt(goods.tvSearchResultShowAllGoodName, blSearchShopAndGood.getTitle());
            StrUtils.SetMoneyFormat(BaseContext, goods.tvSearchResultShowAllGoodPrice, blSearchShopAndGood.getSell_price(), 15);
            if ("0".equals(blSearchShopAndGood.getOrig_price()) || StrUtils.isEmpty(blSearchShopAndGood.getOrig_price())) {
                goods.tvSearchResultShowAllGoodOrigprice.setVisibility(View.INVISIBLE);
            } else {
                goods.tvSearchResultShowAllGoodOrigprice.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(goods.tvSearchResultShowAllGoodOrigprice, StrUtils.SetTextForMony(blSearchShopAndGood.getOrig_price()));
                goods.tvSearchResultShowAllGoodOrigprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }

            if (blSearchShopAndGood.getScore() > 0) {
                goods.tvSearchResultShowAllGoodScore.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(goods.tvSearchResultShowAllGoodScore, "积分：" + blSearchShopAndGood.getScore());
            } else {
                goods.tvSearchResultShowAllGoodScore.setVisibility(View.GONE);
            }

            if (blSearchShopAndGood.getSales() > 0) {
                goods.tvSearchResultShowAllGoodSales.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(goods.tvSearchResultShowAllGoodSales, "销量：" + blSearchShopAndGood.getSales() + "件");
            } else {
                goods.tvSearchResultShowAllGoodSales.setVisibility(View.GONE);
            }


            return convertView;
        }


    }

    class ViewHolder {
        @BindView(R.id.iv_search_result_show_all_good_icon)
        ImageView ivSearchResultShowAllGoodIcon;
        @BindView(R.id.tv_search_result_show_all_good_name)
        TextView tvSearchResultShowAllGoodName;
        @BindView(R.id.tv_search_result_show_all_good_sales)
        TextView tvSearchResultShowAllGoodSales;
        @BindView(R.id.tv_search_result_show_all_good_score)
        TextView tvSearchResultShowAllGoodScore;
        @BindView(R.id.tv_search_result_show_all_good_price)
        TextView tvSearchResultShowAllGoodPrice;
        @BindView(R.id.tv_search_result_show_all_good_origprice)
        TextView tvSearchResultShowAllGoodOrigprice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinder.unbind();
    }
}
