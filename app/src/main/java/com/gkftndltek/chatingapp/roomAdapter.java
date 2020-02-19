package com.gkftndltek.chatingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.List;

public class roomAdapter extends RecyclerView.Adapter<roomAdapter.MyViewHolder> {

    private  List<roomData> mDataset;
    private  static Context con;
    private String uid;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView TextView_roomName,TextView_roomPeople,TextView_roomStory,TextView_roomTime;
        public ImageView ImageView_picture;
        public LinearLayout LinearLayout_roomLayout;

        public MyViewHolder(View v) {
            super(v);
            TextView_roomName = v.findViewById(R.id.TextView_roomName);
            TextView_roomPeople = v.findViewById(R.id.TextView_roomPeople);
            TextView_roomStory = v.findViewById(R.id.TextView_roomStory);
            TextView_roomTime = v.findViewById(R.id.TextView_roomTime);
            LinearLayout_roomLayout = v.findViewById(R.id.LinearLayout_roomLayout);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public roomAdapter(List<roomData> myDataset, Context context,String uid) {
        this.uid = uid;
        mDataset = myDataset;
        con = context;
        Fresco.initialize(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public roomAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
              .inflate(R.layout.activity_roomdata, parent, false);

        //화면 각각 목록의 레이아웃

        MyViewHolder vh = new MyViewHolder(v);
       return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final roomData data = mDataset.get(position);
        holder.LinearLayout_roomLayout.setClickable(true);
        holder.TextView_roomName.setText(data.getRoomName());
        holder.TextView_roomPeople.setText(Integer.toString(data.getNumber()));
        holder.TextView_roomStory.setText(data.getStory());
        holder.TextView_roomTime.setText(data.getTime());

        holder.LinearLayout_roomLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(con,chatActivity.class);
                it.putExtra("data",data);
                it.putExtra("uid",uid);
                con.startActivity(it);
                System.out.println("시발시발시발시발시발시발시발시발시발시발시발");
            }
        });
    }

    public void addRoom(roomData data){
        if(data != null) {
            mDataset.add(data);
            notifyItemInserted(mDataset.size() - 1); // 갱신용
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset == null ? 0  : mDataset.size();
    }
    public roomData getRoom(int pos){
        return mDataset != null ? mDataset.get(pos) : null;
    }
}