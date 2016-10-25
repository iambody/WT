package io.vtown.WeiTangApp.ui.title;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.BCMyLeader;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.util.ui.UiHelper;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by Yihuihua on 2016/10/24.
 */

public class AMyLeader extends ATitleBase {
    @BindView(R.id.iv_my_leader_icon)
    CircleImageView ivMyLeaderIcon;
    @BindView(R.id.tv_my_leader_name)
    TextView tvMyLeaderName;
    @BindView(R.id.tv_my_leader_usertag)
    TextView tvMyLeaderUsertag;
    @BindView(R.id.sv_my_leader)
    ScrollView sv_my_leader;
    private Unbinder mBind;
    private View mRootView;
    private View my_leader_nodata_lay;

    @Override
    protected void InItBaseView() {

        setContentView(R.layout.activity_my_leader);
        mRootView = LayoutInflater.from(BaseContext).inflate(R.layout.activity_my_leader, null);

        mBind = ButterKnife.bind(this);
        IView();
        IData();

    }

    private void IView() {
        my_leader_nodata_lay = ViewHolder.get(mRootView, R.id.my_leader_nodata_lay);
        my_leader_nodata_lay.setOnClickListener(this);
    }

    private void IData() {
        SetTitleHttpDataLisenter(this);
        HashMap<String, String> map = new HashMap<>();
        map.put("member_id", Spuit.User_Get(BaseContext).getMember_id());
        FBGetHttpData(map, Constants.Get_My_Leader, Request.Method.GET, 0, LOAD_INITIALIZE);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt(getString(R.string.my_leader));
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

        BCMyLeader leader = JSON.parseObject(Data.getHttpResultStr(), BCMyLeader.class);
        RefreshView(leader);
    }

    private void RefreshView(BCMyLeader leader) {
        ImageLoaderUtil.Load2(leader.getAvatar(), ivMyLeaderIcon, R.drawable.error_iv2);
        StrUtils.SetTxt(tvMyLeaderName, leader.getSeller_name());
        if (0 == leader.getIs_activate()) {
            //设置shape的背景色和字体颜色
            UiHelper.SetShapeColor(tvMyLeaderUsertag, getResources().getColor(R.color.app_line));
            tvMyLeaderUsertag.setTextColor(getResources().getColor(R.color.gray));
            StrUtils.SetTxt(tvMyLeaderUsertag, getResources().getString(R.string.weijihuo));
        } else {
            StrUtils.SetTxt(tvMyLeaderUsertag, leader.getMember_level_name());
            //设置shape的背景色和字体颜色44
            switch (leader.getMember_level()) {
                case 0:
                    UiHelper.SetShapeColor(tvMyLeaderUsertag, getResources().getColor(R.color.lv1));
                    break;
                case 1:
                    UiHelper.SetShapeColor(tvMyLeaderUsertag, getResources().getColor(R.color.lv2));
                    break;
                case 2:
                    UiHelper.SetShapeColor(tvMyLeaderUsertag, getResources().getColor(R.color.lv3));
                    break;
                case 3:
                    UiHelper.SetShapeColor(tvMyLeaderUsertag, getResources().getColor(R.color.lv4));
                    break;
                case 4:
                    UiHelper.SetShapeColor(tvMyLeaderUsertag, getResources().getColor(R.color.lv5));
                    break;
                case 5:
                    UiHelper.SetShapeColor(tvMyLeaderUsertag, getResources().getColor(R.color.lv6));
                    break;
                case 6:
                    UiHelper.SetShapeColor(tvMyLeaderUsertag, getResources().getColor(R.color.lv7));
                    break;

            }
            tvMyLeaderUsertag.setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.ShowCustomToast(BaseContext, error);
        sv_my_leader.setVisibility(View.GONE);
        my_leader_nodata_lay.setVisibility(View.VISIBLE);
        my_leader_nodata_lay.setClickable(true);
        ShowErrorCanLoad(getResources().getString(R.string.error_null_noda));
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
            case R.id.my_leader_nodata_lay:
                if (CheckNet(BaseContext)) return;
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
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
    }
}
