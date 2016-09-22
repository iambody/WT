/**
 *
 */
package io.vtown.WeiTangApp.comment.contant;

import io.vtown.WeiTangApp.bean.bcache.BShop;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.comment.util.IMUtile;
import io.vtown.WeiTangApp.comment.util.StrUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author 王永奎 E-mail:wangyk@nsecurities.cn
 * @version 创建时间：2015-11-30 上午9:21:56 用来保存账号相关数据
 * @department 互联网金融部
 */

public class Spuit {
    public static final int CLZ_BYTE = 1;
    public static final int CLZ_SHORT = 2;
    public static final int CLZ_INTEGER = 3;
    public static final int CLZ_LONG = 4;
    public static final int CLZ_STRING = 5;
    public static final int CLZ_BOOLEAN = 6;
    public static final int CLZ_FLOAT = 7;
    public static final int CLZ_DOUBLE = 8;
    public static final Map<Class<?>, Integer> TYPES;

    static {
        TYPES = new HashMap<Class<?>, Integer>();
        TYPES.put(byte.class, CLZ_BYTE);
        TYPES.put(short.class, CLZ_SHORT);
        TYPES.put(int.class, CLZ_INTEGER);
        TYPES.put(long.class, CLZ_LONG);
        TYPES.put(String.class, CLZ_STRING);
        TYPES.put(boolean.class, CLZ_BOOLEAN);
        TYPES.put(float.class, CLZ_FLOAT);
        TYPES.put(double.class, CLZ_DOUBLE);
    }

    /**
     * 是否首次安装
     */
    private static final String FristSet = "fristset";
    // 首次安装trueORfalse的标识符
    private static final String FristStrmark = "fristtag";

    /**
     * 保存的用户
     */
    private static final String User_Sp = "usersp";
    /**
     * 保存的用户是否登录的信息
     */
    private static final String User_IsLogin_Sp = "isloginp";
    private static final String User_IsLogin_Sp_tag = "islogintag";
    /**
     * 我的店铺的数据
     */
    private static final String Shop_Inf = "myshop_inf";

    /**
     * 我的show里面的json数据
     */
    private static final String Show_Sp = "showsp";
    // show的str存储的key
    private static final String Show_str_key = "showstrkey";

    // 绑定邀请码
    private static final String Invitation_Sp = "invitation_sp";

    /**
     * IM消息
     */
    private static final String IM_MESSAGE = "IM_Message";
    /**
     * 保存购物车的清单数量
     */
    private static final String ShopNumber = "shopnumber_sp";

    /**
     * 搜索推荐的缓存数据
     */
    private static final String SouSouRecommend = "sousoureoment_sp";

    /**
     * 获取是否已经登录过的标识
     */
    public static boolean Frist_IsFrist(Context xx) {
        SharedPreferences sp = xx.getSharedPreferences(FristSet,
                Context.MODE_PRIVATE);
        return sp.getBoolean(FristStrmark, true);

    }

    /**
     * 设置状态值为非首次登录状态
     */
    public static void Frist_Set(Context xx) {
        SharedPreferences sp = xx.getSharedPreferences(FristSet,
                Context.MODE_PRIVATE);
        Editor ed = sp.edit();
        ed.putBoolean(FristStrmark, false);
        ed.commit();
    }

    /**
     * 保存用户是否登录
     */
    public static void IsLogin_Set(Context xx, boolean IsLogin) {
        SharedPreferences sp = xx.getSharedPreferences(User_IsLogin_Sp,
                Context.MODE_PRIVATE);
        Editor ed = sp.edit();
        ed.putBoolean(User_IsLogin_Sp_tag, IsLogin);
        ed.commit();
    }

    /**
     * 获取是否已经登录
     */
    public static boolean IsLogin_Get(Context xx) {
        SharedPreferences sp = xx.getSharedPreferences(User_IsLogin_Sp,
                Context.MODE_PRIVATE);
        return sp.getBoolean(User_IsLogin_Sp_tag, false);
    }

    /**
     * 注销账户
     */
    public static void Login_Out(Context XX) {
        IsLogin_Set(XX, false);
        User_Save(XX, new BUser());
        Shop_Save(XX, new BShop());
        Show_SaveStr(XX, "");
        InvitationCode_Set(XX, false);
        //
        CacheUtil.ClearnCache(XX);// 清除列表的缓存

        if (Spuit.IsLogin_RenZheng_Set(XX)) {
            Spuit.IsLogin_RenZheng_Save(XX, "", "");
        }
        // 清除极光推送
        JPushInterface.setAliasAndTags(XX, "", null, new TagAliasCallback() {
            @Override
            public void gotResult(int arg0, String arg1, Set<String> arg2) {
            }
        });

        /**
         * 清理保存的两个高斯图片
         */
        ImagePathConfig.CleranGaosi(XX);
        // 清除IM的消息接收
        IMUtile.LoginOut();


    }

