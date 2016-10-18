package io.vtown.WeiTangApp.ui.title.shop.purchase;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.purchase.BDPurchaseNoPayDetail;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.im.AChat;
import io.vtown.WeiTangApp.ui.title.ABrandDetail;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.account.ACashierDesk;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-7-28 下午4:17:20
 * 
 */
public class APurchaseNoPayDetail extends ATitleBase {

	/**
	 * 加载数据成功的布局
	 */
	private LinearLayout shop_purchase_order_no_pay_detail_outlay;
	/**
	 * 加载数据失败的布局
	 */
	private View shop_purchase_order_no_pay_detail_nodata_lay;
	/**
	 * 地址信息
	 */
	private View shop_purchase_my_order_no_pay_address;
	/**
	 * 订单信息
	 */
	private View shop_purchase_my_order_no_pay_order_message;
	/**
	 * 最外层的列表
	 */
	private CompleteListView item_shop_purchase_order_no_pay_detail_outside;
	/**
	 * 总价
	 */
	private TextView tv_shop_purchase_my_order_no_pay_total_price;
	/**
	 * 取消订单
	 */
	private TextView tv_shop_purchase_order_no_pay_cancel_order;
	/**
	 * 去支付
	 */
	private TextView tv_shop_purchase_order_no_pay_to_pay;
	/**
	 * 收货人
	 */
	private TextView commentview_add_name;
	/**
	 * 联系电话
	 */
	private TextView commentview_add_phone;
	/**
	 * 地址
	 */
	private TextView commentview_add_address;
	/**
	 * 订单号
	 */
	private TextView tv_order_id;
	/**
	 * 下单时间
	 */
	private TextView tv_ordering_time;
	/**
	 * 传进来的订单号
	 */
	private String order_sn;
	/**
	 * 传进来的member_id
	 */
	private String member_id;

	/**
	 * 是不是获取详情的标志
	 */
	private boolean isGetDetail = false;
	private BDPurchaseNoPayDetail data;
	/**
	 * 卡券
	 */
	private LinearLayout ll_shop_purchase_no_pay_used_balance_and_coupons;
	
