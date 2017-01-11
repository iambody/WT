package io.vtown.WeiTangApp.ui.title.center.mycoupons;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.coupons.BLMyCoupons;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.comment.view.pop.PShare;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-31 下午2:54:33 我的卡卷页面
 */
public class AMyCoupons extends ATitleBase {

    /**
     * 我的卡卷列表
     */
    private ListView lv_my_conpons_list;
    /**
     * AP
     */
    private MyConpousAdapter myConpousAdapter;

    private boolean IsFromOderBeing = false;

    public static final String Tage_key = "isfromoder";
    private BUser mBUser = new BUser();
    /**
     * 获取数据成功显示的布局
     */
    private LinearLayout center_my_coupons_outlay;
    /**
     * 获取数据失败显示的布局
     */
    private View center_my_coupons_nodata_lay;
    /**
     * 查看过期卡券
     */
    private TextView tv_look_over_time_coupons;
    private LinearLayout ll_look_out_data_coupons;

    private List<BLMyCoupons> listdata = new ArrayList<BLMyCoupons>();

    private View mView;

    @Override
    protected void InItBaseView() {
        mView = LayoutInflater.from(BaseContext).inflate(R.layout.activity_center_my_conpons, null);
        setContentView(mView);
        mBUser = Spuit.User_Get(getApplicationContext());

        GetRequst();
        IView();
        ICache();
        IData();
    }

    private void GetRequst() {
        IsFromOderBeing = getIntent().getBooleanExtra(Tage_key, false);
    }

    private void IData() {

        SetTitleHttpDataLisenter(this);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", mBUser.getId());
        map.put("status", "1");// 2全部 1 可用 0 过期
        FBGetHttpData(map, Constants.CENTER_MY_COUPONS, Method.GET, 0,
                LOAD_INITIALIZE);
    }

    private void ICache() {
        String cache_data = CacheUtil.My_Coupons_Get(getApplicationContext());
        if (StrUtils.isEmpty(cache_data)) {
            PromptManager.showtextLoading(BaseContext,
                    getResources()
                            .getString(R.string.xlistview_header_hint_loading));

        } else {
            lv_my_conpons_list.setVisibility(View.VISIBLE);
            try {
                listdata = JSON.parseArray(cache_data, BLMyCoupons.class);
            } catch (Exception e) {
                return;
            }
            myConpousAdapter.FrashData(listdata);
        }
    }

