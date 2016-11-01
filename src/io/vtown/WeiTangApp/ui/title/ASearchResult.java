package io.vtown.WeiTangApp.ui.title;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import io.vtown.WeiTangApp.bean.bcomment.three_one.search.BCSearchInfo;
import io.vtown.WeiTangApp.bean.bcomment.three_one.search.BLSearchShopAndGood;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by Yihuihua on 2016/11/1.
 */

public class ASearchResult extends ATitleBase {
    @BindView(R.id.tv_search_result_all_shops)
    TextView tvSearchResultAllShops;
    @BindView(R.id.lv_search_result_shops)
    CompleteListView lvSearchResultShops;
    @BindView(R.id.tv_search_result_all_goods)
    TextView tvSearchResultAllGoods;
    @BindView(R.id.lv_search_result_goods)
    CompleteListView lvSearchResultGoods;
    @BindView(R.id.ll_search_shops)
    LinearLayout llSearchShops;
    @BindView(R.id.ll_search_goods)
    LinearLayout llSearchGoods;
    private Unbinder mBinder;
    private ShopResultAdapter mShopResultAdapter;
    private GoodResultAdapter mGoodResultAdapter;
    private String search_key;

    private BCSearchInfo info;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_search_result);
        mBinder = ButterKnife.bind(this);
        IBundle();
        IData();
        IListView();
    }

    private void IBundle() {
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("search_key")) {
            search_key = getIntent().getStringExtra("search_key");
        }
    }

    private void IData() {
        SetTitleHttpDataLisenter(this);
        HashMap<String, String> map = new HashMap<>();
        map.put("version", "3.0.1");
        map.put("keyword", search_key);
        FBGetHttpData(map, Constants.Search_Shop_Good, Request.Method.GET, 0, LOAD_INITIALIZE);
    }

    private void IListView() {
        mShopResultAdapter = new ShopResultAdapter();
        lvSearchResultShops.setAdapter(mShopResultAdapter);
        mGoodResultAdapter = new GoodResultAdapter();
        lvSearchResultGoods.setAdapter(mGoodResultAdapter);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt(search_key);
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        if (StrUtils.isEmpty(Data.getHttpResultStr())) {
            return;
        }
        info = JSON.parseObject(Data.getHttpResultStr(),BCSearchInfo.class);
        if (info.getSellerinfo() == null) {
            llSearchShops.setVisibility(View.GONE);
        } else {
            llSearchShops.setVisibility(View.VISIBLE);
            mShopResultAdapter.RefreshShop(info.getSellerinfo());
        }

        if (info.getGoodsinfo()== null) {
            llSearchGoods.setVisibility(View.GONE);
        } else {
            llSearchGoods.setVisibility(View.VISIBLE);
            mGoodResultAdapter.RefreshGood(info.getSellerinfo());
        }

    }

    @Override
    protected void DataError(String error, int LoadType) {

    }

    @Override
    protected void NetConnect() {

    }

    @Override
    protected void NetDisConnect() {

    }

    @Override
    protected void SetNetView() {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    class ShopResultAdapter extends BaseAdapter {

        private List<BLSearchShopAndGood> datas = new ArrayList<BLSearchShopAndGood>();

        public ShopResultAdapter() {
            super();
        }

        @Override
        public int getCount() {
            if(datas.size()<3){
                return datas.size();
            }else{
                return 3;
            }

        }

        public void RefreshShop(List<BLSearchShopAndGood> datas) {
            this.datas = datas;
            this.notifyDataSetChanged();
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
            if (null == convertView) {
                convertView = LayoutInflater.from(BaseContext).inflate(R.layout.item_search_result_shops, null);
                holder = new ShopsHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ShopsHolder) convertView.getTag();
            }
            BLSearchShopAndGood blSearchShopAndGood = datas.get(position);
            ImageLoaderUtil.Load2(blSearchShopAndGood.getAvatar(),holder.ivSearchResultShopIcon,R.drawable.error_iv2);
            StrUtils.SetTxt(holder.tvSearchResultShopName,blSearchShopAndGood.getSeller_name());

            return convertView;
        }


    }

    class ShopsHolder {
        @BindView(R.id.iv_search_result_shop_icon)
        ImageView ivSearchResultShopIcon;
        @BindView(R.id.tv_search_result_shop_name)
        TextView tvSearchResultShopName;
        @BindView(R.id.tv_search_result_shop_desc)
        TextView tvSearchResultShopDesc;
        @BindView(R.id.shop_line)
        View shopLine;

        ShopsHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    class GoodResultAdapter extends BaseAdapter {
        private List<BLSearchShopAndGood> datas = new ArrayList<BLSearchShopAndGood>();

        public GoodResultAdapter() {
            super();
        }

        @Override
        public int getCount() {
            if (datas.size() < 5) {
                return datas.size();
            } else {
                return 5;
            }

        }

        public void RefreshGood(List<BLSearchShopAndGood> datas) {
            this.datas = datas;
            this.notifyDataSetChanged();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            GoodsHolder holder = null;
            if (null == convertView) {
                convertView = LayoutInflater.from(BaseContext).inflate(R.layout.item_search_result_goods, null);
                holder = new GoodsHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (GoodsHolder) convertView.getTag();
            }
            BLSearchShopAndGood blSearchShopAndGood = datas.get(position);
            ImageLoaderUtil.Load2(blSearchShopAndGood.getCover(), holder.ivSearchResultGoodIcon, R.drawable.error_iv2);
            if (1 == blSearchShopAndGood.getIs_agent()) {
                StrUtils.setTxtLeftDrawable(BaseContext, holder.tvSearchResultGoodName);
            }
            StrUtils.SetTxt(holder.tvSearchResultGoodName, blSearchShopAndGood.getTitle());
            StrUtils.SetMoneyFormat(BaseContext, holder.tvSearchResultGoodPrice, blSearchShopAndGood.getSell_price(), 17);
            if ("0".equals(blSearchShopAndGood.getOrig_price()) && StrUtils.isEmpty(blSearchShopAndGood.getOrig_price())) {
                holder.tvSearchResultGoodOrigprice.setVisibility(View.GONE);
            } else {
                holder.tvSearchResultGoodOrigprice.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(holder.tvSearchResultGoodOrigprice, StrUtils.SetTextForMony(blSearchShopAndGood.getOrig_price()));
                holder.tvSearchResultGoodOrigprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            return convertView;
        }


    }

    class GoodsHolder {
        @BindView(R.id.iv_search_result_good_icon)
        ImageView ivSearchResultGoodIcon;
        @BindView(R.id.iv_search_result_good_level)
        ImageView ivSearchResultGoodLevel;
        @BindView(R.id.tv_search_result_good_name)
        TextView tvSearchResultGoodName;
        @BindView(R.id.tv_search_result_good_price)
        TextView tvSearchResultGoodPrice;
        @BindView(R.id.tv_search_result_good_origprice)
        TextView tvSearchResultGoodOrigprice;

        GoodsHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinder.unbind();
    }
}
