package cscb07.group4.androidproject;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder>{

    public Context context;
    public ArrayList<Course> courseList;

    public AdminAdapter(ArrayList<Course> courseList){
        this.courseList = courseList;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
        AdminViewHolder holder = new AdminViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder holder, int position) {
        String pre = "";
        String season = "";
        int count1 = 0;
        int count2 = 0;

        holder.course_code.setText("Course Code: " + courseList.get(position).getCode());
        holder.course_name.setText("Course Name: " + courseList.get(position).getName());

        if(courseList.get(position).getPrerequisites()!=null) {
            for (String p : courseList.get(position).getPrerequisites()) {
                if(count1 == 0 && p!= null) {
                    pre = p;
                }
                else if(count1 != 0 && p != null){
                    pre = pre + " " + p;
                }
                count1++;
            }
        }


        if(courseList.get(position).getSessions() != null) {
            for (Session s : courseList.get(position).getSessions()) {
                if(count2 == 0 && s != null) {
                    season = s.name();
                }
                else if(count2 != 0 && s != null){
                    season = season + " " + s;
                }
                count2++;
            }
        }

        holder.course_pre.setText("Prerequisites: " + pre);
        holder.course_session.setText("Seasonal Offerings: " + season);
    }

    @Override
    public int getItemCount() {
        return (courseList != null ? courseList.size() : 0);
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder{
        TextView course_name;
        TextView course_code;
        TextView course_pre;
        TextView course_session;

        public AdminViewHolder(@NonNull View itemView){
            super(itemView);
            this.course_name = itemView.findViewById(R.id.nameText);
            this.course_code = itemView.findViewById(R.id.codeText);
            this.course_session = itemView.findViewById(R.id.sessionText);
            this.course_pre = itemView.findViewById(R.id.preText);
        }
    }
}
