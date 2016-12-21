package io.vtown.WeiTangApp.ui.title.account;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcache.BShop;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.bean.bcomment.news.BPay;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PayResult;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.onConfirmClick;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.oncancelClick;
import io.vtown.WeiTangApp.comment.view.pop.PPassWord;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.event.interf.OnPasswordInputFinish;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.afragment.ACenterOder;
import io.vtown.WeiTangApp.ui.comment.order.ACenterMyOrder;
import io.vtown.WeiTangApp.ui.title.center.wallet.ACenterWallet;
import io.vtown.WeiTangApp.ui.title.loginregist.ALogin;
import io.vtown.WeiTangApp.ui.title.loginregist.ARealIdauth;
import io.vtown.WeiTangApp.ui.ui.AMainTab;
import io.vtown.WeiTangApp.ui.ui.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.android.volley.Request.Method;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;

import android.animation.ValueAnimator;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-6-1 下午12:58:35 收银台页面
 */
public class ACashierDesk extends ATitleBase {
    private View mView;
    /**
     * 订单编号
     */
    private TextView tv_cashier_desk_order_id;
    /**
     * 下单时间
     */
    private TextView tv_cashier_desk_create_time;
    /**
     * 订单金额
     */
    private TextView tv_cashier_desk_odermony;
    /**
     * 当现账户余额
     */
    private TextView tv_cashier_desk_now_account_balance;
    /**
     * 账户余额的
     */
    private LinearLayout cashier_desk_blance_lay;
    /**
     * 选择支付方式
     */
    private ImageView iv_cashier_desk_select;
    /**
     * 账户密码layout
     */
    private LinearLayout ll_cashier_desk_edit_psw;
    /**
     * 输入账户密码
     */
    private EditText et_cashier_desk_account_psw;
    /**
     * 忘记支付密码
     */
    private TextView tv_cashier_desk_forget_psw;
    /**
     * 微信支付
     */
    private View weixin_pay;
    /**
     * 支付宝支付
     */
    private View alipay_pay;
    /**
     * 银联支付
     */
    private View yinlian_pay;
    /**
     * 列表的view
     */
    private List<View> views;
    private RelativeLayout cashier_desks;

    /**
     * 还需支付金额
     */
    private TextView tv_cashier_desk_need_pay;
    /**
     * 去支付按钮
     */
    private TextView tv_cashier_desk_go_pay;

    /**
     * 是否选中账户余额状态
     */
    private boolean isBlanceCheck = false;
    /**
     * 是否选择微信支付状态
     */
    private int IsPayWay = 1;
    /**
     * 传进来的数据
     */
    private BDComment addOrderInfo;// blance是账户余额//money_paid是应付金额
    private BUser mBUser = new BUser();

    // 支付
    private IWXAPI msgApi;

