package io.vtown.WeiTangApp.ui.comment;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.selectpic.ui.AAlterBum;
import io.vtown.WeiTangApp.comment.selectpic.ui.AAlterGaller;
import io.vtown.WeiTangApp.comment.selectpic.ui.AShareGaller;
import io.vtown.WeiTangApp.comment.selectpic.ui.AlbumActivity;
import io.vtown.WeiTangApp.comment.selectpic.util.Bimp;
import io.vtown.WeiTangApp.comment.selectpic.util.FileUtils;
import io.vtown.WeiTangApp.comment.selectpic.util.ImageItem;
import io.vtown.WeiTangApp.comment.selectpic.util.PublicWay;
import io.vtown.WeiTangApp.comment.selectpic.util.Res;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.event.interf.IBottomDialogResult;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-19 下午2:58:17
 * 
 */
public class AGoodEditPicSelect extends ATitleBase {

	public final static String Tage_IsGoods = "goodeditpicselectpic";

	/**
	 * 商品图片还是 宝贝描述图片
	 */
	private boolean IsGoodsPicSelect;
	private GridView edit_goodpic_noScrollgridview;
	private TextView edit_selectpic_submint;

	//

	private MyGridAdapter EditAdapter;
	private View parentView;

	public static Bitmap bimap;

	private ArrayList<String> urlStrings = new ArrayList<String>();

	// 判断下是否进行了修改 如果修改了true 如果没修改false
	private boolean IsAlter = false;

	// 所有的需要的size目前需要宝贝9张 描述10张

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_goodeditpicselect);
		// EventBus.getDefault().register(this, "FrashItem", BMessage.class);
		parentView = LayoutInflater.from(BaseContext).inflate(
				R.layout.activity_goodeditpicselect, null);
		Res.init(this);
		IBund();
		bimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.multiphoto_icon_addpic_unfocused);
		PublicWay.activityList.add(this);

		IView();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
	}

	private void IBund() {
		IsGoodsPicSelect = getIntent().getBooleanExtra(Tage_IsGoods, false);
		urlStrings = getIntent().getStringArrayListExtra("lss");
		Bimp.tempSelectBitmap = (ArrayList<ImageItem>) GetPicChange(urlStrings);
		Bimp.max = urlStrings.size();
	}

	private void IView() {
		edit_goodpic_noScrollgridview = (GridView) findViewById(R.id.edit_goodpic_noScrollgridview);
		edit_selectpic_submint = (TextView) findViewById(R.id.edit_selectpic_submint);
		edit_selectpic_submint.setOnClickListener(this);

		EditAdapter = new MyGridAdapter(BaseContext);
		EditAdapter.update();
		edit_goodpic_noScrollgridview.setAdapter(EditAdapter);
		edit_goodpic_noScrollgridview
				.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						if (arg2 == Bimp.tempSelectBitmap.size()) {
							// new PopupWindows(BaseContext, parentView);
							SelectPop();

						} else {
							Intent intent = new Intent(BaseActivity,
									AAlterGaller.class);
							intent.putExtra("position", "1");
							intent.putExtra("ID", arg2);
							BaseActivity.startActivity(intent);
						}
					}

				});

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

	@Override
	protected void InitTile() {
		SetTitleTxt(IsGoodsPicSelect ? "宝贝图片" : "宝贝描述");
		SetRightText("添加图片");// SelectPop();
		right_txt.setOnClickListener(this);
		HindBackIv();
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
	}

	@Override
	protected void DataError(String error, int LoadType) {
	}

	private void SelectPop() {
		ShowBottomPop(BaseContext, parentView, "拍照", "相册",
				new IBottomDialogResult() {

					@Override
					public void SecondResult() {
						Intent intent = new Intent(BaseActivity,
								AAlterBum.class);
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

	@SuppressLint("HandlerLeak")
	public class MyGridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;
		private boolean IsZero = false;

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

				// PromptManager.ShowCustomToast(BaseContext, "位置" + position);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (IsZero && position == 0) {
				return convertView;
			}
			if (!IsZero && position == 0) {
				IsZero = true;
			}

			if (null != Bimp.tempSelectBitmap.get(position).getBitmap()
					|| !StrUtils.isEmpty(Bimp.tempSelectBitmap.get(position)
							.getImagePath())) {
				// if (StrUtils.isEmpty(
				// Bimp.tempSelectBitmap.get(position).getThumbnailPath()))
				// {
//				if (null == Bimp.tempSelectBitmap.get(position).getBitmap()) {
//					Bimp.tempSelectBitmap.get(position).setBitmap(
//							StrUtils.GetBitMapFromPath(Bimp.tempSelectBitmap
//									.get(position).getImagePath()));
//				}
				if(!StrUtils.isEmpty(Bimp.tempSelectBitmap.get(position).getImagePath())){
					Bimp.tempSelectBitmap.get(position).setBitmap(
							StrUtils.GetBitMapFromPath(Bimp.tempSelectBitmap
									.get(position).getImagePath()));
					
				}
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
					EditAdapter.notifyDataSetChanged();
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

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	private static final int TAKE_PICTURE = 0x000001;

	@Override
	protected void onRestart() {
		EditAdapter.update();
		super.onRestart();
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
				IsAlter = true;
			}
			break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			for (int i = 0; i < PublicWay.activityList.size(); i++) {
				if (null != PublicWay.activityList.get(i)) {
					PublicWay.activityList.get(i).finish();
				}
			}
			// System.exit(0);
		}
		return true;
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
		case R.id.right_txt:
			SelectPop();
			break;
		case R.id.edit_selectpic_submint:// 点击提交
			//
			if (Bimp.tempSelectBitmap.size() == 0) {
				ShowCustomDialog("商品图片不能清空!", "取消", "退出编辑",
						new IDialogResult() {

							@Override
							public void RightResult() {
								BaseActivity.finish();
							}

							@Override
							public void LeftResult() {
							}
						});
				return;
			}

			// 点击提交
			BMessage bMessage;
			if (IsGoodsPicSelect) {// 传递商品的轮播图
				bMessage = new BMessage(BMessage.Tage_Alter_Goods);
				bMessage.setAddImageItems(Bimp.tempSelectBitmap);
				EventBus.getDefault().post(bMessage);

			} else {
				bMessage = new BMessage(BMessage.Tage_Alter_Miaoshu);
				bMessage.setAddImageItems(Bimp.tempSelectBitmap);
				EventBus.getDefault().post(bMessage);
			}
			BaseActivity.finish();
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

}
