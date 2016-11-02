package io.vtown.WeiTangApp.bean.bcomment.three_one.search;

import java.util.ArrayList;
import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2016/11/1.
 */

public class BCSearchInfo extends BBase {
//    private List<BLSearchShopAndGood> sellerinfo = new ArrayList<BLSearchShopAndGood>();
//    private List<BLSearchShopAndGood> goodsinfo = new ArrayList<BLSearchShopAndGood>();
//
//    public List<BLSearchShopAndGood> getSellerinfo() {
//        return sellerinfo;
//    }
//
//    public void setSellerinfo(List<BLSearchShopAndGood> sellerinfo) {
//        this.sellerinfo = sellerinfo;
//    }
//
//    public List<BLSearchShopAndGood> getGoodsinfo() {
//        return goodsinfo;
//    }
//
//    public void setGoodsinfo(List<BLSearchShopAndGood> goodsinfo) {
//        this.goodsinfo = goodsinfo;
//    }

    private String sellerinfo;
    private String goodsinfo;

    public String getSellerinfo() {
        return sellerinfo;
    }

    public void setSellerinfo(String sellerinfo) {
        this.sellerinfo = sellerinfo;
    }

    public String getGoodsinfo() {
        return goodsinfo;
    }

    public void setGoodsinfo(String goodsinfo) {
        this.goodsinfo = goodsinfo;
    }
}
