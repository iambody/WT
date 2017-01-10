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
    private String coupons_img;//"coupons_img": "http://dev.vt.h5.v-town.cn/images/invite11.jpg?app=3.1",
    private String coupons_url;// "coupons_url": "http://dev.vt.m.v-town.cn/packet/share/conpons?invite_code=ZHN83H&coupons_id=313682&member_id=10014951&id=169",
    private String coupons_type;//"coupons_type": "通用 ¥10.00",
    private String apply_brands;//"apply_brands": "全部品牌",
    private String apply_store;//"apply_store": 0,
    private String apply_goods;// "apply_goods": 0,

    private int donation;//是否可转赠

    public int getDonation() {
        return donation;
    }

    public void setDonation(int donation) {
        this.donation = donation;
    }

    public String getApply_store() {
        return apply_store;
    }

    public void setApply_store(String apply_store) {
        this.apply_store = apply_store;
    }

    public String getApply_goods() {
        return apply_goods;
    }

    public void setApply_goods(String apply_goods) {
        this.apply_goods = apply_goods;
    }

    public String getCoupons_img() {
        return coupons_img;
    }

    public void setCoupons_img(String coupons_img) {
        this.coupons_img = coupons_img;
    }

    public String getApply_brands() {
        return apply_brands;
    }

    public void setApply_brands(String apply_brands) {
        this.apply_brands = apply_brands;
    }

    public String getCoupons_type() {
        return coupons_type;
    }

    public void setCoupons_type(String coupons_type) {
        this.coupons_type = coupons_type;
    }

    public String getCoupons_url() {
        return coupons_url;
    }

    public void setCoupons_url(String coupons_url) {
        this.coupons_url = coupons_url;
    }

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
