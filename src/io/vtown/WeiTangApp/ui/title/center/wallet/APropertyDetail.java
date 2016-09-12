package io.vtown.WeiTangApp.ui.title.center.wallet;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.wallet.BLAPropertyDetail;
import io.vtown.WeiTangApp.bean.bcomment.easy.wallet.BLAPropertyList;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.view.DotView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.comment.view.listview.LListView.IXListViewListener;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore.Video;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-19 下午9:53:58 资产明细页面
 */
public class APropertyDetail extends ATitleBase implements IXListViewListener {

	/**
	 * 明细列表
	 */
	private LListView lv_property_detail_list;
	private PopupWindow popupWindow;
	/**
	 * Ap
	 */
	private PropertyAdapter LsAp;

	/**
	 * 当前的页数
	 */
	private int CurrentPage = 0;

	// 操作类别 1提现，2帐户充值，3销售，4购物
	private static final int TAGE_ALL = 0;
	private static final int TAGE_WITHDRAW = 1;
	private static final int TAGE_RECHARGE = 5;
	private static final int TAGE_SELl = 3;
	private static final int TAGE_SHOPPING = 4;
	private int CurrentType = TAGE_ALL;
	/**
	 * 用户相关信息
	 */
	private BUser user_Get;
	/**
	 * 获取数据成功页面
	 */
	private LinearLayout center_my_property_detail_outlay;
	/**
	 * 获取数据失败页面
	 */
	private View center_my_property_detail_nodata_lay;
	/**
	 * 获取到的数据
	 */
	private List<BLAPropertyList> dattaa;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_center_wallet_property_detail);
		user_Get = Spuit.User_Get(getApplicationContext());
		IView();
		ICache();
		SetTitleHttpDataLisenter(this);
		IData(CurrentPage, TAGE_ALL, LOAD_INITIALIZE);
	}

	private void IData(int Page, int type, int LoadType) {
		if (LoadType == LOAD_INITIALIZE)
			PromptManager.showtextLoading(BaseContext,
					getResources()
							.getString(R.string.xlistview_header_hint_loading));

		HashMap<String, String> map = new HashMap<String, String>();
		// map.put("page", Page + "");
		map.put("member_id", user_Get.getId());
		map.put("last_id", "");
		map.put("type", type + "");
		FBGetHttpData(map, Constants.ZiJinJiLu, Method.GET, 0, LoadType);

	}
	
	private void ICache(){
		String center_Wallet_Property = CacheUtil.Center_Wallet_Property_Get(getApplicationContext());
		if(StrUtils.isEmpty(center_Wallet_Property)){
			return;
		}
		try {
			dattaa = JSON.parseArray(center_Wallet_Property, BLAPropertyList.class);
		} catch (Exception e) {
			return;
		}
		if(CurrentType == TAGE_ALL){
			LsAp.FrashData(dattaa);
		}
		
	}

	private void IView() {
		
		center_my_property_detail_outlay = (LinearLayout) findViewById(R.id.center_my_property_detail_outlay);
		center_my_property_detail_nodata_lay = findViewById(R.id.center_my_property_detail_nodata_lay);
		IDataView(center_my_property_detail_outlay, center_my_property_detail_nodata_lay, NOVIEW_INITIALIZE);

		lv_property_detail_list = (LListView) findViewById(R.id.lv_property_detail_list);
		

		LsAp = new PropertyAdapter(R.layout.item_property_detail_outside);
		lv_property_detail_list.setAdapter(LsAp);
		
		lv_property_detail_list.setPullRefreshEnable(true);
		lv_property_detail_list.setPullLoadEnable(true);
		lv_property_detail_list.setXListViewListener(this);
		lv_property_detail_list.hidefoot();
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.title_property_detail_all));
		SetRightText(getResources().getString(R.string.txt_filter));
		right_txt.setOnClickListener(this);
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		if (StrUtils.isEmpty(Data.getHttpResultStr())){
			PromptManager.ShowCustomToast(BaseContext, "记录为空");
			if(LOAD_INITIALIZE == Data.getHttpLoadType()){
				if(CurrentType == TAGE_ALL){
					CacheUtil.Center_Wallet_Property_Save(getApplicationContext(), Data.getHttpResultStr());
				}	
			}
			
			return;
		}
			
		dattaa = new ArrayList<BLAPropertyList>();
		try {

			dattaa = JSON.parseArray(Data.getHttpResultStr(), BLAPropertyList.class);
		} catch (Exception e) {
			DataError("解析错误", 1);
		}
		
		IDataView(center_my_property_detail_outlay, center_my_property_detail_nodata_lay, NOVIEW_RIGHT);
		
		if(CurrentType == TAGE_ALL){
			CacheUtil.Center_Wallet_Property_Save(getApplicationContext(), Data.getHttpResultStr());
		}

		switch (Data.getHttpLoadType()) {
		case LOAD_INITIALIZE:// 初始化
			LsAp.FrashData(dattaa);
			break;
		case LOAD_REFRESHING:// 刷新数据

			lv_property_detail_list.stopRefresh();
			LsAp.FrashData(dattaa);
			if (dattaa.size() == Constants.PageSize)
				lv_property_detail_list.ShowFoot();
			if (dattaa.size() < Constants.PageSize)
				lv_property_detail_list.hidefoot();
			break;
		case LOAD_LOADMOREING:// 加载更多
			lv_property_detail_list.stopLoadMore();
			LsAp.AddFrashData(dattaa);
			if (dattaa.size() == Constants.PageSize)
				lv_property_detail_list.ShowFoot();
			if (dattaa.size() < Constants.PageSize)
				lv_property_detail_list.hidefoot();
			break;
		default:
			break;
		}

	}

	@Override
	protected void DataError(String error, int LoadTyp) {
		PromptManager.ShowCustomToast(BaseContext, error);
		
		switch (LoadTyp) {
		case LOAD_INITIALIZE:
			IDataView(center_my_property_detail_outlay, center_my_property_detail_nodata_lay, NOVIEW_ERROR);
			break;
		case LOAD_REFRESHING:// 刷新数据
			lv_property_detail_list.stopRefresh();

			break;
		case LOAD_LOADMOREING:// 加载更多
			lv_property_detail_list.stopLoadMore();

			break;
		}
	}

	@Override
	protected void NetConnect() {
		NetError.setVisibility(View.GONE);
		CurrentPage = 0;
		IData(CurrentPage, CurrentType, LOAD_REFRESHING);
		
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
		case R.id.right_txt:
			IPopupWindow(V);
			break;
			
		case R.id.tv_select_all://全部
			if(CurrentType != TAGE_ALL){
				SetTitleTxt(getResources().getString(R.string.title_property_detail_all));
				CurrentType = TAGE_ALL;
				IData(CurrentPage, CurrentType, LOAD_INITIALIZE);
				LsAp.Clearn();
			}
			popupWindow.dismiss();
			break;
		case R.id.tv_buy_good:// 购买
			if(CurrentType != TAGE_SHOPPING){
				SetTitleTxt(getResources().getString(R.string.title_property_detail_buy_good));
				CurrentType = TAGE_SHOPPING;
				IData(CurrentPage, TAGE_SHOPPING, LOAD_INITIALIZE);
				LsAp.Clearn();
			}
			popupWindow.dismiss();
			break;

		case R.id.tv_top_up:// 退款
			if(CurrentType != TAGE_RECHARGE){
				SetTitleTxt(getResources().getString(R.string.title_property_detail_top_up));
				CurrentType = TAGE_RECHARGE;
				IData(CurrentPage, TAGE_RECHARGE, LOAD_INITIALIZE);
				LsAp.Clearn();
			}
			popupWindow.dismiss();
			break;

		case R.id.tv_withdraw:// 提现
			if(CurrentType != TAGE_WITHDRAW){
				SetTitleTxt(getResources().getString(R.string.title_property_detail_withdraw));
				CurrentType = TAGE_WITHDRAW;
				IData(CurrentPage, TAGE_WITHDRAW, LOAD_INITIALIZE);
				LsAp.Clearn();
			}
			popupWindow.dismiss();
			break;

		case R.id.tv_sell_record:// 销售
			if(CurrentType != TAGE_SELl){
				SetTitleTxt(getResources().getString(R.string.title_property_detail_sell_record));
				CurrentType = TAGE_SELl;
				IData(CurrentPage, TAGE_SELl, LOAD_INITIALIZE);
				LsAp.Clearn();
			}
			popupWindow.dismiss();
			break;

		}
	}

	/**
	 * 控制PopupWindow
	 */
	private void IPopupWindow(View V) {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		} else {
			showPop(V);
		}
	}

	private void showPop(View V) {

		View view = View.inflate(BaseContext, R.layout.pop_property_filter,
				null);
		TextView tv_buy_good = (TextView) view.findViewById(R.id.tv_buy_good);
		TextView tv_top_up = (TextView) view.findViewById(R.id.tv_top_up);
		TextView tv_withdraw = (TextView) view.findViewById(R.id.tv_withdraw);
		TextView tv_select_all = (TextView) view.findViewById(R.id.tv_select_all);
		TextView tv_sell_record = (TextView) view
				.findViewById(R.id.tv_sell_record);
		tv_buy_good.setOnClickListener(this);
		tv_top_up.setOnClickListener(this);
		tv_withdraw.setOnClickListener(this);
		tv_sell_record.setOnClickListener(this);
		tv_select_all.setOnClickListener(this);

		popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		popupWindow.setBackgroundDrawable(dw);
		popupWindow.showAsDropDown(V, 0,30);
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	class PropertyAdapter extends BaseAdapter {

		/**
		 * 填充器
		 */
		private LayoutInflater inflater;
		/**
		 * 资源id
		 */
		private int ResourceId;
		/**
		 * 数据
		 */
		private List<BLAPropertyList> datas = new ArrayList<BLAPropertyList>();

		public PropertyAdapter(int resourceId) {
			super();
			this.inflater = LayoutInflater.from(BaseContext);
			ResourceId = resourceId;
		}

		public void Clearn() {
			datas=new ArrayList<BLAPropertyList>();
			notifyDataSetChanged();
		}

		/**
		 * 刷新数据
		 * 
		 * @param dattaa
		 */
		public void FrashData(List<BLAPropertyList> dattaa) {
			datas.clear();
			this.datas = dattaa;
			this.notifyDataSetChanged();
		}

		/**
		 * 加载更多
		 */
		public void AddFrashData(List<BLAPropertyList> dattaa) {
			this.datas.addAll(datas);
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
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			OutSideItem item = null;
			if (arg1 == null) {
				arg1 = inflater.inflate(ResourceId, null);

				item = new OutSideItem();
				item.tv_month = ViewHolder.get(arg1, R.id.tv_month);

				item.lv_property_list_inside = ViewHolder.get(arg1,
						R.id.lv_property_list_inside);
				arg1.setTag(item);
			} else {
				item = (OutSideItem) arg1.getTag();
			}
			List<BLAPropertyDetail> capitalDetail = datas.get(arg0).getList();
			item.lv_property_list_inside.setAdapter(new InsedeAdapter(
					capitalDetail));
			item.tv_month.setText(datas.get(arg0).getMonth());

			return arg1;
		}

		class OutSideItem {
			TextView tv_month;
			CompleteListView lv_property_list_inside;
		}

	}

	class InsedeAdapter extends BaseAdapter {
		List<BLAPropertyDetail> data = new ArrayList<BLAPropertyDetail>();

		public InsedeAdapter(List<BLAPropertyDetail> capitalDetail) {
			this.data = capitalDetail;
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
			PropertyItem item = null;
			if (arg1 == null) {
				item = new PropertyItem();
				arg1 = View.inflate(BaseContext, R.layout.item_property_detail,
						null);
				item.tv_data = ViewHolder.get(arg1, R.id.tv_data);
				item.tv_time = ViewHolder.get(arg1, R.id.tv_time);
				item.tv_record_desc = ViewHolder.get(arg1, R.id.tv_record_desc);
				item.tv_trade_state = ViewHolder.get(arg1, R.id.tv_trade_state);
				item.tv_record_money = ViewHolder.get(arg1,
						R.id.tv_record_money);
				item.dot_view = (DotView) arg1.findViewById(R.id.dot_view);

				item.tv_data = (TextView) arg1.findViewById(R.id.tv_data);

				arg1.setTag(item);
			} else {
				item = (PropertyItem) arg1.getTag();
			}

			item.dot_view.SetDotColor(getRandomColor());

			StrUtils.SetTxt(item.tv_data, data.get(arg0).getDateStr());
			StrUtils.SetTxt(item.tv_time, data.get(arg0).getDate());
			StrUtils.SetTxt(item.tv_record_desc, data.get(arg0).getTitle());
			StrUtils.SetTxt(item.tv_data, data.get(arg0).getDateStr());

			item.tv_data.setText(data.get(arg0).getDateStr());
			item.tv_time.setText(data.get(arg0).getDate());
			item.tv_record_desc.setText(data.get(arg0).getTitle());
			String type = data.get(arg0).getType();
			int type_Int = Integer.parseInt(type);
			switch (type_Int) {
			case 1:
				item.tv_trade_state.setText("提现");
				break;

			case 2:
				item.tv_trade_state.setText("帐户充值");
				break;

			case 3:
				item.tv_trade_state.setText("销售");
				break;

			case 4:
				item.tv_trade_state.setText("退款");
				break;

			default:
				item.tv_trade_state.setText("其它");
				break;
			}

			item.tv_record_money.setText(String.format("￥%1$s元", StrUtils.SetTextForMony(data.get(arg0).getPrice())));

			return arg1;

		}

		class PropertyItem {
			TextView tv_data, tv_time, tv_record_desc, tv_trade_state,
					tv_record_money;
			DotView dot_view;
		}

		/**
		 * 生成随机颜色
		 * 
		 * @return
		 */
		private int getRandomColor() {

			Random random = new Random();
			int r = random.nextInt(200) + 56;
			int g = random.nextInt(200) + 56;
			int b = random.nextInt(200) + 56;
			return Color.rgb(r, g, b);
		}

	}

	@Override
	public void onRefresh() {
		CurrentPage = 0;
		IData(CurrentPage, CurrentType, LOAD_REFRESHING);
	}

	@Override
	public void onLoadMore() {
		CurrentPage += 1;

		IData(CurrentPage, CurrentType, LOAD_LOADMOREING);
	}

}
