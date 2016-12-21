package io.vtown.WeiTangApp.bean.bcomment.new_three.invite_friends;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2016/10/12.
 */

public class BLInviteFriends extends BBase {
    private String phone;//"phone":"177****0029",
    private String is_activate;//"is_activate":1
    private String member_level_name;//"member_level_name":"钻石店铺",
    private String isstar;//"isstar":0,
    private String member_level;// "member_level":0,
    private String avatar;// "avatar":"http://fs.v-town.cc/DefaultSellerAvatar.jpg",
    private String seller_name;//"seller_name":"wt29029064",
    private String seller_no;//"seller_no":10000100,
    private String seller_id;//"seller_id":1014787
    private String member_level_picture;//"member_level_picture": "http://fs.v-town.cc/StoreLevel_0_201611021548.jpg"
    private String member_id;
    private String remark;//"remark": "",

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_level_picture() {
        return member_level_picture;
    }

    public void setMember_level_picture(String member_level_picture) {
        this.member_level_picture = member_level_picture;
    }

    public String getIsstar() {
        return isstar;
    }

    public void setIsstar(String isstar) {
        this.isstar = isstar;
    }

    public String getMember_level() {
        return member_level;
    }

    public void setMember_level(String member_level) {
        this.member_level = member_level;
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

    public String getSeller_no() {
        return seller_no;
    }

    public void setSeller_no(String seller_no) {
        this.seller_no = seller_no;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIs_activate() {
        return is_activate;
    }

    public void setIs_activate(String is_activate) {
        this.is_activate = is_activate;
    }

    public String getMember_level_name() {
        return member_level_name;
    }

    public void setMember_level_name(String member_level_name) {
        this.member_level_name = member_level_name;
    }
}
