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
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
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
import io.vtown.WeiTangApp.ui.comment.AphotoPager;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.center.myshow.AOtherShow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-8-11 下午2:53:20
 * 
 */
public class AShowNew extends ATitleBase implements IXListViewListener {
	/**
	 * ls
	 */
	private LListView ShowLs;
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

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_show);
		BaseView = LayoutInflater.from(BaseContext).inflate(
				R.layout.activity_show, null);
		user_Get = Spuit.User_Get(BaseContext);
		SetTitleHttpDataLisenter(this);

		EventBus.getDefault().register(this, "GetMessage", BMessage.class);

		IBase();
		ICacheLs();

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
				IData(LastId, LOAD_INITIALIZE);
				return;
			}

			showAp.FrashData(data);
			IsCache = true;
		} else {// 没有数据就直接显示空白
			IsCache = false;
			IDataView(ShowLs, show_nodata_lay, NOVIEW_INITIALIZE);
			IData(LastId, LOAD_INITIALIZE);
		}
	}

	private void IData(String LastId, int LoadType) {
		if (LoadType == LOAD_INITIALIZE && !IsCache)
			PromptManager.showtextLoading(BaseContext, getResources()
					.getString(R.string.loading));
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("seller_id", user_Get.getSeller_id());
		map.put("lastid", LastId);
		// map.put("pagesize", "10");
		FBGetHttpData(map, Constants.Show_ls, Method.GET, 0, LoadType);
	}

	private void IBase() {
		show_nodata_lay = findViewById(R.id.show_nodata_lay);

		ShowLs = (LListView) findViewById(R.id.activity_show_ls);
		ShowLs.setPullLoadEnable(true);
		ShowLs.setPullRefreshEnable(true);
		ShowLs.setXListViewListener(this);
		ShowLs.hidefoot();
		// ls滑动时候不要加载图片
//		ShowLs.setOnScrollListener(getPauseOnScrollListener(new OnScrollListener() {
//
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				// if (scrollState == OnScrollListener.SCROLL_STATE_IDLE)
//				// {//list停止滚动时加载图片
//				// loadImage(startPos, endPos);// 异步加载图片 ,只加载可以看到的图片
//				// }
//				// if(scrollState==OnScrollListener.){}
//
//				// switch (scrollState) {
//				// case OnScrollListener.SCROLL_STATE_FLING:
//				// PromptManager.ShowCustomToast(BaseContext,
//				// "起始位置:"+startPos+"   起始位置:"+endPos);
//				// break;
//				//
//				// default:
//				// break;
//				// }
//			}
//
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem,
//					int visibleItemCount, int totalItemCount) {
//				// 设置当前屏幕显示的起始pos和结束pos
//				startPos = firstVisibleItem;
//				endPos = firstVisibleItem + visibleItemCount;
//				if (endPos >= totalItemCount) {
//					endPos = totalItemCount - 1;
//				}
//
//			}
//		}));

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

		/**
		 * 1=>一张图片 2==》2张图片 3=》视频
		 */

		@Override
		public int getItemViewType(int position) {
			BLShow myBlShow = datas.get(position);
			boolean IsPic = myBlShow.getIs_type().equals("0");
			List<String> Urls = myBlShow.getImgarr();
			if (!IsPic)
				return 3;
			if (Urls.size() == 1)
				return 1;
			if (Urls.size() > 1)
				return 2;
			return -1;

		}

		@Override
		public int getViewTypeCount() {
			return 3;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
			viewHolder_One_Iv holder_One_Iv = null;
			viewHolder_Two_Iv holder_Two_Iv = null;
			viewHolder_Vido holder_Vido = null;
			int type = getItemViewType(position);
			final BLShow myBlShow = datas.get(position);
			final int Mypostion = position;
			if (convertView == null) {
				switch (type) {
				case 1:// 一张图片
					holder_One_Iv = new viewHolder_One_Iv();
					convertView = inflater.inflate(R.layout.item_show_oniv,
							null);
					holder_One_Iv.item_show_iv = (CircleImageView) convertView
							.findViewById(R.id.item_show_iv);// ViewHolder.get(convertView,
					// R.id.item_show_iv);
					holder_One_Iv.item_show_gooddetail_lay = (LinearLayout) convertView
							.findViewById(R.id.item_show_gooddetail_lay);
					holder_One_Iv.item_show_name = ViewHolder.get(convertView,
							R.id.item_show_name);
					holder_One_Iv.item_show_txt_inf = ViewHolder.get(
							convertView, R.id.item_show_txt_inf);
					holder_One_Iv.item_show_time = ViewHolder.get(convertView,
							R.id.item_show_time);
					holder_One_Iv.item_show_delete_txt = ViewHolder.get(
							convertView, R.id.item_show_delete_txt);
					holder_One_Iv.item_show_gooddetail_iv = ViewHolder.get(
							convertView, R.id.item_show_gooddetail_iv);
					holder_One_Iv.item_show_share_iv = ViewHolder.get(
							convertView, R.id.item_show_share_iv);
					holder_One_Iv.item_show_share_number = ViewHolder.get(
							convertView, R.id.item_show_share_number);

					//
					holder_One_Iv.item_show_one_iv = ViewHolder.get(
							convertView, R.id.item_show_one_iv);

					//
					// holder_One_Iv.item_show_iv.setOnClickListener(holder_One_Iv.this);

					convertView.setTag(holder_One_Iv);
					break;
				case 2:// 两张图片CompleteGridView item_show_gridview;
					holder_Two_Iv = new viewHolder_Two_Iv();
					convertView = inflater.inflate(R.layout.item_show_moreiv,
							null);
					holder_Two_Iv.item_show_iv = (CircleImageView) convertView
							.findViewById(R.id.item_show_iv);
					holder_Two_Iv.item_show_gooddetail_lay = (LinearLayout) convertView
							.findViewById(R.id.item_show_gooddetail_lay);
					holder_Two_Iv.item_show_name = ViewHolder.get(convertView,
							R.id.item_show_name);
					holder_Two_Iv.item_show_txt_inf = ViewHolder.get(
							convertView, R.id.item_show_txt_inf);
					holder_Two_Iv.item_show_time = ViewHolder.get(convertView,
							R.id.item_show_time);
					holder_Two_Iv.item_show_delete_txt = ViewHolder.get(
							convertView, R.id.item_show_delete_txt);
					holder_Two_Iv.item_show_gooddetail_iv = ViewHolder.get(
							convertView, R.id.item_show_gooddetail_iv);
					holder_Two_Iv.item_show_share_iv = ViewHolder.get(
							convertView, R.id.item_show_share_iv);
					holder_Two_Iv.item_show_share_number = ViewHolder.get(
							convertView, R.id.item_show_share_number);
					//
					holder_Two_Iv.item_show_gridview = (CompleteGridView) convertView
							.findViewById(R.id.item_show_gridview);

					convertView.setTag(holder_Two_Iv);
					break;
				case 3:// 视频布局
					holder_Vido = new viewHolder_Vido();
					convertView = inflater.inflate(R.layout.item_show_vido,
							null);
					holder_Vido.item_show_iv = (CircleImageView) convertView
							.findViewById(R.id.item_show_iv);
					holder_Vido.item_show_gooddetail_lay = (LinearLayout) convertView
							.findViewById(R.id.item_show_gooddetail_lay);
					holder_Vido.item_show_name = ViewHolder.get(convertView,
							R.id.item_show_name);
					holder_Vido.item_show_txt_inf = ViewHolder.get(convertView,
							R.id.item_show_txt_inf);
					holder_Vido.item_show_time = ViewHolder.get(convertView,
							R.id.item_show_time);
					holder_Vido.item_show_delete_txt = ViewHolder.get(
							convertView, R.id.item_show_delete_txt);
					holder_Vido.item_show_gooddetail_iv = ViewHolder.get(
							convertView, R.id.item_show_gooddetail_iv);
					holder_Vido.item_show_share_iv = ViewHolder.get(
							convertView, R.id.item_show_share_iv);
					holder_Vido.item_show_share_number = ViewHolder.get(
							convertView, R.id.item_show_share_number);
					//
					holder_Vido.item_show_vido_image = ViewHolder.get(
							convertView, R.id.item_show_vido_image);
					holder_Vido.item_show_vido_control_image = ViewHolder.get(
							convertView, R.id.item_show_vido_control_image);

					convertView.setTag(holder_Vido);
					break;
				default:
					break;
				}
			} else {

				switch (type) {
				case 1:
					holder_One_Iv = (viewHolder_One_Iv) convertView.getTag();
					break;
				case 2:
					holder_Two_Iv = (viewHolder_Two_Iv) convertView.getTag();
					break;
				case 3:
					holder_Vido = (viewHolder_Vido) convertView.getTag();
					break;
				default:
					break;
				}

			}

			switch (type) {
			case 1:// 一张图
				holder_One_Iv.SetPostion(position);

				ImageLoaderUtil.Load2(myBlShow.getSellerinfo().getAvatar(),
						holder_One_Iv.item_show_iv, R.drawable.testiv);
				// 是我发的show就需要显示删除
				holder_One_Iv.item_show_delete_txt
						.setVisibility(user_Get.getSeller_id().equals(
								myBlShow.getSeller_id()) ? View.VISIBLE
								: View.GONE);
				// 点击分享
				// holder_One_Iv.item_show_share_iv.setOnClickListener(new
				// ShareClick(
				// myBlShow, myItem, BaseView));
				StrUtils.SetTxt(holder_One_Iv.item_show_share_number,
						myBlShow.getSendnumber() + "人转发");
				StrUtils.SetTxt(holder_One_Iv.item_show_name, myBlShow
						.getSellerinfo().getSeller_name());
				StrUtils.SetTxt(holder_One_Iv.item_show_txt_inf,
						myBlShow.getIntro());
				StrUtils.SetTxt(holder_One_Iv.item_show_time, DateUtils
						.convertTimeToFormat(Long.parseLong(myBlShow
								.getCreate_time())));

				ImageLoaderUtil.Load2(myBlShow.getImgarr().get(0),
						holder_One_Iv.item_show_one_iv, R.drawable.error_iv1);
				// *********************************************************
				// 查看详情
				holder_One_Iv.item_show_iv
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								if (CheckNet(BaseContext))
									return;
								Intent intent = new Intent(BaseActivity,
										AOtherShow.class);
								intent.putExtra(BaseKey_Bean, new BComment(
										myBlShow.getSeller_id(), myBlShow
												.getSellerinfo().getCover(),
										myBlShow.getSellerinfo().getAvatar(),
										myBlShow.getSellerinfo()
												.getSeller_name(), myBlShow
												.getSellerinfo().getIs_brand()));
								PromptManager
										.SkipActivity(BaseActivity, intent);
							}
						});
				// 点击分享
				holder_One_Iv.item_show_share_iv
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (myBlShow.getIs_type().equals("0")) {// 照片
									PromptManager.SkipActivity(
											BaseActivity,
											new Intent(BaseActivity,
													AGoodShare.class).putExtra(
													AGoodShare.Key_FromShow,
													true).putExtra(
													AGoodShare.Key_Data,
													myBlShow));

								} else {// 视频
									PromptManager
											.SkipActivity(
													BaseActivity,
													new Intent(
															BaseActivity,
															AGoodVidoShare.class)
															.putExtra(
																	AGoodVidoShare.Key_VidoFromShow,
																	true)
															.putExtra(
																	AGoodVidoShare.Key_VidoData,
																	myBlShow));

								}

							}
						});
				holder_One_Iv.item_show_gooddetail_iv
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								PromptManager.SkipActivity(BaseActivity,
										new Intent(BaseActivity,
												AGoodDetail.class).putExtra(
												"goodid",
												myBlShow.getGoods_id()));
							}
						});

				holder_One_Iv.item_show_delete_txt
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (CheckNet(BaseContext))
									return;
								SelectToDo(1, myBlShow.getId(), Mypostion);
							}
						});
				break;
			case 2:// 多张图
				holder_Two_Iv.SetPostion(position);
				ImageLoaderUtil.Load2(myBlShow.getSellerinfo().getAvatar(),
						holder_Two_Iv.item_show_iv, R.drawable.testiv);
				// 是我发的show就需要显示删除
				holder_Two_Iv.item_show_delete_txt
						.setVisibility(user_Get.getSeller_id().equals(
								myBlShow.getSeller_id()) ? View.VISIBLE
								: View.GONE);
				// 点击分享
				// holder_One_Iv.item_show_share_iv.setOnClickListener(new
				// ShareClick(
				// myBlShow, myItem, BaseView));
				StrUtils.SetTxt(holder_Two_Iv.item_show_share_number,
						myBlShow.getSendnumber() + "人转发");
				StrUtils.SetTxt(holder_Two_Iv.item_show_name, myBlShow
						.getSellerinfo().getSeller_name());
				StrUtils.SetTxt(holder_Two_Iv.item_show_txt_inf,
						myBlShow.getIntro());
				StrUtils.SetTxt(holder_Two_Iv.item_show_time, DateUtils
						.convertTimeToFormat(Long.parseLong(myBlShow
								.getCreate_time())));
				holder_Two_Iv.item_show_gridview.setAdapter(new Ivdapter(
						myBlShow.getImgarr()));

				// item_show_gridview.new Ivdapter(Urls)
				// *********************************************************
				// 查看详情
				holder_Two_Iv.item_show_iv
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								if (CheckNet(BaseContext))
									return;
								Intent intent = new Intent(BaseActivity,
										AOtherShow.class);
								intent.putExtra(BaseKey_Bean, new BComment(
										myBlShow.getSeller_id(), myBlShow
												.getSellerinfo().getCover(),
										myBlShow.getSellerinfo().getAvatar(),
										myBlShow.getSellerinfo()
												.getSeller_name(), myBlShow
												.getSellerinfo().getIs_brand()));
								PromptManager
										.SkipActivity(BaseActivity, intent);
							}
						});
				// 点击分享
				holder_Two_Iv.item_show_share_iv
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (myBlShow.getIs_type().equals("0")) {// 照片
									PromptManager.SkipActivity(
											BaseActivity,
											new Intent(BaseActivity,
													AGoodShare.class).putExtra(
													AGoodShare.Key_FromShow,
													true).putExtra(
													AGoodShare.Key_Data,
													myBlShow));

								} else {// 视频
									PromptManager
											.SkipActivity(
													BaseActivity,
													new Intent(
															BaseActivity,
															AGoodVidoShare.class)
															.putExtra(
																	AGoodVidoShare.Key_VidoFromShow,
																	true)
															.putExtra(
																	AGoodVidoShare.Key_VidoData,
																	myBlShow));

								}

							}
						});
				holder_Two_Iv.item_show_gooddetail_iv
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								PromptManager.SkipActivity(BaseActivity,
										new Intent(BaseActivity,
												AGoodDetail.class).putExtra(
												"goodid",
												myBlShow.getGoods_id()));
							}
						});

				holder_Two_Iv.item_show_delete_txt
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (CheckNet(BaseContext))
									return;
								SelectToDo(1, myBlShow.getId(), Mypostion);
							}
						});

				holder_Two_Iv.item_show_gridview
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								Intent mIntent = new Intent(BaseContext,
										AphotoPager.class);
								mIntent.putExtra("position", arg2);
								mIntent.putExtra("urls", StrUtils
										.LsToArray(myBlShow.getImgarr()));
								PromptManager.SkipActivity(BaseActivity,
										mIntent);
							}

						});
				break;
			case 3:// 视频 holder_Vido
				holder_Vido.SetPostion(position);
				ImageLoaderUtil.Load2(myBlShow.getSellerinfo().getAvatar(),
						holder_Vido.item_show_iv, R.drawable.testiv);
				// 是我发的show就需要显示删除
				holder_Vido.item_show_delete_txt
						.setVisibility(user_Get.getSeller_id().equals(
								myBlShow.getSeller_id()) ? View.VISIBLE
								: View.GONE);
				// 点击分享
				// holder_One_Iv.item_show_share_iv.setOnClickListener(new
				// ShareClick(
				// myBlShow, myItem, BaseView));

				StrUtils.SetTxt(holder_Vido.item_show_share_number,
						myBlShow.getSendnumber() + "人转发");
				StrUtils.SetTxt(holder_Vido.item_show_name, myBlShow
						.getSellerinfo().getSeller_name());
				StrUtils.SetTxt(holder_Vido.item_show_txt_inf,
						myBlShow.getIntro());
				StrUtils.SetTxt(holder_Vido.item_show_time, DateUtils
						.convertTimeToFormat(Long.parseLong(myBlShow
								.getCreate_time())));
				ImageLoaderUtil.Load2(myBlShow.getPre_url(),
						holder_Vido.item_show_vido_image, R.drawable.testiv);
				// *********************************************************
				// 查看详情
				holder_Vido.item_show_iv
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								if (CheckNet(BaseContext))
									return;
								Intent intent = new Intent(BaseActivity,
										AOtherShow.class);
								intent.putExtra(BaseKey_Bean, new BComment(
										myBlShow.getSeller_id(), myBlShow
												.getSellerinfo().getCover(),
										myBlShow.getSellerinfo().getAvatar(),
										myBlShow.getSellerinfo()
												.getSeller_name(), myBlShow
												.getSellerinfo().getIs_brand()));
								PromptManager
										.SkipActivity(BaseActivity, intent);
							}
						});
				// 点击分享
				holder_Vido.item_show_share_iv
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (myBlShow.getIs_type().equals("0")) {// 照片
									PromptManager.SkipActivity(
											BaseActivity,
											new Intent(BaseActivity,
													AGoodShare.class).putExtra(
													AGoodShare.Key_FromShow,
													true).putExtra(
													AGoodShare.Key_Data,
													myBlShow));

								} else {// 视频
									PromptManager
											.SkipActivity(
													BaseActivity,
													new Intent(
															BaseActivity,
															AGoodVidoShare.class)
															.putExtra(
																	AGoodVidoShare.Key_VidoFromShow,
																	true)
															.putExtra(
																	AGoodVidoShare.Key_VidoData,
																	myBlShow));

								}

							}
						});
				holder_Vido.item_show_gooddetail_iv
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								PromptManager.SkipActivity(BaseActivity,
										new Intent(BaseActivity,
												AGoodDetail.class).putExtra(
												"goodid",
												myBlShow.getGoods_id()));
							}
						});
				holder_Vido.item_show_delete_txt
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (CheckNet(BaseContext))
									return;
								SelectToDo(1, myBlShow.getId(), Mypostion);
							}
						});
				break;

			default:
				break;
			}

			return convertView;
		}

		class viewHolder_One_Iv implements OnClickListener {

			int Postion;
			int Type;// 1标识删除 2标识查看详情 3标识查看show
			//
			CircleImageView item_show_iv;
			LinearLayout item_show_gooddetail_lay;
			TextView item_show_name;
			TextView item_show_txt_inf;
			TextView item_show_time;
			TextView item_show_delete_txt;
			ImageView item_show_gooddetail_iv;
			ImageView item_show_share_iv;
			TextView item_show_share_number;
			//
			ImageView item_show_one_iv;

			public void SetPostion(int postion) {
				Postion = postion;
			}

			public void SetType(int type) {
				Type = type;
			}

			@Override
			public void onClick(View v) {
			}
		}

		class viewHolder_Two_Iv implements OnClickListener {
			int Postion;
			int Type;// 1标识删除 2标识查看详情 3标识查看show
			//
			CircleImageView item_show_iv;
			LinearLayout item_show_gooddetail_lay;
			TextView item_show_name;
			TextView item_show_txt_inf;
			TextView item_show_time;
			TextView item_show_delete_txt;
			ImageView item_show_gooddetail_iv;
			ImageView item_show_share_iv;
			TextView item_show_share_number;

			CompleteGridView item_show_gridview;

			public void SetPostion(int postion) {
				Postion = postion;
			}

			public void SetType(int type) {
				Type = type;
			}

			@Override
			public void onClick(View v) {
			}

		}

		class viewHolder_Vido implements OnClickListener {
			int Postion;
			int Type;// 1标识删除 2标识查看详情 3标识查看show
			//
			CircleImageView item_show_iv;
			LinearLayout item_show_gooddetail_lay;
			TextView item_show_name;
			TextView item_show_txt_inf;
			TextView item_show_time;
			TextView item_show_delete_txt;
			ImageView item_show_gooddetail_iv;
			ImageView item_show_share_iv;
			TextView item_show_share_number;
			//
			ImageView item_show_vido_image;
			ImageView item_show_vido_control_image;

			public void SetPostion(int postion) {
				Postion = postion;
			}

			public void SetType(int type) {
				Type = type;
			}

			@Override
			public void onClick(View v) {
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

				ImageLoaderUtil.Load2(datas.get(arg0),
						imageItem.item_show_in_imagview, R.drawable.error_iv2);

				arg1.setTag(imageItem);
			} else {
				imageItem = (InImageItem) arg1.getTag();
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
	private void ShowSelectPop(final BLShow datBlComment2,
			final Boolean IsBrand, final Boolean IsPass) {
		String Title = StrUtils.NullToStr(datBlComment2.getSellerinfo()
				.getSeller_name()) + "(" + (IsBrand ? "品牌" : "自营") + ")";

		ShowCustomDialog(Title, "取消", "分享", new IDialogResult() {
			@Override
			public void RightResult() {
				if (datBlComment2.getIs_type().equals("0")) {// 照片
					PromptManager.SkipActivity(
							BaseActivity,
							new Intent(BaseActivity, AGoodShare.class)
									.putExtra(AGoodShare.Key_FromShow, true)
									.putExtra(AGoodShare.Key_Data,
											datBlComment2));

				} else {// 视频
					PromptManager.SkipActivity(
							BaseActivity,
							new Intent(BaseActivity, AGoodVidoShare.class)
									.putExtra(AGoodVidoShare.Key_VidoFromShow,
											true).putExtra(
											AGoodVidoShare.Key_VidoData,
											datBlComment2));

				}

			}

			@Override
			public void LeftResult() {
			}
		});

	}

	// 是否品牌商品=》需要
	private void ShowClick(final BLShow datBlComment2) {
		final boolean IsBrand = datBlComment2.getSellerinfo().getIs_brand()
				.equals("1");
		PromptManager.showtextLoading(BaseContext,
				getResources().getString(R.string.loading));
		NHttpBaseStr mHttpBaseStr = new NHttpBaseStr(BaseContext);
		mHttpBaseStr.setPostResult(new IHttpResult<String>() {

			@Override
			public void onError(String error, int LoadType) {
				PromptManager.ShowCustomToast(BaseContext, error);
			}

			@Override
			public void getResult(int Code, String Msg, String Data) {
				// if (IsBrand) {// 是品牌商品
				// if (200 == Code) {// 品牌商品可以上架并且分享
				ShowSelectPop(datBlComment2, IsBrand, 200 == Code);
				// } else {// 品牌商品不可以上架=》只能分享
				// }
				//
				// } else {// 是自营商品
				// if (200 == Code) {// 自营商品可以上架并且分享
				// } else {// 品牌商品可以上架=》只能分享
				// }
				// }

			}
		});

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("goods_id", datBlComment2.getGoods_id());
		map.put("seller_id", user_Get.getSeller_id());
		mHttpBaseStr.getData(IsBrand ? Constants.IsDaiMai_Brand
				: Constants.IsDaiMai_ZiYing, map, Method.GET);

	}

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

		ShowCustomDialog("是否删除该show?", "取消", "确定", new IDialogResult() {

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
