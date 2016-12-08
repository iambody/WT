package io.vtown.WeiTangApp.ui.title.myhome;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcache.BShop;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.BShowShare;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.ShowSelectPic;
import io.vtown.WeiTangApp.comment.view.pop.PShowShare;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.AExitNull;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.afragment.AMyShop;
import io.vtown.WeiTangApp.ui.comment.ACommentList;
import io.vtown.WeiTangApp.ui.comment.AGoodVidoShare;
import io.vtown.WeiTangApp.ui.title.ASearchResult;
import io.vtown.WeiTangApp.ui.title.center.myshow.ARecyclerMyShow;
import io.vtown.WeiTangApp.ui.title.center.set.AAboutWt;
import io.vtown.WeiTangApp.ui.title.center.set.AAddressManage;
import io.vtown.WeiTangApp.ui.title.center.set.APersonalData;
import io.vtown.WeiTangApp.ui.title.shop.center.AShopData;
import io.vtown.WeiTangApp.ui.ui.AAddNewShow;

/**
 * Created by Yihuihua on 2016/10/31.
 * 我的页面
 */

public class ANewCenter extends ATitleBase {

    private View view_personal_data;
    private View view_good_soucang;
    private View view_shop_guanzhu;
    private View view_scan_record;
    private View view_my_show;
    private View view_about_me;
    private View view_my_address;
    private View view_my_shop;
    private Button btn_quit;
    private View baseView;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_new_center);
        baseView = LayoutInflater.from(BaseContext).inflate(R.layout.activity_new_center, null);
        IView();
    }

    private void IView() {

        view_personal_data = findViewById(R.id.view_personal_data);
        view_my_show = findViewById(R.id.view_my_show);
        view_my_shop = findViewById(R.id.view_my_shop);
        view_my_address = findViewById(R.id.view_my_address);
        view_good_soucang = findViewById(R.id.view_good_soucang);
        view_shop_guanzhu = findViewById(R.id.view_shop_guanzhu);
        view_scan_record = findViewById(R.id.view_scan_record);
        view_about_me = findViewById(R.id.view_about_me);
        btn_quit = (Button) findViewById(R.id.btn_quit);

        SetItemContent(view_personal_data, R.drawable.ic_zhanghuxiangqing_nor, R.string.my_personal_data);
        SetItemContent(view_my_address, R.drawable.ic_wodedizhi_nor, R.string._my_address);
        SetItemContent(view_my_shop, R.drawable.shop_grad8, R.string._my_shop);
        SetItemContent(view_my_show, R.drawable.center_iv1, R.string._my_show);
        SetItemContent(view_good_soucang, R.drawable.ic_shangpinguanzhu_nor, R.string.my_good_shouchang);
        SetItemContent(view_shop_guanzhu, R.drawable.ic_dianpushoucang_nor, R.string.my_shop_guanzhu);
        SetItemContent(view_scan_record, R.drawable.ic_liulanjilu_nor, R.string.my_scan_record);
        SetItemContent(view_about_me, R.drawable.ic_guanyu_nor, R.string.about_w_town);
        btn_quit.setOnClickListener(this);

    }

    private void SetItemContent(View VV, int imgsre, int ResourceTitlet) {
        ImageView commentview_center_iv = (ImageView) VV.findViewById(R.id.commentview_center_iv);
        commentview_center_iv.setImageResource(imgsre);
        ((TextView) VV.findViewById(R.id.commentview_center_txt))
                .setText(getResources().getString(ResourceTitlet));

        VV.setOnClickListener(this);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt(getResources().getString(R.string.my_title));
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

    }

    @Override
    protected void DataError(String error, int LoadType) {

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
        if (CheckNet(BaseContext)) return;
        switch (V.getId()) {
            case R.id.view_personal_data://个人资料
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext, AShopData.class));
                break;

            case R.id.view_my_show://我的show
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ARecyclerMyShow.class).putExtra("seller_id", Spuit.User_Get(BaseContext).getSeller_id()));
                break;

            case R.id.view_good_soucang://商品收藏
                Intent intent = new Intent(BaseActivity, ACommentList.class);
                intent.putExtra(ACommentList.Tage_ResultKey,
                        ACommentList.Tage_ACenterOderGuanzhu);

                PromptManager.SkipActivity(BaseActivity, intent);
                break;
            case R.id.view_shop_guanzhu://店铺关注
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ACommentList.class).putExtra(ACommentList.Tage_ResultKey,
                        ACommentList.Tage_ACenterShopCollect));
                break;
            case R.id.view_scan_record://浏览记录
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ACommentList.class).putExtra(ACommentList.Tage_ResultKey,
                        ACommentList.Tage_ACenterGoodBrowseRecord));
                break;

            case R.id.view_about_me://关于我们
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        AAboutWt.class));

                break;

            case R.id.view_my_address://我的地址
                Intent intentss = new Intent(BaseActivity, AAddressManage.class);
                intentss.putExtra("NeedFinish", false);
                PromptManager.SkipActivity(BaseActivity, intentss);
                break;

            case R.id.view_my_shop://我的店铺
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext, AMyShop.class));
                break;

            case R.id.btn_quit://退出
                // 提示对话框
                ShowCustomDialog("确定退出该账号?", "取消", "退出", new IDialogResult() {
                    @Override
                    public void RightResult() {
                        PromptManager.showLoading(BaseContext);
//                        PromptManager.ShowCustomToast(BaseContext, "退出成功");
//                        AppManager.getAppManager().AppExit(BaseContext);

                        Spuit.Login_Out(BaseContext);
                        // 清理数据库
                        Spuit.Shop_Save(BaseContext, new BShop());
                        EventBus.getDefault().post(new BMessage(BMessage.Tage_Tab_Kill_Self));
//                        BaseActivity.finish();
                        PromptManager.SkipActivity(BaseActivity, new Intent(
                                BaseActivity, AExitNull.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        BaseActivity.finish();

                    }

                    @Override
                    public void LeftResult() {
                    }
                });
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
