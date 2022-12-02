package cscb07.group4.androidproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import cscb07.group4.androidproject.databinding.FragmentTimelineBinding;
import cscb07.group4.androidproject.manager.AccountManager;
import cscb07.group4.androidproject.manager.Course;
import cscb07.group4.androidproject.manager.CourseManger;
import cscb07.group4.androidproject.manager.Session;
import cscb07.group4.androidproject.manager.StudentCourseManager;

public class TimelineFragment extends Fragment {

    static FragmentTimelineBinding binding;

    TreeMap<Integer, Set<Course>> timeline = new TreeMap<>();

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
        if(AccountManager.getInstance().isLoggedIn()) {
            List<String> courses = StudentCourseManager.getInstance().getWantedCourses();
            for (String courseCode : courses) {
                if (CourseManger.getInstance().getCourseByID(courseCode)!=null) {
                    Course currentCourse = CourseManger.getInstance().getCourseByID(courseCode);
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
                if(StudentCourseManager.getInstance().getTakenCourses().contains(prerequisite)){
                    continue;
                }

                Course current = CourseManger.getInstance().getCourseByID(prerequisite);
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

        if(session == Session.WINTER){
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

    private void generateTimeline() {
        timeline.put(0, new HashSet<>()); // Fall 2022
        for (String courseCode : StudentCourseManager.getInstance().getWantedCourses()) {
            Course course = CourseManger.getInstance().getCourseByID(courseCode);
            if (course != null && !course.getSessions().isEmpty()) {
                addTimelineCourse(course);
            }
        }
    }

    private int addTimelineCourse(Course course) {
        int maxSession = -1;
        if (course.getPrerequisites() != null) {
            for (String prereqCourseCode : course.getPrerequisites()) {
                Course prereqCourse = CourseManger.getInstance().getCourseByID(prereqCourseCode);
                maxSession = Math.max(maxSession, addTimelineCourse(prereqCourse));
            }
        }

        if (maxSession == -1) {
            maxSession = getFirstSession();
        } else {
            maxSession++;
        }
        // Increment session until we are in an offered session
        while (!course.getSessions().contains(Session.values()[maxSession % 3])) {
            maxSession++;
        }
        timeline.get(maxSession).add(course);
        return maxSession;
    }

    private int getFirstSession() {
        for (Map.Entry<Integer, Set<Course>> entry : timeline.entrySet()) {
            // Cannot have more than 6 courses per session
            if (entry.getValue().size() >= 6) {
                continue;
            }
            return entry.getKey();
        }
        // No free session found, add new session
        int nextSession = timeline.lastKey() + 1;
        timeline.put(timeline.lastKey() + 1, new HashSet<>());
        return nextSession;
    }

}
