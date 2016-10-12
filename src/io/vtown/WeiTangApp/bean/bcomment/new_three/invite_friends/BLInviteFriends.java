package io.vtown.WeiTangApp.bean.bcomment.new_three.invite_friends;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2016/10/12.
 */

public class BLInviteFriends extends BBase {

    private String isstar;//"isstar":0,
    private String member_level;// "member_level":0,
    private String avatar;// "avatar":"http://fs.v-town.cc/DefaultSellerAvatar.jpg",
    private String seller_name;//"seller_name":"wt29029064",
    private String seller_no;//"seller_no":10000100,
    private String seller_id;//"seller_id":1014787

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
}
