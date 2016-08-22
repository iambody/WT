package io.vtown.WeiTangApp.ui.comment;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils;
import io.vtown.WeiTangApp.comment.net.qiniu.NUpLoadUtils.UpResult;
import io.vtown.WeiTangApp.comment.selectpic.ui.AAlterGaller;
import io.vtown.WeiTangApp.comment.selectpic.ui.AlbumActivity;
import io.vtown.WeiTangApp.comment.selectpic.ui.GalleryActivity;
import io.vtown.WeiTangApp.comment.selectpic.util.Bimp;
import io.vtown.WeiTangApp.comment.selectpic.util.FileUtils;
import io.vtown.WeiTangApp.comment.selectpic.util.ImageItem;
import io.vtown.WeiTangApp.comment.selectpic.util.PublicWay;
import io.vtown.WeiTangApp.comment.selectpic.util.Res;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.event.interf.IBottomDialogResult;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-23 下午6:29:05
 * 
 */
public class ASelectPic extends ATitleBase {
	public final static int SelecPicKey = 401;
	private GridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;

	public static Bitmap bimap;
	private TextView selectpic_submint;
	private int Type = -1;

	private int AllNumber = 0;

	private Boolean descfrist = false;

	// TODO ===============》other界面注册时间总线finish
	@Override
	protected void InItBaseView() {
		Type = getIntent().getIntExtra("pictype", -1);
		  setPicType(1);
		bimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.multiphoto_icon_addpic_unfocused);
		
		
		
