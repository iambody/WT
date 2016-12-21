package io.vtown.WeiTangApp.ui.title.center.set;

import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.comment.BUpData;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.upgrade.UpdateManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.onConfirmClick;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.oncancelClick;
import io.vtown.WeiTangApp.event.interf.ICustomDialogResult;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.service.DownloadService;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.AWeb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-22 下午10:53:11 关于微糖页面
 */
public class AAboutWt extends ATitleBase {

    /**
     * 应用ICON
     */
    private ImageView iv_app_icon;
    /**
     * 应用版本
     */
    private TextView tv_app_version;
    /**
     * 新版更新
     */
    private View new_version_update;
    /**
     * 等级规则
     */
    private View opinion_dengjiguize;
    /**
     * 关于我们
     */
    private View opinion_abouus;
    /**
     * 功能介绍
     */
    private View function_introduce;
    /**
     * 意见反馈
     */
    private View opinion_jieshao;
    /**
     * 联系客服
     */
    private View contact_service;

//	/**
//	 * 服务条款
//	 */
//	private TextView tv_service_clause;
//	/**
//	 * 隐私声明
//	 */
//	private TextView tv_private_statement;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_center_set_personal_data_about_wt);
        IView();
    }

    private void IView() {

        iv_app_icon = (ImageView) findViewById(R.id.iv_app_icon);
        tv_app_version = (TextView) findViewById(R.id.tv_app_version);
        new_version_update = findViewById(R.id.new_version_update);
        function_introduce = findViewById(R.id.function_introduce);
        opinion_jieshao = findViewById(R.id.opinion_feedback);
        contact_service = findViewById(R.id.contact_service);
        opinion_dengjiguize = findViewById(R.id.opinion_dengjiguize);
        opinion_abouus = findViewById(R.id.opinion_abouus);
//		tv_service_clause = (TextView) findViewById(R.id.tv_service_clause);
//		tv_private_statement = (TextView) findViewById(R.id.tv_private_statement);
        StrUtils.SetTxt(tv_app_version,
                "版本" + Constants.getVersionName(BaseContext));
        SetItemContent(new_version_update, R.string.new_version_update, "当前版本："
                + Constants.getVersionName(BaseContext));
        SetItemContent(function_introduce, R.string.function_introduce, "");
        SetItemContent(opinion_abouus, R.string.function_guanyuwomen, "");


        SetItemContent(opinion_jieshao, R.string.opinion_feedback, "");
        SetItemContent(contact_service, R.string.contact_service, "");
        SetItemContent(opinion_dengjiguize, R.string.contact_dengjiguize, "");

        SetWdithIv();
    }

    private void SetWdithIv() {
        Drawable drawable = getResources().getDrawable(R.drawable.login_my_log);
        Bitmap roundedCornerBitmap = StrUtils.getRoundedCornerBitmap(StrUtils.drawableToBitmap(drawable), 10);
        iv_app_icon.setImageBitmap(roundedCornerBitmap);
        LinearLayout.LayoutParams PS = new LayoutParams(screenWidth / 4,
                screenWidth / 4);
        PS.setMargins(screenWidth / 6, screenWidth / 9, screenWidth / 6, 0);
        iv_app_icon.setLayoutParams(PS);
    }

    /**
     * 查看用户协议
     */
    private void LookUsrProtocol() {
        PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                AWeb.class).putExtra(AWeb.Key_Bean, new BComment(
                Constants.AppDeal_Url, "用户协议")));
    }

    /**
     * 查看功能介绍
     */
    private void LookGongneng() {
        PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                AWeb.class).putExtra(AWeb.Key_Bean, new BComment(
                Constants.AppGongnneg_Url, "功能介绍")));
    }

    private void SetItemContent(View VV, int ResourceTitle, String ResourceRight) {
        ((TextView) VV.findViewById(R.id.comment_txtarrow_title))
                .setText(getResources().getString(ResourceTitle));
        if (!StrUtils.isEmpty(ResourceRight)) {
            ((TextView) VV.findViewById(R.id.comment_txtarrow_content))
                    .setText(ResourceRight);
        }
        VV.setOnClickListener(this);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt(getString(R.string.about_w_town));
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

        if (StrUtils.isEmpty(Data.getHttpResultStr())) {
            // PromptManager.ShowCustomToast(BaseContext, getResources()
            // .getString(R.string.wt_app_new_version));
            return;
        }

        BUpData data = JSON.parseObject(Data.getHttpResultStr(), BUpData.class);
        if (data.getCode() > Constants.getVersionCode(BaseContext)) {// 需要升级

            // status 1强制升级2不强制升级
            switch (data.getStatus()) {
                case 1:// 强制升级
                    UpdateManager m = new UpdateManager(BaseContext, data.getUrl(),
                            data.getDesc(), data.getVersion());// "产品进行了优化\n部分功能进行升级"
                    m.UpDown();
                    break;
                case 2:// 不强制升级
                    ShowCustomDialog(data);
                    break;
                default:
                    break;
            }

        } else {// 不需要升级
            //PromptManager.ShowCustomToast(BaseContext, getResources()
            //.getString(R.string.wt_app_new_version));
            ShowPromptCustomDialog(Constants.getVersionName(BaseContext)
                            + getString(R.string.wt_app_new_version),
                    new ICustomDialogResult() {

                        @Override
                        public void onResult() {

                        }
                    });
            return;
        }

    }

    /**
     * 左右选择弹出框的封装
     */

    public void ShowCustomDialog(final BUpData data) {
        final CustomDialog dialog = new CustomDialog(BaseContext,
                R.style.mystyle, R.layout.dialog_purchase_cancel, 1,
                getResources().getString(R.string.hulie_version),
                getResources().getString(R.string.updown_version));
        dialog.show();
        dialog.setTitleText(getResources()
                .getString(R.string.check_new_version));
        dialog.Settitles(getResources().getString(R.string.new_version)
                + data.getVersion() + "\n" + data.getDesc());

        dialog.setcancelListener(new oncancelClick() {

            @Override
            public void oncancelClick(View v) {
                dialog.dismiss();

            }
        });

        dialog.setConfirmListener(new onConfirmClick() {
            @Override
            public void onConfirmCLick(View v) {
                dialog.dismiss();
                Intent mIntent = new Intent(BaseContext, DownloadService.class);
                mIntent.putExtra(DownloadService.INTENT_URL, data.getUrl());
                mIntent.putExtra(DownloadService.Desc, data.getDesc());
                startService(mIntent);
            }
        });
    }

    @Override
    protected void DataError(String error, int LoadTyp) {
        PromptManager.ShowCustomToast(BaseContext, error);
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
            case R.id.opinion_abouus:
                PromptManager.SkipActivity(BaseActivity, new Intent(
                        BaseActivity, AWeb.class).putExtra(
                        AWeb.Key_Bean,
                        new BComment(getResources().getString(R.string.wt_weburl) , getResources().getString(R.string.about_w_town))));
                break;
            case R.id.opinion_dengjiguize://等级规则
                PromptManager.SkipActivity(BaseActivity, new Intent(
                        BaseActivity, AWeb.class).putExtra(
                        AWeb.Key_Bean,
                        new BComment(Constants.Home_Level, getResources().getString(R.string.dengjiguize))));
                break;
            case R.id.new_version_update:// 版本更新
                CheckUp();
                break;

            case R.id.function_introduce:
                LookUsrProtocol();
                break;

            case R.id.opinion_feedback:// 功能介绍
                LookGongneng();
                // PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                // AOpinionFeedback.class));
                break;

            case R.id.contact_service:
                ContactService();
                break;

            default:
                break;
        }
    }

    /**
     * 检查版本
     */
    private void CheckUp() {
        SetTitleHttpDataLisenter(this);
        HashMap<String, String> map = new HashMap<String, String>();
        // map.put("sellerid", Spuit.User_Get(BaseContext).getSeller_id());
        FBGetHttpData(map, Constants.UpData, Method.GET, 0, LOAD_INITIALIZE);

        // mBaseStr.getData(Constants.UpData, new HashMap<String, String>(),
        // Method.GET);

    }

    /**
     * 联系客服---拨号
     */

    @Override
    protected void InItBundle(Bundle bundle) {
    }

    @Override
    protected void SaveBundle(Bundle bundle) {
    }

}
