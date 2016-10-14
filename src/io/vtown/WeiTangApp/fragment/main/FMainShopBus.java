package io.vtown.WeiTangApp.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.shopbus.BLShopBusIn;
import io.vtown.WeiTangApp.bean.bcomment.easy.shopbus.BLShopBusOut;
import io.vtown.WeiTangApp.bean.bcomment.easy.shopbus.BShopBus;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.ViewUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.AddAndSubView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.comment.view.custom.RefreshLayout;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.comment.view.pop.PShopBus;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.ui.title.ABrandDetail;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.account.AOderBeing;
import io.vtown.WeiTangApp.ui.ui.ANewHome;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;

/**
 * Created by datutu on 2016/9/18.
 */
public class FMainShopBus extends FBase implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private RefreshLayout fragment_shopbus_refrash;

    private TextView maintab_shopbus_left_txt;
    private ImageView maintab_shopbus_right_iv;


    //外层包裹布局
    private LinearLayout maintab_shopbus_show_lay;
    //
    private ListView maintab_shopbus_ls;
    //下边结算布局
    private LinearLayout maintab_shopbus_down_lay;
    //下边勾选
    private ImageView maintab_sopbus_bottom_select_iv;
    //商品总数量，商品总价钱
    private TextView maintab_sopbus_bottom_sum_mony, maintab_sopbus_bottom_sum_mumber;
    //结算
    private TextView maintab_sopbus_bottom_jiesuan;
    //错误的view
    private View maintab_shopbus_nodata_lay;
    //右上角完成
    private TextView maintab_shopbus_right_txt;
    private MainShopBusAp busAdapter;
