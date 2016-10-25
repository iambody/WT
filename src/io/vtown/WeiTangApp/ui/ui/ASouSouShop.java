package io.vtown.WeiTangApp.ui.ui;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.BShopSousou;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoader;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.ui.ATitileNoBase;
import io.vtown.WeiTangApp.ui.title.ABrandDetail;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-20 上午11:33:27
 * @author 商铺搜索
 */
public class ASouSouShop extends ATitileNoBase {
	// private ImageView sousou_shop_iv;
	private View shousoushop_show_nodata_lay;
	private TextView sousou_shop_cancle_txt;
	private EditText sousou_shop_ed;
	// 数据源
	private BShopSousou mBldComment = null;
	// 店铺iv
	private ImageView shop_sousou_shop_iv;
	// 店铺名字
	private TextView shop_sousou_name;
	// 店铺信息
	private TextView shop_sousou_inf;
	// 关注人数
//	private TextView shop_sousou_guanzhu;
	// privat
	private LinearLayout soushop_outlay;
	private String ToSouShop = null;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_sousou_shop);
		EventBus.getDefault().register(this, "RfeashData", BMessage.class);
		IBundl();
		IView();
		SetTitleHttpDataLisenter(this);
		if (!StrUtils.isEmpty(ToSouShop)) {
			sousou_shop_ed.setText(ToSouShop);

			hintKbTwo();
			ShopSouNet(ToSouShop);
		}
	}

	private void IBundl() {
		if (getIntent().getExtras() != null
				&& getIntent().getExtras().containsKey("id")) {
			ToSouShop = getIntent().getExtras().getString("id");

		}
	}

	/**
	 * 下一级店铺收藏取消收藏后进行及时刷新数据
	 */
	public void RfeashData(BMessage message) {
		if (message.getMessageType() == BMessage.Tage_ShopSouFrash) {
			if (!StrUtils.isEmpty(sousou_shop_ed.getText().toString().trim())) {
				ShopSouNet(sousou_shop_ed.getText().toString().trim());
			}
		}

	}

	private void IView() {
		shousoushop_show_nodata_lay=findViewById(R.id.shousoushop_show_nodata_lay);
		soushop_outlay = (LinearLayout) findViewById(R.id.soushop_outlay);
		soushop_outlay.setOnClickListener(this);
		soushop_outlay.setVisibility(View.GONE);
		sousou_shop_cancle_txt = (TextView) findViewById(R.id.sousou_shop_cancle_txt);
		sousou_shop_cancle_txt.setOnClickListener(this);
		sousou_shop_ed = (EditText) findViewById(R.id.sousou_shop_ed);
		shop_sousou_shop_iv = (ImageView) findViewById(R.id.shop_sousou_shop_iv);

		shop_sousou_name = (TextView) findViewById(R.id.shop_sousou_name);
		shop_sousou_inf = (TextView) findViewById(R.id.shop_sousou_inf);


	}

	private void ShopSouNet(String str) {

		PromptManager.showtextLoading(BaseContext,
				getResources().getString(R.string.loading));
		soushop_outlay.setVisibility(View.GONE);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", str);
		FBGetHttpData(map, Constants.SouShop, Method.GET, 0, LOAD_INITIALIZE);

	}

	@Override
	protected void InitTile() {

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
	protected void onDestroy() {
		super.onDestroy();
		try {
			EventBus.getDefault().unregister(this);
		} catch (Exception e) {
		}
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		if (StrUtils.isEmpty(Data.getHttpResultStr())) {
			DataError("暂无相关店铺", Data.getHttpLoadType());
			return;
		}
		IDataView(soushop_outlay, shousoushop_show_nodata_lay, NOVIEW_RIGHT);
		soushop_outlay.setVisibility(View.VISIBLE);
		// 开始解析****************************************************************
		JSONObject obj = null;
		try {
			obj = new JSONObject(Data.getHttpResultStr());
		} catch (JSONException e) {
			e.printStackTrace();
			return;
		} 
		
		// 开始解析*********************************************************************

		 mBldComment = JSON.parseObject(Data.getHttpResultStr(),
				 BShopSousou.class);

		IShop(mBldComment);
	}

	private void IShop(BShopSousou ad) {
		ImageLoaderUtil.Load2(ad.getAvatar(), shop_sousou_shop_iv,
				R.drawable.error_iv2);

		StrUtils.SetTxt(shop_sousou_name, ad.getSeller_name());

		StrUtils.SetTxt(shop_sousou_inf, ad.getIntro());

//		StrUtils.SetColorsTxt(BaseContext, shop_sousou_guanzhu,
//				R.color.app_fen, "关注人数:", StrUtils.NullToStr(ad.getAttention()));

	}

	@Override
	protected void DataError(String error, int LoadTyp) {
		PromptManager.ShowCustomToast(BaseContext,getResources().getString(R.string.sousoushop_nodata) );
		ShowErrorCanLoad(getResources().getString(R.string.sousoushop_nodata) );
		ShowErrorIv(R.drawable.error_sou);
		IDataView(soushop_outlay, shousoushop_show_nodata_lay, NOVIEW_ERROR);

	}

	@Override
	protected void MyClick(View V) {
		switch (V.getId()) {
		case R.id.sousou_shop_cancle_txt:
			if (StrUtils.isEmpty(sousou_shop_ed.getText().toString().trim())) {
				PromptManager.ShowCustomToast(BaseContext, getResources()
						.getString(R.string.shurushopid));
				return;

			}
			ShopSouNet(sousou_shop_ed.getText().toString().trim());
			break;
		case R.id.soushop_outlay:
			// 1代表品牌商 //0代表代理商家
			Intent mIntent = new Intent(BaseActivity, "1".equals(mBldComment
					.getIs_brand()) ? ABrandDetail.class : AShopDetail.class);
			mIntent.putExtra(BaseKey_Bean, new BComment(mBldComment.getId(),
					mBldComment.getSeller_name()));
			PromptManager.SkipActivity(BaseActivity, mIntent);
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
