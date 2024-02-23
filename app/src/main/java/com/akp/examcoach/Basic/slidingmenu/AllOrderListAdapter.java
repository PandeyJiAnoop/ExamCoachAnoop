package com.akp.examcoach.Basic.slidingmenu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AllOrderListAdapter extends RecyclerView.Adapter<AllOrderListAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    public AllOrderListAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public AllOrderListAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_all_order_details, viewGroup, false);
        AllOrderListAdapter.VH viewHolder = new AllOrderListAdapter.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllOrderListAdapter.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.title_tv.setText(arrayList.get(i).get("PackageName"));
        vh.oder_tv.setText("Order Id- @_"+arrayList.get(i).get("OrderId"));
        vh.des_tv.setText(arrayList.get(i).get("packageFor")+"("+arrayList.get(i).get("PackageId")+")");
        vh.des1_tv.setText("Package Amount:- ₹ "+arrayList.get(i).get("PackageAmount"));
        vh.des2_tv.setText("Valid Upto"+"("+arrayList.get(i).get("validity").trim()+")");
        vh.des3_tv.setText("Payment Mode: -"+arrayList.get(i).get("PaymentMode"));
        vh.des4_tv.setText("Payment Status:- "+arrayList.get(i).get("PaymentStatus"));

        if (arrayList.get(i).get("PaymentStatus").equalsIgnoreCase("FAILED")){
            vh.des4_tv.setTextColor(Color.RED);
        }
        else {
            vh.des4_tv.setTextColor(Color.GREEN);
        }



        if (arrayList.get(i).get("OrderStatus").equalsIgnoreCase("Pending")){
            vh.status_tv.setText(arrayList.get(i).get("OrderStatus"));
            vh.status_tv.setBackgroundResource(R.color.red);
        }
         else if (arrayList.get(i).get("OrderStatus").equalsIgnoreCase("Canceled")){
            vh.status_tv.setText(arrayList.get(i).get("OrderStatus"));
            vh.status_tv.setBackgroundResource(R.color.primaryTextColor);
        }
        else {
            vh.status_tv.setText(arrayList.get(i).get("OrderStatus"));
            vh.status_tv.setBackgroundResource(R.color.green);
        }

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MyOrder.class);
                intent.putExtra("package_id",arrayList.get(i).get("PackageId"));
                intent.putExtra("package_for",arrayList.get(i).get("packageFor"));
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView title_tv, des_tv,des1_tv,des2_tv,status_tv,des3_tv,oder_tv,des4_tv;
        public VH(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
            des_tv = itemView.findViewById(R.id.des_tv);
            des1_tv = itemView.findViewById(R.id.des1_tv);
            des2_tv = itemView.findViewById(R.id.des2_tv);
            status_tv = itemView.findViewById(R.id.status_tv);
            des3_tv = itemView.findViewById(R.id.des3_tv);
            oder_tv=itemView.findViewById(R.id.oder_tv);
            des4_tv = itemView.findViewById(R.id.des4_tv);

        }
    }
}

