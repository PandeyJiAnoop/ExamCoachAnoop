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

public class StudentWorkVideoAdapter extends RecyclerView.Adapter<StudentWorkVideoAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    public StudentWorkVideoAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public StudentWorkVideoAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_studentworkvideo, viewGroup, false);
        StudentWorkVideoAdapter.VH viewHolder = new StudentWorkVideoAdapter.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StudentWorkVideoAdapter.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.title_tv.setText(arrayList.get(i).get("Title")+"\n"+arrayList.get(i).get("Message"));
        vh.username_tv.setText("Customer Id:- "+arrayList.get(i).get("studentId"));
        vh.date_tv.setText(arrayList.get(i).get("EntryDate"));
        vh.subject_id.setText("Status:- "+arrayList.get(i).get("adminStatus"));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView title_tv, date_tv,username_tv,subject_id;
        public VH(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
            date_tv = itemView.findViewById(R.id.date_tv);
            username_tv = itemView.findViewById(R.id.username_tv);
            subject_id= itemView.findViewById(R.id.subject_id);
        }
    }
}

