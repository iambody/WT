package io.vtown.WeiTangApp.ui.title.zhuanqu;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.channl.BChannl;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.QRCodeUtil;
import io.vtown.WeiTangApp.comment.util.SdCardUtils;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by Yihuihua on 2016/11/15.
 */

public class ABrandShopShare extends ATitleBase {
    @BindView(R.id.brand_shop_code)
    ImageView brandShopCode;
    @BindView(R.id.brand_shop_share_shop_ic)
    TextView brand_shop_share_shop_ic;
    @BindView(R.id.brand_shop_share_null)
    TextView brand_shop_share_null;
    @BindView(R.id.brand_shop_share_to_wx)
    LinearLayout brandShopShareToWx;
    @BindView(R.id.brand_shop_share_to_frends)
    LinearLayout brandShopShareToFrends;
    private Bitmap MyBitMap;

    public static String Tage_Key = "code_url";
    public static String Tage_Avatar_Key = "avatar_url";
    public static String Tage_Seller_Id = "seller_id";
    public static String Tage_Title = "seller_title";
    private String seller_url = "";
    private String avatar = "";
    private String seller_id = "";
    private String seller_title = "";
    private Unbinder mBind;

    @Override
    protected void InItBaseView() {

        setContentView(R.layout.activity_brand_shop_share);
        mBind = ButterKnife.bind(this);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        overridePendingTransition(R.anim.slide_in, 0);
        IBundle();
        IView();
        ToBeCode();

    }

    private void IView() {
        brand_shop_share_null.setOnClickListener(this);
        if(!StrUtils.isEmpty(seller_id)){
            StrUtils.SetTxt(brand_shop_share_shop_ic,"店铺ID:"+seller_id);
        }
        brandShopShareToWx.setOnClickListener(this);
        brandShopShareToFrends.setOnClickListener(this);
    }

    private void ToBeCode() {
       // ISizeCodeIv();
        if (!StrUtils.isEmpty(seller_url)) {
            String InItCode = SdCardUtils.CodePath(BaseContext) + "brand_code.jpg";
            CreatQ(seller_url, InItCode);
        }

    }

    private void CreatQ(final String Str, final String Pathe) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Bitmap logoBm = com.nostra13.universalimageloader.core.ImageLoader
                        .getInstance().loadImageSync(avatar);
                boolean success = QRCodeUtil.createQRImage(Str, 800, 800,
                        logoBm, Pathe);

                if (success) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyBitMap = BitmapFactory.decodeFile(Pathe);
                            brandShopCode.setImageBitmap(MyBitMap);
                            brandShopCode.setVisibility(View.VISIBLE);
//							new ImagViewDialog(BaseContext, MyBitMap, screenWidth, 2)
//									.show();
                        }
                    });
                }
            }
        }).start();

    }

    private void IBundle() {
        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(Tage_Key)) {
            seller_url = getIntent().getStringExtra(Tage_Key);
            avatar = getIntent().getStringExtra(Tage_Avatar_Key);
        }

        if(getIntent().getExtras() != null && getIntent().getExtras().containsKey(Tage_Seller_Id)){
            seller_id = getIntent().getExtras().getString(Tage_Seller_Id);
        }

        if(getIntent().getExtras() != null && getIntent().getExtras().containsKey(Tage_Title)){
            seller_title = getIntent().getExtras().getString(Tage_Title);
        }
    }

    private void ISizeCodeIv() {
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(
                screenWidth / 2, screenWidth / 2);
        mLayoutParams.gravity = Gravity.CENTER;
        brandShopCode.setLayoutParams(mLayoutParams);

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
            case R.id.brand_shop_share_null:
                overridePendingTransition(0, R.anim.slide_out);
                this.finish();
                break;

            case R.id.brand_shop_share_to_wx://分享微信
                Share(1);
                break;

            case R.id.brand_shop_share_to_frends://分享朋友圈
                Share(2);
                break;
        }
    }


    /**
     * 分享
     *
     * @param Type
     */
    private void Share(int Type) {
        if (!Constants.isWeixinAvilible(BaseContext)) {
            PromptManager.ShowCustomToast(BaseContext, "请安装微信");
            return;
        }

        ShareSDK.initSDK(BaseContext);
        Platform platform = null;
        Platform.ShareParams sp = new Platform.ShareParams();
        switch (Type) {
            case 1:// 好友分享
                platform = ShareSDK.getPlatform(BaseContext, Wechat.NAME);
                sp.setShareType(Platform.SHARE_WEBPAGE);

                sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);

                sp.setImageUrl(avatar);
                sp.setTitle(seller_title);//
                sp.setText(seller_title);
                sp.setUrl(seller_url);

//                BNew mBNew = new BNew();
//                mBNew.setShare_url(seller_url);
//                mBNew.setShare_content(seller_title);
//                mBNew.setShare_title(seller_title);
//                mBNew.setShare_log(avatar);


                break;
            case 2:// 朋友圈分享
                platform = ShareSDK.getPlatform(BaseContext, WechatMoments.NAME);
                sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);

                sp.setText(seller_title);
                sp.setImageUrl(avatar);
                sp.setTitle(seller_title);//
                sp.setUrl(seller_url);
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
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CacheUtil.BitMapRecycle(MyBitMap);
        overridePendingTransition(0, R.anim.slide_out);
        mBind.unbind();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, R.anim.slide_out);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out);
    }
}
