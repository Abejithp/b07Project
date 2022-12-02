package cscb07.group4.androidproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cscb07.group4.androidproject.databinding.FragmentDeleteCourseBinding;
import cscb07.group4.androidproject.manager.StudentCourseManager;

public class DeletePopUpFragment extends DialogFragment {

    String deleteCourseCode;
    CourseType type;
    Runnable onExit;

    public DeletePopUpFragment(String deleteCourseCode, CourseType type, Runnable onExit){
        this.deleteCourseCode = deleteCourseCode;
        this.type = type;
        this.onExit = onExit;
    }
    private FragmentDeleteCourseBinding binding;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_delete_course, null);
        binding = FragmentDeleteCourseBinding.bind(view);
        AlertDialog dialog = new AlertDialog.Builder(requireContext()).setView(view).create();
        binding.buttonYes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                type.deleteCourses(deleteCourseCode);
                onExit.run();
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