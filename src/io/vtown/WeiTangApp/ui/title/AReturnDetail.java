package io.vtown.WeiTangApp.ui.title;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.wallet.BLAPropertyDetail;
import io.vtown.WeiTangApp.bean.bcomment.easy.wallet.BLAPropertyList;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by Yihuihua on 2016/10/12.
 * 返佣明细页面
 */

public class AReturnDetail extends ATitleBase implements LListView.IXListViewListener {
    @BindView(R.id.retrun_detail_list)
    LListView retrunDetailList;
    private Unbinder mBind;
    private View mRootView;
    private View retrun_detail_nodata_lay;
    private String lastid = "";
    private BUser mUser;
    private List<BLAPropertyList> dattaa;
    private ReturnOutsideAdapter mAdapter;

    @Override
    protected void InItBaseView() {

        mRootView = LayoutInflater.from(BaseContext).inflate(R.layout.activity_return_detail, null);
        setContentView(mRootView);
        mBind = ButterKnife.bind(this, mRootView);
        SetTitleHttpDataLisenter(this);
        mUser = Spuit.User_Get(BaseContext);
        IView();
        ICache();
        IData(LOAD_INITIALIZE);
    }


    private void IView() {
        retrun_detail_nodata_lay = ViewHolder.get(mRootView, R.id.retrun_detail_nodata_lay);
        retrun_detail_nodata_lay.setOnClickListener(this);
        retrunDetailList.setXListViewListener(this);
        retrunDetailList.setPullLoadEnable(true);
        retrunDetailList.setPullRefreshEnable(true);
        retrunDetailList.hidefoot();
        mAdapter = new ReturnOutsideAdapter();
        retrunDetailList.setAdapter(mAdapter);
    }

