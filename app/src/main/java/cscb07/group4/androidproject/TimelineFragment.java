package cscb07.group4.androidproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.FirebaseDatabase;

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
            if (course != null) {
                addTimelineCourse(course);
            }
        }
    }

    private int addTimelineCourse(Course course) {
        if (course.getSessions().isEmpty()) {
            return -1;
        }

        int session = -1;
        if (course.getPrerequisites() != null) {
            for (String prereqCourseCode : course.getPrerequisites()) {
                // Must add prerequisite course first if it hasn't been taken
                if (!StudentCourseManager.getInstance().getTakenCourses().contains(prereqCourseCode)) {
                    Course prereqCourse = CourseManger.getInstance().getCourseByID(prereqCourseCode);
                    session = Math.max(session, addTimelineCourse(prereqCourse));
                }
            }
            // Add the course after all of its prerequisite courses
            session = getNextSession(session);

        } else {
            // If no prerequisites, add it to the first session
            session = timeline.firstKey();
        }

        // Increment the session until there is room & the course is offered
        while (timeline.get(session).size() > 6 ||
                !course.getSessions().contains(Session.values()[session % 3])) {
            session = getNextSession(session);
        }
        timeline.get(session).add(course);
        return session;
    }

    private int getNextSession(int session) {
        int nextSession = session + 1;
        if (!timeline.containsKey(nextSession)) {
            timeline.put(nextSession, new HashSet<>());
        }
        return nextSession;
    }
}
