package io.vtown.WeiTangApp.ui.title.center.wallet;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BLSelectBank;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-6-3 下午5:46:32
 *          修改/删除银行卡
 */
public class ABankCardOperation extends ATitleBase {


    /**
     * 银行图标
     */
    private ImageView iv_modify_bank_icon;
    /**
     * 银行名称
     */
    private TextView tv_modify_bank_name;
    /**
     * 银行账号
     */
    private TextView tv_modify_bank_card_id;
    /**
     * 用户名
     */
    private TextView tv_modify_bank_usr_name;

    /**
     * 账号title
     */
    private TextView tv_modify_bank_card_id_lable;
    /**
     * 账号输入框
     */
    private EditText et_modify_bank_card_numb;
    /**
     * 提交
     */
    private TextView tv_mofify_submit;
    /**
     * 转入的数据
     */
    private BLComment mBlcomment;
    /**
     * 选择银行
     */
    private LinearLayout ll_select_bank;
    /**
     * 选择的银行图标
     */
    private ImageView iv_modify_bank_select_icon;
    /**
     * 选择的银行名称
     */
    private TextView tv_modify_bank_card_select_name;
    /**
     * 选择的银行信息
     */
    private BLSelectBank bank_info;
    private boolean isFinish;
    /**
     * 用户相关信息
     */
    private BUser user_Get;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_center_wallet_bankcard_manager_modify_delete);
        user_Get = Spuit.User_Get(getApplicationContext());
        IView();
        IData();

    }

    /**
     * 初始化控件
     */
    private void IView() {
        iv_modify_bank_icon = (ImageView) findViewById(R.id.iv_modify_bank_icon);
        tv_modify_bank_name = (TextView) findViewById(R.id.tv_modify_bank_name);
        tv_modify_bank_card_id = (TextView) findViewById(R.id.tv_modify_bank_card_id);
        tv_modify_bank_usr_name = (TextView) findViewById(R.id.tv_modify_bank_usr_name);
        ll_select_bank = (LinearLayout) findViewById(R.id.ll_select_bank);
        iv_modify_bank_select_icon = (ImageView) findViewById(R.id.iv_modify_bank_select_icon);
        tv_modify_bank_card_select_name = (TextView) findViewById(R.id.tv_modify_bank_card_select_name);
        tv_modify_bank_card_select_name.setText("请选择开户银行");
        iv_modify_bank_select_icon.setVisibility(View.GONE);

        tv_modify_bank_card_id_lable = (TextView) findViewById(R.id.tv_modify_bank_card_id_lable);
        et_modify_bank_card_numb = (EditText) findViewById(R.id.et_modify_bank_card_numb);
        tv_mofify_submit = (TextView) findViewById(R.id.tv_mofify_submit);
        ll_select_bank.setOnClickListener(this);
        tv_mofify_submit.setOnClickListener(this);

    }

    /**
     * 初始化数据
     */
    private void IData() {
        Intent intent = getIntent();
        mBlcomment = (BLComment) intent.getSerializableExtra("datas");
        isFinish = intent.getBooleanExtra("isFinish", false);
        ImageLoaderUtil.Load2(mBlcomment.getIcon(), iv_modify_bank_icon, R.drawable.error_iv2);
        StrUtils.SetTxt(tv_modify_bank_name, mBlcomment.getBank_name());
        StrUtils.SetTxt(tv_modify_bank_card_id, StrUtils.getCardFormatForUser(mBlcomment.getCard_number()));

        String name = user_Get.getName();

        String format_name = String.format("开户姓名：%1$s", name);
        StrUtils.SetTxt(tv_modify_bank_usr_name, format_name);

    }

    private void ModifyBank(String member_id, String card_number, String bank_name, String name, String bank_id, String id) {
        SetTitleHttpDataLisenter(this);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", member_id);
        map.put("card_number", card_number);
        map.put("bank_name", bank_name);
        map.put("name", name);
        map.put("bank_id", bank_id);
        map.put("id", id);
        FBGetHttpData(map, Constants.Modify_Bank_Card, Method.PUT, 0, LOAD_INITIALIZE);

    }

    @Override
    protected void InitTile() {
        SetTitleTxt("修改银行卡");
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        tv_mofify_submit.setEnabled(true);
        if (200 == Code) {
            PromptManager.ShowMyToast(BaseContext, "银行卡修改成功");
            Intent intent = new Intent(BaseContext, ABankCardManager.class);
            intent.putExtra("isFinish", false);
            EventBus.getDefault().post(new BMessage(BMessage.Tage_Updata_BankCard_List));
            startActivity(intent);

            this.finish();
        }
    }

    @Override
    protected void DataError(String error, int LoadType) {
        tv_mofify_submit.setEnabled(true);
        PromptManager.ShowMyToast(BaseContext, error);

    }

    @Override
    protected void NetConnect() {
        NetError.setVisibility(View.GONE);
        tv_mofify_submit.setEnabled(true);
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
            case R.id.ll_select_bank:

                Intent intent = new Intent(BaseContext, ASelectBank.class);

                PromptManager.SkipResultActivity(BaseActivity, intent, 100);

                break;

            case R.id.tv_mofify_submit:
                tv_mofify_submit.setEnabled(false);
                if (CheckNet(BaseContext)) return;
                getBankCardInfo();
                break;

            default:
                break;
        }
    }

    /**
     * 获取要输入的信息内容
     */
    private void getBankCardInfo() {

        String name = user_Get.getName();
        String bank_name = tv_modify_bank_card_select_name.getText().toString().trim();
        if ("请选择开户银行".equals(bank_name)) {
            tv_mofify_submit.setEnabled(true);
            PromptManager.ShowMyToast(BaseContext, "选择您要修改的银行");
            return;
        }
        String card_number = et_modify_bank_card_numb.getText().toString().trim();
        if (!StrUtils.checkBankCard(BaseContext, card_number)) {
            tv_mofify_submit.setEnabled(true);
            return;
        }

        String bank_id = mBlcomment.getBank_id();
        String id = mBlcomment.getId();
/**
 * 如果已经切换新的银行卡 需要传入新银行卡的bankid和bankname
 */
        if (bank_info != null) {
            bank_id = bank_info.getBank_id();
            bank_name = bank_info.getBank_name();

        }
        ModifyBank(user_Get.getId(), card_number, bank_name, name, bank_id, id);
    }

    @Override
    protected void InItBundle(Bundle bundle) {
    }

    @Override
    protected void SaveBundle(Bundle bundle) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (100 == requestCode && RESULT_OK == resultCode) {
            bank_info = (BLSelectBank) data.getSerializableExtra("bank_info");
            if (bank_info != null) {
                iv_modify_bank_select_icon.setVisibility(View.VISIBLE);
                ImageLoaderUtil.Load2(bank_info.getIcon(), iv_modify_bank_select_icon, R.drawable.error_iv2);

                StrUtils.SetTxt(tv_modify_bank_card_select_name, bank_info.getBank_name());
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
