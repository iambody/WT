package io.vtown.WeiTangApp.comment.view.pop;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.ui.comment.AGoodShare;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-17 下午2:30:46
 * 
 */
public class PShowShangJia extends PopupWindow implements OnClickListener {

	/**
	 * 上下文
	 */
	private Context pContext;
	private Activity pActivity;

	/**
	 * 基view
	 */
	private View BaseView;

	/**
	 * 外层的view
	 */
	private View conten_show_shangjia;
	/**
	 * 内容的view
	 */
	private View conten_show_shangjia_nei;

	/**
	 * 取消按钮
	 */
	private ImageView pop_show_shangjia_cha;
	/**
	 * 规格的标题
	 */
	private TextView pop_show_shangjia_guige1, pop_show_shangjia_guige2;

	/**
	 * 规格的数据
	 */
	private CompleteGridView pop_show_shangjia_guige_grid,
			pop_show_shangjia_guige_grid_down;
	/**
	 * 规格的AP
	 */
	private MyAdapter myUpAdapter;
	/**
	 * 规格的AP
	 */
	private MyAdapter myDownAdapter;

	/**
	 * 数据源
	 */
	private BLComment datBlComment2;
	/**
	 * 输入框
	 */
	private EditText pop_show_shangjia_price_ed;
	/**
	 * 价格
	 */
	private TextView pop_show_shangjia_myprice;
	/**
	 * 库存
	 */
	private TextView pop_show_shangjia_kucun;

	private Button pop_show_shangjia_confirm_btn, pop_show_shangjia_cancel_btn;

	/**
	 * 是否第一次点击过的标识 1=》标识是先点击的上边/////2=》标识先点击的下边/////0=》标识未点击
	 */
	private int IsFristClick = 0;

	private int UpPostion, DownPostion;

	public PShowShangJia(Context pContext, Activity activity, int width,
			BLComment datBlCommen) {
		super();
		this.pActivity = activity;
		this.pContext = pContext;

		BaseView = LayoutInflater.from(pContext).inflate(
				R.layout.pop_show_shangjia, null);
		datBlComment2 = datBlCommen;
		// datBlComment2.getGoodinfo().getAttr();//列表
		conten_show_shangjia = BaseView.findViewById(R.id.conten_show_shangjia);
		conten_show_shangjia_nei = BaseView
				.findViewById(R.id.conten_show_shangjia_nei);
		IPop(width);
		IView();
		ITouch();
	}

	private void IView() {
		pop_show_shangjia_confirm_btn = ViewHolder.get(BaseView,
				R.id.pop_show_shangjia_confirm_btn);
		pop_show_shangjia_cancel_btn = ViewHolder.get(BaseView,
				R.id.pop_show_shangjia_cancel_btn);
		pop_show_shangjia_confirm_btn.setOnClickListener(this);
		pop_show_shangjia_cancel_btn.setOnClickListener(this);

		pop_show_shangjia_price_ed = ViewHolder.get(BaseView,
				R.id.pop_show_shangjia_price_ed);
		pop_show_shangjia_myprice = ViewHolder.get(BaseView,
				R.id.pop_show_shangjia_myprice);
		pop_show_shangjia_kucun = ViewHolder.get(BaseView,
				R.id.pop_show_shangjia_kucun);

		// pop_show_shangjia_price.setInputType(InputType.TYPE_CLASS_NUMBER
		// | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		pop_show_shangjia_cha = ViewHolder.get(BaseView,
				R.id.pop_show_shangjia_cha);
		pop_show_shangjia_guige_grid = (CompleteGridView) BaseView
				.findViewById(R.id.pop_show_shangjia_guige_grid);
		pop_show_shangjia_guige_grid_down = (CompleteGridView) BaseView
				.findViewById(R.id.pop_show_shangjia_guige_grid_down);

