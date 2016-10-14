package io.vtown.WeiTangApp.comment.net.delet;

import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.event.interf.IHttpResult;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.VolleyError;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-12 上午10:16:59
 * 
 */
public class NHttpDeletBaseStr extends NDeletHttpBase    {

	public NHttpDeletBaseStr(Context context) {
		super(context);
	}

	private IHttpResult<String> postResult;

	public void setPostResult(IHttpResult<String> postresult) {
		this.postResult = postresult;
	}

	@Override
	public void myOnResponse(String str) {
		if (StrUtils.isEmpty(str)) {
			postResult.getResult(201, "未获得服务器信息请重试.....", str);
		} else {
			int Status = 0;
			int Code = 0;
			String Msg = null;
			String Data = "";
			try {
				JSONObject obj = new JSONObject(str);

				try {
					Code = obj.getInt("code");
				} catch (Exception e) {

				}
				try {
					Msg = obj.getString("msg");
				} catch (Exception e) {
				}

				try {
					Data = obj.getString("data");
				} catch (Exception e) {

				}

			} catch (Exception e) {
				if (postResult != null)
					postResult.onError(e.getMessage(), 0);
			}

			if (postResult != null)
				postResult.getResult(Code, Msg, Data);
		}
	}

	@Override
	public void myonErrorResponse(VolleyError arg0) {

		if (postResult != null)
			postResult.onError("服务器繁忙", 0);
	}

}
