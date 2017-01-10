package io.vtown.WeiTangApp.ui.title.center.mycoupons;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.coupons.BLMyCoupons;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-8-5 下午1:52:42
 *          过期卡券
 */
public class AMyOutDataCoupons extends ATitleBase {

    /**
     * 数据加载成功布局
     */
    private LinearLayout center_my_out_data_coupons_outlay;
    /**
     * 列表
     */
    private LListView lv_my_out_data_conpons_list;
    /**
     * 加载失败布局
     */
    private View center_my_out_data_coupons_nodata_lay;
    /**
     * AP
     */
    private OutDataConponsAdapter outDataConponsAp;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_center_my_out_data_coupons);
        IView();
        IData();
    }

    /**
     * 初始化控件
     */
    private void IView() {
        center_my_out_data_coupons_outlay = (LinearLayout) findViewById(R.id.center_my_out_data_coupons_outlay);
        lv_my_out_data_conpons_list = (LListView) findViewById(R.id.lv_my_out_data_conpons_list);
        center_my_out_data_coupons_nodata_lay = findViewById(R.id.center_my_out_data_coupons_nodata_lay);
        IDataView(center_my_out_data_coupons_outlay, center_my_out_data_coupons_nodata_lay, NOVIEW_INITIALIZE);
        lv_my_out_data_conpons_list.setPullRefreshEnable(false);
        lv_my_out_data_conpons_list.setPullLoadEnable(false);
        lv_my_out_data_conpons_list.hidefoot();
        outDataConponsAp = new OutDataConponsAdapter(R.layout.item_my_out_data_coupons_list2);
        lv_my_out_data_conpons_list.setAdapter(outDataConponsAp);
        center_my_out_data_coupons_nodata_lay.setOnClickListener(this);
    }

    private void IData() {
        PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.xlistview_header_hint_loading));
        SetTitleHttpDataLisenter(this);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", Spuit.User_Get(getApplicationContext()).getId());
        map.put("status", "0");// 2全部 1 可用 0 过期
        FBGetHttpData(map, Constants.CENTER_MY_COUPONS, Method.GET, 0,
                LOAD_INITIALIZE);
    }


    @Override
    protected void InitTile() {
        SetTitleTxt("过期卡券");
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        if (StrUtils.isEmpty(Data.getHttpResultStr())) {
            //PromptManager.ShowCustomToast(BaseContext, "还没有失效卡券");
            IDataView(center_my_out_data_coupons_outlay, center_my_out_data_coupons_nodata_lay, NOVIEW_ERROR);
            center_my_out_data_coupons_nodata_lay.setClickable(false);
            //DataError(getResources().getString(R.string.error_null_over_coupons), Data.getHttpLoadType());
            ShowErrorCanLoad(getResources().getString(R.string.error_null_over_coupons));
            return;
        }

        List<BLMyCoupons> list_data = new ArrayList<BLMyCoupons>();
        try {
            list_data = JSON.parseArray(Data.getHttpResultStr(),BLMyCoupons.class);
        } catch (Exception e) {
            DataError("解析失败", 0);
        }
        IDataView(center_my_out_data_coupons_outlay, center_my_out_data_coupons_nodata_lay, NOVIEW_RIGHT);
        outDataConponsAp.RefreshData(list_data);
    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.ShowMyToast(BaseContext, error);
        if (LOAD_INITIALIZE == LoadType) {
            IDataView(center_my_out_data_coupons_outlay, center_my_out_data_coupons_nodata_lay, NOVIEW_ERROR);
            center_my_out_data_coupons_nodata_lay.setClickable(true);
            ShowErrorCanLoad(getResources().getString(R.string.error_null_noda));
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
            case R.id.center_my_out_data_coupons_nodata_lay:
                IData();
                break;
        }
    }

    @Override
    protected void InItBundle(Bundle bundle) {
    }

    @Override
    protected void SaveBundle(Bundle bundle) {
    }

    class OutDataConponsAdapter extends BaseAdapter {

        private int ResourseID;
        private LayoutInflater inflater;
        private List<BLMyCoupons> datas = new ArrayList<BLMyCoupons>();

        public OutDataConponsAdapter(int ResourseID) {
            super();
            this.ResourseID = ResourseID;
            this.inflater = LayoutInflater.from(BaseContext);
        }

        public void RefreshData(List<BLMyCoupons> datas) {
            this.datas = datas;
            this.notifyDataSetChanged();
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
            OutDataItem item = null;
            if (convertView == null) {
                item = new OutDataItem();
                convertView = inflater.inflate(ResourseID, null);
                item.tv_my_out_data_coupons_price = (TextView) convertView.findViewById(R.id.tv_my_out_data_coupons_price);
                item.tv_my_out_data_coupons_type = (TextView) convertView.findViewById(R.id.tv_my_out_data_coupons_type);
                item.tv_my_out_data_coupons_validity = (TextView) convertView.findViewById(R.id.tv_my_out_data_coupons_validity);
                convertView.setTag(item);
            } else {
                item = (OutDataItem) convertView.getTag();
            }

            StrUtils.SetTxt(item.tv_my_out_data_coupons_price, String.format("%1$s", StrUtils.SetTextForMony(datas.get(position).getCoupons_money())));
            if(1 == datas.get(position).getDonation()){
                StrUtils.SetTxt(item.tv_my_out_data_coupons_type, datas.get(position).getCoupons_name()+"  已转赠");
            }else{
                StrUtils.SetTxt(item.tv_my_out_data_coupons_type, datas.get(position).getCoupons_name()+"  已过期");
            }

            String validity = getString(R.string.my_coupons_validity);

            StrUtils.SetTxt(item.tv_my_out_data_coupons_validity, "有效期:" + String.format(
                    validity, datas.get(position).getUsed_starttime(),
                    datas.get(position).getUsed_endtime()));
            return convertView;
        }

    }

    class OutDataItem {
        TextView tv_my_out_data_coupons_price;
        TextView tv_my_out_data_coupons_type;
        TextView tv_my_out_data_coupons_validity;
    }

}
