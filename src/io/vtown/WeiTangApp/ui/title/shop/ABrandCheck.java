package io.vtown.WeiTangApp.ui.title.shop;

import com.easemob.chat.EMMessage;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.AWeb;
import io.vtown.WeiTangApp.ui.comment.im.AChatInf;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-8-8 下午4:41:39
 * 
 */
public class ABrandCheck extends ATitleBase {

	private View brandcheck_phone, brandcheck_im, brandcheck_web;
//	private TextView brandcheck_inf_txt;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_brand_checkin);
		brandcheck_phone = findViewById(R.id.brandcheck_phone);
		brandcheck_im = findViewById(R.id.brandcheck_im);
		brandcheck_web = findViewById(R.id.brandcheck_web);

		SetItemContent(brandcheck_phone, "客服电话");
		SetItemContent(brandcheck_im, "微糖客服");
		SetItemContent(brandcheck_web, "微糖官网");
//		brandcheck_inf_txt=(TextView) findViewById(R.id.brandcheck_inf_txt);
		 
//		brandcheck_inf_txt.setText(getResources().getString(R.string.brand_ruzhu));
	}

	private void SetItemContent(View VV, String Title) {
		((TextView) VV.findViewById(R.id.comment_txtarrow_title))
				.setText(Title);
		VV.setOnClickListener(this);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt("品牌入驻");
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
		
		case R.id.brandcheck_phone:
			ContactService();
			break;
		case R.id.brandcheck_im: 
			Intent intent = new Intent(BaseActivity, AChatInf.class);
			// holder.name.setText(ReciverName);
			intent.putExtra("tagname", Constants.WtHelper);
			String ReciverName = Constants.WtHelper;

			intent.putExtra("title","微糖客服" );
			intent.putExtra("iv","" );
			intent.putExtra("ishepler", true);
			startActivity(intent);
			break;
		case R.id.brandcheck_web:
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					AWeb.class).putExtra(AWeb.Key_Bean, new BComment(
					getResources().getString(R.string.wt_weburl),
					getResources().getString(R.string.app_name))));
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
