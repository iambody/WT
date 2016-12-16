package io.vtown.WeiTangApp.ui.title.myhome;

import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import io.vtown.WeiTangApp.bean.bcomment.easy.BLLevelName;
import io.vtown.WeiTangApp.bean.bcomment.new_three.invite_friends.BCInviteFriends;
import io.vtown.WeiTangApp.bean.bcomment.new_three.invite_friends.BLInviteFriends;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DateUtils;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.comment.view.custom.RefreshLayout;
import io.vtown.WeiTangApp.comment.view.dialog.CommonDialog;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.im.AChatLoad;
import io.vtown.WeiTangApp.ui.title.center.myinvitecode.AMyInviteCode;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;


/**
 * Created by Yihuihua on 2016/10/12.
 */

public class AInviteFriendRecord extends ATitleBase implements LListView.IXListViewListener, TextWatcher {

    private RefreshLayout invite_friends_refrash11;
    private LListView invite_friends_record_list;
    private View invite_friends_nodata_lay;
    private BUser bUser;
    private int page = 1;
    private int scan_page = 1;
    private int nanor_page = 1;
    private int scan_type = 0;

    private List<BCInviteFriends> datass = new ArrayList<BCInviteFriends>();
    private List<BCInviteFriends> scan_datass = new ArrayList<BCInviteFriends>();
    private InviteFriendAdapter mAdapter;

    private TextView tv_invite_date_current;
    private PopupWindow mPopupWindow;
    List<BLLevelName> lv_list = new ArrayList<BLLevelName>();
    private final String Shop_All_Lv = "";
    private String Current_Lv = Shop_All_Lv;


    private final int Type_All = 110;

