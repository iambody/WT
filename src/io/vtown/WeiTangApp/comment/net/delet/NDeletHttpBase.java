package io.vtown.WeiTangApp.comment.net.delet;

import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.LogUtils;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-15 下午12:51:37
 * 
 */
public abstract class NDeletHttpBase {

	/**
	 * 上下文
	 */
	public Context context;

	/**
	 * 超时时间
	 */
	private int SOCKET_TIMEOUT = 10 * 1000;

	// private RequestQueue queue = null;

	public NDeletHttpBase(Context context) {
		this.context = context;
	}

	public abstract void myOnResponse(String str);

	public abstract void myonErrorResponse(VolleyError arg0);

	public void getData(String url, final HashMap<String, String> mapout,
			final int method) {

		// 添加标识
		mapout.put("UUID", Constants.GetPhoneId(context));
		mapout.put("source", 20 + "");
		mapout.put("api_version",Constants.Api_Version);
		// 进行底层的封装sign
		final HashMap<String, String> map = Constants.Sign(mapout);
		final JSONObject obj = Constants.SignToJson(map);

		RequestParams params = new RequestParams();
		// params.addHeader("name", "value");
		// params.addQueryStringParameter("name", "value");

		// 只包含字符串参数时默认使用BodyParamsEntity，
		// 类似于UrlEncodedFormEntity（"application/x-www-form-urlencoded"）。

		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			// entry.getKey() 返回与此项对应的键
			// entry.getValue() 返回与此项对应的值

			params.addBodyParameter(String.valueOf(entry.getKey()),
					String.valueOf(entry.getValue()));

		}

		// 加入文件参数后默认使用MultipartEntity（"multipart/form-data"），
		// 如需"multipart/related"，xUtils中提供的MultipartEntity支持设置subType为"related"。
		// 使用params.setBodyEntity(httpEntity)可设置更多类型的HttpEntity（如：
		// MultipartEntity,BodyParamsEntity,FileUploadEntity,InputStreamUploadEntity,StringEntity）。
		// 例如发送json参数：params.setBodyEntity(new StringEntity(jsonStr,charset));
		// params.addBodyParameter("file", new File("path"));
		// ...

		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.DELETE, url, params,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {

					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						PromptManager.closeLoading();
						PromptManager.closetextLoading();
						PromptManager.closeTextLoading();
						myOnResponse(responseInfo.result);
						LogUtils.i("sss");

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						PromptManager.closeLoading();
						PromptManager.closetextLoading();
						PromptManager.closeTextLoading();
						myonErrorResponse(null);
					}
				});
	}

	private String MapToStr(HashMap<String, String> map) {
		if (map == null)
			return "";

		Iterator it = map.entrySet().iterator();

		String MapStr = "?";
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
			// entry.getKey() 返回与此项对应的键
			// entry.getValue() 返回与此项对应的值
			MapStr = it.hasNext() ? MapStr + entry.getKey() + "="
					+ entry.getValue() + "&" : MapStr + entry.getKey() + "="
					+ entry.getValue();

		}

		return MapStr;

	}

}
