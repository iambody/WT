package io.vtown.WeiTangApp.ui.title.mynew;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-29 上午10:20:52
 * 
 */
public class ANewInf extends ATitleBase {

	public final static String Tage_key = "newbewan";
	private BNew MyNew;

	private TextView newinf_title, newinf_content;

	
	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_newinf);
		IBund();
		IBView();
	}

	private void IBView() {
		newinf_title = (TextView) findViewById(R.id.newinf_title);
		newinf_content = (TextView) findViewById(R.id.newinf_content);
		StrUtils.SetTxt(newinf_title, MyNew.getTitle());
		StrUtils.SetTxt(newinf_content, MyNew.getContent());
	}

	private void IBund() {
		if (getIntent().getExtras() != null
				&& getIntent().getExtras().containsKey(Tage_key)) {
			MyNew = (BNew) getIntent().getExtras().getSerializable(Tage_key);
		} else {
			BaseActivity.finish();
			PromptManager.ShowCustomToast(BaseContext, "消息读取失败");
		}

	}

	@Override
	protected void InitTile() {
		SetTitleTxt("消息详情");
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
