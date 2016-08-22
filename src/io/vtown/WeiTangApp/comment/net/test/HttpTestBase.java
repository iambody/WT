package io.vtown.WeiTangApp.comment.net.test;

import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-24 下午7:13:02
 * 
 */
public abstract class HttpTestBase {
	/**
	 * 上下文
	 */
	public Context context;
	// public Activity MtActivity;
	/**
	 * 超时时间
	 */
	private int SOCKET_TIMEOUT = 10 * 1000;

	public HttpTestBase(Context context) {
		this.context = context;
		// this.MtActivity = context;
	}

	public abstract void myOnResponse(String str);

	public abstract void myonErrorResponse(VolleyError arg0);

	public void getData(String url, final HashMap<String, String> map1,
			final int method) {

		RequestQueue queue = Volley.newRequestQueue(context);
		// 进行底层的封装sign
		// final HashMap<String, String> map=Constants.Sign(map1);
		// FakeX509TrustManager.allowAllSSL();// 安全认证
		queue.add(new StringRequest(method, method == Method.GET ? url
				+ MapToStr(map1) : url, new Listener<String>() {
			@Override
			public void onResponse(String arg0) {
				PromptManager.closeLoading();
				PromptManager.closetextLoading();
				myOnResponse(arg0);

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				PromptManager.closeLoading();
				PromptManager.closetextLoading();
				myonErrorResponse(arg0);
			}
		}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {// post方法获取

				return method == Method.GET ? super.getParams() : map1;
				// return super.getParams();
			}

			@Override
			public byte[] getBody() throws AuthFailureError {
				// return obj.toString().getBytes();
				return super.getBody();

			}

			@Override
			protected VolleyError parseNetworkError(VolleyError volleyError) {

				return super.parseNetworkError(volleyError);
			}

			@Override
			public RetryPolicy getRetryPolicy() {
				RetryPolicy retryPolicy = new DefaultRetryPolicy(
						SOCKET_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
						DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
				return retryPolicy;

			}

			@Override
			protected Response<String> parseNetworkResponse(
					NetworkResponse response) {
				String str = null;
				try {
					str = new String(response.data, "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return Response.success(str,
						HttpHeaderParser.parseCacheHeaders(response));

			}

		});
		queue.start();
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
