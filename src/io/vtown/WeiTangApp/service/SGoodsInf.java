package io.vtown.WeiTangApp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.android.volley.Request;

import java.util.HashMap;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.net.NHttpBaseStr;
import io.vtown.WeiTangApp.event.interf.IHttpResult;

public class SGoodsInf extends Service {



    public static final String Tag = "action";
    public static final String ACTION_START = "StartAction";
    public static final String ACTION_STOP = "StopAction";
    /**
     * 开启线程
     */
    private MyThread myThread = null;


    /**
     * 获取的参数
     */
    private String HostUrL = null;



    /**
     * 是否线程内轮询的标记
     */
    private boolean flag = true;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /**
         * TODO获取参数
         */
        if (intent.getStringExtra("action").equals(ACTION_START)) {// 开始轮询服务
//            Bundle MBundle=intent.getBundleExtra("ex");
            String GoodId = intent.getStringExtra("goodid");

            // TODO 获取参数
            if (this.myThread == null) {
            } else {
                this.myThread.stop();
            }
            this.myThread = new MyThread(GoodId);
            this.myThread.start();
        }
        if (intent.getStringExtra("action").equals(ACTION_STOP)) {// 结束轮询服务
            if (this.myThread == null) {
            } else {
                flag = false;
                stopSelf();

            }

        }
        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().post(new BMessage(2002));
    }


    private class MyThread extends Thread {

        /**
         * 线程参数
         */

        /**
         * 线程的url
         */
        private String goodsid;

        public MyThread(String mygoodsid
        ) {
            super();
            goodsid = mygoodsid;
        }

        @Override
        public void run() {
            while (flag) {
                try {
                    // 每个10秒向服务器发送一次请求
                    Thread.sleep(10 * 10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 采用get方式向服务器发送请求
                NHttpBaseStr NHttpBaseStr = new NHttpBaseStr(getBaseContext());
                NHttpBaseStr.setPostResult(new IHttpResult<String>() {
                    @Override
                    public void getResult(int Code, String Msg, String Data) {
                        EventBus.getDefault().post(new BMessage(2001));
                    }

                    @Override
                    public void onError(String error, int LoadType) {
                        EventBus.getDefault().post(new BMessage(2001));
                    }
                });
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("goods_id", goodsid);
                NHttpBaseStr.getData(Constants.Order_Instant_Info_Select, map, Request.Method.GET);


            }
            if (!flag) {
                stopSelf();
            }
        }
    }


}
