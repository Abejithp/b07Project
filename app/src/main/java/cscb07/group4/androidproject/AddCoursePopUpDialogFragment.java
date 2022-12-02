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

public class AddCoursePopUpDialogFragment extends DialogFragment {

    Runnable onExit;
    List <String> courses;
    public AddCoursePopUpDialogFragment(List<String> courses, Runnable onExit){
        this.onExit = onExit;
        this.courses= courses;
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
                    for (String courseCode:selectedCourses) {
                        if(!courses.contains(courseCode))
                            courses.add(courseCode);
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
        for (Course course : CourseManger.getInstance().getCourses()) {
            if ((!course.getName().toLowerCase(Locale.ROOT).contains(searchQuery) &&
                    !course.getCode().toLowerCase(Locale.ROOT).contains(searchQuery)) ||
                    courses.contains(course.getCode())){
                continue;
            }

            LinearLayout courseLinearLayout = new LinearLayout(this.getContext());
            courseLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

            ToggleButton courseButton = new ToggleButton(this.getContext());
            courseButton.setTag(course.getCode());
            courseButton.setText(course.getName() + " | " + course.getCode());
            courseButton.setTextOff(course.getName() + " | " + course.getCode());
            courseButton.setTextOn(course.getName() + " | " + course.getCode());
            courseButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 200));

            courseButton.setOnClickListener(v -> {
                if (courseButton.isChecked()) {
                    selectedCourses.add((String) v.getTag());
                    courseButton.setBackgroundColor(Color.BLUE);
                } else {
                    courseButton.setBackgroundColor(Color.LTGRAY);
                    selectedCourses.remove(((String) v.getTag()));
                }
            });

            courseLinearLayout.addView(courseButton);
            binding.courseLinearLayout.addView(courseLinearLayout);
        }
    }

    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
    }
}
