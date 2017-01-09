package io.vtown.WeiTangApp.ui.title.center.mycoupons;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import io.vtown.WeiTangApp.bean.bcomment.easy.BCCouponsShop;
import io.vtown.WeiTangApp.bean.bcomment.easy.BLCouponsShops;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by Yihuihua on 2017/1/9.
 */

public class ACouponsShop extends ATitleBase {
    @BindView(R.id.lv_my_conpons_shop_list)
    ListView lvMyConponsShopList;
    private Unbinder mBinder;
    public static final String KEY_COUPONS_ID = "key_coupons_id";
    public static final String KEY_COUPONS_NAME = "key_coupons_name";
    private String coupons_id = "";
    private String coupons_name = "";
    private CouponsShopAdapter mCouponsShopAdapter;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_coupons_shop);
        mBinder = ButterKnife.bind(this);
        IBundle();
        IView();
        IData();
    }

    private void IView() {
        mCouponsShopAdapter = new CouponsShopAdapter();
        lvMyConponsShopList.setAdapter(mCouponsShopAdapter);
    }

    private void IData() {
        SetTitleHttpDataLisenter(this);
        HashMap<String, String> map = new HashMap<>();
        map.put("coupons_id", coupons_id);
        FBGetHttpData(map, Constants.CENTER_MY_COUPONS_SELLER, Request.Method.GET, 0, LOAD_INITIALIZE);
    }

    private void IBundle() {
        coupons_id = getIntent().getStringExtra(KEY_COUPONS_ID);
        coupons_name = getIntent().getStringExtra(KEY_COUPONS_NAME);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt(coupons_name);
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        if (StrUtils.isEmpty(Data.getHttpResultStr())) {
            return;
        }
        BCCouponsShop shop_data = JSON.parseObject(Data.getHttpResultStr(), BCCouponsShop.class);
        mCouponsShopAdapter.setData(shop_data.getSellerList());

    }

    @Override
    protected void DataError(String error, int LoadType) {

        PromptManager.ShowCustomToast(BaseContext,error);
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
    protected void onDestroy() {
        super.onDestroy();
        mBinder.unbind();
    }


    class CouponsShopAdapter extends BaseAdapter {
        private List<BLCouponsShops> sellerList = new ArrayList<BLCouponsShops>();

        @Override
        public int getCount() {
            return sellerList.size();
        }

        public void setData(List<BLCouponsShops> sellerList) {
            this.sellerList = sellerList;
            CouponsShopAdapter.this.notifyDataSetChanged();
        }

        @Override
        public Object getItem(int position) {
            return sellerList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CouponsShopHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(BaseContext).inflate(R.layout.item_coupons_shop_lv, null);
                holder = new CouponsShopHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (CouponsShopHolder) convertView.getTag();
            }

            ImageLoaderUtil.Load2(sellerList.get(position).getCover(), holder.ivConponsShopIcon, R.drawable.error_iv2);

            StrUtils.SetTxt(holder.tvConponsShopName, sellerList.get(position).getSeller_name());

            return convertView;
        }


    }

    class CouponsShopHolder {
        @BindView(R.id.iv_conpons_shop_icon)
        CircleImageView ivConponsShopIcon;
        @BindView(R.id.tv_conpons_shop_name)
        TextView tvConponsShopName;
        @BindView(R.id.tv_user_coupons)
        TextView tvUserCoupons;

        CouponsShopHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
