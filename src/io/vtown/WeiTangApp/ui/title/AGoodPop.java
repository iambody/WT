package io.vtown.WeiTangApp.ui.title;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BGoodDetail;
import io.vtown.WeiTangApp.bean.bcomment.easy.BShowShare;
import io.vtown.WeiTangApp.bean.bcomment.easy.gooddetail.BDataGood;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.AddAndSubView;
import io.vtown.WeiTangApp.comment.view.ShowSelectPic;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.listview.HorizontalListView;
import io.vtown.WeiTangApp.comment.view.pop.PShowShare;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.AGoodVidoShare;

/**
 * Created by Yihuihua on 2016/9/21.
 */

public class AGoodPop extends ATitleBase implements AddAndSubView.OnNumChangeListener {
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
    private View mRootView;
    private TextView tv_gray_layout;
    private ScrollView good_content_sv;
    private LinearLayout ll_return_and_integral;
    private TextView tv_return_money;
    private TextView tv_good_integral;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_good_pop);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        mRootView = LayoutInflater.from(BaseContext).inflate(R.layout.activity_good_pop, null);
       overridePendingTransition(R.anim.slide_in,0);

        SetTitleHttpDataLisenter(this);
        IData();
        IView();
    }

    @Override
    protected void InitTile() {
        SetTitleTxt("");
    }

    /**
     * @param Type ：1， 表示添加购物车，2，表示全部上架 ，3表示上架并转发
     */
    private void ConnectNet(final int Type) {
        PromptManager.showLoading(BaseContext);
        String urlString = "";
        HashMap<String, String> map = new HashMap<String, String>();
        int method = 0;
        switch (Type) {
            case 1:
                urlString = Constants.Add_to_Good_Bus;
                CommintData(map);
                // method=Method.GET;
                method = Request.Method.POST;
                // method=Method.PUT;
                // method=Method.DELETE;
                break;

            case 2:
            case 3:
                urlString = Constants.Goods_Desell;
                Add2Online(map);
                method = Request.Method.POST;
                break;

            default:
                break;
        }
        FBGetHttpData(map, urlString, method, Type, LOAD_INITIALIZE);
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

    private void IData() {
        Intent intent = getIntent();
        databean = (BGoodDetail)intent.getSerializableExtra("good_info");
        goods_id = intent.getStringExtra("GoodsId");
        ShowType = intent.getIntExtra("Show_type",0);
        IsCaiGou = intent.getBooleanExtra("IsCaiGou",false);
        user_Get = Spuit.User_Get(BaseContext);
        MyChannel = IsCaiGou ? "CG" : "PT";

        if(databean == null){
            return;
        }
        this.data = databean.getGoods_attr();
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

    private void IView() {
        pop_show_shangjia_cha = (ImageView) findViewById(R.id.pop_purchase_cha);
        tv_gray_layout = (TextView) findViewById(R.id.tv_gray_layout);
        tv_gray_layout.setOnClickListener(this);
        pop_show_shangjia_cha.setOnClickListener(this);
        gv_net_content = (CompleteGridView) findViewById(R.id.pop_purchase_grid);
        gv_colors = (CompleteGridView) findViewById(R.id.gv_colors);
        good_content_sv = (ScrollView) findViewById(R.id.good_content_sv);
        good_content_sv.smoothScrollTo(0, 20);
        ll_pop_good_icon_price_store = (LinearLayout) findViewById(R.id.ll_pop_good_icon_price_store);
        iv_pop_goods_icon = (ImageView) findViewById(R.id.iv_pop_goods_icon);
        ll_return_and_integral = (LinearLayout) findViewById(R.id.ll_return_and_integral);
        tv_return_money = (TextView) findViewById(R.id.tv_return_money);
        tv_good_integral = (TextView) findViewById(R.id.tv_good_integral);

        ImageLoaderUtil.Load2(databean.getCover(), iv_pop_goods_icon,
                R.drawable.error_iv2);
        // 第一次弹出页面时，头布局隐藏

        pop_purchase_price = (TextView) findViewById(R.id.pop_purchase_price);
        pop_purchase_kucun = (TextView) findViewById(R.id.pop_purchase_kucun);
        pop_purchase_price.setVisibility(View.GONE);
        ll_return_and_integral.setVisibility(View.GONE);

        // 默认显示商品的名字
        StrUtils.SetTxt(pop_purchase_kucun, databean.getTitle());

        tv_good_standard_lable1 = (TextView) findViewById(R.id.tv_good_standard_lable1);
        tv_good_standard_lable2 = (TextView) findViewById(R.id.tv_good_standard_lable2);

        ll_make_how_much = (LinearLayout) findViewById(R.id.ll_make_how_much);
        hl_make_how_much = (HorizontalListView) findViewById(R.id.hl_make_how_much);

        StrUtils.SetTxt(tv_good_standard_lable1, data.get(0).getAttr_map()
                .getC1());
        StrUtils.SetTxt(tv_good_standard_lable2, data.get(0).getAttr_map()
                .getC2());

        aasv_add_sub = (AddAndSubView) findViewById(R.id.aasv_add_sub);
//        if (IsCaiGou) {// 采购
//            aasv_add_sub.setNum(10);
//            aasv_add_sub.SetMinNumber(10);
//        } else {
        aasv_add_sub.setNum(1);
        aasv_add_sub.SetMinNumber(1);
//        }
        goods_num = aasv_add_sub.getNum();
        tv_real_lib = (TextView) findViewById(R.id.tv_real_lib);
        tv_virtual_lib = (TextView) findViewById(R.id.tv_virtual_lib);
        iv_help = (ImageView) findViewById(R.id.iv_help);
        ll_real_virtual = (LinearLayout) findViewById(R.id.ll_real_virtual);
        et_price = (EditText) findViewById(R.id.pop_purchase_ed);
        ll_price = (LinearLayout) findViewById(R.id.ll_price);

        ll_counts = (LinearLayout) findViewById(R.id.ll_counts);

        btn_virtual_lib_confirm = (Button) findViewById(R.id.btn_virtual_lib_confirm);
        btn_virtual_lib_cancel = (Button) findViewById(R.id.btn_virtual_lib_cancel);

        btn_virtual_lib_confirm.setOnClickListener(this);
        btn_virtual_lib_cancel.setOnClickListener(this);
        aasv_add_sub.setOnNumChangeListener(this);

        switch (ShowType) {
            case TYPE_SHOP_GOOD_MANAGER_CAIGOU:
                ll_real_virtual.setVisibility(View.GONE);
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
                btn_virtual_lib_cancel.setText("上架并分享");
                break;
            case 113:

                break;
        }

        makeMoneyAdapter = new MakeMoneyAdapter(R.layout.item_ppurchase_make_how_money, make_money_beans);
        hl_make_how_much.setAdapter(makeMoneyAdapter);

        // myUpAdapter = new MyAdapter(FilterData(datBlComment2.getGoodinfo()
        // .getAttr(), 1), pContext, 1);
        // myDownAdapter = new MyAdapter(FilterData(datBlComment2.getGoodinfo()
        // .getAttr(), 2), pContext, 2);

        up_Data = FilterData(data, 1);
        myUpAdapter = new MyAdapter(up_Data, 1);
        Down_Data = FilterData(data, 2);
        myDownAdapter = new MyAdapter(Down_Data, 2);

        // 上边
        gv_net_content.setNumColumns(3);
        gv_net_content.setHorizontalSpacing(DimensionPixelUtil.dip2px(BaseContext,
                5));
        gv_net_content.setVerticalSpacing(DimensionPixelUtil
                .dip2px(BaseContext, 5));
        // 下边
        gv_colors.setNumColumns(3);
        gv_colors.setHorizontalSpacing(DimensionPixelUtil.dip2px(BaseContext, 5));
        gv_colors.setVerticalSpacing(DimensionPixelUtil.dip2px(BaseContext, 5));

        gv_net_content.setAdapter(myUpAdapter);
        gv_colors.setAdapter(myDownAdapter);
        gv_net_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                                    .setBackground(getResources()
                                            .getDrawable(
                                                    (arg2 == i) ? R.drawable.shape_pop_gridview_txt_select_bg
                                                            : R.drawable.shape_pop_gridview_txt_normal_bg));
                            myUpAdapter.gradItems().get(i).tv_good_detail_grad_content
                                    .setTextColor(getResources().getColor(
                                            (arg2 == i) ? R.color.white
                                                    : R.color.app_black));
                        }

                        myDownAdapter = new MyAdapter(GetFrashData(da.getAttr_map()
                                .getV1(), 2), 2);
                        gv_colors.setAdapter(myDownAdapter);
                        // PromptManager.ShowCustomToast(pContext, "上边的规格："
                        // + da.getAttr_map().getV1());
                        StrUtils.SetTxt(et_price, "");
                        pop_purchase_price.setVisibility(View.GONE);
                        ll_return_and_integral.setVisibility(View.GONE);
                        pop_purchase_kucun.setVisibility(View.VISIBLE);
                        StrUtils.SetTxt(pop_purchase_kucun, databean.getTitle());
                        break;
                    case 2:// 下边的gradview已经点击过了===》
                        for (int i = 0; i < myUpAdapter.gradItems().size(); i++) {
                            myUpAdapter.gradItems().get(i).tv_good_detail_grad_content
                                    .setBackground(getResources()
                                            .getDrawable(
                                                    (arg2 == i) ? R.drawable.shape_pop_gridview_txt_select_bg
                                                            : R.drawable.shape_pop_gridview_txt_normal_bg));
                            myUpAdapter.gradItems().get(i).tv_good_detail_grad_content
                                    .setTextColor(getResources().getColor(
                                            (arg2 == i) ? R.color.white
                                                    : R.color.app_black));
                        }
                        //
                        IFView();
                        break;

                }

            }
        });
        gv_colors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                                    .setBackground(getResources()
                                            .getDrawable(
                                                    (arg2 == i) ? R.drawable.shape_pop_gridview_txt_select_bg
                                                            : R.drawable.shape_pop_gridview_txt_normal_bg));
                            myDownAdapter.gradItems().get(i).tv_good_detail_grad_content
                                    .setTextColor(getResources().getColor(
                                            (arg2 == i) ? R.color.white
                                                    : R.color.app_black));
                        }

                        myUpAdapter = new MyAdapter(GetFrashData(da.getAttr_map()
                                .getV2(), 1), 1);
                        gv_net_content.setAdapter(myUpAdapter);