    /**
     * 保存用户信息TODO注意当用反射区存储Bean时候 需要变量时public类型的
     */
    public static void User_Save(Context xx, BUser mBUser) {
        int Number = 0;
        SharedPreferences sp = xx.getSharedPreferences(User_Sp,
                Context.MODE_PRIVATE);
        Editor ed = sp.edit();
        Class<? extends BUser> clazz = mBUser.getClass();
        Field[] arrFiled = clazz.getDeclaredFields();
        try {
            for (Field f : arrFiled) {
                // int type = TYPES.get(f.getType());
                // switch (type) {
                // case CLZ_BYTE:
                // case CLZ_SHORT:
                // case CLZ_INTEGER:
                // editor.putInt(f.getName(), f.getInt(bean));
                // break;
                // case CLZ_LONG:
                // editor.putLong(f.getName(), f.getLong(bean));
                // break;
                // case CLZ_STRING:
                ed.putString(f.getName(), (String) f.get(mBUser));
                // break; mBUser
                // case CLZ_BOOLEAN:
                // editor.putBoolean(f.getName(), f.getBoolean(bean));
                // break;
                // }
                // }
                ed.commit();
            }
        } catch (IllegalArgumentException e) {

            Number += 1;
            if (Number < 4)
                User_Save(xx, mBUser);
            e.printStackTrace();
        } catch (IllegalAccessException e) {

            Number += 1;
            if (Number < 4)
                User_Save(xx, mBUser);
            e.printStackTrace();
        }
        InvitationCode_Set(xx,
                !(StrUtils.isEmpty(mBUser.getParent_id()) || mBUser
                        .getParent_id().equals("0")));
        ed.commit();
    }

    /**
     * 获取用户信息bean TODO注意当用反射区存储Bean时候 需要变量时public类型的
     */
    public static BUser User_Get(Context xx) {
        int Number = 0;
        SharedPreferences sp = xx.getSharedPreferences(User_Sp,
                Context.MODE_PRIVATE);
        BUser mBUser = new BUser();

        Class<? extends BUser> clazz = mBUser.getClass();
        Field[] arrFiled = clazz.getDeclaredFields();
        try {
            for (Field f : arrFiled) {
                String str = sp.getString(f.getName(), null);
                f.set(mBUser, str);
            }
        } catch (IllegalArgumentException e) {
            Number += 1;
            if (Number < 4)
                User_Get(xx);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Number += 1;
            if (Number < 4)
                User_Get(xx);
            e.printStackTrace();
        }
        return mBUser;
    }

    /**
     * 保存用户是否实名认证
     */
    public static void IsLogin_RenZheng_Save(Context xx, String Name,
                                             String Identity_card) {
        BUser mBUser = User_Get(xx);
        mBUser.setName(Name);
        mBUser.setIdentity_card(Identity_card);
        User_Save(xx, mBUser);
    }

    /**
     * 获取是否是否实名认证
     */
    public static boolean IsLogin_RenZheng_Set(Context xx) {
        if (!Spuit.IsLogin_Get(xx))// 未登录就没有认证
            return false;
        return !StrUtils.isEmpty(User_Get(xx).getName())
                && !StrUtils.isEmpty(User_Get(xx).getIdentity_card());
    }

    /**
     * 保存Shop信息 TODO注意当用反射区存储Bean时候 需要变量时public类型的
     */
    public static void Shop_Save(Context xx, BShop mShop) {
        int Number = 0;
        SharedPreferences sp = xx.getSharedPreferences(Shop_Inf,
                Context.MODE_PRIVATE);
        Editor ed = sp.edit();
        Class<? extends BShop> clazz = mShop.getClass();
        Field[] arrFiled = clazz.getDeclaredFields();
        try {
            for (Field f : arrFiled) {
                int type = TYPES.get(f.getType());
                switch (type) {
                    case CLZ_BYTE:
                    case CLZ_SHORT:
                    case CLZ_INTEGER:
                        ed.putInt(f.getName(), f.getInt(mShop));
                        ed.commit();
                        break;
                    case CLZ_LONG:
                        // editor.putLong(f.getName(), f.getLong(bean));
                        break;
                    case CLZ_STRING:
                        ed.putString(f.getName(), (String) f.get(mShop));
                        ed.commit();
                        break;
                    case CLZ_BOOLEAN:
                        // editor.putBoolean(f.getName(), f.getBoolean(bean));
                        break;
                }
            }

        } catch (IllegalArgumentException e) {

            Number += 1;
            if (Number < 4)
                Shop_Save(xx, mShop);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Number += 1;
            if (Number < 4)
                Shop_Save(xx, mShop);
            e.printStackTrace();
        }

        ed.commit();
    }

