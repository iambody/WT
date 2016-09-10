package io.vtown.WeiTangApp.bean.bcomment.easy.zhuanqu;

import java.util.List;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * Created by datutu on 2016/9/10.
 */
public class BZhuan extends BBase {
    private String content;//": "高端驱蚊",
    private String id;//": 21,
    private List<BZhuanQuBean> category;
    private String title;//": "高端驱蚊",
    //    private String update_time;//": 0,
    private String status;//": 1,
    //    private String create_time": 1470652361,
    private String pic_path;//": "http://fs.v-town.cc/activity1470652361",
    //    private String is_delete": 0,
    private String url;//": "1"

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<BZhuanQuBean> getCategory() {
        return category;
    }

    public void setCategory(List<BZhuanQuBean> category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPic_path() {
        return pic_path;
    }

    public void setPic_path(String pic_path) {
        this.pic_path = pic_path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
