package io.vtown.WeiTangApp.bean.bcomment.new_three.integral_detail;

import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2016/10/12.
 */

public class BCIntegralDetail extends BBase {
    private String month;
    private List<BLIntegralDetails> list;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<BLIntegralDetails> getList() {
        return list;
    }

    public void setList(List<BLIntegralDetails> list) {
        this.list = list;
    }
}
