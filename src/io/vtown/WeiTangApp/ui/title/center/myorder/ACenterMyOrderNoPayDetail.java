package io.vtown.WeiTangApp.ui.title.center.myorder;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.centerorder.BDCenterOrderNoPayDetail;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.im.AChatLoad;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.account.ACashierDesk;
import io.vtown.WeiTangApp.ui.title.center.set.AAddressManage;
import io.vtown.WeiTangApp.ui.title.mynew.ANew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ArrowKeyMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.BufferType;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-7-6 上午10:37:39 我的----我的订单---未付款订单详情
 */
public class ACenterMyOrderNoPayDetail extends ATitleBase {

	/**
	 * member_id
	 */
	private String member_id;
	/**
	 * 订单号
	 */
	private String order_sn;
	/**
	 * 地址信息
	 */
	private View center_my_order_no_pay_address;
	/**
	 * 订单信息
	 */
	private View center_my_order_no_pay_order_message;
	/**
	 * 订单号
	 */
	private TextView tv_center_order_no_pay_order_sn;
	/**
	 * 订单列表
	 */
	private CompleteListView item_center_order_no_pay_detail_outside;
	/**
	 * 取消订单
	 */
	private TextView tv_center_my_order_no_pay_cancel_order;
	/**
	 * 去付款
	 */
	private TextView tv_center_my_order_no_pay_to_pay;
	/**
	 * 姓名
	 */
	private TextView commentview_add_name;
	/**
	 * 电话
	 */
	private TextView commentview_add_phone;
	/**
	 * 详细地址
	 */
	private TextView commentview_add_address;
	/**
	 * 订单编号
	 */
	private TextView tv_order_id;
	/**
	 * 下单时间
	 */
	private TextView tv_ordering_time;

	/**
	 * 数据对象
	 */
	private BDCenterOrderNoPayDetail data = null;

	/**
	 * 修改地址的请求码
	 */
	private static final int REQUEST_CODE_ADDRESS = 213;
	/**
	 * 获取到数据之后显示的布局
	 */
	private LinearLayout center_my_order_no_pay_detail_outlay;
	/**
	 * 获取数据失败之后显示的布局
	 */
	private View center_my_order_no_pay_detail_nodata_lay;

