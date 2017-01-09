package io.vtown.WeiTangApp.bean.bcomment.easy;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2017/1/9.
 */

public class BLCouponsShops extends BBase {
    private String id;//"id":1014691,
    private int is_brand;//"is_brand":1,
    private String seller_name;//"seller_name":"BIO",
    private String cover;//"cover":"http://fs.v-town.cc/photo_p7r5VqMsMdo0sRcOUuTJ5Gcrhh7GL7i1",
    private String attention;//"attention":2,
    private String avatar;//"avatar":"http://fs.v-town.cc/photo_tPElfGcryUu9if8EBw0z9LNXhIUUeigA",
    private String seller_no;//"seller_no":10000004

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIs_brand() {
        return is_brand;
    }

    public void setIs_brand(int is_brand) {
        this.is_brand = is_brand;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSeller_no() {
        return seller_no;
    }

    public void setSeller_no(String seller_no) {
        this.seller_no = seller_no;
    }
}
