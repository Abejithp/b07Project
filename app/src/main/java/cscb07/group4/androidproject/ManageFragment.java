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

public class ManageFragment extends Fragment {
    private FragmentManageBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentManageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonAddTakenCourse.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new AddCoursePopUpDialogFragment(AccountManager.getInstance().getAccount().
                        getCourses_taken(), new Runnable() {
                    @Override
                    public void run() {
                        refreshCourses(AccountManager.getInstance().getAccount().getCourses_taken(),
                                binding.takenCourseLinearLayout);
                    }
                }).show(getChildFragmentManager(),"dialog_select_course");
            }
        });

        binding.buttonAddWantedCourse.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new AddCoursePopUpDialogFragment(AccountManager.getInstance().getAccount().
                        getCourses_wanted(), new Runnable() {
                    @Override
                    public void run() {
                        refreshCourses(AccountManager.getInstance().getAccount().getCourses_wanted(),
                                binding.wantedCourseLinearLayout);
                    }
                }).show(getChildFragmentManager(),"dialog_select_course");
            }
        });


        return root;
    }
    public void refreshCourses(List<String> courses, LinearLayout layout) {
        layout.removeAllViews();
        if (AccountManager.getInstance().isLoggedIn()) {
            for (String courseCode : courses) {
                ConstraintLayout courseConstraintLayout = new ConstraintLayout(this.getContext());
                courseConstraintLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
                ConstraintSet constraintSet = new ConstraintSet();


//              LinearLayout courseLinearLayout = new LinearLayout(this.getContext());
//              courseLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

                ToggleButton courseButton = new ToggleButton(this.getContext());

                TextView courseName = new TextView(this.getContext());
                courseName.setText(courseCode);
                courseName.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));
                courseButton.setTag(courseCode);
                courseButton.setText(courseCode);
                courseButton.setTextOff(courseCode);
                courseButton.setTextOn("Remove: " + courseCode);

                courseButton.setOnClickListener(v -> {
                    if (courseButton.isChecked()) {
                        courseButton.setBackgroundColor(Color.RED);
                        new DeletePopUpFragment().show(getChildFragmentManager(),"delete_course");

                    } else {
                        courseButton.setBackgroundColor(Color.LTGRAY);
                    }
                });
                constraintSet.connect(courseName.getId(), ConstraintSet.START, courseConstraintLayout.getId(),ConstraintSet.START);
                constraintSet.connect(courseName.getId(), ConstraintSet.TOP, courseConstraintLayout.getId(),ConstraintSet.TOP);

                constraintSet.connect(courseName.getId(), ConstraintSet.END,courseButton.getId(),ConstraintSet.END);

                constraintSet.connect(courseButton.getId(), ConstraintSet.END, courseConstraintLayout.getId(),ConstraintSet.END);
                constraintSet.connect(courseButton.getId(), ConstraintSet.TOP, courseConstraintLayout.getId(),ConstraintSet.TOP);
                constraintSet.applyTo(courseConstraintLayout);
                courseConstraintLayout.addView(courseButton);
                courseConstraintLayout.addView(courseName);
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
