package io.vtown.WeiTangApp.ui.comment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.easy.mainsort.BSortCategory;
import io.vtown.WeiTangApp.bean.bcomment.easy.mainsort.BSortRang;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.net.NHttpBaseStr;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.event.interf.IHttpResult;
import io.vtown.WeiTangApp.ui.ABase;

/**
 * Created by datutu on 2016/11/1.
 */

public class AMianSort extends ABase {
    @BindView(R.id.pop_maitab_sort_type)
    TextView popMaitabSortType;
    @BindView(R.id.pop_maitab_sort_price)
    TextView popMaitabSortPrice;
    @BindView(R.id.pop_maitab_sort_jifen)
    TextView popMaitabSortJifen;
    @BindView(R.id.pop_maitab_sort_branc)
    TextView popMaitabSortBranc;
    @BindView(R.id.pop_maitab_queding)
    TextView popMaitabQueding;
    @BindView(R.id.pop_maitab_reset)
    TextView popMaitabReset;
    @BindView(R.id.pop_maitab_cancle)
    TextView popMaitabCancle;
    @BindView(R.id.pop_maitab_sort_ls)
    CompleteListView popMaitabSortLs;
    @BindView(R.id.pop_maitab_sort_brand_gridview)
    CompleteGridView popMaitabSortBrandGridview;
    @BindView(R.id.pop_maitab_rangprice_ls)
    CompleteListView popMaitabRangLs;
    @BindView(R.id.pop_maitab_rangscore_ls)
    CompleteListView popMaitabRangscoreLs;
    @BindView(R.id.pop_sort_rang_price_min_ed)
    EditText popSortRangPriceMinEd;//价格自定义最小输入框
    @BindView(R.id.pop_sort_rang_price_max_ed)
    EditText popSortRangPriceMaxEd;//价格自定义最大输入框
    @BindView(R.id.pop_sort_rang_price_bt)
    TextView popSortRangPriceBt;//价格自定义 确定按钮
    @BindView(R.id.pop_sort_rang_price_ed_lay)
    LinearLayout popSortRangPriceEdLay;//价格自定义布局view
    @BindView(R.id.pop_sort_rang_score_min_ed)
    EditText popSortRangScoreMinEd;//积分自定义最小输入框
    @BindView(R.id.pop_sort_rang_score_max_ed)
    EditText popSortRangScoreMaxEd;//积分自定义最大输入框
    @BindView(R.id.pop_sort_rang_score_bt)
    TextView popSortRangScoreBt;//积分自定义 确定按钮
    @BindView(R.id.pop_sort_rang_score_ed_lay)
    LinearLayout popSortRangScoreEdLay;//积分自定义布局
    @BindView(R.id.pop_sort_rang_price_tag)
    TextView popSortRangPriceTag;//价格的显示标题
    @BindView(R.id.pop_sort_rang_score_tag)
    TextView popSortRangScoreTag;//积分的显示标题


    //我左侧的文本控制列表
    private List<TextView> MyLeft;
    //我的二级ap
    private MySortAp mySortAp;
    private MyBrandAp myBrnadAp;
    private MyRangAp myRangAp;
    private MyRangAp myRangScoreAp;
    //我左侧上边的位置标示
    private int LeftPostion = 0;
    //记录下二级类别被选中后的

    //排序Fragment页面带过来的数据
    private String catoryid;
    private boolean IsReSet;
    //*******************需要直接初始化view还原数据时fragment带来的参数********************************
    private boolean IsRecover;
    private String SecondSortId;
    private BSortRang PriceSort;
    private BSortRang ScoreSort;
    private String BrandName;
    //获取位置
    private int SecondSortId_Postion;
    private int PriceSort_Postion;
    private int ScoreSort_Postion;
    private int BrandName_Postion;

    //自定义区间的保存数据
    private boolean IsZiDingYiPrice;//是否自定义价格
    private boolean IsZiDingYiScore;//是否自定义积分