    private void IView() {

        center_my_coupons_outlay = (LinearLayout) findViewById(R.id.center_my_coupons_outlay);
//		center_my_coupons_nodata_lay = findViewById(R.id.center_my_coupons_nodata_lay);

//		center_my_coupons_nodata_lay.setOnClickListener(this);
        lv_my_conpons_list = (ListView) findViewById(R.id.lv_my_conpons_list);
//		IDataView(lv_my_conpons_list, center_my_coupons_nodata_lay,
//				NOVIEW_INITIALIZE);

        View view = LayoutInflater.from(BaseContext).inflate(R.layout.my_conpons_list_footer, null);


        ll_look_out_data_coupons = (LinearLayout) view.findViewById(R.id.ll_look_out_data_coupons);
        tv_look_over_time_coupons = (TextView) view.findViewById(R.id.tv_look_over_time_coupons);
        lv_my_conpons_list.setVisibility(View.GONE);
        // 如果是确认订单过来的就不需要显示查看过期卡券
        if (!IsFromOderBeing) {
            lv_my_conpons_list.addFooterView(view);
        }

        tv_look_over_time_coupons.setOnClickListener(this);


        myConpousAdapter = new MyConpousAdapter(BaseContext,
                R.layout.item_my_coupons_lv2);
        lv_my_conpons_list.setAdapter(myConpousAdapter);
        lv_my_conpons_list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                BLMyCoupons blComment = (BLMyCoupons) arg0.getItemAtPosition(arg2);

                if (IsFromOderBeing) {// 需要返回到订单页面上数据
                    Intent intent = new Intent();
                    intent.putExtra("coupresult", blComment);
//					intent.putExtra("coupresult", blComment);
                    setResult(202, intent);
                    finish();
                }
            }
        });
    }


    @Override
    protected void InitTile() {
        SetTitleTxt(getResources().getString(R.string.center_kaquan));
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        lv_my_conpons_list.setVisibility(View.VISIBLE);
        if (StrUtils.isEmpty(Data.getHttpResultStr())) {
            PromptManager.ShowCustomToast(BaseContext, "暂无优惠券");
            //IDataView(lv_my_conpons_list, center_my_coupons_nodata_lay,
            //		NOVIEW_ERROR);
            myConpousAdapter.FrashData(new ArrayList<BLMyCoupons>());
            CacheUtil.My_Coupons_Save(getApplicationContext(), "");
            return;
        }


        try {
            listdata = JSON
                    .parseArray(Data.getHttpResultStr(), BLMyCoupons.class);

        } catch (Exception e) {
            DataError("解析失败", 1);
        }
        CacheUtil.My_Coupons_Save(getApplicationContext(), Data.getHttpResultStr());
//		IDataView(lv_my_conpons_list, center_my_coupons_nodata_lay,
//				NOVIEW_RIGHT);
        myConpousAdapter.FrashData(listdata);
    }

    @Override
    protected void DataError(String error, int LoadType) {
        lv_my_conpons_list.setVisibility(View.VISIBLE);
        PromptManager.ShowCustomToast(BaseContext, error);
        switch (LoadType) {
            case LOAD_INITIALIZE:
//				IDataView(lv_my_conpons_list, center_my_coupons_nodata_lay,
//						NOVIEW_ERROR);
                IData();
                break;
            case LOAD_REFRESHING:// 刷新数据


                break;
            case LOAD_LOADMOREING:// 加载更多


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
//		case R.id.center_my_coupons_nodata_lay:// 重新加载数据
//			IData();

//			break;
            case R.id.tv_look_over_time_coupons:// 查看过期卡券
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                        AMyOutDataCoupons.class));
                break;

            default:
                break;
        }
    }

    @Override
    protected void InItBundle(Bundle bundle) {
    }

    @Override
    protected void SaveBundle(Bundle bundle) {
    }

    class MyConpousAdapter extends BaseAdapter {

        private int ResourcesId;
        private Context context;

        /**
         * 填充器
         */
        private LayoutInflater inflater;

        /**
         * 数据
         */
        private List<BLMyCoupons> datas = new ArrayList<BLMyCoupons>();

        public MyConpousAdapter(Context context, int ResourcesId) {
            super();
            this.context = context;
            this.ResourcesId = ResourcesId;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {

            return datas.size();
        }

        /**
         * 刷新数据
         *
         * @param dass
         */
        public void FrashData(List<BLMyCoupons> dass) {
            this.datas = dass;
            this.notifyDataSetChanged();
        }

        /**
         * 加载更多
         */
        public void AddFrashData(List<BLMyCoupons> dass) {
            this.datas.addAll(datas);
            this.notifyDataSetChanged();
        }

        @Override
        public Object getItem(int arg0) {

            return datas.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {

            return arg0;
        }

        @Override
        public View getView(final int arg0, View arg1, ViewGroup arg2) {

            MyCouponsItem myCoupons = null;
            if (arg1 == null) {
                myCoupons = new MyCouponsItem();
                arg1 = inflater.inflate(ResourcesId, null);
//				myCoupons.iv_coupons_item_bg = ViewHolder.get(arg1,
//						R.id.iv_coupons_item_bg);

                myCoupons.tv_my_coupons_price = ViewHolder.get(arg1,
                        R.id.tv_my_coupons_price);
                myCoupons.tv_coupons_type = ViewHolder.get(arg1, R.id.tv_coupons_type);
                myCoupons.tv_my_coupons_name = ViewHolder.get(arg1,
                        R.id.tv_my_coupons_type);
                myCoupons.tv_coupons_donation = ViewHolder.get(arg1, R.id.tv_coupons_donation);
                myCoupons.tv_coupons_other_donation = ViewHolder.get(arg1, R.id.tv_coupons_other_donation);
                myCoupons.tv_my_coupons_validity = ViewHolder.get(arg1,
                        R.id.tv_my_coupons_validity);
                myCoupons.tv_apply_shops = ViewHolder.get(arg1, R.id.tv_apply_shops);
                myCoupons.tv_look_all_shop = ViewHolder.get(arg1, R.id.tv_look_all_shop);
                arg1.setTag(myCoupons);
            } else {
                myCoupons = (MyCouponsItem) arg1.getTag();
            }

            StrUtils.SetTxt(myCoupons.tv_my_coupons_price,
                    StrUtils.SetTextForMony(datas.get(arg0).getCoupons_money()));
            StrUtils.SetTxt(myCoupons.tv_coupons_type, datas.get(arg0).getCoupons_type());
            StrUtils.SetTxt(myCoupons.tv_my_coupons_name, datas.get(arg0)
                    .getCoupons_name());
            String validity = getString(R.string.my_coupons_validity);

            StrUtils.SetTxt(myCoupons.tv_my_coupons_validity, "有效期:" + String.format(
                    validity, datas.get(arg0).getUsed_starttime(),
                    datas.get(arg0).getUsed_endtime()));

            if (IsFromOderBeing) {
                myCoupons.tv_coupons_donation.setVisibility(View.GONE);

            } else {
                myCoupons.tv_coupons_donation.setVisibility(View.VISIBLE);
            }


            if ("0".equals(datas.get(arg0).getApply_store())) {
                myCoupons.tv_look_all_shop.setVisibility(View.GONE);
                StrUtils.SetTxt(myCoupons.tv_apply_shops, "全场通用");
            } else {
                myCoupons.tv_look_all_shop.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(myCoupons.tv_apply_shops, "限部分店铺可用");
            }


            if (0 == datas.get(arg0).getDonation()) {
                myCoupons.tv_coupons_donation.setVisibility(View.VISIBLE);
                myCoupons.tv_coupons_other_donation.setVisibility(View.GONE);
            } else if (2 == datas.get(arg0).getDonation()) {
                myCoupons.tv_coupons_donation.setVisibility(View.GONE);
                myCoupons.tv_coupons_other_donation.setVisibility(View.VISIBLE);
            } else {
                myCoupons.tv_coupons_donation.setVisibility(View.GONE);
                myCoupons.tv_coupons_other_donation.setVisibility(View.GONE);
            }
//			myCoupons.iv_coupons_item_bg.setBackgroundResource(datas.get(arg0)
//					.getStatus().equals("1") ? R.drawable.ico_kaquan_nor
//					: R.drawable.ico_kaquan_press);
            myCoupons.tv_coupons_donation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BNew mBNew = new BNew();
                    mBNew.setShare_url(datas.get(arg0).getCoupons_url());
                    mBNew.setShare_content(StrUtils.isEmpty(datas.get(arg0).getCoupons_type()) ? BaseContext.getResources().getString(R.string.invter_share_conten) : datas.get(arg0).getCoupons_type());
                    mBNew.setShare_title(mBUser.getSeller_name() + "分享了" + datas.get(arg0).getCoupons_name());
                    mBNew.setShare_log(datas.get(arg0).getCoupons_img());
//                ShowP(mView, mBNew);
                    PShare da = new PShare(BaseContext, mBNew, false);
                    da.showAtLocation(mView, Gravity.BOTTOM, 0, 0);

                }
            });

            myCoupons.tv_look_all_shop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaseContext, ACouponsShop.class);
                    intent.putExtra(ACouponsShop.KEY_COUPONS_NAME, datas.get(arg0).getCoupons_name());
                    intent.putExtra(ACouponsShop.KEY_COUPONS_ID, datas.get(arg0).getCoupons_id());
                    PromptManager.SkipActivity(BaseActivity, intent);
                }
            });
            return arg1;
        }

        class MyCouponsItem {
            TextView tv_my_coupons_price, tv_my_coupons_name, tv_coupons_type, tv_apply_shops, tv_look_all_shop,
                    tv_my_coupons_validity, tv_coupons_donation, tv_coupons_other_donation;
        }

    }

}
