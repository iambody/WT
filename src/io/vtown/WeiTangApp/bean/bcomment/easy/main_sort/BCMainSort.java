package io.vtown.WeiTangApp.bean.bcomment.easy.main_sort;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2016/12/20.
 */

public class BCMainSort extends BBase {

    private String category;//二级分类
    private String bannerlist;
    private String goodslist;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBannerlist() {
        return bannerlist;
    }

    public void setBannerlist(String bannerlist) {
        this.bannerlist = bannerlist;
    }

    public String getGoodslist() {
        return goodslist;
    }

    public void setGoodslist(String goodslist) {
        this.goodslist = goodslist;
    }
}
