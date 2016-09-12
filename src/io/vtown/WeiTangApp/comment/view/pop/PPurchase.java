package io.vtown.WeiTangApp.comment.view.pop;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BGoodDetail;
import io.vtown.WeiTangApp.bean.bcomment.easy.BShowShare;
import io.vtown.WeiTangApp.bean.bcomment.easy.gooddetail.BDataGood;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BLShow;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.NHttpBaseStr;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.AddAndSubView;
import io.vtown.WeiTangApp.comment.view.AddAndSubView.OnNumChangeListener;
import io.vtown.WeiTangApp.comment.view.ShowSelectPic;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.listview.HorizontalListView;
import io.vtown.WeiTangApp.event.interf.IHttpResult;
import io.vtown.WeiTangApp.ui.comment.AGoodVidoShare;
import io.vtown.WeiTangApp.ui.title.account.AOderBeing;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-19 上午10:57:14 采购PopupWindow
 * @author Yihuihua
 */
/**
 * @author Yihuihua
 *
 */

/**
 * @author Yihuihua
 *
 */
public class PPurchase extends PopupWindow implements OnClickListener,
        OnNumChangeListener {

    /**
     * 上下文
     */
    private Context pContext;
    /**
     *
     */
    private Activity pActivity;

    /**
     * 基view
     */
    private View BaseView;
    /**
     * 外层的view
     */
    private View pop_conten_purchase;
    /**
     * 内容的view
     */
    private View conten_purchase_nei;

    /**
     * X
     */
    private ImageView pop_show_shangjia_cha;

    /**
     * 含净量的内容
     */
    private CompleteGridView gv_net_content;

    /**
     * 增加和减少
     */
    private AddAndSubView aasv_add_sub;

    /**
     * 实库
     */
    private TextView tv_real_lib;

    /**
     * 虚库
     */
    private TextView tv_virtual_lib;

    /**
     * 帮助
     */
    private ImageView iv_help;

    /**
     * 类型为含净量
     */
    private static final int TYPE_NET_CONTENT = 100;
    /**
     * 类型为颜色
     */
    private static final int TYPE_COLORS = 101;

    private static final int TYPE_REAL_LIB = 102;

    private static final int TYPE_VIRTUAL_LIB = 103;

    /**
     * 区别不同页面进来的Popup
     */
    private int ShowType = 0;

    private LinearLayout ll_real_virtual;

    /**
     * 建议零售价输入框
     */
    private EditText et_price;

    /**
     * 建议零售价
     */
    private LinearLayout ll_price;

    /**
     * 增减按钮
     */
    private LinearLayout ll_counts;

    /**
     * 确定按钮
     */
    private Button btn_virtual_lib_confirm;

    /**
     * 取消按钮
     */
    private Button btn_virtual_lib_cancel;

    /**
     * 店铺-->商品管理-->品牌商品-->采购
     */
    public static final int TYPE_SHOP_GOOD_MANAGER_CAIGOU = 110;
    /**
     * 从商品详情点击买弹出的popup
     */
    public static final int TYPE_GOOD_DETAIL_BUY = 111;
    /**
     * 商品管理=》虚库到家
     */
    public static final int TYPE_GOOD_GoodManger_XuKuDaoJia = 137;
    /**
     * 从商品详情点击一件代卖弹出的popup
     */
    public static final int TYPE_GOOD_DETAIL_REPLACE_SELL = 112;

    /**
     * 加入购物车成功的type
     */
    public static final int TYPE_ADD_SHOPBUS = 113;
    /**
     * 一键代卖成功的type
     */
    public static final int TYPE_ADD_ONLINE = 114;

    private List<BDataGood> data = new ArrayList<BDataGood>();

    /**
     * 价格
     */
    private TextView pop_purchase_price;

    /**
     * 库存数量
     */
    private TextView pop_purchase_kucun;

    /**
     * 颜色
     */
    private CompleteGridView gv_colors;

    /**
     * 规格1
     */
    private TextView tv_good_standard_lable1;

    /**
     * 规格2
     */
    private TextView tv_good_standard_lable2;

    /**
     * 上面GridView的AP
     */
    private MyAdapter myUpAdapter;

    /**
     * 下面GridView的AP
     */
    private MyAdapter myDownAdapter;

    /**
     * 赚钱AP
     */
    private MakeMoneyAdapter makeMoneyAdapter;

    /**
     * popup头布局
     */
    private LinearLayout ll_pop_good_icon_price_store;

    /**
     * 商品icon
     */
    private ImageView iv_pop_goods_icon;

    /**
     * 赚到布局
     */
    private LinearLayout ll_make_how_much;

    /**
     * 赚到横向ListView
     */
    private HorizontalListView hl_make_how_much;

    private List<BDComment> make_money_beans = null;

    /**
     * 是否第一次点击过的标识 1=》标识是先点击的上边/////2=》标识先点击的下边/////0=》标识未点击
     */
    private int IsFristClick = 0;
    // 记录是否上边下边已经点击过了
    private int UpPostion = -1;
    private int DownPostion = -1;
    private int mPostion = 0;
    // 最后一次的点击位置
    private int LastClickItem = 0;
    private boolean LastClickIsUp = true;

    /**
     * 是否选中了上面的规格
     */
    private boolean isSelectContentUp = false;

    /**
     * 是否选中了下面的规格
     */
    private boolean isSelectContentDown = false;

    private boolean UpAdapterItemClick = false;
    private boolean DownAdapterItemClick = false;

    // 记录数量
    private int goods_num = 0;

    private List<BDataGood> up_Data = new ArrayList<BDataGood>();

    private List<BDataGood> Down_Data = new ArrayList<BDataGood>();

    // /**
    // * 存放所有价格
    // */
    // private List<String> sell_prices = new ArrayList<String>();

    private float[] sell_prices = null;

    private BGoodDetail databean = null;

    /**
     * 商品Id
     */
    private String goods_id;

    private BUser user_Get;

    private String MyChannel;

    private boolean IsCaiGou;

    public PPurchase(Activity activity, Context pContext, int width,
                     int ShowType, BGoodDetail data, String goods_id, boolean iscaigoy) {
        super();
        this.IsCaiGou = iscaigoy;
        this.data = data.getGoods_attr();
        this.databean = data;
        this.pActivity = activity;
        this.pContext = pContext;
        this.ShowType = ShowType;
        this.goods_id = goods_id;

        user_Get = Spuit.User_Get(pContext);
        MyChannel = IsCaiGou ? "CG" : "PT";
        BaseView = LayoutInflater.from(pContext).inflate(
                R.layout.pop_purchase_good, null);
        pop_conten_purchase = BaseView.findViewById(R.id.pop_conten_purchase);
        conten_purchase_nei = BaseView.findViewById(R.id.conten_purchase_nei);
        IData();
        IPop(width);
        IView();
        ITouch();

    }

    private void IData() {

        make_money_beans = new ArrayList<BDComment>();

        if (!StrUtils.isEmpty(databean.getLadder_price().getNumber())) {
            List<String> Numbers = JSON.parseArray(databean.getLadder_price()
                    .getNumber(), String.class);
            for (String numb : Numbers) {
                BDComment data = new BDComment(numb, 2);
                make_money_beans.add(data);
            }

        }

        if (!StrUtils.isEmpty(databean.getLadder_price().getRate())) {
            List<String> Rates = JSON.parseArray(databean.getLadder_price()
                    .getRate(), String.class);

            for (String rate : Rates) {
                BDComment data = new BDComment(rate, 1);
                make_money_beans.add(data);
            }

        }

    }

    /**
     * 组合上架参数
     */
    private JSONArray GetOnlineAttrs() {
        // [{"id":"15","attr_id":1041328660,"sell_price":13000},{"id":"16","attr_id":1041328632,"sell_price":14000},{"id":"17","attr_id":1041328633,"sell_price":15000}]
        BDataGood select_bl = null;
        if (!isSelectContentUp && isSelectContentDown) {
            select_bl = (LastClickIsUp ? myUpAdapter : myDownAdapter)
                    .GetDataResource().get(LastClickItem);
        }

        JSONArray json = new JSONArray();
        if (data != null && data.size() > 0) {
            if (make_money_beans != null && make_money_beans.size() > 0) {
                BDComment bd = make_money_beans.get(mPostion);
                String makeMoney = bd.getMakeMoney();
                float makeMoneyF = Float.parseFloat(makeMoney);
                int makeMoneyType = bd.getMakeMoneyType();
                String id = "";
                String attr_id = "";
                String sell_price = "";
                sell_prices = new float[data.size()];

                for (int i = 0; i < data.size(); i++) {

                    JSONObject jo = new JSONObject();
                    if (select_bl != null) {
                        if (select_bl.getId().equals(data.get(i).getId())) {
                            id = select_bl.getId();
                            attr_id = select_bl.getAttr_id();
                            String price = et_price.getText().toString().trim();
                            // if(StrUtils.isEmpty(price)){
                            // PromptManager.ShowMyToast(pContext, "请输入建议零售价");
                            //
                            // }

                            sell_price = Float.parseFloat(price) * 100 + "";

                        }
                    } else {
                        float sell_priceF = 0;
                        switch (makeMoneyType) {
                            case 1:// 百分比

                                sell_priceF = Float.parseFloat(data.get(i)
                                        .getSell_price())
                                        + Float.parseFloat(data.get(i)
                                        .getSell_price())
                                        * (makeMoneyF / 100);

                                break;

                            case 2:// 具体数值
                                sell_priceF = Float.parseFloat(data.get(i)
                                        .getSell_price()) + makeMoneyF;
                                break;
                        }
                        id = data.get(i).getId();
                        attr_id = data.get(i).getAttr_id();
                        sell_price = sell_priceF + "";
                    }

                    sell_prices[i] = Float.parseFloat(sell_price);

                    try {
                        jo.put("id", id);
                        jo.put("attr_id", attr_id);
                        jo.put("sell_price", sell_price);
                        json.put(jo);
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            }
        }
        return json;
    }

    /**
     * @author 王永奎
     */
    private void IStandard1() {

        List<String> netContent = new ArrayList<String>();
        List<String> colors = new ArrayList<String>();

        for (BDataGood bld : data) {
            netContent.add(bld.getAttr_map().getV1());
            colors.add(bld.getAttr_map().getV2());
        }
    }

    /**
     * 刷新布局
     *
     */
    private void IFView() {

        BDataGood blComment = (LastClickIsUp ? myUpAdapter : myDownAdapter)
                .GetDataResource().get(LastClickItem);

        if (blComment != null) {
            pop_purchase_price.setVisibility(View.VISIBLE);

            String format_price = String.format("￥%1$s元",
                    StrUtils.SetTextForMony(blComment.getSell_price()));
            StrUtils.SetTxt(pop_purchase_price, format_price);
            StrUtils.SetTxt(pop_purchase_kucun, databean.getTitle() + " "
                    + blComment.getAttr_name());
            if (null != makeMoneyAdapter && ShowType == TYPE_GOOD_DETAIL_REPLACE_SELL) {
                BDComment item = (BDComment) makeMoneyAdapter.getItem(mPostion);
                float showMoney = 0.0f;
                switch (item.getMakeMoneyType()) {
                    case 1://百分比
                        showMoney = Float.parseFloat(blComment.getSell_price()) + (Float.parseFloat(blComment.getSell_price()) * (Float.parseFloat(item.getMakeMoney()) / 100));
                        //showMoney = StrUtils.To2Float(showMoneyF);

                        //Float.parseFloat(new DecimalFormat("#.00").format(showMoneyF));
                        break;

                    case 2://具体数字
                        showMoney = Float.parseFloat(blComment.getSell_price()) + Float.parseFloat(item.getMakeMoney());
                        break;

                }
                StrUtils.SetTxt(et_price,
                        StrUtils.SetTextForMony(showMoney + ""));
            }


        }

    }

    private void IView() {
        pop_show_shangjia_cha = (ImageView) BaseView
                .findViewById(R.id.pop_purchase_cha);

        pop_show_shangjia_cha.setOnClickListener(this);
        gv_net_content = (CompleteGridView) BaseView
                .findViewById(R.id.pop_purchase_grid);
        gv_colors = (CompleteGridView) BaseView.findViewById(R.id.gv_colors);

        ll_pop_good_icon_price_store = (LinearLayout) BaseView
                .findViewById(R.id.ll_pop_good_icon_price_store);
        iv_pop_goods_icon = (ImageView) BaseView
                .findViewById(R.id.iv_pop_goods_icon);

        ImageLoaderUtil.Load2(databean.getCover(), iv_pop_goods_icon,
                R.drawable.error_iv2);
        // 第一次弹出页面时，头布局隐藏

        pop_purchase_price = (TextView) BaseView
                .findViewById(R.id.pop_purchase_price);
        pop_purchase_kucun = (TextView) BaseView
                .findViewById(R.id.pop_purchase_kucun);
        pop_purchase_price.setVisibility(View.GONE);

        // 默认显示商品的名字
        StrUtils.SetTxt(pop_purchase_kucun, databean.getTitle());

        tv_good_standard_lable1 = (TextView) BaseView
                .findViewById(R.id.tv_good_standard_lable1);
        tv_good_standard_lable2 = (TextView) BaseView
                .findViewById(R.id.tv_good_standard_lable2);

        ll_make_how_much = (LinearLayout) BaseView
                .findViewById(R.id.ll_make_how_much);
        hl_make_how_much = (HorizontalListView) BaseView
                .findViewById(R.id.hl_make_how_much);

        StrUtils.SetTxt(tv_good_standard_lable1, data.get(0).getAttr_map()
                .getC1());
        StrUtils.SetTxt(tv_good_standard_lable2, data.get(0).getAttr_map()
                .getC2());

        aasv_add_sub = (AddAndSubView) BaseView.findViewById(R.id.aasv_add_sub);
//        if (IsCaiGou) {// 采购
//            aasv_add_sub.setNum(10);
//            aasv_add_sub.SetMinNumber(10);
//        } else {
            aasv_add_sub.setNum(1);
//        }
        goods_num = aasv_add_sub.getNum();
        tv_real_lib = (TextView) BaseView.findViewById(R.id.tv_real_lib);
        tv_virtual_lib = (TextView) BaseView.findViewById(R.id.tv_virtual_lib);
        iv_help = (ImageView) BaseView.findViewById(R.id.iv_help);
        ll_real_virtual = (LinearLayout) BaseView
                .findViewById(R.id.ll_real_virtual);
        et_price = (EditText) BaseView.findViewById(R.id.pop_purchase_ed);
        ll_price = (LinearLayout) BaseView.findViewById(R.id.ll_price);

        ll_counts = (LinearLayout) BaseView.findViewById(R.id.ll_counts);

        btn_virtual_lib_confirm = (Button) BaseView
                .findViewById(R.id.btn_virtual_lib_confirm);
        btn_virtual_lib_cancel = (Button) BaseView
                .findViewById(R.id.btn_virtual_lib_cancel);

        btn_virtual_lib_confirm.setOnClickListener(this);
        btn_virtual_lib_cancel.setOnClickListener(this);
        aasv_add_sub.setOnNumChangeListener(this);

        switch (ShowType) {
            case TYPE_SHOP_GOOD_MANAGER_CAIGOU:
                ll_real_virtual.setVisibility(View.VISIBLE);
                ll_counts.setVisibility(View.VISIBLE);
                ll_price.setVisibility(View.GONE);
                break;
            case TYPE_GOOD_DETAIL_BUY:
                ll_real_virtual.setVisibility(View.GONE);
                ll_counts.setVisibility(View.VISIBLE);
                ll_price.setVisibility(View.GONE);

                break;
            case TYPE_GOOD_DETAIL_REPLACE_SELL:
                ll_real_virtual.setVisibility(View.GONE);
                ll_counts.setVisibility(View.GONE);
                ll_price.setVisibility(View.VISIBLE);
                ll_make_how_much.setVisibility(View.VISIBLE);
                btn_virtual_lib_confirm.setText("直接上架");
                btn_virtual_lib_cancel.setText("上架并转发");
                break;
            case 113:

                break;
        }

        makeMoneyAdapter = new MakeMoneyAdapter(pContext,
                R.layout.item_ppurchase_make_how_money, make_money_beans);
        hl_make_how_much.setAdapter(makeMoneyAdapter);

        // myUpAdapter = new MyAdapter(FilterData(datBlComment2.getGoodinfo()
        // .getAttr(), 1), pContext, 1);
        // myDownAdapter = new MyAdapter(FilterData(datBlComment2.getGoodinfo()
        // .getAttr(), 2), pContext, 2);

        up_Data = FilterData(data, 1);
        myUpAdapter = new MyAdapter(up_Data, pContext, 1);
        Down_Data = FilterData(data, 2);
        myDownAdapter = new MyAdapter(Down_Data, pContext, 2);

        // 上边
        gv_net_content.setNumColumns(3);
        gv_net_content.setHorizontalSpacing(DimensionPixelUtil.dip2px(pContext,
                5));
        gv_net_content.setVerticalSpacing(DimensionPixelUtil
                .dip2px(pContext, 5));
        // 下边
        gv_colors.setNumColumns(3);
        gv_colors.setHorizontalSpacing(DimensionPixelUtil.dip2px(pContext, 5));
        gv_colors.setVerticalSpacing(DimensionPixelUtil.dip2px(pContext, 5));

        gv_net_content.setAdapter(myUpAdapter);
        gv_colors.setAdapter(myDownAdapter);
        gv_net_content.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                BDataGood da = (BDataGood) arg0.getItemAtPosition(arg2);
                LastClickItem = arg2;
                LastClickIsUp = true;
                isSelectContentUp = true;
                UpPostion = arg2;
                switch (IsFristClick) {
                    case 0:// 表示第一次点击时候的控制
                    case 1:

                        IsFristClick = 1;
                        for (int i = 0; i < myUpAdapter.gradItems().size(); i++) {
                            myUpAdapter.gradItems().get(i).tv_good_detail_grad_content
                                    .setBackground(pContext
                                            .getResources()
                                            .getDrawable(
                                                    (arg2 == i) ? R.drawable.shape_pop_gridview_txt_select_bg
                                                            : R.drawable.shape_pop_gridview_txt_normal_bg));
                            myUpAdapter.gradItems().get(i).tv_good_detail_grad_content
                                    .setTextColor(pContext.getResources().getColor(
                                            (arg2 == i) ? R.color.white
                                                    : R.color.app_black));
                        }

                        myDownAdapter = new MyAdapter(GetFrashData(da.getAttr_map()
                                .getV1(), 2), pContext, 2);
                        gv_colors.setAdapter(myDownAdapter);
                        // PromptManager.ShowCustomToast(pContext, "上边的规格："
                        // + da.getAttr_map().getV1());
                        StrUtils.SetTxt(et_price, "");
                        pop_purchase_price.setVisibility(View.GONE);
                        pop_purchase_kucun.setVisibility(View.VISIBLE);
                        StrUtils.SetTxt(pop_purchase_kucun, databean.getTitle());
                        break;
                    case 2:// 下边的gradview已经点击过了===》
                        for (int i = 0; i < myUpAdapter.gradItems().size(); i++) {
                            myUpAdapter.gradItems().get(i).tv_good_detail_grad_content
                                    .setBackground(pContext
                                            .getResources()
                                            .getDrawable(
                                                    (arg2 == i) ? R.drawable.shape_pop_gridview_txt_select_bg
                                                            : R.drawable.shape_pop_gridview_txt_normal_bg));
                            myUpAdapter.gradItems().get(i).tv_good_detail_grad_content
                                    .setTextColor(pContext.getResources().getColor(
                                            (arg2 == i) ? R.color.white
                                                    : R.color.app_black));
                        }
                        //
                        IFView();
                        break;

                }

            }
        });
        gv_colors.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                BDataGood da = (BDataGood) arg0.getItemAtPosition(arg2);
                LastClickItem = arg2;
                LastClickIsUp = false;
                isSelectContentDown = true;
                DownPostion = arg2;
                switch (IsFristClick) {
                    case 0:
                    case 2:
                        IsFristClick = 2;
                        for (int i = 0; i < myDownAdapter.gradItems().size(); i++) {
                            myDownAdapter.gradItems().get(i).tv_good_detail_grad_content
                                    .setBackground(pContext
                                            .getResources()
                                            .getDrawable(
                                                    (arg2 == i) ? R.drawable.shape_pop_gridview_txt_select_bg
                                                            : R.drawable.shape_pop_gridview_txt_normal_bg));
                            myDownAdapter.gradItems().get(i).tv_good_detail_grad_content
                                    .setTextColor(pContext.getResources().getColor(
                                            (arg2 == i) ? R.color.white
                                                    : R.color.app_black));
                        }

                        myUpAdapter = new MyAdapter(GetFrashData(da.getAttr_map()
                                .getV2(), 1), pContext, 1);
                        gv_net_content.setAdapter(myUpAdapter);

//					PromptManager.ShowCustomToast(pContext, "上边的规格："
//							+ da.getAttr_map().getV2());

                        StrUtils.SetTxt(et_price, "");
                        pop_purchase_price.setVisibility(View.GONE);
                        pop_purchase_kucun.setVisibility(View.VISIBLE);
                        StrUtils.SetTxt(pop_purchase_kucun, databean.getTitle());

                        break;
                    case 1:

                        for (int i = 0; i < myDownAdapter.gradItems().size(); i++) {
                            myDownAdapter.gradItems().get(i).tv_good_detail_grad_content
                                    .setBackground(pContext
                                            .getResources()
                                            .getDrawable(
                                                    (arg2 == i) ? R.drawable.shape_pop_gridview_txt_select_bg
                                                            : R.drawable.shape_pop_gridview_txt_normal_bg));
                            myDownAdapter.gradItems().get(i).tv_good_detail_grad_content
                                    .setTextColor(pContext.getResources().getColor(
                                            (arg2 == i) ? R.color.white
                                                    : R.color.app_black));
                        }
                        IFView();
                        break;

                }

            }
        });

    }

    // 第一次展示时候获取得到两个gradview的数据 避免重复
    private List<BDataGood> FilterData(List<BDataGood> da, int type) {// type=1标识上边的
        List<String> NameStrs = new ArrayList<String>();
        List<BDataGood> datas = new ArrayList<BDataGood>();
        for (int i = 0; i < da.size(); i++) {
            if (!NameStrs.contains(1 == type ? da.get(i).getAttr_map().getV1()
                    : da.get(i).getAttr_map().getV2())) {
                NameStrs.add(1 == type ? da.get(i).getAttr_map().getV1() : da
                        .get(i).getAttr_map().getV2());
                datas.add(da.get(i));
            }
        }
        return datas;
    }

    // 点击完后刷新另外一个gradview的Ap //type=>1标识需要刷新上边的数据源////type=>2标识需要刷新下边的数据源
    private List<BDataGood> GetFrashData(String SortKey, int type) {
        List<BDataGood> datas = new ArrayList<BDataGood>();
        for (int i = 0; i < data.size(); i++) {
            if (SortKey.equals((1 != type ? data.get(i).getAttr_map().getV1()
                    : data.get(i).getAttr_map().getV2()))) {
                datas.add(data.get(i));
            }
        }
        return datas;
    }

    private void ITView() {
        tv_real_lib.setOnClickListener(this);
        tv_virtual_lib.setOnClickListener(this);
    }

    private void IPop(int width) {
        setContentView(BaseView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);

        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x4c000000);
        setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);

    }

    private void ITouch() {
        pop_conten_purchase.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View arg0, MotionEvent event) {
                int Bottom = conten_purchase_nei.getBottom();
                int Top = conten_purchase_nei.getTop();
                int Left = conten_purchase_nei.getLeft();
                int Right = conten_purchase_nei.getRight();
                int y = (int) event.getY();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > Bottom || y < Top || x < Left || x > Right) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_real_lib:
                setContentColor(TYPE_REAL_LIB);
                break;

            case R.id.tv_virtual_lib:
                setContentColor(TYPE_VIRTUAL_LIB);
                break;

            case R.id.pop_purchase_cha:
                this.dismiss();
                break;

            case R.id.btn_virtual_lib_confirm:
                confirmTo();
                break;

            case R.id.btn_virtual_lib_cancel:
                cancelTo();
                break;

        }
    }

    private void cancelTo() {
        switch (ShowType) {
            case TYPE_SHOP_GOOD_MANAGER_CAIGOU:

                break;
            case TYPE_GOOD_DETAIL_BUY:

                AddGoodBus();

                break;
            case TYPE_GOOD_DETAIL_REPLACE_SELL:
                ConnectNet(3);
                break;
        }
    }

    private void confirmTo() {
        switch (ShowType) {
            case TYPE_SHOP_GOOD_MANAGER_CAIGOU:

                break;
            case TYPE_GOOD_DETAIL_BUY:
                this.dismiss();
                // toBuy();
                break;
            case TYPE_GOOD_DETAIL_REPLACE_SELL:
                ConnectNet(2);
                break;

            default:
                break;
        }
    }

    /**
     * 全部上架
     */
    private void Add2Online(HashMap<String, String> map) {
        JSONArray OnlineAttrs = GetOnlineAttrs();

        map.put("seller_id", user_Get.getSeller_id());
        map.put("goods_id", goods_id);
        map.put("attrs", String.valueOf(OnlineAttrs));
        Arrays.sort(sell_prices);
        map.put("sell_price", sell_prices[0] + "");
        map.put("max_price", sell_prices[sell_prices.length - 1] + "");
    }

    /**
     * 购买
     */
