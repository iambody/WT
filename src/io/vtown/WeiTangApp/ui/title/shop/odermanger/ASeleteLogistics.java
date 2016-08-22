package io.vtown.WeiTangApp.ui.title.shop.odermanger;


import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
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
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-6-22 上午10:27:44
 *  选择物流页面
 */
public class ASeleteLogistics extends ATitleBase implements OnItemClickListener {

	/**
	 * 物流列表
	 */
	private ListView lv_select_logistics_list;
	/**
	 * AP
	 */
	private LogisticsAdapter mLogisticsAdapter;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_shop_order_manage_select_logistics);
		IView();
		IData();
	}

	

	@Override
	protected void InitTile() {
		SetTitleTxt(getString(R.string.select_logistics_company_title));
	}
	

	/**
	 * 初始化控件
	 */
	private void IView() {
		lv_select_logistics_list = (ListView) findViewById(R.id.lv_select_logistics_list);
		mLogisticsAdapter = new LogisticsAdapter(R.layout.item_logistics);
		lv_select_logistics_list.setAdapter(mLogisticsAdapter);
		lv_select_logistics_list.setOnItemClickListener(this);
	}
	
	/**
	 * 获取列表数据
	 */
	private void IData() {
		SetTitleHttpDataLisenter(this);
		HashMap<String,String> map = new HashMap<String, String>();
		FBGetHttpData(map, Constants.Logistics_List, Method.GET, 0, LOAD_INITIALIZE);
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		if(StrUtils.isEmpty(Data.getHttpResultStr())){
			return;
		}
		
		List<BLComment> datas = new ArrayList<BLComment>();
		try {
			datas = JSON.parseArray(Data.getHttpResultStr(), BLComment.class);
		} catch (Exception e) {
			DataError("解析失败", 1);
		}
		
		mLogisticsAdapter.FrashData(datas);
	}

	@Override
	protected void DataError(String error, int LoadType) {
		PromptManager.ShowMyToast(BaseContext, error);
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
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}
	
	class LogisticsAdapter extends BaseAdapter{
		private int ResoureId;
		private LayoutInflater inflater;
		private List<BLComment> list_data = new ArrayList<BLComment>();
		
		public LogisticsAdapter(int ResoureId){
			super();
			this.ResoureId = ResoureId;
			this.inflater = LayoutInflater.from(BaseContext);
		}
		
		@Override
		public int getCount() {
			
			return list_data.size();
		}
		
		/**
		 * 刷新数据
		 * 
		 * @param dass
		 */
		public void FrashData(List<BLComment> dass) {
			this.list_data = dass;
			this.notifyDataSetChanged();
		}


		@Override
		public Object getItem(int arg0) {
			
			return list_data.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			LogisticsItem logistics = null;
			if(arg1 == null){
				logistics = new LogisticsItem();
				arg1 = inflater.inflate(ResoureId, null);
				logistics.tv_Logistics_name = ViewHolder.get(arg1, R.id.tv_Logistics_name);
				arg1.setTag(logistics);
			}else{
				logistics = (LogisticsItem) arg1.getTag();
			}
			StrUtils.SetTxt(logistics.tv_Logistics_name, list_data.get(arg0).getName());
			return arg1;
		}
		
	}
	
	class LogisticsItem {
		public TextView tv_Logistics_name;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		BLComment logisticsinfo = (BLComment) mLogisticsAdapter.getItem(arg2);
		Intent intent = new Intent();
		intent.putExtra("logisticsinfo", logisticsinfo);
		setResult(RESULT_OK,intent);
		this.finish();
	}

}
