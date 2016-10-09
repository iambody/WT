package io.vtown.WeiTangApp.ui.title.center.wallet;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BLBankCardAndAlipayList;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-20 上午11:02:18 支付宝管理页面
 */
public class AAlipayManager extends ATitleBase {

    /**
     * 支付宝头像
     */
    private ImageView iv_alipay_icon;
    /**
     * 支付宝姓名
     */
    private TextView tv_real_alipay_name;
    /**
     * 支付宝账号
     */
    private TextView tv_alipay_id;
    /**
     * 按钮
     */
    private Button btn_add_or_modify_alipay;
    /**
     * 没有支付宝下的提示
     */
    private TextView tv_no_alipay_desc;
    /**
     * 有支付宝的布局
     */
    private LinearLayout ll_alipay_layout;

    private int mClick_type = 0;
    private BLBankCardAndAlipayList mDatas = null;
    /**
     * 用户信息
     */
    private BUser user_Get;
    private LinearLayout center_wallet_alipay_manage_outlay;
    private View center_wallet_alipay_manage_nodata_lay;
    /**
     * 获取数据为空时绑定支付宝按钮
     */
    private Button btn_add_new_alipay1;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_center_wallet_alipay_manage);
        user_Get = Spuit.User_Get(getApplicationContext());
        IView();
        ICache();
        IData();
    }

    private void ICache() {
        String alipay_cache = CacheUtil.Center_Wallet_Alipay_Get(BaseContext);
        if (!StrUtils.isEmpty(alipay_cache)) {
            try {
                mDatas = JSON.parseObject(alipay_cache, BLBankCardAndAlipayList.class);
            } catch (Exception e) {
                return;
            }
            RefreshView(mDatas);
        } else {
            PromptManager.showtextLoading(BaseContext,
                    getResources()
                            .getString(R.string.xlistview_header_hint_loading));
        }
    }

    private void IView() {

        boolean isFinish = getIntent().getBooleanExtra("isFinish", false);
        if (isFinish) {
            IData();
        }

        center_wallet_alipay_manage_outlay = (LinearLayout) findViewById(R.id.center_wallet_alipay_manage_outlay);
        center_wallet_alipay_manage_nodata_lay = findViewById(R.id.center_wallet_alipay_manage_nodata_lay);
        IDataView(center_wallet_alipay_manage_outlay,
                center_wallet_alipay_manage_nodata_lay, NOVIEW_INITIALIZE);

        iv_alipay_icon = (ImageView) findViewById(R.id.iv_alipay_icon);

        tv_real_alipay_name = (TextView) findViewById(R.id.tv_real_alipay_name);

        tv_alipay_id = (TextView) findViewById(R.id.tv_alipay_id);

        btn_add_or_modify_alipay = (Button) findViewById(R.id.btn_add_or_modify_alipay);

        tv_no_alipay_desc = (TextView) findViewById(R.id.tv_no_alipay_desc);

        ll_alipay_layout = (LinearLayout) findViewById(R.id.ll_alipay_layout);

        btn_add_new_alipay1 = (Button) findViewById(R.id.btn_add_new_alipay1);

        btn_add_or_modify_alipay.setOnClickListener(this);
        ll_alipay_layout.setOnClickListener(this);
        center_wallet_alipay_manage_nodata_lay.setOnClickListener(this);
        btn_add_new_alipay1.setOnClickListener(this);
    }

    private void IData() {

        SetTitleHttpDataLisenter(this);

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", user_Get.getId());
        FBGetHttpData(map, Constants.MY_ALIPAY_LIST, Method.GET, 0,
                LOAD_INITIALIZE);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);
        setIntent(intent);
        IData();
    }

    @Override
    protected void InitTile() {
        SetTitleTxt(getString(R.string.alipay_manage));

    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

        if (StrUtils.isEmpty(Data.getHttpResultStr())) {
            if (LOAD_INITIALIZE == Data.getHttpLoadType()) {
                IDataView(center_wallet_alipay_manage_outlay,
                        center_wallet_alipay_manage_nodata_lay, NOVIEW_ERROR);
                btn_add_new_alipay1.setVisibility(View.VISIBLE);
                ShowErrorCanLoad(getResources().getString(R.string.error_null_alipay));
                center_wallet_alipay_manage_nodata_lay.setClickable(false);
            }
            return;
        }

        mDatas = new BLBankCardAndAlipayList();
        try {

            mDatas = JSON.parseObject(Data.getHttpResultStr(), BLBankCardAndAlipayList.class);

        } catch (Exception e) {
            DataError("解析失败", 1);
        }
        IDataView(center_wallet_alipay_manage_outlay,
                center_wallet_alipay_manage_nodata_lay, NOVIEW_RIGHT);

        if (StrUtils.isEmpty(mDatas.getAlipay())) {// 说明没有进行过绑定
            ll_alipay_layout.setVisibility(View.GONE);
            tv_no_alipay_desc.setVisibility(View.VISIBLE);
            btn_add_or_modify_alipay.setText("绑定支付宝");
            mClick_type = 1;
            return;
        } else {// 已经进行过绑定

        }
        CacheUtil.Center_Wallet_Alipay_Save(BaseContext, Data.getHttpResultStr());
        RefreshView(mDatas);

    }

    /**
     * 刷新View
     *
     * @param datas
     */
    private void RefreshView(BLBankCardAndAlipayList datas) {
        ll_alipay_layout.setVisibility(View.VISIBLE);
        tv_no_alipay_desc.setVisibility(View.GONE);
        StrUtils.SetTxt(tv_real_alipay_name, datas.getName());
        StrUtils.SetTxt(tv_alipay_id, datas.getAlipay());

        btn_add_or_modify_alipay.setText("修改支付宝");
        mClick_type = 2;

    }

    @Override
    protected void DataError(String error, int LoadTyp) {
        PromptManager.ShowMyToast(BaseContext, error);
        if (LOAD_INITIALIZE == LoadTyp) {
            IDataView(center_wallet_alipay_manage_outlay,
                    center_wallet_alipay_manage_nodata_lay, NOVIEW_ERROR);
            btn_add_new_alipay1.setVisibility(View.VISIBLE);
            ShowErrorCanLoad(getResources().getString(R.string.error_null_noda));
            center_wallet_alipay_manage_nodata_lay.setClickable(true);
        }
    }

    @Override
    protected void NetConnect() {
        NetError.setVisibility(View.GONE);
        IData();
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
            case R.id.btn_add_or_modify_alipay:
                if (CheckNet(BaseContext)) return;
                switch (mClick_type) {
                    case 1:

                        Intent intent = new Intent(BaseActivity, AAddAliPay.class);
                        intent.putExtra("togo", 0);
                        PromptManager.SkipActivity(BaseActivity, intent);

                        break;

                    case 2:
                        Intent intent2 = new Intent(BaseActivity, AModifyAlipay.class);
                        if (mDatas != null && !StrUtils.isEmpty(mDatas.getAlipay())
                                && !StrUtils.isEmpty(mDatas.getName())) {
                            intent2.putExtra("alipay_info", mDatas);
                        }

                        PromptManager.SkipActivity(BaseActivity, intent2);
                        break;

                }

                break;

            case R.id.center_wallet_alipay_manage_nodata_lay:// 重新获取数据
                if (CheckNet(BaseContext)) return;
                IData();
                break;

            case R.id.btn_add_new_alipay1://绑定支付宝
                if (CheckNet(BaseContext)) return;
                Intent intent = new Intent(BaseActivity, AAddAliPay.class);
                intent.putExtra("togo", 0);
                PromptManager.SkipActivity(BaseActivity, intent);
                break;

            default:
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