    /**
     * 获取用户信息bean TODO注意当用反射区存储Bean时候 需要变量时public类型的
     */
    public static BShop Shop_Get(Context xx) {
        int Number = 0;
        SharedPreferences sp = xx.getSharedPreferences(Shop_Inf,
                Context.MODE_PRIVATE);
        BShop mShop = new BShop();

        Class<? extends BShop> clazz = mShop.getClass();
        Field[] arrFiled = clazz.getDeclaredFields();
        try {
            for (Field f : arrFiled) {
                int type = TYPES.get(f.getType());
                switch (type) {
                    case CLZ_BYTE:
                    case CLZ_SHORT:
                    case CLZ_INTEGER:
                        int str2 = sp.getInt(f.getName(), 0);
                        f.set(mShop, str2);

                        break;
                    case CLZ_LONG:
                        // editor.putLong(f.getName(), f.getLong(bean));
                        break;
                    case CLZ_STRING:
                        String str = sp.getString(f.getName(), null);
                        f.set(mShop, str);
                        break;
                    case CLZ_BOOLEAN:
                        // editor.putBoolean(f.getName(), f.getBoolean(bean));
                        break;
                }

            }
        } catch (IllegalArgumentException e) {
            Number += 1;
            if (Number < 4)
                Shop_Get(xx);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Number += 1;
            if (Number < 4)
                Shop_Get(xx);
            e.printStackTrace();
        }
        return mShop;
    }

    /**
     * 修改shop店铺的头像
     */
    public static void Save_Shop_Head(Context context, String HeadIv) {
        BShop mBShop = Shop_Get(context);
        mBShop.setAvatar(HeadIv);
        Shop_Save(context, mBShop);
    }

    /**
     * 修改shop店铺的背景图片
     */
    public static void Save_Shop_Background(Context context, String BackgroundIv) {
        BShop mBShop = Shop_Get(context);
        mBShop.setCover(BackgroundIv);
        Shop_Save(context, mBShop);
    }

    /**
     * 修改shop店铺昵称
     */
    public static void Save_Shop_Nickname(Context context, String Nickname) {
        BShop mBShop = Shop_Get(context);
        mBShop.setSeller_name(Nickname);
        Shop_Save(context, mBShop);
    }

    /**
     * 修改shop店铺的介绍
     */
    public static void Save_Shop_Introduce(Context context, String Introduce) {
        BShop mBShop = Shop_Get(context);
        mBShop.setIntro(Introduce);
        Shop_Save(context, mBShop);
    }

    /**
     * 保存show列表的Str数据
     */
    public static void Show_SaveStr(Context pContext, String showstr) {
        SharedPreferences sp = pContext.getSharedPreferences(Show_Sp,
                Context.MODE_PRIVATE);
        Editor ed = sp.edit();
        ed.putString(Show_str_key, showstr);
        ed.commit();
    }

    /**
     * 取出show列表的str数据
     */
    public static String Show_GetStr(Context pContext) {
        SharedPreferences sp = pContext.getSharedPreferences(Show_Sp,
                Context.MODE_APPEND);
        return sp.getString(Show_str_key, "");
    }

    /**
     * 是否绑定过
     */
    public static boolean InvitationCode_Get(Context xx) {
        SharedPreferences sp = xx.getSharedPreferences(Invitation_Sp,
                Context.MODE_PRIVATE);
        return sp.getBoolean("bangding", false);

    }

    /**
     * 是否绑定过
     */
    public static void InvitationCode_Set(Context xx, boolean isinvitation) {
        SharedPreferences sp = xx.getSharedPreferences(Invitation_Sp,
                Context.MODE_PRIVATE);
        Editor ed = sp.edit();
        ed.putBoolean("bangding", isinvitation);
        ed.commit();
    }

    /**
     * 获取是否有消息的标志
     *
     * @param context
     * @return
     */
    public static boolean IMMessage_Get(Context context) {
        SharedPreferences sp = context.getSharedPreferences(IM_MESSAGE, Context.MODE_PRIVATE);
        return sp.getBoolean("im_message", false);
    }

    /**
     * 设置消息标志
     *
     * @param context
     * @param immessage
     */
    public static void IMMessage_Set(Context context, boolean immessage) {
        SharedPreferences sp = context.getSharedPreferences(IM_MESSAGE,
                Context.MODE_PRIVATE);
        Editor ed = sp.edit();
        ed.putBoolean("im_message", immessage);
        ed.commit();
    }

    //购物车数量的存储
    public static void ShopBusNumber_Save(Context Pcontext, int Number) {
        SharedPreferences sp = Pcontext.getSharedPreferences(ShopNumber,
                Context.MODE_PRIVATE);
        Editor ed = sp.edit();
        ed.putInt("shopbusnumber", Number);
        ed.commit();
    }

    public static int ShopBusNumber_Get(Context Pcontext) {
        SharedPreferences sp = Pcontext.getSharedPreferences(ShopNumber, Context.MODE_PRIVATE);
        return sp.getInt("shopbusnumber", 0);

    }

    //搜索推荐
    public static void SousouRecommend_Save(Context PconContext, String Recommend) {
        SharedPreferences sp = PconContext.getSharedPreferences(SouSouRecommend,
                Context.MODE_PRIVATE);
        Editor ed = sp.edit();
        ed.putString("sousourecommended", Recommend);
        ed.commit();

    }

    public static String SousouRecommend_Get(Context PPContext) {
        SharedPreferences sp = PPContext.getSharedPreferences(SouSouRecommend, Context.MODE_PRIVATE);
        return sp.getString("sousourecommended", "");
    }

}
