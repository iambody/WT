package io.vtown.WeiTangApp.ui.title;


import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.QRCodeUtil;
import io.vtown.WeiTangApp.comment.util.image.ImageLoader;
import io.vtown.WeiTangApp.comment.view.RevealLayout;
import io.vtown.WeiTangApp.ui.ATitleBase;

import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * @author 作者 易惠华 yihuihua@v-town.cc
 * @version 创建时间：2016-6-23 下午4:40:39
 *  
 */
public class ABeQRCode extends ATitleBase {

	private ImageView iv_show_qrcode;
	private Button btn_1;
	private RevealLayout layout1;

	@Override
	protected void InItBaseView() {
		setContentView(R.layout.activity_show_qrcode);
		IView();
	}

	private void IView() {
		iv_show_qrcode = (ImageView) findViewById(R.id.iv_show_qrcode);
		final String filePath = getFileRoot(BaseContext) + File.separator
                + "qr_" + System.currentTimeMillis() + ".jpg";
		 new Thread(new Runnable() {
             @Override
             public void run() {
            	 String avatar = Spuit.Shop_Get(BaseContext).getAvatar();
 				Bitmap logoBm = com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImageSync(avatar);
                 boolean success = QRCodeUtil.createQRImage("www.baidu.com///", 800, 800,logoBm,filePath);

                 if (success) {
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                        	 iv_show_qrcode.setImageBitmap(BitmapFactory.decodeFile(filePath));
                        	
                         }
                     });
                 }
             }
         }).start();
		 
		 
		 btn_1 = (Button) findViewById(R.id.btn_1);
		 layout1 = (RevealLayout) findViewById(R.id.layout1);
		
	}
	
//	//文件存储根目录
//    private String getFileRoot(Context context) {
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            File external = context.getExternalFilesDir(null);
//            if (external != null) {
//                return external.getAbsolutePath();
//            }
//        }
// 
//        return context.getFilesDir().getAbsolutePath();
//    }

	@Override
	protected void InitTile() {
		SetTitleTxt("二维码");
	}

	@Override
	protected void DataResult(int Code, String Msg, BComment Data) {
	}

	@Override
	protected void DataError(String error, int LoadType) {
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
	}

	@Override
	protected void InItBundle(Bundle bundle) {
	}

	@Override
	protected void SaveBundle(Bundle bundle) {
	}

}
