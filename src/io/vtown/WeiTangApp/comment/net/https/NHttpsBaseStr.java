package io.vtown.WeiTangApp.comment.net.https;

import io.vtown.WeiTangApp.event.interf.IHttpResult;
import android.content.Context;

public class NHttpsBaseStr extends HttpsBaseNet {

	private IHttpResult<String> mResult;

	public void GetResult(IHttpResult<String> data) {
		this.mResult = data;
	}

	public NHttpsBaseStr(Context baseContext) {
		super(baseContext);
	}

	@Override
	public void myOnResponse(String str) {
		if (mResult != null)
			mResult.getResult( 201, "", str);
	}

	@Override
	public void myonErrorResponse(String arg0) {
		if (mResult != null)
			mResult.onError(null,0);
	}

}
