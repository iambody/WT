package io.vtown.WeiTangApp.ui.title.center.wallet;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-19 下午9:53:10 银行卡管理页面
 */
public class ABankCardManager extends ATitleBase implements
		OnItemClickListener, OnItemLongClickListener {

	/**
	 * 银行卡列表
	 */
	private ListView lv_my_bank_card_list;
	/**
	 * AP
	 */
	private BankCardAdapter bankCardAdapter;
	private List<BLComment> listdata;
	private boolean isFinish;
	/**
	 * 布局View
	 */
	private View contentView;
	/**
	 * 银行操作的Popup
	 */
	private PopupWindow mPopupWindow;
	/**
	 * 用户信息
	 */
	private BUser user_Get;
	/**
	 * 获取到数据时显示的布局
	 */
	private LinearLayout center_wallet_bankcard_manage_outlay;
	/**
	 * 获取数据失败时显示的布局
	 */
	private View center_wallet_bankcard_manage_nodata_lay;
	
	/**
	 * 是否需要显示错误的布局
	 */
	private boolean needShowErrorLayout = false;
	/**
	 * 添加银行卡
	 */
	private Button btn_add_new_bank_card;
	private Button btn_add_new_bank_card1;
	/**
	 * 没有银行卡时提示文言
	 */
	private TextView tv_add_bank_card;
	/**
	 * 银行列表脚布局
	 */
	private LinearLayout ll_bank_card_list_foot;

	@Override
	protected void InItBaseView() {
		SetTitleHttpDataLisenter(this);
		contentView = LayoutInflater.from(BaseContext).inflate(
				R.layout.activity_center_wallet_bankcard_manager, null);
		setContentView(contentView);
		// 注册事件
		EventBus.getDefault().register(this, "OnGetMessage", BMessage.class);
		user_Get = Spuit.User_Get(getApplicationContext());
		IView();
		IData();
	}

	private void IData() {

		PromptManager.showtextLoading(BaseContext,
				getResources()
						.getString(R.string.xlistview_header_hint_loading));
		HashMap<String, String> map = new HashMap<String, String>();
		needShowErrorLayout = true;
		map.put("member_id", user_Get.getId());
		FBGetHttpData(map, Constants.MY_BANK_LIST, Method.GET, 0,
				LOAD_INITIALIZE);

	}

	/**
	 * 删除银行卡
	 */
	private void RemoveBankCard(String bank_id, String member_id) {
		HashMap<String, String> map = new HashMap<String, String>();
		needShowErrorLayout = false;
		map.put("bank_id", bank_id);
		map.put("member_id", member_id);
		FBGetHttpData(map, Constants.Remove_Bank_Card, Method.DELETE, 1,
				LOAD_INITIALIZE);
	}

	private void IView() {
//		center_wallet_bankcard_manage_outlay = (LinearLayout) findViewById(R.id.center_wallet_bankcard_manage_outlay);
//		center_wallet_bankcard_manage_nodata_lay = findViewById(R.id.center_wallet_bankcard_manage_nodata_lay);
//		btn_add_new_bank_card = (Button) center_wallet_bankcard_manage_nodata_lay.findViewById(R.id.btn_add_new_bank_card);
//		IDataView(center_wallet_bankcard_manage_outlay,
//				center_wallet_bankcard_manage_nodata_lay, NOVIEW_INITIALIZE);
		lv_my_bank_card_list = (ListView) findViewById(R.id.lv_my_bank_card_list);
//		btn_add_new_bank_card1 = (Button) findViewById(R.id.btn_add_new_bank_card1);
//		btn_add_new_bank_card1.setOnClickListener(this);
//		center_wallet_bankcard_manage_nodata_lay.setOnClickListener(this);
		isFinish = getIntent().getBooleanExtra("isFinish", false);
		
		if (!isFinish) {
			IData();
			//btn_add_new_bank_card1.setVisibility(View.VISIBLE);
			//btn_add_new_bank_card1.setOnClickListener(this);
			IFooterView();
		}
		
		IList();
	}
	
	/**
	 * 初始化脚布局
	 */
	private void IFooterView(){
		View footView = LayoutInflater.from(BaseContext).inflate(R.layout.item_bank_card_manage_foot_view, null);
		lv_my_bank_card_list.addFooterView(footView);
		ll_bank_card_list_foot = (LinearLayout) footView.findViewById(R.id.ll_bank_card_list_foot);
		tv_add_bank_card = (TextView) footView.findViewById(R.id.tv_add_bank_card);
		ll_bank_card_list_foot.setOnClickListener(this);
	}

	private void IList() {
		lv_my_bank_card_list.setVisibility(View.GONE);
		bankCardAdapter = new BankCardAdapter(R.layout.item_bank_card);
		lv_my_bank_card_list.setAdapter(bankCardAdapter);
		if (isFinish) {
			lv_my_bank_card_list.setOnItemClickListener(this);
		} else {
			lv_my_bank_card_list.setOnItemLongClickListener(this);
		}
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.bank_card_manage));
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		lv_my_bank_card_list.setVisibility(View.VISIBLE);
		switch (Data.getHttpResultTage()) {
		case 0:
			if (StrUtils.isEmpty(Data.getHttpResultStr())) {

				return;
			}

			listdata = new ArrayList<BLComment>();

			try {
				listdata = JSON.parseArray(Data.getHttpResultStr(),
						BLComment.class);

			} catch (Exception e) {
				DataError("解析失败", 1);
			}

			bankCardAdapter.FrashData(listdata);
			if(!isFinish){
				tv_add_bank_card.setVisibility(View.GONE);
			}
			
			break;
		case 1:
			PromptManager.ShowMyToast(BaseContext, "删除成功");
			IData();
			break;

		default:
			break;
		}

	}

	@Override
	protected void DataError(String error, int LoadTyp) {
		lv_my_bank_card_list.setVisibility(View.VISIBLE);
		PromptManager.ShowMyToast(BaseContext, error);
		bankCardAdapter.FrashData(new ArrayList<BLComment>());
		if (!isFinish) {
			
			tv_add_bank_card.setVisibility(View.VISIBLE);
		}
//		if (LOAD_INITIALIZE == LoadTyp && needShowErrorLayout) {
//			IDataView(center_wallet_bankcard_manage_outlay,
//					center_wallet_bankcard_manage_nodata_lay, NOVIEW_ERROR);
//			
//			if (!isFinish) {
//				//btn_add_new_bank_card.setVisibility(View.VISIBLE);
//				//btn_add_new_bank_card.setOnClickListener(this);
//				tv_add_bank_card.setVisibility(View.VISIBLE);
//			}
//	//	}
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
		//case R.id.btn_add_new_bank_card:
		//case R.id.btn_add_new_bank_card1:
		case R.id.ll_bank_card_list_foot:
			Intent intent = new Intent(BaseActivity, AAddBankCard.class);
			intent.putExtra("togo", 0);
			PromptManager.SkipActivity(BaseActivity, intent);

			break;

		case R.id.center_wallet_bankcard_manage_nodata_lay:// 重新获取数据
			IData();
			break;

		default:
			break;
		}
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);

		IData();

	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	class BankCardAdapter extends BaseAdapter {

		private int ResourcesId;
		
		/**
		 * 填充器
		 */
		private LayoutInflater inflater;

		/**
		 * 数据
		 */
		private List<BLComment> datas = new ArrayList<BLComment>();

		public BankCardAdapter(int ResourcesId) {
			super();
			this.ResourcesId = ResourcesId;
			this.inflater = LayoutInflater.from(BaseContext);
		}

		@Override
		public int getCount() {

			return datas.size();
		}

		/**
		 * 刷新数据
		 * 
		 * @param dass
		 */
		public void FrashData(List<BLComment> dass) {
			this.datas = dass;
			this.notifyDataSetChanged();
		}

		/**
		 * 加载更多
		 */
		public void AddFrashData(List<BLComment> dass) {
			this.datas.addAll(datas);
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
			BankCardItem item = null;
			if (arg1 == null) {
				arg1 = inflater.inflate(ResourcesId, null);
				item = new BankCardItem();
				//item.iv_bank_icon = ViewHolder.get(arg1, R.id.iv_bank_icon);
				item.iv_bank_icon = (CircleImageView)arg1.findViewById(R.id.iv_bank_icon);
				item.tv_bank_card_name = ViewHolder.get(arg1,
						R.id.tv_bank_card_name);
				item.tv_bank_card_numb = ViewHolder.get(arg1,
						R.id.tv_bank_card_numb);
				arg1.setTag(item);
			} else {
				item = (BankCardItem) arg1.getTag();
			}

			ImageLoaderUtil.Load2(datas.get(arg0).getIcon(), item.iv_bank_icon,
					R.drawable.error_iv2);
			StrUtils.SetTxt(item.tv_bank_card_name, datas.get(arg0)
					.getBank_name());
			StrUtils.SetTxt(item.tv_bank_card_numb, StrUtils
					.getCardFormatForUser(datas.get(arg0).getCard_number()));
			return arg1;
		}

		class BankCardItem {
			CircleImageView iv_bank_icon;
			TextView tv_bank_card_name, tv_bank_card_numb;

		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent();
		BLComment blComment = listdata.get(arg2);
		intent.putExtra("bankinfo", blComment);
		setResult(RESULT_OK, intent);
		finish();

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

		BLComment bl = (BLComment) arg0.getItemAtPosition(arg2);

		if (mPopupWindow == null) {
			ShowPop(bl,
					LayoutInflater.from(BaseContext).inflate(
							R.layout.activity_center_wallet_bankcard_manager,
							null));
		}

		return true;
	}

	/**
	 * 显示Popup
	 * 
	 * @param bl
	 * @param v
	 */
	private void ShowPop(BLComment bl, View v) {

		View view = LayoutInflater.from(BaseContext).inflate(
				R.layout.pop_bankcard_manage, null);
		ImageView iv_bank_manage_icon = (ImageView) view
				.findViewById(R.id.iv_bank_manage_icon);
		TextView tv_bank_manage_name = (TextView) view
				.findViewById(R.id.tv_bank_manage_name);
		TextView tv_bank_manage_card_numb = (TextView) view
				.findViewById(R.id.tv_bank_manage_card_numb);
		TextView tv_modify_bank_card = (TextView) view
				.findViewById(R.id.tv_modify_bank_card);
		TextView tv_bank_card_cancel = (TextView) view
				.findViewById(R.id.tv_bank_card_cancel);
		TextView tv_bank_card_delete = (TextView) view
				.findViewById(R.id.tv_bank_card_delete);

		ImageLoaderUtil.Load(bl.getIcon(), iv_bank_manage_icon,
				R.drawable.error_iv2);

		tv_modify_bank_card.setOnClickListener(new ShareClick(bl));
		tv_bank_card_cancel.setOnClickListener(new ShareClick(bl));
		tv_bank_card_delete.setOnClickListener(new ShareClick(bl));

		StrUtils.SetTxt(tv_bank_manage_name, bl.getBank_name());

		String bank_numb = getResources().getString(
				R.string.lable_bank_numb_format_1);
		String card_number = bl.getCard_number();
		String card_number_sub = card_number.substring(
				card_number.length() - 4, card_number.length());
		StrUtils.SetTxt(tv_bank_manage_card_numb,
				String.format(bank_numb, card_number_sub));

		mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		mPopupWindow.setBackgroundDrawable(dw);
		mPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);

	}

