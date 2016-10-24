package io.vtown.WeiTangApp.ui.title.shop.odermanger;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.ui.ATitleBase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-7-11 下午7:50:36 编辑商品价格
 * 
 */
public class AEditGoodPrice extends ATitleBase implements TextWatcher {

	/**
	 * 商品图标
	 */
	private ImageView iv_good_edit_good_icon;
	/**
	 * 商品类型，是否是品牌商品
	 */
	private ImageView iv_good_edit_goods_type;
	/**
	 * 商品名称
	 */
	private TextView tv_good_edit_good_title;
	/**
	 * 规格
	 */
	private TextView tv_good_edit_content_value;
	/**
	 * 价格
	 */
	private TextView tv_good_edit_good_price;
	/**
	 * 个数
	 */
	private TextView tv_good_edit_good_count;
	/**
	 * 价格输入框
	 */
	private EditText et_good_edit_price;
	/**
	 * 修改之前的个数
	 */
	private TextView tv_good_edit_modity_before_count;
	/**
	 * 修改之前的价格
	 */
	private TextView tv_good_edit_price_modify_before;

	/**
	 * 修改完成按钮
	 */
	private TextView tv_good_edit_success;
	/**
	 * 修改价格提示
	 */
	private TextView tv_modify_price_tips;
	/**
	 * 传进来的数据
	 */
	private BLComment goodInfo;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_shop_good_manger_edit_price);
		goodInfo = (BLComment) getIntent().getSerializableExtra("goodInfo");
		if (goodInfo == null) {
			return;
		}
		IView();
		IData(goodInfo);
	}

	/**
	 * 初始控件
	 */
	private void IView() {
		iv_good_edit_good_icon = (ImageView) findViewById(R.id.iv_good_edit_good_icon);
		iv_good_edit_goods_type = (ImageView) findViewById(R.id.iv_good_edit_goods_type);
		tv_good_edit_good_title = (TextView) findViewById(R.id.tv_good_edit_good_title);
		tv_good_edit_content_value = (TextView) findViewById(R.id.tv_good_edit_content_value);
		tv_good_edit_good_price = (TextView) findViewById(R.id.tv_good_edit_good_price);
		tv_good_edit_good_count = (TextView) findViewById(R.id.tv_good_edit_good_count);
		et_good_edit_price = (EditText) findViewById(R.id.et_good_edit_price);
		tv_good_edit_modity_before_count = (TextView) findViewById(R.id.tv_good_edit_modity_before_count);
		tv_good_edit_price_modify_before = (TextView) findViewById(R.id.tv_good_edit_price_modify_before);
		tv_modify_price_tips = (TextView) findViewById(R.id.tv_modify_price_tips);

		tv_good_edit_success = (TextView) findViewById(R.id.tv_good_edit_success);
		et_good_edit_price.addTextChangedListener(this);
	}

	/**
	 * 刷新控件数据
	 * 
	 * @param goodInfo
	 */
	private void IData(BLComment goodInfo) {
		ImageLoaderUtil.Load2(goodInfo.getGoods_cover(), iv_good_edit_good_icon,
				R.drawable.error_iv2);
		String goods_type = goodInfo.getGoods_type();
		if ("0".equals(goods_type)) {
			iv_good_edit_goods_type.setVisibility(View.GONE);
		} else {
			iv_good_edit_goods_type.setVisibility(View.VISIBLE);
		}
		StrUtils.SetTxt(tv_good_edit_good_title, goodInfo.getGoods_name());
		StrUtils.SetTxt(tv_good_edit_content_value,
				goodInfo.getGoods_standard());
		StrUtils.SetTxt(
				tv_good_edit_good_price,
				String.format("￥%1$s元",
						StrUtils.SetTextForMony(goodInfo.getGoods_price())));
		StrUtils.SetTxt(tv_good_edit_good_count,
				String.format("x%1$s", goodInfo.getGoods_number()));
		StrUtils.SetTxt(tv_good_edit_modity_before_count, "修改前价格:");
		StrUtils.SetTxt(tv_modify_price_tips,
				String.format("价格不能小于%1$s元", StrUtils.SetTextForMony(goodInfo.getPurchase_price())));
//		StrUtils.SetTxt(
//				tv_good_edit_price_modify_before,
//				String.format("￥%1$s元",
//						StrUtils.SetTextForMony(goodInfo.getGoods_money())));

		StrUtils.SetMoneyFormat(BaseContext,tv_good_edit_price_modify_before,goodInfo.getGoods_money(),17);
		et_good_edit_price.setText(StrUtils.SetTextForMony(goodInfo.getGoods_money()));
		
		tv_good_edit_success.setOnClickListener(this);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt("修改价格");
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
		case R.id.tv_good_edit_success:// 修改完成

			String price = et_good_edit_price.getText().toString().trim();
			if (StrUtils.isEmpty(price)) {
				PromptManager.ShowMyToast(BaseContext, "请输入价格");
				return;
			}
			
			
			BMessage bMessage = new BMessage(251);
			bMessage.setTageEditGoodPrice(Float.parseFloat(price) * 100);
			EventBus.getDefault().post(bMessage);
			this.finish();
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

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		float editpriceF = 0.0f;
		if(StrUtils.isEmpty(s.toString().trim())){
			editpriceF = 0.0f;
		}else{
			editpriceF = Float.parseFloat(s.toString().trim());
		}
		float goodmoneyF = Float.parseFloat(goodInfo.getPurchase_price())/100;
		if (editpriceF < goodmoneyF) {

			tv_modify_price_tips.setVisibility(View.VISIBLE);
			tv_good_edit_success.setEnabled(false);
			tv_good_edit_success.setBackgroundResource(R.drawable.regist_code_shape_pre);
		} else {
			tv_modify_price_tips.setVisibility(View.GONE);
			tv_good_edit_success.setEnabled(true);
			tv_good_edit_success.setBackgroundResource(R.drawable.select_white_to_fen1);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

}
