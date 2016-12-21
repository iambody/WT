package io.vtown.WeiTangApp.event.receiver;

import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.ui.afragment.ACenterOder;
import io.vtown.WeiTangApp.ui.afragment.AShopGoodManger;
import io.vtown.WeiTangApp.ui.comment.order.ACenterMyOrder;
import io.vtown.WeiTangApp.ui.comment.order.AShopOrderManager;
import io.vtown.WeiTangApp.ui.comment.order.AShopPurchaseOrder;
import io.vtown.WeiTangApp.ui.title.APay;
import io.vtown.WeiTangApp.ui.title.AReturnDetail;
import io.vtown.WeiTangApp.ui.title.mynew.ANew;
import io.vtown.WeiTangApp.ui.ui.AMainTab;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * @author 作者 大兔兔 wangyongkui@v-town.cc
 * @version 创建时间：2016-5-5 下午5:31:00 极光推送接收者
 */
public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
                + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle
                    .getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            // send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
                .getAction())) {
            Log.d(TAG,
                    "[MyReceiver] 接收到推送下来的自定义消息: "
                            + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
                .getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle
                    .getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
                .getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            //扩展字段
            String EXTRA = bundle.getString(JPushInterface.EXTRA_EXTRA);
            if (!StrUtils.isJson(EXTRA)) {
                Intent newIntent = new Intent(context, ANew.class);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(newIntent);
                //通知消息
                return;
            }
            //包含扩展字段在事件通知
            int source_type = 1;
            int action = 0;
            try {
                JSONObject json = new JSONObject(EXTRA);

                source_type = Integer.parseInt(json.getString("source_type"));
                action = Integer.parseInt(json.getString("action"));
                //Toast.makeText(context,"**source_type**"+source_type+"**action**"+action,Toast.LENGTH_LONG).show();
            } catch (JSONException e) {

                e.printStackTrace();
            }

            switch (source_type) {
                case Constants.SOURCE_TYPE_MESSAGE:// 消息
                    switch (action) {
                        case 0:
                            Intent newIntent = new Intent(context, AMainTab.class);
                            newIntent.putExtra("isNewNotify", true);
                            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(newIntent);
                            break;

                        default:
                            break;
                    }

                    break;
                case Constants.SOURCE_TYPE_PAY:// 支付-----预留

                    break;

                case Constants.SOURCE_TYPE_RETURN://返佣
                    Intent returnIntent = new Intent(context,
                            AReturnDetail.class);
                    returnIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(returnIntent);
                    break;
                case Constants.SOURCE_TYPE_ORDER:// 订单
                    switch (action) {
                        case Constants.ACTION_PT_ORDER://普通下单
                            Intent shopOrderIntent = new Intent(context,
                                    AShopOrderManager.class);
                            //shopOrderIntent.putExtra("order_stutas", 3);
                            shopOrderIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(shopOrderIntent);
                            break;
                        case Constants.ACTION_CG_ORDER://采购下单
                            Intent shopGoodIntent = new Intent(context,
                                    AShopOrderManager.class);
                            shopGoodIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(shopGoodIntent);
                            break;
                        case Constants.ACTION_TO_PAY://付款
                            Intent shopOrderIntent6 = new Intent(context,
                                    AShopOrderManager.class);
                            shopOrderIntent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(shopOrderIntent6);
                            break;
                        case Constants.ACTION_CG_SEND://采购订单发货
                            Intent shopGoodIntent1 = new Intent(context,
                                    AShopPurchaseOrder.class);
                            shopGoodIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(shopGoodIntent1);
                            break;
                        case Constants.ACTION_PT_SEND://普通订单发货
//					Intent shopOrderIntent1 = new Intent(context,
//							AShopOderManage.class);
//					shopOrderIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//							| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					context.startActivity(shopOrderIntent1);
                            Intent shopOrderIntent1 = new Intent(context,
                                    ACenterMyOrder.class);
                            shopOrderIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(shopOrderIntent1);
                            break;
                        case Constants.ACTION_CG_REFUND://采购订单同意退款
                            Intent shopOrderIntent2 = new Intent(context,
                                    AShopPurchaseOrder.class);
                            shopOrderIntent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(shopOrderIntent2);
                            break;
                        case Constants.ACTION_PT_REFUND://普通订单同意退款
                            Intent shopOrderIntent3 = new Intent(context,
                                    ACenterMyOrder.class);
                            shopOrderIntent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(shopOrderIntent3);
                            break;
                        case Constants.ACTION_CG_UNREFUND://采购订单拒绝退款
                            Intent shopOrderIntent4 = new Intent(context,
                                    AShopPurchaseOrder.class);
                            shopOrderIntent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(shopOrderIntent4);
                            break;

                        case Constants.ACTION_PT_UNREFUND://采购订单拒绝退款
                            Intent shopOrderIntent5 = new Intent(context,
                                    ACenterMyOrder.class);
                            shopOrderIntent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(shopOrderIntent5);
                            break;

                        case Constants.ACTION_PT_MODIFY_PRICE:
                            Intent shopOrderIntent7 = new Intent(context,
                                    ACenterMyOrder.class);
                            shopOrderIntent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(shopOrderIntent7);
                            break;

                        default:
                            // 打开自定义的Activity
                            Intent i = new Intent(context, AMainTab.class).putExtra("a",
                                    printBundle(bundle));
                            i.putExtras(bundle);
                            // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(i);

                            break;
                    }

                    break;

                default:
                    // 打开自定义的Activity
                    Intent i = new Intent(context, AMainTab.class).putExtra("a",
                            printBundle(bundle));
                    i.putExtras(bundle);
                    // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(i);
                    break;
            }


        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
                .getAction())) {
            Log.d(TAG,
                    "[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
                            + bundle.getString(JPushInterface.EXTRA_EXTRA));
            // 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
            // 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
                .getAction())) {
            boolean connected = intent.getBooleanExtra(
                    JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction()
                    + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    @SuppressLint("NewApi")
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(
                            bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();
                    // if(true) return json.toString();
                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" + myKey + " - "
                                + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    // send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        // if (MainActivity.isForeground) {
        // String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        // String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        // Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
        // msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
        // if (!ExampleUtil.isEmpty(extras)) {
        // try {
        // JSONObject extraJson = new JSONObject(extras);
        // if (null != extraJson && extraJson.length() > 0) {
        // msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
        // }
        // } catch (JSONException e) {
        //
        // }
        //
        // }
        // context.sendBroadcast(msgIntent);
        // }
    }

}
