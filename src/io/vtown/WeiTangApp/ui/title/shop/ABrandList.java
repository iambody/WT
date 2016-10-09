package io.vtown.WeiTangApp.ui.title.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.custom.RefreshLayout;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.comment.view.listview.LListView.IXListViewListener;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.AApplyProxy;
import io.vtown.WeiTangApp.ui.title.ABrandDetail;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-8-22 上午11:49:54
 */
public class ABrandList extends ATitleBase implements AdapterView.OnItemClickListener, RefreshLayout.OnLoadListener {

    /**
     * 品牌列表
     */
    private ListView shop_brands_list;
    /**
     * AP
     */
    private BrandListAdapter brandListAdapter;
    /**
     * 加载数据失败时显示的布局
     */
    private View shop_brand_nodata_lay;

    private int CurrentPage = 1;
    private RefreshLayout shop_brands_refrash;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_shop_brand_list);
        EventBus.getDefault().register(this, "getEventMsg", BMessage.class);
        SetTitleHttpDataLisenter(this);
        IView();
        IData(LOAD_INITIALIZE, CurrentPage);
    }

    private void IData(int LoadType, int Page) {
        if (LOAD_INITIALIZE == LoadType) {
            PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.xlistview_header_hint_loading));
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("page", Page + "");
        map.put("seller_id", Spuit.User_Get(BaseContext).getSeller_id());
        map.put("pagesize", Constants.SouGoodinf_size);
        FBGetHttpData(map, Constants.Brand_List, Method.GET,
                0, LoadType);
    }

    private void IView() {
        shop_brand_nodata_lay = findViewById(R.id.shop_brand_nodata_lay);
        shop_brands_refrash = (RefreshLayout) findViewById(R.id.shop_brands_refrash);
        shop_brands_refrash.setOnLoadListener(this);
        shop_brands_refrash.setColorSchemeResources(R.color.app_fen, R.color.app_fen1, R.color.app_fen2, R.color.app_fen3);
        shop_brands_refrash.setCanLoadMore(false);
        shop_brands_list = (ListView) findViewById(R.id.shop_brands_list);
        IDataView(shop_brands_list, shop_brand_nodata_lay, NOVIEW_INITIALIZE);

        brandListAdapter = new BrandListAdapter(R.layout.item_shop_brands_list);
        shop_brands_list.setAdapter(brandListAdapter);
        shop_brands_list.setOnItemClickListener(this);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt("品牌");
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                //shop_brands_list.stopRefresh();
                shop_brands_refrash.setRefreshing(false);
            }
            if (msg.what == 2) {
                //shop_brands_list.stopLoadMore();
                shop_brands_refrash.setLoading(false);
            }
        }
    };

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        if (StrUtils.isEmpty(Data.getHttpResultStr()) && Data.getHttpLoadType() != LOAD_LOADMOREING) {
            DataError(Constants.SucessToError, Data.getHttpLoadType());
            return;
        }

        List<BLComment> datas = new ArrayList<BLComment>();
        try {
            datas = JSON.parseArray(Data.getHttpResultStr(), BLComment.class);
        } catch (Exception e) {
            return;
        }
        switch (Data.getHttpLoadType()) {
            case LOAD_INITIALIZE:
            case LOAD_HindINIT:
                shop_brands_refrash.setRefreshing(false);
                brandListAdapter.FreshData(datas);
                IDataView(shop_brands_list, shop_brand_nodata_lay, NOVIEW_RIGHT);
                break;

            case LOAD_REFRESHING:

                Message m = new Message();
                m.what = 1;
                mHandler.sendMessage(m);
                brandListAdapter.FreshData(datas);
                break;

            case LOAD_LOADMOREING:

                Message mm = new Message();
                mm.what = 2;
                mHandler.sendMessage(mm);
                brandListAdapter.FreshMoreData(datas);
                break;

        }
        if (datas.size() < Constants.PageSize)
            //shop_brands_list.hidefoot();
            shop_brands_refrash.setCanLoadMore(false);
        else
            //shop_brands_list.ShowFoot();
            shop_brands_refrash.setCanLoadMore(true);
    }

    @Override
    protected void DataError(String error, int LoadType) {
        switch (LoadType) {
            case LOAD_INITIALIZE:
                shop_brands_refrash.setRefreshing(false);
                PromptManager.ShowCustomToast(BaseContext, error);
                IDataView(shop_brands_list, shop_brand_nodata_lay, NOVIEW_ERROR);
                break;

            case LOAD_REFRESHING:
                Message m = new Message();
                m.what = 1;
                mHandler.sendMessage(m);
                PromptManager.ShowCustomToast(BaseContext, error);
                break;

            case LOAD_LOADMOREING:
                Message mm = new Message();
                mm.what = 2;
                mHandler.sendMessage(mm);
                PromptManager.ShowCustomToast(BaseContext, error);
                break;

            default:
                break;
        }
    }

    @Override
    protected void NetConnect() {
        NetError.setVisibility(View.GONE);
        CurrentPage = 1;
        IData(LOAD_INITIALIZE, CurrentPage);
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
        BLComment daa = (BLComment) parent.getItemAtPosition(position);
        BComment d = new BComment(daa.getId(), daa.getSeller_name());
        PromptManager.SkipActivity(BaseActivity, new Intent(
                BaseActivity, ABrandDetail.class).putExtra(
                BaseKey_Bean, d));
    }

    @Override
    public void OnLoadMore() {
        CurrentPage = CurrentPage + 1;
        IData(LOAD_LOADMOREING, CurrentPage);
    }

    @Override
    public void OnFrash() {
        CurrentPage = 1;
        IData(LOAD_REFRESHING, CurrentPage);
    }



    class BrandListAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        private int ResourseId;

        private List<BLComment> datas = new ArrayList<BLComment>();

        public BrandListAdapter(int ResourseId) {
            super();
            this.ResourseId = ResourseId;
            this.inflater = LayoutInflater.from(BaseContext);
        }

        @Override
        public int getCount() {

            return datas.size();
        }

        public void FreshData(List<BLComment> datas) {
            this.datas = datas;
            this.notifyDataSetChanged();
        }

        public void FreshMoreData(List<BLComment> datas) {
            this.datas.addAll(datas);
            this.notifyDataSetChanged();
        }

        public void NotifiItem(int Postion, int Type) {
            switch (Type) {
                case 1://申请成功需要修改文字和颜色

                    int visiblePosition = shop_brands_list.getFirstVisiblePosition();
                    if (Postion - visiblePosition >= 0) {
                        View view = shop_brands_list.getChildAt(Postion - visiblePosition + 1);
                        ShopBrandItem holder1 = (ShopBrandItem) view.getTag();
                        holder1.tv_shop_brand_apply.setVisibility(View.GONE);
                        holder1.tv_shop_brand_is_apply.setVisibility(View.VISIBLE);
                        holder1.tv_shop_brand_apply_success.setVisibility(View.GONE);
                    }
                    break;
            }
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
            ShopBrandItem item = null;
            if (convertView == null) {
                item = new ShopBrandItem();
                convertView = inflater.inflate(ResourseId, null);
                item.shop_brand_iv = (CircleImageView) convertView.findViewById(R.id.shop_brand_iv);
                item.tv_shop_brand_name = (TextView) convertView.findViewById(R.id.tv_shop_brand_name);
                item.tv_shop_brand_desc = (TextView) convertView.findViewById(R.id.tv_shop_brand_desc);
                item.tv_shop_brand_apply = (TextView) convertView.findViewById(R.id.tv_shop_brand_apply);
                item.tv_shop_brand_is_apply = (TextView) convertView.findViewById(R.id.tv_shop_brand_is_apply);
                item.tv_shop_brand_apply_success = (TextView) convertView.findViewById(R.id.tv_shop_brand_apply_success);

                convertView.setTag(item);

            } else {
                item = (ShopBrandItem) convertView.getTag();
            }
            final int MyPostion = position;
            ImageLoaderUtil.Load2(datas.get(MyPostion).getAvatar(), item.shop_brand_iv, R.drawable.error_iv2);
            StrUtils.SetTxt(item.tv_shop_brand_name, datas.get(MyPostion).getSeller_name());
            int status = Integer.parseInt(datas.get(MyPostion).getStatus());
            switch (status) {
                case 1:
                    item.tv_shop_brand_apply.setVisibility(View.VISIBLE);
                    item.tv_shop_brand_is_apply.setVisibility(View.GONE);
                    item.tv_shop_brand_apply_success.setVisibility(View.GONE);
                    break;

                case 2:
                    item.tv_shop_brand_apply.setVisibility(View.GONE);
                    item.tv_shop_brand_is_apply.setVisibility(View.VISIBLE);
                    item.tv_shop_brand_apply_success.setVisibility(View.GONE);
                    break;

                case 3:
                    item.tv_shop_brand_apply.setVisibility(View.GONE);
                    item.tv_shop_brand_is_apply.setVisibility(View.GONE);
                    item.tv_shop_brand_apply_success.setVisibility(View.VISIBLE);
                    break;

            }
            item.tv_shop_brand_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                            AApplyProxy.class).putExtra("brandid", datas.get(MyPostion).getId()).putExtra("postion", MyPostion));
                }
            });
            return convertView;
        }
    }

    class ShopBrandItem {
        public TextView tv_shop_brand_name, tv_shop_brand_desc, tv_shop_brand_apply, tv_shop_brand_is_apply, tv_shop_brand_apply_success;
        public CircleImageView shop_brand_iv;
    }

    private void getEventMsg(BMessage event) {
        int msg = event.getMessageType();
        if (msg == BMessage.Tage_Updata_Brand_list) {
//            CurrentPage = 1;
//            IData(LOAD_HindINIT, CurrentPage);
            int Postion = event.getBrandApplyPostion();
            brandListAdapter.NotifiItem(Postion,1);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            return;
        }
    }
}
