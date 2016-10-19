package io.vtown.WeiTangApp.comment.contant;

import io.vtown.WeiTangApp.comment.util.SdCardUtils;
import io.vtown.WeiTangApp.comment.util.StrUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.text.Editable;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-7-29 下午2:37:27
 */
public class CacheUtil {
    // 注意我们需要在Spuit中注销时候需要清除所有缓存数据（账户切换数据也会随即发生变化）！！！！！！！！！！！！！！！！！！！！！！！！！
    /**
     * 首页缓存
     */
    private final static String Sp_HomeCache = "homecachesp";

    /**
     * 我的show圈
     */
    private final static String Sp_MyShowCache = "myshowcachesp";

    /**
     * 商品关注,店铺收藏，浏览记录
     */
    private final static String Sp_Guanzhu = "guznzhusp";

    /**
     * 我的钱包
     */
    private final static String Sp_Center_Wallet = "centerwallets";

    /**
     * 采购单列表
     */
    private final static String Sp_Shop_Purchase_List = "shoppurchase";

    /**
     * 订单管理列表
     */
    private final static String Sp_Shop_Order_List = "shoporder";

    /**
     * 我的订单列表
     */
    private final static String Sp_Center_Order_List = "centerorder";

    /**
     * 资金明细
     */
    private final static String Sp_Center_Wallet_Property = "walletproperty";

    /**
     * 地址管理
     */
    private final static String Sp_Center_Set_Address = "addressmanage";

    /**
     * 店铺里面的所有的缓存 订单管理 ，渠道管理，采购单，品牌代理，商品管理，销售统计
     */
    private final static String Sp_Shop = "shopsp";

    /**
     * 消息首页
     */

    private final static String Sp_New = "newsp";
    /**
     * 分享邀请码的缓存
     */
    private final static String SP_InvitCode = "invitercodesp";

    /**
     * shop===》折线的数据的缓存
     */
    private final static String SP_ShopLine = "shoplinesp";
    /**
     * 卡券的数据的缓存
     */
    private final static String SP_My_Coupons = "coupons";
    /**
     * 银行卡管理的数据的缓存
     */
    private final static String SP_BankCard_Manage = "bankcardlist";

    /**
     * 支付宝管理的数据的缓存
     */
    private final static String SP_Alipay_Manage = "alipaylist";

    /**
     * 银行列表的数据的缓存
     */
    private final static String SP_Bank_List = "banklist";
    /**
     *  银行列表的数据的缓存
     */
//	private final static String SP_Bank_List = "banklist";
    /**
     * 邀请好友的数据的缓存
     */
    private final static String SP_My_Invite_Friend = "invitefriends";

    /**
     * 积分明细的数据的缓存
     */
    private final static String SP_Integral_Detail = "integral";

    /**
     * 店铺等级的数据的缓存
     */
    private final static String SP_Shop_Lv = "shoplv";

    private final static String Sp_MynewHome = "mynewhome";

    /**
     * 返佣明细的数据的缓存
     */
    private final static String SP_Return_Detail = "returndetail";

    /**
     * 缓存首页数据
     */
    public static void Home_Save(Context pcContext, String HomeStr) {
        SharedPreferences Sp = pcContext.getSharedPreferences(Sp_HomeCache,
                Context.MODE_PRIVATE);
        Editor editor = Sp.edit();
        editor.putString("homecachekey", HomeStr);
        editor.commit();
    }

    public static String Home_Get(Context pcContext) {
        SharedPreferences sp = pcContext.getSharedPreferences(Sp_HomeCache,
                Context.MODE_PRIVATE);
        return sp.getString("homecachekey", "");
    }

    /**
     * 我的showshow数据
     */
    public static void MyShow_Save(Context pcContext, String ShowStr) {
        SharedPreferences Sp = pcContext.getSharedPreferences(Sp_MyShowCache,
                Context.MODE_PRIVATE);
        Editor editor = Sp.edit();
        editor.putString("myshowcachekey", ShowStr);
        editor.commit();
    }

    public static String MyShow_Get(Context pcContext) {
        SharedPreferences sp = pcContext.getSharedPreferences(Sp_MyShowCache,
                Context.MODE_PRIVATE);
        return sp.getString("myshowcachekey", "");
    }

    /**
     * 商品关注
     */
    public static void Guanzhu_Good_Save(Context pcContext, String GoodStr) {
        SharedPreferences Sp = pcContext.getSharedPreferences(Sp_Guanzhu,
                Context.MODE_PRIVATE);
        Editor editor = Sp.edit();
        editor.putString("goods", GoodStr);
        editor.commit();

    }

