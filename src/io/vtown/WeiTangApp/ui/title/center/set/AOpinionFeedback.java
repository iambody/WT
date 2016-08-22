package io.vtown.WeiTangApp.ui.title.center.set;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.ui.ATitleBase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-6-9 上午12:27:53 意见反馈页面
 */
public class AOpinionFeedback extends ATitleBase implements TextWatcher, OnFocusChangeListener {

	/**
	 * 反馈意见输入框
	 */
	private EditText et_enter_your_valuable_opinion;
	/**
	 * 联系方式输入框
	 */
	private EditText et_enter_your_contact;
	/**
	 * 字数限制
	 */
	private TextView tv_opinion_words_count;
	/**
	 * 意见提交按钮
	 */
	private Button btn_submit_opinion;
	/**
	 * 常见问题1
	 */
	private View common_question1;
	/**
	 * 常见问题2
	 */
	private View common_question2;
	/**
	 * 常见问题2
	 */
	private View common_question3;
	/**
	 * 常见问题4
	 */
	private View common_question4;
	
	/**
	 * 输入的起始位置
	 */
	private int et_start;
	/**
	 * 结束位置
	 */
	private int et_end;

	/**
	 * 商品标题输入输入的内容
	 */
	private String editContent;
	/**
	 * 商品标题输入框最大值
	 */
	private static final int MAX_NUMBER = 100;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_center_set_personal_data_about_wt_opinion_feedback);
		IView();
	}

	private void IView() {
		et_enter_your_valuable_opinion = (EditText) findViewById(R.id.et_enter_your_valuable_opinion);
		et_enter_your_contact = (EditText) findViewById(R.id.et_enter_your_contact);
		tv_opinion_words_count = (TextView) findViewById(R.id.tv_opinion_words_count);
		btn_submit_opinion = (Button) findViewById(R.id.btn_submit_opinion);
		et_enter_your_valuable_opinion.clearFocus();
		et_enter_your_valuable_opinion.addTextChangedListener(this);
		et_enter_your_valuable_opinion.setOnFocusChangeListener(this);
		common_question1 = findViewById(R.id.common_question1);
		common_question2 = findViewById(R.id.common_question2);
		common_question3 = findViewById(R.id.common_question3);
		common_question4 = findViewById(R.id.common_question4);
		SetItemContent(common_question1, R.string.common_question1);
		SetItemContent(common_question2, R.string.common_question2);
		SetItemContent(common_question3, R.string.common_question3);
		SetItemContent(common_question4, R.string.common_question4);
	}

	private void SetItemContent(View VV, int ResourceTitle) {
		((TextView) VV.findViewById(R.id.comment_txtarrow_title))
				.setText(getResources().getString(ResourceTitle));

		VV.setOnClickListener(this);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getString(R.string.opinion_feedback));
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
		case R.string.common_question1:

			break;

		case R.string.common_question2:

			break;

		case R.string.common_question3:

			break;

		case R.string.common_question4:

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
	public void afterTextChanged(Editable arg0) {

		et_start = et_enter_your_valuable_opinion.getSelectionStart();
		et_end = et_enter_your_valuable_opinion.getSelectionEnd();
		et_enter_your_valuable_opinion.removeTextChangedListener(this);
		while (calculateLength(arg0.toString()) > MAX_NUMBER) {
			arg0.delete(et_start - 1, et_end);
			et_start--;
			et_end--;
		}
		et_enter_your_valuable_opinion.setText(arg0);
		et_enter_your_valuable_opinion.setSelection(et_start);
		et_enter_your_valuable_opinion.addTextChangedListener(this);
		setLeftCount();
	}

	private void setLeftCount() {
		tv_opinion_words_count.setText(getInputCount() + "/"
				+ (MAX_NUMBER - getInputCount()));
	}

	private long getInputCount() {

		return calculateLength(et_enter_your_valuable_opinion.getText().toString());
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

	@Override
	public void onFocusChange(View arg0, boolean arg1) {
		if (arg1) {
			et_enter_your_valuable_opinion.setHint("");

		} else {
			et_enter_your_valuable_opinion.setHint(editContent);
		}
	}

}
