package io.vtown.WeiTangApp.ui.comment;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.onConfirmClick;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.oncancelClick;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.comment.view.listview.LListView.IXListViewListener;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.title.ABrandDetail;
import io.vtown.WeiTangApp.ui.title.ABrandJoin;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-20 下午6:34:48
 * @author 所有纯listview的activity TODO确定是否全部为分页加载
 * @author 同时需要所有的javabean的每个Item数据全都是BLComment
 * @author 同时 TODO考虑Item的点击（item里边有先按钮需要点击属于特殊情况另外添加点击事件监听处理）
 * 
 */
public class ACommentList extends ATitleBase implements IXListViewListener {
	private View comment_nodata_lay;
	/**
	 * 商品搜索点击Item跳转过来的商品列表
	 */
	public final static int Tage_SouGoodResultItem = 301;
	/**
	 * 首页的弹出框item0的品牌按钮点击后进来的品牌列表
	 */
	public final static int Tage_HomePopBrand = 302;

	/**
	 * 店铺的渠道管理点击我的上级后进来的列表
	 */
	public final static int Tage_My_Superior = 303;

	/**
	 * 店铺的渠道管理点击我的下级后进来的列表
	 */
	public final static int Tage_My_Junior = 304;
	/**
	 * 我的页面，点击商品关注进来的列表
	 */
	public final static int Tage_ACenterOderGuanzhu = 305;

	/**
	 * 我的页面，点击店铺关注进来的列表
	 */
	public final static int Tage_ACenterShopCollect = 306;

	/**
	 * 我的页面，点击浏览记录进来的列表
	 */
	public final static int Tage_ACenterGoodBrowseRecord = 307;

	/**
	 * 首页popup非品牌-->分类GridView进来的列表
	 */
	public final static int Tage_AGoodSort = 308;
	/**
	 * 公用的传递标识的Key
	 */
	public static String Tage_ResultKey = "CommentKey";
	/**
	 * 传递到界面的key
	 */
	private int Tage_Result = 0;

	/**
	 * 当前分页的页数
	 */
	private int CurrentPage = 1;
	/**
	 * 传递bean需要的key
	 */
	public static String Tage_BeanKey = "CommentBeanKey";

	/**
	 * 传递来的bean
	 */
	private BComment SkipBean = new BComment();
	/**
	 * 公用listview
	 */
	private LListView acomment_list;
	/**
	 * CommentAdapter
	 */

	private CommentAdapter commentAdapter;

