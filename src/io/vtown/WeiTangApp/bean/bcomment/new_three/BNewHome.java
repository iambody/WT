package io.vtown.WeiTangApp.bean.bcomment.new_three;

import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by datutu on 2016/10/12.
 */

public class BNewHome extends BBase {
//    private String seller_name;//":"wt02768841",
//    private String cover;//":"http://fs.v-town.cc/DefaultSellerCover.jpg",
//    private String avatar;//":"http://fs.v-town.cc/DefaultSellerAvatar.jpg",
    private String member_level;//等级":0,
    private String integral;//积分":1000,
    private int isstar;//是否是明星店铺":0,
    private String rebate;//返佣金额":"0.00",
    private int is_activate;//是否激活":0,
    private int bindstatus;//是否绑定":1,
    private String mySub;//我邀请的人数":13,
    private String activitydata;//激活礼包数据 ":"",
    private String is_attendance;//今天是否签到":1
    private List<BLBanner> banner;
    private BNewShop sellerinfo=new BNewShop();
    //激活专区
    private String activityid;//=28
    private String activitytitle;//=激活礼包3

    public BNewShop getSellerinfo() {
        return sellerinfo;
    }

    public void setSellerinfo(BNewShop sellerinfo) {
        this.sellerinfo = sellerinfo;
    }

    public String getActivityid() {
        return activityid;
    }

    public void setActivityid(String activityid) {
        this.activityid = activityid;
    }

    public String getActivitytitle() {
        return activitytitle;
    }

    public void setActivitytitle(String activitytitle) {
        this.activitytitle = activitytitle;
    }

    public String getMember_level() {
        return member_level;
    }

    public void setMember_level(String member_level) {
        this.member_level = member_level;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public int getIsstar() {
        return isstar;
    }

    public void setIsstar(int isstar) {
        this.isstar = isstar;
    }

    public String getRebate() {
        return rebate;
    }

    public void setRebate(String rebate) {
        this.rebate = rebate;
    }

    public int getIs_activate() {
        return is_activate;
    }

    public void setIs_activate(int is_activate) {
        this.is_activate = is_activate;
    }

    public int getBindstatus() {
        return bindstatus;
    }

    public void setBindstatus(int bindstatus) {
        this.bindstatus = bindstatus;
    }

    public String getMySub() {
        return mySub;
    }

    public void setMySub(String mySub) {
        this.mySub = mySub;
    }

    public String getActivitydata() {
        return activitydata;
    }

    public void setActivitydata(String activitydata) {
        this.activitydata = activitydata;
    }

    public String getIs_attendance() {
        return is_attendance;
    }

    public void setIs_attendance(String is_attendance) {
        this.is_attendance = is_attendance;
    }

    public List<BLBanner> getBanner() {
        return banner;
    }

    public void setBanner(List<BLBanner> banner) {
        this.banner = banner;
    }
}
