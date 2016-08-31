package io.vtown.WeiTangApp.ui.comment;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BLShow;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils.UpResult;
import io.vtown.WeiTangApp.comment.selectpic.ui.AShareGaller;
import io.vtown.WeiTangApp.comment.selectpic.ui.AlbumActivity;
import io.vtown.WeiTangApp.comment.selectpic.ui.GalleryActivity;
import io.vtown.WeiTangApp.comment.selectpic.util.Bimp;
import io.vtown.WeiTangApp.comment.selectpic.util.FileUtils;
import io.vtown.WeiTangApp.comment.selectpic.util.ImageItem;
import io.vtown.WeiTangApp.comment.selectpic.util.PublicWay;
import io.vtown.WeiTangApp.comment.selectpic.util.Res;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.comment.view.custom.switchButtonView.EaseSwitchButton;
import io.vtown.WeiTangApp.event.interf.IBottomDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.volley.Request.Method;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-29 上午11:10:08
 * @see 上架并转发=》转发分享界面
 */
public class AGoodShare extends ATitleBase implements OnCheckedChangeListener {
	// 转发时候的输入的内容
	private EditText good_zhuanfa_share_ed;

	// 上传图片时候的九宫格的
	private CompleteGridView good_zhuanfa_gridview;

	// 商品分享的按钮
	private TextView good_zhuanfa_share_bt;

	// 上传图片时候的九宫格的Ap
	private MyGridAdapter myGridAdapter;

	// 父类的布局
	private View parentView;
	// 上传文件时候记录是否已经上传完
	private int AllNumber = 0;
	// 需要上传的本地图片的个数
	private int NeedUpNumber = 0;
	// switchbutton的layout文件
	private View good_zhuanfa_switchbt_lay;
	// 只分享商品链接****************************
	private EaseSwitchButton good_zhuanfa_switchbt;
	private boolean IsOnLyShareWx;

	// 默认是show列表进来的 需要获取blcomment&&&如果是商品详情进入的需要获取其他类型的数据
	public final static String Key_FromShow = "fromshow";
	private boolean IsShow = true;

	// show；列表传递进来的数据****************
	public final static String Key_Data = "showdata";
	// =====>从show列表进入的分享界面
	private BLShow ShowDatas = new BLShow();// ;new BLComment();

	// TODO====>还要添加一个从商品详情弹出框里面进入分享的接受数据

