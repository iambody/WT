package io.vtown.WeiTangApp.ui.derls;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BGoodDetail;
import io.vtown.WeiTangApp.bean.bcomment.easy.BLGoodManger;
import io.vtown.WeiTangApp.bean.bcomment.easy.BLShopDaiLi;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BLShow;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.bean.bcomment.news.BNew;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.horizontalscroll.HBaseAdapter;
import io.vtown.WeiTangApp.comment.view.custom.horizontalscroll.HorizontalScrollMenu;
import io.vtown.WeiTangApp.comment.view.listview.HorizontalListView;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.comment.view.listview.LListView.IXListViewListener;
import io.vtown.WeiTangApp.comment.view.pop.PShare;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.fragment.FShopGoodManger;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.ACommentList;
import io.vtown.WeiTangApp.ui.comment.AGoodShare;
import io.vtown.WeiTangApp.ui.comment.AGoodVidoShare;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.shop.goodmanger.AAlterBrandNumber;
import io.vtown.WeiTangApp.ui.title.shop.goodmanger.AGoodMangerEdit;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-8-18 下午6:01:46
 * 
 */
public class AGoodManger extends ATitleBase implements IXListViewListener {
	private HorizontalScrollMenu activity_ls_good_odermanger_container;
	/**
	 * 头部的title
	 */
	private String[] TitleNames = new String[] { "在售中", "已下架", "品牌商品", "垃圾桶" };
	// **********************************fragment
	final static int GetGood_Detail = 777;// 垃圾箱=》彻底删除

	final static int Manger_XiaJia = 301;// 在售中=》下架
	final static int Manger_ShanChu = 302;// 在售中=》删除
	final static int Manger_ShangJia = 303;// 已下架=》上架
	final static int Manger_HuiFu = 304;// 垃圾箱=》恢复
	final static int Manger_CheDi_ShanChu = 305;// 垃圾箱=》彻底删除
	final static int Manger_ShanJia = 308;// ==>品牌的列表=》上架
	final static int Manger_Brand_Number = 309;// 修改品牌商品库存
	final static int Manger_Good_Alter = 445;// 垃圾箱=》彻底删除
	final static int Manger_Good_Attrs = 335;// 垃圾箱=》彻底删除
	/**
	 * 横线滑动的ls只在品牌fragmen页面显示 其他页面隐藏 不做控制
	 */
	private HorizontalListView fragment_shop_good_manger_brand_horizon_ls;
	/**
	 * 横线滑动ls对应的ap
	 */
	private BrandAp brandAp;
	/**
	 * 在售中、已下架共同的ListView
	 */
	private LListView lv_comment_listview;

	private TextView tv_add_item;

	// 未获取到数据时候需要进行的
	private View fragent_goodmanger_nodata_lay;

	private TextView fragment_isbrand_brand_txt, fragment_isbrand_ziying_txt;
	private LinearLayout fragment_isbrand_lay;

	/**
	 * 当前订单状态 100在售中;;20 已经下架;;0品牌商品;;1标识垃圾箱
	 */
	private int Sale_Status = 100;

	/**
	 * AP
	 */
	private MYCommentAdapter mycommentAdapter;

	private BUser user_Get;

	private int CurrentPage = 1;

	private String CurrentBrandId = "";

	private boolean IsBrand = false;

