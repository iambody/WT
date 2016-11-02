package io.vtown.WeiTangApp.bean.bcomment.easy.mainsort;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by datutu on 2016/11/2.
 */

public class BSortRang extends BBase {
    private String price_min;//":0,
    private String price_max;//":1000

    public String getPrice_min() {
        return price_min;
    }

    public void setPrice_min(String price_min) {
        this.price_min = price_min;
    }

    public String getPrice_max() {
        return price_max;
    }

    public void setPrice_max(String price_max) {
        this.price_max = price_max;
    }
}
