package io.vtown.WeiTangApp.bean.bcomment.easy;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2017/1/9.
 */

public class BCCouponsShop extends BBase {

    private String apply_brands;//"apply_brands":"1014691,1014693,1014699",
    private String apply_store;//"apply_store":0,
    private int status;// "status":0,
    private String id;//"id":189,
    private String coupons_name;//"coupons_name":"店铺满减测试",
    private String coupons_money;//"coupons_money":2000,
    private String min_amount;//"min_amount":10000,
    private List<BLCouponsShops> sellerList = new ArrayList<BLCouponsShops>();

    public String getApply_brands() {
        return apply_brands;
    }

    public void setApply_brands(String apply_brands) {
        this.apply_brands = apply_brands;
    }

    public String getApply_store() {
        return apply_store;
    }

    public void setApply_store(String apply_store) {
        this.apply_store = apply_store;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getMin_amount() {
        return min_amount;
    }

    public void setMin_amount(String min_amount) {
        this.min_amount = min_amount;
    }

    public List<BLCouponsShops> getSellerList() {
        return sellerList;
    }

    public void setSellerList(List<BLCouponsShops> sellerList) {
        this.sellerList = sellerList;
    }
}
