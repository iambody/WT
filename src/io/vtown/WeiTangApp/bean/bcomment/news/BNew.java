package io.vtown.WeiTangApp.bean.bcomment.news;

import io.vtown.WeiTangApp.bean.BBase;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-6-1 下午3:51:21
 */
public class BNew extends BBase {
    // 生成二维码分享********************
    private String share_log;// ": "http://fs.v-town.cc/logo.png",

    private String share_title;// ": "邀请下级",
    private String share_content;// ": " ",
    private String share_url;// ": "http://www.qq.com"
    private String sharing_url;
    private String qrcode;
    private String share_vido_url;//纯视频的合成播放视频网页的url
    private String share_pic;// ": "http://fs.v-town.cc/qrcode/1db35b2bbd147f28f1ffffc1310d7693.jpg",
    // 消息列表*********ANew****************************
    private String id;// ": "219",
    private String member_id;// ": "2",

    private String title;// ": "sdf",
    private String content;// ": "asdf",
    private String message_type;// ": "2",
    private String update_time;// ": "1465967927",
    private String create_time;// ": "1465196209",
    private String is_delete;// ": "0",
    private String source;// ": "dsf",
    private String source_type;// ": "1"
    private String action;
    private String system_message_id;
    private String message_id;
    //获取接口得到的
    private String packet_name;
    private String packet_img;
    //    private String sharing_url;
    private String packet_money;

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    // 消息列表********************ItemNew*******************
    // private String message_id;// ":"22",
    // private String title;// ":"提醒发货",
    // private String content;// ":"提醒卖家发货",
    // private String message_type;// ":"2",
    private String status;// ":"1",

    // private String source;// ":"",
    // private String create_time;// ":"1464578478"
    //
    // ***************销售统计****************


    private String date;// ": "2016-07-04",
    private String value;// ": 0

    public String getShare_log() {
        return share_log;
    }

    public void setShare_log(String share_log) {
        this.share_log = share_log;
    }

    public String getShare_pic() {
        return share_pic;
    }

    public void setShare_pic(String share_pic) {
        this.share_pic = share_pic;
    }

    public String getShare_title() {
        return share_title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public String getShare_content() {
        return share_content;
    }

    public void setShare_content(String share_content) {
        this.share_content = share_content;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getSource_type() {
        return source_type;
    }

    public void setSource_type(String source_type) {
        this.source_type = source_type;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSystem_message_id() {
        return system_message_id;
    }

    public void setSystem_message_id(String system_message_id) {
        this.system_message_id = system_message_id;
    }

    public String getShare_vido_url() {
        return share_vido_url;
    }

    public void setShare_vido_url(String share_vido_url) {
        this.share_vido_url = share_vido_url;
    }

    public String getSharing_url() {
        return sharing_url;
    }

    public void setSharing_url(String sharing_url) {
        this.sharing_url = sharing_url;
    }


    public String getPacket_name() {
        return packet_name;
    }

    public void setPacket_name(String packet_name) {
        this.packet_name = packet_name;
    }

    public String getPacket_img() {
        return packet_img;
    }

    public void setPacket_img(String packet_img) {
        this.packet_img = packet_img;
    }

    public String getPacket_money() {
        return packet_money;
    }

    public void setPacket_money(String packet_money) {
        this.packet_money = packet_money;
    }
}
