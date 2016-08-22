package io.vtown.WeiTangApp.ui.title.shop.addgood;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.multiphoto.util.ImageDisplayer;
import io.vtown.WeiTangApp.comment.net.qiniu.NUPLoadUtil;
import io.vtown.WeiTangApp.comment.net.qiniu.NUPLoadUtil.UpResult1;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils.UpResult;
import io.vtown.WeiTangApp.comment.selectpic.util.Bimp;
import io.vtown.WeiTangApp.comment.selectpic.util.ImageItem;
import io.vtown.WeiTangApp.comment.util.DimensionPixelUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.comment.view.listview.HorizontalListView;
import io.vtown.WeiTangApp.comment.view.pop.PAddGoosSort;
import io.vtown.WeiTangApp.comment.view.pop.PAddSelect;
import io.vtown.WeiTangApp.comment.view.pop.PAddGoosSort.OnPopupListener;
import io.vtown.WeiTangApp.comment.view.pop.PAddSelect.AddSelectInterface;
import io.vtown.WeiTangApp.event.interf.IBottomDialogResult;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;
import io.vtown.WeiTangApp.ui.comment.AEditInfBack;
import io.vtown.WeiTangApp.ui.comment.ASelectPic;
import io.vtown.WeiTangApp.ui.comment.AVidemplay;
import io.vtown.WeiTangApp.ui.comment.recordervido.ARecoderVido;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-11 下午2:42:56
 * @author 商铺里面的添加宝贝
 */
public class AAddGood extends ATitleBase {

	private View AddbaseView;
	/**
	 * 返回图片列表后的宝贝图片展示
	 */
	private CompleteGridView iv_add_good_goods_gridview;
	/**
	 * 返回视频后的视频展示
	 */
	private RelativeLayout addgood_vido_lay;
	private ImageView addgood_vido_cover;
	private ImageView addgood_vido_play;

	/**
	 * 添加规格
	 */
	private LinearLayout ll_add_rule;

	/**
	 * 商品标题输入框最大值
	 */

	private ImageView iv_add_good_addhead;

	/**
	 * b保存已经生成的规格Ap
	 */
	private AddGoodRuleAdapter1 addGoodRuleAdapter;

	/**
	 * 规格列表
	 */
	private CompleteListView good_rule_list_item;
	/**
	 * 规格列表的Ap
	 */
	private ShowGoodsPicAp goodsPicAp;

	/**
	 * 收货地址
	 */
	private String send_out_address = "";

	// 宝贝描述
	private View addgoods_description;
	// 发货地
	private View addgoods_send_out_address;

	/**
	 * 运费
	 */
	private EditText et_add_good_post_price;

	// 保存当前最新的分类数据
	private List<BLComment> SortResourcesList = new ArrayList<BLComment>();

	private List<BLComment> ResourcesList = new ArrayList<BLComment>();

	/**
	 * 图片类型
	 */
	private static final int BITMAP_TYPE = 100;
	/**
	 * 视频类型
	 */
	private static final int VIDEO_TYPE = 101;

	/**
	 * 预览
	 */
	private Button btn_add_good_preview;

	/**
	 * 直接上架
	 */
	private Button btn_add_good_direct_sell;
	/**
	 * 添加title
	 */
	private View add_good_title;

	/**
	 * 类目
	 */
	private View category;
	/**
	 * 当前的类目规格保存数据
	 */
	private BLComment CurrentSortData = null;

	// 文件路径
	private String VidoNewPath = "";

	/**
	 * 类目显示
	 */
	private TextView Sort_Name_content;
	/*
	 * title显示
	 */
	private TextView Tv_Goods_Title;

	/**
	 * 商品描述
	 */
	private TextView tv_addgoods_description;
	/**
	 * 重新录制
	 */
	// private TextView addgood_vido_record;
	/**
	 * 商品详情的展示
	 */
	private HorizontalListView addgood_desc_horizon_ls;
	private ShowGoodsPicAp goodsDescAp;
	/**
	 * 发货地址
	 */
	private TextView tv_address_display;

	/**
	 * 添加宝贝描述时候（图片的列表Bean）需要保存 的数据
	 */
	private List<ImageItem> GoodsDescriptionLs = new ArrayList<ImageItem>();
	// 宽高比
	private List<Float> goodsDescriptFloats = new ArrayList<Float>();
	/**
	 * 是否当前是照片 false标识是当前的展示是视频
	 */
	private boolean IsCurrentPic = true;
	/**
	 * 用户信息
	 */
	private BUser user_Get;

	/**
	 * 上传描述图片时候需要 上传图片的个数 仅仅是一个计数器功能
	 */
	private int AllMyDescNeedUpNumber = 0;
	/**
	 * 上传宝贝图片时候需要 上传图片的个数 仅仅是一个计数器功能
	 */
	private int AllGoodNeedUpNumber = 0;

	/**
	 * 上传视频时候需要保存的上传后获取的视频的封面
	 */
	private String HavaUpVidoCover = "";

	/**
	 * 上传视频是需要保存上传后获取的视频地址
	 */
	private String HavaUpVido = "";

	/**
	 * 获取是否已经编辑了图片或者视频
	 */
	private boolean IsVidoImageEditing = false;

	@Override
	protected void InItBaseView() {
		AddbaseView = LayoutInflater.from(BaseContext).inflate(
				R.layout.activity_add_good, null);
		setContentView(AddbaseView);
		user_Get = Spuit.User_Get(BaseContext);

		// 注册事件
		EventBus.getDefault()
				.register(this, "OnGoodPicMessage", BMessage.class);
		IView();
	}

