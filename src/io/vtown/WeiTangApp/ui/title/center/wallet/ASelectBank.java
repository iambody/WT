package io.vtown.WeiTangApp.ui.title.center.wallet;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.BLSelectBank;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.ui.ATitleBase;

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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-6-2 下午8:00:24 银行卡列表
 */
public class ASelectBank extends ATitleBase {

	/**
	 * 银行卡列表
	 */
	private ListView lv_select_bank_card_list;
	/**
	 * AP
	 */
	private BankAdapter bankAdapter;
	private List<BLSelectBank> listdata;
	/**
	 * 获取到数据时显示的布局
	 */
	private LinearLayout center_wallet_select_bankcard_outlay;
	/**
	 * 获取数据失败时显示的布局
	 */
	private View center_wallet_select_bankcard_nodata_lay;

	@Override
	protected void InItBaseView() {

		setContentView(R.layout.activity_center_wallet_bankcard_manager_add_bankcard_select_bank);
		IView();
		IData();
	}

	private void IView() {
		center_wallet_select_bankcard_outlay = (LinearLayout) findViewById(R.id.center_wallet_select_bankcard_outlay);
		center_wallet_select_bankcard_nodata_lay = findViewById(R.id.center_wallet_select_bankcard_nodata_lay);
		IDataView(center_wallet_select_bankcard_outlay, center_wallet_select_bankcard_nodata_lay, NOVIEW_INITIALIZE);
		center_wallet_select_bankcard_nodata_lay.setOnClickListener(this);
		
		lv_select_bank_card_list = (ListView) findViewById(R.id.lv_select_bank_card_list);
		bankAdapter = new BankAdapter(R.layout.item_select_bank);
		lv_select_bank_card_list.setAdapter(bankAdapter);
		lv_select_bank_card_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent();
				intent.putExtra("bank_info", listdata.get(arg2));
				PromptManager.ShowMyToast(BaseContext, "您选择了"+listdata.get(arg2).getBank_name());
				setResult(RESULT_OK,intent);
				finish();
			}
		});
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getString(R.string.please_select_bank));
	}

	private void IData() {
		PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.xlistview_header_hint_loading));
		SetTitleHttpDataLisenter(this);
		
		HashMap<String, String> map = new HashMap<String, String>();

		FBGetHttpData(map, Constants.BANK_LIST, Method.GET, 0, LOAD_INITIALIZE);

	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {

		if (StrUtils.isEmpty(Data.getHttpResultStr())) {
			return;
		}

		listdata = new ArrayList<BLSelectBank>();

		try {
			listdata = JSON
					.parseArray(Data.getHttpResultStr(), BLSelectBank.class);
			
		} catch (Exception e) {
			DataError("解析失败", 1);
		}
		IDataView(center_wallet_select_bankcard_outlay, center_wallet_select_bankcard_nodata_lay, NOVIEW_RIGHT);
		bankAdapter.FrashData(listdata);
	}

	@Override
	protected void DataError(String error, int LoadType) {
		PromptManager.ShowMyToast(BaseContext, error);
		if(LOAD_INITIALIZE == LoadType){
			IDataView(center_wallet_select_bankcard_outlay, center_wallet_select_bankcard_nodata_lay, NOVIEW_ERROR);
		}
	}

	@Override
	protected void NetConnect() {
	}

	@Override
	protected void NetDisConnect() {
	}

	@Override
	protected void SetNetView() {
	}

	@Override
	protected void MyClick(View V) {
		switch (V.getId()) {
		case R.id.center_wallet_select_bankcard_nodata_lay://重新获取数据
			
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
	protected void SaveBundle(Bundle bundle) {
	}

	class BankAdapter extends BaseAdapter {

		private int ResourcesId;

		/**
		 * 填充器
		 */
		private LayoutInflater inflater;

		/**
		 * 数据
		 */
		private List<BLSelectBank> datas = new ArrayList<BLSelectBank>();

		public BankAdapter(int ResourcesId) {
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
		public void FrashData(List<BLSelectBank> dass) {
			this.datas = dass;
			this.notifyDataSetChanged();
		}

		/**
		 * 加载更多
		 */
		public void AddFrashData(List<BLSelectBank> dass) {
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
			BankItem item = null;
			if (arg1 == null) {
				arg1 = inflater.inflate(ResourcesId, null);
				item = new BankItem();
				item.iv_bank_select_icon = ViewHolder.get(arg1,
						R.id.iv_bank_select_icon);
				item.tv_bank_card_select_name = ViewHolder.get(arg1,
						R.id.tv_bank_card_select_name);

				arg1.setTag(item);
			} else {
				item = (BankItem) arg1.getTag();
			}

			ImageLoaderUtil.Load2(datas.get(arg0).getIcon(),
					item.iv_bank_select_icon, R.drawable.error_iv2);
			StrUtils.SetTxt(item.tv_bank_card_select_name, datas.get(arg0)
					.getBank_name());

			return arg1;
		}

		class BankItem {
			ImageView iv_bank_select_icon;
			TextView tv_bank_card_select_name;

		}

	}

}
