package io.vtown.WeiTangApp.bean.bcomment.easy.coupons;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2016/9/2.
 */
public class BLMyCoupons extends BBase{
    private String coupons_id;
    private String taken_time;
    private String coupons_name;
    private String coupons_money;
    private String used_endtime;
    private String used_starttime;
    // private String info;
    private String used_day;
    private String status;// 0:可有，1:可用

    public String getCoupons_id() {
        return coupons_id;
    }

    public void setCoupons_id(String coupons_id) {
        this.coupons_id = coupons_id;
    }

    public String getTaken_time() {
        return taken_time;
    }

    public void setTaken_time(String taken_time) {
        this.taken_time = taken_time;
    }

    public String getCoupons_name() {
        return coupons_name;
    }

    public void setCoupons_name(String coupons_name) {
        this.coupons_name = coupons_name;
    }

    public String getCoupons_money() {
        return coupons_money;
    }

    public void setCoupons_money(String coupons_money) {
        this.coupons_money = coupons_money;
    }

    public String getUsed_endtime() {
        return used_endtime;
    }

    public void setUsed_endtime(String used_endtime) {
        this.used_endtime = used_endtime;
    }

    public String getUsed_starttime() {
        return used_starttime;
    }

    public void setUsed_starttime(String used_starttime) {
        this.used_starttime = used_starttime;
    }

    public String getUsed_day() {
        return used_day;
    }

    public void setUsed_day(String used_day) {
        this.used_day = used_day;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
