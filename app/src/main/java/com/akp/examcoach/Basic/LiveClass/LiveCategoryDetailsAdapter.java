package com.akp.examcoach.Basic.LiveClass;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.Basic.slidingmenu.AnimationHelper;
import com.akp.examcoach.R;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LiveCategoryDetailsAdapter extends RecyclerView.Adapter<LiveCategoryDetailsAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;

    public LiveCategoryDetailsAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public LiveCategoryDetailsAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_live_categorydetailslist, viewGroup, false);
        LiveCategoryDetailsAdapter.VH viewHolder = new LiveCategoryDetailsAdapter.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LiveCategoryDetailsAdapter.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.title_name_tv.setText(arrayList.get(i).get("BatchName"));
        vh.teacher_name_tv.setText(arrayList.get(i).get("CourseName"));
        vh.status_tv.setText("Live");

        Animation animBlink = AnimationUtils.loadAnimation(context,
                R.anim.blink);
        vh.blink.startAnimation(animBlink);

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,LiveClassBatchDetails.class);
                intent.putExtra("HomeCatId","1");
                intent.putExtra("BatchId",arrayList.get(i).get("BatchId"));
                intent.putExtra("BatchName",arrayList.get(i).get("BatchName"));
                intent.putExtra("a_type","normal");
                context.startActivity(intent);

//
//                Intent intent=new Intent(context,LiveClassDetails.class);
//                intent.putExtra("subject_id",arrayList.get(i).get("Subjectcode"));
//                intent.putExtra("subject_name",arrayList.get(i).get("SubjectName"));
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView title_name_tv,teacher_name_tv,status_tv;
        LinearLayout live_ll;
        ImageView blink;



        CircleImageView cat_img;
        public VH(@NonNull View itemView) {
            super(itemView);
            title_name_tv = itemView.findViewById(R.id.title_name_tv);
            teacher_name_tv = itemView.findViewById(R.id.teacher_name_tv);
            cat_img = itemView.findViewById(R.id.cat_img);
            live_ll = itemView.findViewById(R.id.live_ll);
            status_tv = itemView.findViewById(R.id.status_tv);
            blink = itemView.findViewById(R.id.blink);
        }
    }
}


