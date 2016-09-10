package io.vtown.WeiTangApp.ui.title.center.myshow;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.othershow.BLOtherShowIn;
import io.vtown.WeiTangApp.bean.bcomment.easy.othershow.BLOtherShowOut;
import io.vtown.WeiTangApp.bean.bcomment.easy.othershow.BOtherShow;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.comment.view.custom.PullView;
import io.vtown.WeiTangApp.comment.view.custom.PullView.OnFooterRefreshListener;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.ACommentList;
import io.vtown.WeiTangApp.ui.comment.AVidemplay;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;
import io.vtown.WeiTangApp.ui.title.ABrandDetail;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.ui.AShopDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-14 下午2:15:55
 * @category 点击首页Show=》头像=》进入的show列表
 * @category 点击头像时候需要跳转到想要的店铺 所以需要知道show头像来源处是品牌还是自营 商铺
 */
public class AOtherShow extends ATitleBase implements OnFooterRefreshListener {
	/**
	 * scrollview
	 */
	private PullView other_out_scrollview;
	/**
	 * 布局
	 */
	private RelativeLayout othershow_head_lay;
	/**
	 * 个人show的封面
	 */
	// private LinearLayout center_othershow_bg;
	private ScrollView other_show_scroll;
	/**
	 * 封面
	 */
	private ImageView center_othershow_bg_iv;
	/**
	 * 头像
	 */
	private CircleImageView center_othershow_head;

	private TextView center_othershow_head_myname;
	/**
	 * 名字
	 */
	// private TextView center_othershow_head_name;
	// /**
	// * 介绍
	// */
	// private TextView center_othershow_head_inf;
	/**
	 * 列表
	 */
	private CompleteListView center_othershow_ls;
	/**
	 * AP
	 */
	private OtherShowAp mOtherShowAp;
	// 无数据时候的view
	private View othershow_nodata_lay;

	// 保存用户简介的数据

	private String ShopIntro;
	private String LastId = "";

	/**
	 * 用户信息
	 */
	private BUser user_Get;

	// /**
	// * show列表传递数据bean时候的key
	// */
	// public static String Bean_Show_Tage = "beankey"; 基类里面的数据 baseBcBComment

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_othershow);
		user_Get = Spuit.User_Get(BaseContext);
		IBundlle();
		IBase();
		SetTitleHttpDataLisenter(this);
		IData(LastId, LOAD_INITIALIZE);
	}

	private void IBundlle() {
		if (getIntent().getExtras() == null
				&& !getIntent().getExtras().containsKey(BaseKey_Bean))
			// baseBcBComment = (BComment) getIntent().getSerializableExtra(
			// BaseKey_Bean);
			BaseActivity.finish();

	}

	private void IData(String Lastid, int LoadType) {
		if (LoadType == LOAD_INITIALIZE) {
			PromptManager.showtextLoading(BaseContext, getResources()
					.getString(R.string.loading));
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", baseBcBComment.getId());
		map.put("lastid", Lastid);
		map.put("pagesize", Constants.PageSize + "");
		FBGetHttpData(map, Constants.Center_My_Show_Data, Method.GET, 0,
				LoadType);
	}

	private void IBase() {
		other_show_scroll = (ScrollView) findViewById(R.id.other_show_scroll);

		center_othershow_head_myname = (TextView) findViewById(R.id.center_othershow_head_myname);

		othershow_nodata_lay = findViewById(R.id.othershow_nodata_lay);
		othershow_head_lay = (RelativeLayout) findViewById(R.id.othershow_head_lay);

		other_out_scrollview = (PullView) findViewById(R.id.other_out_scrollview);
		other_out_scrollview.setOnFooterRefreshListener(this);

		other_out_scrollview.HindFoot();

		center_othershow_bg_iv = (ImageView) findViewById(R.id.center_othershow_bg_iv);

		center_othershow_head = (CircleImageView) findViewById(R.id.center_othershow_head);

		center_othershow_head.setBorderWidth(10);
		center_othershow_head.setBorderColor(getResources().getColor(R.color.transparent6));

		ImageLoaderUtil.Load2(baseBcBComment.getCover(),
				center_othershow_bg_iv, R.drawable.error_iv1);
		ImageLoaderUtil.Load2(baseBcBComment.getAvatar(),
				center_othershow_head, R.drawable.error_iv2);

		center_othershow_ls = (CompleteListView) findViewById(R.id.center_othershow_ls);
		// 头像点击

		mOtherShowAp = new OtherShowAp(BaseContext,
				R.layout.item_center_show_outside);
		center_othershow_ls.setAdapter(mOtherShowAp);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				screenWidth, screenWidth / 2);// new lParams(screenWidth,
												// screenWidth/2);
		center_othershow_bg_iv.setLayoutParams(layoutParams);

		LinearLayout.LayoutParams pasLayoutParams = new LayoutParams(
				screenWidth / 4, screenWidth / 4);
		pasLayoutParams.setMargins(screenWidth * 11 / 16, -(screenWidth / 8),
				0, 0);
		center_othershow_head.setLayoutParams(pasLayoutParams);
		// center_othershow_head.setOnClickListener(this);
		center_othershow_head.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PromptManager.SkipActivity(
						BaseActivity,
						new Intent(BaseActivity, baseBcBComment.getIs_brand()
								.equals("1") ? ABrandDetail.class
								: AShopDetail.class).putExtra(
								ACommentList.Tage_ResultKey,
								ACommentList.Tage_HomePopBrand).putExtra(
								BaseKey_Bean,
								new BComment(baseBcBComment.getId(),
										baseBcBComment.getSeller_name())));
			}
		});
