package io.vtown.WeiTangApp.ui.title.shop.channel;

import java.util.HashMap;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.channl.BChannl;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.circlescroll.CircleImageView;
import io.vtown.WeiTangApp.comment.view.circlescroll.CircleLayout;
import io.vtown.WeiTangApp.comment.view.circlescroll.CircleLayout.OnItemClickListener;
import io.vtown.WeiTangApp.comment.view.circlescroll.CircleLayout.OnItemSelectedListener;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.ui.CaptureActivity;
import io.vtown.WeiTangApp.ui.comment.ACommentList;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-11 下午2:23:38
 * @author 渠道管理
 * 
 */
public class AChannel extends ATitleBase {
	private ScrollView channel_out_lay;
	private View channel_nodata_lay;
	/**
	 * 本周销量
	 */
	private LinearLayout ll_this_week_sales;

	/**
	 * 本周进货量
	 */
	// private LinearLayout ll_this_week_goods_in;

	/**
	 * 我的上级
	 */
	private LinearLayout ll_my_superior;

	/**
	 * 我的下级
	 */
	private LinearLayout ll_my_junior;

	/**
	 * 成为下级
	 */
	private View be_junior;

	/**
	 * 发展下线
	 */
	private View develop_junior;

	/**
	 * 邀请上级
	 */
	private View invite_superior;

	/**
	 * 邀请记录
	 */
	private View invite_record;

	/**
	 * 本周进货量
	 */
	// private TextView tv_this_week_goods_in;

	/**
	 * 本周销量
	 */
	private TextView tv_this_week_sales;

	/**
	 * 我的上级
	 */
	private TextView tv_my_superior;

	/**
	 * 我的下级
	 */
	private TextView tv_my_junior;

	/**
	 * 用户信息
	 */
	private BUser user_Get;

