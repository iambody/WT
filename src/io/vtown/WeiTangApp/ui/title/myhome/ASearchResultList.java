package io.vtown.WeiTangApp.ui.title.myhome;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.three_one.search.BLSearchShopAndGood;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.RefreshLayout;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;

/**
 * Created by Yihuihua on 2016/11/2.
 */

public class ASearchResultList extends ATitleBase implements RefreshLayout.OnLoadListener, AdapterView.OnItemClickListener {


    @BindView(R.id.search_result_list)
    ListView searchResultList;
    @BindView(R.id.search_result_list_refrash)
    RefreshLayout searchResultListRefrash;
    @BindView(R.id.search_result_list_nodata_lay)
    View search_result_list_nodata_lay;
    private Unbinder mBinder;
    private String title;
    private int page = 1;
    private int Show_Type;

    private List<BLSearchShopAndGood> datas = new ArrayList<BLSearchShopAndGood>();
    private SearchResultAdapter mSearchResultAdapter;
    private String content;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_search_result_list);
        mBinder = ButterKnife.bind(this);
        IBundle();
        IView();
    }


    private void IBundle() {


        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("resultinfo")) {
            Bundle bundle = getIntent().getBundleExtra("resultinfo");
            Show_Type = bundle.getInt("show_type");
            title = bundle.getString("title");
            content = bundle.getString("content");
            datas = JSON.parseArray(content, BLSearchShopAndGood.class);
        }
    }

    private void IView() {


        searchResultListRefrash.setOnLoadListener(this);
        searchResultListRefrash.setColorSchemeResources(R.color.app_fen, R.color.app_fen1, R.color.app_fen2, R.color.app_fen3);
        searchResultListRefrash.setCanLoadMore(false);
        mSearchResultAdapter = new SearchResultAdapter();
        searchResultList.setAdapter(mSearchResultAdapter);
        searchResultList.setOnItemClickListener(this);
        if (datas.size() == Constants.PageSize) {
            searchResultListRefrash.setCanLoadMore(true);
        } else {
            searchResultListRefrash.setCanLoadMore(false);
        }
    }

    private void IData(int loadtype) {
        SetTitleHttpDataLisenter(this);
        HashMap<String, String> map = new HashMap<>();
        map.put("keyword", title);
        map.put("page", page + "");
        map.put("pagesize", Constants.PageSize + "");
        String host = "";
        switch (Show_Type) {
            case 1:
                host = Constants.Search_Shoplist;
                break;

            case 2:
                host = Constants.Search_Goodinfo;
                break;
        }
        FBGetHttpData(map, host, Request.Method.GET, 0, loadtype);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt(title);
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        switch (Data.getHttpLoadType()) {
            case LOAD_REFRESHING:
                datas = JSON.parseArray(Data.getHttpResultStr(), BLSearchShopAndGood.class);
                searchResultListRefrash.setRefreshing(false);
                if (datas.size() == Constants.PageSize) {
                    searchResultListRefrash.setCanLoadMore(true);
                } else {
                    searchResultListRefrash.setCanLoadMore(false);
                }
                mSearchResultAdapter.notifyDataSetChanged();
                break;

            case LOAD_LOADMOREING:
                List<BLSearchShopAndGood> data_more = new ArrayList<BLSearchShopAndGood>();
                data_more = JSON.parseArray(Data.getHttpResultStr(), BLSearchShopAndGood.class);
                searchResultListRefrash.setLoading(false);
                if (data_more.size() == Constants.PageSize) {
                    searchResultListRefrash.setCanLoadMore(true);
                } else {
                    searchResultListRefrash.setCanLoadMore(false);
                }
                datas.addAll(data_more);
                mSearchResultAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.ShowCustomToast(BaseContext, error);
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
    protected void onDestroy() {
        super.onDestroy();
        mBinder.unbind();
    }

    @Override
    public void OnLoadMore() {
        page += 1;
        IData(LOAD_LOADMOREING);
    }

    @Override
    public void OnFrash() {
        page = 1;
        IData(LOAD_REFRESHING);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BLSearchShopAndGood item = (BLSearchShopAndGood) mSearchResultAdapter.getItem(position);
        switch (Show_Type) {
            case 1:
                BComment bComment = new BComment(item.getId(), item.getSeller_name());
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext, AShopDetail.class).putExtra(BaseKey_Bean, bComment));
                break;

            case 2:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext, AGoodDetail.class).putExtra("goodid", item.getId()));
                break;
        }

    }


    class SearchResultAdapter extends BaseAdapter {

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
            ShopsHolder holder = null;
            GoodsHolder goods = null;
            if (convertView == null) {
                switch (Show_Type) {
                    case 1:
                        convertView = LayoutInflater.from(BaseContext).inflate(R.layout.item_search_result_all_shops, null);
                        holder = new ShopsHolder(convertView);
                        convertView.setTag(holder);
                        break;

                    case 2:
                        convertView = LayoutInflater.from(BaseContext).inflate(R.layout.item_search_result_all_goods, null);
                        goods = new GoodsHolder(convertView);
                        convertView.setTag(goods);
                        break;
                }


            } else {
                switch (Show_Type) {
                    case 1:
                        holder = (ShopsHolder) convertView.getTag();
                        break;

                    case 2:
                        goods = (GoodsHolder) convertView.getTag();
                        break;
                }

            }
            BLSearchShopAndGood blSearchShopAndGood = datas.get(position);

            switch (Show_Type) {
                case 1:
                    ImageLoaderUtil.Load2(blSearchShopAndGood.getAvatar(), holder.ivSearchResultAllShopIcon, R.drawable.error_iv2);
                    StrUtils.SetTxt(holder.tvSearchResultAllShopName, blSearchShopAndGood.getSeller_name());
                    if(StrUtils.isEmpty(blSearchShopAndGood.getIntro())){
                        StrUtils.SetTxt(holder.tvSearchResultAllShopDesc,"店铺暂时还没有相关描述，敬请期待……");
                    }else{
                        StrUtils.SetTxt(holder.tvSearchResultAllShopDesc, blSearchShopAndGood.getIntro());
                }

                    break;

                case 2:
                    ImageLoaderUtil.Load2(blSearchShopAndGood.getCover(), goods.ivSearchResultAllGoodIcon, R.drawable.error_iv2);
                    StrUtils.SetTxt(goods.tvSearchResultAllGoodName, blSearchShopAndGood.getTitle());
                    if (0 == blSearchShopAndGood.getIs_agent()) {
                        goods.ivSearchResultAllGoodLevel.setVisibility(View.GONE);
                    } else {
                        goods.ivSearchResultAllGoodLevel.setVisibility(View.VISIBLE);
                    }
                    StrUtils.SetMoneyFormat(BaseContext, goods.tvSearchResultAllGoodPrice, blSearchShopAndGood.getSell_price(), 15);
                    if ("0".equals(blSearchShopAndGood.getOrig_price()) || StrUtils.isEmpty(blSearchShopAndGood.getOrig_price())) {
                        goods.tvSearchResultAllGoodOrigprice.setVisibility(View.INVISIBLE);
                    } else {
                        goods.tvSearchResultAllGoodOrigprice.setVisibility(View.VISIBLE);
                        StrUtils.SetTxt(goods.tvSearchResultAllGoodOrigprice, StrUtils.SetTextForMony(blSearchShopAndGood.getOrig_price()));
                        goods.tvSearchResultAllGoodOrigprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    }

                    if (blSearchShopAndGood.getScore() > 0) {
                        goods.tvSearchResultAllGoodScore.setVisibility(View.VISIBLE);
                        StrUtils.SetTxt(goods.tvSearchResultAllGoodScore, "积分：" + blSearchShopAndGood.getScore());
                    } else {
                        goods.tvSearchResultAllGoodScore.setVisibility(View.GONE);
                    }

                    if (blSearchShopAndGood.getSales() > 0) {
                        goods.tvSearchResultAllGoodSales.setVisibility(View.VISIBLE);
                        StrUtils.SetTxt(goods.tvSearchResultAllGoodSales, "销量：" + blSearchShopAndGood.getSales() + "件");
                    } else {
                        goods.tvSearchResultAllGoodSales.setVisibility(View.GONE);
                    }
                    break;
            }

            return convertView;
        }

    }

    class ShopsHolder {
        @BindView(R.id.iv_search_result_all_shop_icon)
        ImageView ivSearchResultAllShopIcon;
        @BindView(R.id.tv_search_result_all_shop_name)
        TextView tvSearchResultAllShopName;
        @BindView(R.id.tv_search_result_all_shop_desc)
        TextView tvSearchResultAllShopDesc;
        @BindView(R.id.all_shop_line)
        View allShopLine;

        ShopsHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class GoodsHolder {
        @BindView(R.id.iv_search_result_all_good_icon)
        ImageView ivSearchResultAllGoodIcon;
        @BindView(R.id.iv_search_result_all_good_level)
        ImageView ivSearchResultAllGoodLevel;
        @BindView(R.id.tv_search_result_all_good_name)
        TextView tvSearchResultAllGoodName;
        @BindView(R.id.tv_search_result_all_good_price)
        TextView tvSearchResultAllGoodPrice;
        @BindView(R.id.tv_search_result_all_good_origprice)
        TextView tvSearchResultAllGoodOrigprice;
        @BindView(R.id.tv_search_result_all_good_sales)
        TextView tvSearchResultAllGoodSales;
        @BindView(R.id.tv_search_result_all_good_score)
        TextView tvSearchResultAllGoodScore;

        GoodsHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
