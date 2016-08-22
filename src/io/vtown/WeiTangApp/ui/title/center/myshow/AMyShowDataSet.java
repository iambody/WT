package io.vtown.WeiTangApp.ui.title.center.myshow;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.view.custom.switchButtonView.EaseSwitchButton;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-6-7 上午10:41:50 我的show进来的资料设置页面
 */
public class AMyShowDataSet extends ATitleBase implements
		OnCheckedChangeListener {

	/**
	 * 我看他的show
	 */
	private View commentview_txt_switchbuttom_i_look_he_show;
	/**
	 * 他看我的show
	 */
	private View commentview_txt_switchbuttom_he_look_i_show;
	/**
	 * 获取并传递beand的key
	 */
	public static String Bean_Key = "beankey";

	private EaseSwitchButton switch_select_Up;
	private EaseSwitchButton switch_select_Down;

	/**
	 * show列表传递进来的数据源
	 * 
	 */
	private BLComment ItemData = null;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_my_show_data_set);
//		IBUnde();
		IBase(); 
	}

	private void IBase() {
		commentview_txt_switchbuttom_i_look_he_show = findViewById(R.id.commentview_txt_switchbuttom_i_look_he_show);
		commentview_txt_switchbuttom_he_look_i_show = findViewById(R.id.commentview_txt_switchbuttom_he_look_i_show);
		setSwitchbuttomContent(commentview_txt_switchbuttom_he_look_i_show,
				"不让他看我的show");
		setSwitchbuttomContent(commentview_txt_switchbuttom_i_look_he_show,
				"不看他的show");
		 switch_select_Up.setChecked(true);//
	}

	private void IBUnde() {
		if (getIntent().getExtras() == null
				|| !getIntent().getExtras().containsKey(Bean_Key))
			BaseActivity.finish();
		ItemData = (BLComment) getIntent().getSerializableExtra(Bean_Key);
	}

	private void setSwitchbuttomContent(View view, String txt) {
		((TextView) view.findViewById(R.id.tv_switch_button_lable))
				.setText(txt);
		if (view.getId() == commentview_txt_switchbuttom_i_look_he_show.getId()) {
			switch_select_Up = (EaseSwitchButton) view
					.findViewById(R.id.switch_select);
			switch_select_Up.setOnCheckedChangeListener(this);
		} else {
			switch_select_Down = (EaseSwitchButton) view
					.findViewById(R.id.switch_select);
			switch_select_Down.setOnCheckedChangeListener(this);
		}

		// EaseSwitchButton switch_select = (EaseSwitchButton) view
		// .findViewById(R.id.switch_select);

	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getString(R.string.my_show_data_set));
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

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView == switch_select_Up) {// 上边
			PromptManager.ShowCustomToast(BaseContext, "switch_select_Up");
		}
		if (buttonView == switch_select_Down) {// 下边
			PromptManager.ShowCustomToast(BaseContext, "switch_select_Down");
		}
		// switch (buttonView.getId()) {
		// case (CompoundButton)switch_select_Down.getId():
		//
		// break;
		//
		// default:
		// break;
		// }

	}

}
