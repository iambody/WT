package io.vtown.WeiTangApp.ui.title.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import android.R.integer;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.comment.view.listview.LListView.IXListViewListener;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.AApplyProxy;
import io.vtown.WeiTangApp.ui.title.ABrandDetail;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-8-22 上午11:49:54
 *  
 */
public class ABrandList extends ATitleBase implements IXListViewListener, AdapterView.OnItemClickListener {

	/**
	 * 品牌列表
	 */
	private LListView shop_brands_list;
	/**
	 * AP
	 */
	private BrandListAdapter brandListAdapter;
	/**
	 * 加载数据失败时显示的布局
	 */
	private View shop_brand_nodata_lay;
	
	private int CurrentPage = 1;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_shop_brand_list);
		SetTitleHttpDataLisenter(this);
		IView();
		IData(LOAD_INITIALIZE,CurrentPage);
	}

	private void IData(int LoadType,int Page) {
		if (LOAD_INITIALIZE == LoadType) {
			PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.xlistview_header_hint_loading));
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("page", Page + "");
		map.put("seller_id", Spuit.User_Get(BaseContext).getSeller_id());
		map.put("pagesize", Constants.SouGoodinf_size);
		FBGetHttpData(map, Constants.Brand_List, Method.GET,
				0, LoadType);
	}

	private void IView() {
		shop_brand_nodata_lay = findViewById(R.id.shop_brand_nodata_lay);
		shop_brands_list = (LListView) findViewById(R.id.shop_brands_list);
		IDataView(shop_brands_list, shop_brand_nodata_lay, NOVIEW_INITIALIZE);
		shop_brands_list.setPullRefreshEnable(true);
		shop_brands_list.setPullLoadEnable(true);
		shop_brands_list.setXListViewListener(this);
		shop_brands_list.hidefoot();
		brandListAdapter = new BrandListAdapter(R.layout.item_shop_brands_list);
		shop_brands_list.setAdapter(brandListAdapter);
		shop_brands_list.setOnItemClickListener(this);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt("品牌");
	}
	
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				shop_brands_list.stopRefresh();
			}
			if (msg.what == 2) {
				shop_brands_list.stopLoadMore();
			}
		}
	};

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		if(StrUtils.isEmpty(Data.getHttpResultStr())&& Data.getHttpLoadType() != LOAD_LOADMOREING){
			DataError(Constants.SucessToError, Data.getHttpLoadType());
			return;
		}
		
		List<BLComment> datas = new ArrayList<BLComment>();
		try {
			datas = JSON.parseArray(Data.getHttpResultStr(), BLComment.class);
		} catch (Exception e) {
			return;
		}
		switch (Data.getHttpLoadType()) {
		case LOAD_INITIALIZE:
			brandListAdapter.FreshData(datas);
			IDataView(shop_brands_list, shop_brand_nodata_lay, NOVIEW_RIGHT);
			break;
			
		case LOAD_REFRESHING:
			Message m = new Message();
			m.what = 1;
			mHandler.sendMessage(m);
			brandListAdapter.FreshData(datas);
			break;
			
		case LOAD_LOADMOREING:
			Message mm = new Message();
			mm.what = 2;
			mHandler.sendMessage(mm);
			brandListAdapter.FreshMoreData(datas);
			break;

		}
		if (datas.size() < Constants.PageSize)
			shop_brands_list.hidefoot();
		else
			shop_brands_list.ShowFoot();
	}

	@Override
	protected void DataError(String error, int LoadType) {
		switch (LoadType) {
		case LOAD_INITIALIZE:
			PromptManager.ShowCustomToast(BaseContext, error);
			IDataView(shop_brands_list, shop_brand_nodata_lay, NOVIEW_ERROR);
			break;
			
		case LOAD_REFRESHING:
			Message m = new Message();
			m.what = 1;
			mHandler.sendMessage(m);
			PromptManager.ShowCustomToast(BaseContext, error);
			break;
			
		case LOAD_LOADMOREING:
			Message mm = new Message();
			mm.what = 2;
			mHandler.sendMessage(mm);
			PromptManager.ShowCustomToast(BaseContext, error);
			break;

		default:
			break;
		}
	}

	@Override
	protected void NetConnect() {
		NetError.setVisibility(View.GONE);
		CurrentPage = 1;
		IData(LOAD_INITIALIZE,CurrentPage);
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
		CurrentPage = 1;
		IData(LOAD_REFRESHING,CurrentPage);
		
	}

	@Override
	public void onLoadMore() {
		CurrentPage = CurrentPage+1;
		IData(LOAD_LOADMOREING,CurrentPage);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		BLComment daa = (BLComment) parent.getItemAtPosition(position);
		BComment d = new BComment(daa.getId(), daa.getSeller_name());
		PromptManager.SkipActivity(BaseActivity, new Intent(
				BaseActivity, ABrandDetail.class).putExtra(
				BaseKey_Bean, d));
	}

	class BrandListAdapter extends BaseAdapter{
		
		private LayoutInflater inflater;
		
		private int ResourseId;
		
		private List<BLComment> datas = new ArrayList<BLComment>();
		
		public BrandListAdapter(int ResourseId){
			super();
			this.ResourseId = ResourseId;
			this.inflater = LayoutInflater.from(BaseContext);
		}

		@Override
		public int getCount() {
			
			return datas.size();
		}
		
		public void FreshData(List<BLComment> datas){
			this.datas = datas;
			this.notifyDataSetChanged();
		}
		
		public void FreshMoreData(List<BLComment> datas){
			this.datas.addAll(datas);
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			ShopBrandItem item = null;
			if(convertView == null){
				item = new ShopBrandItem();
				convertView = inflater.inflate(ResourseId, null);
				item.shop_brand_iv = (CircleImageView) convertView.findViewById(R.id.shop_brand_iv);
				item.tv_shop_brand_name = (TextView) convertView.findViewById(R.id.tv_shop_brand_name);
				item.tv_shop_brand_desc = (TextView) convertView.findViewById(R.id.tv_shop_brand_desc);
				item.tv_shop_brand_apply = (TextView) convertView.findViewById(R.id.tv_shop_brand_apply);
				item.tv_shop_brand_is_apply = (TextView) convertView.findViewById(R.id.tv_shop_brand_is_apply);
				item.tv_shop_brand_apply_success = (TextView) convertView.findViewById(R.id.tv_shop_brand_apply_success);

				convertView.setTag(item);

			}else{
				item = (ShopBrandItem) convertView.getTag();
			}
			ImageLoaderUtil.Load2(datas.get(position).getAvatar(), item.shop_brand_iv, R.drawable.error_iv2);
			StrUtils.SetTxt(item.tv_shop_brand_name, datas.get(position).getSeller_name());
			int status = Integer.parseInt(datas.get(position).getStatus());
			switch (status) {
			case 1:
				item.tv_shop_brand_apply.setVisibility(View.VISIBLE);
				item.tv_shop_brand_is_apply.setVisibility(View.GONE);
				item.tv_shop_brand_apply_success.setVisibility(View.GONE);
				break;
				
			case 2:
				item.tv_shop_brand_apply.setVisibility(View.GONE);
				item.tv_shop_brand_is_apply.setVisibility(View.VISIBLE);
				item.tv_shop_brand_apply_success.setVisibility(View.GONE);
				break;
				
			case 3:
				item.tv_shop_brand_apply.setVisibility(View.GONE);
				item.tv_shop_brand_is_apply.setVisibility(View.GONE);
				item.tv_shop_brand_apply_success.setVisibility(View.VISIBLE);
				break;

			}
			item.tv_shop_brand_apply.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
							AApplyProxy.class).putExtra("brandid", datas.get(position).getId()));
				}
			});
			return convertView;
		}
	}
	
	class ShopBrandItem{
		public TextView tv_shop_brand_name,tv_shop_brand_desc,tv_shop_brand_apply,tv_shop_brand_is_apply,tv_shop_brand_apply_success;
		public CircleImageView shop_brand_iv;
	}

}
