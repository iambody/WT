package io.vtown.WeiTangApp.ui.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.custom.switchButtonView.EaseSwitchButton;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by Yihuihua on 2016/12/5.
 */

public class AAddNewShow extends ATitleBase implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.tv_add_new_show_pic)
    TextView tvAddNewShowPic;
    @BindView(R.id.tv_add_new_show_vedio)
    TextView tvAddNewShowVedio;
    @BindView(R.id.iv_add_new_show_vedio_bg)
    ImageView ivAddNewShowVedioBg;
    @BindView(R.id.iv_add_new_show_vedio_control_icon)
    ImageView ivAddNewShowVedioControlIcon;
    @BindView(R.id.rl_add_new_show_vedio_layout)
    RelativeLayout rlAddNewShowVedioLayout;
    @BindView(R.id.gv_add_new_show_pics)
    CompleteGridView gvAddNewShowPics;
    @BindView(R.id.et_add_new_show_txt_content)
    EditText etAddNewShowTxtContent;
    @BindView(R.id.sb_add_new_show_select_good)
    EaseSwitchButton sbAddNewShowSelectGood;
    @BindView(R.id.rl_add_new_show_add_good)
    RelativeLayout rlAddNewShowAddGood;
    @BindView(R.id.iv_add_new_show_good_icon)
    ImageView ivAddNewShowGoodIcon;
    @BindView(R.id.iv_add_new_show_good_name)
    TextView ivAddNewShowGoodName;
    @BindView(R.id.tv_add_new_show_good_sales)
    TextView tvAddNewShowGoodSales;
    @BindView(R.id.tv_add_new_show_good_score)
    TextView tvAddNewShowGoodScore;
    @BindView(R.id.tv_add_new_show_good_price)
    TextView tvAddNewShowGoodPrice;
    @BindView(R.id.tv_add_new_show_good_origprice)
    TextView tvAddNewShowGoodOrigprice;
    @BindView(R.id.ll_add_new_show_good)
    LinearLayout llAddNewShowGood;
    @BindView(R.id.fl_add_new_show_good)
    FrameLayout flAddNewShowGood;
    @BindView(R.id.tv_add_new_show_good_share)
    TextView tvAddNewShowGoodShare;

    private static final int TYPE_PIC = 123;
    private static final int TYPE_VEDIO = 124;
    private int current_type = TYPE_PIC;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_add_new_show);
        IView();
    }

    private void IView() {
        sbAddNewShowSelectGood.setOnCheckedChangeListener(this);

    }

    @Override
    protected void InitTile() {
        SetTitleTxt("发布Show");
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

    }

    @Override
    protected void DataError(String error, int LoadType) {

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
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }

    private void ControlClick(int ClickId) {
        tvAddNewShowPic
                .setBackground(R.id.tv_add_new_show_pic == ClickId ? getResources()
                        .getDrawable(R.drawable.shape_left_pre)
                        : getResources().getDrawable(R.drawable.shape_left_nor));
        tvAddNewShowPic
                .setTextColor(R.id.tv_add_new_show_pic == ClickId ? getResources()
                        .getColor(R.color.TextColorWhite) : getResources()
                        .getColor(R.color.app_fen));

        tvAddNewShowVedio
                .setBackground(R.id.tv_add_new_show_vedio == ClickId ? getResources()
                        .getDrawable(R.drawable.shape_right_pre)
                        : getResources()
                        .getDrawable(R.drawable.shape_right_nor));
        tvAddNewShowVedio
                .setTextColor(R.id.tv_add_new_show_vedio == ClickId ? getResources()
                        .getColor(R.color.TextColorWhite) : getResources()
                        .getColor(R.color.app_fen));

    }

    @OnClick({R.id.tv_add_new_show_pic, R.id.tv_add_new_show_vedio, R.id.iv_add_new_show_vedio_control_icon, R.id.rl_add_new_show_add_good, R.id.tv_add_new_show_good_share})
    protected void MyClick(View V) {
        switch (V.getId()) {
            case R.id.tv_add_new_show_pic:
                current_type = TYPE_PIC;
                ControlClick(R.id.tv_add_new_show_pic);
                rlAddNewShowVedioLayout.setVisibility(View.GONE);
                gvAddNewShowPics.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_add_new_show_vedio:
                current_type = TYPE_VEDIO;
                ControlClick(R.id.tv_add_new_show_vedio);
                gvAddNewShowPics.setVisibility(View.GONE);
                rlAddNewShowVedioLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_add_new_show_vedio_control_icon:
                break;
            case R.id.rl_add_new_show_add_good:
                break;
            case R.id.tv_add_new_show_good_share:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){

        }else{

        }
    }

}