//	private void toBuy() {
//
//		String c1 = (LastClickIsUp ? myUpAdapter : myDownAdapter)
//				.GetDataResource().get(LastClickItem).getAttr_map().getC1();
//		String c2 = (LastClickIsUp ? myUpAdapter : myDownAdapter)
//				.GetDataResource().get(LastClickItem).getAttr_map().getC2();
//		if (-1 == UpPostion) {
//			PromptManager.ShowMyToast(pContext, "请选择" + c1);
//			return;
//		}
//		if (-1 == DownPostion) {
//			PromptManager.ShowMyToast(pContext, "请选择" + c2);
//			return;
//		}
//
//		if (goods_num == 0) {
//			PromptManager.ShowMyToast(pContext, "请选择数量");
//			return;
//		}
//		PromptManager.SkipActivity((Activity) pContext, new Intent(pContext,
//				AOderBeing.class).putExtra("accountstr", getAccount()));
//
//		this.dismiss();
//
//	}

    /**
     * 获取attrs 格式：cid1:good_num1,cid2:good_num2
     */
    private String getAccount() {
        String AccountStr = "";
        String cid = (LastClickIsUp ? myUpAdapter : myDownAdapter)
                .GetDataResource().get(LastClickItem).getId();
        AccountStr = AccountStr + cid + ":" + goods_num + ",";

        if (!StrUtils.isEmpty(AccountStr)) {// 已经选择了
            AccountStr = AccountStr.substring(0, AccountStr.length() - 1);
        }
        return AccountStr;

    }

    /**
     * 添加到购物车
     */
    private void AddGoodBus() {

        String c1 = (LastClickIsUp ? myUpAdapter : myDownAdapter)
                .GetDataResource().get(LastClickItem).getAttr_map().getC1();
        String c2 = (LastClickIsUp ? myUpAdapter : myDownAdapter)
                .GetDataResource().get(LastClickItem).getAttr_map().getC2();
        if (-1 == UpPostion) {
            PromptManager.ShowMyToast(pContext, "请选择" + c1);
            return;
        }
        if (-1 == DownPostion) {
            PromptManager.ShowMyToast(pContext, "请选择" + c2);
            return;
        }
        if (goods_num == 0) {
            PromptManager.ShowMyToast(pContext, "请选择数量");
            return;
        }
        ConnectNet(1);
    }

    private void setContentColor(int type) {
        switch (type) {
            case TYPE_REAL_LIB:
                tv_real_lib.setTextColor(pContext.getResources().getColor(
                        R.color.white));
                tv_real_lib.setBackground(pContext.getResources().getDrawable(
                        R.drawable.orange));
                tv_virtual_lib.setTextColor(pContext.getResources().getColor(
                        R.color.orange));
                tv_virtual_lib.setBackground(pContext.getResources().getDrawable(
                        R.drawable.shap_rect_no_circular_bg));
                break;

            case TYPE_VIRTUAL_LIB:
                tv_virtual_lib.setTextColor(pContext.getResources().getColor(
                        R.color.white));
                tv_virtual_lib.setBackground(pContext.getResources().getDrawable(
                        R.drawable.orange));
                tv_real_lib.setTextColor(pContext.getResources().getColor(
                        R.color.orange));
                tv_real_lib.setBackground(pContext.getResources().getDrawable(
                        R.drawable.shap_rect_no_circular_bg));
                break;

            default:
                break;
        }

    }

    /**
     * 含净量的adapter
     *
     */
    class MyAdapter extends BaseAdapter {
        private List<BDataGood> mBldComment;
        private Context mContext;
        private LayoutInflater layoutInflater;
        private int Type;// 1标识上边 2标识下边
        private List<ShowGradItem> items;
        private List<Boolean> iBooleans;
        private List<ShowGradItem> Frist = new ArrayList<ShowGradItem>();
        ;

        public MyAdapter(List<BDataGood> mBldComments, Context mContext,
                         int MyType) {
            super();
            this.mBldComment = mBldComments;
            this.mContext = mContext;
            this.layoutInflater = LayoutInflater.from(mContext);
            this.Type = MyType;
            if (!DownAdapterItemClick) {
                DownAdapterItemClick = false;
            }
            if (!UpAdapterItemClick) {
                UpAdapterItemClick = false;
            }

            items = new ArrayList<ShowGradItem>();
            iBooleans = new ArrayList<Boolean>();
            for (int i = 0; i < mBldComments.size(); i++) {
                iBooleans.add(false);
                if (i == mBldComments.size() - 1)
                    continue;

                items.add(new ShowGradItem());
            }
        }

        /**
         * 暴露boole数组源
         */
        private List<Boolean> getBooleans() {
            return iBooleans;
        }

        /**
         * 暴露item值源
         *
         * @return
         */
        private List<ShowGradItem> gradItems() {
            return items;

        }

        /**
         * 暴露数据源
         *
         * @return
         */
        private List<BDataGood> GetDataResource() {
            return mBldComment;
        }

        @Override
        public int getCount() {
            return mBldComment.size();
        }

        @Override
        public Object getItem(int arg0) {
            return mBldComment.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View convertView, ViewGroup arg2) {
            ShowGradItem gradItem = null;

            if (null == convertView) {
                convertView = layoutInflater.inflate(
                        R.layout.item_good_detail_grad, null);
                if (0 == arg0) {
                    gradItem = new ShowGradItem();
                    Frist.add(gradItem);
                    if (GetDataResource().size() == 1)
                        items.add(gradItem);

                } else if (arg0 != items.size()) {
                    gradItem = gradItems().get(arg0 - 1);
                } else {
                    gradItem = gradItems().get(arg0 - 1);

                    List<ShowGradItem> da = new ArrayList<ShowGradItem>();
                    da.add(Frist.get(Frist.size() - 1));
                    da.addAll(gradItems());
                    items = da;
                }

                gradItem.tv_good_detail_grad_content = ViewHolder.get(
                        convertView, R.id.tv_good_detail_grad_content);

                convertView.setTag(gradItem);
            } else {
                gradItem = (ShowGradItem) convertView.getTag();
            }

            StrUtils.SetTxt(gradItem.tv_good_detail_grad_content,
                    1 == Type ? mBldComment.get(arg0).getAttr_map().getV1()
                            : mBldComment.get(arg0).getAttr_map().getV2());

            gradItem.tv_good_detail_grad_content
                    .setBackground(pContext
                            .getResources()
                            .getDrawable(
                                    getBooleans().get(arg0) ? R.drawable.shape_pop_gridview_txt_select_bg
                                            : R.drawable.shape_pop_gridview_txt_normal_bg));
            gradItem.tv_good_detail_grad_content.setTextColor(pContext
                    .getResources().getColor(
                            getBooleans().get(arg0) ? R.color.white
                                    : R.color.app_black));

            return convertView;
        }

        class ShowGradItem {
            TextView tv_good_detail_grad_content;
        }

    }

    /**
     *
     * @param Type
     *            ：1， 表示添加购物车，2，表示全部上架 ，3表示上架并转发
     *
     */
    private void ConnectNet(final int Type) {
        String urlString = "";
        HashMap<String, String> map = new HashMap<String, String>();
        int method = 0;
        switch (Type) {
            case 1:
                urlString = Constants.Add_to_Good_Bus;
                CommintData(map);
                // method=Method.GET;
                method = Method.POST;
                // method=Method.PUT;
                // method=Method.DELETE;
                break;

            case 2:
            case 3:
                urlString = Constants.Goods_Desell;
                Add2Online(map);
                method = Method.POST;
                break;

            default:
                break;
        }
        PromptManager.showtextLoading(pContext, pContext.getResources()
                .getString(R.string.loading));
        NHttpBaseStr mBaseStr = new NHttpBaseStr(pContext);
        mBaseStr.setPostResult(new IHttpResult<String>() {

            @Override
            public void onError(String error, int LoadType) {
                PromptManager.ShowCustomToast(pContext, error);
            }

            @Override
            public void getResult(int Code, String Msg, String Data) {

                if (200 != Code) {
                    PromptManager.ShowCustomToast(pContext, Msg);
                    return;
                }
                // Type1 表示添加购物车

                switch (Type) {// 加入购物车
                    case 1:
                        if (null != popupListener)
                            popupListener.getPopupStuta(TYPE_ADD_SHOPBUS);
                        PPurchase.this.dismiss();
                        PromptManager.ShowMyToast(pContext, "商品已添加到购物车");
                        //EventBus.getDefault().post(new BMessage(BMessage.Shop_Frash));
                        if (goods_num > 0) {//发送商品个数给购物车
                            BMessage msg = new BMessage(BMessage.Shop_Frash);
                            msg.setGood_numb(goods_num);
                            EventBus.getDefault().post(msg);
                        }

                        // 我的订单--订单详情需要finish
                        // EventBus.getDefault().post(new
                        // BMessage(BMessage.Tage_Apply_Refund));
                        // TODO skip
                        break;

                    case 2:// 直接上架
                        if (null != popupListener)
                            popupListener.getPopupStuta(TYPE_ADD_ONLINE);
                        PPurchase.this.dismiss();
                        PromptManager.ShowMyToast(pContext, "上架成功");
                        // TODO skip
                        break;
                    case 3:// 上架后转发
                        PPurchase.this.dismiss();
                        PromptManager.ShowMyToast(pContext, "上架成功");
                        BNew bnew = new BNew();
                        bnew.setTitle(databean.getSeller_name());
                        bnew.setContent(databean.getTitle());
                        bnew.setShare_log(StrUtils.isEmpty(databean.getCover()) ? databean.getGoods_info().getIntro().get(0) : databean.getCover());
                        bnew.setShare_url(databean.getGoods_url());
                        PShowShare showShare = new PShowShare(pContext, bnew);
                        showShare.SetShareListener(new PShowShare.ShowShareInterListener() {
                            @Override
                            public void GetResultType(int ResultType) {
                                switch (ResultType) {
                                    case 3:
                                        if (databean.getGoods_info().getRtype().equals("0")) {// 照片
                                            BShowShare MyBShowShare = new BShowShare();
                                            MyBShowShare.setImgarr(databean.getGoods_info().getIntro());
                                            MyBShowShare.setGoods_id(databean.getId());
                                            MyBShowShare.setIntro(databean.getTitle());
                                            PromptManager
                                                    .SkipActivity(
                                                            pActivity,
                                                            new Intent(pActivity, ShowSelectPic.class).putExtra(
                                                                    ShowSelectPic.Key_Data,
                                                                    MyBShowShare));

                                        } else {// 视频
                                            BShowShare MyVidoBShowShare = new BShowShare();
                                            MyVidoBShowShare.setVido_pre_url(StrUtils.isEmpty(databean.getCover()) ? databean.getGoods_info().getIntro().get(0) : databean.getCover());
                                            MyVidoBShowShare.setVido_Vid(databean.getGoods_info().getVid());
                                            MyVidoBShowShare.setIntro(databean.getTitle());
                                            MyVidoBShowShare.setGoods_id(databean.getId());

                                            PromptManager.SkipActivity(
                                                    pActivity,
                                                    new Intent(pActivity, AGoodVidoShare.class)
                                                            .putExtra(AGoodVidoShare.Key_VidoFromShow,
                                                                    true).putExtra(
                                                            AGoodVidoShare.Key_VidoData,
                                                            MyVidoBShowShare));

                                        }
                                        break;


                                }
                            }
                        });
                        showShare.showAtLocation(BaseView, Gravity.CENTER, 0, 0);

//                        BLShow dComment = StrUtils.BDtoBL_BLShow(databean);
//                        if (dComment.getIs_type().equals("0")) {// 照片
//                            PromptManager
//                                    .SkipActivity(
//                                            pActivity,
//                                            new Intent(pActivity, ShowSelectPic.class).putExtra(
//                                                    ShowSelectPic.Key_Data,
//                                                    dComment));
//
//                        } else {// 视频
//                            PromptManager
//                                    .SkipActivity(
//                                            pActivity,
//                                            new Intent(pActivity,
//                                                    AGoodVidoShare.class)
//                                                    .putExtra(
//                                                            AGoodVidoShare.Key_VidoFromShow,
//                                                            true)
//                                                    .putExtra(
//                                                            AGoodVidoShare.Key_VidoData,
//                                                            dComment));
//
//                        }
                        break;
                    default:
                        break;
                }

            }
        });
        mBaseStr.getData(urlString, map, method);
    }

    private void CommintData(HashMap<String, String> map) {

        map.put("member_id", user_Get.getId());
        map.put("channel", MyChannel);
        map.put("inventory_from", "1");
        map.put("goods_num", goods_num + "");

        String goods_attr_name = (LastClickIsUp ? myUpAdapter : myDownAdapter)
                .GetDataResource().get(LastClickItem).getAttr_name();

        String goods_attr = (LastClickIsUp ? myUpAdapter : myDownAdapter)
                .GetDataResource().get(LastClickItem).getId();

        map.put("goods_id", goods_id);
        map.put("goods_attr_name", goods_attr_name);
        map.put("goods_attr", goods_attr);
    }

    private class MakeMoneyAdapter extends BaseAdapter {
        private Context mycContext;
        private LayoutInflater inflater;
        private int ResourceId;
        private List<BLComment> datas = new ArrayList<BLComment>();
        private List<MakeMoneyItem> Views = new ArrayList<MakeMoneyItem>();
        private List<BDComment> make_money_beans;

        public MakeMoneyAdapter(Context mycContext, int resourceId,
                                List<BDComment> make_money_beans) {
            super();
            this.mycContext = mycContext;
            this.inflater = LayoutInflater.from(mycContext);
            this.ResourceId = resourceId;
            this.make_money_beans = make_money_beans;
        }

        /**
         * 刷新万数据后立马调用
         */
        public void IColor() {
            if (getCount() > 0) {
                Views.get(0).tv_ppurchase_make_how_much_item
                        .setBackground(pContext.getResources().getDrawable(
                                R.drawable.shape_pop_gridview_txt_select_bg));
                Views.get(0).tv_ppurchase_make_how_much_item
                        .setTextColor(pContext.getResources().getColor(
                                R.color.white));
            }
        }

        @Override
        public int getCount() {
            return make_money_beans.size();
        }

        @Override
        public Object getItem(int arg0) {
            return make_money_beans.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }

        @Override
        public View getView(int arg0, View convertView, ViewGroup arg2) {
            MakeMoneyItem myItem = null;
            if (convertView == null) {
                myItem = new MakeMoneyItem();
                convertView = inflater.inflate(ResourceId, null);
                myItem.tv_ppurchase_make_how_much_item = (TextView) convertView
                        .findViewById(R.id.tv_ppurchase_make_how_much_item);
                convertView.setTag(myItem);
                Views.add(myItem);
                if (0 == arg0)// TODO测试用的 后期需要删除 仅供效果展示
                    IColor();
            } else {
                myItem = (MakeMoneyItem) convertView.getTag();
            }

            BDComment bdComment = make_money_beans.get(arg0);
            String data = "";
            switch (bdComment.getMakeMoneyType()) {
                case 1:
                    data = bdComment.getMakeMoney() + "%";
                    break;

                case 2:
                    data = StrUtils.SetTextForMony(bdComment.getMakeMoney());
                    break;

            }
            StrUtils.SetTxt(myItem.tv_ppurchase_make_how_much_item, data);
            myItem.tv_ppurchase_make_how_much_item
                    .setOnClickListener(new HorizontalItemClikListener(Views,
                            arg0, make_money_beans.get(arg0)));// TODO后期需要传入正确的BLComment数据bean
            // 目前为null
            return convertView;
        }

        class MakeMoneyItem {
            TextView tv_ppurchase_make_how_much_item;
        }

        class HorizontalItemClikListener implements OnClickListener {
            private List<MakeMoneyItem> Viewsdata = new ArrayList<MakeMoneyItem>();// 所有的view
            private int Postion;// 记录点击的位置
            private BDComment blComment;// 记录品牌的数据bean

            public HorizontalItemClikListener(List<MakeMoneyItem> viewsdata,
                                              int postion, BDComment data) {
                super();
                Viewsdata = viewsdata;
                Postion = postion;
                blComment = data;

            }

            @Override
            public void onClick(View arg0) {
                for (int i = 0; i < Viewsdata.size(); i++) {
                    Viewsdata.get(i).tv_ppurchase_make_how_much_item
                            .setBackground(pContext
                                    .getResources()
                                    .getDrawable(
                                            Postion == i ? R.drawable.shape_pop_gridview_txt_select_bg
                                                    : R.drawable.shape_pop_gridview_txt_normal_bg));
                    Viewsdata.get(i).tv_ppurchase_make_how_much_item
                            .setTextColor(pContext.getResources().getColor(
                                    Postion == i ? R.color.white
                                            : R.color.app_black));
                    if (Postion == i) {
                        mPostion = i;
                    }

                }

                System.out.println(mPostion);
                IsFristClick = 0;
                // 记录是否上边下边已经点击过了
                UpPostion = -1;
                DownPostion = -1;
                // 最后一次的点击位置
                LastClickItem = 0;
                // myUpAdapter.notifyDataSetChanged();
                // myDownAdapter.notifyDataSetChanged();
                up_Data = FilterData(data, 1);
                myUpAdapter = new MyAdapter(up_Data, pContext, 1);
                Down_Data = FilterData(data, 2);
                myDownAdapter = new MyAdapter(Down_Data, pContext, 2);
                gv_net_content.setAdapter(myUpAdapter);
                gv_colors.setAdapter(myDownAdapter);
                et_price.setText("");
                pop_purchase_price.setVisibility(View.GONE);
                pop_purchase_kucun.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(pop_purchase_kucun, databean.getTitle());

            }
        }
    }

	/*
     * (non-Javadoc)
	 * 
	 * @see io.vtown.WeiTangApp.comment.view.AddAndSubView.OnNumChangeListener#
	 * onNumChange(android.view.View, int) 加减数量控件的监听事件
	 */

    @Override
    public void onNumChange(View view, int num) {
        Log.e("myMessage", "*****************onNumChange**********************"
                + num);
        goods_num = num;
    }

    /**
     * @author Yihuihua 监听pop执行结果
     */
    public interface OnPopupStutaChangerListener {
        public void getPopupStuta(int stuta);
    }

    private OnPopupStutaChangerListener popupListener = null;

    public void setOnPopupStutaChangerListener(
            OnPopupStutaChangerListener Listener) {
        popupListener = Listener;
    }

}
