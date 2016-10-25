package io.vtown.WeiTangApp.bean.bcomment.easy;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2016/10/24.
 */

public class BCMyLeader extends BBase {

   private int member_level;// "member_level":0,
    private String member_level_name;//"member_level_name":"青铜店铺",
    private String phone;//"phone":"17800000002",
    private int is_activate;//"is_activate":1,
    private String avatar;// "avatar":"http://fs.v-town.cc/DefaultSellerAvatar.jpg",
    private String seller_name;//"seller_name":"17800000002",
    private String seller_id;//"seller_id":1014970,
    private String seller_no;//"seller_no":10000283,
    private int is_ropot;//"is_ropot":0

    public int getMember_level() {
        return member_level;
    }

    public void setMember_level(int member_level) {
        this.member_level = member_level;
    }

    public String getMember_level_name() {
        return member_level_name;
    }

    public void setMember_level_name(String member_level_name) {
        this.member_level_name = member_level_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIs_activate() {
        return is_activate;
    }

    public void setIs_activate(int is_activate) {
        this.is_activate = is_activate;
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

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getSeller_no() {
        return seller_no;
    }

    public void setSeller_no(String seller_no) {
        this.seller_no = seller_no;
    }

    public int getIs_ropot() {
        return is_ropot;
    }

    public void setIs_ropot(int is_ropot) {
        this.is_ropot = is_ropot;
    }
}
