package io.vtown.WeiTangApp.ui.comment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.order.ACenterMyOrder;

/**
 * Created by datutu on 2017/1/4.
 */

public class APaySucceed extends ATitleBase {
    private BNew ShareBeanNew = null;
    public static final String Key_Oder = "odernumber";
    public static final String Key_ShareBean = "sharebean";
    public static final String Key_IsShareBean = "issharebean";
    private String OderNumber;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_paysucceed);
        InItMyBundle();
        IBaseInit();
    }

    private void InItMyBundle() {
        if (getIntent().getExtras().containsKey(Key_Oder)) {
            OderNumber = getIntent().getStringExtra(Key_Oder);
        } else {
            BaseActivity.finish();
        }
    }

    private void IBaseInit() {
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

                // sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);
                // sp.setText("大兔兔的测试数据");
                // sp.setImageUrl("http://static.freepik.com/free-photo/letter-a-underlined_318-8682.jpg");
                // sp.setTitle("大兔兔的title");//
                // sp.setUrl("www.baidu.com");

                sp.setText(ShareBeanNew.getShare_content());
                sp.setImageUrl(ShareBeanNew.getShare_log());
                sp.setTitle(ShareBeanNew.getShare_title());//
                sp.setUrl(ShareBeanNew.getShare_url());
                break;
            case 2:// 朋友圈分享
                platform = ShareSDK.getPlatform(BaseContext, WechatMoments.NAME);

                sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);}

                // sp.setText("大兔兔的测试数据");
                // sp.setImageUrl("http://static.freepik.com/free-photo/letter-a-underlined_318-8682.jpg");
                // sp.setTitle("大兔兔的测试数据");//
                // sp.setUrl("www.baidu.com");
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
//            case R.id.activity_paysucceed_bt:
////                PromptManager.SkipActivity(BaseActivity, new Intent(
////                        BaseActivity, ACenterMyOrder.class));
////                BaseActivity.finish();
//                break;
        }
    }

    @Override
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }
}