//		center_othershow_head.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				PromptManager.SkipActivity(
//						BaseActivity,
//						new Intent(BaseActivity, baseBcBComment.getIs_brand()
//								.equals("1") ? ABrandDetail.class
//								: AShopDetail.class).putExtra(
//								ACommentList.Tage_ResultKey,
//								ACommentList.Tage_HomePopBrand).putExtra(
//								BaseKey_Bean,
//								new BComment(baseBcBComment.getId(),
//										baseBcBComment.getSeller_name())));
//				return true;
//			}
//		});

	}

	@Override
	protected void InitTile() {
		SetTitleTxt(baseBcBComment.getSeller_name());
		// SetRightRightIv(R.drawable.ic_gengduo_nor);
		// right_right_iv.setOnClickListener(this);
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		if (StrUtils.isEmpty(Data.getHttpResultStr())) {
			DataError("暂无数据", Data.getHttpLoadType());
			return;
		}
		BOtherShow datas = new BOtherShow();// BComment
		// BDComment bd = new BDComment();
		try {
			datas = JSON.parseObject(Data.getHttpResultStr(), BOtherShow.class);
		} catch (Exception e) {
			DataError(Msg, Data.getHttpLoadType());
			return;
		}
		other_show_scroll.smoothScrollTo(0, 20);
		center_othershow_head.setVisibility(View.VISIBLE);
		othershow_head_lay.setVisibility(View.VISIBLE);
		// List<BLComment> listDatas = datas.getShowinfo();
		List<BLOtherShowOut> listDatas = datas.getShowinfo();
		if (null == listDatas || listDatas.size() == 0) {
			if (Data.getHttpLoadType() == LOAD_INITIALIZE) {// 初始化进来
				PromptManager.ShowCustomToast(BaseContext, "暂无Show");
				othershow_nodata_lay.setVisibility(View.VISIBLE);
			}
		} else {
			LastId = listDatas
					.get(listDatas.size() - 1)
					.getList()
					.get(listDatas.get(listDatas.size() - 1).getList().size() - 1)
					.getId();

		}

		if (listDatas.size() == Constants.PageSize) {
			other_out_scrollview.ShowFoot();
		} else {
			other_out_scrollview.HindFoot();
		}
		if (Data.getHttpLoadType() == LOAD_LOADMOREING) {
			LoadMoreComplet();
		}
		switch (Data.getHttpLoadType()) {
		case LOAD_INITIALIZE:
			mOtherShowAp.FrashData(listDatas);
			ShopIntro = datas.getSellerinfo().getIntro();
			//
			// StrUtils.SetTxt(center_othershow_head_name,
			// StrUtils.isEmpty(ShopIntro) ? "暂无简介" : ShopIntro);
			center_othershow_head_myname.setText(datas.getSellerinfo()
					.getSeller_name());
			break;
		case LOAD_REFRESHING:

			mOtherShowAp.FrashData(listDatas);

			break;
		case LOAD_LOADMOREING:
			if (listDatas != null && listDatas.size() > 0)
				mOtherShowAp.AddFrashData(listDatas);
			break;
		default:
			break;
		}
	}

	@Override
	protected void DataError(String error, int LoadType) {
		PromptManager.ShowCustomToast(BaseContext, error);
		if (LoadType == LOAD_INITIALIZE) {
			othershow_nodata_lay.setVisibility(View.VISIBLE);

		}
		if (LoadType == LOAD_LOADMOREING) {
			LoadMoreComplet();
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
		// case R.id.center_othershow_head_name://查看简介
		// PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
		// ABrandShopInf.class).putExtra(ABrandShopInf.Tage_Key,
		// ShopIntro));
		// break;
		case R.id.right_right_iv:// 更多
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					AMyShowDataSet.class).putExtra(AMyShowDataSet.Bean_Key,
					baseBcBComment));
			break;
		// case R.id.center_othershow_head:
		// PromptManager.ShowCustomToast(BaseContext, "ssssssssssd");
		// PromptManager.SkipActivity(
		// BaseActivity,
		// new Intent(BaseActivity, baseBcBComment.getIs_brand()
		// .equals("1") ? ABrandDetail.class
		// : AShopDetail.class).putExtra(
		// ACommentList.Tage_ResultKey,
		// ACommentList.Tage_HomePopBrand).putExtra(
		// BaseKey_Bean,
		// new BComment(baseBcBComment.getId(), baseBcBComment
		// .getSeller_name())));
		// break;
		default:
			break;
		}
	}

	/**
	 * 外层的
	 * 
	 * @author datutu
	 * 
	 */
	class OtherShowAp extends BaseAdapter {

		private Context context;
		private int ResourseId;
		private LayoutInflater inflater;

		/**
		 * 数据
		 */
		private List<BLOtherShowOut> datas = new ArrayList<BLOtherShowOut>();

		public OtherShowAp(Context context, int ResourseId) {
			super();
			this.context = context;
			this.ResourseId = ResourseId;
			this.inflater = LayoutInflater.from(context);

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
		public void FrashData(List<BLOtherShowOut> dass) {
			this.datas = dass;
			this.notifyDataSetChanged();
		}

		/**
		 * 加载更多
		 */
		public void AddFrashData(List<BLOtherShowOut> dass) {
			this.datas.addAll(dass);
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

			CenterShowOutsideItem centerShowOutside = null;
			if (arg1 == null) {
				centerShowOutside = new CenterShowOutsideItem();
				arg1 = inflater.inflate(ResourseId, null);
				centerShowOutside.tv_show_date = (TextView) arg1
						.findViewById(R.id.tv_show_date);
				centerShowOutside.lv_item_center_show = (CompleteListView) arg1
						.findViewById(R.id.lv_item_center_show);
				arg1.setTag(centerShowOutside);
			} else {
				centerShowOutside = (CenterShowOutsideItem) arg1.getTag();
			}
			int itemWidth = screenWidth
					- DimensionPixelUtil.dip2px(context, 20);

			StrUtils.SetTxt(centerShowOutside.tv_show_date, datas.get(arg0)
					.getDate());
			CenterShowInAp centerShowInAp = new CenterShowInAp(context,
					R.layout.item_center_show_inside,
					datas.get(arg0).getList(), itemWidth);
			centerShowOutside.lv_item_center_show.setAdapter(centerShowInAp);
			centerShowOutside.lv_item_center_show.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					BLOtherShowIn ddddd= (BLOtherShowIn) parent.getItemAtPosition(position);
//					PromptManager.SkipActivity(BaseActivity,new Intent(BaseActivity,));
					PromptManager.SkipActivity(BaseActivity, new Intent(
							BaseContext, AGoodDetail.class).putExtra("goodid",
							ddddd.getGoods_id()));
				}
			});
			// centerShowOutside.lv_item_center_show
			// .setOnItemClickListener(new OnItemClickListener() {
			// @Override
			// public void onItemClick(AdapterView<?> arg0, View arg1,
			// int arg2, long arg3) {
			// PromptManager.ShowCustomToast(BaseContext,
			// "查看show详情");
			// }
			//
			// });
			return arg1;

		}

	}

	/**
	 * 内层的AP
	 */
	class CenterShowInAp extends BaseAdapter {
		private int ApWidth;
		private Context context;
		private int ResourseId;
		private List<BLOtherShowIn> datas;
		private int itemWidth;

		private LayoutInflater inflater;

		public CenterShowInAp(Context context, int ResourseId,
				List<BLOtherShowIn> datas, int itemWidth) {
			super();
			this.context = context;
			this.ResourseId = ResourseId;
			this.datas = datas;
			this.itemWidth = itemWidth;
			this.inflater = LayoutInflater.from(context);
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

			CenterShowInsideItem centerShowInside = null;
			if (arg1 == null) {
				centerShowInside = new CenterShowInsideItem();
				arg1 = inflater.inflate(ResourseId, null);
				centerShowInside.gv_center_show_pic = (CompleteGridView) arg1
						.findViewById(R.id.gv_center_show_pic);
				centerShowInside.tv_center_show_desc = (TextView) arg1
						.findViewById(R.id.tv_center_show_desc);
				centerShowInside.gv_center_show_vido_play_iv = ViewHolder.get(
						arg1, R.id.gv_center_show_vido_play_iv);
				centerShowInside.gv_center_show_vido_lay = (RelativeLayout) arg1
						.findViewById(R.id.gv_center_show_vido_lay);

				centerShowInside.gv_center_show_vido_iv = ViewHolder.get(arg1,
						R.id.gv_center_show_vido_iv);
				arg1.setTag(centerShowInside);
			} else {
				centerShowInside = (CenterShowInsideItem) arg1.getTag();
			}

			// 进行设置View布局
			List<String> imgarr = datas.get(arg0).getImgarr();
			// 屏幕宽度的2/5
			int gridWidth = (int) (itemWidth * 0.4);
			if ("0".equals(datas.get(arg0).getIs_type())) {// 0代表图片；；；1代表视频
				// 图片
				centerShowInside.gv_center_show_vido_lay
						.setVisibility(View.GONE);
				centerShowInside.gv_center_show_pic.setVisibility(View.VISIBLE);
				// 图片文件开始整理图片
				if (StrUtils.isEmpty(datas.get(arg0).getIntro())
						&& imgarr.size() == 0) {
					// arg1.setVisibility(View.GONE);
				} else {
					// arg1.setVisibility(View.VISIBLE);

					// 设置GridView的宽高
					LayoutParams layoutParams = new LinearLayout.LayoutParams(
							gridWidth, gridWidth);
					centerShowInside.gv_center_show_pic
							.setLayoutParams(layoutParams);
					// 通过有多少张图片来判断显示

					if (imgarr.size() < 2) {
						centerShowInside.gv_center_show_pic
								.setColumnWidth(gridWidth);
						centerShowInside.gv_center_show_pic.setNumColumns(1);
					}
					if (imgarr.size() > 1 && imgarr.size() < 5) {
						centerShowInside.gv_center_show_pic
								.setColumnWidth(gridWidth / 2);
						centerShowInside.gv_center_show_pic.setNumColumns(2);
					}
					if (imgarr.size() > 4) {
						centerShowInside.gv_center_show_pic
								.setColumnWidth(gridWidth / 3);
						centerShowInside.gv_center_show_pic.setNumColumns(3);
					}
					centerShowInside.gv_center_show_pic
							.setAdapter(new PicAdapter(context,
									R.layout.item_center_show_gridview, imgarr,
									gridWidth));

				}

			} else {
				// 视频文件
				centerShowInside.gv_center_show_vido_lay
						.setVisibility(View.VISIBLE);
				centerShowInside.gv_center_show_pic.setVisibility(View.GONE);
				// 视频播放第一帧大小
				LayoutParams layoutParams = new LinearLayout.LayoutParams(
						gridWidth, gridWidth);
				centerShowInside.gv_center_show_vido_lay
						.setLayoutParams(layoutParams);
				ImageLoaderUtil.Load2(datas.get(arg0).getPre_url(),
						centerShowInside.gv_center_show_vido_iv,
						R.drawable.error_iv1);
				final BLOtherShowIn da = datas.get(arg0);

				centerShowInside.gv_center_show_vido_play_iv
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View arg0) {
								PromptManager
										.SkipActivity(BaseActivity, new Intent(
												BaseActivity, AVidemplay.class)
												.putExtra(AVidemplay.VidoKey,
														da.getVid()));
							}
						});

			}
			StrUtils.SetTxt(centerShowInside.tv_center_show_desc,
					datas.get(arg0).getIntro());
			// 设置AP

			return arg1;
		}
	}

	class PicAdapter extends BaseAdapter {
		private Context context;
		private int ResourseId;
		private List<String> datas;
		private LayoutInflater inflater;
		private int imageWidth;

		public PicAdapter(Context context, int ResourseId, List<String> datas,
				int gridWidth) {
			super();
			this.context = context;
			this.ResourseId = ResourseId;
			this.datas = datas;
			this.inflater = LayoutInflater.from(context);
			this.imageWidth = gridWidth;
		}

		@Override
		public int getCount() {

			return datas.size();
		}

		@Override
		public String getItem(int arg0) {

			return datas.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {

			return arg0;
		}

		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {

			PicItem pic = null;
			if (arg1 == null) {
				pic = new PicItem();
				arg1 = inflater.inflate(ResourseId, null);
				pic.iv_center_show_pic = ViewHolder.get(arg1,
						R.id.iv_center_show_pic);

				arg1.setTag(pic);
				if (datas.size() < 2 && datas.size() > 0) {
					LayoutParams layoutParams = new LinearLayout.LayoutParams(
							imageWidth, imageWidth);
					pic.iv_center_show_pic.setLayoutParams(layoutParams);

				}
				if (datas.size() > 1 && datas.size() < 5) {
					LayoutParams layoutParams = new LinearLayout.LayoutParams(
							imageWidth / 2, imageWidth / 2);
					pic.iv_center_show_pic.setLayoutParams(layoutParams);
				}
				if (datas.size() >= 5) {
					LayoutParams layoutParams = new LinearLayout.LayoutParams(
							imageWidth / 3, imageWidth / 3);
					pic.iv_center_show_pic.setLayoutParams(layoutParams);
				}

				ImageLoaderUtil.Load2(datas.get(arg0), pic.iv_center_show_pic,
						R.drawable.error_iv2);
				pic.iv_center_show_pic.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent mIntent = new Intent(BaseContext,
								AphotoPager.class);
						mIntent.putExtra("position", arg0);
						mIntent.putExtra("urls", ToArray(datas));
						PromptManager.SkipActivity(BaseActivity,
								mIntent);
					}
				});


//				pic.iv_center_show_pic
//						.setOnTouchListener(new OnTouchListener() {
//
//							@Override
//							public boolean onTouch(View v, MotionEvent event) {
//								Intent mIntent = new Intent(BaseContext,
//										AphotoPager.class);
//								mIntent.putExtra("position", arg0);
//								mIntent.putExtra("urls", ToArray(datas));
//								PromptManager.SkipActivity(BaseActivity,
//										mIntent);
//								return true;
//							}
//						});

				// pic.iv_center_show_pic
				// .setOnClickListener(new OnClickListener() {
				// @Override
				// public void onClick(View arg6) {
				// Intent mIntent = new Intent(BaseContext,
				// AphotoPager.class);
				// mIntent.putExtra("position", arg0);
				// mIntent.putExtra("urls", ToArray(datas));
				// PromptManager.SkipActivity(BaseActivity,
				// mIntent);
				// }
				// });

			} else {
				pic = (PicItem) arg1.getTag();
			}

			return arg1;
		}
	}

	/**
	 * @author Yihuihua GridView中的图片
	 */
	class PicItem {
		ImageView iv_center_show_pic;
	}

	/**
	 * @author Yihuihua 外面的item
	 */
	class CenterShowOutsideItem {
		TextView tv_show_date;
		CompleteListView lv_item_center_show;
	}

	/**
	 * @author Yihuihua 里面的item
	 */
	class CenterShowInsideItem {
		RelativeLayout gv_center_show_vido_lay;// 视频播放的布局文件
		ImageView gv_center_show_vido_iv;// 第一帧的北京图片
		ImageView gv_center_show_vido_play_iv;// 视频播放的开始
		CompleteGridView gv_center_show_pic;// 图片的九宫格
		TextView tv_center_show_desc;
	}

	private String[] ToArray(List<String> da) {
		String array[] = new String[da.size()];
		for (int i = 0; i < da.size(); i++) {
			array[i] = da.get(i);
		}
		return array;
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	@Override
	public void onFooterRefresh(PullView view) {
		other_out_scrollview.ShowFoot();
		IData(LastId, LOAD_LOADMOREING);
	}

	/**
	 * 获取更多的数据成功后取消加载更多的更多
	 */
	private void LoadMoreComplet() {
		Message m = new Message();
		m.what = 101;
		mHandler.sendMessage(m);
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 101) {
				other_out_scrollview.onFooterRefreshComplete();
			}

		}
	};

}
