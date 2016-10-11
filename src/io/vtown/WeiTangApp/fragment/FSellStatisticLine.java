package io.vtown.WeiTangApp.fragment;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

import android.R.integer;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import cn.jpush.a.a.ay;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-20 下午1:21:18
 *          // * @see  销售统计
 */
public class FSellStatisticLine extends FBase implements OnClickListener {

    private int SellType = 1; // 1==>近一周///2=>近一月 ///3==》近一季

    private TextView fragment_sellstatistics_bt1, fragment_sellstatistics_bt2,
            fragment_sellstatistics_bt3;
    private List<TextView> textViews = new ArrayList<TextView>();

    public static final String Key_Tage = "FSellStatisticLinekey";
    // 当前的page位置
    private int CurrentPage = 0;

    // 折现
    private LineChartView fragment_sellstatistics_line;
    private HashMap<Integer, List<BNew>> RecordLs = new HashMap<Integer, List<BNew>>();
    private BUser mBUser;

    private View fragment_shopline_nodata_lay;
    private boolean IsPeople = false;

    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(
                R.layout.fragment_sellstatistics, null);
        if (-1 == SellType)
            return;
        mBUser = Spuit.User_Get(BaseContext);
        IBaseVV();
    }

    private void IBaseVV() {
        fragment_shopline_nodata_lay = BaseView
                .findViewById(R.id.fragment_shopline_nodata_lay);
        fragment_sellstatistics_bt1 = ViewHolder.get(BaseView,
                R.id.fragment_sellstatistics_bt1);
        fragment_sellstatistics_bt2 = ViewHolder.get(BaseView,
                R.id.fragment_sellstatistics_bt2);
        fragment_sellstatistics_bt3 = ViewHolder.get(BaseView,
                R.id.fragment_sellstatistics_bt3);

        fragment_sellstatistics_bt1.setOnClickListener(this);
        fragment_sellstatistics_bt2.setOnClickListener(this);
        fragment_sellstatistics_bt3.setOnClickListener(this);

        textViews.add(fragment_sellstatistics_bt1);
        textViews.add(fragment_sellstatistics_bt2);
        textViews.add(fragment_sellstatistics_bt3);
        fragment_sellstatistics_line = (LineChartView) BaseView
                .findViewById(R.id.fragment_sellstatistics_line);

        SetLineData(new ArrayList<BNew>());
        TextClickControl(CurrentPage);
        GetLines(CurrentPage);
    }

    public String ChangTime(String da) {
        if (da.length() > 5) {
            return da.substring(5, da.length());

        }
        return da;
    }

    private void SetLineData(List<BNew> datass) {
        List<PointValue> mPointValues = new ArrayList<PointValue>();// 每一个点
        List<AxisValue> mAxisValues = new ArrayList<AxisValue>();// 每一个X轴

        // for (int i = 0; i < 10; i++) {
        // mPointValues.add(new PointValue(i, new Random().nextInt(100)));
        // mAxisValues.add(new AxisValue(i).setLabel("第"+i + "天")); //
        // 为每个对应的i设置相应的label(显示在X轴)
        // }
        boolean IsNoData = true;
        float MaxNumber = 0;
        int Sizeleght = 0;
        for (int i = 0; i < datass.size(); i++) {

            int MyValue = StrUtils.toInt(datass.get(i).getValue());

            mPointValues.add(new PointValue(i, IsPeople ? (int) MyValue
                    : ((float) MyValue / 100)));
            if (IsPeople) {
                if (MyValue >= MaxNumber)
                    MaxNumber = MyValue;
            } else {
                if (((float) MyValue / 100) >= MaxNumber)
                    MaxNumber = (float) MyValue / 100;


            }

            if (MyValue > 0) {
                IsNoData = false;
            }
            mAxisValues.add(new AxisValue(i).setLabel(ChangTime(datass.get(i)
                    .getDate())));
        }

        Sizeleght = StrUtils.toStr1(MaxNumber).length();
        if (IsNoData == true) {
            ShowNoDsataShow();
            // ssssss
            fragment_sellstatistics_line.setVisibility(View.GONE);
            fragment_shopline_nodata_lay.setVisibility(View.VISIBLE);
            return;
        } else {
            fragment_sellstatistics_line.setVisibility(View.VISIBLE);
            fragment_shopline_nodata_lay.setVisibility(View.GONE);
        }
        Line line = new Line(mPointValues).setColor(
                getResources().getColor(R.color.app_fen)).setCubic(true);
        // line.setPointColor(getResources().getColor(R.color.gold));
        line.setShape(ValueShape.CIRCLE);

        line.setHasPoints(true);
//        line.setFilled(true);
        line.setStrokeWidth(1);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        line.setHasLabelsOnlyForSelected(true);

        // 坐标轴
        Axis axisX = new Axis(); // X轴
        axisX.setHasTiltedLabels(true);

        axisX.setTextColor(Color.GRAY);
        // axisX.setMaxLabelChars(5);
        axisX.setValues(mAxisValues);
        axisX.setInside(false);
        // axisX.setMaxLabelChars(10);
        axisX.setLineColor(Color.GRAY);
        axisX.setHasLines(false);
        data.setAxisXBottom(axisX);

        Axis axisY = new Axis(); // Y轴
        axisY.setMaxLabelChars(Sizeleght == 0 ? 1 : Sizeleght); // 默认是3，只能看最后三个数字
        axisY.setLineColor(Color.GRAY);
        // axisY.setFormatter(new MyYValueFormatter())
        axisY.setTextColor(Color.GRAY);
        axisY.setHasLines(false);
        data.setAxisYLeft(axisY);
        // 坐标轴

        // 设置行为属性，支持缩放、滑动以及平移
        fragment_sellstatistics_line.setInteractive(true);
        fragment_sellstatistics_line.setInteractive(true);
        fragment_sellstatistics_line.setActivated(true);//setPreviewColor(getResources().getColor(R.color.green));
//        fragment_sellstatistics_line.setBackground(getResources().getDrawable(
//                R.drawable.chat_bg));
        fragment_sellstatistics_line.setZoomType(ZoomType.HORIZONTAL);
        fragment_sellstatistics_line.setContainerScrollEnabled(true,
                ContainerScrollType.HORIZONTAL);
        // fragment_sellstatistics_line.setMaximumViewport(initViewPort());

        fragment_sellstatistics_line.setLineChartData(data);

        fragment_sellstatistics_line.setOnValueTouchListener(new LineChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, int i1, PointValue pointValue) {
                PromptManager.ShowCustomToast(BaseContext, pointValue.getY() + "");
            }

            @Override
            public void onValueDeselected() {

            }
        });
        // fragment_sellstatistics_line.no
        // fragment_sellstatistics_line.setVisibility(View.VISIBLE);
        // fragment_sellstatistics_line.setMaximumViewport(initViewPort());

    }

    // public class MyYValueFormatter implements Formatter {
    //
    // private DecimalFormat mFormat;
    //
    // public MyYValueFormatter() {
    // mFormat = new DecimalFormat("###,###,###,##0");
    // }
    //
    // @Override
    // public String getFormattedValue(float value, YAxis yAxis) {
    // return mFormat.format(value);
    // }
    // }

    /**
     * 设置无数据时候的提示语
     */
    private void ShowNoDsataShow() {
        switch (CurrentPage) {
            case 0:
                ShowErrorCanLoad(getResources().getString(R.string.noincome));
                break;
            case 1:
                ShowErrorCanLoad(getResources().getString(R.string.nosell));
                break;
            case 2:
                ShowErrorCanLoad(getResources().getString(R.string.nofangke));
                break;
        }
    }

    private void TextClickControl(int Postion) {

        for (int i = 0; i < textViews.size(); i++) {
            if (Postion == i) {// 被点击了的修改状态
                textViews.get(Postion).setBackground(
                        getResources().getDrawable(
                                R.drawable.shap_sell_line_pre));
                textViews.get(Postion).setTextColor(
                        getResources().getColor(R.color.app_fen));
            } else {// 未点击 的状态
                textViews.get(i).setBackground(
                        getResources().getDrawable(
                                R.drawable.shap_sell_line_nor));
                textViews.get(i).setTextColor(
                        getResources().getColor(R.color.grey));
            }
        }
        // 刷新数据
        GetLines(Postion);
    }

    @Override
    public void InitCreate(Bundle d) {
        if (null != d && d.containsKey(Key_Tage)) {
            SellType = d.getInt(Key_Tage);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_sellstatistics_bt1:
                CurrentPage = 0;
                TextClickControl(CurrentPage);
                IsPeople = false;
                break;
            case R.id.fragment_sellstatistics_bt2:
                CurrentPage = 1;
                TextClickControl(CurrentPage);
                IsPeople = false;
                break;
            case R.id.fragment_sellstatistics_bt3:
                CurrentPage = 2;
                TextClickControl(CurrentPage);

                IsPeople = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void getResult(int Code, String Msg, BComment Data) {

        List<BNew> bNews = new ArrayList<BNew>();
        // if (StrUtils.isEmpty(Data.getHttpResultStr())) {
        // SetLineData(bNews);
        // return;
        // }
        bNews = JSON.parseArray(Data.getHttpResultStr(), BNew.class);
        RecordLs.put(Data.getHttpResultTage(), bNews);
        // 刷新折线图***********************************************
        SetLineData(bNews);
    }

    @Override
    public void onError(String error, int LoadType) {
        LogUtils.i("s");
    }

    /**
     * 获取数据
     */
    private void GetLines(int CurrentIndex) {

        if (CurrentIndex == 2)
            IsPeople = true;
        else
            IsPeople = false;
        if (RecordLs.containsKey(CurrentIndex)) {// 已经下载过了 直接显示就可以
            List<BNew> news = RecordLs.get(CurrentIndex);
            // 开始刷新
            SetLineData(news);
            return;

        }
        // SellType //1==>近一周///2=>近一月 ///3==》近一季
        // type 1收入 2销量 3访客

        PromptManager.showtextLoading(BaseContext,
                getResources().getString(R.string.loading));
        SetTitleHttpDataLisenter(this);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("seller_id", mBUser.getSeller_id());
        map.put("range", SellType + "");
        map.put("type", (CurrentIndex + 1) + "");
        FBGetHttpData(map, Constants.SellStaatistic_Line, Method.GET,
                CurrentIndex, INITIALIZE);
    }
}
