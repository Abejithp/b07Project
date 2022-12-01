package cscb07.group4.androidproject;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cscb07.group4.androidproject.databinding.FragmentTimelineBinding;

public class TimelineFragment extends Fragment {

    static FragmentTimelineBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTimelineBinding.inflate(inflater, container, false);


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        StudentCourseManager.getInstance().refreshCourses(new Runnable() {
            @Override
            public void run() {
                update();
            }
        });

        return binding.getRoot();
    }
    public void update(){
        if(AccountManager.getInstance().isLoggedIn() && StudentCourseManager.courses!=null){
            List<String> courses = StudentCourseManager.getInstance().getWantedCourses();
            for(String courseCode : courses){
                if(CourseManger.getInstance().contains(courseCode)){
                    Course currentCourse = CourseManger.getInstance().getCourseByCode(courseCode);
                    TimelineFragment.add(currentCourse);
                }
            }
        }
    }

    public static void add(Course course){

        if(course!=null&&course.getPrerequisites()==null){
            TextView textView = new TextView(binding.Sessions.getContext());
            textView.setText(course.getName());
            TimelineFragment.addSession(course,textView);
        }

        if(course!=null&&course.getPrerequisites() != null){
            for(String prerequisite: course.getPrerequisites()){
                if(StudentCourseManager.getInstance().checkPrerequisite(prerequisite)){
                    continue;
                }

                Course current = CourseManger.getInstance().getCourseByCode(prerequisite);
                TimelineFragment.add(current);

            }

            TextView textView = new TextView(binding.Sessions.getContext());
            textView.setText(course.getName());
            TimelineFragment.addSession(course,textView);

            return;
        }

    }

    public static void addSession(Course course, TextView textView){
        Session session = course.getSessions().get(0);
        if(session.compareTo(Session.WINTER)==0){
            binding.sessionWinter2023.addView(textView);
        }
        if(session.compareTo(Session.FALL)==0){
            binding.sessionFall2022.addView(textView);
        }
        if(session.compareTo(Session.SUMMER)==0){
            binding.sessionSummer2023.addView(textView);

        }
        return;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
