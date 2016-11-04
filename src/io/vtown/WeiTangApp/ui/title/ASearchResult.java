package io.vtown.WeiTangApp.ui.title;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.myhome.ASearchResultList;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;

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
    View search_result_nodata_lay;
    @BindView(R.id.search_result_data_lay)
    ScrollView search_result_data_lay;
    @BindView(R.id.tv_search_result_total_shops)
    TextView tvSearchResultTotalShops;
    @BindView(R.id.tv_search_result_total_goods)
    TextView tvSearchResultTotalGoods;
    private Unbinder mBinder;
    private ShopResultAdapter mShopResultAdapter;
    private GoodResultAdapter mGoodResultAdapter;
    private String search_key;

    private BCSearchInfo info;
    private View mRootView;

    @Override
    protected void InItBaseView() {
        mRootView = LayoutInflater.from(BaseContext).inflate(R.layout.activity_search_result, null);
        setContentView(mRootView);
        mBinder = ButterKnife.bind(this);
        IBundle();
        IView();
        IData();
        IListView();
    }

    private void IView() {
        search_result_nodata_lay = ViewHolder.get(mRootView, R.id.search_result_nodata_lay);
        search_result_nodata_lay.setOnClickListener(this);
        tvSearchResultAllShops.setOnClickListener(this);
        tvSearchResultAllGoods.setOnClickListener(this);
    }

    private void IBundle() {
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey("search_key")) {
            search_key = getIntent().getStringExtra("search_key");
        }
    }

    private void IData() {
        SetTitleHttpDataLisenter(this);
        PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.xlistview_header_hint_loading));
        HashMap<String, String> map = new HashMap<>();
        map.put("api_version", "3.0.1");
        map.put("keyword", search_key);
        FBGetHttpData(map, Constants.Search_Shop_Good, Request.Method.GET, 0, LOAD_INITIALIZE);
    }

    private void IListView() {
        mShopResultAdapter = new ShopResultAdapter();
        lvSearchResultShops.setAdapter(mShopResultAdapter);
        mGoodResultAdapter = new GoodResultAdapter();
        lvSearchResultGoods.setAdapter(mGoodResultAdapter);
        lvSearchResultShops.setOnItemClickListener(new myOnItemClickListener(1));
        lvSearchResultGoods.setOnItemClickListener(new myOnItemClickListener(2));
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


        info = JSON.parseObject(Data.getHttpResultStr(), BCSearchInfo.class);
        if (StrUtils.isEmpty(info.getSellerinfo()) && StrUtils.isEmpty(info.getGoodsinfo())) {
            search_result_data_lay.setVisibility(View.GONE);
            search_result_nodata_lay.setVisibility(View.VISIBLE);
            ShowErrorCanLoad(getResources().getString(R.string.search_result_null));
            search_result_nodata_lay.setClickable(false);
        } else {
            search_result_data_lay.setVisibility(View.VISIBLE);
            search_result_nodata_lay.setVisibility(View.GONE);
        }
        if (StrUtils.isEmpty(info.getSellerinfo())) {
            llSearchShops.setVisibility(View.GONE);
        } else {
            llSearchShops.setVisibility(View.VISIBLE);
            List<BLSearchShopAndGood> datas = new ArrayList<BLSearchShopAndGood>();
            datas = JSON.parseArray(info.getSellerinfo(), BLSearchShopAndGood.class);
            tvSearchResultTotalShops.setText("相关店铺"+info.getSeller_total()+"个");
            if (datas.size() < 4) {
                tvSearchResultAllShops.setVisibility(View.GONE);
            }else{
                tvSearchResultAllShops.setVisibility(View.VISIBLE);
            }
            mShopResultAdapter.RefreshShop(datas);
        }

        if (StrUtils.isEmpty(info.getGoodsinfo())) {
            llSearchGoods.setVisibility(View.GONE);
        } else {
            llSearchGoods.setVisibility(View.VISIBLE);
            List<BLSearchShopAndGood> datas = new ArrayList<BLSearchShopAndGood>();
            datas = JSON.parseArray(info.getGoodsinfo(), BLSearchShopAndGood.class);
            tvSearchResultTotalGoods.setText("相关商品"+info.getGoods_total()+"个");
            if (datas.size() < 6) {
                tvSearchResultAllGoods.setVisibility(View.GONE);
            }else{
                tvSearchResultAllGoods.setVisibility(View.VISIBLE);
            }
            mGoodResultAdapter.RefreshGood(datas);
        }

    }

    @Override
    protected void DataError(String error, int LoadType) {
        search_result_data_lay.setVisibility(View.GONE);
        search_result_nodata_lay.setVisibility(View.VISIBLE);
        ShowErrorCanLoad(getResources().getString(R.string.error_null_noda));
        search_result_nodata_lay.setClickable(true);
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
        switch (V.getId()) {
            case R.id.search_result_nodata_lay:
                if (CheckNet(BaseContext)) return;
                IData();
                break;

            case R.id.tv_search_result_all_shops:
                Bundle bundle = new Bundle();
                bundle.putInt("show_type", 1);
                bundle.putString("title", search_key);
                bundle.putString("content", info.getSellerinfo());
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext, ASearchResultList.class).putExtra("resultinfo", bundle));
                break;

            case R.id.tv_search_result_all_goods:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("show_type", 2);
                bundle1.putString("title", search_key);
                bundle1.putString("content", info.getGoodsinfo());
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext, ASearchResultList.class).putExtra("resultinfo", bundle1));
                break;
        }
    }

    @Override
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }


    class myOnItemClickListener implements AdapterView.OnItemClickListener {
        private int type;

        public myOnItemClickListener(int type) {
            super();
            this.type = type;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (type) {
                case 1:
                    BLSearchShopAndGood item = (BLSearchShopAndGood) mShopResultAdapter.getItem(position);
                    BComment bComment = new BComment(item.getId(), item.getSeller_name());
                    PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext, AShopDetail.class).putExtra(BaseKey_Bean, bComment));
                    break;

                case 2:
                    BLSearchShopAndGood data = (BLSearchShopAndGood) mGoodResultAdapter.getItem(position);
                    PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext, AGoodDetail.class).putExtra("goodid", data.getId()));
                    break;
            }
        }
    }


    class ShopResultAdapter extends BaseAdapter {

        private List<BLSearchShopAndGood> datas = new ArrayList<BLSearchShopAndGood>();

        public ShopResultAdapter() {
            super();
        }

        @Override
        public int getCount() {
            if (datas.size() < 3) {
                return datas.size();
            } else {
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
            ImageLoaderUtil.Load2(blSearchShopAndGood.getAvatar(), holder.ivSearchResultShopIcon, R.drawable.error_iv2);
            StrUtils.SetTxt(holder.tvSearchResultShopName, blSearchShopAndGood.getSeller_name());
            StrUtils.SetTxt(holder.tvSearchResultShopDesc, blSearchShopAndGood.getIntro());

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
            return datas.get(position);
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
                holder.ivSearchResultGoodLevel.setVisibility(View.VISIBLE);
            } else {
                holder.ivSearchResultGoodLevel.setVisibility(View.GONE);
            }
            StrUtils.SetTxt(holder.tvSearchResultGoodName, blSearchShopAndGood.getTitle());
            StrUtils.SetMoneyFormat(BaseContext, holder.tvSearchResultGoodPrice, blSearchShopAndGood.getSell_price(), 17);
            if ("0".equals(blSearchShopAndGood.getOrig_price()) || StrUtils.isEmpty(blSearchShopAndGood.getOrig_price())) {
                holder.tvSearchResultGoodOrigprice.setVisibility(View.GONE);
            } else {
                holder.tvSearchResultGoodOrigprice.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(holder.tvSearchResultGoodOrigprice, StrUtils.SetTextForMony(blSearchShopAndGood.getOrig_price()));
                holder.tvSearchResultGoodOrigprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            if (blSearchShopAndGood.getSales() > 0) {
                holder.tvSearchResultGoodSales.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(holder.tvSearchResultGoodSales, "销量：" + blSearchShopAndGood.getSales() + "件");
            } else {
                holder.tvSearchResultGoodSales.setVisibility(View.GONE);
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
        @BindView(R.id.tv_search_result_good_sales)
        TextView tvSearchResultGoodSales;

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
