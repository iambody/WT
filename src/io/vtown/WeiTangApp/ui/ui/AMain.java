package io.vtown.WeiTangApp.ui.ui;

import io.vtown.WeiTangApp.AppManager;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.comment.BUpData;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.NHttpBaseStr;
import io.vtown.WeiTangApp.comment.upgrade.UpdateManager;
import io.vtown.WeiTangApp.comment.util.IMUtile;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.onConfirmClick;
import io.vtown.WeiTangApp.comment.view.dialog.CustomDialog.oncancelClick;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.event.interf.IHttpResult;
import io.vtown.WeiTangApp.event.receiver.NewMessageBroadcastReceiver;
import io.vtown.WeiTangApp.service.DownloadService;
import io.vtown.WeiTangApp.ui.title.ACenter;
import io.vtown.WeiTangApp.ui.title.AShopBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;
import com.jauker.widget.BadgeView;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import de.greenrobot.event.EventBus;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-11 下午2:40:44
 * 
 */
public class AMain extends TabActivity implements OnTabChangeListener {
	private Context BaseCotext;
	private TabHost tabHost;
	/**
	 * 五个tab对应的Layout
	 */

	private FrameLayout layout0, layout1, layout2, layout3, layout4;
	/**
	 * 五个tab对应的imagview
	 */
	private ImageView img0, img1, img2, img3, img4;
	/**
	 * 五个tab对应的文字
	 */
	private TextView text1, text2, text0, text3, text4;
	/**
	 * 标记tab的Tage
	 */
	private String[] TableMenu = { Constants.TableMenu1, Constants.TableMenu2,
			Constants.TableMenu5, Constants.TableMenu3, Constants.TableMenu4 };
	/**
	 * Tab view里面 item的容器
	 */
	private List<FrameLayout> TableViews = new ArrayList<FrameLayout>();
	private List<ImageView> TableImages = new ArrayList<ImageView>();
	private List<TextView> TableTexts = new ArrayList<TextView>();
	/**
	 * Tab 点击时候的表示
	 */
	private int[] nor_ivs = new int[] { R.drawable.tab1_nor,
			R.drawable.tab2_nor, R.drawable.tab_ask_nor, R.drawable.tab3_nor,
			R.drawable.tab4_nor };
	private int[] pr_ivs = new int[] { R.drawable.tab1_pr, R.drawable.tab2_pr,
			R.drawable.tab_ask_pre, R.drawable.tab3_pr, R.drawable.tab4_pr };
	/**
	 * Tab中 接受更改postion的通知
	 */
	private NewMessageBroadcastReceiver msgReceiver;
	/**
	 * 二次退出时候的时长标识
	 */
	private long exitTime = 0;
	/**
	 * 确定当前位置
	 */
	private int Postion = 0;

	private BUser mBUser;

	private IMUtile imUtile;

	private BadgeView ShopBadgeView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		InitJPush();
		AppManager.getAppManager().addActivity(this);
		BaseCotext = AMain.this;
		EventBus.getDefault().register(this, "ReciverChangTab", BMessage.class);
		mBUser = Spuit.User_Get(BaseCotext);
		setContentView(R.layout.activity_main);

