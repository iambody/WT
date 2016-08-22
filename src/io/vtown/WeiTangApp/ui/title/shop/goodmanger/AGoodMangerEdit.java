package io.vtown.WeiTangApp.ui.title.shop.goodmanger;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.BGoodDetail;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.qiniu.NUPLoadUtil;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils;
import io.vtown.WeiTangApp.comment.net.qiniu.NUPLoadUtil.UpResult1;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils.UpResult;
import io.vtown.WeiTangApp.comment.selectpic.util.Bimp;
import io.vtown.WeiTangApp.comment.selectpic.util.ImageItem;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.ImageCycleView;
import io.vtown.WeiTangApp.comment.view.ImageCycleView.ImageCycleViewListener;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.onConfirmClick;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.oncancelClick;
import io.vtown.WeiTangApp.comment.view.listview.HorizontalListView;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.AGoodEditPicSelect;
import io.vtown.WeiTangApp.ui.comment.AVidemplay;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;
import io.vtown.WeiTangApp.ui.comment.recordervido.ARecoderVido;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-22 下午4:40:50
 * @see 商品管理中的编辑商品界面***********发布人才能修改****可以修改轮播图/视频&&&&**********宝贝描述********
 */
public class AGoodMangerEdit extends ATitleBase {

	private BUser mBUser;
	private String GoodId;
	private BGoodDetail datas;

	// 轮播图的布局
	private ImageCycleView alter_goods_banner;

	// 描述分页
	private LinearLayout alter_goods_miaoshu_lay;

	// 视频的布局
	private RelativeLayout alter_goods_vido_lay;
	private ImageView alter_goods_vido_cover;
	private ImageView alter_goods_vido_play;

	// 提交按钮
	private TextView alter_goods_commint;