	// TextView channle_circle_txt;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_channel);
		user_Get = Spuit.User_Get(BaseContext);
		IView();
		ICache();
		IData();
	}

	/**
	 * 获取缓存
	 */
	private void ICache() {
		if (!StrUtils.isEmpty(CacheUtil.Shop_Channel_Get(BaseContext))) {// 存在缓存
			IDataView(channel_out_lay, channel_nodata_lay, NOVIEW_RIGHT);
			BChannl bd = new BChannl();
			try {
				bd = JSON.parseObject(CacheUtil.Shop_Channel_Get(BaseContext),
						BChannl.class);
				RefrashView(bd);
				// PromptManager.ShowCustomToast(BaseContext, "缓存数据");
			} catch (Exception e) {
				IDataView(channel_out_lay, channel_nodata_lay,
						NOVIEW_INITIALIZE);

				// PromptManager.ShowCustomToast(BaseContext, "缓存数据解析失败");
			}

		} else {
			PromptManager.showtextLoading(BaseContext, getResources()
					.getString(R.string.loading));
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	private void IData() {

		SetTitleHttpDataLisenter(this);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", user_Get.getSeller_id());
		FBGetHttpData(map, Constants.SHOP_CHANNEL_MANAGE, Method.GET, 0,
				LOAD_INITIALIZE);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.shop_oder_channel));

	}

	private void IView() {

		channel_out_lay = (ScrollView) findViewById(R.id.channel_out_lay);
		channel_nodata_lay = findViewById(R.id.channel_nodata_lay);

		IDataView(channel_out_lay, channel_nodata_lay, NOVIEW_INITIALIZE);
		ShowErrorCanLoad("点我重试哦");
		ll_this_week_sales = (LinearLayout) findViewById(R.id.ll_this_week_sales);
		// ll_this_week_goods_in = (LinearLayout)
		// findViewById(R.id.ll_this_week_goods_in);
		ll_my_superior = (LinearLayout) findViewById(R.id.ll_my_superior);
		ll_my_junior = (LinearLayout) findViewById(R.id.ll_my_junior);

		// tv_this_week_goods_in = (TextView)
		// findViewById(R.id.tv_this_week_goods_in);
		tv_this_week_sales = (TextView) findViewById(R.id.tv_this_week_sales);
		tv_my_superior = (TextView) findViewById(R.id.tv_my_superior);
		tv_my_junior = (TextView) findViewById(R.id.tv_my_junior);

		be_junior = (View) findViewById(R.id.be_junior);

		develop_junior = (View) findViewById(R.id.develop_junior);

		invite_superior = (View) findViewById(R.id.invite_superior);

		invite_record = (View) findViewById(R.id.invite_record);

		SetItemContent(be_junior, R.string.be_junior);
		SetItemContent(develop_junior, R.string.develop_junior);
		SetItemContent(invite_superior, R.string.invite_superior);
		SetItemContent(invite_record, R.string.invite_record);



		ShowErrorCanLoad(getResources().getString(R.string.error_null_noda));
		// be_junior.setOnClickListener(this);
		// develop_junior.setOnClickListener(this);
		// invite_superior.setOnClickListener(this);
		// invite_record.setOnClickListener(this);
		ll_my_superior.setOnClickListener(this);
		ll_my_junior.setOnClickListener(this);
		channel_nodata_lay.setOnClickListener(this);
		// ICircle();
	}

	// private void ICircle() {
	// CircleLayout circleMenu = (CircleLayout)
	// findViewById(R.id.channle_circle_layout);
	// circleMenu.setOnItemSelectedListener(this);
	// circleMenu.setOnItemClickListener(this);
	//
	// channle_circle_txt = (TextView) findViewById(R.id.channle_circle_txt);
	// channle_circle_txt.setText(((CircleImageView) circleMenu
	// .getSelectedItem()).getName());
	// }

	private void SetItemContent(View VV, int ResourceTitle) {
		((TextView) VV.findViewById(R.id.comment_txtarrow_title))
				.setText(getResources().getString(ResourceTitle));
		VV.setOnClickListener(this);
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		if (StrUtils.isEmpty(Data.getHttpResultStr())) {
			DataError("数据异常", 1);
			return;
		}

		BChannl bd = new BChannl();
		try {
			CacheUtil.Shop_Channel_Save(BaseContext, Data.getHttpResultStr());
			bd = JSON.parseObject(Data.getHttpResultStr(), BChannl.class);
			RefrashView(bd);
			IDataView(channel_out_lay, channel_nodata_lay, NOVIEW_RIGHT);

		} catch (Exception e) {
			DataError("解析失败", 1);
		}

	}

	@Override
	protected void DataError(String error, int LoadTyp) {
		PromptManager.ShowMyToast(BaseActivity, error);
		IDataView(channel_out_lay, channel_nodata_lay, NOVIEW_ERROR);

		if (!StrUtils.isEmpty(CacheUtil.Shop_Channel_Get(BaseContext))) {
			IDataView(channel_out_lay, channel_nodata_lay, NOVIEW_RIGHT);
		}
	}

	private void RefrashView(BChannl bd) {
		StrUtils.SetTxt(tv_this_week_sales, bd.getWeekSales());
		// StrUtils.SetTxt(tv_this_week_goods_in, bd.getWeekstock());
		StrUtils.SetTxt(tv_my_superior, bd.getSuperior());
		StrUtils.SetTxt(tv_my_junior, bd.getJunior());
	}

	@Override
	protected void NetConnect() {
		NetError.setVisibility(View.GONE);
		IData();
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
		switch (V.getId()) {
		case R.id.ll_my_superior:// 我的上级

			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					ACommentList.class).putExtra(ACommentList.Tage_ResultKey,
					ACommentList.Tage_My_Superior));
			break;
		case R.id.ll_my_junior:// 我的下级
			// PromptManager.SkipActivity(BaseActivity, new
			// Intent(BaseContext,AMyJunior.class));

			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					ACommentList.class).putExtra(ACommentList.Tage_ResultKey,
					ACommentList.Tage_My_Junior));
			break;
		case R.id.be_junior:// 成为下级
			// PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
			// ABeJunior.class).putExtra("url", "测试"));
			// 上边是测试 应该先跳转到扫一扫界面 然后在跳转到ABeJunior 界面
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					CaptureActivity.class));
			break;
		case R.id.develop_junior:// 发展下级
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					ADevelopJunior.class));
			break;
		case R.id.invite_superior:// 邀请上级
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					AInviteSuperior.class));
			break;
		case R.id.invite_record:// 邀请记录
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					AInviteRecord.class));
			break;
		case R.id.channel_nodata_lay:
			IData();
			//
			break;

		}
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	/*@Override
	public void onItemClick(View view, int position, long id, String name) {
		// PromptManager.ShowCustomToast(BaseContext, "位置" + position);
		switch (position) {
		case 0:// 成为下级
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					CaptureActivity.class));
			break;
		case 1:// 发展下级
			if (CheckNet(BaseContext))
				return;
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					ADevelopJunior.class));
			break;
		case 2:// 发展上机

			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					AInviteSuperior.class));
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemSelected(View view, int position, long id, String name) {
		channle_circle_txt.setText(name);
	}
*/
}
