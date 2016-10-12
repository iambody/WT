package io.vtown.WeiTangApp.bean.bcomment.new_three.invite_friends;

import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2016/10/12.
 */

public class BCInviteFriends extends BBase {

    private String date;
    private List<BLInviteFriends> list;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<BLInviteFriends> getList() {
        return list;
    }

    public void setList(List<BLInviteFriends> list) {
        this.list = list;
    }
}
