package com.akp.examcoach.Basic.EbooksDashboard;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.Basic.ViewPDF;
import com.akp.examcoach.Basic.slidingmenu.AnimationHelper;
import com.akp.examcoach.Basic.slidingmenu.PermissionUtils;
import com.akp.examcoach.R;

import java.util.HashMap;
import java.util.List;

public class EbookNotesAdapter1 extends RecyclerView.Adapter<EbookNotesAdapter1.VH> {
    Context context;
    List<HashMap<String, String>> arrayList;
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1;

    PermissionUtils permissionUtils;

    public EbookNotesAdapter1(Context context, List<HashMap<String, String>> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public EbookNotesAdapter1.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_ebook, viewGroup, false);
        EbookNotesAdapter1.VH viewHolder = new EbookNotesAdapter1.VH(view);

        permissionUtils = new PermissionUtils();

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EbookNotesAdapter1.VH vh, int i) {
        AnimationHelper.animatate(context, vh.itemView, R.anim.alfa_animation);
        vh.title_tv.setText(arrayList.get(i).get("Title") + "(" + arrayList.get(i).get("TopicName") + ")");
        vh.des_tv.setText(arrayList.get(i).get("Description"));


        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewPDF.class);
                intent.putExtra("pdfurl", arrayList.get(i).get("BookURL"));
                context.startActivity(intent);
            }
        });
        vh.download_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (permissionUtils.checkPermission(context, STORAGE_PERMISSION_REQUEST_CODE, v)) {
                    if (arrayList.get(i).get("BookURL").length() > 0) {
                        try {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(arrayList.get(i).get("BookURL"))));
                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                    }
                }


//                new DownloadTask(context, arrayList.get(i).get("BookURL"));
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VH extends RecyclerView.ViewHolder {
        TextView title_tv, des_tv;
        ImageView img;
        RelativeLayout download_rl;


        public VH(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
            des_tv = itemView.findViewById(R.id.des_tv);
            img = itemView.findViewById(R.id.img);
            download_rl = itemView.findViewById(R.id.download_rl);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Snackbar.make(urlTextInputLayout, "Permission Granted, Now you can write storage.", Snackbar.LENGTH_LONG).show();
                } else {
                    //Snackbar.make(urlTextInputLayout, "Permission Denied, You cannot access storage.", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }
}


