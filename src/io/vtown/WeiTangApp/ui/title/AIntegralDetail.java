package io.vtown.WeiTangApp.ui.title;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.new_three.integral_detail.BCIntegralDetail;
import io.vtown.WeiTangApp.bean.bcomment.new_three.integral_detail.BLIntegralDetails;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by Yihuihua on 2016/10/12.
 * 积分明细页面
 */

public class AIntegralDetail extends ATitleBase implements LListView.IXListViewListener {

    private View integral_detail_nodata_lay;
    private LListView integral_detail_list;
    private BUser mUser;
    private String lastid = "";

    //类型：不传代表全部1系统赠送2每日签到3邀请下级注册4下级激活5自己购买商品6下线购买商品
    private final static String TYPE_ALL = "";
    private final static String TYPE_SYSTEM = "1";
    private final static String TYPE_PAST = "2";
    private final static String TYPE_INVITE = "3";
    private final static String TYPE_ACTIVATION = "4";
    private final static String TYPE_BUY_OWN = "5";
    private final static String TYPE_BUY_FRIEND = "6";
    private String Current_Type = TYPE_ALL;
    private PopupWindow popupWindow;
    private IntegralOutsideAdapter mAdapter;
    private List<BCIntegralDetail> mDatas;


    @Override
    protected void InItBaseView() {

        setContentView(R.layout.activity_integral_detail);
        SetTitleHttpDataLisenter(this);
        mUser = Spuit.User_Get(BaseContext);
        IView();
        ICache();
        IData(Current_Type,LOAD_INITIALIZE);

    }


    private void IView() {
        integral_detail_list = (LListView) findViewById(R.id.integral_detail_list);
        integral_detail_nodata_lay = findViewById(R.id.integral_detail_nodata_lay);
        integral_detail_list.setPullLoadEnable(true);
        integral_detail_list.setPullRefreshEnable(true);
        integral_detail_list.setXListViewListener(this);
        integral_detail_list.hidefoot();
        mAdapter = new IntegralOutsideAdapter(R.layout.item_integral_detail_outside);
        integral_detail_list.setAdapter(mAdapter);
    }

    private void ICache() {
    }

    private void IData(String type, int loadtype) {
        HashMap<String, String> map = new HashMap<>();
        map.put("member_id", mUser.getMember_id());
        map.put("type", type);
        map.put("page_num", Constants.PageSize + "");
        map.put("last_id", lastid);
        FBGetHttpData(map, Constants.Integral_Detail, Request.Method.GET, 0, loadtype);
    }

    @Override
    protected void InitTile() {

        SetTitleTxt(getString(R.string.integral_detail));
        SetRightText(getResources().getString(R.string.txt_filter));
        right_txt.setOnClickListener(this);
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        switch (Data.getHttpLoadType()) {
            case LOAD_INITIALIZE:
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    integral_detail_list.setVisibility(View.GONE);
                    integral_detail_nodata_lay.setVisibility(View.VISIBLE);
                    integral_detail_nodata_lay.setClickable(false);
                    ShowErrorCanLoad(getResources().getString(R.string.null_integral_detail));
                    mAdapter.FreshData(new ArrayList<BCIntegralDetail>());
                    return;
                }
                integral_detail_list.setVisibility(View.VISIBLE);
                integral_detail_nodata_lay.setVisibility(View.GONE);
                mDatas = new ArrayList<BCIntegralDetail>();
                mDatas = JSON.parseArray(Data.getHttpResultStr(), BCIntegralDetail.class);
                mAdapter.FreshData(mDatas);

                List<BLIntegralDetails> data = getAllIntegralDetailList(mDatas);
                lastid = data.get(data.size() - 1).getId();

                if (mDatas.size() == Constants.PageSize) {
                    integral_detail_list.ShowFoot();
                }

                if (mDatas.size() < Constants.PageSize) {
                    integral_detail_list.hidefoot();
                }

                break;

            case LOAD_REFRESHING:
                integral_detail_list.stopRefresh();
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    PromptManager.ShowCustomToast(BaseContext, getString(R.string.no_new_integral_detail));
                    // ShowErrorCanLoad(getResources().getString(R.string.null_integral_detail));
                    return;
                }

                mDatas = new ArrayList<BCIntegralDetail>();
                mDatas = JSON.parseArray(Data.getHttpResultStr(), BCIntegralDetail.class);
                mAdapter.FreshData(mDatas);
                List<BLIntegralDetails> data1 = getAllIntegralDetailList(mDatas);
                lastid = data1.get(data1.size() - 1).getId();
                if (mDatas.size() == Constants.PageSize) {
                    integral_detail_list.ShowFoot();
                }

                if (mDatas.size() < Constants.PageSize) {
                    integral_detail_list.hidefoot();
                }
                break;