	/**
	 * 用户信息
	 */
	private BUser user_Get;





	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_commentlist);
		user_Get = Spuit.User_Get(BaseContext);
		IBundle();
		IView();
		SetTitleHttpDataLisenter(this);
		IData(CurrentPage, LOAD_INITIALIZE);
	}

	@SuppressWarnings("deprecation")
	private void IData(int Page, int LoadType) {
		HashMap<String, String> map = new HashMap<String, String>();
		if (LoadType == LOAD_INITIALIZE) {
			PromptManager.showtextLoading(BaseContext, getResources()
					.getString(R.string.xlistview_header_hint_loading));
		}

		switch (Tage_Result) {
		case Tage_SouGoodResultItem:// 搜索商品列表点击item时候标识

			// map.put("keyword", URLEncoder.encode(SkipBean.getTitle()));
			// try {
			// map.put("keyword", URLEncoder.encode(SkipBean.getTitle()
			// ,"utf-8").replaceAll("\\+","%20"));
			// } catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
			// }
			map.put("keyword", SkipBean.getTitle());
			// try {
			// map.put("keyword", URLEncoder.encode(SkipBean.getTitle(),
			// "utf-8") );
			// } catch (UnsupportedEncodingException e) {
			// e.printStackTrace();
			// }
			map.put("page", Page + "");
			map.put("pagesize", Constants.SouGoodinf_size);
			FBGetHttpData(map, Constants.SouGoodinf, Method.GET,
					Tage_SouGoodResultItem, LoadType);
			break;
		case Tage_HomePopBrand:// 首页的弹出框item0的品牌按钮点击后进来的品牌列表
			// Brand_List
			map.put("page", Page + "");
			map.put("pagesize", Constants.SouGoodinf_size);
			FBGetHttpData(map, Constants.Brand_List, Method.GET,
					Tage_HomePopBrand, LoadType);
			break;

		case Tage_ACenterOderGuanzhu:
			map.put("member_id", user_Get.getId());
			map.put("page", Page + "");
			map.put("pageNum", "");
			FBGetHttpData(map, Constants.CENTER_GOOD_COLLECT, Method.GET,
					Tage_ACenterOderGuanzhu, LoadType);
			break;

		case Tage_ACenterShopCollect:
			map.put("member_id", user_Get.getId());
			map.put("page", Page + "");
			map.put("pageNum", "");
			FBGetHttpData(map, Constants.CENTER_SHOP_COLLECT, Method.GET,
					Tage_ACenterShopCollect, LoadType);
			break;

		case Tage_ACenterGoodBrowseRecord:
			map.put("member_id", user_Get.getId());
			map.put("page", Page + "");
			map.put("pageNum", "");
			FBGetHttpData(map, Constants.CENTER_BROWSE_RECORD, Method.GET,
					Tage_ACenterGoodBrowseRecord, LoadType);
			break;

		case Tage_AGoodSort:

			map.put("seller_id", user_Get.getSeller_id());
			map.put("page", Page + "");
			map.put("pageNum", "");
			map.put("is_agent", "1");
			map.put("category_id", SkipBean.getId());
			FBGetHttpData(map, Constants.Select_Ls, Method.GET, Tage_AGoodSort,
					LoadType);
			break;

		case Tage_My_Junior:
			map.put("seller_id", user_Get.getSeller_id());
			FBGetHttpData(map, Constants.SHOP_CHANNEL_MY_JUNIOR, Method.GET,
					Tage_My_Junior, LoadType);
			break;
		case Tage_My_Superior:
			map.put("seller_id", user_Get.getSeller_id());
			FBGetHttpData(map, Constants.SHOP_CHANNEL_MY_SUPERIOR, Method.GET,
					Tage_My_Superior, LoadType);
			break;
		default:
			break;
		}
	}

	private void IView() {
		comment_nodata_lay = findViewById(R.id.comment_nodata_lay);

		acomment_list = (LListView) findViewById(R.id.acomment_list);
		acomment_list.setAdapter(commentAdapter);

		acomment_list.setPullRefreshEnable(true);
		acomment_list.setPullLoadEnable(true);
		acomment_list.setXListViewListener(this);

		acomment_list.hidefoot();

		IDataView(acomment_list, comment_nodata_lay, NOVIEW_INITIALIZE);
		comment_nodata_lay.setOnClickListener(this);

		// center的商品关注，店铺收藏，浏览记录的缓存
		switch (Tage_Result) {
		case Tage_ACenterOderGuanzhu:// 商品关注
			String GoodsStr = CacheUtil.Guanzhu_Good_Get(BaseContext);
			if (!StrUtils.isEmpty(GoodsStr)) {// 不为空开始缓存
				// PromptManager.ShowCustomToast(BaseContext, "缓存商品");
				List<BLComment> DATA = JSON.parseArray(GoodsStr,
						BLComment.class);
				IDataView(acomment_list, comment_nodata_lay, NOVIEW_RIGHT);
				commentAdapter.Refrsh(DATA);

			}

			break;
		case Tage_ACenterShopCollect:// 店铺收藏
			String ShopsStr = CacheUtil.Guanzhu_Shop_Get(BaseContext);
			if (!StrUtils.isEmpty(ShopsStr)) {// 不为空开始缓存
				// PromptManager.ShowCustomToast(BaseContext, "缓存店铺");
				List<BLComment> DATA = JSON.parseArray(ShopsStr,
						BLComment.class);
				IDataView(acomment_list, comment_nodata_lay, NOVIEW_RIGHT);
				commentAdapter.Refrsh(DATA);
			}

			break;
		case Tage_ACenterGoodBrowseRecord:// 浏览记录
			String LiuLanStr = CacheUtil.Guanzhu_LiuLan_Get(BaseContext);
			if (!StrUtils.isEmpty(LiuLanStr)) {// 不为空开始缓存
				// PromptManager.ShowCustomToast(BaseContext, "缓存浏览");

				List<BLComment> DATA = JSON.parseArray(LiuLanStr,
						BLComment.class);
				IDataView(acomment_list, comment_nodata_lay, NOVIEW_RIGHT);
				commentAdapter.Refrsh(DATA);
			}
			break;
		default:
			break;
		}

		acomment_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (CheckNet(BaseContext))
					return;

				BLComment daa = (BLComment) arg0.getItemAtPosition(arg2);
				BComment d = new BComment(daa.getId(), daa.getSeller_name());
				String goods_id = daa.getId();
				switch (Tage_Result) {
				case Tage_SouGoodResultItem:// 搜索商品列表点击item时候标识
					PromptManager.SkipActivity(BaseActivity, new Intent(
							BaseActivity, AGoodDetail.class).putExtra("goodid",
							goods_id));
					break;
				case Tage_HomePopBrand:// 首页的弹出框item0的品牌按钮点击后进来的品牌列表
					PromptManager.SkipActivity(BaseActivity, new Intent(
							BaseActivity, ABrandDetail.class).putExtra(
							BaseKey_Bean, d));
					break;

				case Tage_My_Superior:

					if (daa.getIs_brand().equals("1")) {
						PromptManager.SkipActivity(BaseActivity, new Intent(
								BaseActivity, ABrandDetail.class).putExtra(
								BaseKey_Bean, d));
					} else {
						PromptManager.SkipActivity(BaseActivity, new Intent(
								BaseActivity, AShopDetail.class).putExtra(
								BaseKey_Bean, d));
					}
					break;

				case Tage_My_Junior:
					PromptManager.SkipActivity(BaseActivity, new Intent(
							BaseActivity, AShopDetail.class).putExtra(
							BaseKey_Bean, d));
					break;

				case Tage_ACenterOderGuanzhu:
					PromptManager.SkipActivity(BaseActivity, new Intent(
							BaseActivity, AGoodDetail.class).putExtra("goodid",
							goods_id));
					break;

				case Tage_ACenterShopCollect:

					if (daa.getIs_brand().equals("1")) {// 品牌商

					} else {// 自营

					}
					PromptManager.SkipActivity(BaseActivity, new Intent(
							BaseActivity,
							daa.getIs_brand().equals("1") ? ABrandDetail.class
									: AShopDetail.class).putExtra(BaseKey_Bean,
							d));

					break;

				case Tage_ACenterGoodBrowseRecord:
					PromptManager.SkipActivity(BaseActivity, new Intent(
							BaseActivity, AGoodDetail.class).putExtra("goodid",
							goods_id));
					break;
				case Tage_AGoodSort:// 在一级分类里面点击分类进来的 产品列表

					PromptManager.SkipActivity(BaseActivity, new Intent(
							BaseActivity, AGoodDetail.class).putExtra("goodid",
							goods_id));
					// BaseActivity.fin

					break;
				default:
					break;
				}
			}
		});
		/**
		 * 长按进行删除或者其他操作
		 */
		acomment_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				BLComment daa = (BLComment) arg0.getItemAtPosition(arg2);
				switch (Tage_Result) {
				case Tage_ACenterOderGuanzhu:// 商品关注
					LongClickDo(daa.getId());
					break;
				case Tage_ACenterShopCollect:// 店铺关注
					LongClickDo(daa.getId());
					break;
				case Tage_ACenterGoodBrowseRecord:// 浏览记录
					LongClickDo(daa.getId());
					break;
				default:
					break;
				}
				return true;
			}
		});

	}

	private void LongClickDo(final String TagerId) {
		String content = "";

		switch (Tage_Result) {
		case Tage_ACenterOderGuanzhu:// 商品关注
			content = "取消该商品关注?";
			break;
		case Tage_ACenterShopCollect:// 店铺关注
			content = "取消该店铺收藏?";
			break;
		case Tage_ACenterGoodBrowseRecord:// 浏览记录
			content = "删除该浏览记录?";
			break;
		default:
			break;
		}

		ShowCustomDialog(content, "取消", "确定", new IDialogResult() {

			@Override
			public void RightResult() {
				DeletNet(TagerId);
			}

			@Override
			public void LeftResult() {
			}
		});

		/*
		 * final CustomDialog dialog = new CustomDialog(BaseContext,
		 * R.style.mystyle, R.layout.customdialog, 3, "取消", "确定");
		 * dialog.show(); String content = "";
		 * 
		 * switch (Tage_Result) { case Tage_ACenterOderGuanzhu:// 商品关注 content =
		 * "取消该商品关注?"; break; case Tage_ACenterShopCollect:// 店铺关注 content =
		 * "取消该店铺收藏?"; break; case Tage_ACenterGoodBrowseRecord:// 浏览记录 content
		 * = "删除该浏览记录?"; break; default: break; } dialog.setTitleText(content);
		 * dialog.setConfirmListener(new onConfirmClick() {
		 * 
		 * @Override public void onConfirmCLick(View v) {
		 * 
		 * dialog.dismiss(); DeletNet(TagerId); } });
		 * dialog.setcancelListener(new oncancelClick() {
		 * 
		 * @Override public void oncancelClick(View v) { dialog.dismiss();
		 * 
		 * } });
		 */

	}

	/**
	 * 删除操作
	 */
	private void DeletNet(String TagerId) {

		// Constants.CancleGuanZhuShop==>删除关注店铺==member_id//seller_id
		// Constants.Good_Attention_Delete==>删除收藏商品==member_id//goods_id
		// Constants.Delete_LiuLan_good==>删除浏览商品==member_id//goods_id
		String HostMy = "";
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("member_id", user_Get.getMember_id());
		switch (Tage_Result) {
		case Tage_ACenterOderGuanzhu:// 商品关注
			HostMy = Constants.Good_Attention_Delete;
			hashMap.put("goods_id", TagerId);
			break;
		case Tage_ACenterShopCollect:// 店铺关注
			HostMy = Constants.CancleGuanZhuShop;
			hashMap.put("seller_id", TagerId);
			break;
		case Tage_ACenterGoodBrowseRecord:// 浏览记录
			HostMy = Constants.Delete_LiuLan_goods;
			hashMap.put("goods_id", TagerId);
			break;
		default:
			break;
		}
		if (StrUtils.isEmpty(HostMy))
			return;

		FBGetHttpData(hashMap, HostMy, Method.DELETE, 111, LOAD_INITIALIZE);
	}

	/**
	 * 处理bund
	 */
	private void IBundle() {
		if (!getIntent().getExtras().containsKey(Tage_ResultKey))
			BaseActivity.finish();

		Tage_Result = getIntent().getIntExtra(Tage_ResultKey, 0);
		switch (Tage_Result) {
		case Tage_SouGoodResultItem: // **Tage_SouGoodResultItem和*Tage_AGoodSort一个item*****************************
			SkipBean = (BComment) getIntent().getExtras().getSerializable(
					Tage_BeanKey);
			// commentAdapter = new CommentAdapter(BaseContext,
			// R.layout.item_acommentlist_goodsousou_in,
			// Tage_SouGoodResultItem);
			commentAdapter = new CommentAdapter(R.layout.item_good_category,
					Tage_AGoodSort);
			break;
		case Tage_HomePopBrand:// 首页的弹出框item0的品牌按钮点击后进来的品牌列表
			SkipBean = (BComment) getIntent().getExtras().getSerializable(
					Tage_BeanKey);
			commentAdapter = new CommentAdapter(R.layout.item_home_brand,
					Tage_HomePopBrand);
			break;

		case Tage_My_Superior:// 店铺->渠道管理->我的上级
			// SkipBean = (BComment) getIntent().getExtras().getSerializable(
			// Tage_BeanKey);
			commentAdapter = new CommentAdapter(R.layout.item_my_superior,
					Tage_My_Superior);
			break;

		case Tage_My_Junior:// 店铺->渠道管理->我的下级
			// SkipBean = (BComment) getIntent().getExtras().getSerializable(
			// Tage_BeanKey);
			commentAdapter = new CommentAdapter(R.layout.item_my_junior,
					Tage_My_Junior);
			break;

		case Tage_ACenterOderGuanzhu:// 我的->商品关注
			commentAdapter = new CommentAdapter(R.layout.item_concern_goods,
					Tage_ACenterOderGuanzhu);
			break;

		case Tage_ACenterShopCollect:// 我的->店铺关注
			commentAdapter = new CommentAdapter(R.layout.item_concern_shop,
					Tage_ACenterShopCollect);
			break;

		case Tage_ACenterGoodBrowseRecord:
			commentAdapter = new CommentAdapter(
					R.layout.item_center_browse_record,
					Tage_ACenterGoodBrowseRecord);
			break;

		case Tage_AGoodSort: // **Tage_SouGoodResultItem和*Tage_AGoodSort一个item*****************************

			SkipBean = (BComment) getIntent().getExtras().getSerializable(
					Tage_BeanKey);
			commentAdapter = new CommentAdapter(R.layout.item_good_category,
					Tage_AGoodSort);
			break;

		default:
			break;
		}
	}

	@Override
	protected void InitTile() {
		switch (Tage_Result) {
		case Tage_SouGoodResultItem:
			SetTitleTxt(SkipBean.getTitle());
			break;
		case Tage_HomePopBrand:// 首页的弹出框item0的品牌按钮点击后进来的品牌列表

			SetTitleTxt("品牌");
			// SetRightText("入驻");
			// right_txt.setOnClickListener(this);
			break;
		case Tage_My_Superior:
			SetTitleTxt(getResources().getString(R.string.my_superior));
			acomment_list.setDividerHeight(DimensionPixelUtil.dip2px(
					BaseContext, 10));
			acomment_list.setDivider(getResources().getDrawable(
					R.color.transparent));
			break;
		case Tage_My_Junior:
			SetTitleTxt(getResources().getString(R.string.my_junior));
			acomment_list.setDividerHeight(DimensionPixelUtil.dip2px(
					BaseContext, 10));
			acomment_list
					.setDivider(getResources().getDrawable(R.color.app_bg));
			break;

		case Tage_ACenterOderGuanzhu:
			SetTitleTxt(getResources().getString(R.string.center_good_guanzhu));

			break;

		case Tage_ACenterShopCollect:
			SetTitleTxt(getResources().getString(R.string.center_shop_collect));
			break;
		case Tage_ACenterGoodBrowseRecord:
			SetTitleTxt(getResources().getString(R.string.center_jilu));
			SetRightText("清空");
			right_txt.setOnClickListener(this);
			right_txt.setVisibility(View.GONE);
			break;
		case Tage_AGoodSort:
			SetTitleTxt(SkipBean.getTitle());

			break;
		default:
			SetTitleTxt("微糖");
			break;
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

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				acomment_list.stopRefresh();
			}
			if (msg.what == 2) {
				acomment_list.stopLoadMore();
			}
		}
	};

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {

		if (Data.getHttpResultTage() == 111) {
			PromptManager.ShowCustomToast(BaseContext, "删除成功");
			CacheUtil.Guanzhu_LiuLan_Delete(BaseContext);
			CurrentPage = 1;
			IData(CurrentPage, LOAD_INITIALIZE);
			return;
		}

		if (StrUtils.isEmpty(Data.getHttpResultStr())
				&& Data.getHttpLoadType() != LOAD_LOADMOREING) {
			// PromptManager.ShowCustomToast(BaseContext, Msg);
			commentAdapter.Refrsh(new ArrayList<BLComment>());
			right_txt.setVisibility(View.GONE);
			IDataView(acomment_list, comment_nodata_lay, NOVIEW_ERROR);
			DataError(Constants.SucessToError, Data.getHttpLoadType());
			return;
		}
		if (StrUtils.isEmpty(Data.getHttpResultStr()) && Data.getHttpLoadType() == LOAD_LOADMOREING) {
			// PromptManager.ShowCustomToast(BaseContext, Msg);

			acomment_list.stopLoadMore();
			return;
		}
		
		List<BLComment> DATA = JSON.parseArray(Data.getHttpResultStr(),
				BLComment.class);



		// 开始缓存center的商品关注，店铺收藏，浏览记录的缓存***************************

		switch (Tage_Result) {
		case Tage_ACenterOderGuanzhu:// 商品关注
			CacheUtil.Guanzhu_Good_Save(BaseContext, Data.getHttpResultStr());

			break;
		case Tage_ACenterShopCollect:// 店铺收藏
			CacheUtil.Guanzhu_Shop_Save(BaseContext, Data.getHttpResultStr());

			break;
		case Tage_ACenterGoodBrowseRecord:// 浏览记录
			CacheUtil.Guanzhu_LiuLan_Save(BaseContext, Data.getHttpResultStr());

			break;
		default:
			break;
		}
		// 开始缓存center的商品关注，店铺收藏，浏览记录的缓存**************************
		switch (Data.getHttpLoadType()) {
		case LOAD_INITIALIZE:
			commentAdapter.Refrsh(DATA);
			right_txt.setVisibility(View.VISIBLE);
			IDataView(acomment_list, comment_nodata_lay, NOVIEW_RIGHT);
			break;
		case LOAD_REFRESHING:
			Message m = new Message();
			m.what = 1;
			mHandler.sendMessage(m);
			commentAdapter.Refrsh(DATA);
			break;
		case LOAD_LOADMOREING:
			Message mm = new Message();
			mm.what = 2;
			mHandler.sendMessage(mm);
			commentAdapter.AddFrash(DATA);
			break;
		default:
			break;
		}

		if (DATA.size() < Constants.PageSize)
			acomment_list.hidefoot();
		else
			acomment_list.ShowFoot();
		// 如果是渠道管理 需要ls需要一个div的高度

		// switch (Tage_Result) {
		// case Tage_SouGoodResultItem:// 搜索商品列表点击item时候标识
		// commentAdapter.Refrsh(DATA);
		// break;
		// case Tage_HomePopBrand:// 首页的弹出框item0的品牌按钮点击后进来的品牌列表
		// commentAdapter.Refrsh(DATA);
		// break;
		//
		// case Tage_My_Junior:
		//
		// break;
		// case Tage_My_Superior:
		// break;
		// default:
		// break;
		// }

		// 111
	}

	@Override
	protected void DataError(String error, int LoadTyp) {
		PromptManager.ShowCustomToast(BaseContext, error);
		switch (LoadTyp) {
		case LOAD_INITIALIZE:
			if (Tage_Result == Tage_ACenterOderGuanzhu) {// 商品关注有缓存
				if (!StrUtils.isEmpty(CacheUtil.Guanzhu_Good_Get(BaseContext)))
					break;
			}

			if (Tage_Result == Tage_ACenterShopCollect) {// 店铺收藏
				if (!StrUtils.isEmpty(CacheUtil.Guanzhu_Shop_Get(BaseContext)))
					break;
			}

			if (Tage_Result == Tage_ACenterGoodBrowseRecord) {// 浏览记录
				if (!StrUtils
						.isEmpty(CacheUtil.Guanzhu_LiuLan_Get(BaseContext))){
					right_txt.setVisibility(View.VISIBLE);
					break;
				}else{
					right_txt.setVisibility(View.GONE);
				}


			}

			PromptManager.ShowCustomToast(BaseContext, error);
			IDataView(acomment_list, comment_nodata_lay, NOVIEW_ERROR);
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
	protected void MyClick(View V) {

		switch (V.getId()) {
		case R.id.right_txt://清空收藏商品记录
			//PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
			//		ABrandJoin.class));

			if (CheckNet(BaseContext))return;

			ShowCustomDialog("确定清空所有商品浏览记录吗？", "取消", "确定", new IDialogResult() {

				@Override
				public void RightResult() {
					DeletNet("");

				}

				@Override
				public void LeftResult() {
				}
			});

			break;
		case R.id.comment_nodata_lay:
			IData(CurrentPage, LOAD_INITIALIZE);
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

	/**
	 * 搜索结果的列表AP
	 */
	private class CommentAdapter extends BaseAdapter {

		/**
		 * 填充器
		 */
		private LayoutInflater inflater;
		/**
		 * 资源id
		 */
		private int ResourceId;
		/**
		 * 当前的Tage_ResultKey
		 */
		private int TageId;

		private List<BLComment> datas = new ArrayList<BLComment>();

		public CommentAdapter(int resourceId, int TageId) {
			super();

			this.inflater = LayoutInflater.from(BaseContext);
			this.ResourceId = resourceId;
			this.TageId = TageId;
		}

		public void Refrsh(List<BLComment> da) {
			this.datas = da;
			notifyDataSetChanged();
		}

		public void AddFrash(List<BLComment> das) {
			this.datas.addAll(das);
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
			MyItem myItem = null;
			MyBrandItem brandItem = null;
			MySuperiorItem superiorItem = null;
			MyJuniorItem myJunior = null;
			CenterGoodCollectItem centerGoodCollect = null;
			CenterShopCollectItem centerShopCollect = null;
			CenterBrowseRecordItem centerBrowseRecord = null;
			GoodSortItem goodSort = null;
			if (convertView == null) {
				convertView = inflater.inflate(ResourceId, null);
				switch (TageId) {
				case Tage_SouGoodResultItem:
					myItem = new MyItem();
					myItem.sss = ViewHolder.get(convertView, R.id.sss);//
					convertView.setTag(myItem);
					break;
				case Tage_HomePopBrand:// 首页的弹出框item0的品牌按钮点击后进来的品牌列表
					brandItem = new MyBrandItem();
					brandItem.item_home_brand_iv = ViewHolder.get(convertView,
							R.id.item_home_brand_iv);

					LinearLayout.LayoutParams mParams = new LayoutParams(
							screenWidth, screenWidth / 2);
					brandItem.item_home_brand_iv.setLayoutParams(mParams);

					convertView.setTag(brandItem);
					break;

				case Tage_My_Junior:// 渠道 我的下级

					myJunior = new MyJuniorItem();
					myJunior.iv_my_junior_shop_logo = ViewHolder.get(
							convertView, R.id.iv_my_junior_shop_logo);
					myJunior.rl_my_superior_item_bag = ViewHolder.get(
							convertView, R.id.rl_my_superior_item_bag);

					myJunior.tv_my_junior_shop_desc = ViewHolder.get(
							convertView, R.id.tv_my_junior_shop_desc);
					myJunior.tv_my_junior_shop_title = ViewHolder.get(
							convertView, R.id.tv_my_junior_shop_title);
					myJunior.tv_my_junior_team_number = ViewHolder.get(
							convertView, R.id.tv_my_junior_team_number);
					myJunior.tv_my_junior_junior_number = ViewHolder.get(
							convertView, R.id.tv_my_junior_junior_number);
					convertView.setTag(myJunior);
					/*
					 * myJunior.rl_my_superior_item_bag = ViewHolder.get(
					 * convertView, R.id.rl_my_superior_item_bag);
					 */
					break;
				case Tage_My_Superior:// 渠道 我的上级
					superiorItem = new MySuperiorItem();
					superiorItem.iv_my_superior_shop_logo = ViewHolder.get(
							convertView, R.id.iv_my_superior_shop_logo);
					superiorItem.tv_my_superior_shop_desc = ViewHolder.get(
							convertView, R.id.tv_my_superior_shop_desc);

					// superiorItem.rl_my_superior_item_bag_iv = ViewHolder.get(
					// convertView, R.id.rl_my_superior_item_bag_iv);

					superiorItem.tv_my_superior_shop_title = ViewHolder.get(
							convertView, R.id.tv_my_superior_shop_title);
					superiorItem.tv_my_superior_team_number = ViewHolder.get(
							convertView, R.id.tv_my_superior_team_number);
					superiorItem.tv_my_superior_junior_number = ViewHolder.get(
							convertView, R.id.tv_my_superior_junior_number);
					convertView.setTag(superiorItem);
					break;
				case Tage_ACenterOderGuanzhu:
					centerGoodCollect = new CenterGoodCollectItem();
					centerGoodCollect.iv_center_good_collect_good_icon = ViewHolder
							.get(convertView,
									R.id.iv_center_good_collect_good_icon);
					centerGoodCollect.iv_center_good_collect_good_level = ViewHolder
							.get(convertView,
									R.id.iv_center_good_collect_good_level);
					centerGoodCollect.tv_center_good_collect_good_title = ViewHolder
							.get(convertView,
									R.id.tv_center_good_collect_good_title);
					centerGoodCollect.tv_center_good_collect_good_price = ViewHolder
							.get(convertView,
									R.id.tv_center_good_collect_good_price);
					centerGoodCollect.tv_center_good_collect_good_rule1 = ViewHolder
							.get(convertView,
									R.id.tv_center_good_collect_good_rule1);
					centerGoodCollect.tv_center_good_collect_good_rule2 = ViewHolder
							.get(convertView,
									R.id.tv_center_good_collect_good_rule2);
					centerGoodCollect.tv_center_good_collect_good_rule3 = ViewHolder
							.get(convertView,
									R.id.tv_center_good_collect_good_rule3);
					centerGoodCollect.ll_center_good_collect_good_rule1 = ViewHolder
							.get(convertView,
									R.id.ll_center_good_collect_good_rule1);
					centerGoodCollect.ll_center_good_collect_good_rule2 = ViewHolder
							.get(convertView,
									R.id.ll_center_good_collect_good_rule2);
					centerGoodCollect.ll_center_good_collect_good_rule3 = ViewHolder
							.get(convertView,
									R.id.ll_center_good_collect_good_rule3);
					convertView.setTag(centerGoodCollect);
					break;

				case Tage_ACenterShopCollect:
					centerShopCollect = new CenterShopCollectItem();
					centerShopCollect.iv_shop_collect_shop_icon = ViewHolder
							.get(convertView, R.id.iv_shop_collect_shop_icon);
					centerShopCollect.tv_shop_collect_shop_desc = ViewHolder
							.get(convertView, R.id.tv_shop_collect_shop_desc);
					centerShopCollect.tv_shop_collect_shop_title = ViewHolder
							.get(convertView, R.id.tv_shop_collect_shop_title);
					convertView.setTag(centerShopCollect);
					break;

				case Tage_ACenterGoodBrowseRecord:
					centerBrowseRecord = new CenterBrowseRecordItem();
					centerBrowseRecord.iv_center_browse_record_good_level = ViewHolder
							.get(convertView,
									R.id.iv_center_browse_record_good_level);
					centerBrowseRecord.iv_center_browse_record_icon = ViewHolder
							.get(convertView, R.id.iv_center_browse_record_icon);
					centerBrowseRecord.tv_center_browse_record_good_title = ViewHolder
							.get(convertView,
									R.id.tv_center_browse_record_good_title);
					centerBrowseRecord.tv_center_browse_record_price = ViewHolder
							.get(convertView,
									R.id.tv_center_browse_record_price);
					centerBrowseRecord.tv_center_browse_record_rule1 = ViewHolder
							.get(convertView,
									R.id.tv_center_browse_record_rule1);
					centerBrowseRecord.tv_center_browse_record_rule2 = ViewHolder
							.get(convertView,
									R.id.tv_center_browse_record_rule2);
					centerBrowseRecord.tv_center_browse_record_rule3 = ViewHolder
							.get(convertView,
									R.id.tv_center_browse_record_rule3);
					centerBrowseRecord.ll_center_browse_record_rule1 = ViewHolder
							.get(convertView,
									R.id.ll_center_browse_record_rule1);
					centerBrowseRecord.ll_center_browse_record_rule2 = ViewHolder
							.get(convertView,
									R.id.ll_center_browse_record_rule2);
					centerBrowseRecord.ll_center_browse_record_rule3 = ViewHolder
							.get(convertView,
									R.id.ll_center_browse_record_rule3);
					convertView.setTag(centerBrowseRecord);
					break;

				case Tage_AGoodSort:
					goodSort = new GoodSortItem();
					convertView = inflater.inflate(ResourceId, null);
					goodSort.iv_good_category_good_icon = ViewHolder.get(
							convertView, R.id.iv_good_category_good_icon);
					goodSort.tv_good_category_good_price = ViewHolder.get(
							convertView, R.id.tv_good_category_good_price);
					goodSort.tv_good_category_good_title = ViewHolder.get(
							convertView, R.id.tv_good_category_good_title);
					convertView.setTag(goodSort);
					break;

				}

			} else {
				switch (TageId) {
				case Tage_SouGoodResultItem:
					myItem = (MyItem) convertView.getTag();
					break;
				case Tage_HomePopBrand:// 首页的弹出框item0的品牌按钮点击后进来的品牌列表
					brandItem = (MyBrandItem) convertView.getTag();
					break;

				case Tage_My_Superior:
					superiorItem = (MySuperiorItem) convertView.getTag();
					break;
				case Tage_My_Junior:
					myJunior = (MyJuniorItem) convertView.getTag();
					break;

				case Tage_ACenterOderGuanzhu:
					centerGoodCollect = (CenterGoodCollectItem) convertView
							.getTag();
					break;

				case Tage_ACenterShopCollect:
					centerShopCollect = (CenterShopCollectItem) convertView
							.getTag();
					break;

				case Tage_ACenterGoodBrowseRecord:
					centerBrowseRecord = (CenterBrowseRecordItem) convertView
							.getTag();
					break;

				case Tage_AGoodSort:
					goodSort = (GoodSortItem) convertView.getTag();
					break;

				default:
					break;
				}

			}
			BLComment data = datas.get(position);
			switch (TageId) {
			case Tage_SouGoodResultItem:
				myItem.sss.setText(data.getTitle());
				break;
			case Tage_HomePopBrand:// 首页的弹出框item0的品牌按钮点击后进来的品牌列表

				// ViewGroup.LayoutParams lp = brandItem.item_home_brand_iv
				// .getLayoutParams();
				// lp.width = screenWidth;
				// lp.height = LayoutParams.WRAP_CONTENT;
				// brandItem.item_home_brand_iv.setScaleType(ScaleType.FIT_XY);
				// brandItem.item_home_brand_iv.setLayoutParams(lp);
				//
				// brandItem.item_home_brand_iv.setMaxWidth(screenWidth);
				// brandItem.item_home_brand_iv.setMaxHeight(screenWidth * 2);

				ImageLoaderUtil.Load2(data.getCover(),
						brandItem.item_home_brand_iv, R.drawable.error_iv1);
				break;
			case Tage_My_Superior:// 我的上级
				ImageLoaderUtil.Load2(data.getAvatar(),
						superiorItem.iv_my_superior_shop_logo,
						R.drawable.error_iv2);

				// ImageLoaderUtil.Load2(data.getAvatar(),
				// superiorItem.rl_my_superior_item_bag_iv,
				// R.drawable.error_iv1);
				StrUtils.SetTxt(superiorItem.tv_my_superior_shop_title,
						data.getSeller_name());
				StrUtils.SetTxt(superiorItem.tv_my_superior_shop_desc,
						data.getIntro());
				StrUtils.SetTxt(superiorItem.tv_my_superior_team_number,
						StrUtils.NullToStr(data.getTeamCounter()) + "人");

				break;
			case Tage_My_Junior:// 我的下级
				ImageLoaderUtil.Load2(data.getAvatar(),
						myJunior.iv_my_junior_shop_logo, R.drawable.error_iv2);
				ImageLoaderUtil.LoadGaosi(BaseContext, data.getCover(),
						myJunior.rl_my_superior_item_bag,
						R.drawable.item_shangji_iv,0);

				/*
				 * ImageLoaderUtil.Load2(data.getCover(),
				 * myJunior.rl_my_superior_item_bag, R.drawable.error_iv2);
				 */
				StrUtils.SetTxt(myJunior.tv_my_junior_shop_title,
						data.getSeller_name());
				StrUtils.SetTxt(myJunior.tv_my_junior_shop_desc,
						data.getIntro());

				StrUtils.SetTxt(myJunior.tv_my_junior_team_number,
						data.getTeamCounter());

				break;
			case Tage_ACenterOderGuanzhu:
				ImageLoaderUtil.Load(data.getCover(),
						centerGoodCollect.iv_center_good_collect_good_icon,
						R.drawable.error_iv2);
				StrUtils.SetTxt(
						centerGoodCollect.tv_center_good_collect_good_title,
						data.getTitle());
				StrUtils.SetTxt(
						centerGoodCollect.tv_center_good_collect_good_price,
						StrUtils.SetTextForMony(data.getSell_price()) + "元");
				String is_agent = data.getIs_agent();
				if ("0".equals(is_agent)) {
					centerGoodCollect.iv_center_good_collect_good_level
							.setVisibility(View.GONE);
				} else if ("1".equals(is_agent)) {
					centerGoodCollect.iv_center_good_collect_good_level
							.setVisibility(View.VISIBLE);
				}
				break;
			case Tage_ACenterShopCollect:
				ImageLoaderUtil.Load2(data.getAvatar(),
						centerShopCollect.iv_shop_collect_shop_icon,
						R.drawable.error_iv2);
				StrUtils.SetTxt(centerShopCollect.tv_shop_collect_shop_desc,
						data.getIntro());
				StrUtils.SetTxt(centerShopCollect.tv_shop_collect_shop_title,
						data.getSeller_name());
				break;

			case Tage_ACenterGoodBrowseRecord:

				ImageLoaderUtil.Load2(data.getCover(),
						centerBrowseRecord.iv_center_browse_record_icon,
						R.drawable.error_iv2);
				StrUtils.SetTxt(
						centerBrowseRecord.tv_center_browse_record_good_title,
						data.getTitle());
				StrUtils.SetTxt(
						centerBrowseRecord.tv_center_browse_record_price,
						StrUtils.SetTextForMony(data.getSell_price()) + "元");
				String is_agent2 = data.getIs_agent();
				if ("0".equals(is_agent2)) {
					centerBrowseRecord.iv_center_browse_record_good_level
							.setVisibility(View.GONE);
				} else if ("1".equals(is_agent2)) {
					centerBrowseRecord.iv_center_browse_record_good_level
							.setVisibility(View.VISIBLE);
				}

				break;
			case Tage_AGoodSort:
				try {
					ImageLoaderUtil.Load(data.getCover(),
							goodSort.iv_good_category_good_icon,
							R.drawable.error_iv2);
				} catch (Exception e) {
					// TODO: handle exception
				}

				StrUtils.SetTxt(goodSort.tv_good_category_good_title,
						data.getTitle());
				StrUtils.SetTxt(
						goodSort.tv_good_category_good_price,
						String.format("价格: %s元",
								StrUtils.SetTextForMony(data.getSell_price())));
				break;

			default:
				break;
			}

			return convertView;
		}

		// 搜索商品进去的列表
		class MyItem {
			TextView sss;
		}

		class MyBrandItem {
			ImageView item_home_brand_iv;
		}

		class MySuperiorItem {
			TextView tv_my_superior_shop_desc, tv_my_superior_shop_title,
					tv_my_superior_team_number, tv_my_superior_junior_number;
			// ImageView rl_my_superior_item_bag_iv;// 我的上级的背景图
			ImageView iv_my_superior_shop_logo;

		}

		class MyJuniorItem {
			TextView tv_my_junior_shop_desc, tv_my_junior_shop_title,
					tv_my_junior_team_number, tv_my_junior_junior_number;
			ImageView iv_my_junior_shop_logo;

			ImageView rl_my_superior_item_bag;// 背景图片
		}

		class CenterGoodCollectItem {
			ImageView iv_center_good_collect_good_level,
					iv_center_good_collect_good_icon;
			TextView tv_center_good_collect_good_title,
					tv_center_good_collect_good_rule1,
					tv_center_good_collect_good_rule2,
					tv_center_good_collect_good_rule3,
					tv_center_good_collect_good_price;
			LinearLayout ll_center_good_collect_good_rule1,
					ll_center_good_collect_good_rule2,
					ll_center_good_collect_good_rule3;

		}

		class CenterShopCollectItem {
			ImageView iv_shop_collect_shop_icon;
			TextView tv_shop_collect_shop_title, tv_shop_collect_shop_desc;
		}

		class CenterBrowseRecordItem {
			ImageView iv_center_browse_record_good_level,
					iv_center_browse_record_icon;
			TextView tv_center_browse_record_good_title,
					tv_center_browse_record_rule1,
					tv_center_browse_record_rule2,
					tv_center_browse_record_rule3,
					tv_center_browse_record_price;
			LinearLayout ll_center_browse_record_rule1,
					ll_center_browse_record_rule2,
					ll_center_browse_record_rule3;

		}

		class GoodSortItem {
			ImageView iv_good_category_good_icon;
			TextView tv_good_category_good_title, tv_good_category_good_price;
		}
	}

	@Override
	public void onRefresh() {
		CurrentPage = 1;

		IData(CurrentPage, LOAD_REFRESHING);

	}

	@Override
	public void onLoadMore() {
		CurrentPage = CurrentPage + 1;
		IData(CurrentPage, LOAD_LOADMOREING);

	}
}
