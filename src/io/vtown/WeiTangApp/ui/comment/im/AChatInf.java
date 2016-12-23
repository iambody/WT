package io.vtown.WeiTangApp.ui.comment.im;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcache.BShop;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.fragment.main.FMainNew;
import io.vtown.WeiTangApp.ui.ABase;
import io.vtown.WeiTangApp.ui.comment.AphotoPager;
import io.vtown.WeiTangApp.ui.comment.im.MyImListView.OnRefreshListener;

import java.io.File;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.PathUtil;
import com.easemob.util.VoiceRecorder;

import de.greenrobot.event.EventBus;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-28 下午5:55:38
 * 
 */
public class AChatInf extends ABase implements OnClickListener {
	private FinalBitmap fb;
	private File cameraFile;// 拍照需要使用到文件
	private static final int take_pictures = 0;// 拍照请求码
	private static final int photo_album = 1;// 相册请求码
	private TextView chatName;
	private MyImListView chat_listview;
	private ImageView plus;// 加号
	private TextView record; // 录音文本转换按钮
	private Button recording;// 录音按钮
	private EditText edittv;// 文本输入框
	private Button send;// 发送按钮
	private ChatAdapter adapter;
	private String chatname;// 聊天的对象 ,从上个页面传过来的
	private String startMsgId;// 获取聊天记录时的第一条信息id
	private boolean message_more = true;// 聊天记录是否还有更多
	private int messageCount;// 记录信息条数，用来判断是否还有更多信息
	private EMConversation conversation; // 整个会话对象
	private VoiceRecorder voiceRecorder;// 环信封装的录音功能类
	private MediaPlayer mPlayer = null;// 播放语音的对象（播放器）
	private boolean isPlaying;// 是否正在播放状态
	private String playMsgId = null;// 正在播放的语音信息id,用于判断播放的是哪一个语音信息
	private ImageView playIv; // 正在播放的语音iv
	private View recordingContainer;// 录音时所弹出的提示
	private ImageView micImage;// 录音时所弹出的提示的语音动画iv
	private TextView recordingHint; // 录音时所弹出的提示里的提示语
	// NewMessageBroadcastReceiver msgReceiver = new
	// NewMessageBroadcastReceiver();
	MessageBroadcastReceiver msgReceiver = new MessageBroadcastReceiver();

	private AnimationDrawable voiceAnimation = null;// 语音图片动画

	// 赋值粘贴文本
	private ClipData myClip;
	private ClipboardManager myClipboard;
	// 扩展布局
	private LinearLayout chatinf_exater_lay;

	/**
	 * 扩展布局的图片选择
	 */
	private View chart_inf_pic;
	/**
	 * 视频
	 */
	private View chart_inf_pho;

	private boolean IsShowDown = false;

	/**
	 * 我的信息
	 * 
	 * @param msgId
	 */
	private BUser mBUser;
	/**
	 * 我的shop信息
	 * 
	 * @param msgId
	 */
	private BShop mBShop;
	private String Title = "";
	private String TagerAvater = "";

	private boolean IsHelper = false;// ishepler


	class MessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 消息id
			String msgId = intent.getStringExtra("msgid");
			// 发消息的人的username(userid)
			String msgFrom = intent.getStringExtra("from");
			// 消息类型，文本，图片，语音消息等,这里返回的值为msg.type.ordinal()。
			// 所以消息type实际为是enum类型
			int msgType = intent.getIntExtra("type", 0);
			Log.d("main", "new message id:" + msgId + " from:" + msgFrom
					+ " type:" + msgType);
			// 更方便的方法是通过msgId直接获取整个message
			// EMMessage message =
			// EMChatManager.getInstance().getMessage(msgId);

