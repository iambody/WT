package io.vtown.WeiTangApp.ui.comment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-17 下午12:01:57
 * @see   activityforresoult
 */
public class AEditInfBack extends ATitleBase implements TextWatcher,
        OnFocusChangeListener {
    // 输入框
    private EditText comment_infback_ed;
    // 输入的长度控制
    private TextView comment_infback_ed_number;
    // 确定按钮
    private TextView comment_infback_submint;

    public static String Tag_key = "editinfley";
    private int Tag_Edit;

    public final static int Tag_AddGoods_Title = 101;

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


    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_editinf_back);
        IEdBunld();
        IBasevv();
    }

    private void IBasevv() {

        comment_infback_ed = (EditText) findViewById(R.id.comment_infback_ed);
        if (!StrUtils.isEmpty(editContent)) {
            comment_infback_ed.setText(editContent);
        }
        comment_infback_ed_number = (TextView) findViewById(R.id.comment_infback_ed_number);
        comment_infback_ed.clearFocus();
        comment_infback_ed.addTextChangedListener(this);
        comment_infback_ed.setOnFocusChangeListener(this);
        comment_infback_submint = (TextView) findViewById(R.id.comment_infback_submint);
        comment_infback_submint.setOnClickListener(this);

        comment_infback_ed_number.setText(String.format("0 / %s", MaxNumber
                + ""));
    }

    private void IEdBunld() {

        if (null == getIntent().getExtras()
                || !getIntent().getExtras().containsKey(Tag_key))
            BaseActivity.finish();
        Tag_Edit = getIntent().getIntExtra(Tag_key, 0);
        if (Tag_Edit == 0)
            BaseActivity.finish();

        switch (Tag_Edit) {
            case Tag_AddGoods_Title:// 添加宝贝时候添加宝贝的title
                MaxNumber = 32;
                SetTitleTxt("商品标题");

                if (getIntent().getExtras().containsKey("title"))
                    editContent = getIntent().getStringExtra("title");
                break;

            default:
                break;
        }

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
            case R.id.comment_infback_submint://
                if (StrUtils.isEmpty(comment_infback_ed.getText().toString().trim())) {
                    PromptManager.ShowCustomToast(BaseContext, "请填写商品标题");
                    return;
                }
                BMessage MBMessagess=new BMessage(BMessage.Tage_AddGood_EditTitle);
                MBMessagess.setAddGood_GoodTitle(comment_infback_ed.getText().toString().trim());
                EventBus.getDefault().post(MBMessagess);
                finish();
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
    public void onFocusChange(View arg0, boolean arg1) {
        if (arg1) {
            comment_infback_ed.setHint("");

        } else {
            comment_infback_ed.setHint(editContent);
        }
    }

    @Override
    public void afterTextChanged(Editable arg0) {
        ed_start = comment_infback_ed.getSelectionStart();
        ed_end = comment_infback_ed.getSelectionEnd();
        comment_infback_ed.removeTextChangedListener(this);
        while (calculateLength(arg0.toString().trim()) > MaxNumber) {
            arg0.delete(ed_start - 1, ed_end);
            ed_start--;
            ed_end--;
        }
        comment_infback_ed.setText(arg0);
        comment_infback_ed.setSelection(ed_start);
        comment_infback_ed.addTextChangedListener(this);
        setLeftCount();
    }

    private void setLeftCount() {
        comment_infback_ed_number.setText(getInputCount() + "/"
                + (MaxNumber - getInputCount()));
    }

    private long getInputCount() {

        return calculateLength(comment_infback_ed.getText().toString().trim());
    }

    private long calculateLength(CharSequence c) {
        double len = 0;
        for (int i = 0; i < c.length(); i++) {
            int temp = (int) c.charAt(i);
            if (temp > 0 && temp < 127) {
                len += 0.5;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                  int arg3) {
    }

    @Override
    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
    }

}
