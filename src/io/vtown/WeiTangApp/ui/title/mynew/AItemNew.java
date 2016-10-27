package io.vtown.WeiTangApp.ui.title.mynew;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DateUtils;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.comment.view.listview.LListView.IXListViewListener;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.afragment.ACenterOder;
import io.vtown.WeiTangApp.ui.afragment.AShopGoodManger;
import io.vtown.WeiTangApp.ui.afragment.AShopOderManage;
import io.vtown.WeiTangApp.ui.afragment.AShopPurchase;
import io.vtown.WeiTangApp.ui.comment.order.ACenterMyOrder;
import io.vtown.WeiTangApp.ui.comment.order.AShopOrderManager;
import io.vtown.WeiTangApp.ui.comment.order.AShopPurchaseOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-14 下午8:51:57
 * 
 */
public class AItemNew extends ATitleBase implements IXListViewListener {
	/**
	 * 消息列表
	 */
	private LListView mynew_Itemlist;
	/**
	 * 对应的Ap
	 */
	private MyItemMew_Ap myMew_Ap;

	private String LastId = "";

	/**
	 * 获取到的source_Type
	 */
	private String SourceType;
	/**
	 * 用户信息
	 */
	private BUser user_Get;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_new_in_ls);
		user_Get = Spuit.User_Get(BaseContext);
		IBund();
		BaseV();
		SetTitleHttpDataLisenter(this);
		IData(LastId, SourceType, LOAD_INITIALIZE);
	}

	private void IBund() {
		if (null == getIntent().getExtras()
				|| !getIntent().getExtras().containsKey("newtype")) {
			BaseActivity.finish();
			return;
		}

		SourceType = getIntent().getStringExtra("newtype");

	}

	private void IData(String LastId, String Type, int loadInitialize) {
		if (loadInitialize == LOAD_INITIALIZE) {
			PromptManager.showtextLoading(BaseContext, getResources()
					.getString(R.string.loading));
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("member_id", user_Get.getId());
		map.put("source_type", Type + "");
		map.put("lastid", LastId);
		map.put("pagesize", Constants.PageSize + "");
		FBGetHttpData(map, Constants.My_Item_New_ls, Method.GET, 0,
				loadInitialize);
	}

	/**
	 * 删除消息
	 */
	private void DeletByType(String message_id) {

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("member_id", user_Get.getMember_id());
		map.put("message_id", message_id);
		FBGetHttpData(map, Constants.NewItemDeletByType, Method.DELETE, 20,
				LOAD_REFRESHING);
	}

	private void BaseV() {
		mynew_Itemlist = (LListView) findViewById(R.id.mynew_new_inlist);
		mynew_Itemlist.setPullLoadEnable(true);
		mynew_Itemlist.setPullRefreshEnable(true);
		mynew_Itemlist.setXListViewListener(this);
		mynew_Itemlist.hidefoot();
		myMew_Ap = new MyItemMew_Ap(R.layout.item_my_new);
		mynew_Itemlist.setAdapter(myMew_Ap);

		mynew_Itemlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				BNew itemdata = (BNew) arg0.getItemAtPosition(arg2);
				// PromptManager.SkipActivity(BaseActivity, new Intent(
				// BaseActivity, AItemNew.class));
				int Type = StrUtils.toInt(SourceType);
				switch (Type) {
				case 1:// 消息
					PromptManager.SkipActivity(BaseActivity, new Intent(
							BaseActivity, ANewInf.class).putExtra(
							ANewInf.Tage_key, itemdata));
					break;
				case 2:// 支付
					break;
				case 3:// 订单
					int action = StrUtils.toInt(itemdata.getAction());
					switch (action) {
					case Constants.ACTION_PT_ORDER:// 普通下单
						Intent shopOrderIntent = new Intent(BaseContext,
								AShopOrderManager.class);
						shopOrderIntent.putExtra("order_stutas", 3);
						shopOrderIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(shopOrderIntent);

						EventBus.getDefault().post(
								new BMessage(BMessage.Tage_New_Kill));
						BaseActivity.finish();
						break;
					case Constants.ACTION_CG_ORDER:// 采购下单
						Intent shopGoodIntent = new Intent(BaseContext,
								AShopOrderManager.class);
						shopGoodIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(shopGoodIntent);

						EventBus.getDefault().post(
								new BMessage(BMessage.Tage_New_Kill));
						BaseActivity.finish();
						break;
					case Constants.ACTION_TO_PAY:// 付款
						Intent shopOrderIntent6 = new Intent(BaseContext,
								AShopOrderManager.class);
						shopOrderIntent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(shopOrderIntent6);

						EventBus.getDefault().post(
								new BMessage(BMessage.Tage_New_Kill));
						BaseActivity.finish();
						break;
					case Constants.ACTION_CG_SEND:// 采购订单发货
						Intent shopGoodIntent1 = new Intent(BaseContext,
								AShopPurchaseOrder.class);
						shopGoodIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(shopGoodIntent1);

						EventBus.getDefault().post(
								new BMessage(BMessage.Tage_New_Kill));
						BaseActivity.finish();
						break;
					case Constants.ACTION_PT_SEND:// 普通订单发货
						Intent shopOrderIntent1 = new Intent(BaseContext,
								ACenterMyOrder.class);
						shopOrderIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(shopOrderIntent1);

						EventBus.getDefault().post(
								new BMessage(BMessage.Tage_New_Kill));
						BaseActivity.finish();
						break;
					case Constants.ACTION_CG_REFUND:// 采购订单同意退款
						Intent shopOrderIntent2 = new Intent(BaseContext,
								AShopPurchaseOrder.class);
						shopOrderIntent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(shopOrderIntent2);

						EventBus.getDefault().post(
								new BMessage(BMessage.Tage_New_Kill));
						BaseActivity.finish();
						break;
					case Constants.ACTION_PT_REFUND:// 普通订单同意退款
						// Intent shopOrderIntent3 = new Intent(BaseContext,
						// AShopOderManage.class);
						// shopOrderIntent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						// | Intent.FLAG_ACTIVITY_CLEAR_TOP);
						// startActivity(shopOrderIntent3);
						//
						// EventBus.getDefault().post(
						// new BMessage(BMessage.Tage_New_Kill));
						// BaseActivity.finish();
						Intent shopOrderIntent3 = new Intent(BaseContext,
								ACenterMyOrder.class);
						shopOrderIntent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(shopOrderIntent3);

						EventBus.getDefault().post(
								new BMessage(BMessage.Tage_New_Kill));
						BaseActivity.finish();
						break;
					case Constants.ACTION_CG_UNREFUND:// 采购订单拒绝退款
						Intent shopOrderIntent4 = new Intent(BaseContext,
								AShopPurchaseOrder.class);
						shopOrderIntent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(shopOrderIntent4);

						EventBus.getDefault().post(
								new BMessage(BMessage.Tage_New_Kill));
						BaseActivity.finish();
						break;

					case Constants.ACTION_PT_UNREFUND:// 采购订单拒绝退款
					case Constants.ACTION_PT_MODIFY_PRICE://订单修改价格
						Intent shopOrderIntent5 = new Intent(BaseContext,
								ACenterMyOrder.class);
						shopOrderIntent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(shopOrderIntent5);

						EventBus.getDefault().post(
								new BMessage(BMessage.Tage_New_Kill));
						BaseActivity.finish();
						break;
						
					
						
						

					default:
						break;
					}

					break;

				default:
					break;
				}

			}
		});

		mynew_Itemlist
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						final BNew mBNew = (BNew) arg0.getItemAtPosition(arg2);

						ShowCustomDialog("是否该删除消息?", "取消", "确定",
								new IDialogResult() {
									@Override
									public void RightResult() {
										DeletByType(mBNew.getId());// "system_message_id":
									}

									@Override
									public void LeftResult() {

									}
								});

						return true;
					}
				});
	}

	@Override
	protected void InitTile() {

		int Type = StrUtils.toInt(SourceType);
		switch (Type) {
		case 1:// 消息
			SetTitleTxt("系统消息");
			break;
		case 2:// 支付
			SetTitleTxt("支付消息");
			break;
		case 3:// 订单
			SetTitleTxt("订单消息");
			break;
		default:
			SetTitleTxt("我的消息");
			break;
		}
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {

		switch (Data.getHttpResultTage()) {
		case 0:// 列表数据源
			if (StrUtils.isEmpty(Data.getHttpResultStr())) {
				// 数据加载完毕的操作

				DataError("无更多数据", Data.getHttpLoadType());
				return;
			}

			List<BNew> comments = new ArrayList<BNew>();
			try {
				comments = JSON.parseArray(Data.getHttpResultStr(), BNew.class);
			} catch (Exception e) {
				DataError("数据格式错误", Data.getHttpLoadType());
				return;
			}
			// LastId=comments.get(comments.size()-1)
			LastId = comments.get(comments.size() - 1).getId();
			switch (Data.getHttpLoadType()) {
			case LOAD_INITIALIZE:
				myMew_Ap.Refrsh(comments);
				break;
			case LOAD_REFRESHING:
				myMew_Ap.Refrsh(comments);
				mynew_Itemlist.stopRefresh();

				break;
			case LOAD_LOADMOREING:
				mynew_Itemlist.stopLoadMore();
				myMew_Ap.AddRefrsh(comments);
				break;
			default:
				break;
			}

			break;
		case 20:// 删除数据
			PromptManager.ShowCustomToast(BaseContext, "删除成功");
			LastId = "";
			IData(LastId, SourceType, 9);
			break;
		default:
			break;
		}

	}

	@Override
	protected void DataError(String error, int LoadType) {

		switch (LoadType) {
		case LOAD_REFRESHING:
			mynew_Itemlist.stopRefresh();
			break;
		case LOAD_LOADMOREING:
			mynew_Itemlist.stopLoadMore();
			PromptManager.ShowCustomToast(BaseContext, error);
			break;
		case LOAD_INITIALIZE:
			PromptManager.ShowCustomToast(BaseContext, error);
			break;
		default:
			break;
		}
	}

	private class MyItemMew_Ap extends BaseAdapter {//

		private LayoutInflater inflater;
		private int ResourceId;
		private List<BNew> datas = new ArrayList<BNew>();

		public MyItemMew_Ap(int resourceId) {
			super();

			this.inflater = LayoutInflater.from(BaseContext);
			this.ResourceId = resourceId;

		}

		/**
		 * 刷新
		 */
		public void Refrsh(List<BNew> da) {
			this.datas = da;
			notifyDataSetChanged();
		}

		/**
		 * 添加刷新
		 */
		public void AddRefrsh(List<BNew> da) {
			this.datas.addAll(da);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return datas.size();
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
			My_New_Item_Item myItem = null;
			if (convertView == null) {
				myItem = new My_New_Item_Item();
				convertView = inflater.inflate(ResourceId, null);
				myItem.item_my_new_title = (TextView) convertView
						.findViewById(R.id.item_my_new_title);
				myItem.item_my_new_content = (TextView) convertView
						.findViewById(R.id.item_my_new_content);
				myItem.item_myin_new_iv = ViewHolder.get(convertView,
						R.id.item_myin_new_iv);
				myItem.item_my_new_time = ViewHolder.get(convertView,
						R.id.item_my_new_time);
				convertView.setTag(myItem);
			} else {
				myItem = (My_New_Item_Item) convertView.getTag();
			}
			BNew data = datas.get(position);

			StrUtils.SetTxt(myItem.item_my_new_title, data.getTitle());
			StrUtils.SetTxt(myItem.item_my_new_content, data.getContent());
			StrUtils.SetTxt(myItem.item_my_new_time, DateUtils
					.timeStampToStr(StrUtils.toLong(data.getCreate_time())));

			myItem.item_myin_new_iv.setVisibility(View.GONE);

			return convertView;
		}

		class My_New_Item_Item {
			TextView item_my_new_title;
			TextView item_my_new_content;
			ImageView item_myin_new_iv;
			TextView item_my_new_time;//
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
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	@Override
	public void onRefresh() {
		LastId = "";
		IData(LastId, SourceType, LOAD_REFRESHING);
	}

	@Override
	public void onLoadMore() {

		IData(LastId, SourceType, LOAD_LOADMOREING);
	}

}
