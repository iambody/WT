package io.vtown.WeiTangApp.ui.comment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BShowShare;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.order.ACenterMyOrder;

/**
 * Created by datutu on 2017/1/4.
 */

public class APaySucceed extends ATitleBase {

    private TextView paysucced_share_bt;
    private TextView pay_succeed_mony;

    private BNew ShareBeanNew = null;
    //
    public static final String Key_Oder = "odernumber";
    public static final String Key_ShareBean = "sharebean";
    public static final String Key_IsShareBean = "issharebean";

    private boolean IsShareBean;


    private String OderNumber;
    private BUser MyUser;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_paysucceed);
        MyUser = Spuit.User_Get(BaseContext);
        SetTitleHttpDataLisenter(this);
        InItMyBundle();
        IBaseInit();
        if (!IsShareBean) IDataNet();
    }

    private void IDataNet() {//
        HashMap<String, String> map = new HashMap<>();
        map.put("member_id", MyUser.getMember_id());
        map.put("order_sn", OderNumber);
        FBGetHttpData(map, Constants.OderNumbe_GetShareBean, Request.Method.GET, 1,
                LOAD_INITIALIZE);
    }

    private void InItMyBundle() {
        IsShareBean = getIntent().getBooleanExtra(Key_IsShareBean, false);
        if (IsShareBean) {
            ShareBeanNew = (BNew) getIntent().getSerializableExtra(Key_ShareBean);
        } else if (getIntent().getExtras().containsKey(Key_Oder)) {
            OderNumber = getIntent().getStringExtra(Key_Oder);
        } else {
            BaseActivity.finish();
        }
    }

    private void IBaseInit() {

        paysucced_share_bt = (TextView) findViewById(R.id.paysucced_share_bt);
        pay_succeed_mony = (TextView) findViewById(R.id.pay_succeed_mony);
        paysucced_share_bt.setOnClickListener(this);
        ShareSDK.initSDK(BaseContext);
    }

    private void Share(int Type) {
        if (!ViewUtils.isWeixinAvilible(BaseContext)) {
            PromptManager.ShowCustomToast(BaseContext, "请先安装手机微信");
            return;
        }
        Platform platform = null;
        Platform.ShareParams sp = new Platform.ShareParams();
        switch (Type) {
            case 1:// 好友分享
                platform = ShareSDK.getPlatform(BaseContext, Wechat.NAME);

                sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);}
                sp.setText(ShareBeanNew.getShare_content());
                sp.setImageUrl(ShareBeanNew.getShare_log());
                sp.setTitle(ShareBeanNew.getShare_title());//
                sp.setUrl(ShareBeanNew.getShare_url());
                break;
            case 2:// 朋友圈分享
                platform = ShareSDK.getPlatform(BaseContext, WechatMoments.NAME);

                sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);}
                sp.setText(ShareBeanNew.getShare_content());
                sp.setImageUrl(ShareBeanNew.getShare_log());
                sp.setTitle(ShareBeanNew.getShare_title());//
                sp.setUrl(ShareBeanNew.getShare_url());
                break;
            default:
                break;
        }
        platform.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                PromptManager.ShowCustomToast(BaseContext, "分享取消");

            }

            @Override
            public void onComplete(Platform arg0, int arg1,
                                   HashMap<String, Object> arg2) {
                PromptManager.ShowCustomToast(BaseContext, "分享完成");

            }

            @Override
            public void onCancel(Platform arg0, int arg1) {

            }
        });
        platform.share(sp);
    }

    @Override
    protected void InitTile() {

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
        switch (V.getId()) {
            case R.id.paysucced_share_bt:


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
