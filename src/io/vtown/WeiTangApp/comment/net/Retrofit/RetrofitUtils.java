package io.vtown.WeiTangApp.comment.net.Retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by datutu on 2016/11/14.
 */

public abstract class RetrofitUtils {
    /**
     * 开发环境 的host
     */
    public static String Host = "http://dev.vt.api.v-town.cn";
    /**
     * 测试环境的host 测试环境 需要切换key 需要同时切换SignKey这个字段!!!!!!!!!!!!!!!!!!!!
     */
    public static String WxHost = "https://static.v-town.cc/";

    /**
     * 生产环境
     */
// 	public static String Host = "https://api.v-town.cc";
    //*****************************************************************************************************************************************

    //服务器路径
    private static final String API_SERVER = "http://dev.vt.api.v-town.cn";

    private static Retrofit mRetrofit;
    private static OkHttpClient mOkHttpClient;

    /**
     * 获取Retrofit对象
     *
     * @return
     */
    public static Retrofit getRetrofit() {

        if (null == mRetrofit) {

            if (null == mOkHttpClient) {
                mOkHttpClient = OkHttp3Utils.getOkHttpClient();
            }
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(API_SERVER)
                   .addConverterFactory(ScalarsConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(mOkHttpClient)
                    .build();

        }

        return mRetrofit;
    }

}
