package io.vtown.WeiTangApp.ui.title.shop;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcache.BHome;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BLShopDaiLi;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.ACommentList;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;
import io.vtown.WeiTangApp.ui.title.ABrandDetail;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-11 下午2:37:54
 * @see 商铺里面的品牌代理
 */
public class ABrandDaiLi extends ATitleBase {

	/**
	 * 品牌代理列表
	 */
	private ListView brand_daili_ls;
	/**
	 * AP
	 */
	private BrandDaiLiAp brandDaiLiAp;
	/**
	 * 添加品牌按钮
	 */
	private ImageView iv_add_brand_item;
	private TextView tv_add_brand_item;

	// 无数据时候品牌的添加
	private View brand_daili_add;
	/**
	 * 用户信息
	 */
	private BUser user_Get;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_brand_daili);
		user_Get = Spuit.User_Get(BaseContext);
		IBase();
		ICache();
		IData();
	}

	/**
	 * 读取缓存
	 */
	private void ICache() {
		if (!StrUtils.isEmpty(CacheUtil.Shop_Brand_Get(BaseContext))) {// 存在缓存

			List<BLShopDaiLi> listdata = new ArrayList<BLShopDaiLi>();
			try {
				listdata = JSON.parseArray(
						CacheUtil.Shop_Brand_Get(BaseContext),
						BLShopDaiLi.class);
				brand_daili_ls.setVisibility(View.VISIBLE);
				brandDaiLiAp.FrashData(listdata);
				if (brandDaiLiAp.getCount() == 0) {
					tv_add_brand_item.setVisibility(View.VISIBLE);
				}

			} catch (Exception e) {

			}

		} else {
			PromptManager.showtextLoading(BaseContext, getResources()
					.getString(R.string.loading));
		}
	}

	private void IData() {

		SetTitleHttpDataLisenter(this);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", user_Get.getSeller_id());
		map.put("page", "1");
		map.put("pagesize", Constants.PageSize1 + "");
		FBGetHttpData(map, Constants.SHOP_BRAND_AGENT, Method.GET, 0,
				LOAD_INITIALIZE);
	}

	/**
	 * 查看证书 触发事件
	 */
	private void LookCertificate(String seller_id, String agency_id) {
		PromptManager.showtextLoading(BaseContext,
				getResources().getString(R.string.getzs_loading));
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", seller_id);
		map.put("agency_id", agency_id);
		FBGetHttpData(map, Constants.SHOP_BRAND_AGENT_LOOK_CERTIFICATE,
				Method.GET, 1, LOAD_INITIALIZE);

	}

	private void ApplyLevel(String seller_id, String apply_id) {
		PromptManager.showtextLoading(BaseContext,
				getResources().getString(R.string.loading));
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", seller_id);
		map.put("agency_id", apply_id);
		FBGetHttpData(map, Constants.SHOP_BRAND_AGENT_APPLY_LEVEL, Method.POST,
				2, LOAD_INITIALIZE);
	}

	private void IBase() {
		brand_daili_add = findViewById(R.id.brand_daili_add);
		brand_daili_add.setOnClickListener(this);
		brand_daili_ls = (ListView) findViewById(R.id.brand_daili_ls);
		IFootView();
		brandDaiLiAp = new BrandDaiLiAp(BaseContext, R.layout.item_branddaili);
		brand_daili_ls.setAdapter(brandDaiLiAp);
		brand_daili_ls.setVisibility(View.GONE);
		brand_daili_ls.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				BLShopDaiLi myda = (BLShopDaiLi) arg0.getItemAtPosition(arg2);
				if (!StrUtils.isEmpty(myda.getIs_brand())
						&& myda.getIs_brand().equals("1")) {
					Intent mIntent = new Intent(BaseActivity,
							ABrandDetail.class);
					mIntent.putExtra(BaseKey_Bean, new BComment(myda.getId(),
							myda.getSeller_name()));
					PromptManager.SkipActivity(BaseActivity, mIntent);

				}

			}
		});
	}

	private void IFootView() {
		View view = LayoutInflater.from(BaseContext).inflate(
				R.layout.item_branddaili_foot, null);
		brand_daili_ls.addFooterView(view);
		iv_add_brand_item = (ImageView) view
				.findViewById(R.id.iv_add_brand_item);
		tv_add_brand_item = (TextView) view
				.findViewById(R.id.tv_add_brand_item);
		LinearLayout ll_brand_list_foot = (LinearLayout) view
				.findViewById(R.id.ll_brand_list_foot);
		ll_brand_list_foot.setOnClickListener(this);

	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.shop_oder_brand_daili));
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {

		switch (Data.getHttpResultTage()) {
		case 0:
			if (StrUtils.isEmpty(Data.getHttpResultStr())) {
				CacheUtil.Shop_Brand_Save(BaseContext, Data.getHttpResultStr());
				brand_daili_ls.setVisibility(View.VISIBLE);
				brandDaiLiAp.FrashData(new ArrayList<BLShopDaiLi>());
				break;
			}
			List<BLShopDaiLi> listdata = new ArrayList<BLShopDaiLi>();
			try {
				CacheUtil.Shop_Brand_Save(BaseContext, Data.getHttpResultStr());
				listdata = JSON.parseArray(Data.getHttpResultStr(),
						BLShopDaiLi.class);
				brand_daili_ls.setVisibility(View.VISIBLE);
				brandDaiLiAp.FrashData(listdata);
				if (brandDaiLiAp.getCount() == 0) {
					tv_add_brand_item.setVisibility(View.VISIBLE);
				}

			} catch (Exception e) {
				DataError("解析失败", 1);
			}
			break;
		case 1:// 是查看证书
			String aS = Data.getHttpResultStr();
			String a[] = { aS };

			Intent mIntent = new Intent(BaseContext, AphotoPager.class);
			mIntent.putExtra("position", 0);
			mIntent.putExtra("urls", a);

			PromptManager.SkipActivity(BaseActivity, mIntent);
			break;

		case 2:// 是申请升级
			PromptManager.ShowCustomToast(BaseContext, Msg);
			break;
		default:
			break;
		}

	}

	@Override
	protected void DataError(String error, int LoadTyp) {
		PromptManager.ShowCustomToast(BaseContext, error);
	}

	/**
	 * 品牌代理的列表的Ap
	 */
	class BrandDaiLiAp extends BaseAdapter {
		private Context mycContext;
		private LayoutInflater inflater;
		private int ResourceId;
		private List<BLShopDaiLi> datas = new ArrayList<BLShopDaiLi>();

		public BrandDaiLiAp(Context mycContext, int resourceId) {
			super();
			this.mycContext = mycContext;
			this.inflater = LayoutInflater.from(mycContext);
			this.ResourceId = resourceId;
		}

		public void FrashData(List<BLShopDaiLi> da) {
			this.datas = da;
			notifyDataSetChanged();
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
			BrandDaiLiItem brandDaiLiItem = null;
			if (null == arg1) {
				brandDaiLiItem = new BrandDaiLiItem();
				arg1 = inflater.inflate(ResourceId, null);
				brandDaiLiItem.iv_brand_agent_icon = ViewHolder.get(arg1,
						R.id.iv_brand_agent_icon);
				brandDaiLiItem.tv_brand_agent_shop_name = ViewHolder.get(arg1,
						R.id.tv_brand_agent_shop_name);

				brandDaiLiItem.tv_brand_agent_level = ViewHolder.get(arg1,
						R.id.tv_brand_agent_level);
				brandDaiLiItem.tv_brand_agent_agency_time = ViewHolder.get(
						arg1, R.id.tv_brand_agent_agency_time);
				brandDaiLiItem.tv_brand_agent_look_certificate = ViewHolder
						.get(arg1, R.id.tv_brand_agent_look_certificate);
				brandDaiLiItem.tv_brand_agent_apply_level = ViewHolder.get(
						arg1, R.id.tv_brand_agent_apply_level);

				arg1.setTag(brandDaiLiItem);
			} else {
				brandDaiLiItem = (BrandDaiLiItem) arg1.getTag();
			}
			ImageLoaderUtil.Load2(datas.get(arg0).getAvatar(),
					brandDaiLiItem.iv_brand_agent_icon, R.drawable.error_iv2);

			BLShopDaiLi blComment = datas.get(arg0);
			StrUtils.SetTxt(brandDaiLiItem.tv_brand_agent_shop_name,
					blComment.getSeller_name());

			String agency_time = getString(R.string.brand_agent_agency_time);

			StrUtils.SetTxt(brandDaiLiItem.tv_brand_agent_agency_time,
					String.format(agency_time, blComment.getAgency_time()));

			int level = Integer.parseInt(blComment.getLevel());
			switch (level) {
			case 0:
				brandDaiLiItem.tv_brand_agent_level
						.setText(getString(R.string.brand_agent_level_0));
				break;
			case 1:
				brandDaiLiItem.tv_brand_agent_level
						.setText(getString(R.string.brand_agent_level_1));
				break;
			case 2:
				brandDaiLiItem.tv_brand_agent_level
						.setText(getString(R.string.brand_agent_level_2));
				break;
			case 3:
				brandDaiLiItem.tv_brand_agent_level
						.setText(getString(R.string.brand_agent_level_3));
				break;
			case 4:
				brandDaiLiItem.tv_brand_agent_level
						.setText(getString(R.string.brand_agent_level_4));
				break;
			case 5:
				brandDaiLiItem.tv_brand_agent_level
						.setText(getString(R.string.brand_agent_level_5));
				break;
			}

			onClickEvent(brandDaiLiItem, blComment);

			return arg1;
		}

		class BrandDaiLiItem {

			ImageView iv_brand_agent_icon;
			TextView tv_brand_agent_shop_name, tv_brand_agent_level,
					tv_brand_agent_agency_time,
					tv_brand_agent_look_certificate,
					tv_brand_agent_apply_level;

		}

		void onClickEvent(BrandDaiLiItem brandDaiLiItem,
				final BLShopDaiLi blComment) {
			brandDaiLiItem.tv_brand_agent_look_certificate
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							 LookCertificate(user_Get.getSeller_id(),
							 blComment.getId());

//							String aS = blComment.getCredential();
//							if (StrUtils.isEmpty(aS)) {
//								LookCertificate(user_Get.getSeller_id(), blComment.getId());
//								return;
//							}
//							String a[] = { aS };
//
//							Intent mIntent = new Intent(BaseContext,
//									AphotoPager.class);
//							mIntent.putExtra("position", 0);
//							mIntent.putExtra("urls", a);
//
//							PromptManager.SkipActivity(BaseActivity, mIntent);
						}
					});

			brandDaiLiItem.tv_brand_agent_apply_level
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							ApplyLevel(user_Get.getSeller_id(),
									blComment.getId());
						}
					});
		}
	}

	@Override
	protected void NetConnect() {
		NetError.setVisibility(View.GONE);

		// 无网络==》有网络
		if (brandDaiLiAp.getCount() == 0)
			IData();

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
		case R.id.ll_brand_list_foot:
			toBrandList();
//			PromptManager.ShowCustomToast(BaseContext, "有数据的foot");
			break;
		case R.id.brand_daili_add:
			toBrandList();
//			PromptManager.ShowCustomToast(BaseContext, "无数据的pop");
			break;
		default:
			break;
		}

	}

	private void toBrandList() {
		BHome bHome = new BHome();
		BComment datas = new BComment(bHome.getId(), bHome.getCate_name());
		Intent intent = new Intent(BaseActivity, ACommentList.class);
		intent.putExtra(ACommentList.Tage_ResultKey,
				ACommentList.Tage_HomePopBrand);
		intent.putExtra(ACommentList.Tage_BeanKey, datas);
		PromptManager.SkipActivity(BaseActivity, intent);
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

}
