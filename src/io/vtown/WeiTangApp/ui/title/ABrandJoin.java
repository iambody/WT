package io.vtown.WeiTangApp.ui.title;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-31 下午1:46:30
 *  品牌入驻页面
 */
public class ABrandJoin extends ATitleBase {

	/**
	 * 品牌入驻规则
	 */
	private TextView tv_brand_join_rule;
	/**
	 * 品牌介绍输入框
	 */
	private EditText et_brand_introduce;
	/**
	 * 提交按钮
	 */
	private Button btn_brand_join_confirm;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_brand_join);
		IView();
	}

	private void IView() {
		tv_brand_join_rule = (TextView) findViewById(R.id.tv_brand_join_rule);
		et_brand_introduce = (EditText) findViewById(R.id.et_brand_introduce);
		btn_brand_join_confirm = (Button) findViewById(R.id.btn_brand_join_confirm);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.brand_join));
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
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

}
