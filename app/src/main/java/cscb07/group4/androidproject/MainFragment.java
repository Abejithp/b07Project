package cscb07.group4.androidproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import cscb07.group4.androidproject.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavHostFragment.findNavController(MainFragment.this)
                        .navigate(R.id.action_MainFragment_to_RegisterFragment);
            }
        });

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                NavHostFragment.findNavController(MainFragment.this)
                        .navigate(R.id.action_MainFragment_to_LoginFragment);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
