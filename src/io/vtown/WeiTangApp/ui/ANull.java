package io.vtown.WeiTangApp.ui;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.ui.title.ABrandDetail;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-8-17 下午12:43:24
 * 
 */
public class ANull extends ATitleBase {
	/**
	 * 用户信息
	 */
	private BUser user_Get;

	 

	/**
	 * 数据的处理
	 */
	private void IData() {
		SetTitleHttpDataLisenter(this);
		PromptManager.showtextLoading(BaseContext,
				getResources().getString(R.string.loading));
		// 获取基本信息
		HashMap<String, String> map = new HashMap<String, String>();

		map.put("seller_id", baseBcBComment.getId());
		map.put("_member_id", user_Get.getId());
		FBGetHttpData(map, Constants.Shop_Inf, Method.GET, 0, LOAD_INITIALIZE);

	}

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_null_for_shopdetailskip);
		user_Get = Spuit.User_Get(BaseContext);
		IData();
	}

	@Override
	protected void InitTile() {
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {

		try {
			JSONObject obj = new JSONObject(Data.getHttpResultStr());

			JSONObject objs = new JSONObject(obj.getString("base"));
			if (objs.getString("is_brand").equals("1")) {// 品牌店铺
				PromptManager.SkipActivity(BaseActivity, new Intent(
						BaseActivity, ABrandDetail.class).putExtra(
						BaseKey_Bean, baseBcBComment));
			} else {// 自营店铺
				PromptManager.SkipActivity(BaseActivity, new Intent(
						BaseActivity, AShopDetail.class).putExtra(BaseKey_Bean,
						baseBcBComment));
			}
			this.finish();
		} catch (JSONException e) {
			e.printStackTrace();
		}

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
