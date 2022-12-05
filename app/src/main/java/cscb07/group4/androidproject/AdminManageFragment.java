package cscb07.group4.androidproject;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cscb07.group4.androidproject.databinding.FragmentAdminCourseBinding;
import cscb07.group4.androidproject.manager.Course;
import cscb07.group4.androidproject.manager.CourseManger;

public class AdminManageFragment extends Fragment {

    private FragmentAdminCourseBinding binding;

    private RecyclerView.Adapter<AdminAdapter.AdminViewHolder> adapter;
    private ArrayList<Course> courseList = new ArrayList<>();
    private FirebaseAuth firebaseAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminCourseBinding.inflate(inflater, container, false);

        RecyclerView recyclerView = binding.coursesRecyclerView;
        recyclerView.setHasFixedSize(true);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("courses");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                courseList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Course course = snapshot.getValue(Course.class);
                    courseList.add(course);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ManageFragment", String.valueOf(error.toException()));
            }
        });

        binding.addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddAdminCourseDialogFragment(() -> {
                    courseList.clear();
                    courseList.addAll(CourseManger.getInstance().getCourses());
                    AdminManageFragment.this.adapter.notifyDataSetChanged();
                }).show(getChildFragmentManager(),"dialog_add_admin_course");
            }
        });

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AdminAdapter(courseList);
        recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
