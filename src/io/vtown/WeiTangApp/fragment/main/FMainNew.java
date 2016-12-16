package io.vtown.WeiTangApp.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import de.greenrobot.event.EventBus;
import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.adapter.ChatHistoryAdapter;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BLComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.easy.im.ImBrand;
import io.vtown.WeiTangApp.bean.bcomment.news.BMessage;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.LogUtils;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DateUtils;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.ViewHolder;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.comment.view.custom.swipeLayout.CustomSwipeToRefresh;
import io.vtown.WeiTangApp.event.interf.IDialogResult;
import io.vtown.WeiTangApp.fragment.FBase;
import io.vtown.WeiTangApp.ui.comment.im.AChatInf;
import io.vtown.WeiTangApp.ui.title.mynew.AItemNew;

/**
 * Created by Yihuihua on 2016/10/28.1111
 */

public class FMainNew extends FBase implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {


    /**
     * 消息列表
     */
    private CompleteListView mynew_newlist;
    /**
     * 对应的Ap
     */
    private MyMew_Ap myMew_Ap;
    /**
     * 用户信息
     */
    private BUser user_Get;
    // IM会话记录列表
    private ChatHistoryAdapter ImHistoryAdapter;
    // lssss
    private CompleteListView mynew_imlist;
    // 所有的会话列表
    private List<EMConversation> conversationList = new ArrayList<EMConversation>();
    // 微糖 小助手的一个EMConversation
    private EMConversation HeplerConversation = null;
    private LinearLayout new_zhushou_lay;
    private TextView item_my_new_content;
    private TextView new_zhushou_time;
    private CustomSwipeToRefresh fragment_main_new_srollviw;
    private boolean IShow = true;

    @Override
    public void InItView() {
        BaseView = LayoutInflater.from(BaseContext).inflate(R.layout.fragment_new, null);
        EventBus.getDefault().register(this, "NewReciver", BMessage.class);
        user_Get = Spuit.User_Get(BaseContext);
        ICacheBrnadLs();
        IView();
        SetTitleHttpDataLisenter(this);
        ICache();

        refresh();
//        GetNetBrandLs();
    }

    private void ICacheBrnadLs() {
        if (StrUtils.isEmpty(CacheUtil.Im_Brand_Get(BaseContext))) {
            GetNetBrandLs();
        }
    }

