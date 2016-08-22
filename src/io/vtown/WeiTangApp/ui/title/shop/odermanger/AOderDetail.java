package io.vtown.WeiTangApp.ui.title.shop.odermanger;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.shoporder.BDShopOrderDetail;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.DotView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.mynew.ANew;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
 * @version 创建时间：2016-5-26 下午1:16:00 订单详情页面
 */
public class AOderDetail extends ATitleBase {

	/**
	 * 地址信息
	 */
	private View address_message;
	/**
	 * 订单信息
	 */
	private View order_message;
	/**
	 * 商品列表
	 */
	private CompleteListView lv_common_goods;
	/**
	 * 点
	 */
	private DotView dot_view;
	/**
	 * 商品总数
	 */
	private TextView tv_good_count;
	/**
	 * 商品总价
	 */
	private TextView tv_total_price;
	/**
	 * 运费
	 */
	private TextView tv_post_price;
	/**
	 * 修改订单
	 */
	private TextView tv_modify_order;
	/**
	 * 发货
	 */
	private TextView tv_send_out_good;

	/**
	 * 收货人姓名
	 */
	private TextView commentview_add_name;
	/**
	 * 收货人电话
	 */
	private TextView commentview_add_phone;
	/**
	 * 收货人地址
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
	 * 付款时间
	 */
	private TextView tv_pay_time;
	private String seller_id;
	private String seller_order_sn;
	private int order_status;
	/**
	 * 修改订单和发货
	 */
	private LinearLayout ll_order_manage_modify_and_send_out;
	/**
	 * 待付款修改订单
	 */
	private TextView tv_order_manage_modify_daifu_order;
	/**
	 * 查看物流
	 */
	private TextView tv_order_manage_look_logistics1;
	/**
	 * 获取到数据时显示的布局
	 */
	private LinearLayout order_comment_detail_outlay;
	/**
	 * 未获取到数据时显示的布局
	 */
	private View order_comment_detail_nodata_lay;
	/**
	 * 同意不同意退款
	 */
	private LinearLayout ll_order_manage_agree_and_unagree_refund;
	/**
	 * 同意退款
	 */
	private TextView tv_agree_refund;
	/**
	 * 不同意退款
	 */
	private TextView tv_unagree_refund;
	/**
	 * 仲裁中
	 */
	private TextView tv_order_manage_arbitramenting;
	/**
	 * 退款中
	 */
	private TextView tv_order_manage_refunding;
	/**
	 * 付款时间布局----待付款时需要隐藏
	 */
	private LinearLayout ll_pay_time;
	/**
	 * 订单进行中
	 */
	private TextView tv_order_manage_optioning;
	/**
	 * 是否有权限发货
	 */
	private String is_send;
	/**
	 * 是否有权限修改地址和价格
	 */
	private String is_edit;
	
	private boolean isGetDetail = false;
	/**
	 * 使用余额和卡券
	 */
	private LinearLayout ll_order_manager_detail_used_balance_and_coupons;
	/**
	 * 使用余额
	 */
	private TextView tv_order_manager_detail_used_balance;
	/**
	 * 使用卡券
	 */
	private TextView tv_order_manager_detail_used_coupons;
	/**
	 * 使用余额和卡券分割线
	 */
	private View line_left_1;
	/**
	 * 联系买家
	 */
	private LinearLayout ll_order_manager_contact_buyer;
	/**
	 * 获取到的数据
	 */
	private BDShopOrderDetail bdComment;

	@Override
	protected void InItBaseView() {

		setContentView(R.layout.activity_order_comment_detail);
		EventBus.getDefault().register(this,"getEventMsg",BMessage.class);
		Intent intent = getIntent();
		seller_id = intent.getStringExtra("seller_id");
		seller_order_sn = intent.getStringExtra("seller_order_sn");
		order_status = intent.getIntExtra("order_status", 0);
		is_send = intent.getStringExtra("is_send");
		is_edit = intent.getStringExtra("is_edit");
		IView();
		IData(seller_id, seller_order_sn);

	}

