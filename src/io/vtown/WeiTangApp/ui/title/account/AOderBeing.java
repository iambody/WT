package io.vtown.WeiTangApp.ui.title.account;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.SerializableMap;
import io.vtown.WeiTangApp.bean.bcomment.easy.coupons.BLMyCoupons;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.afragment.ACenterOder;
import io.vtown.WeiTangApp.ui.afragment.AShopPurchase;
import io.vtown.WeiTangApp.ui.comment.im.AChat;
import io.vtown.WeiTangApp.ui.comment.im.AChatLoad;
import io.vtown.WeiTangApp.ui.title.center.mycoupons.AMyCoupons;
import io.vtown.WeiTangApp.ui.title.center.set.AAddressManage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.ui.ui.AMainTab;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-24 下午7:37:49 产生订单的界面
 */
public class AOderBeing extends ATitleBase {

    private LinearLayout oderbeing_out_lay;

    private View oderbeing_nodata_lay;

    /**
     * 提交按钮
     */
    private TextView oderbeing_commint;

    /**
     * 地址view
     */
    private View oderbeing_address;

    private TextView commentview_add_name;
    private TextView commentview_add_address;
    private TextView commentview_add_phone;
    /**
     * 应付
     */
    private TextView oderbeing_yingfu;
    /**
     * 实际付款
     */
    private TextView oderbeing_need;

    /**
     * ls
     */
    private CompleteListView oderbeing_ls;
    /**
     * ls对应的adapter
     */
    private OderAp oderAp;
    /**
     * 卡劵的view
     */
    private LinearLayout oderbeing_coupons_lay;
    /**
     * 卡券的名字
     */
    private TextView oderbeing_coupons_nameprice;
    /**
     * 卖家留言
     */
    private EditText oderbeing_note_ed;
    /**
     * 获取的数据源
     */
    private BComment mBdComment;
    /**
     * 保存收货地址的数据源
     */
    private BLDComment addressBldComment;
    /**
     * 保存优惠券数据源
     */
    private BLDComment coupComment;
    /**
     *
     */
    private String AccountStr;

    /**
     * 没有设置收货地址时的提示
     */
    private TextView tv_no_address_info_title;

    /**
     * 设置了地址时
     */
    private LinearLayout ll_address_info;

    private boolean IsUserCoup = false;

    private BUser user_Get;
    private Float NeddPay = 0f;

