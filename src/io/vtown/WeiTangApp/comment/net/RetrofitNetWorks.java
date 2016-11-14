package io.vtown.WeiTangApp.comment.net;

import java.util.Map;

import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.event.interf.NetService;
import io.vtown.WeiTangApp.test.BTest;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static io.vtown.WeiTangApp.comment.net.Retrofit.RetrofitUtils.getRetrofit;

/**
 * Created by datutu on 2016/11/14.
 */

public class RetrofitNetWorks {

    protected static final NetService service = getRetrofit().create(NetService.class);

    //设缓存有效期为1天
    protected static final long CACHE_STALE_SEC = 60 * 60 * 24 * 1;
    //查询缓存的Cache-Control设置，使用缓存
    protected static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置。不使用缓存
    protected static final String CACHE_CONTROL_NETWORK = "max-age=0";


    //POST请求参数以map传入
    public static void verfacationCodePostMap(Map<String, String> map, Observer<BUser> observer) {
        setSubscribe(service.getVerfcationCodePostMap(map), observer);
    }

    //Get请求
    public static void verfacationCodeGet(String tel, String pass, Observer<BUser> observer) {
        setSubscribe(service.getVerfcationGet(tel, pass), observer);
    }

    //****************************************************************************************************************
//Get请求
    public static void GetHome(String tel, String pass, Observer<BTest> observer) {
        setSubscribe(service.GetHome(tel, pass), observer);
    }


    /**
     * 插入观察者
     *
     * @param observable
     * @param observer
     * @param <T>
     */
    public static <T> void setSubscribe(Observable<T> observable, Observer<T> observer) {
        observable.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread())//子线程访问网络
                .observeOn(AndroidSchedulers.mainThread())//回调到主线程
                .subscribe(observer);
    }
}
