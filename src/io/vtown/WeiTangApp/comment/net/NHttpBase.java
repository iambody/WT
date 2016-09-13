/**
 * 
 */
package io.vtown.WeiTangApp.comment.net;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.https.FakeX509TrustManager;
import io.vtown.WeiTangApp.comment.net.https.VolleyHttps;
import io.vtown.WeiTangApp.comment.util.NetUtil;
import io.vtown.WeiTangApp.comment.util.StrUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * @author 王永奎 E-mail:wangyk@nsecurities.cn
 * @department 互联网金融部
 * @version 创建时间：2015-11-6 下午1:59:49
 */

public abstract class NHttpBase {
	/**
	 * 上下文
	 */
	public Context context;

	/**
	 * 超时时间
	 */
	private int SOCKET_TIMEOUT = 20 * 1000;

	// private RequestQueue queue = null;

	public NHttpBase(Context context) {
		this.context = context;
	}

	public abstract void myOnResponse(String str);

	public abstract void myonErrorResponse(VolleyError arg0);

	public void getData(String url, final HashMap<String, String> map1,
			final int method) {
//		if (!NetUtil.isConnected(context)) {
//			PromptManager.closeLoading();
//			PromptManager.closetextLoading();
//			PromptManager.ShowCustomToast(context, "请检查网络");
//			myonErrorResponse(new VolleyError());
//			return;
//		}
//		RequestQueue queue = VolleyHttps.newRequestQueue(context, null, true,
//				R.raw.ssl);// Volley.newRequestQueue(context);
		 RequestQueue queue = Volley.newRequestQueue(context);

		// 添加标识
		map1.put("UUID", Constants.GetPhoneId(context));
		map1.put("source", 20 + "");
		 map1.put("api_version",Constants.Api_Version);
		// 如果已经登录了就开始加Yoken&&登录后的Token不为空
		if (Spuit.IsLogin_Get(context)&&!StrUtils.isEmpty(Spuit.User_Get(context).getToken())) {
			map1.put("token", Spuit.User_Get(context).getToken());
		}

		// 进行底层的封装sign

		final HashMap<String, String> map = !url
				.equals(Constants.Shop_AddGoods) ? Constants.Sign(map1)
				: Constants.Sign2(map1);
		 FakeX509TrustManager.allowAllSSL();// 安全认证

		queue.add(new StringRequest(method, method == Method.GET ? url
				+ MapToStr(map) : url, new Listener<String>() {
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

				// return method == Method.GET ?map:super.getParams();
				return (method == Method.GET) ? super.getParams() : map;
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