    // 显示使用卡券的显示
    int CurrenShowKaQuanType = 0;// 0表示 没有卡券 隐藏状态;;1=>当前显示 不使用卡券
    // ;;2==》当前显示 使用卡券
    private View oderbering_view;
    private TextView RightShowTxt;
    HashMap<String, String> map = null;
    private int goGoodBus = -1;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_oderbeing);
        SetTitleHttpDataLisenter(this);
        user_Get = Spuit.User_Get(BaseContext);
        EventBus.getDefault().register(this, "ReciverInf", BMessage.class);
        IBundle();
        IBase();

        if (null == map) {
            IData(user_Get.getId(), AccountStr);
        } else {
            IDirectBuy();

        }

    }

    private void IDirectBuy() {
        IDataView(oderbeing_out_lay, oderbeing_nodata_lay, NOVIEW_INITIALIZE);
        PromptManager.showtextLoading(BaseContext,
                getResources().getString(R.string.loading));
        FBGetHttpData(map, Constants.Direct_Buy, Method.POST, 0, LOAD_INITIALIZE);
    }

    private void IBundle() {


//        if (getIntent().getExtras() != null
//                && getIntent().getExtras().containsKey("accountstr")) {
//            AccountStr = getIntent().getStringExtra("accountstr");
//        }else if (getIntent().getExtras() != null) {
//            Bundle bundle = getIntent().getExtras();
//            SerializableMap serializableMap = (SerializableMap) bundle
//                    .get("DirectBuyInfo");
//            map = serializableMap.getMap();
//        }


        if(getIntent().getExtras() != null){
            if(getIntent().getExtras().containsKey("accountstr")){
                AccountStr = getIntent().getStringExtra("accountstr");
            }else{
                Bundle bundle = getIntent().getExtras();
                SerializableMap serializableMap = (SerializableMap) bundle
                        .get("DirectBuyInfo");
                map = serializableMap.getMap();
                goGoodBus = 1;
            }
        }
    }

    /**
     * 获取数据
     *
     * @param member
     * @param cid
     */
    private void IData(String member, String cid) {// member cid JieSuan_Ui_Url

        IDataView(oderbeing_out_lay, oderbeing_nodata_lay, NOVIEW_INITIALIZE);
        PromptManager.showtextLoading(BaseContext,
                getResources().getString(R.string.loading));

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", member);
        map.put("cid", cid);
        FBGetHttpData(map, Constants.JieSuan_Ui_Url, Method.GET, 0,
                LOAD_INITIALIZE);
    }

    /**
     * 生成订单
     */
    private void OderBeing(String member_id, BLDComment Address,
                           String goods_id, String cid, String order_note, String coupons_id) {//
        SetTitleHttpDataLisenter(this);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", member_id);
        map.put("mobile", Address.getMobile());
        map.put("username", Address.getName());
        map.put("province", Address.getProvince());
        map.put("city", Address.getCity());
        map.put("area", Address.getCounty());
        map.put("street_address", Address.getCounty());
        map.put("address", Address.getAddress());
        map.put("goods_id", goods_id);
        map.put("cid", cid);
        map.put("order_note", order_note);
        map.put("coupons_id", coupons_id);
        FBGetHttpData(map, Constants.OderBeing, Method.POST, 1,
                LOAD_LOADMOREING);
    }

    private void IBase() {
        oderbeing_note_ed = (EditText) findViewById(R.id.oderbeing_note_ed);
        oderbering_view = findViewById(R.id.oderbering_view);

        oderbeing_out_lay = (LinearLayout) findViewById(R.id.oderbeing_out_lay);
        oderbeing_nodata_lay = findViewById(R.id.oderbeing_nodata_lay);
        oderbeing_nodata_lay.setOnClickListener(this);

        oderbeing_ls = (CompleteListView) findViewById(R.id.oderbeing_ls);
        oderbeing_commint = (TextView) findViewById(R.id.oderbeing_commint);
        oderbeing_address = findViewById(R.id.oderbeing_address);
        commentview_add_name = (TextView) oderbeing_address
                .findViewById(R.id.commentview_add_name);
        commentview_add_address = (TextView) oderbeing_address
                .findViewById(R.id.commentview_add_address);
        commentview_add_phone = (TextView) oderbeing_address
                .findViewById(R.id.commentview_add_phone);

        tv_no_address_info_title = (TextView) oderbeing_address
                .findViewById(R.id.tv_no_address_info_title);

        ll_address_info = (LinearLayout) oderbeing_address
                .findViewById(R.id.ll_address_info);
        oderbeing_coupons_lay = (LinearLayout) findViewById(R.id.oderbeing_coupons_lay);
        oderbeing_coupons_nameprice = (TextView) findViewById(R.id.oderbeing_coupons_nameprice);
        oderbeing_yingfu = (TextView) findViewById(R.id.oderbeing_yingfu);
        oderbeing_need = (TextView) findViewById(R.id.oderbeing_need);
        findViewById(R.id.commentview_add_iv).setVisibility(View.VISIBLE);
        oderAp = new OderAp(R.layout.item_oderbeing_out);
        oderbeing_ls.setAdapter(oderAp);
        ShowErrorCanLoad(getResources().getString(R.string.error_null_noda));
        oderbeing_address.setOnClickListener(this);
        oderbeing_commint.setOnClickListener(this);
        oderbeing_coupons_lay.setOnClickListener(this);

    }

    @Override
    protected void InitTile() {
        SetTitleTxt(getResources().getString(R.string.daifu_confirm_order));
        // 添加不适用卡券
        RightShowTxt = (TextView) oderbering_view.findViewById(R.id.right_txt);
        RightShowTxt.setOnClickListener(this);
        RightShowTxt.setTextColor(getResources().getColor(R.color.white));
        // ssssddddff

    }

    // 0表示 没有卡券 隐藏状态;;1=>当前显示 不使用卡券 ;;2==》当前显示 使用卡券
    private void ShowSelect() {
        // 还需要改变钱！！！！！！！！！！！！
        switch (CurrenShowKaQuanType) {
            case 0:// 没有卡券 隐藏状态
                RightShowTxt.setVisibility(View.GONE);
                // right_txt.setOnClickListener(this);
                break;
            case 1:// 当前显示 不使用卡券
                // SetRightText("不用卡券");
                RightShowTxt.setText("不用卡券");
                RightShowTxt.setVisibility(View.VISIBLE);

                break;
            case 2:// 当前显示 使用卡券
                // SetRightText("使用卡券");
                RightShowTxt.setText("使用卡券");
                RightShowTxt.setVisibility(View.VISIBLE);

                break;
            default:
                break;
        }

    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        switch (Data.getHttpResultTage()) {
            case 0:// 获取界面的数据=》
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    PromptManager.ShowCustomToast(BaseContext, Msg);
//                    BaseActivity.finish();
                    IDataView(oderbeing_out_lay, oderbeing_nodata_lay, NOVIEW_ERROR);
                    return;
                }
                IDataView(oderbeing_out_lay, oderbeing_nodata_lay, NOVIEW_RIGHT);
                try {
                    mBdComment = JSON.parseObject(Data.getHttpResultStr(),
                            BComment.class);
                } catch (Exception e) {
                    String aa = e.toString();
                    LogUtils.i("");
                }
                IsUserCoup = !(StrUtils.isEmpty(mBdComment.getIs_used_coupons()) || mBdComment
                        .getIs_used_coupons().equals("N"));
                IResultData(mBdComment);

                break;

            case 1:// =>提交数据生成订单=》
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    PromptManager.ShowCustomToast(BaseContext, Msg);
                    return;
                }
                SenMessage(BMessage.Shop_Frash);//通知购物车刷新

                BDComment data = new BDComment();
                data = JSON.parseObject(Data.getHttpResultStr(), BDComment.class);
                // 如果支付的是
                if (StrUtils.toFloat(data.getMoney_paid()) <= 0f) {
                    if (mBdComment.getList().get(0).getStore_list().get(0)
                            .getChannel().equals("CG")) {// 采购=》需要跳转到采购列表页面
                        PromptManager.SkipActivity(BaseActivity, new Intent(
                                BaseActivity, AShopPurchase.class));
                    } else {// 普通需要跳cneter界面
                        PromptManager.SkipActivity(BaseActivity, new Intent(
                                BaseActivity, ACenterOder.class));
                    }
                    BaseActivity.finish();
                } else {

                    // BDComment data = new BDComment();
                    // data = JSON.parseObject(Data.getHttpResultStr(),
                    // BDComment.class);
                    PromptManager.SkipActivity(BaseActivity, new Intent(
                            BaseContext, ACashierDesk.class).putExtra(
                            "addOrderInfo", data));
                    BaseActivity.finish();
                }

                break;
        }

    }


   public void title_left_bt(View v){
       FrashBus();
    }

    private void FrashBus(){
        if(1 == goGoodBus){
            EventBus.getDefault().post(new BMessage(BMessage.Shop_Frash));
        }
        finish();
        overridePendingTransition(R.anim.push_rigth_in, R.anim.push_rigth_out);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            FrashBus();
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 处理返回结果
     */
    private void IResultData(BComment mBdComment) {// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (IsUserCoup) {
            oderbeing_coupons_lay.setVisibility(View.VISIBLE);
        } else {
            oderbeing_coupons_lay.setVisibility(View.INVISIBLE);
        }
        if (!StrUtils.isEmpty(mBdComment.getCouponsNum())) {
            StrUtils.SetTxt(oderbeing_coupons_nameprice, mBdComment.getCouponsNum() + "张可用卡券");
        }
        oderAp.FrashData(mBdComment.getList());
        // 判断地址数据是否为空
        addressBldComment = mBdComment.getAddress();
        // 判断优惠券是否为空
//        coupComment = mBdComment.getCoupons();
//        coupComment = new BLDComment();
        RefreshResultView(addressBldComment);
        // 判断卡券数据是否为空
//        if (null == coupComment
//                || (StrUtils.isEmpty(coupComment.getCoupons_name()) && StrUtils
//                .isEmpty(coupComment.getId()))) {
//            if (IsUserCoup)
//                oderbeing_coupons_lay.setVisibility(View.INVISIBLE);
//            ;
//            CurrenShowKaQuanType = 0;
        ShowSelect();
//        }// 卡券数据不为空
//        else {
//            StrUtils.SetColorsTxt(
//                    BaseContext,
//                    oderbeing_coupons_nameprice,
//                    R.color.red,
//                    getResources().getString(R.string.lijian),
//                    "￥"
//                            + StrUtils.SetTextForMony(coupComment
//                            .getCoupons_money()));
//            CurrenShowKaQuanType = 1;
//            ShowSelect();
//        }

        StrUtils.SetTxt(
                oderbeing_yingfu,
                "共:"
                        + StrUtils.SetTextForMony(mBdComment
                        .getOrder_total_price()) + "元");
        StrUtils.SetColorsTxt(BaseContext, oderbeing_need, R.color.red,
                getResources().getString(R.string.needpay),
                "￥" + StrUtils.SetTextForMony(mBdComment.getOrder_total_price()));
    }

    @Override
    protected void DataError(String error, int LoadTyp) {
        PromptManager.ShowMyToast(BaseContext, error);
        if (LoadTyp == LOAD_INITIALIZE) {// 获取界面数据失败

            IDataView(oderbeing_out_lay, oderbeing_nodata_lay, NOVIEW_ERROR);

        }
        if (LoadTyp == LOAD_LOADMOREING) {// 生成订单失败

        }

    }

    /**
     * 外层的adapter
     */
    class OderAp extends BaseAdapter {

        /**
         * 填充器
         */
        private LayoutInflater inflater;
        /**
         * 资源id
         */
        private int ResourceId;
        /**
         * 数据
         */
        private List<BLComment> datas = new ArrayList<BLComment>();

        public OderAp(int resourceId) {
            super();

            ResourceId = resourceId;
            this.inflater = LayoutInflater.from(BaseContext);
        }

        /**
         * 数据变化进行刷新
         */
        public void FrashData(List<BLComment> d) {
            this.datas = d;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return datas.size();

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

            OderOutItem oderOutItem = null;

            if (null == arg1) {
                oderOutItem = new OderOutItem();
                arg1 = inflater.inflate(ResourceId, null);

                oderOutItem.item_oderbeing_out_goodnumbers_up = ViewHolder.get(
                        arg1, R.id.item_oderbeing_out_goodnumbers_up);
                oderOutItem.item_oderbeing_out_shoptxt = ViewHolder.get(arg1,
                        R.id.item_oderbeing_out_shoptxt);
                oderOutItem.item_oderbeing_out_yunfei = ViewHolder.get(arg1,
                        R.id.item_oderbeing_out_yunfei);
                oderOutItem.item_oderbeing_out_number_down = ViewHolder.get(
                        arg1, R.id.item_oderbeing_out_number_down);
                oderOutItem.item_oderbeing_out_mony = ViewHolder.get(arg1,
                        R.id.item_oderbeing_out_mony);
                oderOutItem.item_oderbeing_connect = ViewHolder.get(arg1,
                        R.id.item_oderbeing_connect);

                oderOutItem.item_oderbeing_iv1 = ViewHolder.get(arg1,
                        R.id.item_oderbeing_iv1);
                oderOutItem.item_oderbeing_iv2 = ViewHolder.get(arg1,
                        R.id.item_oderbeing_iv2);
                oderOutItem.item_oderbeing_iv3 = ViewHolder.get(arg1,
                        R.id.item_oderbeing_iv3);
                oderOutItem.item_oderbeing_lay = (LinearLayout) arg1
                        .findViewById(R.id.item_oderbeing_lay);
                oderOutItem.item_oderbeing_more_lay = (LinearLayout) arg1
                        .findViewById(R.id.item_oderbeing_more_lay);

                arg1.setTag(oderOutItem);
            } else {
                oderOutItem = (OderOutItem) arg1.getTag();
            }
            final BLComment iBlComment = datas.get(arg0);
            List<ImageView> imaViews = new ArrayList<ImageView>();
            imaViews.add(oderOutItem.item_oderbeing_iv1);
            imaViews.add(oderOutItem.item_oderbeing_iv2);
            imaViews.add(oderOutItem.item_oderbeing_iv3);
            // 需要进行图片的展示！！！！！！！！！！！！
            // for (int i = 0; i < GetGoodsIvs(datas.get(arg0)).size(); i++) {
            for (int i = 0; i < GetGoodsIvs(iBlComment).size(); i++) {
                if (i >= 3) {
                    break;
                }
                ImageLoaderUtil.Load(GetGoodsIvs(iBlComment).get(i),
                        imaViews.get(i), R.drawable.error_iv2);
                imaViews.get(i).setVisibility(View.VISIBLE);
            }
            StrUtils.SetTxt(oderOutItem.item_oderbeing_out_shoptxt,
                    iBlComment.getSeller_name());
//            StrUtils.SetTxt(oderOutItem.item_oderbeing_out_yunfei, "￥"
//                    + StrUtils.SetTextForMony(iBlComment.getPostage()));
            StrUtils.SetColorsTxt(BaseContext, oderOutItem.item_oderbeing_out_yunfei, R.color.gray, R.color.app_fen, "运费:", "￥" + StrUtils.SetTextForMony(iBlComment.getPostage()));
            StrUtils.SetTxt(oderOutItem.item_oderbeing_out_mony,
                    iBlComment.getAll_money());
            StrUtils.SetColorsTxt(BaseContext,
                    oderOutItem.item_oderbeing_out_mony, R.color.app_fen,
                    "合计:",
                    "￥" + StrUtils.SetTextForMony(iBlComment.getAll_money()));
            // 共计件商品
            StrUtils.SetTxt(oderOutItem.item_oderbeing_out_number_down, String
                    .format("共%s件商品", GoodNumberCount(iBlComment, 1) + ""));
            //
            StrUtils.SetTxt(oderOutItem.item_oderbeing_out_goodnumbers_up,
                    String.format("共%s件", GoodNumberCount(iBlComment, 2) + ""));
            // 联系客服
            oderOutItem.item_oderbeing_connect
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            if (!StrUtils.isEmpty(iBlComment.getSeller_id()))
                                PromptManager
                                        .SkipActivity(
                                                BaseActivity,
                                                new Intent(BaseActivity,
                                                        AChatLoad.class)
                                                        .putExtra(
                                                                AChatLoad.Tage_TageId,
                                                                iBlComment
                                                                        .getSeller_id())
                                                        .putExtra(
                                                                AChatLoad.Tage_Name,
                                                                iBlComment
                                                                        .getSeller_name())
                                                        .putExtra(
                                                                AChatLoad.Tage_Iv,
                                                                iBlComment
                                                                        .getCover()));
                        }
                    });
            oderOutItem.item_oderbeing_lay
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            PromptManager.SkipActivity(BaseActivity,
                                    new Intent(BaseActivity,
                                            AGoodsCheckList.class).putExtra(
                                            "checklsbean", iBlComment));
                        }
                    });
            // 跳转到地址
            oderOutItem.item_oderbeing_more_lay
                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            PromptManager.SkipActivity(BaseActivity,
                                    new Intent(BaseActivity,
                                            AGoodsCheckList.class).putExtra(
                                            "checklsbean", iBlComment));
                        }
                    });
            return arg1;
        }

        class OderOutItem {
            TextView item_oderbeing_connect;
            TextView item_oderbeing_out_goodnumbers_up;
            TextView item_oderbeing_out_shoptxt;
            TextView item_oderbeing_out_yunfei;

            TextView item_oderbeing_out_number_down;
            TextView item_oderbeing_out_mony;
            LinearLayout item_oderbeing_lay;// 图片所在的线性布局
            ImageView item_oderbeing_iv1, item_oderbeing_iv2,
                    item_oderbeing_iv3;
            LinearLayout item_oderbeing_more_lay;
        }
    }

    /**
     * 计算下 共计件商品Type=1标识全部商品；；Type=2标识全部分类
     */
    private int GoodNumberCount(BLComment dtaaBlComment, int Type) {
        int Count = 0;
        List<String> GoodsId = new ArrayList<String>();
        List<String> SortsId = new ArrayList<String>();
        List<BLDComment> datas = dtaaBlComment.getStore_list();
        for (int i = 0; i < datas.size(); i++) {
            // 商品id列表
            if (!GoodsId.contains(datas.get(i).getGoods_id()))
                GoodsId.add(datas.get(i).getGoods_id());
            // 规格id列表

            if (!SortsId.contains(datas.get(i).getCid()))
                SortsId.add(datas.get(i).getCid());

            Count += StrUtils.toInt(datas.get(i).getGoods_num());
            // Count += Integer.valueOf(datas.get(i).getGoods_num());
        }
        return 1 == Type ? Count : SortsId.size();
        // return GoodsId.size();
    }

    /**
     * 计算下 同一店铺下边的不同规格的商品的封面图
     *
     * @return
     */
    private List<String> GetGoodsIvs(BLComment dtaaBlComment) {
        List<String> IVsList = new ArrayList<String>();
        List<String> GoodsId = new ArrayList<String>();
        List<String> SortsId = new ArrayList<String>();
        List<BLDComment> datas = dtaaBlComment.getStore_list();
        for (int i = 0; i < datas.size(); i++) {
            // 商品的id
            if (!GoodsId.contains(datas.get(i).getGoods_id())) {
                GoodsId.add(datas.get(i).getGoods_id());
                // IVsList.add(datas.get(i).getCover());
            }
            // 规格的id
            if (!SortsId.contains(datas.get(i).getCid())) {
                SortsId.add(datas.get(i).getCid());
                IVsList.add(datas.get(i).getCover());
            }
        }
        return IVsList;
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
            case R.id.oderbeing_address:// 地址
                Intent intent = new Intent(BaseContext, AAddressManage.class);
                intent.putExtra("NeedFinish", true);
                PromptManager.SkipResultActivity(BaseActivity, intent, 100);

                break;

            case R.id.oderbeing_commint:// 点击提交
                CommintOder();

                break;
            case R.id.oderbeing_coupons_lay:
                PromptManager.SkipResultActivity(BaseActivity, new Intent(
                        BaseActivity, AMyCoupons.class).putExtra(
                        AMyCoupons.Tage_key, true), 201);

                // PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                // AMyCoupons.class).putExtra(AMyCoupons.Tage_key, true));

                break;
            case R.id.oderbeing_nodata_lay:// 获取界面数据失败后点击重新获取
                IData(user_Get.getId(), AccountStr);
                break;
            case R.id.right_txt://
                if (CurrenShowKaQuanType == 1) {// 当前显示的是不使用需要===》使用
                    CurrenShowKaQuanType = 2;
                    oderbeing_coupons_lay.setVisibility(View.GONE);
                    ShowSelect();
                    RCalculate1(false);
                    return;
                }
                if (CurrenShowKaQuanType == 2) {// 当前显示的是使用需要===》不使用
                    CurrenShowKaQuanType = 1;
                    oderbeing_coupons_lay.setVisibility(View.VISIBLE);
                    ShowSelect();
                    RCalculate1(true);
                    return;
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 201 && resultCode == 202) {// 优惠券返回数据
            BLMyCoupons coupresults = (BLMyCoupons) data
                    .getSerializableExtra("coupresult");
            coupComment = new BLDComment(coupresults.getCoupons_id(),
                    coupresults.getCoupons_name(),
                    coupresults.getCoupons_money());
            // 选择优惠券后进行数据重绘
            RCalculate();

            CurrenShowKaQuanType = 1;
            ShowSelect();
        }

        if (100 == requestCode && RESULT_OK == resultCode) {// 地址=》返回
            BLComment AddressInfo = (BLComment) data
                    .getSerializableExtra("AddressInfo");
            if (AddressInfo != null) {
                if (null == addressBldComment) {
                    addressBldComment = new BLDComment();
                }

                String name = AddressInfo.getName();
                addressBldComment.setName(name);

                System.out.println(name);
                String mobile = AddressInfo.getMobile();
                addressBldComment.setMobile(mobile);
                addressBldComment.setProvince(AddressInfo.getProvince());
                addressBldComment.setCity(AddressInfo.getCity());
                addressBldComment.setCounty(AddressInfo.getCounty());
                addressBldComment.setAddress(AddressInfo.getAddress());
                RefreshResultView(addressBldComment);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 刷新从地址管理返回的地址数据
     *
     * @param addressBldComment2
     */
    private void RefreshResultView(BLDComment addressBldComment2) {

        if (null == addressBldComment2
                || (StrUtils.isEmpty(addressBldComment2.getMember_id()) && StrUtils
                .isEmpty(addressBldComment2.getCounty()))) {// 地址数据为空
            tv_no_address_info_title.setVisibility(View.VISIBLE);
            ll_address_info.setVisibility(View.GONE);

        } else {// 存在默认地址
            // addressBldComment = mBdComment.getAddress();
            tv_no_address_info_title.setVisibility(View.GONE);
            ll_address_info.setVisibility(View.VISIBLE);
            StrUtils.SetTxt(commentview_add_name, addressBldComment2.getName());
            StrUtils.SetTxt(
                    commentview_add_address,
                    addressBldComment2.getProvince()
                            + addressBldComment2.getCity()
                            + addressBldComment2.getCounty()
                            + addressBldComment2.getAddress());
            StrUtils.SetTxt(commentview_add_phone,
                    addressBldComment2.getMobile());
        }

    }

    /**
     * 重新计算优惠券金额和将要支付金额
     */
    private void RCalculate() {
        // 重新刷新优惠券的Ui展示
        StrUtils.SetColorsTxt(BaseContext, oderbeing_coupons_nameprice,
                R.color.red, getResources().getString(R.string.lijian),
                "￥" + StrUtils.SetTextForMony(coupComment.getCoupons_money()));
        // 开始重新计算支付金额
        // StrUtils.SetTxt(oderbeing_yingfu,
        // mBdComment.getOrder_total_price());//应该支付
        Float Number = StrUtils.toFloat(StrUtils.SetTextForMony(mBdComment
                .getOrder_total_price()))
                - StrUtils.toFloat(StrUtils.SetTextForMony(coupComment
                .getCoupons_money()));
        NeddPay = Number;
        if (NeddPay == 0f) {
            NeddPay = -1f;
        }
        StrUtils.SetColorsTxt(BaseContext, oderbeing_need, R.color.red,
                getResources().getString(R.string.needpay), "￥"
                        + (Number < 0 ? 0 : Number) + "");

    }

    /**
     * 点击是否使用卡券
     */
    private void RCalculate1(boolean IsNeed) {
        // 重新刷新优惠券的Ui展示
        StrUtils.SetColorsTxt(BaseContext, oderbeing_coupons_nameprice,
                R.color.red, getResources().getString(R.string.lijian),
                "￥" + StrUtils.SetTextForMony(coupComment.getCoupons_money()));
        // 开始重新计算支付金额
        // StrUtils.SetTxt(oderbeing_yingfu,
        // mBdComment.getOrder_total_price());//应该支付
        Float Number = 0f;
        if (IsNeed) {
            Number = StrUtils.toFloat(StrUtils.SetTextForMony(mBdComment
                    .getOrder_total_price()))
                    - StrUtils.toFloat(StrUtils.SetTextForMony(coupComment
                    .getCoupons_money()));
        } else {
            Number = StrUtils.toFloat(StrUtils.SetTextForMony(mBdComment
                    .getOrder_total_price()));

        }
        NeddPay = Number;
        if (NeddPay == 0f) {
            NeddPay = -1f;
        }
        StrUtils.SetColorsTxt(BaseContext, oderbeing_need, R.color.red,
                getResources().getString(R.string.needpay), "￥"
                        + StrUtils.To2Float((Number < 0 ? 0 : Number)) + "");

    }

    /**
     * 获取商品ID的串 1=Type标识返回商品 的串;;;;;;;;;2=Type标识返回规格Cid的串
     */
    private String GetGoodsIdStr(BComment mBdComm, int Type) {
        List<String> Goodsls = new ArrayList<String>();
        List<String> Cidls = new ArrayList<String>();
        List<BLComment> mbdList = mBdComm.getList();
        for (int i = 0; i < mbdList.size(); i++) {
            for (int j = 0; j < mbdList.get(i).getStore_list().size(); j++) {
                // 保存商品id的字符串
                if (!Goodsls.contains(mbdList.get(i).getStore_list().get(j)
                        .getGoods_id()))
                    Goodsls.add(mbdList.get(i).getStore_list().get(j)
                            .getGoods_id());
                // 保存Cid的字符串
                Cidls.add(mbdList.get(i).getStore_list().get(j).getCid());

            }
        }
        // 注意不能用字符串来进行contains判断 "12,13,14"也是包含2,3,4的
        String StrGoods = "";
        String StrCids = "";
        // 商品的串
        for (int i = 0; i < Goodsls.size(); i++) {
            StrGoods = StrGoods + Goodsls.get(i)
                    + (i != (Goodsls.size() - 1) ? "," : "");
        }
        // 规格Cid的串
        for (int j = 0; j < Cidls.size(); j++) {
            StrCids = StrCids + Cidls.get(j)
                    + (j != (Cidls.size() - 1) ? "," : "");
        }
        return (1 == Type) ? StrGoods : StrCids;
    }

    /**
     * 提交按钮进行生成订单操作
     */
    private void CommintOder() {

        if (null == addressBldComment
                || StrUtils.isEmpty1(commentview_add_name.getText().toString()
                .trim())
                || StrUtils.isEmpty1(commentview_add_address.getText()
                .toString().trim())
                || StrUtils.isEmpty1(commentview_add_phone.getText().toString()
                .trim())) {
            PromptManager.ShowCustomToast(BaseContext, getResources().getString(R.string.querendizhi));
            return;
        }

        String GoodsStr = GetGoodsIdStr(mBdComment, 1);
        String CidsStr = GetGoodsIdStr(mBdComment, 2);
        PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.loading));

        // CurrenShowKaQuanType ========> 0表示 没有卡券 隐藏状态;;1=>当前显示 不使用卡券
        // ;;2==》当前显示 使用卡券
        if (CurrenShowKaQuanType == 0 || CurrenShowKaQuanType == 2) {// 手动的不用卡券
            OderBeing(user_Get.getId(), addressBldComment, GoodsStr, CidsStr,
                    StrUtils.NullToStr3(oderbeing_note_ed.getText().toString().trim()), "");
        } else {// 使用卡券
            OderBeing(user_Get.getId(), addressBldComment, GoodsStr, CidsStr,
                    StrUtils.NullToStr3(oderbeing_note_ed.getText().toString().trim()), coupComment == null ? "" : coupComment.getCoupons_id());
        }// 测试先把卡券置为空//
        // coupComment.getCoupons_id());
        // PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
        // ACashierDesk.class));

    }

    @Override
    protected void InItBundle(Bundle bundle) {
    }

    @Override
    protected void SaveBundle(Bundle bundle) {
    }

    public void ReciverInf(BMessage message) {
        switch (message.getMessageType()) {
            case BMessage.Tage_Kill_Self:
                BaseActivity.finish();
                break;

            default:
                break;
        }

    }
}