	/**
	 * 使用卡券
	 */
	private TextView tv_shop_purchase_no_pay_used_coupons;
	

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_shop_my_purchase_no_pay_detail);
		SetTitleHttpDataLisenter(this);
		getExtraData();
		IView();
		IData(member_id, order_sn);
	}

	private void getExtraData() {
		Intent intent = getIntent();
		order_sn = intent.getStringExtra("order_sn");
		member_id = intent.getStringExtra("member_id");
		if (StrUtils.isEmpty(member_id) && StrUtils.isEmpty(order_sn)) {
			return;
		}

	}

	private void IView() {
		shop_purchase_order_no_pay_detail_outlay = (LinearLayout) findViewById(R.id.shop_purchase_order_no_pay_detail_outlay);
		shop_purchase_order_no_pay_detail_nodata_lay = findViewById(R.id.shop_purchase_order_no_pay_detail_nodata_lay);
		IDataView(shop_purchase_order_no_pay_detail_outlay,
				shop_purchase_order_no_pay_detail_nodata_lay, NOVIEW_INITIALIZE);
		shop_purchase_my_order_no_pay_address = findViewById(R.id.shop_purchase_my_order_no_pay_address);
		shop_purchase_my_order_no_pay_order_message = findViewById(R.id.shop_purchase_my_order_no_pay_order_message);
		item_shop_purchase_order_no_pay_detail_outside = (CompleteListView) findViewById(R.id.item_shop_purchase_order_no_pay_detail_outside);
		tv_shop_purchase_my_order_no_pay_total_price = (TextView) findViewById(R.id.tv_shop_purchase_my_order_no_pay_total_price);
		tv_shop_purchase_order_no_pay_cancel_order = (TextView) findViewById(R.id.tv_shop_purchase_order_no_pay_cancel_order);
		tv_shop_purchase_order_no_pay_to_pay = (TextView) findViewById(R.id.tv_shop_purchase_order_no_pay_to_pay);
		//卡券
		ll_shop_purchase_no_pay_used_balance_and_coupons = (LinearLayout) findViewById(R.id.ll_shop_purchase_no_pay_used_balance_and_coupons);
		tv_shop_purchase_no_pay_used_coupons = (TextView) findViewById(R.id.tv_shop_purchase_no_pay_used_coupons);
		
		
		ImageView commentview_add_iv = (ImageView) shop_purchase_my_order_no_pay_address
				.findViewById(R.id.commentview_add_iv);
		commentview_add_iv.setVisibility(View.GONE);
		commentview_add_name = (TextView) shop_purchase_my_order_no_pay_address
				.findViewById(R.id.commentview_add_name);
		commentview_add_phone = (TextView) shop_purchase_my_order_no_pay_address
				.findViewById(R.id.commentview_add_phone);
		commentview_add_address = (TextView) shop_purchase_my_order_no_pay_address
				.findViewById(R.id.commentview_add_address);

		ImageView iv_right_arrow = (ImageView) shop_purchase_my_order_no_pay_address
				.findViewById(R.id.iv_right_arrow);
		iv_right_arrow.setVisibility(View.GONE);

		shop_purchase_my_order_no_pay_address.setEnabled(false);

		tv_order_id = (TextView) shop_purchase_my_order_no_pay_order_message
				.findViewById(R.id.tv_order_id);
		// 让订单编号文本有可复制功能
		StrUtils.SetTextViewCopy(tv_order_id);
		tv_ordering_time = (TextView) shop_purchase_my_order_no_pay_order_message
				.findViewById(R.id.tv_ordering_time);
		LinearLayout ll_pay_time = (LinearLayout) shop_purchase_my_order_no_pay_order_message
				.findViewById(R.id.ll_pay_time);
		ll_pay_time.setVisibility(View.GONE);
		tv_shop_purchase_order_no_pay_cancel_order.setOnClickListener(this);
		tv_shop_purchase_order_no_pay_to_pay.setOnClickListener(this);
		shop_purchase_order_no_pay_detail_nodata_lay.setOnClickListener(this);
	}

	/**
	 * 获取详情数据
	 * 
	 * @param member_id
	 * @param order_sn
	 */
	private void IData(String member_id, String order_sn) {
		PromptManager.showtextLoading(BaseContext,
				getResources()
						.getString(R.string.xlistview_header_hint_loading));
		HashMap<String, String> map = new HashMap<String, String>();
		isGetDetail = true;
		map.put("member_id", member_id);
		map.put("order_sn", order_sn);
		FBGetHttpData(map, Constants.Purchase_No_Pay_Detail, Method.GET, 0,
				LOAD_INITIALIZE);

	}

	/**
	 * 取消订单
	 * 
	 * @param member_id
	 * @param seller_order_sn
	 */
	private void CancelOrder(String member_id, String order_sn) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("order_sn", order_sn);
		map.put("member_id", member_id);
		map.put("cancel_reason", "");
		map.put("cancel_type", "2");// 取消状态 1 自动取消 2 手动取消
		FBGetHttpData(map, Constants.Purchase_Cancel_Order, Method.PUT, 1,
				LOAD_INITIALIZE);
	}

	/**
	 * 去付款
	 */
	private void GoPay(String member_id, String order_sn) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("member_id", member_id);
		map.put("order_sn", order_sn);
		FBGetHttpData(map, Constants.Center_My_Order_Go_Pay, Method.PUT, 2,
				LOAD_INITIALIZE);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt("订单详情");
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {

		switch (Data.getHttpResultTage()) {
		case 0:// 获取详情数据
			if (StrUtils.isEmpty(Data.getHttpResultStr())) {
				return;
			}
			data = new BDPurchaseNoPayDetail();
			try {
				data = JSON.parseObject(Data.getHttpResultStr(),
						BDPurchaseNoPayDetail.class);

			} catch (Exception e) {
				DataError("解析失败", 1);
			}
			IDataView(shop_purchase_order_no_pay_detail_outlay,
					shop_purchase_order_no_pay_detail_nodata_lay, NOVIEW_RIGHT);
			RefreshView(data);
			break;

		case 1:// 取消订单
			PromptManager.ShowMyToast(BaseContext, "订单取消成功");
			EventBus.getDefault().post(new BMessage(BMessage.Tage_My_Purchase));
			this.finish();
			break;

		case 2:// 去付款
			BDComment data = new BDComment();
			try {
				data = JSON.parseObject(Data.getHttpResultStr(),
						BDComment.class);
			} catch (Exception e) {

			}
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					ACashierDesk.class).putExtra("addOrderInfo", data));
			break;

		}

	}

	private void RefreshView(BDPurchaseNoPayDetail data) {

		StrUtils.SetTxt(commentview_add_name, data.getUsername());
		StrUtils.SetTxt(commentview_add_phone, data.getMobile());

		StrUtils.SetTxt(
				commentview_add_address,
				data.getProvince() + data.getCity() + data.getArea()
						+ data.getAddress());

		StrUtils.SetTxt(tv_order_id, data.getOrder_sn());
		
		
		StrUtils.SetColorsTxt(BaseContext, tv_shop_purchase_no_pay_used_coupons, R.color.app_gray, "使用卡券：", String.format("%1$s元",StrUtils.SetTextForMony(data.getUsed_coupons_money())));
		//显示使用余额和卡券，只有金额不为0的时候才显示
		if(0 != Integer.parseInt(data.getUsed_coupons_money())){
			ll_shop_purchase_no_pay_used_balance_and_coupons.setVisibility(View.VISIBLE);
			
		}else{
			ll_shop_purchase_no_pay_used_balance_and_coupons.setVisibility(View.GONE);
		}

		StrUtils.SetTxt(
				tv_shop_purchase_my_order_no_pay_total_price,
				String.format("￥%1$s",
						StrUtils.SetTextForMony(data.getOrder_total_price())));

		StrUtils.SetTxt(tv_ordering_time,
				StrUtils.longtostr(Long.parseLong(data.getCreate_time())));

		PuschaseOrderNoPayOutsideAdapter centerOrderNoPayOutside = new PuschaseOrderNoPayOutsideAdapter(
				R.layout.item_shop_purchase_order_no_pay_detail_outside,
				data.getSon_order());
		item_shop_purchase_order_no_pay_detail_outside
				.setAdapter(centerOrderNoPayOutside);
	}

	@Override
	protected void DataError(String error, int LoadType) {
		PromptManager.ShowMyToast(BaseContext, error);
		if (LOAD_INITIALIZE == LoadType && isGetDetail) {
			IDataView(shop_purchase_order_no_pay_detail_outlay,
					shop_purchase_order_no_pay_detail_nodata_lay, NOVIEW_ERROR);
			ShowErrorCanLoad(getResources().getString(R.string.error_null_noda));
		}

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
		switch (V.getId()) {
		case R.id.shop_purchase_order_no_pay_detail_nodata_lay:// 错误的布局，重新加载数据

			break;
		case R.id.tv_shop_purchase_order_no_pay_cancel_order:// 取消订单
			ShowCustomDialog("确认取消订单吗？", "取消", "确认", new IDialogResult() {
				@Override
				public void RightResult() {
					if (CheckNet(BaseContext))
						return;
					CancelOrder(member_id, order_sn);
				}

				@Override
				public void LeftResult() {
				}
			});
			break;
		case R.id.tv_shop_purchase_order_no_pay_to_pay:// 去支付
			ShowCustomDialog("确认要去付款吗？", "取消", "确认", new IDialogResult() {
				@Override
				public void RightResult() {
					if (CheckNet(BaseContext))
						return;
					GoPay(member_id, order_sn);
				}

				@Override
				public void LeftResult() {
				}
			});
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

	class PuschaseOrderNoPayOutsideAdapter extends BaseAdapter {
		private int ResourseId;
		private LayoutInflater inflater;
		private List<BLDComment> secoud_datas = new ArrayList<BLDComment>();

		public PuschaseOrderNoPayOutsideAdapter(int ResourseId,
				List<BLDComment> datas) {
			super();
			this.ResourseId = ResourseId;
			this.inflater = LayoutInflater.from(BaseContext);
			this.secoud_datas = datas;
		}

		@Override
		public int getCount() {

			return secoud_datas.size();
		}

		@Override
		public Object getItem(int position) {

			return secoud_datas.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			PurchaseOrderNoPayInsideItem centerOrderNoPayInside = null;
			if (convertView == null) {
				centerOrderNoPayInside = new PurchaseOrderNoPayInsideItem();
				convertView = inflater.inflate(ResourseId, null);
				centerOrderNoPayInside.tv_purchase_order_no_pay_detail_seller_name = (TextView) convertView
						.findViewById(R.id.tv_purchase_order_no_pay_detail_seller_name);
				centerOrderNoPayInside.item_fragment_purchase_order_no_pay_detail_outside = (CompleteListView) convertView
						.findViewById(R.id.item_fragment_purchase_order_no_pay_detail_outside);
				centerOrderNoPayInside.ll_purchase_my_order_no_pay_contact_seller = (LinearLayout) convertView
						.findViewById(R.id.ll_purchase_my_order_no_pay_contact_seller);
				convertView.setTag(centerOrderNoPayInside);
			} else {
				centerOrderNoPayInside = (PurchaseOrderNoPayInsideItem) convertView
						.getTag();
			}
			StrUtils.SetTxt(
					centerOrderNoPayInside.tv_purchase_order_no_pay_detail_seller_name,
					secoud_datas.get(position).getSeller_name());
			CenterOrderNoPayInnerMostAdapter centerOrderNoPayInnerMost = new CenterOrderNoPayInnerMostAdapter(
					R.layout.item_shop_purchase_order_no_pay_inside,
					secoud_datas.get(position).getGoods());
			centerOrderNoPayInside.item_fragment_purchase_order_no_pay_detail_outside
					.setAdapter(centerOrderNoPayInnerMost);
			final int myItemPosition = position;
			// 点击商品item跳转商品详情页面
			centerOrderNoPayInside.item_fragment_purchase_order_no_pay_detail_outside
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {

							Intent intent = new Intent(BaseContext,
									AGoodDetail.class);
							intent.putExtra("goodid",
									secoud_datas.get(myItemPosition).getGoods()
											.get(position).getGoods_id());
							PromptManager.SkipActivity(BaseActivity, intent);
						}
					});

			// 联系卖家
			centerOrderNoPayInside.ll_purchase_my_order_no_pay_contact_seller
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (CheckNet(BaseContext))
								return;
//							PromptManager.SkipActivity(BaseActivity,
//									new Intent(BaseActivity, AChat.class));
							BComment mBComment = new BComment(secoud_datas.get(position).getSeller_id(),
									secoud_datas.get(position).getSeller_name());
							PromptManager.SkipActivity(BaseActivity,
									new Intent(BaseActivity, ABrandDetail.class)
											.putExtra(BaseKey_Bean, mBComment));
						}
					});
			return convertView;
		}

	}

	/**
	 * @author Yihuihua 最里层的AP
	 */
	class CenterOrderNoPayInnerMostAdapter extends BaseAdapter {

		private int ResourseId;
		private LayoutInflater inflater;
		private List<BDComment> innerMost_data = new ArrayList<BDComment>();

		public CenterOrderNoPayInnerMostAdapter(int ResourseId,
				List<BDComment> datas) {
			super();

			this.ResourseId = ResourseId;
			this.inflater = LayoutInflater.from(BaseContext);
			this.innerMost_data = datas;
		}

		@Override
		public int getCount() {

			return innerMost_data.size();
		}

		@Override
		public Object getItem(int position) {

			return innerMost_data.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PurchaseOrderNoPayDetailInnerMostItem centerOrderNoPayInnerMost = null;
			if (convertView == null) {
				centerOrderNoPayInnerMost = new PurchaseOrderNoPayDetailInnerMostItem();
				convertView = inflater.inflate(ResourseId, null);
				centerOrderNoPayInnerMost.item_purchase_order_no_pay_detail_in_iv = ViewHolder
						.get(convertView,
								R.id.item_purchase_order_no_pay_detail_in_iv);
				centerOrderNoPayInnerMost.item_purchase_order_no_pay_detail_in_name = ViewHolder
						.get(convertView,
								R.id.item_purchase_order_no_pay_detail_in_name);
				centerOrderNoPayInnerMost.item_purchase_order_no_pay_detail_in_price = ViewHolder
						.get(convertView,
								R.id.item_purchase_order_no_pay_detail_in_price);
				centerOrderNoPayInnerMost.item_purchase_order_no_pay_detail_in_number = ViewHolder
						.get(convertView,
								R.id.item_purchase_order_no_pay_detail_in_number);
				convertView.setTag(centerOrderNoPayInnerMost);
				ImageLoaderUtil
						.Load2(innerMost_data.get(position).getGoods_cover(),
								centerOrderNoPayInnerMost.item_purchase_order_no_pay_detail_in_iv,
								R.drawable.error_iv2);
			} else {
				centerOrderNoPayInnerMost = (PurchaseOrderNoPayDetailInnerMostItem) convertView
						.getTag();
			}

			StrUtils.SetTxt(
					centerOrderNoPayInnerMost.item_purchase_order_no_pay_detail_in_name,
					innerMost_data.get(position).getGoods_name());
			StrUtils.SetTxt(
					centerOrderNoPayInnerMost.item_purchase_order_no_pay_detail_in_price,
					String.format("￥%1$s元", StrUtils
							.SetTextForMony(innerMost_data.get(position)
									.getGoods_money())));
			StrUtils.SetTxt(
					centerOrderNoPayInnerMost.item_purchase_order_no_pay_detail_in_number,
					String.format("X%1$s", innerMost_data.get(position)
							.getGoods_number()));
			return convertView;
		}

	}

	/**
	 * @author Yihuihua 第二层列表Holder
	 */
	class PurchaseOrderNoPayInsideItem {
		public TextView tv_purchase_order_no_pay_detail_seller_name;// 买家名称
		public CompleteListView item_fragment_purchase_order_no_pay_detail_outside;// 第二层的订单列表
		public LinearLayout ll_purchase_my_order_no_pay_contact_seller;// 联系卖家
	}

	/**
	 * @author Yihuihua 最里层的Holder
	 */
	class PurchaseOrderNoPayDetailInnerMostItem {
		public ImageView item_purchase_order_no_pay_detail_in_iv;// 商品图标
		public TextView item_purchase_order_no_pay_detail_in_name;// 商品title
		public TextView item_purchase_order_no_pay_detail_in_price;// 商品价格
		public TextView item_purchase_order_no_pay_detail_in_number;// 商品个数
	}

}
