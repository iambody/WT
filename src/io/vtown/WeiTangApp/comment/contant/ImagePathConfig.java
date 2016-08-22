package io.vtown.WeiTangApp.comment.contant;

import java.io.File;

import io.vtown.WeiTangApp.comment.util.SdCardUtils;
import android.content.Context;
import android.os.Environment;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-8-16 下午1:21:15
 * 
 */
public class ImagePathConfig {
	public static String PicCach = Environment.getExternalStorageDirectory()
			+ "/WtAndroid/cach/";

	/**
	 * 店铺高斯图片的保存地址
	 */
	public static String ShopCoverPath = "shopcover.jpg";
	/**
	 * center的高斯背景图片
	 */
	public static String CenterCoverPath = "centercover.jpg";

	/**
	 * 获取 高斯图片=》Shop的保存地址
	 */
	public static String ShopCoverPath(Context pPContext) {
		return getFileRoot(pPContext)+"shopcover.jpg";
	}

	/**
	 * 获取 高斯图片=》Center的保存地址
	 */
	public static String CenterCoverPath(Context pPContext) {
		return getFileRoot(pPContext)+"centercover.jpg";
	}

	// 文件存储根目录
	public static String getFileRoot(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File external = context.getExternalFilesDir(null);
			if (external != null) {
				return external.getAbsolutePath();
			}
		}
		return context.getFilesDir().getAbsolutePath() + File.separator
				+ "code" + File.separator;
	}

	public static void CleranGaosi(Context bContext) {
		File shopFile = new File(ShopCoverPath(bContext));
		File centerFile = new File(CenterCoverPath(bContext));

		if (shopFile.exists()) {
			shopFile.delete();
		}
		if (centerFile.exists()) {
			centerFile.delete();
		}
	}
}