    public static String Guanzhu_Good_Get(Context pcContext) {
        SharedPreferences Sp = pcContext.getSharedPreferences(Sp_Guanzhu,
                Context.MODE_PRIVATE);
        return Sp.getString("goods", "");

    }

    /**
     * 店铺收藏
     */
    public static void Guanzhu_Shop_Save(Context pcContext, String ShopStr) {
        SharedPreferences Sp = pcContext.getSharedPreferences(Sp_Guanzhu,
                Context.MODE_PRIVATE);
        Editor editor = Sp.edit();
        editor.putString("shop", ShopStr);
        editor.commit();

    }

    public static String Guanzhu_Shop_Get(Context pcContext) {
        SharedPreferences Sp = pcContext.getSharedPreferences(Sp_Guanzhu,
                Context.MODE_PRIVATE);
        return Sp.getString("shop", "");

    }

    /**
     * 浏览记录
     */

    public static void Guanzhu_LiuLan_Save(Context pcContext, String ShopStr) {
        SharedPreferences Sp = pcContext.getSharedPreferences(Sp_Guanzhu,
                Context.MODE_PRIVATE);
        Editor editor = Sp.edit();
        editor.putString("liulan", ShopStr);
        editor.commit();

    }

    public static String Guanzhu_LiuLan_Get(Context pcContext) {
        SharedPreferences Sp = pcContext.getSharedPreferences(Sp_Guanzhu,
                Context.MODE_PRIVATE);
        return Sp.getString("liulan", "");

    }

    public static void Guanzhu_LiuLan_Delete(Context pcContext) {
        SharedPreferences Sp = pcContext.getSharedPreferences(Sp_Guanzhu,
                Context.MODE_PRIVATE);
        Editor editor = Sp.edit();
        editor.remove("liulan");
        editor.commit();
    }


    /**
     * 获取我的钱包缓存
     *
     * @param context
     * @return
     */
    public static String Center_Wallet_Get(Context context) {
        SharedPreferences Sp = context.getSharedPreferences(Sp_Center_Wallet,
                Context.MODE_PRIVATE);
        return Sp.getString("wallet_cache", "");
    }

    /**
     * 保存我的钱包缓存
     *
     * @param pcContext
     * @param walletStr
     */
    public static void Center_Wallet_Save(Context pcContext, String walletStr) {
        SharedPreferences Sp = pcContext.getSharedPreferences(Sp_Center_Wallet,
                Context.MODE_PRIVATE);
        Editor editor = Sp.edit();

        editor.putString("wallet_cache", walletStr);

        editor.commit();
    }

    /**
     * 返佣明细
     *
     * @param context
     * @return
     */
    public static String Home_Return_Detail_Get(Context context) {
        SharedPreferences Sp = context.getSharedPreferences(SP_Return_Detail,
                Context.MODE_PRIVATE);
        return Sp.getString("return_detail", "");
    }

    /**
     * 保存返佣明细
     *
     * @param pcContext
     * @param walletStr
     */
    public static void Home_Return_Detail_Save(Context pcContext, String detail) {
        SharedPreferences Sp = pcContext.getSharedPreferences(SP_Return_Detail,
                Context.MODE_PRIVATE);
        Editor editor = Sp.edit();

        editor.putString("return_detail", detail);

        editor.commit();
    }

    /**
     * 获取采购单列表缓存
     *
     * @param context
     * @return
     */
    public static String Shop_Purchase_List_Get(Context context) {
        SharedPreferences Sp = context.getSharedPreferences(
                Sp_Shop_Purchase_List, Context.MODE_PRIVATE);
        return Sp.getString("shop_purchase", "");
    }

    /**
     * 保存采购单列表
     *
     * @param pcContext
     * @param walletStr
     */
    public static void Shop_Purchase_List_Save(Context pcContext, String cache) {
        SharedPreferences Sp = pcContext.getSharedPreferences(
                Sp_Shop_Purchase_List, Context.MODE_PRIVATE);
        Editor editor = Sp.edit();

        editor.putString("shop_purchase", cache);

        editor.commit();
    }

    /**
     * 获取订单管理列表缓存
     *
     * @param context
     * @return
     */
    public static String Shop_Order_List_Get(Context context) {
        SharedPreferences Sp = context.getSharedPreferences(Sp_Shop_Order_List,
                Context.MODE_PRIVATE);
        return Sp.getString("shop_order", "");
    }

