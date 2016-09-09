package io.vtown.WeiTangApp.bean.bcomment.easy.show;

import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;
import io.vtown.WeiTangApp.bean.bcomment.BLDComment;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-27 下午5:30:47
 */
public class BLShow extends BBase {
    private String create_time;// =1469604641
    private BLDComment goodinfo = new BLDComment();
    private String goods_id;// =1019
    private String goods_sid;// =1019
    private String goodurl;// =http://dev.vt.m.v-town.cn/goods/index/index?goods_id=1019&type=goods

    private String id;// =854
    private List<String> imgarr;//
    private String intro;// =aaaaaa
    private String is_type;// =1
    private String mygoodurl;// =
    private String pre_url;// =http://fs.v-town.cc/cover_afeCKKLTn1Fckdy0gWNgQqyZQnj7A.jpg
    private String qrcode;// =
    private String ratio;// =0
    private String seller_id;// =619
    private BLDComment sellerinfo = new BLDComment();
    private String sendnumber;// =0
    private String vid;// =http://fs.v-town.cc/video_6XVZv4GDDS86lJEK.mp4

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public BLDComment getGoodinfo() {
        return goodinfo;
    }

    public void setGoodinfo(BLDComment goodinfo) {
        this.goodinfo = goodinfo;
    }

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_sid() {
        return goods_sid;
    }

    public void setGoods_sid(String goods_sid) {
        this.goods_sid = goods_sid;
    }

    public String getGoodurl() {
        return goodurl;
    }

    public void setGoodurl(String goodurl) {
        this.goodurl = goodurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getImgarr() {
        return imgarr;
    }

    public void setImgarr(List<String> imgarr) {
        this.imgarr = imgarr;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getIs_type() {
        return is_type;
    }

    public void setIs_type(String is_type) {
        this.is_type = is_type;
    }

    public String getMygoodurl() {
        return mygoodurl;
    }

    public void setMygoodurl(String mygoodurl) {
        this.mygoodurl = mygoodurl;
    }

    public String getPre_url() {
        return pre_url;
    }

    public void setPre_url(String pre_url) {
        this.pre_url = pre_url;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public BLDComment getSellerinfo() {
        return sellerinfo;
    }

    public void setSellerinfo(BLDComment sellerinfo) {
        this.sellerinfo = sellerinfo;
    }

    public String getSendnumber() {
        return sendnumber;
    }

    public void setSendnumber(String sendnumber) {
        this.sendnumber = sendnumber;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

}
