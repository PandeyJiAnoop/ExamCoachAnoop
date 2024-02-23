package com.akp.examcoach.Basic.WorkWithUsAdapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.Basic.slidingmenu.AnimationHelper;
import com.akp.examcoach.Basic.slidingmenu.StudentWorkUpload;
import com.akp.examcoach.R;

import java.util.HashMap;
import java.util.List;

public class WorkWithUsQuestionListAdapter extends RecyclerView.Adapter<WorkWithUsQuestionListAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    public WorkWithUsQuestionListAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }
    @NonNull
    @Override
    public WorkWithUsQuestionListAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_wwusquestion, viewGroup, false);
        WorkWithUsQuestionListAdapter.VH viewHolder = new WorkWithUsQuestionListAdapter.VH(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull WorkWithUsQuestionListAdapter.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.subject_id.setText(arrayList.get(i).get("Title"));
        vh.username_tv.setText(arrayList.get(i).get("SubjectName"));
        vh.title_tv.setText(arrayList.get(i).get("Message"));
        vh.date_tv.setText(arrayList.get(i).get("EntryDate"));

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, StudentWorkUpload.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView subject_id, username_tv,title_tv,date_tv;
        public VH(@NonNull View itemView) {
            super(itemView);
            subject_id = itemView.findViewById(R.id.subject_id);
            username_tv = itemView.findViewById(R.id.username_tv);
            title_tv = itemView.findViewById(R.id.title_tv);

            date_tv = itemView.findViewById(R.id.date_tv);

        }
    }
}