	// 判断是照片还是视频=====》标识
	private boolean IsPic;
	// 如果是图片 需要设置pic的数据源
	private List<ImageItem> picImageItems = new ArrayList<ImageItem>();
	public static Bitmap bimap;
	// ===============》other界面注册时间总线finish
	private BUser MyUser;
	boolean OtherCount = false;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_good_zhuanfa);
		EventBus.getDefault().register(this, "SharGoodEvent", BMessage.class);
		setPicType(3);
		bimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.multiphoto_icon_addpic_unfocused);

		Res.init(this);
		MyUser = Spuit.User_Get(BaseContext);
		IBund();
		parentView = LayoutInflater.from(BaseContext).inflate(
				R.layout.activity_good_zhuanfa, null);
		PublicWay.activityList.add(this);
		IBasView();
	}

	// 获取数据
	private void IBund() {
		IsShow = getIntent().getBooleanExtra(Key_FromShow, false);
		if (IsShow) {// 从show进入的
			ShowDatas = (BLShow) getIntent().getSerializableExtra(Key_Data);
			IsPic = ShowDatas.getIs_type().equals("0");

			IsPic = true;
			// if (IsPic) {// 是图片的分享
			Bimp.tempSelectBitmap = (ArrayList<ImageItem>) GetPicChange(ShowDatas
					.getImgarr());
			Bimp.max = GetPicChange(ShowDatas.getImgarr()).size();

		} else {

		}

	}

	private void IBasView() {
		// swithbt的初始化=》只分享商品链接
		good_zhuanfa_switchbt_lay = findViewById(R.id.good_zhuanfa_switchbt_lay);
		good_zhuanfa_switchbt = (EaseSwitchButton) good_zhuanfa_switchbt_lay
				.findViewById(R.id.switch_select);
		((TextView) good_zhuanfa_switchbt_lay
				.findViewById(R.id.tv_switch_button_lable)).setText("只分享商品链接");

		good_zhuanfa_share_ed = (EditText) findViewById(R.id.good_zhuanfa_share_ed);
		// good_zhuanfa_share_ed.setFocusableInTouchMode(false);
		good_zhuanfa_gridview = (CompleteGridView) findViewById(R.id.good_zhuanfa_gridview);
		good_zhuanfa_share_bt = (TextView) findViewById(R.id.good_zhuanfa_share_bt);
		good_zhuanfa_share_bt.setOnClickListener(this);
		myGridAdapter = new MyGridAdapter(BaseContext);
		myGridAdapter.update();
		good_zhuanfa_gridview.setAdapter(myGridAdapter);
		good_zhuanfa_gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					// new PicPop(BaseContext, parentView);
					SelectPicPop();
				} else {
					Intent intent = new Intent(BaseActivity, AShareGaller.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					BaseActivity.startActivity(intent);
				}
			}
		});

		good_zhuanfa_switchbt.setChecked(false);
		good_zhuanfa_switchbt.setOnCheckedChangeListener(this);

	}

	@Override
	protected void onRestart() {
		myGridAdapter.update();
		super.onRestart();

	}

	@Override
	protected void InitTile() {
		SetTitleTxt(getResources().getString(R.string.share));
		SetRightText("添加图片");

		right_txt.setOnClickListener(this);

	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
		PromptManager.closeTextLoading3();

		switch (Data.getHttpResultTage()) {
		case 5:// 分享Show成功
			PromptManager.ShowCustomToast(BaseContext, "转发成功");
			BaseActivity.finish();
			break;

		default:
			break;
		}

	}

	@Override
	protected void DataError(String error, int LoadType) {
		PromptManager.closeTextLoading3();

		PromptManager.ShowCustomToast(BaseContext, error);
		if (LOAD_LOADMOREING == LoadType) {// 分享show失败

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

	@Override
	protected void MyClick(View V) {
		switch (V.getId()) {
		case R.id.good_zhuanfa_share_bt:
			hintKbTwo();
			// new SharePopupWindows(BaseContext, parentView, IsShow);

			SharePop();

			break;
		case R.id.right_txt:
			SelectPicPop();
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
	 * 点击添加图片时候的pop操作
	 */
	private void SelectPicPop() {
		ShowBottomPop(BaseContext, parentView, "拍照", "照片",
				new IBottomDialogResult() {

					@Override
					public void SecondResult() {
						Intent intent = new Intent(BaseActivity,
								AlbumActivity.class).putExtra("isshare", true);
						startActivity(intent);
						overridePendingTransition(R.anim.activity_translate_in,
								R.anim.activity_translate_out);
					}

					@Override
					public void FristResult() {
						photo();
					}

					@Override
					public void CancleResult() {
					}
				});
	}

	/**
	 * 点击分享时候弹出的框
	 */
	private void SharePop() {
		ShowBottomPop(BaseContext, parentView, "朋友圈分享", IsOnLyShareWx ? "微信分享"
				: "Show分享", new IBottomDialogResult() {

			@Override
			public void SecondResult() {
				if (!IsOnLyShareWx) {
					ShowZhuanNet();
				} else {
					if (IsShow) {// show的数据源转发微信好友
						Share(1, ShowDatas.getGoodinfo().getTitle(), ShowDatas
								.getSellerinfo().getSeller_name(), ShowDatas
								.getImgarr().get(0), ShowDatas.getGoodurl());
					} else {// 商品详情的数据源专访微信好友
						// Share(2);
					}

				}
			}

			@Override
			public void FristResult() {
				if (IsShow) {// show的数据源转发朋友圈

					Share(2, ShowDatas.getGoodinfo().getTitle(), ShowDatas
							.getSellerinfo().getSeller_name(), ShowDatas
							.getImgarr().get(0), ShowDatas.getGoodurl());
				} else {// 商品详情的数据源专访朋友圈
					// Share(2);
				}
			}

			@Override
			public void CancleResult() {
			}
		});
	}

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {

				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				FileUtils.saveBitmap(bm, fileName);
				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);
				Bimp.tempSelectBitmap.add(takePhoto);
			}
			break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			BaseActivity.finish();
		}
		return true;
	}

	@SuppressLint("HandlerLeak")
	public class MyGridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;
		boolean IsZroo = false;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public MyGridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {

			loading();
		}

		public int getCount() {

			return Bimp.tempSelectBitmap.size();

			// if (Bimp.tempSelectBitmap.size() == 9) {
			// return 9;
			// }
			// return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (IsZroo && position == 0)
				return convertView;
			if (!IsZroo && position == 0) {
				IsZroo = true;
			}
			if (!StrUtils.isEmpty(Bimp.tempSelectBitmap.get(position)
					.getImagePath())
					|| Bimp.tempSelectBitmap.get(position).getBitmap() != null) {
				if (Bimp.tempSelectBitmap.get(position).getBitmap() == null)
					Bimp.tempSelectBitmap.get(position).setBitmap(
							StrUtils.GetBitMapFromPath(Bimp.tempSelectBitmap
									.get(position).getImagePath()));
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position)
						.getBitmap());
			} else {
				String path = Bimp.tempSelectBitmap.get(position)
						.getThumbnailPath();
				if (null == path)
					path = "";

				ImageLoaderUtil.Load2(path, holder.image, R.drawable.error_iv2);
			}

			// }

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					myGridAdapter.notifyDataSetChanged();

					// PromptManager.ShowCustomToast(BaseContext, "条数："
					// + Bimp.tempSelectBitmap.size());
					if (Bimp.tempSelectBitmap.size() > 0) {
						// PromptManager.ShowCustomToast(BaseContext, "path："
						// + Bimp.tempSelectBitmap.get(0).getImagePath());
					}
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		IsOnLyShareWx = isChecked;
		good_zhuanfa_share_ed.setVisibility(IsOnLyShareWx ? View.GONE
				: View.VISIBLE);
		good_zhuanfa_gridview.setVisibility(IsOnLyShareWx ? View.GONE
				: View.VISIBLE);
		right_txt.setVisibility(!IsOnLyShareWx ? View.VISIBLE : View.GONE);

	}

	/**
	 * 如果是图片将图片换成bitmap数组&&&&&&&&&&&&&&如果是视频就不需要操作
	 */

	private List<ImageItem> GetPicChange(List<String> pics) {
		List<ImageItem> items = new ArrayList<ImageItem>();
		// 需要图片转化内置的列表数据======》并且展示
		for (int i = 0; i < pics.size(); i++) {
			items.add(new ImageItem(pics.get(i), ""));
		}
		return items;
	}

	/**
	 * 接受事件总线的消息
	 * 
	 * @param da
	 */
	public void SharGoodEvent(BMessage da) {

	}

	/**
	 * 点击只分享商品链接的话 =>图片的商品需要分享图片&&&&&&视频的商品分享第一帧图片
	 */

	/**
	 * 开始进行Show分享的操作
	 */

	// 生成show分享的参数
	/**
	 * 
	 * @param Urls
	 *            视频的集合
	 * @param GoodId商品的ID
	 * @param vid小视频的地址
	 * @param intro填写内容
	 * @param is_type发图片还是视频
	 *            0图片1视频
	 * @param pre_url缩略图地址
	 * @return
	 */
	private HashMap<String, String> ShowZhuanNetParm(List<String> Urls,
			String GoodId, String vid, String intro, Boolean Is_Type_Pic,
			String pre_url) {
		HashMap<String, String> hashMap = new HashMap<String, String>();

		hashMap.put("goods_id", GoodId);
		hashMap.put("seller_id", MyUser.getSeller_id());
		hashMap.put("is_type", Is_Type_Pic ? "0" : "1");
		hashMap.put("intro", intro);
		hashMap.put("ratio", "1");
		if (IsShow) {// Show进来的
			if (IsPic) {// Show==>图片
				SetHasmp(hashMap, Urls);
				hashMap.put("pre_url", Urls.get(0));
			}
			// else {// Show==>视频
			// hashMap.put("pre_url", pre_url);
			// }
		} else {// 商品详情进来的
			if (IsPic) {
				// hashMap.put("pre_url", value)
			} else {
				// hashMap.put("pre_url", value)
			}
		}
		return hashMap;

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
			CacheUtil.BitMapRecycle(Bimp.tempSelectBitmap.get(i).getBitmap());
		}
		CacheUtil.BitMapRecycle(bimap);
		Bimp.tempSelectBitmap = new ArrayList<ImageItem>();
		Bimp.max = 0;
	}

	// 图片列表传递存放到hasmap里面
	private void SetHasmp(HashMap<String, String> ha, List<String> Urls) {
		for (int i = 0; i < Urls.size(); i++) {
			ha.put("cid" + (i + 1), Urls.get(i));
		}

	}

	// 提交Show分享的直接接口
	private void ShowZhuanNet() {
		if (Bimp.tempSelectBitmap.size() == 0) {
			PromptManager.ShowCustomToast(BaseContext, "请添加图片");
			return;
		}
		// HashMap<String, String> mHashMap = null;
		if (IsShow) {// 从Show进来的
			if (IsPic) {// 是照片
				// 提交前进行上传图片的操作
				ImageShareShow();
				return;
			}

		} else {// 从商品详情进来的

		}

		// SetTitleHttpDataLisenter(this);
		// if (null == mHashMap) {
		// PromptManager.ShowCustomToast(BaseContext, "系统错误");
		// return;
		// }
		// PromptManager.showtextLoading3(BaseContext, "努力转发...");
		// FBGetHttpData(mHashMap, Constants.GoodsShow_ZhuanFa, Method.POST, 5,
		// LOAD_LOADMOREING);
	}

	/**
	 * 图片分享时候 需要先上传图片完毕在根据上传后的七牛返回的URL分享Show
	 */
	private void ImageShareShow() {

		List<ImageItem> PicLs = Bimp.tempSelectBitmap;
		// 计算下需要上传的图片信息和 总的图片的信息****************
		NeedUpNumber = 0;

		AllNumber = 0;
		for (int i = 0; i < PicLs.size(); i++) {
			// if (StrUtils.isEmpty(PicLs.get(i).getThumbnailPath())) {
			// NeedUpNumber = NeedUpNumber + 1;
			// }
			if (null != PicLs.get(i).getBitmap()) {
				NeedUpNumber = NeedUpNumber + 1;
			}
		}

		// 如果没有需要上传的图片 直接进行转发
		if (0 == NeedUpNumber) {
			UpOverToShare();
			return;
		}
		// 如果有需要上传的图片===》开始对上边处理过需要上传图片的信息进行上传处理****************

		for (int i = 0; i < PicLs.size(); i++) {
			final int Postion = i;
			// if (StrUtils.isEmpty(PicLs.get(i).getThumbnailPath())) {
			if (null != PicLs.get(i).getBitmap()) {
				// 开始上传本地新增加的图片
				PromptManager.showtextLoading3(BaseContext, getResources()
						.getString(R.string.uploading));

				NUpLoadUtils dLoadUtils = new NUpLoadUtils(BaseContext,
						StrUtils.Bitmap2Bytes(PicLs.get(i).getBitmap()),
						StrUtils.UploadQNName("photo"));

				dLoadUtils.SetUpResult(new UpResult() {

					@Override
					public void Progress(String arg0, double arg1) {

					}

					@Override
					public void Onerror() {
						PromptManager.closeTextLoading3();
						Bimp.tempSelectBitmap.get(Postion).setThumbnailPath("");
						Bimp.tempSelectBitmap.remove(Postion);
						AllNumber = AllNumber + 1;
						// PromptManager.ShowCustomToast(BaseContext,
						// "上传Onerror");
						if (AllNumber == NeedUpNumber) {
							UpOverToShare();
						}
					}

					@Override
					public void Complete(String HostUrl, String Url) {
						AllNumber = AllNumber + 1;

						PromptManager.closeTextLoading3();
						Bimp.tempSelectBitmap.get(Postion).setThumbnailPath(
								HostUrl);
						if (AllNumber == NeedUpNumber) {
							UpOverToShare();
						}
					}
				});
				dLoadUtils.UpLoad();

			}

		}

		// PromptManager.ShowCustomToast(BaseContext, "需要上传的图片" + NeedUpNumber
		// + ";;;;;总共图片数量" + PicLs.size());
		// if (true)
		// return;

	}

	/**
	 * 上传完毕图片后需要进行相应的分享
	 */

	private void UpOverToShare() {
		List<String> Urlss = new ArrayList<String>();
		for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
			if (!StrUtils.isEmpty(Bimp.tempSelectBitmap.get(i)
					.getThumbnailPath())) {
				Urlss.add(Bimp.tempSelectBitmap.get(i).getThumbnailPath());
			}
		}
		SetTitleHttpDataLisenter(this);
		PromptManager.showtextLoading3(BaseContext,
				getResources().getString(R.string.loading));
		HashMap<String, String> mHashMap = ShowZhuanNetParm(Urlss,
				ShowDatas.getGoods_id(), "", good_zhuanfa_share_ed.getText()
						.toString().trim(), IsPic, ShowDatas.getImgarr().get(0));

		FBGetHttpData(mHashMap, Constants.GoodsShow_ZhuanFa, Method.POST, 5,
				LOAD_LOADMOREING);

	}

	/**
	 * 微信分享的操作代码
	 */

	private void Share(int Type, String Title, String Content, String IvUrl,
			String UrL) {
		ShareSDK.initSDK(BaseContext);
		Platform platform = null;
		ShareParams sp = new ShareParams();
		switch (Type) {
		case 1:// 好友分享
			platform = ShareSDK.getPlatform(BaseContext, Wechat.NAME);
			sp.setShareType(Platform.SHARE_WEBPAGE);

			sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);

			sp.setText(Content);
			sp.setImageUrl(IvUrl);
			sp.setTitle(getResources().getString(R.string.share_app) + Title);//
			sp.setUrl(UrL );

			// sp.setText("大兔兔的测试数据");
			// sp.setImageUrl("http://static.freepik.com/free-photo/letter-a-underlined_318-8682.jpg");
			// sp.setTitle("大兔兔的title");//
			// sp.setUrl("www.baidu.com");
			break;
		case 2:// 朋友圈分享
			platform = ShareSDK.getPlatform(BaseContext, WechatMoments.NAME);
			sp.setShareType(Platform.SHARE_WEBPAGE);// SHARE_WEBPAGE);

			sp.setText(Content);
			sp.setImageUrl(IvUrl);
			sp.setTitle(getResources().getString(R.string.share_app) +" "+ Title);//
			sp.setUrl(UrL );

			// sp.setText("大兔兔的测试数据");
			// sp.setImageUrl("http://static.freepik.com/free-photo/letter-a-underlined_318-8682.jpg");
			// sp.setTitle("大兔兔的测试数据");//
			// sp.setUrl("www.baidu.com");
			break;
		default:
			break;
		}
		platform.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				PromptManager.ShowCustomToast(BaseContext, "分享失败");
			}

			@Override
			public void onComplete(Platform arg0, int arg1,
					HashMap<String, Object> arg2) {
				PromptManager.ShowCustomToast(BaseContext, "分享完成");
				BaseActivity.finish();
			}

			@Override
			public void onCancel(Platform arg0, int arg1) {
				PromptManager.ShowCustomToast(BaseContext, "分享取消");
			}
		});
		platform.share(sp);
	}
}
