package cscb07.group4.androidproject;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;
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

        if(StudentCourseManager.getInstance().getWantedCourses()!=null) {
            generateTimeline();
            for (Integer session : timeline.keySet()) {

                LinearLayout linearLayout = new LinearLayout(this.getContext());
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(1160, ViewGroup.LayoutParams.MATCH_PARENT));



                TextView textView = new TextView(this.getContext());

                int year = 2022 + (int) Math.ceil(session / 3.0);

                textView.setText(Session.values()[session % 3].getName() + " " + year);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    textView.setTextAppearance(R.style.SessionTheme);
                }
                linearLayout.addView(textView);
                if (timeline.get(session) != null && !timeline.get(session).isEmpty()) {
                    for (Course course : timeline.get(session)) {
                        TextView courseText = new TextView(this.getContext());
                        courseText.setText(course.getName());
                        linearLayout.addView(courseText);
                    }
                }

                binding.parentSession.addView(linearLayout);

            }
        }
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void generateTimeline() {
        timeline.clear();
        timeline.put(0, new HashSet<>()); // Fall 2022
        for (String courseID : StudentCourseManager.getInstance().getWantedCourses()) {
            Course course = CourseManger.getInstance().getCourseByID(courseID);
            if (course != null) {
                addTimelineCourse(course);
            }
        }
    }

    private int addTimelineCourse(Course course) {
        if (course.getSessions()==null || course.getSessions().isEmpty()) {
            return -1;
        }

        if (StudentCourseManager.getInstance().getTakenCourses().contains(course.getId())){
            return -1;
        }

        int session = -1;
        if (course.getPrerequisites() != null && !course.getPrerequisites().isEmpty()) {
            for (String prereqID : course.getPrerequisites()) {
                // Must add prerequisite course first if it hasn't been taken
                    Course prereqCourse = CourseManger.getInstance().getCourseByID(prereqID);
                    session = Math.max(session, addTimelineCourse(prereqCourse));

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