	/**
	 * 当此标志是获取详情数据时，失败时需要显示失败布局
	 */
	private boolean isGetDetail = false;
	/**
	 * seller_order_sn
	 */
	private String seller_order_sn;
	/**
	 * 总价
	 */
	private TextView tv_center_my_order_no_pay_total_price;
	private LinearLayout ll_center_order_no_pay_used_balance_and_coupons;
	private TextView tv_center_order_no_pay_used_coupons;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_center_my_order_no_pay_detail);
		Intent intent = getIntent();
		member_id = intent.getStringExtra("member_id");
		order_sn = intent.getStringExtra("order_sn");
		seller_order_sn = intent.getStringExtra("seller_order_sn");
		IView();
		IData();
	}

	/**
	 * 初始化控件
	 */
	private void IView() {

		center_my_order_no_pay_detail_outlay = (LinearLayout) findViewById(R.id.center_my_order_no_pay_detail_outlay);
		center_my_order_no_pay_detail_nodata_lay = findViewById(R.id.center_my_order_no_pay_detail_nodata_lay);
		IDataView(center_my_order_no_pay_detail_outlay,
				center_my_order_no_pay_detail_nodata_lay, NOVIEW_INITIALIZE);

		// 地址相关信息
		center_my_order_no_pay_address = findViewById(R.id.center_my_order_no_pay_address);
		ImageView commentview_add_iv = (ImageView) center_my_order_no_pay_address
				.findViewById(R.id.commentview_add_iv);
		commentview_add_iv.setVisibility(View.GONE);
		commentview_add_name = (TextView) center_my_order_no_pay_address
				.findViewById(R.id.commentview_add_name);
		commentview_add_phone = (TextView) center_my_order_no_pay_address
				.findViewById(R.id.commentview_add_phone);
		commentview_add_address = (TextView) center_my_order_no_pay_address
				.findViewById(R.id.commentview_add_address);
		
		ll_center_order_no_pay_used_balance_and_coupons = (LinearLayout) findViewById(R.id.ll_center_order_no_pay_used_balance_and_coupons);
		tv_center_order_no_pay_used_coupons = (TextView) findViewById(R.id.tv_center_order_no_pay_used_coupons);

		ImageView iv_right_arrow = (ImageView) center_my_order_no_pay_address
				.findViewById(R.id.iv_right_arrow);
		iv_right_arrow.setVisibility(View.GONE);

		center_my_order_no_pay_address.setEnabled(false);

		// 订单信息
		center_my_order_no_pay_order_message = findViewById(R.id.center_my_order_no_pay_order_message);
		tv_order_id = (TextView) center_my_order_no_pay_order_message
				.findViewById(R.id.tv_order_id);
		tv_ordering_time = (TextView) center_my_order_no_pay_order_message
				.findViewById(R.id.tv_ordering_time);
		LinearLayout ll_pay_time = (LinearLayout) center_my_order_no_pay_order_message
				.findViewById(R.id.ll_pay_time);
		ll_pay_time.setVisibility(View.GONE);

		// 让订单编号文本有可复制功能
		 StrUtils.SetTextViewCopy(tv_order_id);

		item_center_order_no_pay_detail_outside = (CompleteListView) findViewById(R.id.item_center_order_no_pay_detail_outside);
		tv_center_my_order_no_pay_cancel_order = (TextView) findViewById(R.id.tv_center_my_order_no_pay_cancel_order);
		tv_center_my_order_no_pay_to_pay = (TextView) findViewById(R.id.tv_center_my_order_no_pay_to_pay);
		tv_center_my_order_no_pay_total_price = (TextView) findViewById(R.id.tv_center_my_order_no_pay_total_price);

		center_my_order_no_pay_address.setOnClickListener(this);
		tv_center_my_order_no_pay_cancel_order.setOnClickListener(this);
		tv_center_my_order_no_pay_to_pay.setOnClickListener(this);
		center_my_order_no_pay_detail_nodata_lay.setOnClickListener(this);

	}

	/**
	 * 刷新数据
	 * 
	 * @param data2
	 */
	private void RefreshView(BDCenterOrderNoPayDetail data2) {

		String name = getResources().getString(R.string.consignee_name_order);

		StrUtils.SetColorsTxt(BaseContext, commentview_add_name,
				R.color.app_gray, getString(R.string.tv_commentview_name),
				data2.getUsername());

		StrUtils.SetTxt(commentview_add_phone, data2.getMobile());
		StrUtils.SetTxt(
				commentview_add_address,
				data2.getProvince() + data2.getCity() + data2.getArea()
						+ data2.getAddress());

		StrUtils.SetTxt(tv_order_id, data2.getOrder_sn());
		
		StrUtils.SetColorsTxt(BaseContext, tv_center_order_no_pay_used_coupons, R.color.app_gray, "使用卡券：", String.format("%1$s元",StrUtils.SetTextForMony(data2.getUsed_coupons_money())));
		//显示使用余额和卡券，只有金额不为0的时候才显示
		if(0 != Integer.parseInt(data2.getUsed_coupons_money())){
			ll_center_order_no_pay_used_balance_and_coupons.setVisibility(View.VISIBLE);
			
		}else{
			ll_center_order_no_pay_used_balance_and_coupons.setVisibility(View.GONE);
		}

		StrUtils.SetTxt(
				tv_center_my_order_no_pay_total_price,
				String.format("￥%1$s",
						StrUtils.SetTextForMony(data2.getOrder_total_price())));

		StrUtils.SetTxt(tv_ordering_time,
				StrUtils.longtostr(Long.parseLong(data2.getCreate_time())));

		CenterOrderNoPayOutsideAdapter centerOrderNoPayOutside = new CenterOrderNoPayOutsideAdapter(
				R.layout.item_center_order_no_pay_detail_outside_item,
				data2.getSon_order());
		item_center_order_no_pay_detail_outside
				.setAdapter(centerOrderNoPayOutside);

	}

	/**
	 * 数据初始化
	 */
	private void IData() {
		PromptManager.showtextLoading(BaseContext,
				getResources()
						.getString(R.string.xlistview_header_hint_loading));
		SetTitleHttpDataLisenter(this);
		HashMap<String, String> map = new HashMap<String, String>();
		isGetDetail = true;
		map.put("order_sn", order_sn);
		map.put("member_id", member_id);
		FBGetHttpData(map, Constants.Center_Order_No_Pay_Detail, Method.GET, 0,
				LOAD_INITIALIZE);
	}

	/**
	 * 取消订单
	 */
	private void CancelMyOrder() {
		HashMap<String, String> map = new HashMap<String, String>();
		isGetDetail = false;
		map.put("member_id", member_id);
		map.put("order_sn", order_sn);
		FBGetHttpData(map, Constants.Center_My_Order_Cancel, Method.PUT, 1,
				LOAD_INITIALIZE);
	}

	/**
	 * 去付款
	 */
	private void GoPay() {
		HashMap<String, String> map = new HashMap<String, String>();
		isGetDetail = false;
		map.put("member_id", member_id);
		map.put("order_sn", order_sn);
		FBGetHttpData(map, Constants.Center_My_Order_Go_Pay, Method.PUT, 2,
				LOAD_INITIALIZE);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt("确认订单");
		ImageView right_right_iv = (ImageView) findViewById(R.id.right_right_iv);
		right_right_iv.setVisibility(View.VISIBLE);
		right_right_iv.setImageDrawable(getResources().getDrawable(
				R.drawable.new1));
		right_right_iv.setOnClickListener(this);
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		switch (Data.getHttpResultTage()) {
		case 0:// 获取详情数据
			if (StrUtils.isEmpty(Data.getHttpResultStr())) {
				return;
			}
			data = new BDCenterOrderNoPayDetail();
			try {
				data = JSON.parseObject(Data.getHttpResultStr(),
						BDCenterOrderNoPayDetail.class);

			} catch (Exception e) {
				DataError("解析失败", 1);
			}
			IDataView(center_my_order_no_pay_detail_outlay,
					center_my_order_no_pay_detail_nodata_lay, NOVIEW_RIGHT);
			RefreshView(data);
			break;

		case 1:// 取消订单
			PromptManager.ShowMyToast(BaseContext, "订单取消成功");
			EventBus.getDefault().post(
					new BMessage(BMessage.Tage_Center_Order_Updata));
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

		default:
			break;
		}

	}

	@Override
	protected void DataError(String error, int LoadType) {
		PromptManager.ShowMyToast(BaseContext, error);
		if (LOAD_INITIALIZE == LoadType && isGetDetail) {
			IDataView(center_my_order_no_pay_detail_outlay,
					center_my_order_no_pay_detail_nodata_lay, NOVIEW_ERROR);
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
		case R.id.center_my_order_no_pay_address:// 地址
			Intent intent = new Intent(BaseActivity, AAddressManage.class);
			intent.putExtra("NeedFinish", true);
			PromptManager.SkipResultActivity(BaseActivity, intent,
					REQUEST_CODE_ADDRESS);
			break;
		case R.id.tv_center_my_order_no_pay_cancel_order:// 取消订单
			if (CheckNet(BaseContext))
				return;

			ShowCustomDialog("确认取消订单吗？", "取消", "确认", new IDialogResult() {
				@Override
				public void RightResult() {
					CancelMyOrder();

				}

				@Override
				public void LeftResult() {
				}
			});

			break;

		case R.id.tv_center_my_order_no_pay_to_pay:// 去付款
			if (CheckNet(BaseContext))
				return;

			ShowCustomDialog("确认去付款？", "取消", "确认", new IDialogResult() {
				@Override
				public void RightResult() {
					GoPay();
				}

				@Override
				public void LeftResult() {
				}
			});

			break;
		case R.id.center_my_order_no_pay_detail_nodata_lay:// 重新加载数据
			if (CheckNet(BaseContext))
				return;
			IData();
			break;

		case R.id.right_right_iv:// 消息按钮
			if (CheckNet(BaseContext))
				return;
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					ANew.class));
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (REQUEST_CODE_ADDRESS == requestCode && RESULT_OK == resultCode) {
			BLComment bl = (BLComment) data.getSerializableExtra("AddressInfo");
			if (bl != null) {

				StrUtils.SetColorsTxt(BaseContext, commentview_add_name,
						R.color.app_gray,
						getString(R.string.tv_commentview_name), bl.getName());

				StrUtils.SetTxt(commentview_add_phone, bl.getMobile());
				StrUtils.SetTxt(
						commentview_add_address,
						bl.getProvince() + bl.getCity() + bl.getCounty()
								+ bl.getAddress());

			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	class CenterOrderNoPayOutsideAdapter extends BaseAdapter {
		private int ResourseId;
		private LayoutInflater inflater;
		private List<BLDComment> secoud_datas = new ArrayList<BLDComment>();

		public CenterOrderNoPayOutsideAdapter(int ResourseId,
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
		public View getView(int position, View convertView, ViewGroup parent) {
			CenterOrderNoPayInsideItem centerOrderNoPayInside = null;
			if (convertView == null) {
				centerOrderNoPayInside = new CenterOrderNoPayInsideItem();
				convertView = inflater.inflate(ResourseId, null);
				centerOrderNoPayInside.tv_center_order_no_pay_detail_seller_name = (TextView) convertView
						.findViewById(R.id.tv_center_order_no_pay_detail_seller_name);
				centerOrderNoPayInside.item_fragment_center_order_no_pay_detail_outside = (CompleteListView) convertView
						.findViewById(R.id.item_fragment_center_order_no_pay_detail_outside);
				centerOrderNoPayInside.ll_center_my_order_no_pay_contact_seller = (LinearLayout) convertView
						.findViewById(R.id.ll_center_my_order_no_pay_contact_seller);
				convertView.setTag(centerOrderNoPayInside);
			} else {
				centerOrderNoPayInside = (CenterOrderNoPayInsideItem) convertView
						.getTag();
			}
			StrUtils.SetTxt(
					centerOrderNoPayInside.tv_center_order_no_pay_detail_seller_name,
					secoud_datas.get(position).getSeller_name());
			CenterOrderNoPayInnerMostAdapter centerOrderNoPayInnerMost = new CenterOrderNoPayInnerMostAdapter(
					R.layout.item_center_order_no_pay_detail_inside,
					secoud_datas.get(position).getGoods());
			centerOrderNoPayInside.item_fragment_center_order_no_pay_detail_outside
					.setAdapter(centerOrderNoPayInnerMost);
			final int myItemPosition = position;
			// 点击商品item跳转商品详情页面
			centerOrderNoPayInside.item_fragment_center_order_no_pay_detail_outside
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
			centerOrderNoPayInside.ll_center_my_order_no_pay_contact_seller
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (!StrUtils.isEmpty(secoud_datas.get(
									myItemPosition).getSeller_id())) {
								PromptManager
										.SkipActivity(
												BaseActivity,
												new Intent(BaseActivity,
														AChatLoad.class)
														.putExtra(
																AChatLoad.Tage_Iv,
																secoud_datas
																		.get(myItemPosition)
																		.getAvatar())
														.putExtra(
																AChatLoad.Tage_TageId,
																secoud_datas
																		.get(myItemPosition)
																		.getSeller_id())
														.putExtra(
																AChatLoad.Tage_Name,
																secoud_datas
																		.get(myItemPosition)
																		.getSeller_name()));
							}
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
			CenterOrderNoPayDetailInnerMostItem centerOrderNoPayInnerMost = null;
			if (convertView == null) {
				centerOrderNoPayInnerMost = new CenterOrderNoPayDetailInnerMostItem();
				convertView = inflater.inflate(ResourseId, null);
				centerOrderNoPayInnerMost.item_center_order_no_pay_detail_in_iv = ViewHolder
						.get(convertView,
								R.id.item_center_order_no_pay_detail_in_iv);
				centerOrderNoPayInnerMost.item_center_order_no_pay_detail_in_name = ViewHolder
						.get(convertView,
								R.id.item_center_order_no_pay_detail_in_name);
				centerOrderNoPayInnerMost.item_center_order_no_pay_detail_in_price = ViewHolder
						.get(convertView,
								R.id.item_center_order_no_pay_detail_in_price);
				centerOrderNoPayInnerMost.item_center_order_no_pay_detail_in_number = ViewHolder
						.get(convertView,
								R.id.item_center_order_no_pay_detail_in_number);
				convertView.setTag(centerOrderNoPayInnerMost);
				ImageLoaderUtil
						.Load2(innerMost_data.get(position).getGoods_cover(),
								centerOrderNoPayInnerMost.item_center_order_no_pay_detail_in_iv,
								R.drawable.error_iv2);
			} else {
				centerOrderNoPayInnerMost = (CenterOrderNoPayDetailInnerMostItem) convertView
						.getTag();
			}

			StrUtils.SetTxt(
					centerOrderNoPayInnerMost.item_center_order_no_pay_detail_in_name,
					innerMost_data.get(position).getGoods_name());
			StrUtils.SetTxt(
					centerOrderNoPayInnerMost.item_center_order_no_pay_detail_in_price,
					String.format("￥%1$s元", StrUtils
							.SetTextForMony(innerMost_data.get(position)
									.getGoods_money())));
			StrUtils.SetTxt(
					centerOrderNoPayInnerMost.item_center_order_no_pay_detail_in_number,
					String.format("X%1$s", innerMost_data.get(position)
							.getGoods_number()));
			return convertView;
		}

	}

	/**
	 * @author Yihuihua 第二层列表Holder
	 */
	class CenterOrderNoPayInsideItem {
		public TextView tv_center_order_no_pay_detail_seller_name;// 买家名称
		public CompleteListView item_fragment_center_order_no_pay_detail_outside;// 第二层的订单列表
		public LinearLayout ll_center_my_order_no_pay_contact_seller;// 联系卖家
	}

	/**
	 * @author Yihuihua 最里层的Holder
	 */
	class CenterOrderNoPayDetailInnerMostItem {
		public ImageView item_center_order_no_pay_detail_in_iv;// 商品图标
		public TextView item_center_order_no_pay_detail_in_name;// 商品title
		public TextView item_center_order_no_pay_detail_in_price;// 商品价格
		public TextView item_center_order_no_pay_detail_in_number;// 商品个数
	}

}
