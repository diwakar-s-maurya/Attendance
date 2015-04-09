package com.maurya.diwakar.attendance;

/**
 * Created by diwakar on 4/8/15.
 */

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.FeedListRowHolder> {

    private List<FeedItem> feedItemList;
    private Context mContext;
    private int orgColor;

    public MyRecyclerAdapter(Context context, List<FeedItem> feedItemList) {
        this.feedItemList = feedItemList;
        this.mContext = context;
    }

    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view, null);

        //orgColor = v.getSolidColor();
        v.setBackgroundColor(Color.RED);
        FeedListRowHolder mh = new FeedListRowHolder(v);
        return mh;
    }



    @Override
    public void onBindViewHolder(FeedListRowHolder feedListRowHolder, int i) {
        FeedItem feedItem = feedItemList.get(i);

        feedListRowHolder.title.setText(feedItem.myRollString);
    }

    @Override
    public int getItemCount() {
        return (null != feedItemList ? feedItemList.size() : 0);
    }

    public class FeedListRowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView title;
        int orgColor;
        //    protected TextView status;

        public FeedListRowHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.rcv_rollno);
            //this.status = (TextView) view.findViewById(R.id.status);
            orgColor = view.getSolidColor();
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.rcv_rollno) {
                int pos = getAdapterPosition();
                if(feedItemList.get(pos).myAttRecord)
                {
                    feedItemList.get(pos).myAttRecord = false;
                    view.setBackgroundColor(Color.RED);
                }else
                {
                    feedItemList.get(pos).myAttRecord = true;
                    view.setBackgroundColor(orgColor);
                }
            }
        }
    }
}