            case LOAD_LOADMOREING:
                integral_detail_list.stopLoadMore();
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    PromptManager.ShowCustomToast(BaseContext, getString(R.string.no_more_integral_detail));
                    // ShowErrorCanLoad(getResources().getString(R.string.null_integral_detail));
                    return;
                }

                mDatas = new ArrayList<BCIntegralDetail>();
                mDatas = JSON.parseArray(Data.getHttpResultStr(), BCIntegralDetail.class);

                if (mDatas.get(0).getMonth().equals(mAdapter.GetApData().get(mAdapter.getCount() - 1).getMonth())) {
                    mAdapter.MergeFrashData(mDatas);
                } else {
                    mAdapter.FreshDataAll(mDatas);
                }

                List<BLIntegralDetails> data2 = getAllIntegralDetailList(mDatas);
                lastid = data2.get(data2.size() - 1).getId();
                if (mDatas.size() == Constants.PageSize) {
                    integral_detail_list.ShowFoot();
                }

                if (mDatas.size() < Constants.PageSize) {
                    integral_detail_list.hidefoot();
                }
                break;
        }
    }

    private List<BLIntegralDetails> getAllIntegralDetailList(List<BCIntegralDetail> datas) {
        List<BLIntegralDetails> details = new ArrayList<BLIntegralDetails>();
        for (int i = 0; i < datas.size(); i++) {
            details.addAll(datas.get(i).getList());
        }
        return details;
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

        switch (V.getId()) {
            case R.id.right_txt:
                if (CheckNet(BaseContext)) return;
                IPopupWindow(V);
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
        IData(Current_Type, LOAD_REFRESHING);
    }

    @Override
    public void onLoadMore() {
        IData(Current_Type, LOAD_LOADMOREING);
    }

    /**
     * 控制PopupWindow
     */
    private void IPopupWindow(View V) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            showPop(V);
        }
    }

    private void showPop(View V) {

        View view = View.inflate(BaseContext, R.layout.pop_integral_filter,
                null);
        TextView tv_all_integral = (TextView) view.findViewById(R.id.tv_all_integral);
        TextView tv_system = (TextView) view.findViewById(R.id.tv_system);
        TextView tv_past = (TextView) view.findViewById(R.id.tv_past);
        TextView tv_invite_register = (TextView) view.findViewById(R.id.tv_invite_register);
        TextView tv_friend_activation = (TextView) view.findViewById(R.id.tv_friend_activation);
        TextView tv_own_buy = (TextView) view.findViewById(R.id.tv_own_buy);
        TextView tv_friend_buy = (TextView) view.findViewById(R.id.tv_friend_buy);


        tv_all_integral.setOnClickListener(this);
        tv_system.setOnClickListener(this);
        tv_past.setOnClickListener(this);
        tv_invite_register.setOnClickListener(this);
        tv_friend_activation.setOnClickListener(this);
        tv_own_buy.setOnClickListener(this);
        tv_friend_buy.setOnClickListener(this);


        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAsDropDown(V, 0, 30);
    }


    class IntegralOutsideAdapter extends BaseAdapter {

        private int ResourseId;
        private LayoutInflater inflater;
        private List<BCIntegralDetail> datas = new ArrayList<BCIntegralDetail>();

        public IntegralOutsideAdapter(int ResourseId) {
            super();
            this.ResourseId = ResourseId;
            this.inflater = LayoutInflater.from(BaseContext);
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        public List<BCIntegralDetail> GetApData() {
            return this.datas;
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void FreshData(List<BCIntegralDetail> data) {
            this.datas.clear();
            this.datas = data;
            this.notifyDataSetChanged();
        }

        public void FreshDataAll(List<BCIntegralDetail> more_data) {
            this.datas.addAll(more_data);
            this.notifyDataSetChanged();
        }

        public void MergeFrashData(List<BCIntegralDetail> dattaa) {

            this.datas.get(getCount() - 1).getList().addAll(dattaa.get(0).getList());
            for (int i = 1; i < dattaa.size(); i++) {
                this.datas.add(dattaa.get(i));
            }
            this.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            IntegralOutsideHoler holer = null;
            if (null == convertView) {
                holer = new IntegralOutsideHoler();
                convertView = inflater.inflate(ResourseId, null);
                holer.integral_month = (TextView) convertView.findViewById(R.id.integral_month);
                holer.lv_integral_list_outside = (CompleteListView) convertView.findViewById(R.id.lv_integral_list_outside);
                convertView.setTag(holer);
            } else {
                holer = (IntegralOutsideHoler) convertView.getTag();
            }
            StrUtils.SetTxt(holer.integral_month, datas.get(position).getMonth());
            holer.lv_integral_list_outside.setAdapter(new IntegralInsideAdapter(R.layout.item_integral_detail, datas.get(position).getList()));
            return convertView;
        }
    }


    class IntegralInsideAdapter extends BaseAdapter {
        private int ResourseId;
        private List<BLIntegralDetails> datas = new ArrayList<BLIntegralDetails>();
        private LayoutInflater inflater;

        public IntegralInsideAdapter(int ResourseId, List<BLIntegralDetails> datas) {
            super();
            this.ResourseId = ResourseId;
            this.datas = datas;
            this.inflater = LayoutInflater.from(BaseContext);
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
            IntegralInsideHolder holder = null;
            if (convertView == null) {
                holder = new IntegralInsideHolder();
                convertView = inflater.inflate(ResourseId, null);
                holder.tv_integral_day = (TextView) convertView.findViewById(R.id.tv_integral_day);
                holder.tv_integral_time = (TextView) convertView.findViewById(R.id.tv_integral_time);
                holder.tv_integral_content = (TextView) convertView.findViewById(R.id.tv_integral_content);
                holder.tv_integral_point = (TextView) convertView.findViewById(R.id.tv_integral_point);
                convertView.setTag(holder);
            } else {
                holder = (IntegralInsideHolder) convertView.getTag();
            }
            BLIntegralDetails data = datas.get(position);
            StrUtils.SetTxt(holder.tv_integral_day, data.getDateStr());
            StrUtils.SetTxt(holder.tv_integral_time, data.getDate());
            StrUtils.SetTxt(holder.tv_integral_content, data.getTitle());

            return convertView;
        }
    }


    class IntegralInsideHolder {
        TextView tv_integral_day, tv_integral_time, tv_integral_content, tv_integral_point;
    }

    class IntegralOutsideHoler {
        TextView integral_month;
        CompleteListView lv_integral_list_outside;
    }
}