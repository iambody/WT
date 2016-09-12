package io.vtown.WeiTangApp.fragment;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.adapter.BasAdapter;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.centerorder.BLCenterOder;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.comment.view.listview.LListView.IXListViewListener;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.comment.order.ACenterMyOrder;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.account.ACashierDesk;
import io.vtown.WeiTangApp.ui.title.center.myorder.AApplyTuikuan;
import io.vtown.WeiTangApp.ui.title.center.myorder.ACenterMyOrderDetail;
import io.vtown.WeiTangApp.ui.title.center.myorder.ACenterMyOrderNoPayDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.lidroid.xutils.util.LogUtils;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-19 下午1:46:37 我的里面进入的订单
 */
public class FCenterOder extends FBase implements OnItemClickListener,
		IXListViewListener, OnClickListener {

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
	public static final int PAgreeTuiKuan = 70;
	public static final int PClose = 100;
	public static final int PCancel = 110;

	/**
	 * 传递tage的key
	 */
	public static final String Key_TageStr = "FCenterssOderkey";
	/**
	 * 获取到的key
	 */
	private static int Ket_Tage = -1;
	/**
	 * 我的订单列表
	 */
	private LListView fragment_center_order_ls;
	private View fragent_centeroder_nodata_lay;
	/**
	 * 用户信息
	 */
	private BUser user_Get;

	/**
	 * 当前订单状态
	 */
	private String order_status = "";

	/**
	 * last_id
	 */
	private String last_id = "";
	/**
	 * AP
	 */
	private CenterOrderOutsideAdapter centerOrderOutsideAdapter;
	/**
	 * 待付款AP
	 */
	private CenterOrderNoPayAdapter centerOrderNoPayAdapter;

	/**
	 * 是否需要在onResumeg再刷新数据标志
	 */
	private boolean NeedRefrashData = false;
	/**
	 * 列表数据
	 */
	private List<BLCenterOder> order_list;

	@Override
	public void InItView() {

		BaseView = LayoutInflater.from(BaseContext).inflate(
				R.layout.fragment_center_oder, null);
		SetTitleHttpDataLisenter(this);
		user_Get = Spuit.User_Get(BaseContext);
		if (-1 == Ket_Tage)
			return;
		IView();
		ICache();
		IData(INITIALIZE, order_status);
		
		// 注册事件
		EventBus.getDefault().register(this, "OnGetMessage", BMessage.class);

	}

	private void ICache() {
		String center_Order_List = CacheUtil.Center_Order_List_Get(BaseContext);
		if (StrUtils.isEmpty(center_Order_List)) {
			return;
		}
		try {
			order_list = JSON.parseArray(center_Order_List, BLCenterOder.class);
		} catch (Exception e) {
			return;
		}
		if (PAll == Ket_Tage) {
			centerOrderOutsideAdapter.RefreshData(order_list);
		}
	}

	private void IView() {
		fragent_centeroder_nodata_lay=BaseView.findViewById(R.id.fragent_centeroder_nodata_lay);
		
		
		fragment_center_order_ls = (LListView) BaseView
				.findViewById(R.id.fragment_center_order_ls);
		centerOrderOutsideAdapter = new CenterOrderOutsideAdapter(
				R.layout.item_fragment_center_order_outside);
		centerOrderNoPayAdapter = new CenterOrderNoPayAdapter(
				R.layout.item_center_order_no_pay_outside);

		if (10 == Ket_Tage) {
			fragment_center_order_ls.setAdapter(centerOrderNoPayAdapter);
		} else {
			fragment_center_order_ls.setAdapter(centerOrderOutsideAdapter);
		}
		// 设置滚动时不从网上加载图片
		fragment_center_order_ls
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
		fragment_center_order_ls.setOnItemClickListener(this);
		fragent_centeroder_nodata_lay.setOnClickListener(this);

		fragment_center_order_ls.setPullRefreshEnable(true);
		fragment_center_order_ls.setPullLoadEnable(true);
		fragment_center_order_ls.setXListViewListener(this);
		fragment_center_order_ls.hidefoot();
	}

	/**
	 * 
	 * 获取列表数据
	 * 
	 * @param string
	 * @param order_status
	 * @param last_id
	 * @param initialize
	 */
	private void IData(int loadType, String order_status) {
		if (loadType == INITIALIZE) {
			PromptManager.showtextLoading(BaseContext, getResources()
					.getString(R.string.loading));
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("member_id", user_Get.getId()); // );

		map.put("lastid", last_id);

		map.put("pagesize", Constants.PageSize + "");

		String host = "";

		if (10 == Ket_Tage) {
			map.put("type", "PT");
			host = Constants.CenterOderWeiFu;
		} else {
			map.put("order_status", order_status + "");
			host = Constants.Center_My_Order;
		}
		FBGetHttpData(map, host, Method.GET, 0, loadType);

	}

	/**
	 * 提醒发货请求
	 * 
	 * @param seller_order_sn
	 * @param member_id
	 */
	private void RemindSendOut(String seller_order_sn, String member_id) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_order_sn", seller_order_sn);
		map.put("member_id", member_id);
		FBGetHttpData(map, Constants.Center_My_Order_Remind_Send_Out,
				Method.PUT, 1, INITIALIZE);
	}

	/**
	 * 确认收货请求
	 * 
	 * @param seller_order_sn
	 * @param member_id
	 */
	private void ConfirmShouhuo(String seller_order_sn, String member_id) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_order_sn", seller_order_sn);
		map.put("member_id", member_id);
		FBGetHttpData(map, Constants.Center_My_Order_Confirm_Order, Method.PUT,
				2, INITIALIZE);
	}

	/**
	 * 取消订单
	 */
	private void CancelMyOrder(String seller_order_sn, String member_id) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("member_id", member_id);
		// @王永奎=======》7.14修改取消订单的key值！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
		map.put("order_sn", seller_order_sn);
		FBGetHttpData(map, Constants.Center_My_Order_Cancel, Method.PUT, 3,
				INITIALIZE);
	}

	/**
	 * 去付款
	 */
	private void GoPay(String member_id, String order_sn) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("member_id", member_id);
		map.put("order_sn", order_sn);
		FBGetHttpData(map, Constants.Center_My_Order_Go_Pay, Method.PUT, 4,
				INITIALIZE);
	}

	/**
	 * 延迟收货
	 * 
	 * @param seller_order_sn
	 * @param member_id
	 */
	private void DelayReceive(String seller_order_sn, String member_id) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("member_id", member_id);
		map.put("seller_order_sn", seller_order_sn);
		FBGetHttpData(map, Constants.Center_My_Order_Delayreceive, Method.PUT,
				5, INITIALIZE);
	}

	@Override
	public void InitCreate(Bundle d) {
		if (null != d && d.containsKey(Key_TageStr)) {
			Ket_Tage = d.getInt(Key_TageStr);
			order_status = Ket_Tage + "";
		}else{
			Ket_Tage = -1;
		}
	}

	/**
	 * 暴露对象
	 * 
	 * @param arg
	 * @return
	 */
	public static FCenterOder newInstance(int arg) {
		FCenterOder fragment = new FCenterOder();
		Bundle bundle = new Bundle();
		bundle.putInt(Key_TageStr, arg);
		fragment.setArguments(bundle);
		return fragment;
	}

	/**
	 * 暴露当是哪个订单状态的页面
	 * 
	 * @return
	 */
	public static int GetKetTage() {
		return Ket_Tage;
	}

	/**
	 * 重新刷新数据
	 */
	public void RegetData() {
		last_id = "";
		IData(INITIALIZE, order_status);
	}

	@Override
	public void getResult(int Code, String Msg, BComment Data) {

		switch (Data.getHttpResultTage()) {
		case 0:// 获取列表的返回结果

			if (StrUtils.isEmpty(Data.getHttpResultStr())) {
				
				
				if (INITIALIZE == Data.getHttpLoadType()) {
					PromptManager.ShowCustomToast(BaseContext, "暂无订单");
					fragent_centeroder_nodata_lay.setVisibility(View.VISIBLE);
					fragment_center_order_ls.setVisibility(View.GONE);
					if (10 == Ket_Tage) {
						centerOrderNoPayAdapter
								.RefreshData(new ArrayList<BLCenterOder>());
					} else {
						centerOrderOutsideAdapter
								.RefreshData(new ArrayList<BLCenterOder>());
						
						if (PAll == Ket_Tage) {
							CacheUtil.Center_Order_List_Save(BaseContext,
									Data.getHttpResultStr());
						}
					}
				}
				if (LOADMOREING == Data.getHttpLoadType()) {
					fragment_center_order_ls.stopLoadMore();
					PromptManager.ShowCustomToast(BaseContext, "没有更多订单哦");
				}
				if (REFRESHING == Data.getHttpLoadType()) {
					fragment_center_order_ls.stopRefresh();
					PromptManager.ShowCustomToast(BaseContext, "暂无订单");
				}
				return;
			}
			fragment_center_order_ls.setVisibility(View.VISIBLE);
			fragent_centeroder_nodata_lay.setVisibility(View.GONE);
			order_list = new ArrayList<BLCenterOder>();
			
//			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,AFragment.class));
//			PromptManager.showtextLoading(BaseContext, getResources()
//					.getString(R.string.loading));
			try {
				order_list = JSON.parseArray(Data.getHttpResultStr(),
						BLCenterOder.class);
			} catch (Exception e) {
				onError("解析失败", 0);
			}
			PromptManager.closetextLoading();
			if (PAll == Ket_Tage) {
				CacheUtil.Center_Order_List_Save(BaseContext,
						Data.getHttpResultStr());
			}
			switch (Data.getHttpLoadType()) {
			case INITIALIZE:
				
				if (10 == Ket_Tage) {
					centerOrderNoPayAdapter.RefreshData(order_list);
				} else {
					centerOrderOutsideAdapter.RefreshData(order_list);
				}
				fragment_center_order_ls.scrollTo(0, 10);
				break;

			case REFRESHING:// 刷新数据
				fragment_center_order_ls.stopRefresh();
				if (10 == Ket_Tage) {
					centerOrderNoPayAdapter.RefreshData(order_list);
				} else {
					centerOrderOutsideAdapter.RefreshData(order_list);
				}

				if (order_list.size() == Constants.PageSize)
					fragment_center_order_ls.ShowFoot();
				if (order_list.size() < Constants.PageSize)
					fragment_center_order_ls.hidefoot();
				break;

			case LOADMOREING:// 加载更多
				fragment_center_order_ls.stopLoadMore();
				if (10 == Ket_Tage) {
					centerOrderNoPayAdapter.AddFrashData(order_list);
				} else {
					centerOrderOutsideAdapter.AddFrashData(order_list);
				}

				if (order_list.size() == Constants.PageSize)
					fragment_center_order_ls.ShowFoot();
				if (order_list.size() < Constants.PageSize)
					fragment_center_order_ls.hidefoot();
				break;
			}

			last_id = order_list.get(order_list.size() - 1).getId();

			break;
		case 1:// 提醒发货
			PromptManager.ShowMyToast(BaseContext, "已提醒卖家发货");
			last_id = "";
			IData(INITIALIZE, order_status);
			switch (Ket_Tage) {
			case PAll:// 如果当前是全部页面，就通知待收货页面
				EventBus.getDefault().post(new BMessage(PYiFu));
				break;

			case PYiFu:// 如果当前是待收货页面，就通知全部页面
				EventBus.getDefault().post(new BMessage(PAll));
				break;
			}
			break;

		case 2:// 确认收货
			PromptManager.ShowMyToast(BaseContext, "确认收货");
			last_id = "";
			IData(INITIALIZE, order_status);
			switch (Ket_Tage) {
			case PAll:// 如果当前是全部页面，就通知待收货页面
				EventBus.getDefault().post(new BMessage(PDaiShou));
				break;

			case PDaiShou:// 如果当前是待收货页面，就通知全部页面
				EventBus.getDefault().post(new BMessage(PAll));
				break;
			}

			break;

		case 3:// 取消订单
			PromptManager.ShowMyToast(BaseContext, "订单取消成功");
			last_id = "";
			IData(INITIALIZE, order_status);
			switch (Ket_Tage) {
			case PAll:// 如果当前是全部页面，就通知待付款页面
				EventBus.getDefault().post(new BMessage(PDaiFu));
				break;

			case PDaiFu:// 如果当前是待付款页面，就通知全部页面
				EventBus.getDefault().post(new BMessage(PAll));
				break;
			}
			break;

		case 4:// 去付款
			BDComment data = new BDComment();
			try {
				data = JSON.parseObject(Data.getHttpResultStr(),
						BDComment.class);
			} catch (Exception e) {

			}
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					ACashierDesk.class).putExtra("addOrderInfo", data));
			break;

		case 5:// 延迟收货
			PromptManager.ShowMyToast(BaseContext, "订单已延期");
			last_id = "";
			IData(INITIALIZE, order_status);
			switch (Ket_Tage) {
			case PAll:// 如果当前是全部页面，就通知待收货页面
				EventBus.getDefault().post(new BMessage(PDaiShou));
				break;

			case PDaiShou:// 如果当前是待收货页面，就通知全部页面
				EventBus.getDefault().post(new BMessage(PAll));
				break;
			}
			break;

		default:
			break;
		}

	}

	@Override
	public void onError(String error, int LoadType) {
		PromptManager.ShowMyToast(BaseContext, error);
		switch (LoadType) {
		case REFRESHING:// 刷新数据
			fragment_center_order_ls.stopRefresh();

			break;
		case LOADMOREING:// 加载更多
			fragment_center_order_ls.stopLoadMore();

			break;
		}
	}

	/**
	 * 外层list的Ap
	 * 
	 * @author datutu
	 * 
	 */
	private class CenterOrderOutsideAdapter extends BasAdapter {
		private LayoutInflater inflater;
		private int ResourceId;
		private List<BLCenterOder> datas = new ArrayList<BLCenterOder>();

		public CenterOrderOutsideAdapter(int resourceId) {
			super();

			this.inflater = LayoutInflater.from(BaseContext);
			this.ResourceId = resourceId;

		}

		/**
		 * 刷新列表数据
		 * 
		 * @param order_list
		 */
		public void RefreshData(List<BLCenterOder> order_list) {
			this.datas = order_list;
			this.notifyDataSetChanged();
		}

		/**
		 * 加载更多
		 */
		public void AddFrashData(List<BLCenterOder> order_list) {
			this.datas.addAll(order_list);
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

		/*
		 * (non-Javadoc)
		 * 
		 * @see io.vtown.WeiTangApp.adapter.BasAdapter#getView(int,
		 * android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			CenterOrderOutsideItem myItem = null;
			if (convertView == null) {

				convertView = inflater.inflate(ResourceId, null);
				myItem = new CenterOrderOutsideItem();
				myItem.fragment_center_order_shopname = (TextView) convertView
						.findViewById(R.id.fragment_center_order_shopname);
				myItem.fragment_center_order_cancel_order = (TextView) convertView
						.findViewById(R.id.fragment_center_order_cancel_order);
				myItem.fragment_center_order_pay_to = (TextView) convertView
						.findViewById(R.id.fragment_center_order_pay_to);
				myItem.fragment_center_order_pay_again = (TextView) convertView
						.findViewById(R.id.fragment_center_order_pay_again);
				myItem.fragment_center_order_shouhuo_commiont = (TextView) convertView
						.findViewById(R.id.fragment_center_order_shouhuo_commiont);
				myItem.fragment_center_order_apply_for_tuikuan_commiont = (TextView) convertView
						.findViewById(R.id.fragment_center_order_apply_for_tuikuan_commiont);
				myItem.fragment_center_order_remind_fahuo = (TextView) convertView
						.findViewById(R.id.fragment_center_order_remind_fahuo);
				myItem.fragment_center_order_arbitration = (TextView) convertView
						.findViewById(R.id.fragment_center_order_arbitration);
				myItem.fragment_center_order_is_delaytime = (TextView) convertView
						.findViewById(R.id.fragment_center_order_is_delaytime);
				myItem.fragment_center_order_delayreceive = (TextView) convertView
						.findViewById(R.id.fragment_center_order_delayreceive);
				myItem.item_fragment_center_order_allnum = (TextView) convertView
						.findViewById(R.id.item_fragment_center_order_allnum);
				myItem.item_fragment_center_order_allmoney = (TextView) convertView
						.findViewById(R.id.item_fragment_center_order_allmoney);
				myItem.item_fragment_center_order_ls = (CompleteListView) convertView
						.findViewById(R.id.item_fragment_center_order_ls);
				myItem.item_fragment_center_order_postage = (TextView) convertView
						.findViewById(R.id.item_fragment_center_order_postage);
				myItem.fragment_center_order_is_cencal = (TextView) convertView
						.findViewById(R.id.fragment_center_order_is_cencal);
				myItem.fragment_center_order_is_over = (TextView) convertView
						.findViewById(R.id.fragment_center_order_is_over);
				myItem.fragment_center_order_apply_refunding = (TextView) convertView
						.findViewById(R.id.fragment_center_order_apply_refunding);
				myItem.tv_center_my_order_seller_order_sn = (TextView) convertView
						.findViewById(R.id.tv_center_my_order_seller_order_sn);
				convertView.setTag(myItem);
			} else {
				myItem = (CenterOrderOutsideItem) convertView.getTag();
			}

			BLCenterOder data = datas.get(position);// 非未付订单列表

			LogUtils.i("**************good--id****************" + data.getId());
			int showType = Integer.parseInt(data.getOrder_status());
			switch (showType) {
			case PDaiFu:
				myItem.fragment_center_order_pay_to.setVisibility(View.VISIBLE);
				myItem.fragment_center_order_cancel_order
						.setVisibility(View.VISIBLE);
				myItem.fragment_center_order_pay_again.setVisibility(View.GONE);
				myItem.fragment_center_order_shouhuo_commiont
						.setVisibility(View.GONE);
				myItem.fragment_center_order_apply_for_tuikuan_commiont
						.setVisibility(View.GONE);
				myItem.fragment_center_order_remind_fahuo
						.setVisibility(View.GONE);
				myItem.fragment_center_order_arbitration
						.setVisibility(View.GONE);
				myItem.fragment_center_order_is_cencal.setVisibility(View.GONE);
				myItem.fragment_center_order_is_over.setVisibility(View.GONE);
				myItem.fragment_center_order_apply_refunding
						.setVisibility(View.GONE);
				myItem.fragment_center_order_delayreceive
						.setVisibility(View.GONE);
				myItem.fragment_center_order_is_delaytime
						.setVisibility(View.GONE);

				break;

			case PYiFu:
				if ("0".equals(data.getRefund())) {
					myItem.fragment_center_order_apply_for_tuikuan_commiont
							.setVisibility(View.VISIBLE);
					if ("0".equals(data.getRemind_time())) {
						myItem.fragment_center_order_remind_fahuo
								.setVisibility(View.VISIBLE);
					} else {
						myItem.fragment_center_order_remind_fahuo
								.setVisibility(View.GONE);
					}

				} else {
					myItem.fragment_center_order_apply_for_tuikuan_commiont
							.setVisibility(View.GONE);
					myItem.fragment_center_order_remind_fahuo
							.setVisibility(View.GONE);
				}

				myItem.fragment_center_order_cancel_order
						.setVisibility(View.GONE);
				myItem.fragment_center_order_pay_to.setVisibility(View.GONE);
				myItem.fragment_center_order_pay_again.setVisibility(View.GONE);
				myItem.fragment_center_order_shouhuo_commiont
						.setVisibility(View.GONE);
				myItem.fragment_center_order_arbitration
						.setVisibility(View.GONE);
				myItem.fragment_center_order_is_cencal.setVisibility(View.GONE);
				myItem.fragment_center_order_is_over.setVisibility(View.GONE);
				myItem.fragment_center_order_apply_refunding
						.setVisibility(View.GONE);
				myItem.fragment_center_order_delayreceive
						.setVisibility(View.GONE);
				myItem.fragment_center_order_is_delaytime
						.setVisibility(View.GONE);

				break;

			case PDaiShou:
				// 延迟时间10天，只有10天后才显示延迟收货
				long delaytime = Long.parseLong(data.getCreate_time())
						+ (10 * 24 * 60 * 60);
				// 当前时间
				long nowtime = System.currentTimeMillis() / 1000;
				if (nowtime < delaytime) {

					myItem.fragment_center_order_shouhuo_commiont
							.setVisibility(View.VISIBLE);
					myItem.fragment_center_order_delayreceive
							.setVisibility(View.GONE);
					myItem.fragment_center_order_is_delaytime
							.setVisibility(View.GONE);

				} else {
					if ("0".equals(data.getDelaynumber())) {
						myItem.fragment_center_order_shouhuo_commiont
								.setVisibility(View.VISIBLE);
						myItem.fragment_center_order_delayreceive
								.setVisibility(View.VISIBLE);
						myItem.fragment_center_order_is_delaytime
								.setVisibility(View.GONE);
					} else {
						myItem.fragment_center_order_shouhuo_commiont
								.setVisibility(View.GONE);
						myItem.fragment_center_order_delayreceive
								.setVisibility(View.GONE);
						myItem.fragment_center_order_is_delaytime
								.setVisibility(View.VISIBLE);
					}
				}

				myItem.fragment_center_order_remind_fahuo
				.setVisibility(View.GONE);
				myItem.fragment_center_order_cancel_order
						.setVisibility(View.GONE);
				myItem.fragment_center_order_pay_again.setVisibility(View.GONE);

				myItem.fragment_center_order_pay_to.setVisibility(View.GONE);
				myItem.fragment_center_order_apply_for_tuikuan_commiont
						.setVisibility(View.GONE);

				myItem.fragment_center_order_arbitration
						.setVisibility(View.GONE);
				myItem.fragment_center_order_is_cencal.setVisibility(View.GONE);
				myItem.fragment_center_order_is_over.setVisibility(View.GONE);
				myItem.fragment_center_order_apply_refunding
						.setVisibility(View.GONE);
				break;

			case PTuiKuan:
				myItem.fragment_center_order_arbitration
						.setVisibility(View.GONE);
				myItem.fragment_center_order_cancel_order
						.setVisibility(View.GONE);
				myItem.fragment_center_order_pay_to.setVisibility(View.GONE);
				myItem.fragment_center_order_pay_again.setVisibility(View.GONE);
				myItem.fragment_center_order_shouhuo_commiont
						.setVisibility(View.GONE);
				myItem.fragment_center_order_apply_for_tuikuan_commiont
						.setVisibility(View.GONE);
				myItem.fragment_center_order_remind_fahuo
						.setVisibility(View.GONE);
				myItem.fragment_center_order_delayreceive
						.setVisibility(View.GONE);
				myItem.fragment_center_order_is_cencal.setVisibility(View.GONE);
				myItem.fragment_center_order_is_over.setVisibility(View.GONE);
				myItem.fragment_center_order_apply_refunding
						.setVisibility(View.VISIBLE);
				myItem.fragment_center_order_is_delaytime
						.setVisibility(View.GONE);
				break;

			case PZhongCai:
				myItem.fragment_center_order_arbitration
						.setVisibility(View.VISIBLE);
				myItem.fragment_center_order_cancel_order
						.setVisibility(View.GONE);
				myItem.fragment_center_order_pay_to.setVisibility(View.GONE);
				myItem.fragment_center_order_pay_again.setVisibility(View.GONE);
				myItem.fragment_center_order_shouhuo_commiont
						.setVisibility(View.GONE);
				myItem.fragment_center_order_apply_for_tuikuan_commiont
						.setVisibility(View.GONE);
				myItem.fragment_center_order_remind_fahuo
						.setVisibility(View.GONE);
				myItem.fragment_center_order_is_cencal.setVisibility(View.GONE);
				myItem.fragment_center_order_is_over.setVisibility(View.GONE);
				myItem.fragment_center_order_apply_refunding
						.setVisibility(View.GONE);
				myItem.fragment_center_order_delayreceive
						.setVisibility(View.GONE);
				myItem.fragment_center_order_is_delaytime
						.setVisibility(View.GONE);
				break;

			case PAgreeTuiKuan:
				myItem.fragment_center_order_cancel_order
						.setVisibility(View.GONE);
				myItem.fragment_center_order_pay_again.setVisibility(View.GONE);
				myItem.fragment_center_order_pay_to.setVisibility(View.GONE);
				myItem.fragment_center_order_shouhuo_commiont
						.setVisibility(View.GONE);
				myItem.fragment_center_order_apply_for_tuikuan_commiont
						.setVisibility(View.GONE);
				myItem.fragment_center_order_remind_fahuo
						.setVisibility(View.GONE);
				myItem.fragment_center_order_arbitration
						.setVisibility(View.GONE);
				myItem.fragment_center_order_is_cencal.setVisibility(View.GONE);
				myItem.fragment_center_order_is_over
						.setVisibility(View.VISIBLE);
				StrUtils.SetTxt(myItem.fragment_center_order_is_over, "退款已完成");
				myItem.fragment_center_order_apply_refunding
						.setVisibility(View.GONE);
				myItem.fragment_center_order_delayreceive
						.setVisibility(View.GONE);
				myItem.fragment_center_order_is_delaytime
						.setVisibility(View.GONE);
				break;

			case PClose:
				myItem.fragment_center_order_cancel_order
						.setVisibility(View.GONE);
				myItem.fragment_center_order_pay_again.setVisibility(View.GONE);
				myItem.fragment_center_order_pay_to.setVisibility(View.GONE);

				myItem.fragment_center_order_shouhuo_commiont
						.setVisibility(View.GONE);
				myItem.fragment_center_order_apply_for_tuikuan_commiont
						.setVisibility(View.GONE);
				myItem.fragment_center_order_remind_fahuo
						.setVisibility(View.GONE);
				myItem.fragment_center_order_arbitration
						.setVisibility(View.GONE);
				myItem.fragment_center_order_is_cencal.setVisibility(View.GONE);
				myItem.fragment_center_order_is_over
						.setVisibility(View.VISIBLE);
				StrUtils.SetTxt(myItem.fragment_center_order_is_over, "订单已完成");
				myItem.fragment_center_order_apply_refunding
						.setVisibility(View.GONE);
				myItem.fragment_center_order_delayreceive
						.setVisibility(View.GONE);
				myItem.fragment_center_order_is_delaytime
						.setVisibility(View.GONE);
				break;

			case PCancel:// 订单已取消
				myItem.fragment_center_order_cancel_order
						.setVisibility(View.GONE);
				myItem.fragment_center_order_pay_again.setVisibility(View.GONE);
				myItem.fragment_center_order_pay_to.setVisibility(View.GONE);

				myItem.fragment_center_order_shouhuo_commiont
						.setVisibility(View.GONE);
				myItem.fragment_center_order_apply_for_tuikuan_commiont
						.setVisibility(View.GONE);
				myItem.fragment_center_order_remind_fahuo
						.setVisibility(View.GONE);
				myItem.fragment_center_order_arbitration
						.setVisibility(View.GONE);
				myItem.fragment_center_order_is_cencal
						.setVisibility(View.VISIBLE);
				myItem.fragment_center_order_is_over.setVisibility(View.GONE);
				myItem.fragment_center_order_apply_refunding
						.setVisibility(View.GONE);
				myItem.fragment_center_order_delayreceive
						.setVisibility(View.GONE);
				myItem.fragment_center_order_is_delaytime
						.setVisibility(View.GONE);
				break;

			default:
				myItem.fragment_center_order_pay_again.setVisibility(View.GONE);
				myItem.fragment_center_order_pay_to.setVisibility(View.GONE);
				myItem.fragment_center_order_cancel_order
						.setVisibility(View.GONE);
				myItem.fragment_center_order_apply_for_tuikuan_commiont
						.setVisibility(View.GONE);
				myItem.fragment_center_order_remind_fahuo
						.setVisibility(View.GONE);
				myItem.fragment_center_order_arbitration
						.setVisibility(View.GONE);
				myItem.fragment_center_order_shouhuo_commiont
						.setVisibility(View.GONE);
				myItem.fragment_center_order_is_cencal.setVisibility(View.GONE);
				myItem.fragment_center_order_is_over.setVisibility(View.GONE);
				myItem.fragment_center_order_apply_refunding
						.setVisibility(View.GONE);
				myItem.fragment_center_order_delayreceive
						.setVisibility(View.GONE);
				myItem.fragment_center_order_is_delaytime
						.setVisibility(View.GONE);
				break;
			}
			StrUtils.SetTxt(myItem.tv_center_my_order_seller_order_sn,
					data.getSeller_order_sn());
			StrUtils.SetColorsTxt(BaseContext,
					myItem.fragment_center_order_shopname, R.color.grey, "卖家：",
					data.getSeller_name());
			StrUtils.SetTxt(myItem.item_fragment_center_order_allnum,
					String.format("共%1$s件商品", data.getNumber()));
			// float total_price = Float.parseFloat(data.getGoods_price())
			// + Float.parseFloat(data.getPostage());
			StrUtils.SetTxt(myItem.item_fragment_center_order_allmoney, String
					.format("%1$s元", StrUtils.SetTextForMony(data
							.getOrder_total_price())));
			float postageF = Float.parseFloat(data.getPostage());
			StrUtils.SetTxt(
					myItem.item_fragment_center_order_postage,
					postageF == 0.0f ? "(免邮费)" : String.format("(含运费%1$s元)",
							StrUtils.SetTextForMony(postageF + "")));

			myItem.item_fragment_center_order_ls
					.setAdapter(new CenterOrderInsideAdapter(
							R.layout.item_fragment_center_order_inside, datas
									.get(position).getGoods(), false));
			final int myItemPosition = position;
			// 商品Item的点击事件，跳转商品详情页面
			myItem.item_fragment_center_order_ls
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

			onClickEvent(myItem, data);

			return convertView;

		}

		/**
		 * 点击事件的处理
		 * 
		 * @param myItem
		 * @param blComment
		 */
		private void onClickEvent(CenterOrderOutsideItem myItem,
				final BLCenterOder blComment) {

			// 取消订单
			myItem.fragment_center_order_cancel_order
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							ShowCustomDialog("确认取消订单吗？", "取消", "确认",
									new IDialogResult() {
										@Override
										public void RightResult() {
											if (CheckNet(BaseContext))
												return;
											CancelMyOrder(
													blComment.getOrder_sn(),
													blComment.getMember_id());

										}

										@Override
										public void LeftResult() {
										}
									});

						}
					});

			// 再次购买
			myItem.fragment_center_order_pay_again
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

						}
					});
			// 去付款
			myItem.fragment_center_order_pay_to
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
			// 延迟收货
			myItem.fragment_center_order_delayreceive
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							ShowCustomDialog("每个商品只允许延期一次，延期为3天，确认延期收货吗？",
									"取消", "确认", new IDialogResult() {
										@Override
										public void RightResult() {
											if (CheckNet(BaseContext))
												return;
											DelayReceive(blComment
													.getSeller_order_sn(),
													blComment.getMember_id());
										}

										@Override
										public void LeftResult() {
										}
									});
						}
					});

			// 确认收货
			myItem.fragment_center_order_shouhuo_commiont
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (CheckNet(BaseContext))
								return;

							ShowCustomDialog("确认收货？", "取消", "确认",
									new IDialogResult() {
										@Override
										public void RightResult() {
											ConfirmShouhuo(blComment
													.getSeller_order_sn(),
													blComment.getMember_id());
										}

										@Override
										public void LeftResult() {
										}
									});

						}
					});
			// 申请退款
			myItem.fragment_center_order_apply_for_tuikuan_commiont
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(BaseContext,
									AApplyTuikuan.class);
							intent.putExtra("FromTag",
									AApplyTuikuan.Tag_From_Center_My_Order);
							intent.putExtra("seller_order_sn",
									blComment.getSeller_order_sn());
							PromptManager.SkipActivity((Activity) BaseContext,
									intent);
						}
					});
			// 提醒发货
			myItem.fragment_center_order_remind_fahuo
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (CheckNet(BaseContext))
								return;
							RemindSendOut(blComment.getSeller_order_sn(),
									blComment.getMember_id());
						}
					});

			// 仲裁
			myItem.fragment_center_order_arbitration
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

						}
					});

		}

		/**
		 * 外层的
		 */
		class CenterOrderOutsideItem {
			TextView tv_center_my_order_seller_order_sn;// 订单编号
			TextView fragment_center_order_shopname;// 店名
			TextView fragment_center_order_cancel_order;// 取消订单
			TextView fragment_center_order_pay_to;// 去付款
			TextView fragment_center_order_pay_again;// 再次购买
			TextView fragment_center_order_shouhuo_commiont;// 确认收货
			TextView fragment_center_order_apply_for_tuikuan_commiont;// 申请退款
			TextView fragment_center_order_remind_fahuo;// 提醒发货
			TextView fragment_center_order_arbitration;// 仲裁
			TextView fragment_center_order_delayreceive;// 延迟收货
			TextView fragment_center_order_is_cencal;// 订单已取消
			TextView fragment_center_order_is_over;// 订单已完成
			TextView fragment_center_order_is_delaytime;// 已延期收货
			TextView fragment_center_order_apply_refunding;// 申请退款中

			CompleteListView item_fragment_center_order_ls;

			TextView item_fragment_center_order_allnum;// 多少件商品
			TextView item_fragment_center_order_allmoney;// 所有费用
			TextView item_fragment_center_order_postage;// 运费

		}

	}

	/**
	 * 内层的ls的adapter
	 * 
	 * @author datutu
	 * 
	 */
	class CenterOrderInsideAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private int ResourceId;
		private boolean IsDaiFu;
		private List<BLDComment> datas = new ArrayList<BLDComment>();

		public CenterOrderInsideAdapter(int resourceId, List<BLDComment> list,
				boolean IsDaiFus) {
			super();

			this.inflater = LayoutInflater.from(BaseContext);
			this.ResourceId = resourceId;
			this.datas = list;
			this.IsDaiFu = IsDaiFus;
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
			CenterOrderInsideItem myItem = null;
			if (convertView == null) {
				myItem = new CenterOrderInsideItem();
				convertView = inflater.inflate(ResourceId, null);
				myItem.item_fragment_center_order_in_iv = (ImageView) convertView
						.findViewById(R.id.item_fragment_center_order_in_iv);
				myItem.item_fragment_center_order_in_name = (TextView) convertView
						.findViewById(R.id.item_fragment_center_order_in_name);
				myItem.item_fragment_center_order_in_price = (TextView) convertView
						.findViewById(R.id.item_fragment_center_order_in_price);
				myItem.item_fragment_center_order_in_number = (TextView) convertView
						.findViewById(R.id.item_fragment_center_order_in_number);
				

				convertView.setTag(myItem);
			} else {
				myItem = (CenterOrderInsideItem) convertView.getTag();
			}
			ImageLoaderUtil.Load2(datas.get(position).getGoods_cover(),
					myItem.item_fragment_center_order_in_iv,
					R.drawable.error_iv2);
			StrUtils.SetTxt(myItem.item_fragment_center_order_in_name, datas
					.get(position).getGoods_name());
			StrUtils.SetTxt(
					myItem.item_fragment_center_order_in_price,
					String.format("%1$s元", StrUtils.SetTextForMony(datas.get(
							position).getGoods_price())));
			StrUtils.SetTxt(myItem.item_fragment_center_order_in_number, String
					.format("X%1$s", datas.get(position).getGoods_number()));

			return convertView;

		}

		class CenterOrderInsideItem {
			ImageView item_fragment_center_order_in_iv;
			TextView item_fragment_center_order_in_name;
			TextView item_fragment_center_order_in_price;
			TextView item_fragment_center_order_in_number;
		}
	}

	// *************************************************************************
	/**
	 * @author Yihuihua 最外层的AP
	 */
	class CenterOrderNoPayAdapter extends BaseAdapter {

		private int ResourseId;
		private LayoutInflater inflater;
		List<BLCenterOder> datas = new ArrayList<BLCenterOder>();

		public CenterOrderNoPayAdapter(int ResourseId) {
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
		 * @param order_list
		 */
		public void RefreshData(List<BLCenterOder> order_list) {
			this.datas = order_list;
			this.notifyDataSetChanged();
		}

		/**
		 * 加载更多
		 */
		public void AddFrashData(List<BLCenterOder> order_list) {
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
			CenterOrderNoPayItem centerOrderNoPay = null;
			if (convertView == null) {
				centerOrderNoPay = new CenterOrderNoPayItem();
				convertView = inflater.inflate(ResourseId, null);
				centerOrderNoPay.tv_center_order_no_pay_order_sn = (TextView) convertView
						.findViewById(R.id.tv_center_order_no_pay_order_sn);
				centerOrderNoPay.fragment_center_order_no_pay_pay_to = (TextView) convertView
						.findViewById(R.id.fragment_center_order_no_pay_pay_to);
				centerOrderNoPay.fragment_center_order_no_pay_cancel_order = (TextView) convertView
						.findViewById(R.id.fragment_center_order_no_pay_cancel_order);
				centerOrderNoPay.item_fragment_center_order_no_pay_outside = (CompleteListView) convertView
						.findViewById(R.id.item_fragment_center_order_no_pay_outside);
				centerOrderNoPay.item_fragment_center_order_no_pay_allnum = (TextView) convertView
						.findViewById(R.id.item_fragment_center_order_no_pay_allnum);
				centerOrderNoPay.item_fragment_center_order_no_pay_allmoney = (TextView) convertView
						.findViewById(R.id.item_fragment_center_order_no_pay_allmoney);
				centerOrderNoPay.item_fragment_center_order_no_pay_postage = (TextView) convertView
						.findViewById(R.id.item_fragment_center_order_no_pay_postage);

				convertView.setTag(centerOrderNoPay);
			} else {
				centerOrderNoPay = (CenterOrderNoPayItem) convertView.getTag();
			}
			CenterOrderNoPayInsideAdapter centerOrderNoPayInside = new CenterOrderNoPayInsideAdapter(
					R.layout.item_center_order_no_pay_inside, datas.get(
							position).getSon_order());
			centerOrderNoPay.item_fragment_center_order_no_pay_outside
					.setAdapter(centerOrderNoPayInside);
			StrUtils.SetTxt(centerOrderNoPay.tv_center_order_no_pay_order_sn,
					datas.get(position).getOrder_sn());

			// float total_price =
			// Float.parseFloat(datas.get(position).getOrder_total_price())
			// + Float.parseFloat(datas.get(position).getPostage_money());
			StrUtils.SetTxt(
					centerOrderNoPay.item_fragment_center_order_no_pay_allmoney,
					String.format("%1$s元", StrUtils.SetTextForMony(datas.get(
							position).getOrder_total_price())));
			float postage_moneyF = Float.parseFloat(datas.get(position)
					.getPostage_money());
			StrUtils.SetTxt(
					centerOrderNoPay.item_fragment_center_order_no_pay_postage,
					postage_moneyF == 0.0f ? "(免邮费)" : String.format(
							"(含运费%1$s元)",
							StrUtils.SetTextForMony(postage_moneyF + "")));

			final BLCenterOder blComment = datas.get(position);
			// 去付款
			centerOrderNoPay.fragment_center_order_no_pay_pay_to
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
			centerOrderNoPay.fragment_center_order_no_pay_cancel_order
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							ShowCustomDialog("确认取消订单吗？", "取消", "确认",
									new IDialogResult() {
										@Override
										public void RightResult() {
											if (CheckNet(BaseContext))
												return;
											CancelMyOrder(
													blComment.getOrder_sn(),
													blComment.getMember_id());

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

	class CenterOrderNoPayInsideAdapter extends BaseAdapter {

		private int ResourseId;
		private LayoutInflater inflater;
		private List<BLDComment> secoud_datas = new ArrayList<BLDComment>();

		public CenterOrderNoPayInsideAdapter(int ResourseId,
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
				centerOrderNoPayInside.tv_center_order_no_pay_seller_name = (TextView) convertView
						.findViewById(R.id.tv_center_order_no_pay_seller_name);
				centerOrderNoPayInside.item_fragment_center_order_no_pay_inside = (CompleteListView) convertView
						.findViewById(R.id.item_fragment_center_order_no_pay_inside);
				convertView.setTag(centerOrderNoPayInside);
			} else {
				centerOrderNoPayInside = (CenterOrderNoPayInsideItem) convertView
						.getTag();
			}
			StrUtils.SetTxt(
					centerOrderNoPayInside.tv_center_order_no_pay_seller_name,
					String.format("卖家：%1$s", secoud_datas.get(position)
							.getSeller_name()));
			CenterOrderNoPayInnerMostAdapter centerOrderNoPayInnerMost = new CenterOrderNoPayInnerMostAdapter(
					R.layout.item_center_order_no_pay_innermost, secoud_datas
							.get(position).getGoods());
			centerOrderNoPayInside.item_fragment_center_order_no_pay_inside
					.setAdapter(centerOrderNoPayInnerMost);
			final int myItemPosition = position;
			centerOrderNoPayInside.item_fragment_center_order_no_pay_inside
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
			CenterOrderNoPayInnerMostItem centerOrderNoPayInnerMost = null;
			if (convertView == null) {
				centerOrderNoPayInnerMost = new CenterOrderNoPayInnerMostItem();
				convertView = inflater.inflate(ResourseId, null);
				centerOrderNoPayInnerMost.item_fragment_center_order_no_pay_in_iv = ViewHolder
						.get(convertView,
								R.id.item_fragment_center_order_no_pay_in_iv);
				centerOrderNoPayInnerMost.item_fragment_center_order_no_pay_in_name = ViewHolder
						.get(convertView,
								R.id.item_fragment_center_order_no_pay_in_name);
				centerOrderNoPayInnerMost.item_fragment_center_order_no_pay_in_price = ViewHolder
						.get(convertView,
								R.id.item_fragment_center_order_no_pay_in_price);
				centerOrderNoPayInnerMost.item_fragment_center_order_no_pay_in_number = ViewHolder
						.get(convertView,
								R.id.item_fragment_center_order_no_pay_in_number);
				convertView.setTag(centerOrderNoPayInnerMost);
				ImageLoaderUtil
						.Load2(innerMost_data.get(position).getGoods_cover(),
								centerOrderNoPayInnerMost.item_fragment_center_order_no_pay_in_iv,
								R.drawable.error_iv2);
			} else {
				centerOrderNoPayInnerMost = (CenterOrderNoPayInnerMostItem) convertView
						.getTag();
			}

			StrUtils.SetTxt(
					centerOrderNoPayInnerMost.item_fragment_center_order_no_pay_in_name,
					innerMost_data.get(position).getGoods_name());
			StrUtils.SetTxt(
					centerOrderNoPayInnerMost.item_fragment_center_order_no_pay_in_price,
					String.format("%1$s元", StrUtils
							.SetTextForMony(innerMost_data.get(position)
									.getGoods_money())));
			StrUtils.SetTxt(
					centerOrderNoPayInnerMost.item_fragment_center_order_no_pay_in_number,
					String.format("X%1$s", innerMost_data.get(position)
							.getGoods_number()));
			return convertView;
		}

	}

	/**
	 * @author Yihuihua 最外层列表的Holder
	 */
	class CenterOrderNoPayItem {

		public TextView tv_center_order_no_pay_order_sn;// 订单号
		public TextView fragment_center_order_no_pay_pay_to;// 去付款
		public TextView fragment_center_order_no_pay_cancel_order;// 取消订单
		public CompleteListView item_fragment_center_order_no_pay_outside;// 第一层的订单列表
		public TextView tv_center_my_order_no_pay_seller_order_sn;// 订单编号
		public TextView item_fragment_center_order_no_pay_allnum;// 有多少件商品
		public TextView item_fragment_center_order_no_pay_allmoney;// 总价
		public TextView item_fragment_center_order_no_pay_postage;// 运费

	}

	/**
	 * @author Yihuihua 第二层列表Holder
	 */
	class CenterOrderNoPayInsideItem {
		public TextView tv_center_order_no_pay_seller_name;// 买家名称
		public CompleteListView item_fragment_center_order_no_pay_inside;// 第二层的订单列表
	}

	/**
	 * @author Yihuihua 最里层的Holder
	 */
	class CenterOrderNoPayInnerMostItem {
		public ImageView item_fragment_center_order_no_pay_in_iv;// 商品图标
		public TextView item_fragment_center_order_no_pay_in_name;// 商品title
		public TextView item_fragment_center_order_no_pay_in_price;// 商品价格
		public TextView item_fragment_center_order_no_pay_in_number;// 商品个数
	}

	// *************************************************************************

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (CheckNet(BaseContext))
			return;
		BLCenterOder bl_data = null;
		
		
		
		if (10 != Ket_Tage) {
			int count = centerOrderOutsideAdapter.getCount();
//			if(count > 0){
				bl_data = (BLCenterOder) centerOrderOutsideAdapter
						.getItem(position - 1);

				int order_status = Integer.parseInt(bl_data.getOrder_status());
				Intent intent = null;
				if (10 == order_status) {
					intent = new Intent(BaseContext,
							ACenterMyOrderNoPayDetail.class);
					intent.putExtra("order_sn", bl_data.getOrder_sn());

				} else {
					intent = new Intent(BaseContext, ACenterMyOrderDetail.class);

					intent.putExtra("Key_TageStr", order_status);
				}

				intent.putExtra("member_id", bl_data.getMember_id());
				intent.putExtra("seller_order_sn", bl_data.getSeller_order_sn());
				PromptManager.SkipActivity((Activity) BaseContext, intent);
//			}
			
		} else {
			int count = centerOrderNoPayAdapter.getCount();
//			if(count > 0){
				bl_data = (BLCenterOder) centerOrderNoPayAdapter
						.getItem(position - 1);
				Intent intent = new Intent(BaseContext,
						ACenterMyOrderNoPayDetail.class);
				intent.putExtra("order_sn", bl_data.getOrder_sn());
				intent.putExtra("member_id", bl_data.getMember_id());
				intent.putExtra("seller_order_sn", bl_data.getSeller_order_sn());
				PromptManager.SkipActivity((Activity) BaseContext, intent);
//			}
			
		}

		

	}

	@Override
	public void onRefresh() {
		last_id = "";
		IData(REFRESHING, order_status);
	}

	@Override
	public void onLoadMore() {
		IData(LOADMOREING, order_status);
	}

	/**
	 * 接收事件
	 * 
	 * @param event
	 */

	public void OnGetMessage(BMessage event) {
		int messageType = event.getMessageType();
		if (messageType == event.Tage_Center_Order_Updata) {
			last_id = "";
			IData(INITIALIZE, order_status);
		}
		if (Ket_Tage == event.getMessageType()) {
			last_id = "";
			IData(INITIALIZE, order_status);
		}

		if (messageType == event.Tage_To_Pay_Updata) {
			last_id = "";
			IData(INITIALIZE, order_status);
		}
		if (714 == messageType) {
			Ket_Tage = 10;
//			order_status = "10";
		}
		if (713 == messageType) {

			Ket_Tage = 0;
//			order_status = "0";

		}
		

	}

	@Override
	public void onClick(View v) {
		last_id = "";
		IData(REFRESHING, order_status);
	}

	@Override
	public void onDestroy() {
		try {
			EventBus.getDefault().unregister(this);
		} catch (Exception e) {
		}
		super.onDestroy();
	}
}
