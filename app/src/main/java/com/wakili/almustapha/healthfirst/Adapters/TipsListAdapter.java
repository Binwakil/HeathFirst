package com.wakili.almustapha.healthfirst.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wakili.almustapha.healthfirst.R;
import com.wakili.almustapha.healthfirst.Models.TipsListDataItem;

import java.util.Collections;
import java.util.List;


public class TipsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private LayoutInflater inflater;
    private Activity activity;
    List<TipsListDataItem> data= Collections.emptyList();
    int currentPos=0;




    // create constructor to innitilize context and data sent from activity
    public TipsListAdapter(Context context, Activity activity, List<TipsListDataItem> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.activity = activity;
        this.data=data;

    }



    public static class MyHolder extends RecyclerView.ViewHolder {


        TextView tiptitle, tipdetail, tipauthor;
        ImageView image;


        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);

            tiptitle = (TextView) itemView.findViewById(R.id.tip_title);
            tipdetail = (TextView) itemView.findViewById(R.id.tip_detail);
            tipauthor = (TextView) itemView.findViewById(R.id.tip_author);
            image = (ImageView) itemView.findViewById(R.id.profile_image);

        }

    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.tips_layout, parent,false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder= (MyHolder) holder;
        final TipsListDataItem current=data.get(position);
        myHolder.tiptitle.setText(current.tiptitle);
        myHolder.tipdetail.setText(current.tipdetail);
        myHolder.tipauthor.setText(current.username);
        Glide.with(context).load(String.valueOf(current.image)).placeholder(R.drawable.dp).error(R.drawable.dp).into(myHolder.image);

    }



    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }



}