//					PromptManager.ShowCustomToast(pContext, "上边的规格："
//							+ da.getAttr_map().getV2());

                        StrUtils.SetTxt(et_price, "");
                        pop_purchase_price.setVisibility(View.GONE);
                        ll_return_and_integral.setVisibility(View.GONE);
                        pop_purchase_kucun.setVisibility(View.VISIBLE);
                        StrUtils.SetTxt(pop_purchase_kucun, databean.getTitle());

                        break;
                    case 1:

                        for (int i = 0; i < myDownAdapter.gradItems().size(); i++) {
                            myDownAdapter.gradItems().get(i).tv_good_detail_grad_content
                                    .setBackground(getResources()
                                            .getDrawable(
                                                    (arg2 == i) ? R.drawable.shape_pop_gridview_txt_select_bg
                                                            : R.drawable.shape_pop_gridview_txt_normal_bg));
                            myDownAdapter.gradItems().get(i).tv_good_detail_grad_content
                                    .setTextColor(getResources().getColor(
                                            (arg2 == i) ? R.color.white
                                                    : R.color.app_black));
                        }
                        IFView();
                        break;

                }

            }
        });

    }

    /**
     * 刷新布局
     */
    private void IFView() {

        BDataGood blComment = (LastClickIsUp ? myUpAdapter : myDownAdapter)
                .GetDataResource().get(LastClickItem);

        if (blComment != null) {
            pop_purchase_price.setVisibility(View.VISIBLE);
            if(TYPE_GOOD_DETAIL_BUY == ShowType && "1".equals(databean.getIs_fee())){
                ll_return_and_integral.setVisibility(View.VISIBLE);
                StrUtils.SetColorsTxt(BaseContext,tv_return_money,R.color.app_fen,"返佣金额：",StrUtils.SetTextForMony(blComment.getFee())+"元");
                StrUtils.SetColorsTxt(BaseContext,tv_good_integral,R.color.app_fen,"获得积分：",blComment.getScore()+"分");
            }else{
                ll_return_and_integral.setVisibility(View.GONE);
            }
            int _sell_price = Integer.parseInt(blComment.getSell_price())*goods_num;
            String format_price = String.format("%1$s元",
                    StrUtils.SetTextForMony(_sell_price+""));
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

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        if (200 != Code) {
            PromptManager.ShowCustomToast(BaseContext, Msg);
            return;
        }

        switch (Data.getHttpResultTage()) {
            case 1:
//                if (null != popupListener)
//                    popupListener.getPopupStuta(TYPE_ADD_SHOPBUS);
                EventBus.getDefault().post(new BMessage(TYPE_ADD_SHOPBUS));
                this.finish();
                PromptManager.ShowMyToast(BaseContext, "商品已添加到购物车");
                //EventBus.getDefault().post(new BMessage(BMessage.Shop_Frash));
                if (goods_num > 0) {//发送商品个数给购物车
                    BMessage msg = new BMessage(BMessage.Shop_Frash);
                    msg.setGood_numb(goods_num);
                    EventBus.getDefault().post(msg);
                }
                break;

            case 2:
               // if (null != popupListener)
                //    popupListener.getPopupStuta(TYPE_ADD_ONLINE);
                EventBus.getDefault().post(new BMessage(TYPE_ADD_ONLINE));
                this.finish();
                PromptManager.ShowMyToast(BaseContext, "上架成功");
                break;

            case 3:
                this.finish();
                PromptManager.ShowMyToast(BaseContext, "上架成功");
                BNew bnew = new BNew();
                bnew.setShare_title(getResources().getString(R.string.share_app) + "  " + databean.getSeller_name());
                bnew.setShare_content(getResources().getString(R.string.share_app) + "  " + databean.getTitle());
                bnew.setShare_log(StrUtils.isEmpty(databean.getCover()) ? databean.getGoods_info().getIntro().get(0) : databean.getCover());
                bnew.setShare_url(databean.getGoods_url());
                PShowShare showShare = new PShowShare(BaseContext, bnew);
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
                                                    BaseActivity,
                                                    new Intent(BaseContext, ShowSelectPic.class).putExtra(
                                                            ShowSelectPic.Key_Data,
                                                            MyBShowShare));

                                } else {// 视频
                                    BShowShare MyVidoBShowShare = new BShowShare();
                                    MyVidoBShowShare.setVido_pre_url(StrUtils.isEmpty(databean.getCover()) ? databean.getGoods_info().getIntro().get(0) : databean.getCover());
                                    MyVidoBShowShare.setVido_Vid(databean.getGoods_info().getVid());
                                    MyVidoBShowShare.setIntro(databean.getTitle());
                                    MyVidoBShowShare.setGoods_id(databean.getId());

                                    PromptManager.SkipActivity(
                                            BaseActivity,
                                            new Intent(BaseContext, AGoodVidoShare.class)
                                                    .putExtra(AGoodVidoShare.Key_VidoFromShow,
                                                            true).putExtra(
                                                    AGoodVidoShare.Key_VidoData,
                                                    MyVidoBShowShare));

                                }
                                break;


                        }
                    }
                });
                showShare.showAtLocation(mRootView, Gravity.CENTER, 0, 0);
                break;
        }
    }


    @Override
    protected void DataError(String error, int LoadType) {

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
            PromptManager.ShowMyToast(BaseContext, "请选择" + c1);
            return;
        }
        if (-1 == DownPostion) {
            PromptManager.ShowMyToast(BaseContext, "请选择" + c2);
            return;
        }
        if (goods_num == 0) {
            PromptManager.ShowMyToast(BaseContext, "请选择数量");
            return;
        }
        ConnectNet(1);
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

            case R.id.pop_purchase_cha:
                overridePendingTransition(0,R.anim.slide_out);
                this.finish();
                break;

            case R.id.btn_virtual_lib_confirm:
                confirmTo();
                break;

            case R.id.btn_virtual_lib_cancel:
                cancelTo();
                break;

            case R.id.tv_gray_layout:
                this.finish();
                break;

        }
    }

    private void cancelTo() {
        switch (ShowType) {
            case TYPE_SHOP_GOOD_MANAGER_CAIGOU:


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

                this.finish();
                // toBuy();
                break;
            case TYPE_GOOD_DETAIL_REPLACE_SELL:
                ConnectNet(2);
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

    @Override
    public void onNumChange(View view, int num) {
        goods_num = num;
        mHandler.sendEmptyMessage(0);
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            IFView();
        }
    };

    /**
     * 含净量的adapter
     */
    class MyAdapter extends BaseAdapter {
        private List<BDataGood> mBldComment;

        private LayoutInflater layoutInflater;
        private int Type;// 1标识上边 2标识下边
        private List<MyAdapter.ShowGradItem> items;
        private List<Boolean> iBooleans;
        private List<MyAdapter.ShowGradItem> Frist = new ArrayList<MyAdapter.ShowGradItem>();
        ;

        public MyAdapter(List<BDataGood> mBldComments,
                         int MyType) {
            super();
            this.mBldComment = mBldComments;

            this.layoutInflater = LayoutInflater.from(BaseContext);
            this.Type = MyType;
            if (!DownAdapterItemClick) {
                DownAdapterItemClick = false;
            }
            if (!UpAdapterItemClick) {
                UpAdapterItemClick = false;
            }

            items = new ArrayList<MyAdapter.ShowGradItem>();
            iBooleans = new ArrayList<Boolean>();
            for (int i = 0; i < mBldComments.size(); i++) {
                iBooleans.add(false);
                if (i == mBldComments.size() - 1)
                    continue;

                items.add(new MyAdapter.ShowGradItem());
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
        private List<MyAdapter.ShowGradItem> gradItems() {
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
            MyAdapter.ShowGradItem gradItem = null;

            if (null == convertView) {
                convertView = layoutInflater.inflate(
                        R.layout.item_good_detail_grad, null);
                if (0 == arg0) {
                    gradItem = new MyAdapter.ShowGradItem();
                    Frist.add(gradItem);
                    if (GetDataResource().size() == 1)
                        items.add(gradItem);

                } else if (arg0 != items.size()) {
                    gradItem = gradItems().get(arg0 - 1);
                } else {
                    gradItem = gradItems().get(arg0 - 1);

                    List<MyAdapter.ShowGradItem> da = new ArrayList<MyAdapter.ShowGradItem>();
                    da.add(Frist.get(Frist.size() - 1));
                    da.addAll(gradItems());
                    items = da;
                }

                gradItem.tv_good_detail_grad_content = ViewHolder.get(
                        convertView, R.id.tv_good_detail_grad_content);

                convertView.setTag(gradItem);
            } else {
                gradItem = (MyAdapter.ShowGradItem) convertView.getTag();
            }

            StrUtils.SetTxt(gradItem.tv_good_detail_grad_content,
                    1 == Type ? mBldComment.get(arg0).getAttr_map().getV1()
                            : mBldComment.get(arg0).getAttr_map().getV2());

            gradItem.tv_good_detail_grad_content
                    .setBackground(
                            getResources()
                                    .getDrawable(
                                            getBooleans().get(arg0) ? R.drawable.shape_pop_gridview_txt_select_bg
                                                    : R.drawable.shape_pop_gridview_txt_normal_bg));
            gradItem.tv_good_detail_grad_content.setTextColor(getResources().getColor(
                    getBooleans().get(arg0) ? R.color.white
                            : R.color.app_black));

            return convertView;
        }

        class ShowGradItem {
            TextView tv_good_detail_grad_content;
        }

    }

    private class MakeMoneyAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private int ResourceId;
        private List<BLComment> datas = new ArrayList<BLComment>();
        private List<MakeMoneyAdapter.MakeMoneyItem> Views = new ArrayList<MakeMoneyAdapter.MakeMoneyItem>();
        private List<BDComment> make_money_beans;

        public MakeMoneyAdapter(int resourceId,
                                List<BDComment> make_money_beans) {
            super();

            this.inflater = LayoutInflater.from(BaseContext);
            this.ResourceId = resourceId;
            this.make_money_beans = make_money_beans;
        }

        /**
         * 刷新万数据后立马调用
         */
        public void IColor() {
            if (getCount() > 0) {
                Views.get(0).tv_ppurchase_make_how_much_item
                        .setBackground(getResources().getDrawable(
                                R.drawable.shape_pop_gridview_txt_select_bg));
                Views.get(0).tv_ppurchase_make_how_much_item
                        .setTextColor(getResources().getColor(
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
            MakeMoneyAdapter.MakeMoneyItem myItem = null;
            if (convertView == null) {
                myItem = new MakeMoneyAdapter.MakeMoneyItem();
                convertView = inflater.inflate(ResourceId, null);
                myItem.tv_ppurchase_make_how_much_item = (TextView) convertView
                        .findViewById(R.id.tv_ppurchase_make_how_much_item);
                convertView.setTag(myItem);
                Views.add(myItem);
                if (0 == arg0)// TODO测试用的 后期需要删除 仅供效果展示
                    IColor();
            } else {
                myItem = (MakeMoneyAdapter.MakeMoneyItem) convertView.getTag();
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
                    .setOnClickListener(new MakeMoneyAdapter.HorizontalItemClikListener(Views,
                            arg0, make_money_beans.get(arg0)));// TODO后期需要传入正确的BLComment数据bean
            // 目前为null
            return convertView;
        }

        class MakeMoneyItem {
            TextView tv_ppurchase_make_how_much_item;
        }

        class HorizontalItemClikListener implements View.OnClickListener {
            private List<MakeMoneyAdapter.MakeMoneyItem> Viewsdata = new ArrayList<MakeMoneyAdapter.MakeMoneyItem>();// 所有的view
            private int Postion;// 记录点击的位置
            private BDComment blComment;// 记录品牌的数据bean

            public HorizontalItemClikListener(List<MakeMoneyAdapter.MakeMoneyItem> viewsdata,
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
                            .setBackground(getResources()
                                    .getDrawable(
                                            Postion == i ? R.drawable.shape_pop_gridview_txt_select_bg
                                                    : R.drawable.shape_pop_gridview_txt_normal_bg));
                    Viewsdata.get(i).tv_ppurchase_make_how_much_item
                            .setTextColor(getResources().getColor(
                                    Postion == i ? R.color.white
                                            : R.color.app_black));
                    if (Postion == i) {
                        mPostion = i;
                    }

                }
                IsFristClick = 0;
                // 记录是否上边下边已经点击过了
                UpPostion = -1;
                DownPostion = -1;
                // 最后一次的点击位置
                LastClickItem = 0;
                // myUpAdapter.notifyDataSetChanged();
                // myDownAdapter.notifyDataSetChanged();
                up_Data = FilterData(data, 1);
                myUpAdapter = new MyAdapter(up_Data, 1);
                Down_Data = FilterData(data, 2);
                myDownAdapter = new MyAdapter(Down_Data, 2);
                gv_net_content.setAdapter(myUpAdapter);
                gv_colors.setAdapter(myDownAdapter);
                et_price.setText("");
                pop_purchase_price.setVisibility(View.GONE);
                ll_return_and_integral.setVisibility(View.GONE);
                pop_purchase_kucun.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(pop_purchase_kucun, databean.getTitle());

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0,R.anim.slide_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(0,R.anim.slide_out);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,R.anim.slide_out);
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