    private int click_type = Type_All;
    private String phone = "";
    private boolean isScan = false;
    private boolean needLoadMore = false;
    private TextView invite_friends_record_filter;
    private ImageView invite_friends_record_back_iv;
    private ImageView invite_friends_record_sou_iv;
    private ImageView invite_friends_record_title_delete;
    private EditText invite_friends_record_title;
    private List<BCInviteFriends>  more_datas = new ArrayList<BCInviteFriends>();


    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_invite_friends_record);
        SetTitleHttpDataLisenter(this);
        bUser = Spuit.User_Get(BaseContext);
        IView();
        ICache();
        ILvData();
        IData(page, LOAD_INITIALIZE);
    }


    private void IView() {

//        invite_friends_refrash = (RefreshLayout) findViewById(R.id.invite_friends_refrash);
//        invite_friends_refrash.setOnLoadListener(this);
//        invite_friends_refrash.setColorSchemeResources(R.color.app_fen, R.color.app_fen1, R.color.app_fen2, R.color.app_fen3);
//        invite_friends_refrash.setCanLoadMore(false);
        invite_friends_record_list = (LListView) findViewById(R.id.invite_friends_record_list);
        invite_friends_record_list.setXListViewListener(this);
        invite_friends_record_list.setPullRefreshEnable(true);
        invite_friends_record_list.setPullLoadEnable(true);
        invite_friends_record_list.hidefoot();
        invite_friends_nodata_lay = findViewById(R.id.invite_friends_nodata_lay);
        invite_friends_nodata_lay.setOnClickListener(this);
        mAdapter = new InviteFriendAdapter(R.layout.item_invite_friends_outside);
        invite_friends_record_list.setAdapter(mAdapter);

    }

    private void ICache() {
        String invite_friends = CacheUtil.My_Invite_Friends_Get(BaseContext);

        if (StrUtils.isEmpty(invite_friends)) {
            PromptManager.showtextLoading(BaseContext, getResources()
                    .getString(R.string.loading));
        } else {
            try {
                datass = JSON.parseArray(invite_friends, BCInviteFriends.class);
            } catch (Exception e) {
                return;
            }
            if (Current_Lv.equals(Shop_All_Lv)) {
                mAdapter.FreshData(datass);
            }

        }
    }

    private void ILvData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("api_version","3.1.3");
        FBGetHttpData(map, Constants.Shop_lv_list, Request.Method.GET, 1, LOAD_INITIALIZE);
    }

    private void IData(int page, int loadtype) {
        HashMap<String, String> map = new HashMap<>();
        map.put("member_id", bUser.getMember_id());//"10014952"
        map.put("seller_id", bUser.getSeller_id());//"1014719"
        map.put("page", page + "");
        map.put("pagesize",Constants.PageSize + "");

        if (!StrUtils.isEmpty(phone)) {
            map.put("phone", phone);
            scan_type = 2;
        }else{
            if (!Shop_All_Lv.equals(Current_Lv)) {

                map.put("member_level", Current_Lv);
                if ("-1".equals(Current_Lv)) {//如果是普通店铺，是否激活就传0
                    map.put("is_activate", "0");
                }else{
                    map.put("is_activate", "1");
                }
            }
        }
        FBGetHttpData(map, Constants.Invite_Friends, Request.Method.GET, scan_type, loadtype);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt(getString(R.string.invite_friends));
//        SetRightText(getResources().getString(R.string.txt_filter));
        //       right_txt.setOnClickListener(this);

        invite_friends_record_back_iv = (ImageView) findViewById(R.id.invite_friends_record_back_iv);
        invite_friends_record_title = (EditText) findViewById(R.id.invite_friends_record_title);
        invite_friends_record_title_delete = (ImageView) findViewById(R.id.invite_friends_record_title_delete);
        invite_friends_record_sou_iv = (ImageView) findViewById(R.id.invite_friends_record_sou_iv);
        invite_friends_record_filter = (TextView) findViewById(R.id.invite_friends_record_filter);
        invite_friends_record_filter.setOnClickListener(this);
        invite_friends_record_back_iv.setOnClickListener(this);
        invite_friends_record_sou_iv.setOnClickListener(this);
        invite_friends_record_title_delete.setOnClickListener(this);
        invite_friends_record_title.addTextChangedListener(this);

        String shop_lvs = CacheUtil.Shop_Lv_Get(BaseContext);
        if (!StrUtils.isEmpty(shop_lvs)) {
            invite_friends_record_filter.setVisibility(View.VISIBLE);
            lv_list = JSON.parseArray(shop_lvs, BLLevelName.class);
        } else {
            invite_friends_record_filter.setVisibility(View.GONE);
        }
    }


    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        switch (Data.getHttpResultTage()) {
            case 0:
                switch (Data.getHttpLoadType()) {
                    case LOAD_INITIALIZE:
                        if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                            invite_friends_record_list.setVisibility(View.GONE);
                            invite_friends_nodata_lay.setVisibility(View.VISIBLE);
                            invite_friends_nodata_lay.setClickable(false);
                            ShowErrorCanLoad(getString(R.string.null_invite_friend));
                            if (Shop_All_Lv.equals(Current_Lv)) {
                                CacheUtil.My_Invite_Friends_Save(BaseContext, Data.getHttpResultStr());
                            }
                            datass.clear();
                            mAdapter.FreshData(datass);
                            return;
                        }
                        datass = JSON.parseArray(Data.getHttpResultStr(), BCInviteFriends.class);
                        if (Shop_All_Lv.equals(Current_Lv)) {
                            CacheUtil.My_Invite_Friends_Save(BaseContext, Data.getHttpResultStr());
                        }

                        invite_friends_record_list.setVisibility(View.VISIBLE);
                        invite_friends_nodata_lay.setVisibility(View.GONE);
                        mAdapter.FreshData(datass);
                        List<BLInviteFriends> allInviteDetailList = getAllInviteDetailList(datass);
                        if (allInviteDetailList.size() == Constants.PageSize) {
                            needLoadMore = true;
                            invite_friends_record_list.ShowFoot();
                            invite_friends_record_list.setPullLoadEnable(true);

                        }
                        if (allInviteDetailList.size() < Constants.PageSize) {
                            invite_friends_record_list.hidefoot();
                            invite_friends_record_list.setPullLoadEnable(false);
                            needLoadMore = false;
                        }

                        break;

                    case LOAD_REFRESHING:
                        invite_friends_record_list.stopRefresh();
                        if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                            PromptManager.ShowCustomToast(BaseContext, getResources().getString(R.string.no_fresh_invite_friend));
                            return;
                        }
                        datass = JSON.parseArray(Data.getHttpResultStr(), BCInviteFriends.class);
                        mAdapter.FreshData(datass);

                        List<BLInviteFriends> allInviteDetailList1 = getAllInviteDetailList(datass);
                        if (allInviteDetailList1.size() == Constants.PageSize) {
                            invite_friends_record_list.ShowFoot();
                            invite_friends_record_list.setPullLoadEnable(true);
                            needLoadMore = true;
                        }

                        if (allInviteDetailList1.size() < Constants.PageSize) {
                            invite_friends_record_list.hidefoot();
                            invite_friends_record_list.setPullLoadEnable(false);
                            needLoadMore = false;
                        }
                        break;

                    case LOAD_LOADMOREING:
                        invite_friends_record_list.stopLoadMore();
                        if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                            invite_friends_record_list.hidefoot();
                            PromptManager.ShowCustomToast(BaseContext, getResources().getString(R.string.no_nore_invite_friend));
                            return;
                        }
                        //List<BCInviteFriends> more_datas = new ArrayList<BCInviteFriends>();

                        datass = JSON.parseArray(Data.getHttpResultStr(), BCInviteFriends.class);
                        if (datass.get(0).getDate().equals(mAdapter.GetApData().get(mAdapter.getCount() - 1).getDate())) {
                            mAdapter.MergeFrashData(datass);
                        } else {
                            mAdapter.FreshAllData(datass);
                        }

                        List<BLInviteFriends> allInviteDetailList2 = getAllInviteDetailList(datass);
                        if (allInviteDetailList2.size() == Constants.PageSize) {
                            invite_friends_record_list.ShowFoot();
                            invite_friends_record_list.setPullLoadEnable(true);
                            needLoadMore = true;
                        }
                        if (allInviteDetailList2.size() < Constants.PageSize) {
                            invite_friends_record_list.hidefoot();
                            invite_friends_record_list.setPullLoadEnable(false);
                            needLoadMore = false;
                        }
                        break;
                }
                break;
            case 1:

                lv_list = JSON.parseArray(Data.getHttpResultStr(), BLLevelName.class);
                CacheUtil.Shop_Lv_Save(BaseContext, Data.getHttpResultStr());
                if (lv_list.size() > 0) {
                    invite_friends_record_filter.setVisibility(View.VISIBLE);
                }

                break;

            case 2:
                isScan = true;
                switch (Data.getHttpLoadType()) {
                    case LOAD_INITIALIZE:
                        invite_friends_record_list.hidefoot();
                        if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                            invite_friends_record_list.setVisibility(View.GONE);
                            invite_friends_nodata_lay.setVisibility(View.VISIBLE);
                            invite_friends_nodata_lay.setClickable(false);
                            ShowErrorCanLoad(getString(R.string.null_invite_friend));
                            mAdapter.FreshData(new ArrayList<BCInviteFriends>());
                            return;
                        }
                        scan_datass = JSON.parseArray(Data.getHttpResultStr(), BCInviteFriends.class);

                        invite_friends_record_list.setVisibility(View.VISIBLE);
                        invite_friends_nodata_lay.setVisibility(View.GONE);
                        mAdapter.FreshData(scan_datass);

                        List<BLInviteFriends> allInviteDetailList = getAllInviteDetailList(scan_datass);
                        if (allInviteDetailList.size() == Constants.PageSize) {
                            invite_friends_record_list.ShowFoot();
                            invite_friends_record_list.setPullLoadEnable(true);

                        }
                        if (allInviteDetailList.size() < Constants.PageSize) {
                            invite_friends_record_list.hidefoot();
                            invite_friends_record_list.setPullLoadEnable(false);
                        }
                        break;

                    case LOAD_REFRESHING:
                        invite_friends_record_list.stopRefresh();
                        if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                            PromptManager.ShowCustomToast(BaseContext, getResources().getString(R.string.no_fresh_invite_friend));
                            return;
                        }
                        scan_datass = JSON.parseArray(Data.getHttpResultStr(), BCInviteFriends.class);
                        mAdapter.FreshData(scan_datass);

                        List<BLInviteFriends> allInviteDetailList1 = getAllInviteDetailList(scan_datass);
                        if (allInviteDetailList1.size() == Constants.PageSize) {
                            invite_friends_record_list.ShowFoot();
                            invite_friends_record_list.setPullLoadEnable(true);
                        }

                        if (allInviteDetailList1.size() < Constants.PageSize) {
                            invite_friends_record_list.hidefoot();
                            invite_friends_record_list.setPullLoadEnable(false);
                        }

                        break;

                    case LOAD_LOADMOREING:
                        invite_friends_record_list.stopLoadMore();
                        if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                            invite_friends_record_list.hidefoot();
                            PromptManager.ShowCustomToast(BaseContext, getResources().getString(R.string.no_nore_invite_friend));
                            return;
                        }
                        scan_datass = JSON.parseArray(Data.getHttpResultStr(), BCInviteFriends.class);
                        if (scan_datass.get(0).getDate().equals(mAdapter.GetApData().get(mAdapter.getCount() - 1).getDate())) {
                            mAdapter.MergeFrashData(scan_datass);
                        } else {
                            mAdapter.FreshAllData(scan_datass);
                        }
                        List<BLInviteFriends> allInviteDetailList2 = getAllInviteDetailList(scan_datass);
                        if (allInviteDetailList2.size() == Constants.PageSize) {
                            invite_friends_record_list.ShowFoot();
                            invite_friends_record_list.setPullLoadEnable(true);
                        }
                        if (allInviteDetailList2.size() < Constants.PageSize) {
                            invite_friends_record_list.hidefoot();
                            invite_friends_record_list.setPullLoadEnable(false);
                        }

                        break;
                }


                break;
        }


    }

    private void showLvPop(View V, List<BLLevelName> lv_list) {
        View view = LayoutInflater.from(BaseContext).inflate(R.layout.pop_shop_filter, null);
        LinearLayout ll_pop_shop_filter = (LinearLayout) view.findViewById(R.id.ll_pop_shop_filter);
        TextView tv_shop_all = (TextView) view.findViewById(R.id.tv_shop_all);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
        for (int i = 0; i < lv_list.size(); i++) {
            View line = new View(BaseContext);
            line.setBackgroundResource(R.color.white);
            line.setLayoutParams(lineParams);
            TextView textView = new TextView(BaseContext);
            textView.setClickable(true);
            textView.setLayoutParams(textParams);
            textView.setPadding(5, DimensionPixelUtil.dip2px(BaseContext, 8), 5, DimensionPixelUtil.dip2px(BaseContext, 8));
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView.setText(lv_list.get(i).getLevel_name());
            textView.setOnClickListener(new OnPopClickListener(i));
            ll_pop_shop_filter.addView(textView);
            ll_pop_shop_filter.addView(line);
        }
        tv_shop_all.setOnClickListener(this);
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        mPopupWindow.setBackgroundDrawable(dw);
        mPopupWindow.showAsDropDown(V, 0, 0);

    }

    /**
     * 控制PopupWindow
     */
    private void IPopupWindow(View V) {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        } else {
            showLvPop(V, lv_list);
        }
    }


    private List<BLInviteFriends> getAllInviteDetailList(List<BCInviteFriends> datas) {
        List<BLInviteFriends> details = new ArrayList<BLInviteFriends>();
        for (int i = 0; i < datas.size(); i++) {
            details.addAll(datas.get(i).getList());
        }
        return details;
    }

    @Override
    protected void DataError(String error, int LoadType) {
        switch (LoadType) {
            case LOAD_INITIALIZE:
                invite_friends_nodata_lay.setVisibility(View.GONE);
                invite_friends_nodata_lay.setVisibility(View.VISIBLE);
                invite_friends_nodata_lay.setClickable(true);
                ShowErrorCanLoad(error);
                //invite_friends_refrash.setRefreshing(false);
                break;
            case LOAD_REFRESHING:
                //invite_friends_refrash.setRefreshing(false);
                invite_friends_record_list.stopRefresh();
                break;
            case LOAD_LOADMOREING:
                //invite_friends_refrash.setLoading(false);
                invite_friends_record_list.stopLoadMore();
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
            case R.id.invite_friends_nodata_lay:
                if (CheckNet(BaseContext)) return;
                page = 1;
                IData(page, LOAD_INITIALIZE);
                break;

            case R.id.invite_friends_record_filter:
                if (CheckNet(BaseContext)) return;
                IPopupWindow(V);
                break;
            case R.id.tv_shop_all:
                LvSwitch(Shop_All_Lv, getResources().getString(R.string.invite_friends), Type_All);
                break;

            case R.id.invite_friends_record_back_iv://返回按钮
                AInviteFriendRecord.this.finish();
                overridePendingTransition(R.anim.push_rigth_in, R.anim.push_rigth_out);
                break;

            case R.id.invite_friends_record_title_delete://删除搜索框内容
                invite_friends_record_title.setText("");
                invite_friends_record_title.setHint(R.string.invite_friends_record_scen_key);
                page = nanor_page;
                deleteScanContent();
                break;

            case R.id.invite_friends_record_sou_iv://搜索按钮
                String scen_key = invite_friends_record_title.getText().toString();
                if (StrUtils.isEmpty(scen_key)) {
                    PromptManager.ShowCustomToast(BaseContext, "请输入您要搜索的手机号");
                    return;
                }
                phone = scen_key;
                //Current_Lv = Shop_All_Lv;
                PromptManager.showtextLoading(BaseContext, getResources()
                        .getString(R.string.loading));
                datass = mAdapter.GetApData();
                page = 1;
                IData(page, LOAD_INITIALIZE);

                break;
        }


    }

    private void deleteScanContent() {
        scan_type = 0;
        phone = "";

        if (datass.size() > 0) {
            invite_friends_record_list.setVisibility(View.VISIBLE);
            invite_friends_nodata_lay.setVisibility(View.GONE);
        }else{
            invite_friends_record_list.setVisibility(View.GONE);
            invite_friends_nodata_lay.setVisibility(View.VISIBLE);
        }

        if (isScan) {
            RefrashAp();
        }
        isScan = false;
    }

    private void RefrashAp() {
        mAdapter.FreshData(datass);
        if (needLoadMore) {
            invite_friends_record_list.ShowFoot();
            invite_friends_record_list.setPullLoadEnable(true);
        } else {
            invite_friends_record_list.hidefoot();
            invite_friends_record_list.setPullLoadEnable(false);
        }
    }

    private void LvSwitch(String switch_type, String titlename, int type) {

        if (type != click_type ||scan_type == 2) {//
            invite_friends_record_title.setText("");
            invite_friends_record_title.setHint(R.string.invite_friends_record_scen_key);
            scan_type = 0;
            phone = "";
            click_type = type;
            Current_Lv = switch_type;
            page = 1;
            SetTitleTxt(titlename);
            IData(page, LOAD_INITIALIZE);
            mAdapter.Clearn();
            invite_friends_nodata_lay.setVisibility(View.GONE);
            invite_friends_record_list.hidefoot();
            PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.xlistview_header_hint_loading));
        }
        mPopupWindow.dismiss();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!StrUtils.isEmpty(s.toString())) {
            invite_friends_record_title_delete.setVisibility(View.VISIBLE);
        } else {
            invite_friends_record_title_delete.setVisibility(View.GONE);
            deleteScanContent();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    class OnPopClickListener implements View.OnClickListener {
        private int clickposition;

        public OnPopClickListener(int position) {
            clickposition = position;
        }

        @Override
        public void onClick(View v) {
            LvSwitch(lv_list.get(clickposition).getLevel_id()+"", lv_list.get(clickposition).getLevel_name(), lv_list.get(clickposition).getLevel_id());
        }
    }

    @Override
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }

