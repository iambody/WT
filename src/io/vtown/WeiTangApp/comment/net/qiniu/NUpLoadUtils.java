package io.vtown.WeiTangApp.comment.net.qiniu;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.a.a.s;

import com.android.volley.Request.Method;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.net.NHttpBaseStr;
import io.vtown.WeiTangApp.event.interf.IHttpResult;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-13 下午2:18:45
 * 
 */
public class NUpLoadUtils {
	/**
	 * 上下文
	 */
	private Context context;

	/**
	 * 图片或者视频的文件路径
	 */
	private File PicFile ;
	/**
	 * byte数组
	 */
	private byte[]bytes;
	/**
	 * 图片保存地址的名字
	 */
	private String PicName = "";
	/**
	 * 回调接口
	 */
	private UpResult upResult;

	public void SetUpResult(UpResult da) {
		this.upResult = da;
	}

	/**
	 * 传递图片视频的路径
	 * 
	 * @param context
	 * @param picname
	 * @param picFile
	 */
	public NUpLoadUtils(Context context, byte[] FileStrs, String PicNames) {
		super();
		this.context = context;
		this.bytes = FileStrs;
		this.PicName = PicNames;
	}

	public NUpLoadUtils() {
		super();

	}

	public void UpLoad() {
		NHttpBaseStr baseStr = new NHttpBaseStr(context);
		baseStr.setPostResult(new IHttpResult<String>() {

			@Override
			public void onError(String error,int LoadType) {
			}

			@Override
			public void getResult(int Code, String Msg, String Data) {
				JSONObject obj;
				String Token = "";
				try {
					obj = new JSONObject(Data);
					Token = obj.getString("token");
					//PromptManager.ShowCustomToastLong(context, "Token=>"+Token);
					LoadUp(Token, PicName, bytes);
				} catch (JSONException e) {
					e.printStackTrace();
					PromptManager.ShowCustomToastLong(context, "JSONException"+e.toString());
				}

			}
		});
		baseStr.getData(Constants.QiNiuToken, new HashMap<String, String>(),
				Method.GET);
	}

	public void LoadUp(String Token, final String Key,  byte[] bytes) {
		

		UploadManager uploadManager = new UploadManager();

		uploadManager.put(bytes, Key, Token, new UpCompletionHandler() {
			@Override
			public void complete(String k, ResponseInfo rinfo,
					JSONObject response) {
				if (rinfo.statusCode == 200) {
					String s = k + ", " + rinfo + ", " + response;
					String key1 = Constants.QiNiuHost + "/"
							+ getKey(k, response);
					upResult.Complete(key1,Key);
				} else {
					upResult.Onerror();
				}

			}
		}, new UploadOptions(null, null, false, new UpProgressHandler() {

			@Override
			public void progress(String arg0, double arg1) {
				upResult.Progress(arg0, arg1);
				 
			}
		}, new UpCancellationSignal() {

			@Override
			public boolean isCancelled() {
				upResult.Onerror();
				return false;
			}
		}));

	}

	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	public interface UpResult {
		/**
		 * 正在上传中
		 * 
		 * @param arg0
		 * @param arg1
		 */
		public void Progress(String arg0, double arg1);

		/**
		 * 上传成功 返回url
		 * 
		 * @param Url
		 */
		public void Complete(String HostUrl,String PicName );

		/**
		 * 上传失败
		 */
		public void Onerror();
	}

	private String getKey(String k, JSONObject response) {
		if (k != null) {
			return k;
		} else {
			return response.optString("key", "<获取key失败>");
		}
	}
}