		Res.init(this);
		
		
		PublicWay.activityList.add(this);
		parentView = getLayoutInflater().inflate(R.layout.activity_select_pic,
				null);
		descfrist = getIntent().getBooleanExtra("descfrist", false);
		setContentView(parentView);
		if (descfrist) {
			Bimp.tempSelectBitmap = new ArrayList<ImageItem>();
			Bimp.max = 0;
		}
		Init();

	}

	@Override
	protected void InitTile() {
		SetTitleTxt(Type == BMessage.Tage_AddGoodDescPic ? "商品描述" : "商品图片");
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
	}

	@Override
	protected void DataError(String error, int LoadType) {
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
	protected void onDestroy() {
		super.onDestroy();
		CacheUtil.BitMapRecycle(bimap);
	}

	@Override
	protected void MyClick(View V) {
		switch (V.getId()) {
		case R.id.selectpic_submint:
			// UpAllPic(Bimp.tempSelectBitmap);
			SkipIntent();
			break;

		default:
			break;
		}
	}

	private void SkipIntent() {
		BMessage bMessage = new BMessage(Type);

		List<ImageItem> MyGoodsPic = Bimp.tempSelectBitmap;
		bMessage.setAddImageItems(MyGoodsPic);
		EventBus.getDefault().post(bMessage);

		for (int i = 1; i < PublicWay.activityList.size(); i++) {
			PublicWay.activityList.get(i).finish();
		}
		BaseActivity.finish();
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

	public void Init() {
		selectpic_submint = (TextView) findViewById(R.id.selectpic_submint);
		selectpic_submint.setOnClickListener(this);
		noScrollgridview = (GridView) findViewById(R.id.addgoodpic_noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					// new PopupWindows(BaseContext, parentView);
					ShowSelectPic();
				} else {
					Intent intent = new Intent(BaseActivity, AAlterGaller.class);
					intent.putExtra("position", "1");
					// intent.putExtra("type", Type);
					intent.putExtra("ID", arg2);
					BaseActivity.startActivity(intent);
				}
			}
		});

	}

	/**
	 * 点击加号时候 选择
	 */
	private void ShowSelectPic() {
		ShowBottomPop(BaseContext, parentView, "拍照", "相片",
				new IBottomDialogResult() {

					@Override
					public void SecondResult() {
						Intent intent = new Intent(BaseActivity,
								AlbumActivity.class);
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
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {

			if (Bimp.tempSelectBitmap.size() == 9) {
				return 9;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
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

			if (position == Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(),
						R.drawable.multiphoto_icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				if(Bimp.tempSelectBitmap.get(position).getBitmap()==null&&!StrUtils.isEmpty(Bimp.tempSelectBitmap.get(position).getImagePath())){
					Bimp.tempSelectBitmap.get(position).setBitmap(StrUtils.GetBitMapFromPath(Bimp.tempSelectBitmap.get(position).getImagePath()));
				}
				
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position)
						.getBitmap());
			}

			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					// PromptManager.ShowCustomToast(BaseContext, "条数："
					// + Bimp.tempSelectBitmap.size());
					if (Bimp.tempSelectBitmap.size() > 0) {
						// PromptManager.ShowCustomToast(BaseContext, "path："
						// + Bimp.tempSelectBitmap.get(0).getImagePath());
						selectpic_submint.setVisibility(View.VISIBLE);
						// PromptManager.ShowCustomToast(BaseContext,
						// "宽高比:"+StrUtils.GetBitmapWidthHeightRatio(
						// Bimp.tempSelectBitmap.get(Bimp.tempSelectBitmap.size()-1).getBitmap()))
						// ;
					}
					if (Bimp.tempSelectBitmap.size() == 0) {
						selectpic_submint.setVisibility(View.GONE);
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

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	private static final int TAKE_PICTURE = 0x000001;

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
			for (int i = 0; i < PublicWay.activityList.size(); i++) {
				if (null != PublicWay.activityList.get(i)) {
					PublicWay.activityList.get(i).finish();
				}
			}
			// System.exit(0);
		}
		return true;
	}

	// public void UpdaIvTest(byte[] bytes, final String picname,
	// final int postion, final ArrayList<ImageItem> da) {
	//
	// NUpLoadUtils dLoadUtils = new NUpLoadUtils(BaseContext, bytes, picname);
	// dLoadUtils.SetUpResult(new UpResult() {
	//
	// @Override
	// public void Progress(String arg0, double arg1) {
	// }
	//
	// @Override
	// public void Onerror() {
	//
	// Bimp.tempSelectBitmap.get(postion).setThumbnailPath("");
	// AllNumber = AllNumber + 1;
	// // PromptManager.ShowCustomToast(BaseContext, "上传出错");
	// if (AllNumber == da.size()) {
	// selectpic_submint.setClickable(true);
	// selectpic_submint.setBackground(getResources().getDrawable(
	// R.drawable.select_fen_to_gray));
	// PromptManager.closeTextLoading3();
	// SkipIntent();
	// }
	// }
	//
	// @Override
	// public void Complete(String HostUrl, String Url) {
	// AllNumber = AllNumber + 1;
	//
	// Bimp.tempSelectBitmap.get(postion).setThumbnailPath(HostUrl);
	// // PromptManager.ShowCustomToast(BaseContext, "上传成功名字：" +
	// // picname);
	// if (AllNumber == da.size()) {
	// selectpic_submint.setClickable(true);
	// selectpic_submint.setBackground(getResources().getDrawable(
	// R.drawable.select_fen_to_gray));
	// PromptManager.closeTextLoading3();
	// SkipIntent();
	// }
	// }
	// });
	// dLoadUtils.UpLoad();
	//
	// }

	// private void UpAllPic(ArrayList<ImageItem> datas) {
	// selectpic_submint.setClickable(false);
	// selectpic_submint.setBackground(getResources().getDrawable(
	// R.drawable.shap_bt_press));
	// AllNumber = 0;
	// if (datas.size() == 0) {
	// PromptManager.ShowCustomToast(BaseContext, "请添加图片");
	// return;
	// }
	// PromptManager.showtextLoading3(BaseContext, "正在努力上传.....");//
	// (BaseContext);
	// for (int i = 0; i < datas.size(); i++) {
	// ImageItem bm = datas.get(i);
	// if (StrUtils.isEmpty(bm.getThumbnailPath()))
	// UpdaIvTest(StrUtils.Bitmap2Bytes(bm.getBitmap()),
	// StrUtils.UploadQNName("photo"), i, datas);
	// else
	// AllNumber = AllNumber + 1;
	//
	// }
	// if (AllNumber == datas.size()) {
	// selectpic_submint.setClickable(true);
	// selectpic_submint.setBackground(getResources().getDrawable(
	// R.drawable.select_fen_to_gray));
	// PromptManager.closeTextLoading3();
	// SkipIntent();
	// }
	//
	// }

}
