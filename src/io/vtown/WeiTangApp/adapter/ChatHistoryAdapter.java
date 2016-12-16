package io.vtown.WeiTangApp.adapter;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.easy.im.ImBrand;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.DateUtils;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContact;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.EMConstant;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatHistoryAdapter extends BaseAdapter {

    Context context;
    List<EMConversation> conversationList;
    private List<EMConversation> copyConversationList;

    public ChatHistoryAdapter(Context context, List<EMConversation> object) {
        this.context = context;
        this.conversationList = object;
    }

    @Override
    public int getCount() {
        return conversationList.size();
    }

    @Override
    public Object getItem(int position) {
        return conversationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.row_chat_history, parent, false);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.unreadLabel = (TextView) convertView
                    .findViewById(R.id.unread_msg_number);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.list_item_layout = (RelativeLayout) convertView
                    .findViewById(R.id.list_item_layout);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // EMConversation conversation = (EMConversation) getItem(position);
        EMConversation conversation = conversationList.get(position);

        EMMessage mymewEmMessage = conversation.getLastMessage();
        // 设置用户名的显示

        holder.message.setText(conversation.getLastMessage().toString());
        if (Constants.WtHelper.equals(conversation.getUserName())) {// 是微糖小助手
            // 就隐藏
            convertView.setVisibility(View.GONE);
        }
        // 设置未读数目显示
        if (conversation.getUnreadMsgCount() > 0) {
            holder.unreadLabel.setText(conversation.getUnreadMsgCount() + "");
            holder.unreadLabel.setVisibility(View.VISIBLE);
        } else {
            holder.unreadLabel.setVisibility(View.INVISIBLE);
        }
        boolean IsGet = false;

//		// 设置message
        if (conversation.getMsgCount() != 0) {
            holder.message.setText(getMessage(conversation.getLastMessage()));
            // 设置时间
            // 这里可能需要进行时间格式化，不同的格式请自行设定
            holder.time.setText(DateUtils.timeStampToStr1(conversation
                    .getLastMessage().getMsgTime()));
        }

        String userName;
        if (mymewEmMessage.direct == EMMessage.Direct.RECEIVE) {
            userName = mymewEmMessage.getFrom();
        } else {
            userName = mymewEmMessage.getTo();
        }
        //获取后开始显示
        try {
            ImBrand MyBrand = GetImBrandInf(userName);
            if (MyBrand != null) {
                ImageLoaderUtil.Load2(MyBrand.getAvatar(),
                        holder.avatar, R.drawable.new_im);
                StrUtils.SetTxt(holder.name, MyBrand.getSeller_name());
            } else {
//双方聊天
                if (mymewEmMessage.direct == EMMessage.Direct.SEND) {// EMMessageDirectionSend
                    // holder.name.setText(ReciverName);
                    String ReciverName = conversation.getLastMessage()
                            .getStringAttribute("extReceiveNickname");
                    StrUtils.SetTxt(holder.name, ReciverName);//mymewEmMessage.getUserName()
                    ImageLoaderUtil.Load2(conversation.getLastMessage()
                                    .getStringAttribute("extReceiveHeadUrl"),
                            holder.avatar, R.drawable.new_im);
                    IsGet = true;
                }

                if (mymewEmMessage.direct == EMMessage.Direct.RECEIVE) {
                    try {
                        String ReciverName = conversation.getLastMessage()
                                .getStringAttribute("extSendNickname");
                        StrUtils.SetTxt(holder.name, ReciverName);//mymewEmMessage.getUserName()
                        ImageLoaderUtil.Load2(conversation.getLastMessage()
                                        .getStringAttribute("extSendHeadUrl"),
                                holder.avatar, R.drawable.new_im);
                        IsGet = true;
                    } catch (Exception e) {
                        IsGet = false;
                    }
                }

                if (!IsGet)
                    for (int i = conversation.getAllMessages().size() - 1; i > 0; i--) {
                        EMMessage mymewEmMessagesss = conversation.getMessage(i);
                        if (mymewEmMessagesss.direct == EMMessage.Direct.SEND) {
                            StrUtils.SetTxt(holder.name, mymewEmMessagesss
                                    .getStringAttribute("extReceiveNickname"));

//						StrUtils.SetTxt(holder.name,  mymewEmMessage.getUserName());
                            ImageLoaderUtil.Load2(mymewEmMessagesss
                                            .getStringAttribute("extReceiveHeadUrl"),
                                    holder.avatar, R.drawable.new_im);
                            IsGet = true;
                            break;
                        }

                    }

//双方聊天
            }
        } catch (Exception e) {
        }

//		try {
//
//			// if (SendName.equals(Spuit.Shop_Get(context).getSeller_name())) {
//			if (mymewEmMessage.direct == EMMessage.Direct.SEND) {// EMMessageDirectionSend
//				// holder.name.setText(ReciverName);
//				String ReciverName = conversation.getLastMessage()
//						.getStringAttribute("extReceiveNickname");
//				StrUtils.SetTxt(holder.name, ReciverName);//mymewEmMessage.getUserName()
//				ImageLoaderUtil.Load2(conversation.getLastMessage()
//						.getStringAttribute("extReceiveHeadUrl"),
//						holder.avatar, R.drawable.new_im);
//				IsGet = true;
//			}
//
//			if (mymewEmMessage.direct == EMMessage.Direct.RECEIVE) {
//				try {
//					String ReciverName = conversation.getLastMessage()
//							.getStringAttribute("extSendNickname");
//					StrUtils.SetTxt(holder.name,  ReciverName);//mymewEmMessage.getUserName()
//					ImageLoaderUtil.Load2(conversation.getLastMessage()
//							.getStringAttribute("extSendHeadUrl"),
//							holder.avatar, R.drawable.new_im);
//					IsGet = true;
//				} catch (Exception e) {
//					IsGet = false;
//				}
//			}
//
//			if (!IsGet)
//				for (int i = conversation.getAllMessages().size() - 1; i > 0; i--) {
//					EMMessage mymewEmMessagesss = conversation.getMessage(i);
//					if (mymewEmMessagesss.direct == EMMessage.Direct.SEND) {
//						StrUtils.SetTxt(holder.name, mymewEmMessagesss
//								.getStringAttribute("extReceiveNickname"));
//
////						StrUtils.SetTxt(holder.name,  mymewEmMessage.getUserName());
//						ImageLoaderUtil.Load2(mymewEmMessagesss
//								.getStringAttribute("extReceiveHeadUrl"),
//								holder.avatar, R.drawable.new_im);
//						IsGet = true;
//						break;
//					}
//
//				}
//
//			//
//			// StrUtils.SetTxt(holder.message,conversation.getLastMessage().getStringAttribute("extSendNickname"));
//		} catch (EaseMobException e) {
//			e.printStackTrace();
//		}
//
//		/**
//		 * 测试用例
//		 */
//		Log.v("count", conversation.getUnreadMsgCount() + "");

        return convertView;
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

    private class ViewHolder {
        /**
         * 和谁的聊天记录
         */
        TextView name;

        /**
         * 消息未读数
         */
        TextView unreadLabel;

        /**
         * 最后一条消息的内容
         */
        TextView message;

        /**
         * 最后一条消息的时间
         */
        TextView time;

        /**
         * 用户头像
         */
        ImageView avatar;

        /**
         * 整个list中每一行总布局
         */
        RelativeLayout list_item_layout;
    }

    private ImBrand GetImBrandInf(String Chat) {
        ImBrand myBrnd = null;
        if (!StrUtils.isEmpty(CacheUtil.Im_Brand_Get(context))) {
            List<ImBrand> brands = JSON.parseArray(CacheUtil.Im_Brand_Get(context), ImBrand.class);
            for (int i = 0; i < brands.size(); i++) {
                if (brands.get(i).getEmchat().equals(Chat)) {
                    myBrnd = brands.get(i);
                }
            }
        }
        return myBrnd;
    }
}