//	/**
//	 * 对话框测试
//	 * 
//	 * @param datBlComment
//	 * 
//	 * @param aa
//	 */
//	private void ShowDialog(final BLComment datBlComment) {
//
//		final CustomDialog dialog = new CustomDialog(BaseContext,
//				R.style.mystyle, R.layout.customdialog, 1, "取消", "删除");
//		dialog.show();
//		dialog.setTitleText("删除银行卡");
//		dialog.setConfirmListener(new onConfirmClick() {
//			@Override
//			public void onConfirmCLick(View v) {
//				PromptManager.showLoading(BaseContext);
//				RemoveBankCard(datBlComment.getBank_id(), user_Get.getId());
//				dialog.dismiss();
//				if (mPopupWindow != null && mPopupWindow.isShowing()) {
//					mPopupWindow.dismiss();
//					mPopupWindow = null;
//				}
//
//			}
//		});
//		dialog.setcancelListener(new oncancelClick() {
//			@Override
//			public void oncancelClick(View v) {
//
//				dialog.dismiss();
//			}
//		});
//	}

	class ShareClick implements OnClickListener {
		private BLComment datBlComment = null;

		public ShareClick() {
			super();
		}

		public ShareClick(BLComment datBlComment) {
			super();
			this.datBlComment = datBlComment;

		}

		@Override
		public void onClick(View view) {

			switch (view.getId()) {
			case R.id.tv_modify_bank_card:
				Intent intent = new Intent(BaseActivity,
						ABankCardOperation.class);
				intent.putExtra("datas", datBlComment);
				PromptManager.SkipResultActivity(BaseActivity, intent, 101);
				if (mPopupWindow != null && mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
					mPopupWindow = null;
				}

				break;

			case R.id.tv_bank_card_cancel:
				if (mPopupWindow != null && mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
					mPopupWindow = null;
				}

				break;

			case R.id.tv_bank_card_delete:
				
				
				ShowCustomDialog("删除银行卡？", "取消", "删除", new IDialogResult() {
					@Override
					public void RightResult() {
						if(CheckNet(BaseContext))return;
						PromptManager.showLoading(BaseContext);
						RemoveBankCard(datBlComment.getBank_id(), user_Get.getId());
						
						if (mPopupWindow != null && mPopupWindow.isShowing()) {
							mPopupWindow.dismiss();
							mPopupWindow = null;
						}
					}

					@Override
					public void LeftResult() {
					}
				});
				break;
			}

		}

	}
	
	/**
	 * 接收事件
	 * 
	 * @param event
	 */

	public void OnGetMessage(BMessage event) {
		int messageType = event.getMessageType();
		if (messageType == event.Tage_Updata_BankCard_List) {
			this.finish();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		try {
			EventBus.getDefault().unregister(new BMessage());
		} catch (Exception e) {
			
		}
	}

}
