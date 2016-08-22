package io.vtown.WeiTangApp.comment.view.dialog;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcache.BShop;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-8-8 下午2:11:00
 * 
 */
public class ImagViewDialog extends Dialog {
	private Context dpContext;
	private Bitmap bitmap;
	private int MyWidth;

	private View BaseView;
	private ImageView pop_imagview_iv;
	private ImageView pop_imagview_head;
	private TextView pop_imagview_head_name;
	private TextView pop_imagview_desc;
	private BShop bShop;

	private int Type;// 1==>店铺的二维码 2==》邀请码

	public ImagViewDialog(Context context, Bitmap pBitmap, int Width, int MyType) {
		super(context, R.style.mystyle);
		this.dpContext = context;
		this.bitmap = pBitmap;
		this.MyWidth = Width;
		this.Type = MyType;
		Window window = this.getWindow();
		// 设置显示动画
		window.setWindowAnimations(R.style.main_menu_animstyless);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dialog_imagview);
		bShop = Spuit.Shop_Get(dpContext);
		// BaseView = findViewById(R.layout.dialog_imagview);
		pop_imagview_iv = (ImageView) findViewById(R.id.pop_imagview_iv);
		pop_imagview_head = (ImageView) findViewById(R.id.pop_imagview_head);
		pop_imagview_head_name = (TextView) findViewById(R.id.pop_imagview_head_name);
		pop_imagview_desc = (TextView) findViewById(R.id.pop_imagview_desc);

		LinearLayout.LayoutParams params = new LayoutParams(MyWidth, MyWidth);
		pop_imagview_iv.setLayoutParams(params);
		pop_imagview_iv.setImageBitmap(bitmap);
		pop_imagview_head_name.setText(StrUtils.NullToStr(bShop
				.getSeller_name()));
		ImageLoaderUtil.Load2(bShop.getAvatar(), pop_imagview_head,
				R.drawable.testiv);
		pop_imagview_iv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ImagViewDialog.this.dismiss();
				return true;
			}
		});

		if (Type == 1) {// 店铺二维码
			pop_imagview_desc.setText(dpContext.getResources().getString(
					R.string.qrcode_shop_desc));
		}
		if (Type == 2) {// 邀请注册二维码
			pop_imagview_desc.setText(dpContext.getResources().getString(
					R.string.qrcode_regist_desc));
		}
	}
}