    //获取品牌商的列表
    private void GetNetBrandLs() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", user_Get.getId());//"10015086"user_Get.getId()
        FBGetHttpData(map, Constants.Im_Chat_Brand_Ls, Request.Method.GET, 432, LOADHind);
    }

    @Override
    public void InitCreate(Bundle d) {

    }

    private void IView() {
        fragment_main_new_srollviw = (CustomSwipeToRefresh) BaseView.findViewById(R.id.fragment_main_new_srollviw);
        fragment_main_new_srollviw.setOnRefreshListener(this);
        fragment_main_new_srollviw.setRefreshing(false);
        fragment_main_new_srollviw.setColorSchemeResources(R.color.app_fen, R.color.app_fen1, R.color.app_fen2, R.color.app_fen3);
        fragment_main_new_srollviw.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                fragment_main_new_srollviw.setEnabled(fragment_main_new_srollviw.getScrollY() == 0);

            }
        });
        new_zhushou_lay = (LinearLayout) BaseView.findViewById(R.id.new_zhushou_lay);
        item_my_new_content = (TextView) BaseView.findViewById(R.id.item_my_new_content);
        new_zhushou_time = (TextView) BaseView.findViewById(R.id.new_zhushou_time);
        new_zhushou_lay.setOnClickListener(this);

        conversationList.addAll(loadConversationWithRecentChat());

        // IM的view
        mynew_imlist = (CompleteListView) BaseView.findViewById(R.id.mynew_imlist);
        ImHistoryAdapter = new ChatHistoryAdapter(BaseContext, conversationList);
        mynew_imlist.setAdapter(ImHistoryAdapter);
        mynew_imlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                EMConversation conversation = (EMConversation) ImHistoryAdapter
                        .getItem(position);


                EMMessage latmessage = conversation.getLastMessage();
                LogUtils.i("========from========" + latmessage.getFrom());
                LogUtils.i("========tagname========" + conversation.getUserName());
                LogUtils.i("========latmessage.getUserName()========" + latmessage.getUserName());

                EventBus.getDefault().post(new BMessage(BMessage.IM_MSG_READ));
                Intent intent = new Intent(BaseActivity, AChatInf.class);
                try {
                    if (latmessage.direct == EMMessage.Direct.SEND) {// EMMessageDirectionSend
                        // holder.name.setText(ReciverName);
                        intent.putExtra("tagname", conversation.getUserName());
                        String ReciverName = latmessage
                                .getStringAttribute("extReceiveNickname");
                        intent.putExtra("title", latmessage
                                .getStringAttribute("extReceiveNickname"));
                        intent.putExtra("iv", latmessage
                                .getStringAttribute("extReceiveHeadUrl"));
                        startActivity(intent);
                        conversation.resetUnreadMsgCount();
                    }
                    if (latmessage.direct == EMMessage.Direct.RECEIVE) {
                        try {
                            String ReciverName = conversation.getLastMessage()
                                    .getStringAttribute("extSendNickname");
                            String ReciverUrl = conversation.getLastMessage()
                                    .getStringAttribute("extSendHeadUrl");
                            intent.putExtra("tagname",
                                    conversation.getUserName());

                            intent.putExtra("title", ReciverName);
                            intent.putExtra("iv", ReciverUrl);

                            startActivity(intent);
                            conversation.resetUnreadMsgCount();
                        } catch (Exception e) {

                        }
                    }

                    for (int i = conversation.getAllMessages().size() - 1; i > 0; i--) {
                        EMMessage mymewEmMessagesss = conversation
                                .getMessage(i);
                        if (mymewEmMessagesss.direct == EMMessage.Direct.SEND) {
                            intent.putExtra("tagname",
                                    conversation.getUserName());
                            try {
                                intent.putExtra(
                                        "title",
                                        mymewEmMessagesss
                                                .getStringAttribute("extReceiveNickname"));
                            } catch (Exception e) {
                                intent.putExtra("title", "小糖果");
                            }
                            try {
                                intent.putExtra(
                                        "iv",
                                        mymewEmMessagesss
                                                .getStringAttribute("extReceiveHeadUrl"));

                            } catch (Exception e) {
                            }
                            startActivity(intent);
                            conversation.resetUnreadMsgCount();
                            return;

                        }

                    }
                    intent.putExtra("tagname", conversation.getUserName());
                    intent.putExtra("title", "小糖果");
                    intent.putExtra("iv", "");
                    startActivity(intent);
                    conversation.resetUnreadMsgCount();

                } catch (Exception e) {

                }

            }

        });
        mynew_imlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                final EMConversation conversation = (EMConversation) ImHistoryAdapter
                        .getItem(arg2);

                ShowCustomDialog("是否删除该条对话?", "取消", "删除", new IDialogResult() {

                    @Override
                    public void RightResult() {
                        EMChatManager.getInstance().deleteConversation(
                                conversation.getUserName());
                        refresh();

                    }

                    @Override
                    public void LeftResult() {
                    }
                });

                return true;
            }
        });
        // 消息的view
        mynew_newlist = (CompleteListView) BaseView.findViewById(R.id.mynew_newlist);

        myMew_Ap = new MyMew_Ap(R.layout.item_my_new);
        mynew_newlist.setAdapter(myMew_Ap);

        mynew_newlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                BLComment itemdata = (BLComment) arg0.getItemAtPosition(arg2);
                if (itemdata.getMessage_info() == null
                        || StrUtils.isEmpty(itemdata.getMessage_info().getId())) {
                    PromptManager.ShowCustomToast(BaseContext, "暂无消息");
                } else {
                    PromptManager.SkipActivity(BaseActivity, new Intent(
                            BaseActivity, AItemNew.class).putExtra("newtype",
                            itemdata.getMessage_info().getSource_type()));

                }
                if (fragment_main_new_srollviw.isRefreshing()) {
                    fragment_main_new_srollviw.setRefreshing(false);
                }

            }
        });

        mynew_newlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                final BLComment itemdata = (BLComment) arg0
                        .getItemAtPosition(arg2);
                ShowCustomDialog("是否删除该类型消息?", "取消", "确定", new IDialogResult() {

                    @Override
                    public void RightResult() {
                        DeletByType(itemdata.getSource_type());
                    }

                    @Override
                    public void LeftResult() {

                    }
                });
                return true;
            }
        });

    }


    /**
     * 检查是否包含缓存
     */
    private void ICache() {
        if (!StrUtils.isEmpty(CacheUtil.New_Get(BaseContext))) {// 存在缓存

            List<BLComment> comments = new ArrayList<BLComment>();
            try {
                comments = JSON.parseArray(CacheUtil.New_Get(BaseContext),
                        BLComment.class);
                myMew_Ap.Refrsh(comments);
                mynew_imlist.setVisibility(View.VISIBLE);
                new_zhushou_lay.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                mynew_imlist.setVisibility(View.VISIBLE);
                new_zhushou_lay.setVisibility(View.VISIBLE);
                PromptManager.showtextLoading(BaseContext, getResources()
                        .getString(R.string.loading));
                return;
            }
            fragment_main_new_srollviw.setRefreshing(false);
            IData(INITIALIZE);

        } else {// 没有缓存
            PromptManager.showtextLoading(BaseContext, getResources()
                    .getString(R.string.loading));
            fragment_main_new_srollviw.setRefreshing(true);
            IData(REFRESHING);
        }
    }

    /* 获取接口的数据 */
    private void IData(int LoadType) {
        if (LoadType == LOADMOREING) {
            PromptManager.showtextLoading(BaseContext, getResources()
                    .getString(R.string.loading));
        }
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", user_Get.getId());//"10015086"user_Get.getId()
        map.put("api_version", "3.1.0");
        FBGetHttpData(map, Constants.My_New_ls, Request.Method.GET, 0, LoadType);
    }

    @Override
    public void getResult(int Code, String Msg, BComment Data) {

        switch (Data.getHttpResultTage()) {
            case 0:// 初始化进来获取数据
                mynew_imlist.setVisibility(View.VISIBLE);
                // new_zhushou_lay.setVisibility(View.VISIBLE);
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    // 数据加载完毕的操作
                    onError("无更多数据", Data.getHttpLoadType());
                    return;
                }

                List<BLComment> comments = new ArrayList<BLComment>();
                try {
                    comments = JSON.parseArray(Data.getHttpResultStr(),
                            BLComment.class);
                } catch (Exception e) {
                    onError("数据格式错误", Data.getHttpLoadType());
                    return;
                }
                CacheUtil.New_Save(BaseContext, Data.getHttpResultStr());
                // right_right_iv.setVisibility(View.VISIBLE);
                switch (Data.getHttpLoadType()) {
                    case INITIALIZE:

                        myMew_Ap.Refrsh(comments);
                        break;
                    case REFRESHING:
                        fragment_main_new_srollviw.setRefreshing(false);
                        myMew_Ap.Refrsh(comments);

                        break;
                    case LOADMOREING:

                        myMew_Ap.AddRefrsh(comments);
                        break;
                    default:
                        break;
                }

                break;
            case 40:// 清空消息成功
                myMew_Ap.Refrsh(new ArrayList<BLComment>());
                break;
            case 31:// 删除单个消息
                IData(INITIALIZE);
                break;
            case 432://品牌商列表

                if (!StrUtils.isEmpty(Data.getHttpResultStr())) {
                    CacheUtil.Im_Brand_Save(BaseContext, Data.getHttpResultStr());
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onError(String error, int LoadType) {
        mynew_imlist.setVisibility(View.VISIBLE);

    }

    private List<BLComment> FiltrateData(List<BLComment> dda) {
        List<BLComment> dataa = new ArrayList<BLComment>();
        for (int i = 0; i < dda.size(); i++) {
            if (dda.get(i).getMessage_info() != null
                    && !StrUtils.isEmpty(dda.get(i).getMessage_info()
                    .getCreate_time())) {
                dataa.add(dda.get(i));
            }
        }
        return dataa;

    }

    /**
     * 清空消息
     */
    private void Delet() {
        PromptManager.showtextLoading(BaseContext, "努力清理中..");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", user_Get.getId());

        FBGetHttpData(map, Constants.My_Item_New_Delet, Request.Method.DELETE, 40,
                LOADMOREING);
    }

    /**
     * 单个删除消息
     */
    private void DeletByType(String SourceType) {
        // NewDeletByTypes
        SetTitleHttpDataLisenter(this);
        PromptManager.showtextLoading(BaseContext, "删除中....");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("member_id", user_Get.getMember_id());
        map.put("source_type", SourceType);
        FBGetHttpData(map, Constants.NewDeletByType, Request.Method.DELETE, 31,
                REFRESHING);

    }

    public void refresh() {
        conversationList.clear();
        conversationList.addAll(loadConversationWithRecentChat());
        if (ImHistoryAdapter != null) {
            ImHistoryAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取所有会话
     *
     * @return
     */
    private Collection<? extends EMConversation> loadConversationWithRecentChat() {
        Hashtable<String, EMConversation> conversations = EMChatManager
                .getInstance().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    if (!conversation.getUserName().equals(Constants.WtHelper)) {
                        sortList.add(new Pair<Long, EMConversation>(
                                conversation.getLastMessage().getMsgTime(),
                                conversation));

                    } else {
                        HeplerConversation = conversation;
                        Frashherpler();
                    }
                }
            }
        }

        try {
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }


    /**
     * 根据最后一条消息的时间排序
     *
     * @param sortList
     */
    private void sortConversationByLastChatTime(
            List<Pair<Long, EMConversation>> sortList) {
        Collections.sort(sortList,
                new Comparator<Pair<Long, EMConversation>>() {

                    @Override
                    public int compare(Pair<Long, EMConversation> con1,
                                       Pair<Long, EMConversation> con2) {
                        if (con1.first == con2.first) {
                            return 0;
                        } else if (con2.first > con1.first) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });
    }


    /**
     * 刷新小助手数据
     */
    private void Frashherpler() {
        if (HeplerConversation != null) {// 微糖小助手的会话实体存在

            StrUtils.SetTxt(item_my_new_content,
                    getMessage(HeplerConversation.getLastMessage()));

            StrUtils.SetTxt(new_zhushou_time, DateUtils
                    .timeStampToStr1(HeplerConversation.getLastMessage()
                            .getMsgTime()));
            //
        } else {// 不存在

        }

    }

    /**
     * 从一条message里面获取
     *
     * @param message
     * @return
     */
    public String getMessage(EMMessage message) {

        String str = "";

        switch (message.getType()) {

            // 图片消息
            case IMAGE: {
                ImageMessageBody imageBody = (ImageMessageBody) message.getBody();
                str = "图片";
                break;
            }

            case TXT: {
                TextMessageBody txtBody = (TextMessageBody) message.getBody();
                str = txtBody.getMessage();

                break;
            }

        }

        return str;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_right_iv:// 跳转
                ShowCustomDialog("确定清理消息?", "取消", "清理", new IDialogResult() {

                    @Override
                    public void RightResult() {
                        Delet();
                    }

                    @Override
                    public void LeftResult() {
                    }
                });

                // PromptManager.SkipActivity(BaseActivity, new Intent(BaseActivity,
                // AAllNewSet.class));
                break;
            case R.id.new_zhushou_lay:// 微糖小助手 v-town000111222
                Intent intent = new Intent(BaseActivity, AChatInf.class);
                intent.putExtra("tagname", Constants.WtHelper);
                intent.putExtra("title",
                        getResources().getString(R.string.wt_helper));
                intent.putExtra("iv", "");
                intent.putExtra("ishepler", true);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (IShow) {
            Log.i("homewave", "隐藏");

        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.i("homewave", "隐藏");

        } else {
            Log.i("homewave", "显示");
            refresh();
        }

    }

    @Override
    public void onRefresh() {
        IData(REFRESHING);
    }


    private class MyMew_Ap extends BaseAdapter {//
        private LayoutInflater inflater;
        private int ResourceId;
        private List<BLComment> datas = new ArrayList<BLComment>();

        public MyMew_Ap(int resourceId) {
            super();

            this.inflater = LayoutInflater.from(BaseContext);
            this.ResourceId = resourceId;

        }

        /**
         * 刷新
         */
        public void Refrsh(List<BLComment> da) {
            this.datas = FiltrateData(da);
            notifyDataSetChanged();
        }

        /**
         * 添加刷新
         */
        public void AddRefrsh(List<BLComment> da) {
            this.datas.addAll(da);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyMew_Ap.My_New_Item myItem = null;
            if (convertView == null) {
                myItem = new MyMew_Ap.My_New_Item();
                convertView = inflater.inflate(ResourceId, null);
                myItem.item_my_new_title = (TextView) convertView
                        .findViewById(R.id.item_my_new_title);
                myItem.item_my_new_content = (TextView) convertView
                        .findViewById(R.id.item_my_new_content);

                myItem.item_my_new_iv = ViewHolder.get(convertView,
                        R.id.item_myin_new_iv);
                myItem.item_my_new_time = ViewHolder.get(convertView,
                        R.id.item_my_new_time);
                convertView.setTag(myItem);
            } else {
                myItem = (MyMew_Ap.My_New_Item) convertView.getTag();
            }

            BLComment data = datas.get(position);
            if (data.getMessage_info() == null
                    || StrUtils.isEmpty(data.getMessage_info().getId())) {
                StrUtils.SetTxt(myItem.item_my_new_content, "暂无消息");
                myItem.item_my_new_title.setVisibility(View.INVISIBLE);
            } else {
                myItem.item_my_new_title.setVisibility(View.VISIBLE);
                myItem.item_my_new_content.setVisibility(View.VISIBLE);
                StrUtils.SetTxt(myItem.item_my_new_title, data
                        .getMessage_info().getTitle());
                StrUtils.SetTxt(myItem.item_my_new_content, data
                        .getMessage_info().getContent());
                StrUtils.SetTxt(myItem.item_my_new_time, DateUtils
                        .timeStampToStr(StrUtils.toLong(data.getMessage_info()
                                .getCreate_time())));

                int SourType = StrUtils.toInt(data.getSource_type());
                switch (SourType) {
                    case 1:// 消息
                        myItem.item_my_new_iv.setImageResource(R.drawable.new_new);
                        break;
                    case 2:// 支付
                        myItem.item_my_new_iv
                                .setImageResource(R.drawable.new_fukuan);
                        break;
                    case 3:// 订单
                        myItem.item_my_new_iv.setImageResource(R.drawable.new_oder);
                        break;
                    case 5://返佣
                        myItem.item_my_new_iv.setImageResource(R.drawable.ic_fanyong_nor);
                        break;
                    default:
                        break;
                }

            }

            return convertView;
        }

        class My_New_Item {
            ImageView item_my_new_iv;
            TextView item_my_new_title;
            TextView item_my_new_content;
            TextView item_my_new_time;//
        }
    }

    public String ssss = "";

    public void NewReciver(BMessage message) {
        switch (message.getMessageType()) {
            case BMessage.Tage_New_Kill:
                BaseActivity.finish();

                break;

            case 11186:


                break;

            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private ImBrand GetImBrandInf(String Chat) {
        ImBrand myBrnd = null;
        if (!StrUtils.isEmpty(CacheUtil.Im_Brand_Get(BaseContext))) {
            List<ImBrand> brands = JSON.parseArray(CacheUtil.Im_Brand_Get(BaseContext), ImBrand.class);
            for (int i = 0; i < brands.size(); i++) {
                if (brands.get(i).getEmchat().equals(Chat)) {
                    myBrnd = brands.get(i);
                }
            }
        }
        return myBrnd;
    }

}
