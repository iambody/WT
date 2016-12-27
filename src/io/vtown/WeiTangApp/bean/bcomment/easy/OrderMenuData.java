package io.vtown.WeiTangApp.bean.bcomment.easy;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2016/12/27.
 */

public class OrderMenuData extends BBase {
    private String title;
    private int order_status;

    public OrderMenuData(String title,int order_status){
        super();
        this.title = title;
        this.order_status = order_status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }
}
