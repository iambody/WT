/**
 * 
 */
package io.vtown.WeiTangApp.comment.net.https;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

 

import android.content.Context;
 
/**
 *@author 王永奎 E-mail:wangyongkui@ucfgroup.com
 *@department 互联网金融部
 *@version 创建时间：2015-7-29 下午3:46:33
 */

public abstract class HttpsBaseNet {
	private Context baseContext;
	public abstract void myOnResponse(String str);

	public abstract void myonErrorResponse(String arg0);
	
	public HttpsBaseNet(Context baseContext) {
		super();
		this.baseContext = baseContext;
	}
	
	/**
	 * HttpClient方式实现，支持验证指定证书
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void initSSLCertainWithHttpClientPost(Context m,String host ,JSONObject jsonObject) throws ClientProtocolException, IOException {
		int timeOut = 20 * 1000;
		HttpParams param = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(param, timeOut);
		HttpConnectionParams.setSoTimeout(param, timeOut);
		HttpConnectionParams.setTcpNoDelay(param, true);

		SchemeRegistry registry = new SchemeRegistry();
		registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		registry.register(new Scheme("https", TrustCertainHostNameFactory.getDefault(m), 443));
		ClientConnectionManager manager = new ThreadSafeClientConnManager(param, registry);
		DefaultHttpClient client = new DefaultHttpClient(manager, param);

		 HttpPost mypPost=new HttpPost(host);
		 HttpResponse response=null;
		 String MyStringResult=null;
		 try {
			StringEntity entity=new StringEntity(jsonObject.toString());
			mypPost.setEntity(entity);
			response=client.execute(mypPost);
			
			
			
			if(response.getStatusLine().getStatusCode()==200){
				MyStringResult=Change(response);
//				return MyStringResult;
				myOnResponse(MyStringResult);
			}else {
				myonErrorResponse("");
			}
		} catch (Exception e) {
			myonErrorResponse("");
		}
		 
//		HttpGet request = new HttpGet("https://www.alipay.com/");
//		HttpResponse response = client.execute(request);
//		HttpEntity entity = response.getEntity();
//		BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
//		StringBuilder result = new StringBuilder();
//		String line = "";
//		while ((line = reader.readLine()) != null) {
//			result.append(line);
//		}
//		
	}
	
	//post进行乱码处理
	private String Change(HttpResponse response) {
		StringBuffer sb = new StringBuffer();
		HttpEntity entity = response.getEntity();
		InputStream is;
		BufferedReader br = null;
		try {
			is = entity.getContent();
			br = new BufferedReader(new InputStreamReader(is, HTTP.UTF_8));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String data = "";
		try {

			while ((data = br.readLine()) != null) {
				sb.append(data);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sb.toString();
	}
	

}
