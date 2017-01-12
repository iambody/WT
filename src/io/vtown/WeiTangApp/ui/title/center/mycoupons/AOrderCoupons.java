package io.vtown.WeiTangApp.ui.title.center.mycoupons;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.coupons.BLMyCoupons;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by Yihuihua on 2017/1/12.
 */

public class AOrderCoupons extends ATitleBase implements AdapterView.OnItemClickListener {

    public static final String COUPONS_STR_KEY = "coupons_key";
    public static final String CLICK_ITEM_KEY = "click_item";
    @BindView(R.id.lv_order_conpons_list)
    ListView lvOrderConponsList;
    private Unbinder mBinder;
    private List<BLMyCoupons> blMyCouponses;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_order_coupons);
        mBinder = ButterKnife.bind(this);
        IBundle();
        lvOrderConponsList.setOnItemClickListener(this);
    }

    private void IBundle() {
        String coupons_str = getIntent().getStringExtra(COUPONS_STR_KEY);
        if (!StrUtils.isEmpty(coupons_str)) {
            IData(coupons_str);
        }
    }

    private void IData(String coupons_str) {
        blMyCouponses = JSON.parseArray(coupons_str, BLMyCoupons.class);
        OrderCouponsAdapter mAdapter = new OrderCouponsAdapter(blMyCouponses);
        lvOrderConponsList.setAdapter(mAdapter);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt("可用卡券");
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

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
    protected void onDestroy() {
        super.onDestroy();
        mBinder.unbind();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BLMyCoupons blMyCoupons = blMyCouponses.get(position);
    }

    private class OrderCouponsAdapter extends BaseAdapter {

        List<BLMyCoupons> datas = new ArrayList<BLMyCoupons>();

        public OrderCouponsAdapter(List<BLMyCoupons> datas) {
            super();
            this.datas = datas;
        }

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
            OrderCouponsHolder holder = null;
            if (null == convertView) {
                convertView = LayoutInflater.from(BaseContext).inflate(R.layout.item_order_coupons, null);
                holder = new OrderCouponsHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (OrderCouponsHolder) convertView.getTag();
            }
            BLMyCoupons blMyCoupons = datas.get(position);
            StrUtils.SetTxt(holder.tvOrderCouponsPrice,StrUtils.SetTextForMony(blMyCoupons.getCoupons_money()));
            StrUtils.SetTxt(holder.tvOrderCouponsType,blMyCoupons.getCoupons_name());
            String validity = getString(R.string.my_coupons_validity);
            StrUtils.SetTxt(holder.tvOrderCouponsValidity, "有效期:" + String.format(validity,
                    blMyCoupons.getUsed_starttime(),
                    blMyCoupons.getUsed_endtime()));
            return convertView;
        }


    }

    class OrderCouponsHolder {
        @BindView(R.id.tv_order_coupons_price)
        TextView tvOrderCouponsPrice;
        @BindView(R.id.tv_order_coupons_type)
        TextView tvOrderCouponsType;
        @BindView(R.id.tv_order_coupons_validity)
        TextView tvOrderCouponsValidity;

        OrderCouponsHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
