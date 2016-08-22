package io.vtown.WeiTangApp.ui.title.shop.channel;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-5-16 上午11:20:18 发展下级页面
 */
public class ADevelopJunior extends ATitleBase {

	/**
	 * 输入被邀请人手机号
	 */
	private EditText et_enter_invite_phone;
	/**
	 * 成生二维码
	 */
	private Button btn_produce_two_dimension_code;
	/**
	 * ls
	 */
	private CompleteListView develop_junior_ls;
	/**
	 * ls对应的ap
	 */
	private DevelopJuniorAp developJuniorAp;
	/**
	 * 用户信息
	 */
	private BUser user_Get;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_develop_junior);
		user_Get = Spuit.User_Get(BaseContext);
		IView();
		IData();
	}

	private void IData() {// SHOP_CHANNEL_XiaJi_BrandLs
		PromptManager.showtextLoading(BaseContext,
				getResources().getString(R.string.loading));

		SetTitleHttpDataLisenter(this);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", user_Get.getSeller_id());
		FBGetHttpData(map, Constants.SHOP_CHANNEL_XiaJi_BrandLs, Method.GET, 0,
				LOAD_INITIALIZE);
	}

	/**
	 * 提交信息 =>后台返回需要生成二维码的Str
	 */
	private void IUpData(String Obj) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", user_Get.getSeller_id());
		map.put("priv", Obj);
		FBGetHttpData(map, Constants.CreateQRCodeInf, Method.POST, 2,
				LOAD_INITIALIZE);
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.develop_junior));
	}

	private void IView() {
		develop_junior_ls = (CompleteListView) findViewById(R.id.develop_junior_ls);
		developJuniorAp = new DevelopJuniorAp(BaseContext,
				R.layout.item_develop_junior);
		develop_junior_ls.setAdapter(developJuniorAp);

		et_enter_invite_phone = (EditText) findViewById(R.id.et_enter_invite_phone);
		btn_produce_two_dimension_code = (Button) findViewById(R.id.btn_produce_two_dimension_code);
		btn_produce_two_dimension_code.setOnClickListener(this);
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		if (StrUtils.isEmpty(Data.getHttpResultStr())) {
			PromptManager.ShowCustomToast(BaseContext, "您还没代理品牌不能邀请下级");
			return;
		}
		btn_produce_two_dimension_code.setVisibility(View.VISIBLE);
		List<BLComment> blComments = new ArrayList<BLComment>();
		switch (Data.getHttpResultTage()) {
		case 0:// 获取信息
			try {
				blComments = JSON.parseArray(Data.getHttpResultStr(),
						BLComment.class);
			} catch (Exception e) {
				return;
			}
			developJuniorAp.FrashAp(blComments);
			break;

		case 2:// 生成二维码
			String as = Data.getHttpResultStr();

			// BNew bNew=JSON.parseObject(as, BNew.class);

			PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
					AQRCode.class).putExtra("codeBean", as));

			break;
		default:
			break;
		}

	}

	@Override
	protected void DataError(String error, int LoadTyp) {
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

	class DevelopJuniorAp extends BaseAdapter {
		private Context mycContext;
		private LayoutInflater inflater;
		private int ResourceId;
		private List<BLComment> datas = new ArrayList<BLComment>();

		private List<InLsAp> lsAps = new ArrayList<InLsAp>();

		public DevelopJuniorAp(Context mycContext, int resourceId) {
			super();
			this.mycContext = mycContext;
			this.inflater = LayoutInflater.from(mycContext);
			this.ResourceId = resourceId;
		}

		public void FrashAp(List<BLComment> ss) {
			this.datas = ss;
			lsAps = new ArrayList<InLsAp>();
			for (int i = 0; i < ss.size(); i++) {
				lsAps.add(new InLsAp(mycContext, ss.get(i).getLevels()));
			}
			this.notifyDataSetChanged();
		}

		public BLComment GetData(int Postion) {
			return datas.get(Postion);
		}

		public List<InLsAp> GetInAps() {
			return lsAps;
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
			DevelopJuniorItem mDevelopJuniorItem = null;
			if (arg1 == null) {
				mDevelopJuniorItem = new DevelopJuniorItem();
				arg1 = inflater.inflate(ResourceId, null);//
				mDevelopJuniorItem.item_develop_junior_ls = (CompleteListView) arg1
						.findViewById(R.id.item_develop_junior_ls);
				mDevelopJuniorItem.item_develop_junior_logo = ViewHolder.get(
						arg1, R.id.item_develop_junior_logo);
				mDevelopJuniorItem.item_develop_junior_name = ViewHolder.get(
						arg1, R.id.item_develop_junior_name);

				arg1.setTag(mDevelopJuniorItem);

			} else {
				mDevelopJuniorItem = (DevelopJuniorItem) arg1.getTag();
			}
			ImageLoaderUtil.Load2(datas.get(arg0).getAvatar(),
					mDevelopJuniorItem.item_develop_junior_logo,
					R.drawable.error_iv2);

			final int Postion = arg0;
			mDevelopJuniorItem.item_develop_junior_ls.setAdapter(GetInAps()
					.get(Postion));

			mDevelopJuniorItem.item_develop_junior_ls
					.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> aarg0,
								View arg1, int arg2, long arg3) {
							GetInAps().get(Postion).ClickItem(arg2);
						}
					});
			StrUtils.SetTxt(mDevelopJuniorItem.item_develop_junior_name, datas
					.get(arg0).getSeller_name());
			return arg1;
		}

		class DevelopJuniorItem {
			ImageView item_develop_junior_logo;
			TextView item_develop_junior_name;

			CompleteListView item_develop_junior_ls;

		}

	}

	/**
	 * 内部的Ap
	 * 
	 */
	class InLsAp extends BaseAdapter {
		private Context mycContext;
		private LayoutInflater inflater;
		private List<String> datas = new ArrayList<String>();
		private List<Boolean> IsChecks = new ArrayList<Boolean>();

		public InLsAp(Context mycContext, List<String> da) {
			super();
			this.mycContext = mycContext;
			this.inflater = LayoutInflater.from(mycContext);
			this.datas = da;
			for (int i = 0; i < da.size(); i++) {
				IsChecks.add(false);
			}
		}

		/**
		 * 获取boole标记数组
		 */
		public List<Boolean> GetBools() {
			return IsChecks;
		}

		/***
		 * 点击Item时候
		 */
		public void ClickItem(int Postion) {
			for (int i = 0; i < datas.size(); i++) {
				if (i != Postion)
					GetBools().set(i, false);
			}
			GetBools().set(Postion, !GetBools().get(Postion));
			this.notifyDataSetChanged();
		}

		public String GetInId(int Postion) {
			return datas.get(Postion);
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
			InItem inItem = null;
			if (arg1 == null) {
				inItem = new InItem();
				arg1 = inflater.inflate(R.layout.item_develep_junior_in, null);//
				inItem.item_develop_junior_in_selectiv = ViewHolder.get(arg1,
						R.id.item_develop_junior_in_selectiv);
				inItem.item_develop_junior_in_name = ViewHolder.get(arg1,
						R.id.item_develop_junior_in_name);

				arg1.setTag(inItem);
			} else {
				inItem = (InItem) arg1.getTag();
			}
			inItem.item_develop_junior_in_selectiv.setImageResource(IsChecks
					.get(arg0) ? R.drawable.quan_select_3
					: R.drawable.quan_select_2);
			StrUtils.SetTxt(inItem.item_develop_junior_in_name, datas.get(arg0)
					+ "级");
			return arg1;
		}

		class InItem {
			ImageView item_develop_junior_in_selectiv;
			TextView item_develop_junior_in_name;
		}
	}

	@Override
	protected void MyClick(View V) {
		switch (V.getId()) {

		case R.id.btn_produce_two_dimension_code:
			// PromptManager.SkipActivity(BaseActivity, new Intent(BaseContext,
			// AQRCode.class));

			String CodeStr = CreateCode();
			if (StrUtils.isEmpty(CodeStr)) {
				PromptManager.ShowCustomToast(BaseContext, "请选择下级");
				return;
			}
			IUpData(CodeStr);
			break;
		}
	}

	private String CreateCode() {
		List<InLsAp> datas = developJuniorAp.GetInAps();
		if (datas == null || datas.size() == 0) {// 需要单独逻辑判断 需求未定
			return "";
		}
		List<String> BrandCode = new ArrayList<String>();
		List<String> LevelCode = new ArrayList<String>();

		JSONObject obj = new JSONObject();

		for (int i = 0; i < datas.size(); i++) {
			for (int j = 0; j < datas.get(i).GetBools().size(); j++) {
				if (datas.get(i).GetBools().get(j)) {
					BrandCode.add(developJuniorAp.GetData(i).getId());

					LevelCode.add(datas.get(i).GetInId(j));
					try {
						obj.put(developJuniorAp.GetData(i).getId(), datas
								.get(i).GetInId(j));
					} catch (Exception e) {

					}
				}

			}
		}
		return obj.toString();
		// IUpData("2", "4", obj.toString());
		// PromptManager.ShowCustomToast(BaseContext, obj.toString());

	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

}
