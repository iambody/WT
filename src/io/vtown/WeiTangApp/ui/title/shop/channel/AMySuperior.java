package io.vtown.WeiTangApp.ui.title.shop.channel;

import android.os.Bundle;
import android.view.View;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-16 上午11:16:21
 *  我的上级页面
 */
public class AMySuperior extends ATitleBase {

	@Override
	protected void InItBaseView() {
		
		setContentView(R.layout.activity_my_supertior);
		
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.my_superior));
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
	}

	@Override
	protected void DataError(String error,int LoadTyp) {
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
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

}