//    @Override
//    public void OnLoadMore() {
//        page++;
//        if (scan_type == 0) {
//            nanor_page = page;
//        }
//
//        IData(page, LOAD_LOADMOREING);
//    }
//
//    @Override
//    public void OnFrash() {
//        page = 1;
//        nanor_page = 1;
//        IData(page, LOAD_REFRESHING);
//    }


    @Override
    public void onRefresh() {
        page = 1;
        if (scan_type == 0) {
            nanor_page = 1;
        }
        IData(page, LOAD_REFRESHING);
    }

    @Override
    public void onLoadMore() {
        page++;
        if (scan_type == 0) {
            nanor_page = page;
        }

        IData(page, LOAD_LOADMOREING);
    }


    class InviteFriendAdapter extends BaseAdapter {
        private List<BCInviteFriends> datas = new ArrayList<BCInviteFriends>();
        private int ResourseId;

        private LayoutInflater inflater;

        private int mPosition;

        public InviteFriendAdapter(int ResourseId) {
            super();
            this.ResourseId = ResourseId;
            this.inflater = LayoutInflater.from(BaseContext);
        }

        public void Clearn() {
            datas = new ArrayList<BCInviteFriends>();
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            Log.i("tests","getCount长度"+datas.size());
            Log.i("tests","getCount长度datass"+datass.size());
            Log.i("tests","getCount长度more_datas====》"+more_datas.size());
            return this.datas.size();
        }

        public List<BCInviteFriends> GetApData() {
            return this.datas;
        }

        @Override
        public Object getItem(int position) {
            return this.datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void FreshData(List<BCInviteFriends> datas1) {
            this.datas = datas1;
            this.notifyDataSetChanged();
        }

        public void FreshAllData(List<BCInviteFriends> more_datas1) {
            Log.i("tests","FreshAllData长度前====》"+datas.size());
            Log.i("tests","FreshAllData长度前more_datas====》"+more_datas.size());
            this.datas.addAll(more_datas1);
            this.notifyDataSetChanged();
            Log.i("tests","FreshAllData长度后more_datas====》"+more_datas.size());
            Log.i("tests","FreshAllData长度后====》"+datas.size());
        }

        public void MergeFrashData(List<BCInviteFriends> dattaa) {
            this.datas.get(getCount() - 1).getList().addAll(dattaa.get(0).getList());
            for (int i = 1; i < dattaa.size(); i++) {
                this.datas.add(dattaa.get(i));
            }
            this.notifyDataSetChanged();
            Log.i("tests","MergeFrashData长度"+datas.size());
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            InviteFriendHolder holder = null;
            if (null == convertView) {
                holder = new InviteFriendHolder();
                convertView = inflater.inflate(ResourseId, null);
                holder.tv_invite_date = (TextView) convertView.findViewById(R.id.tv_invite_date);
                holder.invite_friends_record_inside = (CompleteListView) convertView.findViewById(R.id.invite_friends_record_inside);
                convertView.setTag(holder);
            } else {
                holder = (InviteFriendHolder) convertView.getTag();
            }

            mPosition = position;
            final BCInviteFriends bcInviteFriends = datas.get(mPosition);
            StrUtils.SetTxt(holder.tv_invite_date, datas.get(position).getDate());
            FriendsAdapter friendsAdapter = new FriendsAdapter(R.layout.item_invite_friends_detail, datas.get(position).getList());
            holder.invite_friends_record_inside.setAdapter(friendsAdapter);
            holder.invite_friends_record_inside.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final BLInviteFriends friend = bcInviteFriends.getList().get(position);
//                    BComment mBComment = new BComment(friend.getSeller_id(), friend
//                            .getSeller_name());
//                    PromptManager.SkipActivity(BaseActivity, new Intent(
//                            BaseActivity, AShopDetail.class).putExtra(
//                            BaseKey_Bean, mBComment));

                    ContactFriend(friend);


                }
            });
            return convertView;
        }
    }

    /**
     * 联系客服---拨号
     */
    public void ContactFriend(final BLInviteFriends friend) {


        ShowCustomDialog("联系：" + friend.getSeller_name(), "复制手机号", "微糖联系", new IDialogResult() {
            @Override
            public void LeftResult() {

                ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                String content = friend.getPhone();
                c.setText(content);
                PromptManager.ShowCustomToast(BaseContext, "手机号复制成功");

            }

            @Override
            public void RightResult() {
                if (!StrUtils.isEmpty(friend.getSeller_id()))
                    PromptManager.SkipActivity(
                            BaseActivity,
                            new Intent(BaseActivity, AChatLoad.class)
                                    .putExtra(AChatLoad.Tage_TageId,
                                            friend.getSeller_id())
                                    .putExtra(AChatLoad.Tage_Name,
                                            friend.getSeller_name())
                                    .putExtra(AChatLoad.Tage_Iv,
                                            friend.getAvatar()));

            }
        });

    }

    private void ControlFriendDialog(final BLInviteFriends friend){
        List<String> strs = new ArrayList<>();
        strs.add("复制手机号");
        strs.add("发送信息");
        CommonDialog commonDialog = new CommonDialog(AInviteFriendRecord.this,strs);
        commonDialog.setOnCommonDialogClickListener(new CommonDialog.OnCommonDialogClickListener() {
            @Override
            public void clickPosition(int position) {
                switch (position){
                    case 0:
                        ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        String content = friend.getPhone();
                        c.setText(content);
                        PromptManager.ShowCustomToast(BaseContext, "手机号复制成功");

                        break;

                    case 1:
                        if (!StrUtils.isEmpty(friend.getSeller_id()))
                            PromptManager.SkipActivity(
                                    BaseActivity,
                                    new Intent(BaseActivity, AChatLoad.class)
                                            .putExtra(AChatLoad.Tage_TageId,
                                                    friend.getSeller_id())
                                            .putExtra(AChatLoad.Tage_Name,
                                                    friend.getSeller_name())
                                            .putExtra(AChatLoad.Tage_Iv,
                                                    friend.getAvatar()));
                        break;
                }
            }
        });
        commonDialog.show();
    }


    class FriendsAdapter extends BaseAdapter {

        private int ResourseId;
        private LayoutInflater inflater;
        private List<BLInviteFriends> friends_datas = new ArrayList<BLInviteFriends>();

        public FriendsAdapter(int ResourseId, List<BLInviteFriends> friends_datas) {
            super();
            this.ResourseId = ResourseId;
            this.friends_datas = friends_datas;
            this.inflater = LayoutInflater.from(BaseContext);
        }

        @Override
        public int getCount() {
            return friends_datas.size();
        }

        @Override
        public Object getItem(int position) {
            return friends_datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FriendsHolder holder = null;
            if (convertView == null) {
                holder = new FriendsHolder();
                convertView = inflater.inflate(ResourseId, null);
                holder.iv_friend_icon = (CircleImageView) convertView.findViewById(R.id.iv_friend_icon);
                holder.iv_friend_lv = (ImageView) convertView.findViewById(R.id.iv_friend_lv);
                holder.tv_friend_name = (TextView) convertView.findViewById(R.id.tv_friend_name);
                holder.tv_friend_shop_id = (TextView) convertView.findViewById(R.id.tv_friend_shop_id);
                holder.tv_invite_phone = (TextView) convertView.findViewById(R.id.tv_invite_phone);
                holder.iv_lv = (ImageView) convertView.findViewById(R.id.iv_lv);
                holder.list_line = convertView.findViewById(R.id.list_line);
                convertView.setTag(holder);
            } else {
                holder = (FriendsHolder) convertView.getTag();
            }
            BLInviteFriends friend = friends_datas.get(position);
            ImageLoaderUtil.Load2(friend.getAvatar(), holder.iv_friend_icon, R.drawable.error_iv2);

            StrUtils.SetTxt(holder.tv_friend_name, friend.getSeller_name());
            String shop_id = BaseContext.getResources().getString(R.string.invite_friend_shop_id);
            StrUtils.SetTxt(holder.tv_friend_shop_id, String.format(shop_id, friend.getSeller_no()));
            StrUtils.SetTxt(holder.tv_invite_phone, friend.getPhone());

            if ("0".equals(friend.getIs_activate())) {
                holder.iv_friend_lv.setImageDrawable(BaseContext.getResources().getDrawable(R.drawable.ic_putongdianpuxiaotubiao_nor));

            } else {
                holder.iv_friend_lv.setImageDrawable(BaseContext.getResources().getDrawable(R.drawable.ic_mingxingdianpuxiaotubiao_nor));


            }
            ImageLoaderUtil.Load2(friend.getMember_level_picture(), holder.iv_lv, R.drawable.error_iv2);
            if (friends_datas.size() - 1 == position) {
                holder.list_line.setVisibility(View.GONE);
            } else {
                holder.list_line.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }

    class InviteFriendHolder {
        TextView tv_invite_date;
        CompleteListView invite_friends_record_inside;
    }

    class FriendsHolder {
        CircleImageView iv_friend_icon;
        ImageView iv_friend_lv, iv_lv;
        TextView tv_friend_name, tv_friend_shop_id, tv_invite_phone;
        View list_line;


    }
}
