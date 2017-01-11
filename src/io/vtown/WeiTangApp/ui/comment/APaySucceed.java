package io.vtown.WeiTangApp.ui.comment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
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
import io.vtown.WeiTangApp.comment.view.pop.PShare;
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
    public static final String Key_ShareMony = "sharemony";
    private boolean IsShareBean;


    private String OderNumber;
    private BUser MyUser;
    private int ShareMony = 0;
    //第一次进来需要获取数据封装好的bean
    private BNew HttpDataShareBeanNew = null;
    private View mView;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_paysucceed);
        mView = LayoutInflater.from(this).inflate(R.layout.activity_paysucceed, null);
        MyUser = Spuit.User_Get(BaseContext);
        SetTitleHttpDataLisenter(this);
        InItMyBundle();
        IBaseInit();
        if (!IsShareBean) IDataNet();
        if (IsShareBean)
            StrUtils.SetTxt(pay_succeed_mony, String.format("恭喜获得%s元红包", (ShareMony / 100) + ""));

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
            ShareBeanNew.setShare_url(ShareBeanNew.getSharing_url());
            ShareMony = getIntent().getIntExtra(Key_ShareMony, 0);
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

    }


    @Override
    protected void InitTile() {

    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        String HttpData = Data.getHttpResultStr();
        if (!StrUtils.isEmpty(HttpData)) {

            HttpDataShareBeanNew = JSON.parseObject(HttpData, BNew.class);
            StrUtils.SetTxt(pay_succeed_mony, String.format("恭喜获得%s元红包", (HttpDataShareBeanNew.getPacket_money() / 100) + ""));
            ShareBeanNew = new BNew();
            ShareBeanNew.setShare_title(HttpDataShareBeanNew.getPacket_name());
            ShareBeanNew.setShare_log(HttpDataShareBeanNew.getPacket_img());
            ShareBeanNew.setShare_content(getResources().getString(R.string.share_lingqu));
            ShareBeanNew.setShare_url(HttpDataShareBeanNew.getSharing_url());
        }


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
                if (ShareBeanNew != null) {
                    PShare da = new PShare(BaseContext, ShareBeanNew, false);
                    da.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
                    da.GetShareResult(new PShare.ShareResultIntface() {
                        @Override
                        public void ShareResult(int ResultType) {
                            BaseActivity.finish();

                        }
                    });
                }

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
