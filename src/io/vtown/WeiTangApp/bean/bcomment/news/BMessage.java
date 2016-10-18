package io.vtown.WeiTangApp.bean.bcomment.news;

import io.vtown.WeiTangApp.bean.bcomment.easy.addgood.BCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @author 本时间总线 无需要进行注销操作！！！！！
 * @version 创建时间：2016-6-17 下午6:43:01
 */
public class BMessage {
    /**
     * 需要注意 当与sp有关时候只需要传递 定义好的type值就ok
     */
    public static final int Tage_Shop_data = 101;
    /**
     * 添加商品时候的分类
     */
    // 添加商品的图片的标识
    public static final int Tage_AddGoodPic = 501;
    // 添加商品描述的标识
    public static final int Tage_AddGoodDescPic = 502;

    // 添加完毕视频后需要把视频的path传递给添加宝贝界面
    public final static int Tage_AddGoodVido = 512;

    /**
     * 订单管理刷新数据的标识
     */
    public static final int Tage_Order_Manage_Updata = 503;

    /**
     * 申请退款的标识
     */
    public static final int Tage_Apply_Refund = 504;

    /**
     * 我的订单刷新数据的标识
     */
    public static final int Tage_Center_Order_Updata = 505;

    /**
     * 我的采购单刷新数据的标识
     */
    public static final int Tage_My_Purchase = 506;

    /**
     * 品牌列刷新
     */
    public static final int Tage_Updata_Brand_list = 1178;



    /**
     * 修改商品价格事件标识
     */
    public float Tage_Edit_Good_Price = 0;

    /**
     * 支付完成后需要跳转相对应的订单界面 之前的activity都需要finsh==》通知
     */
    public static final int Tage_Kill_Self = 701;
    public static final int Tage_Tab_Kill_Self = 7011;
    /**
     * 在选择图片界面 在退出时候需要 finshi掉其他的关联界面
     */
    public static final int Tage_Kill_PicSelect = 703;
    /**
     * 登录时候成功需要kill掉之前的activity标识
     */
    public final static int Tage_Login_Kill_Other = 704;

    /**
     * 有IM信息
     */
    public final static int IM_Have_MSG = 564;

    /**
     * 看了IM信息
     */
    public final static int IM_MSG_READ = 565;

    /**
     * 卖家订单数据刷新
     */
    public final static int ORDER_DETAIL_UPDATE = 4821;

    // 录制视频完毕后的Path
    public String ReCordVidoPath;
    /**
     * 在分享视频时候 录制完毕后通知视频分享页进行重置
     */

    // IM环信的通知表示

    public static final int Tage_IM_Notic = 901;
    private String FromImId;

    // 在店铺中进行收藏取消收藏后 即使刷新上一个搜索店铺的数据
    public static final int Tage_ShopSouFrash = 903;

    // 编辑商品时候的选择图片
    public static final int Tage_AlterGoods_select = 905;

    // 编辑商品时候需区分是从=》宝贝图片&&&图文详情&&&&视频&&&&&&&&&发货地址&&&&&&&&&&&&&&&
    public static final int Tage_Alter_Goods = 907;
    public static final int Tage_Alter_Miaoshu = 908;
    public static final int Tage_Alter_Address = 909;

    /**
     * 刷新银行卡列表
     */
    public static final int Tage_Updata_BankCard_List = 172;

    /**
     * 获取提现账户信息
     */
    public static final int Tage_Updata_Tixian_Message = 888;

    /**
     * 在消息二级页面跳转到订单订单管理也时候需要进行
     */
    public static final int Tage_New_Kill = 711;
    /**
     * 通知TabMain时候需要的的标识
     */
    public static final int Tage_Tab_one = 131;
    public static final int Tage_Tab_two = 132;
    public static final int Tage_Tab_three = 133;
    public static final int Tage_Tab_four = 134;
    public static final int Tage_Tab_five = 135;

    public static final int Tage_Tab_ShopBus = 105;
    // 取消注册
    public static final int Tage_Tab_Im_Regist = 136;
    public static final int Tage_Tab_Im_UnRegist = 137;
    /**
     * 从采购单或我的订单里去付款之后返回页面需要刷新页面
     */
    public static final int Tage_To_Pay_Updata = 508;
    /**
     * 店铺头像改了
     */
    public static final int Tage_Shop_data_shopname_change = 1001;

    /**
     * 店铺描述改了
     */
    public static final int Tage_Shop_data_desc_change = 1002;

    /**
     * 店铺头像改了
     */
    public static final int Tage_Shop_data_cover_change = 1003;

    /**
     * 店铺封面改了
     */
    public static final int Tage_Shop_data_background_change = 1004;


    /**
     * 第一次进来mainactivity 偷偷下载show数据
     */

    public static final int Tage_Main_To_ShowData = 1014;
    /**
     * 操作与show列表相关的信息时候需要进行偷偷刷新
     */
    public static final int Tage_Show_Hind_Load = 1015;
    /**
     * 第一次进来时候 需要通知shop和center进行刷新毛玻璃背景图片
     */
    public static final int Tage_Main_To_ShowGaoSi = 1019;

    /**
     * 申请成功后刷新品牌商首页的申请状态
     */
    public static final int Tage_Brand_Apply_Statue = 1114;


    /**
     * 商品管理中修改品牌商品的库存后通知fragment进行刷新
     */
    public static final int Good_Manger_ToEdit_num = 1601;
    private int PostEditPostion;//fragment中列表的postion跳转到
    private int AlterAllNumber;
    //商品管理中如果修改商品的图片或者图文详情或者视频需要刷新fragment