	/**
	 * 记录下当前的fragment是否进行了去修改库存的操作 如果是才进行操作
	 */

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_oderls_goodmanger);
		user_Get = Spuit.User_Get(BaseContext);
		SetTitleHttpDataLisenter(this);
		EventBus.getDefault().register(this, "DataNeddFrash", BMessage.class);

		InItView();
	}

	public void DataNeddFrash(BMessage message) {
		if (message.getMessageType() == BMessage.Good_Manger_ToEdit_num) {

			LoadData(CurrentPage, LOAD_INITIALIZE);

		}
	}

	private void InItView() {
		activity_ls_good_odermanger_container = (HorizontalScrollMenu) findViewById(R.id.activity_ls_good_odermanger_container);
		activity_ls_good_odermanger_container.setSwiped(true);
		IFragView();
		activity_ls_good_odermanger_container.setAdapter(new MenuAdapter());
		activity_ls_good_odermanger_container.initMenuItems3(BaseContext,
				screenWidth / 4);

	}

	// 初始化fagment里面的空间
	private void IFragView() {
		fragment_isbrand_lay = (LinearLayout) findViewById(R.id.fragment_isbrand_lay);
		fragment_isbrand_brand_txt = (TextView) findViewById(R.id.fragment_isbrand_brand_txt);
		fragment_isbrand_ziying_txt = (TextView) findViewById(R.id.fragment_isbrand_ziying_txt);
		fragment_isbrand_brand_txt.setOnClickListener(this);
		fragment_isbrand_ziying_txt.setOnClickListener(this);
		fragent_goodmanger_nodata_lay = findViewById(R.id.fragent_goodmanger_nodata_lay);
		lv_comment_listview = (LListView) findViewById(R.id.lv_comment_listview);
		tv_add_item = (TextView) findViewById(R.id.tv_add_item);
		lv_comment_listview.setPullRefreshEnable(true);
		lv_comment_listview.setPullLoadEnable(true);
		lv_comment_listview.setXListViewListener(this);

		// 品牌商品需要的view控件
		fragment_shop_good_manger_brand_horizon_ls = (HorizontalListView) findViewById(R.id.fragment_shop_good_manger_brand_horizon_ls);

		lv_comment_listview.setVisibility(View.VISIBLE);
	}

	// 切换数据
	private void SetCurrentPostion(int postion) {

		switch (postion) {
		case 0:
			Sale_Status = 100;
			fragment_isbrand_lay.setVisibility(View.VISIBLE);
			fragment_shop_good_manger_brand_horizon_ls.setVisibility(View.GONE);
			break;
		case 1:
			Sale_Status = 20;
			fragment_isbrand_lay.setVisibility(View.VISIBLE);
			fragment_shop_good_manger_brand_horizon_ls.setVisibility(View.GONE);
			break;
		case 2:
			Sale_Status = 0;
			fragment_isbrand_lay.setVisibility(View.GONE);
			fragment_shop_good_manger_brand_horizon_ls
					.setVisibility(View.VISIBLE);
			break;
		case 3:
			Sale_Status = 1;
			fragment_isbrand_lay.setVisibility(View.VISIBLE);
			fragment_shop_good_manger_brand_horizon_ls.setVisibility(View.GONE);
			break;
		default:
			break;
		}
		InItFragment();
		LoadData(CurrentPage, LOAD_INITIALIZE);
	}

	public void InItFragment() {
		lv_comment_listview.hidefoot();
		mycommentAdapter = new MYCommentAdapter(Sale_Status,
				R.layout.item_selling_and_soldout);
		lv_comment_listview.setAdapter(mycommentAdapter);
		brandAp = new BrandAp(R.layout.item_fragment_shop_good_manger_brand);
		fragment_shop_good_manger_brand_horizon_ls.setAdapter(brandAp);
		switch (Sale_Status) {
		case 100:// 在售中

			break;
		case 20:// 已经下架

			break;
		case 0:// 品牌商品ss
			fragment_isbrand_lay.setVisibility(View.GONE);
			tv_add_item.setVisibility(View.VISIBLE);
			fragment_shop_good_manger_brand_horizon_ls
					.setVisibility(View.VISIBLE);
			// lv_comment_listview.setPullRefreshEnable(false);
			// lv_comment_listview.setPullLoadEnable(false);
			tv_add_item.setOnClickListener(this);

			IGetBrands();// 获取品牌的列表

			break;
		case 1:// 垃圾箱

			break;
		default:
			break;
		}
		lv_comment_listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				BLGoodManger blComment = (BLGoodManger) arg0
						.getItemAtPosition(arg2);
				switch (Sale_Status) {
				case 100:// 在售中

					PromptManager.SkipActivity(BaseActivity, new Intent(
							BaseContext, AGoodDetail.class).putExtra("goodid",
							blComment.getId()));
					break;
				case 20:// 已经下架

					PromptManager.SkipActivity(BaseActivity, new Intent(
							BaseContext, AGoodDetail.class).putExtra("goodid",
							blComment.getId()));
					break;
				case 0:// 品牌商品
					PromptManager.SkipActivity(BaseActivity, new Intent(
							BaseContext, AGoodDetail.class).putExtra("goodid",
							blComment.getId()));
					break;
				case 1:// 垃圾箱
					PromptManager.SkipActivity(BaseActivity, new Intent(
							BaseContext, AGoodDetail.class).putExtra("goodid",
							blComment.getId()));
					break;
				default:
					break;
				}
			}
		});

	}

	// 加载列表
	private void LoadData(int page, int LoadType) {
		switch (Sale_Status) {
		case 100:// 在售中
			IData(LoadType, Sale_Status, page, IsBrand ? 1 : 0, 0);// -1=>全部
																	// 0=>自营商品
																	// 1=>品牌商品
			break;
		case 20:// 已经下架
			IData(LoadType, Sale_Status, page, IsBrand ? 1 : 0, 0);
			break;
		case 0:// 品牌商品
			/**
			 * 获取品牌的列表
			 */
			// IData(LoadType, 100, page, 1, 0);

			GetBrandLs(LoadType, page);// 获取品牌商品的列表
			break;
		case 1:// 垃圾箱
			IData(LoadType, 20, page, IsBrand ? 1 : 0, 1);
			break;
		default:
			break;
		}
	}

	/**
	 * 获取品牌商品
	 */
	public void GetBrandLs(int LoadType, int Page) {

		if (LoadType == LOAD_INITIALIZE)
			PromptManager.showtextLoading(BaseContext, getResources()
					.getString(R.string.loading));
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("agency_id", CurrentBrandId);
		map.put("pagesize", Constants.PageSize + "");
		map.put("page", Page + "");// 售卖状态100在售/////20下架
		map.put("seller_id", user_Get.getSeller_id());
		FBGetHttpData(map, Constants.GoodCanSellList, Method.GET, 0, LoadType);
	}

	/**
	 * 
	 * @param LoadType
	 * @param sale_status售卖状态100在售
	 *            /////20下架
	 * @param Page
	 * @param Is_agent
	 *            // 0-自营商品 1-品牌商品
	 * @param Is_delete
	 *            // 0-正常 1-已删除
	 */
	private void IData(int LoadType, int sale_status, int Page, int Is_agent,
			int Is_delete) {
		if (LoadType == LOAD_INITIALIZE) {
			PromptManager.showtextLoading(BaseContext, getResources()
					.getString(R.string.loading));
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", user_Get.getSeller_id());
		map.put("sale_status", sale_status + "");// 售卖状态100在售/////20下架
		map.put("page", Page + "");
		map.put("pagesize", Constants.PageSize + "");
		map.put("is_agent", Is_agent + "");// 0-自营商品 1-品牌商品
		map.put("is_delete", Is_delete + "");// 0-正常 1-已删除
		map.put("category_id", "");// 分类暂时没用 默认空

		FBGetHttpData(map, Constants.GOODS_MANAGE_LIST, Method.GET, 0, LoadType);
	}

	/**
	 * 获取品牌的列表
	 */
	private void IGetBrands() {
		PromptManager.showtextLoading(BaseContext,
				getResources().getString(R.string.loading));
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", user_Get.getSeller_id());
		map.put("page", "1");
		map.put("pagesize", "100");
		FBGetHttpData(map, Constants.SHOP_BRAND_AGENT, Method.GET, 55,
				LOAD_INITIALIZE);
	}

	// 获取商品详情*********************
	private void GetGoodDetail(String GoodId) {
		PromptManager.showtextLoading(BaseContext,
				getResources()
						.getString(R.string.xlistview_header_hint_loading));
		SetTitleHttpDataLisenter(this);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("goods_id", GoodId);
		map.put("extend", "1");
		map.put("member_id", user_Get.getId());
		map.put("seller_id", user_Get.getSeller_id());
		FBGetHttpData(map, Constants.GoodDetail, Method.GET, GetGood_Detail,
				LOAD_INITIALIZE);
	}

	class MYCommentAdapter extends BaseAdapter {

		// ArrayList<SellingSoldoutItem> items;
		// ArrayList<SellingSoldoutItem>();

		/**
		 * 填充器
		 */
		private LayoutInflater inflater;
		/**
		 * 资源id
		 */
		private int ResourceId;
		/**
		 * 数据
		 */
		private List<BLGoodManger> mydatas = new ArrayList<BLGoodManger>();

		private int showType;

		public MYCommentAdapter(int type, int ResourceId) {
			super();
			this.showType = type;

			this.ResourceId = ResourceId;
			this.inflater = LayoutInflater.from(BaseContext);

		}

		/**
		 * 刷新数据
		 * 
		 * @param dass
		 */
		public void FrashData(List<BLGoodManger> dass) {
			this.mydatas = dass;
			// items = new
			// ArrayList<FShopGoodManger.MYCommentAdapter.SellingSoldoutItem>();
			// for (int i = 0; i < dass.size(); i++) {
			// items.add(new SellingSoldoutItem());
			// }

			this.notifyDataSetChanged();

		}

		/**
		 * 加载更多
		 */
		public void AddFrashData(List<BLGoodManger> dassss) {
			this.mydatas.addAll(dassss);

			// for (int i = 0; i < dassss.size(); i++) {
			// items.add(new SellingSoldoutItem());
			// }
			this.notifyDataSetChanged();
		}

		@Override
		public int getCount() {

			return mydatas.size();
		}

		@Override
		public Object getItem(int arg0) {

			return mydatas.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			SellingSoldoutItem holder = null;

			if (arg1 == null) {
				// arg1 = ViewHolder.ToView(BaseContext,
				// R.layout.item_selling_and_soldout);
				arg1 = inflater.inflate(ResourceId, null);
				holder = new SellingSoldoutItem();
				// holder.rb_select = ViewHolder.get(arg1, R.id.rb_select);

				holder.iv_good_pic = (ImageView) arg1
						.findViewById(R.id.iv_good_pic2);// ViewHolder.get(arg1,
				// R.id.iv_good_pic);
				holder.tv_delete = ViewHolder.get(arg1, R.id.tv_delete);
				holder.tv_edit = ViewHolder.get(arg1, R.id.tv_edit);
				holder.tv_good_price = ViewHolder.get(arg1, R.id.tv_good_price);
				holder.tv_good_title = ViewHolder.get(arg1, R.id.tv_good_title);
				holder.tv_inventory = ViewHolder.get(arg1, R.id.tv_inventory);
				holder.tv_sales = ViewHolder.get(arg1, R.id.tv_sales);
				holder.tv_share_to = ViewHolder.get(arg1, R.id.tv_share_to);
				holder.tv_sold_out = ViewHolder.get(arg1, R.id.tv_sold_out);
				holder.tv_good_tag = ViewHolder.get(arg1, R.id.tv_good_tag);
				holder.view_vertical_line1 = ViewHolder.get(arg1,
						R.id.view_vertical_line1);
				holder.view_vertical_line2 = ViewHolder.get(arg1,
						R.id.view_vertical_line2);
				holder.view_vertical_line3 = ViewHolder.get(arg1,
						R.id.view_vertical_line3);
				// items.add(holder);
				// ImageLoaderUtil.Load2(mydatas.get(arg0).getCover(),
				// holder.iv_good_pic, R.drawable.error_iv2);
				arg1.setTag(holder);

			} else {
				holder = (SellingSoldoutItem) arg1.getTag();

			}

			// final SellingSoldoutItem holderItem = holder;
			// final int Postion = arg0;

			ImageLoaderUtil.Load2(mydatas.get(arg0).getCover(),
					holder.iv_good_pic, R.drawable.error_iv2);

			holder.tv_good_tag
					.setText((mydatas.get(arg0).getIs_agent() != null && mydatas
							.get(arg0).getIs_agent().equals("1")) ? "品" : "自");
			holder.tv_good_tag
					.setBackgroundColor(getResources()
							.getColor(
									(mydatas.get(arg0).getIs_agent() != null && mydatas
											.get(arg0).getIs_agent()
											.equals("1")) ? R.color.green
											: R.color.blue));

			ItemData(holder, arg0, mydatas.get(arg0));

			holder.tv_good_price.setText(StrUtils.SetTextForMony(mydatas.get(
					arg0).getSell_price())
					+ "元");
			holder.tv_good_title.setText(mydatas.get(arg0).getTitle());
			holder.tv_inventory.setText(mydatas.get(arg0).getStore());
			holder.tv_sales.setText(mydatas.get(arg0).getSales());

			return arg1;

		}

		private void ItemData(SellingSoldoutItem holder, int postion,
				BLGoodManger mBlComment) {
			switch (showType) {
			case 100:// 在售中
			case 20:// 已经下架
				OnClickEvent(holder, showType, postion, mBlComment);
				if (showType == 20) {
					holder.tv_sold_out.setText("上架");
				}

				// if (showType == 100) {// 在售中的商品 判断是否可以编辑
				if (mBlComment.getIs_edit() != null
						&& mBlComment.getIs_edit().equals("1")) {// 可以编辑

					holder.tv_edit.setText("编辑");

					holder.tv_edit.setVisibility(View.VISIBLE);
					holder.view_vertical_line1.setVisibility(View.VISIBLE);
				} else {// 不可编辑
					holder.tv_edit.setVisibility(View.GONE);
					holder.view_vertical_line1.setVisibility(View.GONE);
					// holder.tv_edit.setText("不可编辑");

				}

				// }
				if (100 == showType) {
					holder.tv_delete.setVisibility(View.GONE);
					holder.view_vertical_line3.setVisibility(View.GONE);
				}
				if (20 == showType)
					holder.tv_share_to.setVisibility(View.GONE);

				if (showType == 100) {// 在售中
					if (mBlComment.getIs_agent().equals("1")) {
						holder.tv_delete.setVisibility(View.VISIBLE);
						holder.tv_delete.setText("修改库存");
						holder.view_vertical_line3.setVisibility(View.VISIBLE);
					}

				} else {// 已经下架
					if (mBlComment.getIs_agent().equals("1")) {
						holder.tv_edit.setVisibility(View.VISIBLE);
						holder.tv_edit.setText("修改库存");
						holder.view_vertical_line1.setVisibility(View.VISIBLE);
					}
				}
				break;

			case 0:// 品牌
				holder.tv_edit.setVisibility(View.GONE);
				holder.view_vertical_line1.setVisibility(View.GONE);
				holder.tv_sold_out.setText("采购");
				holder.tv_share_to.setText("虚库到家");
				holder.tv_share_to.setVisibility(View.GONE);
				OnClickEvent(holder, showType, postion, mBlComment);

				// holder.tv_delete.setText("删除");
				// holder.tv_delete.setVisibility(View.GONE);
				// holder.tv_delete.setText("上架");

				holder.view_vertical_line2.setVisibility(View.GONE);
				holder.view_vertical_line1.setVisibility(View.GONE);

				if (mBlComment.getStatus() == 0) {// 可以上架
					holder.tv_delete.setText("上架");
					holder.tv_delete.setVisibility(View.VISIBLE);
					holder.view_vertical_line3.setVisibility(View.VISIBLE);
				} else {
					holder.tv_delete.setVisibility(View.GONE);
					holder.view_vertical_line3.setVisibility(View.GONE);
				}

				// holder.tv_sold_out.setBackgroundDrawable(getResources()
				// .getDrawable(R.drawable.select_brand_caigou));
				holder.tv_sold_out.setTextColor(getResources().getColor(
						R.color.app_fen));
				break;

			case 1:// 垃圾桶
				holder.tv_edit.setVisibility(View.GONE);
				holder.view_vertical_line1.setVisibility(View.GONE);
				holder.tv_sold_out.setVisibility(View.GONE);
				holder.view_vertical_line2.setVisibility(View.GONE);
				holder.tv_share_to.setText("恢复");
				holder.tv_delete.setText("彻底删除");
				OnClickEvent(holder, showType, postion, mBlComment);
				break;

			}
		}

		// 在售中******* 和已下架**********绑定的事件************
		private void OnClickEvent(final SellingSoldoutItem holder,
				final int clickType, final int postion,
				final BLGoodManger mBlComment) {
			holder.tv_edit.setOnClickListener(new OnClickListener() {// 编辑事件

						@Override
						public void onClick(View arg0) {
							// showType==100是在售///////////showType=200是下架

							switch (clickType) {
							case 100:
								PromptManager.SkipActivity(BaseActivity,
										new Intent(BaseContext,
												AGoodMangerEdit.class)
												.putExtra("goodid",
														mBlComment.getId()));

								break;
							case 20:// 已下架
								ShowReminder("确定修改库存?", Manger_Brand_Number,
										mydatas.get(postion));
								break;
							default:
								break;
							}

						}
					});

			holder.tv_share_to.setOnClickListener(new OnClickListener() {// 分享事件

						@Override
						public void onClick(View arg0) {
							switch (clickType) {
							case 100:// 在售中=》item
								// BNew mBNew = new BNew();
								// mBNew.setShare_title(mBlComment.getTitle());
								// mBNew.setShare_log(mBlComment.getCover());
								// mBNew.setShare_content("微糖商城,努力做的更好");
								// mBNew.setShare_url(mBlComment.getGoods_url());
								//
								// ShowP(LayoutInflater.from(BaseContext).inflate(
								// R.layout.fragment_shop_good_manger,
								// null), mBNew);

								// 开始获取商品详情数据
								// 获取后进行转发界面*********************************
								GetGoodDetail(mBlComment.getId());

								break;

							case 20:// 已下架=》item
								// PromptManager.SkipActivity(
								// (Activity) BaseContext, new Intent(
								// BaseContext,
								// AVirtualLibToHome.class));
								break;
							case 1:// 垃圾箱=》恢复
								ShowReminder("确定恢复?", Manger_HuiFu,
										mydatas.get(postion));
								break;
							case 0:// 品牌商品=》虚库到家
									// PromptManager.SkipActivity(BaseActivity,
									// new Intent(BaseActivity,
									// AVirtualLibToHome.class));
								break;
							}

						}
					});
			holder.tv_sold_out.setOnClickListener(new OnClickListener() {// 下架上架事件

						@Override
						public void onClick(View arg0) {
							switch (clickType) {
							case 100:// 在售中=》下架
								ShowReminder("确定下架?", Manger_XiaJia,
										mydatas.get(postion));
								break;

							case 20:// 已下架=》上架
								ShowReminder("确定上架?", Manger_ShangJia,
										mydatas.get(postion));
								// PPurchase pShowVirtualLibGood = new
								// PPurchase(
								// BaseContext, 200,
								// PPurchase.TYPE_SHOP_GOOD_MANAGER_CAIGOU,
								// mydatas, mydatas.get(postion).getId());
								// pShowVirtualLibGood.showAtLocation(BaseView,
								// Gravity.CENTER, 0, 0);
								break;
							case 0:// 品牌商品=》采购

								// GetAttars(mydatas.get(postion).getId());
								BLGoodManger datas = mydatas.get(postion);
								// Intent mIntent = new Intent(BaseContext,
								// AGoodDetail.class);
								// mIntent.putExtra("goodid", datas.getId());
								// mIntent.putExtra(AGoodDetail.Tage_CaiGou,
								// true);
								// PromptManager.SkipActivity(BaseActivity,
								// mIntent);

								PromptManager
										.SkipActivity(
												BaseActivity,
												new Intent(BaseActivity,
														AGoodDetail.class)
														.putExtra("goodid",
																datas.getId())
														.putExtra(
																AGoodDetail.Tage_CaiGou,
																true));

								break;
							}
						}
					});
			holder.tv_delete.setOnClickListener(new OnClickListener() {// 删除事件

						@Override
						public void onClick(View arg0) {
							switch (clickType) {
							// case 100:// 在售中=》删除
							// ShowReminder("确定删除?", Manger_ShanChu,
							// mydatas.get(postion));

							case 100:// 在售中=》修改库存
								ShowReminder("确定修改库存?", Manger_Brand_Number,
										mydatas.get(postion));
								break;
							case 20:// 已下架=》删除
								ShowReminder("确定删除?", Manger_ShanChu,
										mydatas.get(postion));
								break;
							case 0:// 品牌商的列表 进行上架操作
								ShowReminder("确定上架该商品?", Manger_ShanJia,
										mydatas.get(postion));

								break;
							case 1:// 垃圾箱=》彻底删除
								ShowReminder("确定彻底删除?", Manger_CheDi_ShanChu,
										mydatas.get(postion));
								break;
							default:
								break;
							}

						}

					});
		}

		/**
		 * 微信分享弹出框 BVivew 代表目前activity的view
		 */
		protected void ShowP(View BVivew, BNew bNew) {
			PShare da = new PShare(BaseContext, bNew);
			da.showAtLocation(BVivew, Gravity.BOTTOM, 0, 0);

		}

		class SellingSoldoutItem {

			ImageView iv_good_pic;
			TextView tv_good_title, tv_inventory, tv_sales, tv_good_price,
					tv_edit, tv_sold_out, tv_share_to, tv_delete, tv_good_tag;
			View view_vertical_line1, view_vertical_line2, view_vertical_line3;

		}

	}

	/**
	 * 在售=》删除下架 &&垃圾箱=》恢复彻底删除
	 * 
	 * @param X
	 * @param Title
	 */
	private void ShowReminder(String Title, final int Type,
			final BLGoodManger data) {

		ShowCustomDialog(Title, "取消", "确定", new IDialogResult() {

			@Override
			public void RightResult() {
				if (Manger_Brand_Number == Type) {// 修改库存
					PromptManager.SkipActivity(BaseActivity, new Intent(
							BaseContext, AAlterBrandNumber.class).putExtra(
							"goodid", data.getId()));

					return;
				}

				RequestHttp(Type, data);
			}

			@Override
			public void LeftResult() {
			}
		});

	}

	/**
	 * 删除下架上架等的请求操作
	 * 
	 * @param Type
	 * @param data
	 */
	private void RequestHttp(int Type, BLGoodManger data) {

		int Methode = -1000;
		String Host = null;
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("goods_id", data.getId());
		map.put("seller_id", Spuit.User_Get(BaseContext).getSeller_id());
		switch (Type) {
		case Manger_XiaJia:
			Host = Constants.Good_Manger_XiaJia;// 在售中=》下架PUT
			Methode = Method.PUT;
			break;
		case Manger_ShanChu:
			Host = Constants.Good_Manger_Delete;// 在售中=》删除DELETE
			Methode = Method.DELETE;
			break;
		case Manger_ShangJia:
			Host = Constants.Good_Manger_ShangJia;// 已下架=》上架PUT
			Methode = Method.PUT;
			break;
		case Manger_HuiFu:
			Host = Constants.Good_Manger_HuiFu;// 垃圾箱=》恢复PUT
			Methode = Method.PUT;
			break;
		case Manger_CheDi_ShanChu:// 垃圾箱=》彻底删除DELETE
			Host = Constants.Good_Manger_Delete_Chedi;
			Methode = Method.DELETE;
			break;
		case Manger_ShanJia:// 上架
			Host = Constants.GoodsDaiLi;// 品牌==》上架
			Methode = Method.POST;
			break;
		default:
			break;
		}
		if (-1000 == Methode || null == Host)
			return;
		PromptManager.showtextLoading(BaseContext,
				getResources().getString(R.string.loading));
		FBGetHttpData(map, Host, Methode, Type, LOAD_INITIALIZE);
	}

	// 品牌商的横向列表********************************
	/**
	 * 只有品牌商品中使用到的横向滑动的listview
	 */

	private class BrandAp extends BaseAdapter {

		private LayoutInflater inflater;
		private int ResourceId;
		private List<BLShopDaiLi> datas = new ArrayList<BLShopDaiLi>();
		private List<MyBrandItem> Views= new ArrayList<MyBrandItem>();;

		public BrandAp(int resourceId) {
			super();

			this.inflater = LayoutInflater.from(BaseContext);
			this.ResourceId = resourceId;

		}

		public void Refrsh(List<BLShopDaiLi> da) {
			this.datas = da;
//			Views	= new ArrayList<MyBrandItem>();
//			for(int i=0;i<da.size();i++){
//				Views.add(new MyBrandItem());
//			}
			notifyDataSetChanged();
//			IColor();
		}

		/**
		 * 刷新万数据后立马调用
		 */
		public void IColor() {
			if (getCount() > 0) {
				Views.get(0).item_fragment_shop_good_manger_brand_name
						.setBackground(getResources().getDrawable(
								R.drawable.shape_comment_oval_pre));
				Views.get(0).item_fragment_shop_good_manger_brand_name
						.setTextColor(getResources().getColor(R.color.app_fen));
			}
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
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			MyBrandItem myItem = null;
			if (convertView == null) {
				myItem =  new MyBrandItem();
				convertView = inflater.inflate(ResourceId, null);
				myItem.item_fragment_shop_good_manger_brand_name = (TextView) convertView
						.findViewById(R.id.item_fragment_shop_good_manger_brand_name);
				convertView.setTag(myItem);

//				if(Views.contains(myItem))
				Views.add(myItem);

			} else {
				myItem = (MyBrandItem) convertView.getTag();
			}

			BLShopDaiLi blShopDaiLi = datas.get(arg0);
			// myItem.item_fragment_shop_good_manger_brand_name.setText()
			StrUtils.SetTxt(myItem.item_fragment_shop_good_manger_brand_name,
					blShopDaiLi.getSeller_name());
			myItem.item_fragment_shop_good_manger_brand_name
					.setOnClickListener(new HorizontalItemClikListener(Views,
							arg0, blShopDaiLi));// TODO后期需要传入正确的BLComment数据bean
												// 目前为null
			return convertView;
		}

		class MyBrandItem {
			TextView item_fragment_shop_good_manger_brand_name;
		}

		class HorizontalItemClikListener implements OnClickListener {
			private List<MyBrandItem> Viewsdata = new ArrayList<MyBrandItem>();// 所有的view
			private int Postion;// 记录点击的位置
			private BLShopDaiLi blComment;// 记录品牌的数据bean

			public HorizontalItemClikListener(List<MyBrandItem> viewsdata,
					int postion, BLShopDaiLi data) {
				super();
				Viewsdata = viewsdata;
				Postion = postion;
				blComment = data;
			}

			@Override
			public void onClick(View arg0) {
				for (int i = 0; i < Viewsdata.size(); i++) {
					Viewsdata.get(i).item_fragment_shop_good_manger_brand_name
							.setBackground(getResources()
									.getDrawable(
											Postion == i ? R.drawable.shape_comment_oval_pre
													: R.drawable.shape_comment_oval));
					Viewsdata.get(i).item_fragment_shop_good_manger_brand_name
							.setTextColor(getResources().getColor(
									Postion == i ? R.color.app_fen
											: R.color.grey));

				}
				// 开始刷新
				CurrentBrandId = blComment.getId();
				GetBrandLs(LOAD_INITIALIZE, CurrentPage);
			}
		}
	}

	@Override
	protected void InitTile() {
		SetTitleTxt("商品管理");
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {

		switch (Data.getHttpResultTage()) {
		case 0:// 获取订单列表

			List<BLGoodManger> dattaa = new ArrayList<BLGoodManger>();// BLComment
			try {
				if (!StrUtils.isEmpty(Data.getHttpResultStr())) {// 数据部位空
					dattaa = JSON.parseArray(Data.getHttpResultStr(),
							BLGoodManger.class);
					fragent_goodmanger_nodata_lay.setVisibility(View.GONE);
				} else {// 数据为空
					if (Sale_Status != 0)
						fragent_goodmanger_nodata_lay
								.setVisibility(View.VISIBLE);
				}

			} catch (Exception e) {
				onError("解析错误", 1);
			}

			switch (Data.getHttpLoadType()) {
			case LOAD_INITIALIZE:// 初始化
			case LOAD_REFRESHING:// 刷新数据
				// if (Data.getHttpLoadType() == INITIALIZE) {
				// new java.util.Timer().schedule(new TimerTask() {
				//
				// @Override
				// public void run() {
				// PromptManager.SkipActivity(BaseActivity, new
				// Intent(BaseActivity, ANull.class));
				// this.cancel();
				// }
				// }, 4*1000);
				// }
				mycommentAdapter.FrashData(dattaa);

				// for(int i=0;i<mycommentAdapter.getItems().size();i++){
				// ImageLoaderUtil.Load2(dattaa.get(i).getCover().trim(),
				// mycommentAdapter.getItems().get(i).iv_good_pic,
				// R.drawable.error_iv2);
				// }

				if (dattaa.size() == Constants.PageSize)
					lv_comment_listview.ShowFoot();
				if (dattaa.size() < Constants.PageSize)
					lv_comment_listview.hidefoot();
				if (Data.getHttpLoadType() == LOAD_REFRESHING)
					lv_comment_listview.stopRefresh();

				break;
			case LOAD_LOADMOREING:// 加载更多
				mycommentAdapter.AddFrashData(dattaa);
				if (dattaa.size() == Constants.PageSize)
					lv_comment_listview.ShowFoot();
				if (dattaa.size() < Constants.PageSize)
					lv_comment_listview.hidefoot();
				lv_comment_listview.stopLoadMore();
				break;
			}
			break;
		// 当前订单状态 100在售中;;20 已经下架;;0品牌商品;;1标识垃圾箱
		case Manger_XiaJia:// 在售中=》下架
			PromptManager.ShowCustomToast(BaseContext, "下架成功");
			CurrentPage = 1;
			LoadData(CurrentPage, LOAD_INITIALIZE);
			EventBus.getDefault().post(new BMessage(20));
			break;
		case Manger_ShanChu:// 在售中=》删除
			PromptManager.ShowCustomToast(BaseContext, "删除成功");
			CurrentPage = 1;
			LoadData(CurrentPage, LOAD_INITIALIZE);
			EventBus.getDefault().post(new BMessage(1));
			break;
		case Manger_ShangJia:// 已下架=》上架
			PromptManager.ShowCustomToast(BaseContext, "上架成功");
			CurrentPage = 1;
			LoadData(CurrentPage, LOAD_INITIALIZE);
			EventBus.getDefault().post(new BMessage(100));
			break;
		case Manger_HuiFu:// 垃圾箱=》恢复
			PromptManager.ShowCustomToast(BaseContext, "恢复成功");
			CurrentPage = 1;
			LoadData(CurrentPage, LOAD_INITIALIZE);
			EventBus.getDefault().post(new BMessage(100));
			break;
		case Manger_CheDi_ShanChu:// 垃圾箱=》彻底删除
			PromptManager.ShowCustomToast(BaseContext, "清理成功");
			CurrentPage = 1;
			LoadData(CurrentPage, LOAD_INITIALIZE);
			break;
		case Manger_ShanJia:// 品牌==》上架
			PromptManager.ShowCustomToast(BaseContext, "上架成功");
			CurrentPage = 1;
			LoadData(CurrentPage, LOAD_INITIALIZE);
			break;
		case Manger_Good_Attrs:// 品牌商品 =》采购=》获取产品的规格

			List<BLComment> dattbb = new ArrayList<BLComment>();
			try {
				if (!StrUtils.isEmpty(Data.getHttpResultStr()))
					dattbb = JSON.parseArray(Data.getHttpResultStr(),
							BLComment.class);
			} catch (Exception e) {
				onError("获取产品规格失败", 1);
				return;
			}
			if (dattbb.size() == 0) {
				onError("获取产品规格失败", 1);
				return;
			}
			// =========================================================需要处理处理>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			// PPurchase pShowVirtualLibGood1 = new PPurchase(BaseContext, 200,
			// PPurchase.TYPE_GOOD_GoodManger_XuKuDaoJia, dattbb, dattbb
			// .get(0).getSeller_id());

			// pShowVirtualLibGood1.showAtLocation(BaseView, Gravity.CENTER, 0,
			// 0);

			break;
		case Manger_Good_Alter:// 商品的编辑获取数据
			// 需要==============>解析进入商品编辑

			break;
		case GetGood_Detail:// 获取商品详情进入转发界面！！！！！！！！！！！！！！！！！！！！！！！！！！！！！

			BGoodDetail dattt = null;
			try {
				dattt = JSON.parseObject(Data.getHttpResultStr(),
						BGoodDetail.class);
			} catch (Exception e) {
				PromptManager.ShowCustomToast(BaseContext, "解析错误");
				return;

			}
			BLShow dComment = StrUtils.BDtoBL_BLShow(dattt);
			// BLComment dComment = StrUtils.BDtoBL_Comment(dattt);
			if (dComment.getIs_type().equals("0")) {// 照片
				PromptManager.SkipActivity(
						BaseActivity,
						new Intent(BaseActivity, AGoodShare.class).putExtra(
								AGoodShare.Key_FromShow, true).putExtra(
								AGoodShare.Key_Data, dComment));

			} else {// 视频
				PromptManager
						.SkipActivity(
								BaseActivity,
								new Intent(BaseActivity, AGoodVidoShare.class)
										.putExtra(
												AGoodVidoShare.Key_VidoFromShow,
												true).putExtra(
												AGoodVidoShare.Key_VidoData,
												dComment));

			}
			break;
		case 55:// 获取品牌的列表

			List<BLShopDaiLi> mBlShopDaiLis = new ArrayList<BLShopDaiLi>();

			if (StrUtils.isEmpty(Data.getHttpResultStr())) {
				return;
			} else {
				mBlShopDaiLis = JSON.parseArray(Data.getHttpResultStr(),
						BLShopDaiLi.class);
//				List<BLShopDaiLi> datss = new ArrayList<BLShopDaiLi>();
//				datss.add(new BLShopDaiLi("", "全部商品"));
//				datss.addAll(mBlShopDaiLis);

				brandAp.Refrsh(mBlShopDaiLis);
//				brandAp.IColor();
			}
			break;
		default:
			break;
		}

	}

	@Override
	protected void DataError(String error, int LoadType) {
		PromptManager.ShowCustomToast(BaseContext, error);
		switch (LoadType) {
		case LOAD_REFRESHING:
			lv_comment_listview.stopRefresh();
			break;
		case LOAD_LOADMOREING:
			lv_comment_listview.stopLoadMore();
			break;
		default:
			break;
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
		case R.id.tv_add_item:// ss
			BComment datas = new BComment("", "品牌");
			PromptManager.SkipActivity(
					BaseActivity,
					new Intent(BaseContext, ACommentList.class).putExtra(
							ACommentList.Tage_ResultKey,
							ACommentList.Tage_HomePopBrand).putExtra(
							ACommentList.Tage_BeanKey, datas));
			break;

		case R.id.fragment_isbrand_brand_txt:// 品牌
			if (!IsBrand) {
				fragment_isbrand_brand_txt.setBackground(getResources()
						.getDrawable(R.drawable.shape_right_pre));
				fragment_isbrand_ziying_txt.setBackground(getResources()
						.getDrawable(R.drawable.shape_left_nor));
				fragment_isbrand_brand_txt.setTextColor(getResources()
						.getColor(R.color.white));
				fragment_isbrand_ziying_txt.setTextColor(getResources()
						.getColor(R.color.app_fen));
				IsBrand = true;

				CurrentPage = 1;
				LoadData(CurrentPage, LOAD_INITIALIZE);
			}

			break;
		case R.id.fragment_isbrand_ziying_txt:// 自营
			if (IsBrand) {
				fragment_isbrand_brand_txt.setBackground(getResources()
						.getDrawable(R.drawable.shape_right_nor));
				fragment_isbrand_ziying_txt.setBackground(getResources()
						.getDrawable(R.drawable.shape_left_pre));
				fragment_isbrand_brand_txt.setTextColor(getResources()
						.getColor(R.color.app_fen));
				fragment_isbrand_ziying_txt.setTextColor(getResources()
						.getColor(R.color.white));
				IsBrand = false;
				CurrentPage = 1;
				LoadData(CurrentPage, LOAD_INITIALIZE);
			}
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

	class MenuAdapter extends HBaseAdapter {

		int MyScreenWidth = screenWidth;

		@Override
		public List<String> getMenuItems() {
			return Arrays.asList(TitleNames);
		}

		@Override
		public List<View> getContentViews() {
			List<View> views = new ArrayList<View>();
			for (String str : TitleNames) {
				View v = LayoutInflater.from(BaseContext).inflate(
						R.layout.content_view, null);
				TextView tv = (TextView) v.findViewById(R.id.tv_content);
				tv.setText(str);
				LinearLayout.LayoutParams ps = new LinearLayout.LayoutParams(
						120, DimensionPixelUtil.dip2px(BaseContext, 50));
				ps.setMargins(16, 10, 16, 10);
				v.setLayoutParams(ps);
				views.add(v);
			}
			return views;
		}

		@Override
		public void onPageChanged(int position, boolean visitStatus) {
			SetCurrentPostion(position);

		}
	}

	@Override
	public void onRefresh() {
		CurrentPage = 1;
		LoadData(CurrentPage, LOAD_REFRESHING);
		// IData(REFRESHING, Sale_Status, CurrentPage, 0, 0);
	}

	@Override
	public void onLoadMore() {
		CurrentPage = CurrentPage + 1;
		LoadData(CurrentPage, LOAD_LOADMOREING);
		// IData(LOADMOREING, Sale_Status, CurrentPage, 0, 0);
	}
}
