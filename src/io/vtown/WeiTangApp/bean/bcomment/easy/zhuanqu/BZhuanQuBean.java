package io.vtown.WeiTangApp.bean.bcomment.easy.zhuanqu;

import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by datutu on 2016/9/10.
 */
public class BZhuanQuBean extends BBase {
    private List<BZhuanquGood>goods;
    private String category_name;


    public List<BZhuanquGood> getGoods() {
        return goods;
    }

    public void setGoods(List<BZhuanquGood> goods) {
        this.goods = goods;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
