package io.vtown.WeiTangApp.ui.comment.order;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.adapter.BasAdapter;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.purchase.BLShopPurchase;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.comment.view.listview.LListView.IXListViewListener;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.fragment.FShopPurchase;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.account.ACashierDesk;
import io.vtown.WeiTangApp.ui.title.center.myorder.AApplyTuikuan;
import io.vtown.WeiTangApp.ui.title.shop.purchase.APurchaseDetail;
import io.vtown.WeiTangApp.ui.title.shop.purchase.APurchaseNoPayDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-8-19 上午10:14:17
 * 
 */
public class AShopPurchaseOrder extends ATitleBase implements
		IXListViewListener, OnItemClickListener {

	/**
	 * 传递tage的key
	 */
	public static final String Key_TageStr = "FShopsPurchasekey";
	/**
	 * 标记 分别是 全部，代付款，已付款，待收货，退货,关闭 订单状态 10:代付款 20:已付款 待发货 30:已发货 待收货 40:退款中
	 * 50:仲裁处理中 100:已完成 110:已取消 60拒绝退款，70同意退款
	 */
	public static final int PAll = 0;
	public static final int PDaiFu = 10;
	public static final int PYiFu = 20;
	public static final int PDaiShou = 30;
	public static final int PTuiKuan = 40;
	public static final int PZhongCai = 60;
	public static final int PClose = 100;
	public static final int PAgreeRefund = 70;
	public static final int PIsCancel = 110;

	// 订单状态 10:代付款 20:已付款 待发货 30:已发货 待收货 40:退款中 50:仲裁处理中 100:已完成 110:已取消

	/**
	 * 获取到的key
	 */
	private static int Ket_Tage = PAll;
	/**
	 * Ls
	 */
	private LListView fragment_shop_purchase_ls;

	/**
	 * 外层的Ap
	 */
	private LsAp lsAp;

	/**
	 * 用户信息
	 */
	private BUser user_Get;

	/**
	 * last_id
	 */
	private String last_id = "";

	/**
	 * Dialog
	 */
	private CustomDialog dialog;

	// 未获取数据时候的view
	private View fragent_purchase_nodata_lay;
	/**
	 * 未付款AP
	 */
	private PurchaseOrderNoPayAdapter purchaseOrderNoPayAdapter;
	/**
	 * 列表List
	 */
	private List<BLShopPurchase> list_datas = null;

	/** 弹出popup的按钮 */
	private PopupWindow mPopupWindow;
	private TextView tv_center_order_all;
	private TextView tv_center_order_no_pay;
	private TextView tv_center_order_paid;
	private TextView tv_center_order_no_take;
	private TextView tv_center_order_refund_and_arbitrament;
	private TextView tv_center_order_over;

	private Drawable mDrawable;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_shop_purchase);
		SetTitleHttpDataLisenter(this);

		IBase();

		// 注册事件
		EventBus.getDefault().register(this, "OnGetMessage", BMessage.class);
	}

	/**
	 * 关闭窗口
	 */
	private void closePopupWindow() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}

	/**
	 * 显示PopupWindow
	 * 
	 * @param view
	 */
	private void showPopupWindow(View view) {

		if (mPopupWindow == null) {
			View contentView = View.inflate(AShopPurchaseOrder.this,
					R.layout.pop_center_order, null);
			LinearLayout ll_center_order_all = (LinearLayout) contentView
					.findViewById(R.id.ll_center_order_all);
			LinearLayout ll_center_order_no_pay = (LinearLayout) contentView
					.findViewById(R.id.ll_center_order_no_pay);
			LinearLayout ll_center_order_paid = (LinearLayout) contentView
					.findViewById(R.id.ll_center_order_paid);

			LinearLayout ll_center_order_no_take = (LinearLayout) contentView
					.findViewById(R.id.ll_center_order_no_take);
			LinearLayout ll_center_order_refund_and_arbitrament = (LinearLayout) contentView
					.findViewById(R.id.ll_center_order_refund_and_arbitrament);
			LinearLayout ll_center_order_over = (LinearLayout) contentView
					.findViewById(R.id.ll_center_order_over);

			tv_center_order_all = (TextView) contentView
					.findViewById(R.id.tv_center_order_all);
			tv_center_order_no_pay = (TextView) contentView
					.findViewById(R.id.tv_center_order_no_pay);
			tv_center_order_paid = (TextView) contentView
					.findViewById(R.id.tv_center_order_paid);
			tv_center_order_no_take = (TextView) contentView
					.findViewById(R.id.tv_center_order_no_take);
			tv_center_order_refund_and_arbitrament = (TextView) contentView
					.findViewById(R.id.tv_center_order_refund_and_arbitrament);
			tv_center_order_over = (TextView) contentView
					.findViewById(R.id.tv_center_order_over);

			View gray_view = contentView.findViewById(R.id.gray_view);
			changeTextColor(tv_center_order_all);
			ll_center_order_all.setOnClickListener(this);
			ll_center_order_no_pay.setOnClickListener(this);
			ll_center_order_paid.setOnClickListener(this);
			ll_center_order_no_take.setOnClickListener(this);
			ll_center_order_refund_and_arbitrament.setOnClickListener(this);
			ll_center_order_over.setOnClickListener(this);

			gray_view.setOnClickListener(this);

			mPopupWindow = new PopupWindow(contentView,
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
			mPopupWindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					mDrawable = getResources().getDrawable(
							R.drawable.arrow_down);
					mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
							mDrawable.getMinimumHeight());
					title.setCompoundDrawables(null, null, mDrawable, null);
				}
			});

			mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x0ffffff));
		}

		mDrawable = getResources().getDrawable(R.drawable.arrow_up);
		mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
				mDrawable.getMinimumHeight());
		title.setCompoundDrawables(null, null, mDrawable, null);

		// 设置好参数之后再show
		mPopupWindow.showAsDropDown(view, 0, 29);

	}

	private void changeTextColor(TextView tv) {
		tv_center_order_all.setTextColor(getResources().getColor(R.color.grey));
		tv_center_order_no_pay.setTextColor(getResources().getColor(
				R.color.grey));
		tv_center_order_paid
				.setTextColor(getResources().getColor(R.color.grey));
		tv_center_order_no_take.setTextColor(getResources().getColor(
				R.color.grey));
		tv_center_order_refund_and_arbitrament.setTextColor(getResources()
				.getColor(R.color.grey));
		tv_center_order_over
				.setTextColor(getResources().getColor(R.color.grey));
		tv.setTextColor(getResources().getColor(R.color.app_fen));
	}

	@Override
	protected void InitTile() {
		SetTitleTxt("全部");
		mDrawable = getResources().getDrawable(R.drawable.arrow_down);
		mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(),
				mDrawable.getMinimumHeight());
		title.setCompoundDrawables(null, null, mDrawable, null);
		title.setOnClickListener(this);
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		switch (Data.getHttpResultTage()) {
		case 0:
			if (StrUtils.isEmpty(Data.getHttpResultStr())) {

				if (LOAD_INITIALIZE == Data.getHttpLoadType()) {
					PromptManager.ShowCustomToast(BaseContext, "暂无订单");
					fragent_purchase_nodata_lay.setVisibility(View.VISIBLE);
					fragment_shop_purchase_ls.setVisibility(View.GONE);
					if (10 == Ket_Tage) {
						purchaseOrderNoPayAdapter
								.RefreshData(new ArrayList<BLShopPurchase>());
					} else {
						lsAp.RefreshData(new ArrayList<BLShopPurchase>());
						if (0 == Ket_Tage) {
							CacheUtil.Shop_Purchase_List_Save(getApplicationContext(),
									Data.getHttpResultStr());
						}
					}
				}

				if (LOAD_LOADMOREING == Data.getHttpLoadType()) {
					fragment_shop_purchase_ls.stopLoadMore();
					PromptManager.ShowCustomToast(BaseContext, "没有更多订单哦");
				}
				if (LOAD_REFRESHING == Data.getHttpLoadType()) {
					fragment_shop_purchase_ls.stopRefresh();
					PromptManager.ShowCustomToast(BaseContext, "暂无订单");
					if (10 == Ket_Tage) {
						purchaseOrderNoPayAdapter
								.RefreshData(new ArrayList<BLShopPurchase>());
					} else {
						lsAp.RefreshData(new ArrayList<BLShopPurchase>());

					}
				}

				return;
			} else {
				fragent_purchase_nodata_lay.setVisibility(View.GONE);
				fragment_shop_purchase_ls.setVisibility(View.VISIBLE);
				list_datas = new ArrayList<BLShopPurchase>();
				try {
					list_datas = JSON.parseArray(Data.getHttpResultStr(),
							BLShopPurchase.class);
				} catch (Exception e) {
					onError("解析失败", 1);
				}
				if (0 == Ket_Tage) {
					CacheUtil.Shop_Purchase_List_Save(getApplicationContext(),
							Data.getHttpResultStr());
				}

				switch (Data.getHttpLoadType()) {
				case LOAD_INITIALIZE:
					if (10 == Ket_Tage) {
						purchaseOrderNoPayAdapter.RefreshData(list_datas);
					} else {
						lsAp.RefreshData(list_datas);
					}

					break;

				case LOAD_REFRESHING:// 刷新数据
					fragment_shop_purchase_ls.stopRefresh();
					lsAp.RefreshData(list_datas);
					if (list_datas.size() == Constants.PageSize)
						fragment_shop_purchase_ls.ShowFoot();
					if (list_datas.size() < Constants.PageSize)
						fragment_shop_purchase_ls.hidefoot();
					break;

				case LOAD_LOADMOREING:// 加载更多
					fragment_shop_purchase_ls.stopLoadMore();
					lsAp.AddFrashData(list_datas);
					if (list_datas.size() == Constants.PageSize)
						fragment_shop_purchase_ls.ShowFoot();
					if (list_datas.size() < Constants.PageSize)
						fragment_shop_purchase_ls.hidefoot();
					break;
				}
			}

			break;

		case 1:// 延期收货
			PromptManager.ShowMyToast(BaseContext, "订单已延期");
			last_id = "";
			IData(LOAD_INITIALIZE, Ket_Tage + "");
			break;

		case 2:// 提醒发货
			PromptManager.ShowMyToast(BaseContext, "已提醒卖家发货");// TODO 需要换成dialog
			last_id = "";
			IData(LOAD_INITIALIZE, Ket_Tage + "");
			
			break;

		case 3:// 确认收货
			PromptManager.ShowMyToast(BaseContext, "收货成功");// TODO 需要换成dialog
			last_id = "";
			IData(LOAD_INITIALIZE, Ket_Tage + "");
			break;
		case 4:// 取消订单
			PromptManager.ShowMyToast(BaseContext, "订单取消成功");
			last_id = "";
			IData(LOAD_INITIALIZE, Ket_Tage + "");
			break;

		case 5:// 去付款
			BDComment data = new BDComment();
			try {
				data = JSON.parseObject(Data.getHttpResultStr(),
						BDComment.class);
			} catch (Exception e) {

			}
			PromptManager.SkipActivity(AShopPurchaseOrder.this, new Intent(BaseContext,
					ACashierDesk.class).putExtra("addOrderInfo", data));
			break;

		}
	}

	@Override
	protected void DataError(String error, int LoadType) {
		if (dialog != null) {
			dialog.dismiss();
		}
		PromptManager.ShowMyToast(BaseContext, error);
		switch (LoadType) {
		case LOAD_INITIALIZE:// ss
			fragent_purchase_nodata_lay.setVisibility(View.VISIBLE);
			break;
		case LOAD_REFRESHING:// 刷新数据
			fragment_shop_purchase_ls.stopRefresh();
			break;
		case LOAD_LOADMOREING:// 加载更多
			fragment_shop_purchase_ls.stopLoadMore();
			break;
		}
		
	}

	@Override
	protected void NetConnect() {
		NetError.setVisibility(View.GONE);
		last_id = "";
		IData(LOAD_REFRESHING, Ket_Tage + "");
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
		case R.id.title:
			showPopupWindow(V);
			break;
		case R.id.ll_center_order_all:
			categoryOperate("全部", tv_center_order_all, PAll);
			break;

		case R.id.ll_center_order_no_pay:

			categoryOperate("待付款", tv_center_order_no_pay, PDaiFu);
			break;

		case R.id.ll_center_order_paid:
			categoryOperate("待发货", tv_center_order_paid, PYiFu);
			break;

		case R.id.ll_center_order_no_take:
			categoryOperate("待收货", tv_center_order_no_take, PDaiShou);
			break;

		case R.id.ll_center_order_refund_and_arbitrament:
			categoryOperate("退款/仲裁", tv_center_order_refund_and_arbitrament,
					PTuiKuan);
			break;

		case R.id.ll_center_order_over:
			categoryOperate("已完成", tv_center_order_over, PClose);
			break;

		case R.id.gray_view:
			closePopupWindow();
			break;

		case R.id.shop_purchase_nodata_lay:
			last_id = "";
			IData(LOAD_INITIALIZE, Ket_Tage + "");
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

	private void ICache() {
		String shop_Purchase_List = CacheUtil
				.Shop_Purchase_List_Get(getApplicationContext());
		if (StrUtils.isEmpty(shop_Purchase_List)) {
			PromptManager.showtextLoading(BaseContext, getResources()
					.getString(R.string.loading));
			return;
		}
		try {
			list_datas = JSON.parseArray(shop_Purchase_List,
					BLShopPurchase.class);
		} catch (Exception e) {
			return;
		}
		if (Ket_Tage == 0) {
			lsAp.RefreshData(list_datas);
		}

	}

	private void IBase() {
		user_Get = Spuit.User_Get(getApplicationContext());
		ILs();

		ICache();
		IData(LOAD_INITIALIZE, Ket_Tage + "");
	}

	private void IData(int loadType, String order_status) {

		if (loadType == LOAD_INITIALIZE) {
			PromptManager.showtextLoading(BaseContext, getResources()
					.getString(R.string.loading));
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("member_id", user_Get.getId());
		map.put("order_status", order_status);
		map.put("last_id", last_id);
		String host = "";
		if (10 == Ket_Tage) {
			map.put("channel", "CG");
			host = Constants.Purchase_No_Pay_List;
		} else {
			host = Constants.Purchase_List;
		}
		FBGetHttpData(map, host, Method.GET, 0, loadType);
	}

	/**
	 * 延期收货
	 */
	private void DelayShouHuo(String seller_order_sn, String member_id) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_order_sn", seller_order_sn);
		map.put("member_id", member_id);
		FBGetHttpData(map, Constants.Delay_Shou_Huo, Method.PUT, 1,
				LOAD_INITIALIZE);
	}

	/**
	 * 提醒发货
	 */
	private void RemindSendOut(String member_id, String seller_order_sn) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("member_id", member_id);
		map.put("seller_order_sn", seller_order_sn);
		FBGetHttpData(map, Constants.Purchase_Remind_Send_Out, Method.POST, 2,
				LOAD_INITIALIZE);
	}

	/**
	 * 确认收货
	 */
	private void ConfirmShouhuo(String member_id, String seller_order_sn) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_order_sn", seller_order_sn);
		map.put("member_id", member_id);
		FBGetHttpData(map, Constants.Confirm_Order, Method.POST, 3,
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
		FBGetHttpData(map, Constants.Purchase_Cancel_Order, Method.PUT, 4,
				LOAD_INITIALIZE);
	}

	/**
	 * 去付款
	 */
	private void GoPay(String member_id, String order_sn) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("member_id", member_id);
		map.put("order_sn", order_sn);
		FBGetHttpData(map, Constants.Center_My_Order_Go_Pay, Method.PUT, 5,
				LOAD_INITIALIZE);
	}

	private void ILs() {
		fragent_purchase_nodata_lay = findViewById(R.id.shop_purchase_nodata_lay);

		fragment_shop_purchase_ls = (LListView) findViewById(R.id.shop_purchase_ls);
		fragment_shop_purchase_ls.setPullRefreshEnable(true);
		fragment_shop_purchase_ls.setPullLoadEnable(true);
		fragment_shop_purchase_ls.setXListViewListener(this);
		fragment_shop_purchase_ls.hidefoot();

		// 设置滚动时不从网上加载图片
		fragment_shop_purchase_ls
				.setOnScrollListener(getPauseOnScrollListener(new OnScrollListener() {

					@Override
					public void onScrollStateChanged(AbsListView view,
							int scrollState) {
					}

					@Override
					public void onScroll(AbsListView view,
							int firstVisibleItem, int visibleItemCount,
							int totalItemCount) {
					}
				}));

		purchaseOrderNoPayAdapter = new PurchaseOrderNoPayAdapter(
				R.layout.item_purchase_order_no_pay_outside);

		lsAp = new LsAp(Ket_Tage, R.layout.item_fragment_shop_purchase);
		fragment_shop_purchase_ls.setAdapter(lsAp);

		fragment_shop_purchase_ls.setOnItemClickListener(this);
		fragent_purchase_nodata_lay.setOnClickListener(this);
		// fragent_purchase_nodata_lay.setEnabled(false);
	}

	/**
	 * 外层list的Ap
	 * 
	 * @author datutu
	 * 
	 */
	private class LsAp extends BasAdapter {
		private LayoutInflater inflater;
		private int ResourceId;
		private List<BLShopPurchase> datas = new ArrayList<BLShopPurchase>();
		private int type;

		public LsAp(int type, int resourceId) {
			super();
			this.inflater = LayoutInflater.from(BaseContext);
			this.ResourceId = resourceId;
			this.type = type;
		}

		/**
		 * 刷新数据
		 * 
		 * @param datas
		 */
		public void RefreshData(List<BLShopPurchase> datas) {

			this.datas = datas;
			this.notifyDataSetChanged();
		}

		/**
		 * 加载更多
		 */
		public void AddFrashData(List<BLShopPurchase> dass) {
			this.datas.addAll(dass);
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return datas.size();
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
		public View getView(int position, View convertView, ViewGroup arg2) {
			LsMyItem myItem = null;
			if (convertView == null) {
				myItem = new LsMyItem();
				convertView = inflater.inflate(ResourceId, null);
				myItem.item_fragment_shop_purchase_ls = (CompleteListView) convertView
						.findViewById(R.id.item_fragment_shop_purchase_ls);
				myItem.fragment_shop_purchase_shopname = ViewHolder.get(
						convertView, R.id.fragment_shop_purchase_shopname);
				myItem.item_fragment_shop_purchase_allnum = ViewHolder.get(
						convertView, R.id.item_fragment_shop_purchase_allnum);
				myItem.item_fragment_shop_purchase_allmoney = ViewHolder.get(
						convertView, R.id.item_fragment_shop_purchase_allmoney);
				myItem.fragment_shop_purchase_pay_to = ViewHolder.get(
						convertView, R.id.fragment_shop_purchase_pay_to);
				myItem.fragment_shop_purchase_pay_again = ViewHolder.get(
						convertView, R.id.fragment_shop_purchase_pay_again);
				myItem.fragment_shop_purchase_shouhuo_commiont = ViewHolder
						.get(convertView,
								R.id.fragment_shop_purchase_shouhuo_commiont);
				myItem.fragment_purchase_order_arbitration_is_cancel = ViewHolder
						.get(convertView,
								R.id.fragment_purchase_order_arbitration_is_cancel);
				myItem.fragment_purchase_order_arbitration = ViewHolder.get(
						convertView, R.id.fragment_purchase_order_arbitration);
				myItem.fragment_purchase_order_is_over = ViewHolder.get(
						convertView, R.id.fragment_purchase_order_is_over);
				myItem.fragment_purchase_order_apply_refunding = ViewHolder
						.get(convertView,
								R.id.fragment_purchase_order_apply_refunding);
				myItem.fragment_purchase_order_is_delay = ViewHolder.get(
						convertView, R.id.fragment_purchase_order_is_delay);
				myItem.tv_purchase_seller_order_sn = ViewHolder.get(
						convertView, R.id.tv_purchase_seller_order_sn);

				myItem.item_fragment_shop_purchase_postage = ViewHolder.get(
						convertView, R.id.item_fragment_shop_purchase_postage);

				convertView.setTag(myItem);
			} else {
				myItem = (LsMyItem) convertView.getTag();
			}
			final int myItemPosition = position;
			myItem.item_fragment_shop_purchase_ls.setAdapter(new ItemLsAp(
					R.layout.item_fragment_shop_purchase_initem, datas.get(
							position).getGoods()));
			// 商品Item的点击事件，跳转商品详情页面
			myItem.item_fragment_shop_purchase_ls
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							Intent intent = new Intent(BaseContext,
									AGoodDetail.class);
							intent.putExtra("goodid", datas.get(myItemPosition)
									.getGoods().get(position).getGoods_id());
							PromptManager.SkipActivity(BaseActivity, intent);
						}
					});

			StrUtils.SetTxt(myItem.fragment_shop_purchase_shopname,
					datas.get(position).getSeller_name());

			StrUtils.SetTxt(myItem.item_fragment_shop_purchase_allnum,
					String.format("共%1$s件商品", datas.get(position).getNumber()));
			float total_price = Float.parseFloat(datas.get(position)
					.getGoods_price())
					+ Float.parseFloat(datas.get(position).getPostage());
			StrUtils.SetTxt(
					myItem.item_fragment_shop_purchase_allmoney,
					String.format("%1$s元",
							StrUtils.SetTextForMony(total_price + "")));
			float postageF = Float.parseFloat(datas.get(position).getPostage());

			StrUtils.SetTxt(
					myItem.item_fragment_shop_purchase_postage,
					postageF == 0.0f ? "(免邮费)" : String.format("(含运费%1$s元)",
							StrUtils.SetTextForMony(postageF + "")));
			int order_status = Integer.parseInt(datas.get(position)
					.getOrder_status());
			StrUtils.SetTxt(myItem.tv_purchase_seller_order_sn,
					datas.get(position).getSeller_order_sn());

			switch (order_status) {
			case PDaiFu:// 待付款
				myItem.fragment_shop_purchase_pay_to
						.setVisibility(View.VISIBLE);
				myItem.fragment_shop_purchase_pay_again
						.setVisibility(View.VISIBLE);
				myItem.fragment_shop_purchase_pay_again.setText("取消订单");
				myItem.fragment_shop_purchase_shouhuo_commiont
						.setVisibility(View.GONE);
				myItem.fragment_purchase_order_arbitration_is_cancel
						.setVisibility(View.GONE);
				myItem.fragment_purchase_order_arbitration
						.setVisibility(View.GONE);
				myItem.fragment_purchase_order_is_over.setVisibility(View.GONE);
				myItem.fragment_purchase_order_apply_refunding
						.setVisibility(View.GONE);
				myItem.fragment_purchase_order_is_delay
						.setVisibility(View.GONE);
				break;
			case PYiFu:// 已付款
				if ("0".equals(datas.get(position).getRefund())) {
					myItem.fragment_shop_purchase_pay_again
							.setVisibility(View.VISIBLE);
					myItem.fragment_shop_purchase_pay_again.setText("申请退款");
					myItem.fragment_shop_purchase_shouhuo_commiont
							.setText("提醒发货");

					if ("0".equals(datas.get(position).getRemind_time())) {
						myItem.fragment_shop_purchase_shouhuo_commiont
								.setVisibility(View.VISIBLE);
					} else {
						myItem.fragment_shop_purchase_shouhuo_commiont
								.setVisibility(View.GONE);
					}

				} else {
					myItem.fragment_shop_purchase_pay_again
							.setVisibility(View.GONE);
				}
				myItem.fragment_shop_purchase_pay_to.setVisibility(View.GONE);
				myItem.fragment_purchase_order_arbitration_is_cancel
						.setVisibility(View.GONE);
				myItem.fragment_purchase_order_arbitration
						.setVisibility(View.GONE);
				myItem.fragment_purchase_order_is_over.setVisibility(View.GONE);
				myItem.fragment_purchase_order_apply_refunding
						.setVisibility(View.GONE);
				break;
			case PDaiShou:// 待收货

				// 延迟时间10天，只有10天后才显示延迟收货
				long delaytime = Long.parseLong(datas.get(position)
						.getCreate_time()) + (10 * 24 * 60 * 60);
				// 当前时间
				long nowtime = System.currentTimeMillis() / 1000;

				if (nowtime < delaytime) {
					myItem.fragment_shop_purchase_pay_again
							.setVisibility(View.GONE);
					myItem.fragment_shop_purchase_shouhuo_commiont
							.setVisibility(View.VISIBLE);
					myItem.fragment_shop_purchase_shouhuo_commiont
							.setText("确认收货");
					myItem.fragment_purchase_order_is_delay
							.setVisibility(View.GONE);
				} else {
					if ("0".equals(datas.get(position).getDelaynumber())) {
						myItem.fragment_shop_purchase_pay_again
								.setVisibility(View.VISIBLE);
						myItem.fragment_shop_purchase_pay_again.setText("延期收货");
						myItem.fragment_shop_purchase_shouhuo_commiont
								.setText("确认收货");
						myItem.fragment_shop_purchase_shouhuo_commiont
								.setVisibility(View.VISIBLE);
						myItem.fragment_purchase_order_is_delay
								.setVisibility(View.GONE);
					} else {
						myItem.fragment_shop_purchase_pay_again
								.setVisibility(View.GONE);
						myItem.fragment_shop_purchase_shouhuo_commiont
								.setVisibility(View.VISIBLE);
						myItem.fragment_shop_purchase_shouhuo_commiont
								.setText("确认收货");
						myItem.fragment_purchase_order_is_delay
								.setVisibility(View.GONE);
					}
				}

				myItem.fragment_shop_purchase_pay_to.setVisibility(View.GONE);
				myItem.fragment_purchase_order_arbitration_is_cancel
						.setVisibility(View.GONE);
				myItem.fragment_purchase_order_arbitration
						.setVisibility(View.GONE);
				myItem.fragment_purchase_order_is_over.setVisibility(View.GONE);
				myItem.fragment_purchase_order_apply_refunding
						.setVisibility(View.GONE);

				break;
			case PTuiKuan:// 退款--买家申请退款，卖家还没有同意退款时，订单状态为退款申请中
				myItem.fragment_purchase_order_apply_refunding
						.setVisibility(View.VISIBLE);
				myItem.fragment_shop_purchase_pay_again
						.setVisibility(View.GONE);
				myItem.fragment_shop_purchase_shouhuo_commiont
						.setVisibility(View.GONE);
				myItem.fragment_shop_purchase_pay_to.setVisibility(View.GONE);
				myItem.fragment_purchase_order_arbitration_is_cancel
						.setVisibility(View.GONE);
				myItem.fragment_purchase_order_arbitration
						.setVisibility(View.GONE);
				myItem.fragment_purchase_order_is_over.setVisibility(View.GONE);
				myItem.fragment_purchase_order_is_delay
						.setVisibility(View.GONE);
				break;

			case PZhongCai:// 仲裁，，卖家拒绝退款----状态变为仲裁状态
				myItem.fragment_purchase_order_arbitration
						.setVisibility(View.VISIBLE);
				myItem.fragment_shop_purchase_pay_again
						.setVisibility(View.GONE);
				myItem.fragment_shop_purchase_shouhuo_commiont
						.setVisibility(View.GONE);
				myItem.fragment_shop_purchase_pay_to.setVisibility(View.GONE);
				myItem.fragment_purchase_order_arbitration_is_cancel
						.setVisibility(View.GONE);
				myItem.fragment_purchase_order_apply_refunding
						.setVisibility(View.GONE);
				myItem.fragment_purchase_order_is_over.setVisibility(View.GONE);
				myItem.fragment_purchase_order_is_delay
						.setVisibility(View.GONE);
				break;

			case PAgreeRefund:// 卖家已同意退款-----此订单状态为已完成
				myItem.fragment_purchase_order_is_over
						.setVisibility(View.VISIBLE);
				StrUtils.SetTxt(myItem.fragment_purchase_order_is_over, "退款已完成");
				myItem.fragment_shop_purchase_pay_again
						.setVisibility(View.GONE);
				myItem.fragment_shop_purchase_shouhuo_commiont
						.setVisibility(View.GONE);
				myItem.fragment_shop_purchase_pay_to.setVisibility(View.GONE);
				myItem.fragment_purchase_order_arbitration_is_cancel
						.setVisibility(View.GONE);

				myItem.fragment_purchase_order_apply_refunding
						.setVisibility(View.GONE);

				myItem.fragment_purchase_order_arbitration
						.setVisibility(View.GONE);
				myItem.fragment_purchase_order_is_delay
						.setVisibility(View.GONE);
				break;
			case PClose:// 已关闭
				myItem.fragment_shop_purchase_pay_again
						.setVisibility(View.GONE);
				myItem.fragment_purchase_order_is_over
						.setVisibility(View.VISIBLE);
				StrUtils.SetTxt(myItem.fragment_purchase_order_is_over, "订单已完成");
				myItem.fragment_purchase_order_arbitration_is_cancel
						.setVisibility(View.GONE);
				myItem.fragment_shop_purchase_shouhuo_commiont
						.setVisibility(View.GONE);
				myItem.fragment_shop_purchase_pay_to.setVisibility(View.GONE);
				myItem.fragment_purchase_order_apply_refunding
						.setVisibility(View.GONE);
				myItem.fragment_purchase_order_arbitration
						.setVisibility(View.GONE);
				myItem.fragment_purchase_order_is_delay
						.setVisibility(View.GONE);
				break;

			case PIsCancel:// 订单已取消
				myItem.fragment_purchase_order_arbitration_is_cancel
						.setVisibility(View.VISIBLE);
				myItem.fragment_shop_purchase_pay_again
						.setVisibility(View.GONE);
				myItem.fragment_shop_purchase_shouhuo_commiont
						.setVisibility(View.GONE);
				myItem.fragment_shop_purchase_pay_to.setVisibility(View.GONE);
				myItem.fragment_purchase_order_apply_refunding
						.setVisibility(View.GONE);
				myItem.fragment_purchase_order_arbitration
						.setVisibility(View.GONE);
				myItem.fragment_purchase_order_is_over.setVisibility(View.GONE);
				myItem.fragment_purchase_order_is_delay
						.setVisibility(View.GONE);
				break;

			}

			OnClickEvent(myItem, datas.get(position), order_status);

			return convertView;

		}

		/**
		 * item中按钮点击事件
		 * 
		 * @param myItem
		 * @param blComment
		 * @param order_status
		 */
		private void OnClickEvent(LsMyItem myItem,
				final BLShopPurchase blComment, final int order_status) {
			myItem.fragment_shop_purchase_pay_again
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							switch (order_status) {
							case PDaiFu:// 待付款--取消订单

								ShowCustomDialog("确认取消订单吗？", "取消", "确认",
										new IDialogResult() {
											@Override
											public void RightResult() {
												if (CheckNet(BaseContext))
													return;
												CancelOrder(blComment
														.getMember_id(),
														blComment.getOrder_sn());

											}

											@Override
											public void LeftResult() {
											}
										});

								break;
							case PYiFu:// 已付款,点申请退款--->申请退款页面
								Intent intent2 = new Intent(BaseContext,
										AApplyTuikuan.class);
								intent2.putExtra("seller_order_sn",
										blComment.getSeller_order_sn());
								intent2.putExtra("FromTag",
										AApplyTuikuan.Tag_From_Purchase);
								PromptManager.SkipActivity(BaseActivity,
										intent2);
								break;
							case PDaiShou:// 待收货,延期发货
								// ShowCustomDialog(2, "确认延期收货吗？",
								// "每个商品只允许延期一次，延期为3天", blComment);

								ShowCustomDialog("每个商品只允许延期一次，延期为3天,确认延期收货吗？",
										"取消", "确认", new IDialogResult() {
											@Override
											public void RightResult() {
												if (CheckNet(BaseContext))
													return;
												DelayShouHuo(blComment
														.getSeller_order_sn(),
														blComment
																.getMember_id());
											}

											@Override
											public void LeftResult() {
											}
										});

								break;
							case PTuiKuan:// 退款
							case PZhongCai:// 仲裁

								break;
							case PClose:// 已关闭

								break;
							}
						}
					});
			// 提醒发货/确认收货的点击事件
			myItem.fragment_shop_purchase_shouhuo_commiont
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							switch (order_status) {
							case PYiFu:// 已付款,提醒发货
								if (CheckNet(BaseContext))
									return;
								RemindSendOut(blComment.getMember_id(),
										blComment.getSeller_order_sn());

								break;
							case PDaiShou:// 待收货,确认收货
								if (CheckNet(BaseContext))
									return;

								ShowCustomDialog("确认收货？", "取消", "确认",
										new IDialogResult() {
											@Override
											public void RightResult() {
												ConfirmShouhuo(
														blComment
																.getMember_id(),
														blComment
																.getSeller_order_sn());
											}

											@Override
											public void LeftResult() {
											}
										});

								break;
							case PTuiKuan:// 退款
							case PZhongCai:// 仲裁

								break;
							case PClose:// 已关闭

								break;
							}
						}
					});

			myItem.fragment_shop_purchase_pay_to
					.setOnClickListener(new OnClickListener() {
						// 去付款----->收银台页面
						@Override
						public void onClick(View v) {

							ShowCustomDialog("确认要去付款吗？", "取消", "确认",
									new IDialogResult() {
										@Override
										public void RightResult() {
											if (CheckNet(BaseContext))
												return;
											GoPay(blComment.getMember_id(),
													blComment.getOrder_sn());
										}

										@Override
										public void LeftResult() {
										}
									});

						}
					});

		}

		/**
		 * 外层的
		 */
		class LsMyItem {
			TextView fragment_shop_purchase_shopname;// 店名
			TextView fragment_shop_purchase_pay_to;// 去付款
			TextView fragment_shop_purchase_pay_again;// 再次购买
			TextView fragment_shop_purchase_shouhuo_commiont;// 确认收货
			TextView fragment_purchase_order_arbitration_is_cancel;// 订单已取消

			TextView fragment_purchase_order_arbitration;// 订单仲裁中
			TextView fragment_purchase_order_is_over;// 订单已完成
			TextView fragment_purchase_order_apply_refunding;// 申请退款中
			TextView fragment_purchase_order_is_delay;// 已延期收货
			TextView tv_purchase_seller_order_sn;// 订单编号

			CompleteListView item_fragment_shop_purchase_ls;

			TextView item_fragment_shop_purchase_allnum;// 多少件商品
			TextView item_fragment_shop_purchase_allmoney;// 所有费用
			TextView item_fragment_shop_purchase_postage;// 运费

		}
	}

	/**
	 * 内层的ls的adapter
	 * 
	 * @author datutu
	 * 
	 */
	class ItemLsAp extends BaseAdapter {

		private LayoutInflater inflater;
		private int ResourceId;
		private List<BLDComment> datas = new ArrayList<BLDComment>();

		public ItemLsAp(int resourceId, List<BLDComment> datass) {
			super();
			this.inflater = LayoutInflater.from(BaseContext);
			this.ResourceId = resourceId;
			this.datas = datass;
		}

		@Override
		public int getCount() {
			return datas.size();
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
		public View getView(int position, View convertView, ViewGroup arg2) {
			InLsMyItem myItem = null;
			if (convertView == null) {
				myItem = new InLsMyItem();
				convertView = inflater.inflate(ResourceId, null);
				myItem.item_fragment_shop_purchase_in_iv = ViewHolder.get(
						convertView, R.id.item_fragment_shop_purchase_in_iv);
				myItem.item_fragment_shop_purchase_in_name = ViewHolder.get(
						convertView, R.id.item_fragment_shop_purchase_in_name);
				myItem.item_fragment_shop_purchase_in_price = ViewHolder.get(
						convertView, R.id.item_fragment_shop_purchase_in_price);
				myItem.item_fragment_shop_purchase_in_number = ViewHolder
						.get(convertView,
								R.id.item_fragment_shop_purchase_in_number);
				myItem.item_fragment_shop_purchase_in_content = ViewHolder.get(
						convertView,
						R.id.item_fragment_shop_purchase_in_content);
				ImageLoaderUtil.Load2(datas.get(position).getGoods_cover(),
						myItem.item_fragment_shop_purchase_in_iv,
						R.drawable.error_iv2);
				convertView.setTag(myItem);
			} else {
				myItem = (InLsMyItem) convertView.getTag();
			}
			StrUtils.SetTxt(myItem.item_fragment_shop_purchase_in_name, datas
					.get(position).getGoods_name());
			StrUtils.SetTxt(
					myItem.item_fragment_shop_purchase_in_price,
					String.format("￥%1$s元", StrUtils.SetTextForMony(datas.get(
							position).getGoods_price())));
			StrUtils.SetTxt(myItem.item_fragment_shop_purchase_in_number,
					String.format("X%1$s", datas.get(position)
							.getGoods_number()));
			StrUtils.SetTxt(myItem.item_fragment_shop_purchase_in_content,
					String.format("规格：%1$s", datas.get(position)
							.getGoods_standard()));

			return convertView;

		}

		class InLsMyItem {
			ImageView item_fragment_shop_purchase_in_iv;
			TextView item_fragment_shop_purchase_in_name;
			TextView item_fragment_shop_purchase_in_price;
			TextView item_fragment_shop_purchase_in_number;
			TextView item_fragment_shop_purchase_in_content;
		}
	}

	// *************************************************************************
	/**
	 * @author Yihuihua 最外层的AP
	 */
	class PurchaseOrderNoPayAdapter extends BaseAdapter {

		private int ResourseId;
		private LayoutInflater inflater;
		List<BLShopPurchase> datas = new ArrayList<BLShopPurchase>();

		public PurchaseOrderNoPayAdapter(int ResourseId) {
			super();
			this.ResourseId = ResourseId;
			this.inflater = LayoutInflater.from(BaseContext);
		}

		@Override
		public int getCount() {

			return datas.size();
		}

		/**
		 * 刷新列表数据
		 * 
		 * @param list_datas
		 */
		public void RefreshData(List<BLShopPurchase> list_datas) {
			this.datas = list_datas;
			this.notifyDataSetChanged();
		}

		/**
		 * 加载更多
		 */
		public void AddFrashData(List<BLShopPurchase> order_list) {
			this.datas.addAll(order_list);
			this.notifyDataSetChanged();
		}

		@Override
		public Object getItem(int position) {

			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			PurchaseOrderNoPayItem centerOrderNoPay = null;
			if (convertView == null) {
				centerOrderNoPay = new PurchaseOrderNoPayItem();
				convertView = inflater.inflate(ResourseId, null);
				centerOrderNoPay.tv_purchase_order_no_pay_order_sn = (TextView) convertView
						.findViewById(R.id.tv_purchase_order_no_pay_order_sn);
				centerOrderNoPay.fragment_purchase_order_no_pay_pay_to = (TextView) convertView
						.findViewById(R.id.fragment_purchase_order_no_pay_pay_to);
				centerOrderNoPay.fragment_purchase_order_no_pay_cancel_order = (TextView) convertView
						.findViewById(R.id.fragment_purchase_order_no_pay_cancel_order);
				centerOrderNoPay.item_fragment_purchase_order_no_pay_outside = (CompleteListView) convertView
						.findViewById(R.id.item_fragment_purchase_order_no_pay_outside);
				centerOrderNoPay.item_fragment_purchase_order_no_pay_allnum = (TextView) convertView
						.findViewById(R.id.item_fragment_purchase_order_no_pay_allnum);
				centerOrderNoPay.item_fragment_purchase_order_no_pay_allmoney = (TextView) convertView
						.findViewById(R.id.item_fragment_purchase_order_no_pay_allmoney);
				centerOrderNoPay.item_fragment_purchase_order_no_pay_postage = (TextView) convertView
						.findViewById(R.id.item_fragment_purchase_order_no_pay_postage);

				convertView.setTag(centerOrderNoPay);
			} else {
				centerOrderNoPay = (PurchaseOrderNoPayItem) convertView
						.getTag();
			}
			PurchaseOrderNoPayInsideAdapter centerOrderNoPayInside = new PurchaseOrderNoPayInsideAdapter(
					R.layout.item_purchase_order_no_pay_inside, datas.get(
							position).getSon_order());
			centerOrderNoPay.item_fragment_purchase_order_no_pay_outside
					.setAdapter(centerOrderNoPayInside);
			StrUtils.SetTxt(centerOrderNoPay.tv_purchase_order_no_pay_order_sn,
					datas.get(position).getOrder_sn());

			// float total_price =
			// Float.parseFloat(datas.get(position).getOrder_total_price())
			// + Float.parseFloat(datas.get(position).getPostage_money());
			StrUtils.SetTxt(
					centerOrderNoPay.item_fragment_purchase_order_no_pay_allmoney,
					String.format("%1$s元", StrUtils.SetTextForMony(datas.get(
							position).getOrder_total_price())));
			float postageF = Float.parseFloat(datas.get(position)
					.getPostage_money());
			StrUtils.SetTxt(
					centerOrderNoPay.item_fragment_purchase_order_no_pay_postage,
					postageF == 0.0f ? "(免邮费)" : String.format("(含运费%1$s元)",
							StrUtils.SetTextForMony(datas.get(position)
									.getPostage_money())));

			final BLShopPurchase blComment = datas.get(position);
			// 去付款
			centerOrderNoPay.fragment_purchase_order_no_pay_pay_to
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							ShowCustomDialog("确认要去付款吗？", "取消", "确认",
									new IDialogResult() {
										@Override
										public void RightResult() {
											if (CheckNet(BaseContext))
												return;
											GoPay(blComment.getMember_id(),
													blComment.getOrder_sn());
										}

										@Override
										public void LeftResult() {
										}
									});

						}
					});
			// 取消订单
			centerOrderNoPay.fragment_purchase_order_no_pay_cancel_order
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

							ShowCustomDialog("确认取消订单吗？", "取消", "确认",
									new IDialogResult() {
										@Override
										public void RightResult() {
											if (CheckNet(BaseContext))
												return;
											CancelOrder(
													blComment.getMember_id(),
													blComment.getOrder_sn());

										}

										@Override
										public void LeftResult() {
										}
									});
						}
					});
			return convertView;
		}

	}

	class PurchaseOrderNoPayInsideAdapter extends BaseAdapter {

		private int ResourseId;
		private LayoutInflater inflater;
		private List<BLDComment> secoud_datas = new ArrayList<BLDComment>();

		public PurchaseOrderNoPayInsideAdapter(int ResourseId,
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
			PurchaseOrderNoPayInsideItem centerOrderNoPayInside = null;
			if (convertView == null) {
				centerOrderNoPayInside = new PurchaseOrderNoPayInsideItem();
				convertView = inflater.inflate(ResourseId, null);
				centerOrderNoPayInside.tv_purchase_order_no_pay_seller_name = (TextView) convertView
						.findViewById(R.id.tv_purchase_order_no_pay_seller_name);
				centerOrderNoPayInside.item_fragment_purchase_order_no_pay_inside = (CompleteListView) convertView
						.findViewById(R.id.item_fragment_purchase_order_no_pay_inside);
				convertView.setTag(centerOrderNoPayInside);
			} else {
				centerOrderNoPayInside = (PurchaseOrderNoPayInsideItem) convertView
						.getTag();
			}
			StrUtils.SetTxt(
					centerOrderNoPayInside.tv_purchase_order_no_pay_seller_name,
					String.format("卖家：%1$s", secoud_datas.get(position)
							.getSeller_name()));
			PurchaseOrderNoPayInnerMostAdapter centerOrderNoPayInnerMost = new PurchaseOrderNoPayInnerMostAdapter(
					R.layout.item_purchase_order_no_pay_innermost, secoud_datas
							.get(position).getGoods());
			centerOrderNoPayInside.item_fragment_purchase_order_no_pay_inside
					.setAdapter(centerOrderNoPayInnerMost);
			final int myItemPosition = position;
			centerOrderNoPayInside.item_fragment_purchase_order_no_pay_inside
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

			return convertView;
		}

	}

	/**
	 * @author Yihuihua 最里层的AP
	 */
	class PurchaseOrderNoPayInnerMostAdapter extends BaseAdapter {

		private int ResourseId;
		private LayoutInflater inflater;
		private List<BDComment> innerMost_data = new ArrayList<BDComment>();

		public PurchaseOrderNoPayInnerMostAdapter(int ResourseId,
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
			PurchaseOrderNoPayInnerMostItem centerOrderNoPayInnerMost = null;
			if (convertView == null) {
				centerOrderNoPayInnerMost = new PurchaseOrderNoPayInnerMostItem();
				convertView = inflater.inflate(ResourseId, null);
				centerOrderNoPayInnerMost.item_fragment_purchase_order_no_pay_in_iv = ViewHolder
						.get(convertView,
								R.id.item_fragment_purchase_order_no_pay_in_iv);
				centerOrderNoPayInnerMost.item_fragment_purchase_order_no_pay_in_name = ViewHolder
						.get(convertView,
								R.id.item_fragment_purchase_order_no_pay_in_name);
				centerOrderNoPayInnerMost.item_fragment_purchase_order_no_pay_in_price = ViewHolder
						.get(convertView,
								R.id.item_fragment_purchase_order_no_pay_in_price);
				centerOrderNoPayInnerMost.item_fragment_purchase_order_no_pay_in_number = ViewHolder
						.get(convertView,
								R.id.item_fragment_purchase_order_no_pay_in_number);
				convertView.setTag(centerOrderNoPayInnerMost);
				ImageLoaderUtil
						.Load2(innerMost_data.get(position).getGoods_cover(),
								centerOrderNoPayInnerMost.item_fragment_purchase_order_no_pay_in_iv,
								R.drawable.error_iv2);
			} else {
				centerOrderNoPayInnerMost = (PurchaseOrderNoPayInnerMostItem) convertView
						.getTag();
			}

			StrUtils.SetTxt(
					centerOrderNoPayInnerMost.item_fragment_purchase_order_no_pay_in_name,
					innerMost_data.get(position).getGoods_name());
			StrUtils.SetTxt(
					centerOrderNoPayInnerMost.item_fragment_purchase_order_no_pay_in_price,
					String.format("%1$s元", StrUtils
							.SetTextForMony(innerMost_data.get(position)
									.getGoods_money())));
			StrUtils.SetTxt(
					centerOrderNoPayInnerMost.item_fragment_purchase_order_no_pay_in_number,
					String.format("X%1$s", innerMost_data.get(position)
							.getGoods_number()));
			return convertView;
		}

	}

	/**
	 * @author Yihuihua 最外层列表的Holder
	 */
	class PurchaseOrderNoPayItem {

		public TextView tv_purchase_order_no_pay_order_sn;// 订单号
		public TextView fragment_purchase_order_no_pay_pay_to;// 去付款
		public TextView fragment_purchase_order_no_pay_cancel_order;// 取消订单
		public CompleteListView item_fragment_purchase_order_no_pay_outside;// 第一层的订单列表
		public TextView tv_center_my_order_no_pay_seller_order_sn;// 订单编号
		public TextView item_fragment_purchase_order_no_pay_allnum;// 有多少件商品
		public TextView item_fragment_purchase_order_no_pay_allmoney;// 总价
		public TextView item_fragment_purchase_order_no_pay_postage;// 运费

	}

	/**
	 * @author Yihuihua 第二层列表Holder
	 */
	class PurchaseOrderNoPayInsideItem {
		public TextView tv_purchase_order_no_pay_seller_name;// 买家名称
		public CompleteListView item_fragment_purchase_order_no_pay_inside;// 第二层的订单列表
	}

	/**
	 * @author Yihuihua 最里层的Holder
	 */
	class PurchaseOrderNoPayInnerMostItem {

		public ImageView item_fragment_purchase_order_no_pay_in_iv;// 商品图标
		public TextView item_fragment_purchase_order_no_pay_in_name;// 商品title
		public TextView item_fragment_purchase_order_no_pay_in_price;// 商品价格
		public TextView item_fragment_purchase_order_no_pay_in_number;// 商品个数
	}

	// *************************************************************************







	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (CheckNet(BaseContext))
			return;
		BLShopPurchase bl_data = null;
		Intent intent = null;
		try {

			if (10 != Ket_Tage) {
				// int count = lsAp.getCount();
				// if (count > 0) {
				bl_data = (BLShopPurchase) lsAp.getItem(arg2 - 1);
				if ("10".equals(bl_data.getOrder_status())) {
					intent = new Intent(BaseContext, APurchaseNoPayDetail.class);
					intent.putExtra("order_sn", bl_data.getOrder_sn());
					intent.putExtra("member_id", bl_data.getMember_id());
					PromptManager.SkipActivity((Activity) BaseContext, intent);
				} else {
					intent = new Intent(BaseContext, APurchaseDetail.class);
					intent.putExtra("seller_order_sn",
							bl_data.getSeller_order_sn());
					intent.putExtra("Key_TageStr",
							Integer.parseInt(bl_data.getOrder_status()));
					intent.putExtra("member_id", bl_data.getMember_id());
					PromptManager.SkipActivity((Activity) BaseContext, intent);
				}
				// }

			} else {
				int count = purchaseOrderNoPayAdapter.getCount();
				// if (count > 0) {
				bl_data = (BLShopPurchase) purchaseOrderNoPayAdapter
						.getItem(arg2 - 1);
				intent = new Intent(BaseContext, APurchaseNoPayDetail.class);
				intent.putExtra("order_sn", bl_data.getOrder_sn());
				intent.putExtra("member_id", bl_data.getMember_id());
				PromptManager.SkipActivity((Activity) BaseContext, intent);
				// }

			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	/**
	 * 接收事件
	 * 
	 * @param event
	 */

	public void OnGetMessage(BMessage event) {
		int messageType = event.getMessageType();
		if (messageType == event.Tage_My_Purchase) {
			last_id = "";
			IData(LOAD_INITIALIZE, Ket_Tage + "");
		}
		if (messageType == event.Tage_To_Pay_Updata) {
			last_id = "";
			IData(LOAD_INITIALIZE, Ket_Tage + "");
		}
	}

	@Override
	public void onRefresh() {
		last_id = "";
		IData(LOAD_REFRESHING, Ket_Tage + "");
	}

	@Override
	public void onLoadMore() {
		IData(LOAD_LOADMOREING, Ket_Tage + "");
	}

	// @Override
	// public void onClick(View v) {
	// last_id = "";
	// IData(LOAD_INITIALIZE, Ket_Tage + "");
	// }

	private void categoryOperate(String title, TextView view, int oprateType) {
		if (PDaiFu == oprateType) {
			fragment_shop_purchase_ls.setAdapter(purchaseOrderNoPayAdapter);
		} else {
			fragment_shop_purchase_ls.setAdapter(lsAp);
		}
		
		changeTextColor(view);
		SetTitleTxt(title);
		closePopupWindow();
		
		if(Ket_Tage == oprateType){
			closePopupWindow();
		}else{
			last_id = "";
			if(list_datas != null &&list_datas.size()>0){
				list_datas.clear();
			}
			Ket_Tage = oprateType;
			IData(LOAD_INITIALIZE, Ket_Tage + "");
		}
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		last_id = "";
		Ket_Tage = PAll;
		mPopupWindow = null;
		try {
			EventBus.getDefault().unregister(this);
		} catch (Exception e) {

		}
	}

}