		// 接收Im的广播'
		InitIm();
		InItTab();
		IShowDown();
		// 检测升级
		UpCheck();
	}

	/**
	 * 初次进来通知Show进行获取数据
	 */

	private void IShowDown() {
		// 通知偷偷下载show数据
		EventBus.getDefault()
				.post(new BMessage(BMessage.Tage_Main_To_ShowData));
		// 偷偷加载毛玻璃
		EventBus.getDefault().post(
				new BMessage(BMessage.Tage_Main_To_ShowGaoSi));
	}

	
	private void InitIm() {
		msgReceiver = new NewMessageBroadcastReceiver();
		imUtile = new IMUtile(msgReceiver, this);
		imUtile.Login(Constants.ImHost + mBUser.getSeller_id(),
				Constants.ImPasd);

	}

	/**
	 * 初始化Tab的原始数据
	 */
	private void InItTab() {
		ShopBadgeView = new BadgeView(BaseCotext);
		Postion = 0;
		InItTabDateView1();

		tabHost.addTab(tabHost.newTabSpec(TableMenu[0])
				.setIndicator(TableMenu[0])
				.setContent(new Intent(BaseCotext, ANewHome.class)));

		tabHost.addTab(tabHost.newTabSpec(TableMenu[1])
				.setIndicator(TableMenu[1])
				.setContent(new Intent(BaseCotext, AShop.class)));
		tabHost.addTab(tabHost.newTabSpec(TableMenu[2])
				.setIndicator(TableMenu[2])
				.setContent(new Intent(BaseCotext, AShow.class)));//
		tabHost.addTab(tabHost.newTabSpec(TableMenu[3])
				.setIndicator(TableMenu[3])
				.setContent(new Intent(BaseCotext, AShopBus.class)));

		tabHost.addTab(tabHost.newTabSpec(TableMenu[4])
				.setIndicator(TableMenu[4])
				.setContent(new Intent(BaseCotext, ACenter.class)));

		// tabHost.setOnTabChangedListener(this);
	}

	@SuppressWarnings("deprecation")
	private void InItTabDateView1() {

		tabHost = this.getTabHost();
		// Fraglayout集合
		layout0 = (FrameLayout) findViewById(R.id.tab_frame0);
		layout1 = (FrameLayout) findViewById(R.id.tab_frame1);
		layout2 = (FrameLayout) findViewById(R.id.tab_frame2);
		layout3 = (FrameLayout) findViewById(R.id.tab_frame3);
		layout4 = (FrameLayout) findViewById(R.id.tab_frame4);
		// 图片的初始化
		img0 = (ImageView) findViewById(R.id.tab_iv0);
		img1 = (ImageView) findViewById(R.id.tab_iv1);
		img2 = (ImageView) findViewById(R.id.tab_iv2);
		img3 = (ImageView) findViewById(R.id.tab_iv3);
		img4 = (ImageView) findViewById(R.id.tab_iv4);
		// 文字的初始化
		text0 = (TextView) findViewById(R.id.tab_txt0);
		text1 = (TextView) findViewById(R.id.tab_txt1);
		text2 = (TextView) findViewById(R.id.tab_txt2);
		text3 = (TextView) findViewById(R.id.tab_txt3);
		text4 = (TextView) findViewById(R.id.tab_txt4);
		// framlayout的集合
		TableViews.add(layout0);
		TableViews.add(layout1);
		TableViews.add(layout2);
		TableViews.add(layout3);
		TableViews.add(layout4);
		// 图片集合
		TableImages.add(img0);
		TableImages.add(img1);
		TableImages.add(img2);
		TableImages.add(img3);
		TableImages.add(img4);
		// 文字集合
		TableTexts.add(text0);
		TableTexts.add(text1);
		TableTexts.add(text2);
		TableTexts.add(text3);
		TableTexts.add(text4);
		// 设置监听事件
		for (int i = 0; i < 5; i++) {
			TableViews.get(i).setOnClickListener(new TabClickLisener(i));
		}
	}

	class TabClickLisener implements OnClickListener {
		int postionTage;

		public TabClickLisener(int postionTage) {
			super();
			this.postionTage = postionTage;
		}

		@Override
		public void onClick(View arg0) {
			tabHost.setCurrentTabByTag(TableMenu[postionTage]);
			ChangeTabBg(TableMenu[postionTage]);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			EventBus.getDefault().unregister(this);
		} catch (Exception e) {
		}
	}

	@Override
	public void onTabChanged(String tabId) {

		// ChangeTabBg(tabId);
	}

	/**
	 * 变换卡片时候需要变换选项卡片的图片颜色和
	 */

	public void ChangeTabBg(String tabid) {
		// if (tabid.equals(Constants.TableMenu3))
		// ShopBadgeView.setBadgeCount(0);

		for (int i = 0; i < 5; i++) {
			if (TableMenu[i].equals(tabid)) {
				// mBaseApplication.setTabPostion(i);
				// tabHost.setCurrentTabByTag(TableMenu[i]);
				TableTexts.get(i).setTextColor(
						getResources().getColor(R.color.tab_text_pr));
				TableImages.get(i).setImageResource(pr_ivs[i]);

			} else {
				TableTexts.get(i).setTextColor(
						getResources().getColor(R.color.black));
				TableImages.get(i).setImageResource(nor_ivs[i]);

			}
		}
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) { // 返回键
			// Sputis.SavaTabPostion(BaseCotext, 0);
			// Sputis.SecurityLoginOut(BaseCotext);

			if ((System.currentTimeMillis() - exitTime) > 2000) {
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();

				exitTime = System.currentTimeMillis();
			} else {
				// Sputis.SaveFundListCondition(this, 0);
				// Sputis.SaveFundListSort(this, 1);
				AppManager.getAppManager().AppExit(BaseCotext);
				finish();
				System.exit(0);
			}

			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	public void ReciverChangTab(BMessage bMessage) {
		switch (bMessage.getMessageType()) {
		case BMessage.Tage_Tab_one:// 首页
			tabHost.setCurrentTabByTag(TableMenu[0]);
			ChangeTabBg(TableMenu[0]);
			break;
		case BMessage.Tage_Tab_two:// 商铺
			tabHost.setCurrentTabByTag(TableMenu[1]);
			ChangeTabBg(TableMenu[1]);
			break;
		case BMessage.Tage_Tab_three:// show
			tabHost.setCurrentTabByTag(TableMenu[2]);
			ChangeTabBg(TableMenu[2]);

			break;
		case BMessage.Tage_Tab_four:// shopbus
			tabHost.setCurrentTabByTag(TableMenu[3]);
			ChangeTabBg(TableMenu[3]);

			break;
		case BMessage.Tage_Tab_five:// center
			tabHost.setCurrentTabByTag(TableMenu[4]);
			ChangeTabBg(TableMenu[4]);
			break;
		case BMessage.Tage_Tab_Im_Regist:// 当不处于聊天页面时候就开始注册
			imUtile.ImLister_Regist();
			// unregisterReceiver(msgReceiver);
			break;
		case BMessage.Tage_Tab_Im_UnRegist:// 当处于聊天页面时候就取消注册
			imUtile.ImLister_UnRegist();
			// unregisterReceiver(msgReceiver);
			// PromptManager.ShowCustomToast(BaseCotext, "注销广播");
			break;
		case BMessage.Tage_Tab_ShopBus:
			// img3

			ShopBadgeView.setTargetView(img3);
			// ShopBadgeView.setBadgeCount(5);
			ShopBadgeView.setBadgeCount(bMessage.getTabShopBusNumber());

			// ShopBadgeView.setBackgroundColor(getResources().getColor(
			// R.color.app_fen));
			break;
		default:
			break;
		}
	}

	private void UpCheck() {
		NHttpBaseStr mBaseStr = new NHttpBaseStr(BaseCotext);
		mBaseStr.setPostResult(new IHttpResult<String>() {

			@Override
			public void onError(String error, int LoadType) {
				LogUtils.i(error);
			}

			@Override
			public void getResult(int Code, String Msg, String Data) {
				if (Code != 200 || StrUtils.isEmpty(Data)) {
					return;
				}
				BUpData data = JSON.parseObject(Data, BUpData.class);
				if (data.getCode() > Constants.getVersionCode(BaseCotext)) {// 需要升级

					// status 1强制升级2不强制升级
					switch (data.getStatus()) {
					case 1:// 强制升级
						UpdateManager m = new UpdateManager(BaseCotext, data
								.getUrl(), data.getDesc(), data.getVersion());// "产品进行了优化\n部分功能进行升级"
						m.UpDown();
						break;
					case 2:// 不强制升级
						ShowCustomDialog(data);
						break;
					default:
						break;
					}

				} else {// 不需要升级
					return;
				}

			}
		});
		HashMap<String, String> map = new HashMap<String, String>();

		// map.put("sellerid", mBUser.getSeller_id());
		mBaseStr.getData(Constants.UpData, map, Method.GET);

	}

	/**
	 * 左右选择弹出框的封装
	 */

	public void ShowCustomDialog(final BUpData data) {
		final CustomDialog dialog = new CustomDialog(BaseCotext,
				R.style.mystyle, R.layout.dialog_purchase_cancel, 1,
				getResources().getString(R.string.hulie_version),
				getResources().getString(R.string.updown_version));
		dialog.show();
		dialog.setTitleText(getResources()
				.getString(R.string.check_new_version));
		dialog.Settitles(getResources().getString(R.string.new_version)
				+ data.getVersion() + "\n" + data.getDesc());

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
				Intent mIntent = new Intent(AMain.this, DownloadService.class);
				mIntent.putExtra(DownloadService.INTENT_URL, data.getUrl());
				mIntent.putExtra(DownloadService.Desc, data.getDesc());
				startService(mIntent);

			}
		});
	}

}
