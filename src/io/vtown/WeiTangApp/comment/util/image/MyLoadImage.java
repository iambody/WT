package io.vtown.WeiTangApp.comment.util.image;
/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-4-19 上午11:06:47
 *  
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.os.Environment;  
public class MyLoadImage extends AsyncTask<Integer, Void, Bitmap>{

	@Override
	protected Bitmap doInBackground(Integer... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

//	/**
//	 * 图片的URL地址
//	 */
//	private String mImageUrl;
//	/**
//	 * 加载图片使用的ImageView
//	 */
//	private ImageView mImageView;
//	
//	/**
//	 * 对图片进行管理的工具类
//	 */
//	private ImageLoader imageLoader;
//	
//
//	/**
//	 * 图片的宽度
//	 */
//	//private int columnWidth;
//	/**
//	 * 图片的高度
//	 */
//	//private int columnHeight;
//	
//	public MyLoadImage(ImageView imageView,String imageUrl) {
//		mImageView = imageView;
//		mImageUrl = imageUrl;
//		
//		//columnWidth = mImageView.getWidth();
//		//columnHeight = mImageView.getHeight();
//		imageLoader = ImageLoader.getInstance();
//	}
//	
//	@Override
//	protected Bitmap doInBackground(Integer... arg0) {
//		// TODO Auto-generated method stub
//		if(mImageUrl == null)
//		{
//			return null;
//		}
//		try {
//			Bitmap imageBitmap = loadImage(mImageUrl);
//			return imageBitmap;
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//
//
//	
//	private void addImage(Bitmap bitmap) {
//		
//		if (mImageView != null) {
//			mImageView.setImageBitmap(bitmap);
//		} 
//	}
//	
//	@Override
//	protected void onPostExecute(Bitmap bitmap) {
//		if (bitmap != null) {
//			
//			addImage(bitmap);
//		}
//
//	}
//	
//	
//	/**
//	 * 根据传入的URL，对图片进行加载。如果这张图片已经存在于SD卡中，则直接从SD卡里读取，否则就从网络上下载。
//	 * 
//	 * @param imageUrl
//	 *            图片的URL地址
//	 * @return 加载到内存的图片。
//	 */
//	private Bitmap loadImage(String imageUrl) {
//		File imageFile = new File(getImagePath(imageUrl));
//		if (!imageFile.exists()) {
//			downloadImage(imageUrl);
//		}
//		if (imageUrl != null) {
//			
//			Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath() );
//			//Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath(), columnWidth);
//			if (bitmap != null) {
//				imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
//				return bitmap;
//			}
//		}
//		return null;
//	}
//
//
//	/**
//	 * 将图片下载到SD卡缓存起来。
//	 * 
//	 * @param imageUrl
//	 *            图片的URL地址。
//	 */
//	private void downloadImage(String imageUrl) {
//		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//			Log.d("TAG", "monted sdcard");
//		} else {
//			Log.d("TAG", "has no sdcard");
//		}
//		HttpURLConnection con = null;
//		FileOutputStream fos = null;
//		BufferedOutputStream bos = null;
//		BufferedInputStream bis = null;
//		File imageFile = null;
//		try {
//			URL url = new URL(imageUrl);
//			con = (HttpURLConnection) url.openConnection();
//			con.setConnectTimeout(5 * 1000);
//			con.setReadTimeout(15 * 1000);
//			con.setDoInput(true);
//			con.setDoOutput(true);
//			bis = new BufferedInputStream(con.getInputStream());
//			imageFile = new File(getImagePath(imageUrl));
//			fos = new FileOutputStream(imageFile);
//			bos = new BufferedOutputStream(fos);
//			byte[] b = new byte[1024];
//			int length;
//			while ((length = bis.read(b)) != -1) {
//				bos.write(b, 0, length);
//				bos.flush();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (bis != null) {
//					bis.close();
//				}
//				if (bos != null) {
//					bos.close();
//				}
//				if (con != null) {
//					con.disconnect();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		if (imageFile != null) {
//			Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath() );
//			//Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(imageFile.getPath(),columnWidth);
//			if (bitmap != null) {
//				imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
//			}
//		}
//	}
//
//	/**
//	 * 获取图片的本地存储路径。
//	 * 
//	 * @param imageUrl
//	 *            图片的URL地址。
//	 * @return 图片的本地存储路径。
//	 */
//	private String getImagePath(String imageUrl) {
//		int lastSlashIndex = imageUrl.lastIndexOf("/");
//		String imageName = imageUrl.substring(lastSlashIndex + 1);
//		String imageDir = ConstInfo.saveImgPath;
//		File file = new File(imageDir);
//		if (!file.exists()) {
//			file.mkdirs();
//		}
//		String imagePath = imageDir + imageName;
//		return imagePath;
//	}
}

	
	/*
	 public  byte[] getImage(String path) throws Exception{
	        URL url = new URL(path);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();//基于HTTP协议连接对象
	        conn.setConnectTimeout(5000);
	        conn.setRequestMethod("GET");
	        if(conn.getResponseCode() == 200){
	            InputStream inStream = conn.getInputStream();
	            return read(inStream);
	        }
	        return null;
	    }
	    
	    public  byte[] read(InputStream inStream) throws Exception{
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while( (len = inStream.read(buffer)) != -1){
				outStream.write(buffer, 0, len);
			}
			inStream.close();
			return outStream.toByteArray();
		}
	    
	    public  Bitmap getBitMapOnWebUrl(String url)
	    {
	    	byte[] data;
			try {
				data = getImage(url);
				if(data == null)
				{
					return null;
				}
				Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
		    	return bitmap;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
	    }*/


