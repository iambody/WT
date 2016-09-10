/**
 *
 */
package io.vtown.WeiTangApp.comment.util;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BDComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.easy.BGoodDetail;
import io.vtown.WeiTangApp.bean.bcomment.easy.PicImageItem;
import io.vtown.WeiTangApp.bean.bcomment.easy.addgood.BCategory;
import io.vtown.WeiTangApp.bean.bcomment.easy.show.BLShow;
import io.vtown.WeiTangApp.bean.bcomment.news.BPay;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.comment.contant.PromptManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.alibaba.fastjson.JSON;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.ArrowKeyMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.BufferType;

/**
 * @author 王永奎 E-mail:wangyongkui@ucfgroup.com
 * @version 创建时间：2015-11-5 下午4:54:58
 * @department 互联网金融部
 */

@SuppressLint("SimpleDateFormat")
public class StrUtils {

    /**
     * 判断是否含有特殊字符
     *
     * @param str
     * @return
     */
    public static boolean IsContaintSpecialhare(String str) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }
//

    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param input
     * @return boolean
     */
    public static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断textview是否有内容
     *
     * @param textView1
     * @return
     */
    public static boolean TextIsEmPty(TextView textView1) {
        String str = textView1.getText().toString().trim();

        return isEmpty(str);
    }

    /**
     * 判断EditText 是否为空
     *
     * @param textView2
     * @return
     */
    public static boolean EditTextIsEmPty(EditText textView2) {
        String str = textView2.getText().toString().trim();

        return isEmpty(str);
    }

    /**
     * 获取textview的数据
     */
    public static String TextStrGet(TextView tview) {
        return StrUtils.isEmpty(tview.getText().toString()) ? "" : tview.getText().toString().trim();
    }
    public static String EdStrGet(EditText tview) {
        return StrUtils.isEmpty(tview.getText().toString()) ? "" : tview.getText().toString().trim();
    }
    /**
     * 判断给定字符串是否空白串 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true z
     *
     * @param input这个同时也会判断下是否是 -- 这个空的字段
     * @return boolean
     */
    public static boolean isEmpty1(String input) {
        if (input == null || "".equals(input) || "--".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串转整数
     *
     * @param str
     * @param defValue
     * @return
     */
    public static int toInt(String str, int defValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
        }
        return defValue;
    }

    public static String toStr(int tag) {

        return tag + "";
    }

    /**
     * 分时线的 时间节点数据解析
     */
    public static String TimePointCh(String str) {// 10 10 00000
        return (new StringBuilder((str.substring(0, str.length() - 5))))
                .insert(new StringBuilder((str.substring(0, str.length() - 5)))
                                .length() - 2,
                        ":").toString();
    }

    /**
     * 分时线的 时间节点数据 INT
     */
    public static int TimePointChInt(String str) {// 10 10 00000
        return Integer.valueOf((new StringBuilder((str.substring(0,
                str.length() - 5)))).toString());
    }

    /**
     * 带有逗号的String 转换为 数组列表
     */
    public static String[] StrToArry(String Str) {
        Pattern myPattern = Pattern.compile(",");
        return myPattern.split(Str);
    }

    /**
     * 扫码时候需要进行？截取后边的参数
     */
    public static BPay StrUrlToBean(String Str) {
        if (Str.contains("?")) {
            Str = Str.substring(Str.indexOf("?") + 1, Str.length());
            Str = Str.replace("&", ",");
            try {

                LogUtils.i("ssss");
                String dd = StrToArry1(Str);

                BPay DDDD = JSON.parseObject(dd.trim(), BPay.class);

                return DDDD;

            } catch (Exception e) {
                e.printStackTrace();
                return new BPay();
            }
        } else {

            return new BPay();
        }
    }

    public static String StrToArry1(String Str) {
        Pattern myPattern = Pattern.compile(",");
        String[] datas = myPattern.split(Str);
        JSONObject dat = new JSONObject();

        for (int i = 0; i < datas.length; i++) {
            Pattern myPatternssss = Pattern.compile("=");
            String[] datassssss = myPatternssss.split(datas[i]);
            for (int j = 0; j < datassssss.length; j++) {
                try {
                    dat.put(datassssss[0], datassssss[1]);
                    // dat.put("\""+datassssss[0]+"\"", datassssss[1]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return dat.toString();

    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static int toInt(String obj) {
        try {
            return Integer.parseInt(obj);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 对象转整数
     *
     * @param obj
     * @return 转换异常返回 0
     */
    public static long toLong(String obj) {
        try {
            return Long.parseLong(obj);
        } catch (Exception e) {
        }
        return 0;
    }

    /**
     * 字符串转布尔类型
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static boolean toBool(String b) {
        try {
            return Boolean.parseBoolean(b);
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 字符串转布尔类型
     *
     * @param b
     * @return 转换异常返回 false
     */
    public static float toFloat(String b) {
        try {
            return Float.parseFloat(b);
        } catch (Exception e) {
        }
        return 0f;
    }

    public static float To2Float(Float float1) {
        BigDecimal b = new BigDecimal(float1);
        return b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 时间戳的转换
     *
     * @param lSysTime1
     * @return
     */
    public static String longtostr(long lSysTime1) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 前面的lSysTime是秒数，先乘1000得到毫秒数，再转为java.util.Date类型
        java.util.Date dt = new Date(lSysTime1 * 1000);
        String sDateTime = sdf.format(dt); // 得到精确到秒的表示：08/31/2006 21:08:00
        return sDateTime;

    }

    public static String Md5(String plainText) {
        StringBuffer buf = new StringBuffer("");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            System.out.println("result: " + buf.toString());// 32位的加密

            System.out.println("result: " + buf.toString().substring(8, 24));// 16位的加密

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return buf.toString();
    }

    public static String SHA1(String decript) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("SHA-1");
            digest.update(decript.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * SHA1加密
     *
     * @param val
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getSHA(String val) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("SHA-1");
        md5.update(val.getBytes());
        byte[] m = md5.digest();// 加密
        return GetShaString(m);
    }

    private static String GetShaString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            sb.append(b[i]);
        }
        return sb.toString();
    }

    public static boolean isMobileNO(String mobiles) {// ^1[358]\\d{9}$
        Pattern p = Pattern.compile("^1[24635879]\\d{9}$");

        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "---");
        return m.matches();
    }

    /**
     * 判断如果
     **/
    public static String NullToStr(String str) {

        if (null == str || "".equals(str)) {
            return "--";
        }
        return str;
    }

    /**
     * 判断如果
     **/
    public static String NullToStr1(String str) {

        if (null == str || "".equals(str)) {
            return "";
        }
        return str;
    }

    // 判断字符串是不是空
    public static boolean ISNullStr(String st) {
        if (null == st || "".equals(st) || "--".equals(st)) {
            return true;
        }
        return false;
    }

    /**
     * 处理含有%号的 数据 数据变成两位数
     *
     * @return
     */
    public static String Float2(String scale) {
        boolean IsContain = scale.contains("%");
        scale = scale.replace("%", "");
        DecimalFormat fnum = new DecimalFormat("######0.00");
        String dd = fnum.format(Float.valueOf(scale));
        // NumberFormat formatter = new DecimalFormat("###,###");
        // dd= formatter.format(String.valueOf(dd))+"";
        // DecimalFormat d1 =new DecimalFormat("#,##0.##");
        // dd=d1.format(dd);
        dd = dd + (IsContain ? "%" : "");
        return dd;
    }

    /**
     * 某一字符换特定位置插入
     *
     * @param src
     * @param dec
     * @param position
     * @return
     */
    public static String insertStringInParticularPosition(String src,
                                                          String dec, int position) {
        StringBuffer stringBuffer = new StringBuffer(src);
        return stringBuffer.insert(position, dec).toString();

    }

    /**
     * string中字母转变成大写
     */
    public static String StrUp(String data) {
        String kk = data.toUpperCase();
        return kk;
    }

    /**
     * 判断textview是否为空
     *
     * @return
     */
    public static boolean IsTextViewEmpty(TextView TextttV) {
        return StrUtils.isEmpty(TextttV.getText().toString().trim());
    }



    public static String StrSort(String data) {
        char[] s1 = data.toCharArray();
        System.out.println(s1);
        for (int i = 0; i < s1.length; i++) {
            for (int j = 0; j < i; j++) {
                if (s1[i] < s1[j]) {
                    char temp = s1[i];
                    s1[i] = s1[j];
                    s1[j] = temp;
                }
            }
        }
        return String.valueOf(s1);
    }

    /**
     * 科学记数法转换成普通数字 //eg:3.40256010353E11
     */
    public static String BigNumberToNumber(String number) {
        BigDecimal bd = new BigDecimal(number);// ("3.40256010353E11");
        return bd.toPlainString();
    }

    /**
     * Textview放置文本
     */
    public static void SetTxt(TextView T, String Str) {
        T.setText(NullToStr1(Str));
    }
    public static void SetEdTxt(EditText T, String Str) {
        T.setText(NullToStr1(Str));
    }
    /**
     * 设置字体库的字体
     */
    public static void SetTextType(TextView T, String Str, Context context) {

        Typeface mTypeface = Typeface.createFromAsset(context.getAssets(),
                "font/fzxh.ttf");
        SetTxt(T, Str);
        T.setTypeface(mTypeface);

    }

    /**
     * 判断是否是json结构
     */
    public static boolean isJson(String value) {
        try {
            new JSONObject(value);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }

    /**
     * 进行颜色字体的混淆 默认是前边黑色后边红色
     */
    public static void SetColorsTxt(Context XX, TextView textView,
                                    int RightColor, String left, String right) {
        String AllStr = left + right;
        SpannableString spsString = new SpannableString(AllStr);
        spsString
                .setSpan(
                        new ForegroundColorSpan(XX.getResources().getColor(
                                RightColor)), left.length(), AllStr.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spsString);
    }

    public static void SetColorsTxt(Context XX, TextView textView, int RightColor, String left, String center, String right) {
        String AllStr = left + center + right;
        String leftStr = left + center;
        SpannableString spsString = new SpannableString(AllStr);
        spsString
                .setSpan(
                        new ForegroundColorSpan(XX.getResources().getColor(
                                RightColor)), left.length(), leftStr.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//		spsString.setSpan(new AbsoluteSizeSpan(43), left.length(), leftStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  ;
        textView.setText(spsString);
    }

    public static String[] LsToArray(List<String> da) {
        String array[] = new String[da.size()];
        for (int i = 0; i < da.size(); i++) {
            array[i] = da.get(i);
        }
        return array;
    }

    public static String NullToStr3(String Str) {
        return StrUtils.isEmpty(Str) ? "" : Str;
    }


    private static boolean isBankCard(String id) {
        String telRegex = "^[\\d]{12,20}$";

        return id.matches(telRegex);

    }

    /**
     * 检察银行卡号
     *
     * @param context
     * @param cardNo
     * @return
     */
    public static boolean checkBankCard(Context context, String cardNo) {
        if (StrUtils.isEmpty(cardNo)) {
            PromptManager.ShowMyToast(context, "请输入银行卡号");
            return false;
        }

        if (!isBankCard(cardNo)) {
            PromptManager.ShowMyToast(context, "您输入的银行卡号有误");

            return false;
        }
        return true;
    }

    private static boolean isMoneyValue(String money) {
        String reg = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$";

        return money.matches(reg);

    }

    /**
     * 检察金额输入
     *
     * @param context
     * @param money    输入的金额
     * @param minLimit 金额最小限制（0表示不需要限制）
     * @param maxLimit 金额最大限制（0表示不需要限制）
     * @return
     */
    public static boolean checkMoney(Context context, String money,
                                     float minLimit, float maxLimit) {
        if (isEmpty(money)) {
            PromptManager.ShowMyToast(context, "请输入金额");
            return false;
        }

        if (!isMoneyValue(money)) {
            PromptManager.ShowMyToast(context, "您输入的金额有误");

            return false;
        }
        float Float_money = Float.parseFloat(money);
        if (maxLimit > 0) {
            if (Float_money > maxLimit) {
                PromptManager.ShowMyToast(context, "金额不能超过" + maxLimit);

                return false;
            }
        }

        if (minLimit > 0) {
            if (Float_money < minLimit) {
                PromptManager.ShowMyToast(context, "金额不能小于" + minLimit);

                return false;
            }
        }
        return true;
    }

    /**
     * 检查手机号
     *
     * @param context
     * @param mobile
     * @return
     */
    public static boolean checkMobile(Context context, String mobile) {

        if (isEmpty(mobile)) {
            PromptManager.ShowMyToast(context, "请输入联系电话");
            return false;

        }

        if (!isMobileNO(mobile)) {
            PromptManager.ShowMyToast(context, "联系人电话输入有误");
            return false;
        }
        return true;
    }

    public static boolean isIDNO(String id) {
        String telRegex = "[\\d]{14,17}[0-9a-zA-Z]";
        String Regex = "";

        if (isEmpty(id))
            return false;
        else
            return id.matches(telRegex);
    }

    /**
     * 检查身份证号
     *
     * @param context
     * @param idNo
     * @return
     */
    public static boolean checkIdNo(Context context, String idNo) {
        if (isEmpty(idNo)) {
            PromptManager.ShowMyToast(context, "请输入身份证号码");
            return false;
        }

        if (idNo.length() > 15 && idNo.length() < 18) {
            PromptManager.ShowMyToast(context, "您输入的身份证号码有误");
            return false;
        }

        if (!isIDNO(idNo)) {
            PromptManager.ShowMyToast(context, "您输入的身份证号码有误");
            return false;
        }
        return true;
    }

    /**
     * 上传7牛图片命名
     *
     * @param picType ：图片用途开头,比如头像就传avatar
     * @return
     */
    public static String UploadQNName(String picType) {

        Random random = new Random();

        if (!isEmpty(picType)) {
            return picType + "_" + random.nextInt(100000)
                    + System.currentTimeMillis() + ".jpg";
        }

        return null;
    }

    /**
     * 是否str中仅仅包含数字
     */
    public static boolean IsOnlyNumber(String Str) {
        return Str.matches("^[0-9]*$");
    }

    public static String UploadVido(String picType) {

        Random random = new Random();

        if (!isEmpty(picType)) {
            return picType + "_" + random.nextInt(100000)
                    + System.currentTimeMillis() + ".mp4";
        }

        return null;
    }

    public static Bitmap getBitmapFromPath(String path) {

        if (!new File(path).exists()) {
            System.err.println("getBitmapFromPath: file not exists");
            return null;
        }
        // Bitmap bitmap = Bitmap.createBitmap(1366, 768, Config.ARGB_8888);
        // Canvas canvas = new Canvas(bitmap);
        // Movie movie = Movie.decodeFile(path);
        // movie.draw(canvas, 0, 0);
        //
        // return bitmap;

        byte[] buf = new byte[1024 * 1024];// 1M
        Bitmap bitmap = null;

        try {

            FileInputStream fis = new FileInputStream(path);
            int len = fis.read(buf, 0, buf.length);
            bitmap = BitmapFactory.decodeByteArray(buf, 0, len);
            if (bitmap == null) {
                System.out.println("len= " + len);
                System.err
                        .println("path: " + path + "  could not be decode!!!");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return bitmap;
    }

    /**
     * 图片的路径返回一个bitmap
     */
    public static Bitmap GetBitMapFromPath(String Path) {
//        Bitmap mBitmap = null;
        InputStream io = null;
        try {
            io = new FileInputStream(Path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        BitmapFactory.Options mOptions = new BitmapFactory.Options();
        mOptions.inTempStorage = new byte[100 * 1024];
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;

        mOptions.inInputShareable = true;
        // 设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
        mOptions.inPurgeable = true;
        // width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
        mOptions.inSampleSize = 2;
        // opts.inTempStorage = new byte[100 * 1024];
//        mBitmap = BitmapFactory.decodeStream(io, null, mOptions);
        return BitmapFactory.decodeStream(io, null, mOptions);
    }

    /**
     * @param Path
     * @param Type 1标识宽；；2标识高
     * @return
     */
    public static int GetSizeBitMap(String Path, int Type) {
        Bitmap mBitmap = null;
        InputStream io = null;
        try {
            io = new FileInputStream(Path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
        BitmapFactory.Options mOptions = new BitmapFactory.Options();
        mOptions.inTempStorage = new byte[100 * 1024];
        // 设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收
        mOptions.inJustDecodeBounds = true;
        // width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。
        mOptions.inSampleSize = 4;
        // opts.inTempStorage = new byte[100 * 1024];
        mBitmap = BitmapFactory.decodeStream(io, null, mOptions);
        int width = mOptions.outWidth;
        int height = mOptions.outHeight;
        return 1 == Type ? width : height;
    }

    /**
     * 身份证号打掩码
     *
     * @param idNo
     * @return
     */
    public static String getIdNoFormatForUser(String idNo) {
        //哈哈
        if (idNo == null || "".equals(idNo))
            return "";
        String newIdString = "";
        if (idNo.length() == 18) {
            newIdString = idNo.replace(idNo.substring(5, 16), "***********");
        } else if (idNo.length() == 15) {
            newIdString = idNo.replace(idNo.substring(3, 11), "********");
        }
        return newIdString;
    }

    /**
     * 姓名打掩码
     *
     * @param realName
     * @return
     */
    public static String getRealNameFormatString(String realName) {
        if (isEmpty(realName) || realName.length() < 2) {
            return realName;
        }
        String formatString = realName.substring(0, 1);
        for (int i = 0; i < realName.length() - 1; i++) {
            formatString = formatString + "*";
        }
        return formatString;
    }

    /**
     * 银行卡号打掩码
     *
     * @param cardNo
     * @return
     */
    public static String getCardFormatForUser(String cardNo) {
        if (cardNo == null || "".equals(cardNo))
            return "";
        String newCardNo = "**** **** " + cardNo.substring(cardNo.length() - 4);
        for (int i = 13; i <= cardNo.length(); i++) {
            if (i % 4 == 1)
                newCardNo = " " + newCardNo;
            newCardNo = "*" + newCardNo;
        }
        return newCardNo;
    }

    /**
     * bitmap转换成byte数组进行七牛上传
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 所有获取的金额全部是分为单位=》显示时候需要除以100展示成元
     */

    public static String SetTextForMony(String mony) {
        if (StrUtils.isEmpty(mony))
            return "--";
        Float mFloat = toFloat(mony);
        Float NeedMony = mFloat / 100;
        return String.valueOf(To2Float(NeedMony));
    }

    /**
     * 上传宝贝候需将商品规格的数据保存成json列表返给后台
     */
    public static JSONArray BeansToJson(List<BCategory> data) {

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < data.size(); i++) {
            BCategory daComment = data.get(i);
            JSONObject OutObj = new JSONObject();
            JSONObject InObj = new JSONObject();
            try {
                OutObj.put(
                        "attr_id",
                        System.currentTimeMillis()
                                + new Random().nextInt(1000000)
                                + new Random().nextInt(100000));
                OutObj.put("sell_price", daComment.getAdd_good_attrs_value_3());
                OutObj.put("agent_price", 0);
                OutObj.put("store", daComment.getAdd_good_attrs_value_4());
                OutObj.put("attr_name", daComment.getAdd_good_attrs_value_1()
                        + daComment.getAdd_good_attrs_value_2());
                OutObj.put("vstore", 0);

                InObj.put("p1", daComment.getAdd_good_attrs_id_1());
                InObj.put("v1", daComment.getAdd_good_attrs_value_1());
                InObj.put("c1", daComment.getAdd_good_attrs_name_1());

                InObj.put("p2", daComment.getAdd_good_attrs_id_2());
                InObj.put("v2", daComment.getAdd_good_attrs_value_2());
                InObj.put("c2", daComment.getAdd_good_attrs_name_2());

                OutObj.put("attr_map", InObj);
            } catch (Exception e) {
            }
            jsonArray.put(OutObj);
        }
        return jsonArray;
    }

    /**
     * 上传宝贝时候 上传的图片列表 需要构造成 Url的json列表
     */
//    public static JSONArray ListImages(List<ImageItem> data) {
//        JSONArray jsonArray = new JSONArray();
//        for (int i = 0; i < data.size(); i++) {
//            ImageItem dItem = data.get(i);
//            jsonArray.put(dItem.getThumbnailPath());
//        }
//        return jsonArray;
//    }
    public static String PicLsToStr(List<PicImageItem> data) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < data.size(); i++) {
            jsonArray.put(data.get(i).getWeburl());
        }
        return jsonArray.toString();
    }

    /**
     * 上传宝贝时候获取最大价格最小价格Type=1标识获取最小的////Type=2标识湖区最大的
     */
    public static String AddGoodPriceGet(List<BCategory> data, int Type) {
        float MaxPrice = 0f;
        float MinPrice = 1000000000000f;
        for (int i = 0; i < data.size(); i++) {
            BCategory daComment = data.get(i);
            Float price = Float.parseFloat(daComment
                    .getAdd_good_attrs_value_3());

            if (price >= MaxPrice)
                MaxPrice = price;
            if (price < MinPrice)
                MinPrice = price;
        }

        return 1 == Type ? String.valueOf(MinPrice) : String.valueOf(MaxPrice);
    }

    /**
     * 在商品详情界面BDComment 需要转化成BLComment 为了进行上架并且分享的分享界面的跳转
     * <p/>
     * 只有图片的商品！！！！！！！
     */

    public static BLComment BDtoBL_Comment(BDComment data) {
        BLComment blComment = new BLComment();
        blComment.setId(data.getId());
        blComment.setSeller_id(data.getSeller_id());
        blComment.setIs_type(data.getGoods_info().getRtype());
        blComment.setImgarr(data.getGoods_info().getRoll());
        blComment.setGoodurl(data.getGoods_url());
        blComment.setPre_url(data.getCover());

        blComment.setVid(data.getGoods_info().getVid());

        blComment.getGoodinfo().setTitle(data.getTitle());
        blComment.getSellerinfo().setSeller_name(data.getSeller_name());
        blComment.setGoods_id(data.getId());
        return blComment;
    }

    /**
     * 在商品详情界面BDComment 需要转化成BLComment 为了进行上架并且分享的分享界面的跳转
     * <p/>
     * 只有图片的商品 改变商品详情的bean
     */

    public static BLComment BDtoBL_Comment22(BGoodDetail data) {
        BLComment blComment = new BLComment();
        blComment.setId(data.getId());
        blComment.setSeller_id(data.getSeller_id());
        blComment.setIs_type(data.getGoods_info().getRtype());
        blComment.setImgarr(data.getGoods_info().getRoll());
        blComment.setGoodurl(data.getGoods_url());
        blComment.setPre_url(data.getCover());

        blComment.setVid(data.getGoods_info().getVid());

        blComment.getGoodinfo().setTitle(data.getTitle());
        blComment.getSellerinfo().setSeller_name(data.getSeller_name());
        blComment.setGoods_id(data.getId());
        return blComment;
    }

    // BLShow

    public static BLShow BDtoBL_BLShow(BGoodDetail data) {
        BLShow blComment = new BLShow();
        blComment.setId(data.getId());
        blComment.setSeller_id(data.getSeller_id());
        blComment.setIs_type(data.getGoods_info().getRtype());
        blComment.setImgarr(data.getGoods_info().getRoll());
        blComment.setGoodurl(data.getGoods_url());
        blComment.setPre_url(data.getCover());
        blComment.setVid(data.getGoods_info().getVid());
        blComment.getGoodinfo().setTitle(data.getTitle());
        blComment.getSellerinfo().setSeller_name(data.getSeller_name());
        blComment.setGoods_id(data.getId());
        return blComment;
    }

    /**
     * 支付宝输入中文时校验
     *
     * @param context
     * @param alipay
     */
    public static boolean CheckAlipay(Context context, String alipay) {
        String telRegex = "^[^\u4e00-\u9fa5]{0,}$";
        if (StrUtils.isEmpty(alipay)) {
            PromptManager.ShowMyToast(context, "请输入支付宝账号");
            return false;
        } else {
            if (!alipay.matches(telRegex)) {
                PromptManager.ShowMyToast(context, "支付宝账号格式有误，请重新输入");
                return false;
            }
        }
        return true;

    }

    /**
     * 邮编校验
     *
     * @param context
     * @param code
     */
    public static boolean CheckPostCode(Context context, String code) {
        String telRegex = "^[1-9]\\d{5}$";
        if (StrUtils.isEmpty(code)) {
            PromptManager.ShowMyToast(context, "请输入邮编");
            return false;
        } else {
            if (!code.matches(telRegex)) {
                PromptManager.ShowMyToast(context, "邮编格式有误，请重新输入");
                return false;
            }
        }
        return true;

    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
                .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    /**
     * 判断json是否包含某个key
     */
    public static boolean JsonContainKey(JSONObject oJsonObject, String Key) {
        return !oJsonObject.isNull(Key);
    }

    /**
     * 获取数据
     */
    public static String JsonGetStr(JSONObject obj, String key) {
        String Result = "";
        if (JsonContainKey(obj, key)) {
            try {
                Result = obj.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return Result;

    }

    /**
     * 获取Bitmap宽高比
     *
     * @param bitmap
     * @return
     */
    public static float GetBitmapWidthHeightRatio(Bitmap bitmap) {
        float ratio = 0;
        if (bitmap != null) {
            float width = bitmap.getWidth();
            float height = bitmap.getHeight();
            DecimalFormat decimalFormat = new DecimalFormat(".0");
            ratio = Float.parseFloat(decimalFormat.format(width / height));
        }
        return ratio;

    }

    public static String GetStrs(List<Float> da) {
        String oo = JSON.toJSONString(da);
        return oo;
    }

    // 获得圆角图片的方法
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 让TextView有复制功能
     *
     * @param view
     */
    public static void SetTextViewCopy(TextView view) {
        int __sdkLevel = Build.VERSION.SDK_INT;
        if (__sdkLevel >= 11) {
            view.setTextIsSelectable(true);
        } else {
            view.setFocusableInTouchMode(true);
            view.setFocusable(true);
            view.setClickable(true);
            view.setLongClickable(true);
            view.setMovementMethod(ArrowKeyMovementMethod.getInstance());
            view.setText(view.getText(), BufferType.SPANNABLE);
        }
    }

    /**
     * 图片设置宽高比例
     */
    public static void SetImageRoat(Context mContext, ImageView mImageView,
                                    int ScrenWidth, float roat) {

        LinearLayout.LayoutParams pLayoutParams = new LinearLayout.LayoutParams(
                ScrenWidth, (int) (ScrenWidth * roat / 100));
        mImageView.setLayoutParams(pLayoutParams);

    }

    /**
     * 计算第几页
     *
     * @param Size
     * @return
     */
    public static int ShopDetailPage(int Size) {
        return Size % 10 == 0 ? Size / Size : Size / Size + 1;
    }
}