		pop_show_shangjia_guige1 = ViewHolder.get(BaseView,
				R.id.pop_show_shangjia_guige1);
		pop_show_shangjia_guige2 = ViewHolder.get(BaseView,
				R.id.pop_show_shangjia_guige2);
		if (null == datBlComment2.getGoodinfo().getAttr()
				|| datBlComment2.getGoodinfo().getAttr().size() == 0)
			return;
		StrUtils.SetTxt(pop_show_shangjia_myprice, "￥"
				+ datBlComment2.getGoodinfo().getSell_price());
		StrUtils.SetTxt(pop_show_shangjia_kucun, String.format("库存%s件",
				datBlComment2.getGoodinfo().getPostage()));
		StrUtils.SetTxt(pop_show_shangjia_guige1, datBlComment2.getGoodinfo()
				.getAttr().get(0).getAttr_map().getC1()
				+ ":");
		StrUtils.SetTxt(pop_show_shangjia_guige2, datBlComment2.getGoodinfo()
				.getAttr().get(0).getAttr_map().getC2()
				+ ":");

		myUpAdapter = new MyAdapter(FilterData(datBlComment2.getGoodinfo()
				.getAttr(), 1), pContext, 1);
		myDownAdapter = new MyAdapter(FilterData(datBlComment2.getGoodinfo()
				.getAttr(), 2), pContext, 2);

		// 上边
		pop_show_shangjia_guige_grid.setNumColumns(3);
		pop_show_shangjia_guige_grid.setHorizontalSpacing(DimensionPixelUtil
				.dip2px(pContext, 5));
		pop_show_shangjia_guige_grid.setVerticalSpacing(DimensionPixelUtil
				.dip2px(pContext, 5));
		// 下边
		pop_show_shangjia_guige_grid_down.setNumColumns(3);
		pop_show_shangjia_guige_grid_down
				.setHorizontalSpacing(DimensionPixelUtil.dip2px(pContext, 5));
		pop_show_shangjia_guige_grid_down.setVerticalSpacing(DimensionPixelUtil
				.dip2px(pContext, 5));

		pop_show_shangjia_guige_grid.setAdapter(myUpAdapter);
		pop_show_shangjia_guige_grid_down.setAdapter(myDownAdapter);
		pop_show_shangjia_guige_grid
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						BLComment da = (BLComment) arg0.getItemAtPosition(arg2);

