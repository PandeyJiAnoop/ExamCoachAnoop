package com.akp.examcoach.Basic.adapter;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.akp.examcoach.Basic.LiveClass.ExamPrivacyPolicy;
import com.akp.examcoach.Basic.QuizExam.StartQuizTest;
import com.akp.examcoach.Basic.QuizExam.ViewResult;
import com.akp.examcoach.Basic.planpackage.MenuPackageList;
import com.akp.examcoach.Basic.slidingmenu.AnimationHelper;
import com.akp.examcoach.Basic.slidingmenu.PrivacyPolicy;
import com.akp.examcoach.R;
import java.util.HashMap;
import java.util.List;

public class TestCatListAdapter extends RecyclerView.Adapter<TestCatListAdapter.VH> {
    Context context;
    List<HashMap<String,String>> arrayList;

    public TestCatListAdapter(Context context, List<HashMap<String,String>> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }

    @NonNull
    @Override
    public TestCatListAdapter.VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_test_cat_details, viewGroup, false);
        TestCatListAdapter.VH viewHolder = new TestCatListAdapter.VH(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull TestCatListAdapter.VH vh, int i) {
        AnimationHelper.animatate(context,vh.itemView, R.anim.alfa_animation);
        vh.title_tv.setText(arrayList.get(i).get("ExamType"));
        vh.exam_cat_tv.setText(arrayList.get(i).get("ExamName"));
        vh.exam_code_tv.setText(arrayList.get(i).get("ExamType")+ arrayList.get(i).get("ExamCode"));
        vh.quest_tv.setText("Total Questions:- "+arrayList.get(i).get("NoOfQuestions"));
        vh.mark_tv.setText("Total Marks:- "+arrayList.get(i).get("TotalMarks"));
        vh.starttime_tv.setText("Exam Time:- "+arrayList.get(i).get("ExamTime"));
        vh.startdate_tv.setText("Exam Date:- "+arrayList.get(i).get("ExamDate"));
        vh.duration_tv.setText("Durations:- "+arrayList.get(i).get("ExamDurationInMins")+" min.");
        vh.view_result.setText(arrayList.get(i).get("ResultStatus"));


        if (arrayList.get(i).get("TestStartStatus").equalsIgnoreCase("False")){
            if (arrayList.get(i).get("TestStatus").equalsIgnoreCase("Not Attempted")){
                vh.start_test_tv.setText("Start Test");
                vh.start_test_tv.setVisibility(View.VISIBLE);
                vh.attemped_tv.setVisibility(View.GONE);
            }
            else {
                vh.attemped_tv.setText("Attempted");
                vh.attemped_tv.setVisibility(View.VISIBLE);
                vh.start_test_tv.setVisibility(View.GONE);
                vh.view_result.setVisibility(View.VISIBLE);
            }
        }
        else if (arrayList.get(i).get("TestStartStatus").equalsIgnoreCase("true")){
            if (arrayList.get(i).get("TestStatus").equalsIgnoreCase("Not Attempted")){
                vh.start_test_tv.setText("Start Test");
                vh.start_test_tv.setVisibility(View.VISIBLE);
                vh.attemped_tv.setVisibility(View.GONE);
            }
            else {
                vh.attemped_tv.setText("Attempted");
                vh.attemped_tv.setVisibility(View.VISIBLE);
                vh.start_test_tv.setVisibility(View.GONE);
                vh.view_result.setVisibility(View.VISIBLE);
            }
        }



        vh.view_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList.get(i).get("ResultStatus").equalsIgnoreCase("Click Here for Result")){
                    Intent intent = new Intent(context, ViewResult.class);
                    intent.putExtra("ExamCode", arrayList.get(i).get("ExamCode"));
                    intent.putExtra("BranchCode", arrayList.get(i).get("id"));
                    context.startActivity(intent);
                }
            }
        });
        if (arrayList.get(i).get("PackageStatus").equalsIgnoreCase("Paid")){
            vh.paid_img.setVisibility(View.GONE);
        }
        if (arrayList.get(i).get("PackageStatus").equalsIgnoreCase("Free")){
            vh.paid_img.setVisibility(View.VISIBLE);
        }

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayList.get(i).get("PackageStatus").equalsIgnoreCase("Paid")){
                    vh.paid_img.setVisibility(View.GONE);
                    Toast.makeText(context,"You Do'nt have any Purchased Package \nTry Another one!",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(context, MenuPackageList.class);
                    context.startActivity(intent);
                }
                if (arrayList.get(i).get("PackageStatus").equalsIgnoreCase("Free")){
                    vh.paid_img.setVisibility(View.VISIBLE);
                    if (arrayList.get(i).get("TestStatus").equalsIgnoreCase("Not Attempted")){
                        if (arrayList.get(i).get("TestStartStatus").equalsIgnoreCase("true")){
                            Intent intent=new Intent(context, ExamPrivacyPolicy.class);
                            intent.putExtra("examcode",arrayList.get(i).get("ExamCode"));
                            context.startActivity(intent);
                        }
                    }
                    else {
                        Toast.makeText(context,"You have already attempted this test \nTry Another one!",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    vh.paid_img.setVisibility(View.GONE);
                    Toast.makeText(context,"You Do'nt have any Purchased Package \nTry Another one!",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(context, MenuPackageList.class);
                    context.startActivity(intent);
                }
            }
        });



      /*  {"msg":"Success","status":1,"TestList":[{"ExamCode":"EX-0000006","ExamType":"CTET EXAM","ExamName":"HOME EXAM","NoOfQuestions":"5",
                "ExamDurationInMins":"5","TotalMarks":"14.00","TestStatus":" Not Attempted","ExamDate":"","ExamTime":"","PackageId":"",
                "PackageStatus":"Free","PackagePurchaseStatus":"Pending","ResultStatus":"Click Here for Result","TestStartStatus":"False"},
            {"ExamCode":"EX-0000009","ExamType":"CTET EXAM","ExamName":"Test Maths","NoOfQuestions":"24","ExamDurationInMins":"50",
                    "TotalMarks":"52.00","TestStatus":"Attempted","ExamDate":"12/1/2022 12:00:00 AM","ExamTime":"13:04:00","PackageId":"",
                    "PackageStatus":"Free","PackagePurchaseStatus":"Pending","ResultStatus":"Click Here for Result","TestStartStatus":"False"},
            {"ExamCode":"EX-0000010","ExamType":"CTET EXAM","ExamName":"Test Maths","NoOfQuestions":"2","ExamDurationInMins":"30","TotalMarks":"11.00",
                    "TestStatus":" Not Attempted","ExamDate":"12/1/2022 12:00:00 AM","ExamTime":"12:54:00","PackageId":"","PackageStatus":
                "Free","PackagePurchaseStatus":"Pending","ResultStatus":"Please Wait For Result...","TestStartStatus":"true"},{"ExamCode":"EX-0000011",
                "ExamType":"CTET EXAM","ExamName":"Test Akp","NoOfQuestions":"5","ExamDurationInMins":"360","TotalMarks":"14.00",
                "TestStatus":" Not Attempted","ExamDate":"12/2/2022 12:00:00 AM","ExamTime":"13:50:00","PackageId":"",
                "PackageStatus":"Free","PackagePurchaseStatus":"Pending","ResultStatus":"Please Wait For Result...",
                "TestStartStatus":"true"},{"ExamCode":"EX-0000007","ExamType":"CTET EXAM","ExamName":"my Exam",
                "NoOfQuestions":"1","ExamDurationInMins":"1","TotalMarks":"1.00","TestStatus":" Not Attempted",
                "ExamDate":"12/1/2022 12:00:00 AM","ExamTime":"11:00:00","PackageId":"1012","PackageStatus":"Paid",
                "PackagePurchaseStatus":"Pending","ResultStatus":"Click Here for Result","TestStartStatus":"False"},
            {"ExamCode":"EX-0000008","ExamType":"CTET EXAM","ExamName":"my Exam","NoOfQuestions":"1","ExamDurationInMins":"1",
                    "TotalMarks":"10.00","TestStatus":" Not Attempted","ExamDate":"12/2/2022 12:00:00 AM","ExamTime":"23:08:00",
                "PackageId":"1012","PackageStatus":"Paid","PackagePurchaseStatus":"Pending","ResultStatus":"Please Wait For Result...","TestStartStatus":"true"}]}*/













    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class VH extends RecyclerView.ViewHolder {
        ImageView paid_img;
        TextView title_tv,exam_cat_tv,exam_code_tv,quest_tv,mark_tv,duration_tv,attemped_tv,start_test_tv,view_result,starttime_tv,startdate_tv;
        public VH(@NonNull View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
            exam_cat_tv = itemView.findViewById(R.id.exam_cat_tv);
            exam_code_tv = itemView.findViewById(R.id.exam_code_tv);
            quest_tv = itemView.findViewById(R.id.quest_tv);
            mark_tv = itemView.findViewById(R.id.mark_tv);
            duration_tv = itemView.findViewById(R.id.duration_tv);
            attemped_tv = itemView.findViewById(R.id.attemped_tv);
            start_test_tv = itemView.findViewById(R.id.start_test_tv);

            view_result=itemView.findViewById(R.id.view_result);
            starttime_tv=itemView.findViewById(R.id.starttime_tv);
            startdate_tv=itemView.findViewById(R.id.startdate_tv);
            paid_img=itemView.findViewById(R.id.paid_img);
        }
    }


}