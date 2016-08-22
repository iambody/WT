package io.vtown.WeiTangApp.ui.ui;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.AApplyProxy;
import io.vtown.WeiTangApp.ui.title.ABrandDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request.Method;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-6-6 下午2:10:57 查看资质页面
 */
public class ALookAptitude extends ATitleBase {

	/**
	 * 资质列表
	 */
	private ListView lv_my_aptitude_list;
	/**
	 * 用户信息
	 */
	private BUser user_Get;

	private List<BLComment> myBlComments = new ArrayList<BLComment>();

	@Override
	protected void InItBaseView() {

		setContentView(R.layout.activity_shopdetail_look_aptitude);
		user_Get = Spuit.User_Get(BaseContext);

		myBlComments = baseApplication.getZiYingShop_To_Ls();
		baseApplication.setZiYingShop_To_Ls(null);
		IList();
	}

	/**
	 * 查看证书 触发事件
	 */
	private void LookCertificate(String seller_id, String agency_id) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", user_Get.getSeller_id());
		map.put("agency_id", agency_id);
		FBGetHttpData(map, Constants.SHOP_BRAND_AGENT_LOOK_CERTIFICATE,
				Method.GET, 1, LOAD_INITIALIZE);

	}

	private void IList() {
		lv_my_aptitude_list = (ListView) findViewById(R.id.lv_my_aptitude_list);
		AaptitudeAdapter aaptitudeAdapter = new AaptitudeAdapter(
				R.layout.item_my_aptitude);
		lv_my_aptitude_list.setAdapter(aaptitudeAdapter);
		aaptitudeAdapter.Frash(myBlComments);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getString(R.string.look_aptitude));
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

	class AaptitudeAdapter extends BaseAdapter implements OnClickListener {

		private int ResourseId;
		private LayoutInflater inflater;
		private List<BLComment> blComments = new ArrayList<BLComment>();

		public AaptitudeAdapter(int ResourseId) {
			super();

			this.ResourseId = ResourseId;
			this.inflater = LayoutInflater.from(BaseContext);
		}

		@Override
		public int getCount() {

			return blComments.size();
		}

		public void Frash(List<BLComment> dd) {
			if (dd == null)
				return;
			blComments = dd;
			this.notifyDataSetChanged();
		}

		@Override
		public Object getItem(int arg0) {

			return blComments.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			AaptitudeItem aaptitude = null;
			if (arg1 == null) {

				aaptitude = new AaptitudeItem();
				arg1 = inflater.inflate(ResourseId, null);
				aaptitude.iv_my_aptitude_brand_icon = ViewHolder.get(arg1,
						R.id.iv_my_aptitude_brand_icon);
				aaptitude.tv_my_aptitude_desc = ViewHolder.get(arg1,
						R.id.tv_my_aptitude_desc);
				aaptitude.tv_my_aptitude_become_agent = ViewHolder.get(arg1,
						R.id.tv_my_aptitude_become_agent);
				aaptitude.tv_my_aptitude_look_certificate = ViewHolder.get(
						arg1, R.id.tv_my_aptitude_look_certificate);
				aaptitude.tv_my_aptitude_start = ViewHolder.get(arg1,
						R.id.tv_my_aptitude_start);

				aaptitude.tv_my_aptitude_look_certificate
						.setOnClickListener(this);
				arg1.setTag(aaptitude);

			} else {
				aaptitude = (AaptitudeItem) arg1.getTag();
			}

			// 1-没代理 2-正在审核中 3-已经代理

			final BLComment dddaa = blComments.get(arg0);
			ImageLoaderUtil.Load2(dddaa.getAvatar(),
					aaptitude.iv_my_aptitude_brand_icon, R.drawable.error_iv2);
			StrUtils.SetTxt(aaptitude.tv_my_aptitude_desc,
					dddaa.getSeller_name());
			StrUtils.SetColorsTxt(BaseContext, aaptitude.tv_my_aptitude_start,
					R.color.app_fen, "级别:",
					StrUtils.NullToStr(dddaa.getLevel()) + "级");
			aaptitude.iv_my_aptitude_brand_icon
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (dddaa.getIs_brand().equals("1")) {// 跳转到品牌店铺首页
								BComment mBComment = new BComment(
										dddaa.getId(), dddaa.getSeller_name());
								// 跳转到品牌品牌店铺
								PromptManager.SkipActivity(BaseActivity,
										new Intent(BaseActivity,
												ABrandDetail.class).putExtra(
												BaseKey_Bean, mBComment));
							}

						}
					});

			if (!StrUtils.isEmpty(dddaa.getStatus())) {
				if (dddaa.getStatus().equals("1")) {// 没有代理过
					aaptitude.tv_my_aptitude_become_agent.setText("申请代理");
					aaptitude.tv_my_aptitude_become_agent
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {// 成为代理
									PromptManager.SkipActivity(BaseActivity,
											new Intent(BaseContext,
													AApplyProxy.class)
													.putExtra("brandid",
															dddaa.getId()));
								}
							});
				}
				if (dddaa.getStatus().equals("2")) {// 正在审核中
					aaptitude.tv_my_aptitude_become_agent.setText("审核中");
					aaptitude.tv_my_aptitude_become_agent.setClickable(false);
				}
				if (dddaa.getStatus().equals("3")) {// 已经代理过
					aaptitude.tv_my_aptitude_become_agent.setText("您已代理过");
					aaptitude.tv_my_aptitude_become_agent.setClickable(false);
				}
			}

			// StrUtils.SetTxt(aaptitude.tv_my_aptitude_start,
			// StrUtils.NullToStr(dddaa.getLevel()) + "级");
			if (Spuit.User_Get(BaseContext).getSeller_id()
					.equals(dddaa.getId())) {
				aaptitude.tv_my_aptitude_become_agent.setVisibility(View.GONE);

			} else {
				aaptitude.tv_my_aptitude_become_agent
						.setVisibility(View.VISIBLE);
			}
			return arg1;
		}

		@Override
		public void onClick(View V) {
			switch (V.getId()) {
			case R.id.tv_my_aptitude_look_certificate:

				break;

			default:
				break;
			}
		}

	}

	class AaptitudeItem {
		TextView tv_my_aptitude_start;
		ImageView iv_my_aptitude_brand_icon;
		TextView tv_my_aptitude_desc, tv_my_aptitude_become_agent,
				tv_my_aptitude_look_certificate;
	}

}
