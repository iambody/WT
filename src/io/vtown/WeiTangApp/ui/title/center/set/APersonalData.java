package io.vtown.WeiTangApp.ui.title.center.set;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcache.BShop;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DataCleanManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.custom.switchButtonView.EaseSwitchButton;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.AExitNull;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.loginregist.ALogin;
import io.vtown.WeiTangApp.ui.title.loginregist.ARealIdauth;
import io.vtown.WeiTangApp.ui.ui.AMain;
import io.vtown.WeiTangApp.ui.ui.AMainTab;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-22 下午3:37:38 个人资料页面
 */
public class APersonalData extends ATitleBase implements
        OnCheckedChangeListener {


    /**
     * 电话号码
     */
    private TextView tv_phone_numb;

    /**
     * 实名认证
     */
    private View authentication;
    /**
     * 地址管理
     */
//    private View address_manage;
    /**
     * 账户安全
     */
    private View account_safe;
    /**
     * 关于微糖
     */
    private View about_w_town;
    /**
     * 退出按钮
     */
    private Button btn_quit;
    /**
     * 认证状态
     */
    private TextView comment_txtarrow_content;
    /**
     * 是否认证
     */
    private boolean isLogin_RenZheng_Set;
    /**
     * 显示缓存
     */
    private TextView tv_cache_size;
    /**
     * 清除缓存
     */
    private EaseSwitchButton switch_delete_cache;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_center_set_personal_data);
//        Log.i("AMainTab","在个人界面开始发送广播");
//        EventBus.getDefault().post(new BMessage(BMessage.Tage_Tab_Kill_Self));
        IView();
        IData();
    }

    private void IData() {
        BUser user_Get = Spuit.User_Get(getApplicationContext());
        String phone = user_Get.getPhone();
        if (!StrUtils.isEmpty(phone)) {
            StrUtils.SetTxt(tv_phone_numb, phone.substring(0, 3) + "****"
                    + phone.substring(7));
        } 
        ICache();
        switch_delete_cache.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        isLogin_RenZheng_Set = Spuit.IsLogin_RenZheng_Set(BaseContext);
        if (isLogin_RenZheng_Set) {
            StrUtils.SetTxt(comment_txtarrow_content, "已认证");

        } else {
            StrUtils.SetTxt(comment_txtarrow_content, "未认证");
        }
    }

    private void ICache() {
        try {
            // String totalCacheSize =
            // DataCleanManager.getTotalCacheSize(BaseContext);
            String totalCacheSize = DataCleanManager
                    .getFormatSize(DataCleanManager
                            .getFolderSize1(getCacheDir()));
            StrUtils.SetTxt(tv_cache_size, totalCacheSize);

            // if(CacheManager.getCacheSize() >
            // 0){DataCleanManager.getFolderSize(getFilesDir())
            if (DataCleanManager.getFolderSize1(getCacheDir()) > 0) {
                switch_delete_cache.setEnabled(true);
                switch_delete_cache.setChecked(true);
            } else {
                switch_delete_cache.setChecked(false);
                switch_delete_cache.setEnabled(false);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void IView() {
        tv_phone_numb = (TextView) findViewById(R.id.tv_phone_numb);
        authentication = findViewById(R.id.authentication);
        comment_txtarrow_content = (TextView) authentication
                .findViewById(R.id.comment_txtarrow_content);
//        address_manage = findViewById(R.id.address_manage);
        account_safe = findViewById(R.id.account_safe);
        about_w_town = findViewById(R.id.about_w_town);
        tv_cache_size = (TextView) findViewById(R.id.tv_cache_size);
        switch_delete_cache = (EaseSwitchButton) findViewById(R.id.switch_delete_cache);
        btn_quit = (Button) findViewById(R.id.btn_quit);
        btn_quit.setOnClickListener(this);
        SetItemContent(authentication, R.string.authentication, "未认证");
//        SetItemContent(address_manage, R.string.address_manage, "");
        SetItemContent(account_safe, R.string.account_safe, "");
        SetItemContent(about_w_town, R.string.about_w_town, "");

    }

    @Override
    protected void InitTile() {

        SetTitleTxt(getString(R.string.personal_data));
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
    protected void DataResult(int Code, String Msg, BComment Data) {
    }

    @Override
    protected void DataError(String error, int LoadTyp) {
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
            case R.id.authentication:
                if (isLogin_RenZheng_Set) {
                    PromptManager.SkipActivity(BaseActivity, new Intent(
                            BaseActivity, ARealIdauthSucceed.class));
                } else {
                    PromptManager.SkipActivity(BaseActivity, new Intent(
                            BaseActivity, ARealIdauth.class).putExtra(
                            ARealIdauth.FROM_WHERE_KEY, 2));
                }

                break;

//            case R.id.address_manage:
//                if (CheckNet(BaseContext))
//                    return;
//                Intent intent = new Intent(BaseActivity, AAddressManage.class);
//                intent.putExtra("NeedFinish", false);
//                PromptManager.SkipActivity(BaseActivity, intent);
//
//                break;

            case R.id.account_safe:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        AAccountSafe.class));
                break;

            case R.id.about_w_town:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        AAboutWt.class));
                break;

            case R.id.btn_quit:// 推出
                // 提示对话框
                ShowCustomDialog("确定退出该账号?", "取消", "退出", new IDialogResult() {
                    @Override
                    public void RightResult() {
                        Spuit.Login_Out(BaseContext);
                        // 清理数据库
                        Spuit.Shop_Save(BaseContext, new BShop());
//                        PromptManager.ShowCustomToast(BaseContext, "退出成功");
//                        AppManager.getAppManager().AppExit(BaseContext);
//                        ActivityManager activityMgr = (ActivityManager) BaseActivity.getSystemService(Context.ACTIVITY_SERVICE);
//                        activityMgr.restartPackage(BaseActivity.getPackageName());
//                        System.exit(0);
//                        EventBus.getDefault().post(new BMessage(BMessage.Tage_Tab_Kill_Self));
//                        PromptManager.SkipActivity(BaseActivity, new Intent(
//                                BaseActivity, ALogin.class).putExtra("isexit",true));
                        System.exit(0);
//                        ScreenAppManager.getScreenManager().popAllActivityExceptOne(APersonalData.class);
//                        BaseActivity.finish();
//                        BaseActivity.finish();
//                        AppManager.getAppManager().finishAllActivity();
//                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

                    }

                    @Override
                    public void LeftResult() {
                    }
                });

                break;
        }
    }

    private void LoginOut() {
        Spuit.Login_Out(BaseContext);
        // 清理数据库
        Spuit.Shop_Save(BaseContext, new BShop());
        PromptManager.ShowCustomToast(BaseContext, "退出成功");
        PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
                ALogin.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    @Override
    protected void InItBundle(Bundle bundle) {
    }

    @Override
    protected void SaveBundle(Bundle bundle) {
    }

    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
        if (!isChecked) {
            // CacheManager.clearAllCache(BaseContext);
            DataCleanManager.cleanInternalCache(BaseContext);
            ICache();
        }

    }


}
