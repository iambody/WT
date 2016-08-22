package io.vtown.WeiTangApp.ui.comment;

import android.os.Bundle;
import android.view.View;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-28 下午3:05:01 所有纯GradView的activity TODO确定是否全部为分页加载
 */
public class ACommentGrad extends ATitleBase {
	/**
	 * 标题
	 */
	private String Title="";
	/**
	 * 标识位
	 */
	private int Key_Type;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_commnegrad);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(Title);
//		switch (Key_Type) {
//		case 1:
//
//			break;
//
//		default:
//			break;
//		}
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
	}

	@Override
	protected void DataError(String error,int LoadType) {
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
