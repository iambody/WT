package io.vtown.WeiTangApp.ui.ui;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcache.BHome;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.goodsort.BGoodSort;
import io.vtown.WeiTangApp.bean.bcomment.easy.goodsort.BLGoodSort;
import io.vtown.WeiTangApp.bean.bcomment.easy.shopbrand.BLBrandGood;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleTextView;
import io.vtown.WeiTangApp.comment.view.ImageCycleView;
import io.vtown.WeiTangApp.comment.view.ImageCycleView.ImageCycleViewListener;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.ACommentList;
import io.vtown.WeiTangApp.ui.comment.AWeb;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;
import io.vtown.WeiTangApp.ui.title.ABrandDetail;
import io.vtown.WeiTangApp.ui.title.AGoodDetail;
import io.vtown.WeiTangApp.ui.title.zhuanqu.AZhuanQu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-4 下午2:22:19
 * @deprecated商品一级分类
 * 
 */
public class AGoodSort extends ATitleBase {

	private LinearLayout goodsortshow_lay;
	private View goodsort_nodata_lay;

	private RelativeLayout goodsort_banner_lay;
	/**
	 * // * page //
	 */
	// private ViewPager MyPager = null;
	// private ImageView imageView = null;
	// private boolean isContinue = true;
	// private AtomicInteger what = new AtomicInteger(0);
	// private ImageView[] DotimageViews = {};// 控制
	// /**
	// * 原点的view
	// */
	// private ViewGroup DotGroup;
	// /**
	// * page
	// */

	/**
	 * 轮播图
	 */
	private ImageCycleView goodsort_banner;

