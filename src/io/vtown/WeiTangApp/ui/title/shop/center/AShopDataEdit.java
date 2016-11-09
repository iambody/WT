package io.vtown.WeiTangApp.ui.title.shop.center;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-25 上午11:05:25 店铺资料输入页面
 */
public class AShopDataEdit extends ATitleBase implements TextWatcher, OnFocusChangeListener {

    private EditText et_content;
    private String content;

    private static final int RESULT_CODE = 111;
    private int type;
    private String seller_id;

    private TextView tv_modify_desc;
    private TextView tv_content_count;

    private int MaxNumber = 0;
    /**
     * 输入的起始位置
     */
    private int ed_start;
    /**
     * 结束位置
     */
    private int ed_end;
    // 记录下数据 供返回
    private String editContent;
    private TextView tv_name_or_desc;

    private void IData() {
        Intent intent = getIntent();
        content = intent.getStringExtra("content");
        if (content == null || "您还未填写个性签名".equals(content))
            content = "";
        type = intent.getIntExtra("type", 0);
        seller_id = intent.getStringExtra("seller_id");

    }

    private void IHData(String content) {
        SetTitleHttpDataLisenter(this);
        PromptManager.showLoading(BaseContext);
        HashMap<String, String> map = new HashMap<String, String>();
        String host = "";
        switch (type) {
            case 1:// 修改名称
                map.put("seller_id", seller_id);
                map.put("nickname", content);
                host = Constants.MODIFI_SHOP_NICKNAME;
                break;

            case 2:// 修改描述
                map.put("seller_id", seller_id);
                map.put("intro", content);
                host = Constants.MODIFI_SHOP_INTRODUCE;
                break;
        }

        FBGetHttpData(map, host, Method.PUT, type, LOAD_INITIALIZE);
    }

    private void IView() {
        et_content = (EditText) findViewById(R.id.et_content);
        Button btn_ok = (Button) findViewById(R.id.btn_ok);

        tv_modify_desc = (TextView) findViewById(R.id.tv_modify_desc);

        tv_content_count = (TextView) findViewById(R.id.tv_content_count);

        tv_name_or_desc = (TextView) findViewById(R.id.tv_name_or_desc);

        if (StrUtils.isEmpty(content)) {
            //et_content.setHint("请添加描述");
        } else {
            et_content.setText(content);
            et_content.setSelection(content.length());
        }
        switch (type) {
            case 1:
                tv_name_or_desc.setText("昵称");
                tv_modify_desc.setText("好昵称可以让你更加瞩目");
                tv_content_count.setVisibility(View.GONE);
                break;

            case 2:
                tv_name_or_desc.setVisibility(View.GONE);
                tv_modify_desc.setText("填写个性签名，赋予你与众不同的气质");
                MaxNumber = 20;
                et_content.clearFocus();
                et_content.addTextChangedListener(this);
                et_content.setOnFocusChangeListener(this);
                tv_content_count.setText(String.format("%1$s/%2$s", content.length() + "", MaxNumber
                        + ""));
                break;

            default:
                break;
        }
        btn_ok.setOnClickListener(this);

    }

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_shop_center_shop_data_edit);
        IData();
        IView();
    }

    @Override
    protected void InitTile() {
        String title = "";
        switch (type) {
            case 1:

                title = "昵称";


                break;

            case 2:

                title = "个性签名";


                break;

            default:
                break;
        }
        SetTitleTxt(title);
        SetRightText("完成");
        right_txt.setOnClickListener(this);
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {
        if (Code == 200) {
            String content = et_content.getText().toString().trim();
            EventBus.getDefault().post(
                    new BMessage(
                            BMessage.Fragment_Home_Bind));
            switch (type) {
                case 1:// 保存名称到SP
                    Spuit.Save_Shop_Nickname(getApplicationContext(), content);
                    // 发送事件
                    EventBus.getDefault().post(
                            new BMessage(BMessage.Tage_Shop_data_shopname_change));
                    break;

                case 2:// 保存描述到SP
                    Spuit.Save_Shop_Introduce(getApplicationContext(), content);
                    // 发送事件
                    EventBus.getDefault().post(
                            new BMessage(BMessage.Tage_Shop_data_desc_change));
                    break;
            }

            Intent intent = new Intent();
            intent.putExtra("content", content);
            setResult(RESULT_CODE, intent);

            PromptManager.ShowMyToast(BaseContext, "修改成功");

            finish();

        } else {
            DataError("修改失败", 1);
        }
    }

    @Override
    protected void DataError(String error, int LoadTyp) {
        PromptManager.ShowMyToast(BaseContext, error);
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
            case R.id.btn_ok:
            case R.id.right_txt:
                String content_txt = et_content.getText().toString().trim();
                if (StrUtils.isEmpty(content_txt)) {

                    PromptManager.ShowMyToast(BaseContext, "输入不能为空");
                    return;

                }

                if (content.equals(content_txt)) {
                    this.finish();
                } else {
                    IHData(content_txt);
                }

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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            et_content.setHint("");

        } else {
            et_content.setHint("");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        ed_start = et_content.getSelectionStart();
        ed_end = et_content.getSelectionEnd();
        et_content.removeTextChangedListener(this);
        while (calculateLength(s.toString().trim()) > MaxNumber) {
            s.delete(ed_start - 1, ed_end);
            ed_start--;
            ed_end--;
        }
        et_content.setText(s);
        et_content.setSelection(ed_start);
        et_content.addTextChangedListener(this);
        setLeftCount();
    }

    private void setLeftCount() {
        tv_content_count.setText(getInputCount() + "/"
                + (MaxNumber - getInputCount()));
    }

    private long getInputCount() {

        return calculateLength(et_content.getText().toString().trim());
    }

    private long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
//			int temp = (int) c.charAt(i);
//			if (temp > 0 && temp < 127) {
//				len += 1;
//			} else {
//				len++;
//			}
            len++;
        }
        return Math.round(len);
    }

}