	// 宝贝轮播图的修改后的展示***
	private HorizontalListView alter_goods_horizon_ls;
	private HorizonAdapter goodHorizonAdapter;
	// 商品描述的修改后的展示*****
	private HorizontalListView alter_goods_miaoshucontent_horizon_ls;
	private HorizonAdapter miaoShuHorizonAdapter;
	// 是否是图片
	private boolean IsPicDetail;
	// 宝贝描述
	private ListView alter_goods_conten_ls;
	// 宝贝描述的ap
	private PicTextAdapter picTextAdapter;
	// 商品轮播图的集合
	private ArrayList<String> GoodsLs = new ArrayList<String>();
	// 宝贝描述的数据集合
	private ArrayList<String> PicMiaoShuLs = new ArrayList<String>();
	// 宽高比
	private List<Float> goodsDescriptFloats = new ArrayList<Float>();
	// 视频的本地图片*******
	private String NewVidoLocation;
	// 视频的web地址************
	private String NewVidoWebPath;
	// 视频的封面的web地址*******
	private String NewVidoPathCover;
	// 是否修改了视频
	private boolean VidoIsAlter = false;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_goodmanger_edit);
		EventBus.getDefault().register(this, "AlterReciver", BMessage.class);

		IBuund();
		mBUser = Spuit.User_Get(BaseContext);
		IView();
		IData(GoodId);
	}

	private void IView() {
		alter_goods_miaoshu_lay = (LinearLayout) findViewById(R.id.alter_goods_miaoshu_lay);

		alter_goods_commint = (TextView) findViewById(R.id.alter_goods_commint);
		alter_goods_commint.setOnClickListener(this);

		alter_goods_conten_ls = (ListView) findViewById(R.id.alter_goods_conten_ls);

		alter_goods_banner = (ImageCycleView) findViewById(R.id.alter_goods_banner);
		// 视频的布局
		alter_goods_vido_lay = (RelativeLayout) findViewById(R.id.alter_goods_vido_lay);
		alter_goods_vido_cover = (ImageView) findViewById(R.id.alter_goods_vido_cover);
		alter_goods_vido_play = (ImageView) findViewById(R.id.alter_goods_vido_play);
		alter_goods_vido_play.setOnClickListener(this);

		alter_goods_conten_ls.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (PicMiaoShuLs.size() == 0)
					return;

				Intent mIntent = new Intent(BaseContext, AphotoPager.class);
				mIntent.putExtra("position", arg2);
				mIntent.putExtra("urls", StrUtils.LsToArray(PicMiaoShuLs));
				PromptManager.SkipActivity(BaseActivity, mIntent);
			}
		});
		// 修改后的的ls展示数据的初始化*******
		alter_goods_horizon_ls = (HorizontalListView) findViewById(R.id.alter_goods_horizon_ls);
		alter_goods_miaoshucontent_horizon_ls = (HorizontalListView) findViewById(R.id.alter_goods_miaoshucontent_horizon_ls);
		goodHorizonAdapter = new HorizonAdapter();
		miaoShuHorizonAdapter = new HorizonAdapter();

		alter_goods_horizon_ls.setAdapter(goodHorizonAdapter);
		alter_goods_miaoshucontent_horizon_ls.setAdapter(miaoShuHorizonAdapter);
	}

	private void IBuund() {
		if (getIntent().getExtras() != null
				&& getIntent().getExtras().containsKey("goodid")) {
			GoodId = getIntent().getStringExtra("goodid");
			return;
		}
		BaseActivity.finish();

	}

	@Override
	protected void InitTile() {
		SetTitleTxt("商品编辑");

	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		switch (Data.getHttpResultTage()) {
		case 0:
			// ==================>"is_edit":1 //0-不可编辑 1-自营发布人，可修改商品详情
			// 2-末级代卖，可修改代卖价格
			if (StrUtils.isEmpty(Data.getHttpResultStr())) {
				// TODO未获取到数据后需要进行提示并且推出
				PromptManager.ShowCustomToast1(BaseContext, Msg);
				BaseActivity.finish();
				return;
			}

			datas = new BGoodDetail();
			try {
				datas = JSON.parseObject(Data.getHttpResultStr(),
						BGoodDetail.class);

			} catch (Exception e) {
				DataError("解析错误", 1);
				return;
			}
			// 获取到数据后进行刷新数据
			FrashView(datas);
			break;

		case 10:
			PromptManager.closeTextLoading3();
			BaseActivity.finish();
			break;
		}

	}

	/**
	 * 刷新view****************只能修改轮播图和宝贝描述***********************
	 */
	private void FrashView(BGoodDetail datas2) {
		SetRightText("编辑");
		right_txt.setOnClickListener(this);

		IsPicDetail = datas.getGoods_info().getRtype().equals("0");
		// 显示隐藏布局
		alter_goods_banner
				.setVisibility(IsPicDetail ? View.VISIBLE : View.GONE);
		alter_goods_vido_lay.setVisibility(!IsPicDetail ? View.VISIBLE
				: View.GONE);
		// 显示隐藏布局
		if (IsPicDetail) {// 图片进行轮播图设置
			GoodsLs = (ArrayList<String>) datas2.getGoods_info().getRoll();
			alter_goods_banner.setImageResources(GoodsLs, GoodsLs,
					mAdCycleViewListener, screenWidth / 2);
		} else {// 视频 进行相应布局
			ImageLoaderUtil.Load2(datas2.getCover(), alter_goods_vido_cover,
					R.drawable.error_iv1);
		}
		if (!StrUtils.isEmpty(datas.getGoods_info().getIntro())) { // 判断是否存在宝贝描述**************
			PicMiaoShuLs = (ArrayList<String>) JSON.parseArray(datas2
					.getGoods_info().getIntro(), String.class);
			picTextAdapter = new PicTextAdapter(
					R.layout.item_good_detail_pic_text_list, PicMiaoShuLs);
			alter_goods_conten_ls.setAdapter(picTextAdapter);
		}
	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {
		@Override
		public void onImageClick(int position, View imageView) {

			if (GoodsLs.size() == 0)
				return;

			Intent mIntent = new Intent(BaseContext, AphotoPager.class);
			mIntent.putExtra("position", position);
			mIntent.putExtra("urls", StrUtils.LsToArray(GoodsLs));
			PromptManager.SkipActivity(BaseActivity, mIntent);
		}

		@Override
		public void displayImage(String imageURL, ImageView imageView) {
			ImageLoaderUtil.Load2(imageURL, imageView, R.drawable.error_iv1);
		}
	};

	@Override
	protected void DataError(String error, int LoadType) {
		PromptManager.closeTextLoading3();
		PromptManager.ShowCustomToast(BaseContext, "编辑失败");
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
		case R.id.alter_goods_vido_play:// 播放视频
			if (CheckNet(BaseContext))
				return;
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					AVidemplay.class).putExtra(AVidemplay.VidoKey, datas
					.getGoods_info().getVid()));
			break;
		case R.id.right_txt:
			if (CheckNet(BaseContext))
				return;
			// EdSelect();s
			SelectPop();
			break;
		case R.id.alter_goods_commint:// 提交按钮
			if (CheckNet(BaseContext))
				return;
			PromptManager.showtextLoading3(BaseContext, getResources()
					.getString(R.string.edgoodloading));
			UpLoadOk();
			// AlterGoods();
			break;
		default:
			break;
		}
	}

	private void SelectPop() {
		ShowCustomDialog("修改内容？", IsPicDetail ? "宝贝图片" : "宝贝视频", "宝贝描述",
				new IDialogResult() {

					@Override
					public void RightResult() {
						// 直接跳转到宝贝描述界面
						PromptManager.SkipActivity(BaseActivity, new Intent(
								BaseContext, AGoodEditPicSelect.class)
								.putStringArrayListExtra("lss", PicMiaoShuLs));
					}

					@Override
					public void LeftResult() {
						// 直接进行
						if (IsPicDetail) {
							PromptManager.SkipActivity(
									BaseActivity,
									new Intent(BaseContext,
											AGoodEditPicSelect.class).putExtra(
											AGoodEditPicSelect.Tage_IsGoods,
											true).putStringArrayListExtra(
											"lss", GoodsLs));
						} else {
							// PromptManager.ShowCustomToast(BaseContext,
							// "跳转到视频录制页面");
							PromptManager
									.SkipActivity(BaseActivity, new Intent(
											BaseActivity, ARecoderVido.class));
						}

					}
				});

	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	// 获取商品详情的通道
	private void IData(String GoodId) {
		PromptManager.showtextLoading(BaseContext,
				getResources()
						.getString(R.string.xlistview_header_hint_loading));
		SetTitleHttpDataLisenter(this);
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("goods_id", GoodId);
		map.put("extend", "1");
		map.put("member_id", mBUser.getId());
		map.put("check_edit", "1");
		map.put("seller_id", mBUser.getSeller_id());
		FBGetHttpData(map, Constants.GoodDetail, Method.GET, 0, LOAD_INITIALIZE);
	}

	/**
	 * 
	 * @param Type
	 *            1标识修改轮播图&&视频///// 2标识修改宝贝描述
	 */
	// private void EdSelect() {
	// final CustomDialog dialog = new CustomDialog(BaseContext,
	// R.style.mystyle, R.layout.dialog_purchase_cancel, 1,
	// IsPicDetail ? "宝贝图片" : "宝贝视频", "宝贝描述");
	// dialog.show();
	// dialog.setTitleText("修改内容？");
	// dialog.HindTitle2();
	// dialog.setCanceledOnTouchOutside(false);
	// dialog.setcancelListener(new oncancelClick() {
	//
	// @Override
	// public void oncancelClick(View v) {
	// dialog.dismiss();
	// // 直接进行
	// if (IsPicDetail) {
	// PromptManager.SkipActivity(
	// BaseActivity,
	// new Intent(BaseContext, AGoodEditPicSelect.class)
	// .putExtra(AGoodEditPicSelect.Tage_IsGoods,
	// true).putStringArrayListExtra(
	// "lss", GoodsLs));
	// } else {
	// // PromptManager.ShowCustomToast(BaseContext, "跳转到视频录制页面");
	// PromptManager.SkipActivity(BaseActivity, new Intent(
	// BaseActivity, ARecoderVido.class));
	// }
	// }
	// });
	//
	// dialog.setConfirmListener(new onConfirmClick() {
	// @Override
	// public void onConfirmCLick(View v) {
	// dialog.dismiss();
	// // 直接跳转到宝贝描述界面
	// PromptManager.SkipActivity(BaseActivity, new Intent(
	// BaseContext, AGoodEditPicSelect.class)
	// .putStringArrayListExtra("lss", PicMiaoShuLs));
	// }
	// });
	// }

	/**
	 * 图文详情的适配器
	 */
	class PicTextAdapter extends BaseAdapter {

		private int ResourseId;
		private LayoutInflater inflater;
		private List<String> datas = new ArrayList<String>();

		public PicTextAdapter(int ResourseId, List<String> datas) {
			super();

			this.ResourseId = ResourseId;
			this.datas = datas;
			this.inflater = LayoutInflater.from(BaseContext);
			alter_goods_miaoshu_lay.setVisibility(View.VISIBLE);
		}

		public List<String> GetResource() {
			return datas;

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
			PicTextDetailItem picTextDetail = null;
			if (convertView == null) {
				picTextDetail = new PicTextDetailItem();
				convertView = inflater.inflate(ResourseId, null);
				picTextDetail.iv_good_detail_pic_text = ViewHolder.get(
						convertView, R.id.iv_good_detail_pic_text);

				convertView.setTag(picTextDetail);

			} else {
				picTextDetail = (PicTextDetailItem) convertView.getTag();
			}

			// ViewGroup.LayoutParams lp = picTextDetail.iv_good_detail_pic_text
			// .getLayoutParams();
			// lp.width = screenWidth;
			// lp.height = LayoutParams.WRAP_CONTENT;
			// picTextDetail.iv_good_detail_pic_text.setLayoutParams(lp);
			// picTextDetail.iv_good_detail_pic_text
			// .setScaleType(ScaleType.FIT_CENTER);
			// picTextDetail.iv_good_detail_pic_text.setMaxWidth(screenWidth);
			// picTextDetail.iv_good_detail_pic_text.setMaxHeight(screenWidth);

			ImageLoaderUtil
					.Load2(datas.get(position),
							picTextDetail.iv_good_detail_pic_text,
							R.drawable.error_iv2);
			return convertView;
		}

	}

	/**
	 * 修改后的适配器
	 */
	private class HorizonAdapter extends BaseAdapter {
		private List<ImageItem> data = new ArrayList<ImageItem>();

		private LayoutInflater layoutInflater;

		public HorizonAdapter() {
			super();

			layoutInflater = LayoutInflater.from(BaseContext);
		}

		/**
		 * 刷新
		 */
		public void FrashView(List<ImageItem> pdata) {
			this.data = pdata;
			this.notifyDataSetChanged();
		}

		/**
		 * 获取数据源
		 */
		public List<ImageItem> GetResource() {
			return data;
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int arg0) {
			return data.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			MyHonreItem myHonreItem = null;
			if (arg1 == null) {
				arg1 = layoutInflater.inflate(R.layout.item_editgoods_horizon,
						null);
				myHonreItem = new MyHonreItem();
				myHonreItem.item_editgoods_horizon_iv = ViewHolder.get(arg1,
						R.id.item_editgoods_horizon_iv);
				arg1.setTag(myHonreItem);
			} else {
				myHonreItem = (MyHonreItem) arg1.getTag();
			}
			ImageItem mImageItem = data.get(arg0);
			if (mImageItem.getBitmap() != null
					|| !StrUtils.isEmpty(mImageItem.getImagePath())) {
				if (mImageItem.getBitmap() == null) {
					mImageItem.setBitmap(StrUtils.getBitmapFromPath(mImageItem
							.getImagePath()));
				}
				myHonreItem.item_editgoods_horizon_iv.setImageBitmap(mImageItem
						.getBitmap());
			} else {
				ImageLoaderUtil.Load2(mImageItem.getThumbnailPath(),
						myHonreItem.item_editgoods_horizon_iv,
						R.drawable.error_iv2);
			}

			return arg1;
		}

		class MyHonreItem {
			ImageView item_editgoods_horizon_iv;
		}

	}

	/**
	 * @author Yihuihua 图文详情的Holder
	 */
	class PicTextDetailItem {
		public ImageView iv_good_detail_pic_text;
	}

	// 获取到数据后进行处理************************************************************
	public void AlterReciver(BMessage bMessage) {

		switch (bMessage.getMessageType()) {
		case BMessage.Tage_Alter_Goods:// 商品轮播图
			List<ImageItem> data = bMessage.getAddImageItems();

			alter_goods_commint.setVisibility(View.VISIBLE);
			alter_goods_banner.setVisibility(View.GONE);
			alter_goods_horizon_ls.setVisibility(View.VISIBLE);
			goodHorizonAdapter.FrashView(data);
			IsPicChange_Goods = true;
			break;
		case BMessage.Tage_Alter_Miaoshu:// 商品描述
			List<ImageItem> datas = bMessage.getAddImageItems();

			alter_goods_commint.setVisibility(View.VISIBLE);
			alter_goods_conten_ls.setVisibility(View.GONE);
			alter_goods_miaoshucontent_horizon_ls.setVisibility(View.VISIBLE);
			miaoShuHorizonAdapter.FrashView(datas);

			IsPicChange_Miaoshu = true;
			break;
		case BMessage.Tage_Alter_Address:// 修改地址

			break;
		case 290:
			alter_goods_commint.setVisibility(View.VISIBLE);
			NewVidoLocation = bMessage.getReCordVidoPath();
			ShowVidoView(true);
			// NewVidoPathCover
			VidoIsAlter = true;
			UpVidoPathCover(true);
			break;
		// case 291://如果录制过一次 *第二次录制********
		// NewVidoLocation=null;
		// NewVidoWebPath=null;
		// NewVidoPathCover=null;
		//
		// break;
		default:
			break;
		}

	}

	private void ShowVidoView(boolean b) {
		alter_goods_vido_cover
				.setImageBitmap(createVideoThumbnail(NewVidoLocation));

	}

	int CountNumber_Miaoshu = 0;
	int NeedUpNumber_Miaoshu = 0;
	List<ImageItem> MiaoShusUpLs = new ArrayList<ImageItem>();
	boolean IsPicChange_Miaoshu = false;// 没有变化

	// 先检测上传宝贝描述的 在检测上传宝贝图片||视频的
	private void UpLoadOk() {
		MiaoShusUpLs = miaoShuHorizonAdapter.GetResource();
		if (MiaoShusUpLs.size() == 0) {// 宝贝描述没有变化值直接上传宝贝的图片||视频
			IUpGoods(MiaoShusUpLs);
			return;
		}

		// 计算需要的上传的个数
		NeedUpNumber_Miaoshu = 0;
		for (int i = 0; i < MiaoShusUpLs.size(); i++) {
			if (MiaoShusUpLs.get(i).getBitmap() != null)
				NeedUpNumber_Miaoshu = NeedUpNumber_Miaoshu + 1;
		}
		if (NeedUpNumber_Miaoshu == 0) {
			IUpGoods(MiaoShusUpLs);
			return;
		}
		// 开始上传宝贝描述*********
		CountNumber_Miaoshu = 0;

		for (int i = 0; i < MiaoShusUpLs.size(); i++) {
			final int postion = i;
			if (MiaoShusUpLs.get(i).getBitmap() == null)
				continue;
			// 存在bitmap时候进行上传*****************************
			NUpLoadUtils dLoadUtils = new NUpLoadUtils(BaseContext,
					Bitmap2Bytes(MiaoShusUpLs.get(i).getBitmap()),
					StrUtils.UploadQNName("photo"));
			dLoadUtils.SetUpResult(new UpResult() {
				@Override
				public void Progress(String arg0, double arg1) {
				}

				@Override
				public void Onerror() {
					MiaoShusUpLs.get(postion).setThumbnailPath("");
					CountNumber_Miaoshu = CountNumber_Miaoshu + 1;

					if (CountNumber_Miaoshu == NeedUpNumber_Miaoshu) {
						// 上传描述完毕
						IUpGoods(MiaoShusUpLs);
					}
				}

				@Override
				public void Complete(String HostUrl, String Url) {
					MiaoShusUpLs.get(postion).setThumbnailPath(HostUrl);
					CountNumber_Miaoshu = CountNumber_Miaoshu + 1;

					if (CountNumber_Miaoshu == NeedUpNumber_Miaoshu) {
						// 上传描述完毕
						IUpGoods(MiaoShusUpLs);
					}
				}
			});
			dLoadUtils.UpLoad();
		}

	}

	int CountNumber_Goods = 0;
	int NeedUpNumber_Goods = 0;
	List<ImageItem> GoodsUpLs = new ArrayList<ImageItem>();
	boolean IsPicChange_Goods = false;// 没有变化

	/**
	 * 上传完宝贝描述后 开始上传视频||宝贝图片
	 */
	private void IUpGoods(List<ImageItem> DescLSs) {
		if (IsPicDetail) {
			// 图片进行处理****************************
			GoodsUpLs = goodHorizonAdapter.GetResource();
			if (GoodsUpLs.size() == 0) {// 宝贝描述没有变化值直接上传宝贝的图片||视频
				AlterGoods();
				return;
			}

			// 计算需要的上传的个数
			NeedUpNumber_Goods = 0;
			for (int i = 0; i < GoodsUpLs.size(); i++) {
				if (GoodsUpLs.get(i).getBitmap() != null)
					NeedUpNumber_Goods = NeedUpNumber_Goods + 1;
			}
			if (NeedUpNumber_Goods == 0) {//不需要上传
				AlterGoods();
				return;
			}

			// 开始上传宝贝描述*********
			CountNumber_Goods = 0;
			for (int i = 0; i < GoodsUpLs.size(); i++) {
				final int postion = i;
				if (GoodsUpLs.get(i).getBitmap() == null)
					continue;
				// 存在bitmap时候进行上传*****************************
				NUpLoadUtils dLoadUtils = new NUpLoadUtils(BaseContext,
						Bitmap2Bytes(GoodsUpLs.get(i).getBitmap()),
						StrUtils.UploadQNName("photo"));
				dLoadUtils.SetUpResult(new UpResult() {
					@Override
					public void Progress(String arg0, double arg1) {
					}

					@Override
					public void Onerror() {
						GoodsUpLs.get(postion).setThumbnailPath("");
						CountNumber_Goods = CountNumber_Goods + 1;

						if (CountNumber_Goods == NeedUpNumber_Goods) {
							// 上传描述完毕
							AlterGoods();
						}
					}

					@Override
					public void Complete(String HostUrl, String Url) {
						GoodsUpLs.get(postion).setThumbnailPath(HostUrl);
						CountNumber_Goods = CountNumber_Goods + 1;

						if (CountNumber_Goods == NeedUpNumber_Goods) {
							// 上传描述完毕
							AlterGoods();
						}
					}
				});
				dLoadUtils.UpLoad();
			}

			// 图片进行处理****************************
		} else {
			// 视频进行处理************************
			// 视频进行处理************************
			UpVidoPathCover(false);
		}
	}

	// ******************************视频处理（封面和视频）*************************
	/**
	 * 上传封面 bitmap s是不是后台偷偷下载 是的话 下载完毕 不需要调用接口
	 */
	private void UpVidoPathCover(final boolean IsFristLoadUp) {

		if (!VidoIsAlter) {// 如果没有修改视频
			AlterGoods();
			return;
		}

		// 判断下商品封面是否已经上传成功*******
		if (!StrUtils.isEmpty(NewVidoPathCover)) {// 已经上传成功直接进行上传视频
			UpVido(IsFristLoadUp);
			return;

		}
		// 没有上传过封面 先==》上传封面===》上传视频
		NUpLoadUtils dLoadUtils = new NUpLoadUtils(BaseContext,
				StrUtils.Bitmap2Bytes(createVideoThumbnail(NewVidoLocation)),
				StrUtils.UploadQNName("photo"));

		dLoadUtils.SetUpResult(new UpResult() {

			@Override
			public void Progress(String arg0, double arg1) {

			}

			@Override
			public void Onerror() {
				PromptManager.closeTextLoading3();
				PromptManager.ShowCustomToast(BaseContext, "编辑视频失败请检查网络");
			}

			@Override
			public void Complete(String HostUrl, String Url) {
				NewVidoPathCover = HostUrl;
				// 上传本地的视频***************
				// if (IsFristLoadUp) {//是第一次偷偷下载
				// return;

				// 点击确定时候
				UpVido(IsFristLoadUp);
			}
		});
		dLoadUtils.UpLoad();

	}

	/**
	 * 上传视频 是否是第一次后台偷偷下载视频 ==》是的话 不用调接口 不是的话默认是直接调用接口的
	 * 
	 * @param VidoPath
	 */
	private void UpVido(final boolean isFristUpLoad) {
		// 判断是否图片已经上传************
		if (StrUtils.isEmpty(NewVidoPathCover)) {
			UpVidoPathCover(isFristUpLoad);
			// 处理
			return;
		}
		// 判断视频是否已经上传*************

		if (!StrUtils.isEmpty(NewVidoWebPath)) {// 视频已经上传过了 需要进行直接调接口
			if (isFristUpLoad) {// 是调用接口的 需要调用接口******************
				return;
			} else {// 调用接口**********************
				AlterGoods();
				return;
			}
		}
		NUPLoadUtil dLoadUtils = new NUPLoadUtil(BaseContext, new File(
				NewVidoLocation), StrUtils.UploadVido("vid"));

		dLoadUtils.SetUpResult1(new UpResult1() {

			@Override
			public void Progress(String arg0, double arg1) {

			}

			@Override
			public void Onerror() {
				PromptManager.closeTextLoading3();
				PromptManager.ShowCustomToast(BaseContext, "编辑视频失败请检查网络");
			}

			@Override
			public void Complete(String HostUrl, String Url) {
				// TODO调接口进行处理***********
				NewVidoWebPath = HostUrl;
				if (isFristUpLoad) {

					return;
				} else {

					// 是调用接口的 需要调用接口******************
					AlterGoods();
				}
			}
		});
		dLoadUtils.UpLoad();

	}

	// goods_id int Y 商品id
	// intro char Y 商品详情 多个图片的json格式
	// deliver char N 发货地
	// roll char Y 轮播图
	// vid char Y 小视频地址
	// seller_id int Y 发布人seller_id
	// rtype int Y 轮播图内容类型 0-图片 1-小视频

	// //上传完毕后 需要进行接口连接进行处理
	private void AlterGoods() {

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("goods_id", GoodId);

		// 商品详情 多个图片的json格式***********************************
		if (IsPicChange_Miaoshu) {// 发生变化
			map.put("intro", JSON.toJSONString(PicsChangeStr(MiaoShusUpLs)));
			map.put("ratio", GetRate(PicsChangeStr(MiaoShusUpLs).size()));
		} else {// 没变化
			map.put("intro", datas.getGoods_info().getIntro());
			map.put("ratio", datas.getGoods_info().getIntro());
		}
		// 轮播图**************************************
		if (IsPicDetail) {
			if (IsPicChange_Goods) {// 发生变化
				map.put("roll", JSON.toJSONString(PicsChangeStr(GoodsUpLs)));// 轮播图
			} else {//

				map.put("roll", JSON.toJSONString(GoodsLs));// 轮播图
			}

			map.put("vid", "");// 小视频地址
			map.put("cover", "");// 小视频封面
		} else {
			map.put("roll", "");// 轮播图

			map.put("vid", !VidoIsAlter ? datas.getGoods_info().getVid()
					: NewVidoWebPath);// 小视频地址
			map.put("cover", !VidoIsAlter ? datas.getCover() : NewVidoPathCover);// 小视频封面
		}

		// ***********************************************************************************************************************************************************************************************************
		// map.put("vid", IsPicDetail ? "" :
		// "http://fs.v-town.cc/vid_964411469071619899.mp4");// 小视频地址
		// map.put("cover", IsPicDetail ? "" :
		// "http://fs.v-town.cc/photo_623971469071596044.jpg");// 小视频
		

		map.put("seller_id", mBUser.getSeller_id());//
		map.put("rtype", IsPicDetail ? "0" : "1");// 轮播图内容类型 0-图片 1-小视频
		FBGetHttpData(map, Constants.GoodAlter, Method.PUT, 10, LOAD_INITIALIZE);
	}

	private List<String> PicsChangeStr(List<ImageItem> dass) {
		List<String> data = new ArrayList<String>();
		for (int i = 0; i < dass.size(); i++) {
			if (!StrUtils.isEmpty(dass.get(i).getThumbnailPath())) {
				data.add(dass.get(i).getThumbnailPath());
			}
		}
		return data;

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		for (int i = 0; i < goodHorizonAdapter.GetResource().size(); i++) {
			CacheUtil.BitMapRecycle(goodHorizonAdapter.GetResource().get(i)
					.getBitmap());

		}
		for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
			CacheUtil.BitMapRecycle(Bimp.tempSelectBitmap.get(i).getBitmap());
		}
		for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
			CacheUtil.BitMapRecycle(Bimp.tempSelectBitmap.get(i).getBitmap());
		}
		Bimp.tempSelectBitmap = new ArrayList<ImageItem>();
		Bimp.max = 0;

	}

	private String GetRate(int MysIZE) {
		goodsDescriptFloats = new ArrayList<Float>();
		for (int i = 0; i < MysIZE; i++) {
			goodsDescriptFloats.add(0.5f);
		}

		return StrUtils.GetStrs(goodsDescriptFloats);
	}

}
