package io.vtown.WeiTangApp.bean.bcomment.easy.gooddetail;

import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by datutu on 2016/9/8.
 */
public class BSellInf extends BBase {

    private String cover;//": "http://fs.v-town.cc/cover_14726370750000000000001146798672.jpg",
    private List<String> roll;
    private String deliver;//": "黑龙江省",
    private String create_time;//": 1472637263,
    private String rtype;//": 0,
    private String warehouse;//": "北京仓",
    private String vid;//": "",
    private List<String> intro;
    private String itype;//": 0,
    private String is_revise;//": 0
    private String subtitle;//"subtitle": "",

    private int buy_left;//=0;
    private int buy_max;//=0;buy_max=3




    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<String> getRoll() {
        return roll;
    }

    public void setRoll(List<String> roll) {
        this.roll = roll;
    }

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getRtype() {
        return rtype;
    }

    public void setRtype(String rtype) {
        this.rtype = rtype;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public List<String> getIntro() {
        return intro;
    }

    public void setIntro(List<String> intro) {
        this.intro = intro;
    }

    public String getItype() {
        return itype;
    }

    public void setItype(String itype) {
        this.itype = itype;
    }

    public String getIs_revise() {
        return is_revise;
    }

    public void setIs_revise(String is_revise) {
        this.is_revise = is_revise;
    }

    public int getBuy_left() {
        return buy_left;
    }

    public void setBuy_left(int buy_left) {
        this.buy_left = buy_left;
    }

    public int getBuy_max() {
        return buy_max;
    }

    public void setBuy_max(int buy_max) {
        this.buy_max = buy_max;
    }
}
