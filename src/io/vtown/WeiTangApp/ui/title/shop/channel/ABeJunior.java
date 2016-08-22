package io.vtown.WeiTangApp.ui.title.shop.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BData;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-16 上午11:18:53 成为下级页面
 */
public class ABeJunior extends ATitleBase {

	/**
	 * 列表
	 */
	private ListView be_junior_ls;
	/**
	 * ls对应的ls
	 */
	private BeJuniorAp beJuniorAp;
	/**
	 * 按钮
	 */
	private TextView be_junior_bt;

	private String GetUel;

	/**
	 * 获取数据
	 */
	private BDComment da;
	/**
	 * 用户信息
	 */
	private BUser user_Get;

	/**
	 * 获取到的列表
	 */
	private BData mBData;;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_be_junior);
		user_Get = Spuit.User_Get(BaseContext);
		IBundle();
		IView();
	}

	private void IBundle() {
		if (getIntent().getExtras() != null
				&& getIntent().getExtras().containsKey("url")) {
			GetUel = getIntent().getStringExtra("url");
		} else {
			BaseActivity.finish();
		}

		IData(GetUel);
	}

	private void IData(String getUel2) {
		// SaoMiaoCodeToList
		PromptManager.showtextLoading(BaseContext,
				getResources().getString(R.string.loading));
		SetTitleHttpDataLisenter(this);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("qrcode", getUel2);
		FBGetHttpData(map, Constants.SaoMiaoCodeToList, Method.GET, 10,
				LOAD_INITIALIZE);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.be_junior));
	}

	private void IView() {
		be_junior_ls = (ListView) findViewById(R.id.be_junior_ls);
		be_junior_bt = (TextView) findViewById(R.id.be_junior_bt);
		be_junior_bt.setOnClickListener(this);

		be_junior_ls.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				beJuniorAp.OnItemClick(arg2);
			}
		});
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		// if (StrUtils.isEmpty(Data.getHttpResultStr())) {
		// PromptManager.ShowCustomToast(BaseContext, Msg);
		// return;
		// }

		switch (Data.getHttpResultTage()) {
		case 0:
			PromptManager.ShowCustomToast(BaseContext, "申请成功");
			BaseActivity.finish();
			break;
		case 10:
			if (StrUtils.isEmpty(Data.getHttpResultStr())) {
				PromptManager.ShowCustomToast(BaseContext, "获取失败");
				BaseActivity.finish();
				return;
			}

			try {
				mBData = JSON.parseObject(Data.getHttpResultStr(), BData.class);
			} catch (Exception e) {
				PromptManager.ShowCustomToast(BaseContext, "解析出错");
				return;
			}
			beJuniorAp = new BeJuniorAp(BaseContext, mBData.getQrtext());
			be_junior_ls.setAdapter(beJuniorAp);

			if (mBData.getQrtext() != null && mBData.getQrtext().size() > 0)
				be_junior_bt.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}

	}

	@Override
	protected void DataError(String error, int LoadTyp) {
		PromptManager.ShowCustomToast(BaseContext, error);
	}

	class BeJuniorAp extends BaseAdapter {
		private Context mycContext;
		private LayoutInflater inflater;

		private List<BLDComment> datas = new ArrayList<BLDComment>();
		private List<Boolean> tagBooleans = new ArrayList<Boolean>();

		public BeJuniorAp(Context mycContext, List<BLDComment> s) {
			super();
			this.mycContext = mycContext;
			this.inflater = LayoutInflater.from(mycContext);
			datas = s;
			for (int i = 0; i < s.size(); i++) {
				tagBooleans.add(false);
			}
		}

		/**
		 * 获取数据
		 */
		public List<BLDComment> GetDatas() {
			return datas;

		}

		/**
		 * 获取数据
		 */
		public List<Boolean> GetBooles() {
			return tagBooleans;

		}

		/**
		 * 获取标志列表
		 */
		public void OnItemClick(int Postion) {
			tagBooleans.set(Postion, !tagBooleans.get(Postion));
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
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {// item_be_junior
			MyJunioritem myItem = null;
			if (arg1 == null) {
				myItem = new MyJunioritem();
				arg1 = inflater.inflate(R.layout.item_be_junior, null);
				myItem.item_be_junior_text = ViewHolder.get(arg1,
						R.id.item_be_junior_text);
				myItem.item_be_junior_selectiv = ViewHolder.get(arg1,
						R.id.item_be_junior_selectiv);

				arg1.setTag(myItem);
			} else {
				myItem = (MyJunioritem) arg1.getTag();
			}
			BLDComment data = datas.get(arg0);

			StrUtils.SetTxt(myItem.item_be_junior_text, data.getDescription());
			myItem.item_be_junior_selectiv
					.setImageResource(GetBooles().get(arg0) ? R.drawable.quan_select_3
							: R.drawable.quan_select_1);

			return arg1;
		}

		class MyJunioritem {

			TextView item_be_junior_text;
			ImageView item_be_junior_selectiv;
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
		switch (V.getId()) {
		case R.id.be_junior_bt:
			Commint();
			break;

		default:
			break;
		}
	}

	private void Commint() {
		List<BLDComment> data = beJuniorAp.GetDatas();

		JSONObject obj = new JSONObject();

		for (int i = 0; i < data.size(); i++) {
			if (beJuniorAp.GetBooles().get(i)) {
				try {
					obj.put(data.get(i).getAgency(), data.get(i).getLevel());
				} catch (JSONException e) {
					e.printStackTrace();
					return;
				}

			}
		}
		IComment(mBData.getInvite_id(), user_Get.getSeller_id(), obj.toString());
		// PromptManager.ShowCustomToastLong(BaseContext, obj.toString());
	}

	/**
	 * 
	 * @param sellerId是二维码中带的sellerid
	 * @param ToId
	 *            是用户的id(我是被邀请成为下级 所以我是目标对象)
	 * @param string
	 *            json参数
	 */
	private void IComment(String sellerId, String ToId, String string) {
		SetTitleHttpDataLisenter(this);
		PromptManager.showtextLoading(BaseContext, "努力成为下级中...");
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("invite_id", sellerId);
		map.put("seller_id", ToId);
		map.put("priv",string );
		FBGetHttpData(map, Constants.SHOP_CHANNEL_XiaJi_commint, Method.POST,
				0, LOAD_INITIALIZE);

	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

}
