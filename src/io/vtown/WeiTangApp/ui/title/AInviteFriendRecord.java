package io.vtown.WeiTangApp.ui.title;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.bean.bcomment.BUser;
import io.vtown.WeiTangApp.bean.bcomment.new_three.invite_friends.BCInviteFriends;
import io.vtown.WeiTangApp.bean.bcomment.new_three.invite_friends.BLInviteFriends;
import io.vtown.WeiTangApp.comment.contant.CacheUtil;
import io.vtown.WeiTangApp.comment.contant.Constants;
import io.vtown.WeiTangApp.comment.contant.PromptManager;
import io.vtown.WeiTangApp.comment.contant.Spuit;
import io.vtown.WeiTangApp.comment.util.StrUtils;
import io.vtown.WeiTangApp.comment.util.image.ImageLoaderUtil;
import io.vtown.WeiTangApp.comment.view.CircleImageView;
import io.vtown.WeiTangApp.comment.view.custom.CompleteListView;
import io.vtown.WeiTangApp.comment.view.custom.RefreshLayout;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by Yihuihua on 2016/10/12.
 */

public class AInviteFriendRecord extends ATitleBase implements RefreshLayout.OnLoadListener {

    private RefreshLayout invite_friends_refrash;
    private ListView invite_friends_record_list;
    private View invite_friends_nodata_lay;
    private BUser bUser;
    private int page = 1;
    private List<BCInviteFriends> datas = new ArrayList<BCInviteFriends>();
    private InviteFriendAdapter mAdapter;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_invite_friends_record);
        SetTitleHttpDataLisenter(this);
        bUser = Spuit.User_Get(BaseContext);
        IView();
        ICache();
        IData(page, LOAD_INITIALIZE);
    }


    private void IView() {
        invite_friends_refrash = (RefreshLayout) findViewById(R.id.invite_friends_refrash);
        invite_friends_refrash.setOnLoadListener(this);
        invite_friends_refrash.setColorSchemeResources(R.color.app_fen, R.color.app_fen1, R.color.app_fen2, R.color.app_fen3);
        invite_friends_refrash.setCanLoadMore(false);
        invite_friends_record_list = (ListView) findViewById(R.id.invite_friends_record_list);
        invite_friends_nodata_lay = findViewById(R.id.invite_friends_nodata_lay);
        mAdapter = new InviteFriendAdapter(R.layout.item_invite_friends_outside);
        invite_friends_record_list.setAdapter(mAdapter);
    }

    private void ICache() {
        String invite_friends = CacheUtil.My_Invite_Friends_Get(BaseContext);
        if (StrUtils.isEmpty(invite_friends)) {
            PromptManager.showtextLoading(BaseContext, getResources()
                    .getString(R.string.loading));
        } else {
            try {
                datas = JSON.parseArray(invite_friends, BCInviteFriends.class);
            } catch (Exception e) {
                return;
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private void IData(int page, int loadtype) {
        HashMap<String, String> map = new HashMap<>();
        map.put("member_id", bUser.getMember_id());
        map.put("seller_id", bUser.getSeller_id());
        map.put("page", page + "");
        map.put("pagesize", Constants.PageSize + "");
        FBGetHttpData(map, Constants.Invite_Friends, Request.Method.GET, 0, loadtype);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt(getString(R.string.invite_friends));
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

        switch (Data.getHttpLoadType()) {
            case LOAD_INITIALIZE:
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    invite_friends_refrash.setVisibility(View.GONE);
                    invite_friends_nodata_lay.setVisibility(View.VISIBLE);
                    invite_friends_nodata_lay.setClickable(false);
                    ShowErrorCanLoad(getString(R.string.null_invite_friend));
                    datas = new ArrayList<BCInviteFriends>();
                    mAdapter.notifyDataSetChanged();
                    return;
                }
                datas = JSON.parseArray(Data.getHttpResultStr(), BCInviteFriends.class);
                invite_friends_refrash.setVisibility(View.VISIBLE);
                invite_friends_nodata_lay.setVisibility(View.GONE);
                invite_friends_refrash.setRefreshing(false);
                mAdapter.notifyDataSetChanged();
                if (datas.size() == Constants.PageSize) {
                    invite_friends_refrash.setCanLoadMore(true);
                }

                if (datas.size() < Constants.PageSize) {
                    invite_friends_refrash.setCanLoadMore(false);
                }

                break;

            case LOAD_REFRESHING:
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    PromptManager.ShowCustomToast(BaseContext, getResources().getString(R.string.no_fresh_invite_friend));
                    return;
                }
                datas = JSON.parseArray(Data.getHttpResultStr(), BCInviteFriends.class);
                //invite_friends_refrash.setVisibility(View.VISIBLE);
                // invite_friends_nodata_lay.setVisibility(View.GONE);
                invite_friends_refrash.setRefreshing(false);
                mAdapter.notifyDataSetChanged();
                if (datas.size() == Constants.PageSize) {
                    invite_friends_refrash.setCanLoadMore(true);
                }

                if (datas.size() < Constants.PageSize) {
                    invite_friends_refrash.setCanLoadMore(false);
                }
                break;

            case LOAD_LOADMOREING:
                if (StrUtils.isEmpty(Data.getHttpResultStr())) {
                    PromptManager.ShowCustomToast(BaseContext, getResources().getString(R.string.no_nore_invite_friend));
                    return;
                }
                datas = JSON.parseArray(Data.getHttpResultStr(), BCInviteFriends.class);
                //invite_friends_refrash.setVisibility(View.VISIBLE);
                // invite_friends_nodata_lay.setVisibility(View.GONE);
                invite_friends_refrash.setLoading(false);
                mAdapter.notifyDataSetChanged();
                if (datas.size() == Constants.PageSize) {
                    invite_friends_refrash.setCanLoadMore(true);
                }

                if (datas.size() < Constants.PageSize) {
                    invite_friends_refrash.setCanLoadMore(false);
                }
                break;
        }

    }

    @Override
    protected void DataError(String error, int LoadType) {
        switch (LoadType) {
            case LOAD_INITIALIZE:
                invite_friends_refrash.setVisibility(View.GONE);
                invite_friends_nodata_lay.setVisibility(View.VISIBLE);
                invite_friends_nodata_lay.setClickable(true);
                ShowErrorCanLoad(getString(R.string.error_null_noda));
                invite_friends_refrash.setRefreshing(false);
                break;
            case LOAD_REFRESHING:
                invite_friends_refrash.setRefreshing(false);
                break;
            case LOAD_LOADMOREING:
                invite_friends_refrash.setLoading(false);
                break;
        }
    }

    @Override
    protected void NetConnect() {

    }

    @Override
    protected void NetDisConnect() {

    }

    @Override
    protected void SetNetView() {

    }

    @Override
    protected void MyClick(View V) {

    }

    @Override
    protected void InItBundle(Bundle bundle) {

    }

    @Override
    protected void SaveBundle(Bundle bundle) {

    }

    @Override
    public void OnLoadMore() {
        page++;
        IData(page, LOAD_REFRESHING);
    }

    @Override
    public void OnFrash() {
        page = 1;
        IData(page, LOAD_REFRESHING);
    }

    class InviteFriendAdapter extends BaseAdapter {

        private int ResourseId;

        private LayoutInflater inflater;

        public InviteFriendAdapter(int ResourseId) {
            super();
            this.ResourseId = ResourseId;
            this.inflater = LayoutInflater.from(BaseContext);
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

        public void FreshAllData(List<BCInviteFriends> more_datas) {
            datas.addAll(more_datas);
            this.notifyDataSetChanged();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            InviteFriendHolder holder = null;
            if (null == convertView) {
                holder = new InviteFriendHolder();
                convertView = inflater.inflate(ResourseId, null);
                holder.tv_invite_date = (TextView) convertView.findViewById(R.id.tv_invite_date);
                holder.invite_friends_record_inside = (CompleteListView) convertView.findViewById(R.id.invite_friends_record_inside);
                convertView.setTag(holder);
            } else {
                holder = (InviteFriendHolder) convertView.getTag();
            }
            StrUtils.SetTxt(holder.tv_invite_date, datas.get(position).getDate());
            FriendsAdapter friendsAdapter = new FriendsAdapter(R.layout.item_invite_friends_detail, datas.get(position).getList());
            holder.invite_friends_record_inside.setAdapter(friendsAdapter);
            return convertView;
        }
    }


    class FriendsAdapter extends BaseAdapter {

        private int ResourseId;
        private LayoutInflater inflater;
        private List<BLInviteFriends> friends_datas = new ArrayList<BLInviteFriends>();

        public FriendsAdapter(int ResourseId, List<BLInviteFriends> friends_datas) {
            super();
            this.ResourseId = ResourseId;
            this.friends_datas = friends_datas;
            this.inflater = LayoutInflater.from(BaseContext);
        }

        @Override
        public int getCount() {
            return friends_datas.size();
        }

        @Override
        public Object getItem(int position) {
            return friends_datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FriendsHolder holder = null;
            if (convertView == null) {
                holder = new FriendsHolder();
                convertView = inflater.inflate(ResourseId, null);
                holder.iv_friend_icon = (CircleImageView) convertView.findViewById(R.id.iv_friend_icon);
                holder.iv_friend_lv = (ImageView) convertView.findViewById(R.id.iv_friend_lv);
                holder.tv_friend_name = (TextView) convertView.findViewById(R.id.tv_friend_name);
                holder.tv_friend_shop_id = (TextView) convertView.findViewById(R.id.tv_friend_shop_id);
                holder.tv_lv = (TextView) convertView.findViewById(R.id.tv_lv);
                holder.tv_lv_top = (TextView) convertView.findViewById(R.id.tv_lv_top);
                holder.list_line = convertView.findViewById(R.id.list_line);
                convertView.setTag(holder);
            } else {
                holder = (FriendsHolder) convertView.getTag();
            }
            BLInviteFriends friend = friends_datas.get(position);
            ImageLoaderUtil.Load2(friend.getAvatar(), holder.iv_friend_icon, R.drawable.error_iv2);
            if ("0".equals(friend.getIsstar())) {
                holder.iv_friend_lv.setImageDrawable(BaseContext.getResources().getDrawable(R.drawable.ic_putongdianpuxiaotubiao_nor));
            } else {
                holder.iv_friend_lv.setImageDrawable(BaseContext.getResources().getDrawable(R.drawable.ic_mingxingdianpuxiaotubiao_nor));
            }
            StrUtils.SetTxt(holder.tv_friend_name, friend.getSeller_name());
            String shop_id = BaseContext.getResources().getString(R.string.invite_friend_shop_id);
            StrUtils.SetTxt(holder.tv_friend_shop_id, String.format(shop_id, friend.getSeller_no()));
            int level = Integer.parseInt(friend.getMember_level());
            switch (level) {
                case 0:
                    holder.tv_lv.setText("Lv1");
                    holder.tv_lv.setBackgroundColor(BaseContext.getResources().getColor(R.color.lv1));
                    holder.tv_lv_top.setBackgroundColor(BaseContext.getResources().getColor(R.color.lv1_top));
                    break;
                case 1:
                    holder.tv_lv.setText("Lv2");
                    holder.tv_lv.setBackgroundColor(BaseContext.getResources().getColor(R.color.lv2));
                    holder.tv_lv_top.setBackgroundColor(BaseContext.getResources().getColor(R.color.lv2_top));
                    break;
                case 2:
                    holder.tv_lv.setText("Lv3");
                    holder.tv_lv.setBackgroundColor(BaseContext.getResources().getColor(R.color.lv3));
                    holder.tv_lv_top.setBackgroundColor(BaseContext.getResources().getColor(R.color.lv3_top));
                    break;
            }
            if (friends_datas.size() - 1 == position) {
                holder.list_line.setVisibility(View.GONE);
            }else{
                holder.list_line.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }

    class InviteFriendHolder {
        TextView tv_invite_date;
        CompleteListView invite_friends_record_inside;
    }

    class FriendsHolder {
        CircleImageView iv_friend_icon;
        ImageView iv_friend_lv;
        TextView tv_friend_name, tv_friend_shop_id, tv_lv_top, tv_lv;
        View list_line;

    }
}
