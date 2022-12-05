package cscb07.group4.androidproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import cscb07.group4.androidproject.databinding.FragmentLoginBinding;
import cscb07.group4.androidproject.manager.AccountManager;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_LoginFragment_to_RegisterFragment);
            }
        });


        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.usernameTextFieldDesign.getEditText().getText().toString();
                String pwd = binding.passwordTextFieldDesign.getEditText().getText().toString();

                // FirebaseAuth cannot take empty email/password
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
                    return;
                }

                AccountManager.getInstance().login(email, pwd, getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            binding.usernameTextFieldDesign.getEditText().setText(null);
                            binding.passwordTextFieldDesign.getEditText().setText(null);
                            NavHostFragment.findNavController(LoginFragment.this)
                                    .navigate(R.id.action_LoginFragment_to_EditCourseFragment);
                            Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            binding.passwordTextFieldDesign.setError("Incorrect Login");
                            Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });




            }
        });

        return root;
    }
}