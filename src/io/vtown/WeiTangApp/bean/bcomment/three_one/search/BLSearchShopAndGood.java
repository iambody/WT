package io.vtown.WeiTangApp.bean.bcomment.three_one.search;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2016/11/1.
 */

public class BLSearchShopAndGood extends BBase {


    private String seller_no;// "seller_no":1012535,
    private String is_brand;//"is_brand":0,
    private String attention;//"attention":0,
    private String intro;//"intro":""
    private int sales;
    private int score;

    private int is_agent;//"is_agent": 1,
    private String id;//"id": 151,
    private String cover;// "cover": "",
    private String sell_price;//"sell_price": 46900,
    private String title;// "title": "雅诗兰黛特润修护肌透精华露(第六代小棕瓶)（特价）  (50ml",
    private String goods_sid;//"goods_sid": 0,
    private String goods_info_id;//"goods_info_id": 151,
    private String seller_id;//"seller_id": 1014708,
    private String orig_price;// "orig_price": 0,
    private String postage;//"postage": 0
    private String seller_name;//"seller_name":"旺铺301-2016",
    private String avatar;//"avatar":"http://fs.v-town.cc/DefaultSellerAvatar.jpg"

    private String goodsurl;
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public int getIs_agent() {
        return is_agent;
    }

    public void setIs_agent(int is_agent) {
        this.is_agent = is_agent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSell_price() {
        return sell_price;
    }

    public void setSell_price(String sell_price) {
        this.sell_price = sell_price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGoods_sid() {
        return goods_sid;
    }

    public void setGoods_sid(String goods_sid) {
        this.goods_sid = goods_sid;
    }

    public String getGoods_info_id() {
        return goods_info_id;
    }

    public void setGoods_info_id(String goods_info_id) {
        this.goods_info_id = goods_info_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getOrig_price() {
        return orig_price;
    }

    public void setOrig_price(String orig_price) {
        this.orig_price = orig_price;
    }

    public String getPostage() {
        return postage;
    }

    public void setPostage(String postage) {
        this.postage = postage;
    }

    public String getSeller_name() {
        return seller_name;
    }

    public void setSeller_name(String seller_name) {
        this.seller_name = seller_name;
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

    public String getIs_brand() {
        return is_brand;
    }

    public void setIs_brand(String is_brand) {
        this.is_brand = is_brand;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getGoodsurl() {
        return goodsurl;
    }

    public void setGoodsurl(String goodsurl) {
        this.goodsurl = goodsurl;
    }

    public BLSearchShopAndGood() {
    }

    public BLSearchShopAndGood(String seller_name, String avatar, int sales, int score, String sell_price, String orig_price) {
        this.seller_name = seller_name;
        this.avatar = avatar;
        this.sales = sales;
        this.score = score;
        this.sell_price = sell_price;
        this.orig_price = orig_price;
    }
    public BLSearchShopAndGood(String  id,String title,String seller_name, String avatar, int sales, int score, String sell_price, String orig_price) {
        this.id=id;
        this.title=title;
        this.seller_name = seller_name;
        this.avatar = avatar;
        this.sales = sales;
        this.score = score;
        this.sell_price = sell_price;
        this.orig_price = orig_price;
    }
}
