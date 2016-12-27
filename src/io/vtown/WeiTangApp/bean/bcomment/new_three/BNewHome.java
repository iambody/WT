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
    private int member_level;//等级":0,
    private int integral;//积分":1000,
    //    private int isstar;//是否是明星店铺":0,
    private String rebate;//返佣金额":"0.00",
    private int is_activate;//是否激活":0,
    private int bindstatus;//是否绑定":1,
    private String mySub;//我邀请的人数":13,
    private String activitydata;//激活礼包数据 ":"",
    private int is_attendance;//今天是否签到":1
    private List<BLBanner> banner;
    private String category;
    private BNewShop sellerinfo = new BNewShop();
    //激活专区
    private String activityid;//=28
    private String activitytitle;//=激活礼包3
    //等级
//    private int  member_level;//=0
    private String member_level_name;//=青铜店铺

    //是否绑定机器人
    private int is_ropot;

    private String team_num;//=54
    private String member_level_picture;//=http://fs.v-town.cc/StoreLevel_2_201612131544.png

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

    public int getMember_level() {
        return member_level;
    }

    public void setMember_level(int member_level) {
        this.member_level = member_level;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

//    public int getIsstar() {
//        return isstar;
//    }
//
//    public void setIsstar(int isstar) {
//        this.isstar = isstar;
//    }

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

    public int getIs_attendance() {
        return is_attendance;
    }

    public void setIs_attendance(int is_attendance) {
        this.is_attendance = is_attendance;
    }

    public List<BLBanner> getBanner() {
        return banner;
    }

    public void setBanner(List<BLBanner> banner) {
        this.banner = banner;
    }

    public String getMember_level_name() {
        return member_level_name;
    }

    public void setMember_level_name(String member_level_name) {
        this.member_level_name = member_level_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getIs_ropot() {
        return is_ropot;
    }

    public void setIs_ropot(int is_ropot) {
        this.is_ropot = is_ropot;
    }

    public String getTeam_num() {
        return team_num;
    }

    public void setTeam_num(String team_num) {
        this.team_num = team_num;
    }


    public String getMember_level_picture() {
        return member_level_picture;
    }

    public void setMember_level_picture(String member_level_picture) {
        this.member_level_picture = member_level_picture;
    }
}