//数据**********************************

    //用户信息

    private BUser MyUser;

    // 用户的type1标识显示零售；；； 2标识显示采购

    private int TypeShow = -1;

    //是否是普通

    private boolean IsPu;
    private int AllNumber = 0;

    private boolean NoGood = false;

    // 标记购物车是否全选

    private boolean IsAllSelectIv = false;

    // 标记是否删除

    private boolean IsJeiSuan = true;

    /**
     * 当前的列表筛选分类
     */
    private String Channel = "";

    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_main_shopbus, null);
        MyUser = Spuit.User_Get(BaseContext);
        EventBus.getDefault().register(this, "ReciverBusMsg", BMessage.class);
        InItBaseView();
        SetTitleHttpDataLisenter(this);
        CachShopBus();
    }

    private void CachShopBus() {
        String ShoobuBusStr = Spuit.ShopBus_Get(BaseContext);
        if (StrUtils.isEmpty(ShoobuBusStr)) {//没有缓存
            IData(INITIALIZE);
        } else {//存在缓存
            SetShopView(ShoobuBusStr, LOADHind);
            IData(LOADHind);
        }
    }

    private void InItBaseView() {
        maintab_shopbus_right_txt= (TextView) BaseView.findViewById(R.id.maintab_shopbus_right_txt);
        maintab_shopbus_right_txt.setOnClickListener(this);

        neterrorview = io.vtown.WeiTangApp.comment.util.ViewHolder.get(BaseView, R.id.fragment_main_shopbus_neterrorview);
        neterrorview.setOnClickListener(this);
        CheckNet();
        fragment_shopbus_refrash = (RefreshLayout) BaseView.findViewById(R.id.fragment_shopbus_refrash);
        fragment_shopbus_refrash.setOnRefreshListener(this);
        fragment_shopbus_refrash.setCanLoadMore(false);
        fragment_shopbus_refrash.setColorSchemeResources(R.color.app_fen, R.color.app_fen1, R.color.app_fen2, R.color.app_fen3);


        maintab_shopbus_left_txt = (TextView) BaseView.findViewById(R.id.maintab_shopbus_left_txt);
        maintab_shopbus_left_txt.setOnClickListener(this);
        maintab_shopbus_right_iv = (ImageView) BaseView.findViewById(R.id.maintab_shopbus_right_iv);


        maintab_shopbus_show_lay = (LinearLayout) BaseView.findViewById(R.id.maintab_shopbus_show_lay);
        maintab_shopbus_ls = (ListView) BaseView.findViewById(R.id.maintab_shopbus_ls);
        maintab_shopbus_down_lay = (LinearLayout) BaseView.findViewById(R.id.maintab_shopbus_down_lay);
        maintab_sopbus_bottom_select_iv = ViewHolder.get(BaseView, R.id.maintab_sopbus_bottom_select_iv);
        maintab_sopbus_bottom_sum_mony = ViewHolder.get(BaseView, R.id.maintab_sopbus_bottom_sum_mony);
        maintab_sopbus_bottom_sum_mumber = ViewHolder.get(BaseView, R.id.maintab_sopbus_bottom_sum_mumber);
        maintab_sopbus_bottom_jiesuan = ViewHolder.get(BaseView, R.id.maintab_sopbus_bottom_jiesuan);
        maintab_shopbus_nodata_lay = ViewHolder.get(BaseView, R.id.maintab_shopbus_nodata_lay);

//        maintab_shopbus_ls.setPullRefreshEnable(true);
//        maintab_shopbus_ls.setPullLoadEnable(false);
//        maintab_shopbus_ls.setXListViewListener(this);
//        maintab_shopbus_ls.hidefoot();
        maintab_shopbus_nodata_lay.setOnClickListener(this);
        maintab_shopbus_left_txt.setOnClickListener(this);
        maintab_shopbus_right_iv.setOnClickListener(this);
        maintab_sopbus_bottom_jiesuan.setOnClickListener(this);
        maintab_sopbus_bottom_select_iv.setOnClickListener(this);
    }

    public void ReciverBusMsg(BMessage MyMessage) {
        int messageType = MyMessage.getMessageType();
        switch (messageType) {
            case BMessage.Shop_Frash:
                IData(REFRESHING);
                break;
            default:
                break;

        }
    }

    /**
     * 加载购物车列表数据 type标识 是否是删除完后的刷新
     */
    private void IData(int Type) {
        if (Type == INITIALIZE)
            PromptManager.showtextLoading(BaseContext, "加载中");
        // 获取数据钱 先要收到设置未处于全选状态********处于结算状态***********
        IsAllSelectIv = false;
        IsJeiSuan = true;
        maintab_shopbus_left_txt.setVisibility(View.GONE);
//        maintab_shopbus_Right_iv.setImageResource(!IsJeiSuan ? R.drawable.shoubus_ok// R.drawable.center_iv2
//                : R.drawable.lajixiang_iv);
        maintab_shopbus_right_iv.setVisibility(IsJeiSuan?View.VISIBLE:View.GONE);
        maintab_shopbus_right_txt.setVisibility(!IsJeiSuan?View.VISIBLE:View.GONE);
        SetIvSelect(maintab_sopbus_bottom_select_iv, IsAllSelectIv);
        maintab_shopbus_right_iv.setVisibility(View.GONE);
        // 开始获取数据
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", MyUser.getId());
        map.put("channel", "");
        FBGetHttpData(map, Constants.ShopBus_Ls, Request.Method.GET, 0, Type);
    }

    @Override
    public void InitCreate(Bundle d) {
    }

    @Override
    public void getResult(int Code, String Msg, BComment Data) {
// type=0是代表购物车列表数据
        // ；；type=1代表删除
        // ；；type

        switch (Data.getHttpResultTage()) {
            case 0:// 代表购物车列表数据
                SetShopView(Data.getHttpResultStr(), Data.getHttpLoadType());

                break;
            case 1:// 删除购物车
                IsJeiSuan = !IsJeiSuan;
//                maintab_shopbus_Right_iv.setImageResource(!IsJeiSuan ? R.color.transparent
//                        : R.drawable.lajixiang_iv);
                maintab_shopbus_right_iv.setVisibility(IsJeiSuan?View.VISIBLE:View.GONE);
                maintab_shopbus_right_txt.setVisibility(!IsJeiSuan?View.VISIBLE:View.GONE);

                StrUtils.SetTxt(maintab_sopbus_bottom_jiesuan, IsJeiSuan ? "结算" : "删除");
                PromptManager.ShowCustomToast(BaseContext, "删除成功");
                IData(INITIALIZE);
                break;
            case 4:// 修改规格商品的个数
                // PromptManager.ShowCustomToast(BaseContext, "修改成功");
                break;
            default:
                break;
        }
    }

    /**
     * 根据获取的string 进行解析处理到view层显示
     */
    private void SetShopView(String ShopData, int Type) {
        if (StrUtils.isEmpty(ShopData)) {
            onError(Constants.SucessToError, Type);
            maintab_shopbus_right_iv.setVisibility(View.GONE);
            maintab_shopbus_down_lay.setVisibility(View.GONE);
            IDataView(maintab_shopbus_show_lay, maintab_shopbus_nodata_lay, NOVIEW_ERROR);
            ShowErrorCanLoad(getResources().getString(R.string.no_shopbus));
            ShowErrorIv(R.drawable.error_shopbus);
            NoGood = true;
            if (Type == REFRESHING) {
//                    maintab_shopbus_ls.stopRefresh();
                fragment_shopbus_refrash.setRefreshing(false);
            }
            return;
        }
        NoGood = false;
        IDataView(maintab_shopbus_show_lay, maintab_shopbus_nodata_lay, NOVIEW_RIGHT);
        if (Type == REFRESHING) {
            fragment_shopbus_refrash.setRefreshing(false);
        }

        BShopBus bComment = JSON.parseObject(ShopData,
                BShopBus.class);
        // AllNumber=0;


        if (bComment.getPT() == null && bComment.getCG() == null) {// 没有普通也没有采购的
            maintab_shopbus_right_iv.setVisibility(View.GONE);
            maintab_shopbus_down_lay.setVisibility(View.GONE);
            IDataView(maintab_shopbus_show_lay, maintab_shopbus_nodata_lay, NOVIEW_ERROR);
            AllNumber = 0;
            Spuit.ShopBusNumber_Save(BaseContext, 0);
            Send(AllNumber);
            ShowErrorCanLoad(getResources().getString(R.string.no_shopbus));
            ShowErrorIv(R.drawable.error_shopbus);
            NoGood = true;
        }
        if (bComment.getPT() != null && bComment.getCG() == null) {// 只有普通的没有采购的
            busAdapter = new MainShopBusAp(R.layout.item_shopbus_out);
            maintab_shopbus_ls.setAdapter(busAdapter);
            busAdapter.FrashData(bComment.getPT());
            IsPu = true;
            maintab_shopbus_left_txt.setText("零售商品");

            maintab_shopbus_left_txt.setVisibility(View.GONE);
            maintab_shopbus_left_txt.setClickable(false);
            maintab_shopbus_right_iv.setVisibility(View.VISIBLE);
            AllNumber = 0;

            for (int i = 0; i < bComment.getPT().size(); i++) {
                AllNumber = AllNumber
                        + bComment.getPT().get(i).getList().size();

            }
            Spuit.ShopBusNumber_Save(BaseContext, AllNumber);
            Send(AllNumber);
            maintab_shopbus_down_lay.setVisibility(View.VISIBLE);
        }
        if (bComment.getPT() == null && bComment.getCG() != null) {// 只有采购的没有普通的
            busAdapter = new MainShopBusAp(R.layout.item_shopbus_out);
            maintab_shopbus_ls.setAdapter(busAdapter);
            busAdapter.FrashData(bComment.getCG());
            IsPu = false;
            maintab_shopbus_left_txt.setText("采购商品");
            maintab_shopbus_left_txt.setVisibility(View.GONE);
            maintab_shopbus_left_txt.setClickable(false);
            maintab_shopbus_left_txt.setVisibility(View.VISIBLE);

            AllNumber = 0;

            for (int i = 0; i < bComment.getCG().size(); i++) {

                AllNumber = AllNumber
                        + bComment.getCG().get(i).getList().size();

            }
            Spuit.ShopBusNumber_Save(BaseContext, AllNumber);
            Send(AllNumber);
            maintab_shopbus_down_lay.setVisibility(View.VISIBLE);
        }

        if (bComment.getPT() != null && bComment.getCG() != null) {// 既有采购也有普通
            maintab_shopbus_left_txt.setVisibility(View.VISIBLE);
            busAdapter = new MainShopBusAp(R.layout.item_shopbus_out);
            maintab_shopbus_ls.setAdapter(busAdapter);
            if (TypeShow == -1)
                TypeShow = 1;
            if (TypeShow == 1) {
                StrUtils.SetTxt(maintab_shopbus_left_txt, "零售商品");
                ViewUtils.SetIvOnTextview(BaseContext, maintab_shopbus_left_txt, R.drawable.arrow_down, 20);
                busAdapter.FrashData(bComment.getPT());
                IsPu = true;
            }
            if (TypeShow == 2) {
                StrUtils.SetTxt(maintab_shopbus_left_txt, "采购商品");
                busAdapter.FrashData(bComment.getCG());
                IsPu = false;
            }
            maintab_shopbus_left_txt.setClickable(true);
            maintab_shopbus_left_txt.setVisibility(View.VISIBLE);
            AllNumber = 0;
            for (int i = 0; i < bComment.getPT().size(); i++) {
                AllNumber = AllNumber
                        + bComment.getPT().get(i).getList().size();
            }

            for (int i = 0; i < bComment.getCG().size(); i++) {
                AllNumber = AllNumber
                        + bComment.getCG().get(i).getList().size();
            }
            Spuit.ShopBusNumber_Save(BaseContext, AllNumber);
            Send(AllNumber);
            maintab_shopbus_down_lay.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onError(String error, int LoadType) {

        NoGood = false;
        switch (LoadType) {
            case INITIALIZE:
                IDataView(maintab_shopbus_show_lay, maintab_shopbus_nodata_lay, NOVIEW_ERROR);
                ShowErrorCanLoad(getResources().getString(R.string.error_null_noda));
                break;
            case REFRESHING:
                fragment_shopbus_refrash.setRefreshing(false);

                break;
            case DELETE:
                break;

        }
        PromptManager.ShowCustomToast(BaseContext, StrUtils.NullToStr(error));

//if(DELETE==LoadType){
//    PromptManager.ShowCustomToast(BaseContext, StrUtils.NullToStr(error));
//    return;
//}
//        if (LoadType == INITIALIZE) {
//            IDataView(maintab_shopbus_show_lay, maintab_shopbus_nodata_lay, NOVIEW_ERROR);
//            ShowErrorCanLoad(getResources().getString(R.string.error_null_noda));
//        }
//
//        NoGood = false;
//        if (LoadType == REFRESHING) {
//            fragment_shopbus_refrash.setRefreshing(false);
//            return;
//        }
//        PromptManager.ShowCustomToast(BaseContext, StrUtils.NullToStr(error));

    }

    /**
     * 设置被勾选状态的方法
     */
    private void SetIvSelect(ImageView vv, boolean IsSelect) {
        vv.setImageResource(IsSelect ? R.drawable.quan_select_3
                : R.drawable.quan_select_1);
    }

    @Override
    public void onRefresh() {
        IData(REFRESHING);
    }

//    @Override
//    public void onRefresh() {
//
//    }
//
//    @Override
//    public void onLoadMore() {
//
//    }

    private class MainShopBusAp extends BaseAdapter {

        /**
         * 填充器
         */
        private LayoutInflater inflater;
        /**
         * 资源id
         */
        private int ResourceId;
        /**
         * 记录内层所有列表的boolen标记
         */
        private List<Boolean> Outbooleans = new ArrayList<Boolean>();
        /**
         * 数据
         */
        private List<BLShopBusOut> datas = new ArrayList<BLShopBusOut>();
        /**
         * 记录下所有的ap
         */
        private List<BusInAdapter> BusInAdapters = new ArrayList<BusInAdapter>();

        public MainShopBusAp(int resourceId) {
            super();

            this.inflater = LayoutInflater.from(BaseContext);
            ResourceId = resourceId;

        }

        /**
         * 刷新时候调用 即使是加载更多 也需要先获取到原有数据然后合并数据后再刷新操作
         *
         * @param dass
         */
        public void FrashData(List<BLShopBusOut> dass) {
            this.datas = dass;

            for (int i = 0; i < dass.size(); i++) {

                getOutbooleans().add(false);

                for (int k = 0; k < datas.get(i).getList().size(); k++) {
                    if (!IsJeiSuan) {// 删除状态
                    } else {
                        if (!IsPu) {// 是采购的全部通过
                            datas.get(i).setIsCanSelct(true);
                        } else {// 是普通的进行判断
                            if (datas.get(i).getList().get(k).getIs_sales()
                                    .equals("1"))
                                datas.get(i).setIsCanSelct(true);
                        }
                    }

                }
                BusInAdapter data = new BusInAdapter(this, datas.get(i)
                        .getList(), i);
                BusInAdapters.add(data);
            }
            if (dass.size() > 0) {
                maintab_shopbus_right_iv.setVisibility(View.VISIBLE);
            }
            ShowAllMony();
            this.notifyDataSetChanged();
        }

        /**
         * 获取数据源的列表
         */
        public List<BLShopBusOut> GetDatas() {
            return datas;
        }

        /**
         * 获取Boolen源的列表
         */
        public List<Boolean> getOutbooleans() {
            return Outbooleans;
        }

        /**
         * 获取InAp的列表
         */
        public List<BusInAdapter> GetInAp() {
            return BusInAdapters;
        }

        /**
         * 设置是否全选
         */
        public void AllSelect(boolean IsAllSelect) {
            for (int i = 0; i < getCount(); i++) {

                getOutbooleans().set(i, IsAllSelect);
                for (int j = 0; j < BusInAdapters.get(i).getCount(); j++) {
                    BusInAdapters.get(i).getInSelect().set(j, IsAllSelect);
                    BusInAdapters.get(i).notifyDataSetChanged();
                }
            }
            ShowAllMony();
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
        public View getView(int arg0, View convertView, ViewGroup arg2) {
            BusOutItem myItem = null;
            if (convertView == null) {
                myItem = new BusOutItem();
                convertView = inflater.inflate(ResourceId, null);
                myItem.item_shopbus_ls = (CompleteListView) convertView
                        .findViewById(R.id.item_shopbus_ls);
                myItem.item_shopbus_out_shoptxt = ViewHolder.get(convertView,
                        R.id.item_shopbus_out_shoptxt);
                myItem.item_shopbus_out_select_iv = ViewHolder.get(convertView,
                        R.id.item_shopbus_out_select_iv);
                myItem.item_shopbus_out_iv_tag = ViewHolder.get(convertView,
                        R.id.item_shopbus_out_iv_tag);

                myItem.item_shopbus_out_shopinf_lay = (LinearLayout) convertView
                        .findViewById(R.id.item_shopbus_out_shopinf_lay);
                myItem.item_shopbus_ls.setAdapter(BusInAdapters.get(arg0));
                convertView.setTag(myItem);
            } else {
                myItem = (BusOutItem) convertView.getTag();
            }

            final BLShopBusOut daBlComment = datas.get(arg0);
            if (!IsPu) {
                datas.get(arg0).setIsCanSelct(true);
            }
            myItem.item_shopbus_out_iv_tag.setImageResource(daBlComment
                    .getIs_brand().equals("1") ? R.drawable.shopbus_item_log1
                    : R.drawable.shopbus_item_log2);

            myItem.item_shopbus_ls
                    .setOnItemClickListener(new AdapterView.OnItemClickListener() {// 点击进入商品详情

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1,
                                                int arg2, long arg3) {
                            BLShopBusIn dBldCssomment = (BLShopBusIn) arg0
                                    .getItemAtPosition(arg2);
                            PromptManager.SkipActivity(
                                    BaseActivity,
                                    new Intent(BaseContext, AGoodDetail.class)
                                            .putExtra("goodid",
                                                    dBldCssomment.getGoods_id()));

                        }
                    });
            StrUtils.SetTxt(myItem.item_shopbus_out_shoptxt,
                    daBlComment.getSeller_name());
            if (!IsJeiSuan) {// 删除
                SetIvSelect(myItem.item_shopbus_out_select_iv, getOutbooleans()
                        .get(arg0));

                myItem.item_shopbus_out_select_iv
                        .setOnClickListener(new OutApClick(BusInAdapters
                                .get(arg0), !getOutbooleans().get(arg0),
                                myItem.item_shopbus_out_select_iv, this, arg0));


            } else {

                if (daBlComment.isIsCanSelct()) {
                    SetIvSelect(myItem.item_shopbus_out_select_iv,
                            getOutbooleans().get(arg0));

                    myItem.item_shopbus_out_select_iv
                            .setOnClickListener(new OutApClick(BusInAdapters
                                    .get(arg0), !getOutbooleans().get(arg0),
                                    myItem.item_shopbus_out_select_iv, this,
                                    arg0));

                } else {
                    myItem.item_shopbus_out_select_iv.setVisibility(View.INVISIBLE);
                }
            }
            myItem.item_shopbus_out_shopinf_lay
                    .setOnClickListener(new View.OnClickListener() {// 点击进入店铺
                        @Override
                        public void onClick(View v) {

                            PromptManager
                                    .SkipActivity(
                                            BaseActivity,
                                            new Intent(
                                                    BaseActivity,
                                                    daBlComment.getIs_brand()
                                                            .equals("1") ? ABrandDetail.class
                                                            : AShopDetail.class)
                                                    .putExtra(
                                                            BaseKey_Bean,
                                                            new BComment(
                                                                    daBlComment
                                                                            .getSeller_id(),
                                                                    daBlComment
                                                                            .getSeller_name())));
                        }
                    });
            // // Outbooleans.set(arg0, !Outbooleans.get(arg0));
            return convertView;
        }

        class BusOutItem {
            ImageView item_shopbus_out_iv_tag;

            LinearLayout item_shopbus_out_shopinf_lay;
            CompleteListView item_shopbus_ls;
            TextView item_shopbus_out_shoptxt;
            ImageView item_shopbus_out_select_iv;
        }
    }

    /**
     * 外层点击时候需要的点击事件
     *
     * @author datutu
     */
    class OutApClick implements View.OnClickListener {
        /**
         * 内层的ap
         */
        BusInAdapter busInAdapter;
        /**
         * true标识 需要勾选
         */
        // boolean IsSelect;
        /**
         * 外层的iamgview
         */

        ImageView MYvIEW;
        /**
         * 需要把通过ap调用方法改变外层的列表
         */
        MainShopBusAp busAdapter;
        /**
         * 上边方法需要用到postion
         */
        int Postion;

        public OutApClick(BusInAdapter busInAdapter, boolean isselect,
                          ImageView VV, MainShopBusAp mBusAdapter, int mpostion) {
            super();
            this.busInAdapter = busInAdapter;
            this.MYvIEW = VV;
            // this.IsSelect = isselect;
            this.busAdapter = mBusAdapter;
            this.Postion = mpostion;
        }

        @Override
        public void onClick(View arg0) {
            SetIvSelect(MYvIEW, !busAdapter.getOutbooleans().get(Postion));
            busAdapter.getOutbooleans().set(Postion, !busAdapter.getOutbooleans().get(Postion));
            for (int i = 0; i < busInAdapter.getCount(); i++) {
                busInAdapter.getInSelect().set(i,
                        busAdapter.getOutbooleans().get(Postion));
            }
            busInAdapter.notifyDataSetChanged();
            ShowAllMony();
            checkAll(!busAdapter.getOutbooleans().contains(false));
        }
    }

    /**
     * 内部的adapter
     */
    private class BusInAdapter extends BaseAdapter {

        /**
         * 填充器
         */
        private LayoutInflater inflater;
        /**
         * 记录外层adapter的AP
         */
        private MainShopBusAp MyOutAdapter;
        /**
         * 记录标识
         */
        private List<Boolean> Inselected = new ArrayList<Boolean>();
        /**
         * 数据
         */
        private List<BLShopBusIn> datas;
        /**
         * 记录外层的Item的位置
         */
        private int OutPostion;

        public BusInAdapter(MainShopBusAp OutAdapter, List<BLShopBusIn> dss,
                            int outpostion) {
            super();

            this.inflater = LayoutInflater.from(BaseContext);
            this.MyOutAdapter = OutAdapter;
            this.datas = dss;
            this.OutPostion = outpostion;
            for (int j = 0; j < dss.size(); j++) {
                getInSelect().add(false);
            }
        }

        /**
         * 获取boolean的列表
         */
        public List<Boolean> getInSelect() {
            return Inselected;
        }

        /**
         * 获取列表
         */
        public List<BLShopBusIn> GetInLs() {
            return datas;
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
        public View getView(final int arg0, View convertView, ViewGroup arg2) {
            BusInItem myItem = null;
            if (convertView == null) {
                myItem = new BusInItem();
                convertView = inflater.inflate(R.layout.item_shopbus_in, null);
                myItem.item_shopbus_in_guige = ViewHolder.get(convertView,
                        R.id.item_shopbus_in_guige);
                myItem.item_shopbus_in_name = ViewHolder.get(convertView,
                        R.id.item_shopbus_in_name);
                myItem.item_shopbus_in_price = ViewHolder.get(convertView,
                        R.id.item_shopbus_in_price);
                myItem.item_shopbus_in_log = ViewHolder.get(convertView,
                        R.id.item_shopbus_in_log);
                myItem.item_shopbus_in_number = (AddAndSubView) convertView
                        .findViewById(R.id.item_shopbus_in_number);
                myItem.item_shopbus_in_select_log = ViewHolder.get(convertView,
                        R.id.item_shopbus_in_select_log);
                myItem.item_shopbus_in_log_tag = ViewHolder.get(convertView,
                        R.id.item_shopbus_in_log_tag);
                myItem.item_shopbus_in_sell_statue = ViewHolder.get(
                        convertView, R.id.item_shopbus_in_sell_statue);
                ImageLoaderUtil.Load2(datas.get(arg0).getCover(),
                        myItem.item_shopbus_in_log, R.drawable.error_iv2);
                convertView.setTag(myItem);
            } else {
                myItem = (BusInItem) convertView.getTag();
            }

            BLShopBusIn daBldComment = datas.get(arg0);

            // IsPu
            myItem.item_shopbus_in_log_tag.setVisibility(IsPu ? View.GONE
                    : View.VISIBLE);
            StrUtils.SetTxt(myItem.item_shopbus_in_name,
                    daBldComment.getGoods_name());

            StrUtils.SetTxt(myItem.item_shopbus_in_guige,
                    daBldComment.getAttr_name());

            StrUtils.SetTxt(
                    myItem.item_shopbus_in_price,
                    "￥"
                            + StrUtils.SetTextForMony(daBldComment
                            .getGoods_price()));

            if (!IsPu) {
//				myItem.item_shopbus_in_number.SetMinNumber(10);

                // 设置可选
                daBldComment.setIs_sales("1");
            }
            myItem.item_shopbus_in_number
                    .setOnNumChangeListener(new AddAndSubView.OnNumChangeListener() {
                        @Override
                        public void onNumChange(View view, int num) {

                            if (num != StrUtils.toInt(datas.get(arg0)
                                    .getGoods_num())) {
                                AlterGoodsNumber(datas.get(arg0).getGoods_id(),
                                        num, datas.get(arg0).getCid());
                            }
                            datas.get(arg0).setGoods_num(num + "");
                            ShowAllMony();
                        }
                    });
            myItem.item_shopbus_in_number.setNum(StrUtils.toInt(daBldComment
                    .getGoods_num()));

            if (!IsJeiSuan) {// 删除状态
                myItem.item_shopbus_in_select_log.setClickable(true);
                SetIvSelect(myItem.item_shopbus_in_select_log, getInSelect()
                        .get(arg0));
                myItem.item_shopbus_in_select_log
                        .setOnClickListener(new InSelcetClickListener(myItem,
                                getInSelect().get(arg0), getInSelect(), arg0,
                                MyOutAdapter, OutPostion));
                myItem.item_shopbus_in_sell_statue.setVisibility(View.GONE);
                myItem.item_shopbus_in_name.setTextColor(getResources()
                        .getColor(R.color.black));
                myItem.item_shopbus_in_price.setTextColor(getResources()
                        .getColor(R.color.app_fen));
                myItem.item_shopbus_in_select_log.setVisibility(View.VISIBLE);
            } else {
                if (daBldComment.getIs_sales().equals("1")) {// 可以状态
                    myItem.item_shopbus_in_select_log.setClickable(true);
                    SetIvSelect(myItem.item_shopbus_in_select_log,
                            getInSelect().get(arg0));
                    myItem.item_shopbus_in_select_log
                            .setOnClickListener(new InSelcetClickListener(
                                    myItem, getInSelect().get(arg0),
                                    getInSelect(), arg0, MyOutAdapter,
                                    OutPostion));
                    myItem.item_shopbus_in_sell_statue.setVisibility(View.GONE);
                    myItem.item_shopbus_in_name.setTextColor(getResources()
                            .getColor(R.color.black));
                    myItem.item_shopbus_in_price.setTextColor(getResources()
                            .getColor(R.color.app_fen));
                    myItem.item_shopbus_in_select_log
                            .setVisibility(View.VISIBLE);

                } else {// 不可以
                    myItem.item_shopbus_in_sell_statue
                            .setVisibility(View.VISIBLE);
                    myItem.item_shopbus_in_sell_statue.setText("已下架");
                    myItem.item_shopbus_in_name.setTextColor(getResources()
                            .getColor(R.color.grey));
                    myItem.item_shopbus_in_price.setTextColor(getResources()
                            .getColor(R.color.grey));
                    myItem.item_shopbus_in_select_log.setClickable(false);
                    myItem.item_shopbus_in_select_log
                            .setVisibility(View.INVISIBLE);
                }
            }
            return convertView;
        }

        class BusInItem {
            ImageView item_shopbus_in_log_tag;// 实库虚库的log

            TextView item_shopbus_in_sell_statue;
            TextView item_shopbus_in_guige;
            TextView item_shopbus_in_name;
            TextView item_shopbus_in_price;
            ImageView item_shopbus_in_log;
            ImageView item_shopbus_in_select_log;
            AddAndSubView item_shopbus_in_number;

        }

        /**
         * 二级item的选中Listener
         */

        class InSelcetClickListener implements View.OnClickListener {
            /**
             * 内层AP的boolean 列表
             */
            List<Boolean> booleans;
            /**
             * 当前的item
             */
            BusInItem busInItem;
            /**
             * 位置
             */
            int Postion;
            /**
             * 每点击内部一次 外部需要检测是否全选OR全不选择
             */
            MainShopBusAp MyOutAdapter;
            /**
             * 外层的位置
             */
            private int OutPostion;

            public InSelcetClickListener(BusInItem item, Boolean isselect,
                                         List<Boolean> ss, int postion, MainShopBusAp MyOutA,
                                         int OutPostions) {
                super();
                this.busInItem = item;
                this.OutPostion = OutPostions;
                this.booleans = ss;
                this.Postion = postion;
                this.MyOutAdapter = MyOutA;
            }

            @Override
            public void onClick(View v) {
                booleans.set(Postion, !booleans.get(Postion));
                SetIvSelect(busInItem.item_shopbus_in_select_log,
                        booleans.get(Postion));

                if (!booleans.contains(false)) {// 全部选择
                    MyOutAdapter.getOutbooleans().set(OutPostion, true);
                } else {// 部分选择或者全部未选择
                    MyOutAdapter.getOutbooleans().set(OutPostion, false);
                }
                MyOutAdapter.notifyDataSetChanged();
                ShowAllMony();
                checkAll(!MyOutAdapter.getOutbooleans().contains(false));
            }
        }
    }

    /**
     * 计算总金额并展示金额
     */
    private void ShowAllMony() {
        Float AllMony = 0f;
        int GoodNumber = 0;
        for (int i = 0; i < busAdapter.getCount(); i++) {// 店铺的循环

            List<BLShopBusIn> inls = busAdapter.GetInAp().get(i).GetInLs();
            for (int j = 0; j < busAdapter.GetInAp().get(i).getCount(); j++) {

                if (busAdapter.GetInAp().get(i).getInSelect().get(j)) {// 规格的循环
                    // daComments.add(busAdapter.GetInAp().get(i).GetInLs()
                    // .get(j));
                    if (!inls.get(j).getIs_sales().equals("1"))
                        continue;
                    AllMony = AllMony
                            + Float.valueOf(busAdapter.GetInAp().get(i)
                            .GetInLs().get(j).getGoods_price())
                            * Integer.valueOf(busAdapter.GetInAp().get(i)
                            .GetInLs().get(j).getGoods_num());
                    GoodNumber += Integer.valueOf(busAdapter.GetInAp().get(i)
                            .GetInLs().get(j).getGoods_num());
                }
            }
        }

        StrUtils.SetTxt(maintab_sopbus_bottom_sum_mony,
                "￥" + StrUtils.SetTextForMony(String.valueOf(AllMony)));
        StrUtils.SetTxt(maintab_sopbus_bottom_sum_mumber,
                String.format("(%s件)", String.valueOf(GoodNumber)));
        //
    }

    /**
     * 当全部勾选后需要把activity中选择全部进行自动勾选
     * <p/>
     * boolean表示全部勾选置为
     */
    public void checkAll(boolean checked) {
        IsAllSelectIv = checked;
        SetIvSelect(maintab_sopbus_bottom_select_iv, checked);
    }

    /**
     * 修改G购物车末一个规格商品的数量
     */
    private void AlterGoodsNumber(String goods_id, int goods_num, String cid) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("goods_id", goods_id);
        map.put("member_id", MyUser.getId());
        map.put("goods_num", goods_num + "");
        map.put("cid", cid);
        FBGetHttpData(map, Constants.ShopBus_GoodAlter, Request.Method.PUT, 4,
                INITIALIZE);
    }

    /**
     * 购物车的提示
     */
    public void Send(int Number) {
        BMessage bMessage = new BMessage(BMessage.Tage_Tab_ShopBus);
        // bMessage.setTabShopBusNumber(mBComment.getCart_num());
        bMessage.setTabShopBusNumber(Number);
        EventBus.getDefault().post(bMessage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_main_shopbus_neterrorview:
                PromptManager.GoToNetSeting(BaseActivity);

                break;
            case R.id.maintab_shopbus_nodata_lay:
                if (NoGood) {//标识是没有商品
                    //TODO去跳转到新 商品首页的列表！！！！！！！！！！！！！！！！！！！！！
//                    PromptManager.ShowCustomToast(BaseContext,"跳转到商品列表");
                    PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, ANewHome.class));
                } else
                    IData(INITIALIZE);
                break;
            case R.id.maintab_shopbus_left_txt://切换
                ShowSelect(maintab_shopbus_left_txt);
                break;
            case R.id.maintab_shopbus_right_txt:
                IsJeiSuan = !IsJeiSuan;
                busAdapter.FrashData(busAdapter.GetDatas());
                maintab_shopbus_right_txt.setVisibility(View.GONE);
                maintab_shopbus_right_iv.setVisibility(View.VISIBLE);
//                maintab_shopbus_Right_iv.setImageResource(!IsJeiSuan ? R.drawable.shoubus_ok// R.drawable.center_iv2
//                        : R.drawable.lajixiang_iv);
                StrUtils.SetTxt(maintab_sopbus_bottom_jiesuan, IsJeiSuan ? "结算" : "删除");
                break;
            case R.id.maintab_shopbus_right_iv://删除
                IsJeiSuan = !IsJeiSuan;
                busAdapter.FrashData(busAdapter.GetDatas());
                maintab_shopbus_right_txt.setVisibility(View.VISIBLE);
                maintab_shopbus_right_iv.setVisibility(View.GONE);
//                maintab_shopbus_Right_iv.setImageResource(!IsJeiSuan ? R.drawable.shoubus_ok// R.drawable.center_iv2
//                        : R.drawable.lajixiang_iv);
                StrUtils.SetTxt(maintab_sopbus_bottom_jiesuan, IsJeiSuan ? "结算" : "删除");
                break;
            case R.id.maintab_sopbus_bottom_select_iv://全选
                AllSelect(!IsAllSelectIv);
                break;
            case R.id.maintab_sopbus_bottom_jiesuan://结算
                Account(IsJeiSuan);
                break;

        }

    }

    /**
     * 点击结算
     */
    private void Account(boolean IsAccount) {
        String AccountStr = "";
        String DeleteStr = "";
        // List<BLComment> ShopsLs = new ArrayList<BLComment>();
        List<BLShopBusIn> daComments = new ArrayList<BLShopBusIn>();
        for (int i = 0; i < busAdapter.getCount(); i++) {// 店铺的循环
            List<BLShopBusIn> inls = busAdapter.GetInAp().get(i).GetInLs();
            for (int j = 0; j < busAdapter.GetInAp().get(i).getCount(); j++) {
                if (!inls.get(j).getIs_sales().equals("1")) {
                    if (IsJeiSuan)
                        continue;
                }
                if (busAdapter.GetInAp().get(i).getInSelect().get(j)) {// 规格的循环
                    daComments
                            .add(busAdapter.GetInAp().get(i).GetInLs().get(j));

                }
            }
        }
        // daComments 现在已经保存数据 下边测试数据正确性
        for (BLShopBusIn h : daComments) {
            AccountStr = AccountStr + h.getCid() + ":" + h.getGoods_num() + ",";
            DeleteStr = DeleteStr + h.getCid() + ",";
        }
        if (IsAccount) {// 结算按钮操作
            if (!StrUtils.isEmpty(AccountStr)) {// 已经选择了
                AccountStr = AccountStr.substring(0, AccountStr.length() - 1);
            } else {// 没有选择
                PromptManager.ShowCustomToastLong(BaseContext, "亲,你还没选择结算的商品哦~"
                        + AccountStr);
                return;
            }
            PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                    AOderBeing.class).putExtra("accountstr", AccountStr));
        } else {// 删除按钮操作
            if (!StrUtils.isEmpty(AccountStr)) {// 已经选择了
                DeleteStr = DeleteStr.substring(0, DeleteStr.length() - 1);
            } else {// 没有选择
                PromptManager.ShowCustomToastLong(BaseContext, "亲,你还没选择删除的商品哦~"
                        + AccountStr);
                return;
            }
            // PromptManager.ShowCustomToast(BaseContext, "删除的Cid数据：" +
            // DeleteStr);
            DeleteBus(DeleteStr);

            return;
        }

    }

    /**
     * 删除选中的商品
     *
     * @param Cids
     */
    private void Delete(String Cids) {
        PromptManager.showLoading(BaseContext);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", MyUser.getId());
        map.put("cid", Cids);
        FBGetHttpData(map, Constants.ShopBus_Delete, Request.Method.DELETE, 1,
                DELETE);
    }

    /**
     * 删除购物车
     */
    private void DeleteBus(final String dString) {

        ShowCustomDialog("是否删除选中宝贝", getResources().getString(R.string.cancle),
                getResources().getString(R.string.queding),
                new IDialogResult() {

                    @Override
                    public void RightResult() {
                        Delete(dString);
                    }

                    @Override
                    public void LeftResult() {
                    }
                });


    }

    /**
     * TRUE标识需要进行全部选择操作 ;;;false标识进行全部取消操作
     */
    private void AllSelect(boolean b) {
        busAdapter.AllSelect(b);
        IsAllSelectIv = !IsAllSelectIv;
        SetIvSelect(maintab_sopbus_bottom_select_iv, b);
    }

    /**
     * 左上按钮点击筛选弹出框
     */
    private void ShowSelect(View VV) {

        // left_txt
        PShopBus mBus = new PShopBus(BaseContext, TypeShow);
        mBus.GetSelectReult(new PShopBus.BusSelecListener() {
            @Override
            public void GetResult(int type) {

                if (type == PShopBus.Type_LingShou) {// 零售
                    StrUtils.SetTxt(maintab_shopbus_left_txt, "零售商品");
                    Channel = "PT";

                    TypeShow = 1;
                    IData(INITIALIZE);
                }
                if (type == PShopBus.Type_CaiGou) {// 采购
                    StrUtils.SetTxt(maintab_shopbus_left_txt, "采购商品");
                    Channel = "CG";
                    TypeShow = 2;
                    IData(INITIALIZE);
                }
                return;
            }
        });
        mBus.showAsDropDown(VV, -20, 20);

    }

}
