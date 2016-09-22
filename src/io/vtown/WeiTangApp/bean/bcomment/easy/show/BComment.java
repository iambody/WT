package io.vtown.WeiTangApp.bean.bcomment.easy.show;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by datutu on 2016/9/22.
 */

public class BComment extends BBase {
    private String cover;//":"http://fs.v-town.cc/cover1.jpg",
    //下边是sellinf
    private String is_brand;//":"0",
    private String avatar;
    private String seller_name;//":"sfasfasdf",

    public String getIs_brand() {
        return is_brand;
    }

    public void setIs_brand(String is_brand) {
        this.is_brand = is_brand;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
}
