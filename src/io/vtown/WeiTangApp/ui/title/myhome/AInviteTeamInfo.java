package io.vtown.WeiTangApp.ui.title.myhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.util.HashMap;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.three_two.BCTeamInfo;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by Yihuihua on 2016/11/16.
 */

public class AInviteTeamInfo extends ATitleBase {
    /*
    * 团队总人数
    * */
    private View invite_info_amount;
    /*
    * 团队活跃人数
    * */
    private View invite_info_team_activity_conter;
    /*
    * 我的直属下级活跃人数
    * */
    private View invite_info_my_activity_conter;
    /*
    * 昨日新增用户
    * */
    private View invite_info_my_yestaday_add;
    /*
    * 昨日激活用户
    * */
    private View invite_info_my_yestaday_activity;
    /**
     * 本月活跃人数
     */
    private View invite_info_my_benyue_activity;
    /**
     * 本月直属
     */
    private View invite_info_my_benyue_zhishu;

    private BCTeamInfo mTeamInfo;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_invite_info);
        SetTitleHttpDataLisenter(this);
        IView();
        ICache();
        IData();

    }

    private void ICache() {
        String invite_info = CacheUtil.Invite_Team_Get(BaseContext);
        if (StrUtils.isEmpty(invite_info)) {
            PromptManager.showtextLoading(BaseContext, getResources()
                    .getString(R.string.loading));
        } else {
            try {
                mTeamInfo = JSON.parseObject(invite_info, BCTeamInfo.class);
            } catch (Exception e) {
                return;
            }
            RefrashView(mTeamInfo);
        }


    }

    private void IData() {
        HashMap<String, String> map = new HashMap<>();
        map.put("member_id", Spuit.User_Get(BaseContext).getMember_id());
        FBGetHttpData(map, Constants.Invite_Team_info, Request.Method.GET, 0, LOAD_INITIALIZE);
    }

    private void IView() {
        invite_info_amount = findViewById(R.id.invite_info_amount);
        invite_info_team_activity_conter = findViewById(R.id.invite_info_team_activity_conter);
        invite_info_my_activity_conter = findViewById(R.id.invite_info_my_activity_conter);
        invite_info_my_yestaday_add = findViewById(R.id.invite_info_my_yestaday_add);
        invite_info_my_yestaday_activity = findViewById(R.id.invite_info_my_yestaday_activity);


        invite_info_my_benyue_activity= findViewById(R.id.invite_info_my_benyue_activity);
        invite_info_my_benyue_zhishu= findViewById(R.id.invite_info_my_benyue_zhishu);
    }

    private void RefrashView(BCTeamInfo data) {
        SetItemContent(invite_info_amount, R.drawable.ic_yaoqingrenshu_nor, R.string.invite_team_info_invite_amount, data.getInvite_num(), true);
        SetItemContent(invite_info_team_activity_conter, R.drawable.ic_tuanduihuoyueyonghu_nor, R.string.invite_team_info_team_activity, data.getTeam_activate_num(), false);
        SetItemContent(invite_info_my_activity_conter, R.drawable.ic_zhishuhuoyueyonghu_nor, R.string.invite_team_info_my_activity, data.getInvite_activate_num(), false);
        SetItemContent(invite_info_my_yestaday_add, R.drawable.ic_zuorixinzengyonghu_nor, R.string.invite_team_info_yestaday_add, data.getActivate_num(), false);
        SetItemContent(invite_info_my_yestaday_activity, R.drawable.ic_zuorijihuoyonghu_nor, R.string.invite_team_info_yestaday_activity, data.getSub_activate_num(), false);
   //
        SetItemContent(invite_info_my_benyue_activity, R.drawable.ic_benyuehuoyue_nor, R.string.invite_team_info_benyue_activity, data.getMonth_activate_num(), false);
        SetItemContent(invite_info_my_benyue_zhishu, R.drawable.ic_benyuezhishu_nor, R.string.invite_team_info_benyue_zhishu, data.getMonth_sub_activate_num(), false);

    }

    private void SetItemContent(View VV, int imgsre, int ResourceTitlet, String txt2, boolean arrershow) {
        ImageView commentview_center_iv = (ImageView) VV.findViewById(R.id.commentview_center_iv);
        ImageView commentview_center_arrer = (ImageView) VV.findViewById(R.id.commentview_center_arrer);
        TextView commentview_center_txt2 = (TextView) VV.findViewById(R.id.commentview_center_txt2);

        commentview_center_iv.setImageResource(imgsre);
        ((TextView) VV.findViewById(R.id.commentview_center_txt))
                .setText(getResources().getString(ResourceTitlet));
        if (!StrUtils.isEmpty(txt2)) {
            StrUtils.SetTxt(commentview_center_txt2, txt2);
        }

        if (arrershow) {
            VV.setOnClickListener(this);
        } else {
            VV.setBackgroundResource(R.color.white);
            commentview_center_arrer.setVisibility(View.INVISIBLE);
        }


    }

    @Override
    protected void InitTile() {
        SetTitleTxt(getResources().getString(R.string.invite_team_info_title));
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        if (StrUtils.isEmpty(Data.getHttpResultStr())) {
            return;
        }
        mTeamInfo = new BCTeamInfo();
        mTeamInfo = JSON.parseObject(Data.getHttpResultStr(), BCTeamInfo.class);
        CacheUtil.Invite_Team_Save(BaseContext, Data.getHttpResultStr());
        RefrashView(mTeamInfo);
    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.ShowCustomToast(BaseContext, error);
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
            case R.id.invite_info_amount://邀请人数
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext, AInviteFriendRecord.class));
                break;
        }
    }

    @Override
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }
}
