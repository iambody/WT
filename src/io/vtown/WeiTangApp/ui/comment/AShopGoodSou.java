package io.vtown.WeiTangApp.ui.comment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.vtown.WeiTangApp.BaseApplication;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.shop.BShopGoods;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;

/**
 * Created by datutu on 2016/9/10.
 * 本搜索是可以分页加载的注意！！！！！！！！！！
 */
public class AShopGoodSou extends ATitleBase implements LListView.IXListViewListener, AdapterView.OnItemClickListener, TextWatcher {

    /**
     * 记录当前的列表页数
     */
    private int CurrentPage = 1;
    /**
     * 记录当前搜索哦的搜索词语
     */
    private String CurrentTitle = "";


    private String SellId;
    private String Title;
    private EditText shop_good_sou_title;
    private ImageView shop_good_sou_iv;
    private LListView shop_good_sou_result_list;
    private SearchResultAdapter searchAP;
    private View shop_good_sou_nodata_lay;

    //获取推荐上牌
    private List<BShopGoods> Recommend = new ArrayList<BShopGoods>();
    private LinearLayout layout_shop_good_sou_hot_goods;
    private CompleteGridView shop_good_sou_hot_goods;
    private ImageView shop_good_sou_title_delete;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_shopgoodsou);
        SetTitleHttpDataLisenter(this);
        SellId = getIntent().getStringExtra("Sellid");
        Title = getIntent().getStringExtra("Sellname");

        Recommend = BaseApplication.GetInstance().getShopSouRecommend();
        BaseApplication.GetInstance().setShopSouRecommend(null);
        IView();
    }

    private void IView() {
        shop_good_sou_title = (EditText) findViewById(R.id.shop_good_sou_title);
        shop_good_sou_iv = (ImageView) findViewById(R.id.shop_good_sou_iv);
        shop_good_sou_result_list = (LListView) findViewById(R.id.shop_good_sou_result_list);
        shop_good_sou_nodata_lay = findViewById(R.id.shop_good_sou_nodata_lay);
        IDataView(shop_good_sou_result_list, shop_good_sou_nodata_lay, NOVIEW_INITIALIZE);
        layout_shop_good_sou_hot_goods = (LinearLayout) findViewById(R.id.layout_shop_good_sou_hot_goods);
        shop_good_sou_hot_goods = (CompleteGridView) findViewById(R.id.shop_good_sou_hot_goods);
        shop_good_sou_title_delete = (ImageView) findViewById(R.id.shop_good_sou_title_delete);
        shop_good_sou_iv.setOnClickListener(this);
        shop_good_sou_title.addTextChangedListener(this);
        shop_good_sou_nodata_lay.setOnClickListener(this);
        shop_good_sou_title_delete.setOnClickListener(this);
        IGrid();
        IList();
    }


    private void IGrid() {
        if (Recommend.size() == 0 || Recommend == null) {
            layout_shop_good_sou_hot_goods.setVisibility(View.GONE);
            return;
        }

        layout_shop_good_sou_hot_goods.setVisibility(View.VISIBLE);
        HotGoodAdapter hotAP = new HotGoodAdapter(R.layout.item_shop_good_sou_hot_goods, Recommend);
        shop_good_sou_hot_goods.setAdapter(hotAP);
        shop_good_sou_hot_goods.setOnItemClickListener(this);
    }

    private void IList() {
        searchAP = new SearchResultAdapter(R.layout.item_shop_good_sou_result);
        shop_good_sou_result_list.setAdapter(searchAP);
        shop_good_sou_result_list.setPullRefreshEnable(false);
        shop_good_sou_result_list.setPullLoadEnable(true);
        shop_good_sou_result_list.setXListViewListener(this);
        shop_good_sou_result_list.hidefoot();
        shop_good_sou_result_list.setOnItemClickListener(this);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt(StrUtils.NullToStr(Title));
    }

    /**
     * 搜索
     */
    private void ISouConnect(int LoadType) {
        if (LOAD_INITIALIZE == LoadType) {
            PromptManager.showtextLoading(BaseContext, getResources()
                    .getString(R.string.searching));
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("title", CurrentTitle);
        hashMap.put("seller_id", SellId);
        hashMap.put("page", CurrentPage + "");
        hashMap.put("pagesize", Constants.PageSize + "");
        FBGetHttpData(hashMap, Constants.ShopGoodSou, Request.Method.GET, 1, LoadType);

    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        layout_shop_good_sou_hot_goods.setVisibility(View.GONE);
        if (StrUtils.isEmpty(Data.getHttpResultStr())) {
            if (LOAD_INITIALIZE == Data.getHttpLoadType()) {
                IDataView(shop_good_sou_result_list, shop_good_sou_nodata_lay, NOVIEW_ERROR);
                shop_good_sou_nodata_lay.setClickable(false);
                DataError(getResources().getString(R.string.error_null_search_result), Data.getHttpLoadType());
                ShowErrorCanLoad(getResources().getString(R.string.error_null_search_result));

            }

            return;
        }
        List<BShopGoods> list_datas = new ArrayList<BShopGoods>();
        try {
            list_datas = JSON.parseArray(Data.getHttpResultStr(), BShopGoods.class);
        } catch (Exception e) {
            return;
        }
        IDataView(shop_good_sou_result_list, shop_good_sou_nodata_lay, NOVIEW_RIGHT);
        switch (Data.getHttpLoadType()) {
            case LOAD_INITIALIZE:
                searchAP.UpdataList(list_datas);
                break;

            case LOAD_LOADMOREING:
                shop_good_sou_result_list.stopLoadMore();
                searchAP.AddMore(list_datas);
                if (list_datas.size() == Constants.PageSize)
                    shop_good_sou_result_list.ShowFoot();
                if (list_datas.size() < Constants.PageSize)
                    shop_good_sou_result_list.hidefoot();
                break;
        }

    }

    @Override
    protected void DataError(String error, int LoadType) {
        layout_shop_good_sou_hot_goods.setVisibility(View.GONE);

        PromptManager.ShowMyToast(BaseContext, error);
        switch (LoadType) {
            case LOAD_INITIALIZE:// 刷新数据
                IDataView(shop_good_sou_result_list, shop_good_sou_nodata_lay, NOVIEW_ERROR);
                ShowErrorCanLoad(getResources().getString(R.string.error_null_noda));
                shop_good_sou_nodata_lay.setClickable(true);
                break;
            case LOAD_LOADMOREING:// 加载更多
                shop_good_sou_result_list.stopLoadMore();

                break;
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
        switch (V.getId()) {
            case R.id.shop_good_sou_nodata_lay:
            case R.id.shop_good_sou_iv:
                hintKbTwo();
                if (CheckNet(BaseContext)) return;
                CurrentTitle = shop_good_sou_title.getText().toString();
                if (!StrUtils.isEmpty(CurrentTitle)) {
                    CurrentPage = 1;
                    ISouConnect(LOAD_INITIALIZE);
                } else {
                    PromptManager.ShowCustomToast(BaseContext, "请输入您要搜索的内容");
                    return;
                }

                break;

            case R.id.shop_good_sou_title_delete:
                shop_good_sou_title.setText("");
                break;
        }

    }

    @Override
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }


    @Override
    public void onRefresh() {
//        CurrentPage = 1;
//        ISouConnect(LOAD_REFRESHING);
    }

    @Override
    public void onLoadMore() {

        CurrentPage = CurrentPage + 1;
        ISouConnect(LOAD_REFRESHING);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BShopGoods daa = (BShopGoods) parent.getItemAtPosition(position);
        String goods_id = daa.getId();
        PromptManager.SkipActivity(BaseActivity, new Intent(
                BaseActivity, AGoodDetail.class).putExtra("goodid",
                goods_id));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().length() == 0) {
            layout_shop_good_sou_hot_goods.setVisibility(View.VISIBLE);
            shop_good_sou_result_list.setVisibility(View.GONE);
            shop_good_sou_title_delete.setVisibility(View.GONE);
        } else {
            //          layout_shop_good_sou_hot_goods.setVisibility(View.GONE);
//            shop_good_sou_result_list.setVisibility(View.VISIBLE);
            shop_good_sou_title_delete.setVisibility(View.VISIBLE);
//            CurrentTitle = s.toString();
//            CurrentPage = 1;
//            ISouConnect(LOAD_INITIALIZE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    class SearchResultAdapter extends BaseAdapter {

        private int ResoultId;
        private LayoutInflater inflater;
        private List<BShopGoods> datas = new ArrayList<BShopGoods>();

        public SearchResultAdapter(int ResoultId) {
            super();
            this.ResoultId = ResoultId;
            this.inflater = LayoutInflater.from(BaseContext);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        public void UpdataList(List<BShopGoods> datas) {
            this.datas = datas;
            this.notifyDataSetChanged();
        }

        public void AddMore(List<BShopGoods> data) {
            this.datas.addAll(data);
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
            ResultItem holder = null;

            if (convertView == null) {
                holder = new ResultItem();
                convertView = inflater.inflate(ResoultId, null);
                holder.shop_good_sou_good_icon = (ImageView) convertView.findViewById(R.id.shop_good_sou_good_icon);
                holder.shop_good_sou_good_title = (TextView) convertView.findViewById(R.id.shop_good_sou_good_title);
                holder.shop_good_sou_price = (TextView) convertView.findViewById(R.id.shop_good_sou_price);
                holder.item_shop_good_sou_result_sales = (TextView) convertView.findViewById(R.id.item_shop_good_sou_result_sales);
                holder.shop_good_sou_orig_price = (TextView) convertView.findViewById(R.id.shop_good_sou_orig_price);
                holder.item_shop_good_sou_result_score = (TextView) convertView.findViewById(R.id.item_shop_good_sou_result_score);
                convertView.setTag(holder);
                ImageLoaderUtil.Load2(datas.get(position).getCover(), holder.shop_good_sou_good_icon, R.drawable.error_iv2);
            } else {
                holder = (ResultItem) convertView.getTag();
            }


            StrUtils.SetTxt(holder.shop_good_sou_good_title, datas.get(position).getTitle());
            //StrUtils.SetTxt(holder.shop_good_sou_price, StrUtils.SetTextForMony(datas.get(position).getSell_price()) + "元");
            StrUtils.SetMoneyFormat(BaseContext, holder.shop_good_sou_price, datas.get(position).getSell_price(), 14);
            StrUtils.SetTxt(holder.shop_good_sou_orig_price, StrUtils.SetTextForMony(datas.get(position).getOrig_price()));
            holder.shop_good_sou_orig_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            if ("0".equals(datas.get(position).getScore())) {
                holder.item_shop_good_sou_result_score.setVisibility(View.GONE);
            } else {
                holder.item_shop_good_sou_result_score.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(holder.item_shop_good_sou_result_score, "积分：" + datas.get(position).getScore());
            }

            if ("0".equals(datas.get(position).getSales())) {
                holder.item_shop_good_sou_result_sales.setVisibility(View.GONE);
            } else {
                holder.item_shop_good_sou_result_sales.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(holder.item_shop_good_sou_result_sales, "销量：" + datas.get(position).getSales());
            }
            return convertView;
        }
    }


    class HotGoodAdapter extends BaseAdapter {

        private int ResourseId;
        private List<BShopGoods> hot_goods;
        private LayoutInflater inflater;

        public HotGoodAdapter(int ResourseId, List<BShopGoods> hot_goods) {
            super();
            this.ResourseId = ResourseId;
            this.hot_goods = hot_goods;
            this.inflater = LayoutInflater.from(BaseContext);
        }

        @Override
        public int getCount() {
            return hot_goods.size();
        }

        @Override
        public Object getItem(int position) {
            return hot_goods.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            HotGoodsItem item = null;
            if (null == convertView) {
                item = new HotGoodsItem();
                convertView = inflater.inflate(ResourseId, null);
                item.shop_good_sou_hot_goods_icon = (ImageView) convertView.findViewById(R.id.shop_good_sou_hot_goods_icon);
                item.shop_good_sou_hot_goods_title = (TextView) convertView.findViewById(R.id.shop_good_sou_hot_goods_title);
                item.shop_good_sou_hot_goods_price = (TextView) convertView.findViewById(R.id.shop_good_sou_hot_goods_price);
                convertView.setTag(item);
                ImageLoaderUtil.Load2(hot_goods.get(position).getCover(), item.shop_good_sou_hot_goods_icon, R.drawable.error_iv2);
            } else {
                item = (HotGoodsItem) convertView.getTag();
            }
            StrUtils.SetTxt(item.shop_good_sou_hot_goods_title, hot_goods.get(position).getTitle());
            StrUtils.SetTxt(item.shop_good_sou_hot_goods_price, StrUtils.SetTextForMony(hot_goods.get(position).getSell_price()) + "元");


            return convertView;
        }
    }


    class HotGoodsItem {
        ImageView shop_good_sou_hot_goods_icon;
        TextView shop_good_sou_hot_goods_title, shop_good_sou_hot_goods_price;

    }

    class ResultItem {
        ImageView shop_good_sou_good_icon;
        TextView shop_good_sou_good_title, shop_good_sou_price, shop_good_sou_orig_price, item_shop_good_sou_result_sales, item_shop_good_sou_result_score;

    }
}
