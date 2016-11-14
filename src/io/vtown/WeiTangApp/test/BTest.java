package io.vtown.WeiTangApp.test;

import java.io.Serializable;

/**
 * Created by datutu on 2016/11/14.
 */

public class BTest implements Serializable {

   private String  msg;
    private String  code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "BTest{" +
                "msg='" + msg + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