	private void IView() {
		// add_good_title
		add_good_title = findViewById(R.id.add_good_title);
		((TextView) add_good_title.findViewById(R.id.comment_txtarrow_title))
				.setText(R.string.add_good_title);
		Tv_Goods_Title = ((TextView) add_good_title
				.findViewById(R.id.comment_txtarrow_content));
		add_good_title.setOnClickListener(this);
		// 九宫格展示已经选择的图片
		iv_add_good_goods_gridview = (CompleteGridView) findViewById(R.id.iv_add_good_goods_gridview);
		goodsPicAp = new ShowGoodsPicAp();
		iv_add_good_goods_gridview.setAdapter(goodsPicAp);
		// 录视频后需要展示视频的view
		addgood_vido_lay = (RelativeLayout) findViewById(R.id.addgood_vido_lay);
		addgood_vido_cover = (ImageView) findViewById(R.id.addgood_vido_cover);
		addgood_vido_play = (ImageView) findViewById(R.id.addgood_vido_play);
		addgood_vido_play.setOnClickListener(this);
		// addgood_vido_record = (TextView)
		// findViewById(R.id.addgood_vido_record);
		// addgood_vido_record.setOnClickListener(this);
		// TODO图片视频上传是试入口
		iv_add_good_addhead = (ImageView) findViewById(R.id.iv_add_good_addhead);
		iv_add_good_addhead.setOnClickListener(this);
		et_add_good_post_price = (EditText) findViewById(R.id.et_add_good_post_price);

		btn_add_good_preview = (Button) findViewById(R.id.btn_add_good_preview);
		btn_add_good_direct_sell = (Button) findViewById(R.id.btn_add_good_direct_sell);
		ll_add_rule = (LinearLayout) findViewById(R.id.ll_add_rule);

		// 添加类目
		category = findViewById(R.id.category);
		((TextView) category.findViewById(R.id.comment_txtarrow_title))
				.setText(R.string.add_good_category);
		Sort_Name_content = (TextView) category
				.findViewById(R.id.comment_txtarrow_content);
		// 宝贝描述
		addgoods_description = findViewById(R.id.addgoods_description);
		((TextView) addgoods_description
				.findViewById(R.id.comment_txtarrow_title))
				.setText(getResources().getString(R.string.add_good_miaoshuf));
		tv_addgoods_description = (TextView) addgoods_description
				.findViewById(R.id.comment_txtarrow_content);
		StrUtils.SetTxt(tv_addgoods_description,
				getResources().getString(R.string.add_good_inf));
		addgoods_description.setOnClickListener(this);
		// 宝贝描述的展示
		addgood_desc_horizon_ls = (HorizontalListView) findViewById(R.id.addgood_desc_horizon_ls);
		LinearLayout.LayoutParams ps = new LinearLayout.LayoutParams(
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
				(screenWidth - DimensionPixelUtil.dip2px(BaseContext, 30)) / 3);
		addgood_desc_horizon_ls.setLayoutParams(ps);
		goodsDescAp = new ShowGoodsPicAp();
		addgood_desc_horizon_ls.setAdapter(goodsDescAp);
		addgood_desc_horizon_ls.setVisibility(View.GONE);
		// 发货地址
		addgoods_send_out_address = findViewById(R.id.addgoods_send_out_address);

		((TextView) addgoods_send_out_address
				.findViewById(R.id.comment_txtarrow_title))
				.setText(getResources().getString(R.string.add_good_fahuodi));
		tv_address_display = (TextView) addgoods_send_out_address
				.findViewById(R.id.comment_txtarrow_content);
		tv_address_display.setHint(getResources().getString(
				R.string.add_address));
		((ImageView) addgoods_send_out_address
				.findViewById(R.id.iv_comment_right_arrow))
				.setVisibility(View.GONE);

		// 发货地***********
		tv_address_display.setSingleLine(true);
		tv_address_display.setEllipsize(TruncateAt.END);
		addgoods_send_out_address.setOnClickListener(this);

		// 添加类目
		category.setOnClickListener(this);
		// 直接上架
		btn_add_good_direct_sell.setOnClickListener(this);
		// 预览
		btn_add_good_preview.setOnClickListener(this);
		// 添加规格
		ll_add_rule.setOnClickListener(this);
		// 视频test
		// iv_video.setOnClickListener(this);
		// // 图片test
		// iv_head.setOnClickListener(this);
		// 初始化规格的列表
		IList();
		// 初始化添加宝贝九宫格图片的数据
		iv_add_good_goods_gridview
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// if (arg2 != goodsPicAp.getCount())
						Bimp.tempSelectBitmap = (ArrayList<ImageItem>) goodsPicAp
								.GetPicDatas();
						Bimp.max = goodsPicAp.GetPicDatas().size();

						PromptManager.SkipActivity(BaseActivity, new Intent(
								BaseActivity, ASelectPic.class).putExtra(
								"pictype", BMessage.Tage_AddGoodPic));
					}
				});

		addgood_desc_horizon_ls
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						Bimp.tempSelectBitmap = (ArrayList<ImageItem>) goodsDescAp
								.GetPicDatas();
						Bimp.max = goodsDescAp.GetPicDatas().size();

						PromptManager.SkipActivity(BaseActivity, new Intent(
								BaseActivity, ASelectPic.class).putExtra(
								"pictype", BMessage.Tage_AddGoodDescPic));

					}
				});
	}

	private void IList() {
		good_rule_list_item = (CompleteListView) findViewById(R.id.good_rule_list_item);
		addGoodRuleAdapter = new AddGoodRuleAdapter1(
				R.layout.item_add_good_rule_lv);
		good_rule_list_item.setAdapter(addGoodRuleAdapter);
		good_rule_list_item
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						DeletItemRule(arg2);
						return true;
					}
				});
	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.shop_oder_add_good));
		// 为视频设置的
		right_txt = (TextView) findViewById(R.id.right_txt);
		right_txt.setOnClickListener(this);
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {

		if (1 == Data.getHttpResultTage()) {// 发布宝贝
			PromptManager.ShowCustomToast(BaseContext, "添加成功");
			PromptManager.closeTextLoading3();
			BaseActivity.finish();

		}
	}

	@Override
	protected void DataError(String error, int LoadTyp) {
		PromptManager.closeTextLoading3();
		PromptManager.ShowCustomToast(BaseContext, error);
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

	/**
	 * 跳转到选择图片的操作
	 */
	private void GoToSelectPic() {
		Intent intent = new Intent(BaseContext, ASelectPic.class).putExtra(
				"pictype", BMessage.Tage_AddGoodPic);
		PromptManager.SkipActivity(BaseActivity, intent);

		ArrayList<ImageItem> ss = (ArrayList<ImageItem>) goodsPicAp
				.GetPicDatas();
		Bimp.tempSelectBitmap = ss;
		Bimp.max = goodsPicAp.GetPicDatas().size();
	}

	/**
	 * 跳转到选择视频的操作
	 */
	private void GoToSelectVid() {
		PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
				ARecoderVido.class));
	}

	@Override
	protected void MyClick(View V) {
		switch (V.getId()) {

		case R.id.add_good_title:// 选择宝贝的标题
			// if (IsCurrentPic) {// 如果是添加图片标识时候需要跳转添加图片的界面
			Intent mIntent = new Intent(BaseActivity, AEditInfBack.class)
					.putExtra(AEditInfBack.Tag_key,
							AEditInfBack.Tag_AddGoods_Title).putExtra("title",
							StrUtils.TextStrGet(Tv_Goods_Title));
			PromptManager.SkipResultActivity(BaseActivity, mIntent,
					AEditInfBack.Tag_AddGoods_Title);
			// } else {// 添加视频的标识
			//
			// }
			// new AddGoodPopupWindows(BaseContext, AddbaseView);
			break;

		case R.id.iv_add_good_addhead:// 选择宝贝图片或者视频

			new AddGoodPopupWindows(AddbaseView);

			break;
		case R.id.ll_add_rule:
			if (null == CurrentSortData) {// 表示还没有选择分类的数据
				PromptManager.ShowMyToast(BaseContext, "请先选择类目");
				return;
			}
			ShowSortPop(AddbaseView);
			break;
		case R.id.btn_add_good_preview:// 商品预览的跳转
			getGoodInfo();
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					AGoodPreview.class));
			break;
		case R.id.btn_add_good_direct_sell:// 点击直接上架按钮
			if (CheckGoodsInf())
				UpPicVido();
			// AddMyGoods();
			break;
		case R.id.category:// 宝贝添加规格
			if (CurrentSortData == null)
				PromptManager.SkipResultActivity(BaseActivity, new Intent(
						BaseActivity, AAddGoodCategory.class), 101);
			else
				ShowCustomDialog(getResources().getString(R.string.changesort),
						getResources().getString(R.string.cancle),
						getResources().getString(R.string.sure),
						new IDialogResult() {
							@Override
							public void RightResult() {
								PromptManager.SkipResultActivity(BaseActivity,
										new Intent(BaseActivity,
												AAddGoodCategory.class), 101);
							}

							@Override
							public void LeftResult() {

							}
						});

			break;
		case R.id.addgoods_description:// 宝贝描述
			// Bimp.tempSelectBitmap=new ArrayList<ImageItem>();

			Intent intent22 = new Intent(BaseContext, ASelectPic.class)
					.putExtra("pictype", BMessage.Tage_AddGoodDescPic)
					.putExtra("descfrist", true);
			PromptManager.SkipActivity(BaseActivity, intent22);
			break;

		case R.id.addgoods_send_out_address:// 商品选择发货地址的跳转
			hintKbTwo();
			Address();
			break;
		case R.id.addgood_vido_play:
			PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
					AVidemplay.class).putExtra(AVidemplay.VidoKey, VidoNewPath)
					.putExtra("issd", true));
			break;
		case R.id.right_txt:// 照片
			// GoToSelectVid();
			ShowCustomDialog(getResources().getString(R.string.chongzhi_vido),
					getResources().getString(R.string.cancle), getResources()
							.getString(R.string.sure), new IDialogResult() {

						@Override
						public void RightResult() {
							GoToSelectVid();
						}

						@Override
						public void LeftResult() {
						}
					});

			break;
		// case R.id.addgood_vido_record:// 重新录制*******
		//
		// GoToSelectVid();
		// break;
		default:
			break;
		}
	}

	/**
	 * 选择地区
	 */
	private void Address() {
		final PAddSelect m = new PAddSelect(BaseContext,true);

		m.GetAddressResult(new AddSelectInterface() {

			@Override
			public void GetAddResult(String ProviceName, String CityName,
					String DistrictName, String ZipCode) {
				send_out_address = ProviceName + CityName + DistrictName;
				send_out_address=ProviceName;
				StrUtils.SetTxt(tv_address_display, send_out_address);
				m.dismiss();
			}
		});
		m.showAtLocation(AddbaseView, Gravity.BOTTOM, 0, 0);
	}

	// 点击商家时候需要判断添加宝贝需要的所有数据参数是否完整
	private boolean CheckGoodsInf() {

		// 判断是否有标题title
		if (StrUtils.TextIsEmPty(Tv_Goods_Title)) {
			PromptManager.ShowCustomToast(BaseContext, "亲忘记添加宝贝标题了");
			return false;
		}
		// 规格列表是否为空
		if (0 == addGoodRuleAdapter.GetShortDatas().size()) {
			PromptManager.ShowCustomToast(BaseContext, "亲忘记添加宝贝规格了");
			return false;
		}
		// 是否填写运费
		if (StrUtils.EditTextIsEmPty(et_add_good_post_price)) {
			PromptManager.ShowCustomToast(BaseContext, "亲忘记添加宝贝运费了");
			return false;
		}
		// 是否填写地址
		if (StrUtils.TextIsEmPty(tv_address_display)) {
			PromptManager.ShowCustomToast(BaseContext, "亲忘记添加发货地址了");
			return false;
		}
		// 类别的数据是否为空
		if (null == CurrentSortData
				|| StrUtils.isEmpty(CurrentSortData.getId())) {
			PromptManager.ShowCustomToast(BaseContext, "亲规格失效重新选择规格");
			return false;
		}
		// 此处现在默认是只是传递图片 以后需要判断是视频
		if (IsCurrentPic) {// 传递图片的界面******************
			if (null == goodsPicAp.GetPicDatas()
					|| goodsPicAp.GetPicDatas().size() == 0) {
				PromptManager.ShowCustomToast(BaseContext, "亲请先选择宝贝封面");
				return false;

			}
		} else {// 传递视频的界面******************
			if (StrUtils.isEmpty(VidoNewPath)) {
				PromptManager.ShowCustomToast(BaseContext, "亲请先选择宝贝视频");
				return false;
			}

		}

		if (null == addGoodRuleAdapter.GetShortDatas()
				|| addGoodRuleAdapter.GetShortDatas().size() == 0) {
			PromptManager.ShowCustomToast(BaseContext, "亲请先选择宝贝规格");
			return false;
		}

		if (null == goodsDescAp.GetPicDatas()
				|| goodsDescAp.GetPicDatas().size() == 0) {
			PromptManager.ShowCustomToast(BaseContext, "亲请先选择商品描述图片");
			return false;
		}
		return true;
	}

	/**
	 * 获取发布宝贝时候需要传递的参数
	 * 
	 * @return
	 */
	private HashMap<String, String> GetUpData() {
		HashMap<String, String> mHashMap = new HashMap<String, String>();
		mHashMap.put("title", StrUtils.TextStrGet(Tv_Goods_Title));// 标题
		mHashMap.put("category_id", CurrentSortData.getId());// 分类=》外层的id******************
		mHashMap.put("postage", String.valueOf(Integer.valueOf(StrUtils
				.TextStrGet(et_add_good_post_price)) * 100));// 邮费******************

		if (IsCurrentPic) {// 传递图片
			mHashMap.put("cover", goodsPicAp.GetPicDatas().get(0).getThumbnailPath());// 视频或者图片地址
		} else {// 传递视频
			mHashMap.put("cover", HavaUpVidoCover);// HavaUpVidoCover===&&&==HavaUpVido

		}

		if (null != GoodsDescriptionLs && GoodsDescriptionLs.size() != 0) {// 添加商品描述
			JSONArray jsonArray = StrUtils.ListImages(GoodsDescriptionLs);
			mHashMap.put("intro", jsonArray.toString());// 商品简介
														// 文字或者图片******************
		} else {// 未添加商品描述
			mHashMap.put("intro", "");// 商品简介 文字或者图片******************
		}
		// goodsDescriptFloats
		if (null != goodsDescriptFloats && goodsDescriptFloats.size() != 0) {

			mHashMap.put("ratio", StrUtils.GetStrs(goodsDescriptFloats));
		} else {
			mHashMap.put("ratio", "");
		}

		mHashMap.put("deliver", StrUtils.TextStrGet(tv_address_display));// 发货地
		if (IsCurrentPic) {// 传递的是图片
			JSONArray jsonArray = StrUtils.ListImages(goodsPicAp.GetPicDatas());
			mHashMap.put("roll", jsonArray.toString());// 轮播图******************
		} else {// 传递的是视频
			mHashMap.put("roll", "");// 轮播图******************
		}
		if (IsCurrentPic) {// 传递的是图片
			mHashMap.put("vid", "");// 小视频******************
		} else {// 传递的是视频
			mHashMap.put("vid", HavaUpVido);// 小视频******************
		}
		String SortStr = StrUtils.BeansToJson(
				addGoodRuleAdapter.GetShortDatas()).toString();
		mHashMap.put("attrs", SortStr);// 商品规格信息******************
		mHashMap.put("seller_id", user_Get.getSeller_id());// 代理商id******************

		mHashMap.put("rtype", (IsCurrentPic ? 0 : 1) + "");// 轮播图内容类型 0-图片
															// 1-小视频******************
															// ratio!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
		mHashMap.put("itype", "0");// 内容类型 0-图片 1-文字******************
		mHashMap.put("sell_price",
				StrUtils.AddGoodPriceGet(addGoodRuleAdapter.GetShortDatas(), 1));// 售价**************
		mHashMap.put("max_price",
				StrUtils.AddGoodPriceGet(addGoodRuleAdapter.GetShortDatas(), 2));// 最大价格******************
		return mHashMap;
	}

	/**
	 * 发布宝贝
	 */
	private void AddMyGoods() {

		SetTitleHttpDataLisenter(this);
		FBGetHttpData(GetUpData(), Constants.Shop_AddGoods, Method.POST, 1,
				LOAD_INITIALIZE);

	}

	/**
	 * 获取所有的商品信息
	 */
	private void getGoodInfo() {
		BDComment good_info = new BDComment();
		String post_price = et_add_good_post_price.getText().toString().trim();
		if (StrUtils.isEmpty(post_price)) {
			PromptManager.ShowMyToast(BaseContext,
					getResources().getString(R.string.shuruyunfei));
			return;
		}

	}

	// 填写规格的的POp输入框**************************************************
	private void ShowSortPop(View addbaseView2) {
		PAddGoosSort mAddGoosSort = new PAddGoosSort(BaseContext,
				CurrentSortData);
		mAddGoosSort.showAtLocation(addbaseView2, Gravity.CENTER, 0, 0);
		mAddGoosSort.SetOnPopupListener(new OnPopupListener() {

			@Override
			public void sendData(BLComment ResouceData) {
				SortResourcesList.add(ResouceData);
				addGoodRuleAdapter.AddFrashData(SortResourcesList);

			}
		});
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	private long calculateLength(CharSequence c) {
		double len = 0;
		for (int i = 0; i < c.length(); i++) {
			int temp = (int) c.charAt(i);
			if (temp > 0 && temp < 127) {
				len += 0.5;
			} else {
				len++;
			}
		}
		return Math.round(len);
	}

	/**
	 * 返回结果*************************************************
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// 获取类别的数据源
		if (101 == requestCode && RESULT_OK == resultCode) {// 类别的数据源
			BLComment category_data = (BLComment) data
					.getSerializableExtra("category_data");
			StrUtils.SetTxt(Sort_Name_content, category_data.getCate_name());

			// 如果换规格了 那就
			if (CurrentSortData != null
					&& !category_data.getAdd_good_id().equals(
							CurrentSortData.getAdd_good_id())) {
				// PromptManager.ShowCustomToast(BaseContext, "您已更换商品规格");
				SortResourcesList = new ArrayList<BLComment>();
				addGoodRuleAdapter.RemoveDatas();

			}

			CurrentSortData = category_data;
			ll_add_rule.setVisibility(View.VISIBLE);
			// List<BLComment> category_datas = new ArrayList<BLComment>();
			// category_datas.add(category_data);
			// TODO设置一个全局变量去保存获取当前最新的类别的数据源！！！！！
			// good_rule_list_item.setAdapter(addGoodRuleAdapter);

		}

		if (103 == requestCode && RESULT_OK == resultCode) {// 发货地址返回数据sss
			BLComment addressInfo = (BLComment) data
					.getSerializableExtra("addressInfo");
			send_out_address = addressInfo.getProvince()
					+ addressInfo.getCity() + addressInfo.getCounty()
					+ addressInfo.getAddress();
			StrUtils.SetTxt(tv_address_display, send_out_address);

		}
		if (AEditInfBack.Tag_AddGoods_Title == requestCode
				&& AEditInfBack.Tag_AddGoods_Title == resultCode) {// 添加产品的title

			StrUtils.SetTxt(Tv_Goods_Title,
					data.getStringExtra(AEditInfBack.Tag_key));
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	// 保存已经生成的规格数据的ap*******************************************
	class AddGoodRuleAdapter1 extends BaseAdapter {

		private int ResourseId;
		private LayoutInflater inflater;
		private List<BLComment> datas = new ArrayList<BLComment>();

		public AddGoodRuleAdapter1(int ResourseId) {
			super();

			this.ResourseId = ResourseId;
			this.inflater = LayoutInflater.from(BaseContext);

		}

		/**
		 * 加载更多
		 */
		public void AddFrashData(List<BLComment> dass) {

			this.datas = dass;

			this.notifyDataSetChanged();
		}

		/**
		 * 获取类别的数据源
		 */
		public List<BLComment> GetShortDatas() {
			return datas;
		}

		// 长按删除***********************************************
		public void DeletItem(int Postion) {
			datas.remove(Postion);
			this.notifyDataSetChanged();
		}

		/**
		 * 当换类目时候需要清除之前老的数据
		 */
		public void RemoveDatas() {
			datas = new ArrayList<BLComment>();
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
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			AddGoodItem addGoodRule = null;
			if (arg1 == null) {
				addGoodRule = new AddGoodItem();
				arg1 = inflater.inflate(ResourseId, null);
				addGoodRule.tv_add_good_value1 = ViewHolder.get(arg1,
						R.id.tv_add_good_value1);
				addGoodRule.tv_add_good_value2 = ViewHolder.get(arg1,
						R.id.tv_add_good_value2);
				addGoodRule.tv_add_good_value3 = ViewHolder.get(arg1,
						R.id.tv_add_good_value3);
				addGoodRule.tv_add_good_value4 = ViewHolder.get(arg1,
						R.id.tv_add_good_value4);

				addGoodRule.tv_add_good_rule_lable1 = ViewHolder.get(arg1,
						R.id.tv_add_good_rule_lable1);

				addGoodRule.tv_add_good_rule_lable2 = ViewHolder.get(arg1,
						R.id.tv_add_good_rule_lable2);

				arg1.setTag(addGoodRule);
			} else {
				addGoodRule = (AddGoodItem) arg1.getTag();
			}

			StrUtils.SetTxt(addGoodRule.tv_add_good_rule_lable1, datas
					.get(arg0).getAdd_good_attrs_name_1());
			StrUtils.SetTxt(addGoodRule.tv_add_good_rule_lable2, datas
					.get(arg0).getAdd_good_attrs_name_2());

			StrUtils.SetTxt(addGoodRule.tv_add_good_value1, datas.get(arg0)
					.getAdd_good_attrs_value_1());
			StrUtils.SetTxt(addGoodRule.tv_add_good_value2, datas.get(arg0)
					.getAdd_good_attrs_value_2());
			StrUtils.SetTxt(
					addGoodRule.tv_add_good_value3,
					(StrUtils.toFloat(datas.get(arg0)
							.getAdd_good_attrs_value_3()) / 100) + "元");
			StrUtils.SetTxt(addGoodRule.tv_add_good_value4, datas.get(arg0)
					.getAdd_good_attrs_value_4());

			return arg1;
		}

		public class AddGoodItem {

			TextView tv_add_good_rule_lable1, tv_add_good_rule_lable2,
					tv_add_good_value1, tv_add_good_value2, tv_add_good_value3,
					tv_add_good_value4;
		}
	}

	// 选择拍摄视频或者是图片***********************************************
	public class AddGoodPopupWindows extends PopupWindow {

		public AddGoodPopupWindows(View parent) {

			ShowBottomPop(BaseContext, parent, "发布图片", "发布视频",
					new IBottomDialogResult() {

						@Override
						public void SecondResult() {
							IsCurrentPic = false;
							GoToSelectVid();
						}

						@Override
						public void FristResult() {
							IsCurrentPic = true;
							GoToSelectPic();
						}

						@Override
						public void CancleResult() {
						}
					});

		}
	}

	/**
	 * 展示已经获取到商品的图片*********************************************
	 */
	private class ShowGoodsPicAp extends BaseAdapter {
		private List<ImageItem> mDataList = new ArrayList<ImageItem>();

		public ShowGoodsPicAp() {

		}

		public List<ImageItem> GetPicDatas() {
			return mDataList;
		}

		/**
		 * 进行刷新操作
		 */
		public void FrashShowAp(List<ImageItem> daa) {
			iv_add_good_addhead.setVisibility(View.GONE);
			iv_add_good_goods_gridview.setVisibility(View.VISIBLE);
			this.mDataList = daa;
			this.notifyDataSetChanged();

		}

		/**
		 * 进行刷新操作
		 */
		public void FrashShowAp1(List<ImageItem> daa) {

			this.mDataList = daa;

			this.notifyDataSetChanged();

		}

		public int getCount() {
			// 多返回一个用于展示添加图标
			if (mDataList.size() == 9)
				return 9;
			else
				return mDataList.size() + 1;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("ViewHolder")
		public View getView(int position, View convertView, ViewGroup parent) {
			// 所有Item展示不满一页，就不进行ViewHolder重用了，避免了一个拍照以后添加图片按钮被覆盖的奇怪问题
			convertView = View.inflate(BaseContext, R.layout.item_addgood_grad,
					null);
			ImageView imageIv = (ImageView) convertView
					.findViewById(R.id.item_grid_image_addgood);
			LinearLayout.LayoutParams ps = new LinearLayout.LayoutParams(
					(screenWidth - DimensionPixelUtil.dip2px(BaseContext, 30)) / 3,
					(screenWidth - DimensionPixelUtil.dip2px(BaseContext, 30)) / 3);
			ps.setMargins(DimensionPixelUtil.dip2px(BaseContext, 4), 0, 0, 0);
			imageIv.setLayoutParams(ps);
			imageIv.setScaleType(ImageView.ScaleType.FIT_XY);

			// ImageDisplayer.getInstance(mContext).displayBmp(imageIv,
			// item.thumbnailPath, item.sourcePath);
			if (position == mDataList.size()) {
				if (position == 9) {
					final ImageItem item = mDataList.get(position);
					ImageDisplayer.getInstance(BaseContext).displayBmp(imageIv,
							"", item.imagePath);
				} else
					imageIv.setImageResource(R.drawable.multiphoto_icon_addpic_unfocused);
			} else {
				final ImageItem item = mDataList.get(position);
				// ImageDisplayer.getInstance(mContext).displayBmp(imageIv, "",
				// item.imagePath);
				imageIv.setImageBitmap(item.getBitmap());
			}
			return convertView;
		}
	}

	// 展示已经获取到商品的图片***************************************************

	/**
	 * 在选择玩照片后进行的消息通知
	 * 
	 * @param MSG
	 */
	public void OnGoodPicMessage(BMessage MSG) {

		switch (MSG.getMessageType()) {
		case BMessage.Tage_AddGoodPic:// 添加商品的图片
			IsCurrentPic = true;
			HavaUpVidoCover = "";
			HavaUpVido = "";

			if (0 == MSG.getAddImageItems().size()) {
				// PromptManager.ShowCustomToast(BaseContext, "空");
			} else {
				IsVidoImageEditing = true;
				goodsPicAp.FrashShowAp(MSG.getAddImageItems());
				// PromptManager.ShowCustomToast(BaseContext, MyGoodsPic.size()
				// + "张图片");

			}

			break;
		case BMessage.Tage_AddGoodDescPic:// 添加商品描述的图片s

			HavaUpVidoCover = "";
			HavaUpVido = "";

			GoodsDescriptionLs = MSG.getAddImageItems();
			addgoods_description.setVisibility(View.GONE);
			addgood_desc_horizon_ls.setVisibility(View.VISIBLE);
			if (null == GoodsDescriptionLs || 0 == GoodsDescriptionLs.size()) {// 描述宝贝时候没有添加图片

			} else {
				IsVidoImageEditing = true;
				goodsDescAp.FrashShowAp1(GoodsDescriptionLs);
				JSONArray jsonArray = StrUtils.ListImages(GoodsDescriptionLs);
				// PromptManager.ShowCustomToast(BaseContext, "描述的大小"
				// + GoodsDescriptionLs.size());
				// LogUtils.i("宝贝描述=》" + jsonArray.toString());

			}

			break;
		case 290:// 添加宝贝视频需要进行的刷新界面
			// 收到录制视频的消息 进行刷新
			IsVidoImageEditing = true;
			right_txt.setText("录制");
			right_txt.setVisibility(View.VISIBLE);
			right_txt.setTextColor(getResources().getColor(R.color.white));

			IsCurrentPic = false;
			VidoNewPath = MSG.getReCordVidoPath();
			FrashVidoView(VidoNewPath);
			UpLoadVidoCover(VidoNewPath, true);

			break;
		default:
			break;
		}

	}

	/**
	 * 通过视频的psth进行展示
	 */
	private void FrashVidoView(String vidoNewPath2) {
		HavaUpVidoCover = "";
		HavaUpVido = "";
		iv_add_good_addhead.setVisibility(View.GONE);

		addgood_vido_lay.setVisibility(View.VISIBLE);
//		Bitmap mcover = createVideoThumbnail(vidoNewPath2);
		addgood_vido_cover.setImageBitmap(createVideoThumbnail(vidoNewPath2));
//		CacheUtil.BitMapRecycle(mcover);

	}

	// ***************************开始上传*********************************
	/**
	 * 上传宝贝并且上架
	 */
	private void UpPicVido() {
		// 先上传描述的图片=====>再上传宝贝的主要图片或者主要视频视频==>调用上传宝贝的接口进行上传
		PromptManager.showtextLoading3(BaseContext, "正在发布");
		List<ImageItem> DescPicLs = goodsDescAp.GetPicDatas();

		if (DescPicLs.size() == 0) {// 没有描述宝贝==》直接上传宝贝的视频或者图片=》调用上传宝贝接口
			UpGoodPicOrVido();
		} else {// 存在描述宝贝 上传描述图片==》直接上传宝贝的视频或则图片==》调用上传宝贝接口

			int AllDescNeedPics = 0;// 计算需要上传几张图片
			for (int i = 0; i < DescPicLs.size(); i++) {
				if (DescPicLs.get(i).getBitmap() != null) {
					AllDescNeedPics = AllDescNeedPics + 1;
					goodsDescriptFloats.add(StrUtils
							.GetBitmapWidthHeightRatio(DescPicLs.get(i)
									.getBitmap()));
				}

			}
			AllMyDescNeedUpNumber = 0;// 需要进行描述累加的
			for (int i = 0; i < DescPicLs.size(); i++) {

				if (DescPicLs.get(i).getBitmap() != null) {// 不等于
					UpDescPic(Bitmap2Bytes(DescPicLs.get(i).getBitmap()),
							StrUtils.UploadQNName("pic"), i, AllDescNeedPics);
				}

			}
		}

	}

	/**
	 * 上传宝贝描述的图片资源
	 */
	public void UpDescPic(byte[] bytes, final String picname,
			final int postion, final int AllMyNeedNumber) {

		NUpLoadUtils dLoadUtils = new NUpLoadUtils(BaseContext, bytes, picname);
		dLoadUtils.SetUpResult(new UpResult() {

			@Override
			public void Progress(String arg0, double arg1) {
			}

			@Override
			public void Onerror() {

				AllMyDescNeedUpNumber = AllMyDescNeedUpNumber + 1;
				// PromptManager.ShowCustomToast(BaseContext, "上传Onerror");
				if (AllMyDescNeedUpNumber == AllMyNeedNumber) {// 说明宝贝的描述以及全部上传完毕
																// 忽略上传失败的东西
					UpGoodPicOrVido();
				}
			}

			@Override
			public void Complete(String HostUrl, String Url) {
				AllMyDescNeedUpNumber = AllMyDescNeedUpNumber + 1;
				goodsDescAp.GetPicDatas().get(postion)
						.setThumbnailPath(HostUrl);
				// PromptManager.ShowCustomToast(BaseContext, "上传成功名字：" +
				// picname);
				if (AllMyDescNeedUpNumber == AllMyNeedNumber) {// 说明宝贝的描述以及全部上传完毕
																// 忽略上传失败的东西
					UpGoodPicOrVido();
				}
			}
		});
		dLoadUtils.UpLoad();

	}

	// *********************************宝贝效果***************************************
	/**
	 * 上传完毕宝贝描述后 需要上传宝贝的图片或者视频
	 */
	private void UpGoodPicOrVido() {
		if (IsCurrentPic) {// 上传图片
			AllGoodNeedUpNumber = 0;
			List<ImageItem> imageItems = goodsPicAp.GetPicDatas();
			int NeedGoodsUp = 0;
			for (int i = 0; i < imageItems.size(); i++) {// 计算需要上传商品的数据
				if (null != imageItems.get(i).getBitmap())
					NeedGoodsUp = NeedGoodsUp + 1;
			}
			// 开始上传图片
			for (int i = 0; i < imageItems.size(); i++) {// 计算需要上传商品的数据
				if (null != imageItems.get(i).getBitmap())
					UpGoodsPic(Bitmap2Bytes(imageItems.get(i).getBitmap()),
							StrUtils.UploadQNName("pic"), i, NeedGoodsUp);
			}

		} else {// 视频上传
			UpLoadVidoCover(VidoNewPath, false);
		}

	}

	/**
	 * 上传宝贝图片资源
	 */
	public void UpGoodsPic(byte[] bytes, final String picname,
			final int postion, final int AllMyNeedNumber) {
		NUpLoadUtils dLoadUtils = new NUpLoadUtils(BaseContext, bytes, picname);
		dLoadUtils.SetUpResult(new UpResult() {
			@Override
			public void Progress(String arg0, double arg1) {
			}

			@Override
			public void Onerror() {
				AllGoodNeedUpNumber = AllGoodNeedUpNumber + 1;
				// PromptManager.ShowCustomToast(BaseContext, "上传Onerror");
				if (AllGoodNeedUpNumber == AllMyNeedNumber) {// 说明宝贝的描述以及全部上传完毕
					PicVidoHaveUpLoadDwon(); // 忽略上传失败的东西

				}
			}

			@Override
			public void Complete(String HostUrl, String Url) {
				AllGoodNeedUpNumber = AllGoodNeedUpNumber + 1;

				goodsPicAp.GetPicDatas().get(postion).setThumbnailPath(HostUrl);
				// PromptManager.ShowCustomToast(BaseContext, "上传成功名字：" +
				// picname);
				if (AllGoodNeedUpNumber == AllMyNeedNumber) {// 说明宝贝的描述以及全部上传完毕
					PicVidoHaveUpLoadDwon(); // 忽略上传失败的东西

				}
			}
		});
		dLoadUtils.UpLoad();

	}

	// *******************************视频操作******************************************************
	/**
	 * 上传宝贝视频 先上传宝贝的封面===》在上传宝贝的视频
	 */
	private void UpLoadVidoCover(final String VidoPath, final boolean IsHindLoad) {
		// 先上传宝贝的封面===》在上传宝贝的实际需要的视频
		if (!StrUtils.isEmpty(HavaUpVidoCover)) {
			// 直接去上传视频
			UpLoadVido(VidoPath, IsHindLoad);
			return;
		}
		final Bitmap mBitmap = createVideoThumbnail(VidoPath);
		NUpLoadUtils dLoadUtils = new NUpLoadUtils(BaseContext,
				Bitmap2Bytes(mBitmap), StrUtils.UploadQNName("vid"));
		dLoadUtils.SetUpResult(new UpResult() {
			@Override
			public void Progress(String arg0, double arg1) {

			}

			@Override
			public void Onerror() {
				CacheUtil.BitMapRecycle(mBitmap);
			}

			@Override
			public void Complete(String HostUrl, String Url) {
				HavaUpVidoCover = HostUrl;
				// if(!IsHindLoad)//如果不是
				UpLoadVido(VidoPath, IsHindLoad);
				CacheUtil.BitMapRecycle(mBitmap);
			}
		});
		dLoadUtils.UpLoad();

	}

	/**
	 * 上传视频的图片
	 */
	private void UpLoadVido(String Path, final boolean IsHindUpVido) {

		if (!StrUtils.isEmpty(HavaUpVido))// 不是第一次上传宝贝{}
		{
			PicVidoHaveUpLoadDwon();
			return;
		}
		NUPLoadUtil dLoadUtils = new NUPLoadUtil(BaseContext, new File(Path),
				StrUtils.UploadVido("photo"));

		dLoadUtils.SetUpResult1(new UpResult1() {

			@Override
			public void Progress(String arg0, double arg1) {
			}

			@Override
			public void Onerror() {
				PromptManager.closeTextLoading3();
			}

			@Override
			public void Complete(String HostUrl, String Url) {
				HavaUpVido = HostUrl;
				if (!IsHindUpVido)// 不是第一次上传宝贝
					PicVidoHaveUpLoadDwon();

			}
		});
		dLoadUtils.UpLoad();
	}

	/**
	 * 宝贝图片或者视频已经上传完成==》需要调用上传宝贝的接口进行传递数据
	 */
	private void PicVidoHaveUpLoadDwon() {

		AddMyGoods();
	}

	/**
	 * 长按规格item进行删除
	 * 
	 * @param Postion
	 */
	private void DeletItemRule(final int Postion) {

		Builder dialog = new AlertDialog.Builder(BaseContext);
		LayoutInflater inflater = (LayoutInflater) BaseContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.dialog_reminder, null);
		dialog.setView(layout);
		TextView dialog_txt_title = (TextView) layout
				.findViewById(R.id.dialog_txt_title);

		dialog_txt_title.setText("是否删除该规格？");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				addGoodRuleAdapter.DeletItem(Postion);

			}
		});

		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		dialog.show();
	}

	// 按返回键时候 已经编辑过了 提示是否退出，没有编辑就直接退出
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复
			if (!StrUtils.TextIsEmPty(Tv_Goods_Title)
					|| addGoodRuleAdapter.GetShortDatas().size() > 0
					|| !StrUtils.EditTextIsEmPty(et_add_good_post_price)
					|| (!StrUtils.TextIsEmPty(tv_address_display) && tv_address_display
							.equals(getResources().getString(
									R.string.add_address)))
					|| (null != CurrentSortData && !StrUtils
							.isEmpty(CurrentSortData.getId()))
					|| IsVidoImageEditing || !StrUtils.isEmpty(VidoNewPath)) {

				ShowCustomDialog("添加商品中,是否退出?",
						getResources().getString(R.string.cancle), "退出",
						new IDialogResult() {

							@Override
							public void RightResult() {
								onBackPressed();
							}

							@Override
							public void LeftResult() {
							}
						});
			} else {
				onBackPressed();
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		try {
			EventBus.getDefault().unregister(this);
		} catch (Exception e) {
		}
		for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
			CacheUtil.BitMapRecycle(Bimp.tempSelectBitmap.get(i).getBitmap());
		}
		for (int i = 0; i < goodsPicAp.GetPicDatas().size(); i++) {
			CacheUtil
					.BitMapRecycle(goodsPicAp.GetPicDatas().get(i).getBitmap());
		}
		for (int i = 0; i < goodsDescAp.GetPicDatas().size(); i++) {
			CacheUtil.BitMapRecycle(goodsDescAp.GetPicDatas().get(i)
					.getBitmap());
		}
		for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
			CacheUtil.BitMapRecycle(Bimp.tempSelectBitmap.get(i).getBitmap());
		}
		for (int i = 0; i < GoodsDescriptionLs.size(); i++) {
			CacheUtil.BitMapRecycle(GoodsDescriptionLs.get(i).getBitmap());
		}
		// PromptManager.ShowCustomToast(BaseContext, "add回收bitmap");
	}
}
