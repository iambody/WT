package io.vtown.WeiTangApp.bean.bcomment.easy.mainsort;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by datutu on 2016/11/2.
 */

public class BSortRang extends BBase {
    private String min;//":0,
    private String max;//":1000

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public BSortRang() {
    }

    public BSortRang(String min, String max) {
        this.min = min;
        this.max = max;
    }
}
