package com.akp.examcoach.Basic.slidingmenu;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.R;

import java.util.HashMap;
import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    public FAQAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public FAQAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_privacypolicy, viewGroup, false);
        FAQAdapter.VH viewHolder = new FAQAdapter.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FAQAdapter.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.title_tv.setText(arrayList.get(i).get("Heading"));
        vh.content_tv.setText(arrayList.get(i).get("Content"));
//        vh.time_tv1.setText(arrayList.get(i).get("Date"));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView title_tv,content_tv;

        public VH(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
            content_tv= itemView.findViewById(R.id.content_tv);


        }
    }


}


