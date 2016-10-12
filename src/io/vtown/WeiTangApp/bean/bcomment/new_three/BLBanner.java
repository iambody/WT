package io.vtown.WeiTangApp.bean.bcomment.new_three;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by datutu on 2016/10/12.
 */

public class BLBanner extends BBase {
    private String id;//":36,
    private String title;//":"七夕",
    private String pic_path;//":"http://fs.v-town.cc/advert1470649820",
    private String url;//":"http://dev.vt.m.v-town.cn/activity/index/index?id=1",
    private int advert_type;//":4, // advert_type 类型 1H5首页，2商品详情页，3店铺详情页,4活动详情页
    private String category_id;//":0,
    private String advert_type_str;//":"活动详情页",
    private int is_brand;//

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic_path() {
        return pic_path;
    }

    public void setPic_path(String pic_path) {
        this.pic_path = pic_path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getAdvert_type() {
        return advert_type;
    }

    public void setAdvert_type(int advert_type) {
        this.advert_type = advert_type;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getAdvert_type_str() {
        return advert_type_str;
    }

    public void setAdvert_type_str(String advert_type_str) {
        this.advert_type_str = advert_type_str;
    }

    public int getIs_brand() {
        return is_brand;
    }

    public void setIs_brand(int is_brand) {
        this.is_brand = is_brand;
    }
}
