package io.vtown.WeiTangApp.bean.bcomment.easy.show;

import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by datutu on 2016/9/22.
 */

public class BShow extends BBase {
    private String intro;
    private String goods_id;
    private String seller_id;
    private String is_type;//,    发show形式，0图片 1 视频
    private List<String> imgarr;
    private BComment goodinfo;//商品详情
    private BComment sellerinfo;//店铺详情
    private String vid;
    private String pre_url;

    private String sendnumber;
    private long create_time;

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getIs_type() {
        return is_type;
    }

    public void setIs_type(String is_type) {
        this.is_type = is_type;
    }

    public List<String> getImgarr() {
        return imgarr;
    }

    public void setImgarr(List<String> imgarr) {
        this.imgarr = imgarr;
    }

    public BComment getGoodinfo() {
        return goodinfo;
    }

    public void setGoodinfo(BComment goodinfo) {
        this.goodinfo = goodinfo;
    }

    public BComment getSellerinfo() {
        return sellerinfo;
    }

    public void setSellerinfo(BComment sellerinfo) {
        this.sellerinfo = sellerinfo;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getSendnumber() {
        return sendnumber;
    }

    public void setSendnumber(String sendnumber) {
        this.sendnumber = sendnumber;
    }

    public long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(long create_time) {
        this.create_time = create_time;
    }

    public String getPre_url() {
        return pre_url;
    }

    public void setPre_url(String pre_url) {
        this.pre_url = pre_url;
    }
}
