package io.vtown.WeiTangApp.ui.title.center.myshow;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcache.BShop;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.othershow.BLOtherShowIn;
import io.vtown.WeiTangApp.bean.bcomment.easy.othershow.BLOtherShowOut;
import io.vtown.WeiTangApp.bean.bcomment.easy.othershow.BOtherShow;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
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
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.onConfirmClick;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.oncancelClick;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.AVidemplay;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;

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
import android.widget.AdapterView.OnItemLongClickListener;
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
 * @version 创建时间：2016-5-19 下午2:19:28
 * 
 */
public class ACenterShow extends ATitleBase implements OnFooterRefreshListener {
	/**
	 * 外层
	 */
	private PullView center_out_scrollview;
	/**
	 * head
	 */
	private RelativeLayout myshow_head_lay;
	/**
	 * 个人show的封面
	 */
	// private LinearLayout center_show_bg;
	//
	private ImageView center_show_bg_iv;
	private TextView center_show_head_myname;
	/**
	 * 头像
	 */
	private CircleImageView center_show_head;
	/**
	 * 名字
	 */
	// private TextView center_show_head_name;
	// /**
	// * 介绍
	// */
	// private TextView center_show_head_inf;
	/**
	 * 列表
	 */
	private CompleteListView center_show_ls;
	/**
	 * AP
	 */
	private CenterShowAp mCenterShowAp;

	// scrollview
	private ScrollView myshowscrollview;
	// 没数据时候
	private View myshow_nodata_lay;
	private String LastId = "";

	/**
	 * 别人的seller_id
	 */
	private String OtherSellerId = "";

