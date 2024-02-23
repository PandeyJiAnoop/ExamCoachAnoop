package com.akp.examcoach.Basic.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.akp.examcoach.Basic.slidingmenu.AnimationHelper;
import com.akp.examcoach.R;
import java.util.HashMap;
import java.util.List;

public class MyOrderListAdapter extends RecyclerView.Adapter<MyOrderListAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    public MyOrderListAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public MyOrderListAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_running_order_details, viewGroup, false);
        MyOrderListAdapter.VH viewHolder = new MyOrderListAdapter.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderListAdapter.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.title_tv.setText(arrayList.get(i).get("PackageName"));
        vh.des_tv.setText(arrayList.get(i).get("packageType")+"(Including total "+arrayList.get(i).get("NoOfTest")+" test series)");
        vh.des1_tv.setText("₹ "+arrayList.get(i).get("Price"));
        vh.des2_tv.setText("(Valid upto- "+arrayList.get(i).get("ValidTill").trim()+")");
        vh.des3_tv.setText("Course Name: -"+arrayList.get(i).get("CourseName"));
        if (arrayList.get(i).get("OrderStatus").equalsIgnoreCase("Pending")){
            vh.status_tv.setText(arrayList.get(i).get("OrderStatus"));
            vh.status_tv.setBackgroundResource(R.color.red);
        }
        else {
            vh.status_tv.setText(arrayList.get(i).get("OrderStatus"));
        }

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList.get(i).get("OrderStatus").equalsIgnoreCase("Pending")){
                    Toast.makeText(context,"Please wait for Admin approval\nif you have any issue contact to Support Team",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView title_tv, des_tv,des1_tv,des2_tv,status_tv,des3_tv;
        public VH(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
            des_tv = itemView.findViewById(R.id.des_tv);
            des1_tv = itemView.findViewById(R.id.des1_tv);
            des2_tv = itemView.findViewById(R.id.des2_tv);
            status_tv = itemView.findViewById(R.id.status_tv);
            des3_tv = itemView.findViewById(R.id.des3_tv);
        }
    }
}

