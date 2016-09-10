package io.vtown.WeiTangApp.test;

import io.vtown.WeiTangApp.bean.bcache.BShop;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.news.BPay;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.net.NHttpBaseStr;
import io.vtown.WeiTangApp.comment.net.delet.NHttpDeletBaseStr;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.event.interf.IHttpResult;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.test.AndroidTestCase;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request.Method;

public class DaTuTuTest extends AndroidTestCase {
    public void Tsss() {
        int DDD = StrUtils.toInt(Math.ceil(7f / 10) + "");
        int sssDD = StrUtils.toInt(Math.ceil(10f / 10) + "");
        int sssDssssD = StrUtils.toInt(Math.ceil(14f / 10) + "");
        LogUtils.i("sss");
    }

    /**
     * get请求
     */
    public void NetGet() {
        NHttpBaseStr mBaseStr = new NHttpBaseStr(getContext());
        mBaseStr.setPostResult(new IHttpResult<String>() {

            @Override
            public void onError(String error, int LoadType) {
                LogUtils.i(error);
                LogUtils.i(error);
            }

            @Override
            public void getResult(int Code, String Msg, String Data) {
                LogUtils.i(Data);
                LogUtils.i(Data);
            }
        });
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("keyword[]", "a");
        map.put("keyword[]", "b");
        map.put("keyword[]", "c");
        map.put("sss", "dddd");
        mBaseStr.getData(
                "http://dev.vt.api.v-town.cn:8300/v1/order/order/test", map,
                Method.POST);
    }

    /**
     * 反射机制的测试 （SP为例子）
     */
    public void TestSp() {
        BUser mBUser = new BUser("aaa", "bb", "cc", "dd", "ee", "ff", "dd",
                "gg", "hh", "jj", "ee", "qq");
        String aa = mBUser.toString();

        Spuit.User_Save(getContext(), mBUser);

        BUser mBUser2 = new BUser();
        mBUser2 = Spuit.User_Get(getContext());
        String aasss = mBUser2.toString();
        LogUtils.i("sssssssssss");
        LogUtils.i("sssssssssss");
    }

    public void ShopDataTest() {
        BShop mBShop = new BShop();
        mBShop = Spuit.Shop_Get(getContext());
        LogUtils.i("sssss");

    }

    public void TestHashmapToJson() {

//		HashMap<String, String> MAP = new HashMap<String, String>();
//		MAP.put("aa", "11");
//		MAP.put("bb", "22");
//		MAP.put("cc", "33");
//
//		JSONObject obj = Constants.SignToJson(MAP);

        String sss = URLEncoder.encode("ssssssddcc");
        LogUtils.i("sssssssssss");
        LogUtils.i("sssssssssss");
    }

    public void Delet() {
        List<String> dddd = new ArrayList<String>();

        try {
            dddd = JSON.parseArray(null, String.class);
            LogUtils.i("sssssssssss");
        } catch (Exception e) {
            LogUtils.i("sssssssssss");
        }
        try {
            dddd = JSON.parseArray("", String.class);
            LogUtils.i("sssssssssss");
        } catch (Exception e) {
            LogUtils.i("sssssssssss");
        }

        // My_Item_New_Delet

        // NHttpDeletBaseStr baseStr=new NHttpDeletBaseStr(getContext());
        // baseStr.setPostResult(new IHttpResult<String>() {
        //
        // @Override
        // public void onError(String error, int LoadType) {
        // LogUtils.i("sssssssssss");
        // }
        //
        // @Override
        // public void getResult(int Code, String Msg, String Data) {
        // LogUtils.i("sssssssssss");
        // }
        // });
        // HashMap<String, String>da=new HashMap<String, String>();
        // da.put("member_id", "1");
        // baseStr.getData(Constants.My_Item_New_Delet, da, Method.DELETE);
    }