						UpPostion = arg2;
						switch (IsFristClick) {
						case 0:
						case 1:

							IsFristClick = 1;
							for (int i = 0; i < myUpAdapter.gradItems().size(); i++) {
								myUpAdapter.gradItems().get(i).tv_showgrad_content
										.setBackground(pContext
												.getResources()
												.getDrawable(
														(arg2 == i) ? R.drawable.shape_pop_gridview_txt_select_bg
																: R.drawable.shape_pop_gridview_txt_normal_bg));
								myUpAdapter.gradItems().get(i).tv_showgrad_content
										.setTextColor(pContext
												.getResources()
												.getColor(
														(arg2 == i) ? R.color.white
																: R.color.app_black));
							}

							myDownAdapter = new MyAdapter(GetFrashData(da
									.getAttr_map().getV1(), 2), pContext, 2);
							pop_show_shangjia_guige_grid_down
									.setAdapter(myDownAdapter);
							// PromptManager.ShowCustomToast(pContext, "上边的规格："
							// + da.getAttr_map().getV1());
							break;
						case 2:
							for (int i = 0; i < myUpAdapter.gradItems().size(); i++) {
								myUpAdapter.gradItems().get(i).tv_showgrad_content
										.setBackground(pContext
												.getResources()
												.getDrawable(
														(arg2 == i) ? R.drawable.shape_pop_gridview_txt_select_bg
																: R.drawable.shape_pop_gridview_txt_normal_bg));
								myUpAdapter.gradItems().get(i).tv_showgrad_content
										.setTextColor(pContext
												.getResources()
												.getColor(
														(arg2 == i) ? R.color.white
																: R.color.app_black));
							}
							break;

						}

					}
				});
		pop_show_shangjia_guige_grid_down
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						BLComment da = (BLComment) arg0.getItemAtPosition(arg2);
						DownPostion = arg2;
						switch (IsFristClick) {
						case 0:
						case 2:
							IsFristClick = 2;
							for (int i = 0; i < myDownAdapter.gradItems()
									.size(); i++) {
								myDownAdapter.gradItems().get(i).tv_showgrad_content
										.setBackground(pContext
												.getResources()
												.getDrawable(
														(arg2 == i) ? R.drawable.shape_pop_gridview_txt_select_bg
																: R.drawable.shape_pop_gridview_txt_normal_bg));
								myDownAdapter.gradItems().get(i).tv_showgrad_content
										.setTextColor(pContext
												.getResources()
												.getColor(
														(arg2 == i) ? R.color.white
																: R.color.app_black));
							}

							myUpAdapter = new MyAdapter(GetFrashData(da
									.getAttr_map().getV2(), 1), pContext, 1);
							pop_show_shangjia_guige_grid
									.setAdapter(myUpAdapter);

							// PromptManager.ShowCustomToast(pContext, "上边的规格："
							// + da.getAttr_map().getV2());
							break;
						case 1:

							for (int i = 0; i < myDownAdapter.gradItems()
									.size(); i++) {
								myDownAdapter.gradItems().get(i).tv_showgrad_content
										.setBackground(pContext
												.getResources()
												.getDrawable(
														(arg2 == i) ? R.drawable.shape_pop_gridview_txt_select_bg
																: R.drawable.shape_pop_gridview_txt_normal_bg));
								myDownAdapter.gradItems().get(i).tv_showgrad_content
										.setTextColor(pContext
												.getResources()
												.getColor(
														(arg2 == i) ? R.color.white
																: R.color.app_black));
							}
							break;

						}

					}
				});

	}

	// 第一次展示时候获取得到两个gradview的数据 避免重复
	private List<BLComment> FilterData(List<BLComment> da, int type) {// type=1标识上边的
		List<String> NameStrs = new ArrayList<String>();
		List<BLComment> datas = new ArrayList<BLComment>();
		for (int i = 0; i < da.size(); i++) {
			if (!NameStrs.contains(1 == type ? da.get(i).getAttr_map().getV1()
					: da.get(i).getAttr_map().getV2())) {
				NameStrs.add(1 == type ? da.get(i).getAttr_map().getV1() : da
						.get(i).getAttr_map().getV2());
				datas.add(da.get(i));
			}
		}
		return datas;
	}

	// 点击完后刷新另外一个gradview的Ap //type=>1标识需要刷新上边的数据源////type=>2标识需要刷新下边的数据源
	private List<BLComment> GetFrashData(String SortKey, int type) {
		List<BLComment> datas = new ArrayList<BLComment>();
		for (int i = 0; i < datBlComment2.getGoodinfo().getAttr().size(); i++) {
			if (SortKey.equals((1 != type ? datBlComment2.getGoodinfo()
					.getAttr().get(i).getAttr_map().getV1() : datBlComment2
					.getGoodinfo().getAttr().get(i).getAttr_map().getV2()))) {
				datas.add(datBlComment2.getGoodinfo().getAttr().get(i));
			}
		}
		return datas;
	}

	private void IPop(int width) {
		setContentView(BaseView);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);

		this.setFocusable(true);
		ColorDrawable dw = new ColorDrawable(0x00000000);
		setBackgroundDrawable(dw);
		this.setOutsideTouchable(true);
	}

	private void ITouch() {
		conten_show_shangjia.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				int Bottom = conten_show_shangjia_nei.getBottom();
				int Top = conten_show_shangjia_nei.getTop();
				int Left = conten_show_shangjia_nei.getLeft();
				int Right = conten_show_shangjia_nei.getRight();
				int y = (int) event.getY();
				int x = (int) event.getX();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y > Bottom || y < Top || x < Left || x > Right) {
						dismiss();
					}
				}
				return true;
			}
		});
	}

	class MyAdapter extends BaseAdapter {
		private List<BLComment> mBldComment;
		private Context mContext;
		private LayoutInflater layoutInflater;
		private int Type;// 1标识上边 2标识下边
		private List<ShowGradItem> items;
		private List<Boolean> iBooleans;
		private List<ShowGradItem> Frist = new ArrayList<PShowShangJia.MyAdapter.ShowGradItem>();;

		public MyAdapter(List<BLComment> mBldComments, Context mContext,
				int MyType) {
			super();
			this.mBldComment = mBldComments;
			this.mContext = mContext;
			this.layoutInflater = LayoutInflater.from(mContext);
			this.Type = MyType;

			items = new ArrayList<ShowGradItem>();
			iBooleans = new ArrayList<Boolean>();
			for (int i = 0; i < mBldComments.size(); i++) {
				iBooleans.add(false);
				if (i == mBldComments.size() - 1)
					continue;

				items.add(new ShowGradItem());
			}
		}

		/**
		 * 暴露boole数组源
		 */
		private List<Boolean> getBooleans() {
			return iBooleans;
		}

		/**
		 * 暴露item值源
		 * 
		 * @return
		 */
		private List<ShowGradItem> gradItems() {
			return items;

		}

		/**
		 * 暴露数据源
		 * 
		 * @return
		 */
		private List<BLComment> GetDataResource() {
			return mBldComment;
		}

		@Override
		public int getCount() {
			return mBldComment.size();
		}

		@Override
		public Object getItem(int arg0) {
			return mBldComment.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			ShowGradItem gradItem = null;

			if (null == convertView) {
				convertView = layoutInflater.inflate(
						R.layout.item_shangjia_grad, null);
				if (0 == arg0) {
					gradItem = new ShowGradItem();
					Frist.add(gradItem);
				} else if (arg0 != items.size()) {
					gradItem = gradItems().get(arg0 - 1);
				} else {
					gradItem = gradItems().get(arg0 - 1);

					List<ShowGradItem> da = new ArrayList<ShowGradItem>();
					da.add(Frist.get(Frist.size() - 1));
					da.addAll(gradItems());
					items = da;
				}

				gradItem.tv_showgrad_content = ViewHolder.get(convertView,
						R.id.tv_shangjiagrad_content);

				convertView.setTag(gradItem);
			} else {
				gradItem = (ShowGradItem) convertView.getTag();
			}

			// gradItem.tv_net_content.setOnClickListener(new SortItemClick(
			// MyAdapter.this, arg0, Type, GetDataResource()));
			StrUtils.SetTxt(gradItem.tv_showgrad_content,
					1 == Type ? mBldComment.get(arg0).getAttr_map().getV1()
							: mBldComment.get(arg0).getAttr_map().getV2());

			// shape_pop_gridview_txt_normal_bg.se
			gradItem.tv_showgrad_content
					.setBackground(pContext
							.getResources()
							.getDrawable(
									getBooleans().get(arg0) ? R.drawable.shape_pop_gridview_txt_select_bg
											: R.drawable.shape_pop_gridview_txt_normal_bg));
			gradItem.tv_showgrad_content.setTextColor(pContext.getResources()
					.getColor(
							getBooleans().get(arg0) ? R.color.white
									: R.color.app_black));
			// SetItemIsClick(gradItem.tv_net_content, arg0, Type,
			// GetDataResource());

			return convertView;
		}

		class ShowGradItem {
			TextView tv_showgrad_content;
		}

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.pop_show_shangjia_confirm_btn: // 直接上架

			break;
		case R.id.pop_show_shangjia_cancel_btn:// 上架并转发
			PromptManager.SkipActivity(pActivity, new Intent(pContext,
					AGoodShare.class));// AGoodShare.class);

			break;
		default:
			break;
		}
	}

	private void ShangjiaNet() {

	}

}