    public static final int Good_Manger_Frash_Hind = 1607;

    //修改数量的activity通知规格的listview
    public static final int Good_Manger_Edit = 1602;

    //购物车有结算  开始刷新购物车
    public static final int Shop_Frash = 1703;


    public String GoodMangeAlterNUmbe;
    public int GoodMangeAlterPostion;

    private int MessageType;

    private int TabShopBusNumber = 0;

    private int good_numb = 0;
    //fragment切换For all===>nopay
    private int FragmentPostion;
    //申请品牌商代理时候需要进行通知前边 品牌列表刷新的位置
    private int BrandApplyPostion;

    private List<String> tmpArrayList;



    public static final int Tage_Select_Pic_Show = 7788;
    public static final int Tage_Select_Pic_Good_Pics = 7789;
    public static final int Tage_Select_Pic_Good_Desc = 7790;
    public static final int Tage_Select_Pic_ID_Face = 7791;
    public static final int Tage_Select_Pic_ID_Back = 7792;
    //新创建的首页的四个fragment之间的信息传递
    //在首页fragment获取数据后 刷新购物车数量的提示
    public static final int Tage_MainTab_ShopBus=853;



    //添加商品时候填写商品标题后
    public static final int Tage_AddGood_EditTitle = 57;
    private String AddGood_GoodTitle;
    //添加商品时候选择的商品的规格
    public static final int Tage_AddGood_SelectCategory = 58;
    private BCategory AddGoods_SelectCatory;
    //修改品牌商品库存时候的通知和存储修改的数量

    //选择好地址后返回
    public static final int Tage_Select_Address = 10010;
    private  List<String> address_infos;

    //3.0版本************************************************************************************************************
    //绑定页面绑定成功后刷新个人中心的绑定状态
    public static final int Fragment_Center_ChangStatus=670;
    //绑定页面绑定成功后刷新首页的绑定
    public static final int Fragment_Home_Bind=671;



    //退出时候通知所有进行
    public BMessage() {
        super();
    }

    public BMessage(int messageType) {
        super();
        this.MessageType = messageType;
    }

    public String getGoodMangeAlterNUmbe() {
        return GoodMangeAlterNUmbe;
    }

    public void setGoodMangeAlterNUmbe(String goodMangeAlterNUmbe) {
        GoodMangeAlterNUmbe = goodMangeAlterNUmbe;
    }

    public static int getTageShopData() {
        return Tage_Shop_data;
    }

    public int getMessageType() {
        return this.MessageType;
    }

    public void setMessageType(int messageType) {
        MessageType = messageType;
    }

    public int getGoodMangeAlterPostion() {
        return GoodMangeAlterPostion;
    }

    public void setGoodMangeAlterPostion(int goodMangeAlterPostion) {
        GoodMangeAlterPostion = goodMangeAlterPostion;
    }



    public static int getTageOrderManageUpdata() {
        return Tage_Order_Manage_Updata;
    }

    public String getReCordVidoPath() {
        return ReCordVidoPath;
    }

    public void setReCordVidoPath(String reCordVidoPath) {
        ReCordVidoPath = reCordVidoPath;
    }

    public static int getTageCenterOrderUpdata() {
        return Tage_Center_Order_Updata;
    }

    public float getTageEditGoodPrice() {
        return Tage_Edit_Good_Price;
    }

    public void setTageEditGoodPrice(float value) {
        Tage_Edit_Good_Price = value;
    }

    public String getFromImId() {
        return FromImId;
    }

    public void setFromImId(String fromImId) {
        FromImId = fromImId;
    }

    public int getTabShopBusNumber() {
        return TabShopBusNumber;
    }

    public void setTabShopBusNumber(int tabShopBusNumber) {
        TabShopBusNumber = tabShopBusNumber;
    }

    public int getFragmentPostion() {
        return FragmentPostion;
    }

    public void setFragmentPostion(int fragmentPostion) {
        FragmentPostion = fragmentPostion;
    }

    public int getGood_numb() {
        return good_numb;
    }

    public void setGood_numb(int good_numb) {
        this.good_numb = good_numb;
    }

    public List<String> getTmpArrayList() {
        return tmpArrayList;
    }

    public int getBrandApplyPostion() {
        return BrandApplyPostion;
    }

    public void setBrandApplyPostion(int brandApplyPostion) {
        BrandApplyPostion = brandApplyPostion;
    }

    public int getPostEditPostion() {
        return PostEditPostion;
    }

    public void setPostEditPostion(int postEditPostion) {
        PostEditPostion = postEditPostion;
    }

    public int getAlterAllNumber() {
        return AlterAllNumber;
    }

    public void setAlterAllNumber(int alterAllNumber) {
        AlterAllNumber = alterAllNumber;
    }

    public String getAddGood_GoodTitle() {
        return AddGood_GoodTitle;
    }

    public void setAddGood_GoodTitle(String addGood_GoodTitle) {
        AddGood_GoodTitle = addGood_GoodTitle;
    }

    public BCategory getAddGoods_SelectCatory() {
        return AddGoods_SelectCatory;
    }

    public void setAddGoods_SelectCatory(BCategory addGoods_SelectCatory) {
        AddGoods_SelectCatory = addGoods_SelectCatory;
    }

    public void setTmpArrayList(List<String> tmpArrayList) {
        this.tmpArrayList = tmpArrayList;
    }

    public List<String> getAddress_infos() {
        return address_infos;
    }

    public void setAddress_infos(List<String> address_infos) {
        this.address_infos = address_infos;
    }
}