    /**
     * 保存订单管理列表
     *
     * @param pcContext
     * @param walletStr
     */
    public static void Shop_Order_List_Save(Context pcContext, String cache) {
        SharedPreferences Sp = pcContext.getSharedPreferences(
                Sp_Shop_Order_List, Context.MODE_PRIVATE);
        Editor editor = Sp.edit();

        editor.putString("shop_order", cache);

        editor.commit();
    }

    /**
     * 获取我的订单列表缓存
     *
     * @param context
     * @return
     */
    public static String Center_Order_List_Get(Context context) {
        SharedPreferences Sp = context.getSharedPreferences(
                Sp_Center_Order_List, Context.MODE_PRIVATE);
        return Sp.getString("center_order", "");
    }

    /**
     * 保存我的订单列表
     *
     * @param pcContext
     * @param walletStr
     */
    public static void Center_Order_List_Save(Context pcContext, String cache) {
        SharedPreferences Sp = pcContext.getSharedPreferences(
                Sp_Center_Order_List, Context.MODE_PRIVATE);
        Editor editor = Sp.edit();

        editor.putString("center_order", cache);

        editor.commit();
    }

    /**
     * 获取资金明细列表缓存
     *
     * @param context
     * @return
     */
    public static String Center_Wallet_Property_Get(Context context) {
        SharedPreferences Sp = context.getSharedPreferences(
                Sp_Center_Wallet_Property, Context.MODE_PRIVATE);
        return Sp.getString("wallet_property", "");
    }

    /**
     * 保存资金明细列表
     *
     * @param pcContext
     * @param walletStr
     */
    public static void Center_Wallet_Property_Save(Context pcContext,
                                                   String cache) {
        SharedPreferences Sp = pcContext.getSharedPreferences(
                Sp_Center_Wallet_Property, Context.MODE_PRIVATE);
        Editor editor = Sp.edit();

        editor.putString("wallet_property", cache);

        editor.commit();
    }


    /**
     * 获取卡券列表缓存
     *
     * @param context
     * @return
     */
    public static String My_Coupons_Get(Context context) {
        SharedPreferences Sp = context.getSharedPreferences(
                SP_My_Coupons, Context.MODE_PRIVATE);
        return Sp.getString("my_coupons", "");
    }

    /**
     * 保存卡券列表
     *
     * @param pcContext
     * @param walletStr
     */
    public static void My_Coupons_Save(Context pcContext,
                                       String cache) {
        SharedPreferences Sp = pcContext.getSharedPreferences(
                SP_My_Coupons, Context.MODE_PRIVATE);
        Editor editor = Sp.edit();

        editor.putString("my_coupons", cache);

        editor.commit();
    }


    /**
     * 获取邀请好友列表缓存
     *
     * @param context
     * @return
     */
    public static String My_Invite_Friends_Get(Context context) {
        SharedPreferences Sp = context.getSharedPreferences(
                SP_My_Invite_Friend, Context.MODE_PRIVATE);
        return Sp.getString("my_invite", "");
    }

    /**
     * 积分明细列表
     *
     * @param pcContext
     * @param walletStr
     */
    public static void Integral_Detail_Save(Context pcContext,
                                              String integral) {
        SharedPreferences Sp = pcContext.getSharedPreferences(
                SP_Integral_Detail, Context.MODE_PRIVATE);
        Editor editor = Sp.edit();

        editor.putString("integral", integral);

        editor.commit();
    }


    /**
     * 积分明细列表缓存
     *
     * @param context
     * @return
     */
    public static String Integral_Detail_Get(Context context) {
        SharedPreferences Sp = context.getSharedPreferences(
                SP_Integral_Detail, Context.MODE_PRIVATE);
        return Sp.getString("integral", "");
    }

    /**
     * 邀请好友列表
     *
     * @param pcContext
     * @param walletStr
     */
    public static void My_Invite_Friends_Save(Context pcContext,
                                              String my_invite) {
        SharedPreferences Sp = pcContext.getSharedPreferences(
                SP_My_Invite_Friend, Context.MODE_PRIVATE);
        Editor editor = Sp.edit();

        editor.putString("my_invite", my_invite);

        editor.commit();
    }


    /**
     * 店铺等级列表
     *
     * @param pcContext
     * @param walletStr
     */
    public static void Shop_Lv_Save(Context pcContext,
                                            String integral) {
        SharedPreferences Sp = pcContext.getSharedPreferences(
                SP_Shop_Lv, Context.MODE_PRIVATE);
        Editor editor = Sp.edit();

        editor.putString("shoplv", integral);

        editor.commit();
    }


