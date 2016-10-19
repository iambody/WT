package io.vtown.WeiTangApp.bean.bcomment.new_three;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by datutu on 2016/10/19.
 */

public class BActive extends BBase {
    public String activityid;//=47;
    public String activitytitle;//=激活礼包3

    public String getActivityid() {
        return activityid;
    }

    public String getActivitytitle() {
        return activitytitle;
    }

    public BActive() {
    }

    public BActive(String activityid, String activitytitle) {
        this.activityid = activityid;
        this.activitytitle = activitytitle;
    }
}
