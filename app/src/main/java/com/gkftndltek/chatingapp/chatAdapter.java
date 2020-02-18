package com.gkftndltek.chatingapp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.MyViewHolder> {
    private  List<chatdata> mDataset;
    private String mynicknames;
    private Context con;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView LinearLayout_chatname,LinearLayout_chatmsg,TextView_time_start,TextView_time_end;
        public ImageView LinearLayout_profile;
        public LinearLayout LinearLayout_bubble;

        public MyViewHolder(View v) {
            super(v);
            LinearLayout_bubble = v.findViewById(R.id.LinearLayout_bubble);
            TextView_time_start = v.findViewById(R.id.TextView_time_start);
            TextView_time_end = v.findViewById(R.id.TextView_time_end);
            LinearLayout_chatname = v.findViewById(R.id.LinearLayout_chatname);
            LinearLayout_chatmsg = v.findViewById(R.id.LinearLayout_chatmsg);
            LinearLayout_profile = v.findViewById(R.id.LinearLayout_profile);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public chatAdapter(List<chatdata> myDataset, Context context,String mynickname) {
        mDataset = myDataset;
        con = context;
        Fresco.initialize(context);
        mynicknames = mynickname;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public chatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_chatdata, parent, false);

        //화면 각각 목록의 레이아웃

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        chatdata data = mDataset.get(position);
        if(data.getNickname().equals(mynicknames)) {

            holder.TextView_time_end.setText("");
            holder.TextView_time_start.setText(data.getTime());
            holder.LinearLayout_chatmsg.setText(data.getMessage());
            holder.LinearLayout_chatmsg.setBackground(ContextCompat.getDrawable(con,R.drawable.bubble_green));
            holder.LinearLayout_profile.setImageResource(0);
            holder.LinearLayout_chatname.setText(data.getNickname());

            holder.LinearLayout_chatname.setGravity(Gravity.RIGHT);
            holder.LinearLayout_bubble.setGravity(Gravity.RIGHT);
        }
        else {
            holder.TextView_time_start.setText("");
            holder.TextView_time_end.setText(data.getTime());
            holder.LinearLayout_chatname.setText(data.getNickname());
            holder.LinearLayout_chatmsg.setText(data.getMessage());
            holder.LinearLayout_profile.setImageResource(R.drawable.twitch);
            holder.LinearLayout_chatmsg.setBackground(ContextCompat.getDrawable(con,R.drawable.bubble_yellow));
            holder.LinearLayout_chatname.setGravity(Gravity.LEFT);
            holder.LinearLayout_bubble.setGravity(Gravity.LEFT);
        }
    }

    public void addChat(chatdata chat){
        if(chat != null) {
            mDataset.add(chat);
            notifyItemInserted(mDataset.size() - 1); // 갱신용
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset == null ? 0  : mDataset.size();
    }
    public chatdata getChat(int pos){
        return mDataset != null ? mDataset.get(pos) : null;
    }

}