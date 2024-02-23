package com.akp.examcoach.Basic.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.Basic.slidingmenu.AnimationHelper;
import com.akp.examcoach.R;

import java.util.HashMap;
import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    public NotificationListAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public NotificationListAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_notification, viewGroup, false);
        NotificationListAdapter.VH viewHolder = new NotificationListAdapter.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationListAdapter.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.msg_tv.setText(arrayList.get(i).get("Title"));
        vh.des_tv.setText(arrayList.get(i).get("Description"));
        vh.time_tv.setText(arrayList.get(i).get("Ondate"));
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView msg_tv, des_tv,time_tv;
        public VH(@NonNull View itemView) {
            super(itemView);
            msg_tv = itemView.findViewById(R.id.msg_tv);
            des_tv = itemView.findViewById(R.id.des_tv);
            time_tv = itemView.findViewById(R.id.time_tv);
        }
    }
}

