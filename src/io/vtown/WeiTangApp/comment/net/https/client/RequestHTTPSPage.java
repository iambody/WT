package io.vtown.WeiTangApp.comment.net.https.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-8-4 下午2:25:51
 * 
 */
public class RequestHTTPSPage {

	public static String requestHTTPSPage(Context context, String mUrl) {
		InputStream ins = null;
		String result = "";
		try {
			ins = context.getAssets().open("my.key"); // 下载的证书放到项目中的assets目录中
			CertificateFactory cerFactory = CertificateFactory
					.getInstance("X.509");
			Certificate cer = cerFactory.generateCertificate(ins);
			KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
			keyStore.load(null, null);
			keyStore.setCertificateEntry("trust", cer);

			SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore);
			Scheme sch = new Scheme("https", socketFactory, 443);
			HttpClient mHttpClient = new DefaultHttpClient();
			mHttpClient.getConnectionManager().getSchemeRegistry()
					.register(sch);

			BufferedReader reader = null;
			try {
				HttpGet request = new HttpGet();
				request.setURI(new URI(mUrl));
				HttpResponse response = mHttpClient.execute(request);
				if (response.getStatusLine().getStatusCode() != 200) {
					request.abort();
					return result;
				}

				reader = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				StringBuffer buffer = new StringBuffer();
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				result = buffer.toString();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ins != null)
					ins.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
