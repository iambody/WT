package io.vtown.WeiTangApp.ui.title;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-14 下午5:52:30
 * @see 品牌店铺跳转进来的 文字介绍
 */
public class ABrandShopInf extends ATitleBase {

	public static String Tage_Key = "strkey";

	private TextView brandshopinf_text;

	private String Reslource = "";

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_brandshop_inf);
		IBundl();
		IBaseV();
	}

	private void IBundl() {
		if (getIntent().getExtras() == null
				|| !getIntent().getExtras().containsKey(Tage_Key))
			BaseActivity.finish();
		Reslource = getIntent().getStringExtra(Tage_Key);

	}

	private void IBaseV() {
		brandshopinf_text = (TextView) findViewById(R.id.brandshopinf_text);
		brandshopinf_text.setText(StrUtils.isEmpty(Reslource) ? "暂无品牌介绍,敬请期待"
				: Reslource);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.brand_inf));
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
	}

	@Override
	protected void DataError(String error, int LoadType) {
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
