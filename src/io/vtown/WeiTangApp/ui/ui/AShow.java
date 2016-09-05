package io.vtown.WeiTangApp.ui.ui;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BLShow;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.NHttpBaseStr;
import io.vtown.WeiTangApp.comment.util.DateUtils;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.ShowSelectPic;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.onConfirmClick;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.oncancelClick;
import io.vtown.WeiTangApp.comment.view.listview.LListView;
import io.vtown.WeiTangApp.comment.view.listview.LListView.IXListViewListener;
import io.vtown.WeiTangApp.comment.view.pop.PShowShangJia;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.event.interf.IHttpResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.AGoodShare;
import io.vtown.WeiTangApp.ui.comment.AGoodVidoShare;
import io.vtown.WeiTangApp.ui.comment.AVidemplay;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.center.myshow.AOtherShow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Cache;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-12 下午4:15:07
 * @author 一级页面 Show
 */
public class AShow extends ATitleBase implements IXListViewListener {
	/**
	 * ls
	 */
	private LListView ShowLs;
	private TextView activity_show_uptxt;
	/**
	 * AP
	 */
	private ShowAp showAp;
	/**
	 * baseView
	 */
	private View BaseView;
	/**
	 * 当前的最后item的lastid
	 */
	private String LastId = "";

	/**
	 * 未获取数据
	 */
	private View show_nodata_lay;

	/**
	 * 用户信息
	 */
	private BUser user_Get;
	/**
	 * 是否存在缓存
	 */
	private boolean IsCache;

	/**
	 * 当前的listview可见的第一个位置
	 */
	private int startPos = 0;
	/**
	 * 当前listview可见的最后一个位置
	 */
	private int endPos;

