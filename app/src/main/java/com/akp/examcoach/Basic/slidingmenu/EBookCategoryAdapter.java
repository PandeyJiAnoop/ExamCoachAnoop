package com.akp.examcoach.Basic.slidingmenu;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.Basic.Bottomview.Api_Urls;
import com.akp.examcoach.Basic.LiveClass.LiveClassBatchDetails;
import com.akp.examcoach.R;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EBookCategoryAdapter extends RecyclerView.Adapter<EBookCategoryAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;
    ArrayList<HashMap<String, String>> arrayList1 = new ArrayList<>();



    public EBookCategoryAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public EBookCategoryAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_ebook_cat, viewGroup, false);
        EBookCategoryAdapter.VH viewHolder = new EBookCategoryAdapter.VH(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EBookCategoryAdapter.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.cat_btn.setText(arrayList.get(i).get("SubjectName"));

        vh.cat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, ELibraryMenu.class);
                intent.putExtra("SubjectCode",arrayList.get(i).get("Subjectcode"));
                intent.putExtra("a_type","adapter");
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
        RecyclerView wallet_histroy1;
        public VH(@NonNull View itemView) {
            super(itemView);
            cat_btn = itemView.findViewById(R.id.cat_btn);

            wallet_histroy1 = itemView.findViewById(R.id.wallet_histroy1);
        }
    }


}

