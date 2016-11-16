package io.vtown.WeiTangApp.ui.title.myhome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
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

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_invite_info);
        IView();

    }

    private void IView() {
        invite_info_amount = findViewById(R.id.invite_info_amount);
        invite_info_team_activity_conter = findViewById(R.id.invite_info_team_activity_conter);
        invite_info_my_activity_conter = findViewById(R.id.invite_info_my_activity_conter);
        invite_info_my_yestaday_add = findViewById(R.id.invite_info_my_yestaday_add);
        invite_info_my_yestaday_activity = findViewById(R.id.invite_info_my_yestaday_activity);

        SetItemContent(invite_info_amount, R.drawable.center_iv6, R.string.invite_team_info_invite_amount, "22", true);
        SetItemContent(invite_info_team_activity_conter, R.drawable.center_iv6, R.string.invite_team_info_team_activity, "22", false);
        SetItemContent(invite_info_my_activity_conter, R.drawable.center_iv6, R.string.invite_team_info_my_activity, "22", false);
        SetItemContent(invite_info_my_yestaday_add, R.drawable.center_iv6, R.string.invite_team_info_yestaday_add, "22", false);
        SetItemContent(invite_info_my_yestaday_activity, R.drawable.center_iv6, R.string.invite_team_info_yestaday_activity, "22", false);


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
    protected void MyClick(View V) {

        switch (V.getId()){
            case R.id.invite_info_amount://邀请人数
                PromptManager.SkipActivity(BaseActivity,new Intent(BaseContext,AInviteFriendRecord.class));
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
