package io.vtown.WeiTangApp.ui.title.shop.goodmanger;

import de.greenrobot.event.EventBus;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-8-19 上午10:18:08
 * 
 */
public class ABrandNumberEdit extends ATitleBase {
	private int Postion;
	private Button brand_number_edit_bt;
	private EditText brand_number_edit_ed;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_brandnumber_edit);
		Postion = getIntent().getIntExtra("edpostion", 0);
		brand_number_edit_ed = (EditText) findViewById(R.id.brand_number_edit_ed);
		brand_number_edit_bt = (Button) findViewById(R.id.brand_number_edit_bt);
		brand_number_edit_bt.setOnClickListener(this);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt("修改数量");
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
		case R.id.brand_number_edit_bt:

			if (StrUtils.EditTextIsEmPty(brand_number_edit_ed)) {
				PromptManager.ShowCustomToast(BaseContext, "请输入数量");
				return;
			}
			BMessage message = new BMessage(BMessage.Good_Manger_Edit);
			message.setGoodMangeAlterPostion(Postion);
			message.setGoodMangeAlterNUmbe(brand_number_edit_ed.getText()
					.toString().trim());
			EventBus.getDefault().post(message);
			BaseActivity.finish();
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
