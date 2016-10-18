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
import java.util.Map;

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
     * 获取数据失败显示的布局
     */
    private View center_address_manage_nodata_lay;


    private boolean isGetList = false;

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


        isGetList = true;
        String id = user_Get.getId();

        HashMap<String, String> map = new HashMap<String, String>();
        //map.put("address_id", "2");
        map.put("member_id", id);
        FBGetHttpData(map, Constants.SET_ADDRESS_MANAGE, Method.GET, 0,
                LOAD_INITIALIZE);
    }

    private void AddressSet(String member_id, String address_id, int LoadType) {
        isGetList = false;
        HashMap<String, String> map = new HashMap<String, String>();
        PromptManager.showLoading(BaseContext);
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
            PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.xlistview_header_hint_loading));
        }
        try {
            BD = JSON.parseObject(center_Set_Address, BDComment.class);
        } catch (Exception e) {
            return;
        }
        addressAdapter.FrashData(BD.getList());
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
        center_address_manage_nodata_lay = findViewById(R.id.center_address_manage_nodata_lay);
        lv_address_list = (ListView) findViewById(R.id.lv_address_list);
        btn_add_new_consignee_address = (Button) findViewById(R.id.btn_add_new_consignee_address);
        btn_add_new_consignee_address.setOnClickListener(this);
        center_address_manage_nodata_lay.setOnClickListener(this);
        IList();
    }

    private void IList() {
        addressAdapter = new AddressAdapter(R.layout.item_address_manage);
        lv_address_list.setAdapter(addressAdapter);


            lv_address_list.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    if (needFinish) {
                    BLComment bl = (BLComment) addressAdapter.getItem(arg2);
                    Intent intent = new Intent();
                    intent.putExtra("AddressInfo", bl);
                    setResult(RESULT_OK, intent);
                    AAddressManage.this.finish();
                    }else{

                        PromptManager.SkipResultActivity(BaseActivity, new Intent(
                                BaseActivity, AEditAddress.class).putExtra("data",
                                BD.getList().get(arg2)), 100);
                    }
                }
            });

        if(!needFinish){
            lv_address_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {


                    ShowCustomDialog("确认删除收货地址？", "取消", "确认", new IDialogResult() {
                        @Override
                        public void RightResult() {
                            if (CheckNet(BaseContext)) return;
                            AddressSet(user_Get.getId(), BD.getList().get(position)
                                    .getAddress_id(), 2);

                        }

                        @Override
                        public void LeftResult() {
                        }
                    });
                    return true;
                }
            });
        }



    }

    @Override
    protected void InitTile() {
        SetTitleTxt(getString(R.string.address_manage));
        SetRightIv(R.drawable.ic_jiahao_add);
        right_iv.setOnClickListener(this);
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

        switch (Data.getHttpResultTage()) {

            case 0:// 获取列表
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    if (LOAD_INITIALIZE == Data.getHttpLoadType()) {
                        CacheUtil.Center_Set_Address_Save(BaseContext, "");
                        center_address_manage_nodata_lay.setVisibility(View.VISIBLE);
                        lv_address_list.setVisibility(View.GONE);
                        ShowErrorCanLoad(getResources().getString(R.string.error_null_address));
                        center_address_manage_nodata_lay.setClickable(false);
                        addressAdapter.FrashData(new ArrayList<BLComment>());
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
                center_address_manage_nodata_lay.setVisibility(View.GONE);
                lv_address_list.setVisibility(View.VISIBLE);
                addressAdapter.FrashData(BD.getList());
                break;
            case 1:// 设置默认地址
                PromptManager.ShowMyToast(BaseContext, "设置成功");
                // IData();
                addressAdapter.RefreshPosition(pos);

                break;

            case 2:// 删除地址
                PromptManager.ShowMyToast(BaseContext, "删除成功");
               // addressAdapter.RefreshPosition(pos);
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
        if (LOAD_INITIALIZE == LoadTyp && isGetList) {
            ShowErrorCanLoad(getResources().getString(R.string.error_null_noda));
            center_address_manage_nodata_lay.setVisibility(View.VISIBLE);
            lv_address_list.setVisibility(View.GONE);
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
            case R.id.right_iv:
                PromptManager.SkipResultActivity(BaseActivity, new Intent(
                        BaseActivity, AAddConsigneeAddress.class), 100);
                break;

            case R.id.center_address_manage_nodata_lay://重新加载数据

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

        //private List<Boolean> mBooleans = new ArrayList<Boolean>();

        private int position = 0;

        //用于局部刷新使用
        public Map<Integer, AddressItem> map = new HashMap<>();

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
        public void FrashData(List<BLComment> dass) {
            //           this.position = postion;
            this.datas = dass;
//            for (BLComment blComment : dass) {
//                if (postion == dass.indexOf(blComment)) {
//                    mBooleans.add(true);
//                }
//                mBooleans.add(false);
//            }
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

        //局部刷新的代码


        public void RefreshPosition(int position) {//
            if (datas.size() > 0) {

                datas.remove(position);
                FrashData(datas);
            }else{
                IData();
            }
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



                item.iv_address_manager_arrow = ViewHolder.get(arg1,R.id.iv_address_manager_arrow);

                arg1.setTag(item);

            } else {
                item = (AddressItem) arg1.getTag();
            }
            if(needFinish){
                item.iv_address_manager_arrow.setVisibility(View.GONE);
            }else{
                item.iv_address_manager_arrow.setVisibility(View.VISIBLE);
            }
//            map.put(arg0,item);
//
//            int address_id = Integer.parseInt(datas.get(arg0).getAddress_id());
//            if (position == address_id) {
//                item.iv_quan_select.setImageResource(R.drawable.quan_select_3);
//                item.tv_default_address.setTextColor(getResources().getColor(
//                        R.color.app_fen));
//            } else {
//                item.iv_quan_select.setImageResource(R.drawable.quan_select_1);
//                item.tv_default_address.setTextColor(getResources().getColor(
//                        R.color.grey));
//            }
            BLComment blComment = datas.get(arg0);
//            StrUtils.SetColorsTxt(BaseContext, item.tv_usr_name, R.color.app_gray, "收货人：", datas.get(arg0).getName());
//
//            StrUtils.SetColorsTxt(BaseContext, item.tv_delivery_address, R.color.app_gray, "详细地址：", blComment.getProvince() + blComment.getCity()
//                    + blComment.getCounty()
//                    + blComment.getAddress());

            StrUtils.SetTxt(item.tv_usr_name,datas.get(arg0).getName());
            StrUtils.SetTxt(item.tv_usr_phone_numb, datas.get(arg0).getMobile());
            StrUtils.SetTxt(item.tv_delivery_address,blComment.getProvince() + blComment.getCity()
                    + blComment.getCounty()
                    + blComment.getAddress());



            return arg1;
        }



        class AddressItem {
            TextView tv_usr_name, tv_usr_phone_numb, tv_delivery_address;
            ImageView iv_address_manager_arrow;


        }

    }


//    Handler myHandler = new Handler() {
//        public void handleMessage(android.os.Message msg) {
//            pos = msg.what;
//        }
//
//        ;
//    };


}
