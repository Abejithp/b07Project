package cscb07.group4.androidproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import cscb07.group4.androidproject.databinding.FragmentDeleteCourseBinding;
import cscb07.group4.androidproject.manager.Course;
import cscb07.group4.androidproject.manager.CourseManger;

public class AdminDeletePopUpFragment extends DialogFragment {

    Course deleteCourse;
    private FragmentDeleteCourseBinding binding;
    Runnable onExit;

    public AdminDeletePopUpFragment(Course deleteCourseID, Runnable onExit){
        this.deleteCourse = deleteCourseID;
        this.onExit = onExit;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_delete_course, null);
        binding = FragmentDeleteCourseBinding.bind(view);
        AlertDialog dialog = new AlertDialog.Builder(requireContext()).setView(view).create();
        TextView textView = binding.courseCodeTextView;
        textView.setText("Delete " + deleteCourse.getCode());
        textView.setTextSize(24);
        textView.setTextColor(Color.BLACK);
        binding.buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseManger.getInstance().removeCourse(deleteCourse);
                onExit.run();
                dismiss();
            }
        });

        binding.buttonNo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return dialog;
    }

    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
    }
}