    private void ICache() {
        String return_detail = CacheUtil.Home_Return_Detail_Get(BaseContext);
        if (StrUtils.isEmpty(return_detail)) {
            PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.xlistview_header_hint_loading));
        } else {
            try {
                dattaa = JSON.parseArray(return_detail, BLAPropertyList.class);
            } catch (Exception e) {
                return;
            }
            mAdapter.FrashData(dattaa);
        }
    }

    private void IData(int LoadType) {

        HashMap<String, String> map = new HashMap<String, String>();
        // map.put("page_num",Constants.PageSize+"");
        map.put("member_id", "10014952");//mUser.getId()
        map.put("last_id", lastid);
        map.put("type", "6");
        FBGetHttpData(map, Constants.ZiJinJiLu, Request.Method.GET, 0, LoadType);

    }


    @Override
    protected void InitTile() {
        SetTitleTxt(getString(R.string.return_detail));

    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

        switch (Data.getHttpLoadType()) {


            case LOAD_INITIALIZE:
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    retrunDetailList.setVisibility(View.GONE);
                    retrun_detail_nodata_lay.setVisibility(View.VISIBLE);
                    retrun_detail_nodata_lay.setClickable(false);
                    ShowErrorIv(R.drawable.pic_fanyongjine);
                    String error_tip = getResources().getString(R.string.null_return_detail);
                    ShowErrorCanLoad(error_tip);
                    return;
                }
                try {

                    dattaa = JSON.parseArray(Data.getHttpResultStr(), BLAPropertyList.class);
                } catch (Exception e) {
                    DataError("解析错误", 1);
                }
                CacheUtil.Home_Return_Detail_Save(BaseContext, Data.getHttpResultStr());
                retrunDetailList.setVisibility(View.VISIBLE);
                retrun_detail_nodata_lay.setVisibility(View.GONE);

                List<BLAPropertyDetail> list = getAllPropertyDetailList(dattaa);
                lastid = list.get(list.size() - 1).getId();
                mAdapter.FrashData(dattaa);

                //CacheUtil.Center_Wallet_Property_Save(getApplicationContext(), Data.getHttpResultStr());


                if (list.size() == Constants.PageSize2) {
                    retrunDetailList.ShowFoot();

                }

                if (list.size() < Constants.PageSize2) {
                    retrunDetailList.hidefoot();

                }


                break;
            case LOAD_REFRESHING:

                retrunDetailList.stopRefresh();
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    return;
                }
                try {
                    dattaa = JSON.parseArray(Data.getHttpResultStr(), BLAPropertyList.class);
                } catch (Exception e) {

                }
                mAdapter.FrashData(dattaa);
                List<BLAPropertyDetail> list1 = getAllPropertyDetailList(dattaa);
                lastid = list1.get(list1.size() - 1).getId();
                if (list1.size() == Constants.PageSize2) {
                    retrunDetailList.ShowFoot();
                }
                if (list1.size() < Constants.PageSize2) {
                    retrunDetailList.hidefoot();

                }

                break;
            case LOAD_LOADMOREING:

                retrunDetailList.stopLoadMore();
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    return;
                }
                try {
                    dattaa = JSON.parseArray(Data.getHttpResultStr(), BLAPropertyList.class);
                } catch (Exception e) {

                }

                if (dattaa.get(0).getMonth().equals(mAdapter.GetApData().get(mAdapter.getCount() - 1).getMonth())) {
                    mAdapter.MergeFrashData(dattaa);
                } else {
                    mAdapter.AddFrashData(dattaa);
                }


                List<BLAPropertyDetail> list2 = getAllPropertyDetailList(dattaa);
                lastid = list2.get(list2.size() - 1).getId();

                if (list2.size() == Constants.PageSize2) {
                    retrunDetailList.ShowFoot();
                }

                if (list2.size() < Constants.PageSize2) {
                    retrunDetailList.hidefoot();
                }


                break;
        }

    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.ShowCustomToast(BaseContext, error);

        switch (LoadType) {
            case LOAD_INITIALIZE:
                retrunDetailList.setVisibility(View.GONE);
                retrun_detail_nodata_lay.setVisibility(View.VISIBLE);
                retrun_detail_nodata_lay.setClickable(true);
                ShowErrorIv(R.drawable.pic_fanyongjine);
                ShowErrorCanLoad(getResources().getString(R.string.error_null_noda));
                //IDataView(lv_property_detail_list, center_my_property_detail_nodata_lay, NOVIEW_ERROR);
                // property_detail_list_refrash.setCanLoadMore(false);
                break;
            case LOAD_REFRESHING:// 刷新数据
                retrunDetailList.stopRefresh();
                //property_detail_list_refrash.setRefreshing(false);

                break;
            case LOAD_LOADMOREING:// 加载更多
                retrunDetailList.stopLoadMore();
                // property_detail_list_refrash.setLoading(false);


                break;
        }

    }

    private List<BLAPropertyDetail> getAllPropertyDetailList(List<BLAPropertyList> datas) {
        List<BLAPropertyDetail> details = new ArrayList<BLAPropertyDetail>();
        for (int i = 0; i < datas.size(); i++) {
            details.addAll(datas.get(i).getList());
        }
        return details;
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
            case R.id.retrun_detail_nodata_lay:
                lastid = "";
                IData(LOAD_INITIALIZE);
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
        lastid = "";
        IData(LOAD_REFRESHING);
    }

    @Override
    public void onLoadMore() {
        IData(LOAD_LOADMOREING);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }

    class ReturnOutsideAdapter extends BaseAdapter {

        private List<BLAPropertyList> datas = new ArrayList<BLAPropertyList>();

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

        /**
         * 刷新数据
         *
         * @param dattaa
         */
        public void FrashData(List<BLAPropertyList> dattaa) {
            datas.clear();
            this.datas = dattaa;
            this.notifyDataSetChanged();
        }

        /**
         * 加载更多
         */
        public void AddFrashData(List<BLAPropertyList> dattaa) {
            this.datas.addAll(dattaa);
            this.notifyDataSetChanged();
        }

        public void MergeFrashData(List<BLAPropertyList> dattaa) {

            this.datas.get(getCount() - 1).getList().addAll(dattaa.get(0).getList());
            for (int i = 1; i < dattaa.size(); i++) {
                this.datas.add(dattaa.get(i));
            }
            this.notifyDataSetChanged();
        }

        public List<BLAPropertyList> GetApData() {
            return this.datas;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ReturnOutsideViewHolder holder = null;
            if (null == convertView) {
                convertView = LayoutInflater.from(BaseContext).inflate(R.layout.item_return_detail_outside, null);
                holder = new ReturnOutsideViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ReturnOutsideViewHolder) convertView.getTag();
            }
            BLAPropertyList data = datas.get(position);
            StrUtils.SetTxt(holder.returnMonth, data.getMonth());
            holder.lvReturnListOutside.setAdapter(new ReturnInsideAdapter(data.getList()));
            return convertView;
        }


    }

    class ReturnInsideAdapter extends BaseAdapter {

        private List<BLAPropertyDetail> return_datas;

        public ReturnInsideAdapter(List<BLAPropertyDetail> return_datas) {
            super();
            this.return_datas = return_datas;
        }

        @Override
        public int getCount() {
            return return_datas.size();
        }

        @Override
        public Object getItem(int position) {
            return return_datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ReturnInsideViewHolder holder = null;
            if (null == convertView) {
                convertView = LayoutInflater.from(BaseContext).inflate(R.layout.item_return_detail_inside, null);
                holder = new ReturnInsideViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ReturnInsideViewHolder) convertView.getTag();
            }
            BLAPropertyDetail data = return_datas.get(position);
            StrUtils.SetTxt(holder.tvReturnDay, data.getDateStr());
            StrUtils.SetTxt(holder.tvReturnTime, data.getDate());
            StrUtils.SetTxt(holder.tvReturnContent, data.getTitle());
            StrUtils.SetTxt(holder.tvReturnPoint, String.format("+ %1$s元", StrUtils.SetTextForMony(StrUtils.SetTextForMony(data.getPrice()))));
            return convertView;
        }

    }


    class ReturnOutsideViewHolder {
        @BindView(R.id.return_month)
        TextView returnMonth;
        @BindView(R.id.lv_return_list_outside)
        CompleteListView lvReturnListOutside;

        ReturnOutsideViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class ReturnInsideViewHolder {
        @BindView(R.id.tv_return_day)
        TextView tvReturnDay;
        @BindView(R.id.tv_return_time)
        TextView tvReturnTime;
        @BindView(R.id.tv_return_content)
        TextView tvReturnContent;
        @BindView(R.id.tv_return_point)
        TextView tvReturnPoint;

        ReturnInsideViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
