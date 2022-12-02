package cscb07.group4.androidproject;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Locale;

import cscb07.group4.androidproject.databinding.FragmentAddCourseBinding;
import cscb07.group4.androidproject.databinding.FragmentManageBinding;
import cscb07.group4.androidproject.manager.AccountManager;
import cscb07.group4.androidproject.manager.StudentCourseManager;

public class ManageFragment extends Fragment {
    private FragmentManageBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentManageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonAddTakenCourse.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new AddCoursePopUpDialogFragment(CourseType.TAKEN, new Runnable() {
                    @Override
                    public void run() {
                        refreshCourses(StudentCourseManager.getInstance().getTakenCourses(),
                                CourseType.TAKEN);
                    }
                }).show(getChildFragmentManager(),"dialog_select_course");
            }
        });

        binding.buttonAddWantedCourse.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new AddCoursePopUpDialogFragment(CourseType.WANTED, new Runnable() {
                    @Override
                    public void run() {
                        refreshCourses(StudentCourseManager.getInstance().getWantedCourses(),
                                CourseType.WANTED);
                    }
                }).show(getChildFragmentManager(),"dialog_select_course");
            }
        });

        StudentCourseManager.getInstance().refreshCourses(() -> {
            refreshCourses(CourseType.TAKEN.getCourses(), CourseType.TAKEN);
            refreshCourses(CourseType.WANTED.getCourses(), CourseType.WANTED);
        });
        return root;
    }

    public void refreshCourses(List<String> courses, CourseType type) {
        LinearLayout layout = type.getLayout(binding);
        layout.removeAllViews();
        if (AccountManager.getInstance().isLoggedIn()) {
            for (String courseCode : courses) {
                ConstraintLayout courseConstraintLayout = new ConstraintLayout(this.getContext());
                courseConstraintLayout.setId(View.generateViewId());
                courseConstraintLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
                ToggleButton courseButton = new ToggleButton(this.getContext());
                TextView courseName = new TextView(this.getContext());
                courseName.setId(View.generateViewId());
                courseName.setText(courseCode);
                courseName.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
                courseButton.setId(View.generateViewId());
                courseButton.setTag(courseCode);
                courseButton.setText("X");
                courseButton.setTextOff(courseCode);
                courseButton.setTextOn("Remove: " + courseCode);

                courseButton.setOnClickListener(v -> {
                    if (courseButton.isChecked()) {
                        courseButton.setBackgroundColor(Color.RED);
                        new DeletePopUpFragment((String)courseButton.getTag(), type,
                                () -> refreshCourses(type.getCourses(),type)).show(getChildFragmentManager(),"delete_course");

                    } else {
                        courseButton.setBackgroundColor(Color.LTGRAY);
                    }
                });

                courseConstraintLayout.addView(courseButton);
                courseConstraintLayout.addView(courseName);

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(courseConstraintLayout);

                constraintSet.connect(courseName.getId(), ConstraintSet.START, courseConstraintLayout.getId(),ConstraintSet.START);
                constraintSet.connect(courseName.getId(), ConstraintSet.TOP, courseConstraintLayout.getId(),ConstraintSet.TOP);

                constraintSet.connect(courseButton.getId(), ConstraintSet.END, courseConstraintLayout.getId(),ConstraintSet.END);
                constraintSet.connect(courseButton.getId(), ConstraintSet.TOP, courseConstraintLayout.getId(),ConstraintSet.TOP);

                constraintSet.applyTo(courseConstraintLayout);
                layout.addView(courseConstraintLayout);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
