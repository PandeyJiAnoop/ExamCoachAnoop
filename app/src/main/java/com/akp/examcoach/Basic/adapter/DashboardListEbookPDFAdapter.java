package com.akp.examcoach.Basic.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.Basic.EbooksDashboard.Das_LiveClassEbook;
import com.akp.examcoach.Basic.slidingmenu.AnimationHelper;
import com.akp.examcoach.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardListEbookPDFAdapter extends RecyclerView.Adapter<DashboardListEbookPDFAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    public DashboardListEbookPDFAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public DashboardListEbookPDFAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_das_ebookpdf, viewGroup, false);
        DashboardListEbookPDFAdapter.VH viewHolder = new DashboardListEbookPDFAdapter.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardListEbookPDFAdapter.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.provider_name_tv.setText(arrayList.get(i).get("AayogTitle"));

        if (arrayList.get(i).get("imageUrl").equalsIgnoreCase("")){
//            Toast.makeText(context,"Image not found!",Toast.LENGTH_LONG).show();
        }
        else {
            Picasso.with(context).load(arrayList.get(i).get("imageUrl")).error(R.drawable.logo).into(vh.provider_img);
        }

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,"Coming Soon!", Toast.LENGTH_LONG).show();
//              comment 05-11-2022 anoop
                Intent intent=new Intent(context, Das_LiveClassEbook.class);
                intent.putExtra("AayogId",arrayList.get(i).get("AayogId"));
                intent.putExtra("AayogTitle",arrayList.get(i).get("AayogTitle"));
                context.startActivity(intent);

//                Intent intent=new Intent(context, Das_Live_AyogListEbook.class);
//                intent.putExtra("examtypeId",arrayList.get(i).get("AayogId"));
//                intent.putExtra("examtypeTitle",arrayList.get(i).get("AayogTitle"));
//                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView provider_name_tv;
        CircleImageView provider_img;


        public VH(@NonNull View itemView) {
            super(itemView);
            provider_name_tv = itemView.findViewById(R.id.provider_name_tv);
            provider_img = itemView.findViewById(R.id.provider_img);

        }
    }
}

