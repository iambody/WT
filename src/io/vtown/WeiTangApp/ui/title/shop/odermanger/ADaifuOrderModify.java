package io.vtown.WeiTangApp.ui.title.shop.odermanger;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.BAddress;
import io.vtown.WeiTangApp.bean.bcomment.easy.shoporder.BDShopOrderDetail;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.event.receiver.NewMessageBroadcastReceiver;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.mynew.ANew;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-27 上午11:11:12 订单管理-->待付款-->修改订单页面
 */
public class ADaifuOrderModify extends ATitleBase {

	/**
	 * 商品列表
	 */
	private CompleteListView lv_daifu_common_goods;
	/**
	 * 商品数量
	 */
	private TextView tv_daifu_good_count;
	/**
	 * 商品总价
	 */
	private TextView tv_daifu_total_price;
	/**
	 * 运费
	 */
	private TextView tv_daifu_post_price;
	/**
	 * 订单编号
	 */
	private TextView tv_daifu_order_id;
	/**
	 * 下单时间
	 */
	private TextView tv_daifu_create_time;
	/**
	 * 收货人
	 */
	private TextView tv_daifu_consignee;
	/**
	 * 联系方式
	 */
	private TextView tv_daifu_contact;

	/**
	 * AP
	 */
	private myAdapter myAdapter;

	/**
	 * 确认订单按钮
	 */
	private Button btn_daifu_confirm_order;
	private List<MyModifyItem> dItems;
	private BDShopOrderDetail data;

	/**
	 * 用于存放修改之前的价格
	 */
	private List<String> prices = new ArrayList<String>();

	private String seller_id;
	private String seller_order_sn;

	/**
	 * 获取到数据之后的布局
	 */
	private LinearLayout daifu_order_modify_outlay;
	/**
	 * 获取数据失败时显示的布局
	 */
	private View daifu_order_modify_nodata_lay;

	private int visiableLayoutHeight = 0;

	private int pricePosition = -1;

	/**
	 * 地址信息
	 */
	private View daifu_address_message;
	/**
	 * 收货人姓名
	 */
	private TextView commentview_add_name;
	/**
	 * 收货人手机号
	 */
	private TextView commentview_add_phone;
	/**
	 * 收货人地址
	 */
	private TextView commentview_add_address;
	/**
	 * 收货人地址信息
	 */
	private View daifa_address_message;

	/**
	 * 地址图标
	 */
	private ImageView commentview_add_iv;

	/**
	 * 是否修改了地址
	 */
	private boolean isModifyAddress = false;

	/**
	 * 是否修改了价格
	 */
	private boolean isModifyPrice = false;

	/**
	 * 地址内容
	 */
	private Bundle bundle;

	/**
	 * 当此标志是获取详情数据时，失败时需要显示失败布局
	 */
	private boolean isGetDetail = false;
	
