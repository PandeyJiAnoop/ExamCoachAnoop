package com.akp.examcoach.Basic.QuizExam;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.Basic.Bottomview.Das_Testseries;
import com.akp.examcoach.Basic.DasELibraryNew.Das_Live_AyogListELibrary;
import com.akp.examcoach.Basic.slidingmenu.AnimationHelper;
import com.akp.examcoach.R;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LiveSelectionAdapterExam extends RecyclerView.Adapter<LiveSelectionAdapterExam.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;

    public LiveSelectionAdapterExam(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public LiveSelectionAdapterExam.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_das_selection_exam, viewGroup, false);
        LiveSelectionAdapterExam.VH viewHolder = new LiveSelectionAdapterExam.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LiveSelectionAdapterExam.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.cat_name.setText(arrayList.get(i).get("examtypeTitle"));

        if (arrayList.get(i).get("imageUrl").equalsIgnoreCase("")){
//            Toast.makeText(context,"Image not found!",Toast.LENGTH_LONG).show();
        }
        else {
            Glide.with(context).load(arrayList.get(i).get("imageUrl")).error(R.drawable.logo).into(vh.cat_img);
        }

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Das_Testseries.class);
                intent.putExtra("type","das");
                context.startActivity(intent);
//                Toast.makeText(context,""+arrayList.get(i).get("HomeCatId"),Toast.LENGTH_LONG).show();
//                Intent intent=new Intent(context, Das_Live_AyogListELibrary.class);
//                intent.putExtra("examtypeId",arrayList.get(i).get("examtypeId"));
//                intent.putExtra("examtypeTitle",arrayList.get(i).get("examtypeTitle"));
//                intent.putExtra("HomeCatId","2");
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView cat_name;
        CircleImageView cat_img;

        public VH(@NonNull View itemView) {
            super(itemView);
            cat_name = itemView.findViewById(R.id.cat_name);
            cat_img = itemView.findViewById(R.id.cat_img);
        }
    }
}