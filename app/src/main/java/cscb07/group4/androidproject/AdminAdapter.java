package cscb07.group4.androidproject;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import cscb07.group4.androidproject.manager.Course;
import cscb07.group4.androidproject.manager.Session;
import cscb07.group4.androidproject.manager.CourseManger;
import cscb07.group4.androidproject.manager.StudentCourseManager;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.AdminViewHolder>{

    public Fragment fragment;
    public ArrayList<Course> courseList;

    public AdminAdapter(Fragment fragment, ArrayList<Course> courseList){
        this.fragment = fragment;
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
    public void onBindViewHolder(@NonNull AdminViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String pre = "";
        String season = "";
        int count1 = 0;
        int count2 = 0;
        CourseManger manager = CourseManger.getInstance();

        holder.course_code.setText("Course Code: " + courseList.get(position).getCode());
        holder.course_name.setText("Course Name: " + courseList.get(position).getName());

        Course course = courseList.get(position);

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

        if(position % 4 == 0){
            holder.itemView.setBackgroundColor(Color.parseColor("#BAE7AF"));
        }
        else if(position % 4 == 1){
            holder.itemView.setBackgroundColor(Color.parseColor("#DFD4E4"));
        }
        else if(position % 4 == 2){
            holder.itemView.setBackgroundColor(Color.parseColor("#AEE4FF"));
        }
        else if(position % 4 == 3){
            holder.itemView.setBackgroundColor(Color.parseColor("#FCFFB0"));
        }
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AdminDeletePopUpFragment(course, new Runnable() {
                    @Override
                    public void run() {
                        refresh(course, position);
                    }
                }).show(AdminAdapter.this.fragment.getChildFragmentManager(),"dialog_admin_delete_course");
            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddAdminCourseDialogFragment(course, (course) -> {
                    AdminAdapter.this.courseList.set(position, course);
                    AdminAdapter.this.notifyItemChanged(position);
                }).show(AdminAdapter.this.fragment.getChildFragmentManager(),"dialog_edit_admin_course");
            }
        });
    }
    public void refresh(Course course, int position){
        AdminAdapter.this.courseList.remove(course);
        AdminAdapter.this.notifyItemRemoved(position);
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
        AppCompatImageButton btn_delete;
        AppCompatImageButton editButton;

        public AdminViewHolder(@NonNull View itemView){
            super(itemView);
            this.course_name = itemView.findViewById(R.id.nameText);
            this.course_code = itemView.findViewById(R.id.codeText);
            this.course_session = itemView.findViewById(R.id.sessionText);
            this.course_pre = itemView.findViewById(R.id.preText);
            this.btn_delete = itemView.findViewById(R.id.btn_delete);
            this.editButton = itemView.findViewById(R.id.editButton);
        }
    }
}