    public void JsonTest() {
        JSONArray mJsonArray = new JSONArray();
        try {

            JSONObject oJsonObject1 = new JSONObject();
            oJsonObject1.put("attr_id", 1);
            oJsonObject1.put("sell_price", 2);
            oJsonObject1.put("agent_price", 3);
            oJsonObject1.put("store", 4);
            oJsonObject1.put("attr_name", 5);
            oJsonObject1.put("vstore", 5);
            JSONObject inobj1 = new JSONObject();
            inobj1.put("p1", 23);
            inobj1.put("v1", 34);
            inobj1.put("c1", 35);
            inobj1.put("p2", 123);
            inobj1.put("v2", 453);
            inobj1.put("c3", 421);
            oJsonObject1.put("attr_map", inobj1);
            mJsonArray.put(oJsonObject1);

            JSONObject oJsonObject2 = new JSONObject();
            oJsonObject2.put("attr_id", 22);
            oJsonObject2.put("sell_price", 32);
            oJsonObject2.put("agent_price", 43);
            oJsonObject2.put("store", 45);
            oJsonObject2.put("attr_name", 75);
            oJsonObject2.put("vstore", 55);
            JSONObject inobj2 = new JSONObject();
            inobj2.put("p1", 823);
            inobj2.put("v1", 34);
            inobj2.put("c1", 356);
            inobj2.put("p2", 1263);
            inobj2.put("v2", 4653);
            inobj2.put("c3", 4261);
            oJsonObject1.put("attr_map", inobj2);
            mJsonArray.put(oJsonObject2);

        } catch (Exception e) {
            // TODO: handle exception
        }
        String da = mJsonArray.toString();
        LogUtils.i("ssss");
    }


    public void Tesss() {
        // List<String> aa = new ArrayList<String>();
        //
        // try {
        // FlatBufferBuilder da = new FlatBufferBuilder();
        //
        // } catch (Exception e) {
        // LogUtils.i("ssss");
        // }
        LogUtils.i("ssss");
        LogUtils.i("ssss");

    }

    public void TTT() {
        BPay ssddd = StrUtils
                .StrUrlToBean("http://dev.vt.www.v-town.cn/pay/pay/token?type=pay&token=dc39f137c0aa");
        LogUtils.i("ssss");
        LogUtils.i("ssss");
        LogUtils.i("ssss");
        LogUtils.i("ssss");
        LogUtils.i("ssss");
        LogUtils.i("ssss");
    }

    public void datassjson() {
        List<String> dd = new ArrayList<String>();
        dd.add("aaaaaaaaa");
        dd.add("bbbbbbbb");

        String oo = JSON.toJSONString(dd);
        LogUtils.i("ssss");
        LogUtils.i("ssss");
    }

    public void tt() {
        String ss = Constants.RSA("123456789", getContext());
        LogUtils.i("ssss");
        LogUtils.i("ssss");
    }

    /**
     * json包含
     */
    public void jOSNCONTAIN() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("sv", "哈哈");
            obj.put("s", null);
            obj.put("aa", "呼呼");
            obj.put("tt", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        boolean gggg = StrUtils.JsonContainKey(obj, "s");
        boolean bbb = StrUtils.JsonContainKey(obj, "sv");
        boolean nnnn = StrUtils.JsonContainKey(obj, "aa");
        boolean uu = StrUtils.JsonContainKey(obj, "tt");

        boolean sssuu = StrUtils.JsonContainKey(obj, "ttdddddddd");
        try {
            String saa = obj.getString("lllll");
            LogUtils.i("ssss");
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.i("ssss");
        }
        LogUtils.i("ssss");
        LogUtils.i("ssss");

    }

    // 版本号
    public void versiontest() {
        int kkk = Constants.getVersionCode(getContext());
        LogUtils.i("ssss");
        LogUtils.i("ssss");
    }

    public void floastostr() {
        List<Float> ddaaa = new ArrayList<Float>();
        ddaaa.add(0.4f);
        ddaaa.add(0.7f);
        ddaaa.add(1.2f);
        String mydas = StrUtils.GetStrs(ddaaa);
        LogUtils.i("ssss");
        LogUtils.i("ssss");
    }

    public void RsaTest() {
        String aaa = Constants.RSA("abcdee", getContext());
        LogUtils.i("ssss");
        LogUtils.i("ssss");
    }
}
