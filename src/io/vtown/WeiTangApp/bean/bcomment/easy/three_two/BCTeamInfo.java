package io.vtown.WeiTangApp.bean.bcomment.easy.three_two;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2016/11/17.
 */

public class BCTeamInfo extends BBase {

    private String member_id;//"member_id":" 10012906",
    private String team_num;//"team_num":100,
    private String invite_num;//"invite_num":2675,
    private String invite_activate_num;//"invite_activate_num":38,
    private String team_activate_num;//"team_activate_num":50,
    private String register_num;//"register_num":100,
    private String activate_num;//"activate_num":40
    private String month_sub_activate_num;//"month_sub_activate_num": 4,
    private String month_activate_num;//   "month_activate_num": 4,

    public String getMonth_sub_activate_num() {
        return month_sub_activate_num;
    }

    public void setMonth_sub_activate_num(String month_sub_activate_num) {
        this.month_sub_activate_num = month_sub_activate_num;
    }

    public String getActivate_num() {
        return activate_num;
    }

    public void setActivate_num(String activate_num) {
        this.activate_num = activate_num;
    }

    public String getRegister_num() {
        return register_num;
    }

    public void setRegister_num(String register_num) {
        this.register_num = register_num;
    }

    public String getTeam_activate_num() {
        return team_activate_num;
    }

    public void setTeam_activate_num(String team_activate_num) {
        this.team_activate_num = team_activate_num;
    }

    public String getInvite_activate_num() {
        return invite_activate_num;
    }

    public void setInvite_activate_num(String invite_activate_num) {
        this.invite_activate_num = invite_activate_num;
    }

    public String getInvite_num() {
        return invite_num;
    }

    public void setInvite_num(String invite_num) {
        this.invite_num = invite_num;
    }

    public String getTeam_num() {
        return team_num;
    }

    public void setTeam_num(String team_num) {
        this.team_num = team_num;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }
}
