package io.vtown.WeiTangApp.bean.bcomment.easy.main_sort;

import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2016/12/20.
 */

public class BLBannerListData extends BBase {
    private String id;//"id": 91,
    private String title;// "title": "商城首页测试2",
    private String pic_path;//"pic_path": "http://fs.v-town.cc/advert1482128766",
    private String url;//"url": "http://dev.vt.m.v-town.cn/activity/share/index?id=55",
    private String advert_type;//"advert_type": 4,
    private String category_id;// "category_id": 0,
    private String price;//"price": 0,
    private String recommend_position;// "recommend_position": 2,
    private String status;//"status": 1,
    private String update_time;//"update_time": 0,
    private String create_time;//"create_time": 1482128766,
    private String source_id;//"source_id": 55,
    private String client;//"client": "10",
    private String is_delete;//"is_delete": 0,
    private String sort;//"sort": 2120,
    private String advert_type_str;//"advert_type_str": "活动详情页",
    private String is_brand;//"is_brand": 0,
    private List<BLGoods> goods;

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

    public String getAdvert_type() {
        return advert_type;
    }

    public void setAdvert_type(String advert_type) {
        this.advert_type = advert_type;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getRecommend_position() {
        return recommend_position;
    }

    public void setRecommend_position(String recommend_position) {
        this.recommend_position = recommend_position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getSource_id() {
        return source_id;
    }

    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getAdvert_type_str() {
        return advert_type_str;
    }

    public void setAdvert_type_str(String advert_type_str) {
        this.advert_type_str = advert_type_str;
    }

    public String getIs_brand() {
        return is_brand;
    }

    public void setIs_brand(String is_brand) {
        this.is_brand = is_brand;
    }

    public List<BLGoods> getGoods() {
        return goods;
    }

    public void setGoods(List<BLGoods> goods) {
        this.goods = goods;
    }
}
