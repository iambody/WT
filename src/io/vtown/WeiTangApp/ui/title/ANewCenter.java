package io.vtown.WeiTangApp.ui.title;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.ACommentList;
import io.vtown.WeiTangApp.ui.title.center.myshow.ARecyclerMyShow;
import io.vtown.WeiTangApp.ui.title.center.set.AAboutWt;
import io.vtown.WeiTangApp.ui.title.center.set.APersonalData;
import io.vtown.WeiTangApp.ui.title.shop.center.AShopData;

/**
 * Created by Yihuihua on 2016/10/31.
 * 我的页面
 */

public class ANewCenter extends ATitleBase {

    private View view_personal_data;
    private View view_good_soucang;
    private View view_shop_guanzhu;
    private View view_scan_record;
    private View view_setting;
    private View view_my_show;
    private View view_about_me;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_new_center);
        IView();
    }

    private void IView() {

        view_personal_data = findViewById(R.id.view_personal_data);
        view_my_show = findViewById(R.id.view_my_show);
        view_good_soucang = findViewById(R.id.view_good_soucang);
        view_shop_guanzhu = findViewById(R.id.view_shop_guanzhu);
        view_scan_record = findViewById(R.id.view_scan_record);
        view_about_me = findViewById(R.id.view_about_me);
        view_setting = findViewById(R.id.view_setting);

        SetItemContent(view_personal_data, R.drawable.shop_grad9, R.string.my_personal_data);
        SetItemContent(view_my_show, R.drawable.center_iv1, R.string._my_show);
        SetItemContent(view_good_soucang, R.drawable.center_iv6, R.string.my_good_shouchang);
        SetItemContent(view_shop_guanzhu, R.drawable.center_iv7, R.string.my_shop_guanzhu);
        SetItemContent(view_scan_record, R.drawable.center_iv8, R.string.my_scan_record);
        SetItemContent(view_about_me, R.drawable.tab1_nor, R.string.about_w_town);
        SetItemContent(view_setting, R.drawable.shop_grad9, R.string.my_setting);

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
            case R.id.view_personal_data:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext, AShopData.class));
                break;

            case R.id.view_my_show:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ARecyclerMyShow.class).putExtra("seller_id", Spuit.User_Get(BaseContext).getSeller_id()));
                break;

            case R.id.view_good_soucang:
                Intent intent = new Intent(BaseActivity, ACommentList.class);
                intent.putExtra(ACommentList.Tage_ResultKey,
                        ACommentList.Tage_ACenterOderGuanzhu);

                PromptManager.SkipActivity(BaseActivity, intent);
                break;
            case R.id.view_shop_guanzhu:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ACommentList.class).putExtra(ACommentList.Tage_ResultKey,
                        ACommentList.Tage_ACenterShopCollect));
                break;
            case R.id.view_scan_record:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ACommentList.class).putExtra(ACommentList.Tage_ResultKey,
                        ACommentList.Tage_ACenterGoodBrowseRecord));
                break;

            case R.id.view_about_me:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                        ASearchResult.class));
                break;

            case R.id.view_setting:
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext, APersonalData.class));
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