	/**
	 * gridview
	 */
	private CompleteGridView goodsort_grid;
	/**
	 * gradview对应的ap
	 */
	private SortAp SortAp;
	/**
	 * ls
	 */
	private CompleteListView goodsort_list;
	/**
	 * 页面数据
	 */
	private BGoodSort bComment = null;
	/**
	 * 商品的Ap
	 */
	private GoodsAp goodsAp;
	private int[] Clors = { Color.GREEN, Color.GRAY, Color.CYAN, Color.BLUE,
			Color.MAGENTA };

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_goodshort);
		IPage();
		ISort();
		IGood();
		IData();
	}

	/**
	 * 请求数据
	 */
	private void IData() {
		PromptManager.showtextLoading(BaseContext,
				getResources().getString(R.string.loading));
		SetTitleHttpDataLisenter(this);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("pid", "1");
		FBGetHttpData(map, Constants.Good_Sort, Method.GET, 0, LOAD_INITIALIZE);
	}

	private void IPage() {
		goodsort_banner_lay = (RelativeLayout) findViewById(R.id.goodsort_banner_lay);

		goodsortshow_lay = (LinearLayout) findViewById(R.id.goodsortshow_lay);
		goodsort_nodata_lay = findViewById(R.id.goodsort_nodata_lay);
		goodsort_nodata_lay.setOnClickListener(this);
		IDataView(goodsortshow_lay, goodsort_nodata_lay, NOVIEW_INITIALIZE);

		InItViewPage();
	}

	private void ISort() {
		goodsort_grid = (CompleteGridView) findViewById(R.id.goodsort_grid);
		SortAp = new SortAp(R.layout.item_goodsort_grid);
		goodsort_grid.setAdapter(SortAp);
		goodsort_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (CheckNet(BaseContext))
					return;

				BLGoodSort bl = (BLGoodSort) SortAp.getItem(arg2);
				BComment bc = new BComment(bl.getId(), bl.getCate_name());
				// PromptManager.ShowCustomToast(BaseContext, "点击进去是列表页木接口怎么办");
				Intent intent = new Intent(BaseActivity, ACommentList.class);
				intent.putExtra(ACommentList.Tage_ResultKey,
						ACommentList.Tage_AGoodSort);
				intent.putExtra(ACommentList.Tage_BeanKey, bc);

				PromptManager.SkipActivity(BaseActivity, intent);
			}
		});
	}

	private void IGood() {
		goodsort_list = (CompleteListView) findViewById(R.id.goodsort_list);
		goodsAp = new GoodsAp(R.layout.item_goodsort_recommend);
		goodsort_list.setAdapter(goodsAp);
		goodsort_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (CheckNet(BaseContext))
					return;

				BLBrandGood mBHome = (BLBrandGood) arg0.getItemAtPosition(arg2);

				PromptManager.SkipActivity(BaseActivity, new Intent(
						BaseActivity, AGoodDetail.class).putExtra("goodid",
						mBHome.getId()));
			}
		});
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(baseBcBComment.getTitle());
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {

		if (StrUtils.isEmpty(Data.getHttpResultStr())) {
			DataError(Msg, Data.getHttpLoadType());
			return;
		}
		IDataView(goodsortshow_lay, goodsort_nodata_lay, NOVIEW_RIGHT);

		try {
			bComment = JSON.parseObject(Data.getHttpResultStr(),
					BGoodSort.class);
		} catch (Exception e) {
			PromptManager.ShowCustomToast(BaseContext, "解析错误");
		}

		InItPaGeView(bComment.getBanner());
		SortAp.Refrsh(bComment.getSubcategory());
		goodsAp.Refrsh(bComment.getGoods());
	}

	private void InItPaGeView(List<BLGoodSort> banner) {
		if (banner == null || banner.size() == 0)
			return;
		goodsort_banner.setVisibility(View.VISIBLE);

		goodsort_banner.setImageResources(ToStrls(banner), ToStrls(banner),
				mAdCycleViewListener, screenWidth/2);
	}

	private ArrayList<String> ToStrls(List<BLGoodSort> DAss) {
		ArrayList<String> data = new ArrayList<String>();
		for (int i = 0; i < DAss.size(); i++) {
			data.add(DAss.get(i).getPic_path());
		}
		return data;

	}

	@Override
	protected void DataError(String error, int LoadTyp) {
		PromptManager.ShowCustomToast(BaseContext, error);
		IDataView(goodsortshow_lay, goodsort_nodata_lay, NOVIEW_ERROR);

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
		case R.id.goodsort_nodata_lay:
			IData();
			break;

		default:
			break;
		}
	}

	/**
	 * 初始化viewpage的view
	 */
	private void InItViewPage() {

		goodsort_banner = (ImageCycleView) findViewById(R.id.goodsort_banner);

	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
		@Override
		public void displayImage(String imageURL, ImageView imageView, int postion) {
			ImageLoaderUtil.Load2(imageURL, imageView, R.drawable.error_iv1);
		}

		@Override
		public void onImageClick(int position, View imageView) {

			// PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
			// AWeb.class).putExtra(AWeb.Key_Bean, new BComment(bComment
			// .getBanner().get(position).getUrl(), bComment.getBanner()
			// .get(position).getTitle())));
			BLGoodSort data = bComment.getBanner().get(position);
			int Type = StrUtils.toInt(data.getAdvert_type());

			switch (Type) {
			case 1:// HT跳转

				PromptManager.SkipActivity(BaseActivity, new Intent(
						BaseActivity, AWeb.class).putExtra(
						AWeb.Key_Bean,
						new BComment(data.getUrl(), StrUtils.NullToStr(data
								.getTitle()))));
				break;
			case 2:// 商品详情页
				PromptManager.SkipActivity(BaseActivity, new Intent(
						BaseContext, AGoodDetail.class).putExtra("goodid",
						data.getSource_id()));
				// PromptManager.SkipActivity(BaseActivity, new
				// Intent(BaseContext, APlayer.class));
				break;
			case 3:// 店铺详情页!!!!!!!!!!!!!!!!!!!!!!!!需要修改
				BComment mBComment = new BComment(data.getSource_id(),
						data.getTitle());
				if (data.getIs_brand().equals("1")) {// 品牌店铺
					PromptManager.SkipActivity(BaseActivity, new Intent(
							BaseActivity, ABrandDetail.class).putExtra(
							BaseKey_Bean, mBComment));
				} else {// 自营店铺
					PromptManager.SkipActivity(BaseActivity, new Intent(
							BaseActivity, AShopDetail.class).putExtra(
							BaseKey_Bean, mBComment));
				}
				break;
			case 4:// 活动详情页
				BComment mBCommentss = new BComment(data.getSource_id(),
						data.getTitle());
				PromptManager.SkipActivity(BaseActivity, new Intent(
						BaseContext, AZhuanQu.class).putExtra(BaseKey_Bean,
						mBCommentss));
				break;
			default:
				// default时候直接展示大图
				break;
			}

		}

//		@Override
//		public void displayImage(String imageURL, ImageView imageView) {
//			ImageLoaderUtil.Load2(imageURL, imageView, R.drawable.error_iv1);
//		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		goodsort_banner.startImageCycle();
	};

	@Override
	protected void onPause() {
		super.onPause();
		goodsort_banner.pushImageCycle();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		goodsort_banner.pushImageCycle();
	}

	/**
	 * 分类的Gradview的AP
	 * 
	 * @author datutu
	 * 
	 */
	class SortAp extends BaseAdapter {

		/**
		 * 填充器
		 */
		private LayoutInflater inflater;
		/**
		 * 资源id
		 */
		private int ResourceId;
		private List<BLGoodSort> datas = new ArrayList<BLGoodSort>();

		public SortAp(int resourceId) {
			super();
			this.inflater = LayoutInflater.from(BaseContext);
			this.ResourceId = resourceId;
		}

		public void Refrsh(List<BLGoodSort> da) {
			if (da == null || da.size() == 0) {
				return;
			}
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
			SortItem itemSortItem = null;
			if (null == arg1) {
				itemSortItem = new SortItem();
				arg1 = inflater.inflate(ResourceId, null);
				itemSortItem.item_goodsort_grid_iv = (CircleTextView) arg1
						.findViewById(R.id.item_goodsort_grid_iv);

				arg1.setTag(itemSortItem);
			} else {
				itemSortItem = (SortItem) arg1.getTag();
			}
			BLGoodSort da = datas.get(arg0);
			itemSortItem.item_goodsort_grid_iv
					.setmTextString(da.getCate_name());
			itemSortItem.item_goodsort_grid_iv
					.setmCircleColor(Clors[(arg0 + 5) % 5]);
			return arg1;
		}

		class SortItem {
			CircleTextView item_goodsort_grid_iv;
		}
	}

	/**
	 * 推荐商品的AP
	 */
	class GoodsAp extends BaseAdapter {

		/**
		 * 填充器
		 */
		private LayoutInflater inflater;
		/**
		 * 资源id
		 */
		private int ResourceId;
		private List<BLBrandGood> datas = new ArrayList<BLBrandGood>();

		public GoodsAp(int resourceId) {
			super();

			this.inflater = LayoutInflater.from(BaseContext);
			this.ResourceId = resourceId;
		}

		public void Refrsh(List<BLBrandGood> da) {
			if (da == null || da.size() == 0) {
				return;
			}
			this.datas = da;
			// this.datas.addAll(da);
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
			GoodItem goodItem = null;
			if (null == arg1) {
				goodItem = new GoodItem();
				arg1 = inflater.inflate(ResourceId, null);
				goodItem.item_goodsort_recommend_iv = ViewHolder.get(arg1,
						R.id.item_goodsort_recommend_iv);
				goodItem.item_goodsort_recommend_name = ViewHolder.get(arg1,
						R.id.item_goodsort_recommend_name);
				goodItem.item_goodsort_recommend_price = ViewHolder.get(arg1,
						R.id.item_goodsort_recommend_price);
				arg1.setTag(goodItem);
			} else {
				goodItem = (GoodItem) arg1.getTag();
			}
			BLBrandGood da = datas.get(arg0);
			ImageLoaderUtil.Load2(da.getCover(),
					goodItem.item_goodsort_recommend_iv, R.drawable.error_iv2);
			StrUtils.SetTxt(goodItem.item_goodsort_recommend_name,
					da.getTitle());

			StrUtils.SetTxt(
					goodItem.item_goodsort_recommend_price,
					String.format("价格:￥%s",
							StrUtils.SetTextForMony(da.getSell_price())));// );

			return arg1;
		}

		class GoodItem {
			ImageView item_goodsort_recommend_iv;
			TextView item_goodsort_recommend_name;
			TextView item_goodsort_recommend_price;

		}
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

}
