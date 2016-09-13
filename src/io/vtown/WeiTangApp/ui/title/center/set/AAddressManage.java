package io.vtown.WeiTangApp.ui.title.center.set;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.onConfirmClick;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.oncancelClick;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-22 下午4:31:13
 *          <p/>
 *          地址管理页面
 */
public class AAddressManage extends ATitleBase {

    /**
     * 地址列表
     */
    private ListView lv_address_list;
    /**
     * 新建收货人地址按钮
     */
    private Button btn_add_new_consignee_address;
    /**
     * AP
     */
    private AddressAdapter addressAdapter;

    private int pos = 0;

    BDComment BD = null;

    /**
     * 用户相关信息
     */
    private BUser user_Get;

    /**
     * 是否需要Finish
     */
    private boolean needFinish;

    /**
     * 获取数据成功显示的布局
     */
    private LinearLayout center_address_manage_outlay;
    /**
     * 获取数据失败显示的布局
     */
    private View center_address_manage_nodata_lay;

    /**
     * 新建收货人按钮
     */
    private Button btn_add_new_consignee_address1;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_center_set_personal_data_addressmanage);
        SetTitleHttpDataLisenter(this);
        user_Get = Spuit.User_Get(getApplicationContext());
        needFinish = getIntent().getBooleanExtra("NeedFinish", false);
        IView();
        ICache();
        IData();

    }

    private void IData() {

        PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.xlistview_header_hint_loading));


        String id = user_Get.getId();

        HashMap<String, String> map = new HashMap<String, String>();
        //map.put("address_id", "2");
        map.put("member_id", id);
        FBGetHttpData(map, Constants.SET_ADDRESS_MANAGE, Method.GET, 0,
                LOAD_INITIALIZE);
    }

    private void AddressSet(String member_id, String address_id, int LoadType) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", member_id);
        map.put("address_id", address_id);

        switch (LoadType) {
            case 1:// 设置默认地址
                FBGetHttpData(map, Constants.set_Default_Address, Method.PUT, 1,
                        LOAD_INITIALIZE);
                break;

            case 2:// 删除地址
                FBGetHttpData(map, Constants.set_Delete_Address, Method.DELETE, 2,
                        LOAD_INITIALIZE);
                break;

            default:
                break;
        }

    }


    private void ICache() {
        String center_Set_Address = CacheUtil.Center_Set_Address_Get(BaseContext);
        if (StrUtils.isEmpty(center_Set_Address)) {
            return;
        }
        try {
            BD = JSON.parseObject(center_Set_Address, BDComment.class);
        } catch (Exception e) {
            return;
        }
        addressAdapter.FrashData(BD.getList(), BD.getDefault_id());
    }

    /**
     * 获取地址详情
     *
     * @param member_id
     */
    private void AddressDetail(String member_id) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", member_id);
        FBGetHttpData(map, Constants.Default_Address_Detail, Method.GET, 3,
                LOAD_INITIALIZE);
    }

    private void IView() {

        center_address_manage_outlay = (LinearLayout) findViewById(R.id.center_address_manage_outlay);
        center_address_manage_nodata_lay = findViewById(R.id.center_address_manage_nodata_lay);
        btn_add_new_consignee_address1 = (Button) center_address_manage_nodata_lay.findViewById(R.id.btn_add_new_consignee_address1);
        IDataView(center_address_manage_outlay, center_address_manage_nodata_lay, NOVIEW_INITIALIZE);
        btn_add_new_consignee_address1.setVisibility(View.GONE);
        lv_address_list = (ListView) findViewById(R.id.lv_address_list);
        btn_add_new_consignee_address = (Button) findViewById(R.id.btn_add_new_consignee_address);
        btn_add_new_consignee_address.setOnClickListener(this);
        center_address_manage_nodata_lay.setOnClickListener(this);
        btn_add_new_consignee_address1.setOnClickListener(this);

        IList();
    }

    private void IList() {
        addressAdapter = new AddressAdapter(R.layout.item_address_manage);
        lv_address_list.setAdapter(addressAdapter);

        if (needFinish) {
            lv_address_list.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {

                    BLComment bl = (BLComment) addressAdapter.getItem(arg2);
                    Intent intent = new Intent();
                    intent.putExtra("AddressInfo", bl);
                    setResult(RESULT_OK, intent);
                    AAddressManage.this.finish();

                }
            });
        }


    }

    @Override
    protected void InitTile() {
        SetTitleTxt(getString(R.string.address_manage));
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

        switch (Data.getHttpResultTage()) {

            case 0:// 获取列表
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    if (LOAD_INITIALIZE == Data.getHttpLoadType()) {
                        IDataView(center_address_manage_outlay, center_address_manage_nodata_lay, NOVIEW_ERROR);
                        btn_add_new_consignee_address1.setVisibility(View.VISIBLE);
                        DataError(getResources().getString(R.string.error_null_address), Data.getHttpLoadType());
                        ShowErrorCanLoad(getResources().getString(R.string.error_null_address));
                        center_address_manage_nodata_lay.setClickable(false);
                    }
                    CacheUtil.Center_Set_Address_Save(BaseContext, Data.getHttpResultStr());
                    //DataError(Msg, Data.getHttpLoadType());
                    return;
                }

                try {
                    BD = JSON.parseObject(Data.getHttpResultStr(), BDComment.class);
                } catch (Exception e) {
                    DataError("解析失败", 1);
                }
                CacheUtil.Center_Set_Address_Save(BaseContext, Data.getHttpResultStr());
                IDataView(center_address_manage_outlay, center_address_manage_nodata_lay, NOVIEW_RIGHT);
                addressAdapter.FrashData(BD.getList(), BD.getDefault_id());
                break;
            case 1:// 设置默认地址
                PromptManager.ShowMyToast(BaseContext, "设置成功");
                IData();

                break;

            case 2:// 删除地址
                PromptManager.ShowMyToast(BaseContext, "删除成功");
                IData();
                break;

            case 3:

                break;

            default:

                break;
        }

    }

    @Override
    protected void DataError(String error, int LoadTyp) {
        PromptManager.ShowMyToast(BaseActivity, error);
        if (LOAD_INITIALIZE == LoadTyp) {
            ShowErrorCanLoad(getResources().getString(R.string.error_null_noda));
            IDataView(center_address_manage_outlay, center_address_manage_nodata_lay, NOVIEW_ERROR);
            btn_add_new_consignee_address1.setVisibility(View.VISIBLE);
            center_address_manage_nodata_lay.setClickable(true);
        }
    }

    @Override
    protected void NetConnect() {
        NetError.setVisibility(View.GONE);
        IData();
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
            case R.id.btn_add_new_consignee_address:
            case R.id.btn_add_new_consignee_address1://没有获取到数据情况下的新建收货人地址按钮
                PromptManager.SkipResultActivity(BaseActivity, new Intent(
                        BaseActivity, AAddConsigneeAddress.class), 100);
                break;

            case R.id.center_address_manage_nodata_lay://重新加载数据
                btn_add_new_consignee_address1.setVisibility(View.GONE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (100 == requestCode && resultCode == RESULT_OK) {
            IData();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    class AddressAdapter extends BaseAdapter {

        private int ResourcesId;

        /**
         * 填充器
         */
        private LayoutInflater inflater;

        /**
         * 数据
         */
        private List<BLComment> datas = new ArrayList<BLComment>();

        private List<Boolean> mBooleans = new ArrayList<Boolean>();

        private int position = 0;

        public AddressAdapter(int ResourcesId) {
            super();
            this.ResourcesId = ResourcesId;
            this.inflater = LayoutInflater.from(BaseContext);
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
        public void FrashData(List<BLComment> dass, int postion) {
            this.position = postion;
            this.datas = dass;
            for (BLComment blComment : dass) {
                if (postion == dass.indexOf(blComment)) {
                    mBooleans.add(true);
                }
                mBooleans.add(false);
            }
            this.notifyDataSetChanged();
        }

        /**
         * 加载更多
         */
        public void AddFrashData(List<BLComment> dass) {
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
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            AddressItem item = null;
            if (arg1 == null) {

                arg1 = inflater.inflate(ResourcesId, null);
                item = new AddressItem();
                item.tv_usr_name = ViewHolder.get(arg1, R.id.tv_usr_name);
                item.tv_usr_phone_numb = ViewHolder.get(arg1,
                        R.id.tv_usr_phone_numb);
                item.tv_delivery_address = ViewHolder.get(arg1,
                        R.id.tv_delivery_address);
                item.tv_default_address = ViewHolder.get(arg1,
                        R.id.tv_default_address);
                item.tv_edit = ViewHolder.get(arg1, R.id.tv_edit);
                item.tv_delete = ViewHolder.get(arg1, R.id.tv_delete);
                item.iv_quan_select = ViewHolder.get(arg1, R.id.iv_quan_select);
                item.ll_select_default = ViewHolder.get(arg1,
                        R.id.ll_select_default);

                arg1.setTag(item);

            } else {
                item = (AddressItem) arg1.getTag();
            }

            int address_id = Integer.parseInt(datas.get(arg0).getAddress_id());
            if (position == address_id) {
                item.iv_quan_select.setImageResource(R.drawable.quan_select_3);
                item.tv_default_address.setTextColor(getResources().getColor(
                        R.color.app_fen));
            } else {
                item.iv_quan_select.setImageResource(R.drawable.quan_select_1);
                item.tv_default_address.setTextColor(getResources().getColor(
                        R.color.grey));
            }
            BLComment blComment = datas.get(arg0);
            StrUtils.SetColorsTxt(BaseContext, item.tv_usr_name, R.color.app_gray, "收货人：", datas.get(arg0).getName());
            StrUtils.SetTxt(item.tv_usr_phone_numb, datas.get(arg0).getMobile());
            StrUtils.SetColorsTxt(BaseContext, item.tv_delivery_address, R.color.app_gray, "详细地址：", blComment.getProvince() + blComment.getCity()
                    + blComment.getCounty()
                    + blComment.getAddress());

            onClickEvent(item, datas, arg0);

            return arg1;
        }

        private void onClickEvent(final AddressItem item,
                                  final List<BLComment> datas2, final int pos) {
            item.ll_select_default.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if (CheckNet(BaseContext)) return;
                    AddressSet(user_Get.getId(), datas2.get(pos)
                            .getAddress_id(), 1);

                    int address_id = Integer.parseInt(datas2.get(pos).getAddress_id());
                    Message msg = Message.obtain();
                    msg.what = address_id;
                    myHandler.sendMessage(msg);
                }
            });

            item.tv_edit.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    PromptManager.SkipResultActivity(BaseActivity, new Intent(
                            BaseActivity, AEditAddress.class).putExtra("data",
                            datas2.get(pos)), 100);

                }
            });

            item.tv_delete.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    ShowCustomDialog("确认删除收货地址？", "取消", "确认", new IDialogResult() {
                        @Override
                        public void RightResult() {
                            if (CheckNet(BaseContext)) return;
                            AddressSet(user_Get.getId(), datas2.get(pos)
                                    .getAddress_id(), 2);
                        }

                        @Override
                        public void LeftResult() {
                        }
                    });


                }
            });
        }

        class AddressItem {
            TextView tv_usr_name, tv_usr_phone_numb, tv_delivery_address,
                    tv_default_address, tv_edit, tv_delete;
            ImageView iv_quan_select;

            LinearLayout ll_select_default;
        }

    }


    Handler myHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            pos = msg.what;
        }

        ;
    };


}
