package io.vtown.WeiTangApp.bean.bcomment.easy;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by Yihuihua on 2016/9/6.
 */
public class PicImageItem extends BBase {
    private String weburl;
    private String pathurl;


    public PicImageItem(String weburl,String pathurl){
        super();
        this.weburl = weburl;
        this.pathurl = pathurl;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getPathurl() {
        return pathurl;
    }

    public void setPathurl(String pathurl) {
        this.pathurl = pathurl;
    }
}