    /**
     * 店铺等级列表缓存
     *
     * @param context
     * @return
     */
    public static String Shop_Lv_Get(Context context) {
        SharedPreferences Sp = context.getSharedPreferences(
                SP_Shop_Lv, Context.MODE_PRIVATE);
        return Sp.getString("shoplv", "");
    }


    /**
     * 清除所有的sp缓存数据
     */
    public static void ClearnCache(Context mpContext) {

        Center_Order_List_Save(mpContext, "");
        Shop_Order_List_Save(mpContext, "");
        Shop_Purchase_List_Save(mpContext, "");
        Center_Wallet_Save(mpContext, "");
        Guanzhu_LiuLan_Save(mpContext, "");

        Guanzhu_Shop_Save(mpContext, "");
        Guanzhu_Good_Save(mpContext, "");
        MyShow_Save(mpContext, "");
        Home_Save(mpContext, "");
        MyShow_Save(mpContext, "");
        Center_Set_Initve_Save(mpContext, "");
        Center_Wallet_Bank_List_Save(mpContext,"");
        Shop_Lv_Save(mpContext,"");
        Center_Wallet_Alipay_Save(mpContext,"");
        Center_Wallet_BankCard_Save(mpContext,"");
        Home_Return_Detail_Save(mpContext,"");
        My_Coupons_Save(mpContext,"");
        My_Invite_Friends_Save(mpContext,"");
        Integral_Detail_Save(mpContext,"");
        Center_Wallet_Property_Save(mpContext, "");
        Center_Order_List_Save(mpContext, "");
        Shop_Order_List_Save(mpContext, "");
        Shop_Purchase_List_Save(mpContext, "");
        Shop_Channel_Save(mpContext, "");
        Shop_Brand_Save(mpContext, "");
        New_Save(mpContext, "");
        Center_Set_Address_Save(mpContext, "");
        //需要删除生成的二维码的文件
        SdCardUtils.delFile(SdCardUtils.CodePath(mpContext) + "mycode.jpg");
        SdCardUtils.delFile(SdCardUtils.CodePath(mpContext) + "shopcode.jpg");
    }

    /**
     * shop====>的渠道管理的获取
     */

    public static String Shop_Channel_Get(Context pContext) {
        SharedPreferences Sp = pContext.getSharedPreferences(Sp_Shop,
                Context.MODE_PRIVATE);
        return Sp.getString("shop_channel", "");
    }

    /**
     * shop====>的渠道管理的保存
     */
    public static void Shop_Channel_Save(Context pContext, String ChannelStr) {
        SharedPreferences Sp = pContext.getSharedPreferences(Sp_Shop,
                Context.MODE_PRIVATE);
        Editor editor = Sp.edit();

        editor.putString("shop_channel", ChannelStr);

        editor.commit();
    }

    /**
     * shop====>的品牌代理的获取
     */

    public static String Shop_Brand_Get(Context pContext) {
        SharedPreferences Sp = pContext.getSharedPreferences(Sp_Shop,
                Context.MODE_PRIVATE);
        return Sp.getString("shop_brand", "");
    }

    /**
     * shop====>的品牌代理的保存
     */
    public static void Shop_Brand_Save(Context pContext, String BrandStr) {
        SharedPreferences Sp = pContext.getSharedPreferences(Sp_Shop,
                Context.MODE_PRIVATE);
        Editor editor = Sp.edit();

        editor.putString("shop_brand", BrandStr);

        editor.commit();
    }

    /**
     * 消息界面的缓存获取
     */
    public static String New_Get(Context mPContext) {

        SharedPreferences Sp = mPContext.getSharedPreferences(Sp_New,
                Context.MODE_PRIVATE);
        return Sp.getString("newstr", "");

    }

    /**
     * 消息界面的缓存存放
     */
    public static void New_Save(Context pContext, String newstr) {
        SharedPreferences Sp = pContext.getSharedPreferences(Sp_New,
                Context.MODE_PRIVATE);
        Editor editor = Sp.edit();

        editor.putString("newstr", newstr);

        editor.commit();
    }

    /**
     * 地址管理缓存获取
     */
    public static String Center_Set_Address_Get(Context mPContext) {

        SharedPreferences Sp = mPContext.getSharedPreferences(
                Sp_Center_Set_Address, Context.MODE_PRIVATE);
        return Sp.getString("address", "");

    }

