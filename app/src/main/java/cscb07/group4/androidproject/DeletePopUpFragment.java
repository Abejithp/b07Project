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
import java.util.Set;

import cscb07.group4.androidproject.databinding.FragmentDeleteCourseBinding;

public class DeletePopUpFragment extends DialogFragment {


    private FragmentDeleteCourseBinding binding;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_delete_course, null);
        binding = FragmentDeleteCourseBinding.bind(view);
        AlertDialog dialog = new AlertDialog.Builder(requireContext()).setView(view).create();

        return dialog;
    }



    public void onResume() {
        super.onResume();

        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
    }
}