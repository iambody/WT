package io.vtown.WeiTangApp.ui.title;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.BCMyLeader;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.util.ui.UiHelper;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.AWeb;
import io.vtown.WeiTangApp.ui.comment.im.AChatLoad;
import io.vtown.WeiTangApp.ui.title.loginregist.bindcode_three.ANewBindCode;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;

/**
 * Created by Yihuihua on 2016/10/24.
 */

public class AMyLeader extends ATitleBase {
    @BindView(R.id.iv_my_leader_icon)
    ImageView ivMyLeaderIcon;
    @BindView(R.id.tv_my_leader_name)
    TextView tvMyLeaderName;
    @BindView(R.id.tv_my_leader_usertag)
    TextView tvMyLeaderUsertag;
    @BindView(R.id.sv_my_leader)
    ScrollView sv_my_leader;

    @BindView(R.id.tv_my_leader_phone)
    TextView tvMyLeaderPhone;
    @BindView(R.id.iv_my_leader_back_icon)
    ImageView ivMyLeaderBackIcon;
    @BindView(R.id.tv_my_leader_look_guize)
    LinearLayout tvMyLeaderLookGuize;
    @BindView(R.id.iv_my_leader_invite_code)
    TextView ivMyLeaderInviteCode;
    @BindView(R.id.tv_my_leader_bind_super)
    LinearLayout tvMyLeaderBindSuper;

    private Unbinder mBind;
    private View mRootView;
    private View my_leader_nodata_lay;
    private BCMyLeader leader;

    //如果是机器人 跳转===>ANewBind
    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_my_leader);
        mRootView = LayoutInflater.from(BaseContext).inflate(R.layout.activity_my_leader, null);
        EventBus.getDefault().register(this, "onRefrashView", BMessage.class);
        mBind = ButterKnife.bind(this);
        IView();
        ICache();
        IData();

    }

    private void ICache() {
        String s = CacheUtil.My_Super_Get(BaseContext);
        if (StrUtils.isEmpty(s)) {
            PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.loading));
        } else {
            try {
                leader = JSON.parseObject(s, BCMyLeader.class);
            } catch (Exception e) {
                return;
            }
            RefreshView(leader);
        }
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

        leader = JSON.parseObject(Data.getHttpResultStr(), BCMyLeader.class);
        CacheUtil.My_Super_Save(BaseContext, Data.getHttpResultStr());
        RefreshView(leader);
    }

    private void RefreshView(BCMyLeader leader) {
        if (!StrUtils.isEmpty(leader.getInvite_code())) {
            StrUtils.SetTxt(ivMyLeaderInviteCode, leader.getInvite_code());
        }

        if (1 == leader.getIs_ropot()) {
            tvMyLeaderBindSuper.setVisibility(View.VISIBLE);
        } else {
            tvMyLeaderBindSuper.setVisibility(View.GONE);
        }
        ImageLoaderUtil.Load2(leader.getAvatar(), ivMyLeaderIcon, R.drawable.error_iv2);
        StrUtils.SetTxt(tvMyLeaderName, leader.getSeller_name());
        StrUtils.SetTxt(tvMyLeaderPhone, leader.getPhone());
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
        ShowErrorCanLoad(getResources().getString(R.string.error_fuwuqi));
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

    public void onRefrashView(BMessage event) {
        int messageType = event.getMessageType();
        if (messageType == BMessage.Fragment_Home_Bind) {
            IData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBind.unbind();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {

        }
    }


    @OnClick({R.id.iv_my_leader_back_icon, R.id.iv_my_leader_icon, R.id.tv_my_leader_look_guize, R.id.tv_my_leader_bind_super})
    public void onClick(View V) {
        switch (V.getId()) {
            case R.id.iv_my_leader_back_icon:
                BaseActivity.finish();
                break;
            case R.id.iv_my_leader_icon:
                if (leader.getIs_ropot() == 1) //是机器人不需要查看其店铺
                    return;

//                BComment mBComment = new BComment(leader.getSeller_id(), leader.getSeller_name());
//                PromptManager.SkipActivity(BaseActivity, new Intent(
//                        BaseActivity, AShopDetail.class).putExtra(
//                        BaseKey_Bean, mBComment));

                //lsoso
//跳转聊天页面
                PromptManager.SkipActivity(
                        BaseActivity,
                        new Intent(BaseActivity, AChatLoad.class)
                                .putExtra(AChatLoad.Tage_TageId,
                                        leader.getSeller_id())
                                .putExtra(AChatLoad.Tage_Name,
                                        leader.getSeller_name())
                                .putExtra(AChatLoad.Tage_Iv,
                                        leader.getAvatar()));
                break;
            case R.id.tv_my_leader_look_guize:
                PromptManager.SkipActivity(BaseActivity, new Intent(
                        BaseActivity, AWeb.class).putExtra(
                        AWeb.Key_Bean,
                        new BComment(Constants.Homew_FanYong, getResources().getString(R.string.fanyongguize))));
                break;

            case R.id.tv_my_leader_bind_super://绑定上级
//如果是机器人 跳转===>ANewBind
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext, ANewBindCode.class));
                break;
        }

    }


}
