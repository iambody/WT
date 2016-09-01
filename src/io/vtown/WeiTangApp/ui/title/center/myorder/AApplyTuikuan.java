package io.vtown.WeiTangApp.ui.title.center.myorder;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-6-6 下午4:33:04 申请退款页面
 * 
 */
public class AApplyTuikuan extends ATitleBase implements OnItemClickListener {

	/**
	 * 来自我的订单
	 */
	public static final int Tag_From_Center_My_Order = 100;
	/**
	 * 来自采购单
	 */
	public static final int Tag_From_Purchase = 101;

	/**
	 * 申请退款原因列表
	 */
	private ListView lv_apply_tuikuan_cause_list;
	/**
	 * 申请退款按钮
	 */
	private TextView tv_apply_tuikuan;
	/**
	 * 选择按钮
	 */
	private ImageView iv_select_cause;
	/**
	 * 脚布局中的布局
	 */
	private LinearLayout ll_apply_tuikuan_select_other;
	/**
	 * 脚布局中的选择按钮
	 */
	private ImageView iv_apply_tuikuan_select_other;
	/**
	 * 脚布局中的输入框
	 */
	private EditText et_apply_tuikuan_cause_other;

	/**
	 * 是否选择了其它
	 */
	boolean flag = false;

	/**
	 * 原因ID
	 */
	private String return_reason_id = "";
	/**
	 * 原因
	 */
	private String return_reason = "";
	/**
	 * 订单号
	 */
	private String seller_order_sn;
	/**
	 * 来自哪里
	 */
	private int mFromTag;
	/**
	 * 用户信息
	 */
	private BUser user_Get;
	/**
	 * 退款原因AP
	 */
	private CauseAdapter causeAdapter;
	/**
	 * 获取数据成功显示的布局
	 */
	private LinearLayout center_refund_reason_outlay;
	/**
	 * 获取数据失败显示的布局
	 */
	private View center_refund_reason_nodata_lay;
	/**
	 * 选择的item数据
	 */
	private BLComment bl_data;
	private List<BLComment> remind_reason;
	/**
	 * 其它
	 */
	private TextView tv_apply_tuikuan_select_other;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_center_my_order_apply_tuikuan);
		SetTitleHttpDataLisenter(this);
		user_Get = Spuit.User_Get(getApplicationContext());
		Intent intent = getIntent();
		seller_order_sn = intent.getStringExtra("seller_order_sn");
		mFromTag = intent.getIntExtra("FromTag", 0);
		IView();
		IData();
	}

	/**
	 * 获取退款原因列表
	 */
	private void IData() {
		PromptManager.showtextLoading(BaseContext,
				getResources()
						.getString(R.string.xlistview_header_hint_loading));
		HashMap<String, String> map = new HashMap<String, String>();
		FBGetHttpData(map, Constants.Refund_Reason, Method.GET, 0,
				LOAD_INITIALIZE);
	}

	private void IView() {

		center_refund_reason_outlay = (LinearLayout) findViewById(R.id.center_refund_reason_outlay);
		center_refund_reason_nodata_lay = findViewById(R.id.center_refund_reason_nodata_lay);
		IDataView(center_refund_reason_outlay, center_refund_reason_nodata_lay,
				NOVIEW_INITIALIZE);
		lv_apply_tuikuan_cause_list = (ListView) findViewById(R.id.lv_apply_tuikuan_cause_list);
		tv_apply_tuikuan = (TextView) findViewById(R.id.tv_apply_tuikuan);
		View view = View.inflate(BaseContext,
				R.layout.item_apply_tuikuan_cause_footer, null);
		ll_apply_tuikuan_select_other = (LinearLayout) view
				.findViewById(R.id.ll_apply_tuikuan_select_other);
		tv_apply_tuikuan_select_other = (TextView) view
				.findViewById(R.id.tv_apply_tuikuan_select_other);

		iv_apply_tuikuan_select_other = (ImageView) view
				.findViewById(R.id.iv_apply_tuikuan_select_other);
		et_apply_tuikuan_cause_other = (EditText) view
				.findViewById(R.id.et_apply_tuikuan_cause_other);
		lv_apply_tuikuan_cause_list.addFooterView(view, null, true);
		causeAdapter = new CauseAdapter(R.layout.item_apply_tuikuan_cause);
		lv_apply_tuikuan_cause_list.setAdapter(causeAdapter);
		OtherCause(flag);

		lv_apply_tuikuan_cause_list.setOnItemClickListener(this);
		ll_apply_tuikuan_select_other.setOnClickListener(this);
		center_refund_reason_nodata_lay.setOnClickListener(this);
		tv_apply_tuikuan.setOnClickListener(this);

	}

	/**
	 * 选择其它原因
	 * 
	 * @param flag
	 */
	private void OtherCause(boolean flag) {
		if (flag) {
			iv_apply_tuikuan_select_other
					.setImageResource(R.drawable.ic_zhengfangxing_press);
			tv_apply_tuikuan_select_other.setTextColor(getResources().getColor(
					R.color.app_fen));
			causeAdapter.setPos(-1);
			return_reason_id = "99";
		} else {
			iv_apply_tuikuan_select_other
					.setImageResource(R.drawable.ic_zhengfangxing_nor);
			tv_apply_tuikuan_select_other.setTextColor(getResources().getColor(
					R.color.app_black));
		}
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getString(R.string.apply_tuikuan));
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		tv_apply_tuikuan.setEnabled(true);
		switch (Data.getHttpResultTage()) {
		case 0:
			if (StrUtils.isEmpty(Data.getHttpResultStr())) {
				return;
			}
			remind_reason = new ArrayList<BLComment>();
			try {
				remind_reason = JSON.parseArray(Data.getHttpResultStr(),
						BLComment.class);
			} catch (Exception e) {
				DataError("解析失败", 1);
				IDataView(center_refund_reason_outlay,
						center_refund_reason_nodata_lay, NOVIEW_ERROR);
			}
			IDataView(center_refund_reason_outlay,
					center_refund_reason_nodata_lay, NOVIEW_RIGHT);
			causeAdapter.RefreshData(remind_reason);
			if (StrUtils.isEmpty(return_reason_id)
					&& StrUtils.isEmpty(return_reason)) {// 没有选择原因直接点击退款申请，默认第一条被选中
				return_reason_id = remind_reason.get(0).getId();
				return_reason = remind_reason.get(0).getText();
			}
			break;

		case 1:
			DataError("退款申请成功", 1);
			switch (mFromTag) {
			case Tag_From_Center_My_Order:
				EventBus.getDefault().post(
						new BMessage(BMessage.Tage_Center_Order_Updata));
				EventBus.getDefault().post(
						new BMessage(BMessage.Tage_Apply_Refund));
				break;
			case Tag_From_Purchase:
				EventBus.getDefault().post(
						new BMessage(BMessage.Tage_My_Purchase));
				EventBus.getDefault().post(
						new BMessage(BMessage.Tage_Apply_Refund));
				break;

			}

			this.finish();
			break;

		default:
			break;
		}

	}

	@Override
	protected void DataError(String error, int LoadType) {
		tv_apply_tuikuan.setEnabled(true);
		PromptManager.ShowMyToast(BaseContext, error);
	}

	@Override
	protected void NetConnect() {
		NetError.setVisibility(View.GONE);
		tv_apply_tuikuan.setEnabled(true);
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
		case R.id.ll_apply_tuikuan_select_other:// 选择其它
			OtherCause(!flag);
			break;

		case R.id.tv_apply_tuikuan:// 申请退款按钮
			tv_apply_tuikuan.setEnabled(false);
			if(CheckNet(BaseContext))return;
			if ("99".equals(return_reason_id)) {
				String cause_other = et_apply_tuikuan_cause_other.getText()
						.toString().trim();
				if (StrUtils.isEmpty(cause_other)) {
					PromptManager.ShowMyToast(BaseContext, "请输入其它退款原因");
					tv_apply_tuikuan.setEnabled(true);
					return;
				}
				ApplyRefund(cause_other, return_reason_id);
			} else {
				ApplyRefund(return_reason, return_reason_id);
			}
			break;

		case R.id.center_refund_reason_nodata_lay:// 重新获取数据
			IData();
			break;
		}
	}

	/**
	 * 申请退款
	 * 
	 * @param bl_data2
	 */
	private void ApplyRefund(String return_reason, String return_reason_id) {

		HashMap<String, String> map = new HashMap<String, String>();
		String host = "";
		int method = 0;
		map.put("seller_order_sn", seller_order_sn);
		map.put("member_id", user_Get.getId());
		map.put("return_reason", return_reason);
		map.put("return_reason_id", return_reason_id);
		switch (mFromTag) {
		case Tag_From_Center_My_Order:
			host = Constants.Center_My_Order_Apply_Refund;
			method = Method.PUT;
			break;

		case Tag_From_Purchase:
			host = Constants.Purchase_Apply_Refund;
			method = Method.POST;
			break;

		}
		FBGetHttpData(map, host, method, 1, LOAD_INITIALIZE);
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	class CauseAdapter extends BaseAdapter {

		private int ResourseId;
		private LayoutInflater inflater;
		private int pos;

		List<BLComment> datas = new ArrayList<BLComment>();
		public CauseAdapter(int ResourseId) {
			super();
			this.ResourseId = ResourseId;
			this.inflater = LayoutInflater.from(BaseContext);
		}

		@Override
		public int getCount() {

			return datas.size();
		}

		public void setPos(int pos) {
			this.pos = pos;
			this.notifyDataSetChanged();
		}

		/**
		 * 刷新数据
		 * 
		 * @param dass
		 */
		public void RefreshData(List<BLComment> dass) {
			this.datas = dass;
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
			CauseItem cause = null;
			if (arg1 == null) {
				cause = new CauseItem();
				arg1 = inflater.inflate(ResourseId, null);
				cause.iv_select_cause = ViewHolder.get(arg1,
						R.id.iv_select_cause);
				cause.tv_tuikuan_cause = ViewHolder.get(arg1,
						R.id.tv_tuikuan_cause);
				arg1.setTag(cause);
			} else {
				cause = (CauseItem) arg1.getTag();
			}

			if (pos == arg0) {
				cause.iv_select_cause
						.setImageResource(R.drawable.ic_zhengfangxing_press);
				cause.tv_tuikuan_cause.setTextColor(getResources().getColor(
						R.color.app_fen));
			} else {
				cause.iv_select_cause
						.setImageResource(R.drawable.ic_zhengfangxing_nor);
				cause.tv_tuikuan_cause.setTextColor(getResources().getColor(
						R.color.app_black));
			}
			StrUtils.SetTxt(cause.tv_tuikuan_cause, datas.get(arg0).getText());
			return arg1;
		}

	}

	class CauseItem {
		ImageView iv_select_cause;
		TextView tv_tuikuan_cause;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		causeAdapter.setPos(arg2);
		BLComment bl_cause = (BLComment) causeAdapter.getItem(arg2);
		return_reason_id = bl_cause.getId();
		return_reason = bl_cause.getText();
		OtherCause(false);

	}

}
