package io.vtown.WeiTangApp.ui.title;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.shopbus.BLShopBusIn;
import io.vtown.WeiTangApp.bean.bcomment.easy.shopbus.BLShopBusOut;
import io.vtown.WeiTangApp.bean.bcomment.easy.shopbus.BShopBus;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.AddAndSubView;
import io.vtown.WeiTangApp.comment.view.AddAndSubView.OnNumChangeListener;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.onConfirmClick;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.oncancelClick;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.comment.view.listview.LListView.IXListViewListener;
import io.vtown.WeiTangApp.comment.view.pop.PShopBus;
import io.vtown.WeiTangApp.comment.view.pop.PShopBus.BusSelecListener;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.ACommentList;
import io.vtown.WeiTangApp.ui.title.account.AOderBeing;
import io.vtown.WeiTangApp.ui.title.mynew.ANew;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.jauker.widget.BadgeView;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-12 下午4:15:52
 * @author 购物车
 */
public class AShopBus extends ATitleBase implements IXListViewListener {
	/**
	 * 下边的按钮
	 */
	private ImageView sopbus_bottom_select_iv;
	/**
	 * 下边的价格
	 */
	private TextView sopbus_bottom_sum_mony;
	/**
	 * 下边的数量
	 */
	private TextView sopbus_bottom_sum_mumber;
	/**
	 * 结算
	 */
	private TextView sopbus_bottom_jiesuan;
	/**
	 * ls
	 */
	private LListView shopbus_ls;
	/**
	 * ls对应的ap
	 */
	private BusAdapter busAdapter;
	/**
	 * 标记购物车是否全选
	 */
	private boolean IsAllSelectIv = false;
	/**
	 * 标记是否删除
	 */
	private boolean IsJeiSuan = true;
	/**
	 * 需要显示的
	 */
	private LinearLayout shopbus_show_lay;
	/**
	 * 错误时候需要显示的
	 */
	private View shopbus_nodata_lay;
	/**
	 * 下边结算的布局
	 */
private LinearLayout shopbus_down_lay;
	/**
	 * 当前的列表筛选分类
	 */
	private String Channel = "";
	/**
	 * 用户信息
	 */
	private BUser user_Get;

	/**
	 * 用户的type1标识显示零售；；； 2标识显示采购
	 */
	private int TypeShow = -1;
	/**
	 * 是否是普通
	 */
	private boolean IsPu;
//	private boolean IsAfterFrist = false;

	private int AllNumber = 0;

	// private BadgeView ShopNumberBadgeView;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_shopbus);
		user_Get = Spuit.User_Get(BaseContext);
		EventBus.getDefault().register(this, "getEventBusMsg", BMessage.class);
		IBase();
		SetTitleHttpDataLisenter(this);
		IData(LOAD_INITIALIZE);
	}