    private BSortRang ZiDingYiPrice;
    private BSortRang ZiDingYiScore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_maintab_sort);
        ButterKnife.bind(this);
        IBund();
        IBase();
        IData();
    }

    /**
     * 根据情况获取数据 默认刚进来获取的数据时
     */
    private void IData() {
        //获取分类列表

    }

    private void IBund() {
        catoryid = getIntent().getStringExtra("catoryid");
        IsRecover = getIntent().getBooleanExtra("IsInItView", false);
        if (IsRecover) {
            SecondSortId = getIntent().getStringExtra("SecondSortId");
            PriceSort = (BSortRang) getIntent().getSerializableExtra("PriceSort");
            ScoreSort = (BSortRang) getIntent().getSerializableExtra("ScoreSort");
            BrandName = getIntent().getStringExtra("BrandName");
            //开始获取对应的位置
            SecondSortId_Postion = getIntent().getIntExtra("SecondSortId_Postion", -1);//getIntent().getIntExtra("SecondSortId_Postion",-1);
            PriceSort_Postion = getIntent().getIntExtra("PriceSort_Postion", -1);
            ScoreSort_Postion = getIntent().getIntExtra("ScoreSort_Postion", -1);
            BrandName_Postion = getIntent().getIntExtra("BrandName_Postion", -1);
            IsZiDingYiPrice = getIntent().getBooleanExtra("IsSort_Rang_Price_ZiDingYi", false);
            IsZiDingYiScore = getIntent().getBooleanExtra("IsSort_Rang_Score_ZiDingYi", false);
            if (IsZiDingYiPrice) {
                ZiDingYiPrice = PriceSort;
            }
            if (IsZiDingYiScore) {
                ZiDingYiScore = ScoreSort;
            }

        }
    }

    private void IBase() {
        MyLeft = new ArrayList<>();
        MyLeft.add(popMaitabSortType);
        MyLeft.add(popMaitabSortPrice);
        MyLeft.add(popMaitabSortJifen);
        MyLeft.add(popMaitabSortBranc);

        //开始二级分类的Ap的初始化
        mySortAp = new MySortAp();
        popMaitabSortLs.setAdapter(mySortAp);
//开始进行品牌列表的Ap的初始化
        myBrnadAp = new MyBrandAp();
        popMaitabSortBrandGridview.setAdapter(myBrnadAp);
//开始初始化我的价格的列表
        myRangAp = new MyRangAp();
        popMaitabRangLs.setAdapter(myRangAp);
//开始初始化我的积分列表
        myRangScoreAp = new MyRangAp();
        popMaitabRangscoreLs.setAdapter(myRangScoreAp);
        if (catoryid.equals("0")) {//全部进来 应该请求的是价格
            popMaitabSortType.setVisibility(View.GONE);
            LeftPostion = 1;
            CheckLeftPostion(LeftPostion);
            popMaitabSortLs.setVisibility(View.GONE);
            popMaitabRangLs.setVisibility(View.VISIBLE);
            popSortRangPriceEdLay.setVisibility(View.VISIBLE);
            Net_Rang_Price();
            //没有二级分类列表*************************
            Net_Rang_Price();
            Net_Rang_Scro();
            NetBrandLs();
            //先显示价格 如果价格是已经自定义过需要直接显示
            if (IsZiDingYiPrice) { //刚进来需要初始化的
                popSortRangPriceTag.setTextColor(getResources().getColor(R.color.app_fen));
                popSortRangPriceMinEd.setText(PriceSort.getMin());
                popSortRangPriceMaxEd.setText(PriceSort.getMax());


            }

        } else {//点击一级列表进来
            CheckLeftPostion(LeftPostion);
            popMaitabSortLs.setVisibility(View.VISIBLE);
            popMaitabSortBrandGridview.setVisibility(View.GONE);
            NetSort(catoryid);
            //有二级分类列白哦***************************
            NetSort(catoryid);
            Net_Rang_Price();
            Net_Rang_Scro();
            NetBrandLs();
        }


        popMaitabSortLs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (LeftPostion) {
                    case 0:
                        mySortAp.SetSelectPostion(position);

                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        });
        popMaitabSortBrandGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (LeftPostion) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        myBrnadAp.SetSelectPostion(position);
                        break;
                }
            }
        });
        popMaitabRangLs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (LeftPostion) {
                    case 0:
                        break;
                    case 1:
                        myRangAp.SetSelectPostion(position);

                        IsZiDingYiPrice = false;
                        ZiDingYiPrice = null;
                        popSortRangPriceTag.setTextColor(getResources().getColor(R.color.app_black));
                        popSortRangPriceMinEd.setText("");
                        popSortRangPriceMaxEd.setText("");
                        break;
                    case 2:
                        break;
                    case 3:

                        break;
                }
            }
        });
        popMaitabRangscoreLs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (LeftPostion) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        myRangScoreAp.SetSelectPostion(position);

                        IsZiDingYiScore = false;
                        ZiDingYiScore = null;
                        popSortRangScoreTag.setTextColor(getResources().getColor(R.color.app_black));
                        popSortRangScoreMinEd.setText("");
                        popSortRangScoreMaxEd.setText("");

                        break;
                    case 3:
                        break;
                }
            }
        });
    }

    private void CheckLeftPostion(int postion) {
        for (int i = 0; i < 4; i++) {
            if (i == postion) {
                MyLeft.get(i).setBackgroundColor(getResources().getColor(R.color.white));
            } else {
                MyLeft.get(i).setBackgroundColor(getResources().getColor(R.color.transparent));
            }
        }
    }

    @OnClick({R.id.pop_maitab_sort_type, R.id.pop_maitab_sort_price, R.id.pop_maitab_sort_jifen, R.id.pop_maitab_sort_branc, R.id.pop_maitab_queding, R.id.pop_maitab_reset, R.id.pop_maitab_cancle, R.id.pop_sort_rang_price_bt, R.id.pop_sort_rang_score_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pop_sort_rang_price_bt://价格区间的确定按钮
                RangEdCommeint(popSortRangPriceMinEd, popSortRangPriceMaxEd, 1);
                break;
            case R.id.pop_sort_rang_score_bt://积分区间的确定按钮
                RangEdCommeint(popSortRangScoreMinEd, popSortRangScoreMaxEd, 2);
                break;
            case R.id.pop_maitab_sort_type:
                LeftPostion = 0;
                CheckLeftPostion(LeftPostion);


                popSortRangPriceEdLay.setVisibility(View.GONE);
                popSortRangScoreEdLay.setVisibility(View.GONE);
                //

                //
                popMaitabRangscoreLs.setVisibility(View.GONE);
                popMaitabRangLs.setVisibility(View.GONE);
                popMaitabSortBrandGridview.setVisibility(View.GONE);
                popMaitabSortLs.setVisibility(View.VISIBLE);
                break;
            case R.id.pop_maitab_sort_price:
                LeftPostion = 1;
                CheckLeftPostion(LeftPostion);

                popSortRangPriceEdLay.setVisibility(View.VISIBLE);
                popSortRangScoreEdLay.setVisibility(View.GONE);
//是否已经选择了自定义的价格
                if (IsZiDingYiPrice) {
                    popSortRangPriceTag.setTextColor(getResources().getColor(R.color.app_fen));
                    popSortRangPriceMinEd.setText(ZiDingYiPrice.getMin());
                    popSortRangPriceMaxEd.setText(ZiDingYiPrice.getMax());
                } else {
                    popSortRangPriceTag.setTextColor(getResources().getColor(R.color.app_black));
                    popSortRangPriceMinEd.setText("");
                    popSortRangPriceMaxEd.setText("");
                }


                //
                popMaitabRangscoreLs.setVisibility(View.GONE);
                popMaitabSortBrandGridview.setVisibility(View.GONE);
                popMaitabSortLs.setVisibility(View.GONE);
                popMaitabRangLs.setVisibility(View.VISIBLE);
                if (myRangAp.getCount() == 0)
                    Net_Rang_Price();
                break;
            case R.id.pop_maitab_sort_jifen:
                LeftPostion = 2;
                CheckLeftPostion(LeftPostion);

                popSortRangPriceEdLay.setVisibility(View.GONE);
                popSortRangScoreEdLay.setVisibility(View.VISIBLE);
//是否已经选择了自定义积分
                if (IsZiDingYiScore) {
                    popSortRangScoreTag.setTextColor(getResources().getColor(R.color.app_fen));
                    popSortRangScoreMinEd.setText(ZiDingYiScore.getMin());
                    popSortRangScoreMaxEd.setText(ZiDingYiScore.getMax());
                } else {
                    popSortRangScoreTag.setTextColor(getResources().getColor(R.color.app_black));
                    popSortRangScoreMinEd.setText("");
                    popSortRangScoreMaxEd.setText("");
                }

                //
                popMaitabSortBrandGridview.setVisibility(View.GONE);
                popMaitabSortLs.setVisibility(View.GONE);
                popMaitabRangLs.setVisibility(View.GONE);
                popMaitabRangscoreLs.setVisibility(View.VISIBLE);
                if (myRangScoreAp.getCount() == 0)
                    Net_Rang_Scro();


                break;
            case R.id.pop_maitab_sort_branc://
                LeftPostion = 3;
                CheckLeftPostion(LeftPostion);
                //开始请求数据

                popSortRangPriceEdLay.setVisibility(View.GONE);
                popSortRangScoreEdLay.setVisibility(View.GONE);
                //
                popMaitabRangscoreLs.setVisibility(View.GONE);
                popMaitabRangLs.setVisibility(View.GONE);
                popMaitabSortLs.setVisibility(View.GONE);
                popMaitabSortBrandGridview.setVisibility(View.VISIBLE);
                if (popMaitabSortBrandGridview.getCount() == 0)
                    NetBrandLs();

                break;
            case R.id.pop_maitab_queding://确定筛选项
//                private MySortAp mySortAp;
//                private MyBrandAp myBrnadAp;
//                private MyRangAp myRangAp;
//                private MyRangAp myRangScoreAp;
                if (IsReSet) {
                    mySortAp.SetSelectPostion(-1);
                    myBrnadAp.SetSelectPostion(-1);
                    myRangAp.SetSelectPostion(-1);
                    myRangScoreAp.SetSelectPostion(-1);
                }

                //需要通过事件总线直接给ta页面的筛选fragment传递数据 要定义三种数据
                BMessage SortMessage = new BMessage(9901);

                //把筛选的二级分类id依string形式传递出去**********************************************************************************************
                String SortStr = "";
                List<BSortCategory> mydatas = mySortAp.GetDatas();
                if (mydatas.size() == 0) {
                    SortStr = "";
                    SortMessage.setSecondSortId_Postion(-1);
                } else if (mySortAp.GetSelectPostion() == -1) {// 没有点击筛选需要选择全部
                    for (int i = 0; i < mydatas.size(); i++) {
                        SortStr = SortStr + mydatas.get(i).getId() + ",";
                    }
                    SortStr = SortStr.substring(0, SortStr.length() - 1);
                    SortMessage.setSecondSortId_Postion(-1);
                } else {//点击筛选了
                    SortStr = mydatas.get(mySortAp.GetSelectPostion()).getId();
                    SortMessage.setSecondSortId_Postion(mySortAp.GetSelectPostion());
                }

//                PromptManager.ShowCustomToast(BaseContext,"筛选二级分类"+SortStr);
                SortMessage.setSecondSortId(SortStr);
                //把筛选的价格区间封装bean传递出去************************************************************************************************

                BSortRang MyPriceSort;
                if (myRangAp.GetSelectPostion() == -1) {// 没有点击筛选需要选择全部
                    MyPriceSort = new BSortRang("0", Constants.SortMax);
                    SortMessage.setPriceSort_Postion(-1);//-1标识没选择  -2标识自定义价格区间
                } else {//点击筛选了
                    MyPriceSort = myRangAp.GetDatas().get(myRangAp.GetSelectPostion());
                    SortMessage.setPriceSort_Postion(myRangAp.GetSelectPostion());
                }
//
                if (IsZiDingYiPrice) {//如果已经自定义筛选了直接改成自定义筛选的功能
                    MyPriceSort = ZiDingYiPrice;
                }
                SortMessage.setPriceSort(MyPriceSort);
                SortMessage.setSort_Price_ZiDingYi(IsZiDingYiPrice);//把是否自定义选择价格区间的标识传给你
                //把筛选的积分区间封装bean传递出去**********************************************************************************************

                BSortRang MyScoreSort;
                if (myRangScoreAp.GetSelectPostion() == -1) {// 没有点击筛选需要选择全部
                    MyScoreSort = new BSortRang("0", "200000000000000");
                    SortMessage.setScoreSort_Postion(-1);//-1标识没选择  -2标识自定义积分区间
                } else {//点击筛选了
                    MyScoreSort = myRangScoreAp.GetDatas().get(myRangScoreAp.GetSelectPostion());
                    SortMessage.setScoreSort_Postion(myRangScoreAp.GetSelectPostion());
                }
                if (IsZiDingYiScore) {//如果已经自定义筛选了直接改成自定义筛选的功能
                    MyScoreSort = ZiDingYiScore;
                }
//                PromptManager.ShowCustomToast(BaseContext, String.format("最小积分%s--最大积分%s", MyScoreSort.getMin(), MyScoreSort.getMax()));
                SortMessage.setScoreSort(MyScoreSort);
                SortMessage.setSort_Score_ZiDingYi(IsZiDingYiScore);//把是否自定义的字段返回
                //把筛选的品牌的string传递出去**********************************************************************************************
                String BrnadSort;
                if (myBrnadAp.GetSelectPostion() == -1) {
                    BrnadSort = "";
                    SortMessage.setBrandSort_Postion(-1);//-1标识没有选择品牌
                } else {
                    BrnadSort = myBrnadAp.GetDatas().get(myBrnadAp.GetSelectPostion());
                    SortMessage.setBrandSort_Postion(myBrnadAp.GetSelectPostion());
                }
                PromptManager.ShowCustomToast(BaseContext, String.format("我的筛选品牌=>%s", BrnadSort));

                SortMessage.setBrandSort(BrnadSort);
                //开始发送广播到fragment页面进行刷新列表
                EventBus.getDefault().post(SortMessage);
                BaseActivity.finish();
                break;
            case R.id.pop_maitab_reset://重置 相当于没做任何筛选
                if (mySortAp.getCount() == 0 && myBrnadAp.getCount() == 0 && myRangAp.getCount() == 0 && myRangScoreAp.getCount() == 0) {
                    PromptManager.ShowCustomToast(BaseContext, getResources().getString(R.string.toselect));
                    return;
                }


                if (mySortAp.GetSelectPostion() == -1 && myBrnadAp.GetSelectPostion() == -1 && myRangAp.GetSelectPostion() == -1 && myRangScoreAp.GetSelectPostion() == -1) {
                    PromptManager.ShowCustomToast(BaseContext, getResources().getString(R.string.toselect));
                    return;
                }
                ShowCustomDialog(getResources().getString(R.string.resetsort), getResources().getString(R.string.cancle), getResources().getString(R.string.queding), new IDialogResult() {
                    @Override
                    public void LeftResult() {

                    }

                    @Override
                    public void RightResult() {
                        mySortAp.SetReSet();
                        myBrnadAp.SetReSet();
                        myRangAp.SetReSet();
                        myRangScoreAp.SetReSet();
                        IsReSet = true;

                        if (IsZiDingYiPrice) {
                            popSortRangPriceTag.setTextColor(getResources().getColor(R.color.app_black));
                            popSortRangPriceMinEd.setText("");
                            popSortRangPriceMaxEd.setText("");
                        }
                        if(IsZiDingYiScore){
                            popSortRangScoreTag.setTextColor(getResources().getColor(R.color.app_black));
                            popSortRangScoreMinEd.setText("");
                            popSortRangScoreMaxEd.setText("");
                        }


                        IsZiDingYiPrice = false;//是否自定义价格
                        IsZiDingYiScore = false;//是否自定义积分

                        ZiDingYiPrice = new BSortRang("0", Constants.SortMax);
                        ZiDingYiScore = new BSortRang("0", Constants.SortMax);
//                        sssssss
                    }
                });
//                SecondSortId_Postion = -1;//getIntent().getIntExtra("SecondSortId_Postion",-1);
//                PriceSort_Postion = -1;
//                ScoreSort_Postion = -1;
//                BrandName_Postion = -1;

                break;
            case R.id.pop_maitab_cancle://请取消==》直接退出相当于没做任何筛选
                BaseActivity.finish();
                break;
        }
    }

    //点击价格或者积分=====》自定义区间的确定按钮
    private void RangEdCommeint(EditText popSortRangMinEd, EditText popSortRangMaxEd, int i) {

        if (StrUtils.isEmpty(popSortRangMinEd.getText().toString().trim())) {
            PromptManager.ShowCustomToast(BaseContext, String.format("请输入最小%s", i == 1 ? "价格" : "积分"));
            return;
        }
        if (StrUtils.isEmpty(popSortRangMaxEd.getText().toString().trim())) {
            PromptManager.ShowCustomToast(BaseContext, String.format("请输入最大%s", i == 1 ? "价格" : "积分"));
            return;
        }

        if (Integer.parseInt(popSortRangMaxEd.getText().toString().trim()) < Integer.parseInt(popSortRangMinEd.getText().toString().trim())) {
            PromptManager.ShowCustomToast(BaseContext, "请输入正确范围区间");
            return;
        }
        //开始保存自定义区间的数据/////同时如果对应的价格或者积分如果已经选择过了/需要把已选择过的item进行去掉亚瑟处理
        //如果筛选区间已经选择过需要把上边的徐泽期间的title文字变成红色的  便于进行区别


        if (1 == i) { //价格的区间的处理
            IsZiDingYiPrice = true;
            ZiDingYiPrice = new BSortRang(popSortRangMinEd.getText().toString().trim(), popSortRangMaxEd.getText().toString().trim());
            popSortRangPriceTag.setTextColor(getResources().getColor(R.color.app_fen));

            if (myRangAp.GetSelectPostion() == -1) {//没选择


            } else {//已经选择
                myRangAp.SetSelectPostion(-1);
            }

        } else {//积分的区间的处理
            IsZiDingYiScore = true;
            ZiDingYiScore = new BSortRang(popSortRangMinEd.getText().toString().trim(), popSortRangMaxEd.getText().toString().trim());
            popSortRangScoreTag.setTextColor(getResources().getColor(R.color.app_fen));

            if (myRangScoreAp.GetSelectPostion() == -1) {//没选择过

            } else {//已经选择过
                myRangScoreAp.SetSelectPostion(-1);
            }

        }
        PromptManager.ShowCustomToast(BaseContext, String.format("%s区间选择成功", 1 == i ? "价格" : "积分"));
        hintKbTwo();
    }


    /**
     * 获取二级分类的列表
     */
    private void NetSort(String Sorttype) {
        PromptManager.showtextLoading(BaseContext, "筛选中");
        NHttpBaseStr mNHttpBaseStr = new NHttpBaseStr(BaseContext);
        mNHttpBaseStr.setPostResult(new IHttpResult<String>() {
            @Override
            public void getResult(int Code, String Msg, String Data) {
                List<BSortCategory> dataresult = JSON.parseArray(Data, BSortCategory.class);
                mySortAp.FrashSortAp(dataresult);

                if (IsRecover) {
                    mySortAp.SetSelectPostion(SecondSortId_Postion);
                }
            }

            @Override
            public void onError(String error, int LoadType) {

            }
        });
        HashMap<String, String> map = new HashMap<>();
        map.put("pid", Sorttype);
        mNHttpBaseStr.getData(Constants.Add_Good_Categoty, map, Request.Method.GET);
    }

    /**
     * 获取品牌列表
     * 点击品牌后
     */
    private void NetBrandLs() {
        //先判断是否存在品牌的缓存数据
        String BrnadCahcStr = CacheUtil.HomeSort_Brand_Get(BaseContext);
        if (!StrUtils.isEmpty(BrnadCahcStr)) {//存在缓存
            List<String> dataresult = JSON.parseArray(BrnadCahcStr, String.class);
            myBrnadAp.FrashBrandAp(dataresult);

            if (IsRecover) {
                myBrnadAp.SetSelectPostion(BrandName_Postion);
            }
            return;
        }

        PromptManager.showtextLoading(BaseContext, "筛选中");
        NHttpBaseStr mbrandNHttpBaseStr = new NHttpBaseStr(BaseContext);
        mbrandNHttpBaseStr.setPostResult(new IHttpResult<String>() {
            @Override
            public void getResult(int Code, String Msg, String Data) {
                List<String> dataresult = JSON.parseArray(Data, String.class);
                myBrnadAp.FrashBrandAp(dataresult);
                CacheUtil.HomeSort_Brand_Save(BaseContext, Data);
            }

            @Override
            public void onError(String error, int LoadType) {

            }
        });
        HashMap<String, String> map = new HashMap<>();
        mbrandNHttpBaseStr.getData(Constants.BrandsLs, map, Request.Method.GET);


    }

    /**
     * 获取 价格的 区间列表
     */
    private void Net_Rang_Price() {
        //先判断是否存在 的缓存数据
        String BrnadCahcStr = CacheUtil.HomeSort_Price_Range_Get(BaseContext);
        if (!StrUtils.isEmpty(BrnadCahcStr)) {//存在缓存
            //直接获取数据展示数据
            List<BSortRang> ResultPrice = JSON.parseArray(BrnadCahcStr, BSortRang.class);
            myRangAp.FrashRangAp(ResultPrice);

            if (IsRecover) {
                myRangAp.SetSelectPostion(PriceSort_Postion);
            }
            return;
        }
        PromptManager.showtextLoading(BaseContext, "筛选中");
        NHttpBaseStr mbrandNHttpBaseStr = new NHttpBaseStr(BaseContext);
        mbrandNHttpBaseStr.setPostResult(new IHttpResult<String>() {
            @Override
            public void getResult(int Code, String Msg, String Data) {
                List<BSortRang> ResultPrice = JSON.parseArray(Data, BSortRang.class);
                myRangAp.FrashRangAp(ResultPrice);
                CacheUtil.HomeSort_Price_Range_Save(BaseContext, Data);
            }

            @Override
            public void onError(String error, int LoadType) {

            }
        });
        HashMap<String, String> map = new HashMap<>();
        mbrandNHttpBaseStr.getData(Constants.MainSort_Price_Rang, map, Request.Method.GET);
    }

    /**
     * 获取积分的 区间列表
     */
    private void Net_Rang_Scro() {

        //先判断是否存在品牌的缓存数据
        String BrnadCahcStr = CacheUtil.HomeSort_Scroe_Range_Get(BaseContext);
        if (!StrUtils.isEmpty(BrnadCahcStr)) {//存在缓存
            //直接获取数据展示数据
            List<BSortRang> ResultPrice = JSON.parseArray(BrnadCahcStr, BSortRang.class);
            myRangScoreAp.FrashRangAp(ResultPrice);

            if (IsRecover) {
                myRangScoreAp.SetSelectPostion(ScoreSort_Postion);
            }
            return;
        }
        PromptManager.showtextLoading(BaseContext, "筛选中");
        NHttpBaseStr mbrandNHttpBaseStr = new NHttpBaseStr(BaseContext);
        mbrandNHttpBaseStr.setPostResult(new IHttpResult<String>() {
            @Override
            public void getResult(int Code, String Msg, String Data) {
                List<BSortRang> ResultPrice = JSON.parseArray(Data, BSortRang.class);
                myRangScoreAp.FrashRangAp(ResultPrice);
                CacheUtil.HomeSort_Scroe_Range_Save(BaseContext, Data);
            }

            @Override
            public void onError(String error, int LoadType) {

            }
        });
        HashMap<String, String> map = new HashMap<>();
        mbrandNHttpBaseStr.getData(Constants.MainSort_Score_Rang, map, Request.Method.GET);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //通知分类Fragment进行重置分类文本的颜色和图片的颜色
        EventBus.getDefault().post(new BMessage(2111));
    }


    /**
     * 二级分类的ap
     */
    private class MySortAp extends BaseAdapter {
        //二级分类
        private List<BSortCategory> datas = new ArrayList<>();
        //品牌列表
        //假重置
        private boolean IsJiaClear;
        private int selectItem = -1;

        public void SetReSet() {
            IsJiaClear = true;
            this.notifyDataSetChanged();
        }

        public void SetSelectPostion(int postion) {
            IsJiaClear = false;
            this.selectItem = postion;
            this.notifyDataSetChanged();
        }

        public List<BSortCategory> GetDatas() {
            return datas;
        }

        public int GetSelectPostion() {
            return selectItem;
        }

        public void FrashSortAp(List<BSortCategory> ddd) {
//            BSortCategory all = new BSortCategory("", "全部");
//            this.datas.add(all);
//            this.datas.addAll(ddd);
            this.datas = ddd;
            this.notifyDataSetChanged();
        }

        public void FrashBrnadAp(List<String> dass) {

        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            myitem mmiten = null;
            if (null == convertView) {
                convertView = LayoutInflater.from(BaseContext).inflate(R.layout.pop_mainsort_sort_item, null);
                mmiten = new myitem();
                mmiten.pop_mainsort_sort_item_txt = (TextView) convertView.findViewById(R.id.pop_mainsort_sort_item_txt);
                convertView.setTag(mmiten);
            } else {
                mmiten = (myitem) convertView.getTag();
            }
            BSortCategory da = datas.get(position);
            StrUtils.SetTxt(mmiten.pop_mainsort_sort_item_txt, da.getCate_name());
            if (selectItem == position && !IsJiaClear) {
//                mmiten.pop_mainsort_sort_item_txt.setBackgroundColor(getResources().getColor(R.color.app_fen2));
                mmiten.pop_mainsort_sort_item_txt.setTextColor(getResources().getColor(R.color.red));
            } else {
//                mmiten.pop_mainsort_sort_item_txt.setBackgroundColor(getResources().getColor(R.color.transparent));
                mmiten.pop_mainsort_sort_item_txt.setTextColor(getResources().getColor(R.color.black));
            }
            return convertView;
        }

        class myitem {
            TextView pop_mainsort_sort_item_txt;
        }
    }

    /**
     * 品牌的分类的Ap
     */
    private class MyBrandAp extends BaseAdapter {
        //二级分类
        private List<String> datas = new ArrayList<>();
        //品牌列表


        //假重置

        private int selectItem = -1;
        private boolean IsJiaClear;

        public void SetReSet() {
            IsJiaClear = true;
            this.notifyDataSetChanged();
        }

        public List<String> GetDatas() {
            return datas;
        }

        public int GetSelectPostion() {
            return selectItem;
        }

        public void SetSelectPostion(int postion) {
            this.selectItem = postion;
            IsJiaClear = false;
            this.notifyDataSetChanged();
        }

        public void FrashBrandAp(List<String> ddd) {
            this.datas = ddd;
            this.notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyBrnaditem mmiten = null;
            if (null == convertView) {
                convertView = LayoutInflater.from(BaseContext).inflate(R.layout.item_pop_mainsort_sort_brand, null);
                mmiten = new MyBrnaditem();
                mmiten.pop_mainsort_sort_brand_item_txt = (TextView) convertView.findViewById(R.id.pop_mainsort_sort_brand_item_txt);
                convertView.setTag(mmiten);
            } else {
                mmiten = (MyBrnaditem) convertView.getTag();
            }
            String da = datas.get(position);
            StrUtils.SetTxt(mmiten.pop_mainsort_sort_brand_item_txt, da);
            if (selectItem == position && !IsJiaClear) {
//                mmiten.pop_mainsort_sort_brand_item_txt.setBackgroundColor(getResources().getColor(R.color.app_fen2));
                mmiten.pop_mainsort_sort_brand_item_txt.setTextColor(getResources().getColor(R.color.red));
            } else {
//                mmiten.pop_mainsort_sort_brand_item_txt.setBackgroundColor(getResources().getColor(R.color.transparent));
                mmiten.pop_mainsort_sort_brand_item_txt.setTextColor(getResources().getColor(R.color.black));
            }
            return convertView;
        }

        class MyBrnaditem {
            TextView pop_mainsort_sort_brand_item_txt;
        }
    }

    //价格和积分的Ap

    /**
     * 二级分类的ap
     */
    private class MyRangAp extends BaseAdapter {
        //二级分类
        private List<BSortRang> datas = new ArrayList<>();
        //品牌列表

        private int selectItem = -1;
        private boolean IsJiaClear;

        public void SetReSet() {
            IsJiaClear = true;
            this.notifyDataSetChanged();
        }

        public List<BSortRang> GetDatas() {
            return datas;
        }

        public int GetSelectPostion() {
            return selectItem;
        }

        public void SetSelectPostion(int postion) {
            this.selectItem = postion;
            IsJiaClear = false;
            this.notifyDataSetChanged();
        }

        public void FrashRangAp(List<BSortRang> ddd) {
//            BSortRang daa = new BSortRang("0", Constants.SortMax);
//            this.datas.add(daa);
//            this.datas.addAll(ddd);
            this.datas = ddd;
            this.notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            myitem mmiten = null;
            if (null == convertView) {
                convertView = LayoutInflater.from(BaseContext).inflate(R.layout.pop_mainsort_sort_item, null);
                mmiten = new myitem();
                mmiten.pop_mainsort_sort_item_txt = (TextView) convertView.findViewById(R.id.pop_mainsort_sort_item_txt);
                convertView.setTag(mmiten);
            } else {
                mmiten = (myitem) convertView.getTag();
            }
            BSortRang da = datas.get(position);
            StrUtils.SetTxt(mmiten.pop_mainsort_sort_item_txt, da.getMin() + "--" + da.getMax());
            if (StrUtils.isEmpty(da.getMax())) {
                StrUtils.SetTxt(mmiten.pop_mainsort_sort_item_txt, "大于" + da.getMin());
            }
            if (selectItem == position && !IsJiaClear) {
//                mmiten.pop_mainsort_sort_item_txt.setBackgroundColor(getResources().getColor(R.color.app_fen2));
                mmiten.pop_mainsort_sort_item_txt.setTextColor(getResources().getColor(R.color.red));
            } else {
//                mmiten.pop_mainsort_sort_item_txt.setBackgroundColor(getResources().getColor(R.color.transparent));
                mmiten.pop_mainsort_sort_item_txt.setTextColor(getResources().getColor(R.color.black));
            }
            return convertView;
        }

        class myitem {
            TextView pop_mainsort_sort_item_txt;
        }
    }

}
