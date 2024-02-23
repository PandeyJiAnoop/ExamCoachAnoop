package com.akp.examcoach.Basic.EbooksDashboard;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.Basic.LiveClass.LiveClassBatchDetails;
import com.akp.examcoach.Basic.slidingmenu.AnimationHelper;
import com.akp.examcoach.Basic.slidingmenu.ELibraryMenu;
import com.akp.examcoach.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EbookNotesAdapter extends RecyclerView.Adapter<EbookNotesAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;

    public EbookNotesAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public EbookNotesAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_live_batchlistebook, viewGroup, false);
        EbookNotesAdapter.VH viewHolder = new EbookNotesAdapter.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EbookNotesAdapter.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.cat_btn.setText(arrayList.get(i).get("SubjectName"));
//        Animation animBlink = AnimationUtils.loadAnimation(context,
//                R.anim.blink);
//        vh.cat_name.startAnimation(animBlink);

//        if (arrayList.get(i).get("imageUrl").equalsIgnoreCase("")){
////            Toast.makeText(context,"Image not found!",Toast.LENGTH_LONG).show();
//        }
//        else {
//            Glide.with(context).load(arrayList.get(i).get("imageUrl")).error(R.drawable.logo).into(vh.cat_img);
//        }

        vh.cat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, EbookNotes.class);
                intent.putExtra("SubjectCode",arrayList.get(i).get("Subjectcode"));
                intent.putExtra("SubjectName",arrayList.get(i).get("SubjectName")+"("+arrayList.get(i).get("BatchName")+")");
                intent.putExtra("BatchId",arrayList.get(i).get("BatchId"));
                intent.putExtra("types","adapter");

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        AppCompatButton cat_btn;
        public VH(@NonNull View itemView) {
            super(itemView);
            cat_btn = itemView.findViewById(R.id.cat_btn);
        }
    }
}


