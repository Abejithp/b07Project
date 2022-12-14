package cscb07.group4.androidproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

import cscb07.group4.androidproject.databinding.DialogAddCourseAdminBinding;
import cscb07.group4.androidproject.manager.Course;
import cscb07.group4.androidproject.manager.CourseManger;
import cscb07.group4.androidproject.manager.Session;

public class AddAdminCourseDialogFragment extends DialogFragment {

    private DialogAddCourseAdminBinding binding;

    private Course course;
    private CourseChangeCallback callback;

    public AddAdminCourseDialogFragment(Course course, CourseChangeCallback callback){
        this(callback);
        this.course = course.copy();
    }

    public AddAdminCourseDialogFragment(CourseChangeCallback callback) {
        this.course = new Course();
        this.course.setId(UUID.randomUUID().toString());
        this.callback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_course_admin, null);
        binding = DialogAddCourseAdminBinding.bind(view);
        AlertDialog dialog = new AlertDialog.Builder(requireContext(), R.style.EndgameDialog)
                .setView(view).create();

        binding.closeButton.setOnClickListener(view1 -> dismiss());

        binding.courseNameEditText.setText(this.course.getName());
        binding.courseNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                AddAdminCourseDialogFragment.this.course.setName(s.toString());
            }
        });

        binding.courseCodeEditText.setText(this.course.getCode());
        binding.courseCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                AddAdminCourseDialogFragment.this.course.setCode(s.toString());
            }
        });

        this.setupSessionButton(Session.FALL, binding.fallButton);
        this.setupSessionButton(Session.WINTER, binding.winterButton);
        this.setupSessionButton(Session.SUMMER, binding.summerButton);

        binding.addPrerequisiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Course course = CourseManger.getInstance()
                        .getCourseByCourseCode(binding.prerequisitesEditText.getText().toString());
                if (course == null) {
                    binding.prerequisitesEditText.setError("Course does not exist");
                    return;
                }

                if (course.getPrerequisites().contains(AddAdminCourseDialogFragment.this.course.getId())) {
                    binding.prerequisitesEditText.setError("Cannot add this prereq");
                    return;
                }

                AddAdminCourseDialogFragment.this.course.getPrerequisites().add(course.getId());
                binding.prerequisitesEditText.setText("");
                refreshPrerequisites();
            }
        });

        refreshPrerequisites();

        this.binding.addCourseButton2.setOnClickListener(v -> {
            if (course.getName() == null || course.getName().isEmpty()) {
                binding.courseNameEditText.setError("Empty Field");
                return;
            }
            else if(course.getCode() == null || course.getCode().isEmpty()){
                binding.courseCodeEditText.setError("Empty Field");
                return;
            }
            else if(course.getSessions().isEmpty()){
                binding.sessionsText.setError("Empty Field");
                return;
            }

            CourseManger manager = CourseManger.getInstance();
            if (manager.getCourseByID(course.getId()) != null) {
                manager.removeCourse(course.getId());
            }
            manager.addCourse(course);
            callback.onCourseChange(course);
            dismiss();
        });

        return dialog;
    }

    private void setupSessionButton(Session session, ToggleButton button) {
        button.setChecked(AddAdminCourseDialogFragment.this.course.getSessions().contains(session));
        button.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AddAdminCourseDialogFragment.this.course.getSessions().add(session);
            } else {
                AddAdminCourseDialogFragment.this.course.getSessions().remove(session);
            }
        });
    }

    public void refreshPrerequisites() {
        binding.prerequisitesLinearLayout.removeAllViews();
        for (String prereqID : course.getPrerequisites()) {
            Course course = CourseManger.getInstance().getCourseByID(prereqID);
            if (course == null) {
                continue;
            }

            LinearLayout buttonsLayout = new LinearLayout(this.getContext());
            buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);

            TextView prereqText = new TextView(this.getContext());
            prereqText.setText(course.getCode());
            prereqText.setTextSize(16.0f);
            buttonsLayout.addView(prereqText);

            ImageButton prereqRemoveButton = new ImageButton(this.getContext());
            prereqRemoveButton.setBackgroundResource(R.drawable.close_icon);
            prereqRemoveButton.setOnClickListener((view) -> {
                course.getPrerequisites().remove(prereqID);
                refreshPrerequisites();
            });
            buttonsLayout.addView(prereqRemoveButton);

            binding.prerequisitesLinearLayout.addView(buttonsLayout);
        }
    }
}
