package com.akp.examcoach.Basic.slidingmenu;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WalletListAdapter extends RecyclerView.Adapter<WalletListAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    public WalletListAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public WalletListAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_wallet, viewGroup, false);
        WalletListAdapter.VH viewHolder = new WalletListAdapter.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WalletListAdapter.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);

        vh.tv2.setText(arrayList.get(i).get("ondate"));

        if (arrayList.get(i).get("debit").equalsIgnoreCase("0.00")){
            vh.tv3.setText("₹ "+arrayList.get(i).get("credit"));
            vh.tv1.setText("Credit Amount Successfully ("+arrayList.get(i).get("MemberId")+")");
            vh.tv3.setTextColor(Color.GREEN);

        }

        if (arrayList.get(i).get("credit").equalsIgnoreCase("0.00")){
            vh.tv3.setText("₹ "+arrayList.get(i).get("debit"));
            vh.tv1.setText("Debit Amount ("+arrayList.get(i).get("MemberId")+")");
            vh.tv3.setTextColor(Color.RED);

        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        TextView tv1, tv2,tv3;
        public VH(@NonNull View itemView) {
            super(itemView);
            tv1 = itemView.findViewById(R.id.tv1);
            tv2 = itemView.findViewById(R.id.tv2);
            tv3 = itemView.findViewById(R.id.tv3);
        }
    }
}

