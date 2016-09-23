package io.vtown.WeiTangApp.ui.title.center.myshow;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.vtown.WeiTangApp.R;
import io.vtown.WeiTangApp.bean.bcomment.BComment;
import io.vtown.WeiTangApp.comment.view.CopyTextView;
import io.vtown.WeiTangApp.comment.view.RecyclerCommentItemDecoration;
import io.vtown.WeiTangApp.comment.view.custom.CompleteGridView;
import io.vtown.WeiTangApp.ui.ATitleBase;

/**
 * Created by Yihuihua on 2016/9/23.
 */

public class ARecyclerMyShow extends ATitleBase {

    private RecyclerView recyclerview_my_show;

    @Override
    protected void InItBaseView() {
        setContentView(R.layout.activity_recycler_my_show);
        IView();
    }

    private void IView() {
        recyclerview_my_show = (RecyclerView) findViewById(R.id.recyclerview_my_show);
        recyclerview_my_show.setLayoutManager(new LinearLayoutManager(this));
        recyclerview_my_show.addItemDecoration(new RecyclerCommentItemDecoration(BaseContext,RecyclerCommentItemDecoration.VERTICAL_LIST,R.drawable.shape_show_divider_line));
        MyShowAdapter myShowAdapter = new MyShowAdapter(R.layout.item_recycler_my_show);
        recyclerview_my_show.setAdapter(myShowAdapter);
    }

    @Override
    protected void InitTile() {
        SetTitleTxt("我的SHOW");
    }

    @Override
    protected void DataResult(int Code, String Msg, BComment Data) {

    }

    @Override
    protected void DataError(String error, int LoadType) {

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

    class MyShowAdapter extends RecyclerView.Adapter<MyShowItem>{

        private int ResourseId;
        private LayoutInflater inflater;

        public MyShowAdapter(int ResourseId){
            super();
            this.ResourseId = ResourseId;
            this.inflater = LayoutInflater.from(BaseContext);

        }


        @Override
        public MyShowItem onCreateViewHolder(ViewGroup parent, int viewType) {

            MyShowItem myShowItem = new MyShowItem(inflater.inflate(ResourseId,parent,false));

            return myShowItem;
        }

        @Override
        public void onBindViewHolder(MyShowItem holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }



    class MyShowItem extends RecyclerView.ViewHolder{

        private CopyTextView my_show_content_title;
        private  CompleteGridView item_recycler_my_show_gridview;
        private  TextView my_show_create_time;

        public MyShowItem(View itemView) {
            super(itemView);
            my_show_content_title = (CopyTextView) itemView.findViewById(R.id.my_show_content_title);
            item_recycler_my_show_gridview = (CompleteGridView) itemView.findViewById(R.id.item_recycler_my_show_gridview);
            my_show_create_time = (TextView) itemView.findViewById(R.id.my_show_create_time);
        }
    }
}
