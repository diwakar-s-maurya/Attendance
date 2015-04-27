package com.maurya.diwakar.attendance;

/**
 * Created by diwakar on 4/8/15.
 */

import android.content.Context;
import android.content.Entity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.FeedListRowHolder> {

    List<FeedItem> feedItemList = Collections.emptyList();
    private int orgColor;
    private LayoutInflater inflater;
    TextView briefInfo;
    int presentStudents = 0;
    int totalStudents = 0;

    public MyRecyclerAdapter(Context context, List<FeedItem> feedItemList, TextView briefInfo) {
        inflater = LayoutInflater.from(context);
        this.feedItemList = feedItemList;

        totalStudents = feedItemList.size();
        for(int i = 0; i<feedItemList.size(); ++i)
            if(feedItemList.get(i).myAttRecord)
                ++presentStudents;
        this.briefInfo = briefInfo;
        briefInfo.setText(presentStudents + "/" + totalStudents);
    }

    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_view, parent, false);//view is the root view of all rows items
        FeedListRowHolder mh = new FeedListRowHolder(view);
        return mh;
    }



    @Override
    public void onBindViewHolder(FeedListRowHolder feedListRowHolder, final int position) {//prepare the view by adding the data
        FeedItem feedItem = feedItemList.get(position);

        feedListRowHolder.rollNo.setText(feedItem.myRollString);
        this.orgColor = feedListRowHolder.recyclerViewRow.getSolidColor();
        if( ! feedItemList.get(position).myAttRecord)
            feedListRowHolder.recyclerViewRow.setBackgroundColor(Color.RED);
    }

    @Override
    public int getItemCount(){
        return (feedItemList != null ? feedItemList.size() : 0);

    }



    public class FeedListRowHolder extends RecyclerView.ViewHolder {
        TextView rollNo;
        LinearLayout recyclerViewRow;
        //    protected TextView status;

        public FeedListRowHolder(final View itemView) {
            super(itemView);
            rollNo = (TextView) itemView.findViewById(R.id.rcv_rollno);
            recyclerViewRow = (LinearLayout)itemView.findViewById(R.id.recycler_view_row);

            recyclerViewRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FeedItem item = feedItemList.get(getPosition());
                    if(item.myAttRecord){
                        item.myAttRecord = false;
                        recyclerViewRow.setBackgroundColor(Color.RED);
                        rollNo.setTextColor(Color.WHITE);
                        --presentStudents;
                        briefInfo.setText(presentStudents + "/" + totalStudents);
                    }else{
                        item.myAttRecord = true;
                        recyclerViewRow.setBackgroundColor(orgColor);
                        rollNo.setTextColor(Color.BLACK);
                        ++presentStudents;
                        briefInfo.setText(presentStudents + "/" + totalStudents);
                    }
                }
            });
        }
    }
}