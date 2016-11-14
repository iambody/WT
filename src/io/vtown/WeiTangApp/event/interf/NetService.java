package io.vtown.WeiTangApp.event.interf;

import java.util.Map;

import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.test.BTest;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by datutu on 2016/11/14.
 */

public interface NetService {
//    //GET请求，设置缓存
//    @Headers("Cache-Control: public," + CACHE_CONTROL_CACHE)
//    @GET("bjws/app.user/login")
//    Observable<Verification> getVerfcationGetCache(@Query("tel") String tel, @Query("password") String pass);
//
//
//    @Headers("Cache-Control: public," + CACHE_CONTROL_NETWORK)
//    @GET("bjws/app.menu/getMenu")
//    Observable<MenuBean> getMainMenu();

    //POST请求
    @FormUrlEncoded
    @POST("bjws/app.user/login")
    Observable<BUser> getVerfcationCodePostMap(@FieldMap Map<String, String> map);

    //GET请求
    @GET("bjws/app.user/login")
    Observable<BUser> getVerfcationGet(@Query("tel") String tel, @Query("password") String pass);
    //********************************************************************************************************************************

    //GET请求==>获取首页数据
    @GET("/v1/home/home/index")
    Observable<BTest> GetHome(@Query("member_id") String member_id, @Query("seller_id") String seller_id);
}