    /**
     * 地址管理缓存
     */
    public static void Center_Set_Address_Save(Context pContext, String newstr) {
        SharedPreferences Sp = pContext.getSharedPreferences(
                Sp_Center_Set_Address, Context.MODE_PRIVATE);
        Editor editor = Sp.edit();

        editor.putString("address", newstr);

        editor.commit();
    }

    /**
     * 获取邀请码的数据
     */
    public static String Center_Set_Initve_Get(Context mPContext) {
        SharedPreferences Sp = mPContext.getSharedPreferences(SP_InvitCode,
                Context.MODE_PRIVATE);
        return Sp.getString("invitcode", "");

    }

    /**
     * 邀请码的保存呢
     */
    public static void Center_Set_Initve_Save(Context pContext,
                                              String invitcodetr) {
        SharedPreferences Sp = pContext.getSharedPreferences(SP_InvitCode,
                Context.MODE_PRIVATE);
        Editor editor = Sp.edit();
        editor.putString("invitcode", invitcodetr);
        editor.commit();
    }

    /**
     * 银行卡列表的数据
     */
    public static String Center_Wallet_BankCard_Get(Context mPContext) {
        SharedPreferences Sp = mPContext.getSharedPreferences(SP_BankCard_Manage,
                Context.MODE_PRIVATE);
        return Sp.getString("bankcardlist", "");

    }

    /**
     * 银行卡列表的保存呢
     */
    public static void Center_Wallet_BankCard_Save(Context pContext,
                                                   String banklist) {
        SharedPreferences Sp = pContext.getSharedPreferences(SP_BankCard_Manage,
                Context.MODE_PRIVATE);
        Editor editor = Sp.edit();
        editor.putString("bankcardlist", banklist);
        editor.commit();
    }

    /**
     * 支付宝列表的数据
     */
    public static String Center_Wallet_Alipay_Get(Context mPContext) {
        SharedPreferences Sp = mPContext.getSharedPreferences(SP_Alipay_Manage,
                Context.MODE_PRIVATE);
        return Sp.getString("alipaylist", "");

    }

    /**
     * 支付宝列表的保存呢
     */
    public static void Center_Wallet_Alipay_Save(Context pContext,
                                                 String alipaylist) {
        SharedPreferences Sp = pContext.getSharedPreferences(SP_Alipay_Manage,
                Context.MODE_PRIVATE);
        Editor editor = Sp.edit();
        editor.putString("alipaylist", alipaylist);
        editor.commit();
    }

    /**
     * 银行列表的数据
     */
    public static String Center_Wallet_Bank_List_Get(Context mPContext) {
        SharedPreferences Sp = mPContext.getSharedPreferences(SP_Bank_List,
                Context.MODE_PRIVATE);
        return Sp.getString("banklist", "");

    }

    /**
     * 银行列表的保存呢
     */
    public static void Center_Wallet_Bank_List_Save(Context pContext,
                                                    String banklist) {
        SharedPreferences Sp = pContext.getSharedPreferences(SP_Bank_List,
                Context.MODE_PRIVATE);
        Editor editor = Sp.edit();
        editor.putString("banklist", banklist);
        editor.commit();
    }


    // 折线图的数据缓存

    /**
     * 获取折线图数据
     */
    public static String Shop_Line_Save(Context pContext, String SpName) {
        SharedPreferences Sp = pContext.getSharedPreferences(SP_ShopLine,
                Context.MODE_PRIVATE);
        return Sp.getString(SpName, "");
    }

    public static void Shop_Line_Get(Context pContext, String SpName,
                                     String LineStr) {
        SharedPreferences Sp = pContext.getSharedPreferences(SP_ShopLine,
                Context.MODE_PRIVATE);
        Editor editor = Sp.edit();
        editor.putString(SpName, LineStr);
        editor.commit();
    }

    public static String NewHome_Get(Context pContext) {
        SharedPreferences Sp = pContext.getSharedPreferences(Sp_MynewHome,
                Context.MODE_PRIVATE);
        return Sp.getString("newhomestr", "");
    }

    public static void NewHome_Save(Context pContext, String Str) {
        SharedPreferences Sp = pContext.getSharedPreferences(Sp_MynewHome,
                Context.MODE_PRIVATE);
        Editor editor = Sp.edit();
        editor.putString("newhomestr", Str);
        editor.commit();

    }


    /**
     * bitmap进行回收
     *
     * @param bitmap
     */
    public static void BitMapRecycle(Bitmap bitmap) {
        try {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

}
