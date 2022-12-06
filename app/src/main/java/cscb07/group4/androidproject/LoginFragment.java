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

    private LoginPresenter presenter = new LoginPresenter(this, AccountManager.getInstance());
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
                if (!presenter.checkEmail() || !presenter.checkPassword()) {
                    return;
                }

                presenter.login(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            NavHostFragment.findNavController(LoginFragment.this)
                                    .navigate(R.id.action_LoginFragment_to_EditCourseFragment);
                            Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        // TODO REMOVE BEFORE LAUNCH
        binding.becomeAdminButton.setOnClickListener(v -> {
            AccountManager.getInstance().becomeAdmin();
            NavHostFragment.findNavController(LoginFragment.this)
                    .navigate(R.id.action_LoginFragment_to_EditCourseFragment);
            Toast.makeText(getContext(), "Login Successful", Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    public String getEmail() {
        return binding.usernameTextFieldDesign.getEditText().getText().toString();
    }

    public String getPassword() {
        return binding.passwordTextFieldDesign.getEditText().getText().toString();
    }

    public void setEmailError(String error) {
        binding.usernameTextFieldDesign.setError(error);
    }

    public void setPasswordError(String error) {
        binding.passwordTextFieldDesign.setError(error);
    }
}