	/**
	 * 用户信息
	 */
	private BUser user_Get;
	// 需要他的封面和头像
	private BShop bShop;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_center_show);
		user_Get = Spuit.User_Get(BaseContext);
		bShop = Spuit.Shop_Get(BaseContext);
		IBUndle();
		IBase();
		ICache();
		SetTitleHttpDataLisenter(this);
		LastId = "";
		IData(LastId, LOAD_INITIALIZE);
	}

	private void ICache() {
		String CacheStr = CacheUtil.MyShow_Get(BaseContext);
		if (StrUtils.isEmpty(CacheStr))
			return;
		// 开始显示缓存数据

		BOtherShow datas = new BOtherShow();
		try {
			datas = JSON.parseObject(CacheStr, BOtherShow.class);

		} catch (Exception e) {

			return;
		}
		center_show_head.setVisibility(View.VISIBLE);
		myshow_head_lay.setVisibility(View.VISIBLE);
		List<BLOtherShowOut> listDatas = datas.getShowinfo();
		if (listDatas.size() > 0)
			LastId = listDatas
					.get(listDatas.size() - 1)
					.getList()
					.get(listDatas.get(listDatas.size() - 1).getList().size() - 1)
					.getId();
		if (listDatas.size() == Constants.PageSize) {
			center_out_scrollview.ShowFoot();
		} else {
			center_out_scrollview.HindFoot();
		}
		myshowscrollview.smoothScrollTo(0, 20);
		mCenterShowAp.FrashData(listDatas, datas);
	}

	private void IBUndle() {

	}

	private void IData(String Lastid, int LoadType) {
		if (LoadType == LOAD_INITIALIZE) {
			PromptManager.showtextLoading(BaseContext, getResources()
					.getString(R.string.loading));
		}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", user_Get.getSeller_id());
		map.put("lastid", Lastid);
		map.put("pagesize", Constants.PageSize + "");
		FBGetHttpData(map, Constants.Center_My_Show_Data, Method.GET, 0,
				LoadType);
	}

	private void IBase() {
		myshow_nodata_lay = findViewById(R.id.myshow_nodata_lay);
		myshowscrollview = (ScrollView) findViewById(R.id.myshowscrollview);
		myshow_head_lay = (RelativeLayout) findViewById(R.id.myshow_head_lay);

		center_out_scrollview = (PullView) findViewById(R.id.center_out_scrollview);
		center_out_scrollview.setOnFooterRefreshListener(this);
		center_out_scrollview.HindFoot();

		center_show_head_myname = (TextView) findViewById(R.id.center_show_head_myname);
		center_show_head = (CircleImageView) findViewById(R.id.center_show_head);
		// center_show_head_name = (TextView)
		// findViewById(R.id.center_show_head_name);
		// center_show_head_inf = (TextView)
		// findViewById(R.id.center_show_head_inf);
		center_show_bg_iv = (ImageView) findViewById(R.id.center_show_bg_iv);
		// center_show_head_name.setText(bShop.getIntro());

		// StrUtils.SetTxt(center_show_head_name, bShop.getIntro());
		ImageLoaderUtil.Load2(bShop.getCover(), center_show_bg_iv,
				R.drawable.error_iv2);
		ImageLoaderUtil.Load2(bShop.getAvatar(), center_show_head,
				R.drawable.error_iv1);
		center_show_ls = (CompleteListView) findViewById(R.id.center_show_ls);

		mCenterShowAp = new CenterShowAp(BaseContext,
				R.layout.item_center_show_outside);
		center_show_ls.setAdapter(mCenterShowAp);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				screenWidth, screenWidth / 2);// new lParams(screenWidth,
												// screenWidth/2);
		center_show_bg_iv.setLayoutParams(layoutParams);

		// 设置头像
		LinearLayout.LayoutParams pasLayoutParams = new LayoutParams(
				screenWidth / 4, screenWidth / 4);
		pasLayoutParams.setMargins(screenWidth * 11 / 16, -(screenWidth / 8),
				0, 0);
		center_show_head.setLayoutParams(pasLayoutParams);
		StrUtils.SetTxt(center_show_head_myname, bShop.getSeller_name());
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.center_show));
		// SetRightRightIv(R.drawable.ic_jiahao_add);
		// right_right_iv.setOnClickListener(this);

	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {

		if (91 == Data.getHttpResultTage()) {// 删除
			PromptManager.ShowCustomToast(BaseContext, "删除成功");
			LastId = "";
			IData(LastId, LOAD_REFRESHING);
			return;
		}
		if (StrUtils.isEmpty(Data.getHttpResultStr())) {
			DataError(Msg, Data.getHttpLoadType());
			return;
		}

		// BDComment bd = new BDComment();
		// List<BLComment> listDatas = new ArrayList<BLComment>();
		BOtherShow datas = new BOtherShow();
		try {
			datas = JSON.parseObject(Data.getHttpResultStr(), BOtherShow.class);
			// listDatas = JSON.parseArray(Data.getHttpResultStr(),
			// BLComment.class);
		} catch (Exception e) {
			DataError(Msg, Data.getHttpLoadType());
			return;
		}
		center_show_head.setVisibility(View.VISIBLE);
		myshow_head_lay.setVisibility(View.VISIBLE);
		List<BLOtherShowOut> listDatas = datas.getShowinfo();
		if (listDatas.size() > 0) {
			LastId = listDatas
					.get(listDatas.size() - 1)
					.getList()
					.get(listDatas.get(listDatas.size() - 1).getList().size() - 1)
					.getId();
		} else {
			if (Data.getHttpLoadType() == LOAD_INITIALIZE) {// 初始化进来无数据时
				myshow_nodata_lay.setVisibility(View.VISIBLE);
				CacheUtil.MyShow_Save(BaseContext, Data.getHttpResultStr());
			}
		}

		if (listDatas.size() == Constants.PageSize) {
			center_out_scrollview.ShowFoot();
		} else {
			center_out_scrollview.HindFoot();
		}
		if (Data.getHttpLoadType() == LOAD_LOADMOREING) {
			LoadMoreComplet();
		}
		switch (Data.getHttpLoadType()) {
		case LOAD_INITIALIZE:
			CacheUtil.MyShow_Save(BaseContext, Data.getHttpResultStr());

			myshowscrollview.smoothScrollTo(0, 20);

			mCenterShowAp.FrashData(listDatas, datas);
			break;
		case LOAD_REFRESHING:

			mCenterShowAp.FrashData(listDatas, datas);

			break;
		case LOAD_LOADMOREING:
			if (listDatas != null && listDatas.size() > 0)
				mCenterShowAp.AddFrashData(listDatas);
			break;
		default:
			break;
		}
	}

	@Override
	protected void DataError(String error, int LoadTyp) {
		PromptManager.ShowCustomToast(BaseContext, error);

		switch (LoadTyp) {
		case LOAD_INITIALIZE:

			break;
		case LOAD_REFRESHING:

			break;
		case LOAD_LOADMOREING:

			LoadMoreComplet();
			break;
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
	class CenterShowAp extends BaseAdapter {

		private Context context;
		private int ResourseId;
		private LayoutInflater inflater;

		/**
		 * 数据
		 */
		private List<BLOtherShowOut> datas = new ArrayList<BLOtherShowOut>();

		public CenterShowAp(Context context, int ResourseId) {
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
		public void FrashData(List<BLOtherShowOut> dass, BOtherShow bComment) {
			this.datas = dass;
			this.notifyDataSetChanged();
			//
			ImageLoaderUtil.Load2(bComment.getSellerinfo().getCover(),
					center_show_bg_iv, R.drawable.error_iv1);
			ImageLoaderUtil.Load2(bComment.getSellerinfo().getAvatar(),
					center_show_head, R.drawable.error_iv2);
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
			final BLOtherShowOut blComment = datas.get(arg0);
			int itemWidth = screenWidth
					- DimensionPixelUtil.dip2px(context, 20);

			StrUtils.SetTxt(centerShowOutside.tv_show_date, datas.get(arg0)
					.getDate());
			CenterShowInAp centerShowInAp = new CenterShowInAp(context,
					R.layout.item_center_show_inside,
					datas.get(arg0).getList(), itemWidth);
			centerShowOutside.lv_item_center_show.setAdapter(centerShowInAp);
			centerShowOutside.lv_item_center_show
					.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							ShowCustomDialog(blComment.getList().get(arg2)
									.getId());
							return true;
						}
					});
			return arg1;

		}
	}

	/**
	 * 
	 * 删除show
	 * 
	 * @param type
	 * @param title1
	 * @param title2
	 * @param blComment
	 */
	private void ShowCustomDialog(final String ShowId) {
		final CustomDialog dialog = new CustomDialog(BaseContext,
				R.style.mystyle, R.layout.dialog_purchase_cancel, 1, "取消", "删除");
		dialog.show();
		dialog.setTitleText("是否删除该show?");
		dialog.HindTitle2();
		dialog.setCanceledOnTouchOutside(true);
		dialog.setcancelListener(new oncancelClick() {

			@Override
			public void oncancelClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.setConfirmListener(new onConfirmClick() {
			@Override
			public void onConfirmCLick(View v) {
				dialog.dismiss();
				DeletMyShow(ShowId);
			}
		});
	}

	/**
	 * 删除我自己的show
	 */

	private void DeletMyShow(String ShowId) {
		HashMap<String, String> mHashMap = new HashMap<String, String>();

		mHashMap.put("id", ShowId);
		mHashMap.put("seller_id", user_Get.getSeller_id());

		FBGetHttpData(mHashMap, Constants.MyShowDelete, Method.DELETE, 91,
				LOAD_INITIALIZE);

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

				ImageLoaderUtil.Load(datas.get(arg0), pic.iv_center_show_pic,
						R.drawable.error_iv2);

				pic.iv_center_show_pic
						.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								Intent mIntent = new Intent(BaseContext,
										AphotoPager.class);
								mIntent.putExtra("position", arg0);
								mIntent.putExtra("urls", ToArray(datas));
								PromptManager.SkipActivity(BaseActivity,
										mIntent);
								return true;
							}
						});
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
		// case R.id.right_right_iv:
		//
		// PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
		// AMyShowDataSet.class));
		//
		// break;

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

	@Override
	public void onFooterRefresh(PullView view) {
		center_out_scrollview.ShowFoot();
		IData(LastId, LOAD_LOADMOREING);
	}

	// @Override
	// public void onRefresh() {
	// LastId = "";
	// IData(LastId, LOAD_REFRESHING);
	// }
	//
	// @Override
	// public void onLoadMore() {
	// IData(LastId, LOAD_LOADMOREING);
	// }
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
				center_out_scrollview.onFooterRefreshComplete();
			}

		}
	};

}