	private void IData(String seller_id, String seller_order_sn) {
		PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.xlistview_header_hint_loading));
		SetTitleHttpDataLisenter(this);
		HashMap<String, String> map = new HashMap<String, String>();
		isGetDetail = true;
		map.put("seller_id", seller_id);
		map.put("seller_order_sn", seller_order_sn);
		FBGetHttpData(map, Constants.ORDER_DETAIL_MESSAGE, Method.GET, 0,
				LOAD_INITIALIZE);
	}
	
	/**
	 * 同意退款
	 * 
	 * @param seller_order_sn
	 * @param type
	 */
	private void AgreeTuiKun() {

		HashMap<String, String> map = new HashMap<String, String>();
		isGetDetail = false;
		map.put("seller_id", seller_id);
		map.put("seller_order_sn", seller_order_sn);
		FBGetHttpData(map, Constants.Agree_TuiKuan, Method.PUT, 1, LOAD_INITIALIZE);

	}

	/**
	 * 拒绝退款
	 * 
	 * @param seller_order_sn
	 * @param type
	 */
	private void UnAgreeTuiKun() {

		HashMap<String, String> map = new HashMap<String, String>();
		isGetDetail = false;
		map.put("seller_id", seller_id);
		map.put("seller_order_sn", seller_order_sn);
		FBGetHttpData(map, Constants.UnAgree_TuiKuan, Method.PUT, 2, LOAD_INITIALIZE);

	}

	private void IView() {
		
		order_comment_detail_outlay = (LinearLayout) findViewById(R.id.order_comment_detail_outlay);
		order_comment_detail_nodata_lay = findViewById(R.id.order_comment_detail_nodata_lay);
		IDataView(order_comment_detail_outlay, order_comment_detail_nodata_lay, NOVIEW_INITIALIZE);

		ll_order_manager_contact_buyer = (LinearLayout) findViewById(R.id.ll_order_manager_contact_buyer);
		
		address_message = findViewById(R.id.address_message);
		commentview_add_name = (TextView) address_message
				.findViewById(R.id.commentview_add_name);
		commentview_add_phone = (TextView) address_message
				.findViewById(R.id.commentview_add_phone);
		commentview_add_address = (TextView) address_message
				.findViewById(R.id.commentview_add_address);
		address_message.findViewById(R.id.commentview_add_iv).setVisibility(
				View.GONE);
		address_message.findViewById(R.id.iv_right_arrow).setVisibility(
				View.GONE);
		// 地址不可点击
		address_message.setEnabled(false);
		order_message = findViewById(R.id.order_message);
		tv_order_id = (TextView) order_message.findViewById(R.id.tv_order_id);
		
		ll_order_manager_detail_used_balance_and_coupons = (LinearLayout) findViewById(R.id.ll_order_manager_detail_used_balance_and_coupons);
		tv_order_manager_detail_used_balance = (TextView) findViewById(R.id.tv_order_manager_detail_used_balance);
		tv_order_manager_detail_used_coupons = (TextView) findViewById(R.id.tv_order_manager_detail_used_coupons);
		line_left_1 = findViewById(R.id.line_left_1);
		
		//让订单编号文本有可复制功能
	    StrUtils.SetTextViewCopy(tv_order_id);
		tv_ordering_time = (TextView) order_message
				.findViewById(R.id.tv_ordering_time);
		tv_pay_time = (TextView) order_message.findViewById(R.id.tv_pay_time);
		ll_pay_time = (LinearLayout) order_message.findViewById(R.id.ll_pay_time);
		lv_common_goods = (CompleteListView) findViewById(R.id.lv_common_goods);
		dot_view = (DotView) findViewById(R.id.dot_view);
		tv_good_count = (TextView) findViewById(R.id.tv_good_count);
		tv_total_price = (TextView) findViewById(R.id.tv_total_price);
		tv_post_price = (TextView) findViewById(R.id.tv_post_price);
		
		//修改订单和发货
		ll_order_manage_modify_and_send_out = (LinearLayout) findViewById(R.id.ll_order_manage_modify_and_send_out);
		tv_modify_order = (TextView) findViewById(R.id.tv_modify_order);
		tv_send_out_good = (TextView) findViewById(R.id.tv_send_out_good);
		
		//待付款修改订单
		tv_order_manage_modify_daifu_order = (TextView) findViewById(R.id.tv_order_manage_modify_daifu_order);
		tv_order_manage_optioning = (TextView) findViewById(R.id.tv_order_manage_optioning);
		
		//查看物流
		tv_order_manage_look_logistics1 = (TextView) findViewById(R.id.tv_order_manage_look_logistics1);
		//同意退款和拒绝退款
		
		ll_order_manage_agree_and_unagree_refund = (LinearLayout) findViewById(R.id.ll_order_manage_agree_and_unagree_refund);
		tv_agree_refund = (TextView) findViewById(R.id.tv_agree_refund);
		tv_unagree_refund = (TextView) findViewById(R.id.tv_unagree_refund);
		//仲裁中
		tv_order_manage_arbitramenting = (TextView) findViewById(R.id.tv_order_manage_arbitramenting);
		//退款已完成
		tv_order_manage_refunding = (TextView) findViewById(R.id.tv_order_manage_refunding);

		// 订单状态  10:代付款 20:已付款 待发货 30:已发货 待收货 40:退款中  50:仲裁处理中  100:已完成  110:已取消 60拒绝退款，70同意退款
		
		switch (order_status) {
		case 10:
			if("0".equals(is_edit)){
				tv_order_manage_optioning.setVisibility(View.VISIBLE);
			}else{
				tv_order_manage_modify_daifu_order.setVisibility(View.VISIBLE);
			}
			
			ll_pay_time.setVisibility(View.GONE);
			break;
		case 20:
			ll_order_manage_modify_and_send_out.setVisibility(View.VISIBLE);
			if("0".equals(is_edit)){//没有权限修改，判断是否有权限发货
				if("0".equals(is_send)){//没有权限发货时显示交易进行中
					tv_order_manage_optioning.setVisibility(View.VISIBLE);
					ll_order_manage_modify_and_send_out.setVisibility(View.GONE);
				}else{//有权限发货时显示发货
					tv_modify_order.setVisibility(View.GONE);
				}
			}else{//有权限修改
				if("0".equals(is_send)){//没有权限发货时显示修改
					tv_send_out_good.setVisibility(View.GONE);
					tv_modify_order.setTextColor(getResources().getColor(R.color.white));
					tv_modify_order.setBackground(getResources().getDrawable(R.drawable.select_fen_to_gray1));
				}else{//有权限发货时显示发货和修改
					tv_send_out_good.setVisibility(View.VISIBLE);
					
				}
				
			}
			
			
			break;
		case 30:
			tv_order_manage_look_logistics1.setVisibility(View.VISIBLE);
			break;
		case 40:
			ll_order_manage_agree_and_unagree_refund.setVisibility(View.VISIBLE);
			break;
		case 50:
			
			break;
			
		case 60:
			tv_order_manage_arbitramenting.setVisibility(View.VISIBLE);
			tv_order_manage_arbitramenting.setEnabled(false);
			
			break;
			
		case 70:
			tv_order_manage_refunding.setVisibility(View.VISIBLE);
			tv_order_manage_refunding.setEnabled(false);
			break;

		case 100:
			
			break;

		case 110:
			ll_pay_time.setVisibility(View.GONE);
			
			break;

		default:
			break;
		}

		tv_modify_order.setOnClickListener(this);
		tv_send_out_good.setOnClickListener(this);
		tv_order_manage_modify_daifu_order.setOnClickListener(this);
		tv_order_manage_look_logistics1.setOnClickListener(this);
		order_comment_detail_nodata_lay.setOnClickListener(this);
		tv_agree_refund.setOnClickListener(this);
		tv_unagree_refund.setOnClickListener(this);
		commentview_add_phone.setOnClickListener(this);
		ll_order_manager_contact_buyer.setOnClickListener(this);
		

	}

	private void IList(final List<BLComment> goods) {

		orderAdater adapter = new orderAdater(R.layout.item_order_manage_order_detail, goods);

		lv_common_goods.setAdapter(adapter);
		lv_common_goods.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String goods_id = goods.get(position).getGoods_id();
				Intent intent = new Intent(BaseContext, AGoodDetail.class);
				intent.putExtra("goodid", goods_id);
				PromptManager.SkipActivity(BaseActivity, intent);
			}
		});

	}

	@Override
	protected void InitTile() {
		SetTitleTxt("订单详情");
		ImageView right_right_iv = (ImageView) findViewById(R.id.right_right_iv);
		right_right_iv.setVisibility(View.VISIBLE);
		right_right_iv.setImageDrawable(getResources().getDrawable(R.drawable.new1));
		right_right_iv.setOnClickListener(this);
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		
		switch (Data.getHttpResultTage()) {
		case 0://获取详情数据
			if (StrUtils.isEmpty(Data.getHttpResultStr())) {
				return;
			}

			bdComment = new BDShopOrderDetail();
			try {
				bdComment = JSON.parseObject(Data.getHttpResultStr(),
						BDShopOrderDetail.class);
				
			} catch (Exception e) {
				DataError("解析错误", 1);
			}
			IDataView(order_comment_detail_outlay, order_comment_detail_nodata_lay, NOVIEW_RIGHT);
			RefreshView(bdComment);
			List<BLComment> goods = bdComment.getGoods();
			IList(goods);
			break;

		case 1://同意退款
			PromptManager.ShowMyToast(BaseContext, "订单退款中");
			this.finish();
			break;
			
		case 2://拒绝退款
			PromptManager.ShowMyToast(BaseContext, "订单进入仲裁中");
			this.finish();
			break;
		}
	}

	private void RefreshView(BDShopOrderDetail bdComment) {
		String refund = bdComment.getRefund();
		if("40".equals(bdComment.getOrder_status())){
			if("0".equals(refund)){
				ll_order_manage_agree_and_unagree_refund.setVisibility(View.GONE);
				
			}
		}
		
		//String name = getResources().getString(R.string.consignee_name_order);

		StrUtils.SetColorsTxt(BaseContext,commentview_add_name,R.color.app_gray,getString(R.string.tv_commentview_name),bdComment.getUsername());
		
		StrUtils.SetTxt(commentview_add_phone, bdComment.getMobile());
		StrUtils.SetTxt(commentview_add_address,bdComment.getProvince() + bdComment.getCity() + bdComment.getArea()
				+ bdComment.getAddress());
		int Order_status = Integer.parseInt(bdComment.getOrder_status());
		//如果是待付款订单和已取消订单显示为大单号，已付款显示小单号
		if(10 == Order_status || 110 == Order_status){
			StrUtils.SetTxt(tv_order_id, bdComment.getOrder_sn());
		}else{
			StrUtils.SetTxt(tv_order_id, bdComment.getSeller_order_sn());
		}
		
		StrUtils.SetTxt(tv_ordering_time,
				StrUtils.longtostr(Long.parseLong(bdComment.getCreate_time())));
		StrUtils.SetTxt(tv_pay_time,
				StrUtils.longtostr(Long.parseLong(bdComment.getPay_time())));
		
		
		StrUtils.SetColorsTxt(BaseContext, tv_order_manager_detail_used_balance, R.color.app_gray, "使用余额：", String.format("%1$s元", StrUtils.SetTextForMony(bdComment.getBalance_price())));
		StrUtils.SetColorsTxt(BaseContext, tv_order_manager_detail_used_coupons, R.color.app_gray, "使用卡券：", String.format("%1$s元",StrUtils.SetTextForMony(bdComment.getCoupons_price())));
		//显示使用余额和卡券，只有金额不为0的时候才显示
		if(0!=Integer.parseInt(bdComment.getBalance_price())||0 != Integer.parseInt(bdComment.getCoupons_price())){
			ll_order_manager_detail_used_balance_and_coupons.setVisibility(View.VISIBLE);
			if(0!=Integer.parseInt(bdComment.getBalance_price())){
				tv_order_manager_detail_used_balance.setVisibility(View.VISIBLE);
			}else{
				tv_order_manager_detail_used_balance.setVisibility(View.GONE);
				line_left_1.setVisibility(View.GONE);
			}
			if(0 != Integer.parseInt(bdComment.getCoupons_price())){
				tv_order_manager_detail_used_coupons.setVisibility(View.VISIBLE);
			}else{
				tv_order_manager_detail_used_coupons.setVisibility(View.GONE);
				line_left_1.setVisibility(View.GONE);
			}
		}else{
			ll_order_manager_detail_used_balance_and_coupons.setVisibility(View.GONE);
		}
		
		
		String count = getResources().getString(R.string.goods_count);
		StrUtils.SetTxt(tv_good_count,
				String.format(count, bdComment.getGoods().size()));
		float price = Float.parseFloat(bdComment.getGoods_price())+Float.parseFloat(bdComment.getPostage());
		StrUtils.SetTxt(tv_total_price,
				String.format("%1$s元", StrUtils.SetTextForMony(price+"")));
		float postageF = Float.parseFloat(bdComment.getPostage());
		String postage = String.format("(含运费%1$s元)", StrUtils.SetTextForMony(postageF+""));
		StrUtils.SetTxt(tv_post_price, postageF == 0.0f ?"(免邮费)":postage);

	}

	@Override
	protected void DataError(String error, int LoadType) {
		PromptManager.ShowCustomToast(BaseContext, error);
		if(LOAD_INITIALIZE == LoadType && isGetDetail){
			IDataView(order_comment_detail_outlay, order_comment_detail_nodata_lay, NOVIEW_ERROR);
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
		Intent intent = null;
		if (StrUtils.isEmpty(seller_id) && StrUtils.isEmpty(seller_order_sn)) {
			return;
		}
		
		switch (V.getId()) {

		case R.id.tv_modify_order:// 修改订单
			intent = new Intent(BaseActivity, ADaifaOrderModify.class);
			intent.putExtra(ADaifaOrderModify.Tag,
					ADaifaOrderModify.Tage_From_Modify);
			intent.putExtra("seller_id", seller_id);
			intent.putExtra("seller_order_sn", seller_order_sn);
			PromptManager.SkipActivity(BaseActivity, intent);

			break;

		case R.id.tv_send_out_good:// 发货
			intent = new Intent(BaseActivity, ADaifaOrderModify.class);
			intent.putExtra(ADaifaOrderModify.Tag,
					ADaifaOrderModify.Tage_From_Send);
			intent.putExtra("seller_id", seller_id);
			intent.putExtra("seller_order_sn", seller_order_sn);
			PromptManager.SkipActivity(BaseActivity, intent);
			break;
			
		case R.id.tv_order_manage_modify_daifu_order://修改订单
			intent = new Intent(BaseActivity,ADaifuOrderModify.class);
			intent.putExtra("seller_id", seller_id);
			intent.putExtra("seller_order_sn", seller_order_sn);
			PromptManager.SkipActivity(BaseActivity, intent);
			break;
			
		case R.id.tv_order_manage_look_logistics1://查看物流
			intent = new Intent(BaseActivity, ADaifaOrderModify.class);
			intent.putExtra(ADaifaOrderModify.Tag,
					ADaifaOrderModify.Tage_From_Look_Express);
			intent.putExtra("seller_id", seller_id);
			intent.putExtra("seller_order_sn", seller_order_sn);
			PromptManager.SkipActivity(BaseActivity, intent);
			break;
			
		case R.id.order_comment_detail_nodata_lay://重新加载数据
			IData(seller_id, seller_order_sn);
			break;
			
		case R.id.right_right_iv://标题栏消息按钮
			if(CheckNet(BaseContext))return;
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					ANew.class));
			break;
			
		case R.id.tv_agree_refund://同意退款
			if(CheckNet(BaseContext))return;
			AgreeTuiKun();
			break;
			
		case R.id.tv_unagree_refund://拒绝退款
			if(CheckNet(BaseContext))return;
			UnAgreeTuiKun();
			break;
			
		case R.id.commentview_add_phone://拨号联系买家
			CallBuyer();
			break;
			
		case R.id.ll_order_manager_contact_buyer://联系买家
			BComment mBComment = new BComment(bdComment.getMember_seller_id(),
					"");
			PromptManager.SkipActivity(BaseActivity,
					new Intent(BaseActivity, AShopDetail.class)
							.putExtra(BaseKey_Bean, mBComment));
			break;
		}
		
	}
	
	/**
	 * 拨号
	 */
	private void CallBuyer(){
		String phoneNumber = commentview_add_phone.getText().toString().trim();
		if(!StrUtils.isEmpty(phoneNumber)){
			Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri
					.parse("tel:" + phoneNumber));

			startActivity(intentPhone);
		}
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	class orderAdater extends BaseAdapter {

		/**
		 * 填充器
		 */
		private LayoutInflater inflater;
		/**
		 * 资源id
		 */
		private int ResourceId;

		private List<BLComment> data = new ArrayList<BLComment>();

		public orderAdater(int ResouceId, List<BLComment> data) {
			super();
			this.inflater = LayoutInflater.from(BaseContext);
			ResourceId = ResouceId;
			this.data = data;
		}

		@Override
		public int getCount() {

			return data.size();
		}

		@Override
		public Object getItem(int arg0) {

			return data.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {

			OrderItem item = null;
			if (arg1 == null) {
				arg1 = inflater.inflate(ResourceId, null);
				item = new OrderItem();
				item.iv_order_detail_good_icon = (ImageView) arg1
						.findViewById(R.id.iv_order_detail_good_icon);
				item.iv_modify_order_goods_type = (ImageView) arg1
						.findViewById(R.id.iv_modify_order_goods_type);
				item.tv_order_detail_good_title = (TextView) arg1
						.findViewById(R.id.tv_order_detail_good_title);
				item.tv_good_content = (TextView) arg1
						.findViewById(R.id.tv_good_content);
				item.tv_content_value = (TextView) arg1
						.findViewById(R.id.tv_content_value);
				item.tv_order_detail_good_price = (TextView) arg1
						.findViewById(R.id.tv_order_detail_good_price);
				item.tv_order_detail_good_count = (TextView) arg1
						.findViewById(R.id.tv_order_detail_good_count);

				ImageLoaderUtil.Load2(data.get(arg0).getGoods_cover(),
						item.iv_order_detail_good_icon, R.drawable.error_iv2);

				arg1.setTag(item);
			} else {
				item = (OrderItem) arg1.getTag();
			}

			String goods_type = data.get(arg0).getGoods_type();

			if ("0".equals(goods_type)) {
				item.iv_modify_order_goods_type.setVisibility(View.GONE);
			} else {
				item.iv_modify_order_goods_type.setVisibility(View.VISIBLE);
			}

			StrUtils.SetTxt(item.tv_order_detail_good_title, data.get(arg0)
					.getGoods_name());
			StrUtils.SetTxt(item.tv_content_value, data.get(arg0)
					.getGoods_standard());
			String goods_price = String.format("￥%1$s", StrUtils.SetTextForMony(data.get(arg0)
					.getGoods_price()));
			StrUtils.SetTxt(item.tv_order_detail_good_price, goods_price);
			String goods_number = String.format("x%1$s", data.get(arg0)
					.getGoods_number());
			StrUtils.SetTxt(item.tv_order_detail_good_count, goods_number);

			return arg1;
		}

		class OrderItem {
			ImageView iv_order_detail_good_icon, iv_modify_order_goods_type;
			TextView tv_order_detail_good_title, tv_good_content,
					tv_content_value, tv_order_detail_good_price,
					tv_order_detail_good_count;

		}

	}
	
	private void getEventMsg(BMessage event){
		int messageType = event.getMessageType();
		if(BMessage.ORDER_DETAIL_UPDATE == messageType){
			IData(seller_id, seller_order_sn);
		}
	}
	
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		try {
			EventBus.getDefault().unregister(this);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