	/**
	 * 使用卡券
	 */
	private LinearLayout ll_order_manager_daifu_detail_used_coupons;
	/**
	 *使用卡券
	 */
	private TextView tv_order_manager_daifu_detail_used_coupons;
	private float total_price_old;
	private int total_price_set;
	private BAddress address_info;


	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_daifu_order_modity);
		// 注册事件
		EventBus.getDefault().register(this, "OnGetMessage", BMessage.class);
		Intent intent = getIntent();
		seller_id = intent.getStringExtra("seller_id");
		seller_order_sn = intent.getStringExtra("seller_order_sn");
		IView();
		IData(seller_id, seller_order_sn);
	}

	private void IView() {
		daifu_order_modify_outlay = (LinearLayout) findViewById(R.id.daifu_order_modify_outlay);
		daifu_order_modify_nodata_lay = findViewById(R.id.daifu_order_modify_nodata_lay);
		IDataView(daifu_order_modify_outlay, daifu_order_modify_nodata_lay,
				NOVIEW_INITIALIZE);
		daifu_address_message = findViewById(R.id.daifu_address_message);
		commentview_add_name = (TextView) daifu_address_message
				.findViewById(R.id.commentview_add_name);
		commentview_add_phone = (TextView) daifu_address_message
				.findViewById(R.id.commentview_add_phone);
		commentview_add_address = (TextView) daifu_address_message
				.findViewById(R.id.commentview_add_address);
		commentview_add_iv = (ImageView) daifu_address_message
				.findViewById(R.id.commentview_add_iv);
		commentview_add_iv.setVisibility(View.VISIBLE);
		ll_order_manager_daifu_detail_used_coupons = (LinearLayout) findViewById(R.id.ll_order_manager_daifu_detail_used_coupons);
		tv_order_manager_daifu_detail_used_coupons = (TextView) findViewById(R.id.tv_order_manager_daifu_detail_used_coupons);
		lv_daifu_common_goods = (CompleteListView) findViewById(R.id.lv_daifu_common_goods);

		tv_daifu_good_count = (TextView) findViewById(R.id.tv_daifu_good_count);

		tv_daifu_total_price = (TextView) findViewById(R.id.tv_daifu_total_price);

		tv_daifu_post_price = (TextView) findViewById(R.id.tv_daifu_post_price);

		tv_daifu_order_id = (TextView) findViewById(R.id.tv_daifu_order_id);
		// 让订单编号文本有可复制功能
		StrUtils.SetTextViewCopy(tv_daifu_order_id);

		tv_daifu_create_time = (TextView) findViewById(R.id.tv_daifu_create_time);

		tv_daifu_consignee = (TextView) findViewById(R.id.tv_daifu_consignee);

		tv_daifu_contact = (TextView) findViewById(R.id.tv_daifu_contact);

		btn_daifu_confirm_order = (Button) findViewById(R.id.btn_daifu_confirm_order);
		btn_daifu_confirm_order.setOnClickListener(this);
		dItems = new ArrayList<MyModifyItem>();
		dItems.add(new MyModifyItem());
		dItems.add(new MyModifyItem());
		daifu_order_modify_nodata_lay.setOnClickListener(this);
		daifu_address_message.setOnClickListener(this);
	}

	/**
	 * 刷新控件
	 * 
	 * @param data2
	 */
	private void RefreshView(BDShopOrderDetail data2) {

		StrUtils.SetTxt(commentview_add_name, data2.getUsername());
		StrUtils.SetTxt(commentview_add_phone, data2.getMobile());
		StrUtils.SetTxt(
				commentview_add_address,
				data2.getProvince() + data2.getCity() + data2.getArea()
						+ data2.getAddress());

		address_info = new BAddress(data2.getUsername(), data2.getMobile(), data2.getProvince(), data2.getCity(), data2.getArea(), data2.getAddress());
		StrUtils.SetColorsTxt(BaseContext, tv_order_manager_daifu_detail_used_coupons, R.color.app_gray, "使用卡券：", String.format("%1$s元",StrUtils.SetTextForMony(data2.getCoupons_price())));
		//显示使用余额和卡券，只有金额不为0的时候才显示
		if(0 != Integer.parseInt(data2.getCoupons_price())){
			ll_order_manager_daifu_detail_used_coupons.setVisibility(View.VISIBLE);
			
		}else{
			ll_order_manager_daifu_detail_used_coupons.setVisibility(View.GONE);
		}
		StrUtils.SetTxt(tv_daifu_order_id, data2.getOrder_sn());
		StrUtils.SetTxt(tv_daifu_create_time,
				StrUtils.longtostr(Long.parseLong(data2.getCreate_time())));
		StrUtils.SetTxt(tv_daifu_consignee, data2.getUsername());
		StrUtils.SetTxt(tv_daifu_contact, data2.getMobile());

		String count = String.format("共%1$s件商品", data2.getGoods().size() + "");
		StrUtils.SetTxt(tv_daifu_good_count, count);
		total_price_set = Integer.parseInt(data2.getGoods_price())
				+ Integer.parseInt(data2.getPostage());
		total_price_old = total_price_set;
//		StrUtils.SetTxt(
//				tv_daifu_total_price,
//				String.format("%1$s元",
//						StrUtils.SetTextForMony(total_price + "")));

		StrUtils.SetMoneyFormat(BaseContext,tv_daifu_total_price, total_price_set + "",17);
		float postageF = Float.parseFloat(data2.getPostage());
		StrUtils.SetTxt(
				tv_daifu_post_price,
				postageF == 0.0f ? "(免邮费)" : String.format("(含运费%1$s元)",
						StrUtils.SetTextForMony(postageF + "")));
		myAdapter = new myAdapter(data2.getGoods());
		lv_daifu_common_goods.setAdapter(myAdapter);
		// getListViewHeight();
	}

	/**
	 * 获取订单详情
	 * 
	 * @param seller_id
	 * @param seller_order_sn
	 */
	private void IData(String seller_id, String seller_order_sn) {
		PromptManager.showtextLoading(BaseContext,
				getResources()
						.getString(R.string.xlistview_header_hint_loading));
		SetTitleHttpDataLisenter(this);
		isGetDetail = true;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", seller_id);
		map.put("seller_order_sn", seller_order_sn);
		FBGetHttpData(map, Constants.Order_Detail, Method.GET, 0,
				LOAD_INITIALIZE);
	}

	/**
	 * 修改总价网络请求
	 */
	private void ModifyTotalPrice() {

		String total = getTotalAttrsListToString();
		HashMap<String, String> map = new HashMap<String, String>();
		isGetDetail = false;
		map.put("seller_order_sn", seller_order_sn);
		map.put("total", total);
		map.put("seller_id", seller_id);
		FBGetHttpData(map, Constants.Modify_Total_Price, Method.PUT, 1,
				LOAD_INITIALIZE);
	}

	/**
	 * 修改地址请求
	 */
	private void ModifyAddress(Bundle bundle) {
		SetTitleHttpDataLisenter(this);
		HashMap<String, String> map = new HashMap<String, String>();
		isGetDetail = false;
		map.put("seller_order_sn", seller_order_sn);
		map.put("seller_id", seller_id);
		map.put("username", bundle.getString("name"));
		map.put("mobile", bundle.getString("mobile"));
		map.put("province", bundle.getString("province"));
		map.put("city", bundle.getString("city"));
		map.put("area", bundle.getString("county"));
		map.put("street_address", bundle.getString("address"));
		map.put("address", bundle.getString("address"));
		map.put("postcode", "");//bundle.getString("postcode")
		FBGetHttpData(map, Constants.Order_Manage_Modify_Address, Method.PUT,
				1, LOAD_INITIALIZE);

	}

	public List<MyModifyItem> GetItems() {
		return dItems;

	}

	private List<String> getTotalAttrsList() {
		List<String> total_strs = new ArrayList<String>();
		for (int i = 0; i < data.getGoods().size(); i++) {
			// BLComment bl = (BLComment) myAdapter.getItem(i);
			List<BLComment> goods = data.getGoods();
			BLComment bl = goods.get(i);
			String goods_attr_id = bl.getGoods_attr_id();
			String goods_price = bl.getGoods_money();
			total_strs.add(goods_attr_id + "-" + goods_price);
		}
		return total_strs;
	}

	/**
	 * 获取修改总价需要的参数total
	 * 
	 * @return
	 */
	private String getTotalAttrsListToString() {
		List<String> totalAttrsList = getTotalAttrsList();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < totalAttrsList.size(); i++) {

			if (i == totalAttrsList.size() - 1) {
				buf.append(totalAttrsList.get(i));
			} else {
				buf.append(totalAttrsList.get(i) + ",");
			}
		}
		return buf.toString();
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.faifu_order_modity));
		ImageView right_right_iv = (ImageView) findViewById(R.id.right_right_iv);
		right_right_iv.setVisibility(View.VISIBLE);
		right_right_iv.setImageDrawable(getResources().getDrawable(
				R.drawable.new1));
		right_right_iv.setOnClickListener(this);
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {

		switch (Data.getHttpResultTage()) {
		case 0:
			if (StrUtils.isEmpty(Data.getHttpResultStr())) {
				return;
			}
			data = new BDShopOrderDetail();
			try {
				data = JSON.parseObject(Data.getHttpResultStr(),
						BDShopOrderDetail.class);
				SavaModifyBeforePrice(data.getGoods());
			} catch (Exception e) {
				DataError("解析失败", 1);
			}
			IDataView(daifu_order_modify_outlay, daifu_order_modify_nodata_lay,
					NOVIEW_RIGHT);
			RefreshView(data);
			break;

		case 1:
			PromptManager.ShowMyToast(BaseContext, "订单修改成功");
			EventBus.getDefault().post(
					new BMessage(BMessage.Tage_Order_Manage_Updata));
			this.finish();
			break;

		default:
			break;
		}

	}

	/**
	 * 保存所有的修改之前的价格
	 * 
	 * @param goods
	 */
	private void SavaModifyBeforePrice(List<BLComment> goods) {
		for (BLComment blComment : goods) {
			prices.add(blComment.getGoods_money());
		}
	}

	@Override
	protected void DataError(String error, int LoadType) {
		PromptManager.ShowMyToast(BaseContext, error);
		if (LOAD_INITIALIZE == LoadType && isGetDetail) {
			IDataView(daifu_order_modify_outlay, daifu_order_modify_nodata_lay,
					NOVIEW_ERROR);
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
		case R.id.btn_daifu_confirm_order:
			if (CheckNet(BaseContext))
				return;
			if (isModifyAddress) {// 改了地址就会调用改地址接口
				ModifyAddress(bundle);
				EventBus.getDefault().post(BMessage.ORDER_DETAIL_UPDATE);
			}
			if (isModifyPrice) {// 改了价格就会调用改价接口
				ModifyTotalPrice();
				EventBus.getDefault().post(BMessage.ORDER_DETAIL_UPDATE);
			}
			if (!isModifyAddress && !isModifyPrice) {// 都没有改动时，点击按钮直接finish
				this.finish();
			}

			break;

		case R.id.daifu_order_modify_nodata_lay:// 重新获取数据
			if (CheckNet(BaseContext))
				return;
			IData(seller_id, seller_order_sn);
			break;

		case R.id.right_right_iv:// 标题栏消息按钮
			if (CheckNet(BaseContext))
				return;
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					ANew.class));
			break;
		case R.id.daifu_address_message:// 地址
			Intent intent = new Intent(BaseContext,
					AModifyDeliveryAddress.class);
			// intent.putExtra("seller_order_sn", seller_order_sn);
			// intent.putExtra("seller_id", seller_id);
			// PromptManager.SkipActivity(BaseActivity, intent);
			if (address_info != null) {
				intent.putExtra("address_info", address_info);
				PromptManager.SkipResultActivity(BaseActivity, intent, 100);
			}

			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (100 == requestCode && RESULT_OK == resultCode) {

			bundle = data.getBundleExtra("AddressInfo");
			if (bundle != null) {
				isModifyAddress = true;

				StrUtils.SetTxt(commentview_add_name, bundle.getString("name"));

				StrUtils.SetTxt(commentview_add_phone,
						bundle.getString("mobile"));
				StrUtils.SetTxt(
						commentview_add_address,
						bundle.getString("province") + bundle.getString("city")
								+ bundle.getString("county")
								+ bundle.getString("address"));

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

	class myAdapter extends BaseAdapter {

		private List<Boolean> booleans = new ArrayList<Boolean>();

		private List<BLComment> datas = new ArrayList<BLComment>();

		public myAdapter(List<BLComment> datas) {
			super();
			this.datas = datas;
		}

		@Override
		public int getCount() {

			return datas.size();
		}

		public void FrashView(List<BLComment> dass) {
			this.datas = dass;
			this.notifyDataSetChanged();
		}

		@Override
		public Object getItem(int arg0) {

			return datas.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			MyModifyItem item = null;

			if (arg1 == null) {
				arg1 = View.inflate(BaseContext,
						R.layout.item_daifu_goods_list, null);
				// item = GetItems().get(arg0);
				item = new MyModifyItem();
				item.iv_daifu_goods_type = (ImageView) arg1
						.findViewById(R.id.iv_daifu_goods_type);
				item.iv_daifu_good_icon = (ImageView) arg1
						.findViewById(R.id.iv_daifu_good_icon);
				item.tv_daifu_good_title = (TextView) arg1
						.findViewById(R.id.tv_daifu_good_title);
				item.tv_daifu_content_value = (TextView) arg1
						.findViewById(R.id.tv_daifu_content_value);
				item.tv_daifu_good_price = (TextView) arg1
						.findViewById(R.id.tv_daifu_good_price);
				item.tv_daifu_good_count = (TextView) arg1
						.findViewById(R.id.tv_daifu_good_count);
				item.tv_daifu_edit = (TextView) arg1
						.findViewById(R.id.tv_daifu_edit);

				item.tv_daifu_good_total_money = (TextView) arg1
						.findViewById(R.id.tv_daifu_good_total_money);
				ImageLoaderUtil.Load2(datas.get(arg0).getGoods_cover(),
						item.iv_daifu_good_icon, R.drawable.error_iv2);

				arg1.setTag(item);

			} else {
				item = (MyModifyItem) arg1.getTag();
			}

			item.tv_daifu_edit.setVisibility(View.VISIBLE);

			StrUtils.SetTxt(item.tv_daifu_good_title, datas.get(arg0)
					.getGoods_name());
			String goods_type = datas.get(arg0).getGoods_type();
			if ("0".equals(goods_type)) {
				item.iv_daifu_goods_type.setVisibility(View.GONE);
				item.tv_daifu_edit.setVisibility(View.VISIBLE);
			} else {
				item.iv_daifu_goods_type.setVisibility(View.VISIBLE);
				item.tv_daifu_edit.setVisibility(View.GONE);
			}
			StrUtils.SetTxt(item.tv_daifu_content_value, datas.get(arg0)
					.getGoods_standard());
			String goods_price = String.format("￥%1$s",
					StrUtils.SetTextForMony(datas.get(arg0).getGoods_price()));
			StrUtils.SetTxt(item.tv_daifu_good_price, goods_price);
//			if(isModifyPrice){
//				item.tv_daifu_good_price.setVisibility(View.VISIBLE);
//				item.tv_daifu_good_price.setTextColor(getResources().getColor(R.color.app_gray));
//				item.tv_daifu_good_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//			}else{
//				item.tv_daifu_good_price.setVisibility(View.GONE);
//			}
			String goods_number = String.format("x%1$s", datas.get(arg0)
					.getGoods_number());
			StrUtils.SetTxt(item.tv_daifu_good_count, goods_number);

			StrUtils.SetTxt(
					item.tv_daifu_good_total_money,
					String.format("￥%1$s",
							StrUtils.SetTextForMony(prices.get(arg0))));

			BLComment blComment = datas.get(arg0);
			blComment.setGoods_money(prices.get(arg0));
			onClickEvent(item, blComment, arg0);

			return arg1;
		}

		void onClickEvent(MyModifyItem item11, final BLComment goodInfo,
				final int position) {
			final MyModifyItem datiemItem = item11;

			datiemItem.tv_daifu_edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					pricePosition = position;
					Intent intent = new Intent(BaseContext,
							AEditGoodPrice.class);
					intent.putExtra("goodInfo", goodInfo);
					PromptManager.SkipActivity(BaseActivity, intent);

				}
			});

		}

	}

	public class MyModifyItem {
		ImageView iv_daifu_good_icon, iv_daifu_goods_type;
		TextView tv_daifu_good_title, tv_daifu_content_value,
				tv_daifu_good_price, tv_daifu_good_count, tv_daifu_edit,
				tv_faifu_order_modity_before_count,
				tv_daifu_total_price_modify_before,
				tv_faifu_order_modity_after_count,
				tv_faifu_order_modity_after_postage,
				tv_faifu_order_modity_before_postage,
				tv_daifu_good_total_money, tv_daifu_total_price_modify_after;
		Button btn_daifu_finish;
		EditText et_modify_good_price;

		LinearLayout ll_daifu_modify;
		boolean IsExpends = false;

	}
	
	/**
	 * 接收事件
	 * 
	 * @param event
	 */

	public void OnGetMessage(BMessage event) {
		float total_price = 0;
		float good_price = 0;

		if (event.getMessageType() == 251) {
			good_price = event.getTageEditGoodPrice();
			StrUtils.SetTextForMony(good_price + "");
			for (int i = 0; i < prices.size(); i++) {
				if (i == pricePosition) {
					String price_before = prices.get(pricePosition);
					float price_before_F = Float.parseFloat(price_before);
					total_price = good_price - price_before_F;

					prices.remove(pricePosition);
					prices.add(pricePosition, good_price + "");
					myAdapter.notifyDataSetChanged();
					isModifyPrice = true;
				}
			}
			total_price_old += total_price;
			StrUtils.SetMoneyFormat(BaseContext,tv_daifu_total_price,total_price_old + Integer.parseInt(data.getPostage())+ "",17);
		}

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		// 注销事件
		EventBus.getDefault().unregister(this, BMessage.class);
	}

}
