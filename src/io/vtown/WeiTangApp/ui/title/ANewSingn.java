package io.vtown.WeiTangApp.ui.title;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by datutu on 2017/1/3.
 */

public class ANewSingn extends ATitleBase implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.tv_sign_days)
    TextView tvSignDays;
    @BindView(R.id.rl_sign_btn_layout)
    RelativeLayout rlSignBtnLayout;
    @BindView(R.id.iv_sign_piont_1)
    ImageView ivSignPiont1;
    @BindView(R.id.iv_sign_piont_2)
    ImageView ivSignPiont2;
    @BindView(R.id.iv_sign_piont_3)
    ImageView ivSignPiont3;
    @BindView(R.id.iv_sign_piont_4)
    ImageView ivSignPiont4;
    @BindView(R.id.iv_sign_piont_5)
    ImageView ivSignPiont5;
    @BindView(R.id.tv_new_sign_tips1)
    TextView tvNewSignTips1;
    @BindView(R.id.tv_new_sign_tips2)
    TextView tvNewSignTips2;
    @BindView(R.id.tv_new_sign_tips3)
    TextView tvNewSignTips3;
    @BindView(R.id.tv_new_sign_tips4)
    TextView tvNewSignTips4;
    @BindView(R.id.tv_new_sign_tips5)
    TextView tvNewSignTips5;
    @BindView(R.id.tv_new_sign_tips6)
    TextView tvNewSignTips6;
    private SwipeRefreshLayout new_sign_swiperefeshlayout;
    private View layout_sign_1, layout_sign_2, layout_sign_3, layout_sign_4, layout_sign_5, layout_sign_6;
    private List<View> LineViews = new ArrayList<>();
    private BUser MyUser;
    private Unbinder mBinder;


    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_newsingn);
        mBinder = ButterKnife.bind(this);
        MyUser = Spuit.User_Get(BaseContext);
        SetTitleHttpDataLisenter(this);
        IBaseView();
        //IResetView(CacheUtil.Sign_Number_Get(BaseContext));
        showCaptions(CacheUtil.Sign_Caption_Get(BaseContext));
        IBaseNet(LOAD_INITIALIZE);
    }

    private void IBaseNet(int LoadType) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", MyUser.getMember_id());
        FBGetHttpData(map, Constants.NewHomeSign, Request.Method.POST, 10, LoadType);
    }

    private void IBaseView() {
        new_sign_swiperefeshlayout = (SwipeRefreshLayout) findViewById(R.id.new_sign_swiperefeshlayout);
        new_sign_swiperefeshlayout.setColorSchemeResources(R.color.app_fen, R.color.app_fen1, R.color.app_fen2, R.color.app_fen3);
        new_sign_swiperefeshlayout.setOnRefreshListener(this);
        layout_sign_1 = findViewById(R.id.layout_sign_1);
        layout_sign_2 = findViewById(R.id.layout_sign_2);
        layout_sign_3 = findViewById(R.id.layout_sign_3);
        layout_sign_4 = findViewById(R.id.layout_sign_4);
        layout_sign_5 = findViewById(R.id.layout_sign_5);
        layout_sign_6 = findViewById(R.id.layout_sign_6);
        LineViews.add(layout_sign_1);
        LineViews.add(layout_sign_2);
        LineViews.add(layout_sign_3);
        LineViews.add(layout_sign_4);
        LineViews.add(layout_sign_5);
        LineViews.add(layout_sign_6);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt(getResources().getString(R.string.signe));
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        if (Data.getHttpLoadType() == LOAD_REFRESHING)
            new_sign_swiperefeshlayout.setRefreshing(false);
        BComment bComment = JSON.parseObject(Data.getHttpResultStr(), BComment.class);
        int sign_number = bComment.getSign_number();
        String captions = bComment.getCaption();

        if(!captions.equals(CacheUtil.Sign_Caption_Get(BaseContext))){
            CacheUtil.Sign_Caption_Save(BaseContext,captions);
        }

            showCaptions(captions);


        if (CacheUtil.Sign_Number_Get(BaseContext) != sign_number)
            PromptManager.ShowMyToast(BaseContext, getResources().getString(R.string.signe_succeed));
        CacheUtil.Sign_Number_Save(BaseContext, sign_number);
        IResetView(sign_number);
    }

    private void showCaptions(String captionstr) {
        if(!StrUtils.isEmpty(captionstr)){
            List<String> caption = JSON.parseArray(captionstr, String.class);
            StrUtils.SetTxt(tvNewSignTips1,caption.get(0));
            StrUtils.SetTxt(tvNewSignTips2,caption.get(1));
            StrUtils.SetTxt(tvNewSignTips3,caption.get(2));
            StrUtils.SetTxt(tvNewSignTips4,caption.get(3));
            StrUtils.SetTxt(tvNewSignTips5,caption.get(4));
            StrUtils.SetTxt(tvNewSignTips6,caption.get(5));
        }

    }

    /**
     * 获取到数据后重新绘制
     *
     * @param sign_number
     */
    private void IResetView(int sign_number) {
        StrUtils.SetTxt(tvSignDays, String.format("连续%s天", sign_number + ""));
        //开始处理签到显示
        LineToLine(sign_number);
        int BeiShu = GetLinePosion(sign_number, true);
        int Postion = GetLinePosion(sign_number, false);
        switch (BeiShu) {
            case 1:
                OddLineControl(1, Postion);
                break;
            case 2:
                OddLineControl(1, 5);
                EvenLineControl(2, Postion);
                break;
            case 3:
                OddLineControl(1, 5);
                EvenLineControl(2, 5);
                OddLineControl(3, Postion);
                break;
            case 4:
                OddLineControl(1, 5);
                EvenLineControl(2, 5);
                OddLineControl(3, 5);
                EvenLineControl(4, Postion);
                break;
            case 5:
                OddLineControl(1, 5);
                EvenLineControl(2, 5);
                OddLineControl(3, 5);
                EvenLineControl(4, 5);
                OddLineControl(5, Postion);
                break;
            case 6:
                OddLineControl(1, 5);
                EvenLineControl(2, 5);
                OddLineControl(3, 5);
                EvenLineControl(4, 5);
                OddLineControl(5, 5);
                EvenLineControl(6, Postion);
                break;

        }
    }

    /**
     * 该方法是传入一个0-5的整数 奇数行的显示（1,3,5行）
     */
    private void OddLineControl(int OddLinePostion, int OddPostion) {
        View OddLineView = LineViews.get(OddLinePostion - 1);
        switch (OddPostion) {
            case 1:
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_01)).setImageResource(R.drawable.pic_fensetangguo_nor);
                break;
            case 2:
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_01)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_point_01)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_02)).setImageResource(R.drawable.pic_fensetangguo_nor);
                break;
            case 3:
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_01)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_point_01)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_02)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_point_02)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_03)).setImageResource(R.drawable.pic_fensetangguo_nor);

                break;
            case 4:
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_01)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_point_01)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_02)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_point_02)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_03)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_point_03)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_04)).setImageResource(R.drawable.pic_fensetangguo_nor);
                break;
            case 5:
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_01)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_point_01)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_02)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_point_02)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_03)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_point_03)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_04)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_point_04)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_right_05)).setImageResource(R.drawable.pic_libao_nor);
                break;

        }
    }

    /**
     * 该方法是传入一个0-5的整数 偶数行的显示(2,4,6行)
     */
    private void EvenLineControl(int EvenLinePostion, int EvenPostion) {
        View OddLineView = LineViews.get(EvenLinePostion - 1);
        switch (EvenPostion) {
            case 1:
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_05)).setImageResource(R.drawable.pic_fensetangguo_nor);

                break;
            case 2:
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_05)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_point_04)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_04)).setImageResource(R.drawable.pic_fensetangguo_nor);
                break;
            case 3:
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_05)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_point_04)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_04)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_point_03)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_03)).setImageResource(R.drawable.pic_fensetangguo_nor);
                break;
            case 4:
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_05)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_point_04)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_04)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_point_03)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_03)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_point_02)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_02)).setImageResource(R.drawable.pic_fensetangguo_nor);
                break;
            case 5:
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_05)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_point_04)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_04)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_point_03)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_03)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_point_02)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_02)).setImageResource(R.drawable.pic_fensetangguo_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_point_01)).setImageResource(R.drawable.ic_dandian_nor);
                ((ImageView) OddLineView.findViewById(R.id.iv_sign_left_01)).setImageResource(R.drawable.pic_libao_nor);
                break;
        }
    }

    /**
     * 通过签到次数设置行业航连接的上下的险
     *
     * @param Number
     */
    private void LineToLine(int Number) {
        if (Number > 5 && Number <= 10) {
            ivSignPiont1.setImageResource(R.drawable.ic_dandian_vertical_nor);
        } else if (Number > 10 && Number <= 15) {
            ivSignPiont1.setImageResource(R.drawable.ic_dandian_vertical_nor);
            ivSignPiont2.setImageResource(R.drawable.ic_dandian_vertical_nor);
        } else if (Number > 15 && Number <= 20) {
            ivSignPiont1.setImageResource(R.drawable.ic_dandian_vertical_nor);
            ivSignPiont2.setImageResource(R.drawable.ic_dandian_vertical_nor);
            ivSignPiont3.setImageResource(R.drawable.ic_dandian_vertical_nor);
        } else if (Number > 20 && Number <= 25) {
            ivSignPiont1.setImageResource(R.drawable.ic_dandian_vertical_nor);
            ivSignPiont2.setImageResource(R.drawable.ic_dandian_vertical_nor);
            ivSignPiont3.setImageResource(R.drawable.ic_dandian_vertical_nor);
            ivSignPiont4.setImageResource(R.drawable.ic_dandian_vertical_nor);
        } else if (Number > 25) {
            ivSignPiont1.setImageResource(R.drawable.ic_dandian_vertical_nor);
            ivSignPiont2.setImageResource(R.drawable.ic_dandian_vertical_nor);
            ivSignPiont3.setImageResource(R.drawable.ic_dandian_vertical_nor);
            ivSignPiont4.setImageResource(R.drawable.ic_dandian_vertical_nor);
            ivSignPiont5.setImageResource(R.drawable.ic_dandian_vertical_nor);
        } else {
        }
    }

    @Override
    protected void DataError(String error, int LoadType) {
        if (LoadType == LOAD_REFRESHING)
            new_sign_swiperefeshlayout.setRefreshing(false);
    }

    @Override
    protected void NetConnect() {
        if (new_sign_swiperefeshlayout.isRefreshing()) {
            new_sign_swiperefeshlayout.setRefreshing(false);
        }
    }

    @Override
    protected void NetDisConnect() {
        if (new_sign_swiperefeshlayout.isRefreshing()) {
            new_sign_swiperefeshlayout.setRefreshing(false);
        }
    }

    @Override
    protected void SetNetView() {

    }

    @Override
    @OnClick(R.id.rl_sign_btn_layout)
    protected void MyClick(View V) {
        switch (V.getId()) {
            case R.id.rl_sign_btn_layout:
                if (CheckNet(BaseContext)) return;
                IBaseNet(LOAD_INITIALIZE);
                break;
        }
    }

    @Override
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }

    /**
     * 传入一个1-30的书获取是第几行
     * IsLine=true标识获取的是第几行  IsLine=false标识获取的是这一行的第几个
     */
    private int GetLinePosion(int Number, boolean IsLine) {

        if (Number <= 5) {
            return IsLine ? 1 : Number;
        } else if (Number > 5 && Number <= 10) {
            if (Number == 10) return IsLine ? 2 : 5;
            return IsLine ? 2 : Number % 5;
        } else if (Number > 10 && Number <= 15) {
            if (Number == 15) return IsLine ? 3 : 5;
            return IsLine ? 3 : Number % 5;
        } else if (Number > 15 && Number <= 20) {
            if (Number == 20) return IsLine ? 4 : 5;
            return IsLine ? 4 : Number % 5;
        } else if (Number > 20 && Number <= 25) {
            if (Number == 25) return IsLine ? 5 : 5;
            return IsLine ? 5 : Number % 5;
        } else if (Number > 25) {
            if (Number == 30) return IsLine ? 6 : 5;
            return IsLine ? 6 : Number % 5;
        } else {
            return 1;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinder.unbind();
        if (new_sign_swiperefeshlayout.isRefreshing()) {
            new_sign_swiperefeshlayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        if (CheckNet(BaseContext)) {
            new_sign_swiperefeshlayout.setRefreshing(false);
            return;
        }
        IBaseNet(LOAD_REFRESHING);
    }

}
