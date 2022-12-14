package cscb07.group4.androidproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import cscb07.group4.androidproject.databinding.FragmentAddCourseBinding;
import cscb07.group4.androidproject.databinding.FragmentManageBinding;
import cscb07.group4.androidproject.manager.AccountManager;
import cscb07.group4.androidproject.manager.Course;
import cscb07.group4.androidproject.manager.CourseManger;
import cscb07.group4.androidproject.manager.StudentCourseManager;

public class AddCoursePopUpDialogFragment extends DialogFragment {

    Runnable onExit;
    CourseType type;
    public AddCoursePopUpDialogFragment(CourseType type, Runnable onExit){
        this.onExit = onExit;
        this.type = type;
    }

    private FragmentAddCourseBinding binding;
    private Set<String> selectedCourses = new HashSet<>();
    private String searchQuery = "";
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_add_course, null);
        binding = FragmentAddCourseBinding.bind(view);
        AlertDialog dialog = new AlertDialog.Builder(requireContext()).setView(view).create();

        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                AddCoursePopUpDialogFragment.this.searchQuery = newText.toLowerCase(Locale.ROOT);
                AddCoursePopUpDialogFragment.this.refreshCourses();
                return false;
            }
        });
        binding.buttonAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AccountManager.getInstance().isLoggedIn())
                    for (String courseID:selectedCourses) {
                        if(!type.getCourses().contains(courseID))
                            type.addCourses(courseID);
                    }
                onExit.run();
                dismiss();
            }
        });
        binding.buttonClosePopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        refreshCourses();

        return dialog;
    }

    public void refreshCourses() {
        binding.courseLinearLayout.removeAllViews();
        TextView textView = new TextView(this.getContext());
        textView.setText("Course Has Been Added Or Course Does Not Exist");
        textView.setTextSize(24);
        textView.setTextColor(Color.BLACK);
        int count = 0;
        for (Course course : CourseManger.getInstance().getCourses()) {
            if ((!course.getName().toLowerCase(Locale.ROOT).contains(searchQuery) &&
                    !course.getCode().toLowerCase(Locale.ROOT).contains(searchQuery)) ||
                    CourseType.TAKEN.getCourses().contains(course.getId()) ||
                    CourseType.WANTED.getCourses().contains(course.getId())){
                continue;
            }
            count++;
            LinearLayout courseLinearLayout = new LinearLayout(this.getContext());
            courseLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

            ToggleButton courseButton = new ToggleButton(this.getContext());
            courseButton.setTag(course.getId());
            courseButton.setText(course.getName() + " | " + course.getCode());
            courseButton.setTextOff(course.getName() + " | " + course.getCode());
            courseButton.setTextOn(course.getName() + " | " + course.getCode());
            courseButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));

            courseButton.setOnClickListener(v -> {
                if (courseButton.isChecked()) {
                    selectedCourses.add((String) v.getTag());
                    courseButton.setBackgroundResource(R.color.colorPrimary);
                    courseButton.setTextColor(Color.WHITE);
                } else {
                    courseButton.setBackgroundColor(Color.LTGRAY);
                    courseButton.setTextColor(Color.BLACK);
                    selectedCourses.remove(((String) v.getTag()));
                }
            });
            courseLinearLayout.addView(courseButton);
            binding.courseLinearLayout.addView(courseLinearLayout);
        }
        if(count == 0){
            binding.courseLinearLayout.addView(textView);
        }
    }
    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
    }
}
