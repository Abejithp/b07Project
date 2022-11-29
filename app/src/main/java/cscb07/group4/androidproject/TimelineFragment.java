package cscb07.group4.androidproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import cscb07.group4.androidproject.databinding.FragmentManageBinding;

public class TimelineFragment extends Fragment{
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Course> courseList;
    FirebaseAuth firebaseAuth;
    FragmentManageBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RecyclerView recyclerView;
        FirebaseDatabase database;
        DatabaseReference databaseReference;
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        firebaseAuth = FirebaseAuth.getInstance();
        courseList = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        adapter = new AdminAdapter(courseList);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("courses");

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

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new AdminAdapter(courseList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
