package io.vtown.WeiTangApp.ui.title.shop.addgood;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-6-2 下午4:58:48 添加商品之预览
 * 
 */
public class AGoodPreview extends ATitleBase {

	/**
	 * banaer布局
	 */
	private RelativeLayout rl_add_good_preview_gooddetail_page_lay;
	/**
	 * banaer ViewPager
	 */
	private ViewPager vp_add_good_preview_gooddetail_pager;
	/**
	 * banaer 点的布局
	 */
	private LinearLayout ll_add_good_preview_gooddetail_viewGroup;
	/**
	 * 商品标题
	 */
	private TextView tv_good_add_good_preview_title;
	/**
	 * 查看show
	 */
	private RelativeLayout rl_add_good_preview_look_show;
	/**
	 * show的图标
	 */
	private ImageView iv_add_good_preview_show_icon;
	/**
	 * 多少条show
	 */
	private TextView tv_add_good_preview_show_count;
	/**
	 * 零售价
	 */
	private TextView tv_add_good_preview_suggest_retail_price;
	/**
	 * 发货地址
	 */
	private TextView tv_add_good_preview_send_address;
	/**
	 * 运费
	 */
	private TextView tv_add_good_preview_freight;
	/**
	 * 卖家条目布局
	 */
	private RelativeLayout rl_add_good_preview_seller;
	/**
	 * 卖家头像
	 */
	private ImageView iv_add_good_preview_seller_icon;
	/**
	 * 卖家名称
	 */
	private TextView tv_add_good_preview_seller_shop_name;
	/**
	 * 商品文字描述
	 */
	private TextView tv_add_good_preview_good_desc_txt;

	/**
	 * 直接上架
	 */
	private TextView tv_add_good_preview_replace_sell;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_add_good_preview);
		IView();
	}

	private void IView() {
		rl_add_good_preview_gooddetail_page_lay = (RelativeLayout) findViewById(R.id.rl_add_good_preview_gooddetail_page_lay);
		vp_add_good_preview_gooddetail_pager = (ViewPager) findViewById(R.id.vp_add_good_preview_gooddetail_pager);
		ll_add_good_preview_gooddetail_viewGroup = (LinearLayout) findViewById(R.id.ll_add_good_preview_gooddetail_viewGroup);
		tv_good_add_good_preview_title = (TextView) findViewById(R.id.tv_good_add_good_preview_title);
		rl_add_good_preview_look_show = (RelativeLayout) findViewById(R.id.rl_add_good_preview_look_show);
		iv_add_good_preview_show_icon = (ImageView) findViewById(R.id.iv_add_good_preview_show_icon);
		tv_add_good_preview_show_count = (TextView) findViewById(R.id.tv_add_good_preview_show_count);
		tv_add_good_preview_suggest_retail_price = (TextView) findViewById(R.id.tv_add_good_preview_suggest_retail_price);
		tv_add_good_preview_send_address = (TextView) findViewById(R.id.tv_add_good_preview_send_address);
		tv_add_good_preview_freight = (TextView) findViewById(R.id.tv_add_good_preview_freight);
		rl_add_good_preview_seller = (RelativeLayout) findViewById(R.id.rl_add_good_preview_seller);
		iv_add_good_preview_seller_icon = (ImageView) findViewById(R.id.iv_add_good_preview_seller_icon);
		tv_add_good_preview_seller_shop_name = (TextView) findViewById(R.id.tv_add_good_preview_seller_shop_name);
		tv_add_good_preview_good_desc_txt = (TextView) findViewById(R.id.tv_add_good_preview_good_desc_txt);
		tv_add_good_preview_replace_sell = (TextView) findViewById(R.id.tv_add_good_preview_replace_sell);
		tv_add_good_preview_replace_sell.setOnClickListener(this);
		rl_add_good_preview_look_show.setOnClickListener(this);
		rl_add_good_preview_seller.setOnClickListener(this);

	}

	@Override
	protected void InitTile() {
		SetTitleTxt("预览");
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
		case R.id.rl_add_good_preview_look_show:

			break;

		case R.id.rl_add_good_preview_seller:

			break;

		case R.id.tv_add_good_preview_replace_sell:

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