    // 是否支持全部余额支付！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
    private boolean IsBlanceAllPay = false;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_cashier_desk);
        mView = LayoutInflater.from(BaseContext).inflate(
                R.layout.activity_cashier_desk, null);
        addOrderInfo = (BDComment) getIntent().getSerializableExtra(
                "addOrderInfo");
        if (addOrderInfo == null) {
            return;
        }
        EventBus.getDefault().register(this, "ReciverInf", BMessage.class);
        mBUser = Spuit.User_Get(BaseContext);
        SetTitleHttpDataLisenter(this);
        InItPay();
        IView();
        IInItData();
    }

    private void InItPay() {
        msgApi = WXAPIFactory.createWXAPI(BaseContext, null);
        // 将该app注册到微信
        // msgApi.registerApp("wxd930ea5d5a258f4f");
        msgApi.registerApp(Constants.WxPayKey);

    }

    /**
     * 初始化Data
     */
    private void IInItData() {
        StrUtils.SetTxt(tv_cashier_desk_order_id,
                String.format("订单编号：%1$s", addOrderInfo.getOrder_sn()));
        if (addOrderInfo.getCreate_time() != null)
            StrUtils.SetTxt(tv_cashier_desk_create_time, String.format(
                    "下单时间：%1$s", StrUtils.longtostr(Long.parseLong(addOrderInfo
                            .getCreate_time()))));

        StrUtils.SetTxt(tv_cashier_desk_need_pay,
                "￥" + StrUtils.SetTextForMony(addOrderInfo.getMoney_paid()));//

        if (StrUtils.isEmpty(addOrderInfo.getBlance())
                || StrUtils.toFloat(addOrderInfo.getBlance()) == 0f) {
            cashier_desk_blance_lay.setVisibility(View.GONE);
        } else {
            StrUtils.SetTxt(tv_cashier_desk_now_account_balance,
                    "￥" + StrUtils.SetTextForMony(addOrderInfo.getBlance()));
        }
        // 订单金额
        StrUtils.SetTxt(
                tv_cashier_desk_odermony,
                String.format("订单金额：%s元",
                        StrUtils.SetTextForMony(addOrderInfo.getMoney_paid())));

    }

    private void IView() {
        cashier_desk_blance_lay = (LinearLayout) findViewById(R.id.cashier_desk_blance_lay);

        cashier_desks = (RelativeLayout) findViewById(R.id.cashier_desks);
        cashier_desks.setOnClickListener(this);
        tv_cashier_desk_order_id = (TextView) findViewById(R.id.tv_cashier_desk_order_id);
        tv_cashier_desk_create_time = (TextView) findViewById(R.id.tv_cashier_desk_create_time);
        tv_cashier_desk_odermony = (TextView) findViewById(R.id.tv_cashier_desk_odermony);
        tv_cashier_desk_now_account_balance = (TextView) findViewById(R.id.tv_cashier_desk_now_account_balance);
        iv_cashier_desk_select = (ImageView) findViewById(R.id.iv_cashier_desk_select);
        ll_cashier_desk_edit_psw = (LinearLayout) findViewById(R.id.ll_cashier_desk_edit_psw);
        et_cashier_desk_account_psw = (EditText) findViewById(R.id.et_cashier_desk_account_psw);
        tv_cashier_desk_forget_psw = (TextView) findViewById(R.id.tv_cashier_desk_forget_psw);
        weixin_pay = findViewById(R.id.weixin_pay);
        alipay_pay = findViewById(R.id.alipay_pay);
        yinlian_pay = findViewById(R.id.yinlian_pay);
        views = new ArrayList<View>();
        views.add(weixin_pay);
        views.add(alipay_pay);
        views.add(yinlian_pay);
        ClickViewPay(1);
        ll_cashier_desk_edit_psw.setOnClickListener(this);
        tv_cashier_desk_need_pay = (TextView) findViewById(R.id.tv_cashier_desk_need_pay);
        tv_cashier_desk_go_pay = (TextView) findViewById(R.id.tv_cashier_desk_go_pay);
        SetViewContent(weixin_pay, getString(R.string.cashier_desk_weixin_pay), R.drawable.ic_pay_wx);
        SetViewContent(alipay_pay, getString(R.string.cashier_desk_alipay_pay), R.drawable.ic_pay_zfb);
        SetViewContent(yinlian_pay, getString(R.string.cashier_desk_yinlian_pay), R.drawable.ic_pay_yl);

        // iv_cashier_desk_select.setOnClickListener(this);
        weixin_pay.setOnClickListener(this);
        alipay_pay.setOnClickListener(this);
        yinlian_pay.setOnClickListener(this);
        tv_cashier_desk_go_pay.setOnClickListener(this);
    }

    /**
     * 当点击某个的时候的控制
     */

    private void ClickViewPay(int PayWay) {

        for (int i = 1; i <= views.size(); i++) {

            if (PayWay == i) {
                ((ImageView) (ImageView) (views.get(i - 1))
                        .findViewById(R.id.tv_commentview_left_txt_right_check_check))
                        .setImageResource(R.drawable.quan_select_3);

            } else {
                ((ImageView) (ImageView) (views.get(i - 1))
                        .findViewById(R.id.tv_commentview_left_txt_right_check_check))
                        .setImageResource(R.drawable.quan_select_2);
            }

        }

        // PromptManager.ShowCustomToast(BaseContext, "调用ClickViewPay");
        // ((ImageView) (ImageView) (!IsWxPay ? weixin_pay : alipay_pay)
        // .findViewById(R.id.tv_commentview_left_txt_right_check_check))
        // .setImageResource(R.drawable.quan_select_2);

    }

    // 点击余额支付&&&余额可以全部支付===》不让其他三方支付
    private void ClearnViewPay() {

        for (int i = 1; i <= views.size(); i++) {

            ((ImageView) (ImageView) (views.get(i - 1))
                    .findViewById(R.id.tv_commentview_left_txt_right_check_check))
                    .setImageResource(R.drawable.quan_select_2);

        }

    }

    private void SetViewContent(View view, String content, int ResourceId) {
        TextView tv_commentview_left_txt_right_check_txt = (TextView) view
                .findViewById(R.id.tv_commentview_left_txt_right_check_txt);
        ((ImageView) view.findViewById(R.id.tv_commentview_left_iv)).setImageResource(ResourceId);
        ((ImageView) view.findViewById(R.id.tv_commentview_left_iv)).setVisibility(View.VISIBLE);
        tv_commentview_left_txt_right_check_txt.setText(content);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt(getString(R.string.cashier_desk));
        HindBackIv();
    }

    // 支付宝的支付开始
    private void PayZhiFuBao(final String PayStr) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(BaseActivity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(PayStr, true);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                PayHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    // 支付宝支付的会掉接口
    private Handler PayHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(BaseContext, "支付成功", Toast.LENGTH_SHORT)
                                .show();
                        // 支付宝支付成功后需要进行跳转到
                        // PromptManager.SkipActivity(BaseActivity, new Intent(
                        // BaseActivity, ACenterOder.class));

                        EventBus.getDefault().post(
                                new BMessage(BMessage.Tage_Kill_Self));
                        // 通知刷新订单数据
                        EventBus.getDefault().post(
                                new BMessage(BMessage.Tage_To_Pay_Updata));
//					BaseActivity.finish();

                        PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, ACenterMyOrder.class));
                        BaseActivity.finish();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(BaseContext, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(BaseContext, "支付失败", Toast.LENGTH_SHORT)
                                    .show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 微信支付
     */
    private void WXPay(BPay mBPay) {
        PayReq request = new PayReq();

        request.appId = mBPay.getAppid();

        request.partnerId = mBPay.getPartnerid();

        request.prepayId = mBPay.getPrepayid();

        request.packageValue = "Sign=WXPay";

        request.nonceStr = mBPay.getNoncestr();
        request.timeStamp = mBPay.getTimestamp();
        request.sign = mBPay.getSign();
        msgApi.sendReq(request);
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        PromptManager.closeTextLoading3();

        String PayStr = Data.getHttpResultStr();
        switch (Data.getHttpResultTage()) {
            case 10:// 微信
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    DataError(Msg, 1);
                    return;
                }
                BPay mBPay = new BPay();
                try {
                    JSONObject obj = new JSONObject(PayStr);
                    mBPay = JSON.parseObject(obj.getString("pay"), BPay.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                    PromptManager.ShowCustomToast(BaseContext, "微信解析错误");
                    return;
                }
                WXPay(mBPay);
                break;
            case 20:// 支付宝
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    DataError(Msg, 1);
                    return;
                }
                BPay mBP = JSON.parseObject(PayStr, BPay.class);
                PayZhiFuBao(mBP.getPay());
                break;
            case 30:// 银联支付
                // 获取银联支付！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
                try {
                    JSONObject mJsonObject = new JSONObject(PayStr);
                    String Url = mJsonObject.getString("pay_url");
                    YiLian(Url);
                } catch (JSONException e) {
                    e.printStackTrace();
                    PromptManager.ShowCustomToast(BaseContext, "获取信息失败请重试");
                }

                break;
            case 40:// 余额支付！！！！！！！！！！
                Toast.makeText(BaseContext, "支付成功", Toast.LENGTH_SHORT).show();
                // 支付宝支付成功后需要进行跳转到
                // PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                // ACenterOder.class));

                EventBus.getDefault().post(new BMessage(BMessage.Tage_Kill_Self));
                // 通知刷新订单数据
                EventBus.getDefault().post(
                        new BMessage(BMessage.Tage_To_Pay_Updata));
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, ACenterMyOrder.class));
                BaseActivity.finish();
                break;
            default:
                break;
        }

    }

    @Override
    protected void DataError(String error, int LoadType) {
        PromptManager.closeTextLoading3();
        PromptManager.ShowCustomToast(BaseContext, error);

    }

    @Override
    protected void NetConnect() {
        NetError.setVisibility(View.GONE);
        tv_cashier_desk_go_pay.setClickable(true);
    }

    @Override
    protected void NetDisConnect() {
        NetError.setVisibility(View.VISIBLE);
        tv_cashier_desk_go_pay.setClickable(false);
    }

    @Override
    protected void SetNetView() {
        SetNetStatuse(NetError);

    }

    @Override
    protected void MyClick(View V) {
        switch (V.getId()) {
            case R.id.cashier_desks:// 点击使用账户余额
                isBlanceCheck = !isBlanceCheck;
                // ll_cashier_desk_edit_psw.setVisibility(isCheck ? View.VISIBLE
                // : View.GONE);
                SetIvSelect(iv_cashier_desk_select, isBlanceCheck);

                break;

            case R.id.weixin_pay:
                Float NeedPay = StrUtils.toFloat(addOrderInfo.getMoney_paid())
                        - StrUtils.toFloat(addOrderInfo.getBlance());
                IsBlanceAllPay = false;
                if (NeedPay <= 0f) {// 只要使用余额就直接全部余额支付 用三方支付时候就要是多少就支付多少要不然我的月会扣除完
                    StrUtils.SetTxt(
                            tv_cashier_desk_need_pay,
                            "￥"
                                    + StrUtils.toFloat(StrUtils
                                    .SetTextForMony(addOrderInfo
                                            .getMoney_paid())));

                    // 取消勾选余额支付！！！！！！！！！
                    isBlanceCheck = false;
                    iv_cashier_desk_select
                            .setImageResource(isBlanceCheck ? R.drawable.quan_select_3
                                    : R.drawable.quan_select_1);
                } else {
                    if (isBlanceCheck) {// 余额是否点击
                        StrUtils.SetTxt(tv_cashier_desk_need_pay,
                                "￥" + StrUtils.SetTextForMony(NeedPay + ""));
                    } else {
                        StrUtils.SetTxt(
                                tv_cashier_desk_need_pay,
                                "￥"
                                        + StrUtils.SetTextForMony(addOrderInfo
                                        .getMoney_paid() + ""));
                    }

                }

                IsPayWay = 1;
                ClickViewPay(IsPayWay);
                break;

            case R.id.alipay_pay:
                Float NeedPay1 = StrUtils.toFloat(addOrderInfo.getMoney_paid())
                        - StrUtils.toFloat(addOrderInfo.getBlance());
                IsBlanceAllPay = false;
                if (NeedPay1 <= 0f) {// 只要使用余额就直接全部余额支付 用三方支付时候就要是多少就支付多少要不然我的月会扣除完
                    StrUtils.SetTxt(
                            tv_cashier_desk_need_pay,
                            "￥"
                                    + StrUtils.toFloat(StrUtils
                                    .SetTextForMony(addOrderInfo
                                            .getMoney_paid())));

                    // 取消勾选余额支付！！！！！！！！！
                    isBlanceCheck = false;
                    iv_cashier_desk_select
                            .setImageResource(isBlanceCheck ? R.drawable.quan_select_3
                                    : R.drawable.quan_select_1);
                } else {
                    // StrUtils.SetTxt(tv_cashier_desk_need_pay,
                    // "￥" + StrUtils.SetTextForMony(NeedPay1 + ""));
                    if (isBlanceCheck) {// 余额是否点击
                        StrUtils.SetTxt(tv_cashier_desk_need_pay,
                                "￥" + StrUtils.SetTextForMony(NeedPay1 + ""));
                    } else {
                        StrUtils.SetTxt(
                                tv_cashier_desk_need_pay,
                                "￥"
                                        + StrUtils.SetTextForMony(addOrderInfo
                                        .getMoney_paid() + ""));
                    }

                }

                IsPayWay = 2;
                ClickViewPay(IsPayWay);
                break;

            case R.id.yinlian_pay:
                Float NeedPay2 = StrUtils.toFloat(addOrderInfo.getMoney_paid())
                        - StrUtils.toFloat(addOrderInfo.getBlance());
                IsBlanceAllPay = false;
                if (NeedPay2 <= 0f) {// 只要使用余额就直接全部余额支付 用三方支付时候就要是多少就支付多少要不然我的月会扣除完
                    StrUtils.SetTxt(
                            tv_cashier_desk_need_pay,
                            "￥"
                                    + StrUtils.toFloat(StrUtils
                                    .SetTextForMony(addOrderInfo
                                            .getMoney_paid())));

                    // 取消勾选余额支付！！！！！！！！！
                    isBlanceCheck = false;
                    iv_cashier_desk_select
                            .setImageResource(isBlanceCheck ? R.drawable.quan_select_3
                                    : R.drawable.quan_select_1);
                } else {
                    // StrUtils.SetTxt(tv_cashier_desk_need_pay,
                    // "￥" + StrUtils.SetTextForMony(NeedPay2 + ""));
                    if (isBlanceCheck) {// 余额是否点击
                        StrUtils.SetTxt(tv_cashier_desk_need_pay,
                                "￥" + StrUtils.SetTextForMony(NeedPay2 + ""));
                    } else {
                        StrUtils.SetTxt(
                                tv_cashier_desk_need_pay,
                                "￥"
                                        + StrUtils.SetTextForMony(addOrderInfo
                                        .getMoney_paid() + ""));
                    }

                }

                IsPayWay = 3;
                ClickViewPay(IsPayWay);
                break;

            case R.id.tv_cashier_desk_go_pay:

                // 需要判断 余额是否大于支付金额！！！！！！！！！！！！！、、
                // 如果大于支付金额就直接支付 如果小于就选择支付！！！！！！！！！！！！！！！
                if (IsBlanceAllPay) {// 使用余额支付
                    // AllYuEPay();

                    boolean isLogin_RenZheng_Set = Spuit
                            .IsLogin_RenZheng_Set(BaseContext);
                    if (isLogin_RenZheng_Set) {// 使命认证过直接就余额支付
                        AllYuEPay();
                    } else {// 没有实名认证就让实名认证
                        ShowRealAuthDialog();
                    }

                    return;
                }
                if (IsPayWay == 1) {// 如果是微信支付
                    if (!Constants.isWeixinAvilible(BaseContext)) {// 如果没有安装微信
                        PromptManager.ShowCustomToast(BaseContext, "请安装微信客户端");
                        return;
                    }
                }
                if (isBlanceCheck) {// 使用余额
                    boolean isLogin_RenZheng_Set = Spuit
                            .IsLogin_RenZheng_Set(BaseContext);
                    if (isLogin_RenZheng_Set) {// 使命认证过直接就余额支付
                        BuFenPay();
                    } else {// 没有实名认证就让实名认证
                        ShowRealAuthDialog();
                    }

                    // BuFenPay();
                } else {// 未使用余额
                    PromptManager.showtextLoading3(BaseContext, "正在检测安全支付环境");
                    PayInfConnect(addOrderInfo.getOrder_sn(), IsPayWay,
                            isBlanceCheck, null);
                }

                break;

            default:
                break;
        }
    }

    /**
     * 设置被勾选状态的方法
     */
    private void SetIvSelect(ImageView vv, boolean IsSelect) {
        vv.setImageResource(IsSelect ? R.drawable.quan_select_3
                : R.drawable.quan_select_1);
        // isCheck = !IsSelect;
        if (isBlanceCheck) {// 使用账户余额
            Float NeedPay = StrUtils.toFloat(addOrderInfo.getMoney_paid())
                    - StrUtils.toFloat(addOrderInfo.getBlance());
            StrUtils.SetTxt(tv_cashier_desk_need_pay,
                    "￥" + StrUtils.SetTextForMony(String.valueOf(NeedPay)));//

//			StrUtils.SetTxt(tv_cashier_desk_need_pay,
//					"￥" + StrUtils.toFloat(addOrderInfo.getMoney_paid()));//

            // if(StrUtils.toFloat(String.valueOf(NeedPay)))ss

            IsBlanceAllPay = false;
            if (NeedPay <= 0f) {
                StrUtils.SetTxt(
                        tv_cashier_desk_need_pay,
                        "￥"
                                + StrUtils.SetTextForMony(addOrderInfo
                                .getMoney_paid()));
                IsBlanceAllPay = true;
                IsPayWay = 4;
                ClearnViewPay();
            }
        } else {// 不使用账户余额
            IsBlanceAllPay = false;
            StrUtils.SetTxt(tv_cashier_desk_need_pay,
                    "￥" + StrUtils.SetTextForMony(addOrderInfo.getMoney_paid()));//
        }
    }

    @Override
    protected void InItBundle(Bundle bundle) {
    }

    @Override
    protected void SaveBundle(Bundle bundle) {
    }

    private void PayInfConnect(String OderStr, int payway, Boolean is_balace,
                               String YuPassd) {
        switch (payway) {
            case 1:// 微信支付
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("order_sn", OderStr);// 订单编号
                map.put("member_id", mBUser.getId());
                map.put("payment_type", "10");// 支付方式 10微信 20支付宝 30银联
                map.put("trade_type", "APP");// 交易类型JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付
                map.put("is_balace", is_balace ? "Y" : "");// 是否使用余额 Y使用 空 不实用
                map.put("openid", "");
                if (YuPassd != null) {
                    map.put("pay_password", Constants.RSA(YuPassd, BaseActivity));
                }
                FBGetHttpData(map, Constants.Oder_Pay, Method.POST, 10,
                        LOAD_LOADMOREING);
                break;
            case 2:// 支付宝支付
                HashMap<String, String> mapzfb = new HashMap<String, String>();
                mapzfb.put("order_sn", OderStr);// 订单编号
                mapzfb.put("member_id", mBUser.getId());
                mapzfb.put("payment_type", "20");// 支付方式 10微信 20支付宝 30银联
                mapzfb.put("trade_type", "APP");// 交易类型JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付
                mapzfb.put("is_balace", is_balace ? "Y" : "");// 是否使用余额 Y使用 空 不实用
                mapzfb.put("openid", "");
                if (YuPassd != null) {
                    mapzfb.put("pay_password", Constants.RSA(YuPassd, BaseActivity));
                }
                FBGetHttpData(mapzfb, Constants.Oder_Pay, Method.POST, 20,
                        LOAD_LOADMOREING);
                break;
            case 3:// 银联支付
                // ToDo进入扫码界面操作

                HashMap<String, String> mapyl = new HashMap<String, String>();
                mapyl.put("order_sn", OderStr);// 订单编号
                mapyl.put("member_id", mBUser.getId());
                mapyl.put("payment_type", "30");// 支付方式 10微信 20支付宝 30银联
                mapyl.put("trade_type", "JSAPI");// 交易类型JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付
                mapyl.put("is_balace", is_balace ? "Y" : "");// 是否使用余额 Y使用 空 不实用
                mapyl.put("openid", "");
                if (YuPassd != null) {
                    mapyl.put("pay_password", Constants.RSA(YuPassd, BaseActivity));
                }
                FBGetHttpData(mapyl, Constants.Oder_Pay, Method.POST, 30,
                        LOAD_LOADMOREING);

                break;

            default:
                break;
        }

    }

    private void YiLian(String Url) {
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setText(Url);
        PromptManager.ShowCustomToast(BaseContext,
                getResources().getString(R.string.yinlian_pay_payfuzhi));
        ShowCustomDialog("请打开网页" + Url,
                getResources().getString(R.string.cancle), getResources()
                        .getString(R.string.queding), new IDialogResult() {

                    @Override
                    public void RightResult() {
                        PromptManager.SkipActivity(
                                BaseActivity,
                                new Intent(BaseActivity, CaptureActivity.class)
                                        .putExtra("ispay", true).putExtra(
                                        "payoder",
                                        addOrderInfo.getOrder_sn()));
                    }

                    @Override
                    public void LeftResult() {
                    }
                });

        // final CustomDialog dialog = new CustomDialog(BaseContext,
        // R.style.mystyle, R.layout.customdialog, 3, "取消", "确定");
        // dialog.show();
        // dialog.setTitleText("请打开网页http://dev.vt.www.v-town.cn/pay/pay/index扫描安全支付码进行支付");
        // dialog.setConfirmListener(new onConfirmClick() {
        // @Override
        // public void onConfirmCLick(View v) {
        // PromptManager.SkipActivity(
        // BaseActivity,
        // new Intent(BaseActivity, CaptureActivity.class)
        // .putExtra("ispay", true).putExtra("payoder",
        // addOrderInfo.getOrder_sn()));
        // dialog.dismiss();
        //
        // }
        // });
        // dialog.setcancelListener(new oncancelClick() {
        // @Override
        // public void oncancelClick(View v) {
        // dialog.dismiss();
        // }
        // });

    }

    /**
     * 全部使用余额支付
     */
    private void AllYuEPay() {
        final PPassWord pp = new PPassWord(BaseContext, screenWidth, "输入支付密码");
        pp.setOnPassWordListener(new OnPasswordInputFinish() {
            @Override
            public void inputFinish(String getStrPassword) {
                pp.dismiss();
                PromptManager.showtextLoading3(BaseContext, "正在检测安全支付环境");
                HashMap<String, String> mapzfb = new HashMap<String, String>();
                mapzfb.put("order_sn", addOrderInfo.getOrder_sn());// 订单编号
                mapzfb.put("member_id", mBUser.getId());
                mapzfb.put("payment_type", "40");// 支付方式 10微信 20支付宝 30银联
                mapzfb.put("trade_type", "APP");// 交易类型JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付
                mapzfb.put("is_balace", "Y");// 是否使用余额 Y使用 空 不实用
                mapzfb.put("openid", "");
                mapzfb.put("pay_password",
                        Constants.RSA(getStrPassword, BaseActivity));
                FBGetHttpData(mapzfb, Constants.Oder_Pay, Method.POST, 40,
                        LOAD_LOADMOREING);

            }

            @Override
            public void LostPassWord() {
                PromptManager.ShowCustomToast(BaseContext, "忘记密码怎么办");
            }

            @Override
            public void Cancle() {
                pp.dismiss();
            }
        });
        pp.showAtLocation(mView, Gravity.CENTER, 0, 0);
    }

    // 部分使用余额进行支付
    private void BuFenPay() {
        final PPassWord pp = new PPassWord(BaseContext, screenWidth, "输入支付密码");
        pp.setOnPassWordListener(new OnPasswordInputFinish() {
            @Override
            public void inputFinish(String getStrPassword) {
                pp.dismiss();
                PromptManager.showtextLoading3(BaseContext, "正在检测安全支付环境");
                PayInfConnect(addOrderInfo.getOrder_sn(), IsPayWay,
                        isBlanceCheck, getStrPassword);
            }

            @Override
            public void LostPassWord() {
                PromptManager.ShowCustomToast(BaseContext, "忘记密码怎么办");
            }

            @Override
            public void Cancle() {
                pp.dismiss();
            }
        });
        pp.showAtLocation(mView, Gravity.CENTER, 0, 0);
    }

    public void ReciverInf(BMessage message) {
        switch (message.getMessageType()) {
            case BMessage.Tage_Kill_Self2:
//			BaseActivity.finish();
                PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, ACenterMyOrder.class));
                BaseActivity.finish();
                break;

            default:
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
        }
    }

    /**
     * 实名认证对话框
     *
     * @param fetch_type
     * @param datBlComment
     * @param aa
     */
    private void ShowRealAuthDialog() {

        ShowCustomDialog("您还没有进行实名认证", "取消", "去认证", new IDialogResult() {
            @Override
            public void RightResult() {
                int from_where = 10;
                PromptManager.SkipActivity(BaseActivity, new Intent(
                        BaseContext, ARealIdauth.class).putExtra(
                        ARealIdauth.FROM_WHERE_KEY, from_where));

            }

            @Override
            public void LeftResult() {
            }
        });


//		final CustomDialog dialog = new CustomDialog(BaseContext,
//				R.style.mystyle, R.layout.customdialog, 1, "取消", "去认证");
//		dialog.show();
//		dialog.setTitleText("您还没有进行实名认证");
//		dialog.setConfirmListener(new onConfirmClick() {
//			@Override
//			public void onConfirmCLick(View v) {
//				int from_where = 10;
//
//				PromptManager.SkipActivity(BaseActivity, new Intent(
//						BaseContext, ARealIdauth.class).putExtra(
//						ARealIdauth.FROM_WHERE_KEY, from_where));
//
//				dialog.dismiss();
//			}
//		});
//		dialog.setcancelListener(new oncancelClick() {
//			@Override
//			public void oncancelClick(View v) {
//
//				dialog.dismiss();
//			}
//		});
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity, ACenterMyOrder.class));
            BaseActivity.finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
