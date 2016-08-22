package io.vtown.WeiTangApp.comment.net.https;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.res.AssetManager;

public class FakeX509TrustManager implements X509TrustManager {
	private static TrustManager[] trustManagers;
	private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[] {};

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {

	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {

	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return _AcceptedIssuers;
	}

	public boolean isClientTrusted(X509Certificate[] chain) {
		return true;
	}

	public boolean isServerTrusted(X509Certificate[] chain) {
		return true;
	}

	public static void allowAllSSL() {
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

			@Override
			public boolean verify(String arg0, SSLSession arg1) {
			 
				return true;
			}

		});

		SSLContext context = null;
		if (trustManagers == null) {
			trustManagers = new TrustManager[] { new FakeX509TrustManager() };
		}

		try {
			context = SSLContext.getInstance("TLS");
			context.init(null, trustManagers, new SecureRandom());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}

		HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
		 
	}

	private  void  scsss(Context context) throws IOException{
		AssetManager am = context.getAssets();
		InputStream ins = null;
		 KeyStore keyStore = null;
		try {
			  ins = am.open("srca.cer");
		        //读取证书
		        CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");  //问1
		        Certificate cer = cerFactory.generateCertificate(ins);
		        //创建一个证书库，并将证书导入证书库
		         keyStore = KeyStore.getInstance("PKCS12", "BC");   //问2
		        keyStore.load(null, null);
		        keyStore.setCertificateEntry("trust", cer);
//		        return keyStore;
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
		        ins.close();
		}
		//把咱的证书库作为信任证书库
		SSLSocketFactory socketFactory = null;
		try {
			socketFactory = new SSLSocketFactory(keyStore);
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Scheme sch = new Scheme("https", socketFactory, 443);
		//完工
		HttpClient mHttpClient = new DefaultHttpClient();
		mHttpClient.getConnectionManager().getSchemeRegistry().register(sch);
	}
}
