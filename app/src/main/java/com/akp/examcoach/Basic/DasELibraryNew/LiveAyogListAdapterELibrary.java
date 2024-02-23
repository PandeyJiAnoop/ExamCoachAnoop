package com.akp.examcoach.Basic.DasELibraryNew;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.Basic.CourceListNew;
import com.akp.examcoach.Basic.slidingmenu.AnimationHelper;
import com.akp.examcoach.R;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LiveAyogListAdapterELibrary extends RecyclerView.Adapter<LiveAyogListAdapterELibrary.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;

    public LiveAyogListAdapterELibrary(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public LiveAyogListAdapterELibrary.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_live_ayoglistelibrary, viewGroup, false);
        LiveAyogListAdapterELibrary.VH viewHolder = new LiveAyogListAdapterELibrary.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LiveAyogListAdapterELibrary.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.cat_name.setText(arrayList.get(i).get("AayogTitle"));

        if (arrayList.get(i).get("imageUrl").equalsIgnoreCase("")){
//            Toast.makeText(context,"Image not found!",Toast.LENGTH_LONG).show();
        }
        else {
            Glide.with(context).load(arrayList.get(i).get("imageUrl")).error(R.drawable.logo).into(vh.cat_img);
        }


        vh.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CourceListNewELibrary.class);
                intent.putExtra("AayogId",arrayList.get(i).get("AayogId"));
                intent.putExtra("AayogTitle",arrayList.get(i).get("AayogTitle"));
                intent.putExtra("HomeCatId","3");
                context.startActivity(intent);
            }
        });
        vh.cat_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, CourceListNewELibrary.class);
                intent.putExtra("AayogId",arrayList.get(i).get("AayogId"));
                intent.putExtra("AayogTitle",arrayList.get(i).get("AayogTitle"));
                intent.putExtra("HomeCatId","3");
                context.startActivity(intent);
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
        LinearLayout ll;

        public VH(@NonNull View itemView) {
            super(itemView);
            cat_name = itemView.findViewById(R.id.cat_name);
            cat_img = itemView.findViewById(R.id.cat_img);
            ll = itemView.findViewById(R.id.ll);
        }
    }

}