			// if (msgFrom.equals(chatname) && adapter != null) {//
			// 如果发消息的人就是当前聊天的人
			// conversation = EMChatManager.getInstance().getConversation(
			// msgFrom);
			// adapter.notifyDataSetChanged();// 刷新适配器
			// chat_listview.setSelection(chat_listview.getBottom());// 显示到最后一条
			// }
			GetMessag(msgFrom);
		}
	}

	private void GetMessag(String msgId) {
		if (msgId.equals(chatname) && adapter != null) {// 如果发消息的人就是当前聊天的人
			conversation = EMChatManager.getInstance().getConversation(msgId);
			adapter.notifyDataSetChanged();// 刷新适配器
			chat_listview.setSelection(chat_listview.getBottom());// 显示到最后一条
		} else {
			conversation = EMChatManager.getInstance().getConversation(msgId);
			adapter.notifyDataSetChanged();// 刷新适配器
			chat_listview.setSelection(chat_listview.getBottom());// 显示到最后一条
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// EventBus.getDefault().register(this, "GetImMessage", BMessage.class);
		InItBase();

	}

	// public void GetImMessage(BMessage message) {
	// if (message.getMessageType() == BMessage.Tage_IM_Notic) {
	// GetMessag(message.getFromImId());
	// }
	// }

	// 初始化
	private void InItBase() {
		// 注册广播
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(3);
		registerReceiver(msgReceiver, intentFilter);
		EMChat.getInstance().setAppInited();// sdk才会发送新消息的广播
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 请求没有标题栏样式
		setContentView(R.layout.chat_chatinf);
		mBUser = Spuit.User_Get(BaseContext);
		mBShop = Spuit.Shop_Get(BaseContext);
		chatName = (TextView) findViewById(R.id.chatName);
		chat_listview = (MyImListView) findViewById(R.id.chat_listview);
		plus = (ImageView) findViewById(R.id.plus);
		edittv = (EditText) findViewById(R.id.edittv);
		send = (Button) findViewById(R.id.send);
		chatinf_exater_lay = (LinearLayout) findViewById(R.id.chatinf_exater_lay);
		chart_inf_pic = findViewById(R.id.chart_inf_pic);
		chart_inf_pic.setOnClickListener(this);
		chart_inf_pho = findViewById(R.id.chart_inf_pho);
		chart_inf_pho.setOnClickListener(this);
		record = (TextView) findViewById(R.id.record);
		recording = (Button) findViewById(R.id.recording);
		fb = FinalBitmap.create(BaseContext);
		fb.configLoadfailImage(R.drawable.chat_default_image);
		fb.configLoadingImage(R.drawable.chat_default_image);
		// 获取intent的扩展字段
		chatname = getIntent().getStringExtra("tagname");
		Title = getIntent().getStringExtra("title");
		TagerAvater = getIntent().getStringExtra("iv");
		IsHelper = getIntent().getBooleanExtra("ishepler", false);

		chatName.setText(Title);
		recordingContainer = findViewById(R.id.recording_container);
		recordingHint = (TextView) findViewById(R.id.recording_hint);
		micImage = (ImageView) findViewById(R.id.mic_image);
		myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		// 动画资源文件,用于录制语音时
		final Drawable[] micImages = new Drawable[] {
				getResources().getDrawable(R.drawable.record_animate_01),
				getResources().getDrawable(R.drawable.record_animate_02),
				getResources().getDrawable(R.drawable.record_animate_03),
				getResources().getDrawable(R.drawable.record_animate_04),
				getResources().getDrawable(R.drawable.record_animate_05), };
		voiceRecorder = new VoiceRecorder(new Handler() {
			@Override
			public void handleMessage(android.os.Message msg) {
				// 切换msg切换图片
				micImage.setImageDrawable(micImages[msg.what]); // 录音时的图片
			}
		});

		conversation = EMChatManager.getInstance().getConversation(chatname); // 获取整个会话
		messageCount = conversation.getAllMsgCount(); // 设置记录信息条数
		if (messageCount != 0)
			startMsgId = conversation.getMessage(0).getMsgId();// 设置第一条信息的id
		adapter = new ChatAdapter(conversation.getAllMessages());
		chat_listview.setAdapter(adapter);
		chat_listview.setSelection(conversation.getAllMsgCount() - 1);// 显示到最后一条

		chat_listview.setonRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {

				new Thread(new LoadMoreMsgRun()).start();// 加载聊天记录
			}
		});
		record.setOnClickListener(this);
		plus.setOnClickListener(this);
		send.setOnClickListener(this);

		// 录音按钮触摸监听
		recording.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {

				if (MotionEvent.ACTION_DOWN == arg1.getAction()) { // 按下
					if (!android.os.Environment.getExternalStorageState()
							.equals(android.os.Environment.MEDIA_MOUNTED)) {
						toast("内存卡不存在");
						return false;
					}
					arg0.setPressed(true);
					if (isPlaying)
						stopPlayVoice();
					recordingContainer.setVisibility(View.VISIBLE);
					recordingHint.setText("手指上滑，取消发送");
					recordingHint.setBackgroundColor(Color.TRANSPARENT);

					try { // 环信方法
						voiceRecorder.startRecording(null, chatname,
								getApplicationContext());
					} catch (Exception e) {
						if (voiceRecorder != null)
							voiceRecorder.discardRecording();
						recordingContainer.setVisibility(View.INVISIBLE);
						toast("录音失败，请重试");
						arg0.setPressed(false);
						e.printStackTrace();
					}

				} else if (MotionEvent.ACTION_MOVE == arg1.getAction()) {
					if (arg1.getY() < 0) {
						recordingHint.setText("松开手指，取消发送");
						recordingHint
								.setBackgroundResource(R.drawable.recording_text_hint_bg);
					} else {
						recordingHint.setText("手指上滑，取消发送");
						recordingHint.setBackgroundColor(Color.TRANSPARENT);
					}
					return true;
				} else if (MotionEvent.ACTION_UP == arg1.getAction()) {// 抬起
					arg0.setPressed(false);
					recordingContainer.setVisibility(View.INVISIBLE);

					if (arg1.getY() < 0) { // 如果是已经上滑了后
						voiceRecorder.discardRecording(); // 丢弃录音
						return true;
					}
					try {
						int length = voiceRecorder.stopRecoding(); // 停止录音
						if (length > 0) {
							sendMessage(EMMessage.Type.VOICE, null,
									voiceRecorder.getVoiceFilePath(), length);// 发送语音
						} else if (length == EMError.INVALID_FILE) {
							toast("录音失败");
						} else {
							toast("录音时间太短");
						}
					} catch (Exception e) {
						toast("语音异常");
					}
				}
				return false;
			}
		});

		// 设置扩展view图片
		SetCommentIV("图库", R.drawable.chart_pic, chart_inf_pic);
		SetCommentIV("拍照", R.drawable.chart_pho, chart_inf_pho);

		EventBus.getDefault().post(new BMessage(BMessage.Tage_Tab_Im_UnRegist));
	}

	// 加载聊天记录
	class LoadMoreMsgRun implements Runnable {
		public void run() {
			// 判断是否还有更多
			if (message_more == true) {
				// 获取startMsgId之前的pagesize条消息，此方法获取的messages
				// sdk会自动存入到此会话中，app中无需再次把获取到的messages添加到会话中
				// List<EMMessage> messages =
				// System.out.println("加载前" + conversation.getMsgCount() +
				// "信息条");
				// System.out.println("加载前" + messageCount);

				conversation.loadMoreMsgFromDB(startMsgId, 10);// 加载更多记录

				// System.out.println("加载后" + conversation.getMsgCount() +
				// "信息条");

				if (conversation.getMsgCount() > messageCount) {// 表示获取到更多信息了

					loadFinish(1, conversation.getMsgCount() - messageCount);// 重新设置适配器
					messageCount = conversation.getMsgCount();
					startMsgId = conversation.getMessage(0).getMsgId();// 设置第一条信息的id

				} else { // 没有获取到
					loadFinish(2, 0); // 提示没有更多
					message_more = false;// 设置没有更多标记
				}
			} else if (message_more == false) {
				loadFinish(2, 0); // 提示没有更多
			}

			loadFinish(3, 0);// lisetview下拉刷新完成
		}
	}

	// 加载完成后的ui处理
	private void loadFinish(final int tag, final int index) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (tag == 1) {
					// 重新设置适配器
					chat_listview.setAdapter(adapter);
					chat_listview.setSelection(index);
				} else if (tag == 2)
					toast("没有更多记录了");
				else if (tag == 3)
					chat_listview.onRefreshComplete(); // 刷新完成
			}
		});
	}

	public void SetCommentIV(String title, int IvRource, View V) {
		ImageView viessw;
		((ImageView) V.findViewById(R.id.comment_pictxt_iv))
				.setBackgroundResource(IvRource);
		((TextView) V.findViewById(R.id.comment_pictxt_txt)).setText(title);
		V.setOnClickListener(this);

	}

	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		// return sdDir.toString();
		return sdDir.getPath();
	}

	private void paiZhao() {// 调用系统相机进行拍照：

		cameraFile = new File(PathUtil.getInstance().getImagePath(),// 环信获取图片路径的方法
				System.currentTimeMillis() + ".jpg");
		cameraFile.getParentFile().mkdirs();

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// action is
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile));
		startActivityForResult(intent, take_pictures);//

		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	private void xiangChe() { // 调用系统相册

		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(intent, photo_album);

		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != Activity.RESULT_OK)
			return;
		switch (requestCode) {
		case take_pictures: // 拍照返回
			// plus.setImageURI(imageUri);
			sendMessage(EMMessage.Type.IMAGE, null,
					cameraFile.getAbsolutePath(), 0);
			break;
		case photo_album: // 相册返回
			Uri uri = data.getData(); // 获取图片的uri
			// 将uri转成路径
			Cursor cursor = getContentResolver().query(uri, null, null, null,
					null);
			if (cursor != null) {
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex("_data");
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				cursor = null;

				if (picturePath == null || picturePath.equals("null")) {
					toast("图片未找到到");
					return;
				}
				sendMessage(EMMessage.Type.IMAGE, null, picturePath, 0);// 调用发送图片消息的方法'

			} else {
				File file = new File(uri.getPath());
				if (!file.exists()) {
					toast("图片未找到到");
					return;
				}
				sendMessage(EMMessage.Type.IMAGE, null, file.getAbsolutePath(),
						0);// 调用发送图片消息的方法
			}

			System.out.println("uri: " + uri.toString());
		}
	}

	private void sendMessage(EMMessage.Type type, String text, String filePath,
			int length) {
		// 获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
		EMConversation conversation = EMChatManager.getInstance()
				.getConversation(chatname);

		EMMessage message = null;
		if (type == EMMessage.Type.IMAGE) {
			// 创建一条图片消息
			message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);

			// 设置消息body
			ImageMessageBody body = new ImageMessageBody(new File(filePath));
			// 默认超过100k的图片会压缩后发给对方，可以设置成发送原图
			// body.setSendOriginalImage(true);
			message.addBody(body);
			try {
				// 增加自己特定的属性，目前SDK支持int、boolean、String这三种属性，可以设置多个扩展属性
				message.setAttribute("extSendNickname", mBUser.getSeller_name());
				message.setAttribute("extReceiveNickname", Title);
				message.setAttribute("extSendHeadUrl", mBUser.getHead_img());// TagerAvater
				message.setAttribute("extReceiveHeadUrl", TagerAvater);
			} catch (Exception e) {

			}
		} else if (type == EMMessage.Type.TXT) {
			// 创建一条文本消息
			message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			// 设置消息body
			TextMessageBody body = new TextMessageBody(text);
			message.addBody(body);
		} else if (type == EMMessage.Type.VOICE) { // 如果是语音
			message = EMMessage.createSendMessage(EMMessage.Type.VOICE);
			VoiceMessageBody body = new VoiceMessageBody(new File(filePath),
					length);
			message.addBody(body);
		}

		// 如果是群聊，设置chattype,默认是单聊
		// message.setChatType(ChatType.GroupChat);

		message.setReceipt(chatname);// 设置接收人
		conversation.addMessage(message);// 把消息加入到此会话对象中

		adapter.notifyDataSetChanged();// 刷新适配器
		chat_listview.setSelection(chat_listview.getBottom());// 显示到最后一条
		EMChatManager.getInstance().sendMessage(message, new EMCallBack() {
			@Override
			public void onError(int arg0, final String arg1) {
				runOnUiThread(new Runnable() {
					public void run() {
						toast("发送失败：" + arg1);
						adapter.notifyDataSetChanged();// 刷新适配器
					}
				});
			}

			@Override
			public void onProgress(int arg0, String arg1) {
			}

			@Override
			public void onSuccess() {
				runOnUiThread(new Runnable() {
					public void run() {
						adapter.notifyDataSetChanged();// 刷新适配器
					}
				});
			}
		});
	}

	class ChatAdapter extends BaseAdapter {
		List<EMMessage> emmessage;

		ChatAdapter(List<EMMessage> emmessage) {
			this.emmessage = emmessage;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return emmessage.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return emmessage.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			// EMChatManager.getInstance().getCurrentUser();
			final EMMessage message = emmessage.get(arg0); // 每条消息
			// if(!message.getFrom().equals(message.getUserName()))
			if (!EMChatManager.getInstance().getCurrentUser()
					.equals(message.getFrom())) // 对方发的消息
			{
				arg1 = getLayoutInflater().inflate(R.layout.chat_item1, null);
				ImageView otherhead = (CircleImageView) arg1
						.findViewById(R.id.iv);
				ImageLoaderUtil.Load2(TagerAvater, otherhead,
						R.drawable.chat_default_head);
			} else
				// 自己发的消息
				arg1 = getLayoutInflater().inflate(R.layout.chat_item2, null);
			final ProgressBar pb = (ProgressBar) arg1.findViewById(R.id.pb);// 转圈
			final TextView fail = (TextView) arg1.findViewById(R.id.fail);// 感叹号
			TextView time = (TextView) arg1.findViewById(R.id.time);
			TextView tv = (TextView) arg1.findViewById(R.id.tv);
			final ImageView image = (ImageView) arg1.findViewById(R.id.image);
			// image.setMaxWidth(Data.getInstance().getLayout_width()/2);
			CircleImageView OtherHead = (CircleImageView) arg1
					.findViewById(R.id.iv);

			long msgTime = message.getMsgTime(); // 信息时间
			if (arg0 == 0
					|| msgTime - emmessage.get(arg0 - 1).getMsgTime() > 120000) {
				time.setVisibility(View.VISIBLE);

				long dateTaken = System.currentTimeMillis(); // 当前系统时间
				String currentDate = (String) DateFormat.format("yyyy/MM/dd",
						dateTaken); // 当前系统日期
				String messageDate = (String) DateFormat.format("yyyy/MM/dd",
						msgTime); // 信息的日期
				if (currentDate.equals(messageDate)) // 如果是当天

					time.setText(DateFormat.format("kk:mm:ss", msgTime));// 只显示时间
				else
					time.setText(DateFormat.format("yyyy/MM/dd kk:mm:ss",// 显示日期加时间
							msgTime));
			}

			try {
				if (!EMChatManager.getInstance().getCurrentUser()
						.equals(message.getFrom())) {// 对方
					if (IsHelper) {// 微糖小助手！！！！！！！！！
						ImageLoaderUtil.Load2(Constants.QiNiuHost
								+ "VTownServiceTeamProfile.jpg", OtherHead,
								R.drawable.chat_default_head);
					} else
						ImageLoaderUtil.Load2(
								message.getStringAttribute("extSendHeadUrl"),
								OtherHead, R.drawable.chat_default_head);

				} else {// 自己
					ImageLoaderUtil.Load2(mBUser.getHead_img(), OtherHead,
							R.drawable.chat_default_head);

				}
			} catch (EaseMobException e) {
				e.printStackTrace();
			}

			if (message.getType() == EMMessage.Type.TXT) { // 如果是文本

				final String mess = ((TextMessageBody) message.getBody())
						.getMessage();
				tv.setText(mess);
				tv.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {

						myClip = ClipData.newPlainText("text", mess);
						myClipboard.setPrimaryClip(myClip);
						PromptManager.ShowCustomToast(BaseContext, "已复制");
						return true;
					}
				});

			} else if (message.getType() == EMMessage.Type.VOICE) {// 如果是语音
				tv.setBackgroundColor(getResources().getColor(
						R.color.transparent));
				tv.setGravity(Gravity.TOP);
				tv.setTextColor(getResources().getColor(R.color.gray));
				tv.setText(((VoiceMessageBody) message.getBody()).getLength()
						+ "”"); // 设置语音的时长
				image.setVisibility(View.VISIBLE);// 图片控件显示
				// image.setPadding(10, 5, 5, 5);
				// 设置为语音的图片
				if (message.direct == EMMessage.Direct.RECEIVE) {
					image.setImageResource(R.drawable.chatfrom_voice_playing);
				} else {
					image.setImageResource(R.drawable.chatto_voice_playing);
				}
				image.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// 开始播放录音
						startPlay(message, image);
					}
				});

			} else if (message.getType() == EMMessage.Type.IMAGE) {// 如果是图片
				tv.setVisibility(View.GONE);
				// fail.setPadding(50, 0, 0, 0);
				image.setVisibility(View.VISIBLE);// 图片控件显示

				if (message.getFrom().equals(chatname)) // 对方发的消息
				{
					String ThumbnailUrl = ((ImageMessageBody) message.getBody())
							.getThumbnailUrl(); // 获取缩略图片地址
					String thumbnailPath = ImageUtils
							.getThumbnailImagePath(ThumbnailUrl);
					// Bitmap bitmap =
					// ImageCache.getInstance().get(thumbnailPath);
					fb.display(image, ThumbnailUrl); // 显示图片
					// ImageLoaderUtil.Load2(thumbnailPath, image,
					// R.drawable.error_iv2);
					String imageRemoteUrl = ((ImageMessageBody) message
							.getBody()).getRemoteUrl();// 获取远程原图片地址

					// imageClick(image, imageRemoteUrl);
					imageClick(image, ThumbnailUrl);

				} else {
					// 自己发的消息
					String LocalUrl = ((ImageMessageBody) message.getBody())
							.getLocalUrl(); // 获取本地图片地址
					Bitmap bm = com.easemob.util.ImageUtils.decodeScaleImage(
							LocalUrl, 160, 160);// 获取缩略图片
					image.setImageBitmap(bm);// 显示图片
					imageClickss(image, LocalUrl);

					// if (!StrUtils.isEmpty(Spuit.Shop_Get(BaseActivity)
					// .getAvatar())) {
					// ImageLoaderUtil.Load2(Spuit.Shop_Get(BaseActivity)
					// .getAvatar(), MyHead,
					// R.drawable.chat_default_image);
					// } else {
					// MyHead.setImageResource(R.drawable.chat_default_image);
					// }

				}
			}
			if (message.status == EMMessage.Status.INPROGRESS) { // 发送中
				if (pb != null)
					pb.setVisibility(View.VISIBLE);
			} else if (message.status == EMMessage.Status.SUCCESS) {// 成功
				if (pb != null)
					pb.setVisibility(View.GONE);
			} else if (message.status == EMMessage.Status.FAIL) {// 失败
				if (pb != null)
					pb.setVisibility(View.GONE);
				if (fail != null)
					fail.setVisibility(View.VISIBLE);
			}

			return arg1;
		}

		// 图片点击监听
		private void imageClick(ImageView image, final String imageUrl) {
			image.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					// new PopWindow_Image(BaseActivity,
					// imageUrl).showAtLocation(
					// arg0, 0, 0, 0);
					String a[] = { imageUrl };
					Intent mIntent = new Intent(BaseContext, AphotoPager.class);
					mIntent.putExtra("position", 0);
					mIntent.putExtra("urls", a);
					PromptManager.SkipActivity(BaseActivity, mIntent);

				}
			});
		}

		// 图片点击监听
		private void imageClickss(ImageView image, final String imageUrl) {
			image.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {

					new PopWindow_Image(BaseActivity, imageUrl).showAtLocation(
							arg0, 0, 0, 0);
					// String a[] = { imageUrl };
					// Intent mIntent = new Intent(BaseContext,
					// AphotoPager.class);
					// mIntent.putExtra("position", 0);
					// mIntent.putExtra("urls", a);
					// PromptManager.SkipActivity(BaseActivity, mIntent);

				}
			});
		}
	}

	// 点击图片后显示大图的弹出框
	class PopWindow_Image extends PopupWindow {

		public PopWindow_Image(final Activity context, String imageUrl) {
			LayoutInflater layout = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = layout.inflate(R.layout.chat_pic, null);
			ImageView iv = (ImageView) view.findViewById(R.id.iv);
			fb.display(iv, imageUrl);
			// 设置SelectPicPopupWindow的View
			setContentView(view);
			// 设置SelectPicPopupWindow弹出窗体的宽
			this.setWidth(LayoutParams.MATCH_PARENT);
			this.setHeight(LayoutParams.MATCH_PARENT);
			// 设置SelectPicPopupWindow弹出窗体可点击
			this.setFocusable(true);
			this.setOutsideTouchable(true);
			// 刷新状态
			this.update();
			// 实例化一个ColorDrawable颜色为半透明
			ColorDrawable drawable = new ColorDrawable(00000000);
			// 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
			this.setBackgroundDrawable(drawable);

			this.setAnimationStyle(R.style.AnimatioPop);

		}
	}

	private void toast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	// 开始播放
	private void startPlay(final EMMessage message, ImageView image) {

		VoiceMessageBody voiceBody = (VoiceMessageBody) message.getBody();
		if (isPlaying) {
			stopPlayVoice();
			if (playMsgId != null && (playMsgId.equals(message.getMsgId())))
				return;
		}

		if (message.direct == EMMessage.Direct.SEND) {
			// for sent msg, we will try to play the voice file directly
			playVoice(voiceBody.getLocalUrl(), message, image);
		} else {
			if (message.status == EMMessage.Status.SUCCESS) {
				playVoice(voiceBody.getLocalUrl(), message, image);

			} else if (message.status == EMMessage.Status.INPROGRESS) {
				toast("信息还在发送中");
			} else if (message.status == EMMessage.Status.FAIL) {
				toast("接收失败");
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						EMChatManager.getInstance().asyncFetchMessage(message);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						adapter.notifyDataSetChanged();
					}
				}.execute();
			}
		}
	}

	// 播放录音
	public void playVoice(String filePath, final EMMessage message,
			final ImageView image) {
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			System.err.println("file not exist");
			toast("语音文件不存在");
			return;
		}
		playMsgId = message.getMsgId();
		mPlayer = new MediaPlayer();

		// AudioManager audioManager = (AudioManager)
		// getSystemService(Context.AUDIO_SERVICE);
		// if (HXSDKHelper.getInstance().getModel().getSettingMsgSpeaker()) {
		// audioManager.setMode(AudioManager.MODE_NORMAL);
		// audioManager.setSpeakerphoneOn(true);
		// mPlayer.setAudioStreamType(AudioManager.STREAM_RING);
		// } else {
		// audioManager.setSpeakerphoneOn(false);// 关闭扬声器
		// // 把声音设定成Earpiece（听筒）出来，设定为正在通话中
		// audioManager.setMode(AudioManager.MODE_IN_CALL);
		// mPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
		// }
		try {
			mPlayer.setDataSource(filePath);
			mPlayer.prepare();
			mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					if (mPlayer == null) // 表示因为要播放其他语音时已经被停止了,所以不需要再次调用停止
						return;
					stopPlayVoice(); // stop animation
				}
			});
			isPlaying = true;
			mPlayer.start();
			playIv = image;
			showAnimation(message, image);

		} catch (Exception e) {
		}
	}

	private void showAnimation(EMMessage message, ImageView voiceIconView) {
//		// play voice, and start animation
//		if (message.direct == EMMessage.Direct.RECEIVE) {
//			voiceIconView.setImageResource(R.anim.voice_from_icon);
//		} else {
//			voiceIconView.setImageResource(R.anim.voice_to_icon);
//		}
//		voiceAnimation = (AnimationDrawable) voiceIconView.getDrawable();
//		voiceAnimation.start();
	}

	// 停止播放录音
	public void stopPlayVoice() {
		voiceAnimation.stop();
		EMMessage playMessage = EMChatManager.getInstance().getMessage(
				playMsgId);// 通过信息id获取信息
		if (playMessage.direct == EMMessage.Direct.RECEIVE) {
			playIv.setImageResource(R.drawable.chatfrom_voice_playing);
		} else {
			playIv.setImageResource(R.drawable.chatto_voice_playing);
		}
		// stop play voice
		mPlayer.setOnCompletionListener(null);// 以免调用了stop后走完成监听，而导致重复走当前函数
		mPlayer.stop();
		mPlayer.release();
		mPlayer = null;

		isPlaying = false;
		// adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.chart_inf_pho:// 拍照
			paiZhao();
			IsShowDown = !IsShowDown;
			chatinf_exater_lay.setVisibility(IsShowDown ? View.VISIBLE
					: View.GONE);
			break;
		case R.id.chart_inf_pic:// 选择图片
			xiangChe();
			IsShowDown = !IsShowDown;
			chatinf_exater_lay.setVisibility(IsShowDown ? View.VISIBLE
					: View.GONE);

			break;
		case R.id.plus:// 加号
			IsShowDown = !IsShowDown;
			chatinf_exater_lay.setVisibility(IsShowDown ? View.VISIBLE
					: View.GONE);

			break;
		case R.id.send:// 发送按钮

			String content = edittv.getText().toString().trim();
			if (content.length() == 0) {
				toast("请输入消息内容");
				return;
			}

			// sendMessage(EMMessage.Type.TXT, content, null, 0);
			// 修改发送方式
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			TextMessageBody txtBody = new TextMessageBody(content);
			String msgId = message.getMsgId();

			try {
				// 增加自己特定的属性，目前SDK支持int、boolean、String这三种属性，可以设置多个扩展属性
				message.setAttribute("extSendNickname", mBUser.getSeller_name());
				message.setAttribute("extReceiveNickname", Title);
				message.setAttribute("extSendHeadUrl", mBUser.getHead_img());// TagerAvater
				message.setAttribute("extReceiveHeadUrl", TagerAvater);


			} catch (Exception e) {
				LogUtils.i(e.toString());
			}
			message.setReceipt(chatname);
			message.addBody(txtBody);
			conversation.addMessage(message);

			try {
				EMChatManager.getInstance().sendMessage(message);


			} catch (EaseMobException e) {
				e.printStackTrace();
			}
			// 修改发送方式
			edittv.setText("");

			EventBus.getDefault().post(new BMessage(11186));
			hintKbTwo();

			chat_listview.setSelection(chat_listview.getBottom());
			break;
		case R.id.record:// 录音文本转换按钮
			if (record.getText().equals("录音")) { // 表示当前为文本模式，改为录音模式
				record.setText("文本");
				edittv.setVisibility(View.GONE);// 文本输入框隐藏
				recording.setVisibility(View.VISIBLE);// 录音按钮显示
				hideKeyboard();// 隐藏软键盘
			} else {
				record.setText("录音");
				recording.setVisibility(View.GONE);// 录音按钮隐藏
				edittv.setVisibility(View.VISIBLE);// 文本输入框显示
			}
			break;
		}
	}

	/**
	 * 隐藏软键盘
	 */
	private InputMethodManager manager;

	private void hideKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		try {
			// 停止语音播放
			if (mPlayer.isPlaying())
				stopPlayVoice();
			// 停止录音
			if (voiceRecorder.isRecording()) {
				voiceRecorder.discardRecording();
				recordingContainer.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e) {
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			EventBus.getDefault().post(
					new BMessage(BMessage.Tage_Tab_Im_Regist));

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(msgReceiver);
	}
}