	private boolean needToTop;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_show);
		BaseView = LayoutInflater.from(BaseContext).inflate(
				R.layout.activity_show, null);
		user_Get = Spuit.User_Get(BaseContext);
		SetTitleHttpDataLisenter(this);
		// EventBus.getDefault().register(this, "GetMessage", BMessage.class);
		IBase();
		ICacheLs();

		IData(LastId, LOAD_INITIALIZE);

	}

	private void ICacheLs() {
		// 缓存中有show的数据进行处理
		String CachData = Spuit.Show_GetStr(BaseContext);
		if (!StrUtils.isEmpty(CachData)) {
			List<BLShow> data = new ArrayList<BLShow>();
			// 开始解析*************************

			try {
				data = JSON.parseArray(CachData, BLShow.class);// ();
			} catch (Exception e) {
				IDataView(ShowLs, show_nodata_lay, NOVIEW_INITIALIZE);
				return;
			}

			showAp.FrashData(data);
			IsCache = true;
		} else {// 没有数据就直接显示空白
			IsCache = false;
			// IDataView(ShowLs, show_nodata_lay, NOVIEW_INITIALIZE);
		}
	}

	private void IData(String LastId, int LoadType) {
		if (LoadType == LOAD_INITIALIZE && !IsCache)
			PromptManager.showtextLoading(BaseContext, getResources()
					.getString(R.string.loading));
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", user_Get.getSeller_id());
		map.put("lastid", LastId);
		// map.put("pagesize", "10");s
		FBGetHttpData(map, Constants.Show_ls, Method.GET, 0, LoadType);
	}

	private void IBase() {
		activity_show_uptxt = (TextView) findViewById(R.id.activity_show_uptxt);
		activity_show_uptxt.setOnClickListener(this);
		show_nodata_lay = findViewById(R.id.show_nodata_lay);

		ShowLs = (LListView) findViewById(R.id.activity_show_ls);
		ShowLs.setPullLoadEnable(true);
		ShowLs.setPullRefreshEnable(true);
		ShowLs.setXListViewListener(this);
		ShowLs.hidefoot();
		// ls滑动时候不要加载图片
		ShowLs.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

				if (firstVisibleItem != 0) {
					needToTop = true;
					activity_show_uptxt.setVisibility(View.VISIBLE);
				} else if (firstVisibleItem == 0) {
					needToTop = false;
					activity_show_uptxt.setVisibility(View.GONE);
				}

			}
		});

		showAp = new ShowAp(R.layout.item_show);
		ShowLs.setAdapter(showAp);

		show_nodata_lay.setOnClickListener(this);

	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.tab_show));
		findViewById(R.id.lback).setVisibility(View.GONE);
	}

	public void GetMessage(BMessage mBMessage) {
		if (mBMessage.getMessageType() == BMessage.Tage_Main_To_ShowData) {// 第一次进来
																			// 后台通知
																			// 进行偷偷加载数据
			IData(LastId, LOAD_INITIALIZE);
		}
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		PromptManager.closeTextLoading3();
		switch (Data.getHttpResultTage()) {
		case 0:// 获取商品的列表
			if (StrUtils.isEmpty(Data.getHttpResultStr())) {
				DataError(Constants.SucessToError, Data.getHttpLoadType());
				if (LOAD_INITIALIZE == Data.getHttpLoadType()) {
					Spuit.Show_SaveStr(BaseContext, Data.getHttpResultStr());
					showAp.FrashData(new ArrayList<BLShow>());
				}

				return;
			}

			List<BLShow> data = new ArrayList<BLShow>();
			try {
				data = JSON.parseArray(Data.getHttpResultStr(), BLShow.class);// ();
			} catch (Exception e) {
				DataError("解析错误", 1);
				return;
			}
			IDataView(ShowLs, show_nodata_lay, NOVIEW_RIGHT);
			if (data.size() < 10)
				ShowLs.hidefoot();
			else
				ShowLs.ShowFoot();
			switch (Data.getHttpLoadType()) {
			case LOAD_INITIALIZE:
				showAp.FrashData(data);
				Spuit.Show_SaveStr(BaseContext, Data.getHttpResultStr());
				break;
			case LOAD_REFRESHING:
				showAp.FrashData(data);
				IsStopFresh(LOAD_REFRESHING);

				break;
			case LOAD_LOADMOREING:
				showAp.AddFrashData(data);
				IsStopFresh(LOAD_LOADMOREING);
				break;
			default:
				break;
			}
			LastId = data.get(data.size() - 1).getId();

			break;
		case 11:// 删除我的show
			PromptManager.ShowCustomToast(BaseContext, "删除成功");

			showAp.DeletPostion(Data.getHttpLoadType() - 11);
			break;

		default:
			break;
		}

	}

	@Override
	protected void DataError(String error, int LoadTyp) {
		PromptManager.closeTextLoading3();
		if (LoadTyp == LOAD_REFRESHING) {
			ShowLs.stopRefresh();
		}
		if (LoadTyp == LOAD_LOADMOREING) {
			ShowLs.stopLoadMore();
		}
		if (LoadTyp == LOAD_INITIALIZE) {
			if (showAp.getCount() == 0)
				IDataView(ShowLs, show_nodata_lay, NOVIEW_ERROR);
			// PromptManager.ShowCustomToast(BaseContext, getResources()
			// .getString(R.string.zanwushow));
			return;
		}

		PromptManager.ShowCustomToast(BaseContext, error);
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
		case R.id.show_nodata_lay:
			IData(LastId, LOAD_INITIALIZE);
			break;
		case R.id.activity_show_uptxt:
			ShowLs.smoothScrollToPosition(0);
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
	 * Show的adapter
	 * 
	 * @author datutu
	 */
	private class ShowAp extends BaseAdapter {

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
		private List<BLShow> datas = new ArrayList<BLShow>();

		public ShowAp(int resourceId) {
			super();

			this.inflater = LayoutInflater.from(BaseContext);
			ResourceId = resourceId;
		}

		public void DeletPostion(int Postioo) {
			this.datas.remove(Postioo);
			this.notifyDataSetChanged();
		}

		public void FrashData(List<BLShow> dass) {

			this.datas = dass;
			// this.datas.addAll(dass);
			this.notifyDataSetChanged();
		}

		public void AddFrashData(List<BLShow> dasss) {
			datas.addAll(dasss);
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
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			ShowItem myItem = null;
			if (convertView == null) {
				myItem = new ShowItem();
				convertView = inflater.inflate(ResourceId, null);
				// 公用
				myItem.item_show_iv = (CircleImageView) convertView
						.findViewById(R.id.item_show_iv);

				myItem.item_show_gooddetail = ViewHolder.get(convertView,
						R.id.item_show_gooddetail);
				myItem.item_show_name = ViewHolder.get(convertView,
						R.id.item_show_name);
				myItem.item_show_txt_inf = ViewHolder.get(convertView,
						R.id.item_show_txt_inf);
				myItem.item_show_time = ViewHolder.get(convertView,
						R.id.item_show_time);
				myItem.item_show_share_iv = ViewHolder.get(convertView,
						R.id.item_show_share_iv);
				myItem.item_show_share_number = ViewHolder.get(convertView,
						R.id.item_show_share_number);
				myItem.item_show_gooddetail_iv = ViewHolder.get(convertView,
						R.id.item_show_gooddetail_iv);

				// 视频播放
				myItem.item_show_vido_lay = (RelativeLayout) convertView
						.findViewById(R.id.item_show_vido_lay);
				myItem.item_show_vido_image = ViewHolder.get(convertView,
						R.id.item_show_vido_image);
				myItem.item_show_vido_control_image = ViewHolder.get(
						convertView, R.id.item_show_vido_control_image);
				myItem.item_show_delete_txt = ViewHolder.get(convertView,
						R.id.item_show_delete_txt);
				myItem.item_show_gooddetail_lay = (LinearLayout) convertView
						.findViewById(R.id.item_show_gooddetail_lay);
				// 一张图片
				myItem.item_show_oneiv = ViewHolder.get(convertView,
						R.id.item_show_oneiv);
				// 多张图片
				myItem.item_show_gridview = (CompleteGridView) convertView
						.findViewById(R.id.item_show_gridview);

				// IvSet(datas.get(arg0), myItem);

				convertView.setTag(myItem);
			} else {
				myItem = (ShowItem) convertView.getTag();
			}
			ImageLoaderUtil.Load(datas.get(arg0).getSellerinfo().getAvatar(),
					myItem.item_show_iv, R.drawable.testiv);
			IvSet(datas.get(arg0), myItem);
			final BLShow ItemData = datas.get(arg0);
			final int MyPostion = arg0;
			// 是我发的show就需要显示删除
			myItem.item_show_delete_txt
					.setVisibility(user_Get.getSeller_id().equals(
							ItemData.getSeller_id()) ? View.VISIBLE : View.GONE);

			// IvSet(ItemData, myItem);
			// IViewData(myItem, datas.get(arg0), BaseView, arg0);

			myItem.item_show_share_iv.setOnClickListener(new ShareClick(
					ItemData, myItem, BaseView));
			StrUtils.SetTxt(myItem.item_show_share_number,
					ItemData.getSendnumber() + "人转发");
			StrUtils.SetTxt(myItem.item_show_name, ItemData.getSellerinfo()
					.getSeller_name());
			StrUtils.SetTxt(myItem.item_show_txt_inf, ItemData.getIntro());
			StrUtils.SetTxt(myItem.item_show_time, DateUtils
					.convertTimeToFormat(Long.parseLong(ItemData
							.getCreate_time())));
			myItem.item_show_delete_txt
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if (CheckNet(BaseContext))
								return;
							SelectToDo(1, ItemData.getId(), MyPostion);

						}
					});
			myItem.item_show_gooddetail_lay
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// if (CheckNet(BaseContext))
							// return;
							// PromptManager.SkipActivity(BaseActivity,
							// new Intent(BaseActivity, AGoodDetail.class)
							// .putExtra("goodid",
							// ItemData.getGoods_id()));

							if (CheckNet(BaseContext))
								return;
							Intent intent = new Intent(BaseActivity,
									AOtherShow.class);
							intent.putExtra(
									BaseKey_Bean,
									new BComment(
											ItemData.getSeller_id(),
											ItemData.getSellerinfo().getCover(),
											ItemData.getSellerinfo()
													.getAvatar(), ItemData
													.getSellerinfo()
													.getSeller_name(), ItemData
													.getSellerinfo()
													.getIs_brand()));
							PromptManager.SkipActivity(BaseActivity, intent);
						}
					});
			myItem.item_show_gooddetail_iv
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (CheckNet(BaseContext))
								return;
							PromptManager.SkipActivity(BaseActivity,
									new Intent(BaseActivity, AGoodDetail.class)
											.putExtra("goodid",
													ItemData.getGoods_id()));
						}
					});
			myItem.item_show_iv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					if (CheckNet(BaseContext))
						return;
					Intent intent = new Intent(BaseActivity, AOtherShow.class);
					intent.putExtra(BaseKey_Bean,
							new BComment(ItemData.getSeller_id(), ItemData
									.getSellerinfo().getCover(), ItemData
									.getSellerinfo().getAvatar(), ItemData
									.getSellerinfo().getSeller_name(), ItemData
									.getSellerinfo().getIs_brand()));
					PromptManager.SkipActivity(BaseActivity, intent);

				}
			});

			return convertView;
		}

		class ShowItem {
			// 基本信息
			CircleImageView item_show_iv;
			LinearLayout item_show_gooddetail_lay;// 查看show
			TextView item_show_name;// 名字
			TextView item_show_gooddetail;// 查看详情//废弃
			ImageView item_show_gooddetail_iv;// 查看详情
			TextView item_show_txt_inf;// 信息
			TextView item_show_time;// 时间
			TextView item_show_delete_txt;// 删除
			ImageView item_show_share_iv;// 分享
			TextView item_show_share_number;// 转发数量
			// 视频
			RelativeLayout item_show_vido_lay;// 视频播放的父布局
			ImageView item_show_vido_image;// vido第一帧的图片
			ImageView item_show_vido_control_image;// 控制播放的view
			// 一张图片
			ImageView item_show_oneiv;
			// 多张图片时候需要的九宫格
			CompleteGridView item_show_gridview;
		}

		private void IvSet(BLShow blComment, ShowItem myItem) {
			boolean IsPic = blComment.getIs_type().equals("0");
			// 下边是图片和视频
			if (IsPic) {// 图片显示gradview
				final List<String> Urls = blComment.getImgarr();
				if (Urls == null || Urls.size() == 0)
					return;
				if (1 == Urls.size()) {// 一张图片
					myItem.item_show_gridview.setVisibility(View.GONE);
					myItem.item_show_vido_lay.setVisibility(View.GONE);
					myItem.item_show_oneiv.setVisibility(View.VISIBLE);
					try {
						ImageLoaderUtil.Load(Urls.get(0),
								myItem.item_show_oneiv, R.drawable.error_iv1);
					} catch (Exception e) {
					}
					myItem.item_show_oneiv
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									Intent mIntent = new Intent(BaseContext,
											AphotoPager.class);
									mIntent.putExtra("position", 0);
									mIntent.putExtra("urls",
											StrUtils.LsToArray(Urls));
									PromptManager.SkipActivity(BaseActivity,
											mIntent);
								}
							});
				}
				if (Urls.size() > 1) {// 两张图片
					myItem.item_show_gridview.setVisibility(View.VISIBLE);
					myItem.item_show_vido_lay.setVisibility(View.GONE);
					myItem.item_show_oneiv.setVisibility(View.GONE);
					// 赋数据
					myItem.item_show_gridview.setAdapter(new Ivdapter(Urls));
					myItem.item_show_gridview
							.setOnItemClickListener(new OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									Intent mIntent = new Intent(BaseContext,
											AphotoPager.class);
									mIntent.putExtra("position", arg2);
									mIntent.putExtra("urls",
											StrUtils.LsToArray(Urls));
									PromptManager.SkipActivity(BaseActivity,
											mIntent);
								}

							});

					LayoutParams layoutParams = new LinearLayout.LayoutParams(
							(screenWidth - DimensionPixelUtil.dip2px(
									BaseContext, 80)),
							LayoutParams.WRAP_CONTENT);
					myItem.item_show_gridview.setLayoutParams(layoutParams);
					if (Urls.size() == 4) {
						myItem.item_show_gridview.setNumColumns(2);

					}
					if (Urls.size() > 4) {
						myItem.item_show_gridview.setNumColumns(3);

					}
				}
			} else {// 视频只有一个relativity

				// String VidoPicUrl = blComment.getPre_url();// 视频的第一帧的图片
				myItem.item_show_gridview.setVisibility(View.GONE);
				myItem.item_show_vido_lay.setVisibility(View.VISIBLE);
				myItem.item_show_oneiv.setVisibility(View.GONE);
				// myItem.item_show_vido_control_image
				try {
					ImageLoaderUtil.Load2(blComment.getPre_url(),
							myItem.item_show_vido_image, R.drawable.error_iv1);
				} catch (Exception e) {
				}

				final String VidoPath = blComment.getVid();
				myItem.item_show_vido_control_image
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								if (CheckNet(BaseContext))
									return;
								PromptManager.SkipActivity(BaseActivity,
										new Intent(BaseActivity,
												AVidemplay.class).putExtra(
												AVidemplay.VidoKey, VidoPath));
							}
						});
			}

		}

		class ShareClick implements OnClickListener {
			private BLShow datBlComment = null;
			private ShowItem showItem;
			private View view;

			public ShareClick() {
				super();
			}

			public ShareClick(BLShow datBlComment, ShowItem showItem, View views) {
				super();
				this.datBlComment = datBlComment;
				this.showItem = showItem;
				this.view = views;
			}

			@Override
			public void onClick(View arg0) {
				// SharePop(datBlComment, view);
				// ShowClick(datBlComment);

				if (datBlComment.getIs_type().equals("0")) {// 照片
					PromptManager
							.SkipActivity(
									BaseActivity,
									new Intent(BaseActivity, ShowSelectPic.class)
											.putExtra(AGoodShare.Key_FromShow,
													true).putExtra(
													AGoodShare.Key_Data,
													datBlComment));

				} else {// 视频
					PromptManager.SkipActivity(
							BaseActivity,
							new Intent(BaseActivity, AGoodVidoShare.class)
									.putExtra(AGoodVidoShare.Key_VidoFromShow,
											true).putExtra(
											AGoodVidoShare.Key_VidoData,
											datBlComment));

				}

			}
		}
	}

	/**
	 * 九宫格图片的Ap
	 */
	class Ivdapter extends BaseAdapter {

		private List<String> datas;

		private LayoutInflater iLayoutInflater;

		public Ivdapter(List<String> datas) {
			super();
			this.datas = datas;

			this.iLayoutInflater = LayoutInflater.from(BaseContext);
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
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			InImageItem imageItem = null;
			if (null == arg1) {
				arg1 = iLayoutInflater.inflate(R.layout.item_show_in_imagview,
						null);
				imageItem = new InImageItem();
				imageItem.item_show_in_imagview = ViewHolder.get(arg1,
						R.id.item_show_in_imagview);
				// LayoutParams layoutParams = new LinearLayout.LayoutParams(
				// (screenWidth - DimensionPixelUtil.dip2px(baseApplication,
				// 1020)) / 3,
				// (screenWidth - DimensionPixelUtil.dip2px(baseApplication,
				// 100)) / 3);
				// imageItem.item_show_in_imagview.setLayoutParams(layoutParams);
				//



				arg1.setTag(imageItem);
			} else {
				imageItem = (InImageItem) arg1.getTag();
			}
//			ImageLoaderUtil.Load22(datas.get(arg0),
//					imageItem.item_show_in_imagview, R.drawable.error_iv2);



			String tag = (String) imageItem.item_show_in_imagview.getTag();
			if (tag == null || !tag.equals(datas.get(arg0))) {
				ImageLoader.getInstance().displayImage(
						datas.get(arg0),
						new ImageViewAware(imageItem.item_show_in_imagview, false), ImageLoaderUtil.GetDisplayOptions(R.drawable.error_iv2),
						new ImageLoadingListener() {
							@Override
							public void onLoadingStarted(String s, View view) {

							}

							@Override
							public void onLoadingFailed(String s, View view,
														FailReason failReason) {

							}

							@Override
							public void onLoadingComplete(String s, View view,
														  Bitmap bitmap) {
								view.setTag(datas.get(arg0));// 确保下载完成再打tag.
							}

							@Override
							public void onLoadingCancelled(String s, View view) {

							}

						});
			}


			return arg1;
		}

		class InImageItem {
			ImageView item_show_in_imagview;
		}
	}

	/**
	 * 图片缓存机制的实力
	 */
	// class ImageCache mCache = new ImageCache() {
	//
	// @Override
	// public void putBitmap(String url, Bitmap bitmap) {
	// }
	//
	// @Override
	// public Bitmap getBitmap(String url) {
	// return null;
	// }
	// };

	/**
	 * 弹出分享上架的操作
	 * 
	 * @param datBlComment2
	 * @param view2
	 */
	private void SharePop(BLComment datBlComment2, View view2) {
		PShowShangJia pShowShangJia = new PShowShangJia(BaseContext,
				BaseActivity, screenWidth, datBlComment2);
		pShowShangJia.showAtLocation(view2, Gravity.CENTER, 0, 0);
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == LOAD_REFRESHING) {
				ShowLs.stopRefresh();
			}
			if (msg.what == LOAD_LOADMOREING) {
				ShowLs.stopLoadMore();
			}
		}
	};

	/**
	 * 1=》停止刷新 ；；；；2=>停止加载更多
	 * 
	 * @param Type
	 */
	private void IsStopFresh(int Type) {
		Message m = new Message();
		m.what = Type;
		mHandler.sendMessage(m);
	}

	@Override
	public void onRefresh() {
		LastId = "";
		IData(LastId, LOAD_REFRESHING);
	}

	@Override
	public void onLoadMore() {
		IData(LastId, LOAD_LOADMOREING);
	}

	// 点击分享后需要进行判断IsPass=>true标识可以上架/////IsPass=>false标识不可以上架只能转发
	// private void ShowSelectPop(final BLShow datBlComment2,
	// final Boolean IsBrand, final Boolean IsPass) {
	// String Title = StrUtils.NullToStr(datBlComment2.getSellerinfo()
	// .getSeller_name()) + "(" + (IsBrand ? "品牌" : "自营") + ")";
	//
	// ShowCustomDialog(Title, "取消", "分享", new IDialogResult() {
	// @Override
	// public void RightResult() {
	// if (datBlComment2.getIs_type().equals("0")) {// 照片
	// PromptManager.SkipActivity(
	// BaseActivity,
	// new Intent(BaseActivity, AGoodShare.class)
	// .putExtra(AGoodShare.Key_FromShow, true)
	// .putExtra(AGoodShare.Key_Data,
	// datBlComment2));
	//
	// } else {// 视频
	// PromptManager.SkipActivity(
	// BaseActivity,
	// new Intent(BaseActivity, AGoodVidoShare.class)
	// .putExtra(AGoodVidoShare.Key_VidoFromShow,
	// true).putExtra(
	// AGoodVidoShare.Key_VidoData,
	// datBlComment2));
	//
	// }
	//
	// }
	//
	// @Override
	// public void LeftResult() {
	// }
	// });
	//
	// // final CustomDialog dialog = new CustomDialog(BaseContext,
	// // R.style.mystyle, R.layout.customdialog, 3, "取消", !IsPass ? "分享"
	// // : "上架并分享");
	// // final CustomDialog dialog = new CustomDialog(BaseContext,
	// // R.style.mystyle, R.layout.customdialog, 3, "取消", "分享");
	// //
	// // dialog.show();
	// // dialog.setTitleText(Title);
	// // dialog.setConfirmListener(new onConfirmClick() {
	// // @Override
	// // public void onConfirmCLick(View v) {
	// // dialog.dismiss();
	// // // if (IsPass) {// 是品牌商品直接分享
	// // // PromptManager.SkipActivity(
	// // // BaseActivity,
	// // // new Intent(BaseActivity, AGoodDetail.class)
	// // // .putExtra("needshowpop", true).putExtra(
	// // // "goodid",
	// // // datBlComment2.getGoods_id()));
	// // // } else {// 是自营产品时候=》需要调用接口判断是否已经上架过=》if(上架过)跳转分享页面else跳转商品详情
	// //
	// // if (datBlComment2.getIs_type().equals("0")) {// 照片
	// // PromptManager.SkipActivity(
	// // BaseActivity,
	// // new Intent(BaseActivity, AGoodShare.class)
	// // .putExtra(AGoodShare.Key_FromShow, true)
	// // .putExtra(AGoodShare.Key_Data,
	// // datBlComment2));
	// //
	// // } else {// 视频
	// // PromptManager.SkipActivity(
	// // BaseActivity,
	// // new Intent(BaseActivity, AGoodVidoShare.class)
	// // .putExtra(AGoodVidoShare.Key_VidoFromShow,
	// // true).putExtra(
	// // AGoodVidoShare.Key_VidoData,
	// // datBlComment2));
	// //
	// // }
	// //
	// // // }
	// //
	// // }
	// // });
	// // dialog.setcancelListener(new oncancelClick() {
	// // @Override
	// // public void oncancelClick(View v) {
	// // dialog.dismiss();
	// // }
	// // });
	// }
	//
	// // 是否品牌商品=》需要
	// private void ShowClick(final BLShow datBlComment2) {
	// final boolean IsBrand = datBlComment2.getSellerinfo().getIs_brand()
	// .equals("1");
	// PromptManager.showtextLoading(BaseContext,
	// getResources().getString(R.string.loading));
	// NHttpBaseStr mHttpBaseStr = new NHttpBaseStr(BaseContext);
	// mHttpBaseStr.setPostResult(new IHttpResult<String>() {
	//
	// @Override
	// public void onError(String error, int LoadType) {
	// PromptManager.ShowCustomToast(BaseContext, error);
	// }
	//
	// @Override
	// public void getResult(int Code, String Msg, String Data) {
	// // if (IsBrand) {// 是品牌商品
	// // if (200 == Code) {// 品牌商品可以上架并且分享
	// ShowSelectPop(datBlComment2, IsBrand, 200 == Code);
	// // } else {// 品牌商品不可以上架=》只能分享
	// // }
	// //
	// // } else {// 是自营商品
	// // if (200 == Code) {// 自营商品可以上架并且分享
	// // } else {// 品牌商品可以上架=》只能分享
	// // }
	// // }
	//
	// }
	// });
	//
	// HashMap<String, String> map = new HashMap<String, String>();
	// map.put("goods_id", datBlComment2.getGoods_id());
	// map.put("seller_id", user_Get.getSeller_id());
	// mHttpBaseStr.getData(IsBrand ? Constants.IsDaiMai_Brand
	// : Constants.IsDaiMai_ZiYing, map, Method.GET);
	//
	// }

	/**
	 * 删除我自己的show
	 */

	private void DeletMyShow(String ShowId, int postion) {
		HashMap<String, String> mHashMap = new HashMap<String, String>();

		mHashMap.put("id", ShowId);
		mHashMap.put("seller_id", user_Get.getSeller_id());

		FBGetHttpData(mHashMap, Constants.MyShowDelete, Method.DELETE, 11,
				postion + 11);

	}

	/**
	 * 选择的操作 Type==1标识的是删除show
	 */
	private void SelectToDo(int Type, final String ShowId, final int Postion) {
		ShowCustomDialog("是否删除该show", "取消", "确定", new IDialogResult() {

			@Override
			public void RightResult() {
				PromptManager.showtextLoading3(BaseContext, "正在删除..");
				DeletMyShow(ShowId, Postion);
			}

			@Override
			public void LeftResult() {
			}
		});

		// final CustomDialog dialog = new CustomDialog(BaseContext,
		// R.style.mystyle, R.layout.customdialog, 3, "取消", "确定");
		//
		// dialog.show();
		// dialog.setTitleText("是否删除该show");
		// dialog.setConfirmListener(new onConfirmClick() {
		// @Override
		// public void onConfirmCLick(View v) {
		//
		// dialog.dismiss();
		// PromptManager.showtextLoading3(BaseContext, "正在删除..");
		// DeletMyShow(ShowId, Postion);
		// }
		// });
		// dialog.setcancelListener(new oncancelClick() {
		// @Override
		// public void oncancelClick(View v) {
		// dialog.dismiss();
		// }
		// });
	}
}
