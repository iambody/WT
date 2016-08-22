package io.vtown.WeiTangApp.ui.title.shop.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-16 上午11:24:15 邀请记录页面
 */
public class AInviteRecord extends ATitleBase {

	private ListView lv_invite_listview;
	/**
	 * 用户信息
	 */
	private BUser user_Get;
	/**
	 * 获取数据成功的页面
	 */
	private LinearLayout ll_invite_revord_outlay;
	/**
	 * 获取数据失败的页面
	 */
	private View invite_record_nodata_lay;
	/**
	 * AP
	 */
	private myAdapter myAdapter;

	@Override
	protected void InItBaseView() { 
		setContentView(R.layout.activity_invite_record);
		user_Get = Spuit.User_Get(BaseContext);
		IView();
		IData();
	}

	/**
	 * 初始化数据
	 */
	private void IData() {
		SetTitleHttpDataLisenter(this);
		PromptManager.showtextLoading(BaseContext, getResources().getString(R.string.xlistview_header_hint_loading));
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", user_Get.getSeller_id());
		map.put("page", "1");
		map.put("pagesize", Constants.PageSize+"");
		FBGetHttpData(map, Constants.SHOP_CHANNEL_INVITE_RECORD, Method.GET, 0, LOAD_INITIALIZE);
	}

	private void IView() {
		ll_invite_revord_outlay = (LinearLayout) findViewById(R.id.ll_invite_revord_outlay);
		invite_record_nodata_lay = findViewById(R.id.invite_record_nodata_lay);
		IDataView(ll_invite_revord_outlay, invite_record_nodata_lay, NOVIEW_INITIALIZE);
		lv_invite_listview = (ListView) findViewById(R.id.lv_invite_listview);
		myAdapter = new myAdapter(BaseContext, R.layout.item_invite_record);
		lv_invite_listview.setAdapter(myAdapter);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.invite_record));
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
		IDataView(ll_invite_revord_outlay, invite_record_nodata_lay, NOVIEW_RIGHT);
		myAdapter.RefreshData(datas);
	}

	@Override
	protected void DataError(String error, int LoadTyp) {
		PromptManager.ShowMyToast(BaseContext, error);
		if(LOAD_INITIALIZE == LoadTyp){
			IDataView(ll_invite_revord_outlay, invite_record_nodata_lay, NOVIEW_ERROR);
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
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	class myAdapter extends BaseAdapter {
		
		private Context context;
		private int ResourseId;
		private LayoutInflater inflater;
		List<BLComment> datas = new ArrayList<BLComment>();
		
		public myAdapter(Context context,int ResourseId){
			super();
			this.context = context;
			this.ResourseId = ResourseId;
			this.inflater = LayoutInflater.from(context);
			
		}
		
		public void RefreshData(List<BLComment> datas){
			this.datas = datas;
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
			ViewHolder holder = null;
			if (arg1 == null) {
				arg1 = inflater.inflate(ResourseId, null);
				holder = new ViewHolder();
				holder.tv_data = (TextView) arg1.findViewById(R.id.tv_data);
				holder.tv_time = (TextView) arg1.findViewById(R.id.tv_time);
				holder.tv_record_desc = (TextView) arg1
						.findViewById(R.id.tv_record_desc);
				holder.vertical_line = arg1.findViewById(R.id.vertical_line);

				arg1.setTag(holder);
			} else {
				holder = (ViewHolder) arg1.getTag();

			}

			return arg1;
		}

		class ViewHolder {

			TextView tv_data, tv_time, tv_record_desc;
			View vertical_line;
		}

	}

}
