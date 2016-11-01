package io.vtown.WeiTangApp.bean.bcomment.easy;

import java.util.HashMap;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2016/11/1.
 */

public class SerializableMap extends BBase {

    private HashMap<String,String> map ;

    public HashMap<String, String> getMap() {
        return map;
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }
}