//	@Override
//	protected void onResume() {
//		super.onResume();
////		IData(LOAD_INITIALIZE);
////		IsAfterFrist = true;
//	}

	/**
	 * 加载购物车列表数据 type标识 是否是删除完后的刷新
	 */
	private void IData(int Type) {
		if (Type != LOAD_REFRESHING)
		PromptManager.showtextLoading(BaseContext,"加载中");
//		PromptManager.showtextLoading();
		;// (boolean frash, int Type) {
			// 获取数据钱 先要收到设置未处于全选状态********处于结算状态***********
		IsAllSelectIv = false;
		IsJeiSuan = true;
		left_txt.setVisibility(View.GONE);
		right_iv.setImageResource(!IsJeiSuan ? R.drawable.shoubus_ok// R.drawable.center_iv2
				: R.drawable.lajixiang_iv);
		SetIvSelect(sopbus_bottom_select_iv, IsAllSelectIv);
		right_iv.setVisibility(View.GONE);
		// 开始获取数据
//		if (Type != LOAD_REFRESHING)// 不是下拉刷新时候才需要 界面显示为空
//			IDataView(shopbus_show_lay, shopbus_nodata_lay, NOVIEW_INITIALIZE);
		// if (!frash)
		// PromptManager.showtextLoading(BaseContext, "努力加载");
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("member_id", user_Get.getId());
		// map.put("channel", Channel);
		map.put("channel", "");
		FBGetHttpData(map, Constants.ShopBus_Ls, Method.GET, 0, Type);
	}

	/**
	 * 删除商品操作
	 */
	private void IBase() {
		InitTile();
		shopbus_down_lay= (LinearLayout) findViewById(R.id.shopbus_down_lay);

		shopbus_show_lay = (LinearLayout) findViewById(R.id.shopbus_show_lay);
		shopbus_nodata_lay = findViewById(R.id.shopbus_nodata_lay);
		shopbus_nodata_lay.setOnClickListener(this);

		sopbus_bottom_select_iv = (ImageView) findViewById(R.id.sopbus_bottom_select_iv);
		sopbus_bottom_sum_mony = (TextView) findViewById(R.id.sopbus_bottom_sum_mony);
		sopbus_bottom_jiesuan = (TextView) findViewById(R.id.sopbus_bottom_jiesuan);
		sopbus_bottom_sum_mumber = (TextView) findViewById(R.id.sopbus_bottom_sum_mumber);

		sopbus_bottom_select_iv.setOnClickListener(this);
		sopbus_bottom_jiesuan.setOnClickListener(this);

		shopbus_ls = (LListView) findViewById(R.id.shopbus_ls);
		shopbus_ls.setPullRefreshEnable(true);
		shopbus_ls.setPullLoadEnable(false);
		shopbus_ls.setXListViewListener(this);
		shopbus_ls.hidefoot();
	}

	@Override
	protected void InitTile() {
		SetTitleTxt("购物车");
		SetRightRightIv(R.drawable.new1);
		SetRightIv(R.drawable.lajixiang_iv);
		SetLeftText("");
		TextPaint tp = left_txt.getPaint();
		tp.setFakeBoldText(true);
		left_txt.setOnClickListener(this);// 筛选按钮
		left_txt.setVisibility(View.GONE);
		right_iv.setVisibility(View.GONE);
		left_txt.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.select_white_to_fen));
		// left_txt.setBackgroundResource(R.drawable.select_white_to_fen1);\
		boolean imMessage = Spuit.IMMessage_Get(BaseContext);
		if (imMessage) {
			right_right_iv.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_xiaoxitixing_nor));
		} else {
			right_right_iv.setImageDrawable(getResources().getDrawable(
					R.drawable.new1));
		}

		right_iv.setOnClickListener(this);// 垃圾箱按钮
		right_right_iv.setOnClickListener(this);// 消息
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {// type=0是代表购物车列表数据
																	// ；；type=1代表删除
																	// ；；type

		switch (Data.getHttpResultTage()) {
		case 0:// 代表购物车列表数据
//			if (StrUtils.isEmpty(Data.getHttpResultStr())) {
//				DataError(Constants.SucessToError, Data.getHttpLoadType());
//				right_iv.setVisibility(View.GONE);
//				shopbus_down_lay.setVisibility(View.GONE);
//				IDataView(shopbus_show_lay, shopbus_nodata_lay, NOVIEW_ERROR);
//				return;
//			}
			IDataView(shopbus_show_lay, shopbus_nodata_lay, NOVIEW_RIGHT);
			if (Data.getHttpLoadType() == LOAD_REFRESHING) {
				shopbus_ls.stopRefresh();
			}

			BShopBus bComment = JSON.parseObject(Data.getHttpResultStr(),
					BShopBus.class);
			// AllNumber=0;

			// 开始解析***************************************************************
			// JSONObject Obj = null;
			// try {
			// Obj = new JSONObject(Data.getHttpResultStr());
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }
			//
			// if (StrUtils.JsonContainKey(Obj, "PT")) {
			// try {
			// bComment.setPT(JSON.parseArray(Obj.getString("PT"),
			// BLComment.class));
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }
			// }
			// if (StrUtils.JsonContainKey(Obj, "CG")) {
			// try {
			// bComment.setCG(JSON.parseArray(Obj.getString("CG"),
			// BLComment.class));
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }
			// }
			// 开始解析***************************************************************
			if(bComment.getPT() == null && bComment.getCG() == null){// 没有普通也没有采购的

				right_iv.setVisibility(View.GONE);
				shopbus_down_lay.setVisibility(View.GONE);
				IDataView(shopbus_show_lay, shopbus_nodata_lay, NOVIEW_ERROR);
				AllNumber = 0;
				Spuit.ShopBusNumber_Save(getApplicationContext(),0);
				Send(AllNumber);
			}
			if (bComment.getPT() != null && bComment.getCG() == null) {// 只有普通的没有采购的
				busAdapter = new BusAdapter(R.layout.item_shopbus_out);
				shopbus_ls.setAdapter(busAdapter);
				busAdapter.FrashData(bComment.getPT());
				IsPu = true;
				left_txt.setText("零售商品");

				left_txt.setVisibility(View.GONE);
				left_txt.setClickable(false);
				right_iv.setVisibility(View.VISIBLE);
				AllNumber = 0;

				for (int i = 0; i < bComment.getPT().size(); i++) {

					AllNumber = AllNumber
							+ bComment.getPT().get(i).getList().size();

				}
				Spuit.ShopBusNumber_Save(BaseContext,AllNumber);
				Send(AllNumber);
				shopbus_down_lay.setVisibility(View.VISIBLE);
			}
			if (bComment.getPT() == null && bComment.getCG() != null) {// 只有采购的没有普通的

				busAdapter = new BusAdapter(R.layout.item_shopbus_out);
				shopbus_ls.setAdapter(busAdapter);
				busAdapter.FrashData(bComment.getCG());
				IsPu = false;
				left_txt.setText("采购商品");
				left_txt.setVisibility(View.GONE);
				left_txt.setClickable(false);
				right_iv.setVisibility(View.VISIBLE);

				AllNumber = 0;

				for (int i = 0; i < bComment.getCG().size(); i++) {

					AllNumber = AllNumber
							+ bComment.getCG().get(i).getList().size();

				}
				Spuit.ShopBusNumber_Save(BaseContext,AllNumber);
				Send(AllNumber);
				shopbus_down_lay.setVisibility(View.VISIBLE);
			}

			if (bComment.getPT() != null && bComment.getCG() != null) {// 既有采购也有普通
				left_txt.setVisibility(View.VISIBLE);
				busAdapter = new BusAdapter(R.layout.item_shopbus_out);
				shopbus_ls.setAdapter(busAdapter);
				if (TypeShow == -1)
					TypeShow = 1;
				if (TypeShow == 1) {
					StrUtils.SetTxt(left_txt, "零售商品");
					busAdapter.FrashData(bComment.getPT());
					IsPu = true;
				}
				if (TypeShow == 2) {
					StrUtils.SetTxt(left_txt, "采购商品");
					busAdapter.FrashData(bComment.getCG());
					IsPu = false;
				}
				left_txt.setClickable(true);
				right_iv.setVisibility(View.VISIBLE);

				// AllNumber = bComment.getCG().size() +
				// bComment.getPT().size();

				AllNumber = 0;
				for (int i = 0; i < bComment.getPT().size(); i++) {
					AllNumber = AllNumber
							+ bComment.getPT().get(i).getList().size();
				}

				for (int i = 0; i < bComment.getCG().size(); i++) {
					AllNumber = AllNumber
							+ bComment.getCG().get(i).getList().size();
				}
				Spuit.ShopBusNumber_Save(BaseContext,AllNumber);
				Send(AllNumber);
				shopbus_down_lay.setVisibility(View.VISIBLE);
			}

			break;
		case 1:// 删除购物车
			IsJeiSuan = !IsJeiSuan;
			right_iv.setImageResource(!IsJeiSuan ? R.color.transparent
					: R.drawable.lajixiang_iv);
			StrUtils.SetTxt(sopbus_bottom_jiesuan, IsJeiSuan ? "结算" : "删除");
			PromptManager.ShowCustomToast(BaseContext, "删除成功");
			IData(LOAD_INITIALIZE);
			break;
		case 4:// 修改规格商品的个数
				// PromptManager.ShowCustomToast(BaseContext, "修改成功");
			break;
		default:
			break;
		}

	}

	@Override
	protected void DataError(String error, int LoadTyp) {

		if (LoadTyp != LOAD_LOADMOREING)
			IDataView(shopbus_show_lay, shopbus_nodata_lay, NOVIEW_ERROR);
		if (LoadTyp == LOAD_REFRESHING) {
			shopbus_ls.stopRefresh();
		}

		PromptManager.ShowCustomToast(BaseContext, StrUtils.NullToStr(error));
	}

	@Override
	protected void NetConnect() {
		NetError.setVisibility(View.GONE);

		// 无网络==》有网络
		if (busAdapter == null || busAdapter.getCount() == 0)
			IData(LOAD_INITIALIZE);

	}

	@Override
	protected void NetDisConnect() {
		NetError.setVisibility(View.VISIBLE);

	}

	@Override
	protected void SetNetView() {
		SetNetStatuse(NetError);

	}

	/**
	 * 外层的Ap
	 */
	private class BusAdapter extends BaseAdapter {

		/**
		 * 填充器
		 */
		private LayoutInflater inflater;
		/**
		 * 资源id
		 */
		private int ResourceId;
		/**
		 * 记录内层所有列表的boolen标记
		 */
		private List<Boolean> Outbooleans = new ArrayList<Boolean>();
		/**
		 * 数据
		 */
		private List<BLShopBusOut> datas = new ArrayList<BLShopBusOut>();
		/**
		 * 记录下所有的ap
		 */
		private List<BusInAdapter> BusInAdapters = new ArrayList<BusInAdapter>();

		public BusAdapter(int resourceId) {
			super();

			this.inflater = LayoutInflater.from(BaseContext);
			ResourceId = resourceId;

		}

		/**
		 * 刷新时候调用 即使是加载更多 也需要先获取到原有数据然后合并数据后再刷新操作
		 * 
		 * @param dass
		 */
		public void FrashData(List<BLShopBusOut> dass) {
			this.datas = dass;

			for (int i = 0; i < dass.size(); i++) {

				getOutbooleans().add(false);

				for (int k = 0; k < datas.get(i).getList().size(); k++) {
					if (!IsJeiSuan) {// 删除状态
					} else {
						if (!IsPu) {// 是采购的全部通过
							datas.get(i).setIsCanSelct(true);
						} else {// 是普通的进行判断
							if (datas.get(i).getList().get(k).getIs_sales()
									.equals("1"))
								datas.get(i).setIsCanSelct(true);
						}
					}

				}
				BusInAdapter data = new BusInAdapter(this, datas.get(i)
						.getList(), i);
				BusInAdapters.add(data);
			}
			if (dass.size() > 0) {
				right_iv.setVisibility(View.VISIBLE);
			}
			ShowAllMony();
			this.notifyDataSetChanged();
		}

		/**
		 * 获取数据源的列表
		 */
		public List<BLShopBusOut> GetDatas() {
			return datas;
		}

		/**
		 * 获取Boolen源的列表
		 */
		public List<Boolean> getOutbooleans() {
			return Outbooleans;
		}

		/**
		 * 获取InAp的列表
		 */
		public List<BusInAdapter> GetInAp() {
			return BusInAdapters;
		}

		/**
		 * 设置是否全选
		 */
		public void AllSelect(boolean IsAllSelect) {
			for (int i = 0; i < getCount(); i++) {

				getOutbooleans().set(i, IsAllSelect);
				for (int j = 0; j < BusInAdapters.get(i).getCount(); j++) {
					BusInAdapters.get(i).getInSelect().set(j, IsAllSelect);
					BusInAdapters.get(i).notifyDataSetChanged();
				}
			}
			ShowAllMony();
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
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			BusOutItem myItem = null;
			if (convertView == null) {
				myItem = new BusOutItem();
				convertView = inflater.inflate(ResourceId, null);
				myItem.item_shopbus_ls = (CompleteListView) convertView
						.findViewById(R.id.item_shopbus_ls);
				myItem.item_shopbus_out_shoptxt = ViewHolder.get(convertView,
						R.id.item_shopbus_out_shoptxt);
				myItem.item_shopbus_out_select_iv = ViewHolder.get(convertView,
						R.id.item_shopbus_out_select_iv);
				myItem.item_shopbus_out_iv_tag = ViewHolder.get(convertView,
						R.id.item_shopbus_out_iv_tag);

				myItem.item_shopbus_out_shopinf_lay = (LinearLayout) convertView
						.findViewById(R.id.item_shopbus_out_shopinf_lay);
				myItem.item_shopbus_ls.setAdapter(BusInAdapters.get(arg0));
				convertView.setTag(myItem);
			} else {
				myItem = (BusOutItem) convertView.getTag();
			}

			final BLShopBusOut daBlComment = datas.get(arg0);
			if (!IsPu) {
				datas.get(arg0).setIsCanSelct(true);
			}
			myItem.item_shopbus_out_iv_tag.setImageResource(daBlComment
					.getIs_brand().equals("1") ? R.drawable.shopbus_item_log1
					: R.drawable.shopbus_item_log2);

			myItem.item_shopbus_ls
					.setOnItemClickListener(new OnItemClickListener() {// 点击进入商品详情

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							BLShopBusIn dBldCssomment = (BLShopBusIn) arg0
									.getItemAtPosition(arg2);
							PromptManager.SkipActivity(
									BaseActivity,
									new Intent(BaseContext, AGoodDetail.class)
											.putExtra("goodid",
													dBldCssomment.getGoods_id()));

						}
					});
			StrUtils.SetTxt(myItem.item_shopbus_out_shoptxt,
					daBlComment.getSeller_name());
			if (!IsJeiSuan) {// 删除
				SetIvSelect(myItem.item_shopbus_out_select_iv, getOutbooleans()
						.get(arg0));

				myItem.item_shopbus_out_select_iv
						.setOnClickListener(new OutApClick(BusInAdapters
								.get(arg0), !getOutbooleans().get(arg0),
								myItem.item_shopbus_out_select_iv, this, arg0));
			} else {

				if (daBlComment.isIsCanSelct()) {
					SetIvSelect(myItem.item_shopbus_out_select_iv,
							getOutbooleans().get(arg0));

					myItem.item_shopbus_out_select_iv
							.setOnClickListener(new OutApClick(BusInAdapters
									.get(arg0), !getOutbooleans().get(arg0),
									myItem.item_shopbus_out_select_iv, this,
									arg0));

				} else {

				}
			}
			myItem.item_shopbus_out_shopinf_lay
					.setOnClickListener(new OnClickListener() {// 点击进入店铺
						@Override
						public void onClick(View v) {

							PromptManager
									.SkipActivity(
											BaseActivity,
											new Intent(
													BaseActivity,
													daBlComment.getIs_brand()
															.equals("1") ? ABrandDetail.class
															: AShopDetail.class)
													.putExtra(
															BaseKey_Bean,
															new BComment(
																	daBlComment
																			.getSeller_id(),
																	daBlComment
																			.getSeller_name())));
						}
					});
			// // Outbooleans.set(arg0, !Outbooleans.get(arg0));
			return convertView;
		}

		class BusOutItem {
			ImageView item_shopbus_out_iv_tag;

			LinearLayout item_shopbus_out_shopinf_lay;
			CompleteListView item_shopbus_ls;
			TextView item_shopbus_out_shoptxt;
			ImageView item_shopbus_out_select_iv;
		}
	}

	/**
	 * 外层点击时候需要的点击事件
	 * 
	 * @author datutu
	 * 
	 */
	class OutApClick implements OnClickListener {
		/**
		 * 内层的ap
		 */
		BusInAdapter busInAdapter;
		/**
		 * true标识 需要勾选
		 */
		// boolean IsSelect;
		/**
		 * 外层的iamgview
		 */

		ImageView MYvIEW;
		/**
		 * 需要把通过ap调用方法改变外层的列表
		 */
		BusAdapter busAdapter;
		/**
		 * 上边方法需要用到postion
		 */
		int Postion;

		public OutApClick(BusInAdapter busInAdapter, boolean isselect,
				ImageView VV, BusAdapter mBusAdapter, int mpostion) {
			super();
			this.busInAdapter = busInAdapter;
			this.MYvIEW = VV;
			// this.IsSelect = isselect;
			this.busAdapter = mBusAdapter;
			this.Postion = mpostion;
		}

		@Override
		public void onClick(View arg0) {
			SetIvSelect(MYvIEW, !busAdapter.getOutbooleans().get(Postion));
			busAdapter.getOutbooleans().set(Postion,
					!busAdapter.getOutbooleans().get(Postion));
			for (int i = 0; i < busInAdapter.getCount(); i++) {
				busInAdapter.getInSelect().set(i,
						busAdapter.getOutbooleans().get(Postion));
			}
			busInAdapter.notifyDataSetChanged();
			ShowAllMony();
			checkAll(!busAdapter.getOutbooleans().contains(false));
		}
	}

	/**
	 * 内部的adapter
	 * 
	 */
	private class BusInAdapter extends BaseAdapter {

		/**
		 * 填充器
		 */
		private LayoutInflater inflater;
		/**
		 * 记录外层adapter的AP
		 */
		private BusAdapter MyOutAdapter;
		/**
		 * 记录标识
		 */
		private List<Boolean> Inselected = new ArrayList<Boolean>();
		/**
		 * 数据
		 */
		private List<BLShopBusIn> datas;
		/**
		 * 记录外层的Item的位置
		 */
		private int OutPostion;

		public BusInAdapter(BusAdapter OutAdapter, List<BLShopBusIn> dss,
				int outpostion) {
			super();

			this.inflater = LayoutInflater.from(BaseContext);
			this.MyOutAdapter = OutAdapter;
			this.datas = dss;
			this.OutPostion = outpostion;
			for (int j = 0; j < dss.size(); j++) {
				getInSelect().add(false);
			}
		}

		/**
		 * 获取boolean的列表
		 */
		public List<Boolean> getInSelect() {
			return Inselected;
		}

		/**
		 * 获取列表
		 */
		public List<BLShopBusIn> GetInLs() {
			return datas;
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
		public View getView(final int arg0, View convertView, ViewGroup arg2) {
			BusInItem myItem = null;
			if (convertView == null) {
				myItem = new BusInItem();
				convertView = inflater.inflate(R.layout.item_shopbus_in, null);
				myItem.item_shopbus_in_guige = ViewHolder.get(convertView,
						R.id.item_shopbus_in_guige);
				myItem.item_shopbus_in_name = ViewHolder.get(convertView,
						R.id.item_shopbus_in_name);
				myItem.item_shopbus_in_price = ViewHolder.get(convertView,
						R.id.item_shopbus_in_price);
				myItem.item_shopbus_in_log = ViewHolder.get(convertView,
						R.id.item_shopbus_in_log);
				myItem.item_shopbus_in_number = (AddAndSubView) convertView
						.findViewById(R.id.item_shopbus_in_number);
				myItem.item_shopbus_in_select_log = ViewHolder.get(convertView,
						R.id.item_shopbus_in_select_log);
				myItem.item_shopbus_in_log_tag = ViewHolder.get(convertView,
						R.id.item_shopbus_in_log_tag);
				myItem.item_shopbus_in_sell_statue = ViewHolder.get(
						convertView, R.id.item_shopbus_in_sell_statue);
				ImageLoaderUtil.Load2(datas.get(arg0).getCover(),
						myItem.item_shopbus_in_log, R.drawable.error_iv2);
				convertView.setTag(myItem);
			} else {
				myItem = (BusInItem) convertView.getTag();
			}

			BLShopBusIn daBldComment = datas.get(arg0);

			// IsPu
			myItem.item_shopbus_in_log_tag.setVisibility(IsPu ? View.GONE
					: View.VISIBLE);
			StrUtils.SetTxt(myItem.item_shopbus_in_name,
					daBldComment.getGoods_name());

			StrUtils.SetTxt(myItem.item_shopbus_in_guige,
					daBldComment.getAttr_name());

			StrUtils.SetTxt(
					myItem.item_shopbus_in_price,
					"￥"
							+ StrUtils.SetTextForMony(daBldComment
									.getGoods_price()));

			if (!IsPu) {
				myItem.item_shopbus_in_number.SetMinNumber(10);

				// 设置可选
				daBldComment.setIs_sales("1");
			}
			myItem.item_shopbus_in_number
					.setOnNumChangeListener(new OnNumChangeListener() {
						@Override
						public void onNumChange(View view, int num) {

							if (num != StrUtils.toInt(datas.get(arg0)
									.getGoods_num())) {
								AlterGoodsNumber(datas.get(arg0).getGoods_id(),
										num, datas.get(arg0).getCid());
							}
							datas.get(arg0).setGoods_num(num + "");
							ShowAllMony();
						}
					});
			myItem.item_shopbus_in_number.setNum(StrUtils.toInt(daBldComment
					.getGoods_num()));

			if (!IsJeiSuan) {// 删除状态
				myItem.item_shopbus_in_select_log.setClickable(true);
				SetIvSelect(myItem.item_shopbus_in_select_log, getInSelect()
						.get(arg0));
				myItem.item_shopbus_in_select_log
						.setOnClickListener(new InSelcetClickListener(myItem,
								getInSelect().get(arg0), getInSelect(), arg0,
								MyOutAdapter, OutPostion));
				myItem.item_shopbus_in_sell_statue.setVisibility(View.GONE);
				myItem.item_shopbus_in_name.setTextColor(getResources()
						.getColor(R.color.black));
				myItem.item_shopbus_in_price.setTextColor(getResources()
						.getColor(R.color.app_fen));
				myItem.item_shopbus_in_select_log.setVisibility(View.VISIBLE);
			} else {
				if (daBldComment.getIs_sales().equals("1")) {// 可以状态
					myItem.item_shopbus_in_select_log.setClickable(true);
					SetIvSelect(myItem.item_shopbus_in_select_log,
							getInSelect().get(arg0));
					myItem.item_shopbus_in_select_log
							.setOnClickListener(new InSelcetClickListener(
									myItem, getInSelect().get(arg0),
									getInSelect(), arg0, MyOutAdapter,
									OutPostion));
					myItem.item_shopbus_in_sell_statue.setVisibility(View.GONE);
					myItem.item_shopbus_in_name.setTextColor(getResources()
							.getColor(R.color.black));
					myItem.item_shopbus_in_price.setTextColor(getResources()
							.getColor(R.color.app_fen));
					myItem.item_shopbus_in_select_log
							.setVisibility(View.VISIBLE);

				} else {// 不可以
					myItem.item_shopbus_in_sell_statue
							.setVisibility(View.VISIBLE);
					myItem.item_shopbus_in_sell_statue.setText("已下架");
					myItem.item_shopbus_in_name.setTextColor(getResources()
							.getColor(R.color.grey));
					myItem.item_shopbus_in_price.setTextColor(getResources()
							.getColor(R.color.grey));
					myItem.item_shopbus_in_select_log.setClickable(false);
					myItem.item_shopbus_in_select_log
							.setVisibility(View.INVISIBLE);
				}
			}
			return convertView;
		}

		class BusInItem {
			ImageView item_shopbus_in_log_tag;// 实库虚库的log

			TextView item_shopbus_in_sell_statue;
			TextView item_shopbus_in_guige;
			TextView item_shopbus_in_name;
			TextView item_shopbus_in_price;
			ImageView item_shopbus_in_log;
			ImageView item_shopbus_in_select_log;
			AddAndSubView item_shopbus_in_number;

		}

		/**
		 * 二级item的选中Listener
		 */

		class InSelcetClickListener implements OnClickListener {
			/**
			 * 内层AP的boolean 列表
			 */
			List<Boolean> booleans;
			/**
			 * 当前的item
			 */
			BusInItem busInItem;
			/**
			 * 位置
			 */
			int Postion;
			/**
			 * 每点击内部一次 外部需要检测是否全选OR全不选择
			 */
			BusAdapter MyOutAdapter;
			/**
			 * 外层的位置
			 */
			private int OutPostion;

			public InSelcetClickListener(BusInItem item, Boolean isselect,
					List<Boolean> ss, int postion, BusAdapter MyOutA,
					int OutPostions) {
				super();
				this.busInItem = item;
				this.OutPostion = OutPostions;
				this.booleans = ss;
				this.Postion = postion;
				this.MyOutAdapter = MyOutA;
			}

			@Override
			public void onClick(View v) {
				booleans.set(Postion, !booleans.get(Postion));
				SetIvSelect(busInItem.item_shopbus_in_select_log,
						booleans.get(Postion));

				if (!booleans.contains(false)) {// 全部选择
					MyOutAdapter.getOutbooleans().set(OutPostion, true);
				} else {// 部分选择或者全部未选择
					MyOutAdapter.getOutbooleans().set(OutPostion, false);
				}
				MyOutAdapter.notifyDataSetChanged();
				ShowAllMony();
				checkAll(!MyOutAdapter.getOutbooleans().contains(false));
			}
		}
	}

	/**
	 * 设置被勾选状态的方法
	 */
	private void SetIvSelect(ImageView vv, boolean IsSelect) {
		vv.setImageResource(IsSelect ? R.drawable.quan_select_3
				: R.drawable.quan_select_1);
	}

	/**
	 * 当全部勾选后需要把activity中选择全部进行自动勾选
	 * 
	 * boolean表示全部勾选置为
	 */
	public void checkAll(boolean checked) {
		IsAllSelectIv = checked;
		SetIvSelect(sopbus_bottom_select_iv, checked);
	}

	@Override
	protected void MyClick(View V) {
		switch (V.getId()) {
		case R.id.left_txt:// 筛选
			ShowSelect(left_txt);
			break;
		case R.id.sopbus_bottom_select_iv:// 全选择
			AllSelect(!IsAllSelectIv);
			break;
		case R.id.sopbus_bottom_jiesuan:// 结算
			Account(IsJeiSuan);

			break;
		case R.id.right_iv:// 点击删除
			IsJeiSuan = !IsJeiSuan;
			busAdapter.FrashData(busAdapter.GetDatas());
			right_iv.setImageResource(!IsJeiSuan ? R.drawable.shoubus_ok// R.drawable.center_iv2
					: R.drawable.lajixiang_iv);
			StrUtils.SetTxt(sopbus_bottom_jiesuan, IsJeiSuan ? "结算" : "删除");

			break;
		case R.id.shopbus_nodata_lay:
			IData(LOAD_INITIALIZE);
			break;
		case R.id.right_right_iv:
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					ANew.class));
			// EventBus.getDefault().post(new BMessage(BMessage.IM_MSG_READ));
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * TRUE标识需要进行全部选择操作 ;;;false标识进行全部取消操作
	 */
	private void AllSelect(boolean b) {
		busAdapter.AllSelect(b);
		IsAllSelectIv = !IsAllSelectIv;
		SetIvSelect(sopbus_bottom_select_iv, b);
	}

	/**
	 * 点击结算
	 */
	private void Account(boolean IsAccount) {
		String AccountStr = "";
		String DeleteStr = "";
		// List<BLComment> ShopsLs = new ArrayList<BLComment>();

		List<BLShopBusIn> daComments = new ArrayList<BLShopBusIn>();
		for (int i = 0; i < busAdapter.getCount(); i++) {// 店铺的循环
			List<BLShopBusIn> inls = busAdapter.GetInAp().get(i).GetInLs();
			for (int j = 0; j < busAdapter.GetInAp().get(i).getCount(); j++) {
				if (!inls.get(j).getIs_sales().equals("1")) {
					if (IsJeiSuan)
						continue;
				}
				if (busAdapter.GetInAp().get(i).getInSelect().get(j)) {// 规格的循环
					daComments
							.add(busAdapter.GetInAp().get(i).GetInLs().get(j));

				}
			}
		}
		// daComments 现在已经保存数据 下边测试数据正确性
		for (BLShopBusIn h : daComments) {
			AccountStr = AccountStr + h.getCid() + ":" + h.getGoods_num() + ",";
			DeleteStr = DeleteStr + h.getCid() + ",";
		}
		if (IsAccount) {// 结算按钮操作
			if (!StrUtils.isEmpty(AccountStr)) {// 已经选择了
				AccountStr = AccountStr.substring(0, AccountStr.length() - 1);
			} else {// 没有选择
				PromptManager.ShowCustomToastLong(BaseContext, "亲,你还没选择结算的商品哦~"
						+ AccountStr);
				return;
			}
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					AOderBeing.class).putExtra("accountstr", AccountStr));
		} else {// 删除按钮操作
			if (!StrUtils.isEmpty(AccountStr)) {// 已经选择了
				DeleteStr = DeleteStr.substring(0, DeleteStr.length() - 1);
			} else {// 没有选择
				PromptManager.ShowCustomToastLong(BaseContext, "亲,你还没选择删除的商品哦~"
						+ AccountStr);
				return;
			}
			// PromptManager.ShowCustomToast(BaseContext, "删除的Cid数据：" +
			// DeleteStr);
			DeleteBus(DeleteStr);

			return;
		}

	}

	/**
	 * 计算总金额并展示金额
	 */
	private void ShowAllMony() {
		Float AllMony = 0f;
		int GoodNumber = 0;
		for (int i = 0; i < busAdapter.getCount(); i++) {// 店铺的循环

			List<BLShopBusIn> inls = busAdapter.GetInAp().get(i).GetInLs();
			for (int j = 0; j < busAdapter.GetInAp().get(i).getCount(); j++) {

				if (busAdapter.GetInAp().get(i).getInSelect().get(j)) {// 规格的循环
					// daComments.add(busAdapter.GetInAp().get(i).GetInLs()
					// .get(j));
					if (!inls.get(j).getIs_sales().equals("1"))
						continue;
					AllMony = AllMony
							+ Float.valueOf(busAdapter.GetInAp().get(i)
									.GetInLs().get(j).getGoods_price())
							* Integer.valueOf(busAdapter.GetInAp().get(i)
									.GetInLs().get(j).getGoods_num());
					GoodNumber += Integer.valueOf(busAdapter.GetInAp().get(i)
							.GetInLs().get(j).getGoods_num());
				}
			}
		}

		StrUtils.SetTxt(sopbus_bottom_sum_mony,
				"￥" + StrUtils.SetTextForMony(String.valueOf(AllMony)));
		StrUtils.SetTxt(sopbus_bottom_sum_mumber,
				String.format("(%s件)", String.valueOf(GoodNumber)));
		//
	}

	/**
	 * 左上按钮点击筛选弹出框
	 */
	private void ShowSelect(View VV) {

		// left_txt
		PShopBus mBus = new PShopBus(BaseContext, TypeShow);
		mBus.GetSelectReult(new BusSelecListener() {
			@Override
			public void GetResult(int type) {

				if (type == PShopBus.Type_LingShou) {// 零售
					StrUtils.SetTxt(left_txt, "零售商品");
					Channel = "PT";

					TypeShow = 1;
					IData(LOAD_INITIALIZE);
				}
				if (type == PShopBus.Type_CaiGou) {// 采购
					StrUtils.SetTxt(left_txt, "采购商品");
					Channel = "CG";
					TypeShow = 2;
					IData(LOAD_INITIALIZE);
				}
				return;
			}
		});
		mBus.showAsDropDown(VV, -20, 20);

	}

	/**
	 * 删除购物车
	 */
	private void DeleteBus(final String dString) {

		ShowCustomDialog("是否删除选中宝贝", getResources().getString(R.string.cancle),
				getResources().getString(R.string.queding),
				new IDialogResult() {

					@Override
					public void RightResult() {
						Delete(dString);
					}

					@Override
					public void LeftResult() {
					}
				});

		// final CustomDialog dialog = new CustomDialog(BaseContext,
		// R.style.mystyle, R.layout.customdialog, 0, "取消", "确定");
		// dialog.show();
		// dialog.setTitleText("是否删除选中产品");
		// dialog.setConfirmListener(new onConfirmClick() {
		// @Override
		// public void onConfirmCLick(View v) {
		// Delete(dString);
		// dialog.dismiss();
		//
		// }
		// });
		// dialog.setcancelListener(new oncancelClick() {
		// @Override
		// public void oncancelClick(View v) {
		// dialog.dismiss();
		// }
		// });

	}

	/**
	 * 删除选中的商品
	 * 
	 * @param Cids
	 */
	private void Delete(String Cids) {
		PromptManager.showLoading(BaseContext);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("member_id", user_Get.getId());
		map.put("cid", Cids);
		FBGetHttpData(map, Constants.ShopBus_Delete, Method.DELETE, 1,
				LOAD_INITIALIZE);
	}

	/**
	 * 修改G购物车末一个规格商品的数量
	 */
	private void AlterGoodsNumber(String goods_id, int goods_num, String cid) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("goods_id", goods_id);
		map.put("member_id", user_Get.getId());
		map.put("goods_num", goods_num + "");
		map.put("cid", cid);
		FBGetHttpData(map, Constants.ShopBus_GoodAlter, Method.PUT, 4,
				LOAD_INITIALIZE);
	}

	public void getEventBusMsg(BMessage event) {
		int messageType = event.getMessageType();
		switch (messageType) {
			case BMessage.Shop_Frash:

				IData(LOAD_REFRESHING);
				break;
		case BMessage.IM_Have_MSG:

			right_right_iv.setImageDrawable(getResources().getDrawable(
					R.drawable.ic_xiaoxitixing_nor));
			break;

		case BMessage.IM_MSG_READ:
			// badgeView.setBadgeCount(0);
			right_right_iv.setImageDrawable(getResources().getDrawable(
					R.drawable.new1));
			Spuit.IMMessage_Set(BaseContext, false);
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
	public void onRefresh() {
		IData(LOAD_REFRESHING);
	}

	@Override
	public void onLoadMore() {

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
